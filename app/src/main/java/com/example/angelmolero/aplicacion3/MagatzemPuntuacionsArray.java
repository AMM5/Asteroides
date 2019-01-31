package com.example.angelmolero.aplicacion3;

import java.util.Vector;

public class MagatzemPuntuacionsArray implements MagatzemPuntuacions {
    private Vector<String> puntuacions;

    public MagatzemPuntuacionsArray() {
        puntuacions = new Vector<String>();
    }

    @Override
    public void guardarPuntuacio(int punts, String nom, long data) {
        puntuacions.add(0,punts+" "+nom);
    }

    @Override
    public Vector<String> llistaPuntuacions(int quantitat) {
        return puntuacions;
    }
}
