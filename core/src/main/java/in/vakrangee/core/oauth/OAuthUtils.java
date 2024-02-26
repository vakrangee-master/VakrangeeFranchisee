package in.vakrangee.core.oauth;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class OAuthUtils {

    private static final String TAG = OAuthUtils.class.getSimpleName();

    private static Context mContext;

    public static String CONNECTION_TYPE;
    public static String HOST;
    private static String URL_OAUTH_TOKEN;
    private static String OAUTH_CLIENT_ID;      // "iOS-VakrangeeFranchisee";
    private static String OAUTH_CLIENT_SECRET;  // "secret";

    // Keys
    private static final String OAUTH_PREFS = "oauth_prefs";
    private static final String KEY_CONNECTION_TYPE = "CONNECTION_TYPE";
    private static final String KEY_OAUTH_CLIENT_ID = "OAUTH_CLIENT_ID";
    private static final String KEY_OAUTH_CLIENT_SECRET = "OAUTH_CLIENT_SECRET";
    private static final String KEY_OAUTH = "OAUTH";

    private static final String GRANT_TYPE_PASSWORD = "password";
    private static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";

    private OkHttpClient client;
    private String OAUTH_TOKEN;

    private static OAuthUtils oAuthUtils = null;

    //region Initialization
    private OAuthUtils() {
    }

    public static OAuthUtils getInstance(Context context) {

        if (oAuthUtils == null) {
            mContext = context;
            oAuthUtils = new OAuthUtils();
            init();
        }

        return oAuthUtils;
    }

    private static void init() {
        // Read Client Id and Secret from file and load
        Properties properties = new Properties();
        try {
            AssetManager assetManager = mContext.getAssets();
            InputStream inputStream = assetManager.open("config.properties");
            properties.load(inputStream);
            CONNECTION_TYPE = properties.getProperty(KEY_CONNECTION_TYPE);
            OAUTH_CLIENT_ID = properties.getProperty(KEY_OAUTH_CLIENT_ID);
            OAUTH_CLIENT_SECRET = properties.getProperty(KEY_OAUTH_CLIENT_SECRET);

            if (CONNECTION_TYPE.equals("LIVE")) {
                HOST = "auth";
            } else if (CONNECTION_TYPE.equals("UAT")) {
                HOST = "authsit";
            }

            // OAUTH Server URL.
            URL_OAUTH_TOKEN = String.format("https://%s.vakrangee.in/oauth/token", HOST);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getClientSecret() {

        if (TextUtils.isEmpty(OAUTH_CLIENT_ID) || TextUtils.isEmpty(OAUTH_CLIENT_SECRET)) {
            Log.e(TAG, "Client Secret Can't be Blank. OAUTH_CLIENT_ID: " + OAUTH_CLIENT_ID + " | OAUTH_CLIENT_SECRET: " + OAUTH_CLIENT_SECRET);
            return "";
        }

        String data = OAUTH_CLIENT_ID + ":" + OAUTH_CLIENT_SECRET;
        data = Base64.encodeToString(data.getBytes(), Base64.NO_WRAP);
        Log.e(TAG, "Client Secret Id: " + data);

        return data;
    }
    //endregion

    //region Get Auth Token

    /**
     * @param username
     * @param pwd
     * @return
     */
    public boolean authenticate(String username, String pwd) {
        Log.d(TAG, "Authentication Started...");

        // Prepare Request To get Token
        client = new OkHttpClient();
        try {

            // Initialize Builder (not RequestBody)
            FormBody.Builder builder = new FormBody.Builder();
            builder.add("grant_type", GRANT_TYPE_PASSWORD);
            builder.add("username", username);
            builder.add("password", pwd);

            RequestBody formBody = builder.build();
            Request request = new Request.Builder()
                    .url(URL_OAUTH_TOKEN)
                    .addHeader("Authorization", "Basic " + getClientSecret())
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .post(formBody)
                    .build();

            Response response = client.newCall(request).execute();
            int statusCode = response.code();
            String authResponse = response.body().string();

            Log.e(TAG, "Response Status: " + response.code() + " | Body : " + authResponse);

            // Do something with the response
            if (response.isSuccessful()) {

                if (!TextUtils.isEmpty(authResponse)) {
                    // Save Username and Password into Preferences
                    updatePrefs("username", username);
                    updatePrefs("password", pwd);

                    // Save JSON Data into preferences.
                    updatePrefs(KEY_OAUTH, authResponse);

                    // Get Access Token and Set to Variable
                    OAUTH_TOKEN = getDataFromAuthResponse("access_token");

                    Log.e(TAG, "OAUTH Access Token : " + OAUTH_TOKEN);

                    return true;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    //endregion

    //region Refresh Auth Token
    public void refreshToken() {
        // Prepare Request To get Token
        client = new OkHttpClient();
        try {
            // Get RefreshToken and JTI

            // Initialize Builder (not RequestBody)
            FormBody.Builder builder = new FormBody.Builder();
            builder.add("grant_type", GRANT_TYPE_REFRESH_TOKEN);
            builder.add("refresh_token", getDataFromAuthResponse("refresh_token"));
            builder.add("jti", getDataFromAuthResponse("jti"));

            RequestBody formBody = builder.build();
            Request request = new Request.Builder()
                    .url(URL_OAUTH_TOKEN)
                    .addHeader("Authorization", "Basic " + getClientSecret())
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .post(formBody)
                    .build();

            Response response = client.newCall(request).execute();

            int statusCode = response.code();
            String authResponse = response.body().string();

            Log.e(TAG, "Response Status: " + response.code() + " | Body : " + authResponse);

            // Do something with the response
            if (response.isSuccessful()) {
                // Get Auth Response
                if (!TextUtils.isEmpty(authResponse)) {

                    // Save JSON Data into preferences.
                    updatePrefs(KEY_OAUTH, authResponse);

                    // Get Access Token and Set to Variable
                    OAUTH_TOKEN = getDataFromAuthResponse("access_token");
                    Log.e(TAG, "OAUTH Access Token : " + OAUTH_TOKEN);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //endregion

    // region Get Access Token
    public String getToken() {
        return OAUTH_TOKEN;
    }
    // endregion

    //region Save Data into App Prefs
    private String getDataFromAuthResponse(String key) {
        String value = null;
        try {
            String authResponse = getPrefs(KEY_OAUTH);
            JSONObject jsonObject = new JSONObject(authResponse);
            value = jsonObject.optString(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    private void updatePrefs(String key, String value) {
        if (!TextUtils.isEmpty(key)) {
            SharedPreferences.Editor editor = mContext.getSharedPreferences(OAUTH_PREFS, MODE_PRIVATE).edit();
            editor.putString(key, value);
            editor.apply();
        }
    }

    private String getPrefs(String key) {
        SharedPreferences prefs = mContext.getSharedPreferences(OAUTH_PREFS, MODE_PRIVATE);
        return prefs.getString(key, null);
    }
    //endregion
}
