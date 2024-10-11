package eu.agricore.indexer.ldap.dto;

import javax.validation.constraints.NotEmpty;

public class AuthRequest {
	
	@NotEmpty()
	private String username;
	
	@NotEmpty()
	private String password;
	
	public AuthRequest(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	public AuthRequest setUsername(String username) {
		this.username = username;
		return this;
	}
	public String getPassword() {
		return password;
	}
	public AuthRequest setPassword(String password) {
		this.password = password;
		return this;
	}
	
	
}
