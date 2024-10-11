/*
package eu.agricore.indexer.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.agricore.indexer.config.IndexerMockTest;
import eu.agricore.indexer.ldap.dto.AuthRequest;
import eu.agricore.indexer.ldap.dto.CurrentUserCredentials;
import eu.agricore.indexer.ldap.dto.LdapUserDTO;
import eu.agricore.indexer.ldap.model.LdapUser;
import eu.agricore.indexer.ldap.service.LdapUserService;
import eu.agricore.indexer.util.CookieUtil;
import eu.agricore.indexer.util.JWTService;

public class IndexerAuthControllerTests extends IndexerMockTest {
	
	@Value("${cookie.name}")
    private String tokenCookieName;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private LdapUserService ldapService;
	
	@Autowired
	private JWTService jwtService;
	
	@Autowired
	private CookieUtil cookieUtil;
	
	
    @BeforeEach 
    void init() {
    	// TODO: This must be done automatically using @Transactional and @Rollback ?
    	LdapUser existingUser = createExistingUser();
    	ldapService.delete(existingUser);
    	ldapService.create(existingUser);
    	
    	LdapUser newUser = createNewUser();
    	ldapService.delete(newUser);
    }
	
    // TODO: pending account verification
	*/
/*@Test
	public void createUser() throws Exception {
		
		LdapUser newUser = createNewUser();
		
		mockMvc.perform(post(getApiAuthURL() + "register")
		        .contentType("application/json")
		        .content(objectMapper.writeValueAsString(newUser)))
		        .andExpect(status().isOk())
		        .andExpect(cookie().exists(tokenCookieName));
		
		assertTrue(ldapService.find(newUser.getUsername()).isPresent());
	}
	
	@Test
	public void createUserThatExists() throws Exception {
		
		LdapUser existingUser = createExistingUser();
		
		mockMvc.perform(post(getApiAuthURL() + "register")
		        .contentType("application/json")
		        .content(objectMapper.writeValueAsString(existingUser)))
		 		.andExpect(status().is4xxClientError());
	}*//*

	
	@Test
	public void loginSuccessfully() throws Exception {
			
		mockMvc.perform(post(getApiAuthURL() + "login")
		        .contentType("application/json")
		        .content(objectMapper.writeValueAsString(createAuthRequestForExistingUser())))
		        .andExpect(status().isOk())
		        .andExpect(cookie().exists(tokenCookieName));
	}
	
	@Test
	public void loginSuccessfullyCheckingResponse() throws Exception {
		
		mockMvc.perform(post(getApiAuthURL() + "login")
		        .contentType("application/json")
		        .content(objectMapper.writeValueAsString(createAuthRequestForExistingUser())))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Bearer ")));
	}
	
	@Test
	public void loginUnsuccessfulWithBadCredentials() throws Exception {
		
		mockMvc.perform(post(getApiAuthURL() + "login")
		        .contentType("application/json")
		        .content(objectMapper.writeValueAsString(createAuthRequestForNotExistingUser())))
		        .andExpect(status().is4xxClientError());
	}
	
	@Test
	public void logout() throws Exception {
		
		LdapUser existingUser = createExistingUser();
		Set<String> roles = ldapService.getRolesByUser(existingUser.getUsername());
		
		String token = jwtService.generateToken(existingUser.getUsername(), roles);
		
		mockMvc.perform(post(getApiAuthURL() + "logout").cookie(cookieUtil.createTokenCookie(token)))
			.andExpect(status().is2xxSuccessful());
	}
	
	@Test
	public void findUserThatExists() throws Exception {
		
		LdapUser existingUser = createExistingUser();
		Set<String> roles = ldapService.getRolesByUser(existingUser.getUsername());
		
		mockMvc.perform(get(getApiAuthURL() + "me").header(HttpHeaders.AUTHORIZATION, 
			"Bearer " + jwtService.generateToken(existingUser.getUsername(), roles)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("username", is(existingUser.getUsername())));
	}
	
	@Test
	public void findUserThatExistsUsingCookie() throws Exception {
		
		LdapUser existingUser = createExistingUser();
		Set<String> roles = ldapService.getRolesByUser(existingUser.getUsername());
		
		String token = "Bearer " + jwtService.generateToken(existingUser.getUsername(), roles);
		
		mockMvc.perform(get(getApiAuthURL() + "me").cookie(cookieUtil.createTokenCookie(token)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("username", is(existingUser.getUsername())));
	}
	
	@Test
	public void findUserThatDoesNotExist() throws Exception {
		
		mockMvc.perform(get(getApiAuthURL() + "me").header(HttpHeaders.AUTHORIZATION, getNotExistingUserToken()))
				.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void updateUserThatExists() throws Exception {
		
		LdapUser existingUser = createExistingUser();
		LdapUserDTO userDTO = new LdapUserDTO(existingUser);
		Set<String> roles = ldapService.getRolesByUser(existingUser.getUsername());
		
		String newEmail = "mynewemail@mail.com";
		userDTO.setEmail(newEmail);
		
		mockMvc.perform(put(getApiAuthURL() + "me")
			.contentType("application/json")
			.content(objectMapper.writeValueAsString(userDTO))
			.header(HttpHeaders.AUTHORIZATION, 
			"Bearer " + jwtService.generateToken(userDTO.getUsername(), roles)))
			.andExpect(status().isOk());
		
		assertEquals(newEmail, ldapService.find(userDTO.getUsername()).get().getEmail());
	}
	
	@Test
	public void updateUserThatExistsUsingCookie() throws Exception {
		
		LdapUser existingUser = createExistingUser();
		LdapUserDTO userDTO = new LdapUserDTO(existingUser);
		Set<String> roles = ldapService.getRolesByUser(existingUser.getUsername());
		
		String token = "Bearer " + jwtService.generateToken(existingUser.getUsername(), roles);
		
		String newEmail = "mynewemail@mail.com";
		userDTO.setEmail(newEmail);
		
		mockMvc.perform(put(getApiAuthURL() + "me")
			.contentType("application/json")
			.content(objectMapper.writeValueAsString(userDTO))
			.cookie(cookieUtil.createTokenCookie(token)))
			.andExpect(status().isOk());	
		
		assertEquals(newEmail, ldapService.find(userDTO.getUsername()).get().getEmail());
	}
	
	@Test
	public void updateUserThatDoesNotExist() throws Exception {
		
		LdapUser nonExistingUser = createNewUser();
		LdapUserDTO userDTO = new LdapUserDTO(nonExistingUser);
		Set<String> roles = ldapService.getRolesByUser(nonExistingUser.getUsername());
		
		String token = "Bearer " + jwtService.generateToken(nonExistingUser.getUsername(), roles);
		
		String newEmail = "mynewemail@mail.com";
		userDTO.setEmail(newEmail);
		
		mockMvc.perform(put(getApiAuthURL() + "me")
			.contentType("application/json")
			.content(objectMapper.writeValueAsString(userDTO))
			.cookie(cookieUtil.createTokenCookie(token)))
			.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void updateUserPassword() throws Exception {
		
		// To generate the token
		LdapUser existingUser = createExistingUser();
		Set<String> roles = ldapService.getRolesByUser(existingUser.getUsername());
		
		// Generate new credentials
		CurrentUserCredentials credentials = createCredentialsForExistingUser();
		
		mockMvc.perform(put(getApiAuthURL() + "me/password")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(credentials))
				.header(HttpHeaders.AUTHORIZATION, 
						"Bearer " + jwtService.generateToken(existingUser.getUsername(), roles)))
				.andExpect(status().isOk());
	}
	
	@Test
	public void updateNotExistingUserPassword() throws Exception {
		
		// To generate the token
		LdapUser nonExistingUser = createNewUser();
		Set<String> roles = ldapService.getRolesByUser(nonExistingUser.getUsername());
		
		// Generate new credentials
		CurrentUserCredentials credentials = createCredentialsForNotExistingUser();
		
		mockMvc.perform(put(getApiAuthURL() + "me/password")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(credentials))
				.header(HttpHeaders.AUTHORIZATION, 
						"Bearer " + jwtService.generateToken(nonExistingUser.getUsername(), roles)))
				.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void updateUserPasswordUsingCookie() throws Exception {
		
		// To generate the token
		LdapUser existingUser = createExistingUser();
		Set<String> roles = ldapService.getRolesByUser(existingUser.getUsername());
		String token = "Bearer " + jwtService.generateToken(existingUser.getUsername(), roles);
		
		// Generate new credentials
		CurrentUserCredentials credentials = createCredentialsForExistingUser();
		
		mockMvc.perform(put(getApiAuthURL() + "me/password")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(credentials))
				.cookie(cookieUtil.createTokenCookie(token)))
				.andExpect(status().isOk());
	}
	
	@Test
	public void updateUserPasswordNotMatching() throws Exception {
		
		// To generate the token
		LdapUser existingUser = createExistingUser();
		Set<String> roles = ldapService.getRolesByUser(existingUser.getUsername());
		String token = "Bearer " + jwtService.generateToken(existingUser.getUsername(), roles);
		
		// Generate new credentials
		CurrentUserCredentials credentials = createCredentialsForExistingUser();
		
		// Set new password confirm with a non-matching value
		credentials.setNewPasswordConfirm("not-matching-pass");
		
		mockMvc.perform(put(getApiAuthURL() + "me/password")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(credentials))
				.cookie(cookieUtil.createTokenCookie(token)))
				.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void updateUserPasswordBadCredentials() throws Exception {
		
		// To generate the token
		LdapUser existingUser = createExistingUser();
		Set<String> roles = ldapService.getRolesByUser(existingUser.getUsername());
		String token = "Bearer " + jwtService.generateToken(existingUser.getUsername(), roles);
		
		// Generate new credentials
		CurrentUserCredentials credentials = createCredentialsForExistingUser();
		
		// Set current password with a wrong value
		credentials.setCurrentPassword("bad-password");
		
		mockMvc.perform(put(getApiAuthURL() + "me/password")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(credentials))
				.cookie(cookieUtil.createTokenCookie(token)))
				.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void updateOtherUserPassword() throws Exception {
		
		// Test that a logged-in user cannot change the password of another existing user
		
		// Generate a token with a geiven user credentials
		LdapUser existingUser = createExistingUser2();
		Set<String> roles = ldapService.getRolesByUser(existingUser.getUsername());
		String token = "Bearer " + jwtService.generateToken(existingUser.getUsername(), roles);
		
		// Generate new credentials for a different user
		CurrentUserCredentials credentials = createCredentialsForExistingUser();
		
		mockMvc.perform(put(getApiAuthURL() + "me/password")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(credentials))
				.cookie(cookieUtil.createTokenCookie(token)))
				.andExpect(status().is4xxClientError());
	}
	
	*/
