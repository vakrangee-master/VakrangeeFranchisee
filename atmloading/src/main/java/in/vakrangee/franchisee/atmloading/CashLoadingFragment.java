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
public class CashLoadingFragment extends Fragment {
    private View view;

    //cash loading
    //private CashLoadingDto cashLoadingDto;
    private AsyncSaveATMLoadingDetails asyncSaveATMLoadingDetails;
    private AsyncGetATMPhysicalORCashLoadingDetails asyncGetATMPhysicalORCashLoadingDetails;
    private Context context;
    private String imgBase64;
    private Uri picUri;
    private Bitmap bitmap = null;
    private static final int CAMERA_PIC_REQUEST = 1;
    private CustomImageZoomDialog customImagePreviewDialog;
    private PermissionHandler permissionHandler;
    private String mCurrentPhotoPath;
    private GPSTracker gpsTracker;
    private String latitude = "", longitude = "", currentTimestamp = "";


    private String receiptAmount = "0";
    private List<ATMRoCashLoadingDetailsDto> loadingDetailsDto;
    private ATMRoCashLoadingDetailsDto atmRoCashLoadingDetailsDto;
    private CustomATMLoadinDialog customATMLoadinDialog;
    private EditText edittextCashLoadC1;
    private EditText edittextCashLoadC2;
    private EditText edittextCashLoadC3;
    private EditText edittextCashLoadC4;
    private TextView textCashLoadC1;
    private TextView textCashLoadC2;
    private TextView textCashLoadC3;
    private TextView textCashLoadC4;
    private TextView textTotalNoteCountCashLoad;
    private TextView textTotalAmountCountCashLoad;
    private TextView textViewDiffAmounnt;
    private LinearLayout layoutParent;
    private ImageView imageViewCaptureCashLoading;
    private Button btnSubmit;

    public CashLoadingFragment() {
        // Required empty public constructor
    }


