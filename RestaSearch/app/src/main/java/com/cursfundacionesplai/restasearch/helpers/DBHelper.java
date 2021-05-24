package com.cursfundacionesplai.restasearch.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.cursfundacionesplai.restasearch.models.Keys;
import com.cursfundacionesplai.restasearch.models.Photo;
import com.cursfundacionesplai.restasearch.models.RestaurantList;
import com.cursfundacionesplai.restasearch.models.RestaurantModel;
import com.cursfundacionesplai.restasearch.models.Photo;
import com.cursfundacionesplai.restasearch.models.RestaurantModel;

import java.util.ArrayList;
import java.util.Arrays;

public class DBHelper extends SQLiteOpenHelper {
    SQLiteDatabase database;
    Cursor cursor;

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creacio taula Restaurant
        db.execSQL("CREATE TABLE historial(" +
                "places_id TEXT PRIMARY KEY," +
                "rating REAL," +
                "address TEXT," +
                "price_level INTEGER," +
                "name TEXT," +
                "user_ratings_total INTEGER)");
        Log.d("RESTASEARCH", "S'ha creat la taula historial");

        db.execSQL("CREATE TABLE favourites(" +
                "places_id TEXT PRIMARY KEY," +
                "rating REAL," +
                "address TEXT," +
                "price_level INTEGER," +
                "name TEXT," +
                "user_ratings_total INTEGER)");
        Log.d("RESTASEARCH", "S'ha creat la taula preferits");

        db.execSQL("CREATE TABLE photos(" +
                "places_id TEXT," +
                "height INTEGER," +
                "html_attributions TEXT," +
                "photo_reference TEXT," +
                "width INTEGER)");
        Log.d("RESTASEARCH", "S'ha creat la taula photos");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertRestaurantHistorial(RestaurantModel restaurant){
        Log.d("RESTASEARCH", "s'ha fet un insert a la taula historial");

        database = this.getReadableDatabase();

        //Todo: aqui hem de mirar si el restaurant que ens passen ja esta dins la base de dades.
        // Si es aixi, no l'haurem d'insertar.
        cursor = database.rawQuery("" +
                "SELECT * " +
                "FROM historial " +
                "WHERE places_id = '" + restaurant.getPlace_id() + "'", null);

        database = this.getWritableDatabase();

        try {
            if(!cursor.moveToFirst()){

                database.execSQL(
                        ("INSERT INTO historial(" +
                                "places_id, rating, " +
                                "address, price_level, " +
                                "name, user_ratings_total) " +
                                "VALUES(" +
                                ":places_id, :rating," +
                                ":address, :price_level," +
                                ":name, :user_ratings_total)"),
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
            cursor.close();
            database.close();
        }
        catch (Exception e){
            Log.d("NIL", "Error al llegir les dades del select. Error: " + e.getMessage());
        }

        return true;
    }

    public void insertRestaurantFavourites(String places_id){
        Log.d("RESTASEARCH", "s'ha fet un insert a la taula favourites");

        database = this.getWritableDatabase();
        cursor = database.rawQuery("" +
                "SELECT places_id, rating, address, price_level, name, user_ratings_total " +
                "FROM historial " +
                "WHERE places_id == '" + places_id + "'", null);

        double rating = 0;
        String address = "";
        int price_level = 0;
        String name = "";
        int user_ratings_total = 0;

        try {
            if(cursor.moveToFirst()){
                do{
                    rating = cursor.getDouble(1);
                    address = cursor.getString(2);
                    price_level = cursor.getInt(3);
                    name = cursor.getString(4);
                    user_ratings_total = cursor.getInt(5);
                    Log.d("NIL","Rating: " + rating + ", Address " + address + ", Price Level: " + price_level  + ", Name " + name + ", User ratings total " + user_ratings_total);
                }
                while (cursor.moveToNext());
            }
            else{
                Log.d("NIL", "No hi han dades a la base de dades");
            }
        }
        catch (Exception e){
            Log.d("NIL", "Error al llegir les dades del select. Error: " + e.getMessage());
        }


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
                        places_id,
                        rating,
                        address,
                        price_level,
                        name,
                        user_ratings_total
                });

        /*for (Photo photo : restaurant.getPhotos()){
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
        }*/
    }

    public ArrayList<RestaurantModel> getRestaurantsHistoric() {
        Log.d("miki", "getRestaurantsHistoric: ");

        ArrayList<RestaurantModel> list = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();

        String args[] = {Keys.DATABASE_LIMIT_HISTORIC};
        Cursor c = db.rawQuery("SELECT places_id, rating, address, price_level, name, user_ratings_total FROM historial LIMIT ?", args);

        if (c.moveToFirst()) {
            do {
                RestaurantModel model = new RestaurantModel();

                model.setPlace_id(c.getString(0));
                model.setRating(c.getDouble(1));
                model.setFormatted_address(c.getString(2));
                model.setPrice_level(c.getInt(3));
                model.setName(c.getString(4));
                model.setUser_ratings_total(c.getInt(5));

                list.add(model);
            } while (c.moveToNext());
        }

        return list;
    }
}
