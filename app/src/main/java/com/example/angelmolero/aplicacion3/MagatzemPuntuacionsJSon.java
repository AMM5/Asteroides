package com.example.angelmolero.aplicacion3;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MagatzemPuntuacionsJSon implements MagatzemPuntuacions {
    private String string; //Almacena puntuaciones en formato JSON

    public MagatzemPuntuacionsJSon() {
        guardarPuntuacio(45000,"Mi nombre", System.currentTimeMillis());
        guardarPuntuacio(31000,"Otro nombre", System.currentTimeMillis());
    }


    @Override
    public void guardarPuntuacio(int punts, String nom, long data) {
        //string = llegirString();
        List<Puntuacio> puntuacions = llegirJSon(string);
        puntuacions.add(new Puntuacio(punts, nom, data));
        string = guardarJSon(puntuacions);
        //guardarString(string);
    }

    @Override
    public Vector<String> llistaPuntuacions(int quantitat) {
        //string = llegirString();
        List<Puntuacio> puntuacions = llegirJSon(string);
        Vector<String> salida = new Vector<>();
        for (Puntuacio puntuacion: puntuacions) {
            salida.add(puntuacion.getPunts()+" "+puntuacion.getNom());
        }
        return salida;
    }

    private String guardarJSon(List<Puntuacio> puntuacions) {
        String string = "";
        try {
            JSONArray jsonArray = new JSONArray();
            for (Puntuacio puntuacio : puntuacions) {
                JSONObject objeto = new JSONObject();
                objeto.put("punts", puntuacio.getPunts());
                objeto.put("nom", puntuacio.getNom());
                objeto.put("data", puntuacio.getData());
                jsonArray.put(objeto);
            }
            string = jsonArray.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return string;
    }

    private List<Puntuacio> llegirJSon(String string) {
        List<Puntuacio> puntuacions = new ArrayList<>();
        try {
            JSONArray json_array = new JSONArray(string);
            for (int i = 0; i < json_array.length(); i++) {
                JSONObject objeto = json_array.getJSONObject(i);
                puntuacions.add(new Puntuacio(objeto.getInt("puntos"),
                        objeto.getString("nombre"), objeto.getLong("fecha")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return puntuacions;
    }
}
