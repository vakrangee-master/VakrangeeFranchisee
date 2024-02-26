package in.vakrangee.simcarddetail.simcarddetails;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import in.vakrangee.simcarddetail.R;
import in.vakrangee.supercore.franchisee.application.VakrangeeKendraApplication;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.commongui.FileUtils;
import in.vakrangee.supercore.franchisee.nextgenfranchiseeapplication.CustomFranchiseeApplicationSpinnerDto;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.GPSTracker;
import in.vakrangee.supercore.franchisee.utils.GUIUtils;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;
import in.vakrangee.supercore.franchisee.utils.CustomSearchableSpinner;

/**
 * A simple {@link Fragment} subclass.
 */
public class SimcardDetailsFragment extends BaseFragment implements AdapterView.OnItemSelectedListener,
        View.OnClickListener {

    private static final String TAG = "SimcardDetailsFragment";
    private View view;
    private Context context;
    private DeprecateHandler deprecateHandler;
    private PermissionHandler permissionHandler;

    private SimcardDetailsRepository simcardDetailsRepository;
    private AsyncGetSimcardDetailsData asyncGetSimcardDetailsData = null;

    private SimcardDetailsDto simcardDetailsDto;

    //view find id
    private CustomSearchableSpinner spinnerSimcardProvider, spinnerATMServiceProvider;
    private EditText editTextSimcardNumber, editTextSimcardIMSINumber;
    private LinearLayout layoutATMData, layoutSimcardDetails;
    private ImageView imgSimcardCover, imgSimcardPhoto;
    private TextView imgSimcardCoverScan;
    private List<CustomFranchiseeApplicationSpinnerDto> listServiceProvider;
    private List<CustomFranchiseeApplicationSpinnerDto> listATMProvider;
    private GetAllSpinnerData getAllSpinnerData;

    private Uri picUri;
    private static final int CAMERA_PIC_REQUEST = 100;
    private int FROM = -1;
    private static final int SIMCARD_COVER = 1;
    private static final int SIMCARD_IMAGE = 2;
    private String latitude = "", longitude = "", currentTimestamp = "";
    private GPSTracker gpsTracker;
    private String mCurrentPhotoPath;
    private CustomImageZoomDialogSimcard customImagePreviewDialog;
    private static final int REQUEST_CODE_QR_SCAN = 101;
    private String atmID, serviceProviderId;

    public SimcardDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_simcard_details, container, false);

        //Initialize data
        this.context = getContext();

        findViewId(view);

        return view;
    }

    private void findViewId(View view) {
        gpsTracker = new GPSTracker(context);
        deprecateHandler = new DeprecateHandler(context);
        permissionHandler = new PermissionHandler((Activity) context);

        simcardDetailsRepository = new SimcardDetailsRepository(context);

        spinnerATMServiceProvider = view.findViewById(R.id.spinnerATMServiceProvider);
        spinnerSimcardProvider = view.findViewById(R.id.spinnerSimcardProvider);
        editTextSimcardNumber = view.findViewById(R.id.editTextSimcardNumber);
        editTextSimcardIMSINumber = view.findViewById(R.id.editTextSimcardIMSINumber);

        layoutATMData = view.findViewById(R.id.layoutATMData);
        layoutSimcardDetails = view.findViewById(R.id.layoutSimcardDetails);

        imgSimcardCover = view.findViewById(R.id.imgSimcardCover);
        imgSimcardPhoto = view.findViewById(R.id.imgSimcardPhoto);
        imgSimcardCoverScan = view.findViewById(R.id.imgSimcardCoverScan);

        imgSimcardCover.setOnClickListener(this);
        imgSimcardPhoto.setOnClickListener(this);
        imgSimcardCoverScan.setOnClickListener(this);


        editTextSimcardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String result = s.toString().replaceAll(" ", "");
                if (!s.toString().equals(result)) {
                    editTextSimcardNumber.setText(result);
                    editTextSimcardNumber.setSelection(result.length());
                    // alert the user
                }
            }
        });

        editTextSimcardIMSINumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String result = s.toString().replaceAll(" ", "");
                if (!s.toString().equals(result)) {
                    editTextSimcardIMSINumber.setText(result);
                    editTextSimcardIMSINumber.setSelection(result.length());
                    // alert the user
                }
            }
        });
        simcardDetailsDto = new SimcardDetailsDto();

        getAllSpinnerData = new GetAllSpinnerData();
        getAllSpinnerData.execute();
    }

    public void reload(String result) {
        try {
            if (TextUtils.isEmpty(result)) {
                AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                return;
            }

            if (result.startsWith("ERROR")) {
                String msg = result.replace("ERROR|", "");
                msg = TextUtils.isEmpty(msg) ? "Something went wrong. Please try again later." : msg;
                AlertDialogBoxInfo.alertDialogShow(context, msg);
                return;
            }

            if (result.startsWith("OKAY")) {
                //Handle Response
                String data = result.replace("OKAY|", "");
                if (TextUtils.isEmpty(data))
                    AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                else {
                    simcardDetailsData(data);
                }
            } else {
                AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
        }


    }

    public void simcardDetailsData(String data) {
        //Reload Data
        try {
            if (TextUtils.isEmpty(data)) {
                simcardDetailsDto = new SimcardDetailsDto();
            } else {
                Gson gson = new GsonBuilder().create();
                List<SimcardDetailsDto> applicationList = gson.fromJson(data, new TypeToken<ArrayList<SimcardDetailsDto>>() {
                }.getType());
                if (applicationList.size() > 0) {
                    simcardDetailsDto = applicationList.get(0);
                } else {
                    simcardDetailsDto = new SimcardDetailsDto();
                    // simcardDetailsDto = getSimcardDetailsDto();
                }
            }
            bindData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindData() {

        //STEP 0: service provider
        //setSimcardProvider(listServiceProvider, spinnerSimcardProvider, simcardDetailsDto.getServiceProvider());
        //setATMServiceProvider(listATMProvider, spinnerATMServiceProvider, simcardDetailsDto.getAtmId());

        //STEP 1: SImcard number
        editTextSimcardNumber.setText(simcardDetailsDto.getAtmSimNumber());
        //STEP 2: SImcard number
        editTextSimcardIMSINumber.setText(simcardDetailsDto.getAtmSimIMSINumber());

        //STEP 3 :Simcard cover photo
        if (!TextUtils.isEmpty(simcardDetailsDto.getAtmSimCoverImageId())) {
            String simcardCoverURl = Constants.DownloadImageUrl + simcardDetailsDto.getAtmSimCoverImageId();
            Glide.with(context)
                    .load(simcardCoverURl)
                    .apply(new RequestOptions()
                            .error(R.drawable.ic_camera_alt_black_72dp)
                            .placeholder(R.drawable.ic_camera_alt_black_72dp)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgSimcardCover);
        } else {
            imgSimcardCover.setImageDrawable(getResources().getDrawable(R.drawable.ic_camera_alt_black_72dp));
        }

        //STEP 4 :Simcard  photo
        if (!TextUtils.isEmpty(simcardDetailsDto.getAtmSimImageId())) {
            String simcardCoverURl = Constants.DownloadImageUrl + simcardDetailsDto.getAtmSimImageId();
            Glide.with(context)
                    .load(simcardCoverURl)
                    .apply(new RequestOptions()
                            .error(R.drawable.ic_camera_alt_black_72dp)
                            .placeholder(R.drawable.ic_camera_alt_black_72dp)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgSimcardPhoto);
        } else {
            imgSimcardPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_camera_alt_black_72dp));
        }

        //STEP 5 :Simcard  qr code Photo
        if (!TextUtils.isEmpty(simcardDetailsDto.getAtmSimQrCodeDetail())) {
            setFontawesomeIcon(imgSimcardCoverScan, getResources().getString(R.string.fa_circle_check), "1");
            // imgSimcardCoverScan.setImageDrawable(getResources().getDrawable(R.drawable.approved));
        } else {
            setFontawesomeIcon(imgSimcardCoverScan, getResources().getString(R.string.fa_qrcode), "0");
            // setFontawesomeIcon(imgSimcardCoverScan, "");
            //imgSimcardCoverScan.setImageDrawable(getResources().getDrawable(R.drawable.ic_camera_alt_black_72dp));
        }


        //Enable/disable views
        //boolean IsEditable = (!TextUtils.isEmpty(simcardDetailsDto.getIsEditable()) && simcardDetailsDto.getIsEditable().equalsIgnoreCase("1")) ? true : false;
        if (!TextUtils.isEmpty(simcardDetailsDto.getIsEditable())) {
            if (simcardDetailsDto.getIsEditable().equalsIgnoreCase("1")) {
                //GUIUtils.setViewAndChildrenEnabled(layoutSimcardDetails, true);
                ((SimcardDetailsActivity) getActivity()).IsFooterLayoutVisible(false);
            } else {
                // GUIUtils.setViewAndChildrenEnabled(layoutSimcardDetails, false);
                ((SimcardDetailsActivity) getActivity()).IsFooterLayoutVisible(true);
            }
        }


    }

    //region Set State Adapter
    private void setSimcardProvider(List<CustomFranchiseeApplicationSpinnerDto> list, Spinner spinnerProvider, String selStateId) {
        spinnerProvider.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, list));
        int appsstatePos = simcardDetailsRepository.getSelectedPos(list, selStateId);
        spinnerProvider.setSelection(appsstatePos);
        spinnerProvider.setOnItemSelectedListener(this);

    }

    //region Set State Adapter
    private void setATMServiceProvider(List<CustomFranchiseeApplicationSpinnerDto> list, Spinner spinnerProvider, String selStateId) {
        spinnerProvider.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, list));
        int appsstatePos = simcardDetailsRepository.getSelectedPos(list, selStateId);
        spinnerProvider.setSelection(appsstatePos);
        spinnerProvider.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int Id = parent.getId();
        if (Id == R.id.spinnerSimcardProvider) {
            if (position > 0) {
                spinnerSimcardProvider.setTitle("Select Simcard Provider");
                spinnerSimcardProvider.requestFocus();
                CustomFranchiseeApplicationSpinnerDto spinnerDto = (CustomFranchiseeApplicationSpinnerDto) spinnerSimcardProvider.getItemAtPosition(position);
                if (!spinnerDto.getId().equals("0")) {
                    serviceProviderId = spinnerDto.getId();
                    //  simcardDetailsDto.setServiceProvider(spinnerDto.getId());

                }
            } else {
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));
                serviceProviderId = "0";
                //simcardDetailsDto.setServiceProvider("0");
            }
        } else if (Id == R.id.spinnerATMServiceProvider) {
            if (position > 0) {
                spinnerATMServiceProvider.setTitle("Select ATM ID");
                spinnerATMServiceProvider.requestFocus();
                CustomFranchiseeApplicationSpinnerDto spinnerDto = (CustomFranchiseeApplicationSpinnerDto) spinnerATMServiceProvider.getItemAtPosition(position);
                if (!spinnerDto.getId().equals("0")) {
                    atmID = spinnerDto.getId();
                    //  simcardDetailsDto.setAtmId(spinnerDto.getId());
                    layoutATMData.setVisibility(View.VISIBLE);
                    ((SimcardDetailsActivity) getActivity()).IsFooterLayoutVisible(true);
                    asyntaskCall(spinnerDto.getId());
                }
            } else {
                layoutATMData.setVisibility(View.GONE);
                ((SimcardDetailsActivity) getActivity()).IsFooterLayoutVisible(false);
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));
                //simcardDetailsDto.setAtmId("0");
                atmID = "0";

            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void asyntaskCall(String ATMID) {
        asyncGetSimcardDetailsData = new AsyncGetSimcardDetailsData(context, ATMID, new AsyncGetSimcardDetailsData.Callback() {
            @Override
            public void onResult(String result) {
                reload(result);
            }
        });
        asyncGetSimcardDetailsData.execute();
    }


    @Override
    public void onClick(View v) {
        int Id = v.getId();
        if (Id == R.id.imgSimcardCover) {
            FROM = SIMCARD_COVER;
            if (TextUtils.isEmpty(simcardDetailsDto.getAtmSimCoverImageId())) {
                startCamera(view);
            } else {
                showImagePreviewDialog(simcardDetailsDto, v);
            }
           /* if (TextUtils.isEmpty(simcardDetailsDto.getAtmSimCoverImageId())) {
                if (TextUtils.isEmpty(simcardDetailsDto.getAtmSimcardCoverPhotoBase64())) {
                    startCamera(view);
                } else {
                    showImagePreviewDialog(simcardDetailsDto, v);
                }
                //startCamera(view);
            } else {
                showImagePreviewDialog(simcardDetailsDto, v);
            }*/
        }
        if (Id == R.id.imgSimcardPhoto) {
            FROM = SIMCARD_IMAGE;

            if (TextUtils.isEmpty(simcardDetailsDto.getAtmSimImageId())) {
                startCamera(view);
            } else {
                showImagePreviewDialog(simcardDetailsDto, v);
            }

        }
        if (Id == R.id.imgSimcardCoverScan) {
            Intent i = new Intent(context, QrCodeActivity.class);
            startActivityForResult(i, REQUEST_CODE_QR_SCAN);
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
                picUri = FileProvider.getUriForFile(getContext(), context.getApplicationContext().getPackageName() + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
                startActivityForResult(takePictureIntent, CAMERA_PIC_REQUEST);
            }
        }
    }

    private void showImagePreviewDialog(Object object, final View view1) {
        if (customImagePreviewDialog != null && customImagePreviewDialog.isShowing()) {
            customImagePreviewDialog.refresh(object);
            return;
        }

        if (object != null) {
            customImagePreviewDialog = new CustomImageZoomDialogSimcard(context, object, FROM, new CustomImageZoomDialogSimcard.IImagePreviewDialogClicks() {
                @Override
                public void capturePhotoClick() {
                    startCamera(view1);
                }

                @Override
                public void OkClick(Object object) {
                    SimcardDetailsDto dto = ((SimcardDetailsDto) object);

                    try {
                        if (FROM == 1) {
                            simcardDetailsDto.setAtmSimcardCoverPhotoBase64(dto.getAtmSimcardCoverPhotoBase64());
                            simcardDetailsDto.setSimcardCoverPhotoExt(dto.getSimcardCoverPhotoExt());
                            simcardDetailsDto.setAtmSimCoverImageId(dto.getAtmSimCoverImageId());

                            //Refresh SIm card cover
                            if (!TextUtils.isEmpty(simcardDetailsDto.getAtmSimcardCoverPhotoBase64())) {
                                Bitmap bitmap = CommonUtils.StringToBitMap(simcardDetailsDto.getAtmSimcardCoverPhotoBase64());
                                if (bitmap != null)
                                    imgSimcardCover.setImageBitmap(bitmap);
                            }

                        } else if (FROM == 2) {
                            simcardDetailsDto.setAtmSimcardPhotoBase64(dto.getAtmSimcardPhotoBase64());
                            simcardDetailsDto.setSimcardPhotoExt(dto.getSimcardPhotoExt());
                            simcardDetailsDto.setAtmSimImageId(dto.getAtmSimImageId());

                            //Refresh SIm card cover
                            if (!TextUtils.isEmpty(simcardDetailsDto.getAtmSimcardPhotoBase64())) {
                                Bitmap bitmap = CommonUtils.StringToBitMap(simcardDetailsDto.getAtmSimcardPhotoBase64());
                                if (bitmap != null)
                                    imgSimcardPhoto.setImageBitmap(bitmap);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            customImagePreviewDialog.show();
            customImagePreviewDialog.setCancelable(false);

            //Title
            String title;
            if (FROM == 1) {
                title = "SIM Card Cover Image";
            } else {
                title = "SIM Card Image";
            }

            customImagePreviewDialog.setDialogTitle(title);

            //Change Photo Allowed
            //boolean IsEditable = (!TextUtils.isEmpty(panCardDetailsDto.getIsEditable()) && panCardDetailsDto.getIsEditable().equalsIgnoreCase("1")) ? true : false;
            //customImagePreviewDialog.allowChangePhoto(IsEditable);
            customImagePreviewDialog.allowImageTitle(false);
            customImagePreviewDialog.allowChangePhoto(true);
            //customImagePreviewDialog.allowSaveOption(true);
            customImagePreviewDialog.allowRemarks(false);

        } else {
            Toast.makeText(context, "No photo available to preview.", Toast.LENGTH_SHORT).show();
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

    //endregion

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK) {
                Uri imageUri = Uri.parse(mCurrentPhotoPath);
                picUri = imageUri;

                getLocationAndTimestamp();
                Bitmap bitmapNew = ImageUtils.getBitmap(getActivity().getContentResolver(), picUri, latitude, longitude, currentTimestamp);

                //BitMap with TimeStamp on it
                bitmapNew = ImageUtils.stampWithTimeInBitmap(bitmapNew);
                String imageBase64 = ImageUtils.updateExifData(picUri, bitmapNew, latitude, longitude, currentTimestamp);

                ImageView imageView = new ImageView(context);
                imageView.setImageBitmap(bitmapNew);

                File file = new File(picUri.getPath());
                String base64Data = CommonUtils.convertBitmapToString(bitmapNew);
                String fileName = URLDecoder.decode(file.getName(), "UTF-8");


                setImageAndName(fileName, base64Data, picUri);
                /*if (!CommonUtils.isLandscapePhoto(picUri, imageView)) {
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getString(R.string.landscape_mode_allowed));
                } else {
                    File file = new File(picUri.getPath());
                    String base64Data = CommonUtils.convertBitmapToString(bitmapNew);
                    String fileName = URLDecoder.decode(file.getName(), "UTF-8");
                    setImageAndName(fileName, base64Data, picUri);
                }*/
            }
            if (requestCode == REQUEST_CODE_QR_SCAN) {
                if (data == null)
                    return;
                //Getting the passed result
                String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
                simcardDetailsDto.setAtmSimQrCodeDetail(result);
                setFontawesomeIcon(imgSimcardCoverScan, getResources().getString(R.string.fa_circle_check), "1");
                //  imgSimcardCoverScan.setImageDrawable(context.getResources().getDrawable(R.drawable.approved));
                AlertDialogBoxInfo.showOkDialog(context, "Scan Successfully.");
            }
            /*if (resultCode != Activity.RESULT_OK) {
                setFontawesomeIcon(imgSimcardCoverScan, getResources().getString(R.string.fa_qrcode), "0");
                // imgSimcardCoverScan.setImageDrawable(context.getResources().getDrawable(R.drawable.reject));
                AlertDialogBoxInfo.showOkDialog(context, "QR Code could not be scanned");
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setImageAndName(String fileName, String base64Data, Uri uri) {

        try {
            Bitmap imageBitMap = null;
            boolean IsDrawable = false;
            String ext = FileUtils.getFileExtension(context, uri);
            if (ext.equalsIgnoreCase("pdf"))
                IsDrawable = true;
            else
                imageBitMap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);

            setImageData(IsDrawable, imageBitMap, fileName, base64Data, ext);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setImageData(boolean IsDrawable, Bitmap bitmap, String fileName, String
            base64, String ext) {

        switch (FROM) {
            case SIMCARD_COVER:
               /* if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgSimcardCover);
                else
                    Glide.with(context).asBitmap().load(bitmap).into(imgSimcardCover);*/

                simcardDetailsDto.setAtmSimcardCoverPhotoBase64(base64);
                simcardDetailsDto.setSimcardCoverPhotoExt(ext);
                showImagePreviewDialog((Object) simcardDetailsDto, view);
                break;

            case SIMCARD_IMAGE:
                /*if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgSimcardPhoto);
                else
                    Glide.with(context).asBitmap().load(bitmap).into(imgSimcardPhoto);*/


                simcardDetailsDto.setAtmSimcardPhotoBase64(base64);
                simcardDetailsDto.setSimcardPhotoExt(ext);
                showImagePreviewDialog((Object) simcardDetailsDto, view);
                break;
            default:
                break;

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


    class GetAllSpinnerData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressSpinner(context);
        }

        @Override
        protected String doInBackground(String... strings) {

            Connection connection = new Connection(context);
            String tmpVkId = connection.getVkid();
            // String enquiryId = CommonUtils.getEnquiryId(context);
            // String vkIdOrEnquiryId = TextUtils.isEmpty(tmpVkId) ? enquiryId : tmpVkId;

            listServiceProvider = simcardDetailsRepository.getSimcardComapanyName(tmpVkId);
            listATMProvider = simcardDetailsRepository.getATMServiceProviderList(tmpVkId);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissProgressSpinner();
            bindSpinnerData();
            //bindData();
        }
    }

    private void bindSpinnerData() {
        setSimcardProvider(listServiceProvider, spinnerSimcardProvider, simcardDetailsDto.getAtmSimServiceProviderId());
        setATMServiceProvider(listATMProvider, spinnerATMServiceProvider, simcardDetailsDto.getAtmId());

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public int isValidationData() {
        //XMl to json
        String simNo = null, imsiNo = null;

        if (!TextUtils.isEmpty(simcardDetailsDto.getAtmSimQrCodeDetail())) {
            String xmlString = simcardDetailsDto.getAtmSimQrCodeDetail();  // some XML String previously created
            XmlToJson xmlToJson = new XmlToJson.Builder(xmlString).build();
            JSONObject jsonObject = xmlToJson.toJson();
            try {
                JSONObject object = jsonObject.getJSONObject("PrintLetterBarcodeData");
                imsiNo = object.getString("imsiNo");
                //String mobileNo = object.getString("mobileNo");
                simNo = object.getString("simNo");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        simcardDetailsDto.setAtmSimServiceProviderId(serviceProviderId);
        simcardDetailsDto.setAtmId(atmID);
        //STEP 0: State
        // simcardDetailsDto.setServiceProvider("1");
        if (TextUtils.isEmpty(simcardDetailsDto.getAtmSimServiceProviderId()) || simcardDetailsDto.getAtmSimServiceProviderId().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Service Provider.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerSimcardProvider, "Please select Service Provider.", context);
            return 1;
        }

        //Step 1: Validate Simcard Number
        simcardDetailsDto.setAtmSimNumber(editTextSimcardNumber.getText().toString().trim());
        if (TextUtils.isEmpty(simcardDetailsDto.getAtmSimNumber())) {
            //if (TextUtils.isEmpty(gstDetailsDto.getTrnNumber())) {
            Toast.makeText(context, "Please enter Simcard Number.", Toast.LENGTH_LONG).show();
            editTextSimcardNumber.setError("Please enter 20 character simcard number.");
            return 2;
        }

        int len = editTextSimcardNumber.getText().toString().length();
        if (len < 20) {
            Toast.makeText(context, "Simcard number should be 20 characters long.", Toast.LENGTH_LONG).show();
            editTextSimcardNumber.setError("Simcard number should be 20 characters.");
            return 3;
        }

        //Step 2: Validate Simcard IMSI
        simcardDetailsDto.setAtmSimIMSINumber(editTextSimcardIMSINumber.getText().toString().trim());
        if (TextUtils.isEmpty(simcardDetailsDto.getAtmSimIMSINumber())) {
            //if (TextUtils.isEmpty(gstDetailsDto.getTrnNumber())) {
            Toast.makeText(context, "Please enter simcard IMSI Number.", Toast.LENGTH_LONG).show();
            editTextSimcardIMSINumber.setError("Please enter 15 digits simcard IMSI number.");
            return 4;
        }

        int lenIMSI = editTextSimcardIMSINumber.getText().toString().length();
        if (lenIMSI < 15) {
            Toast.makeText(context, "Simcard number should be 15 characters long.", Toast.LENGTH_LONG).show();
            editTextSimcardIMSINumber.setError("Simcard IMSI number should be 15 digits.");
            return 5;
        }

        //STEP 3: Simcard cover Image
        if (TextUtils.isEmpty(simcardDetailsDto.getAtmSimCoverImageId()) || simcardDetailsDto.getAtmSimCoverImageId().equalsIgnoreCase("0")) {
            if (TextUtils.isEmpty(simcardDetailsDto.getAtmSimcardCoverPhotoBase64())) {
                AlertDialogBoxInfo.alertDialogShow(context, "Please capture simcard cover Image.");
                return 6;
            }
        }

        //STEP 4: Simcard image
        if (TextUtils.isEmpty(simcardDetailsDto.getAtmSimImageId()) || simcardDetailsDto.getAtmSimImageId().equalsIgnoreCase("0")) {
            if (TextUtils.isEmpty(simcardDetailsDto.getAtmSimcardPhotoBase64())) {
                AlertDialogBoxInfo.alertDialogShow(context, "Please capture simcard Image.");
                return 7;
            }
        }

        //STEP 5: Simcard QR scan
        if (TextUtils.isEmpty(simcardDetailsDto.getSimcardQRcodePhotoId()) || simcardDetailsDto.getSimcardQRcodePhotoId().equalsIgnoreCase("0")) {
            if (TextUtils.isEmpty(simcardDetailsDto.getAtmSimQrCodeDetail())) {
                AlertDialogBoxInfo.alertDialogShow(context, "Please scan QR code Image.");
                return 8;
            }
        }

        //STEP 6: ATM service Provider
        if (TextUtils.isEmpty(simcardDetailsDto.getAtmId()) || simcardDetailsDto.getAtmId().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select ATM ID.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerATMServiceProvider, "Please select ATM ID.", context);
            return 9;
        }

        //checl atmsim number with scan qr code
        if (simNo != null && !simcardDetailsDto.getAtmSimNumber().equalsIgnoreCase(simNo)) {
            editTextSimcardNumber.setError("scan Sim card number not match");
            return 2;
        }

        if (imsiNo != null && !simcardDetailsDto.getAtmSimIMSINumber().equalsIgnoreCase(imsiNo)) {
            editTextSimcardIMSINumber.setError("scan Sim card IMSI number not match");
            return 2;
        }


        return 0;
    }

    public SimcardDetailsDto getSimcardDetailsDto() {

        if (simcardDetailsDto == null)
            return null;

        return simcardDetailsDto;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (asyncGetSimcardDetailsData != null && !asyncGetSimcardDetailsData.isCancelled()) {
            asyncGetSimcardDetailsData.cancel(true);
        }
    }

    public void setFontawesomeIcon(TextView textView, String icon, String type) {
        textView.setText(icon);
        textView.setTextSize(80);
        if (type.equalsIgnoreCase("1")) {
            textView.setTextColor(deprecateHandler.getColor(R.color.green));
        } else {
            textView.setTextColor(deprecateHandler.getColor(R.color.black));
        }
        textView.setTypeface(VakrangeeKendraApplication.font_awesome, Typeface.BOLD);

    }

}
