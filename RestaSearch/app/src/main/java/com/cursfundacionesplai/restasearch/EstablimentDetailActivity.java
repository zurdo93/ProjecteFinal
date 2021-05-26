package com.cursfundacionesplai.restasearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.cursfundacionesplai.restasearch.classesextended.ToolbarEx;
import com.cursfundacionesplai.restasearch.helpers.DBHelper;
import com.cursfundacionesplai.restasearch.models.Keys;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

public class EstablimentDetailActivity extends AppCompatActivity {

    ToolbarEx toolbar;
    private String placeName;
    private String placeId;
    Boolean checked = false;
    DBHelper dbHelper;

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

        ImageView btnFavourite;
        btnFavourite = this.findViewById(R.id.btnFavourite);

        dbHelper = new DBHelper(this, Keys.DATABASE_NAME, null, Keys.DATABASE_VERSION);
    }
    @Override
    public void onBackPressed() {
        if (toolbar.getDrawerLayout().isDrawerOpen(GravityCompat.START)) {
            toolbar.getDrawerLayout().closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dialog, menu);
        if (dbHelper.isRestaurantByPlacesId(placeId)){
            menu.getItem(0).setIcon(getResources().getIdentifier("@drawable/baseline_bookmark_24", null, this.getPackageName()));

        }
        else{
            menu.getItem(0).setIcon(getResources().getIdentifier("@drawable/baseline_bookmark_border_24", null, this.getPackageName()));

        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.favourite){

            if (dbHelper.isRestaurantByPlacesId(placeId)){
                item.setIcon(getResources().getIdentifier("@drawable/baseline_bookmark_24", null, getPackageName()));
                dbHelper.insertFavourites(placeId);
            }
            else{
                item.setIcon(getResources().getIdentifier("@drawable/baseline_bookmark_border_24", null, getPackageName()));
                dbHelper.deleteFavourites(placeId);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}