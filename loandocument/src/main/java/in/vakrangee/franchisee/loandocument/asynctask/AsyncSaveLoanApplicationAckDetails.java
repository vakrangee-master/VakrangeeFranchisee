package in.vakrangee.franchisee.loandocument.asynctask;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import in.vakrangee.franchisee.loandocument.LoanDocumentRepository;
import in.vakrangee.franchisee.loandocument.model.LoanDocumentDto;
import in.vakrangee.franchisee.loandocument.model.UploadAcknowledgementDto;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;

public class AsyncSaveLoanApplicationAckDetails extends AsyncTask<String, Void, String> {

    private static final String TAG = "AsyncSaveLoanApplicationAckDetails";
    private Context context;
    private ProgressDialog progressDialog;
    private String response;
    private LoanDocumentRepository loanDocumentRepo;
    private Callback callback;
    private UploadAcknowledgementDto uploadAcknowledgementDto;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncSaveLoanApplicationAckDetails(Context context, UploadAcknowledgementDto uploadAcknowledgementDto, Callback callback) {
        this.context = context;
        this.uploadAcknowledgementDto = uploadAcknowledgementDto;
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

        Connection connection = new Connection(context);
        String vkId = connection.getVkid();

        //String enquiryId = CommonUtils.getEnquiryId(context);
        response = loanDocumentRepo.saveUBILoanApplicationAckDetail(vkId, uploadAcknowledgementDto);

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
