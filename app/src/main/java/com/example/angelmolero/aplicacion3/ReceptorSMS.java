package com.example.angelmolero.aplicacion3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReceptorSMS extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, AcercaDe.class);
        //L'activitat s'ha de crear en una nova tasca
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
