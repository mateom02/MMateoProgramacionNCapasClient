package com.digis01.MMateoProgramacionNCapas.controller;

import com.digis01.MMateoProgramacionNCapas.ML.LoginRequest;
import com.digis01.MMateoProgramacionNCapas.ML.Result;
import jakarta.servlet.http.HttpSession;
import java.io.StringReader;
import java.util.Base64;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("login")
public class LoginController {

    private final static String urlBase = "http://localhost:8080/";

    @GetMapping
    public String login(Model model) {
        LoginRequest login = new LoginRequest();
        model.addAttribute("loginRequest", login);
        return "Login";
    }

    @PostMapping
    public String loginPost(@ModelAttribute("loginRequest") LoginRequest loginRequest, Model model, HttpSession httpSession) {
        //Hacemos peticion post con RestTemplate al endpoint del service

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders jsonHeader = new HttpHeaders();
        jsonHeader.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LoginRequest> body = new HttpEntity<>(loginRequest, jsonHeader);

        try {
            ResponseEntity<Result> responseEntity = restTemplate.exchange(urlBase + "api/auth/login", HttpMethod.POST, body, Result.class);

            if (responseEntity.getStatusCode().value() == 200) {
                Result result = responseEntity.getBody();
                String token = (String) result.object;

                Base64.Decoder decoder = Base64.getUrlDecoder();

                String chunks[] = token.split("\\.");
                String payload = new String(decoder.decode(chunks[1]));
                
                JSONObject json = new JSONObject(payload);
                String rol = json.getString("Rol");
                int id = json.getInt("idUsuario");     

                httpSession.setAttribute("token", token);

                if ("ROLE_ADMIN".equalsIgnoreCase(rol)) {
                    return "redirect:/UsuarioIndex";
                } else {
                    return "redirect:/UsuarioIndex/usuario-detalles/" + id;
                }

            } else {
                model.addAttribute("msgError", "Datos invalidos, intente nuevamente");
                return "redirect:/Login";
            }

        } catch (Exception ex) {
            String msgError = "Error al Comunicarse al servidor: " + ex.getLocalizedMessage();
            model.addAttribute("error", msgError);
            return "Login";
        }

    }
}
