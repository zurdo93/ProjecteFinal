package com.cursfundacionesplai.restasearch.interfaces;

import com.cursfundacionesplai.restasearch.models.RestaurantList;

public interface CustomResponse {
    interface EstablimentDetail {
        void onResponse(RestaurantList r);
    }
}