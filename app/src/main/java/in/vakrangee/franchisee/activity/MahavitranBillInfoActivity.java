package in.vakrangee.franchisee.activity;

import android.app.Activity;
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
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.utils.Logger;
import in.vakrangee.supercore.franchisee.webservice.WebService;

//

public class MahavitranBillInfoActivity extends AppCompatActivity {

    private static final String TAG = MahavitranBillInfoActivity.class.getCanonicalName();

    Toolbar toolbar;
    Button btnBack, btnpaidBill;
    TextView ConsumerName, PC, BillingUnit, ConsumerNumber, BillMonth, BillNumber, BillPeriod, BillDate, DueDate, IncentiveDate,
            DisconnTag, DTCCode, BillAmount, IncentiveAmount, AmountafterdueDate;
    String name, pc, billAmount, billDate, billPeriod, dueDate, afterDueDateAmount, disconnTag, incentiveAmountt, incentiveDate,
            billMonth, billNo, dtcCode, billUnitName, consumerNo, billUnit, status;

    int amountToBePaid;
    TextView alreadyPaid;

    //String TAG = "Response";
    TelephonyManager telephonyManager;
    public static String diplayServerResopnse;
    TextView getBalance;
    TextView getResponse;
    ProgressDialog progress;
    ImageView imgAlreadyPaidBill;

    EditText edtPopupEmail, edtPopupMobileno;
    boolean valid = true;

    private AsyncSubmitPayBill asyncSubmitPayBill;
    private Logger logger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fontawesome-webfont.ttf");
        setContentView(R.layout.activity_mahavitran_bill_info);

        logger = Logger.getInstance(MahavitranBillInfoActivity.this);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setTypeface(font);
        btnBack.setText(new SpannableStringBuilder(new String(new char[]{0xf0a8}) + " " + getResources().getString(R.string.back)));
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);


        ConsumerName = (TextView) findViewById(R.id.ConsumerName);
        PC = (TextView) findViewById(R.id.PC);
        BillingUnit = (TextView) findViewById(R.id.BillingUnit);
        ConsumerNumber = (TextView) findViewById(R.id.ConsumerNumber);
        BillMonth = (TextView) findViewById(R.id.BillMonth);
        BillNumber = (TextView) findViewById(R.id.BillNumber);
        BillPeriod = (TextView) findViewById(R.id.BillPeriod);
        BillDate = (TextView) findViewById(R.id.BillDate);
        DueDate = (TextView) findViewById(R.id.DueDate);
        IncentiveDate = (TextView) findViewById(R.id.IncentiveDate);
        DisconnTag = (TextView) findViewById(R.id.DisconnTag);
        DTCCode = (TextView) findViewById(R.id.DTCCode);
        BillAmount = (TextView) findViewById(R.id.BillAmount);
        IncentiveAmount = (TextView) findViewById(R.id.IncentiveAmount);
        AmountafterdueDate = (TextView) findViewById(R.id.AmountafterdueDate);

        alreadyPaid = (TextView) findViewById(R.id.alreadyPaid);
        getResponse = (TextView) findViewById(R.id.getResponseMaha);


        btnpaidBill = (Button) findViewById(R.id.btnpaidBill);
        btnpaidBill.setTypeface(font);

        //imgSuccessfullBill =(ImageView)findViewById(R.id.successfullpaid);
        imgAlreadyPaidBill = (ImageView) findViewById(R.id.paidbill);


        try {

            String data = getIntent().getExtras().getString("second");
            String jsonStr = data;
            JSONObject myJsonObj = null;

            myJsonObj = new JSONObject(jsonStr);
            name = myJsonObj.getString("name");
            pc = myJsonObj.getString("pc");
            billAmount = myJsonObj.getString("billAmount");
            billDate = myJsonObj.getString("billDate");
            billPeriod = myJsonObj.getString("billPeriod");
            dueDate = myJsonObj.getString("dueDate");
            afterDueDateAmount = myJsonObj.getString("afterDueDateAmount");
            disconnTag = myJsonObj.getString("disconnTag");
            incentiveAmountt = myJsonObj.getString("incentiveAmountt");
            incentiveDate = myJsonObj.getString("incentiveDate");
            billMonth = myJsonObj.getString("billMonth");
            billNo = myJsonObj.getString("billNo");
            dtcCode = myJsonObj.getString("dtcCode");
            consumerNo = myJsonObj.getString("consumerNo");
            billUnit = myJsonObj.getString("billUnit");
            billUnitName = myJsonObj.getString("billUnitName");

            status = myJsonObj.getString("status");
            amountToBePaid = Integer.parseInt(myJsonObj.getString("amountToBePaid"));


            ConsumerName.setText(name);
            BillingUnit.setText(billUnit + "-" + billUnitName);
            ConsumerNumber.setText(consumerNo);
            BillMonth.setText(billMonth);
            BillNumber.setText(billNo);
            BillPeriod.setText(billPeriod);
            BillDate.setText(billDate);
            IncentiveDate.setText(incentiveDate);
            DueDate.setText(dueDate);
            DisconnTag.setText(disconnTag);
            PC.setText(pc);
            DTCCode.setText(dtcCode);
            BillAmount.setText(billAmount);
            IncentiveAmount.setText(incentiveAmountt);
            AmountafterdueDate.setText(afterDueDateAmount);


            btnpaidBill.setText(new SpannableStringBuilder(new String(new char[]{0xf058}) + " " + getResources().getString(R.string.payBill) + " " + amountToBePaid));

            Log.e("mainactivity Reason ", status);
            if (status.equals("N") || amountToBePaid <= 0) {
                // imgSuccessfullBill.setVisibility(View.INVISIBLE);
                alreadyPaid.setVisibility(View.VISIBLE);
                imgAlreadyPaidBill.setVisibility(View.VISIBLE);
                btnpaidBill.setVisibility(View.INVISIBLE);

            } else {
                alreadyPaid.setVisibility(View.INVISIBLE);
                //imgSuccessfullBill.setVisibility(View.INVISIBLE);
                imgAlreadyPaidBill.setVisibility(View.INVISIBLE);
                btnpaidBill.setVisibility(View.VISIBLE);

            }

        } catch (JSONException e) {
            e.printStackTrace();
            AlertDialogBoxInfo.alertDialogShow(MahavitranBillInfoActivity.this, getResources().getString(R.string.Warning));
        }


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // onBackPressed();
                Intent intent = new Intent(MahavitranBillInfoActivity.this, MahavitranActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.onlineBillInfo);

