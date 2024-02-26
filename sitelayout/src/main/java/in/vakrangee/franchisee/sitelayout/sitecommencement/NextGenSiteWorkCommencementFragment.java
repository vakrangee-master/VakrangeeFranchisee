package in.vakrangee.franchisee.sitelayout.sitecommencement;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.vakrangee.franchisee.sitelayout.R;
import in.vakrangee.franchisee.sitelayout.Utils;
import in.vakrangee.franchisee.sitelayout.activity.MyVakrangeeKendraLocationDetailsNextGen;
import in.vakrangee.supercore.franchisee.commongui.DateTimePickerDialog;
import in.vakrangee.supercore.franchisee.commongui.ImageSliderDialog;
import in.vakrangee.supercore.franchisee.commongui.PopupUtils;
import in.vakrangee.supercore.franchisee.commongui.imagegallery.ImageDto;
import in.vakrangee.supercore.franchisee.commongui.imagepreview.CustomImagePreviewDialog;
import in.vakrangee.supercore.franchisee.model.FranchiseeDetails;
import in.vakrangee.supercore.franchisee.model.FranchiseeRemarkDetails;
import in.vakrangee.supercore.franchisee.model.My_vakranggekendra_image;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeviceInfo;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.utils.GPSTracker;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;
import in.vakrangee.supercore.franchisee.utils.JSONUtils;

@SuppressLint("ValidFragment")
public class NextGenSiteWorkCommencementFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {

    private final static String TAG = "NextGenSiteWorkCommencementFragment";

