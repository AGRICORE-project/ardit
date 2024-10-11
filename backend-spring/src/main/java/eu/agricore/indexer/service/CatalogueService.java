package eu.agricore.indexer.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.agricore.indexer.model.Catalogue;
import eu.agricore.indexer.repository.CatalogueRepository;

@Service
public class CatalogueService {
	
	@Autowired
	private CatalogueRepository catalogueRepository;
	
	@Autowired
	private DatasetService datasetService;
	
	@Transactional(readOnly = true)
	public Optional<Catalogue> findOne(Long id) {
		Optional<Catalogue> catalogue = catalogueRepository.findById(id);
		return catalogue;
	}
	
	/*@Transactional(readOnly = true)
	public List<Catalogue> findByTitle(String title) {
		return catalogueRepository.findByTitle(title);
	}*/
	
	@Transactional(readOnly = true)
	public List<Catalogue> findAll() {
		return (List<Catalogue>) catalogueRepository.findAll();
	}
	
	@Transactional
	public Catalogue save(Catalogue catalogue) {
		return catalogueRepository.save(catalogue);
	}
	
	@Transactional
	public Catalogue update(Catalogue catalogue) {
		return catalogueRepository.save(catalogue);
	}
	
	@Transactional
	public void delete(Long id) {
		datasetService.emptyCatalogue(id); // Remove the association between the given catalogue and its datasets
		
		catalogueRepository.deleteById(id); // Delete the catalogue
	}
	
	@Transactional
	public void deleteAll() {
		catalogueRepository.deleteAll();
	}
}
