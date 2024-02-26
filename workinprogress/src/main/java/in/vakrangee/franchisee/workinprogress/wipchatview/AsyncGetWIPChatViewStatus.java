package in.vakrangee.franchisee.workinprogress.wipchatview;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;

import java.util.StringTokenizer;

import in.vakrangee.franchisee.workinprogress.R;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Constants;

public class AsyncGetWIPChatViewStatus extends AsyncTask<String, Void, String> {

    private Context mContext;
    private String response;
    private ProgressDialog progress;
    private WIPChatViewRepository wipChatViewRepo;

    public AsyncGetWIPChatViewStatus(Context context) {
        this.mContext = context;
        wipChatViewRepo = new WIPChatViewRepository(context);
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

        Connection connection = new Connection(mContext);
        String vkId = connection.getVkid();
        if (!TextUtils.isEmpty(vkId)) {
            response = wipChatViewRepo.getWIPStatus(vkId);
        } else {
            String enquiryId = CommonUtils.getEnquiryId(mContext);
            response = wipChatViewRepo.getWIPStatus(enquiryId);
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            progress.dismiss();

            if (TextUtils.isEmpty(response)) {
                AlertDialogBoxInfo.alertDialogShow(mContext, mContext.getResources().getString(R.string.Warning));
                return;
            }

            // Handle Error Response From Server.
            if (response.startsWith("ERROR|")) {

                StringTokenizer tokens = new StringTokenizer(response, "|");
                tokens.nextToken();     // Jump to next Token
                String errMsg = tokens.hasMoreElements() ? tokens.nextToken() : mContext.getResources().getString(R.string.Warning);

                if (!TextUtils.isEmpty(errMsg)) {
                    AlertDialogBoxInfo.alertDialogShow(mContext, errMsg);
                }
                return;
            }

            //Process response
            if (response.startsWith("OKAY|")) {
                Intent intent = new Intent(mContext, WorkInProgressChatViewActivity.class);
                intent.putExtra("MODE", Constants.NEXT_GEN_WORK_IN_PROGRESS);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
