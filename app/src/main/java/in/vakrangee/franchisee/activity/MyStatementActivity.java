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
import in.vakrangee.franchisee.adapter.MyWalltetViewPager;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;

public class MyStatementActivity extends AppCompatActivity {
    Toolbar toolbar;
    private final static int DELAY = 10;
    private final Handler handler = new Handler();
    private final Timer timer = new Timer();
    int widthg = 0;
    InternetConnection internetConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_statement);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.mystatement);

//            final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
//            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//            getSupportActionBar().setHomeAsUpIndicator(upArrow);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        final LinearLayout MyTraction = (LinearLayout) findViewById(R.id.btnMyTraction);
        final LinearLayout MyRecharge = (LinearLayout) findViewById(R.id.btnMyRecharge);
        final LinearLayout getbillinfo = (LinearLayout) findViewById(R.id.getbillinfo);
        final LinearLayout MyhelpLine = (LinearLayout) findViewById(R.id.btnMyHelpLine);
        final LinearLayout invisible1 = (LinearLayout) findViewById(R.id.invisible1);
        final LinearLayout invisible2 = (LinearLayout) findViewById(R.id.invisible2);
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

                                LinearLayout layoutl1 = (LinearLayout) findViewById(R.id.l3);
                                LinearLayout layoutl2 = (LinearLayout) findViewById(R.id.l2);

                                assert layoutl1 != null;
                                int widtha = layoutl1.getWidth();
                                int heighta = layoutl1.getWidth() / 3;
                                int getHeigh = layoutl1.getHeight();


                                assert layoutl2 != null;
                                int width2 = layoutl2.getWidth();
                                widthg = width2;
                                int height2 = layoutl2.getWidth() / 3;
                                int getHeig2 = layoutl2.getHeight();


                                if (getHeigh < heighta) {
                                    LinearLayout.LayoutParams parmsa = new LinearLayout.LayoutParams(widtha, heighta);
                                    layoutl1.setLayoutParams(parmsa);
//

                                    int heightl = layoutl1.getWidth() / 3;
                                    LinearLayout.LayoutParams parmsl = new LinearLayout.LayoutParams(heightl, heightl);

                                    MyhelpLine.setLayoutParams(parmsl);
                                    invisible1.setLayoutParams(parmsl);
                                    invisible2.setLayoutParams(parmsl);
                                }

                                if (getHeig2 < height2) {
                                    LinearLayout.LayoutParams parmsa = new LinearLayout.LayoutParams(width2, height2);
                                    layoutl2.setLayoutParams(parmsa);
//

                                    int heightl = layoutl2.getWidth() / 3;
                                    LinearLayout.LayoutParams parmsl = new LinearLayout.LayoutParams(heightl, heightl);
                                    assert getbillinfo != null;
                                    getbillinfo.setLayoutParams(parmsl);
                                    if (MyRecharge != null) {
                                        MyRecharge.setLayoutParams(parmsl);
                                    }

                                    MyTraction.setLayoutParams(parmsl);
                                    MyhelpLine.setLayoutParams(parmsl);
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

        assert MyhelpLine != null;
        MyhelpLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                internetConnection = new InternetConnection(MyStatementActivity.this);

                if (internetConnection.isNetworkAvailable(getApplicationContext()) == false) {

                    AlertDialogBoxInfo.alertDialogShow(MyStatementActivity.this, getResources().getString(R.string.internetCheck));

                } else {
                    //Intent i = new Intent(DashboardActivity.this, MyhelpLineActivity.class);
                    Intent i = new Intent(MyStatementActivity.this, MyhelpLineActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }


            }
        });


        assert getbillinfo != null;
        getbillinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                internetConnection = new InternetConnection(MyStatementActivity.this);

                if (internetConnection.isNetworkAvailable(getApplicationContext()) == false) {

                    AlertDialogBoxInfo.alertDialogShow(MyStatementActivity.this, getResources().getString(R.string.internetCheck));

                } else {
                    Intent i = new Intent(MyStatementActivity.this, MyWalltetViewPager.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }


            }
        });

        assert MyTraction != null;
        MyTraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                internetConnection = new InternetConnection(MyStatementActivity.this);


                if (internetConnection.isNetworkAvailable(getApplicationContext()) == false) {

                    AlertDialogBoxInfo.alertDialogShow(MyStatementActivity.this, getResources().getString(R.string.internetCheck));

                } else {
                    Intent i = new Intent(MyStatementActivity.this, MyTransactionActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    // Intent i = new Intent(DashboardActivity.this, MyTransactionActivity.class);
                    startActivity(i);
                    finish();
                }


            }
        });

        assert MyRecharge != null;
        MyRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                internetConnection = new InternetConnection(MyStatementActivity.this);


                if (internetConnection.isNetworkAvailable(getApplicationContext()) == false) {

                    AlertDialogBoxInfo.alertDialogShow(MyStatementActivity.this, getResources().getString(R.string.internetCheck));

                } else {
                    Intent i = new Intent(MyStatementActivity.this, RechargeHistroyActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(i);
                    finish();
                }


            }
        });

    }


    public void onBackPressed() {

        Intent intent = new Intent(MyStatementActivity.this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(MyStatementActivity.this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

                break;
        }
        return true;
    }


}
