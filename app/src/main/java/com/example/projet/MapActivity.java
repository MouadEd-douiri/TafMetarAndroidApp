package com.example.projet;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Xml;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapActivity extends AppCompatActivity {

    ViewPager2 pager2;
    FragementAdapter adapter;
    TabLayout tabLayout;
    String OACI;
    String fcst_time_from1,fcst_time_to1,wind_dir_degrees1,visibility_statute_mi1,wx_string1,change_indicator1,sky_condition1;
    String fcst_time_from2,fcst_time_to2,wind_dir_degrees2,visibility_statute_mi2,wx_string2,change_indicator2,sky_condition2;
    String fcst_time_from3,fcst_time_to3,wind_dir_degrees3,visibility_statute_mi3,wx_string3,change_indicator3,sky_condition3;
    String TafCode, elevation_m, latitude,longitude,issue_time;
    String MetarCode,pressure,dewpoint,ceiling,visibility,winds,temperature,clouds;
    ArrayList<Taf> tafs=new ArrayList<Taf>();

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        tabLayout = findViewById(R.id.tab_layout);
        pager2 = findViewById(R.id.view_pager2);


        FragmentManager fm = getSupportFragmentManager();
        adapter = new FragementAdapter(fm, getLifecycle());
        pager2.setAdapter(adapter);

        tabLayout.addTab(tabLayout.newTab().setText(R.string.Coded));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.Decoded));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.In_Map));


        tabLayout.getTabAt(0).setIcon(R.drawable.lock);
        tabLayout.getTabAt(1).setIcon(R.drawable.lock_1);
        tabLayout.getTabAt(2).setIcon(R.drawable.map);
       


        Intent intent= getIntent();
        /*  METAR INFOS*/
        OACI=intent.getStringExtra("OACI");
        MetarCode=intent.getStringExtra("MetarCode");
        temperature=intent.getStringExtra("temperature");
        winds=intent.getStringExtra("winds");
        visibility=intent.getStringExtra("visibility");
        ceiling=intent.getStringExtra("ceiling");
        dewpoint=intent.getStringExtra("dewpoint");
        clouds=intent.getStringExtra("clouds");
        pressure=intent.getStringExtra("pressure");
        /*  METAR INFOS*/

        /*  TAF INFOS*/
        TafCode=intent.getStringExtra("TafCode");
        elevation_m=intent.getStringExtra("elevation_m");
        latitude=intent.getStringExtra("latitude");
        longitude=intent.getStringExtra("longitude");
        issue_time=intent.getStringExtra("issue_time");
        ////
        change_indicator1=intent.getStringExtra("change_indicator1");
        wx_string1=intent.getStringExtra("wx_string1");
        wind_dir_degrees1=intent.getStringExtra("wind_dir_degrees1");
        visibility_statute_mi1=intent.getStringExtra("visibility_statute_mi1");
        sky_condition1=intent.getStringExtra("sky_condition1");
        fcst_time_to1=intent.getStringExtra("fcst_time_to1");
        fcst_time_from1=intent.getStringExtra("fcst_time_from1");
        ////
        change_indicator2=intent.getStringExtra("change_indicator2");
        wx_string2=intent.getStringExtra("wx_string2");
        wind_dir_degrees2=intent.getStringExtra("wind_dir_degrees2");
        visibility_statute_mi2=intent.getStringExtra("visibility_statute_mi2");
        sky_condition2=intent.getStringExtra("sky_condition2");
        fcst_time_to2=intent.getStringExtra("fcst_time_to2");
        fcst_time_from2=intent.getStringExtra("fcst_time_from2");
        ////
        change_indicator3=intent.getStringExtra("change_indicator3");
        wx_string3=intent.getStringExtra("wx_string3");
        wind_dir_degrees3=intent.getStringExtra("wind_dir_degrees3");
        visibility_statute_mi3=intent.getStringExtra("visibility_statute_mi3");
        sky_condition3=intent.getStringExtra("wind_dir_degrees3");
        fcst_time_to3=intent.getStringExtra("fcst_time_to3");
        fcst_time_from3=intent.getStringExtra("fcst_time_from3");


        /*  TAF INFOS*/


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }


        });

        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }

}

