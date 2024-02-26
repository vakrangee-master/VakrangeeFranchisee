package in.vakrangee.franchisee.sitelayout.sitereadiness;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import in.vakrangee.franchisee.sitelayout.R;
import in.vakrangee.supercore.franchisee.commongui.ImageSliderDialog;
import in.vakrangee.supercore.franchisee.commongui.imagegallery.ImageDto;
import in.vakrangee.supercore.franchisee.commongui.imagepreview.CustomImagePreviewDialog;
import in.vakrangee.supercore.franchisee.model.FranchiseeDetails;
import in.vakrangee.supercore.franchisee.model.My_vakranggekendra_image;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.GPSTracker;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;

@SuppressLint("ValidFragment")
public class SiteReadinessPhotoFragment extends Fragment implements View.OnClickListener {

    private View view;
    private FranchiseeDetails franchiseeDetails;
    private Context context;

    ImageView imageView1;                    // Frontage Image
    ImageView imageView2;                    // Left Wall Image
    ImageView imageView3;                    // Front Wall Image
    ImageView imageView4;                    // Right Wall Image
    ImageView imageView5;                    // Back Wall Image
    ImageView imageView6;                    // Ceiling Image
    ImageView imageView7;                    // Floor Image

    Bitmap bitmap1, bitmap2, bitmap3, bitmap4, bitmap5, bitmap6, bitmap7;

    final int CAMERA_CAPTURE = 201;     // REQUEST CODE
    final int CHANGE_PHOTO = 202;     // REQUEST CODE
    private Uri picUri;                 // Picture URI

    private int selectedImageViewId = 0;

    TextView clickToViewImage;

    private CustomImagePreviewDialog customImagePreviewDialog;
    private DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");

    private GPSTracker gpsTracker;
    private String latitude = "", longitude = "", currentTimestamp = "";

    @SuppressLint("ValidFragment")
    public SiteReadinessPhotoFragment(FranchiseeDetails franchiseeDetails) {

        this.franchiseeDetails = franchiseeDetails;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_readiness_photo, container, false);

        //Initialize data
        this.context = getContext();
        gpsTracker = new GPSTracker(getContext());

        // Bind GUI
        ButterKnife.bind(this, view);

