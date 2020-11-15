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
							.get(Calendar.HOUR_OF_DAY), start.get(Cal