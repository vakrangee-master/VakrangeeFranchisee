package in.vakrangee.franchisee.webservice;

import android.app.PendingIntent;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import in.vakrangee.franchisee.activity.AppNotificationActivity;
import in.vakrangee.supercore.franchisee.utils.AppNotificationUtils;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    final static String TAG = MyFirebaseMessagingService.class.getCanonicalName();


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        try {

            //region Check Data
            if (remoteMessage.getData() == null) {
                Log.e(TAG, "No Data Found.");
                return;
            }

            String body = null;
            if (remoteMessage.getData() != null) {
                body = remoteMessage.getData().toString();
            }

            if (TextUtils.isEmpty(body)) {
                Log.e(TAG, "Body is blank or null.");
                return;
            }
            //endregion

            Log.i(TAG, "Body: " + body);

            // Extract Data From Body
            try {
                JSONObject jsonObject = new JSONObject(body);
                String id = jsonObject.optString("id");
                String action = jsonObject.optString("action");
                String payload = jsonObject.optString("payload");

                /*
                    NOTIFY - Show Message into Notification
                 */
                switch (action.toUpperCase()) {
                    case "NOTIFY": {
                        // Extract Data From Payload
                        JSONObject jsonObjPayload = new JSONObject(payload);
                        String title = jsonObjPayload.optString("title");
                        String message = jsonObjPayload.optString("message");
                        String imgId = jsonObjPayload.optString("img_id");

                        // Show Notification with Image
                        if (!TextUtils.isEmpty(imgId)) {
                            new AppNotificationUtils(this).createNotificationWithImageID(title, message, imgId);
                        } else if (!TextUtils.isEmpty(message) && message.length() <= AppNotificationUtils.MAX_LENGTH_TEXT) {
                            //if length of message 35
                            new AppNotificationUtils(this).createNotification(title, message);
                        } else {
                            // Show Big Style Notification and message size more then 35.
                            new AppNotificationUtils(this).createNotificationWithSummary(title, message);
                        }
                    }
                    break;

                    case "NEXTGEN_SITE_VISIT":
                        // Extract Data From Payload
                        JSONObject jsonObjPayload = new JSONObject(payload);
                        if (jsonObjPayload.has("application_no")) {
                            String fappNo = jsonObjPayload.optString("application_no");
                            String title = jsonObjPayload.optString("title");
                            String msg = jsonObjPayload.optString("message");

                            Intent intent = new Intent(this, AppNotificationActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("action", action);
                            intent.putExtra("data", payload);
                            // use System.currentTimeMillis() to have a unique ID for the pending intent
                            //PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
                            PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                            new AppNotificationUtils(this).showNotification(title, msg, pIntent);
                        }
                        break;

                    case "CLEAR_APP_DATA":

                        break;

                    default:

                        break;
                }

                //TODO: Acknowledge the notification has been received by an application.


               /* if (action.equalsIgnoreCase("NEXTGEN")) {
                    new AppNotificationUtils(this).showNotification("VKMS", "Notification Received.");
                }


                if(jsonObject.has("custom_notification")) {
                    JSONObject objectCustomNotification = jsonObject.getJSONObject("custom_notification");
                    String title = objectCustomNotification.getString("title");
                    String msg = objectCustomNotification.getString("message");

                    Intent intent = new Intent(this, AppNotificationActivity.class);
                    intent.putExtra("data", body);
                    // use System.currentTimeMillis() to have a unique ID for the pending intent
                    PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
                    new AppNotificationUtils(this).showNotification(title, msg, pIntent);
                }
                else {

                    String action = jsonObject.getString("action");
                    int id = jsonObject.getInt("id");
                    String payload = jsonObject.getString("payload");

                    if (action.equalsIgnoreCase("NEXTGEN")) {
                        new AppNotificationUtils(this).showNotification("VKMS", "Notification Received.");
                    }
                }*/

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
