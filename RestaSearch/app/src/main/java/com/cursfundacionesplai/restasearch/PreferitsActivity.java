package com.cursfundacionesplai.restasearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.cursfundacionesplai.restasearch.adapters.RestaurantAdapter;
import com.cursfundacionesplai.restasearch.classesextended.ToolbarEx;
import com.cursfundacionesplai.restasearch.helpers.LanguageHelper;
import com.cursfundacionesplai.restasearch.models.Restaurant;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;

public class PreferitsActivity extends AppCompatActivity {

    ToolbarEx toolbarEx;

    RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LanguageHelper.loadSavedLanguage(this);

        setContentView(R.layout.activity_preferits);

        Bundle extras = getIntent().getExtras();
        setTitle(extras.getString("titol"));

        list = findViewById(R.id.list);

        toolbarEx = new ToolbarEx(this,
                this.findViewById(R.id.toolbar),
                this.findViewById(R.id.drawer_layout),
                this.findViewById(R.id.navigation_view));

        toolbarEx.getNavigationView().setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return toolbarEx.onNavigationItemSelected(item);
            }
        });

        ArrayList<Restaurant> restaurants = new ArrayList<>();

        Restaurant r = new Restaurant();
        r.setPlace_id("ChIJ0Q0yD9nmuhIRyG5UM2dyVxQ");
        r.setName("Rest test 1");

        Restaurant r2 = new Restaurant();
        r2.setPlace_id("ChIJn-TkqiHnuhIRWCxxLWqeJ14");
        r2.setName("Rest test 2");

        restaurants.add(r);
        restaurants.add(r2);

        RestaurantAdapter adapter = new RestaurantAdapter(this, restaurants);

        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));
    }
}