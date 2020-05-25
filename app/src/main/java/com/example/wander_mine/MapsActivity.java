//# COMP 4521    #  CHEN ZIYI       20319433          zchenbu@connect.ust.hk
//# COMP 4521    #  FENG ZIHAN      20412778          zfengae@ust.uk
package com.example.wander_mine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.StreetViewPanoramaOptions;
import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
//import com.google.android.gms.GoogleMap.OnMarkerClickListener;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    //   public class MapsActivity extends AppCompatActivity implements OnMarkerClickListener,
    //     OnMapReadyCallback {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private GoogleMap mMap;
    private static final String TAG = MapsActivity.class.getSimpleName();
    private Marker mMarker;
    private Context context;


    private void enableMyLocation(GoogleMap map) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }


    private void setMarkerClickListener(final GoogleMap map) {
        map.setOnMarkerClickListener(
                new OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Log.d(TAG, "onClick: Marker is clicked by user!");
                        try {
                            if (marker.isInfoWindowShown()) {
                                marker.hideInfoWindow();
                                return false;
                            } else {
                                Log.d(TAG, "onClick: infowindow is shown");
                                marker.showInfoWindow();
                                return true;
                            }
                        } catch (NullPointerException e) {
                            Log.e(TAG, "onClick: NullPointerException: " + e.getMessage());
                            return false;
                        }
                    }
                }
        );
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mapFragment).commit();
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Change the map type based on the user's selection.
        switch (item.getItemId()) {
            case R.id.normal_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.hybrid_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.satellite_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.terrain_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void setMarker(final GoogleMap map,
                          LatLng position,
                          String name,
                          String snippetText) {
        map.addMarker(new MarkerOptions()
                .position(position)
                .title(name)
                .snippet(snippetText));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // Check if location permissions are granted and if so enable the
        // location data layer.
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocation(mMap);
                    break;
                }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));

        String json = readJson();
        List<Attraction> list = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            //Attraction list = mapper.readValue(getFromAssets(), Attraction.class);
            list = mapper.readValue(json,new TypeReference<List<Attraction>>() { });
        }
        catch (Exception e){e.printStackTrace();}

        //hkust position
        LatLng home = new LatLng(22.2798, 114.1922);

        for(int i = 0; i < list.size(); i++) {
            Attraction tmp = list.get(i);
            LatLng helper = new LatLng(tmp.getLatLng()[0], tmp.getLatLng()[1]);
            setMarker(mMap, helper, tmp.getName(), tmp.getInfo());
        }
//        Marker demo_use = mMap.addMarker(new MarkerOptions()
//                .position(home)
//                .title("HKUST")
//                .snippet("中华路七号_www. baidu. com_37块钱成人票_早上七点到晚上七点"));
        // zoom level 15: street    10:city  20:buildings
        float zoom = 12;

        // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        //move camera to hkust
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, zoom));
        //setMapLongClick(mMap);
        //setPoiClick(mMap);
        enableMyLocation(mMap);
        //setInfoWindowClickToPanorama(mMap);

        try {
            // Customize the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.map_style));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
    }

    public String readJson(){
        try {
            InputStreamReader inputReader = new InputStreamReader( getResources().getAssets().open("data.json") );
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line="";
            String Result="";
            while((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


//    public void openWebsite(View view) {
//        TextView tvURL = (TextView) view.findViewById(R.id.URL);
//        String url = tvURL.getText().toString().split(" ")[1];
//        Log.i(TAG, "zfengae");
//        // Parse the URI and create the intent.
//        if(!url.equals("NA")) {
//            Uri webpage = Uri.parse(url);
//            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
//            // Find an activity to hand the intent and start that activity.
//            if (intent.resolveActivity(getPackageManager()) != null) {
//                startActivity(intent);
//            } else {
//                Log.d("ImplicitIntents", "Can't handle this intent!");
//            }
//        }
//    }
}
