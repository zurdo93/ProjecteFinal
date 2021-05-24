package com.cursfundacionesplai.restasearch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class EstablimentDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_establiment_detail);

        Bundle bundle = getIntent().getExtras();

        Log.d("RESTASEARCH", "onCreate: " + bundle.getString("place_id"));

        EstablimentFragment fragment = EstablimentFragment.newInstance(bundle.getString("place_id"));

        getSupportFragmentManager().beginTransaction().replace(R.id.list, fragment).commit();
    }
}