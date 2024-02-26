package in.vakrangee.franchisee.support;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import in.vakrangee.franchisee.activity.AppNotificationActivity;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Constants;

public class SecretCodeReceiver extends BroadcastReceiver {

    private static final String TAG = "SecretCodeReceiver";
    private Connection connection;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SECRET_CODE")) {
            String uri = intent.getDataString();
            String sep[] = uri.split("://");
            if (sep[1].equalsIgnoreCase("1800")) {

                connection = new Connection(context);
                connection.openDatabase();

                String strTokenId = connection.getTokenId();
                Log.d(TAG, "Token Id: " + strTokenId);
                if (TextUtils.isEmpty(strTokenId)) {

                    Intent i = new Intent(context, AppNotificationActivity.class);
                    i.putExtra(Constants.INTENT_KEY_FROM_ACTIVITY, Constants.INTENT_VALUE_SUPPORT_TICKET);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);

                } else {

                    Intent i = new Intent(context, SupportTicketActivity.class);
                    intent.putExtra("IsBackVisible", false);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            }
        }
    }
}
