package in.vakrangee.franchisee.documentmanager;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nononsenseapps.filepicker.Utils;
import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;

import java.io.File;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.fragment.app.Fragment;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.commongui.DateTimePickerDialog;
import in.vakrangee.supercore.franchisee.commongui.FileAttachmentDialog;
import in.vakrangee.supercore.franchisee.commongui.FileUtils;
import in.vakrangee.supercore.franchisee.commongui.FilteredFilePickerActivity;
import in.vakrangee.supercore.franchisee.commongui.imagepreview.CustomImageZoomDialogDM;
import in.vakrangee.supercore.franchisee.gstdetails.GSTINDTO;
import in.vakrangee.supercore.franchisee.nextgenfranchiseeapplication.CustomFranchiseeApplicationSpinnerDto;
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


/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MSMECertificateFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "MSMECertificateFragment";

    private View view;
    private Context context;
    private DeprecateHandler deprecateHandler;
    private PermissionHandler permissionHandler;

    private MSMECertificateDto msmeCertificateDto;

    // View Objects
    private EditText editTextNameOnMSMECertificate, editTextMSMECertificateNumber, editTextMSMEIssuingAuthority;
    private TextView txtNameOnMSMECertificateLbl, txtMSMECertificateNumberLbl, txtMSMEIssuingAuthorityLbl, txtIssuingDateLbl, txtValidFromDateLbl, txtExpiringDateLbl, txtMSMECopyLbl, txtStateLbl, txtDistrictLbl;
    ;
    private TextView textViewIssuingDateOfMSME;
    private TextView textViewValidFromDateOfMSME;
    private TextView textViewExpiryDateOfMSME;
    private ImageView imgMSMECerificationImg;

    public CustomSearchableSpinner spinnerState;
    public CustomSearchableSpinner spinnerDistrict;

    private LinearLayout layoutMSMECertificationDetail;


    //Date
    private DateTimePickerDialog dateTimePickerDialog;
    private DateFormat dateFormatter2 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    private DateFormat dateFormatterYMD = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private Date msmeIssuingDate;
    private String strMSMEIssuingDate;
    private Date msmeValidFromDate;
    private String strMSMEValidFromDate;
    private Date msmeExpiryDate;
    private String strMSMEExpiryDate;
    private int selectedDateTimeId = 0;

    //img
    private static final int CAMERA_PIC_REQUEST = 100;
    private static final int BROWSE_FOLDER_REQUEST = 101;
    private static final int SCANLIB_REQUEST_CODE = 99;
    private Uri picUri;                 //Picture URI
    private String SEL_FILE_TYPE;
    private int FROM = -1;
    private static final int MSME_CERTIFICATION_IMAGE_PHOTO = 1;
    private FileAttachmentDialog fileAttachementDialog;
    private CustomImageZoomDialogDM customImagePreviewDialog;
    private List<CustomFranchiseeApplicationSpinnerDto> stateList, districtList;
    private DocumentManagerRepository documentManagerRepo;
    private GetAllSpinnerData getAllSpinnerData = null;

    public MSMECertificateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_msme_certificate, container, false);

        //Initialize data
        this.context = getContext();
        deprecateHandler = new DeprecateHandler(context);
        permissionHandler = new PermissionHandler(getActivity());
        documentManagerRepo = new DocumentManagerRepository(context);

        // Bind UI
        editTextNameOnMSMECertificate = view.findViewById(R.id.editTextNameOnMSMECertificate);
        txtNameOnMSMECertificateLbl = view.findViewById(R.id.txtNameOnMSMECertificateLbl);
        txtIssuingDateLbl = view.findViewById(R.id.txtIssuingDateLbl);
        txtValidFromDateLbl = view.findViewById(R.id.txtValidFromDateLbl);
        txtExpiringDateLbl = view.findViewById(R.id.txtExpiringDateLbl);
        txtMSMECopyLbl = view.findViewById(R.id.txtMSMECopyLbl);
        textViewIssuingDateOfMSME = view.findViewById(R.id.textViewIssuingDateOfMSME);
        textViewValidFromDateOfMSME = view.findViewById(R.id.textViewValidFromDateOfMSME);
        textViewExpiryDateOfMSME = view.findViewById(R.id.textViewExpiryDateOfMSME);
        layoutMSMECertificationDetail = view.findViewById(R.id.layoutMSMECertificationDetail);
        imgMSMECerificationImg = view.findViewById(R.id.imgMSMECerificationImg);
        txtStateLbl = view.findViewById(R.id.txtStateLbl);
        txtDistrictLbl = view.findViewById(R.id.txtDistrictLbl);
        spinnerState = view.findViewById(R.id.spinnerState);
        spinnerDistrict = view.findViewById(R.id.spinnerDistrict);
        editTextMSMECertificateNumber = view.findViewById(R.id.editTextMSMECertificateNumber);
        editTextMSMEIssuingAuthority = view.findViewById(R.id.editTextMSMEIssuingAuthority);
        txtMSMECertificateNumberLbl = view.findViewById(R.id.txtMSMECertificateNumberLbl);
        txtMSMEIssuingAuthorityLbl = view.findViewById(R.id.txtMSMEIssuingAuthorityLbl);

        imgMSMECerificationImg.setOnClickListener(this);
        textViewIssuingDateOfMSME.setOnClickListener(this);
        textViewValidFromDateOfMSME.setOnClickListener(this);
        textViewExpiryDateOfMSME.setOnClickListener(this);

        CommonUtils.InputFiletrWithMaxLength(editTextNameOnMSMECertificate, "~#^|$%&*!", 50);
        CommonUtils.InputFiletrWithMaxLength(editTextMSMEIssuingAuthority, "~#^|$%&*!", 50);

        setCompulsoryMarkLabel();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        permissionHandler.dismissSnackbar();
    }

    //region realod data
    public void reload(String data) {
        //Reload Data
        if (TextUtils.isEmpty(data))
            msmeCertificateDto = new MSMECertificateDto();
        else {
            try {
                Gson gson = new GsonBuilder().create();
                List<MSMECertificateDto> applicationList = gson.fromJson(data, new TypeToken<ArrayList<MSMECertificateDto>>() {
                }.getType());
                if (applicationList.size() > 0) {
                    msmeCertificateDto = applicationList.get(0);
                } else {
                    msmeCertificateDto = new MSMECertificateDto();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        getAllSpinnerData = new GetAllSpinnerData(null, null, null);
        getAllSpinnerData.execute("STATE");

    }
    //endregion

    //region Set State Adapter
    private void setStateSpinnerAdapter(List<CustomFranchiseeApplicationSpinnerDto> stateList, Spinner spinnerState, String selStateId) {

        //spinnerState.setEnabled(true);
        spinnerState.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, stateList));
        int appsstatePos = documentManagerRepo.getSelectedPos(stateList, selStateId);
        spinnerState.setSelection(appsstatePos);
        spinnerState.setOnItemSelectedListener(this);

    }
    //endregion

    //region Set District Adapter
    private void setDistrictSpinnerAdapter(List<CustomFranchiseeApplicationSpinnerDto> districtList, Spinner spinnerDistrict, String selDistrictId) {

        //spinnerDistrict.setEnabled(true);
        spinnerDistrict.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, districtList));
        int appsstatePos = documentManagerRepo.getSelectedPos(districtList, selDistrictId);
        spinnerDistrict.setSelection(appsstatePos);
        spinnerDistrict.setOnItemSelectedListener(this);

    }
    //endregion

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int Id = parent.getId();

        if (Id == R.id.spinnerState) {
            if (position > 0) {
                spinnerState.setTitle("Select State");
                spinnerState.requestFocus();
                CustomFranchiseeApplicationSpinnerDto stateDto = (CustomFranchiseeApplicationSpinnerDto) spinnerState.getItemAtPosition(position);
                if (!stateDto.getId().equals("0")) {
                    msmeCertificateDto.setState(stateDto.getId());

                    //Get Division-District
                    getAllSpinnerData = new GetAllSpinnerData(msmeCertificateDto.getState(), null, null);
                    getAllSpinnerData.execute("DISTRICT");
                }
            } else {
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));
                msmeCertificateDto.setState("0");

                //DISTRICT
                setDistrictSpinnerAdapter(list1, spinnerDistrict, null);
                msmeCertificateDto.setDistrict(null);

            }
        } else if (Id == R.id.spinnerDistrict) {
            if (position > 0) {
                spinnerDistrict.setTitle("Select District");
                spinnerDistrict.requestFocus();
                CustomFranchiseeApplicationSpinnerDto disDto = (CustomFranchiseeApplicationSpinnerDto) spinnerDistrict.getItemAtPosition(position);
                if (!disDto.getId().equals("0")) {
                    msmeCertificateDto.setDistrict(disDto.getId());

                }
            } else {
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));
                msmeCertificateDto.setDistrict("0");

            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    //region bind data
    private void bindData() {

        //STEP 1:Name on MSME Certificate
        editTextNameOnMSMECertificate.setText(msmeCertificateDto.getNameOnMSMECertificate());

        //MSME Certifcate Number
        editTextMSMECertificateNumber.setText(msmeCertificateDto.getMsme_certificate_no());

        //MSME Certifcate Issuing Authority
        editTextMSMEIssuingAuthority.setText(msmeCertificateDto.getMsme_issuing_authority());

        //State
        setStateSpinnerAdapter(stateList, spinnerState, msmeCertificateDto.getState());

        //STEP 2: MSME Certificate Issuing Date
        if (!TextUtils.isEmpty(msmeCertificateDto.getMsmeIssuingDate())) {
            String formattedDate = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", msmeCertificateDto.getMsmeIssuingDate());
            textViewIssuingDateOfMSME.setText(formattedDate);
        }

        //STEP : MSME Certificate Valid From Date
        if (!TextUtils.isEmpty(msmeCertificateDto.getMsmeValidFromDate())) {
            String formattedDate = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", msmeCertificateDto.getMsmeValidFromDate());
            textViewValidFromDateOfMSME.setText(formattedDate);
        }

        //STEP 3: MSME Certificate Expiry Date
        if (!TextUtils.isEmpty(msmeCertificateDto.getMsmeExpiryDate())) {
            String formattedDate = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", msmeCertificateDto.getMsmeExpiryDate());
            textViewExpiryDateOfMSME.setText(formattedDate);
        }

        //STEP 5: IIBF Certificate Scanned Copy
        boolean IsPanPDF = (msmeCertificateDto.getMsmeCertificateScanExt() != null && msmeCertificateDto.getMsmeCertificateScanExt().equalsIgnoreCase("pdf")) ? true : false;
        if (IsPanPDF) {
            Glide.with(context)
                    .load(R.drawable.pdf)
                    .apply(new RequestOptions()
                            .error(R.drawable.pdf)
                            .placeholder(R.drawable.pdf)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgMSMECerificationImg);

        } else {
            if (!TextUtils.isEmpty(msmeCertificateDto.getMsmeCertificateScanId())) {
                String gstUrl = Constants.DownloadImageUrl + msmeCertificateDto.getMsmeCertificateScanId();
                Glide.with(context)
                        .load(gstUrl)
                        .apply(new RequestOptions()
                                .error(R.drawable.ic_camera_alt_black_72dp)
                                .placeholder(R.drawable.ic_camera_alt_black_72dp)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))
                        .into(imgMSMECerificationImg);
            }
        }

        //Enable/disable views
        boolean IsEditable = (!TextUtils.isEmpty(msmeCertificateDto.getIsEditable()) && msmeCertificateDto.getIsEditable().equalsIgnoreCase("1")) ? true : false;
        GUIUtils.setViewAndChildrenEnabled(layoutMSMECertificationDetail, IsEditable);
        ((DocumentManagerActivity) getActivity()).IsFooterLayoutVisible(IsEditable);

    }
    //endregion

    //region compulsaroy red mark
    public void setCompulsoryMarkLabel() {
        TextView[] txtViewsForCompulsoryMark = {txtNameOnMSMECertificateLbl, txtMSMECertificateNumberLbl, txtMSMEIssuingAuthorityLbl,
                txtStateLbl, txtDistrictLbl, txtIssuingDateLbl, txtMSMECopyLbl};  //txtValidFromDateLbl, txtExpiringDateLbl,
        GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);
    }
    //endregion

    //region onclick image and datepicker
    @Override
    public void onClick(View v) {
        int Id = v.getId();
        if (Id == R.id.imgMSMECerificationImg) {
            showImgOrPDF(v);
        } else if (Id == R.id.textViewIssuingDateOfMSME) {
            selectedDateTimeId = v.getId();
            showDateTimeDialogPicker(v);
        } else if (Id == R.id.textViewValidFromDateOfMSME) {

            if (TextUtils.isEmpty(msmeCertificateDto.getMsmeIssuingDate())) {
                textViewIssuingDateOfMSME.setError("Please select MSME Issuing Date first.");
                showMessage("Please select MSME Issuing Date first.");
                return;
            }
            selectedDateTimeId = v.getId();
            showDateTimeDialogPicker(v);
        } else if (Id == R.id.textViewExpiryDateOfMSME) {
            if (TextUtils.isEmpty(msmeCertificateDto.getMsmeValidFromDate())) {
                textViewValidFromDateOfMSME.setError("Please select MSME Valid From Date first.");
                showMessage("Please select MSME Valid From Date first.");
                return;
            }

            selectedDateTimeId = v.getId();
            showDateTimeDialogPicker(v);
        }
    }
    //endregion

    //region On Activity Result

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK) {

                Bitmap bitmapNew = ImageUtils.getBitmap(getActivity().getContentResolver(), picUri, "", "", "");
                File file = new File(picUri.getPath());
                String base64Data = CommonUtils.convertBitmapToString(bitmapNew);
                String fileName = URLDecoder.decode(file.getName(), "UTF-8");
                setImageData(picUri, base64Data, bitmapNew);

            }
            if (requestCode == BROWSE_FOLDER_REQUEST && resultCode == Activity.RESULT_OK) {
                // Use the provided utility method to parse the result
                List<Uri> files = Utils.getSelectedFilesFromResult(data);
                for (Uri uri : files) {
                    File file = Utils.getFileForUri(uri);

                    //Check File size
                    int fileSize = CommonUtils.getFileSizeInMB(file);
                    if (fileSize > 1) {
                        showMessage(context.getResources().getString(R.string.file_size_msg));
                        return;
                    }

                    String ext = FileUtils.getFileExtension(context, uri);
                    String base64Data;

                    if (ext.equalsIgnoreCase("pdf")) {
                        base64Data = CommonUtils.encodeFileToBase64Binary(file);
                        Glide.with(context).asDrawable().load(context.getResources().getDrawable(R.drawable.pdf)).into(imgMSMECerificationImg);
                        msmeCertificateDto.setMsmeCertificateScanBase64(base64Data);
                        msmeCertificateDto.setMsmeCertificateScanExt(ext);
                    } else {
                        file = new ImageZipper(context).setQuality(50).compressToFile(file);
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

                //getActivity().getContentResolver().delete(uri, null, null);
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

    private void setScanCopyData(boolean IsDrawable, Bitmap bitmap, String base64, String ext) {

        switch (FROM) {

            case MSME_CERTIFICATION_IMAGE_PHOTO:
                if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgMSMECerificationImg);
                else {
                    try {
                        bitmap = ImageUtils.rotateImageIfRequired(getActivity().getContentResolver(), bitmap, picUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Glide.with(context).asBitmap().load(bitmap).into(imgMSMECerificationImg);
                }

                msmeCertificateDto.setMsmeCertificateScanBase64(base64);
                msmeCertificateDto.setMsmeCertificateScanExt(ext);
                break;

            default:
                break;

        }
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
                            FROM = MSME_CERTIFICATION_IMAGE_PHOTO;

                            String ext = TextUtils.isEmpty(msmeCertificateDto.getMsmeCertificateScanExt()) ? "jpg" : msmeCertificateDto.getMsmeCertificateScanExt();

                            //FOR PDF
                            if (ext.equalsIgnoreCase("pdf")) {
                                SEL_FILE_TYPE = " images";
                                startScanCamera();
                                return;
                            }

                            //FOR Image
                            if ((TextUtils.isEmpty(msmeCertificateDto.getMsmeCertificateScanId()) ||
                                    msmeCertificateDto.getMsmeCertificateScanId().equalsIgnoreCase("0"))
                                    && TextUtils.isEmpty(msmeCertificateDto.getMsmeCertificateScanBase64())) {
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

    public int IsMSMEDetailsValidated() {

        //STEP 1: Name on MSME Certificate
        msmeCertificateDto.setNameOnMSMECertificate(editTextNameOnMSMECertificate.getText().toString().trim());
        if (TextUtils.isEmpty(msmeCertificateDto.getNameOnMSMECertificate())) {
            Toast.makeText(context, "Please enter Name as mentioned on MSME Certificate.", Toast.LENGTH_LONG).show();
            editTextNameOnMSMECertificate.setError("Please enter Name as mentioned on MSME Certificate.");
            return 1;
        }

        //MSME Certificate Number
        msmeCertificateDto.setMsme_certificate_no(editTextMSMECertificateNumber.getText().toString().trim());
        if (TextUtils.isEmpty(msmeCertificateDto.getMsme_certificate_no())) {
            Toast.makeText(context, "Please enter MSME Certificate Number.", Toast.LENGTH_LONG).show();
            editTextMSMECertificateNumber.setError("Please enter MSME Certificate Number.");
            return 1;
        }

        //MSME Issuing Authority
        msmeCertificateDto.setMsme_issuing_authority(editTextMSMEIssuingAuthority.getText().toString().trim());
        if (TextUtils.isEmpty(msmeCertificateDto.getMsme_issuing_authority())) {
            Toast.makeText(context, "Please enter MSME Issuing Authority.", Toast.LENGTH_LONG).show();
            editTextMSMEIssuingAuthority.setError("Please enter MSME Issuing Authority.");
            return 1;
        }

        //STEP 3: State
        if (TextUtils.isEmpty(msmeCertificateDto.getState()) || msmeCertificateDto.getState().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select State.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerState, "Please select State.", context);
            return 1;
        }

        //STEP 4: District
        if (TextUtils.isEmpty(msmeCertificateDto.getDistrict()) || msmeCertificateDto.getDistrict().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select District.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerDistrict, "Please select District.", context);
            return 1;
        }

        //STEP 2: Issuing Date
        if (TextUtils.isEmpty(msmeCertificateDto.getMsmeIssuingDate())) {
            Toast.makeText(context, "Please select MSME Issuing Date.", Toast.LENGTH_LONG).show();
            textViewIssuingDateOfMSME.setError("Please select MSME Issuing Date.");
            return 1;
        }
/*
        //STEP : Valid From Date
        if (TextUtils.isEmpty(msmeCertificateDto.getMsmeValidFromDate())) {
            Toast.makeText(context, "Please select MSME Valid From Date.", Toast.LENGTH_LONG).show();
            textViewValidFromDateOfMSME.setError("Please select MSME Valid From Date.");
            return 1;
        }

        //STEP 3: Expiry Date
        if (TextUtils.isEmpty(msmeCertificateDto.getMsmeExpiryDate())) {
            Toast.makeText(context, "Please select MSME Expiry Date.", Toast.LENGTH_LONG).show();
            textViewExpiryDateOfMSME.setError("Please select MSME Expiry Date.");
            return 1;
        }*/

        //STEP 3: MSME Card Scan Copy
        if (TextUtils.isEmpty(msmeCertificateDto.getMsmeCertificateScanId()) || msmeCertificateDto.getMsmeCertificateScanId().equalsIgnoreCase("0")) {
            if (TextUtils.isEmpty(msmeCertificateDto.getMsmeCertificateScanBase64())) {
                showMessage("Please add MSME Certificate Scan Copy.");
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

    public MSMECertificateDto getMSMECertificateDetail() {

        if (msmeCertificateDto == null)
            return null;

        msmeCertificateDto.setNameOnMSMECertificate(editTextNameOnMSMECertificate.getText().toString());
        msmeCertificateDto.setMsme_issuing_authority(editTextMSMEIssuingAuthority.getText().toString());

        return msmeCertificateDto;
    }

    private void refreshMSMEDetailsImg(GSTINDTO imageDto) {

        msmeCertificateDto.setChangedPhoto(imageDto.isChangedPhoto());
        msmeCertificateDto.setMsmeCertificateScanId(imageDto.getGstImageId());
        msmeCertificateDto.setBitmap(imageDto.getBitmap());
        msmeCertificateDto.setMsmeCertificateScanExt("jpg");
        msmeCertificateDto.setMsmeCertificateScanBase64(imageDto.getGstImage());

    }

    private GSTINDTO prepareDtoForPreview(int FROM) {
        GSTINDTO imageDto = new GSTINDTO();

        imageDto.setChangedPhoto(msmeCertificateDto.isChangedPhoto());
        imageDto.setGstImageId(msmeCertificateDto.getMsmeCertificateScanId());
        imageDto.setBitmap(msmeCertificateDto.getBitmap());
        imageDto.setName("");
        imageDto.setGstImage(msmeCertificateDto.getMsmeCertificateScanBase64());
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

                    refreshMSMEDetailsImg(dto);

                    //Refresh MSME Certificate Image
                    if (!TextUtils.isEmpty(msmeCertificateDto.getMsmeCertificateScanBase64())) {
                        Bitmap bitmap = CommonUtils.StringToBitMap(msmeCertificateDto.getMsmeCertificateScanBase64());
                        if (bitmap != null)
                            imgMSMECerificationImg.setImageBitmap(bitmap);
                    }
                }
            });
            customImagePreviewDialog.show();
            customImagePreviewDialog.setCancelable(false);

            //Title
            String title = "MSME Certificate Image";
            customImagePreviewDialog.setDialogTitle(title);

            //Change Photo Allowed
            boolean IsEditable = (!TextUtils.isEmpty(msmeCertificateDto.getIsEditable()) &&
                    msmeCertificateDto.getIsEditable().equalsIgnoreCase("1")) ? true : false;
            customImagePreviewDialog.allowChangePhoto(IsEditable);
            customImagePreviewDialog.allowSaveOption(IsEditable);
            customImagePreviewDialog.allowRemarks(false);

        } else {
            Toast.makeText(context, "No photo available to preview.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showImgOrPDF(final View view) {

        boolean IsGSTImgPDF = (msmeCertificateDto.getMsmeCertificateScanExt() != null && msmeCertificateDto.getMsmeCertificateScanExt().equalsIgnoreCase("pdf")) ? true : false;
        boolean IsEditable = (!TextUtils.isEmpty(msmeCertificateDto.getIsEditable()) && msmeCertificateDto.getIsEditable().equalsIgnoreCase("1")) ? true : false;

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
        if (selectedDateTimeId == R.id.textViewIssuingDateOfMSME) {
            defaultDate = msmeIssuingDate;
        } else if (selectedDateTimeId == R.id.textViewValidFromDateOfMSME) {
            defaultDate = msmeValidFromDate;
        } else if (selectedDateTimeId == R.id.textViewExpiryDateOfMSME) {
            defaultDate = msmeExpiryDate;
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

                        if (selectedDateTimeId == R.id.textViewIssuingDateOfMSME) {
                            msmeIssuingDate = datetime;
                            strMSMEIssuingDate = formateYMD;
                            msmeCertificateDto.setMsmeIssuingDate(strMSMEIssuingDate);

                            String issuingDate = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", msmeCertificateDto.getMsmeIssuingDate());
                            textViewIssuingDateOfMSME.setText(issuingDate);
                            textViewIssuingDateOfMSME.setError(null);

                        } else if (selectedDateTimeId == R.id.textViewExpiryDateOfMSME) {
                            msmeExpiryDate = datetime;
                            strMSMEExpiryDate = formateYMD;
                            msmeCertificateDto.setMsmeExpiryDate(strMSMEExpiryDate);

                            String DOB = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", msmeCertificateDto.getMsmeExpiryDate());
                            textViewExpiryDateOfMSME.setText(DOB);
                            textViewExpiryDateOfMSME.setError(null);

                        } else if (selectedDateTimeId == R.id.textViewValidFromDateOfMSME) {
                            msmeValidFromDate = datetime;
                            strMSMEValidFromDate = formateYMD;
                            msmeCertificateDto.setMsmeValidFromDate(strMSMEValidFromDate);

                            String DOB = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", msmeCertificateDto.getMsmeValidFromDate());
                            textViewValidFromDateOfMSME.setText(DOB);
                            textViewValidFromDateOfMSME.setError(null);

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

        if (selectedDateTimeId == R.id.textViewIssuingDateOfMSME) {
            dateTimePickerDialog.setMinDate(end);
            dateTimePickerDialog.setMaxDate(now);
        } else if (selectedDateTimeId == R.id.textViewValidFromDateOfMSME) {

            Date issueDate = CommonUtils.parseStringToDate(msmeCertificateDto.getMsmeIssuingDate());
            dateTimePickerDialog.setMinDate(issueDate.getTime());
            dateTimePickerDialog.setMaxDate(now);

        } else if (selectedDateTimeId == R.id.textViewExpiryDateOfMSME) {
            Date fromDate = CommonUtils.parseStringToDate(msmeCertificateDto.getMsmeValidFromDate());
            dateTimePickerDialog.setMinDate(fromDate.getTime());
        }

        dateTimePickerDialog.setCancelable(false);
        dateTimePickerDialog.setActionButtonName("Save");
        dateTimePickerDialog.show();

    }
    //endregion

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    class GetAllSpinnerData extends AsyncTask<String, Void, String> {

        private String stateId;
        private String districtId;
        private String vtcId;
        private String type;

        public GetAllSpinnerData(String stateId, String districtId, String vtcId) {
            this.stateId = stateId;
            this.districtId = districtId;
            this.vtcId = vtcId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressSpinner(context);
        }

        @Override
        protected String doInBackground(String... strings) {

            type = strings[0];

            Connection connection = new Connection(context);
            String tmpVkId = connection.getVkid();
            String enquiryId = CommonUtils.getEnquiryId(context);
            String vkIdOrEnquiryId = TextUtils.isEmpty(tmpVkId) ? enquiryId : tmpVkId;

            switch (type.toUpperCase()) {

                case "STATE":
                    stateList = documentManagerRepo.getStateList(vkIdOrEnquiryId);
                    break;

                case "DISTRICT":
                    districtList = documentManagerRepo.getDistrictList(vkIdOrEnquiryId, stateId);
                    break;

                default:
                    break;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissProgressSpinner();

            switch (type.toUpperCase()) {

                case "STATE":
                    //Bind Data
                    bindData();
                    break;

                case "DISTRICT":
                    setDistrictSpinnerAdapter(districtList, spinnerDistrict, msmeCertificateDto.getDistrict());
                    break;

                default:
                    break;
            }
        }
    }

}

