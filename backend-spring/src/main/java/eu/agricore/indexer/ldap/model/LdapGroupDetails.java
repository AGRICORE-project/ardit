package eu.agricore.indexer.ldap.model;


import java.util.List;

public class LdapGroupDetails {

	private Long groupId;
	private List<String> groupMembers;
	private String groupName;


	public LdapGroupDetails() {
	}

	public LdapGroupDetails(Long groupId, List<String> groupMembers, String groupName) {
		this.groupId = groupId;
		this.groupMembers = groupMembers;
		this.groupName = groupName;
	}


	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public List<String> getGroupMembers() {
		return groupMembers;
	}

	public void setGroupMembers(List<String> groupMembers) {
		this.groupMembers = groupMembers;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
}
