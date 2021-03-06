package com.appdeveloperblog.app.ws.ui.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appdeveloperblog.app.ws.exceptions.UserServiceException;
import com.appdeveloperblog.app.ws.service.AddressService;
import com.appdeveloperblog.app.ws.service.UserService;
import com.appdeveloperblog.app.ws.shared.Roles;
import com.appdeveloperblog.app.ws.shared.dto.AddressDTO;
import com.appdeveloperblog.app.ws.shared.dto.UserDto;
import com.appdeveloperblog.app.ws.ui.model.request.PasswordResetModel;
import com.appdeveloperblog.app.ws.ui.model.request.PasswordResetRequestModel;
import com.appdeveloperblog.app.ws.ui.model.request.UserDetailsRequestModel;
import com.appdeveloperblog.app.ws.ui.model.response.AddressesRest;
import com.appdeveloperblog.app.ws.ui.model.response.ErrorMessages;
import com.appdeveloperblog.app.ws.ui.model.response.OperationStatusModel;
import com.appdeveloperblog.app.ws.ui.model.response.RequestOperationStatus;
import com.appdeveloperblog.app.ws.ui.model.response.UserRest;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(path = "/users")
//@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:8083"})
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private AddressService addressService;

	@PostAuthorize("returnObject.userId == principal.userId")
	@ApiOperation(value = "Get User Details Web Service Endpoint", notes = "${userController.GetUser.ApiOperation.Notes}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", paramType = "header")
	})
	@GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserRest getUser(@PathVariable String id) {
		UserRest returnValue = new UserRest();
		UserDto userDto = userService.getUserByUserId(id);

		ModelMapper modelMapper = new ModelMapper();
		//BeanUtils.copyProperties(userDto, returnValue);
		returnValue = modelMapper.map(userDto, UserRest.class);
		return returnValue;
	}

	@ApiImplicitParams({
		@ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", paramType = "header")
	})
	@PostMapping(consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetailsRequestModel) throws Exception {
		UserRest returnValue = new UserRest(); // response

		if (userDetailsRequestModel.getFirstName().isEmpty())
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRE_FIELD.getErrorMessage());

		//the old way
		//UserDto userDto = new UserDto(); // user transform
		//BeanUtils.copyProperties(userDetailsRequestModel, userDto); // to transform the request

		// the new way
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userDetailsRequestModel, UserDto.class);
		userDto.setRoles(new HashSet<>(Arrays.asList(Roles.ROLE_USER.name())));
		
		UserDto createdUser = userService.createUser(userDto); // createUser
		//BeanUtils.copyProperties(createdUser, returnValue); // to transform the response
		returnValue = modelMapper.map(createdUser, UserRest.class);
		
		return returnValue;
	}

	@ApiImplicitParams({
		@ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", paramType = "header")
	})
	@PutMapping(path = "/{id}", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {
		UserRest returnValue = new UserRest();

		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);

		UserDto updatedUser = userService.updateUser(id, userDto);
		BeanUtils.copyProperties(updatedUser, returnValue);

		return returnValue;
	}

	//@PreAuthorize("hasRole('ROLE_ADMIN') or #id == principal.userId")
	//@PreAuthorize("hasAuthority('DELETE_AUTHORITY')")
	//@Secured("ROLE_ADMIN")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", paramType = "header")
	})
	@DeleteMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public OperationStatusModel deleteUser(@PathVariable String id) {
		OperationStatusModel returnValue = new OperationStatusModel();

		returnValue.setOperationName(RequestOperationName.DELETE.name());

		userService.deleteUserByUserId(id);

		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());

		return returnValue;
	}

	
	@ApiImplicitParams({
		@ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", paramType = "header")
	})
	@GetMapping(produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "limit", defaultValue = "2") int limit) {
		List<UserRest> returnValue = new ArrayList<>();
		ModelMapper mapper = new ModelMapper();
		
		List<UserDto> users = userService.getUsers(page, limit);
		
		for (UserDto userDto : users) {
			//UserRest userModel = new UserRest();
			//BeanUtils.copyProperties(userDto, userModel);
			UserRest userModel = mapper.map(userDto, UserRest.class);
			returnValue.add(userModel);
		}
		
		return returnValue;
	}

	@ApiImplicitParams({
		@ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", paramType = "header")
	})
	@GetMapping(path = "/{id}/addresses", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public List<AddressesRest> getUserAddresses(@PathVariable String id) {
		List<AddressesRest> addressesListRestModel = new ArrayList<>();
		
		List<AddressDTO> addressesDTO = addressService.getAddresses(id);
		
		ModelMapper modelMapper = new ModelMapper();
		
		if (addressesDTO != null && !addressesDTO.isEmpty()) {
			Type listType = new TypeToken<List<AddressesRest>>() {}.getType();
			addressesListRestModel = modelMapper.map(addressesDTO, listType);
			
			for (AddressesRest addressRest : addressesListRestModel) {
				Link addressLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddress(id, addressRest.getAddressId())).withSelfRel();
				
				Link userLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUser(id)).withRel("user");
				addressRest.add(addressLink);
				addressRest.add(userLink);
			}
			
		} else {
			throw new UserServiceException("No Addresses found related to this particular user ID");
		}
		
		return addressesListRestModel;
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", paramType = "header")
	})
	@GetMapping(path = "/{userId}/addresses/{addressId}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public AddressesRest getUserAddress(@PathVariable String userId, @PathVariable String addressId) {
		
		AddressDTO addresses = addressService.getAddress(addressId);
		
		// with linkTo
		//Link addressLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(userId).slash("addresses").slash(addressId).withSelfRel();
		//Link userLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(userId).withRel("user");
		
		// with 
		Link addressLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddress(userId, addressId)).withSelfRel();
		Link userLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUser(userId)).withRel("user");
		Link addressesLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddresses(userId)).withRel("addresses");
		
		AddressesRest addressesRestModel = new ModelMapper().map(addresses, AddressesRest.class);
		addressesRestModel.add(addressLink);
		addressesRestModel.add(userLink);
		addressesRestModel.add(addressesLink);
		
		return addressesRestModel;
	}
	
	/*
     * http://localhost:8080/mobile-app-ws/users/email-verification?token=sdfsdf
     * */
    @GetMapping(path = "/email-verification", produces = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE })
    public OperationStatusModel verifyEmailToken(@RequestParam(value = "token") String token) {

        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.VERIFY_EMAIL.name());
        
        boolean isVerified = userService.verifyEmailToken(token);
        
        if(isVerified) {
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        } else {
            returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
        }

        return returnValue;
    }

    /*
     * http://localhost:8080/mobile-app-ws/users/password-reset-request
     * */
    @PostMapping(path = "/password-reset-request",
    		consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },
    		produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public OperationStatusModel requestReset(@RequestBody PasswordResetRequestModel passwordResetRequestModel) throws Exception {
    	
    	OperationStatusModel returnValue = new OperationStatusModel();
    	boolean operationResult = userService.requestPasswordReset(passwordResetRequestModel.getEmail());
    	
    	returnValue.setOperationName(RequestOperationName.REQUEST_PASSWORD_RESET.name());
    	returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
    	
    	if (operationResult) {
    		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
    	}
    	
    	return returnValue;
    }
    
    @PostMapping(path = "/password-reset",
    		consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },
    		produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public OperationStatusModel resetPassword(@RequestBody PasswordResetModel passwordResetModel) throws Exception {
    	OperationStatusModel returnValue = new OperationStatusModel();
    	
    	boolean operationResult = userService.resetPassword(passwordResetModel.getToken(), passwordResetModel.getPassword());
    	
    	returnValue.setOperationName(RequestOperationName.PASSWORD_RESET.name());
    	returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
    	
    	 if(operationResult) {
             returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
         }
    	
    	return returnValue;
    }
}
