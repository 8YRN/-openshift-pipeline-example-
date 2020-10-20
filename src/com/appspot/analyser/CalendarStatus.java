
package com.appspot.analyser;

import java.util.*;

import com.appspot.datastore.SphereInfo;
import com.appspot.datastore.SphereName;

/* Keeps status of a calendar */
public class CalendarStatus implements Comparable<CalendarStatus> {
	private IEvent event;
	private double additionalEventTime;
	private double coefficient;
	private double userBusyTime;
	private List<CalendarStatus> alternatives;
	public FreeSlotsManager slotsManager;
	private Map<SphereName, SphereInfo> sphereResults;
	private boolean containsProposal;
	private boolean successful;

	public CalendarStatus(double userBusyTime, Map<SphereName, SphereInfo> currentSphereResults, List<BaseCalendarSlot> freeSlots) {
		event = null;
		this.userBusyTime = userBusyTime;
		sphereResults = new HashMap<SphereName, SphereInfo>();
		copySphereResults(currentSphereResults);
		setCurrentCoefficient();
		slotsManager = new FreeSlotsManager(freeSlots, this);
	}

	public CalendarStatus(Proposal proposal, CalendarStatus other, List<BaseCalendarSlot> freeSlots, List<BaseCalendarSlot> possibleSlots) {
		copyOtherCalendar(other);
		this.event = proposal;
		recordProposal();
		// after recording minimum duration we improved our status and it is
		// worth analysing
		if (this.compareTo(other) < 0) {
			successful = true;
			analyse();
		}
		slotsManager = new FreeSlotsManager(freeSlots, possibleSlots, this);
		containsProposal = true;
	}

	public CalendarStatus(IEvent event, CalendarStatus other) {
		copyOtherCalendar(other);
		this.event = event;
		slotsManager = new FreeSlotsManager(Utilities.copyFreeSlots(other.getFreeSlotsManager().getFreeSlots()), this);
		analyse();
	}

	public CalendarStatus() {
		// TODO Auto-generated constructor stub
	}

	private void copyOtherCalendar(CalendarStatus other) {
		this.userBusyTime = other.getUserBusyTime();
		sphereResults = new HashMap<SphereName, SphereInfo>();
		copySphereResults(other.getSphereResults());
		coefficient = other.getCoefficient();
	}

	private void recordProposal() {
		saveSphereInfos(event.getDuration());
		setCurrentCoefficient();
		additionalEventTime = 0;
	}
	
	
	//while analysing current event, remove influence of other event to the calendar
	private void deleteOtherInfluence(Map<SphereName, Double> influences, double additionalTime){
		userBusyTime += additionalTime;
		double oldTime = additionalEventTime;
		for (SphereName sphere : influences.keySet()) {
			double extraSphereTime = influences.get(sphere) * additionalTime;
			sphereResults.get(sphere).saveResults(extraSphereTime, this.userBusyTime);
		}
		if(this.containsProposal){
			recordProposal();
		}
		additionalEventTime = 0;
		this.saveSphereInfos(oldTime);
		setCurrentCoefficient();
	}
	
	//set maximum duration for event based on current calendar
	private void checkMaxDuration(){
		slotsManager.updateEventMaxDuration();
		if(this.hasAlternatives()){
			for(CalendarStatus alternative : alternatives)
				alternative.checkMaxDuration();
		}
	}
	
	private void reAnalyse(){
		successful = false;
		analyse();
	}
	
	//Recalculate coefficient of calendar based on the other calendar (with updated event)
	public Pair<List<CalendarStatus>, List<CalendarStatus>> recalculate(CalendarStatus other) {
		List<CalendarStatus> successes = new LinkedList<CalendarStatus>();
		List<CalendarStatus> fails = new LinkedList<CalendarStatus>();
		//Set same free slots for event and alternatives
		if(!slotsManager.getFreeSlots().equals(other.getFreeSlots())){
			setFreeSlots(other.getFreeSlots());
			if(this.hasAlternatives()){
				for(CalendarStatus alternative : alternatives)
					alternative.setFreeSlots(this.getFreeSlots());
			}
			checkMaxDuration();
		}
		double eventDuration = additionalEventTime;
		if(this.containsProposal)
			eventDuration += event.getDuration();
		//Copy results from other calendar and reanalyse
		copyOtherCalendar(other);
		reAnalyse();
		//if we are successful, add to successes
		if(successful)
			successes.add(this);
		else
			fails.add(this);
		if(this.hasAlternatives()){
			for(CalendarStatus alternative : alternatives){
				//Similarly for alternatives - delete influences of main event and reanalyse
				alternative.copyOtherCalendar(other);
				alternative.deleteOtherInfluence(event.getSpheres(), -eventDuration);
				alternative.reAnalyse();
				if(alternative.hasImproved())
					successes.add(alternative);
				else
					fails.add(alternative);
			}
		}
		//clearing alternatives - will be rebuilt in analyser
		clearAlternatives();
		List<CalendarStatus> removed = new LinkedList<CalendarStatus>();
		//Set two final lists - successes (based on best coefficient) and failures
		if(!successes.isEmpty()){
			Collections.sort(successes);
			CalendarStatus best = successes.get(0);
			//remove successes which are far away from our best
			for(CalendarStatus success : successes){
				if(success.getCoefficient() > 0.05 && success.getCoefficient() > best.getCoefficient()*(1+Analyser.ALTERNATIVE))
					removed.add(success);
			}
			successes.removeAll(removed);
			for(CalendarStatus failure : fails){
				//Add failures which are still close to our best calendar
				if(failure.getCoefficient() > 0.05 && failure.getCoefficient() > best.getCoefficient()*(1+Analyser.ALTERNATIVE))
					removed.add(failure);
				else
					successes.add(failure);
			}
		}
		else
			successes.addAll(fails);
		return new Pair<List<CalendarStatus>, List<CalendarStatus>>(successes, removed); 
	}


	private void clearAlternatives() {
		if(alternatives != null)
			alternatives.clear();
	}

	private void setFreeSlots(List<BaseCalendarSlot> freeSlots) {
		slotsManager.setFreeSlots(freeSlots);		
	}

	private FreeSlotsManager getFreeSlotsManager() {
		return slotsManager;
	}

	//Copy results for each sphere
	private void copySphereResults(Map<SphereName, SphereInfo> currentSphereResults) {
		for (SphereName name : currentSphereResults.keySet()) {
			SphereInfo currentInfo = currentSphereResults.get(name);
			sphereResults.put(name, new SphereInfo(currentInfo.getCurrentRatio(), currentInfo.getTargetRatio(), currentInfo.getSphereTotalTime()));
		}
	}

	public Map<SphereName, SphereInfo> getSphereResults() {
		return sphereResults;
	}

	public void setSphereResults(Map<SphereName, SphereInfo> sphereResults) {
		this.sphereResults = sphereResults;
	}