package com.cursfundacionesplai.restasearch.interfaces;

import com.cursfundacionesplai.restasearch.models.Restaurant;

public interface CustomResponse {
    interface EstablimentDetail {
        void onResponse(Restaurant r);
    }
}