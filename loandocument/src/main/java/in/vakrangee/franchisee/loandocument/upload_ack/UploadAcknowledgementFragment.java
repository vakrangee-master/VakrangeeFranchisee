package in.vakrangee.franchisee.loandocument.upload_ack;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.vakrangee.franchisee.loandocument.LoanDocumentRepository;
import in.vakrangee.franchisee.loandocument.R;
import in.vakrangee.franchisee.loandocument.asynctask.AsyncGetUBILoanApplicationAckDetails;
import in.vakrangee.franchisee.loandocument.asynctask.AsyncSaveLoanApplicationAckDetails;
import in.vakrangee.franchisee.loandocument.model.UploadAcknowledgementDto;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.commongui.DateTimePickerDialog;
import in.vakrangee.supercore.franchisee.commongui.FileAttachmentDialog;
import in.vakrangee.supercore.franchisee.commongui.FileUtils;
import in.vakrangee.supercore.franchisee.commongui.FilteredFilePickerActivity;
import in.vakrangee.supercore.franchisee.commongui.animation.AnimationHanndler;
import in.vakrangee.supercore.franchisee.commongui.imagepreview.CustomImageZoomDialogDM;
import in.vakrangee.supercore.franchisee.gstdetails.GSTINDTO;
import in.vakrangee.supercore.franchisee.nextgenfranchiseeapplication.CustomFranchiseeApplicationSpinnerDto;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.GUIUtils;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;
import in.vakrangee.supercore.franchisee.utils.ImageZipper;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;
import in.vakrangee.supercore.franchisee.utils.CustomSearchableSpinner;

