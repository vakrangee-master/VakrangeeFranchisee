package in.vakrangee.franchisee.gwr.gwrwitness_camera;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.activity.DashboardActivity;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.Connection;

public class GWRActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "GWRActivity";
    Toolbar toolbar;
    Connection connection;
    private Context context;
    //  private TextView btnBack;
    //   private TextView btnNext;
    private TextView btnSubmit;
    private Typeface font;
    private TextView txtPaging;
    private GWRWitnessFragment gwrWitnessFragment;
    //private GWRCameraManFragment gwrCameraManFragment;
    private LinearLayout layoutParentWitness;
    private LinearLayout layoutParentCamera;
    private AsyncSaveWitnessAndCameraInfo asyncSaveWitnessAndCameraInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gwr_main_witness_camera);

        this.context = this;
        font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        connection = new Connection(getApplicationContext());

        //Widgets
        layoutParentCamera = findViewById(R.id.layoutParentCamera);
        layoutParentWitness = findViewById(R.id.layoutParentWitness);
        gwrWitnessFragment = (GWRWitnessFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentWitness);
        //gwrCameraManFragment = (GWRCameraManFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentCamera);
        //  btnBack = findViewById(R.id.btnBack);
        //  btnNext = findViewById(R.id.btnNext);
        btnSubmit = findViewById(R.id.btnSubmitWitness);
        //  btnNext.setTypeface(font);
        //  btnNext.setText(new SpannableStringBuilder(context.getResources().getString(R.string.next) + " " + " " + new String(new char[]{0xf054})));
        //  btnBack.setTypeface(font);
        // btnBack.setText(new SpannableStringBuilder(new String(new char[]{0xf053}) + " " + " " + context.getResources().getString(R.string.back)));
        toolbar = (Toolbar) findViewById(R.id.toolbarGWR);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null) {
            String title = "Witness Profile";
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + title + "</small>"));
        }

        txtPaging = findViewById(R.id.txtPaging);
        // btnBack.setOnClickListener(this);
        //   btnNext.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        //main fragment - inside 2 child fragment -1st witness -2nd camera
        String gwrData = getIntent().getStringExtra("GWR_WITNESS");
        refreshWitnessAndCamera(gwrData);

    }

    public void refreshWitnessAndCamera(String jsonData) {

        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(jsonData);
            JSONArray camera_man_list = jsonObject.getJSONArray("camera_man_list");
            JSONArray witness_man_list = jsonObject.getJSONArray("witness_man_list");


            // Gson gson = new GsonBuilder().create();
            //gson.fromJson()


            //Witness List
            Gson gson = new GsonBuilder().create();
            List<WitnessListDto> witnessList = gson.fromJson(witness_man_list.toString(), new TypeToken<ArrayList<WitnessListDto>>() {
            }.getType());
            gwrWitnessFragment.refresh(witnessList);

            //Camera List

          /*  List<CameraListDto> CameraList = gson.fromJson(camera_man_list.toString(), new TypeToken<ArrayList<CameraListDto>>() {
            }.getType());
            gwrCameraManFragment.refresh(CameraList);
*/

            //TODO: Camera List


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();
        if (Id == R.id.btnSubmitWitness) {
            // int witnessValidation = gwrWitnessFragment.IsValid();
            int witnessValidation1 = gwrWitnessFragment.IsWitnessValidated1();
            int witnessValidation2 = gwrWitnessFragment.IsWitnessValidated2();

            if (witnessValidation1 == 0 && witnessValidation2 == 0) {
                String jsondataforWitness = gwrWitnessFragment.modelToJsonConvert("all");
                saveWitnessAndCameraAsynTaskCall(jsondataforWitness);
            } else if (witnessValidation1 == 0 && witnessValidation2 != 0) {
                alertDialogWitnessConfirmation(getResources().getString(R.string.witness2), "witness2");
            } else if (witnessValidation2 == 0 && witnessValidation1 != 0) {
                alertDialogWitnessConfirmation(getResources().getString(R.string.witness1), "witness1");
            }

//
            /*if (witnessValidation1 == 0 || witnessValidation2 == 0) {
                if (witnessValidation1 == 0 && witnessValidation2 == 0) {
                    String jsondataforWitness = gwrWitnessFragment.modelToJsonConvert("all");
                    Toast.makeText(context,"ALL",Toast.LENGTH_SHORT).show();
                    //String jsondataforWitness = gwrWitnessFragment.modelToJsonConvert("all");
                   // saveWitnessAndCameraAsynTaskCall(jsondataforWitness);
                } else if (witnessValidation1 == 0 && witnessValidation2 != 0) {
                   alertDialogWitnessConfirmation(getResources().getString(R.string.witness2), "witness2");
                } else if (witnessValidation1 != 0 && witnessValidation2 == 0) {
                    alertDialogWitnessConfirmation(getResources().getString(R.string.witness1), "witness1");
                }
            }*/

        }
       /* if (Id == R.id.btnBack) { // back button
            layoutParentWitness.setVisibility(View.VISIBLE);
            layoutParentCamera.setVisibility(View.GONE);
            setNextPrevVisibility();
        } else if (Id == R.id.btnNext) { //next to camera fragment
            int witnessValidation = gwrWitnessFragment.IsValid();
            //int witnessValidation = gwrWitnessFragment.IsWitnessValidated();
            if (witnessValidation == 0) {
                layoutParentWitness.setVisibility(View.GONE);
                layoutParentCamera.setVisibility(View.VISIBLE);
                setNextPrevVisibility();
                String jsondataforWitness = gwrWitnessFragment.modelToJsonConvert();
                saveWitnessAndCameraAsynTaskCall(jsondataforWitness);
            }
        }*/

        /*else if (Id == R.id.btnSubmit) { // sumbit camera info
            int cameraManValidation = gwrCameraManFragment.IsValid();
            if (cameraManValidation == 0) {
                String jsondataforWitness = gwrCameraManFragment.modelToJsonConvert();
                saveWitnessAndCameraAsynTaskCall(jsondataforWitness);
            }

        }*/
    }

    private void alertDialogWitnessConfirmation(String message, final String witness) {
        AlertDialog.Builder builder = new AlertDialog.Builder(GWRActivity.this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String jsondataforWitness = gwrWitnessFragment.modelToJsonConvert(witness);
                        saveWitnessAndCameraAsynTaskCall(jsondataforWitness);
                        gwrWitnessFragment.IsValid(witness);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //region save witness and camera data to server
    private void saveWitnessAndCameraAsynTaskCall(String jsndat) {

        asyncSaveWitnessAndCameraInfo = new AsyncSaveWitnessAndCameraInfo(GWRActivity.this, connection.getVkid(), jsndat, new AsyncSaveWitnessAndCameraInfo.Callback() {
            @Override
            public void onResult(String result) {
                try {

                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(GWRActivity.this, getResources().getString(R.string.Warning));
                        return;
                    }

                    if (result.startsWith("OKAY")) {
                        //Handle Response
                        System.out.println(result);
                        StringTokenizer st1 = new StringTokenizer(result, "|");
                        String key = st1.nextToken();
                        String franchiseeData = st1.nextToken();
                        try {
                            refreshWitnessAndCamera(franchiseeData);
                            AlertDialogBoxInfo.alertDialogShow(GWRActivity.this, "Witness Information Submitted Successfully.");

                        } catch (Exception e) {
                            e.getMessage();
                        }
                    } else if (result.startsWith("ERROR")) {
                        AlertDialogBoxInfo.alertDialogShow(GWRActivity.this, result + "Details saving failed.");
                    } else {
                        AlertDialogBoxInfo.alertDialogShow(GWRActivity.this, "Details saving failed.");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(GWRActivity.this, getResources().getString(R.string.Warning));
                }

            }
        });
        asyncSaveWitnessAndCameraInfo.execute("");
    }

    //endregion

  /*  public void setNextPrevVisibility() {
        if (layoutParentCamera.getVisibility() == View.VISIBLE) {
            btnBack.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.GONE);
            txtPaging.setText("2 of 2");
            btnSubmit.setVisibility(View.VISIBLE);

        } else if (layoutParentWitness.getVisibility() == View.VISIBLE) {
            btnBack.setVisibility(View.GONE);
            btnNext.setVisibility(View.VISIBLE);
            txtPaging.setText("1 of 2");
            btnSubmit.setVisibility(View.GONE);
        }
    }*/

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


/*
    public TextView getBackButton() {
        return btnBack;
    }

    public TextView getNextButton() {
        return btnNext;
    }
*/

    public void setPagingText(String txt) {
        txtPaging.setText(txt);
    }

}
