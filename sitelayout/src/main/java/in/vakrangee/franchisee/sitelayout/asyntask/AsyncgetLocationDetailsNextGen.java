package in.vakrangee.franchisee.sitelayout.asyntask;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import in.vakrangee.franchisee.sitelayout.R;
import in.vakrangee.supercore.franchisee.ifc.OnTaskCompleted;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.webservice.WebService;


public class AsyncgetLocationDetailsNextGen extends AsyncTask<String, Void, String> {


    Context mContext;
    TelephonyManager telephonyManager;
    String diplayServerResopnse;
    private static final String TAG = "Result";
    Logger log;
    ProgressDialog progress;
    String[] myVakrangeeKendraLocations;
    public OnTaskCompleted delegate = null;
    HashSet<String> words;

    int NEXTGEN_SITE_TYPE;

    public AsyncgetLocationDetailsNextGen(int type, Context context, String[] myVakrangeeKendraLocations) {
        super();
        mContext = context;
        this.myVakrangeeKendraLocations = myVakrangeeKendraLocations;
        this.delegate = (OnTaskCompleted) context;
        NEXTGEN_SITE_TYPE = type;
    }
    //this is the method to query

    @Override
    protected void onPreExecute() {
        Log.e("TAG", "onPreExecute");
        progress = new ProgressDialog(mContext);
        progress.setMessage(mContext.getResources().getString(R.string.pleaseWait));
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }

    @SuppressLint("LongLogTag")
    @Override
    protected String doInBackground(String... values) {
        Log.i(TAG, "doInBackground type:");
        try {

            String vkid = values[0];
            String TokenId = values[1];
            String imei = values[2];
            String deviceid = values[3];
            String simserialnumber = values[4];
            String scope = values[5];
            String id = values[6];

            if (Constants.ENABLE_FRANCHISEE_MODE) {
                diplayServerResopnse = WebService.myVakrangeeKendraLocationDetailsNextGen1(vkid, scope, id, String.valueOf(NEXTGEN_SITE_TYPE));
            } else {
                diplayServerResopnse = WebService.myVakrangeeKendraLocationDetailsNextGen(vkid, TokenId, imei, deviceid, simserialnumber, scope, id, String.valueOf(NEXTGEN_SITE_TYPE));
            }
            Log.e("diplayServerResopnse", diplayServerResopnse);

        } catch (Exception e) {
            Log.e(" AsyncResetMPin  doin bakcground catch", e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        progress.dismiss();
        try {

            if (diplayServerResopnse.equals("Service from Vakrangee Kendra unavailable. Please try again later.")) {
                //Do Nothing
            } else {

                StringTokenizer st1 = new StringTokenizer(diplayServerResopnse, "|");
                st1.nextToken();
                String name2 = st1.nextToken();
                if (name2.equals("No data found.")) {
                    AlertDialogBoxInfo.alertDialogShow(mContext, "No data found.");

                } else {
                    delegate.processFinish(diplayServerResopnse);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
