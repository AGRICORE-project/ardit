/*
package eu.agricore.indexer.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;

import eu.agricore.indexer.config.IndexerMockTest;
import eu.agricore.indexer.ldap.dto.LdapUserDTO;
import eu.agricore.indexer.ldap.model.LdapUser;
import eu.agricore.indexer.ldap.service.LdapUserService;
import eu.agricore.indexer.service.UserManagementService;
import eu.agricore.indexer.util.CookieUtil;
import eu.agricore.indexer.util.JWTService;
import eu.agricore.indexer.util.RoleUtils;

public class IndexerUserManagementTests extends IndexerMockTest {

	@Value("${cookie.name}")
    private String tokenCookieName;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private LdapUserService ldapService;

	@Autowired
	private UserManagementService userManagementService;

	@Autowired
	private JWTService jwtService;

	@Autowired
	private CookieUtil cookieUtil;

	@BeforeEach
    void init() {
    	// TODO: This must be done automatically using @Transactional and @Rollback
    	LdapUser existingUser = getDefaultUser();
    	ldapService.delete(existingUser);
    	ldapService.create(existingUser);
    	userManagementService.updateUserRoles(existingUser, RoleUtils.defaultRoles());
    }

	@Test
	public void findAllUsers() throws Exception {

		LdapUser adminUser = getAdminUser();
		ldapService.authenticate(adminUser);

		Set<String> roles = ldapService.getRolesByUser(adminUser.getUsername());

		mockMvc.perform(get(getApiUserManagementURL()).header(HttpHeaders.AUTHORIZATION,
			"Bearer " + jwtService.generateToken(adminUser.getUsername(), roles)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].username", is(adminUser.getUsername())))
			.andExpect(jsonPath("$[1].username", is(getMantainerUser().getUsername())));
	}

	@Test
	public void findAllUsersUsingCookie() throws Exception {

		LdapUser adminUser = getAdminUser();
		ldapService.authenticate(adminUser);
		Set<String> roles = ldapService.getRolesByUser(adminUser.getUsername());

		String token = "Bearer " + jwtService.generateToken(adminUser.getUsername(), roles);

		mockMvc.perform(get(getApiUserManagementURL())
			.cookie(cookieUtil.createTokenCookie(token)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].username", is(adminUser.getUsername())))
			.andExpect(jsonPath("$[1].username", is(getMantainerUser().getUsername())));
	}

	@Test
	public void findOneExistingUser() throws Exception {
		LdapUser adminUser = getAdminUser();
		ldapService.authenticate(adminUser);
		Set<String> roles = ldapService.getRolesByUser(adminUser.getUsername());

		LdapUser defaultUser = getDefaultUser();

		mockMvc.perform(get(getApiUserManagementURL() + defaultUser.getUsername()).header(HttpHeaders.AUTHORIZATION,
			"Bearer " + jwtService.generateToken(adminUser.getUsername(), roles)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("username", is(defaultUser.getUsername())));
	}

	@Test
	public void findOneExistingUserByNotAdminUser() throws Exception {

		LdapUser defaultUser = getDefaultUser();
		ldapService.authenticate(defaultUser);
		Set<String> roles = ldapService.getRolesByUser(defaultUser.getUsername());

		mockMvc.perform(get(getApiUserManagementURL() + defaultUser.getUsername())
				.header(HttpHeaders.AUTHORIZATION,
				"Bearer " + jwtService.generateToken(defaultUser.getUsername(), roles)))
		        .andExpect(status().isForbidden()); //Request is forbidden for not admin users
	}

	@Test
	public void findOneNotExistingUser() throws Exception {
		LdapUser adminUser = getAdminUser();
		ldapService.authenticate(adminUser);
		Set<String> roles = ldapService.getRolesByUser(adminUser.getUsername());

		mockMvc.perform(get(getApiUserManagementURL() + getNotExistingUser().getUsername()).header(HttpHeaders.AUTHORIZATION,
			"Bearer " + jwtService.generateToken(adminUser.getUsername(), roles)))
			.andExpect(status().is4xxClientError());
	}

	@Test
	public void updateUserThatExists() throws Exception {

		LdapUser adminUser = getAdminUser();
		ldapService.authenticate(adminUser);
		Set<String> roles = ldapService.getRolesByUser(adminUser.getUsername());

		LdapUser existingUser = getDefaultUser();
		LdapUserDTO userDTO = new LdapUserDTO(existingUser);
		String newEmail = "mynewemail@mail.com";
		userDTO.setEmail(newEmail);

		mockMvc.perform(put(getApiUserManagementURL() + existingUser.getUsername())
			.contentType("application/json")
			.content(objectMapper.writeValueAsString(userDTO))
			.header(HttpHeaders.AUTHORIZATION,
			"Bearer " + jwtService.generateToken(adminUser.getUsername(), roles)))
			.andExpect(status().isOk());

		assertEquals(newEmail, ldapService.find(userDTO.getUsername()).get().getEmail());
	}

	@Test
	public void updateUserThatNotExists() throws Exception {

		LdapUser adminUser = getAdminUser();
		ldapService.authenticate(adminUser);
		Set<String> roles = ldapService.getRolesByUser(adminUser.getUsername());

		LdapUser notExistingUser = getNotExistingUser();
		LdapUserDTO userDTO = new LdapUserDTO(notExistingUser);
		String newEmail = "mynewemail@mail.com";
		userDTO.setEmail(newEmail);

		mockMvc.perform(put(getApiUserManagementURL() + notExistingUser.getUsername())
			.contentType("application/json")
			.content(objectMapper.writeValueAsString(userDTO))
			.header(HttpHeaders.AUTHORIZATION,
			"Bearer " + jwtService.generateToken(adminUser.getUsername(), roles)))
			.andExpect(status().is4xxClientError());
	}

	// TODO: units tests for endpoints to update roles and password

	*/
