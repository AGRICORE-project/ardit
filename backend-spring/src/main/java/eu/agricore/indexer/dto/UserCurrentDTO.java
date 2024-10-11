package eu.agricore.indexer.dto;

import java.util.ArrayList;
import java.util.List;


public class UserCurrentDTO {
	private String username;
	private List<String> roles;
	
	public UserCurrentDTO() {
		this.username = new String();
		this.roles = new ArrayList<>();
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
}
