package in.vakrangee.franchisee.kendra_final_photo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.commongui.imagepreview.CustomImageZoomDialog;
import in.vakrangee.supercore.franchisee.model.KendraFinalPhotoDto;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.GPSTracker;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;

/**
 * A simple {@link Fragment} subclass.
 */
public class KendraFinalPhotoFragment extends BaseFragment implements KendraFinalPhotoAdapter.ISiteCheckHandler {

    private View view;
    private Context context;
    private Connection connection;
    private KendraFinalPhotoAdapter finalPhotoAdapter;
    @BindView(R.id.recycler_kendra_final_photo)
    RecyclerView recycler_kendra_final_photo;
    private CustomImageZoomDialog customImagePreviewDialog;
    private PermissionHandler permissionHandler;
    private Uri picUri;
    private static final int CAMERA_PIC_REQUEST = 1;
    private String mCurrentPhotoPath;
    private String latitude = "", longitude = "", currentTimestamp = "";
    private GPSTracker gpsTracker;
    private int selectedPos = -1;
    private KendraFinalPhotoDto finalPhotoDto;
    private List<KendraFinalPhotoDto> photoDtoList;
    private AsyncSaveKendraFinalPhotos asyncSaveKendraFinalPhotos;

    public KendraFinalPhotoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_kendra_final_photo, container, false);

        //Initialize data
        this.context = getContext();
        connection = new Connection(context);
        ButterKnife.bind(this, view);
        permissionHandler = new PermissionHandler(getActivity());
        gpsTracker = new GPSTracker(context);

        return view;
    }

    public void reloadEventPhotoDetails(String kendraData) {

        try {
            Gson gson = new GsonBuilder().create();
            photoDtoList = gson.fromJson(kendraData.toString(), new TypeToken<ArrayList<KendraFinalPhotoDto>>() {
            }.getType());
            //Recycler View Data set

            finalPhotoAdapter = new KendraFinalPhotoAdapter(context, photoDtoList, this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            recycler_kendra_final_photo.setLayoutManager(layoutManager);
            recycler_kendra_final_photo.setItemAnimator(new DefaultItemAnimator());
            recycler_kendra_final_photo.setAdapter(finalPhotoAdapter);

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
                photoFile = createImageFile();
            } catch (Exception ex) {
                ex.printStackTrace();
                // Error occurred while creating the File
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                picUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", photoFile);
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
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK) {
            try {
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

                    KendraFinalPhotoDto kendraFinalPhotoDto = photoDtoList.get(selectedPos);
                    kendraFinalPhotoDto.setPhoto(imageBase64);
                    photoDtoList.set(selectedPos, kendraFinalPhotoDto);
                    showImagePreviewDialog((Object) kendraFinalPhotoDto);
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


    //endregion

    @Override
    public void onDestroy() {
        super.onDestroy();
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
                    startCamera(view);
                }

                @Override
                public void OkClick(Object object) {
                    //save data to server
                    KendraFinalPhotoDto eventPhotoDto = ((KendraFinalPhotoDto) object);
                    Gson gson = new Gson();
                    String eventjson = gson.toJson(eventPhotoDto, KendraFinalPhotoDto.class);
                    saveEventDataToServer(eventjson, eventPhotoDto.getNextgen_photo_type_name());

                }
            });

            customImagePreviewDialog.show();
            customImagePreviewDialog.setCancelable(false);
            customImagePreviewDialog.setDialogTitle("Kendra Final Photo");
            customImagePreviewDialog.allowChangePhoto(true);
            customImagePreviewDialog.allowRemarks(false);

        } else {
            Toast.makeText(context, "No photo available to preview.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveEventDataToServer(String eventjson, final String photoname) {
        asyncSaveKendraFinalPhotos = new AsyncSaveKendraFinalPhotos(context, connection.getVkid(), eventjson, new AsyncSaveKendraFinalPhotos.Callback() {
            @Override
            public void onResult(String result) {
                try {

                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                        return;
                    }

                    if (result.startsWith("OKAY")) {
                        //Handle Response
                        StringTokenizer st1 = new StringTokenizer(result, "|");
                        st1.nextToken();
                        String franchiseeData = st1.nextToken();

                        reloadEventPhotoDetails(franchiseeData);
                        AlertDialogBoxInfo.alertDialogShow(context, photoname + " Photo Submitted Successfully.");

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
        asyncSaveKendraFinalPhotos.execute("");

    }

    @Override
    public void cameraClick(int position, KendraFinalPhotoDto photoDto) {
        selectedPos = position;
        finalPhotoDto = photoDto;
        if (TextUtils.isEmpty(finalPhotoDto.getPhoto_id())) {
            startCamera(view);
        } else {
            showImagePreviewDialog(finalPhotoDto);
        }
    }
    //endregion

}
