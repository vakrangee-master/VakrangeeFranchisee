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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.StringTokenizer;

import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.adapter.ODApplyAdpaterWithVLVA;
import in.vakrangee.franchisee.adapter.ODSpnAdapter;
import in.vakrangee.supercore.franchisee.model.LeaveOD;
import in.vakrangee.supercore.franchisee.model.ODSpinner;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.webservice.WebService;

//

public class OutdoorDutyActivityWithVL_VA extends AppCompatActivity {
    InternetConnection internetConnection;
    Toolbar toolbaroOutDoorVA;


    TextView emptyView;
    private ODApplyAdpaterWithVLVA mAdapter;
    private RecyclerView recyclerView;
    Connection connection;
    TelephonyManager telephonyManager;
    String TAG = "Response";
    String diplayServerResopnse;

    ArrayList<LeaveOD> data;
    ProgressDialog progressODList;
    Spinner spnODStatus;
    private List<ODSpinner> ODSpinnerEntityList = new ArrayList<ODSpinner>();
    EditText edtFromDate, edtToDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fontawesome-webfont.ttf");

        setContentView(R.layout.activity_outdoor_duty_with_vl__v);

        toolbaroOutDoorVA = (Toolbar) findViewById(R.id.toolbaroutdoorVA);
        setSupportActionBar(toolbaroOutDoorVA);

        if (getSupportActionBar() != null) {
            toolbaroOutDoorVA.setTitleTextColor(Color.WHITE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.outdoorduty);

//            final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
//            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//            getSupportActionBar().setHomeAsUpIndicator(upArrow);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }
        internetConnection = new InternetConnection(getApplicationContext());
        connection = new Connection(getApplicationContext());
        //connection.openDatabase();
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        emptyView = (TextView) findViewById(R.id.empty_view);
        List<String> languages = new ArrayList<String>();
        languages.add("Please Select");
        languages.add("ALL");
        languages.add("Pending");
        languages.add("Recommened");
        languages.add("Approved");
        languages.add("Rejected");
        languages.add("On Hold");
        languages.add("Withdrawn");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, languages);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnODStatus = (Spinner) findViewById(R.id.spnODlist);
        //spnAccountStatement.setAdapter(dataAdapter);

