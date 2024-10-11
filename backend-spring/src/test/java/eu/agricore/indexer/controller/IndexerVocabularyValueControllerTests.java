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
import eu.agricore.indexer.model.vocabulary.VocabularyValue;
import eu.agricore.indexer.service.VocabularyService;
import eu.agricore.indexer.service.VocabularyValueService;
import eu.agricore.indexer.util.CookieUtil;
import eu.agricore.indexer.util.JWTService;

public class IndexerVocabularyValueControllerTests extends IndexerMockTest {
	
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
	public void findVocabularyValuesByVocabularyId() throws Exception {
		
		LdapUser user = getUser();
		ldapService.authenticate(user);
		Set<String> roles = ldapService.getRolesByUser(user.getUsername());
		
		String token = "Bearer " + jwtService.generateToken(user.getUsername(), roles);
		
		// Create a new vocabulary
		Vocabulary vocabulary = createNewVocabulary();
		
		// Create a new vocabulary value
		VocabularyValue vocabularyValue = createNewVocabularyValue(vocabulary.getId());
		
		mockMvc.perform(get(getApiVocabularyManagementURL() + vocabulary.getId() + "/values")
				.cookie(cookieUtil.createTokenCookie(token)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].label", is(vocabularyValue.getLabel())));
	}
	
	@Test
	public void findVocabularyValuesByTopic() throws Exception {
		
		LdapUser user = getUser();
		ldapService.authenticate(user);
		Set<String> roles = ldapService.getRolesByUser(user.getUsername());
		
		String token = "Bearer " + jwtService.generateToken(user.getUsername(), roles);
		
		// Create a new vocabulary
		Vocabulary vocabulary = createNewVocabulary();
		
		// Create a new vocabulary value
		VocabularyValue vocabularyValue = createNewVocabularyValue(vocabulary.getId());
		
		mockMvc.perform(get(getApiVocabularyManagementURL())
				.cookie(cookieUtil.createTokenCookie(token))
				.param("topic", vocabulary.getTopic().toString()))
				.andExpect(status().isOk());
				// TODO: check the response value
				//.andExpect(jsonPath("$[0].label", is(vocabularyValue.getLabel())));
	}
	
	@Test
	public void findVocabularyValuesByExtraData() throws Exception {
		
		LdapUser user = getUser();
		ldapService.authenticate(user);
		Set<String> roles = ldapService.getRolesByUser(user.getUsername());
		
		String token = "Bearer " + jwtService.generateToken(user.getUsername(), roles);
		
		// Create a new vocabulary
		Vocabulary vocabulary = createNewVocabulary();
		
		//Create a new vocabulary value
		VocabularyValue vocabularyValue = createNewVocabularyValueWithExtraData(vocabulary.getId());
		
		mockMvc.perform(get(getApiVocabularyManagementURL())
				.cookie(cookieUtil.createTokenCookie(token))
				.param("extra_data", vocabularyValue.getExtra_data()))
				.andExpect(status().isOk());
				// TODO: check the response value
				//.andExpect(jsonPath("$[0].label", is(vocabularyValue.getLabel())));
	}
	
	@Test
	public void createVocabularyValue() throws Exception {

		LdapUser user = getUser();
		ldapService.authenticate(user);
		Set<String> roles = ldapService.getRolesByUser(user.getUsername());	
		
		// Create a new vocabulary
		Vocabulary vocabulary = createNewVocabulary();
		
		//Get a new vocabulary value
		VocabularyValue vocabularyValue = getNewVocabularyValue();
		
		mockMvc.perform(post(getApiVocabularyManagementURL() + vocabulary.getId() + "/values")
			.contentType("application/json")
			.content(objectMapper.writeValueAsString(vocabularyValue))
			.header(HttpHeaders.AUTHORIZATION, 
			"Bearer " + jwtService.generateToken(user.getUsername(), roles)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("label", is(vocabularyValue.getLabel())));
	}
	
	@Test
	public void updateVocabularyValue() throws Exception {

		LdapUser user = getUser();
		ldapService.authenticate(user);
		Set<String> roles = ldapService.getRolesByUser(user.getUsername());	
		
		// Create a new vocabulary
		Vocabulary vocabulary = createNewVocabulary();
		
		// Create a new vocabulary value
		VocabularyValue vocabularyValue = createNewVocabularyValue(vocabulary.getId());
		String newLabel = "Testing label";
		vocabularyValue.setLabel(newLabel);
		
		mockMvc.perform(put(getApiVocabularyManagementURL() + vocabulary.getId() + "/values/" + vocabularyValue.getId())
			.contentType("application/json")
			.content(objectMapper.writeValueAsString(vocabularyValue))
			.header(HttpHeaders.AUTHORIZATION, 
			"Bearer " + jwtService.generateToken(user.getUsername(), roles)))
			.andExpect(status().isOk());
		
		Optional<VocabularyValue> res = vocabularyValueService.findOne(vocabularyValue.getId());
		
		assertTrue(res.isPresent());
		assertEquals(newLabel, res.get().getLabel());	
	}
	
	@Test
	public void updateVocabularyValueThatNotExists() throws Exception {
		
		LdapUser user = getUser();
		ldapService.authenticate(user);
		Set<String> roles = ldapService.getRolesByUser(user.getUsername());	
		
		// Create a new vocabulary
		Vocabulary vocabulary = createNewVocabulary();
		
		// Create a new vocabulary value
		VocabularyValue vocabularyValue = createNewVocabularyValue(vocabulary.getId());
		String newLabel = "Testing label";
		vocabularyValue.setLabel(newLabel);
		
		mockMvc.perform(put(getApiVocabularyManagementURL() + vocabulary.getId() + "/values/" + getNotExistingId())
			.contentType("application/json")
			.content(objectMapper.writeValueAsString(vocabularyValue))
			.header(HttpHeaders.AUTHORIZATION, 
			"Bearer " + jwtService.generateToken(user.getUsername(), roles)))
			.andExpect(status().is4xxClientError());	
	}
	
	@Test
	public void updateVocabularyValueWithVocabularyThatNotExists() throws Exception {
		
		LdapUser user = getUser();
		ldapService.authenticate(user);
		Set<String> roles = ldapService.getRolesByUser(user.getUsername());
		
		// Create a new vocabulary
		Vocabulary vocabulary = createNewVocabulary();
		
		// Create a new vocabulary value
		VocabularyValue vocabularyValue = createNewVocabularyValue(vocabulary.getId());
		String newLabel = "Testing label";
		vocabularyValue.setLabel(newLabel);
		
		mockMvc.perform(put(getApiVocabularyManagementURL() + getNotExistingId() + "/values/" + vocabularyValue.getId())
			.contentType("application/json")
			.content(objectMapper.writeValueAsString(vocabularyValue))
			.header(HttpHeaders.AUTHORIZATION, 
			"Bearer " + jwtService.generateToken(user.getUsername(), roles)))
			.andExpect(status().is4xxClientError());	
	}
	
	@Test
	public void updateNotBelongingVocabularyValue() throws Exception {
		
		LdapUser user = getUser();
		ldapService.authenticate(user);
		Set<String> roles = ldapService.getRolesByUser(user.getUsername());
		
		// Create a new vocabulary
		Vocabulary vocabulary = createNewVocabulary();
		
		// Create another vocabulary
		Vocabulary vocabulary2 = createNewVocabulary();
		
		// Create a new vocabulary value and assign it to the second vocabulary
		VocabularyValue vocabularyValue = createNewVocabularyValue(vocabulary2.getId());
		String newLabel = "Testing label";
		vocabularyValue.setLabel(newLabel);
		
		// Test that the vocabulary value does not belong to the given vocabulary in the request
		mockMvc.perform(put(getApiVocabularyManagementURL() + vocabulary.getId() + "/values/" + vocabularyValue.getId())
			.contentType("application/json")
			.content(objectMapper.writeValueAsString(vocabularyValue))
			.header(HttpHeaders.AUTHORIZATION, 
			"Bearer " + jwtService.generateToken(user.getUsername(), roles)))
			.andExpect(status().is4xxClientError());
		
		// TODO: check error message
	}
	
	@Test
	public void deleteVocabularyValue() throws Exception {
		
		LdapUser user = getUser();
		ldapService.authenticate(user);
		Set<String> roles = ldapService.getRolesByUser(user.getUsername());
		
		// Create a new vocabulary
		Vocabulary vocabulary = createNewVocabulary();
		
		// Create a new vocabulary value
		VocabularyValue vocabularyValue = createNewVocabularyValue(vocabulary.getId());
		
		mockMvc.perform(delete(getApiVocabularyManagementURL() + vocabulary.getId() + "/values/" + vocabularyValue.getId())
				.header(HttpHeaders.AUTHORIZATION, 
				"Bearer " + jwtService.generateToken(user.getUsername(), roles)))
		        .andExpect(status().is2xxSuccessful());
		
		assertTrue(vocabularyValueService.findOne(vocabularyValue.getId()).isEmpty());
		
		// TODO: check deletion when a vocabulary value is being used by a dataset
	}
	
	@Test
	public void deleteVocabularyValueThatDoesNotExist() throws Exception {
		
		LdapUser user = getUser();
		ldapService.authenticate(user);
		Set<String> roles = ldapService.getRolesByUser(user.getUsername());
		
		// Create a new vocabulary
		Vocabulary vocabulary = createNewVocabulary();
		
		mockMvc.perform(delete(getApiVocabularyManagementURL() + vocabulary.getId() + "/values/" + getNotExistingId())
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
	
	private VocabularyValue createNewVocabularyValue(Long vocabularyId) {
		VocabularyValue vocabularyValue = new VocabularyValue("tai", "http://id.loc.gov/vocabulary/iso639-2/tai", "Tai languages", "");
		return vocabularyValueService.save(vocabularyId, vocabularyValue);
	}
	
	private VocabularyValue getNewVocabularyValue() {
		return new VocabularyValue("tai", "http://id.loc.gov/vocabulary/iso639-2/tai", "Tai languages", "");
	}
	
	private VocabularyValue createNewVocabularyValueWithExtraData(Long vocabularyId) {
		VocabularyValue vocabularyValue = new VocabularyValue("bul", "http://id.loc.gov/vocabulary/iso639-2/bul", "Bulgarian", "Testing");
		return vocabularyValueService.save(vocabularyId, vocabularyValue);
	}
	
	private static Long getNotExistingId() {
		return -1L;
	}
}
