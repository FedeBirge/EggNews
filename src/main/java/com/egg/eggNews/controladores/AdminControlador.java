package com.egg.eggNews.controladores;

import com.egg.eggNews.entidades.Noticia;
import com.egg.eggNews.entidades.Usuario;
import com.egg.eggNews.enumeraciones.Rol;
import com.egg.eggNews.excepciones.MyException;
import com.egg.eggNews.servicios.NoticiaService;
import com.egg.eggNews.servicios.PeriodistaService;
import com.egg.eggNews.servicios.UsuarioService;
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
@RequestMapping("/admin")
public class AdminControlador {

    @Autowired
    private NoticiaService notiServ;
    @Autowired
    private PeriodistaService periodistaServ;
    @Autowired
    private UsuarioService userServ;

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
            modelo.put("exito", "Noticia eliminada con exito!");
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

            return "listar_noticias.html";
        } catch (MyException ex) {

            modelo.put("error", ex.getMessage());
            return "redirect:/panel/admin";
        }

    }

    @GetMapping("/crearUsuario")
    public String crearUsusario(ModelMap modelo) {
        Rol[] roles = Rol.values();
        modelo.addAttribute("roles", roles);
        return "adminRegistro.html";
    }

    @PostMapping("/crearUser")
    public String crearUsuario(@RequestParam String nombre, @RequestParam String email,
            @RequestParam String password, @RequestParam String password2, @RequestParam String rol, @RequestParam String sueldo, ModelMap modelo) {
        try {

            if (rol.equals("JOURNALIST")) {
                periodistaServ.registrarPeriodista(nombre, email, password, password2, sueldo, rol);

            } else {
                periodistaServ.registrarUsuario(nombre, email, password, password2, rol);

            }
            modelo.put("exito", "!Usuario registrado con exito!");
            return "adminRegistro.html";

        } catch (MyException ex) {

            modelo.put("error", ex.getMessage());
            return crearUsusario(modelo);
        }

    }

    @GetMapping("/listarUsuarios")
    public String listarUsusario(ModelMap modelo) {
        try {
            List<Usuario> usuarios = userServ.listarUsuarios();
            modelo.addAttribute("usuarios", usuarios);
            return "listar_usuarios.html";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "redirect:/panel/admin";
        }

    }

    @GetMapping("/modificarUsuario/{id}")
    public String modificarUsusario(@PathVariable("id") String id, ModelMap modelo) {

        try {
            Rol[] roles = Rol.values();
            modelo.addAttribute("roles", roles);
            modelo.put("usuario", userServ.getOne(id));
            modelo.addAttribute("id", notiServ.getOne(id).getId());
       
            return "modificar_user.html";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "modificar_user.html";
        }

    }

    @PostMapping("/modificarUsuario/{id}")
    public String modificarUsusarios(@PathVariable("id") String id, @RequestParam String nombre, @RequestParam String email,
            @RequestParam String password, @RequestParam String password2, @RequestParam String rol, @RequestParam String sueldo, ModelMap modelo) {

        try {
            modelo.put("usuario", userServ.getOne(id));
            modelo.addAttribute("id", notiServ.getOne(id).getId());
            userServ.modificarUsuario(id, nombre, email, password, password2, rol);
         
             modelo.put("exito", "!Usuario modificado con exito!");
            return "modificar_user.html";
        } catch (MyException ex) {
            modelo.put("error", ex.getMessage());
            return "modificar_user.html";
        }

    }

}
