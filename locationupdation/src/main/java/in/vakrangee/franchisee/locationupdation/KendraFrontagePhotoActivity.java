package in.vakrangee.franchisee.locationupdation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import butterknife.ButterKnife;
import in.vakrangee.supercore.franchisee.application.VakrangeeKendraApplication;
import in.vakrangee.supercore.franchisee.baseutils.BaseAppCompatActivity;
import in.vakrangee.supercore.franchisee.commongui.FileUtils;
import in.vakrangee.supercore.franchisee.commongui.animation.AnimationHanndler;
import in.vakrangee.supercore.franchisee.nextgenfranchiseeapplication.CustomFranchiseeApplicationSpinnerDto;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.CustomFaceDetectionImageDialog;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.FaceDetectionImageDto;
import in.vakrangee.supercore.franchisee.utils.GPSTracker;
import in.vakrangee.supercore.franchisee.utils.GUIUtils;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;
import in.vakrangee.supercore.franchisee.utils.CustomSearchableSpinner;

public class KendraFrontagePhotoActivity extends BaseAppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "KendraFrontagePhotoActivity";
    private Toolbar toolbarKendraPhoto;
    private Context context;
    private PermissionHandler permissionHandler;
    private Uri picUri;
    private static final int CAMERA_PIC_REQUEST = 1;
    private String latitude = "", longitude = "", currentTimestamp = "";
    private GPSTracker gpsTracker;
    private CustomFaceDetectionImageDialog customFaceDetectionImageDialog;
    //private View view;
    private DeprecateHandler deprecateHandler;
    private String mCurrentPhotoPath;


    private List<CustomFranchiseeApplicationSpinnerDto> stateList;
    private List<CustomFranchiseeApplicationSpinnerDto> districtList;
    private List<CustomFranchiseeApplicationSpinnerDto> VTCList;
    private GetAllAddressDetailSpinnerData getAllAddressDetailSpinnerData = null;
    private KendraVerificationRepository kendraVerificationRepository;
    private KendraSavePhotoDto saveLocationDto;
    private FaceDetectionImageDto detectionImageDto;
    private AsyncGetSpectificFranchiseeDetails asyncGetSpectificFranchiseeDetails;

    private TextView textFranchiseeName;
    private TextView textVKID;
    private EditText editTextAddressLine1;
    private EditText editTextAddressLine2;
    private EditText editTextLandmark;
    private EditText editTextPincode;
    private LinearLayout layoutCapturePhoto;
    private ImageView imgCaptureImage;
    private Button btnCancel;
    private Button btnOK;
    private TextView txtMobileIcon;
    private LinearLayout layoutMobileNo;
    private LinearLayout layoutEmailId;
    private TextView txtEmailIcon;
    private TextView txtAddressLine1Lbl;
    private TextView txtStateLbl;
    private TextView txtDistrictLbl;
    private TextView txtVTCLbl;
    private TextView txtPincodeLbl;
    private TextView textImagelbl;
    private ImageView profile_image;
    private CustomSearchableSpinner commspinnerState;
    private CustomSearchableSpinner commspinnerDistrict;
    private CustomSearchableSpinner commspinnerVTC;
    private AsyncSaveKendraLocationUpdation asyncSaveKendraLocationUpdation = null;
    private LinearLayout layoutFooter, layoutMainEMP;
    private String VKID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kendra_frontage_photo);

        bindViewId();
        final Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        //Initialize data
        ButterKnife.bind(this);
        context = KendraFrontagePhotoActivity.this;
        Connection connection = new Connection(context);
        VKID = connection.getVkid();

        permissionHandler = new PermissionHandler(this);
        gpsTracker = new GPSTracker(KendraFrontagePhotoActivity.this);
        deprecateHandler = new DeprecateHandler(KendraFrontagePhotoActivity.this);
        kendraVerificationRepository = new KendraVerificationRepository(KendraFrontagePhotoActivity.this);

        //Widget
        btnCancel.setTypeface(font);
        btnCancel.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + "  " + getResources().getString(R.string.cancel)));

        btnOK.setTypeface(font);
        btnOK.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + "  " + getResources().getString(R.string.save)));

        toolbarKendraPhoto = (Toolbar) findViewById(R.id.toolbarKendraPhoto);
        setSupportActionBar(toolbarKendraPhoto);
        if (getSupportActionBar() != null) {
            toolbarKendraPhoto.setTitleTextColor(Color.WHITE);
            String title = getResources().getString(R.string.kendra_verification_updation);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + title + "</small>"));
        }

        CommonUtils.InputFiletrWithMaxLength(editTextAddressLine1, "~#^|$%&*!", 35);
        CommonUtils.InputFiletrWithMaxLength(editTextAddressLine2, "~#^|$%&*!", 35);
        CommonUtils.InputFiletrWithMaxLength(editTextLandmark, "~#^|$%&*!", 35);

        GUIUtils.CompulsoryMark(txtAddressLine1Lbl, "Address ");
        GUIUtils.CompulsoryMark(txtStateLbl, "State ");
        GUIUtils.CompulsoryMark(txtDistrictLbl, "District ");
        GUIUtils.CompulsoryMark(txtVTCLbl, "Village/Town/City ");
        GUIUtils.CompulsoryMark(txtPincodeLbl, "Pincode ");
        GUIUtils.CompulsoryMark(textImagelbl, "Please Capture Frontage Image ");

        detectionImageDto = new FaceDetectionImageDto();


        layoutCapturePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePreviewDialog((Object) detectionImageDto, v);
            }
        });


        //set franchisee data
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AnimationHanndler.bubbleAnimation(KendraFrontagePhotoActivity.this, v);

                int status = IsLocationDetailsValidated();
                if (status != 0)
                    return;

                //Save
                saveKendraLocationDetails();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //anynctask get information
        getFranchiseeDetails();
    }

    private void bindViewId() {
        //Communication Address
        layoutFooter = findViewById(R.id.layoutFooter);
        layoutMainEMP = findViewById(R.id.layoutMainEMP);
        textFranchiseeName = findViewById(R.id.textFranchiseeName);
        textVKID = findViewById(R.id.textVKID);
        editTextAddressLine1 = findViewById(R.id.editTextAddressLine1);
        editTextAddressLine2 = findViewById(R.id.editTextAddressLine2);
        editTextLandmark = findViewById(R.id.editTextLandmark);
        editTextPincode = findViewById(R.id.editTextPincode);
        layoutCapturePhoto = findViewById(R.id.layoutCapturePhoto);
        imgCaptureImage = findViewById(R.id.imgCaptureImage);
        btnCancel = findViewById(R.id.btnCancel);
        btnOK = findViewById(R.id.btnOK);
        txtMobileIcon = findViewById(R.id.txtMobileIcon);
        layoutMobileNo = findViewById(R.id.layoutMobileNo);
        layoutEmailId = findViewById(R.id.layoutEmailId);
        txtEmailIcon = findViewById(R.id.txtEmailIcon);
        //LBL
        txtAddressLine1Lbl = findViewById(R.id.txtAddressLine1Lbl);
        txtStateLbl = findViewById(R.id.txtStateLbl);
        txtDistrictLbl = findViewById(R.id.txtDistrictLbl);
        txtVTCLbl = findViewById(R.id.txtVTCLbl);
        txtPincodeLbl = findViewById(R.id.txtPincodeLbl);
        textImagelbl = findViewById(R.id.textImagelbl);
        profile_image = findViewById(R.id.profile_image);

        textFranchiseeName = findViewById(R.id.textFranchiseeName);
        textVKID = findViewById(R.id.textVKID);
        editTextAddressLine1 = findViewById(R.id.editTextAddressLine1);
        editTextAddressLine2 = findViewById(R.id.editTextAddressLine2);
        editTextLandmark = findViewById(R.id.editTextLandmark);
        editTextPincode = findViewById(R.id.editTextPincode);
        layoutCapturePhoto = findViewById(R.id.layoutCapturePhoto);
        imgCaptureImage = findViewById(R.id.imgCaptureImage);
        btnCancel = findViewById(R.id.btnCancel);
        btnOK = findViewById(R.id.btnOK);
        txtMobileIcon = findViewById(R.id.txtMobileIcon);
        layoutMobileNo = findViewById(R.id.layoutMobileNo);
        layoutEmailId = findViewById(R.id.layoutEmailId);
        txtEmailIcon = findViewById(R.id.txtEmailIcon);
        //LBL
        txtAddressLine1Lbl = findViewById(R.id.txtAddressLine1Lbl);
        txtStateLbl = findViewById(R.id.txtStateLbl);
        txtDistrictLbl = findViewById(R.id.txtDistrictLbl);
        txtVTCLbl = findViewById(R.id.txtVTCLbl);
        txtPincodeLbl = findViewById(R.id.txtPincodeLbl);
        textImagelbl = findViewById(R.id.textImagelbl);
        profile_image = findViewById(R.id.profile_image);
        commspinnerState = findViewById(R.id.spinnerState);
        commspinnerDistrict = findViewById(R.id.spinnerDistrict);
        commspinnerVTC = findViewById(R.id.spinnerVTC);

        //Pincode
        editTextPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int len = editTextPincode.getText().toString().trim().length();
                if (len <= 0)
                    return;

                if (i != 6) {
                    editTextPincode.setTextColor(Color.parseColor("#000000"));
                    editTextPincode.setError(getResources().getString(R.string.EnterPincode));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                if (editTextPincode.getText().toString().contains(" ")) {
                    editTextPincode.setText(editTextPincode.getText().toString().replaceAll(" ", ""));
                    editTextPincode.setSelection(editTextPincode.getText().length());
                }

                if (editTextPincode.length() >= 6) {
                    editTextPincode.setTextColor(Color.parseColor("#468847"));
                    editTextPincode.setError(null);

                }
            }
        });
    }

    private void getFranchiseeDetails() {
        asyncGetSpectificFranchiseeDetails = new AsyncGetSpectificFranchiseeDetails(KendraFrontagePhotoActivity.this, new AsyncGetSpectificFranchiseeDetails.Callback() {
            @Override
            public void onResult(String result) {
                try {
                    if (result.startsWith("ERROR|")) {
                        result = result.replace("ERROR|", "");
                        if (TextUtils.isEmpty(result)) {
                            AlertDialogBoxInfo.alertDialogShow(KendraFrontagePhotoActivity.this, getResources().getString(R.string.Warning));
                        } else {
                            AlertDialogBoxInfo.alertDialogShow(KendraFrontagePhotoActivity.this, result);
                        }
                    } else if (result.startsWith("OKAY")) {
                        result = result.replace("OKAY|", "");
                        reloadData(result);
                    } else {
                        AlertDialogBoxInfo.alertDialogShow(KendraFrontagePhotoActivity.this, getResources().getString(R.string.Warning));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        asyncGetSpectificFranchiseeDetails.execute();
    }

    private int IsLocationDetailsValidated() {
        saveLocationDto.setAddressline1(editTextAddressLine1.getText().toString());
        saveLocationDto.setAddressline2(editTextAddressLine2.getText().toString());
        saveLocationDto.setLandmark(editTextLandmark.getText().toString());
        saveLocationDto.setPincode(editTextPincode.getText().toString());
        saveLocationDto.setFr_vkid(saveLocationDto.getFr_vkid());
        saveLocationDto.setFortageImage(detectionImageDto.getImage_base64());
        saveLocationDto.setFortageImageExtension(detectionImageDto.getExt());

        //STEP 1: Address Line 1
        if (TextUtils.isEmpty(saveLocationDto.getAddressline1())) {
            editTextAddressLine1.setError("Please enter Address Line 1.");
            return 1;
        }

        if (saveLocationDto.getAddressline1().trim().length() < 3) {
            Toast.makeText(context, "Please enter minimum 3 characters in Address Line 1.", Toast.LENGTH_LONG).show();
            editTextAddressLine1.setError("Please enter minimum 3 characters in Address Line 1.");
            return 1;
        }

        //STEP 2: Address Line 2
        /*if (TextUtils.isEmpty(saveLocationDto.getAddressline2())) {
            editTextAddressLine2.setError("Please enter Address Line 2.");
            return 2;
        }*/

        if (!TextUtils.isEmpty(saveLocationDto.getAddressline2())) {
            if (saveLocationDto.getAddressline2().trim().length() < 3) {
                Toast.makeText(context, "Please enter minimum 3 characters in Address Line 2.", Toast.LENGTH_LONG).show();
                editTextAddressLine2.setError("Please enter minimum 3 characters in Address Line 2.");
                return 1;
            }
        }

        //STEP 3: Landmark
        if (TextUtils.isEmpty(saveLocationDto.getLandmark())) {
            editTextLandmark.setError("Please enter Landmark.");
            return 3;
        }

        if (saveLocationDto.getLandmark().trim().length() < 3) {
            Toast.makeText(context, "Please enter minimum 3 characters in LandMark.", Toast.LENGTH_LONG).show();
            editTextLandmark.setError("Please enter minimum 3 characters in LandMark.");
            return 1;
        }

        //STEP 5: State
        if (TextUtils.isEmpty(saveLocationDto.getStateId()) || saveLocationDto.getStateId().equalsIgnoreCase("0")) {
            GUIUtils.setErrorToSpinner(commspinnerState, "Please select State.", KendraFrontagePhotoActivity.this);
            return 5;
        }

        //STEP 6: District
        if (TextUtils.isEmpty(saveLocationDto.getDistrictId()) || saveLocationDto.getDistrictId().equalsIgnoreCase("0")) {
            GUIUtils.setErrorToSpinner(commspinnerDistrict, "Please select District.", KendraFrontagePhotoActivity.this);
            return 6;
        }

        //STEP 7: Village/Town/City
        if (TextUtils.isEmpty(saveLocationDto.getVtcId()) || saveLocationDto.getVtcId().equalsIgnoreCase("0")) {
            GUIUtils.setErrorToSpinner(commspinnerVTC, "Please select Village/Town/City.", KendraFrontagePhotoActivity.this);
            return 7;
        }

        //STEP 4: Pincode
        if (TextUtils.isEmpty(saveLocationDto.getPincode()) || saveLocationDto.getPincode().trim().length() < 6) {
            Toast.makeText(context, "Please enter proper Pin code.", Toast.LENGTH_LONG).show();
            editTextPincode.setError("Please enter proper Pin code.");
            return 1;
        }

        int isPinCodeStartValid = isPinCodeStartValid(String.valueOf(saveLocationDto.getPincode().charAt(0)));
        if (isPinCodeStartValid != 0) {
            Toast.makeText(context, "Invalid Pin Code.", Toast.LENGTH_LONG).show();
            editTextPincode.setError("Invalid Pin Code.");
            return 1;
        }

        //STEP 8: frontage image
        if (TextUtils.isEmpty(saveLocationDto.getFrontageImageId()) || saveLocationDto.getFrontageImageId().equalsIgnoreCase("0")) {
            if (TextUtils.isEmpty(saveLocationDto.getFortageImage())) {
                Toast.makeText(KendraFrontagePhotoActivity.this, "Please add Frontage Image.", Toast.LENGTH_SHORT).show();
                return 1;
            }
        }

        //STEP 9: accuracy
        if (gpsTracker.getLocationAccuracy() >= Float.parseFloat(saveLocationDto.getAccuracy())) {
            String msg = context.getString(R.string.not_valid_location);
            showMessage(msg);
            return 9;
        }
        return 0;
    }

    //set fontawesome
    public void setFontawesomeIcon(TextView textView, String icon) {
        textView.setText(icon);
        textView.setTextSize(12);
        textView.setTextColor(deprecateHandler.getColor(R.color.iGrey));
        textView.setTypeface(VakrangeeKendraApplication.font_awesome, Typeface.BOLD);

    }
    //endregion

    //region startCamera
    public void startCamera(View view) {

        //If the app has not the permission then asking for the permission
        permissionHandler.requestPermission(view, Manifest.permission.CAMERA, getString(R.string.needs_permission_camera_msg), new IPermission() {
            @Override
            public void IsPermissionGranted(boolean IsGranted) {
                if (IsGranted) {
                    try {
                        dispatchTakePictureIntent();
                    } catch (Exception e) {
                        e.getMessage();
                        e.printStackTrace();
                    }
                }
            }
        });

    }
    //endregion

    //region dispatch Take Picture Intent
    private void dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile(); //CommonUtils.getOutputMediaFile(1);
            } catch (Exception ex) {
                ex.printStackTrace();
                // Error occurred while creating the File
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                //picUri = Uri.fromFile(createImageFile());
                picUri = FileProvider.getUriForFile(KendraFrontagePhotoActivity.this, getApplicationContext().getPackageName() + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
                startActivityForResult(takePictureIntent, CAMERA_PIC_REQUEST);
            }
        }

    }
    //endregion

    //region create Image File
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }
    //endregion

    //region check front camera support or not
    @SuppressLint("LongLogTag")
    private Camera openFrontFacingCameraGingerbread() {
        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    cam = Camera.open(camIdx);
                } catch (RuntimeException e) {
                    Log.e(TAG, "Camera failed to open: " + e.toString());
                }
            }
        }

        return cam;
    }
    //endregion

    //region onActivityResult for camera
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK) {
            try {
                Uri imageUri = Uri.parse(mCurrentPhotoPath);
                String facedetectionCount = TextUtils.isEmpty(saveLocationDto.getFaceDetectionCount()) ? "0" : saveLocationDto.getFaceDetectionCount();

                //------------face detection count -----
                int faceCount = customFaceDetectionImageDialog.faceCount(KendraFrontagePhotoActivity.this, imageUri);
                if (faceCount >= Integer.parseInt(facedetectionCount)) {
                    getLocationAndTimestamp();
                    Bitmap bitmapNew = ImageUtils.getBitmap(getContentResolver(), imageUri, latitude, longitude, currentTimestamp);
                    //BitMap with TimeStamp on it
                    bitmapNew = ImageUtils.stampWithTimeInBitmap(bitmapNew);

                    ImageView imageView = new ImageView(KendraFrontagePhotoActivity.this);
                    imageView.setImageBitmap(bitmapNew);
                    String imageBase64 = ImageUtils.updateExifData(imageUri, bitmapNew, latitude, longitude, currentTimestamp);
                    detectionImageDto.setImage_base64(imageBase64);
                    detectionImageDto.setExt(FileUtils.getFileExtension(KendraFrontagePhotoActivity.this, picUri));
                    detectionImageDto.setId(null);
                    saveLocationDto.setFrontageImageId(null);
                    if (customFaceDetectionImageDialog != null && customFaceDetectionImageDialog.isShowing()) {
                        customFaceDetectionImageDialog.refresh(detectionImageDto);
                        return;
                    }
                } else {
                    AlertDialogBoxInfo.alertDialogShow(KendraFrontagePhotoActivity.this, "No Face is detected. Kindly, capture Selfie at frontage of Vakrangee Kendra.");
                }

            } catch (Exception e) {
                e.printStackTrace();
                AlertDialogBoxInfo.alertDialogShow(KendraFrontagePhotoActivity.this, getResources().getString(R.string.Warning));
            }
        }
    }
    //endregion

    //region get location timestamp  - GPStracker lat and lng
    public void getLocationAndTimestamp() {
        //Get Current location and time stamp
        if (gpsTracker.canGetLocation()) {
            latitude = String.valueOf(gpsTracker.getLatitude());
            longitude = String.valueOf(gpsTracker.getLongitude());
            currentTimestamp = gpsTracker.getFormattedDateTime();
        }
    }
    //endregion

    //region showImagePreviewDialog
    private void showImagePreviewDialog(Object object, final View view) {

        if (customFaceDetectionImageDialog != null && customFaceDetectionImageDialog.isShowing()) {
            customFaceDetectionImageDialog.refresh(detectionImageDto);
            return;
        }

        if (detectionImageDto != null) {
            customFaceDetectionImageDialog = new CustomFaceDetectionImageDialog(KendraFrontagePhotoActivity.this,
                    detectionImageDto, "KendraVerificationUpdation", new CustomFaceDetectionImageDialog.IImageFaceDetectionDialogClicks() {
                @Override
                public void capturePhotoClick() {
                    startCamera(view);
                    //Toast.makeText(context, "Capture", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void OkClick(Object object) {
                    if (TextUtils.isEmpty(detectionImageDto.getImage_base64())) {
                        Toast.makeText(KendraFrontagePhotoActivity.this, "Please Capture Photo", Toast.LENGTH_SHORT).show();
                    } else {
                        Bitmap faceBitmap = CommonUtils.StringToBitMap(detectionImageDto.getImage_base64());
                        imgCaptureImage.setImageBitmap(faceBitmap);
                        customFaceDetectionImageDialog.dismiss();
                    }
                }

                @Override
                public void cancelClick() {
                    //if cancel icon click - remove image
                    //detectionImageDto = new FaceDetectionImageDto();
                }
            });
            customFaceDetectionImageDialog.show();
            customFaceDetectionImageDialog.setDialogTitle("Kendra Frontage Photo");
            customFaceDetectionImageDialog.allowImageTitle(true, "To update your Vakrangee Kendra Location, please take a selfie in front of Vakrangee Kendra covering full frontage of outlet including signboard and your face.");
            customFaceDetectionImageDialog.setCancelable(false);
        } else {
            Toast.makeText(this, "No photo available to preview.", Toast.LENGTH_SHORT).show();
        }


    }
    //endregion

    @Override
    public void onBackPressed() {
        backPressed();
    }

    public void backPressed() {
        try {
            Intent myIntent = new Intent(KendraFrontagePhotoActivity.this, Class.forName("in.vakrangee.franchisee.activity.DashboardActivity"));
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(myIntent);
            finish();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
/*
        Intent intent = new Intent(KendraFrontagePhotoActivity.this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();*/

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
    public void onDestroy() {
        super.onDestroy();
        if (getAllAddressDetailSpinnerData != null && !getAllAddressDetailSpinnerData.isCancelled()) {
            getAllAddressDetailSpinnerData.cancel(true);
        }

    }

    //region get reload data
    public void reloadData(String data) {

        //Reload Data
        if (TextUtils.isEmpty(data))
            saveLocationDto = new KendraSavePhotoDto();
        else {
            try {

                JSONArray jsonArray = new JSONArray(data);
                if (jsonArray == null || jsonArray.length() == 0) {
                    saveLocationDto = new KendraSavePhotoDto();
                } else {

                    Gson gson = new GsonBuilder().create();
                    List<KendraSavePhotoDto> attendanceList = gson.fromJson(data, new TypeToken<ArrayList<KendraSavePhotoDto>>() {
                    }.getType());
                    if (attendanceList.size() > 0) {
                        saveLocationDto = attendanceList.get(0);

                    } else {
                        saveLocationDto = new KendraSavePhotoDto();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //set mobile no
        String mobileNo = TextUtils.isEmpty(saveLocationDto.getMobileNo()) ? "" : saveLocationDto.getMobileNo();
        if (TextUtils.isEmpty(mobileNo)) {
            txtMobileIcon.setVisibility(View.INVISIBLE);
        } else {
            setFontawesomeIcon(txtMobileIcon, getResources().getString(R.string.fa_call));
            layoutMobileNo.setTag(mobileNo);
            layoutMobileNo.setVisibility(View.VISIBLE);
        }

        //click on mobile
        layoutMobileNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + saveLocationDto.getMobileNo()));
                startActivity(intent);
            }
        });

        //set email
        //Email Id
        String emailId = TextUtils.isEmpty(saveLocationDto.getEmailId()) ? "" : saveLocationDto.getEmailId();
        if (TextUtils.isEmpty(emailId)) {
            txtEmailIcon.setVisibility(View.INVISIBLE);
        } else {

            setFontawesomeIcon(txtEmailIcon, getResources().getString(R.string.fa_mail));
            layoutEmailId.setTag(emailId);
            layoutEmailId.setVisibility(View.VISIBLE);

        }

        //click email id icon
        if (TextUtils.isEmpty(saveLocationDto.getEmailId())) {
            layoutEmailId.setVisibility(View.GONE);
        } else {
            layoutEmailId.setVisibility(View.VISIBLE);
        }
        layoutEmailId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", saveLocationDto.getEmailId(), null));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Kendra Frontage Photo");
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
            }
        });

        textFranchiseeName.setText(TextUtils.isEmpty(saveLocationDto.getName()) ? "" : saveLocationDto.getName());
        textVKID.setText(TextUtils.isEmpty(saveLocationDto.getFr_vkid()) ? "" : saveLocationDto.getFr_vkid());

        editTextAddressLine1.setText(TextUtils.isEmpty(saveLocationDto.getAddressline1()) ? "" : saveLocationDto.getAddressline1());
        editTextAddressLine2.setText(TextUtils.isEmpty(saveLocationDto.getAddressline2()) ? "" : saveLocationDto.getAddressline2());
        editTextLandmark.setText(TextUtils.isEmpty(saveLocationDto.getLandmark()) ? "" : saveLocationDto.getLandmark());
        editTextPincode.setText(TextUtils.isEmpty(saveLocationDto.getPincode()) ? "" : saveLocationDto.getPincode());
        detectionImageDto.setId(TextUtils.isEmpty(saveLocationDto.getFrontageImageId()) ? "" : saveLocationDto.getFrontageImageId());

        boolean IsPDF = (saveLocationDto.getFortageImageExtension() != null && saveLocationDto.getFortageImageExtension().equalsIgnoreCase("pdf")) ? true : false;
        if (IsPDF) {
            Glide.with(context)
                    .load(R.drawable.pdf)
                    .override(200, 200)
                    .apply(new RequestOptions()
                            .error(R.drawable.pdf)
                            .placeholder(R.drawable.pdf)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgCaptureImage);

        } else {
            if (!TextUtils.isEmpty(saveLocationDto.getFrontageImageId())) {
                String gstUrl = Constants.DownloadImageUrl + saveLocationDto.getFrontageImageId();
                Glide.with(context)
                        .load(gstUrl)
                        .apply(new RequestOptions()
                                .error(R.drawable.ic_camera_alt_black_72dp)
                                .placeholder(R.drawable.ic_camera_alt_black_72dp)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))
                        .into(imgCaptureImage);
            }
        }

        //profile image
        if (!TextUtils.isEmpty(saveLocationDto.getFrProfileId())) {
            String imageUrl = Constants.DownloadImageUrl + saveLocationDto.getFrProfileId();

            Glide.with(KendraFrontagePhotoActivity.this)
                    .load(imageUrl)
                    .apply(new RequestOptions()
                            .error(R.drawable.profile5)
                            .placeholder(R.drawable.profile5)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(profile_image);
        } else {
            Glide.with(KendraFrontagePhotoActivity.this).asDrawable().load(deprecateHandler.getDrawable(R.drawable.profile5)).into(profile_image);
        }


        getAllAddressDetailSpinnerData = new GetAllAddressDetailSpinnerData(null, null);
        getAllAddressDetailSpinnerData.execute("STATE");

        //Enable/disable views
        boolean IsEditable = (!TextUtils.isEmpty(saveLocationDto.getIsEditable()) && saveLocationDto.getIsEditable().equalsIgnoreCase("1")) ? true : false;
        GUIUtils.setViewAndChildrenEnabled(layoutMainEMP, IsEditable);
        if (IsEditable) {
            layoutFooter.setVisibility(View.VISIBLE);
        } else {
            layoutFooter.setVisibility(View.GONE);
        }

    }
    //endregion

    //region get and bind spinner data
    private class GetAllAddressDetailSpinnerData extends AsyncTask<String, Void, String> {

        private String stateId;
        private String districtId;
        private String type;

        public GetAllAddressDetailSpinnerData(String stateId, String districtId) {
            this.stateId = stateId;
            this.districtId = districtId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressSpinner(context);
        }

        @Override
        protected String doInBackground(String... strings) {
            type = strings[0];
            switch (type.toUpperCase()) {

                case "STATE":
                    //STEP 1: State List
                    stateList = kendraVerificationRepository.getAllStateBylList(VKID);
                    break;

                case "COM_DISTRICT":
                    districtList = kendraVerificationRepository.getAllDistrictBylList(VKID, stateId);
                    break;
                case "COM_VTC":
                    VTCList = kendraVerificationRepository.getAllVTCBylList(VKID, stateId, districtId);
                    break;

                default:
                    break;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissProgressSpinner();

            switch (type.toUpperCase()) {

                case "STATE":
                    BindSpineer();
                    break;
                case "COM_DISTRICT":
                    setDistrictSpinnerAdapter(districtList, commspinnerDistrict, saveLocationDto.getDistrictId());
                    break;
                case "COM_VTC":
                    setVTCSpinnerAdapter(VTCList, commspinnerVTC, saveLocationDto.getVtcId());
                    break;

                default:
                    break;
            }
        }
    }
    //endregion

    //region bind spinner - State
    private void BindSpineer() {
        //STEP 1 : Comm State
        spinner_focusablemode(commspinnerState);
        spinner_focusablemode(commspinnerDistrict);
        spinner_focusablemode(commspinnerVTC);

        commspinnerState.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(KendraFrontagePhotoActivity.this, android.R.layout.simple_spinner_dropdown_item, stateList));
        int appsstatePos = kendraVerificationRepository.getSelectedPos(stateList, saveLocationDto.getStateId());
        commspinnerState.setSelection(appsstatePos);
        commspinnerState.setOnItemSelectedListener(this);
    }
    //endregion

    private void spinner_focusablemode(CustomSearchableSpinner stateSpinner) {
        stateSpinner.setFocusable(true);
        stateSpinner.setFocusableInTouchMode(true);
    }

    //region onspinner item selected
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int Id = parent.getId();
        if (Id == R.id.spinnerState) {
            if (position > 0) {
                commspinnerState.setTitle("Select State");
                commspinnerState.requestFocus();
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) commspinnerState.getItemAtPosition(position);
                if (!entityDto.getId().equals("0")) {
                    saveLocationDto.setStateId(entityDto.getId());

                    //Get District
                    getAllAddressDetailSpinnerData = new GetAllAddressDetailSpinnerData(saveLocationDto.getStateId(), null);
                    getAllAddressDetailSpinnerData.execute("COM_DISTRICT");

                }
            } else {
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

                //STATE
                saveLocationDto.setStateId(null);

                //DISTRICT
                saveLocationDto.setDistrictId(null);
                setSpinnerAdapter(list1, commspinnerDistrict, null);

                //VTC
                saveLocationDto.setVtcId(null);
                setSpinnerAdapter(list1, commspinnerVTC, null);

            }
        } else if (Id == R.id.spinnerDistrict) {
            if (position > 0) {
                commspinnerDistrict.setTitle("Select District");
                commspinnerDistrict.requestFocus();
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) commspinnerDistrict.getItemAtPosition(position);
                if (!entityDto.getId().equals("0")) {
                    saveLocationDto.setDistrictId(entityDto.getId());

                    //Get VTC
                    getAllAddressDetailSpinnerData = new GetAllAddressDetailSpinnerData(saveLocationDto.getStateId(), saveLocationDto.getDistrictId());
                    getAllAddressDetailSpinnerData.execute("COM_VTC");

                }
            } else {
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

                //DISTRICT
                saveLocationDto.setDistrictId(null);

                //VTC
                saveLocationDto.setVtcId(null);
                setSpinnerAdapter(list1, commspinnerVTC, null);
            }
        } else if (Id == R.id.spinnerVTC) {
            commspinnerVTC.setTitle("Select VTC");
            commspinnerVTC.requestFocus();
            CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) commspinnerVTC.getItemAtPosition(position);
            saveLocationDto.setVtcId(entityDto.getId());

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    //endregion

    //region Set Adapter
    private void setSpinnerAdapter(List<CustomFranchiseeApplicationSpinnerDto> spinnerList, Spinner spinner, String selId) {

        //spinnerState.setEnabled(true);
        spinner.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, spinnerList));
        int pos = kendraVerificationRepository.getSelectedPos(spinnerList, selId);
        spinner.setSelection(pos);
        spinner.setOnItemSelectedListener(this);

    }
    //endregion

    //region Set District Adapter
    private void setDistrictSpinnerAdapter(List<CustomFranchiseeApplicationSpinnerDto> districtList, Spinner spinnerDistrict, String selDistrictId) {

        spinnerDistrict.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(KendraFrontagePhotoActivity.this, android.R.layout.simple_spinner_dropdown_item, districtList));
        int appsstatePos = kendraVerificationRepository.getSelectedPos(districtList, selDistrictId);
        spinnerDistrict.setSelection(appsstatePos);
        spinnerDistrict.setOnItemSelectedListener(this);
    }
    //endregion

    //region Set VTC Adapter
    private void setVTCSpinnerAdapter(List<CustomFranchiseeApplicationSpinnerDto> VTCList, Spinner spinnerVTC, String selVTCId) {

        spinnerVTC.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(KendraFrontagePhotoActivity.this, android.R.layout.simple_spinner_dropdown_item, VTCList));
        int appsstatePos = kendraVerificationRepository.getSelectedPos(VTCList, selVTCId);
        spinnerVTC.setSelection(appsstatePos);
        spinnerVTC.setOnItemSelectedListener(this);
    }
    //endregion

    private void saveKendraLocationDetails() {

        getLocationAndTimestamp();
        saveLocationDto.setLatitude(latitude);
        saveLocationDto.setLongitude(longitude);

        //Save
        asyncSaveKendraLocationUpdation = new AsyncSaveKendraLocationUpdation(KendraFrontagePhotoActivity.this, saveLocationDto, new AsyncSaveKendraLocationUpdation.Callback() {
            @Override
            public void onResult(String result) {
                try {
                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                        return;
                    }

                    //Handle Response
                    if (result.startsWith("ERROR|")) {
                        result = result.replace("ERROR|", "");

                        String msg = TextUtils.isEmpty(result) ? context.getString(R.string.Warning) : result;
                        AlertDialogBoxInfo.alertDialogShow(context, msg);
                        return;
                    }

                    if (result.startsWith("OKAY|")) {
                        String msg = "Kendra Location Updation Details saved Successfully.";
                        showMessage(msg);

                        //Handle Response
                        String data = result.replace("OKAY|", "");
                        reloadData(result);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                }
            }
        });

        asyncSaveKendraLocationUpdation.execute("");
    }

    private int isPinCodeStartValid(String pinCodeFirstDigit) {
        if (!Pattern.matches("[1-9]+", pinCodeFirstDigit)) {
            return 1;
        }
        return 0;
    }
}
