package in.vakrangee.franchisee.gwr.event_photos;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nononsenseapps.filepicker.Utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.vakrangee.franchisee.BuildConfig;
import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.commongui.CustomGIFImageDialog;
import in.vakrangee.franchisee.gwr.evidence.EvidenceRepository;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.commongui.FileAttachmentDialog;
import in.vakrangee.supercore.franchisee.commongui.FileUtils;
import in.vakrangee.supercore.franchisee.commongui.FilteredFilePickerActivity;
import in.vakrangee.supercore.franchisee.commongui.ImageGIFDto;
import in.vakrangee.supercore.franchisee.commongui.imagepreview.CustomImageZoomDialog;
import in.vakrangee.supercore.franchisee.model.GWREventPhotoDto;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.GPSTracker;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;
import in.vakrangee.supercore.franchisee.utils.ImageZipper;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;

@SuppressLint("ValidFragment")
public class GWREventPhotosFragment extends BaseFragment implements GWREventPhotoAdapter.ISiteCheckHandler, CustomGIFImageDialog.onCameraClickGIf {

    private static final String TAG = "GWREventPhotosFragment";
    private View view;
    private Context context;
    private Connection connection;
    private GWREventPhotoAdapter gwrEventPhotoAdapter;
    @BindView(R.id.recycler_view_event_photo)
    RecyclerView recycler_view_event_photo;
    private CustomImageZoomDialog customImagePreviewDialog;
    private PermissionHandler permissionHandler;
    private Bitmap bitmap = null;
    private String imgBase64;
    private Uri picUri;
    private static final int CAMERA_PIC_REQUEST = 1;
    private String mCurrentPhotoPath;
    private String latitude = "", longitude = "", currentTimestamp = "";
    private GPSTracker gpsTracker;
    private int selectedPos = -1;
    private GWREventPhotoDto eventPhotoDto;
    private List<GWREventPhotoDto> gwrEventPhotoDtos;
    private AsyncSaveGWREventPhotos asyncSaveGWREventPhotos;
    private boolean IsEditable = false;
    private String vkid;
    private InternetConnection internetConnection;
    private TextView txtNoDataMsg;
    private LinearLayout layoutProgress;
    private AsyncGetGWREventPhotos asyncGetGWR = null;
    private boolean IsAlreadyExecuted = false;
    private EvidenceRepository evidenceRepository;
    private CustomGIFImageDialog customGIFImageDialog;
    private FileAttachmentDialog fileAttachementDialog;
    private static final int BROWSE_FOLDER_REQUEST = 101;

    public GWREventPhotosFragment() {
    }

    public GWREventPhotosFragment(boolean IsEditable) {
        this.IsEditable = IsEditable;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag_gwr_event_photos, container, false);

        //Initialize data
        this.context = getContext();
        connection = new Connection(context);
        ButterKnife.bind(this, view);
        permissionHandler = new PermissionHandler(getActivity());
        gpsTracker = new GPSTracker(context);
        internetConnection = new InternetConnection(context);
        evidenceRepository = new EvidenceRepository(context);

        layoutProgress = (LinearLayout) view.findViewById(R.id.layoutProgress);
        txtNoDataMsg = view.findViewById(R.id.txtNoDataMsg);

        final Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");

