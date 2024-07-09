package com.codewithprojects.dto;

import com.codewithprojects.enums.UserRole;

public class AuthenticationResponse {
	private String jwt;
	private UserRole userRole;
	private long userId;
	public String getJwt() {
		return jwt;
	}
	public void setJwt(String jwt) {
		this.jwt = jwt;
	}
	public UserRole getUserRole() {
		return userRole;
	}
	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	@Override
	public String toString() {
		return "AuthenticationResponse [jwt=" + jwt + ", userRole=" + userRole + ", userId=" + userId + "]";
	}
	
}
