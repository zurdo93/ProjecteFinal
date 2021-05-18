package com.cursfundacionesplai.restasearch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.cursfundacionesplai.restasearch.helpers.LanguageHelper;

public class HistorialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LanguageHelper.loadSavedLanguage(this);

        setContentView(R.layout.activity_historial);

        Bundle extras = getIntent().getExtras();
        setTitle(extras.getString("titol"));
    }
}