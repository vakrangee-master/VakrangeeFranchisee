package in.vakrangee.franchisee.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.StringTokenizer;

import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.utils.Logger;
import in.vakrangee.supercore.franchisee.webservice.WebService;

//

public class MpinAcitivty extends AppCompatActivity {

    private static final String TAG = MpinAcitivty.class.getCanonicalName();

    EditText edtRePassword1, edtRePassword2;
    Button btnPasswordCheck;
    TelephonyManager telephonyManager;
    String diplayServerResopnse;
    //String TAG = "Response";
    ProgressDialog progress;
    Toolbar toolbar;
    String getVkid;
    InternetConnection internetConnection;
    private boolean isMinimized = false;

    private AsyncUpdateMPin asyncUpdateMPin;
    private Logger logger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fontawesome-webfont.ttf");
        setContentView(R.layout.acitivty_set_mpin);

        logger = Logger.getInstance(MpinAcitivty.this);

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        edtRePassword1 = (EditText) findViewById(R.id.rePassword1);
        edtRePassword1.setTypeface(Typeface.SANS_SERIF);
        edtRePassword2 = (EditText) findViewById(R.id.rePassword2);
        edtRePassword2.setTypeface(Typeface.SANS_SERIF);

        btnPasswordCheck = (Button) findViewById(R.id.passwordcheck);

        btnPasswordCheck.setTypeface(font);
        btnPasswordCheck.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " + getResources().getString(R.string.passwordVerify) + ""));

        internetConnection = new InternetConnection(MpinAcitivty.this);
        Connection connection = new Connection(this);
        //connection.openDatabase();


        TextView mpinset = (TextView) findViewById(R.id.mpinset);
        mpinset.setTypeface(Typeface.SANS_SERIF);

        getVkid = connection.getVkid(); //this is the method to query
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(R.string.app_name);
//Mobile Password(Mpin) will be used for Login as Password.


