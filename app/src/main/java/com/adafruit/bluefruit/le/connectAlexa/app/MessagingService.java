package com.adafruit.bluefruit.le.connectAlexa.app;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MessagingService extends FirebaseMessagingService {
    public static final String VOICE_COMMAND_RECEIVED = "VOICE_COMMAND_RECEIVED";

    private static final String TAG = "MessagingService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getData().get("code"));
        Intent intent = new Intent(VOICE_COMMAND_RECEIVED);
        intent.putExtra("code", new Integer(remoteMessage.getData().get("code")));
        sendBroadcast(intent);
    }
}
