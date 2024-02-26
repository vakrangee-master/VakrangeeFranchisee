package in.vakrangee.core.commongui.gallery;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.vakrangee.core.R;
import in.vakrangee.core.commongui.animation.AnimationHanndler;
import in.vakrangee.core.commongui.imagegallery.ImageDto;
import in.vakrangee.core.commongui.imagegallery.RecyclerViewClickListener;
import in.vakrangee.core.commongui.imagepreview.ImageZoom;
import in.vakrangee.core.utils.AlertDialogBoxInfo;
import in.vakrangee.core.utils.CommonUtils;
import in.vakrangee.core.utils.Constants;
import in.vakrangee.core.utils.DeprecateHandler;
import in.vakrangee.core.utils.GPSTracker;
import in.vakrangee.core.utils.IPermission;
import in.vakrangee.core.utils.ImageUtils;
import in.vakrangee.core.utils.PermissionHandler;

public class ImageSMGalleryFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "ImageSMGalleryFragment";

    private Context context;
    private PermissionHandler permissionHandler;
    private GPSTracker gpsTracker;

    private LinearLayout parentLayout;
    private LinearLayout layoutImagePreview;
    private ImageZoom imageViewPreview;
    private LinearLayout layoutThumbnails;
    private TextView textviewMsg;
    private LinearLayout layoutImagesGallery;
    //private ImageView imgAddPhotos;
    private LinearLayout layoutAddPhotos;
    private RecyclerView recyclerViewImagePreview;
    private Button btnSaveWIPInteriorImages;

    private ImageSMGalleryAdapter imageThumbnailAdapter;
    private DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");

    private int maxImagesAllowed = 0;
    private List<ImageDto> imageList = new ArrayList<ImageDto>();
    private Uri picUri;
    private static final int CAMERA_PIC_REQUEST = 111;
    private String latitude = "", longitude = "", currentTimestamp = "";
    private boolean IsOnlyLandscapeAllowed = false;
    private int imgMaxCount = 0;
    private RecyclerViewClickListener recyclerViewClickListener;
    private DeprecateHandler deprecateHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_gallery_preview, container, false);

        //Data
        this.context = getContext();
        permissionHandler = new PermissionHandler(getActivity());
        gpsTracker = new GPSTracker(context);
        deprecateHandler = new DeprecateHandler(context);

        //Widgets
        parentLayout = rootView.findViewById(R.id.parentLayout);
        layoutImagePreview = rootView.findViewById(R.id.layoutImagePreview);
        imageViewPreview = rootView.findViewById(R.id.imageViewPreview);
        layoutThumbnails = rootView.findViewById(R.id.layoutThumbnails);
        textviewMsg = rootView.findViewById(R.id.textviewMsg);
        layoutImagesGallery = rootView.findViewById(R.id.layoutImagesGallery);
        layoutAddPhotos = rootView.findViewById(R.id.layoutAddPhotos);
        recyclerViewImagePreview = rootView.findViewById(R.id.recyle_view_image_gallery_preview);
        btnSaveWIPInteriorImages = rootView.findViewById(R.id.btnSaveWIPInteriorImages);

        //Listeners
        layoutAddPhotos.setOnClickListener(this);
        btnSaveWIPInteriorImages.setOnClickListener(this);

        //Recycler View Item Listener
        recyclerViewClickListener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (position != RecyclerView.NO_POSITION) {
                    ImageDto imageDto = imageList.get(position);
                    if (imageDto.getBitmap() != null) {
                        Glide.with(context).asBitmap().load(imageDto.getBitmap())
                                .apply(new RequestOptions()
                                        .fitCenter())
                                .into(imageViewPreview);
                        //imageViewPreview.setImageBitmap(imageDto.getBitmap());
                    }
                }
            }
        };

        setAdapter("");
        refresh(imageList);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();

        if (Id == R.id.layoutAddPhotos) {
            AnimationHanndler.bubbleAnimation(context, view);
            addPhoto(view);

        } else if (Id == R.id.btnSaveWIPInteriorImages) {
            Toast.makeText(context, "Size: " + imageList.size(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        permissionHandler.dismissSnackbar();
    }

    public void addPhoto(View view) {
        if (imageList.size() >= maxImagesAllowed) {
            String msg = context.getResources().getString(R.string.max_images_msg);
            msg = msg.replace("{MAXCOUNT}", String.valueOf(maxImagesAllowed));
            AlertDialogBoxInfo.alertDialogShow(context, msg);

        } else {
            //If the app has not the permission then asking for the permission
            permissionHandler.requestPermission(view, Manifest.permission.CAMERA, getString(R.string.needs_permission_camera_msg), new IPermission() {
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

    public List<ImageDto> getImageGalleryList() {
        return imageList;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void getLocationAndTimestamp() {
        //Get Current location and time stamp
        if (gpsTracker.canGetLocation()) {
            latitude = String.valueOf(gpsTracker.getLatitude());
            longitude = String.valueOf(gpsTracker.getLongitude());
            currentTimestamp = gpsTracker.getFormattedDateTime();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK) {
            try {
                getLocationAndTimestamp();
                Bitmap bitmapNew = ImageUtils.getBitmap(getActivity().getContentResolver(), picUri, latitude, longitude, currentTimestamp);
                bitmapNew = ImageUtils.rotateImageIfRequired(getActivity().getContentResolver(), bitmapNew, picUri);
                //BitMap with TimeStamp on it
                bitmapNew = ImageUtils.stampWithTimeInBitmap(bitmapNew);
                String imageBase64 = ImageUtils.updateExifData(picUri, bitmapNew, latitude, longitude, currentTimestamp);

                int status = validateCapturedPhoto(bitmapNew);
                if (status == -1) {
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getString(R.string.landscape_mode_allowed));
                } else {
                    ImageDto imageDto = savePhoto(bitmapNew, imageBase64);
                    if (imageDto.getBitmap() != null) {
                        Glide.with(context).asBitmap().load(imageDto.getBitmap())
                                .apply(new RequestOptions()
                                        .fitCenter())
                                .into(imageViewPreview);
                    }

                    imageList.add(imageDto);
                    notifyAdapter();
                    imgMaxCount++;
                }

            } catch (Exception e) {
                e.printStackTrace();
                AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
            }
        }
    }

    public void refresh(List<ImageDto> imageGalleryList) {
        String type = Constants.RECYCLER_TYPE_HORIZONTAL;
        maxImagesAllowed = 10;
        IsOnlyLandscapeAllowed = false;

        imageList = imageGalleryList;
        setAdapter(type);
    }

    public void setAdapter(String TYPE) {
        // add a divider after each item for more clarity
        recyclerViewImagePreview.setItemAnimator(new DefaultItemAnimator());
        //Set Adapter
        imageThumbnailAdapter = new ImageSMGalleryAdapter(context, imageList, recyclerViewClickListener, new ImageSMGalleryAdapter.IImageRemove() {
            @Override
            public void removeImage(ImageDto imageDto) {
                imageList.remove(imageDto);
                notifyAdapter();
                //Display Default image
                imageViewPreview.invalidate();
                imageViewPreview.setImageBitmap(null);
                //imageViewPreview.setImageResource(R.drawable.ic_camera_alt_black_72dp);
                imageViewPreview.setImageResource(R.drawable.preview1);
            }
        });

        recyclerViewImagePreview.setAdapter(imageThumbnailAdapter);
        if (TYPE.equalsIgnoreCase(Constants.RECYCLER_TYPE_GRID)) {
            int numberOfColumns = 2;
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, numberOfColumns);
            gridLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            recyclerViewImagePreview.setLayoutManager(gridLayoutManager);

        } else if (TYPE.equalsIgnoreCase(Constants.RECYCLER_TYPE_VERTICAL)) {
            LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(new ContextThemeWrapper(context, R.style.scrollbar_vertical_style), LinearLayoutManager.VERTICAL, false);
            recyclerViewImagePreview.setLayoutManager(verticalLayoutManager);

        } else {
            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(new ContextThemeWrapper(context, R.style.scrollbar_horizontal_style), LinearLayoutManager.HORIZONTAL, false);
            recyclerViewImagePreview.setLayoutManager(horizontalLayoutManager);

        }

        if (imageList.size() == 0) {
            textviewMsg.setVisibility(View.VISIBLE);
            recyclerViewImagePreview.setVisibility(View.GONE);

        } else {
            textviewMsg.setVisibility(View.GONE);
            recyclerViewImagePreview.setVisibility(View.VISIBLE);
        }

        //Max Image Count
        imgMaxCount = imageList.size();
    }

    public void notifyAdapter() {

        imageThumbnailAdapter.notifyDataSetChanged();

        if (imageList.size() == 0) {
            textviewMsg.setVisibility(View.VISIBLE);
            recyclerViewImagePreview.setVisibility(View.GONE);
            parentLayout.invalidate();
        } else {
            textviewMsg.setVisibility(View.GONE);
            recyclerViewImagePreview.setVisibility(View.VISIBLE);
        }
    }

}
