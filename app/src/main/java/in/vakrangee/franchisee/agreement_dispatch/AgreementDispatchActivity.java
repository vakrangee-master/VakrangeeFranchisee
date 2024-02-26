package in.vakrangee.franchisee.agreement_dispatch;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.activity.DashboardActivity;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;

public class AgreementDispatchActivity extends AppCompatActivity {
    private static final String TAG = AgreementDispatchActivity.class.getSimpleName();
    @BindView(R.id.toolbarAgreement)
    Toolbar toolbarAgreement;

    private AgreementDisptachFragment agreementDisptachFragment;
    private AsyncGetAgreementDispatchDetails asyncGetAgreementDispatchDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement_dispatch);


        ButterKnife.bind(this);
        init();

        agreementDisptachFragment = (AgreementDisptachFragment) getSupportFragmentManager().findFragmentById(R.id.agreementDisptachFragment);
        //agreementDisptachFragment.reload("");
        callAsynctask();

    }

    //region call asynctask Get Agreement Dispatch Details
    private void callAsynctask() {
        asyncGetAgreementDispatchDetails = new AsyncGetAgreementDispatchDetails(AgreementDispatchActivity.this, new AsyncGetAgreementDispatchDetails.Callback() {
            @Override
            public void onResult(String result) {
                //result="ERROR|Agreement not Dispatch";
                if (TextUtils.isEmpty(result)) {
                    AlertDialogBoxInfo.alertDialogShow(AgreementDispatchActivity.this, result);
                    return;
                }
                if (result.startsWith("ERROR|")) {
                    result = result.replace("ERROR|", "");
                    // dialogAgreementDisptachNotReceived(AgreementDispatchActivity.this, result);
                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(AgreementDispatchActivity.this, getResources().getString(R.string.Warning));
                    } else {
                        dialogAgreementDisptachNotReceived(AgreementDispatchActivity.this, result);
                        //AlertDialogBoxInfo.alertDialogShow(AgreementDispatchActivity.this, result);
                    }
                } else if (result.startsWith("OKAY")) {
                    agreementDisptachFragment.reload(result);
                } else {
                    AlertDialogBoxInfo.alertDialogShow(AgreementDispatchActivity.this, getResources().getString(R.string.Warning));

                }

            }
        });
        asyncGetAgreementDispatchDetails.execute();
    }
    //endregion

    private void init() {
        //set toolbar name
        setSupportActionBar(toolbarAgreement);
        if (getSupportActionBar() != null) {
            toolbarAgreement.setTitleTextColor(Color.WHITE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + getResources().getString(R.string.agreement_dispatch) + "</small>"));

        }
    }

    @Override
    public void onBackPressed() {
        backPressed();
    }

    public void backPressed() {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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
                Intent intent = new Intent(AgreementDispatchActivity.this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;

        }
        return true;
    }

    //region is not agreement disptach yet
    public void dialogAgreementDisptachNotReceived(final Context context, String message) {
        try {
            final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setMessage(Html.fromHtml(message));
            alertDialog.setCancelable(false);
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                    Intent intent = new Intent(context, DashboardActivity.class);
                    context.startActivity(intent);
                }
            });
            alertDialog.show();
            TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
            textView.setTextSize(12);
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }
    //endregion
}