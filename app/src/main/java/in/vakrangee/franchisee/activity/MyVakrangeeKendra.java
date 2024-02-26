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

public class MyVakrangeeKendra extends AppCompatActivity {
    Toolbar toolbar;
    private final static int DELAY = 10;
    private final Handler handler = new Handler();
    private final Timer timer = new Timer();
    int widthg = 0;
    InternetConnection internetConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vakrangee_kendra);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.mykendra);
//
//            final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
//            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//            getSupportActionBar().setHomeAsUpIndicator(upArrow);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        final LinearLayout myvkphoto = (LinearLayout) findViewById(R.id.btnvkphoto);
        final LinearLayout btnCustomerProflie = (LinearLayout) findViewById(R.id.btnCustomerProflie);

        final LinearLayout MyOutlettime = (LinearLayout) findViewById(R.id.btnMyOutletsTiming);
        final LinearLayout Myoutletsa = (LinearLayout) findViewById(R.id.btnChangeMap);
        //  final LinearLayout locationMarker = (LinearLayout) findViewById(R.id.locationMarker);
        final LinearLayout btnMyVK = (LinearLayout) findViewById(R.id.btnmyVK);
        final LinearLayout fieldforce = (LinearLayout) findViewById(R.id.fieldforce);
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


                                LinearLayout layoutl2 = (LinearLayout) findViewById(R.id.l3);

                                assert layoutl2 != null;
                                int width2 = layoutl2.getWidth();
                                widthg = width2;
                                int height2 = layoutl2.getWidth() / 3;
                                int getHeig2 = layoutl2.getHeight();


                                if (getHeig2 < height2) {
                                    LinearLayout.LayoutParams parmsa = new LinearLayout.LayoutParams(width2, height2);
                                    layoutl2.setLayoutParams(parmsa);
//

                                    int heightl = layoutl2.getWidth() / 3;
                                    LinearLayout.LayoutParams parmsl = new LinearLayout.LayoutParams(heightl, heightl);

                                    myvkphoto.setLayoutParams(parmsl);
                                    btnCustomerProflie.setLayoutParams(parmsl);

                                    MyOutlettime.setLayoutParams(parmsl);
                                    Myoutletsa.setLayoutParams(parmsl);
                                    //locationMarker.setLayoutParams(parmsl);
                                    btnMyVK.setLayoutParams(parmsl);
                                    fieldforce.setLayoutParams(parmsl);
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


        assert myvkphoto != null;
        myvkphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MyVakrangeeKendra.this, VkphotoUploadActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //   i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });

        assert btnMyVK != null;
        btnMyVK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                internetConnection = new InternetConnection(MyVakrangeeKendra.this);

                if (internetConnection.isConnectingToInternet() == false) {

                    AlertDialogBoxInfo.alertDialogShow(MyVakrangeeKendra.this, getResources().getString(R.string.internetCheck));


                } else {
                    //Intent i = new Intent(DashboardActivity.this, MyhelpLineActivity.class);
                    Intent i = new Intent(MyVakrangeeKendra.this, MyVakrangeeKendraPhotoActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }


            }
        });


        assert MyOutlettime != null;
        MyOutlettime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                internetConnection = new InternetConnection(MyVakrangeeKendra.this);

                if (internetConnection.isConnectingToInternet() == false) {

                    AlertDialogBoxInfo.alertDialogShow(MyVakrangeeKendra.this, getResources().getString(R.string.internetCheck));


                } else {
                    //Intent i = new Intent(DashboardActivity.this, MyhelpLineActivity.class);
                    Intent i = new Intent(MyVakrangeeKendra.this, MyoutletTimingActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }


            }
        });

        assert fieldforce != null;
        fieldforce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                internetConnection = new InternetConnection(MyVakrangeeKendra.this);

                if (internetConnection.isConnectingToInternet() == false) {

                    AlertDialogBoxInfo.alertDialogShow(MyVakrangeeKendra.this, getResources().getString(R.string.internetCheck));


                } else {
                    //Intent i = new Intent(DashboardActivity.this, MyhelpLineActivity.class);
                    Intent i = new Intent(MyVakrangeeKendra.this, MyFieldForceActivtiy.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }


            }
        });

        assert Myoutletsa != null;
        Myoutletsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                internetConnection = new InternetConnection(MyVakrangeeKendra.this);

                if (internetConnection.isConnectingToInternet() == false) {

                    AlertDialogBoxInfo.alertDialogShow(MyVakrangeeKendra.this, getResources().getString(R.string.internetCheck));


                } else {
                    //Intent i = new Intent(DashboardActivity.this, MyhelpLineActivity.class);
                    Intent i = new Intent(MyVakrangeeKendra.this, MyOutletsActitvity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }


            }
        });

        assert btnCustomerProflie != null;
        btnCustomerProflie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internetConnection = new InternetConnection(MyVakrangeeKendra.this);

                if (internetConnection.isConnectingToInternet() == false) {

                    AlertDialogBoxInfo.alertDialogShow(MyVakrangeeKendra.this, getResources().getString(R.string.internetCheck));


                } else {
                    Intent i = new Intent(MyVakrangeeKendra.this, MyCustomerProflie.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }

            }
        });
    }


    public void onBackPressed() {

        Intent intent = new Intent(MyVakrangeeKendra.this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(MyVakrangeeKendra.this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

                break;
        }
        return true;
    }

}
