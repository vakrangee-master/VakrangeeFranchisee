package in.vakrangee.franchisee.payment_details;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import in.vakrangee.franchisee.BuildConfig;
import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.baseutils.BaseAppCompatActivity;
import in.vakrangee.supercore.franchisee.commongui.DateTimePickerDialog;
import in.vakrangee.supercore.franchisee.model.PhotoDto;
import in.vakrangee.supercore.franchisee.nextgenfranchiseeapplication.CustomFranchiseeApplicationSpinnerDto;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.GPSTracker;
import in.vakrangee.supercore.franchisee.utils.GUIUtils;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;
import in.vakrangee.supercore.franchisee.utils.CustomSearchableSpinner;

public class RaiseDisputeActivity extends BaseAppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Toolbar toolbarRaiseDispute;
    private LinearLayout layoutRaiseDisputeParent;
    private TextView textViewDate;
    private CustomSearchableSpinner spinnerBankName, spinnerTransferMode;
    private EditText editTextIFSCCode1, editTextAccountHolderName, editTextAccountNumber1, editTextUTR, editTextBankTransactionID, editTextAmount, editTextRemarks;
    private TextView txtDateLbl, txtBankNameLbl, txtIFSCCodeLbl, txtAccountHolderNameLbl, txtAccountNumberLbl, txtTransferModeLbl, txtUTRLbl, txtBankTransactionIDLbl, txtAmountLbl, txtProofImageLbl;
    private ImageView imgProofImage;
    private MaterialButton btnSubmit;
    private Context context;
    private List<CustomFranchiseeApplicationSpinnerDto> bankNameList, transferModeList;
    private PaymentDetailsRepository paymentDetailsRepo;
    private RaiseDisputeDto raiseDisputeDto;
    private GetAllSpinnerData getAllSpinnerData = null;
    private boolean IsEditable = false;
    private AsyncPostRaiseDisputeDetails asyncPostRaiseDisputeDetails = null;
    private static final String ENTER_VALID_CONST = "Please enter valid Amount.";
    private static final String ENTER_ACCOUNT_NO_CONST = "Please enter valid Account Number.";

    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alert;
    private Date selectedDate;
    private String strSelectedDate;
    private int selectedDateTimeId = 0;
    private DateTimePickerDialog dateTimePickerDialog;
    private static final String COLOR_FF0000_CONST = "#FF0000";
    private static final String COLOR_468847_CONST = "#468847";
    private static final String YYYY_MM_DD_CONST = "yyyy-MM-dd";
    private DateFormat dateFormatterYMD = new SimpleDateFormat(YYYY_MM_DD_CONST, Locale.US);

    private PermissionHandler permissionHandler;
    private Uri picUri;
    private String mCurrentPhotoPath;
    private String latitude = "", longitude = "", currentTimestamp = "";
    private GPSTracker gpsTracker;
    private String selectedImageType;
    private static final String PROOF_IMAGE = "Proof Image Preview";
    private PhotoDto selProofDto;
    private CustomAllImagePreviewDialog customImagePreviewDialog;
    private static final int CAMERA_PIC_REQUEST = 100;
    private static final String EXT_JPG = "jpg";
    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raise_dispute);

        //Context
        context = this;
        paymentDetailsRepo = new PaymentDetailsRepository(context);
        permissionHandler = new PermissionHandler(RaiseDisputeActivity.this);
        gpsTracker = new GPSTracker(context);

        //set toolbar name
        toolbarRaiseDispute = findViewById(R.id.toolbarRaiseDispute);
        setSupportActionBar(toolbarRaiseDispute);
        if (getSupportActionBar() != null) {
            toolbarRaiseDispute.setTitleTextColor(Color.WHITE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + getResources().getString(R.string.payment_details) + "</small>"));

        }

        //Widget
        layoutRaiseDisputeParent = findViewById(R.id.layoutRaiseDisputeParent);
        textViewDate = findViewById(R.id.textViewDate);
        spinnerBankName = findViewById(R.id.spinnerBankName);
        editTextIFSCCode1 = findViewById(R.id.editTextIFSCCode1);
        editTextAccountHolderName = findViewById(R.id.editTextAccountHolderName);
        editTextAccountNumber1 = findViewById(R.id.editTextAccountNumber1);
        spinnerTransferMode = findViewById(R.id.spinnerTransferMode);
        editTextUTR = findViewById(R.id.editTextUTR);
        editTextBankTransactionID = findViewById(R.id.editTextBankTransactionID);
        editTextAmount = findViewById(R.id.editTextAmount);
        imgProofImage = findViewById(R.id.imgProofImage);
        editTextRemarks = findViewById(R.id.editTextRemarks);
        btnSubmit = findViewById(R.id.btnSubmit);

        //Labels
        txtDateLbl = findViewById(R.id.txtDateLbl);
        txtBankNameLbl = findViewById(R.id.txtBankNameLbl);
        txtIFSCCodeLbl = findViewById(R.id.txtIFSCCodeLbl);
        txtAccountHolderNameLbl = findViewById(R.id.txtAccountHolderNameLbl);
        txtAccountNumberLbl = findViewById(R.id.txtAccountNumberLbl);
        txtTransferModeLbl = findViewById(R.id.txtTransferModeLbl);
        txtUTRLbl = findViewById(R.id.txtUTRLbl);
        txtBankTransactionIDLbl = findViewById(R.id.txtBankTransactionIDLbl);
        txtAmountLbl = findViewById(R.id.txtAmountLbl);
        txtProofImageLbl = findViewById(R.id.txtProofImageLbl);

        //Set Compulsory mark
        TextView[] txtViewsForCompulsoryMark = {txtDateLbl, txtBankNameLbl, txtIFSCCodeLbl, txtAccountHolderNameLbl, txtAccountNumberLbl,
                txtTransferModeLbl, txtUTRLbl, txtBankTransactionIDLbl, txtAmountLbl, txtProofImageLbl};
        GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);

        //Bank Related Validation
        applyBankRelatedValidation();

        CommonUtils.InputFiletrWithMaxLength(editTextAccountHolderName, "~#^|$%&*!", 50);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRaiseDisputeDetails();
            }
        });

        textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDateTimeId = v.getId();
                showDateTimeDialogPicker();
            }
        });

        imgProofImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedImageType = PROOF_IMAGE;

                if (TextUtils.isEmpty(raiseDisputeDto.getProofPicId()) && TextUtils.isEmpty(raiseDisputeDto.getProofPicBase64())) {
                    startCamera(v);
                } else {
                    Bitmap bitmap = CommonUtils.StringToBitMap(raiseDisputeDto.getProofPicBase64());
                    displayPreviewDialog(raiseDisputeDto.getProofPicBase64(), bitmap);
                }
            }
        });

        reloadData();
    }

    //region startCamera
    public void startCamera(View view) {
        //If the app has not the permission then asking for the permission
        permissionHandler.requestMultiplePermission(view, permissions, getString(R.string.needs_camera_storage_permission_msg), new IPermission() {
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

    //region dispatch Take Picture Intent
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
                picUri = FileProvider.getUriForFile(context.getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
                startActivityForResult(takePictureIntent, CAMERA_PIC_REQUEST);
            }
        }
    }
    //endregion

    //region Create Image File
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
                    displayPreviewDialog(imageBase64, bitmapNew);
                }

            } catch (Exception e) {
                e.printStackTrace();
                AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
            }
        }
    }

    private void displayPreviewDialog(String imageBase64, Bitmap bitmapNew) {

        PhotoDto imageDto = null;
        if (selectedImageType.equalsIgnoreCase(PROOF_IMAGE)) {

            selProofDto.setImageBase64(imageBase64);
            selProofDto.setBitmap(bitmapNew);
            selProofDto.setPhotoId(raiseDisputeDto.getProofPicId());
            imageDto = selProofDto;
        }
        showImagePreviewDialog((Object) imageDto);
    }

    //region showImagePreviewDialog
    private void showImagePreviewDialog(Object object) {

        if (customImagePreviewDialog != null && customImagePreviewDialog.isShowing()) {
            customImagePreviewDialog.refresh(object);
            return;
        }

        if (object != null) {
            customImagePreviewDialog = new CustomAllImagePreviewDialog(context, object, new CustomAllImagePreviewDialog.IImagePreviewDialogClicks() {
                @Override
                public void capturePhotoClick() {
                    if (selectedImageType.equalsIgnoreCase(PROOF_IMAGE)) {
                        selProofDto.setChangedPhoto(true);
                    }

                    startCamera(imgProofImage);
                }

                @Override
                public void OkClick(Object object) {
                    //save data
                    PhotoDto photoDto = ((PhotoDto) object);
                    String base64 = photoDto.getImageBase64();
                    Bitmap bitmap = photoDto.getBitmap();

                    if (selectedImageType.equalsIgnoreCase(PROOF_IMAGE)) {
                        if (base64 != null || bitmap != null) {
                            raiseDisputeDto.setProofPicBase64(base64);
                            raiseDisputeDto.setProofExt(EXT_JPG);
                            imgProofImage.setImageBitmap(bitmap);
                        }
                    }
                }
            });

            customImagePreviewDialog.show();
            customImagePreviewDialog.setCancelable(false);
            customImagePreviewDialog.setDialogTitle(selectedImageType);
            customImagePreviewDialog.allowChangePhoto(true);
            customImagePreviewDialog.allowRemarks(false);

        } else {
            Toast.makeText(context, "No photo available to preview.", Toast.LENGTH_SHORT).show();
        }
    }

    //endregion

    public void getLocationAndTimestamp() {
        //Get Current location and time stamp
        if (gpsTracker.canGetLocation()) {
            latitude = String.valueOf(gpsTracker.getLatitude());
            longitude = String.valueOf(gpsTracker.getLongitude());
            currentTimestamp = gpsTracker.getFormattedDateTime();
        }
    }

    private void reloadData() {
        getAllSpinnerData = new GetAllSpinnerData();
        getAllSpinnerData.execute("");
    }

    private void setTextListenerToIFSCCode() {
        editTextIFSCCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Do Nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int len = editTextIFSCCode1.getText().toString().trim().length();
                if (len <= 0)
                    return;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int len = editTextIFSCCode1.getText().toString().trim().length();
                if (len <= 0)
                    return;

                if (editTextIFSCCode1.getText().toString().contains(" ")) {
                    editTextIFSCCode1.setText(editTextIFSCCode1.getText().toString().replaceAll(" ", ""));
                    editTextIFSCCode1.setSelection(editTextIFSCCode1.getText().length());
                }

                String s = editTextIFSCCode1.getText().toString().trim();
                CommonUtils.setWordsCaps(s, editTextIFSCCode1);

                if (!CommonUtils.IsIFSCCodeValid(String.valueOf(editable))) {
                    editTextIFSCCode1.setTextColor(Color.parseColor(COLOR_FF0000_CONST));
                    editTextIFSCCode1.setError("Please enter a valid IFSC Code.");
                } else {
                    editTextIFSCCode1.setTextColor(Color.parseColor(COLOR_468847_CONST));
                    editTextIFSCCode1.setError(null);

                }
            }
        });

    }

    private void setTextChangeListenerToAccountNumber() {
        editTextAccountNumber1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Do Nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int len = editTextAccountNumber1.getText().toString().trim().length();
                if (len <= 0)
                    return;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int len = editTextAccountNumber1.getText().toString().trim().length();
                if (len <= 0)
                    return;

                if (editTextAccountNumber1.getText().toString().contains(" ")) {
                    editTextAccountNumber1.setText(editTextAccountNumber1.getText().toString().replaceAll(" ", ""));
                    editTextAccountNumber1.setSelection(editTextAccountNumber1.getText().length());
                }

                String s = editTextAccountNumber1.getText().toString().trim();
                CommonUtils.setWordsCaps(s, editTextAccountNumber1);

                if (editTextAccountNumber1.length() < 10) {
                    editTextAccountNumber1.setTextColor(Color.parseColor(COLOR_FF0000_CONST));
                    editTextAccountNumber1.setError("Account number must be 10 characters long.");
                } else {
                    editTextAccountNumber1.setTextColor(Color.parseColor(COLOR_468847_CONST));
                    editTextAccountNumber1.setError(null);

                }
            }
        });
    }

    private void setTextChangeListenerToUTR() {
        editTextUTR.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Do Nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int len = editTextUTR.getText().toString().trim().length();
                if (len <= 0)
                    return;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int len = editTextUTR.getText().toString().trim().length();
                if (len <= 0)
                    return;

                if (editTextUTR.getText().toString().contains(" ")) {
                    editTextUTR.setText(editTextUTR.getText().toString().replaceAll(" ", ""));
                    editTextUTR.setSelection(editTextUTR.getText().length());
                }

                String s = editTextUTR.getText().toString().trim();
                CommonUtils.setWordsCaps(s, editTextUTR);

                editTextUTR.setTextColor(Color.parseColor(COLOR_468847_CONST));
                editTextUTR.setError(null);

            }
        });
    }

    private void setTextChangeListenerToRRN() {
        editTextBankTransactionID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Do Nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int len = editTextBankTransactionID.getText().toString().trim().length();
                if (len <= 0)
                    return;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int len = editTextBankTransactionID.getText().toString().trim().length();
                if (len <= 0)
                    return;

                if (editTextBankTransactionID.getText().toString().contains(" ")) {
                    editTextBankTransactionID.setText(editTextBankTransactionID.getText().toString().replaceAll(" ", ""));
                    editTextBankTransactionID.setSelection(editTextBankTransactionID.getText().length());
                }

                String s = editTextBankTransactionID.getText().toString().trim();
                CommonUtils.setWordsCaps(s, editTextBankTransactionID);

                editTextBankTransactionID.setTextColor(Color.parseColor(COLOR_468847_CONST));
                editTextBankTransactionID.setError(null);

            }
        });

    }

    private void applyBankRelatedValidation() {
        CommonUtils.InputFiletrWithMaxLength(editTextIFSCCode1, "~#^|$%&*!", 11);

        //IFSC Code
        setTextListenerToIFSCCode();

        //Account Number
        setTextChangeListenerToAccountNumber();

        //UTR
        setTextChangeListenerToUTR();

        //RRN
        setTextChangeListenerToRRN();

        //Amount
        editTextAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Do Nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int len = editTextAmount.getText().toString().trim().length();
                if (len <= 0)
                    return;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int len = editTextAmount.getText().toString().trim().length();
                if (len <= 0)
                    return;

                if (editTextAmount.getText().toString().contains(" ")) {
                    editTextAmount.setText(editTextAmount.getText().toString().replaceAll(" ", ""));
                    editTextAmount.setSelection(editTextAmount.getText().length());
                }

                String s = editTextAmount.getText().toString().trim();
                CommonUtils.setWordsCaps(s, editTextAmount);

                if (!CommonUtils.isValidAmount(String.valueOf(editable))) {
                    editTextAmount.setTextColor(Color.parseColor(COLOR_FF0000_CONST));
                    editTextAmount.setError(ENTER_VALID_CONST);
                } else {
                    editTextAmount.setTextColor(Color.parseColor(COLOR_468847_CONST));
                    editTextAmount.setError(null);

                }
            }
        });
    }

    class GetAllSpinnerData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressSpinner(context);
        }

        @Override
        protected String doInBackground(String... strings) {

            /*Connection connection = new Connection(context);
            String tmpVkId = connection.getVkid();
            String enquiryId = CommonUtils.getEnquiryId(context);
            String vkIdOrEnquiryId = TextUtils.isEmpty(enquiryId) ? tmpVkId : enquiryId;

            //STEP 1: Get Existing Dispute Data
            raiseDisputeDto = paymentDetailsRepo.getExistingRaiseDisputeDetail(vkIdOrEnquiryId);*/

            //STEP 2: Bank Name
            bankNameList = paymentDetailsRepo.getBankNameList();

            //STEP 3: Transfer Mode
            transferModeList = paymentDetailsRepo.getTransferModeList();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissProgressSpinner();
            if (raiseDisputeDto == null) {
                raiseDisputeDto = new RaiseDisputeDto();
            }

            IsEditable = true;     //(!TextUtils.isEmpty(raiseDisputeDto.getIsEditable()) && raiseDisputeDto.getIsEditable().equalsIgnoreCase("1")) ? true : false;

            bindSpinner();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long Id) {
        int id = parent.getId();

        if (id == R.id.spinnerBankName && position >= 0) {
            CustomFranchiseeApplicationSpinnerDto dto = (CustomFranchiseeApplicationSpinnerDto) spinnerBankName.getItemAtPosition(position);
            raiseDisputeDto.setBankId(dto.getId());
            raiseDisputeDto.setBankName(dto.getName());

        } else if (id == R.id.spinnerTransferMode && position >= 0) {
            CustomFranchiseeApplicationSpinnerDto dto = (CustomFranchiseeApplicationSpinnerDto) spinnerTransferMode.getItemAtPosition(position);
            raiseDisputeDto.setTransferModeId(dto.getId());
            raiseDisputeDto.setTransferMode(dto.getName());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //DO Nothing
    }

    private void bindSpinner() {

        //STEP 1: Payment Date
        if (!TextUtils.isEmpty(raiseDisputeDto.getPaymentDate())) {
            String rDate = CommonUtils.getFormattedDate(YYYY_MM_DD_CONST, "dd MMM yyyy", raiseDisputeDto.getPaymentDate());
            textViewDate.setText(rDate);
        }

        //STEP 2: Bank Name
        spinnerBankName.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, bankNameList));
        int bankNamePos = paymentDetailsRepo.getSelectedPos(bankNameList, raiseDisputeDto.getBankId());
        spinnerBankName.setSelection(bankNamePos);
        spinnerBankName.setOnItemSelectedListener(this);

        //STEP 3: IFSC Code
        editTextIFSCCode1.setText(raiseDisputeDto.getIfscCode());

        //STEP 4: Account Holder Name
        editTextAccountHolderName.setText(raiseDisputeDto.getAccountHolderName());

        //STEP 5: Account Number
        editTextAccountNumber1.setText(raiseDisputeDto.getAccountNumber());

        //STEP 6: Transfer Mode
        spinnerTransferMode.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, transferModeList));
        int transferModePos = paymentDetailsRepo.getSelectedPos(transferModeList, raiseDisputeDto.getTransferModeId());
        spinnerTransferMode.setSelection(transferModePos);
        spinnerTransferMode.setOnItemSelectedListener(this);

        //STEP 7: UTR
        editTextUTR.setText(raiseDisputeDto.getUtr());

        //STEP 8: RRN
        editTextBankTransactionID.setText(raiseDisputeDto.getRrn());

        //STEP 9: Amount
        editTextAmount.setText(raiseDisputeDto.getAmount());

        //STEP 10: Proof Photo
        selProofDto = new PhotoDto();
        boolean IsPDF = ((raiseDisputeDto.getProofExt() != null) && raiseDisputeDto.getProofExt().equalsIgnoreCase("pdf")) ? true : false;
        if (IsPDF) {
            Glide.with(context).asDrawable().load(in.vakrangee.franchisee.hardwareacknowledgement.R.drawable.pdf).into(imgProofImage);
        } else {
            if (!TextUtils.isEmpty(raiseDisputeDto.getProofPicId())) {
                String picUrl = Constants.DownloadImageUrl + raiseDisputeDto.getProofPicId();
                Glide.with(context)
                        .load(picUrl)
                        .apply(new RequestOptions()
                                .error(in.vakrangee.franchisee.hardwareacknowledgement.R.drawable.ic_camera_alt_black_72dp)
                                .placeholder(in.vakrangee.franchisee.hardwareacknowledgement.R.drawable.ic_camera_alt_black_72dp)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))

                        .into(imgProofImage);
            }
        }

        //STEP 11: Remarks
        editTextRemarks.setText(raiseDisputeDto.getRemarks());

        //Enable/disable views
        GUIUtils.setViewAndChildrenEnabled(layoutRaiseDisputeParent, IsEditable);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getAllSpinnerData != null && !getAllSpinnerData.isCancelled()) {
            getAllSpinnerData.cancel(true);
        }
    }

    public RaiseDisputeDto getRaiseDisputeDetailsDto() {

        raiseDisputeDto.setIfscCode(editTextIFSCCode1.getText().toString());
        raiseDisputeDto.setAccountHolderName(editTextAccountHolderName.getText().toString());
        raiseDisputeDto.setAccountNumber(editTextAccountNumber1.getText().toString());
        raiseDisputeDto.setRrn(editTextBankTransactionID.getText().toString());
        raiseDisputeDto.setUtr(editTextUTR.getText().toString());
        raiseDisputeDto.setAmount(editTextAmount.getText().toString());
        raiseDisputeDto.setRemarks(editTextRemarks.getText().toString());

        return raiseDisputeDto;
    }

    private int validateUTRAndOtherDetails() {
        //STEP 3: IFSC Code
        if (TextUtils.isEmpty(raiseDisputeDto.getIfscCode()) || !CommonUtils.IsIFSCCodeValid(editTextIFSCCode1.getText().toString().trim())) {
            Toast.makeText(context, "Please enter valid IFSC Code.", Toast.LENGTH_LONG).show();
            editTextIFSCCode1.setError("Please enter valid IFSC Code.");
            return 1;
        }

        //STEP 4: Account Holder Name
        if (TextUtils.isEmpty(raiseDisputeDto.getAccountHolderName())) {
            Toast.makeText(context, "Please enter Account Holder Name.", Toast.LENGTH_LONG).show();
            editTextAccountHolderName.setError("Please enter Account Holder Name.");
            return 1;
        }

        //STEP 5: Account Number
        if (TextUtils.isEmpty(raiseDisputeDto.getAccountNumber()) || editTextAccountNumber1.getText().toString().trim().length() < 10) {
            Toast.makeText(context, ENTER_ACCOUNT_NO_CONST, Toast.LENGTH_LONG).show();
            editTextAccountNumber1.setError(ENTER_ACCOUNT_NO_CONST);
            return 1;
        }

        //STEP 5.1: Complete Zero
        if (CommonUtils.isCompleteZero(raiseDisputeDto.getAccountNumber())) {
            Toast.makeText(context, ENTER_ACCOUNT_NO_CONST, Toast.LENGTH_LONG).show();
            editTextAccountNumber1.setError(ENTER_ACCOUNT_NO_CONST);
            return 1;
        }

        //STEP 6: Transfer Mode
        if (TextUtils.isEmpty(raiseDisputeDto.getTransferModeId()) || raiseDisputeDto.getTransferModeId().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Transfer Mode.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerTransferMode, "Please select Transfer Mode.", context);
            return 1;
        }

        //STEP 7: UTR
        if (TextUtils.isEmpty(raiseDisputeDto.getUtr())) {
            Toast.makeText(context, "Please enter UTR.", Toast.LENGTH_LONG).show();
            editTextUTR.setError("Please enter UTR.");
            return 1;
        }

        //STEP 7.1: Complete Zero
        if (CommonUtils.isCompleteZero(raiseDisputeDto.getUtr())) {
            Toast.makeText(context, "Please enter valid UTR.", Toast.LENGTH_LONG).show();
            editTextUTR.setError("Please enter valid UTR.");
            return 1;
        }

        return 0;
    }

    public int IsRaiseDisputeDetailsValidated() {

        //STEP 1: Payment Date
        if (TextUtils.isEmpty(raiseDisputeDto.getPaymentDate())) {
            Toast.makeText(context, "Please select Payment Date.", Toast.LENGTH_LONG).show();
            textViewDate.setError("Please select Payment Date.");
            return 1;
        }

        //STEP 2: Bank Name
        if (TextUtils.isEmpty(raiseDisputeDto.getBankId()) || raiseDisputeDto.getBankId().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Bank Name.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerBankName, "Please select Bank Name.", context);
            return 1;
        }

        int status = validateUTRAndOtherDetails();
        if (status != 0)
            return status;

        //STEP 8: RRN
        if (TextUtils.isEmpty(raiseDisputeDto.getRrn())) {
            Toast.makeText(context, "Please enter Bank Transaction ID (RRN).", Toast.LENGTH_LONG).show();
            editTextBankTransactionID.setError("Please enter Bank Transaction ID (RRN).");
            return 1;
        }

        //STEP 8.1: Complete Zero
        if (CommonUtils.isCompleteZero(raiseDisputeDto.getRrn())) {
            Toast.makeText(context, "Please enter valid Bank Transaction ID (RRN).", Toast.LENGTH_LONG).show();
            editTextBankTransactionID.setError("Please enter valid Bank Transaction ID (RRN).");
            return 1;
        }

        //STEP 9: Amount
        if (TextUtils.isEmpty(raiseDisputeDto.getAmount()) || !CommonUtils.isValidAmount(editTextAmount.getText().toString().trim())) {
            Toast.makeText(context, ENTER_VALID_CONST, Toast.LENGTH_LONG).show();
            editTextAmount.setError(ENTER_VALID_CONST);
            return 1;
        }

        //STEP 10: Proof Photo
        if (TextUtils.isEmpty(raiseDisputeDto.getProofPicId()) || raiseDisputeDto.getProofPicId().equalsIgnoreCase("0")) {
            if (TextUtils.isEmpty(raiseDisputeDto.getProofPicBase64())) {
                showMessage("Please add Proof Photo.");
                return 1;
            }
        }

        //STEP 11: Remarks
        if (!TextUtils.isEmpty(raiseDisputeDto.getRemarks())) {
            int remarksLen = editTextRemarks.getText().toString().length();
            if (remarksLen < 3) {
                Toast.makeText(context, "The Remarks must be minimum 3 characters long.", Toast.LENGTH_LONG).show();
                editTextRemarks.setError("The Remarks must be minimum 3 characters long.");
                return 1;
            }
        }

        return 0;
    }

    public void saveRaiseDisputeDetails() {

        //Get Data
        raiseDisputeDto = getRaiseDisputeDetailsDto();

        //Internet Connectivity check
        if (!InternetConnection.isNetworkAvailable(context)) {
            showMessage("No Internet Connection.");
            return;
        }

        //Validate Mandatory fields
        int status = IsRaiseDisputeDetailsValidated();
        if (status != 0)
            return;

        //Post data
        asyncPostRaiseDisputeDetails = new AsyncPostRaiseDisputeDetails(context, raiseDisputeDto, new AsyncPostRaiseDisputeDetails.Callback() {
            @Override
            public void onResult(String result) {
                processResult(result);
            }
        });
        asyncPostRaiseDisputeDetails.execute("");
    }

    private void processResult(String result) {
        try {
            if (TextUtils.isEmpty(result)) {
                AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                return;
            }

            //Handle Response
            if (result.startsWith("ERROR|")) {
                result = result.replace("ERROR|", "");

                String msg = TextUtils.isEmpty(result) ? context.getString(R.string.Warning) : result;
                AlertDialogBoxInfo.alertDialogShow(context, msg);
                return;
            }

            if (result.startsWith("OKAY|")) {
                result = result.replace("OKAY|", "");
                String msg = TextUtils.isEmpty(result) ? "Dispute Raised Successfully." : result;
                showMessageWithFinish(msg);
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
        }
    }

    private void setResult(boolean IsReload) {
        Intent intent = new Intent(context, AllRaisedDisputeDetailsActivity.class);
        intent.putExtra("PERFORM_RELOAD", IsReload);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public void showMessageWithFinish(String msg) {
        if (TextUtils.isEmpty(msg))
            return;

        if (alert == null) {
            alertDialogBuilder = new AlertDialog.Builder(context);

            alertDialogBuilder
                    .setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            alert = null;
                            setResult(true);
                        }
                    });
            alert = alertDialogBuilder.create();
            alert.show();
        }
    }

    private void showDateTimeDialogPicker() {

        Date defaultDate = null;
        defaultDate = selectedDate;

        // Show DateTime Picker Dialog.
        dateTimePickerDialog = new DateTimePickerDialog(this, true, defaultDate, new DateTimePickerDialog.IDateTimePicker() {
            @Override
            public void getDateTime(Date datetime, String defaultFormattedDateTime) {
                try {
                    String formateYMD = dateFormatterYMD.format(datetime);

                    if (selectedDateTimeId != 0) {

                        selectedDate = datetime;
                        strSelectedDate = formateYMD;
                        raiseDisputeDto.setPaymentDate(strSelectedDate);

                        String date = CommonUtils.getFormattedDate(YYYY_MM_DD_CONST, "dd MMM yyyy", raiseDisputeDto.getPaymentDate());
                        textViewDate.setText(date);
                        textViewDate.setError(null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Calendar c = Calendar.getInstance();
        c.set(2020, 2, 1);//Year,Month -1,Day

        Calendar curCal = Calendar.getInstance();
        int day = curCal.get(Calendar.DATE);
        int month = curCal.get(Calendar.MONTH);
        int year = curCal.get(Calendar.YEAR) - 18;
        curCal.set(year, month, day);

        //Yesterdays date
        try {
            dateTimePickerDialog.setMinDate(c.getTimeInMillis());
            dateTimePickerDialog.setMaxDate(CommonUtils.yesterday().getTime() - 1000);

        } catch (Exception e) {
            e.printStackTrace();
        }

        dateTimePickerDialog.setCancelable(false);
        dateTimePickerDialog.setActionButtonName("Save");
        dateTimePickerDialog.show();

    }

    @Override
    public void onBackPressed() {
        backPressed();
    }

    public void backPressed() {
        setResult(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_serach, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search_main);
        searchItem.setVisible(false);
        MenuItem home = menu.findItem(R.id.action_home_dashborad);
        home.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backPressed();
                break;
            case R.id.action_home_dashborad:
                backPressed();
                break;

            default:
                break;

        }
        return true;
    }
}
