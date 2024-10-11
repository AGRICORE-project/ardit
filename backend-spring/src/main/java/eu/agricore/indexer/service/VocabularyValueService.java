package eu.agricore.indexer.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.agricore.indexer.model.vocabulary.VocabularyValue;
import eu.agricore.indexer.repository.VocabularyValueRepository;

@Service
public class VocabularyValueService {
	
	@Autowired
	private VocabularyService vocabularyService;
	
	@Autowired
	private VocabularyValueRepository vocabularyValueRepository;
	
	@Transactional(readOnly = true)
	public List<VocabularyValue> findValuesByVocabularyIdOrderByLabel(Long vocabularyId) {
		return vocabularyValueRepository.findByVocabularyIdOrderByLabel(vocabularyId);
	}
	
	@Transactional(readOnly = true)
	public List<VocabularyValue> findValuesByVocabularyIdOrderByCode(Long vocabularyId) {
		return vocabularyValueRepository.findByVocabularyIdOrderByCode(vocabularyId);
	}
	
	@Transactional(readOnly = true)
	public List<VocabularyValue> findByExtraData(String extraData) {
		return vocabularyValueRepository.findByExtraData(extraData);
	}
	
	@Transactional(readOnly = true)
	public List<VocabularyValue> findByLabel(String label) {
		return vocabularyValueRepository.findByLabel(label);
	}
	
	/**
	 * Method used to filter NUTS values using a set of given NUTS codes
	 * @param codes: set of codes by which the NUTS searched should start with
	 * @param level: NUTS level 1, s2 or 3
	 * @param mappedBy: can take two values, "country" to get all country NUTS, or "region" to filter the NUTS by regions within the country
	 * @return a list of vocabulary values 
	 * 
	 * */
	@Transactional(readOnly = true)
	public List<VocabularyValue> findValuesByCodeStartsWith(List<String> codes, String levelString, String mappedBy) {
		
		List<VocabularyValue> valuesFound = new ArrayList<>();
		Integer level = Integer.parseInt(levelString);
		
		// Loop all the given codes
		for (String code: codes) {
			
			code = code.toUpperCase(); // Set the characters to upper case to match those stored in the DB (i.e: ES23)
			
			if (mappedBy.equals("country")) {
				
				code = code.substring(0, 2); // Keep only the country code to get all the NUTS values for that country (i.e: ES)
				
			} else if (mappedBy.equals("region")) {
				
				// Keep the country code and the region code to filter by region (i.e: ES2X for NUTS2, ES23X for NUTS3)
				code = code.substring(0, Integer.parseInt(levelString) + 1);
			}
			
			valuesFound.addAll(vocabularyValueRepository.findByCodeStartsWith(code, level + 2));
		}
		
		// Filter the list to delete duplicated NUTS values
		List<VocabularyValue> nutsWithoutDuplicates =  valuesFound.stream()
			     .distinct()
			     .collect(Collectors.toList());
		
		// Filter the list to avoid vocabulary values not related with the NUTS
		Iterator<VocabularyValue> it = nutsWithoutDuplicates.iterator();
		while (it.hasNext()) {
			VocabularyValue v = it.next();
			if (!v.getVocabulary().getTopic().toString().contains("NUTS")) {
				it.remove(); // Remove values that do not belong to NUTS vocabularies
			}
		}

		return nutsWithoutDuplicates;
	}
	
	@Transactional(readOnly = true)
	public Optional<VocabularyValue> findOne(Long valueId) {
		return vocabularyValueRepository.findById(valueId);
	}

	@Transactional
	public VocabularyValue save(Long vocabularyId, VocabularyValue vocabularyValue) {
		vocabularyValue.setVocabulary(vocabularyService.findOne(vocabularyId).get());
		return vocabularyValueRepository.save(vocabularyValue);
	}
	
	@Transactional
	public VocabularyValue update(VocabularyValue vocabularyValue) {
		if(!(vocabularyValue.getId() > 0)) {
			throw new IllegalArgumentException("Vocabulary value to update has not ID");
		}
		
		return vocabularyValueRepository.save(vocabularyValue);
	}
	
	@Transactional
	public void delete(Long valueId) {
		vocabularyValueRepository.deleteById(valueId);
	}
	
	@Transactional
	public void deleteAll() {
		vocabularyValueRepository.deleteAll();
	}
}
