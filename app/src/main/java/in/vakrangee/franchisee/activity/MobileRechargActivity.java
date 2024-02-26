package in.vakrangee.franchisee.activity;

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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.adapter.RelianceJIOPlanAdapter;
import in.vakrangee.franchisee.adapter.ServiceProviderAdapter;
import in.vakrangee.franchisee.task.AsyncGetRelianceJIOPlan;
import in.vakrangee.franchisee.task.AsyncRechargeRelianceJIO;
import in.vakrangee.supercore.franchisee.ifc.ServiceProviderIfc;
import in.vakrangee.supercore.franchisee.impl.ServiceProviderImpl;
import in.vakrangee.supercore.franchisee.model.RelianceJIOTariffPlanDto;
import in.vakrangee.supercore.franchisee.model.ServiceProvider;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.utils.Logger;
import in.vakrangee.supercore.franchisee.utils.RelianceJIOUtils;
import in.vakrangee.supercore.franchisee.webservice.WebService;

//

public class MobileRechargActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener, ServiceProviderIfc {

    SoapObject response = null;
    String TAG = "Response";
    Spinner spnServiceProvider;
    Button btnSubmitRecharge, btnClear, btnCancle;
    EditText edtEnterMobileNumber, edtEnterAmount;
    Toolbar toolbar;
    SoapPrimitive soapResultString;


    int spnServiceId;
    String spnSpineerVlaues;
    InternetConnection internetConnection;

    TelephonyManager telephonyManager;

    String diplayServerResopnse;
    ProgressDialog progress, progress1;
    String vkid;

    TextView getResponse;

    private GoogleApiClient client;

    ServiceProvider mServiceProvider;

    TextView getBalance;
    String intentM;

    // Reliance JIO Prepaid
    private LinearLayout layoutRelianceJIOPlan;
    private Spinner spinnerRelianceJIOPlans;
    private TextView relianceJIOPlanDescription;
    private RelianceJIOTariffPlanDto relianceJIOTariffPlanDto;
    private String relianceJIOVR;
    private RelianceJIOPlanAdapter relianceJIOPlanAdapter;

    private AsyncGetRelianceJIOPlan asyncGetRelianceJIOPlan;
    private AsyncRechargeRelianceJIO asyncRechargeRelianceJIO;
    private Logger logger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fontawesome-webfont.ttf");
        setContentView(R.layout.activity_mobilerecharge);

        logger = Logger.getInstance(MobileRechargActivity.this);

        internetConnection = new InternetConnection(MobileRechargActivity.this);
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        spnServiceProvider = (Spinner) findViewById(R.id.mserviceprovider);

        edtEnterMobileNumber = (EditText) findViewById(R.id.mmobilenumber);
        edtEnterMobileNumber.setTypeface(Typeface.SANS_SERIF);

        getResponse = (TextView) findViewById(R.id.getResponse);
        getResponse.setTypeface(Typeface.SANS_SERIF);

        edtEnterAmount = (EditText) findViewById(R.id.mmobAmount);
        edtEnterAmount.setTypeface(Typeface.SANS_SERIF);

        getBalance = (TextView) findViewById(R.id.getBalance);
        getBalance.setTypeface(Typeface.SANS_SERIF);

