package com.CSUF.EventFy_Beans;

import com.fasterxml.jackson.annotation.JsonBackReference;

public class Comments {


	private int commentId;
	private String userID; // ref to signup entity
	private String userName; // ref to signup entity
	private String commentText; 
	private String isImage;
	private String voteUp;
	private String voteDown;
	
	
	// mapping for events - comments (users in events and  events by user)
	@JsonBackReference
	private Events events;
	
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
