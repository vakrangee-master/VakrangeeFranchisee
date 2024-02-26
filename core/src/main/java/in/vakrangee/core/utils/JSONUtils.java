package in.vakrangee.core.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;

public class JSONUtils {
    public static <T> T toJson(Class<T> type, String data) {
        final Gson gson = new GsonBuilder().create();
        return gson.fromJson(data, type);
    }

    public static String toString(Object obj) {
        final Gson gson = new GsonBuilder().create();
        return gson.toJson(obj);
    }

    public static JSONObject convertStringToJSONObject(@NonNull String str, @NonNull String delimiter, @NonNull String keySplit) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        if(TextUtils.isEmpty(str)) {
            return jsonObject;
        }

        // Format data
        String[] lines = str.split(delimiter);  // "\\r?\\n"
        for (String line : lines) {
            String[] data = line.split(keySplit);  // ":"      // Split Key and Value from line
            if(data.length != 2) {
                continue;
            }

            String key = data[0].trim();
            String value = data[1].trim();
            jsonObject.put(key, value);                   // Add Key and Value to JSONObject
        }
        return jsonObject;

    }
}
