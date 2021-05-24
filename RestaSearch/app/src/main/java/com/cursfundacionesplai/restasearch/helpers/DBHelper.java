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
import com.cursfundacionesplai.restasearch.models.Photo;
import com.cursfundacionesplai.restasearch.models.RestaurantModel;

import java.util.ArrayList;
import java.util.Arrays;

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

    public boolean insertRestaurantHistorial(RestaurantModel restaurant){
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

        return true;
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
