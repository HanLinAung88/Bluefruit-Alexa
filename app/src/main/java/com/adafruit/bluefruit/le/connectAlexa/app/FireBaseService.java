package com.adafruit.bluefruit.le.connectAlexa.app;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class FireBaseService extends FirebaseInstanceIdService {

    private static final String TAG = "FirebaseService";

    private String firebaseToken;

    @Override
    public void onTokenRefresh() {
        firebaseToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + firebaseToken);
    }
}
