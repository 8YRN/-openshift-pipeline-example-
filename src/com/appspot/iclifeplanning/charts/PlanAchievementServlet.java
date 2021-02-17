package com.appspot.iclifeplanning.charts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.appspot.datastore.SphereName;
import com.appspot.datastore.UserProfile;
import com.appspot.datastore.UserProfileStore;
import com.appspot.iclifeplanning.charts.utils.DataToJSONConverter;
import com.appspot.iclifeplanning.charts.utils.WeeklyDataProfile;
import com.appspot.iclifeplanning.charts.utils.WeeklyDataProfileStore;

@SuppressWarnings("serial")
// Used to get data for the pie-chart representing user's life priorities
public class PlanAchievementServlet extends HttpServlet
{
	private final static int ACHIEVED = 0;
	private final static int PLANNED = 1;

	public void doGet(HttpServletRequest request_, HttpServletResponse response_)
			throws IOException
	{
		String userID = request_.getParameter("userName");
		UserProfile userProfile = UserProfileStore.getUserProfile(userID);
		if(userProfile==null)
		{
			return;
		}
		
		// Get data for all weeks
		List<WeeklyDataProfile> listOfAll