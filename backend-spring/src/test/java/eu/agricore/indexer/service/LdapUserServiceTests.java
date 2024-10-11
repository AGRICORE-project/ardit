package eu.agricore.indexer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Sets;

import eu.agricore.indexer.config.IndexerBaseTest;
import eu.agricore.indexer.ldap.dto.LdapUserDTO;
import eu.agricore.indexer.ldap.exception.UserAlreadyExistException;
import eu.agricore.indexer.ldap.model.LdapUser;
import eu.agricore.indexer.ldap.service.LdapUserService;


public class LdapUserServiceTests extends IndexerBaseTest{
	
	@Autowired
	private LdapUserService ldapService;
	
	@Test
	void loginSuccessfullyByUser() {
		LdapUser existingUser = createExistingUser();
		assertTrue(ldapService.authenticate(existingUser));
	}
	
	@Test
	void loginSuccessfullyByUsername() {
		LdapUser existingUser = createExistingUser();
		assertTrue(ldapService.authenticate(existingUser.getUsername(), existingUser.getPassword()));
	}
	
	@Test
	void loginUnsuccessfullyByUser() {
		LdapUser nonExisting  = createNewUser();
		assertFalse(ldapService.authenticate(nonExisting));
	}
	
	@Test
	void loginUnsuccessfullyByUsername() {
		LdapUser nonExisting = createNewUser();
		assertFalse(ldapService.authenticate(nonExisting.getUsername(), nonExisting.getPassword()));
	}
	
	@Test
	void createUser() {
		LdapUser user = createNewUser();
		ldapService.create(user);
		assertTrue(ldapService.authenticate(user));
		assertTrue(ldapService.find(user.getUsername()).isPresent());
		// TODO: We have to make LDAP @Transactional works
		ldapService.delete(user);
	}
	
	@Test
	void createUserThatExistsError() {
		LdapUser user = createExistingUser();
		Exception exception = assertThrows(UserAlreadyExistException.class, () -> {
			ldapService.create(user);
	    });
	 
	    String expectedMessage = "User already exists";
	    String actualMessage = exception.getMessage();
	 
	    assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	void deleteUserThatExists() {
		LdapUser existingUser = createExistingUser();
		String username = existingUser.getUsername();
		String password = existingUser.getPassword();
		Optional<LdapUser> findUserResult = ldapService.find(username);
		assertTrue(findUserResult.isPresent());
		LdapUser user = findUserResult.get();
		
		ldapService.delete(user);
		
		assertTrue(ldapService.find(username).isEmpty());
		
		// Rollback TODO: Must be done by @transactional
		user.setPassword(password);
		ldapService.create(user);
	}
	
	@Test
	void deleteUserThatDoesNotExist() {
		LdapUser newUser = createNewUser();
		Optional<LdapUser> findUserResult = ldapService.find(newUser.getUsername());
		assert(findUserResult.isEmpty());
		ldapService.delete(newUser);
	}
	
	@Test
	void findUserDoesNotExist() {
		Optional<LdapUser> user = ldapService.find("non-existentuser");
		assertTrue(user.isEmpty());
	}
	
	@Test
	void findUserThatExists() {
		LdapUser existingUser = createExistingUser();
		Optional<LdapUser> user = ldapService.find(existingUser.getUsername());
		assertTrue(user.isPresent());
		assertEquals(existingUser.getUsername(), user.get().getUsername());
		assertEquals(existingUser.getEmail(), user.get().getEmail());
	}

	@Test
	void getRolesOfUserThatExists() {
		LdapUser user = createExistingUser();
		Set<String> roles = ldapService.getRolesByUser(user.getUsername());
		Set<String> expected = Sets.newHashSet("USER");
		assertEquals(expected, roles);
	}
	
	@Test
	void updateUserThatExists() {
		LdapUser user = createExistingUser();
		LdapUserDTO userDTO = new LdapUserDTO(user);
		
		String oldEmail = userDTO.getEmail();
		String newEmail = "mynewemail@mail.com";
		
		// Update email operation
		userDTO.setEmail(newEmail);
		ldapService.update(userDTO);
		
		// Check modification
		Optional<LdapUser> optionalUser = ldapService.find(userDTO.getUsername());
		assertTrue(optionalUser.isPresent());
		assertEquals(newEmail, optionalUser.get().getEmail());
		
		// Rollback TODO: This must be performed automatically by transcational
		userDTO.setEmail(oldEmail);
		ldapService.update(userDTO);
		assertEquals(
				oldEmail
				, ldapService.find(userDTO.getUsername()).get().getEmail());	
	}
	
	@Test
	void updateUserPassword() {
		
		LdapUser user = createExistingUser();
		String oldPassword = user.getPassword();
		String newPassword = "testing";
		
		// Update password
		ldapService.updatePassword(user.getUsername(), newPassword);
		
		// Check the change
		Optional<LdapUser> optionalUser = ldapService.find(user.getUsername());
		assertTrue(optionalUser.isPresent());
		assertTrue(ldapService.authenticate(user.getUsername(), newPassword));
		
		// Rollback TODO: This must be performed automatically by transactional
		ldapService.updatePassword(user.getUsername(), oldPassword);
		assertTrue(ldapService.authenticate(user.getUsername(), oldPassword));
	}
	
	private static LdapUser createNewUser() {
		return new LdapUser("testuser", "testuser", "testuser@mail.com"); // Returns a user that does not exist in the database
	}
	
	private static LdapUser createExistingUser() {
		return new LdapUser("user1", "user1", "user1@gmail.com"); // Returns a user that already exists in the database
	}
}
