package in.vakrangee.franchisee.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;
import java.util.StringTokenizer;

import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.task.AsyncgetAvailableBalance;
import in.vakrangee.supercore.franchisee.ifc.ServiceProviderIfc;
import in.vakrangee.supercore.franchisee.model.ServiceProvider;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.webservice.WebService;

//

public class MahavitranActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, ServiceProviderIfc {
    String TAG = "Response";
    Button btnSubmitmaha, btnClear, btnCancle;
    Toolbar toolbar;
    ServiceProvider mserviceProvider;

    InternetConnection internetConnection;
    static Context context;
    String spineervlaues;
    TelephonyManager telephonyManager;
    EditText edtConsumerNumber;
    TextView getBalance;
    TextView getResponse;
    ProgressDialog progress1;
    SharedPreferences sharedPref;
    String diplayServerResopnse;
    ProgressDialog progress;
    AutoCompleteTextView autoCompleteTextView;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fontawesome-webfont.ttf");
        setContentView(R.layout.activity_mahavitran);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.mahavitran);
//
//            final Drawable upArrow = getResources().getDrawable(R.drawable.back);
//            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//            getSupportActionBar().setHomeAsUpIndicator(upArrow);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autocompleted);


        internetConnection = new InternetConnection(MahavitranActivity.this);
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        edtConsumerNumber = (EditText) findViewById(R.id.MconsumerNumber);
        edtConsumerNumber.setTypeface(Typeface.SANS_SERIF);

        getBalance = (TextView) findViewById(R.id.getBalance);
        getBalance.setTypeface(Typeface.SANS_SERIF);

        btnSubmitmaha = (Button) findViewById(R.id.mahaSubmit);
        btnSubmitmaha.setTypeface(font);
        btnSubmitmaha.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " + getResources().getString(R.string.submit)));


        btnClear = (Button) findViewById(R.id.mahaClear);
        btnClear.setTypeface(font);
        btnClear.setText(new SpannableStringBuilder(new String(new char[]{0xf021}) + " " + getResources().getString(R.string.clear)));

        getResponse = (TextView) findViewById(R.id.txtgetResponseMahavitran);
        getResponse.setTypeface(Typeface.SANS_SERIF);


        btnCancle = (Button) findViewById(R.id.mahaCancel);
        btnCancle.setTypeface(font);
        btnCancle.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + "  " + getResources().getString(R.string.cancel)));


        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MahavitranActivity.this, MySerivcesActivity.class);
                startActivity(i);
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoCompleteTextView.setText("");
                autoCompleteTextView.setError(null);

                edtConsumerNumber.setText("");
                edtConsumerNumber.setError(null);

            }
        });


        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(edtConsumerNumber.getWindowToken(), 0);


        edtConsumerNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // hide virtual keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edtConsumerNumber.getWindowToken(),
                            InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    return true;
                }
                return false;
            }
        });

