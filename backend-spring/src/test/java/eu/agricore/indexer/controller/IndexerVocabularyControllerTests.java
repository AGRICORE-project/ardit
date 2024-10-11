package eu.agricore.indexer.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.agricore.indexer.config.IndexerMockTest;
import eu.agricore.indexer.ldap.model.LdapUser;
import eu.agricore.indexer.ldap.service.LdapUserService;
import eu.agricore.indexer.model.vocabulary.Vocabulary;
import eu.agricore.indexer.model.vocabulary.VocabularyTopic;
import eu.agricore.indexer.service.VocabularyService;
import eu.agricore.indexer.service.VocabularyValueService;
import eu.agricore.indexer.util.CookieUtil;
import eu.agricore.indexer.util.JWTService;

public class IndexerVocabularyControllerTests extends IndexerMockTest {
	
	@Value("${cookie.name}")
    private String tokenCookieName;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private LdapUserService ldapService;
	
	@Autowired
	private VocabularyService vocabularyService;
	
	@Autowired
	private VocabularyValueService vocabularyValueService;
	
	@Autowired
	private JWTService jwtService;
	
	@Autowired
	private CookieUtil cookieUtil;
	
	@BeforeEach 
    void setUp() {
    	vocabularyValueService.deleteAll();
    	vocabularyService.deleteAll();
    }
	
