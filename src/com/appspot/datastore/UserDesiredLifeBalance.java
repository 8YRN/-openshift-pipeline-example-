package com.appspot.datastore;

import java.util.HashMap;

import javax.jdo.annotations.*;

@PersistenceCapable
public class UserDesiredLifeBalance extends BaseDataObject {
	@PrimaryKey
	@Persistent
	private String key;
	@Persistent
	private String userID;
	@Persistent
	priva