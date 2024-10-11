package eu.agricore.indexer.repository;

import org.springframework.data.repository.CrudRepository;

import eu.agricore.indexer.model.datasetvariable.DatasetVariable;

public interface DatasetVariableRepository extends CrudRepository<DatasetVariable, Long> {

}
