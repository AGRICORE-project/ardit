package eu.agricore.indexer.ldap.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class CurrentUserCredentials {
	
	@NotEmpty()
	private String username;
	
	@NotEmpty()
	private String currentPassword;
	
	@NotEmpty()
	@Size(min = 8, message = "The password must be at least 8 characters long")
	private String newPassword;
	
	@NotEmpty()
	@Size(min = 8, message = "The password must be at least 8 characters long")
	private String newPasswordConfirm;
	
	public CurrentUserCredentials(String username, String currentPassword, String newPassword, String newPasswordConfirm) {
		this.username = username;
		this.currentPassword = currentPassword;
		this.newPassword = newPassword;
		this.newPasswordConfirm = newPasswordConfirm;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getNewPasswordConfirm() {
		return newPasswordConfirm;
	}

	public void setNewPasswordConfirm(String newPasswordConfirm) {
		this.newPasswordConfirm = newPasswordConfirm;
	}
}
