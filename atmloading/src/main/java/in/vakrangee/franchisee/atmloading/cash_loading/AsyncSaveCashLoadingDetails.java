package in.vakrangee.franchisee.atmloading.cash_loading;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import in.vakrangee.franchisee.atmloading.ATMLoadingRepository;
import in.vakrangee.franchisee.atmloading.cash_sourcing.ATMCashSourcingDto;
import in.vakrangee.franchisee.atmloading.cash_sourcing.AsyncSaveCashSourcingDetails;
import in.vakrangee.supercore.franchisee.utils.Connection;

@SuppressLint("LongLogTag")
public class AsyncSaveCashLoadingDetails extends AsyncTask<String, Void, String> {

    private static final String TAG = "AsyncSaveCashLoadingDetails";
    private Context context;
    private ProgressDialog progressDialog;
    private String response;
    private ATMLoadingRepository atmLoadingRepo;
    private AsyncSaveCashLoadingDetails.Callback callback;
    private ATMCashLoadingDto atmCashLoadingDto;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncSaveCashLoadingDetails(Context context, ATMCashLoadingDto atmCashLoadingDto, AsyncSaveCashLoadingDetails.Callback callback) {
        this.context = context;
        this.atmCashLoadingDto = atmCashLoadingDto;
        atmLoadingRepo = new ATMLoadingRepository(context);
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

        response = atmLoadingRepo.saveATMCashLoadingDetail(vkId, atmCashLoadingDto);

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
