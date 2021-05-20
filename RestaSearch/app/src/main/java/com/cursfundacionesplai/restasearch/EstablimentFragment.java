package com.cursfundacionesplai.restasearch;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cursfundacionesplai.restasearch.helpers.WSHelper;
import com.cursfundacionesplai.restasearch.interfaces.CustomResponse;
import com.cursfundacionesplai.restasearch.models.Restaurant;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EstablimentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EstablimentFragment extends Fragment {

    // Elements de vista
    ConstraintLayout visible;
    ConstraintLayout notVisible;

    TextView labelAddress;
    TextView labelPhoneNumber;
    TextView labelRating;
    TextView labelReviews;

    // variables
    private String placeId;

    public EstablimentFragment() {
        // Required empty public constructor
    }

    public static EstablimentFragment newInstance(String placeId) {
        EstablimentFragment fragment = new EstablimentFragment();
        Bundle args = new Bundle();
        args.putString("placeId", placeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            placeId = getArguments().getString("placeId");
        }

        new WSHelper(getContext()).getEstablimentDetails(placeId, new CustomResponse.EstablimentDetail() {
            @Override
            public void onResponse(Restaurant r) {
                if (r != null) {
                    labelAddress.setText(r.getFormattedAdress());
                    labelPhoneNumber.setText(r.getInternationalPhoneNumber());
                    labelRating.setText(getResources().getString(R.string.label_establiment_global_rating, r.getRating()));
                    labelReviews.setText(getResources().getString(R.string.label_establiment_total_reviews, r.getUser_ratings_total()));

                    // amaga el contenidor del progress bar i mostrar el contenidor de les dades
                    notVisible.setVisibility(View.GONE);
                    visible.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_establiment, container, false);

        visible = view.findViewById(R.id.constraint_loaded);
        notVisible = view.findViewById(R.id.constraint_not_loaded);

        labelAddress = view.findViewById(R.id.label_address);
        labelPhoneNumber = view.findViewById(R.id.label_phone_number);
        labelRating = view.findViewById(R.id.label_rating);
        labelReviews = view.findViewById(R.id.label_reviews);

        return view;
    }
}