package vn.dating.app.social.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    private final KeycloakLogoutHandler keycloakLogoutHandler;

    public SecurityConfig(KeycloakLogoutHandler keycloakLogoutHandler) {
        this.keycloakLogoutHandler = keycloakLogoutHandler;
    }



    @Bean
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/api/v1/social/users/public").permitAll()
                .antMatchers("/api/v1/social/users/private").permitAll()
                .anyRequest()
                .permitAll();

        http.oauth2Login()
                .and()
                .logout()
                .addLogoutHandler(keycloakLogoutHandler)
                .logoutSuccessUrl("/");

        http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .build();
    }
}


//    @Override
//    public void configure(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity
//                .cors().and().csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/api/v1/social/**").permitAll()
//                .antMatchers("/api/v1/social/users/public").permitAll()
//                //.antMatchers("/api/v1/social/users/create").hasIpAddress(CommonAPI.SOCIAL_HAS_IP_URL)
//                .antMatchers("/api/v1/social/users/create").permitAll()
//                .antMatchers("/ws/**").permitAll()
//                .anyRequest().permitAll()
////                .and()
////                .sessionManagement()
////                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
////                .and()
////                .cors()
////                .and()
////                .csrf()
////                .disable()
////                .oauth2ResourceServer()
////                .jwt()
//        ;
//    }
//@Override
//protected void configure(HttpSecurity http) throws Exception {
//    http.oauth2Client()
//            .and()
//            .oauth2Login()
//            .tokenEndpoint()
//            .and()
//            .userInfoEndpoint();
//
//    http.sessionManagement()
//            .sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
//
//    http.authorizeRequests(authorizeRequests ->
//                    authorizeRequests
//                            .antMatchers("/api/v1/social/users/public").permitAll() // Public routes
//                            .anyRequest().authenticated());
//}
//
//
//}