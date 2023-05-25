package com.egg.eggNews.servicios;

import com.egg.eggNews.entidades.Noticia;
import com.egg.eggNews.excepciones.MyException;
import com.egg.eggNews.repositorios.NoticiaRepositorio;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author feder
 */
@Service
public class NoticiaService {

    @Autowired
    private NoticiaRepositorio notiRepo;

    @Transactional
    public void crearNoticia(String titulo, String cuerpo) throws MyException {
        validar(titulo, cuerpo);
        Noticia noti = new Noticia();
        noti.setTitulo(titulo);
        noti.setCuerpo(cuerpo);
        noti.setFecha(new Date());
        notiRepo.save(noti);

    }

    public List<Noticia> listarNoticias() {
        List<Noticia> noticias = new ArrayList();
        noticias = notiRepo.findAll();
        Collections.sort(noticias);
        return noticias;
    }


    public Noticia buscarPorTitulo(String titulo) {
        
         Optional<Noticia> respuesta = notiRepo.findById(titulo);
          Noticia noti = respuesta.get();
         return noti;
      
    }
    
    public Noticia getOne(String id)  {
        return notiRepo.getOne(id);
    }

    @Transactional
    public void modificarNoticia(String id,String titulo, String cuerpo) throws MyException {
        System.out.println("Entro a modificar noticia?");
        validar(titulo, cuerpo);
        Optional<Noticia> respuesta = notiRepo.findById(id);
        if (respuesta.isPresent()) {
            Noticia noti = respuesta.get();
            noti.setTitulo(titulo);
            noti.setCuerpo(cuerpo);
            notiRepo.save(noti);
        }
    }
    @Transactional
    public void eliminarNoticia(String id) throws MyException {
        
        Optional<Noticia> resp = notiRepo.findById(id);
        if (resp.isPresent()) {
            notiRepo.delete(resp.get());

        }

    }

    private void validar(String titulo, String cuerpo) throws MyException {

        if (titulo.isEmpty() || titulo == null) {
            throw new MyException("el tiutlo no puede ser nulo o estar vacio");
        }
        if (cuerpo.isEmpty() || cuerpo == null) {
            throw new MyException("el cuerpo no puede ser nulo o estar vacio");
        }

    }
}
