
package com.egg.eggNews.controladores;

import com.egg.eggNews.excepciones.MyException;
import com.egg.eggNews.servicios.NoticiaService;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
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
    
}