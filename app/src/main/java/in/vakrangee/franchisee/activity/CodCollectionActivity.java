package in.vakrangee.franchisee.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;

import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;

//

public class CodCollectionActivity extends AppCompatActivity {
    private String responsereturn;
    SoapObject response = null;
    String TAG = "Response";

    Button submitrecharge, cancel;
    EditText mobilerecharge, amount;
    Toolbar toolbar;
    SoapPrimitive resultString;

    ProgressDialog progress;

    String spineervlaues;
    InternetConnection internetConnection;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    NiftyDialogBuilder materialDesignAnimatedDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fontawesome-webfont.ttf");

        setContentView(R.layout.activtiy_codcollection);

        submitrecharge = (Button) findViewById(R.id.csubmitRecharge);
        submitrecharge.setTypeface(font);
        submitrecharge.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " + getResources().getString(R.string.submit)));


        cancel = (Button) findViewById(R.id.cmobCancel);
        cancel.setTypeface(font);
        cancel.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + " " + getResources().getString(R.string.cancel)));


        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        mobilerecharge = (EditText) findViewById(R.id.cmobilenumber);
        amount = (EditText) findViewById(R.id.cmobAmount);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("COD Collection");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

//            final Drawable upArrow = getResources().getDrawable(R.drawable.back);
//            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//            getSupportActionBar().setHomeAsUpIndicator(upArrow);

        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mobilerecharge.setText("");
                amount.setText("");
                amount.setError(null);
                mobilerecharge.setError(null);
            }
        });

        internetConnection = new InternetConnection(CodCollectionActivity.this);

        submitrecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String mob = mobilerecharge.getText().toString();
                final String amo = amount.getText().toString();
                if (mob.length() != 10) {
                    mobilerecharge.setError("Enter 10 digits Number");

                } else if (amo.length() <= 1) {
                    amount.setError("Minimum 2 Digits  Amount number");
                } else if (internetConnection.isConnectingToInternet() == false) {
                    Toast.makeText(CodCollectionActivity.this, "No Internet Connection  You don't have internet connection",
                            Toast.LENGTH_SHORT).show();
                } else {

                    //create builder
                    final AlertDialog.Builder builder = new AlertDialog.Builder(CodCollectionActivity.this);
                    //inflate layout from xml. you must create an xml layout file in res/layout first
                    LayoutInflater inflater = CodCollectionActivity.this.getLayoutInflater();
                    View layout = inflater.inflate(R.layout.popupmobile, null);
                    builder.setView(layout);
                    TextView serviceproide = (TextView) layout.findViewById(R.id.amount);
                    serviceproide.setText("\u20B9 " + amount.getText().toString());
                    TextView mobilenumber = (TextView) layout.findViewById(R.id.mobilenumber);
                    mobilenumber.setText(mobilerecharge.getText().toString());


                    builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            progress = new ProgressDialog(CodCollectionActivity.this);
                            progress.setTitle("Please Wait!!");
                            progress.setMessage("Wait!!");
                            progress.setCancelable(true);
                            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            progress.show();
                            AsyncCallWS myRequest = new AsyncCallWS();
                            myRequest.execute();

                            // Toast.makeText(MobileRechargActivity.this, " Succesfull", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                        }
                    });

                    builder.setNegativeButton("Cancel  ", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builder.show();

//                    AlertDialog alert = builder.create();
//                    alert.show();
//                    Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
//                    nbutton.setBackgroundColor(Color.rgb(212, 117, 0));
//
//                    Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
//                    pbutton.setBackgroundColor(Color.rgb(19, 54, 92));


                }
            }
        });

        mobilerecharge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i != 10) {
                    // Toast.makeText(getApplicationContext(), "Please Enter 10 digits", Toast.LENGTH_SHORT).show();
                    mobilerecharge.setTextColor(Color.parseColor("#000000"));
                    mobilerecharge.setError("Enter 10 digits Number");

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {


                if (mobilerecharge.length() <= 9) {


                } else {
                    mobilerecharge.setTextColor(Color.parseColor("#468847"));
                    mobilerecharge.setError(null);

                }
            }
        });

        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i != 2) {
                    // Toast.makeText(getApplicationContext(), "Please Enter 10 digits", Toast.LENGTH_SHORT).show();
                    amount.setTextColor(Color.parseColor("#000000"));
                    amount.setError("Minimum 2 Digits  Amount number");

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (amount.length() <= 1) {


                } else {
                    amount.setTextColor(Color.parseColor("#468847"));
                    amount.setError(null);

                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(CodCollectionActivity.this, DashboardActivity.class);
                startActivity(intent);


                break;
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "MobileRechargActivity Page", // TODO: Define a title for the contentOpen shown.
                // TODO: If you have web page contentOpen that matches this app activity's contentOpen,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://in.vakrangee.vkmsfinal/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "MobileRechargActivity Page", // TODO: Define a title for the contentOpen shown.
                // TODO: If you have web page contentOpen that matches this app activity's contentOpen,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://in.vakrangee.vkmsfinal/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            calculate();


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            //Toast.makeText(CodCollectionActivity.this, "Response" + resultString, Toast.LENGTH_LONG).show();

            progress.dismiss();


            mobilerecharge.setText("");
            amount.setText("");
            amount.setError(null);
            mobilerecharge.setError(null);
        }

    }

    private void calculate() {


        try {

//

            HttpTransportSE androidHttpTransport = new HttpTransportSE(Constants.URL_CYBER_PLAT);

            SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.METHOD_NAME);
            String mob = mobilerecharge.getText().toString();
            String amo = amount.getText().toString();

            request.addProperty("securitycode", "12345");
            request.addProperty("transid", "1211ABC");
            request.addProperty("vk_id", "VL0003167");
            request.addProperty("password", "pass@123");
            request.addProperty("agentid", "MOB03167");
            request.addProperty("service_provider", "service imagetype spinner");
            request.addProperty("recharge_type", "ETOPUP");
            request.addProperty("mobile_number", mob);
            request.addProperty("edtEnterAmount", amo);
            request.addProperty("parameterList", "vakrangee");

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(Constants.URL_CYBER_PLAT);

            transport.call(Constants.SOAP_ACTION, soapEnvelope);
            resultString = (SoapPrimitive) soapEnvelope.getResponse();

            if (resultString == null) {
                Log.i(TAG, "No Error");
            } else {

                Log.i(TAG, "Result Celsius: " + resultString.toString());
            }
        } catch (IOException i) {
            i.printStackTrace();
        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getMessage());
        }


    }


}
