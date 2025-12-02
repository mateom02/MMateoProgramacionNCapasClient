package com.digis01.MMateoProgramacionNCapas.controller;

import com.digis01.MMateoProgramacionNCapas.ML.Colonia;
import com.digis01.MMateoProgramacionNCapas.ML.Direccion;
import com.digis01.MMateoProgramacionNCapas.ML.Estado;
import com.digis01.MMateoProgramacionNCapas.ML.Municipio;
import com.digis01.MMateoProgramacionNCapas.ML.Pais;
import com.digis01.MMateoProgramacionNCapas.ML.Result;
import com.digis01.MMateoProgramacionNCapas.ML.Rol;
import com.digis01.MMateoProgramacionNCapas.ML.Usuario;
import jakarta.servlet.http.HttpSession;
import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("UsuarioIndex")
public class UsuarioIndex {

    private final static String urlBase = "http://localhost:8080/";

    @GetMapping
    public String UsuarioIndex(Model model, HttpSession httpSession) {

        RestTemplate restTemplate = new RestTemplate();
        Object tokenObj = httpSession.getAttribute("token");
//        if (tokenObj == null) {
//            redirectAttributes.addFlashAttribute("msgCargaError",
//                    "Token de sesión no encontrado. Por favor, cargue el archivo nuevamente.");
//            return "redirect:/UsuarioIndex/CargaMasiva";
//        }

        String token = tokenObj.toString();

        HttpHeaders tokenHeader = new HttpHeaders();
        tokenHeader.setBearerAuth(token);
        HttpEntity bodyHeader = new HttpEntity(tokenHeader);
        ResponseEntity<Result<List<Usuario>>> responseEntityUsuario = restTemplate.exchange(
                urlBase + "api/usuario",
                HttpMethod.GET,
                bodyHeader, new ParameterizedTypeReference<Result<List<Usuario>>>() {
        });

        ResponseEntity<Result<List<Rol>>> responseEntityRol = restTemplate.exchange(
                urlBase + "api/rol",
                HttpMethod.GET,
                bodyHeader, new ParameterizedTypeReference<Result<List<Rol>>>() {
        });

        if (responseEntityUsuario.getStatusCode().value() == 200 && responseEntityRol.getStatusCode().value() == 200) {
            Result resultUsuario = responseEntityUsuario.getBody();
            Result resultRol = responseEntityRol.getBody();
            model.addAttribute("usuarios", resultUsuario.object);
            model.addAttribute("roles", resultRol.object);
            Usuario usuario = new Usuario();
            model.addAttribute("usuario", usuario);
            return "UsuarioIndex";
        } else {
            return "Error";
        }
    }

    @GetMapping("usuario-detalles/{IdUsuario}")
    public String UsuarioDetalles(@PathVariable("IdUsuario") int idUsuario, Model model, HttpSession httpSession) {

        RestTemplate restTemplate = new RestTemplate();

        Object tokenObj = httpSession.getAttribute("token");
        String token = tokenObj.toString();

        HttpHeaders tokenHeader = new HttpHeaders();
        tokenHeader.setBearerAuth(token);
        HttpEntity bodyHeader = new HttpEntity(tokenHeader);
        
        ResponseEntity<Result<Usuario>> responseEntityUsuario = restTemplate.exchange(urlBase + "api/usuario/" + idUsuario,
                HttpMethod.GET, bodyHeader,
                new ParameterizedTypeReference<Result<Usuario>>() {
        });

        ResponseEntity<Result<List<Rol>>> responseEntityRol = restTemplate.exchange(
                urlBase + "api/rol",
                HttpMethod.GET,
                bodyHeader, new ParameterizedTypeReference<Result<List<Rol>>>() {
        });

        ResponseEntity<Result<List<Pais>>> responseEntityPais = restTemplate.exchange(
                urlBase + "api/pais", HttpMethod.GET, bodyHeader, new ParameterizedTypeReference<Result<List<Pais>>>() {
        });

        if (responseEntityUsuario.getStatusCode().value() == 200
                && responseEntityRol.getStatusCode().value() == 200
                && responseEntityPais.getStatusCode().value() == 200) {
            Result resultUsuario = responseEntityUsuario.getBody();
            Result resultRol = responseEntityRol.getBody();
            Result resultPais = responseEntityPais.getBody();

            model.addAttribute("usuario", resultUsuario.object);
            model.addAttribute("roles", resultRol.object);
            model.addAttribute("paises", resultPais.object);
            model.addAttribute("Direccion", new Direccion());
            return "UsuarioDetalles";
        } else {
            return "Error";
        }
    }

