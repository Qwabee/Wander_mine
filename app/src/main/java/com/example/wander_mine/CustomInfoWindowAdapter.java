//# COMP 4521    #  CHEN ZIYI       20319433          zchenbu@connect.ust.hk
//# COMP 4521    #  FENG ZIHAN      20412778          zfengae@ust.uk
package com.example.wander_mine;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private static final String TAG = CustomInfoWindowAdapter.class.getSimpleName();
    private final View mWindow;
    private Context mContext;

    private void rendowWindowText(Marker marker, View view){

        //marker title
        String title = marker.getTitle();
        TextView tvTitle = (TextView) view.findViewById(R.id.title);

        if(!title.equals("")){
            tvTitle.setText(title);
        }

        //Snippet  ="First String"+"_"+"Second String"
        // Snippet = address+url+price+open time
        String snippet = marker.getSnippet();
        String[] str=snippet.split("_");

        //Log.i(TAG, "davy " + str.length);
        TextView tvAddress = (TextView) view.findViewById(R.id.address);
         if(!str[0].equals("")){
             String tmp = "Address:" + str[0];
             tvAddress .setText(tmp);
        }

        TextView tvURL = (TextView) view.findViewById(R.id.URL);
        if(!str[1].equals("")){
            if(str[1].equals("NA")) {
                String tmp = "Website: " + str[1];
                tvURL.setText(tmp);
            }
            else {
                tvURL.setMovementMethod(LinkMovementMethod.getInstance());
                StringBuilder helper = new StringBuilder("Website: ");
                helper.append("<a href = \"https://");
                helper.append(str[1]);
                helper.append("\">");
                helper.append(str[1]);
                helper.append("</a>");
                tvURL.setText(Html.fromHtml(helper.toString()));
                Linkify.addLinks(tvURL,Linkify.WEB_URLS);
            }
//            String tmp = "Website: " + str[1];
//            tvURL.setText(tmp);
        }

        TextView tvPrice = (TextView) view.findViewById(R.id.Price);
        if(!str[2].equals("")){
            String tmp = "Ticket Price: " + str[2];
            tvPrice .setText(tmp);
        }

        TextView tvOpenTime = (TextView) view.findViewById(R.id.OpenTime);
        if(!str[3].equals("")){
            String tmp = "Opening Hours: " + str[3];
            tvOpenTime .setText(tmp);
        }


    }


    public CustomInfoWindowAdapter(Context context){
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);

    }

    @Override
    public View getInfoWindow(Marker marker){
        rendowWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker){
        rendowWindowText(marker, mWindow);
        return mWindow;
    }

}
