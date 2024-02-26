package in.vakrangee.franchisee.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import in.vakrangee.franchisee.BuildConfig;
import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.service.OnClearFromRecentService;
import in.vakrangee.franchisee.utils.TimeService;
import in.vakrangee.supercore.franchisee.application.VakrangeeKendraApplication;
import in.vakrangee.supercore.franchisee.impl.ServiceProviderImpl;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.utils.Logger;
import in.vakrangee.supercore.franchisee.webservice.WebService;

//

public class MainActivity extends AppCompatActivity {

    static final String TAG = MainActivity.class.getCanonicalName();

    private static Context mContext;
    private static int TIME_OUT = 1000; //Time to launch the another activity
    String diplayServerResopnse;
    Connection connection;
    //String TAG = "Response";
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private boolean isMinimized = false;

    ProgressDialog progress;
    InternetConnection internetConnection;
    TelephonyManager telephonyManager;
    private TextView txtVersion;
    Logger logger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (VakrangeeKendraApplication.getStrictMode() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }


        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);

        //Version Name
        txtVersion = findViewById(R.id.txtVersion);
        txtVersion.setText("v" + BuildConfig.VERSION_NAME);

        logger = Logger.getInstance(mContext);

        //---background timeserivce call when GPS location
        if (checkAndRequestPermissions()) {
            // carry on the normal flow, as the case of  permissions  granted.
        }

        // Start Service To Clean Token if app is close via swipe.
        startService(new Intent(getBaseContext(), OnClearFromRecentService.class));

        Intent serviceIntent = new Intent(getApplicationContext(), TimeService.class);
        serviceIntent.putExtra("VKID", connection.getVkid());
        startService(serviceIntent);

        internetConnection = new InternetConnection(MainActivity.this);
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        try {

            if (internetConnection.isConnectingToInternet() == false) {
                AlertDialogBoxInfo.alertDialogShow(MainActivity.this, getResources().getString(R.string.internetCheck));

            } else if (telephonyManager.getSimState() == TelephonyManager.SIM_STATE_ABSENT) {

                AlertDialogBoxInfo.alertDialogShow(MainActivity.this, getResources().getString(R.string.insertSimcard));

            } else {

                if (internetConnection.isNetworkAvailable(getApplicationContext()) == false) {

                    AlertDialogBoxInfo.alertDialogShow(MainActivity.this, getResources().getString(R.string.internetCheck));

                } else if (telephonyManager.getSimState() == TelephonyManager.SIM_STATE_ABSENT) {

                    AlertDialogBoxInfo.alertDialogShow(MainActivity.this, getResources().getString(R.string.insertSimcard));

                } else {
                    AsyncCheckVersion myRequest = new AsyncCheckVersion();
                    myRequest.execute();
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
            logger.writeError(TAG, "Error in MainActivity: " + e.toString());
        }
    }

    private boolean checkAndRequestPermissions() {

        try {


            int permissionSendMessage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
            int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            int ExternalPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            List<String> listPermissionsNeeded = new ArrayList<>();

            if (locationPermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (ExternalPermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
                return false;
            }
        } catch (Exception e) {
            e.getMessage();
            AlertDialogBoxInfo.alertDialogShow(MainActivity.this, getResources().getString(R.string.permissionnoallow));

        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            // Toast.makeText(getApplicationContext(), "Backpress ", Toast.LENGTH_SHORT).show();

            finish();
            System.exit(0);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();
        isMinimized = true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (isMinimized) {
            startActivity(new Intent(this, MainActivity.class));
            isMinimized = false;
            finish();
        }
    }

    private class AsyncCheckVersion extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {

                String mystring = BuildConfig.VERSION_NAME;
                String versioncode = EncryptionUtil.encryptString(mystring, getApplicationContext());


                Log.e("AsyncCheckVersion", mystring);

                diplayServerResopnse = WebService.checkVersion(versioncode);
            } catch (Exception e) {


                Log.e("AsyncCheckVersion catch ", e.getMessage());
                Log.e("diplayServerResopnse ", diplayServerResopnse);
                AlertDialogBoxInfo.alertDialogShow(MainActivity.this, getResources().getString(R.string.Warning));


            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            try {


                Log.e("AsyncCheckVersion", diplayServerResopnse);

                /**
                 * METHOD: checkVersion
                 * RESPONSE:
                 * 1. OKAY
                 * 2. OutdoorID from CyberPlat unavailable. Please try again later.             : Unable to connect VKMS Server Please Try Again
                 * 3. Invalid request.  : You are using outdated version of VKMS Application. Inorder to continue using the application upgrade to the latest version.
                 *    ELSE : Warning! Oops, some error encountered. Please try again.
                 */
                if (diplayServerResopnse == null) {

                    String message = null;
                    Log.e("TAG", ((message == null) ? "string null" : message));

                } else if (diplayServerResopnse.equals("OKAY")) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {


                            Connection connection = new Connection(MainActivity.this);
                            // connection.openDatabase();
                            String status = new ServiceProviderImpl().getCurrentStatus();


                            Log.e("mainactivity Reason ", status);
                            switch (status) {
                                case "P":
                                    //OTP not verified
                                    Intent intent = new Intent(MainActivity.this, OtpActitvity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                                    Log.e("OTp  ", "Goto OTP Acitity");
                                    // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();


                                    break;
                                case "OTP_V":
                                    //OTP verified password tobe set
                                    intent = new Intent(MainActivity.this, MpinAcitivty.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);

                                    Log.e("Mpin ", "Goto MpinAcitivty");
                                    finish();
                                    break;
                                case "Y":
                                    //Already register OTP verified and Mpin set


                                    intent = new Intent(MainActivity.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);

                                    Log.e("Login ", "Goto LoginActivity");
                                    finish();
                                    break;
                                default:
                                    //tobe Registered
                                    // intent = new Intent(MainActivity.this, RegisterPageActivity.class);
                                    intent = new Intent(MainActivity.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    //  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    // intent = new Intent(MainActivity.this, UploadImageActivity.class);
                                    startActivity(intent);

                                    Log.e("RegisterPage ", "Goto RegisterPageActivity");
                                    finish();
                                    break;
                            }
                        }
                    }, TIME_OUT);


                } else if (diplayServerResopnse.equals("OutdoorID from CyberPlat unavailable. Please try again later.")) {

                    Log.e("OutdoorID from CyberPlat unavailable ", diplayServerResopnse);
                    AlertDialogBoxInfo.alertDialogShow(MainActivity.this, getResources().getString(R.string.unableToConnectVkms));

                } else if (diplayServerResopnse.equals("Invalid request.")) {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                    builder1.setMessage(getResources().getString(R.string.updateApplication));
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                    try {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                    } catch (android.content.ActivityNotFoundException anfe) {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                    }
                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    Intent intent = new Intent(Intent.ACTION_MAIN);
                                    intent.addCategory(Intent.CATEGORY_HOME);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    alert11.setCancelable(false);

                } else {

                    AlertDialogBoxInfo.alertDialogShow(MainActivity.this, getResources().getString(R.string.Warning));
                    /*
                    log.info("You are using out date version of VKMS Application");
                    Log.e("You are using out date version of VKMS Application ", "Old version");
                    // AlertDialogBoxInfo.alertDialogShow(MainActivity.this, getResources().getString(R.string.updateApplication));

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                    builder1.setMessage(getResources().getString(R.string.updateApplication));
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                    try {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                    } catch (android.content.ActivityNotFoundException anfe) {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                    }
                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    Intent intent = new Intent(Intent.ACTION_MAIN);
                                    intent.addCategory(Intent.CATEGORY_HOME);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    */
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("catch in MainActivity", e.getMessage());

                Log.e("MainActivity catch ", e.getMessage());

                AlertDialogBoxInfo.alertDialogShow(MainActivity.this, getResources().getString(R.string.Warning));

            }

        }


    }

    public static Context getContext() {
        return mContext;
    }

}

