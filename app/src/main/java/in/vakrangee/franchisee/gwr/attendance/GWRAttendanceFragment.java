package in.vakrangee.franchisee.gwr.attendance;

import android.Manifest;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.vakrangee.franchisee.BuildConfig;
import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.gwr.GWRRepository;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.GPSTracker;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;
import in.vakrangee.supercore.franchisee.utils.Logger;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;

public class GWRAttendanceFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "GWRAttendanceFragment";
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

    @BindView(R.id.layoutCameraMan)
    LinearLayout layoutCameraMan;
    @BindView(R.id.recyclerViewCameraManAttendance)
    RecyclerView recyclerViewCameraManAttendance;
    @BindView(R.id.txtCameraHeader)
    TextView txtCameraHeader;
    @BindView(R.id.btnSubmitAttendance)
    Button btnSubmitAttendance;
    @BindView(R.id.txtNoCameraMsg)
    TextView txtNoCameraMsg;
    @BindView(R.id.txtNoWitnessMsg)
    TextView txtNoWitnessMsg;

    private List<AttendanceDetailsDto> witnessAttendanceList, cameraManAttendanceList;
    private AttendanceDetailsAdapter witnessAttendanceDetailsAdapter, cameraManAttendanceAdapter;
    private DeprecateHandler deprecateHandler;
    private static final int CAMERA_PIC_REQUEST = 1;
    private Uri picUri;
    private String latitude = "", longitude = "", currentTimestamp = "";
    private int selectedPos = -1;
    private String mCurrentPhotoPath;
    private GPSTracker gpsTracker;
    private DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
    private int type;
    private AsyncSaveAttendanceDetails asyncSaveAttendanceDetails = null;
    private GWRRepository gwrRepository;

    public GWRAttendanceFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag_gwr_attendance, container, false);

        //Initialize data
        this.context = getContext();
        logger = Logger.getInstance(context);
        connection = new Connection(context);
        permissionHandler = new PermissionHandler(getActivity());
        deprecateHandler = new DeprecateHandler(context);
        gpsTracker = new GPSTracker(context);
        gwrRepository = new GWRRepository(context);
        ButterKnife.bind(this, view);
        final Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");

        btnSubmitAttendance.setTypeface(font);
        btnSubmitAttendance.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " + getResources().getString(R.string.submit)));
        btnSubmitAttendance.setOnClickListener(this);

        return view;
    }

    @OnClick({R.id.txtWitnessHeader, R.id.txtCameraHeader, R.id.btnSubmitAttendance})
    @Override
    public void onClick(View view) {
        int Id = view.getId();

        if (Id == R.id.txtWitnessHeader) {
            layoutWitness.setVisibility(View.VISIBLE);
            layoutCameraMan.setVisibility(View.GONE);
            txtWitnessHeader.setTextColor(deprecateHandler.getColor(R.color.gray));
            txtWitnessHeader.setBackgroundColor(deprecateHandler.getColor(R.color.grey));
            txtCameraHeader.setTextColor(deprecateHandler.getColor(R.color.white));
            txtCameraHeader.setBackgroundColor(deprecateHandler.getColor(R.color.gray));


        } else if (Id == R.id.txtCameraHeader) {
            layoutWitness.setVisibility(View.GONE);
            layoutCameraMan.setVisibility(View.VISIBLE);
            txtCameraHeader.setTextColor(deprecateHandler.getColor(R.color.gray));
            txtCameraHeader.setBackgroundColor(deprecateHandler.getColor(R.color.grey));
            txtWitnessHeader.setTextColor(deprecateHandler.getColor(R.color.white));
            txtWitnessHeader.setBackgroundColor(deprecateHandler.getColor(R.color.gray));

        } else if (Id == R.id.btnSubmitAttendance) {
            save();
        }
    }

    public void refreshWitnessAttendance(String gwrData) {

        if (TextUtils.isEmpty(gwrData)) {
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(gwrData);
            JSONArray cameraJsonData = jsonObject.getJSONArray("camera_man_list");
            JSONArray witnessJsonData = jsonObject.getJSONArray("witness_man_list");

            Gson gson = new GsonBuilder().create();
            cameraManAttendanceList = gson.fromJson(cameraJsonData.toString(), new TypeToken<ArrayList<AttendanceDetailsDto>>() {
            }.getType());

            witnessAttendanceList = gson.fromJson(witnessJsonData.toString(), new TypeToken<ArrayList<AttendanceDetailsDto>>() {
            }.getType());

            //Set Witness Adapter
            if (witnessAttendanceList != null && witnessAttendanceList.size() > 0) {
                txtNoWitnessMsg.setVisibility(View.GONE);
                recyclerViewWitnessAttendance.setVisibility(View.VISIBLE);

                witnessAttendanceDetailsAdapter = new AttendanceDetailsAdapter(context, AttendanceDetailsAdapter.TYPE_WITNESS, AttendanceDetailsAdapter.FROM_ATTENDANCE, witnessAttendanceList, new AttendanceDetailsAdapter.IAttendancePicHandler() {
                    @Override
                    public void cameraClick(int position, AttendanceDetailsDto attendanceDetailsDto) {
                        type = AttendanceDetailsAdapter.TYPE_WITNESS;
                        selectedPos = position;
                        startCamera(view);

                    }

                    @Override
                    public void navigateMap(int position, AttendanceDetailsDto attendanceDetailsDto) {
                        mapNavigation(attendanceDetailsDto.getLatitude(), attendanceDetailsDto.getLongitude());
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

            //Set Camera Adapter
            if (cameraManAttendanceList != null && cameraManAttendanceList.size() > 0) {
                txtNoCameraMsg.setVisibility(View.GONE);
                recyclerViewCameraManAttendance.setVisibility(View.VISIBLE);

                cameraManAttendanceAdapter = new AttendanceDetailsAdapter(context, AttendanceDetailsAdapter.TYPE_CAMERA_MAN, AttendanceDetailsAdapter.FROM_ATTENDANCE, cameraManAttendanceList, new AttendanceDetailsAdapter.IAttendancePicHandler() {
                    @Override
                    public void cameraClick(int position, AttendanceDetailsDto attendanceDetailsDto) {
                        type = AttendanceDetailsAdapter.TYPE_CAMERA_MAN;
                        selectedPos = position;
                        startCamera(view);
                    }

                    @Override
                    public void navigateMap(int position, AttendanceDetailsDto attendanceDetailsDto) {
                        mapNavigation(attendanceDetailsDto.getLatitude(), attendanceDetailsDto.getLongitude());
                    }
                });
                LinearLayoutManager layoutManager1 = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                recyclerViewCameraManAttendance.setLayoutManager(layoutManager1);
                recyclerViewCameraManAttendance.setItemAnimator(new DefaultItemAnimator());
                recyclerViewCameraManAttendance.setAdapter(cameraManAttendanceAdapter);

            } else {
                txtNoCameraMsg.setVisibility(View.VISIBLE);
                recyclerViewCameraManAttendance.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK) {
            try {
                Uri imageUri = Uri.parse(mCurrentPhotoPath);
                picUri = imageUri;

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
                AttendanceDetailsDto detailsDto = savePhoto(bitmapNew, imageBase64);

                if (type == AttendanceDetailsAdapter.TYPE_CAMERA_MAN) {
                    cameraManAttendanceList.set(selectedPos, detailsDto);
                    cameraManAttendanceAdapter.notifyDataSetChanged();

                } else {
                    witnessAttendanceList.set(selectedPos, detailsDto);
                    witnessAttendanceDetailsAdapter.notifyDataSetChanged();
                }
                //}
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

    public AttendanceDetailsDto savePhoto(Bitmap bitmap, String imageBase64) {
        AttendanceDetailsDto attendanceDetailsDto;

        if (type == AttendanceDetailsAdapter.TYPE_CAMERA_MAN) {
            attendanceDetailsDto = cameraManAttendanceList.get(selectedPos);
        } else {
            attendanceDetailsDto = witnessAttendanceList.get(selectedPos);
        }

        attendanceDetailsDto.setLatitude(latitude);
        attendanceDetailsDto.setLongitude(longitude);
        attendanceDetailsDto.setElementUri(picUri);
        attendanceDetailsDto.setElementImgBitmap(bitmap);
        attendanceDetailsDto.setImgAttendBase64(imageBase64);
        attendanceDetailsDto.setCapturedDateTime(dateTimeFormat.format(new Date()));
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

        int type;
        List<AttendanceDetailsDto> attendanceDetailsList;
        if (layoutWitness.getVisibility() == View.VISIBLE) {
            type = AttendanceDetailsAdapter.TYPE_WITNESS;
            attendanceDetailsList = witnessAttendanceList;

        } else {
            type = AttendanceDetailsAdapter.TYPE_CAMERA_MAN;
            attendanceDetailsList = cameraManAttendanceList;
        }

        for (AttendanceDetailsDto detailsDto : attendanceDetailsList) {

            if (!TextUtils.isEmpty(detailsDto.getImgAttendBase64())) {
                return true;
            }
        }
        return false;
    }

    public void saveData() {

        int type;
        List<AttendanceDetailsDto> attendanceDetailsList;
        if (layoutWitness.getVisibility() == View.VISIBLE) {
            type = AttendanceDetailsAdapter.TYPE_WITNESS;
            attendanceDetailsList = witnessAttendanceList;

        } else {
            type = AttendanceDetailsAdapter.TYPE_CAMERA_MAN;
            attendanceDetailsList = cameraManAttendanceList;
        }

        asyncSaveAttendanceDetails = new AsyncSaveAttendanceDetails(getActivity(), type, attendanceDetailsList, new AsyncSaveAttendanceDetails.Callback() {
            @Override
            public void onResult(String result) {
                try {
                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                        return;
                    }

                    Log.e(TAG, "Result : " + result);
                    if (result.startsWith("OKAY")) {

                        showMessage("GWR Attendance details saved successfully.");
                        //Handle Response
                        StringTokenizer st1 = new StringTokenizer(result, "|");
                        String key = st1.nextToken();
                        String gwrData = st1.nextToken();
                        try {
                            if (!TextUtils.isEmpty(gwrData)) {
                                refreshWitnessAttendance(gwrData);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        AlertDialogBoxInfo.showOkDialog(getActivity(), "Failed to Update GWR Attendance Details.");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                }
            }
        });
        asyncSaveAttendanceDetails.execute("");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (asyncSaveAttendanceDetails != null && !asyncSaveAttendanceDetails.isCancelled()) {
            asyncSaveAttendanceDetails.cancel(true);
        }
    }

    public void mapNavigation(String latitude, String longitude) {
        try {

            if (TextUtils.isEmpty(latitude) || TextUtils.isEmpty(longitude)) {
                showMessage("Something went wrong.");
                return;
            }

            //Navigate
            String query;
            float destinationLatitude = Float.valueOf(latitude);//19.1809f;
            float destinationLongitude = Float.valueOf(longitude);//72.8575f;
            query = String.format(Locale.ENGLISH, "%f,%f (%s)", destinationLatitude, destinationLongitude, "Vakrangee Kendra");

            if (!TextUtils.isEmpty(query)) {
                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%s", query);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                context.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
