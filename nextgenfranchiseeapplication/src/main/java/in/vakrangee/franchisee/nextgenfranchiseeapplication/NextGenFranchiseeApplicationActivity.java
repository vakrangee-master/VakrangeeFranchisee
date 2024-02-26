package in.vakrangee.franchisee.nextgenfranchiseeapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import in.vakrangee.franchisee.nextgenfranchiseeapplication.stepstepper.HeaderStepperDTO;
import in.vakrangee.franchisee.nextgenfranchiseeapplication.stepstepper.HeaderStepsFragment;
import in.vakrangee.supercore.franchisee.commongui.CustomWebViewDialog;
import in.vakrangee.supercore.franchisee.model.NextGenFranchiseeApplicationFormDto;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;

public class NextGenFranchiseeApplicationActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    NextGenFranchiseeApplicationFormDto applicationFormDto;
    Connection connection;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alert;
    private Context context;
    private NextGenFranchiseeApplicationFormFragment formFragment;
    private LinearLayout layoutFragment;
    private TextView btnBack;
    private TextView btnNext;
    private TextView txtPaging;
    private CustomScrollView fapScrollview;
    public HeaderStepsFragment headerStepsFragment;
    private CustomWebViewDialog customWebViewDialog = null;
    private Typeface font;
    private static final String PLEASE_SELECT = "Please Select";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nextgen_franchisee_application);

        this.context = this;
        connection = new Connection(getApplicationContext());
        applicationFormDto = (NextGenFranchiseeApplicationFormDto) getIntent().getSerializableExtra("NextGenFranchiseeApplicationDetail");
        font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");

        toolbar = (Toolbar) findViewById(R.id.toolbarFranchiseeApplication);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null) {
            String title = getString(R.string.next_gen_franchisee_application_form);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + title + "</small>"));
        }

        fapScrollview = findViewById(R.id.fapScrollview);
        formFragment = (NextGenFranchiseeApplicationFormFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentFormDetails);
        formFragment.refresh(applicationFormDto, new NextGenFranchiseeApplicationFormFragment.ISwipeAndClicks() {
            @Override
            public void onPosChange(int position, int status) {
                headerStepsFragment.notifyAdapter(position);
            }
        });

        //Show Disclaimer Dialog
        showDisclaimerDialog();

        headerStepsFragment = (HeaderStepsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_steeper_header_fragement);
        headerStepsFragment.refresh(applicationFormDto, new HeaderStepsFragment.IHeaderClicks() {
            @Override
            public void onItemClick(int position, View v, ArrayList<HeaderStepperDTO> headerStepperDTO) {
                formFragment.displayLayout(position);
            }
        });

        btnBack = findViewById(R.id.btnBack);
        btnNext = findViewById(R.id.btnNext);
        txtPaging = findViewById(R.id.txtPaging);
        txtPaging.setText("1 of " + formFragment.MAX_COUNT);
        btnBack.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        btnNext.setTypeface(font);
        btnNext.setText(new SpannableStringBuilder(context.getResources().getString(R.string.next) + " " + " " + new String(new char[]{0xf054})));

        btnBack.setTypeface(font);
        btnBack.setText(new SpannableStringBuilder(new String(new char[]{0xf053}) + " " + " " + context.getResources().getString(R.string.back)));

        layoutFragment = findViewById(R.id.layoutFragment);
        layoutFragment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                formFragment.touchViewFlipper(motionEvent);
                return true;
            }
        });
    }

    public void showDisclaimerDialog() {

        if (TextUtils.isEmpty(applicationFormDto.getStatusDetail()))
            return;

        int agreeStatus = 0;
        try {
            JSONObject jsonObject = new JSONObject(applicationFormDto.getStatusDetail());
            String status = jsonObject.optString("fa_status");
            String disclaimerStatus = jsonObject.optString("fa_disclaimer");
            agreeStatus = (!TextUtils.isEmpty(disclaimerStatus) && disclaimerStatus.equalsIgnoreCase("1")) ? 1 : 0;

            boolean IsEditable = (!TextUtils.isEmpty(status) && status.equalsIgnoreCase("E")) ? true : false;
            if (!IsEditable)
                return;

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (agreeStatus == 1)
            return;

        //Show Disclaimer dialog
        if (customWebViewDialog != null && customWebViewDialog.isShowing()) {
            return;
        }

        customWebViewDialog = new CustomWebViewDialog(NextGenFranchiseeApplicationActivity.this, new CustomWebViewDialog.IOnClick() {
            @Override
            public void onAgreeClick() {
                //Do Nothing
            }

            @Override
            public void onCancelClick() {
                //Go to Dashboard
                backPressed();
            }
        });
        customWebViewDialog.show();
        customWebViewDialog.setCancelable(false);
    }

    public void showMessage(String msg) {
        if (TextUtils.isEmpty(msg))
            return;

        if (alert == null) {
            alertDialogBuilder = new AlertDialog.Builder(context);

            alertDialogBuilder
                    .setMessage(msg)
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

    @Override
    public void onBackPressed() {
        backPressed();
    }

    public void backPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backPressed();
                break;

            default:
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customWebViewDialog != null) {
            customWebViewDialog.dismiss();
            customWebViewDialog = null;

        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();

        if (Id == R.id.btnBack) {
            formFragment.showPrevious();

        } else if (Id == R.id.btnNext) {
            formFragment.showNext();
        }
    }

    public TextView getBackButton() {
        return btnBack;
    }

    public TextView getNextButton() {
        return btnNext;
    }

    public void setScrollViewScrolltop() {
        fapScrollview.scrollTo(0, 0);

    }

    public String getApplicationNo() {
        return applicationFormDto.getNextgenApplicationNo();
    }

    public void setApplicationFormDto(NextGenFranchiseeApplicationFormDto formDto) {
        this.applicationFormDto = formDto;
        if (headerStepsFragment != null)
            headerStepsFragment.refreshApplicationFormDto(formDto);
    }

    public String getAddress() {

        //2. Address Format:comAddress1 + " " + comAddress2 + " " + comLandmark + " " + comVillageCode + " " + comDistrictCode + " " + comStateCode + " ";

        String add1 = null;
        String add2 = null;
        String landMark = null;
        String state = null;
        String district = null;
        String vtc = null;

        try {
            add1 = formFragment.fragmentFAPAddress.editTextAddressLine1.getText().toString().trim();
            if (add1 == null)
                return null;

            add2 = formFragment.fragmentFAPAddress.editTextAddressLine2.getText().toString().trim();
            landMark = formFragment.fragmentFAPAddress.editTextLandmark.getText().toString().trim();
            state = formFragment.fragmentFAPAddress.commspinnerState.getSelectedItem().toString().trim();
            district = formFragment.fragmentFAPAddress.commspinnerDistrict.getSelectedItem().toString().trim();
            vtc = formFragment.fragmentFAPAddress.commspinnerVTC.getSelectedItem().toString().trim();

            //State Check
            if (TextUtils.isEmpty(state) || state.equalsIgnoreCase(PLEASE_SELECT))
                return null;

            //District Check
            if (TextUtils.isEmpty(district) || district.equalsIgnoreCase(PLEASE_SELECT))
                return null;

            //VTC Check
            if (TextUtils.isEmpty(vtc) || vtc.equalsIgnoreCase(PLEASE_SELECT))
                return null;

        } catch (Exception e) {
            e.printStackTrace();
        }

        add2 = add2 == null ? "" : add2;
        landMark = landMark == null ? "" : landMark;
        state = state == null ? "" : state;
        district = district == null ? "" : district;
        vtc = vtc == null ? "" : vtc;

        return add1 + " " + add2 + " " + landMark + " " + state + " " + district + " " + vtc;
    }

    public String getApplicantName() {

        String first = null;
        String middle = null;
        String last = null;

        try {
            first = formFragment.fapFranchiseeDetailFragment.editTextApplicantFirstName.getText().toString().trim();
            if (first == null)
                return null;

            middle = formFragment.fapFranchiseeDetailFragment.editTextApplicantMiddleName.getText().toString().trim();
            last = formFragment.fapFranchiseeDetailFragment.editTextApplicantLastName.getText().toString().trim();

        } catch (Exception e) {
            e.printStackTrace();
        }

        middle = middle == null ? "" : middle;
        last = last == null ? "" : last;

        return CommonUtils.toTitleCase(first + " " + middle + " " + last) + " (Franchisee Application No.: " + getApplicationNo() + ") ";
    }

    public void setPagingText(String txt) {
        txtPaging.setText(txt);
    }

}
