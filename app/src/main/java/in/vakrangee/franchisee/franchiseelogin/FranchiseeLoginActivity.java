package in.vakrangee.franchisee.franchiseelogin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.mindorks.nybus.NYBus;
import com.mindorks.nybus.annotation.Subscribe;
import com.mindorks.nybus.event.Channel;


import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.vakrangee.franchisee.BuildConfig;
import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.activity.DashboardActivity;
import in.vakrangee.franchisee.smsretrieval.IOTPReceiveListener;
import in.vakrangee.franchisee.smsretrieval.SMSRetrievalReceiver;
import in.vakrangee.supercore.franchisee.baseutils.BaseAppCompatActivity;
import in.vakrangee.supercore.franchisee.franchiseelogin.FranchiseeLoginChecksDto;
import in.vakrangee.supercore.franchisee.oauth.OAuthUtils;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;
import in.vakrangee.supercore.franchisee.utils.SharedPrefUtils;
import in.vakrangee.supercore.franchisee.utils.network.ConnectivityChangeReceiver;
import in.vakrangee.supercore.franchisee.utils.network.EventData;
import in.vakrangee.supercore.franchisee.utils.network.NetworkHealthHandler;

public class FranchiseeLoginActivity extends BaseAppCompatActivity implements IOTPReceiveListener {

    private static final String TAG = "FranchiseeLoginActivity";

    private MaterialButton btnLogin;
    private TextView textViewAppVersionName;
    //private Toolbar toolbar;
    private VerifyOTPDialog verifyOTPDialog;
    private PermissionHandler permissionHandler;
    private ConnectivityChangeReceiver receiver;
    //private SmsListener otpReceiver;

    //region Google Sign In
    //a constant for detecting the login intent result
    private static final int RC_SIGN_IN = 234;
    //creating a GoogleSignInClient object
    GoogleSignInClient mGoogleSignInClient;
    //And also a Firebase Auth object
    FirebaseAuth mAuth;
    private OAuthUtils oAuthUtils;
    //endregion

    private AsyncValidateUser asyncValidateUser = null;
    private DeprecateHandler deprecateHandler;
    private Connection connection;
    private Typeface clanPro, font;
    TextView txtLoginNote;
    TextView textViewCopyRight;
    TextView textViewTermOfUse;
    TextView textViewPrivacyPolicy;

    private static final String MSG_SENDER = "VKRNGE";
    private SMSRetrievalReceiver smsReceiver;
    private TextView txtVakrangee;
    private TextInputLayout txtInputVKID;
    private TextInputLayout txtVKID;
    private TextInputEditText editTextMobEmail;
    private TextInputEditText editTextVKID;

    private String mobNo = "", vkid = "";
    private ProgressDialog progressDialog;
    private Context context;
    private  String appID = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fontawesome-webfont.ttf");
        setContentView(R.layout.activity_franchisee_login);
        context = FranchiseeLoginActivity.this;

        progressDialog = new ProgressDialog(context);

        clanPro = Typeface.createFromAsset(getAssets(), "ClanPro-Bold.otf");

        final Typeface fonta = Typeface.createFromAsset(getApplicationContext().getAssets(), "ClanPro-Medium.otf");
        txtVakrangee = findViewById(R.id.txtVakrangee);
        txtInputVKID = findViewById(R.id.txtInputVKID);
        txtVKID = findViewById(R.id.txtVKID);
        //txtKendra = findViewById(R.id.txtKendra);
        //txtConnect = findViewById(R.id.txtConnect);

        txtVakrangee.setTypeface(fonta, Typeface.BOLD_ITALIC);
        //txtKendra.setTypeface(fonta, Typeface.BOLD_ITALIC);
        // txtConnect.setTypeface(fonta, Typeface.BOLD_ITALIC);

        //TextView txtVakrangeeKendra = findViewById(R.id.txtVakrangeeKendra);
        // TextView txtFranchisee = findViewById(R.id.txtFranchisee);

        //CommonUtils.setTextStyleFont(txtVakrangeeKendra, clanPro, "Vakrangee Kendra ");
        //  CommonUtils.setTextStyleFont(txtFranchisee, clanPro, "Franchisee ");


        textViewCopyRight = (TextView) findViewById(R.id.textViewCopyRight);
        textViewTermOfUse = (TextView) findViewById(R.id.textViewTermOfUse);
        textViewPrivacyPolicy = (TextView) findViewById(R.id.textViewPrivacyPolicy);


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

