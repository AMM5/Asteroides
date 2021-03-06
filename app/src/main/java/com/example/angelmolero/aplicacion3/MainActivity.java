package com.example.angelmolero.aplicacion3;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener, View.OnLongClickListener, GestureOverlayView.OnGesturePerformedListener {
    private Button bJugar;
    private Button bAcercaDe;
    private Button bSortir;
    private Button bConfigurar;

    //Gesturas
    private GestureLibrary llibreria;
    private TextView sortida;

    //Música
    public static MediaPlayer mp;
    private String var;
    private int pos;

    public  static MagatzemPuntuacions magatzem = new MagatzemPuntuacionsArray();

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "onCreate", Toast.LENGTH_LONG).show();
        bJugar = (Button)findViewById(R.id.button);
        bJugar.setOnClickListener(this);

        bAcercaDe=(Button)findViewById(R.id.button3);
        bAcercaDe.setOnLongClickListener(this);

        bConfigurar = (Button)findViewById(R.id.button2);
        bConfigurar.setOnClickListener(this);
        bConfigurar.setOnLongClickListener(this);

        bSortir=(Button)findViewById(R.id.button4);
        bSortir.setOnClickListener(this);
        bSortir.setOnLongClickListener(this);

        //Gesturas
        llibreria = GestureLibraries.fromRawResource(this, R.raw.gestures);
        if (!llibreria.load()) finish();
        GestureOverlayView gesturesView = (GestureOverlayView)findViewById(R.id.gestures);
        //associa escoltador d'event de la gesturaen la mateixa classe
        gesturesView.addOnGesturePerformedListener(this);
        sortida = (TextView)findViewById(R.id.textView2);

        //Música
        /*mp = MediaPlayer.create(this, R.raw.audio);
        mp.start();*/
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        if (pref.getBoolean(getResources().getString(R.string.pa1_key), true)) {
            // mp.start();
            startService(new Intent(MainActivity.this, ServeiMusica.class));
        } else {
            // mp.pause();
            stopService(new Intent(MainActivity.this, ServeiMusica.class));
        }

       /* magatzem = new MagatzemPuntuacionsPreferencies(this);
        magatzem = new MagatzemPuntuacionsFitxerIntern(this);*/
    }

    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        ArrayList<Prediction> predictions = llibreria.recognize(gesture);
        String nombre = "";
        double score = 0;
        sortida.setText("");
        for (Prediction prediction : predictions) {
            if (score<prediction.score) {
                nombre = prediction.name;
                score = prediction.score;
            }
           // sortida.append(prediction.name+" "+prediction.score+"\n");
        }
        sortida.setText(nombre);

        switch (nombre) {
            case "jugar": llancarJoc(null); break;
            case "acercade": llancarAcercaDe(null); break;
            case "cancelar": finish(); break;
            case "configurar": lanzarConfigurar(null); break;
        }
    }

    public void llancarJoc(View view) {
        Intent i = new Intent(this, Joc.class);
        //Llançar una activitat mitjançant un objecte Intenció.
        startActivityForResult(i, 1234);
    }

    //Mètode que es crida de forma automàtica quan finalitza
    //l'activitat secundària. Permet llegir les dades retornades.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1234 && resultCode==RESULT_OK && data!=null) {
            int puntuacio = data.getExtras().getInt("puntuacio");
            String nom = "Jo";
            //Millor si ho llegim des d'un diàleg o una nova activitat
            //AlertDialog.Builder
            magatzem.guardarPuntuacio(puntuacio, nom, System.currentTimeMillis());
            llancarPuntuacions(null);
        }
    }

    //Mètode que s'executa quean pitjam boto4
    //Llança una nova activitat per mostrar les puntuacions
    public void llancarPuntuacions(View view) {
        Intent i = new Intent(this, Puntuacions.class);
        //Llançar una activitat mitjançant un objecte Intenció.
        startActivity(i);
    }

    public String mostrarPreferencies(View view) {
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        String s = "música "+pref.getBoolean(getResources().getString(R.string.pa1_key), false)+
                    "\ntipo de Grafico "+pref.getString(getResources().getString(R.string.pa2_key),"1")+
                    "\nnúmero de fragmentos "+pref.getString(getResources().getString(R.string.pa3_key),"3")+
                    "\nactivar multijugador "+pref.getBoolean(getResources().getString(R.string.multijugador_key), false)+
                    "\nmaximo de jugadores "+pref.getString(getResources().getString(R.string.maximoJugadores_key),"1")+
                    "\ntipos de conexión "+pref.getString(getResources().getString(R.string.conexion_key),"1");
        return s;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button: llancarJoc(v); break;
            case R.id.button2: lanzarConfigurar(v); break;
            case R.id.button3: llancarAcercaDe(v); break;
            case R.id.button4: finish(); break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.button2: mostrarPreferencies(v); return true;
            case R.id.button4: llancarPuntuacions(v); return true;
        }
        return false;
    }

    public void lanzarConfigurar(View v) {
        Intent i = new Intent(this,Preferencies.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        //Crea l'objecte en Java que representa el menú
        MenuInflater infl = getMenuInflater();
        //Associa el menú creat en XML a l'objecte Java
        infl.inflate(R.menu.menu_main, menu);
        //Indica que es vol visualitzar (activar) el menú
        return true;
    }

    public void crearAlmacen() {
        //pref = getSharedPreferences("com.example.angelmolero.asteroides_preferences", MODE_PRIVATE);

        String sAlmacen = pref.getString(getResources().getString(R.string.pa5_key), "0");
        int tipoAlmacen = Integer.valueOf(sAlmacen);

        switch(tipoAlmacen) {
            case 0: magatzem = new MagatzemPuntuacionsArray();
               // Toast.makeText(MainActivity.this, "array", Toast.LENGTH_SHORT).show();
                break;
            case 1: magatzem = new MagatzemPuntuacionsPreferencies(this);
                //    Toast.makeText(MainActivity.this, "preferencias", Toast.LENGTH_SHORT).show();
                    break;
            case 2: magatzem = new MagatzemPuntuacionsFitxerIntern(this);
               // Toast.makeText(MainActivity.this, "interna", Toast.LENGTH_SHORT).show();
                break;
            case 3: magatzem = new MagatzemPuntuacionsFitxerExtern(this);
               //  Toast.makeText(MainActivity.this, "externa", Toast.LENGTH_SHORT).show();
                 break;
            case 4: magatzem = new MagatzemPuntuacionsXML_SAX(this);
               // Toast.makeText(MainActivity.this, "xml", Toast.LENGTH_SHORT).show();
                break;
            case 5: magatzem = new MagatzemPuntuacionsGson();
                // Toast.makeText(MainActivity.this, "xml", Toast.LENGTH_SHORT).show();
                break;
            case 6: magatzem = new MagatzemPuntuacionsJSon();
                // Toast.makeText(MainActivity.this, "xml", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //Cada vegada que es selecciona el menú es crida el següent mètode
    //per què tracti els esdeveniments capturats.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.config) {
            lanzarConfigurar(null);
            return true;
        }
        if (id==R.id.acercaDe) {
            llancarAcercaDe(null);
            //Indica que l'event ha sigut tractat i que no s'ha de propagar més
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void llancarAcercaDe(View view) {
        Intent i = new Intent(this, AcercaDe.class);
        startActivity(i);
    }

    @Override
    protected void onSaveInstanceState(Bundle estadoGuardado){
        super.onSaveInstanceState(estadoGuardado);
        if (mp != null) {
            int pos = mp.getCurrentPosition();
            estadoGuardado.putInt("posicion", pos);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle estadoGuardado){
        super.onRestoreInstanceState(estadoGuardado);
        if (estadoGuardado != null && mp != null) {
            int pos = estadoGuardado.getInt("posicion");
            mp.seekTo(pos);
        }
    }

    @Override protected void onStart() {
        super.onStart();
        Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
        crearAlmacen();
    }

    @Override protected void onResume() {
        super.onResume();
        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();

        if (pref.getBoolean(getResources().getString(R.string.pa1_key), true)) {
           // mp.start();
            startService(new Intent(MainActivity.this, ServeiMusica.class));
        } else {
           // mp.pause();
            stopService(new Intent(MainActivity.this, ServeiMusica.class));
        }

    }

    @Override protected void onPause() {
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
        super.onPause();
        stopService(new Intent(MainActivity.this, ServeiMusica.class));
       // mp.pause();
    }

    @Override protected void onStop() {
        super.onStop();
        Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
       // mp.pause();
    }

    @Override protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();
       // mp.start();
    }

    @Override protected void onDestroy() {
        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
        stopService(new Intent(MainActivity.this, ServeiMusica.class));
        super.onDestroy();
    }
}
