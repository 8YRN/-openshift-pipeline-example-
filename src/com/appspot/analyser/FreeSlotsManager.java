package com.appspot.analyser;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class FreeSlotsManager {

	private List<BaseCalendarSlot> freeSlots;
	private CalendarStatus status;

	public FreeSlotsManager(List<BaseCalendarSlot> freeSlots, CalendarStatus status) {
		super();
		setFreeSlots(freeSlots);
		this.status = status;
	}

	public FreeSlotsManager(List<BaseCalendarSlot> freeSlots, List<BaseCalendarSlot> possibleSlots, CalendarStatus status) {
		this(freeSlots, status);
		chooseSlot(possibleSlots);
	}
	
	public void setFreeSlots(List<BaseCalendarSlot> slots){
		freeSlots = slots;
	}
	
	//Check whether there are any possible slots to allocate event and create corresponding cirtual calendar
	public CalendarStatus checkProposal(Proposal proposal) {
		List<BaseCalendarSlot> possibleSlots = getPossibleSlots(proposal);
		if (possibleSlots != null) {
			return new CalendarStatus(proposal, status, Utilities.copyFreeSlots(freeSlots), possibleSlots);
		}
		return null;
	}
	
	//Generate all possible slots, taking into account max duration and possible time slot
	public List<BaseCalendarSlot> getPossibleSlots(Proposal proposal) {
		List<BaseCalendarSlot> ret = new LinkedList<BaseCalendarSlot>();
		Pair<Calendar, Calendar> possibleTimeSlot = proposal.getPossibleTimeSlot();
		Double minDuration = proposal.getDurationInterval().getFirst();
		Double maxDuration = proposal.getDurationInterval().getSecond();
		double nextDuration, currentMax = 0;
		Calendar slotStartDate, slotEndDate;
		for (BaseCalendarSlot freeSlot : freeSlots) {
			BaseCalendarSlot hourSlot = generateStartingSlot(freeSlot.getStartDate(), possibleTimeSlot);
			Calendar possibleStartDate = hourSlot.getStartDate();
			Calendar possibleEndDate = hourSlot.getEndDate();
			slotStartDate = freeSlot.getStartDate();
			slotEndDate = freeSlot.getEndDate();
			/* proposal fits inside the free slot */
			while (possibleStartDate.compareTo(slotEndDate) < 0) {
				if (possibleEndDate.compareTo(slotStartDate) > 0 && possibleStartDate.compareTo(slotEndDate) < 0) {

					Calendar start = Utilities.max(possibleStartDate, slotStartDate);

					Calendar tmp = new GregorianCalendar(start.get(Calendar.YEAR), start.get(Calendar.MONTH), start.get(Calendar.DAY_OF_MONTH), start
							.get(Calendar.HOUR_OF_DAY), start.get(Calendar.MINUTE), 0);
					tmp.add(Calendar.MINUTE, (int) ((double) maxDuration));

					Calendar endSlot = Utilities.min(possibleEndDate, slotEndDate);

					Calendar end = Utilities.min(tmp, endSlot);

					BaseCalendarSlot candidate = new BaseCalendarSlot("Best fit", null, start, end);
					nextDuration = candidate.getDuration();
					if (nextDuration > minDuration) {
						ret.add(candidate);
						if (nextDuration > currentMax)
							currentMax = nextDuration;
					}
				}
				possibleStartDate.add(Calendar.DAY_OF_MONTH, 1);
				possibleEndDate.add(Calendar.DAY_OF_MONTH, 1);
			}
		}
		if (!ret.isEmpty()) {
			if (currentMax > 0) {
				proposal.getDurationInterval().setSecond(currentMax);
			}
			Collections.sort(ret);
			return ret;
		}
		return null;
	}
	
	//Check whether our event's max duration has to be changed due to lack of free slots
	public void updateEventMaxDuration() {
		IEvent event = status.getEvent();
		Calendar endDate = event.getEndDate();
		if (!status.containsProposal()) {
			if (status.getAdditionalEventTime() > 0)
				endDate.add(Calendar.MINUTE, (int) status.getAdditionalEventTime());
			else
				return;
		}
		for(BaseCalendarSlot freeSlot : freeSlots){
			if(freeSlot.getStartDate().equals(endDate)){
				Pair<Double,Double> possibleDuration = event.getDurationInterval();
				possibleDuration.setSecond(Math.min(possibleDuration.getSecond(), 
						event.getDuration() + freeSlot.getDuration()));
				break;
			}
		}
	}
	
	//Check if any slots must disappear due to new events in calendar
	public void updateCurrentSlots(CalendarStatus stat) {
		IEvent takenSlot = stat.getEvent();
		Calendar takenStartDate = takenSlot.getStartDate();
		Calendar takenEndDate = takenSlot.getEndDate();
		if (!stat.containsProposal()) {
			if (stat.getAdditionalEventTime() > 0)
				takenEndDate.add(Calendar.MINUTE, (int) stat.getAdditionalEventTime());
			else
				return;
		}
		List<BaseCalendarSlot> toBeUpdated = new LinkedList<BaseCalendarSlot>();
		for (BaseCalendarSlot freeSlot : freeSlots) {
			Calendar slotStartDate = freeSlot.getStartDate();
			Calendar slotEndDate = freeSlot.getEndDate();
			if (takenEndDate.compareTo(slotStartDate) > 0 && takenStartDate.compareTo(slotEndDate) < 0)
				toBeUpdated.add(freeSlot);
		}
		for (BaseCalendarSlot slot : toBeUpdated) {
			Calendar slotStartDate = slot.getStartDate();
			Calendar slotEndDate = slot.getEndDate();
			Calendar start = Utilities.max(takenStartDate, slotStartDate);
			Calendar end = Utilities.min(takenEndDate, 