package com.elegion.hellomap.billing;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.vending.billing.IInAppBillingService;
import com.elegion.hellomap.BuildConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by: Alex
 * Date creation: 23.02.15.
 */
public class BillingActivity extends Activity {

    @Nullable
    private IInAppBillingService mService;

    @NonNull
    private ServiceConnection mServiceConn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // connect to Billing service.
        bindService(
                new Intent("com.android.vending.billing.InAppBillingService.BIND")
                        .setPackage("com.android.vending"),
                mServiceConn,
                Context.BIND_AUTO_CREATE
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            unbindService(mServiceConn);
        }
    }

    private void initProductListInBackgound() throws RemoteException, JSONException {
        if (mService == null) {
            return;
        } else if (mService.isBillingSupported(3, BuildConfig.APPLICATION_ID, "inapp") != RESULT_OK) {
            return;
        }

        // setup a list of skus for details.
        ArrayList<String> skuList = new ArrayList<>(20);
        skuList.add("product1");
        skuList.add("product2");
        skuList.add("coins");

        Bundle skus = new Bundle();
        skus.putStringArrayList("ITEM_ID_LIST", skuList);

        Bundle result = mService.getSkuDetails(3, BuildConfig.APPLICATION_ID, "inapp", skus);

        int response = result.getInt("RESPONSE_CODE");
        if (response == 0) {
            ArrayList<String> responseList = result.getStringArrayList("DETAILS_LIST");

            for (String thisResponse : responseList) {
                JSONObject object = new JSONObject(thisResponse);
                String sku = object.getString("productId");
                String price = object.getString("price");

//                {
//                    "productId": "exampleSku",
//                    "type": "inapp",
//                    "price": "$5.00",
//                    "title": "ExampleTitle",
//                    "description": "Thisisanexampledescription"
//                }
            }
        }
    }

    private void purchaseItemInBackgournd() throws RemoteException, IntentSender.SendIntentException {
        if (mService == null) {
            return;
        }

        // developerPayload - optional, used to get additional information - randomly gnerated string.
        Bundle buyIntentBundle = mService.getBuyIntent(3, getPackageName(),
                "productId", "inapp", "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
        if (buyIntentBundle.get("RESPONSE_CODE") != 0) {
            return;
        }

        PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");

        startIntentSenderForResult(pendingIntent.getIntentSender(), 1001, new Intent(), 0, 0, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001) {
            int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
            String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
            String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");

            if (resultCode == RESULT_OK) {
                try {
                    JSONObject jo = new JSONObject(purchaseData);
                    String sku = jo.getString("productId");
//                    {
//                        "orderId":"12999763169054705758.1371079406387615",
//                        "packageName":"com.example.app",
//                        "productId":"exampleSku",
//                        "purchaseTime":1345678900000,
//                        "purchaseState":0,
//                        "developerPayload":"bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ",
//                        "purchaseToken":"opaque-token-up-to-1000-characters"
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
