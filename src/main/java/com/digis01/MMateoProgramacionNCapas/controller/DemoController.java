
package com.digis01.MMateoProgramacionNCapas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("demo")
public class DemoController {

    @GetMapping("HolaMundo")
    public String Saludo(){
        return "HolaMundo";
    }
    
    @GetMapping("Saludo/{nombre}")
    public String Saludo(@PathVariable("nombre") String nombre, Model model){
        model.addAttribute("nombre", nombre);
        return "Saludo";
    }
    
    @GetMapping("Multiplicacion")
    public String Multiplicar(@RequestParam("numeroA") int numeroA, @RequestParam("numeroB") int numeroB, Model model){
        int resultado = numeroA*numeroB;
        model.addAttribute("resultado", resultado);
        model.addAttribute("numeroA", numeroA);
        model.addAttribute("numeroB", numeroB);
        return "Multiplicacion";
    }
    
}
