package eu.agricore.indexer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import eu.agricore.indexer.model.vocabulary.VocabularyValue;

public interface VocabularyValueRepository extends CrudRepository<VocabularyValue, Long>{
	
	@Query("select v from VocabularyValue v where v.vocabulary.id=?1 order by v.label asc")
	public List<VocabularyValue> findByVocabularyIdOrderByLabel(Long vocabularyId);
	
	@Query("select v from VocabularyValue v where v.vocabulary.id=?1 order by v.code asc")
	public List<VocabularyValue> findByVocabularyIdOrderByCode(Long vocabularyId);
	
	@Query("select v from VocabularyValue v where v.extraData = :#{#extra_data}")
	public List<VocabularyValue> findByExtraData(@Param("extra_data") String extraData);
	
	@Query("select v from VocabularyValue v where v.label = ?1 order by v.label asc")
	public List<VocabularyValue> findByLabel(String label);
	
	@Query("select v from VocabularyValue v where v.code = ?1 order by v.code asc")
	public List<VocabularyValue> findByCode(String code);
	
	@Query("select v from VocabularyValue v where v.code LIKE ?1% and length(v.code)=?2 order by v.code asc")
	public List<VocabularyValue> findByCodeStartsWith(String code, Integer level);

}
