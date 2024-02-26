package in.vakrangee.franchisee.nextgenfranchiseeapplication.bank_statement;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.vakrangee.franchisee.nextgenfranchiseeapplication.FAPBankDetailFragment;
import in.vakrangee.franchisee.nextgenfranchiseeapplication.R;
import in.vakrangee.supercore.franchisee.commongui.imagegallery.RecyclerViewClickListener;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;

@SuppressLint("LongLogTag")
public class AllBankStatementActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AllBankStatementActivity";
    private Context context;
    private Toolbar toolbar;
    private Button btnAddBankStatement;
    private String APP_ID;
    private static final int ADD_BANK_STATEMENT_ACTIVITY_REQUEST = 100;
    private AsyncGetAllBankStatementDetails asyncGetAllBankStatementDetails = null;
    private AsyncGetSpecificBankStatementDetails asyncGetSpecificBankStatementDetails = null;
    private List<BankStatementDto> bankStatementList;
    private boolean IsEditable = false;
    private RecyclerView recyclerViewBankStmtDetails;
    private TextView txtNoDataMsg;
    private AllBankStatementDetailsAdapter allBankStatementDetailsAdapter;
    private int MAX_STMT_COUNT = 6;
    private String IS_BANK_STMT_VALID = "1";
    private String IS_BANK_STMT_VALID_MSG;
    private String minStartDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_bank_statement);

        //Widgets
        context = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null) {
            String title = "All Bank Statement Details";
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + title + "</small>"));
        }

        APP_ID = getIntent().getStringExtra("APP_ID");

        recyclerViewBankStmtDetails = findViewById(R.id.recyclerViewBankStmtDetails);
        txtNoDataMsg = findViewById(R.id.txtNoDataMsg);
        btnAddBankStatement = findViewById(R.id.btnAddBankStatement);
        btnAddBankStatement.setOnClickListener(this);
        reloadData();
    }

    @Override
    public void onClick(View v) {
        int Id = v.getId();

        if (Id == R.id.btnAddBankStatement) {

            BankStatementDto bankStatementDto = new BankStatementDto();
            bankStatementDto.setBankStmtMinStartDate(minStartDate);

            Intent intent = new Intent(context, AddBankStatementActivity.class);
            intent.putExtra("APP_ID", APP_ID);
            intent.putExtra("BANK_STATEMENT_DETAILS", bankStatementDto);
            startActivityForResult(intent, ADD_BANK_STATEMENT_ACTIVITY_REQUEST);
        }
    }

    @Override
    public void onBackPressed() {
        backPressed();
    }

    public void backPressed() {
        setResult();

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_BANK_STATEMENT_ACTIVITY_REQUEST && resultCode == Activity.RESULT_OK) {
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

    private void reloadData() {
        bankStatementList = new ArrayList<>();
        asyncGetAllBankStatementDetails = new AsyncGetAllBankStatementDetails(context, APP_ID, new AsyncGetAllBankStatementDetails.Callback() {
            @Override
            public void onResult(String result) {
                processData(result);
            }
        });
        asyncGetAllBankStatementDetails.execute("");
    }

    private void processData(String result) {
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

        txtNoDataMsg.setVisibility(View.VISIBLE);
        recyclerViewBankStmtDetails.setVisibility(View.GONE);

        //Reload Data
        if (TextUtils.isEmpty(data)) {
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject == null) {
                return;
            }

            String editable = jsonObject.optString("isEditable");
            IsEditable = (!TextUtils.isEmpty(editable) && editable.equalsIgnoreCase("1")) ? true : false;
            String maxCount = jsonObject.optString("max_bank_stmt");
            MAX_STMT_COUNT = TextUtils.isEmpty(maxCount) ? 0 : Integer.parseInt(maxCount);
            minStartDate = jsonObject.optString("bank_stmt_min_start_date");

            IS_BANK_STMT_VALID = jsonObject.optString("is_bank_stmt_valid");
            IS_BANK_STMT_VALID_MSG = jsonObject.optString("is_bank_stmt_valid_msg");

            JSONArray bank_stmtJSON = jsonObject.optJSONArray("bank_stmt");
            if (bank_stmtJSON == null) {
                return;
            }

            Gson gson = new GsonBuilder().create();
            bankStatementList = gson.fromJson(bank_stmtJSON.toString(), new TypeToken<ArrayList<BankStatementDto>>() {
            }.getType());
            if (bankStatementList != null && bankStatementList.size() > 0) {
                txtNoDataMsg.setVisibility(View.GONE);
                recyclerViewBankStmtDetails.setVisibility(View.VISIBLE);
                allBankStatementDetailsAdapter = new AllBankStatementDetailsAdapter(context, bankStatementList, new RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        BankStatementDto statementDto = bankStatementList.get(position);

                        Intent intent = new Intent(context, AddBankStatementActivity.class);
                        intent.putExtra("APP_ID", APP_ID);
                        intent.putExtra("IS_EDITABLE", IsEditable);
                        intent.putExtra("BANK_STATEMENT_DETAILS", statementDto);
                        startActivityForResult(intent, ADD_BANK_STATEMENT_ACTIVITY_REQUEST);

                    }
                });
                LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                recyclerViewBankStmtDetails.setLayoutManager(layoutManager);
                recyclerViewBankStmtDetails.setItemAnimator(new DefaultItemAnimator());
                recyclerViewBankStmtDetails.setAdapter(allBankStatementDetailsAdapter);

            } else {
                txtNoDataMsg.setVisibility(View.VISIBLE);
                recyclerViewBankStmtDetails.setVisibility(View.GONE);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (asyncGetAllBankStatementDetails != null && !asyncGetAllBankStatementDetails.isCancelled()) {
            asyncGetAllBankStatementDetails.cancel(true);
        }

        if (asyncGetSpecificBankStatementDetails != null && !asyncGetSpecificBankStatementDetails.isCancelled()) {
            asyncGetSpecificBankStatementDetails.cancel(true);
        }

    }

    private void setResult() {
        String isBankAvail = "0";
        if(bankStatementList != null && bankStatementList.size() > 0){
            isBankAvail = "1";
        } else {
            isBankAvail = "0";
        }

        Intent intent = new Intent(context, FAPBankDetailFragment.class);
        intent.putExtra("IS_BANK_AVAILABLE", isBankAvail);
        intent.putExtra("IS_BANK_STM_VALID", IS_BANK_STMT_VALID);
        intent.putExtra("IS_BANK_STM_VALID_MSG", IS_BANK_STMT_VALID_MSG);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
