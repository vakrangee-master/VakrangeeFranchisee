package in.vakrangee.franchisee.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.webservice.WebService;

import static in.vakrangee.supercore.franchisee.utils.Constants.mgetData;

//

/**
 * Created by Nileshd on 6/1/2017.
 */
public class MyATMAmountFragment extends Fragment {


    Context context;
    EditText edtDate;
    EditText edt200, edt500, edt100, edtremarks;
    TextView txt200, txt500, txt100, totalamount, txtaviable, getamountvalues, diffamounnt;
    private ViewPager viewPager;
    public static final String MyPREFERENCES = "MyPrefs";
    Button btnSubmitAmount;
    Bundle bundle;
    TextView typeA, typeB, typeC;
    private ArrayList<ATMDeomination> getDenomination;

    ATMDeomination deomination;

    String diplayServerResopnse_getDenomination;

    Long etKids;

    public MyATMAmountFragment(ViewPager viewPager) {
        this.viewPager = viewPager;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fontawesome-webfont.ttf");

        View rootView = inflater.inflate(R.layout.fragment_atm_amount_fill, container, false);
        totalamount = (TextView) rootView.findViewById(R.id.totalamount);
        getamountvalues = (TextView) rootView.findViewById(R.id.getamountvalues);
        getamountvalues.setText("0");

        btnSubmitAmount = (Button) rootView.findViewById(R.id.btnSubmitAmount);
        btnSubmitAmount.setVisibility(View.INVISIBLE);


        typeA = (TextView) rootView.findViewById(R.id.typea);
        typeB = (TextView) rootView.findViewById(R.id.typeb);
        typeC = (TextView) rootView.findViewById(R.id.typec);
        try {

            AsyncGetDenomination myRequest = new AsyncGetDenomination();
            myRequest.execute();


        } catch (Exception e) {
            e.getMessage();
        }
        bundle = getArguments();
        if (bundle != null) {
            String value = getArguments().getString("username");
            getamountvalues.setText(value);
            btnSubmitAmount.setVisibility(View.VISIBLE);


        }


        edtDate = (EditText) rootView.findViewById(R.id.edtDate);
        edtDate.setTypeface(Typeface.SANS_SERIF);

        edtremarks = (EditText) rootView.findViewById(R.id.edtremarks);
        edtremarks.setTypeface(Typeface.SANS_SERIF);

        edt200 = (EditText) rootView.findViewById(R.id.TotalAmt2000);
        edt200.setTypeface(Typeface.SANS_SERIF);


        edt500 = (EditText) rootView.findViewById(R.id.TotalAmt500);
        edt500.setTypeface(Typeface.SANS_SERIF);


        edt100 = (EditText) rootView.findViewById(R.id.TotalAmt100);
        edt100.setTypeface(Typeface.SANS_SERIF);


        edt200.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    edt200.setHint("");
                else
                    edt200.setHint("0");
            }
        });

        edt500.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    edt500.setHint("");
                else
                    edt500.setHint("0");
            }
        });

        edt100.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    edt100.setHint("");
                else
                    edt100.setHint("0");
            }
        });


        txt200 = (TextView) rootView.findViewById(R.id.txt200);
        txt500 = (TextView) rootView.findViewById(R.id.txt500);
        txt100 = (TextView) rootView.findViewById(R.id.txt100);
        txtaviable = (TextView) rootView.findViewById(R.id.txtaviable);
        diffamounnt = (TextView) rootView.findViewById(R.id.diffamounnt);

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat dfDate = new SimpleDateFormat(myFormat, Locale.US);
        Date nowDate = new Date(System.currentTimeMillis());
        String datetime = dfDate.format(nowDate);
        edtDate.setText(datetime);
        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date();
            }
        });
        TextwatcherFunction();
        edt200.setFilters(new InputFilter[]{new InputFilterMinMax("1", "2000")});
        edt500.setFilters(new InputFilter[]{new InputFilterMinMax("1", "2500")});
        edt100.setFilters(new InputFilter[]{new InputFilterMinMax("1", "5000")});
        btnSubmitAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    //mgetData.getAtmId();
                    //viewPager.setCurrentItem(2);

                    if (bundle != null) {

                        String diff = diffamounnt.getText().toString();
                        String getEdittetx = edtremarks.getText().toString();

                        String TypeA = String.valueOf(edt200.getText().toString().equals("") ? 0 : Integer.parseInt(edt200.getText().toString()));
                        String TypeB = String.valueOf(edt500.getText().toString().equals("") ? 0 : Integer.parseInt(edt500.getText().toString()));
                        String TypeC = String.valueOf(edt100.getText().toString().equals("") ? 0 : Integer.parseInt(edt100.getText().toString()));
                        int value = Integer.parseInt(diff);

                        String date1 = edtDate.getText().toString();
                        @SuppressLint("SimpleDateFormat")
                        Date initDate = new SimpleDateFormat("dd-MM-yyyy").parse(date1);
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        String parsedDate = formatter.format(initDate);

                        if (value < 0) {
                            // Toast.makeText(getActivity(), "Enter Remarks", Toast.LENGTH_SHORT).show();
                            AlertDialogBoxInfo.alertDialogShow(getActivity(), String.valueOf(value) + " is not allow");

//                            if (getEdittetx.length() < 4) {
//                                Toast.makeText(getActivity(), "Enter Remarks", Toast.LENGTH_SHORT).show();
//                                edtremarks.setError(getResources().getString(R.string.enter4CharacterName));
//
//                            } else {
//                                edtremarks.setError(null);
//                                MyATMPhysicalCashFragment fragment = new MyATMPhysicalCashFragment(viewPager);
//                                Bundle bundle = new Bundle();
//                                bundle.putString("edt200", TypeA);
//                                bundle.putString("edt500", TypeB);
//                                bundle.putString("edt100", TypeC);
//                                fragment.setArguments(bundle);
//                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frgATMPhysicalamount, fragment).commit();
//                                mgetData.setTypeA(TypeA);
//                                mgetData.setTypeB(TypeB);
//                                mgetData.setTypeC(TypeB);
//                                mgetData.setBalance(getamountvalues.toString());
//                                mgetData.setDifferenceRemarks(getamountvalues.toString());
//                                mgetData.setAtmRoCashLoadingId("0");
//                                mgetData.setBalance(diff);
//                                mgetData.setDifferenceRemarks(getEdittetx);
//                                mgetData.setRemarks("0");
//                                mgetData.setLoadingDate(edtDate.getText().toString());
//                                viewPager.setCurrentItem(2);
//
//                            }
                        } else if (value > 0) {

                            if (getEdittetx.length() < 4) {
                                Toast.makeText(getActivity(), "Enter Remarks", Toast.LENGTH_SHORT).show();
                                edtremarks.setError(getResources().getString(R.string.enter4CharacterName));

                            } else {
                                edtremarks.setError(null);
                                MyATMPhysicalCashFragment fragment = new MyATMPhysicalCashFragment(viewPager);
                                Bundle bundle = new Bundle();
                                bundle.putString("edt200", TypeA);
                                bundle.putString("edt500", TypeB);
                                bundle.putString("edt100", TypeC);
                                fragment.setArguments(bundle);
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frgATMPhysicalamount, fragment).commit();
                                mgetData.setTypeA(TypeA);
                                mgetData.setTypeB(TypeB);
                                mgetData.setTypeC(TypeC);
                                mgetData.setBalance(getamountvalues.getText().toString());
                                mgetData.setDifferenceRemarks(getamountvalues.getText().toString());
                                mgetData.setAtmRoCashLoadingId("0");
                                mgetData.setBalance(diff);
                                mgetData.setDifferenceRemarks(getEdittetx);
                                mgetData.setRemarks("0");
                                mgetData.setLoadingDate(parsedDate);
                                viewPager.setCurrentItem(2);
                            }
                        } else {
                            edtremarks.setError(null);
                            MyATMPhysicalCashFragment fragment = new MyATMPhysicalCashFragment(viewPager);
                            Bundle bundle = new Bundle();
                            bundle.putString("edt200", TypeA);
                            bundle.putString("edt500", TypeB);
                            bundle.putString("edt100", TypeC);
                            fragment.setArguments(bundle);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frgATMPhysicalamount, fragment).commit();
                            mgetData.setTypeA(TypeA);
                            mgetData.setTypeB(TypeB);
                            mgetData.setTypeC(TypeB);
                            mgetData.setBalance(getamountvalues.toString());
                            mgetData.setDifferenceRemarks(getamountvalues.toString());
                            mgetData.setAtmRoCashLoadingId("");
                            mgetData.setBalance(diff);
                            mgetData.setDifferenceRemarks(getEdittetx);
                            mgetData.setRemarks("");
                            mgetData.setLoadingDate(edtDate.getText().toString());
                            viewPager.setCurrentItem(2);

                        }
                    } else {
                        Toast.makeText(getActivity(), "please select ATM Ro", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {

                    Toast.makeText(getActivity(), "please Enter Cassette Value", Toast.LENGTH_SHORT).show();
                    e.getMessage();
                }
            }
        });


        return rootView;
    }

    private void TextwatcherFunction() {


        final ArrayList<EditText> editTexts = new ArrayList<>(); // Container list
        editTexts.add(edt200); // editTexts[0]
        editTexts.add(edt500); // editTexts[1]
        editTexts.add(edt100); // editTexts[2]
        for (final EditText editText : editTexts) { //need to be final for custom behaviors
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    totalamount.setText(addNumbers());
                    txtaviable.setText(getTotalNote());
                    diffamounnt.setText(differenceVal());


                }

                @Override
                public void afterTextChanged(Editable s) {
                    //Apply general behavior for all editTexts
                    if (editText == editTexts.get(0)) {
                        if (s.length() > 0) {
                            String e = edt200.getText().toString();
                            Long cst = Long.valueOf(e);
                            cst = cst * 2000;
                            txt200.setText(cst.toString());
                        } else {
                            txt200.setText("");
                        }
                    }
                    if (editText == editTexts.get(1)) {
                        if (s.length() > 0) {
                            String e = edt500.getText().toString();
                            Long cst = Long.valueOf(e);
                            cst = cst * 500;
                            txt500.setText(cst.toString());
                        } else {
                            txt500.setText("");
                        }
                    }
                    if (editText == editTexts.get(2)) {
                        if (s.length() > 0) {
                            String e = edt100.getText().toString();
                            Long cst = Long.valueOf(e);
                            cst = cst * 100;
                            txt100.setText(cst.toString());
                        } else {
                            txt100.setText("");
                        }

                    }

                }
            });

        }
    }

    private String differenceVal() {
        int number1;
        int number2;


        if (getamountvalues.getText().toString() != "" && getamountvalues.getText().length() > 0) {
            number1 = Integer.parseInt(getamountvalues.getText().toString());
        } else {
            number1 = Integer.valueOf(0);
        }

        if (totalamount.getText().toString() != "" && totalamount.getText().length() > 0) {
            number2 = Integer.parseInt(totalamount.getText().toString());
        } else {
            number2 = Integer.valueOf(0);
        }

        return Integer.toString((int) (number1 - number2));
    }


    private String getTotalNote() {
        Long number1;
        Long number2;
        Long number3;

        if (edt200.getText().toString() != "" && edt200.getText().length() > 0) {
            number1 = Long.parseLong(edt200.getText().toString());
        } else {
            number1 = Long.valueOf(0);
        }
        if (edt500.getText().toString() != "" && edt500.getText().length() > 0) {
            number2 = Long.parseLong(edt500.getText().toString());
        } else {
            number2 = Long.valueOf(0);
        }

        if (edt100.getText().toString() != "" && edt100.getText().length() > 0) {
            number3 = Long.parseLong(edt100.getText().toString());
        } else {
            number3 = Long.valueOf(0);
        }


        return Long.toString((int) (number1 + number2 + number3));
    }

    private String addNumbers() {

        Long number1;
        Long number2;
        Long number3;
        int G2000 = Integer.parseInt(typeA.getText().toString());
        int G500 = Integer.parseInt(typeB.getText().toString());
        int G100 = Integer.parseInt(typeC.getText().toString());

        if (edt200.getText().toString() != "" && edt200.getText().length() > 0) {
            number1 = Long.parseLong(edt200.getText().toString());
        } else {
            number1 = Long.valueOf(0);
        }
        if (edt500.getText().toString() != "" && edt500.getText().length() > 0) {
            number2 = Long.parseLong(edt500.getText().toString());
        } else {
            number2 = Long.valueOf(0);
        }

        if (edt100.getText().toString() != "" && edt100.getText().length() > 0) {
            number3 = Long.parseLong(edt100.getText().toString());
        } else {
            number3 = Long.valueOf(0);
        }


        return Long.toString((int) (number1 * G2000 + number2 * G500 + number3 * G100));
    }


    private void date() {
        final Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(), AlertDialog.BUTTON_POSITIVE, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mcurrentDate.set(Calendar.YEAR, year);
                mcurrentDate.set(Calendar.MONTH, monthOfYear);
                mcurrentDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "yyyy=M";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                edtDate.setText(sdf.format(mcurrentDate.getTime()));

            }
        }, mYear, mMonth, mDay);


        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }


    private class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            boolean isPM = (hourOfDay >= 12);
            //edtTime.setText(String.format("%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM"));

            //edtTime.setText( view.getCurrentHour()+ view.getCurrentMinute());


        }

    }

    public class InputFilterMinMax implements InputFilter {

        private int min, max;

        public InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public InputFilterMinMax(String min, String max) {
            this.min = Integer.parseInt(min);
            this.max = Integer.parseInt(max);
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                int input = Integer.parseInt(dest.toString() + source.toString());
                if (isInRange(min, max, input))

                    return null;
            } catch (NumberFormatException nfe) {
            }
            return "";
        }

        private boolean isInRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }

    private class AsyncGetDenomination extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(getActivity());


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String myFormat = "yyyy-MM-dd"; //In which you need put here
            SimpleDateFormat dfDate = new SimpleDateFormat(myFormat, Locale.US);
            Date nowDate = new Date(System.currentTimeMillis());
            String datetime = dfDate.format(nowDate);
            String date = datetime;

