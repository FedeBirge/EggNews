package com.egg.eggNews.controladores;

import com.egg.eggNews.entidades.Noticia;
import com.egg.eggNews.excepciones.MyException;
import com.egg.eggNews.servicios.NoticiaService;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author feder
 */
@Controller
@RequestMapping("/noticia")//localhost:8080/panel/noticia
public class NoticiaControlador {

    @Autowired
    private NoticiaService notiServ;

    @GetMapping("/registrar") //localhost:8080/panel/noticia/registrar
    public String cargar() {
        return "noti_form.html";
    }

    @PostMapping("/registro") //localhost:8080/panel/noticia/registro
    public String registro(@RequestParam String titulo, @RequestParam String cuerpo, ModelMap modelo) {
        try {
            notiServ.crearNoticia(titulo, cuerpo);
            modelo.put("exito", "Noticia cargada con exito!");
            List<Noticia> noticias = notiServ.listarNoticias();
            Collections.sort(noticias);
            modelo.addAttribute("noticias", noticias);
            return "noti_form.html";
        } catch (MyException ex) {
            modelo.put("error", ex.getMessage());
            List<Noticia> noticias = notiServ.listarNoticias();
            Collections.sort(noticias);
            modelo.addAttribute("noticias", noticias);
            return "noti_form.html";
        }

    }

    @GetMapping("/mostrar/{id}")
    public String mostrar(@PathVariable("id") String id, ModelMap modelo) throws MyException {

        // getOne con titulo
        try {
            modelo.addAttribute("titulo", notiServ.getOne(id).getTitulo());
            modelo.addAttribute("cuerpo", notiServ.getOne(id).getCuerpo());
            return "mostrar.html";
        } catch (Exception ex) {
            List<Noticia> noticias = notiServ.listarNoticias();
            Collections.sort(noticias);
            modelo.addAttribute("noticias", noticias);
            return "panelAdmin.html";
        }
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable("id") String id, ModelMap modelo) {

        try {
            modelo.put("noticia", notiServ.getOne(id));

            return "modificar.html";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "redirect:/panel/admin";
        }

    }

    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable("id") String id, String titulo, String cuerpo, ModelMap modelo) {

        try {

            notiServ.modificarNoticia(id, titulo, cuerpo);
            modelo.put("exito", "!Noticia modificada con exito!");
            List<Noticia> noticias = notiServ.listarNoticias();
            Collections.sort(noticias);
            modelo.addAttribute("noticias", noticias);
            return "panelAdmin.html";
        } catch (MyException ex) {
            modelo.put("error", ex.getMessage());
            List<Noticia> noticias = notiServ.listarNoticias();
            Collections.sort(noticias);
            modelo.addAttribute("noticias", noticias);
            return "panelAdmin.html";
        }

    }

}
