package in.vakrangee.franchisee.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.ifc.OnTaskCompleted;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.webservice.WebService;

/**
 * Created by Nileshd on 2/1/2017.
 */
public class AsyncgetLocationDetails extends AsyncTask<String, Void, String> {
    Context mContext;
    TelephonyManager telephonyManager;
    String diplayServerResopnse;
    private static final String TAG = "Result";
    Logger log;
    ProgressDialog progress;
    String[] myVakrangeeKendraLocations;
    public OnTaskCompleted delegate = null;
    HashSet<String> words;

    public AsyncgetLocationDetails(Context context, String[] myVakrangeeKendraLocations) {
        super();
        mContext = context;
        this.myVakrangeeKendraLocations = myVakrangeeKendraLocations;
        this.delegate = (OnTaskCompleted) context;

    }
    //this is the method to query

    @Override
    protected void onPreExecute() {
        Log.e("TAG", "onPreExecute");
        progress = new ProgressDialog(mContext);
        // progress.setTitle(R.string.authenicationUsernamePassword);
        progress.setMessage(mContext.getResources().getString(R.string.pleaseWait));
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }

    @Override
    protected String doInBackground(String... values) {
        Log.i(TAG, "doInBackground");
        try {

            String vkid = values[0];
            String TokenId = values[1];
            String imei = values[2];
            String deviceid = values[3];
            String simserialnumber = values[4];
            String scope = values[5];
            String id = values[6];


            diplayServerResopnse = WebService.myVakrangeeKendraLocationDetails(vkid, TokenId, imei, deviceid, simserialnumber, scope, id);
            Log.e("diplayServerResopnse", diplayServerResopnse);

        } catch (Exception e) {
            // AlertDialogBoxInfo.alertDialogShow(mContext, mContext.getResources().getString(R.string.unableToConnectVkms));

            Log.e(" AsyncResetMPin  doin bakcground catch", e.getMessage());


        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        progress.dismiss();
        try {


//            if (name2.equals("No data found.")) {
//                AlertDialogBoxInfo.alertDialogShow(mContext, "No data found.");
//
//            }
//            else if(diplayServerResopnse.equals("Service from Vakrangee Kendra unavailable. Please try again later."))
//            {
//                AlertDialogBoxInfo.alertDialogShow(mContext, mContext.getResources().getString(R.string.unableToConnectVkms));
//            }
//            else {
//                delegate.processFinish(diplayServerResopnse);
//            }

            if (diplayServerResopnse.equals("Service from Vakrangee Kendra unavailable. Please try again later.")) {
                //  AlertDialogBoxInfo.alertDialogShow(mContext, mContext.getResources().getString(R.string.unableToConnectVkms));
                //Toast.makeText(mContext, "Soory !!!", Toast.LENGTH_SHORT).show();
            } else {

                StringTokenizer st1 = new StringTokenizer(diplayServerResopnse, "|");
                String name1 = st1.nextToken();
                String name2 = st1.nextToken();
                if (name2.equals("No data found.")) {
                    AlertDialogBoxInfo.alertDialogShow(mContext, "No data found.");

                } else {
                    delegate.processFinish(diplayServerResopnse);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            //AlertDialogBoxInfo.alertDialogShow(mContext, mContext.getResources().getString(R.string.Warning));
            //AlertDialogBoxInfo.alertDialogShow(mContext, "Please re-press it");
        }


    }


}
