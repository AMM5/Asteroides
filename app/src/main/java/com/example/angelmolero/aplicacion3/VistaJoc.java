package com.example.angelmolero.aplicacion3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.RectShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class VistaJoc extends View implements SensorEventListener {
    private Context context;
    private SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());

    //Variables per ASTEROIDES
    private Vector<Grafic> asteroides;//Vector amb els asteroides
    private int numAsteroides = 5; //Numero inicial de Asteroides
    private int numFragments = Integer.parseInt(pref.getString(getResources().getString(R.string.pa3_key),"1")); //Fragments en que es divideix
    private Drawable drawableAsteroide[] = new Drawable[numFragments];

    //Variables per la NAU
    private Grafic nau; //Gràfic de la nau
    private int girNau; //Increment de direcció
    private double acceleracioNau; //Augment de velocitat
    private static final int MAX_VELOCITAT_NAU=20;
    //increment estandard de gir i acceleració
    private static final int PAS_GIR_NAU=5;
    private static final float PAS_ACCELERACIO_NAU=0.5f;
    //Manejador d'events de la pantalla tàctil per la nau
    private float mX = 0, mY = 0;
    private boolean dispar = false;
    //FILS I TEMPS
    //Fil encarregat de processar el joc
    private ThreadJoc fil = new ThreadJoc();
    //Cada quan volem processar canvis (ms);
    private static int PERIODE_PROCES = 50;
    //Quan es va realitzat el darrer procés
    private long darrerProces = 0;
    //Sensores
    private boolean sensor;
    //Variables pel Missil
    //private Vector<Grafic> misiles;
    private Grafic missil;
    private static int PAS_VELOCITAT_MISSIL = 12;
   /* private boolean missilActiu = false;
    private int tempsMissil;*/

    private List<Integer> tempsMissils = new ArrayList<Integer>();
    private List<Boolean> missilsActiu = new ArrayList<Boolean>();
    private List<Grafic> missils = new ArrayList<Grafic>();

    private int numMissils = 20;
    private static int missilActual = 0;

    //Variables pel so
    //MediaPlayer mpDispar, mpExplosio;
    SoundPool soundPool;
    int idDispar, idExplosio;

    //Sensores
    private SensorManager mSensorManager;

    public VistaJoc(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        //Declara i obte les imatges
        Drawable drawableNau, drawableMissil;
       // drawableAsteroide = context.getResources().getDrawable(R.drawable.asteroide1);
        if (pref.getString(getResources().getString(R.string.pa2_key),"1").equals("0")) {
            setLayerType(View.LAYER_TYPE_SOFTWARE,null);
            Path pathNau = new Path();
            Path pathAsteroide = new Path();
            //NAU
            pathNau.moveTo((float)0.0, (float)0.0);
            pathNau.lineTo((float)1.0,(float)0.5);
            pathNau.lineTo((float)0.0,(float)1.0);
            pathNau.lineTo((float)0.0,(float)0.0);
            ShapeDrawable dNau = new ShapeDrawable(new PathShape(pathNau, 1, 1));
            dNau.getPaint().setColor(Color.WHITE);
            dNau.getPaint().setStyle(Paint.Style.FILL);
            dNau.setIntrinsicWidth(20);
            dNau.setIntrinsicHeight(15);
            drawableNau = dNau;

            pathAsteroide.moveTo((float)0.3, (float)0.0);
            pathAsteroide.lineTo((float)0.6,(float)0.0);
            pathAsteroide.lineTo((float)0.6,(float)0.3);
            pathAsteroide.lineTo((float)0.8,(float)0.2);
            pathAsteroide.lineTo((float)1.0,(float)0.4);
            pathAsteroide.lineTo((float)0.8,(float)0.6);
            pathAsteroide.lineTo((float)0.9,(float)0.9);
            pathAsteroide.lineTo((float)0.8,(float)1.0);
            pathAsteroide.lineTo((float)0.4,(float)1.0);
            pathAsteroide.lineTo((float)0.0,(float)0.6);
            pathAsteroide.lineTo((float)0.0,(float)0.2);
            pathAsteroide.lineTo((float)0.3,(float)0.0);
            setBackgroundColor(Color.BLACK);

            for (int i=0; i<numFragments; i++) {
                ShapeDrawable dAsteroide = new ShapeDrawable(new PathShape(pathAsteroide, 1, 1));
                dAsteroide.getPaint().setColor(Color.WHITE);
                dAsteroide.getPaint().setStyle(Paint.Style.STROKE);
                dAsteroide.setIntrinsicWidth(50-i*14);
                dAsteroide.setIntrinsicHeight(50-i*14);
                drawableAsteroide[i] = dAsteroide;
            }

            /*//Gràfic vectorial Missil
            ShapeDrawable dMissil = new ShapeDrawable();
            dMissil.getPaint().setColor(Color.WHITE);
            dMissil.getPaint().setStyle(Paint.Style.STROKE);
            dMissil.setIntrinsicWidth(15);
            dMissil.setIntrinsicHeight(3);
            drawableMissil = dMissil;*/

        } else {
            drawableAsteroide[0] = context.getResources().getDrawable(R.drawable.asteroide1);
            drawableAsteroide[1] = context.getResources().getDrawable(R.drawable.asteroide2);
            drawableAsteroide[2] = context.getResources().getDrawable(R.drawable.asteroide3);
            drawableNau = context.getResources().getDrawable(R.drawable.nau);
            drawableMissil = context.getResources().getDrawable(R.drawable.missil1);
        }

        //Inicializar la variable drawableMissil
       // missil = new Grafic(this, drawableMissil);
        //Inicializar la variable nau
        nau = new Grafic(this, drawableNau);
        //Inicialitza els asteroides
        asteroides = new Vector<Grafic>();

        for (int i = 0; i<numAsteroides; i++) {
            Grafic asteroide = new Grafic(this, drawableAsteroide[0]);
            asteroide.setIncY(Math.random()*4-2);
            asteroide.setIncX(Math.random()*4-2);
            asteroide.setAngle((int)(Math.random()*360));
            asteroide.setRotacio((int)(Math.random()*8-4));
            asteroides.add(asteroide);
        }

        //Registre el sensor d'orientació i indica gestió d'events.
        mSensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> llistaSensors = mSensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
        if (!llistaSensors.isEmpty()) {
            Sensor orientacioSensor = llistaSensors.get(0);
            mSensorManager.registerListener(this, orientacioSensor, SensorManager.SENSOR_DELAY_GAME);
        }

        //Sensores
        sensor = pref.getBoolean(getResources().getString(R.string.pa4_key), true);

        //Inicialitza so
       /* mpDispar = MediaPlayer.create(context, R.raw.dispar);
        mpExplosio = MediaStore.Audio.Media.create(context, R.raw.explosio);*/
       soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
       idDispar = soundPool.load(context, R.raw.dispar, 0);
       idExplosio = soundPool.load(context, R.raw.explosio, 0);
    }

    @Override
    protected void onSizeChanged(int ample, int alt, int ample_ant, int alt_ant) {
        super.onSizeChanged(ample, alt, ample_ant, alt_ant);
        //Una vegada que coneixem la nostra amplada i altura
        //Posicionar nau
        nau.setCenY(getMeasuredHeight()/2);
        nau.setCenX(getMeasuredWidth()/2);
        //Posiciona els asteroides
        for (Grafic asteroide:asteroides) {
            do {
                asteroide.setCenX((int) (Math.random() * ample));
                asteroide.setCenY((int) (Math.random() * alt));
            } while (asteroide.distancia(nau) < (ample+alt)/5);
        }

        //Llança un nou fil
        darrerProces = System.currentTimeMillis();
        fil.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Dibuixa els asteroides
        for (Grafic asteroide:asteroides) {
            asteroide.dibuixaGrafic(canvas);
        }

        //Dibuixar nau
        nau.dibuixaGrafic(canvas);

        /*if (missilActiu) {
            missil.dibuixaGrafic(canvas);
        }*/
        for(Grafic missil: missils){
            missil.dibuixaGrafic(canvas);
        }

    }

    //ACTUALITZA ELS VALORS DELS ELEMENTS
    //ES A DIR, GESTIONA ELS MOVIMENTS
    protected void actualitzaFisica() {
        //Hora actual en milisegons
        long ara = System.currentTimeMillis();
        //No fer res si el periode de proces NO s'ha complert
        if (darrerProces+PERIODE_PROCES > ara) {
            return;
        }
        //Per una execució en temps real calculem retard
        double retard = (ara-darrerProces)/PERIODE_PROCES;
        darrerProces = ara;//Per la propera vegada
        //Actualitzem velocitat i direcció de la nau a partir de
        //girNau i acceleracioNau segons l'entrada del jugador
        nau.setAngle((int)(nau.getAngle()+girNau*retard));
        double nIncX = nau.getIncX()+acceleracioNau*Math.cos(
                                    Math.toRadians(nau.getAngle()))*retard;
        double nIncY = nau.getIncY()+acceleracioNau*Math.sin(
                                    Math.toRadians(nau.getAngle()))*retard;
        //Actualitzem si el mòdul de la velocitat no passa el màxim
        if (Math.hypot(nIncX,nIncY) <= MAX_VELOCITAT_NAU) {
            nau.setIncX(nIncX);
            nau.setIncY(nIncY);
        }
        //Actualitzem les posicions X i Y
        nau.incrementaPos(retard);
        for (int i=0; i<asteroides.size(); i++) {
            asteroides.get(i).incrementaPos(retard);
        }

        //Actualitzem posició del missil
        for(int i = 0; i<missils.size(); i++){
            missils.get(i).incrementaPos(retard);
            double aux = tempsMissils.get(i) - retard;
            tempsMissils.set(i, (int)aux);
            if(aux < 1){
                missils.remove(i);
                tempsMissils.remove(i);
            }
            if(!missils.isEmpty()){
                for (int u = 0; u < asteroides.size(); u++){
                    if(missils.get(i).verificaColisio(asteroides.get(u))){
                        destrueixAsteroide(u);
                        missils.remove(i);
                        break;
                    }
                }
            }
        }
    }
