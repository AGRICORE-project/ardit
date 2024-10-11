package eu.agricore.indexer.ldap.model;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.sun.istack.NotNull;

public class LdapUser {

	@NotNull()
	@NotEmpty()
	private String username;
	
	@NotNull()
	@NotEmpty()
	@Size(min = 8, message = "The password must be at least 8 characters long")
	private String password;
	
	@NotNull()
	@NotEmpty()
	@Email()
	private String email;
    
	private Set<String> roles;
	
	public LdapUser() {
		this.username = new String();
		this.password = new String();
		this.email = new String();
		this.roles = new HashSet<String>();
	}
	
	public LdapUser(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.roles = new HashSet<>();
	}
	
	public LdapUser(String username, String password, String email, Set<String> roles) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.roles = roles;
	}

	
	public String getUsername() {
		return username;
	}

	public LdapUser setUsername(String username) {
		this.username = username;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public LdapUser setPassword(String password) {
		this.password = password;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public LdapUser setEmail(String email) {
		this.email = email;
		return this;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public LdapUser setRoles(Set<String> roles) {
		this.roles = roles;
		return this;
	}
}