        final ODSpnAdapter ODSpnAdapter = new ODSpnAdapter(this, android.R.layout.simple_spinner_dropdown_item, loadDummyCities());
        ODSpnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnODStatus.setAdapter(ODSpnAdapter);
        spnODStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = ODSpnAdapter.getItem(position).getCity();
                ODSpinner ODSpinner = ODSpnAdapter.getItem(position);
                if (mAdapter == null) {

                } else {
                    mAdapter.filter(item);

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        try {

            progressODList = new ProgressDialog(OutdoorDutyActivityWithVL_VA.this);
            progressODList.setTitle(R.string.accountStmt);
            progressODList.setMessage(getResources().getString(R.string.pleaseWait));
            progressODList.setCancelable(false);
            progressODList.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressODList.show();

            AsyncGetMyLeaveODList myRequest = new AsyncGetMyLeaveODList();
            myRequest.execute();
        } catch (Exception e) {
            e.getMessage();
            AlertDialogBoxInfo.alertDialogShow(OutdoorDutyActivityWithVL_VA.this, getResources().getString(R.string.cycerplatServicedown));

        }

        edtFromDate = (EditText) findViewById(R.id.ffromdate);
        edtToDate = (EditText) findViewById(R.id.todate);
        Date nowDate = new Date(System.currentTimeMillis());
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat dfDate = new SimpleDateFormat(myFormat, Locale.US);
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
                        OutdoorDutyActivityWithVL_VA.this, AlertDialog.BUTTON_POSITIVE, new DatePickerDialog.OnDateSetListener() {

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
                        OutdoorDutyActivityWithVL_VA.this, AlertDialog.BUTTON_POSITIVE, new DatePickerDialog.OnDateSetListener() {

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
        Button btnSignIn = (Button) findViewById(R.id.loginsubmit);
        btnSignIn.setTypeface(font);
        btnSignIn.setText(new SpannableStringBuilder(new String(new char[]{0xf090}) + " " + " " +
                getResources().getString(R.string.submit)));


    }

    private List<ODSpinner> loadDummyCities() {
        ODSpinnerEntityList = new ArrayList<ODSpinner>();


        ODSpinner ODSpinner1 = new ODSpinner();
        ODSpinner1.setId(1);
        ODSpinner1.setCity("ALL");
        ODSpinnerEntityList.add(ODSpinner1);
        ODSpinner ODSpinner2 = new ODSpinner();
        ODSpinner2.setId(2);
        ODSpinner2.setCity("Recommened");
        ODSpinnerEntityList.add(ODSpinner2);
        ODSpinner ODSpinner3 = new ODSpinner();
        ODSpinner3.setId(3);
        ODSpinner3.setCity("Approved");
        ODSpinnerEntityList.add(ODSpinner3);
        ODSpinner ODSpinner4 = new ODSpinner();
        ODSpinner4.setId(4);
        ODSpinner4.setCity("Rejected");
        ODSpinnerEntityList.add(ODSpinner4);
        ODSpinner ODSpinner5 = new ODSpinner();
        ODSpinner5.setId(5);
        ODSpinner5.setCity("On Hold");
        ODSpinnerEntityList.add(ODSpinner5);
        ODSpinner ODSpinner6 = new ODSpinner();
        ODSpinner6.setId(6);
        ODSpinner6.setCity("Pending");
        ODSpinnerEntityList.add(ODSpinner6);
        ODSpinner ODSpinner7 = new ODSpinner();
        ODSpinner7.setId(7);
        ODSpinner7.setCity("Withdrawn");
        ODSpinnerEntityList.add(ODSpinner7);


        return ODSpinnerEntityList;
    }

    private class AsyncGetMyLeaveODList extends AsyncTask<Void, Void, Void> {


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

                String vkidd = EncryptionUtil.encryptString(vkid, getApplicationContext());
                String TokenId = EncryptionUtil.encryptString(tokenId, getApplicationContext());

                String deviceIdget = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                String deviceid = EncryptionUtil.encryptString(deviceIdget, getApplicationContext());

                String deviceIDAndroid = CommonUtils.getAndroidUniqueID(getApplicationContext());
                String imei = EncryptionUtil.encryptString(deviceIDAndroid, getApplicationContext());

                String simSerial = CommonUtils.getSimSerialNumber(getApplicationContext());
                String simserialnumber = EncryptionUtil.encryptString(simSerial, getApplicationContext());

                String leaveOdId = EncryptionUtil.encryptString("1", getApplicationContext());


                diplayServerResopnse = WebService.getMyLeaveODList(vkidd, TokenId, imei, deviceid, simserialnumber, leaveOdId);


            } catch (Exception e) {
                e.printStackTrace();
                String message = null;
                Log.e(TAG + "Error", message);
                Log.i("TAG", ((message == null) ? "string null" : message));
                AlertDialogBoxInfo.alertDialogShow(OutdoorDutyActivityWithVL_VA.this, getResources().getString(R.string.Warning));
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");

            progressODList.dismiss();

            try {

                if (diplayServerResopnse.equals("Invalid request.")) {
                    Toast.makeText(getApplicationContext(), "Invalid request. ", Toast.LENGTH_SHORT).show();

                }
                StringTokenizer tokens = new StringTokenizer(diplayServerResopnse, "|");
                String first = tokens.hasMoreTokens() ? tokens.nextToken() : null;
                String second = tokens.hasMoreTokens() ? tokens.nextToken() : null;
                if (diplayServerResopnse == null) {

                    String message = null;
                    Log.i("TAG", ((message == null) ? "string null" : message));

                    //  Log.e(TAG + "Please Null error", diplayServerResopnse);
                } else if (second.equals("Invalid request.")) {

                    AlertDialogBoxInfo.alertDialogShow(OutdoorDutyActivityWithVL_VA.this, getResources().getString(R.string.Warning));

                } else if (second.equals("empty")) {
                    emptyView.setVisibility(View.VISIBLE);
                } else {


                    JSONArray jsonArray = new JSONArray();


                    JSONObject studentsObj = new JSONObject();
                    studentsObj.put("Students", jsonArray);


                    String jsonStr = second.toString();
                    data = new ArrayList<>();
                    try {


                        JSONArray array = new JSONArray(jsonStr);
                        //JSONArray Jarray = object.getJSONArray("Students");

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject Jasonobject = array.getJSONObject(i);
                            LeaveOD constants = new LeaveOD();
                            constants.setLeaveOdId(Jasonobject.getString("myModelObject1"));
                            constants.setStartDate(Jasonobject.getString("myModelObject3"));
                            constants.setEndDate(Jasonobject.getString("myModelObject4"));
                            constants.setReason(Jasonobject.getString("myModelObject5"));
                            constants.setTime(Jasonobject.getString("myModelObject2"));
                            constants.setLeaveOdStatus(Jasonobject.getString("myModelObject6"));


                            data.add(constants);
                        }


                        //Get the instance of JSONArray that contains JSONObjects


                        // Setup and Handover data to recyclerview
                        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_outdoor_duty);
                        mAdapter = new ODApplyAdpaterWithVLVA(OutdoorDutyActivityWithVL_VA.this, data);
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
                        AlertDialogBoxInfo.alertDialogShow(OutdoorDutyActivityWithVL_VA.this, getResources().getString(R.string.Warning));
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                AlertDialogBoxInfo.alertDialogShow(OutdoorDutyActivityWithVL_VA.this, getResources().getString(R.string.Warning));
            }

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        return super.onCreateOptionsMenu(menu);
    }

    public void onBackPressed() {

        Intent intent = new Intent(OutdoorDutyActivityWithVL_VA.this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(OutdoorDutyActivityWithVL_VA.this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();


                break;
        }
        return true;
    }

}
