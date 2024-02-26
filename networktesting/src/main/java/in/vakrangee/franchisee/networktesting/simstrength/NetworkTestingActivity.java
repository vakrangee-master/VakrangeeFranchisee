package in.vakrangee.franchisee.networktesting.simstrength;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.vakrangee.franchisee.networktesting.R;
import in.vakrangee.franchisee.networktesting.speedtest.NetworkSpeedTestingActivity;
import in.vakrangee.supercore.franchisee.commongui.imagegallery.RecyclerViewClickListener;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.GPSTracker;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;

@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressLint("MissingPermission")
public class NetworkTestingActivity extends AppCompatActivity {

    private static final String TAG = "NetworkTestingActivity";
    private Context context;
    private Toolbar toolbar;
    private RecyclerView recyclerViewNetworkTesting;
    private SubscriptionManager localSubscriptionManager;
    private PermissionHandler permissionHandler;
    private SIMListAdapter simListAdapter;
    public static final int BAR_5_EXCELLENT = 5;
    public static final int BAR_4_VERY_GOOD = 4;
    public static final int BAR_3_GOOD = 3;
    public static final int BAR_2_FAIR = 2;
    public static final int BAR_1_POOR = 1;
    public static final int BAR_0_NO_SIGNAL = 0;
    private MaterialButton btnSpeedTest;
    private MaterialButton btnNetworkStrengthTest;
    private List<SIMDetailsDto> simDetailsList;
    private AsynSaveNetworkSIMDetailsData asynSaveNetworkSIMDetailsData = null;
    private AsyncGetSIMNetworkTestingConfigDetails asyncGetSIMNetworkTestingConfigDetails = null;
    private GPSTracker gpsTracker;
    private String latitude = "", longitude = "";
    private int lastX = 0;
    private int simCount;
    private String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_networktesting);

        //Initialize
        context = this;
        permissionHandler = new PermissionHandler(this);
        toolbar = (Toolbar) findViewById(R.id.toolbarNetworkTesting);
        gpsTracker = new GPSTracker(context);

        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null) {
            String title = getString(R.string.title_network_testing);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + title + "</small>"));
        }

        //Widgets
        btnSpeedTest = findViewById(R.id.btnSpeedTest);
        recyclerViewNetworkTesting = findViewById(R.id.recyclerViewNetworkTesting);

        localSubscriptionManager = SubscriptionManager.from(this);
        permissionHandler.requestMultiplePermission(recyclerViewNetworkTesting, permissions, getString(R.string.needs_phone_location_permission_msg), new IPermission() {

            @Override
            public void IsPermissionGranted(boolean IsGranted) {
                if (IsGranted) {
                    simDetailsList = getSIMDetailsList();
                    setSimListAdapter(simDetailsList);
                    setupChart();
                }
            }
        });
        btnSpeedTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NetworkTestingActivity.this, NetworkSpeedTestingActivity.class);
                startActivity(intent);
            }
        });

        btnNetworkStrengthTest = findViewById(R.id.btnNetworkStrengthTest);
        btnNetworkStrengthTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asyncGetSIMNetworkTestingConfigDetails = new AsyncGetSIMNetworkTestingConfigDetails(context, new AsyncGetSIMNetworkTestingConfigDetails.Callback() {
                    @Override
                    public void onResult(String result) {
                        handleResult(result);
                    }
                });
                asyncGetSIMNetworkTestingConfigDetails.execute("");
            }
        });

    }

    private void handleResult(String result) {
        try {

            if (TextUtils.isEmpty(result)) {
                AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                return;
            }

            // Handle Error Response From Server.
            if (result.startsWith("ERROR|")) {

                StringTokenizer tokens = new StringTokenizer(result, "|");
                tokens.nextToken();     // Jump to next Token
                String errMsg = tokens.hasMoreElements() ? tokens.nextToken() : context.getResources().getString(R.string.Warning);
                AlertDialogBoxInfo.alertDialogShow(context, errMsg);
                return;
            }

            //Process response
            if (result.startsWith("OKAY|")) {
                StringTokenizer st1 = new StringTokenizer(result, "|");
                st1.nextToken();
                String resultData = st1.nextToken();

                if (TextUtils.isEmpty(resultData)) {
                    AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                    return;
                }

                //Process Response
                Gson gson = new Gson();
                SIMNetworkConfigDto networkConfigDto = gson.fromJson(resultData, SIMNetworkConfigDto.class);
                getSIMStrengthValue(networkConfigDto);

            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
        }
    }

    private void setSimListAdapter(List<SIMDetailsDto> simDetailsList) {
        simListAdapter = new SIMListAdapter(context, simDetailsList, new RecyclerViewClickListener() {

            @Override
            public void onClick(View view, int position) {
                if (position != RecyclerView.NO_POSITION) {
                    SIMDetailsDto detailsDto = simDetailsList.get(position);
                    Toast.makeText(context, detailsDto.getSimSlotName(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(context, SIMDetailsActivity.class);
                    intent.putExtra("SIM_DETAILS_DTO", detailsDto);
                    startActivity(intent);

                }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerViewNetworkTesting.setLayoutManager(layoutManager);
        recyclerViewNetworkTesting.setItemAnimator(new DefaultItemAnimator());
        recyclerViewNetworkTesting.setAdapter(simListAdapter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        permissionHandler.dismissSnackbar();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<SIMDetailsDto> getSIMDetailsList() {

        List<SIMDetailsDto> simDetailsDtoList = new ArrayList<>();
        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        simCount = manager.getPhoneCount();

        for (int i = 0; i < simCount; i++) {
            SIMDetailsDto detailsDto = prepareSIMDetailsData(localSubscriptionManager, i);
            simDetailsDtoList.add(detailsDto);

            if (detailsDto.getSubscriptionId() != 0) {
                TelephonyManager simManager = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).createForSubscriptionId(detailsDto.getSubscriptionId());
                simManager.listen(new MyCustomPhoneStateListener(simManager, i, new MyCustomPhoneStateListener.IGetSignalStrength() {
                    @Override
                    public void onSignalStrengthChanged(TelephonyManager manager1, int pos, SignalStrength signalStrength) {

                        SIMDetailsDto detailsDto = simDetailsDtoList.get(pos);

                        List<CellInfo> cellInfoList = getAllRegisteredCellInfo(manager1);

                        if (cellInfoList.size() == 0) {
                            return;
                        }

                        int signalStrengthValue = getSignalStrengthValue(cellInfoList, detailsDto.getSimSlotIndex(), signalStrength);

                        detailsDto.setSignalStrength("" + signalStrengthValue + " dBM");
                        int bar = signalStrength.getLevel();
                        detailsDto.setSignalBar(bar);

                        if (detailsDto.isExecutionStarted() && !TextUtils.isEmpty(detailsDto.getSignalStrength())) {
                            detailsDto.signalStrengthValue.add(detailsDto.getSignalStrength());
                        }

                        String simSignalStrength = getSIMSignalStrengthWithQualityUsingLevel(bar, detailsDto.getSignalStrength());
                        detailsDto.setSimSignalStrength(simSignalStrength);
                        simDetailsDtoList.set(pos, detailsDto);
                        simListAdapter.notifyDataSetChanged();

                        //Notify Line Chart
                        addEntry(signalStrengthValue, detailsDto.getSimSlotIndex());

                    }

                }), PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
            }
        }

        return simDetailsDtoList;
    }

    private List<CellInfo> getAllRegisteredCellInfo(TelephonyManager manager1) {
        List<CellInfo> cellInfoList = new ArrayList<>();
        for (CellInfo info : manager1.getAllCellInfo()) {
            if (info.isRegistered()) {
                cellInfoList.add(info);
            }
        }
        return cellInfoList;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private SIMDetailsDto prepareSIMDetailsData(SubscriptionManager localSubscriptionManager, int simSlotIndex) {
        SIMDetailsDto simDetailsDto = new SIMDetailsDto();

        simDetailsDto.setSimSlotIndex(simSlotIndex);
        simDetailsDto.setSimSlotName("SIM " + (simSlotIndex + 1));
        if (localSubscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(simSlotIndex) == null) {
            return simDetailsDto;
        }

        int subscriptionId = localSubscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(simSlotIndex).getSubscriptionId();
        simDetailsDto.setSubscriptionId(subscriptionId);
        TelephonyManager manager = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).createForSubscriptionId(subscriptionId);
        simDetailsDto = prepareOtherDataUsingTelephonyManager(manager, simDetailsDto);
        return simDetailsDto;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private SIMDetailsDto prepareOtherDataUsingTelephonyManager(TelephonyManager telephonyManager, SIMDetailsDto simDetailsDto) {

        try {

            //Network
            simDetailsDto.setNetwork(telephonyManager.getNetworkOperatorName());

            //Phone Number
            simDetailsDto.setLineNumber(telephonyManager.getLine1Number());

            //Mobile Network State
            int dataState = telephonyManager.getDataState();
            String networkState = dataState == TelephonyManager.DATA_DISCONNECTED ? "Disconnected" : "Connected";
            simDetailsDto.setMobNetworkState(networkState);

            //Operator Info
            simDetailsDto.setOperatorInfo(telephonyManager.getSimOperatorName());

            //Service Status
            String serviceState = getServiceState(telephonyManager.getServiceState().getState());
            simDetailsDto.setServiceState(serviceState);

            //Mobile Voice Network Type
            String networkType = getNetworkType(telephonyManager.getNetworkType());
            simDetailsDto.setMobVoiceNetworkType(networkType);

            //Mobile Data Network Type
            simDetailsDto.setMobDataNetworkType(networkType);

            //Roaming
            String roaming = telephonyManager.isNetworkRoaming() ? "Roaming" : "Not Roaming";
            simDetailsDto.setRoaming(roaming);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return simDetailsDto;
    }

    public String getNetworkType(int networkType) {

        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "2G";
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "3G";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "4G";
            default:
                return "Unknown";
        }
    }

    private String getServiceState(int state) {
        switch (state) {
            case ServiceState.STATE_OUT_OF_SERVICE:
                return "No Service";
            case ServiceState.STATE_EMERGENCY_ONLY:
                return "Emergency Only";
            case ServiceState.STATE_IN_SERVICE:
                return "In Service";
            case ServiceState.STATE_POWER_OFF:
                return "Power Off";

            default:
                break;
        }

        return "Not Available";
    }

    private int getSignalStrengthValue(List<CellInfo> regCellInfo, int simSlotIndex, SignalStrength signalStrength) {

        if (regCellInfo.size() == 1)
            simSlotIndex = 0;

        if (regCellInfo.get(simSlotIndex) instanceof CellInfoLte) {
            CellInfoLte cellInfoLte = (CellInfoLte) regCellInfo.get(simSlotIndex);
            // Reflection code starts from here
            try {
                Method[] methods = android.telephony.SignalStrength.class.getMethods();
                for (Method mthd : methods) {
                    if (mthd.getName().equals("getLteSignalStrength") || mthd.getName().equals("getLteRsrp") || mthd.getName().equals("getLteRsrq") || mthd.getName().equals("getLteRssnr") || mthd.getName().equals("getLteCqi")) {
                        Log.i(TAG, "onSignalStrengthsChanged: " + mthd.getName() + " " + mthd.invoke(signalStrength));

                        if (mthd.getName().equals("getLteRsrp")) {
                            return (int) mthd.invoke(signalStrength);
                        }
                    }
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            // Reflection code ends here
        }

        if (regCellInfo.get(simSlotIndex) instanceof CellInfoGsm) {
            CellInfoGsm cellInfoGsm = (CellInfoGsm) regCellInfo.get(simSlotIndex);
            CellSignalStrengthGsm cellSignalStrengthGsm = cellInfoGsm.getCellSignalStrength();

            return cellSignalStrengthGsm.getDbm();
        }

        if (regCellInfo.get(simSlotIndex) instanceof CellInfoCdma) {
            CellInfoCdma cellInfoCdma = (CellInfoCdma) regCellInfo.get(simSlotIndex);
            CellSignalStrengthCdma cellSignalStrengthCdma = cellInfoCdma.getCellSignalStrength();
            return cellSignalStrengthCdma.getDbm();
        }

        if (regCellInfo.get(simSlotIndex) instanceof CellInfoWcdma) {
            CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) regCellInfo.get(simSlotIndex);
            CellSignalStrengthWcdma cellSignalStrengthWcdma = cellInfoWcdma.getCellSignalStrength();
            return cellSignalStrengthWcdma.getDbm();

        }
        return 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (asynSaveNetworkSIMDetailsData != null && !asynSaveNetworkSIMDetailsData.isCancelled()) {
            asynSaveNetworkSIMDetailsData.cancel(true);
        }
    }

    @Override
    public void onBackPressed() {
        backPressed();
    }

    public void backPressed() {
        finish();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home || itemId == R.id.action_home_dashborad) {
            backPressed();
        }
        return true;
    }

    private String getSIMSignalStrengthWithQualityUsingLevel(int bar, String signalStrengthInDBM) {
        String quality = "";

        switch (bar) {

            case BAR_0_NO_SIGNAL:
                quality = "No Signal (" + signalStrengthInDBM + ")";
                break;

            case BAR_1_POOR:
                quality = "Poor (" + signalStrengthInDBM + ")";
                break;

            case BAR_2_FAIR:
                quality = "Moderate (" + signalStrengthInDBM + ")";
                break;

            case BAR_3_GOOD:
                quality = "Good (" + signalStrengthInDBM + ")";
                break;

            case BAR_4_VERY_GOOD:
                quality = "Great (" + signalStrengthInDBM + ")";
                break;

            case BAR_5_EXCELLENT:
                quality = "Excellent (" + signalStrengthInDBM + ")";
                break;

            default:
                break;
        }
        return quality;
    }

    private int determineBarsSignalQuality(String signalStrengthInDBM) {

        int bar = BAR_0_NO_SIGNAL;

        if (TextUtils.isEmpty(signalStrengthInDBM)) {
            return bar;
        }

        signalStrengthInDBM = signalStrengthInDBM.trim().replace("dBM", "");
        signalStrengthInDBM = signalStrengthInDBM.trim();
        int signalValue = Integer.parseInt(signalStrengthInDBM);

        if (signalValue > -60) {                                     //Excellent: Greater Than -60 or 5 Bars
            bar = BAR_5_EXCELLENT;

        } else if (signalValue >= -75 && signalValue <= -60) {       //Very good: -60 to -75 or 4 Bars
            bar = BAR_4_VERY_GOOD;

        } else if (signalValue >= -90 && signalValue <= -76) {       //Good: -76 to -90 or 3 Bars.
            bar = BAR_3_GOOD;

        } else if (signalValue >= -100 && signalValue <= -91) {       //Fair: -91 to -100 or 2 Bars.
            bar = BAR_2_FAIR;

        } else if (signalValue >= -110 && signalValue <= -101) {      //Poor: -101 to -110 or 1 Bar.
            bar = BAR_1_POOR;

        } else if (signalValue < -110) {                             //No signal: Less Than -110 or No Bars / No Service.
            bar = BAR_0_NO_SIGNAL;
        }
        return bar;
    }

    //region Start: Network Signal

    private int TOTAL_TIME_IN_MS;  // = 60000;       //60sec = 60000 ms
    private int INTERVAL_IN_MS; // = 6000;          //6 sec = 6000 ms
    private int executedCount = 0;
    private Timer timer;
    private TimerTask timerTask;
    private ProgressDialog progressDialog;
    private HashMap<Integer, String[]> allSIMData;

    public void showProgressSpinner(Context context) {

        progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    public void dismissProgressSpinner() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private void getSIMStrengthValue(SIMNetworkConfigDto networkConfigDto) {

        if (networkConfigDto.getTOTAL_TIME_IN_MS() <= 0 || networkConfigDto.getINTERVAL_IN_MS() <= 0) {
            AlertDialogBoxInfo.alertDialogShow(context, "Improper SIM Network Configuration details.");
            return;
        }

        TOTAL_TIME_IN_MS = networkConfigDto.getTOTAL_TIME_IN_MS();
        INTERVAL_IN_MS = networkConfigDto.getINTERVAL_IN_MS();
        executedCount = 0;
        int maxCountParts = (int) Math.round((double) TOTAL_TIME_IN_MS / INTERVAL_IN_MS);
        timer = new Timer();
        allSIMData = getAllSIMSignalData(maxCountParts);
        setSIMExecutionStatus(true);
        showProgressSpinner(context);
        timerTask = new TimerTask() {
            @Override
            public void run() {

                if (executedCount >= maxCountParts) {
                    Log.e(TAG, "Testing: Timer Cancelled: " + executedCount + " maxCountParts: " + maxCountParts);
                    for (Map.Entry mapElement : allSIMData.entrySet()) {
                        int key = (int) mapElement.getKey();
                        String[] value = (String[]) mapElement.getValue();
                        Log.e(TAG, "Testing: " + key + "  Array: " + Arrays.toString(value));

                    }
                    stopTimerTask(timerTask);
                    setSIMExecutionStatus(false);
                    collectSIMDetailsToPost(allSIMData);
                    return;
                }

                allSIMData = collectAllSIMSignalValue(allSIMData, executedCount);
                executedCount++;
                Log.e(TAG, "Testing: Executed Count: " + executedCount);

            }
        };
        timer.schedule(timerTask, 1000, INTERVAL_IN_MS);

    }

    private HashMap<Integer, String[]> collectAllSIMSignalValue(HashMap<Integer, String[]> simSignalData, int executedCountPos) {

        for (int i = 0; i < simDetailsList.size(); i++) {
            String value = getLastSignalValue(simDetailsList.get(i));
            if (!TextUtils.isEmpty(value)) {
                String[] signalArray = simSignalData.get(simDetailsList.get(i).getSimSlotIndex());
                signalArray[executedCountPos] = value;
                simSignalData.put(simDetailsList.get(i).getSimSlotIndex(), signalArray);
            }
        }
        return simSignalData;
    }

    private HashMap<Integer, String[]> getAllSIMSignalData(int maxCountPartsSize) {
        HashMap<Integer, String[]> simSignalData = new HashMap<Integer, String[]>();

        for (int i = 0; i < simDetailsList.size(); i++) {
            int simSlot = simDetailsList.get(i).getSimSlotIndex();
            String slotOperatorName = simDetailsList.get(i).getOperatorInfo();
            if (TextUtils.isEmpty(slotOperatorName) || slotOperatorName.toUpperCase().equals("UNKNOWN")) {
                simSignalData.put(simSlot, null);
            } else {
                simSignalData.put(simSlot, new String[maxCountPartsSize]);
            }
        }
        return simSignalData;
    }

    private String getLastSignalValue(SIMDetailsDto simDetailsDto) {
        String value = null;

        ArrayList<String> list = simDetailsDto.getSignalStrengthValue();
        if (list.size() > 0) {
            value = list.get(list.size() - 1);
        }
        return value;
    }

    private void setSIMExecutionStatus(boolean status) {

        for (int i = 0; i < simDetailsList.size(); i++) {
            SIMDetailsDto detailsDto = simDetailsList.get(i);
            detailsDto.setExecutionStarted(status);
            if (!status) {
                detailsDto.setSignalStrengthValue(new ArrayList<>());
            }
            simDetailsList.set(i, detailsDto);
        }
    }

    public void stopTimerTask(TimerTask timerTask) {
        //stop the timer, if it's not already null
        if (timer != null) {
            dismissProgressSpinner();
            timer.purge();
            timer.cancel();
            timerTask.cancel();
            timer = null;
            AlertDialogBoxInfo.alertDialogShow(context, "Network Strength Testing successfully done.");
        }
    }

    private void collectSIMDetailsToPost(HashMap<Integer, String[]> allSIMData) {

        try {

            for (int i = 0; i < simDetailsList.size(); i++) {
                SIMDetailsDto detailsDto = simDetailsList.get(i);

                int simSlot = detailsDto.getSimSlotIndex();
                ArrayList<String> signalData;
                if (allSIMData.get(simSlot) == null || allSIMData.isEmpty()) {
                    signalData = new ArrayList<>();
                } else {
                    signalData = new ArrayList(Arrays.asList(allSIMData.get(simSlot)));
                }
                detailsDto.setSignalStrengthValue(signalData);
                simDetailsList.set(i, detailsDto);
            }

            //Get Current Location
            getLocation();

            //Collect Data to Prepare JSON
            Gson gson = new Gson();
            String resultData = gson.toJson(simDetailsList, new TypeToken<ArrayList<SIMDetailsDto>>() {
            }.getType());
            JSONArray jsonArray = new JSONArray(resultData);

            //Prepare Final JSON
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("latitude", latitude);
            jsonObject.put("longitude", longitude);
            jsonObject.put("sim_signal_data", jsonArray);

            String finalJsonData = jsonObject.toString();

            Log.e(TAG, "Testing: Data: Complete Data: " + finalJsonData);

            asynSaveNetworkSIMDetailsData = new AsynSaveNetworkSIMDetailsData(context, finalJsonData, new AsynSaveNetworkSIMDetailsData.Callback() {
                @Override
                public void onResult(String result) {
                    Log.e(TAG, "Testing: AsynSaveNetworkSIMDetailsData: " + result);
                }
            });
            asynSaveNetworkSIMDetailsData.execute("");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getLocation() {
        //Get Current location
        if (gpsTracker.canGetLocation()) {
            latitude = String.valueOf(gpsTracker.getLatitude());
            longitude = String.valueOf(gpsTracker.getLongitude());
        }
    }

    //endregion

    LineChart mChart;
    List<LineDataSet> lineDataSetList = null;
    private LineData data;
    int[] colorCode = {R.color.line_chart_circle_color, R.color.line2_chart_circle_color};
    int[] highlightColorCode = {R.color.line_chart_highlight_color, R.color.line2_chart_highlight_color};

    protected void setupChart() {

        mChart = new LineChart(this);
        mChart = (LineChart) findViewById(R.id.chart2);

        mChart.setHighlightPerTapEnabled(true);
        mChart.setTouchEnabled(true);
        Description description = new Description();
        description.setText("");
        mChart.setDescription(description);

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setPinchZoom(true);

        data = new LineData();
        data.setValueTextColor(getResources().getColor(R.color.line_chart_text));
        mChart.setData(data);
        mChart.invalidate();

        // Setup legend
        Legend l = mChart.getLegend();
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setTextColor(getResources().getColor(R.color.line_chart_text_legend));

        XAxis x1 = mChart.getXAxis();
        x1.setTextColor(getResources().getColor(R.color.line_chart_x_axis_text));
        x1.setDrawGridLines(false);
        x1.setAvoidFirstLastClipping(true);

        YAxis y1 = mChart.getAxisLeft();
        y1.setTextColor(getResources().getColor(R.color.line_chart_y_axis_text));
        y1.setAxisMaximum(-60f);
        y1.setAxisMinimum(-140f);

        mChart.getAxisLeft().setStartAtZero(false);
        mChart.getAxisRight().setStartAtZero(false);

        y1.setDrawGridLines(true);
        y1.setLabelCount(10, true);

        YAxis y2 = mChart.getAxisRight();
        y2.setEnabled(false);

        l.setXEntrySpace(10f); // set the space between the legend entries on the x-axis
        l.setYEntrySpace(10f);

    }

    protected LineDataSet createSet(String lineLabel, int color, int highlightColor) {

        LineDataSet set = new LineDataSet(null, lineLabel);
        set.setCubicIntensity(0.2f);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(getResources().getColor(color));
        set.setCircleColor(getResources().getColor(color));
        set.setCircleHoleColor(getResources().getColor(R.color.line_chart_circle_hole_color));
        set.setLineWidth(2f);
        set.setCircleRadius(6f);
        set.setHighLightColor(getResources().getColor(highlightColor));
        set.setValueTextColor(getResources().getColor(R.color.line_chart_value_text_color));
        set.setValueTextSize(10f);

        return set;
    }

    protected void addEntry(int value, int slotIndex) {

        LineData dataChart = mChart.getData();
        if (dataChart == null) {
            return;
        }

        //Create Line Data Set
        if (lineDataSetList == null || lineDataSetList.size() != simDetailsList.size()) {
            lineDataSetList = new ArrayList<>();
            for (int i = 0; i < simDetailsList.size(); i++) {

                String label = simDetailsList.get(i).getSimSlotName() + ": " + simDetailsList.get(i).getOperatorInfo() + " (RSRP dBm)";
                LineDataSet lineDataSet = createSet(label, colorCode[i], highlightColorCode[i]);
                lineDataSetList.add(lineDataSet);
                data.addDataSet(lineDataSet);
            }
        }

        mChart.setData(data);
        mChart.invalidate();

        int entryCount = dataChart.getDataSets().get(slotIndex).getEntryCount();

        dataChart.addEntry(new Entry((float) entryCount, value), slotIndex);

        mChart.notifyDataSetChanged();
        mChart.invalidate();
        mChart.setVisibleXRange(0, 7);
        if (lastX > 8)
            mChart.moveViewToX(lastX - 8);

        lastX++;
    }

}
