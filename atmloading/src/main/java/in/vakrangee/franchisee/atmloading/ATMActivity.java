package in.vakrangee.franchisee.atmloading;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.ButterKnife;
import in.vakrangee.supercore.franchisee.commongui.DateTimePickerDialog;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;

public class ATMActivity extends AppCompatActivity {


    private int avaliableBalance;
    private int[] tabIcons = {
            R.drawable.ic_home_white_24dp,
            R.drawable.ic_home_white_24dp
    };
    private int selectedDateTimeId = 0;
    private DateTimePickerDialog dateTimePickerDialog;
    private DateFormat dateFormatter2 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    private DateFormat dateFormatterYMD = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private Date startDate;
    private String strStartDate;
    private String cashLoadingDate, cashLoadingId, ackNo;
    private ATMPageAdapter flmPagerAdapter;
    //Fragments
    private PhysicalLoadingFragment physicalLoadingFragment;
    private CashLoadingFragment cashLoadingFragment;
    private String getPhysicalLoadingDetails, getCashLoadingDetails, statusType;
    private ifcLoadingDatePickerClick ifcLoadingDatePickerClick;
    // private CustomNavigationBar customNavigationBar;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TextView textviewLoadingDate;
    private TextView textviewCashPerReceipt;
    private LinearLayout layoutLoadingDate;
    private Button btnSubmit;
    private LinearLayout layoutFooter;