        edtRePassword1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i != 6) {
                    // Toast.makeText(getApplicationContext(), "Please Enter 10 digits", Toast.LENGTH_SHORT).show();
                    edtRePassword1.setTextColor(Color.parseColor("#000000"));
                    edtRePassword1.setError("Enter 6 Digits Password");

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                {


                    if (edtRePassword1.length() <= 5) {


                    } else {
                        edtRePassword1.setTextColor(Color.parseColor("#468847"));
                        edtRePassword1.setError(null);

                    }
                }
            }
        });


        edtRePassword2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i != 6) {
                    // Toast.makeText(getApplicationContext(), "Please Enter 10 digits", Toast.LENGTH_SHORT).show();
                    edtRePassword2.setTextColor(Color.parseColor("#000000"));
                    edtRePassword2.setError("Enter 6 Digits Password");

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                {


                    if (edtRePassword2.length() <= 5) {


                    } else {
                        edtRePassword2.setTextColor(Color.parseColor("#468847"));
                        edtRePassword2.setError(null);

                    }
                }
            }
        });


        btnPasswordCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String p1 = edtRePassword1.getText().toString();
                String p2 = edtRePassword2.getText().toString();

                if (p1.length() == 0) {
                    edtRePassword1.setError(getResources().getString(R.string.enterMpin));
                } else if (p2.length() == 0) {
                    edtRePassword2.setError(getResources().getString(R.string.confrimPassword));
                } else if (!p1.equals(p2)) {
                    edtRePassword2.setError(getResources().getString(R.string.passwordNotMatch));

                } else if (!isValidPassword(p2)) {
                    edtRePassword2.setError(getResources().getString(R.string.enterMpin));
                } else if (internetConnection.isConnectingToInternet() == false) {

                    AlertDialogBoxInfo.alertDialogShow(MpinAcitivty.this, getResources().getString(R.string.internetCheck));

                } else if (telephonyManager.getSimState() == TelephonyManager.SIM_STATE_ABSENT) {

                    AlertDialogBoxInfo.alertDialogShow(MpinAcitivty.this, getResources().getString(R.string.insertSimcard));

                } else {
//
                    asyncUpdateMPin = new AsyncUpdateMPin();
                    asyncUpdateMPin.execute();
                    //Toast.makeText(MpinAcitivty.this, "sucesssssfull", Toast.LENGTH_SHORT).show();
                    progress = new ProgressDialog(MpinAcitivty.this);
                    progress.setTitle(R.string.mpinSet);
                    progress.setMessage(getResources().getString(R.string.pleaseWait));
                    progress.setCancelable(false);
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.show();

                }
            }
        });
    }

    private boolean isValidPassword(String p2) {
        if (p2 != null && p2.length() > 5) {
            return true;
        }
        return false;
    }


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    private class AsyncUpdateMPin extends AsyncTask<Void, Void, Void> {
        String re1 = edtRePassword1.getText().toString();
        String re2 = edtRePassword2.getText().toString();

        //this is the method to query

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {
                String vkid = getVkid;
                String vkidd = EncryptionUtil.encryptString(vkid, getApplicationContext());
                String Mpin = EncryptionUtil.encryptString(re2, getApplicationContext());

                final String deviceIdget = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                String deviceid = EncryptionUtil.encryptString(deviceIdget, getApplicationContext());

                String deviceIDAndroid = CommonUtils.getAndroidUniqueID(getApplicationContext());
                String imei = EncryptionUtil.encryptString(deviceIDAndroid, getApplicationContext());

                String simSerial = CommonUtils.getSimSerialNumber(getApplicationContext());
                String simserialnumber = EncryptionUtil.encryptString(simSerial, getApplicationContext());


                diplayServerResopnse = WebService.updateMPin(vkidd, Mpin, imei, deviceid, simserialnumber);
                Log.e("diplayServerResopnse", diplayServerResopnse);

            } catch (Exception e) {
                e.printStackTrace();
                Log.e(" AsyncUpdateMPin  catch", e.getMessage());
                // AlertDialogBoxInfo.alertDialogShow(MpinAcitivty.this, getResources().getString(R.string.Warning));
                logger.writeError(TAG, "Error in upating mpin: " + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            progress.dismiss();
            if (TextUtils.isEmpty(diplayServerResopnse)) {
                AlertDialogBoxInfo.alertDialogShow(MpinAcitivty.this, getResources().getString(R.string.Warning));
                return;
            }

            try {
                Log.i(TAG, "onPostExecute");

                final StringTokenizer tokens = new StringTokenizer(diplayServerResopnse, "|");
                final String first = tokens.nextToken();
                final String second = tokens.nextToken();

                /*if (diplayServerResopnse == null) {
                    //String message = null;
                    //Log.e("TAG", ((message == null) ? "string null" : message));
                } else */

                if ("OKAY".equals(first)) {

                    Connection c = new Connection(MpinAcitivty.this);
                    c.UpdatepasswordTokenId(second);
                    c.setTokenIdnull();


                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MpinAcitivty.this);
                    alertDialogBuilder
                            .setMessage(R.string.congratulations)
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    Intent intent = new Intent(MpinAcitivty.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();

                                    dialog.dismiss();
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();

                    alertDialog.show();
                    TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
                    textView.setTextSize(16);


                    Log.e(TAG + "Suucffull set MPin", diplayServerResopnse);


                } else {
                    Log.e(TAG + "Error in Server", diplayServerResopnse);

                }
            } catch (Exception e) {

                Log.e(" AsyncUpdateMPin postexecute catch", e.toString());
                e.printStackTrace();
                AlertDialogBoxInfo.alertDialogShow(MpinAcitivty.this, getResources().getString(R.string.Warning));

            }


        }

    }

    public void onBackPressed() {
        Intent mainActivity = new Intent(Intent.ACTION_MAIN);
        mainActivity.addCategory(Intent.CATEGORY_HOME);
        mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainActivity);
        moveTaskToBack(true);
        finish();


    }

    @Override
    public void onPause() {
        super.onPause();
        //  isMinimized = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        // isMinimized = true;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (asyncUpdateMPin != null && !asyncUpdateMPin.isCancelled()) {
            asyncUpdateMPin.cancel(true);
        }
    }
}
