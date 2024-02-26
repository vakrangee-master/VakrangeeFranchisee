package in.vakrangee.core.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import in.vakrangee.core.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AppNotificationUtils {

    final static String TAG = AppNotificationUtils.class.getCanonicalName();
    private Context mContext;
    private NotificationManager mNotifyManager;
    private Uri alarmSound;
    public static final int MAX_LENGTH_TEXT = 35;

    private AppNotificationUtils() {
    }

    public AppNotificationUtils(@NonNull Context context) {
        this.mContext = context;
        mNotifyManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    }

    //create notification with small title and description
    public void createNotification(@NonNull String title, @NonNull String description) {

        mNotifyManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(mNotifyManager, title, description);
        } else {
            showNotification(title, description);
        }
    }

    //create notification with big description and title
    public void createNotificationWithSummary(@NonNull String title, @NonNull String description) {

        mNotifyManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannelWithSummary(mNotifyManager, title, description);
        } else {
            showSummaryNotification(title, description);
        }
    }

    //create notification with image and description
    public void createNotificationWithImageID(@NonNull String title, @NonNull String description, @NonNull String imageId) {

        final String imageUrl = Constants.DownloadImageUrl + imageId;
        new notificationWithImageAsyn(mContext, title, description, imageUrl).execute();

    }

    /**
     * Show notification with default icon
     *
     * @param title
     * @param description
     * @return notificationId
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public int showNotification(@NonNull String title, @NonNull String description) {

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description))
            return 0;

        Notification notification = new Notification.Builder(mContext)
                .setContentTitle(title)
                .setContentText(Html.fromHtml(description))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setSound(alarmSound)
                .build();

        int id = (int) System.currentTimeMillis();
        mNotifyManager.notify(id, notification);

        return id;
    }

    /**
     * Show Summary notification with default icon
     *
     * @param title
     * @param description
     * @return notificationId
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public int showSummaryNotification(@NonNull String title, @NonNull String description) {

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description))
            return 0;

        final Notification.Builder builder = new Notification.Builder(mContext);
        builder.setStyle(new Notification.BigTextStyle(builder)
                .bigText(Html.fromHtml(description))
                .setBigContentTitle(title)
                .setSummaryText(Html.fromHtml(description)))
                .setContentTitle(title)
                .setContentText(Html.fromHtml(description))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setSound(alarmSound);

        Notification notification = builder.build();

        int id = (int) System.currentTimeMillis();
        mNotifyManager.notify(id, notification);

        return id;
    }

    /**
     * Show notification - On click of notification open Specified PendingIntent.
     *
     * @param title
     * @param description
     * @param pendingIntent
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public int showNotification(@NonNull String title, @NonNull String description, @NonNull PendingIntent pendingIntent) {

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description) || pendingIntent == null)
            return 0;

        Notification notification = new Notification.Builder(mContext)
                .setContentTitle(title)
                .setContentText(description)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(alarmSound)
                .build();


        int id = (int) System.currentTimeMillis();
        mNotifyManager.notify(id, notification);

        return id;
    }

    //region show notification with image
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public int showNotificationWithImage(@NonNull String title, @NonNull String description, @NonNull String imageId) {
        final String imageUrl = Constants.DownloadImageUrl + imageId;
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description))
            return 0;

        int id = (int) System.currentTimeMillis();
        new notificationWithImageAsyn(mContext, title, description,
                imageUrl).execute();
        return id;

    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public int showNotification(@NonNull String title, @NonNull String description, @NonNull PendingIntent pendingIntent, boolean isAutoCancel) {

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description) || pendingIntent == null)
            return 0;

        Notification notification = new Notification.Builder(mContext)
                .setContentTitle(title)
                .setContentText(description)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setAutoCancel(isAutoCancel)
                .build();

        int id = (int) System.currentTimeMillis();
        mNotifyManager.notify(id, notification);

        return id;
    }

    //region Clear or Cancel Notification
    public void clearNotification(int id) {
        mNotifyManager.cancel(id);
    }

    public void cancelAll() {
        mNotifyManager.cancelAll();
    }
    //endregion

    // create channel use for Oreo  - notification handle
    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel(NotificationManager notificationManager, @NonNull String title, @NonNull String desc) {
        String chanelId = "Channel_01";
        String description = "Notifications for download status";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel mChannel = new NotificationChannel(chanelId, chanelId, importance);
        mChannel.setDescription(description);
        mChannel.enableLights(true);
        mChannel.setLightColor(Color.BLUE);
        notificationManager.createNotificationChannel(mChannel);

        Notification notification = new Notification.Builder(mContext)
                .setContentTitle(title)
                .setContentText(Html.fromHtml(desc))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setChannelId(chanelId)
                .build();

        // Issue the notification.
        int id = (int) System.currentTimeMillis();
        notificationManager.notify(id, notification);

    }

    // create channel use for Oreo  - notification big message size handle
    @TargetApi(Build.VERSION_CODES.O)
    private void createChannelWithSummary(NotificationManager notificationManager, @NonNull String title, @NonNull String desc) {
        String chanelId = "Channel_01";
        String description = "Notifications for download status";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel mChannel = new NotificationChannel(chanelId, chanelId, importance);
        mChannel.setDescription(description);
        mChannel.enableLights(true);
        mChannel.setLightColor(Color.BLUE);
        notificationManager.createNotificationChannel(mChannel);

       /* Notification notification = new Notification.Builder(mContext)
                .setContentTitle(title)
                .setContentText(Html.fromHtml(desc))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setChannelId(chanelId)
                .build();*/

        final Notification.Builder builder = new Notification.Builder(mContext);
        builder.setStyle(new Notification.BigTextStyle(builder)
                .bigText(Html.fromHtml(desc))
                .setBigContentTitle(title)
                .setSummaryText(Html.fromHtml(desc)))
                .setContentTitle(title)
                .setContentText(Html.fromHtml(desc))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setChannelId(chanelId)
                .setSound(alarmSound);

        Notification notification = builder.build();


        // Issue the notification.
        int id = (int) System.currentTimeMillis();
        notificationManager.notify(id, notification);

    }


    // create channel use for Oreo  - notification with Image handle
    @TargetApi(Build.VERSION_CODES.O)
    private void createChannelWithImageID(NotificationManager notificationManager, @NonNull String title, @NonNull String desc, @NonNull Bitmap imagebitmap) {
        String chanelId = "Channel_01";
        String description = "Notifications for download status";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel mChannel = new NotificationChannel(chanelId, chanelId, importance);
        mChannel.setDescription(description);
        mChannel.enableLights(true);
        mChannel.setLightColor(Color.BLUE);
        notificationManager.createNotificationChannel(mChannel);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(Html.fromHtml(title)) //set title
                .setChannelId(chanelId)
                .setContentText(Html.fromHtml(desc)) //set message
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        if (imagebitmap != null) {
            NotificationCompat.BigPictureStyle bpic = new NotificationCompat.BigPictureStyle()
                    .bigPicture(imagebitmap);  //result set - image url to bitma)
            builder.setStyle(bpic);
        }

        NotificationManager mNotificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        builder.setAutoCancel(true);
        int id = (int) System.currentTimeMillis();
        mNotificationManager.notify(id, builder.build());

        // Issue the notification.
        //int id = (int) System.currentTimeMillis();
        // notificationManager.notify(id, notification);
    }

    //Asyntask use for Image url to image show in notification
    @SuppressLint("StaticFieldLeak")
    public class notificationWithImageAsyn extends AsyncTask<String, Void, Bitmap> {

        private Context mContext;
        private String title, message, imageUrl;

        public notificationWithImageAsyn(Context context, String title, String message, String imageUrl) {
            super();
            this.mContext = context;
            this.title = title;
            this.message = message;
            this.imageUrl = imageUrl;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            mNotifyManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createChannelWithImageID(mNotifyManager, title, message, result);
            } else {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
                builder.setStyle(new NotificationCompat.BigTextStyle(builder)
                        .bigText(Html.fromHtml(message))
                        .setBigContentTitle(title)
                        .setSummaryText(Html.fromHtml(message)))
                        .setContentTitle(title)
                        .setContentText(Html.fromHtml(message))
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title) //set title
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                if (result != null) {
                    NotificationCompat.BigPictureStyle bpic = new NotificationCompat.BigPictureStyle()
                            .bigPicture(result)  //result set - image url to bitmap
                            .setSummaryText(Html.fromHtml(message));
                    builder.setStyle(bpic);
                }

                NotificationManager mNotificationManager =
                        (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

                builder.setAutoCancel(true);
                int id = (int) System.currentTimeMillis();
                mNotificationManager.notify(id, builder.build());

            }
        }
    }

}
