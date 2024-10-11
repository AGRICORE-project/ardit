package eu.agricore.indexer.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.agricore.indexer.model.vocabulary.Vocabulary;
import eu.agricore.indexer.model.vocabulary.VocabularyTopic;
import eu.agricore.indexer.repository.VocabularyRepository;

@Service
public class VocabularyService {
	
	@Autowired
	private VocabularyRepository vocabularyRepository;
	
	@Transactional(readOnly = true)
	public Optional<Vocabulary> findOne(Long id) {
		Optional<Vocabulary> vocabulary = vocabularyRepository.findById(id);
		return vocabulary;
	}
	
	@Transactional(readOnly = true)
	public List<Vocabulary> findByTopic(VocabularyTopic topic) {
		return vocabularyRepository.findByTopic(topic);
	}
	
	@Transactional(readOnly = true)
	public List<Vocabulary> findAll() {
		return (List<Vocabulary>) vocabularyRepository.findAll();
	}

	@Transactional
	public Vocabulary save(Vocabulary vocabulary) {
		return vocabularyRepository.save(vocabulary);
	}
	
	@Transactional
	public Vocabulary update(Vocabulary vocabulary) {
		if(!(vocabulary.getId() > 0)) {
			throw new IllegalArgumentException("Vocabulary to update has not ID");
		}
		return vocabularyRepository.save(vocabulary);
	}
	
	@Transactional
	public void delete(Long id) {
		vocabularyRepository.deleteById(id);
	}
	
	@Transactional
	public void deleteAll() {
		vocabularyRepository.deleteAll();
	}
}
