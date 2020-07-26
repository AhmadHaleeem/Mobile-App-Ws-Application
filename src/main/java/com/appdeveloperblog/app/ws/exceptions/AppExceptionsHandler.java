package com.appdeveloperblog.app.ws.exceptions;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.appdeveloperblog.app.ws.ui.model.response.ErrorMessage;

@RestControllerAdvice
public class AppExceptionsHandler {

	@ExceptionHandler(value = { UserServiceException.class })
	public ResponseEntity<Object> handleUserServiceExcpetion(UserServiceException ex, WebRequest request) {

		ErrorMessage errorMessage = new ErrorMessage(new Date(), ex.getMessage());

		return new ResponseEntity<Object>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = { NullPointerException.class })
	public ResponseEntity<Object> handleOtherExceptions(Exception ex, WebRequest request) {
		ErrorMessage errorMessage = new ErrorMessage(new Date(), ex.getMessage());

		return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
