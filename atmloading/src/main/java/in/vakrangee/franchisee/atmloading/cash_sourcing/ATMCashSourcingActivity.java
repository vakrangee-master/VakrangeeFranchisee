package in.vakrangee.franchisee.atmloading.cash_sourcing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.vakrangee.franchisee.atmloading.R;
import in.vakrangee.franchisee.atmloading.cash_loading.AddATMCashLoadingDetailsActivity;
import in.vakrangee.supercore.franchisee.baseutils.BaseAppCompatActivity;
import in.vakrangee.supercore.franchisee.commongui.imagegallery.RecyclerViewClickListener;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;

public class ATMCashSourcingActivity extends BaseAppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TextView txtNoDataMsg;
    private MaterialButton btnAddCashSource;
    private AsyncGetAllCashSourcingDetails asyncGetAllCashSourcingDetails = null;
    private List<ATMCashSourcingDto> atmCashSourcingList;
    private ATMCashSourcingAdapter atmCashSourcingAdapter;
    private Context context;
    private static final int ADD_ATM_SOURCING_ACTIVITY_REQUEST = 100;
    private boolean IsNewSourceEnabled = false;
    private String minSourceDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atm_cash_sourcing);

        context = this;

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            toolbar.setTitleTextColor(Color.WHITE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + getResources().getString(R.string.atm_cash_sourcing) + "</small>"));

        }

        //Widgets
        recyclerView = findViewById(R.id.recyclerView);
        txtNoDataMsg = findViewById(R.id.txtNoDataMsg);
        btnAddCashSource = findViewById(R.id.btnAddCashSource);

        //Listeners
        btnAddCashSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!IsNewSourceEnabled) {
                    String msg = "You cannot add a new source as you have pending cash loading.";
                    showMessage(msg);
                    return;
                }

                ATMCashSourcingDto sourcingDto = new ATMCashSourcingDto();
                sourcingDto.setMinSourceDate(minSourceDate);
                sourcingDto.setIsEditable("1");

                Intent intent = new Intent(context, AddATMCashSourcingDetailsActivity.class);
                intent.putExtra("CASH_SOURCING_DETAILS", sourcingDto);
                startActivityForResult(intent, ADD_ATM_SOURCING_ACTIVITY_REQUEST);
            }
        });

        reloadData();
    }

    public void reloadData() {
        asyncGetAllCashSourcingDetails = new AsyncGetAllCashSourcingDetails(context, new AsyncGetAllCashSourcingDetails.Callback() {
            @Override
            public void onResult(String result) {
                processResult(result);
            }
        });
        asyncGetAllCashSourcingDetails.execute("");
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

    private void refreshDetails(String data) {
        //Reload Data
        if (TextUtils.isEmpty(data)) {
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray cashSourcingJSON = jsonObject.getJSONArray("cash_sourcing_details");
            String isAllowNewSourcing = jsonObject.optString("is_allow_new_sourcing");
            minSourceDate = jsonObject.optString("min_source_date");
            IsNewSourceEnabled = (!TextUtils.isEmpty(isAllowNewSourcing) && isAllowNewSourcing.equalsIgnoreCase("1")) ? true : false;

            if (cashSourcingJSON == null || cashSourcingJSON.length() == 0) {
                return;
            }

            Gson gson = new GsonBuilder().create();
            atmCashSourcingList = gson.fromJson(cashSourcingJSON.toString(), new TypeToken<ArrayList<ATMCashSourcingDto>>() {
            }.getType());

            if (atmCashSourcingList != null && atmCashSourcingList.size() > 0) {
                txtNoDataMsg.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                atmCashSourcingAdapter = new ATMCashSourcingAdapter(context, atmCashSourcingList, new RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent intent = new Intent(context, AddATMCashSourcingDetailsActivity.class);
                        intent.putExtra("CASH_SOURCING_DETAILS", atmCashSourcingList.get(position));
                        startActivityForResult(intent, ADD_ATM_SOURCING_ACTIVITY_REQUEST);
                    }
                });
                LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(atmCashSourcingAdapter);

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
        if (requestCode == ADD_ATM_SOURCING_ACTIVITY_REQUEST && resultCode == Activity.RESULT_OK) {
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

}
