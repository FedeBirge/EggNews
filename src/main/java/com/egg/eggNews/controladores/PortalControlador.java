package com.egg.eggNews.controladores;

import com.egg.eggNews.entidades.Noticia;
import com.egg.eggNews.entidades.Usuario;
import com.egg.eggNews.enumeraciones.Rol;
import com.egg.eggNews.excepciones.MyException;
import com.egg.eggNews.servicios.NoticiaService;
import com.egg.eggNews.servicios.PeriodistaService;
import com.egg.eggNews.servicios.UsuarioService;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpSession;
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
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    private NoticiaService notiServ;

    @Autowired
    private UsuarioService userServ;
    @Autowired
    private PeriodistaService periodistaServ;

    @GetMapping("/")
    public String index(ModelMap modelo) {
        List<Noticia> noticias = notiServ.listarNoticias();
        Collections.sort(noticias);
        modelo.addAttribute("noticias", noticias);
        return "index.html";
    }

    @GetMapping("/registrar")
    public String registrar() {
        return "registro.html";

    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {
        if (error != null) {
            modelo.put("error", "Usuario o contraseñas invalidos");
        }
        return "login.html";

    }
//@GetMapping("/logout")
//    public String logout(@RequestParam(required=false ) String error, ModelMap modelo) {
//        if(error!=null){
//            modelo.put("error","Usuario o contraseñas invalidos");
//        }
//        return "index.html";
//
//    }

    @PostMapping("/registro")
    public String registro(MultipartFile archivo, @RequestParam String nombre, @RequestParam String email,
            @RequestParam String password, @RequestParam String password2, ModelMap modelo) {
        try {

            userServ.registrarUsuario(archivo, nombre, email, password, password2, "USER");
            modelo.put("exito", "!Usuario registrado correctamente!");
            List<Noticia> noticias = notiServ.listarNoticias();
            Collections.sort(noticias);
            modelo.addAttribute("noticias", noticias);
            return "news.html";
        } catch (MyException ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);
            return "registro.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_JOURNALIST')")
    @GetMapping("/news")
    public String news(ModelMap modelo, HttpSession session) {
        try {
            List<Noticia> noticias = notiServ.listarNoticias();
            Collections.sort(noticias);
            modelo.addAttribute("noticias", noticias);

            Usuario logueado = (Usuario) session.getAttribute("usuariosession");

            if (logueado.getRol().toString().equals("ADMIN")) {
                return "redirect:/admin/dashboard";
            }
            return "news.html";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            List<Noticia> noticias = notiServ.listarNoticias();
            modelo.addAttribute("noticias", noticias);
            return "login.html";
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_JOURNALIST')")
    @GetMapping("/perfil/{id}")
    public String perfil(@PathVariable("id") String id, ModelMap modelo, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.put("usuario", usuario);
        modelo.put("usuario", userServ.getOne(id));
        return "modificar_user.html";

    }

    @PostMapping("/perfil/{id}")
    public String modificarUsusarios(@PathVariable("id") String id, MultipartFile archivo, @RequestParam String nombre, @RequestParam String email,
            @RequestParam String password, @RequestParam String password2, @RequestParam String rol,  String sueldo, ModelMap modelo) throws IOException {

        try {
            Rol[] roles = Rol.values();
            modelo.put("usuario", userServ.getOne(id));
            modelo.addAttribute("id", notiServ.getOne(id).getId());
          
            
            if (rol.equals("JOURNALIST")) {
                periodistaServ. modificarPeriodista(archivo,id, nombre, email, password, password2, rol,sueldo);

            } else {
                userServ.modificarUsuario(archivo, id, nombre, email, password, password2, rol);

            }

            modelo.put("exito", "!Usuario modificado con exito!");
            List<Usuario> usuarios = userServ.listarUsuarios();
            modelo.put("usuarios", usuarios);
           return "modificar_user.html";
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