    private DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US); //new SimpleDateFormat("dd-MM-yyyy HH:mm a", Locale.US);
    private DateFormat dateFormatter2 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    private DateFormat dateFormatter3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
    private DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");

    FranchiseeDetails franchiseeDetails;
    View view;

    TextView switchCommencementPhoto;

    View includeInteriorPhotos;
    ImageView imageView1;                    // Frontage Image
    ImageView imageView2;                    // Left Wall Image
    ImageView imageView3;                    // Front Wall Image
    ImageView imageView4;                    // Right Wall Image
    ImageView imageView5;                    // Back Wall Image
    ImageView imageView6;                    // Ceiling Image
    ImageView imageView7;                    // Floor Image
    //@BindView(R.id.imageView8)ImageView imageView8;                    // Frontage from 15feet

    Bitmap bitmap1, bitmap2, bitmap3, bitmap4, bitmap5, bitmap6, bitmap7;


    TextView textViewStartDate;        // Start Date
    TextView textViewCompletionDate;  // Completion Date

    LinearLayout layoutFooter;
    Button btnSubmit;                 // Save Data
    Button btnCancel;                 // Go to Back
    Button btnClear;                   // Clear Data
    TextView tooltipSiteCommencement;   // Show ErrorMsg
    TextView clickToViewImage;

    final int CAMERA_CAPTURE = 201;     // REQUEST CODE
    final int CHANGE_PHOTO = 202;     // REQUEST CODE
    private Uri picUri;                 // Picture URI

    private int selectedImageViewId = 0;
    private int selectedDateTimeId = 0;

    private Date startDate, completionDate;
    private String strStartDate, strCompletionDate;

    private AsyncUpdateWorkCommencement asyncUpdateWorkCommencement;

    private DeviceInfo deviceInfo;

    private AlphaAnimation btnClickAnim = new AlphaAnimation(1F, 0.6F);

    private List<My_vakranggekendra_image> my_vakranggekendra_images;

    private String strCommencementMinDate;
    private Date commencementMinDate;
    private GPSTracker gpsTracker;
    private String latitude = "", longitude = "", currentTimestamp = "";
    private CustomImagePreviewDialog customImagePreviewDialog;
    private boolean isAdhoc = false;

    TextView txtExpectedDateLbl;

    public NextGenSiteWorkCommencementFragment() {
    }

    public NextGenSiteWorkCommencementFragment(FranchiseeDetails franchiseeDetails) {
        this.franchiseeDetails = franchiseeDetails;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_nextgen_site_work_commencement_fragment, container, false);

        final Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fontawesome-webfont.ttf");
        // Bind GUI
        ButterKnife.bind(this, view);

        imageView1 = (ImageView) view.findViewById(R.id.imageView1);
        imageView2 = (ImageView) view.findViewById(R.id.imageView2);
        imageView3 = (ImageView) view.findViewById(R.id.imageView3);
        imageView4 = (ImageView) view.findViewById(R.id.imageView4);
        imageView5 = (ImageView) view.findViewById(R.id.imageView5);
        imageView6 = (ImageView) view.findViewById(R.id.imageView6);
        imageView7 = (ImageView) view.findViewById(R.id.imageView7);

        textViewStartDate = (TextView) view.findViewById(R.id.textViewStartDate);
        textViewCompletionDate = (TextView) view.findViewById(R.id.textViewCompletionDate);
        switchCommencementPhoto = (TextView) view.findViewById(R.id.switchCommencementPhoto);
        txtExpectedDateLbl = view.findViewById(R.id.txtExpectedDateLbl);

        layoutFooter = view.findViewById(R.id.layoutFooter);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnClear = view.findViewById(R.id.btnClear);

        tooltipSiteCommencement = view.findViewById(R.id.tooltipSiteCommencement);
        clickToViewImage = view.findViewById(R.id.clickToViewImage);

        includeInteriorPhotos = view.findViewById(R.id.includeInteriorPhotos);

       // includeInteriorPhotos =  view.findViewById(R.includeInteriorPhotos);
        deviceInfo = DeviceInfo.getInstance(getActivity());
        gpsTracker = new GPSTracker(getContext());

        // Get App MODE
        isAdhoc = Constants.ENABLE_ADHOC_MODE || Constants.ENABLE_FRANCHISEE_MODE;

        /*// Add Listener On Switch
        switchCommencementPhoto.setOnCheckedChangeListener(null);
        switchCommencementPhoto.setChecked(true);
        switchCommencementPhoto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    includeInteriorPhotos.setVisibility(View.VISIBLE);
                } else {
                    includeInteriorPhotos.setVisibility(View.GONE);
                }
            }
        });*/
        switchCommencementPhoto.setText(Html.fromHtml("<b>" + getResources().getString(R.string.label_work_commencement) + "</b>"));

        btnSubmit.setTypeface(font);
        btnSubmit.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + "  " + getResources().getString(R.string.submit)));

        btnClear.setTypeface(font);
        btnClear.setText(new SpannableStringBuilder(new String(new char[]{0xf021}) + "  " + getResources().getString(R.string.clear)));

        btnCancel.setTypeface(font);
        btnCancel.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + "  " + getResources().getString(R.string.cancel)));

        tooltipSiteCommencement.setTypeface(font);
        tooltipSiteCommencement.setText(new SpannableStringBuilder(" " + new String(new char[]{0xf05a}) + " "));

        // Add Listeners
        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);
        imageView4.setOnClickListener(this);
        imageView5.setOnClickListener(this);
        imageView6.setOnClickListener(this);
        imageView7.setOnClickListener(this);
        //imageView8.setOnClickListener(this);

        clickToViewImage.setOnClickListener(this);
        clickToViewImage.setVisibility(View.GONE);

        // Add Long Click Listener
        /*imageView1.setOnLongClickListener(this);
        imageView2.setOnLongClickListener(this);
        imageView3.setOnLongClickListener(this);
        imageView4.setOnLongClickListener(this);
        imageView5.setOnLongClickListener(this);
        imageView6.setOnLongClickListener(this);
        imageView7.setOnLongClickListener(this);*/

        textViewStartDate.setOnClickListener(this);
        textViewCompletionDate.setOnClickListener(this);

        btnSubmit.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        my_vakranggekendra_images = new ArrayList<My_vakranggekendra_image>();

        // Set Already Available Data
        reloadData();

        return view;
    }

    public void getLocationAndTimestamp() {
        //Get Current location and time stamp
        if (gpsTracker.canGetLocation()) {
            latitude = String.valueOf(gpsTracker.getLatitude());
            longitude = String.valueOf(gpsTracker.getLongitude());
            currentTimestamp = gpsTracker.getFormattedDateTime();
        }
    }

    //region Reload Data & Images
    @SuppressLint("LongLogTag")
    public void reloadData() {

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
           /* if(!TextUtils.isEmpty(franchiseeDetails.getFrontage15feetImageId()) &&
                    franchiseeDetails.getFrontage15feetImageId().equalsIgnoreCase("null")) {
                realoadImage(franchiseeDetails.getFrontage15feetImageId(), imageView8);
            }*/

            // Photo Available
            /*if (isPhotoAvailable)
                switchCommencementPhoto.setChecked(true);
            else
                switchCommencementPhoto.setChecked(false);*/

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

            /**
             * Work Commencement Status
             * 0 = Data Updated
             * 1 = Site Send Back For Correction
             * 2 = Resubmitted For Nextgen Site Work Commencement
             * 3 = Site Verified
             */
            //if(franchiseeDetails.getStatus() != NextGenSiteCommencementViewPager.SITE_SEND_BACK_FOR_CORRECTION) {
            if (!franchiseeDetails.isAllowToEdit()) {
                // Photos
                //switchCommencementPhoto.setEnabled(false);
                /*imageView1.setEnabled(false);
                imageView2.setEnabled(false);
                imageView3.setEnabled(false);
                imageView4.setEnabled(false);
                imageView5.setEnabled(false);
                imageView6.setEnabled(false);
                imageView7.setEnabled(false);*/

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
                        tooltipSiteCommencement.setVisibility(View.VISIBLE);
                        tooltipSiteCommencement.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                tooltipSiteCommencement.setAnimation(btnClickAnim);
                                PopupUtils.show(getActivity(), v, commencementTooltip);
                            }
                        });
                    }

                }
            }

            //Expected Date lbl

            if (!TextUtils.isEmpty(franchiseeDetails.getInteriorBrandingStatus())) {

                switch (franchiseeDetails.getInteriorBrandingStatus()) {
                    case "1":
                        txtExpectedDateLbl.setText(getContext().getResources().getString(R.string.estimated_completion_date));
                        break;

                    case "2":
                    case "3":
                        txtExpectedDateLbl.setText(getContext().getResources().getString(R.string.completion_date));
                        break;

                    default:
                        txtExpectedDateLbl.setText(getContext().getResources().getString(R.string.estimated_completion_date));
                        break;
                }
            }
        }

        //Reload Images
        my_vakranggekendra_images = getImageSliderData(false, -1);
    }

    private Bitmap realoadImage(String imageId, ImageView imageView) {
        Bitmap img = CommonUtils.StringToBitMap(imageId);
        if (img != null)
            imageView.setImageBitmap(img);
        return img;
    }

    //endregion

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.imageView1) {
            if (TextUtils.isEmpty(franchiseeDetails.getFrontageImageId()) && franchiseeDetails.isAllowToEdit()) {
                capturePhoto(id, CAMERA_CAPTURE);
            } else {
                showImageSlider(0);
            }
        } else if (id == R.id.imageView2) {
            if (TextUtils.isEmpty(franchiseeDetails.getLeftWallImageId()) && franchiseeDetails.isAllowToEdit()) {
                capturePhoto(id, CAMERA_CAPTURE);
            } else {
                showImageSlider(1);
            }
        } else if (id == R.id.imageView3) {
            if (TextUtils.isEmpty(franchiseeDetails.getFrontWallImageId()) && franchiseeDetails.isAllowToEdit()) {
                capturePhoto(id, CAMERA_CAPTURE);
            } else {
                showImageSlider(2);
            }
        } else if (id == R.id.imageView4) {
            if (TextUtils.isEmpty(franchiseeDetails.getRightWallImageId()) && franchiseeDetails.isAllowToEdit()) {
                capturePhoto(id, CAMERA_CAPTURE);
            } else {
                showImageSlider(3);
            }
        } else if (id == R.id.imageView5) {
            if (TextUtils.isEmpty(franchiseeDetails.getBackWallImageId()) && franchiseeDetails.isAllowToEdit()) {
                capturePhoto(id, CAMERA_CAPTURE);
            } else {
                showImageSlider(4);
            }
        } else if (id == R.id.imageView6) {
            if (TextUtils.isEmpty(franchiseeDetails.getCeilingImageId()) && franchiseeDetails.isAllowToEdit()) {
                capturePhoto(id, CAMERA_CAPTURE);
            } else {
                showImageSlider(5);
            }
        } else if (id == R.id.imageView7) {
            if (TextUtils.isEmpty(franchiseeDetails.getFloorImageId()) && franchiseeDetails.isAllowToEdit()) {
                capturePhoto(id, CAMERA_CAPTURE);
            } else {
                showImageSlider(6);
            }
        } else if (id == R.id.textViewStartDate) {
            textViewStartDate.setError(null);
            selectedDateTimeId = id;
            showDateTimeDialogPicker();
        } else if (id == R.id.textViewCompletionDate) {
            if (!TextUtils.isEmpty(strStartDate)) {
                selectedDateTimeId = id;
                showDateTimeDialogPicker();
            } else {
                Toast.makeText(getActivity(), "Please Select Start Date.", Toast.LENGTH_LONG).show();
                textViewStartDate.setError("Select Start Date.");
            }
        } else if (id == R.id.btnSubmit) {// Validate NextGen Site Work Commencement Data
            if (!validateNextGenSiteWorkCommencement())
                return;

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(getResources().getString(R.string.alert_submit_confirmation_msg))
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ((NextGenSiteCommencementActivity) getActivity()).selectFragment(2);
                            submitCommencementData();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else if (id == R.id.btnClear) {//String msg = getContext().getResources().getString(R.string.alert_clear_confirmation_msg);
            String msg = "This will clear all the data entered by you, including photos. Do you want to proceed?";
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            clearCommencementData();
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
            cancelCommencement();
        }
    }

    @Override
    public boolean onLongClick(View v) {
        int id = v.getId();
        if (id == R.id.imageView1 || id == R.id.imageView2 || id == R.id.imageView3 || id == R.id.imageView4 || id == R.id.imageView5 || id == R.id.imageView6 || id == R.id.imageView7) {//showImageSlider();
        }

        return false;
    }

    //region Clear Only Commencement Data
    public void clearCommencementData() {

        // All Images
        imageView1.setImageResource(R.drawable.ic_camera_alt_black_72dp);
        imageView2.setImageResource(R.drawable.ic_camera_alt_black_72dp);
        imageView3.setImageResource(R.drawable.ic_camera_alt_black_72dp);
        imageView4.setImageResource(R.drawable.ic_camera_alt_black_72dp);
        imageView5.setImageResource(R.drawable.ic_camera_alt_black_72dp);
        imageView6.setImageResource(R.drawable.ic_camera_alt_black_72dp);
        imageView7.setImageResource(R.drawable.ic_camera_alt_black_72dp);

        // Switch
        //switchCommencementPhoto.setChecked(false);

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

        // Commencement Start and End Date
        strStartDate = null;
        strCompletionDate = null;
        startDate = null;
        completionDate = null;

        textViewStartDate.setText("");
        textViewCompletionDate.setText("");
        franchiseeDetails.setCommencementStartDate(null);
        franchiseeDetails.setCommencementEstimatedEndDate(null);

        // Image and DateTime View Id
        selectedImageViewId = 0;
        selectedDateTimeId = 0;

        // Reset Image
        picUri = null;
    }
    //endregion

    //region Show Schedule Visit DateTime Picker Dialog
    private DateTimePickerDialog dateTimePickerDialog;

    private void showDateTimeDialogPicker() {

        Date defaultDate = null;
        if (selectedDateTimeId == R.id.textViewStartDate) {
            defaultDate = startDate;
        } else if (selectedDateTimeId == R.id.textViewCompletionDate) {
            defaultDate = completionDate;
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
                            franchiseeDetails.setCommencementStartDate(defaultFormattedDateTime);
                        } else if (selectedDateTimeId == R.id.textViewCompletionDate) {
                            completionDate = datetime;
                            strCompletionDate = formatedDate;
                            franchiseeDetails.setCommencementEstimatedEndDate(defaultFormattedDateTime);
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
            /*if(TextUtils.isEmpty(strStartDate)) {
                dateTimePickerDialog.setMinDate(new Date().getTime() - 604800000L); // 7 * 24 * 60 * 60 * 1000
            }
            else {
                Date newDate = new Date(startDate.getTime() - 604800000L); // 7 * 24 * 60 * 60 * 1000
                dateTimePickerDialog.setMinDate(newDate.getTime());
            }*/
            if (commencementMinDate == null) {
                dateTimePickerDialog.setMinDate(new Date().getTime() - 604800000L); // 7 * 24 * 60 * 60 * 1000
            } else {
                dateTimePickerDialog.setMinDate(commencementMinDate.getTime()); // Received from Server.
            }
        } else if (selectedDateTimeId == R.id.textViewCompletionDate && !TextUtils.isEmpty(strStartDate)) {
            dateTimePickerDialog.setMinDate(startDate.getTime());
        }
        dateTimePickerDialog.show();

    }
    //endregion

    //region Save Work Commencement Data
    public void submitCommencementData() {

        String jsonData = JSONUtils.toString(franchiseeDetails);
        asyncUpdateWorkCommencement = new AsyncUpdateWorkCommencement(getActivity(), new AsyncUpdateWorkCommencement.Callback() {
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
                        Toast.makeText(getActivity(), getResources().getString(R.string.alert_msg_work_commencement_success), Toast.LENGTH_LONG).show();
                        // Hide Layout Footer [Cancel, Clear and Submit Button]
                        layoutFooter.setVisibility(View.GONE);

                        //TODO: Refresh Work Commencement Data
                        final Connection connection = new Connection(getActivity());
                        final String franchiseeApplicationNo = franchiseeDetails.getNextGenFranchiseeApplicationNo();
                        String tempVkId = connection.getVkid();
                        final String vkId = EncryptionUtil.encryptString(tempVkId, getActivity());
                        final String tokenId = EncryptionUtil.encryptString(connection.getTokenId(), getActivity());
                        final String fAppNo = EncryptionUtil.encryptString(franchiseeApplicationNo, getActivity());

                        if (isAdhoc) {
                            if (!TextUtils.isEmpty(tempVkId)) {
                                Utils.reloadWorkCommencentmentData(Constants.NEXTGEN_SITE_WORK_COMMENCEMENT, getActivity(), tempVkId, franchiseeApplicationNo);
                            } else {
                                String enquiryId = CommonUtils.getEnquiryId(getContext());
                                Utils.reloadWorkCommencentmentData(Constants.NEXTGEN_SITE_WORK_COMMENCEMENT, getActivity(), enquiryId, franchiseeApplicationNo);
                            }
                        } else {
                            Utils.reloadWorkCommencentmentData(Constants.NEXTGEN_SITE_WORK_COMMENCEMENT, getActivity(), vkId, fAppNo, tokenId,
                                    deviceInfo.getIMEI(), deviceInfo.getDeviceId(), deviceInfo.getSimNo());
                        }

                        ((NextGenSiteCommencementActivity) getActivity()).viewPager.setCurrentItem(2, true);

                    } else {
                        AlertDialogBoxInfo.showOkDialog(getActivity(), getResources().getString(R.string.alert_msg_work_commencement_fail));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                }
            }
        });
        asyncUpdateWorkCommencement.execute(jsonData);
    }
    //endregion

    //region On Activity Result
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == CAMERA_CAPTURE && resultCode == Activity.RESULT_OK) {
                getLocationAndTimestamp();
                Bitmap bitmapNew = ImageUtils.getBitmap(getActivity().getContentResolver(), picUri, latitude, longitude, currentTimestamp); //MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), picUri);

                if (selectedImageViewId != 0 && bitmapNew != null) {
                    ImageView img = new ImageView(getActivity());
                    img.setImageBitmap(bitmapNew);

                    // Check Image Capture into Landscape mode or not.
                    if (!CommonUtils.isLandscapePhoto(picUri, img)) {
                        AlertDialogBoxInfo.alertDialogShow(getActivity(), "Capture Image only landscape mode");
                    } else {

                        //Display Preview Dialog
                       /* ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmapNew.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                        String imageBase64 = EncryptionUtil.encodeBase64(byteArrayOutputStream.toByteArray());*/
                        String imageBase64 = ImageUtils.updateExifData(picUri, bitmapNew, latitude, longitude, currentTimestamp);

                        ImageDto imageDto = savePhoto(bitmapNew, imageBase64);
                        showImagePreviewDialog(imageDto);
                    }
                }
            } else if (requestCode == CHANGE_PHOTO && resultCode == Activity.RESULT_OK) {
                getLocationAndTimestamp();
                Bitmap bitmapNew = ImageUtils.getBitmap(getActivity().getContentResolver(), picUri, latitude, longitude, currentTimestamp); //MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), picUri);

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
                        setImageIntoFrachiseeDetail(selectedImageViewId, bitmapNew, true);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion

    public ImageDto savePhoto(Bitmap bitmap, String imageBase64) {
        File file = new File(picUri.toString());
        ImageDto imageDto = new ImageDto();
        imageDto.setName(getImageName(selectedImageViewId));
        imageDto.setUri(picUri);
        imageDto.setBitmap(bitmap);
        imageDto.setImageBase64(imageBase64);
        imageDto.setCapturedDateTime(dateTimeFormat.format(new Date()));
        return imageDto;

    }

    // Set Image into Franchisee Detail
    public void setImageIntoFrachiseeDetail(int imageType, Bitmap bitmapNew, boolean IsChangePhoto) {

        //ByteArrayOutputStream byteArrayOutputStream  = ImageUtils.stampWithTimeAndName(bitmapNew, getImageName(imageType));

        Bitmap bitmapTimeStamp = ImageUtils.stampWithTimeAndNameInBitmap(bitmapNew, getImageName(imageType));
        /*ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmapTimeStamp.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);*/
        String imageBase64 = ImageUtils.updateExifData(picUri, bitmapNew, latitude, longitude, currentTimestamp);

        // Convert Bitmap to Bytes
        /*ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmapNew.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);*/
        /*String a = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        byte[] imageBytes = Base64.decode(a, Base64.DEFAULT);*/

        //String image = imageBytes != null ? (EncryptionUtil.encodeBase64(imageBytes)) : "";
        //String imageBase64 = EncryptionUtil.encodeBase64(byteArrayOutputStream.toByteArray());

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
        }

        // Refresh Image Slider
        if (imageSliderDialog != null) {
            //imageSliderDialog.notifyViewPagerAdapter(new ArrayList<Object>(getImageSliderData()));
            int selectedImagePos = getImagePos(imageType);
            My_vakranggekendra_image image = updateSpecificImageData(selectedImagePos, IsChangePhoto);
            my_vakranggekendra_images.set(selectedImagePos, image);
            imageSliderDialog.refresh(new ArrayList<Object>(my_vakranggekendra_images), selectedImagePos);
        }
    }

    private My_vakranggekendra_image updateSpecificImageData(int selectedImagePos, boolean IsChangePhoto) {
        My_vakranggekendra_image image = my_vakranggekendra_images.get(selectedImagePos);

        switch (selectedImagePos) {
            case 0:
                image.setID(0);
                image.setImageId(franchiseeDetails.getFrImageId());
                image.setImgetype("Frontage Image");
                image.setImage(CommonUtils.StringToBitMap(franchiseeDetails.getFrontageImageId()));
                image.setImageHash(CommonUtils.getImageSalt(franchiseeDetails.getFrontageImageId()));
                image.setChangedPhoto(IsChangePhoto);
                break;
            case 1:
                image.setID(1);
                image.setImageId(franchiseeDetails.getLwImageId());
                image.setImgetype("Left Wall Image");
                image.setImage(CommonUtils.StringToBitMap(franchiseeDetails.getLeftWallImageId()));
                image.setImageHash(CommonUtils.getImageSalt(franchiseeDetails.getLeftWallImageId()));
                image.setChangedPhoto(IsChangePhoto);
                break;
            case 2:
                image.setID(2);
                image.setImageId(franchiseeDetails.getFwImageId());
                image.setImgetype("Front Wall Image");
                image.setImage(CommonUtils.StringToBitMap(franchiseeDetails.getFrontWallImageId()));
                image.setImageHash(CommonUtils.getImageSalt(franchiseeDetails.getFrontWallImageId()));
                image.setChangedPhoto(IsChangePhoto);
                break;
            case 3:
                image.setID(3);
                image.setImageId(franchiseeDetails.getRwImageId());
                image.setImgetype("Right Wall Image");
                image.setImage(CommonUtils.StringToBitMap(franchiseeDetails.getRightWallImageId()));
                image.setImageHash(CommonUtils.getImageSalt(franchiseeDetails.getRightWallImageId()));
                image.setChangedPhoto(IsChangePhoto);
                break;
            case 4:
                image.setID(4);
                image.setImageId(franchiseeDetails.getBwImageId());
                image.setImgetype("Back Wall Image");
                image.setImage(CommonUtils.StringToBitMap(franchiseeDetails.getBackWallImageId()));
                image.setImageHash(CommonUtils.getImageSalt(franchiseeDetails.getBackWallImageId()));
                image.setChangedPhoto(IsChangePhoto);
                break;
            case 5:
                image.setID(5);
                image.setImageId(franchiseeDetails.getCeImageId());
                image.setImgetype("Ceiling Image");
                image.setImage(CommonUtils.StringToBitMap(franchiseeDetails.getCeilingImageId()));
                image.setImageHash(CommonUtils.getImageSalt(franchiseeDetails.getCeilingImageId()));
                image.setChangedPhoto(IsChangePhoto);
                break;
            case 6:
                image.setID(6);
                image.setImageId(franchiseeDetails.getFlImageId());
                image.setImgetype("Floor Image");
                image.setImage(CommonUtils.StringToBitMap(franchiseeDetails.getFloorImageId()));
                image.setImageHash(CommonUtils.getImageSalt(franchiseeDetails.getFloorImageId()));
                image.setChangedPhoto(IsChangePhoto);
                break;
        }
        return image;
    }

    //region Validation Of NextGenSiteWorkCommencement
    public boolean validateNextGenSiteWorkCommencement() {

        /**
         * Commented by Vasundhara - 18th Feb, 2019
         * Location tab is not shown, so commented validation of the same
         */
        //return (isValidKendraLocation() && isValidWorkCommencementPhoto() && isValidWorkCommencementDateTime());
        return (isValidWorkCommencementPhoto() && isValidWorkCommencementDateTime());
    }

    //region Validate Work Commencement Photo
    private boolean isValidWorkCommencementPhoto() {

        boolean isValid = true;
        String msg = "";

        /*if (!switchCommencementPhoto.isChecked()) {
            isValid = false;
            msg = getString(R.string.alert_msg_work_commencement_photo);
        } else */
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
        }

        // If Invalid
        if (!isValid) {
            AlertDialogBoxInfo.alertDialogShow(getActivity(), msg);
        }
        return isValid;
    }
    //endregion

    //region Validate VKendra Location
    private boolean isValidKendraLocation() {
        if (TextUtils.isEmpty(franchiseeDetails.getLatitude()) || TextUtils.isEmpty(franchiseeDetails.getLongitude())) {
            //TODO: Need to show an alert and jump to Kendra Location Tab
            AlertDialogBoxInfo.alertDialogShow(getActivity(), getString(R.string.alert_msg_location_unavailable));
            ((NextGenSiteCommencementActivity) getActivity()).selectFragment(1);
            return false;
        }
        return true;
    }
    //endregion

    //region Validate Work Commencement Start and End DateTime
    private boolean isValidWorkCommencementDateTime() {
        boolean isValid = true;
        String msg = "";

        if (TextUtils.isEmpty(franchiseeDetails.getCommencementStartDate()) || startDate == null) {
            isValid = false;
            msg = getString(R.string.alert_msg_work_start_date_unavailable);
        } else if (TextUtils.isEmpty(franchiseeDetails.getCommencementEstimatedEndDate()) || completionDate == null) {
            isValid = false;
            msg = getString(R.string.alert_msg_work_end_date_unavailable);
        } else if (completionDate.before(startDate)) {
            isValid = false;
            msg = getString(R.string.alert_msg_work_date_validation);
        }

        // If Invalid
        if (!isValid) {
            AlertDialogBoxInfo.alertDialogShow(getActivity(), msg);
        }
        return isValid;
    }
    //endregion

    //endregion

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (asyncUpdateWorkCommencement != null && !asyncUpdateWorkCommencement.isCancelled()) {
            asyncUpdateWorkCommencement.cancel(true);
        }

        // Image Slider Dialog.
        if (imageSliderDialog != null) {
            imageSliderDialog.dismiss();
        }

        //Image Preview DIalog
        if (customImagePreviewDialog != null) {
            customImagePreviewDialog.dismiss();
            customImagePreviewDialog = null;

        }
    }

    public void cancelCommencement() {
        Connection connection = new Connection(getActivity());
        if (connection.getVkid().toUpperCase().startsWith("VL")
                || connection.getVkid().toUpperCase().startsWith("VA")) {
            Intent intent = new Intent(getActivity(), MyVakrangeeKendraLocationDetailsNextGen.class);
            intent.putExtra("MODE", Constants.NEXTGEN_SITE_WORK_COMMENCEMENT);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        } else {
           /* Intent intent = new Intent(getActivity(), DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);*/
            getActivity().finish();
            ((NextGenSiteCommencementActivity) getActivity()).viewPager.setCurrentItem(0, true);
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
        }
        return pos;
    }
    //endregion

    //region Show Image Slider
    ImageSliderDialog imageSliderDialog;

    public List<My_vakranggekendra_image> getImageSliderData(boolean IsChangePhoto, int selectedImagePos) {

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
            mvi.setImageId(franchiseeDetails.getFrImageId());
            mvi.setImgetype("Frontage Image");
            mvi.setImage(CommonUtils.StringToBitMap(franchiseeDetails.getFrontageImageId()));
            mvi.setImageHash(CommonUtils.getImageSalt(franchiseeDetails.getFrontageImageId()));
            my_vakranggekendra_images.add(mvi);
        }
        if (!TextUtils.isEmpty(franchiseeDetails.getLeftWallImageId())) {
            My_vakranggekendra_image mvi = new My_vakranggekendra_image();
            mvi.setID(1);
            mvi.setImageId(franchiseeDetails.getLwImageId());
            mvi.setImgetype("Left Wall Image");
            mvi.setImage(CommonUtils.StringToBitMap(franchiseeDetails.getLeftWallImageId()));
            mvi.setImageHash(CommonUtils.getImageSalt(franchiseeDetails.getLeftWallImageId()));
            my_vakranggekendra_images.add(mvi);

        }
        if (!TextUtils.isEmpty(franchiseeDetails.getFrontWallImageId())) {
            My_vakranggekendra_image mvi = new My_vakranggekendra_image();
            mvi.setID(2);
            mvi.setImageId(franchiseeDetails.getFwImageId());
            mvi.setImgetype("Front Wall Image");
            mvi.setImage(CommonUtils.StringToBitMap(franchiseeDetails.getFrontWallImageId()));
            mvi.setImageHash(CommonUtils.getImageSalt(franchiseeDetails.getFrontWallImageId()));
            my_vakranggekendra_images.add(mvi);
        }
        if (!TextUtils.isEmpty(franchiseeDetails.getRightWallImageId())) {
            My_vakranggekendra_image mvi = new My_vakranggekendra_image();
            mvi.setID(3);
            mvi.setImageId(franchiseeDetails.getRwImageId());
            mvi.setImgetype("Right Wall Image");
            mvi.setImage(CommonUtils.StringToBitMap(franchiseeDetails.getRightWallImageId()));
            mvi.setImageHash(CommonUtils.getImageSalt(franchiseeDetails.getRightWallImageId()));
            my_vakranggekendra_images.add(mvi);
        }
        if (!TextUtils.isEmpty(franchiseeDetails.getBackWallImageId())) {
            My_vakranggekendra_image mvi = new My_vakranggekendra_image();
            mvi.setID(4);
            mvi.setImageId(franchiseeDetails.getBwImageId());
            mvi.setImgetype("Back Wall Image");
            mvi.setImage(CommonUtils.StringToBitMap(franchiseeDetails.getBackWallImageId()));
            mvi.setImageHash(CommonUtils.getImageSalt(franchiseeDetails.getBackWallImageId()));
            my_vakranggekendra_images.add(mvi);
        }
        if (!TextUtils.isEmpty(franchiseeDetails.getCeilingImageId())) {
            My_vakranggekendra_image mvi = new My_vakranggekendra_image();
            mvi.setID(5);
            mvi.setImageId(franchiseeDetails.getCeImageId());
            mvi.setImgetype("Ceiling Image");
            mvi.setImage(CommonUtils.StringToBitMap(franchiseeDetails.getCeilingImageId()));
            mvi.setImageHash(CommonUtils.getImageSalt(franchiseeDetails.getCeilingImageId()));
            my_vakranggekendra_images.add(mvi);
        }
        if (!TextUtils.isEmpty(franchiseeDetails.getFloorImageId())) {
            My_vakranggekendra_image mvi = new My_vakranggekendra_image();
            mvi.setID(6);
            mvi.setImageId(franchiseeDetails.getFlImageId());
            mvi.setImgetype("Floor Image");
            mvi.setImage(CommonUtils.StringToBitMap(franchiseeDetails.getFloorImageId()));
            mvi.setImageHash(CommonUtils.getImageSalt(franchiseeDetails.getFloorImageId()));
            my_vakranggekendra_images.add(mvi);
        }

        //Set IsChangePhoto flag of provided ImagePos
        if (IsChangePhoto) {
            My_vakranggekendra_image imageDto = my_vakranggekendra_images.get(selectedImagePos);
            imageDto.setChangedPhoto(true);
            my_vakranggekendra_images.set(selectedImagePos, imageDto);
        }
        return my_vakranggekendra_images;
    }

    public void showImageSlider(int pos) {

        if (imageSliderDialog != null && imageSliderDialog.isShowing()) {
            return;
        }

        //List<My_vakranggekendra_image> my_vakranggekendra_images = getImageSliderData(false, pos);

        if (my_vakranggekendra_images.size() > 0) {

            //Image Slider is used to show Sliding Images with selected position in list.
            imageSliderDialog = new ImageSliderDialog(getActivity(), new ArrayList<Object>(my_vakranggekendra_images), pos, new ImageSliderDialog.ISliderClickHandler() {
                @Override
                public void captureClick(int position) {

                    int id = getImageId(position);
                    if (id > 0) {
                        capturePhoto(id, CHANGE_PHOTO);
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

    //region Show Image Preview Dialog
    private void showImagePreviewDialog(Object object) {

        if (customImagePreviewDialog != null && customImagePreviewDialog.isShowing()) {
            customImagePreviewDialog.refresh(object);
            return;
        }

        if (object != null) {
            customImagePreviewDialog = new CustomImagePreviewDialog(getContext(), object, new CustomImagePreviewDialog.IImagePreviewDialogClicks() {
                @Override
                public void capturePhotoClick() {
                    capturePhoto(selectedImageViewId, CAMERA_CAPTURE);
                }

                @Override
                public void OkClick(Object object) {
                    Bitmap bitmapNew = ((ImageDto) object).getBitmap();
                    ImageView imageView = (ImageView) view.findViewById(selectedImageViewId);
                    imageView.setImageBitmap(bitmapNew);
                    // Set Image to Franchisee Detail
                    setImageIntoFrachiseeDetail(selectedImageViewId, bitmapNew, false);
                }
            });
            customImagePreviewDialog.show();
            customImagePreviewDialog.setCancelable(false);
            customImagePreviewDialog.allowChangePhoto(true);
            customImagePreviewDialog.allowRemarks(true);

        } else {
            Toast.makeText(getActivity(), "No photo available to preview.", Toast.LENGTH_SHORT).show();
        }
    }
    //endregion

    //region Capture Photo
    public void capturePhoto(int id, int REQUEST_CODE) {
        File file = CommonUtils.getOutputMediaFile(CommonUtils.FILE_IMAGE_TYPE);
        Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        picUri = Uri.fromFile(file); // create
        i.putExtra(MediaStore.EXTRA_OUTPUT, picUri); // set the image file
        i.putExtra("ImageId", picUri); // set the image file
        selectedImageViewId = id;
        startActivityForResult(i, REQUEST_CODE);
    }
    //endregion

}