/*@Test
	public void deleteUserThatExists() throws Exception {
		
		LdapUser existingUser = createExistingUser();
		Set<String> roles = ldapService.getRolesByUser(existingUser.getUsername());
		
		mockMvc.perform(delete(getApiAuthURL() + "me")
				.header(HttpHeaders.AUTHORIZATION, 
				"Bearer " + jwtService.generateToken(existingUser.getUsername(), roles)))
		        .andExpect(status().is2xxSuccessful());
		
		assertTrue(ldapService.find(existingUser.getUsername()).isEmpty());
		
	}
	
	@Test
	public void deleteUserThatExistsUsingCookie() throws Exception {
		
		LdapUser existingUser = createExistingUser();
		Set<String> roles = ldapService.getRolesByUser(existingUser.getUsername());
		
		String token = "Bearer " + jwtService.generateToken(existingUser.getUsername(), roles);
		
		mockMvc.perform(delete(getApiAuthURL() + "me")
				.cookie(cookieUtil.createTokenCookie(token)))
		        .andExpect(status().is2xxSuccessful());
		
		assertTrue(ldapService.find(existingUser.getUsername()).isEmpty());
		
	}
	
	@Test
	public void deleteUserThatDoesNotExist() throws Exception {
		
		//TO DO: finish the test
		assertTrue(true);
			
	}*//*

	
	//Auxiliary methods
	private static String getApiAuthURL() {
		return "/api/v1/auth/";
	}
	
	private static LdapUser createNewUser() {
		return new LdapUser("testuser", "testuser", "testuser@mail.com");
	}
	
	private static LdapUser createExistingUser() {
		return new LdapUser("user1", "user1", "user1@gmail.com");
	}
	
	private static LdapUser createExistingUser2() {
		return new LdapUser("user2", "user2", "user2@gmail.com");
	}
	
	private static AuthRequest createAuthRequestForExistingUser() {
		return new AuthRequest("user1", "user1");
	}
	
	private static AuthRequest createAuthRequestForNotExistingUser() {
		return new AuthRequest("testing", "testing");
	}
	
	private static CurrentUserCredentials createCredentialsForExistingUser() {
		return new CurrentUserCredentials("user1", "user1", "testing1234", "testing1234");
	}
	
	private static CurrentUserCredentials createCredentialsForNotExistingUser() {
		return new CurrentUserCredentials("testuser", "testuser", "testing1234", "testing1234");
	}
	
	private static String getNotExistingUserToken() {
		return "ksnfdsjofakesdjasipdj";
	}
}



*/
