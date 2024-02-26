package in.vakrangee.franchisee.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.MenuItem;
import android.view.View;
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
import in.vakrangee.franchisee.adapter.AdapterRecyclerViewMyTraction;
import in.vakrangee.supercore.franchisee.model.ServiceProvider;
import in.vakrangee.supercore.franchisee.model.WalletData;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.webservice.WebService;

//

public class TransactionReportActivity extends AppCompatActivity {


    Toolbar toolbar;

    EditText edtFromDate, edtToDate;
    Spinner spnMyTraction;
    ProgressDialog progress;
    Button btnAccountStmt;
    ServiceProvider mserviceProvider;
    String TAG = "Response";
    String spineervlaues;
    TelephonyManager telephonyManager;
    String diplayServerResopnse;
    TextView output;
    private RecyclerView recyclerView;
    private AdapterRecyclerViewMyTraction mAdapter;
    private TextView emptyView;
    Context context;
    InternetConnection internetConnection;
    private Activity mActivity;
    ServiceProvider mServiceProvider;
    String spnSpineerVlaues;
    EditText edtrVKID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        setContentView(R.layout.activity_transaction_report);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.WalletRechargeHistory);

//            final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
//            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//            getSupportActionBar().setHomeAsUpIndicator(upArrow);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        btnAccountStmt = (Button) findViewById(R.id.btnMyTraction);
        btnAccountStmt.setTypeface(font);

        btnAccountStmt.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " + getResources().getString(R.string.go)));
        emptyView = (TextView) findViewById(R.id.empty_view_mytraction);
        edtFromDate = (EditText) findViewById(R.id.fromdateMytraction);
        edtToDate = (EditText) findViewById(R.id.todateMyTracton);

        edtrVKID = (EditText) findViewById(R.id.edtVkid);
        edtrVKID.setTypeface(Typeface.SANS_SERIF);


        //  telephonyManager = (TelephonyManager)view.getSystemService(Context.TELEPHONY_SERVICE);

        internetConnection = new InternetConnection(getApplicationContext());
        btnAccountStmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "button click", Toast.LENGTH_LONG).show();
            }
        });
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat dfDate = new SimpleDateFormat(myFormat, Locale.US);

        Date nowDate = new Date(System.currentTimeMillis());
        String from = edtFromDate.getText().toString();
        String to = edtToDate.getText().toString();

        String datetime = dfDate.format(nowDate);
        edtFromDate.setText(datetime);
        edtToDate.setText(datetime);
        edtFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        TransactionReportActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mcurrentDate.set(Calendar.YEAR, year);
                        mcurrentDate.set(Calendar.MONTH, monthOfYear);
                        mcurrentDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        String myFormat = "dd-MM-yyyy";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        edtFromDate.setText(sdf.format(mcurrentDate.getTime()));

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

                final Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        TransactionReportActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mcurrentDate.set(Calendar.YEAR, year);
                        mcurrentDate.set(Calendar.MONTH, monthOfYear);
                        mcurrentDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        String myFormat = "dd-MM-yyyy";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        edtToDate.setText(sdf.format(mcurrentDate.getTime()));

                    }
                }, mYear, mMonth, mDay);


                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();


            }
        });


        btnAccountStmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat dfDate = new SimpleDateFormat(myFormat, Locale.US);

                Date nowDate = new Date(System.currentTimeMillis());
                String from = edtFromDate.getText().toString();
                String to = edtToDate.getText().toString();
                String datetime = dfDate.format(nowDate);
                String vkid = edtrVKID.getText().toString().trim().toUpperCase();

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
                } else if (vkid.length() != 9) {
                    edtrVKID.setError(getResources().getString(R.string.enter9DigitsVKID));

                } else if (internetConnection.isConnectingToInternet() == false) {

                    AlertDialogBoxInfo.alertDialogShow(getApplicationContext(), getResources().getString(R.string.internetCheck));


                } else {

                    //AsyncSubmitDTH myRequest = new AsyncSubmitDTH();
                    //myRequest.execute();
                    edtFromDate.setError(null);
                    edtToDate.setError(null);


                    progress = new ProgressDialog(TransactionReportActivity.this);
                    progress.setTitle(R.string.accountStmt);
                    progress.setMessage(getResources().getString(R.string.pleaseWait));
                    progress.setCancelable(false);
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.show();


                    AsyncGetMyTransactions myRequest = new AsyncGetMyTransactions();
                    myRequest.execute();
                    //Toast.makeText(DTHRechargeActivity.this, " Succesfull", Toast.LENGTH_SHORT).show();

                }


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

    }

    public void onBackPressed() {

        Intent intent = new Intent(TransactionReportActivity.this, TechnicalSupportActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(TransactionReportActivity.this, TechnicalSupportActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();


                break;
        }
        return true;
    }

    private class AsyncGetMyTransactions extends AsyncTask<Void, Void, Void> {
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

                // int val = mServiceProvider.getServiceId();
//


                String vkidd = EncryptionUtil.encryptString(vkid, getApplicationContext());
                String TokenId = EncryptionUtil.encryptString(tokenId, getApplicationContext());
                String deviceIdget = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                String deviceid = EncryptionUtil.encryptString(deviceIdget, getApplicationContext());

                String deviceIDAndroid = CommonUtils.getAndroidUniqueID(getApplicationContext());
                String imei = EncryptionUtil.encryptString(deviceIDAndroid, getApplicationContext());

                String simSerial = CommonUtils.getSimSerialNumber(getApplicationContext());
                String simserialnumber = EncryptionUtil.encryptString(simSerial, getApplicationContext());

                String fromDate = EncryptionUtil.encryptString(fromdate, getApplicationContext());
                String toDate = EncryptionUtil.encryptString(todate, getApplicationContext());
                String ServiceProvider = EncryptionUtil.encryptString("11", getApplicationContext());


                diplayServerResopnse = WebService.getMyTransactions(vkidd, TokenId, imei, deviceid, simserialnumber, fromDate, toDate, ServiceProvider);

                Log.d(TAG, "WebSer...ceResponse: " + diplayServerResopnse);

            } catch (Exception e) {
                e.printStackTrace();
                String message = null;
                Log.e(TAG + "Error", message);
                Log.i("TAG", ((message == null) ? "string null" : message));
                AlertDialogBoxInfo.alertDialogShow(TransactionReportActivity.this, getResources().getString(R.string.Warning));
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
                        constants.SUCCESS = jsonobject.getString("myModelObject8");
                        String myModelObject9 = jsonobject.getString("myModelObject9");
                        String myModelObject10 = jsonobject.getString("myModelObject10");

                        String myModelObject11 = jsonobject.getString("myModelObject11");
                        String myModelObject12 = jsonobject.getString("myModelObject12");
                        String myModelObject13 = jsonobject.getString("myModelObject13");
                        String myModelObject14 = jsonobject.getString("myModelObject14");
                        String myModelObject15 = jsonobject.getString("myModelObject15");
                        String myModelObject16 = jsonobject.getString("myModelObject16");
                        String myModelObject17 = jsonobject.getString("myModelObject17");
                        String myModelObject18 = jsonobject.getString("myModelObject18");
                        String myModelObject19 = jsonobject.getString("myModelObject19");
                        String myModelObject20 = jsonobject.getString("myModelObject20");

                        String myModelObject21 = jsonobject.getString("myModelObject21");


                        data.add(constants);


                    }

                    // Setup and Handover data to recyclerview
                    recyclerView = (RecyclerView) findViewById(R.id.recycler_view_mytraction);
                    mAdapter = new AdapterRecyclerViewMyTraction(getApplicationContext(), data);
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
                    AlertDialogBoxInfo.alertDialogShow(TransactionReportActivity.this, getResources().getString(R.string.Warning));
                }


            } catch (Exception e) {
                e.printStackTrace();
                AlertDialogBoxInfo.alertDialogShow(TransactionReportActivity.this, getResources().getString(R.string.Warning));
            }

        }


    }

}