    @GetMapping("add")
    public String Add(Model model) {

        //Roles y paises
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Result<List<Pais>>> responseEntityPaises = restTemplate.exchange(
                urlBase + "api/pais", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<Result<List<Pais>>>() {
        });

        ResponseEntity<Result<List<Rol>>> responseEntityRol = restTemplate.exchange(
                urlBase + "api/rol",
                HttpMethod.GET,
                HttpEntity.EMPTY, new ParameterizedTypeReference<Result<List<Rol>>>() {
        });

        if (responseEntityPaises.getStatusCode().value() == 200 && responseEntityRol.getStatusCode().value() == 200) {
            Result resultPaises = responseEntityPaises.getBody();
            Result resultRoles = responseEntityRol.getBody();
            model.addAttribute("roles", resultRoles.object);
            model.addAttribute("paises", resultPaises.object);

            Usuario usuario = new Usuario();

            usuario.setNombre("Dioni");
            usuario.setApellidoPaterno("Mateo");
            usuario.setApellidoMaterno("Martinez");
            usuario.setUserName("dioni1");
            usuario.setCurp("012345678912345678");
            usuario.setSexo("F");
            usuario.setFechaNacimiento(new Date());
            usuario.setEmail("dioni1@gmail.com");
            usuario.setTelefono("2261164021");
            usuario.setCelular("2261164021");
            usuario.setPassword("1Ahfasaskfas");
            usuario.Direcciones = new ArrayList<>();
            Direccion direccion = new Direccion();
            direccion.setCalle("Av DIaz Miron");
            direccion.setNumeroExterior("158");
            direccion.setNumeroInterior("185");
            direccion.Colonia = new Colonia();
            direccion.Colonia.setIdColonia(1000);
            usuario.Direcciones.add(direccion);
            model.addAttribute("usuario", usuario);

            return "UsuarioFormulario";
        } else {
            return "Error";
        }

    }

    @PostMapping("add")
    public String Add(@ModelAttribute("usuario") Usuario usuario,
            BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam("imagenFile") MultipartFile imagenFile) throws IOException {

        RestTemplate restTemplate = new RestTemplate();

        //BindingResult es para regresar el fomulario con datos en caso de que algo salga mal
        if (bindingResult.hasErrors()) {
            ResponseEntity<Result<List<Pais>>> responseEntityPaises = restTemplate.exchange(
                    urlBase + "api/pais", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<Result<List<Pais>>>() {
            });

            ResponseEntity<Result<List<Rol>>> responseEntityRol = restTemplate.exchange(
                    urlBase + "api/rol",
                    HttpMethod.GET,
                    HttpEntity.EMPTY, new ParameterizedTypeReference<Result<List<Rol>>>() {
            });

            if (responseEntityPaises.getStatusCode().value() == 200 && responseEntityRol.getStatusCode().value() == 200) {
                Result resultPaises = responseEntityPaises.getBody();
                Result resultRoles = responseEntityRol.getBody();
                model.addAttribute("roles", resultRoles.object);
                model.addAttribute("paises", resultPaises.object);
            }

            model.addAttribute("usuario", usuario);

            if (usuario.Direcciones.get(0).Colonia.Municipio.Estado.Pais.getIdPais() > 0) {

                ResponseEntity<Result<List<Estado>>> responseEntityEstados = restTemplate.exchange(
                        urlBase + "api/estado/" + usuario.Direcciones.get(0).Colonia.Municipio.Estado.Pais.getIdPais(), HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<Result<List<Estado>>>() {
                });
                if (responseEntityEstados.getStatusCode().value() == 200) {
                    Result resultEstados = responseEntityEstados.getBody();
                    model.addAttribute("estados", resultEstados.object);

                    if (usuario.Direcciones.get(0).Colonia.Municipio.Estado.getIdEstado() > 0) {
                        ResponseEntity<Result<List<Municipio>>> responseEntityMunicipios = restTemplate.exchange(
                                urlBase + "api/estado/" + usuario.Direcciones.get(0).Colonia.Municipio.Estado.Pais.getIdPais(), HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<Result<List<Municipio>>>() {
                        });

                        if (responseEntityEstados.getStatusCode().value() == 200) {
                            Result resultMunicipios = responseEntityEstados.getBody();
                            model.addAttribute("municipios", resultMunicipios.object);
                        }
                    }
                }

            }

            return "UsuarioFormulario";
        }

        MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        if (imagenFile != null) {
            HttpHeaders fileHeaders = new HttpHeaders();

            String extension = imagenFile.getOriginalFilename().split("\\.")[1];
            if (extension.equals("jpg") || extension.equals("jpeg")) {
                fileHeaders.setContentType(MediaType.IMAGE_JPEG);
            } else if (extension.equals("png")) {
                fileHeaders.setContentType(MediaType.IMAGE_PNG);
            } else {

                fileHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            }

            ByteArrayResource fileAsResource = new ByteArrayResource(imagenFile.getBytes()) {
                @Override
                public String getFilename() {
                    return imagenFile.getOriginalFilename();
                }
            };

            HttpEntity<ByteArrayResource> filePart = new HttpEntity<>(fileAsResource, fileHeaders);
            multipartRequest.add("imagenFile", filePart);
        }

        HttpHeaders jsonHeaders = new HttpHeaders();
        jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Usuario> usuarioPart = new HttpEntity<>(usuario, jsonHeaders);
        multipartRequest.add("usuario", usuarioPart);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(multipartRequest, requestHeaders);

        ResponseEntity<String> response = restTemplate.exchange(urlBase + "api/usuario", HttpMethod.POST, requestEntity, String.class);

        int status = response.getStatusCode().value();

        redirectAttributes.addFlashAttribute("successMessage", "El usuario " + usuario.getUserName() + " se creo con exito.");
        return "redirect:/UsuarioIndex";
    }

