package in.vakrangee.franchisee.networktesting.speedtest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import in.vakrangee.franchisee.networktesting.R;
import in.vakrangee.franchisee.networktesting.simstrength.Connectivity;
import in.vakrangee.franchisee.networktesting.speedtest.test.HttpDownloadTest;
import in.vakrangee.franchisee.networktesting.speedtest.test.HttpUploadTest;
import in.vakrangee.franchisee.networktesting.speedtest.test.PingTest;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;

public class NetworkSpeedTestingActivity extends AppCompatActivity {

    private static final String TAG = "NetworkSpeedTestingActivity";
    private Context context;
    private Toolbar toolbar;
    private PermissionHandler permissionHandler;
    static int position = 0;
    static int lastPosition = 0;
    GetSpeedTestHostsHandler getSpeedTestHostsHandler = null;
    HashSet<String> tempBlackList;
    private DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SubscriptionManager localSubscriptionManager;
    private int simCount;
    private String startTime, endTime;
    private AsyncSaveNetworkSpeedTestingData asyncSaveNetworkSpeedTestingData = null;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alert;
    private Thread speedTesterThread = null;
    boolean exit = false;
    private Button startButton;
    private DecimalFormat dec;

    @Override
    public void onResume() {
        super.onResume();

        getSpeedTestHostsHandler = new GetSpeedTestHostsHandler();
        getSpeedTestHostsHandler.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_speed);

