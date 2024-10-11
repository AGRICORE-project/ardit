package eu.agricore.indexer.controller.api;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
public class VocabularyController {
	
	@Autowired
	private VocabularyService vocabularyService;
	
	@Autowired
	private VocabularyValueService vocabularyValueService;
	
	@GetMapping("")
	public ResponseEntity<Object> getAllVocabularies() {
		
		List<Vocabulary> vocabulariesList = vocabularyService.findAll();
		
	   return ResponseEntity.ok()
        		.body(vocabulariesList);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> getVocabularyById(@PathVariable Long id) {
		
		Optional<Vocabulary> vocabulary = vocabularyService.findOne(id);
		
		if(vocabulary.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vocabulary not found");
		}
		
		return ResponseEntity.ok()
        		.body(vocabulary);	
	}
	
	@GetMapping("/topics")
	public ResponseEntity<Object> getVocabularyTopics() {
		
		List<String> topics = Stream.of(VocabularyTopic.values())
	            .map(Enum::name)
	            .collect(Collectors.toList());

		
		return ResponseEntity.ok()
        		.body(topics);	
	}
	
	@PostMapping("")
	public ResponseEntity<Object> createVocabulary(@RequestBody @Valid Vocabulary vocabulary) {
        
		// Save the new vocabulary
		Vocabulary newVocabulary = vocabularyService.save(vocabulary);
        
        return ResponseEntity.ok()
        		.body(newVocabulary);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateVocabulary(@RequestBody @Valid Vocabulary vocabulary, @PathVariable Long id) {
	
		if(!vocabulary.getId().equals(id)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vocabulary id doesn't match");
		}
		
		if(vocabularyService.findOne(id).isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vocabulary not found");
		}
		
		Vocabulary vocabularyModified = vocabularyService.update(vocabulary);
		
	    return ResponseEntity.ok(vocabularyModified);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteVocabulary(@PathVariable Long id) {
		
		if(vocabularyService.findOne(id).isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vocabulary not found");
		}
		
		try {
			
			List<VocabularyValue> values = vocabularyValueService.findValuesByVocabularyIdOrderByLabel(id); //Get vocabulary values
			
			for(VocabularyValue value: values) {
				vocabularyValueService.delete(value.getId()); // First delete all values if it is possible
			}
			
			vocabularyService.delete(id); // Delete the vocabulary
			return ResponseEntity.noContent().build();
			
        } catch (DataIntegrityViolationException e) {
        	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The vocabulary might be used by a dataset. Try deleting its values first");
        }
	}
}
