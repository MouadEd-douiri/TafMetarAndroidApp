package com.example.projet;

import java.util.ArrayList;

public class Taf {
    private String tafCode, elevation_m, longitude,latitude,issue_time,bulletin_time;
    private ArrayList<forecast> forecasts;
public void Taf(){
    this.tafCode="";
    this.elevation_m="";
    this.longitude="";
    this.latitude="";
    this.issue_time="";
    forecasts=null;

}
    public String getTafCode() {
        return this.tafCode;
    }
    public String getElevation_m() {
        return this.elevation_m;
    }
    public String getLongitude() {
        return this.longitude;
    }
    public String getLatitude() {
        return this.latitude;
    }
    public String getIssue_time() { return this.issue_time; }

    public void setBulletin_time(String bulletin_time) {
        this.bulletin_time = bulletin_time;
    }

    public void setForecasts(ArrayList<forecast> forecasts) {
        this.forecasts = forecasts;
    }

    public String getBulletin_time() {
        return bulletin_time;
    }

    public ArrayList<forecast> getForecasts() {
        return forecasts;
    }

    public void setTafCode(String tafCode) {
        this.tafCode = tafCode;
    }
    public void setElevation_m(String elevation_m) {
        this.elevation_m = elevation_m;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public void setIssue_time(String issue_time) {
        this.issue_time = issue_time;
    }
}
