package com.appspot.iclifeplanning.notifications;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.appspot.analyser.Suggestion;
import com.appspot.datastore.SphereName;

public class NotificationEmailContent implements EmailContent {

	private boolean isEmpty = true;
	private List<Lis