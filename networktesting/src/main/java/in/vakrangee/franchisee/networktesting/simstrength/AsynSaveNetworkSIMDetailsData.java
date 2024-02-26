package in.vakrangee.franchisee.networktesting.simstrength;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import in.vakrangee.supercore.franchisee.utils.Connection;

public class AsynSaveNetworkSIMDetailsData extends AsyncTask<String, Void, String> {

    private static final String TAG = "AsynSaveNetworkSIMDetailsData";
    private Context context;
    private String response;
    private NetworkTestingRepository networkTestingRepo;
    private Callback callback;
    private String jsonData;

    public interface Callback {
        void onResult(String result);
    }

    public AsynSaveNetworkSIMDetailsData(Context context, String jsonData, Callback callback) {
        this.context = context;
        this.jsonData = jsonData;
        networkTestingRepo = new NetworkTestingRepository(context);
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... strings) {

        Connection connection = new Connection(context);
        String tmpVkId = connection.getVkid();

        response = networkTestingRepo.saveNetworkSIMStrengthDetails(tmpVkId,jsonData);

        return null;
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (!TextUtils.isEmpty(response)) {
            if (response.startsWith("OKAY"))
                Log.d(TAG, "Get Data " + response);
            else
                Log.e(TAG, "Failed to get Data. " + response);

            if (callback != null)
                callback.onResult(response);  // Send Response Back To Caller.
        } else {
            if (callback != null)
                callback.onResult(null);    // Exception Occurred or Some Issue Occurred.
        }

    }

}
