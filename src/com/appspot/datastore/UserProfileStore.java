package com.appspot.datastore;

import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;

import com.appspot.iclifeplanning.charts.utils.WeeklyDataProfileStore;


/* Retrieve user profile from datastore */
public class UserProfileStore {

	public static UserProfile getUserProfile(String userID) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			UserProfile userProfile = pm.detachCopy(pm.getObjectById(UserProfile.class, userID));
			return userProfile;
		} 
		catch (JDOObjectNotFoundException e) {
			return null;
		} 
		finally {
			pm.close();
		}
	}

  
  /*
   * Returns all users. Useful for debugging.
   */
  public static List<UserProfile> getAllUserProfiles()
  {
  	PersistenceManager pm = PMF.get().getPersistenceManager();

    try
    {
    	List<UserProfile> userProfiles = (List<UserProfile>) pm.detachCopyAll((List<UserProfile>)
    			pm.newQuery("SELECT FROM " + UserProfile.class.getName() + " ORDER BY userID").execute());

      return userProfiles;
    } catch (JDOObjectNotFoundException e) {
      ret