package eu.agricore.indexer.util;

import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {
	
	@Value("${cookie.name}")
    private String tokenCookieName;
	
	@Value("${cookie.domain}")
    private String tokenCookieDomain;
	
	@Value("${cookie.expirationTimeInSeconds}")
	private Integer expirationInSeconds;
    
    public Cookie createTokenCookie(String token) {
        
        Cookie cookie = new Cookie(tokenCookieName, token);
        cookie.setHttpOnly(true);
        //cookie.setSecure(true); //TODO: enable for HTTPS connections
        cookie.setMaxAge(expirationInSeconds);
        cookie.setDomain(tokenCookieDomain);
        cookie.setPath("/");
        
        return cookie;
    }

    public Cookie deleteTokenCookie() {
    	
    	Cookie cookie = new Cookie(tokenCookieName, null);
        cookie.setHttpOnly(true);
        //cookie.setSecure(true); //TODO: enable for HTTPS connections
        cookie.setMaxAge(0); //Expiration date to 0 to remove the cookie
        cookie.setDomain(tokenCookieDomain);
        cookie.setPath("/");
        
        return cookie;
    }

}
