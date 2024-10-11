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

import java.util.Date;
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
import eu.agricore.indexer.model.Catalogue;
import eu.agricore.indexer.model.dataset.Dataset;
import eu.agricore.indexer.model.dataset.DatasetGenerationActivity;
import eu.agricore.indexer.model.dataset.DatasetReferencedResource;
import eu.agricore.indexer.model.dataset.Dataset.DatasetType;
import eu.agricore.indexer.model.datasetvariable.DatasetVariable;
import eu.agricore.indexer.model.vocabulary.Vocabulary;
import eu.agricore.indexer.model.vocabulary.VocabularyTopic;
import eu.agricore.indexer.model.vocabulary.VocabularyValue;
import eu.agricore.indexer.service.CatalogueService;
import eu.agricore.indexer.service.DatasetService;
import eu.agricore.indexer.service.VocabularyService;
import eu.agricore.indexer.service.VocabularyValueService;
import eu.agricore.indexer.util.CookieUtil;
import eu.agricore.indexer.util.JWTService;

public class IndexerDatasetControllerTests extends IndexerMockTest {
	
	@Value("${cookie.name}")
    private String tokenCookieName;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private LdapUserService ldapService;
	
	@Autowired
	private DatasetService datasetService;
	
	@Autowired
	private CatalogueService catalogueService;
	
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
    	datasetService.deleteAll();
    	catalogueService.deleteAll();
    	vocabularyValueService.deleteAll();
    	vocabularyService.deleteAll();
    }
	
	@Test
	public void findAllDatasets() throws Exception {
		
		LdapUser user = getUser();
		ldapService.authenticate(user);
		Set<String> roles = ldapService.getRolesByUser(user.getUsername());
		
		Dataset dataset = createNewDataset();
		
		mockMvc.perform(get(getApiDatasetManagementURL() + "/search").header(HttpHeaders.AUTHORIZATION, 
			"Bearer " + jwtService.generateToken(user.getUsername(), roles)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content[0].title", is(dataset.getTitle())));
	}
	
	@Test
	public void findAllDatasetsUsingCookie() throws Exception {
			
		LdapUser user = getUser();
		ldapService.authenticate(user);
		Set<String> roles = ldapService.getRolesByUser(user.getUsername());
		
		String token = "Bearer " + jwtService.generateToken(user.getUsername(), roles);
		
		Dataset dataset = createNewDataset();
		
		mockMvc.perform(get(getApiDatasetManagementURL() + "/search")
			.cookie(cookieUtil.createTokenCookie(token)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content[0].title", is(dataset.getTitle())));
	}
	
	@Test
	public void findOneExistingDataset() throws Exception {
		
		LdapUser user = getUser();
		ldapService.authenticate(user);
		Set<String> roles = ldapService.getRolesByUser(user.getUsername());
		
		Dataset dataset = createNewDataset();
		
		mockMvc.perform(get(getApiDatasetManagementURL() + dataset.getId()).header(HttpHeaders.AUTHORIZATION, 
			"Bearer " + jwtService.generateToken(user.getUsername(), roles)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("title", is(dataset.getTitle())));
	}
	
	@Test
	public void findOneNotExistingDataset() throws Exception {
		
		LdapUser user = getUser();
		ldapService.authenticate(user);
		Set<String> roles = ldapService.getRolesByUser(user.getUsername());
		
		mockMvc.perform(get(getApiDatasetManagementURL() + getNotExistingDatasetId()).header(HttpHeaders.AUTHORIZATION, 
			"Bearer " + jwtService.generateToken(user.getUsername(), roles)))
			.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void createDataset() throws Exception {

		LdapUser user = getUser();
		ldapService.authenticate(user);
		Set<String> roles = ldapService.getRolesByUser(user.getUsername());	
		
		Dataset dataset = createNewDataset();
		
		mockMvc.perform(post(getApiDatasetManagementURL())
			.contentType("application/json")
			.content(objectMapper.writeValueAsString(dataset))
			.header(HttpHeaders.AUTHORIZATION, 
			"Bearer " + jwtService.generateToken(user.getUsername(), roles)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("title", is(dataset.getTitle())));
	}
	
	@Test
	public void updateDatasetThatExists() throws Exception {

		LdapUser user = getUser();
		ldapService.authenticate(user);
		Set<String> roles = ldapService.getRolesByUser(user.getUsername());	
		
		Dataset dataset = createNewDataset();
		String newTitle = "New testing title";
		dataset.setTitle(newTitle);
		
		mockMvc.perform(put(getApiDatasetManagementURL() + dataset.getId())
			.contentType("application/json")
			.content(objectMapper.writeValueAsString(dataset))
			.header(HttpHeaders.AUTHORIZATION, 
			"Bearer " + jwtService.generateToken(user.getUsername(), roles)))
			.andExpect(status().isOk());
		
		assertEquals(newTitle, datasetService.findOne(dataset.getId()).get().getTitle());	
	}
	
	@Test
	public void updateDatasetThatNotExists() throws Exception {
		
		LdapUser user = getUser();
		ldapService.authenticate(user);
		Set<String> roles = ldapService.getRolesByUser(user.getUsername());
		
		Dataset dataset = createNewDataset();
		
		String newTitle = "New testing title";
		dataset.setTitle(newTitle);
		
		mockMvc.perform(put(getApiDatasetManagementURL() + getNotExistingDatasetId())
			.contentType("application/json")
			.content(objectMapper.writeValueAsString(dataset))
			.header(HttpHeaders.AUTHORIZATION, 
			"Bearer " + jwtService.generateToken(user.getUsername(), roles)))
			.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void deleteDatasetThatExists() throws Exception {
		
		LdapUser user = getUser();
		ldapService.authenticate(user);
		Set<String> roles = ldapService.getRolesByUser(user.getUsername());
		
		Dataset dataset = createNewDataset();
		
		mockMvc.perform(delete(getApiDatasetManagementURL() + dataset.getId())
				.header(HttpHeaders.AUTHORIZATION, 
				"Bearer " + jwtService.generateToken(user.getUsername(), roles)))
		        .andExpect(status().is2xxSuccessful());
		
		assertTrue(datasetService.findOne(dataset.getId()).isEmpty());
	}
	
	@Test
	public void deleteDatasetThatDoesNotExist() throws Exception {
		
		LdapUser user = getUser();
		ldapService.authenticate(user);
		Set<String> roles = ldapService.getRolesByUser(user.getUsername());
		
		mockMvc.perform(delete(getApiDatasetManagementURL() + getNotExistingDatasetId())
				.header(HttpHeaders.AUTHORIZATION, 
				"Bearer " + jwtService.generateToken(user.getUsername(), roles)))
		        .andExpect(status().is4xxClientError());
	}
	
	//Auxiliary methods
	private static String getApiDatasetManagementURL() {
		return "/api/v1/datasets/";
	}
	
	private static LdapUser getUser() {
		return new LdapUser("admin1", "admin1", "admin1@agricore.eu");
	}
	
	private Dataset createNewDataset() {
		
		// Create a catalog
		Catalogue newCatalogue = createNewCatalogue();
		
		/********Initialize a vocabularies and their values********/
		

		//Task to which it will be reported
		Vocabulary tasksVoc = createNewVocabulary(VocabularyTopic.REPORT_TO_TASK);;
		VocabularyValue task = new VocabularyValue("task1", "", "Characterisation of geo-referenced datasets", "");
		createNewVocabularyValue(tasksVoc.getId(), task);
		
		//Language
		Vocabulary languageVoc = createNewVocabulary(VocabularyTopic.LANGUAGE); // Save vocabulary
		VocabularyValue language1 = new VocabularyValue("bul", "http://id.loc.gov/vocabulary/iso639-2/bul", "Bulgarian", "");
		VocabularyValue language2 = new VocabularyValue("tai", "http://id.loc.gov/vocabulary/iso639-2/tai", "Tai languages", "");
		createNewVocabularyValue(languageVoc.getId(), language1); // Save vocabulary value
		createNewVocabularyValue(languageVoc.getId(), language2); // Save vocabulary value
		
		//Resource type
		Vocabulary resourceVoc = createNewVocabulary(VocabularyTopic.RESOURCE_TYPE); // Save vocabulary
		VocabularyValue resourceType = new VocabularyValue("dat", "http://purl.org/dc/dcmitype/Dataset", "Dataset", "");
		createNewVocabularyValue(resourceVoc.getId(), resourceType); // Save vocabulary value
		
		//Periodicity
		Vocabulary periodicityVoc = createNewVocabulary(VocabularyTopic.PERIODICITY);;
		VocabularyValue periodicity = new VocabularyValue("annual", "http://purl.org/cld/freq/annual", "Annual", "");
		createNewVocabularyValue(periodicityVoc.getId(), periodicity);
		
		//Subject
		Vocabulary subjectVoc = createNewVocabulary(VocabularyTopic.SUBJECT);
		VocabularyValue subject1 = new VocabularyValue("1000", "http://data.europa.eu/uxp/1000", "financing", "");
		VocabularyValue subject2 = new VocabularyValue("1095", "http://data.europa.eu/uxp/1095", "tax evasion", "");
		createNewVocabularyValue(subjectVoc.getId(), subject1);
		createNewVocabularyValue(subjectVoc.getId(), subject2);
	
		//Purpose
		Vocabulary purposeVoc = createNewVocabulary(VocabularyTopic.PURPOSE);
		VocabularyValue purpose1 = new VocabularyValue("ENVP", "", "Environmental policy", "");
		VocabularyValue purpose2 = new VocabularyValue("EUIA", "", "Energy use in agriculture", "");
		createNewVocabularyValue(purposeVoc.getId(), purpose1);
		createNewVocabularyValue(purposeVoc.getId(), purpose2);
		
		//Themes
		Vocabulary themesVoc = createNewVocabulary(VocabularyTopic.THEME);
		VocabularyValue theme1 = new VocabularyValue("AGRI", "http://publications.europa.eu/resource/authority/data-theme/AGRI", "Agriculture, fisheries, forestry and food", "");
		VocabularyValue theme2 = new VocabularyValue("ENVI", "http://publications.europa.eu/resource/authority/data-theme/ENVI", "Environment", "");
		createNewVocabularyValue(themesVoc.getId(), theme1);
		createNewVocabularyValue(themesVoc.getId(), theme2);
		
		//Access right
		Vocabulary accessRightVoc = createNewVocabulary(VocabularyTopic.ACCESS_RIGHT);
		VocabularyValue accessRight = new VocabularyValue("PUBLIC", "http://publications.europa.eu/resource/authority/access-right/PUBLIC", "Public", "");
		createNewVocabularyValue(accessRightVoc.getId(), accessRight);
		
		//Formats
		Vocabulary formatsVoc = createNewVocabulary(VocabularyTopic.FORMAT);
		VocabularyValue format1 = new VocabularyValue("JSON", "http://publications.europa.eu/resource/authority/file-type/JSON", "JSON", "");
		VocabularyValue format2 = new VocabularyValue("PDF", "http://publications.europa.eu/resource/authority/file-type/PDF", "PDF", "");
		createNewVocabularyValue(formatsVoc.getId(), format1);
		createNewVocabularyValue(formatsVoc.getId(), format2);
		
		//Procedures to access the data
		String accessProcedures = "";
		
		//Data frequency (using periodicity vocabulary)
		VocabularyValue frequency = new VocabularyValue("annual", "http://purl.org/cld/freq/annual", "Annual", "");
		createNewVocabularyValue(periodicityVoc.getId(), frequency);
		
		//Math representation
		Vocabulary mathRepVoc = createNewVocabulary(VocabularyTopic.MATH_REPRESENTATION);
		VocabularyValue mathRep = new VocabularyValue("AVG", "", "Average", "");
		createNewVocabularyValue(mathRepVoc.getId(), mathRep);
		
		//Aggregation level
		Integer aggregationLevel = 100;
		Integer aggragationScale = 1000;
		Vocabulary measureUnitVoc = createNewVocabulary(VocabularyTopic.MEASURE);
		VocabularyValue aggregationLevelUnit = new VocabularyValue("MTK", "http://publications.europa.eu/resource/authority/measurement-unit/MTK", "square metre", "");
		createNewVocabularyValue(measureUnitVoc.getId(), aggregationLevelUnit);

		//Mark as draft
		Boolean draft = false;
		
		String title = "Testing title";
		String description= "Testing description";
		String producer = "European comission";
		String link = "https://agricore-project.eu/";
		Float spatialResolution = 100.02f;
		String temporalResolution = "PT15M";
		
		
		Dataset newDataset = new Dataset(title, description, DatasetType.GEOREFERENCED, task, draft, new Date(), new Date(), new Date(), new Date(), producer, link, 
				periodicity, newCatalogue, spatialResolution, temporalResolution, resourceType, new Date(), new Date(), accessRight, accessProcedures, frequency, "", aggregationLevel, aggregationLevelUnit, aggragationScale);
		
		// Generation activities
		DatasetGenerationActivity wasGeneratedBy = new DatasetGenerationActivity("Testing activity");
		newDataset.addGenerationActivity(wasGeneratedBy);
		
		// Referenced resources
		DatasetReferencedResource isReferencedBy = new DatasetReferencedResource("Testing link");
		newDataset.addReferencedResource(isReferencedBy);
		
		//Add languages
		newDataset.addVocabularyValue(language1, VocabularyTopic.LANGUAGE);
		newDataset.addVocabularyValue(language2, VocabularyTopic.LANGUAGE);
		
		//Add subjects
		newDataset.addVocabularyValue(subject1, VocabularyTopic.SUBJECT);
		newDataset.addVocabularyValue(subject2, VocabularyTopic.SUBJECT);
		
		//Add purposes
		newDataset.addVocabularyValue(purpose1, VocabularyTopic.PURPOSE);
		newDataset.addVocabularyValue(purpose2, VocabularyTopic.PURPOSE);
		
		//Add themes
		newDataset.addVocabularyValue(theme1, VocabularyTopic.THEME);
		newDataset.addVocabularyValue(theme2, VocabularyTopic.THEME);
		
		//Add formats
		newDataset.addVocabularyValue(format1, VocabularyTopic.FORMAT);
		newDataset.addVocabularyValue(format2, VocabularyTopic.FORMAT);
		
		//TODO: Add variables
		
		//Data origin vocabulary
		Vocabulary dataOriginVoc = createNewVocabulary(VocabularyTopic.VARIABLE_DATA_ORIGIN);
		VocabularyValue dataOrigin = new VocabularyValue("OBS", "", "Observed", "");
		createNewVocabularyValue(dataOriginVoc.getId(), dataOrigin);
		
		newDataset.addDatasetVariable( new DatasetVariable("Prices", "euros", new Date(), new Date(), dataOrigin, frequency, mathRep, mathRep, aggregationLevel, aggregationLevelUnit, aggragationScale, "100", ""));
		newDataset.addDatasetVariable( new DatasetVariable("Insecticides", "kg", new Date(), new Date(), dataOrigin, frequency, mathRep, mathRep, aggregationLevel, aggregationLevelUnit, aggragationScale, "100", ""));
		
		//TODO: Add analysis unit
		//newDataset.addAnalysisUnit(new AnalysisUnit(frequency, 100, 2009, 2012, 1000));
		
		//TODO: geonames
		
		return datasetService.save(newDataset);
	}
	
	/*
	 * Initialises a catalog and stores it in the database
	 */
	private Catalogue createNewCatalogue() {
		Catalogue catalogue = new Catalogue(new Date(), new Date(), "Testing catalogue", "A description for a testing catalogue", "Eurostat", "Eurostat", "https://ec.europa.eu/eurostat/web/main", new Date(), new Date(), 0.0f, "", null);
		return catalogueService.save(catalogue);
	}
	
	/*
	 * Initialises a vocabulary and stores it in the database
	 */
	private Vocabulary createNewVocabulary(VocabularyTopic topic) {
		Vocabulary vocabulary = new Vocabulary("Testing vocabulary name", "Testing vocabulary description","Testing vocabulary URL", topic);
		return vocabularyService.save(vocabulary);
	}
	
	/*
	 * Store a vocabulary value in the database
	 */
	private void createNewVocabularyValue(Long vocabularyId, VocabularyValue vocabularyValue) {
		vocabularyValueService.save(vocabularyId, vocabularyValue);
	}

	private static Long getNotExistingDatasetId() {
		return -1L;
	}

}
