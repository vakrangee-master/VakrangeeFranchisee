package in.vakrangee.franchisee.sitelayout.sitereadiness;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import in.vakrangee.franchisee.sitelayout.R;
import in.vakrangee.franchisee.sitelayout.Utils;
import in.vakrangee.franchisee.sitelayout.activity.MyVakrangeeKendraLocationDetailsNextGen;
import in.vakrangee.franchisee.sitelayout.asyntask.AsyncSaveSiteReadinessVerification;
import in.vakrangee.franchisee.sitelayout.model.SiteReadinessCategoryDto;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.commongui.imagepreview.CustomImagePreviewDialog;
import in.vakrangee.supercore.franchisee.model.FranchiseeDetails;
import in.vakrangee.supercore.franchisee.model.SiteReadinessCheckListDto;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.AppUtilsforLocationService;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.GPSTracker;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;
import in.vakrangee.supercore.franchisee.utils.Logger;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;

@SuppressLint("ValidFragment")
public class SiteReadinessInteriorFragment extends BaseFragment implements View.OnClickListener, CategoryListAdapter.ISiteCheckHandler {

    private static final String TAG = "SiteReadinessBrandingFragment";

    private Context context;
    private FranchiseeDetails franchiseeDetails;
    private boolean IsEditable;
    private View view;
    private Logger logger;
    private Uri picUri;
    private static final int CAMERA_PIC_REQUEST = 1;
    private CustomImagePreviewDialog customImagePreviewDialog;
    private GPSTracker gpsTracker;
    private String latitude = "", longitude = "", currentTimestamp = "";
    private PermissionHandler permissionHandler;
    private DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");

    MaterialButton btnCancel;
    MaterialButton btnClear;
    MaterialButton btnSubmitInterior;
    RecyclerView recyclerViewCategoryList;

    private int selectedPos = -1;
    private int selectedParentPos = -1;
    private SiteReadinessCheckListDto siteReadinessCheckListDto;
    private String FROM = "";

    //Layouts
    private static final String STORE_LAYOUT = "STORE LAYOUT";
    private static final String FURNITURE_AND_FINISHING = "FURNITURE AND FINISHING";
    private static final String FLOORING_LAYOUT = "FLOORING LAYOUT";
    private static final String CEILING_AND_LIGHTING_LAYOUT = "CEILING AND LIGHTING LAYOUT";
    private static final String ELECTRICAL_POINTS_FOR_FIXTURE = "ELECTRICAL POINTS FOR FIXTURE";

    private static final String APPROVED_BY_RM = "2";
    private static final String ON_HOLD_BY_RM = "6";
    private static final String SEND_BACK_FOR_CORRECTION_BY_RM = "3";
    private static final String REJECTED_BY_FIELD_TEAM = "7";

    private List<SiteReadinessCategoryDto> categoryList;
    private CategoryListAdapter categoryListAdapter;
    private AsyncSaveSiteReadinessVerification asyncSaveSiteReadinessVerification = null;
    private Connection connection;
    private boolean IsFranchisee = false;
    private String mCurrentPhotoPath;

    //regionStore Layout
    private String[] storeLayoutData = {"Frontage glass & door", "Space for UPS NVR/Router near ATM", "Fire extinguisher (2kg) provided", "store room/storage", "Digital Signage on wall", "Clip on frames display on wall"};
    //endregion

    //region Furniture and Furnishing Layout
    private String[] furnitureLayoutData = {"Counter Tables - (Laminate Colour - Walnut & Smog)", "Glass on banking counter", "Back Storage Cabinet - (Laminate Colour - Walnut & Smog)", "Waiting Bench - (Laminate Colour - Walnut)", "Glass shelf for Cash Counting Machine near to ATM", "Storage Cabinet - Frosty White", "Chairs (as per Design Manual)", "Wall Paint finish - (Colour - morning glory(White) / Cold flint (Grey))"};
    //endregion

    //region Flooring Layout
    private String[] flooringLayoutData = {"Flooring - Tiles / PVC (Vinyl)"};
    //endregion

    //region Ceiling and Lighting Layout
    private String[] ceilingLayoutData = {"Ceiling Colour - Morning Glory (White)", "Lights - LED/CFL/Tubelights"};
    //endregion

