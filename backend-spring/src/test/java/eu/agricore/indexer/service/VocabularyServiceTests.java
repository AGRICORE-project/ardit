package eu.agricore.indexer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.agricore.indexer.config.IndexerBaseTest;
import eu.agricore.indexer.model.vocabulary.Vocabulary;
import eu.agricore.indexer.model.vocabulary.VocabularyTopic;
import eu.agricore.indexer.model.vocabulary.VocabularyValue;

public class VocabularyServiceTests  extends IndexerBaseTest {
	
	@Autowired
	private VocabularyService vocabularyService;
	
	@Autowired
	private VocabularyValueService vocabularyValueService;
	
	@BeforeEach 
    void setUp() {
		vocabularyValueService.deleteAll();
    	vocabularyService.deleteAll();
    }
	
	/**********************************************************************************************************/
	/***************************************** VOCABULARY TESTS *****************************************/
	/**********************************************************************************************************/
	@Test
	void createVocabulary() {
		Vocabulary vocabulary = createNewVocabulary();
		
		Optional<Vocabulary> res = vocabularyService.findOne(vocabulary.getId());
		
		assertTrue(res.isPresent());
		assertEquals(res.get(), vocabulary);
	}
	
	@Test
	void findAllVocabularies() {
		Vocabulary vocabulary1 = createNewVocabulary();
		
		Vocabulary vocabulary2 = createNewVocabulary();
		
		List<Vocabulary> vocabularies = vocabularyService.findAll();
		
		assertTrue(vocabularies.size() == 2);
		assertEquals(vocabularies.get(0), vocabulary1);
		assertEquals(vocabularies.get(1), vocabulary2);
	}
	
	@Test
	void findVocabularyById() {
		Vocabulary vocabulary = createNewVocabulary();
		
		Optional<Vocabulary> res = vocabularyService.findOne(vocabulary.getId());
		
		assertTrue(res.isPresent());
		assertEquals(res.get(), vocabulary);
	}
	
	
	@Test
	void findNonExistingVocabularyById() {
		
		Long testingId = 100000L;
		
		Optional<Vocabulary> res = vocabularyService.findOne(testingId);
		
		assertFalse(res.isPresent());
	}
	
	@Test
	void findVocabularyByTopic() {
		Vocabulary vocabulary = createNewVocabulary();
		
		VocabularyTopic topic = getVocabularyTopic();
		
		List<Vocabulary> vocabularies = vocabularyService.findByTopic(topic);
		
		assertTrue(vocabularies.size() == 1);
		assertEquals(vocabularies.get(0), vocabulary);
	}
	
	@Test
	void updateVocabularyProperties() {
		
		Vocabulary vocabulary = createNewVocabulary();
		
		String newName = "Testing name";
		vocabulary.setName(newName);
		vocabularyService.save(vocabulary);
		
		Optional<Vocabulary> res = vocabularyService.findOne(vocabulary.getId());
		
		assertEquals(newName, res.get().getName());	
	}
	
	@Test
	void deleteVocabulary() {
		Vocabulary vocabulary = createNewVocabulary();
		
		vocabularyService.delete(vocabulary.getId());
		assertTrue(vocabularyService.findOne(vocabulary.getId()).isEmpty());
	}
	
	/**********************************************************************************************************/
	/***************************************** VOCABULARY VALUES TESTS *****************************************/
	/**********************************************************************************************************/
	@Test
	void createVocabularyValue() {
		
		// Create a new vocabulary
		Vocabulary vocabulary = createNewVocabulary();
		
		// Create a new vocabulary value and attach it to the vocabulary
		VocabularyValue vocabularyValue = createNewVocabularyValue(vocabulary.getId());
		
		Optional<VocabularyValue> res = vocabularyValueService.findOne(vocabularyValue.getId());
		
		assertTrue(res.isPresent());
		assertEquals(res.get(), vocabularyValue);
		assertEquals(res.get().getVocabulary().getId(), vocabulary.getId());
	}
	
