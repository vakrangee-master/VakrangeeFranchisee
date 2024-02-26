package in.vakrangee.franchisee.gwr;

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
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.vakrangee.franchisee.BuildConfig;
import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.commongui.imagepreview.CustomImagePreviewDialog;
import in.vakrangee.supercore.franchisee.model.GWRCheckListDto;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.GPSTracker;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.utils.Logger;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;

@SuppressLint("ValidFragment")
public class GWRFragment extends BaseFragment implements View.OnClickListener, GWRCategoryAdapter.ISiteCheckHandler {

    private static final String TAG = "GWRFragment";
    private View view;
    private Context context;
    private GPSTracker gpsTracker;
    private Logger logger;
    private Connection connection;
    private PermissionHandler permissionHandler;
    private static final int CAMERA_PIC_REQUEST = 1;
    private Uri picUri;
    private String latitude = "", longitude = "", currentTimestamp = "";
    private int selectedPos = -1;
    private int selectedParentPos = -1;
    private DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");

    //region References
    @BindView(R.id.btnSubmitGWRInterior)
    Button btnSubmitGWRInterior;
    @BindView(R.id.recyclerViewGWRCategoryList)
    RecyclerView recyclerViewGWRCategoryList;
    //endregion
    private TextView txtNoDataMsg;
    private LinearLayout layoutProgress;

    private List<GWRCategoryDto> categoryList;
    private GWRCategoryAdapter categoryListAdapter;
    private String mCurrentPhotoPath;
    private GWRCheckListDto siteReadinessCheckListDto;
    private CustomImagePreviewDialog customImagePreviewDialog;
    private AsyncSaveGWRActivityDetails asyncSaveGWRActivityDetails = null;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alert;

    @BindView(R.id.nestedScrollview)
    NestedScrollView nestedScrollview;
    private boolean IsEditable = false;
    private String vkid;
    private InternetConnection internetConnection;
    private AsyncGetGWR asyncGetGWR = null;
    private boolean IsAlreadyExecuted = false;

    private String type;

    public GWRFragment() {
    }

    public GWRFragment(boolean IsEditable, String type) {
        this.IsEditable = IsEditable;
        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag_gwr, container, false);
        final Typeface font = android.graphics.Typeface.createFromAsset(getActivity().getAssets(), "fontawesome-webfont.ttf");

        //Initialize data
        this.context = getContext();
        logger = Logger.getInstance(context);
        gpsTracker = new GPSTracker(context);
        connection = new Connection(context);
        permissionHandler = new PermissionHandler(getActivity());
        internetConnection = new InternetConnection(context);
        ButterKnife.bind(this, view);

        layoutProgress = (LinearLayout) view.findViewById(R.id.layoutProgress);
        txtNoDataMsg = view.findViewById(R.id.txtNoDataMsg);

        btnSubmitGWRInterior.setTypeface(font);

        // Set Font Text
        btnSubmitGWRInterior.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " + getResources().getString(R.string.submit)));

        // Add Listener to Buttons
        btnSubmitGWRInterior.setOnClickListener(this);

