package eu.agricore.indexer.ldap.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.NameAlreadyBoundException;
import org.springframework.stereotype.Service;

import eu.agricore.indexer.ldap.dto.AuthRequest;
import eu.agricore.indexer.ldap.dto.LdapUserDTO;
import eu.agricore.indexer.ldap.exception.UserAlreadyExistException;
import eu.agricore.indexer.ldap.model.LdapUser;
import eu.agricore.indexer.ldap.repository.LdapUserRepository;

@Service
public class LdapUserService {

    @Autowired
    private LdapUserRepository repository;
    
    @Autowired
    private AppUserService appUserService;

    public Boolean authenticate(LdapUser user) {
    	return repository.authenticate(user);
    }
    
    public Boolean authenticate(AuthRequest authRequest) {
    	return repository.authenticate(authRequest.getUsername(), authRequest.getPassword());
    }
    
    public Boolean authenticate(final String username, final String password) {
    	return repository.authenticate(username, password);
    }
    
    public LdapUser create(LdapUser user) throws UserAlreadyExistException {
        LdapUser res;
    	try {
    		res = repository.create(user);
    		res = addToGroup(res, "user");
        } catch(NameAlreadyBoundException e)  {
        	throw new UserAlreadyExistException("User already exists");
        }
        return res;
    }


    public void deleteLdapUser(String username) {

        // Delete LDAP user
        repository.deleteByUsername(username);
        // Remove user entry of each group
        Set<String> roles = getRolesByUser(username);

        LdapUser ldapUser = new LdapUser();
        ldapUser.setRoles(roles);
        ldapUser.setUsername(username);

        for(String group: roles) {
            repository.deleteFromGroup(ldapUser, group);
        }

    }


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
    
    public Optional<LdapUser> find(final String username) {
    	Optional<LdapUser> res = repository.find(username);
    	return res;
    }
    
    public Set<String> getRolesByUser(final String username) {
    	Set<String> res = repository.getRolesByUser(username);
    	return res;
    }

    public Optional<LdapUser> update(LdapUserDTO userDTO) {
    	Optional<LdapUser> res;
    	LdapUser user = new LdapUser(userDTO.getUsername(), "", userDTO.getEmail());
    	res = repository.update(user);
    	return res;
    }

    public Optional<LdapUser> updatePassword(final String username, final String password) {
    	Optional<LdapUser> res = repository.updatePassword(username, password);
    	return res;
    }
    
    private LdapUser addToGroup(LdapUser user, String group) {
    	LdapUser res = repository.addToGroup(user, group);
    	return res;
    }
}
