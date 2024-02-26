package in.vakrangee.franchisee.commongui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.commongui.photocapture.PhotoDto;
import in.vakrangee.franchisee.commongui.photocapture.PhotoFragment;
import in.vakrangee.supercore.franchisee.service.OkHttpService;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import okhttp3.Response;

public class StartUpActivity extends AppCompatActivity {

    private static final String TAG = "StartUpActivity";
    private PhotoFragment fragmentPhotoCapture;
    private String[] photosArray = {"Frontage", "Left Wall", "Right Wall", "Ceiling"};
    Uri picUri;
    int CAMERA_CAPTURE = 100;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    private Button btnGetData;
    private OkHttpService okHttpService;
    private ImageView imgResponse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);

        String msg = getIntent().getStringExtra("body");
        String val = getIntent().getStringExtra("popup");
        Log.e(TAG, "FCMTesting: msg: " + msg + " val: " + val);

        //Widgets
        fragmentPhotoCapture = (PhotoFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentPhotoCapture);
        reloadImageFragment();

        //GEt Data
        okHttpService = new OkHttpService(this);
        btnGetData = findViewById(R.id.btnGetData);
        imgResponse = findViewById(R.id.imgResponse);
        btnGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new GetOKHttpData().execute("");

                Picasso.with(StartUpActivity.this)
                        .load("https://vkmssit.vakrangee.in/ImageDisplayDatabase?type=A&image_id=1")
                        //.networkPolicy(NetworkPolicy.OFFLINE)
                        //.memoryPolicy(MemoryPolicy.)
                        .into(imgResponse);
            }
        });
    }

    public class GetOKHttpData extends AsyncTask<String, Integer, String> {
        Response responseData;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {

            String url = "https://vkms.vakrangee.in/ImageDisplayDatabase?type=A&image_id=665";
            responseData = okHttpService.getResponseFromService(true, url);
            Log.e(TAG, "GetOKHttpData: Response data: " + responseData);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Bitmap bm = BitmapFactory.decodeStream(responseData.body().byteStream());
            imgResponse.setImageBitmap(bm);
        }
    }

    public List<PhotoDto> preparePhotoList() {
        List<PhotoDto> photoList = new ArrayList<PhotoDto>();
        for (int i = 0; i < photosArray.length; i++) {
            PhotoDto photoDto = new PhotoDto();
            photoDto.setName(photosArray[i]);
            photoList.add(photoDto);
        }
        return photoList;
    }

    public void reloadImageFragment() {
        List<PhotoDto> photoDtoList = preparePhotoList();

        Bundle args = new Bundle();
        args.putSerializable(Constants.INTENT_KEY_PHOTO_CAPTURE_LIST, (Serializable) photoDtoList);
        args.putString(Constants.INTENT_KEY_TYPE, Constants.RECYCLER_TYPE_HORIZONTAL);
        args.putBoolean(Constants.INTENT_KEY_PORTRAIT_ALLOWED, true);
        fragmentPhotoCapture.refresh(args);
    }

    public void searchPlace(View v) {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void capturePhoto(View v) {

        String data = "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?><billFetchRequest><inputParams><input><paramName>a</paramName><paramValue>10</paramValue></input><input><paramName>a b</paramName><paramValue>20</paramValue></input><input><paramName>a b c</paramName><paramValue>30</paramValue></input><input><paramName>a b c d</paramName><paramValue>40</paramValue></input><input><paramName>a b c d e</paramName><paramValue>50</paramValue></input></inputParams><customerInfo><customerEmail>nileshdhola1@gmail.com</customerEmail><customerMobile>9898990084</customerMobile></customerInfo><agentDeviceInfo><initChannel>%s</initChannel><ip>%s</ip><mac>%s</mac></agentDeviceInfo><agentId>%s</agentId><billerId>%s</billerId></billFetchRequest>";
        String base64 = EncryptionUtil.encryptString(data, this);
        Log.e("Test", "Enc: " + base64);

       /* //setCameraDisplayOrientation(this, 1, new Camera());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        File file = CommonUtils.getOutputMediaFile(1);
        Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        //i.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        picUri = Uri.fromFile(file); // create
        i.putExtra(MediaStore.EXTRA_OUTPUT, picUri); // set the image file
        startActivityForResult(i, CAMERA_CAPTURE);*/
    }

    public static void setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
}
