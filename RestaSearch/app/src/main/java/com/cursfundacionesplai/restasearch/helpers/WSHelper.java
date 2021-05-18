package com.cursfundacionesplai.restasearch.helpers;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cursfundacionesplai.restasearch.MapsFragment;
import com.cursfundacionesplai.restasearch.models.Restaurant;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class WSHelper {

    private static String URL_BASE = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";

    RequestQueue cuaPeticions;
    JsonArrayRequest jsonArrayRequest;
    JsonObjectRequest jsonObjectRequest;

    ArrayList<Restaurant> restaurants;

    private static String STATUS_CODE_OK = "OK";
    private static String STATUS_CODE_UNKNOWN_ERROR = "UNKNOWN_ERROR";
    private static String STATUS_CODE_ZERO_RESULTS = "ZERO_RESULTS";
    private static String STATUS_CODE_OVER_QUERY_LIMIT = "OVER_QUERY_LIMIT";
    private static String STATUS_CODE_REQUEST_DENIED = "REQUEST_DENIED";
    private static String STATUS_CODE_INVALID_REQUEST = "INVALID_REQUEST";
    private static String STATUS_CODE_NOT_FOUND = "NOT_FOUND";

    public WSHelper(Context context){
        cuaPeticions = Volley.newRequestQueue(context);
        restaurants = new ArrayList<>();
    }

    public void buscarRestaurants(LatLng possition, MapsFragment mapsFragment){
        /*
        Aquí és on es posarà la informació dels filtres i despres es passaran a una funció per
        que ens crei la URL
         */
        String url;
        Map<String, Object> values = new HashMap<>();

        values.put("location", possition.latitude+","+possition.longitude);
        values.put("radius", 50000d);
        values.put("key","AIzaSyAH53nRGennl8oBDVBPMx1AhhWO5Kb9Ohw");
        url = posarFiltre(values);

        /*
        Aquesta és la crida que s'encarrega de buscar els restaurants i de mostrar-los al mapa
         */
        jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();

                try {
                    //Mirem que el status sigui OK
                    if(response.getString("status").equals(STATUS_CODE_OK)){
                        Log.d("RESTASEARCH", "Restaurants rebuts");

                        //Agafem l'array de results i el convertim a una array de restaurants i els guardem
                        JSONArray result = response.getJSONArray("results");
                        ArrayList<Restaurant> results = new ArrayList<>(Arrays.asList(gson.fromJson(String.valueOf(result),Restaurant[].class)));
                        restaurants.addAll(results);
                    }
                    //Todo: falta tindre en compte els altres STATUS_CODE
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Netejem els markers que hi hagin i podem els dels restaurants
                mapsFragment.clearMarkers();
                for(Restaurant restaurant : restaurants){
                    LatLng possition = new LatLng(
                            restaurant.getGeometry().getLocation().getLat(),
                            restaurant.getGeometry().getLocation().getLng()
                    );
                    mapsFragment.loadPossition(restaurant.getName(), possition);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("RESTASEARCH", "Error al buscar els restaurants");
            }
        });

        cuaPeticions.add(jsonObjectRequest);
    }

    private String posarFiltre(Map<String, Object> values){
        /*
        Aquesta funció s'encarrega de crear-nos la url. El que fa es per cada entrada que hi hagi
        en el map ens afaga la key i el valor i ens el va afegint
         */
        String url = URL_BASE;
        boolean first = true;
        for(Map.Entry<String, Object> entry : values.entrySet()){

            if(!first){
                url += "&";
            }
            else{
                first = false;
            }

            url += entry.getKey() + "=" + entry.getValue().toString();
        }
        Log.d("NIL", url);
        return url;
    }
}