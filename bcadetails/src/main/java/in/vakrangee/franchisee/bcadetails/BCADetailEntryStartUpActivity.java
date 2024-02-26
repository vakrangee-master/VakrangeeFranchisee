package in.vakrangee.franchisee.bcadetails;

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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

import in.vakrangee.franchisee.nextgenfranchiseeapplication.CustomScrollView;
import in.vakrangee.franchisee.nextgenfranchiseeapplication.stepstepper.HeaderStepperDTO;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;

public class BCADetailEntryStartUpActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "BCADetailEntryStartUpActivity";
    private Context context;
    private BCADetailEntryStartUpFragment bcaDetailEntryStartUpFragment;
    Toolbar toolbar;
    private BCAEntryDetailsDto bcaEntryDetailsDto;
    Connection connection;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alert;
    private LinearLayout layoutFragment;
    private TextView btnBack;
    private TextView btnNext;
    private TextView txtPaging;
    private CustomScrollView fapScrollview;
    public BCAHeaderStepperFragment headerStepsFragment;
    private Typeface font;
    private DeprecateHandler deprecateHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bca_detail_startup);

        //Widgets
        context = this;
        connection = new Connection(getApplicationContext());
        bcaEntryDetailsDto = (BCAEntryDetailsDto) getIntent().getSerializableExtra("BCA_ENTRY_DETAILS");
        deprecateHandler = new DeprecateHandler(context);
        font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");

        toolbar = (Toolbar) findViewById(R.id.toolbarBCADetailEntry);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null) {
            String title = getString(R.string.bca_detail_entry);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + title + "</small>"));
        }

        fapScrollview = findViewById(R.id.fapScrollview);
        bcaDetailEntryStartUpFragment = (BCADetailEntryStartUpFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentBCADetailEntry);
        bcaDetailEntryStartUpFragment.refresh(bcaEntryDetailsDto, new BCADetailEntryStartUpFragment.ISwipeAndClicks() {
            @Override
            public void onPosChange(int position, int status) {
                headerStepsFragment.notifyAdapter(position);
            }
        });

        headerStepsFragment = (BCAHeaderStepperFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_steeper_header_fragement);
        headerStepsFragment.refresh(bcaEntryDetailsDto, new BCAHeaderStepperFragment.IHeaderClicks() {
            @Override
            public void onItemClick(int position, View v, ArrayList<HeaderStepperDTO> headerStepperDTO) {
                bcaDetailEntryStartUpFragment.displayLayout(position);
            }
        });

        btnBack = findViewById(R.id.btnBack);
        btnNext = findViewById(R.id.btnNext);
        txtPaging = findViewById(R.id.txtPaging);
        txtPaging.setText("1 of " + bcaDetailEntryStartUpFragment.MAX_COUNT);
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
                bcaDetailEntryStartUpFragment.touchViewFlipper(motionEvent);
                return true;
            }
        });
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
        /*Intent intent = new Intent(context, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();*/
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backPressed();
                break;
           /* case R.id.action_home_dashborad:
                backPressed();
                break;*/
        }
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();

        if (Id == R.id.btnBack) {
            bcaDetailEntryStartUpFragment.showPrevious();

        } else if (Id == R.id.btnNext) {
            bcaDetailEntryStartUpFragment.showNext();
        }
    }

    public TextView getBackButton() {
        return btnBack;
    }

    public TextView getNextButton() {
        return btnNext;
    }

    public void setScrollViewScrolltop() {
        //fapScrollview.scrollTo(0, 0);
        fapScrollview.smoothScrollTo(0, 0);
    }

    public void setBcaEntryDetailsDto(BCAEntryDetailsDto entryDetailsDto) {
        this.bcaEntryDetailsDto = entryDetailsDto;
        if (headerStepsFragment != null)
            headerStepsFragment.refreshBCAEntryDetailsDto(entryDetailsDto);
    }

    public String getAddress() {
        return "";
    }

    public String getApplicantName() {

        return "";
    }

    public void setPagingText(String txt) {
        txtPaging.setText(txt);
    }
}
