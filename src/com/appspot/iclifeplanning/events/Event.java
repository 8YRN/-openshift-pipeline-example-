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

	public Event(CalendarEventEntr