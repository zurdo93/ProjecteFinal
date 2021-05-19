package com.cursfundacionesplai.restasearch;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class EstablimentDialog extends DialogFragment {

    public static final String TAG = "establiment_dialog";

    // Elements de vista
    ConstraintLayout visible;
    ConstraintLayout notVisible;
    private Toolbar toolbar;

    TextView labelAddress;
    TextView labelPhoneNumber;
    TextView labelRating;
    TextView labelReviews;

    // variables
    private String placeId;

    public EstablimentDialog(String placeId) {
        this.placeId = placeId;
    }

    public static EstablimentDialog display(FragmentManager fragmentManager, String placeId) {
        EstablimentDialog dialog = new EstablimentDialog(placeId);
        dialog.show(fragmentManager, TAG);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();

        // si el dialeg no es null canviar la mida per que ocupi tota la pantalla
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);

            // aplicar animacions quan s'obre i es tancar el dialeg
            dialog.getWindow().setWindowAnimations(R.style.Theme_RestaSearch_Slide);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.establiment_dialog, container, false);

        visible = view.findViewById(R.id.constraint_loaded);
        notVisible = view.findViewById(R.id.constraint_not_loaded);
        toolbar = view.findViewById(R.id.toolbar);

        labelAddress = view.findViewById(R.id.label_address);
        labelPhoneNumber = view.findViewById(R.id.label_phone_number);
        labelRating = view.findViewById(R.id.label_rating);
        labelReviews = view.findViewById(R.id.label_reviews);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setTitle(placeId);
        /*toolbar.inflateMenu(R.menu.example_dialog);
        toolbar.setOnMenuItemClickListener(item -> {
            dismiss();
            return true;
        });*/

        loadEstablimentData(placeId);
    }

    public void loadEstablimentData(String placeId) {

        RequestQueue queue = Volley.newRequestQueue(getContext());

        String key = "AIzaSyAH53nRGennl8oBDVBPMx1AhhWO5Kb9Ohw";
        String fields = "business_status,opening_hours,review,photo,user_ratings_total,price_level,rating,formatted_address,international_phone_number,name";
        String url = "https://maps.googleapis.com/maps/api/place/details/json?place_id=" + placeId + "&fields=" + fields + "&language=es&key=" + key;

        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("OK")) {
                        JSONObject result = response.getJSONObject("result");
                        toolbar.setTitle(result.getString("name"));

                        labelAddress.setText(result.getString("formatted_address"));
                        labelPhoneNumber.setText(result.getString("international_phone_number"));
                        labelRating.setText(getResources().getString(R.string.label_establiment_global_rating, result.getString("rating")));
                        labelReviews.setText(getResources().getString(R.string.label_establiment_total_reviews, result.getString("user_ratings_total")));

                        // amaga el contenidor del progress bar i mostrar el contenidor de les dades
                        notVisible.setVisibility(View.GONE);
                        visible.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    Log.d("miki", "onErrorResponse: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("miki", "onErrorResponse: " + error.getMessage());
            }
        });

        queue.add(request);
    }
}
