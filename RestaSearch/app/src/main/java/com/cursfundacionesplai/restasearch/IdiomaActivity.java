package com.cursfundacionesplai.restasearch;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class IdiomaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LanguageHelper.loadSavedLanguage(this);

        setContentView(R.layout.activity_idioma);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.gc();

                LanguageHelper.saveLanguage(IdiomaActivity.this, v.getTag().toString());

                startActivity(new Intent(IdiomaActivity.this, MainActivity.class));
                finish();
            }
        };

        findViewById(R.id.lang_en).setOnClickListener(listener);
        findViewById(R.id.lang_es).setOnClickListener(listener);
        findViewById(R.id.lang_ca).setOnClickListener(listener);

        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}