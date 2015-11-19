package com.example.admin.ctu;

import android.app.ActionBar;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class map extends FragmentActivity implements LocationListener{
    private GoogleMap map;
    double latitude;
    double longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);
        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getMap();

        if (map != null) {
            addHeatMap();
        }
    }

    /**
     * This  method will add a heat map displaying road roughness on a google map along with the
     * markers of two different colors: Red markers display road bumps whereas green markers display
     * potholes.
     */
    public void addHeatMap() {
        List<LatLng> list = new ArrayList<LatLng>();
        HeatmapTileProvider provider;
        TileOverlay overlay;
        LatLng position = null;
        try
        {
            String json = null;
            InputStream is = getAssets().open("roughness.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);
            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                LatLng latLng = new LatLng(jsonObject.getDouble("gps_lat"), jsonObject.getDouble("gps_lng"));
                list.add(latLng);
            }
            position = new LatLng(jsonArray.getJSONObject(0).getDouble("gps_lat"),
                    jsonArray.getJSONObject(0).getDouble("gps_lng"));

            is = getAssets().open("bump.json");
            size = is.available();
            buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            jsonArray = new JSONArray(json);
            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                LatLng latLng = new LatLng(jsonObject.getDouble("gps_lat"), jsonObject.getDouble("gps_lng"));
                map.addMarker(new MarkerOptions().position(latLng));
            }
            is = getAssets().open("potholes.json");
            size = is.available();
            buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            jsonArray = new JSONArray(json);
            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                LatLng latLng = new LatLng(jsonObject.getDouble("gps_lat"), jsonObject.getDouble("gps_lng"));
                map.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        provider = new HeatmapTileProvider.Builder()
                .data(list)
                .build();
        provider.setRadius(10);
        overlay = map.addTileOverlay(new TileOverlayOptions().tileProvider(provider));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 12));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == android.R.id.home)
        {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude=location.getLatitude();
        longitude=location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}