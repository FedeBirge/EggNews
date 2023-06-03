/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.egg.eggNews.servicios;

import com.egg.eggNews.entidades.Periodista;
import com.egg.eggNews.entidades.Usuario;
import com.egg.eggNews.enumeraciones.Rol;
import com.egg.eggNews.excepciones.MyException;
import com.egg.eggNews.repositorios.UsuarioRepositorio;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author feder
 */
@Service
public class PeriodistaService extends UsuarioService {

    @Autowired
    private UsuarioRepositorio usuarioRepo;
    
    @Transactional
    public void registrarPeriodista(String nombre, String email, String password, String password2,String rol, String sueldo) throws MyException {
        if (sueldo == null || sueldo.isEmpty()){
            throw new MyException("Debe ingresar un sueldo al periodista");
        }
        
        validar(nombre, email, password, password2,rol);
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

}
