package com.example.ahmedd.firabasetest.Receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

public class WIFIBroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
            if (intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false)){
                //do stuff
                Toast.makeText(context, "Wifi is on", Toast.LENGTH_LONG).show();
            } else {
                // wifi connection was lost
                Toast.makeText(context, "Wifi is lost", Toast.LENGTH_LONG).show();
            }

        }

    }


}
