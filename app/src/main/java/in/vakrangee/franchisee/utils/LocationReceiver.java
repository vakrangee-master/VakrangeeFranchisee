package in.vakrangee.franchisee.utils;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import in.vakrangee.supercore.franchisee.impl.ServiceProviderImpl;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.webservice.WebService;

/**
 * Created by Nileshd on 8/10/2016.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class LocationReceiver extends BroadcastReceiver {


    String latitu, longit, accurcy, getAltitude;
    Connection connection;
    TelephonyManager telephonyManager;
    String diplayServerResopnse;
    Context context;
    String TAG = "Response";
    String vkdi, lat, laong, acc, alti, imei, deviceid, simserialnumber, getdate, batterylevel, singleStrngth;
    public static final int UNKNOW_CODE = 99;
    int MAX_SIGNAL_DBM_VALUE = 31;
    String strength;
    myPhoneStateListener psListener;
    TextView txtSignalStr;
    static int Signal;

    @Override
    public void onReceive(final Context context, final Intent calledIntent) {
        Log.d("LOC_RECEIVER", "Location RECEIVED!");


        latitu = calledIntent.getStringExtra("latitude");
        longit = calledIntent.getStringExtra("longitude");
        accurcy = calledIntent.getStringExtra("getAccuracy");
        getAltitude = calledIntent.getStringExtra("getAltitude");

        //Connection connection = new Connection(context);
        //connection.openDatabase();
        String status = new ServiceProviderImpl().getCurrentStatus();

        if (status.equals("NA")) {

        } else if (status.equals("P")) {

        } else if (status.equals("OTP_V")) {

        } else if (status.equals("Y")) {
            try {

                connection = new Connection(context);

                telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

                String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                deviceid = EncryptionUtil.encryptString(deviceId, context);

                String imeiDevice = CommonUtils.getAndroidUniqueID(context);
                imei = EncryptionUtil.encryptString(imeiDevice, context);

                String simSerial = CommonUtils.getSimSerialNumber(context);
                simserialnumber = EncryptionUtil.encryptString(simSerial, context);

                psListener = new myPhoneStateListener();

                telephonyManager.listen(psListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);


//                android.telephony.CellInfoWcdma cellinfogsm = (android.telephony.CellInfoWcdma) telephonyManager.getAllCellInfo().get(0);
//                CellSignalStrengthWcdma cellSignalStrengthWcdma = cellinfogsm.getCellSignalStrength();
//                String netwokr = String.valueOf(cellSignalStrengthWcdma.getDbm());


                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                getdate = EncryptionUtil.encryptString(df.format(c.getTime()), context);


                String vk = connection.getVkid();

                vkdi = EncryptionUtil.encryptString(vk, context);

                int single = Signal;
                String battery = batteryLevel(context);
                singleStrngth = EncryptionUtil.encryptString(String.valueOf(Signal), context);
                lat = EncryptionUtil.encryptString(latitu, context);
                //Toast.makeText(context.getApplicationContext(), latitu, Toast.LENGTH_SHORT).show();
                laong = EncryptionUtil.encryptString(longit, context);
                acc = EncryptionUtil.encryptString(accurcy, context);
                alti = EncryptionUtil.encryptString(getAltitude, context);


// for example value of first element


                batterylevel = EncryptionUtil.encryptString(battery, context);
                Log.e("VKID  " + vk + "  TIME  " + c.getTime() + "   Lat   ", latitu + "   - Long  " + longit + "   getAccuracy " + accurcy
                        + "  getAltitude data  " + getAltitude + "  Battery Status  " + battery + " Single " + Signal);

                if (latitu == null) {
                    Log.e("log null ", "No lat Long found");

                } else {
                    try {


//
                        String trackingID = connection.getTrackingID();
                        if (trackingID.equals("Y")) {
                            AsyncLocationTracking myRequest = new AsyncLocationTracking();
                            myRequest.execute();
                            Log.e("log ", "call Aynsk" + trackingID);
                        } else if (trackingID.equals("N")) {
                            Log.e("No tracking ", "" + trackingID);
                        } else {
                            Log.e("No tracking goto Else", "" + trackingID);
                        }


                    } catch (Exception e) {
                        e.getMessage();
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
                e.getMessage();
            }
        } else {

        }


    }

    public static String batteryLevel(Context context) {
        Intent intent = context.getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
        int percent = (level * 100) / scale;
        return String.valueOf(percent) + "%";
    }

    public static String convertDate(String dateInMilliseconds, String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }

    private class AsyncLocationTracking extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {


                diplayServerResopnse = WebService.insertFFLocationLog(vkdi, imei, deviceid, simserialnumber, getdate,
                        lat, laong, acc, alti, batterylevel, singleStrngth);
                Log.d(TAG, "WebSer...ceResponse: " + diplayServerResopnse);

            } catch (Exception e) {

                String message = null;
                Log.i("TAG", ((message == null) ? "string null" : message));
                // AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));

            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");

            try {
                String tokens = diplayServerResopnse;

                // StringTokenizer display = new StringTokenizer(diplayServerResopnse, "|");
                // String status = display.nextToken();
                // String token = display.nextToken();

                Log.e("diplayServerResopnse LocationReceiver", tokens);
                if (diplayServerResopnse.equals("Decryption fail.")) {

                } else if (diplayServerResopnse.equals("OKAY")) {


                } else {

                }

            } catch (Exception e) {
                e.printStackTrace();
                // AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
            }
        }

    }

    private int calculateSignalStrengthInPercent(int signalStrength) {
        return (int) ((float) signalStrength / MAX_SIGNAL_DBM_VALUE * 100);
    }

    public class myPhoneStateListener extends PhoneStateListener {
        public int signalStrengthValue;

        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            if (signalStrength.isGsm()) {
                if (signalStrength.getGsmSignalStrength() != 99)
                    signalStrengthValue = signalStrength.getGsmSignalStrength() * 2 - 113;
                else
                    signalStrengthValue = signalStrength.getGsmSignalStrength();
            } else {
                signalStrengthValue = signalStrength.getCdmaDbm();
            }
            int Signalval = signalStrengthValue;

            Signal = Signalval;

        }
    }

}
