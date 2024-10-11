package eu.agricore.indexer.security;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import eu.agricore.indexer.util.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

public class JWTAuthorizationFilter extends OncePerRequestFilter {
	
	private final Logger log = LoggerFactory.getLogger(JWTAuthorizationFilter.class);
	
	private JWTService jwtService;	
	
	private final String AUTH_PREFIX = "[AUTH]";
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
		
		// Lazy set on the first call
		if(jwtService==null){
            ServletContext servletContext = request.getServletContext();
            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            jwtService = webApplicationContext.getBean(JWTService.class);
        }
		
		try {
			String token = jwtService.checkJWTTokenExists(request, response);
			if (token != "") {
				Claims claims = jwtService.validateToken(token);
				if (claims.get("authorities") != null) {
					setUpSpringAuthentication(claims);
				} else {
					SecurityContextHolder.clearContext();
				}
			}
			
			chain.doFilter(request, response);
			
		} catch (MalformedJwtException e) {
			int sc = HttpServletResponse.SC_BAD_REQUEST;
			sendJWTErrorResponse(response, "Malformed JWT token", sc);
			return;
		} catch (UnsupportedJwtException e) {
			int sc = HttpServletResponse.SC_BAD_REQUEST;
			sendJWTErrorResponse(response, "Unsuported JWT token", sc);
			return;
		} catch (ExpiredJwtException e) {
			int sc = 440; // HTTP login timeout code
			sendJWTErrorResponse(response, "Session expired. Please log in again", sc);
			return;
		} catch (JwtException e) {
			
			log.info("Invalid token (JwtException).");
	        log.trace("Invalid token (JwtException): {}", e);
			
			int sc = HttpServletResponse.SC_BAD_REQUEST;
			sendJWTErrorResponse(response, "Invalid token", sc);
			return;
		}
	}	
	
	private void sendJWTErrorResponse(HttpServletResponse response, String message, int sc) throws IOException {
		response.setStatus(sc);
		 response.sendError(sc, AUTH_PREFIX + " " + message);
	}

	private void setUpSpringAuthentication(Claims claims) {
		List<String> authorities = (List<String>) claims.get("authorities");
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
				authorities.stream()
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList())
				);
		SecurityContextHolder.getContext().setAuthentication(auth);

	}
}
