/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.egg.eggNews.servicios;

import com.egg.eggNews.entidades.Imagen;
import com.egg.eggNews.entidades.Periodista;
import com.egg.eggNews.entidades.Usuario;

import com.egg.eggNews.enumeraciones.Rol;
import com.egg.eggNews.excepciones.MyException;
import com.egg.eggNews.repositorios.UsuarioRepositorio;
import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author feder
 */
@Service
public class PeriodistaService extends UsuarioService {
    
    @Autowired
    private UsuarioRepositorio usuarioRepo;
    
    @Transactional
    public void registrarPeriodista(MultipartFile archivo, String nombre, String email, String password, String password2, String rol, String sueldo) throws MyException {
        if (sueldo == null || sueldo.isEmpty()) {
            throw new MyException("Debe ingresar un sueldo al periodista");
        }
        
        validar(nombre, email, password, password2, rol);
        Periodista perdiodista = new Periodista();
        perdiodista.setNombre(nombre);
        perdiodista.setEmail(email);
        perdiodista.setPassword(new BCryptPasswordEncoder().encode(password));
        perdiodista.setFecha(new Date());
        perdiodista.setActivo(true);
        perdiodista.setRol(Rol.JOURNALIST);
        perdiodista.setSueldoMensual(Integer.valueOf(sueldo));
        usuarioRepo.save(perdiodista);
        
    }

    @Transactional
    public void modificarPeriodista(MultipartFile archivo, String id, String nombre, String email, String password, String password2, String rol, String sueldo) throws MyException {
        
               
        validar(nombre, email, password, password2, rol);
        Optional<Usuario> respuesta = usuarioRepo.findById(id);
        if (respuesta.isPresent()) {
            Periodista usuario = (Periodista) respuesta.get();
            usuario.setNombre(nombre);
            usuario.setEmail(email);
            usuario.setPassword(new BCryptPasswordEncoder().encode(password));
            usuario.setSueldoMensual(Integer.valueOf(sueldo));
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
            Imagen imagen = imagenServ.actualizar(archivo, id);
            usuario.setImagen(imagen);
            
            usuarioRepo.save(usuario);
        }
    }
    


}
