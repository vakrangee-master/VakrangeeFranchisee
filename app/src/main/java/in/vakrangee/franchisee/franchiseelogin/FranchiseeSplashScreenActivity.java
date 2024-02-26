package in.vakrangee.franchisee.franchiseelogin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.StringTokenizer;

import in.vakrangee.franchisee.BuildConfig;
import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.activity.DashboardActivity;
import in.vakrangee.supercore.franchisee.baseutils.BaseAppCompatActivity;
import in.vakrangee.supercore.franchisee.franchiseelogin.FranchiseeLoginChecksDto;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.utils.Logger;

public class FranchiseeSplashScreenActivity extends BaseAppCompatActivity {

    private static final String TAG = "FranchiseeSplashScreenActivity";
    private Context mContext;
    private Logger logger;
    /**
     * Duration of wait
     **/
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    private AsyncGetVKIDByEnquiryId asyncGetVKIDByEnquiryId = null;
    private FranchiseeAuthenticationRepository franchiseeAuthRepo;
    private Connection connection;
    private AsyncCheckVersion asyncCheckVersion = null;
    private Typeface clanPro;

    private TextView txtVakrangee, txtVersion;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_franchisee_splash);
        clanPro = Typeface.createFromAsset(getAssets(), "ClanPro-Bold.otf");
        //TextView txtVakrangeeKendra = findViewById(R.id.txtVakrangeeKendra);
        // TextView txtFranchisee = findViewById(R.id.txtFranchisee);

        // CommonUtils.setTextStyleFont(txtVakrangeeKendra, clanPro, "Vakrangee Kendra ");
        // CommonUtils.setTextStyleFont(txtFranchisee, clanPro, "Franchisee ");
        final Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "ClanPro-Medium.otf");
        txtVakrangee = findViewById(R.id.txtVakrangee);
        txtVersion = findViewById(R.id.txtVersion);
        txtVersion.setText("Version: " + BuildConfig.VERSION_NAME);

        // txtKendra = findViewById(R.id.txtKendra);
        //txtConnect = findViewById(R.id.txtConnect);

        txtVakrangee.setTypeface(font, Typeface.BOLD_ITALIC);
        // txtKendra.setTypeface(font, Typeface.BOLD_ITALIC);
        //txtConnect.setTypeface(font, Typeface.BOLD_ITALIC);
        //Intialization
        mContext = this;
        connection = new Connection(mContext);
        connection.openDatabase();

        logger = Logger.getInstance(mContext);
        franchiseeAuthRepo = new FranchiseeAuthenticationRepository(mContext);

    }

    @Override
    protected void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!InternetConnection.isNetworkAvailable(mContext)) {
                    showMessage("No Internet Connection.");
                    return;
                }

                //Check Version
                asyncCheckVersion = new AsyncCheckVersion();
                asyncCheckVersion.execute();

            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void validateAndLaunchSpecificScreen() {

        int status = validateFranchiseeLoginData();

        switch (status) {
            case 0:
                if (!InternetConnection.isNetworkAvailable(mContext)) {
                    showMessage("No Internet Connection.");
                    return;
                }

                asyncGetVKIDByEnquiryId = new AsyncGetVKIDByEnquiryId();
                asyncGetVKIDByEnquiryId.execute();
                break;

            case -1:
            case -3:
                Intent intent1 = new Intent(mContext, FranchiseeLoginActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                finish();
                break;

            case -2:
                Intent intent2 = new Intent(mContext, NextGenFranchiseeEnquiryDetailsActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                finish();
                break;
            default:
                break;
        }
    }

    public int validateFranchiseeLoginData() {

        //STEP 1: Check if all details found
        FranchiseeLoginChecksDto loginChecksDto = CommonUtils.getFranchiseeLoginDataFromPreferences(this);
        if (loginChecksDto == null) {
            return -1;
        }

        //STEP 2: Check if OTP is verified
        if (TextUtils.isEmpty(loginChecksDto.getOtpVerified()) || loginChecksDto.getOtpVerified().equalsIgnoreCase("0")) {
            return -1;
        }

        //STEP 3: Check OTP Expiry Time
        if (TextUtils.isEmpty(loginChecksDto.getExpiryTime()) || System.currentTimeMillis() > Long.parseLong(loginChecksDto.getExpiryTime())) {
            return -3;
        }

        //STEP 4: Check if Next Gen Enquiry is selected
        if (TextUtils.isEmpty(loginChecksDto.getNextGenEnquiryId()) || loginChecksDto.getNextGenEnquiryId().equalsIgnoreCase("0")) {
            return -2;
        }
        return 0;
    }

    public class AsyncGetVKIDByEnquiryId extends AsyncTask<Void, String, Void> {

        private String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            FranchiseeLoginChecksDto loginChecksDto = CommonUtils.getFranchiseeLoginDataFromPreferences(mContext);
            if (loginChecksDto == null || loginChecksDto.getNextGenEnquiryId() == null)
                return null;

            response = franchiseeAuthRepo.getVKIDByEnquiryId(loginChecksDto.getNextGenEnquiryId());

            if (!TextUtils.isEmpty(response)) {
                StringTokenizer st1 = new StringTokenizer(response, "\\|");
                String key = st1.nextToken();

                if (key.equalsIgnoreCase("ERROR"))
                    return null;

                String vkId = st1.nextToken();
                //Insert VKID in to table
                connection.deleteTableinfo();
                connection.insertIntoDB(vkId.toUpperCase(), "", "", "");

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //Update Franchisee FCM ID
            new AsyncUpdateFranchiseeFCMIdViaEnquiryId(mContext).execute();

            Intent intent = new Intent(mContext, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    public class AsyncCheckVersion extends AsyncTask<Void, String, Void> {

        private String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            response = franchiseeAuthRepo.checkVersion();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (TextUtils.isEmpty(response)) {
                AlertDialogBoxInfo.alertDialogShow(mContext, getResources().getString(R.string.Warning));
                Log.e("SplashScreenError", "Found Error: " + response);

                return;
            }

            if (response.toUpperCase().startsWith("OKAY"))
                validateAndLaunchSpecificScreen();
            else if (response.toUpperCase().startsWith("INVALID"))
                showDialogMessage();
                //showMessage(getResources().getString(R.string.updateApplication));
            else
                AlertDialogBoxInfo.alertDialogShow(mContext, getResources().getString(R.string.Warning));

        }
    }

    private void showDialogMessage() {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(FranchiseeSplashScreenActivity.this);
        builder1.setMessage(getResources().getString(R.string.updateApplication));
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
        alert11.setCancelable(false);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (asyncCheckVersion != null && !asyncCheckVersion.isCancelled()) {
            asyncCheckVersion.cancel(true);
        }

        if (asyncGetVKIDByEnquiryId != null && !asyncGetVKIDByEnquiryId.isCancelled()) {
            asyncGetVKIDByEnquiryId.cancel(true);
        }
    }
}
