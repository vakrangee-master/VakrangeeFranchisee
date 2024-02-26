package in.vakrangee.franchisee.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Timer;
import java.util.TimerTask;

import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;

public class MySerivcesActivity extends AppCompatActivity {
    Toolbar toolbar;
    private final static int DELAY = 10;
    private final Handler handler = new Handler();
    private final Timer timer = new Timer();
    int widthg = 0;
    InternetConnection internetConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_serivces);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.service);

//            final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
//            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//            getSupportActionBar().setHomeAsUpIndicator(upArrow);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        final LinearLayout mobilerechagre = (LinearLayout) findViewById(R.id.btnservicemobilerecharge);
        final LinearLayout dthrechagre = (LinearLayout) findViewById(R.id.btnservicedthrechagre);
        final LinearLayout mahavitran = (LinearLayout) findViewById(R.id.btnServiceMahavitran);

        final LinearLayout btnElectricityBillPayment = (LinearLayout) findViewById(R.id.btnElectricityBillPayment);
        final LinearLayout btnPipedGas = (LinearLayout) findViewById(R.id.btnPipedGas);
        final LinearLayout btnLandlineBillPayment = (LinearLayout) findViewById(R.id.btnLandlineBillPayment);


        final Handler handler = new Handler();
        final TimerTask task = new TimerTask() {
            private int counter = 0;

            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        //  Toast.makeText(DashboardActivity.this, "test", Toast.LENGTH_SHORT).show();

                        handler.postDelayed(new Runnable() {
                            public void run() {
                                // Actions to do after 10 seconds

                                // Toast.makeText(LoginPage.this, "Click ", Toast.LENGTH_SHORT).show();

                                LinearLayout layoutl1 = (LinearLayout) findViewById(R.id.l1);

                                assert layoutl1 != null;
                                int widtha = layoutl1.getWidth();
                                int heighta = layoutl1.getWidth() / 3;
                                int getHeigh = layoutl1.getHeight();


                                if (getHeigh < heighta) {
                                    LinearLayout.LayoutParams parmsa = new LinearLayout.LayoutParams(widtha, heighta);
                                    layoutl1.setLayoutParams(parmsa);
//

                                    int heightl = layoutl1.getWidth() / 3;
                                    LinearLayout.LayoutParams parmsl = new LinearLayout.LayoutParams(heightl, heightl);
                                    assert mobilerechagre != null;
                                    mobilerechagre.setLayoutParams(parmsl);

                                    assert dthrechagre != null;
                                    dthrechagre.setLayoutParams(parmsl);
                                    assert mahavitran != null;
                                    mahavitran.setLayoutParams(parmsl);
                                    btnElectricityBillPayment.setLayoutParams(parmsl);
                                    btnLandlineBillPayment.setLayoutParams(parmsl);
                                    btnPipedGas.setLayoutParams(parmsl);
                                }


                            }
                        }, 10);


                    }
                });
                if (++counter == 1) {
                    timer.cancel();
                }
            }
        };


        timer.schedule(task, DELAY, DELAY);

        assert mobilerechagre != null;
        mobilerechagre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                internetConnection = new InternetConnection(MySerivcesActivity.this);


                if (internetConnection.isNetworkAvailable(getApplicationContext()) == false) {

                    AlertDialogBoxInfo.alertDialogShow(MySerivcesActivity.this, getResources().getString(R.string.internetCheck));

                } else {
                    Intent i = new Intent(MySerivcesActivity.this, MobileRechargActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(i);
                    finish();
                }


            }
        });

        assert mahavitran != null;
        mahavitran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                internetConnection = new InternetConnection(MySerivcesActivity.this);
                if (internetConnection.isNetworkAvailable(getApplicationContext()) == false) {

                    AlertDialogBoxInfo.alertDialogShow(MySerivcesActivity.this, getResources().getString(R.string.internetCheck));

                } else {
                    Intent i = new Intent(MySerivcesActivity.this, MahavitranActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }


            }
        });

        assert dthrechagre != null;
        dthrechagre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internetConnection = new InternetConnection(MySerivcesActivity.this);

                if (internetConnection.isNetworkAvailable(getApplicationContext()) == false) {

                    AlertDialogBoxInfo.alertDialogShow(MySerivcesActivity.this, getResources().getString(R.string.internetCheck));

                } else {
                    Intent i = new Intent(MySerivcesActivity.this, DTHRechargeActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }


            }
        });
        assert btnElectricityBillPayment != null;
        btnElectricityBillPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                internetConnection = new InternetConnection(MySerivcesActivity.this);
                if (internetConnection.isNetworkAvailable(getApplicationContext()) == false) {

                    AlertDialogBoxInfo.alertDialogShow(MySerivcesActivity.this, getResources().getString(R.string.internetCheck));

                } else {
                    Intent i = new Intent(MySerivcesActivity.this, ElectricityBillPaymentActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(i);
                    finish();
                }


            }
        });


        assert btnPipedGas != null;
        btnPipedGas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                internetConnection = new InternetConnection(MySerivcesActivity.this);

                if (internetConnection.isNetworkAvailable(getApplicationContext()) == false) {

                    AlertDialogBoxInfo.alertDialogShow(MySerivcesActivity.this, getResources().getString(R.string.internetCheck));

                } else {
                    Intent i = new Intent(MySerivcesActivity.this, PipedGasBillPaymentActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(i);
                    finish();
                }


            }
        });

        assert btnLandlineBillPayment != null;
        btnLandlineBillPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                internetConnection = new InternetConnection(MySerivcesActivity.this);

                if (internetConnection.isNetworkAvailable(getApplicationContext()) == false) {

                    AlertDialogBoxInfo.alertDialogShow(MySerivcesActivity.this, getResources().getString(R.string.internetCheck));

                } else {
                    Intent i = new Intent(MySerivcesActivity.this, LandlineBillPayment.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(i);
                    finish();
                }


            }
        });

    }


    public void onBackPressed() {

        Intent intent = new Intent(MySerivcesActivity.this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(MySerivcesActivity.this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

                break;
        }
        return true;
    }

}
