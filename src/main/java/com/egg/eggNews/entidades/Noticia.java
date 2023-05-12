
package com.egg.eggNews.entidades;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 *
 * @author feder
 */
@Entity
@Table(name = "noticias")
public class Noticia implements Serializable, Comparable<Noticia> {
    @Id
    @Column(name = "titulo")
    private String titulo;
    @Column(name = "cuerpo")
    private String cuerpo;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    public Noticia() {
    } 

    public Noticia(String titulo, String cuerpo, Date fecha) {
        this.titulo = titulo;
        this.cuerpo = cuerpo;
        this.fecha = fecha;
    }
     public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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

    @Override
    public int compareTo(Noticia t) {
      return t.fecha.compareTo(this.fecha);
    }
    
}
