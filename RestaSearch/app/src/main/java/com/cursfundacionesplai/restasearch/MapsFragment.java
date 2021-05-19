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
        mMap.addMarker(new MarkerOptions().position(possition).title(title));
    }

    public void possitionCamera(LatLng possition, int zoom){
        /*
        Aquesta funció s'encarrega de centrar la càmara en la posició que estem actualment
         */
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(possition, zoom));
    }

    public void afegirCercle (LatLng position, double radius) {
        /* Determina el radi que es mostrarà
        quan estableixis el teu radi de cerca (per Km a la rondona) */
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(position);
        mMap.clear();
        Circle circle = mMap.addCircle(
                new CircleOptions()
                        .center(position)
                        .radius(radius+12000)
                        .strokeWidth(3f)
                        .strokeColor(R.color.secondary_color)
                        .fillColor(getActivity().getResources().getColor(R.color.circle_color))
        );

    }
}
