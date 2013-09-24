package com.example.context_provider;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 24.09.13
 * Time: 22:08
 * To change this template use File | Settings | File Templates.
 */
public class HttpHelpers {

    private static Context _context;

    public static void initialize(Context context){
        _context = context;
    }

    public static boolean isInternetAvailable(){

        ConnectivityManager cm = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null)
            return false;

        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isAvailable() && ni.isConnected())
            return true;

        return false;
    }
}
