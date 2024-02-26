package in.vakrangee.franchisee.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.activity.ATMDetailsViewPager;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.webservice.WebService;

/**
 * Created by nileshd on 6/13/2017.
 */
public class AsyncAddAtmRoCashLoading extends AsyncTask<String, Void, String> {

    Context mContext;
    TelephonyManager telephonyManager;
    String diplayServerResopnse;
    ProgressDialog progress;


    public AsyncAddAtmRoCashLoading(FragmentActivity activity) {
        this.mContext = activity;
    }


    @Override
    protected void onPreExecute() {
        Log.e("TAG", "onPreExecute");
        progress = new ProgressDialog(mContext);
        progress.setTitle(R.string.authenicationUsernamePassword);
        progress.setMessage(mContext.getResources().getString(R.string.pleaseWait));
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }

    @Override
    protected String doInBackground(String... values) {
        // If you want to use 'values' string in here
        Log.i("TAG", "doInBackground");
        try {

            diplayServerResopnse = WebService.addAtmRoCashLoading();


            Log.e("diplayServerResopnse ", diplayServerResopnse);


        } catch (Exception e) {
            Log.e("TAG", "AsyncAddAtmRoCashLoading " + e.getMessage());
            AlertDialogBoxInfo.alertDialogShow(mContext, mContext.getResources().getString(R.string.Warning));


        }


        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            progress.dismiss();


            if (diplayServerResopnse.equals("OKAY|empty")) {
                AlertDialogBoxInfo.alertDialogShow(mContext, "data saved successfully ");
                Intent intent = new Intent(mContext, ATMDetailsViewPager.class);
                mContext.startActivity(intent);

            } else {
                AlertDialogBoxInfo.alertDialogShow(mContext, diplayServerResopnse);
            }

        } catch (Exception e) {
            AlertDialogBoxInfo.alertDialogShow(mContext, mContext.getResources().getString(R.string.Warning));
        }

    }
}
