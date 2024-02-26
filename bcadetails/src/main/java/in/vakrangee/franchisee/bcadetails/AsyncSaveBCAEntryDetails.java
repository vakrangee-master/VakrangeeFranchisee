package in.vakrangee.franchisee.bcadetails;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Logger;

public class AsyncSaveBCAEntryDetails extends AsyncTask<String, Void, String> {

    private final static String TAG = "AsyncSaveBCAEntryDetails";
    private Context mContext;
    private Logger logger;
    private ProgressDialog progress;
    private BCAEntryDetailsRepository bcaEntryDetailsRepo;
    private AsyncSaveBCAEntryDetails.Callback callback;
    private String type;
    private BCAEntryDetailsDto bcaEntryDetailsDto;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncSaveBCAEntryDetails(Context context, String type, BCAEntryDetailsDto bcaEntryDetailsDto, AsyncSaveBCAEntryDetails.Callback icallback) {
        super();
        this.mContext = context;
        logger = Logger.getInstance(context);
        bcaEntryDetailsRepo = new BCAEntryDetailsRepository(context);
        this.type = type;
        this.bcaEntryDetailsDto = bcaEntryDetailsDto;
        this.callback = icallback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.e("TAG", "onPreExecute");
        progress = new ProgressDialog(mContext);
        //progress.setTitle(R.string.pleaseWait);
        progress.setMessage(mContext.getResources().getString(R.string.pleaseWait));
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            Connection connection = new Connection(mContext);
            String vkId = connection.getVkid();

            String response = bcaEntryDetailsRepo.saveBCAEntryDetail(vkId, type, bcaEntryDetailsDto);

            return response;

        } catch (Exception e) {
            Log.e("TAG", "Exception: " + e.getMessage());
            logger.writeError(TAG, "Exception: " + e.toString());
        }

        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        progress.dismiss();     // Hide Progress Bar

        if (!TextUtils.isEmpty(response)) {
            if (response.startsWith("OKAY"))
                Log.d(TAG, "BCA Entry Details Updated successfully. " + response);
            else
                Log.e(TAG, "Failed to Update BCA Entry Details . " + response);

            if (callback != null)
                callback.onResult(response);  // Send Response Back To Caller.
        } else {
            if (callback != null)
                callback.onResult(null);    // Exception Occurred or Some Issue Occurred.
        }
    }
}
