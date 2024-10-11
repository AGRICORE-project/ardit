package eu.agricore.indexer.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

/**
 * The need for this support class arises because in Spring, 
 * the Cookie class does not have any parameter to set the sameSite field/attribute 
 * to avoid CSRF attacks. The ResponseCookie class have other additional attributes
 * 
 * */
@Component
public class ResponseCookieUtil {
	
	@Value("${cookie.name}")
    private String tokenCookieName;
	
	@Value("${cookie.domain}")
    private String tokenCookieDomain;
	
	@Value("${cookie.expirationTimeInSeconds}")
	private Integer expirationInSeconds;
	
	@Value("${cookie.secure}")
	private Boolean secure;
    
    public ResponseCookie createTokenCookie(String token) {
    	
    	String tokenWithoutBearer = token.split(" ")[1]; // Response cookies do not allow empty spaces in the value -> Delete 'Bearer '
        
        ResponseCookie cookie = ResponseCookie.from(tokenCookieName, tokenWithoutBearer)
        		.maxAge(expirationInSeconds) 
                .domain(tokenCookieDomain)
                .path("/")
                .sameSite("Lax")
                .secure(secure) //TODO: enable for HTTPS connections
                .httpOnly(true)
                .build();
        
        return cookie;
    }

    public ResponseCookie deleteTokenCookie() {
    	
        ResponseCookie cookie = ResponseCookie.from(tokenCookieName, null)
        		.maxAge(0) 
                .domain(tokenCookieDomain)
                .path("/")
                .sameSite("Lax")
                //.secure(secure) //TODO: enable for HTTPS connections
                .httpOnly(true)
                .build();
        
        return cookie;
    }

}
