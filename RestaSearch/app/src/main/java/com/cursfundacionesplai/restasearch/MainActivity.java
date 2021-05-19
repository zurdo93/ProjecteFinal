package com.cursfundacionesplai.restasearch;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;

import com.cursfundacionesplai.restasearch.helpers.AdsHelper;
import com.cursfundacionesplai.restasearch.helpers.LanguageHelper;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationView;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.cursfundacionesplai.restasearch.classesextended.ToolbarEx;
import com.cursfundacionesplai.restasearch.helpers.WSHelper;
import com.novoda.merlin.Merlin;
import com.novoda.merlin.MerlinsBeard;


public class MainActivity extends AppCompatActivity implements LocationListener {

    private static String PROVIDER = LocationManager.NETWORK_PROVIDER;

    SharedPreferences prefs;
    ToolbarEx toolbarEx;

    MapsFragment mapsFragment;
    LocationManager locationManager;
    WSHelper wsHelper;

    Button btnDistance;
    Button btnPreu;
    Button btnValoracio;

    RatingBar ratingBar;

    LatLng possition = new LatLng(0,0);
    double radius = 10;

    int price_level = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LanguageHelper.loadSavedLanguage(this);

        setContentView(R.layout.activity_main);

        /*
        Li posem el títol a la pantalla
         */
        setTitle(R.string.menu_inici);

        /*
        Agafem el valor del sharedPreferences per saber si hem de mostrar la pantalla de política
        de privacitat o no.
         */
        prefs = getSharedPreferences(Keys.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        boolean acceptedPolicy = prefs.getBoolean(Keys.PREFS_SAVE_POLICY, false);
        if (!acceptedPolicy) {
            Intent intent = new Intent(this, PoliticaActivity.class);
            intent.putExtra("titol", getResources().getString(R.string.menu_privacitat_us));
            startActivity(intent);
        }

        /*
        Inicialitzem la classe que s'encarregarà de controlar la toolbar i li pasem les referencies
        dels objectes de la vista
         */
        toolbarEx = new ToolbarEx(this,
                this.findViewById(R.id.toolbar),
                this.findViewById(R.id.drawer_layout),
                this.findViewById(R.id.navigation_view));

        /*
        Definim el listener per tal de que al fer un clic ens redirigeixi a les diferents pantalles
         */
        toolbarEx.getNavigationView().setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return toolbarEx.onNavigationItemSelected(item);
            }
        });

        wsHelper = new WSHelper(this);

        // funcio estatica d'una clase per carregar un anunci en un contenidor d'anuncis
        AdsHelper.loadAd(this, findViewById(R.id.adView));//Sistema de filtres mitjançant alerts.
        //region Filtres
        btnDistance = findViewById(R.id.btn_distancia);
        btnDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle(getResources().getString(R.string.alert_distancia));
                String[] botons = {"10 km", "20 km", "30 km", "40 km", "50 km"};
                alert.setItems(botons, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                radius = 10;
                                break;

                            case 1:
                                radius = 20;
                                break;

                            case 2:
                                radius = 30;
                                break;

                            case 3:
                                radius = 40;
                                break;

                            case 4:
                                radius = 50;
                                break;
                        }
                        marcarRestaurants();
                    }
                });

                alert.show();
            }
        });

        btnPreu = findViewById(R.id.btn_preu);
        btnPreu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle(getResources().getString(R.string.alert_preu));
                String[] botons = {getResources().getString(R.string.alert_price_1),
                        getResources().getString(R.string.alert_price_2),
                        getResources().getString(R.string.alert_price_3),
                        getResources().getString(R.string.alert_price_4)};
                alert.setItems(botons, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                price_level = 1;
                                break;
                            case 1:
                                price_level = 2;
                                break;
                            case 2:
                                price_level = 3;
                                break;
                            case 3:
                                price_level = 4;
                                break;
                        }
                    }
                });
                alert.show();
            }
        });
        btnValoracio = findViewById(R.id.btn_valoracio);
        //ratingBar = findViewById(R.id.ratingBar);
        btnValoracio.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle(getResources().getString(R.string.alert_rating_text));
                LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                View view = inflater.inflate(R.layout.alert_rating_layout,null);
                ratingBar = view.findViewById(R.id.ratingBar);
                alert.setView(view);
                Log.d("daniel", "rating:"+ (ratingBar == null));
                alert.setPositiveButton(getResources().getString(R.string.button_accept_policy), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String rating = getResources().getString(R.string.alert_rating_stars) + ratingBar.getRating();
                        Toast.makeText(getApplicationContext(), "\n" + rating, Toast.LENGTH_LONG).show();
                    }
                });
                alert.setNegativeButton(getResources().getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();
            }
        });

        //endregion
    }

    @Override
    protected void onStart() {
        super.onStart();

        /*
        Inicialitzem el mapa i la localització
         */
        mapsFragment = new MapsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.lin_map, mapsFragment).commit();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("miki", "onResume: ");

        /*
        Demanem els permisos en el cas de que no els tinguem. Si els tenim agafem la localització
         */
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else{
            locationManager.requestLocationUpdates(PROVIDER, 0, 0, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        locationManager.requestLocationUpdates(PROVIDER, 0, 0, this);
                    }
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        /*
        Aquest metode serveix per si tenim el menú desplegat i li donem al boto de tornar enradera,
        ens tanqui el menú i no ens torni a la pantalla anterior
         */
        if (toolbarEx.getDrawerLayout().isDrawerOpen(GravityCompat.START)) {
            toolbarEx.getDrawerLayout().closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //region Funcions Mapa
    public void marcarRestaurants(){
         /*
        Cridem a les funcions per posar la càmara en la nostre posició i perquè busqui els restaurants
        que estan dins del radi que hem especificat
         */
        mapsFragment.possitionCamera(possition);
        mapsFragment.afegirCercle(possition,radius);
        wsHelper.buscarRestaurants(possition, mapsFragment);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        /*
        Agafem la localització i posem un punt en el mapa i centrem la càmara
         */
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        possition = new LatLng(lat, lng);
        /*double latEnd = EndP.latitude;
        double lngEnd = EndP.longitude;*/

        marcarRestaurants();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
    //endregion
}