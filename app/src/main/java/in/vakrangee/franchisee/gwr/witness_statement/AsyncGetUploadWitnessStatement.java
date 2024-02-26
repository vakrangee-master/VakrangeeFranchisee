package in.vakrangee.franchisee.gwr.witness_statement;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import in.vakrangee.franchisee.gwr.GWRRepository;

public class AsyncGetUploadWitnessStatement extends AsyncTask<String, Void, String> {

    private static final String TAG = "AsyncGetUploadWitnessStatement";
    private Context mContext;
    private String response;
    private ProgressDialog progress;
    private String vkId;
    private GWRRepository gwrRepository;
    private AsyncGetUploadWitnessStatement.Callback callback;

    public AsyncGetUploadWitnessStatement(Context context, String vkId) {
        this.mContext = context;
        this.vkId = vkId;
        gwrRepository = new GWRRepository(mContext);
    }

    public AsyncGetUploadWitnessStatement(Context context, String vkId, AsyncGetUploadWitnessStatement.Callback icallback) {
        this.mContext = context;
        this.vkId = vkId;
        gwrRepository = new GWRRepository(mContext);
        this.callback = icallback;
    }

    public interface Callback {
        void onResult(String result);
    }

    @Override
    protected void onPreExecute() {
       /* progress = new ProgressDialog(mContext);
        progress.setMessage(mContext.getResources().getString(R.string.pleaseWait));
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();*/
    }

    @Override
    protected String doInBackground(String... strings) {

        response = gwrRepository.getUploadWitnesStatement(vkId);
        return null;
    }

    @Override
    protected void onPostExecute(String s) {

        if (!TextUtils.isEmpty(response)) {
            if (response.startsWith("OKAY"))
                Log.d(TAG, "GWR Details Updated successfully. " + response);
            else
                Log.e(TAG, "Failed to Update GWR Details. " + response);

            if (callback != null)
                callback.onResult(response);  // Send Response Back To Caller.
        } else {
            if (callback != null)
                callback.onResult(null);    // Exception Occurred or Some Issue Occurred.
        }

       /* progress.dismiss();

        if (TextUtils.isEmpty(response)) {
            AlertDialogBoxInfo.alertDialogShow(mContext, mContext.getResources().getString(R.string.Warning));
            return;
        }

        // Handle Error Response From Server.
        if (response.startsWith("ERROR|")) {

            StringTokenizer tokens = new StringTokenizer(response, "|");
            tokens.nextToken();     // Jump to next Token
            String errMsg = tokens.nextToken();

            if (!TextUtils.isEmpty(errMsg)) {
                AlertDialogBoxInfo.alertDialogShow(mContext, errMsg);
            }
            return;
        }

        //Process response
        if (response.startsWith("OKAY|")) {
            StringTokenizer st1 = new StringTokenizer(response, "|");
            String key = st1.nextToken();
            String data = st1.nextToken();

            if (TextUtils.isEmpty(data)) {
                AlertDialogBoxInfo.alertDialogShow(mContext, mContext.getResources().getString(R.string.Warning));
                return;
            }

            Intent intent = new Intent(mContext, GuinnessUploadWitnessStatementActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("GWR_UPLOAD_WITNESS_STMT_DETAILS", data);
            mContext.startActivity(intent);
        }*/
    }
}
