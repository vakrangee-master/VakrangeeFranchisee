package in.vakrangee.franchisee.payment_details;

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
import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.commongui.animation.AnimationHanndler;
import in.vakrangee.supercore.franchisee.commongui.imagegallery.RecyclerViewClickListener;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;

public class AllRaisedDisputeDetailsActivity extends AppCompatActivity {

    private Toolbar toolbarPaymentDetails;
    private View view;
    private Context context;
    private RecyclerView recyclerViewAllRaisedDisputeDetails;
    private TextView txtNoDataMsg;
    private AsyncGetAllRaisedDisputeDetails asyncGetAllRaisedDisputeDetails = null;
    private List<RaiseDisputeDto> allRaisedDisputeList;
    private AllRaisedDisputeAdapter allRaisedDisputeAdapter;
    private Button btnNewRaisedRequest;
    private static final int RAISE_REQUEST = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_raised_dispute_details);

        context = this;

        //set toolbar name
        toolbarPaymentDetails = findViewById(R.id.toolbarRaiseDispute);
        setSupportActionBar(toolbarPaymentDetails);
        if (getSupportActionBar() != null) {
            toolbarPaymentDetails.setTitleTextColor(Color.WHITE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + getResources().getString(R.string.all_raised_dispute) + "</small>"));

        }

        recyclerViewAllRaisedDisputeDetails = findViewById(R.id.recyclerViewAllRaisedDisputeDetails);
        txtNoDataMsg = findViewById(R.id.txtNoDataMsg);
        btnNewRaisedRequest = findViewById(R.id.btnNewRaisedRequest);
        btnNewRaisedRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationHanndler.bubbleAnimation(context, v);

                Intent intent = new Intent(context, RaiseDisputeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, RAISE_REQUEST);
            }
        });

        reloadData();
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
        switch (item.getItemId()) {
            case android.R.id.home:
                backPressed();
                break;
            case R.id.action_home_dashborad:
                backPressed();
                break;

            default:
                break;

        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RAISE_REQUEST && resultCode == Activity.RESULT_OK) {
            try {

                if (data != null) {
                    boolean IsReload = data.getBooleanExtra("PERFORM_RELOAD", false);
                    if (IsReload)
                        reloadData();
                }
            } catch (Exception e) {
                e.printStackTrace();
                AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
            }
        }
    }

    public void reloadData() {
        asyncGetAllRaisedDisputeDetails = new AsyncGetAllRaisedDisputeDetails(context, new AsyncGetAllRaisedDisputeDetails.Callback() {
            @Override
            public void onResult(String result) {
                refreshResponse(result);
            }
        });
        asyncGetAllRaisedDisputeDetails.execute("");
    }

    private void refreshResponse(String result) {
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
            JSONArray detailsJSON = jsonObject.getJSONArray("raised_dispute_list");

            if (detailsJSON == null || detailsJSON.length() == 0) {
                return;
            }

            Gson gson = new GsonBuilder().create();
            allRaisedDisputeList = gson.fromJson(detailsJSON.toString(), new TypeToken<ArrayList<RaiseDisputeDto>>() {
            }.getType());

            if (allRaisedDisputeList != null && allRaisedDisputeList.size() > 0) {
                txtNoDataMsg.setVisibility(View.GONE);
                recyclerViewAllRaisedDisputeDetails.setVisibility(View.VISIBLE);
                allRaisedDisputeAdapter = new AllRaisedDisputeAdapter(context, allRaisedDisputeList, new RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        // Do nothing
                    }
                });
                LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                recyclerViewAllRaisedDisputeDetails.setLayoutManager(layoutManager);
                recyclerViewAllRaisedDisputeDetails.setItemAnimator(new DefaultItemAnimator());
                recyclerViewAllRaisedDisputeDetails.setAdapter(allRaisedDisputeAdapter);

            } else {
                txtNoDataMsg.setVisibility(View.VISIBLE);
                recyclerViewAllRaisedDisputeDetails.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (asyncGetAllRaisedDisputeDetails != null && !asyncGetAllRaisedDisputeDetails.isCancelled()) {
            asyncGetAllRaisedDisputeDetails.cancel(true);
        }

    }
}
