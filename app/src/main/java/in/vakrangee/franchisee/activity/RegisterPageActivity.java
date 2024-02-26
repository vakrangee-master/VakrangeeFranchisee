package in.vakrangee.franchisee.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

import in.vakrangee.franchisee.BuildConfig;
import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.webservice.WebService;

//

public class RegisterPageActivity extends AppCompatActivity {
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    EditText edtrMobileNumber, edtrEmail, edtrName, edtrVKID, edtrPassword;
    Button btnrClear, btnrSubmit;
    String TAG = "Response";
    InternetConnection internetConnection;
    private boolean isMinimized = false;

    Toolbar toolbar;
    TelephonyManager telephonyManager;
    String deviceId;

    String diplayServerResopnse;
    ProgressDialog progress, progress1;
    String vkid, name, mob, mail;
    TextView registerpage, errorset;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // insertDummyContactWrapper();
        final Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fontawesome-webfont.ttf");
        setContentView(R.layout.activity_register);

        if (checkAndRequestPermissions()) {
            // carry on the normal flow, as the case of  permissions  granted.
        }


        registerpage = (TextView) findViewById(R.id.registerpage);
        registerpage.setTypeface(Typeface.SANS_SERIF);

        errorset = (TextView) findViewById(R.id.errorset);
        errorset.setTypeface(Typeface.SANS_SERIF);

        edtrMobileNumber = (EditText) findViewById(R.id.rMobileNumber);
        edtrMobileNumber.setTypeface(Typeface.SANS_SERIF);

        edtrEmail = (EditText) findViewById(R.id.rEmailId);
        edtrEmail.setTypeface(Typeface.SANS_SERIF);

        edtrName = (EditText) findViewById(R.id.rName);
        edtrName.setTypeface(Typeface.SANS_SERIF);

        edtrVKID = (EditText) findViewById(R.id.rVkid);
        edtrVKID.setTypeface(Typeface.SANS_SERIF);

        edtrPassword = (EditText) findViewById(R.id.rPassword);
        edtrPassword.setTypeface(Typeface.SANS_SERIF);

        btnrClear = (Button) findViewById(R.id.rCancel);
        btnrClear.setTypeface(font);
        btnrClear.setText(new SpannableStringBuilder(new String(new char[]{0xf021}) + " " + getResources().getString(R.string.clear)));

        btnrSubmit = (Button) findViewById(R.id.rSubmit);
        btnrSubmit.setTypeface(font);
        btnrSubmit.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " + getResources().getString(R.string.submit)));

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setTypeface(Typeface.SANS_SERIF);


        internetConnection = new InternetConnection(RegisterPageActivity.this);


        btnrClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtrEmail.setText("");
                edtrName.setText("");
                edtrVKID.setText("");
                edtrMobileNumber.setText("");
                edtrPassword.setText("");
                edtrPassword.setError(null);
                edtrEmail.setError(null);
                edtrName.setError(null);
                edtrVKID.setError(null);
                edtrMobileNumber.setError(null);
                errorset.setText("");
            }
        });