    //region Electrical points for fixture
    private String[] electricalLayoutData = {"Switches & Sockets - 5 sockets on each table", "Electrical point for printer (15 AMP on each workstations)", "Electrical point for Cash Counting Machine, Weighing Machine, Pocket Router near back storage", "Electrical point for Digital Signage at 6 ft height", "Electrical UPS point for Router/NVR near ATM", "15 AMP UPS Points for ATM near to the ATM", "Electrical point for Cash Counting Machine near to ATM glass shelf", "Earthing for ATM Machine", "Air Conditioner/Air Cooler (as required)", "Fans (as required)"};
    //endregion

    private SiteReadinessInteriorFragment() {
    }

    public SiteReadinessInteriorFragment(FranchiseeDetails franchiseeDetails, boolean IsEditable) {
        this.franchiseeDetails = franchiseeDetails;
        this.IsEditable = IsEditable;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag_site_readiness_interior, container, false);

        final Typeface font = android.graphics.Typeface.createFromAsset(getActivity().getAssets(), "fontawesome-webfont.ttf");

        //Initialize data
        this.context = getContext();
        logger = Logger.getInstance(context);
        gpsTracker = new GPSTracker(context);
        connection = new Connection(context);
        permissionHandler = new PermissionHandler(getActivity());
        ButterKnife.bind(this, view);

        btnCancel = view.findViewById(R.id.btnCancel);
        btnClear = view.findViewById(R.id.btnClear);
        btnSubmitInterior = view.findViewById(R.id.btnSubmitInterior);
        recyclerViewCategoryList = view.findViewById(R.id.recyclerViewCategoryList);

        btnClear.setTypeface(font);
        btnCancel.setTypeface(font);
        btnSubmitInterior.setTypeface(font);


