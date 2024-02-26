package in.vakrangee.franchisee.sitelayout.sitereadiness;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import in.vakrangee.franchisee.sitelayout.R;
import in.vakrangee.supercore.franchisee.model.FranchiseeDetails;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.webservice.WebService;

public class AsyncValidateLatLongWithAddress extends AsyncTask<String, Void, String> {

    private final static String TAG = "AsyncValidateLatLongWithAddress";
    private Context mContext;
    private boolean isAdhoc = false;
    private ProgressDialog progress;
    private AsyncValidateLatLongWithAddress.Callback callback;
    private FranchiseeDetails franchiseeDetails;
    private DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String type;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncValidateLatLongWithAddress(Context context) {
        super();
        this.mContext = context;
    }

    public AsyncValidateLatLongWithAddress(Context context, FranchiseeDetails franchiseeDetails, AsyncValidateLatLongWithAddress.Callback callback) {
        super();
        this.mContext = context;
        this.franchiseeDetails = franchiseeDetails;
        this.type = type;
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.e("TAG", "onPreExecute");
        // Get App MODE
        isAdhoc = Constants.ENABLE_ADHOC_MODE || Constants.ENABLE_FRANCHISEE_MODE;
        progress = new ProgressDialog(mContext);
        progress.setTitle(R.string.pleaseWait);
        progress.setMessage(mContext.getResources().getString(R.string.pleaseWait));
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }

    @SuppressLint("LongLogTag")
    @Override
    protected String doInBackground(String... strings) {

        try {

            //STEP 1: Get VKId
            Connection connection = new Connection(mContext);
            String vkId = connection.getVkid();
            if (TextUtils.isEmpty(vkId)) {
                Log.e(TAG, "Failed to validate LatLong with Address. [VKId is null].");
                return null;
            }

            //STEP 2: Get NextGenApplicationNo
            String appNo = franchiseeDetails.getNextGenFranchiseeApplicationNo();
            if (TextUtils.isEmpty(appNo)) {
                Log.e(TAG, "Failed to validate LatLong with Address.[NextGenApplicationNo is null].");
                return null;
            }

            //STEP 3: Get Latitude
            String latitude = franchiseeDetails.getLatitude();
            if (TextUtils.isEmpty(latitude)) {
                Log.e(TAG, "Failed to validate LatLong with Address.[Latitude is null].");
                return null;
            }

            //STEP 4: Get Longitude
            String longitude = franchiseeDetails.getLongitude();
            if (TextUtils.isEmpty(longitude)) {
                Log.e(TAG, "Failed to validate LatLong with Address.[Longitude is null].");
                return null;
            }

            String response = null;
            if (isAdhoc) {
                response = WebService.validateLatLongWithAddress(vkId, appNo, latitude, longitude);
            }
            return response;

        } catch (Exception e) {
            Log.e("TAG", "Exception: " + e.getMessage());
            Log.e(TAG, "Exception: " + e.toString());
        }
        return null;
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        progress.dismiss();     // Hide Progress Bar

        if (!TextUtils.isEmpty(response)) {
            if (response.startsWith("OKAY"))
                Log.d(TAG, "Validated LatLong with Address successfully. " + response);
            else
                Log.e(TAG, "Failed to validate LatLong with Address. " + response);

            if (callback != null)
                callback.onResult(response);  // Send Response Back To Caller.
        } else {
            if (callback != null)
                callback.onResult(null);    // Exception Occurred or Some Issue Occurred.
        }
    }
}