    // newInstance constructor for creating fragment with arguments
    public static CashLoadingFragment newInstance() {
        CashLoadingFragment fragmentFirst = new CashLoadingFragment();
        Bundle args = new Bundle();
        //args.putInt("someInt", page);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cash_loading, container, false);
        bindViewId(view);
        ButterKnife.bind(this, view);
        this.context = getContext();
        permissionHandler = new PermissionHandler(getActivity());
        gpsTracker = new GPSTracker(context);
        //cashLoadingDto = new CashLoadingDto();
        atmRoCashLoadingDetailsDto = new ATMRoCashLoadingDetailsDto();
        //----------physcial load
        editTextAmountEnter(edittextCashLoadC1, textCashLoadC1, Long.valueOf(2000));
        editTextAmountEnter(edittextCashLoadC2, textCashLoadC2, Long.valueOf(500));
        editTextAmountEnter(edittextCashLoadC3, textCashLoadC3, Long.valueOf(100));
        editTextAmountEnter(edittextCashLoadC4, textCashLoadC4, Long.valueOf(100));


        edittextCashLoadC1.setFilters(new InputFilter[]{new CommonUtils.InputFilterMinMax("1", "2500")});
        edittextCashLoadC2.setFilters(new InputFilter[]{new CommonUtils.InputFilterMinMax("1", "2500")});
        edittextCashLoadC3.setFilters(new InputFilter[]{new CommonUtils.InputFilterMinMax("1", "2500")});
        edittextCashLoadC4.setFilters(new InputFilter[]{new CommonUtils.InputFilterMinMax("1", "2500")});

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openConfirmationDialog(view);
            }
        });
        imageViewCaptureCashLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureCashLoadingImage(view);
            }
        });
        return view;
    }

    private void bindViewId(View view) {
        //Physcial Cash
        edittextCashLoadC1 = view.findViewById(R.id.edittextCashLoadC1);
        edittextCashLoadC2 = view.findViewById(R.id.edittextCashLoadC2);
        edittextCashLoadC3 = view.findViewById(R.id.edittextCashLoadC3);
        edittextCashLoadC4 = view.findViewById(R.id.edittextCashLoadC4);
        textCashLoadC1 = view.findViewById(R.id.textCashLoadC1);
        textCashLoadC2 = view.findViewById(R.id.textCashLoadC2);
        textCashLoadC3 = view.findViewById(R.id.textCashLoadC3);
        textCashLoadC4 = view.findViewById(R.id.textCashLoadC4);
        textTotalNoteCountCashLoad = view.findViewById(R.id.textTotalNoteCountCashLoad);
        textTotalAmountCountCashLoad = view.findViewById(R.id.textTotalAmountCountCashLoad);
        textViewDiffAmounnt = view.findViewById(R.id.textViewDiffAmounnt);
        layoutParent = view.findViewById(R.id.layoutParent);
        imageViewCaptureCashLoading = view.findViewById(R.id.imageViewCaptureCashLoading);
        btnSubmit = view.findViewById(R.id.btnSubmit);

    }

    private void openConfirmationDialog(View view) {
        int valid = validationPhysicalDto();
                /*if (valid == 0) {
                    //show dialog
                    showATMLoadingDialog((Object) atmRoCashLoadingDetailsDto);
                }*/

        showATMLoadingDialog((Object) atmRoCashLoadingDetailsDto);
    }


    private void showATMLoadingDialog(Object object) {
        if (object != null) {
            customATMLoadinDialog = new CustomATMLoadinDialog(context, object, "2", new CustomATMLoadinDialog.IfcATMLoadingDialogClicks() {
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
            customATMLoadinDialog.setDialogTitle("Cash Loading Preview");

        } else {
            Toast.makeText(context, "No cash loading available to preview.", Toast.LENGTH_SHORT).show();
        }
    }

    private int validationPhysicalDto() {
        int valid = 0;
        atmRoCashLoadingDetailsDto.setLoadCassette1(edittextCashLoadC1.getText().toString());
        atmRoCashLoadingDetailsDto.setLoadCassette2(edittextCashLoadC2.getText().toString());
        atmRoCashLoadingDetailsDto.setLoadCassette3(edittextCashLoadC3.getText().toString());
        atmRoCashLoadingDetailsDto.setLoadCassette4(edittextCashLoadC4.getText().toString());


        atmRoCashLoadingDetailsDto.setTotalCashNote(textTotalNoteCountCashLoad.getText().toString());
        atmRoCashLoadingDetailsDto.setTotalCashAmount(textTotalAmountCountCashLoad.getText().toString());

        if (TextUtils.isEmpty(atmRoCashLoadingDetailsDto.getLoadCassette1())) {
            Toast.makeText(context, "Please enter note", Toast.LENGTH_LONG).show();
            edittextCashLoadC1.setError("Please enter note.");
            return 1;
        }
        if (TextUtils.isEmpty(atmRoCashLoadingDetailsDto.getLoadCassette2())) {
            Toast.makeText(context, "Please enter note", Toast.LENGTH_LONG).show();
            edittextCashLoadC2.setError("Please enter note.");
            return 2;
        }
        if (TextUtils.isEmpty(atmRoCashLoadingDetailsDto.getLoadCassette3())) {
            Toast.makeText(context, "Please enter note", Toast.LENGTH_LONG).show();
            edittextCashLoadC3.setError("Please enter note.");
            return 3;
        }
        if (TextUtils.isEmpty(atmRoCashLoadingDetailsDto.getLoadCassette4())) {
            Toast.makeText(context, "Please enter note", Toast.LENGTH_LONG).show();
            edittextCashLoadC4.setError("Please enter note.");
            return 4;
        }

        if (TextUtils.isEmpty(atmRoCashLoadingDetailsDto.getSetPhysicalImage())) {
            Toast.makeText(context, "Please Capture Physical Cash Loading Image", Toast.LENGTH_LONG).show();
            return 5;
        }

        return valid;
    }


    private void captureCashLoadingImage(View view) {
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

                    atmRoCashLoadingDetailsDto.setExt(FileUtils.getFileExtension(context, picUri));
                    atmRoCashLoadingDetailsDto.setId(null);

                    atmRoCashLoadingDetailsDto.setUri(imageUri);
                    atmRoCashLoadingDetailsDto.setBitmap(bitmapNew);
                    atmRoCashLoadingDetailsDto.setChangedPhoto(true);
                    atmRoCashLoadingDetailsDto.setSetPhysicalImage(imageBase64);
                    //loadingDetailsDto.get(0).setChangedPhoto(true);
                    showImagePreviewDialog((Object) atmRoCashLoadingDetailsDto);
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
                            imageViewCaptureCashLoading.setImageBitmap(bitmap);
                    }
                }
            });
            customImagePreviewDialog.show();
            customImagePreviewDialog.setCancelable(false);
            customImagePreviewDialog.setDialogTitle("Cash Loading Image");
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
            //phyiscal cash - pc
            String cL1 = edittextCashLoadC1.getText().toString().equals("") ? "0" : edittextCashLoadC1.getText().toString();
            String cL2 = edittextCashLoadC2.getText().toString().equals("") ? "0" : edittextCashLoadC2.getText().toString();
            String cL3 = edittextCashLoadC3.getText().toString().equals("") ? "0" : edittextCashLoadC3.getText().toString();
            String cL4 = edittextCashLoadC4.getText().toString().equals("") ? "0" : edittextCashLoadC4.getText().toString();
            String totalCountCL = textTotalNoteCountCashLoad.getText().toString().equals("") ? "0" : textTotalNoteCountCashLoad.getText().toString();
            String totalAmountCl = textTotalAmountCountCashLoad.getText().toString().equals("") ? "0" : textTotalAmountCountCashLoad.getText().toString();
            totalAmountCl = totalAmountCl.replace(" ₹", "");
            if (loadingDetailsDto != null) {

                loadingDetailsDto.get(0).setLoadCassette1(cL1);
                loadingDetailsDto.get(0).setLoadCassette2(cL2);
                loadingDetailsDto.get(0).setLoadCassette3(cL3);
                loadingDetailsDto.get(0).setLoadCassette4(cL4);

                Gson gson = new GsonBuilder().create();
                jsonData = gson.toJson(loadingDetailsDto, new TypeToken<ArrayList<ATMRoCashLoadingDetailsDto>>() {
                }.getType());
            } else {

                atmRoCashLoadingDetailsDto = new ATMRoCashLoadingDetailsDto();
                atmRoCashLoadingDetailsDto.setLoadCassette1(cL1);
                atmRoCashLoadingDetailsDto.setLoadCassette2(cL2);
                atmRoCashLoadingDetailsDto.setLoadCassette3(cL3);
                atmRoCashLoadingDetailsDto.setLoadCassette4(cL4);
                Gson gson = new Gson();
                jsonData = gson.toJson(loadingDetailsDto, ATMRoCashLoadingDetailsDto.class);
            }

            return jsonData;
            //save data
            //callAsyntask(jsonData);
        } catch (Exception e) {
            e.getMessage();
        }

        return jsonData;
    }

    private void callAsyntask(String jsonData) {
        asyncSaveATMLoadingDetails = new AsyncSaveATMLoadingDetails(context, Constants.CASH_LOADING_TYPE, jsonData, new AsyncSaveATMLoadingDetails.Callback() {
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

                CommonUtils.totalPhyscialNoteCount(textTotalNoteCountCashLoad, edittextCashLoadC1, edittextCashLoadC2, edittextCashLoadC3,
                        edittextCashLoadC4, textTotalAmountCountCashLoad);

                //textViewDiffAmounnt.setText(CommonUtils.differenceVal(receiptAmount, textTotalAmountCountCashLoad));

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    String e = enterNote.getText().toString();
                    Long cst = Long.valueOf(e);
                    cst = cst * note;

                    String moneyString = new DecimalFormat("##,##,##0").format(cst);
                    //amount.setText(moneyString + " ₹");
                    amount.setText(moneyString);

                    //amount.setText(cst.toString() + " ₹");
                } else {
                    //amount.setText("0 ₹");
                    amount.setText("0");
                }
            }
        });
    }


    public void reload(String getPhysicalLoadingDetails, String cashAsPerReceipt) {
        this.receiptAmount = cashAsPerReceipt;
        if (TextUtils.isEmpty(getPhysicalLoadingDetails)) {
            return;
        }
        //set data in view
        bindData(getPhysicalLoadingDetails);
    }

    //region set data from service
    private void bindData(String getPhysicalLoadingDetails) {
        try {

            JSONObject jsonObject = new JSONObject(getPhysicalLoadingDetails);
            JSONArray jsonArrayatmLoadingDetails = jsonObject.getJSONArray("atmLoading");
            JSONArray jsonArrayreceiptCountNumber = jsonObject.getJSONArray("receiptCount");
            receiptAmount = jsonArrayreceiptCountNumber.getJSONObject(0).getString("amount");
            //set amount

            Gson gson = new GsonBuilder().create();
            loadingDetailsDto = gson.fromJson(jsonArrayatmLoadingDetails.toString(), new TypeToken<ArrayList<ATMRoCashLoadingDetailsDto>>() {
            }.getType());

            edittextCashLoadC1.setText(TextUtils.isEmpty(loadingDetailsDto.get(0).getLoadCassette1()) ? "0" : loadingDetailsDto.get(0).getLoadCassette1());
            edittextCashLoadC2.setText(TextUtils.isEmpty(loadingDetailsDto.get(0).getLoadCassette2()) ? "0" : loadingDetailsDto.get(0).getLoadCassette2());
            edittextCashLoadC3.setText(TextUtils.isEmpty(loadingDetailsDto.get(0).getLoadCassette3()) ? "0" : loadingDetailsDto.get(0).getLoadCassette3());
            edittextCashLoadC4.setText(TextUtils.isEmpty(loadingDetailsDto.get(0).getLoadCassette4()) ? "0" : loadingDetailsDto.get(0).getLoadCassette4());

            if (loadingDetailsDto.get(0).getAllowEditing().equalsIgnoreCase("E")) {
                GUIUtils.setViewAndChildrenEnabled(layoutParent, true);
            } else {
                GUIUtils.setViewAndChildrenEnabled(layoutParent, false);
            }

            CommonUtils.animateTextView(0, Integer.parseInt(edittextCashLoadC1.getText().toString()), edittextCashLoadC1);
            CommonUtils.animateTextView(0, Integer.parseInt(edittextCashLoadC2.getText().toString()), edittextCashLoadC2);
            CommonUtils.animateTextView(0, Integer.parseInt(edittextCashLoadC3.getText().toString()), edittextCashLoadC3);
            CommonUtils.animateTextView(0, Integer.parseInt(edittextCashLoadC4.getText().toString()), edittextCashLoadC4);
        } catch (Exception e) {
            e.getMessage();
        }


    }
}
