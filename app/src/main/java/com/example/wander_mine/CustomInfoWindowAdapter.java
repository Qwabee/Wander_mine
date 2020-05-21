package com.example.wander_mine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
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
        final String[] str=snippet.split("_");

        TextView tvAddress = (TextView) view.findViewById(R.id.address);
         if(!str[0].euqals("")){
             tvAddress .setText(str[0]);
        }

        TextView tvURL = (TextView) view.findViewById(R.id.URL);
        if(!str[1].euqals("")){
            tvURL .setText(str[1]);
        }

        TextView tvPrice = (TextView) view.findViewById(R.id.Price);
        if(!str[2].euqals("")){
            tvPrice .setText(str[2]);
        }

        TextView tvOpenTime = (TextView) view.findViewById(R.id.OpenTime);
        if(!str[3].euqals("")){
            tvOpenTime .setText(str[3]);
        }


    }


    public CustomInfoWindowAdapter(Context context){
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);

    }

    @Override
    public View getInfoWindow(Marker marker){
        return null;
    }

    @Override
    public View getInfoContents(Marker marker){
        return null;
    }
}
