package in.vakrangee.core.utils.network;

import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import in.vakrangee.core.R;
import in.vakrangee.core.application.VakrangeeKendraApplication;
import in.vakrangee.core.utils.DeprecateHandler;

/**
 * Created by Vasundhara on 3/3/2018.
 */

public class NetworkHealthHandler {

    private static final String TAG = "NetworkHealthHandler";
    private static boolean connectionStatus;
    public final static String CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";

    private void checkNetworkHealth(Context context) {

        connectionStatus = isConnectedFast(context);
        if (!connectionStatus) {
            // connection is slow
            Log.e(TAG, "Network Health is Poor.");

        } else {
            // connection is fast
            Log.e(TAG, "Network Health is Good.");
        }
    }

    public static boolean isConnectedFast(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && isConnectionFast(info.getType(), info.getSubtype(), context));
    }

    public static boolean isConnectionFast(int type, int subType, Context context) {
        if (type == ConnectivityManager.TYPE_WIFI) {

            // Get Wifi Single Level
          /*  WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            int linkSpeed = wifiManager.getConnectionInfo().getRssi();
            int level = WifiManager.calculateSignalLevel(linkSpeed, 3);
            if (level <= 1)
                return false;*/

            return true;

        } else if (type == ConnectivityManager.TYPE_MOBILE) {

            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true; // ~ 400-7000 kbps

                // Above API level 7, make sure to set android:targetSdkVersion to appropriate level to use these

                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                    return true; // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                    return true; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                    return true; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                    return false; // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                    return true; // ~ 10+ Mbps
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return false;
            }
        } else {
            return false;
        }

    }

    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    public static void checkNetworkHealth(View v, Context context, DeprecateHandler deprecateHandler) {

        connectionStatus = isConnectedFast(context.getApplicationContext());
        if (!connectionStatus) {

            // connection is slow
            Log.e(TAG, "Network Health is Poor.");
            String msg = context.getString(R.string.poor_internet_text);
            String actionMsg = context.getString(R.string.close_text);
            displaySnackBar(v, msg, actionMsg, deprecateHandler);

        } else {
            // connection is fast
            //Log.e(TAG, "Network Health is Good.");
            displaySnackBar(v, "Good Connection.", "Close", deprecateHandler);
        }
    }

    public static void displaySnackBar(View view, String msg, String actionMsg, DeprecateHandler deprecateHandler) {
        final Snackbar snackbar = Snackbar.make(view, msg, 5000);

        //Message Text
        TextView tv = (TextView) (snackbar.getView()).findViewById(com.google.android.material.R.id.snackbar_text);
        tv.setTypeface(VakrangeeKendraApplication.sans_serif, Typeface.BOLD);
        tv.setTextSize(16);
        tv.setTextColor(deprecateHandler.getColor(R.color.white));
        tv.setMaxLines(4);

        //Button Text
        Button button = (Button) (snackbar.getView()).findViewById(com.google.android.material.R.id.snackbar_action);
        tv.setTypeface(VakrangeeKendraApplication.sans_serif, Typeface.BOLD);
        button.setTextSize(14);
        button.setTextColor(deprecateHandler.getColor(R.color.white));

        //Background Color
        snackbar.getView().setBackgroundColor(deprecateHandler.getColor(R.color.blackTransparent));

        snackbar.setAction(actionMsg, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    public static IntentFilter prepareIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CONNECTIVITY_CHANGE);
        intentFilter.setPriority(999);
        return intentFilter;
    }
}
