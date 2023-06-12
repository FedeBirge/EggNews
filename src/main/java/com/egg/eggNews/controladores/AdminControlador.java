package com.egg.eggNews.controladores;

import com.egg.eggNews.entidades.Noticia;
import com.egg.eggNews.entidades.Usuario;
import com.egg.eggNews.enumeraciones.Rol;
import com.egg.eggNews.excepciones.MyException;
import com.egg.eggNews.servicios.NoticiaService;
import com.egg.eggNews.servicios.PeriodistaService;
import com.egg.eggNews.servicios.UsuarioService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author feder
 */
@Controller
 @PreAuthorize("hasRole('ROLE_ADMIN')")
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

    @GetMapping("/eliminar_noti/{id}")
    public String eliminar(@PathVariable("id") String id, ModelMap modelo) {

        try {
            modelo.put("exito", "Noticia eliminada con exito!");
            return "redirect:/panel/admin";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "redirect:/panel/admin";
        }

    }

    @PostMapping("/eliminar_noti/{id}")
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

    @GetMapping("/eliminar/{id}")
    public String eliminarU(@PathVariable("id") String id, ModelMap modelo) {

        try {
            modelo.put("exito", "Usuario eliminado con exito!");

            return "redirect:/admin/listarUsuarios";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "redirect:/admin/listarUsusarios";
        }

    }

    @PostMapping("/eliminar/{id}")
    public String eliminarUser(@PathVariable("id") String id, ModelMap modelo) {

        try {
            userServ.eliminarUsuario(id);
            List<Usuario> usuarios = userServ.listarUsuarios();
            modelo.addAttribute("usuarios", usuarios);
            modelo.put("exito", "Usuario eliminado con exito!");

            return "listar_usuarios.html";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "redirect:/admin/listarUsusarios";
        }

    }

    @GetMapping("/crearUsuario")
    public String crearUsusario(ModelMap modelo) {
        Rol[] roles = Rol.values();
        modelo.addAttribute("roles", roles);
        return "registro.html";
    }

    @PostMapping("/crearUser")
    public String crearUsuario( MultipartFile archivo, @RequestParam String nombre, @RequestParam String email,
            @RequestParam String password, @RequestParam String password2, @RequestParam String rol, @RequestParam String sueldo, ModelMap modelo) {
        try {
            Rol[] roles = Rol.values();
            modelo.addAttribute("roles", roles);
            if (rol.equals("JOURNALIST")) {
                periodistaServ.registrarPeriodista(archivo, nombre, email, password, password2, sueldo, rol);

            } else {
                periodistaServ.registrarUsuario(archivo,nombre, email, password, password2, rol);

            }
            modelo.put("exito", "!Usuario registrado con exito!");
            return "registro.html";

        } catch (MyException ex) {
            Rol[] roles = Rol.values();
            modelo.addAttribute("roles", roles);
            modelo.put("error", ex.getMessage());
            return "registro.html";
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
            Rol[] roles = Rol.values();
            modelo.addAttribute("roles", roles);
            modelo.put("error", ex.getMessage());
            return "modificar_user.html";
        }

    }

    @PostMapping("/modificarUsuario/{id}")
    public String modificarUsusarios(@PathVariable("id") String id, @RequestParam MultipartFile archivo, @RequestParam String nombre, @RequestParam String email,
            @RequestParam String password, @RequestParam String password2, @RequestParam String rol, @RequestParam String sueldo, ModelMap modelo) throws IOException {

        try {
            Rol[] roles = Rol.values();
            modelo.addAttribute("roles", roles);
            modelo.put("usuario", userServ.getOne(id));
            modelo.addAttribute("id", notiServ.getOne(id).getId());
          
            userServ.modificarUsuario(archivo, id, nombre, email, password, password2, rol);

            modelo.put("exito", "!Usuario modificado con exito!");
            List<Usuario> usuarios = userServ.listarUsuarios();
            modelo.put("usuarios", usuarios);
            return "listar_usuarios.html";
        } catch (MyException ex) {
            Rol[] roles = Rol.values();
            modelo.addAttribute("roles", roles);
             modelo.put("usuario", userServ.getOne(id));
            modelo.addAttribute("id", notiServ.getOne(id).getId());
          
            modelo.put("error", ex.getMessage());
            return "modificar_user.html";
        }

    }

}
