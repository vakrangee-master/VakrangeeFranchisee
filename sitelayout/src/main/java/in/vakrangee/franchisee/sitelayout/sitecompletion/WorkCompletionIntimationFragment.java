package in.vakrangee.franchisee.sitelayout.sitecompletion;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import butterknife.ButterKnife;
import in.vakrangee.franchisee.sitelayout.R;
import in.vakrangee.franchisee.sitelayout.activity.MyVakrangeeKendraLocationDetailsNextGen;
import in.vakrangee.supercore.franchisee.commongui.DateTimePickerDialog;
import in.vakrangee.supercore.franchisee.commongui.ImageSliderDialog;
import in.vakrangee.supercore.franchisee.commongui.imagepreview.CustomImagePreviewDialog;
import in.vakrangee.supercore.franchisee.model.FranchiseeDetails;
import in.vakrangee.supercore.franchisee.model.My_vakranggekendra_image;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeviceInfo;
import in.vakrangee.supercore.franchisee.utils.GPSTracker;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;
import in.vakrangee.supercore.franchisee.utils.JSONUtils;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;

@SuppressLint("ValidFragment")
public class WorkCompletionIntimationFragment extends Fragment implements View.OnClickListener {

    private final static String TAG = "WorkCompletionIntimationFragment";

    private DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US); //new SimpleDateFormat("dd-MM-yyyy HH:mm a", Locale.US);
    private DateFormat dateFormatter2 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    private DateFormat dateFormatter3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

    FranchiseeDetails franchiseeDetails;
    View view;

    TextView textViewStartDate;        // Work Completion Data

    RecyclerView recyclerViewCheckList;

    WorkCompletionCheckListAdapter workCompletionCheckListAdapter;
    List<WorkCompletionCheckListDto> workCompletionCheckListDtos;

    TextView switchWorkCompletionPhoto;

    View includeInteriorPhotos;
    ImageView imageView1;                    // Frontage Image
    ImageView imageView2;                    // Left Wall Image
    ImageView imageView3;                    // Front Wall Image
    ImageView imageView4;                    // Right Wall Image
    ImageView imageView5;                    // Back Wall Image
    ImageView imageView6;                    // Ceiling Image
    ImageView imageView7;                    // Floor Image
    CardView cardViewImage8;
    ImageView imageView8;                    // Frontage Opposite Road Side

    Bitmap bitmap1, bitmap2, bitmap3, bitmap4, bitmap5, bitmap6, bitmap7, bitmap8;

    LinearLayout layoutRemarks;
    EditText editTextRemarks;

    // Action
    LinearLayout layoutFooter;
    Button btnSubmit;                 // Save Data
    Button btnCancel;                 // Go to Back
    Button btnClear;                   // Clear Data
    TextView tooltipWorkCompletion;   // Show ErrorMsg
    TextView clickToViewImage;

    // final int CAMERA_CAPTURE = 201;     // REQUEST CODE
    // final int CHANGE_PHOTO = 202;     // REQUEST CODE

    private PermissionHandler permissionHandler;

    private static final int CAMERA_PIC_REQUEST = 111;
    private static final int SLIDER_CAMERA_PIC_REQUEST = 112;
    private static final int PREVIEW_IMAGE_CAPTURE = 113;

    private Uri picUri;                 // Picture URI

    private int selectedImageViewId = 0;
    private int selectedDateTimeId = 0;

    private Date startDate, completionDate;
    private String strStartDate, strCompletionDate;

    private AsyncGetWorkCompletionCheckList asyncGetWorkCompletionCheckList;
    private AsyncUpdateWorkCompletion asyncUpdateWorkCompletion;

    private DeviceInfo deviceInfo;

    private AlphaAnimation btnClickAnim = new AlphaAnimation(1F, 0.6F);

    private List<My_vakranggekendra_image> my_vakranggekendra_images;

    private GPSTracker gpsTracker;
    private String latitude = "", longitude = "", currentTimestamp = "";

    public WorkCompletionIntimationFragment(FranchiseeDetails franchiseeDetails) {
        this.franchiseeDetails = franchiseeDetails;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_nextgen_site_work_completion_fragment, container, false);

        final Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fontawesome-webfont.ttf");
        // Bind GUI
        ButterKnife.bind(this, view);

        permissionHandler = new PermissionHandler(getActivity());
        deviceInfo = DeviceInfo.getInstance(getActivity());
        gpsTracker = new GPSTracker(getContext());

        recyclerViewCheckList = view.findViewById(R.id.recyclerViewCheckList);
        textViewStartDate = view.findViewById(R.id.textViewStartDate);
        switchWorkCompletionPhoto = view.findViewById(R.id.switchCommencementPhoto);
        includeInteriorPhotos = view.findViewById(R.id.includeInteriorPhotos);

        imageView1 = view.findViewById(R.id.imageView1);
        imageView2 = view.findViewById(R.id.imageView2);
        imageView3 = view.findViewById(R.id.imageView3);
        imageView4 = view.findViewById(R.id.imageView4);
        imageView5 = view.findViewById(R.id.imageView5);
        imageView6 = view.findViewById(R.id.imageView6);
        imageView7 = view.findViewById(R.id.imageView7);
        imageView8 = view.findViewById(R.id.imageView8);
        cardViewImage8 = view.findViewById(R.id.cardViewImage8);

        layoutRemarks = view.findViewById(R.id.layoutRemarks);
        editTextRemarks = view.findViewById(R.id.editTextRemarks);

        layoutFooter = view.findViewById(R.id.layoutFooter);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnClear = view.findViewById(R.id.btnClear);
        tooltipWorkCompletion = view.findViewById(R.id.tooltipSiteCommencement);
        clickToViewImage = view.findViewById(R.id.clickToViewImage);

        // Checklist Recycler View
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewCheckList.setLayoutManager(layoutManager);
        recyclerViewCheckList.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCheckList.setNestedScrollingEnabled(false);

        // Add Listener On Switch
        /*switchWorkCompletionPhoto.setOnCheckedChangeListener(null);
        switchWorkCompletionPhoto.setChecked(true);
        switchWorkCompletionPhoto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    includeInteriorPhotos.setVisibility(View.VISIBLE);
                } else {
                    includeInteriorPhotos.setVisibility(View.GONE);
                }
            }
        });*/
        switchWorkCompletionPhoto.setText(Html.fromHtml("<b>" + getResources().getString(R.string.label_work_completion_photos) + "</b>"));

        btnSubmit.setTypeface(font);
        btnSubmit.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + "  " + getResources().getString(R.string.submit)));

        btnClear.setTypeface(font);
        btnClear.setText(new SpannableStringBuilder(new String(new char[]{0xf021}) + "  " + getResources().getString(R.string.clear)));

        btnCancel.setTypeface(font);
        btnCancel.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + "  " + getResources().getString(R.string.cancel)));

        tooltipWorkCompletion.setTypeface(font);
        tooltipWorkCompletion.setText(new SpannableStringBuilder(" " + new String(new char[]{0xf05a}) + " "));

        // Enable Frontage Road Side Image
        cardViewImage8.setVisibility(View.VISIBLE);

        // Add Listeners
        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);
        imageView4.setOnClickListener(this);
        imageView5.setOnClickListener(this);
        imageView6.setOnClickListener(this);
        imageView7.setOnClickListener(this);
        imageView8.setOnClickListener(this);

        clickToViewImage.setOnClickListener(this);
        clickToViewImage.setVisibility(View.GONE);

        textViewStartDate.setOnClickListener(this);

        btnSubmit.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        my_vakranggekendra_images = new ArrayList<My_vakranggekendra_image>();

        // Load Work Completion CheckList
        loadData();

        // Set Already Available Data
        //reloadData();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        permissionHandler.dismissSnackbar();
    }

    public void getLocationAndTimestamp() {
        //Get Current location and time stamp
        if (gpsTracker.canGetLocation()) {
            latitude = String.valueOf(gpsTracker.getLatitude());
            longitude = String.valueOf(gpsTracker.getLongitude());
            currentTimestamp = gpsTracker.getFormattedDateTime();
        }
    }

    //region Load Data of Work Completion Intimation CheckList from Server
    public void loadData() {

        asyncGetWorkCompletionCheckList = new AsyncGetWorkCompletionCheckList(getActivity(), new AsyncGetWorkCompletionCheckList.Callback() {

            @Override
            public void onResult(String result) {

                //Check if response if null or empty
                if (TextUtils.isEmpty(result)) {
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                    return;
                }

                //Process response
                StringTokenizer tokens1 = new StringTokenizer(result, "|");
                String key = tokens1.nextToken();
                String response = tokens1.nextToken();

                if (key.equalsIgnoreCase(Constants.OKAY_RESPONSE)) {

                    /*boolean IsValidJSON = CommonUtils.isJSONValid(result);
                    if (!IsValidJSON) {
                        AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                        return;
                    }*/

                    Gson gson = new GsonBuilder().create();
                    workCompletionCheckListDtos = gson.fromJson(response, new TypeToken<ArrayList<WorkCompletionCheckListDto>>() {
                    }.getType());

                    if (workCompletionCheckListDtos != null) {
                        workCompletionCheckListAdapter = new WorkCompletionCheckListAdapter(getActivity(), workCompletionCheckListDtos);
                        recyclerViewCheckList.setAdapter(workCompletionCheckListAdapter);
                    }
                } else if (key.equalsIgnoreCase(Constants.ERROR_RESPONSE)) {
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), response);
                } else {
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                    return;
                }
            }
        });
        asyncGetWorkCompletionCheckList.execute("");
    }
    //endregion

    //region Reload Data & Images
    /*public void reloadData() {

        boolean isPhotoAvailable = false;
        if (franchiseeDetails != null) {

            //region Load All Image
            // Frontage Image
            if (!TextUtils.isEmpty(franchiseeDetails.getFrontageImageId()) &&
                    !franchiseeDetails.getFrontageImageId().equalsIgnoreCase("null")) {
                bitmap1 = realoadImage(franchiseeDetails.getFrontageImageId(), imageView1);
                isPhotoAvailable = true;
            }
            // Left Wall Image
            if (!TextUtils.isEmpty(franchiseeDetails.getLeftWallImageId()) &&
                    !franchiseeDetails.getLeftWallImageId().equalsIgnoreCase("null")) {
                bitmap2 = realoadImage(franchiseeDetails.getLeftWallImageId(), imageView2);
                isPhotoAvailable = true;
            }
            // Front Wall Image
            if (!TextUtils.isEmpty(franchiseeDetails.getFrontWallImageId()) &&
                    !franchiseeDetails.getFrontWallImageId().equalsIgnoreCase("null")) {
                bitmap3 = realoadImage(franchiseeDetails.getFrontWallImageId(), imageView3);
                isPhotoAvailable = true;
            }
            // Right Wall Image
            if (!TextUtils.isEmpty(franchiseeDetails.getRightWallImageId()) &&
                    !franchiseeDetails.getRightWallImageId().equalsIgnoreCase("null")) {
                bitmap4 = realoadImage(franchiseeDetails.getRightWallImageId(), imageView4);
                isPhotoAvailable = true;
            }
            // Back Wall Image
            if (!TextUtils.isEmpty(franchiseeDetails.getBackWallImageId()) &&
                    !franchiseeDetails.getBackWallImageId().equalsIgnoreCase("null")) {
                bitmap5 = realoadImage(franchiseeDetails.getBackWallImageId(), imageView5);
                isPhotoAvailable = true;
            }
            // Ceiling Image
            if (!TextUtils.isEmpty(franchiseeDetails.getCeilingImageId()) &&
                    !franchiseeDetails.getCeilingImageId().equalsIgnoreCase("null")) {
                bitmap6 = realoadImage(franchiseeDetails.getCeilingImageId(), imageView6);
                isPhotoAvailable = true;
            }
            // Floor Image
            if (!TextUtils.isEmpty(franchiseeDetails.getFloorImageId()) &&
                    !franchiseeDetails.getFloorImageId().equalsIgnoreCase("null")) {
                bitmap7 = realoadImage(franchiseeDetails.getFloorImageId(), imageView7);
                isPhotoAvailable = true;
            }
            // Frontage from 15feet
           *//* if(!TextUtils.isEmpty(franchiseeDetails.getFrontage15feetImageId()) &&
                    franchiseeDetails.getFrontage15feetImageId().equalsIgnoreCase("null")) {
                realoadImage(franchiseeDetails.getFrontage15feetImageId(), imageView8);
            }*//*

            // Photo Available
            *//*if (isPhotoAvailable)
                switchWorkCompletionPhoto.setChecked(true);
            else
                switchWorkCompletionPhoto.setChecked(false);*//*

            //endregion

            try {

                //Set Minimum Commencement Date
                strCommencementMinDate = franchiseeDetails.getCommencementMinDate();
                Log.e(TAG, "Commencement Min Date : " + strCommencementMinDate);
                commencementMinDate = dateFormatter3.parse(strCommencementMinDate);

                // Set Start and Estimated End Date
                strStartDate = franchiseeDetails.getCommencementStartDate();
                if (!TextUtils.isEmpty(strStartDate)) {
                    startDate = dateFormatter.parse(strStartDate);
                    textViewStartDate.setText(dateFormatter2.format(startDate));
                }

                strCompletionDate = franchiseeDetails.getCommencementEstimatedEndDate();
                if (!TextUtils.isEmpty(strCompletionDate)) {
                    completionDate = dateFormatter.parse(strCompletionDate);
                    textViewCompletionDate.setText(dateFormatter2.format(completionDate));
                }

            } catch (ParseException pe) {
                pe.toString();
            }

            */

    /**
     * Work Commencement Status
     * 0 = Data Updated
     * 1 = Site Send Back For Correction
     * 2 = Resubmitted For Nextgen Site Work Commencement
     * 3 = Site Verified
     *//*
            //if(franchiseeDetails.getStatus() != NextGenSiteCommencementViewPager.SITE_SEND_BACK_FOR_CORRECTION) {
            if (!franchiseeDetails.isAllowToEdit()) {
                // Photos
                switchWorkCompletionPhoto.setEnabled(false);
                *//*imageView1.setEnabled(false);
                imageView2.setEnabled(false);
                imageView3.setEnabled(false);
                imageView4.setEnabled(false);
                imageView5.setEnabled(false);
                imageView6.setEnabled(false);
                imageView7.setEnabled(false);*//*

                textViewStartDate.setEnabled(false);
                textViewCompletionDate.setEnabled(false);

                layoutFooter.setVisibility(View.GONE);
            } else {
                layoutFooter.setVisibility(View.VISIBLE);
            }

            // Set Tooltips
            // Check Status and Set Remarks
            if (franchiseeDetails.getStatus() == NextGenSiteCommencementViewPager.SITE_SEND_BACK_FOR_CORRECTION) {
                FranchiseeRemarkDetails franchiseeRemarkDetails = franchiseeDetails.getFranchiseeRemarkDetails();
                if (franchiseeRemarkDetails != null) {

                    // Premise Detail
                    final String commencementTooltip = franchiseeRemarkDetails.getWorkCommencementTooltip();
                    if (!TextUtils.isEmpty(commencementTooltip)) {
                        tooltipWorkCompletion.setVisibility(View.VISIBLE);
                        tooltipWorkCompletion.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                tooltipWorkCompletion.setAnimation(btnClickAnim);
                                PopupUtils.show(getActivity(), v, commencementTooltip);
                            }
                        });
                    }

                }
            }
        }
    }

    private Bitmap realoadImage(String imageId, ImageView imageView) {
        Bitmap img = CommonUtils.StringToBitMap(imageId);
        if (img != null)
            imageView.setImageBitmap(img);
        return img;
    }
*/
    //endregion
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.imageView1) {
            if (TextUtils.isEmpty(franchiseeDetails.getFrontageImageId()) && franchiseeDetails.isAllowToEdit()) {
                capturePhoto(id);
            } else {
                showImageSlider(0);
            }
        } else if (id == R.id.imageView2) {
            if (TextUtils.isEmpty(franchiseeDetails.getLeftWallImageId()) && franchiseeDetails.isAllowToEdit()) {
                capturePhoto(id);
            } else {
                showImageSlider(1);
            }
        } else if (id == R.id.imageView3) {
            if (TextUtils.isEmpty(franchiseeDetails.getFrontWallImageId()) && franchiseeDetails.isAllowToEdit()) {
                capturePhoto(id);
            } else {
                showImageSlider(2);
            }
        } else if (id == R.id.imageView4) {
            if (TextUtils.isEmpty(franchiseeDetails.getRightWallImageId()) && franchiseeDetails.isAllowToEdit()) {
                capturePhoto(id);
            } else {
                showImageSlider(3);
            }
        } else if (id == R.id.imageView5) {
            if (TextUtils.isEmpty(franchiseeDetails.getBackWallImageId()) && franchiseeDetails.isAllowToEdit()) {
                capturePhoto(id);
            } else {
                showImageSlider(4);
            }
        } else if (id == R.id.imageView6) {
            if (TextUtils.isEmpty(franchiseeDetails.getCeilingImageId()) && franchiseeDetails.isAllowToEdit()) {
                capturePhoto(id);
            } else {
                showImageSlider(5);
            }
        } else if (id == R.id.imageView7) {
            if (TextUtils.isEmpty(franchiseeDetails.getFloorImageId()) && franchiseeDetails.isAllowToEdit()) {
                capturePhoto(id);
            } else {
                showImageSlider(6);
            }
        } else if (id == R.id.imageView8) {
            if (TextUtils.isEmpty(franchiseeDetails.getFrontageOppositeImageId()) && franchiseeDetails.isAllowToEdit()) {
                capturePhoto(id);
            } else {
                showImageSlider(7);
            }
        } else if (id == R.id.textViewStartDate) {
            textViewStartDate.setError(null);
            selectedDateTimeId = id;
            showDateTimeDialogPicker();
        } else if (id == R.id.btnSubmit) {// Validate NextGen Site Work Commencement Data
            if (!validateWorkCompletionIntimation())
                return;

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(getResources().getString(R.string.alert_submit_confirmation_msg))
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ((WorkCompletionIntimationActivity) getActivity()).selectFragment(1);
                            submitWorkCompletionData();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else if (id == R.id.btnClear) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(getResources().getString(R.string.alert_clear_confirmation_msg))
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            clearWorkCompletionData();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else if (id == R.id.clickToViewImage) {
            showImageSlider(0);
        } else if (id == R.id.btnCancel) {
            cancelWorkCompletionIntimation();
        }
    }

    //region Clear Only Commencement Data
    public void clearWorkCompletionData() {

        // All Images
        imageView1.setImageResource(R.drawable.ic_camera_alt_black_72dp);
        imageView2.setImageResource(R.drawable.ic_camera_alt_black_72dp);
        imageView3.setImageResource(R.drawable.ic_camera_alt_black_72dp);
        imageView4.setImageResource(R.drawable.ic_camera_alt_black_72dp);
        imageView5.setImageResource(R.drawable.ic_camera_alt_black_72dp);
        imageView6.setImageResource(R.drawable.ic_camera_alt_black_72dp);
        imageView7.setImageResource(R.drawable.ic_camera_alt_black_72dp);
        imageView8.setImageResource(R.drawable.ic_camera_alt_black_72dp);

        // Switch
        //switchWorkCompletionPhoto.setChecked(false);

        // Reset Franchisee Details
        // Frontage Image
        franchiseeDetails.setFrontageImageId(null);
        // Left Wall Image
        franchiseeDetails.setLeftWallImageId(null);
        // Front Wall Image
        franchiseeDetails.setFrontWallImageId(null);
        // Right Wall Image
        franchiseeDetails.setRightWallImageId(null);
        // Back Wall Image
        franchiseeDetails.setBackWallImageId(null);
        // Ceiling Image
        franchiseeDetails.setCeilingImageId(null);
        // Floor Image
        franchiseeDetails.setFloorImageId(null);
        // Frontage Opposite Road Side
        franchiseeDetails.setFrontageOppositeImageId(null);

        // Work Completion Start and End Date
        strStartDate = null;
        startDate = null;

        textViewStartDate.setText("");

        franchiseeDetails.setWorkCompletionDate(null);

        // Image and DateTime View Id
        selectedImageViewId = 0;
        selectedDateTimeId = 0;

        // Reset Image
        picUri = null;

        // Reset Check List Status
        resetCheckListData();
    }

    private void resetCheckListData() {
        if (workCompletionCheckListDtos != null && workCompletionCheckListDtos.size() > 0) {
            for (int i = 0; i < workCompletionCheckListDtos.size(); i++) {
                workCompletionCheckListDtos.get(i).setStatus(0);
            }

            //Refresh Adapter
            workCompletionCheckListAdapter = new WorkCompletionCheckListAdapter(getActivity(), workCompletionCheckListDtos);
            recyclerViewCheckList.setAdapter(workCompletionCheckListAdapter);
            recyclerViewCheckList.notify();
        }
    }
    //endregion

    //region Show Schedule Visit DateTime Picker Dialog
    private DateTimePickerDialog dateTimePickerDialog;

    private void showDateTimeDialogPicker() {

        Date defaultDate = null;
        if (selectedDateTimeId == R.id.textViewStartDate) {
            defaultDate = startDate;
        }
        // Show DateTime Picker Dialog.
        dateTimePickerDialog = new DateTimePickerDialog(getActivity(), true, defaultDate, new DateTimePickerDialog.IDateTimePicker() {
            @Override
            public void getDateTime(Date datetime, String defaultFormattedDateTime) {
                try {
                    String formatedDate = dateFormatter2.format(datetime);
                    Toast.makeText(getActivity(), "Selected DateTime : " + formatedDate, Toast.LENGTH_LONG).show();

                    if (selectedDateTimeId != 0) {
                        TextView textViewDateTime = (TextView) view.findViewById(selectedDateTimeId);
                        textViewDateTime.setText(formatedDate);

                        if (selectedDateTimeId == R.id.textViewStartDate) {
                            startDate = datetime;
                            strStartDate = formatedDate;
                            franchiseeDetails.setWorkCompletionDate(defaultFormattedDateTime);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        dateTimePickerDialog.setCancelable(false);
        dateTimePickerDialog.setActionButtonName("Save");

        // 7 Days Back To allow.
        if (selectedDateTimeId == R.id.textViewStartDate) {
            if (TextUtils.isEmpty(strStartDate)) {
                dateTimePickerDialog.setMinDate(new Date().getTime() - 604800000L); // 7 * 24 * 60 * 60 * 1000
            } else {
                Date newDate = new Date(startDate.getTime() - 604800000L); // 7 * 24 * 60 * 60 * 1000
                dateTimePickerDialog.setMinDate(newDate.getTime());
            }
        }
        dateTimePickerDialog.show();

    }
    //endregion

    //region Save Work Completion Intimation Data
    public void submitWorkCompletionData() {

        String jsonData = JSONUtils.toString(franchiseeDetails);
        asyncUpdateWorkCompletion = new AsyncUpdateWorkCompletion(getActivity(), new AsyncUpdateWorkCompletion.Callback() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResult(String result) {
                try {
                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                        return;
                    }

                    Log.e(TAG, "Result : " + result);
                    if (result.startsWith("OKAY")) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.alert_msg_work_completion_success), Toast.LENGTH_LONG).show();
                        // Hide Layout Footer [Cancel, Clear and Submit Button]
                        layoutFooter.setVisibility(View.GONE);
/*
                        //TODO: Refresh Work Commencement Data
                        final Connection connection = new Connection(getActivity());
                        final String franchiseeApplicationNo = franchiseeDetails.getNextGenFranchiseeApplicationNo();
                        final String vkId = EncryptionUtil.encryptString(connection.getVkid(), getActivity());
                        final String tokenId = EncryptionUtil.encryptString(connection.getTokenId(), getActivity());
                        final String fAppNo = EncryptionUtil.encryptString(franchiseeApplicationNo, getActivity());

                        Utils.reloadWorkCommencentmentData(Constants.NEXTGEN_SITE_WORK_COMMENCEMENT, getActivity(), vkId, fAppNo, tokenId,
                                deviceInfo.getIMEI(), deviceInfo.getDeviceId(), deviceInfo.getSimNo());
                        ((WorkCompletionIntimationActivity) getActivity()).viewPager.setCurrentItem(2, true);*/

                    } else {
                        AlertDialogBoxInfo.showOkDialog(getActivity(), getResources().getString(R.string.alert_msg_work_completion_fail));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                }
            }
        });
        asyncUpdateWorkCompletion.execute(jsonData);
    }
    //endregion

    //region On Activity Result
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK) {
                //Save Lat n Long into Image which choosed on map.
                //CommonUtils.saveLatLongIntoImage(picUri, franchiseeDetails.getLatitude(), franchiseeDetails.getLongitude());

                getLocationAndTimestamp();
                Bitmap bitmapNew = ImageUtils.getBitmap(getActivity().getContentResolver(), picUri, latitude, longitude, currentTimestamp); //MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), picUri);

                //BitMap with TimeStamp on it
                bitmapNew = ImageUtils.stampWithTimeAndNameInBitmap(bitmapNew, getImageName(selectedImageViewId));

                if (selectedImageViewId != 0 && bitmapNew != null) {
                    ImageView img = new ImageView(getActivity());
                    img.setImageBitmap(bitmapNew);

                    // Check Image Capture into Landscape mode or not.
                    if (!CommonUtils.isLandscapePhoto(picUri, img)) {
                        AlertDialogBoxInfo.alertDialogShow(getActivity(), "Capture Image only landscape mode");
                    } else {

                        My_vakranggekendra_image mvi = new My_vakranggekendra_image();
                        mvi.setID(getImagePos(selectedImageViewId));
                        mvi.setImgetype(getImageName(selectedImageViewId));
                        mvi.setImage(bitmapNew);

                        showImagePreviewDialog((Object) mvi);

                    }
                }
            } else if (requestCode == SLIDER_CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK) {

                getLocationAndTimestamp();
                Bitmap bitmapNew = ImageUtils.getBitmap(getActivity().getContentResolver(), picUri, latitude, longitude, currentTimestamp); //MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), picUri);

                //BitMap with TimeStamp on it
                bitmapNew = ImageUtils.stampWithTimeAndNameInBitmap(bitmapNew, getImageName(selectedImageViewId));

                if (selectedImageViewId != 0 && bitmapNew != null) {
                    ImageView img = new ImageView(getActivity());
                    img.setImageBitmap(bitmapNew);

                    // Check Image Capture into Landscape mode or not.
                    if (!CommonUtils.isLandscapePhoto(picUri, img)) {
                        AlertDialogBoxInfo.alertDialogShow(getActivity(), "Capture Image only landscape mode");
                    } else {
                        ImageView imageView = (ImageView) view.findViewById(selectedImageViewId);
                        imageView.setImageBitmap(bitmapNew);
                        // Set Image to Franchisee Detail
                        setImageIntoFrachiseeDetail(selectedImageViewId, bitmapNew);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion

    // Set Image into Franchisee Detail
    public void setImageIntoFrachiseeDetail(int imageType, Bitmap bitmapNew) {

        //ByteArrayOutputStream byteArrayOutputStream  = ImageUtils.stampWithTimeAndName(bitmapNew, getImageName(imageType));

        Bitmap bitmapTimeStamp = bitmapNew; //ImageUtils.stampWithTimeAndNameInBitmap(bitmapNew, getImageName(imageType));
        /*ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmapTimeStamp.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);*/

        // Convert Bitmap to Bytes
        /*ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmapNew.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);*/
        /*String a = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        byte[] imageBytes = Base64.decode(a, Base64.DEFAULT);*/

        //String image = imageBytes != null ? (EncryptionUtil.encodeBase64(imageBytes)) : "";
        //String imageBase64 = EncryptionUtil.encodeBase64(byteArrayOutputStream.toByteArray());
        String imageBase64 = ImageUtils.updateExifData(picUri, bitmapNew, latitude, longitude, currentTimestamp);

        // Frontage Image
        if (imageType == R.id.imageView1) {
            bitmap1 = bitmapTimeStamp;
            franchiseeDetails.setFrontageImageId(imageBase64);
            // Left Wall Image
        } else if (imageType == R.id.imageView2) {
            bitmap2 = bitmapTimeStamp;
            franchiseeDetails.setLeftWallImageId(imageBase64);
            // Front Wall Image
        } else if (imageType == R.id.imageView3) {
            bitmap3 = bitmapTimeStamp;
            franchiseeDetails.setFrontWallImageId(imageBase64);
            // Right Wall Image
        } else if (imageType == R.id.imageView4) {
            bitmap4 = bitmapTimeStamp;
            franchiseeDetails.setRightWallImageId(imageBase64);
            // Back Wall Image
        } else if (imageType == R.id.imageView5) {
            bitmap5 = bitmapTimeStamp;
            franchiseeDetails.setBackWallImageId(imageBase64);
            // Ceiling Image
        } else if (imageType == R.id.imageView6) {
            bitmap6 = bitmapTimeStamp;
            franchiseeDetails.setCeilingImageId(imageBase64);
            // Floor Image
        } else if (imageType == R.id.imageView7) {
            bitmap7 = bitmapTimeStamp;
            franchiseeDetails.setFloorImageId(imageBase64);
            // Frontage Outside Road Image
        } else if (imageType == R.id.imageView8) {
            bitmap8 = bitmapTimeStamp;
            franchiseeDetails.setFloorImageId(imageBase64);
        }

        // Refresh Image Slider
        if (imageSliderDialog != null) {
            //imageSliderDialog.notifyViewPagerAdapter(new ArrayList<Object>(getImageSliderData()));
            imageSliderDialog.refresh(new ArrayList<Object>(getImageSliderData()), getImagePos(imageType));
        }
    }

    //region Validation Of Work Completion Intimation
    public boolean validateWorkCompletionIntimation() {

        return (isValidWorkCommencementPhoto() && isValidWorkCompletionDateTime() && isValidWorkCompletionCheckList());
    }

    //region Validate Work Commencement Photo
    private boolean isValidWorkCommencementPhoto() {

        boolean isValid = true;
        String msg = "";

       /* if (!switchWorkCompletionPhoto.isChecked()) {
            isValid = false;
            msg = getString(R.string.alert_msg_work_commencement_photo);
        } else*/
        if (TextUtils.isEmpty(franchiseeDetails.getFrontageImageId())) {
            isValid = false;
            msg = getString(R.string.alert_msg_frontageimage_unavailable);
        } else if (TextUtils.isEmpty(franchiseeDetails.getLeftWallImageId())) {
            isValid = false;
            msg = getString(R.string.alert_msg_leftwallimage_unavailable);
        } else if (TextUtils.isEmpty(franchiseeDetails.getFrontWallImageId())) {
            isValid = false;
            msg = getString(R.string.alert_msg_frontwallimage_unavailable);
        } else if (TextUtils.isEmpty(franchiseeDetails.getRightWallImageId())) {
            isValid = false;
            msg = getString(R.string.alert_msg_rightwallimage_unavailable);
        } else if (TextUtils.isEmpty(franchiseeDetails.getBackWallImageId())) {
            isValid = false;
            msg = getString(R.string.alert_msg_backwallimage_unavailable);
        } else if (TextUtils.isEmpty(franchiseeDetails.getCeilingImageId())) {
            isValid = false;
            msg = getString(R.string.alert_msg_ceilingimage_unavailable);
        } else if (TextUtils.isEmpty(franchiseeDetails.getFloorImageId())) {
            isValid = false;
            msg = getString(R.string.alert_msg_floorimage_unavailable);
        } else if (TextUtils.isEmpty(franchiseeDetails.getFrontageOppositeImageId())) {
            isValid = false;
            msg = getString(R.string.alert_msg_frontageoppositesideroad_unavailable);
        }


        // If Invalid
        if (!isValid) {
            AlertDialogBoxInfo.alertDialogShow(getActivity(), msg);
        }
        return isValid;
    }
    //endregion

    //region Validate Work Completion Date
    private boolean isValidWorkCompletionDateTime() {
        boolean isValid = true;
        String msg = "";

        if (TextUtils.isEmpty(franchiseeDetails.getWorkCompletionDate()) || startDate == null) {
            isValid = false;
            msg = getString(R.string.alert_msg_work_completion_date_unavailable);
        }

        // If Invalid
        if (!isValid) {
            AlertDialogBoxInfo.alertDialogShow(getActivity(), msg);
        }
        return isValid;
    }
    //endregion

    //region Validate Work Completion Check List
    private boolean isValidWorkCompletionCheckList() {
        return true;
    }
    //endregion

    //endregion

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (asyncUpdateWorkCompletion != null && !asyncUpdateWorkCompletion.isCancelled()) {
            asyncUpdateWorkCompletion.cancel(true);
        }

        if (asyncGetWorkCompletionCheckList != null && !asyncGetWorkCompletionCheckList.isCancelled()) {
            asyncGetWorkCompletionCheckList.cancel(true);
        }

        // Image Slider Dialog.
        if (imageSliderDialog != null) {
            imageSliderDialog.dismiss();
        }
    }

    public void cancelWorkCompletionIntimation() {
        Connection connection = new Connection(getActivity());
        if (connection.getVkid().toUpperCase().startsWith("VL")
                || connection.getVkid().toUpperCase().startsWith("VA")) {
            Intent intent = new Intent(getActivity(), MyVakrangeeKendraLocationDetailsNextGen.class);
            intent.putExtra("MODE", Constants.NEXTGEN_WORK_COMPLETION_INTIMATION);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        } else {
           /* Intent intent = new Intent(getActivity(), DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);*/
            getActivity().finish();
            ((WorkCompletionIntimationActivity) getActivity()).viewPager.setCurrentItem(0, true);
        }
    }

    //region Get Image Name
    public String getImageName(int imageid) {
        String name = null;
        if (imageid == R.id.imageView1) {
            name = "Frontage Image";
        } else if (imageid == R.id.imageView2) {
            name = "Left Wall Image";
        } else if (imageid == R.id.imageView3) {
            name = "Front Wall Image";
        } else if (imageid == R.id.imageView4) {
            name = "Right Wall Image";
        } else if (imageid == R.id.imageView5) {
            name = "Back Wall Image";
        } else if (imageid == R.id.imageView6) {
            name = "Ceiling Image";
        } else if (imageid == R.id.imageView7) {
            name = "Floor Image";
        } else if (imageid == R.id.imageView8) {
            name = "Frontage Opposite Road Image";
        }
        return name;
    }
    //endregion

    //region Get Image Id
    public int getImageId(int pos) {
        int id = -1;
        switch (pos) {
            case 0:
                id = R.id.imageView1;
                break;
            case 1:
                id = R.id.imageView2;
                break;
            case 2:
                id = R.id.imageView3;
                break;
            case 3:
                id = R.id.imageView4;
                break;
            case 4:
                id = R.id.imageView5;
                break;
            case 5:
                id = R.id.imageView6;
                break;
            case 6:
                id = R.id.imageView7;
                break;
            case 7:
                id = R.id.imageView8;
                break;
        }
        return id;
    }
    //endregion

    //region Get Image Pos
    public int getImagePos(int imageid) {
        int pos = 0;
        if (imageid == R.id.imageView1) {
            pos = 0;
        } else if (imageid == R.id.imageView2) {
            pos = 1;
        } else if (imageid == R.id.imageView3) {
            pos = 2;
        } else if (imageid == R.id.imageView4) {
            pos = 3;
        } else if (imageid == R.id.imageView5) {
            pos = 4;
        } else if (imageid == R.id.imageView6) {
            pos = 5;
        } else if (imageid == R.id.imageView7) {
            pos = 6;
        } else if (imageid == R.id.imageView8) {
            pos = 7;
        }
        return pos;
    }
    //endregion

    //region Show Image Slider
    ImageSliderDialog imageSliderDialog;

    public List<My_vakranggekendra_image> getImageSliderData() {
        //Prepare Data For Image Slider
        List<My_vakranggekendra_image> my_vakranggekendra_images = new ArrayList<My_vakranggekendra_image>();

        // Frontage Image
        // Left Wall Image
        // Front Wall Image
        // Right Wall Image
        // Back Wall Image
        // Ceiling Image
        // Floor Image
        if (!TextUtils.isEmpty(franchiseeDetails.getFrontageImageId())) {
            My_vakranggekendra_image mvi = new My_vakranggekendra_image();
            mvi.setID(0);
            mvi.setImgetype("Frontage Image");
            mvi.setImage(CommonUtils.StringToBitMap(franchiseeDetails.getFrontageImageId()));
            my_vakranggekendra_images.add(mvi);
        }
        if (!TextUtils.isEmpty(franchiseeDetails.getLeftWallImageId())) {
            My_vakranggekendra_image mvi = new My_vakranggekendra_image();
            mvi.setID(1);
            mvi.setImgetype("Left Wall Image");
            mvi.setImage(CommonUtils.StringToBitMap(franchiseeDetails.getLeftWallImageId()));
            my_vakranggekendra_images.add(mvi);

        }
        if (!TextUtils.isEmpty(franchiseeDetails.getFrontWallImageId())) {
            My_vakranggekendra_image mvi = new My_vakranggekendra_image();
            mvi.setID(2);
            mvi.setImgetype("Front Wall Image");
            mvi.setImage(CommonUtils.StringToBitMap(franchiseeDetails.getFrontWallImageId()));
            my_vakranggekendra_images.add(mvi);
        }
        if (!TextUtils.isEmpty(franchiseeDetails.getRightWallImageId())) {
            My_vakranggekendra_image mvi = new My_vakranggekendra_image();
            mvi.setID(3);
            mvi.setImgetype("Right Wall Image");
            mvi.setImage(CommonUtils.StringToBitMap(franchiseeDetails.getRightWallImageId()));
            my_vakranggekendra_images.add(mvi);
        }
        if (!TextUtils.isEmpty(franchiseeDetails.getBackWallImageId())) {
            My_vakranggekendra_image mvi = new My_vakranggekendra_image();
            mvi.setID(4);
            mvi.setImgetype("Back Wall Image");
            mvi.setImage(CommonUtils.StringToBitMap(franchiseeDetails.getBackWallImageId()));
            my_vakranggekendra_images.add(mvi);
        }
        if (!TextUtils.isEmpty(franchiseeDetails.getCeilingImageId())) {
            My_vakranggekendra_image mvi = new My_vakranggekendra_image();
            mvi.setID(5);
            mvi.setImgetype("Ceiling Image");
            mvi.setImage(CommonUtils.StringToBitMap(franchiseeDetails.getCeilingImageId()));
            my_vakranggekendra_images.add(mvi);
        }
        if (!TextUtils.isEmpty(franchiseeDetails.getFloorImageId())) {
            My_vakranggekendra_image mvi = new My_vakranggekendra_image();
            mvi.setID(6);
            mvi.setImgetype("Floor Image");
            mvi.setImage(CommonUtils.StringToBitMap(franchiseeDetails.getFloorImageId()));
            my_vakranggekendra_images.add(mvi);
        }
        if (!TextUtils.isEmpty(franchiseeDetails.getFrontageOppositeImageId())) {
            My_vakranggekendra_image mvi = new My_vakranggekendra_image();
            mvi.setID(7);
            mvi.setImgetype("Frontage Opposite Road Imge");
            mvi.setImage(CommonUtils.StringToBitMap(franchiseeDetails.getFrontageOppositeImageId()));
            my_vakranggekendra_images.add(mvi);
        }
        return my_vakranggekendra_images;
    }


    public void showImageSlider(int pos) {

        if (imageSliderDialog != null && imageSliderDialog.isShowing()) {
            return;
        }

        List<My_vakranggekendra_image> my_vakranggekendra_images = getImageSliderData();

        if (my_vakranggekendra_images.size() > 0) {

            //Image Slider is used to show Sliding Images with selected position in list.
            imageSliderDialog = new ImageSliderDialog(getActivity(), new ArrayList<Object>(my_vakranggekendra_images), pos, new ImageSliderDialog.ISliderClickHandler() {
                @Override
                public void captureClick(int position) {

                    int id = getImageId(position);
                    if (id > 0) {
                        changePhotoFromSlider(id);
                    }
                }

                @Override
                public void saveClick(List<Object> objectList) {

                }
            });
            imageSliderDialog.allowRemarks(false);
            // Based On isAllowToEdit - Allow to change Photo.
            imageSliderDialog.allowChangePhoto(franchiseeDetails.isAllowToEdit());
            imageSliderDialog.show();
            imageSliderDialog.setCancelable(false);
        } else {
            Toast.makeText(getActivity(), "No photos are available for preview.", Toast.LENGTH_SHORT).show();
        }

    }
    //endregion

    //region Capture Photo
    public void capturePhoto(int id) {
        File file = CommonUtils.getOutputMediaFile(CommonUtils.FILE_IMAGE_TYPE);
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        picUri = Uri.fromFile(file); // create
        i.putExtra(MediaStore.EXTRA_OUTPUT, picUri); // set the image file
        i.putExtra("ImageId", picUri); // set the image file
        selectedImageViewId = id;
        startActivityForResult(i, CAMERA_PIC_REQUEST);
    }

    public void changePhotoFromSlider(int id) {
        File file = CommonUtils.getOutputMediaFile(CommonUtils.FILE_IMAGE_TYPE);
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        picUri = Uri.fromFile(file); // create
        i.putExtra(MediaStore.EXTRA_OUTPUT, picUri); // set the image file
        i.putExtra("ImageId", picUri); // set the image file
        selectedImageViewId = id;
        startActivityForResult(i, SLIDER_CAMERA_PIC_REQUEST);
    }
    //endregion

    //region Show Preview Image Dialog
    CustomImagePreviewDialog customImagePreviewDialog;

    private void showImagePreviewDialog(Object object) {

        if (customImagePreviewDialog != null && customImagePreviewDialog.isShowing()) {
            customImagePreviewDialog.refresh(object);
            return;
        }

        if (object != null) {
            customImagePreviewDialog = new CustomImagePreviewDialog(getActivity(), object, new CustomImagePreviewDialog.IImagePreviewDialogClicks() {
                @Override
                public void capturePhotoClick() {
                    //If the app has not the permission then asking for the permission
                    permissionHandler.requestPermission(view, Manifest.permission.CAMERA, getString(R.string.needs_permission_camera_msg), new IPermission() {
                        @Override
                        public void IsPermissionGranted(boolean IsGranted) {
                            if (IsGranted) {
                                File file = CommonUtils.getOutputMediaFile(CommonUtils.FILE_IMAGE_TYPE);
                                Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                picUri = Uri.fromFile(file); //create
                                i.putExtra(MediaStore.EXTRA_OUTPUT, picUri); // set the image file
                                i.putExtra("ImageId", picUri); // set the image file
                                startActivityForResult(i, CAMERA_PIC_REQUEST);
                            }
                        }
                    });
                }

                @Override
                public void OkClick(Object object) {

                    if (object instanceof My_vakranggekendra_image) {
                        My_vakranggekendra_image my_vakranggekendra_image = (My_vakranggekendra_image) object;
                        ImageView imageView = (ImageView) view.findViewById(selectedImageViewId);
                        imageView.setImageBitmap(my_vakranggekendra_image.getImage());
                        // Set Image to Franchisee Detail
                        setImageIntoFrachiseeDetail(selectedImageViewId, my_vakranggekendra_image.getImage());
                    }

                }
            });
            customImagePreviewDialog.show();
            customImagePreviewDialog.setCancelable(false);
            customImagePreviewDialog.allowChangePhoto(true);
            customImagePreviewDialog.allowRemarks(false);

        } else {
            Toast.makeText(getActivity(), "No photo available to preview.", Toast.LENGTH_SHORT).show();
        }
    }
    //endregion
}