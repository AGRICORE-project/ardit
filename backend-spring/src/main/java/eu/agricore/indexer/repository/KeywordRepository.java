package eu.agricore.indexer.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import eu.agricore.indexer.model.dataset.Keyword;

public interface KeywordRepository extends PagingAndSortingRepository<Keyword, Long>{
	
	@Query("select k from Keyword k where k.label LIKE ?1% order by k.label asc")
	public Page<Keyword> findByLabel(String label, Pageable pageable);

}
