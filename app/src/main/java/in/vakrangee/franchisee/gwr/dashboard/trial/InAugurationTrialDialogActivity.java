package in.vakrangee.franchisee.gwr.dashboard.trial;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.gwr.dashboard.GWRDashboardDto;

public class InAugurationTrialDialogActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CustomInAugurationTrialDialog";
    private Context context;
    private LinearLayout parentLayout;
    private Button btnClose;
    private String data;
    private LinearLayout layoutGWRDashboardTrial;
    private GWRDashboardTrialFragment fragmentGWRDashboardTrial;
    private GWRDashboardDto dashboardDto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().setBackgroundDrawable(new ColorDrawable(0));
        //getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        this.context = InAugurationTrialDialogActivity.this;
        final Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        setContentView(R.layout.dialog_inaugurate_trial);

        // fixing portrait mode problem for SDK 26 if using windowIsTranslucent = true
        if (Build.VERSION.SDK_INT == 26) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        //Widgets
        parentLayout = findViewById(R.id.parentLayout);
        //CommonUtils.setDialogWidth(context, parentLayout);
        btnClose = findViewById(R.id.btnClose);
        btnClose.setTypeface(font);
        btnClose.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + "  "));
        btnClose.setOnClickListener(this);
        dashboardDto = (GWRDashboardDto) getIntent().getSerializableExtra("gwrDashboardDto");


        //Get GWR Dashboard Trial Data
        layoutGWRDashboardTrial = findViewById(R.id.layoutGWRDashboardTrial);
        fragmentGWRDashboardTrial = (GWRDashboardTrialFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentGWRDashboardTrial);

        setFinishOnTouchOutside(false);
        //Refresh
        refresh(data);
    }

    public void refresh(String data) {
        this.data = data;

        fragmentGWRDashboardTrial.refresh(dashboardDto);


    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();

        if (Id == R.id.btnClose) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fragmentGWRDashboardTrial != null) {
            fragmentGWRDashboardTrial.stopGWR();
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}


