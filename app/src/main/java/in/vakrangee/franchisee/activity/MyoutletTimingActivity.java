package in.vakrangee.franchisee.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import in.vakrangee.franchisee.R;

public class MyoutletTimingActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;

    TextView txtOpenMon, txtCloseMon;
    TextView txtOpenTue, txtCloseTue;
    TextView txtOpenWed, txtCloseWed;
    TextView txtOpenThu, txtCloseThu;
    TextView txtOpenFri, txtCloseFri;
    TextView txtOpenSat, txtCloseSat;
    TextView txtOpenSun, txtCloseSun;

    CheckBox checkBoxMon, checkBoxTue, checkBoxWed, checkBoxThu, checkBoxFri, checkBoxSat, checkBoxSun;



    private Calendar mCalen;
    private int hourOfDay;
    private int minute;
    private int ampm;
    int timePickerInput;
    int hour;
    private static final int Time_PICKER_ID = 0;


    Button btnSubmittime;
    String dataUnderline;

    String strOpenmMon = "9:00", strCloseMon = "9:00";
    String strOpenTue = "9.00", strCloseTue = "9.00";
    String strOpenWed = "9.00", strCloseWed = "9.00";
    String strOpenThu = "9.00", strCloseThu = "9.00";
    String strOpenFri = "9.00", strCloseFri = "9.00";
    String strOpenSat = "9.00", strCloseSat = "9.00";
    String strOpenSun = "9.00", strCloseSun = "9.00";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myoutlet_timing);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.outlestimeing);

