
package com.appspot.iclifeplanning.charts.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import com.appspot.datastore.SphereName;
import com.appspot.datastore.UserProfile;
import com.appspot.datastore.UserProfileStore;

@SuppressWarnings("serial")
public class AddUserDataServlet extends HttpServlet
{
	// these parameters describe how far can the random spheres achievement be from the given values
	static double UNOPTIMISED_DISPERSION = 0.06;
	static double OPTIMISED_DISPERSION = 0.017;
	
	public void doGet(HttpServletRequest request_, HttpServletResponse response_) throws IOException
	{
		String p = request_.getParameter("action");
		if("addUser".equals(p))
		{
			doAddUser(request_, response_);