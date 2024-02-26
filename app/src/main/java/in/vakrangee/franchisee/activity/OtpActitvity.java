package in.vakrangee.franchisee.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.StringTokenizer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.webservice.WebService;

public class OtpActitvity extends AppCompatActivity {
    static EditText edtOtpEnter;
    Button btnOptVerified;
    String TAG = "Response";
    TelephonyManager telephonyManager;
    String diplayServerResopnse;
    ProgressDialog progress, progress1;
    String getVkid, getTokenId;
    InternetConnection internetConnection;
    private boolean isMinimized = false;
    WebView webView;
    Toolbar toolbar;
    TextView txtResendOTP, txtResendOTPResponse, txtTimerCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fontawesome-webfont.ttf");
        setContentView(R.layout.otp_actitvity);

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        internetConnection = new InternetConnection(OtpActitvity.this);
        Connection connection = new Connection(OtpActitvity.this);
        getVkid = connection.getVkid();


        TextView otpverification = (TextView) findViewById(R.id.otpverification);
        otpverification.setTypeface(Typeface.SANS_SERIF);

        txtResendOTP = (TextView) findViewById(R.id.resendOTP);
        txtResendOTP.setTypeface(Typeface.SANS_SERIF);

        txtResendOTPResponse = (TextView) findViewById(R.id.resendOTPresponse);
        txtResendOTPResponse.setTypeface(Typeface.SANS_SERIF);

        txtResendOTPResponse.setVisibility(View.INVISIBLE);


        txtTimerCount = (TextView) findViewById(R.id.timercount);
        txtTimerCount.setTypeface(Typeface.SANS_SERIF);


