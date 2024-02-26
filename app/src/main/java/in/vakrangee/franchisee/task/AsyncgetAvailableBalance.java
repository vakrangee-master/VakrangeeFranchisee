package in.vakrangee.franchisee.task;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.ksoap2.serialization.SoapPrimitive;

import java.util.StringTokenizer;

import in.vakrangee.supercore.franchisee.ifc.ServiceProviderIfc;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.webservice.WebService;

/**
 * Created by nileshd on 5/4/2016.
 */
public class AsyncgetAvailableBalance extends AsyncTask<Void, Void, Void> {
    String TAG = "Response";
    String diplayServerResopnse;
    private Context context;
    SoapPrimitive soapPrimitive;
    private TelephonyManager telephonyManager;
    public ServiceProviderIfc delegate = null;

    public AsyncgetAvailableBalance(Context context) {

        // this.soapPrimitive = context1;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        Log.i(TAG, "onPreExecute");
    }


    @Override
    protected Void doInBackground(Void... voids) {
        Log.i(TAG, "doInBackground");
        getAvailableBalance();
        Log.i("", "Some not null string");
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        Log.i(TAG, "onPostExecute");
        // progress.dismiss();
        //  Toast.makeText(RegisterPageActivity.this, "Response aa" + diplayServerResopnse, Toast.LENGTH_SHORT).show();
        //displaydata.setText(diplayServerResopnse);

        try {

            if (diplayServerResopnse.equals("Invalid Request.")) {
                //getBalance.setText("Invalid Request.");
                Log.e(TAG + "Invalid Request.", diplayServerResopnse);
                delegate.getAvailableBalance(diplayServerResopnse);
            }
            // progress.dismiss();
            StringTokenizer tokens = new StringTokenizer(diplayServerResopnse, "|");
            String first = tokens.nextToken();
            String second = tokens.nextToken();

            // OKAY|0.00


            if (diplayServerResopnse == null) {

                Log.e(TAG + "Error", diplayServerResopnse);

            } else if (second.equals("Invalid Request.")) {
                // getBalance.setText("Invalid Request.");
                Log.e(TAG + "Invalid Request.", diplayServerResopnse);
                delegate.getAvailableBalance(diplayServerResopnse);


            } else if (first.equals("OKAY")) {

                Log.e(TAG + "balace", first);
                delegate.getAvailableBalance(diplayServerResopnse);

                //getBalance.setText(getResources().getString(R.string.rs) + second + "");

            } else if (second != null) {

                delegate.getAvailableBalance(diplayServerResopnse);

                Log.e(TAG + "Avalibale balance", second);
            }

        } catch (Exception e) {
            Log.d(TAG, "Error" + e);
            // AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));

        }
    }

    private void getAvailableBalance() {
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        Connection connection = new Connection(context);
        String getVkid = connection.getVkid();
        String getTokenId = connection.getTokenId();

        String vkid = EncryptionUtil.encryptString(getVkid, context);
        String token = EncryptionUtil.encryptString(getTokenId, context);

        String deviceIdget = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        String deviceid = EncryptionUtil.encryptString(deviceIdget, context);

        String deviceIDAndroid = CommonUtils.getAndroidUniqueID(context);
        String imei = EncryptionUtil.encryptString(deviceIDAndroid, context);

        String simSerial = CommonUtils.getSimSerialNumber(context);
        String simopertaor = EncryptionUtil.encryptString(simSerial, context);


        try {


            diplayServerResopnse = WebService.getAvailableBalance(vkid, token, imei, deviceid, simopertaor);

            Log.d(TAG, "WebSer...ceResponse: " + diplayServerResopnse);
        } catch (Exception e) {
            // AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
            e.printStackTrace();

        }

    }


}