        permissionHandler = new PermissionHandler(this);
        receiver = new ConnectivityChangeReceiver();
        //otpReceiver = new SmsListener();
        deprecateHandler = new DeprecateHandler(this);

        configureSMSRetreiver();

        //Commented for Google Restriction -- Vasundhara on 28th Feb 2019
       /*//OTP Reader
        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                String code = parseCode(message);//Parse verification code
                Log.e(TAG, "OTP: " + code);
                if (verifyOTPDialog != null && verifyOTPDialog.isShowing())
                    verifyOTPDialog.SetOTPValue(code);

            }
        });
        smsVerifyCatcher.setPhoneNumberFilter(MSG_SENDER);*/

        //Register
        NYBus.get().register(this, Channel.TWO);

        //Widgets
        textViewAppVersionName = (TextView) findViewById(R.id.textViewAppVersionName);
      /*  toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);*/
        editTextMobEmail = findViewById(R.id.editTextMobEmail);
        editTextVKID = findViewById(R.id.editTextVKID);
        txtLoginNote = findViewById(R.id.txtLoginNote);

        // Set App Version Name
        textViewAppVersionName.setText("Version: " + BuildConfig.VERSION_NAME);
        //CommonUtils.InputFiletrWithMaxLength(editTextMobEmail, "~#^|$%&*!", 50);

        connection = new Connection(FranchiseeLoginActivity.this);
        connection.openDatabase();

        btnLogin = (MaterialButton) findViewById(R.id.loginsubmit);
        btnLogin.setTypeface(font);
        btnLogin.setText(new SpannableStringBuilder(new String(new char[]{0xf090}) + " " + " " + getResources().getString(R.string.signIn)));
        //btnLogin.setOnClickListener(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int status = validateDetails();
                    if (status == 0) {
                        loginApiCall();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //Permissions.
        permissionHandler.requestPhoneLocationAndStoragePermission(btnLogin, new IPermission() {
            @Override
            public void IsPermissionGranted(boolean IsGranted) {
                if (IsGranted) {
                    Log.i(TAG, "All permission granted");
                } else {
                    Log.i(TAG, "All permission denied");
                }
            }
        });

        txtLoginNote.setText(Html.fromHtml("<b><font color=\"#FF0000\">NOTE: </font></b><font color=\"#13365C\">For Login, use Mobile No. provided in NextGen Franchisee Application Form.</font>"));
        //txtLoginNote.setText("NOTE: For Login, Use Mobile No/Email Id provided in NextGen Franchisee Enquiry Form.");

        //Validate Mobile no/Email ID

        txtInputVKID.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i != 9) {
                    // Toast.makeText(getApplicationContext(), "Please Enter 10 digits", Toast.LENGTH_SHORT).show();
                    txtInputVKID.setError(getResources().getString(R.string.EnterMob));
                    txtInputVKID.setErrorEnabled(true);
                    txtInputVKID.setErrorTextAppearance(R.style.AppTheme_TextErrorAppearance);

                }

                int len = editTextMobEmail.getText().toString().trim().length();
                if (len <= 0)
                    return;

                // boolean IsEmailMatched = editTextMobEmail.getText().toString().trim().matches(CommonUtils.emailPattern);
                boolean IsValidMob = CommonUtils.isValidMobile(editTextMobEmail.getText().toString().trim());
                if (!IsValidMob) {
                    Log.e(TAG, "Testing: IsEmailMatched: " + " IsValidMob: " + IsValidMob);
                    editTextMobEmail.setTextColor(Color.parseColor("#000000"));
                    editTextMobEmail.setError(getResources().getString(R.string.InvalidEmailMob));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editTextMobEmail.getText().toString().contains(" ")) {
                    editTextMobEmail.setText(editTextMobEmail.getText().toString().replaceAll(" ", ""));
                    editTextMobEmail.setSelection(editTextMobEmail.getText().length());


                }

