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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
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

import in.vakrangee.franchisee.sitelayout.R;
import in.vakrangee.franchisee.sitelayout.Utils;
import in.vakrangee.franchisee.sitelayout.asyntask.AsyncSaveSiteReadinessVerification;
import in.vakrangee.franchisee.sitelayout.model.SiteReadinessCategoryDto;
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
public class SiteReadinessBrandingFragment extends Fragment implements View.OnClickListener, CategoryListAdapter.ISiteCheckHandler {

    private static final String TAG = "SiteReadinessBrandingFragment";

    private Context context;
    private RecyclerView recyclerViewCheckList;
    private List<SiteReadinessCheckListDto> siteCompletionCheckList;
    private FranchiseeDetails franchiseeDetails;
    private boolean IsEditable;
    private View view;
    private Logger logger;
    private static final int CAMERA_PIC_REQUEST = 1;
    private CustomImagePreviewDialog customImagePreviewDialog;
    private PermissionHandler permissionHandler;
    private Uri picUri;
    private GPSTracker gpsTracker;
    private String latitude = "", longitude = "", currentTimestamp = "";
    private SiteReadinessCheckListDto siteReadinessCheckListDto;
    private DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
    private Button btnCancel;
    private Button btnClear;
    private MaterialButton btnSubmitBranding;
    private List<SiteReadinessCategoryDto> brandingElementsList;
    private CategoryListAdapter categoryListAdapter;
    private int selectedPos = -1;
    private int selectedParentPos = -1;
    private Connection connection;
    private boolean IsFranchisee = false;
    private String mCurrentPhotoPath;

    private AsyncSaveSiteReadinessVerification asyncSaveSiteReadinessVerification = null;

    private SiteReadinessBrandingFragment() {
    }

    public SiteReadinessBrandingFragment(FranchiseeDetails franchiseeDetails, boolean IsEditable) {
        this.franchiseeDetails = franchiseeDetails;
        this.IsEditable = IsEditable;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag_site_readiness_branding, container, false);

        //Initialize data
        this.context = getContext();
        logger = Logger.getInstance(context);
        permissionHandler = new PermissionHandler(getActivity());
        gpsTracker = new GPSTracker(context);
        connection = new Connection(context);

        //Widget
        btnCancel = view.findViewById(R.id.btnCancel);
        btnClear = view.findViewById(R.id.btnClear);
        btnSubmitBranding = view.findViewById(R.id.btnSubmitBranding);

        final Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        btnClear.setTypeface(font);
        btnCancel.setTypeface(font);
        btnSubmitBranding.setTypeface(font);

