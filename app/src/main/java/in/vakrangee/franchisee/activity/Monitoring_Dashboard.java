package in.vakrangee.franchisee.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import in.vakrangee.franchisee.R;

public class Monitoring_Dashboard extends AppCompatActivity {
    Toolbar toolbar;
    Button btnMoreDetails;
    AlertDialog.Builder alertdialogbuilder;

    String[] AlertDialogItems = new String[]{
            "Tech Live",
            "Cancelled",
            "Testing",
            "Work In progress",
            "Operation"
    };

    String ATmList = "Operation,Tech Live,Cancelled,Testing,Work In progress";
    List<String> ItemsIntoList;

    boolean[] Selectedtruefalse = new boolean[]{
            false,
            false,
            false,
            false,
            false,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fontawesome-webfont.ttf");

        setContentView(R.layout.monitoring__dashboard);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnMoreDetails = (Button) findViewById(R.id.btnMoreDetails);
        btnMoreDetails.setTypeface(font);
        btnMoreDetails.setText("  " + new SpannableStringBuilder(new String(new char[]{0xf05d}) + "  " + getResources().getString(R.string.moredetails)) + " ");


        if (getSupportActionBar() != null) {
            toolbar.setTitleTextColor(Color.WHITE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.monitoring_dashboard);

//            final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
//            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//            getSupportActionBar().setHomeAsUpIndicator(upArrow);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        btnMoreDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Monitoring_Dashboard.this, MoreDetailsActivity.class);
                startActivity(intent);
            }
        });

        final TextView txtvalue = (TextView) findViewById(R.id.txtvalue);


        txtvalue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertdialogbuilder = new AlertDialog.Builder(Monitoring_Dashboard.this);

                ItemsIntoList = Arrays.asList(AlertDialogItems);

                alertdialogbuilder.setMultiChoiceItems(AlertDialogItems, Selectedtruefalse, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                    }
                });

                alertdialogbuilder.setCancelable(false);

                alertdialogbuilder.setTitle("Select Here");

                alertdialogbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        txtvalue.setText("");
                        int a = 0;
                        boolean value = false;
                        while (a < Selectedtruefalse.length) {
                            value = Selectedtruefalse[a];

                            if (value) {

                                txtvalue.setText(txtvalue.getText() + ItemsIntoList.get(a) + " ,");

                            }


                            a++;

                        }


                    }
                });

                alertdialogbuilder.setNeutralButton("ALL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Selectedtruefalse = new boolean[5];
                        Arrays.fill(Selectedtruefalse, false);
                        txtvalue.setText(ATmList);


                    }
                });

                AlertDialog dialog = alertdialogbuilder.create();

                dialog.show();
            }
        });

    }


    public void onBackPressed() {

        Intent intent = new Intent(Monitoring_Dashboard.this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(Monitoring_Dashboard.this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();


                break;
        }
        return true;
    }
}
