package com.egg.eggNews.controladores;

import com.egg.eggNews.entidades.Noticia;
import com.egg.eggNews.entidades.Usuario;
import com.egg.eggNews.enumeraciones.Rol;
import com.egg.eggNews.excepciones.MyException;
import com.egg.eggNews.servicios.NoticiaService;
import com.egg.eggNews.servicios.UsuarioService;

import java.util.ArrayList;

import java.util.List;

import javax.servlet.http.HttpSession;
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
    @Autowired
    private UsuarioService userServ;

    @GetMapping("/registrar") //localhost:8080/panel/noticia/registrar
    public String cargar(ModelMap modelo) {
        List<Usuario> usuarios = userServ.listarUsuarios();

        modelo.addAttribute("usuarios", usuarios);
        return "noti_form.html";
    }

    @PostMapping("/registro") //localhost:8080/panel/noticia/registro
    public String registro(@RequestParam String titulo, @RequestParam String cuerpo, ModelMap modelo, HttpSession session) {
        try {
            List<Usuario> usuarios = userServ.listarUsuarios();

            modelo.addAttribute("usuarios", usuarios);
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            notiServ.crearNoticia(titulo, cuerpo, logueado);
            modelo.put("exito", "Noticia cargada con exito!");
            List<Noticia> noticias = notiServ.listarNoticias();
            modelo.addAttribute("noticias", noticias);
            return "noti_form.html";
        } catch (MyException ex) {
            modelo.put("error", ex.getMessage());
            List<Noticia> noticias = notiServ.listarNoticias();
            modelo.addAttribute("noticias", noticias);
            List<Usuario> usuarios = userServ.listarUsuarios();
            for (Usuario usuario : usuarios) {
                if (usuario.getRol() == Rol.USER) {
                    usuarios.remove(usuario);
                }
            }
            modelo.addAttribute("usuarios", usuarios);
            return "noti_form.html";
        }
    }

    @GetMapping("/listar")
    public String listarNoticias(ModelMap modelo) {
        List<Noticia> noticias = notiServ.listarNoticias();
        modelo.addAttribute("noticias", noticias);
        return "listar_noticias.html";
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
            modelo.addAttribute("noticias", noticias);
            return "panelAdmin.html";
        }
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable("id") String id, ModelMap modelo) {

        try {
            modelo.put("noticia", notiServ.getOne(id));
            modelo.addAttribute("id", notiServ.getOne(id).getId());
            return "modificar.html";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "modificar";
        }

    }

    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable("id") String id, String titulo, String cuerpo, Usuario usuario, ModelMap modelo) {

        try {
            notiServ.modificarNoticia(id, titulo, cuerpo, (Usuario) usuario);
            modelo.put("exito", "!Noticia modificada con exito!");
            List<Noticia> noticias = notiServ.listarNoticias();
            modelo.addAttribute("noticias", noticias);
            return this.modificar(id, modelo);
        } catch (MyException ex) {
            modelo.put("error", ex.getMessage());
            List<Noticia> noticias = notiServ.listarNoticias();
            List<Usuario> usuarios = userServ.listarUsuarios();
            modelo.addAttribute("usuarios", usuarios);
            modelo.addAttribute("noticias", noticias);
            return this.modificar(id, modelo);
        }

    }

}
