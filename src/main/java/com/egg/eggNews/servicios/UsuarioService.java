/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.egg.eggNews.servicios;

import com.egg.eggNews.entidades.Imagen;
import com.egg.eggNews.entidades.Usuario;
import com.egg.eggNews.enumeraciones.Rol;
import com.egg.eggNews.excepciones.MyException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.egg.eggNews.repositorios.UsuarioRepositorio;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author feder
 */
@Primary
@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepo;
    @Autowired
    ImagenService imagenServ;

    @Transactional
    public void registrarUsuario(MultipartFile archivo, String nombre, String email, String password, String password2, String rol) throws MyException {
        validar(nombre, email, password, password2, rol);
        if(usuarioRepo.buscarPorEmail(email)!=null){
             throw new MyException("EL mail ingresado ya existe! Ingreso otro!");
        }
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setFecha(new Date());
        usuario.setActivo(true);
        if (rol.equals("ADMIN")) {
            usuario.setRol(Rol.ADMIN);
        } else {
            usuario.setRol(Rol.USER);
        }
        Imagen imagen = imagenServ.guardar(archivo);

        usuario.setImagen(imagen);
        usuarioRepo.save(usuario);

    }

    @Transactional
    public void modificarUsuario(MultipartFile archivo, String id, String nombre, String email, String password, String password2, String rol) throws MyException, IOException {
        validar(nombre, email, password, password2, rol);
        Optional<Usuario> respuesta = usuarioRepo.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
              if(!usuarioRepo.buscarPorEmail(email).getId().equals(usuario.getId())){
                  throw new MyException("EL mail ingresado ya existe en otro ususario! Ingreso otro!");
        }
            usuario.setNombre(nombre);
            usuario.setEmail(email);
            usuario.setPassword(new BCryptPasswordEncoder().encode(password));

            usuario.setActivo(true);
            if (rol.equals("ADMIN")) {
                usuario.setRol(Rol.ADMIN);
            }
            if (rol.equals("USER")) {
                usuario.setRol(Rol.USER);
            }
            if (rol.equals("JOURNALIST")) {
                usuario.setRol(Rol.JOURNALIST);
            }
            String idImg = null;
            if (usuario.getImagen() != null) {
                idImg = usuario.getImagen().getId();
            }
            if (archivo.getBytes().length != 0 ) {
                Imagen imagen = imagenServ.actualizar(archivo, idImg);
                usuario.setImagen(imagen);
            }

            usuarioRepo.save(usuario);
        }

    }

    public Usuario getOne(String id) {
        return usuarioRepo.getOne(id);
    }

    protected void validar(String nombre, String email, String password, String password2, String rol) throws MyException {
        if (nombre == null || nombre.isEmpty()) {
            throw new MyException("Debe ingresar un nombre");
        }
        if (email == null || email.isEmpty()) {
            throw new MyException("Debe ingresar un email");
        }
        if (password == null || password.isEmpty() || password.length() < 6) {
            throw new MyException("Debe ingresar una contraseña válida");
        }
        if (password2 == null || password2.isEmpty() || password2.length() < 6) {
            throw new MyException("Debe ingresar una contraseña");
        }
        if (!password.equals(password2)) {
            throw new MyException("Las contraseñas no coinciden");
        }

        if (rol == null || rol.isEmpty() || rol.equals("Seleccione Rol")) {
            throw new MyException("Debe ingresar un rol");
        }

    }

    public List<Usuario> listarUsuarios() {

        List<Usuario> aux = new ArrayList();
        List<Usuario> usuarios = new ArrayList();
        aux = usuarioRepo.findAll();
        for (Usuario usuario : aux) {
            if (usuario.getActivo().equals(Boolean.TRUE)) {
                usuarios.add(usuario);
            }

        }
        return usuarios;
    }

    @Transactional
    public void eliminarUsuario(String id) {
        Optional<Usuario> resp = usuarioRepo.findById(id);
        if (resp.isPresent()) {
            Usuario user = (Usuario) (resp.get());
            user.setActivo(Boolean.FALSE);
            usuarioRepo.save(user);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepo.buscarPorEmail(email);
        //if activo==FALSE
        if (usuario != null && usuario.getActivo().equals(Boolean.TRUE)) {

            List<GrantedAuthority> permisos = new ArrayList();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());
            permisos.add(p);
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = attr.getRequest().getSession(true);

            session.setAttribute("usuariosession", usuario);
            return new User(usuario.getEmail(), usuario.getPassword(), permisos);

        } else {
            return null;
        }

    }

}
