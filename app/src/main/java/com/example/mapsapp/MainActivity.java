package com.example.mapsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.example.mapsapp.Permissions.PERMISSION_ALL;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback
{
    private static final int ERROR_DIALOGUE = 1111;
    private Context context = MainActivity.this;
    private GoogleMap mGoogleMap;
    private EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editTextSearch);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (Permissions.hasPermissions(context))
            {
                if (isServicesOk())
                {
                    showMap();
                } // if closed

            } //if closed
            else if (!Permissions.hasPermissions(context))
            {
                ActivityCompat.requestPermissions(this, Permissions.permissions, PERMISSION_ALL);
            }
        } // Build version if

    } // onCreate closed

    private void showMap()
    {

//        GoogleMapOptions options = new GoogleMapOptions();
//        options.zoomControlsEnabled(true);
//        SupportMapFragment supportMapFragment = SupportMapFragment.newInstance(options);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.main_container);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mGoogleMap  = googleMap;
        LatLng sydney = new LatLng(0, 0);
        goToLocation(sydney);
    }
    private void goToLocation(LatLng latLng)
    {
        mGoogleMap.addMarker(new MarkerOptions().position(latLng).title("Marker title"));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(true);
        mGoogleMap.setBuildingsEnabled(true);
    }
    public void searchLocation(View view)
    {
        hideSoftKeyBoard(view);
        String locationName = editText.getText().toString().trim();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());


        try
        {
           List<Address> addressList =  geocoder.getFromLocationName(locationName,3);
           if(addressList.size()>0)
           {
               Log.d("1111", "searchLocation: "+addressList);
                Address address = addressList.get(0);
               LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
               goToLocation(latLng);

           }
        } catch (IOException e)
        {
            e.printStackTrace();
        }


    } // searchLocation closed

    private void hideSoftKeyBoard(View view)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }//  hideSoftKeyboard closed

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch (requestCode)
        {
            case PERMISSION_ALL:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //granted
                } else
                {
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    } // onReQuestPermissionClosed

    private boolean isServicesOk()
    {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int result = googleApiAvailability.isGooglePlayServicesAvailable(context);
        if (result == ConnectionResult.SUCCESS)
        {
            return true;

        } else if (googleApiAvailability.isUserResolvableError(result))
        {
            Dialog dialog = googleApiAvailability.getErrorDialog(MainActivity.this, result, ERROR_DIALOGUE);
            dialog.show();
        } else
        {
            Toast.makeText(context, "Google Play Services are Required", Toast.LENGTH_SHORT).show();
        }
        return false;
    } // is ServicesOk closed




    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.map_menus,menu);

        return super.onCreateOptionsMenu(menu);
    } // onCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.map_menu_none:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;
            case R.id.map_menu_normal:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.map_menu_satellite:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.map_menu_terrain:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.map_menu_hybrid:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
        } // switch closed
        return super.onOptionsItemSelected(item);
    }
} // class closed