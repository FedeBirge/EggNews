
package com.egg.eggNews.controladores;

import com.egg.eggNews.entidades.Noticia;
import com.egg.eggNews.excepciones.MyException;
import com.egg.eggNews.servicios.NoticiaService;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    public String cargar(){       
        return "noti_form.html";
    }
    
   
    @PostMapping("/registro") //localhost:8080/panel/noticia/registro
    public String registro(@RequestParam String titulo,@RequestParam String cuerpo, ModelMap modelo) {
        try {
            notiServ.crearNoticia(titulo, cuerpo);
            modelo.put("exito","Noticia cargada con exito!");
            return "noti_form.html";
        } catch (MyException ex) {
            modelo.put("error", ex.getMessage());
            return "noti_form.html";
        }
        
    }
     @GetMapping("/mostrar/{titulo}")
    public String mostrar(@PathVariable("titulo") String titulo, ModelMap modelo) throws MyException {
           
        // getOne con titulo
        try {            
            modelo.addAttribute("titulo", notiServ.getOne(titulo).getTitulo());
            modelo.addAttribute("cuerpo", notiServ.getOne(titulo).getCuerpo());
            return "mostrar.html";
        } catch (Exception ex) {
             List<Noticia> noticias = notiServ.listarNoticias();
             Collections.sort(noticias);
             modelo.addAttribute("noticias", noticias);
            return "panelAdmin.html";
        }
    }
    @GetMapping("/modificar/{titulo}")
    public String modificar(@PathVariable("titulo") String titulo, ModelMap modelo) {

        try {
            modelo.put("exito", "Noticia eliminada(get) con exito!");
            return "redirect:/panel/admin";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "redirect:/panel/admin";
        }

    }

    @PostMapping("/modificar/{titulo}")
    public String modificar(@PathVariable("titulo") String titulo,@RequestParam String cuerpo, ModelMap modelo) {
        
        try {
            notiServ.eliminarNoticia(notiServ.getOne(titulo).getTitulo());
            modelo.put("exito", "!Noticia eliminada con exito!");
            List<Noticia> noticias = notiServ.listarNoticias();
            Collections.sort(noticias);
            modelo.addAttribute("noticias", noticias);
            
            return "panelAdmin.html";
        } catch (MyException ex) {
            modelo.put("error", ex.getMessage());
            return "redirect:/panel/admin";
        }

    }
    
}
