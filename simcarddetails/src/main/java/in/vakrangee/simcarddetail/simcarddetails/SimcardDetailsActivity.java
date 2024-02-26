package in.vakrangee.simcarddetail.simcarddetails;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import in.vakrangee.simcarddetail.R;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;

public class SimcardDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SimcardDetailsActivity";
    private Toolbar toolbar;
    private LinearLayout layoutSimcard;
    private Typeface font;
    private Context context;
    //All Child Fragments
    private SimcardDetailsFragment simcardDetailsFragment;
    private AsyncGetSimcardDetailsData asyncGetSimcardDetailsData = null;
    private AsyncSaveSimcardDetails asyncSaveSimcardDetails = null;
    private LinearLayout layoutFooter;
    private Button btnSubmitSimcardDetails, btnCancelDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simcard_details);

        //Initialize data
        context = this;

        //Widgets
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null) {
            String title = getResources().getString(R.string.simcardDetails);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + title + "</small>"));
        }
        font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");

        layoutFooter = findViewById(R.id.layoutFooter);
        btnSubmitSimcardDetails = findViewById(R.id.btnSubmitSimcardDetails);
        btnCancelDetails = findViewById(R.id.btnCancelDetails);

        btnSubmitSimcardDetails.setTypeface(font);
        btnSubmitSimcardDetails.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " + getResources().getString(R.string.submit)));
        btnSubmitSimcardDetails.setOnClickListener(this);

        btnCancelDetails.setTypeface(font);
        btnCancelDetails.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " + getResources().getString(R.string.Cancel)));
        btnCancelDetails.setOnClickListener(this);

        //Fragments
        simcardDetailsFragment = (SimcardDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentSimcardDetails);

        //asyntaskCall();
    }


    public void IsFooterLayoutVisible(boolean IsVisible) {
        if (IsVisible)
            layoutFooter.setVisibility(View.VISIBLE);
        else
            layoutFooter.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        finish();
        //super.onBackPressed();
    }

    /*  public void backPressed() {
          finish();

      }
  */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        int Id = v.getId();

        if (Id == R.id.btnSubmitSimcardDetails) {
            //Validate
            int status = simcardDetailsFragment.isValidationData();
            if (status != 0)
                return;

            //Internet Connectivity check
            if (!InternetConnection.isNetworkAvailable(context)) {
                AlertDialogBoxInfo.alertDialogShow(context, "No Internet Connection.");
                return;
            }

            String jsonData = null;
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();

            jsonData = gson.toJson(simcardDetailsFragment.getSimcardDetailsDto(), SimcardDetailsDto.class);

            //base on type save data
            asyncSaveSimcardDetails = new AsyncSaveSimcardDetails(context, jsonData, new AsyncSaveSimcardDetails.Callback() {
                @Override
                public void onResult(String result) {
                    try {
                        reload(result);

                    } catch (Exception e) {
                        e.printStackTrace();
                        AlertDialogBoxInfo.alertDialogShow(SimcardDetailsActivity.this, getResources().getString(R.string.Warning));
                    }
                }
            });

            asyncSaveSimcardDetails.execute("");

        }
        if (Id == R.id.btnCancelDetails) {
            finish();
        }
    }

    public void reload(String result) {
        try {
            if (TextUtils.isEmpty(result)) {
                AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                return;
            }

            if (result.startsWith("ERROR")) {
                String msg = result.replace("ERROR|", "");
                msg = TextUtils.isEmpty(msg) ? "Something went wrong. Please try again later." : msg;
                AlertDialogBoxInfo.alertDialogShow(context, msg);
                return;
            }

            if (result.startsWith("OKAY")) {
                //Handle Response
                String data = result.replace("OKAY|", "");
                if (TextUtils.isEmpty(data))
                    AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                else {
                    AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.simcardDetails_saveData));
                    simcardDetailsFragment.simcardDetailsData(data);
                }
            } else {
                AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (asyncGetSimcardDetailsData != null && !asyncGetSimcardDetailsData.isCancelled()) {
            asyncGetSimcardDetailsData.cancel(true);
        }

        if (asyncSaveSimcardDetails != null && !asyncSaveSimcardDetails.isCancelled()) {
            asyncSaveSimcardDetails.cancel(true);
        }
    }
}
