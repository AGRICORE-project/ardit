package eu.agricore.indexer.controller.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import eu.agricore.indexer.model.dataset.DatasetDescription;
import eu.agricore.indexer.model.dataset.DatasetDescriptionWrapper;
import eu.agricore.indexer.model.dataset.DatasetProperty;
import eu.agricore.indexer.service.DatasetDescriptionService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin(allowCredentials = "true")
@RestController
@RequestMapping("/api/v1/datasets/descriptions")
public class DatasetDescriptionController {
	
	@Autowired
	private DatasetDescriptionService datasetDescriptionService;
	
	
	@GetMapping("")
	public ResponseEntity<Object> getAllDescriptions() {
		
		List<DatasetDescription> descriptions = datasetDescriptionService.findAll();
		
	   return ResponseEntity.ok()
        		.body(descriptions);
	}
	
	@GetMapping(value="", params = "property")
	public ResponseEntity<Object> getDatasetDescriptionByProperty(@RequestParam(value = "property") String property) {
		
		try {
			DatasetProperty propertyEnumValue = DatasetProperty.valueOf(property.toUpperCase());
			return ResponseEntity.ok()
	        		.body(datasetDescriptionService.findByProperty(propertyEnumValue));
        } catch (IllegalArgumentException e) {
        	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dataset property not found");
        }
	}
	
	// TODO: secure the method to avoid creating duplicate descriptions
	/*@PostMapping("")
	public ResponseEntity<Object> createDatasetDescription(@RequestBody @Valid DatasetDescription description) {
        
		// Save the new description
		DatasetDescription newDescritpion = datasetDescriptionService.save(description);
        
        return ResponseEntity.ok()
        		.body(newDescritpion);
	}*/
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateDatasetDescription(@PathVariable Long id, @RequestBody @Valid DatasetDescriptionWrapper descriptionWrapper) {
		
		if(datasetDescriptionService.findOne(id).isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dataset property description not found");
		}
		
		DatasetDescription descriptionModified = datasetDescriptionService.update(id, descriptionWrapper.getDescritpion());
		
	    return ResponseEntity.ok(descriptionModified);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteDatasetDescription(@PathVariable Long id) {
		
		if(datasetDescriptionService.findOne(id).isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dataset property description not found");
		}
		
		DatasetDescription descriptionDeleted = datasetDescriptionService.delete(id);
		
		return ResponseEntity.ok(descriptionDeleted);
	}
}
