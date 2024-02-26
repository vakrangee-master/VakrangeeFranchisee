package in.vakrangee.franchisee.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.adapter.ODApplyAdpater;
import in.vakrangee.franchisee.adapter.ODSpnAdapter;
import in.vakrangee.supercore.franchisee.model.LeaveOD;
import in.vakrangee.supercore.franchisee.model.ODSpinner;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.CustomDateTimePicker;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.webservice.WebService;

//

public class OutdoorDutyActivity extends AppCompatActivity {

    EditText edtFromDate, edtToDate;

    Button btnODFilterData;


    Toolbar toolbar;
    ProgressDialog progress, progressODList;
    TelephonyManager telephonyManager;
    String TAG = "Response";
    String diplayServerResopnse;
    InternetConnection internetConnection;

    private TextView emptyView;
    Button btnSubmitOutdoorDuty;

    private ODApplyAdpater mAdapter;
    private RecyclerView recyclerView;

    String ODfrom, ODto;
    Button ODFilterApply;
    Spinner spnODStatus;
    //model object for our list data

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    Connection connection;
    EditText fromdateODpopup, todateODpopup, edtODReasonpopup;
    CustomDateTimePicker customto, customfrom;
//-----------------------------------------------------

    private List<ODSpinner> ODSpinnerEntityList = new ArrayList<ODSpinner>();
    private in.vakrangee.franchisee.adapter.ODSpnAdapter ODSpnAdapter;
    private static final int PLACE_PICKER_REQUEST = 1;

    ArrayList<LeaveOD> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fontawesome-webfont.ttf");

        setContentView(R.layout.activity_outdoor_duty);


        toolbar = (Toolbar) findViewById(R.id.toolbarhlep);
        setSupportActionBar(toolbar);