                if (editTextMobEmail.length() <= 9) {
                    editTextMobEmail.setTextColor(getResources().getColor(R.color.md_red_800));
                } else {

                    txtInputVKID.setError(null);
                    txtInputVKID.setErrorEnabled(false);

                    editTextMobEmail.setTextColor(getResources().getColor(R.color.colorAccent));

                }
            }
        });


        txtVKID.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i != 9) {
                    // Toast.makeText(getApplicationContext(), "Please Enter 10 digits", Toast.LENGTH_SHORT).show();
                    txtVKID.setError(getResources().getString(R.string.customerId));
                    txtVKID.setErrorEnabled(true);
                    txtVKID.setErrorTextAppearance(R.style.AppTheme_TextErrorAppearance);

                }

                int len = editTextVKID.getText().toString().trim().length();
                if (len <= 0)
                    return;

                // boolean IsEmailMatched = editTextMobEmail.getText().toString().trim().matches(CommonUtils.emailPattern);
                boolean IsValidVKID = CommonUtils.isValidVKID(editTextVKID.getText().toString().trim());
                if (!IsValidVKID) {
                    Log.e(TAG, "Testing: IsEmailMatched: " + " IsValidMob: " + IsValidVKID);
                    editTextVKID.setTextColor(Color.parseColor("#000000"));
                   // editTextVKID.setError(getResources().getString(R.string.InvalidVKID));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editTextVKID.getText().toString().contains(" ")) {
                    editTextVKID.setText(editTextVKID.getText().toString().replaceAll(" ", ""));
                    editTextVKID.setSelection(editTextVKID.getText().length());


                }

                if (editTextVKID.length() < 9) {
                    editTextVKID.setTextColor(getResources().getColor(R.color.md_red_800));
                } else {

                    txtVKID.setError(null);
                    txtVKID.setErrorEnabled(false);
                    editTextVKID.setTextColor(getResources().getColor(R.color.colorAccent));

                }
            }
        });



