package eu.agricore.indexer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import eu.agricore.indexer.model.vocabulary.Vocabulary;
import eu.agricore.indexer.model.vocabulary.VocabularyTopic;

public interface VocabularyRepository extends CrudRepository<Vocabulary, Long> {
	
	@Query("select v from Vocabulary v where v.topic = :#{#topic}")
	public List<Vocabulary> findByTopic(@Param("topic") VocabularyTopic topic);

}
