package in.vakrangee.franchisee.bcadetails.pre.personal;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nononsenseapps.filepicker.Utils;

import java.io.File;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import in.vakrangee.franchisee.bcadetails.BCAEntryDetailsRepository;
import in.vakrangee.franchisee.bcadetails.R;
import in.vakrangee.franchisee.bcadetails.pre.AsyncPanCardForBCAValidation;
import in.vakrangee.franchisee.bcadetails.pre.FADataDto;
import in.vakrangee.franchisee.bcadetails.pre.PreBCADataEntryDetailsFragment;
import in.vakrangee.supercore.franchisee.application.VakrangeeKendraApplication;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.commongui.DateTimePickerDialog;
import in.vakrangee.supercore.franchisee.commongui.FileAttachmentDialog;
import in.vakrangee.supercore.franchisee.commongui.FileUtils;
import in.vakrangee.supercore.franchisee.commongui.FilteredFilePickerActivity;
import in.vakrangee.supercore.franchisee.commongui.MonthYearPickerDialog;
import in.vakrangee.supercore.franchisee.nextgenfranchiseeapplication.CustomFranchiseeApplicationSpinnerDto;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.GUIUtils;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;
import in.vakrangee.supercore.franchisee.utils.ImageZipper;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;
import in.vakrangee.supercore.franchisee.utils.CustomSearchableSpinner;

public class PreBCAPersonalDetailsFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private View view;
    private Context context;
    private DeprecateHandler deprecateHandler;
    private PermissionHandler permissionHandler;

    private PreBCAPersonalDetailsDto preBCAPersonalDetailsDto;
    private boolean IsEditable = false;
    private GetAllSpinnerData getAllSpinnerData = null;

    private BCAEntryDetailsRepository bcaEntryDetailsRepo;
    private List<CustomFranchiseeApplicationSpinnerDto> salutationList, qualificationList,
            stateList, districtList, blockList, vtcList, bankNameList, branchName1List, branchName2List, branchName3List, approxDistance1List, approxDistance2List, approxDistance3List;

    private FileAttachmentDialog fileAttachementDialog;
    private static final int CAMERA_PIC_REQUEST = 100;
    private static final int BROWSE_FOLDER_REQUEST = 101;
    private Uri picUri;                 //Picture URI
    private String SEL_FILE_TYPE;
    private int FROM = -1;
    private static final int PAN_CARD_SCAN_COPY = 2;
    private static final int BCA_POLICE_VERIFICATION_COPY = 6;
    private DateTimePickerDialog dateTimePickerDialog;
    private DateFormat dateFormatter2 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    private DateFormat dateFormatterYMD = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private Date yearOfPassingDate;
    private String strYearOfPassingDate;
    private Date dateOfPassingDate;
    private String strDateOfPassing;
    private Date dOBDate;
    private String strDOB;
    private Date cibilReportDate;
    private String strCibilReportDate;
    private Date bcaPoliceDate;
    private String strBcaPoliceDate;
    private int selectedDateTimeId = 0;

    private static final int BCA_PASSPORT_PHOTO = 1;

    private MonthYearPickerDialog monthYearPickerDialog = null;
    private String getDateConvert;

    private TextView txtBCANameLbl, txtBCAFatherNameLbl, txtBCAMobileNumberLbl, txtEmailIdLbl,
            txtGenderLbl, txtMaritalStatusLbl, txtSpouseNameLbl, txtBCADOBLbl, txtQualificationLbl,
            txtYearOfPassingLbl, txtAddressLine1Lbl, txtAreaLbl, txtLocalityLbl, txtStateLbl,
            txtDistrictLbl, txtTehsilLbl, txtVTCLbl, txtPincodeLbl, txtCIBILScoreLbl, txtCIBILReportDateLbl, txtNameAsPerPANCardLbl,
            txtBCAPoliceVerificationLbl, txtPANNumberLbl, txtPanCardScanCopyLbl;

    private TextView textViewBCADOB, textViewYearOfPassing, textViewDateOfPassing, textViewCIBILReportDate, textViewBCAPoliceVerificationDate;
    private CheckBox checkboxSameAsFranchisee;
    private Spinner spinnerBCASalutation, spinnerBCAFatherSalutation, spinnerSpouseSalutation, spinnerQualification;
    private EditText editTextBCAFirstName, editTextBCAMiddleName, editTextBCALastName,
            editTextBCAFatherFirstName, editTextBCAFatherMiddleName, editTextBCAFatherLastName,
            editTextBCAMobileNo, editTextBCAALternateMobileNo, editTextEmailID,
            editTextSpouseFirstName, editTextSpouseMiddleName, editTextSpouseLastName,
            editTextAddressLine1, editTextAddressLine2, editTextLandmark, editTextArea, editTextLocality, editTextPincode, editTextCIBILScore,
            editTextNameAsPerPANCard, editTextPANNumber1, editTextPANNumber2;

    private RadioGroup radioGroupGender, radioGroupMaritalStatus;
    private CustomSearchableSpinner spinnerState, spinnerDistrict, spinnerTehsil, spinnerVTC;
    private ImageView imgBCAPoliceVerification, imgPanScanCopy;
    private LinearLayout layoutNameAsPerPANCard, layoutPANNumber, layoutSameAsFranchisee, layoutIsPancardValid, layoutSpouseName, layoutCIBILBCAPolice, layoutBCAGroup1, layoutBCAGroup2, layoutBCAGroup3;
    private boolean edittext_listener = true;

    private TextView txtBankName1Lbl, txtBankName2Lbl, txtBankName3Lbl;
    private CustomSearchableSpinner spinnerBankName1, spinnerBankName2, spinnerBankName3;
    private TextView txtBranchName1Lbl, txtBranchName2Lbl, txtBranchName3Lbl;
    private CustomSearchableSpinner spinnerBranchName1, spinnerBranchName2, spinnerBranchName3;
    private TextView txtApproxDistance1Lbl, txtApproxDistance2Lbl, txtApproxDistance3Lbl, txtIsPanCardValid,
            txtSelectedBranchName1, txtSelectedBranchName2, txtSelectedBranchName3;
    private CustomSearchableSpinner spinnerApproxDistance1, spinnerApproxDistance2, spinnerApproxDistance3;
    private static final String ENTER_PAN_NUMBER = "Please enter PAN Number.";
    private static final String COLOR_000000 = "#000000";
    private static final String NOT_MATCH = "Not Match";
    private static final String ENTER_PAN_NAME = "Please enter Name as per PAN Card.";
    private AsyncPanCardForBCAValidation asyncPanCardForBCAValidation = null;
    private boolean IsPanValidated = false;
    private String vStateCode, vDistrictCode;

    public PreBCAPersonalDetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag_pre_bca_personal_details, container, false);

        bindViewId(view);

        //Initialize data
        this.context = getContext();
        deprecateHandler = new DeprecateHandler(context);
        permissionHandler = new PermissionHandler(getActivity());
        bcaEntryDetailsRepo = new BCAEntryDetailsRepository(context);

        setCompulsoryMarkLabel();

        //Input filter
        CommonUtils.InputFiletrWithMaxLength(editTextEmailID, "~#^|$%&*!", 50);
        CommonUtils.InputFiletrWithMaxLength(editTextNameAsPerPANCard, "~#^|$%&*!", 50);
        CommonUtils.InputFiletrWithMaxLength(editTextPANNumber1, "~#^|$%&*!", 10);
        CommonUtils.InputFiletrWithMaxLength(editTextPANNumber2, "~#^|$%&*!", 10);

        //Mobile No
        editTextBCAMobileNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int len = editTextBCAMobileNo.getText().toString().trim().length();
                if (len <= 0)
                    return;

                if (i != 10) {
                    editTextBCAMobileNo.setTextColor(Color.parseColor("#000000"));
                    editTextBCAMobileNo.setError(getResources().getString(R.string.EnterMob));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                if (editTextBCAMobileNo.getText().toString().contains(" ")) {
                    editTextBCAMobileNo.setText(editTextBCAMobileNo.getText().toString().replaceAll(" ", ""));
                    editTextBCAMobileNo.setSelection(editTextBCAMobileNo.getText().length());
                }

                if (editTextBCAMobileNo.length() >= 10) {
                    editTextBCAMobileNo.setTextColor(Color.parseColor("#468847"));
                    editTextBCAMobileNo.setError(null);
                }
            }
        });

        TextChangedListenerPANcard();

        //Pincode
        editTextPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int len = editTextPincode.getText().toString().trim().length();
                if (len <= 0)
                    return;

                if (i != 6) {
                    editTextPincode.setTextColor(Color.parseColor("#000000"));
                    editTextPincode.setError(getResources().getString(R.string.EnterPincode));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                if (editTextPincode.getText().toString().contains(" ")) {
                    editTextPincode.setText(editTextPincode.getText().toString().replaceAll(" ", ""));
                    editTextPincode.setSelection(editTextPincode.getText().length());
                }

                if (editTextPincode.length() >= 6) {
                    editTextPincode.setTextColor(Color.parseColor("#468847"));
                    editTextPincode.setError(null);

                }
            }
        });

        return view;
    }

    private void bindViewId(View view) {

        txtSelectedBranchName1 = view.findViewById(R.id.txtSelectedBranchName1);
        txtSelectedBranchName2 = view.findViewById(R.id.txtSelectedBranchName2);
        txtSelectedBranchName3 = view.findViewById(R.id.txtSelectedBranchName3);
        layoutIsPancardValid = view.findViewById(R.id.layoutIsPancardValid);
        layoutPANNumber = view.findViewById(R.id.layoutPANNumber);
        txtIsPanCardValid = view.findViewById(R.id.txtIsPanCardValid);
        layoutSameAsFranchisee = view.findViewById(R.id.layoutSameAsFranchisee);
        layoutBCAGroup1 = view.findViewById(R.id.layoutBCAGroup1);
        layoutBCAGroup2 = view.findViewById(R.id.layoutBCAGroup2);
        layoutBCAGroup3 = view.findViewById(R.id.layoutBCAGroup3);
        layoutSpouseName = view.findViewById(R.id.layoutSpouseName);
        layoutNameAsPerPANCard = view.findViewById(R.id.layoutNameAsPerPANCard);
        txtBCANameLbl = view.findViewById(R.id.txtBCANameLbl);
        txtBCAFatherNameLbl = view.findViewById(R.id.txtBCAFatherNameLbl);
        txtBCAMobileNumberLbl = view.findViewById(R.id.txtBCAMobileNumberLbl);
        txtEmailIdLbl = view.findViewById(R.id.txtEmailIdLbl);
        txtGenderLbl = view.findViewById(R.id.txtGenderLbl);
        txtMaritalStatusLbl = view.findViewById(R.id.txtMaritalStatusLbl);
        txtSpouseNameLbl = view.findViewById(R.id.txtSpouseNameLbl);
        txtBCADOBLbl = view.findViewById(R.id.txtBCADOBLbl);
        txtQualificationLbl = view.findViewById(R.id.txtQualificationLbl);
        txtYearOfPassingLbl = view.findViewById(R.id.txtYearOfPassingLbl);
        txtAddressLine1Lbl = view.findViewById(R.id.txtAddressLine1Lbl);
        txtAreaLbl = view.findViewById(R.id.txtAreaLbl);
        txtLocalityLbl = view.findViewById(R.id.txtLocalityLbl);
        txtStateLbl = view.findViewById(R.id.txtStateLbl);
        txtDistrictLbl = view.findViewById(R.id.txtDistrictLbl);
        txtTehsilLbl = view.findViewById(R.id.txtTehsilLbl);
        txtVTCLbl = view.findViewById(R.id.txtVTCLbl);
        txtPincodeLbl = view.findViewById(R.id.txtPincodeLbl);
        txtCIBILScoreLbl = view.findViewById(R.id.txtCIBILScoreLbl);
        txtCIBILReportDateLbl = view.findViewById(R.id.txtCIBILReportDateLbl);
        txtNameAsPerPANCardLbl = view.findViewById(R.id.txtNameAsPerPANCardLbl);
        txtBCAPoliceVerificationLbl = view.findViewById(R.id.txtBCAPoliceVerificationLbl);
        txtPANNumberLbl = view.findViewById(R.id.txtPANNumberLbl);
        txtPanCardScanCopyLbl = view.findViewById(R.id.txtPanCardScanCopyLbl);

        textViewCIBILReportDate = view.findViewById(R.id.textViewCIBILReportDate);
        textViewBCAPoliceVerificationDate = view.findViewById(R.id.textViewBCAPoliceVerificationDate);
        textViewDateOfPassing = view.findViewById(R.id.textViewDateOfPassing);
        textViewYearOfPassing = view.findViewById(R.id.textViewYearOfPassing);
        textViewBCADOB = view.findViewById(R.id.textViewBCADOB);
        checkboxSameAsFranchisee = view.findViewById(R.id.checkboxSameAsFranchisee);
        spinnerBCASalutation = view.findViewById(R.id.spinnerBCASalutation);
        editTextBCAFirstName = view.findViewById(R.id.editTextBCAFirstName);
        editTextBCAMiddleName = view.findViewById(R.id.editTextBCAMiddleName);
        editTextBCALastName = view.findViewById(R.id.editTextBCALastName);
        spinnerBCAFatherSalutation = view.findViewById(R.id.spinnerBCAFatherSalutation);
        editTextBCAFatherFirstName = view.findViewById(R.id.editTextBCAFatherFirstName);
        editTextBCAFatherMiddleName = view.findViewById(R.id.editTextBCAFatherMiddleName);
        editTextBCAFatherLastName = view.findViewById(R.id.editTextBCAFatherLastName);
        editTextBCAMobileNo = view.findViewById(R.id.editTextBCAMobileNo);
        editTextBCAALternateMobileNo = view.findViewById(R.id.editTextBCAALternateMobileNo);
        editTextEmailID = view.findViewById(R.id.editTextEmailID);
        radioGroupGender = view.findViewById(R.id.radioGroupGender);
        radioGroupMaritalStatus = view.findViewById(R.id.radioGroupMaritalStatus);
        spinnerSpouseSalutation = view.findViewById(R.id.spinnerSpouseSalutation);
        editTextSpouseFirstName = view.findViewById(R.id.editTextSpouseFirstName);
        editTextSpouseMiddleName = view.findViewById(R.id.editTextSpouseMiddleName);
        editTextSpouseLastName = view.findViewById(R.id.editTextSpouseLastName);
        spinnerQualification = view.findViewById(R.id.spinnerQualification);
        editTextAddressLine1 = view.findViewById(R.id.editTextAddressLine1);
        editTextAddressLine2 = view.findViewById(R.id.editTextAddressLine2);
        editTextLandmark = view.findViewById(R.id.editTextLandmark);
        editTextArea = view.findViewById(R.id.editTextArea);
        editTextLocality = view.findViewById(R.id.editTextLocality);
        spinnerState = view.findViewById(R.id.spinnerState);
        spinnerDistrict = view.findViewById(R.id.spinnerDistrict);
        spinnerTehsil = view.findViewById(R.id.spinnerTehsil);
        spinnerVTC = view.findViewById(R.id.spinnerVTC);
        editTextPincode = view.findViewById(R.id.editTextPincode);
        editTextCIBILScore = view.findViewById(R.id.editTextCIBILScore);
        editTextNameAsPerPANCard = view.findViewById(R.id.editTextNameAsPerPANCard);
        imgBCAPoliceVerification = view.findViewById(R.id.imgBCAPoliceVerification);
        editTextPANNumber1 = view.findViewById(R.id.editTextPANNumber1);
        editTextPANNumber2 = view.findViewById(R.id.editTextPANNumber2);
        imgPanScanCopy = view.findViewById(R.id.imgPanPReScanCopy);
        txtBankName1Lbl = view.findViewById(R.id.txtBankName1Lbl);
        txtBankName2Lbl = view.findViewById(R.id.txtBankName2Lbl);
        txtBankName3Lbl = view.findViewById(R.id.txtBankName3Lbl);
        spinnerBankName1 = view.findViewById(R.id.spinnerBankName1);
        spinnerBankName2 = view.findViewById(R.id.spinnerBankName2);
        spinnerBankName3 = view.findViewById(R.id.spinnerBankName3);
        txtBranchName1Lbl = view.findViewById(R.id.txtBranchName1Lbl);
        txtBranchName2Lbl = view.findViewById(R.id.txtBranchName2Lbl);
        txtBranchName3Lbl = view.findViewById(R.id.txtBranchName3Lbl);
        spinnerBranchName1 = view.findViewById(R.id.spinnerBranchName1);
        spinnerBranchName2 = view.findViewById(R.id.spinnerBranchName2);
        spinnerBranchName3 = view.findViewById(R.id.spinnerBranchName3);
        txtApproxDistance1Lbl = view.findViewById(R.id.txtApproxDistance1Lbl);
        txtApproxDistance2Lbl = view.findViewById(R.id.txtApproxDistance2Lbl);
        txtApproxDistance3Lbl = view.findViewById(R.id.txtApproxDistance3Lbl);
        spinnerApproxDistance1 = view.findViewById(R.id.spinnerApproxDistance1);
        spinnerApproxDistance2 = view.findViewById(R.id.spinnerApproxDistance2);
        spinnerApproxDistance3 = view.findViewById(R.id.spinnerApproxDistance3);
        layoutCIBILBCAPolice = view.findViewById(R.id.layoutCIBILBCAPolice);

        textViewDateOfPassing.setOnClickListener(this);
        textViewBCADOB.setOnClickListener(this);
        textViewYearOfPassing.setOnClickListener(this);
        textViewCIBILReportDate.setOnClickListener(this);
        textViewBCAPoliceVerificationDate.setOnClickListener(this);
        imgBCAPoliceVerification.setOnClickListener(this);
        imgPanScanCopy.setOnClickListener(this);

    }

    public void setCompulsoryMarkLabel() {
        TextView[] txtViewsForCompulsoryMark = {txtBCANameLbl, txtBCAFatherNameLbl, txtBCAMobileNumberLbl, txtEmailIdLbl,
                txtGenderLbl, txtMaritalStatusLbl, txtSpouseNameLbl, txtBCADOBLbl, txtQualificationLbl,
                txtYearOfPassingLbl, txtAddressLine1Lbl, txtAreaLbl, txtLocalityLbl, txtStateLbl,
                txtDistrictLbl, txtTehsilLbl, txtVTCLbl, txtPincodeLbl, txtNameAsPerPANCardLbl,
                txtPANNumberLbl, txtPanCardScanCopyLbl, txtBankName1Lbl, txtBankName2Lbl, txtBankName3Lbl, txtBranchName1Lbl, txtBranchName2Lbl, txtBranchName3Lbl,
                txtApproxDistance1Lbl, txtApproxDistance2Lbl, txtApproxDistance3Lbl};
        GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);
    }

    //region TextChangedListener PAN Card
    private void TextChangedListenerPANcard() {

        editTextPANNumber1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do Nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int len = editTextPANNumber1.getText().toString().trim().length();
                if (len <= 0)
                    return;
            }

            @Override
            public void afterTextChanged(Editable editable) {

                int len = editTextPANNumber1.getText().toString().trim().length();
                if (len <= 0)
                    return;

                if (editTextPANNumber1.getText().toString().contains(" ")) {
                    editTextPANNumber1.setText(editTextPANNumber1.getText().toString().replaceAll(" ", ""));
                    editTextPANNumber1.setSelection(editTextPANNumber1.getText().length());
                }


                if (!CommonUtils.PANCardValid(String.valueOf(editable.toString()))) {
                    edittext_listener = true;
                } else {
                    edittext_listener = false;
                }

                //set color base on
                if (edittext_listener) {
                    editTextPANNumber1.setTextColor(Color.parseColor("#FF0000"));
                    editTextPANNumber1.setError("Please enter a valid PAN number.");
                } else {
                    editTextPANNumber1.setTextColor(Color.parseColor("#468847"));
                    editTextPANNumber1.setError(null);

                    String strPass1 = editTextPANNumber1.getText().toString();
                    String strPass2 = editTextPANNumber2.getText().toString();

                    if (editTextPANNumber1.length() < 1) {
                        //Do Nothing
                    } else if (!strPass1.equalsIgnoreCase(strPass2)) {
                        editTextPANNumber1.setError(NOT_MATCH);
                        editTextPANNumber1.setTextColor(Color.parseColor(COLOR_000000));
                    } else {
                        editTextPANNumber2.setTextColor(Color.parseColor("#468847"));
                        editTextPANNumber2.setError(null);
                        editTextPANNumber1.setTextColor(Color.parseColor("#468847"));
                        editTextPANNumber1.setError(null);
                    }
                }
            }
        });

        editTextPANNumber2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do Nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int len = editTextPANNumber2.getText().toString().trim().length();
                if (len <= 0)
                    return;

                if (start != 10) {
                    editTextPANNumber2.setTextColor(Color.parseColor(COLOR_000000));
                    editTextPANNumber2.setError("Please enter a valid PAN number");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editTextPANNumber2.getText().toString().contains(" ")) {
                    editTextPANNumber2.setText(editTextPANNumber2.getText().toString().replaceAll(" ", ""));
                    editTextPANNumber2.setSelection(editTextPANNumber2.getText().length());
                }

                String strPass1 = editTextPANNumber1.getText().toString();
                String strPass2 = editTextPANNumber2.getText().toString();

                if (editTextPANNumber2.length() < 1) {
                    //Do Nothing
                } else if (!strPass1.equalsIgnoreCase(strPass2)) {
                    editTextPANNumber2.setError(NOT_MATCH);
                    editTextPANNumber2.setTextColor(Color.parseColor(COLOR_000000));
                } else {
                    editTextPANNumber2.setTextColor(Color.parseColor("#468847"));
                    editTextPANNumber2.setError(null);
                    editTextPANNumber1.setTextColor(Color.parseColor("#468847"));
                    editTextPANNumber1.setError(null);

                    if (TextUtils.isEmpty(editTextNameAsPerPANCard.getText().toString()) || editTextNameAsPerPANCard.getText().toString().length() <= 4) {
                        editTextNameAsPerPANCard.setError(ENTER_PAN_NAME);
                        Toast.makeText(context, ENTER_PAN_NAME, Toast.LENGTH_LONG).show();
                        editTextNameAsPerPANCard.setTextColor(Color.parseColor(COLOR_000000));
                    } else {
                        editTextNameAsPerPANCard.setTextColor(Color.parseColor("#468847"));
                        editTextNameAsPerPANCard.setError(null);
                        //check is validate pan card  - nilesh
                        isPanCardValidationCheck(strPass2, editTextNameAsPerPANCard.getText().toString());
                    }
                }
            }
        });

        editTextNameAsPerPANCard.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if (hasFocus) {
                    //Do Nothing
                } else {
                    if (!TextUtils.isEmpty(editTextNameAsPerPANCard.getText().toString())) {
                        editTextPANNumber1.requestFocus();

                        String strPass1 = editTextPANNumber1.getText().toString();
                        String strPass2 = editTextPANNumber2.getText().toString();

                        if (TextUtils.isEmpty(editTextNameAsPerPANCard.getText().toString())) {
                            editTextNameAsPerPANCard.setError(ENTER_PAN_NAME);
                            editTextNameAsPerPANCard.setTextColor(Color.parseColor(COLOR_000000));
                        } else {
                            editTextNameAsPerPANCard.setTextColor(Color.parseColor("#468847"));
                            editTextNameAsPerPANCard.setError(null);

                            if (strPass1.length() <= 9 || !strPass1.equalsIgnoreCase(strPass2)) {
                                editTextPANNumber2.setError(NOT_MATCH);
                                editTextPANNumber2.setTextColor(Color.parseColor(COLOR_000000));
                            } else {
                                //check is validate pan card
                                isPanCardValidationCheck(strPass2, editTextNameAsPerPANCard.getText().toString());
                            }
                        }
                    }
                }
            }
        });


    }
    //endregion

    public int IsPersonalDetailsValidated() {

        //BCA Salutation
        if (TextUtils.isEmpty(preBCAPersonalDetailsDto.getBcaSalution())) {
            Toast.makeText(context, "Please select BCA Salutation.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerBCASalutation, "Please select BCA Salutation.", context);
            return 1;
        }

        //BCA First Name
        preBCAPersonalDetailsDto.setBcaFirstName(editTextBCAFirstName.getText().toString().trim());
        if (TextUtils.isEmpty(preBCAPersonalDetailsDto.getBcaFirstName())) {
            Toast.makeText(context, "Please enter BCA First Name.", Toast.LENGTH_LONG).show();
            editTextBCAFirstName.setError("Please enter BCA First Name.");
            return 1;
        }

        if (preBCAPersonalDetailsDto.getBcaFirstName().length() < 3) {
            Toast.makeText(context, "Please enter minimum 3 characters in BCA First Name.", Toast.LENGTH_LONG).show();
            editTextBCAFirstName.setError("Please enter minimum 3 characters in BCA First Name.");
            return 1;
        }

        //Last Name
        preBCAPersonalDetailsDto.setBcaLastName(editTextBCALastName.getText().toString().trim());
        if (TextUtils.isEmpty(preBCAPersonalDetailsDto.getBcaLastName())) {
            Toast.makeText(context, "Please enter BCA Last Name.", Toast.LENGTH_LONG).show();
            editTextBCALastName.setError("Please enter BCA Last Name.");
            return 1;
        }

        if (preBCAPersonalDetailsDto.getBcaLastName().length() < 3) {
            Toast.makeText(context, "Please enter minimum 3 characters in BCA Last Name.", Toast.LENGTH_LONG).show();
            editTextBCALastName.setError("Please enter minimum 3 characters in BCA Last Name.");
            return 1;
        }

        //BCA Father's Salutation
        if (TextUtils.isEmpty(preBCAPersonalDetailsDto.getBcaFatherNameSalution())) {
            Toast.makeText(context, "Please select BCA Father's Salutation.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerBCAFatherSalutation, "Please select BCA Father's Salutation.", context);
            return 1;
        }

        //BCA Father's First Name
        preBCAPersonalDetailsDto.setBcaFatherFirstName(editTextBCAFatherFirstName.getText().toString().trim());
        if (TextUtils.isEmpty(preBCAPersonalDetailsDto.getBcaFatherFirstName())) {
            Toast.makeText(context, "Please enter BCA Father's First Name.", Toast.LENGTH_LONG).show();
            editTextBCAFatherFirstName.setError("Please enter BCA Father's First Name.");
            return 1;
        }

        if (preBCAPersonalDetailsDto.getBcaFatherFirstName().length() < 3) {
            Toast.makeText(context, "Please enter minimum 3 characters in BCA Father's First Name.", Toast.LENGTH_LONG).show();
            editTextBCAFatherFirstName.setError("Please enter minimum 3 characters in BCA Father's First Name.");
            return 1;
        }

        //BCA Father Last Name
        preBCAPersonalDetailsDto.setBcaFatherLastName(editTextBCAFatherLastName.getText().toString().trim());
        if (TextUtils.isEmpty(preBCAPersonalDetailsDto.getBcaFatherLastName())) {
            Toast.makeText(context, "Please enter BCA Father's Last Name.", Toast.LENGTH_LONG).show();
            editTextBCAFatherLastName.setError("Please enter BCA Father's Last Name.");
            return 1;
        }

        if (preBCAPersonalDetailsDto.getBcaFatherLastName().length() < 3) {
            Toast.makeText(context, "Please enter minimum 3 characters in BCA Father's Last Name.", Toast.LENGTH_LONG).show();
            editTextBCAFatherLastName.setError("Please enter minimum 3 characters in BCA Father's Last Name.");
            return 1;
        }

        //BCA Mobile Number
        preBCAPersonalDetailsDto.setBcaMobileNumber(editTextBCAMobileNo.getText().toString().trim());
        if (TextUtils.isEmpty(preBCAPersonalDetailsDto.getBcaMobileNumber())) {
            Toast.makeText(context, "Please enter BCA Mobile Number.", Toast.LENGTH_LONG).show();
            editTextBCAMobileNo.setError("Please enter BCA Mobile Number.");
            return 1;
        }

        if (preBCAPersonalDetailsDto.getBcaMobileNumber().length() < 10) {
            Toast.makeText(context, "Please enter 10 digit Mobile Number.", Toast.LENGTH_LONG).show();
            editTextBCAMobileNo.setError("Please enter 10 digit Mobile Number.");
            return 1;
        }

        if (Integer.parseInt(preBCAPersonalDetailsDto.getBcaMobileNumber()) == 0) {
            Toast.makeText(context, "Please enter valid 10 digit Mobile Number.", Toast.LENGTH_LONG).show();
            editTextBCAMobileNo.setError("Please enter valid 10 digit Mobile Number.");
            return 1;
        }

        preBCAPersonalDetailsDto.setBcaAltMobileNumber(editTextBCAALternateMobileNo.getText().toString().trim());
        if (!TextUtils.isEmpty(preBCAPersonalDetailsDto.getBcaAltMobileNumber())) {
            if (preBCAPersonalDetailsDto.getBcaAltMobileNumber().length() < 10) {
                Toast.makeText(context, "Please enter 10 digit Alt Mobile Number.", Toast.LENGTH_LONG).show();
                editTextBCAALternateMobileNo.setError("Please enter 10 digit Alt Mobile Number.");
                return 1;
            }
        }

        if (Integer.parseInt(preBCAPersonalDetailsDto.getBcaAltMobileNumber()) == 0) {
            Toast.makeText(context, "Please enter valid 10 digit Alt Mobile Number.", Toast.LENGTH_LONG).show();
            editTextBCAMobileNo.setError("Please enter valid 10 digit Alt Mobile Number.");
            return 1;
        }

        //Email ID
        preBCAPersonalDetailsDto.setBcaEmailId(editTextEmailID.getText().toString().trim());
        if (TextUtils.isEmpty(preBCAPersonalDetailsDto.getBcaEmailId()) || !editTextEmailID.getText().toString().trim().matches(CommonUtils.emailPattern)) {
            Toast.makeText(context, "Please enter valid Email ID.", Toast.LENGTH_LONG).show();
            editTextEmailID.setError("Please enter valid Email ID.");
            return 1;
        }

        //Gender
        if (TextUtils.isEmpty(preBCAPersonalDetailsDto.getGender())) {
            showMessage("Please select Gender.");
            return 1;
        }

        //Marital Status
        if (TextUtils.isEmpty(preBCAPersonalDetailsDto.getMaritalStatus())) {
            showMessage("Please select Marital Status.");
            return 1;
        } else if (preBCAPersonalDetailsDto.getMaritalStatus().equalsIgnoreCase("0")) {
            int status = validateSpouseDetails();
            if (status != 0)
                return 1;
        }

        //BCA DOB
        if (TextUtils.isEmpty(preBCAPersonalDetailsDto.getBcaDOB())) {
            Toast.makeText(context, "Please select BCA DOB.", Toast.LENGTH_LONG).show();
            textViewBCADOB.setError("Please select BCA DOB.");
            return 1;
        }

        //Highest Qualification
        if (TextUtils.isEmpty(preBCAPersonalDetailsDto.getQualification()) || preBCAPersonalDetailsDto.getQualification().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Qualification.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerQualification, "Please select Qualification.", context);
            return 1;
        }

        //Year Of Passing
        if (TextUtils.isEmpty(preBCAPersonalDetailsDto.getYearOfPassing())) {
            Toast.makeText(context, "Please select Year Of Passing.", Toast.LENGTH_LONG).show();
            textViewYearOfPassing.setError("Please select Year Of Passing.");
            return 1;
        }

        //Address Line 1
        preBCAPersonalDetailsDto.setBcaAddressLine1(editTextAddressLine1.getText().toString().trim());
        if (TextUtils.isEmpty(preBCAPersonalDetailsDto.getBcaAddressLine1())) {
            Toast.makeText(context, "Please enter Address Line 1.", Toast.LENGTH_LONG).show();
            editTextAddressLine1.setError("Please enter Address Line 1.");
            return 1;
        }

        if (preBCAPersonalDetailsDto.getBcaAddressLine1().length() < 3) {
            Toast.makeText(context, "Please enter minimum 3 characters in Address Line 1.", Toast.LENGTH_LONG).show();
            editTextAddressLine1.setError("Please enter minimum 3 characters in Address Line 1.");
            return 1;
        }

        //Address Line 2
        preBCAPersonalDetailsDto.setBcaAddressLine2(editTextAddressLine2.getText().toString().trim());
        if (!TextUtils.isEmpty(preBCAPersonalDetailsDto.getBcaAddressLine2())) {
            if (preBCAPersonalDetailsDto.getBcaAddressLine2().length() < 3) {
                Toast.makeText(context, "Please enter minimum 3 characters in Address Line 2.", Toast.LENGTH_LONG).show();
                editTextAddressLine2.setError("Please enter minimum 3 characters in Address Line 2.");
                return 1;
            }
        }

        //LandMark
        preBCAPersonalDetailsDto.setBcaLandmark(editTextLandmark.getText().toString().trim());
        if (TextUtils.isEmpty(preBCAPersonalDetailsDto.getBcaLandmark())) {
            Toast.makeText(context, "Please enter Landmark.", Toast.LENGTH_LONG).show();
            editTextLandmark.setError("Please enter Landmark.");
            return 1;
        }

        if (preBCAPersonalDetailsDto.getBcaLandmark().length() < 3) {
            Toast.makeText(context, "Please enter minimum 3 characters in LandMark.", Toast.LENGTH_LONG).show();
            editTextLandmark.setError("Please enter minimum 3 characters in LandMark.");
            return 1;
        }

        //Area
        preBCAPersonalDetailsDto.setArea(editTextArea.getText().toString().trim());
        /*if (TextUtils.isEmpty(preBCAPersonalDetailsDto.getArea())) {
            Toast.makeText(context, "Please enter Area.", Toast.LENGTH_LONG).show();
            editTextArea.setError("Please enter Area.");
            return 1;
        }*/

        //Locality
        preBCAPersonalDetailsDto.setLocality(editTextLocality.getText().toString().trim());
       /* if (TextUtils.isEmpty(preBCAPersonalDetailsDto.getLocality())) {
            Toast.makeText(context, "Please enter Locality.", Toast.LENGTH_LONG).show();
            editTextLocality.setError("Please enter Locality.");
            return 1;
        }*/

        //State
        if (TextUtils.isEmpty(preBCAPersonalDetailsDto.getState()) || preBCAPersonalDetailsDto.getState().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select State.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerState, "Please select State.", context);
            return 1;
        }

        //District
        if (TextUtils.isEmpty(preBCAPersonalDetailsDto.getDistrict()) || preBCAPersonalDetailsDto.getDistrict().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select District.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerDistrict, "Please select District.", context);
            return 1;
        }

        //VTC
        if (TextUtils.isEmpty(preBCAPersonalDetailsDto.getVtc()) || preBCAPersonalDetailsDto.getVtc().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select VTC.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerVTC, "Please select VTC.", context);
            return 1;
        }

        //Pin code
        preBCAPersonalDetailsDto.setPincode(editTextPincode.getText().toString().trim());
        if (TextUtils.isEmpty(preBCAPersonalDetailsDto.getPincode()) || preBCAPersonalDetailsDto.getPincode().trim().length() < 6) {
            Toast.makeText(context, "Please enter proper Pin code.", Toast.LENGTH_LONG).show();
            editTextPincode.setError("Please enter proper Pin code.");
            return 1;
        }

        int isPinCodeStartValid = isPinCodeStartValid(String.valueOf(preBCAPersonalDetailsDto.getPincode().charAt(0)));
        if (isPinCodeStartValid != 0) {
            Toast.makeText(context, "Invalid Pin Code.", Toast.LENGTH_LONG).show();
            editTextPincode.setError("Invalid Pin Code.");
            return 1;
        }

        //Name as per PAN Card
        preBCAPersonalDetailsDto.setNameAsPanCard(editTextNameAsPerPANCard.getText().toString().trim());
        if (TextUtils.isEmpty(preBCAPersonalDetailsDto.getNameAsPanCard())) {
            Toast.makeText(context, "Please enter Name as mentioned on PAN Card.", Toast.LENGTH_LONG).show();
            editTextNameAsPerPANCard.setError("Please enter Name as mentioned on PAN Card.");
            return 1;
        }

        //STEP 1.1: PAN Number
        preBCAPersonalDetailsDto.setPanNumber(editTextPANNumber1.getText().toString().trim());
        if (TextUtils.isEmpty(preBCAPersonalDetailsDto.getPanNumber()) || !editTextPANNumber1.getText().toString().trim().matches(CommonUtils.pancardpattern)) {
            Toast.makeText(context, "Please enter PAN Number.", Toast.LENGTH_LONG).show();
            editTextPANNumber1.setError("Please enter PAN Number.");
            return 1;
        }

        //STEP 1.2: Confirm PAN Number
        if (TextUtils.isEmpty(preBCAPersonalDetailsDto.getPanNumber()) || !editTextPANNumber2.getText().toString().trim().matches(CommonUtils.pancardpattern)) {
            Toast.makeText(context, "Please enter Confirm PAN Number.", Toast.LENGTH_LONG).show();
            editTextPANNumber2.setError("Please enter Confirm PAN Number.");
            return 1;
        }

        //PAN Card Scan Copy
        if (TextUtils.isEmpty(preBCAPersonalDetailsDto.getPanCardScanId()) || preBCAPersonalDetailsDto.getPanCardScanId().equalsIgnoreCase("0")) {
            if (TextUtils.isEmpty(preBCAPersonalDetailsDto.getPanCardScanBase64())) {
                showMessage("Please add PAN Card Scan Copy.");
                return 1;
            }
        }

        if (!IsPanValidated) {
            Toast.makeText(context, "PAN Card Details not verified.", Toast.LENGTH_LONG).show();
            editTextNameAsPerPANCard.setError("PAN Card Details not verified.");
            editTextPANNumber1.setError("PAN Card Details not verified.");
            return 1;
        }

        //Bank 1
        if (TextUtils.isEmpty(preBCAPersonalDetailsDto.getBankName1Id()) || preBCAPersonalDetailsDto.getBankName1Id().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Bank Preference 1.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerBankName1, "Please select Bank Preference 1.", context);
            return 1;
        }

        //Branch 1
        if (TextUtils.isEmpty(preBCAPersonalDetailsDto.getBranchName1Id()) || preBCAPersonalDetailsDto.getBranchName1Id().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Branch Preference 1.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerBranchName1, "Please select Branch Preference 1.", context);
            return 1;
        }

        //Distance 1
        if (TextUtils.isEmpty(preBCAPersonalDetailsDto.getApproxDistance1()) || preBCAPersonalDetailsDto.getApproxDistance1().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Distance From Kendra for Bank Preference 1.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerApproxDistance1, "Please select Distance From Kendra for Bank Preference 1.", context);
            return 1;
        }

        //Bank 2
        if (TextUtils.isEmpty(preBCAPersonalDetailsDto.getBankName2Id()) || preBCAPersonalDetailsDto.getBankName2Id().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Bank Preference 2.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerBankName2, "Please select Bank Preference 2.", context);
            return 1;
        }

        //Branch 2
        if (TextUtils.isEmpty(preBCAPersonalDetailsDto.getBranchName2Id()) || preBCAPersonalDetailsDto.getBranchName2Id().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Branch Preference 2.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerBranchName2, "Please select Branch Preference 2.", context);
            return 1;
        }

        //Distance 2
        if (TextUtils.isEmpty(preBCAPersonalDetailsDto.getApproxDistance2()) || preBCAPersonalDetailsDto.getApproxDistance2().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Distance From Kendra for Bank Preference 2.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerApproxDistance2, "Please select Distance From Kendra for Bank Preference 2.", context);
            return 1;
        }

        //Bank 3
        if (TextUtils.isEmpty(preBCAPersonalDetailsDto.getBankName3Id()) || preBCAPersonalDetailsDto.getBankName3Id().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Bank Preference 3.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerBankName3, "Please select Bank Preference 3.", context);
            return 1;
        }

        //Branch 3
        if (TextUtils.isEmpty(preBCAPersonalDetailsDto.getBranchName3Id()) || preBCAPersonalDetailsDto.getBranchName3Id().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Branch Preference 3.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerBranchName3, "Please select Branch Preference 3.", context);
            return 1;
        }

        //Distance 3
        if (TextUtils.isEmpty(preBCAPersonalDetailsDto.getApproxDistance3()) || preBCAPersonalDetailsDto.getApproxDistance3().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Distance From Kendra for Bank Preference 3.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerApproxDistance3, "Please select Distance From Kendra for Bank Preference 3.", context);
            return 1;
        }
        return 0;
    }

    private int isPinCodeStartValid(String pinCodeFirstDigit) {
        if (!Pattern.matches("[1-9]+", pinCodeFirstDigit)) {
            return 1;
        }
        return 0;
    }

    private int validateSpouseDetails() {

        //STEP 1: Spouse Salutation
        if (TextUtils.isEmpty(preBCAPersonalDetailsDto.getBcaSpouseSalution())) {
            Toast.makeText(context, "Please select Spouse Salutation.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerSpouseSalutation, "Please select Spouse Salutation.", context);
            return 1;
        }

        //STEP 2: Spouse First Name
        preBCAPersonalDetailsDto.setBcaSpouseFirstName(editTextSpouseFirstName.getText().toString().trim());
        if (TextUtils.isEmpty(preBCAPersonalDetailsDto.getBcaSpouseFirstName())) {
            Toast.makeText(context, "Please enter Spouse First Name.", Toast.LENGTH_LONG).show();
            editTextSpouseFirstName.setError("Please enter Spouse First Name.");
            return 1;
        }

        if (preBCAPersonalDetailsDto.getBcaSpouseFirstName().length() < 3) {
            Toast.makeText(context, "Please enter minimum 3 characters in Spouse First Name.", Toast.LENGTH_LONG).show();
            editTextSpouseFirstName.setError("Please enter minimum 3 characters in Spouse First  Name.");
            return 1;
        }


        //STEP 3: Spouse Middle Name
        preBCAPersonalDetailsDto.setBcaSpouseMiddleName(editTextSpouseMiddleName.getText().toString().trim());

        //STEP 4: Spouse Last Name
        preBCAPersonalDetailsDto.setBcaSpouseLastName(editTextSpouseLastName.getText().toString().trim());
        if (TextUtils.isEmpty(preBCAPersonalDetailsDto.getBcaSpouseLastName())) {
            Toast.makeText(context, "Please enter Spouse Last Name.", Toast.LENGTH_LONG).show();
            editTextSpouseLastName.setError("Please enter Spouse Last Name.");
            return 1;
        }

        if (preBCAPersonalDetailsDto.getBcaSpouseLastName().length() < 3) {
            Toast.makeText(context, "Please enter minimum 3 characters in Spouse Last Name.", Toast.LENGTH_LONG).show();
            editTextSpouseLastName.setError("Please enter minimum 3 characters in Spouse Last  Name.");
            return 1;
        }
        return 0;
    }

    private void showDateTimeDialogPicker() {

        Date defaultDate = null;
        if (selectedDateTimeId == R.id.textViewYearOfPassing) {
            defaultDate = yearOfPassingDate;
        } else if (selectedDateTimeId == R.id.textViewDateOfPassing) {
            defaultDate = dateOfPassingDate;
        } else if (selectedDateTimeId == R.id.textViewBCADOB) {
            defaultDate = dOBDate;
        } else if (selectedDateTimeId == R.id.textViewCIBILReportDate) {
            defaultDate = cibilReportDate;
        } else if (selectedDateTimeId == R.id.textViewBCAPoliceVerificationDate) {
            defaultDate = cibilReportDate;
        }

        // Show DateTime Picker Dialog.
        dateTimePickerDialog = new DateTimePickerDialog(getActivity(), true, defaultDate, new DateTimePickerDialog.IDateTimePicker() {
            @Override
            public void getDateTime(Date datetime, String defaultFormattedDateTime) {
                try {
                    String formatedDate = dateFormatter2.format(datetime);
                    String formateYMD = dateFormatterYMD.format(datetime);
                    Toast.makeText(getActivity(), "Selected DateTime : " + formatedDate, Toast.LENGTH_LONG).show();

                    if (selectedDateTimeId != 0) {
                        TextView textViewDateTime = (TextView) view.findViewById(selectedDateTimeId);
                        textViewDateTime.setText(formateYMD);

                        if (selectedDateTimeId == R.id.textViewYearOfPassing) {
                            yearOfPassingDate = datetime;
                            strYearOfPassingDate = formateYMD;
                            preBCAPersonalDetailsDto.setYearOfPassing(strYearOfPassingDate);

                            String DOB = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MM-yyyy", preBCAPersonalDetailsDto.getYearOfPassing());
                            textViewYearOfPassing.setText(DOB);

                        } else if (selectedDateTimeId == R.id.textViewDateOfPassing) {
                            dateOfPassingDate = datetime;
                            strDateOfPassing = formateYMD;
                            preBCAPersonalDetailsDto.setDateOfPassing(strDateOfPassing);

                            String DOB = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MM-yyyy", preBCAPersonalDetailsDto.getDateOfPassing());
                            textViewDateOfPassing.setText(DOB);

                        } else if (selectedDateTimeId == R.id.textViewBCADOB) {
                            dOBDate = datetime;
                            strDOB = formateYMD;
                            preBCAPersonalDetailsDto.setBcaDOB(strDOB);

                            String DateOfIncorporation = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", preBCAPersonalDetailsDto.getBcaDOB());
                            textViewBCADOB.setText(DateOfIncorporation);

                        } else if (selectedDateTimeId == R.id.textViewCIBILReportDate) {
                            cibilReportDate = datetime;
                            strCibilReportDate = formateYMD;
                            preBCAPersonalDetailsDto.setCibilReportDate(strCibilReportDate);

                            String CBLReportDate = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", preBCAPersonalDetailsDto.getCibilReportDate());
                            textViewCIBILReportDate.setText(CBLReportDate);
                        } else if (selectedDateTimeId == R.id.textViewBCAPoliceVerificationDate) {
                            bcaPoliceDate = datetime;
                            strBcaPoliceDate = formateYMD;
                            preBCAPersonalDetailsDto.setBcaPoliceVerificationDate(strBcaPoliceDate);

                            String BCAReportDate = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", preBCAPersonalDetailsDto.getBcaPoliceVerificationDate());
                            textViewBCAPoliceVerificationDate.setText(BCAReportDate);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //Set Min and Max Date
        long now = new Date().getTime() - 1000;
        int after4days = (1000 * 60 * 60 * 24 * 4);         //no Of Days

        Calendar c = Calendar.getInstance();
        c.set(1920, 0, 1);//Year,Month -1,Day

        Calendar curCal = Calendar.getInstance();
        int day = curCal.get(Calendar.DATE);
        int month = curCal.get(Calendar.MONTH);
        int year = curCal.get(Calendar.YEAR) - 18;
        curCal.set(year, month, day);

        Calendar last3Cal = Calendar.getInstance();
        int day3 = last3Cal.get(Calendar.DATE);
        int month3 = last3Cal.get(Calendar.MONTH) - 3;
        int year3 = last3Cal.get(Calendar.YEAR);
        last3Cal.set(year3, month3, day3);

        if (selectedDateTimeId == R.id.textViewYearOfPassing) {
            dateTimePickerDialog.setMinDate(c.getTimeInMillis());
            dateTimePickerDialog.setMaxDate(curCal.getTimeInMillis());

        } else if (selectedDateTimeId == R.id.textViewDateOfPassing) {
            dateTimePickerDialog.setMinDate(c.getTimeInMillis());
            dateTimePickerDialog.setMaxDate(curCal.getTimeInMillis());

        } else if (selectedDateTimeId == R.id.textViewBCADOB) {
            dateTimePickerDialog.setMinDate(c.getTimeInMillis());
            dateTimePickerDialog.setMaxDate(curCal.getTimeInMillis());
        } else if (selectedDateTimeId == R.id.textViewCIBILReportDate) {
            dateTimePickerDialog.setMinDate(last3Cal.getTimeInMillis());
            dateTimePickerDialog.setMaxDate(now);
        } else if (selectedDateTimeId == R.id.textViewBCAPoliceVerificationDate) {
            dateTimePickerDialog.setMinDate(last3Cal.getTimeInMillis());
            dateTimePickerDialog.setMaxDate(now);
        }

        dateTimePickerDialog.setCancelable(false);
        dateTimePickerDialog.setActionButtonName("Save");
        dateTimePickerDialog.show();

    }

    class GetAllSpinnerData extends AsyncTask<String, Void, String> {

        private String stateId;
        private String districtId;
        private String blockId;
        private String bankId;
        private String type;

        public GetAllSpinnerData(String stateId, String districtId, String blockId, String bankId) {
            this.stateId = stateId;
            this.districtId = districtId;
            this.blockId = blockId;
            this.bankId = bankId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (TextUtils.isEmpty(stateId)) {
                showProgressSpinner(context);
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            type = strings[0];

            switch (type.toUpperCase()) {

                case "STATE":

                    //STEP 1: Salutation List
                    salutationList = bcaEntryDetailsRepo.getSalutationList();

                    //STEP 2: Qualification List
                    qualificationList = bcaEntryDetailsRepo.getNewQualificationList();

                    //STEP 6: State List
                    stateList = bcaEntryDetailsRepo.getStateList();

                    //STEP 7: Bank Name List
                    bankNameList = bcaEntryDetailsRepo.getBankNameUsingStateDistrictList(vStateCode, vDistrictCode);

                    break;

                case "DISTRICT":
                    districtList = bcaEntryDetailsRepo.getDistrictList(stateId);
                    break;

                case "BLOCK":
                    blockList = bcaEntryDetailsRepo.getBlockList(stateId, districtId);
                    break;

                case "VTC":
                    vtcList = bcaEntryDetailsRepo.getVTCList(stateId, districtId, blockId);
                    break;

                case "BRANCH_NAME_1":
                    branchName1List = bcaEntryDetailsRepo.getBranchNameUsingStateDistrictList(bankId, vStateCode, vDistrictCode);
                    break;

                case "BRANCH_NAME_2":
                    branchName2List = bcaEntryDetailsRepo.getBranchNameUsingStateDistrictList(bankId, vStateCode, vDistrictCode);
                    break;

                case "BRANCH_NAME_3":
                    branchName3List = bcaEntryDetailsRepo.getBranchNameUsingStateDistrictList(bankId, vStateCode, vDistrictCode);
                    break;

                case "APPROX_DISTANCE_1":
                    approxDistance1List = bcaEntryDetailsRepo.getApproxBankDistanceList();
                    break;

                case "APPROX_DISTANCE_2":
                    approxDistance2List = bcaEntryDetailsRepo.getApproxBankDistanceList();
                    break;

                case "APPROX_DISTANCE_3":
                    approxDistance3List = bcaEntryDetailsRepo.getApproxBankDistanceList();
                    break;

                default:
                    break;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (TextUtils.isEmpty(stateId)) {
                dismissProgressSpinner();
            }

            switch (type.toUpperCase()) {

                case "STATE":
                    bindSpinner();
                    setBankNameSpinnerAdapter(bankNameList, spinnerBankName1, preBCAPersonalDetailsDto.getBankName1Id());
                    setBankNameSpinnerAdapter(bankNameList, spinnerBankName2, preBCAPersonalDetailsDto.getBankName2Id());
                    setBankNameSpinnerAdapter(bankNameList, spinnerBankName3, preBCAPersonalDetailsDto.getBankName3Id());
                    break;

                case "DISTRICT":
                    setDistrictSpinnerAdapter(districtList, spinnerDistrict, preBCAPersonalDetailsDto.getDistrict());
                    break;

                case "BLOCK":
                    setBlockSpinnerAdapter(blockList, spinnerTehsil, preBCAPersonalDetailsDto.getBlock());
                    break;

                case "VTC":
                    setVTCSpinnerAdapter(vtcList, spinnerVTC, preBCAPersonalDetailsDto.getVtc());
                    break;

                case "BRANCH_NAME_1":
                    setBranchNameSpinnerAdapter(branchName1List, spinnerBranchName1, preBCAPersonalDetailsDto.getBranchName1Id());
                    break;

                case "BRANCH_NAME_2":
                    setBranchNameSpinnerAdapter(branchName2List, spinnerBranchName2, preBCAPersonalDetailsDto.getBranchName2Id());
                    break;

                case "BRANCH_NAME_3":
                    setBranchNameSpinnerAdapter(branchName3List, spinnerBranchName3, preBCAPersonalDetailsDto.getBranchName3Id());
                    break;

                case "APPROX_DISTANCE_1":
                    setApproxDistanceSpinnerAdapter(approxDistance1List, spinnerApproxDistance1, preBCAPersonalDetailsDto.getApproxDistance1());
                    break;

                case "APPROX_DISTANCE_2":
                    setApproxDistanceSpinnerAdapter(approxDistance2List, spinnerApproxDistance2, preBCAPersonalDetailsDto.getApproxDistance2());
                    break;

                case "APPROX_DISTANCE_3":
                    setApproxDistanceSpinnerAdapter(approxDistance3List, spinnerApproxDistance3, preBCAPersonalDetailsDto.getApproxDistance3());
                    break;

                default:
                    break;
            }

        }
    }

    //region Set Bank Name Adapter
    private void setBankNameSpinnerAdapter(List<CustomFranchiseeApplicationSpinnerDto> bankNameList, Spinner spinnerBank, String selBankId) {

        spinnerBank.setEnabled(true);
        spinnerBank.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, bankNameList));
        int Pos = bcaEntryDetailsRepo.getSelectedPos(bankNameList, selBankId);
        spinnerBank.setSelection(Pos);
        spinnerBank.setOnItemSelectedListener(this);
    }
    //endregion

    //region Set Branch Name Adapter
    private void setBranchNameSpinnerAdapter(List<CustomFranchiseeApplicationSpinnerDto> branchNameList, Spinner spinnerBranchName, String selBranchId) {

        spinnerBranchName.setEnabled(true);
        spinnerBranchName.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, branchNameList));
        int Pos = bcaEntryDetailsRepo.getSelectedPos(branchNameList, selBranchId);
        spinnerBranchName.setSelection(Pos);
        spinnerBranchName.setOnItemSelectedListener(this);
    }
    //endregion

    //region Set Approx Distance Adapter
    private void setApproxDistanceSpinnerAdapter(List<CustomFranchiseeApplicationSpinnerDto> approxDistanceList, Spinner spinnerApproxDis, String selApproxDisId) {

        spinnerApproxDis.setEnabled(true);
        spinnerApproxDis.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, approxDistanceList));
        int Pos = bcaEntryDetailsRepo.getSelectedPos(approxDistanceList, selApproxDisId);
        spinnerApproxDis.setSelection(Pos);
        spinnerApproxDis.setOnItemSelectedListener(this);
    }
    //endregion

    public PreBCAPersonalDetailsDto getPreBCAPersonalDetailsDto() {
        preBCAPersonalDetailsDto.setBcaFirstName(editTextBCAFirstName.getText().toString());
        preBCAPersonalDetailsDto.setBcaMiddleName(editTextBCAMiddleName.getText().toString());
        preBCAPersonalDetailsDto.setBcaLastName(editTextBCALastName.getText().toString());
        preBCAPersonalDetailsDto.setBcaFatherFirstName(editTextBCAFatherFirstName.getText().toString());
        preBCAPersonalDetailsDto.setBcaFatherMiddleName(editTextBCAFatherMiddleName.getText().toString());
        preBCAPersonalDetailsDto.setBcaFatherLastName(editTextBCAFatherLastName.getText().toString());
        preBCAPersonalDetailsDto.setBcaSpouseFirstName(editTextSpouseFirstName.getText().toString());
        preBCAPersonalDetailsDto.setBcaSpouseMiddleName(editTextSpouseMiddleName.getText().toString());
        preBCAPersonalDetailsDto.setBcaSpouseLastName(editTextSpouseLastName.getText().toString());
        preBCAPersonalDetailsDto.setBcaAddressLine1(editTextAddressLine1.getText().toString());
        preBCAPersonalDetailsDto.setBcaAddressLine2(editTextAddressLine2.getText().toString());
        preBCAPersonalDetailsDto.setBcaLandmark(editTextLandmark.getText().toString());
        preBCAPersonalDetailsDto.setBcaMobileNumber(editTextBCAMobileNo.getText().toString());
        preBCAPersonalDetailsDto.setBcaAltMobileNumber(editTextBCAALternateMobileNo.getText().toString());
        preBCAPersonalDetailsDto.setBcaEmailId(editTextEmailID.getText().toString());
        preBCAPersonalDetailsDto.setArea(editTextArea.getText().toString());
        preBCAPersonalDetailsDto.setLocality(editTextLocality.getText().toString());
        preBCAPersonalDetailsDto.setPincode(editTextPincode.getText().toString());
        preBCAPersonalDetailsDto.setCibilScore(editTextCIBILScore.getText().toString());
        preBCAPersonalDetailsDto.setNameAsPanCard(editTextNameAsPerPANCard.getText().toString());
        preBCAPersonalDetailsDto.setPanNumber(editTextPANNumber1.getText().toString());

        String status = checkboxSameAsFranchisee.isChecked() ? "1" : "0";
        ((PreBCADataEntryDetailsFragment) getParentFragment()).preBCADetailsDto.setSameAsFranchisee(status);

        return preBCAPersonalDetailsDto;
    }

    public void reloadData(String data, boolean IsEditable) {
        //Reload Data
        if (TextUtils.isEmpty(data))
            preBCAPersonalDetailsDto = new PreBCAPersonalDetailsDto();
        else {
            try {
                Gson gson = new GsonBuilder().create();
                preBCAPersonalDetailsDto = gson.fromJson(data, PreBCAPersonalDetailsDto.class);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.IsEditable = IsEditable;
        String isShown = ((PreBCADataEntryDetailsFragment) getParentFragment()).preBCADetailsDto.getIsSameAsFranchiseeVisible();
        boolean IsSameAsFranchiseeVisible = (!TextUtils.isEmpty(isShown) && isShown.equalsIgnoreCase("1")) ? true : false;

        if (IsSameAsFranchiseeVisible) {
            layoutSameAsFranchisee.setVisibility(View.VISIBLE);
        } else {
            layoutSameAsFranchisee.setVisibility(View.GONE);
        }

        vStateCode = ((PreBCADataEntryDetailsFragment) getParentFragment()).preBCADetailsDto.getvStateCode();
        vDistrictCode = ((PreBCADataEntryDetailsFragment) getParentFragment()).preBCADetailsDto.getvDistrictCode();

        getAllSpinnerData = new GetAllSpinnerData(null, null, null, null);
        getAllSpinnerData.execute("STATE");
    }

    private void bindSpinner() {

        spinner_focusablemode(spinnerState);
        spinner_focusablemode(spinnerDistrict);
        spinner_focusablemode(spinnerTehsil);
        spinner_focusablemode(spinnerVTC);

        //BCA is same as Franchisee
        checkboxSameAsFranchisee.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean IsChecked) {
                String status = IsChecked ? "1" : "0";
                ((PreBCADataEntryDetailsFragment) getParentFragment()).updateIsFranchiseeDetail(status);

            }
        });
        String isSameFranchisee = ((PreBCADataEntryDetailsFragment) getParentFragment()).preBCADetailsDto.getSameAsFranchisee();
        if (!TextUtils.isEmpty(isSameFranchisee)) {
            int type = Integer.parseInt(isSameFranchisee);
            if (type == 1) {
                checkboxSameAsFranchisee.setChecked(true);
            } else {
                checkboxSameAsFranchisee.setChecked(false);
            }
        }

        //BCA Salutation
        spinnerBCASalutation.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, salutationList));
        int sPos = bcaEntryDetailsRepo.getDefaultOrSelectedChooserData(salutationList, preBCAPersonalDetailsDto.getBcaSalution());
        spinnerBCASalutation.setSelection(sPos);
        spinnerBCASalutation.setOnItemSelectedListener(this);

        //BCA First Name
        editTextBCAFirstName.setText(preBCAPersonalDetailsDto.getBcaFirstName());

        //BCA Middle Name
        editTextBCAMiddleName.setText(preBCAPersonalDetailsDto.getBcaMiddleName());

        //BCA Last Name
        editTextBCALastName.setText(preBCAPersonalDetailsDto.getBcaLastName());

        //BCA Father Salutation
        spinnerBCAFatherSalutation.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, salutationList));
        int sFPos = bcaEntryDetailsRepo.getDefaultOrSelectedChooserData(salutationList, preBCAPersonalDetailsDto.getBcaFatherNameSalution());
        spinnerBCAFatherSalutation.setSelection(sFPos);
        spinnerBCAFatherSalutation.setOnItemSelectedListener(this);

        //BCA Father First Name
        editTextBCAFatherFirstName.setText(preBCAPersonalDetailsDto.getBcaFatherFirstName());

        //BCA Father Middle Name
        editTextBCAFatherMiddleName.setText(preBCAPersonalDetailsDto.getBcaFatherMiddleName());

        //BCA Father Last Name
        editTextBCAFatherLastName.setText(preBCAPersonalDetailsDto.getBcaFatherLastName());

        //BCA Mobile Number
        editTextBCAMobileNo.setText(preBCAPersonalDetailsDto.getBcaMobileNumber());

        //BCA Alt Mobile Number
        editTextBCAALternateMobileNo.setText(preBCAPersonalDetailsDto.getBcaAltMobileNumber());

        //BCA Email Id
        editTextEmailID.setText(preBCAPersonalDetailsDto.getBcaEmailId());

        //Gender
        radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonMale) {
                    preBCAPersonalDetailsDto.setGender("0");
                } else if (checkedId == R.id.radioButtonFemale) {
                    preBCAPersonalDetailsDto.setGender("1");
                } else if (checkedId == R.id.radioButtonOthers) {
                    preBCAPersonalDetailsDto.setGender("2");
                }
            }
        });

        if (!TextUtils.isEmpty(preBCAPersonalDetailsDto.getGender())) {
            switch (preBCAPersonalDetailsDto.getGender().toUpperCase()) {
                case "0":
                    radioGroupGender.check(R.id.radioButtonMale);
                    break;

                case "1":
                    radioGroupGender.check(R.id.radioButtonFemale);
                    break;

                case "2":
                    radioGroupGender.check(R.id.radioButtonOthers);
                    break;

                default:
                    break;
            }
        }

        //Marital Status
        radioGroupMaritalStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonMarried) {
                    preBCAPersonalDetailsDto.setMaritalStatus("0");
                    layoutSpouseName.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.radioButtonSingle) {
                    preBCAPersonalDetailsDto.setMaritalStatus("3");
                    layoutSpouseName.setVisibility(View.GONE);
                }
            }
        });

        if (!TextUtils.isEmpty(preBCAPersonalDetailsDto.getMaritalStatus())) {
            switch (preBCAPersonalDetailsDto.getMaritalStatus().toUpperCase()) {
                case "0":
                    radioGroupMaritalStatus.check(R.id.radioButtonMarried);
                    break;

                case "3":
                    radioGroupMaritalStatus.check(R.id.radioButtonSingle);
                    break;

                default:
                    break;
            }
        }

        //BCA Spouse Salutation
        spinnerSpouseSalutation.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, salutationList));
        int spousePos = bcaEntryDetailsRepo.getDefaultOrSelectedChooserData(salutationList, preBCAPersonalDetailsDto.getBcaSpouseSalution());
        spinnerSpouseSalutation.setSelection(spousePos);
        spinnerSpouseSalutation.setOnItemSelectedListener(this);

        //Spouse First Name
        editTextSpouseFirstName.setText(preBCAPersonalDetailsDto.getBcaSpouseFirstName());

        //Spouse Middle Name
        editTextSpouseMiddleName.setText(preBCAPersonalDetailsDto.getBcaSpouseMiddleName());

        //Spouse Last Name
        editTextSpouseLastName.setText(preBCAPersonalDetailsDto.getBcaSpouseLastName());

        //Date Of Birth
        if (!TextUtils.isEmpty(preBCAPersonalDetailsDto.getBcaDOB())) {
            String DOB = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", preBCAPersonalDetailsDto.getBcaDOB());
            textViewBCADOB.setText(DOB);
        }

        //Highest Qualification
        spinnerQualification.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, qualificationList));
        int quaPos = bcaEntryDetailsRepo.getSelectedPos(qualificationList, preBCAPersonalDetailsDto.getQualification());
        spinnerQualification.setSelection(quaPos);
        spinnerQualification.setOnItemSelectedListener(this);

        //Year Of Passing
        if (!TextUtils.isEmpty(preBCAPersonalDetailsDto.getYearOfPassing())) {
            textViewYearOfPassing.setText(preBCAPersonalDetailsDto.getYearOfPassing());
        }

        //Date Of Passing
        if (!TextUtils.isEmpty(preBCAPersonalDetailsDto.getDateOfPassing())) {
            String DOP = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", preBCAPersonalDetailsDto.getDateOfPassing());
            textViewDateOfPassing.setText(DOP);
        }

        //Address Line 1
        editTextAddressLine1.setText(preBCAPersonalDetailsDto.getBcaAddressLine1());

        //Address Line 2
        editTextAddressLine2.setText(preBCAPersonalDetailsDto.getBcaAddressLine2());

        //LandMark
        editTextLandmark.setText(preBCAPersonalDetailsDto.getBcaLandmark());

        //Area
        editTextArea.setText(preBCAPersonalDetailsDto.getArea());

        //Locality
        editTextLocality.setText(preBCAPersonalDetailsDto.getLocality());

        //State
        setStateSpinnerAdapter(stateList, spinnerState, preBCAPersonalDetailsDto.getState());

        //Pin Code
        editTextPincode.setText(preBCAPersonalDetailsDto.getPincode());

        //CIBIL Score
        editTextCIBILScore.setText(preBCAPersonalDetailsDto.getCibilScore());

        //CIBIL Report Date
        if (!TextUtils.isEmpty(preBCAPersonalDetailsDto.getCibilReportDate())) {
            String DOB = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", preBCAPersonalDetailsDto.getCibilReportDate());
            textViewCIBILReportDate.setText(DOB);
        }

        //Name as per PAN Card
        editTextNameAsPerPANCard.setText(preBCAPersonalDetailsDto.getNameAsPanCard());

        //BCA Police Verification Date
        if (!TextUtils.isEmpty(preBCAPersonalDetailsDto.getBcaPoliceVerificationDate())) {
            String DOB = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", preBCAPersonalDetailsDto.getBcaPoliceVerificationDate());
            textViewBCAPoliceVerificationDate.setText(DOB);
        }

        //BCA Police Verification Copy
        if (!TextUtils.isEmpty(preBCAPersonalDetailsDto.getBcaPoliceVerificationId())) {
            String gstUrl = Constants.DownloadImageUrl + preBCAPersonalDetailsDto.getBcaPoliceVerificationId();
            Glide.with(context)
                    .load(gstUrl)
                    .apply(new RequestOptions()
                            .error(R.drawable.ic_camera_alt_black_72dp)
                            .placeholder(R.drawable.ic_camera_alt_black_72dp)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgBCAPoliceVerification);
        }

        //PAN Card Number
        editTextPANNumber1.setText(preBCAPersonalDetailsDto.getPanNumber());
        editTextPANNumber2.setText(preBCAPersonalDetailsDto.getPanNumber());

        //Pan Card Scan Copy
        if (!TextUtils.isEmpty(preBCAPersonalDetailsDto.getPanCardScanId())) {
            String gstUrl = Constants.DownloadImageUrl + preBCAPersonalDetailsDto.getPanCardScanId();
            Glide.with(context)
                    .load(gstUrl)
                    .apply(new RequestOptions()
                            .error(R.drawable.ic_camera_alt_black_72dp)
                            .placeholder(R.drawable.ic_camera_alt_black_72dp)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgPanScanCopy);
        }

        //Enable/disable views
        // GUIUtils.setViewAndChildrenEnabled(layoutBCABasicInfo, IsEditable);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getAllSpinnerData != null && !getAllSpinnerData.isCancelled()) {
            getAllSpinnerData.cancel(true);
        }

        if (asyncPanCardForBCAValidation != null && !asyncPanCardForBCAValidation.isCancelled()) {
            asyncPanCardForBCAValidation.cancel(true);
        }
    }

    private void spinner_focusablemode(CustomSearchableSpinner spinner) {
        /*spinner.setFocusable(true);
        spinner.setFocusableInTouchMode(true);*/
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int Id = parent.getId();

        if (Id == R.id.spinnerQualification) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto dto = (CustomFranchiseeApplicationSpinnerDto) spinnerQualification.getItemAtPosition(position);
                preBCAPersonalDetailsDto.setQualification(dto.getId());
            }
        } else if (Id == R.id.spinnerBCASalutation) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto dto = (CustomFranchiseeApplicationSpinnerDto) spinnerBCASalutation.getItemAtPosition(position);
                preBCAPersonalDetailsDto.setBcaSalution(dto.getId());
            }
        } else if (Id == R.id.spinnerBCAFatherSalutation) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto dto = (CustomFranchiseeApplicationSpinnerDto) spinnerBCAFatherSalutation.getItemAtPosition(position);
                preBCAPersonalDetailsDto.setBcaFatherNameSalution(dto.getId());
            }
        } else if (Id == R.id.spinnerSpouseSalutation) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto dto = (CustomFranchiseeApplicationSpinnerDto) spinnerSpouseSalutation.getItemAtPosition(position);
                preBCAPersonalDetailsDto.setBcaSpouseSalution(dto.getId());
            }
        } else if (Id == R.id.spinnerState) {
            if (position > 0) {
                spinnerState.setTitle("Select State");
                spinnerState.requestFocus();
                CustomFranchiseeApplicationSpinnerDto stateDto = (CustomFranchiseeApplicationSpinnerDto) spinnerState.getItemAtPosition(position);
                if (!stateDto.getId().equals("0")) {
                    preBCAPersonalDetailsDto.setState(stateDto.getId());

                    //Get District
                    getAllSpinnerData = new GetAllSpinnerData(preBCAPersonalDetailsDto.getState(), null, null, null);
                    getAllSpinnerData.execute("DISTRICT");
                }
            } else {
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

                //DISTRICT
                spinnerDistrict.setEnabled(IsEditable);
                setDistrictSpinnerAdapter(list1, spinnerDistrict, null);
                preBCAPersonalDetailsDto.setDistrict(null);

                //BLOCK
                spinnerTehsil.setEnabled(IsEditable);
                setBlockSpinnerAdapter(list1, spinnerTehsil, null);
                preBCAPersonalDetailsDto.setBlock(null);

                //VTC
                spinnerVTC.setEnabled(IsEditable);
                setVTCSpinnerAdapter(list1, spinnerVTC, null);
                preBCAPersonalDetailsDto.setVtc(null);

            }
        } else if (Id == R.id.spinnerDistrict) {
            if (position > 0) {
                spinnerDistrict.setTitle("Select District");
                spinnerDistrict.requestFocus();
                CustomFranchiseeApplicationSpinnerDto disDto = (CustomFranchiseeApplicationSpinnerDto) spinnerDistrict.getItemAtPosition(position);
                if (!disDto.getId().equals("0")) {
                    preBCAPersonalDetailsDto.setDistrict(disDto.getId());

                    //Get BLOCK
                    getAllSpinnerData = new GetAllSpinnerData(preBCAPersonalDetailsDto.getState(), preBCAPersonalDetailsDto.getDistrict(), null, null);
                    getAllSpinnerData.execute("VTC"); //BLOCK

                }
            } else {
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

                //BLOCK
                spinnerTehsil.setEnabled(IsEditable);
                setBlockSpinnerAdapter(list1, spinnerTehsil, null);
                preBCAPersonalDetailsDto.setBlock(null);

                //VTC
                spinnerVTC.setEnabled(IsEditable);
                setVTCSpinnerAdapter(list1, spinnerVTC, null);
                preBCAPersonalDetailsDto.setVtc(null);

            }
        } else if (Id == R.id.spinnerTehsil) {
            if (position > 0) {
                spinnerTehsil.setTitle("Select Block");
                spinnerTehsil.requestFocus();
                CustomFranchiseeApplicationSpinnerDto blockDto = (CustomFranchiseeApplicationSpinnerDto) spinnerTehsil.getItemAtPosition(position);
                if (!blockDto.getId().equals("0")) {
                    preBCAPersonalDetailsDto.setBlock(blockDto.getId());

                    //Get VTC
                    getAllSpinnerData = new GetAllSpinnerData(preBCAPersonalDetailsDto.getState(), preBCAPersonalDetailsDto.getDistrict(), preBCAPersonalDetailsDto.getBlock(), null);
                    getAllSpinnerData.execute("VTC");

                }
            } else {
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

                //VTC
                spinnerVTC.setEnabled(IsEditable);
                setVTCSpinnerAdapter(list1, spinnerVTC, null);
                preBCAPersonalDetailsDto.setVtc(null);
            }
        } else if (Id == R.id.spinnerVTC) {
            spinnerVTC.setTitle("Select VTC");
            spinnerVTC.requestFocus();
            CustomFranchiseeApplicationSpinnerDto vtcDto = (CustomFranchiseeApplicationSpinnerDto) spinnerVTC.getItemAtPosition(position);
            preBCAPersonalDetailsDto.setVtc(vtcDto.getId());

        } else if (Id == R.id.spinnerBankName1) {
            onBankSelection(position, spinnerBankName1, "BANK_1");
        } else if (Id == R.id.spinnerBankName2) {
            onBankSelection(position, spinnerBankName2, "BANK_2");
        } else if (Id == R.id.spinnerBankName3) {
            onBankSelection(position, spinnerBankName3, "BANK_3");
        } else if (Id == R.id.spinnerBranchName1) {
            onBranchSelection(position, spinnerBranchName1, "BRANCH_NAME_1");
        } else if (Id == R.id.spinnerBranchName2) {
            onBranchSelection(position, spinnerBranchName2, "BRANCH_NAME_2");
        } else if (Id == R.id.spinnerBranchName3) {
            onBranchSelection(position, spinnerBranchName3, "BRANCH_NAME_3");
        } else if (Id == R.id.spinnerApproxDistance1) {
            spinnerApproxDistance1.setTitle("Select Approx Distance");
            spinnerApproxDistance1.requestFocus();
            CustomFranchiseeApplicationSpinnerDto dto = (CustomFranchiseeApplicationSpinnerDto) spinnerApproxDistance1.getItemAtPosition(position);
            preBCAPersonalDetailsDto.setApproxDistance1(dto.getId());

        } else if (Id == R.id.spinnerApproxDistance2) {
            spinnerApproxDistance2.setTitle("Select Approx Distance");
            spinnerApproxDistance2.requestFocus();
            CustomFranchiseeApplicationSpinnerDto dto = (CustomFranchiseeApplicationSpinnerDto) spinnerApproxDistance2.getItemAtPosition(position);
            preBCAPersonalDetailsDto.setApproxDistance2(dto.getId());

        } else if (Id == R.id.spinnerApproxDistance3) {
            spinnerApproxDistance3.setTitle("Select Approx Distance");
            spinnerApproxDistance3.requestFocus();
            CustomFranchiseeApplicationSpinnerDto dto = (CustomFranchiseeApplicationSpinnerDto) spinnerApproxDistance3.getItemAtPosition(position);
            preBCAPersonalDetailsDto.setApproxDistance3(dto.getId());

        }
    }

    private void onBankSelection(int position, CustomSearchableSpinner spinnerBank, String FROM_BANK) {
        String TYPE_BRANCH = "";
        if (position > 0) {
            spinnerBank.setTitle("Select Bank Name");
            spinnerBank.requestFocus();
            CustomFranchiseeApplicationSpinnerDto dto = (CustomFranchiseeApplicationSpinnerDto) spinnerBank.getItemAtPosition(position);
            if (!dto.getId().equals("0")) {

                switch (FROM_BANK) {

                    case "BANK_1":
                        TYPE_BRANCH = "BRANCH_NAME_1";
                        preBCAPersonalDetailsDto.setBankName1Id(dto.getId());
                        break;

                    case "BANK_2":
                        TYPE_BRANCH = "BRANCH_NAME_2";
                        preBCAPersonalDetailsDto.setBankName2Id(dto.getId());
                        break;

                    case "BANK_3":
                        TYPE_BRANCH = "BRANCH_NAME_3";
                        preBCAPersonalDetailsDto.setBankName3Id(dto.getId());
                        break;

                    default:
                        break;
                }

                //Get Branch Name
                getAllSpinnerData = new GetAllSpinnerData(preBCAPersonalDetailsDto.getState(), preBCAPersonalDetailsDto.getDistrict(), null, dto.getId());
                getAllSpinnerData.execute(TYPE_BRANCH);
            }
        } else {
            List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
            list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

            switch (FROM_BANK) {

                case "BANK_1":
                    //BRANCH NAME
                    spinnerBranchName1.setEnabled(true);
                    setBranchNameSpinnerAdapter(list1, spinnerBranchName1, null);
                    preBCAPersonalDetailsDto.setBranchName1Id(null);

                    //APPROX DISTANCE
                    spinnerApproxDistance1.setEnabled(true);
                    setApproxDistanceSpinnerAdapter(list1, spinnerApproxDistance1, null);
                    preBCAPersonalDetailsDto.setApproxDistance1(null);
                    break;

                case "BANK_2":
                    //BRANCH NAME
                    spinnerBranchName2.setEnabled(true);
                    setBranchNameSpinnerAdapter(list1, spinnerBranchName2, null);
                    preBCAPersonalDetailsDto.setBranchName2Id(null);

                    //APPROX DISTANCE
                    spinnerApproxDistance2.setEnabled(true);
                    setApproxDistanceSpinnerAdapter(list1, spinnerApproxDistance2, null);
                    preBCAPersonalDetailsDto.setApproxDistance2(null);
                    break;

                case "BANK_3":
                    //BRANCH NAME
                    spinnerBranchName3.setEnabled(true);
                    setBranchNameSpinnerAdapter(list1, spinnerBranchName3, null);
                    preBCAPersonalDetailsDto.setBranchName3Id(null);

                    //APPROX DISTANCE
                    spinnerApproxDistance3.setEnabled(true);
                    setApproxDistanceSpinnerAdapter(list1, spinnerApproxDistance3, null);
                    preBCAPersonalDetailsDto.setApproxDistance3(null);
                    break;

                default:
                    break;
            }


        }
    }

    private void onBranchSelection(int position, CustomSearchableSpinner spinnerBranch, String FROM_BRANCH) {
        String TYPE_DISTANCE = "";
        String bankId = null;
        if (position > 0) {
            spinnerBranch.setTitle("Select Branch Name");
            spinnerBranch.requestFocus();
            CustomFranchiseeApplicationSpinnerDto dto = (CustomFranchiseeApplicationSpinnerDto) spinnerBranch.getItemAtPosition(position);
            if (!dto.getId().equals("0")) {

                switch (FROM_BRANCH) {

                    case "BRANCH_NAME_1":
                        TYPE_DISTANCE = "APPROX_DISTANCE_1";
                        preBCAPersonalDetailsDto.setBranchName1Id(dto.getId());
                        bankId = preBCAPersonalDetailsDto.getBankName1Id();
                        txtSelectedBranchName1.setVisibility(View.VISIBLE);
                        txtSelectedBranchName1.setText(dto.getName());
                        break;

                    case "BRANCH_NAME_2":
                        TYPE_DISTANCE = "APPROX_DISTANCE_2";
                        preBCAPersonalDetailsDto.setBranchName2Id(dto.getId());
                        bankId = preBCAPersonalDetailsDto.getBankName2Id();
                        txtSelectedBranchName2.setVisibility(View.VISIBLE);
                        txtSelectedBranchName2.setText(dto.getName());
                        break;

                    case "BRANCH_NAME_3":
                        TYPE_DISTANCE = "APPROX_DISTANCE_3";
                        preBCAPersonalDetailsDto.setBranchName3Id(dto.getId());
                        bankId = preBCAPersonalDetailsDto.getBankName3Id();
                        txtSelectedBranchName3.setVisibility(View.VISIBLE);
                        txtSelectedBranchName3.setText(dto.getName());
                        break;

                    default:
                        break;
                }

                //Get Branch Name
                getAllSpinnerData = new GetAllSpinnerData(preBCAPersonalDetailsDto.getState(), preBCAPersonalDetailsDto.getDistrict(), null, bankId);
                getAllSpinnerData.execute(TYPE_DISTANCE);
            }
        } else {
            List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
            list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

            switch (FROM_BRANCH) {

                case "BRANCH_NAME_1":

                    //APPROX DISTANCE
                    spinnerApproxDistance1.setEnabled(true);
                    setApproxDistanceSpinnerAdapter(list1, spinnerApproxDistance1, null);
                    preBCAPersonalDetailsDto.setApproxDistance1(null);
                    txtSelectedBranchName1.setVisibility(View.GONE);
                    txtSelectedBranchName1.setText("");
                    break;

                case "BRANCH_NAME_2":

                    //APPROX DISTANCE
                    spinnerApproxDistance2.setEnabled(true);
                    setApproxDistanceSpinnerAdapter(list1, spinnerApproxDistance2, null);
                    preBCAPersonalDetailsDto.setApproxDistance2(null);
                    txtSelectedBranchName2.setVisibility(View.GONE);
                    txtSelectedBranchName2.setText("");
                    break;

                case "BRANCH_NAME_3":
                    //APPROX DISTANCE
                    spinnerApproxDistance3.setEnabled(true);
                    setApproxDistanceSpinnerAdapter(list1, spinnerApproxDistance3, null);
                    preBCAPersonalDetailsDto.setApproxDistance3(null);
                    txtSelectedBranchName3.setVisibility(View.GONE);
                    txtSelectedBranchName3.setText("");
                    break;

                default:
                    break;
            }


        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //region Set State Adapter
    private void setStateSpinnerAdapter(List<CustomFranchiseeApplicationSpinnerDto> stateList, Spinner spinnerState, String selStateId) {

        //spinnerState.setEnabled(IsEditable);
        spinnerState.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, stateList));
        int appsstatePos = bcaEntryDetailsRepo.getSelectedPos(stateList, selStateId);
        spinnerState.setSelection(appsstatePos);
        spinnerState.setOnItemSelectedListener(this);

    }
    //endregion

    //region Set District Adapter
    private void setDistrictSpinnerAdapter(List<CustomFranchiseeApplicationSpinnerDto> districtList, Spinner spinnerDistrict, String selDistrictId) {

        //spinnerDistrict.setEnabled(IsEditable);
        spinnerDistrict.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, districtList));
        int appsstatePos = bcaEntryDetailsRepo.getSelectedPos(districtList, selDistrictId);
        spinnerDistrict.setSelection(appsstatePos);
        spinnerDistrict.setOnItemSelectedListener(this);

    }
    //endregion

    //region Set Block Adapter
    private void setBlockSpinnerAdapter(List<CustomFranchiseeApplicationSpinnerDto> blockList, Spinner spinner, String selBlockId) {

        //spinner.setEnabled(IsEditable);
        spinner.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, blockList));
        int blockPos = bcaEntryDetailsRepo.getSelectedPos(blockList, selBlockId);
        spinner.setSelection(blockPos);
        spinner.setOnItemSelectedListener(this);

    }
    //endregion

    //region Set VTC Adapter
    private void setVTCSpinnerAdapter(List<CustomFranchiseeApplicationSpinnerDto> VTCList, Spinner spinnerVTC, String selVTCId) {

        //spinnerVTC.setEnabled(IsEditable);
        spinnerVTC.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, VTCList));
        int appsstatePos = bcaEntryDetailsRepo.getSelectedPos(VTCList, selVTCId);
        spinnerVTC.setSelection(appsstatePos);
        spinnerVTC.setOnItemSelectedListener(this);
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

        if (Id == R.id.imgChoosePhoto) {
            FROM = BCA_PASSPORT_PHOTO;
            SEL_FILE_TYPE = " images";
            showAttachmentDialog(view, SEL_FILE_TYPE);

        } else if (Id == R.id.textViewYearOfPassing) {
            /*selectedDateTimeId = Id;
            showDateTimeDialogPicker();*/
            showMonthYearDialog();

        } else if (Id == R.id.textViewDateOfPassing) {
            selectedDateTimeId = Id;
            showDateTimeDialogPicker();

        } else if (Id == R.id.textViewBCADOB) {
            selectedDateTimeId = Id;
            showDateTimeDialogPicker();

        } else if (Id == R.id.textViewCIBILReportDate) {
            selectedDateTimeId = Id;
            showDateTimeDialogPicker();

        } else if (Id == R.id.textViewBCAPoliceVerificationDate) {
            selectedDateTimeId = Id;
            showDateTimeDialogPicker();

        } else if (Id == R.id.imgPanPReScanCopy) {
            FROM = PAN_CARD_SCAN_COPY;
            SEL_FILE_TYPE = " images";
            showAttachmentDialog(view, SEL_FILE_TYPE);

        } else if (Id == R.id.imgBCAPoliceVerification) {
            FROM = BCA_POLICE_VERIFICATION_COPY;
            SEL_FILE_TYPE = " images";
            showAttachmentDialog(view, SEL_FILE_TYPE);

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
                setImageAndName(fileName, base64Data, picUri);

            }
            if (requestCode == BROWSE_FOLDER_REQUEST && resultCode == Activity.RESULT_OK) {
                // Use the provided utility method to parse the result
                List<Uri> files = Utils.getSelectedFilesFromResult(data);
                for (Uri uri : files) {
                    File file = Utils.getFileForUri(uri);

                    //Check File size
                    int fileSize = CommonUtils.getFileSizeInMB(file);
                    if (fileSize > 1) {
                        showMessage(getString(R.string.file_size_msg));
                        return;
                    }

                    String ext = FileUtils.getFileExtension(context, uri);
                    String base64Data;

                    if (ext.equalsIgnoreCase("pdf")) {
                        base64Data = CommonUtils.encodeFileToBase64Binary(file);
                    } else {
                        file = new ImageZipper(context).setQuality(50).compressToFile(file);
                        Bitmap bitmapNew = new ImageZipper(context).compressToBitmap(file);
                        base64Data = CommonUtils.convertBitmapToString(bitmapNew);
                    }

                    String fileName = URLDecoder.decode(file.getName(), "UTF-8");
                    setImageAndName(fileName, base64Data, uri);

                }
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
                imageBitMap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);

            setScanCopyData(IsDrawable, imageBitMap, fileName, base64Data, ext);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setScanCopyData(boolean IsDrawable, Bitmap bitmap, String fileName, String base64, String ext) {

        switch (FROM) {

            case PAN_CARD_SCAN_COPY:
                if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgPanScanCopy);
                else {
                    try {
                        bitmap = ImageUtils.rotateImageIfRequired(getActivity().getContentResolver(), bitmap, picUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Glide.with(context).asBitmap().load(bitmap).into(imgPanScanCopy);
                }

                preBCAPersonalDetailsDto.setPanCardScanBase64(base64);
                preBCAPersonalDetailsDto.setPanCardScanExt(ext);
                break;


            case BCA_POLICE_VERIFICATION_COPY:
                if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgBCAPoliceVerification);
                else {
                    try {
                        bitmap = ImageUtils.rotateImageIfRequired(getActivity().getContentResolver(), bitmap, picUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Glide.with(context).asBitmap().load(bitmap).into(imgBCAPoliceVerification);
                }

                preBCAPersonalDetailsDto.setBcaPoliceVerificationBase64(base64);
                preBCAPersonalDetailsDto.setBcaPoliceVerificationExt(ext);
                break;

            default:
                break;

        }
    }

    public void showAttachmentDialog(final View view, final String fileType) {
        fileAttachementDialog = new FileAttachmentDialog(context, new FileAttachmentDialog.IFileAttachmentClicks() {
            @Override
            public void cameraClick() {
                //If the app has not the permission then asking for the permission
                permissionHandler.requestPermission(view, Manifest.permission.CAMERA, getString(R.string.needs_permission_camera_msg), new IPermission() {
                    @Override
                    public void IsPermissionGranted(boolean IsGranted) {
                        if (IsGranted) {
                            File file = CommonUtils.getOutputMediaFile(CommonUtils.FILE_IMAGE_TYPE);
                            Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            //picUri = Uri.fromFile(file); // create
                            picUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
                            i.putExtra(MediaStore.EXTRA_OUTPUT, picUri); // set the image file
                            i.putExtra("ImageId", picUri); // set the image file
                            startActivityForResult(i, CAMERA_PIC_REQUEST);
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
                            FilteredFilePickerActivity.FILE_TYPE = fileType;

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

    public void showMonthYearDialog() {

        if (monthYearPickerDialog != null && monthYearPickerDialog.isVisible()) {
            return;
        }

        monthYearPickerDialog = new MonthYearPickerDialog();
        monthYearPickerDialog.setListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                String monthString = new DateFormatSymbols().getMonths()[month];
                String monthConverted = "" + (month + 1);
                if (month < 10) {
                    monthConverted = "0" + monthConverted;
                }
                textViewYearOfPassing.setError(null);
                getDateConvert = year + "-" + monthConverted + "-" + dayOfMonth;
                textViewYearOfPassing.setText("" + year);
                preBCAPersonalDetailsDto.setYearOfPassing("" + year);
            }
        });
        monthYearPickerDialog.show(getFragmentManager(), "MonthYearPickerDialog");
        monthYearPickerDialog.setMonthVisiblity(false);

    }

    public void performSameAsFranchiseeActivity(String status) {

        if (status.equalsIgnoreCase("1")) {
            layoutCIBILBCAPolice.setVisibility(View.GONE);
            GUIUtils.setViewAndChildrenEnabled(layoutBCAGroup1, false);
            GUIUtils.setViewAndChildrenEnabled(layoutBCAGroup2, false);
            GUIUtils.setViewAndChildrenEnabled(layoutBCAGroup3, false);

            preBCAPersonalDetailsDto = prepareDataUsingFAData();
            bindDataUsingFAData();
        } else {
            layoutCIBILBCAPolice.setVisibility(View.VISIBLE);
            GUIUtils.setViewAndChildrenEnabled(layoutBCAGroup1, true);
            GUIUtils.setViewAndChildrenEnabled(layoutBCAGroup2, true);
            GUIUtils.setViewAndChildrenEnabled(layoutBCAGroup3, true);

            preBCAPersonalDetailsDto = new PreBCAPersonalDetailsDto();
            bindDataUsingFAData();
        }
    }

    private void bindDataUsingFAData() {
        spinner_focusablemode(spinnerState);
        spinner_focusablemode(spinnerDistrict);
        spinner_focusablemode(spinnerTehsil);
        spinner_focusablemode(spinnerVTC);

        //BCA Salutation
        spinnerBCASalutation.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, salutationList));
        int sPos = bcaEntryDetailsRepo.getSelectedPos(salutationList, preBCAPersonalDetailsDto.getBcaSalution());
        spinnerBCASalutation.setSelection(sPos);
        spinnerBCASalutation.setOnItemSelectedListener(this);

        //BCA First Name
        editTextBCAFirstName.setText(preBCAPersonalDetailsDto.getBcaFirstName());

        //BCA Middle Name
        editTextBCAMiddleName.setText(preBCAPersonalDetailsDto.getBcaMiddleName());

        //BCA Last Name
        editTextBCALastName.setText(preBCAPersonalDetailsDto.getBcaLastName());

        //BCA Father Salutation
        spinnerBCAFatherSalutation.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, salutationList));
        int sFPos = bcaEntryDetailsRepo.getSelectedPos(salutationList, preBCAPersonalDetailsDto.getBcaFatherNameSalution());
        spinnerBCAFatherSalutation.setSelection(sFPos);
        spinnerBCAFatherSalutation.setOnItemSelectedListener(this);

        //BCA Father First Name
        editTextBCAFatherFirstName.setText(preBCAPersonalDetailsDto.getBcaFatherFirstName());

        //BCA Father Middle Name
        editTextBCAFatherMiddleName.setText(preBCAPersonalDetailsDto.getBcaFatherMiddleName());

        //BCA Father Last Name
        editTextBCAFatherLastName.setText(preBCAPersonalDetailsDto.getBcaFatherLastName());

        //BCA Mobile Number
        editTextBCAMobileNo.setText(preBCAPersonalDetailsDto.getBcaMobileNumber());

        //BCA Alt Mobile Number
        editTextBCAALternateMobileNo.setText(preBCAPersonalDetailsDto.getBcaAltMobileNumber());

        //BCA Email Id
        editTextEmailID.setText(preBCAPersonalDetailsDto.getBcaEmailId());

        //Gender
        radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonMale) {
                    preBCAPersonalDetailsDto.setGender("0");
                } else if (checkedId == R.id.radioButtonFemale) {
                    preBCAPersonalDetailsDto.setGender("1");
                } else if (checkedId == R.id.radioButtonOthers) {
                    preBCAPersonalDetailsDto.setGender("2");
                }
            }
        });

        if (!TextUtils.isEmpty(preBCAPersonalDetailsDto.getGender())) {
            switch (preBCAPersonalDetailsDto.getGender().toUpperCase()) {
                case "0":
                    radioGroupGender.check(R.id.radioButtonMale);
                    break;

                case "1":
                    radioGroupGender.check(R.id.radioButtonFemale);
                    break;

                case "2":
                    radioGroupGender.check(R.id.radioButtonOthers);
                    break;

                default:
                    break;
            }
        } else {
            radioGroupGender.clearCheck();
        }

        //Marital Status
        radioGroupMaritalStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonMarried) {
                    preBCAPersonalDetailsDto.setMaritalStatus("0");
                    layoutSpouseName.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.radioButtonSingle) {
                    preBCAPersonalDetailsDto.setMaritalStatus("3");
                    layoutSpouseName.setVisibility(View.GONE);
                }
            }
        });

        if (!TextUtils.isEmpty(preBCAPersonalDetailsDto.getMaritalStatus())) {
            switch (preBCAPersonalDetailsDto.getMaritalStatus().toUpperCase()) {
                case "0":
                    radioGroupMaritalStatus.check(R.id.radioButtonMarried);
                    break;

                case "3":
                    radioGroupMaritalStatus.check(R.id.radioButtonSingle);
                    break;

                default:
                    break;
            }
        } else {
            radioGroupMaritalStatus.clearCheck();
        }

        //BCA Spouse Salutation
        spinnerSpouseSalutation.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, salutationList));
        int spousePos = bcaEntryDetailsRepo.getSelectedPos(salutationList, preBCAPersonalDetailsDto.getBcaSpouseSalution());
        spinnerSpouseSalutation.setSelection(spousePos);
        spinnerSpouseSalutation.setOnItemSelectedListener(this);

        //Spouse First Name
        editTextSpouseFirstName.setText(preBCAPersonalDetailsDto.getBcaSpouseFirstName());

        //Spouse Middle Name
        editTextSpouseMiddleName.setText(preBCAPersonalDetailsDto.getBcaSpouseMiddleName());

        //Spouse Last Name
        editTextSpouseLastName.setText(preBCAPersonalDetailsDto.getBcaSpouseLastName());

        //Date Of Birth
        if (!TextUtils.isEmpty(preBCAPersonalDetailsDto.getBcaDOB())) {
            String DOB = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", preBCAPersonalDetailsDto.getBcaDOB());
            textViewBCADOB.setText(DOB);
        } else {
            textViewBCADOB.setText("");
        }

        //Highest Qualification
        spinnerQualification.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, qualificationList));
        int quaPos = bcaEntryDetailsRepo.getSelectedPos(qualificationList, preBCAPersonalDetailsDto.getQualification());
        spinnerQualification.setSelection(quaPos);
        spinnerQualification.setOnItemSelectedListener(this);

        //Year Of Passing
        if (!TextUtils.isEmpty(preBCAPersonalDetailsDto.getYearOfPassing())) {
            textViewYearOfPassing.setText(preBCAPersonalDetailsDto.getYearOfPassing());
        } else {
            textViewYearOfPassing.setText("");
        }

        //Date Of Passing
        if (!TextUtils.isEmpty(preBCAPersonalDetailsDto.getDateOfPassing())) {
            String DOP = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", preBCAPersonalDetailsDto.getDateOfPassing());
            textViewDateOfPassing.setText(DOP);
        } else {
            textViewDateOfPassing.setText("");
        }

        //Address Line 1
        editTextAddressLine1.setText(preBCAPersonalDetailsDto.getBcaAddressLine1());

        //Address Line 2
        editTextAddressLine2.setText(preBCAPersonalDetailsDto.getBcaAddressLine2());

        //LandMark
        editTextLandmark.setText(preBCAPersonalDetailsDto.getBcaLandmark());

        //Area
        editTextArea.setText(preBCAPersonalDetailsDto.getArea());

        //Locality
        editTextLocality.setText(preBCAPersonalDetailsDto.getLocality());

        //State
        setStateSpinnerAdapter(stateList, spinnerState, preBCAPersonalDetailsDto.getState());

        //Pin Code
        editTextPincode.setText(preBCAPersonalDetailsDto.getPincode());

        //CIBIL Score
        editTextCIBILScore.setText(preBCAPersonalDetailsDto.getCibilScore());

        //CIBIL Report Date
        if (!TextUtils.isEmpty(preBCAPersonalDetailsDto.getCibilReportDate())) {
            String DOB = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", preBCAPersonalDetailsDto.getCibilReportDate());
            textViewCIBILReportDate.setText(DOB);
        }

        //BCA Police Verification Date
        if (!TextUtils.isEmpty(preBCAPersonalDetailsDto.getBcaPoliceVerificationDate())) {
            String verificationDate = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", preBCAPersonalDetailsDto.getBcaPoliceVerificationDate());
            textViewBCAPoliceVerificationDate.setText(verificationDate);
        }

        //Name as per PAN Card
        editTextNameAsPerPANCard.setText(preBCAPersonalDetailsDto.getNameAsPanCard());

        //BCA Police Verification Copy
        if (!TextUtils.isEmpty(preBCAPersonalDetailsDto.getBcaPoliceVerificationId())) {
            String gstUrl = Constants.DownloadImageUrl + preBCAPersonalDetailsDto.getBcaPoliceVerificationId();
            Glide.with(context)
                    .load(gstUrl)
                    .apply(new RequestOptions()
                            .error(R.drawable.ic_camera_alt_black_72dp)
                            .placeholder(R.drawable.ic_camera_alt_black_72dp)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgBCAPoliceVerification);
        } else {
            Glide.with(context).load(R.drawable.ic_camera_alt_black_72dp).into(imgBCAPoliceVerification);
        }

        //PAN Card Number
        editTextPANNumber1.setText(preBCAPersonalDetailsDto.getPanNumber());
        editTextPANNumber2.setText(preBCAPersonalDetailsDto.getPanNumber());

        //Pan Card Scan Copy
        if (!TextUtils.isEmpty(preBCAPersonalDetailsDto.getPanCardScanId())) {
            String gstUrl = Constants.DownloadImageUrl + preBCAPersonalDetailsDto.getPanCardScanId();
            Glide.with(context)
                    .load(gstUrl)
                    .apply(new RequestOptions()
                            .error(R.drawable.ic_camera_alt_black_72dp)
                            .placeholder(R.drawable.ic_camera_alt_black_72dp)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgPanScanCopy);
        } else {
            Glide.with(context).load(R.drawable.ic_camera_alt_black_72dp).into(imgPanScanCopy);
        }
    }

    private PreBCAPersonalDetailsDto prepareDataUsingFAData() {
        String faData = ((PreBCADataEntryDetailsFragment) getParentFragment()).preBCADetailsDto.getFaData();

        if (TextUtils.isEmpty(faData)) {
            return preBCAPersonalDetailsDto;
        }

        Gson gson = new GsonBuilder().create();
        FADataDto faDataDto = gson.fromJson(faData, FADataDto.class);

        PreBCAPersonalDetailsDto personalDetailsDto = new PreBCAPersonalDetailsDto();

        if (!TextUtils.isEmpty(faDataDto.getGender())) {

            switch (faDataDto.getGender().toUpperCase()) {
                case "M":
                    personalDetailsDto.setGender("0");
                    break;

                case "F":
                    personalDetailsDto.setGender("1");
                    break;

                case "O":
                    personalDetailsDto.setGender("2");
                    break;

                default:
                    break;
            }
        } else {
            personalDetailsDto.setGender(faDataDto.getGender());
        }
        personalDetailsDto.setBcaFatherNameSalution(faDataDto.getfTitleId());
        personalDetailsDto.setBcaFatherFirstName(faDataDto.getfFName());
        personalDetailsDto.setBcaFatherMiddleName(faDataDto.getfMName());
        personalDetailsDto.setBcaFatherLastName(faDataDto.getfLName());
        personalDetailsDto.setBcaSalution(faDataDto.getFrTitleId());
        personalDetailsDto.setBcaFirstName(faDataDto.getFrFName());
        personalDetailsDto.setBcaMiddleName(faDataDto.getFrMName());
        personalDetailsDto.setBcaLastName(faDataDto.getFrLname());
        personalDetailsDto.setBcaMobileNumber(faDataDto.getFrMobileNumber());
        personalDetailsDto.setBcaAltMobileNumber(faDataDto.getFrAltMobileNumber());
        personalDetailsDto.setBcaEmailId(faDataDto.getFrEmailId());
        personalDetailsDto.setBcaDOB(faDataDto.getDob());

        if (!TextUtils.isEmpty(faDataDto.getMaritalStatus())) {

            switch (faDataDto.getMaritalStatus().toUpperCase()) {
                case "M":
                    personalDetailsDto.setMaritalStatus("0");
                    break;

                case "U":
                    personalDetailsDto.setMaritalStatus("3");
                    break;

                default:
                    break;
            }
        } else {
            personalDetailsDto.setMaritalStatus(faDataDto.getGender());
        }

        personalDetailsDto.setBcaSpouseSalution(faDataDto.getSpouseTitleId());
        personalDetailsDto.setBcaSpouseFirstName(faDataDto.getSpouseFName());
        personalDetailsDto.setBcaSpouseMiddleName(faDataDto.getSpouseMName());
        personalDetailsDto.setBcaSpouseLastName(faDataDto.getSpouseLName());
        personalDetailsDto.setQualification(faDataDto.getQualification());
        personalDetailsDto.setNameAsPanCard(faDataDto.getPanCardHolderName());
        personalDetailsDto.setPanNumber(faDataDto.getPannumber());
        personalDetailsDto.setPanCardScanId(faDataDto.getPanFile());
        personalDetailsDto.setBcaAddressLine1(faDataDto.getComAddress1());
        personalDetailsDto.setBcaAddressLine2(faDataDto.getComAddress2());
        personalDetailsDto.setState(faDataDto.getComStateCode());
        personalDetailsDto.setDistrict(faDataDto.getComDistrictCode());
        personalDetailsDto.setVtc(faDataDto.getComVillageCode());
        personalDetailsDto.setBcaLandmark(faDataDto.getComLandmark());
        personalDetailsDto.setPincode(faDataDto.getComPincode());

        return personalDetailsDto;
    }

    //region Check PAN Card for BCA Detail
    public void isPanCardValidationCheck(String panNumber, String pancardName) {

        asyncPanCardForBCAValidation = new AsyncPanCardForBCAValidation(context, panNumber, pancardName, new AsyncPanCardForBCAValidation.Callback() {
            @Override
            public void onResult(String result) {
                try {

                    if (TextUtils.isEmpty(result)) {
                        IsPanValidated = false;
                        AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                        return;
                    }
                    if (result.startsWith("OKAY|")) {
                        IsPanValidated = true;

                        GUIUtils.setViewAndChildrenEnabled(layoutNameAsPerPANCard, false);
                        GUIUtils.setViewAndChildrenEnabled(layoutPANNumber, false);

                        layoutIsPancardValid.setVisibility(View.VISIBLE);
                        txtIsPanCardValid.setVisibility(View.VISIBLE);
                        txtIsPanCardValid.setText(context.getString(in.vakrangee.franchisee.nextgenfranchiseeapplication.R.string.fa_circle_check));
                        txtIsPanCardValid.setTypeface(VakrangeeKendraApplication.font_awesome, Typeface.BOLD);
                        txtIsPanCardValid.setTextColor(deprecateHandler.getColor(R.color.green));

                    } else if (result.startsWith("ERROR|")) {
                        IsPanValidated = false;
                        layoutIsPancardValid.setVisibility(View.GONE);
                        txtIsPanCardValid.setVisibility(View.GONE);
                        result = result.replace("ERROR|", "");
                        if (TextUtils.isEmpty(result)) {
                            AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                        } else {
                            AlertDialogBoxInfo.alertDialogShow(context, result);
                        }
                    } else {
                        IsPanValidated = false;
                        layoutIsPancardValid.setVisibility(View.GONE);
                        txtIsPanCardValid.setVisibility(View.GONE);
                        AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                }
            }
        });
        asyncPanCardForBCAValidation.execute();

    }
    //endregion

}
