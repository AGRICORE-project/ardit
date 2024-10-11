package eu.agricore.indexer.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.agricore.indexer.model.vocabulary.Vocabulary;
import eu.agricore.indexer.service.VocabularyService;

@Component
public class VocabularyLoader {

	@Autowired
	private ResourceLoader resourceLoader;
	
	@Autowired
	private VocabularyService vocabularyService;
	
	public List<Vocabulary> loadVocabularies() {
		
		List<Vocabulary> res = new ArrayList<>();
		
		// Recovering vocabulary resources
		Optional<Resource[]> oResources = getVocabularyResources();
		if (oResources.isEmpty()) return res;
	
		// Storing vocabularies
		Resource[] resources = oResources.get();
		for(Resource resource: resources) {
			Optional<Vocabulary> oVocabulary = storeVocabulary(resource);
			if(oVocabulary.isPresent()) {
				res.add(oVocabulary.get());
			}
		}
		
		return res;
	}
	
	
	private Optional<Resource[]> getVocabularyResources() {
		Optional<Resource[]> res = Optional.empty();
		try{
			Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources("classpath*:vocabularies/*.json");
			res = Optional.of(resources);
		}catch (IOException e) {
			System.err.println("[VocabularyLoader] Error loading the vocabulary files from resources: " + e.getMessage());
		}
		return res;
	}
	
	private Optional<Vocabulary> storeVocabulary(Resource resource) {
		Optional<Vocabulary> res = Optional.empty();
		
		ObjectMapper mapper = new ObjectMapper();
		TypeReference<Vocabulary> typeReference = new TypeReference<Vocabulary>(){};
		try {
			System.out.println("[VocabularyLoader] Loading vocabulary: " + resource.getFilename());
			
			InputStream inputStream = resource.getInputStream();
			Vocabulary vocabulary = mapper.readValue(inputStream, typeReference);
			vocabulary = vocabularyService.save(vocabulary);
			
			res = Optional.of(vocabulary);
			
			System.out.println("[VocabularyLoader] Vocabulary saved: " + resource.getFilename());
		} catch (IOException e) {
			System.err.println("[VocabularyLoader] Unable to load  the vocabulary: " + resource.getFilename());
		}
		return res;

	}
}