/*
	 * Admin user receives new role as mantainer
	 * {U, A} + {M} = {U, M}
	 *//*

	@Test
	public void addNotExistingRoles() throws Exception {

		LdapUser user = getDefaultUser();

		//Previous roles
		userManagementService.updateUserRoles(user, RoleUtils.adminRoles());

		//New roles
		userManagementService.updateUserRoles(user, Sets.newHashSet(RoleUtils.MANTAINER_ROLE));

		Set<String> expected = RoleUtils.mantainerRoles();
		Set<String> roles = ldapService.getRolesByUser(user.getUsername());

		assertEquals(expected, roles);
	}

	*/
/*
	 * Default user receives admin and mantainer roles
	 * {U} + {A, M} = {U, A, M}
	 *//*

	@Test
	public void addAllRoles() throws Exception {

		LdapUser user = getDefaultUser();

		//Previous roles
		userManagementService.updateUserRoles(user, Sets.newHashSet(RoleUtils.USER_ROLE));

		//New roles
		userManagementService.updateUserRoles(user, Sets.newHashSet(RoleUtils.ADMIN_ROLE, RoleUtils.MANTAINER_ROLE));

		Set<String> expected = RoleUtils.allRoles();
		Set<String> roles = ldapService.getRolesByUser(user.getUsername());

		assertEquals(expected, roles);
	}

	*/
/*
	 * Admin user receives admin role
	 * {U, A} + {A} = {U, A}
	 *//*

	@Test
	public void addSameRoles() throws Exception {

		LdapUser user = getDefaultUser();

		//Previous roles
		userManagementService.updateUserRoles(user, RoleUtils.adminRoles());

		//New roles
		userManagementService.updateUserRoles(user, Sets.newHashSet(RoleUtils.ADMIN_ROLE));

		Set<String> expected = RoleUtils.adminRoles();
		Set<String> roles = ldapService.getRolesByUser(user.getUsername());

		assertEquals(expected, roles);
	}

	*/
/*
	 * Admin user receives an invalid role and loses admin role
	 * {U, A} + {X} = {U}
	 *//*

	@Test
	public void addInvalidRoles() throws Exception {

		LdapUser user = getDefaultUser();

		//Previous roles
		userManagementService.updateUserRoles(user, RoleUtils.adminRoles());

		//New roles
		userManagementService.updateUserRoles(user, Sets.newHashSet("TESTING"));

		Set<String> expected = RoleUtils.defaultRoles();
		Set<String> roles = ldapService.getRolesByUser(user.getUsername());

		assertEquals(expected, roles);
	}

	*/
