/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.egg.eggNews.servicios;

import com.egg.eggNews.entidades.Imagen;
import com.egg.eggNews.excepciones.MyException;
import com.egg.eggNews.repositorios.ImagenRepositorio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author feder
 */
@Service
public class ImagenService {
    @Autowired
    ImagenRepositorio imagenRepo;
    
    public Imagen guardar(MultipartFile archivo) throws MyException{
        if (archivo!=null){
            try {
                Imagen imagen = new Imagen();
                imagen.setMime(archivo.getContentType());
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());
                
                return imagenRepo.save(imagen);
            } catch (Exception e) {
                System.err.println(e.getMessage());
                
            }
        }
        return null;
    }
    
 public Imagen actualizar(MultipartFile archivo, String id) throws MyException{
        if (archivo!=null){
            try {
                Imagen imagen = new Imagen();
                if(id != null){
                    Optional<Imagen> resp = imagenRepo.findById(id);
                    if(resp.isPresent()){
                        imagen = resp.get();
                    }
                }
                imagen.setMime(archivo.getContentType());
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());
                
                return imagenRepo.save(imagen);
            } catch (Exception e) {
                System.err.println(e.getMessage());
                
            }
        }
        return null;
    }
}
