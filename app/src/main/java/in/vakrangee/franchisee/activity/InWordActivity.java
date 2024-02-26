package in.vakrangee.franchisee.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.vakrangee.franchisee.R;

public class InWordActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button btnSubmitInword, btnClear, btnCancle;
    EditText edtFromDate, edtToDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fontawesome-webfont.ttf");

        setContentView(R.layout.activity_in_word);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.inwordinfo);

//            final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
//            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//            getSupportActionBar().setHomeAsUpIndicator(upArrow);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        btnSubmitInword = (Button) findViewById(R.id.dsubmitRecharge);
        btnSubmitInword.setTypeface(font);
        btnSubmitInword.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " + getResources().getString(R.string.submit)));


        btnClear = (Button) findViewById(R.id.dmobCancel);
        btnClear.setTypeface(font);
        btnClear.setText(new SpannableStringBuilder(new String(new char[]{0xf021}) + " " + getResources().getString(R.string.clear)));


        btnCancle = (Button) findViewById(R.id.mCancel);
        btnCancle.setTypeface(font);
        btnCancle.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + "  " + getResources().getString(R.string.cancel)));


        edtFromDate = (EditText) findViewById(R.id.fromdateMytraction);
        edtToDate = (EditText) findViewById(R.id.todateMyTracton);

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
                        InWordActivity.this, AlertDialog.BUTTON_POSITIVE, new DatePickerDialog.OnDateSetListener() {

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
                        InWordActivity.this, AlertDialog.BUTTON_POSITIVE, new DatePickerDialog.OnDateSetListener() {

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
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(InWordActivity.this, TechnicalSupportActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnSubmitInword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InWordActivity.this, InWordInformationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        Spinner spnStore = (Spinner) findViewById(R.id.spnStore);
        Spinner spnSeller = (Spinner) findViewById(R.id.spnSeller);
        Spinner spnStatus = (Spinner) findViewById(R.id.spnStatus);

        // Spinner Drop down elements
        List<String> Store = new ArrayList<String>();
        Store.add("Jaipur");
        Store.add("Mumbai");
        Store.add("MP");
        Store.add("UP");
        Store.add("Delhi");
        ArrayAdapter<String> dataAdapterStore = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Store);
        dataAdapterStore.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnStore.setAdapter(dataAdapterStore);

        List<String> seller = new ArrayList<String>();
        seller.add("Jio");
        seller.add("Vodafone");
        seller.add("Airtel");
        ArrayAdapter<String> dataAdapterseller = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, seller);
        dataAdapterseller.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSeller.setAdapter(dataAdapterseller);

        List<String> status = new ArrayList<String>();
        status.add("Active");
        status.add("Deactive");

        ArrayAdapter<String> dataAdapterstatus = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, status);
        dataAdapterstatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnStatus.setAdapter(dataAdapterstatus);
    }

    public void onBackPressed() {

        Intent intent = new Intent(InWordActivity.this, TechnicalSupportActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(InWordActivity.this, TechnicalSupportActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();


                break;
        }
        return true;
    }

}
