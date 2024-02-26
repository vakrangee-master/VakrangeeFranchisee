package in.vakrangee.core.commongui.carouselgallery;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import in.vakrangee.core.R;
import in.vakrangee.core.commongui.animation.AnimationHanndler;
import in.vakrangee.core.commongui.imagegallery.ImageDto;
import in.vakrangee.core.commongui.imagegallery.RecyclerViewClickListener;
import in.vakrangee.core.ifc.IRadioButtonClick;
import in.vakrangee.core.utils.AlertDialogBoxInfo;
import in.vakrangee.core.utils.CommonUtils;
import in.vakrangee.core.utils.DeprecateHandler;
import in.vakrangee.core.utils.GPSTracker;
import in.vakrangee.core.utils.IPermission;
import in.vakrangee.core.utils.ImageUtils;
import in.vakrangee.core.utils.PermissionHandler;

public class ImageCarouselGalleryFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "ImageCarouselGalleryFragment";

    private Context context;
    private PermissionHandler permissionHandler;
    private GPSTracker gpsTracker;
    private DeprecateHandler deprecateHandler;
    private LinearLayout parentLayout;
    private RadioGroup radioGroupStatus;
    private EditText editTextRemarks;
    private LinearLayout layoutAddPhotos;
    private Button btnSaveWIPInteriorImages;
    private Uri picUri;
    private static final int CAMERA_PIC_REQUEST = 111;
    private String latitude = "", longitude = "", currentTimestamp = "";
    private boolean IsOnlyLandscapeAllowed = false;
    private int imgMaxCount = 0;
    private DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
    private int maxImagesAllowed = 10;

    private ImageCarouselAdapter mAdapter;
    private List<ImageDto> mData = new ArrayList<>();
    private LinearLayout layoutImagePreview;
    private ImageView imageViewPreview;
    private RecyclerViewClickListener mListener;
    private CarouselLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private int prevPos = 0;
    private IRadioButtonClick iRadioButtonClick;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_carousel_gallery, container, false);

        //Data
        this.context = getContext();
        permissionHandler = new PermissionHandler(getActivity());
        gpsTracker = new GPSTracker(context);
        deprecateHandler = new DeprecateHandler(context);

        //Widgets
        parentLayout = rootView.findViewById(R.id.parentLayout);
        editTextRemarks = rootView.findViewById(R.id.editTextRemarks);
        layoutAddPhotos = rootView.findViewById(R.id.layoutAddPhotos);
        btnSaveWIPInteriorImages = rootView.findViewById(R.id.btnSaveWIPInteriorImages);
        layoutImagePreview = rootView.findViewById(R.id.layoutImagePreview);
        imageViewPreview = rootView.findViewById(R.id.imageViewPreview);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        radioGroupStatus = rootView.findViewById(R.id.radioGroupStatus);

        //Listeners
        layoutAddPhotos.setOnClickListener(this);
        btnSaveWIPInteriorImages.setOnClickListener(this);

        //Recycler View Item Listener
        mListener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (position != RecyclerView.NO_POSITION) {
                    ImageDto imageDto = mData.get(position);
                    refreshRemarks(position);
                    Toast.makeText(context, imageDto.getName(), Toast.LENGTH_SHORT).show();

                }
            }
        };

        setAdapter(mData);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();

        if (Id == R.id.layoutAddPhotos) {
            AnimationHanndler.bubbleAnimation(context, view);
            addPhoto(view);

        } else if (Id == R.id.btnSaveWIPInteriorImages) {

        }

    }

    public List<ImageDto> getImageGalleryList() {
        return mData;
    }

    public void addPhoto(View view) {
        if (mData.size() >= maxImagesAllowed) {
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

    @Override
    public void onPause() {
        super.onPause();
        permissionHandler.dismissSnackbar();
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
                   /* if (imageDto.getBitmap() != null) {
                        Glide.with(context).asBitmap().load(imageDto.getBitmap())
                                .apply(new RequestOptions()
                                        .fitCenter())
                                .into(imageViewPreview);
                    }*/

                    mData.add(imageDto);
                    notifyAdapter();
                    imgMaxCount++;
                }

            } catch (Exception e) {
                e.printStackTrace();
                AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
            }
        }
    }

    public void refresh(List<ImageDto> imageGalleryList, int maxCount, String status, final IRadioButtonClick iRadioButtonClick) {
        maxImagesAllowed = maxCount;
        IsOnlyLandscapeAllowed = false;
        this.iRadioButtonClick = iRadioButtonClick;

        mData = imageGalleryList;
        if (mData == null)
            mData = new ArrayList<>();
        setAdapter(mData);

        //Status
        radioGroupStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonCompleted) {
                    iRadioButtonClick.onRadioButtonSelected("1");
                } else if (checkedId == R.id.radioButtonWIP) {
                    iRadioButtonClick.onRadioButtonSelected("0");
                }
            }
        });

        if (status != null) {
            if (status.equals("1")) {
                radioGroupStatus.check(R.id.radioButtonCompleted);

            } else if (status.equals("0")) {
                radioGroupStatus.check(R.id.radioButtonWIP);
            }
        }
    }

    public void setAdapter(List<ImageDto> imageList) {
        mData = imageList;

        if (mData == null || mData.size() == 0) {
            imageViewPreview.setVisibility(View.VISIBLE);
            layoutImagePreview.setVisibility(View.GONE);
        } else {
            imageViewPreview.setVisibility(View.GONE);
            layoutImagePreview.setVisibility(View.VISIBLE);
        }

        layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
        layoutManager.addOnItemSelectionListener(new CarouselLayoutManager.OnCenterItemSelectionListener() {
            @Override
            public void onCenterItemChanged(int adapterPosition) {
                refreshRemarks(adapterPosition);
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new CenterScrollListener());
        mAdapter = new ImageCarouselAdapter(context, mData, mListener, new ImageCarouselAdapter.IImageRemove() {
            @Override
            public void removeImage(ImageDto imageDto) {
                mData.remove(imageDto);
                notifyAdapter();
            }
        });
        recyclerView.setAdapter(mAdapter);
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
    }

    public void notifyAdapter() {

        Collections.reverse(mData);
        mAdapter.notifyDataSetChanged();

        if (mData.size() == 0) {
            imageViewPreview.setVisibility(View.VISIBLE);
            layoutImagePreview.setVisibility(View.GONE);
        } else {
            imageViewPreview.setVisibility(View.GONE);
            layoutImagePreview.setVisibility(View.VISIBLE);
        }
    }

    private void refreshRemarks(int pos) {

        //Update Prev Remarks
        if (prevPos < mData.size()) {
            ImageDto preDto = mData.get(prevPos);
            preDto.setRemarks(editTextRemarks.getText().toString());
            mData.set(prevPos, preDto);
        }

        if (pos < mData.size()) {
            ImageDto imageDto = mData.get(pos);
            editTextRemarks.setText(imageDto.getRemarks());
            prevPos = pos;
        }

    }

}
