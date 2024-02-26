package in.vakrangee.core.utils.network;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import com.mindorks.nybus.NYBus;
import com.mindorks.nybus.event.Channel;

import in.vakrangee.core.R;

@SuppressLint("LongLogTag")
public class ConnectivityChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String msg = "";
        String action = intent.getAction();

        if (!isOnline(context)) {
            msg = "You're offline.";
        } else {

            boolean connectionStatus = NetworkHealthHandler.isConnectedFast(context.getApplicationContext());
            if (!connectionStatus)           // connection is slow
                msg = context.getString(R.string.poor_internet_text);
           /* else                           // connection is fast
                msg = "Good Connection.";*/
        }

        // Post the event
        Log.e("ConnectivityChangeReceiver", "Testing: Action:" + action + " Msg: " + msg);
        if (TextUtils.isEmpty(msg))
            return;

        EventData event = new EventData(msg);
        NYBus.get().post(event, Channel.TWO);

    }

    public boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }
}
