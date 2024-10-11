package eu.agricore.indexer.controller.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import eu.agricore.indexer.ldap.service.AppUserService;
import eu.agricore.indexer.ldap.service.LdapUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import eu.agricore.indexer.ldap.dto.LdapUserDTO;
import eu.agricore.indexer.ldap.dto.RoleWrapper;
import eu.agricore.indexer.ldap.dto.UserCredentials;
import eu.agricore.indexer.ldap.model.LdapUser;
import eu.agricore.indexer.service.UserManagementService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin(allowCredentials = "true")
@RestController
@RequestMapping("/api/v1/users")
public class UserManagementController {
	
	@Autowired
	private UserManagementService userManagementService;
	@Autowired
	private AppUserService appUserService;
	@Autowired
	private LdapUserService ldapService;
	
	@GetMapping("")
	public ResponseEntity<Object> getAllUsers() {
		
		List<LdapUser> userList = userManagementService.findAll();
		List<LdapUserDTO> userListDTO = new ArrayList<>();
		
		userListDTO = userList.stream()
			.map(LdapUserDTO::new)
			.collect(Collectors.toList());
		
	   return ResponseEntity.ok()
        		.body(userListDTO);
	}
	
	@GetMapping("/roles")
	public ResponseEntity<Object> getAllRoles() {
		
		Set<String> rolesAvailable = userManagementService.getAllRoles();
		
	   return ResponseEntity.ok()
        		.body(rolesAvailable);
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<Object> getUser(@PathVariable String username) {
		
		Optional<LdapUser> optionalUser = userManagementService.find(username);
		
		if(optionalUser.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Current user not found");
		}
		
		LdapUserDTO userDTO = new LdapUserDTO(optionalUser.get());
		
		return ResponseEntity.ok()
        		.body(userDTO);	
	}
	
	@PostMapping("")
	public ResponseEntity<?> createUser(@RequestBody @Valid LdapUser user) {
		
		LdapUser newUser = userManagementService.create(user); // Create a new user account
		
		LdapUserDTO userDTO = new LdapUserDTO(newUser);
        
	    return ResponseEntity.ok(userDTO);
	}






	
	@PutMapping("/{username}")
	public ResponseEntity<?> updateUser(@RequestBody @Valid LdapUserDTO user, @PathVariable String username) {
	
		if(!user.getUsername().equals(username)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username doesn't match");
		}
		
		Optional<LdapUser> res = userManagementService.update(user);
		if(res.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Current user not found");
		}
		
		LdapUserDTO userDTO = new LdapUserDTO(res.get());
	    
	    return ResponseEntity.ok(userDTO);
	}
	
	@PutMapping("/{username}/password")
	public ResponseEntity<?> updateUserPassword(@RequestBody @Valid UserCredentials userCredentials, @PathVariable String username) {
		
		if(!userCredentials.getUsername().equals(username)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username doesn't match");
		}
		
		// Check that the new password and its confirmation match
		if(!userCredentials.getNewPassword().equals(userCredentials.getNewPasswordConfirm())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password does not match");
		}
		
		// Check that the user exists
		getLdapUser(username); 
		
		Optional<LdapUser> userModified = userManagementService.updatePassword(userCredentials.getUsername(), userCredentials.getNewPassword());
		LdapUserDTO userModifiedDTO = new LdapUserDTO(userModified.get());
        
        return ResponseEntity.ok(userModifiedDTO);
	}
	
	@PutMapping("/{username}/roles")
	public ResponseEntity<?> updateUserRoles(@RequestBody @Valid RoleWrapper roleWrapper, @PathVariable String username) {
		
		LdapUser user = getLdapUser(username);
		
		LdapUser userModified = userManagementService.updateUserRoles(user, roleWrapper.getRoles());
		LdapUserDTO userModifiedDTO = new LdapUserDTO(userModified);
        
        return ResponseEntity.ok(userModifiedDTO.getRoles());
	}

	@DeleteMapping("/{username}")
	public ResponseEntity<?> deleteUser(@PathVariable String username) {

		LdapUser user = getLdapUser(username); // Check if the user exists

		// Check if user is verified
		if (!appUserService.checkIfUserIsVerified(username)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Your email address is not verified");
		}

		// Check if the user account is disabled
		if (!appUserService.checkIfUserIsDisabled(username)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Your user account has been blocked");
		}
			
		userManagementService.delete(user);
		
		return ResponseEntity.noContent().build();
	}
	
	private LdapUser getLdapUser(String username) throws ResponseStatusException {
		
		Optional<LdapUser> optionalUser = userManagementService.find(username);
		
		if(optionalUser.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Current user not found");
		}
		return optionalUser.get();
	}


	private Boolean isCurrentUser(final String username) {
		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
		String currentUsername = auth.getPrincipal().toString();
		return currentUsername.equals(username);
	}


	private LdapUser getCurrentLdapUser() throws ResponseStatusException {
		Optional<LdapUser> optionalUser = getOptionalCurrentLdapUser();
		if (optionalUser.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Current user not found");
		}
		return optionalUser.get();
	}

	private Optional<LdapUser> getOptionalCurrentLdapUser() throws ResponseStatusException {
		String username = getCurrentUsername();
		Optional<LdapUser> optionalUser = ldapService.find(username);
		return optionalUser;
	}

	private String getCurrentUsername() {
		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
		String currentUsername = auth.getPrincipal().toString();
		return currentUsername;
	}

}
