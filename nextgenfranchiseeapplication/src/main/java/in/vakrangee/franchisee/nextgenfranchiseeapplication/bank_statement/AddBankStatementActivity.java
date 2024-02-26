package in.vakrangee.franchisee.nextgenfranchiseeapplication.bank_statement;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.button.MaterialButton;
import com.nononsenseapps.filepicker.Utils;
import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;

import java.io.File;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import in.vakrangee.franchisee.nextgenfranchiseeapplication.R;
import in.vakrangee.supercore.franchisee.baseutils.BaseAppCompatActivity;
import in.vakrangee.supercore.franchisee.commongui.DateTimePickerDialog;
import in.vakrangee.supercore.franchisee.commongui.FileAttachmentDialog;
import in.vakrangee.supercore.franchisee.commongui.FileUtils;
import in.vakrangee.supercore.franchisee.commongui.FilteredFilePickerActivity;
import in.vakrangee.supercore.franchisee.commongui.imagepreview.CustomImageZoomDialogDM;
import in.vakrangee.supercore.franchisee.gstdetails.GSTINDTO;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.GUIUtils;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;

public class AddBankStatementActivity extends BaseAppCompatActivity {

    private static final String TAG = "AddBankStatementActivity";
    private Context context;
    private Toolbar toolbar;
    private LinearLayout layoutAddBankStatemtnParent;
    private PermissionHandler permissionHandler;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alert;
    private Date startDate, endDate;
    private String strStartDate, strEndDate;
    private int selectedDateTimeId = 0;
    private DateTimePickerDialog dateTimePickerDialog;
    private static final String DATE_YY_MM_DD_CONSTANTS = "yyyy-MM-dd";
    private DateFormat dateFormatterYMD = new SimpleDateFormat(DATE_YY_MM_DD_CONSTANTS, Locale.US);
    private BankStatementDto bankStatementDto;
    private AsyncPostBankStatement asyncPostBankStatement = null;
    private boolean IsEditable = false;
    private FileAttachmentDialog fileAttachementDialog;
    private static final int CAMERA_PIC_REQUEST = 100;
    private static final int BROWSE_FOLDER_REQUEST = 101;
    private Uri picUri;                 //Picture URI
    private String SEL_FILE_TYPE = "images/pdf";
    private int FROM = -1;
    private DeprecateHandler deprecateHandler;

    private static final int BANK_STATEMENT_SCAN_COPY = 1;
    private static final int SCANLIB_REQUEST_CODE = 99;
    private CustomImageZoomDialogDM customImagePreviewDialog;
    private static final String EXT_JPG = "jpg";
    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    private TextView textViewStatementStartDate, textViewStatementEndDate;
    private EditText editTextBankStatementPassword, editTexConfirmPassword;
    private TextView txtBankStatementPasswordLbl, txtConfirmPasswordLbl;
    private TextView txtStatementStartDateLbl, txtStatementEndDateLbl, txtBankStatementImageLbl;
    private ImageView imgBankStatementImage;
    private String appId;

