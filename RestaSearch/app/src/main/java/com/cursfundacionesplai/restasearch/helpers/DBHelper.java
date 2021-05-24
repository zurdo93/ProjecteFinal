package com.cursfundacionesplai.restasearch.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.cursfundacionesplai.restasearch.models.Photo;
import com.cursfundacionesplai.restasearch.models.RestaurantModel;

public class DBHelper extends SQLiteOpenHelper {
    SQLiteDatabase database;

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creacio taula Restaurant
        db.execSQL("CREATE TABLE historial(" +
                "places_id INTEGER PRIMARY KEY NOT NULL UNIQUE," +
                "rating REAL NOT NULL," +
                "address TEXT NOT NULL," +
                "price_level INTEGER NOT NULL," +
                "name TEXT NOT NULL," +
                "user_ratings_total INTEGER NOT NULL)");
        Log.d("RESTASEARCH", "S'ha creat la taula historial");

        db.execSQL("CREATE TABLE favourites(" +
                "places_id INTEGER PRIMARY KEY NOT NULL UNIQUE," +
                "rating REAL NOT NULL," +
                "address TEXT NOT NULL," +
                "price_level INTEGER NOT NULL," +
                "name TEXT NOT NULL," +
                "user_ratings_total INTEGER NOT NULL)");
        Log.d("RESTASEARCH", "S'ha creat la taula preferits");

        db.execSQL("CREATE TABLE photos(" +
                "places_id INTEGER PRIMARY KEY NOT NULL," +
                "height INTEGER NOT NULL," +
                "html_attributions TEXT NOT NULL," +
                "photo_reference TEXT NOT NULL," +
                "width INTEGER NOT NULL)");
        Log.d("RESTASEARCH", "S'ha creat la taula photos");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertRestaurantHistorial(RestaurantModel restaurant){
        Log.d("RESTASEARCH", "s'ha fet un insert a la taula historial");

        database = this.getWritableDatabase();

        //Todo: aqui hem de mirar si el restaurant que ens passen ja esta dins la base de dades.
        // Si es aixi, no l'haurem d'insertar.

        database.execSQL(
                "INSERT INTO historial(" +
                        "places_id, rating, " +
                        "address, price_level, " +
                        "name, user_ratings_total) " +
                    "VALUES(" +
                        ":places_id, :rating," +
                        ":address, :price_level," +
                        ":name, :user_ratings_total)",
                new Object[]{
                    restaurant.getPlace_id(),
                    restaurant.getRating(),
                    restaurant.getFormatted_address(),
                    restaurant.getPrice_level(),
                    restaurant.getName(),
                    restaurant.getUser_ratings_total()
                });

        for (Photo photo : restaurant.getPhotos()){
            database.execSQL("" +
                    "INSERT INTO photos(" +
                        "places_id, height," +
                        "html_attributions," +
                        "photo_reference," +
                        "width)" +
                    "VALUES(" +
                        ":places_id, :height," +
                        ":html_attributions," +
                        ":photo_reference," +
                        ":width)",
                    new Object[]{
                        restaurant.getPlace_id(),
                        photo.getHeight(),
                        photo.getHtml_attributions().get(0),
                        photo.getPhoto_reference(),
                        photo.getWidth()
                    });
        }
    }

    public void insertRestaurantFavourites(RestaurantModel restaurant){
        Log.d("RESTASEARCH", "s'ha fet un insert a la taula favourites");

        database = this.getWritableDatabase();

        //Todo: aqui hem de mirar si el restaurant que ens passen ja esta dins la base de dades.
        // Si es aixi, no l'haurem d'insertar.

        database.execSQL(
                "INSERT INTO favourites(" +
                        "places_id, rating, " +
                        "address, price_level, " +
                        "name, user_ratings_total) " +
                        "VALUES(" +
                        ":places_id, :rating," +
                        ":address, :price_level," +
                        ":name, :user_ratings_total)",
                new Object[]{
                        restaurant.getPlace_id(),
                        restaurant.getRating(),
                        restaurant.getFormatted_address(),
                        restaurant.getPrice_level(),
                        restaurant.getName(),
                        restaurant.getUser_ratings_total()
                });

        for (Photo photo : restaurant.getPhotos()){
            database.execSQL("" +
                            "INSERT INTO photos(" +
                            "places_id, height," +
                            "html_attributions," +
                            "photo_reference," +
                            "width)" +
                            "VALUES(" +
                            ":places_id, :height," +
                            ":html_attributions," +
                            ":photo_reference," +
                            ":width)",
                    new Object[]{
                            restaurant.getPlace_id(),
                            photo.getHeight(),
                            photo.getHtml_attributions().get(0),
                            photo.getPhoto_reference(),
                            photo.getWidth()
                    });
        }
    }
}