	@Test
	public void findAllVocabularies() throws Exception {
		LdapUser user = getUser();
		ldapService.authenticate(user);
		Set<String> roles = ldapService.getRolesByUser(user.getUsername());
		
		// Create a new vocabulary
		Vocabulary vocabulary = createNewVocabulary();
		
		mockMvc.perform(get(getApiVocabularyManagementURL()).header(HttpHeaders.AUTHORIZATION, 
			"Bearer " + jwtService.generateToken(user.getUsername(), roles)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].name", is(vocabulary.getName())));
	}
	
	@Test
	public void findVocabularyById() throws Exception {
		
		LdapUser user = getUser();
		ldapService.authenticate(user);
		Set<String> roles = ldapService.getRolesByUser(user.getUsername());
		
		String token = "Bearer " + jwtService.generateToken(user.getUsername(), roles);
		
		// Create a new vocabulary
		Vocabulary vocabulary = createNewVocabulary();
		
		mockMvc.perform(get(getApiVocabularyManagementURL() + vocabulary.getId())
				.cookie(cookieUtil.createTokenCookie(token)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("name", is(vocabulary.getName())));
	}
	
	@Test
	public void findNonExistingVocabularyById() throws Exception {
		
		LdapUser user = getUser();
		ldapService.authenticate(user);
		Set<String> roles = ldapService.getRolesByUser(user.getUsername());
		
		mockMvc.perform(get(getApiVocabularyManagementURL() + getNotExistingVocabularyId()).header(HttpHeaders.AUTHORIZATION, 
			"Bearer " + jwtService.generateToken(user.getUsername(), roles)))
			.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void findVocabularyTopics() throws Exception {
		
		LdapUser user = getUser();
		ldapService.authenticate(user);
		Set<String> roles = ldapService.getRolesByUser(user.getUsername());
		
		mockMvc.perform(get(getApiVocabularyManagementURL() + "topics").header(HttpHeaders.AUTHORIZATION, 
			"Bearer " + jwtService.generateToken(user.getUsername(), roles)))
			.andExpect(status().isOk());
		
		// TODO: check that a VocabularyTopic value is included on the list returned
	}
	
	@Test
	public void createVocabulary() throws Exception {

		LdapUser user = getUser();
		ldapService.authenticate(user);
		Set<String> roles = ldapService.getRolesByUser(user.getUsername());	
		
		// Get a new vocabulary
		Vocabulary vocabulary = getNewVocabulary();
		
		mockMvc.perform(post(getApiVocabularyManagementURL())
			.contentType("application/json")
			.content(objectMapper.writeValueAsString(vocabulary))
			.header(HttpHeaders.AUTHORIZATION, 
			"Bearer " + jwtService.generateToken(user.getUsername(), roles)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("name", is(vocabulary.getName())));
	}
	
	@Test
	public void updateVocabularyThatExists() throws Exception {

		LdapUser user = getUser();
		ldapService.authenticate(user);
		Set<String> roles = ldapService.getRolesByUser(user.getUsername());	
		
		// Create a new vocabulary
		Vocabulary vocabulary = createNewVocabulary();
		String newDescription = "New testing description";
		vocabulary.setDescription(newDescription);
		
		mockMvc.perform(put(getApiVocabularyManagementURL() + vocabulary.getId())
			.contentType("application/json")
			.content(objectMapper.writeValueAsString(vocabulary))
			.header(HttpHeaders.AUTHORIZATION, 
			"Bearer " + jwtService.generateToken(user.getUsername(), roles)))
			.andExpect(status().isOk());
		
		Optional<Vocabulary> res = vocabularyService.findOne(vocabulary.getId());
		
		assertTrue(res.isPresent());
		assertEquals(newDescription, res.get().getDescription());	
	}
	
	@Test
	public void updateVocabularyThatNotExists() throws Exception {
		
		LdapUser user = getUser();
		ldapService.authenticate(user);
		Set<String> roles = ldapService.getRolesByUser(user.getUsername());
		
		// Create a new vocabulary
		Vocabulary vocabulary = createNewVocabulary();
		
		String newDescription = "New testing description";
		vocabulary.setDescription(newDescription);
		
		mockMvc.perform(put(getApiVocabularyManagementURL() + getNotExistingVocabularyId())
			.contentType("application/json")
			.content(objectMapper.writeValueAsString(vocabulary))
			.header(HttpHeaders.AUTHORIZATION, 
			"Bearer " + jwtService.generateToken(user.getUsername(), roles)))
			.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void deleteVocabularyThatExists() throws Exception {
		
		LdapUser user = getUser();
		ldapService.authenticate(user);
		Set<String> roles = ldapService.getRolesByUser(user.getUsername());
		
		// Create a new vocabulary
		Vocabulary vocabulary = createNewVocabulary();
		
		mockMvc.perform(delete(getApiVocabularyManagementURL() + vocabulary.getId())
				.header(HttpHeaders.AUTHORIZATION, 
				"Bearer " + jwtService.generateToken(user.getUsername(), roles)))
		        .andExpect(status().is2xxSuccessful());
		
		assertTrue(vocabularyService.findOne(vocabulary.getId()).isEmpty());
		
		// TODO: check deletion when a vocabulary value is being used by a dataset
	}
	
	@Test
	public void deleteVocabularyThatDoesNotExist() throws Exception {
		
		LdapUser user = getUser();
		ldapService.authenticate(user);
		Set<String> roles = ldapService.getRolesByUser(user.getUsername());
		
		mockMvc.perform(delete(getApiVocabularyManagementURL() + getNotExistingVocabularyId())
				.header(HttpHeaders.AUTHORIZATION, 
				"Bearer " + jwtService.generateToken(user.getUsername(), roles)))
		        .andExpect(status().is4xxClientError());
	}
	
	//Auxiliary methods
	private static String getApiVocabularyManagementURL() {
		return "/api/v1/vocabularies/";
	}
	
	private static LdapUser getUser() {
		return new LdapUser("admin1", "admin1", "admin1@agricore.eu");
	}
	
	private Vocabulary createNewVocabulary() {
		Vocabulary vocabulary = new Vocabulary("iso639-2", "Languages vocabulary","http://id.loc.gov/vocabulary/iso639-2", VocabularyTopic.LANGUAGE);
		
		return vocabularyService.save(vocabulary);
	}
	
	private Vocabulary getNewVocabulary() {
		return  new Vocabulary("iso639-2", "Languages vocabulary","http://id.loc.gov/vocabulary/iso639-2", VocabularyTopic.LANGUAGE);
	}
	
	private static Long getNotExistingVocabularyId() {
		return -1L;
	}
}