    private MaterialButton btnSubmit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank_statement);

        //Context
        context = this;
        permissionHandler = new PermissionHandler(AddBankStatementActivity.this);
        deprecateHandler = new DeprecateHandler(context);

        //set toolbar name
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            toolbar.setTitleTextColor(Color.WHITE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + getResources().getString(R.string.add_bank_statement_detail) + "</small>"));

        }

        appId = getIntent().getStringExtra("APP_ID");
        bankStatementDto = (BankStatementDto) getIntent().getSerializableExtra("BANK_STATEMENT_DETAILS");
        IsEditable = getIntent().getBooleanExtra("IS_EDITABLE", true);

        layoutAddBankStatemtnParent = findViewById(R.id.layoutAddBankStatementParent);
        txtStatementStartDateLbl = findViewById(R.id.txtStatementStartDateLbl);
        textViewStatementStartDate = findViewById(R.id.textViewStatementStartDate);
        txtStatementEndDateLbl = findViewById(R.id.txtStatementEndDateLbl);
        textViewStatementEndDate = findViewById(R.id.textViewStatementEndDate);
        txtBankStatementPasswordLbl = findViewById(R.id.txtBankStatementPasswordLbl);
        editTextBankStatementPassword = findViewById(R.id.editTextBankStatementPassword);
        txtConfirmPasswordLbl = findViewById(R.id.txtConfirmPasswordLbl);
        editTexConfirmPassword = findViewById(R.id.editTexConfirmPassword);
        txtBankStatementImageLbl = findViewById(R.id.txtBankStatementImageLbl);
        imgBankStatementImage = findViewById(R.id.imgBankStatementImage);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBankStatementDetails();
            }
        });

        textViewStatementStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDateTimeId = v.getId();
                showDateTimeDialogPicker();
            }
        });

        textViewStatementEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(bankStatementDto.getBankStmtStartDate())) {
                    textViewStatementStartDate.setError("Please select Bank Statement Start Date first.");
                    showMessage("Please select Bank Statement Start Date first.");
                    return;
                }

                selectedDateTimeId = v.getId();
                showDateTimeDialogPicker();
            }
        });

        imgBankStatementImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImgOrPDF(v);
            }
        });

        //Set Compulsory mark
        TextView[] txtViewsForCompulsoryMark = {txtStatementStartDateLbl, txtStatementEndDateLbl, txtBankStatementImageLbl};
        GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);

        reloadData();

    }

    private void showDateTimeDialogPicker() {

        Date defaultDate = null;
        if (selectedDateTimeId == R.id.textViewStatementStartDate) {
            defaultDate = startDate;
        } else if (selectedDateTimeId == R.id.textViewStatementEndDate) {
            defaultDate = endDate;
        }

        // Show DateTime Picker Dialog.
        dateTimePickerDialog = new DateTimePickerDialog(this, true, defaultDate, new DateTimePickerDialog.IDateTimePicker() {
            @Override
            public void getDateTime(Date datetime, String defaultFormattedDateTime) {
                try {
                    String formateYMD = dateFormatterYMD.format(datetime);

                    if (selectedDateTimeId != 0) {

                        if (selectedDateTimeId != 0) {
                            if (selectedDateTimeId == R.id.textViewStatementStartDate) {
                                startDate = datetime;
                                strStartDate = formateYMD;
                                bankStatementDto.setBankStmtStartDate(strStartDate);

                                String date1 = CommonUtils.getFormattedDate(DATE_YY_MM_DD_CONSTANTS, "dd MMM yyyy", bankStatementDto.getBankStmtStartDate());
                                textViewStatementStartDate.setText(date1);
                                textViewStatementStartDate.setError(null);

                            } else if (selectedDateTimeId == R.id.textViewStatementEndDate) {
                                endDate = datetime;
                                strEndDate = formateYMD;
                                bankStatementDto.setBankStmtEndDate(strEndDate);

                                String date2 = CommonUtils.getFormattedDate(DATE_YY_MM_DD_CONSTANTS, "dd MMM yyyy", bankStatementDto.getBankStmtEndDate());
                                textViewStatementEndDate.setText(date2);
                                textViewStatementEndDate.setError(null);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        long now = new Date().getTime() - 1000;

        Calendar c = Calendar.getInstance();
        c.set(1920, 0, 1);//Year,Month -1,Day

        try {
            if (selectedDateTimeId == R.id.textViewStatementStartDate) {

                Date minDate = CommonUtils.parseStringToDate(bankStatementDto.getBankStmtMinStartDate());
                dateTimePickerDialog.setMinDate(minDate.getTime());
                // dateTimePickerDialog.setMinDate(c.getTimeInMillis());
                dateTimePickerDialog.setMaxDate(now);

            } else if (selectedDateTimeId == R.id.textViewStatementEndDate) {
                dateTimePickerDialog.setMinDate(startDate.getTime());

                //Current Date
                Calendar nowCal = Calendar.getInstance();
                nowCal.setTimeInMillis(now);
                dateTimePickerDialog.setMaxDate(nowCal.getTimeInMillis());

               /* //Future 6 Month
                Calendar cal = Calendar.getInstance();
                cal.setTime(startDate);
                cal.add(Calendar.MONTH, +6);

                int retval = cal.getTime().compareTo(nowCal.getTime());

                if (retval > 0) {
                    dateTimePickerDialog.setMaxDate(nowCal.getTimeInMillis());
                } else {
                    dateTimePickerDialog.setMaxDate(cal.getTimeInMillis());
                }*/
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        dateTimePickerDialog.setCancelable(false);
        dateTimePickerDialog.setActionButtonName("Save");
        dateTimePickerDialog.show();

    }

    private void bindSpinner() {

        //Start Date
        if (!TextUtils.isEmpty(bankStatementDto.getBankStmtStartDate())) {
            startDate = CommonUtils.formatDateFromString(bankStatementDto.getBankStmtStartDate());
            String date1 = CommonUtils.getFormattedDate(DATE_YY_MM_DD_CONSTANTS, "dd MMM yyyy", bankStatementDto.getBankStmtStartDate());
            textViewStatementStartDate.setText(date1);
        }

        //End Date
        if (!TextUtils.isEmpty(bankStatementDto.getBankStmtEndDate())) {
            endDate = CommonUtils.formatDateFromString(bankStatementDto.getBankStmtEndDate());
            String date1 = CommonUtils.getFormattedDate(DATE_YY_MM_DD_CONSTANTS, "dd MMM yyyy", bankStatementDto.getBankStmtEndDate());
            textViewStatementEndDate.setText(date1);
        }

        //Bank Statement Password
        editTextBankStatementPassword.setText(bankStatementDto.getBankStmtPassword());
        editTexConfirmPassword.setText(bankStatementDto.getBankStmtPassword());

        //Photo
        boolean IsPDF = ((bankStatementDto.getBankStmtImageExt() != null) && bankStatementDto.getBankStmtImageExt().equalsIgnoreCase("pdf")) ? true : false;
        if (IsPDF) {
            Glide.with(context).asDrawable().load(R.drawable.pdf).into(imgBankStatementImage);
        } else {
            if (!TextUtils.isEmpty(bankStatementDto.getBankStmtImageId())) {
                String picUrl = Constants.DownloadImageUrl + bankStatementDto.getBankStmtImageId();
                Glide.with(context)
                        .load(picUrl)
                        .apply(new RequestOptions()
                                .error(R.drawable.ic_camera_alt_black_72dp)
                                .placeholder(R.drawable.ic_camera_alt_black_72dp)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))

                        .into(imgBankStatementImage);
            }
        }

        //Enable/disable views
        GUIUtils.setViewAndChildrenEnabled(layoutAddBankStatemtnParent, IsEditable);

    }

    private void reloadData() {
        if (bankStatementDto == null) {
            bankStatementDto = new BankStatementDto();
            IsEditable = true;
        }

        bindSpinner();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (asyncPostBankStatement != null && !asyncPostBankStatement.isCancelled()) {
            asyncPostBankStatement.cancel(true);
        }
    }

    public BankStatementDto getBankStatementDto() {

        bankStatementDto.setBankStmtPassword(editTextBankStatementPassword.getText().toString());
        return bankStatementDto;
    }

    public int IsBankStatementDetailsValidated() {

        //Start Date
        if (TextUtils.isEmpty(bankStatementDto.getBankStmtStartDate())) {
            Toast.makeText(context, "Please select Start Date.", Toast.LENGTH_LONG).show();
            textViewStatementStartDate.setError("Please select Start Date.");
            return 1;
        }

        //End Date
        if (TextUtils.isEmpty(bankStatementDto.getBankStmtEndDate())) {
            Toast.makeText(context, "Please select End Date.", Toast.LENGTH_LONG).show();
            textViewStatementEndDate.setError("Please select End Date.");
            return 1;
        }

        //Password
        String pwd = editTextBankStatementPassword.getText().toString().trim();
        String confirm = editTexConfirmPassword.getText().toString().trim();
        if (!TextUtils.isEmpty(pwd) && TextUtils.isEmpty(confirm)) {
            String msg = "Please enter Confirm Bank Statement Password.";
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            editTexConfirmPassword.setError(msg);
            return 1;
        }

        //Confirm Password
        if (!TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(confirm)) {

            if (!pwd.equalsIgnoreCase(confirm)) {
                String msg = "Both Bank Statement Password does not match.";
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                editTextBankStatementPassword.setError(msg);
                return 1;
            }
        }

        //Days Count
        int daysCount = CommonUtils.getNoOfDaysBetweenDates(bankStatementDto.getBankStmtStartDate(), bankStatementDto.getBankStmtEndDate());
        //if (daysCount < 0 || daysCount > 186) {
        if (daysCount == 0) {
            showMessage("Both Bank Statement Start and End date cannot be same.");
            return 1;
        }

        if (daysCount < 0) {
            showMessage("Bank Statement Start date cannot be greater than Bank Statement End date.");
            return 1;
        }

        //Proof Photo
        if (TextUtils.isEmpty(bankStatementDto.getBankStmtImageId()) || bankStatementDto.getBankStmtImageId().equalsIgnoreCase("0")) {
            if (TextUtils.isEmpty(bankStatementDto.getBankStmtImageBase64())) {
                showMessage("Please add Bank Statement Photo.");
                return 1;
            }
        }

        return 0;
    }

    public void saveBankStatementDetails() {

        //Get Data
        bankStatementDto = getBankStatementDto();

        //Internet Connectivity check
        if (!InternetConnection.isNetworkAvailable(context)) {
            showMessage("No Internet Connection.");
            return;
        }

        //Validate Mandatory fields
        int status = IsBankStatementDetailsValidated();
        if (status != 0)
            return;

        //Post data
        postData();
    }

    private void postData() {
        asyncPostBankStatement = new AsyncPostBankStatement(context, appId, bankStatementDto, new AsyncPostBankStatement.Callback() {
            @Override
            public void onResult(String result) {
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
                        String msg = TextUtils.isEmpty(result) ? "Bank Statement saved Successfully." : result;
                        showMessageWithFinish(msg);
                        return;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                }
            }
        });
        asyncPostBankStatement.execute("");
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
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            backPressed();
        } else if (itemId == R.id.action_home_dashborad) {
            backPressed();
        }
        return true;
    }

    private void setResult(boolean IsReload) {
        Intent intent = new Intent(context, AllBankStatementActivity.class);
        intent.putExtra("PERFORM_RELOAD", IsReload);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK) {

                Bitmap bitmapNew = ImageUtils.getBitmap(context.getContentResolver(), picUri, "", "", "");
                String base64Data = CommonUtils.convertBitmapToString(bitmapNew);
                setImageData(picUri, base64Data, bitmapNew);

            }
            if (requestCode == BROWSE_FOLDER_REQUEST && resultCode == Activity.RESULT_OK) {
                // Use the provided utility method to parse the result
                List<Uri> files = Utils.getSelectedFilesFromResult(data);
                for (Uri uri : files) {
                    File file = Utils.getFileForUri(uri);

                    //Check File size
                    int fileSize = CommonUtils.getFileSizeInMB(file);
                    /*if (fileSize > 1) {
                        showMessage(context.getResources().getString(R.string.file_size_msg));
                        return;
                    }*/

                    String ext = FileUtils.getFileExtension(context, uri);
                    String base64Data = CommonUtils.encodeFileToBase64Binary(file);

                    bankStatementDto.setBankStmtImageBase64(base64Data);
                    bankStatementDto.setBankStmtImageExt(ext);
                    bankStatementDto.setChangedPhoto(true);

                    String fileName = URLDecoder.decode(file.getName(), "UTF-8");
                    setImageAndName(fileName, base64Data, uri);

                }
            }
            if (requestCode == SCANLIB_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
                Bitmap bitmap = null;

                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                picUri = uri;
                String base64Data = CommonUtils.convertBitmapToString(bitmap);
                setImageData(picUri, base64Data, bitmap);

            }
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
                imageBitMap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);

            setScanCopyData(IsDrawable, imageBitMap, fileName, base64Data);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setScanCopyData(boolean IsDrawable, Bitmap bitmap, String fileName, String base64) {
        if (IsDrawable)
            Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgBankStatementImage);
        else
            Glide.with(context).asBitmap().load(bitmap).into(imgBankStatementImage);

    }

    private void setImageData(Uri imageUri, String imageBase64, Bitmap bitmapNew) {

        GSTINDTO imageDto = new GSTINDTO();
        imageDto.setUri(imageUri);
        imageDto.setBitmap(bitmapNew);
        imageDto.setChangedPhoto(true);
        imageDto.setGstImage(imageBase64);

        showImagePreviewDialog((Object) imageDto);
    }

    public void showAttachmentDialog(final View view) {
        fileAttachementDialog = new FileAttachmentDialog(context, new FileAttachmentDialog.IFileAttachmentClicks() {
            @Override
            public void cameraClick() {
                //If the app has not the permission then asking for the permission
                performCameraClick(view);
            }

            @Override
            public void folderClick() {
                //If the app has not the permission then asking for the permission
                performFolderClick(view);
            }
        });
        fileAttachementDialog.setCancelable(false);
        fileAttachementDialog.show();
    }

    private void performFolderClick(View view) {
        permissionHandler.requestMultiplePermission(view, permissions, getString(R.string.needs_camera_storage_permission_msg), new IPermission() {
            @Override
            public void IsPermissionGranted(boolean IsGranted) {
                if (IsGranted) {
                    // This always works
                    Intent i = new Intent(context, FilteredFilePickerActivity.class);
                    // Set these depending on your use case. These are the defaults.
                    i.putExtra(FilteredFilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
                    i.putExtra(FilteredFilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
                    i.putExtra(FilteredFilePickerActivity.EXTRA_MODE, FilteredFilePickerActivity.MODE_FILE);
                    FilteredFilePickerActivity.FILE_TYPE = SEL_FILE_TYPE;

                    // Configure initial directory by specifying a String.
                    // You could specify a String like "/storage/emulated/0/", but that can
                    // dangerous. Always use Android's API calls to get paths to the SD-card or
                    // internal memory.
                    i.putExtra(FilteredFilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
                    startActivityForResult(i, BROWSE_FOLDER_REQUEST);
                }
            }
        });

    }

    private void performCameraClick(View view) {
        permissionHandler.requestMultiplePermission(view, permissions, getString(R.string.needs_camera_storage_permission_msg), new IPermission() {
            @Override
            public void IsPermissionGranted(boolean IsGranted) {
                if (IsGranted) {
                    FROM = BANK_STATEMENT_SCAN_COPY;

                    String ext = TextUtils.isEmpty(bankStatementDto.getBankStmtImageExt()) ? "jpg" : bankStatementDto.getBankStmtImageExt();

                    //FOR PDF
                    if (ext.equalsIgnoreCase("pdf")) {
                        startScanCamera();
                        return;
                    }

                    //FOR Image
                    if ((TextUtils.isEmpty(bankStatementDto.getBankStmtImageId()) || bankStatementDto.getBankStmtImageId().equalsIgnoreCase("0")) && TextUtils.isEmpty(bankStatementDto.getBankStmtImageBase64())) {
                        startScanCamera();

                    } else {
                        GSTINDTO previewDto = prepareDtoForPreview(FROM);
                        showImagePreviewDialog(previewDto);
                    }
                }
            }
        });

    }

    private void startScanCamera() {

        //If the app has not the permission then asking for the permission
        permissionHandler.requestMultiplePermission(imgBankStatementImage, permissions, getString(R.string.needs_camera_storage_permission_msg), new IPermission() {
            @Override
            public void IsPermissionGranted(boolean IsGranted) {
                if (IsGranted) {

                    int preference = ScanConstants.OPEN_CAMERA;
                    Intent intent1 = new Intent(AddBankStatementActivity.this, ScanActivity.class);
                    intent1.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
                    startActivityForResult(intent1, SCANLIB_REQUEST_CODE);

                }
            }
        });
    }

    private void refreshGSTDetailsImg(GSTINDTO imageDto) {

        bankStatementDto.setChangedPhoto(imageDto.isChangedPhoto());
        bankStatementDto.setBankStmtImageId(imageDto.getGstImageId());
        bankStatementDto.setBitmap(imageDto.getBitmap());
        bankStatementDto.setBankStmtImageExt("jpg");
        bankStatementDto.setBankStmtImageBase64(imageDto.getGstImage());

    }

    @SuppressLint("LongLogTag")
    private GSTINDTO prepareDtoForPreview(int FROM) {
        Log.e(TAG, "FROM: " + FROM);
        GSTINDTO imageDto = new GSTINDTO();

        imageDto.setChangedPhoto(bankStatementDto.isChangedPhoto());
        imageDto.setGstImageId(bankStatementDto.getBankStmtImageId());
        imageDto.setBitmap(bankStatementDto.getBitmap());
        imageDto.setName("");
        imageDto.setGstImage(bankStatementDto.getBankStmtImageBase64());
        return imageDto;
    }

    private void showImagePreviewDialog(Object object) {

        if (customImagePreviewDialog != null && customImagePreviewDialog.isShowing()) {
            customImagePreviewDialog.refresh(object);
            return;
        }

        if (object != null) {
            customImagePreviewDialog = new CustomImageZoomDialogDM(context, object, new CustomImageZoomDialogDM.IImagePreviewDialogClicks() {
                @Override
                public void capturePhotoClick() {
                    startScanCamera();
                }

                @Override
                public void OkClick(Object object) {
                    GSTINDTO dto = ((GSTINDTO) object);

                    refreshGSTDetailsImg(dto);

                    //Refresh PAN Card Image
                    if (!TextUtils.isEmpty(bankStatementDto.getBankStmtImageBase64())) {
                        Bitmap bitmap = CommonUtils.StringToBitMap(bankStatementDto.getBankStmtImageBase64());
                        if (bitmap != null)
                            imgBankStatementImage.setImageBitmap(bitmap);
                    }
                }
            });
            customImagePreviewDialog.show();
            customImagePreviewDialog.setCancelable(false);

            //Title
            String title = "Bank Statement Image Preview";
            customImagePreviewDialog.setDialogTitle(title);

            //Change Photo Allowed
            customImagePreviewDialog.allowChangePhoto(IsEditable);
            customImagePreviewDialog.allowSaveOption(IsEditable);
            customImagePreviewDialog.allowRemarks(false);

        } else {
            Toast.makeText(context, "No photo available to preview.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showImgOrPDF(final View view) {

        boolean IsGSTImgPDF = (bankStatementDto.getBankStmtImageExt() != null && bankStatementDto.getBankStmtImageExt().equalsIgnoreCase("pdf")) ? true : false;

        if (IsEditable) {
            showAttachmentDialog(view);

        } else {

            //PDF - No Preview for PDF

            //Image
            if (!IsGSTImgPDF) {
                return;
            }
        }
    }

}
