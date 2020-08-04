package com.udemy.data.jpa.auth.handler.filter;

import com.udemy.data.jpa.auth.handler.JWTService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private JWTService jwtService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JWTService jwtService) {
        super(authenticationManager);
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String header = request.getHeader(JWTService.HEADER_STRING);

        if (!requiresAuthentication(header)){ /*si no requiere autenticacion continuamos con la ejecucion, y salimos del filtro*/
            /*si en cambio necesita autenticacion, continuamos con el filtro para ver si tiene autorizacion*/
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = null;

        if (jwtService.validate(header)){

            authentication = new UsernamePasswordAuthenticationToken(jwtService.getUsername(header), null, jwtService.getRoles(header));
        }

        SecurityContextHolder.getContext().setAuthentication(authentication); /*este metodo es el encargado de autenticar al usuario dentro de la request*/
        chain.doFilter(request, response);
    }

    protected boolean requiresAuthentication(String header){
        if (header == null || !header.startsWith(JWTService.TOKEN_PREFIX)){
            return false;
        }

        return true;
    }
}
