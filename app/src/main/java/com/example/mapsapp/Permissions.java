package com.example.mapsapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

public class Permissions
{
    public static final int PERMISSION_ALL = 1;
    public static final String [] permissions =
            {
                    Manifest.permission.ACCESS_FINE_LOCATION
            };

    public static boolean hasPermissions(Context context)
    {
        if (context != null && permissions != null)
        {
            for (String permission : permissions)
            {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                {
                    return false;
                }
            }
        }
        return true;
    }


}
