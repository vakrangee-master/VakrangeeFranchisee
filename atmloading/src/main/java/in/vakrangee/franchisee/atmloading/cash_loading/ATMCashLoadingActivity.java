package in.vakrangee.franchisee.atmloading.cash_loading;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.vakrangee.franchisee.atmloading.ATMLoadingRepository;
import in.vakrangee.franchisee.atmloading.R;
import in.vakrangee.supercore.franchisee.baseutils.BaseAppCompatActivity;
import in.vakrangee.supercore.franchisee.commongui.animation.AnimationHanndler;
import in.vakrangee.supercore.franchisee.commongui.imagegallery.RecyclerViewClickListener;
import in.vakrangee.supercore.franchisee.nextgenfranchiseeapplication.CustomFranchiseeApplicationSpinnerDto;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.GUIUtils;
import in.vakrangee.supercore.franchisee.utils.CustomSearchableSpinner;

public class ATMCashLoadingActivity extends BaseAppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TextView txtNoDataMsg;
    private AsyncGetAllCashLoadingList asyncGetAllCashLoadingList = null;
    private AsyncGetSpecificCashLoadingDetails asyncGetSpecificCashLoadingDetails = null;
    private List<ATMCashLoadingDto> atmCashLoadingList;
    private ATMCashLoadingAdapter atmCashLoadingAdapter;
    private Context context;
    private static final int ADD_ATM_LOADING_ACTIVITY_REQUEST = 100;
    private CustomFranchiseeApplicationSpinnerDto selATMIDDto, selStatusDto;
    private ATMLoadingRepository atmLoadingRepo;
    private List<CustomFranchiseeApplicationSpinnerDto> atmIDList, statusList;
    private GetAllSpinnerData getAllSpinnerData = null;
    private CustomSearchableSpinner spinnerATMID, spinnerStatus;
    private TextView txtATMIDLbl, txtStatusLbl;
    private MaterialButton btnGo;
    private CardView cardViewAddNewLoading;
    private TextView txtSourcedDate, txtSourcedAmount, txtCashLoaded, txtBalanceCash;
    private MaterialButton btnAddNewLoading;
    private PendingCashLoadingDto pendingCashLoadingDto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atm_loading);

        context = this;
        atmLoadingRepo = new ATMLoadingRepository(context);
        selATMIDDto = new CustomFranchiseeApplicationSpinnerDto();
        selStatusDto = new CustomFranchiseeApplicationSpinnerDto();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            toolbar.setTitleTextColor(Color.WHITE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + getResources().getString(R.string.atm_cash_loading) + "</small>"));

        }

        //Widgets
        txtATMIDLbl = findViewById(R.id.txtATMIDLbl);
        txtStatusLbl = findViewById(R.id.txtStatusLbl);
        recyclerView = findViewById(R.id.recyclerView);
        txtNoDataMsg = findViewById(R.id.txtNoDataMsg);
        spinnerATMID = findViewById(R.id.spinnerATMID);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        btnGo = findViewById(R.id.btnGo);

        cardViewAddNewLoading = findViewById(R.id.cardViewAddNewLoading);
        txtSourcedDate = findViewById(R.id.txtSourcedDate);
        txtSourcedAmount = findViewById(R.id.txtSourcedAmount);
        txtCashLoaded = findViewById(R.id.txtCashLoaded);
        txtBalanceCash = findViewById(R.id.txtBalanceCash);
        btnAddNewLoading = findViewById(R.id.btnAddNewLoading);

        btnAddNewLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationHanndler.bubbleAnimation(context, v);

                if (pendingCashLoadingDto == null)
                    return;

                ATMCashLoadingDto atmCashLoadingDto = new ATMCashLoadingDto();
                atmCashLoadingDto.setIsEditable(pendingCashLoadingDto.getIsEditable());
                atmCashLoadingDto.setMinLoadingDate(pendingCashLoadingDto.getMinLoadingDate());
                atmCashLoadingDto.setPendingAmount(pendingCashLoadingDto.getPendingAmount());
                atmCashLoadingDto.setAtmId(pendingCashLoadingDto.getAtmId());
                atmCashLoadingDto.setCashSourcingId(pendingCashLoadingDto.getCashSourcingId());
                atmCashLoadingDto.setRemaining_loading_note_count_c1(pendingCashLoadingDto.getRemaining_loading_note_count_c1());
                atmCashLoadingDto.setRemaining_loading_note_count_c2(pendingCashLoadingDto.getRemaining_loading_note_count_c2());
                atmCashLoadingDto.setRemaining_loading_note_count_c3_c4(pendingCashLoadingDto.getRemaining_loading_note_count_c3_c4());

                Intent intent = new Intent(context, AddATMCashLoadingDetailsActivity.class);
                intent.putExtra("CASH_LOADING_DETAILS", atmCashLoadingDto);
                startActivityForResult(intent, ADD_ATM_LOADING_ACTIVITY_REQUEST);
            }
        });

        TextView[] txtViewsForCompulsoryMark = {txtATMIDLbl, txtStatusLbl};
        GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Validate ATM ID
                if (selATMIDDto == null || TextUtils.isEmpty(selATMIDDto.getId()) || selATMIDDto.getId().equalsIgnoreCase("0")) {
                    AlertDialogBoxInfo.alertDialogShow(context, "Please select ATM ID.");
                    return;
                }

                //Validate Status
                if (selStatusDto == null || TextUtils.isEmpty(selStatusDto.getId()) || selStatusDto.getId().equalsIgnoreCase("0")) {
                    AlertDialogBoxInfo.alertDialogShow(context, "Please select Status.");
                    return;
                }

                //Get All Data
                reloadData();
            }
        });

        getAllSpinnerData = new GetAllSpinnerData();
        getAllSpinnerData.execute("");

    }

    public void reloadData() {

        txtNoDataMsg.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        if (selATMIDDto == null || (TextUtils.isEmpty(selATMIDDto.getId()) || selATMIDDto.getId().equalsIgnoreCase("0"))) {
            Toast.makeText(context, "Please select ATM ID.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerATMID, "Please select ATM ID.", context);
            return;
        }

        //Get All ATM Loading Details
        asyncGetAllCashLoadingList = new AsyncGetAllCashLoadingList(context, selATMIDDto.getId(), "1", new AsyncGetAllCashLoadingList.Callback() {
            @Override
            public void onResult(String result) {
                processResult(result);
            }
        });
        asyncGetAllCashLoadingList.execute("");
    }

    private void processResult(String result) {
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
                    refreshDetails(data);
                }
            } else {
                AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
        }
    }

    private void setPendingLoadingDetails(PendingCashLoadingDto pendingCashLoadingDto) {
        cardViewAddNewLoading.setVisibility(View.VISIBLE);

        String sourcedDate = TextUtils.isEmpty(pendingCashLoadingDto.getSourceDate()) ? "-" : pendingCashLoadingDto.getSourceDate();
        txtSourcedDate.setText(sourcedDate);

        String sourcedAmt = TextUtils.isEmpty(pendingCashLoadingDto.getSourcedAmount()) ? "0" : pendingCashLoadingDto.getSourcedAmount();
        setCommaUsingPlaceValue(sourcedAmt, txtSourcedAmount);

        String cashLoaded = TextUtils.isEmpty(pendingCashLoadingDto.getCashLoaded()) ? "0" : pendingCashLoadingDto.getCashLoaded();
        setCommaUsingPlaceValue(cashLoaded, txtCashLoaded);

        String balCash = TextUtils.isEmpty(pendingCashLoadingDto.getBalanceCash()) ? "0" : pendingCashLoadingDto.getBalanceCash();
        setCommaUsingPlaceValue(balCash, txtBalanceCash);
    }

    private void refreshDetails(String data) {

        //Reload Data
        if (TextUtils.isEmpty(data)) {
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray cashLoadingJSON = jsonObject.optJSONArray("cash_loading_details");
            JSONArray pendingLoadingJSON = jsonObject.optJSONArray("pending_loading_details");

            //Pending Cash Loading
            if (pendingLoadingJSON == null || pendingLoadingJSON.length() == 0) {
                cardViewAddNewLoading.setVisibility(View.GONE);
            } else {
                Gson gson = new GsonBuilder().create();
                JSONObject pendingLoadinObj = pendingLoadingJSON.optJSONObject(0);
                if (pendingLoadinObj == null)
                    cardViewAddNewLoading.setVisibility(View.GONE);
                else {
                    pendingCashLoadingDto = gson.fromJson(pendingLoadinObj.toString(), PendingCashLoadingDto.class);
                    setPendingLoadingDetails(pendingCashLoadingDto);
                }
            }

            //Cash Loading List
            if (cashLoadingJSON == null || cashLoadingJSON.length() == 0) {
                return;
            }

            Gson gson = new GsonBuilder().create();
            atmCashLoadingList = gson.fromJson(cashLoadingJSON.toString(), new TypeToken<ArrayList<ATMCashLoadingDto>>() {
            }.getType());

            if (atmCashLoadingList != null && atmCashLoadingList.size() > 0) {
                txtNoDataMsg.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                atmCashLoadingAdapter = new ATMCashLoadingAdapter(context, atmCashLoadingList, new RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                    /*   boolean isNextAllowed =  (!TextUtils.isEmpty(atmCashLoadingList.get(position).getIsNextAllowed()) && atmCashLoadingList.get(position).getIsNextAllowed().equalsIgnoreCase("1")) ? true : false;

                        if(!isNextAllowed){
                            String msg = "Your Total Cash is already Loaded.";
                            showMessage(msg);
                            return;
                        }*/

                        String atmCashLoadingId = atmCashLoadingList.get(position).getCashLoadingId();
                        if (TextUtils.isEmpty(atmCashLoadingId)) {
                            return;
                        }

                        getSpecificCashLoadingDetail(atmCashLoadingId);

                    }
                });
                LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(atmCashLoadingAdapter);

            } else {
                txtNoDataMsg.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_ATM_LOADING_ACTIVITY_REQUEST && resultCode == Activity.RESULT_OK) {
            try {

                if (data != null) {
                    boolean IsReload = data.getBooleanExtra("PERFORM_RELOAD", false);
                    if (IsReload) {
                        reloadData();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
            }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_serach, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search_main);
        searchItem.setVisible(false);
        MenuItem home = menu.findItem(R.id.action_home_dashborad);
        home.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            backPressed();
        }
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int Id = parent.getId();

        if (Id == R.id.spinnerATMID) {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            CustomFranchiseeApplicationSpinnerDto dto3 = (CustomFranchiseeApplicationSpinnerDto) spinnerATMID.getItemAtPosition(position);
            selATMIDDto = dto3;
            //resetSelection();

            reloadData();

        } else if (Id == R.id.spinnerStatus) {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            CustomFranchiseeApplicationSpinnerDto dto3 = (CustomFranchiseeApplicationSpinnerDto) spinnerStatus.getItemAtPosition(position);
            selStatusDto = dto3;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void resetSelection() {
        atmCashLoadingList = new ArrayList<>();

        txtNoDataMsg.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        if (atmCashLoadingAdapter != null) {
            atmCashLoadingAdapter.resetData();
        }
    }

    class GetAllSpinnerData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressSpinner(context);
        }

        @Override
        protected String doInBackground(String... strings) {

            Connection connection = new Connection(context);
            String vkId = connection.getVkid();

            //ATM ID
            atmIDList = atmLoadingRepo.getAllATMList(vkId);

            //Status
            statusList = atmLoadingRepo.getCashLoadingStatusList(vkId);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissProgressSpinner();

            setSpinnerAdapter(atmIDList, spinnerATMID, null);

            setSpinnerAdapter(statusList, spinnerStatus, null);
        }
    }

    private void setSpinnerAdapter(List<CustomFranchiseeApplicationSpinnerDto> list, Spinner spinner, String selectedId) {

        spinner.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, list));
        int pos = atmLoadingRepo.getDefaultOrSelectedChooserData(list, selectedId);
        spinner.setSelection(pos);
        spinner.setOnItemSelectedListener(this);

    }

    private void getSpecificCashLoadingDetail(String atmCashSourcingId) {

        asyncGetSpecificCashLoadingDetails = new AsyncGetSpecificCashLoadingDetails(context, atmCashSourcingId, new AsyncGetSpecificCashLoadingDetails.Callback() {
            @Override
            public void onResult(String result) {
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
                            Gson gson = new Gson();
                            ATMCashLoadingDto cashLoadingDto = gson.fromJson(data, ATMCashLoadingDto.class);

                            Intent intent = new Intent(context, AddATMCashLoadingDetailsActivity.class);
                            intent.putExtra("CASH_LOADING_DETAILS", cashLoadingDto);
                            startActivityForResult(intent, ADD_ATM_LOADING_ACTIVITY_REQUEST);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                }
            }
        });
        asyncGetSpecificCashLoadingDetails.execute("");

    }

    private void setCommaUsingPlaceValue(String value, TextView textView) {

        if (value.equalsIgnoreCase("-"))
            textView.setText(value);
        else {
            BigDecimal bd = new BigDecimal(value);
            long lDurationMillis = bd.setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
            String moneyString1 = new DecimalFormat("##,##,###.##").format(lDurationMillis);
            textView.setText("₹ " + moneyString1);
        }
    }

}
