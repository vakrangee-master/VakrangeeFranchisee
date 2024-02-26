package in.vakrangee.core.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.InputStream;
import java.util.Properties;

public class Helper {
    private static final String TAG = "Helper";

    public static String getConfigValue(Context context, String key) {
        Properties properties = new Properties();
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open("config.properties");
            properties.load(inputStream);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return properties.getProperty(key);
    }
}
