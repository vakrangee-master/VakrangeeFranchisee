package in.vakrangee.franchisee.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.StringTokenizer;

import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.activity.DashboardActivity;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.webservice.WebService;

/**
 * Created by nileshd on 9/16/2016.
 */
public class AsyncLoginUser extends AsyncTask<String, Void, String> {

    Context mContext;
    TelephonyManager telephonyManager;
    String diplayServerResopnse;
    ProgressDialog progress;
    String from;
    Intent intent;

    public AsyncLoginUser(@NonNull Context context) {
        super();
        mContext = context;

        try {
            intent = ((Activity) mContext).getIntent();
            from = intent.getStringExtra("FROM");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            String vkid = values[0];
            String MPin = values[1];

            String imei = values[2];
            String deviceid = values[3];
            String simserialnumber = values[4];


            Connection connection = new Connection(mContext);
            //connection.openDatabase();

            boolean tid = connection.getTokenIdisNull();


            if (tid) {

                String gettokenIda = EncryptionUtil.encryptString(connection.getTokenId(), mContext);
                Log.e("WithToken ID ", connection.getTokenId());
                diplayServerResopnse = WebService.authenticateFranchisee(vkid, MPin, gettokenIda, imei, deviceid, simserialnumber);


            } else {

                diplayServerResopnse = WebService.authenticateFranchiseewithoutTid(vkid, MPin, imei, deviceid, simserialnumber);
                Log.e("WithOut TokenID ", "No Token ID");

            }
            Log.e("diplayServerResopnse ", diplayServerResopnse);


        } catch (Exception e) {
            Log.e("TAG", "Error:in LoginPage " + e.getMessage());


            Log.e(" AsyncLogin  catch", e.getMessage());
            e.printStackTrace();
            AlertDialogBoxInfo.alertDialogShow(mContext, mContext.getResources().getString(R.string.Warning));


        }


        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        try {

            StringTokenizer tokens = new StringTokenizer(diplayServerResopnse, "|");
            String first = tokens.nextToken();

            progress.dismiss();

            /**
             * METHOD: authenticateFranchisee, authenticateFranchiseeWithToken
             * RESPONSE:
             * 1. OKAY
             * 2. ERROR             : Your account has been Deactivated.
             * 3. Invalid request.  : Invalid VKID or Password
             * 4. Decryption fail.  : Your account has been Deactivated.
             */
            if (diplayServerResopnse == null) {
                Log.e("Null  Loginpage -", diplayServerResopnse);

            } else if (first.equals("OKAY")) {

                Log.e("okay in Loginpage -", diplayServerResopnse);

                Connection connection = new Connection(mContext);
                // connection.openDatabase();
                String tokenId = connection.getTokenId();
                boolean tid = connection.getTokenIdisNull();
                if (tid) {
                    try {
                        StringTokenizer display = new StringTokenizer(diplayServerResopnse, "|");
                        String status = display.nextToken();
                        String token = display.nextToken();
                        connection.TrackingUpdate(token);
                        // If Application is open from Notification
                        if (from != null && from.equalsIgnoreCase("APP_NOTIFICATION")) {
                            ((Activity) mContext).setResult(((Activity) mContext).RESULT_OK,
                                    intent);
                            ((Activity) mContext).finish();
                        } else {
                            Intent i = new Intent(mContext, DashboardActivity.class);

                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            mContext.startActivity(i);
                        }

                        Log.e("okay with Tracking Update -", diplayServerResopnse);
                    } catch (Exception e) {
                        e.getMessage();
                    }
                } else {

                    try {
                        StringTokenizer display = new StringTokenizer(diplayServerResopnse, "|");
                        String status = display.nextToken();
                        String token = display.nextToken();
                        String trackingId = display.nextToken();

                        String second = tokens.nextToken();
                        Connection c = new Connection(mContext);
                        c.UpdatepasswordTokenId(second);
                        c.TrackingUpdate(trackingId);

                        // If Application is open from Notification
                        if (from != null && from.equalsIgnoreCase("APP_NOTIFICATION")) {
                            ((Activity) mContext).setResult(((Activity) mContext).RESULT_OK,
                                    intent);
                            ((Activity) mContext).finish();
                        } else {
                            // Start Dashboard
                            Intent i = new Intent(mContext, DashboardActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            mContext.startActivity(i);
                        }

                        Log.e("okay with Tracking Update or TokenID -", diplayServerResopnse);
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }

                // Update FCMId to server
                new AsyncUpdateFCMId(mContext).execute();

            } else if (first.equals("ERROR")) {

                AlertDialogBoxInfo.alertDialogShow(mContext, mContext.getResources().getString(R.string.accountdeactive));


            } else if (diplayServerResopnse.equals("Invalid request.")) {

                AlertDialogBoxInfo.alertDialogShow(mContext, mContext.getResources().getString(R.string.usernameNotMatch));


            } else if (diplayServerResopnse.equals("Decryption fail.")) {

                AlertDialogBoxInfo.alertDialogShow(mContext, mContext.getResources().getString(R.string.accountdeactive));


            } else {
                AlertDialogBoxInfo.alertDialogShow(mContext, mContext.getResources().getString(R.string.unableToConnectVkms));
            }

        } catch (Exception e) {


            Log.e(" AsyncLogin  postbackground catch", e.getMessage());
            e.printStackTrace();
            Log.e("catch in LoginPage", e.getMessage());
            AlertDialogBoxInfo.alertDialogShow(mContext, mContext.getResources().getString(R.string.Warning));
        }

    }
}
