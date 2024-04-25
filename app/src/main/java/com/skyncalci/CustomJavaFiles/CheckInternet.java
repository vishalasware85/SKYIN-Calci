package com.skyncalci.CustomJavaFiles;

import android.content.Context;
import android.net.ConnectivityManager;

public class CheckInternet {
    public static boolean isNetworkAvailable(Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }
}