package eu.agricore.indexer.repository;

import org.springframework.data.repository.CrudRepository;

import eu.agricore.indexer.model.analysisunit.AnalysisUnit;

public interface AnalysisUnitRepository extends CrudRepository<AnalysisUnit, Long> {

}
