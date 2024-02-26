package in.vakrangee.franchisee.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import in.vakrangee.franchisee.BuildConfig;
import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.task.AsyncLoginUser;
import in.vakrangee.franchisee.task.AsyncResetMPin;
import in.vakrangee.franchisee.task.AsyncauthenticateFranchiseeOnUpgrade;
import in.vakrangee.supercore.franchisee.application.VakrangeeKendraApplication;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.utils.SharedPrefUtils;
//

public class LoginActivity extends AppCompatActivity {

    Button btnSignIn, btnOnUpgrade;
    Toolbar toolbar;
    EditText edtLoginName, edtloginPassword;
    InternetConnection internetConnection;
    String diplayServerResopnse;
    ProgressDialog progress, prorgess1;
    private static final String TAG = "Result";
    TelephonyManager telephonyManager;
    TextView login, txtForgotMpin, txthindi, txtEnglish;
    private SharedPreferences saveVKId;
    Connection connection;
    TextView mpinmsg;
    Button btnResetMpin;
    private boolean isMinimized = false;

    private Locale myLocale;
    String lang = "en";
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    public final static int REQUEST_CODE = 10101;

    //Login Footer
    TextView textViewAppVersionName;
    TextView textViewCopyRight;
    TextView textViewTermOfUse;
    TextView textViewPrivacyPolicy;

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
        final Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fontawesome-webfont.ttf");

