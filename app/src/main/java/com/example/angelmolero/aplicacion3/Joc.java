package com.example.angelmolero.aplicacion3;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

public class Joc extends Activity {
    private VistaJoc vistaJoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joc);
        vistaJoc = (VistaJoc)findViewById(R.id.VistaJoc);
    }

    @Override
    protected void onPause() {
        super.onPause();
        vistaJoc.getFil().pausar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        vistaJoc.getFil().reanudar();
    }

    @Override
    protected void onStop() {
        vistaJoc.getmSensorManager().unregisterListener(vistaJoc);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        vistaJoc.getFil().aturar();
        super.onDestroy();
    }
}
