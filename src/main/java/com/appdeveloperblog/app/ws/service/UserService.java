package com.appdeveloperblog.app.ws.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.appdeveloperblog.app.ws.shared.dto.UserDto;

public interface UserService extends UserDetailsService {

	UserDto createUser(UserDto user);

	UserDto getUser(String email);

	UserDto getUserByUserId(String id);

	UserDto updateUser(String id, UserDto userDto);

	void deleteUserByUserId(String id);

	List<UserDto> getUsers(int page, int limit);

	boolean verifyEmailToken(String token);

	boolean requestPasswordReset(String email);

	boolean resetPassword(String token, String password);

	
}
