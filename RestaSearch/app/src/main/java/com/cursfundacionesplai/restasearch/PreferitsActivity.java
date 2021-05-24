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
import com.cursfundacionesplai.restasearch.models.RestaurantList;
import com.cursfundacionesplai.restasearch.models.RestaurantModel;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

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

        ArrayList<RestaurantModel> restaurantLists = new ArrayList<>();

        RestaurantModel r = new RestaurantModel();
        r.setPlace_id("ChIJ0Q0yD9nmuhIRyG5UM2dyVxQ");
        r.setName("Rest test 1");

        RestaurantModel r2 = new RestaurantModel();
        r2.setPlace_id("ChIJn-TkqiHnuhIRWCxxLWqeJ14");
        r2.setName("Rest test 2");

        restaurantLists.add(r);
        restaurantLists.add(r2);

        RestaurantAdapter adapter = new RestaurantAdapter(this, restaurantLists);

        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));
    }
}