package com.cursfundacionesplai.restasearch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cursfundacionesplai.restasearch.helpers.LanguageHelper;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

public class IdiomaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseCrashlytics.getInstance().setUserId("RESTASEARCH_PROVA");
        FirebaseCrashlytics.getInstance().setCustomKey("RESTASEARCH_PROVA","S'ha produit un error a la classe IdiomaActivity");

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