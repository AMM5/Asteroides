package com.example.angelmolero.aplicacion3;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class PreferenciesFragment extends PreferenceFragment {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencies);
        final EditTextPreference fragmentos = (EditTextPreference) findPreference(getResources().getString(R.string.pa3_key));
        fragmentos.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                int valor;
                try {
                    valor = Integer.parseInt((String)newValue);
                } catch(Exception e) {
                    Toast.makeText(getActivity(),getString(R.string.textoError1), Toast.LENGTH_SHORT).show();
                    return false;
                }

                if (valor>=0 && valor<=9) {
                    fragmentos.setSummary(getString(R.string.textoAsteroide1)+valor+")");
                    return true;
                } else {
                    Toast.makeText(getActivity(),R.string.textoMaximoFragmentos, Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        });
        restaurarValorFragments(fragmentos);
    }

    public void restaurarValorFragments(EditTextPreference pref) {
        // restaurar el ultimo valor
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String valor = preferences.getString(getResources().getString(R.string.pa3_key),"3");
        pref.setSummary("En cuantos trozos se divide un asteroide("+valor+")");
    }
}
