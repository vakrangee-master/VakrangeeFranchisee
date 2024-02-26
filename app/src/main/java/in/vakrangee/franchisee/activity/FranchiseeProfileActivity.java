package in.vakrangee.franchisee.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;

public class FranchiseeProfileActivity extends AppCompatActivity {
    Toolbar toolbar;
    InternetConnection internetConnection;

    EditText edtrVKID;
    Button btnAccountStmt;
    ProgressDialog progress;
    Button btnSubmitCustomerProflie, btnClear, btnCancle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        // setContentView(R.layout.activity_franchisee_profile);
        setContentView(R.layout.franchiseeprofile);
        internetConnection = new InternetConnection(getApplicationContext());


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.FranchiseeProfile);

//            final Drawable upArrow = getResources().getDrawable(R.drawable.back);
//            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//            getSupportActionBar().setHomeAsUpIndicator(upArrow);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);


        }

        btnSubmitCustomerProflie = (Button) findViewById(R.id.btnCustomerProflieSubmit);
        btnSubmitCustomerProflie.setTypeface(font);
        btnSubmitCustomerProflie.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " + getResources().getString(R.string.submit)));


        btnClear = (Button) findViewById(R.id.btnCustomerProflieClear);
        btnClear.setTypeface(font);
        btnClear.setText(new SpannableStringBuilder(new String(new char[]{0xf021}) + " " + getResources().getString(R.string.clear)));


        btnCancle = (Button) findViewById(R.id.btnCustomerProflieCancle);
        btnCancle.setTypeface(font);
        btnCancle.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + "  " + getResources().getString(R.string.cancel)));
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FranchiseeProfileActivity.this, TechnicalSupportActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });
    }
/*
        edtrVKID = (EditText) findViewById(R.id.edtVkid);
        edtrVKID.setTypeface(Typeface.SANS_SERIF);

        btnAccountStmt = (Button) findViewById(R.id.btnMyTraction);
        btnAccountStmt.setTypeface(font);

        btnAccountStmt.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " + getResources().getString(R.string.go)));


        btnAccountStmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vkid = edtrVKID.getText().toString().trim().toUpperCase();
                if (vkid.length() != 9) {
                    edtrVKID.setError(getResources().getString(R.string.enter9DigitsVKID));
                } else if (internetConnection.isConnectingToInternet() == false) {

                    AlertDialogBoxInfo.alertDialogShow(getApplicationContext(), getResources().getString(R.string.internetCheck));


                } else {

                    //AsyncSubmitDTH myRequest = new AsyncSubmitDTH();
                    //myRequest.execute();


//                    progress = new ProgressDialog(FranchiseeProfileActivity.this);
//                    progress.setTitle(R.string.accountStmt);
//                    progress.setMessage(getResources().getString(R.string.pleaseWait));
//                    progress.setCancelable(false);
//                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                    progress.show();
//
//
//                    AsyncGetMyTransactions myRequest = new AsyncGetMyTransactions();
//                    myRequest.execute();
                    //Toast.makeText(FranchiseeProfileActivity.this, " Succesfull", Toast.LENGTH_SHORT).show();

                }


            }
        });


    }

    */

    public void onBackPressed() {

        Intent intent = new Intent(FranchiseeProfileActivity.this, TechnicalSupportActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(FranchiseeProfileActivity.this, TechnicalSupportActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();


                break;
        }
        return true;
    }


}
