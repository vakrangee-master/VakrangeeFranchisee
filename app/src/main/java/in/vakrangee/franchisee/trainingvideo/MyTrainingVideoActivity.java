package in.vakrangee.franchisee.trainingvideo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mindorks.nybus.NYBus;
import com.mindorks.nybus.annotation.Subscribe;
import com.mindorks.nybus.event.Channel;

import java.util.ArrayList;
import java.util.List;

import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.activity.DashboardActivity;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.utils.Logger;
import in.vakrangee.supercore.franchisee.utils.network.ConnectivityChangeReceiver;
import in.vakrangee.supercore.franchisee.utils.network.EventData;
import in.vakrangee.supercore.franchisee.utils.network.NetworkHealthHandler;
import in.vakrangee.supercore.franchisee.webservice.WebService;

public class MyTrainingVideoActivity extends AppCompatActivity {
    private static final String TAG = MyTrainingVideoActivity.class.getCanonicalName();

    private Toolbar toolbar;
    private ProgressDialog progress;
    private TelephonyManager telephonyManager;
    //String TAG = "Response";
    private String diplayServerResopnse;
    private InternetConnection internetConnection;
    private RecyclerView recyclerView;
    private AdapterTrainingVideo mAdapter;

    private TextView emptyView;

    private AsyncgetGetTrainingVideo asyncgetGetTrainingVideo;
    private Logger logger;

    private Connection connection;
    public static boolean active = false;
    private int FROM = 0;
    private DeprecateHandler deprecateHandler;
    private ConnectivityChangeReceiver receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_training_video);

        //toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbarhlep);
        emptyView = (TextView) findViewById(R.id.empty_view);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_myvideo);


        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            toolbar.setTitleTextColor(Color.WHITE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.myvideotraining);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        logger = Logger.getInstance(MyTrainingVideoActivity.this);
        deprecateHandler = new DeprecateHandler(this);
        receiver = new ConnectivityChangeReceiver();
        connection = new Connection(getApplicationContext());
        //Register
        NYBus.get().register(this, Channel.TWO);


        String vkid = connection.getVkid();
        internetConnection = new InternetConnection(getApplicationContext());
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //GEt Data from Intent
        FROM = getIntent().getIntExtra("From", 0);

        if (vkid.toUpperCase().startsWith("VL") || vkid.toUpperCase().startsWith("VA")) {
            emptyView.setText("Sorry. This service is available for franchisee only.");
            emptyView.setTextColor(Color.RED);
            emptyView.setVisibility(View.VISIBLE);
        } else if (internetConnection.isConnectingToInternet() == false) {
            AlertDialogBoxInfo.alertDialogShow(getApplicationContext(), getResources().getString(R.string.internetCheck));
        } else {
            progress = new ProgressDialog(MyTrainingVideoActivity.this);
            progress.setTitle(R.string.fetchingtriaingvideo);
            progress.setMessage(getResources().getString(R.string.pleaseWait));
            progress.setCancelable(false);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();
            asyncgetGetTrainingVideo = new AsyncgetGetTrainingVideo();
            asyncgetGetTrainingVideo.execute();
        }


    }


    public void onBackPressed() {
        if (FROM == 1) {
            finish();
        } else {
            Intent intent = new Intent(MyTrainingVideoActivity.this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (FROM == 1) {
                    finish();
                } else {
                    Intent intent = new Intent(MyTrainingVideoActivity.this, DashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                break;
        }
        return true;
    }

    private class AsyncgetGetTrainingVideo extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {

                String vkid = connection.getVkid();
                diplayServerResopnse = WebService.getTrainingVideo(vkid);
            } catch (Exception e) {
                e.printStackTrace();
                logger.writeError(TAG, "Error in helpline service: " + e.toString());
                //AlertDialogBoxInfo.alertDialogShow(MyhelpLineActivity.this, getResources().getString(R.string.Warning));
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            progress.dismiss();

            if (TextUtils.isEmpty(diplayServerResopnse)) {
                AlertDialogBoxInfo.alertDialogShow(MyTrainingVideoActivity.this, getResources().getString(R.string.Warning));
                return;
            }
            try {
                String strGeo = diplayServerResopnse.replace("OKAY|", "");
                Gson gson = new Gson();
                List<trainingvideopojo> events = gson.fromJson(strGeo,
                        new TypeToken<ArrayList<trainingvideopojo>>() {
                        }.getType());

                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext()) {
                    @Override
                    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                    }
                };
                recyclerView.setLayoutManager(layoutManager);
                // recyclerView.setNestedScrollingEnabled(true);
                recyclerView.setHasFixedSize(true);
                mAdapter = new AdapterTrainingVideo(getApplicationContext(), events);
                recyclerView.setAdapter(mAdapter);


                if (events.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }

            } catch (Exception e) {
                e.printStackTrace();
                AlertDialogBoxInfo.alertDialogShow(MyTrainingVideoActivity.this, getResources().getString(R.string.Warning));
            }

        }


    }

    @Override
    protected void onDestroy() {
        // Unregister
        unregisterReceiver(receiver);
        NYBus.get().unregister(this, Channel.TWO);
        super.onDestroy();
        if (asyncgetGetTrainingVideo != null && !asyncgetGetTrainingVideo.isCancelled()) {
            asyncgetGetTrainingVideo.cancel(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        active = true;
        registerReceiver(receiver, NetworkHealthHandler.prepareIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        active = false;
    }

    @Subscribe(channelId = Channel.TWO)
    public void onEvent(EventData event) {
        String actionMsg = getString(R.string.close_text);
        NetworkHealthHandler.displaySnackBar(findViewById(android.R.id.content), event.getData(), actionMsg, deprecateHandler);
    }
}