        connection = new Connection(getApplicationContext());
        //connection.openDatabase();
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        if (getSupportActionBar() != null) {
            toolbar.setTitleTextColor(Color.WHITE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.outdoorduty);

//            final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
//            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//            getSupportActionBar().setHomeAsUpIndicator(upArrow);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }
        emptyView = (TextView) findViewById(R.id.empty_view);


        internetConnection = new InternetConnection(getApplicationContext());


        try {


            progressODList = new ProgressDialog(OutdoorDutyActivity.this);
            progressODList.setTitle(R.string.accountStmt);
            progressODList.setMessage(getResources().getString(R.string.pleaseWait));
            progressODList.setCancelable(false);
            progressODList.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressODList.show();

            AsyncGetMyLeaveODList myRequest = new AsyncGetMyLeaveODList();
            myRequest.execute();


            btnSubmitOutdoorDuty = (Button) findViewById(R.id.outdoorsubmitRecharge);
            btnSubmitOutdoorDuty.setTypeface(font);
            btnSubmitOutdoorDuty.setText(new SpannableStringBuilder(new String(new char[]{0xf067}) + " " + getResources().getString(R.string.add)));


            btnSubmitOutdoorDuty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Addleavepopup();
                }
            });


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

            ODSpnAdapter = new ODSpnAdapter(this, android.R.layout.simple_spinner_dropdown_item, loadDummyCities());
            ODSpnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spnODStatus.setAdapter(ODSpnAdapter);


            btnODFilterData = (Button) findViewById(R.id.btnODFilterData);
            btnODFilterData.setTypeface(font);
            btnODFilterData.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " + getResources().getString(R.string.go)));

            edtFromDate = (EditText) findViewById(R.id.ffromdate);
            edtToDate = (EditText) findViewById(R.id.todate);

            btnODFilterData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String dateto = edtToDate.getText().toString() + " " + "00:00:00";
                    String datefrom = edtFromDate.getText().toString() + " " + "00:00:00";


                }
            });

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

            //loading list view item with this function
            String myFormat = "dd-MM-yyyy"; //In which you need put here
            SimpleDateFormat dfDate = new SimpleDateFormat(myFormat, Locale.US);

            Date nowDate = new Date(System.currentTimeMillis());


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
                            OutdoorDutyActivity.this, AlertDialog.BUTTON_POSITIVE, new DatePickerDialog.OnDateSetListener() {

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
                            OutdoorDutyActivity.this, AlertDialog.BUTTON_POSITIVE, new DatePickerDialog.OnDateSetListener() {

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


            // ATTENTION: This was auto-generated to implement the App Indexing API.
            // See https://g.co/AppIndexing/AndroidStudio for more information.
            client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        } catch (Exception e) {
            e.getMessage();
        }
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

    private void Addleavepopup() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(OutdoorDutyActivity.this);
        //inflate layout from xml. you must create an xml layout file in res/layout first
        LayoutInflater inflater = OutdoorDutyActivity.this.getLayoutInflater();
        View layout = inflater.inflate(R.layout.popupodadd, null);
        builder.setView(layout);

        fromdateODpopup = (EditText) layout.findViewById(R.id.fromdateODpopup);
        todateODpopup = (EditText) layout.findViewById(R.id.todateODpopup);
        edtODReasonpopup = (EditText) layout.findViewById(R.id.edtODReasonpopup);


        fromdateODpopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customfrom.showDialog();
            }
        });


        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        // formattedDate have current date/time

        fromdateODpopup.setText(formattedDate);
        fromdateODpopup.setTextColor(Color.parseColor("#468847"));

        customto = new CustomDateTimePicker(OutdoorDutyActivity.this,
                new CustomDateTimePicker.ICustomDateTimeListener() {

                    @Override
                    public void onSet(Dialog dialog, Calendar calendarSelected,
                                      Date dateSelected, int year, String monthFullName,
                                      String monthShortName, int monthNumber, int date,
                                      String weekDayFullName, String weekDayShortName,
                                      int hour24, String hours12, int min, int sec,
                                      String AM_PM) {


                        String da = String.valueOf(calendarSelected.get(Calendar.DAY_OF_MONTH));
                        String DDay = ((calendarSelected.get(Calendar.DAY_OF_MONTH)) < 10 ? "0" : "") + da;

                        String mo = String.valueOf((monthNumber + 1));
                        String month = ((monthNumber + 1) < 10 ? "0" : "") + mo;

                        String mi = String.valueOf(min);
                        String mina = (min < 10 ? "0" : "") + mi + ":00";

                        todateODpopup.setText(DDay
                                + "-" + month + "-" + year
                                + " " + hours12 + ":" + mina);


                    }


                    @Override
                    public void onCancel() {

                    }
                });


        customfrom = new CustomDateTimePicker(OutdoorDutyActivity.this,
                new CustomDateTimePicker.ICustomDateTimeListener() {

                    @Override
                    public void onSet(Dialog dialog, Calendar calendarSelected,
                                      Date dateSelected, int year, String monthFullName,
                                      String monthShortName, int monthNumber, int date,
                                      String weekDayFullName, String weekDayShortName,
                                      int hour24, String hours12, int min, int sec,
                                      String AM_PM) {


                        String da = String.valueOf(calendarSelected.get(Calendar.DAY_OF_MONTH));
                        String DDay = ((calendarSelected.get(Calendar.DAY_OF_MONTH)) < 10 ? "0" : "") + da;

                        String mo = String.valueOf((monthNumber + 1));
                        String month = ((monthNumber + 1) < 10 ? "0" : "") + mo;

                        String mi = String.valueOf(min);
                        String mina = (min < 10 ? "0" : "") + mi + ":00";

                        Calendar c = Calendar.getInstance();
                        c.set(year, Calendar.DAY_OF_MONTH, Calendar.DAY_OF_MONTH);


                        fromdateODpopup.setText(DDay
                                + "-" + month + "-" + year
                                + " " + hours12 + ":" + mina);


                    }


                    @Override
                    public void onCancel() {

                    }
                });
        customfrom.set24HourFormat(true);
        customfrom.setDate(Calendar.getInstance());

        customto.set24HourFormat(true);
        customto.setDate(Calendar.getInstance());


        todateODpopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customto.showDialog();
            }
        });


        todateODpopup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (i == 0) {
                    // Toast.makeText(getApplicationContext(), "Please Enter 10 digits", Toast.LENGTH_SHORT).show();
                    todateODpopup.setTextColor(Color.parseColor("#000000"));
                    todateODpopup.setError("Enter To date");

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (todateODpopup.length() <= 3) {


                } else {
                    todateODpopup.setTextColor(Color.parseColor("#468847"));
                    todateODpopup.setError(null);

                }
            }
        });


        fromdateODpopup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (i == 0) {
                    // Toast.makeText(getApplicationContext(), "Please Enter 10 digits", Toast.LENGTH_SHORT).show();
                    fromdateODpopup.setTextColor(Color.parseColor("#000000"));
                    fromdateODpopup.setError("Enter from date");

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (fromdateODpopup.length() <= 3) {


                } else {
                    fromdateODpopup.setTextColor(Color.parseColor("#468847"));
                    fromdateODpopup.setError(null);

                }
            }
        });

        edtODReasonpopup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (i < 4) {
                    // Toast.makeText(getApplicationContext(), "Please Enter 10 digits", Toast.LENGTH_SHORT).show();
                    edtODReasonpopup.setTextColor(Color.parseColor("#000000"));
                    edtODReasonpopup.setError("Enter minimumm 4 character OD reason.");

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edtODReasonpopup.length() <= 3) {


                } else {
                    edtODReasonpopup.setTextColor(Color.parseColor("#468847"));
                    edtODReasonpopup.setError(null);

                }
            }
        });

        builder.setPositiveButton(R.string.Confirm, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        builder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(edtODReasonpopup, InputMethodManager.SHOW_IMPLICIT);
            }
        });


        dialog.show();
