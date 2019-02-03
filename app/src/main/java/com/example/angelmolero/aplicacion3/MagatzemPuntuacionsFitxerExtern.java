package com.example.angelmolero.aplicacion3;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Vector;

public class MagatzemPuntuacionsFitxerExtern implements MagatzemPuntuacions {
    private static String FITXER= Environment.getExternalStorageDirectory()+"/puntuacions.txt";
    private Context context;

    public MagatzemPuntuacionsFitxerExtern(Context context) {
        this.context = context;
    }

    @Override
    public void guardarPuntuacio(int punts, String nom, long data) {
        String estatSD=Environment.getExternalStorageState();
        if(estatSD.equals(Environment.MEDIA_MOUNTED)) {
            try {
                FileOutputStream f = context.openFileOutput(FITXER, Context.MODE_APPEND);
                String text = punts + " " + nom + "\n";
                f.write(text.getBytes());
                f.close();
            } catch (Exception e) {
                Log.e("Asteroides", e.getMessage(), e);
            }
        } else {
            Toast.makeText(context, "No se puede escribir el almacenamiento externo", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Vector<String> llistaPuntuacions(int quantitat) {
        Vector<String> result = new Vector<String>();
        String estatSD=Environment.getExternalStorageState();
        if(estatSD.equals(Environment.MEDIA_MOUNTED) || estatSD.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            try {
                FileInputStream f = context.openFileInput(FITXER);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(f));
                int n = 0;
                String linia;
                do {
                    linia = entrada.readLine();
                    if (linia != null) {
                        result.add(linia);
                        n++;
                    }
                } while (n < quantitat && linia != null);
                f.close();
            } catch (Exception e) {
                Log.e("Asteroides", e.getMessage(), e);
            }
        } else {
            Toast.makeText(context, "No se puede leer el almacenamiento externo", Toast.LENGTH_SHORT).show();
        }
        return result;
    }
}
