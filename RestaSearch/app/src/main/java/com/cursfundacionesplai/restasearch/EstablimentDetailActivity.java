package com.cursfundacionesplai.restasearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

import com.cursfundacionesplai.restasearch.classesextended.ToolbarEx;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

public class EstablimentDetailActivity extends AppCompatActivity {

    ToolbarEx toolbar;
    private String placeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_establiment_detail);
        this.placeName = placeName;

        FirebaseCrashlytics.getInstance().setUserId("RESTASEARCH_PROVA");
        FirebaseCrashlytics.getInstance().setCustomKey("RESTASEARCH_PROVA","S'ha produit un error a la classe EstablimentDetailActivity");

        toolbar = new ToolbarEx(this,
                findViewById(R.id.toolbar),
                findViewById(R.id.drawer_layout),
                findViewById(R.id.navigation_view));

        toolbar.getNavigationView().setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return toolbar.onNavigationItemSelected(item);
            }
        });

        Bundle bundle = getIntent().getExtras();
        Log.d("RESTASEARCH", "onCreate: " + bundle.getString("place_id"));

        EstablimentFragment fragment = EstablimentFragment.newInstance(bundle.getString("place_id"));

        getSupportFragmentManager().beginTransaction().replace(R.id.list, fragment).commit();

    }
    @Override
    public void onBackPressed() {
        if (toolbar.getDrawerLayout().isDrawerOpen(GravityCompat.START)) {
            toolbar.getDrawerLayout().closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    //TODO arreglar el botó de preferits!
}