        imageView1 = view.findViewById(R.id.imageView1);
        imageView2 = view.findViewById(R.id.imageView2);
        imageView3 = view.findViewById(R.id.imageView3);
        imageView4 = view.findViewById(R.id.imageView4);
        imageView5 = view.findViewById(R.id.imageView5);
        imageView6 = view.findViewById(R.id.imageView6);
        imageView7 = view.findViewById(R.id.imageView7);
        clickToViewImage = view.findViewById(R.id.clickToViewImage);
        // Add Listeners
        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);
        imageView4.setOnClickListener(this);
        imageView5.setOnClickListener(this);
        imageView6.setOnClickListener(this);
        imageView7.setOnClickListener(this);
        //imageView8.setOnClickListener(this);

        clickToViewImage.setOnClickListener(this);
        clickToViewImage.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.imageView1) {
            if (TextUtils.isEmpty(franchiseeDetails.getFrontageImageId()) && franchiseeDetails.isAllowToEdit()) {
                capturePhoto(id, CAMERA_CAPTURE);
            } else {
                showImageSlider(0);
            }
        } else if (id == R.id.imageView2) {
            if (TextUtils.isEmpty(franchiseeDetails.getLeftWallImageId()) && franchiseeDetails.isAllowToEdit()) {
                capturePhoto(id, CAMERA_CAPTURE);
            } else {
                showImageSlider(1);
            }
        } else if (id == R.id.imageView3) {
            if (TextUtils.isEmpty(franchiseeDetails.getFrontWallImageId()) && franchiseeDetails.isAllowToEdit()) {
                capturePhoto(id, CAMERA_CAPTURE);
            } else {
                showImageSlider(2);
            }
        } else if (id == R.id.imageView4) {
            if (TextUtils.isEmpty(franchiseeDetails.getRightWallImageId()) && franchiseeDetails.isAllowToEdit()) {
                capturePhoto(id, CAMERA_CAPTURE);
            } else {
                showImageSlider(3);
            }
        } else if (id == R.id.imageView5) {
            if (TextUtils.isEmpty(franchiseeDetails.getBackWallImageId()) && franchiseeDetails.isAllowToEdit()) {
                capturePhoto(id, CAMERA_CAPTURE);
            } else {
                showImageSlider(4);
            }
        } else if (id == R.id.imageView6) {
            if (TextUtils.isEmpty(franchiseeDetails.getCeilingImageId()) && franchiseeDetails.isAllowToEdit()) {
                capturePhoto(id, CAMERA_CAPTURE);
            } else {
                showImageSlider(5);
            }
        } else if (id == R.id.imageView7) {
            if (TextUtils.isEmpty(franchiseeDetails.getFloorImageId()) && franchiseeDetails.isAllowToEdit()) {
                capturePhoto(id, CAMERA_CAPTURE);
            } else {
                showImageSlider(6);
            }
        }
    }

    public ImageDto savePhoto(Bitmap bitmap, String imageBase64) {
        File file = new File(picUri.toString());
        ImageDto imageDto = new ImageDto();
        imageDto.setName(getImageName(selectedImageViewId));
        imageDto.setUri(picUri);
        imageDto.setBitmap(bitmap);
        imageDto.setImageBase64(imageBase64);
        imageDto.setCapturedDateTime(dateTimeFormat.format(new Date()));
        return imageDto;

    }

    public void getLocationAndTimestamp() {
        //Get Current location and time stamp
        if (gpsTracker.canGetLocation()) {
            latitude = String.valueOf(gpsTracker.getLatitude());
            longitude = String.valueOf(gpsTracker.getLongitude());
            currentTimestamp = gpsTracker.getFormattedDateTime();
        }
    }

    // Set Image into Franchisee Detail
    public void setImageIntoFrachiseeDetail(int imageType, Bitmap bitmapNew) {

        //ByteArrayOutputStream byteArrayOutputStream  = ImageUtils.stampWithTimeAndName(bitmapNew, getImageName(imageType));

        Bitmap bitmapTimeStamp = ImageUtils.stampWithTimeAndNameInBitmap(bitmapNew, getImageName(imageType));
        /*ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmapTimeStamp.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);*/
        String imageBase64 = ImageUtils.updateExifData(picUri, bitmapNew, latitude, longitude, currentTimestamp);

        // Convert Bitmap to Bytes
        /*ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmapNew.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);*/
        /*String a = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        byte[] imageBytes = Base64.decode(a, Base64.DEFAULT);*/

        //String image = imageBytes != null ? (EncryptionUtil.encodeBase64(imageBytes)) : "";
        //String imageBase64 = EncryptionUtil.encodeBase64(byteArrayOutputStream.toByteArray());

        // Frontage Image
        if (imageType == R.id.imageView1) {
            bitmap1 = bitmapTimeStamp;
            franchiseeDetails.setFrontageImageId(imageBase64);
            // Left Wall Image
        } else if (imageType == R.id.imageView2) {
            bitmap2 = bitmapTimeStamp;
            franchiseeDetails.setLeftWallImageId(imageBase64);
            // Front Wall Image
        } else if (imageType == R.id.imageView3) {
            bitmap3 = bitmapTimeStamp;
            franchiseeDetails.setFrontWallImageId(imageBase64);
            // Right Wall Image
        } else if (imageType == R.id.imageView4) {
            bitmap4 = bitmapTimeStamp;
            franchiseeDetails.setRightWallImageId(imageBase64);
            // Back Wall Image
        } else if (imageType == R.id.imageView5) {
            bitmap5 = bitmapTimeStamp;
            franchiseeDetails.setBackWallImageId(imageBase64);
            // Ceiling Image
        } else if (imageType == R.id.imageView6) {
            bitmap6 = bitmapTimeStamp;
            franchiseeDetails.setCeilingImageId(imageBase64);
            // Floor Image
        } else if (imageType == R.id.imageView7) {
            bitmap7 = bitmapTimeStamp;
            franchiseeDetails.setFloorImageId(imageBase64);
        }

        // Refresh Image Slider
        if (imageSliderDialog != null) {
            //imageSliderDialog.notifyViewPagerAdapter(new ArrayList<Object>(getImageSliderData()));
            imageSliderDialog.refresh(new ArrayList<Object>(getImageSliderData()), getImagePos(imageType));
        }
    }

    private Bitmap realoadImage(String imageId, ImageView imageView) {
        Bitmap img = CommonUtils.StringToBitMap(imageId);
        if (img != null)
            imageView.setImageBitmap(img);
        return img;
    }

    //region Get Image Name
    public String getImageName(int imageid) {
        String name = null;
        if (imageid == R.id.imageView1) {
            name = "Frontage Image";
        } else if (imageid == R.id.imageView2) {
            name = "Left Wall Image";
        } else if (imageid == R.id.imageView3) {
            name = "Front Wall Image";
        } else if (imageid == R.id.imageView4) {
            name = "Right Wall Image";
        } else if (imageid == R.id.imageView5) {
            name = "Back Wall Image";
        } else if (imageid == R.id.imageView6) {
            name = "Ceiling Image";
        } else if (imageid == R.id.imageView7) {
            name = "Floor Image";
        }
        return name;
    }
    //endregion

    //region Get Image Id
    public int getImageId(int pos) {
        int id = -1;
        switch (pos) {
            case 0:
                id = R.id.imageView1;
                break;
            case 1:
                id = R.id.imageView2;
                break;
            case 2:
                id = R.id.imageView3;
                break;
            case 3:
                id = R.id.imageView4;
                break;
            case 4:
                id = R.id.imageView5;
                break;
            case 5:
                id = R.id.imageView6;
                break;
            case 6:
                id = R.id.imageView7;
                break;
        }
        return id;
    }
    //endregion

    //region Get Image Pos
    public int getImagePos(int imageid) {
        int pos = 0;
        if (imageid == R.id.imageView1) {
            pos = 0;
        } else if (imageid == R.id.imageView2) {
            pos = 1;
        } else if (imageid == R.id.imageView3) {
            pos = 2;
        } else if (imageid == R.id.imageView4) {
            pos = 3;
        } else if (imageid == R.id.imageView5) {
            pos = 4;
        } else if (imageid == R.id.imageView6) {
            pos = 5;
        } else if (imageid == R.id.imageView7) {
            pos = 6;
        }
        return pos;
    }
    //endregion

    //region Show Image Slider
    ImageSliderDialog imageSliderDialog;

    public List<My_vakranggekendra_image> getImageSliderData() {
        //Prepare Data For Image Slider
        List<My_vakranggekendra_image> my_vakranggekendra_images = new ArrayList<My_vakranggekendra_image>();

        // Frontage Image
        // Left Wall Image
        // Front Wall Image
        // Right Wall Image
        // Back Wall Image
        // Ceiling Image
        // Floor Image
        if (!TextUtils.isEmpty(franchiseeDetails.getFrontageImageId())) {
            My_vakranggekendra_image mvi = new My_vakranggekendra_image();
            mvi.setID(0);
            mvi.setImgetype("Frontage Image");
            mvi.setImage(CommonUtils.StringToBitMap(franchiseeDetails.getFrontageImageId()));
            my_vakranggekendra_images.add(mvi);
        }
        if (!TextUtils.isEmpty(franchiseeDetails.getLeftWallImageId())) {
            My_vakranggekendra_image mvi = new My_vakranggekendra_image();
            mvi.setID(1);
            mvi.setImgetype("Left Wall Image");
            mvi.setImage(CommonUtils.StringToBitMap(franchiseeDetails.getLeftWallImageId()));
            my_vakranggekendra_images.add(mvi);

        }
        if (!TextUtils.isEmpty(franchiseeDetails.getFrontWallImageId())) {
            My_vakranggekendra_image mvi = new My_vakranggekendra_image();
            mvi.setID(2);
            mvi.setImgetype("Front Wall Image");
            mvi.setImage(CommonUtils.StringToBitMap(franchiseeDetails.getFrontWallImageId()));
            my_vakranggekendra_images.add(mvi);
        }
        if (!TextUtils.isEmpty(franchiseeDetails.getRightWallImageId())) {
            My_vakranggekendra_image mvi = new My_vakranggekendra_image();
            mvi.setID(3);
            mvi.setImgetype("Right Wall Image");
            mvi.setImage(CommonUtils.StringToBitMap(franchiseeDetails.getRightWallImageId()));
            my_vakranggekendra_images.add(mvi);
        }
        if (!TextUtils.isEmpty(franchiseeDetails.getBackWallImageId())) {
            My_vakranggekendra_image mvi = new My_vakranggekendra_image();
            mvi.setID(4);
            mvi.setImgetype("Back Wall Image");
            mvi.setImage(CommonUtils.StringToBitMap(franchiseeDetails.getBackWallImageId()));
            my_vakranggekendra_images.add(mvi);
        }
        if (!TextUtils.isEmpty(franchiseeDetails.getCeilingImageId())) {
            My_vakranggekendra_image mvi = new My_vakranggekendra_image();
            mvi.setID(5);
            mvi.setImgetype("Ceiling Image");
            mvi.setImage(CommonUtils.StringToBitMap(franchiseeDetails.getCeilingImageId()));
            my_vakranggekendra_images.add(mvi);
        }
        if (!TextUtils.isEmpty(franchiseeDetails.getFloorImageId())) {
            My_vakranggekendra_image mvi = new My_vakranggekendra_image();
            mvi.setID(6);
            mvi.setImgetype("Floor Image");
            mvi.setImage(CommonUtils.StringToBitMap(franchiseeDetails.getFloorImageId()));
            my_vakranggekendra_images.add(mvi);
        }
        return my_vakranggekendra_images;
    }

    public void showImageSlider(int pos) {

        if (imageSliderDialog != null && imageSliderDialog.isShowing()) {
            return;
        }

        List<My_vakranggekendra_image> my_vakranggekendra_images = getImageSliderData();

        if (my_vakranggekendra_images.size() > 0) {

            //Image Slider is used to show Sliding Images with selected position in list.
            imageSliderDialog = new ImageSliderDialog(getActivity(), new ArrayList<Object>(my_vakranggekendra_images), pos, new ImageSliderDialog.ISliderClickHandler() {
                @Override
                public void captureClick(int position) {

                    int id = getImageId(position);
                    if (id > 0) {
                        capturePhoto(id, CHANGE_PHOTO);
                    }
                }

                @Override
                public void saveClick(List<Object> objectList) {

                }
            });
            imageSliderDialog.allowRemarks(false);
            // Based On isAllowToEdit - Allow to change Photo.
            imageSliderDialog.allowChangePhoto(franchiseeDetails.isAllowToEdit());
            imageSliderDialog.show();
            imageSliderDialog.setCancelable(false);
        } else {
            Toast.makeText(getActivity(), "No photos are available for preview.", Toast.LENGTH_SHORT).show();
        }

    }
    //endregion

    //region Show Image Preview Dialog
    private void showImagePreviewDialog(Object object) {

        if (customImagePreviewDialog != null && customImagePreviewDialog.isShowing()) {
            customImagePreviewDialog.refresh(object);
            return;
        }

        if (object != null) {
            customImagePreviewDialog = new CustomImagePreviewDialog(getContext(), object, new CustomImagePreviewDialog.IImagePreviewDialogClicks() {
                @Override
                public void capturePhotoClick() {
                    capturePhoto(selectedImageViewId, CAMERA_CAPTURE);
                }

                @Override
                public void OkClick(Object object) {
                    Bitmap bitmapNew = ((ImageDto) object).getBitmap();
                    ImageView imageView = (ImageView) view.findViewById(selectedImageViewId);
                    imageView.setImageBitmap(bitmapNew);
                    // Set Image to Franchisee Detail
                    setImageIntoFrachiseeDetail(selectedImageViewId, bitmapNew);
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
    //endresgion

    //region On Activity Result
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == CAMERA_CAPTURE && resultCode == Activity.RESULT_OK) {
                getLocationAndTimestamp();
                Bitmap bitmapNew = ImageUtils.getBitmap(getActivity().getContentResolver(), picUri, latitude, longitude, currentTimestamp); //MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), picUri);

                if (selectedImageViewId != 0 && bitmapNew != null) {
                    ImageView img = new ImageView(getActivity());
                    img.setImageBitmap(bitmapNew);

                    // Check Image Capture into Landscape mode or not.
                    if (!CommonUtils.isLandscapePhoto(picUri, img)) {
                        AlertDialogBoxInfo.alertDialogShow(getActivity(), "Capture Image only landscape mode");
                    } else {

                        //Display Preview Dialog
                       /* ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmapNew.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                        String imageBase64 = EncryptionUtil.encodeBase64(byteArrayOutputStream.toByteArray());*/
                        String imageBase64 = ImageUtils.updateExifData(picUri, bitmapNew, latitude, longitude, currentTimestamp);

                        ImageDto imageDto = savePhoto(bitmapNew, imageBase64);
                        showImagePreviewDialog(imageDto);
                    }
                }
            } else if (requestCode == CHANGE_PHOTO && resultCode == Activity.RESULT_OK) {
                getLocationAndTimestamp();
                Bitmap bitmapNew = ImageUtils.getBitmap(getActivity().getContentResolver(), picUri, latitude, longitude, currentTimestamp); //MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), picUri);

                if (selectedImageViewId != 0 && bitmapNew != null) {
                    ImageView img = new ImageView(getActivity());
                    img.setImageBitmap(bitmapNew);

                    // Check Image Capture into Landscape mode or not.
                    if (!CommonUtils.isLandscapePhoto(picUri, img)) {
                        AlertDialogBoxInfo.alertDialogShow(getActivity(), "Capture Image only landscape mode");
                    } else {
                        ImageView imageView = (ImageView) view.findViewById(selectedImageViewId);
                        imageView.setImageBitmap(bitmapNew);
                        // Set Image to Franchisee Detail
                        setImageIntoFrachiseeDetail(selectedImageViewId, bitmapNew);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion

    //region Capture Photo
    public void capturePhoto(int id, int REQUEST_CODE) {
        File file = CommonUtils.getOutputMediaFile(CommonUtils.FILE_IMAGE_TYPE);
        Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        picUri = Uri.fromFile(file); // create
        i.putExtra(MediaStore.EXTRA_OUTPUT, picUri); // set the image file
        i.putExtra("ImageId", picUri); // set the image file
        selectedImageViewId = id;
        startActivityForResult(i, REQUEST_CODE);
    }
    //endregion
}
