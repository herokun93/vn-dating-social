package vn.dating.app.auth;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
public class AuthApplication {
    public static void main(String[] args) {

        SpringApplication.run(AuthApplication.class, args);
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
