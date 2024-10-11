package eu.agricore.indexer.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import eu.agricore.indexer.model.dataset.Dataset;

import java.util.List;

public interface DatasetRepository extends PagingAndSortingRepository<Dataset, Long>, JpaSpecificationExecutor<Dataset>{
	
	//public Page<Dataset> findAllByOrderByLastUpdateDateTimeDesc(Pageable pageable);
	
	@Query("select d from Dataset d where d.catalogue.id = :#{#catalogueId}")
	public Page<Dataset> findByCatalogue(@Param("catalogueId")Long catalogueId, Pageable pageable);

	@Query("select d from Dataset d where d.draft = false and d.catalogue.id = :#{#catalogueId}")
	public Page<Dataset> findByCatalogueNotLogged(@Param("catalogueId")Long catalogueId,Pageable pageable);

	@Query("select d from Dataset d where d.draft = true and d.catalogue.id = :#{#catalogueId} and d.owner = :#{#owner}")
	public List<Dataset> findListByCatalogueUser(@Param("catalogueId")Long catalogueId, @Param("owner")String owner);

	@Query("select d from Dataset d where d.draft = false and d.catalogue.id = :#{#catalogueId}")
	public List<Dataset> findListByCatalogueNotLogged(@Param("catalogueId")Long catalogueId);

	@Modifying
	@Query("update Dataset set catalogue.id=null where catalogue.id = :#{#catalogueId}")
	public void emptyCatalogue(@Param("catalogueId")Long catalogueId);

	@Modifying
	@Query("update Dataset set owner='unknown' where owner = :#{#owner}")
	public void deleteDatasetUnknown(@Param("owner")String owner);
	
}
