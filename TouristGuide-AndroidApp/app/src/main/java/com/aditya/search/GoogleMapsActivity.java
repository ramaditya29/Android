package com.aditya.search;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.List;
public class GoogleMapsActivity extends ActionBarActivity {
    GoogleMap gmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Intent intent1 = getIntent();
        double latitude = Double.parseDouble(intent1.getStringExtra("latitude"));
        double longitude = Double.parseDouble(intent1.getStringExtra("longitude"));
        String location = intent1.getStringExtra("location");
        gmap = ((MapFragment)getFragmentManager().findFragmentById(R.id.mapFragment)).getMap();

        gmap.setMyLocationEnabled(true);

        LatLng latlng = new LatLng(latitude,longitude);
        MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(location);
        gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,13));
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        gmap.getUiSettings().setZoomControlsEnabled(true);
        gmap.addMarker(marker);

    }
}
