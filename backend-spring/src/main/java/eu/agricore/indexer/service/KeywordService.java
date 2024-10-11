package eu.agricore.indexer.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.agricore.indexer.model.dataset.Keyword;
import eu.agricore.indexer.repository.KeywordRepository;

@Service
public class KeywordService {
	@Autowired
	private KeywordRepository keywordRepository;
	
	@Transactional(readOnly = true)
	public Optional<Keyword> findById(Long id) {
		return keywordRepository.findById(id);
	}
	
	@Transactional(readOnly = true)
	public List<Keyword> findByLabel(String label) {
		
		// Limit the results obtained to the first 10 values found
		Pageable paging = PageRequest.of(0, 10);
		
		Page<Keyword> pageKeywords = keywordRepository.findByLabel(label, paging);
		
		return pageKeywords.getContent();
	}

	@Transactional
	public Keyword save(Keyword keyword) {
		return keywordRepository.save(keyword);
	}
	
	@Transactional
	public void delete(Long valueId) {
		keywordRepository.deleteById(valueId);
	}

}