//            final Drawable upArrow = getResources().getDrawable(R.drawable.back);
//            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//            getSupportActionBar().setHomeAsUpIndicator(upArrow);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        btnpaidBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {


                    final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                    //create builder
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MahavitranBillInfoActivity.this);
                    //inflate layout from xml. you must create an xml layout file in res/layout first
                    LayoutInflater inflater = MahavitranBillInfoActivity.this.getLayoutInflater();
                    View layout = inflater.inflate(R.layout.popupmahavitranbillpayment, null);
                    builder.setView(layout);

                    TextView dthvertify = (TextView) layout.findViewById(R.id.dthverify);
                    dthvertify.setTypeface(Typeface.SANS_SERIF);

                    TextView consumername = (TextView) layout.findViewById(R.id.mahavirtranname);
                    consumername.setText(String.valueOf(name));
                    TextView mobilenumber = (TextView) layout.findViewById(R.id.numbermahavirtran);
                    mobilenumber.setText(String.valueOf(consumerNo));
                    TextView serviceprovidername = (TextView) layout.findViewById(R.id.amountmahavirtran);
                    serviceprovidername.setText(String.valueOf(amountToBePaid));


                    edtPopupEmail = (EditText) layout.findViewById(R.id.popupemail);

                    edtPopupMobileno = (EditText) layout.findViewById(R.id.popupmobileno);

               /*= if (!enterEmail.matches(emailPattern)) {
                    popupemail.setError(getResources().getString(R.string.invalidEmail));
                    popupemail.setTextColor(Color.parseColor("#000000"));
                    valid = false;

                }
*/
                    builder.setPositiveButton(android.R.string.ok, null);//init button
                    final AlertDialog d = builder.create();//store reference to the dialog
//the click listener for your button
                    final View.OnClickListener myListener = new View.OnClickListener() {
                        public void onClick(View v) {
                            if (edtPopupMobileno.getText().toString().trim().length() == 0) {
                                progress = new ProgressDialog(MahavitranBillInfoActivity.this);
                                progress.setTitle(R.string.transcationprogress);
                                progress.setMessage(getResources().getString(R.string.pleaseWait));
                                progress.setCancelable(false);
                                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                progress.show();

                                asyncSubmitPayBill = new AsyncSubmitPayBill();
                                asyncSubmitPayBill.execute();
                                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(edtPopupMobileno.getWindowToken(), 0);

                                d.dismiss();

                            } else if (edtPopupMobileno.getText().toString().trim().length() >= 1 && edtPopupMobileno.getText().toString().trim().length() <= 9) {
                                edtPopupMobileno.setError(getString(R.string.enter10DigitesNumber));
                            } else if (edtPopupEmail.getText().toString().trim().length() >= 1 && !edtPopupEmail.getText().toString().matches(emailPattern)) {
                                edtPopupEmail.setError(getString(R.string.invalidEmail));
                            } else {
                                //some logic

                                progress = new ProgressDialog(MahavitranBillInfoActivity.this);
                                progress.setTitle(R.string.transcationprogress);
                                progress.setMessage(getResources().getString(R.string.pleaseWait));
                                progress.setCancelable(false);
                                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                progress.show();

                                asyncSubmitPayBill = new AsyncSubmitPayBill();
                                asyncSubmitPayBill.execute();
                                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(edtPopupMobileno.getWindowToken(), 0);

                                d.dismiss();
                            }
                        }
                    };


                    d.setOnShowListener(new DialogInterface.OnShowListener() {

                        public void onShow(DialogInterface dialog) {
                            //here get the Button and set onclicklistener
                            Button b = d.getButton(DialogInterface.BUTTON_POSITIVE);
                            b.setOnClickListener(myListener);//your validation in your onclicklistener.
                            //do not forget to dismiss the dialon in your View.OnClickListener
                        }

                    });
                    d.show();