//

            diplayServerResopnse_getDenomination = WebService.getDenomination(date);
            Log.d("TAG", diplayServerResopnse_getDenomination);

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            try {

                pdLoading.dismiss();


                if (diplayServerResopnse_getDenomination.equals("Service from Vakrangee Kendra unavailable. Please try again later.")) {
                    Toast.makeText(getActivity(), diplayServerResopnse_getDenomination, Toast.LENGTH_LONG).show();
                } else if (diplayServerResopnse_getDenomination.equals("OKAY|empty")) {

                    Toast.makeText(getActivity(), diplayServerResopnse_getDenomination, Toast.LENGTH_LONG).show();
                } else {

                    StringTokenizer tokens = new StringTokenizer(diplayServerResopnse_getDenomination, "|");
                    String first = tokens.nextToken();
                    String second = tokens.nextToken();
                    getDenomination = new ArrayList<>();


                    JSONArray jArray = new JSONArray(second);


                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.getJSONObject(i);
                        deomination = new ATMDeomination();
                        deomination.setMyModelObject1(json_data.getString("myModelObject1"));
                        deomination.setMyModelObject2(json_data.getString("myModelObject2"));
                        deomination.setMyModelObject3(json_data.getString("myModelObject3"));
                        deomination.setMyModelObject4(json_data.getString("myModelObject4"));
                        deomination.setMyModelObject5(json_data.getString("myModelObject5"));
                        getDenomination.add(deomination);

                        if (deomination.getMyModelObject1().equals("1") && deomination.getMyModelObject1() != null) {
                            typeA.setText(deomination.getMyModelObject2());
                        } else {

                        }
                        if (deomination.getMyModelObject1().toString().equals("2") && deomination.getMyModelObject1().toString() != null) {
                            typeB.setText(deomination.getMyModelObject2());
                        } else {

                        }

                        if (deomination.getMyModelObject1().toString().equals("3") && deomination.getMyModelObject1().toString() != null) {
                            typeC.setText(deomination.getMyModelObject2());
                        } else {

                        }


                    }
                }

            } catch (JSONException e) {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }

}
