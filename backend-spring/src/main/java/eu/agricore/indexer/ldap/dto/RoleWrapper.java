package eu.agricore.indexer.ldap.dto;

import java.util.Set;

import javax.validation.constraints.NotEmpty;

import com.sun.istack.NotNull;

public class RoleWrapper {
	
	@NotNull()
	@NotEmpty()
	Set<String> roles;

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
}
