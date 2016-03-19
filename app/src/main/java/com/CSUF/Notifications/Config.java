package com.CSUF.Notifications;

public interface Config {

	// used to share GCM regId with application server - using php app server
	static final String APP_SERVER_URL = "";

	// GCM server using java
	// static final String APP_SERVER_URL =
	// "http://192.168.1.17:8080/GCM-App-Server/GCMNotification?shareRegId=1";

	// Google Project Number
	static final String GOOGLE_PROJECT_ID = "1070420205452";
	static final String MESSAGE_KEY = "message";

}
