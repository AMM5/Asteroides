package com.example.angelmolero.aplicacion3;

import java.util.Vector;

public class MagatzemPuntuacionsArray implements MagatzemPuntuacions {
    private Vector<String> puntuacions;

    public MagatzemPuntuacionsArray() {
        puntuacions = new Vector<String>();
        puntuacions.add("123000 Pepito Dominguez");
        puntuacions.add("111000 Pedro Martinez");
        puntuacions.add("011000 Paco Pérez");
        puntuacions.add("521456 Angel Molero");
        puntuacions.add("854536 Marc Lopez");
        puntuacions.add("452365 Pepe pepito");
        puntuacions.add("025486 Biel Lopez");
        puntuacions.add("478569 Miquel Riera");
        puntuacions.add("789542 Jonatan suarex");
        puntuacions.add("123689 Antonio Fernandez");
        puntuacions.add("878965 Hola Papi");
        puntuacions.add("789641 Dino pato");
        puntuacions.add("478445 Sergio Ramos");
        puntuacions.add("879986 Vinicius Junior");
        puntuacions.add("087932 Nacho Fernandez");
        puntuacions.add("556687 Marcos Asensio");
        puntuacions.add("789456 Luka Modric");
        puntuacions.add("456789 Marcelo vieiria");
        puntuacions.add("877777 Toni Kroos");
        puntuacions.add("700002 Lucas Vazquez");
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
