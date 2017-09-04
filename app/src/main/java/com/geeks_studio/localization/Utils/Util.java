package com.geeks_studio.localization.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by Classic on 27/9/2016.
 */
public class Util {

    private void _(String s){
        Log.d("utils", "#  " + s);
    }
    //checks if ther is internet connection
    public static boolean hasInternetConnectivity(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}
