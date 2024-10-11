package eu.agricore.indexer.ldap.dto;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.sun.istack.NotNull;

import eu.agricore.indexer.ldap.model.LdapUser;

public class LdapUserDTO {
	
	@NotNull()
	@NotEmpty()
	private String username;
	
	@NotNull()
	@NotEmpty()
	@Email()
	private String email;
    
	private Set<String> roles;

	private Boolean subscribed;
	
	public LdapUserDTO() {
		this.username = new String();
		this.email = new String();
		this.roles = new HashSet<String>();

	}

	public LdapUserDTO(LdapUser user) {
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.roles = user.getRoles();
	}

	
	public String getUsername() {
		return username;
	}

	public LdapUserDTO setUsername(String username) {
		this.username = username;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public LdapUserDTO setEmail(String email) {
		this.email = email;
		return this;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public LdapUserDTO setRoles(Set<String> roles) {
		this.roles = roles;
		return this;
	}

	public Boolean getSubscribed() {
		return subscribed;
	}

	public void setSubscribed(Boolean subscribed) {
		this.subscribed = subscribed;
	}
}
