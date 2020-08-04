package com.udemy.data.jpa.auth.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.udemy.data.jpa.auth.handler.Interfaces.IJWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Service
public class JWTService implements IJWTService {

    public static final String SECRET = Base64Utils.encodeToString("Alguna.Clave.Secreta.12345".getBytes());
    public static final long EXPIRATION_DATE =  3600000L * 4L;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    /*aca se crea el token JWT*/
    @Override
    public String create(Authentication authentication) throws JsonProcessingException {

        /*authentication es lo mismo que authToken del metodo attemptAuthentication, pero ya autenticado y aceptado*/
        String username = ((User) authentication.getPrincipal()).getUsername(); /*el principal tiene los datos del usuario*/

        Collection<? extends GrantedAuthority> roles = authentication.getAuthorities();

        Claims claims = Jwts.claims();
        claims.put("authorities", (new ObjectMapper().writeValueAsString(roles)));

        /*no se añade el password ya que no se permite pasar informacion sensible en los tokens*/
        String token = Jwts.builder()
                .setClaims(claims) /*paso los roles del usuario al token*/
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_DATE))
                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
                .compact();

        return token;
    }

    @Override
    public boolean validate(String token) {

        try{
            getClaims(token);
            return true;
        }catch (JwtException | IllegalArgumentException e){
            return false;
        }

    }

    @Override
    public Claims getClaims(String token) {
        Claims claims = null;

        claims = Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(resolve(token))
                .getBody(); /*retorna autorizaciones, username, fecha de creacion y expiracion*/

        return claims;
    }

    @Override
    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    @Override
    public Collection<? extends GrantedAuthority> getRoles(String token) throws IOException {
        Object roles = getClaims(token).get("authorities");

        Collection<? extends GrantedAuthority> authorities = Arrays.asList
                (new ObjectMapper().addMixIn(SimpleGrantedAuthority.class,
                        SimpleGrantedAuthorityMixIn.class).readValue(roles.toString().getBytes(),
                        SimpleGrantedAuthority[].class));

        return authorities;
    }

    @Override
    public String resolve(String token) {

        if (token != null && token.startsWith(TOKEN_PREFIX)){

            return token.replace(TOKEN_PREFIX, "");
        }else {
            return null;
        }

    }
}
