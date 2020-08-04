package com.udemy.data.jpa.auth.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.SessionFlashMapManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        SessionFlashMapManager flashMapManager = new SessionFlashMapManager();
        /*para agregar mensajes al flash, no se puede hacer de la misma manera q en los controladores*/
        FlashMap flashMap = new FlashMap();

        flashMap.put("success", "Hola " + authentication.getName() + ", haz iniciado sesion con exito");

        flashMapManager.saveOutputFlashMap(flashMap,request,response);

        if (authentication != null){
            logger.info("El usuario " + authentication.getName() + " ha iniciado sesion con exito");
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
