package in.vakrangee.franchisee.atmtechlivechecklist.asynctask;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import in.vakrangee.franchisee.atmtechlivechecklist.ATMCheckListRepository;
import in.vakrangee.franchisee.atmtechlivechecklist.R;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;

@SuppressLint("LongLogTag")
public class AsyncGetATMTechLiveCheckList extends AsyncTask<String, Void, String> {

    private final static String TAG = "AsyncGetATMTechLiveCheckList";
    private Context mContext;
    private ProgressDialog progress;
    private ATMCheckListRepository atmCheckListRepo;
    private Callback callback;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncGetATMTechLiveCheckList(Context context, Callback icallback) {
        super();
        this.mContext = context;
        atmCheckListRepo = new ATMCheckListRepository(context);
        this.callback = icallback;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.e("TAG", "onPreExecute");
        progress = new ProgressDialog(mContext);
        progress.setMessage(mContext.getResources().getString(R.string.pleaseWait));
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {

            Connection connection = new Connection(mContext);
            String enquiryId = CommonUtils.getEnquiryId(mContext);

            String response = atmCheckListRepo.getAllATMTechLiveCheckListDetails(enquiryId);
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
            if (response.startsWith("OKAY"))
                Log.d(TAG, "AsyncGetATMTechLiveCheckList  Data successfully. " + response);
            else
                Log.e(TAG, "Failed to get AsyncGetATMTechLiveCheckList  Data. " + response);

            if (callback != null)
                callback.onResult(response);  // Send Response Back To Caller.
        } else {
            if (callback != null)
                callback.onResult(null);    // Exception Occurred or Some Issue Occurred.
        }
    }
}