	@Test
	void findVocabularyValueByVocabularyId() {
		
		// Create a new vocabulary
		Vocabulary vocabulary = createNewVocabulary();
		
		// Create a new vocabulary value and attach it to the vocabulary
		VocabularyValue vocabularyValue = createNewVocabularyValue(vocabulary.getId());
		
		List<VocabularyValue> res = vocabularyValueService.findValuesByVocabularyIdOrderByLabel(vocabulary.getId());
		
		assertTrue(res.size() == 1);
		assertEquals(res.get(0), vocabularyValue);
		assertEquals(res.get(0).getVocabulary().getId(), vocabulary.getId());
	}
	
	@Test
	void findVocabularyValueByExtraData() {
		
		// Create a new vocabulary
		Vocabulary vocabulary = createNewVocabulary();
		
		// Create a new vocabulary value and attach it to the vocabulary
		VocabularyValue vocabularyValue = createNewVocabularyValueWithExtraData(vocabulary.getId());
		
		// Extra data value
		String extraDataValue = vocabularyValue.getExtra_data();
		
		List<VocabularyValue> res = vocabularyValueService.findByExtraData(extraDataValue);
		
		assertTrue(res.size() > 0);
		assertEquals(res.get(0), vocabularyValue);
		assertEquals(res.get(0).getVocabulary().getId(), vocabulary.getId());
	}
	
	@Test
	void createVocabularyValueForNonExistingVocabulary() {
		
		Exception exception = assertThrows(NoSuchElementException.class, () -> {
			
			// Create a new vocabulary
			Long testingId = 10000L; // Non-existing vocabulary ID
			
			// Create a new vocabulary value and attach it to the vocabulary
			VocabularyValue vocabularyValue = createNewVocabularyValue(testingId);
			
			Optional<VocabularyValue> res = vocabularyValueService.findOne(vocabularyValue.getId());
			
			assertFalse(res.isPresent());
	    });
	 
	    String expectedMessage = "No value present";
	    String actualMessage = exception.getMessage();
	 
	    assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	void updateVocabularyValue() {
		
		// Create a new vocabulary
		Vocabulary vocabulary = createNewVocabulary();
		
		// Create a new vocabulary value and attach it to the vocabulary
		VocabularyValue vocabularyValue = createNewVocabularyValue(vocabulary.getId());
		
		// Update the vocabulary value
		String newLabel = "Testing label";
		vocabularyValue.setLabel(newLabel);
		vocabularyValueService.update(vocabularyValue);
		
		Optional<VocabularyValue> res = vocabularyValueService.findOne(vocabularyValue.getId());
		
		assertTrue(res.isPresent());
		assertEquals(res.get().getLabel(), newLabel);
		assertEquals(res.get(), vocabularyValue);
		assertEquals(res.get().getVocabulary().getId(), vocabulary.getId());
	}
	
	@Test
	void deleteVocabularyValue() {
		
		// Create a new vocabulary
		Vocabulary vocabulary = createNewVocabulary();
		
		// Create a new vocabulary value and attach it to the vocabulary
		VocabularyValue vocabularyValue = createNewVocabularyValue(vocabulary.getId());
		
		// Delete the vocabulary value
		vocabularyValueService.delete(vocabularyValue.getId());
		
		Optional<VocabularyValue> res = vocabularyValueService.findOne(vocabularyValue.getId());
		
		assertFalse(res.isPresent());
	}
	
	private Vocabulary createNewVocabulary() {
		Vocabulary vocabulary = new Vocabulary("iso639-2", "Languages vocabulary","http://id.loc.gov/vocabulary/iso639-2", VocabularyTopic.LANGUAGE);
		
		return vocabularyService.save(vocabulary);
	}
	
	private VocabularyValue createNewVocabularyValue(Long vocabularyId) {
		VocabularyValue vocabularyValue = new VocabularyValue("tai", "http://id.loc.gov/vocabulary/iso639-2/tai", "Tai languages", "");
		return vocabularyValueService.save(vocabularyId, vocabularyValue);
	}
	
	private VocabularyValue createNewVocabularyValueWithExtraData(Long vocabularyId) {
		VocabularyValue vocabularyValue = new VocabularyValue("bul", "http://id.loc.gov/vocabulary/iso639-2/bul", "Bulgarian", "Testing");
		return vocabularyValueService.save(vocabularyId, vocabularyValue);
	}
	
	private VocabularyTopic getVocabularyTopic() {
		return VocabularyTopic.LANGUAGE;
	}

}
