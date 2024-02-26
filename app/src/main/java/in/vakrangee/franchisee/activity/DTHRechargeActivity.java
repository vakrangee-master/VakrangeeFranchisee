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
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.ksoap2.serialization.SoapPrimitive;

import java.util.List;
import java.util.StringTokenizer;

import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.adapter.ServiceProviderAdapter;
import in.vakrangee.franchisee.task.AsyncgetAvailableBalance;
import in.vakrangee.supercore.franchisee.ifc.ServiceProviderIfc;
import in.vakrangee.supercore.franchisee.impl.ServiceProviderImpl;
import in.vakrangee.supercore.franchisee.model.ServiceProvider;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.webservice.WebService;
//

public class DTHRechargeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, ServiceProviderIfc {
    String TAG = "Response";
    Spinner serviceprovider;
    Button btnSubmitDTHRecharge, btnClear, btnCancle;
    EditText edtEnterDTHNumber, edtEnterAmount;
    Toolbar toolbar;
    SoapPrimitive soapResultStringVlaues;
    TelephonyManager telephonyManager;
    ProgressDialog progress, progress1;
    String diplayServerResopnse;
    String spineervlaues;
    InternetConnection internetConnection;
    private GoogleApiClient client;

    ServiceProvider mserviceProvider;

    TextView getBalance;
    TextView getResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        final Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fontawesome-webfont.ttf");

        setContentView(R.layout.activity_dthrecharge);


        if (internetConnection.isNetworkAvailable(getApplicationContext()) == false) {

            AlertDialogBoxInfo.alertDialogShow(DTHRechargeActivity.this, getResources().getString(R.string.internetCheck));

        } else {
            progress1 = new ProgressDialog(DTHRechargeActivity.this);
            progress1.setTitle(R.string.fetchingBalance);
            progress1.setMessage(getResources().getString(R.string.pleaseWait));
            progress1.setCancelable(false);
            progress1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress1.show();

            AsyncgetAvailableBalance task = new AsyncgetAvailableBalance(getApplicationContext());
            task.delegate = this;
            task.execute();
        }


        TextView dthrecharge = (TextView) findViewById(R.id.dthrecharge);
        dthrecharge.setTypeface(Typeface.SANS_SERIF);

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //Toast.makeText(DTHRechargeActivity.this, " Succesfull"+aa, Toast.LENGTH_SHORT).show();
        serviceprovider = (Spinner) findViewById(R.id.dserviceprovider);
        edtEnterDTHNumber = (EditText) findViewById(R.id.dmobilenumber);
        edtEnterDTHNumber.setTypeface(Typeface.SANS_SERIF);
        edtEnterAmount = (EditText) findViewById(R.id.dmobAmount);
        edtEnterAmount.setTypeface(Typeface.SANS_SERIF);

        getBalance = (TextView) findViewById(R.id.getBalance);
        getBalance.setTypeface(Typeface.SANS_SERIF);
        getResponse = (TextView) findViewById(R.id.getResponse);
        getResponse.setTypeface(Typeface.SANS_SERIF);

        btnSubmitDTHRecharge = (Button) findViewById(R.id.dsubmitRecharge);
        btnSubmitDTHRecharge.setTypeface(font);
        btnSubmitDTHRecharge.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " + getResources().getString(R.string.submit)));


        btnClear = (Button) findViewById(R.id.dmobCancel);
        btnClear.setTypeface(font);
        btnClear.setText(new SpannableStringBuilder(new String(new char[]{0xf021}) + " " + getResources().getString(R.string.clear)));


        btnCancle = (Button) findViewById(R.id.mCancel);
        btnCancle.setTypeface(font);
        btnCancle.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + "  " + getResources().getString(R.string.cancel)));

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DTHRechargeActivity.this, MySerivcesActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.dth);

