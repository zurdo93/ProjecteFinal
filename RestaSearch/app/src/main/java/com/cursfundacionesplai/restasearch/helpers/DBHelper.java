package com.cursfundacionesplai.restasearch.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.cursfundacionesplai.restasearch.models.Keys;
import com.cursfundacionesplai.restasearch.models.Photo;
import com.cursfundacionesplai.restasearch.models.RestaurantModel;

import java.util.ArrayList;
import java.util.Arrays;

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

    public boolean insertRestaurantHistorial(RestaurantModel restaurant){
        Log.d("RESTASEARCH", "s'ha fet un insert a la taula historial");
        return true;
    }

    public ArrayList<RestaurantModel> getRestaurantsHistoric() {
        Log.d("miki", "getRestaurantsHistoric: ");

        ArrayList<RestaurantModel> list = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();

        String args[] = {Keys.DATABASE_LIMIT_HISTORIC};
        Cursor c = db.rawQuery("SELECT places_id, rating, vicinity, price_level, name, user_ratings_total, photo_reference FROM historial LIMIT ?", args);

        if (c.moveToFirst()) {
            do {
                RestaurantModel model = new RestaurantModel();
                model.setPlace_id(c.getString(0));
                model.setRating(c.getDouble(1));
                model.setFormatted_address(c.getString(2));
                model.setPrice_level(c.getInt(3));
                model.setName(c.getString(4));
                model.setUser_ratings_total(c.getInt(5));

                Photo photo = new Photo(1080, null, c.getString(6), 1920);

                model.setPhotos(new ArrayList<>(Arrays.asList(photo)));

                list.add(model);
            } while (c.moveToNext());
        }

        return list;
    }
}
