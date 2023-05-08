
package com.egg.eggNews.entidades;

import javax.persistence.Entity;
import javax.persistence.Id;


/**
 *
 * @author feder
 */
@Entity
public class Noticia {
    @Id
    private String titulo;
    private String cuerpo;

    public Noticia() {
    }

    public Noticia(String titulo, String cuerpo) {
        this.titulo = titulo;
        this.cuerpo = cuerpo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }
    
}
