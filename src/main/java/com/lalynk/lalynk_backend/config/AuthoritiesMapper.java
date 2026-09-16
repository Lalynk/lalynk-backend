package com.lalynk.lalynk_backend.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AuthoritiesMapper implements GrantedAuthoritiesMapper {

    @Override
    public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {

        Set<GrantedAuthority> mappedAuthorities = new HashSet<>(authorities);

        for(GrantedAuthority authority: authorities) {
            if(authority instanceof OidcUserAuthority oidcUserAuthority) {
                List<String> roles = oidcUserAuthority.getIdToken().getClaim("https://api.lalynk.com/roles");
                for(String role: roles) {
                    mappedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role));
                }
            }
        }

        return mappedAuthorities;
    }
}
