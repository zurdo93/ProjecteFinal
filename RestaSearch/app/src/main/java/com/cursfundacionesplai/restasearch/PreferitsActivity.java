package com.cursfundacionesplai.restasearch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class PreferitsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferits);

        Bundle extras = getIntent().getExtras();
        setTitle(extras.getString("titol"));
    }
}