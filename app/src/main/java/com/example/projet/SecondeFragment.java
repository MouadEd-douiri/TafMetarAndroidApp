package com.example.projet;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SecondeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecondeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SecondeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SecondeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SecondeFragment newInstance(String param1, String param2) {
        SecondeFragment fragment = new SecondeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView=inflater.inflate(R.layout.fragment_seconde, container, false);
        TextView textView2 =myView.findViewById(R.id.textView2);
        TextView textView1=myView.findViewById(R.id.textView1);
        TextView textView3=myView.findViewById(R.id.textView3);
        ListView tafs_list=myView.findViewById(R.id.taf_list);
        ListView metars_list=myView.findViewById(R.id.metars_list);
        MapActivity temp=(MapActivity)getActivity();

        /*  TAF INFOS*/
        List<String> tafs = new ArrayList<>();
        tafs.add("ELevation:  "+temp.elevation_m);
        tafs.add("Latitude:  "+temp.latitude);
        tafs.add("Longitude:  "+temp.longitude);
        tafs.add("Issue Time:  "+temp.issue_time);
        tafs.add("Forecast period:  "+temp.fcst_time_to1);
        tafs.add("Forecast type:  "+temp.change_indicator1);
        tafs.add("Probability:  "+temp.fcst_time_from1);
        tafs.add("Visibility:  "+temp.visibility_statute_mi1);

        tafs.add("Forecast period:  "+temp.fcst_time_to2);
        tafs.add("Forecast type:  "+temp.change_indicator2);
        tafs.add("Probability:  "+temp.fcst_time_from2);
        tafs.add("Visibility:  "+temp.visibility_statute_mi2);
        

        ListAdapter_2 adapter = new ListAdapter_2(temp.getApplicationContext(),tafs);
        tafs_list.setAdapter(adapter);
        /*  TAF INFOS*/


        /*  METAR INFOS*/
        List<String> metars = new ArrayList<>();
        metars.add("Temperature:  "+temp.temperature);
        metars.add("Dewpoint:  "+temp.dewpoint);
        metars.add("Pressure:  "+temp.pressure);
        metars.add("Winds:  "+temp.winds);
        metars.add("Visibility:  "+temp.visibility);
        metars.add("Ceiling:  "+temp.ceiling);
        metars.add("Clouds:  "+temp.clouds);
        ListAdapter_2 adapter1 = new ListAdapter_2(temp.getApplicationContext(),metars);
        metars_list.setAdapter(adapter1);


        /*  METAR INFOS*/

        return myView;
    }
}