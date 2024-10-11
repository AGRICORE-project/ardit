package eu.agricore.indexer.controller.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import eu.agricore.indexer.dto.DatasetDTO;
import eu.agricore.indexer.model.dataset.Dataset;
import eu.agricore.indexer.service.DatasetService;
import eu.agricore.indexer.util.DatasetEntityToDTO;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin(allowCredentials = "true")
@RestController
@RequestMapping("/api/v1/export/datasets")
public class DatasetExportController {
	
	@Autowired
	private DatasetService datasetService;
	
	@GetMapping(value="")
	public ResponseEntity<Object> getAllDatasets() {
		
		List<Dataset> datasetList = datasetService.findAll();
		
		List<DatasetDTO> datasetDTOList = new ArrayList<DatasetDTO>();
		for(Dataset dataset: datasetList) {
			DatasetDTO newDatasetDTO = DatasetEntityToDTO.convertEntityToDTO(dataset);
			datasetDTOList.add(newDatasetDTO);
		}
		
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=collection.json")
				.contentType(MediaType.APPLICATION_JSON)
				.body(datasetDTOList);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> getDataset(@PathVariable Long id) {
		
		Optional<Dataset> dataset = datasetService.findOne(id);
		
		if(dataset.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dataset not found");
		}
		
		DatasetDTO datasetDTO = DatasetEntityToDTO.convertEntityToDTO(dataset.get());
		
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + datasetDTO.getTitle() + ".json")
				.contentType(MediaType.APPLICATION_JSON)
        		.body(datasetDTO);	
	}
}