    @PostMapping("update")
    public String Update(@ModelAttribute("usuario") Usuario usuario, RedirectAttributes redirectAttributes) {

        //Hacer peticion
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders jsonHeader = new HttpHeaders();
        jsonHeader.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Usuario> usuarioEntidad = new HttpEntity<>(usuario, jsonHeader);
        ResponseEntity<String> response = restTemplate.exchange(urlBase + "api/usuario/" + usuario.getIdUsuario(), HttpMethod.PUT, usuarioEntidad, String.class);

        int status = response.getStatusCode().value();

        redirectAttributes.addFlashAttribute("msgUsuarioEditado", "El usuario " + usuario.getUserName() + " se edito con exito");
        return "redirect:/UsuarioIndex/usuario-detalles/" + usuario.getIdUsuario();

    }

    @PostMapping("addDireccion")
    public String AddDireccion(@ModelAttribute("Direccion") Direccion direccion, @ModelAttribute("usuario") Usuario usuario, RedirectAttributes redirectAttributes) {

        //Agregar lo de BindingResult
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders jsonHeader = new HttpHeaders();
        jsonHeader.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Direccion> direccionData = new HttpEntity<>(direccion, jsonHeader);
        ResponseEntity<String> response = restTemplate.exchange(urlBase + "api/direccion/" + usuario.getIdUsuario(), HttpMethod.POST, direccionData, String.class);

        int status = response.getStatusCode().value();
        //RedirectCon mensaje
        redirectAttributes.addFlashAttribute("msgDireccionAgregada", "La direccion se agregó correctamente");
        return "redirect:/UsuarioIndex/usuario-detalles/" + usuario.getIdUsuario();
    }

    @PostMapping("updateDireccion/{idUsuario}")
    public String UpdateDireccion(@ModelAttribute("Direccion") Direccion direccion, @PathVariable("idUsuario") int idUsuario, RedirectAttributes redirectAttributes) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders jsonHeader = new HttpHeaders();
        jsonHeader.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Direccion> direccionData = new HttpEntity<>(direccion, jsonHeader);
        ResponseEntity<String> response = restTemplate.exchange(urlBase + "api/direccion/" + idUsuario, HttpMethod.PUT, direccionData, String.class);

        int status = response.getStatusCode().value();

