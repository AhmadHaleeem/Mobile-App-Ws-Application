package com.appdeveloperblog.app.ws.io.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.appdeveloperblog.app.ws.io.entity.AddressEntity;
import com.appdeveloperblog.app.ws.io.entity.UserEntity;
import com.appdeveloperblog.app.ws.io.repositories.UserRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserRepositoryTest {

	@Autowired
	UserRepository userRepository;
	
	static boolean recordsCreated = false;

	@BeforeEach
	void setUp() throws Exception {
		if (!recordsCreated) {
			createRecords();
		}
	}

	@Test
	void testGetVerifiedUsers() {
		Pageable pageableRequest = PageRequest.of(0, 2);
		Page<UserEntity> page = userRepository.findAllUsesWithConfirmedEmailAddress(pageableRequest);
		assertNotNull(page);

		List<UserEntity> userEntities = page.getContent();
		assertTrue(userEntities.size() == 2);
	}
	
	@Test
	void testGFindUserByFirstName() {
		String firstName = "Ahmad";
		List<UserEntity> users = userRepository.findUserByFirstName(firstName);
		assertNotNull(users);
		assertTrue(users.size() == 1);
		
		UserEntity userEntity = users.get(0);
		assert(userEntity.getFirstName().equals(firstName));
	}
	
	@Test
	void testFindUserByLastName() {
		String lastName = "haleem";
		List<UserEntity> users = userRepository.findUserByLastName(lastName);
		assertNotNull(users);
		assertTrue(users.size() == 1);
		
		UserEntity userEntity = users.get(0);
		assert(userEntity.getLastName().equals(lastName));
	}
	
	@Test
	void testFindUserByKeyword() {
		String keyword = "haleem";
		List<UserEntity> users = userRepository.findUserByKeyword(keyword);
		assertNotNull(users);
		assertTrue(users.size() == 1);
		
		UserEntity userEntity = users.get(0);
		assert(userEntity.getLastName().contains(keyword) || userEntity.getFirstName().contains(keyword));
	}
	
	@Test
	void testFindUserFirstNameLastNameKeyword() {
		String keyword = "lee";
		List<Object[]> users = userRepository.findUserFirstNameAndLastNameKeyword(keyword);
		assertNotNull(users);
		assertTrue(users.size() == 1);
		
		Object[] userEntity = users.get(0);
		String firstName = String.valueOf(userEntity[0]);
		String lastName = String.valueOf(userEntity[1]);
		
		assertNotNull(firstName);
		assertNotNull(lastName);
		
		System.out.println(firstName + " ,lastName is : " + lastName);
	}
	
	@Test
	void testUpdateUserEmailVerificationStatus() {
		boolean emailVerificationStatus = false;
		
		userRepository.updateUserEmailVerificationStatus(emailVerificationStatus, "32423ad");
		UserEntity storedUserDetails = userRepository.findByUserId("32423ad");
		
		boolean sotredEmailVerificationStatus = storedUserDetails.getEmailVerificationStatus();
		
		assertTrue(sotredEmailVerificationStatus == emailVerificationStatus);
	}
	
	@Test
	void testFindUserEntityByUserId() {
		String userId = "32423ad";
		
		UserEntity user = userRepository.findUserEntityByUserId(userId);
		assertNotNull(user);
		
		assertTrue(user.getUserId() == userId);
	}
	
	@Test
	void testGetUserEntityFullnameById() {
		String userId = "32423ad";
		
		List<Object[]> users = userRepository.getUserEntityFullnameById(userId);
		assertNotNull(users);
		
		assertTrue(users.size() == 1);
		
		Object[] userEntity = users.get(0);
		String firstName = String.valueOf(userEntity[0]);
		String lastName = String.valueOf(userEntity[1]);
		
		assertNotNull(firstName);
		assertNotNull(lastName);
		
		System.out.println(firstName + " ,lastName is : " + lastName);
		
		
	}

	@Test
	void testUpdateUserEntityEmailVerificationStatus() {
		boolean emailVerificationStatus = false;
		
		userRepository.updateUserEntityEmailVerificationStatus(emailVerificationStatus, "32423ad");
		UserEntity storedUserDetails = userRepository.findByUserId("32423ad");
		
		boolean sotredEmailVerificationStatus = storedUserDetails.getEmailVerificationStatus();
		
		assertTrue(sotredEmailVerificationStatus == emailVerificationStatus);
	}
	
	private void createRecords() {
		UserEntity userEntity = new UserEntity();
		userEntity.setId(1l);
		userEntity.setFirstName("Ahmad");
		userEntity.setLastName("haleem");
		userEntity.setUserId("32423ad");
		userEntity.setEncryptedPassword("xxx");
		userEntity.setEmail("ahmadhaleem1992@gmail.com");
		userEntity.setEmailVerificationStatus(true);

		AddressEntity addressEntity = new AddressEntity();
		addressEntity.setType("shipping");
		addressEntity.setAddressId("ahgyt74hfy");
		addressEntity.setCity("Vancouver");
		addressEntity.setCountry("Canada");
		addressEntity.setPostalCode("ABCCDA");
		addressEntity.setStreetName("123 Street Address");

		List<AddressEntity> addresses = new ArrayList<>();
		addresses.add(addressEntity);

		userEntity.setAddresses(addresses);

		userRepository.save(userEntity);

		// Prepare User Entity
		UserEntity userEntity2 = new UserEntity();
		userEntity2.setFirstName("Sergey");
		userEntity2.setLastName("Kargopolov");
		userEntity2.setUserId("1a2b3cddddd");
		userEntity2.setEncryptedPassword("xxx");
		userEntity2.setEmail("test@test.com");
		userEntity2.setEmailVerificationStatus(true);

		// Prepare User Addresses
		AddressEntity addressEntity2 = new AddressEntity();
		addressEntity2.setType("shipping");
		addressEntity2.setAddressId("ahgyt74hfywwww");
		addressEntity2.setCity("Vancouver");
		addressEntity2.setCountry("Canada");
		addressEntity2.setPostalCode("ABCCDA");
		addressEntity2.setStreetName("123 Street Address");

		List<AddressEntity> addresses2 = new ArrayList<>();
		addresses2.add(addressEntity2);

		userEntity2.setAddresses(addresses2);

		userRepository.save(userEntity2);
		
		recordsCreated = true;
	}

}
