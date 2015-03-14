package com.sherifmakhlouf.goeuro.api.model;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.sherifmakhlouf.goeuro.GoEuroApp;

import java.lang.reflect.Type;

public class GeoPositionTypeAdapter implements JsonDeserializer<Location.GeoPosition> {

    private GoEuroApp context;

    public GeoPositionTypeAdapter(GoEuroApp context) {
        this.context = context;
    }

    @Override
    public Location.GeoPosition deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        Location.GeoPosition temp = new Location.GeoPosition();
        JsonObject jsonObject = json.getAsJsonObject();
        temp.latitude = jsonObject.get("latitude").getAsDouble();
        temp.longitude = jsonObject.get("longitude").getAsDouble();
        temp.distance = calculateDistance(getLastLatitiude(), getLastLongtiude(), temp.latitude, temp.longitude);
        return temp;
    }

    private double getLastLatitiude() {
        SharedPreferences prefs = context.getSharedPreferences("com.goeuro.sharedprefs", Context.MODE_PRIVATE);
        return Double.longBitsToDouble(prefs.getLong("Latitude", 0));
    }

    private double getLastLongtiude() {
        SharedPreferences prefs = context.getSharedPreferences("com.goeuro.sharedprefs", Context.MODE_PRIVATE);
        return Double.longBitsToDouble(prefs.getLong("Longitude", 0));
    }

    /**
     * Calculates the distance between two locations
     *
     * @param lat1 latitude of the first location
     * @param lng1 longitude of the first location
     * @param lat2 latitude of the second location
     * @param lng2 longitude of the second location
     * @return distance
     */
    private double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        double latDif = (lat2 - lat1);
        double lngDif = (lng2 - lng1);
        return Math.abs(Math.sqrt(latDif * latDif + lngDif * lngDif));
    }

}