/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.egg.eggNews.entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author feder
 */
@Entity
@Getter @Setter
@NoArgsConstructor  
@AllArgsConstructor
public class Periodista extends Usuario implements Serializable{
    @OneToMany
    private List<Noticia> misNoticias;
    private Integer sueldoMensual;
    
}
