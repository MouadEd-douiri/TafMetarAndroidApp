package com.example.projet;

public class forecast {
   String probability,fcst_time_to,wind_dir_degrees,wx_string,visibility_statute_mi,sky_condition,change_indicator;

    public String getChange_indicator() {
        return change_indicator;
    }



    public String getFcst_time_to() {
        return fcst_time_to;
    }

    public void setChange_indicator(String change_indicator) {
        this.change_indicator = change_indicator;
    }

    public String getWx_string() {
        return wx_string;
    }

    public void setWx_string(String wx_string) {
        this.wx_string = wx_string;
    }

    public String getWind_dir_degrees() {
        return wind_dir_degrees;
    }


    public String getVisibility_statute_mi() {
        return visibility_statute_mi;
    }

    public String getSky_condition() {
        return sky_condition;
    }

    public String getProbability() {
        return probability;
    }

    public void setProbability(String probability) {
        this.probability = probability;
    }

    public void setFcst_time_to(String fcst_time_to) {
        this.fcst_time_to = fcst_time_to;
    }

    public void setWind_dir_degrees(String wind_dir_degrees) {
        this.wind_dir_degrees = wind_dir_degrees;
    }


    public void setVisibility_statute_mi(String visibility_statute_mi) {
        this.visibility_statute_mi = visibility_statute_mi;
    }

    public void setSky_condition(String sky_condition) {
        this.sky_condition = sky_condition;
    }
}
