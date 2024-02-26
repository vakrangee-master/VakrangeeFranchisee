package in.vakrangee.franchisee.gwr.witness_statement;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.activity.DashboardActivity;
import in.vakrangee.supercore.franchisee.utils.Connection;

public class GuinnessUploadWitnessStatementActivity extends AppCompatActivity {

    private static final String TAG = "GuinnessUploadWitnessStatementActivity";
    Toolbar toolbar;
    Connection connection;
    private Context context;
    private GWRUploadWitnessStatementFragment gwrWitnessStatementFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_witness_statement);

        this.context = this;
        connection = new Connection(getApplicationContext());

        toolbar = (Toolbar) findViewById(R.id.toolbarGWRUploadWitnessStatement);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null) {
            String title = "Upload Witness Statement";
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + title + "</small>"));
        }

        String gwrData = getIntent().getStringExtra("GWR_UPLOAD_WITNESS_STMT_DETAILS");
        gwrWitnessStatementFragment = (GWRUploadWitnessStatementFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentUploadWitnessStatement);
        gwrWitnessStatementFragment.refreshWitnessStatement(gwrData);
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
}