//        String[] countries = Connection.getAllMahavitranName();
//
//        for (int i = 0; i < countries.length; i++) {
//            Log.i(this.toString(), countries[i]);
//        }
//
//
//        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, countries);
//        autoCompleteTextView.setAdapter(adapter);
//        autoCompleteTextView.setThreshold(1);
//        autoCompleteTextView.setOnItemSelectedListener(MahavitranActivity.this);

        loadSpinner();

        if (internetConnection.isNetworkAvailable(getApplicationContext()) == false) {

            AlertDialogBoxInfo.alertDialogShow(MahavitranActivity.this, getResources().getString(R.string.internetCheck));

        } else {
            progress1 = new ProgressDialog(MahavitranActivity.this);
            progress1.setTitle(R.string.fetchingBalance);
            progress1.setMessage(getResources().getString(R.string.pleaseWait));
            progress1.setCancelable(false);
            progress1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress1.show();
            AsyncgetAvailableBalance task = new AsyncgetAvailableBalance(getApplicationContext());
            task.delegate = this;
            task.execute();
        }


        btnSubmitmaha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String pos = autoCompleteTextView.getText().toString();
                final String mob = edtConsumerNumber.getText().toString();
                if (pos.length() == 0) {

                    autoCompleteTextView.setError("");

                    autoCompleteTextView.setError("Type OutdoorID Provider Name");//changes the selected item text to this
                } else if (mob.length() <= 11) {

                    edtConsumerNumber.setError(getResources().getString(R.string.enter12DigitsConsumerNumber));


                } else if (internetConnection.isConnectingToInternet() == false) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MahavitranActivity.this);
                    alertDialogBuilder
                            .setMessage(R.string.internetCheck)
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dialog.dismiss();
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                } else if (telephonyManager.getSimState() == TelephonyManager.SIM_STATE_ABSENT) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MahavitranActivity.this);
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
                } else {

                    //int userChoice = spnMahavitran.getSelectedItemPosition();


                    /*StringTokenizer tokens = new StringTokenizer(autoCompleteTextView.getText().toString());
                    String first = tokens.nextToken();
                    Connection connection = new Connection(MahavitranActivity.this);
                    connection.openDatabase();
                    connection.InsertSpineerBillUnitNo(Integer.parseInt(first));
*/
//
                    AsyncGetBillDetails myRequest = new AsyncGetBillDetails();
                    //UserAlreadyRegister myRequest = new UserAlreadyRegister();
                    myRequest.execute();


                    progress = new ProgressDialog(MahavitranActivity.this);
                    progress.setTitle("Fetching Bill...");
                    progress.setMessage(getResources().getString(R.string.pleaseWait));
                    progress.setCancelable(false);
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.show();

                    // Intent intent = new Intent(MahavitranActivity.this, MahavitranBillInfoActivity.class);
                    //startActivity(intent);
                    // Toast.makeText(MahavitranActivity.this, " Select-" + val, Toast.LENGTH_LONG).show();
                }
            }
        });


        autoCompleteTextView.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on Enter key press
                    autoCompleteTextView.clearFocus();
                    edtConsumerNumber.requestFocus();
                    return true;
                }
                return false;
            }
        });


        edtConsumerNumber.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_UP) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    edtConsumerNumber.requestFocus();
                    // Perform action on Enter key press
                    // check for username - password correctness here
                    return true;
                }
                return false;
            }
        });


        edtConsumerNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i != 12) {
                    // Toast.makeText(getApplicationContext(), "Please Enter 10 digits", Toast.LENGTH_SHORT).show();
                    edtConsumerNumber.setTextColor(Color.parseColor("#000000"));
                    edtConsumerNumber.setError(getResources().getString(R.string.enter12DigitsConsumerNumber));

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {


                if (edtConsumerNumber.length() <= 11) {


                } else {
                    edtConsumerNumber.setTextColor(Color.parseColor("#468847"));
                    edtConsumerNumber.setError(null);

                }

            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void loadSpinner() {


        Connection connection = new Connection(this);
        //connection.openDatabase();
       /* ServiceProviderAdapter serviceProviderAdapter;
        List<ServiceProvider> serviceProviderList = new ServiceProviderImpl().getServiceMahavitran();
        serviceProviderAdapter = new ServiceProviderAdapter(MahavitranActivity.this,
                android.R.layout.simple_spinner_dropdown_item, serviceProviderList);

        serviceProviderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
*/

        String[] countries = connection.getAllMahavitranName();

        for (int i = 0; i < countries.length; i++) {
            //Log.i(this.toString(), countries[i]);
        }


        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, countries);

        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setOnItemSelectedListener(MahavitranActivity.this);


//      SharedPreferences sharedPref = getSharedPreferences("FileName",MODE_PRIVATE);
//        int spinnerValue = sharedPref.getInt("userChoiceSpinner",-1);
//        if(spinnerValue != -1)
//            // set the value of the spinner
//            spnMahavitran.setSelection(spinnerValue);
//

       /* int spinnerValue = connection.getSelectedValues();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerValue);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMahavitran.setAdapter(serviceProviderAdapter);
        spnMahavitran.setSelection(spinnerValue);*/
    }

    public void onBackPressed() {

        Intent intent = new Intent(MahavitranActivity.this, MySerivcesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(MahavitranActivity.this, MySerivcesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

                break;
        }
        return true;
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
            e.printStackTrace();
            AlertDialogBoxInfo.alertDialogShow(MahavitranActivity.this, getResources().getString(R.string.Warning));
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Mahavitran Page", // TODO: Define a title for the contentOpen shown.
//                // TODO: If you have web page contentOpen that matches this app activity's contentOpen,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://in.vakrangee.vkms.activity/http/host/path")
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
//                "Mahavitran Page", // TODO: Define a title for the contentOpen shown.
//                // TODO: If you have web page contentOpen that matches this app activity's contentOpen,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://in.vakrangee.vkms.activity/http/host/path")
//        );
        //   AppIndex.AppIndexApi.end(client, viewAction);
        // client.disconnect();
    }

    private class AsyncGetBillDetails extends AsyncTask<Void, Void, Void> {


        Connection connection = new Connection(MahavitranActivity.this);
        String getVkid = connection.getVkid();
        String vkid = getVkid;
        String tokenId = connection.getTokenId();
        String imeiDevice = CommonUtils.getAndroidUniqueID(getApplicationContext());


        String vkidreg = EncryptionUtil.encryptString(vkid, getApplicationContext());
        String tokenid = EncryptionUtil.encryptString(tokenId, getApplicationContext());
        String imei = EncryptionUtil.encryptString(imeiDevice, getApplicationContext());

        final String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        String deviceid = EncryptionUtil.encryptString(deviceId, getApplicationContext());

        String simserial = CommonUtils.getSimSerialNumber(getApplicationContext());
        String simserialnumber = EncryptionUtil.encryptString(simserial, getApplicationContext());

        StringTokenizer tokens = new StringTokenizer(autoCompleteTextView.getText().toString());
        String first = tokens.nextToken();


        String val = first;
        String BillUnitNo = EncryptionUtil.encryptString(val, getApplicationContext());

        String consumerNo = edtConsumerNumber.getText().toString();
        String consumerNumber = EncryptionUtil.encryptString(consumerNo, getApplicationContext());

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {


                diplayServerResopnse = WebService.getBillDetails(vkidreg, tokenid, imei, deviceid, simserialnumber, BillUnitNo, consumerNumber);

                Log.d(TAG, "WebSer...ceResponse: " + diplayServerResopnse);

            } catch (Exception e) {

                String message = null;
                Log.i("TAG", ((message == null) ? "string null" : message));
                AlertDialogBoxInfo.alertDialogShow(MahavitranActivity.this, getResources().getString(R.string.Warning));

            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");


            progress.dismiss();
//            edtConsumerNumber.setText("");
//            getResponse.setText(diplayServerResopnse);
//            getResponse.setTextColor(Color.RED);


            try {
                StringTokenizer tokens = new StringTokenizer(diplayServerResopnse, "|");
                String first = tokens.nextToken();


                if (diplayServerResopnse == null) {

                    String message = null;
                    Log.i("TAG", ((message == null) ? "string null" : message));


                } else if (first.equals("OKAY")) {
                    // Toast.makeText(getApplicationContext(), "OTP  successful", Toast.LENGTH_SHORT).show();


                    String second = tokens.nextToken();

                    Intent i = new Intent(MahavitranActivity.this, MahavitranBillInfoActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra("second", second);
                    startActivity(i);
                    finish();

                    Log.e(TAG + "MahavitranBillInfoActivity", diplayServerResopnse);


                } else if (diplayServerResopnse.equals("No Bill found for this billing unit and consumer number. Kindly check the billing unit and consumer number.")) {
                    edtConsumerNumber.setText("");
                    getResponse.setText(diplayServerResopnse);
                    getResponse.setTextColor(Color.RED);


                } else if (diplayServerResopnse.equals("Error occured.")) {
                    AlertDialogBoxInfo.alertDialogShow(MahavitranActivity.this, getResources().getString(R.string.unableToConnectVkms));
                } else {
                    Log.e(TAG + "Error in Server", diplayServerResopnse);
                    edtConsumerNumber.setText("");
                    getResponse.setText(diplayServerResopnse);
                    getResponse.setTextColor(Color.RED);
                    //  Toast.makeText(getApplicationContext(), "Error OTP ", Toast.LENGTH_SHORT).show();

                }

            } catch (Exception e) {
                e.printStackTrace();
                AlertDialogBoxInfo.alertDialogShow(MahavitranActivity.this, getResources().getString(R.string.Warning));
            }
        }

    }

}