        CountDownTimer countDownTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                txtTimerCount.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {

                txtTimerCount.setVisibility(View.INVISIBLE);
                txtResendOTP.setVisibility(View.VISIBLE);
            }
        }.start();


        edtOtpEnter = (EditText) findViewById(R.id.inputOtp);
        edtOtpEnter.requestFocus();
        edtOtpEnter.setTypeface(Typeface.SANS_SERIF);

        btnOptVerified = (Button) findViewById(R.id.optcheck);
        btnOptVerified.setTypeface(font);
        btnOptVerified.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " + getResources().getString(R.string.verifyOTP)));


        txtResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AsyncResendOTP myRequest = new AsyncResendOTP();
                //UserAlreadyRegister myRequest = new UserAlreadyRegister();
                myRequest.execute();

                progress1 = new ProgressDialog(OtpActitvity.this);
                progress1.setTitle(R.string.resendOTP);
                progress1.setMessage(getResources().getString(R.string.pleaseWait));
                progress1.setCancelable(false);
                progress1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress1.show();


            }
        });
        edtOtpEnter.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2,
                                      int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (edtOtpEnter.getText().toString().contains(" ")) {
                    edtOtpEnter.setText(edtOtpEnter.getText().toString().replaceAll(" ", ""));
                    edtOtpEnter.setSelection(edtOtpEnter.getText().length());

                    //Toast.makeText(getApplicationContext(), "No Spaces Allowed", Toast.LENGTH_LONG).show();
                }
            }
        });


        webView = (WebView) findViewById(R.id.webView);

        webView.setVerticalScrollBarEnabled(true);
        webView.setHorizontalScrollBarEnabled(true);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(R.string.app_name);


        String str = "<font color='gray', font style ='Typeface.SANS_SERIF'><html><body><ul><li>An OTP has been send your Vakrangee Email ID and Registered Mobile Number.</li><li>For dual SIM phone, Please keep the Default SIM as the one which is registered for Mobile Access.</li><li>Please keep your mobile data/ internet connection on.</li></ul></body></html></font>";

        webView.loadDataWithBaseURL(null, str, "text/html", "utf-8", null);


        View.OnFocusChangeListener ofcListener = new MyFocusChangeListener();
        edtOtpEnter.setOnFocusChangeListener(ofcListener);

        btnOptVerified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = edtOtpEnter.getText().toString();
                if (otp.trim().length() == 0) {
                    edtOtpEnter.setError(getResources().getString(R.string.enterOTP));
                } else if (otp.length() <= 7) {
                    edtOtpEnter.setError(getResources().getString(R.string.enterOTP8character));
                } else if (internetConnection.isConnectingToInternet() == false) {
                    AlertDialogBoxInfo.alertDialogShow(OtpActitvity.this, getResources().getString(R.string.internetCheck));

                } else if (telephonyManager.getSimState() == TelephonyManager.SIM_STATE_ABSENT) {
                    AlertDialogBoxInfo.alertDialogShow(OtpActitvity.this, getResources().getString(R.string.insertSimcard));

                } else {
//
                    AsyncOtpVerifyUser myRequest = new AsyncOtpVerifyUser();
                    //UserAlreadyRegister myRequest = new UserAlreadyRegister();
                    myRequest.execute();

                    progress = new ProgressDialog(OtpActitvity.this);
                    progress.setTitle(R.string.verifiedOTP);
                    progress.setMessage(getResources().getString(R.string.pleaseWait));
                    progress.setCancelable(false);
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.show();
                }
            }
        });
    }


    public static void updateMessageBox(String s) {

        try {
            //  messageBox.append(s);
            StringTokenizer tokens = new StringTokenizer(s, ".");
            String first = tokens.nextToken();
            String second = tokens.nextToken();

            String substring = first.substring(Math.max(first.length() - 8, 0));
            edtOtpEnter.setText(substring);

        } catch (Exception e) {

            e.printStackTrace();

            Log.e(" AsyncUpdateMPin  catch", e.getMessage());

        }

    }


    private class AsyncOtpVerifyUser extends AsyncTask<Void, Void, Void> {
        String Otp = edtOtpEnter.getText().toString();

        String vkid = getVkid;
        String vkidreg = EncryptionUtil.encryptString(vkid, getApplicationContext());
        String deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        String deviceid = EncryptionUtil.encryptString(deviceId, getApplicationContext());

        String imeiDevice = CommonUtils.getAndroidUniqueID(getApplicationContext());
        String imei = EncryptionUtil.encryptString(imeiDevice, getApplicationContext());

        String simserial = CommonUtils.getSimSerialNumber(getApplicationContext());
        String simserialnumber = EncryptionUtil.encryptString(simserial, getApplicationContext());

        String otpvalues = EncryptionUtil.encryptString(Otp, getApplicationContext());


        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {
                diplayServerResopnse = WebService.otpVerfiy(vkidreg, otpvalues, imei, deviceid, simserialnumber);
                Log.e("diplayServerResopnse - OTP", diplayServerResopnse);

            } catch (Exception e) {


                Log.e(" AsyncOtpVerifyUser  catch", e.getMessage());
                e.printStackTrace();
                Log.e("diplayServerResopnse", e.getMessage());
                String message = null;
                Log.i("TAG", ((message == null) ? "string null" : message));
                AlertDialogBoxInfo.alertDialogShow(OtpActitvity.this, getResources().getString(R.string.Warning));

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");


            progress.dismiss();

            try {


                /**
                 * METHOD: verifyOtp
                 * RESPONSE:
                 * 1. OKAY
                 * 2. OTP Verified.      : Warning! Oops, some error encountered. Please try again.
                 * 3. Invalid OTP.       : Invalid OTP Please Try Again
                 *    ELSE : Unable to connect VKMS Server Please Try Again.
                 */
                if (diplayServerResopnse == null) {

                    String message = null;
                    Log.i("TAG", ((message == null) ? "string null" : message));


                } else if (diplayServerResopnse.equals("OTP Verified.")) {
                    Connection connection = new Connection(OtpActitvity.this);
                    connection.UpdateOtp(getVkid);

                    Intent i = new Intent(OtpActitvity.this, MpinAcitivty.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();


                    Log.e(TAG + "OTP  verify", diplayServerResopnse);

                } else if (diplayServerResopnse.equals("Invalid OTP.")) {
                    AlertDialogBoxInfo.alertDialogShow(OtpActitvity.this, getResources().getString(R.string.invalidOTP));
                    edtOtpEnter.setText("");


                } else {
                    Log.e(TAG + "Error in Server", diplayServerResopnse);
                    AlertDialogBoxInfo.alertDialogShow(OtpActitvity.this, getResources().getString(R.string.unableToConnectVkms));


                    //  Toast.makeText(getApplicationContext(), "Error OTP ", Toast.LENGTH_SHORT).show();

                }

            } catch (Exception e) {

                Log.e(" AsyncOtpVerifyUser onPostExecute catch", e.getMessage());
                e.printStackTrace();
                Log.e("catchBlock- OTP", diplayServerResopnse);
                AlertDialogBoxInfo.alertDialogShow(OtpActitvity.this, getResources().getString(R.string.Warning));
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            // Toast.makeText(getApplicationContext(), "Backpress ", Toast.LENGTH_SHORT).show();

            finish();
            moveTaskToBack(true);
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onBackPressed() {
        Intent mainActivity = new Intent(Intent.ACTION_MAIN);
        mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mainActivity.addCategory(Intent.CATEGORY_HOME);
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
        //  isMinimized = true;
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


    private class MyFocusChangeListener implements View.OnFocusChangeListener {

        public void onFocusChange(View v, boolean hasFocus) {

            if (v.getId() == R.id.inputOtp && !hasFocus) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }
        }
    }

    private class AsyncResendOTP extends AsyncTask<Void, Void, Void> {


        String vkid = getVkid;


        String vkidreg = EncryptionUtil.encryptString(vkid, getApplicationContext());
        String deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        String deviceid = EncryptionUtil.encryptString(deviceId, getApplicationContext());

        String imeiDevice = CommonUtils.getAndroidUniqueID(getApplicationContext());
        String imei = EncryptionUtil.encryptString(imeiDevice, getApplicationContext());

        String simserial = CommonUtils.getSimSerialNumber(getApplicationContext());
        String simserialnumber = EncryptionUtil.encryptString(simserial, getApplicationContext());


        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {
                diplayServerResopnse = WebService.resendOtp(vkidreg, imei, deviceid, simserialnumber);
                Log.e("diplayServerResopnse - OTP", diplayServerResopnse);

            } catch (Exception e) {

                Log.e(" AsyncResendOTP Do in Background catch", e.getMessage());
                e.printStackTrace();
                Log.e("diplayServerResopnse", e.getMessage());
                String message = null;
                Log.i("TAG", ((message == null) ? "string null" : message));
                AlertDialogBoxInfo.alertDialogShow(OtpActitvity.this, getResources().getString(R.string.Warning));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");


            progress1.dismiss();

            try {

                /**
                 * METHOD: resendOtp
                 * RESPONSE:
                 * 1. OKAY
                 * 2. Invalid Request.      : Warning! Oops, some error encountered. Please try again.
                 *    ELSE : Unable to connect VKMS Server Please Try Again.
                 */
                if (diplayServerResopnse == null) {

                    /*String message = null;
                    Log.i("TAG", ((message == null) ? "string null" : message));*/


                } else if (diplayServerResopnse.equals("OKAY")) {
                    txtResendOTPResponse.setVisibility(View.VISIBLE);

                } else if (diplayServerResopnse.equals("Invalid Request.")) {
                    AlertDialogBoxInfo.alertDialogShow(OtpActitvity.this, getResources().getString(R.string.Warning));
                    edtOtpEnter.setText("");


                } else {
                    Log.e(TAG + "Error in Server", diplayServerResopnse);
                    AlertDialogBoxInfo.alertDialogShow(OtpActitvity.this, getResources().getString(R.string.unableToConnectVkms));


                    //  Toast.makeText(getApplicationContext(), "Error OTP ", Toast.LENGTH_SHORT).show();

                }

            } catch (Exception e) {
                e.printStackTrace();

                Log.e(" AsyncResendOTP onPostExecute catch", e.getMessage());
                Log.e("catchBlock- OTP", diplayServerResopnse);
                AlertDialogBoxInfo.alertDialogShow(OtpActitvity.this, getResources().getString(R.string.Warning));
            }
        }

    }

}
