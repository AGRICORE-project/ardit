package eu.agricore.indexer.util;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTService {
	
	@Value("${jwt.id}")
	private String id;
	
	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expirationMinutes}")
	private Integer expirationInMinutes;
	
	@Value("${cookie.name}")
	private String jwtCookieName;
	
	private final String HEADER = "Authorization";
	
	private final String PREFIX = "Bearer ";
	
	public String generateToken(String username, Collection<? extends GrantedAuthority>  grantedAuthorities) {
		String token = Jwts
				.builder()
				.setId(id)
				.setSubject(username)
				.claim("authorities",
						grantedAuthorities.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + (expirationInMinutes * 60 * 1000) * 3)) // 3 hours long
				.signWith(SignatureAlgorithm.HS512,
						secret.getBytes()).compact();
		return PREFIX + token;
	}
	
	public String generateToken(String username, Set<String> authorities) {
		String[] buffer = new String[authorities.size()];
		authorities.toArray(buffer);
		
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.createAuthorityList(buffer);
		
		return this.generateToken(username, grantedAuthorities);
	}
	
	public Claims validateToken(String token) {
		
		if(token.contains(PREFIX)) token = token.replace(PREFIX, "");
		
		return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
	}
	
	public String checkJWTTokenExists(HttpServletRequest request, HttpServletResponse res) {
		
		// First check if JWT is in the cookie
		String jwt = checkJWTInCookie(request);
		if(jwt.isEmpty() && checkJWTInHeader(request)) {
			jwt = request.getHeader(HEADER); //JWT is in the Authorization header
		}
		return jwt;
	}
	
	public String checkJWTInCookie(HttpServletRequest request) {
		Cookie cookies[] = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if(cookie.getName().equals(jwtCookieName)) {
					return cookie.getValue(); //JWT
				}
			}
		}
		return "";
	}
	
	public Boolean checkJWTInHeader(HttpServletRequest request) {
		String authenticationHeader = request.getHeader(HEADER);
		return (authenticationHeader != null && authenticationHeader.startsWith(PREFIX));
	}
}
