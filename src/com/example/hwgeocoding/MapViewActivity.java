package com.example.hwgeocoding;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapViewActivity extends Activity {
	
	private GoogleMap mMap;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        
        double lng = getIntent().getExtras().getDouble("lng");
        double lat = getIntent().getExtras().getDouble("lat");
        String address = getIntent().getExtras().getString("address");
        
        mMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
        LatLng latlng = new LatLng(lat, lng);
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 13));
        mMap.addMarker(new MarkerOptions().position(latlng).title(address));
    }
}
