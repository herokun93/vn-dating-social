package vn.dating.app.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    @GetMapping("/public")
    @ResponseStatus(HttpStatus.OK)
    public String getPublic(){
        return "public";
    }

    @GetMapping("/private")
    @ResponseStatus(HttpStatus.OK)
    public String getPrivate(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        List<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
            Jwt jwt = jwtAuthenticationToken.getToken();

            log.info( jwt.getClaimAsStringList("realm_access").toString());
        }



        return "Logged in as: " + authentication.getName() + "\nRoles: " + roles;
    }
}
