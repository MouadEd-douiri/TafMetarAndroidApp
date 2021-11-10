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

public class ListAdapter extends ArrayAdapter<String> {
    MySQLiteHelper db = new MySQLiteHelper(ListAdapter.super.getContext());
    boolean isOACI;
    public ListAdapter(Context context, List<String> oaci) {
        super(context, R.layout.list_item,oaci);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String oaci=getItem(position);
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }
        if (position % 2 == 0) {
            convertView.findViewById(R.id.layout).setBackgroundResource(R.drawable.item_bkg);}
        ImageView delete=convertView.findViewById(R.id.delete);
        ImageView edit=convertView.findViewById(R.id.edit);
        TextView title_item=convertView.findViewById(R.id.title_item);
        title_item.setText(oaci);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteAero(oaci);
                Toast.makeText(ListAdapter.super.getContext() , R.string.deleted, Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(ListAdapter.super.getContext(),MainActivity.class);
                ListAdapter.super.getContext().startActivity(intent1);
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder( ListAdapter.super.getContext());
                builder.setTitle(R.string.change_oaci);

                final EditText input = new EditText( ListAdapter.super.getContext());
                input.setText(oaci);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(input.getText().toString().trim().isEmpty()){
                            Toast.makeText(ListAdapter.super.getContext(), R.string.nadd, Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                        else if(input.getText().toString().trim().length()!=4){
                            Toast.makeText(ListAdapter.super.getContext(), R.string.Invalid_oaci, Toast.LENGTH_SHORT).show();
                            //dialog.cancel();
                        }
                        else if(db.IsIn(input.getText().toString().trim())){
                            Toast.makeText(ListAdapter.super.getContext(), R.string.Already_Existed, Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                        else{
                            db.updateAERO(oaci,input.getText().toString().trim());
                            Toast.makeText(ListAdapter.super.getContext(), R.string.edited, Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(ListAdapter.super.getContext(),MainActivity.class);
                            ListAdapter.super.getContext().startActivity(intent1);
                        }

                    }
                });

                builder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

            }
        });


   return convertView; }
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
                    MainActivity activity=new MainActivity();
                    activity.activity.runOnUiThread(new Runnable() {
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
                                                isOACI = false;
                                            } else {
                                                isOACI = true;}

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
}
