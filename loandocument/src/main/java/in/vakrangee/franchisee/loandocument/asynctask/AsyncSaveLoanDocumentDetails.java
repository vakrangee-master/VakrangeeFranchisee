package in.vakrangee.franchisee.loandocument.asynctask;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import in.vakrangee.franchisee.loandocument.LoanDocumentRepository;
import in.vakrangee.franchisee.loandocument.model.LoanDocumentDto;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;

public class AsyncSaveLoanDocumentDetails extends AsyncTask<String, Void, String> {

    private static final String TAG = "AsyncSaveLoanDocumentDetails";
    private Context context;
    private ProgressDialog progressDialog;
    private String response;
    private LoanDocumentRepository loanDocumentRepo;
    private Callback callback;
    private LoanDocumentDto loanDocumentDto;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncSaveLoanDocumentDetails(Context context, LoanDocumentDto loanDocumentDto, Callback callback) {
        this.context = context;
        this.loanDocumentDto = loanDocumentDto;
        loanDocumentRepo = new LoanDocumentRepository(context);
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

        String enquiryId = CommonUtils.getEnquiryId(context);
        response = loanDocumentRepo.saveLoanDocumentDetail(enquiryId, loanDocumentDto);

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
