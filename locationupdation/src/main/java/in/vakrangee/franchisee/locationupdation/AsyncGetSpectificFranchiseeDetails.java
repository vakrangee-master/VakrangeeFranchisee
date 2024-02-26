package in.vakrangee.franchisee.locationupdation;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;


import in.vakrangee.supercore.franchisee.franchiseelogin.FranchiseeLoginChecksDto;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;

import static android.content.Context.MODE_PRIVATE;

public class AsyncGetSpectificFranchiseeDetails extends AsyncTask<String, Void, String> {

    private static final String TAG = AsyncGetSpectificFranchiseeDetails.class.getSimpleName();
    private Context context;
    ProgressDialog progress;
    private KendraVerificationRepository kendraVerificationRepository;
    private String response = null;
    private AsyncGetSpectificFranchiseeDetails.Callback callback;
    private Connection connection;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncGetSpectificFranchiseeDetails(Context context, AsyncGetSpectificFranchiseeDetails.Callback callback) {
        this.context = context;
        this.kendraVerificationRepository = new KendraVerificationRepository(context);
        this.callback = callback;
        connection = new Connection(context);

    }

    @Override
    protected void onPreExecute() {
        Log.e("TAG", "onPreExecute");
        progress = new ProgressDialog(context);
        progress.setMessage(context.getResources().getString(R.string.pleaseWait));
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }

    @Override
    protected String doInBackground(String... strings) {

        FranchiseeLoginChecksDto loginChecksDto = CommonUtils.getFranchiseeLoginDataFromPreferences(context);
        if (loginChecksDto == null || loginChecksDto.getNextGenEnquiryId() == null)
            return null;

        String vkId = connection.getVkid();
        //String vkId = context.getSharedPreferences("CommonPrefs", MODE_PRIVATE).getString("USER_VKID", null);

        //data - filter data pass in post service
        response = kendraVerificationRepository.getSpecificFranchiseeDetails(vkId);
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        progress.dismiss();

        if (!TextUtils.isEmpty(response)) {
            if (response.startsWith("OKAY"))
                Log.d(TAG, "Get AsyncGetSpectificFranchiseeDetails Data successfully. " + response);
            else
                // AlertDialogBoxInfo.alertDialogShow(context, "Failed to get data from server.");
                Log.e(TAG, "Failed to Get  AsyncGetSpectificFranchiseeDetails Data." + response);

            if (callback != null)
                callback.onResult(response);  // Send Response Back To Caller.
        } else {
            if (callback != null)
                callback.onResult(null);    // Exception Occurred or Some Issue Occurred.

        }
    }

}
