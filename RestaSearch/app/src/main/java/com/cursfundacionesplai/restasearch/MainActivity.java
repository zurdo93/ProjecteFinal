package com.cursfundacionesplai.restasearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.internal.NavigationMenuItemView;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {


    SharedPreferences prefs;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);

        boolean acceptedPolicy = prefs.getBoolean("key_shared_prefs_policy", false);

        if (!acceptedPolicy) {
            startActivity(new Intent(this, PoliticaActivity.class));
        }

        //Amb això creem una variable toolbar i la vinculem amb la que tenim a la vista
        Toolbar toolbar = this.findViewById(R.id.toolbar);
        setSupportActionBar((Toolbar) this.findViewById(R.id.toolbar));

        //Vinculem el drawerLayout amb el de la vista i li afegim la toolbar
        drawerLayout = this.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = this.findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return MainActivity.this.onNavigationItemSelected(item);
            }
        });

        MenuItem menuItem = navigationView.getMenu().getItem(0);
        onNavigationItemSelected(menuItem);
        menuItem.setChecked(true);
    }

    @Override
    public void onBackPressed() {
        /*Aquest metode serveix per si tenim el menú desplegat i li donem al boto de tornar enradera,
          ens tanqui el menú i no ens torni a la pantalla anterior
         */
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int titol;
        switch (item.getItemId()){
            case R.id.nav_inici:
                titol = R.string.menu_inici;
                break;
            case R.id.nav_preferits:
                titol = R.string.menu_preferits;
                break;
            case R.id.nav_historial:
                titol = R.string.menu_historial;
                break;
            case R.id.nav_idioma:
                titol = R.string.menu_idioma;
                break;
            case R.id.nav_privacitat_us:
                titol = R.string.menu_privacitat_us;
                Intent intent = new Intent(MainActivity.this,PoliticaActivity.class);
                intent.putExtra("titol",getResources().getString(R.string.menu_privacitat_us));
                startActivity(intent);
                break;
            default:
                throw new IllegalArgumentException("Aquesta opció encara no està implementada");
        }

        setTitle(titol);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}