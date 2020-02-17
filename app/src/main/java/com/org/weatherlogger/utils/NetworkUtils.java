package com.org.weatherlogger.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public interface NetworkUtils {

        public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
}
