package in.vakrangee.franchisee.loandocument.loan_sanction;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.nononsenseapps.filepicker.Utils;
import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;

import java.io.File;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.vakrangee.franchisee.loandocument.LoanDocumentRepository;
import in.vakrangee.franchisee.loandocument.R;
import in.vakrangee.franchisee.loandocument.asynctask.AsyncGetUBILoanSanctionLetterDetails;
import in.vakrangee.franchisee.loandocument.asynctask.AsyncSaveUBILoanSanctionDetails;
import in.vakrangee.franchisee.loandocument.model.UploadSanctionDto;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.commongui.DateTimePickerDialog;
import in.vakrangee.supercore.franchisee.commongui.FileAttachmentDialog;
import in.vakrangee.supercore.franchisee.commongui.FileUtils;
import in.vakrangee.supercore.franchisee.commongui.FilteredFilePickerActivity;
import in.vakrangee.supercore.franchisee.commongui.animation.AnimationHanndler;
import in.vakrangee.supercore.franchisee.commongui.imagepreview.CustomImageZoomDialogDM;
import in.vakrangee.supercore.franchisee.gstdetails.GSTINDTO;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.GUIUtils;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;
import in.vakrangee.supercore.franchisee.utils.ImageZipper;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;

public class UBILoanSanctionFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "UBILoanSanctionFragment";

    private View view;
    private Context context;
    private DeprecateHandler deprecateHandler;
    private PermissionHandler permissionHandler;

    //Date
    private DateTimePickerDialog dateTimePickerDialog;
    private DateFormat dateFormatter2 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    private DateFormat dateFormatterYMD = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private Date loanSanctionDate;
    private String strLoanSanctionDate;

    private int selectedDateTimeId = 0;

    //img
    private static final int BROWSE_FOLDER_REQUEST = 101;
    private static final int SCANLIB_REQUEST_CODE = 99;
    private Uri picUri;                 //Picture URI
    private String SEL_FILE_TYPE;
    private int FROM = -1;
    private static final int UPLOAD_SANCTION_IMAGE_PHOTO = 1;
    private FileAttachmentDialog fileAttachementDialog;
    private CustomImageZoomDialogDM customImagePreviewDialog;
    private LoanDocumentRepository loanDocumentRepo;
    private AsyncGetUBILoanSanctionLetterDetails asyncGetUBILoanSanctionLetterDetails = null;
    private AsyncSaveUBILoanSanctionDetails asyncSaveUBILoanSanctionDetails = null;
    private UploadSanctionDto uploadSanctionDto;
    private TextView txtLoanSanctionLetterDateLbl, txtTermLoanSanctionAmountLbl, txtWorkingCapitalSanctionAmountLbl, txtSanctionLetterLbl;
    private TextView textViewLoanSanctionLetterDate;
    private EditText editTextTermLoanSanctionAmount, editTextWorkingCapitalSanctionAmount;
    private ImageView imgSanctionLetter;
    private LinearLayout layoutLoanSanctionDetail;
    private MaterialButton btnCancel, btnSubmit;
    private static final String ENTER_VALID_TERM_AMT_CONST = "Please enter valid Term Loan Sanction Amount.";
    private static final String ENTER_VALID_WORKING_CAPITAL_AMT_CONST = "Please enter valid Working Capital Sanction Amount.";
    private static final String COLOR_FF0000_CONST = "#FF0000";
    private static final String COLOR_468847_CONST = "#468847";

    public UBILoanSanctionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_ubi_loan_sanction, container, false);

        //Initialize data
        this.context = getContext();
        deprecateHandler = new DeprecateHandler(context);
        permissionHandler = new PermissionHandler(getActivity());
        loanDocumentRepo = new LoanDocumentRepository(context);

        // Bind UI
        txtLoanSanctionLetterDateLbl = view.findViewById(R.id.txtLoanSanctionLetterDateLbl);
        txtTermLoanSanctionAmountLbl = view.findViewById(R.id.txtTermLoanSanctionAmountLbl);
        txtWorkingCapitalSanctionAmountLbl = view.findViewById(R.id.txtWorkingCapitalSanctionAmountLbl);
        txtSanctionLetterLbl = view.findViewById(R.id.txtSanctionLetterLbl);
        textViewLoanSanctionLetterDate = view.findViewById(R.id.textViewLoanSanctionLetterDate);
        editTextTermLoanSanctionAmount = view.findViewById(R.id.editTextTermLoanSanctionAmount);
        editTextWorkingCapitalSanctionAmount = view.findViewById(R.id.editTextWorkingCapitalSanctionAmount);
        imgSanctionLetter = view.findViewById(R.id.imgSanctionLetter);
        layoutLoanSanctionDetail = view.findViewById(R.id.layoutLoanSanctionDetail);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnSubmit = view.findViewById(R.id.btnSubmit);

        textViewLoanSanctionLetterDate.setOnClickListener(this);
        imgSanctionLetter.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        setCompulsoryMarkLabel();

        //Term Loan Sanction Amount
        editTextTermLoanSanctionAmount.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(15, 2)});

        //Working Capital Sanction Amount
        editTextWorkingCapitalSanctionAmount.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(15, 2)});

        return view;
    }

    //region realod data
    public void reload() {
        asyncGetUBILoanSanctionLetterDetails = new AsyncGetUBILoanSanctionLetterDetails(context, new AsyncGetUBILoanSanctionLetterDetails.Callback() {
            @Override
            public void onResult(String result) {
                processResult(result);
            }
        });
        asyncGetUBILoanSanctionLetterDetails.execute("");
    }
    //endregion

    private void processResult(String result) {
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
                    uploadSanctionDto = new UploadSanctionDto();
                else {
                    Gson gson = new Gson();
                    uploadSanctionDto = gson.fromJson(data, UploadSanctionDto.class);
                }

                bindData();
            } else {
                AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
        }
    }

    //region bind data
    private void bindData() {

        //Loan Sanction Letter Date
        if (!TextUtils.isEmpty(uploadSanctionDto.getLoanSanctionLetterDate())) {
            String formattedDate = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", uploadSanctionDto.getLoanSanctionLetterDate());
            textViewLoanSanctionLetterDate.setText(formattedDate);
        }

        //Term Loan Sanction Amount
        editTextTermLoanSanctionAmount.setText(uploadSanctionDto.getTermLoanSanctionAmount());

        //Working Capital Sanction Amount
        editTextWorkingCapitalSanctionAmount.setText(uploadSanctionDto.getWorkingCapitalSanctionAmount());

        //Upload Acknowledgement Scanned Copy
        boolean IsPanPDF = (uploadSanctionDto.getUploadSanctionScanExt() != null && uploadSanctionDto.getUploadSanctionScanExt().equalsIgnoreCase("pdf")) ? true : false;
        if (IsPanPDF) {
            Glide.with(context)
                    .load(R.drawable.pdf)
                    .apply(new RequestOptions()
                            .error(R.drawable.pdf)
                            .placeholder(R.drawable.pdf)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgSanctionLetter);

        } else {
            if (!TextUtils.isEmpty(uploadSanctionDto.getUploadSanctionScanId())) {
                String gstUrl = Constants.DownloadImageUrl + uploadSanctionDto.getUploadSanctionScanId();
                Glide.with(context)
                        .load(gstUrl)
                        .apply(new RequestOptions()
                                .error(R.drawable.ic_camera_alt_black_72dp)
                                .placeholder(R.drawable.ic_camera_alt_black_72dp)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))
                        .into(imgSanctionLetter);
            } else {
                Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.ic_camera_alt_black_72dp)).into(imgSanctionLetter);
            }
        }

        //Enable/disable views
        boolean IsEditable = (!TextUtils.isEmpty(uploadSanctionDto.getIsEditable()) && uploadSanctionDto.getIsEditable().equalsIgnoreCase("1")) ? true : false;
        GUIUtils.setViewAndChildrenEnabled(layoutLoanSanctionDetail, IsEditable);
        if (IsEditable) {
            btnSubmit.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.VISIBLE);
        } else {
            btnSubmit.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
        }
    }
    //endregion

    //region compulsaroy red mark
    public void setCompulsoryMarkLabel() {
        TextView[] txtViewsForCompulsoryMark = {txtLoanSanctionLetterDateLbl, txtTermLoanSanctionAmountLbl, txtWorkingCapitalSanctionAmountLbl, txtSanctionLetterLbl};
        GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);
    }
    //endregion

    @Override
    public void onPause() {
        super.onPause();
        permissionHandler.dismissSnackbar();
    }

    @Override
    public void onClick(View v) {
        int Id = v.getId();
        if (Id == R.id.imgSanctionLetter) {
            showImgOrPDF(v);

        } else if (Id == R.id.textViewLoanSanctionLetterDate) {
            selectedDateTimeId = v.getId();
            showDateTimeDialogPicker(v);

        } else if (Id == R.id.btnSubmit) {
            AnimationHanndler.bubbleAnimation(context, v);

            int status = IsUploadSanctionDetailsValidated();
            if (status != 0)
                return;

            //Save
            saveUploadSanctionDetails();

        } else if (Id == R.id.btnCancel) {
            ((UBILoanSanctionActivity) getActivity()).backPressed();
        }
    }

    //region On Activity Result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == BROWSE_FOLDER_REQUEST && resultCode == Activity.RESULT_OK) {
                // Use the provided utility method to parse the result
                List<Uri> files = Utils.getSelectedFilesFromResult(data);
                for (Uri uri : files) {
                    File file = Utils.getFileForUri(uri);
                    String ext = FileUtils.getFileExtension(context, uri);

                    if (!ext.equalsIgnoreCase("pdf")) {
                        file = new ImageZipper(context).setQuality(50).compressToFile(file);

                    }

                    //Check File size
                    int fileSize = CommonUtils.getFileSizeInMB(file);
                    if (fileSize > 1) {
                        showMessage(context.getResources().getString(R.string.file_size_msg));
                        return;
                    }

                    String base64Data;
                    if (ext.equalsIgnoreCase("pdf")) {
                        base64Data = CommonUtils.encodeFileToBase64Binary(file);
                        Glide.with(context).asDrawable().load(context.getResources().getDrawable(R.drawable.pdf)).into(imgSanctionLetter);
                        uploadSanctionDto.setUploadSanctionScanBase64(base64Data);
                        uploadSanctionDto.setUploadSanctionScanExt(ext);
                    } else {
                        Bitmap bitmapNew = new ImageZipper(context).compressToBitmap(file);
                        bitmapNew = ImageUtils.getResizedBitmap(bitmapNew);
                        base64Data = CommonUtils.convertBitmapToString(bitmapNew);
                        setImageData(uri, base64Data, bitmapNew);
                    }
                }
            }
            if (requestCode == SCANLIB_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
                Bitmap bitmap = null;

                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                picUri = uri;

                File file = new File(uri.getPath());
                String ext = FileUtils.getFileExtension(context, uri);

                if (!ext.equalsIgnoreCase("pdf")) {
                    file = new ImageZipper(context).setQuality(50).compressToFile(file);
                    bitmap = new ImageZipper(context).compressToBitmap(file);
                }

                //Check File size
                int fileSize = CommonUtils.getFileSizeInMB(file);
                if (fileSize > 1) {
                    showMessage(context.getResources().getString(R.string.file_size_msg));
                    return;
                }

                String base64Data = CommonUtils.convertBitmapToString(bitmap);
                String fileName = URLDecoder.decode(file.getName(), "UTF-8");

                setImageData(picUri, base64Data, bitmap);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion

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
                permissionHandler.requestPermission(view, Manifest.permission.CAMERA, getString(R.string.needs_permission_camera_msg), new IPermission() {
                    @Override
                    public void IsPermissionGranted(boolean IsGranted) {
                        if (IsGranted) {
                            FROM = UPLOAD_SANCTION_IMAGE_PHOTO;

                            String ext = TextUtils.isEmpty(uploadSanctionDto.getUploadSanctionScanExt()) ? "jpg" : uploadSanctionDto.getUploadSanctionScanExt();

                            //FOR PDF
                            if (ext.equalsIgnoreCase("pdf")) {
                                SEL_FILE_TYPE = " images";
                                startScanCamera();
                                return;
                            }

                            //FOR Image
                            if ((TextUtils.isEmpty(uploadSanctionDto.getUploadSanctionScanId()) ||
                                    uploadSanctionDto.getUploadSanctionScanId().equalsIgnoreCase("0"))
                                    && TextUtils.isEmpty(uploadSanctionDto.getUploadSanctionScanBase64())) {
                                SEL_FILE_TYPE = " images";
                                startScanCamera();

                            } else {
                                GSTINDTO previewDto = prepareDtoForPreview(FROM);
                                showImagePreviewDialog(previewDto);
                            }
                        }
                    }
                });
            }

            @Override
            public void folderClick() {
                //If the app has not the permission then asking for the permission
                permissionHandler.requestPermission(view, Manifest.permission.WRITE_EXTERNAL_STORAGE, getString(R.string.needs_permission_storage_msg), new IPermission() {
                    @Override
                    public void IsPermissionGranted(boolean IsGranted) {
                        if (IsGranted) {
                            // This always works
                            Intent i = new Intent(context, FilteredFilePickerActivity.class);
                            // Set these depending on your use case. These are the defaults.
                            i.putExtra(FilteredFilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
                            i.putExtra(FilteredFilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
                            i.putExtra(FilteredFilePickerActivity.EXTRA_MODE, FilteredFilePickerActivity.MODE_FILE);
                            FilteredFilePickerActivity.FILE_TYPE = "images/pdf";

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
        });
        fileAttachementDialog.setCancelable(false);
        fileAttachementDialog.show();
    }

    public int IsUploadSanctionDetailsValidated() {

        editTextTermLoanSanctionAmount.setError(null);
        editTextWorkingCapitalSanctionAmount.setError(null);

        //Loan Sanction Letter Date
        if (TextUtils.isEmpty(uploadSanctionDto.getLoanSanctionLetterDate())) {
            Toast.makeText(context, "Please select Loan Sanction Letter.", Toast.LENGTH_LONG).show();
            textViewLoanSanctionLetterDate.setError("Please select Loan Sanction Letter.");
            return 1;
        }

        //Term Loan Sanction Amount
        uploadSanctionDto.setTermLoanSanctionAmount(editTextTermLoanSanctionAmount.getText().toString().trim());
        if (TextUtils.isEmpty(uploadSanctionDto.getTermLoanSanctionAmount())) {
            Toast.makeText(context, ENTER_VALID_TERM_AMT_CONST, Toast.LENGTH_LONG).show();
            editTextTermLoanSanctionAmount.setError(ENTER_VALID_TERM_AMT_CONST);
            return 1;
        }

        //Working Capital Sanction Amount
        uploadSanctionDto.setWorkingCapitalSanctionAmount(editTextWorkingCapitalSanctionAmount.getText().toString().trim());
        if (TextUtils.isEmpty(uploadSanctionDto.getWorkingCapitalSanctionAmount())) {
            Toast.makeText(context, ENTER_VALID_WORKING_CAPITAL_AMT_CONST, Toast.LENGTH_LONG).show();
            editTextWorkingCapitalSanctionAmount.setError(ENTER_VALID_WORKING_CAPITAL_AMT_CONST);
            return 1;
        }

        //Both Zero
        if (uploadSanctionDto.getTermLoanSanctionAmount().equalsIgnoreCase("0") && uploadSanctionDto.getWorkingCapitalSanctionAmount().equalsIgnoreCase("0")) {
            String msg = "Both Term Loan and Working Capital Sanction Amount cannot be zero.";
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            editTextTermLoanSanctionAmount.setError(msg);
            editTextWorkingCapitalSanctionAmount.setError(msg);
            return 1;
        }

        BigDecimal maxTermLoanAmt = new BigDecimal(uploadSanctionDto.getMaxTermLoanSanctionAmount());
        BigDecimal termLoanAmt = new BigDecimal(uploadSanctionDto.getTermLoanSanctionAmount());
        if (termLoanAmt.compareTo(maxTermLoanAmt) == 1) {
            String msg = "Term Loan Sanction Amount cannot be greater than " + uploadSanctionDto.getMaxTermLoanSanctionAmount() + ".";
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            editTextTermLoanSanctionAmount.setError(msg);
            return 1;
        }

        BigDecimal maxWorkingCapAmt = new BigDecimal(uploadSanctionDto.getMaxWorkingCapitalSanctionAmount());
        BigDecimal workingCapAmt = new BigDecimal(uploadSanctionDto.getWorkingCapitalSanctionAmount());
        if (workingCapAmt.compareTo(maxWorkingCapAmt) == 1) {
            String msg = "Working Capital Sanction Amount cannot be greater than " + uploadSanctionDto.getMaxWorkingCapitalSanctionAmount() + ".";
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            editTextWorkingCapitalSanctionAmount.setError(msg);
            return 1;
        }

        //Upload Sanction Scan Copy
        if (TextUtils.isEmpty(uploadSanctionDto.getUploadSanctionScanId()) || uploadSanctionDto.getUploadSanctionScanId().equalsIgnoreCase("0")) {
            if (TextUtils.isEmpty(uploadSanctionDto.getUploadSanctionScanBase64())) {
                showMessage("Please add Sanction Letter Copy.");
                return 1;
            }
        }

        return 0;
    }

    private void startScanCamera() {

        //If the app has not the permission then asking for the permission
        permissionHandler.requestPermission(view, Manifest.permission.CAMERA, getString(R.string.needs_permission_camera_msg), new IPermission() {
            @Override
            public void IsPermissionGranted(boolean IsGranted) {
                if (IsGranted) {
                    int preference = ScanConstants.OPEN_CAMERA;
                    Intent intent1 = new Intent(getActivity(), ScanActivity.class);
                    intent1.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
                    startActivityForResult(intent1, SCANLIB_REQUEST_CODE);
                }
            }
        });
    }

    private void refreshDetailsImg(GSTINDTO imageDto) {

        uploadSanctionDto.setChangedPhoto(imageDto.isChangedPhoto());
        uploadSanctionDto.setUploadSanctionScanId(imageDto.getGstImageId());
        uploadSanctionDto.setBitmap(imageDto.getBitmap());
        uploadSanctionDto.setUploadSanctionScanExt("jpg");
        uploadSanctionDto.setUploadSanctionScanBase64(imageDto.getGstImage());

    }

    private GSTINDTO prepareDtoForPreview(int FROM) {
        GSTINDTO imageDto = new GSTINDTO();

        imageDto.setChangedPhoto(uploadSanctionDto.isChangedPhoto());
        imageDto.setGstImageId(uploadSanctionDto.getUploadSanctionScanId());
        imageDto.setBitmap(uploadSanctionDto.getBitmap());
        imageDto.setName("");
        imageDto.setGstImage(uploadSanctionDto.getUploadSanctionScanBase64());
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

                    refreshDetailsImg(dto);

                    //Refresh Image
                    if (!TextUtils.isEmpty(uploadSanctionDto.getUploadSanctionScanBase64())) {
                        Bitmap bitmap = CommonUtils.StringToBitMap(uploadSanctionDto.getUploadSanctionScanBase64());
                        if (bitmap != null)
                            imgSanctionLetter.setImageBitmap(bitmap);
                    }
                }
            });
            customImagePreviewDialog.show();
            customImagePreviewDialog.setCancelable(false);

            //Title
            String title = "UBI Loan Sanction Letter Image";
            customImagePreviewDialog.setDialogTitle(title);

            //Change Photo Allowed
            boolean IsEditable = (!TextUtils.isEmpty(uploadSanctionDto.getIsEditable()) &&
                    uploadSanctionDto.getIsEditable().equalsIgnoreCase("1")) ? true : false;
            customImagePreviewDialog.allowChangePhoto(IsEditable);
            customImagePreviewDialog.allowSaveOption(IsEditable);
            customImagePreviewDialog.allowRemarks(false);

        } else {
            Toast.makeText(context, "No photo available to preview.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showImgOrPDF(final View view) {

        boolean IsGSTImgPDF = (uploadSanctionDto.getUploadSanctionScanExt() != null && uploadSanctionDto.getUploadSanctionScanExt().equalsIgnoreCase("pdf")) ? true : false;
        boolean IsEditable = (!TextUtils.isEmpty(uploadSanctionDto.getIsEditable()) && uploadSanctionDto.getIsEditable().equalsIgnoreCase("1")) ? true : false;

        if (IsEditable) {
            showAttachmentDialog(view);

        } else {

            //PDF - No Preview for PDF

            //Image
            if (!IsGSTImgPDF) {
                //GSTINDTO previewDto = prepareDtoForPreview(FROM);
                //showImagePreviewDialog(previewDto);
                return;
            }
        }
    }

    //region date picker dialog open
    private void showDateTimeDialogPicker(final View view) {

        Date defaultDate = null;
        if (selectedDateTimeId == R.id.textViewLoanSanctionLetterDate) {
            defaultDate = loanSanctionDate;
        }
        // Show DateTime Picker Dialog.
        dateTimePickerDialog = new DateTimePickerDialog(context, true, defaultDate, new DateTimePickerDialog.IDateTimePicker() {
            @Override
            public void getDateTime(Date datetime, String defaultFormattedDateTime) {
                try {
                    String formatedDate = dateFormatter2.format(datetime);
                    String formateYMD = dateFormatterYMD.format(datetime);
                    //Toast.makeText(context, "Selected DateTime : " + formatedDate, Toast.LENGTH_LONG).show();

                    if (selectedDateTimeId != 0) {
                        TextView textViewDateTime = (TextView) view.findViewById(selectedDateTimeId);
                        textViewDateTime.setText(formateYMD);

                        if (selectedDateTimeId == R.id.textViewLoanSanctionLetterDate) {
                            loanSanctionDate = datetime;
                            strLoanSanctionDate = formateYMD;
                            uploadSanctionDto.setLoanSanctionLetterDate(strLoanSanctionDate);

                            String issuingDate = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", uploadSanctionDto.getLoanSanctionLetterDate());
                            textViewLoanSanctionLetterDate.setText(issuingDate);
                            textViewLoanSanctionLetterDate.setError(null);

                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //Last 50 year from Today
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -50);
        long end = calendar.getTimeInMillis();//Set Min and Max Date

        //Today's Date
        long now = new Date().getTime() - 1000;

        if (selectedDateTimeId == R.id.textViewLoanSanctionLetterDate) {

            Date issueDate = CommonUtils.parseStringToDate(uploadSanctionDto.getMinLoanSanctionLetterDate());
            dateTimePickerDialog.setMinDate(issueDate.getTime());
            dateTimePickerDialog.setMaxDate(now);

        }

        dateTimePickerDialog.setCancelable(false);
        dateTimePickerDialog.setActionButtonName("Save");
        dateTimePickerDialog.show();

    }
    //endregion

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (asyncGetUBILoanSanctionLetterDetails != null && !asyncGetUBILoanSanctionLetterDetails.isCancelled()) {
            asyncGetUBILoanSanctionLetterDetails.cancel(true);
        }

        if (asyncSaveUBILoanSanctionDetails != null && !asyncSaveUBILoanSanctionDetails.isCancelled()) {
            asyncSaveUBILoanSanctionDetails.cancel(true);
        }

    }

    private void saveUploadSanctionDetails() {
        //Save
        asyncSaveUBILoanSanctionDetails = new AsyncSaveUBILoanSanctionDetails(context, uploadSanctionDto, new AsyncSaveUBILoanSanctionDetails.Callback() {
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
                        String msg = "UBI Loan Sanction Letter Details saved Successfully.";
                        showMessage(msg);

                        //Handle Response
                        String data = result.replace("OKAY|", "");
                        if (TextUtils.isEmpty(data))
                            uploadSanctionDto = new UploadSanctionDto();
                        else {
                            Gson gson = new Gson();
                            uploadSanctionDto = gson.fromJson(data, UploadSanctionDto.class);
                        }
                        bindData();
                        return;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                }
            }
        });

        asyncSaveUBILoanSanctionDetails.execute("");
    }

    class DecimalDigitsInputFilter implements InputFilter {
        private Pattern mPattern;

        DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero) {
            mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher matcher = mPattern.matcher(dest);
            if (!matcher.matches())
                return "";
            return null;
        }
    }
}
