package vn.dating.app.social.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import vn.dating.common.api.CommonAPI;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


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
@Override
protected void configure(HttpSecurity http) throws Exception {
    http.oauth2Client()
            .and()
            .oauth2Login()
            .tokenEndpoint()
            .and()
            .userInfoEndpoint();

    http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.ALWAYS);

    http.authorizeRequests(authorizeRequests ->
                    authorizeRequests
                            .antMatchers("/api/v1/social/users/public").permitAll() // Public routes
                            .anyRequest().authenticated());
}


}