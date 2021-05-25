package com.cursfundacionesplai.restasearch;

import android.media.Image;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cursfundacionesplai.restasearch.adapters.ReviewAdapter;
import com.cursfundacionesplai.restasearch.helpers.WSHelper;
import com.cursfundacionesplai.restasearch.interfaces.CustomResponse;
import com.cursfundacionesplai.restasearch.models.Photo;
import com.cursfundacionesplai.restasearch.models.RestaurantList;
import com.cursfundacionesplai.restasearch.models.RestaurantModel;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EstablimentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EstablimentFragment extends Fragment {

    // Elements de vista
    NestedScrollView visible;
    ConstraintLayout notVisible;

    ImageView photo;
    ImageView iconPrev;
    ImageView iconNext;
    TextView labelPhotoPos;

    TextView labelAddress;
    TextView labelPhoneNumber;
    TextView labelRating;
    TextView labelReviews;
    ImageView iconWebsite;
    TextView labelWebsite;

    ListView listHours;
    RecyclerView listReviews;

    private int currentPhotoPos;
    private ArrayList<Photo> photos;

    // variables
    private String placeId;

    public EstablimentFragment() {
        // Required empty public constructor
        currentPhotoPos = 0;
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

        FirebaseCrashlytics.getInstance().setUserId("RESTASEARCH_PROVA");
        FirebaseCrashlytics.getInstance().setCustomKey("RESTASEARCH_PROVA","S'ha produit un error a la classe EstablimentFragment");

        if (getArguments() != null) {
            placeId = getArguments().getString("placeId");
        }

        new WSHelper(getContext()).getEstablimentDetails(placeId, new CustomResponse.EstablimentDetail() {
            @Override
            public void onEstablimentResponse(RestaurantModel r) {
                if (r != null) {
                    labelAddress.setText(r.getFormatted_address());
                    labelPhoneNumber.setText(r.getInternational_phone_number());
                    labelRating.setText(getResources().getString(R.string.label_establiment_global_rating, r.getRating()));
                    labelReviews.setText(getResources().getString(R.string.label_establiment_total_reviews, r.getUser_ratings_total()));

                    // comprova si l'establiment conté una pàgina web
                    if (r.getWebsite() == null) {
                        iconWebsite.setVisibility(View.GONE);
                        labelWebsite.setVisibility(View.GONE);
                    } else {
                        labelWebsite.setText(r.getWebsite());
                    }

                    if (r.getOpening_hours().getWeekday_text() != null) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, r.getOpening_hours().getWeekday_text());
                        listHours.setAdapter(adapter);
                    }

                    photos = r.getPhotos();

                    if (photos.size() > 0) {
                        loadPhotoIntoContainer();
                    }

                    ReviewAdapter adapter = new ReviewAdapter(r.getReviews());

                    listReviews.setAdapter(adapter);
                    listReviews.setLayoutManager(new LinearLayoutManager(getContext()));

                    Log.d("miki", "rest id: " + r.getPlace_id());
                    Log.d("miki", "onEstablimentResponse: " + r.getWebsite());
                    Log.d("miki", "restaurant photos: " + r.getPhotos().size());

                    // amaga el contenidor del progress bar i mostrar el contenidor de les dades
                    notVisible.setVisibility(View.GONE);
                    visible.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void loadPhotoIntoContainer() {
        labelPhotoPos.setText(getResources().getString(R.string.establiment_photo_page, currentPhotoPos + 1, photos.size()));
        Picasso.get().load(photos.get(currentPhotoPos).buildUrl()).into(photo);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_establiment, container, false);

        visible = view.findViewById(R.id.scroll_loaded);
        notVisible = view.findViewById(R.id.constraint_not_loaded);

        photo = view.findViewById(R.id.rest_photo);
        iconPrev = view.findViewById(R.id.iconPrev);
        iconNext = view.findViewById(R.id.iconNext);
        labelPhotoPos = view.findViewById(R.id.label_photos_pages);

        iconPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPhotoPos--;

                if (currentPhotoPos == -1) {
                    currentPhotoPos = photos.size() - 1;
                }

                loadPhotoIntoContainer();
            }
        });

        iconNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPhotoPos++;

                if (currentPhotoPos == photos.size()) {
                    currentPhotoPos = 0;
                }

                loadPhotoIntoContainer();
            }
        });

        labelAddress = view.findViewById(R.id.label_address);
        labelPhoneNumber = view.findViewById(R.id.label_phone_number);
        labelRating = view.findViewById(R.id.label_rating);
        labelReviews = view.findViewById(R.id.label_reviews);

        iconWebsite = view.findViewById(R.id.icon_website);
        labelWebsite = view.findViewById(R.id.label_website);

        listHours = view.findViewById(R.id.list_hours);
        listReviews = view.findViewById(R.id.list_reviews);

        return view;
    }
}