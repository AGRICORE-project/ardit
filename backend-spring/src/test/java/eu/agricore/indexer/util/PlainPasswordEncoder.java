package eu.agricore.indexer.util;

import org.springframework.stereotype.Component;

import eu.agricore.indexer.ldap.util.PasswordEncoder;

@Component
public class PlainPasswordEncoder implements PasswordEncoder {

	@Override
	public String encondePassword(String password) {
		return password;
	}

}
