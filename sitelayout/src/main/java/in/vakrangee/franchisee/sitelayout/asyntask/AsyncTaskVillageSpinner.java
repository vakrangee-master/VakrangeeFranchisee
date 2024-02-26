package in.vakrangee.franchisee.sitelayout.asyntask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import in.vakrangee.franchisee.sitelayout.R;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.webservice.WebService;

public class AsyncTaskVillageSpinner extends AsyncTask<String, Void, String> {
    private final static String TAG = AsyncTaskVillageSpinner.class.getCanonicalName();
    private Context mContext;
    private ProgressDialog progress;
    private Callback callback;
    private String selectStateID, selectDistricID;
    private boolean isProgressDialogVisible = true;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncTaskVillageSpinner(Context context, String selectStateID, String selectDistricID,
                                   Callback callback) {

        this.mContext = context;
        this.callback = callback;
        this.selectStateID = selectStateID;
        this.selectDistricID = selectDistricID;
    }

    public AsyncTaskVillageSpinner(Context context, String selectStateID, String selectDistricID, boolean isProgressDialogVisible,
                                   Callback callback) {

        this.mContext = context;
        this.callback = callback;
        this.selectStateID = selectStateID;
        this.selectDistricID = selectDistricID;
        this.isProgressDialogVisible = isProgressDialogVisible;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.e("TAG", "onPreExecute");
        // Get App MODE
        progress = new ProgressDialog(mContext);
        if (isProgressDialogVisible) {

            progress.setTitle(R.string.pleaseWait);
            progress.setMessage(mContext.getResources().getString(R.string.pleaseWait));
            progress.setCancelable(false);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();
        } else {
            progress.dismiss();
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        Connection connection = new Connection(mContext);
        String tmpVkId = connection.getVkid();

        String vkId = EncryptionUtil.encryptString(tmpVkId, mContext);
        String tmpTokenId = connection.getTokenId();
        String response = null;

        EncryptionUtil.encryptString(tmpTokenId, mContext);
        response = WebService.getVillage(vkId, selectStateID, selectDistricID);

        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        Log.i(TAG, "onPostExecute");
        progress.dismiss();
        try {

            if (callback != null)
                callback.onResult(response);


        } catch (Exception e) {
            Log.d(TAG, "Error" + e);
        }
    }
}
