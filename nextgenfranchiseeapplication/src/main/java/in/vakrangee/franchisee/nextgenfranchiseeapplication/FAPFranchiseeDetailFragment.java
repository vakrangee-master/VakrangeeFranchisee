package in.vakrangee.franchisee.nextgenfranchiseeapplication;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nononsenseapps.filepicker.Utils;

import org.json.JSONObject;

import java.io.File;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.core.content.FileProvider;
import butterknife.ButterKnife;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.commongui.DateTimePickerDialog;
import in.vakrangee.supercore.franchisee.commongui.FileAttachmentDialog;
import in.vakrangee.supercore.franchisee.commongui.FileUtils;
import in.vakrangee.supercore.franchisee.commongui.FilteredFilePickerActivity;
import in.vakrangee.supercore.franchisee.commongui.MonthYearPickerDialog;
import in.vakrangee.supercore.franchisee.commongui.animation.AnimationHanndler;
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

public class FAPFranchiseeDetailFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Context context;
    private DeprecateHandler deprecateHandler;
    private PermissionHandler permissionHandler;
    private FileAttachmentDialog fileAttachementDialog;
    private CustomRequestCallBackDialog requestCallBackDialog;
    private static final int CAMERA_PIC_REQUEST = 100;
    private static final int BROWSE_FOLDER_REQUEST = 101;
    private static final int PERMISSIONS_REQUEST_PHONE_CALL = 1;
    private Uri picUri;                 //Picture URI

    private String SEL_FILE_TYPE;
    public FAPFranchiseeDetailsDto franchiseeDetailsDto;
    private DateTimePickerDialog dateTimePickerDialog;
    private static final String DD_MM_YYYY_CONST = "dd-MM-yyyy";
    private static final String YYYY_MM_DD_CONST = "yyyy-MM-dd";
    private DateFormat dateFormatter2 = new SimpleDateFormat(DD_MM_YYYY_CONST, Locale.US);
    private DateFormat dateFormatterYMD = new SimpleDateFormat(YYYY_MM_DD_CONST, Locale.US);
    private Date startDate;
    private String strStartDate;
    private Date incorporationDate;
    private String strIncorporationDate;
    private Date yearOfPassing;
    private String strYearOfPassing;
    private int selectedDateTimeId = 0;
    private View view;
    private FranchiseeApplicationRepository fapRepo;
    private List<CustomFranchiseeApplicationSpinnerDto> entityTypeList;
    private List<CustomFranchiseeApplicationSpinnerDto> entityProofTypeList;
    private List<CustomFranchiseeApplicationSpinnerDto> referredByList;
    private List<CustomFranchiseeApplicationSpinnerDto> salutationList;
    private List<CustomFranchiseeApplicationSpinnerDto> highestQualilficationList;
    private List<CustomFranchiseeApplicationSpinnerDto> priorExpList;
    private List<CustomFranchiseeApplicationSpinnerDto> nationalityList;
    private List<CustomFranchiseeApplicationSpinnerDto> curOccupationList;
    private GetAllFranchiseeDetailSpinnerData getAllFranchiseeDetailSpinnerData = null;
    private GetIOCLRoNameORReferralName getIOCLRoNameORReferralName = null;
    private CheckIOCOdeAlreadyExist checkIOCOdeAlreadyExist = null;
    private CheckNayaraROCOdeAlreadyExist checkNayaraROCOdeAlreadyExist = null;
    private GetEntityProofTypeData getEntityProofTypeData = null;
    private static final int TYPE_IOCL_RO_CODE = 1;
    private static final int TYPE_EMPVKID = 2;
    private static final int TYPE_NAYARA_RO_CODE = 3;
    private static final String notes = "<b>a.</b> Please contact Toll Free Number (1800-2744-427) or request for a call back. <br/> <b>b.</b> Police verification is mandatory and should be done by you before signing the agreement.";
    private boolean IsEditable = false;

    private Typeface font;

    private int FROM = -1;

    private static final int APPLICANT_PIC_COPY = 1;
    private static final int HQUALI_COPY = 2;
    private static final int ENTITY_PROOF_COPY = 3;


    private List<CustomFranchiseeApplicationSpinnerDto> bcaBankNameList;

    //region Labels
    private TextView txtVakrangeeKendraTypeLbl;
    private TextView txtIOCLRoCodeLbl;
    private TextView txtIOCLRoNameLbl;
    private TextView txtNayaraRoCodeLbl;
    private TextView txtNayaraRoNameLbl;
    private TextView txtEntityTypeLbl;
    private TextView txtReferredTypeLbl;
    private TextView txtChoosePhoto;
    private TextView txtApplicantNameLbl;
    private TextView txtFatherNameLbl;
    private TextView txtSpouseNameLbl;
    private TextView txtGenderLbl;
    private TextView txtYearOfPassingLbl;
    private TextView txtMaritalStatusLbl;
    private TextView txtDateOfBirthLbl;
    private TextView txtNationalityLbl;
    private TextView txtHighestQualificationLbl;
    private TextView txtHighestQualificationUploadLbl;
    private TextView txtPriorExperienceLbl;
    private TextView txtCurrentOccupationLbl;
    //endregion
    private RadioGroup radioGroupVakrangeeKendraType;
    private RadioButton radioButtonIOCL;
    private RadioButton radioButtonNonIOCL;
    private RadioButton radioButtonNayara;
    private LinearLayout layoutIOCLROCode;
    private EditText editTextIOCLROCode;
    private LinearLayout layoutIOCLROName;
    private EditText editTextIOCLROName;

    private LinearLayout layoutNayaraROCode;
    private EditText editTextNayaraROCode;
    private LinearLayout layoutNayaraROName;
    private EditText editTextNayaraROName;

    private Spinner spinnerEntityType;
    private LinearLayout layoutCoperativeSocietyName;
    private TextView txtCoperativeSocietyNameLbl;
    private EditText editTextCoperativeSocietyName;
    private LinearLayout layoutDateOfIncorporation;
    private TextView txtDateOfIncorporationLbl;
    private TextView textViewDateOfIncorporation;
    private Spinner spinnerReferredBy;
    private ImageView imgChoosePhoto;
    private TextView txtChoosePhotoName;
    private LinearLayout layoutApplicantName;
    private Spinner spinnerApplicantSalutation;
    public EditText editTextApplicantFirstName;
    public EditText editTextApplicantMiddleName;
    public EditText editTextApplicantLastName;

    private LinearLayout layoutFatherName;
    private Spinner spinnerFatherSalutation;
    private EditText editTextFatherFirstName;
    private EditText editTextFatherMiddleName;
    private EditText editTextFatherLastName;
    private LinearLayout layoutSpouseName;
    private Spinner spinnerSpouseSalutation;
    private EditText editTextSpouseFirstName;
    private EditText editTextSpouseMiddleName;
    private EditText editTextSpouseLastName;
    private RadioGroup radioGroupMaritalStatus;
    private RadioButton radioButtonMarried;
    private RadioButton radioButtonSingle;
    private RadioGroup radioGroupGender;
    private RadioButton radioButtonMale;
    private RadioButton radioButtonFemale;
    private RadioButton radioButtonOthers;
    private TextView textViewDateOfBirth;
    private CustomSearchableSpinner spinnerNationality;
    private Spinner spinnerHighestQualification;
    private TextView txtChooseHQualiUploadPhotoName;
    private ImageView imgHQualiUploadPhoto;

    private Spinner spinnerPriorExperience;
    private Spinner spinnerCurrentOccupation;
    private CheckBox checkboxSpeciallyAbled;
    private CheckBox checkboxPriorMiliExperience;
    private LinearLayout layoutEmployeeVKID;
    private TextView txtEmployeeVKIDLbl;
    private EditText editTextEmployeeVKID;
    private LinearLayout layoutNameOfReferral;
    private TextView txtNameOfReferralLbl;
    private EditText editTextNameOfReferral;
    private LinearLayout layoutGender;
    private LinearLayout layoutMaritalStatus;
    private TextView txtNotes1;
    private TextView txtNotes2;
    private EditText editTextApplicationNo;
    private LinearLayout layoutFranchiseeParent;
    private TextView txtTollFreeNumber;
    private TextView txtRequestCallBack;
    private TextView txtPhoneIcon;
    private TextView txtCallNow;
    private TextView txtRqtPhoneIcon;
    private TextView txtRqtCallBackButton;
    private TextView textViewYearOfPassing;

    private RelativeLayout layoutRequestCallBack;
    private RelativeLayout layoutCallNow;
    private LinearLayout layoutEntityProofSection;
    private Spinner spinnerEntityProofType;
    private TextView txtChooseEntityProofUploadPhotoName;
    private ImageView imgEntityProofUploadPhoto;
    private TextView txtEntityProofTypeLbl;
    private TextView txtEntityProofUploadLbl;
    private LinearLayout layoutDateOfBirth;
    private static final String INDIVIDUAL = "Individual";
    //region BCA Code Details
    private LinearLayout layoutBCACodeDetails;
    private CheckBox checkboxHaveBCACode;
    private LinearLayout layoutBCADetails;
    private LinearLayout layoutBCABankName;
    private TextView txtBCABankNameLbl;
    private Spinner spinnerBCABankName;
    private LinearLayout layoutBCACode;
    private TextView txtBCACodeLbl;
    private EditText editTextBCACode;
    private static final String SPECIAL_CHARS = "~#^|$%&*!'";
    //endregion
    private static final String ENTER_IOCL_NAME = "Please enter proper IOCL Code to get IOCL Name.";
    private static final String ENTER_NAYARA_NAME = "Please enter proper Nayara CMS Code to get Nayara Name.";
    private static final String ORGANIZATION_NAME = "Organisation Name";
    private static final String FIRMS_NAME = "Firm's Name";
    private MonthYearPickerDialog monthYearPickerDialog = null;
    private boolean IsIndividualSelected = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frag_fap_franchisee_detail, container, false);

        bindViewID(view);
        //Data
        this.context = getContext();
        deprecateHandler = new DeprecateHandler(context);
        permissionHandler = new PermissionHandler(getActivity());
        fapRepo = new FranchiseeApplicationRepository(context);
        ButterKnife.bind(this, view);
        font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");

        //Input filter
        CommonUtils.InputFiletrWithMaxLength(editTextApplicantFirstName, SPECIAL_CHARS, 50);
        CommonUtils.InputFiletrWithMaxLength(editTextApplicantMiddleName, SPECIAL_CHARS, 50);
        CommonUtils.InputFiletrWithMaxLength(editTextApplicantLastName, SPECIAL_CHARS, 50);
        CommonUtils.InputFiletrWithMaxLength(editTextFatherFirstName, SPECIAL_CHARS, 50);
        CommonUtils.InputFiletrWithMaxLength(editTextFatherMiddleName, SPECIAL_CHARS, 50);
        CommonUtils.InputFiletrWithMaxLength(editTextFatherLastName, SPECIAL_CHARS, 50);

        CommonUtils.InputFiletrWithMaxLength(editTextSpouseFirstName, SPECIAL_CHARS, 50);
        CommonUtils.InputFiletrWithMaxLength(editTextSpouseMiddleName, SPECIAL_CHARS, 50);
        CommonUtils.InputFiletrWithMaxLength(editTextSpouseLastName, SPECIAL_CHARS, 50);
        CommonUtils.InputFiletrWithMaxLength(editTextEmployeeVKID, SPECIAL_CHARS, 9);
        CommonUtils.InputFiletrWithMaxLength(editTextNameOfReferral, SPECIAL_CHARS, 50);
        CommonUtils.InputFiletrWithMaxLength(editTextCoperativeSocietyName, SPECIAL_CHARS, 50);

        //Set Compulsory mark
        TextView[] txtViewsForCompulsoryMark = {txtVakrangeeKendraTypeLbl, txtDateOfIncorporationLbl, txtCoperativeSocietyNameLbl, txtEntityProofUploadLbl, txtEntityProofTypeLbl, txtIOCLRoCodeLbl, txtIOCLRoNameLbl, txtNayaraRoCodeLbl, txtNayaraRoNameLbl, txtEmployeeVKIDLbl, txtNameOfReferralLbl, txtEntityTypeLbl, txtReferredTypeLbl, txtChoosePhoto, txtApplicantNameLbl,
                txtFatherNameLbl, txtSpouseNameLbl, txtGenderLbl, txtMaritalStatusLbl, txtDateOfBirthLbl, txtYearOfPassingLbl, txtNationalityLbl, txtHighestQualificationLbl, txtHighestQualificationUploadLbl,
                txtPriorExperienceLbl, txtCurrentOccupationLbl, txtBCABankNameLbl, txtBCACodeLbl};
        GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);

        txtPhoneIcon.setTypeface(font);
        txtPhoneIcon.setText(new SpannableStringBuilder(new String(new char[]{0xf095})));
        LayerDrawable layerPhoneDrawable = (LayerDrawable) txtPhoneIcon.getBackground();
        GradientDrawable RqtPhoneIconBgShape = (GradientDrawable) layerPhoneDrawable.findDrawableByLayerId(R.id.gradientDrawable);
        RqtPhoneIconBgShape.setColor(deprecateHandler.getColor(R.color.green));
        GradientDrawable RqtPhoneIconBgGShape = (GradientDrawable) txtCallNow.getBackground();
        RqtPhoneIconBgGShape.setColor(deprecateHandler.getColor(R.color.green));

        txtRqtPhoneIcon.setTypeface(font);
        txtRqtPhoneIcon.setText(new SpannableStringBuilder(new String(new char[]{0xf025})));

        LayerDrawable layerDrawable = (LayerDrawable) txtRqtPhoneIcon.getBackground();
        GradientDrawable RqtPhoneBgShape = (GradientDrawable) layerDrawable.findDrawableByLayerId(R.id.gradientDrawable);
        RqtPhoneBgShape.setColor(deprecateHandler.getColor(R.color.ALO));
        GradientDrawable RqtPhoneTxtBgShape = (GradientDrawable) txtRqtCallBackButton.getBackground();
        RqtPhoneTxtBgShape.setColor(deprecateHandler.getColor(R.color.ALO));

        //Notes
        String note1 = ("\u2022 \t" + getContext().getResources().getString(R.string.franchisee_note1));
        txtNotes1.setText(note1);
        String note2 = ("\u2022 \t" + getContext().getResources().getString(R.string.franchisee_note2));
        txtNotes2.setText(note2);

        txtTollFreeNumber.setText(Html.fromHtml("<u><font color=\"#33A1DE\">1800-2744-427</font></u>"));
        txtRequestCallBack.setText(Html.fromHtml("<u><font color=\"#33A1DE\">Request Call Back</font></u>"));

        return view;
    }

    private void bindViewID(View view) {
        //region Labels
        txtYearOfPassingLbl = view.findViewById(R.id.txtYearOfPassingLbl);
        txtVakrangeeKendraTypeLbl = view.findViewById(R.id.txtVakrangeeKendraTypeLbl);
        txtIOCLRoCodeLbl = view.findViewById(R.id.txtIOCLRoCodeLbl);
        txtIOCLRoNameLbl = view.findViewById(R.id.txtIOCLRoNameLbl);
        txtEntityTypeLbl = view.findViewById(R.id.txtEntityTypeLbl);
        txtReferredTypeLbl = view.findViewById(R.id.txtReferredTypeLbl);
        txtChoosePhoto = view.findViewById(R.id.txtChoosePhoto);
        txtApplicantNameLbl = view.findViewById(R.id.txtApplicantNameLbl);
        txtFatherNameLbl = view.findViewById(R.id.txtFatherNameLbl);
        txtSpouseNameLbl = view.findViewById(R.id.txtSpouseNameLbl);
        txtGenderLbl = view.findViewById(R.id.txtGenderLbl);
        txtMaritalStatusLbl = view.findViewById(R.id.txtMaritalStatusLbl);
        txtDateOfBirthLbl = view.findViewById(R.id.txtDateOfBirthLbl);
        txtNationalityLbl = view.findViewById(R.id.txtNationalityLbl);
        txtHighestQualificationLbl = view.findViewById(R.id.txtHighestQualificationLbl);
        txtHighestQualificationUploadLbl = view.findViewById(R.id.txtHighestQualificationUploadLbl);
        txtPriorExperienceLbl = view.findViewById(R.id.txtPriorExperienceLbl);
        txtCurrentOccupationLbl = view.findViewById(R.id.txtCurrentOccupationLbl);
        //endregion
        textViewYearOfPassing = view.findViewById(R.id.textViewYearOfPassing);
        radioGroupVakrangeeKendraType = view.findViewById(R.id.radioGroupVakrangeeKendraType);
        radioButtonIOCL = view.findViewById(R.id.radioButtonIOCL);
        radioButtonNayara = view.findViewById(R.id.radioButtonNayara);
        radioButtonNonIOCL = view.findViewById(R.id.radioButtonNonIOCL);
        layoutIOCLROCode = view.findViewById(R.id.layoutIOCLROCode);
        editTextIOCLROCode = view.findViewById(R.id.editTextIOCLROCode);
        layoutIOCLROName = view.findViewById(R.id.layoutIOCLROName);
        editTextIOCLROName = view.findViewById(R.id.editTextIOCLROName);
        spinnerEntityType = view.findViewById(R.id.spinnerEntityType);
        layoutCoperativeSocietyName = view.findViewById(R.id.layoutCoperativeSocietyName);
        txtCoperativeSocietyNameLbl = view.findViewById(R.id.txtCoperativeSocietyNameLbl);
        editTextCoperativeSocietyName = view.findViewById(R.id.editTextCoperativeSocietyName);
        layoutDateOfIncorporation = view.findViewById(R.id.layoutDateOfIncorporation);
        txtDateOfIncorporationLbl = view.findViewById(R.id.txtDateOfIncorporationLbl);
        textViewDateOfIncorporation = view.findViewById(R.id.textViewDateOfIncorporation);
        spinnerReferredBy = view.findViewById(R.id.spinnerReferredBy);
        imgChoosePhoto = view.findViewById(R.id.imgChoosePhoto);
        txtChoosePhotoName = view.findViewById(R.id.txtChoosePhotoName);
        layoutApplicantName = view.findViewById(R.id.layoutApplicantName);
        spinnerApplicantSalutation = view.findViewById(R.id.spinnerApplicantSalutation);
        editTextApplicantFirstName = view.findViewById(R.id.editTextApplicantFirstName);
        editTextApplicantMiddleName = view.findViewById(R.id.editTextApplicantMiddleName);
        editTextApplicantLastName = view.findViewById(R.id.editTextApplicantLastName);
        layoutFatherName = view.findViewById(R.id.layoutFatherName);
        spinnerFatherSalutation = view.findViewById(R.id.spinnerFatherSalutation);
        editTextFatherFirstName = view.findViewById(R.id.editTextFatherFirstName);
        editTextFatherMiddleName = view.findViewById(R.id.editTextFatherMiddleName);
        editTextFatherLastName = view.findViewById(R.id.editTextFatherLastName);
        layoutSpouseName = view.findViewById(R.id.layoutSpouseName);
        spinnerSpouseSalutation = view.findViewById(R.id.spinnerSpouseSalutation);
        editTextSpouseFirstName = view.findViewById(R.id.editTextSpouseFirstName);
        editTextSpouseMiddleName = view.findViewById(R.id.editTextSpouseMiddleName);
        editTextSpouseLastName = view.findViewById(R.id.editTextSpouseLastName);
        radioGroupMaritalStatus = view.findViewById(R.id.radioGroupMaritalStatus);
        radioButtonMarried = view.findViewById(R.id.radioButtonMarried);
        radioButtonSingle = view.findViewById(R.id.radioButtonSingle);
        radioGroupGender = view.findViewById(R.id.radioGroupGender);
        radioButtonMale = view.findViewById(R.id.radioButtonMale);
        radioButtonFemale = view.findViewById(R.id.radioButtonFemale);
        radioButtonOthers = view.findViewById(R.id.radioButtonOthers);
        textViewDateOfBirth = view.findViewById(R.id.textViewDateOfBirth);
        spinnerNationality = view.findViewById(R.id.spinnerNationality);
        spinnerHighestQualification = view.findViewById(R.id.spinnerHighestQualification);
        txtChooseHQualiUploadPhotoName = view.findViewById(R.id.txtChooseHQualiUploadPhotoName);
        imgHQualiUploadPhoto = view.findViewById(R.id.imgHQualiUploadPhoto);
        spinnerPriorExperience = view.findViewById(R.id.spinnerPriorExperience);
        spinnerCurrentOccupation = view.findViewById(R.id.spinnerCurrentOccupation);
        checkboxSpeciallyAbled = view.findViewById(R.id.checkboxSpeciallyAbled);
        checkboxPriorMiliExperience = view.findViewById(R.id.checkboxPriorMiliExperience);
        layoutEmployeeVKID = view.findViewById(R.id.layoutEmployeeVKID);
        txtEmployeeVKIDLbl = view.findViewById(R.id.txtEmployeeVKIDLbl);
        editTextEmployeeVKID = view.findViewById(R.id.editTextEmployeeVKID);
        layoutNameOfReferral = view.findViewById(R.id.layoutNameOfReferral);
        txtNameOfReferralLbl = view.findViewById(R.id.txtNameOfReferralLbl);
        editTextNameOfReferral = view.findViewById(R.id.editTextNameOfReferral);
        layoutGender = view.findViewById(R.id.layoutGender);
        layoutMaritalStatus = view.findViewById(R.id.layoutMaritalStatus);
        txtNotes1 = view.findViewById(R.id.txtNotes1);
        txtNotes2 = view.findViewById(R.id.txtNotes2);
        editTextApplicationNo = view.findViewById(R.id.editTextApplicationNo);
        layoutFranchiseeParent = view.findViewById(R.id.layoutFranchiseeParent);
        txtTollFreeNumber = view.findViewById(R.id.txtTollFreeNumber);
        txtRequestCallBack = view.findViewById(R.id.txtRequestCallBack);
        txtPhoneIcon = view.findViewById(R.id.txtPhoneIcon);
        txtCallNow = view.findViewById(R.id.txtCallNow);
        txtRqtPhoneIcon = view.findViewById(R.id.txtRqtPhoneIcon);
        txtRqtCallBackButton = view.findViewById(R.id.txtRqtCallBackButton);
        layoutRequestCallBack = view.findViewById(R.id.layoutRequestCallBack);
        layoutCallNow = view.findViewById(R.id.layoutCallNow);
        layoutEntityProofSection = view.findViewById(R.id.layoutEntityProofSection);
        spinnerEntityProofType = view.findViewById(R.id.spinnerEntityProofType);
        txtChooseEntityProofUploadPhotoName = view.findViewById(R.id.txtChooseEntityProofUploadPhotoName);
        imgEntityProofUploadPhoto = view.findViewById(R.id.imgEntityProofUploadPhoto);
        txtEntityProofTypeLbl = view.findViewById(R.id.txtEntityProofTypeLbl);
        txtEntityProofUploadLbl = view.findViewById(R.id.txtEntityProofUploadLbl);
        layoutDateOfBirth = view.findViewById(R.id.layoutDateOfBirth);
        //region BCA Code Details
        layoutBCACodeDetails = view.findViewById(R.id.layoutBCACodeDetails);
        checkboxHaveBCACode = view.findViewById(R.id.checkboxHaveBCACode);
        layoutBCADetails = view.findViewById(R.id.layoutBCADetails);
        layoutBCABankName = view.findViewById(R.id.layoutBCABankName);
        txtBCABankNameLbl = view.findViewById(R.id.txtBCABankNameLbl);
        spinnerBCABankName = view.findViewById(R.id.spinnerBCABankName);
        layoutBCACode = view.findViewById(R.id.layoutBCACode);
        txtBCACodeLbl = view.findViewById(R.id.txtBCACodeLbl);
        editTextBCACode = view.findViewById(R.id.editTextBCACode);

        layoutNayaraROCode = view.findViewById(R.id.layoutNayaraROCode);
        txtNayaraRoCodeLbl = view.findViewById(R.id.txtNayaraRoCodeLbl);
        editTextNayaraROCode = view.findViewById(R.id.editTextNayaraROCode);
        layoutNayaraROName = view.findViewById(R.id.layoutNayaraROName);
        txtNayaraRoNameLbl = view.findViewById(R.id.txtNayaraRoNameLbl);
        editTextNayaraROName = view.findViewById(R.id.editTextNayaraROName);

        imgChoosePhoto.setOnClickListener(this);
        imgHQualiUploadPhoto.setOnClickListener(this);
        textViewDateOfBirth.setOnClickListener(this);
        textViewDateOfIncorporation.setOnClickListener(this);
        txtTollFreeNumber.setOnClickListener(this);
        layoutRequestCallBack.setOnClickListener(this);
        layoutCallNow.setOnClickListener(this);
        imgEntityProofUploadPhoto.setOnClickListener(this);
        textViewYearOfPassing.setOnClickListener(this);


        spinnerEntityType.setOnItemSelectedListener(this);
        spinnerReferredBy.setOnItemSelectedListener(this);
        spinnerApplicantSalutation.setOnItemSelectedListener(this);
        spinnerFatherSalutation.setOnItemSelectedListener(this);

        spinnerSpouseSalutation.setOnItemSelectedListener(this);
        spinnerNationality.setOnItemSelectedListener(this);
        spinnerHighestQualification.setOnItemSelectedListener(this);
        spinnerPriorExperience.setOnItemSelectedListener(this);

        spinnerCurrentOccupation.setOnItemSelectedListener(this);
        spinnerEntityProofType.setOnItemSelectedListener(this);
    }

    public void setCompulsoryMarkToFatherAndApplicantLabel() {
        TextView[] txtViewsForCompulsoryMark = {txtCoperativeSocietyNameLbl, txtApplicantNameLbl, txtFatherNameLbl};
        GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);
    }

    @Override
    public void onPause() {
        super.onPause();
        permissionHandler.dismissSnackbar();
    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();

        if (Id == R.id.imgChoosePhoto) {
            FROM = APPLICANT_PIC_COPY;
            SEL_FILE_TYPE = "images";
            showAttachmentDialog(view, SEL_FILE_TYPE);

        } else if (Id == R.id.imgHQualiUploadPhoto) {
            FROM = HQUALI_COPY;
            SEL_FILE_TYPE = "images/pdf";
            showAttachmentDialog(view, SEL_FILE_TYPE);

        } else if (Id == R.id.imgEntityProofUploadPhoto) {
            FROM = ENTITY_PROOF_COPY;
            SEL_FILE_TYPE = "images/pdf";
            showAttachmentDialog(view, SEL_FILE_TYPE);

        } else if (Id == R.id.textViewDateOfBirth) {
            selectedDateTimeId = Id;
            showDateTimeDialogPicker();

        } else if (Id == R.id.textViewYearOfPassing) {
            dateOfPassingChecks();

        } else if (Id == R.id.textViewDateOfIncorporation) {

            selectedDateTimeId = Id;
            showDateTimeDialogPicker();

        } else if (Id == R.id.txtTollFreeNumber) {
            //Open call function
            directCall("18002744427");

        } else if (Id == R.id.layoutCallNow) {
            AnimationHanndler.bubbleAnimation(context, view);
            //Open call function
            directCall("18002744427");

        } else if (Id == R.id.layoutRequestCallBack) {
            AnimationHanndler.bubbleAnimation(context, view);

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

            } else if (requestCode == BROWSE_FOLDER_REQUEST && resultCode == Activity.RESULT_OK) {
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

    private void setScanCopyData(boolean IsDrawable, Bitmap bitmap, String fileName, String base64, String ext) {

        switch (FROM) {

            case APPLICANT_PIC_COPY:
                if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgChoosePhoto);
                else {
                    try {
                        bitmap = ImageUtils.rotateImageIfRequired(getActivity().getContentResolver(), bitmap, picUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Glide.with(context).asBitmap().load(bitmap).into(imgChoosePhoto);
                }
                franchiseeDetailsDto.setProfilePicBase64(base64);
                franchiseeDetailsDto.setProfilePicExt(ext);
                franchiseeDetailsDto.setProfilePicName(fileName);

                break;

            case HQUALI_COPY:
                if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgHQualiUploadPhoto);
                else
                    Glide.with(context).asBitmap().load(bitmap).into(imgHQualiUploadPhoto);

                franchiseeDetailsDto.setHighestQualiUploadBase64(base64);
                franchiseeDetailsDto.setHighestQualiFileExt(ext);
                franchiseeDetailsDto.setHighestQualificationName(fileName);
                break;

            case ENTITY_PROOF_COPY:
                if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgEntityProofUploadPhoto);
                else
                    Glide.with(context).asBitmap().load(bitmap).into(imgEntityProofUploadPhoto);

                franchiseeDetailsDto.setEntityProofFileBase64(base64);
                franchiseeDetailsDto.setEntityProofFileExt(ext);
                franchiseeDetailsDto.setEntityProofName(fileName);
                break;

            default:
                break;

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getAllFranchiseeDetailSpinnerData != null && !getAllFranchiseeDetailSpinnerData.isCancelled()) {
            getAllFranchiseeDetailSpinnerData.cancel(true);
        }

        if (getIOCLRoNameORReferralName != null && !getIOCLRoNameORReferralName.isCancelled()) {
            getIOCLRoNameORReferralName.cancel(true);
        }

        if (checkIOCOdeAlreadyExist != null && !checkIOCOdeAlreadyExist.isCancelled()) {
            checkIOCOdeAlreadyExist.cancel(true);
        }

        if (checkNayaraROCOdeAlreadyExist != null && !checkNayaraROCOdeAlreadyExist.isCancelled()) {
            checkNayaraROCOdeAlreadyExist.cancel(true);
        }

        //Date Time DIalog
        if (dateTimePickerDialog != null && dateTimePickerDialog.isShowing()) {
            dateTimePickerDialog.dismiss();
            dateTimePickerDialog = null;
        }

        //FileAttachement Dialog
        if (fileAttachementDialog != null && fileAttachementDialog.isShowing()) {
            fileAttachementDialog.dismiss();
            fileAttachementDialog = null;
        }

        //Request Call Back DIalog
        if (requestCallBackDialog != null && requestCallBackDialog.isShowing()) {
            requestCallBackDialog.dismiss();
            requestCallBackDialog = null;

        }

        //Entity Proof Type
        if (getEntityProofTypeData != null && !getEntityProofTypeData.isCancelled()) {
            getEntityProofTypeData.cancel(true);
        }
    }

    public int IsFranchiseeNameValidated() {

        //Entity Type
        if (TextUtils.isEmpty(franchiseeDetailsDto.getEntityType()) || franchiseeDetailsDto.getEntityType().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Entity Type.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerEntityType, "Please select Entity Type.", context);
            return 2;
        }

        String entityName = txtApplicantNameLbl.getText().toString().trim();
        entityName = entityName.replaceAll("Name", "");
        entityName = entityName.replaceAll("\\*", "");

        //Applicant Salutation
        if (TextUtils.isEmpty(franchiseeDetailsDto.getApplicantSalutation())) {
            Toast.makeText(context, "Please select " + entityName + " Salutation.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerApplicantSalutation, "Please select " + entityName + " Salutation.", context);
            return 5;
        }

        //Applicant First Name
        franchiseeDetailsDto.setApplicantFirstName(editTextApplicantFirstName.getText().toString().trim());
        if (TextUtils.isEmpty(franchiseeDetailsDto.getApplicantFirstName())) {
            Toast.makeText(context, "Please enter " + entityName + " First Name.", Toast.LENGTH_LONG).show();
            editTextApplicantFirstName.setError("Please enter " + entityName + " First Name.");
            return 6;
        }

        //Applicant Middle Name
        franchiseeDetailsDto.setApplicantMiddleName(editTextApplicantMiddleName.getText().toString().trim());

        //Applicant Last Name
        franchiseeDetailsDto.setApplicantLastName(editTextApplicantLastName.getText().toString().trim());
        if (TextUtils.isEmpty(franchiseeDetailsDto.getApplicantLastName())) {
            Toast.makeText(context, "Please enter " + entityName + " Last Name.", Toast.LENGTH_LONG).show();
            editTextApplicantLastName.setError("Please enter " + entityName + " Last Name.");
            return 8;
        }
        return 0;
    }

    public int IsFranchiseeDetailsValidated() {

        //STEP 1: Vakrangee Kendra Type
        if (TextUtils.isEmpty(franchiseeDetailsDto.getVakrangeeKendraType())) {
            showMessage("Please select Vakrangee Kendra Type.");
            return 1;
        }

        //STEP 1.1: Check if IOCL Name exists if IOCL Selected
        if (franchiseeDetailsDto.getVakrangeeKendraType().equalsIgnoreCase("2")) {
            franchiseeDetailsDto.setIoclRoCode(editTextIOCLROCode.getText().toString());
            franchiseeDetailsDto.setIoclRoName(editTextIOCLROName.getText().toString());

            //IOCL Code
            if (TextUtils.isEmpty(franchiseeDetailsDto.getIoclRoCode())) {
                Toast.makeText(context, "Please enter proper IOCL Code.", Toast.LENGTH_LONG).show();
                editTextIOCLROCode.setError("Please enter proper IOCL Code.");
                return 1;
            }

            //IOCL Name
            if (TextUtils.isEmpty(franchiseeDetailsDto.getIoclRoName())) {
                Toast.makeText(context, ENTER_IOCL_NAME, Toast.LENGTH_LONG).show();
                editTextIOCLROCode.setError(ENTER_IOCL_NAME);
                editTextIOCLROName.setError(ENTER_IOCL_NAME);
                return 1;
            }
        } else if (franchiseeDetailsDto.getVakrangeeKendraType().equalsIgnoreCase("3")) {      //Nayara
            franchiseeDetailsDto.setNayaraRoCode(editTextNayaraROCode.getText().toString());
            franchiseeDetailsDto.setNayaraRoName(editTextNayaraROName.getText().toString());

            //Nayara Code
            if (TextUtils.isEmpty(franchiseeDetailsDto.getNayaraRoCode())) {
                Toast.makeText(context, "Please enter proper Nayara CMS Code.", Toast.LENGTH_LONG).show();
                editTextNayaraROCode.setError("Please enter proper Nayara CMS Code.");
                return 1;
            }

            int len = franchiseeDetailsDto.getNayaraRoCode().trim().length();
            if (len < 10) {
                Toast.makeText(context, "Please enter proper Nayara CMS Code.", Toast.LENGTH_LONG).show();
                editTextNayaraROCode.setError("Please enter proper Nayara CMS Code.");
                return 1;
            }

            //Nayara Name
            if (TextUtils.isEmpty(franchiseeDetailsDto.getNayaraRoName())) {
                Toast.makeText(context, ENTER_NAYARA_NAME, Toast.LENGTH_LONG).show();
                editTextNayaraROCode.setError(ENTER_NAYARA_NAME);
                editTextNayaraROName.setError(ENTER_NAYARA_NAME);
                return 1;
            }
        }

        //STEP 2: Entity Type
        if (TextUtils.isEmpty(franchiseeDetailsDto.getEntityType())) {
            Toast.makeText(context, "Please select Entity Type.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerEntityType, "Please select Entity Type.", context);
            return 2;
        }

        String entityType = spinnerEntityType.getSelectedItem().toString();
        if (entityType.equalsIgnoreCase(INDIVIDUAL)) {
            int status = validateIndividualDetails();
            if (status != 0)
                return status;

        } else {

            //Validate Entity Proof Details
            int status = validateEntityProofDetails();
            if (status != 0)
                return status;

            //STEP 1: Entity Name
            franchiseeDetailsDto.setEntityName(editTextCoperativeSocietyName.getText().toString().trim());
            if (TextUtils.isEmpty(franchiseeDetailsDto.getEntityName())) {
                Toast.makeText(context, "Please enter Entity Name.", Toast.LENGTH_LONG).show();
                editTextCoperativeSocietyName.setError("Please enter Entity Name.");
                return 8;
            }

            //STEP 2: Date Of Incorporation
            if (TextUtils.isEmpty(franchiseeDetailsDto.getDateOfIncorporation())) {
                Toast.makeText(context, "Please select Date Of Incorporation.", Toast.LENGTH_LONG).show();
                textViewDateOfIncorporation.setError("Please select Date Of Incorporation.");
                return 15;
            }
        }

        //STEP 3: Referred Type
        if (TextUtils.isEmpty(franchiseeDetailsDto.getReferredType())) {
            Toast.makeText(context, "Please select Referred By.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerReferredBy, "Please select Referred By.", context);
            return 3;
        }

        //STEP 4: Choose Photo
        if (TextUtils.isEmpty(franchiseeDetailsDto.getApplicantPicFileId()) || franchiseeDetailsDto.getApplicantPicFileId().equalsIgnoreCase("0")) {
            if (TextUtils.isEmpty(franchiseeDetailsDto.getProfilePicBase64())) {
                showMessage("Please add Applicant's photo.");
                return 4;
            }
        }

        //STEP 5: Applicant Salutation
        if (TextUtils.isEmpty(franchiseeDetailsDto.getApplicantSalutation())) {
            Toast.makeText(context, "Please select Applicant Salutation.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerApplicantSalutation, "Please select Applicant Salutation.", context);
            return 5;
        }

        //STEP 6: Applicant First Name
        franchiseeDetailsDto.setApplicantFirstName(editTextApplicantFirstName.getText().toString().trim());
        if (TextUtils.isEmpty(franchiseeDetailsDto.getApplicantFirstName())) {
            Toast.makeText(context, "Please enter Applicant First Name.", Toast.LENGTH_LONG).show();
            editTextApplicantFirstName.setError("Please enter Applicant First Name.");
            return 6;
        }

        //STEP 7: Applicant Middle Name
        franchiseeDetailsDto.setApplicantMiddleName(editTextApplicantMiddleName.getText().toString().trim());

        //STEP 8: Applicant Last Name
        franchiseeDetailsDto.setApplicantLastName(editTextApplicantLastName.getText().toString().trim());
        if (TextUtils.isEmpty(franchiseeDetailsDto.getApplicantLastName())) {
            Toast.makeText(context, "Please enter Applicant Last Name.", Toast.LENGTH_LONG).show();
            editTextApplicantLastName.setError("Please enter Applicant Last Name.");
            return 8;
        }

        //STEP 16: Nationality
        if (TextUtils.isEmpty(franchiseeDetailsDto.getNationality())) {
            Toast.makeText(context, "Please select Nationality.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerNationality, "Please select Nationality.", context);
            return 16;
        }

        //STEP 17: Highest Qualification
        if (TextUtils.isEmpty(franchiseeDetailsDto.getHighestQualification()) || franchiseeDetailsDto.getHighestQualification().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Highest Qualification.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerHighestQualification, "Please select Highest Qualification.", context);
            return 17;
        }

        //Year Of Passing
        if (TextUtils.isEmpty(franchiseeDetailsDto.getYearOfPassing())) {
            Toast.makeText(context, "Please select Year Of Passing.", Toast.LENGTH_LONG).show();
            textViewYearOfPassing.setError("Please select Year Of Passing.");
            return 15;
        }

        //STEP 18: Highest Qualification Upload
        if (TextUtils.isEmpty(franchiseeDetailsDto.getQualificationUploadFileId()) || franchiseeDetailsDto.getQualificationUploadFileId().equalsIgnoreCase("0")) {
            if (TextUtils.isEmpty(franchiseeDetailsDto.getHighestQualiUploadBase64())) {
                showMessage("Please add Highest Qualification Upload.");
                return 4;
            }
        }

        //STEP 19: Prior Experience
        if (TextUtils.isEmpty(franchiseeDetailsDto.getPriorExperience())) {
            Toast.makeText(context, "Please select Prior Experience.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerPriorExperience, "Please select Prior Experience.", context);
            return 19;
        }

        //STEP 20: Current Occupation
        if (TextUtils.isEmpty(franchiseeDetailsDto.getCurrentOccupation()) || franchiseeDetailsDto.getCurrentOccupation().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Current Occupation.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerCurrentOccupation, "Please select Current Occupation.", context);
            return 20;
        }


        //region BCA Code Details
        if (!TextUtils.isEmpty(franchiseeDetailsDto.getIsHavingBCACode()) && franchiseeDetailsDto.getIsHavingBCACode().equalsIgnoreCase("1")) {

            //BCA Bank Name
            if (TextUtils.isEmpty(franchiseeDetailsDto.getBcaBankName()) || franchiseeDetailsDto.getBcaBankName().equalsIgnoreCase("0")) {
                Toast.makeText(context, "Please select BCA Bank Name.", Toast.LENGTH_LONG).show();
                GUIUtils.setErrorToSpinner(spinnerBCABankName, "Please select BCA Bank Name.", context);
                return 2;
            }

            //BCA Code
            franchiseeDetailsDto.setBcaCode(editTextBCACode.getText().toString().trim());
            if (TextUtils.isEmpty(franchiseeDetailsDto.getBcaCode())) {
                Toast.makeText(context, "Please enter BCA Code.", Toast.LENGTH_LONG).show();
                editTextBCACode.setError("Please enter BCA Code.");
                return 8;
            }

            //BCA Code Length minimum 1
            int remarksLen = editTextBCACode.getText().toString().length();
            if (remarksLen < 1) {
                editTextBCACode.setError("BCA Code must have minimum 1 character.");
                return 8;
            }

        }

        return 0;
    }

    private int validateIndividualDetails() {

        //STEP 9: Father Salutation
        if (TextUtils.isEmpty(franchiseeDetailsDto.getFatherSalutation())) {
            Toast.makeText(context, "Please select Father Salutation.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerFatherSalutation, "Please select Father Salutation.", context);
            return 9;
        }

        //STEP 10: Father First Name
        franchiseeDetailsDto.setFatherFirstName(editTextFatherFirstName.getText().toString().trim());
        if (TextUtils.isEmpty(franchiseeDetailsDto.getFatherFirstName())) {
            Toast.makeText(context, "Please enter Father First Name.", Toast.LENGTH_LONG).show();
            editTextFatherFirstName.setError("Please enter Father First Name.");
            return 10;
        }

        //STEP 12: Father Last Name
        franchiseeDetailsDto.setFatherLastName(editTextFatherLastName.getText().toString().trim());
        if (TextUtils.isEmpty(franchiseeDetailsDto.getFatherLastName())) {
            Toast.makeText(context, "Please enter Father Last Name.", Toast.LENGTH_LONG).show();
            editTextFatherLastName.setError("Please enter Father Last Name.");
            return 12;
        }

        //STEP 13: Gender
        if (TextUtils.isEmpty(franchiseeDetailsDto.getGender())) {
            showMessage("Please select Gender.");
            return 13;
        }

        //STEP 14: Marital Status
        if (TextUtils.isEmpty(franchiseeDetailsDto.getMaritalStatus())) {
            showMessage("Please select Marital Status.");
            return 14;
        } else if (franchiseeDetailsDto.getMaritalStatus().equalsIgnoreCase("M")) {
            int status = validateSpouseDetails();
            if (status != 0)
                return 14;
        }

        //STEP 15: Date Of Birth
        if (TextUtils.isEmpty(franchiseeDetailsDto.getDateOfBirth())) {
            Toast.makeText(context, "Please select Date Of Birth.", Toast.LENGTH_LONG).show();
            textViewDateOfBirth.setError("Please select Date Of Birth.");
            return 15;
        }
        return 0;
    }

    private int validateSpouseDetails() {
        //STEP 1: Spouse Salutation
        if (TextUtils.isEmpty(franchiseeDetailsDto.getSpouseSalutation())) {
            Toast.makeText(context, "Please select Spouse Salutation.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerSpouseSalutation, "Please select Spouse Salutation.", context);
            return 1;
        }

        //STEP 2: Spouse First Name
        franchiseeDetailsDto.setSpouseFirstName(editTextSpouseFirstName.getText().toString().trim());
        if (TextUtils.isEmpty(franchiseeDetailsDto.getSpouseFirstName())) {
            Toast.makeText(context, "Please enter Spouse First Name.", Toast.LENGTH_LONG).show();
            editTextSpouseFirstName.setError("Please enter Spouse First Name.");
            return 1;
        }

        //STEP 3: Spouse Middle Name
        franchiseeDetailsDto.setSpouseMiddleName(editTextSpouseMiddleName.getText().toString().trim());

        //STEP 4: Spouse Last Name
        franchiseeDetailsDto.setSpouseLastName(editTextSpouseLastName.getText().toString().trim());
        if (TextUtils.isEmpty(franchiseeDetailsDto.getSpouseLastName())) {
            Toast.makeText(context, "Please enter Spouse Last Name.", Toast.LENGTH_LONG).show();
            editTextSpouseLastName.setError("Please enter Spouse Last Name.");
            return 1;
        }
        return 0;
    }

    private int validateEntityProofDetails() {

        //STEP 1: Entity Proof Type
        if (TextUtils.isEmpty(franchiseeDetailsDto.getEntityProofType()) || franchiseeDetailsDto.getEntityProofType().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Entity Proof Type.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerEntityProofType, "Please select Entity Proof Type.", context);
            return 9;
        }

        //STEP 2: Entity Proof Image
        if (TextUtils.isEmpty(franchiseeDetailsDto.getEntityProofFileId()) || franchiseeDetailsDto.getEntityProofFileId().equalsIgnoreCase("0")) {
            if (TextUtils.isEmpty(franchiseeDetailsDto.getEntityProofFileBase64())) {
                showMessage("Please add Entity Proof.");
                return 4;
            }
        }

        return 0;
    }

    private void showDateTimeDialogPicker() {

        Date defaultDate = null;
        if (selectedDateTimeId == R.id.textViewDateOfBirth) {
            defaultDate = startDate;
        } else if (selectedDateTimeId == R.id.textViewDateOfIncorporation) {
            defaultDate = incorporationDate;
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

                        if (selectedDateTimeId == R.id.textViewDateOfBirth) {
                            startDate = datetime;
                            strStartDate = formateYMD;
                            franchiseeDetailsDto.setDateOfBirth(strStartDate);

                            String DOB = CommonUtils.getFormattedDate(YYYY_MM_DD_CONST, DD_MM_YYYY_CONST, franchiseeDetailsDto.getDateOfBirth());
                            textViewDateOfBirth.setText(DOB);

                        } else if (selectedDateTimeId == R.id.textViewDateOfIncorporation) {
                            incorporationDate = datetime;
                            strIncorporationDate = formateYMD;
                            franchiseeDetailsDto.setDateOfIncorporation(strIncorporationDate);

                            String DateOfIncorporation = CommonUtils.getFormattedDate(YYYY_MM_DD_CONST, DD_MM_YYYY_CONST, franchiseeDetailsDto.getDateOfIncorporation());
                            textViewDateOfIncorporation.setText(DateOfIncorporation);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //Set Min and Max Date
        long now = new Date().getTime() - 1000;

        Calendar c = Calendar.getInstance();
        c.set(1920, 0, 1);//Year,Month -1,Day

        Calendar curCal = Calendar.getInstance();
        int day = curCal.get(Calendar.DATE);
        int month = curCal.get(Calendar.MONTH);
        int year = curCal.get(Calendar.YEAR) - 18;
        curCal.set(year, month, day);

        if (selectedDateTimeId == R.id.textViewDateOfBirth) {
            dateTimePickerDialog.setMinDate(c.getTimeInMillis());
            dateTimePickerDialog.setMaxDate(curCal.getTimeInMillis());

        } else if (selectedDateTimeId == R.id.textViewDateOfIncorporation) {
            dateTimePickerDialog.setMinDate(c.getTimeInMillis());
            dateTimePickerDialog.setMaxDate(now);
        }

        dateTimePickerDialog.setCancelable(false);
        dateTimePickerDialog.setActionButtonName("Save");
        dateTimePickerDialog.show();

    }

    public void reloadData(String data, boolean IsEditable) {
        //Reload Data
        if (TextUtils.isEmpty(data))
            franchiseeDetailsDto = new FAPFranchiseeDetailsDto();
        else {
            try {
                Gson gson = new GsonBuilder().create();
                franchiseeDetailsDto = gson.fromJson(data, FAPFranchiseeDetailsDto.class);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.IsEditable = IsEditable;
        getAllFranchiseeDetailSpinnerData = new GetAllFranchiseeDetailSpinnerData();
        getAllFranchiseeDetailSpinnerData.execute("");
    }

    private void getIOCLNameUsingROCode(String ioclCode, boolean IsFirstTime) {

        //STEP 1: Check if Entered IOCL Code already Exists
        checkIOCOdeAlreadyExist = new CheckIOCOdeAlreadyExist(IsFirstTime);
        checkIOCOdeAlreadyExist.execute(ioclCode);
    }

    private void getNayaraRONameUsingROCode(String nayaraCode, boolean IsFirstTime) {

        //STEP 1: Check if Entered Nayara Code already Exists
        checkNayaraROCOdeAlreadyExist = new CheckNayaraROCOdeAlreadyExist(IsFirstTime);
        checkNayaraROCOdeAlreadyExist.execute(nayaraCode);
    }

    private void bindSpinner() {

        String appNo = ((NextGenFranchiseeApplicationActivity) getActivity()).getApplicationNo();
        editTextApplicationNo.setText(appNo);

        //STEP 1: Vakrangee Kendra Type
        radioGroupVakrangeeKendraType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonIOCL) {
                    franchiseeDetailsDto.setVakrangeeKendraType("2");
                    layoutIOCLROCode.setVisibility(View.VISIBLE);
                    layoutIOCLROName.setVisibility(View.VISIBLE);
                    layoutNayaraROCode.setVisibility(View.GONE);
                    layoutNayaraROName.setVisibility(View.GONE);

                    //Nayara RO Code
                    franchiseeDetailsDto.setNayaraRoCode(null);
                    franchiseeDetailsDto.setNayaraRoName(null);
                    editTextNayaraROCode.setText(franchiseeDetailsDto.getNayaraRoCode());

                    ((NextGenFranchiseeApplicationFormFragment) getParentFragment()).fragmentFAPProposedKendraDetails.updateVakrangeeKendraModel(franchiseeDetailsDto.getVakrangeeKendraType());
                } else if (checkedId == R.id.radioButtonNonIOCL) {
                    franchiseeDetailsDto.setVakrangeeKendraType("1");

                    //Nayara RO Code
                    franchiseeDetailsDto.setNayaraRoCode(null);
                    franchiseeDetailsDto.setNayaraRoName(null);
                    editTextNayaraROCode.setText(franchiseeDetailsDto.getNayaraRoCode());

                    //IOCL RO Code
                    franchiseeDetailsDto.setIoclRoCode(null);
                    franchiseeDetailsDto.setIoclRoName(null);
                    editTextIOCLROCode.setText(franchiseeDetailsDto.getIoclRoCode());

                    layoutIOCLROCode.setVisibility(View.GONE);
                    layoutIOCLROName.setVisibility(View.GONE);
                    layoutNayaraROCode.setVisibility(View.GONE);
                    layoutNayaraROName.setVisibility(View.GONE);

                    ((NextGenFranchiseeApplicationFormFragment) getParentFragment()).fragmentFAPProposedKendraDetails.updateVakrangeeKendraModel(franchiseeDetailsDto.getVakrangeeKendraType());

                } else if (checkedId == R.id.radioButtonNayara) {
                    franchiseeDetailsDto.setVakrangeeKendraType("3");
                    layoutIOCLROCode.setVisibility(View.GONE);
                    layoutIOCLROName.setVisibility(View.GONE);
                    layoutNayaraROCode.setVisibility(View.VISIBLE);
                    layoutNayaraROName.setVisibility(View.VISIBLE);

                    //IOCL RO Code
                    franchiseeDetailsDto.setIoclRoCode(null);
                    franchiseeDetailsDto.setIoclRoName(null);
                    editTextIOCLROCode.setText(franchiseeDetailsDto.getIoclRoCode());

                    ((NextGenFranchiseeApplicationFormFragment) getParentFragment()).fragmentFAPProposedKendraDetails.updateVakrangeeKendraModel(franchiseeDetailsDto.getVakrangeeKendraType());
                }
            }
        });

        if (!TextUtils.isEmpty(franchiseeDetailsDto.getVakrangeeKendraType())) {
            int type = Integer.parseInt(franchiseeDetailsDto.getVakrangeeKendraType());
            if (type == 2)
                radioGroupVakrangeeKendraType.check(R.id.radioButtonIOCL);
            else if (type == 3)
                radioGroupVakrangeeKendraType.check(R.id.radioButtonNayara);
            else
                radioGroupVakrangeeKendraType.check(R.id.radioButtonNonIOCL);
        }
        ((NextGenFranchiseeApplicationFormFragment) getParentFragment()).fragmentFAPProposedKendraDetails.updateVakrangeeKendraModel(franchiseeDetailsDto.getVakrangeeKendraType());

        //STEP 1.1:
        editTextIOCLROCode.setText(franchiseeDetailsDto.getIoclRoCode());
        if (!TextUtils.isEmpty(franchiseeDetailsDto.getIoclRoCode())) {
            getIOCLNameUsingROCode(franchiseeDetailsDto.getIoclRoCode(), true);
        }

        editTextIOCLROCode.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                String data = s.toString().trim();
                int length = data.length();
                if (length == 0) {
                    editTextIOCLROName.setText("");
                    return;
                }

                //STEP 1: IOCL Code MaxLength 6
                if (length < 6)
                    return;

                editTextIOCLROCode.setTextColor(Color.parseColor("#468847"));

                //Get IOCL Name using IOCL Code
                getIOCLNameUsingROCode(data, false);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do Nothing
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Do Nothing
            }
        });

        //Nayara CMS Code
        editTextNayaraROCode.setText(franchiseeDetailsDto.getNayaraRoCode());
        if (!TextUtils.isEmpty(franchiseeDetailsDto.getNayaraRoCode())) {
            getNayaraRONameUsingROCode(franchiseeDetailsDto.getNayaraRoCode(), true);
        }

        editTextNayaraROCode.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                String data = s.toString().trim();
                int length = data.length();
                if (length == 0) {
                    editTextNayaraROName.setText("");
                    return;
                }

                String s1 = editTextNayaraROCode.getText().toString().trim();
                CommonUtils.setWordsCaps(s1, editTextNayaraROCode);

                //STEP 1: Nayara Code MaxLength 10
                if (length < 10)
                    return;

                editTextNayaraROCode.setTextColor(Color.parseColor("#468847"));

                //Get Nayara Name using Nayara Code
                getNayaraRONameUsingROCode(data, false);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do Nothing
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Do Nothing
            }
        });

        //STEP 2: Entity Type
        CustomFranchiseeApplicationSpinnerAdapter entityTypeAdapter = new CustomFranchiseeApplicationSpinnerAdapter(context, entityTypeList);
        spinnerEntityType.setAdapter(entityTypeAdapter);
        int entityPos = fapRepo.getSelectedPos(entityTypeList, franchiseeDetailsDto.getEntityType());
        spinnerEntityType.setSelection(entityPos);
        spinnerEntityType.setOnItemSelectedListener(this);

        //STEP 3: Referred By
        CustomFranchiseeApplicationSpinnerAdapter referredByAdapter = new CustomFranchiseeApplicationSpinnerAdapter(context, referredByList);
        spinnerReferredBy.setAdapter(referredByAdapter);
        int referredByPos = fapRepo.getSelectedPos(referredByList, franchiseeDetailsDto.getReferredType());
        spinnerReferredBy.setSelection(referredByPos);
        spinnerReferredBy.setOnItemSelectedListener(this);

        //STEP 4: Applicant Salutation
        CustomFranchiseeApplicationSpinnerAdapter salutationAdapter = new CustomFranchiseeApplicationSpinnerAdapter(context, salutationList);
        spinnerApplicantSalutation.setAdapter(salutationAdapter);
        int appSalutationPos = fapRepo.getSelectedPos(salutationList, franchiseeDetailsDto.getApplicantSalutation());
        spinnerApplicantSalutation.setSelection(appSalutationPos);
        spinnerApplicantSalutation.setOnItemSelectedListener(this);

        //STEP 4.1: Applicant First Name
        editTextApplicantFirstName.setText(franchiseeDetailsDto.getApplicantFirstName());

        //STEP 4.2: Applicant Middle Name
        editTextApplicantMiddleName.setText(franchiseeDetailsDto.getApplicantMiddleName());

        //STEP 4.3: Applicant Last Name
        editTextApplicantLastName.setText(franchiseeDetailsDto.getApplicantLastName());

        //STEP 5: Father Salutation
        CustomFranchiseeApplicationSpinnerAdapter fatherSalutationAdapter = new CustomFranchiseeApplicationSpinnerAdapter(context, salutationList);
        spinnerFatherSalutation.setAdapter(fatherSalutationAdapter);
        int fatherSalutationPos = fapRepo.getSelectedPos(salutationList, franchiseeDetailsDto.getFatherSalutation());
        spinnerFatherSalutation.setSelection(fatherSalutationPos);
        spinnerFatherSalutation.setOnItemSelectedListener(this);

        //STEP 5.1: Father First Name
        editTextFatherFirstName.setText(franchiseeDetailsDto.getFatherFirstName());

        //STEP 5.2: Father Middle Name
        editTextFatherMiddleName.setText(franchiseeDetailsDto.getFatherMiddleName());

        //STEP 5.3: Father Last Name
        editTextFatherLastName.setText(franchiseeDetailsDto.getFatherLastName());

        //STEP 6: Gender
        radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonMale) {
                    franchiseeDetailsDto.setGender("M");
                } else if (checkedId == R.id.radioButtonFemale) {
                    franchiseeDetailsDto.setGender("F");
                } else if (checkedId == R.id.radioButtonOthers) {
                    franchiseeDetailsDto.setGender("O");
                }
            }
        });

        if (!TextUtils.isEmpty(franchiseeDetailsDto.getGender())) {
            switch (franchiseeDetailsDto.getGender().toUpperCase()) {
                case "M":
                    radioGroupGender.check(R.id.radioButtonMale);
                    break;

                case "F":
                    radioGroupGender.check(R.id.radioButtonFemale);
                    break;

                case "O":
                    radioGroupGender.check(R.id.radioButtonOthers);
                    break;

                default:
                    break;
            }
        }

        //STEP 7: Marital Status
        radioGroupMaritalStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonMarried) {
                    franchiseeDetailsDto.setMaritalStatus("M");
                    layoutSpouseName.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.radioButtonSingle) {
                    franchiseeDetailsDto.setMaritalStatus("U");
                    layoutSpouseName.setVisibility(View.GONE);
                }
            }
        });

        if (!TextUtils.isEmpty(franchiseeDetailsDto.getMaritalStatus())) {
            switch (franchiseeDetailsDto.getMaritalStatus().toUpperCase()) {
                case "M":
                    radioGroupMaritalStatus.check(R.id.radioButtonMarried);
                    break;

                case "U":
                    radioGroupMaritalStatus.check(R.id.radioButtonSingle);
                    break;

                default:
                    break;
            }
        }

        //STEP 8: Spouse Salutation
        CustomFranchiseeApplicationSpinnerAdapter spouseSalutationAdapter = new CustomFranchiseeApplicationSpinnerAdapter(context, salutationList);
        spinnerSpouseSalutation.setAdapter(spouseSalutationAdapter);
        int spouseSalutationPos = fapRepo.getSelectedPos(salutationList, franchiseeDetailsDto.getSpouseSalutation());
        spinnerSpouseSalutation.setSelection(spouseSalutationPos);
        spinnerSpouseSalutation.setOnItemSelectedListener(this);

        //STEP 8.1: Spouse First Name
        editTextSpouseFirstName.setText(franchiseeDetailsDto.getSpouseFirstName());

        //STEP 8.2: Spouse Middle Name
        editTextSpouseMiddleName.setText(franchiseeDetailsDto.getSpouseMiddleName());

        //STEP 8.3: Spouse Last Name
        editTextSpouseLastName.setText(franchiseeDetailsDto.getSpouseLastName());

        //STEP 9: Nationality
        spinner_focusablemode(spinnerNationality);
        spinnerNationality.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, nationalityList));
        int nationalityPos = fapRepo.getSelectedPos(nationalityList, franchiseeDetailsDto.getNationality());
        spinnerNationality.setSelection(nationalityPos);
        spinnerNationality.setOnItemSelectedListener(this);

        //STEP 10: Highest Qualification
        CustomFranchiseeApplicationSpinnerAdapter higQualiAdapter = new CustomFranchiseeApplicationSpinnerAdapter(context, highestQualilficationList);
        spinnerHighestQualification.setAdapter(higQualiAdapter);
        int highQualiPos = fapRepo.getSelectedPos(highestQualilficationList, franchiseeDetailsDto.getHighestQualification());
        spinnerHighestQualification.setSelection(highQualiPos);
        spinnerHighestQualification.setOnItemSelectedListener(this);

        //Year Of Passing
        if (!TextUtils.isEmpty(franchiseeDetailsDto.getYearOfPassing())) {
            textViewYearOfPassing.setText(franchiseeDetailsDto.getYearOfPassing());
        }

        //STEP 12: Prior Experience
        CustomFranchiseeApplicationSpinnerAdapter priorExpAdapter = new CustomFranchiseeApplicationSpinnerAdapter(context, priorExpList);
        spinnerPriorExperience.setAdapter(priorExpAdapter);
        int priorExpPos = fapRepo.getSelectedPos(priorExpList, franchiseeDetailsDto.getPriorExperience());
        spinnerPriorExperience.setSelection(priorExpPos);
        spinnerPriorExperience.setOnItemSelectedListener(this);

        //STEP 13: Current Occupation
        CustomFranchiseeApplicationSpinnerAdapter curOccupationAdapter = new CustomFranchiseeApplicationSpinnerAdapter(context, curOccupationList);
        spinnerCurrentOccupation.setAdapter(curOccupationAdapter);
        int curOccupationPos = fapRepo.getSelectedPos(curOccupationList, franchiseeDetailsDto.getCurrentOccupation());
        spinnerCurrentOccupation.setSelection(curOccupationPos);
        spinnerCurrentOccupation.setOnItemSelectedListener(this);

        //STEP 14: Specially Abled and Prior Military Experience checkboxes
        checkboxSpeciallyAbled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean IsChecked) {
                String status = IsChecked ? "1" : "0";
                franchiseeDetailsDto.setIsSpeciallyAbled(status);
            }
        });

        if (!TextUtils.isEmpty(franchiseeDetailsDto.getIsSpeciallyAbled())) {
            int type = Integer.parseInt(franchiseeDetailsDto.getIsSpeciallyAbled());
            if (type == 1)
                checkboxSpeciallyAbled.setChecked(true);
            else
                checkboxSpeciallyAbled.setChecked(false);
        }

        checkboxPriorMiliExperience.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean IsChecked) {
                String status = IsChecked ? "1" : "0";
                franchiseeDetailsDto.setIsPriorMilitaryExperience(status);
            }
        });

        if (!TextUtils.isEmpty(franchiseeDetailsDto.getIsPriorMilitaryExperience())) {
            int type = Integer.parseInt(franchiseeDetailsDto.getIsPriorMilitaryExperience());
            if (type == 1)
                checkboxPriorMiliExperience.setChecked(true);
            else
                checkboxPriorMiliExperience.setChecked(false);
        }

        //STEP 15: Date Of Birth
        if (!TextUtils.isEmpty(franchiseeDetailsDto.getDateOfBirth())) {
            String DOB = CommonUtils.getFormattedDate(YYYY_MM_DD_CONST, DD_MM_YYYY_CONST, franchiseeDetailsDto.getDateOfBirth());
            textViewDateOfBirth.setText(DOB);
        }

        //STEP 16: EmployeeId/VKID
        String IsVKID = txtEmployeeVKIDLbl.getText().toString().equalsIgnoreCase("VKID") ? "1" : "0";
        editTextEmployeeVKID.setText(franchiseeDetailsDto.getEmpVKID());
        if (!TextUtils.isEmpty(franchiseeDetailsDto.getEmpVKID())) {
            getIOCLRoNameORReferralName = new GetIOCLRoNameORReferralName(TYPE_EMPVKID, franchiseeDetailsDto.getEmpVKID());
            getIOCLRoNameORReferralName.execute(IsVKID);
        }
        CommonUtils.AllCapCharCaptial(editTextEmployeeVKID);
        editTextEmployeeVKID.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                String data = s.toString().trim();
                int length = data.length();
                if (length == 0) {
                    editTextNameOfReferral.setText("");
                    return;
                }

                final String IsVKID = txtEmployeeVKIDLbl.getText().toString().equalsIgnoreCase("VKID") ? "1" : "0";
                editTextEmployeeVKID.setTextColor(Color.parseColor("#000000"));

                //Validate Length of Entered VKID and EmployeeID
                if (IsVKID.equalsIgnoreCase("1")) {
                    if (length < 9) {
                        editTextNameOfReferral.setText("");
                        return;
                    }

                    if (!CommonUtils.isValidVKID(data)) {
                        editTextNameOfReferral.setText("");
                        editTextEmployeeVKID.setError("Invalid VKID.");
                        return;
                    }
                } else {
                    if (length < 6) {
                        editTextNameOfReferral.setText("");
                        return;
                    }

                    if (!CommonUtils.isValidEMPID(data)) {
                        editTextNameOfReferral.setText("");
                        editTextEmployeeVKID.setError("Invalid Employee ID.");
                        return;
                    }
                }

                editTextEmployeeVKID.setTextColor(Color.parseColor("#468847"));
                editTextEmployeeVKID.setError(null);

                getIOCLRoNameORReferralName = new GetIOCLRoNameORReferralName(TYPE_EMPVKID, data);
                getIOCLRoNameORReferralName.execute(IsVKID);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do Nothing
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Do Nothing
            }
        });

        //STEP 17: Entity Name
        editTextCoperativeSocietyName.setText(franchiseeDetailsDto.getEntityName());

        //STEP 18: Date Of InCorporation
        if (!TextUtils.isEmpty(franchiseeDetailsDto.getDateOfIncorporation())) {
            String dateOfIncorporation = CommonUtils.getFormattedDate(YYYY_MM_DD_CONST, DD_MM_YYYY_CONST, franchiseeDetailsDto.getDateOfIncorporation());
            textViewDateOfIncorporation.setText(dateOfIncorporation);
        }

        //STEP 19: Applicant Profile Pic
        boolean IsPDF = ((franchiseeDetailsDto.getProfilePicExt() != null) && franchiseeDetailsDto.getProfilePicExt().equalsIgnoreCase("pdf")) ? true : false;
        if (IsPDF) {
            Glide.with(context).asDrawable().load(R.drawable.pdf).into(imgChoosePhoto);
        } else {
            if (!TextUtils.isEmpty(franchiseeDetailsDto.getApplicantPicFileId())) {
                String picUrl = Constants.DownloadImageUrl + franchiseeDetailsDto.getApplicantPicFileId();
                Glide.with(context)
                        .load(picUrl)
                        .apply(new RequestOptions()
                                .error(R.drawable.ic_camera_alt_black_72dp)
                                .placeholder(R.drawable.ic_camera_alt_black_72dp)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))

                        .into(imgChoosePhoto);
            }
        }

        //STEP 20: Highest Qualification Pic
        boolean IsQualiPDF = ((franchiseeDetailsDto.getHighestQualiFileExt() != null) && franchiseeDetailsDto.getHighestQualiFileExt().equalsIgnoreCase("pdf")) ? true : false;
        if (IsQualiPDF) {
            Glide.with(context).asDrawable().load(R.drawable.pdf).into(imgHQualiUploadPhoto);
        } else {
            if (!TextUtils.isEmpty(franchiseeDetailsDto.getQualificationUploadFileId())) {
                String quaUrl = Constants.DownloadImageUrl + franchiseeDetailsDto.getQualificationUploadFileId();
                Glide.with(context)
                        .load(quaUrl)
                        .apply(new RequestOptions()
                                .error(R.drawable.ic_camera_alt_black_72dp)
                                .placeholder(R.drawable.ic_camera_alt_black_72dp)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))
                        .into(imgHQualiUploadPhoto);
            }
        }

        //region BCA Code Details

        //Have BCA Code
        checkboxHaveBCACode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean IsChecked) {
                String status = IsChecked ? "1" : "0";
                franchiseeDetailsDto.setIsHavingBCACode(status);
                if (status.equalsIgnoreCase("1")) {
                    layoutBCADetails.setVisibility(View.VISIBLE);
                } else {
                    layoutBCADetails.setVisibility(View.GONE);
                    spinnerBCABankName.setSelection(0);
                    editTextBCACode.setText("");
                }
            }
        });

        if (!TextUtils.isEmpty(franchiseeDetailsDto.getIsHavingBCACode())) {
            int type = Integer.parseInt(franchiseeDetailsDto.getIsHavingBCACode());
            if (type == 1) {
                checkboxHaveBCACode.setChecked(true);
                layoutBCADetails.setVisibility(View.VISIBLE);
            } else {
                checkboxHaveBCACode.setChecked(false);
                layoutBCADetails.setVisibility(View.GONE);
            }
        }

        //BCA Bank Name List
        setBCABankNameSpinnerAdapter(bcaBankNameList, spinnerBCABankName, franchiseeDetailsDto.getBcaBankName());

        //BCA Code
        editTextBCACode.setText(franchiseeDetailsDto.getBcaCode());

        //endregion

        //Enable/disable views
        GUIUtils.setViewAndChildrenEnabled(layoutFranchiseeParent, IsEditable);
        editTextApplicationNo.setEnabled(false);
        editTextIOCLROName.setEnabled(false);
        editTextNayaraROName.setEnabled(false);
        editTextNameOfReferral.setEnabled(false);

        if (IsEditable) {
            String IsBCAStatus = ((NextGenFranchiseeApplicationFormFragment) getParentFragment()).applicationFormDto.getIsBCA();
            String IsBCA = TextUtils.isEmpty(IsBCAStatus) ? "0" : IsBCAStatus;
            if (IsBCA.equalsIgnoreCase("1")) {
                radioButtonIOCL.setEnabled(false);
                radioButtonNonIOCL.setEnabled(false);
            } else {
                radioButtonIOCL.setEnabled(true);
                radioButtonNonIOCL.setEnabled(true);
            }
        }
    }

    private void spinner_focusablemode(CustomSearchableSpinner spinner) {
        spinner.setFocusable(true);
        spinner.setFocusableInTouchMode(true);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int Id = parent.getId();

        if (Id == R.id.spinnerEntityType) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerEntityType.getItemAtPosition(position);
                franchiseeDetailsDto.setEntityType(entityDto.getId());
                entityTypeSelectionChanges(entityDto.getName());
            }
        } else if (Id == R.id.spinnerReferredBy) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto referredByDto = (CustomFranchiseeApplicationSpinnerDto) spinnerReferredBy.getItemAtPosition(position);
                franchiseeDetailsDto.setReferredType(referredByDto.getId());
                referredBySelection(referredByDto.getName());

            }
        } else if (Id == R.id.spinnerApplicantSalutation) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto appSalutationDto = (CustomFranchiseeApplicationSpinnerDto) spinnerApplicantSalutation.getItemAtPosition(position);
                franchiseeDetailsDto.setApplicantSalutation(appSalutationDto.getId());
                setGenderSelectedAsPerApplicantTitle(appSalutationDto.getName());
            }
        } else if (Id == R.id.spinnerFatherSalutation) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto fatherSalutationDto = (CustomFranchiseeApplicationSpinnerDto) spinnerFatherSalutation.getItemAtPosition(position);
                franchiseeDetailsDto.setFatherSalutation(fatherSalutationDto.getId());
            }
        } else if (Id == R.id.spinnerSpouseSalutation) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto spouseSalutationDto = (CustomFranchiseeApplicationSpinnerDto) spinnerSpouseSalutation.getItemAtPosition(position);
                franchiseeDetailsDto.setSpouseSalutation(spouseSalutationDto.getId());
            }
        } else if (Id == R.id.spinnerNationality) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto nationalityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerNationality.getItemAtPosition(position);
                franchiseeDetailsDto.setNationality(nationalityDto.getId());
            }
        } else if (Id == R.id.spinnerHighestQualification) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto highQualiDto = (CustomFranchiseeApplicationSpinnerDto) spinnerHighestQualification.getItemAtPosition(position);
                franchiseeDetailsDto.setHighestQualification(highQualiDto.getId());
            }
        } else if (Id == R.id.spinnerPriorExperience) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto priorExpDto = (CustomFranchiseeApplicationSpinnerDto) spinnerPriorExperience.getItemAtPosition(position);
                franchiseeDetailsDto.setPriorExperience(priorExpDto.getId());
            }
        } else if (Id == R.id.spinnerCurrentOccupation) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto occupationDto = (CustomFranchiseeApplicationSpinnerDto) spinnerCurrentOccupation.getItemAtPosition(position);
                franchiseeDetailsDto.setCurrentOccupation(occupationDto.getId());
            }
        } else if (Id == R.id.spinnerEntityProofType) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto proofDto = (CustomFranchiseeApplicationSpinnerDto) spinnerEntityProofType.getItemAtPosition(position);
                franchiseeDetailsDto.setEntityProofType(proofDto.getId());
            }
        } else if (Id == R.id.spinnerBCABankName) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto proofDto = (CustomFranchiseeApplicationSpinnerDto) spinnerBCABankName.getItemAtPosition(position);
                franchiseeDetailsDto.setBcaBankName(proofDto.getId());
            }
        }
    }

    private void referredBySelection(String referredBy) {

        if (TextUtils.isEmpty(referredBy)) {
            layoutEmployeeVKID.setVisibility(View.GONE);
            layoutNameOfReferral.setVisibility(View.GONE);
            return;
        }

        switch (referredBy) {

            case "Not Applicable":
                layoutEmployeeVKID.setVisibility(View.GONE);
                layoutNameOfReferral.setVisibility(View.GONE);
                break;

            case "Employee":
                CommonUtils.InputFiletrWithMaxLength(editTextEmployeeVKID, "~#^|$%&*!", 6);
                txtEmployeeVKIDLbl.setText("Employee ID");
                layoutEmployeeVKID.setVisibility(View.VISIBLE);
                layoutNameOfReferral.setVisibility(View.VISIBLE);
                break;

            case "Franchisee":
                CommonUtils.InputFiletrWithMaxLength(editTextEmployeeVKID, "~#^|$%&*!", 9);
                txtEmployeeVKIDLbl.setText("VKID");
                layoutEmployeeVKID.setVisibility(View.VISIBLE);
                layoutNameOfReferral.setVisibility(View.VISIBLE);
                break;

            default:
                layoutEmployeeVKID.setVisibility(View.GONE);
                layoutNameOfReferral.setVisibility(View.GONE);
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //Do Nothing
    }

    private void entityTypeSelectionChanges(String entityType) {

        if (TextUtils.isEmpty(entityType)) {
            layoutCoperativeSocietyName.setVisibility(View.GONE);
            layoutCoperativeSocietyName.setVisibility(View.GONE);
            return;
        }

        //Entity Proof Section
        entityProofSection(entityType, franchiseeDetailsDto.getEntityType());

        switch (entityType) {

            case "Co-Operative Society":
                txtCoperativeSocietyNameLbl.setText("Co-Operative Society Name");
                editTextCoperativeSocietyName.setHint("Co-Operative Society Name");
                layoutCoperativeSocietyName.setVisibility(View.VISIBLE);
                layoutDateOfIncorporation.setVisibility(View.VISIBLE);

                txtApplicantNameLbl.setText("Chair Person Name");
                layoutApplicantName.setVisibility(View.VISIBLE);
                layoutFatherName.setVisibility(View.GONE);
                layoutGender.setVisibility(View.GONE);
                layoutMaritalStatus.setVisibility(View.GONE);
                layoutDateOfBirth.setVisibility(View.GONE);
                layoutSpouseName.setVisibility(View.GONE);
                break;

            case "Company":
                txtCoperativeSocietyNameLbl.setText("Company Name");
                editTextCoperativeSocietyName.setHint("Company Name");
                layoutCoperativeSocietyName.setVisibility(View.VISIBLE);
                layoutDateOfIncorporation.setVisibility(View.VISIBLE);

                txtApplicantNameLbl.setText("Director Name");
                layoutApplicantName.setVisibility(View.VISIBLE);
                layoutFatherName.setVisibility(View.GONE);
                layoutGender.setVisibility(View.GONE);
                layoutMaritalStatus.setVisibility(View.GONE);
                layoutSpouseName.setVisibility(View.GONE);
                layoutDateOfBirth.setVisibility(View.GONE);
                break;

            case INDIVIDUAL:
                layoutCoperativeSocietyName.setVisibility(View.GONE);
                layoutDateOfIncorporation.setVisibility(View.GONE);

                txtApplicantNameLbl.setText("Applicant's Name");
                layoutApplicantName.setVisibility(View.VISIBLE);
                txtFatherNameLbl.setText("Father's Name");
                layoutFatherName.setVisibility(View.VISIBLE);
                layoutGender.setVisibility(View.VISIBLE);
                layoutMaritalStatus.setVisibility(View.VISIBLE);
                layoutDateOfBirth.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(franchiseeDetailsDto.getMaritalStatus()) || franchiseeDetailsDto.getMaritalStatus().equalsIgnoreCase("M")) {
                    layoutSpouseName.setVisibility(View.VISIBLE);
                }
                break;

            case "Limited Liability Partnership Firm (LLP)":
                txtCoperativeSocietyNameLbl.setText(FIRMS_NAME);
                editTextCoperativeSocietyName.setHint(FIRMS_NAME);
                layoutCoperativeSocietyName.setVisibility(View.VISIBLE);
                layoutDateOfIncorporation.setVisibility(View.VISIBLE);

                txtApplicantNameLbl.setText("Partner Name");
                layoutApplicantName.setVisibility(View.VISIBLE);
                layoutFatherName.setVisibility(View.GONE);
                layoutGender.setVisibility(View.GONE);
                layoutMaritalStatus.setVisibility(View.GONE);
                layoutDateOfBirth.setVisibility(View.GONE);
                layoutSpouseName.setVisibility(View.GONE);
                break;

            case "NGO":
                txtCoperativeSocietyNameLbl.setText(ORGANIZATION_NAME);
                editTextCoperativeSocietyName.setHint(ORGANIZATION_NAME);
                layoutCoperativeSocietyName.setVisibility(View.VISIBLE);
                layoutDateOfIncorporation.setVisibility(View.VISIBLE);

                txtApplicantNameLbl.setText("Name Of NGO Head");
                layoutApplicantName.setVisibility(View.VISIBLE);
                layoutFatherName.setVisibility(View.GONE);
                layoutGender.setVisibility(View.GONE);
                layoutMaritalStatus.setVisibility(View.GONE);
                layoutDateOfBirth.setVisibility(View.GONE);
                layoutSpouseName.setVisibility(View.GONE);
                break;

            case "Partnership":
                txtCoperativeSocietyNameLbl.setText(FIRMS_NAME);
                editTextCoperativeSocietyName.setHint(FIRMS_NAME);
                layoutCoperativeSocietyName.setVisibility(View.VISIBLE);
                layoutDateOfIncorporation.setVisibility(View.VISIBLE);

                txtApplicantNameLbl.setText("Partner Name");
                layoutApplicantName.setVisibility(View.VISIBLE);
                layoutFatherName.setVisibility(View.GONE);
                layoutGender.setVisibility(View.GONE);
                layoutMaritalStatus.setVisibility(View.GONE);
                layoutDateOfBirth.setVisibility(View.GONE);
                layoutSpouseName.setVisibility(View.GONE);
                break;

            case "Proprietorship (Owned by HUF)":
                txtCoperativeSocietyNameLbl.setText(FIRMS_NAME);
                editTextCoperativeSocietyName.setHint(FIRMS_NAME);
                layoutCoperativeSocietyName.setVisibility(View.VISIBLE);
                layoutDateOfIncorporation.setVisibility(View.VISIBLE);

                txtApplicantNameLbl.setText("Karta Name");
                layoutApplicantName.setVisibility(View.VISIBLE);
                layoutFatherName.setVisibility(View.GONE);
                layoutGender.setVisibility(View.GONE);
                layoutMaritalStatus.setVisibility(View.GONE);
                layoutDateOfBirth.setVisibility(View.GONE);
                layoutSpouseName.setVisibility(View.GONE);
                break;

            case "Proprietorship (Owned by Individual)":
                txtCoperativeSocietyNameLbl.setText(FIRMS_NAME);
                editTextCoperativeSocietyName.setHint(FIRMS_NAME);
                layoutCoperativeSocietyName.setVisibility(View.VISIBLE);
                layoutDateOfIncorporation.setVisibility(View.VISIBLE);

                txtApplicantNameLbl.setText("Propriertor Name");
                layoutApplicantName.setVisibility(View.VISIBLE);
                layoutFatherName.setVisibility(View.GONE);
                layoutGender.setVisibility(View.GONE);
                layoutMaritalStatus.setVisibility(View.GONE);
                layoutDateOfBirth.setVisibility(View.GONE);
                layoutSpouseName.setVisibility(View.GONE);
                break;

            case "Self Help Group":
                txtCoperativeSocietyNameLbl.setText(ORGANIZATION_NAME);
                editTextCoperativeSocietyName.setHint(ORGANIZATION_NAME);
                layoutCoperativeSocietyName.setVisibility(View.VISIBLE);
                layoutDateOfIncorporation.setVisibility(View.VISIBLE);

                txtApplicantNameLbl.setText("Name Of Organisation Head");
                layoutApplicantName.setVisibility(View.VISIBLE);
                layoutFatherName.setVisibility(View.GONE);
                layoutGender.setVisibility(View.GONE);
                layoutMaritalStatus.setVisibility(View.GONE);
                layoutDateOfBirth.setVisibility(View.GONE);
                layoutSpouseName.setVisibility(View.GONE);
                break;

            case "Trust":
                txtCoperativeSocietyNameLbl.setText("Trust Name");
                editTextCoperativeSocietyName.setHint("Trust Name");
                layoutCoperativeSocietyName.setVisibility(View.VISIBLE);
                layoutDateOfIncorporation.setVisibility(View.VISIBLE);

                txtApplicantNameLbl.setText("Trustee Name");
                layoutApplicantName.setVisibility(View.VISIBLE);
                layoutFatherName.setVisibility(View.GONE);
                layoutGender.setVisibility(View.GONE);
                layoutMaritalStatus.setVisibility(View.GONE);
                layoutDateOfBirth.setVisibility(View.GONE);
                layoutSpouseName.setVisibility(View.GONE);
                break;

            default:
                layoutCoperativeSocietyName.setVisibility(View.GONE);
                layoutDateOfIncorporation.setVisibility(View.GONE);

                txtApplicantNameLbl.setText("Applicant's Name");
                layoutApplicantName.setVisibility(View.VISIBLE);
                txtFatherNameLbl.setText("Father's Name");
                layoutFatherName.setVisibility(View.VISIBLE);
                layoutGender.setVisibility(View.VISIBLE);
                layoutMaritalStatus.setVisibility(View.VISIBLE);
                layoutDateOfBirth.setVisibility(View.VISIBLE);
                break;
        }

        setCompulsoryMarkToFatherAndApplicantLabel();

    }

    class GetAllFranchiseeDetailSpinnerData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressSpinner(context);
        }

        @Override
        protected String doInBackground(String... strings) {

            //STEP 1: Entity Type
            entityTypeList = fapRepo.getEntityList();

            //STEP 2: Referred By
            referredByList = fapRepo.getReferredByList();

            //STEP 3: Salutation
            salutationList = fapRepo.getSalutationList();

            //STEP 4: Nationality
            nationalityList = fapRepo.getNationalityList();

            //STEP 5: Highest Qualification
            highestQualilficationList = fapRepo.getHighestQualificationList();

            //STEP 6: Prior Experience
            priorExpList = fapRepo.getPriorExperienceList();

            //STEP 7: Current Occupation
            curOccupationList = fapRepo.getCurrentOccupationList();

            //BCA Bank Name List
            bcaBankNameList = fapRepo.getBCABankNameList();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissProgressSpinner();
            bindSpinner();
        }
    }

    class GetIOCLRoNameORReferralName extends AsyncTask<String, Void, String> {

        private int type;
        private String Id;
        private boolean IsVKID = false;
        private String response, name;

        public GetIOCLRoNameORReferralName(int type, String Id) {
            this.type = type;
            this.Id = Id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                switch (type) {

                    case TYPE_IOCL_RO_CODE:
                        response = fapRepo.getIOCLRoName(Id);
                        break;

                    case TYPE_NAYARA_RO_CODE:
                        response = fapRepo.getNAYARARoName(Id);
                        break;

                    case TYPE_EMPVKID:
                        String value = strings[0];
                        IsVKID = value.equalsIgnoreCase("1") ? true : false;

                        if (IsVKID)         //Get Name by VKID
                            response = fapRepo.getNameOfReferralByVKID(Id);
                        else                //Get Name by EmployeeID
                            response = fapRepo.getNameOfReferralByEmpId(Id);
                        break;

                    default:
                        break;
                }

                //Get Name from Response
                JSONObject jsonObject = new JSONObject(response);
                name = jsonObject.getString("Name");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            switch (type) {

                case TYPE_IOCL_RO_CODE:
                    editTextIOCLROName.setText(name);
                    break;

                case TYPE_NAYARA_RO_CODE:
                    editTextNayaraROName.setText(name);
                    break;

                case TYPE_EMPVKID:
                    editTextNameOfReferral.setText(name);
                    break;

                default:
                    break;
            }
        }
    }

    class CheckIOCOdeAlreadyExist extends AsyncTask<String, Void, String> {

        private String response;
        private String ioclCode;
        private boolean IsFirstTime = false;

        public CheckIOCOdeAlreadyExist(boolean IsFirstTime) {
            this.IsFirstTime = IsFirstTime;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            ioclCode = strings[0];

            if (!IsFirstTime) {
                try {

                    String Id = ((NextGenFranchiseeApplicationFormFragment) getParentFragment()).applicationFormDto.getNextgenApplicationId();
                    String appId = TextUtils.isEmpty(Id) ? "0" : Id;

                    response = fapRepo.IsIOCLAlreadyExist(ioclCode, appId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (!IsFirstTime) {
                if (TextUtils.isEmpty(response)) {
                    editTextIOCLROCode.setText("");
                    editTextIOCLROName.setText("");
                    AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                    return;
                }

                //Response: -1:RO Code does not available.
                if (response.equalsIgnoreCase("-1")) {
                    editTextIOCLROCode.setText("");
                    editTextIOCLROName.setText("");
                    showMessage("Entered IOCL Code not available.");
                    return;
                }

                //Response: 0:RO Code is already exists.
                if (response.equalsIgnoreCase("0")) {
                    editTextIOCLROCode.setText("");
                    editTextIOCLROName.setText("");
                    showMessage("Entered IOCL Code already exist.");
                    return;
                }
            }

            //Get IOCL Name from IOCL Code
            getIOCLRoNameORReferralName = new GetIOCLRoNameORReferralName(TYPE_IOCL_RO_CODE, ioclCode);
            getIOCLRoNameORReferralName.execute("");

        }
    }

    class CheckNayaraROCOdeAlreadyExist extends AsyncTask<String, Void, String> {

        private String response;
        private String nayaraCode;
        private boolean IsFirstTime = false;

        public CheckNayaraROCOdeAlreadyExist(boolean IsFirstTime) {
            this.IsFirstTime = IsFirstTime;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            nayaraCode = strings[0];

            if (!IsFirstTime) {
                try {

                    String Id = ((NextGenFranchiseeApplicationFormFragment) getParentFragment()).applicationFormDto.getNextgenApplicationId();
                    String appId = TextUtils.isEmpty(Id) ? "0" : Id;

                    response = fapRepo.IsNayaraAlreadyExist(nayaraCode, appId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (!IsFirstTime) {
                if (TextUtils.isEmpty(response)) {
                    editTextNayaraROCode.setText("");
                    editTextNayaraROName.setText("");
                    AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                    return;
                }

                //Response: -1:RO Code does not available.
                if (response.equalsIgnoreCase("-1")) {
                    editTextNayaraROCode.setText("");
                    editTextNayaraROName.setText("");
                    showMessage("Entered Nayara CMS Code not available.");
                    return;
                }

                //Response: 0:RO Code is already exists.
                if (response.equalsIgnoreCase("0")) {
                    editTextNayaraROCode.setText("");
                    editTextNayaraROName.setText("");
                    showMessage("Entered Nayara CMS Code already exist.");
                    return;
                }
            }

            //Get Nayara Name from Nayara Code
            getIOCLRoNameORReferralName = new GetIOCLRoNameORReferralName(TYPE_NAYARA_RO_CODE, nayaraCode);
            getIOCLRoNameORReferralName.execute("");

        }
    }

    public FAPFranchiseeDetailsDto getFranchiseeDetailsDto() {

        //Set all editText data to Dto
        franchiseeDetailsDto.setIoclRoCode(editTextIOCLROCode.getText().toString());
        franchiseeDetailsDto.setNayaraRoCode(editTextNayaraROCode.getText().toString());
        franchiseeDetailsDto.setEntityName(editTextCoperativeSocietyName.getText().toString());
        franchiseeDetailsDto.setApplicantFirstName(editTextApplicantFirstName.getText().toString());
        franchiseeDetailsDto.setApplicantMiddleName(editTextApplicantMiddleName.getText().toString());
        franchiseeDetailsDto.setApplicantLastName(editTextApplicantLastName.getText().toString());
        franchiseeDetailsDto.setFatherFirstName(editTextFatherFirstName.getText().toString());
        franchiseeDetailsDto.setFatherMiddleName(editTextFatherMiddleName.getText().toString());
        franchiseeDetailsDto.setFatherLastName(editTextFatherLastName.getText().toString());
        franchiseeDetailsDto.setSpouseFirstName(editTextSpouseFirstName.getText().toString());
        franchiseeDetailsDto.setSpouseMiddleName(editTextSpouseMiddleName.getText().toString());
        franchiseeDetailsDto.setSpouseLastName(editTextSpouseLastName.getText().toString());
        franchiseeDetailsDto.setEmpVKID(editTextEmployeeVKID.getText().toString());
        franchiseeDetailsDto.setNameOfReferral(editTextNameOfReferral.getText().toString());
        franchiseeDetailsDto.setBcaCode(editTextBCACode.getText().toString().trim());

        return franchiseeDetailsDto;
    }

    public void directCall(String mobileNo) {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getContext().checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_PHONE_CALL);
        } else {
            //Open call function
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + mobileNo));
            startActivity(intent);
        }
    }

    class GetEntityProofTypeData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressSpinner(context);
        }

        @Override
        protected String doInBackground(String... strings) {
            String entityId = strings[0];

            //Entity Proof Type
            entityProofTypeList = fapRepo.getEntityProofTypelList(entityId);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissProgressSpinner();
            reloadEntityProofSection();
        }
    }

    private void entityProofSection(String entityName, String entityId) {

        setApplicantDetailsOnEntityTypeSelection(entityName, entityId);

        if (TextUtils.isEmpty(entityId) || entityId.equalsIgnoreCase("0") || entityName.equalsIgnoreCase(INDIVIDUAL)) {
            layoutEntityProofSection.setVisibility(View.GONE);
            return;
        }

        layoutEntityProofSection.setVisibility(View.VISIBLE);
        getEntityProofTypeData = new GetEntityProofTypeData();
        getEntityProofTypeData.execute(entityId);


    }

    //region Set Entity Proof Type Adapter
    private void setEntityProofTypeSpinnerAdapter(List<CustomFranchiseeApplicationSpinnerDto> list, Spinner spinner, String selId) {

        CustomFranchiseeApplicationSpinnerAdapter entityProofTypeAdapter = new CustomFranchiseeApplicationSpinnerAdapter(context, list);
        spinner.setAdapter(entityProofTypeAdapter);
        int pos = fapRepo.getSelectedPos(list, selId);
        spinner.setSelection(pos);
        spinner.setOnItemSelectedListener(this);

    }
    //endregion

    private void reloadEntityProofSection() {

        //STEP 1: Entity Proof Type
        setEntityProofTypeSpinnerAdapter(entityProofTypeList, spinnerEntityProofType, franchiseeDetailsDto.getEntityProofType());

        //STEP 2: Entity Proof Pic
        boolean IsPDF = ((franchiseeDetailsDto.getEntityProofFileExt() != null) && franchiseeDetailsDto.getEntityProofFileExt().equalsIgnoreCase("pdf")) ? true : false;
        if (IsPDF) {
            Glide.with(context).asDrawable().load(R.drawable.pdf).into(imgEntityProofUploadPhoto);
        } else {
            if (!TextUtils.isEmpty(franchiseeDetailsDto.getEntityProofFileId())) {
                String quaUrl = Constants.DownloadImageUrl + franchiseeDetailsDto.getEntityProofFileId();
                Glide.with(context)
                        .load(quaUrl)
                        .apply(new RequestOptions()
                                .error(R.drawable.ic_camera_alt_black_72dp)
                                .placeholder(R.drawable.ic_camera_alt_black_72dp)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))
                        .into(imgEntityProofUploadPhoto);
            }
        }

    }

    //region Set BCA Bank Name Adapter
    private void setBCABankNameSpinnerAdapter(List<CustomFranchiseeApplicationSpinnerDto> list, Spinner spinner, String selId) {

        CustomFranchiseeApplicationSpinnerAdapter entityProofTypeAdapter = new CustomFranchiseeApplicationSpinnerAdapter(context, list);
        spinner.setAdapter(entityProofTypeAdapter);
        int pos = fapRepo.getSelectedPos(list, selId);
        spinner.setSelection(pos);
        spinner.setOnItemSelectedListener(this);

    }
    //endregion

    //region Application Form Changes
    private void setGenderSelectedAsPerApplicantTitle(String applicantTitle) {
        if (TextUtils.isEmpty(applicantTitle)) {
            return;
        }

        applicantTitle = applicantTitle.trim().replace(".", "");
        switch (applicantTitle) {

            case "Miss":
            case "Mrs":
            case "Ms":
            case "M/s":
                setGenderChecked("F");
                break;

            case "Mr":
                setGenderChecked("M");
                break;

            case "Other":
            case "Others":
                setGenderChecked("O");
                break;

            default:
                setGenderChecked("");
                radioGroupGender.clearCheck();
                break;
        }

    }

    private void setGenderChecked(String gender) {
        franchiseeDetailsDto.setGender(gender);
        if (!TextUtils.isEmpty(franchiseeDetailsDto.getGender())) {
            switch (franchiseeDetailsDto.getGender().toUpperCase()) {
                case "M":
                    radioGroupGender.check(R.id.radioButtonMale);
                    break;

                case "F":
                    radioGroupGender.check(R.id.radioButtonFemale);
                    break;

                case "O":
                    radioGroupGender.check(R.id.radioButtonOthers);
                    break;

                default:
                    break;
            }
        }
    }

    private void setApplicantDetailsOnEntityTypeSelection(String entityName, String entityId) {

        IsIndividualSelected = false;

        if (TextUtils.isEmpty(entityId) || entityId.equalsIgnoreCase("0") || TextUtils.isEmpty(entityName)) {
            IsIndividualSelected = false;

        } else if (entityName.equalsIgnoreCase(INDIVIDUAL)) {
            IsIndividualSelected = true;
        }

        if (IsIndividualSelected) {
            franchiseeDetailsDto.setApplicantFirstName(franchiseeDetailsDto.getEnquiryFirstName());
            franchiseeDetailsDto.setApplicantMiddleName(franchiseeDetailsDto.getEnquiryMiddleName());
            franchiseeDetailsDto.setApplicantLastName(franchiseeDetailsDto.getEnquiryLastName());
            franchiseeDetailsDto.setApplicantSalutation(franchiseeDetailsDto.getEnquiryTitle());
            spinnerApplicantSalutation.setEnabled(false);
            editTextApplicantFirstName.setEnabled(false);
            editTextApplicantMiddleName.setEnabled(false);
            editTextApplicantLastName.setEnabled(false);
            editTextApplicantFirstName.setBackground(deprecateHandler.getDrawable(R.drawable.disable_edittext_bg));
            editTextApplicantMiddleName.setBackground(deprecateHandler.getDrawable(R.drawable.disable_edittext_bg));
            editTextApplicantLastName.setBackground(deprecateHandler.getDrawable(R.drawable.disable_edittext_bg));

        } else {
           /* franchiseeDetailsDto.setApplicantFirstName("");
            franchiseeDetailsDto.setApplicantMiddleName("");
            franchiseeDetailsDto.setApplicantLastName("");
            franchiseeDetailsDto.setApplicantSalutation("");*/
            spinnerApplicantSalutation.setEnabled(true);
            editTextApplicantFirstName.setEnabled(true);
            editTextApplicantMiddleName.setEnabled(true);
            editTextApplicantLastName.setEnabled(true);
            editTextApplicantFirstName.setBackground(deprecateHandler.getDrawable(R.drawable.edittext_bottom_bg));
            editTextApplicantMiddleName.setBackground(deprecateHandler.getDrawable(R.drawable.edittext_bottom_bg));
            editTextApplicantLastName.setBackground(deprecateHandler.getDrawable(R.drawable.edittext_bottom_bg));
        }

        editTextApplicantFirstName.setText(franchiseeDetailsDto.getApplicantFirstName());
        editTextApplicantMiddleName.setText(franchiseeDetailsDto.getApplicantMiddleName());
        editTextApplicantLastName.setText(franchiseeDetailsDto.getApplicantLastName());

        int titlePos = fapRepo.getSelectedPos(salutationList, franchiseeDetailsDto.getApplicantSalutation());
        spinnerApplicantSalutation.setSelection(titlePos);
        spinnerApplicantSalutation.getOnItemSelectedListener().onItemSelected(spinnerApplicantSalutation, spinnerApplicantSalutation.getSelectedView(), titlePos, spinnerApplicantSalutation.getSelectedItemId());

    }

    //endregion

    public void showMonthYearDialog(int year) {

        if (monthYearPickerDialog != null && monthYearPickerDialog.isVisible()) {
            return;
        }

        monthYearPickerDialog = new MonthYearPickerDialog(year);
        monthYearPickerDialog.setListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                String monthString = new DateFormatSymbols().getMonths()[month];
                String monthConverted = "" + (month + 1);
                if (month < 10) {
                    monthConverted = "0" + monthConverted;
                }
                textViewYearOfPassing.setError(null);
                textViewYearOfPassing.setText("" + year);
                franchiseeDetailsDto.setYearOfPassing("" + year);
            }
        });
        monthYearPickerDialog.show(getFragmentManager(), "MonthYearPickerDialog");
        monthYearPickerDialog.setMonthVisiblity(false);

    }

    private void dateOfPassingChecks() {
        int year = 1900;
        if (IsIndividualSelected || layoutDateOfBirth.getVisibility() == View.VISIBLE) {

            if (TextUtils.isEmpty(franchiseeDetailsDto.getDateOfBirth())) {
                textViewDateOfBirth.setError("Please select Franchisee Date Of Birth first.");
                showMessage("Please select Franchisee Date Of Birth first.");
                return;
            }


            String date[] = franchiseeDetailsDto.getDateOfBirth().split("-");
            year = Integer.parseInt(date[0]);
        } else {
            year = 1900;
        }

        showMonthYearDialog(year);

    }
}
