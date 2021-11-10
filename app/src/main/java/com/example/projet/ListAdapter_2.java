package com.example.projet;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class ListAdapter_2 extends ArrayAdapter<String> {
    MySQLiteHelper db = new MySQLiteHelper(ListAdapter_2.super.getContext());
    public ListAdapter_2(Context context, List<String> s) {

        super(context, R.layout.list_item_2,s);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String oaci=getItem(position);
        if(convertView==null){
            convertView= LayoutInflater.from(ListAdapter_2.super.getContext()).inflate(R.layout.list_item_2,parent,false);
        }
        if (position % 2 == 0) {
            convertView.findViewById(R.id.layou).setBackgroundResource(R.drawable.back2_1); }

        TextView title_item=convertView.findViewById(R.id.title_item);
        title_item.setText(oaci);
        return convertView; }
}