//        edtrName.setFilters(new InputFilter[]{
//                new InputFilter() {
//                    @Override
//                    public CharSequence filter(CharSequence cs, int start,
//                                               int end, Spanned spanned, int dStart, int dEnd) {
//                        // TODO Auto-generated method stub
//                        if (cs.equals("")) { // for backspace
//                            return cs;
//                        }
//                        if (cs.toString().matches("[a-zA-Z ]+")) {
//                            return cs;
//                        }
//                        return "";
//                    }
//                }
//        });


        deviceId = Secure.getString(getContentResolver(),
                Secure.ANDROID_ID);


        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);


        btnrSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mob = edtrMobileNumber.getText().toString();
                mail = edtrEmail.getText().toString();
                vkid = edtrVKID.getText().toString().trim().toUpperCase();
                name = edtrName.getText().toString();
                final String pass = edtrPassword.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                boolean valid = true;
                if (vkid.length() != 9) {
                    edtrVKID.setError(getResources().getString(R.string.enter9DigitsVKID));
                    valid = false;
                }
                if (name.length() < 4) {
                    edtrName.setError(getResources().getString(R.string.enter4CharacterName));
                    valid = false;
                }
                if (mob.length() != 10) {
                    edtrMobileNumber.setError(getResources().getString(R.string.enter10DigitesNumber));
                    valid = false;
                }
                if (!mail.matches(emailPattern)) {
                    edtrEmail.setError(getResources().getString(R.string.invalidEmail));
                    edtrEmail.setTextColor(Color.parseColor("#000000"));
                    valid = false;

                }
                if (!isValidPassword(pass)) {
                    edtrPassword.setError(getResources().getString(R.string.invalidPassword));
                    valid = false;
                }
                if (internetConnection.isConnectingToInternet() == false) {

                    AlertDialogBoxInfo.alertDialogShow(RegisterPageActivity.this, getResources().getString(R.string.internetCheck));
                    valid = false;

                }
                if (telephonyManager.getSimState() == TelephonyManager.SIM_STATE_ABSENT) {
                    AlertDialogBoxInfo.alertDialogShow(RegisterPageActivity.this, getResources().getString(R.string.insertSimcard));
                    valid = false;
                }

                if (valid) {

                    // X509TrustManagerSSL aa=      new X509TrustManagerSSL();
//
                    Connection connection = new Connection(RegisterPageActivity.this);
                    // connection.openDatabase();
                    AsyncRegisterData task = new AsyncRegisterData();
                    task.execute();
                    Log.d("WebServiceResponse: ", "Initiated.");

                    Log.e("value of result", " result" + diplayServerResopnse);

                    progress = new ProgressDialog(RegisterPageActivity.this);
                    progress.setTitle("Initiating Registration...");
                    progress.setMessage(getResources().getString(R.string.pleaseWait));
                    progress.setCancelable(false);
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.show();

                }

            }
        });


        edtrEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!isValidEmail(edtrEmail.getText().toString())) {
                    edtrEmail.setError(getResources().getString(R.string.invalidEmail));
                    edtrEmail.setTextColor(Color.parseColor("#000000"));
                } else {
                    edtrEmail.setTextColor(Color.parseColor("#468847"));
                    edtrEmail.setError(null);
                }
            }
        });

        edtrMobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i != 10) {
                    // Toast.makeText(getApplicationContext(), "Please Enter 10 digits", Toast.LENGTH_SHORT).show();
                    edtrMobileNumber.setTextColor(Color.parseColor("#000000"));
                    edtrMobileNumber.setError(getResources().getString(R.string.enter10DigitesNumber));

                }


            }

            @Override
            public void afterTextChanged(Editable editable) {


                if (edtrMobileNumber.length() <= 9) {


                } else {
                    edtrMobileNumber.setTextColor(Color.parseColor("#468847"));
                    edtrMobileNumber.setError(null);

                }
                String tmp = editable.toString().trim();
                if (tmp.length() == 1 && tmp.equals("0"))
                    editable.clear();
            }
        });

        edtrVKID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i != 9) {


                    // Toast.makeText(getApplicationContext(), "Please Enter 10 digits", Toast.LENGTH_SHORT).show();
                    edtrVKID.setTextColor(Color.parseColor("#000000"));
                    edtrVKID.setError(getResources().getString(R.string.enter9DigitsVKID));

                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (edtrVKID.getText().toString().contains(" ")) {
                    edtrVKID.setText(edtrVKID.getText().toString().replaceAll(" ", ""));
                    edtrVKID.setSelection(edtrVKID.getText().length());


                }

                if (edtrVKID.length() <= 8) {


                } else {
                    edtrVKID.setTextColor(Color.parseColor("#468847"));
                    edtrVKID.setError(null);

                }
            }
        });

        edtrName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (i < 4) {
                    // Toast.makeText(getApplicationContext(), "Please Enter 10 digits", Toast.LENGTH_SHORT).show();
                    edtrName.setTextColor(Color.parseColor("#000000"));
                    edtrName.setError(getResources().getString(R.string.enter4CharacterName));

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edtrName.length() <= 3) {


                } else {
                    edtrName.setTextColor(Color.parseColor("#468847"));
                    edtrName.setError(null);

                }
            }
        });


        edtrPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (i != 8) {
                    // Toast.makeText(getApplicationContext(), "Please Enter 10 digits", Toast.LENGTH_SHORT).show();
                    edtrPassword.setTextColor(Color.parseColor("#000000"));
                    edtrPassword.setError(getResources().getString(R.string.enter6CharacterPassword));

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {


                if (edtrPassword.getText().toString().contains(" ")) {
                    edtrPassword.setText(edtrPassword.getText().toString().replaceAll(" ", ""));
                    edtrPassword.setSelection(edtrPassword.getText().length());


                }

                if (edtrPassword.length() <= 7) {


                } else {
                    edtrPassword.setTextColor(Color.parseColor("#468847"));
                    edtrPassword.setError(null);

                }
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private boolean checkAndRequestPermissions() {
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
        return true;
    }


    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 7) {
            return true;
        }
        return false;
    }


    private boolean isValidEmail(String s) {
        if (s == null) {
            return false;
        } else {
            //android Regex to check the email address Validation
            return Patterns.EMAIL_ADDRESS.matcher(s).matches();
        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(RegisterPageActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

                break;
        }
        return true;
    }

    private class AsyncRegisterData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            // TrustManager trustManager=new X509TrustManagerSSL();

            RegisterData();


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            //  Toast.makeText(RegisterPageActivity.this, "Response aa" + diplayServerResopnse, Toast.LENGTH_SHORT).show();
            errorset.setText(diplayServerResopnse);
            Log.e("One time register -", diplayServerResopnse);
            progress.dismiss();

            try {

                if (diplayServerResopnse == null) {
                    Log.e("Registerpage null ", diplayServerResopnse);
                    AlertDialogBoxInfo.alertDialogShow(RegisterPageActivity.this, getResources().getString(R.string.unableToConnectVkms));

                } else if (diplayServerResopnse.equals("User Already Registered.")) {


                    Log.e("User Already Registered.", diplayServerResopnse);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegisterPageActivity.this);

                    alertDialogBuilder

                            .setMessage(R.string.alreadyRegistered)
                            .setCancelable(false)
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {


                                    AsyncCallAlreadyRegisterUser task = new AsyncCallAlreadyRegisterUser();
                                    task.execute();

                                    progress1 = new ProgressDialog(RegisterPageActivity.this);
                                    progress1.setTitle("Initiating Registration...");
                                    progress1.setMessage(getResources().getString(R.string.pleaseWait));
                                    progress1.setCancelable(false);
                                    progress1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                    progress1.show();

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


                } else if (diplayServerResopnse.equals("Error occured")) {
                    AlertDialogBoxInfo.alertDialogShow(RegisterPageActivity.this, getResources().getString(R.string.unableToConnectVkms));
                    errorset.setVisibility(View.INVISIBLE);

                } else if (diplayServerResopnse.equals("OKAY")) {

                    String mob = edtrMobileNumber.getText().toString().trim();
                    String mail = edtrEmail.getText().toString().trim().toLowerCase();
                    String vkid = edtrVKID.getText().toString().trim().toUpperCase();
                    String name = edtrName.getText().toString().trim().toLowerCase();
                    Connection connection = new Connection(RegisterPageActivity.this);
                    //connection.openDatabase();


//                    connection.checkIsEmpty();
//                    String checkDbEmpty = connection.checkIsEmpty();
//                    if (checkDbEmpty.equals("0")) {
//                        connection.insertIntoDB(vkid, name, mail, mob);
//                    } else {
//                        connection.deleteTableinfo();
//                        connection.insertIntoDB(vkid, name, mail, mob);
//                    }
                    connection.deleteTableinfo();
                    connection.insertIntoDB(vkid, name, mail, mob);

                    Log.e("Alredy user insert into sqllite DB -", vkid + name + mail + mob);
                    Intent intent = new Intent(RegisterPageActivity.this, OtpActitvity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                } else if (diplayServerResopnse.equals("Invalid registration data provided.")) {

                    AlertDialogBoxInfo.alertDialogShow(RegisterPageActivity.this, getResources().getString(R.string.invalidData));
                    errorset.setVisibility(View.INVISIBLE);

                } else if (diplayServerResopnse.equals("Decryption fail.")) {

                    AlertDialogBoxInfo.alertDialogShow(RegisterPageActivity.this, getResources().getString(R.string.invalidData));
                    errorset.setVisibility(View.INVISIBLE);

                } else if (diplayServerResopnse.equals("Error while sending mail.")) {

                    AlertDialogBoxInfo.alertDialogShow(RegisterPageActivity.this, getResources().getString(R.string.errorSendingMail));
                    errorset.setVisibility(View.INVISIBLE);
                } else {
                    Log.e("Issue in Server ", diplayServerResopnse);
                    AlertDialogBoxInfo.alertDialogShow(RegisterPageActivity.this, getResources().getString(R.string.Warning));

                }


            } catch (Exception e) {

                Log.e(" AsyncRegisterData  catch", e.getMessage());
                e.printStackTrace();
                AlertDialogBoxInfo.alertDialogShow(RegisterPageActivity.this, getResources().getString(R.string.Warning));
            }
        }

    }

    private void RegisterData() {

        try {

            vkid = EncryptionUtil.encryptString(edtrVKID.getText().toString().trim().toUpperCase(), getApplicationContext());
            String name = EncryptionUtil.encryptString(edtrName.getText().toString(), getApplicationContext());
            String email = EncryptionUtil.encryptString(edtrEmail.getText().toString().trim().toLowerCase(), getApplicationContext());
            String mnumber = EncryptionUtil.encryptString(edtrMobileNumber.getText().toString(), getApplicationContext());

            String deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            String deviceid = EncryptionUtil.encryptString(deviceId, getApplicationContext());

            String deviceIDAndroid = CommonUtils.getAndroidUniqueID(getApplicationContext());
            String imei = EncryptionUtil.encryptString(deviceIDAndroid, getApplicationContext());

            String simSerial = CommonUtils.getSimSerialNumber(getApplicationContext());
            String simopertaor = EncryptionUtil.encryptString(simSerial, getApplicationContext());

            String password = EncryptionUtil.encryptString(edtrPassword.getText().toString(), getApplicationContext());

            String status = EncryptionUtil.encryptString("F", getApplicationContext());

            String MVersion = EncryptionUtil.encryptString(BuildConfig.VERSION_NAME, getApplicationContext());
            String AndroidVersiona = Build.VERSION.RELEASE;
            String AndroidVersion = EncryptionUtil.encryptString("Android - " + AndroidVersiona, getApplicationContext());


            diplayServerResopnse = WebService.registerFranchisee(vkid, name, email, mnumber, imei, deviceid, simopertaor, password,
                    status, AndroidVersion, MVersion);

            Log.e("diplayServerResopnse", diplayServerResopnse);
            Log.d(TAG, "WebSer...ceResponse: " + diplayServerResopnse);

        } catch (Exception e) {


            Log.e(" RegisterData method catch", e.getMessage());
            String message = null;
            Log.i("TAG", ((message == null) ? "string null" : message));
            e.printStackTrace();
            AlertDialogBoxInfo.alertDialogShow(RegisterPageActivity.this, getResources().getString(R.string.Warning));
        }


    }


    private class AsyncCallAlreadyRegisterUser extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            AlreadyRegisterCall();


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            //  Toast.makeText(RegisterPageActivity.this, "Response aa" + diplayServerResopnse, Toast.LENGTH_SHORT).show();
            errorset.setText(diplayServerResopnse);
            Log.e("Alread User Yes - No click ", diplayServerResopnse);
            progress1.dismiss();

            try {

                /**
                 * METHOD: registerFranchisee
                 * RESPONSE:
                 * 1. OKAY
                 * 2. User Already Registered.      : Registration details already exists, Do you want to continue from this Device and proceed.
                 * 3. Error occurred                 : Unable to connect VKMS Server Please Try Again.
                 * 4. Invalid registration data provided.   : Invalid Data Entered, Please Verify All Information.
                 *    ELSE : Warning! Oops, some error encountered. Please try again.
                 */
                if (diplayServerResopnse == null) {

                    AlertDialogBoxInfo.alertDialogShow(RegisterPageActivity.this, getResources().getString(R.string.unableToConnectVkms));

                } else if (diplayServerResopnse.equals("User Already Registered.")) {
                    AlertDialogBoxInfo.alertDialogShow(RegisterPageActivity.this, getResources().getString(R.string.alreadyRegistered));

                } else if (diplayServerResopnse.equals("Error occured")) {
                    AlertDialogBoxInfo.alertDialogShow(RegisterPageActivity.this, getResources().getString(R.string.unableToConnectVkms));

                } else if (diplayServerResopnse.equals("OKAY")) {

                    String mob = edtrMobileNumber.getText().toString().trim();
                    String mail = edtrEmail.getText().toString().trim();
                    String vkid = edtrVKID.getText().toString().trim().toUpperCase();
                    String name = edtrName.getText().toString().trim().toLowerCase();

                    Connection connection = new Connection(RegisterPageActivity.this);


                    connection.checkIsEmpty();
//                    String checkDbEmpty = connection.checkIsEmpty();
//                    if (checkDbEmpty.equals("0")) {
//                        connection.insertIntoDB(vkid, name, mail, mob);
//                    } else {
                    connection.deleteTableinfo();
                    connection.insertIntoDB(vkid, name, mail, mob);
//                    }

                    Log.e("Already USer Yes click  ", vkid + name + mail + mob);
                    Intent intent = new Intent(RegisterPageActivity.this, OtpActitvity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                } else if (diplayServerResopnse.equals("Invalid registration data provided.")) {
                    AlertDialogBoxInfo.alertDialogShow(RegisterPageActivity.this, getResources().getString(R.string.invalidData));

                } else {

                    Log.e(TAG + "Issue in Server ", diplayServerResopnse);
                    // Toast.makeText(RegisterPageActivity.this, " Issue in Server", Toast.LENGTH_SHORT).show();
                    AlertDialogBoxInfo.alertDialogShow(RegisterPageActivity.this, getResources().getString(R.string.Warning));

                }

            } catch (Exception e) {
                e.printStackTrace();

                Log.e(" AsyncCallAlreadyRegisterUser catch", e.getMessage());
                AlertDialogBoxInfo.alertDialogShow(RegisterPageActivity.this, getResources().getString(R.string.Warning));
            }
        }

    }

    private void AlreadyRegisterCall() {

        try {

            vkid = EncryptionUtil.encryptString(edtrVKID.getText().toString().trim().toUpperCase(), getApplicationContext());
            String name = EncryptionUtil.encryptString(edtrName.getText().toString(), getApplicationContext());
            String email = EncryptionUtil.encryptString(edtrEmail.getText().toString().trim().toLowerCase(), getApplicationContext());
            String mnumber = EncryptionUtil.encryptString(edtrMobileNumber.getText().toString(), getApplicationContext());

            String deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            String deviceid = EncryptionUtil.encryptString(deviceId, getApplicationContext());

            String deviceIDAndroid = CommonUtils.getAndroidUniqueID(getApplicationContext());
            String imei = EncryptionUtil.encryptString(deviceIDAndroid, getApplicationContext());

            String simSerial = CommonUtils.getSimSerialNumber(getApplicationContext());
            String simopertaor = EncryptionUtil.encryptString(simSerial, getApplicationContext());

            String password = EncryptionUtil.encryptString(edtrPassword.getText().toString(), getApplicationContext());

            String status = EncryptionUtil.encryptString("Y", getApplicationContext());

            String MVersion = EncryptionUtil.encryptString(BuildConfig.VERSION_NAME, getApplicationContext());
            String AndroidVersiona = Build.VERSION.RELEASE;
            String AndroidVersion = EncryptionUtil.encryptString("Android - " + AndroidVersiona, getApplicationContext());


            diplayServerResopnse = WebService.registerFranchisee(vkid, name, email, mnumber, imei, deviceid, simopertaor, password,
                    status, AndroidVersion, MVersion);


            Log.e("diplayServerResopnse", diplayServerResopnse);
        } catch (Exception e) {

            Log.e(" AlreadyRegisterCall method catch", e.getMessage());
            e.printStackTrace();
            Log.e("diplayServerResopnse", diplayServerResopnse);
            AlertDialogBoxInfo.alertDialogShow(RegisterPageActivity.this, getResources().getString(R.string.Warning));
        }


    }


    public void onBackPressed() {

        Intent intent = new Intent(RegisterPageActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
/*
        Intent mainActivity = new Intent(Intent.ACTION_MAIN);
        mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mainActivity.addCategory(Intent.CATEGORY_HOME);
        mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainActivity);
        moveTaskToBack(true);
        finish();
*/

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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        // client.disconnect();
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


}