public class UploadAcknowledgementFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "UploadAcknowledgementFragment";

    private View view;
    private Context context;
    private DeprecateHandler deprecateHandler;
    private PermissionHandler permissionHandler;

    private UploadAcknowledgementDto uploadAcknowledgementDto;

    private LinearLayout layoutUploadAcknowledgementDetail, layoutUploadAcknowledgement, layoutEmailedDate;
    private TextView txtAcknowledgementTypeLbl, txtApplicationSubmissionDateLbl, txtUploadAckLbl, txBranchNameLbl, txtEmailedDateLbl;
    private TextView textViewApplicationSubmissionDate, textViewEmailedDate, txtAcknowledgementTypeStatus, txtBranchName;
    private MaterialButton btnSubmit, btnCancel;
    private CustomSearchableSpinner spinnerAcknowledgementType, spinnerBranchName;

    private ImageView imgUploadAck;
    //Date
    private DateTimePickerDialog dateTimePickerDialog;
    private DateFormat dateFormatter2 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    private DateFormat dateFormatterYMD = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private Date appSubmissionDate;
    private String strAppSubmissionDate;
    private Date appEmailedDate;
    private String strAppEmailedDate;

    private int selectedDateTimeId = 0;

    //img
    private static final int BROWSE_FOLDER_REQUEST = 101;
    private static final int SCANLIB_REQUEST_CODE = 99;
    private Uri picUri;                 //Picture URI
    private String SEL_FILE_TYPE;
    private int FROM = -1;
    private static final int UPLOAD_ACK_IMAGE_PHOTO = 1;
    private FileAttachmentDialog fileAttachementDialog;
    private CustomImageZoomDialogDM customImagePreviewDialog;
    private AsyncGetUBILoanApplicationAckDetails asyncGetUBILoanApplicationAckDetails = null;
    private AsyncSaveLoanApplicationAckDetails asyncSaveLoanApplicationAckDetails = null;
    private GetAllSpinnerData getAllSpinnerData = null;
    private LoanDocumentRepository loanDocumentRepo;
    private List<CustomFranchiseeApplicationSpinnerDto> acknowledgementTypeList, branchNameList;
    private CheckBox checkboxSubmittedThroughEmail;

    public UploadAcknowledgementFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_upload_ack, container, false);

        //Initialize data
        this.context = getContext();
        deprecateHandler = new DeprecateHandler(context);
        permissionHandler = new PermissionHandler(getActivity());
        loanDocumentRepo = new LoanDocumentRepository(context);

        // Bind UI
        checkboxSubmittedThroughEmail = view.findViewById(R.id.checkboxSubmittedThroughEmail);
        textViewEmailedDate = view.findViewById(R.id.textViewEmailedDate);
        txtEmailedDateLbl = view.findViewById(R.id.txtEmailedDateLbl);
        layoutEmailedDate = view.findViewById(R.id.layoutEmailedDate);
        txBranchNameLbl = view.findViewById(R.id.txBranchNameLbl);
        txtBranchName = view.findViewById(R.id.txtBranchName);
        spinnerBranchName = view.findViewById(R.id.spinnerBranchName);
        layoutUploadAcknowledgement = view.findViewById(R.id.layoutUploadAcknowledgement);
        txtAcknowledgementTypeStatus = view.findViewById(R.id.txtAcknowledgementTypeStatus);
        btnCancel = view.findViewById(R.id.btnCancel);
        spinnerAcknowledgementType = view.findViewById(R.id.spinnerAcknowledgementType);
        txtAcknowledgementTypeLbl = view.findViewById(R.id.txtAcknowledgementTypeLbl);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        layoutUploadAcknowledgementDetail = view.findViewById(R.id.layoutUploadAcknowledgementDetail);
        txtApplicationSubmissionDateLbl = view.findViewById(R.id.txtApplicationSubmissionDateLbl);
        txtUploadAckLbl = view.findViewById(R.id.txtUploadAckLbl);
        textViewApplicationSubmissionDate = view.findViewById(R.id.textViewApplicationSubmissionDate);
        imgUploadAck = view.findViewById(R.id.imgUploadAck);

        imgUploadAck.setOnClickListener(this);
        textViewApplicationSubmissionDate.setOnClickListener(this);
        textViewEmailedDate.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        setCompulsoryMarkLabel();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        permissionHandler.dismissSnackbar();
    }

    //region realod data
    public void reload() {
        asyncGetUBILoanApplicationAckDetails = new AsyncGetUBILoanApplicationAckDetails(context, new AsyncGetUBILoanApplicationAckDetails.Callback() {
            @Override
            public void onResult(String result) {
                processResult(result);
            }
        });
        asyncGetUBILoanApplicationAckDetails.execute("");
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
                    uploadAcknowledgementDto = new UploadAcknowledgementDto();
                else {
                    Gson gson = new Gson();
                    uploadAcknowledgementDto = gson.fromJson(data, UploadAcknowledgementDto.class);
                }

                //Call Async to get Acknowledgement Type
                getAllSpinnerData = new GetAllSpinnerData();
                getAllSpinnerData.execute("");

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

        //Branch Name
        spinnerBranchName.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, branchNameList));
        int bPos = loanDocumentRepo.getDefaultOrSelectedChooserData(branchNameList, uploadAcknowledgementDto.getBranchName());
        spinnerBranchName.setSelection(bPos);
        spinnerBranchName.setOnItemSelectedListener(this);

        //Acknowledgement Type
        spinnerAcknowledgementType.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, acknowledgementTypeList));
        int pos = loanDocumentRepo.getDefaultOrSelectedChooserData(acknowledgementTypeList, uploadAcknowledgementDto.getAcknowledgementType());
        spinnerAcknowledgementType.setSelection(pos);
        spinnerAcknowledgementType.setOnItemSelectedListener(this);

        //Application Submission Date
        if (!TextUtils.isEmpty(uploadAcknowledgementDto.getUploadAckDate())) {
            String formattedDate = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", uploadAcknowledgementDto.getUploadAckDate());
            textViewApplicationSubmissionDate.setText(formattedDate);
        }

        //Upload Acknowledgement Scanned Copy
        boolean IsPanPDF = (uploadAcknowledgementDto.getUploadAckScanExt() != null && uploadAcknowledgementDto.getUploadAckScanExt().equalsIgnoreCase("pdf")) ? true : false;
        if (IsPanPDF) {
            Glide.with(context)
                    .load(R.drawable.pdf)
                    .apply(new RequestOptions()
                            .error(R.drawable.pdf)
                            .placeholder(R.drawable.pdf)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgUploadAck);

        } else {
            if (!TextUtils.isEmpty(uploadAcknowledgementDto.getUploadAckScanId())) {
                String gstUrl = Constants.DownloadImageUrl + uploadAcknowledgementDto.getUploadAckScanId();
                Glide.with(context)
                        .load(gstUrl)
                        .apply(new RequestOptions()
                                .error(R.drawable.ic_camera_alt_black_72dp)
                                .placeholder(R.drawable.ic_camera_alt_black_72dp)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))
                        .into(imgUploadAck);
            } else {
                Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.ic_camera_alt_black_72dp)).into(imgUploadAck);
            }
        }

        //Is Application Submitted through Email
        checkboxSubmittedThroughEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String status = isChecked ? "1" : "0";
                uploadAcknowledgementDto.setIsEmailed(status);
                if (status.equalsIgnoreCase("1")) {
                    layoutEmailedDate.setVisibility(View.VISIBLE);
                } else {
                    layoutEmailedDate.setVisibility(View.GONE);
                }
            }
        });

        if (!TextUtils.isEmpty(uploadAcknowledgementDto.getIsEmailed())) {
            int type = Integer.parseInt(uploadAcknowledgementDto.getIsEmailed());
            if (type == 1) {
                checkboxSubmittedThroughEmail.setChecked(true);
                layoutEmailedDate.setVisibility(View.VISIBLE);
            } else {
                checkboxSubmittedThroughEmail.setChecked(false);
                layoutEmailedDate.setVisibility(View.GONE);
            }
        } else {
            layoutEmailedDate.setVisibility(View.GONE);
        }

        //Emailed Date
        if (!TextUtils.isEmpty(uploadAcknowledgementDto.getEmailedDate())) {
            String formattedDate = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", uploadAcknowledgementDto.getEmailedDate());
            textViewEmailedDate.setText(formattedDate);
        }

        //Enable/disable views
        boolean IsEditable = (!TextUtils.isEmpty(uploadAcknowledgementDto.getIsEditable()) && uploadAcknowledgementDto.getIsEditable().equalsIgnoreCase("1")) ? true : false;
        GUIUtils.setViewAndChildrenEnabled(layoutUploadAcknowledgementDetail, IsEditable);
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
        TextView[] txtViewsForCompulsoryMark = {txBranchNameLbl, txtAcknowledgementTypeLbl, txtApplicationSubmissionDateLbl, txtUploadAckLbl, txtEmailedDateLbl};
        GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);
    }
    //endregion

    //region onclick image and datepicker
    @Override
    public void onClick(View v) {
        int Id = v.getId();
        if (Id == R.id.imgUploadAck) {
            showImgOrPDF(v);

        } else if (Id == R.id.textViewApplicationSubmissionDate) {
            selectedDateTimeId = v.getId();
            showDateTimeDialogPicker(v);

        } else if (Id == R.id.textViewEmailedDate) {
            selectedDateTimeId = v.getId();
            showDateTimeDialogPicker(v);

        } else if (Id == R.id.btnSubmit) {
            AnimationHanndler.bubbleAnimation(context, v);

            int status = IsUploadAckDetailsValidated();
            if (status != 0)
                return;

            //Save
            saveUploadAckDetails();

        } else if (Id == R.id.btnCancel) {
            ((UploadAcknowledgementActivity) getActivity()).backPressed();
        }
    }
    //endregion

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
                        Glide.with(context).asDrawable().load(context.getResources().getDrawable(R.drawable.pdf)).into(imgUploadAck);
                        uploadAcknowledgementDto.setUploadAckScanBase64(base64Data);
                        uploadAcknowledgementDto.setUploadAckScanExt(ext);
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
                            FROM = UPLOAD_ACK_IMAGE_PHOTO;

                            String ext = TextUtils.isEmpty(uploadAcknowledgementDto.getUploadAckScanExt()) ? "jpg" : uploadAcknowledgementDto.getUploadAckScanExt();

                            //FOR PDF
                            if (ext.equalsIgnoreCase("pdf")) {
                                SEL_FILE_TYPE = " images";
                                startScanCamera();
                                return;
                            }

                            //FOR Image
                            if ((TextUtils.isEmpty(uploadAcknowledgementDto.getUploadAckScanId()) ||
                                    uploadAcknowledgementDto.getUploadAckScanId().equalsIgnoreCase("0"))
                                    && TextUtils.isEmpty(uploadAcknowledgementDto.getUploadAckScanBase64())) {
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

    public int IsUploadAckDetailsValidated() {

        //Branch Name
        if (TextUtils.isEmpty(uploadAcknowledgementDto.getBranchName()) || uploadAcknowledgementDto.getBranchName().equalsIgnoreCase("-1")) {
            Toast.makeText(context, "Please select Branch Name.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerBranchName, "Please select Branch Name.", context);
            return 1;
        }

        //Acknowledgement Submission Status
        if (TextUtils.isEmpty(uploadAcknowledgementDto.getAcknowledgementType()) || uploadAcknowledgementDto.getAcknowledgementType().equalsIgnoreCase("-1")) {
            Toast.makeText(context, "Please select Acknowledgement Submission Status.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerAcknowledgementType, "Please select Acknowledgement Submission Status.", context);
            return 1;
        }

        //Application Submission Date
        if (TextUtils.isEmpty(uploadAcknowledgementDto.getUploadAckDate())) {
            String dateLbl = txtApplicationSubmissionDateLbl.getText().toString();
            dateLbl = dateLbl.replaceAll("\\*", "");
            Toast.makeText(context, "Please select " + dateLbl + ".", Toast.LENGTH_LONG).show();
            textViewApplicationSubmissionDate.setError("Please select " + dateLbl + ".");
            return 1;
        }

        //Upload Ack Scan Copy
        if (layoutUploadAcknowledgement.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(uploadAcknowledgementDto.getUploadAckScanId()) || uploadAcknowledgementDto.getUploadAckScanId().equalsIgnoreCase("0")) {
                if (TextUtils.isEmpty(uploadAcknowledgementDto.getUploadAckScanBase64())) {
                    showMessage("Please add Application Acknowledgement Copy.");
                    return 1;
                }
            }

        }

        //Emailed Date
        // Commented: 28th Aug 2020 | Discussion with @Sir to remove this option.
        /*if (!TextUtils.isEmpty(uploadAcknowledgementDto.getIsEmailed()) && uploadAcknowledgementDto.getIsEmailed().equalsIgnoreCase("1")) {
            if (TextUtils.isEmpty(uploadAcknowledgementDto.getEmailedDate())) {
                Toast.makeText(context, "Please select Emailed Date.", Toast.LENGTH_LONG).show();
                textViewEmailedDate.setError("Please select Emailed Date.");
                return 1;
            }
        }*/
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

        uploadAcknowledgementDto.setChangedPhoto(imageDto.isChangedPhoto());
        uploadAcknowledgementDto.setUploadAckScanId(imageDto.getGstImageId());
        uploadAcknowledgementDto.setBitmap(imageDto.getBitmap());
        uploadAcknowledgementDto.setUploadAckScanExt("jpg");
        uploadAcknowledgementDto.setUploadAckScanBase64(imageDto.getGstImage());

    }

    private GSTINDTO prepareDtoForPreview(int FROM) {
        GSTINDTO imageDto = new GSTINDTO();

        imageDto.setChangedPhoto(uploadAcknowledgementDto.isChangedPhoto());
        imageDto.setGstImageId(uploadAcknowledgementDto.getUploadAckScanId());
        imageDto.setBitmap(uploadAcknowledgementDto.getBitmap());
        imageDto.setName("");
        imageDto.setGstImage(uploadAcknowledgementDto.getUploadAckScanBase64());
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
                    if (!TextUtils.isEmpty(uploadAcknowledgementDto.getUploadAckScanBase64())) {
                        Bitmap bitmap = CommonUtils.StringToBitMap(uploadAcknowledgementDto.getUploadAckScanBase64());
                        if (bitmap != null)
                            imgUploadAck.setImageBitmap(bitmap);
                    }
                }
            });
            customImagePreviewDialog.show();
            customImagePreviewDialog.setCancelable(false);

            //Title
            String title = "UBI Loan Application Acknowledgement Image";
            customImagePreviewDialog.setDialogTitle(title);

            //Change Photo Allowed
            boolean IsEditable = (!TextUtils.isEmpty(uploadAcknowledgementDto.getIsEditable()) &&
                    uploadAcknowledgementDto.getIsEditable().equalsIgnoreCase("1")) ? true : false;
            customImagePreviewDialog.allowChangePhoto(IsEditable);
            customImagePreviewDialog.allowSaveOption(IsEditable);
            customImagePreviewDialog.allowRemarks(false);

        } else {
            Toast.makeText(context, "No photo available to preview.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showImgOrPDF(final View view) {

        boolean IsGSTImgPDF = (uploadAcknowledgementDto.getUploadAckScanExt() != null && uploadAcknowledgementDto.getUploadAckScanExt().equalsIgnoreCase("pdf")) ? true : false;
        boolean IsEditable = (!TextUtils.isEmpty(uploadAcknowledgementDto.getIsEditable()) && uploadAcknowledgementDto.getIsEditable().equalsIgnoreCase("1")) ? true : false;

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
        if (selectedDateTimeId == R.id.textViewApplicationSubmissionDate) {
            defaultDate = appSubmissionDate;
        } else if (selectedDateTimeId == R.id.textViewEmailedDate) {
            defaultDate = appEmailedDate;
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

                        if (selectedDateTimeId == R.id.textViewApplicationSubmissionDate) {
                            appSubmissionDate = datetime;
                            strAppSubmissionDate = formateYMD;
                            uploadAcknowledgementDto.setUploadAckDate(strAppSubmissionDate);

                            String issuingDate = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", uploadAcknowledgementDto.getUploadAckDate());
                            textViewApplicationSubmissionDate.setText(issuingDate);
                            textViewApplicationSubmissionDate.setError(null);

                        } else if (selectedDateTimeId == R.id.textViewEmailedDate) {
                            appEmailedDate = datetime;
                            strAppEmailedDate = formateYMD;
                            uploadAcknowledgementDto.setEmailedDate(strAppEmailedDate);

                            String issuingDate = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", uploadAcknowledgementDto.getEmailedDate());
                            textViewEmailedDate.setText(issuingDate);
                            textViewEmailedDate.setError(null);

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

        if (selectedDateTimeId == R.id.textViewApplicationSubmissionDate) {

            Date issueDate = CommonUtils.parseStringToDate(uploadAcknowledgementDto.getLoanSubmissionDate());
            dateTimePickerDialog.setMinDate(issueDate.getTime());
            dateTimePickerDialog.setMaxDate(now);

        } else if (selectedDateTimeId == R.id.textViewEmailedDate) {

            Date minDate = CommonUtils.parseStringToDate(uploadAcknowledgementDto.getMinEmailedDate());
            dateTimePickerDialog.setMinDate(minDate.getTime());
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

        if (getAllSpinnerData != null && !getAllSpinnerData.isCancelled()) {
            getAllSpinnerData.cancel(true);
        }

        if (asyncGetUBILoanApplicationAckDetails != null && !asyncGetUBILoanApplicationAckDetails.isCancelled()) {
            asyncGetUBILoanApplicationAckDetails.cancel(true);
        }

        if (asyncSaveLoanApplicationAckDetails != null && !asyncSaveLoanApplicationAckDetails.isCancelled()) {
            asyncSaveLoanApplicationAckDetails.cancel(true);
        }

    }

    private void saveUploadAckDetails() {
        //Save
        asyncSaveLoanApplicationAckDetails = new AsyncSaveLoanApplicationAckDetails(context, uploadAcknowledgementDto, new AsyncSaveLoanApplicationAckDetails.Callback() {
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
                        String msg = "UBI Loan Application Acknowledgement Details saved Successfully.";
                        showMessage(msg);

                        //Handle Response
                        String data = result.replace("OKAY|", "");
                        if (TextUtils.isEmpty(data))
                            uploadAcknowledgementDto = new UploadAcknowledgementDto();
                        else {
                            Gson gson = new Gson();
                            uploadAcknowledgementDto = gson.fromJson(data, UploadAcknowledgementDto.class);
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

        asyncSaveLoanApplicationAckDetails.execute("");
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
            String vkId = connection.getVkid();

            //Acknowledgement Type
            acknowledgementTypeList = loanDocumentRepo.getAcknowledgementType(vkId);

            //Branch Name
            branchNameList = loanDocumentRepo.getBranchNameList(vkId);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissProgressSpinner();
            bindData();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long Id) {
        int id = parent.getId();

        if (id == R.id.spinnerAcknowledgementType) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto dto = (CustomFranchiseeApplicationSpinnerDto) spinnerAcknowledgementType.getItemAtPosition(position);
                uploadAcknowledgementDto.setAcknowledgementType(dto.getId());
                setSelectedStatus(dto.getName());
                onAckStatusSelection(dto.getIsRefused());
            } else {
                uploadAcknowledgementDto.setAcknowledgementType(null);
                setSelectedStatus("Please Select");
            }
        } else if (id == R.id.spinnerBranchName) {
            if (position > 0) {
                CustomFranchiseeApplicationSpinnerDto dto = (CustomFranchiseeApplicationSpinnerDto) spinnerBranchName.getItemAtPosition(position);
                uploadAcknowledgementDto.setBranchName(dto.getId());
                txtBranchName.setVisibility(View.VISIBLE);
                txtBranchName.setText(dto.getName());
            } else {
                uploadAcknowledgementDto.setBranchName(null);
                txtBranchName.setVisibility(View.GONE);
                txtBranchName.setText("");
            }
        }
    }

    private void onAckStatusSelection(String IsRefused) {
        if (!TextUtils.isEmpty(IsRefused) && IsRefused.equalsIgnoreCase("1")) {
            layoutUploadAcknowledgement.setVisibility(View.GONE);
            //txtApplicationSubmissionDateLbl.setText("Application Submission Attempt Date");
            GUIUtils.CompulsoryMark(txtApplicationSubmissionDateLbl, "Application Submission Attempt Date");
        } else {
            layoutUploadAcknowledgement.setVisibility(View.VISIBLE);
            //txtApplicationSubmissionDateLbl.setText("Application Submission Date");
            GUIUtils.CompulsoryMark(txtApplicationSubmissionDateLbl, "Application Submission Date");
        }
    }

    private void setSelectedStatus(String status) {
        if (status.equalsIgnoreCase("Please Select")) {
            txtAcknowledgementTypeStatus.setVisibility(View.GONE);
            txtAcknowledgementTypeStatus.setText("");
        } else {
            txtAcknowledgementTypeStatus.setVisibility(View.VISIBLE);
            txtAcknowledgementTypeStatus.setText(status);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //DO Nothing
    }

}