//Overriding the handler immediately after show is probably a better approach than OnShowListener as described below
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*  DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                Date date = null;
                try {
                    date = inputFormat.parse(from);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                ODfrom = outputFormat.format(date);

*/
                String reason = edtODReasonpopup.getText().toString();
                String ODto = todateODpopup.getText().toString();
                String from = fromdateODpopup.getText().toString();
                boolean both = CheckDates(from, ODto);
                boolean valid = true;
                try {


                    if (reason != null && reason.trim().length() == 0) {
                        edtODReasonpopup.setError("Enter Reason");
                        Toast.makeText(getApplicationContext(), "  Enter Reason  ", Toast.LENGTH_SHORT).show();
                        valid = false;
                    }

                    if (from != null && from.trim().length() == 0) {
                        fromdateODpopup.setError("Enter from date");
                        valid = false;
                    }
                    if (ODto != null && ODto.trim().length() == 0) {
                        todateODpopup.setError("Enter To date");
                        valid = false;
                    }

                    if (from.equals(ODto)) {
                        todateODpopup.setError("To date");
                        fromdateODpopup.setError("From date");
                        Toast.makeText(getApplicationContext(), "select different time", Toast.LENGTH_SHORT).show();
                        valid = false;
                    }

                    if (both == false) {
                        todateODpopup.setError("To date");
                        fromdateODpopup.setError("From date");

                        Toast.makeText(getApplicationContext(), " Please select proper date & time ", Toast.LENGTH_SHORT).show();
                        valid = false;
                    }

                    String vkid = connection.getVkid();

                    if (!(vkid.toUpperCase().startsWith("VL") || vkid.toUpperCase().startsWith("VA"))) {

                        AlertDialogBoxInfo.alertDialogShow(getApplicationContext(), getResources().getString(R.string.serviceonlyfranchiess));
                        valid = false;
                    }

                    if (internetConnection.isConnectingToInternet() == false) {

                        AlertDialogBoxInfo.alertDialogShow(getApplicationContext(), getResources().getString(R.string.internetCheck));
                        valid = false;
                    }
                    if (valid) {

                        progress = new ProgressDialog(OutdoorDutyActivity.this);
                        progress.setTitle(R.string.accountStmt);
                        progress.setMessage(getResources().getString(R.string.pleaseWait));
                        progress.setCancelable(false);
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.show();
                        // new AsyncGetAddLeaveOD(getApplicationContext()).execute(vkidd, TokenID, imei, deviceid, simserialnumber);
                        AsyncGetAddLeaveODPopup myRequest = new AsyncGetAddLeaveODPopup();
                        myRequest.execute();
                        dialog.dismiss();
                    }


                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });


        // builder.show();


    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent dataa) {

        if (requestCode == PLACE_PICKER_REQUEST
                && resultCode == Activity.RESULT_OK) {

            final Place place = PlacePicker.getPlace(this, dataa);
            final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();
            String attributions = (String) place.getAttributions();
            if (attributions == null) {
                attributions = "";
            }


        } else {
            super.onActivityResult(requestCode, resultCode, dataa);
        }
    }


    public static boolean CheckDates(String startDate, String endDate) {

        SimpleDateFormat dfDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        boolean b = false;

        try {
            if (dfDate.parse(startDate).before(dfDate.parse(endDate))) {
                b = true;  // If start date is before end date.
            } else if (dfDate.parse(startDate).equals(dfDate.parse(endDate))) {
                b = true;  // If two dates are equal.
            } else {
                b = false; // If start date is after the end date.
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return b;
    }


    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        SubMenu subMenu1 = menu.addSubMenu("Action Item");

        MenuItem subMenu1Item = subMenu1.getItem();
        subMenu1Item.setIcon(R.drawable.addod);
        subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        subMenu1Item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Addleavepopup();
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    public void onBackPressed() {

        Intent intent = new Intent(OutdoorDutyActivity.this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(OutdoorDutyActivity.this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();


                break;
        }
        return true;
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    private class AsyncGetAddLeaveODPopup extends AsyncTask<Void, Void, Void> {


        String fromdate = fromdateODpopup.getText().toString();
        String todate = todateODpopup.getText().toString();
        String reasonOd = edtODReasonpopup.getText().toString();
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

                String dateStringFrom = fromdate;
                Date date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(dateStringFrom);
                String dateString2from = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);

                String dateStringto = todate;
                Date datet = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(dateStringto);
                String dateStringtodate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(datet);


                String vkidd = EncryptionUtil.encryptString(vkid, getApplicationContext());
                String TokenId = EncryptionUtil.encryptString(tokenId, getApplicationContext());

                String deviceIdget = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                String deviceid = EncryptionUtil.encryptString(deviceIdget, getApplicationContext());

                String deviceIDAndroid = CommonUtils.getAndroidUniqueID(getApplicationContext());
                String imei = EncryptionUtil.encryptString(deviceIDAndroid, getApplicationContext());

                String simSerial = CommonUtils.getSimSerialNumber(getApplicationContext());
                String simserialnumber = EncryptionUtil.encryptString(simSerial, getApplicationContext());

                String leaveTypeId = EncryptionUtil.encryptString("1", getApplicationContext());
                String fromDate = EncryptionUtil.encryptString(dateString2from, getApplicationContext());
                String toDate = EncryptionUtil.encryptString(dateStringtodate, getApplicationContext());
                String reason = EncryptionUtil.encryptString(reasonOd, getApplicationContext());

                diplayServerResopnse = WebService.addLeaveOD(vkidd, TokenId, imei, deviceid, simserialnumber, leaveTypeId, fromDate, toDate, reason);


            } catch (Exception e) {
                e.printStackTrace();
                String message = null;
                Log.e(TAG + "Error", message);
                Log.i("TAG", ((message == null) ? "string null" : message));
                AlertDialogBoxInfo.alertDialogShow(OutdoorDutyActivity.this, getResources().getString(R.string.Warning));
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");

            progress.dismiss();
            try {


                if (diplayServerResopnse.equals("OKAY")) {
                    AlertDialogBoxInfo.alertDialogShow(OutdoorDutyActivity.this, getResources().getString(R.string.odapply));
                    progressODList = new ProgressDialog(OutdoorDutyActivity.this);
                    progressODList.setTitle(R.string.accountStmt);
                    progressODList.setMessage(getResources().getString(R.string.pleaseWait));
                    progressODList.setCancelable(false);
                    progressODList.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressODList.show();

                    AsyncGetMyLeaveODList myRequest = new AsyncGetMyLeaveODList();
                    myRequest.execute();


                } else if (diplayServerResopnse.equals("OKAY|OD for the time frame provided already exists.")) {
                    Log.e(TAG + "Error", diplayServerResopnse);
                    AlertDialogBoxInfo.alertDialogShow(OutdoorDutyActivity.this, getResources().getString(R.string.odalradyexist));

                } else if (diplayServerResopnse.equals("OKAY|OD start time should be less than OD end time.")) {
                    Log.e(TAG + "Error", diplayServerResopnse);
                    AlertDialogBoxInfo.alertDialogShow(OutdoorDutyActivity.this, getResources().getString(R.string.odstarttimeendtime));

                } else if (diplayServerResopnse.equals("OKAY|OD start time and end time cannot be same.")) {
                    Log.e(TAG + "Error", diplayServerResopnse);
                    AlertDialogBoxInfo.alertDialogShow(OutdoorDutyActivity.this, getResources().getString(R.string.odstartandendtimenotsame));

                } else if (diplayServerResopnse.equals("OKAY|You cannot apply for OD after the OD start day has passed.")) {
                    Log.e(TAG + "Error", diplayServerResopnse);
                    AlertDialogBoxInfo.alertDialogShow(OutdoorDutyActivity.this, getResources().getString(R.string.odpassaway));

                } else {
                    Log.e(TAG + "Error in Server", diplayServerResopnse);
                    // Toast.makeText(getApplicationContext(), "Error OTP ", Toast.LENGTH_SHORT).show();
                    AlertDialogBoxInfo.alertDialogShow(OutdoorDutyActivity.this, diplayServerResopnse);

                }
            } catch (Exception e) {
                AlertDialogBoxInfo.alertDialogShow(OutdoorDutyActivity.this, getResources().getString(R.string.Warning));

                e.printStackTrace();
            }


        }


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

                String leaveOdId = EncryptionUtil.encryptString("1", getApplicationContext());


                diplayServerResopnse = WebService.getMyLeaveODList(vkidd, TokenId, imei, deviceid, simserialnumber, leaveOdId);


            } catch (Exception e) {
                e.printStackTrace();
                String message = null;
                Log.e(TAG + "Error", message);
                Log.i("TAG", ((message == null) ? "string null" : message));
                AlertDialogBoxInfo.alertDialogShow(OutdoorDutyActivity.this, getResources().getString(R.string.Warning));
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

                    AlertDialogBoxInfo.alertDialogShow(OutdoorDutyActivity.this, getResources().getString(R.string.Warning));

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
                        mAdapter = new ODApplyAdpater(OutdoorDutyActivity.this, data);
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
                        AlertDialogBoxInfo.alertDialogShow(OutdoorDutyActivity.this, getResources().getString(R.string.Warning));
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                AlertDialogBoxInfo.alertDialogShow(OutdoorDutyActivity.this, getResources().getString(R.string.Warning));
            }

        }


    }


}
