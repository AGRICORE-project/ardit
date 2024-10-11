package eu.agricore.indexer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import eu.agricore.indexer.model.dataset.DatasetDescription;
import eu.agricore.indexer.model.dataset.DatasetProperty;

public interface DatasetDescriptionRepository extends CrudRepository<DatasetDescription, Long> {
	
	@Query("select d from DatasetDescription d where d.property = :#{#property}")
	public DatasetDescription findByDatasetProperty(@Param("property") DatasetProperty property);
	
	public List<DatasetDescription> findAllByOrderByIdAsc();

}