//GESTIÓ D'EVENTS DE SENSORS PER LA NAU
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (pref.getBoolean(getResources().getString(R.string.pa4_key), true)) {
            float valor = event.values[1]; //eix y
            if (!hihaValorInicial) {
                valorInicial = valor;
                hihaValorInicial = true;
            }
            girNau = (int) (valor - valorInicial) / 3;
        }
    }
    private boolean hihaValorInicial = false;
    private float valorInicial;

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //CLASSE QUE CREA UN NOU FIL
    class ThreadJoc extends Thread {
        private boolean pausa, corrent;

        public synchronized void pausar() {
            pausa = true;
        }

        public synchronized void reanudar() {
            pausa = false;
            notify();
        }

        public synchronized void aturar() {
            corrent = false;
            if (pausa) reanudar();
        }

        public void run() {
            corrent = true;
            while (true) {
                actualitzaFisica();
                synchronized (this) {
                    while(pausa) {
                        try {
                            wait();
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
    }

    //GESTIÓ D'EVENTS DE LA NAU AMB PANTALLA TACTIL
    @Override
    public boolean onTouchEvent(MotionEvent mevent) {
        super.onTouchEvent(mevent);
        float x = mevent.getX();
        float y = mevent.getY();
        switch (mevent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dispar = true;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(x - mX);
                float dy = Math.abs(y - mY);
                if (dy < 6 && dx > 6) {
                    if (pref.getBoolean(getResources().getString(R.string.pa4_key), false) == false) {
                        girNau = Math.round((x - mX) / 2);
                        dispar = false;
                    }
                } else if (dx < 6 && dy > 6) {
                    acceleracioNau = Math.round((mY - y) / 25);
                    dispar = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                girNau = 0;
                acceleracioNau = 0;
                if (dispar) {
                    ActivaMissil();
                }
                break;
        }
        mX = x; mY = y;
        return true;
    }

    //MÈTODES AUXILIARS
    private void destrueixAsteroide(int i) {
        int tam;
        if (asteroides.get(i).getDrawable()!=drawableAsteroide[2]) {
            if (asteroides.get(i).getDrawable()==drawableAsteroide[1]) {
                tam=2; //[1]
            } else {//[0]
                tam=1;
            }
            for (int n=0; n<numFragments; n++) {
                Grafic asteroide = new Grafic(this, drawableAsteroide[tam]);
                asteroide.setCenX(asteroides.get(i).getCenX());
                asteroide.setCenY(asteroides.get(i).getCenY());
                asteroide.setIncX(Math.random()*7-2-tam);
                asteroide.setIncY(Math.random()*7-2-tam);
                asteroide.setAngle((int)(Math.random()*360));
                asteroide.setRotacio((int)(Math.random()*8-4));
                asteroides.add(asteroide);
            }
        }
        asteroides.remove(i);
       // missilActiu = false;
       // mpExplosio.start();
      /*  if (pref.getBoolean(getResources().getString(R.string.pa1_key), true)) {
            // mp.start();
            startService(new Intent(VistaJoc.this, ServeiMusica.class));
        } else {
            // mp.pause();
            stopService(new Intent(VistaJoc.this, ServeiMusica.class));
        }*/

        if (pref.getBoolean(getResources().getString(R.string.pa1_key), true)) {
            soundPool.play(idExplosio, 1, 1, 0, 0, 1);
        }
    }

    private void ActivaMissil() {
        Drawable drawableMissil;
        if (pref.getString(getResources().getString(R.string.pa2_key), "1").equals("0")) {
            //Gràfic vectorial Missil
            ShapeDrawable dMissil = new ShapeDrawable(new RectShape());
            dMissil.getPaint().setColor(Color.WHITE);
            dMissil.getPaint().setStyle(Paint.Style.STROKE);
            dMissil.setIntrinsicWidth(15);
            dMissil.setIntrinsicHeight(3);
            drawableMissil=dMissil;
        } else {
            drawableMissil = this.context.getResources().getDrawable(R.drawable.missil1);
        }
        missil = new Grafic(this, drawableMissil);
        missil.setCenX(nau.getCenX());
        missil.setCenY(nau.getCenY());
        missil.setAngle(nau.getAngle());
        missil.setIncX(Math.cos(Math.toRadians(missil.getAngle()))*PAS_VELOCITAT_MISSIL);
        missil.setIncY(Math.sin(Math.toRadians(missil.getAngle()))*PAS_VELOCITAT_MISSIL);
        double tempsMissil = (int)Math.min(
                this.getWidth()/Math.abs(missil.getIncX()),
                this.getHeight()/Math.abs(missil.getIncY()))-2;
        missils.add(missil);
        tempsMissils.add((int)tempsMissil);
        //missilActiu=true;
       // mpDispar.start();
        if (pref.getBoolean(getResources().getString(R.string.pa1_key), true)) {
            soundPool.play(idDispar, 1, 1, 1, 0, 1);
        }
    }

    public ThreadJoc getFil() {
        return fil;
    }

    public SensorManager getmSensorManager() {
        return mSensorManager;
    }
}
