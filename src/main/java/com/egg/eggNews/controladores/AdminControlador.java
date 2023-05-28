package com.egg.eggNews.controladores;

import com.egg.eggNews.entidades.Noticia;
import com.egg.eggNews.excepciones.MyException;
import com.egg.eggNews.servicios.NoticiaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author feder
 */
@Controller
@RequestMapping("/admin")
public class AdminControlador {

    @Autowired
    private NoticiaService notiServ;

    @GetMapping("/dashboard")
    public String panelAdministrativo(ModelMap modelo) {
        List<Noticia> noticias = notiServ.listarNoticias();

        modelo.addAttribute("noticias", noticias);

        return "panelAdmin.html";
    }
@GetMapping("/vista")
    public String vistaNoticias(ModelMap modelo) {
        List<Noticia> noticias = notiServ.listarNoticias();

        modelo.addAttribute("noticias", noticias);

        return "news.html";
    }
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") String id, ModelMap modelo) {

        try {
            modelo.put("exito", "Noticia eliminada(get) con exito!");
            return "redirect:/panel/admin";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "redirect:/panel/admin";
        }

    }

    @PostMapping("/eliminar/{id}")
    public String eliminar1(@PathVariable("id") String id, ModelMap modelo) {

        try {
            notiServ.eliminarNoticia(notiServ.getOne(id).getId());
            modelo.put("exito", "!Noticia eliminada con exito!");
            List<Noticia> noticias = notiServ.listarNoticias();

            modelo.addAttribute("noticias", noticias);

            return "panelAdmin.html";
        } catch (MyException ex) {

            modelo.put("error", ex.getMessage());
            return "redirect:/panel/admin";
        }

    }
}
