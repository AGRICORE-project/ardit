package eu.agricore.indexer.repository;


import org.springframework.data.repository.CrudRepository;

import eu.agricore.indexer.model.Catalogue;

public interface CatalogueRepository extends CrudRepository<Catalogue, Long> {

}
