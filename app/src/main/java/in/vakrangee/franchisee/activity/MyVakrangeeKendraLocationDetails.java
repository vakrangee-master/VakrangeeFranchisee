package in.vakrangee.franchisee.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.StringTokenizer;

import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.adapter.AdapterRecyclerViewVakrangeeKendraLocationDetails;
import in.vakrangee.franchisee.task.AsyncgetLocationDetails;
import in.vakrangee.supercore.franchisee.ifc.OnTaskCompleted;
import in.vakrangee.supercore.franchisee.model.LocationKendraDataModel;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;

public class MyVakrangeeKendraLocationDetails extends AppCompatActivity implements OnTaskCompleted {


    Toolbar toolbar;
    ProgressDialog progress;
    TelephonyManager telephonyManager;


    InternetConnection internetConnection;
    private RecyclerView recyclerView;
    private AdapterRecyclerViewVakrangeeKendraLocationDetails mAdapter;
    ListView listView;
    private TextView emptyView;
    ArrayList<LocationKendraDataModel> dataModels;
    TextView txtSH, txtDL, txtDE, txtBe, txtbelast;
    String getVkid, getTokenId, getimei, getdeviceid, getsimserialnumber;
    private ArrayList<LocationKendraDataModel> dataSet;
    String string1, string2, string3;
    private String[] myVakrangeeKendraLocations = new String[5];
    AsyncgetLocationDetails asyncTask = new AsyncgetLocationDetails(MyVakrangeeKendraLocationDetails.this, myVakrangeeKendraLocations);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vakrangee_kendra_location_details);

        toolbar = (Toolbar) findViewById(R.id.toolbarhlep);
        setSupportActionBar(toolbar);
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        if (getSupportActionBar() != null) {
            toolbar.setTitleTextColor(Color.WHITE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.myVakrangeeKendraLocationDetails);

//            final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
//            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//            getSupportActionBar().setHomeAsUpIndicator(upArrow);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        emptyView = (TextView) findViewById(R.id.empty_view);
        Connection connection = new Connection(getApplicationContext());

        // connection.openDatabase();
        getVkid = EncryptionUtil.encryptString(connection.getVkid(), getApplicationContext());
        getTokenId = EncryptionUtil.encryptString(connection.getTokenId(), getApplicationContext());

       final String deviceIdget = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        getdeviceid = EncryptionUtil.encryptString(deviceIdget, getApplicationContext());

        String deviceIDAndroid = CommonUtils.getAndroidUniqueID(getApplicationContext());
        getimei = EncryptionUtil.encryptString(deviceIDAndroid, getApplicationContext());

        String simSerial = CommonUtils.getSimSerialNumber(getApplicationContext());
        getsimserialnumber = EncryptionUtil.encryptString(simSerial, getApplicationContext());

        internetConnection = new InternetConnection(getApplicationContext());
        txtSH = (TextView) findViewById(R.id.txtname);
        txtDL = (TextView) findViewById(R.id.txtapi);
        txtDE = (TextView) findViewById(R.id.txtversion);
        txtBe = (TextView) findViewById(R.id.txtBe);
        txtbelast = (TextView) findViewById(R.id.txtbelast);

        if (internetConnection.isConnectingToInternet() == false) {

            AlertDialogBoxInfo.alertDialogShow(getApplicationContext(), getResources().getString(R.string.internetCheck));


        } else {
            String scope = EncryptionUtil.encryptString("", getApplicationContext());
            String locationCode = EncryptionUtil.encryptString("0", getApplicationContext());
            myVakrangeeKendraLocations[0] = "Default||0";
            new AsyncgetLocationDetails(MyVakrangeeKendraLocationDetails.this, myVakrangeeKendraLocations).execute(getVkid, getTokenId, getimei, getdeviceid, getsimserialnumber, scope, locationCode);


        }

        asyncTask.delegate = this;

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_kendra_location);


        txtSH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myVakrangeeKendraLocations[1] = null;
                myVakrangeeKendraLocations[2] = null;
                myVakrangeeKendraLocations[3] = null;
                myVakrangeeKendraLocations[4] = null;

                String scope = EncryptionUtil.encryptString("", getApplicationContext());
                String locationCode = EncryptionUtil.encryptString("0", getApplicationContext());
                new AsyncgetLocationDetails(MyVakrangeeKendraLocationDetails.this, myVakrangeeKendraLocations).execute(getVkid,
                        getTokenId, getimei, getdeviceid, getsimserialnumber, scope, locationCode);


                System.out.println("Initialize SH" + myVakrangeeKendraLocations[4]);
            }
        });


        txtDL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(MyVakrangeeKendraLocationDetails.this, myVakrangeeKendraLocations[1], Toast.LENGTH_SHORT).show();

                System.out.println("Initialize DL" + myVakrangeeKendraLocations[1]);


                myVakrangeeKendraLocations[2] = null;
                myVakrangeeKendraLocations[3] = null;
                myVakrangeeKendraLocations[4] = null;


                StringTokenizer st1 = new StringTokenizer(myVakrangeeKendraLocations[1], "|");
                String name1 = st1.nextToken();
                String sco1 = st1.nextToken();
                String ty1 = st1.nextToken();
                String scope = EncryptionUtil.encryptString(sco1, getApplicationContext());
                String locationCode = EncryptionUtil.encryptString(ty1, getApplicationContext());
                new AsyncgetLocationDetails(MyVakrangeeKendraLocationDetails.this, myVakrangeeKendraLocations).execute(getVkid, getTokenId, getimei, getdeviceid, getsimserialnumber, scope, locationCode);


            }
        });
        txtDE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("Initialize DE" + myVakrangeeKendraLocations[2]);

                myVakrangeeKendraLocations[3] = null;
                myVakrangeeKendraLocations[4] = null;
                // Toast.makeText(MyVakrangeeKendraLocationDetails.this, myVakrangeeKendraLocations[2], Toast.LENGTH_SHORT).show();
                StringTokenizer st1 = new StringTokenizer(myVakrangeeKendraLocations[2], "|");
                String name1 = st1.nextToken();
                String sco1 = st1.nextToken();
                String ty1 = st1.nextToken();
                String scope = EncryptionUtil.encryptString(sco1, getApplicationContext());
                String locationCode = EncryptionUtil.encryptString(ty1, getApplicationContext());
                new AsyncgetLocationDetails(MyVakrangeeKendraLocationDetails.this, myVakrangeeKendraLocations).execute(getVkid,
                        getTokenId, getimei, getdeviceid, getsimserialnumber, scope, locationCode);


            }
        });
        txtBe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Initialize BE" + myVakrangeeKendraLocations[3]);

                myVakrangeeKendraLocations[4] = null;

                // Toast.makeText(MyVakrangeeKendraLocationDetails.this, myVakrangeeKendraLocations[3], Toast.LENGTH_SHORT).show();
                StringTokenizer st1 = new StringTokenizer(myVakrangeeKendraLocations[3], "|");
                String name1 = st1.nextToken();
                String sco1 = st1.nextToken();
                String ty1 = st1.nextToken();
                String scope = EncryptionUtil.encryptString(sco1, getApplicationContext());
                String locationCode = EncryptionUtil.encryptString(ty1, getApplicationContext());
                new AsyncgetLocationDetails(MyVakrangeeKendraLocationDetails.this, myVakrangeeKendraLocations).execute(getVkid,
                        getTokenId, getimei, getdeviceid, getsimserialnumber, scope, locationCode);


            }
        });

        txtbelast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("Initialize BE Final" + myVakrangeeKendraLocations[4]);

                StringTokenizer st1 = new StringTokenizer(myVakrangeeKendraLocations[4], "|");
                String name1 = st1.nextToken();
                String sco1 = st1.nextToken();
                String ty1 = st1.nextToken();
                String scope = EncryptionUtil.encryptString(sco1, getApplicationContext());
                String locationCode = EncryptionUtil.encryptString(ty1, getApplicationContext());

                new AsyncgetLocationDetails(MyVakrangeeKendraLocationDetails.this, myVakrangeeKendraLocations).execute(getVkid,
                        getTokenId, getimei, getdeviceid, getsimserialnumber, scope, locationCode);


            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.menu_main_serach, menu);
        // MenuItem searchItem = menu.findItem(R.id.action_search_main);


        return super.onCreateOptionsMenu(menu);
    }


    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {

        BackPressMethod();
    }

    private void BackPressMethod() {
        try {
//            String myVakrangeeKendraLocation = myVakrangeeKendraLocations[0];
//            String myVakrangeeKendraLocation1 = myVakrangeeKendraLocations[1];
//            String myVakrangeeKendraLocation2 = myVakrangeeKendraLocations[2];
//            String myVakrangeeKendraLocation3 = myVakrangeeKendraLocations[3];
//            String myVakrangeeKendraLocation4 = myVakrangeeKendraLocations[4];

            if (myVakrangeeKendraLocations[0] != null && myVakrangeeKendraLocations[1] != null
                    && myVakrangeeKendraLocations[2] != null && myVakrangeeKendraLocations[3] != null &&
                    myVakrangeeKendraLocations[4] != null) {


                StringTokenizer st1 = new StringTokenizer(myVakrangeeKendraLocations[0], "|");
                String name1 = st1.nextToken();

                StringTokenizer st2 = new StringTokenizer(myVakrangeeKendraLocations[1], "|");
                String name2 = st2.nextToken();

                StringTokenizer st3 = new StringTokenizer(myVakrangeeKendraLocations[2], "|");
                String name3 = st3.nextToken();

                StringTokenizer st4 = new StringTokenizer(myVakrangeeKendraLocations[3], "|");
                String name4 = st4.nextToken();
                String scope = st4.nextToken();
                String locationCode = st4.nextToken();

                String scope4 = EncryptionUtil.encryptString(scope, getApplicationContext());
                String locationCode4 = EncryptionUtil.encryptString(locationCode, getApplicationContext());

                StringTokenizer st5 = new StringTokenizer(myVakrangeeKendraLocations[4], "|");
                String name5 = st5.nextToken();


                // nameset4.setText(name1 + " > " + name2 + " > " + name3 + " > " + name4 + " > " + name5);

                System.out.println("Initialize 5" + myVakrangeeKendraLocations[4]);
                new AsyncgetLocationDetails(MyVakrangeeKendraLocationDetails.this, myVakrangeeKendraLocations).execute(getVkid,
                        getTokenId, getimei, getdeviceid, getsimserialnumber, scope4, locationCode4);
                myVakrangeeKendraLocations[4] = null;
                System.out.println("Initialize 5 after" + myVakrangeeKendraLocations[4]);
                //Second
            } else if (myVakrangeeKendraLocations[0] != null && myVakrangeeKendraLocations[1] != null
                    && myVakrangeeKendraLocations[2] != null && myVakrangeeKendraLocations[3] != null) {

                StringTokenizer st1 = new StringTokenizer(myVakrangeeKendraLocations[0], "|");
                String name1 = st1.nextToken();

                StringTokenizer st2 = new StringTokenizer(myVakrangeeKendraLocations[1], "|");
                String name2 = st2.nextToken();


                StringTokenizer st3 = new StringTokenizer(myVakrangeeKendraLocations[2], "|");
                String name3 = st3.nextToken();
                String scope = st3.nextToken();
                String locationCode = st3.nextToken();

                String scope3 = EncryptionUtil.encryptString(scope, getApplicationContext());
                String locationCode3 = EncryptionUtil.encryptString(locationCode, getApplicationContext());

                StringTokenizer st4 = new StringTokenizer(myVakrangeeKendraLocations[3], "|");
                String name4 = st4.nextToken();

                //nameset3.setText(name1 + " > " + name2 + " > " + name3 + " > " + name4);

                System.out.println("Initialize 4" + myVakrangeeKendraLocations[3]);
                new AsyncgetLocationDetails(MyVakrangeeKendraLocationDetails.this, myVakrangeeKendraLocations).execute(getVkid,
                        getTokenId, getimei, getdeviceid, getsimserialnumber, scope3, locationCode3);
                myVakrangeeKendraLocations[3] = null;
                myVakrangeeKendraLocations[4] = null;

                System.out.println("Initialize 4 after" + myVakrangeeKendraLocations[3]);

                //Second
            } else if (myVakrangeeKendraLocations[0] != null && myVakrangeeKendraLocations[1] != null
                    && myVakrangeeKendraLocations[2] != null) {

                StringTokenizer st1 = new StringTokenizer(myVakrangeeKendraLocations[0], "|");
                String name1 = st1.nextToken();

                StringTokenizer st2 = new StringTokenizer(myVakrangeeKendraLocations[1], "|");
                String name2 = st2.nextToken();
                String scope = st2.nextToken();
                String locationCode = st2.nextToken();

                String scope1 = EncryptionUtil.encryptString(scope, getApplicationContext());
                String locationCode1 = EncryptionUtil.encryptString(locationCode, getApplicationContext());

                StringTokenizer st3 = new StringTokenizer(myVakrangeeKendraLocations[2], "|");
                String name3 = st3.nextToken();

                // nameset2.setText(name1 + " > " + name2 + " > " + name3);

                System.out.println("Initialize 3" + myVakrangeeKendraLocations[2]);
                new AsyncgetLocationDetails(MyVakrangeeKendraLocationDetails.this, myVakrangeeKendraLocations).execute(getVkid,
                        getTokenId, getimei, getdeviceid, getsimserialnumber, scope1, locationCode1);
                myVakrangeeKendraLocations[2] = null;
                myVakrangeeKendraLocations[3] = null;
                myVakrangeeKendraLocations[4] = null;
                System.out.println("Initialize 3 after" + myVakrangeeKendraLocations[2]);
                //Second
            } else if (myVakrangeeKendraLocations[0] != null && myVakrangeeKendraLocations[1] != null) {
                StringTokenizer st1 = new StringTokenizer(myVakrangeeKendraLocations[0], "|");

                String scope = EncryptionUtil.encryptString("", getApplicationContext());
                String locationCode = EncryptionUtil.encryptString("0", getApplicationContext());

                StringTokenizer st2 = new StringTokenizer(myVakrangeeKendraLocations[1], "|");
                String name2 = st2.nextToken();

                new AsyncgetLocationDetails(MyVakrangeeKendraLocationDetails.this, myVakrangeeKendraLocations).execute(getVkid,
                        getTokenId, getimei, getdeviceid, getsimserialnumber, scope, locationCode);
                // nameset1.setText(name2 + " > ");
                System.out.println("Initialize 2" + myVakrangeeKendraLocations[1]);
                myVakrangeeKendraLocations[1] = null;
                myVakrangeeKendraLocations[2] = null;
                myVakrangeeKendraLocations[3] = null;
                myVakrangeeKendraLocations[4] = null;
                System.out.println("Initialize 2 after" + myVakrangeeKendraLocations[1]);
                //First
            } else if (myVakrangeeKendraLocations[0] != null) {
                StringTokenizer st1 = new StringTokenizer(myVakrangeeKendraLocations[0], "|");
                String name1 = st1.nextToken();
                System.out.println("Initialize 1" + myVakrangeeKendraLocations[0]);
                super.onBackPressed();
                Intent intent = new Intent(MyVakrangeeKendraLocationDetails.this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }


        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }

    }

//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = new Intent(MyVakrangeeKendraLocationDetails.this, DashboardActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//        finish();
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                BackPressMethod();
                // Intent intent = new Intent(MyVakrangeeKendraLocationDetails.this, DashboardActivity.class);
                // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // startActivity(intent);
                //   finish();


                break;
        }
        return true;
    }


    @Override
    public void processFinish(String result) {

        try {
            dataModels = new ArrayList<>();


            StringTokenizer tokens = new StringTokenizer(result, "|");
            String first = tokens.nextToken();
            String second = tokens.nextToken();
            emptyView = (TextView) findViewById(R.id.empty_view_location);
            if (second.equals("No data found.")) {

                emptyView.setVisibility(View.VISIBLE);
            } else {
                String third = tokens.nextToken();
                String strJsona = third;
                String strJsonaaa = strJsona.replace("\r\n", "");

                try {


                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonarray = new JSONArray(strJsonaaa);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);


                        String idtype = jsonobject.getString("myModelObject1");
                        String name = jsonobject.getString("myModelObject2");
                        dataModels.add(new LocationKendraDataModel(idtype, name, second));
                        System.out.println("My Model" + idtype + name);

                    }
                    // Setup and Handover data to recyclerview

                    mAdapter = new AdapterRecyclerViewVakrangeeKendraLocationDetails(getApplicationContext(), dataModels,
                            txtSH, txtDL, txtDE, txtBe, txtbelast, myVakrangeeKendraLocations);
                    recyclerView.setAdapter(mAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    if (dataModels.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        emptyView.setVisibility(View.GONE);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Error Json", e.getMessage());
                    // AlertDialogBoxInfo.alertDialogShow(MyVakrangeeKendraLocationDetails.this, getResources().getString(R.string.Warning));

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
            //Toast.makeText(MyVakrangeeKendraLocationDetails.this,"MY Vakrnagee Location",Toast.LENGTH_SHORT).show();
            AlertDialogBoxInfo.alertDialogShow(MyVakrangeeKendraLocationDetails.this, getResources().getString(R.string.Warning));
        }


    }


}
