package com.elegion.hellomap.gcm;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by: Alex
 * Date creation: 22.02.15.
 */
public class GcmIntentService extends GcmListenerService {

    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onMessageReceived(String from, final Bundle data) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(
                        getApplicationContext(),
                        String.format("Message from server %s",
                                data.get("message")
                        ), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
