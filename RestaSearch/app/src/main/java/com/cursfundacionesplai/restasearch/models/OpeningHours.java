package com.cursfundacionesplai.restasearch.models;

public class OpeningHours {
    private boolean openNow;

    public OpeningHours(boolean openNow) {
        this.openNow = openNow;
    }

    public boolean isOpenNow() {
        return openNow;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }
}
