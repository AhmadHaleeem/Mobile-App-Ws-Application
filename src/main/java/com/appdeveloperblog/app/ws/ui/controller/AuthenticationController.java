package com.appdeveloperblog.app.ws.ui.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.appdeveloperblog.app.ws.ui.model.request.LoginRequestModel;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;

@RestController
public class AuthenticationController {

	
	@ApiOperation("UserLogin")
	@ApiResponses(value = {
			@ApiResponse(code = 200,
					message = "Response Headers",
					responseHeaders = {
							@ResponseHeader(name = "authorization",
									description = "Bearer <JWT value here>",
									response = String.class),
							@ResponseHeader(name = "userId",
							description = "<Public User Id value  here>",
							response = String.class)
					})
	})
	@PostMapping("/users/login")
	public void theFakeLogin(@RequestBody LoginRequestModel loginRequestModel) {
		throw new IllegalStateException("This method should not b called. This method is implemented by spring security");
	}
	
}
