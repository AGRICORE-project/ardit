package eu.agricore.indexer.util;

import java.util.ArrayList;
import java.util.List;

import eu.agricore.indexer.dto.DatasetSimplifiedDTO;
import eu.agricore.indexer.model.dataset.Dataset;
import eu.agricore.indexer.model.distribution.Distribution;

/**
 * Utility class to convert instances of the dataset entity to a simplified DTO with less properties
 * Can be used to present a downsized version of the dataset without having to load all its properties
 * 
 **/
public final class DatasetEntityToSimplifiedDTO {
	
	public static DatasetSimplifiedDTO convertEntityToDTO(Dataset datasetEntity) {
		
		DatasetSimplifiedDTO simplifiedDataset = new DatasetSimplifiedDTO();
		
		simplifiedDataset.setId(datasetEntity.getId());
		simplifiedDataset.setTitle(datasetEntity.getTitle());
		simplifiedDataset.setDraft(datasetEntity.getDraft());
		simplifiedDataset.setProducer(datasetEntity.getProducer());
		simplifiedDataset.setDatasetType(datasetEntity.getDatasetType().toString());
		simplifiedDataset.setLastUpdateDateTime(datasetEntity.getLastUpdateDateTime());
		simplifiedDataset.setCreationDateTime(datasetEntity.getCreationDateTime());
		simplifiedDataset.setTmpExtentFrom(datasetEntity.getTmpExtentFrom());
		simplifiedDataset.setTmpExtentTo(datasetEntity.getTmpExtentTo());
		simplifiedDataset.setOwner(datasetEntity.getOwner());
		
		if(datasetEntity.getPeriodicity() != null) simplifiedDataset.setPeriodicity(datasetEntity.getPeriodicity().getLabel());
		
		if (datasetEntity.getDistributions().size() > 0) {
			simplifiedDataset.setFormats(getDistributionsFormats(datasetEntity.getDistributions()));
		} else {
			simplifiedDataset.setFormats(new ArrayList<String>());
		}
		
		return simplifiedDataset;
	}
	
	private static List<String> getDistributionsFormats(List<Distribution> distributions) {
		
		List<String> formats = new ArrayList<String>();
		
		for(Distribution distribution: distributions) {
			formats.add(distribution.getFormat().getLabel());
		}
		
		return formats;	
	}
}
