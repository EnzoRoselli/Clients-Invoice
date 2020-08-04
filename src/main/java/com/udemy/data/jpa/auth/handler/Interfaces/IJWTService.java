package com.udemy.data.jpa.auth.handler.Interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;
import java.util.Collection;

public interface IJWTService {
    public String create(Authentication authentication) throws JsonProcessingException;
    public boolean validate(String token);
    /*obtienen su informacion del token*/
    public Claims getClaims(String token);
    public String getUsername(String token);
    public Collection<? extends GrantedAuthority> getRoles(String token) throws IOException;
    /*para quitar el 'Bearer ' del token*/
    public String resolve(String token);
}
