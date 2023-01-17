package com.naomi.nail_dash;

import com.google.android.gms.maps.model.LatLng;

public class ratingDetails {
    String address, uid;
    Double rating;
    LatLng latlng;


    public ratingDetails() {
    }
//    public ratingDetails(String uid,String address, Double rating, LatLng latlng) {
    public ratingDetails(Double rating) {
//        this.uid = uid;
//        this.address = address;
        this.rating = rating;
//        this.latlng = latlng;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }
}
