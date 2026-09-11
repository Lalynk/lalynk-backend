package com.lalynk.lalynk_backend.shared;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "redirect:/oauth2/authorization/auth0";
    }


    @GetMapping("/me")
    @ResponseBody
    public AuthDTO getUser(Authentication authentication) {
        if(authentication == null || !(authentication.getPrincipal() instanceof OidcUser)) {
            return new AuthDTO(false, null, null);
        }
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        return new AuthDTO(true, oidcUser.getSubject(), oidcUser.getEmail());

    }


}
