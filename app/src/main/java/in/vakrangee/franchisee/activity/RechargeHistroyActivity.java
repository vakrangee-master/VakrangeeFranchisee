package in.vakrangee.franchisee.activity;

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
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.adapter.AdapterRecyclerViewMyRechargeHistory;
import in.vakrangee.supercore.franchisee.model.WalletData;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.webservice.WebService;

//

public class RechargeHistroyActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    Toolbar toolbar;
    EditText edtFromDate, edtToDate;
    Button btnRechargeHistory;
    ProgressDialog progress;
    private TextView emptyView;
    String TAG = "Response";
    TelephonyManager telephonyManager;
    private RecyclerView recyclerView;

    private AdapterRecyclerViewMyRechargeHistory mAdapter;
    String diplayServerResopnse;
    DatePickerDialog.OnDateSetListener datefromdate;
    InternetConnection internetConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fontawesome-webfont.ttf");

        setContentView(R.layout.activity_recharge_histroy);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.myRechargeHistroy);

//            final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
//            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//            getSupportActionBar().setHomeAsUpIndicator(upArrow);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);


        btnRechargeHistory = (Button) findViewById(R.id.rechargeHsitorySubmit);
        btnRechargeHistory.setTypeface(font);
        btnRechargeHistory.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " + getResources().getString(R.string.go)));
        emptyView = (TextView) findViewById(R.id.empty_view);


        edtFromDate = (EditText) findViewById(R.id.rechargeFromDate);


        edtToDate = (EditText) findViewById(R.id.rechargeToDate);

        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat dfDate = new SimpleDateFormat(myFormat, Locale.US);
        Date nowDate = new Date(System.currentTimeMillis());
        String datetime = dfDate.format(nowDate);
        edtFromDate.setText(datetime);
        edtToDate.setText(datetime);

        internetConnection = new InternetConnection(RechargeHistroyActivity.this);


        edtFromDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                final Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        RechargeHistroyActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK, new DatePickerDialog.OnDateSetListener() {

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

//                new DatePickerDialog(RechargeHistroyActivity.this, datefromdate, myCalendarFromDate
//                        .get(Calendar.YEAR), myCalendarFromDate.get(Calendar.MONTH),
//                        myCalendarFromDate.get(Calendar.DAY_OF_MONTH)).show();
//
//
//            }
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
                        RechargeHistroyActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK, new DatePickerDialog.OnDateSetListener() {

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


        btnRechargeHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat dfDate = new SimpleDateFormat(myFormat, Locale.US);

                Date nowDate = new Date(System.currentTimeMillis());

                String from = edtFromDate.getText().toString();
                String to = edtToDate.getText().toString();
                String datetime = dfDate.format(nowDate);
                boolean f = isDateAfter(from, datetime);
                boolean t = isDateAfter(to, datetime);
                boolean both = isDateAfter(to, from);


                if (from.length() == 0) {
                    edtFromDate.setError(getResources().getString(R.string.enterfromdate));
                } else if (f == false) {
                    Toast.makeText(RechargeHistroyActivity.this, from + " is greater  than  " + datetime, Toast.LENGTH_SHORT).show();
                    edtFromDate.setError(getResources().getString(R.string.selectproperdate));
                } else if (to.length() == 0) {

                    edtToDate.setError(getResources().getString(R.string.entertodate));

                } else if (t == false) {
                    edtToDate.setError(getResources().getString(R.string.selectproperdate));
                    Toast.makeText(RechargeHistroyActivity.this, to + "  is greater than Current OutdoorStartDate " + datetime, Toast.LENGTH_SHORT).show();
                } else if (from == to) {
                    edtToDate.setError(getResources().getString(R.string.selectproperdate));
                    Toast.makeText(RechargeHistroyActivity.this, to + "  is greater than Current OutdoorStartDate  " + datetime, Toast.LENGTH_SHORT).show();
                } else if (both == false) {
                    edtFromDate.setError(getResources().getString(R.string.selectproperdate));

                    edtToDate.setError(getResources().getString(R.string.selectproperdate));
                    Toast.makeText(RechargeHistroyActivity.this, " End date cannot be less than Start OutdoorStartDate.  ", Toast.LENGTH_SHORT).show();
                } else if (internetConnection.isConnectingToInternet() == false) {

                    AlertDialogBoxInfo.alertDialogShow(RechargeHistroyActivity.this, getResources().getString(R.string.internetCheck));


                } else {
                    progress = new ProgressDialog(RechargeHistroyActivity.this);
                    progress.setTitle(R.string.accountStmt);
                    progress.setMessage(getResources().getString(R.string.pleaseWait));
                    progress.setCancelable(false);
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.show();
                    //AsyncSubmitDTH myRequest = new AsyncSubmitDTH();
                    //myRequest.execute();
                    edtFromDate.setError(null);
                    edtToDate.setError(null);


                    AsyncGetRechargeHistroy myRequest = new AsyncGetRechargeHistroy();
                    myRequest.execute();
                    //Toast.makeText(DTHRechargeActivity.this, " Succesfull", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }


    public static boolean isDateAfter(String startDate, String endDate) {
        {
            try {


                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat df = new SimpleDateFormat(myFormat, Locale.US);


                Date startingDate = df.parse(startDate);
                Date edate1 = df.parse(endDate);

                if (edate1.after(startingDate)) {

                    return true;
                } else if (edate1.equals(startingDate)) {
                    return true;
                } else if (startingDate.after(edate1)) {
                    return false;
                } else
                    return false;
            } catch (Exception e) {

                return false;
            }
        }
    }


    public void onBackPressed() {

        Intent intent = new Intent(RechargeHistroyActivity.this, MyStatementActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(RechargeHistroyActivity.this, MyStatementActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

                // Toast.makeText(getApplicationContext(),"Back button clicked", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }


    private class AsyncGetRechargeHistroy extends AsyncTask<Void, Void, Void> {
        String todatea = edtToDate.getText().toString();
        String fromdate = edtFromDate.getText().toString();

        //this is the method to query

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {

                Connection connection = new Connection(RechargeHistroyActivity.this);
                String vkid = connection.getVkid();
                String tokenId = connection.getTokenId();
                String vkidd = EncryptionUtil.encryptString(vkid, getApplicationContext());
                String TokenId = EncryptionUtil.encryptString(tokenId, getApplicationContext());
                String deviceIdget = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                String deviceid = EncryptionUtil.encryptString(deviceIdget, getApplicationContext());

                String deviceIDAndroid = CommonUtils.getAndroidUniqueID(getApplicationContext());
                String imei = EncryptionUtil.encryptString(deviceIDAndroid, getApplicationContext());

                String simSerial = CommonUtils.getSimSerialNumber(getApplicationContext());
                String simserialnumber = EncryptionUtil.encryptString(simSerial, getApplicationContext());

                String fromDate = EncryptionUtil.encryptString(todatea, getApplicationContext());
                String toDate = EncryptionUtil.encryptString(fromdate, getApplicationContext());


                diplayServerResopnse = WebService.getRechargeHistroyReport(vkidd, TokenId, imei, deviceid, simserialnumber, fromDate, toDate);

                Log.d(TAG, "WebSer...ceResponse: " + diplayServerResopnse);

            } catch (Exception e) {
                AlertDialogBoxInfo.alertDialogShow(RechargeHistroyActivity.this, getResources().getString(R.string.Warning));
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
                    recyclerView = (RecyclerView) findViewById(R.id.recycler_view_rechagre_history);
                    mAdapter = new AdapterRecyclerViewMyRechargeHistory(RechargeHistroyActivity.this, data);
                    recyclerView.setAdapter(mAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(RechargeHistroyActivity.this));

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
                    AlertDialogBoxInfo.alertDialogShow(RechargeHistroyActivity.this, getResources().getString(R.string.Warning));
                }


            } catch (Exception e) {
                e.printStackTrace();
                AlertDialogBoxInfo.alertDialogShow(RechargeHistroyActivity.this, getResources().getString(R.string.Warning));
            }

        }


    }

}
