package eu.agricore.indexer.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import eu.agricore.indexer.util.AuthFailureHandler;

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.exceptionHandling().authenticationEntryPoint(new AuthFailureHandler());
		http
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
			.authorizeRequests()
				// Root
				.antMatchers(HttpMethod.GET, "/").permitAll()
				.antMatchers(HttpMethod.GET, "/api/v1").permitAll()
				// Swagger UI
				.antMatchers(
						"/v2/api-docs",
		                "/configuration/ui",
		                "/swagger-resources/**",
		                "/configuration/security",
		                "/swagger-ui.html",
		                "/webjars/**",
		                "/error").hasAnyAuthority("ADMIN","MANTAINER","EDITOR","USER")
				// Auth
				.antMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
				.antMatchers(HttpMethod.POST, "/api/v1/auth/me").hasAnyAuthority("ADMIN","MANTAINER","EDITOR","USER")
				.antMatchers(HttpMethod.POST, "/api/v1/auth/register").permitAll()
				.antMatchers(HttpMethod.POST, "/api/v1/auth/register/confirm/**").permitAll()
				.antMatchers(HttpMethod.GET, "/api/v1/auth/register/**").permitAll()
				.antMatchers(HttpMethod.POST, "/api/v1/auth/recoverPassword").permitAll()
				.antMatchers(HttpMethod.POST, "/api/v1/auth/newPassword").permitAll()
				.antMatchers(HttpMethod.POST, "/api/v1/auth/logout").permitAll()
				.antMatchers(HttpMethod.DELETE, "/api/v1/auth/me").hasAnyAuthority("ADMIN","MANTAINER","EDITOR","USER")
				.antMatchers(HttpMethod.POST, "/api/v1/auth/subscribe").hasAnyAuthority("ADMIN","MANTAINER","EDITOR","USER")
				// User management
				.antMatchers(HttpMethod.POST, "/api/v1/users/**").hasAuthority("ADMIN")
				.antMatchers(HttpMethod.GET, "/api/v1/users/**").hasAnyAuthority("ADMIN","MANTAINER","EDITOR","USER")
				.antMatchers(HttpMethod.PUT, "/api/v1/users/**").hasAnyAuthority("ADMIN", "MANTAINER")
				.antMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasAnyAuthority("ADMIN","MANTAINER")
				//Dataset management
				.antMatchers(HttpMethod.POST, "/api/v1/datasets/**").hasAnyAuthority("ADMIN","MANTAINER","EDITOR","USER")
				.antMatchers(HttpMethod.GET, "/api/v1/datasets/**").permitAll()
				.antMatchers(HttpMethod.PUT, "/api/v1/datasets/**").hasAnyAuthority("ADMIN","MANTAINER","EDITOR","USER")
				.antMatchers(HttpMethod.DELETE, "/api/v1/datasets/**").hasAnyAuthority("ADMIN","MANTAINER","EDITOR")
				//Dataset properties descriptions management
				.antMatchers(HttpMethod.POST, "/api/v1/datasets/descriptions/**").hasAnyAuthority("ADMIN","MANTAINER")
				.antMatchers(HttpMethod.GET, "/api/v1/datasets/descriptions/**").hasAnyAuthority("ADMIN","MANTAINER")
				.antMatchers(HttpMethod.PUT, "/api/v1/datasets/descriptions/**").hasAnyAuthority("ADMIN","MANTAINER")
				.antMatchers(HttpMethod.DELETE, "/api/v1/datasets/descriptions/**").hasAnyAuthority("ADMIN","MANTAINER")
				// Dataset export
				.antMatchers(HttpMethod.GET, "/api/v1/export/datasets/**").hasAnyAuthority("ADMIN")
				//Vocabulary management
				.antMatchers(HttpMethod.POST, "/api/v1/vocabularies/**").hasAnyAuthority("ADMIN","MANTAINER", "EDITOR")
				.antMatchers(HttpMethod.GET, "/api/v1/vocabularies/**").permitAll()
				.antMatchers(HttpMethod.PUT, "/api/v1/vocabularies/**").hasAnyAuthority("ADMIN","MANTAINER", "EDITOR")
				.antMatchers(HttpMethod.DELETE, "/api/v1/vocabularies/**").hasAnyAuthority("ADMIN","MANTAINER", "EDITOR")
				// Comments
				.antMatchers(HttpMethod.POST, "/api/v1/comments/**").hasAnyAuthority("ADMIN","MANTAINER","EDITOR","USER")
				.antMatchers(HttpMethod.GET, "/api/v1/comments/**").permitAll()
				.antMatchers(HttpMethod.PUT, "/api/v1/comemnts/**").hasAnyAuthority("ADMIN","MANTAINER","EDITOR","USER")
				.antMatchers(HttpMethod.DELETE, "/api/v1/comments/purge/**").hasAnyAuthority("ADMIN","MANTAINER")
				.antMatchers(HttpMethod.DELETE, "/api/v1/comments/**").hasAnyAuthority("ADMIN","MANTAINER","EDITOR","USER")
				// Catalogue
				.antMatchers(HttpMethod.POST, "/api/v1/datasets/catalogues/**").hasAnyAuthority("ADMIN","MANTAINER","EDITOR")
				.antMatchers(HttpMethod.GET, "/api/v1/datasets/catalogues/**").permitAll()
				.antMatchers(HttpMethod.PUT, "/api/v1/datasets/catalogues/**").hasAnyAuthority("ADMIN","MANTAINER","EDITOR")
				.antMatchers(HttpMethod.DELETE, "/api/v1/datasets/catalogues/**").hasAnyAuthority("ADMIN","MANTAINER","EDITOR")
				// Contact Email
				.antMatchers(HttpMethod.POST, "/api/v1/contact/**").permitAll()
				// Help
				.antMatchers(HttpMethod.GET, "/api/v1/help/**").permitAll()
				.antMatchers(HttpMethod.PUT, "/api/v1/help/**").hasAnyAuthority("ADMIN","MANTAINER","EDITOR")
				.antMatchers(HttpMethod.POST, "/api/v1/help/**").hasAnyAuthority("ADMIN","MANTAINER","EDITOR")




				.anyRequest().authenticated()
			.and()
			.cors()
			.and()
			.csrf().disable()
			.headers()
	        .xssProtection()
	        .and()
	        .contentSecurityPolicy("script-src 'self'");
	}
	
	// Remove comments to allow public access to swagger interface
    /*@Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(// 
        		"/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**",
                "/error");
    }*/
	
}
