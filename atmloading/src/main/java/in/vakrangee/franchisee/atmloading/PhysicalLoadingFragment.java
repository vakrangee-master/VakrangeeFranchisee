package in.vakrangee.franchisee.atmloading;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import in.vakrangee.supercore.franchisee.commongui.DateTimePickerDialog;
import in.vakrangee.supercore.franchisee.commongui.FileUtils;
import in.vakrangee.supercore.franchisee.commongui.imagepreview.CustomImageZoomDialog;
import in.vakrangee.supercore.franchisee.model.ATMRoCashLoadingDetailsDto;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.GPSTracker;
import in.vakrangee.supercore.franchisee.utils.GUIUtils;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhysicalLoadingFragment extends Fragment implements ATMActivity.ifcLoadingDatePickerClick {

    private View view;
    private Context context;
    private AsyncSaveATMLoadingDetails asyncSaveATMLoadingDetails;
    private AsyncGetATMPhysicalORCashLoadingDetails asyncGetATMPhysicalORCashLoadingDetails;
    private DateTimePickerDialog dateTimePickerDialog;
    private List<ATMRoCashLoadingDetailsDto> loadingDetailsDto;
    private String imgBase64;
    private Uri picUri;
    private Bitmap bitmap = null;
    private static final int CAMERA_PIC_REQUEST = 1;
    private CustomImageZoomDialog customImagePreviewDialog;
    private PermissionHandler permissionHandler;
    private String mCurrentPhotoPath;
    private GPSTracker gpsTracker;
    private String latitude = "", longitude = "", currentTimestamp = "";
    private ATMRoCashLoadingDetailsDto atmRoPhysicalLoadingDetailsDto;
    private CustomATMLoadinDialog customATMLoadinDialog;
    private String loadingDate;

    private EditText edittextPhyscialC1;
    private EditText edittextPhyscialC2;
    private EditText edittextPhyscialC3;
    private EditText edittextPhyscialC4;
    private TextView textPhyscialC1;
    private TextView textPhyscialC2;
    private TextView textPhyscialC3;
    private TextView textPhyscialC4;
    private TextView textTotalNoteCountPhyscial;
    private TextView textTotalAmountCountPhyscial;
    private TextView textTotalNoteCountPurge;
    private TextView textTotalAmountCountPurge;
    private EditText edittextPurgeC1;
    private EditText edittextPurgeC2;
    private EditText edittextPurgeC3;
    private TextView textPurgeC1;
    private TextView textPurgeC2;
    private TextView textPurgeC3;
    private LinearLayout layoutParent;
    private ImageView imageViewCapturePhysicalLoading;
    private Button btnSubmit;

    public PhysicalLoadingFragment() {
        // Required empty public constructor
    }

    // newInstance constructor for creating fragment with arguments
    public static PhysicalLoadingFragment newInstance() {
        PhysicalLoadingFragment fragmentFirst = new PhysicalLoadingFragment();
        Bundle args = new Bundle();
        //args.putInt("someInt", page);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_physical_loading, container, false);
        bindViewId(view);
        ButterKnife.bind(this, view);
        this.context = getContext();
        permissionHandler = new PermissionHandler(getActivity());
        gpsTracker = new GPSTracker(context);
        atmRoPhysicalLoadingDetailsDto = new ATMRoCashLoadingDetailsDto();
       /* physicalLodingDto = new PhysicalLodingDto();
        purgeBinDto = new PurgeBinDto();*/

        //----------physcial load
        editTextAmountEnter(edittextPhyscialC1, textPhyscialC1, Long.valueOf(2000));
        editTextAmountEnter(edittextPhyscialC2, textPhyscialC2, Long.valueOf(500));
        editTextAmountEnter(edittextPhyscialC3, textPhyscialC3, Long.valueOf(100));
        editTextAmountEnter(edittextPhyscialC4, textPhyscialC4, Long.valueOf(100));


        edittextPhyscialC1.setFilters(new InputFilter[]{new CommonUtils.InputFilterMinMax("1", "2500")});
        edittextPhyscialC2.setFilters(new InputFilter[]{new CommonUtils.InputFilterMinMax("1", "2500")});
        edittextPhyscialC3.setFilters(new InputFilter[]{new CommonUtils.InputFilterMinMax("1", "2500")});
        edittextPhyscialC4.setFilters(new InputFilter[]{new CommonUtils.InputFilterMinMax("1", "2500")});

        //------------Purge bin
        editTextAmountEnter(edittextPurgeC1, textPurgeC1, Long.valueOf(2000));
        editTextAmountEnter(edittextPurgeC2, textPurgeC2, Long.valueOf(500));
        editTextAmountEnter(edittextPurgeC3, textPurgeC3, Long.valueOf(100));

        edittextPurgeC1.setFilters(new InputFilter[]{new CommonUtils.InputFilterMinMax("1", "2500")});
        edittextPurgeC2.setFilters(new InputFilter[]{new CommonUtils.InputFilterMinMax("1", "2500")});
        edittextPurgeC3.setFilters(new InputFilter[]{new CommonUtils.InputFilterMinMax("1", "5000")});

        imageViewCapturePhysicalLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capturePhyscialLoadingImage(view);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int valid = validationPhysicalDto();
             /*   if (valid == 0) {
                    //show dialog
                    showATMLoadingDialog((Object) atmRoPhysicalLoadingDetailsDto);
                }*/
                showATMLoadingDialog((Object) atmRoPhysicalLoadingDetailsDto);
            }
        });
        return view;
    }

    private void bindViewId(View view) {
        //Physcial Cash
        edittextPhyscialC1 = view.findViewById(R.id.edittextPhyscialC1);
        edittextPhyscialC2 = view.findViewById(R.id.edittextPhyscialC2);
        edittextPhyscialC3 = view.findViewById(R.id.edittextPhyscialC3);
        edittextPhyscialC4 = view.findViewById(R.id.edittextPhyscialC4);

        //textview -show total amount
        textPhyscialC1 = view.findViewById(R.id.textPhyscialC1);
        textPhyscialC2 = view.findViewById(R.id.textPhyscialC2);
        textPhyscialC3 = view.findViewById(R.id.textPhyscialC3);
        textPhyscialC4 = view.findViewById(R.id.textPhyscialC4);
        //textview- total note count
        textTotalNoteCountPhyscial = view.findViewById(R.id.textTotalNoteCountPhyscial);
        textTotalAmountCountPhyscial = view.findViewById(R.id.textTotalAmountCountPhyscial);

        //-----------------Purge Bin--------------------------
        textTotalNoteCountPurge = view.findViewById(R.id.textTotalNoteCountPurge);
        textTotalAmountCountPurge = view.findViewById(R.id.textTotalAmountCountPurge);
        edittextPurgeC1 = view.findViewById(R.id.edittextPurgeC1);
        edittextPurgeC2 = view.findViewById(R.id.edittextPurgeC2);
        edittextPurgeC3 = view.findViewById(R.id.edittextPurgeC3);

        textPurgeC1 = view.findViewById(R.id.textPurgeC1);
        textPurgeC2 = view.findViewById(R.id.textPurgeC2);
        textPurgeC3 = view.findViewById(R.id.textPurgeC3);
        layoutParent = view.findViewById(R.id.layoutParent);
        imageViewCapturePhysicalLoading = view.findViewById(R.id.imageViewCapturePhysicalLoading);
        btnSubmit = view.findViewById(R.id.btnSubmit);
    }

    private void showATMLoadingDialog(Object object) {
        if (object != null) {
            customATMLoadinDialog = new CustomATMLoadinDialog(context, object, "1", new CustomATMLoadinDialog.IfcATMLoadingDialogClicks() {
                @Override
                public void OkClick(Object object) {
                    if (object instanceof ATMRoCashLoadingDetailsDto) {
                        ATMRoCashLoadingDetailsDto loadingDetailsDto = (ATMRoCashLoadingDetailsDto) object;
                        Gson gson = new Gson();
                        String jsonData = gson.toJson(loadingDetailsDto, ATMRoCashLoadingDetailsDto.class);
                        System.out.println(jsonData);
                    }
                }
            });
            customATMLoadinDialog.show();
            customATMLoadinDialog.setCancelable(false);
            customATMLoadinDialog.setDialogTitle("Physical Cash Preview");

        } else {
            Toast.makeText(context, "No physical loading available to preview.", Toast.LENGTH_SHORT).show();
        }
    }

    //region validation
    private int validationPhysicalDto() {
        int valid = 0;
        atmRoPhysicalLoadingDetailsDto.setOpeningCassette1(edittextPhyscialC1.getText().toString());
        atmRoPhysicalLoadingDetailsDto.setOpeningCassette2(edittextPhyscialC2.getText().toString());
        atmRoPhysicalLoadingDetailsDto.setOpeningCassette3(edittextPhyscialC3.getText().toString());
        atmRoPhysicalLoadingDetailsDto.setOpeningCassette4(edittextPhyscialC4.getText().toString());

        atmRoPhysicalLoadingDetailsDto.setPurgeCassette1(edittextPurgeC1.getText().toString());
        atmRoPhysicalLoadingDetailsDto.setPurgeCassette2(edittextPurgeC2.getText().toString());
        atmRoPhysicalLoadingDetailsDto.setPurgeCassette3(edittextPurgeC3.getText().toString());

        atmRoPhysicalLoadingDetailsDto.setTotalPhysicalNote(textTotalNoteCountPhyscial.getText().toString());
        atmRoPhysicalLoadingDetailsDto.setTotalPhysicalAmount(textTotalAmountCountPhyscial.getText().toString());

        atmRoPhysicalLoadingDetailsDto.setTotalPurgeNote(textTotalNoteCountPurge.getText().toString());
        atmRoPhysicalLoadingDetailsDto.setTotalPurgeAmount(textTotalAmountCountPurge.getText().toString());

        atmRoPhysicalLoadingDetailsDto.setLoadingDate(loadingDate);

        if (TextUtils.isEmpty(atmRoPhysicalLoadingDetailsDto.getOpeningCassette1())) {
            Toast.makeText(context, "Please enter note", Toast.LENGTH_LONG).show();
            edittextPhyscialC1.setError("Please enter note.");
            return 1;
        }
        if (TextUtils.isEmpty(atmRoPhysicalLoadingDetailsDto.getOpeningCassette2())) {
            Toast.makeText(context, "Please enter note", Toast.LENGTH_LONG).show();
            edittextPhyscialC2.setError("Please enter note.");
            return 2;
        }
        if (TextUtils.isEmpty(atmRoPhysicalLoadingDetailsDto.getOpeningCassette3())) {
            Toast.makeText(context, "Please enter note", Toast.LENGTH_LONG).show();
            edittextPhyscialC3.setError("Please enter note.");
            return 3;
        }
        if (TextUtils.isEmpty(atmRoPhysicalLoadingDetailsDto.getOpeningCassette4())) {
            Toast.makeText(context, "Please enter note", Toast.LENGTH_LONG).show();
            edittextPhyscialC4.setError("Please enter note.");
            return 4;
        }
        if (TextUtils.isEmpty(atmRoPhysicalLoadingDetailsDto.getPurgeCassette1())) {
            Toast.makeText(context, "Please enter note", Toast.LENGTH_LONG).show();
            edittextPurgeC1.setError("Please enter note.");
            return 5;
        }
        if (TextUtils.isEmpty(atmRoPhysicalLoadingDetailsDto.getPurgeCassette2())) {
            Toast.makeText(context, "Please enter note", Toast.LENGTH_LONG).show();
            edittextPurgeC2.setError("Please enter note.");
            return 6;
        }
        if (TextUtils.isEmpty(atmRoPhysicalLoadingDetailsDto.getPurgeCassette3())) {
            Toast.makeText(context, "Please enter note", Toast.LENGTH_LONG).show();
            edittextPurgeC3.setError("Please enter note.");
            return 7;
        }
        if (TextUtils.isEmpty(atmRoPhysicalLoadingDetailsDto.getSetPhysicalImage())) {
            Toast.makeText(context, "Please Capture Physical Cash Loading Image", Toast.LENGTH_LONG).show();
            return 8;
        }
        if (TextUtils.isEmpty(loadingDate)) {
            Toast.makeText(context, "Please select loading date", Toast.LENGTH_LONG).show();
            return 1;
        }
        return valid;
    }

    private void capturePhyscialLoadingImage(View view) {
        if (TextUtils.isEmpty(imgBase64)) {
            startCamera(view);
        } else {
            bitmap = CommonUtils.StringToBitMap(imgBase64);
            ATMRoCashLoadingDetailsDto imageDto = new ATMRoCashLoadingDetailsDto();
            //  imageDto.setChangedPhoto(Boolean.getBoolean(String.valueOf(loadingDetailsDto.get(0).isChangedPhoto())) ? false : loadingDetailsDto.get(0).isChangedPhoto());
            // imageDto.setPhysicalImageId(TextUtils.isEmpty(loadingDetailsDto.get(0).getPhysicalImageId()) ? "" : loadingDetailsDto.get(0).getPhysicalImageId());
            imageDto.setBitmap(bitmap);
            imageDto.setName("");
            imageDto.setSetPhysicalImage(imgBase64);
            showImagePreviewDialog(imageDto);
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
    //endregion

    private void dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
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

                    atmRoPhysicalLoadingDetailsDto.setExt(FileUtils.getFileExtension(context, picUri));
                    atmRoPhysicalLoadingDetailsDto.setId(null);

                    atmRoPhysicalLoadingDetailsDto.setUri(imageUri);
                    atmRoPhysicalLoadingDetailsDto.setBitmap(bitmapNew);
                    atmRoPhysicalLoadingDetailsDto.setChangedPhoto(true);
                    atmRoPhysicalLoadingDetailsDto.setSetPhysicalImage(imageBase64);
                    //loadingDetailsDto.get(0).setChangedPhoto(true);
                    showImagePreviewDialog((Object) atmRoPhysicalLoadingDetailsDto);
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
                    ATMRoCashLoadingDetailsDto imageDto = ((ATMRoCashLoadingDetailsDto) object);
                    imgBase64 = imageDto.getSetPhysicalImage();
                    if (!TextUtils.isEmpty(imgBase64)) {
                        bitmap = CommonUtils.StringToBitMap(imgBase64);
                        if (bitmap != null)
                            imageViewCapturePhysicalLoading.setImageBitmap(bitmap);
                    }
                }
            });
            customImagePreviewDialog.show();
            customImagePreviewDialog.setCancelable(false);
            customImagePreviewDialog.setDialogTitle("Physical Loading Image");
            customImagePreviewDialog.allowChangePhoto(true);
            customImagePreviewDialog.allowRemarks(false);

        } else {
            Toast.makeText(context, "No photo available to preview.", Toast.LENGTH_SHORT).show();
        }
    }
    //endregion

    public String saveData() {
        String jsonData = null;
        try {

            String pc1 = edittextPhyscialC1.getText().toString().equals("") ? "0" : edittextPhyscialC1.getText().toString();
            String pc2 = edittextPhyscialC2.getText().toString().equals("") ? "0" : edittextPhyscialC2.getText().toString();
            String pc3 = edittextPhyscialC3.getText().toString().equals("") ? "0" : edittextPhyscialC3.getText().toString();
            String pc4 = edittextPhyscialC4.getText().toString().equals("") ? "0" : edittextPhyscialC4.getText().toString();
            String totalCountpc = textTotalNoteCountPhyscial.getText().toString().equals("") ? "0" : textTotalNoteCountPhyscial.getText().toString();
            String totalAmountpc = textTotalAmountCountPhyscial.getText().toString().equals("") ? "0" : textTotalAmountCountPhyscial.getText().toString();
            totalAmountpc = totalAmountpc.replace(" ₹", "");

            //purge bin - pb
            String pb1 = edittextPurgeC1.getText().toString().equals("") ? "0" : edittextPurgeC1.getText().toString();
            String pb2 = edittextPurgeC2.getText().toString().equals("") ? "0" : edittextPurgeC2.getText().toString();
            String pb3 = edittextPurgeC3.getText().toString().equals("") ? "0" : edittextPurgeC3.getText().toString();
            String totalCountpb = textTotalNoteCountPurge.getText().toString().equals("") ? "0" : textTotalNoteCountPurge.getText().toString();
            String totalAmountpb = textTotalAmountCountPurge.getText().toString().equals("") ? "0" : textTotalAmountCountPurge.getText().toString();
            totalAmountpb = totalAmountpb.replace(" ₹", "");

            if (loadingDetailsDto != null) {
                loadingDetailsDto.get(0).setOpeningCassette1(pc1);
                loadingDetailsDto.get(0).setOpeningCassette2(pc2);
                loadingDetailsDto.get(0).setOpeningCassette3(pc3);
                loadingDetailsDto.get(0).setOpeningCassette4(pc4);

                loadingDetailsDto.get(0).setPurgeCassette1(pb1);
                loadingDetailsDto.get(0).setPurgeCassette2(pb2);
                loadingDetailsDto.get(0).setPurgeCassette3(pb2);

                Gson gson = new GsonBuilder().create();
                jsonData = gson.toJson(loadingDetailsDto, new TypeToken<ArrayList<ATMRoCashLoadingDetailsDto>>() {
                }.getType());
            } else {
                // atmRoPhysicalLoadingDetailsDto = new ATMRoCashLoadingDetailsDto();
                atmRoPhysicalLoadingDetailsDto.setOpeningCassette1(pc1);
                atmRoPhysicalLoadingDetailsDto.setOpeningCassette2(pc2);
                atmRoPhysicalLoadingDetailsDto.setOpeningCassette3(pc3);
                atmRoPhysicalLoadingDetailsDto.setOpeningCassette4(pc4);

                atmRoPhysicalLoadingDetailsDto.setPurgeCassette1(pb1);
                atmRoPhysicalLoadingDetailsDto.setPurgeCassette2(pb2);
                atmRoPhysicalLoadingDetailsDto.setPurgeCassette3(pb2);
                Gson gson = new Gson();
                jsonData = gson.toJson(loadingDetailsDto, ATMRoCashLoadingDetailsDto.class);
            }
            return jsonData;
            //saveATMPhysicalCashLoadingDetails(jsonData);
        } catch (Exception e) {
            e.getMessage();
        }

        return jsonData;
    }


    private void saveATMPhysicalCashLoadingDetails(String jsonData) {
        asyncSaveATMLoadingDetails = new AsyncSaveATMLoadingDetails(context, Constants.PHYSICAL_LOADING_TYPE, jsonData, new AsyncSaveATMLoadingDetails.Callback() {
            @Override
            public void onResult(String result) {
                if (TextUtils.isEmpty(result)) {
                    AlertDialogBoxInfo.alertDialogShow(context, result);
                    return;
                }
                if (result.startsWith("ERROR")) {
                    result = result.replace("ERROR|", "");
                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                    } else {
                        AlertDialogBoxInfo.alertDialogShow(context, result);
                    }
                } else if (result.startsWith("OKAY")) {
                    //reload data
                    if (result.startsWith("OKAY")) {
                        result = result.replace("OKAY|", "");
                        //set data
                        // dataSet(result);
                    }
                } else {
                    AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));

                }

            }
        });
        asyncSaveATMLoadingDetails.execute();
    }

    private void editTextAmountEnter(final EditText enterNote, final TextView amount, final Long note) {
        enterNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                CommonUtils.totalPhyscialNoteCount(textTotalNoteCountPhyscial, edittextPhyscialC1, edittextPhyscialC2, edittextPhyscialC3,
                        edittextPhyscialC4, textTotalAmountCountPhyscial);
                CommonUtils.totalPhyscialNoteCountPurge(textTotalNoteCountPurge, edittextPurgeC1, edittextPurgeC2, edittextPurgeC3,
                        textTotalAmountCountPurge);


            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    String e = enterNote.getText().toString();
                    Long cst = Long.valueOf(e);
                    cst = cst * note;

                    String moneyString = new DecimalFormat("##,##,##0").format(cst);
                    // amount.setText(moneyString + " ₹");
                    amount.setText(moneyString);
                } else {
                    //amount.setText("0 ₹");
                    amount.setText("0");
                }

                String tmp = editable.toString().trim();
                if (tmp.length() == 1 && tmp.equals("0"))
                    editable.clear();
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        //Date Time DIalog
        if (dateTimePickerDialog != null && dateTimePickerDialog.isShowing()) {
            dateTimePickerDialog.dismiss();
            dateTimePickerDialog = null;
        }

        if (asyncGetATMPhysicalORCashLoadingDetails != null && !asyncGetATMPhysicalORCashLoadingDetails.isCancelled()) {
            asyncGetATMPhysicalORCashLoadingDetails.cancel(true);
        }

        if (asyncSaveATMLoadingDetails != null && !asyncSaveATMLoadingDetails.isCancelled()) {
            asyncSaveATMLoadingDetails.cancel(true);
        }
    }

    public void reload(String getPhysicalLoadingDetails) {
        System.out.println(getPhysicalLoadingDetails);
        if (TextUtils.isEmpty(getPhysicalLoadingDetails)) {
            return;
        }
        setData(getPhysicalLoadingDetails);

    }

    private void setData(String getPhysicalLoadingDetails) {
        try {
            JSONObject jsonObject = new JSONObject(getPhysicalLoadingDetails);
            JSONArray jsonArrayatmLoadingDetails = jsonObject.getJSONArray("atmLoading");
            JSONArray jsonArrayreceiptCountNumber = jsonObject.getJSONArray("receiptCount");
            String receiptAmount = jsonArrayreceiptCountNumber.getJSONObject(0).getString("amount");

            //set loading details
            Gson gson = new GsonBuilder().create();
            loadingDetailsDto = gson.fromJson(jsonArrayatmLoadingDetails.toString(), new TypeToken<ArrayList<ATMRoCashLoadingDetailsDto>>() {
            }.getType());
            edittextPhyscialC1.setText(TextUtils.isEmpty(loadingDetailsDto.get(0).getOpeningCassette1()) ? "0" : loadingDetailsDto.get(0).getOpeningCassette1());
            edittextPhyscialC2.setText(TextUtils.isEmpty(loadingDetailsDto.get(0).getOpeningCassette2()) ? "0" : loadingDetailsDto.get(0).getOpeningCassette2());
            edittextPhyscialC3.setText(TextUtils.isEmpty(loadingDetailsDto.get(0).getOpeningCassette3()) ? "0" : loadingDetailsDto.get(0).getOpeningCassette3());
            edittextPhyscialC4.setText(TextUtils.isEmpty(loadingDetailsDto.get(0).getOpeningCassette4()) ? "0" : loadingDetailsDto.get(0).getOpeningCassette4());

            //purge bin
            String purgeBinC3 = TextUtils.isEmpty(loadingDetailsDto.get(0).getPurgeCassette3()) ? "0" : loadingDetailsDto.get(0).getPurgeCassette3();
            String purgeBinC4 = TextUtils.isEmpty(loadingDetailsDto.get(0).getPurgeCassette4()) ? "0" : loadingDetailsDto.get(0).getPurgeCassette4();
            int totalC3C4 = Integer.parseInt(purgeBinC3) + Integer.parseInt(purgeBinC4);

            edittextPurgeC1.setText(TextUtils.isEmpty(loadingDetailsDto.get(0).getPurgeCassette1()) ? "0" : loadingDetailsDto.get(0).getPurgeCassette1());
            edittextPurgeC2.setText(TextUtils.isEmpty(loadingDetailsDto.get(0).getPurgeCassette2()) ? "0" : loadingDetailsDto.get(0).getPurgeCassette2());
            edittextPurgeC3.setText(String.valueOf(totalC3C4));

            //set date
            CommonUtils.animateTextView(0, Integer.parseInt(edittextPhyscialC1.getText().toString()), edittextPhyscialC1);
            CommonUtils.animateTextView(0, Integer.parseInt(edittextPhyscialC2.getText().toString()), edittextPhyscialC2);
            CommonUtils.animateTextView(0, Integer.parseInt(edittextPhyscialC3.getText().toString()), edittextPhyscialC3);
            CommonUtils.animateTextView(0, Integer.parseInt(edittextPhyscialC4.getText().toString()), edittextPhyscialC4);

            CommonUtils.animateTextView(0, Integer.parseInt(edittextPurgeC1.getText().toString()), edittextPurgeC1);
            CommonUtils.animateTextView(0, Integer.parseInt(edittextPurgeC2.getText().toString()), edittextPurgeC2);
            CommonUtils.animateTextView(0, Integer.parseInt(edittextPurgeC3.getText().toString()), edittextPurgeC3);

            // textTotalNoteCountPurge.setText(TextUtils.isEmpty(purgeBinDto.getPurgeTotalNote()) ? "0" : purgeBinDto.getPurgeTotalNote());
            //textTotalAmountCountPurge.setText(TextUtils.isEmpty(purgeBinDto.getPurgeTotalAmount()) ? "0" : purgeBinDto.getPurgeTotalAmount());

        } catch (Exception e) {
            e.getMessage();
        }
    }


    public void reload(String getPhysicalLoadingDetails, TextView textviewCashPerReceipt, LinearLayout layoutLoadingDate, LinearLayout layoutFooterButtonSubmit) {
        System.out.println(getPhysicalLoadingDetails);
        if (TextUtils.isEmpty(getPhysicalLoadingDetails)) {
            return;
        }
        bindData(getPhysicalLoadingDetails, textviewCashPerReceipt, layoutLoadingDate, layoutFooterButtonSubmit);

    }

    private void bindData(String getPhysicalLoadingDetails, TextView textviewCashPerReceipt, LinearLayout layoutLoadingDate, LinearLayout layoutFooterButtonSubmit) {
        try {

            JSONObject jsonObject = new JSONObject(getPhysicalLoadingDetails);
            JSONArray jsonArrayatmLoadingDetails = jsonObject.getJSONArray("atmLoading");
            JSONArray jsonArrayreceiptCountNumber = jsonObject.getJSONArray("receiptCount");
            String receiptAmount = jsonArrayreceiptCountNumber.getJSONObject(0).getString("amount");
            //set amount
            textviewCashPerReceipt.setText(TextUtils.isEmpty(receiptAmount) ? "0" : receiptAmount);
            //set loading details
            Gson gson = new GsonBuilder().create();
            loadingDetailsDto = gson.fromJson(jsonArrayatmLoadingDetails.toString(), new TypeToken<ArrayList<ATMRoCashLoadingDetailsDto>>() {
            }.getType());
            edittextPhyscialC1.setText(TextUtils.isEmpty(loadingDetailsDto.get(0).getOpeningCassette1()) ? "0" : loadingDetailsDto.get(0).getOpeningCassette1());
            edittextPhyscialC2.setText(TextUtils.isEmpty(loadingDetailsDto.get(0).getOpeningCassette2()) ? "0" : loadingDetailsDto.get(0).getOpeningCassette2());
            edittextPhyscialC3.setText(TextUtils.isEmpty(loadingDetailsDto.get(0).getOpeningCassette3()) ? "0" : loadingDetailsDto.get(0).getOpeningCassette3());
            edittextPhyscialC4.setText(TextUtils.isEmpty(loadingDetailsDto.get(0).getOpeningCassette4()) ? "0" : loadingDetailsDto.get(0).getOpeningCassette4());

            //purge bin
            String purgeBinC3 = TextUtils.isEmpty(loadingDetailsDto.get(0).getPurgeCassette3()) ? "0" : loadingDetailsDto.get(0).getPurgeCassette3();
            String purgeBinC4 = TextUtils.isEmpty(loadingDetailsDto.get(0).getPurgeCassette4()) ? "0" : loadingDetailsDto.get(0).getPurgeCassette4();
            int totalC3C4 = Integer.parseInt(purgeBinC3) + Integer.parseInt(purgeBinC4);

            edittextPurgeC1.setText(TextUtils.isEmpty(loadingDetailsDto.get(0).getPurgeCassette1()) ? "0" : loadingDetailsDto.get(0).getPurgeCassette1());
            edittextPurgeC2.setText(TextUtils.isEmpty(loadingDetailsDto.get(0).getPurgeCassette2()) ? "0" : loadingDetailsDto.get(0).getPurgeCassette2());
            edittextPurgeC3.setText(String.valueOf(totalC3C4));

            //set date
            //submit button visible or not
            if (loadingDetailsDto.get(0).getAllowEditing().equalsIgnoreCase("E")) {
                layoutFooterButtonSubmit.setVisibility(View.VISIBLE);
                GUIUtils.setViewAndChildrenEnabled(layoutLoadingDate, true);
                GUIUtils.setViewAndChildrenEnabled(layoutParent, true);
            } else {
                layoutFooterButtonSubmit.setVisibility(View.GONE);
                GUIUtils.setViewAndChildrenEnabled(layoutLoadingDate, false);
                GUIUtils.setViewAndChildrenEnabled(layoutParent, false);
            }

            CommonUtils.animateTextView(0, Integer.parseInt(edittextPhyscialC1.getText().toString()), edittextPhyscialC1);
            CommonUtils.animateTextView(0, Integer.parseInt(edittextPhyscialC2.getText().toString()), edittextPhyscialC2);
            CommonUtils.animateTextView(0, Integer.parseInt(edittextPhyscialC3.getText().toString()), edittextPhyscialC3);
            CommonUtils.animateTextView(0, Integer.parseInt(edittextPhyscialC4.getText().toString()), edittextPhyscialC4);

            CommonUtils.animateTextView(0, Integer.parseInt(edittextPurgeC1.getText().toString()), edittextPurgeC1);
            CommonUtils.animateTextView(0, Integer.parseInt(edittextPurgeC2.getText().toString()), edittextPurgeC2);
            CommonUtils.animateTextView(0, Integer.parseInt(edittextPurgeC3.getText().toString()), edittextPurgeC3);

            // textTotalNoteCountPurge.setText(TextUtils.isEmpty(purgeBinDto.getPurgeTotalNote()) ? "0" : purgeBinDto.getPurgeTotalNote());
            //textTotalAmountCountPurge.setText(TextUtils.isEmpty(purgeBinDto.getPurgeTotalAmount()) ? "0" : purgeBinDto.getPurgeTotalAmount());

        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public void datePickerClick(String date) {
        this.loadingDate = date;
    }
}
