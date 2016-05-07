package com.CSUF.EventFy_Beans;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Comments {

	@JsonIgnoreProperties(ignoreUnknown = true)
	private int commentId;
	@JsonIgnoreProperties(ignoreUnknown = true)
	private String userID; // ref to signup entity
	@JsonIgnoreProperties(ignoreUnknown = true)
	private String userName; // ref to signup entity
	@JsonIgnoreProperties(ignoreUnknown = true)
	private String commentText;
	@JsonIgnoreProperties(ignoreUnknown = true)
	private String isImage;
	@JsonIgnoreProperties(ignoreUnknown = true)
	private String voteUp;
	@JsonIgnoreProperties(ignoreUnknown = true)
	private String voteDown;
	@JsonIgnoreProperties(ignoreUnknown = true)
	private int eventId;

	// mapping for events - comments (users in events and  events by user)
	@JsonBackReference
	@JsonIgnoreProperties(ignoreUnknown = true)
	@JsonView(Events.class)
	private Events events;


	public Events getEvents() {
		return events;
	}

	public void setEvents(Events events) {
		this.events = events;
	}


	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}
	
	public int getCommentId() {
		return commentId;
	}
	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}

	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCommentText() {
		return commentText;
	}
	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}
	public String getIsImage() {
		return isImage;
	}
	public void setIsImage(String isImage) {
		this.isImage = isImage;
	}
	public String getVoteUp() {
		return voteUp;
	}
	public void setVoteUp(String voteUp) {
		this.voteUp = voteUp;
	}
	public String getVoteDown() {
		return voteDown;
	}
	public void setVoteDown(String voteDown) {
		this.voteDown = voteDown;
	}
	
}
