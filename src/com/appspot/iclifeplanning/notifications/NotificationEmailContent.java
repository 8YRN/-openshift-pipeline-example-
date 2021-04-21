package com.appspot.iclifeplanning.notifications;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.appspot.analyser.Suggestion;
import com.appspot.datastore.SphereName;

public class NotificationEmailContent implements EmailContent {

	private boolean isEmpty = true;
	private List<List<Suggestion>> suggestions;
    private HashMap<SphereName, Double> desiredLifeBalance;
    private HashMap<SphereName, Double> currentLifeBalance;
    private String userName;

    public NotificationEmailContent(List<List<Suggestion>> suggestions, 
    		HashMap<SphereName, Double> desiredLifeBalance, 
    		HashMap<SphereName, Double> currentLifeBalance, String userName) {
    	isEmpty = false;
    	this.suggestions = suggestions;
    	this.