        btnSubmitRecharge = (Button) findViewById(R.id.msubmitRecharge);
        btnSubmitRecharge.setTypeface(font);
        btnSubmitRecharge.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + "  " + getResources().getString(R.string.submit)));

        btnClear = (Button) findViewById(R.id.mmobClear);
        btnClear.setTypeface(font);
        btnClear.setText(new SpannableStringBuilder(new String(new char[]{0xf021}) + "  " + getResources().getString(R.string.clear)));

        btnCancle = (Button) findViewById(R.id.mCancel);
        btnCancle.setTypeface(font);
        btnCancle.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + "  " + getResources().getString(R.string.cancel)));

        layoutRelianceJIOPlan = (LinearLayout) findViewById(R.id.layoutRelianceJIOPlan);
        spinnerRelianceJIOPlans = (Spinner) findViewById(R.id.spinnerRelianceJIOPlans);
        relianceJIOPlanDescription = (TextView) findViewById(R.id.relianceJIOPlanDescription);

        spinnerRelianceJIOPlans.setOnItemSelectedListener(this);

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MobileRechargActivity.this, MySerivcesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        TextView mobilerecharge = (TextView) findViewById(R.id.mobilerecharge);
        mobilerecharge.setTypeface(Typeface.SANS_SERIF);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.mobileRecharge);
        }

        if (internetConnection.isNetworkAvailable(getApplicationContext()) == false) {

            AlertDialogBoxInfo.alertDialogShow(MobileRechargActivity.this, getResources().getString(R.string.internetCheck));

        } else {

            progress1 = new ProgressDialog(MobileRechargActivity.this);
            progress1.setTitle(R.string.fetchingBalance);
            progress1.setMessage(getResources().getString(R.string.pleaseWait));
            progress1.setCancelable(false);
            progress1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress1.show();

            //AsyncgetAvailableBalance task = new AsyncgetAvailableBalance();
            //task.execute();

            in.vakrangee.franchisee.task.AsyncgetAvailableBalance task = new in.vakrangee.franchisee.task.AsyncgetAvailableBalance(getApplicationContext());
            task.delegate = this;
            task.execute();
        }

        // Load Service Provider
        loadSpinner();

        // Add Listener to Service Provider Spinner
        spnServiceProvider.setOnItemSelectedListener(this);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spnServiceProvider.setSelection(0);
                edtEnterMobileNumber.setText("");
                edtEnterAmount.setText("");
                edtEnterAmount.setError(null);
                edtEnterMobileNumber.setError(null);
                getResponse.setText("");

                // For Reliance JIO
                relianceJIOPlanDescription.setText("");
                spinnerRelianceJIOPlans.setSelection(0);
                layoutRelianceJIOPlan.setVisibility(View.GONE);
            }
        });

        btnSubmitRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int pos = spnServiceProvider.getSelectedItemPosition();
                final String mob = edtEnterMobileNumber.getText().toString();
                final String amo = edtEnterAmount.getText().toString();

                if (mob.length() != 10) {
                    edtEnterMobileNumber.setError(getResources().getString(R.string.enter10DigitesNumber));

                } else if (amo.length() <= 1 && !(spnServiceId == RelianceJIOUtils.SERVICE_ID)) {
                    edtEnterAmount.setError(getResources().getString(R.string.mimnimum2DigitsNumber));
                } else if (pos == 0) {
                    // Toast.makeText(MobileRechargActivity.this, " Select OutdoorID Provider", Toast.LENGTH_SHORT).show();
                    TextView errorText = (TextView) spnServiceProvider.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText(R.string.selectServiceProvider);//changes the selected item text to this
                } else if (internetConnection.isConnectingToInternet() == false) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MobileRechargActivity.this);
                    alertDialogBuilder
                            .setMessage(R.string.internetCheck)
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dialog.dismiss();
                                }
                            });


                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                    TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
                    textView.setTextSize(16);


                } else if (telephonyManager.getSimState() == TelephonyManager.SIM_STATE_ABSENT) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MobileRechargActivity.this);
                    alertDialogBuilder
                            .setMessage(R.string.insertSimcard)
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dialog.dismiss();
                                }
                            });


                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                    TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
                    textView.setTextSize(16);

                } else {

                    if (spnServiceId == RelianceJIOUtils.SERVICE_ID) {
                        if (spinnerRelianceJIOPlans.getSelectedItemPosition() == 0) {
                            LinearLayout linearLayout = (LinearLayout) spinnerRelianceJIOPlans.getSelectedView();
                            TextView errorText = (TextView) linearLayout.findViewById(R.id.relianceJIOPlanName);
                            errorText.setError("");
                            errorText.setTextColor(Color.RED);//just to highlight that this is an error
                            errorText.setText(R.string.selectRelianceJIOPlan);//changes the selected item text to this
                        } else {
                            showJIORechargeDialog();    // Show prompt with Reliance JIO Recharge Detail.
                        }
                    } else {

                        //create builder
                        final AlertDialog.Builder builder = new AlertDialog.Builder(MobileRechargActivity.this);
                        //inflate layout from xml. you must create an xml layout file in res/layout first
                        LayoutInflater inflater = MobileRechargActivity.this.getLayoutInflater();
                        View layout = inflater.inflate(R.layout.popupmobile, null);
                        builder.setView(layout);

                        TextView pleaseVerfity = (TextView) layout.findViewById(R.id.pleaseVerfity);
                        pleaseVerfity.setTypeface(Typeface.SANS_SERIF);


                        TextView serviceproide = (TextView) layout.findViewById(R.id.amount);
                        serviceproide.setText("\u20B9 " + edtEnterAmount.getText().toString());

                        TextView mobilenumber = (TextView) layout.findViewById(R.id.mobilenumber);
                        mobilenumber.setText(edtEnterMobileNumber.getText().toString());

                        TextView serviceprovidername = (TextView) layout.findViewById(R.id.serviceprovidername);
                        serviceprovidername.setText(mServiceProvider.getServiceDescription());

                        builder.setPositiveButton(R.string.Confirm, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                progress = new ProgressDialog(MobileRechargActivity.this);
                                progress.setTitle(R.string.transcationprogress);
                                progress.setMessage(getResources().getString(R.string.pleaseWait));
                                progress.setCancelable(false);
                                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                progress.show();

                                //  AsyncSubmitRecharge myRequest = new AsyncSubmitRecharge();
                                // myRequest.execute();
                                AsyncMobileRechagre myRequest = new AsyncMobileRechagre();
                                myRequest.execute();
                                // Toast.makeText(MobileRechargActivity.this, " Succesfull", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();

                            }
                        });

                        builder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        builder.show();
                    }
                }
            }
        });

        edtEnterMobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i != 10) {
                    // Toast.makeText(getApplicationContext(), "Please Enter 10 digits", Toast.LENGTH_SHORT).show();
                    edtEnterMobileNumber.setTextColor(Color.parseColor("#000000"));
                    edtEnterMobileNumber.setError(getResources().getString(R.string.enter10DigitesNumber));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (edtEnterMobileNumber.length() <= 9) {

                } else {
                    edtEnterMobileNumber.setTextColor(Color.parseColor("#468847"));
                    edtEnterMobileNumber.setError(null);
                    getResponse.setText("");        // Reset Response

                    if (spnServiceId == RelianceJIOUtils.SERVICE_ID) {

                        //TODO: Send Service Request to Get JIO Plan
                        asyncGetRelianceJIOPlan = new AsyncGetRelianceJIOPlan(MobileRechargActivity.this, new AsyncGetRelianceJIOPlan.Callback() {
                            @Override
                            public void onResult(String result) {
                                // Process Reliance JIO Plan Data
                                processRelianceJIOPlanData(result);
                            }
                        });
                        asyncGetRelianceJIOPlan.execute(edtEnterMobileNumber.getText().toString()); // Pass JIO Mobile Number to Get Plans
                    }
                }

                String tmp = editable.toString().trim();
                if (tmp.length() == 1 && tmp.equals("0"))
                    editable.clear();
            }

        });

        edtEnterAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i != 2) {
                    // Toast.makeText(getApplicationContext(), "Please Enter 10 digits", Toast.LENGTH_SHORT).show();
                    edtEnterAmount.setTextColor(Color.parseColor("#000000"));
                    edtEnterAmount.setError(getResources().getString(R.string.mimnimum2DigitsNumber));

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edtEnterAmount.length() <= 1) {


                } else {
                    edtEnterAmount.setTextColor(Color.parseColor("#468847"));
                    edtEnterAmount.setError(null);

                }

                String tmp = editable.toString().trim();
                if (tmp.length() == 1 && tmp.equals("0"))
                    editable.clear();
            }
        });

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    //region Load Service Provider List
    private void loadSpinner() {

        Connection connection = new Connection(this);
        // connection.openDatabase();
        ServiceProviderAdapter serviceProviderAdapter;
        List<ServiceProvider> mobileRechargeProviders = new ServiceProviderImpl().getServiceProvider(6, 6);

        // Added Reliance JIO Prepaid Recharge
        mobileRechargeProviders = RelianceJIOUtils.addRelianceJIOPrepaid(mobileRechargeProviders);

        serviceProviderAdapter = new ServiceProviderAdapter(MobileRechargActivity.this,
                android.R.layout.simple_spinner_dropdown_item, mobileRechargeProviders);

        //serviceProviderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnServiceProvider.setAdapter(serviceProviderAdapter);
    }
    //endregion

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(MobileRechargActivity.this, MySerivcesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();


                break;
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "MobileRechargActivity Page", // TODO: Define a title for the contentOpen shown.
//                // TODO: If you have web page contentOpen that matches this app activity's contentOpen,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://in.vakrangee.vkmsfinal/http/host/path")
//        );
//        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "MobileRechargActivity Page", // TODO: Define a title for the contentOpen shown.
//                // TODO: If you have web page contentOpen that matches this app activity's contentOpen,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://in.vakrangee.vkmsfinal/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
//        client.disconnect();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        int id = adapterView.getId();

        if (id == R.id.mserviceprovider) {

            mServiceProvider = (ServiceProvider) adapterView.getItemAtPosition(i);
            spnSpineerVlaues = mServiceProvider.getServiceDescription();
            spnServiceId = mServiceProvider.getServiceId();
            Log.e(TAG, "Service Id : " + spnServiceId + " Name : " + spnSpineerVlaues);


            edtEnterMobileNumber.setText("");
            edtEnterMobileNumber.setError(null);
            edtEnterAmount.setError(null);
            List<RelianceJIOTariffPlanDto> relianceJIOTariffPlanDtos = new ArrayList<RelianceJIOTariffPlanDto>();
            relianceJIOPlanAdapter = new RelianceJIOPlanAdapter(MobileRechargActivity.this, relianceJIOTariffPlanDtos);
            spinnerRelianceJIOPlans.setAdapter(relianceJIOPlanAdapter);

            if (spnServiceId == RelianceJIOUtils.SERVICE_ID) {
                edtEnterAmount.setVisibility(View.GONE);
                layoutRelianceJIOPlan.setVisibility(View.VISIBLE);
            } else {
                edtEnterAmount.setVisibility(View.VISIBLE);
                layoutRelianceJIOPlan.setVisibility(View.GONE);
            }
        } else if (id == R.id.spinnerRelianceJIOPlans) {
            relianceJIOTariffPlanDto = (RelianceJIOTariffPlanDto) adapterView.getItemAtPosition(i);
            //relianceJIOPlanDescription.setText(Html.fromHtml("<b>Description:</b><br>"+relianceJIOTariffPlanDto.getReliance_jio_tariff_plan_description()));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public List<ServiceProvider> getServiceProvider(int serviceId, int subServiceId) {
        return null;
    }

    @Override
    public void getAvailableBalance(String output) {
        try {


            String getBalanceResponse = output;

            progress1.dismiss();
            if (getBalanceResponse.equals("Invalid Request.")) {
                getBalance.setText(getBalanceResponse);
            } else if (getBalanceResponse.equals("Error occured")) {
                {
                    getBalance.setText("Error in Server");
                }
            } else {
                StringTokenizer tokens = new StringTokenizer(getBalanceResponse, "|");
                String first = tokens.nextToken();
                String second = tokens.nextToken();

                getBalance.setText(getResources().getString(R.string.rs) + second + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogBoxInfo.alertDialogShow(MobileRechargActivity.this, getResources().getString(R.string.Warning));

        }

    }

    private class AsyncMobileRechagre extends AsyncTask<Void, Void, Void> implements ServiceProviderIfc {
        String mob = edtEnterMobileNumber.getText().toString();
        String amo = edtEnterAmount.getText().toString();

        //this is the method to query

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {

                Connection connection = new Connection(MobileRechargActivity.this);
                String vkid = connection.getVkid();
                String tokenId = connection.getTokenId();

                int val = mServiceProvider.getServiceId();
//
                final String deviceIdget = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                String deviceid = EncryptionUtil.encryptString(deviceIdget, getApplicationContext());

                String deviceIDAndroid = CommonUtils.getAndroidUniqueID(getApplicationContext());
                String imei = EncryptionUtil.encryptString(deviceIDAndroid, getApplicationContext());

                String simSerial = CommonUtils.getSimSerialNumber(getApplicationContext());
                String simserialnumber = EncryptionUtil.encryptString(simSerial, getApplicationContext());

                String vkidd = EncryptionUtil.encryptString(vkid, getApplicationContext());
                String TokenId = EncryptionUtil.encryptString(tokenId, getApplicationContext());

                String ServiceProvider = EncryptionUtil.encryptString(String.valueOf(val), getApplicationContext());
                String rechargeType = EncryptionUtil.encryptString("ETOPUP", getApplicationContext());
                String mobileNumber = EncryptionUtil.encryptString(mob, getApplicationContext());
                String amount = EncryptionUtil.encryptString(amo, getApplicationContext());


                diplayServerResopnse = WebService.MobileRecharge(vkidd, TokenId, imei, deviceid, simserialnumber, ServiceProvider
                        , rechargeType, mobileNumber, amount);

                Log.d(TAG, "WebSer...ceResponse: " + diplayServerResopnse);

            } catch (Exception e) {
                String message = null;
                e.printStackTrace();
                Log.e(TAG + "Error", message);
                Log.i("TAG", ((message == null) ? "string null" : message));
                AlertDialogBoxInfo.alertDialogShow(MobileRechargActivity.this, getResources().getString(R.string.Warning));
                // Log.d("LOGCAT", "" + diplayServerResopnse);
                //  Log.d(TAG, "WebSer...ceResponse: " + diplayServerResopnse);
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");

            progress.dismiss();
            // progress.dismiss();

            try {


                String CurrentString = diplayServerResopnse;
                Log.e(TAG + "password verified", CurrentString);

                if (diplayServerResopnse.startsWith("Mobile recharged with Rs.")) {
                    getResponse.setText(diplayServerResopnse);
                    getResponse.setTextColor(Color.GREEN);


                    spnServiceProvider.setSelection(0);
                    edtEnterMobileNumber.setText("");
                    edtEnterAmount.setText("");
                    edtEnterAmount.setError(null);
                    edtEnterMobileNumber.setError(null);


                } else {
                    getResponse.setText(diplayServerResopnse);
                    getResponse.setTextColor(Color.RED);

                    spnServiceProvider.setSelection(0);
                    edtEnterMobileNumber.setText("");
                    edtEnterAmount.setText("");
                    edtEnterAmount.setError(null);
                    edtEnterMobileNumber.setError(null);

                }

                in.vakrangee.franchisee.task.AsyncgetAvailableBalance task = new in.vakrangee.franchisee.task.AsyncgetAvailableBalance(getApplicationContext());
                task.delegate = this;
                task.execute();

                /*if (diplayServerResopnse == null) {

                    String message = null;
                    Log.i("TAG", ((message == null) ? "string null" : message));


                } else {
                    Log.e(TAG + "Error in Server", diplayServerResopnse);
                    // Toast.makeText(getApplicationContext(), "Error OTP ", Toast.LENGTH_SHORT).show();

                }*/
            } catch (Exception e) {
                e.printStackTrace();
                AlertDialogBoxInfo.alertDialogShow(MobileRechargActivity.this, getResources().getString(R.string.Warning));
            }

        }

        @Override
        public List<ServiceProvider> getServiceProvider(int serviceId, int subServiceId) {
            return null;
        }

        @Override
        public void getAvailableBalance(String output) {
            String getBalacneInfo = output;
            // Toast.makeText(MobileRechargActivity.this,   aa, Toast.LENGTH_LONG).show();
            try {


                progress1.dismiss();
                if (getBalacneInfo.equals("Invalid Request.")) {
                    getBalance.setText(getBalacneInfo);
                } else if (getBalacneInfo.equals("Error occured")) {
                    {
                        getBalance.setText("Error in Server");
                    }
                } else {
                    StringTokenizer tokens = new StringTokenizer(getBalacneInfo, "|");
                    String first = tokens.nextToken();
                    String second = tokens.nextToken();
                    //  Toast.makeText(MobileRechargActivity.this,   second, Toast.LENGTH_LONG).show();
                    getBalance.setText(getResources().getString(R.string.rs) + second + "");

                }
            } catch (Exception e) {
                e.getMessage();
                e.printStackTrace();
                AlertDialogBoxInfo.alertDialogShow(MobileRechargActivity.this, getResources().getString(R.string.Warning));
            }


        }
    }

    public void onBackPressed() {
        Intent intent = new Intent(MobileRechargActivity.this, MySerivcesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    //region Process Reliance JIO Plans Response
    public void processRelianceJIOPlanData(String result) {
        try {

            //result = "OKAY|{\"accounts\":[{\"circleId\":\"MU\"}],\"segment\":[{\"name\":\"10\",\"value\":\"027001\"}],\"products\":[{\"characteristics\":[{\"name\":\"PRODUCT_STATE\",\"value\":\"MH\"}],\"billingType\":\"1\",\"customerFacingServices\":[{\"serviceType\":\"Z0003\"},{\"serviceType\":\"Z0002\"}],\"type\":\"1\",\"identifier\":{\"value\":\"9082666780\"}},{\"characteristics\":[{\"name\":\"PRODUCT_STATE\",\"value\":\"MH\"}],\"billingType\":\"1\",\"customerFacingServices\":[{\"serviceType\":\"Z0006\"}],\"type\":\"1\",\"identifier\":{\"value\":\"400116333022\"}}],\"success\":\"true\"}|[{\"reliance_jio_tariff_plan_id\":\"1000853\",\"reliance_jio_tariff_plan_description\":\"Get a talk time of Rs. 7.70 on a top-Up of Rs. 10\",\"reliance_jio_tariff_plan_name\":\"Top-Up of Rs. 10\",\"reliance_jio_tariff_plan_amount\":10},{\"reliance_jio_tariff_plan_id\":\"1003919\",\"reliance_jio_tariff_plan_description\":\"Benefits: Unlimited Data (400MB High Speed Data, thereafter unlimited at 64Kbps). Validity: Base Plans validity\",\"reliance_jio_tariff_plan_name\":\"ADD-ON - MRP 11\",\"reliance_jio_tariff_plan_amount\":11},{\"reliance_jio_tariff_plan_id\":\"1000854\",\"reliance_jio_tariff_plan_description\":\"Get a talk time of Rs. 15.39 on a top-Up of Rs. 20\",\"reliance_jio_tariff_plan_name\":\"Top-Up of Rs. 20\",\"reliance_jio_tariff_plan_amount\":20},{\"reliance_jio_tariff_plan_id\":\"1003925\",\"reliance_jio_tariff_plan_description\":\"Benefits: Unlimited Data (1GB High Speed Data, thereafter unlimited at 64Kbps). Validity: Base Plans validity\",\"reliance_jio_tariff_plan_name\":\"ADD-ON - MRP 21\",\"reliance_jio_tariff_plan_amount\":21},{\"reliance_jio_tariff_plan_id\":\"1003739\",\"reliance_jio_tariff_plan_description\":\"Benefits: Free Voice, Unlimited Data (1GB High Speed Data, thereafter unlimited at 64Kbps), 50SMS and Complimentary Subscription to Jio Apps. Validity: 28days\",\"reliance_jio_tariff_plan_name\":\"JioPhone MRP 49\",\"reliance_jio_tariff_plan_amount\":49},{\"reliance_jio_tariff_plan_id\":\"1000855\",\"reliance_jio_tariff_plan_description\":\"Get a talk time of Rs. 40.48 on a top-Up of Rs. 50\",\"reliance_jio_tariff_plan_name\":\"Top-Up of Rs. 50\",\"reliance_jio_tariff_plan_amount\":50},{\"reliance_jio_tariff_plan_id\":\"1003920\",\"reliance_jio_tariff_plan_description\":\"Benefits: Unlimited Data (3GB High Speed Data, thereafter unlimited at 64Kbps). Validity: Base Plans validity\",\"reliance_jio_tariff_plan_name\":\"ADD-ON - MRP 51\",\"reliance_jio_tariff_plan_amount\":51},{\"reliance_jio_tariff_plan_id\":\"1000856\",\"reliance_jio_tariff_plan_description\":\"Get full talk time of Rs. 100 on a top-Up of Rs. 100\",\"reliance_jio_tariff_plan_name\":\"Top-Up of Rs. 100\",\"reliance_jio_tariff_plan_amount\":100},{\"reliance_jio_tariff_plan_id\":\"1003924\",\"reliance_jio_tariff_plan_description\":\"Benefits: Unlimited Data (6GB High Speed Data, thereafter unlimited at 64Kbps). Validity: Base Plans validity\",\"reliance_jio_tariff_plan_name\":\"ADD-ON - MRP 101\",\"reliance_jio_tariff_plan_amount\":101},{\"reliance_jio_tariff_plan_id\":\"1000857\",\"reliance_jio_tariff_plan_description\":\"Get full talk time of Rs. 150 on a top-Up of Rs. 150\",\"reliance_jio_tariff_plan_name\":\"Top-Up of Rs. 150\",\"reliance_jio_tariff_plan_amount\":150},{\"reliance_jio_tariff_plan_id\":\"1003903\",\"reliance_jio_tariff_plan_description\":\"Benefits: Free Voice, Unlimited Data (1.5GB High Speed Data per day, thereafter unlimited at 64Kbps), 100SMS/day and Complimentary Subscription to Jio Apps. Validity: 28days\",\"reliance_jio_tariff_plan_name\":\"JioPhone MRP 153\",\"reliance_jio_tariff_plan_amount\":153},{\"reliance_jio_tariff_plan_id\":\"1003891\",\"reliance_jio_tariff_plan_description\":\"Get Jio Prime Membership and the benefits of INR 98 plan,Benefits: Free Voice, Unlimited Data (2GB High Speed Data, thereafter unlimited at 64Kbps), 300SMS and Complimentary Subscription to Jio Apps. Validity: 28days\",\"reliance_jio_tariff_plan_name\":\"PRIME 99   MRP 98\",\"reliance_jio_tariff_plan_amount\":197},{\"reliance_jio_tariff_plan_id\":\"1000858\",\"reliance_jio_tariff_plan_description\":\"Get full talk time of Rs. 200 on a top-Up of Rs. 200\",\"reliance_jio_tariff_plan_name\":\"Top-Up of Rs. 200\",\"reliance_jio_tariff_plan_amount\":200},{\"reliance_jio_tariff_plan_id\":\"1003890\",\"reliance_jio_tariff_plan_description\":\"Benefits: Free Voice, Unlimited Data (1.5GB High Speed Data per day, thereafter unlimited at 64Kbps), 100SMS/day and Complimentary Subscription to Jio Apps. Validity: 28days\",\"reliance_jio_tariff_plan_name\":\"PRIME 99   MRP 149\",\"reliance_jio_tariff_plan_amount\":248},{\"reliance_jio_tariff_plan_id\":\"1003900\",\"reliance_jio_tariff_plan_description\":\"Benefits: Free Voice, Unlimited Data (2GB High Speed Data per day, thereafter unlimited at 64Kbps), 100SMS/day and Complimentary Subscription to Jio Apps. Validity: 28days\",\"reliance_jio_tariff_plan_name\":\"PRIME 99   MRP 198\",\"reliance_jio_tariff_plan_amount\":297},{\"reliance_jio_tariff_plan_id\":\"1000859\",\"reliance_jio_tariff_plan_description\":\"Get full talk time of Rs. 300 on a top-Up of Rs. 300\",\"reliance_jio_tariff_plan_name\":\"Top-Up of Rs. 300\",\"reliance_jio_tariff_plan_amount\":300},{\"reliance_jio_tariff_plan_id\":\"1004192\",\"reliance_jio_tariff_plan_description\":\"Get Jio Prime Membership and the benefits of INR 251 plan.\\r\\nBenefits: Unlimited Data (2GB High Speed Data per day, thereafter unlimited at 64Kbps), Voice: NA, SMS:NA, Validity: 51 days\\r\\nData can be used to access all Internet content\",\"reliance_jio_tariff_plan_name\":\"PRIME 99   JIO CRICKET SEASON PACK\",\"reliance_jio_tariff_plan_amount\":350},{\"reliance_jio_tariff_plan_id\":\"1003892\",\"reliance_jio_tariff_plan_description\":\"Benefits: Free Voice, Unlimited Data (3GB High Speed Data per day, thereafter unlimited at 64Kbps), 100SMS/day and Complimentary Subscription to Jio Apps. Validity: 28days\",\"reliance_jio_tariff_plan_name\":\"PRIME 99   MRP 299\",\"reliance_jio_tariff_plan_amount\":398},{\"reliance_jio_tariff_plan_id\":\"1003894\",\"reliance_jio_tariff_plan_description\":\"Benefits: Free Voice, Unlimited Data (1.5GB High Speed Data per day, thereafter unlimited at 64Kbps), 100SMS/day and Complimentary Subscription to Jio Apps. Validity:70days\",\"reliance_jio_tariff_plan_name\":\"PRIME 99   MRP 349\",\"reliance_jio_tariff_plan_amount\":448},{\"reliance_jio_tariff_plan_id\":\"1003898\",\"reliance_jio_tariff_plan_description\":\"Benefits: Free Voice, Unlimited Data (2GB High Speed Data per day, thereafter unlimited at 64Kbps), 100SMS/day and Complimentary Subscription to Jio Apps. Validity: 70days\",\"reliance_jio_tariff_plan_name\":\"PRIME 99   MRP 398\",\"reliance_jio_tariff_plan_amount\":497},{\"reliance_jio_tariff_plan_id\":\"1003897\",\"reliance_jio_tariff_plan_description\":\"Benefits: Free Voice, Unlimited Data (1.5GB High Speed Data per day, thereafter unlimited at 64Kbps), 100SMS/day and Complimentary Subscription to Jio Apps. Validity: 84days\",\"reliance_jio_tariff_plan_name\":\"PRIME 99   MRP 399\",\"reliance_jio_tariff_plan_amount\":498},{\"reliance_jio_tariff_plan_id\":\"1000860\",\"reliance_jio_tariff_plan_description\":\"Get full talk time of Rs. 500 on a top-Up of Rs. 500\",\"reliance_jio_tariff_plan_name\":\"Top-Up of Rs. 500\",\"reliance_jio_tariff_plan_amount\":500},{\"reliance_jio_tariff_plan_id\":\"1003896\",\"reliance_jio_tariff_plan_description\":\"Benefits: Free Voice, Unlimited Data (2GB High Speed Data per day, thereafter unlimited at 64Kbps), 100SMS/day and Complimentary Subscription to Jio Apps. Validity: 84days\",\"reliance_jio_tariff_plan_name\":\"PRIME 99   MRP 448\",\"reliance_jio_tariff_plan_amount\":547},{\"reliance_jio_tariff_plan_id\":\"1003899\",\"reliance_jio_tariff_plan_description\":\"Benefits: Free Voice, Unlimited Data (1.5GB High Speed Data per day, thereafter unlimited at 64Kbps), 100SMS/day and Complimentary Subscription to Jio Apps. Validity: 91days\",\"reliance_jio_tariff_plan_name\":\"PRIME 99   MRP 449\",\"reliance_jio_tariff_plan_amount\":548},{\"reliance_jio_tariff_plan_id\":\"1003895\",\"reliance_jio_tariff_plan_description\":\"Benefits: Free Voice, Unlimited Data (2GB High Speed Data per day, thereafter unlimited at 64Kbps), 100SMS/day and Complimentary Subscription to Jio Apps. Validity: 91days\",\"reliance_jio_tariff_plan_name\":\"PRIME 99   MRP 498\",\"reliance_jio_tariff_plan_amount\":597},{\"reliance_jio_tariff_plan_id\":\"1003893\",\"reliance_jio_tariff_plan_description\":\"Benefits: Free Voice, Unlimited Data (4GB High Speed Data per day, thereafter unlimited at 64Kbps), 100SMS/day and Complimentary Subscription to Jio Apps. Validity: 28days\",\"reliance_jio_tariff_plan_name\":\"PRIME 99   MRP 509\",\"reliance_jio_tariff_plan_amount\":608},{\"reliance_jio_tariff_plan_id\":\"1000861\",\"reliance_jio_tariff_plan_description\":\"Get full talk time of Rs. 750 on a top-Up of Rs. 750\",\"reliance_jio_tariff_plan_name\":\"Top-Up of Rs. 750\",\"reliance_jio_tariff_plan_amount\":750},{\"reliance_jio_tariff_plan_id\":\"1000862\",\"reliance_jio_tariff_plan_description\":\"Get full talk time of Rs. 1000 on a top-Up of Rs. 1000\",\"reliance_jio_tariff_plan_name\":\"Top-Up of Rs. 1000\",\"reliance_jio_tariff_plan_amount\":1000},{\"reliance_jio_tariff_plan_id\":\"1003040\",\"reliance_jio_tariff_plan_description\":\"Benefits: Free Voice, Unlimited Data (60GB High Speed Data, thereafter unlimited at 64Kbps), 100SMS/day & Complimentary Subscription to Jio Apps. Validity: 90days\",\"reliance_jio_tariff_plan_name\":\"PRIME 99   MRP 999\",\"reliance_jio_tariff_plan_amount\":1098},{\"reliance_jio_tariff_plan_id\":\"1000863\",\"reliance_jio_tariff_plan_description\":\"Get full talk time of Rs. 2000 on a top-Up of Rs. 2000\",\"reliance_jio_tariff_plan_name\":\"Top-Up of Rs. 2000\",\"reliance_jio_tariff_plan_amount\":2000},{\"reliance_jio_tariff_plan_id\":\"1003042\",\"reliance_jio_tariff_plan_description\":\"Benefits: Free Voice, Unlimited Data (125GB High Speed Data, thereafter unlimited at 64Kbps), 100SMS/day & Complimentary Subscription to Jio Apps. Validity: 180days\",\"reliance_jio_tariff_plan_name\":\"PRIME 99   MRP 1999\",\"reliance_jio_tariff_plan_amount\":2098},{\"reliance_jio_tariff_plan_id\":\"1000864\",\"reliance_jio_tariff_plan_description\":\"Get full talk time of Rs. 5000 on a top-Up of Rs. 5000\",\"reliance_jio_tariff_plan_name\":\"Top-Up of Rs. 5000\",\"reliance_jio_tariff_plan_amount\":5000},{\"reliance_jio_tariff_plan_id\":\"1003043\",\"reliance_jio_tariff_plan_description\":\"Benefits: Free Voice, Unlimited Data (350GB High Speed Data, thereafter unlimited at 64Kbps), 100SMS/day & Complimentary Subscription to Jio Apps. Validity: 360days\",\"reliance_jio_tariff_plan_name\":\"PRIME 99   MRP 4999\",\"reliance_jio_tariff_plan_amount\":5098},{\"reliance_jio_tariff_plan_id\":\"1003044\",\"reliance_jio_tariff_plan_description\":\"Benefits: Free Voice, Unlimited Data (750GB High Speed Data, thereafter unlimited at 64Kbps), 100SMS/day & Complimentary Subscription to Jio Apps. Validity: 360days\",\"reliance_jio_tariff_plan_name\":\"PRIME 99   MRP 9999\",\"reliance_jio_tariff_plan_amount\":10098}]";

            if (TextUtils.isEmpty(result)) {
                AlertDialogBoxInfo.alertDialogShow(MobileRechargActivity.this, getResources().getString(R.string.Warning));
                return;
            }

            if (result.startsWith("OKAY")) {
                StringTokenizer tokens = new StringTokenizer(result, "|");
                String strStatus = tokens.nextToken();          // OKAY Status
                String strVR = tokens.nextToken();              // Validate Response Data
                String strPlans = tokens.nextToken();           // Plan Details

                relianceJIOVR = strVR;

                //TODO: Convert JSON data into List & Populate plans into spinner
                layoutRelianceJIOPlan.setVisibility(View.VISIBLE);
                List<RelianceJIOTariffPlanDto> relianceJIOTariffPlanDtos = RelianceJIOUtils.convertJSONToRelianceJIOPlanList(strPlans);
                relianceJIOPlanAdapter = new RelianceJIOPlanAdapter(MobileRechargActivity.this, relianceJIOTariffPlanDtos);
                spinnerRelianceJIOPlans.setAdapter(relianceJIOPlanAdapter);

            } else if (result.startsWith("ERROR")) {
                edtEnterMobileNumber.setText("");

                /*StringTokenizer tokens = new StringTokenizer(result, "|");
                String strStatus = tokens.nextToken();          // ERROR Status
                String strMsg = tokens.nextToken();             // Error Message*/

                String strMsg = result.replace("ERROR|", "");
                AlertDialogBoxInfo.alertDialogShow(MobileRechargActivity.this, strMsg);

                edtEnterMobileNumber.requestFocus();
            } else {
                edtEnterMobileNumber.setText("");
                AlertDialogBoxInfo.alertDialogShow(MobileRechargActivity.this, getResources().getString(R.string.Warning));
            }
        } catch (Exception e) {
            edtEnterMobileNumber.setText("");
            e.printStackTrace();
            AlertDialogBoxInfo.alertDialogShow(MobileRechargActivity.this, getResources().getString(R.string.Warning));
        }
    }

    public void showJIORechargeDialog() {

        //create builder
        final AlertDialog.Builder builder = new AlertDialog.Builder(MobileRechargActivity.this);
        //inflate layout from xml. you must create an xml layout file in res/layout first
        LayoutInflater inflater = MobileRechargActivity.this.getLayoutInflater();
        View layout = inflater.inflate(R.layout.popupmobile, null);
        builder.setView(layout);

        TextView pleaseVerfity = (TextView) layout.findViewById(R.id.pleaseVerfity);
        pleaseVerfity.setTypeface(Typeface.SANS_SERIF);

        TextView serviceproide = (TextView) layout.findViewById(R.id.amount);
        serviceproide.setText("\u20B9 " + relianceJIOTariffPlanDto.getReliance_jio_tariff_plan_amount());

        TextView mobilenumber = (TextView) layout.findViewById(R.id.mobilenumber);
        mobilenumber.setText(edtEnterMobileNumber.getText().toString());

        TextView serviceprovidername = (TextView) layout.findViewById(R.id.serviceprovidername);
        serviceprovidername.setText(mServiceProvider.getServiceDescription());

        builder.setPositiveButton(R.string.Confirm, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                progress = new ProgressDialog(MobileRechargActivity.this);
                progress.setTitle(R.string.transcationprogress);
                progress.setMessage(getResources().getString(R.string.pleaseWait));
                progress.setCancelable(false);
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.show();

                // Call Recharge Reliance JIO Async Task
                asyncRechargeRelianceJIO = new AsyncRechargeRelianceJIO(MobileRechargActivity.this, new AsyncRechargeRelianceJIO.Callback() {
                    @Override
                    public void onResult(String result) {
                        // Process Recharge Reliance JIO Data
                        try {
                            progress.dismiss();

                            if (TextUtils.isEmpty(result)) {
                                AlertDialogBoxInfo.alertDialogShow(MobileRechargActivity.this, getResources().getString(R.string.Warning));
                                return;
                            }
                            diplayServerResopnse = result;
                            if (diplayServerResopnse.startsWith("Mobile recharged with Rs.")) {
                                getResponse.setText(diplayServerResopnse);
                                getResponse.setTextColor(Color.GREEN);
                            } else {
                                getResponse.setText(diplayServerResopnse);
                                getResponse.setTextColor(Color.RED);
                            }

                            spnServiceProvider.setSelection(0);
                            edtEnterMobileNumber.setText("");
                            edtEnterAmount.setText("");
                            edtEnterAmount.setError(null);
                            edtEnterMobileNumber.setError(null);
                            spinnerRelianceJIOPlans.setSelection(0);
                            layoutRelianceJIOPlan.setVisibility(View.GONE);

                            in.vakrangee.franchisee.task.AsyncgetAvailableBalance task = new in.vakrangee.franchisee.task.AsyncgetAvailableBalance(getApplicationContext());
                            task.delegate = MobileRechargActivity.this;
                            task.execute();

                        } catch (Exception e) {
                            edtEnterMobileNumber.setText("");
                            e.printStackTrace();
                            AlertDialogBoxInfo.alertDialogShow(MobileRechargActivity.this, getResources().getString(R.string.Warning));
                        }
                    }
                });


                asyncRechargeRelianceJIO.execute(edtEnterMobileNumber.getText().toString(),
                        relianceJIOTariffPlanDto.getReliance_jio_tariff_plan_amount() + "",
                        relianceJIOVR, relianceJIOTariffPlanDto.getReliance_jio_tariff_plan_id()); // Pass JIO Mobile Number to Get Plans

                dialog.dismiss();

            }
        });

        builder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();

    }

    //endregion

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Cancel Async Task
        if (asyncGetRelianceJIOPlan != null && !asyncGetRelianceJIOPlan.isCancelled()) {
            asyncGetRelianceJIOPlan.cancel(true);
        }
    }
}