        redirectAttributes.addFlashAttribute("msgDireccionEditada", "La direccion se editó correctamente");
        return "redirect:/UsuarioIndex/usuario-detalles/" + idUsuario;
    }

    @PostMapping
    public String GetAllDinamico(@ModelAttribute("usuario") Usuario usuario, Model model) {

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Usuario> usuarioData = new HttpEntity<>(usuario);
        ResponseEntity<Result<List<Rol>>> resultResponseEntityRoles = restTemplate.exchange(urlBase + "api/rol",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<List<Rol>>>() {
        });

        ResponseEntity<Result<List<Usuario>>> responseEntity = restTemplate.exchange(
                urlBase + "api/usuario/search",
                HttpMethod.POST,
                usuarioData,
                new ParameterizedTypeReference<Result<List<Usuario>>>() {
        });

        //Manejar el error
        if (responseEntity.getStatusCode().value() == 200 && resultResponseEntityRoles.getStatusCode().value() == 200) {
            Result resultUsuarios = responseEntity.getBody();
            Result resultRoles = resultResponseEntityRoles.getBody();
            model.addAttribute("usuarios", resultUsuarios.object);
            model.addAttribute("roles", resultRoles.object);
            model.addAttribute("usuario", usuario);
        }

        return "UsuarioIndex";

    }

    @PostMapping("updateImage/{idUsuario}")
    public String UpdateImage(@RequestParam("imagenFile") MultipartFile imagenFile, @PathVariable("idUsuario") int idUsuario, RedirectAttributes redirectAttributes) throws IOException {

        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

        MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        if (imagenFile != null) {
            HttpHeaders fileHeaders = new HttpHeaders();

            String extension = imagenFile.getOriginalFilename().split("\\.")[1];
            if (extension.equals("jpg") || extension.equals("jpeg")) {
                fileHeaders.setContentType(MediaType.IMAGE_JPEG);
            } else if (extension.equals("png")) {
                fileHeaders.setContentType(MediaType.IMAGE_PNG);
            } else {
                fileHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            }

            ByteArrayResource fileAsResource = new ByteArrayResource(imagenFile.getBytes()) {
                @Override
                public String getFilename() {
                    return imagenFile.getOriginalFilename();
                }
            };

            HttpEntity<ByteArrayResource> filePart = new HttpEntity<>(fileAsResource, fileHeaders);
            multipartRequest.add("imagenFile", filePart);
        }

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(multipartRequest, requestHeaders);

//        ResponseEntity<String> response = restTemplate.exchange(urlBase + "api/usuario/imagen" + idUsuario, HttpMethod.PATCH, requestEntity, String.class);
        Result response = restTemplate.patchForObject(urlBase + "api/usuario/imagen/" + idUsuario, requestEntity, Result.class);

        redirectAttributes.addFlashAttribute("msgImagenEditada", "La imagen se editó correctamente");
        return "redirect:/UsuarioIndex/usuario-detalles/" + idUsuario;
    }

    //Carga masiva
    @GetMapping("CargaMasiva")
    public String CargaMasiva() {
        return "CargaMasiva";
    }

    @PostMapping("CargaMasiva")
    public String CargaMasiva(@RequestParam("archivo") MultipartFile archivo, Model model, HttpSession session) {
        try {

            if (archivo.isEmpty()) {
                model.addAttribute("error", "El archivo está vacío");
                return "CargaMasiva";
            }

            Resource resource = archivo.getResource();
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", resource);

            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);

            // Manejo de excepciones en la llamada HTTP
            ResponseEntity<Result> response = restTemplate.exchange(
                    urlBase + "api/usuario/cargaMasivaValidar",
                    HttpMethod.POST,
                    httpEntity,
                    Result.class
            );

            if (response.getStatusCode().value() == 200) {
                Result result = response.getBody();
                session.setAttribute("token", result.object);
                model.addAttribute("correcto", true);
            } else if (response.getStatusCode().value() == 222) {
                Result result = response.getBody();
                model.addAttribute("listaErrores", result.objects);
                model.addAttribute("error", "Error en la validación del archivo");
            } else {
                model.addAttribute("error", "Error al abrir el archivo");
            }

        } catch (Exception ex) {
            model.addAttribute("error", "Error interno del sistema");
        }

        return "CargaMasiva";
    }

    @GetMapping("CargaMasiva/procesar")
    public String CargaUsuarios(HttpSession session, RedirectAttributes redirectAttributes) {

        try {

            Object tokenObj = session.getAttribute("token");
            if (tokenObj == null) {
                redirectAttributes.addFlashAttribute("msgCargaError",
                        "Token de sesión no encontrado. Por favor, cargue el archivo nuevamente.");
                return "redirect:/UsuarioIndex/CargaMasiva";
            }

            String token = tokenObj.toString();
            session.removeAttribute("token");

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders header = new HttpHeaders();
            header.setBearerAuth(token);
            HttpEntity<String> entity = new HttpEntity<>(header);

            // Manejar posibles excepciones en la llamada HTTP
            ResponseEntity<Result> responseEntity = restTemplate.exchange(
                    urlBase + "api/usuario/cargaMasivaProcesar",
                    HttpMethod.POST,
                    entity,
                    Result.class
            );

            if (responseEntity.getStatusCode().value() == 200) {
                redirectAttributes.addFlashAttribute("msgCargaCorrecta",
                        "Los datos se guardaron correctamente");
            } else {

                redirectAttributes.addFlashAttribute("msgCargaError", "Error al procesar la carga intentelo nuevamente");
            }

        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("msgCargaError",
                    "Error inesperado al procesar la carga. Por favor, intente nuevamente.");
        }

        return "redirect:/UsuarioIndex/CargaMasiva";
    }

}
