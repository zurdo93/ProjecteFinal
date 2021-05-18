package com.cursfundacionesplai.restasearch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.cursfundacionesplai.restasearch.helpers.LanguageHelper;

public class PreferitsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LanguageHelper.loadSavedLanguage(this);

        setContentView(R.layout.activity_preferits);

        Bundle extras = getIntent().getExtras();
        setTitle(extras.getString("titol"));
    }
}