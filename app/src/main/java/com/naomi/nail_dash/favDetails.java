package com.naomi.nail_dash;

import com.google.android.gms.maps.model.LatLng;

public class favDetails {
    LatLng latlng;
    String favAddress, favName;

    public favDetails() {
    }


    public favDetails(LatLng latlng, String favAddress, String favName) {
        this.latlng = latlng;
        this.favAddress = favAddress;
        this.favName = favName;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }

    public String getFavAddress() {
        return favAddress;
    }

    public void setFavAddress(String favAddress) {
        this.favAddress = favAddress;
    }

    public String getFavName() {
        return favName;
    }

    public void setFavName(String favName) {
        this.favName = favName;
    }
}
