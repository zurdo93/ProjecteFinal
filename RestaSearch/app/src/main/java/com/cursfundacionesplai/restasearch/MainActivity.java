package com.cursfundacionesplai.restasearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.internal.NavigationMenuItemView;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;
import com.novoda.merlin.Endpoint;
import com.novoda.merlin.Merlin;
import com.novoda.merlin.MerlinsBeard;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private static String PROVIDER = LocationManager.NETWORK_PROVIDER;

    SharedPreferences prefs;
    ToolbarEx toolbarEx;

    MapsFragment mapsFragment;
    LocationManager locationManager;

    Merlin merlin;
    MerlinsBeard merlinsBeard;

    Button btnDistance;
    Button btnPreu;
    Button btnValoracio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        Li posem el títol a la pantalla
         */
        setTitle(R.string.menu_inici);

        /*
        Agafem el valor del sharedPreferences per saber si hem de mostrar la pantalla de política
        de privacitat o no.
         */
        prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        boolean acceptedPolicy = prefs.getBoolean("key_shared_prefs_policy", false);
        if (!acceptedPolicy) {
            Intent intent = new Intent(this, PoliticaActivity.class);
            intent.putExtra("titol", getResources().getString(R.string.menu_privacitat_us));
            startActivity(intent);
        }

        merlin = new Merlin.Builder().withConnectableCallbacks().withDisconnectableCallbacks().build(this);
        merlinsBeard = MerlinsBeard.from(this);

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

        /*
        Inicialitzem el mapa i la localització
         */
        mapsFragment = new MapsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.lin_map, mapsFragment).commit();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // si te conexio amb wifi o amb dades, mostrar el contenidor d'anuncis
        if (merlinsBeard.isConnectedToWifi() || merlinsBeard.isConnectedToMobileNetwork()) {

            Log.d("miki", "TE INTERNET");

            // Vincula el contenidor d'anuncis
            AdView ad = findViewById(R.id.adView);

            // crear una peticio per generar un anunci
            AdRequest adRequest = new AdRequest.Builder().build();

            // carregar l'anunci generat al contenidor d'anuncis
            ad.loadAd(adRequest);
        }
        //Sistema de filtres mitjançant alerts.
        //region Filtres
        btnDistance = findViewById(R.id.btn_distancia);
        btnDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Determina la Distancia (en Km)");
                String[] botons = {"10 km ", "20 km ", "30 km ", "40 km ", "50 km "};
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
                        mapsFragment.afegirCercle(position, radius);
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
                alert.setTitle("Determina el rang de preu");
                String[] botons = {"Molt Barato", "Barato", "Mitjà", "Car", "Molt Car"};
                alert.setItems(botons, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                     switch (which){
                         case 0:
                             

                     }
                    }
                });
            }
        });
    //endregion
    }

    @Override
    protected void onResume() {
        super.onResume();

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

    LatLng position = new LatLng(0,0);
    double radius = 10;
    @Override
    public void onLocationChanged(@NonNull Location location) {
        /*
        Agafem la localització i posem un punt en el mapa i centrem la càmara
         */
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        position = new LatLng(lat, lng);
        /*double latEnd = EndP.latitude;
        double lngEnd = EndP.longitude;*/

        mapsFragment.loadPossition("Jo", new LatLng(lat, lng));
        mapsFragment.possitionCamera(new LatLng(lat, lng));
        mapsFragment.afegirCercle(position,radius);

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
}