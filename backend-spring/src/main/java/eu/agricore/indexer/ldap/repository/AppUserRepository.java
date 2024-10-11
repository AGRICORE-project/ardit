package eu.agricore.indexer.ldap.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import eu.agricore.indexer.ldap.model.AppUser;

import java.util.List;

public interface AppUserRepository extends CrudRepository<AppUser, Long> {
	
	@Query("select u.verified from AppUser u where u.username = ?1")
	public Boolean checkIfUserIsVerified(String username);
	
	@Query("select u.disabled from AppUser u where u.username = ?1")
	public Boolean checkIfUserIsDisabled(String username);
	
	@Query("select u from AppUser u where u.username = ?1")
	public AppUser findByUsername(String username);

	@Query("select u from AppUser u where u.email = ?1")
	public AppUser findByEmail(String email);

	@Query("select u from AppUser u where u.datasetSubscription = true")
	public List<AppUser> findUserDatasetSubscription();


}
