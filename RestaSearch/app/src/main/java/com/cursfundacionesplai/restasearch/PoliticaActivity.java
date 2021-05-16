package com.cursfundacionesplai.restasearch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class PoliticaActivity extends AppCompatActivity {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    CheckBox checkPolicy;
    Button btnAccept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_politica);

        prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        editor = prefs.edit();

        checkPolicy = this.findViewById(R.id.box_policy);
        btnAccept = this.findViewById(R.id.btn_accept);

        /*
        Todo: Si el valor que ens arriba del sharedpreferences es true, posem el botó i el checkbox amb
        visibilitat gone.
        També ho utilitzarem per mostrar o no la toolbar.
         */

        if(prefs.getBoolean("key_shared_prefs_policy",false)){
            btnAccept.setVisibility(View.GONE);
            checkPolicy.setVisibility(View.GONE);
        }

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = checkPolicy.isChecked();
                if (isChecked) {
                    editor.putBoolean("key_shared_prefs_policy", true);
                    editor.apply();

                    finish();
                }
            }
        });
    }
}