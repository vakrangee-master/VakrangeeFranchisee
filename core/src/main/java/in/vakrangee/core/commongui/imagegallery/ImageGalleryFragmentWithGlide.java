package in.vakrangee.core.commongui.imagegallery;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.vakrangee.core.R;
import in.vakrangee.core.commongui.ImageSliderDialog;
import in.vakrangee.core.commongui.imagepreview.CustomImagePreviewDialog;
import in.vakrangee.core.ifc.IImagesHandler;
import in.vakrangee.core.utils.AlertDialogBoxInfo;
import in.vakrangee.core.utils.CommonUtils;
import in.vakrangee.core.utils.Constants;
import in.vakrangee.core.utils.GPSTracker;
import in.vakrangee.core.utils.IPermission;
import in.vakrangee.core.utils.ImageUtils;
import in.vakrangee.core.utils.PermissionHandler;

public class ImageGalleryFragmentWithGlide extends Fragment {

    private static final String TAG = "ImageGalleryFragmentWithGlide";

    private TextView textviewMsg, titletextview;
    private RecyclerView recyclerViewImageGallery;
    private LinearLayout parentLinearlytAddButton;
    private Context context;
    private ImageThumbnailAdapter imageThumbnailAdapter;
    private List<ImageDto> imageList = new ArrayList<ImageDto>();
    private PermissionHandler permissionHandler;
    private Uri picUri;
    private static final int CAMERA_PIC_REQUEST = 111;
    private static final int SLIDER_CAMERA_PIC_REQUEST = 112;
    private static final int PREVIEW_IMAGE_CAPTURE = 113;
    private IImagesHandler iImagesHandler = null;
    private boolean IsOnlyLandscapeAllowed = false;
    private DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
    private RecyclerViewClickListener recyclerViewClickListener;
    private ImageSliderDialog imageSliderDialog;
    private CustomImagePreviewDialog customImagePreviewDialog;
    private int sliderImagePos = -1;
    private LinearLayout parentLayout;
    public boolean IsEditable = true;
    private int imgMaxCount = 0;
    private GPSTracker gpsTracker;
    private String latitude = "", longitude = "", currentTimestamp = "";
    private String brandingElements;
    private int maxImagesAllowed = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_image_gallery_with_glide, container, false);

        //Data
        this.context = getContext();
        permissionHandler = new PermissionHandler(getActivity());
        gpsTracker = new GPSTracker(context);

        //Widgets
        parentLayout = rootView.findViewById(R.id.parentLayout);
        textviewMsg = rootView.findViewById(R.id.textviewMsg);
        recyclerViewImageGallery = rootView.findViewById(R.id.recyle_view_image_gallery);
        parentLinearlytAddButton = rootView.findViewById(R.id.parentLinearlytAddButton);
        titletextview = rootView.findViewById(R.id.titletextview);

        setAdapter("");

        //Add button
        parentLinearlytAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (imageList.size() >= maxImagesAllowed) {
                    String msg = context.getResources().getString(R.string.max_images_msg);
                    msg = msg.replace("{MAXCOUNT}", String.valueOf(maxImagesAllowed));
                    AlertDialogBoxInfo.alertDialogShow(context, msg);
                } else {
                    //If the app has not the permission then asking for the permission
                    permissionHandler.requestPermission(parentLinearlytAddButton, Manifest.permission.CAMERA, getString(R.string.needs_permission_camera_msg), new IPermission() {
                        @Override
                        public void IsPermissionGranted(boolean IsGranted) {
                            if (IsGranted) {
                                File file = CommonUtils.getOutputMediaFile(CommonUtils.FILE_IMAGE_TYPE);
                                Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                picUri = Uri.fromFile(file); // create
                                i.putExtra(MediaStore.EXTRA_OUTPUT, picUri); // set the image file
                                i.putExtra("ImageId", picUri); // set the image file
                                startActivityForResult(i, CAMERA_PIC_REQUEST);
                            }
                        }
                    });
                }
            }
        });

        //Recycler View Item Listener
        recyclerViewClickListener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (position != RecyclerView.NO_POSITION) {
                    //showImageSliderDialog(position);
                }
            }
        };

        return rootView;
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

        if (imageList.size() > 0) {
            //Image Slider is used to show Sliding Images with selected position in list.
            imageSliderDialog = new ImageSliderDialog(getActivity(), new ArrayList<Object>(imageList), pos, new ImageSliderDialog.ISliderClickHandler() {
                @Override
                public void captureClick(int position) {
                    sliderImagePos = position;
                    //If the app has not the permission then asking for the permission
                    permissionHandler.requestPermission(parentLinearlytAddButton, Manifest.permission.CAMERA, getString(R.string.needs_permission_camera_msg), new IPermission() {
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
                    imageList = (List<ImageDto>) (Object) objectList;
                    notifyAdapter();
                }
            });
            imageSliderDialog.show();
            imageSliderDialog.setCancelable(false);
            imageSliderDialog.allowChangePhoto(IsEditable);
            imageSliderDialog.allowRemarks(true);
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
        if (iImagesHandler != null)
            iImagesHandler.updateImagesList(imageList);

        imageThumbnailAdapter.notifyDataSetChanged();

        if (imageList.size() == 0) {
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
            List<ImageDto> imageGalleryList = (ArrayList<ImageDto>) args.getSerializable(Constants.INTENT_KEY_IMAGE_GALLERY_LIST);
            String type = args.getString(Constants.INTENT_KEY_TYPE, "");
            String title = args.getString(Constants.INTENT_KEY_TITLE, "");
            maxImagesAllowed = args.getInt(Constants.INTENT_VALUE_MAX_IMAGES_COUNT, 10);
            IsOnlyLandscapeAllowed = args.getBoolean(Constants.INTENT_KEY_PORTRAIT_ALLOWED, false);

            if (!TextUtils.isEmpty(title))
                titletextview.setText(title);
            imageList = imageGalleryList;
            setAdapter(type);

        }
    }

    public void refresh(Bundle args, IImagesHandler iImagesHandler) {
        if (args != null) {

            //Get Data from Bundle
            this.iImagesHandler = iImagesHandler;
            List<ImageDto> imageGalleryList = (ArrayList<ImageDto>) args.getSerializable(Constants.INTENT_KEY_IMAGE_GALLERY_LIST);
            String type = args.getString(Constants.INTENT_KEY_TYPE, "");
            String title = args.getString(Constants.INTENT_KEY_TITLE, "");
            brandingElements = args.getString("BrandingElements", "");
            maxImagesAllowed = args.getInt(Constants.INTENT_VALUE_MAX_IMAGES_COUNT, 10);
            IsOnlyLandscapeAllowed = args.getBoolean(Constants.INTENT_KEY_PORTRAIT_ALLOWED, false);

            if (!TextUtils.isEmpty(title))
                titletextview.setText(Html.fromHtml("<small>" + title + "</small>"));
            imageList = imageGalleryList;
            setAdapter(type);

        }
    }

    public void setAdapter(String TYPE) {
        // add a divider after each item for more clarity
        recyclerViewImageGallery.setItemAnimator(new DefaultItemAnimator());
        //Set Adapter
        imageThumbnailAdapter = new ImageThumbnailAdapter(context, true, imageList, recyclerViewClickListener, new ImageThumbnailAdapter.IImageRemove() {
            @Override
            public void removeImage(ImageDto imageDto) {
                imageList.remove(imageDto);
                notifyAdapter();
            }
        });

        recyclerViewImageGallery.setAdapter(imageThumbnailAdapter);
        if (TYPE.equalsIgnoreCase(Constants.RECYCLER_TYPE_GRID)) {
            int numberOfColumns = 2;
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, numberOfColumns);
            gridLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            recyclerViewImageGallery.setLayoutManager(gridLayoutManager);

        } else if (TYPE.equalsIgnoreCase(Constants.RECYCLER_TYPE_VERTICAL)) {
            LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(new ContextThemeWrapper(context, R.style.scrollbar_vertical_style), LinearLayoutManager.VERTICAL, false);
            recyclerViewImageGallery.setLayoutManager(verticalLayoutManager);

        } else {
            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(new ContextThemeWrapper(context, R.style.scrollbar_horizontal_style), LinearLayoutManager.HORIZONTAL, false);
            recyclerViewImageGallery.setLayoutManager(horizontalLayoutManager);

        }

        if (imageList.size() == 0) {
            textviewMsg.setVisibility(View.VISIBLE);
            recyclerViewImageGallery.setVisibility(View.GONE);

        } else {
            textviewMsg.setVisibility(View.GONE);
            recyclerViewImageGallery.setVisibility(View.VISIBLE);
        }

        //Max Image Count
        imgMaxCount = imageList.size();
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
                String imageBase64 = ImageUtils.updateExifData(picUri, bitmapNew, latitude, longitude, currentTimestamp);

                int status = validateCapturedPhoto(bitmapNew);
                if (status == -1) {
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getString(R.string.landscape_mode_allowed));
                } else {
                    ImageDto imageDto = savePhoto(bitmapNew, imageBase64);
                    imageList.add(imageDto);
                    notifyAdapter();
                    imgMaxCount++;
                    //showImagePreviewDialog((Object) imageDto);
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
/*
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmapNew.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                String imageBase64 = EncryptionUtil.encodeBase64(byteArrayOutputStream.toByteArray());*/
                String imageBase64 = ImageUtils.updateExifData(picUri, bitmapNew, latitude, longitude, currentTimestamp);

                if (IsOnlyLandscapeAllowed)
                    validateAndSaveCapturedPhoto(bitmapNew, imageBase64, true);
                else
                    updatePhoto(bitmapNew, imageBase64, sliderImagePos);

            } catch (Exception e) {
                e.printStackTrace();
                AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
            }
        } else if (requestCode == PREVIEW_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {

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
                savePhoto(bitmapNew, imageBase64);
        }
    }

    public void updatePhoto(Bitmap bitmap, String imageBase64, int position) {
        File file = new File(picUri.toString());
        ImageDto imageDto = imageList.get(position);
        imageDto.setName("Photo " + (imgMaxCount + 1));
        imageDto.setUri(picUri);
        imageDto.setBitmap(bitmap);
        imageDto.setImageBase64(imageBase64);
        imageDto.setCapturedDateTime(dateTimeFormat.format(new Date()));
        imageList.set(position, imageDto);
        notifyAdapter();
        imageSliderDialog.notifyViewPagerAdapter(new ArrayList<Object>(imageList));
        imgMaxCount++;

    }

    public ImageDto savePhoto(Bitmap bitmap, String imageBase64) {
        File file = new File(picUri.toString());
        ImageDto imageDto = new ImageDto();
        imageDto.setId("1");
        imageDto.setName("Photo " + (imgMaxCount + 1));
        imageDto.setUri(picUri);
        imageDto.setBitmap(bitmap);
        imageDto.setImageBase64(imageBase64);
        imageDto.setCapturedDateTime(dateTimeFormat.format(new Date()));
        return imageDto;
       /* imageList.add(imageDto);
        notifyAdapter();
        imgMaxCount++;*/
    }

    public List<ImageDto> getImageGalleryList() {
        return imageList;
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
        parentLinearlytAddButton.setEnabled(false);
        IsEditable = false;
        imageThumbnailAdapter.isCancelEnabled = false;
        parentLinearlytAddButton.setVisibility(View.INVISIBLE);
        notifyAdapter();

    }

    private void showImagePreviewDialog(Object object) {

        if (customImagePreviewDialog != null && customImagePreviewDialog.isShowing()) {
            customImagePreviewDialog.refresh(object);
            return;
        }

        if (object != null) {
            customImagePreviewDialog = new CustomImagePreviewDialog(context, object, brandingElements, new CustomImagePreviewDialog.IImagePreviewDialogClicks() {
                @Override
                public void capturePhotoClick() {
                    //If the app has not the permission then asking for the permission
                    permissionHandler.requestPermission(parentLinearlytAddButton, Manifest.permission.CAMERA, getString(R.string.needs_permission_camera_msg), new IPermission() {
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
                    imageList.add((ImageDto) object);
                    notifyAdapter();
                    imgMaxCount++;
                }
            });
            customImagePreviewDialog.show();
            customImagePreviewDialog.setCancelable(false);
            customImagePreviewDialog.allowChangePhoto(true);
            customImagePreviewDialog.allowImageTypeSelection(true);
            customImagePreviewDialog.allowImageTitle(false);
            customImagePreviewDialog.allowRemarks(true);

        } else {
            Toast.makeText(getActivity(), "No photo available to preview.", Toast.LENGTH_SHORT).show();
        }
    }
}
