package com.naomi.nail_dash;

public class Settings_Details {
    public String measurement,  landmark, mode;

    public Settings_Details() {
    }

    public Settings_Details(String measurement, String landmark, String mode) {
        this.measurement = measurement;
        this.landmark = landmark;
        this.mode = mode;
    }
    public Settings_Details(String measurement) {
        this.measurement = measurement;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public String getMode() { return mode; }

    public void setMode(String mode) { this.mode = mode; }
}

