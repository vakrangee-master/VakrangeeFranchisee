package in.vakrangee.franchisee.gwr.witness_statement;

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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.vakrangee.franchisee.BuildConfig;
import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.gwr.attendance.AttendanceDetailsAdapter;
import in.vakrangee.franchisee.gwr.attendance.AttendanceDetailsDto;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.commongui.FileAttachmentDialog;
import in.vakrangee.supercore.franchisee.commongui.FileUtils;
import in.vakrangee.supercore.franchisee.commongui.FilteredFilePickerActivity;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.GPSTracker;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;
import in.vakrangee.supercore.franchisee.utils.ImageZipper;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.utils.Logger;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;

@SuppressLint("ValidFragment")
public class GWRUploadWitnessStatementFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "GWRUploadWitnessStatementFragment";
    private View view;
    private Context context;
    private Logger logger;
    private Connection connection;
    private PermissionHandler permissionHandler;

    @BindView(R.id.layoutWitness)
    LinearLayout layoutWitness;
    @BindView(R.id.recyclerViewWitnessAttendance)
    RecyclerView recyclerViewWitnessAttendance;
    @BindView(R.id.txtWitnessHeader)
    TextView txtWitnessHeader;

    @BindView(R.id.btnSubmitWitnessStatement)
    Button btnSubmitWitnessStatement;
    @BindView(R.id.txtNoWitnessMsg)
    TextView txtNoWitnessMsg;

    private List<AttendanceDetailsDto> witnessAttendanceList;
    private AttendanceDetailsAdapter witnessAttendanceDetailsAdapter;
    private DeprecateHandler deprecateHandler;
    private static final int CAMERA_PIC_REQUEST = 1;
    private Uri picUri;
    private String latitude = "", longitude = "", currentTimestamp = "";
    private int selectedPos = -1;
    private String mCurrentPhotoPath;
    private GPSTracker gpsTracker;
    private DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
    private int type;
    private AsyncSaveUploadWitnessStatement asyncSaveUploadWitnessStatement = null;
    private FileAttachmentDialog fileAttachementDialog;
    private static final int BROWSE_FOLDER_REQUEST = 101;
    private boolean IsEditable = false;
    private String vkid;
    private InternetConnection internetConnection;
    private LinearLayout layoutProgress;
    private AsyncGetUploadWitnessStatement asyncGetGWR = null;
    private boolean IsAlreadyExecuted = false;

    public GWRUploadWitnessStatementFragment() {
    }

    public GWRUploadWitnessStatementFragment(boolean IsEditable) {
        this.IsEditable = IsEditable;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag_upload_witness_statement, container, false);

        //Initialize data
        this.context = getContext();
        logger = Logger.getInstance(context);
        connection = new Connection(context);
        permissionHandler = new PermissionHandler(getActivity());
        deprecateHandler = new DeprecateHandler(context);
        gpsTracker = new GPSTracker(context);
        ButterKnife.bind(this, view);
        final Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        layoutProgress = (LinearLayout) view.findViewById(R.id.layoutProgress);

        btnSubmitWitnessStatement.setTypeface(font);
        btnSubmitWitnessStatement.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " + getResources().getString(R.string.submit)));
        btnSubmitWitnessStatement.setOnClickListener(this);

        return view;
    }

    @OnClick({R.id.btnSubmitWitnessStatement})
    @Override
    public void onClick(View view) {
        int Id = view.getId();

        if (Id == R.id.btnSubmitWitnessStatement) {
            save();
        }
    }

    public void refreshWitnessStatement(String gwrData) {

        if (TextUtils.isEmpty(gwrData)) {
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(gwrData);
            JSONArray witnessJsonData = jsonObject.getJSONArray("witness_man_list");

            Gson gson = new GsonBuilder().create();
            witnessAttendanceList = gson.fromJson(witnessJsonData.toString(), new TypeToken<ArrayList<AttendanceDetailsDto>>() {
            }.getType());

            //Set Witness Adapter
            if (witnessAttendanceList != null && witnessAttendanceList.size() > 0) {
                txtNoWitnessMsg.setVisibility(View.GONE);
                recyclerViewWitnessAttendance.setVisibility(View.VISIBLE);

                witnessAttendanceDetailsAdapter = new AttendanceDetailsAdapter(context, AttendanceDetailsAdapter.TYPE_WITNESS, AttendanceDetailsAdapter.FROM_UPLOAD_STATEMENT, witnessAttendanceList, new AttendanceDetailsAdapter.IAttendancePicHandler() {
                    @Override
                    public void cameraClick(int position, AttendanceDetailsDto attendanceDetailsDto) {
                        type = AttendanceDetailsAdapter.TYPE_WITNESS;
                        selectedPos = position;
                        //startCamera(view);
                        String SEL_FILE_TYPE = "images/pdf";
                        showAttachmentDialog(view, SEL_FILE_TYPE);

                    }

                    @Override
                    public void navigateMap(int position, AttendanceDetailsDto attendanceDetailsDto) {

                    }
                });
                LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                recyclerViewWitnessAttendance.setLayoutManager(layoutManager);
                recyclerViewWitnessAttendance.setItemAnimator(new DefaultItemAnimator());
                recyclerViewWitnessAttendance.setAdapter(witnessAttendanceDetailsAdapter);

            } else {
                txtNoWitnessMsg.setVisibility(View.VISIBLE);
                recyclerViewWitnessAttendance.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK) {

                Uri imageUri = Uri.parse(mCurrentPhotoPath);
                picUri = imageUri;
                //ext set - nilesh
                String ext = FileUtils.getFileExtension(context, picUri);

                getLocationAndTimestamp();
                Bitmap bitmapNew = ImageUtils.getBitmap(getActivity().getContentResolver(), picUri, latitude, longitude, currentTimestamp);
                bitmapNew = ImageUtils.rotateImageIfRequired(getActivity().getContentResolver(), bitmapNew, picUri);

                //BitMap with TimeStamp on it
                bitmapNew = ImageUtils.stampWithTimeInBitmap(bitmapNew);
                String imageBase64 = ImageUtils.updateExifData(picUri, bitmapNew, latitude, longitude, currentTimestamp);

                ImageView imageView = new ImageView(context);
                imageView.setImageBitmap(bitmapNew);

                /*if (!CommonUtils.isLandscapePhoto(picUri, imageView)) {
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getString(R.string.landscape_mode_allowed));
                } else {*/
                AttendanceDetailsDto detailsDto = savePhoto(bitmapNew, imageBase64, ext);

                witnessAttendanceList.set(selectedPos, detailsDto);
                witnessAttendanceDetailsAdapter.notifyDataSetChanged();

            } else if (requestCode == BROWSE_FOLDER_REQUEST && resultCode == Activity.RESULT_OK) {
                // Use the provided utility method to parse the result
                List<Uri> files = Utils.getSelectedFilesFromResult(data);
                for (Uri uri : files) {
                    File file = Utils.getFileForUri(uri);

                    //Check File size
                    int fileSize = CommonUtils.getFileSizeInMB(file);
                    if (fileSize > 1) {
                        showMessage(getString(R.string.file_size_msg));
                        return;
                    }

                    String ext = FileUtils.getFileExtension(context, uri);
                    String base64Data;

                    if (ext.equalsIgnoreCase("pdf")) {
                        base64Data = CommonUtils.encodeFileToBase64Binary(file);
                    } else {
                        file = new ImageZipper(context).setQuality(50).compressToFile(file);
                        Bitmap bitmapNew = new ImageZipper(context).compressToBitmap(file);
                        base64Data = CommonUtils.convertBitmapToString(bitmapNew);
                    }

                    String fileName = URLDecoder.decode(file.getName(), "UTF-8");
                    AttendanceDetailsDto detailsDto = setImageAndName(fileName, base64Data, uri);
                    if (detailsDto != null) {
                        witnessAttendanceList.set(selectedPos, detailsDto);
                        witnessAttendanceDetailsAdapter.notifyDataSetChanged();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AttendanceDetailsDto setImageAndName(String fileName, String base64Data, Uri uri) {
        AttendanceDetailsDto detailsDto = null;
        try {
            Bitmap imageBitMap = null;
            boolean IsDrawable = false;
            String ext = FileUtils.getFileExtension(context, uri);
            if (ext.equalsIgnoreCase("pdf"))
                IsDrawable = true;
            else
                imageBitMap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);

            //Set Data
            detailsDto = savePhoto(imageBitMap, base64Data, ext);
            detailsDto.setExtension(ext);
            detailsDto.setFileName(fileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return detailsDto;
    }

    public void getLocationAndTimestamp() {
        //Get Current location and time stamp
        if (gpsTracker.canGetLocation()) {
            latitude = String.valueOf(gpsTracker.getLatitude());
            longitude = String.valueOf(gpsTracker.getLongitude());
            currentTimestamp = gpsTracker.getFormattedDateTime();
        }
    }

    public AttendanceDetailsDto savePhoto(Bitmap bitmap, String imageBase64, String extension) {
        AttendanceDetailsDto attendanceDetailsDto;

        attendanceDetailsDto = witnessAttendanceList.get(selectedPos);
        attendanceDetailsDto.setLatitude(latitude);
        attendanceDetailsDto.setLongitude(longitude);
        attendanceDetailsDto.setElementUri(picUri);
        attendanceDetailsDto.setElementImgBitmap(bitmap);
        attendanceDetailsDto.setExtension(extension);
        attendanceDetailsDto.setWitnessStatementImage(imageBase64);
        attendanceDetailsDto.setCapturedDateTime(dateTimeFormat.format(new Date()));
        attendanceDetailsDto.setWitnessStatementUploadDateTime(dateTimeFormat.format(new Date()));
        return attendanceDetailsDto;

    }

    public void save() {
        if (IsAttendanceAdded()) {
            saveData();
        } else {
            showMessage("Please add atleast one selfie.");
        }
    }

    private boolean IsAttendanceAdded() {

        List<AttendanceDetailsDto> attendanceDetailsList;
        attendanceDetailsList = witnessAttendanceList;

        for (AttendanceDetailsDto detailsDto : attendanceDetailsList) {

            if (!TextUtils.isEmpty(detailsDto.getWitnessStatementImage())) {
                return true;
            }
        }
        return false;
    }

    public void saveData() {

        List<AttendanceDetailsDto> attendanceDetailsList;
        type = AttendanceDetailsAdapter.TYPE_WITNESS;
        attendanceDetailsList = witnessAttendanceList;

        asyncSaveUploadWitnessStatement = new AsyncSaveUploadWitnessStatement(getActivity(), type, attendanceDetailsList, new AsyncSaveUploadWitnessStatement.Callback() {
            @Override
            public void onResult(String result) {
                try {
                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                        return;
                    }

                    Log.e(TAG, "Result : " + result);
                    if (result.startsWith("OKAY")) {

                        showMessage("Witness Statement uploaded successfully.");
                        //Handle Response
                        StringTokenizer st1 = new StringTokenizer(result, "|");
                        String key = st1.nextToken();
                        String gwrData = st1.nextToken();
                        try {
                            if (!TextUtils.isEmpty(gwrData)) {
                                refreshWitnessStatement(gwrData);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        AlertDialogBoxInfo.showOkDialog(getActivity(), "Failed to Update Witness Statement Details.");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                }
            }
        });
        asyncSaveUploadWitnessStatement.execute("");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (asyncSaveUploadWitnessStatement != null && !asyncSaveUploadWitnessStatement.isCancelled()) {
            asyncSaveUploadWitnessStatement.cancel(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!IsAlreadyExecuted) {
            switchToGWRUploadWitnessStatement();
            IsAlreadyExecuted = true;
        }
    }

    public void switchToGWRUploadWitnessStatement() {
        layoutProgress.setVisibility(View.VISIBLE);
        txtNoWitnessMsg.setVisibility(View.GONE);
        recyclerViewWitnessAttendance.setVisibility(View.GONE);

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
                            asyncGetGWR = new AsyncGetUploadWitnessStatement(context, vkid, new AsyncGetUploadWitnessStatement.Callback() {
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
            txtNoWitnessMsg.setVisibility(View.VISIBLE);
            recyclerViewWitnessAttendance.setVisibility(View.GONE);
            return;
        }

        // Handle Error Response From Server.
        if (result.startsWith("ERROR|")) {

            StringTokenizer tokens = new StringTokenizer(result, "|");
            tokens.nextToken();     // Jump to next Token
            String errMsg = tokens.nextToken();

            if (!TextUtils.isEmpty(errMsg)) {
                AlertDialogBoxInfo.alertDialogShow(context, errMsg);
                txtNoWitnessMsg.setVisibility(View.VISIBLE);
                recyclerViewWitnessAttendance.setVisibility(View.GONE);
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
                txtNoWitnessMsg.setVisibility(View.VISIBLE);
                recyclerViewWitnessAttendance.setVisibility(View.GONE);
                return;
            }

            //Process Response
            txtNoWitnessMsg.setVisibility(View.GONE);
            recyclerViewWitnessAttendance.setVisibility(View.VISIBLE);
            refreshWitnessStatement(data);
        }
    }
}
