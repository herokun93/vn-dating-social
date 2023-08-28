package vn.dating.app.social;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
public class SocialApplication {
    public static void main(String[] args) {

        SpringApplication.run(SocialApplication.class, args);
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

//    @Bean
//    @Scope(scopeName = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
//    public AccessToken getAccessToken() {
//        return ((KeycloakPrincipal) getRequest().getUserPrincipal()).getKeycloakSecurityContext().getToken();
//    }
//
//    @Bean
//    @Scope(scopeName = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
//    public KeycloakSecurityContext getKeycloakSecurityContext() {
//        return ((KeycloakPrincipal) getRequest().getUserPrincipal()).getKeycloakSecurityContext();
//    }
//
//    private HttpServletRequest getRequest() {
//        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
//    }
}
