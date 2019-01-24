package com.example.angelmolero.aplicacion3;

import android.app.Activity;
import android.os.Bundle;

public class Preferencies extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PreferenciesFragment()).commit();
    }
}