//            final Drawable upArrow = getResources().getDrawable(R.drawable.back);
//            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//            getSupportActionBar().setHomeAsUpIndicator(upArrow);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        loadSpinner();


        serviceprovider.setOnItemSelectedListener(this);

        internetConnection = new InternetConnection(DTHRechargeActivity.this);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serviceprovider.setSelection(0);
                edtEnterDTHNumber.setText("");
                edtEnterAmount.setText("");
                edtEnterAmount.setError(null);
                edtEnterDTHNumber.setError(null);
                getResponse.setText("");
            }
        });


        btnSubmitDTHRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int pos = serviceprovider.getSelectedItemPosition();
                final String mob = edtEnterDTHNumber.getText().toString();
                final String amo = edtEnterAmount.getText().toString();
                if (mob.length() <= 6) {
                    edtEnterDTHNumber.setError(getResources().getString(R.string.enter10DigitsCustomerId));

                } else if (amo.length() <= 1) {
                    edtEnterAmount.setError(getResources().getString(R.string.mimnimum2DigitsNumber));
                } else if (pos == 0) {
                    // Toast.makeText(DTHRechargeActivity.this, " Select OutdoorID Provider", Toast.LENGTH_SHORT).show();
                    TextView errorText = (TextView) serviceprovider.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText(R.string.selectServiceProvider);//changes the selected item text to this
                } else if (internetConnection.isConnectingToInternet() == false) {
                    AlertDialogBoxInfo.alertDialogShow(DTHRechargeActivity.this, getResources().getString(R.string.internetCheck));


                } else if (telephonyManager.getSimState() == TelephonyManager.SIM_STATE_ABSENT) {
                    AlertDialogBoxInfo.alertDialogShow(DTHRechargeActivity.this, getResources().getString(R.string.insertSimcard));


                } else {

                    //create builder
                    final AlertDialog.Builder builder = new AlertDialog.Builder(DTHRechargeActivity.this);
                    //inflate layout from xml. you must create an xml layout file in res/layout first
                    LayoutInflater inflater = DTHRechargeActivity.this.getLayoutInflater();
                    View layout = inflater.inflate(R.layout.popupdth, null);
                    builder.setView(layout);
                    TextView dthvertify = (TextView) layout.findViewById(R.id.dthverify);
                    dthvertify.setTypeface(Typeface.SANS_SERIF);

                    TextView serviceproide = (TextView) layout.findViewById(R.id.amount);
                    serviceproide.setText("\u20B9 " + edtEnterAmount.getText().toString());
                    TextView mobilenumber = (TextView) layout.findViewById(R.id.mobilenumber);
                    mobilenumber.setText(edtEnterDTHNumber.getText().toString());
                    TextView serviceprovidername = (TextView) layout.findViewById(R.id.serviceprovidername);
                    serviceprovidername.setText(mserviceProvider.getServiceDescription());

                    builder.setPositiveButton(R.string.Confirm, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            progress = new ProgressDialog(DTHRechargeActivity.this);
                            progress.setTitle(R.string.transcationprogress);
                            progress.setMessage(getResources().getString(R.string.pleaseWait));
                            progress.setCancelable(false);
                            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            progress.show();
                            //AsyncSubmitDTH myRequest = new AsyncSubmitDTH();
                            //myRequest.execute();

                            AsyncMobileRechagre myRequest = new AsyncMobileRechagre();
                            myRequest.execute();
                            //Toast.makeText(DTHRechargeActivity.this, " Succesfull", Toast.LENGTH_SHORT).show();
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
        });

        edtEnterDTHNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i != 10) {
                    // Toast.makeText(getApplicationContext(), "Please Enter 10 digits", Toast.LENGTH_SHORT).show();
                    edtEnterDTHNumber.setTextColor(Color.parseColor("#000000"));
                    edtEnterDTHNumber.setError(getResources().getString(R.string.enter10DigitsCustomerId));

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {


                if (edtEnterDTHNumber.length() <= 6) {


                } else {
                    edtEnterDTHNumber.setTextColor(Color.parseColor("#468847"));
                    edtEnterDTHNumber.setError(null);

                }
//                String tmp = editable.toString().trim();
//                if(tmp.length()==1 && tmp.equals("0"))
//                    editable.clear();
            }
        });

        edtEnterAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i != 2) {

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


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }


    private void loadSpinner() {

        Connection connection = new Connection(this);
        // connection.openDatabase();
        ServiceProviderAdapter serviceProviderAdapter;
        List<ServiceProvider> serviceProvider = new ServiceProviderImpl().getServiceProvider(6, 11);
        serviceProviderAdapter = new ServiceProviderAdapter(DTHRechargeActivity.this,
                android.R.layout.simple_spinner_dropdown_item, serviceProvider);


        serviceProviderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serviceprovider.setAdapter(serviceProviderAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        mserviceProvider = (ServiceProvider) adapterView.getItemAtPosition(i);
        spineervlaues = mserviceProvider.getServiceDescription();

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
        String getAvailableBal = output;
        progress1.dismiss();
        try {


            if (getAvailableBal.equals("Invalid Request.")) {
                getBalance.setText(getAvailableBal);
            } else if (getAvailableBal.equals("Error occured")) {
                {
                    getBalance.setText("Error in Server");
                }
            } else {
                StringTokenizer tokens = new StringTokenizer(getAvailableBal, "|");
                String first = tokens.nextToken();
                String second = tokens.nextToken();

                getBalance.setText(getResources().getString(R.string.rs) + second + "");
            }
        } catch (Exception e) {
            AlertDialogBoxInfo.alertDialogShow(DTHRechargeActivity.this, getResources().getString(R.string.Warning));
            e.printStackTrace();
        }

    }


    public void onBackPressed() {

        Intent intent = new Intent(DTHRechargeActivity.this, MySerivcesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(DTHRechargeActivity.this, MySerivcesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

                break;
        }
        return true;
    }


    private class AsyncMobileRechagre extends AsyncTask<Void, Void, Void> implements ServiceProviderIfc {
        String mob = edtEnterDTHNumber.getText().toString();
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

                Connection connection = new Connection(DTHRechargeActivity.this);
                String vkid = connection.getVkid();
                String tokenId = connection.getTokenId();
                int val = mserviceProvider.getServiceId();
                // int val = mServiceProvider.getServiceId();
//

                final String deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                String deviceid = EncryptionUtil.encryptString(deviceId, getApplicationContext());

                String deviceIDAndroid = CommonUtils.getAndroidUniqueID(getApplicationContext());
                String imei = EncryptionUtil.encryptString(deviceIDAndroid, getApplicationContext());

                String simSerial = CommonUtils.getSimSerialNumber(getApplicationContext());
                String simserialnumber = EncryptionUtil.encryptString(simSerial, getApplicationContext());

                String vkidd = EncryptionUtil.encryptString(vkid, getApplicationContext());
                String TokenId = EncryptionUtil.encryptString(tokenId, getApplicationContext());

                String ServiceProvider = EncryptionUtil.encryptString(String.valueOf(val), getApplicationContext());
                String rechargeType = EncryptionUtil.encryptString("DTH", getApplicationContext());
                String mobileNumber = EncryptionUtil.encryptString(mob, getApplicationContext());
                String amount = EncryptionUtil.encryptString(amo, getApplicationContext());


                diplayServerResopnse = WebService.MobileRecharge(vkidd, TokenId, imei, deviceid, simserialnumber, ServiceProvider
                        , rechargeType, mobileNumber, amount);

                Log.d(TAG, "WebSer...ceResponse: " + diplayServerResopnse);

            } catch (Exception e) {
                AlertDialogBoxInfo.alertDialogShow(DTHRechargeActivity.this, getResources().getString(R.string.Warning));

                e.printStackTrace();
                String message = null;
                Log.e(TAG + "Error", message);
                Log.i("TAG", ((message == null) ? "string null" : message));

            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");

            progress.dismiss();
            // progress.dismiss();
            try {


                if (diplayServerResopnse.startsWith("DTH recharged with Rs.")) {
                    getResponse.setText(diplayServerResopnse);
                    //getResponse.setTextColor(Color.GREEN);
                    getResponse.setTextColor(Color.parseColor("#468847"));
                    serviceprovider.setSelection(0);
                    edtEnterDTHNumber.setText("");
                    edtEnterAmount.setText("");
                    edtEnterAmount.setError(null);
                    edtEnterDTHNumber.setError(null);

                } else {
                    getResponse.setText(diplayServerResopnse);
                    getResponse.setTextColor(Color.RED);

                    serviceprovider.setSelection(0);
                    edtEnterDTHNumber.setText("");
                    edtEnterAmount.setText("");
                    edtEnterAmount.setError(null);
                    edtEnterDTHNumber.setError(null);
                }


                in.vakrangee.franchisee.task.AsyncgetAvailableBalance task = new in.vakrangee.franchisee.task.AsyncgetAvailableBalance(getApplicationContext());
                task.delegate = this;
                task.execute();

                if (diplayServerResopnse == null) {

                    String message = null;
                    Log.i("TAG", ((message == null) ? "string null" : message));

                    //  Log.e(TAG + "Please Null error", diplayServerResopnse);


                } else {
                    Log.e(TAG + "Error in Server", diplayServerResopnse);
                    // Toast.makeText(getApplicationContext(), "Error OTP ", Toast.LENGTH_SHORT).show();

                }
            } catch (Exception e) {
                AlertDialogBoxInfo.alertDialogShow(DTHRechargeActivity.this, getResources().getString(R.string.Warning));

                e.printStackTrace();
            }

        }


        @Override
        public List<ServiceProvider> getServiceProvider(int serviceId, int subServiceId) {
            return null;
        }

        @Override
        public void getAvailableBalance(String output) {
            String getAvaliableBal = output;
            // Toast.makeText(MobileRechargActivity.this,   aa, Toast.LENGTH_LONG).show();

            progress1.dismiss();

            try {


                if (getAvaliableBal.equals("Invalid Request.")) {
                    getBalance.setText(getAvaliableBal);
                } else if (getAvaliableBal.equals("Error occured")) {
                    {
                        getBalance.setText("Error in Server");
                    }
                } else {
                    StringTokenizer tokens = new StringTokenizer(getAvaliableBal, "|");
                    String first = tokens.nextToken();
                    String second = tokens.nextToken();
                    //  Toast.makeText(MobileRechargActivity.this,   second, Toast.LENGTH_LONG).show();
                    getBalance.setText(getResources().getString(R.string.rs) + second + "");
                }

            } catch (Exception e) {
                AlertDialogBoxInfo.alertDialogShow(DTHRechargeActivity.this, getResources().getString(R.string.Warning));

                e.printStackTrace();
            }
        }
    }

}
