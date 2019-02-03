package com.example.angelmolero.aplicacion3;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MagatzemPuntuacionsGson implements MagatzemPuntuacions {
    private String string; //Emmagatzema puntuacions en format JSON
    private Gson gson = new Gson();
    private Type type = new TypeToken<List<Puntuacio>>(){}.getType();

    public MagatzemPuntuacionsGson() {
        //Inicialitza uns valors
        guardarPuntuacio(45000, "Mi nombre", System.currentTimeMillis());
        guardarPuntuacio(31000, "Otro nombre", System.currentTimeMillis());
    }
    @Override
    public void guardarPuntuacio(int punts, String nom, long data) {
        //string = llegirString();
        ArrayList<Puntuacio> puntuacions;
        if (string == null) {
            puntuacions = new ArrayList<>();
        } else {
            puntuacions = gson.fromJson(string, type);
        }
        puntuacions.add(new Puntuacio(punts, nom, data));
        string = gson.toJson(puntuacions, type);
        //guardarString(string);
    }

    @Override
    public Vector<String> llistaPuntuacions(int quantitat) {
        ArrayList<Puntuacio> puntuacions;
        if (string == null) {
            puntuacions = new ArrayList<>();
        } else {
            puntuacions = gson.fromJson(string, type);
        }
        Vector<String> salida = new Vector<>();
        for (Puntuacio puntuacio:puntuacions) {
            salida.add(puntuacio.getPunts()+" "+puntuacio.getNom());
        }
        return salida;
    }
}
