package eu.agricore.indexer;

import eu.agricore.indexer.interceptor.RateLimitInterceptor;
import eu.agricore.indexer.ldap.service.AppUserService;
import eu.agricore.indexer.service.UserManagementService;
import org.apache.tomcat.util.http.LegacyCookieProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import sendinblue.ApiClient;
import sendinblue.ApiException;
import sendinblue.Configuration;
import sendinblue.auth.ApiKeyAuth;
import sibApi.AccountApi;
import sibApi.TransactionalEmailsApi;
import sibModel.*;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class IndexerApplication implements WebMvcConfigurer {

    @Autowired
    private UserManagementService userManagementService;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private RateLimitInterceptor interceptor;

    public static void main(String[] args) {

        SpringApplication.run(IndexerApplication.class, args);

	}


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor)
                .addPathPatterns("/api/v1/**");
    }

    /**
     * This method corrects an error generated by Tomcat when a cookie is set with spaces in the value
     * In our case, we have one space between the Bearer statement and the token chain
     */
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> cookieProcessorCustomizer() {
        return (serverFactory) -> serverFactory.addContextCustomizers(
                (context) -> context.setCookieProcessor(new LegacyCookieProcessor()));
    }
}
