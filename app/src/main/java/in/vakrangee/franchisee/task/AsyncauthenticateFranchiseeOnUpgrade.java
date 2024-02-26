package in.vakrangee.franchisee.task;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

import java.util.StringTokenizer;

import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.activity.DashboardActivity;
import in.vakrangee.franchisee.activity.RegisterPageActivity;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.webservice.WebService;

/**
 * Created by nileshd on 10/17/2016.
 */
public class AsyncauthenticateFranchiseeOnUpgrade extends AsyncTask<String, Void, String> {

    final static String TAG = AsyncauthenticateFranchiseeOnUpgrade.class.getCanonicalName();

    Context mContext;
    TelephonyManager telephonyManager;
    String diplayServerResopnse;
    ProgressDialog progress;
    String from;
    Intent intent;

    public AsyncauthenticateFranchiseeOnUpgrade(Context context) {
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
            diplayServerResopnse = WebService.authenticateFranchiseeOnUpgrade(vkid, MPin, imei, deviceid, simserialnumber);


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
            progress.dismiss();
            StringTokenizer tokens = new StringTokenizer(diplayServerResopnse, "|");
            String okay = tokens.nextToken();

            /**
             * METHOD: authenticateFranchiseeOnUpgrade
             * RESPONSE:
             * 1. OKAY
             * 2. ERROR             : Your account has been Deactivated.
             * 3. Invalid Request.. : Invalid VKID or Password
             * 4. Invalid request.  : This device is not registered with us. Kindly register.
             *
             */
            if (diplayServerResopnse == null) {

                String message = null;
                Log.e("TAG", ((message == null) ? "string null" : message));

            } else if (okay.equals("ERROR")) {

                AlertDialogBoxInfo.alertDialogShow(mContext, mContext.getResources().getString(R.string.accountdeactive));


            } else if (diplayServerResopnse.equals("Invalid Request..")) {

                AlertDialogBoxInfo.alertDialogShow(mContext, mContext.getResources().getString(R.string.usernameNotMatch));


            } else if (diplayServerResopnse.equals("Invalid request.")) {

                //  AlertDialogBoxInfo.alertDialogShow(mContext, mContext.getResources().getString(R.string.notrgister));

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

                alertDialogBuilder

                        .setMessage(R.string.notrgister)
                        .setCancelable(false)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent i = new Intent(mContext, RegisterPageActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                mContext.startActivity(i);


                            }

                        });

                alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
                textView.setTextSize(16);

            } else if (okay.equals("Invalid request.")) {

                AlertDialogBoxInfo.alertDialogShow(mContext, mContext.getResources().getString(R.string.notrgister));

            } else if (okay.equals("OKAY")) {

                String token = tokens.nextToken();
                String vkid = tokens.nextToken();
                String name = tokens.nextToken();
                String mail = tokens.nextToken();
                String mobileid = tokens.nextToken();
                String trackingID = tokens.nextToken();
                Log.e("okay in Loginpage -", diplayServerResopnse);
                Connection connection = new Connection(mContext);
                // connection.openDatabase();
                connection.TrackingUpdate(trackingID);
                connection.insertIntoFranchiseeOnUpgrade(vkid, name, mail, mobileid, token);

                if (from != null && from.equalsIgnoreCase("APP_NOTIFICATION")) {
                    ((Activity) mContext).setResult(((Activity) mContext).RESULT_OK,
                            intent);
                    ((Activity) mContext).finish();
                } else {
                    // Start Dashboard Activity
                    Intent i = new Intent(mContext, DashboardActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.startActivity(i);
                }

                // Update FCMId to server
                new AsyncUpdateFCMId(mContext).execute();


            } else {
                AlertDialogBoxInfo.alertDialogShow(mContext, mContext.getResources().getString(R.string.unableToConnectVkms));
            }

        } catch (Exception e) {


            //  Log.e(" AsyncLogin  postbackground catch", e.getMessage());
            //  e.printStackTrace();
            // Log.e("catch in LoginPage", e.getMessage());
            AlertDialogBoxInfo.alertDialogShow(mContext, mContext.getResources().getString(R.string.Warning));
        }

    }
}