        //Initialize
        context = this;
        permissionHandler = new PermissionHandler(this);
        toolbar = (Toolbar) findViewById(R.id.toolbarNetworkSpeed);

        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null) {
            String title = getString(R.string.title_network_speed_testing);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + title + "</small>"));
        }

        startButton = (Button) findViewById(R.id.startButton);
        dec = new DecimalFormat("#.##");
        startButton.setText("Begin Test");

        tempBlackList = new HashSet<>();
        localSubscriptionManager = SubscriptionManager.from(this);
        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        simCount = manager.getPhoneCount();

        getSpeedTestHostsHandler = new GetSpeedTestHostsHandler();
        getSpeedTestHostsHandler.start();

        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startButtonClick(v);
            }
        });

        //Perform Click to Start Speed Test
        startButton.performClick();
    }

    private void startButtonClick(View v) {
        exit = false;

        // Step1: Check Internet Connection
        if (!InternetConnection.isNetworkAvailable(context)) {
            showMessage("No Internet Connection.");
            return;
        }

        startButton.setEnabled(false);
        startTime = dateTimeFormat.format(new Date());
        Log.e(TAG, "Testing: Data: Start Time: " + startTime);

        //Restart test icin eger baglanti koparsa
        if (getSpeedTestHostsHandler == null) {
            getSpeedTestHostsHandler = new GetSpeedTestHostsHandler();
            getSpeedTestHostsHandler.start();
        }

        speedTesterThread = new Thread(new Runnable() {
            RotateAnimation rotate;
            ImageView barImageView = (ImageView) findViewById(R.id.barImageView);
            TextView pingTextView = (TextView) findViewById(R.id.pingTextView);
            TextView downloadTextView = (TextView) findViewById(R.id.downloadTextView);
            TextView uploadTextView = (TextView) findViewById(R.id.uploadTextView);

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startButton.setText("Selecting best server based on ping...");
                    }
                });

                //Get egcodes.speedtest hosts
                int timeCount = 600; //1min
                while (!getSpeedTestHostsHandler.isFinished()) {
                    timeCount--;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                    if (timeCount <= 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "No Connection...", Toast.LENGTH_LONG).show();
                                startButton.setEnabled(true);
                                startButton.setTextSize(16);
                                startButton.setText("Restart Test");
                            }
                        });
                        getSpeedTestHostsHandler = null;
                        return;
                    }
                }

                //Find closest server
                HashMap<Integer, String> mapKey = getSpeedTestHostsHandler.getMapKey();
                HashMap<Integer, List<String>> mapValue = getSpeedTestHostsHandler.getMapValue();
                double selfLat = getSpeedTestHostsHandler.getSelfLat();
                double selfLon = getSpeedTestHostsHandler.getSelfLon();
                double tmp = 19349458;
                double dist = 0.0;
                int findServerIndex = 0;
                for (int index : mapKey.keySet()) {
                    if (tempBlackList.contains(mapValue.get(index).get(5))) {
                        continue;
                    }

                    Location source = new Location("Source");
                    source.setLatitude(selfLat);
                    source.setLongitude(selfLon);

                    List<String> ls = mapValue.get(index);
                    Location dest = new Location("Dest");
                    dest.setLatitude(Double.parseDouble(ls.get(0)));
                    dest.setLongitude(Double.parseDouble(ls.get(1)));

                    double distance = source.distanceTo(dest);
                    if (tmp > distance) {
                        tmp = distance;
                        dist = distance;
                        findServerIndex = index;
                    }
                }
                String uploadAddr = mapKey.get(findServerIndex);
                final List<String> info = mapValue.get(findServerIndex);
                final double distance = dist;

                if (info == null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startButton.setTextSize(12);
                            startButton.setText("There was a problem in getting Host Location. Try again later.");
                        }
                    });
                    return;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startButton.setTextSize(13);
                        startButton.setText(String.format("Host Location: %s [Distance: %s km]", info.get(2), new DecimalFormat("#.##").format(distance / 1000)));
                    }
                });

                //Init Ping graphic
                final LinearLayout chartPing = (LinearLayout) findViewById(R.id.chartPing);
                XYSeriesRenderer pingRenderer = new XYSeriesRenderer();
                XYSeriesRenderer.FillOutsideLine pingFill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BOUNDS_ALL);
                pingFill.setColor(Color.parseColor("#4d5a6a"));
                pingRenderer.addFillOutsideLine(pingFill);
                pingRenderer.setDisplayChartValues(false);
                pingRenderer.setShowLegendItem(false);
                pingRenderer.setColor(Color.parseColor("#4d5a6a"));
                pingRenderer.setLineWidth(5);
                final XYMultipleSeriesRenderer multiPingRenderer = new XYMultipleSeriesRenderer();
                multiPingRenderer.setXLabels(0);
                multiPingRenderer.setYLabels(0);
                multiPingRenderer.setZoomEnabled(false);
                multiPingRenderer.setXAxisColor(Color.parseColor("#647488"));
                multiPingRenderer.setYAxisColor(Color.parseColor("#2F3C4C"));
                multiPingRenderer.setPanEnabled(true, true);
                multiPingRenderer.setZoomButtonsVisible(false);
                multiPingRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
                multiPingRenderer.addSeriesRenderer(pingRenderer);

                //Init Download graphic
                final LinearLayout chartDownload = (LinearLayout) findViewById(R.id.chartDownload);
                XYSeriesRenderer downloadRenderer = new XYSeriesRenderer();
                XYSeriesRenderer.FillOutsideLine downloadFill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BOUNDS_ALL);
                downloadFill.setColor(Color.parseColor("#4d5a6a"));
                downloadRenderer.addFillOutsideLine(downloadFill);
                downloadRenderer.setDisplayChartValues(false);
                downloadRenderer.setColor(Color.parseColor("#4d5a6a"));
                downloadRenderer.setShowLegendItem(false);
                downloadRenderer.setLineWidth(5);
                final XYMultipleSeriesRenderer multiDownloadRenderer = new XYMultipleSeriesRenderer();
                multiDownloadRenderer.setXLabels(0);
                multiDownloadRenderer.setYLabels(0);
                multiDownloadRenderer.setZoomEnabled(false);
                multiDownloadRenderer.setXAxisColor(Color.parseColor("#647488"));
                multiDownloadRenderer.setYAxisColor(Color.parseColor("#2F3C4C"));
                multiDownloadRenderer.setPanEnabled(false, false);
                multiDownloadRenderer.setZoomButtonsVisible(false);
                multiDownloadRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
                multiDownloadRenderer.addSeriesRenderer(downloadRenderer);

                //Init Upload graphic
                final LinearLayout chartUpload = (LinearLayout) findViewById(R.id.chartUpload);
                XYSeriesRenderer uploadRenderer = new XYSeriesRenderer();
                XYSeriesRenderer.FillOutsideLine uploadFill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BOUNDS_ALL);
                uploadFill.setColor(Color.parseColor("#4d5a6a"));
                uploadRenderer.addFillOutsideLine(uploadFill);
                uploadRenderer.setDisplayChartValues(false);
                uploadRenderer.setColor(Color.parseColor("#4d5a6a"));
                uploadRenderer.setShowLegendItem(false);
                uploadRenderer.setLineWidth(5);
                final XYMultipleSeriesRenderer multiUploadRenderer = new XYMultipleSeriesRenderer();
                multiUploadRenderer.setXLabels(0);
                multiUploadRenderer.setYLabels(0);
                multiUploadRenderer.setZoomEnabled(false);
                multiUploadRenderer.setXAxisColor(Color.parseColor("#647488"));
                multiUploadRenderer.setYAxisColor(Color.parseColor("#2F3C4C"));
                multiUploadRenderer.setPanEnabled(false, false);
                multiUploadRenderer.setZoomButtonsVisible(false);
                multiUploadRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
                multiUploadRenderer.addSeriesRenderer(uploadRenderer);

                //Reset value, graphics
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pingTextView.setText("0 ms");
                        chartPing.removeAllViews();
                        downloadTextView.setText("0 Mbps");
                        chartDownload.removeAllViews();
                        uploadTextView.setText("0 Mbps");
                        chartUpload.removeAllViews();
                    }
                });
                final List<Double> pingRateList = new ArrayList<>();
                final List<Double> downloadRateList = new ArrayList<>();
                final List<Double> uploadRateList = new ArrayList<>();
                Boolean pingTestStarted = false;
                Boolean pingTestFinished = false;
                Boolean downloadTestStarted = false;
                Boolean downloadTestFinished = false;
                Boolean uploadTestStarted = false;
                Boolean uploadTestFinished = false;

                //Init Test
                final PingTest pingTest = new PingTest(info.get(6).replace(":8080", ""), 6);
                final HttpDownloadTest downloadTest = new HttpDownloadTest(uploadAddr.replace(uploadAddr.split("/")[uploadAddr.split("/").length - 1], ""));
                final HttpUploadTest uploadTest = new HttpUploadTest(uploadAddr);


                //Tests
                while (!exit) {

                    try {
                        if (!pingTestStarted) {
                            pingTest.start();
                            pingTestStarted = true;
                        }
                        if (pingTestFinished && !downloadTestStarted) {
                            downloadTest.start();
                            downloadTestStarted = true;
                        }
                        if (downloadTestFinished && !uploadTestStarted) {
                            uploadTest.start();
                            uploadTestStarted = true;
                        }


                        //Ping Test
                        if (pingTestFinished) {
                            //Failure
                            if (pingTest.getAvgRtt() == 0) {
                                System.out.println("Ping error...");
                            } else {
                                //Success
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        pingTextView.setText(dec.format(pingTest.getAvgRtt()) + " ms");
                                    }
                                });
                            }
                        } else {
                            pingRateList.add(pingTest.getInstantRtt());

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pingTextView.setText(dec.format(pingTest.getInstantRtt()) + " ms");
                                }
                            });

                            //Update chart
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Creating an  XYSeries for Income
                                    XYSeries pingSeries = new XYSeries("");
                                    pingSeries.setTitle("");

                                    int count = 0;
                                    List<Double> tmpLs = new ArrayList<>(pingRateList);
                                    for (Double val : tmpLs) {
                                        pingSeries.add(count++, val);
                                    }

                                    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
                                    dataset.addSeries(pingSeries);

                                    GraphicalView chartView = ChartFactory.getLineChartView(getBaseContext(), dataset, multiPingRenderer);
                                    chartPing.addView(chartView, 0);

                                }
                            });
                        }


                        //Download Test
                        if (pingTestFinished) {
                            if (downloadTestFinished) {
                                //Failure
                                if (downloadTest.getFinalDownloadRate() == 0) {
                                    System.out.println("Download error...");
                                } else {
                                    //Success
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            downloadTextView.setText(dec.format(downloadTest.getFinalDownloadRate()) + " Mbps");
                                        }
                                    });
                                }
                            } else {
                                //Calc position
                                double downloadRate = downloadTest.getInstantDownloadRate();
                                downloadRateList.add(downloadRate);
                                position = getPositionByRate(downloadRate);

                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        rotate = new RotateAnimation(lastPosition, position, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                                        rotate.setInterpolator(new LinearInterpolator());
                                        rotate.setDuration(100);
                                        barImageView.startAnimation(rotate);
                                        downloadTextView.setText(dec.format(downloadTest.getInstantDownloadRate()) + " Mbps");

                                    }

                                });
                                lastPosition = position;

                                //Update chart
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Creating an  XYSeries for Income
                                        XYSeries downloadSeries = new XYSeries("");
                                        downloadSeries.setTitle("");

                                        List<Double> tmpLs = new ArrayList<>(downloadRateList);
                                        int count = 0;
                                        for (Double val : tmpLs) {
                                            downloadSeries.add(count++, val);
                                        }

                                        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
                                        dataset.addSeries(downloadSeries);

                                        GraphicalView chartView = ChartFactory.getLineChartView(getBaseContext(), dataset, multiDownloadRenderer);
                                        chartDownload.addView(chartView, 0);
                                    }
                                });

                            }
                        }


                        //Upload Test
                        if (downloadTestFinished) {
                            if (uploadTestFinished) {
                                //Failure
                                if (uploadTest.getFinalUploadRate() == 0) {
                                    System.out.println("Upload error...");
                                } else {
                                    //Success
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            uploadTextView.setText(dec.format(uploadTest.getFinalUploadRate()) + " Mbps");
                                        }
                                    });
                                }
                            } else {
                                //Calc position
                                double uploadRate = uploadTest.getInstantUploadRate();
                                uploadRateList.add(uploadRate);
                                position = getPositionByRate(uploadRate);

                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        rotate = new RotateAnimation(lastPosition, position, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                                        rotate.setInterpolator(new LinearInterpolator());
                                        rotate.setDuration(100);
                                        barImageView.startAnimation(rotate);
                                        uploadTextView.setText(dec.format(uploadTest.getInstantUploadRate()) + " Mbps");
                                    }

                                });
                                lastPosition = position;

                                //Update chart
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Creating an  XYSeries for Income
                                        XYSeries uploadSeries = new XYSeries("");
                                        uploadSeries.setTitle("");

                                        int count = 0;
                                        List<Double> tmpLs = new ArrayList<>(uploadRateList);
                                        for (Double val : tmpLs) {
                                            if (count == 0) {
                                                val = 0.0;
                                            }
                                            uploadSeries.add(count++, val);
                                        }

                                        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
                                        dataset.addSeries(uploadSeries);

                                        GraphicalView chartView = ChartFactory.getLineChartView(getBaseContext(), dataset, multiUploadRenderer);
                                        chartUpload.addView(chartView, 0);
                                    }
                                });

                            }
                        }

                        //Test bitti
                        if (pingTestFinished && downloadTestFinished && uploadTest.isFinished()) {
                            break;
                        }

                        if (pingTest.isFinished()) {
                            pingTestFinished = true;
                        }
                        if (downloadTest.isFinished()) {
                            downloadTestFinished = true;
                        }
                        if (uploadTest.isFinished()) {
                            uploadTestFinished = true;
                        }

                        if (pingTestStarted && !pingTestFinished) {
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e) {
                            }
                        } else {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                            }
                        }

                    } catch (Exception e) {
                        System.out.println("Caught:" + e);
                    }
                }

                if (!exit) {
                    //Thread bitiminde button yeniden aktif ediliyor
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startButton.setEnabled(true);
                            startButton.setTextSize(16);
                            startButton.setText("Restart Test");

                            endTime = dateTimeFormat.format(new Date());

                            //Collect Data to Prepare JSON
                            InternetSpeedDetailsDto detailsDto = prepareInternetSpeedDetailsDto(pingTextView.getText().toString(), downloadTextView.getText().toString(), uploadTextView.getText().toString());
                            Gson gson = new Gson();
                            String data = gson.toJson(detailsDto, InternetSpeedDetailsDto.class);

                            Log.e(TAG, "Testing: Data: Complete Data: " + data);

                            asyncSaveNetworkSpeedTestingData = new AsyncSaveNetworkSpeedTestingData(context, data, new AsyncSaveNetworkSpeedTestingData.Callback() {
                                @Override
                                public void onResult(String result) {
                                    Log.e(TAG, "Testing: AsyncSaveNetworkSpeedTestingData: " + result);
                                }
                            });
                            asyncSaveNetworkSpeedTestingData.execute("");
                        }
                    });
                }
            }
        });
        speedTesterThread.start();
    }

    public void stopThread() {
        exit = true;
    }

    public void showMessage(String msg) {
        if (TextUtils.isEmpty(msg))
            return;

        if (alert == null) {
            alertDialogBuilder = new AlertDialog.Builder(context);

            alertDialogBuilder
                    .setMessage(Html.fromHtml(msg))
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            alert = null;
                        }
                    });
            alert = alertDialogBuilder.create();
            alert.show();
        }
    }

    private InternetSpeedDetailsDto prepareInternetSpeedDetailsDto(String pingMs, String downloadSpeed, String uploadSpeed) {
        InternetSpeedDetailsDto speedDetailsDto = new InternetSpeedDetailsDto();

        String preferredInternetSIM = getPreferredInternetAccessSIMOperator();
        boolean IsWifi = Connectivity.isConnectedWifi(context);

        speedDetailsDto.setStartTime(startTime);
        speedDetailsDto.setEndTime(endTime);
        speedDetailsDto.setWifiStatus(IsWifi);
        speedDetailsDto.setWifiSSID(Connectivity.getWifiSSID(context));
        speedDetailsDto.setWifiMacAddress(Connectivity.getWifiMacAddress(context));
        speedDetailsDto.setPreferredSIMInternet(preferredInternetSIM);
        speedDetailsDto.setPingMs(pingMs);
        speedDetailsDto.setDownloadSpeed(downloadSpeed);
        speedDetailsDto.setUploadSpeed(uploadSpeed);

        //SIM Details
        for (int i = 0; i <= simCount; i++) {

            if (localSubscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(i) != null) {
                int subscriptionId = localSubscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(i).getSubscriptionId();
                TelephonyManager mTelephonyManager = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).createForSubscriptionId(subscriptionId);
                boolean IsMobileEnabled = mTelephonyManager.isDataEnabled();

                SIMDetailsDto simDetailsDto = new SIMDetailsDto();
                simDetailsDto.setSimMobileDataEnabled(IsMobileEnabled);
                simDetailsDto.setSimOperatorName(mTelephonyManager.getSimOperatorName());
                speedDetailsDto.simDetailsList.add(simDetailsDto);
            }
        }

        return speedDetailsDto;
    }

    private String getPreferredInternetAccessSIMOperator() {
        String operator = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            int subscriptionId = manager.getPreferredOpportunisticDataSubscription();
            TelephonyManager mTelephonyManager = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).createForSubscriptionId(subscriptionId);
            operator = mTelephonyManager.getSimOperatorName();

        } else {
            int subscriptionId = localSubscriptionManager.getDefaultDataSubscriptionId();
            TelephonyManager mTelephonyManager = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).createForSubscriptionId(subscriptionId);
            operator = mTelephonyManager.getSimOperatorName();

        }
        return operator;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (asyncSaveNetworkSpeedTestingData != null && !asyncSaveNetworkSpeedTestingData.isCancelled()) {
            asyncSaveNetworkSpeedTestingData.cancel(true);
        }
    }

    @Override
    public void onBackPressed() {
        backPressed();
    }

    public void backPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(getResources().getString(R.string.alert_go_back_msg))
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        stopThread();
                        if (asyncSaveNetworkSpeedTestingData != null && !asyncSaveNetworkSpeedTestingData.isCancelled()) {
                            asyncSaveNetworkSpeedTestingData.cancel(true);
                        }

                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            backPressed();
        } else if (itemId == R.id.action_home_dashborad) {
            backPressed();
        }
        return true;
    }

    public int getPositionByRate(double rate) {
        if (rate <= 1) {
            return (int) (rate * 30);

        } else if (rate <= 10) {
            return (int) (rate * 6) + 30;

        } else if (rate <= 30) {
            return (int) ((rate - 10) * 3) + 90;

        } else if (rate <= 50) {
            return (int) ((rate - 30) * 1.5) + 150;

        } else if (rate <= 100) {
            return (int) ((rate - 50) * 1.2) + 180;
        }

        return 0;
    }

}
