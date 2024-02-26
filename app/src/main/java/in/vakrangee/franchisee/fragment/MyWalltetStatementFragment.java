package in.vakrangee.franchisee.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
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

/**
 * Created by Nileshd on 6/17/2016.
 */
public class MyWalltetStatementFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    View view;
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
    private Activity mActivity;
    Connection connection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fontawesome-webfont.ttf");
        View view = inflater.inflate(R.layout.fragment_wallet_stmt, container, false);


        telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);

        btnAccountStmt = (Button) view.findViewById(R.id.walletmaccountStmt);
        btnAccountStmt.setTypeface(font);

        btnAccountStmt.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " + getResources().getString(R.string.go)));
        emptyView = (TextView) view.findViewById(R.id.empty_view);
        edtFromDate = (EditText) view.findViewById(R.id.ffromdate);
        edtToDate = (EditText) view.findViewById(R.id.todate);
        spnAccountStatement = (Spinner) view.findViewById(R.id.AccountStatement);
        //  telephonyManager = (TelephonyManager)view.getSystemService(Context.TELEPHONY_SERVICE);

        internetConnection = new InternetConnection(getActivity());
        btnAccountStmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "button click", Toast.LENGTH_LONG).show();
            }
        });

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
                        getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK, new DatePickerDialog.OnDateSetListener() {

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
                        getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK, new DatePickerDialog.OnDateSetListener() {

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
        spnAccountStatement.setOnItemSelectedListener(this);
        loadSpinner();

        btnAccountStmt.setOnClickListener(new View.OnClickListener() {
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
                boolean both = isDateAfter(from, to);

                if (edtFromDate.length() == 0) {
                    edtFromDate.setError(getResources().getString(R.string.enterfromdate));
                } else if (f == false) {
                    Toast.makeText(getActivity(), from + " is greater than Current Date   " + datetime, Toast.LENGTH_SHORT).show();
                    edtFromDate.setError(getResources().getString(R.string.selectproperdate));
                } else if (edtToDate.length() == 0) {
                    edtToDate.setError(getResources().getString(R.string.entertodate));
                } else if (t == false) {
                    edtToDate.setError(getResources().getString(R.string.selectproperdate));
                    Toast.makeText(getActivity(), to + " is greater than Current Date " + datetime, Toast.LENGTH_SHORT).show();
                } else if (both == false) {
                    edtToDate.setError(getResources().getString(R.string.selectproperdate));
                    edtFromDate.setError(getResources().getString(R.string.selectproperdate));
                    Toast.makeText(getActivity(), "  End date cannot be less than Start Date.  ", Toast.LENGTH_SHORT).show();
                } else if (internetConnection.isConnectingToInternet() == false) {

                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.internetCheck));


                } else {

                    edtFromDate.setError(null);
                    edtToDate.setError(null);


                    progress = new ProgressDialog(getActivity());
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


        return view;


    }

    public static boolean isDateAfter(String startDate, String endDate) {
        {
            try {


                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat df = new SimpleDateFormat(myFormat, Locale.US);


                Date date1 = df.parse(endDate);
                Date startingDate = df.parse(startDate);

                if (date1.after(startingDate)) {
                    return true;
                } else if (date1.equals(startingDate)) {
                    return true;
                } else if (startingDate.after(date1)) {
                    return false;
                } else
                    return false;
            } catch (Exception e) {

                return false;
            }
        }
    }


    private void loadSpinner() {
        Connection connection = new Connection(getActivity());
        //connection.openDatabase();
        ServiceProviderAdapter serviceProviderAdapter;
        List<ServiceProvider> serviceProvider = new ServiceProviderImpl().getAccountStatement();
        serviceProviderAdapter = new ServiceProviderAdapter(getActivity(),
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

                connection = new Connection(getActivity());
                String vkid = connection.getVkid();
                String tokenId = connection.getTokenId();
                int val = mserviceProvider.getServiceId();
                // int val = mServiceProvider.getServiceId();


                String vkidd = EncryptionUtil.encryptString(vkid, getActivity());
                String TokenId = EncryptionUtil.encryptString(tokenId, getActivity());
                String deviceIdget = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
                String deviceid = EncryptionUtil.encryptString(deviceIdget, getActivity());

                String deviceIDAndroid = CommonUtils.getAndroidUniqueID(getActivity());
                String imei = EncryptionUtil.encryptString(deviceIDAndroid, getActivity());

                String simSerial = CommonUtils.getSimSerialNumber(getActivity());
                String simserialnumber = EncryptionUtil.encryptString(simSerial, getActivity());

                String fromDate = EncryptionUtil.encryptString(fromdate, getActivity());
                String toDate = EncryptionUtil.encryptString(todate, getActivity());
                String ServiceProvider = EncryptionUtil.encryptString(String.valueOf(val), getActivity());


                diplayServerResopnse = WebService.getAccountStatement(vkidd, TokenId, imei, deviceid, simserialnumber, fromDate, toDate, ServiceProvider);

                Log.d(TAG, "WebSer...ceResponse: " + diplayServerResopnse);

            } catch (Exception e) {
                e.printStackTrace();
                AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
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
                        String myModelObject8 = jsonobject.getString("myModelObject8");
                        String myModelObject9 = jsonobject.getString("myModelObject9");
                        String myModelObject10 = jsonobject.getString("myModelObject10");
                        data.add(constants);


                    }

                    // Setup and Handover data to recyclerview
                    recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view);
                    mAdapter = new AdapterRecyclerViewWalletStmt(getActivity(), data);
                    recyclerView.setAdapter(mAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));

                }


            } catch (Exception e) {
                e.printStackTrace();
                AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));

            }

        }


    }

}
