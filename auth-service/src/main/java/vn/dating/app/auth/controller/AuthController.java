package vn.dating.app.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    @GetMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public String getToken(Principal principal){
        System.out.println("Handler");
        if(principal==null){
            return "principal is null";
        }
        System.out.println(principal.getName());
//        System.out.println(principal.getName());
        return principal.getName();
    }
}
