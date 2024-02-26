package in.vakrangee.franchisee.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.adapter.AdapterRecyclerViewWalletStmt;
import in.vakrangee.franchisee.adapter.ServiceProviderAdapter;
import in.vakrangee.supercore.franchisee.impl.ServiceProviderImpl;
import in.vakrangee.supercore.franchisee.model.ServiceProvider;
import in.vakrangee.supercore.franchisee.model.WalletData;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.webservice.WebService;

//

public class AccountStatementActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    EditText edtFromDate, edtToDate;
    Spinner spnAccountStatement;
    ProgressDialog progress;
    Button btnAccountStmt;
    ServiceProvider mserviceProvider;
    String TAG = "Response";
    String spineervlaues;
    TelephonyManager telephonyManager;
    String diplayServerResopnse;
    TextView output;
    private RecyclerView recyclerView;
    private AdapterRecyclerViewWalletStmt mAdapter;
    private TextView emptyView;
    Context context;
    InternetConnection internetConnection;
    Toolbar toolbar;
    Calendar calendar;
    Date date;
    String fromdate, todate;
    Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        setContentView(R.layout.activity_account_statement2);

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        btnAccountStmt = (Button) findViewById(R.id.btnMyTraction);
        btnAccountStmt.setTypeface(font);

        btnAccountStmt.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " + getResources().getString(R.string.go)));
        emptyView = (TextView) findViewById(R.id.empty_view_mytraction);
        edtFromDate = (EditText) findViewById(R.id.fromdateMytraction);
        edtToDate = (EditText) findViewById(R.id.todateMyTracton);
        spnAccountStatement = (Spinner) findViewById(R.id.AccountStatement);
        internetConnection = new InternetConnection(getApplicationContext());
        btnAccountStmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "button click", Toast.LENGTH_LONG).show();
            }
        });

        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat dfDate = new SimpleDateFormat(myFormat, Locale.US);

        date = new Date(System.currentTimeMillis());
        String datetime = dfDate.format(date);
        edtFromDate.setText(datetime);
        edtToDate.setText(datetime);


        fromdate = edtFromDate.getText().toString();
        todate = edtToDate.getText().toString();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.WalletACStatement);

            final Drawable upArrow = getResources().getDrawable(R.drawable.back);
            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);

        }

        edtFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AccountStatementActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        String myFormat = "dd-MM-yyyy";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        edtFromDate.setText(sdf.format(calendar.getTime()));

                    }
                }, mYear, mMonth, mDay);


                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();

            }
        });
        edtToDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                calendar = Calendar.getInstance();
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AccountStatementActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        String myFormat = "dd-MM-yyyy";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        edtToDate.setText(sdf.format(calendar.getTime()));

                    }
                }, mYear, mMonth, mDay);


                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();


            }
        });
        spnAccountStatement.setOnItemSelectedListener(this);
        loadSpinner();
        btnAccountStmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat dfDate = new SimpleDateFormat(myFormat, Locale.US);

                date = new Date(System.currentTimeMillis());
                String from = edtFromDate.getText().toString();
                String to = edtToDate.getText().toString();
                String datetime = dfDate.format(date);


                boolean f = MyTransactionActivity.isDateAfter(from, datetime);
                boolean t = MyTransactionActivity.isDateAfter(to, datetime);
                boolean both = MyTransactionActivity.isDateAfter(from, to);

                if (edtFromDate.length() == 0) {
                    edtFromDate.setError(getResources().getString(R.string.enterfromdate));
                } else if (f == false) {
                    Toast.makeText(getApplicationContext(), from + " is greater than Current OutdoorStartDate   " + datetime, Toast.LENGTH_SHORT).show();
                    edtFromDate.setError(getResources().getString(R.string.selectproperdate));
                } else if (edtToDate.length() == 0) {
                    edtToDate.setError(getResources().getString(R.string.entertodate));
                } else if (t == false) {
                    edtToDate.setError(getResources().getString(R.string.selectproperdate));
                    Toast.makeText(getApplicationContext(), to + " is greater than Current OutdoorStartDate " + datetime, Toast.LENGTH_SHORT).show();
                } else if (both == false) {
                    edtToDate.setError(getResources().getString(R.string.selectproperdate));
                    edtFromDate.setError(getResources().getString(R.string.selectproperdate));
                    Toast.makeText(getApplicationContext(), "  End date cannot be less than Start OutdoorStartDate.  ", Toast.LENGTH_SHORT).show();
                } else if (internetConnection.isConnectingToInternet() == false) {

                    AlertDialogBoxInfo.alertDialogShow(getApplicationContext(), getResources().getString(R.string.internetCheck));


                } else {

                    //AsyncSubmitDTH myRequest = new AsyncSubmitDTH();
                    //myRequest.execute();
                    edtFromDate.setError(null);
                    edtToDate.setError(null);


                    progress = new ProgressDialog(AccountStatementActivity.this);
                    progress.setTitle(R.string.accountStmt);
                    progress.setMessage(getResources().getString(R.string.pleaseWait));
                    progress.setCancelable(false);
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.show();


                    AsyncGetAccountStatement myRequest = new AsyncGetAccountStatement();
                    myRequest.execute();
                    //Toast.makeText(DTHRechargeActivity.this, " Succesfull", Toast.LENGTH_SHORT).show();

                }


            }
        });


    }

    private void loadSpinner() {
        connection = new Connection(AccountStatementActivity.this);
        //connection.openDatabase();
        ServiceProviderAdapter serviceProviderAdapter;
        List<ServiceProvider> serviceProvider = new ServiceProviderImpl().getAccountStatement();
        serviceProviderAdapter = new ServiceProviderAdapter(AccountStatementActivity.this,
                android.R.layout.simple_spinner_dropdown_item, serviceProvider);


        serviceProviderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnAccountStatement.setAdapter(serviceProviderAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        mserviceProvider = (ServiceProvider) adapterView.getItemAtPosition(i);
        spineervlaues = mserviceProvider.getServiceDescription();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private class AsyncGetAccountStatement extends AsyncTask<Void, Void, Void> {
        String fromdate = edtFromDate.getText().toString();
        String todate = edtToDate.getText().toString();

        //this is the method to query

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {

                Connection connection = new Connection(getApplicationContext());
                String vkid = connection.getVkid();
                String tokenId = connection.getTokenId();
                int val = mserviceProvider.getServiceId();
                // int val = mServiceProvider.getServiceId();
//


                String vkidd = EncryptionUtil.encryptString(vkid, getApplicationContext());
                String TokenId = EncryptionUtil.encryptString(tokenId, getApplicationContext());

                final String deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                String deviceid = EncryptionUtil.encryptString(deviceId, getApplicationContext());

                String deviceIDAndroid = CommonUtils.getAndroidUniqueID(getApplicationContext());
                String imei = EncryptionUtil.encryptString(deviceIDAndroid, getApplicationContext());

                String simSerial = CommonUtils.getSimSerialNumber(getApplicationContext());
                String simserialnumber = EncryptionUtil.encryptString(simSerial, getApplicationContext());

                String fromDate = EncryptionUtil.encryptString(fromdate, getApplicationContext());
                String toDate = EncryptionUtil.encryptString(todate, getApplicationContext());
                String ServiceProvider = EncryptionUtil.encryptString(String.valueOf(val), getApplicationContext());


                diplayServerResopnse = WebService.getAccountStatement(vkidd, TokenId, imei, deviceid, simserialnumber, fromDate, toDate, ServiceProvider);

                Log.d(TAG, "WebSer...ceResponse: " + diplayServerResopnse);

            } catch (Exception e) {
                e.printStackTrace();
                String message = null;
                Log.e(TAG + "Error", message);
                Log.i("TAG", ((message == null) ? "string null" : message));
                AlertDialogBoxInfo.alertDialogShow(getApplicationContext(), getResources().getString(R.string.Warning));

            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            progress.dismiss();

            // progress.dismiss();
            try {

                String strJson = diplayServerResopnse;
                String myModelObject1 = "";
                String myModelObject2 = "";
                List<WalletData> data = new ArrayList<>();
                try {


                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonarray = new JSONArray(strJson);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);

                        WalletData constants = new WalletData();
                        constants.DATE = jsonobject.getString("myModelObject1");
                        constants.SERVICES = jsonobject.getString("myModelObject2");
                        constants.PARTICULAR = jsonobject.getString("myModelObject3");
                        constants.TRANACTION = jsonobject.getString("myModelObject4");
                        constants.BALANCECR = jsonobject.getString("myModelObject5");
                        constants.BALANCE = jsonobject.getString("myModelObject6");

                        String myModelObject7 = jsonobject.getString("myModelObject7");
                        String myModelObject8 = jsonobject.getString("myModelObject8");
                        String myModelObject9 = jsonobject.getString("myModelObject9");
                        String myModelObject10 = jsonobject.getString("myModelObject10");
                        data.add(constants);


                    }

                    // Setup and Handover data to recyclerview
                    recyclerView = (RecyclerView) findViewById(R.id.recycler_view_mytraction);
                    mAdapter = new AdapterRecyclerViewWalletStmt(getApplicationContext(), data);
                    recyclerView.setAdapter(mAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    if (data.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        emptyView.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Error Json", e.getMessage());
                    AlertDialogBoxInfo.alertDialogShow(getApplicationContext(), getResources().getString(R.string.Warning));

                }


            } catch (Exception e) {
                e.printStackTrace();
                AlertDialogBoxInfo.alertDialogShow(getApplicationContext(), getResources().getString(R.string.Warning));

            }

        }


    }

    public void onBackPressed() {

        Intent intent = new Intent(AccountStatementActivity.this, TechnicalSupportActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(AccountStatementActivity.this, TechnicalSupportActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();


                break;
        }
        return true;
    }

}