        // Set Font Text
        btnClear.setText(new SpannableStringBuilder(new String(new char[]{0xf021}) + " " + getResources().getString(R.string.clear)));
        btnCancel.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + "  " + getResources().getString(R.string.cancel)));
        btnSubmitBranding.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " + getResources().getString(R.string.submit)));

        // Add Listener to Buttons
        btnClear.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSubmitBranding.setOnClickListener(this);

        recyclerViewCheckList = view.findViewById(R.id.recyclerViewCheckList);

        //Branding Category List
        //String data = "[{\"branding_element_id\":1,\"branding_element_name\":\"Branding elements         \",\"branding_element_description\":\"Branding elements         \",\"sub_brand\":[{\"branding_element_detail_id\":6,\"branding_element_type\":0,\"branding_element_name\":\"BCA Details\",\"branding_element_descriptions\":\"BCA Details\"},{\"branding_element_detail_id\":2,\"branding_element_type\":0,\"branding_element_name\":\"Clip-On frames\",\"branding_element_descriptions\":\"Clip-On frames – Qty 7\"},{\"branding_element_detail_id\":1,\"branding_element_type\":0,\"branding_element_name\":\"Leaflet holder\",\"branding_element_descriptions\":\"Leaflet holder\"},{\"branding_element_detail_id\":5,\"branding_element_type\":0,\"branding_element_name\":\"Main signboard frame\",\"branding_element_descriptions\":\"Main signboard frame - Photo and a field for Main signboard frame Length (Lambai)\"},{\"branding_element_detail_id\":9,\"branding_element_type\":0,\"branding_element_name\":\"Mandatory stickers\",\"branding_element_descriptions\":\"Mandatory stickers-(CCTV,No Smoking, No Helmets, No Spitting)- (Visa-Rupay-Master)\"},{\"branding_element_detail_id\":8,\"branding_element_type\":0,\"branding_element_name\":\"Push/Pull stickers\",\"branding_element_descriptions\":\"Push/Pull stickers\"},{\"branding_element_detail_id\":7,\"branding_element_type\":0,\"branding_element_name\":\"RBI guidelines\",\"branding_element_descriptions\":\"RBI guidelines\"},{\"branding_element_detail_id\":3,\"branding_element_type\":0,\"branding_element_name\":\"Suspended Acrylic frames\",\"branding_element_descriptions\":\"Suspended Acrylic frames - From Outside the Kendra\"},{\"branding_element_detail_id\":4,\"branding_element_type\":0,\"branding_element_name\":\"Suspended Acrylic frames\",\"branding_element_descriptions\":\"Suspended Acrylic frames - From Inside the Kendra\"}]}]";
        String interiorJsonArray = franchiseeDetails.getBranding_element_details();
        franchiseeDetails.getBranding_element_details();
        Gson gson = new GsonBuilder().create();
        brandingElementsList = gson.fromJson(interiorJsonArray.toString(), new TypeToken<ArrayList<SiteReadinessCategoryDto>>() {
        }.getType());

        if (connection.getVkid().startsWith("VL") || connection.getVkid().startsWith("VA")) {
            IsFranchisee = false;
        } else {
            IsFranchisee = true;
        }

        //categoryList = getReadinessCategoryList();
        categoryListAdapter = new CategoryListAdapter(context, IsFranchisee, Constants.SITE_READINESS_ATTRIBUTE_BRANDING_TYPE, brandingElementsList, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerViewCheckList.setLayoutManager(layoutManager);
        recyclerViewCheckList.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCheckList.setAdapter(categoryListAdapter);

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
/*

                    File file = CommonUtils.getOutputMediaFile(CommonUtils.FILE_IMAGE_TYPE);
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

                //picUri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
                startActivityForResult(takePictureIntent, CAMERA_PIC_REQUEST);
            }
        }
    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();

        if (Id == R.id.btnSubmitBranding) {
            //Validate Location in case of BE/DE
            //if (connection.getVkid().startsWith("VL") || connection.getVkid().startsWith("VA")) {
            // btnSubmitBranding.setClickable(false);
            btnSubmitBranding.setVisibility(View.INVISIBLE);
            boolean IsLocationWithinRange = IsValidLocation();
            if (!IsLocationWithinRange) {
                //btnSubmitBranding.setClickable(true);
                btnSubmitBranding.setVisibility(View.VISIBLE);
                AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.not_valid_location));
                return;
            } else {
                btnSubmitBranding.setVisibility(View.INVISIBLE);
            }

            save();
           /* } else {
                save();
            }*/
        }
    }

    public void save() {
        boolean IsValidated = IsElementsValidated();
        if (IsValidated) {
            //added by -nilesh dhola
            btnSubmitBranding.setVisibility(View.INVISIBLE);
            saveElementsData();
        } else {
            //added by -nilesh dhola
            btnSubmitBranding.setVisibility(View.VISIBLE);
            //  btnSubmitBranding.setClickable(true);
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

    public void saveElementsData() {

        asyncSaveSiteReadinessVerification = new AsyncSaveSiteReadinessVerification(getActivity(), brandingElementsList, franchiseeDetails, Constants.SITE_READINESS_BRANDING_TYPE, new AsyncSaveSiteReadinessVerification.Callback() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResult(String result) {
                try {
                    //added by -nilesh dhola
                    // btnSubmitBranding.setClickable(true);
                    btnSubmitBranding.setVisibility(View.VISIBLE);
                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                        return;
                    }

                    Log.e(TAG, "Result : " + result);
                    if (result.startsWith("OKAY")) {
                        AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.alert_msg_readiness_verification_success));
                        String vkIdTemp = connection.getVkid();
                        Utils.reloadReadinessVerificationData(Constants.NEXTGEN_SITE_READINESS_AND_VERIFICATION, getContext(), vkIdTemp, franchiseeDetails.getNextGenFranchiseeApplicationId());
                        ((SiteReadinessActivity) getActivity()).selectFragment(3);
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
        if (!TextUtils.isEmpty(siteReadinessCheckListDto.getStatus()) && siteReadinessCheckListDto.getStatus().equalsIgnoreCase(Constants.SEND_BACK_FOR_CORRECTION_BY_RM) || siteReadinessCheckListDto.getStatus().equalsIgnoreCase(Constants.REJECTED_BY_FIELD_TEAM))
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
                    List<SiteReadinessCheckListDto> siteCheckList = brandingElementsList.get(selectedParentPos).checkList;
                    SiteReadinessCheckListDto siteReadinessCheckListDto = ((SiteReadinessCheckListDto) object);
                    siteCheckList.set(selectedPos, siteReadinessCheckListDto);

                    //Parent Dto
                    SiteReadinessCategoryDto categoryDto = brandingElementsList.get(selectedParentPos);
                    categoryDto.checkList = siteCheckList;
                    brandingElementsList.set(selectedParentPos, categoryDto);
                    categoryListAdapter.notifyDataSetChanged();

                }
            });
            customImagePreviewDialog.show();
            customImagePreviewDialog.setCancelable(false);
            boolean IsChangePhotoEnabled = true;
            if (!TextUtils.isEmpty(siteReadinessCheckListDto.getStatus()) && (siteReadinessCheckListDto.getStatus().equalsIgnoreCase(Constants.APPROVED_BY_RM) || siteReadinessCheckListDto.getStatus().equalsIgnoreCase(Constants.ON_HOLD_BY_RM)))
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
        brandingElementsList.get(selectedParentPos).setChangedPhoto(true);
        List<SiteReadinessCheckListDto> siteCheckList = brandingElementsList.get(selectedParentPos).checkList;

        SiteReadinessCheckListDto imageDto = siteCheckList.get(selectedPos);
        imageDto.setElementUri(picUri);
        imageDto.setElementImgBitmap(bitmap);
        imageDto.setElementImageBase64(imageBase64);
        imageDto.setCapturedDateTime(dateTimeFormat.format(new Date()));
        return imageDto;

    }

    @Override
    public void cameraClick(int parentPosition, int position, SiteReadinessCheckListDto checkListDto) {
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

    public boolean IsElementsValidated() {

        for (int i = 0; i < brandingElementsList.size(); i++) {
            SiteReadinessCategoryDto categoryDto = brandingElementsList.get(i);
            List<SiteReadinessCheckListDto> checkList = categoryDto.checkList;

            //Validate Each Category
            int status = IsSubElementsValidated(checkList);
            if (status == -100) {
                return true;

            } else if (status >= 0) {
                String msg = "Please capture missing photo of " + checkList.get(status).getElementName() + " under Category: " + categoryDto.getName() + ", If it is completed.";
                AlertDialogBoxInfo.alertDialogShow(context, msg);
                return false;
            } else if (status == -2) {
                String msg = "Please select length of Main SignBoard Frame under Category: " + categoryDto.getName();
                AlertDialogBoxInfo.alertDialogShow(context, msg);
                return false;

            } else if (status == -3) {
                String msg = "Please select width of Main SignBoard Frame under Category: " + categoryDto.getName();
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

            //Length
            if (checkListDto.getElementName().equalsIgnoreCase("Main Signboard Frame")) {
                if (TextUtils.isEmpty(checkListDto.getLength()) || checkListDto.getLength().equalsIgnoreCase("0"))
                    return -2;

                //Width
                if (TextUtils.isEmpty(checkListDto.getWidth()) || checkListDto.getWidth().equalsIgnoreCase("0"))
                    return -3;
            }

        }
        return -100;
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
