package in.vakrangee.franchisee.locationupdation;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import in.vakrangee.supercore.franchisee.utils.Connection;

@SuppressLint("LongLogTag")
public class AsyncSaveKendraLocationUpdation extends AsyncTask<String, Void, String> {

    private static final String TAG = "AsyncSaveKendraLocationUpdation";
    private Context context;
    private ProgressDialog progressDialog;
    private String response;
    private KendraVerificationRepository kendraVerificationRepo;
    private AsyncSaveKendraLocationUpdation.Callback callback;
    private KendraSavePhotoDto kendraSavePhotoDto;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncSaveKendraLocationUpdation(Context context, KendraSavePhotoDto kendraSavePhotoDto, AsyncSaveKendraLocationUpdation.Callback callback) {
        this.context = context;
        this.kendraSavePhotoDto = kendraSavePhotoDto;
        kendraVerificationRepo = new KendraVerificationRepository(context);
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {

        Connection connection = new Connection(context);
        String vkId = connection.getVkid();

        response = kendraVerificationRepo.saveKendraLocationUpdationDetail(vkId, kendraSavePhotoDto);

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }

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
