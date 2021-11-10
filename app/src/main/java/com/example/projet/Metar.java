package com.example.projet;

public class Metar {

        private String metarCode,temperature,dewpoint,pressure,winds,visibility,ceiling,clouds;

        public void Metar(){
            this.metarCode="";
            this.temperature="";
            this.dewpoint="";
            this.pressure="";
            this.winds="";
            this.visibility="";
            this.ceiling="";
            this.clouds="";
        }
        public String getTemperature() { return this.temperature; }
        public String getMetarCode() { return this.metarCode;  }
        public String getDewpoint() { return this.dewpoint; }
        public String getPressure() { return this.pressure; }
        public String getWinds() { return this.winds; }
        public String getVisibility() { return this.visibility; }
        public String getCeiling() { return this.ceiling; }
         public String getClouds() { return this.clouds; }

        public void setMetarCode(String abc) { this.metarCode=abc;}
        public void setTemperature(String abc) {this.temperature=abc; }
        public void setDewpoint(String abc) { this.dewpoint=abc; }
        public void setPressure(String abc) { this.pressure=abc; }
        public void setWinds(String abc) { this.winds=abc; }
        public void setVisibility(String abc) { this.visibility=abc; }
        public void setCeiling(String abc) { this.ceiling=abc; }
        public void setClouds(String abc) { this.clouds=abc; }

}
