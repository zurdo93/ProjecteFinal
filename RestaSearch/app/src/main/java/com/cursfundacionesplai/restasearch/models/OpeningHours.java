package com.cursfundacionesplai.restasearch.models;

public class OpeningHours {
    private boolean open_now;

    public OpeningHours(boolean openNow) {
        this.open_now = openNow;
    }

    public boolean isOpen_now() {
        return open_now;
    }

    public void setOpen_now(boolean open_now) {
        this.open_now = open_now;
    }
}
