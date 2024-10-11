package eu.agricore.indexer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import eu.agricore.indexer.ldap.util.PasswordEncoder;
import eu.agricore.indexer.util.PlainPasswordEncoder;

@Configuration
@Profile("test")
public class IndexerConfigurationTest {
	
    @Bean
	public PasswordEncoder passwordEncoder() {
		return new PlainPasswordEncoder();
	}
    
}
