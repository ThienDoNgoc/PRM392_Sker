package com.example.group3_sker.Model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GeocodingResponse {
    @SerializedName("results")
    private List<Result> results;
    @SerializedName("status")
    private String status;

    public List<Result> getResults() {
        return results;
    }

    public String getStatus() {
        return status;
    }

    public static class Result {
        @SerializedName("geometry")
        private Geometry geometry;

        public Geometry getGeometry() {
            return geometry;
        }
    }

    public static class Geometry {
        @SerializedName("location")
        private Location location;

        public Location getLocation() {
            return location;
        }
    }

    public static class Location {
        @SerializedName("lat")
        private double lat;
        @SerializedName("lng")
        private double lng;

        public double getLat() {
            return lat;
        }

        public double getLng() {
            return lng;
        }
    }
}

