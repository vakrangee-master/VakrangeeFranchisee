package in.vakrangee.franchisee.sitelayout.update_kendra_address;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import in.vakrangee.franchisee.sitelayout.R;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;

public class AsyncSaveAddressDetails extends AsyncTask<String, Void, String> {

    private Context mContext;
    private ProgressDialog progress;
    private UpdateAddressRepository updateAddressRepo;
    private Callback callback;
    private UpdateAddressDetailsDto updateAddressDetailsDto;
    private String response;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncSaveAddressDetails(Context context, UpdateAddressDetailsDto addressDetailsDto, Callback icallback) {
        super();
        this.mContext = context;
        updateAddressRepo = new UpdateAddressRepository(context);
        this.updateAddressDetailsDto = addressDetailsDto;
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
            String enquiryId = CommonUtils.getEnquiryId(mContext);

            response = updateAddressRepo.saveAddressDetail(enquiryId, updateAddressDetailsDto);

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
