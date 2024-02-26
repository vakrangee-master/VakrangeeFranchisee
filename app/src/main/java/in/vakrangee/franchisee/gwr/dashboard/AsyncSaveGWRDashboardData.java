package in.vakrangee.franchisee.gwr.dashboard;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import in.vakrangee.franchisee.gwr.GWRRepository;
import in.vakrangee.franchisee.gwr.evidence.EvidenceRepository;
import in.vakrangee.supercore.franchisee.utils.Logger;

public class AsyncSaveGWRDashboardData extends AsyncTask<String, Void, String> {

    private final static String TAG = "AsyncSaveGWRDashboardData";
    private Context mContext;
    private Logger logger;
    private ProgressDialog progress;
    private GWRRepository gwrRepo;
    private AsyncSaveGWRDashboardData.Callback callback;
    private String VKID;
    private String jsonData;
    private EvidenceRepository evidenceRepository;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncSaveGWRDashboardData(Context context, String VKID, String jsonData, AsyncSaveGWRDashboardData.Callback icallback) {
        super();
        this.mContext = context;
        logger = Logger.getInstance(context);
        gwrRepo = new GWRRepository(context);
        evidenceRepository = new EvidenceRepository(context);
        this.VKID = VKID;
        this.callback = icallback;
        this.jsonData = jsonData;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.e("TAG", "onPreExecute");
       /* progress = new ProgressDialog(mContext);
        progress.setTitle(R.string.pleaseWait);
        progress.setMessage(mContext.getResources().getString(R.string.pleaseWait));
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();*/
    }

    @Override
    protected String doInBackground(String... strings) {

        try {

            // Write into file.
            evidenceRepository.writeEvidenceInfo(jsonData);
            String response = gwrRepo.saveGWRInAuguration(VKID, jsonData);
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
        //progress.dismiss();     // Hide Progress Bar

        if (!TextUtils.isEmpty(response)) {
            if (response.startsWith("OKAY"))
                Log.d(TAG, "GWR InAuguration Status Updated successfully. " + response);
            else
                Log.e(TAG, "Failed to Update GWR InAuguration Status Data. " + response);

            if (callback != null)
                callback.onResult(response);  // Send Response Back To Caller.
        } else {
            if (callback != null)
                callback.onResult(null);    // Exception Occurred or Some Issue Occurred.
        }
    }
}
