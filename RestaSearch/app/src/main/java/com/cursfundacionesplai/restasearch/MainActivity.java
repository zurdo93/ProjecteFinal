package com.cursfundacionesplai.restasearch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);

        boolean acceptedPolicy = prefs.getBoolean("key_shared_prefs_policy", false);

        if (!acceptedPolicy) {
            startActivity(new Intent(this, PoliticaActivity.class));
        }
    }
}