package com.appdeveloperblog.app.ws.ui.model.request;

import java.io.Serializable;

public class PasswordResetModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8108428934696984428L;

	private String token;
	private String password;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
