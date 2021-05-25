package com.cursfundacionesplai.restasearch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

public class EstablimentDetailActivity extends AppCompatActivity {

    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_establiment_detail);

        FirebaseCrashlytics.getInstance().setUserId("RESTASEARCH_PROVA");
        FirebaseCrashlytics.getInstance().setCustomKey("RESTASEARCH_PROVA","S'ha produit un error a la classe EstablimentDetailActivity");

        back = findViewById(R.id.btn_back);

        Bundle bundle = getIntent().getExtras();
        Log.d("RESTASEARCH", "onCreate: " + bundle.getString("place_id"));

        EstablimentFragment fragment = EstablimentFragment.newInstance(bundle.getString("place_id"));

        getSupportFragmentManager().beginTransaction().replace(R.id.list, fragment).commit();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}