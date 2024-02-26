package in.vakrangee.franchisee.gwr;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.activity.DashboardActivity;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;

public class GuinessWorldRecordActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "GuinessWorldRecordActivity";
    Toolbar toolbar;
    Connection connection;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alert;
    private Context context;
    private DeprecateHandler deprecateHandler;
    private GWRFragment gwrFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gwr);

        this.context = this;
        connection = new Connection(getApplicationContext());
        deprecateHandler = new DeprecateHandler(context);

        toolbar = (Toolbar) findViewById(R.id.toolbarGWR);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null) {
            String title = getString(R.string.gwr);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + title + "</small>"));
        }

        String gwrData = getIntent().getStringExtra("GWR_ACTIVITY_DETAILS");
        gwrFragment = (GWRFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentGWR);
        gwrFragment.reloadGWRDetails(gwrData);

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
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backPressed();
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
    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();

    }
}
