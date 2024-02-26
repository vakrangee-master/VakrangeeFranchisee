package in.vakrangee.franchisee.gwr.dashboard;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import java.util.StringTokenizer;

import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.gwr.GWRRepository;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;

public class AsyncGetGWRDashboardData extends AsyncTask<String, Void, String> {

    private Context mContext;
    private String response;
    private ProgressDialog progress;
    private String vkId;
    private GWRRepository gwrRepository;
    private Callback callback;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncGetGWRDashboardData(Context context, String vkId, Callback callback) {
        this.mContext = context;
        this.vkId = vkId;
        gwrRepository = new GWRRepository(mContext);
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        progress = new ProgressDialog(mContext);
        progress.setMessage(mContext.getResources().getString(R.string.pleaseWait));
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }

    @Override
    protected String doInBackground(String... strings) {

        response = gwrRepository.getGWRDashboardDetails(vkId);
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            progress.dismiss();

            if (TextUtils.isEmpty(response)) {
                AlertDialogBoxInfo.alertDialogShow(mContext, mContext.getResources().getString(R.string.Warning));
                if (callback != null)
                    callback.onResult(null);
                return;
            }

            // Handle Error Response From Server.
            if (response.startsWith("ERROR|")) {

                StringTokenizer tokens = new StringTokenizer(response, "|");
                tokens.nextToken();     // Jump to next Token
                String errMsg = tokens.hasMoreElements() ? tokens.nextToken() : null;

                if (!TextUtils.isEmpty(errMsg)) {
                    AlertDialogBoxInfo.alertDialogShow(mContext, errMsg);
                }
                if (callback != null)
                    callback.onResult(null);
                return;
            }

            //Process response
            if (response.startsWith("OKAY|")) {
                StringTokenizer st1 = new StringTokenizer(response, "|");
                String key = st1.nextToken();
                String data = st1.nextToken();

                if (TextUtils.isEmpty(data)) {
                    AlertDialogBoxInfo.alertDialogShow(mContext, mContext.getResources().getString(R.string.Warning));
                    if (callback != null)
                        callback.onResult(null);
                    return;
                }

                if (callback != null)
                    callback.onResult(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (callback != null)
                callback.onResult(null);
        }
    }
}
