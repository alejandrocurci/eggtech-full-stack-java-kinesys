package com.kinesysApp.kinesys.controladores;

import com.kinesysApp.kinesys.repositorios.RolRepositorio;
import com.kinesysApp.kinesys.roles.Rol;
import com.kinesysApp.kinesys.servicios.RolServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/roles")
public class RolControlador {

    @Autowired
    private RolServicio rolServicio;

    @GetMapping("/crear")
    public ModelAndView crearRol(){
        ModelAndView mav= new ModelAndView("rol-form");
        mav.addObject("rol",new Rol());
        mav.addObject("titulo","crear rol");
        mav.addObject("action","guardar");
        return mav;
    }

    @PostMapping("/guardar")
    public RedirectView guardarRol(@ModelAttribute("rol") Rol rol){

        rolServicio.crear(rol.getNombre());

        return new RedirectView("/");
}







}