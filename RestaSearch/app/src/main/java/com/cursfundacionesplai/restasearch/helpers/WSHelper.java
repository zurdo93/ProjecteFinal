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
import com.cursfundacionesplai.restasearch.interfaces.CustomResponse;
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
    private static String URL_DETAILS = "https://maps.googleapis.com/maps/api/place/details/json?";
    private static String API_KEY = "AIzaSyAH53nRGennl8oBDVBPMx1AhhWO5Kb9Ohw";

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

    /*
    Todo: acabar de mirar si funciona tot correctament i que es mostrin bé els valors.
    Me trobat amb problemes de que al buscar restaurants per un preu, la URL la fa correctament
    però els resultats no coincideixen amb els restaurants que es mostren al mapa.
     */

    public WSHelper(Context context){
        cuaPeticions = Volley.newRequestQueue(context);
        restaurants = new ArrayList<>();
    }

    public void buscarRestaurants(LatLng actualPossition,
                                  double radius,
                                  int priceLevel,
                                  boolean restaurantOpen,
                                  float rating,
                                  MapsFragment mapsFragment){
        /*
        Aquí és on es posarà la informació dels filtres i despres es passaran a una funció per
        que ens crei la URL
         */
        String url;
        Map<String, Object> values = new HashMap<>();

        values.put("location", actualPossition.latitude+","+actualPossition.longitude);
        values.put("minprice", priceLevel);
        values.put("maxprice", priceLevel);
        values.put("type", "restaurant");
        values.put("keyword", "restaurant,food");
        values.put("key",API_KEY);
        values.put("radius", radius);
        url = posarFiltre(values, URL_BASE);

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
                        restaurants.clear();
                        restaurants.addAll(results);
                    }
                    //Todo: falta tindre en compte els altres STATUS_CODE
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Netejem els markers que hi hagin i podem els dels restaurants
                showRestaurants(restaurants, actualPossition, radius, restaurantOpen, rating, mapsFragment);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("RESTASEARCH", "Error al buscar els restaurants");
            }
        });

        cuaPeticions.add(jsonObjectRequest);
    }

    public void getEstablimentDetails(String placeId, CustomResponse.EstablimentDetail listener) {


        String fields = "business_status,opening_hours,review,photo,user_ratings_total,price_level,rating,formatted_address,international_phone_number,name";

        Map<String, Object> values = new HashMap<>();
        values.put("key", API_KEY);
        values.put("place_id", placeId);
        values.put("fields", fields);

        String url = posarFiltre(values, URL_DETAILS);

        jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("OK")) {

                        Log.d("miki", "onResponse: getting results");

                        JSONObject result = response.getJSONObject("result");

                        Restaurant r = new Restaurant();
                        r.setName(result.getString("name"));
                        r.setFormattedAdress(result.getString("formatted_address"));
                        r.setInternationalPhoneNumber(result.getString("international_phone_number"));
                        r.setRating(result.getDouble("rating"));
                        r.setUser_ratings_total(result.getInt("user_ratings_total"));

                        listener.onResponse(r);
                    }
                } catch (JSONException e) {
                    Log.d("miki", "onErrorResponse: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("miki", "onErrorResponse: " + error.getMessage());
                listener.onResponse(null);
            }
        });

        cuaPeticions.add(jsonObjectRequest);
    }

    public void showRestaurants(LatLng possition, double radius, boolean restaurantOpen, float rating, MapsFragment mapsFragment){
        showRestaurants(restaurants, possition, radius, restaurantOpen, rating, mapsFragment);
    }

    private void showRestaurants(ArrayList<Restaurant> restaurantsList, LatLng actualPossition, double radius, boolean restaurantOpen, float rating, MapsFragment mapsFragment){
        mapsFragment.afegirCercle(actualPossition, radius);
        for(Restaurant restaurant : restaurantsList){
            LatLng restaurantPossition = new LatLng(
                    restaurant.getGeometry().getLocation().getLat(),
                    restaurant.getGeometry().getLocation().getLng()
            );

            if(restaurantOpen) {
                if(restaurant.getOpening_hours().isOpen_now()) {
                    if (rating != 0) {
                        if(restaurant.getRating() >= rating){
                            mapsFragment.loadPossition(restaurant.getPlace_id(), restaurant.getName(), restaurantPossition);
                        }
                    }
                    else{
                        mapsFragment.loadPossition(restaurant.getPlace_id(), restaurant.getName(), restaurantPossition);
                    }
                }
            }
            else if(rating != 0){
                if(restaurant.getRating() >= rating){
                    mapsFragment.loadPossition(restaurant.getPlace_id(), restaurant.getName(), restaurantPossition);
                }
            }
            else{
                mapsFragment.loadPossition(restaurant.getPlace_id(), restaurant.getName(), restaurantPossition);
            }
        }
    }

    private String posarFiltre(Map<String, Object> values, String url){
        /*
        Aquesta funció s'encarrega de crear-nos la url. El que fa es per cada entrada que hi hagi
        en el map ens afaga la key i el valor i ens el va afegint
         */
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
