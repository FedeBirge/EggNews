/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.egg.eggNews.servicios;

import com.egg.eggNews.entidades.Usuario;
import com.egg.eggNews.enumeraciones.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.egg.eggNews.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 *
 * @author feder
 */
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepo;

    @Transactional
    public void registrarUsuario(String nombre, String email, String password, String password2) throws Exception {
        validar(nombre, email, password, password2);
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setPassword(password);
        usuario.setFecha(new Date());
        usuario.setActivo(true);
        usuario.setRol(Rol.USER);
        usuarioRepo.save(usuario);

    }

    private void validar(String nombre, String email, String password, String password2) throws Exception {
        if (nombre == null || nombre.isEmpty()) {
            throw new Exception("Debe ingresar un nombre");
        }
        if (email == null || email.isEmpty()) {
            throw new Exception("Debe ingresar un email");
        }
        if (password == null || password.isEmpty() || password.length() < 6) {
            throw new Exception("Debe ingresar una contraseña");
        }
        if (password2 == null || password2.isEmpty() || password2.length() < 6) {
            throw new Exception("Debe ingresar una contraseña");
        }
        if (!password.equals(password2)) {
            throw new Exception("Las contraseñas no coinciden");
        }
        if (usuarioRepo.buscarPorEmail(email) != null) {
            throw new Exception("Ya existe un usuario con ese email");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepo.buscarPorEmail(email);
        if (usuario != null) {
           List<GrantedAuthority> permisos = new ArrayList();            
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());
            permisos.add(p);
            return new User(usuario.getEmail(), usuario.getPassword(), permisos);

        }
        else
            return null;

    }

}
