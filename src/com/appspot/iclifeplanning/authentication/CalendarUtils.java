package com.appspot.iclifeplanning.authentication;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;

import com.appspot.analyser.Utilities;
import com.appspot.datastore.PMF;
import com.appspot.datastore.SphereName;
import com.appspot.datastore.Token;
import com.appspot.datastore.TokenStore;
import com.appspot.datastore.UserProfile;
import com.appspot.datastore.UserProfileStore;
import com.appspot.iclifeplanning.charts.utils.AddUserDataServlet;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.client.http.AuthSubUtil;
import com.google.gdata.data.Link;
import com.google.gdata.data.calendar.CalendarEntry;
import com.google.gdata.data.calendar.CalendarFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

/**
 * Class responsible for managing the authentication within the application.
 * Uses Google account/password for token-based authentication. Majority of
 * issues are managed by Google services, mainly by the UserService class.
 * 
 * @author Agnieszka Magda Madurska (amm208@doc.ic.ac.uk)
 * 
 */
public class CalendarUtils {

	/** AuthService instance for singleton-based design */
	private static CalendarUtils calendarUtilsInstance = null;

	/** Service used to monitor the currently the users of the application */
	private static UserService userService = UserServiceFactory.getUserService();

	/** Feed-url giving access to all calendars accesible by a give user */
	public static final String CALENDAR_FULL_FEED_REQUEST_URL = "http://www.google.com/calendar/feeds/default/allcalendars/full";

	public static final String DEFAULT_FULL_FEED_REQUEST_URL = "http://www.google.com/calendar/feeds/default";

	public static CalendarService client = new CalendarService("ic-lifeplanning-v1");

	/** Constructor for singleton pattern */
	private CalendarUtils() {
	}

	public static CalendarUtils getCalendarUtils() {
		if (calendarUtilsInstance == null) {
			calendarUtilsInstance = new CalendarUtils();
		}
		return calendarUtilsInstance;
	}

	public String getCalendarAccessUrl(String nextUrl) throws IOException {

		String requestUrl = AuthSubUtil.getRequestUrl(nextUrl, DEFAULT_FULL_FEED_REQUEST_URL, false, true);

		return requestUrl;
	}

	/** Stores token for the currentl