//            final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
//            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//            getSupportActionBar().setHomeAsUpIndicator(upArrow);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        dataUnderline="9.00 AM";
        final SpannableString content = new SpannableString(dataUnderline);
        content.setSpan(new UnderlineSpan(), 0, dataUnderline.length(), 0);



        // set current time into output textview
        btnSubmittime = (Button) findViewById(R.id.btnSubmittime);

        txtOpenMon = (TextView) findViewById(R.id.txtOpenMon);
        txtCloseMon = (TextView) findViewById(R.id.txtCloseMon);


        txtOpenTue = (TextView) findViewById(R.id.txtOpenTue);
        txtCloseTue = (TextView) findViewById(R.id.txtCloseTue);

        txtOpenWed = (TextView) findViewById(R.id.txtOpenWed);
        txtCloseWed = (TextView) findViewById(R.id.txtCloseWed);

        txtOpenThu = (TextView) findViewById(R.id.txtOpenThu);
        txtCloseThu = (TextView) findViewById(R.id.txtCloseThu);

        txtOpenFri = (TextView) findViewById(R.id.txtOpenFri);
        txtCloseFri = (TextView) findViewById(R.id.txtCloseFri);

        txtOpenSat = (TextView) findViewById(R.id.txtOpenSat);
        txtCloseSat = (TextView) findViewById(R.id.txtCloseSat);

        txtOpenSun = (TextView) findViewById(R.id.txtOpenSun);
        txtCloseSun = (TextView) findViewById(R.id.txtCloseSun);


        checkBoxMon = (CheckBox) findViewById(R.id.checkBoxMon);
        checkBoxTue = (CheckBox) findViewById(R.id.checkBoxTue);
        checkBoxWed = (CheckBox) findViewById(R.id.checkBoxWed);
        checkBoxThu = (CheckBox) findViewById(R.id.checkBoxThu);
        checkBoxFri = (CheckBox) findViewById(R.id.checkBoxFri);
        checkBoxSat = (CheckBox) findViewById(R.id.checkBoxSat);
        checkBoxSun = (CheckBox) findViewById(R.id.checkBoxSun);


        txtOpenMon.setText(content);
        txtOpenTue.setText(content);
        txtOpenWed.setText(content);
        txtOpenThu.setText(content);
        txtOpenFri.setText(content);
        txtOpenSat.setText(content);
        txtOpenSun.setText(content);



        checkBoxMon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txtOpenMon.setClickable(true);
                    txtCloseMon.setClickable(true);
                    txtOpenMon.setTextColor(getResources().getColor(R.color.gray));
                    txtCloseMon.setTextColor(getResources().getColor(R.color.gray));
                    txtOpenMon.setText(R.string.opeingtime);
                    txtCloseMon.setText(R.string.closingtime);


                    txtOpenMon.setText(content);

                } else {

                    txtOpenMon.setClickable(false);
                    txtCloseMon.setClickable(false);
                    txtOpenMon.setText(R.string.opeingtime);
                    txtCloseMon.setText(R.string.closingtime);
                    txtOpenMon.setTextColor(getResources().getColor(R.color.gray));
                    txtCloseMon.setTextColor(getResources().getColor(R.color.gray));

                }

            }
        });

        checkBoxTue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txtOpenTue.setClickable(true);
                    txtCloseTue.setClickable(true);
                    txtOpenTue.setTextColor(getResources().getColor(R.color.gray));
                    txtCloseTue.setTextColor(getResources().getColor(R.color.gray));
                    txtOpenTue.setText(R.string.opeingtime);
                    txtCloseTue.setText(R.string.closingtime);


                } else {


                    txtOpenTue.setClickable(false);
                    txtCloseTue.setClickable(false);
                    txtOpenTue.setText(R.string.opeingtime);
                    txtCloseTue.setText(R.string.closingtime);
                    txtOpenTue.setTextColor(getResources().getColor(R.color.gray));
                    txtCloseTue.setTextColor(getResources().getColor(R.color.gray));
                }

            }
        });

        checkBoxWed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txtOpenWed.setClickable(true);
                    txtCloseWed.setClickable(true);
                    txtOpenWed.setTextColor(getResources().getColor(R.color.gray));
                    txtCloseWed.setTextColor(getResources().getColor(R.color.gray));
                    txtOpenWed.setText(R.string.opeingtime);
                    txtCloseWed.setText(R.string.closingtime);


                } else {


                    txtOpenWed.setClickable(false);
                    txtCloseWed.setClickable(false);
                    txtOpenWed.setText(R.string.opeingtime);
                    txtCloseWed.setText(R.string.closingtime);
                    txtOpenWed.setTextColor(getResources().getColor(R.color.gray));
                    txtCloseWed.setTextColor(getResources().getColor(R.color.gray));
                }

            }
        });

        checkBoxThu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txtOpenThu.setClickable(true);
                    txtCloseThu.setClickable(true);
                    txtOpenThu.setTextColor(getResources().getColor(R.color.gray));
                    txtCloseThu.setTextColor(getResources().getColor(R.color.gray));
                    txtOpenThu.setText(R.string.opeingtime);
                    txtCloseThu.setText(R.string.closingtime);


                } else {


                    txtOpenThu.setClickable(false);
                    txtCloseThu.setClickable(false);
                    txtOpenThu.setText(R.string.opeingtime);
                    txtCloseThu.setText(R.string.closingtime);
                    txtOpenThu.setTextColor(getResources().getColor(R.color.gray));
                    txtCloseThu.setTextColor(getResources().getColor(R.color.gray));
                }

            }
        });

        checkBoxFri.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txtOpenFri.setClickable(true);
                    txtCloseFri.setClickable(true);
                    txtOpenFri.setTextColor(getResources().getColor(R.color.gray));
                    txtCloseFri.setTextColor(getResources().getColor(R.color.gray));
                    txtOpenFri.setText(R.string.opeingtime);
                    txtCloseFri.setText(R.string.closingtime);


                } else {

                    txtOpenFri.setClickable(false);
                    txtCloseFri.setClickable(false);
                    txtOpenFri.setText(R.string.opeingtime);
                    txtCloseFri.setText(R.string.closingtime);
                    txtOpenFri.setTextColor(getResources().getColor(R.color.gray));
                    txtCloseFri.setTextColor(getResources().getColor(R.color.gray));

                }

            }
        });

        checkBoxSat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    txtOpenSat.setClickable(true);
                    txtCloseSat.setClickable(true);
                    txtOpenSat.setTextColor(getResources().getColor(R.color.black));
                    txtCloseSat.setTextColor(getResources().getColor(R.color.black));
                    txtOpenSat.setText(R.string.opeingtime);
                    txtCloseSat.setText(R.string.closingtime);


                } else {
                    txtOpenSat.setClickable(false);
                    txtCloseSat.setClickable(false);
                    txtOpenSat.setText(R.string.opeingtime);
                    txtCloseSat.setText(R.string.closingtime);
                    txtOpenSat.setTextColor(getResources().getColor(R.color.gray));
                    txtCloseSat.setTextColor(getResources().getColor(R.color.gray));

                }

            }
        });
        checkBoxSun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txtOpenSun.setClickable(true);
                    txtCloseSun.setClickable(true);
                    txtOpenSun.setTextColor(getResources().getColor(R.color.black));
                    txtCloseSun.setTextColor(getResources().getColor(R.color.black));
                    txtOpenSun.setText(R.string.opeingtime);
                    txtCloseSun.setText(R.string.closingtime);


                } else {

                    txtOpenSun.setClickable(false);
                    txtCloseSun.setClickable(false);
                    txtOpenSun.setText(R.string.opeingtime);
                    txtCloseSun.setText(R.string.closingtime);
                    txtOpenSun.setTextColor(getResources().getColor(R.color.gray));
                    txtCloseSun.setTextColor(getResources().getColor(R.color.gray));

                }

            }
        });




        mCalen = Calendar.getInstance();

        hourOfDay = mCalen.get(Calendar.HOUR_OF_DAY);
        minute = mCalen.get(Calendar.MINUTE);
        ampm = mCalen.get(Calendar.AM_PM);


        txtOpenMon.setOnClickListener(this);
        txtCloseMon.setOnClickListener(this);

        txtOpenTue.setOnClickListener(this);
        txtCloseTue.setOnClickListener(this);


        txtOpenWed.setOnClickListener(this);
        txtCloseWed.setOnClickListener(this);

        txtOpenThu.setOnClickListener(this);
        txtCloseThu.setOnClickListener(this);

        txtOpenFri.setOnClickListener(this);
        txtCloseFri.setOnClickListener(this);

        txtOpenSat.setOnClickListener(this);
        txtCloseSat.setOnClickListener(this);

        txtOpenSun.setOnClickListener(this);
        txtCloseSun.setOnClickListener(this);


        btnSubmittime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean a = CheckDates(strOpenmMon, strCloseMon);
                if (a == true) {
                    Toast.makeText(MyoutletTimingActivity.this, "true", Toast.LENGTH_SHORT).show();
                    txtOpenMon.setError(null);
                    txtCloseMon.setError(null);
                } else {
                    txtOpenMon.setError("");
                    txtCloseMon.setError("");
                    //Toast.makeText(MyoutletTimingActivity.this, strOpenmMon + "not match" + strCloseMon, Toast.LENGTH_SHORT).show();
                }

                boolean b = CheckDates(strOpenTue, strCloseTue);
                if (b == true) {
                    Toast.makeText(MyoutletTimingActivity.this, "true", Toast.LENGTH_SHORT).show();
                } else {
                    // Toast.makeText(MyoutletTimingActivity.this, strOpenTue + "not match" + strCloseTue, Toast.LENGTH_SHORT).show();
                }


                boolean c = CheckDates(strOpenWed, strCloseWed);
                if (c == true) {
                    Toast.makeText(MyoutletTimingActivity.this, "true", Toast.LENGTH_SHORT).show();
                } else {
                    //  Toast.makeText(MyoutletTimingActivity.this, strOpenWed + "not match" + strCloseWed, Toast.LENGTH_SHORT).show();
                }
                boolean d = CheckDates(strOpenThu, strCloseThu);
                if (d == true) {
                    Toast.makeText(MyoutletTimingActivity.this, "true", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(MyoutletTimingActivity.this, strOpenThu + "not match" + strCloseThu, Toast.LENGTH_SHORT).show();
                }
                boolean e = CheckDates(strOpenFri, strCloseFri);
                if (e == true) {
                    Toast.makeText(MyoutletTimingActivity.this, "true", Toast.LENGTH_SHORT).show();
                } else {
                    // Toast.makeText(MyoutletTimingActivity.this, strOpenFri + "not match" + strCloseFri, Toast.LENGTH_SHORT).show();
                }

                boolean f = CheckDates(strOpenSat, strCloseSat);
                if (f == true) {
                    Toast.makeText(MyoutletTimingActivity.this, "true", Toast.LENGTH_SHORT).show();
                } else {
                    // Toast.makeText(MyoutletTimingActivity.this, strOpenSat + "not match" + strCloseSat, Toast.LENGTH_SHORT).show();
                }
                boolean g = CheckDates(strOpenSun, strCloseSun);
                if (g == true) {
                    Toast.makeText(MyoutletTimingActivity.this, "true", Toast.LENGTH_SHORT).show();
                } else {
                    // Toast.makeText(MyoutletTimingActivity.this, strOpenSun + "not match" + strCloseSun, Toast.LENGTH_SHORT).show();
                }
            }

        });


    }

    private boolean CheckDates(String startDate, String endDate) {
        SimpleDateFormat dfDate = new SimpleDateFormat("HH:mm");

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


    public void onBackPressed() {

        Intent intent = new Intent(MyoutletTimingActivity.this, MyVakrangeeKendra.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(MyoutletTimingActivity.this, MyVakrangeeKendra.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

                break;
        }
        return true;
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {

        switch (id) {
            case Time_PICKER_ID:

                return new TimePickerDialog(this, TimePickerListener,
                        hourOfDay, minute, false);
        }
        return null;
    }


    private TimePickerDialog.OnTimeSetListener TimePickerListener =
            new TimePickerDialog.OnTimeSetListener() {

                // while dialog box is closed, below method is called.
                @SuppressLint("SetTextI18n")
                public void onTimeSet(TimePicker view, int hour, int minute) {


                    switch (timePickerInput) {
                        case R.id.txtOpenMon:

                            mCalen.set(Calendar.HOUR_OF_DAY, hour);
                            mCalen.set(Calendar.MINUTE, minute);

                            hourOfDay = mCalen.get(Calendar.HOUR_OF_DAY);
                            minute = mCalen.get(Calendar.MINUTE);
                            ampm = mCalen.get(Calendar.AM_PM);
                            String strHrsToShow = (mCalen.get(Calendar.HOUR) == 0) ? "12" : mCalen.get(Calendar.HOUR) + "";
                            String ampmStr = (ampm == 0) ? "AM" : "PM";
                            txtOpenMon.setText(strHrsToShow + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmStr);
                            // strOpenmMon=strHrsToShow+minute;
                            strOpenmMon = convertTo24Hour(strHrsToShow + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmStr);
                            Log.e("OutdoorStartDate", strOpenmMon);
                            break;

                        case R.id.txtCloseMon:
                            mCalen.set(Calendar.HOUR_OF_DAY, hour);
                            mCalen.set(Calendar.MINUTE, minute);
                            int hour12format2 = mCalen.get(Calendar.HOUR);
                            hourOfDay = mCalen.get(Calendar.HOUR_OF_DAY);
                            minute = mCalen.get(Calendar.MINUTE);
                            ampm = mCalen.get(Calendar.AM_PM);
                            String strHrsToShow2 = (mCalen.get(Calendar.HOUR) == 0) ? "12" : mCalen.get(Calendar.HOUR) + "";
                            String ampmStr2 = (ampm == 0) ? "AM" : "PM";
                            txtCloseMon.setText(strHrsToShow2 + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmStr2);
                            strCloseMon = convertTo24Hour(strHrsToShow2 + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmStr2);
                            ;
                            break;
                        case R.id.txtOpenTue:
                            mCalen.set(Calendar.HOUR_OF_DAY, hour);
                            mCalen.set(Calendar.MINUTE, minute);
                            int hour12format3 = mCalen.get(Calendar.HOUR);
                            hourOfDay = mCalen.get(Calendar.HOUR_OF_DAY);
                            minute = mCalen.get(Calendar.MINUTE);
                            ampm = mCalen.get(Calendar.AM_PM);
                            String ampmStr3 = (ampm == 0) ? "AM" : "PM";
                            String strHrsToShow3 = (mCalen.get(Calendar.HOUR) == 0) ? "12" : mCalen.get(Calendar.HOUR) + "";
                            txtOpenTue.setText(strHrsToShow3 + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmStr3);
                            strOpenTue = convertTo24Hour(strHrsToShow3 + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmStr3);
                            break;

                        case R.id.txtCloseTue:
                            mCalen.set(Calendar.HOUR_OF_DAY, hour);
                            mCalen.set(Calendar.MINUTE, minute);
                            int hour12format4 = mCalen.get(Calendar.HOUR);
                            hourOfDay = mCalen.get(Calendar.HOUR_OF_DAY);
                            minute = mCalen.get(Calendar.MINUTE);
                            ampm = mCalen.get(Calendar.AM_PM);
                            String ampmStr4 = (ampm == 0) ? "AM" : "PM";
                            String strHrsToShow4 = (mCalen.get(Calendar.HOUR) == 0) ? "12" : mCalen.get(Calendar.HOUR) + "";
                            txtCloseTue.setText(strHrsToShow4 + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmStr4);
                            strCloseTue = convertTo24Hour(strHrsToShow4 + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmStr4);
                            break;

                        case R.id.txtOpenWed:
                            mCalen.set(Calendar.HOUR_OF_DAY, hour);
                            mCalen.set(Calendar.MINUTE, minute);
                            int hour12format5 = mCalen.get(Calendar.HOUR);
                            hourOfDay = mCalen.get(Calendar.HOUR_OF_DAY);
                            minute = mCalen.get(Calendar.MINUTE);
                            ampm = mCalen.get(Calendar.AM_PM);
                            String strHrsToShow5 = (mCalen.get(Calendar.HOUR) == 0) ? "12" : mCalen.get(Calendar.HOUR) + "";
                            String ampmStr5 = (ampm == 0) ? "AM" : "PM";
                            txtOpenWed.setText(strHrsToShow5 + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmStr5);
                            strOpenWed = convertTo24Hour(strHrsToShow5 + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmStr5);
                            break;
                        case R.id.txtCloseWed:
                            mCalen.set(Calendar.HOUR_OF_DAY, hour);
                            mCalen.set(Calendar.MINUTE, minute);
                            int hour12format6 = mCalen.get(Calendar.HOUR);
                            hourOfDay = mCalen.get(Calendar.HOUR_OF_DAY);
                            minute = mCalen.get(Calendar.MINUTE);
                            ampm = mCalen.get(Calendar.AM_PM);
                            String strHrsToShow6 = (mCalen.get(Calendar.HOUR) == 0) ? "12" : mCalen.get(Calendar.HOUR) + "";
                            String ampmStr6 = (ampm == 0) ? "AM" : "PM";
                            txtCloseWed.setText(strHrsToShow6 + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmStr6);
                            strCloseWed = convertTo24Hour(strHrsToShow6 + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmStr6);
                            break;
                        case R.id.txtOpenThu:
                            mCalen.set(Calendar.HOUR_OF_DAY, hour);
                            mCalen.set(Calendar.MINUTE, minute);
                            int hour12format7 = mCalen.get(Calendar.HOUR);
                            hourOfDay = mCalen.get(Calendar.HOUR_OF_DAY);
                            minute = mCalen.get(Calendar.MINUTE);
                            ampm = mCalen.get(Calendar.AM_PM);
                            String strHrsToShow7 = (mCalen.get(Calendar.HOUR) == 0) ? "12" : mCalen.get(Calendar.HOUR) + "";
                            String ampmStr7 = (ampm == 0) ? "AM" : "PM";
                            txtOpenThu.setText(strHrsToShow7 + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmStr7);
                            strOpenThu = convertTo24Hour(strHrsToShow7 + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmStr7);
                            break;
                        case R.id.txtCloseThu:
                            mCalen.set(Calendar.HOUR_OF_DAY, hour);
                            mCalen.set(Calendar.MINUTE, minute);
                            int hour12format8 = mCalen.get(Calendar.HOUR);
                            hourOfDay = mCalen.get(Calendar.HOUR_OF_DAY);
                            minute = mCalen.get(Calendar.MINUTE);
                            ampm = mCalen.get(Calendar.AM_PM);
                            String strHrsToShow8 = (mCalen.get(Calendar.HOUR) == 0) ? "12" : mCalen.get(Calendar.HOUR) + "";
                            String ampmStr8 = (ampm == 0) ? "AM" : "PM";
                            txtCloseThu.setText(strHrsToShow8 + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmStr8);
                            strCloseThu = convertTo24Hour(strHrsToShow8 + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmStr8);
                            break;

                        case R.id.txtOpenFri:
                            mCalen.set(Calendar.HOUR_OF_DAY, hour);
                            mCalen.set(Calendar.MINUTE, minute);
                            int hour12format9 = mCalen.get(Calendar.HOUR);
                            hourOfDay = mCalen.get(Calendar.HOUR_OF_DAY);
                            minute = mCalen.get(Calendar.MINUTE);
                            ampm = mCalen.get(Calendar.AM_PM);
                            String strHrsToShow9 = (mCalen.get(Calendar.HOUR) == 0) ? "12" : mCalen.get(Calendar.HOUR) + "";
                            String ampmStr9 = (ampm == 0) ? "AM" : "PM";
                            txtOpenFri.setText(strHrsToShow9 + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmStr9);
                            strOpenFri = convertTo24Hour(strHrsToShow9 + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmStr9);
                            break;

                        case R.id.txtCloseFri:
                            mCalen.set(Calendar.HOUR_OF_DAY, hour);
                            mCalen.set(Calendar.MINUTE, minute);
                            int hour12format10 = mCalen.get(Calendar.HOUR);
                            hourOfDay = mCalen.get(Calendar.HOUR_OF_DAY);
                            minute = mCalen.get(Calendar.MINUTE);
                            ampm = mCalen.get(Calendar.AM_PM);
                            String strHrsToShow10 = (mCalen.get(Calendar.HOUR) == 0) ? "12" : mCalen.get(Calendar.HOUR) + "";
                            String ampmStr10 = (ampm == 0) ? "AM" : "PM";
                            txtCloseFri.setText(strHrsToShow10 + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmStr10);
                            strCloseFri = convertTo24Hour(strHrsToShow10 + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmStr10);
                            break;
                        case R.id.txtOpenSat:
                            mCalen.set(Calendar.HOUR_OF_DAY, hour);
                            mCalen.set(Calendar.MINUTE, minute);
                            int hour12format11 = mCalen.get(Calendar.HOUR);
                            hourOfDay = mCalen.get(Calendar.HOUR_OF_DAY);
                            minute = mCalen.get(Calendar.MINUTE);
                            ampm = mCalen.get(Calendar.AM_PM);
                            String strHrsToShr11 = (mCalen.get(Calendar.HOUR) == 0) ? "12" : mCalen.get(Calendar.HOUR) + "";
                            String ampmSt11 = (ampm == 0) ? "AM" : "PM";
                            txtOpenSat.setText(strHrsToShr11 + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmSt11);
                            strOpenSat = convertTo24Hour(strHrsToShr11 + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmSt11);
                            break;
                        case R.id.txtCloseSat:
                            mCalen.set(Calendar.HOUR_OF_DAY, hour);
                            mCalen.set(Calendar.MINUTE, minute);
                            int hour12format12 = mCalen.get(Calendar.HOUR);
                            hourOfDay = mCalen.get(Calendar.HOUR_OF_DAY);
                            minute = mCalen.get(Calendar.MINUTE);
                            ampm = mCalen.get(Calendar.AM_PM);
                            String strHrsToShow12 = (mCalen.get(Calendar.HOUR) == 0) ? "12" : mCalen.get(Calendar.HOUR) + "";
                            String ampmStr12 = (ampm == 0) ? "AM" : "PM";
                            txtCloseSat.setText(strHrsToShow12 + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmStr12);
                            strCloseSat = convertTo24Hour(strHrsToShow12 + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmStr12);
                            break;
                        case R.id.txtOpenSun:
                            mCalen.set(Calendar.HOUR_OF_DAY, hour);
                            mCalen.set(Calendar.MINUTE, minute);
                            int hour12format13 = mCalen.get(Calendar.HOUR);
                            hourOfDay = mCalen.get(Calendar.HOUR_OF_DAY);
                            minute = mCalen.get(Calendar.MINUTE);
                            ampm = mCalen.get(Calendar.AM_PM);
                            String strHrsToShow13 = (mCalen.get(Calendar.HOUR) == 0) ? "12" : mCalen.get(Calendar.HOUR) + "";
                            String ampmSt13 = (ampm == 0) ? "AM" : "PM";
                            txtOpenSun.setText(strHrsToShow13 + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmSt13);
                            strOpenSun = convertTo24Hour(strHrsToShow13 + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmSt13);
                            break;

                        case R.id.txtCloseSun:
                            mCalen.set(Calendar.HOUR_OF_DAY, hour);
                            mCalen.set(Calendar.MINUTE, minute);
                            int hour12format14 = mCalen.get(Calendar.HOUR);
                            hourOfDay = mCalen.get(Calendar.HOUR_OF_DAY);
                            minute = mCalen.get(Calendar.MINUTE);
                            ampm = mCalen.get(Calendar.AM_PM);
                            String strHrsToShow14 = (mCalen.get(Calendar.HOUR) == 0) ? "12" : mCalen.get(Calendar.HOUR) + "";
                            String ampmStr14 = (ampm == 0) ? "AM" : "PM";
                            txtCloseSun.setText(strHrsToShow14 + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmStr14);
                            strCloseSun = convertTo24Hour(strHrsToShow14 + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmStr14);
                            break;
                    }

                }
            };

    public static String convertTo24Hour(String Time) {
        DateFormat f1 = new SimpleDateFormat("hh:mm a"); //11:00 pm
        Log.e("time", Time);
        Date d = null;
        try {
            d = f1.parse(Time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        DateFormat f2 = new SimpleDateFormat("HH:mm");
        String x = f2.format(d); // "23:00"
        Log.e("x", x);
        return x;
    }


    @Override
    public void onClick(View v) {
        timePickerInput = v.getId();
        showDialog(Time_PICKER_ID);
    }
}