    public interface ifcLoadingDatePickerClick {
        public void datePickerClick(String date);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atm);
        bindviewId();
        //Initialize data
        ButterKnife.bind(this);
        //setup toolbar
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            String title = getResources().getString(R.string.atm_loading);
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + title + "</small>"));
        }

        //customNavigationBar = new CustomNavigationBar(ATMActivity.this, toolbar);

        viewPager.setOffscreenPageLimit(2);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);


        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {

                page.setAlpha(0f);
                page.setVisibility(View.VISIBLE);

                // Start Animation for a short period of time
                page.animate()
                        .alpha(1f)
                        .setDuration(page.getResources().getInteger(android.R.integer.config_shortAnimTime));
            }
        });

        textviewLoadingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDateTimeId = view.getId();
                showDateTimeDialogPicker(view);
            }
        });

        //set tab layout margin between two tab.
        CommonUtils.wrapTabIndicatorToTitle(tabLayout, 5, 5);

        //if type cash loading  -call Asyntask
       /* if (statusType.equalsIgnoreCase("L")) {
            getATMPhysicalORCashLoadingDetails();
        }*/

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });
        //setupTabIcons();
    }

    private void bindviewId() {
        toolbar = findViewById(R.id.toolbar);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        textviewLoadingDate = findViewById(R.id.textviewLoadingDate);
        textviewCashPerReceipt = findViewById(R.id.textviewCashPerReceipt);
        layoutLoadingDate = findViewById(R.id.layoutLoadingDate);
        btnSubmit = findViewById(R.id.btnSubmit);
        layoutFooter = findViewById(R.id.layoutFooter);

    }

    private void validation() {
        if (statusType.equalsIgnoreCase("P")) {
            String acka = ackNo;
            String date = textviewLoadingDate.getText().toString();
            String cashLoadingData = cashLoadingFragment.saveData();
            String physicalLoadingData = physicalLoadingFragment.saveData();
            System.out.println(physicalLoadingData);
        } else {
            String physicalLoadingData = physicalLoadingFragment.saveData();
            System.out.println(physicalLoadingData);
        }


    }

    private void setupViewPager(final ViewPager viewPager) {
        flmPagerAdapter = new ATMPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(flmPagerAdapter);

        //All Fragments
        physicalLoadingFragment = (PhysicalLoadingFragment) flmPagerAdapter.getRegisteredFragment(viewPager, 0);
        cashLoadingFragment = (CashLoadingFragment) flmPagerAdapter.getRegisteredFragment(viewPager, 1);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                reloadAllTabs(viewPager.getCurrentItem());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // No Use
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                reloadAllTabs(viewPager.getCurrentItem());
            }
        });

    }


    private void getATMPhysicalORCashLoadingDetails() {
        AsyncGetATMPhysicalORCashLoadingDetails asyncGetATMPhysicalORCashLoadingDetails = new AsyncGetATMPhysicalORCashLoadingDetails(ATMActivity.this, cashLoadingId, new AsyncGetATMPhysicalORCashLoadingDetails.Callback() {
            @Override
            public void onResult(String result) {
                if (TextUtils.isEmpty(result)) {
                    AlertDialogBoxInfo.alertDialogShow(ATMActivity.this, result);
                    return;
                }
                if (result.startsWith("ERROR")) {
                    result = result.replace("ERROR|", "");
                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(ATMActivity.this, getResources().getString(R.string.Warning));
                    } else {
                        AlertDialogBoxInfo.alertDialogShow(ATMActivity.this, result);
                    }
                } else if (result.startsWith("OKAY")) {
                    //reload data
                    if (result.startsWith("OKAY")) {
                        result = result.replace("OKAY|", "");
                        //set data
                        //dataSet(result);

                        getPhysicalLoadingDetails = result;
                        getCashLoadingDetails = result;
                        reloadAllTabs(viewPager.getCurrentItem());

                    }
                } else {
                    AlertDialogBoxInfo.alertDialogShow(ATMActivity.this, getResources().getString(R.string.Warning));
                }
            }
        });
        asyncGetATMPhysicalORCashLoadingDetails.execute();
    }


    private void reloadAllTabs(int currentPosition) {

        switch (currentPosition) {

            case 0:
                if (physicalLoadingFragment.getView() != null)
                    physicalLoadingFragment.reload(getPhysicalLoadingDetails);
                // physicalLoadingFragment.reload(getPhysicalLoadingDetails, textviewCashPerReceipt, layoutLoadingDate,layoutFooter);
                break;

            case 1:
                if (cashLoadingFragment.getView() != null)
                    cashLoadingFragment.reload(getCashLoadingDetails, String.valueOf(avaliableBalance));
                break;

            default:
                break;
        }
    }


    private void showDateTimeDialogPicker(final View view) {

        Date defaultDate = null;
        if (selectedDateTimeId == R.id.textviewLoadingDate) {
            defaultDate = startDate;
        }

        // Show DateTime Picker Dialog.
        dateTimePickerDialog = new DateTimePickerDialog(ATMActivity.this, true, defaultDate, new DateTimePickerDialog.IDateTimePicker() {
            @Override
            public void getDateTime(Date datetime, String defaultFormattedDateTime) {
                try {
                    String formatedDate = dateFormatter2.format(datetime);
                    String formateYMD = dateFormatterYMD.format(datetime);
                    Toast.makeText(ATMActivity.this, "Selected DateTime : " + formatedDate, Toast.LENGTH_LONG).show();

                    if (selectedDateTimeId != 0) {
                        TextView textViewDateTime = (TextView) view.findViewById(selectedDateTimeId);
                        textViewDateTime.setText(formateYMD);

                        if (selectedDateTimeId == R.id.textviewLoadingDate) {
                            startDate = datetime;
                            strStartDate = formateYMD;
                            String dateFormate = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MM-yyyy", strStartDate);
                            textviewLoadingDate.setText(formatedDate);
                            ifcLoadingDatePickerClick.datePickerClick(dateFormate);

                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //Set Min and Max Date


        long now = new Date().getTime() - 1000;
        int after4days = (1000 * 60 * 60 * 24 * 4);         //no Of Days
        Calendar c = Calendar.getInstance();
        c.set(1920, 0, 1);//Year,Month -1,Day
        if (selectedDateTimeId == R.id.textviewLoadingDate) {
            dateTimePickerDialog.setMinDate(c.getTimeInMillis());
            dateTimePickerDialog.setMaxDate(now);

        }
        dateTimePickerDialog.setCancelable(false);
        dateTimePickerDialog.setActionButtonName("Save");
        dateTimePickerDialog.show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Date Time DIalog
        if (dateTimePickerDialog != null && dateTimePickerDialog.isShowing()) {
            dateTimePickerDialog.dismiss();
            dateTimePickerDialog = null;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressed();
    }

    public void backPressed() {
      /*  Intent intent = new Intent(this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();*/
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //customNavigationBar.openDrawer();
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof ifcLoadingDatePickerClick) {
            ifcLoadingDatePickerClick = (ifcLoadingDatePickerClick) fragment;
        }
    }

}
