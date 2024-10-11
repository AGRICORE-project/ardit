package eu.agricore.indexer.repository;


import eu.agricore.indexer.model.Help;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HelpRepository extends CrudRepository<Help, Long> {

    List<Help> findAllByOrderByCreatedAtDesc();

    @Query("select h from Help h where h.owner = :#{#owner}")
    List<Help> findByOwner(@Param("owner") String owner);

    @Modifying
    @Query("update Help set owner='Unknown' where owner = :#{#owner}")
    public void deleteHelpUnknown(@Param("owner")String owner);

}