//                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//
//                        progress = new ProgressDialog(MahavitranBillInfoActivity.this);
//                        progress.setTitle(R.string.transcationprogress);
//                        progress.setMessage(getResources().getString(R.string.pleaseWait));
//                        progress.setCancelable(false);
//                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                        progress.show();
//
//                        AsyncSubmitPayBill myRequest = new AsyncSubmitPayBill();
//                        myRequest.execute();
//                        // do something else..
//
//
//                    }
//                });
//
//                builder.setNegativeButton("Cancel  ", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//
//
//                builder.show();

                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(MahavitranBillInfoActivity.this, MahavitranActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

                break;
        }
        return true;
    }

    public void onBackPressed() {

        //  super.onBackPressed();

        Intent intent = new Intent(MahavitranBillInfoActivity.this, MahavitranActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private class AsyncSubmitPayBill extends AsyncTask<Void, Void, Void> {
        String m = edtPopupMobileno.getText().toString().trim();
        String e = edtPopupEmail.getText().toString().trim();
        //this is the method to query

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {

                Connection connection = new Connection(MahavitranBillInfoActivity.this);
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

                String billingUnit = EncryptionUtil.encryptString(billUnit, getApplicationContext());
                String consumerNumber = EncryptionUtil.encryptString(consumerNo, getApplicationContext());


                Log.e(TAG + "Error in Server", m);

                String consumerMobileNumber = EncryptionUtil.encryptString(m, getApplicationContext());
                String consumerEmailId = EncryptionUtil.encryptString(e, getApplicationContext());

                diplayServerResopnse = WebService.payBillMahavirtan(vkidd, TokenId, imei, deviceid, simserialnumber, billingUnit, consumerNumber,
                        consumerMobileNumber, consumerEmailId);


                Log.d(TAG, "WebSer...ceResponse: " + diplayServerResopnse);

            } catch (Exception e) {
                e.printStackTrace();
                logger.writeError(TAG, "Error in MahavitranBill: " + e.toString());
                //AlertDialogBoxInfo.alertDialogShow(MahavitranBillInfoActivity.this, getResources().getString(R.string.Warning));
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");

            progress.dismiss();
            // progress.dismiss();
            try {


                if (diplayServerResopnse.equals("Transaction Successful.")) {
                    getResponse.setText(diplayServerResopnse);
                    getResponse.setTextColor(Color.GREEN);
                    getResponse.setTextColor(Color.parseColor("#468847"));

                    // imgSuccessfullBill.setVisibility(View.VISIBLE);
                    btnpaidBill.setVisibility(View.INVISIBLE);
                    final AlertDialog alertDialog = new AlertDialog.Builder(MahavitranBillInfoActivity.this).create();

                    alertDialog.setMessage(getResources().getString(R.string.transcationSuccessfull));
                    alertDialog.setCancelable(false);
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MahavitranBillInfoActivity.this, MahavitranActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    });
                    alertDialog.show();
                    TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
                    textView.setTextSize(12);


                } else {
                    getResponse.setText(diplayServerResopnse);
                    getResponse.setTextColor(Color.RED);

                }


                if (diplayServerResopnse == null) {

                    String message = null;
                    Log.i("TAG", ((message == null) ? "string null" : message));
                    AlertDialogBoxInfo.alertDialogShow(MahavitranBillInfoActivity.this, getResources().getString(R.string.unableToConnectVkms));

                    //  Log.e(TAG + "Please Null error", diplayServerResopnse);


                } else {
                    Log.e(TAG + "Error in Server", diplayServerResopnse);
                    // Toast.makeText(getApplicationContext(), "Error OTP ", Toast.LENGTH_SHORT).show();

                }
            } catch (Exception e) {
                AlertDialogBoxInfo.alertDialogShow(MahavitranBillInfoActivity.this, getResources().getString(R.string.Warning));
                e.printStackTrace();
            }

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
