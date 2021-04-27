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
    	this.desiredLifeBalance = desiredLifeBalance;
    	this.currentLifeBalance = currentLifeBalance;
    	this.userName = userName;
    }

    public boolean isEmpty() {
    	return isEmpty;
    }

    public String toString() {
    	String title = "Hi " + userName + "!\n\n";
    	String openingLine = "This is a regular update from your Life Planning utility.";
    	String headerCurrent = "Your current life balance is: \n\n";
    	String currentLifeBalanceDescription = "";
    	for (Entry<SphereName, Double> sphere : currentLifeBalance.entrySet()) {
    		currentLifeBalanceDescription += sphere.getKey().toString();
    		currentLifeBalanceDescription += ": ";
    		currentLifeBalanceDescription += (int)(sphere.getValue() * 100);
    		currentLifeBalanceDescription += "%\n";
    	}
    	currentLifeBalanceDescription += "\n";
    	
    	String headerDesired = "Your desired life balance is: \n\n";
    	String desiredLifeBalanceDescription = "";
    	for (Entry<SphereName, Double> sphere : desiredLifeBalance.entrySet()) {
    		desiredLifeBalanceDescription += sphere.getKey().toString();
    		desiredLifeBalanceDescription += ": ";
    		desiredLifeBalanceDescription += (int)(sphere.getValue() * 100);
    		desiredLifeBa