package com.egg.eggNews.controladores;

import com.egg.eggNews.entidades.Noticia;
import com.egg.eggNews.servicios.NoticiaService;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author feder
 */
@Controller
@RequestMapping("/")
public class PortalControlador {
     @Autowired
    private NoticiaService notiServ;
    @GetMapping("/")
    public String index(ModelMap modelo){
        List<Noticia> noticias = notiServ.listarNoticias();
        Collections.sort(noticias);
        modelo.addAttribute("noticias", noticias);
        return "index.html";
    }
    
}
