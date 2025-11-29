package com.digis01.MMateoProgramacionNCapas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("login")
public class LoginController {

    @GetMapping
    public String login() {
        return "Login";
    }

    @PostMapping
    public String loginPost() {
        //Hacemos peticion post con RestTemplate al endpoint del service
        //...
        //...
        //Todo salio bien 
        //En el result viene el Token
        //Extraigo del token el rol
        //Extraigo id del rol
        // Rol = "ADMIN"

        String rol = "ADMIN";
        int idUsuario = 1;
        
        if(rol.equalsIgnoreCase("ADMIN")){
            //Hago peticion con restTemplate a getAll
           
           //Mando informacion de usuarios para renderizar vista
            return "UsuarioIndex";
        }else{
            //Hago peticion con restTemplate getByI
            
            //Mando informacion de usuario para renderizar la vista
            
            return "UsuarioDetalles";
        }
        
    }

}
