package eu.agricore.indexer.util;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class AuthFailureHandler implements AuthenticationEntryPoint {

	  @Override
	  public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e)
	      throws IOException, ServletException {
		  
		  Date date = new Date();
		  
		  httpServletResponse.setContentType("application/json");
		  httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

	      httpServletResponse.getOutputStream().println(
	    		  "{ "
	    				  + "\"timestamp\":" + new Timestamp(date.getTime()) + ","
	    			      + "\"status\": 401,"
	    			      + "\"error\": \"Unauthorized\","
	    				  + "\"message\": \"Access denied\","
			              + "}");
	  }
}