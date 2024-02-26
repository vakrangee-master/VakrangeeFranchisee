package in.vakrangee.core.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefUtils {

    private static final String TAG = "SharedPrefUtils";

    private static SharedPrefUtils sharedPrefUtils;
    private static Context context;
    private static SharedPreferences settings;
    private static SharedPreferences.Editor editor;

    private SharedPrefUtils() {
    }

    public static SharedPrefUtils getInstance(Context context) {

        if (sharedPrefUtils == null) {
            sharedPrefUtils = new SharedPrefUtils();
        }
        settings = context.getSharedPreferences("VKMS_SHARED_DATA", Context.MODE_PRIVATE);

        return sharedPrefUtils;
    }

    //region Set and Get FCMId
    public void setFCMId(String fcmId) {
        editor = settings.edit();
        editor.putString("FCM_ID", fcmId);
        editor.commit();
    }

    public String getFCMId() {
        return settings.getString("FCM_ID", null);
    }
    //endregion


    public final static String PREFS_NAME = "VKMS_SHARED_DATA";

    //e.g: setStr("email","abc@mail.com");
    public static void setStr(String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    //e.g: getStr("email");
    public static String getStr(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(key, null);
    }

    /*public final static String FIRST_TIME = "IS_FIRST_CALL";

    public static void setFirstTime(String key){
        SharedPreferences sharedPref = context.getSharedPreferences(FIRST_TIME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key);
        editor.apply();
    }

    public static String getStr(String key) {
        SharedPreferences prefs = context.getSharedPreferences(FIRST_TIME, 0);
        return prefs.getString(key);
    }*/

}
