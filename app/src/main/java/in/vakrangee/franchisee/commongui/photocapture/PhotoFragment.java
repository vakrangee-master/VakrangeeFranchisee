package in.vakrangee.franchisee.commongui.photocapture;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.commongui.ImageSliderDialog;
import in.vakrangee.supercore.franchisee.commongui.imagegallery.RecyclerViewClickListener;
import in.vakrangee.supercore.franchisee.commongui.imagepreview.CustomImagePreviewDialog;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.utils.GPSTracker;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;

public class PhotoFragment extends Fragment {

    private final String TAG = "PhotoFragment";
    private LinearLayout layoutHeader, parentLayout;
    private Context context;
    private PermissionHandler permissionHandler;
    private GPSTracker gpsTracker;
    private String latitude = "", longitude = "", currentTimestamp = "";
    private Uri picUri;
    private static final int CAMERA_PIC_REQUEST = 111;
    private static final int SLIDER_CAMERA_PIC_REQUEST = 112;
    private List<PhotoDto> photoList = new ArrayList<PhotoDto>();
    private TextView textviewMsg, titletextview;
    private RecyclerView recyclerViewImageGallery;
    private PhotoAdapter photoAdapter;
    private RecyclerViewClickListener recyclerViewClickListener;
    private ImageSliderDialog imageSliderDialog;
    private View rootView;
    private int sliderImagePos = -1;
    public boolean IsEditable = true;
    private boolean IsOnlyLandscapeAllowed = false;
    private CustomImagePreviewDialog customImagePreviewDialog;
    private DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
    private int clickedPosition = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.image_gallery, container, false);

        //Widgets
        layoutHeader = rootView.findViewById(R.id.layoutHeader);
        layoutHeader.setVisibility(View.GONE);

        //Data
        this.context = getContext();
        permissionHandler = new PermissionHandler(getActivity());
        gpsTracker = new GPSTracker(context);

        //Widgets
        parentLayout = rootView.findViewById(R.id.parentLayout);
        textviewMsg = rootView.findViewById(R.id.textviewMsg);
        recyclerViewImageGallery = rootView.findViewById(R.id.recyle_view_image_gallery);
        titletextview = rootView.findViewById(R.id.titletextview);

        setAdapter("");

        //Recycler View Item Listener
        recyclerViewClickListener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (position != RecyclerView.NO_POSITION) {
                    clickedPosition = position;
                    PhotoDto photoDto = photoList.get(clickedPosition);
                    if (photoDto.getBitmap() == null)
                        startCamera();
                    else
                        showImageSliderDialog(position);
                }
            }
        };
        return rootView;
    }

    public void setAdapter(String TYPE) {

        // add a divider after each item for more clarity
        recyclerViewImageGallery.setItemAnimator(new DefaultItemAnimator());
        //Set Adapter
        photoAdapter = new PhotoAdapter(context, photoList, recyclerViewClickListener);
        recyclerViewImageGallery.setAdapter(photoAdapter);
        if (TYPE.equalsIgnoreCase(Constants.RECYCLER_TYPE_GRID)) {
            int numberOfColumns = 2;
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, numberOfColumns);
            gridLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            recyclerViewImageGallery.setLayoutManager(gridLayoutManager);

        } else if (TYPE.equalsIgnoreCase(Constants.RECYCLER_TYPE_VERTICAL)) {
            LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(new ContextThemeWrapper(context, R.style.scrollbar_vertical_style), LinearLayoutManager.VERTICAL, false);
            recyclerViewImageGallery.setLayoutManager(verticalLayoutManager);
            recyclerViewImageGallery.setVerticalFadingEdgeEnabled(false);
            recyclerViewImageGallery.setVerticalScrollBarEnabled(true);

        } else {
            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(new ContextThemeWrapper(context, R.style.scrollbar_horizontal_style), LinearLayoutManager.HORIZONTAL, false);
            recyclerViewImageGallery.setLayoutManager(horizontalLayoutManager);

        }
        if (photoList.size() == 0) {
            textviewMsg.setVisibility(View.VISIBLE);
            recyclerViewImageGallery.setVisibility(View.GONE);

        } else {
            textviewMsg.setVisibility(View.GONE);
            recyclerViewImageGallery.setVisibility(View.VISIBLE);
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

    private void showImageSliderDialog(int pos) {

        if (imageSliderDialog != null && imageSliderDialog.isShowing()) {
            return;
        }

        if (photoList.size() > 0) {
            //Image Slider is used to show Sliding Images with selected position in list.
            imageSliderDialog = new ImageSliderDialog(getActivity(), new ArrayList<Object>(photoList), pos, new ImageSliderDialog.ISliderClickHandler() {
                @Override
                public void captureClick(int position) {
                    sliderImagePos = position;
                    //If the app has not the permission then asking for the permission
                    permissionHandler.requestPermission(rootView, Manifest.permission.CAMERA, getString(R.string.needs_permission_camera_msg), new IPermission() {
                        @Override
                        public void IsPermissionGranted(boolean IsGranted) {
                            if (IsGranted) {
                                File file = CommonUtils.getOutputMediaFile(CommonUtils.FILE_IMAGE_TYPE);
                                Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                picUri = Uri.fromFile(file); // create
                                i.putExtra(MediaStore.EXTRA_OUTPUT, picUri); // set the image file
                                i.putExtra("ImageId", picUri); // set the image file
                                startActivityForResult(i, SLIDER_CAMERA_PIC_REQUEST);
                            }
                        }
                    });
                }

                @Override
                public void saveClick(List<Object> objectList) {
                    photoList = (List<PhotoDto>) (Object) objectList;
                    notifyAdapter();
                }
            });
            imageSliderDialog.show();
            imageSliderDialog.setCancelable(false);
            imageSliderDialog.allowChangePhoto(IsEditable);
            imageSliderDialog.allowRemarks(false);
        } else {
            Toast.makeText(getActivity(), "No photos are available for preview.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        permissionHandler.dismissSnackbar();
    }

    public void notifyAdapter() {
        //Update Image List of the parent
       /* if (iImagesHandler != null)
            iImagesHandler.updateImagesList(imageList);*/

        photoAdapter.notifyDataSetChanged();

        if (photoList.size() == 0) {
            textviewMsg.setVisibility(View.VISIBLE);
            recyclerViewImageGallery.setVisibility(View.GONE);
            parentLayout.invalidate();
        } else {
            textviewMsg.setVisibility(View.GONE);
            recyclerViewImageGallery.setVisibility(View.VISIBLE);
        }
    }

    public void refresh(Bundle args) {
        if (args != null) {

            //Get Data from Bundle
            List<PhotoDto> photoCaptureList = (ArrayList<PhotoDto>) args.getSerializable(Constants.INTENT_KEY_PHOTO_CAPTURE_LIST);
            String type = args.getString(Constants.INTENT_KEY_TYPE, "");
            IsOnlyLandscapeAllowed = args.getBoolean(Constants.INTENT_KEY_PORTRAIT_ALLOWED, false);
            photoList = photoCaptureList;
            setAdapter(type);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK) {
            try {
                getLocationAndTimestamp();
                Bitmap bitmapNew = ImageUtils.getBitmap(getActivity().getContentResolver(), picUri, latitude, longitude, currentTimestamp);
                //BitMap with TimeStamp on it
                bitmapNew = ImageUtils.stampWithTimeInBitmap(bitmapNew);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmapNew.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                String imageBase64 = EncryptionUtil.encodeBase64(byteArrayOutputStream.toByteArray());

                int status = validateCapturedPhoto(bitmapNew);
                if (status == -1) {
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getString(R.string.landscape_mode_allowed));
                } else {
                    PhotoDto photoDto = savePhoto(clickedPosition, bitmapNew, imageBase64);
                    showImagePreviewDialog((Object) photoDto);
                }

            } catch (Exception e) {
                e.printStackTrace();
                AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
            }
        } else if (requestCode == SLIDER_CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK) {
            try {
                getLocationAndTimestamp();
                Bitmap bitmapNew = ImageUtils.getBitmap(getActivity().getContentResolver(), picUri, latitude, longitude, currentTimestamp);
                //BitMap with TimeStamp on it
                bitmapNew = ImageUtils.stampWithTimeInBitmap(bitmapNew);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmapNew.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                String imageBase64 = EncryptionUtil.encodeBase64(byteArrayOutputStream.toByteArray());

                if (IsOnlyLandscapeAllowed)
                    validateAndSaveCapturedPhoto(bitmapNew, imageBase64, true);
                else
                    updatePhoto(bitmapNew, imageBase64, sliderImagePos);

            } catch (Exception e) {
                e.printStackTrace();
                AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
            }
        }
    }

    public int validateCapturedPhoto(Bitmap bitmapNew) throws Exception {

        if (!IsOnlyLandscapeAllowed)
            return 0;

        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(bitmapNew);

        if (!CommonUtils.isLandscapePhoto(picUri, imageView))
            return -1;


        return 0;
    }

    // Check Image Capture into Landscape mode or not.
    public void validateAndSaveCapturedPhoto(Bitmap bitmapNew, String imageBase64, boolean IsFromChanged) throws Exception {

        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(bitmapNew);

        if (!CommonUtils.isLandscapePhoto(picUri, imageView)) {
            AlertDialogBoxInfo.alertDialogShow(getActivity(), getString(R.string.landscape_mode_allowed));

        } else {
            if (IsFromChanged)
                updatePhoto(bitmapNew, imageBase64, sliderImagePos);
            else
                savePhoto(clickedPosition, bitmapNew, imageBase64);
        }
    }

    public void updatePhoto(Bitmap bitmap, String imageBase64, int position) {
        File file = new File(picUri.toString());

        PhotoDto imageDto = photoList.get(position);
        imageDto.setUri(picUri);
        imageDto.setBitmap(bitmap);
        imageDto.setImageBase64(imageBase64);
        imageDto.setCapturedDateTime(dateTimeFormat.format(new Date()));
        photoList.set(position, imageDto);
        notifyAdapter();
        imageSliderDialog.notifyViewPagerAdapter(new ArrayList<Object>(photoList));


    }

    public PhotoDto savePhoto(int position, Bitmap bitmap, String imageBase64) {
        File file = new File(picUri.toString());

        PhotoDto imageDto = photoList.get(position);
        imageDto.setUri(picUri);
        imageDto.setBitmap(bitmap);
        imageDto.setImageBase64(imageBase64);
        imageDto.setCapturedDateTime(dateTimeFormat.format(new Date()));
        return imageDto;
       /* imageList.add(imageDto);
        notifyAdapter();
        imgMaxCount++;*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Image Slider Dialog.
        if (imageSliderDialog != null) {
            imageSliderDialog.dismiss();
            imageSliderDialog = null;
        }

        //Image Preview DIalog
        if (customImagePreviewDialog != null) {
            customImagePreviewDialog.dismiss();
            customImagePreviewDialog = null;

        }
    }

    public void disableViews() {
        IsEditable = false;
        notifyAdapter();

    }

    private void showImagePreviewDialog(Object object) {

        if (customImagePreviewDialog != null && customImagePreviewDialog.isShowing()) {
            customImagePreviewDialog.refresh(object);
            return;
        }

        if (object != null) {
            customImagePreviewDialog = new CustomImagePreviewDialog(context, object, new CustomImagePreviewDialog.IImagePreviewDialogClicks() {
                @Override
                public void capturePhotoClick() {
                    startCamera();
                }

                @Override
                public void OkClick(Object object) {
                    photoList.set(clickedPosition, (PhotoDto) object);
                    notifyAdapter();
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

    public void startCamera() {
        //If the app has not the permission then asking for the permission
        permissionHandler.requestPermission(rootView, Manifest.permission.CAMERA, getString(R.string.needs_permission_camera_msg), new IPermission() {
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

}