/*
	 * Admin user receives a mantainer and an invalid role
	 * {U, A} + {M, X} = {U, M}
	 *//*

	@Test
	public void addInvalidAndValidRoles() throws Exception {

		LdapUser user = getDefaultUser();

		//Previous roles {U, A}
		userManagementService.updateUserRoles(user, RoleUtils.adminRoles());

		//New roles {M, X}
		userManagementService.updateUserRoles(user, Sets.newHashSet(RoleUtils.MANTAINER_ROLE, "TESTING"));

		Set<String> expected = RoleUtils.mantainerRoles();
		Set<String> roles = ldapService.getRolesByUser(user.getUsername());

		assertEquals(expected, roles);
	}

	*/
/*
	 * Default user receives an empty list of roles
	 * {}
	 *//*

	@Test
	public void addEmptyRoles() throws Exception {
		LdapUser adminUser = getAdminUser();
		ldapService.authenticate(adminUser);
		Set<String> adminRoles = ldapService.getRolesByUser(adminUser.getUsername());

		//New roles
		Set<String> newRoles = Collections.emptySet();

		mockMvc.perform(put(getApiUserManagementURL() + getDefaultUser().getUsername())
			.contentType("application/json")
			.content(objectMapper.writeValueAsString(newRoles))
			.header(HttpHeaders.AUTHORIZATION,
			"Bearer " + jwtService.generateToken(adminUser.getUsername(), adminRoles)))
			.andExpect(status().is4xxClientError());
	}

	@Test
	public void updateNotExistingUserRoles() throws Exception {
		LdapUser adminUser = getAdminUser();
		ldapService.authenticate(adminUser);
		Set<String> adminRoles = ldapService.getRolesByUser(adminUser.getUsername());

		//New roles
		Set<String> newRoles = Sets.newHashSet(RoleUtils.MANTAINER_ROLE, "TESTING");

		mockMvc.perform(put(getApiUserManagementURL() + getDefaultUser().getUsername())
			.contentType("application/json")
			.content(objectMapper.writeValueAsString(newRoles))
			.header(HttpHeaders.AUTHORIZATION,
			"Bearer " + jwtService.generateToken(adminUser.getUsername(), adminRoles)))
			.andExpect(status().is4xxClientError());
	}

	@Test
	public void deleteUserThatExists() throws Exception {

		LdapUser adminUser = getAdminUser();
		ldapService.authenticate(adminUser);
		Set<String> roles = ldapService.getRolesByUser(adminUser.getUsername());

		mockMvc.perform(delete(getApiUserManagementURL() + getDefaultUser().getUsername())
				.header(HttpHeaders.AUTHORIZATION,
				"Bearer " + jwtService.generateToken(adminUser.getUsername(), roles)))
		        .andExpect(status().is2xxSuccessful());

		assertTrue(ldapService.find(getDefaultUser().getUsername()).isEmpty());
	}

	@Test
	public void deleteUserThatDoesNotExist() throws Exception {

		LdapUser adminUser = getAdminUser();
		ldapService.authenticate(adminUser);
		Set<String> roles = ldapService.getRolesByUser(adminUser.getUsername());

		mockMvc.perform(delete(getApiUserManagementURL() + getNotExistingUser().getUsername())
				.header(HttpHeaders.AUTHORIZATION,
				"Bearer " + jwtService.generateToken(adminUser.getUsername(), roles)))
		        .andExpect(status().is4xxClientError());
	}

	//Auxiliary methods
	private static String getApiUserManagementURL() {
		return "/api/v1/users/";
	}

	private static LdapUser getAdminUser() {
		return new LdapUser("admin1", "admin1", "admin1@agricore.eu");
	}

	private static LdapUser getDefaultUser() {
		return new LdapUser("user1", "user1", "user1@gmail.com");
	}

	private static LdapUser getMantainerUser() {
		return new LdapUser("mantainer1", "mantainer1", "mantainer1@agricore.eu");
	}

	private static LdapUser getNotExistingUser() {
		return new LdapUser("testing", "testing", "testing@gmail.com");
	}

}



    */
