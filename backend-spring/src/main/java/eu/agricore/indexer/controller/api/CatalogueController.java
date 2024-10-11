package eu.agricore.indexer.controller.api;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import eu.agricore.indexer.model.Catalogue;
import eu.agricore.indexer.service.CatalogueService;

@CrossOrigin(allowCredentials = "true")
@RestController
@RequestMapping("/api/v1/datasets/catalogues")
public class CatalogueController {
	
	@Autowired
	private CatalogueService catalogueService;
	
	@GetMapping("")
	public ResponseEntity<Object> getAllCatalogues() {
		
		List<Catalogue> catalogues = catalogueService.findAll();
		
	   return ResponseEntity.ok()
        		.body(catalogues);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> getCatalogue(@PathVariable Long id) {
		
		Optional<Catalogue> catalogue = catalogueService.findOne(id);
		
		if(catalogue.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Catalogue not found");
		}
		
		return ResponseEntity.ok()
        		.body(catalogue);	
	}
	
	@PostMapping("")
	public ResponseEntity<Object> createCatalogue(@RequestBody @Valid Catalogue catalogue) {
       
		Catalogue newCatalogue = catalogueService.save(catalogue);
        
        return ResponseEntity.ok()
        		.body(newCatalogue);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateCatalogue(@RequestBody @Valid Catalogue catalogue, @PathVariable Long id) {
	
		if(!catalogue.getId().equals(id)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Catalogue id doesn't match");
		}
		
		if(catalogueService.findOne(id).isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Catalogue not found");
		}
		
		Catalogue catalogueModified = catalogueService.update(catalogue);
		
	    return ResponseEntity.ok(catalogueModified);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteCatalogue(@PathVariable Long id) {
		
		if(catalogueService.findOne(id).isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Catalogue not found");
		}
		
		catalogueService.delete(id);
		
		return ResponseEntity.noContent().build();
	}
}
