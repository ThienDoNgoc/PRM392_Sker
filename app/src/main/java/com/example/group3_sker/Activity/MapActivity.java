package com.example.group3_sker.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.group3_sker.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng storeLocation;
    private ImageView backBtn;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        address = sharedPreferences.getString("ADDRESS", "User");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        Button directionsButton = findViewById(R.id.btn_directions);
        directionsButton.setOnClickListener(v -> getDirections());

        // Geocode the store location
        geocodeStoreLocation("Dai hoc FPT Ho Chi Minh");

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MapActivity.this, CartActivity.class);
            startActivity(intent);
        });
    }

    private void geocodeStoreLocation(String address) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address location = addresses.get(0);
                storeLocation = new LatLng(location.getLatitude(), location.getLongitude());
            } else {
                Toast.makeText(this, "Store location not found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Geocoding failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        // Set map type
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        if (storeLocation != null) {
            mMap.addMarker(new MarkerOptions().position(storeLocation).title("Sker Store Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(storeLocation, 15));
        }
    }

    private void getDirections() {

        String userAddress = address ;

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(userAddress, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address location = addresses.get(0);
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

                if (storeLocation != null) {
                    Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=" + userLocation.latitude + "," + userLocation.longitude + "&destination=" + storeLocation.latitude + "," + storeLocation.longitude + "&travelmode=driving");
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(mapIntent);
                    }
                } else {
                    Toast.makeText(this, "Store location is not set", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "User address not found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Geocoding user address failed", Toast.LENGTH_SHORT).show();
        }
    }
}