        // Set Font Text
        btnClear.setText(new SpannableStringBuilder(new String(new char[]{0xf021}) + " " + getResources().getString(R.string.clear)));
        btnCancel.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + "  " + getResources().getString(R.string.cancel)));
        btnSubmitInterior.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " + getResources().getString(R.string.submit)));

        // Add Listener to Buttons
        btnClear.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSubmitInterior.setOnClickListener(this);

        //Interior Category List
        //String data = "[{\"design_element_id\":1,\"design_element_name\":\"Flooring \",\"design_element_description\":\"Flooring \",\"sub_cat\":[{\"design_element_detail_id\":1,\"design_element_name\":\"Flooring \",\"design_element_description\":\"Flooring \",\"mandatory\":1}]},{\"design_element_id\":2,\"design_element_name\":\"Ceiling \",\"design_element_description\":\"Ceiling \",\"sub_cat\":[{\"design_element_detail_id\":2,\"design_element_name\":\"Ceiling with LED Lights \",\"design_element_description\":\"Ceiling with LED Lights \",\"mandatory\":1}]},{\"design_element_id\":3,\"design_element_name\":\"Electrical      \",\"design_element_description\":\"Electrical      \",\"sub_cat\":[{\"design_element_detail_id\":3,\"design_element_name\":\"Concealed Wiring \",\"design_element_description\":\"Concealed Wiring \",\"mandatory\":1},{\"design_element_detail_id\":4,\"design_element_name\":\"Point for Digital Signage \",\"design_element_description\":\"Point for Digital Signage \",\"mandatory\":1},{\"design_element_detail_id\":5,\"design_element_name\":\"UPS/ATM Points \",\"design_element_description\":\"UPS/ATM Points \",\"mandatory\":1},{\"design_element_detail_id\":6,\"design_element_name\":\"CCTV point \",\"design_element_description\":\"CCTV point \",\"mandatory\":1}]},{\"design_element_id\":4,\"design_element_name\":\"Painting      \",\"design_element_description\":\"Painting      \",\"sub_cat\":[{\"design_element_detail_id\":7,\"design_element_name\":\"Left Wall \",\"design_element_description\":\"Left Wall \",\"mandatory\":1},{\"design_element_detail_id\":8,\"design_element_name\":\"Left Wall\",\"design_element_description\":\"Left Wall\",\"mandatory\":1},{\"design_element_detail_id\":9,\"design_element_name\":\"Right Wall\",\"design_element_description\":\"Right Wall\",\"mandatory\":1},{\"design_element_detail_id\":10,\"design_element_name\":\"Back Wall\",\"design_element_description\":\"Back Wall\",\"mandatory\":1},{\"design_element_detail_id\":11,\"design_element_name\":\"Front Wall\",\"design_element_description\":\"Front Wall\",\"mandatory\":1},{\"design_element_detail_id\":12,\"design_element_name\":\"Outside Wall\",\"design_element_description\":\"Outside Wall (If applicable)\",\"mandatory\":1},{\"design_element_detail_id\":13,\"design_element_name\":\"Rolling Shutter\",\"design_element_description\":\"Rolling Shutter\",\"mandatory\":1}]},{\"design_element_id\":5,\"design_element_name\":\"Furniture\",\"design_element_description\":\"Furniture\",\"sub_cat\":[{\"design_element_detail_id\":14,\"design_element_name\":\"Bank Counter Table with Chairs\",\"design_element_description\":\"Bank Counter Table with Chairs - Front View\",\"mandatory\":1},{\"design_element_detail_id\":15,\"design_element_name\":\"Bank Counter Table with Chairs\",\"design_element_description\":\"Bank Counter Table with Chairs - Back View\",\"mandatory\":1},{\"design_element_detail_id\":22,\"design_element_name\":\"Franchisee/Owner desk\",\"design_element_description\":\"Franchisee/Owner desk - Front View\",\"mandatory\":1},{\"design_element_detail_id\":23,\"design_element_name\":\"Waiting Bench\",\"design_element_description\":\"Waiting Bench\",\"mandatory\":1},{\"design_element_detail_id\":24,\"design_element_name\":\"Storage Cabinet \",\"design_element_description\":\"Storage Cabinet \",\"mandatory\":1},{\"design_element_detail_id\":25,\"design_element_name\":\"Store Room/ Storage shelf on wall\",\"design_element_description\":\"Store Room/ Storage shelf on wall\",\"mandatory\":1},{\"design_element_detail_id\":26,\"design_element_name\":\"Main Entrance Glass Door & Fixed Glass\",\"design_element_description\":\"Main Entrance Glass Door & Fixed Glass\",\"mandatory\":1},{\"design_element_detail_id\":33,\"design_element_name\":\"E-Commerce & Logistics services counter table with chairs \",\"design_element_description\":\"E-Commerce & Logistics services counter table with chairs - Front View\",\"mandatory\":1},{\"design_element_detail_id\":34,\"design_element_name\":\"E-Commerce & Logistics services counter table with chairs \",\"design_element_description\":\"E-Commerce & Logistics services counter table with chairs - Back View\",\"mandatory\":1},{\"design_element_detail_id\":35,\"design_element_name\":\"Insurance & Financial services counter table with chairs \",\"design_element_description\":\"Insurance & Financial services counter table with chairs - Front View\",\"mandatory\":1},{\"design_element_detail_id\":36,\"design_element_name\":\"Insurance & Financial services counter table with chairs \",\"design_element_description\":\"Insurance & Financial services counter table with chairs - Back View\",\"mandatory\":1},{\"design_element_detail_id\":37,\"design_element_name\":\"E- Governance & Other services counter table with chairs \",\"design_element_description\":\"E- Governance & Other services counter table with chairs - Front View\",\"mandatory\":1},{\"design_element_detail_id\":38,\"design_element_name\":\"E- Governance & Other services counter table with chairs \",\"design_element_description\":\"E- Governance & Other services counter table with chairs - Back View\",\"mandatory\":1}]},{\"design_element_id\":6,\"design_element_name\":\"Fixtures\",\"design_element_description\":\"Fixtures\",\"sub_cat\":[{\"design_element_detail_id\":27,\"design_element_name\":\"AC\",\"design_element_description\":\"AC\",\"mandatory\":1},{\"design_element_detail_id\":28,\"design_element_name\":\"Water Cooler\",\"design_element_description\":\"Water Cooler\",\"mandatory\":1},{\"design_element_detail_id\":29,\"design_element_name\":\"Dust Bins - Qty 6\",\"design_element_description\":\"Dust Bins - Qty 6\",\"mandatory\":1},{\"design_element_detail_id\":30,\"design_element_name\":\"Inverter/Generator\",\"design_element_description\":\"Inverter/Generator (If applicable)\",\"mandatory\":1},{\"design_element_detail_id\":31,\"design_element_name\":\"Fire extinguisher\",\"design_element_description\":\"Fire extinguisher\",\"mandatory\":1},{\"design_element_detail_id\":32,\"design_element_name\":\"Softboard\",\"design_element_description\":\"Softboard\",\"mandatory\":1}]}]";
        String interiorJsonArray = franchiseeDetails.getDesignElements();
        Gson gson = new GsonBuilder().create();
        categoryList = gson.fromJson(interiorJsonArray.toString(), new TypeToken<ArrayList<SiteReadinessCategoryDto>>() {
        }.getType());

        if (connection.getVkid().startsWith("VL") || connection.getVkid().startsWith("VA")) {
            IsFranchisee = false;
        } else {
            IsFranchisee = true;
        }

        //categoryList = getReadinessCategoryList();
        categoryListAdapter = new CategoryListAdapter(context, IsFranchisee, Constants.SITE_READINESS_ATTRIBUTE_DESIGN_TYPE, categoryList, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerViewCategoryList.setLayoutManager(layoutManager);
        recyclerViewCategoryList.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCategoryList.setAdapter(categoryListAdapter);

        return view;
    }

    public void startCamera(View view) {
        //If the app has not the permission then asking for the permission
        permissionHandler.requestPermission(view, Manifest.permission.CAMERA, getString(R.string.needs_permission_camera_msg), new IPermission() {
            @Override
            public void IsPermissionGranted(boolean IsGranted) {
                if (IsGranted) {
                    try {
                        dispatchTakePictureIntent();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    /*File file = CommonUtils.getOutputMediaFile(CommonUtils.FILE_IMAGE_TYPE);
                    Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    picUri = Uri.fromFile(file); // create
                    i.putExtra(MediaStore.EXTRA_OUTPUT, picUri); // set the image file
                    i.putExtra("ImageId", picUri); // set the image file
                    startActivityForResult(i, CAMERA_PIC_REQUEST);*/
                }
            }
        });
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        storageDir.mkdirs();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
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
                picUri = FileProvider.getUriForFile(getContext(), getContext().getApplicationContext().getPackageName() + ".provider", photoFile);

                // picUri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
                startActivityForResult(takePictureIntent, CAMERA_PIC_REQUEST);
            }
        }
    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();

        if (Id == R.id.btnSubmitInterior) {
            //Validate Location in case of BE/DE and Franchisee
            //if (connection.getVkid().startsWith("VL") || connection.getVkid().startsWith("VA")) {

            // btnSubmitInterior.setClickable(false);
            btnSubmitInterior.setVisibility(View.INVISIBLE);
            boolean IsLocationWithinRange = IsValidLocation();
            if (!IsLocationWithinRange) {
                //  btnSubmitInterior.setClickable(true);
                btnSubmitInterior.setVisibility(View.VISIBLE);
                AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.not_valid_location));
                return;
            } else {
                btnSubmitInterior.setVisibility(View.INVISIBLE);
                // btnSubmitInterior.setClickable(false);
            }

            save();
            /*} else {
                save();
            }*/
        } else if (Id == R.id.btnCancel) {
            cancel();
        }
    }

    public boolean IsValidLocation() {
        if (gpsTracker.canGetLocation()) {
            if (TextUtils.isEmpty(franchiseeDetails.getLatitude()) || TextUtils.isEmpty(franchiseeDetails.getLongitude()) || franchiseeDetails.getWipLocationRange() == 0)
                return true;

            int distance = (int) gpsTracker.getDistance(Double.parseDouble(franchiseeDetails.getLatitude()), Double.parseDouble(franchiseeDetails.getLongitude()));
            if (distance <= franchiseeDetails.getWipLocationRange())
                return true;
        }
        return false;
    }

    public void save() {
        showProgressSpinner(context);
        boolean IsValidated = IsInteriorElementsValidated();
        dismissProgressSpinner();
        if (IsValidated) {
            //added by -nilesh dhola
            btnSubmitInterior.setVisibility(View.INVISIBLE);
            //btnSubmitInterior.setClickable(false);
            saveElementData();
        } else {
            //added by -nilesh dhola
            btnSubmitInterior.setVisibility(View.VISIBLE);

        }
    }

    public void saveElementData() {
        asyncSaveSiteReadinessVerification = new AsyncSaveSiteReadinessVerification(getActivity(), categoryList, franchiseeDetails, Constants.SITE_READINESS_INTERIOR_TYPE, new AsyncSaveSiteReadinessVerification.Callback() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResult(String result) {
                try {

                    //added by -nilesh dhola
                    btnSubmitInterior.setVisibility(View.VISIBLE);

                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                        return;
                    }

                    Log.e(TAG, "Result : " + result);
                    if (result.startsWith("OKAY")) {
                        AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.alert_msg_readiness_verification_success));
                        String vkIdTemp = connection.getVkid();
                        Utils.reloadReadinessVerificationData(Constants.NEXTGEN_SITE_READINESS_AND_VERIFICATION, getContext(), vkIdTemp, franchiseeDetails.getNextGenFranchiseeApplicationId());
                        ((SiteReadinessActivity) getActivity()).selectFragment(2);

                    } else {
                        AlertDialogBoxInfo.showOkDialog(getActivity(), getResources().getString(R.string.alert_msg_readiness_verification_fail));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                }
            }
        });
        asyncSaveSiteReadinessVerification.execute("");
    }

    public void cancel() {
        if (connection.getVkid().toUpperCase().startsWith("VL") || connection.getVkid().toUpperCase().startsWith("VA")) {
            Intent intent = new Intent(context, MyVakrangeeKendraLocationDetailsNextGen.class);
            intent.putExtra("MODE", Constants.NEXTGEN_SITE_READINESS_AND_VERIFICATION);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        } else {
           /* Intent intent = new Intent(context, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);*/
            getActivity().finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK) {
            try {
                Uri imageUri = Uri.parse(mCurrentPhotoPath);
                picUri = imageUri;

                getLocationAndTimestamp();
                Bitmap bitmapNew = ImageUtils.getBitmap(getActivity().getContentResolver(), picUri, latitude, longitude, currentTimestamp);

                //BitMap with TimeStamp on it
                bitmapNew = ImageUtils.stampWithTimeInBitmap(bitmapNew);

                /*//START: TEsting
                String capturedAt = GPSUtil.getCompleteAddressString(context, Double.parseDouble(latitude), Double.parseDouble(longitude));
                bitmapNew = ImageUtils.stampWithTimeAndAddressInBitmap(bitmapNew, capturedAt);
                //END*/

                String imageBase64 = ImageUtils.updateExifData(picUri, bitmapNew, latitude, longitude, currentTimestamp);

                ImageView imageView = new ImageView(context);
                imageView.setImageBitmap(bitmapNew);

                if (!CommonUtils.isLandscapePhoto(picUri, imageView)) {
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getString(R.string.landscape_mode_allowed));
                } else {
                    SiteReadinessCheckListDto checkListDto = savePhoto(bitmapNew, imageBase64);
                    checkListDto.setChangedPhoto(true);
                    showImagePreviewDialog((Object) checkListDto);
                }

            } catch (Exception e) {
                e.printStackTrace();
                AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
            }
        }
    }

    public void getLocationAndTimestamp() {
        //Get Current location and time stamp
        if (gpsTracker.canGetLocation()) {
            latitude = String.valueOf(gpsTracker.getLatitude());
            longitude = String.valueOf(gpsTracker.getLongitude());
            currentTimestamp = gpsTracker.getFormattedDateTime();
        }
    }

    private void showImagePreviewDialog(Object object) {

        boolean IsRejected = false;
        if (!TextUtils.isEmpty(siteReadinessCheckListDto.getStatus()) && (siteReadinessCheckListDto.getStatus().equalsIgnoreCase(SEND_BACK_FOR_CORRECTION_BY_RM) || siteReadinessCheckListDto.getStatus().equalsIgnoreCase(REJECTED_BY_FIELD_TEAM)))
            IsRejected = true;

        if (customImagePreviewDialog != null && customImagePreviewDialog.isShowing()) {
            customImagePreviewDialog.refresh(object);
            customImagePreviewDialog.allowImageRejected(IsRejected && !siteReadinessCheckListDto.isChangedPhoto(), siteReadinessCheckListDto.getStatus());
            return;
        }

        if (object != null) {
            customImagePreviewDialog = new CustomImagePreviewDialog(context, object, new CustomImagePreviewDialog.IImagePreviewDialogClicks() {

                @Override
                public void capturePhotoClick() {
                    boolean IsLocationWithinRange = IsValidLocation();

                    //GPS OFF
                    if (!IsLocationEnabled()) {
                        displayAlertToEnableLocation();
                        return;
                    }

                    //Location Range
                    if (!IsLocationWithinRange) {
                        AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.not_valid_location));
                        return;
                    }
                    startCamera(view);
                }

                @Override
                public void OkClick(Object object) {
                    List<SiteReadinessCheckListDto> siteCheckList = categoryList.get(selectedParentPos).checkList;
                    SiteReadinessCheckListDto siteReadinessCheckListDto = ((SiteReadinessCheckListDto) object);
                    siteCheckList.set(selectedPos, siteReadinessCheckListDto);

                    //Parent Dto
                    SiteReadinessCategoryDto categoryDto = categoryList.get(selectedParentPos);
                    categoryDto.checkList = siteCheckList;
                    categoryList.set(selectedParentPos, categoryDto);
                    categoryListAdapter.updateRefreshedPosition(selectedParentPos);
                    categoryListAdapter.notifyDataSetChanged();

                }
            });
            customImagePreviewDialog.show();
            customImagePreviewDialog.setCancelable(false);

            boolean IsChangePhotoEnabled = true;
            if (!TextUtils.isEmpty(siteReadinessCheckListDto.getStatus()) && (siteReadinessCheckListDto.getStatus().equalsIgnoreCase(APPROVED_BY_RM) || siteReadinessCheckListDto.getStatus().equalsIgnoreCase(ON_HOLD_BY_RM)))
                IsChangePhotoEnabled = false;

            customImagePreviewDialog.allowChangePhoto(IsChangePhotoEnabled);
            customImagePreviewDialog.allowSaveOption(IsChangePhotoEnabled);
            customImagePreviewDialog.allowImageRejected(IsRejected && !siteReadinessCheckListDto.isChangedPhoto(), siteReadinessCheckListDto.getStatus());
            customImagePreviewDialog.allowRemarks(false);

        } else {
            Toast.makeText(getActivity(), "No photo available to preview.", Toast.LENGTH_SHORT).show();
        }
    }

    public SiteReadinessCheckListDto savePhoto(Bitmap bitmap, String imageBase64) {
        categoryList.get(selectedParentPos).setChangedPhoto(true);
        List<SiteReadinessCheckListDto> siteCheckList = categoryList.get(selectedParentPos).checkList;

        SiteReadinessCheckListDto imageDto = siteCheckList.get(selectedPos);
        imageDto.setElementUri(picUri);
        imageDto.setElementImgBitmap(bitmap);
        imageDto.setElementImageBase64(imageBase64);
        imageDto.setCapturedDateTime(dateTimeFormat.format(new Date()));
        return imageDto;

    }

    @Override
    public void cameraClick(int parentPosition, int position, SiteReadinessCheckListDto checkListDto) {
        FROM = checkListDto.getTYPE();
        selectedPos = position;
        selectedParentPos = parentPosition;
        siteReadinessCheckListDto = checkListDto;
        if (TextUtils.isEmpty(checkListDto.getElementImageBase64())) {
            boolean IsLocationWithinRange = IsValidLocation();

            //GPS OFF
            if (!IsLocationEnabled()) {
                displayAlertToEnableLocation();
                return;
            }

            //Location Range
            if (!IsLocationWithinRange) {
                AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.not_valid_location));
                return;
            }

            startCamera(view);
        } else {
            showImagePreviewDialog(siteReadinessCheckListDto);
        }
    }

    public boolean IsInteriorElementsValidated() {

        for (int i = 0; i < categoryList.size(); i++) {
            SiteReadinessCategoryDto categoryDto = categoryList.get(i);
            List<SiteReadinessCheckListDto> checkList = categoryDto.checkList;

            //Validate Each Category
            int status = IsSubElementsValidated(checkList);
            if (status != -1) {
                String msg = "Please capture missing photo of " + checkList.get(status).getElementName() + " under Category: " + categoryDto.getName() + ", If it is completed.";
                AlertDialogBoxInfo.alertDialogShow(context, msg);
                return false;
            }
        }
        return true;
    }

    public int IsSubElementsValidated(List<SiteReadinessCheckListDto> siteCheckList) {

        for (int i = 0; i < siteCheckList.size(); i++) {
            SiteReadinessCheckListDto checkListDto = siteCheckList.get(i);

            //Validate If Completed and Photo is taken
            if (!TextUtils.isEmpty(checkListDto.getCompleted()) && (checkListDto.getCompleted().equalsIgnoreCase("1") || checkListDto.getCompleted().equalsIgnoreCase("2"))) {
                if (TextUtils.isEmpty(checkListDto.getElementImageBase64()))
                    return i;
            }
        }
        return -1;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (asyncSaveSiteReadinessVerification != null && !asyncSaveSiteReadinessVerification.isCancelled()) {
            asyncSaveSiteReadinessVerification.cancel(true);
        }
    }

    public boolean IsLocationEnabled() {
        if (AppUtilsforLocationService.checkPlayServices(getActivity())) {
            if (AppUtilsforLocationService.isLocationEnabled(context))
                return true;
        } else {
            Toast toast = Toast.makeText(context, "Location not supported in this device", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        return false;
    }

    public void displayAlertToEnableLocation() {
        // notify user
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage("Please enable GPS/Location.");
        dialog.setPositiveButton("Open location settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(myIntent);
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
            }
        });
        dialog.show();
    }

}
