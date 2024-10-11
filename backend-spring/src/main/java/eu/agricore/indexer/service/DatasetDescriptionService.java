package eu.agricore.indexer.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.agricore.indexer.model.dataset.DatasetDescription;
import eu.agricore.indexer.model.dataset.DatasetProperty;
import eu.agricore.indexer.repository.DatasetDescriptionRepository;

@Service
public class DatasetDescriptionService {
	
	@Autowired
	private DatasetDescriptionRepository datasetDescriptionRepository;
	
	@Transactional(readOnly = true)
	public Optional<DatasetDescription> findOne(Long id) {
		Optional<DatasetDescription> description = datasetDescriptionRepository.findById(id);
		return description;
	}
	
	@Transactional(readOnly = true)
	public DatasetDescription findByProperty(DatasetProperty property) {
		return datasetDescriptionRepository.findByDatasetProperty(property);
	}
	
	@Transactional(readOnly = true)
	public List<DatasetDescription> findAll() {
		return (List<DatasetDescription>) datasetDescriptionRepository.findAllByOrderByIdAsc();
	}
	
	@Transactional
	public DatasetDescription save(DatasetDescription description) {
		return datasetDescriptionRepository.save(description);
	}
	
	@Transactional
	public DatasetDescription update(Long id, String newDescription) {
		DatasetDescription currentDatasetDescription = this.findOne(id).get(); // Get current value
		
		currentDatasetDescription.setDescription(newDescription); // Update the description field with a new value
		
		return datasetDescriptionRepository.save(currentDatasetDescription);
	}
	
	@Transactional
	public DatasetDescription delete(Long id) {
		DatasetDescription currentDatasetDescription = this.findOne(id).get(); // Get current value
		
		currentDatasetDescription.setDescription(""); // Update the description field with a empty value
		
		return datasetDescriptionRepository.save(currentDatasetDescription);
	}
}
