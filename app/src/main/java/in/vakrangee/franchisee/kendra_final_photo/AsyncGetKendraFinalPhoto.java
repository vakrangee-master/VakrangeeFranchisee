package in.vakrangee.franchisee.kendra_final_photo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;

import java.util.StringTokenizer;

import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;

public class AsyncGetKendraFinalPhoto extends AsyncTask<String, Void, String> {

    private Context mContext;
    private String response;
    private ProgressDialog progress;
    private String vkId;
    private KendraPhotoRepository kendraPhotoRepository;

    public AsyncGetKendraFinalPhoto(Context context, String vkId) {
        this.mContext = context;
        this.vkId = vkId;
        kendraPhotoRepository = new KendraPhotoRepository(mContext);
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

        response = kendraPhotoRepository.getKendraFinalPhoto(vkId);
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        progress.dismiss();

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
            st1.nextToken();
            String data = st1.nextToken();

            if (TextUtils.isEmpty(data)) {
                AlertDialogBoxInfo.alertDialogShow(mContext, mContext.getResources().getString(R.string.Warning));
                return;
            }

            Intent intent = new Intent(mContext, KendraFinalPhotoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("KENDRA_PHOTO_DETAILS", data);
            mContext.startActivity(intent);
        }
    }
}
