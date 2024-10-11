package eu.agricore.indexer.util;

import java.util.Set;

import com.google.common.collect.Sets;

public class RoleUtils {
	// Roles definition for testing purposes
	public static final String USER_ROLE = "USER";
	public static final String MANTAINER_ROLE = "MANTAINER";
	public static final String ADMIN_ROLE = "ADMIN";
	
	public static Set<String> defaultRoles(){
		return Sets.newHashSet(USER_ROLE);
	}
	
	public static Set<String> adminRoles(){
		return Sets.newHashSet(USER_ROLE, ADMIN_ROLE);
	}
	
	public static Set<String> mantainerRoles(){
		return Sets.newHashSet(USER_ROLE, MANTAINER_ROLE);
	}
	
	public static Set<String> allRoles(){
		return Sets.newHashSet(USER_ROLE, ADMIN_ROLE, MANTAINER_ROLE);
	}
	
}
