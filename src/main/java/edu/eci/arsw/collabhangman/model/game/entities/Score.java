/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.collabhangman.model.game.entities;

import java.util.Date;

/**
 *
 * @author 2103021
 */
public class Score {
    private String fechaPuntaje;
    private int puntaje;

    public Score(String fechaPuntaje, int puntaje) {
        this.fechaPuntaje = fechaPuntaje;
        this.puntaje = puntaje;
    }

    public String getFechaPuntaje() {
        return fechaPuntaje;
    }

    public void setFechaPuntaje(String fechaPuntaje) {
        this.fechaPuntaje = fechaPuntaje;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }
    
    
}
