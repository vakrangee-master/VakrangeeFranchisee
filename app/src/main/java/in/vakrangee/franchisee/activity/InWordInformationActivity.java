package in.vakrangee.franchisee.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;

import in.vakrangee.franchisee.R;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class InWordInformationActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    Toolbar toolbar;

    private static final int PERMISSIONS_REQUEST_ACCESS_CAMERA = 0;
    private ZXingScannerView mScannerView;
    Context context;
    String getresult;
    Camera camera;
    int k = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_word_information);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);


        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.inwordinfo);

//            final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
//            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//            getSupportActionBar().setHomeAsUpIndicator(upArrow);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                getApplicationContext().checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    PERMISSIONS_REQUEST_ACCESS_CAMERA);
        } else {
            // mScannerView.startCamera();
        }

        final LinearLayout inward = (LinearLayout) findViewById(R.id.inward);

        inward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScannerView = new ZXingScannerView(InWordInformationActivity.this);   // Programmatically initialize the scanner view
                setContentView(mScannerView);

                mScannerView.setResultHandler(InWordInformationActivity.this); // Register ourselves as a handler for scan results.
                mScannerView.startCamera();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_ACCESS_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mScannerView.startCamera();
                Camera.Parameters params = camera.getParameters();
                if (params.getSupportedFocusModes().contains(
                        Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                    params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                }
                camera.setParameters(params);
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        try {
            mScannerView.stopCamera();
        } catch (Exception e) {
            e.getMessage();
        }

    }

    @Override
    public void handleResult(final Result rawResult) {
        // Do something with the result here
        Log.e("handler", rawResult.getText()); // Prints scan results
        Log.e("handler", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode)


        final String shareResult = rawResult.getText();

        final Dialog dialog = new Dialog(InWordInformationActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.customdialog);

        final ImageView click_ok = (ImageView) dialog.findViewById(R.id.click_ok);
        //final ImageView click_share = (ImageView) dialog.findViewById(R.id.click_share);

        final TextView qrResult = (TextView) dialog.findViewById(R.id.qrResult);
        qrResult.setText(rawResult.getText());

        click_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                mScannerView.resumeCameraPreview(InWordInformationActivity.this);
                dialog.dismiss();
            }
        });

//        click_share.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Intent sendIntent = new Intent();
//                sendIntent.setAction(Intent.ACTION_SEND);
//                sendIntent.putExtra(Intent.EXTRA_TEXT, shareResult);
//                sendIntent.setType("text/plain");
//                getBaseContext().startActivity(sendIntent);
//
//            }
//        });

        dialog.show();
//
//        builder.setMessage(rawResult.getText());
//        AlertDialog alert1 = builder.create();
//        alert1.show();

        // If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

        }
        return super.onKeyDown(keyCode, event);
    }


    public void onBackPressed() {
        k++;
        if (k == 1) {

            Intent i = new Intent(InWordInformationActivity.this, InWordInformationActivity.class);

            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(i);
            finish();

            Toast.makeText(InWordInformationActivity.this, "Please press back arrow <- to back.", Toast.LENGTH_SHORT).show();
        } else {


            Intent i = new Intent(InWordInformationActivity.this, InWordInformationActivity.class);

            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(i);
            finish();
            Log.e("logout App", "onBackPressed");

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(InWordInformationActivity.this, InWordActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();


                break;
        }
        return true;
    }

}
