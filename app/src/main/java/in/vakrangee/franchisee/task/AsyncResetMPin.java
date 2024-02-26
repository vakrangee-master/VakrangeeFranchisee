package in.vakrangee.franchisee.task;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

import org.slf4j.Logger;

import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.activity.OtpActitvity;
import in.vakrangee.franchisee.activity.RegisterPageActivity;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.webservice.WebService;

/**
 * Created by nileshd on 9/16/2016.
 */
public class AsyncResetMPin extends AsyncTask<String, Void, String> {
    Context mContext;
    TelephonyManager telephonyManager;
    String diplayServerResopnse;
    private static final String TAG = "Result";
    Logger log;
    ProgressDialog progress;

    public AsyncResetMPin(Context context) {
        super();
        mContext = context;
    }
    //this is the method to query

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
        Log.i(TAG, "doInBackground");
        try {

            String vkid = values[0];
            String imei = values[1];

            String deviceid = values[2];
            String simserialnumber = values[3];


            diplayServerResopnse = WebService.resetMPin(vkid, imei, deviceid, simserialnumber);
            Log.e("diplayServerResopnse", diplayServerResopnse);

        } catch (Exception e) {
            AlertDialogBoxInfo.alertDialogShow(mContext, mContext.getResources().getString(R.string.unableToConnectVkms));
            log.error(" AsyncResetMPin doin background catch" + e.getMessage());
            Log.e(" AsyncResetMPin  doin bakcground catch", e.getMessage());
            e.printStackTrace();
            String message = null;
            Log.e(TAG + "Error", message);
            Log.e("TAG", ((message == null) ? "string null" : message));

        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            Log.i(TAG, "onPostExecute");


//                final StringTokenizer tokens = new StringTokenizer(diplayServerResopnse, "|");
//                String first = tokens.nextToken();
//                final String second = tokens.nextToken();
            progress.dismiss();

            if (diplayServerResopnse == null) {


                Log.e("Null  in Loginpage -", diplayServerResopnse);

            } else if ("OKAY".equals(diplayServerResopnse)) {


                Connection connection = new Connection(mContext);
                //connection.openDatabase();
                connection.resetMpin();
                Intent intent = new Intent(mContext, OtpActitvity.class);
                mContext.startActivity(intent);
            } else if (diplayServerResopnse.equals("Invalid Request.")) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

                alertDialogBuilder

                        .setMessage(R.string.primarysim)
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
                        progress.dismiss();
                    }
                });


                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
                textView.setTextSize(16);
                progress.dismiss();

            } else if (diplayServerResopnse.equals("ERROR|Account has been discontinued.")) {
                AlertDialogBoxInfo.alertDialogShow(mContext, mContext.getResources().getString(R.string.accountdeactive));

            } else {
                Log.e(TAG + "Error in Server", diplayServerResopnse);
                AlertDialogBoxInfo.alertDialogShow(mContext, mContext.getResources().getString(R.string.Warning));

            }
        } catch (Exception e) {
            log.error(" AsyncResetMPin Postexecute catch" + e.getMessage());
            Log.e(" AsyncResetMPin Postexecute catch", e.getMessage());
            System.out.println(e.getMessage());
            e.printStackTrace();
            AlertDialogBoxInfo.alertDialogShow(mContext, mContext.getResources().getString(R.string.Warning));
        }


    }

}

