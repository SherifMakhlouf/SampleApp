package com.sherifmakhlouf.goeuro.api.model;


public class Location implements Comparable<Location>{

    public static final class GeoPosition {
        public double latitude;
        public double longitude;
        public double distance;
    }

    public int _id;
    public String key;
    public String name;
    public String fullName;
    public String iata_airport_code;
    public String type;
    public String country;
    public GeoPosition geo_position;
    public int locationId;
    public boolean inEurope;
    public String countryCode;
    public boolean coreCountry;
    public double distance;

    /**
     * To sort Locations by distance from the user's Location
     */
    @Override
    public int compareTo(Location another) {
        return (int)(this.geo_position.distance - another.geo_position.distance);
    }

    @Override
    public String toString() {
        return  this.fullName;
    }
}
