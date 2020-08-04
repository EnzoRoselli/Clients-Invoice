package com.udemy.data.jpa.auth.handler.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udemy.data.jpa.auth.handler.JWTService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    private JWTService jwtService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JWTService jwtService) {
        this.authenticationManager = authenticationManager;
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/login", "POST"));
        this.jwtService = jwtService;
    }

    /*aca se hace la autenticacion y se genera el token de Spring(con username,password,authorities,enable,etc.)*/
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = this.obtainUsername(request);
        String password = this.obtainPassword(request);

        if (username != null && password != null){
            logger.info("Username desde request parameter(form-data): " + username);
            logger.info("Password desde request parameter(form-data): " + password);
        }else{
            com.udemy.data.jpa.models.entities.User user = null;
            try {
                user = new ObjectMapper().readValue(request.getInputStream(), com.udemy.data.jpa.models.entities.User .class);

                username = user.getUsername();
                password = user.getPassword();

                logger.info("Username desde request InputStream(raw-json): " + username);
                logger.info("Password desde request InputStream(raw-json): " + password);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authToken);
    }

    /*aca se crea el token JWT(con toda la informacion del Spring token, y ademas la firma),
    este metodo se llama si funciona con exito el attemptAuthentication y se
    genero en token de Spring*/
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        String token = jwtService.create(authResult);

        response.addHeader(JWTService.HEADER_STRING, token); /*pasamos el token con toda la info en la response, en el header*/

        Map<String, Object> body = new HashMap<>();

        /*otra forma de pasar el token(seria pasarlo por body)*/
        body.put("token", token);
        body.put("user", (User) authResult.getPrincipal()); /*pasamos el user, pero el de userDetails(que ya tiene el username,password,authorities,enabled,etc..)*/
        body.put("mensaje", "Hola " + authResult.getName() + ", has iniciado sesion con exito");

        /*empezamos a hacer el response de nuestra request, y le pasamos el token y el resto de datos cargados*/
        response.getWriter().write(new ObjectMapper().writeValueAsString(body)); /*ObjectMapper pasa cualquier Map a JSON*/
        response.setStatus(200);
        response.setContentType("application/json");
    }

    /*este mensaje se ejecuta si falla el metodo attemptAuthentication*/
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        Map<String, Object> body = new HashMap<>();

        body.put("mensaje", "Error de autenticacion: username o password incorrecto");
        body.put("error", failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(401);
        response.setContentType("application/json");
    }
}
