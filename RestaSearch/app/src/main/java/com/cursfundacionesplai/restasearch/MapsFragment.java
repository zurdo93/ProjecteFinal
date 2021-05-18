package com.cursfundacionesplai.restasearch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;

public class MapsFragment extends Fragment {

    GoogleMap mMap;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            /*
            LatLng sydney = new LatLng(-34, 151);
            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            */
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    public void loadPossition(String title, LatLng possition){
        /*
        Aquest mètode s'encarrega de netejar els markers que hi hagin posat de la nostre localització.
        Això ho haurem de canviar perquè si posem els markers dels restaurants, aquesta funció ens
        els borrarà
         */
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(possition).title(title));
    }

    public void possitionCamera(LatLng possition){
        /*
        Aquesta funció s'encarrega de centrar la càmara en la posició que estem actualment
         */
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(possition, 12));
    }
        /* Determina el radi que es mostrarà
        quan estableixis el teu radi de cerca (per Km a la rondona) */

    public void afegirCercle (LatLng position, double radius) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(position);
        mMap.clear();
        Marker marker = mMap.addMarker(markerOptions);
        Circle circle = mMap.addCircle(
                new CircleOptions()
                        .center(position)
                        .radius(radius*1000)
                        .strokeWidth(3f)
                        .strokeColor(R.color.secondary_color)
                        .fillColor(getActivity().getResources().getColor(R.color.circle_color))
        );

    }
    /*private LatLng calcularEndPoint(LatLng StartP) {
        double r = 6378.1; //Radius of the Earth
        double brng = 1.57; //Bearing is 90 degrees converted to radians.
        double d = 15; //Distance in km

        //lat2  52.20444 - the lat result I'm hoping for
        //lon2  0.36056 - the long result I'm hoping for.

        double lat1 = Math.toRadians(52.20472); //Current lat point converted to radians
        double lon1 = Math.toRadians(0.14056); //Current long point converted to radians

        double lat2 = Math.asin( Math.sin(lat1)*Math.cos(d/r) +
                Math.cos(lat1)*Math.sin(d/r)*Math.cos(brng));

        double lon2 = lon1 + Math.atan2(Math.sin(brng)*Math.sin(d/r)*Math.cos(lat1),
                Math.cos(d/r)-Math.sin(lat1)*Math.sin(lat2));

        lat2 = Math.toDegrees(lat2);
        lon2 = Math.toDegrees(lon2);
        Log.d("daniel", lat2+"");
        Log.d("daniel", lon2+"");
        return new LatLng(lat2,lon2);
    }*/
}
