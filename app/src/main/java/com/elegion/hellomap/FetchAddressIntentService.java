package com.elegion.hellomap;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * @author Alex
 */
public class FetchAddressIntentService extends IntentService {

    public FetchAddressIntentService() {
        super(FetchAddressIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName("Optikov 4", 10);

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses != null) {
            for (Address address : addresses) {
                Log.i("Address", address.toString());
            }
        }
    }
}
