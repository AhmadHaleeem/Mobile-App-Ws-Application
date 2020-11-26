package com.appdeveloperblog.app.ws.security;

import com.appdeveloperblog.app.ws.SpringApplicationContext;

public class SecurityConstants {

	public static final long EXPIRATION_TIME = 864000000; // 10 days in milliseconds
	public static final long PASSWORD_RESET_EXPIRATION_TIME = 360000; // 1 hour in milliseconds
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String SING_UP_URL = "/users";
	// public static final String TOKEN_SECRET = "jf9i4jqu83nfl0";
	public static final String VERIFICATION_EMAIL_URL = "/users/email-verification";
	public static final String PASSWORD_RESET_REQUEST = "/users/password-reset-request";
	public static final String PASSWORD_RESET = "/users/password-reset";
	public static final String H2_CONSOLE = "/h2-console/**";
	public static final String DELETE_URL = "/users/**";

	public static String getTokenSecret() {
		AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("appProperties");
		return appProperties.getTokenSecret();
	}
}
