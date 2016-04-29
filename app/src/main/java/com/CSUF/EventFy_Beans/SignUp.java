package com.CSUF.EventFy_Beans;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_EMPTY)
public class SignUp implements Serializable{

	@JsonIgnoreProperties(ignoreUnknown = true)
	private String userId; // email or phone number
	@JsonIgnoreProperties(ignoreUnknown = true)
	private String password;
	@JsonIgnoreProperties(ignoreUnknown = true)
	private String userName; // display name
	@JsonIgnoreProperties(ignoreUnknown = true)
	private String dob;
	@JsonIgnoreProperties(ignoreUnknown = true)
	private String imageUrl;
	@JsonIgnoreProperties(ignoreUnknown = true)
	private String isFacebook;
	@JsonIgnoreProperties(ignoreUnknown = true)
	private String isVerified;


	@JsonView(Events.class)
	@JsonIgnoreProperties(ignoreUnknown = true)
	@JsonBackReference
	private List<Events> events = new ArrayList<Events>();


	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getIsFacebook() {
		return isFacebook;
	}
	public void setIsFacebook(String isFacebook) {
		this.isFacebook = isFacebook;
	}
	public String getIsVerified() {
		return isVerified;
	}
	public void setIsVerified(String isVerified) {
		this.isVerified = isVerified;
	}
	@JsonIgnoreProperties(ignoreUnknown = true)
	public List<Events> getEvents() {
		return events;
	}
	@JsonIgnoreProperties(ignoreUnknown = true)
	public void setEvents(List<Events> events) {
		this.events = events;
	}
}
