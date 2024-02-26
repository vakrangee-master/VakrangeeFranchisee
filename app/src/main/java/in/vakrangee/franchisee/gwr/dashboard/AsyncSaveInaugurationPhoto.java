package in.vakrangee.franchisee.gwr.dashboard;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.gwr.GWRRepository;
import in.vakrangee.supercore.franchisee.utils.Logger;

public class AsyncSaveInaugurationPhoto extends AsyncTask<String, Void, String> {

    private final static String TAG = "AsyncSaveInaugurationPhoto";
    private Context mContext;
    private Logger logger;
    private ProgressDialog progress;
    private GWRRepository gwrRepo;
    private AsyncSaveInaugurationPhoto.Callback callback;
    private String VKID;
    private String jsonData;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncSaveInaugurationPhoto(Context context, String VKID, String jsonData, AsyncSaveInaugurationPhoto.Callback icallback) {
        super();
        this.mContext = context;
        logger = Logger.getInstance(context);
        gwrRepo = new GWRRepository(context);
        this.VKID = VKID;
        this.callback = icallback;
        this.jsonData = jsonData;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.e("TAG", "onPreExecute");
        progress = new ProgressDialog(mContext);
        progress.setTitle(R.string.pleaseWait);
        progress.setMessage(mContext.getResources().getString(R.string.pleaseWait));
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }

    @Override
    protected String doInBackground(String... strings) {

        try {
            String response = gwrRepo.saveInAugurationImage(VKID, jsonData);
            return response;

        } catch (Exception e) {
            Log.e("TAG", "Exception: " + e.getMessage());
            logger.writeError(TAG, "Exception: " + e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        progress.dismiss();     // Hide Progress Bar

        if (!TextUtils.isEmpty(response)) {
            if (response.startsWith("OKAY"))
                Log.d(TAG, "InAuguration Photo Updated successfully. " + response);
            else
                Log.e(TAG, "Failed to InAuguration Photo . " + response);

            if (callback != null)
                callback.onResult(response);  // Send Response Back To Caller.
        } else {
            if (callback != null)
                callback.onResult(null);    // Exception Occurred or Some Issue Occurred.
        }
    }
}
