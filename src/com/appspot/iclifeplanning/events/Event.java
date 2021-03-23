package com.appspot.iclifeplanning.events;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.appspot.analyser.BaseCalendarSlot;
import com.appspot.analyser.ICalendarSlot;
import com.appspot.analyser.IEvent;
import com.appspot.analyser.Pair;
import com.appspot.datastore.SphereName;
import com.google.gdata.data.calendar.CalendarEventEntry;

// TODO (amadurska): Ensure keywords come from both title & description
public class Event extends BaseCalendarSlot implements IEvent {
	private Set<String> keywords = new HashSet<String>();
	private Set<Event> childEvents;
	private CalendarEventEntry calendarEventEntry;
	private String id;
	private boolean canReschedule;
	private boolean isRecurring;
	private Pair<Double, Double> durationInterval;

	public Event(CalendarEventEntry calendarEventEntry) {
		super(calendarEventEntry);
		this.calendarEventEntry = calendarEventEntry;
		childEvents = null;
		id = calendarEventEntry.getId();
		canReschedule = calendarEventEntry.getCanEdit();
		isRecurring = calendarEventEntry.getRecurrence() != null;
		parseKeywords(title);
		durationInterval = new Pair<Double, Double>(minDuration(), maxDuration());
	}

	public Event(long startTime, long endTime) {
		this.startDate = startTime;
		this.endDate = endTime;
		durationInterval = new Pair<Double, Double>(minDuration(), maxDuration());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private void parseKeywords(String title) {
		String[] words = title.split("[\\s]+");
		for(int i = 0; i < words.length; i++) {
			if (isKeyword(words[i])) {
				keywords.add(words[i]);
		