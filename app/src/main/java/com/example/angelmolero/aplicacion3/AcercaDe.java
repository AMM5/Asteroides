package com.example.angelmolero.aplicacion3;

import android.app.Activity;
import android.os.Bundle;

public class AcercaDe extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acercade);
        MainActivity.mp.start();
    }
}


