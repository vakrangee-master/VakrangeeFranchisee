package in.vakrangee.franchisee.payment_details;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Logger;

@SuppressLint("LongLogTag")
public class AsyncPostRaiseDisputeDetails extends AsyncTask<String, Void, String> {

    private Context mContext;
    private ProgressDialog progress;
    private PaymentDetailsRepository paymentDetailsRepo;
    private Callback callback;
    private RaiseDisputeDto raiseDisputeDto;
    private String response;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncPostRaiseDisputeDetails(Context context, RaiseDisputeDto raiseDisputeDto, Callback icallback) {
        super();
        this.mContext = context;
        paymentDetailsRepo = new PaymentDetailsRepository(context);
        this.raiseDisputeDto = raiseDisputeDto;
        this.callback = icallback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(mContext);
        progress.setMessage(mContext.getResources().getString(R.string.pleaseWait));
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }

    @Override
    protected String doInBackground(String... strings) {

        try {
            response = null;
            Connection connection = new Connection(mContext);
            String tmpVkId = connection.getVkid();
            String enquiryId = CommonUtils.getEnquiryId(mContext);
            String vkIdOrEnquiryId = TextUtils.isEmpty(enquiryId) ? tmpVkId : enquiryId;

            response = paymentDetailsRepo.saveRaiseDisputeDetail(vkIdOrEnquiryId, raiseDisputeDto);

            return response;

        } catch (Exception e) {
            Log.e("TAG", "Exception: " + e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        progress.dismiss();     // Hide Progress Bar

        if (!TextUtils.isEmpty(response)) {

            if (callback != null)
                callback.onResult(response);  // Send Response Back To Caller.
        } else {
            if (callback != null)
                callback.onResult(null);    // Exception Occurred or Some Issue Occurred.
        }
    }
}
