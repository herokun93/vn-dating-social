package vn.dating.app.gateway.configs;



import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import reactor.core.publisher.Mono;


import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.AuthorizationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;




@EnableWebFluxSecurity
@KeycloakConfiguration
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

    private static final String ROLE_PREFIX = "role_"; // Adjust this based on your actual role naming convention
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
        SimpleAuthorityMapper mapper = new SimpleAuthorityMapper();
        mapper.setConvertToUpperCase(true);
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(mapper);

        auth.authenticationProvider(keycloakAuthenticationProvider);
    }

    @Bean
    public KeycloakSpringBootConfigResolver KeycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }
    @Bean
    protected SessionRegistry buildSessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {

        serverHttpSecurity
//                .csrf().csrfTokenRepository(CookieServerCsrfTokenRepository.withHttpOnlyFalse())
//                .and()
                .csrf().disable()
                .authorizeExchange(exchange ->
                        exchange.pathMatchers("/eureka/**").permitAll()
                                .pathMatchers("/api/auth/register").permitAll()
                                .pathMatchers("/api/auth/login").permitAll()
                                .pathMatchers("/api/auth/verify/**").permitAll()
                                .pathMatchers("/api/gateway/public").permitAll()
                                .pathMatchers("/api/gateway/gateway/**").permitAll()
                                .pathMatchers("/api/gateway/private").authenticated()
                                .pathMatchers("/api/gateway/auth/create").permitAll()
                                .pathMatchers("/api/social/auth/create").permitAll()
                                .pathMatchers("/api/social/auth/public").permitAll()
                                .pathMatchers("/api/social/posts/create").access(this::isRoleUser)
                                .pathMatchers("/api/social/posts/upload/**").permitAll()
                                .pathMatchers("/api/social/media/**").permitAll()
                                .pathMatchers("/api/social/communities/**").permitAll()
                                .pathMatchers("/api/social/posts/public").permitAll()
                                .pathMatchers("/api/social/public").permitAll()
                                .pathMatchers("/api/auth/private").access(this::isRoleUser)
                                .pathMatchers("/api/v1/social/users/public").permitAll()
                                .pathMatchers("/api/v1/social/users/private").access(this::isRoleUser)
                                .pathMatchers("/api/social/private").access(this::isRoleUser)
                                .pathMatchers("/api/auth/public").permitAll()
                               // .pathMatchers("/api/auth/public").access(this::currentUserMatchesPath)
                                .pathMatchers("/api/auth/add").permitAll()
                                //.pathMatchers("/api/auth/limit").hasIpAddress("127.0.0.1")
                                .pathMatchers("/api/auth/limit").hasIpAddress("127.0.0.1")

                                .pathMatchers("/api/upload").permitAll()
                                .pathMatchers("/api/v1/social/**").permitAll()
                                //.pathMatchers("/api/v1/social/users/create").hasIpAddress(CommonAPI.SOCIAL_HAS_IP_URL)
                                .anyExchange().authenticated())
                //.oauth2ResourceServer().jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter())
              .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt);
        return serverHttpSecurity.build();
    }

    private Mono<AuthorizationDecision> isRoleUser(Mono<Authentication> authenticationMono,
                                                   AuthorizationContext authorizationContext) {
        return authenticationMono
                .flatMap(authentication -> {
                    return getRoleFromToken(authentication,"user");
                });
    }
    private Mono<AuthorizationDecision> isRoleAdmin(Mono<Authentication> authenticationMono,
                                                   AuthorizationContext authorizationContext) {
        return authenticationMono
                .flatMap(authentication -> {
                    return getRoleFromToken(authentication,"admin");
                });
    }

    private Mono<AuthorizationDecision> getRoleFromToken(Authentication authentication, String checkRole) {
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
            Jwt jwt = jwtAuthenticationToken.getToken();
            List<String> roles = getRolesFromJwt(jwt);
            boolean hasUserRole = roles
                    .stream()
                    .anyMatch(role -> role.equals(checkRole)); // Change to your actual role name
            return Mono.just(new AuthorizationDecision(hasUserRole));
        }
        return Mono.just(new AuthorizationDecision(false));
    }

    private List<String> getRolesFromJwt(Jwt jwt) {
        List<String> roles = new ArrayList<>();
        List<String> realmAccess = jwt.getClaimAsStringList("realm_access");
        if (realmAccess != null && !realmAccess.isEmpty()) {
            String realmAccessJson = realmAccess.get(0);
            try {
                JsonNode realmAccessNode = new ObjectMapper().readTree(realmAccessJson);
                JsonNode rolesNode = realmAccessNode.get("roles");

                if (rolesNode != null && rolesNode.isArray() && rolesNode.size() > 0) {
                    for (JsonNode roleNode : rolesNode) {
                        roles.add(roleNode.asText());
                        System.out.println( roleNode.asText());
                    }
                }
            } catch (IOException e) {
                // Handle exception if needed
            }
        }
        return roles;
    }


}