        setContentView(R.layout.activity_login);
        if (checkAndRequestPermissions()) {
            // carry on the normal flow, as the case of  permissions  granted.
        }
        // this.setFinishOnTouchOutside(true);

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        loadLocale();
        txthindi = (TextView) findViewById(R.id.txthindi);
        txthindi.setTypeface(Typeface.SANS_SERIF);
        txthindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lang = "hi";
                changeLang(lang);
                Toast.makeText(LoginActivity.this, "select हिन्दी", Toast.LENGTH_SHORT).show();
            }
        });

        txtEnglish = (TextView) findViewById(R.id.txtEnglish);
        txtEnglish.setTypeface(Typeface.SANS_SERIF);
        txtEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lang = "en";
                changeLang(lang);
                Toast.makeText(LoginActivity.this, "select English", Toast.LENGTH_SHORT).show();
            }
        });

        login = (TextView) findViewById(R.id.login);
        login.setTypeface(Typeface.SANS_SERIF);

        txtForgotMpin = (TextView) findViewById(R.id.forgotmpin);
        txtForgotMpin.setTypeface(Typeface.SANS_SERIF);

        internetConnection = new InternetConnection(LoginActivity.this);


        btnSignIn = (Button) findViewById(R.id.loginsubmit);
        btnSignIn.setTypeface(font);
        btnSignIn.setText(new SpannableStringBuilder(new String(new char[]{0xf090}) + " " + " " + getResources().getString(R.string.signIn)));

        btnOnUpgrade = (Button) findViewById(R.id.btnOnUpgrade);
        btnOnUpgrade.setTypeface(font);
        // btnOnUpgrade.setText(new SpannableStringBuilder(new String(getResources().getString(R.string.newuser))));

        edtLoginName = (EditText) findViewById(R.id.lloginname);
        edtLoginName.setTypeface(Typeface.SANS_SERIF);

        edtloginPassword = (EditText) findViewById(R.id.lpassword);
        edtloginPassword.setTypeface(Typeface.SANS_SERIF);

        //region Footer - AppVersion, Copyrights, Terms Of Use and Privacy Policy.
        // Login Footer Detail
        textViewAppVersionName = (TextView) findViewById(R.id.textViewAppVersionName);
        textViewCopyRight = (TextView) findViewById(R.id.textViewCopyRight);
        textViewTermOfUse = (TextView) findViewById(R.id.textViewTermOfUse);
        textViewPrivacyPolicy = (TextView) findViewById(R.id.textViewPrivacyPolicy);

        // Set App Version Name
        textViewAppVersionName.setText("Version: " + BuildConfig.VERSION_NAME);

        // Set Text For Copyright
        String strCopyRight = "Copyright © " + Calendar.getInstance().get(Calendar.YEAR) + "  <u> <a href=\"http://www.vakrangee.in\">Vakrangee Limited</a></u>. All Rights Reserved.";
        if (Build.VERSION.SDK_INT >= 24)
            textViewCopyRight.setText(Html.fromHtml(strCopyRight, Html.FROM_HTML_MODE_LEGACY));
        else
            textViewCopyRight.setText(Html.fromHtml(strCopyRight));

        textViewCopyRight.setMovementMethod(LinkMovementMethod.getInstance());
        textViewTermOfUse.setMovementMethod(LinkMovementMethod.getInstance());
        textViewPrivacyPolicy.setMovementMethod(LinkMovementMethod.getInstance());
        //endregion

        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        connection = new Connection(LoginActivity.this);
        connection.openDatabase();

        try {
            String checkDbEmpty = connection.checkIsEmpty();
            if (checkDbEmpty.equals("0")) {

                edtLoginName.setFocusableInTouchMode(true);
                edtLoginName.requestFocus();

            } else {
                String vkdi = connection.getVkid();
                vkdi = vkdi.toUpperCase();
                edtLoginName.setText(vkdi);
                edtloginPassword.setFocusableInTouchMode(true);
                edtloginPassword.requestFocus();
                edtLoginName.setTextColor(Color.parseColor("#468847"));

            }
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        } catch (Exception e) {
            e.getMessage();
        }

        // String vkdi = connection.getVkid();
        //  vkdi = vkdi.toUpperCase();
        //  edtLoginName.setText(vkdi);


        // final InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        //  inputMethodManager.showSoftInput(edtLoginName, InputMethodManager.SHOW_IMPLICIT);

        // edtloginPassword.setFocusableInTouchMode(true);
        //  edtloginPassword.requestFocus();


        edtloginPassword.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btnSignIn.performClick();
                    return true;
                }
                return false;
            }
        });


        txtForgotMpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    //inflate layout from xml. you must create an xml layout file in res/layout first
                    LayoutInflater inflater = LoginActivity.this.getLayoutInflater();
                    View layout = inflater.inflate(R.layout.forgotmpin, null);
                    builder.setView(layout);


                    mpinmsg = (TextView) layout.findViewById(R.id.Rmpinset);
                    mpinmsg.setTypeface(Typeface.SANS_SERIF);


                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            prorgess1 = new ProgressDialog(LoginActivity.this);

                            String checkDbEmpty = connection.checkIsEmpty();
                            if (checkDbEmpty.equals("0")) {

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);

                                alertDialogBuilder

                                        .setMessage(R.string.notrgister)
                                        .setCancelable(false)
                                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                                Intent i = new Intent(LoginActivity.this, RegisterPageActivity.class);
                                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(i);


                                            }

                                        });

                                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });


                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                                TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
                                textView.setTextSize(16);

                            } else {
                                if (internetConnection.isConnectingToInternet() == false) {

                                    AlertDialogBoxInfo.alertDialogShow(LoginActivity.this, getResources().getString(R.string.internetCheck));

                                } else if (telephonyManager.getSimState() == TelephonyManager.SIM_STATE_ABSENT) {

                                    AlertDialogBoxInfo.alertDialogShow(LoginActivity.this, getResources().getString(R.string.insertSimcard));

                                } else {

                                    prorgess1.setTitle(R.string.resetMpin);
                                    prorgess1.setMessage(getResources().getString(R.string.pleaseWait));
                                    prorgess1.setCancelable(false);
                                    prorgess1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                    prorgess1.show();


//
                                    // AsyncResetMPin myRequest = new AsyncResetMPin();
                                    //myRequest.execute();

                                    String vkidd = EncryptionUtil.encryptString(connection.getVkid(), getApplicationContext());

                                    final String deviceIdget = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                                    String deviceid = EncryptionUtil.encryptString(deviceIdget, getApplicationContext());

                                    String deviceIDAndroid = CommonUtils.getAndroidUniqueID(getApplicationContext());
                                    String imei = EncryptionUtil.encryptString(deviceIDAndroid, getApplicationContext());

                                    String simSerial = CommonUtils.getSimSerialNumber(getApplicationContext());
                                    String simserialnumber = EncryptionUtil.encryptString(simSerial, getApplicationContext());


                                    new AsyncResetMPin(LoginActivity.this).execute(vkidd, imei, deviceid, simserialnumber);
                                    //Toast.makeText(MpinAcitivty.this, "sucesssssfull", Toast.LENGTH_SHORT).show();


                                }
                            }
                        }
                    });

                    builder.setNegativeButton("Cancel  ", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builder.show();

//                Dialog dialog = new Dialog(LoginActivity.this);
//                dialog.setContentView(R.layout.forgotmpin);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        btnOnUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, RegisterPageActivity.class);
                startActivity(intent);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = edtLoginName.getText().toString();
                String pass = edtloginPassword.getText().toString();


                if (name.length() != 9) {
                    edtLoginName.setError(getResources().getString(R.string.enter9DigitsVKID));
                    // edtLoginName.setError(Html.fromHtml("<font color='red'>Username can't be empty</font>"));
                } else if (!isValidPassword(pass)) {
                    edtloginPassword.setError(getResources().getString(R.string.invalidPassword));


                } else if (internetConnection.isConnectingToInternet() == false) {

                    AlertDialogBoxInfo.alertDialogShow(LoginActivity.this, getResources().getString(R.string.internetCheck));

                } else if (telephonyManager.getSimState() == TelephonyManager.SIM_STATE_ABSENT) {
                    AlertDialogBoxInfo.alertDialogShow(LoginActivity.this, getResources().getString(R.string.insertSimcard));
                }

//                 else if (!connection.getVkid().toUpperCase().equals(imagetype.toString().toUpperCase())) {
//                    AlertDialogBoxInfo.alertDialogShow(LoginActivity.this, getResources().getString(R.string.usernameNotMatch));
//                    edtloginPassword.setText("");
//                    edtLoginName.setText("");
//
//                }

                else if (!edtLoginName.getText().toString().toUpperCase().equals(name.toString().toUpperCase())) {
                    AlertDialogBoxInfo.alertDialogShow(LoginActivity.this, getResources().getString(R.string.usernameNotMatch));
                    edtloginPassword.setText("");
                    edtLoginName.setText("");

                } else {
//                    progress = new ProgressDialog(LoginActivity.this);
//                    progress.setTitle(R.string.authenicationUsernamePassword);
//                    progress.setMessage(getResources().getString(R.string.pleaseWait));
//                    progress.setCancelable(false);
//                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                    progress.show();
//
                    //Adding VKId to Crashlytics
                    String loginId = edtLoginName.getText().toString();
                    Crashlytics.setUserIdentifier(loginId);
                    Crashlytics.setUserName(loginId);

                    String vkId = EncryptionUtil.encryptString(loginId, getApplicationContext());
                    String MPin = EncryptionUtil.encryptString(edtloginPassword.getText().toString(), getApplicationContext());


                    final String deviceIdget = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                    String deviceid = EncryptionUtil.encryptString(deviceIdget, getApplicationContext());

                    String deviceIDAndroid = CommonUtils.getAndroidUniqueID(getApplicationContext());
                    String imei = EncryptionUtil.encryptString(deviceIDAndroid, getApplicationContext());

                    String simSerial = CommonUtils.getSimSerialNumber(getApplicationContext());
                    String simserialnumber = EncryptionUtil.encryptString(simSerial, getApplicationContext());

                    //Get FCMId and Save into Shared Preferences.
                    /*String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                    if(!TextUtils.isEmpty(refreshedToken)) {
                        SharedPrefUtils.getInstance(getApplicationContext()).setFCMId(refreshedToken);
                    }*/

                    String checkDbEmpty = connection.checkIsEmpty();
                    if (checkDbEmpty.equals("0")) {
                        new AsyncauthenticateFranchiseeOnUpgrade(LoginActivity.this).execute(vkId, MPin, imei, deviceid, simserialnumber);
                        edtloginPassword.setText("");
                        edtloginPassword.setError(null);

                    } else {
                        new AsyncLoginUser(LoginActivity.this).execute(vkId, MPin, imei, deviceid, simserialnumber);
                        edtloginPassword.setText("");
                        edtloginPassword.setError(null);
                    }

                }

            }
        });

        edtLoginName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i != 9) {
                    // Toast.makeText(getApplicationContext(), "Please Enter 10 digits", Toast.LENGTH_SHORT).show();
                    edtLoginName.setTextColor(Color.parseColor("#000000"));
                    edtLoginName.setError(getResources().getString(R.string.EnterCharacterVKID));

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {


                if (edtLoginName.getText().toString().contains(" ")) {
                    edtLoginName.setText(edtLoginName.getText().toString().replaceAll(" ", ""));
                    edtLoginName.setSelection(edtLoginName.getText().length());


                }

                if (edtLoginName.length() <= 8) {


                } else {
                    edtLoginName.setTextColor(Color.parseColor("#468847"));
                    edtLoginName.setError(null);

                }
            }
        });

        edtloginPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i != 6) {
                    // Toast.makeText(getApplicationContext(), "Please Enter 10 digits", Toast.LENGTH_SHORT).show();
                    edtloginPassword.setTextColor(Color.parseColor("#000000"));
                    edtloginPassword.setError(getResources().getString(R.string.EnterDigitspassword));

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                {


                    if (edtloginPassword.length() <= 5) {


                    } else {
                        edtloginPassword.setTextColor(Color.parseColor("#468847"));
                        edtloginPassword.setError(null);

                    }
                }
            }
        });

        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "FCMId: " + refreshedToken);
        // Save into Shared Preferences.
        if (!TextUtils.isEmpty(refreshedToken)) {
            SharedPrefUtils.getInstance(getApplicationContext()).setFCMId(refreshedToken);
            Log.e(TAG, "FCMId Saved.");
        }

        //TODO: Comment/remove below code for release
        //testCode();

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
            AlertDialogBoxInfo.alertDialogShow(LoginActivity.this, getResources().getString(R.string.permissionnoallow));

        }
        return true;
    }

    public void loadLocale() {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        String language = prefs.getString(langPref, "");
        changeLang(language);
    }

    public void changeLang(String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);
        saveLocale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        updateTexts();
    }

    public void saveLocale(String lang) {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, lang);
        editor.commit();
    }

    private void updateTexts() {

        //  btnSignIn.setText(new SpannableStringBuilder(new String(new char[]{0xf090}) + " " + " " + getResources().getString(R.string.signIn)));

//        btnSignIn.setText(R.string.signIn);

    }

    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 5) {
            return true;
        }
        return false;
    }

    public void onBackPressed() {
        Intent mainActivity = new Intent(Intent.ACTION_MAIN);
        mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mainActivity.addCategory(Intent.CATEGORY_HOME);
        mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainActivity);
        finish();


    }

    @Override
    protected void onStop() {
        super.onStop();
        isMinimized = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
/*
        if (isMinimized) {
            startActivity(new Intent(this, MainActivity.class));
            isMinimized = false;
            finish();
        }
    */
    }

    //region Test Method  TODO: Always comment/remove it in release.

    static final String ACTION_ESIGNRESPONSE = "com.nsdl.egov.esign.rdservice.fp.CAPTURE";
    int ESIGN_REQUEST_CODE = 100;

    public void testCode() {


        //startActivity(new Intent(LoginActivity.this, SupportTicketActivity.class));

        /*Intent appStartIntent = new Intent();
        appStartIntent.setAction(ACTION_ESIGNRESPONSE);
        appStartIntent.putExtra("msg", "4afd477b40e9c9009632a8817f45473596c8f46703a5d5c2a2c249f313064c9a"); // msg contains esign request xml from ASP.
        appStartIntent.putExtra("env", "PREPROD"); //Possible values PREPROD or PROD (case sensitive).
        appStartIntent.putExtra("returnUrl", getPackageName()); // your package name where esign response failure/success will be sent.
        startActivityForResult(appStartIntent, REQUEST_CODE);*/


        /*CallLogReader callLogReader = new CallLogReader(LoginActivity.this);
        callLogReader.readCallLog();*/


        /*try {
            File src = new File(getFilesDir().getAbsolutePath() + File.separator + "Log" + File.separator + "Logger.txt");
            File dst = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Logger.txt");

            //dst.mkdirs();
            dst.createNewFile();

            FileInputStream inStream = new FileInputStream(src);

            if (!dst.exists()) {
                dst.mkdir();
            }

            if (!dst.canWrite()) {
                System.out.print("CAN'T WRITE");
                return;
            }

            FileOutputStream outStream = new FileOutputStream(dst);
            FileChannel inChannel = inStream.getChannel();
            FileChannel outChannel = outStream.getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
            inStream.close();
            outStream.close();
            Log.e(TAG, "Logger File Copied Successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }*/
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        Toast.makeText(this, "Got a response", Toast.LENGTH_SHORT).show();
        if (requestCode == REQUEST_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                String eSignResponse = data.getStringExtra("signedResponse"); //Business logic of ASP
            }
        }
    }*/

    //endregion

}
