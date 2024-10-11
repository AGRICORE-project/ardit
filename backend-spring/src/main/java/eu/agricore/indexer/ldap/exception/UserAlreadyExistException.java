package eu.agricore.indexer.ldap.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserAlreadyExistException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8991429147447602598L;

	public UserAlreadyExistException(final String msg) {
        super(msg);
    }

}