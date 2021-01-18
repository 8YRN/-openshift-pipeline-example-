
package com.appspot.iclifeplanning.authentication;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appspot.datastore.TokenStore;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.client.http.AuthSubUtil;
import com.google.gdata.util.AuthenticationException;

/**
 * Class responsible for managing the authentication within the application.
 * Uses Google account/password for token-based authentication. Majority of
 * issues are managed by Google services, mainly  by the UserService class.
 * 
 * @author Agnieszka Magda Madurska (amm208@doc.ic.ac.uk)
 *
 */
public class AuthService {

	/** AuthService instance for singleton-based design*/
	private static AuthService authServiceInstance = null;

	/**Service used to monitor the currently the users of the application*/
	private static UserService userService = UserServiceFactory.getUserService();

	/**Feed-url giving access to all calendars accesible by a give user*/
	public static final  String CALENDAR_FULL_FEED_REQUEST_URL 