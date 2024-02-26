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

public class GWRCameraActivity extends AppCompatActivity implements View.OnClickListener {
    private GWRCameraManFragment gwrCameraManFragment;
    private LinearLayout layoutParentCamera;
    private static final String TAG = "GWRActivity";
    Toolbar toolbar;
    private Context context;
    Connection connection;
    private Typeface font;
    private TextView btnSubmitCamera;
    private AsyncSaveWitnessAndCameraInfo asyncSaveWitnessAndCameraInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gwrcamera);
        this.context = this;
        font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        connection = new Connection(getApplicationContext());
        btnSubmitCamera = findViewById(R.id.btnSubmitCamera);
        layoutParentCamera = findViewById(R.id.layoutParentCamera);
        gwrCameraManFragment = (GWRCameraManFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentCamera);
        btnSubmitCamera.setOnClickListener(this);

        toolbar = (Toolbar) findViewById(R.id.toolbarGWR);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null) {
            String title = "Cameraman Engagement";
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + title + "</small>"));
        }

        String gwrData = getIntent().getStringExtra("GWR_CAMERA");
        refreshCamera(gwrData);

    }

    //region refresh camera data
    private void refreshCamera(String jsonData) {

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonData);
            JSONArray camera_man_list = jsonObject.getJSONArray("camera_man_list");
            //Witness List
            Gson gson = new GsonBuilder().create();

            //Camera List

            List<CameraListDto> CameraList = gson.fromJson(camera_man_list.toString(), new TypeToken<ArrayList<CameraListDto>>() {
            }.getType());
            gwrCameraManFragment.refresh(CameraList);


            //TODO: Camera List


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //endregion
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
    public void onClick(View view) {
        int Id = view.getId();
        if (Id == R.id.btnSubmitCamera) {
            int cameramanValidation1 = gwrCameraManFragment.IsCameraManValidated1();
            // int cameramanValidation2 = gwrCameraManFragment.IsCameraManValidated2();

            if (cameramanValidation1 == 0) {
                String jsondataforWitness = gwrCameraManFragment.modelToJsonConvert("cameraman2");
                saveWitnessAndCameraAsynTaskCall(jsondataforWitness);
            }


            /*if (cameramanValidation1 == 0 && cameramanValidation2 == 0) {
                String jsondataforWitness = gwrCameraManFragment.modelToJsonConvert("all");
                saveWitnessAndCameraAsynTaskCall(jsondataforWitness);
            } else if (cameramanValidation1 == 0 && cameramanValidation2 != 0) {
                alertDialogWitnessConfirmation(getResources().getString(R.string.cameraman2), "cameraman2");
            } else if (cameramanValidation2 == 0 && cameramanValidation1 != 0) {
                alertDialogWitnessConfirmation(getResources().getString(R.string.cameraman1), "cameraman1");
            }*/
        }
    }


    private void alertDialogWitnessConfirmation(String message, final String witness) {
        AlertDialog.Builder builder = new AlertDialog.Builder(GWRCameraActivity.this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String jsondataforWitness = gwrCameraManFragment.modelToJsonConvert(witness);
                        saveWitnessAndCameraAsynTaskCall(jsondataforWitness);
                        gwrCameraManFragment.IsValid(witness);
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


    //region save camera data to server
    private void saveWitnessAndCameraAsynTaskCall(String jsndat) {

        asyncSaveWitnessAndCameraInfo = new AsyncSaveWitnessAndCameraInfo(GWRCameraActivity.this, connection.getVkid(), jsndat, new AsyncSaveWitnessAndCameraInfo.Callback() {
            @Override
            public void onResult(String result) {
                try {

                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(GWRCameraActivity.this, getResources().getString(R.string.Warning));
                        return;
                    }

                    if (result.startsWith("OKAY")) {
                        //Handle Response
                        System.out.println(result);
                        StringTokenizer st1 = new StringTokenizer(result, "|");
                        String key = st1.nextToken();
                        String franchiseeData = st1.nextToken();
                        try {
                            refreshCamera(franchiseeData);
                            AlertDialogBoxInfo.alertDialogShow(GWRCameraActivity.this, "Cameraman Information Submitted Successfully.");

                        } catch (Exception e) {
                            e.getMessage();
                        }
                    } else if (result.startsWith("ERROR")) {
                        AlertDialogBoxInfo.alertDialogShow(GWRCameraActivity.this, result + "Details saving failed.");
                    } else {
                        AlertDialogBoxInfo.alertDialogShow(GWRCameraActivity.this, "Details saving failed.");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(GWRCameraActivity.this, getResources().getString(R.string.Warning));
                }

            }
        });
        asyncSaveWitnessAndCameraInfo.execute("");
    }
    //endregion
}
