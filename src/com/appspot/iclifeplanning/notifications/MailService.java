package com.appspot.iclifeplanning.notifications;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

import com.appspot.analyser.Suggestion;
import com.appspot.datastore.SphereName;


public class MailService {
	public static ArrayList<Thread> users = new ArrayList<Thread>();
	private static final int time_slice = 10 * 60 * 1000; // 10 minutes
	
	public void sendEmail(String email, EmailContent content) {
		
		//Here, no Authenticator argument is used (it is null).
	    //Authent