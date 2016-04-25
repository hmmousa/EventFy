package com.CSUF.EventFy_Beans;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.ArrayList;
import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Events {

	@JsonIgnoreProperties(ignoreUnknown = true)
	private int eventId;
	@JsonIgnoreProperties(ignoreUnknown = true)
	private String eventName;
	@JsonIgnoreProperties(ignoreUnknown = true)
	private String eventType;
	@JsonIgnoreProperties(ignoreUnknown = true)
	private String eventLocation;
	@JsonIgnoreProperties(ignoreUnknown = true)
	private String eventCapacity;
	@JsonIgnoreProperties(ignoreUnknown = true)
	private String eventDescription;
	@JsonIgnoreProperties(ignoreUnknown = true)
	private String eventImageUrl;
	@JsonIgnoreProperties(ignoreUnknown = true)
	private String eventVisiblityMile;
	@JsonIgnoreProperties(ignoreUnknown = true)
	private String eventDate;
	@JsonIgnoreProperties(ignoreUnknown = true)
	private String eventTime;
	@JsonIgnoreProperties(ignoreUnknown = true)
	private String eventVisiblityTenure;
	@JsonIgnoreProperties(ignoreUnknown = true)
	private String eventIsVerified;
	// once capacity full event will invisible or if admin want to make it invisible on he feel response is enough
	@JsonIgnoreProperties(ignoreUnknown = true)
	private String eventIsVisible;
	@JsonIgnoreProperties(ignoreUnknown = true)
	private String eventAdmin;

	@JsonView(SignUp.class)
	@JsonIgnoreProperties(ignoreUnknown = true)
	// mapping for signUp - Events (users in events and  events by user)
	@JsonBackReference
    	private List<SignUp> userDetail = new ArrayList<SignUp>();


//	@JsonIgnoreProperties(ignoreUnknown = true)
//	// mapping for events - comments (users in events and  events by user)
//	@JsonBackReference
//     private List<Comments> comments = new ArrayList<Comments>();
	
	
	public int getEventID() {
		return eventId;
	}
	public void setEventID(int eventId) {
		this.eventId = eventId;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getEventLocation() {
		return eventLocation;
	}
	public void setEventLocation(String eventLocation) {
		this.eventLocation = eventLocation;
	}
	public String getEventCapacity() {
		return eventCapacity;
	}
	public void setEventCapacity(String eventCapacity) {
		this.eventCapacity = eventCapacity;
	}
	public String getEventDescription() {
		return eventDescription;
	}
	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}
	public String getEventImageUrl() {
		return eventImageUrl;
	}
	public void setEventImageUrl(String eventImageUrl) {
		this.eventImageUrl = eventImageUrl;
	}
	public String getEventVisiblityMile() {
		return eventVisiblityMile;
	}
	public void setEventVisiblityMile(String eventVisiblityMile) {
		this.eventVisiblityMile = eventVisiblityMile;
	}
	public String getEventDate() {
		return eventDate;
	}
	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}
	public String getEventTime() {
		return eventTime;
	}
	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}
	public String getEventVisiblityTenure() {
		return eventVisiblityTenure;
	}
	public void setEventVisiblityTenure(String eventVisiblityTenure) {
		this.eventVisiblityTenure = eventVisiblityTenure;
	}
	public String getEventIsVerified() {
		return eventIsVerified;
	}
	public void setEventIsVerified(String eventIsVerified) {
		this.eventIsVerified = eventIsVerified;
	}
	public String getEventIsVisible() {
		return eventIsVisible;
	}
	public void setEventIsVisible(String eventIsVisible) {
		this.eventIsVisible = eventIsVisible;
	}
	public String getEventAdmin(String string) {
		return eventAdmin;
	}
	public void setEventAdmin(String eventAdmin) {
		this.eventAdmin = eventAdmin;
	}
	public List<SignUp> getUserDetail() {
		return userDetail;
	}
	public void setUserDetail(List<SignUp> userDetail) {
		this.userDetail = userDetail;
	} 
	
}
