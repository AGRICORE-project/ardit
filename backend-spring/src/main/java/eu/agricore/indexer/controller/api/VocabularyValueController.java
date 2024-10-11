package eu.agricore.indexer.controller.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import eu.agricore.indexer.model.vocabulary.Vocabulary;
import eu.agricore.indexer.model.vocabulary.VocabularyTopic;
import eu.agricore.indexer.model.vocabulary.VocabularyValue;
import eu.agricore.indexer.service.VocabularyService;
import eu.agricore.indexer.service.VocabularyValueService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin(allowCredentials = "true")
@RestController
@RequestMapping("/api/v1/vocabularies")
public class VocabularyValueController {
	
	@Autowired
	private VocabularyService vocabularyService;
	
	@Autowired
	private VocabularyValueService vocabularyValueService;
	
	@GetMapping("/{vocabularyId}/values")
	public ResponseEntity<Object> getValuesByVocabularyId(@PathVariable Long vocabularyId) {
		
		Optional<Vocabulary> vocabulary = vocabularyService.findOne(vocabularyId);
		
		// Check if the vocabulary exists
		if(vocabulary.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vocabulary not found");
		}
		
		VocabularyTopic topic = vocabulary.get().getTopic();
		
		List<VocabularyValue> vocabularyValues = new ArrayList<>();
		
		if (topic.equals(VocabularyTopic.NUTS1) || 
				topic.equals(VocabularyTopic.NUTS2) ||
				topic.equals(VocabularyTopic.NUTS3))  {
			
			vocabularyValues = vocabularyValueService.findValuesByVocabularyIdOrderByCode(vocabularyId);
		} else {
			vocabularyValues =vocabularyValueService.findValuesByVocabularyIdOrderByLabel(vocabularyId);
		}
		
		
	   return ResponseEntity.ok()
        		.body(vocabularyValues);
	}
	

	@GetMapping(value="", params = "topic")
	public ResponseEntity<Object> getVocabularyValuesByTopic(@RequestParam(value = "topic") String topic) {
		
		try {
			VocabularyTopic vc = VocabularyTopic.valueOf(topic.toUpperCase());
			
			List<Vocabulary> vocabularies = vocabularyService.findByTopic(vc);
			
			ArrayList<List<VocabularyValue>> values = new ArrayList<>();
			
			for(Vocabulary voc: vocabularies ) {
				
				if (topic.toUpperCase().equals(VocabularyTopic.NUTS1.toString()) || 
						topic.toUpperCase().equals(VocabularyTopic.NUTS2.toString()) ||
						topic.toUpperCase().equals(VocabularyTopic.NUTS3.toString()))  {
					
					values.add(vocabularyValueService.findValuesByVocabularyIdOrderByCode(voc.getId()));
				} else {
					values.add(vocabularyValueService.findValuesByVocabularyIdOrderByLabel(voc.getId()));
				}
			}
			
			return ResponseEntity.ok()
	        		.body(values); // Return a list of vocabularies related with the given topic
        } catch (IllegalArgumentException e) {
        	throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vocabulary topic not found");
        }
	}
	
	@GetMapping(value="", params = "extra_data")
	public ResponseEntity<Object> getVocabularyValuesByExtraData(@RequestParam(value = "extra_data") String extraData) {
		
		try {
			return ResponseEntity.ok()
	        		.body(vocabularyValueService.findByExtraData(extraData.toUpperCase()));
        } catch (IllegalArgumentException e) {
        	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vocabulary values not found");
        }
	}
	
	@GetMapping(value="/{nuts}", params = {"code", "level", "mappedBy"})
	public ResponseEntity<Object> getVocabularyValuesByCodeStartsWith(@RequestParam(value = "code") List<String> code, @RequestParam(value = "level") String level, @RequestParam(value = "mappedBy") String mappedBy) {
		
		try {
			return ResponseEntity.ok()
					.body(vocabularyValueService.findValuesByCodeStartsWith(code, level, mappedBy));
		} catch (IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vocabulary value not found");
		}
	}
	
	@PostMapping("/{vocabularyId}/values")
	public ResponseEntity<Object> createVocabularyValue(@PathVariable Long vocabularyId, @RequestBody @Valid VocabularyValue vocabularyValue) {
		
		// Check if the vocabulary exists
		if(vocabularyService.findOne(vocabularyId).isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vocabulary not found");
		}
        
		// Save the new vocabulary value
		VocabularyValue newVocabularyValue = vocabularyValueService.save(vocabularyId, vocabularyValue);
        
        return ResponseEntity.ok()
        		.body(newVocabularyValue);
	}
	
	@PutMapping("/{vocabularyId}/values/{valueId}")
	public ResponseEntity<?> updateVocabularyValue(@PathVariable Long vocabularyId, @PathVariable Long valueId, @RequestBody @Valid VocabularyValue vocabularyValue) {
		
		Optional<Vocabulary> vocabulary = vocabularyService.findOne(vocabularyId); // Given vocabulary
		Optional<VocabularyValue> vocValue = vocabularyValueService.findOne(valueId); // Given vocabulary value
		
		// Check if the vocabulary exists
		if(vocabulary.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vocabulary not found");
		}
		
		// Check if the vocabulary value exists
		if(vocValue.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vocabulary value not found");
		}
		
		// Check if value id is correct
		if(!vocabularyValue.getId().equals(valueId)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vocabulary value id doesn't match");
		}
		
		// Check if the vocabulary value belongs to the given vocabulary
		if(!vocValue.get().getVocabulary().getId().equals(vocabularyId)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vocabulary value does not belong to the given vocabulary");
		}

		vocabularyValue.setVocabulary(vocabulary.get());
		VocabularyValue vocabularyValueModified = vocabularyValueService.update(vocabularyValue); // Update the vocabulary
		
	    return ResponseEntity.ok(vocabularyValueModified);
	}
	
	@DeleteMapping("/{vocabularyId}/values/{valueId}")
	public ResponseEntity<?> deleteVocabularyValue(@PathVariable Long vocabularyId, @PathVariable Long valueId) {
		
		Optional<Vocabulary> vocabulary = vocabularyService.findOne(vocabularyId); // Given vocabulary
		Optional<VocabularyValue> vocValue = vocabularyValueService.findOne(valueId); // Given vocabulary value
		
		// Check if the vocabulary exists
		if(vocabulary.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vocabulary not found");
		}
		
		// Check if the vocabulary value exists
		if(vocValue.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vocabulary value not found");
		}
		
		// Check if the vocabulary value belongs to the given vocabulary
		if(!vocValue.get().getVocabulary().getId().equals(vocabularyId)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vocabulary value does not belong to the given vocabulary");
		}
		
		try {
			vocabularyValueService.delete(valueId);
			return ResponseEntity.noContent().build();
			
        } catch (DataIntegrityViolationException e) {
        	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The vocabulary value is being used by a dataset");
        }
	}
}
