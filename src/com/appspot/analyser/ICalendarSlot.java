package com.appspot.analyser;

import java.util.Calendar;

import com.google.gdata.data.TextConstruct;

public interface ICalendarSlot extends Comparable<ICalendarSlot> {

	public String getTitle();
	public void setTitle(TextConstruct title);
	public String getD