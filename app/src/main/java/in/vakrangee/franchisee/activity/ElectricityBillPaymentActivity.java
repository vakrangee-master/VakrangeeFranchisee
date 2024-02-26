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

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.util.List;
import java.util.StringTokenizer;

import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.adapter.ServiceProviderAdapter;
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

public class ElectricityBillPaymentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, ServiceProviderIfc {
    SoapObject response = null;
    String TAG = "Response";
    Spinner spnServiceProvider;
    Button btnSubmitRecharge, btnClear, btnCancle;
    EditText edtConsumerNo, edtBillingUnit, edtCycleNo;
    Toolbar toolbar;

    SoapPrimitive soapResultString;


    String spnSpineerVlaues;
    InternetConnection internetConnection;

    TelephonyManager telephonyManager;

    String diplayServerResopnse;
    ProgressDialog progress, progress1;
    String vkid;

    TextView getResponse;


    ServiceProvider mServiceProvider;

    TextView getBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fontawesome-webfont.ttf");
        setContentView(R.layout.activity_electricity_bill_payment);


        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        spnServiceProvider = (Spinner) findViewById(R.id.mserviceprovider);


        edtConsumerNo = (EditText) findViewById(R.id.edtConsumerNo);
        edtConsumerNo.setTypeface(Typeface.SANS_SERIF);

        getResponse = (TextView) findViewById(R.id.getResponse);
        getResponse.setTypeface(Typeface.SANS_SERIF);

        edtBillingUnit = (EditText) findViewById(R.id.edtBillingUnit);
        edtBillingUnit.setTypeface(Typeface.SANS_SERIF);

        edtCycleNo = (EditText) findViewById(R.id.edtCycleNo);
        edtCycleNo.setTypeface(Typeface.SANS_SERIF);


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


        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ElectricityBillPaymentActivity.this, MySerivcesActivity.class);
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
            getSupportActionBar().setTitle(R.string.ElectricityBillPayment);

//            final Drawable upArrow = getResources().getDrawable(R.drawable.back);
//            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//            getSupportActionBar().setHomeAsUpIndicator(upArrow);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        progress1 = new ProgressDialog(ElectricityBillPaymentActivity.this);
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


        loadSpinner();


        spnServiceProvider.setOnItemSelectedListener(this);

        internetConnection = new InternetConnection(ElectricityBillPaymentActivity.this);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spnServiceProvider.setSelection(0);
                edtConsumerNo.setText("");
                edtBillingUnit.setText("");
                edtBillingUnit.setError(null);
                edtConsumerNo.setError(null);
                getResponse.setText("");
                edtCycleNo.setText("");
                edtCycleNo.setError(null);
            }
        });


        btnSubmitRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int pos = spnServiceProvider.getSelectedItemPosition();
                final String cons = edtConsumerNo.getText().toString();
                final String billunit = edtBillingUnit.getText().toString();
                final String cycleno = edtCycleNo.getText().toString();


                if (cons.length() <= 9) {
                    edtConsumerNo.setError(getResources().getString(R.string.enter10DigitesNumber));

                } else if (billunit.length() <= 1) {
                    edtBillingUnit.setError(getResources().getString(R.string.mimnimum2DigitsNumber));
                } else if (cycleno.length() <= 1) {
                    edtCycleNo.setError(getResources().getString(R.string.mimnimum2DigitsNumber));
                } else if (pos == 0) {
                    // Toast.makeText(MobileRechargActivity.this, " Select OutdoorID Provider", Toast.LENGTH_SHORT).show();
                    TextView errorText = (TextView) spnServiceProvider.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText(R.string.selectServiceProvider);//changes the selected item text to this
                } else if (internetConnection.isConnectingToInternet() == false) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ElectricityBillPaymentActivity.this);
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
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ElectricityBillPaymentActivity.this);
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

                    //create builder
                    final AlertDialog.Builder builder = new AlertDialog.Builder(ElectricityBillPaymentActivity.this);
                    //inflate layout from xml. you must create an xml layout file in res/layout first
                    LayoutInflater inflater = ElectricityBillPaymentActivity.this.getLayoutInflater();
                    View layout = inflater.inflate(R.layout.popupmobile, null);
                    builder.setView(layout);

                    TextView pleaseVerfity = (TextView) layout.findViewById(R.id.pleaseVerfity);
                    pleaseVerfity.setTypeface(Typeface.SANS_SERIF);


                    TextView serviceproide = (TextView) layout.findViewById(R.id.amount);
                    serviceproide.setText("\u20B9 " + edtBillingUnit.getText().toString());

                    TextView mobilenumber = (TextView) layout.findViewById(R.id.mobilenumber);
                    mobilenumber.setText(edtConsumerNo.getText().toString());

                    TextView serviceprovidername = (TextView) layout.findViewById(R.id.serviceprovidername);
                    serviceprovidername.setText(mServiceProvider.getServiceDescription());

                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            progress = new ProgressDialog(ElectricityBillPaymentActivity.this);
                            progress.setTitle(R.string.transcationprogress);
                            progress.setMessage(getResources().getString(R.string.pleaseWait));
                            progress.setCancelable(false);
                            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            progress.show();
