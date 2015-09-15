package com.example.admin.ctu;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class routeOnMap extends FragmentActivity implements LocationListener {
    String route_no;
    private GoogleMap map;
    double latitude;
    double longitude;
    ArrayList coordinates = new ArrayList();
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_on_map);
        count = 0;
        Intent intent = getIntent();
        route_no = intent.getStringExtra("route");
        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);
        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getMap();

        if (map != null) {
            database d = new database(getBaseContext());
            d.open();
           coordinates = d.getCoordinates(route_no);
            LatLng position = new LatLng(Double.valueOf(coordinates.get(count).toString()),
                    Double.valueOf(coordinates.get(count+1).toString()));
            while(count < coordinates.size())
            {
                latitude = Double.valueOf(coordinates.get(count).toString());
                longitude = Double.parseDouble(coordinates.get(count + 1).toString());
                map.setMyLocationEnabled(true);
                position = new LatLng(latitude, longitude);
                map.addMarker(new MarkerOptions().position(position).title(coordinates.get(count + 2).toString()));
                count = count + 3;
            }
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(position ,12));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_route_on_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {
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
