
package com.appspot.analyser;

import java.util.*;

import com.appspot.datastore.SphereInfo;
import com.appspot.datastore.SphereName;
import com.appspot.iclifeplanning.events.Event;

//Helper functions used across the application
public class Utilities {
	//Length of day in mins
	public static final int DAYLENGTH = 1440;
	
	public static double getDuration(Calendar start, Calendar end) {
		return (end.getTimeInMillis() - start.getTimeInMillis()) / 1000 / 60;
	}

	//Max and min of two calendars
	public static Calendar min(Calendar c1, Calendar c2) {
		if (c1.compareTo(c2) >= 0)
			return c2;