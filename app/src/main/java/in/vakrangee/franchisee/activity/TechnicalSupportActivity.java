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

public class TechnicalSupportActivity extends AppCompatActivity {
    Toolbar toolbar;
    private final static int DELAY = 10;
    private final Handler handler = new Handler();
    private final Timer timer = new Timer();
    int widthg = 0;
    InternetConnection internetConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technical_support);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.technicalsupport);

//            final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
//            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//            getSupportActionBar().setHomeAsUpIndicator(upArrow);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        final LinearLayout btnWalletRechargeHistory = (LinearLayout) findViewById(R.id.btnWalletRechargeHistory);
        final LinearLayout btnWalletACStatement = (LinearLayout) findViewById(R.id.btnWalletACStatement);
        final LinearLayout btnTransactions = (LinearLayout) findViewById(R.id.btnTransactions);

        final LinearLayout btnFranchiseeProfile = (LinearLayout) findViewById(R.id.btnFranchiseeProfile);
        final LinearLayout btnEmployeeProfileList = (LinearLayout) findViewById(R.id.btnEmployeeProfileList);
        final LinearLayout btnInWord = (LinearLayout) findViewById(R.id.btnInWord);

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


                                LinearLayout layoutl2 = (LinearLayout) findViewById(R.id.l2);


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

                                    if (btnWalletACStatement != null) {
                                        btnWalletACStatement.setLayoutParams(parmsl);
                                    }

                                    btnWalletRechargeHistory.setLayoutParams(parmsl);
                                    btnTransactions.setLayoutParams(parmsl);


                                    btnFranchiseeProfile.setLayoutParams(parmsl);
                                    btnEmployeeProfileList.setLayoutParams(parmsl);
                                    btnInWord.setLayoutParams(parmsl);

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

        assert btnWalletRechargeHistory != null;
        btnWalletRechargeHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                internetConnection = new InternetConnection(TechnicalSupportActivity.this);

                if (internetConnection.isConnectingToInternet() == false) {

                    AlertDialogBoxInfo.alertDialogShow(TechnicalSupportActivity.this, getResources().getString(R.string.internetCheck));


                } else {
                    Intent i = new Intent(TechnicalSupportActivity.this, WalletRechargeHistoryActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(i);
                    finish();
                }


            }
        });


        assert btnWalletACStatement != null;
        btnWalletACStatement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                internetConnection = new InternetConnection(TechnicalSupportActivity.this);

                if (internetConnection.isConnectingToInternet() == false) {

                    AlertDialogBoxInfo.alertDialogShow(TechnicalSupportActivity.this, getResources().getString(R.string.internetCheck));


                } else {
                    Intent i = new Intent(TechnicalSupportActivity.this, AccountStatementActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(i);
                    finish();
                }


            }
        });

        assert btnTransactions != null;
        btnTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                internetConnection = new InternetConnection(TechnicalSupportActivity.this);

                if (internetConnection.isConnectingToInternet() == false) {

                    AlertDialogBoxInfo.alertDialogShow(TechnicalSupportActivity.this, getResources().getString(R.string.internetCheck));


                } else {
                    Intent i = new Intent(TechnicalSupportActivity.this, TransactionReportActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(i);
                    finish();
                }


            }
        });


        assert btnFranchiseeProfile != null;
        btnFranchiseeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                internetConnection = new InternetConnection(TechnicalSupportActivity.this);

                if (internetConnection.isConnectingToInternet() == false) {

                    AlertDialogBoxInfo.alertDialogShow(TechnicalSupportActivity.this, getResources().getString(R.string.internetCheck));


                } else {
                    Intent i = new Intent(TechnicalSupportActivity.this, FranchiseeProfileActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(i);
                    finish();
                }


            }
        });

        assert btnEmployeeProfileList != null;
        btnEmployeeProfileList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                internetConnection = new InternetConnection(TechnicalSupportActivity.this);

                if (internetConnection.isConnectingToInternet() == false) {

                    AlertDialogBoxInfo.alertDialogShow(TechnicalSupportActivity.this, getResources().getString(R.string.internetCheck));


                } else {
                    Intent i = new Intent(TechnicalSupportActivity.this, EmployeeProfileActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(i);
                    finish();
                }


            }
        });


        assert btnInWord != null;
        btnInWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                internetConnection = new InternetConnection(TechnicalSupportActivity.this);

                if (internetConnection.isConnectingToInternet() == false) {

                    AlertDialogBoxInfo.alertDialogShow(TechnicalSupportActivity.this, getResources().getString(R.string.internetCheck));


                } else {
                    Intent i = new Intent(TechnicalSupportActivity.this, InWordActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(i);
                    finish();
                }


            }
        });
    }

    public void onBackPressed() {

        Intent intent = new Intent(TechnicalSupportActivity.this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(TechnicalSupportActivity.this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

                break;
        }
        return true;
    }
}