        return view;
    }

    public void switchToGWR() {
        layoutProgress.setVisibility(View.VISIBLE);
        txtNoDataMsg.setVisibility(View.GONE);
        recyclerViewGWRCategoryList.setVisibility(View.GONE);

        if (!internetConnection.isNetworkAvailable(context)) {
            AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.internetCheck));
        } else {

            Connection connection = new Connection(context);
            vkid = connection.getVkid();
            permissionHandler.requestPermission(btnSubmitGWRInterior, Manifest.permission.READ_PHONE_STATE, getString(R.string.needs_permission_phone_state_msg), new IPermission() {
                @Override
                public void IsPermissionGranted(boolean IsGranted) {
                    if (IsGranted) {
                        if (!internetConnection.isNetworkAvailable(context)) {
                            AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.internetCheck));
                        } else {
                            asyncGetGWR = new AsyncGetGWR(context, vkid, type, new AsyncGetGWR.Callback() {
                                @Override
                                public void onResult(String result) {
                                    layoutProgress.setVisibility(View.GONE);
                                    processData(result);
                                }
                            });
                            asyncGetGWR.execute("");
                        }
                    }
                }
            });
        }
    }

    private void processData(String result) {
        if (TextUtils.isEmpty(result)) {
            AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
            txtNoDataMsg.setVisibility(View.VISIBLE);
            recyclerViewGWRCategoryList.setVisibility(View.GONE);
            return;
        }

        // Handle Error Response From Server.
        if (result.startsWith("ERROR|")) {

            String errMsg = null;

            try {
                StringTokenizer tokens = new StringTokenizer(result, "|");
                tokens.nextToken();     // Jump to next Token
                errMsg = tokens.nextToken();
            } catch (Exception e) {
                e.getMessage();
            }


            if (!TextUtils.isEmpty(errMsg)) {
                AlertDialogBoxInfo.alertDialogShow(context, errMsg);
                txtNoDataMsg.setVisibility(View.VISIBLE);
                recyclerViewGWRCategoryList.setVisibility(View.GONE);
            }
            return;
        }

        //Process response
        if (result.startsWith("OKAY|")) {
            StringTokenizer st1 = new StringTokenizer(result, "|");
            String key = st1.nextToken();
            String data = st1.nextToken();

            if (TextUtils.isEmpty(data)) {
                AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                txtNoDataMsg.setVisibility(View.VISIBLE);
                recyclerViewGWRCategoryList.setVisibility(View.GONE);
                return;
            }

            //Process Response
            txtNoDataMsg.setVisibility(View.GONE);
            recyclerViewGWRCategoryList.setVisibility(View.VISIBLE);
            reloadGWRDetails(data);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!IsAlreadyExecuted) {
            switchToGWR();
            IsAlreadyExecuted = true;
        }
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
    }

    public void reloadGWRDetails(String gwrData) {
        //gwrData = "[{\"sub_cat\":[{\"is_correction\":0,\"element_description\":\"Wall Clock Placement\",\"completed\":0,\"element_detail_id\":41,\"element_name\":\"Wall Clock Placement\",\"mandatory\":1,\"remarks\":null,\"activity_status\":0},{\"is_correction\":0,\"element_description\":\"Wall Time Synchronization\",\"completed\":0,\"element_detail_id\":42,\"element_name\":\"Wall Time Synchronization\",\"mandatory\":1,\"remarks\":null,\"activity_status\":0},{\"is_correction\":0,\"element_description\":\"Kendra Name Plate\",\"completed\":0,\"element_detail_id\":43,\"element_name\":\"Kendra Name Plate\",\"mandatory\":1,\"remarks\":null,\"activity_status\":0},{\"is_correction\":0,\"element_description\":\"Camera Date Time Synchronization\",\"completed\":0,\"element_detail_id\":44,\"element_name\":\"Camera Date Time Synchronization\",\"mandatory\":1,\"remarks\":null,\"activity_status\":0}],\"element_id\":3,\"element_description\":\"Guinness Activity\",\"element_name\":\"Guinness Activity\"}]";
        String interiorJsonArray = gwrData;
        Gson gson = new GsonBuilder().create();
        categoryList = gson.fromJson(interiorJsonArray.toString(), new TypeToken<ArrayList<GWRCategoryDto>>() {
        }.getType());

        categoryListAdapter = new GWRCategoryAdapter(context, true, Constants.SITE_READINESS_ATTRIBUTE_DESIGN_TYPE, categoryList, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerViewGWRCategoryList.setLayoutManager(layoutManager);
        recyclerViewGWRCategoryList.setItemAnimator(new DefaultItemAnimator());
        recyclerViewGWRCategoryList.setAdapter(categoryListAdapter);
    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();

        if (Id == R.id.btnSubmitGWRInterior) {
            save();
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
                String imageBase64 = ImageUtils.updateExifData(picUri, bitmapNew, latitude, longitude, currentTimestamp);

                ImageView imageView = new ImageView(context);
                imageView.setImageBitmap(bitmapNew);

                if (!CommonUtils.isLandscapePhoto(picUri, imageView)) {
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getString(R.string.landscape_mode_allowed));
                } else {
                    GWRCheckListDto checkListDto = savePhoto(bitmapNew, imageBase64);
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
        /*if (!TextUtils.isEmpty(siteReadinessCheckListDto.getStatus()) && siteReadinessCheckListDto.getStatus().equalsIgnoreCase(Constants.GWR_ACTIVITY_SEND_BACK_FOR_CORRECTION))
            IsRejected = true;*/

        if (customImagePreviewDialog != null && customImagePreviewDialog.isShowing()) {
            customImagePreviewDialog.refresh(object);
            customImagePreviewDialog.allowImageRejected(IsRejected && !siteReadinessCheckListDto.isChangedPhoto(), siteReadinessCheckListDto.getStatus());
            return;
        }

        if (object != null) {
            customImagePreviewDialog = new CustomImagePreviewDialog(context, object, new CustomImagePreviewDialog.IImagePreviewDialogClicks() {

                @Override
                public void capturePhotoClick() {
                    startCamera(view);
                }

                @Override
                public void OkClick(Object object) {
                    List<GWRCheckListDto> siteCheckList = categoryList.get(selectedParentPos).checkList;
                    GWRCheckListDto siteReadinessCheckListDto = ((GWRCheckListDto) object);
                    siteCheckList.set(selectedPos, siteReadinessCheckListDto);

                    //Parent Dto
                    GWRCategoryDto categoryDto = categoryList.get(selectedParentPos);
                    categoryDto.checkList = siteCheckList;
                    categoryList.set(selectedParentPos, categoryDto);
                    categoryListAdapter.notifyDataSetChanged();

                }
            });
            customImagePreviewDialog.show();
            customImagePreviewDialog.setCancelable(false);
            boolean IsChangePhotoEnabled = true;
            if (!TextUtils.isEmpty(siteReadinessCheckListDto.getStatus()) && (siteReadinessCheckListDto.getStatus().equalsIgnoreCase(Constants.GWR_ACTIVITY_APPROVED)))
                IsChangePhotoEnabled = false;

            customImagePreviewDialog.allowChangePhoto(IsChangePhotoEnabled);
            customImagePreviewDialog.allowSaveOption(IsChangePhotoEnabled);
            customImagePreviewDialog.allowImageRejected(IsRejected && !siteReadinessCheckListDto.isChangedPhoto(), siteReadinessCheckListDto.getStatus());
            customImagePreviewDialog.allowRemarks(false);

        } else {
            Toast.makeText(getActivity(), "No photo available to preview.", Toast.LENGTH_SHORT).show();
        }
    }

    public GWRCheckListDto savePhoto(Bitmap bitmap, String imageBase64) {
        categoryList.get(selectedParentPos).setChangedPhoto(true);
        List<GWRCheckListDto> siteCheckList = categoryList.get(selectedParentPos).checkList;

        GWRCheckListDto imageDto = siteCheckList.get(selectedPos);
        imageDto.setElementUri(picUri);
        imageDto.setElementImgBitmap(bitmap);
        imageDto.setElementImageBase64(imageBase64);
        imageDto.setCapturedDateTime(dateTimeFormat.format(new Date()));
        return imageDto;

    }

    @Override
    public void cameraClick(int parentPosition, int position, GWRCheckListDto checkListDto) {
        selectedPos = position;
        selectedParentPos = parentPosition;
        siteReadinessCheckListDto = checkListDto;
        if (TextUtils.isEmpty(checkListDto.getElementImageBase64())) {
            startCamera(view);
        } else {
            showImagePreviewDialog(siteReadinessCheckListDto);
        }
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
                picUri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
                startActivityForResult(takePictureIntent, CAMERA_PIC_REQUEST);
            }
        }
    }

    public void save() {
        showProgressSpinner(context);
        boolean IsValidated = IsGWRActivityValidated();
        dismissProgressSpinner();
        if (IsValidated) {
            saveElementData();
        }
    }

    public boolean IsGWRActivityValidated() {

        for (int i = 0; i < categoryList.size(); i++) {
            GWRCategoryDto categoryDto = categoryList.get(i);
            List<GWRCheckListDto> checkList = categoryDto.checkList;

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

    public int IsSubElementsValidated(List<GWRCheckListDto> siteCheckList) {

        for (int i = 0; i < siteCheckList.size(); i++) {
            GWRCheckListDto checkListDto = siteCheckList.get(i);

            //Validate If Completed and Photo is taken
            if (!TextUtils.isEmpty(checkListDto.getCompleted()) && (checkListDto.getCompleted().equalsIgnoreCase("1") || checkListDto.getCompleted().equalsIgnoreCase("2"))) {
                if (TextUtils.isEmpty(checkListDto.getElementImageBase64()))
                    return i;
            }
        }
        return -1;
    }

    public void saveElementData() {
        asyncSaveGWRActivityDetails = new AsyncSaveGWRActivityDetails(getActivity(), categoryList, type, new AsyncSaveGWRActivityDetails.Callback() {
            @Override
            public void onResult(String result) {
                try {
                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                        return;
                    }

                    Log.e(TAG, "Result : " + result);
                    if (result.startsWith("OKAY")) {

                        showMessage("Activity saved successfully.");
                        //Handle Response
                        StringTokenizer st1 = new StringTokenizer(result, "|");
                        String key = st1.nextToken();
                        String gwrData = st1.nextToken();
                        try {
                            if (!TextUtils.isEmpty(gwrData)) {
                                reloadGWRDetails(gwrData);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        AlertDialogBoxInfo.showOkDialog(getActivity(), "Failed to Update Activity Details.");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                }
            }
        });
        asyncSaveGWRActivityDetails.execute("");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (asyncSaveGWRActivityDetails != null && !asyncSaveGWRActivityDetails.isCancelled()) {
            asyncSaveGWRActivityDetails.cancel(true);
        }

        if (asyncGetGWR != null && !asyncGetGWR.isCancelled()) {
            asyncGetGWR.cancel(true);
        }
    }

    public void showMessage(String msg) {
        if (TextUtils.isEmpty(msg))
            return;

        if (alert == null) {
            alertDialogBuilder = new AlertDialog.Builder(context);

            alertDialogBuilder
                    .setMessage(Html.fromHtml(msg))
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            alert = null;
                            nestedScrollview.scrollTo(0, 0);


                        }
                    });
            alert = alertDialogBuilder.create();
            alert.show();
        }
    }
}
