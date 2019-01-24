package com.example.angelmolero.aplicacion3;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class ServeiMusica extends Service {
    MediaPlayer reproductor;
    private static final int ID_NOTIFICACIO_CREAR = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Servei creat", Toast.LENGTH_SHORT).show();
        reproductor = MediaPlayer.create(this, R.raw.audio);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int idArranc) {
        super.onStartCommand(intent, flags, idArranc);
        //Creació de la notificació
        Notification.Builder notificacio = new Notification.Builder(this)
                .setContentTitle("Creant Servei de Música")//titol que descriu la notificació
                .setSmallIcon(R.mipmap.ic_launcher)//icono a visualitzar
                .setContentText("Informació adicional") //Informació més detallada
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_media_play))
                .setWhen(System.currentTimeMillis()+1000*60*60)
                .setContentInfo("més info")
                .setTicker("Text en barra d'estat");
        //Referència que permet manejar les notificacions del sistema
        NotificationManager nm;
        nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        //Igual que (SensorManager)getSystemService(SENSOR_SERVICE);
        //Igual que (LocationManager)getSystemService(LOCATION_SERVICE);
        //Llança la notificació
        nm.notify(ID_NOTIFICACIO_CREAR, notificacio.build());
        //El primer paràmetre incdica un id per identificar aquesta notificació
        //en el futur, i el segon la notificació.
        PendingIntent intencioPendent = PendingIntent.getActivity(this,0,
                new Intent(this, MainActivity.class), 0);
        notificacio.setContentIntent(intencioPendent);
        Toast.makeText(this, "Servei arrancat "+idArranc,Toast.LENGTH_SHORT).show();
        reproductor.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Elimina la notificació si el servei deixa d'estar actiu
        NotificationManager nm;
        nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(ID_NOTIFICACIO_CREAR);
        Toast.makeText(this, "Servei aturat", Toast.LENGTH_SHORT).show();
        reproductor.stop();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