/*
        editTextMobEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int len = editTextMobEmail.getText().toString().trim().length();
                if (len <= 0)
                    return;

                // boolean IsEmailMatched = editTextMobEmail.getText().toString().trim().matches(CommonUtils.emailPattern);
                boolean IsValidMob = CommonUtils.isValidMobile(editTextMobEmail.getText().toString().trim());
                if (!IsValidMob) {
                    Log.e(TAG, "Testing: IsEmailMatched: " + " IsValidMob: " + IsValidMob);
                    editTextMobEmail.setTextColor(Color.parseColor("#000000"));
                    editTextMobEmail.setError(getResources().getString(R.string.InvalidEmailMob));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                if (editTextMobEmail.getText().toString().contains(" ")) {
                    editTextMobEmail.setText(editTextMobEmail.getText().toString().replaceAll(" ", ""));
                    editTextMobEmail.setSelection(editTextMobEmail.getText().length());
                }

                //boolean IsEmailMatched = editTextMobEmail.getText().toString().trim().matches(CommonUtils.emailPattern);
                boolean IsValidMob = CommonUtils.isValidMobile(editTextMobEmail.getText().toString().trim());
                if (IsValidMob) {
                    Log.e(TAG, "Testing: IsEmailMatched: " + " IsValidMob: " + IsValidMob);
                    editTextMobEmail.setTextColor(Color.parseColor("#468847"));
                    editTextMobEmail.setError(null);
                }
            }
        });
*/

        //region Google Sign In

        //first we intialized the FirebaseAuth object
        mAuth = FirebaseAuth.getInstance();

        //Then we need a GoogleSignInOptions object
        //And we need to build it as below
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                .requestEmail()
                .build();

        //Then we will get the GoogleSignInClient object from GoogleSignIn class
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //boolean flag = isSignedIn();
        //Log.e(TAG, "Testing: isSignedIn: " + flag);

        //signOut();
        //Log.e(TAG, "Testing: signOut: ");

        //Now we will attach a click listener to the sign_in_button
        //and inside onClick() method we are calling the signIn() method that will open
        //google sign in intent
        /*findViewById(R.id.login_with_google).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });*/

        oAuthUtils = OAuthUtils.getInstance(this);
        //new TestAsync().execute();

        //endregion
        //gif("Front Image", R.drawable.frontimagemarker);
    }

    /**
     * Parse verification code
     *
     * @param message sms message
     * @return only four numbers from massage string
     */
    private String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{4}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, NetworkHealthHandler.prepareIntentFilter());
        //registerReceiver(otpReceiver, prepareIntentFilterForSMS());
    }

    @Override
    protected void onDestroy() {
        // Unregister
        unregisterReceiver(receiver);
        //unregisterReceiver(otpReceiver);
        NYBus.get().unregister(this, Channel.TWO);
        super.onDestroy();
        if (asyncValidateUser != null && !asyncValidateUser.isCancelled()) {
            asyncValidateUser.cancel(true);
        }

        if (smsReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(smsReceiver);
        }
    }

    /*@Override
    public void onClick(View view) {
        int Id = view.getId();

        if (Id == R.id.loginsubmit) {
            permissionHandler.requestPhoneLocationAndStoragePermission(btnLogin, new IPermission() {
                @Override
                public void IsPermissionGranted(boolean IsGranted) {
                    if (IsGranted) {
                        String text = editTextMobEmail.getText().toString();
                        boolean IsValidated = IsValidMobOREmailId(text);
                        if (IsValidated) {
                            validateUserUsingMobEmailId(text);
                        }
                        Log.i(TAG, "All permission granted");
                    } else {
                        Log.i(TAG, "All permission denied");
                    }
                }
            });
        }
    }*/

    @Override
    protected void onPause() {
        super.onPause();
        permissionHandler.dismissSnackbar();
    }

    @Subscribe(channelId = Channel.TWO)
    public void onEvent(EventData event) {
        //displayConnectivitySnackbar(event.getData());
        String actionMsg = getString(R.string.close_text);
        NetworkHealthHandler.displaySnackBar(findViewById(android.R.id.content), event.getData(), actionMsg, deprecateHandler);
    }

    //region Google Sign In
    @Override
    protected void onStart() {
        super.onStart();

        //if the user is already signed in
        //we will close this activity
        //and take the user to profile activity
        if (mAuth.getCurrentUser() != null) {
            //finish();
            //startActivity(new Intent(this, ProfileActivity.class));
        }

        // Commented for Google Restriction -- Vasundhara on 28th Feb 2019
        //smsVerifyCatcher.onStart();
    }

    class TestAsync extends AsyncTask<Void, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            boolean flag = oAuthUtils.authenticate("deepak", "123");
            Log.e(TAG, "Testing: oAuthUtils.authenticate: " + flag);

            oAuthUtils.refreshToken();
            Log.e(TAG, "Testing: oAuthUtils.refreshToken: ");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if the requestCode is the Google Sign In code that we defined at starting
        if (requestCode == RC_SIGN_IN) {

            //Getting the GoogleSignIn Task
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                Log.d(TAG, "account.getGivenName(): " + account.getGivenName());
                Log.d(TAG, "account.getDisplayName(): " + account.getDisplayName());
                Log.d(TAG, "account.getEmail(): " + account.getEmail());
                Log.d(TAG, "account.getPhotoUrl(): " + account.getPhotoUrl());

                //authenticating with firebase
                //firebaseAuthWithGoogle(account);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        //getting the auth credential
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        //Now using firebase we are signing in the user here
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Toast.makeText(FranchiseeLoginActivity.this, "User Signed In", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(FranchiseeLoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    //this method is called on click
    private void signIn() {
        //getting the google signin intent
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        //starting the activity for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        if (mGoogleSignInClient != null) {
            mGoogleSignInClient.signOut();
            Toast.makeText(this, "Sign Out..", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method to check If already signed in Google
     *
     * @return
     */
    private boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(this) != null;
    }

    private void signOutFB() {
        FirebaseAuth.getInstance().signOut();
    }

    //endregion

    public boolean IsValidMobOREmailId(String text) {

        if (TextUtils.isEmpty(text)) {
            editTextMobEmail.setError("Please enter Mobile No/Email ID.");
            return false;
        }

        //Validate If Email address is entered
       /* if (text.contains("@")) {
            boolean IsValidEmail = text.trim().matches(CommonUtils.emailPattern);
            if (!IsValidEmail) {
                editTextMobEmail.setError("Please enter valid Email ID.");
                return false;
            }
        }*/
        else {
            boolean IsValidMob = CommonUtils.isValidMobile(text);
            if (!IsValidMob) {
                editTextMobEmail.setError("Please enter valid Mobile Number.");
                return false;
            }
        }
        return true;
    }

    public void validateUserUsingMobEmailId(String mobEmailId) {

        //Internet Connectivity check
        if (!InternetConnection.isNetworkAvailable(this)) {
            showMessage("No Internet Connection.");
            return;
        }

        asyncValidateUser = new AsyncValidateUser(this, mobEmailId, new AsyncValidateUser.Callback() {
            @Override
            public void onResult(String result) {
                try {
                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(FranchiseeLoginActivity.this, getResources().getString(R.string.Warning));
                        return;
                    }

                    //Handle Response
                    if (result.startsWith("OKAY")) {
                        StringTokenizer st1 = new StringTokenizer(result, "\\|");
                        String key = st1.nextToken();
                        final String OTP = st1.nextToken();
                        final String expiryTime = st1.nextToken();
                        displayOTPDialog(OTP, expiryTime);

                        //Set Paramaters to start Update LatLng Service
                        String locationStatus = st1.nextToken();
                        boolean IsAllowed = ((!TextUtils.isEmpty(locationStatus)) && locationStatus.equalsIgnoreCase("Y")) ? true : false;
                        int interval = Integer.parseInt(st1.nextToken());
                        updateParametersForLatLngService(IsAllowed, interval);


                    } else {
                        StringTokenizer st1 = new StringTokenizer(result, "\\|");
                        String key = st1.nextToken();
                        String sco1 = st1.nextToken();
                        if (!TextUtils.isEmpty(sco1)) {
                            showMessage(sco1);
                            return;
                        }

                        showMessage("Not a valid user.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(FranchiseeLoginActivity.this, getResources().getString(R.string.Warning));
                }

            }
        });
        asyncValidateUser.execute("");
    }

    private void updateParametersForLatLngService(boolean IsAllowed, int interval) {
        CommonUtils.setUpdateLatLngStatus(FranchiseeLoginActivity.this, IsAllowed);
        CommonUtils.setUpdateLatLngInterval(FranchiseeLoginActivity.this, interval);
    }

    private void displayOTPDialog(String otpValue, final String expiryTime) {

        if (verifyOTPDialog != null && verifyOTPDialog.isShowing()) {
            verifyOTPDialog.OTPValue(otpValue);
            return;
        }

        //Display Verify OTP Dialog
        verifyOTPDialog = new VerifyOTPDialog(FranchiseeLoginActivity.this, new VerifyOTPDialog.IVerifyOTP() {
            @Override
            public void isOTPVerified() {

                //Set Franchisee Login Data
                String userName = editTextMobEmail.getText().toString();
                FranchiseeLoginChecksDto loginChecksDto = prepareFranchiseeLoginData(userName, expiryTime);
                CommonUtils.setFranchiseeLoginDataIntoPreferences(FranchiseeLoginActivity.this, loginChecksDto);
                Toast.makeText(FranchiseeLoginActivity.this, "OTP Verfied.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FranchiseeLoginActivity.this, NextGenFranchiseeEnquiryDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();


            }

            @Override
            public void resendOTP() {
                validateUserUsingMobEmailId(editTextMobEmail.getText().toString());
            }
        });
        verifyOTPDialog.show();
        verifyOTPDialog.setCancelable(false);
        verifyOTPDialog.OTPValue(otpValue);
    }

    public FranchiseeLoginChecksDto prepareFranchiseeLoginData(String userName, String expiryTime) {
        FranchiseeLoginChecksDto loginChecksDto = CommonUtils.getFranchiseeLoginDataFromPreferences(FranchiseeLoginActivity.this);
        if (loginChecksDto == null)
            loginChecksDto = new FranchiseeLoginChecksDto();

        String userInputType = userName.contains("@") ? "EmailId" : "MobileNo";

        loginChecksDto.setOtpVerified("1");
        loginChecksDto.setExpiryTime(expiryTime);
        loginChecksDto.setUserName(userName);
        loginChecksDto.setUserInputType(userInputType);

        return loginChecksDto;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public IntentFilter prepareIntentFilterForSMS() {
        IntentFilter intentFilter;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //smsReceiver = new SmsReceiver();
            intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            intentFilter.addAction(Telephony.Sms.Intents.DATA_SMS_RECEIVED_ACTION);
            //this.registerReceiver(smsReceiver, intentFilter);
        } else {
            intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        }
        return intentFilter;
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Commented for Google Restriction -- Vasundhara on 28th Feb 2019
        //smsVerifyCatcher.onStop();
    }

    /**
     * need for Android 6 real time permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Commented for Google Restriction -- Vasundhara on 28th Feb 2019
        //smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void configureSMSRetreiver() {

        /*AppSignatureHashHelper appSignatureHashHelper = new AppSignatureHashHelper(this);
        // This code requires one time to get Hash keys do comment and share key
        String hashKey = appSignatureHashHelper.getAppSignatures().get(0);
        Log.d(TAG, "Apps Hash Key: " + hashKey);
        String key = "bWE0Ip/XGIk";     //"EvZVsyr0U6I";
        if (hashKey.equalsIgnoreCase(key)) {
            Toast.makeText(FranchiseeLoginActivity.this, " Same Hash Key ", Toast.LENGTH_LONG).show();
        } else {        }*/

        Toast.makeText(FranchiseeLoginActivity.this, " Different Hash Key ", Toast.LENGTH_LONG).show();

        smsReceiver = new SMSRetrievalReceiver();
        smsReceiver.setOTPListener(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
        this.registerReceiver(smsReceiver, intentFilter);

        SmsRetrieverClient client = SmsRetriever.getClient(FranchiseeLoginActivity.this);
        Task<Void> task = client.startSmsRetriever();
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Android will provide message once receive. Start your broadcast receiver.
                IntentFilter filter = new IntentFilter();
                filter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
                registerReceiver(new SMSRetrievalReceiver(), filter);
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Failed to start retriever, inspect Exception for more details
            }
        });
    }

    @Override
    public void onOTPReceived(String otp) {
        String code = parseCode(otp);//Parse verification code
        showToast("OTP Received: " + code);
        Log.e(TAG, "OTP: " + code);
        if (verifyOTPDialog != null && verifyOTPDialog.isShowing())
            verifyOTPDialog.SetOTPValue(code);

        if (smsReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(smsReceiver);
        }
    }

    @Override
    public void onOTPTimeOut() {
        //showToast("OTP Time out");
    }

    @Override
    public void onOTPReceivedError(String error) {
        showToast(error);
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public int validateDetails() {

        mobNo = editTextMobEmail.getText().toString().trim();
        vkid = editTextVKID.getText().toString().trim();
      //  SharedPrefUtils.getInstance(this).setVkid(vkid);


        if (TextUtils.isEmpty(mobNo)) {
            String msg = "Please enter your mobile number!";
            editTextMobEmail.requestFocus();
            editTextMobEmail.setError(msg);
            return 2;
        }

        if (!mobNo.equalsIgnoreCase("")) {
            if (!isValidMobile(mobNo)) {
                editTextMobEmail.setError("Invalid Mobile Number!");
                editTextMobEmail.requestFocus();
                return 2;
            }
        }

        if (TextUtils.isEmpty(vkid)) {
            String msg = "Please enter your VKID OR 6 digit Application ID!";
            editTextVKID.requestFocus();
            editTextVKID.setError(msg);
            return 2;
        }

        return 0;
    }

    private static boolean isValidMobile(String mobile) {

        boolean check = false;
        if (Pattern.matches("[6-9]{1}[0-9]{9}", mobile)) {
            if (mobile.length() < 9 || mobile.length() > 13) {
                check = false;
            } else {
                check = android.util.Patterns.PHONE.matcher(mobile).matches();
            }
        } else {
            check = false;
        }
        return check;
    }

    public void loginApiCall() {
        try {
            progressDialog.setMessage("Please wait....");
            progressDialog.setCancelable(false);
            progressDialog.show();

          String loginURL = Constants.URL_BASE_KDB + Constants.METHOD_LOGIN;
           //String loginURL = "https://vkms.vakrangee.in/flm-common-activity/nxtlog-usr";
            Log.i("URL@@@", loginURL);

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("mobileNo", mobNo);
            jsonBody.put("userId", vkid);
            Log.i("json!!!",jsonBody.toString());

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, loginURL, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Log.i("loginApiResp@@@", response.toString());

                        progressDialog.dismiss();

                        JSONObject jsonObject = new JSONObject(response.toString());

                        JSONObject jsonObject1 = jsonObject.getJSONObject("responseDTO");

                        String statusCode = jsonObject1.getString("statusCode");

                        appID = jsonObject1.getString("appId");

                        if (statusCode.equalsIgnoreCase("24")) {

                            validateUserUsingMobEmailId(mobNo);

                        } else {
                            Toast.makeText(context, "No such user exists", Toast.LENGTH_LONG).show();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        error.printStackTrace();
                        progressDialog.dismiss();
                        Toast.makeText(context, "Please Enter Valid login details", Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<>();
                    params.put("api-key", "0bb6c340-85cb-42f5-8a22-192637df06c5");
                    params.put("Content-Type","application/json");
                    return params;
                }
            };

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));

            requestQueue.add(jsonObjectRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
