package in.vakrangee.franchisee.documentmanager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.commongui.DateTimePickerDialog;
import in.vakrangee.supercore.franchisee.commongui.FileAttachmentDialog;
import in.vakrangee.supercore.franchisee.commongui.FileUtils;
import in.vakrangee.supercore.franchisee.commongui.FilteredFilePickerActivity;
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
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;
import in.vakrangee.supercore.franchisee.utils.CustomSearchableSpinner;

public class GSTDetailsFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "GSTDetailsFragment";

    private View view;
    private Context context;
    private PermissionHandler permissionHandler;
    private DeprecateHandler deprecateHandler;

    private TextView txtGSTINIssueDateLbl;
    private TextView textViewGSTINIssueDate;
    private TextView txtEntityNameLbl;
    private EditText editTextGstEntityName;
    private TextView txtGstinNumberLbl;
    private EditText editTextGstNumber;
    private EditText editTextConfirmGstNumber;
    private TextView txtAddressLine1Lbl;
    private EditText editTextAddressLine1;
    private EditText editTextAddressLine2;
    private EditText editTextLandmark;
    private TextView txtAreaLbl;
    private EditText edtArea;
    private TextView txtLocalityLbl;
    private EditText edtLocality;
    private TextView txtStateLbl;
    private CustomSearchableSpinner spinnerState;
    private TextView txtDistrictLbl;
    private CustomSearchableSpinner spinnerDistrict;
    private TextView txtVTCLbl;
    private CustomSearchableSpinner spinnerVTC;
    private TextView txtPinCodeLbl;
    private EditText ediTextGSTPincode;
    private TextView txtGSTImageCertificateLbl;
    private ImageView imgGSTImage;
    private LinearLayout layoutGSTDetail;

    private boolean edittext_listener = true;
    private FileAttachmentDialog fileAttachementDialog;
    private static final int CAMERA_PIC_REQUEST = 100;
    private static final int BROWSE_FOLDER_REQUEST = 101;
    private Uri picUri;                 //Picture URI
    private String SEL_FILE_TYPE;
    private int FILE_TYPE_FROM = -1;

    private static final int GSTIN_CERTIFICATE_IMG_COPY = 1;
    private static final int TRN_ACK_IMG_COPY = 2;
    private GSTDetailsDto gstDetailsDto;
    private DocumentManagerRepository documentManagerRepo;
    private GetAllSpinnerData getAllSpinnerData = null;
    private List<CustomFranchiseeApplicationSpinnerDto> stateList, districtList, vtcList;

    private static final int SCANLIB_REQUEST_CODE = 99;
    private CustomImageZoomDialogDM customImagePreviewDialog = null;

    private LinearLayout layoutGSTDetailApplied;

    //region Applied For GSTIN
    private static int FROM;
    private static final int TYPE_GST_CERTIFICATE = 1;
    private static final int TYPE_GST_TRN_RECEIPT = 2;

    private CheckBox checkboxAppliedForGSTIN;
    private LinearLayout layoutHavingGSTDetails;
    private LinearLayout layoutAppliedGSTIN;
    private TextView txtTRNNumberLbl;
    private EditText editTextTRNNumber;
    private TextView txtGSTAckReceiptLbl;
    private ImageView imgGSTAckReceipt;
    private TextView txtDateOfApplicationGSTLbl;
    private TextView textViewDateOfApplicationGST;
    private TextView txtExpectedDateOfGSTLbl;
    private TextView textViewExpectedDateOfGST;

    private DateTimePickerDialog dateTimePickerDialog;
    private DateFormat dateFormatter2 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    private DateFormat dateFormatterYMD = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private Date gstApplicationDate;
    private String strGstApplicationDate;
    private Date expectedGstApplicationDate;
    private String strExpectedGstApplicationDate;
    private Date gstIssueDate;
    private String strGstIssueDate;
    private int selectedDateTimeId = 0;

    //endregion

    public GSTDetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag_gst_details, container, false);

        //Initialize data
        this.context = getContext();
        deprecateHandler = new DeprecateHandler(context);
        permissionHandler = new PermissionHandler(getActivity());
        documentManagerRepo = new DocumentManagerRepository(context);

        //References
        layoutGSTDetailApplied = view.findViewById(R.id.layoutGSTDetailApplied);
        txtEntityNameLbl = view.findViewById(R.id.txtEntityNameLbl);
        editTextGstEntityName = view.findViewById(R.id.editTextGstEntityName);
        txtGstinNumberLbl = view.findViewById(R.id.txtGstinNumberLbl);
        editTextGstNumber = view.findViewById(R.id.editTextGstNumber);
        editTextConfirmGstNumber = view.findViewById(R.id.editTextConfirmGstNumber);
        txtAddressLine1Lbl = view.findViewById(R.id.txtAddressLine1Lbl);
        editTextAddressLine1 = view.findViewById(R.id.editTextAddressLine1);
        editTextAddressLine2 = view.findViewById(R.id.editTextAddressLine2);
        editTextLandmark = view.findViewById(R.id.editTextLandmark);
        txtAreaLbl = view.findViewById(R.id.txtAreaLbl);
        edtArea = view.findViewById(R.id.edtArea);
        txtLocalityLbl = view.findViewById(R.id.txtLocalityLbl);
        edtLocality = view.findViewById(R.id.edtLocality);
        txtStateLbl = view.findViewById(R.id.txtStateLbl);
        spinnerState = view.findViewById(R.id.spinnerState);
        txtDistrictLbl = view.findViewById(R.id.txtDistrictLbl);
        spinnerDistrict = view.findViewById(R.id.spinnerDistrict);
        txtVTCLbl = view.findViewById(R.id.txtVTCLbl);
        spinnerVTC = view.findViewById(R.id.spinnerVTC);
        txtPinCodeLbl = view.findViewById(R.id.txtPinCodeLbl);
        ediTextGSTPincode = view.findViewById(R.id.ediTextGSTPincode);
        txtGSTImageCertificateLbl = view.findViewById(R.id.txtGSTImageCertificateLbl);
        imgGSTImage = view.findViewById(R.id.imgGSTImageDoc);
        layoutGSTDetail = view.findViewById(R.id.layoutGSTDetail);
        checkboxAppliedForGSTIN = view.findViewById(R.id.checkboxAppliedForGSTIN);
        layoutHavingGSTDetails = view.findViewById(R.id.layoutHavingGSTDetails);
        layoutAppliedGSTIN = view.findViewById(R.id.layoutAppliedGSTIN);
        txtTRNNumberLbl = view.findViewById(R.id.txtTRNNumberLbl);
        editTextTRNNumber = view.findViewById(R.id.editTextTRNNumber);
        txtGSTAckReceiptLbl = view.findViewById(R.id.txtGSTAckReceiptLbl);
        imgGSTAckReceipt = view.findViewById(R.id.imgGSTAckReceipt);
        txtDateOfApplicationGSTLbl = view.findViewById(R.id.txtDateOfApplicationGSTLbl);
        textViewDateOfApplicationGST = view.findViewById(R.id.textViewDateOfApplicationGST);
        txtExpectedDateOfGSTLbl = view.findViewById(R.id.txtExpectedDateOfGSTLbl);
        textViewExpectedDateOfGST = view.findViewById(R.id.textViewExpectedDateOfGST);
        textViewGSTINIssueDate = view.findViewById(R.id.textViewGSTINIssueDate);
        txtGSTINIssueDateLbl = view.findViewById(R.id.txtGSTINIssueDateLbl);

        gstDetailsDto = new GSTDetailsDto();

        setCompulsoryMarkLabel();

        CommonUtils.InputFiletrWithMaxLength(editTextAddressLine1, "~#^|$%&*!", 35);
        CommonUtils.InputFiletrWithMaxLength(editTextAddressLine2, "~#^|$%&*!", 35);
        CommonUtils.InputFiletrWithMaxLength(editTextLandmark, "~#^|$%&*!", 35);
        CommonUtils.InputFiletrWithMaxLength(editTextGstNumber, "~#^|$%&*!", 15);
        CommonUtils.InputFiletrWithMaxLength(editTextConfirmGstNumber, "~#^|$%&*!", 15);

        CommonUtils.AllCapCharCaptial(editTextGstEntityName);
        CommonUtils.AllCapCharCaptial(editTextTRNNumber);

        //Listener
        TextChangedListenerGSTIN_Number();

        imgGSTImage.setOnClickListener(this);
        imgGSTAckReceipt.setOnClickListener(this);
        textViewDateOfApplicationGST.setOnClickListener(this);
        textViewExpectedDateOfGST.setOnClickListener(this);
        textViewGSTINIssueDate.setOnClickListener(this);

        //Applied For GSTIN
        checkboxAppliedForGSTIN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                String status = isChecked ? "1" : "0";
                gstDetailsDto.setAppliedForGSTIN(status);

                if (isChecked) {
                    layoutAppliedGSTIN.setVisibility(View.VISIBLE);
                    layoutHavingGSTDetails.setVisibility(View.GONE);
                } else {
                    layoutAppliedGSTIN.setVisibility(View.GONE);
                    layoutHavingGSTDetails.setVisibility(View.VISIBLE);
                }
            }
        });

        return view;
    }

    //region TextChangedListener GSTIN Number
    private void TextChangedListenerGSTIN_Number() {

        editTextGstNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int len = editTextGstNumber.getText().toString().trim().length();
                if (len <= 0)
                    return;
            }

            @Override
            public void afterTextChanged(Editable editable) {

                String s = editable.toString();

                int len = editTextGstNumber.getText().toString().trim().length();
                if (len <= 0)
                    return;

                if (!s.equals(s.toUpperCase())) {
                    s = s.toUpperCase();
                    editTextGstNumber.setText(s);
                    editTextGstNumber.setSelection(editTextGstNumber.getText().length());

                }

                if (editTextGstNumber.getText().toString().contains(" ")) {
                    editTextGstNumber.setText(editTextGstNumber.getText().toString().replaceAll(" ", ""));
                    editTextGstNumber.setSelection(editTextGstNumber.getText().length());
                }


                if (!CommonUtils.GSTINisValid(s)) {
                    edittext_listener = true;
                } else {
                    edittext_listener = false;
                }

                //set color base on
                if (edittext_listener) {
                    editTextGstNumber.setTextColor(Color.parseColor("#FF0000"));
                    editTextGstNumber.setError(getResources().getString(R.string.hintGsTnumbernotValid));
                } else {
                    editTextGstNumber.setTextColor(Color.parseColor("#468847"));
                    editTextGstNumber.setError(null);

                    String strPass1 = editTextGstNumber.getText().toString();
                    String strPass2 = editTextConfirmGstNumber.getText().toString();

                    if (editTextGstNumber.length() < 1) {
                    } else if (!strPass1.equalsIgnoreCase(strPass2)) {
                        editTextGstNumber.setError("Not Match");
                        editTextGstNumber.setTextColor(Color.parseColor("#000000"));
                    } else {
                        editTextConfirmGstNumber.setTextColor(Color.parseColor("#468847"));
                        editTextConfirmGstNumber.setError(null);
                        editTextGstNumber.setTextColor(Color.parseColor("#468847"));
                        editTextGstNumber.setError(null);
                    }
                }
            }
        });

        editTextConfirmGstNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int len = editTextConfirmGstNumber.getText().toString().trim().length();
                if (len <= 0)
                    return;

                if (start != 15) {
                    editTextConfirmGstNumber.setTextColor(Color.parseColor("#000000"));
                    editTextConfirmGstNumber.setError(getResources().getString(R.string.hintGsTnumbernotValid));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s1 = editable.toString();

                if (!s1.equals(s1.toUpperCase())) {
                    s1 = s1.toUpperCase();
                    editTextConfirmGstNumber.setText(s1);
                    editTextConfirmGstNumber.setSelection(editTextConfirmGstNumber.getText().length());

                }

                if (editTextConfirmGstNumber.getText().toString().contains(" ")) {
                    editTextConfirmGstNumber.setText(editTextConfirmGstNumber.getText().toString().replaceAll(" ", ""));
                    editTextConfirmGstNumber.setSelection(editTextConfirmGstNumber.getText().length());
                }

                String strPass1 = editTextGstNumber.getText().toString();
                String strPass2 = editTextConfirmGstNumber.getText().toString();

                if (editTextConfirmGstNumber.length() < 1) {
                } else if (!strPass1.equalsIgnoreCase(strPass2)) {
                    editTextConfirmGstNumber.setError("Not Match");
                    editTextConfirmGstNumber.setTextColor(Color.parseColor("#000000"));
                } else if (!CommonUtils.GSTINisValid(s1)) {

                    editTextConfirmGstNumber.setTextColor(Color.parseColor("#FF0000"));
                    editTextConfirmGstNumber.setError(getResources().getString(R.string.hintGsTnumbernotValid));
                    editTextGstNumber.setTextColor(Color.parseColor("#FF0000"));
                    editTextGstNumber.setError(getResources().getString(R.string.hintGsTnumbernotValid));
                } else {
                    editTextConfirmGstNumber.setTextColor(Color.parseColor("#468847"));
                    editTextConfirmGstNumber.setError(null);
                    editTextGstNumber.setTextColor(Color.parseColor("#468847"));
                    editTextGstNumber.setError(null);

                }
            }
        });

    }
    //endregion

    public void reload(String data) {
        //Reload Data
        if (TextUtils.isEmpty(data))
            gstDetailsDto = new GSTDetailsDto();
        else {
            try {
                Gson gson = new GsonBuilder().create();
                List<GSTDetailsDto> applicationList = gson.fromJson(data, new TypeToken<ArrayList<GSTDetailsDto>>() {
                }.getType());
                if (applicationList.size() > 0) {
                    gstDetailsDto = applicationList.get(0);
                } else {
                    gstDetailsDto = new GSTDetailsDto();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        getAllSpinnerData = new GetAllSpinnerData(null, null, null);
        getAllSpinnerData.execute("STATE");
    }

    private void bindSpinner() {

        //STEP 1: Name of Entity
        editTextGstEntityName.setText(gstDetailsDto.getNameOfGSTINEntity());

        //STEP 2: GSTIN Number
        editTextGstNumber.setText(gstDetailsDto.getGstInNumber());

        //STEP 2.1: Confirm GSTIN Number
        editTextConfirmGstNumber.setText(gstDetailsDto.getGstInNumber());
        if (!TextUtils.isEmpty(gstDetailsDto.getGstInNumber()) && gstDetailsDto.getGstInNumber().length() == 15)
            checkboxAppliedForGSTIN.setVisibility(View.GONE);
        else
            checkboxAppliedForGSTIN.setVisibility(View.VISIBLE);

        //Issuing Date
        if (!TextUtils.isEmpty(gstDetailsDto.getGstinIssueDate())) {
            String formattedDate = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", gstDetailsDto.getGstinIssueDate());
            textViewGSTINIssueDate.setText(formattedDate);
        }

        //STEP 3.1: Address Line 1
        editTextAddressLine1.setText(gstDetailsDto.getGstInAddressLine1());

        //STEP 3.2: Address Line 2
        editTextAddressLine2.setText(gstDetailsDto.getGstInAddressLine2());

        //STEP 3.3: Landmark
        editTextLandmark.setText(gstDetailsDto.getLandmark());

        //STEP 4: Area
        edtArea.setText(gstDetailsDto.getArea());

        //STEP 5: Locality
        edtLocality.setText(gstDetailsDto.getLocality());

        //STEP 6: State
        setStateSpinnerAdapter(stateList, spinnerState, gstDetailsDto.getState());

        //STEP 7: Pin Code
        ediTextGSTPincode.setText(gstDetailsDto.getPincode());

        //STEP 8: GSTIN Certificate Image
        boolean IsGSTImgPDF = (gstDetailsDto.getGstinCertificateImgExt() != null && gstDetailsDto.getGstinCertificateImgExt().equalsIgnoreCase("pdf")) ? true : false;
        if (IsGSTImgPDF) {
            Glide.with(context)
                    .load(R.drawable.pdf)
                    .apply(new RequestOptions()
                            .error(R.drawable.pdf)
                            .placeholder(R.drawable.pdf)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgGSTImage);

        } else {
            if (!TextUtils.isEmpty(gstDetailsDto.getGstinCertificateImgId())) {
                String gstUrl = Constants.DownloadImageUrl + gstDetailsDto.getGstinCertificateImgId();
                Glide.with(context)
                        .load(gstUrl)
                        .apply(new RequestOptions()
                                .error(R.drawable.ic_camera_alt_black_72dp)
                                .placeholder(R.drawable.ic_camera_alt_black_72dp)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))
                        .into(imgGSTImage);
            }
        }

        //STEP 9: Application Reference Number(ARN)
        editTextTRNNumber.setText(gstDetailsDto.getTrnNumber());
        editTextTRNNumber.setSelection(editTextTRNNumber.getText().length());
        editTextTRNNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int len = editTextTRNNumber.getText().toString().trim().length();
                if (len <= 0)
                    return;

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int len = editTextTRNNumber.getText().toString().trim().length();
                if (len <= 0)
                    return;

                if (!CommonUtils.isValidARN(editTextTRNNumber.getText().toString().trim())) {
                    editTextTRNNumber.setTextColor(Color.parseColor("#FF0000"));
                    editTextTRNNumber.setError("Please enter valid Application Reference Number(ARN).");
                } else {

                    editTextTRNNumber.setTextColor(Color.parseColor("#468847"));
                    editTextTRNNumber.setError(null);
                }
            }
        });

        //STEP 10: Applied For GSTIN
        String isAppliedForGST = gstDetailsDto.getAppliedForGSTIN();
        if (!TextUtils.isEmpty(isAppliedForGST)) {
            int type = Integer.parseInt(isAppliedForGST);
            if (type == 1) {
                checkboxAppliedForGSTIN.setChecked(true);
                layoutAppliedGSTIN.setVisibility(View.VISIBLE);
                layoutHavingGSTDetails.setVisibility(View.GONE);
            } else {
                checkboxAppliedForGSTIN.setChecked(false);
                layoutAppliedGSTIN.setVisibility(View.GONE);
                layoutHavingGSTDetails.setVisibility(View.VISIBLE);
            }
        }

        //STEP 11: Date of Application of GST
        if (!TextUtils.isEmpty(gstDetailsDto.getGstApplicationDate())) {
            String gstAppDate = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", gstDetailsDto.getGstApplicationDate());
            textViewDateOfApplicationGST.setText(gstAppDate);
        }

        //STEP 12: Expected Date of Application of GST
        if (!TextUtils.isEmpty(gstDetailsDto.getExpectedDateOfGSTApplication())) {
            String expectedDate = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", gstDetailsDto.getExpectedDateOfGSTApplication());
            textViewExpectedDateOfGST.setText(expectedDate);
        }

        //STEP 13: GSTIN Acknowledgement Receipt
        if (!TextUtils.isEmpty(gstDetailsDto.getGstinAckReceiptImgId())) {
            String gstUrl = Constants.DownloadImageUrl + gstDetailsDto.getGstinAckReceiptImgId();
            Glide.with(context)
                    .load(gstUrl)
                    .apply(new RequestOptions()
                            .error(R.drawable.ic_camera_alt_black_72dp)
                            .placeholder(R.drawable.ic_camera_alt_black_72dp)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgGSTAckReceipt);
        }

        //Enable/disable views
        boolean IsEditable = (!TextUtils.isEmpty(gstDetailsDto.getIsEditable()) && gstDetailsDto.getIsEditable().equalsIgnoreCase("1")) ? true : false;
        checkboxAppliedForGSTIN.setEnabled(IsEditable);
        GUIUtils.setViewAndChildrenEnabled(layoutGSTDetailApplied, IsEditable);
        GUIUtils.setViewAndChildrenEnabled(layoutGSTDetail, IsEditable);
        ((DocumentManagerActivity) getActivity()).IsFooterLayoutVisible(IsEditable);


    }

    public void setCompulsoryMarkLabel() {
        TextView[] txtViewsForCompulsoryMark = {txtEntityNameLbl, txtGstinNumberLbl,txtGSTINIssueDateLbl, txtAddressLine1Lbl, txtStateLbl, txtDistrictLbl,
                txtVTCLbl, txtPinCodeLbl, txtGSTImageCertificateLbl, txtTRNNumberLbl, txtDateOfApplicationGSTLbl, txtExpectedDateOfGSTLbl, txtGSTAckReceiptLbl};
        GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);
    }

    @Override
    public void onPause() {
        super.onPause();
        permissionHandler.dismissSnackbar();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getAllSpinnerData != null && !getAllSpinnerData.isCancelled()) {
            getAllSpinnerData.cancel(true);
        }

    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();

        if (Id == R.id.imgGSTImageDoc) {
            FROM = TYPE_GST_CERTIFICATE;
            showImgOrPDF(view);

        } else if (Id == R.id.imgGSTAckReceipt) {
            FROM = TYPE_GST_TRN_RECEIPT;
            if ((TextUtils.isEmpty(gstDetailsDto.getGstinAckReceiptImgId()) || gstDetailsDto.getGstinAckReceiptImgId().equalsIgnoreCase("0")) && TextUtils.isEmpty(gstDetailsDto.getGstinAckReceiptImgBase64())) {
                FILE_TYPE_FROM = TRN_ACK_IMG_COPY;
                SEL_FILE_TYPE = " images";
                startScanCamera();
            } else {
                GSTINDTO previewDto = prepareDtoForPreview(FROM);
                showImagePreviewDialog(previewDto);
            }
        } else if (Id == R.id.textViewDateOfApplicationGST) {
            selectedDateTimeId = view.getId();
            showDateTimeDialogPicker();

        } else if (Id == R.id.textViewExpectedDateOfGST) {
            selectedDateTimeId = view.getId();
            showDateTimeDialogPicker();

        } else if (Id == R.id.textViewGSTINIssueDate) {

            selectedDateTimeId = view.getId();
            showDateTimeDialogPicker();
        }
    }

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
                    String base64Data = CommonUtils.encodeFileToBase64Binary(file);

                    gstDetailsDto.setGstinCertificateImgBase64(base64Data);
                    gstDetailsDto.setGstinCertificateImgExt(ext);
                    gstDetailsDto.setChangedPhoto(true);

                    Glide.with(context).asDrawable().load(context.getResources().getDrawable(R.drawable.pdf)).into(imgGSTImage);

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
                //setImageAndName(fileName, base64Data, uri);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setImageData(Uri imageUri, String imageBase64, Bitmap bitmapNew) {

        GSTINDTO imageDto = new GSTINDTO();
        imageDto.setUri(imageUri);
        imageDto.setBitmap(bitmapNew);
        imageDto.setChangedPhoto(true);
        imageDto.setGstImage(imageBase64);

        showImagePreviewDialog((Object) imageDto);
    }

    public void setImageAndName(String fileName, String base64Data, Uri uri) {
        try {
            Bitmap imageBitMap = null;
            boolean IsDrawable = false;
            String ext = "jpg";     //FileUtils.getFileExtension(context, uri);
            if (ext.equalsIgnoreCase("pdf"))
                IsDrawable = true;
            else
                imageBitMap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);

            setScanCopyData(IsDrawable, imageBitMap, base64Data, ext);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setScanCopyData(boolean IsDrawable, Bitmap bitmap, String base64, String ext) {

        switch (FILE_TYPE_FROM) {

            case GSTIN_CERTIFICATE_IMG_COPY:
                if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgGSTImage);
                else {
                    try {
                        bitmap = ImageUtils.rotateImageIfRequired(getActivity().getContentResolver(), bitmap, picUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Glide.with(context).asBitmap().load(bitmap).into(imgGSTImage);
                }

                gstDetailsDto.setGstinCertificateImgBase64(base64);
                gstDetailsDto.setGstinCertificateImgExt(ext);
                break;

            case TRN_ACK_IMG_COPY:
                if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgGSTAckReceipt);
                else {
                    try {
                        bitmap = ImageUtils.rotateImageIfRequired(getActivity().getContentResolver(), bitmap, picUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Glide.with(context).asBitmap().load(bitmap).into(imgGSTAckReceipt);
                }

                gstDetailsDto.setGstinAckReceiptImgBase64(base64);
                gstDetailsDto.setGstinAckReceiptImgExt(ext);
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
                            FROM = TYPE_GST_CERTIFICATE;

                            String ext = TextUtils.isEmpty(gstDetailsDto.getGstinCertificateImgExt()) ? "jpg" : gstDetailsDto.getGstinCertificateImgExt();

                            //FOR PDF
                            if (ext.equalsIgnoreCase("pdf")) {
                                FILE_TYPE_FROM = GSTIN_CERTIFICATE_IMG_COPY;
                                SEL_FILE_TYPE = " images";
                                startScanCamera();
                                return;
                            }

                            //FOR Image
                            if ((TextUtils.isEmpty(gstDetailsDto.getGstinCertificateImgId()) || gstDetailsDto.getGstinCertificateImgId().equalsIgnoreCase("0")) && TextUtils.isEmpty(gstDetailsDto.getGstinCertificateImgBase64())) {
                                FILE_TYPE_FROM = GSTIN_CERTIFICATE_IMG_COPY;
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
                            FilteredFilePickerActivity.FILE_TYPE = "pdf";

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

    //region Validation
    public int IsGSTDetailsValidated() {

        if (!TextUtils.isEmpty(gstDetailsDto.getAppliedForGSTIN()) && gstDetailsDto.getAppliedForGSTIN().equalsIgnoreCase("1")) {
            return IsAppliedForGSTDetailsValidated();
        }

        return IsHavingGSTDetailsValidated();
    }

    public int IsHavingGSTDetailsValidated() {

        //Step 1: Validate GST Name
        gstDetailsDto.setNameOfGSTINEntity(editTextGstEntityName.getText().toString().trim());
        if (TextUtils.isEmpty(gstDetailsDto.getNameOfGSTINEntity())) {
            Toast.makeText(context, "Please enter Name of Entity on GSTIN Certificate.", Toast.LENGTH_LONG).show();
            editTextGstEntityName.setError("Please enter Name of Entity on GSTIN Certificate.");
            return 1;
        }

        //Step 2: Validate GST Number
        gstDetailsDto.setGstInNumber(editTextGstNumber.getText().toString().trim());
        if (TextUtils.isEmpty(gstDetailsDto.getGstInNumber()) || !CommonUtils.GSTINisValid(editTextGstNumber.getText().toString())) {
            Toast.makeText(context, "Please enter valid GSTIN Number.", Toast.LENGTH_LONG).show();
            editTextGstNumber.setError("Please enter valid GSTIN Number.");
            return 1;
        }

        //STEP 2.1: Confirm GSTIN Number
        if (TextUtils.isEmpty(gstDetailsDto.getGstInNumber()) || !CommonUtils.GSTINisValid(editTextConfirmGstNumber.getText().toString())) {
            Toast.makeText(context, "Please Confirm GSTIN Number.", Toast.LENGTH_LONG).show();
            editTextConfirmGstNumber.setError("Please Confirm GSTIN Number.");
            return 1;
        }

        //Issuing Date
        if (TextUtils.isEmpty(gstDetailsDto.getGstinIssueDate())) {
            Toast.makeText(context, "Please select GSTIN Issue Date.", Toast.LENGTH_LONG).show();
            textViewGSTINIssueDate.setError("Please select GSTIN Issue Date.");
            return 1;
        }

        //Step 3.1: Address Line 1
        gstDetailsDto.setGstInAddressLine1(editTextAddressLine1.getText().toString().trim());
        if (TextUtils.isEmpty(gstDetailsDto.getGstInAddressLine1())) {
            Toast.makeText(context, "Please enter Address Line 1.", Toast.LENGTH_LONG).show();
            editTextAddressLine1.setError("Please enter Address Line 1.");
            return 1;
        }

        //Step 3.2: Address Line 2
        gstDetailsDto.setGstInAddressLine2(editTextAddressLine2.getText().toString().trim());
        if (TextUtils.isEmpty(gstDetailsDto.getGstInAddressLine2())) {
            Toast.makeText(context, "Please enter Address Line 2.", Toast.LENGTH_LONG).show();
            editTextAddressLine2.setError("Please enter Address Line 2.");
            return 1;
        }

        //STEP 4: State
        if (TextUtils.isEmpty(gstDetailsDto.getState()) || gstDetailsDto.getState().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select State.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerState, "Please select State.", context);
            return 1;
        }

        //STEP 5: District
        if (TextUtils.isEmpty(gstDetailsDto.getDistrict()) || gstDetailsDto.getDistrict().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select District.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerDistrict, "Please select District.", context);
            return 1;
        }

        //STEP 6: VTC
        if (TextUtils.isEmpty(gstDetailsDto.getVtc()) || gstDetailsDto.getVtc().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select VTC.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerVTC, "Please select VTC.", context);
            return 1;
        }

        //STEP 8: Pin code
        gstDetailsDto.setPincode(ediTextGSTPincode.getText().toString().trim());
        if (TextUtils.isEmpty(gstDetailsDto.getPincode())) {
            Toast.makeText(context, "Please enter Pin code.", Toast.LENGTH_LONG).show();
            ediTextGSTPincode.setError("Please enter Pin code.");
            return 1;
        }

        //STEP 9: GSTIN Certificate Image
        if (TextUtils.isEmpty(gstDetailsDto.getGstinCertificateImgId()) || gstDetailsDto.getGstinCertificateImgId().equalsIgnoreCase("0")) {
            if (TextUtils.isEmpty(gstDetailsDto.getGstinCertificateImgBase64())) {
                showMessage("Please add GSTIN Certificate Image.");
                return 1;
            }
        }
        return 0;
    }

    public int IsAppliedForGSTDetailsValidated() {

        //Step 1: Validate Application Reference Number(ARN)
        gstDetailsDto.setTrnNumber(editTextTRNNumber.getText().toString().trim());
        if (TextUtils.isEmpty(gstDetailsDto.getTrnNumber()) || !CommonUtils.isValidARN(editTextTRNNumber.getText().toString())) {
            //if (TextUtils.isEmpty(gstDetailsDto.getTrnNumber())) {
            Toast.makeText(context, "Please enter valid Application Reference Number(ARN).", Toast.LENGTH_LONG).show();
            editTextTRNNumber.setError("Please enter valid Application Reference Number(ARN).");
            return 1;
        }

        int len = editTextTRNNumber.getText().toString().length();
        if (len < 15) {
            Toast.makeText(context, "Application Reference Number(ARN) should be 15 characters long.", Toast.LENGTH_LONG).show();
            editTextTRNNumber.setError("Application Reference Number(ARN) should be 15 characters long.");
            return 8;
        }

        //STEP 2: Date Of GSTIN Application
        if (TextUtils.isEmpty(gstDetailsDto.getGstApplicationDate())) {
            Toast.makeText(context, "Please select Date Of GSTIN Application.", Toast.LENGTH_LONG).show();
            textViewDateOfApplicationGST.setError("Please select Date Of GSTIN Application.");
            return 15;
        }

        //STEP 3: Expected Date Of GSTIN Application
        if (TextUtils.isEmpty(gstDetailsDto.getExpectedDateOfGSTApplication())) {
            Toast.makeText(context, "Please select Expected Date Of GSTIN Application.", Toast.LENGTH_LONG).show();
            textViewExpectedDateOfGST.setError("Please select Expected Date Of GSTIN Application.");
            return 15;
        }

        //STEP 4: GSTIN Acknowledgement Image
        if (TextUtils.isEmpty(gstDetailsDto.getGstinAckReceiptImgId()) || gstDetailsDto.getGstinAckReceiptImgId().equalsIgnoreCase("0")) {
            if (TextUtils.isEmpty(gstDetailsDto.getGstinAckReceiptImgBase64())) {
                AlertDialogBoxInfo.alertDialogShow(context, "Please add GSTIN Acknowledgement Receipt Image.");
                return 1;
            }
        }
        return 0;
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

    //region Set VTC Adapter
    private void setVTCSpinnerAdapter(List<CustomFranchiseeApplicationSpinnerDto> VTCList, Spinner spinnerVTC, String selVTCId) {

        //spinnerVTC.setEnabled(true);
        spinnerVTC.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, VTCList));
        int appsstatePos = documentManagerRepo.getSelectedPos(VTCList, selVTCId);
        spinnerVTC.setSelection(appsstatePos);
        spinnerVTC.setOnItemSelectedListener(this);
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
                    gstDetailsDto.setState(stateDto.getId());

                    //Get Division-District
                    getAllSpinnerData = new GetAllSpinnerData(gstDetailsDto.getState(), null, null);
                    getAllSpinnerData.execute("DISTRICT");
                }
            } else {
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));
                gstDetailsDto.setState("0");

                //DISTRICT
                setDistrictSpinnerAdapter(list1, spinnerDistrict, null);
                gstDetailsDto.setDistrict(null);

                //VTC
                setVTCSpinnerAdapter(list1, spinnerVTC, null);
                gstDetailsDto.setVtc(null);

            }
        } else if (Id == R.id.spinnerDistrict) {
            if (position > 0) {
                spinnerDistrict.setTitle("Select District");
                spinnerDistrict.requestFocus();
                CustomFranchiseeApplicationSpinnerDto disDto = (CustomFranchiseeApplicationSpinnerDto) spinnerDistrict.getItemAtPosition(position);
                if (!disDto.getId().equals("0")) {
                    gstDetailsDto.setDistrict(disDto.getId());

                    //Get BLOCK
                    getAllSpinnerData = new GetAllSpinnerData(gstDetailsDto.getState(), gstDetailsDto.getDistrict(), null);
                    getAllSpinnerData.execute("VTC");

                }
            } else {
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));
                gstDetailsDto.setDistrict("0");

                //VTC
                setVTCSpinnerAdapter(list1, spinnerVTC, null);
                gstDetailsDto.setVtc(null);

            }
        } else if (Id == R.id.spinnerVTC) {
            if (position > 0) {
                spinnerVTC.setTitle("Select VTC");
                spinnerVTC.requestFocus();
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerVTC.getItemAtPosition(position);
                gstDetailsDto.setVtc(entityDto.getId());

            } else {
                gstDetailsDto.setVtc("0");
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
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

                case "VTC":
                    vtcList = documentManagerRepo.getVTCList(vkIdOrEnquiryId, stateId, districtId);
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
                    bindSpinner();
                    break;

                case "DISTRICT":
                    setDistrictSpinnerAdapter(districtList, spinnerDistrict, gstDetailsDto.getDistrict());
                    break;

                case "VTC":
                    setVTCSpinnerAdapter(vtcList, spinnerVTC, gstDetailsDto.getVtc());
                    break;

                default:
                    break;
            }
        }
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

    public GSTDetailsDto getGSTDetailsDto() {

        if (gstDetailsDto == null)
            return null;

        gstDetailsDto.setNameOfGSTINEntity(editTextGstEntityName.getText().toString());
        gstDetailsDto.setGstInNumber(editTextGstNumber.getText().toString());
        gstDetailsDto.setGstInAddressLine1(editTextAddressLine1.getText().toString());
        gstDetailsDto.setGstInAddressLine2(editTextAddressLine2.getText().toString());
        gstDetailsDto.setLandmark(editTextLandmark.getText().toString());
        gstDetailsDto.setArea(edtArea.getText().toString());
        gstDetailsDto.setLocality(edtLocality.getText().toString());
        gstDetailsDto.setPincode(ediTextGSTPincode.getText().toString());
        gstDetailsDto.setTrnNumber(editTextTRNNumber.getText().toString());

        return gstDetailsDto;
    }

    private void showDateTimeDialogPicker() {

        Date defaultDate = null;
        if (selectedDateTimeId == R.id.textViewDateOfApplicationGST) {
            defaultDate = gstApplicationDate;
        } else if (selectedDateTimeId == R.id.textViewExpectedDateOfGST) {
            defaultDate = expectedGstApplicationDate;
        } else if(selectedDateTimeId == R.id.textViewGSTINIssueDate) {
            defaultDate = gstIssueDate;
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

                        if (selectedDateTimeId == R.id.textViewDateOfApplicationGST) {
                            gstApplicationDate = datetime;
                            strGstApplicationDate = formateYMD;
                            gstDetailsDto.setGstApplicationDate(strGstApplicationDate);

                            String DOB = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", gstDetailsDto.getGstApplicationDate());
                            textViewDateOfApplicationGST.setText(DOB);
                            textViewDateOfApplicationGST.setError(null);

                        } else if (selectedDateTimeId == R.id.textViewExpectedDateOfGST) {
                            expectedGstApplicationDate = datetime;
                            strExpectedGstApplicationDate = formateYMD;
                            gstDetailsDto.setExpectedDateOfGSTApplication(strExpectedGstApplicationDate);

                            String DOB = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", gstDetailsDto.getExpectedDateOfGSTApplication());
                            textViewExpectedDateOfGST.setText(DOB);
                            textViewExpectedDateOfGST.setError(null);

                        } else if (selectedDateTimeId == R.id.textViewGSTINIssueDate) {
                            gstIssueDate = datetime;
                            strGstIssueDate = formateYMD;
                            gstDetailsDto.setGstinIssueDate(strGstIssueDate);

                            String issuingDate = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", gstDetailsDto.getGstinIssueDate());
                            textViewGSTINIssueDate.setText(issuingDate);
                            textViewGSTINIssueDate.setError(null);

                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //Last 30 days from Today
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -30);
        long end = calendar.getTimeInMillis();//Set Min and Max Date

        //Today's Date
        long now = new Date().getTime() - 1000;

        if (selectedDateTimeId == R.id.textViewDateOfApplicationGST) {
            dateTimePickerDialog.setMinDate(end);
            dateTimePickerDialog.setMaxDate(now);

        } else if (selectedDateTimeId == R.id.textViewExpectedDateOfGST) {
            dateTimePickerDialog.setMinDate(now);
            // dateTimePickerDialog.setMaxDate(curCal.getTimeInMillis());

        } else  if (selectedDateTimeId == R.id.textViewGSTINIssueDate) {
            Calendar c = Calendar.getInstance();
            c.set(1920, 0, 1);//Year,Month -1,Day
            dateTimePickerDialog.setMinDate(c.getTimeInMillis());
            dateTimePickerDialog.setMaxDate(now);
        }

        dateTimePickerDialog.setCancelable(false);
        dateTimePickerDialog.setActionButtonName("Save");
        dateTimePickerDialog.show();

    }

    private void refreshGSTDetailsImg(GSTINDTO imageDto) {

        if (FROM == TYPE_GST_CERTIFICATE) {

            gstDetailsDto.setChangedPhoto(imageDto.isChangedPhoto());
            gstDetailsDto.setGstinCertificateImgId(imageDto.getGstImageId());
            gstDetailsDto.setBitmap(imageDto.getBitmap());
            gstDetailsDto.setGstinCertificateImgExt("jpg");
            gstDetailsDto.setName("");
            gstDetailsDto.setGstinCertificateImgBase64(imageDto.getGstImage());

        } else if (FROM == TYPE_GST_TRN_RECEIPT) {

            gstDetailsDto.setReceiptImgChanged(imageDto.isChangedPhoto());
            gstDetailsDto.setGstinAckReceiptImgId(imageDto.getGstImageId());
            gstDetailsDto.setBitmapReceipt(imageDto.getBitmap());
            gstDetailsDto.setReceiptName("");
            gstDetailsDto.setGstinAckReceiptImgBase64(imageDto.getGstImage());

        }
    }

    private GSTINDTO prepareDtoForPreview(int FROM) {
        GSTINDTO imageDto = new GSTINDTO();

        if (FROM == TYPE_GST_CERTIFICATE) {

            imageDto.setChangedPhoto(gstDetailsDto.isChangedPhoto());
            imageDto.setGstImageId(gstDetailsDto.getGstinCertificateImgId());
            imageDto.setBitmap(gstDetailsDto.getBitmap());
            imageDto.setName("");
            imageDto.setGstImage(gstDetailsDto.getGstinCertificateImgBase64());

        } else if (FROM == TYPE_GST_TRN_RECEIPT) {

            imageDto.setChangedPhoto(gstDetailsDto.isReceiptImgChanged());
            imageDto.setGstImageId(gstDetailsDto.getGstinAckReceiptImgId());
            imageDto.setBitmap(gstDetailsDto.getBitmapReceipt());
            imageDto.setName("");
            imageDto.setGstImage(gstDetailsDto.getGstinAckReceiptImgBase64());

        }
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

                    //Refresh GST Image
                    if (FROM == TYPE_GST_CERTIFICATE) {
                        if (!TextUtils.isEmpty(gstDetailsDto.getGstinCertificateImgBase64())) {
                            Bitmap bitmap = CommonUtils.StringToBitMap(gstDetailsDto.getGstinCertificateImgBase64());
                            if (bitmap != null)
                                imgGSTImage.setImageBitmap(bitmap);
                        }
                    } else if (FROM == TYPE_GST_TRN_RECEIPT) {
                        //Refresh Receipt Image
                        if (!TextUtils.isEmpty(gstDetailsDto.getGstinAckReceiptImgBase64())) {
                            Bitmap bitmap = CommonUtils.StringToBitMap(gstDetailsDto.getGstinAckReceiptImgBase64());
                            if (bitmap != null)
                                imgGSTAckReceipt.setImageBitmap(bitmap);
                        }
                    }
                }
            });
            customImagePreviewDialog.show();
            customImagePreviewDialog.setCancelable(false);

            //Title
            String title = FROM == TYPE_GST_CERTIFICATE ? context.getString(R.string.gstin_certificate_img) : context.getString(R.string.gstin_ack_receipt_img);
            customImagePreviewDialog.setDialogTitle(title);

            //Change Photo Allowed
            boolean IsEditable = (!TextUtils.isEmpty(gstDetailsDto.getIsEditable()) && gstDetailsDto.getIsEditable().equalsIgnoreCase("1")) ? true : false;
            customImagePreviewDialog.allowChangePhoto(IsEditable);
            customImagePreviewDialog.allowSaveOption(IsEditable);
            customImagePreviewDialog.allowRemarks(false);

        } else {
            Toast.makeText(context, "No photo available to preview.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showImgOrPDF(final View view) {
        FROM = TYPE_GST_CERTIFICATE;

        boolean IsGSTImgPDF = (gstDetailsDto.getGstinCertificateImgExt() != null && gstDetailsDto.getGstinCertificateImgExt().equalsIgnoreCase("pdf")) ? true : false;
        boolean IsEditable = (!TextUtils.isEmpty(gstDetailsDto.getIsEditable()) && gstDetailsDto.getIsEditable().equalsIgnoreCase("1")) ? true : false;

        if (IsEditable) {
            showAttachmentDialog(view);

        } else {

            //PDF - No Preview for PDF

            //Image
            if (!IsGSTImgPDF) {
                GSTINDTO previewDto = prepareDtoForPreview(FROM);
                showImagePreviewDialog(previewDto);
                return;
            }
        }
    }

}
