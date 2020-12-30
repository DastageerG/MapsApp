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
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import static com.example.mapsapp.Permissions.PERMISSION_ALL;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback
{
    private static final int ERROR_DIALOGUE = 1111;
    private Context context = MainActivity.this;
    private MapsFragment mapsFragment;
    MapView mapView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      //  mapsFragment = new MapsFragment();
        mapView = findViewById(R.id.mainActivityMapView);
        mapView.onCreate(savedInstanceState);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (Permissions.hasPermissions(context))
            {
                if (isServicesOk())
                {
                   // showFragment(mapsFragment);
                    mapView.getMapAsync(this);
                } // if closed

            } //if closed
            else if (!Permissions.hasPermissions(context))
            {
                ActivityCompat.requestPermissions(this, Permissions.permissions, PERMISSION_ALL);
            }
        } // Build version if

    } // onCreate closed

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
            googleMap.getMyLocation();
    } //onMapReady
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


//    private void showFragment(Fragment fragment)
//    {
//     FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.main_container,fragment);
//        fragmentTransaction.commit();
//    }


    @Override
    protected void onStart()
    {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState)
    {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mapView.onLowMemory();
    }
} // class closed