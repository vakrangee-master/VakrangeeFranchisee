package in.vakrangee.franchisee.documentmanager.address;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import in.vakrangee.franchisee.documentmanager.AsyncGetSpecificDocumentMgrDetails;
import in.vakrangee.franchisee.documentmanager.DocumentManagerRepository;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;

public class AsyncGetSpecificAddressDetails extends AsyncTask<String, Void, String> {

    private static final String TAG = "AsyncGetSpecificAddressDetails";
    private Context context;
    private ProgressDialog progressDialog;
    private String response;
    private DocumentManagerRepository documentManagerRepo;
    private Callback callback;
    private String addressType;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncGetSpecificAddressDetails(Context context,String addressType, Callback callback) {
        this.context = context;
        documentManagerRepo = new DocumentManagerRepository(context);
        this.callback = callback;
        this.addressType = addressType;
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
        String enquiryId = CommonUtils.getEnquiryId(context);
        String vkIdOrEnquiryId = TextUtils.isEmpty(tmpVkId) ? enquiryId : tmpVkId;

        response = documentManagerRepo.getSpecificAddressDetailsByType(vkIdOrEnquiryId, addressType);

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
