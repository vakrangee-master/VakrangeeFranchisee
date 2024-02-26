package in.vakrangee.franchisee.webservice;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import in.vakrangee.supercore.franchisee.utils.SharedPrefUtils;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private final static String TAG = "MyFirebaseInstanceIdService";

    @SuppressLint("LongLogTag")
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // Save into Shared Preferences.
        if (!TextUtils.isEmpty(refreshedToken)) {
            SharedPrefUtils.getInstance(getApplicationContext()).setFCMId(refreshedToken);
            Log.e(TAG, "FCMId Saved.");
        }
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(refreshedToken);
    }
}
