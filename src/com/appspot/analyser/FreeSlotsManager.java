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
	
	//Check whether ther