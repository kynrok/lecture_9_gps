package com.elegion.hellomap.gcm;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by: Alex
 * Date creation: 22.02.15.
 */
public class GCMActivity extends Activity {

    private static final String LOG_TAG = GCMActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerInBackground();
    }

    private void registerInBackground() {
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) != ConnectionResult.SUCCESS) {
            // Need to resolve problem.
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                InstanceID instanceID = InstanceID.getInstance(GCMActivity.this);
                try {
                    String regId = instanceID.getToken("1008709827575", GoogleCloudMessaging.INSTANCE_ID_SCOPE);
                    // Send
                    sendRegIdToServe(regId);
                } catch (IOException e) {
                    // Some error!
                }
            }

        }).start();
    }

    private void sendRegIdToServe(String regId) {
        Log.i(LOG_TAG, regId);
    }
}
