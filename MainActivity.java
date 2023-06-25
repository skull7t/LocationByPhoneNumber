package com.example.locatnacss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView lctn;
    TextView distn;
    Button gt_lcn;
    FusedLocationProviderClient fusedLocationProviderClient;
    private  final static int REQUEST_CODE =100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lctn = findViewById(R.id.lctn);
        gt_lcn = findViewById(R.id.gt_lcn);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        gt_lcn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getlastloc();
            }
        });
       // getlastloc();

    }


    ///
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    private void getlastloc() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
           fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
               @Override
               public void onSuccess(Location location) {
                   if(isLocationEnabled()){
                       Geocoder geocoder =new Geocoder(MainActivity.this, Locale.getDefault());
                       List<Address> address = null;
                       try {
                           address = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                           float dis =location.distanceTo(location);
                           String newt = dis +"\n"+"Latittue : " + address.get(0).getLatitude() +"\n"+ "Longitude : "+address.get(0).getLongitude() +"\n"+ "Address: "+ address.get(0).getAddressLine(0);
                           lctn.setText(newt);
                           dist(address.get(0).getLatitude(),address.get(0).getLongitude());
                       } catch (IOException e) {
                           throw new RuntimeException(e);
                       }
                   }
                   else {
                       // Location services disabled, prompt the user to enable them
                       showLocationEnableDialog();
                   }
               }
           });
        }
        else {
            askpermission();
        }
    }

    //If location is off
    private void showLocationEnableDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enable Location Services")
                .setMessage("Location services are required for this app to function. Please enable them in your device settings.")
                .setPositiveButton("Go to Settings", (dialog, which) -> {
                    // Redirect the user to the device's settings screen to enable location services
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Handle the cancellation (e.g., disable location functionality or show alternative UI)
                    Toast.makeText(this, "Location services disabled", Toast.LENGTH_SHORT).show();
                })
                .setCancelable(false)
                .show();
    }

    private void askpermission() {
        ActivityCompat.requestPermissions(MainActivity.this,new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
    }

    public void dist(double lat1,double long1){
        distn = findViewById(R.id.distn);
        Location startPoint=new Location("locationA");
        startPoint.setLatitude(19.176980555397);
        startPoint.setLongitude(72.96345715208562);

        Location endPoint=new Location("locationA");
        endPoint.setLatitude(lat1);
        endPoint.setLongitude(long1);

        double distance=startPoint.distanceTo(endPoint);
        String dist = String.valueOf(distance);
        distn.setText(dist);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getlastloc();
            } else {
               // showLocationEnableDialog();
                Toast.makeText(this, "Required Permission", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
