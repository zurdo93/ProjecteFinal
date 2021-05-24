package com.cursfundacionesplai.restasearch.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.cursfundacionesplai.restasearch.models.Restaurant;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creacio taula Restaurant
        db.execSQL("CREATE TABLE historial(" +
                "places_id INTEGER PRIMARY KEY NOT NULL," +
                "rating REAL NOT NULL," +
                "vicinity TEXT NOT NULL," +
                "price_level INTEGER NOT NULL," +
                "name TEXT NOT NULL," +
                "user_ratings_total INTEGER NOT NULL," +
                "photo_reference TEXT NOT NULL)");
        Log.d("RESTASEARCH", "S'ha creat la taula historial");

        db.execSQL("CREATE TABLE preferits(" +
                "places_id INTEGER PRIMARY KEY NOT NULL," +
                "rating REAL NOT NULL," +
                "vicinity TEXT NOT NULL," +
                "price_level INTEGER NOT NULL," +
                "name TEXT NOT NULL," +
                "user_ratings_total INTEGER NOT NULL," +
                "photo_reference TEXT NOT NULL)");
        Log.d("RESTASEARCH", "S'ha creat la taula preferits");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertRestaurantHistorial(Restaurant restaurant){
        Log.d("RESTASEARCH", "s'ha fet un insert a la taula historial");
        return true;
    }
}
