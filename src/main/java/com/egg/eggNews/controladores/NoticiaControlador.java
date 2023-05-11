
package com.egg.eggNews.controladores;

import com.egg.eggNews.excepciones.MyException;
import com.egg.eggNews.servicios.NoticiaService;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author feder
 */

@Controller
@RequestMapping("/noticia")//localhost:8080/noticia
public class NoticiaControlador {
    
    @Autowired
    private NoticiaService notiServ;
    
    @GetMapping("/registrar") //localhost:8080/noticia/registrar
    public String cargar(){       
        return "noti_form.html";
    }
    @PostMapping("/registro") //localhost:8080/noticia/registro
    public String registro(@RequestParam String titulo,@RequestParam String cuerpo) {
        try {
            notiServ.crearNoticia(titulo, cuerpo);
        } catch (MyException ex) {
            Logger.getLogger(NoticiaControlador.class.getName()).log(Level.SEVERE, null, ex);
            return "noti_form.html";
        }
        return "index.html";
    }
    
}
