package com.CSUF.EventFy_Beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SignUp{

	@JsonIgnoreProperties(ignoreUnknown = true)
	private String userName;
	//private String Password;
	@JsonIgnoreProperties(ignoreUnknown = true)
	private String email;
	@JsonIgnoreProperties(ignoreUnknown = true)
	private String DOB;
	//private String ImageUrl;


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public String getDOB() {
		return DOB;
	}

	public void setDOB(String DOB) {
		this.DOB = DOB;
	}




}