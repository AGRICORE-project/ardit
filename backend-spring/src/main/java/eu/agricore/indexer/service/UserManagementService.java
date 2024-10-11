package eu.agricore.indexer.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.NameAlreadyBoundException;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;

import eu.agricore.indexer.ldap.dto.LdapUserDTO;
import eu.agricore.indexer.ldap.exception.UserAlreadyExistException;
import eu.agricore.indexer.ldap.model.LdapUser;
import eu.agricore.indexer.ldap.repository.LdapUserRepository;
import eu.agricore.indexer.ldap.service.AppUserService;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserManagementService {
	
	@Autowired
	private LdapUserRepository repository;
	
	@Autowired
	private AppUserService appUserService;
    
    public List<LdapUser> findAll() {
    	return repository.findAll();
    }
   
    public Optional<LdapUser> find(final String username) {
    	return repository.find(username);
    }
    
    public Set<String> getRolesByUser(final String username) {
    	return repository.getRolesByUser(username);
    }
    
    public Set<String> getAllRoles() {
    	return repository.getAllRoles();
    }
    
    public LdapUser create(LdapUser user) throws UserAlreadyExistException {
    	
        LdapUser res; // Create LDAP user
        
        Set<String> desirableRoles = user.getRoles(); // Roles to be assigned to the user
        
    	try {
    		
    		res = repository.create(user); // Create the user
    		
    		res.setRoles(new HashSet<String>()); // Clear the set of roles of the returned user to avoid errors when updating
    		
    		res = updateUserRoles(res, desirableRoles); // Assign the desirable roles
    		
        } catch(NameAlreadyBoundException e)  {
        	throw new UserAlreadyExistException("User already exists");
        }
    	
    	// Create App user
    	appUserService.createAppUser(user);
    	
        return res;
    }
    
    public Optional<LdapUser> update(LdapUserDTO userDTO) {
    	LdapUser user = new LdapUser(userDTO.getUsername(), "", userDTO.getEmail());
		appUserService.updateAppUser(userDTO);
    	return repository.update(user);
    }
    
    public LdapUser updateUserRoles(LdapUser user, Set<String> newRoles) {
   
    	Set<String> allRoles = repository.getAllRoles();
    	Set<String> userRoles = getRolesByUser(user.getUsername());
    	
    	//Role filtering: deletes invalid roles
    	newRoles.retainAll(allRoles);
    	
    	//Add default role 'USER'
		// TODO remove this line if we can erase multiple roles
    	newRoles.add(repository.getDefaultRoleName());
    	
    	//Get roles to remove from the user
    	Set<String> rolesToRemove = Sets.newHashSet(userRoles);
    	rolesToRemove.removeAll(newRoles);
    	
    	//Delete roles
    	for (String role: rolesToRemove) {
    		 user = repository.deleteFromGroup(user, role);
    	}
    	
    	//Add new roles
    	for (String role: newRoles) {
    		user = repository.addToGroup(user, role);
    	}
    	
    	//Clear LdapUser instance roles
    	user.setRoles(getRolesByUser(user.getUsername()));
    	return user;
    	
    }
    
    public Optional<LdapUser> updatePassword(final String username, final String password) {
    	Optional<LdapUser> res = repository.updatePassword(username, password);
    	return res;
    }

	@Transactional
    public void delete(LdapUser user) {
    	
    	// Delete LDAP user
    	repository.delete(user);
    	// Remove user entry of each group
    	for(String group: user.getRoles()) {
    		repository.deleteFromGroup(user, group);
    	}
    	
    	// Delete AppUser
    	appUserService.removeAppUser(user.getUsername());
    }



}