//
                            //  AsyncSubmitRecharge myRequest = new AsyncSubmitRecharge();
                            // myRequest.execute();
                            AsyncBillUnit myRequest = new AsyncBillUnit();
                            myRequest.execute();
                            // Toast.makeText(MobileRechargActivity.this, " Succesfull", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                        }
                    });

                    builder.setNegativeButton("Cancel  ", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builder.show();

                }
            }
        });

        edtConsumerNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i != 10) {
                    // Toast.makeText(getApplicationContext(), "Please Enter 10 digits", Toast.LENGTH_SHORT).show();
                    edtConsumerNo.setTextColor(Color.parseColor("#000000"));
                    edtConsumerNo.setError(getResources().getString(R.string.enter10DigitesNumber));

                }


            }

            @Override
            public void afterTextChanged(Editable editable) {


                if (edtConsumerNo.length() <= 9) {


                } else {
                    edtConsumerNo.setTextColor(Color.parseColor("#468847"));
                    edtConsumerNo.setError(null);

                }

                String tmp = editable.toString().trim();
                if (tmp.length() == 1 && tmp.equals("0"))
                    editable.clear();
            }

        });

        edtBillingUnit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i != 2) {
                    // Toast.makeText(getApplicationContext(), "Please Enter 10 digits", Toast.LENGTH_SHORT).show();
                    edtBillingUnit.setTextColor(Color.parseColor("#000000"));
                    edtBillingUnit.setError(getResources().getString(R.string.mimnimum2DigitsNumber));

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edtBillingUnit.length() <= 1) {


                } else {
                    edtBillingUnit.setTextColor(Color.parseColor("#468847"));
                    edtBillingUnit.setError(null);

                }

                String tmp = editable.toString().trim();
                if (tmp.length() == 1 && tmp.equals("0"))
                    editable.clear();
            }
        });


        edtCycleNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i != 2) {
                    // Toast.makeText(getApplicationContext(), "Please Enter 10 digits", Toast.LENGTH_SHORT).show();
                    edtCycleNo.setTextColor(Color.parseColor("#000000"));
                    edtCycleNo.setError(getResources().getString(R.string.mimnimum2DigitsNumber));

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edtCycleNo.length() <= 1) {


                } else {
                    edtCycleNo.setTextColor(Color.parseColor("#468847"));
                    edtCycleNo.setError(null);

                }

                String tmp = editable.toString().trim();
                if (tmp.length() == 1 && tmp.equals("0"))
                    editable.clear();
            }
        });


    }


    private void loadSpinner() {


        Connection connection = new Connection(this);
        //connection.openDatabase();
        ServiceProviderAdapter serviceProviderAdapter;
        List<ServiceProvider> serviceProviders = new ServiceProviderImpl().getServiceProvider(2, 84);
        serviceProviderAdapter = new ServiceProviderAdapter(ElectricityBillPaymentActivity.this,
                android.R.layout.simple_spinner_dropdown_item, serviceProviders);

        //serviceProviderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spnServiceProvider.setAdapter(serviceProviderAdapter);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(ElectricityBillPaymentActivity.this, MySerivcesActivity.class);
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

        mServiceProvider = (ServiceProvider) adapterView.getItemAtPosition(i);
        spnSpineerVlaues = mServiceProvider.getServiceDescription();


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
            AlertDialogBoxInfo.alertDialogShow(ElectricityBillPaymentActivity.this, getResources().getString(R.string.Warning));

        }

    }

    private class AsyncBillUnit extends AsyncTask<Void, Void, Void> implements ServiceProviderIfc {
        String consumer = edtConsumerNo.getText().toString();
        String billunit = edtBillingUnit.getText().toString();
        String cycle = edtCycleNo.getText().toString();

        //this is the method to query

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {

                Connection connection = new Connection(ElectricityBillPaymentActivity.this);
                String vkid = connection.getVkid();
                String tokenId = connection.getTokenId();

                int val = mServiceProvider.getServiceId();

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
                String mobileNumber = EncryptionUtil.encryptString(consumer, getApplicationContext());
                String amount = EncryptionUtil.encryptString(billunit, getApplicationContext());


                diplayServerResopnse = WebService.MobileRecharge(vkidd, TokenId, imei, deviceid, simserialnumber, ServiceProvider
                        , rechargeType, mobileNumber, amount);

                Log.d(TAG, "WebSer...ceResponse: " + diplayServerResopnse);

            } catch (Exception e) {
                String message = null;
                e.printStackTrace();
                Log.e(TAG + "Error", message);
                Log.i("TAG", ((message == null) ? "string null" : message));
                AlertDialogBoxInfo.alertDialogShow(ElectricityBillPaymentActivity.this, getResources().getString(R.string.Warning));
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
                    edtConsumerNo.setText("");
                    edtBillingUnit.setText("");
                    edtBillingUnit.setError(null);
                    edtConsumerNo.setError(null);
                    edtCycleNo.setText("");
                    edtCycleNo.setError(null);

                } else {
                    getResponse.setText(diplayServerResopnse);
                    getResponse.setTextColor(Color.RED);

                    spnServiceProvider.setSelection(0);
                    edtConsumerNo.setText("");
                    edtBillingUnit.setText("");
                    edtBillingUnit.setError(null);
                    edtConsumerNo.setError(null);

                    edtCycleNo.setText("");
                    edtCycleNo.setError(null);

                }

                in.vakrangee.franchisee.task.AsyncgetAvailableBalance task = new in.vakrangee.franchisee.task.AsyncgetAvailableBalance(getApplicationContext());
                task.delegate = this;
                task.execute();

                if (diplayServerResopnse == null) {

                    String message = null;
                    Log.i("TAG", ((message == null) ? "string null" : message));


                } else {
                    Log.e(TAG + "Error in Server", diplayServerResopnse);
                    // Toast.makeText(getApplicationContext(), "Error OTP ", Toast.LENGTH_SHORT).show();

                }
            } catch (Exception e) {
                e.printStackTrace();
                AlertDialogBoxInfo.alertDialogShow(ElectricityBillPaymentActivity.this, getResources().getString(R.string.Warning));
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
                AlertDialogBoxInfo.alertDialogShow(ElectricityBillPaymentActivity.this, getResources().getString(R.string.Warning));
            }


        }
    }


    public void onBackPressed() {

        Intent intent = new Intent(ElectricityBillPaymentActivity.this, MySerivcesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();


    }
}
