package com.example.projet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity{

     ListView aeroports;
    private Button addbtn,search_btn;
    EditText srch;
    Boolean b=false;
    Boolean isOACI=false;
    Boolean a=false;
    ImageView add,delete;
    MySQLiteHelper db = new MySQLiteHelper(this);
    private String URLTAF="";
    ArrayList<Taf> Tafs=new ArrayList<>();
    ArrayList<Metar> Metars=new ArrayList<>();
    MainActivity activity;
    public ListAdapter adapter;
    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }
    public void setAdapter(ListAdapter adapter) {
        this.adapter=adapter;
    }

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aeroports = (ListView) findViewById(R.id.aeroports_list);
        srch=(EditText) findViewById(R.id.srch) ;
        activity=this;
        add=findViewById(R.id.add);
        delete=findViewById(R.id.delete);
        adapter = new ListAdapter(this, db.getAllOACI(db.getAllAEORO()));
        aeroports.setAdapter(adapter);

        srch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String ss=srch.getText().toString().trim();
                if(!db.searchAéoroport(ss).isEmpty()){
                   activity.adapter = new ListAdapter(MainActivity.this, db.getAllOACI(db.searchAéoroport(ss)));
                    aeroports.setAdapter(activity.adapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s=srch.getText().toString().trim();
                if(isNetworkAvailable()){
                    IsValideOACI(s);
                    final Handler handler1= new Handler();

                    handler1.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            if(s.isEmpty()){
                                Toast.makeText(MainActivity.this, R.string.nadd, Toast.LENGTH_SHORT).show();
                            }
                            else if(s.length()!=4){
                                Toast.makeText(MainActivity.this, R.string.Invalid_oaci, Toast.LENGTH_SHORT).show();
                            }
                            else if(db.IsIn(s)){
                                Toast.makeText(MainActivity.this, R.string.Already_Existed, Toast.LENGTH_SHORT).show();
                            }
                            else if(!isOACI){
                                Toast.makeText(MainActivity.this, R.string.Invalid_oaci, Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Aéoroport aero = new Aéoroport();
                                aero.setOACI(srch.getText().toString().trim());
                                db.addAéoroport(aero);
                                Toast.makeText(MainActivity.this, R.string.Added, Toast.LENGTH_SHORT).show();
                                activity.adapter = new ListAdapter(MainActivity.this, db.getAllOACI(db.getAllAEORO()));
                                aeroports.setAdapter(activity.adapter);
                                srch.setText("");
                            }
                        }

                    },1000);
                }

                else{
                    Toast.makeText(MainActivity.this, R.string.No_Internet_Connection, Toast.LENGTH_SHORT).show();
                }


            }
        });


        aeroports.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(isNetworkAvailable()){
                    parseXML(Tafs,position);
                    final Handler handler= new Handler();

                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            if ((!activity.b) && (!activity.a)) {
                                Toast.makeText(MainActivity.this, R.string.error_oaci_not_valid, Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent1 = new Intent(MainActivity.this, MapActivity.class);
                                /*  TAF INFOS*/
                                intent1.putExtra("OACI", activity.adapter.getItem(position));
                                intent1.putExtra("TafCode", Tafs.get(0).getTafCode());
                                intent1.putExtra("elevation_m", Tafs.get(0).getElevation_m());
                                intent1.putExtra("latitude", Tafs.get(0).getLatitude());
                                intent1.putExtra("longitude", Tafs.get(0).getLongitude());
                                intent1.putExtra("issue_time", Tafs.get(0).getIssue_time());
                                //forr
                                intent1.putExtra("change_indicator1", Tafs.get(0).getForecasts().get(1).change_indicator);
                                intent1.putExtra("fcst_time_from1", Tafs.get(0).getForecasts().get(1).probability);
                                intent1.putExtra("fcst_time_to1", Tafs.get(0).getForecasts().get(1).fcst_time_to);
                                intent1.putExtra("sky_condition1", Tafs.get(0).getForecasts().get(1).sky_condition);
                                intent1.putExtra("visibility_statute_mi1", Tafs.get(0).getForecasts().get(1).visibility_statute_mi);
                                intent1.putExtra("wind_dir_degrees1", Tafs.get(0).getForecasts().get(1).wind_dir_degrees);
                                intent1.putExtra("wx_string1", Tafs.get(0).getForecasts().get(1).wx_string);

                                //forr
                                intent1.putExtra("change_indicator2", Tafs.get(0).getForecasts().get(2).change_indicator);
                                intent1.putExtra("fcst_time_from2", Tafs.get(0).getForecasts().get(2).probability);
                                intent1.putExtra("fcst_time_to2", Tafs.get(0).getForecasts().get(2).fcst_time_to);
                                intent1.putExtra("sky_condition2", Tafs.get(0).getForecasts().get(2).sky_condition);
                                intent1.putExtra("visibility_statute_mi2", Tafs.get(0).getForecasts().get(2).visibility_statute_mi);
                                intent1.putExtra("wind_dir_degrees2", Tafs.get(0).getForecasts().get(2).wind_dir_degrees);
                                intent1.putExtra("wx_string2", Tafs.get(0).getForecasts().get(2).wx_string);

                                //forr
                                intent1.putExtra("change_indicator3", Tafs.get(0).getForecasts().get(3).change_indicator);
                                intent1.putExtra("fcst_time_from3", Tafs.get(0).getForecasts().get(3).probability);
                                intent1.putExtra("fcst_time_to3", Tafs.get(0).getForecasts().get(3).fcst_time_to);
                                intent1.putExtra("sky_condition3", Tafs.get(0).getForecasts().get(3).sky_condition);
                                intent1.putExtra("visibility_statute_mi3", Tafs.get(0).getForecasts().get(3).visibility_statute_mi);
                                intent1.putExtra("wind_dir_degrees3", Tafs.get(0).getForecasts().get(3).wind_dir_degrees);
                                intent1.putExtra("wx_string3", Tafs.get(0).getForecasts().get(3).wx_string);



                                /*  TAF INFOS*/

                                /*  METAR INFOS*/
                                intent1.putExtra("MetarCode", Metars.get(0).getMetarCode());
                                intent1.putExtra("temperature", Metars.get(0).getTemperature());
                                intent1.putExtra("winds", Metars.get(0).getWinds());
                                intent1.putExtra("visibility", Metars.get(0).getVisibility());
                                intent1.putExtra("ceiling", Metars.get(0).getCeiling());
                                intent1.putExtra("dewpoint", Metars.get(0).getDewpoint());
                                intent1.putExtra("clouds", Metars.get(0).getClouds());
                                intent1.putExtra("pressure", Metars.get(0).getPressure());
                                /*  METAR INFOS*/

                                startActivity(intent1);
                            }
                        }

                    },1000);
                }

                else{
                    Toast.makeText(MainActivity.this, R.string.No_Internet_Connection, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    void  parseXML ( ArrayList<Taf> tafss, int position) {
        OkHttpClient client = new OkHttpClient();
        OkHttpClient client2 = new OkHttpClient();
        String url="https://www.aviationweather.gov/adds/dataserver_current/" +
                "httpparam?dataSource=tafs&requestType=retrieve&format=xml&" +
                "stationString="+activity.adapter.getItem(position)+ "&hoursBeforeNow=4";
        String url2="https://www.aviationweather.gov/adds/dataserver_current/" +
                "httpparam?dataSource=metars&requestType=retrieve&format=xml&" +
                "stationString="+activity.adapter.getItem(position)+ "&hoursBeforeNow=1";
        Request request = new Request.Builder()
                .url(url)
                .build();
        Request request2 = new Request.Builder()
                .url(url2)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myresponse = response.body().string();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            XmlPullParserFactory parserFactory;
                            try {

                                parserFactory = XmlPullParserFactory.newInstance();
                                XmlPullParser parser = parserFactory.newPullParser();
                                InputStream is = new ByteArrayInputStream(myresponse.getBytes(StandardCharsets.UTF_8));
                                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                                parser.setInput(is, null);
                                int eventType = parser.getEventType();
                                Taf currentTaf = null;
                                forecast currentforcast=null;
                                ArrayList<forecast> forecasts=new ArrayList<>();
                                while (eventType != XmlPullParser.END_DOCUMENT) {
                                    String eltName = null;

                                    switch (eventType) {
                                        case XmlPullParser.START_TAG:
                                            eltName = parser.getName();
                                            if ("data".equals(eltName) && "0".equals(parser.getAttributeValue(null, "num_results"))) {
                                                activity.b = false;
                                            } else {
                                                activity.b = true;
                                                if ("TAF".equals(eltName)) {
                                                    currentTaf = new Taf();
                                                    tafss.add(currentTaf);
                                                } else if (currentTaf != null) {
                                                    if ("raw_text".equals(eltName)) {
                                                        currentTaf.setTafCode(parser.nextText());
                                                    } else if ("elevation_m".equals(eltName)) {
                                                        currentTaf.setElevation_m(parser.nextText());
                                                    } else if ("longitude".equals(eltName)) {
                                                        currentTaf.setLongitude(parser.nextText());

                                                    } else if ("latitude".equals(eltName)) {
                                                        currentTaf.setLatitude(parser.nextText());

                                                    } else if ("issue_time".equals(eltName)) {
                                                        currentTaf.setIssue_time(parser.nextText());
                                                    }
                                                    else if ("forecast".equals(eltName)) {
                                                        currentforcast= new forecast();
                                                        forecasts.add(currentforcast);
                                                    }
                                                    else if ("probability".equals(eltName)) {
                                                        currentforcast.setProbability(parser.nextText());
                                                    }
                                                    else if ("fcst_time_to".equals(eltName)) {
                                                        currentforcast.setFcst_time_to(parser.nextText());
                                                    }
                                                    else if ("wind_dir_degrees".equals(eltName)) {
                                                        currentforcast.setWind_dir_degrees(parser.nextText());
                                                    }
                                                    else if ("wx_string".equals(eltName)) {
                                                        currentforcast.setWx_string(parser.nextText());
                                                    }
                                                    else if ("visibility_statute_mi".equals(eltName)) {
                                                        currentforcast.setVisibility_statute_mi(parser.nextText());
                                                    }
                                                    else if ("change_indicator".equals(eltName)) {
                                                        currentforcast.setChange_indicator(parser.nextText());
                                                    }


                                                    currentTaf.setForecasts(forecasts);
                                                }

                                                break;
                                            }
                                    }

                                    eventType = parser.next();
                                }
                            } catch (XmlPullParserException e) {
                                Toast.makeText(MainActivity.this, R.string.error_oaci_not_valid, Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            } catch (IOException e) {
                                Toast.makeText(MainActivity.this, R.string.error_oaci_not_valid, Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }

                    });
                }
            }
        });

        client2.newCall(request2).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myresponse2 = response.body().string();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            XmlPullParserFactory parserFactory;
                            try {

                                parserFactory = XmlPullParserFactory.newInstance();
                                XmlPullParser parser = parserFactory.newPullParser();
                                InputStream is = new ByteArrayInputStream(myresponse2.getBytes(StandardCharsets.UTF_8));
                                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                                parser.setInput(is, null);
                                int eventType = parser.getEventType();
                                Metar currentMetars = null;
                                while (eventType != XmlPullParser.END_DOCUMENT) {
                                    String eltName = null;

                                    switch (eventType) {
                                        case XmlPullParser.START_TAG:
                                            eltName = parser.getName();
                                            if ("data".equals(eltName) && "0".equals(parser.getAttributeValue(null, "num_results"))) {
                                                activity.a = false;
                                            } else {
                                                activity.a = true;
                                                if ("METAR".equals(eltName)) {
                                                    currentMetars = new Metar();
                                                    Metars.add(currentMetars);
                                                } else if (currentMetars != null) {
                                                    if ("raw_text".equals(eltName)) {
                                                        currentMetars.setMetarCode(parser.nextText());
                                                    } else if ("temp_c".equals(eltName)) {
                                                        currentMetars.setTemperature(parser.nextText()+" ºC");
                                                    } else if ("visibility_statute_mi".equals(eltName)) {
                                                        currentMetars.setVisibility(parser.nextText());
                                                    } else if ("dewpoint_c".equals(eltName)) {
                                                        currentMetars.setDewpoint(parser.nextText()+" ºC");

                                                    } else if ("altim_in_hg".equals(eltName)) {
                                                        currentMetars.setPressure(parser.nextText()+" inches Hg");

                                                    } else if ("wind_dir_degrees".equals(eltName)) {
                                                        currentMetars.setWinds("from the SSW "+parser.nextText());
                                                    }else if ("sky_condition".equals(eltName)) {
                                                    currentMetars.setCeiling(parser.getAttributeValue(null, "cloud_base_ft_agl")+" feet AGL");
                                                    currentMetars.setClouds("broken clouds at  "+parser.getAttributeValue(null, "cloud_base_ft_agl")+" feet AGL");
                                                    }
                                                }

                                                break;
                                            }
                                    }

                                    eventType = parser.next();
                                }
                            } catch (XmlPullParserException e) {
                                Toast.makeText(MainActivity.this, R.string.error_oaci_not_valid, Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            } catch (IOException e) {
                                Toast.makeText(MainActivity.this, R.string.error_oaci_not_valid, Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }

                    });
                }
            }
        });
    }
    public void IsValideOACI(String oaci){
        OkHttpClient client2 = new OkHttpClient();
        String url2="https://www.aviationweather.gov/adds/dataserver_current/" +
                "httpparam?dataSource=metars&requestType=retrieve&format=xml&" +
                "stationString="+oaci+ "&hoursBeforeNow=1";
        Request request2 = new Request.Builder()
                .url(url2)
                .build();
        client2.newCall(request2).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myresponse = response.body().string();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            XmlPullParserFactory parserFactory;
                            try {

                                parserFactory = XmlPullParserFactory.newInstance();
                                XmlPullParser parser = parserFactory.newPullParser();
                                InputStream is = new ByteArrayInputStream(myresponse.getBytes(StandardCharsets.UTF_8));
                                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                                parser.setInput(is, null);
                                int eventType = parser.getEventType();
                                while (eventType != XmlPullParser.END_DOCUMENT) {
                                    String eltName = null;

                                    switch (eventType) {
                                        case XmlPullParser.START_TAG:
                                            eltName = parser.getName();
                                            if ("data".equals(eltName) && "0".equals(parser.getAttributeValue(null, "num_results"))) {
                                                activity.isOACI = false;
                                            } else {
                                                activity.isOACI = true;}

                                    break;
                                }
                                    eventType = parser.next();
                            }


                        } catch (XmlPullParserException xmlPullParserException) {
                                xmlPullParserException.printStackTrace();
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
    }});}}});}
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}