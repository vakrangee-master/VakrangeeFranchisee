package in.vakrangee.simcarddetail.simcarddetails;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import in.vakrangee.supercore.franchisee.utils.Connection;

public class AsyncGetSimcardDetailsData extends AsyncTask<String, Void, String> {

    private static final String TAG = "AsyncGetSimcardDetailsData";
    private Context context;
    private ProgressDialog progressDialog;
    private String response;
    private SimcardDetailsRepository simcardDetailsRepository;
    private AsyncGetSimcardDetailsData.Callback callback;
    private String ATMID;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncGetSimcardDetailsData(Context context, String ATMID, AsyncGetSimcardDetailsData.Callback callback) {
        this.context = context;
        this.ATMID = ATMID;
        simcardDetailsRepository = new SimcardDetailsRepository(context);
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {

        Connection connection = new Connection(context);
        String tmpVkId = connection.getVkid();
        //String enquiryId = CommonUtils.getEnquiryId(context);
        //String vkIdOrEnquiryId = TextUtils.isEmpty(tmpVkId) ? enquiryId : tmpVkId;

        response = simcardDetailsRepository.getSimcardDetailsData(tmpVkId, ATMID);

        return null;
    }

    @SuppressLint("LongLogTag")
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
