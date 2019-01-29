package com.example.angelmolero.aplicacion3;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Vector;

public class MagatzemPuntuacionsPreferencies implements MagatzemPuntuacions {
    //Aquest valor servirà per donar nom al fitxer xml
    private static String PREFERENCIES = "puntuacions";
    private Context context;

    public MagatzemPuntuacionsPreferencies(Context context) {
        this.context = context;
    }

    @Override
    public void guardarPuntuacio(int punts, String nom, long data) {
        SharedPreferences preferencies = context.getSharedPreferences(PREFERENCIES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencies.edit();
        editor.putString("puntuacio",punts+" "+nom);
        editor.commit();
    }

    @Override
    public Vector<String> llistaPuntuacions(int quantitat) {
        Vector<String> result = new Vector<String>();
        SharedPreferences preferencies = context.getSharedPreferences(PREFERENCIES, Context.MODE_PRIVATE);
        String s = preferencies.getString("puntuacio","");
        if (s!="") {
            result.add(s);
        }
        return result;
    }
}
