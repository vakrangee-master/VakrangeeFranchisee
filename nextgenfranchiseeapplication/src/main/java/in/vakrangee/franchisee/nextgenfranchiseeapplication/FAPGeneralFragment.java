package in.vakrangee.franchisee.nextgenfranchiseeapplication;

import android.Manifest;
import android.app.Activity;
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
import android.text.Html;
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
import android.widget.RadioButton;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.core.content.FileProvider;
import butterknife.ButterKnife;
import in.vakrangee.supercore.franchisee.application.VakrangeeKendraApplication;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.commongui.DateTimePickerDialog;
import in.vakrangee.supercore.franchisee.commongui.FileAttachmentDialog;
import in.vakrangee.supercore.franchisee.commongui.FileUtils;
import in.vakrangee.supercore.franchisee.commongui.FilteredFilePickerActivity;
import in.vakrangee.supercore.franchisee.nextgenfranchiseeapplication.CustomFranchiseeApplicationSpinnerDto;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.FranchiseeApplicationRepository;
import in.vakrangee.supercore.franchisee.utils.GUIUtils;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;
import in.vakrangee.supercore.franchisee.utils.ImageZipper;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;
import in.vakrangee.supercore.franchisee.utils.CustomSearchableSpinner;

public class FAPGeneralFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "FAPGeneralFragment";
    private Context context;
    private DeprecateHandler deprecateHandler;
    private PermissionHandler permissionHandler;
    private int FROM = -1;
    private static final int PAN_CARD_SCAN_COPY = 1;
    private static final int AADHAAR_CARD_SCAN_COPY = 2;
    private static final int ADDRESS_PROOF_SCAN_COPY = 3;
    private static final int GSTIN_SCAN_COPY = 4;
    private static final int MSME_SCAN_COPY = 5;
    private static final int CIBIL_SCAN_COPY = 6;
    private static final int POLICE_VERIFICATION_SCAN_COPY = 7;

    private FAPGeneralInfoDto fapGeneralInfoDto;
    private String SEL_FILE_TYPE;
    private FileAttachmentDialog fileAttachementDialog;
    private static final int CAMERA_PIC_REQUEST = 100;
    private static final int BROWSE_FOLDER_REQUEST = 101;
    private Uri picUri;                 //Picture URI
    private List<CustomFranchiseeApplicationSpinnerDto> AddressProofTypeList;
    private List<CustomFranchiseeApplicationSpinnerDto> gSTINStateList;
    private FranchiseeApplicationRepository fapRepo;
    private GetAllGeneralDetailSpinnerData getAllGeneralDetailSpinnerData = null;
    private boolean IsHidden = false;
    private boolean IsEditable = false;
    private boolean edittext_listener = true;
    private AsyncPanCardValidation asyncPanCardValidation;

    private LinearLayout layoutAadhaarSection;
    private CheckBox checkboxAppliedForPanCard;
    private EditText editTextNameAsPerPANCard;
    private EditText editTextPANNumber1;
    private EditText editTextPANNumber2;
    private ImageView imgPANScanCopy;
    private TextView txtPANScanCopyName;
    private RadioGroup radioGroupAadhaarEID;
    private RadioButton radioButtonAadhaar;
    private RadioButton radioButtonEID;
    private EditText editTextAadhaarNumber1;
    private EditText editTextAadhaarNumber2;
    private EditText editTextNameOnAadhaarCard;
    private ImageView imgAadhaarScanCopy;
    private TextView txtAadhaarScanCopyName;
    private Spinner spinnerAddressProofType;
    private CustomSearchableSpinner spinnerGSTINState;
    private EditText editTextAddressProofNumber1;
    private EditText editTextAddressProofNumber2;
    private ImageView imgAddressProofScanCopy;
    private TextView txtAddressProofScanCopyName;
    private EditText editTextNameAsPerGSTINCertificate;
    private EditText editTextGSTINNumber;
    private ImageView imgGSTINUpload;
    private TextView txtGSTINUploadName;
    private TextView txtNameAsPerPANCardLbl;
    private TextView txtPANNumberLbl;
    private TextView txtPANChooseFile;
    private TextView txtAadharEIDLbl;
    private TextView txtAadhaarNumberLbl;
    private TextView txtNameOnAadhaarCardLbl;
    private TextView txtAadhaarChooseFile;
    private TextView txtAddressProofTypeLbl;
    private TextView txtAddressProofNumberLbl;
    private TextView txtAddressProofChooseFile;
    private LinearLayout layoutPANNumber;
    private LinearLayout layoutNameAsPerPANCard;
    private LinearLayout layoutGeneralParent;
    private TextView txtIsPanCardValid;
    private LinearLayout layoutIsPancardValid;
    private static final String ENTER_AADHAAR_NUMBER = "Please enter Aadhaar Number.";
    private static final String ENTER_PAN_NUMBER = "Please enter PAN Number.";
    private static final String COLOR_000000 = "#000000";
    private static final String NOT_MATCH = "Not Match";
    private static final String ENTER_PAN_NAME = "Please enter Name as per PAN Card.";
    private static final String IMAGES_PDF = "images/pdf";

    private TextView textViewGSTINIssueDate;
    private Date gstIssueDate;
    private String strGstIssueDate;
    private Date msmeIssuingDate;
    private String strMSMEIssuingDate;
    private Date cibilReportDate;
    private String strCibilReportDate;
    private Date policeVerificationDate;
    private String strPoliceVerificationDate;
    private int selectedDateTimeId = 0;
    private DateTimePickerDialog dateTimePickerDialog;
    private DateFormat dateFormatter2 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    private DateFormat dateFormatterYMD = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private View view;
    private EditText editTextNameOnMSMECertificate, editTextMSMECertificateNumber;
    private TextView textViewIssuingDateOfMSME;
    private ImageView imgMSMECerificationImg;
    private LinearLayout layoutCIBILBCAPolice,layoutGSTINCertificate,layoutMSMECertificate;
    private EditText editTextCIBILScore;
    private TextView textViewCIBILReportDate, textViewBCAPoliceVerificationDate, txtCIBILScoreNote;
    private ImageView imgCIBILProof, imgBCAPoliceVerification;
    public String cibilNote = "Cibil score should be NA/(0) or NH/(-1) or between 1 to 5 or between 300 to 900.";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_fap_general, container, false);

        bindViewId(view);
        //Data
        this.context = getContext();
        deprecateHandler = new DeprecateHandler(context);
        permissionHandler = new PermissionHandler(getActivity());
        fapRepo = new FranchiseeApplicationRepository(context);
        ButterKnife.bind(this, view);

        //Input filter
        CommonUtils.InputFiletrWithMaxLength(editTextNameAsPerPANCard, "~#^|$%&*!", 50);
        CommonUtils.InputFiletrWithMaxLength(editTextPANNumber1, "~#^|$%&*!", 10);
        CommonUtils.InputFiletrWithMaxLength(editTextPANNumber2, "~#^|$%&*!", 10);
        CommonUtils.InputFiletrWithMaxLength(editTextNameOnAadhaarCard, "~#^|$%&*!", 50);
        CommonUtils.InputFiletrWithMaxLength(editTextAddressProofNumber1, "~#^|$%&*!", 20);
        CommonUtils.InputFiletrWithMaxLength(editTextAddressProofNumber2, "~#^|$%&*!", 20);
        CommonUtils.InputFiletrWithMaxLength(editTextNameAsPerGSTINCertificate, "~#^|$%&*!", 50);
        CommonUtils.InputFiletrWithMaxLength(editTextGSTINNumber, "~#^|$%&*!", 15);
        CommonUtils.InputFiletrWithMaxLength(editTextNameOnMSMECertificate, "~#^|$%&*!", 50);

        //Set Compulsory mark
        TextView[] txtViewsForCompulsoryMark = {txtNameAsPerPANCardLbl, txtPANNumberLbl, txtPANChooseFile, txtAadharEIDLbl, txtAadhaarNumberLbl, txtNameOnAadhaarCardLbl,
                txtAadhaarChooseFile, txtAddressProofTypeLbl, txtAddressProofNumberLbl, txtAddressProofChooseFile};
        GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);

        textChangedListenerGSTINPANcard();
        textChangedListenerAadhaarCard();
        textChangedListenerAddressProofNumber();

        //Hide and Show Aadhaar section depending upon the flag
        IsHidden = Constants.IsAadhaarSectionToHidden;
        if (IsHidden)
            layoutAadhaarSection.setVisibility(View.GONE);
        else
            layoutAadhaarSection.setVisibility(View.VISIBLE);

        return view;
    }

    private void bindViewId(View view) {
        //region References
        layoutAadhaarSection = view.findViewById(R.id.layoutAadhaarSection);
        checkboxAppliedForPanCard = view.findViewById(R.id.checkboxAppliedForPanCard);
        editTextNameAsPerPANCard = view.findViewById(R.id.editTextNameAsPerPANCard);
        editTextPANNumber1 = view.findViewById(R.id.editTextPANNumber1);
        editTextPANNumber2 = view.findViewById(R.id.editTextPANNumber2);
        imgPANScanCopy = view.findViewById(R.id.imgPANScanCopy);
        txtPANScanCopyName = view.findViewById(R.id.txtPANScanCopyName);
        radioGroupAadhaarEID = view.findViewById(R.id.radioGroupAadhaarEID);
        radioButtonAadhaar = view.findViewById(R.id.radioButtonAadhaar);
        radioButtonEID = view.findViewById(R.id.radioButtonEID);
        editTextAadhaarNumber1 = view.findViewById(R.id.editTextAadhaarNumber1);
        editTextAadhaarNumber2 = view.findViewById(R.id.editTextAadhaarNumber2);
        editTextNameOnAadhaarCard = view.findViewById(R.id.editTextNameOnAadhaarCard);
        imgAadhaarScanCopy = view.findViewById(R.id.imgAadhaarScanCopy);
        txtAadhaarScanCopyName = view.findViewById(R.id.txtAadhaarScanCopyName);
        spinnerAddressProofType = view.findViewById(R.id.spinnerAddressProofType);
        spinnerGSTINState = view.findViewById(R.id.spinnerGSTINStateCode);
        editTextAddressProofNumber1 = view.findViewById(R.id.editTextAddressProofNumber1);
        editTextAddressProofNumber2 = view.findViewById(R.id.editTextAddressProofNumber2);
        imgAddressProofScanCopy = view.findViewById(R.id.imgAddressProofScanCopy);
        txtAddressProofScanCopyName = view.findViewById(R.id.txtAddressProofScanCopyName);
        editTextNameAsPerGSTINCertificate = view.findViewById(R.id.editTextNameAsPerGSTINCertificate);
        editTextGSTINNumber = view.findViewById(R.id.editTextGSTINNumber);
        imgGSTINUpload = view.findViewById(R.id.imgGSTINUpload);
        txtGSTINUploadName = view.findViewById(R.id.txtGSTINUploadName);
        txtNameAsPerPANCardLbl = view.findViewById(R.id.txtNameAsPerPANCardLbl);
        txtPANNumberLbl = view.findViewById(R.id.txtPANNumberLbl);
        txtPANChooseFile = view.findViewById(R.id.txtPANChooseFile);
        txtAadharEIDLbl = view.findViewById(R.id.txtAadharEIDLbl);
        txtAadhaarNumberLbl = view.findViewById(R.id.txtAadhaarNumberLbl);
        txtNameOnAadhaarCardLbl = view.findViewById(R.id.txtNameOnAadhaarCardLbl);
        txtAadhaarChooseFile = view.findViewById(R.id.txtAadhaarChooseFile);
        txtAddressProofTypeLbl = view.findViewById(R.id.txtAddressProofTypeLbl);
        txtAddressProofNumberLbl = view.findViewById(R.id.txtAddressProofNumberLbl);
        txtAddressProofChooseFile = view.findViewById(R.id.txtAddressProofChooseFile);
        layoutPANNumber = view.findViewById(R.id.layoutPANNumber);
        layoutNameAsPerPANCard = view.findViewById(R.id.layoutNameAsPerPANCard);
        layoutGeneralParent = view.findViewById(R.id.layoutGeneralParent);
        txtIsPanCardValid = view.findViewById(R.id.txtIsPanCardValid);
        layoutIsPancardValid = view.findViewById(R.id.layoutIsPancardValid);
        textViewGSTINIssueDate = view.findViewById(R.id.textViewGSTINIssueDate);

        editTextNameOnMSMECertificate = view.findViewById(R.id.editTextNameOnMSMECertificate);
        editTextMSMECertificateNumber = view.findViewById(R.id.editTextMSMECertificateNumber);
        textViewIssuingDateOfMSME = view.findViewById(R.id.textViewIssuingDateOfMSME);
        imgMSMECerificationImg = view.findViewById(R.id.imgMSMECerificationImg);

        layoutCIBILBCAPolice = view.findViewById(R.id.layoutCIBILBCAPolice);
        layoutGSTINCertificate = view.findViewById(R.id.layoutGSTINCertificate);
        layoutMSMECertificate = view.findViewById(R.id.layoutMSMECertificate);
        editTextCIBILScore = view.findViewById(R.id.editTextCIBILScore);
        textViewCIBILReportDate = view.findViewById(R.id.textViewCIBILReportDate);
        textViewBCAPoliceVerificationDate = view.findViewById(R.id.textViewBCAPoliceVerificationDate);
        imgCIBILProof = view.findViewById(R.id.imgCIBILProof);
        imgBCAPoliceVerification = view.findViewById(R.id.imgBCAPoliceVerification);
        txtCIBILScoreNote = view.findViewById(R.id.txtCIBILScoreNote);
        String cibilmsg = "<b>NOTE:</b> "+cibilNote;
        txtCIBILScoreNote.setText(Html.fromHtml(cibilmsg));

        textViewCIBILReportDate.setOnClickListener(this);
        textViewBCAPoliceVerificationDate.setOnClickListener(this);
        imgCIBILProof.setOnClickListener(this);
        imgBCAPoliceVerification.setOnClickListener(this);

        textViewGSTINIssueDate.setOnClickListener(this);
        textViewIssuingDateOfMSME.setOnClickListener(this);
        imgMSMECerificationImg.setOnClickListener(this);

        imgPANScanCopy.setOnClickListener(this);
        imgAadhaarScanCopy.setOnClickListener(this);
        imgAddressProofScanCopy.setOnClickListener(this);
        imgGSTINUpload.setOnClickListener(this);

        editTextCIBILScore.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Do Nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int len = editTextCIBILScore.getText().toString().trim().length();
                if (len <= 0)
                    return;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int len = editTextCIBILScore.getText().toString().trim().length();
                if (len <= 0)
                    return;

                if (editTextCIBILScore.getText().toString().contains(" ")) {
                    editTextCIBILScore.setText(editTextCIBILScore.getText().toString().replaceAll(" ", ""));
                    editTextCIBILScore.setSelection(editTextCIBILScore.getText().length());
                }

                String s = editTextCIBILScore.getText().toString().trim();
                CommonUtils.setWordsCaps(s, editTextCIBILScore);

            }
        });
    }

    //region TextChangedListener_AddressProof_Number
    private void textChangedListenerAddressProofNumber() {
        CommonUtils.EditextListener(editTextAddressProofNumber1, editTextAddressProofNumber2, 1, "Please enter minimum 1 characters", "AddressProof");
    }
    //endregion

    //region TextChangedListener_AadhaarCard
    private void textChangedListenerAadhaarCard() {
        CommonUtils.EditextListener(editTextAadhaarNumber1, editTextAadhaarNumber2, 12, "Please enter a valid Aadhaar number.", "Aadhaarcard");
    }
    //endregion

    //region TextChangedListener_GSTIN and PANcard
    private void textChangedListenerGSTINPANcard() {
        //--------------------------GSTIN Number
        editTextGSTINNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Do Nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int len = editTextGSTINNumber.getText().toString().trim().length();
                if (len <= 0)
                    return;

                if (!CommonUtils.GSTINisValid(String.valueOf(charSequence))) {
                    editTextGSTINNumber.setTextColor(Color.parseColor("#FF0000"));
                    editTextGSTINNumber.setError(getResources().getString(R.string.hintGsTnumbernotValid));
                } else {
                    editTextGSTINNumber.setTextColor(Color.parseColor("#468847"));
                    editTextGSTINNumber.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Do Nothing
            }
        });
        //----------------------------------PAN Card----------------------------------

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

    //region pan card validation check
    public void isPanCardValidationCheck(String panNumber, String pancardName) {

        asyncPanCardValidation = new AsyncPanCardValidation(context, panNumber,
                pancardName, new AsyncPanCardValidation.Callback() {
            @Override
            public void onResult(String result) {
                try {

                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                        return;
                    }
                    if (result.startsWith("OKAY|")) {

                        //@Vasundhara: Set Editable if Send back for correction
                        if (((NextGenFranchiseeApplicationFormFragment) getParentFragment()).statusCode == 6) {
                            GUIUtils.setViewAndChildrenEnabled(layoutNameAsPerPANCard, true);
                            GUIUtils.setViewAndChildrenEnabled(layoutPANNumber, true);
                        } else {
                            GUIUtils.setViewAndChildrenEnabled(layoutNameAsPerPANCard, false);
                            GUIUtils.setViewAndChildrenEnabled(layoutPANNumber, false);
                        }

                        layoutIsPancardValid.setVisibility(View.VISIBLE);
                        txtIsPanCardValid.setVisibility(View.VISIBLE);
                        txtIsPanCardValid.setText(context.getString(R.string.fa_circle_check));
                        txtIsPanCardValid.setTypeface(VakrangeeKendraApplication.font_awesome, Typeface.BOLD);
                        txtIsPanCardValid.setTextColor(deprecateHandler.getColor(R.color.green));

                    } else if (result.startsWith("ERROR|")) {
                        layoutIsPancardValid.setVisibility(View.GONE);
                        txtIsPanCardValid.setVisibility(View.GONE);
                        result = result.replace("ERROR|", "");
                        if (TextUtils.isEmpty(result)) {
                            AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                        } else {
                            AlertDialogBoxInfo.alertDialogShow(context, result);
                        }
                    } else {
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
        asyncPanCardValidation.execute();

    }

    private void BindSpinner() {

        spinner_focusablemode(spinnerGSTINState);

        //STEP 1-Address Proof Type
        CustomFranchiseeApplicationSpinnerAdapter vakrnageeKendramodelapdater = new CustomFranchiseeApplicationSpinnerAdapter(context, AddressProofTypeList);
        spinnerAddressProofType.setAdapter(vakrnageeKendramodelapdater);
        int appvakrangeeKendraModelPos = fapRepo.getSelectedPos(AddressProofTypeList, fapGeneralInfoDto.getAddressProofType());
        spinnerAddressProofType.setSelection(appvakrangeeKendraModelPos);
        spinnerAddressProofType.setOnItemSelectedListener(this);

        //STEP 2- Aadharcard or EID
        radioGroupAadhaarEID.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonAadhaar) {
                    fapGeneralInfoDto.setIsAadhaarEID("1");
                } else if (checkedId == R.id.radioButtonEID) {
                    fapGeneralInfoDto.setIsAadhaarEID("0");
                }
            }
        });
        //STEP 3 -Check box click Listener
        checkboxAppliedForPanCard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean IsChecked) {
                String status = IsChecked ? "1" : "0";
                fapGeneralInfoDto.setIsHavingPANCard(status);
                if (status.equals("1")) {
                    layoutNameAsPerPANCard.setVisibility(View.GONE);
                    layoutPANNumber.setVisibility(View.GONE);
                    txtPANChooseFile.setText("PAN Card Receipt/Acknowledgement scanned copy");
                    TextView[] txtViewsForCompulsoryMark = {txtPANChooseFile};
                    GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);
                } else {
                    layoutNameAsPerPANCard.setVisibility(View.VISIBLE);
                    layoutPANNumber.setVisibility(View.VISIBLE);
                    txtPANChooseFile.setText("PAN Card scanned copy");
                    TextView[] txtViewsForCompulsoryMark = {txtPANChooseFile};
                    GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);
                }
            }
        });

        //STEP 4 - Name As per Pan card
        editTextNameAsPerPANCard.setText(fapGeneralInfoDto.getNameOnPANCard());
        //STEP 5 -PAN Card Number
        editTextPANNumber1.setText(fapGeneralInfoDto.getPanNumber1());
        //STEP 6 -PAN Card Number
        editTextPANNumber2.setText(fapGeneralInfoDto.getPanNumber1());
        //STEP 7 -Check box Applied Pan card
        if (!TextUtils.isEmpty(fapGeneralInfoDto.getIsHavingPANCard())) {
            int type = Integer.parseInt(fapGeneralInfoDto.getIsHavingPANCard());
            if (type == 1) {
                checkboxAppliedForPanCard.setChecked(true);
                layoutNameAsPerPANCard.setVisibility(View.GONE);
                layoutPANNumber.setVisibility(View.GONE);
                txtPANChooseFile.setText("PAN Card Receipt/Acknowledgement scanned copy");
                TextView[] txtViewsForCompulsoryMark = {txtPANChooseFile};
                GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);
            } else {
                checkboxAppliedForPanCard.setChecked(false);
                layoutNameAsPerPANCard.setVisibility(View.VISIBLE);
                layoutPANNumber.setVisibility(View.VISIBLE);
                txtPANChooseFile.setText("PAN Card scanned copy");
                TextView[] txtViewsForCompulsoryMark = {txtPANChooseFile};
                GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);
            }
        }
        //STEP 7 - Aadhar or Eid radio button
        if (!TextUtils.isEmpty(fapGeneralInfoDto.getIsAadhaarEID())) {
            switch (fapGeneralInfoDto.getIsAadhaarEID()) {
                case "1":
                    radioGroupAadhaarEID.check(R.id.radioButtonAadhaar);
                    break;
                case "0":
                    radioGroupAadhaarEID.check(R.id.radioButtonEID);
                    break;
                default:
                    break;
            }
        }
        //STEP 8 -Aadhaar Number
        editTextAadhaarNumber1.setText(fapGeneralInfoDto.getAadhaarNumber1());
        //STEP 9 - re- Aadhaar Number
        editTextAadhaarNumber2.setText(fapGeneralInfoDto.getAadhaarNumber1());
        //STEP 10 - re- Aadhaar Number
        editTextNameOnAadhaarCard.setText(fapGeneralInfoDto.getNameOnAadhaarCard());
        //STEP 11 - Address proof Number
        editTextAddressProofNumber1.setText(fapGeneralInfoDto.getAddressProofNumber1());
        //STEP 12 - re-enter Address proof Number 2
        editTextAddressProofNumber2.setText(fapGeneralInfoDto.getAddressProofNumber1());

        editTextNameAsPerGSTINCertificate.setText(fapGeneralInfoDto.getgSTINName());
        //STEP 13 - GSTIN Number
        editTextGSTINNumber.setText(fapGeneralInfoDto.getgSTINNumber());

        //GSTIN State
        spinnerGSTINState.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, gSTINStateList));
        int gstInStatePos = fapRepo.getSelectedPos(gSTINStateList, fapGeneralInfoDto.getgSTINState());
        spinnerGSTINState.setSelection(gstInStatePos);
        spinnerGSTINState.setOnItemSelectedListener(this);

        //GSTIN Issuing Date
        if (!TextUtils.isEmpty(fapGeneralInfoDto.getGstinIssueDate())) {
            String formattedDate = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", fapGeneralInfoDto.getGstinIssueDate());
            textViewGSTINIssueDate.setText(formattedDate);
        }

        //STEP 14: PAN Card Image
        boolean IsPanPDF = (fapGeneralInfoDto.getPanFileExt() != null && fapGeneralInfoDto.getPanFileExt().equalsIgnoreCase("pdf")) ? true : false;
        if (IsPanPDF) {
            Glide.with(context)
                    .load(R.drawable.pdf)
                    .apply(new RequestOptions()
                            .error(R.drawable.pdf)
                            .placeholder(R.drawable.pdf)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgPANScanCopy);

        } else {
            if (!TextUtils.isEmpty(fapGeneralInfoDto.getPanFileId())) {
                String panUrl = Constants.DownloadImageUrl + fapGeneralInfoDto.getPanFileId();
                Glide.with(context)
                        .load(panUrl)
                        .apply(new RequestOptions()
                                .error(R.drawable.ic_camera_alt_black_72dp)
                                .placeholder(R.drawable.ic_camera_alt_black_72dp)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))
                        .into(imgPANScanCopy);
            }
        }

        //STEP 15: Aadhaar Card Image
        boolean IsAadhaarPDF = ((fapGeneralInfoDto.getAadhaarFileExt() != null) && fapGeneralInfoDto.getAadhaarFileExt().equalsIgnoreCase("pdf")) ? true : false;
        if (IsAadhaarPDF) {
            Glide.with(context)
                    .load(R.drawable.pdf)
                    .apply(new RequestOptions()
                            .error(R.drawable.pdf)
                            .placeholder(R.drawable.pdf)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgAadhaarScanCopy);
        } else {
            if (!TextUtils.isEmpty(fapGeneralInfoDto.getAadhaarFileId())) {
                String aadhaarUrl = Constants.DownloadImageUrl + fapGeneralInfoDto.getAadhaarFileId();
                Glide.with(context)
                        .load(aadhaarUrl)
                        .apply(new RequestOptions()
                                .error(R.drawable.ic_camera_alt_black_72dp)
                                .placeholder(R.drawable.ic_camera_alt_black_72dp)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))
                        .into(imgAadhaarScanCopy);
            }
        }

        //STEP 16: Address Proof Image
        boolean IsAddressPDF = ((fapGeneralInfoDto.getAddressProofFileExt() != null) && fapGeneralInfoDto.getAddressProofFileExt().equalsIgnoreCase("pdf")) ? true : false;
        if (IsAddressPDF) {
            Glide.with(context)
                    .load(R.drawable.pdf)
                    .apply(new RequestOptions()
                            .error(R.drawable.pdf)
                            .placeholder(R.drawable.pdf)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgAddressProofScanCopy);
        } else {
            if (!TextUtils.isEmpty(fapGeneralInfoDto.getAddressProofFileId())) {
                String addProofUrl = Constants.DownloadImageUrl + fapGeneralInfoDto.getAddressProofFileId();
                Glide.with(context)
                        .load(addProofUrl)
                        .apply(new RequestOptions()
                                .error(R.drawable.ic_camera_alt_black_72dp)
                                .placeholder(R.drawable.ic_camera_alt_black_72dp)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))
                        .into(imgAddressProofScanCopy);
            }
        }

        //STEP 17: GSTIN Upload Image
        boolean IsGSTINPDF = ((fapGeneralInfoDto.getGstinUploadFileExt() != null) && fapGeneralInfoDto.getGstinUploadFileExt().equalsIgnoreCase("pdf")) ? true : false;
        if (IsGSTINPDF) {
            Glide.with(context)
                    .load(R.drawable.pdf)
                    .apply(new RequestOptions()
                            .error(R.drawable.pdf)
                            .placeholder(R.drawable.pdf)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgGSTINUpload);
        } else {
            if (!TextUtils.isEmpty(fapGeneralInfoDto.getGSTINUploadFileId())) {
                String gstUrl = Constants.DownloadImageUrl + fapGeneralInfoDto.getGSTINUploadFileId();
                Glide.with(context)
                        .load(gstUrl)
                        .apply(new RequestOptions()
                                .error(R.drawable.ic_camera_alt_black_72dp)
                                .placeholder(R.drawable.ic_camera_alt_black_72dp)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))
                        .into(imgGSTINUpload);
            }
        }

        //MSME Name
        editTextNameOnMSMECertificate.setText(fapGeneralInfoDto.getNameOnMsme());

        //MSME Certifcate Number
        editTextMSMECertificateNumber.setText(fapGeneralInfoDto.getMsmeNumber());

        //MSME Certificate Issuing Date
        if (!TextUtils.isEmpty(fapGeneralInfoDto.getMsmeIssueDate())) {
            String formattedDate = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", fapGeneralInfoDto.getMsmeIssueDate());
            textViewIssuingDateOfMSME.setText(formattedDate);
        }

        //MSME Scanned Copy
        boolean IsPDF = (fapGeneralInfoDto.getMsmeImageExt() != null && fapGeneralInfoDto.getMsmeImageExt().equalsIgnoreCase("pdf")) ? true : false;
        if (IsPDF) {
            Glide.with(context)
                    .load(R.drawable.pdf)
                    .apply(new RequestOptions()
                            .error(R.drawable.pdf)
                            .placeholder(R.drawable.pdf)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgMSMECerificationImg);

        } else {
            if (!TextUtils.isEmpty(fapGeneralInfoDto.getMsmeImageId())) {
                String gstUrl = Constants.DownloadImageUrl + fapGeneralInfoDto.getMsmeImageId();
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

        //CIBIL Score
        editTextCIBILScore.setText(fapGeneralInfoDto.getCibilScore());

        //CIBIL Report Date
        if (!TextUtils.isEmpty(fapGeneralInfoDto.getCibilReportDate())) {
            String formattedDate = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", fapGeneralInfoDto.getCibilReportDate());
            textViewCIBILReportDate.setText(formattedDate);
        }

        //CIBIL Scanned Copy
        boolean IscibilPDF = (fapGeneralInfoDto.getCibilExt() != null && fapGeneralInfoDto.getCibilExt().equalsIgnoreCase("pdf")) ? true : false;
        if (IscibilPDF) {
            Glide.with(context)
                    .load(R.drawable.pdf)
                    .apply(new RequestOptions()
                            .error(R.drawable.pdf)
                            .placeholder(R.drawable.pdf)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgCIBILProof);

        } else {
            if (!TextUtils.isEmpty(fapGeneralInfoDto.getCibilImageId())) {
                String gstUrl = Constants.DownloadImageUrl + fapGeneralInfoDto.getCibilImageId();
                Glide.with(context)
                        .load(gstUrl)
                        .apply(new RequestOptions()
                                .error(R.drawable.ic_camera_alt_black_72dp)
                                .placeholder(R.drawable.ic_camera_alt_black_72dp)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))
                        .into(imgCIBILProof);
            }
        }

        //Police Verification Report Date
        if (!TextUtils.isEmpty(fapGeneralInfoDto.getBcaPoliceVerificationDate())) {
            String formattedDate = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", fapGeneralInfoDto.getBcaPoliceVerificationDate());
            textViewBCAPoliceVerificationDate.setText(formattedDate);
        }

        //Police Verification Scanned Copy
        boolean IsPolicePDF = (fapGeneralInfoDto.getBcaPoliceVerificationExt() != null && fapGeneralInfoDto.getBcaPoliceVerificationExt().equalsIgnoreCase("pdf")) ? true : false;
        if (IsPolicePDF) {
            Glide.with(context)
                    .load(R.drawable.pdf)
                    .apply(new RequestOptions()
                            .error(R.drawable.pdf)
                            .placeholder(R.drawable.pdf)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgBCAPoliceVerification);

        } else {
            if (!TextUtils.isEmpty(fapGeneralInfoDto.getBcaPoliceVerificationId())) {
                String gstUrl = Constants.DownloadImageUrl + fapGeneralInfoDto.getBcaPoliceVerificationId();
                Glide.with(context)
                        .load(gstUrl)
                        .apply(new RequestOptions()
                                .error(R.drawable.ic_camera_alt_black_72dp)
                                .placeholder(R.drawable.ic_camera_alt_black_72dp)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))
                        .into(imgBCAPoliceVerification);
            }
        }

        //Enable/disable views
        GUIUtils.setViewAndChildrenEnabled(layoutCIBILBCAPolice, ((NextGenFranchiseeApplicationFormFragment) getParentFragment()).isCIBILPoliceEditable);
        GUIUtils.setViewAndChildrenEnabled(layoutGSTINCertificate, ((NextGenFranchiseeApplicationFormFragment) getParentFragment()).isGSTINEditable);
        GUIUtils.setViewAndChildrenEnabled(layoutMSMECertificate, ((NextGenFranchiseeApplicationFormFragment) getParentFragment()).isMSMEEditable);

        //Enable/disable views
        GUIUtils.setViewAndChildrenEnabled(layoutGeneralParent, IsEditable);
    }

    @Override
    public void onPause() {
        super.onPause();
        permissionHandler.dismissSnackbar();
    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();

        if (Id == R.id.imgMSMECerificationImg) {

            FROM = MSME_SCAN_COPY;
            SEL_FILE_TYPE = IMAGES_PDF;
            showAttachmentDialog(view, SEL_FILE_TYPE);

        } else if (Id == R.id.imgPANScanCopy) {
            FROM = PAN_CARD_SCAN_COPY;
            SEL_FILE_TYPE = IMAGES_PDF;
            showAttachmentDialog(view, SEL_FILE_TYPE);

        } else if (Id == R.id.imgAadhaarScanCopy) {
            FROM = AADHAAR_CARD_SCAN_COPY;
            SEL_FILE_TYPE = IMAGES_PDF;
            showAttachmentDialog(view, SEL_FILE_TYPE);

        } else if (Id == R.id.imgAddressProofScanCopy) {
            FROM = ADDRESS_PROOF_SCAN_COPY;
            SEL_FILE_TYPE = IMAGES_PDF;
            showAttachmentDialog(view, SEL_FILE_TYPE);

        } else if (Id == R.id.imgGSTINUpload) {
            FROM = GSTIN_SCAN_COPY;
            SEL_FILE_TYPE = IMAGES_PDF;
            showAttachmentDialog(view, SEL_FILE_TYPE);

        } else if (Id == R.id.textViewGSTINIssueDate) {

            selectedDateTimeId = view.getId();
            showDateTimeDialogPicker();

        } else if (Id == R.id.textViewIssuingDateOfMSME) {

            selectedDateTimeId = view.getId();
            showDateTimeDialogPicker();

        } else if (Id == R.id.textViewCIBILReportDate) {

            selectedDateTimeId = view.getId();
            showDateTimeDialogPicker();

        } else if (Id == R.id.textViewBCAPoliceVerificationDate) {

            selectedDateTimeId = view.getId();
            showDateTimeDialogPicker();

        } else if (Id == R.id.imgCIBILProof) {
            FROM = CIBIL_SCAN_COPY;
            SEL_FILE_TYPE = IMAGES_PDF;
            showAttachmentDialog(view, SEL_FILE_TYPE);

        } else if (Id == R.id.imgBCAPoliceVerification) {
            FROM = POLICE_VERIFICATION_SCAN_COPY;
            SEL_FILE_TYPE = IMAGES_PDF;
            showAttachmentDialog(view, SEL_FILE_TYPE);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getAllGeneralDetailSpinnerData != null && !getAllGeneralDetailSpinnerData.isCancelled()) {
            getAllGeneralDetailSpinnerData.cancel(true);
        }
    }

    public int IsFranchiseeGeneralInfoValidated() {

        if (TextUtils.isEmpty(fapGeneralInfoDto.getIsHavingPANCard()) || fapGeneralInfoDto.getIsHavingPANCard().equalsIgnoreCase("0")) {

            //STEP 1: Name on PAN Card
            fapGeneralInfoDto.setNameOnPANCard(editTextNameAsPerPANCard.getText().toString().trim());
            if (TextUtils.isEmpty(fapGeneralInfoDto.getNameOnPANCard())) {
                Toast.makeText(context, ENTER_PAN_NAME, Toast.LENGTH_LONG).show();
                editTextNameAsPerPANCard.setError(ENTER_PAN_NAME);
                return 1;
            }

            //STEP 2: PAN Number 1
            fapGeneralInfoDto.setPanNumber1(editTextPANNumber1.getText().toString().trim());
            if (TextUtils.isEmpty(fapGeneralInfoDto.getPanNumber1()) && !editTextPANNumber1.getText().toString().trim().matches(CommonUtils.pancardpattern)) {
                Toast.makeText(context, ENTER_PAN_NUMBER, Toast.LENGTH_LONG).show();
                editTextPANNumber1.setError(ENTER_PAN_NUMBER);
                return 1;
            }

            //STEP 3: PAN Number 2
            if (TextUtils.isEmpty(fapGeneralInfoDto.getPanNumber1()) && !editTextPANNumber2.getText().toString().trim().matches(CommonUtils.pancardpattern)) {
                Toast.makeText(context, ENTER_PAN_NUMBER, Toast.LENGTH_LONG).show();
                editTextPANNumber2.setError(ENTER_PAN_NUMBER);
                return 1;
            }
        }

        //STEP 4: PAN Card Copy
        if (TextUtils.isEmpty(fapGeneralInfoDto.getPanFileId()) || fapGeneralInfoDto.getPanFileId().equalsIgnoreCase("0")) {
            if (TextUtils.isEmpty(fapGeneralInfoDto.getPanCardCopyImageBase64())) {
                showMessage("Please add PAN Card Scan copy.");
                return 1;
            }
        }

        if (!IsHidden) {

            //STEP 5: Aadhaar OR EID
            if (TextUtils.isEmpty(fapGeneralInfoDto.getIsAadhaarEID())) {
                showMessage("Please select Aadhaar or EID.");
                return 1;
            }

            //STEP 6: Aadhaar Number 1
            fapGeneralInfoDto.setAadhaarNumber1(editTextAadhaarNumber1.getText().toString().trim());
            if (TextUtils.isEmpty(fapGeneralInfoDto.getAadhaarNumber1()) && !CommonUtils.validAadharNumber(editTextAadhaarNumber1.getText().toString())) {
                Toast.makeText(context, ENTER_AADHAAR_NUMBER, Toast.LENGTH_LONG).show();
                editTextAadhaarNumber1.setError(ENTER_AADHAAR_NUMBER);
                return 1;
            }

            //STEP 7: Aadhaar Number 2
            if (TextUtils.isEmpty(fapGeneralInfoDto.getAadhaarNumber1()) && !CommonUtils.validAadharNumber(editTextAadhaarNumber2.getText().toString())) {
                Toast.makeText(context, ENTER_AADHAAR_NUMBER, Toast.LENGTH_LONG).show();
                editTextAadhaarNumber2.setError(ENTER_AADHAAR_NUMBER);
                return 1;
            }

            //STEP 8: Aadhaar Card Name
            fapGeneralInfoDto.setNameOnAadhaarCard(editTextNameOnAadhaarCard.getText().toString().trim());
            if (TextUtils.isEmpty(fapGeneralInfoDto.getNameOnAadhaarCard())) {
                Toast.makeText(context, "Please enter Name as per Aadhaar Card.", Toast.LENGTH_LONG).show();
                editTextNameOnAadhaarCard.setError("Please enter Name as per Aadhaar Card.");
                return 1;
            }

            //STEP 9: Aadhaar Card Copy
            if (TextUtils.isEmpty(fapGeneralInfoDto.getAadhaarFileId()) || fapGeneralInfoDto.getAadhaarFileId().equalsIgnoreCase("0")) {
                if (TextUtils.isEmpty(fapGeneralInfoDto.getAdhaarCardCopyImageBase64())) {
                    showMessage("Please add Aadhaar Card Scan copy.");
                    return 1;
                }
            }
        }

        //STEP 14: GSTIN number
        fapGeneralInfoDto.setgSTINNumber(editTextGSTINNumber.getText().toString().trim());
        if (!TextUtils.isEmpty(fapGeneralInfoDto.getgSTINNumber())) {
            if (!CommonUtils.GSTINisValid(editTextGSTINNumber.getText().toString().trim())) {
                Toast.makeText(context, "Please enter valid GSTIN Number.", Toast.LENGTH_LONG).show();
                editTextGSTINNumber.setError("Please enter valid GSTIN Number.");
                return 1;
            }
        }

        /*//Name on MSME Certificate
        fapGeneralInfoDto.setNameOnMsme(editTextNameOnMSMECertificate.getText().toString().trim());
        if (TextUtils.isEmpty(fapGeneralInfoDto.getNameOnMsme())) {
            Toast.makeText(context, "Please enter Name as mentioned on MSME Certificate.", Toast.LENGTH_LONG).show();
            editTextNameOnMSMECertificate.setError("Please enter Name as mentioned on MSME Certificate.");
            return 1;
        }

        //MSME Certificate Number
        fapGeneralInfoDto.setMsmeNumber(editTextMSMECertificateNumber.getText().toString().trim());
        if (TextUtils.isEmpty(fapGeneralInfoDto.getMsmeNumber())) {
            Toast.makeText(context, "Please enter MSME Certificate Number.", Toast.LENGTH_LONG).show();
            editTextMSMECertificateNumber.setError("Please enter MSME Certificate Number.");
            return 1;
        }*/

        fapGeneralInfoDto.setCibilScore(editTextCIBILScore.getText().toString().trim());
        if (!TextUtils.isEmpty(fapGeneralInfoDto.getCibilScore())) {
            int status = isCIBILScoreValidated();
            if (status != 0) {
                showMessage(cibilNote);
                editTextCIBILScore.setError(cibilNote);
                return 1;
            }
        }

        return 0;
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
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgPANScanCopy);
                else
                    Glide.with(context).asBitmap().load(bitmap).into(imgPANScanCopy);

                txtPANScanCopyName.setVisibility(View.VISIBLE);
                txtPANScanCopyName.setText(fileName);
                fapGeneralInfoDto.setPanCardCopyImageBase64(base64);
                fapGeneralInfoDto.setPanFileExt(ext);
                fapGeneralInfoDto.setPanCardCopyFileName(fileName);
                break;

            case AADHAAR_CARD_SCAN_COPY:
                if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgAadhaarScanCopy);
                else
                    Glide.with(context).asBitmap().load(bitmap).into(imgAadhaarScanCopy);

                txtAadhaarScanCopyName.setVisibility(View.VISIBLE);
                txtAadhaarScanCopyName.setText(fileName);
                fapGeneralInfoDto.setAdhaarCardCopyImageBase64(base64);
                fapGeneralInfoDto.setAadhaarFileExt(ext);
                fapGeneralInfoDto.setAdhaarCardCopyFileName(fileName);
                break;

            case ADDRESS_PROOF_SCAN_COPY:
                if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgAddressProofScanCopy);
                else
                    Glide.with(context).asBitmap().load(bitmap).into(imgAddressProofScanCopy);

                txtAddressProofScanCopyName.setVisibility(View.VISIBLE);
                txtAddressProofScanCopyName.setText(fileName);
                fapGeneralInfoDto.setAddressProofCopyImageBase64(base64);
                fapGeneralInfoDto.setAddressProofFileExt(ext);
                fapGeneralInfoDto.setAddressProofCopyFileName(fileName);
                break;

            case GSTIN_SCAN_COPY:
                if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgGSTINUpload);
                else
                    Glide.with(context).asBitmap().load(bitmap).into(imgGSTINUpload);

                txtGSTINUploadName.setVisibility(View.VISIBLE);
                txtGSTINUploadName.setText(fileName);
                fapGeneralInfoDto.setGstINUploadBase64(base64);
                fapGeneralInfoDto.setGstinUploadFileExt(ext);
                fapGeneralInfoDto.setGstINUploadFileName(fileName);
                break;

            case MSME_SCAN_COPY:
                if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgMSMECerificationImg);
                else
                    Glide.with(context).asBitmap().load(bitmap).into(imgMSMECerificationImg);

                fapGeneralInfoDto.setMsmeImageBase64(base64);
                fapGeneralInfoDto.setMsmeImageExt(ext);
                break;

            case CIBIL_SCAN_COPY:
                if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgCIBILProof);
                else
                    Glide.with(context).asBitmap().load(bitmap).into(imgCIBILProof);

                fapGeneralInfoDto.setCibilBase64(base64);
                fapGeneralInfoDto.setCibilExt(ext);
                break;

            case POLICE_VERIFICATION_SCAN_COPY:
                if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgBCAPoliceVerification);
                else
                    Glide.with(context).asBitmap().load(bitmap).into(imgBCAPoliceVerification);

                fapGeneralInfoDto.setBcaPoliceVerificationBase64(base64);
                fapGeneralInfoDto.setBcaPoliceVerificationExt(ext);
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        int Id = adapterView.getId();
        if (Id == R.id.spinnerAddressProofType) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerAddressProofType.getItemAtPosition(position);
                fapGeneralInfoDto.setAddressProofType(entityDto.getId());
            }
        } else if (Id == R.id.spinnerGSTINStateCode) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto stateCode = (CustomFranchiseeApplicationSpinnerDto) spinnerGSTINState.getItemAtPosition(position);
                fapGeneralInfoDto.setgSTINState(stateCode.getId());
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //Do Nothing
    }

    class GetAllGeneralDetailSpinnerData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            //STEP 1:Address Proof Type
            AddressProofTypeList = fapRepo.getAddressProofTypeList();

            //STEP 2: GSTIN State
            gSTINStateList = fapRepo.getGSTINStateList();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            BindSpinner();
        }
    }

    public void reloadData(String data, boolean IsEditable) {
        //Reload Data
        if (TextUtils.isEmpty(data))
            fapGeneralInfoDto = new FAPGeneralInfoDto();
        else {
            try {
                Gson gson = new GsonBuilder().create();
                fapGeneralInfoDto = gson.fromJson(data, FAPGeneralInfoDto.class);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.IsEditable = IsEditable;
        getAllGeneralDetailSpinnerData = new GetAllGeneralDetailSpinnerData();
        getAllGeneralDetailSpinnerData.execute("");
    }

    public FAPGeneralInfoDto getFapGeneralInfoDto() {

        fapGeneralInfoDto.setNameOnPANCard(editTextNameAsPerPANCard.getText().toString());
        fapGeneralInfoDto.setPanNumber1(editTextPANNumber1.getText().toString());
        fapGeneralInfoDto.setAadhaarNumber1(editTextAadhaarNumber1.getText().toString());
        fapGeneralInfoDto.setNameOnAadhaarCard(editTextNameOnAadhaarCard.getText().toString());
        fapGeneralInfoDto.setAddressProofNumber1(editTextAddressProofNumber1.getText().toString());
        fapGeneralInfoDto.setgSTINName(editTextNameAsPerGSTINCertificate.getText().toString());
        fapGeneralInfoDto.setgSTINNumber(editTextGSTINNumber.getText().toString());
        fapGeneralInfoDto.setNameOnMsme(editTextNameOnMSMECertificate.getText().toString().trim());
        fapGeneralInfoDto.setMsmeNumber(editTextMSMECertificateNumber.getText().toString().trim());
        fapGeneralInfoDto.setCibilScore(editTextCIBILScore.getText().toString().trim());

        if(editTextCIBILScore.getText().toString().equalsIgnoreCase("NA")){
            fapGeneralInfoDto.setCibilScore("0");
        } else  if(editTextCIBILScore.getText().toString().equalsIgnoreCase("NH")){
            fapGeneralInfoDto.setCibilScore("-1");
        }

        return fapGeneralInfoDto;
    }

    private void spinner_focusablemode(CustomSearchableSpinner stateSpinner) {
        stateSpinner.setFocusable(true);
        stateSpinner.setFocusableInTouchMode(true);
    }

    private void showDateTimeDialogPicker() {

        Date defaultDate = null;
        if (selectedDateTimeId == R.id.textViewGSTINIssueDate) {
            defaultDate = gstIssueDate;
        } else if (selectedDateTimeId == R.id.textViewIssuingDateOfMSME) {
            defaultDate = msmeIssuingDate;

        } else if (selectedDateTimeId == R.id.textViewCIBILReportDate) {
            defaultDate = msmeIssuingDate;

        } else if (selectedDateTimeId == R.id.textViewBCAPoliceVerificationDate) {
            defaultDate = msmeIssuingDate;
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

                        if (selectedDateTimeId == R.id.textViewGSTINIssueDate) {
                            gstIssueDate = datetime;
                            strGstIssueDate = formateYMD;
                            fapGeneralInfoDto.setGstinIssueDate(strGstIssueDate);

                            String issuingDate = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", fapGeneralInfoDto.getGstinIssueDate());
                            textViewGSTINIssueDate.setText(issuingDate);
                            textViewGSTINIssueDate.setError(null);

                        } else if (selectedDateTimeId == R.id.textViewIssuingDateOfMSME) {
                            msmeIssuingDate = datetime;
                            strMSMEIssuingDate = formateYMD;
                            fapGeneralInfoDto.setMsmeIssueDate(strMSMEIssuingDate);

                            String issuingDate = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", fapGeneralInfoDto.getMsmeIssueDate());
                            textViewIssuingDateOfMSME.setText(issuingDate);
                            textViewIssuingDateOfMSME.setError(null);

                        } else if (selectedDateTimeId == R.id.textViewCIBILReportDate) {
                            cibilReportDate = datetime;
                            strCibilReportDate = formateYMD;
                            fapGeneralInfoDto.setCibilReportDate(strCibilReportDate);

                            String cibilReportDate = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", fapGeneralInfoDto.getCibilReportDate());
                            textViewCIBILReportDate.setText(cibilReportDate);
                            textViewCIBILReportDate.setError(null);

                        } else if (selectedDateTimeId == R.id.textViewBCAPoliceVerificationDate) {
                            policeVerificationDate = datetime;
                            strPoliceVerificationDate = formateYMD;
                            fapGeneralInfoDto.setBcaPoliceVerificationDate(strPoliceVerificationDate);

                            String policeReportDate = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", fapGeneralInfoDto.getBcaPoliceVerificationDate());
                            textViewBCAPoliceVerificationDate.setText(policeReportDate);
                            textViewBCAPoliceVerificationDate.setError(null);

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

        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.DATE, -50);
        long end1 = calendar1.getTimeInMillis();//Set Min and Max Date

        Calendar last3Cal = Calendar.getInstance();
        int day3 = last3Cal.get(Calendar.DATE);
        int month3 = last3Cal.get(Calendar.MONTH) - 3;
        int year3 = last3Cal.get(Calendar.YEAR);
        last3Cal.set(year3, month3, day3);

        //Today's Date
        long now = new Date().getTime() - 1000;
        if (selectedDateTimeId == R.id.textViewGSTINIssueDate) {

            Calendar c = Calendar.getInstance();
            c.set(1920, 0, 1);//Year,Month -1,Day
            dateTimePickerDialog.setMinDate(c.getTimeInMillis());
            dateTimePickerDialog.setMaxDate(now);

        } else if (selectedDateTimeId == R.id.textViewIssuingDateOfMSME) {

            dateTimePickerDialog.setMinDate(end1);
            dateTimePickerDialog.setMaxDate(now);

        } else if (selectedDateTimeId == R.id.textViewCIBILReportDate) {

            if (TextUtils.isEmpty(fapGeneralInfoDto.getCibilReportMinDate())) {
                dateTimePickerDialog.setMinDate(last3Cal.getTimeInMillis());
            } else {
                Date minCibilDate = CommonUtils.parseStringToDate(fapGeneralInfoDto.getCibilReportMinDate());
                dateTimePickerDialog.setMinDate(minCibilDate.getTime());
            }
            dateTimePickerDialog.setMaxDate(now);

        } else if (selectedDateTimeId == R.id.textViewBCAPoliceVerificationDate) {

            if (TextUtils.isEmpty(fapGeneralInfoDto.getBcaPoliceVerificationMinDate())) {
                dateTimePickerDialog.setMinDate(last3Cal.getTimeInMillis());
            } else {
                Date minPoliceDate = CommonUtils.parseStringToDate(fapGeneralInfoDto.getBcaPoliceVerificationMinDate());
                dateTimePickerDialog.setMinDate(minPoliceDate.getTime());
            }
            dateTimePickerDialog.setMaxDate(now);
        }

        dateTimePickerDialog.setCancelable(false);
        dateTimePickerDialog.setActionButtonName("Save");
        dateTimePickerDialog.show();

    }

    public int isCIBILScoreValidated() {

        fapGeneralInfoDto.setCibilScore(editTextCIBILScore.getText().toString().trim());
        if (TextUtils.isEmpty(fapGeneralInfoDto.getCibilScore()))
            return 0;

        if (fapGeneralInfoDto.getCibilScore().equalsIgnoreCase("NA") || fapGeneralInfoDto.getCibilScore().equalsIgnoreCase("NH")) {
            return 0;
        }

        try {
            int cibilScore = Integer.parseInt(fapGeneralInfoDto.getCibilScore());

            if ((cibilScore >= -1 && cibilScore <= 5) || (cibilScore >= 300 && cibilScore <= 900)) {
                return 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

        return -1;
    }
}