        return view;
    }

    public void reloadEventPhotoDetails(String gwrData) {

        try {
            Gson gson = new GsonBuilder().create();
            gwrEventPhotoDtos = gson.fromJson(gwrData.toString(), new TypeToken<ArrayList<GWREventPhotoDto>>() {
            }.getType());
            //Recycler View Data set

            //categoryList = getReadinessCategoryList();
            gwrEventPhotoAdapter = new GWREventPhotoAdapter(context, gwrEventPhotoDtos, this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            recycler_view_event_photo.setLayoutManager(layoutManager);
            recycler_view_event_photo.setItemAnimator(new DefaultItemAnimator());
            recycler_view_event_photo.setAdapter(gwrEventPhotoAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
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
                picUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
                startActivityForResult(takePictureIntent, CAMERA_PIC_REQUEST);
            }
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK) {

                GWREventPhotoDto Dto = gwrEventPhotoDtos.get(selectedPos);
                String name = getPhotoName(Dto.getGuinnessEventPhotoTypeName());
                evidenceRepository.copyEvidencePhoto(mCurrentPhotoPath, name); //copy GWR floder in mobile

                Uri imageUri = Uri.parse(mCurrentPhotoPath);
                getLocationAndTimestamp();
                Bitmap bitmapNew = ImageUtils.getBitmap(context.getContentResolver(), imageUri, latitude, longitude, currentTimestamp);
                //BitMap with TimeStamp on it
                bitmapNew = ImageUtils.stampWithTimeInBitmap(bitmapNew);
                String imageBase64 = ImageUtils.updateExifData(imageUri, bitmapNew, latitude, longitude, currentTimestamp);

                ImageView imageView = new ImageView(context);
                imageView.setImageBitmap(bitmapNew);
                if (!CommonUtils.isLandscapePhoto(imageUri, imageView)) {
                    AlertDialogBoxInfo.alertDialogShow(context, getString(R.string.landscape_mode_allowed));
                } else {

                    GWREventPhotoDto gwrEventPhotoDto = gwrEventPhotoDtos.get(selectedPos);
                    gwrEventPhotoDto.setEventPhoto(imageBase64);
                    gwrEventPhotoDtos.set(selectedPos, gwrEventPhotoDto);
                    showImagePreviewDialog((Object) gwrEventPhotoDto);
                }

            } else if (requestCode == BROWSE_FOLDER_REQUEST && resultCode == Activity.RESULT_OK) {
                // Use the provided utility method to parse the result
                List<Uri> files = Utils.getSelectedFilesFromResult(data);
                for (Uri uri : files) {
                    File file = Utils.getFileForUri(uri);

                    //Check File size
                    int fileSize = CommonUtils.getFileSizeInMB(file);
                    if (fileSize > 5) {
                        showMessage("Please select file with Size less than or equal to 5 MB");
                        return;
                    }

                    mCurrentPhotoPath = "file:" + file.getAbsolutePath();
                    GWREventPhotoDto Dto = gwrEventPhotoDtos.get(selectedPos);
                    String name = getPhotoName(Dto.getGuinnessEventPhotoTypeName());
                    evidenceRepository.copyEvidencePhoto(mCurrentPhotoPath, name); //copy GWR floder in mobile

                    String ext = FileUtils.getFileExtension(context, uri);
                    String base64Data;

                    if (ext.equalsIgnoreCase("pdf")) {
                        base64Data = CommonUtils.encodeFileToBase64Binary(file);
                    } else {
                        file = new ImageZipper(context).setQuality(50).compressToFile(file);
                        Bitmap bitmapNew = new ImageZipper(context).compressToBitmap(file);
                        base64Data = CommonUtils.convertBitmapToString(bitmapNew);
                    }

                    //String getDateTime = ImageUtils.getDateTimeFromExif(uri,context);
                    //Log.e("getDateTimeFromExif ", "Testing :" + getDateTime);
                    GWREventPhotoDto gwrEventPhotoDto = gwrEventPhotoDtos.get(selectedPos);
                    gwrEventPhotoDto.setEventPhoto(base64Data);
                    gwrEventPhotoDtos.set(selectedPos, gwrEventPhotoDto);

                    showImagePreviewDialog((Object) gwrEventPhotoDto);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
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


    //endregion

    @Override
    public void onDestroy() {
        super.onDestroy();
        /*if (asyncSaveAttendanceDetails != null && !asyncSaveAttendanceDetails.isCancelled()) {
            asyncSaveAttendanceDetails.cancel(true);
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!IsAlreadyExecuted) {
            switchToGWREventPhotos();
            IsAlreadyExecuted = true;
        }
    }

    //region showImagePreviewDialog
    private void showImagePreviewDialog(Object object) {

        if (customImagePreviewDialog != null && customImagePreviewDialog.isShowing()) {
            customImagePreviewDialog.refresh(object);
            return;
        }

        if (object != null) {
            customImagePreviewDialog = new CustomImageZoomDialog(context, object, new CustomImageZoomDialog.IImagePreviewDialogClicks() {
                @Override
                public void capturePhotoClick() {
                    //startCamera(view);
                    // startCamera(view);
                    String SEL_FILE_TYPE = "images";
                    showAttachmentDialog(view, SEL_FILE_TYPE);
                }

                @Override
                public void OkClick(Object object) {
                    GWREventPhotoDto eventPhotoDto = ((GWREventPhotoDto) object);
                    Gson gson = new Gson();
                    String eventjson = gson.toJson(eventPhotoDto, GWREventPhotoDto.class);
                    saveEventDataToServer(eventjson, eventPhotoDto.getGuinnessEventPhotoTypeName());
                    //gwrEventPhotoDtos.set(selectedPos, eventPhotoDto);
                    // gwrEventPhotoAdapter.notifyDataSetChanged();

                    //TODO: @Add Save code
                }
            });

            customImagePreviewDialog.show();
            customImagePreviewDialog.setCancelable(false);
            customImagePreviewDialog.setDialogTitle("Event Photo");
            //customImagePreviewDialog.allowChangePhoto(true);
            customImagePreviewDialog.allowRemarks(false);

        } else {
            Toast.makeText(context, "No photo available to preview.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveEventDataToServer(String eventjson, final String photoname) {
        asyncSaveGWREventPhotos = new AsyncSaveGWREventPhotos(context, connection.getVkid(), eventjson, new AsyncSaveGWREventPhotos.Callback() {
            @Override
            public void onResult(String result) {
                try {
                    /*String name = getPhotoName(photoname);
                    evidenceRepository.copyEvidencePhoto(mCurrentPhotoPath, name); //copy GWR floder in mobile
*/
                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                        return;
                    }

                    if (result.startsWith("OKAY")) {
                        //Handle Response
                        System.out.println(result);
                        StringTokenizer st1 = new StringTokenizer(result, "|");
                        String key = st1.nextToken();
                        String franchiseeData = st1.nextToken();
                        try {
                            reloadEventPhotoDetails(franchiseeData);
                            AlertDialogBoxInfo.alertDialogShow(context, photoname + " Photo Submitted Successfully.");

                        } catch (Exception e) {
                            e.getMessage();
                        }
                    } else if (result.startsWith("ERROR")) {
                        AlertDialogBoxInfo.alertDialogShow(context, photoname + " Details saving failed.");
                    } else {
                        AlertDialogBoxInfo.alertDialogShow(context, photoname + " Details saving failed.");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                }

            }
        });
        asyncSaveGWREventPhotos.execute("");

    }


    @Override
    public void cameraClick(int position, GWREventPhotoDto gwrEventPhotoDto) {
        selectedPos = position;
        eventPhotoDto = gwrEventPhotoDto;
        if (TextUtils.isEmpty(eventPhotoDto.getEventPhotoId())) {
            //if image not capture open GIF dialog
            customGIFDialogPreview(eventPhotoDto);
            // Show Directly Camera.
            //startCamera(view);
        } else {
            showImagePreviewDialog(eventPhotoDto);
        }
    }

    //region  if image not capture open GIF dialog
    private void customGIFDialogPreview(GWREventPhotoDto eventPhotoDto) {
        try {
            String photoname = eventPhotoDto.getGuinnessEventPhotoTypeName().replace(" ", "_");
            ImageGIFDto imageGIFDto = null;
            if (photoname.equalsIgnoreCase("vakrangee_kendra_frontage")) {
                int drawbleId = R.drawable.vakrangee_kendra_frontage;
                imageGIFDto = new ImageGIFDto(eventPhotoDto.getGuinnessEventPhotoTypeName(), drawbleId);
            } else if (photoname.equalsIgnoreCase("Entrance_from_Outside")) {
                int drawbleId = R.drawable.entrace_from_outside;
                imageGIFDto = new ImageGIFDto(eventPhotoDto.getGuinnessEventPhotoTypeName(), drawbleId);
            } else if (photoname.equalsIgnoreCase("Entrance_from_inside")) {
                int drawbleId = R.drawable.entrace_from_inside;
                imageGIFDto = new ImageGIFDto(eventPhotoDto.getGuinnessEventPhotoTypeName(), drawbleId);
            } else if (photoname.equalsIgnoreCase("right_side_diagonally_inside_the_store")) {
                int drawbleId = R.drawable.right_side_diagonally_inside_the_store;
                imageGIFDto = new ImageGIFDto(eventPhotoDto.getGuinnessEventPhotoTypeName(), drawbleId);
            } else if (photoname.equalsIgnoreCase("left_side_diagonally_inside_the_store")) {
                int drawbleId = R.drawable.left_side_diagonally_inside_the_store;
                imageGIFDto = new ImageGIFDto(eventPhotoDto.getGuinnessEventPhotoTypeName(), drawbleId);
            }

            customGIFImageDialog = new CustomGIFImageDialog(context, (Object) imageGIFDto, true, this);
            customGIFImageDialog.show();
            customGIFImageDialog.setCancelable(false);
        } catch (Exception e) {
            e.getMessage();
        }
    }
    //endregion

    private String getPhotoName(String photoName) {
        String name = null;

        if (TextUtils.isEmpty(photoName))
            return null;

        name = photoName.replace(" ", "_");
        name = name.toLowerCase();
        return name;
    }

    //endregion
    @Override
    public void onGIFCameraClick() {
        // startCamera(view);
        String SEL_FILE_TYPE = "images/pdf";
        showAttachmentDialog(view, SEL_FILE_TYPE);

    }

    public void showAttachmentDialog(final View view, final String fileType) {
        fileAttachementDialog = new FileAttachmentDialog(context, new FileAttachmentDialog.IFileAttachmentClicks() {
            @Override
            public void cameraClick() {
                //If the app has not the permission then asking for the permission
                permissionHandler.requestPermission(view, Manifest.permission.CAMERA, getString(R.string.needs_permission_camera_msg), new IPermission() {
                    @Override
                    public void IsPermissionGranted(boolean IsGranted) {
                        if (IsGranted) {
                            startCamera(view);
                            /*File file = CommonUtils.getOutputMediaFile(CommonUtils.FILE_IMAGE_TYPE);
                            Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            //picUri = Uri.fromFile(file); // create
                            picUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
                            i.putExtra(MediaStore.EXTRA_OUTPUT, picUri); // set the image file
                            i.putExtra("ImageId", picUri); // set the image file
                            startActivityForResult(i, CAMERA_PIC_REQUEST);*/
                        }
                    }
                });
            }

            @Override
            public void folderClick() {
                //If the app has not the permission then asking for the permission
                permissionHandler.requestPermission(view, Manifest.permission.WRITE_EXTERNAL_STORAGE, getString(R.string.needs_permission_storage_msg), new IPermission() {
                    @Override
                    public void IsPermissionGranted(boolean IsGranted) {
                        if (IsGranted) {
                            // This always works
                            Intent i = new Intent(context, FilteredFilePickerActivity.class);
                            // Set these depending on your use case. These are the defaults.
                            i.putExtra(FilteredFilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
                            i.putExtra(FilteredFilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
                            i.putExtra(FilteredFilePickerActivity.EXTRA_MODE, FilteredFilePickerActivity.MODE_FILE);
                            FilteredFilePickerActivity.FILE_TYPE = fileType;

                            // Configure initial directory by specifying a String.
                            // You could specify a String like "/storage/emulated/0/", but that can
                            // dangerous. Always use Android's API calls to get paths to the SD-card or
                            // internal memory.
                            i.putExtra(FilteredFilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
                            startActivityForResult(i, BROWSE_FOLDER_REQUEST);
                        }
                    }
                });

            }
        });
        fileAttachementDialog.setCancelable(false);
        fileAttachementDialog.show();
    }

    public void switchToGWREventPhotos() {

        layoutProgress.setVisibility(View.VISIBLE);
        txtNoDataMsg.setVisibility(View.GONE);
        recycler_view_event_photo.setVisibility(View.GONE);


        if (!internetConnection.isNetworkAvailable(context)) {
            AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.internetCheck));
        } else {

            Connection connection = new Connection(context);
            vkid = connection.getVkid();
            permissionHandler.requestPermission(view, Manifest.permission.READ_PHONE_STATE, getString(R.string.needs_permission_phone_state_msg), new IPermission() {
                @Override
                public void IsPermissionGranted(boolean IsGranted) {
                    if (IsGranted) {
                        if (!internetConnection.isNetworkAvailable(context)) {
                            AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.internetCheck));
                        } else {
                            asyncGetGWR = new AsyncGetGWREventPhotos(context, vkid, new AsyncGetGWREventPhotos.Callback() {
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
            recycler_view_event_photo.setVisibility(View.GONE);
            return;
        }

        // Handle Error Response From Server.
        if (result.startsWith("ERROR|")) {

            StringTokenizer tokens = new StringTokenizer(result, "|");
            tokens.nextToken();     // Jump to next Token
            String errMsg = tokens.nextToken();

            if (!TextUtils.isEmpty(errMsg)) {
                AlertDialogBoxInfo.alertDialogShow(context, errMsg);
                txtNoDataMsg.setVisibility(View.VISIBLE);
                recycler_view_event_photo.setVisibility(View.GONE);
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
                recycler_view_event_photo.setVisibility(View.GONE);
                return;
            }

            //Process Response
            txtNoDataMsg.setVisibility(View.GONE);
            recycler_view_event_photo.setVisibility(View.VISIBLE);
            reloadEventPhotoDetails(data);
        }
    }


}
