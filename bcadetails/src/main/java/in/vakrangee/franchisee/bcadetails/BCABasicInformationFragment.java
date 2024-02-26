package in.vakrangee.franchisee.bcadetails;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nononsenseapps.filepicker.Utils;

import in.vakrangee.supercore.franchisee.utils.CustomSearchableSpinner;

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

import butterknife.ButterKnife;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.commongui.DateTimePickerDialog;
import in.vakrangee.supercore.franchisee.commongui.FileAttachmentDialog;
import in.vakrangee.supercore.franchisee.commongui.FileUtils;
import in.vakrangee.supercore.franchisee.commongui.FilteredFilePickerActivity;
import in.vakrangee.supercore.franchisee.commongui.MonthYearPickerDialog;
import in.vakrangee.supercore.franchisee.nextgenfranchiseeapplication.CustomFranchiseeApplicationSpinnerDto;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.GUIUtils;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;
import in.vakrangee.supercore.franchisee.utils.ImageZipper;
import in.vakrangee.supercore.franchisee.utils.Logger;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;

public class BCABasicInformationFragment extends BaseFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private static final String TAG = "BCABasicInformationFragment";

    private View view;
    private Context context;
    private PermissionHandler permissionHandler;
    private Logger logger;
    private DeprecateHandler deprecateHandler;

    private BCABasicInformationDto bcaBasicInformationDto;
    private boolean IsEditable = false;
    private GetAllSpinnerData getAllSpinnerData = null;

    private BCAEntryDetailsRepository bcaEntryDetailsRepo;
    private List<CustomFranchiseeApplicationSpinnerDto> salutationList, qualificationList, religionList, bcAbilityList, categoryList,
            stateList, districtList, blockList, vtcList;


    private FileAttachmentDialog fileAttachementDialog;
    private static final int CAMERA_PIC_REQUEST = 100;
    private static final int BROWSE_FOLDER_REQUEST = 101;
    private Uri picUri;                 //Picture URI
    private String SEL_FILE_TYPE;
    private int FROM = -1;
    private DateTimePickerDialog dateTimePickerDialog;
    private DateFormat dateFormatter2 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    private DateFormat dateFormatterYMD = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private Date yearOfPassingDate;
    private String strYearOfPassingDate;
    private Date dateOfPassingDate;
    private String strDateOfPassing;
    private Date dOBDate;
    private String strDOB;
    private int selectedDateTimeId = 0;

    private static final int BCA_PASSPORT_PHOTO = 1;

    private MonthYearPickerDialog monthYearPickerDialog = null;
    private String getDateConvert;

    private TextView txtChoosePhotoLbl;
    private ImageView imgChoosePhoto;
    private TextView txtBCANameLbl;
    private Spinner spinnerBCASalutation;
    private EditText editTextBCAFirstName;
    private EditText editTextBCAMiddleName;
    private EditText editTextBCALastName;
    private TextView txtBCAFatherNameLbl;
    private Spinner spinnerBCAFatherSalutation;
    private EditText editTextBCAFatherFirstName;
    private EditText editTextBCAFatherMiddleName;
    private EditText editTextBCAFatherLastName;
    private TextView txtGenderLbl;
    private RadioGroup radioGroupGender;
    private RadioButton radioButtonMale;
    private RadioButton radioButtonFemale;
    private TextView txtMaritalStatusLbl;
    private RadioGroup radioGroupMaritalStatus;
    private RadioButton radioButtonMarried;
    private RadioButton radioButtonSingle;
    private TextView txtMotherNameLbl;
    private EditText editTextMotherName;
    private TextView txtYearOfPassingLbl;
    private TextView textViewYearOfPassing;
    private TextView txtQualificationLbl;
    private CustomSearchableSpinner spinnerQualification;
    private TextView txtReligionLbl;
    private CustomSearchableSpinner spinnerReligion;
    private TextView txtIIBFCertificationNumberLbl;
    private EditText editTextIIBFCertificationNumber;
    private TextView txtDateOfPassingLbl;
    private TextView textViewDateOfPassing;
    private TextView txtBCAbilityLbl;
    private CustomSearchableSpinner spinnerBCAbility;
    private TextView txtBCADOBLbl;
    private TextView textViewBCADOB;
    private TextView txtBCAContactAddressLbl;
    private EditText editTextBCAContactAddress;
    private TextView txtBCAMobileNumberLbl;
    private EditText editTextBCAMobileNo;
    private TextView txtCategoryLbl;
    private CustomSearchableSpinner spinnerCategory;
    private TextView txtStateLbl;
    private CustomSearchableSpinner spinnerState;
    private TextView txtDistrictLbl;
    private CustomSearchableSpinner spinnerDistrict;
    private TextView txtTehsilLbl;
    private CustomSearchableSpinner spinnerTehsil;
    private TextView txtVTCLbl;
    private CustomSearchableSpinner spinnerVTC;
    private TextView txtLocalityLbl;
    private EditText editTextLocality;
    private TextView txtPincodeLbl;
    private EditText editTextPincode;
    private LinearLayout layoutSpouseName;
    private TextView txtSpouseNameLbl;
    private Spinner spinnerSpouseSalutation;
    private EditText editTextSpouseFirstName;
    private EditText editTextSpouseMiddleName;
    private EditText editTextSpouseLastName;
    private LinearLayout layoutBCABasicInfo;
    private LinearLayout layoutSameAsFranchisee;
    private CheckBox checkboxSameAsFranchisee;
    private LinearLayout layoutBgMobileNo;
    private LinearLayout layoutBgPincode;

    public BCABasicInformationFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag_bca_basic_info, container, false);

        bindViewId(view);
        //Initialize data
        this.context = getContext();
        logger = Logger.getInstance(context);
        deprecateHandler = new DeprecateHandler(context);
        permissionHandler = new PermissionHandler(getActivity());
        bcaEntryDetailsRepo = new BCAEntryDetailsRepository(context);

        ButterKnife.bind(this, view);

        final Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");

        setCompulsoryMarkLabel();

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

        return view;
    }

    private void bindViewId(View view) {
        txtChoosePhotoLbl = view.findViewById(R.id.txtChoosePhotoLbl);
        imgChoosePhoto = view.findViewById(R.id.imgChoosePhoto);
        txtBCANameLbl = view.findViewById(R.id.txtBCANameLbl);
        spinnerBCASalutation = view.findViewById(R.id.spinnerBCASalutation);
        editTextBCAFirstName = view.findViewById(R.id.editTextBCAFirstName);
        editTextBCAMiddleName = view.findViewById(R.id.editTextBCAMiddleName);
        editTextBCALastName = view.findViewById(R.id.editTextBCALastName);
        txtBCAFatherNameLbl = view.findViewById(R.id.txtBCAFatherNameLbl);
        spinnerBCAFatherSalutation = view.findViewById(R.id.spinnerBCAFatherSalutation);
        editTextBCAFatherFirstName = view.findViewById(R.id.editTextBCAFatherFirstName);
        editTextBCAFatherMiddleName = view.findViewById(R.id.editTextBCAFatherMiddleName);
        editTextBCAFatherLastName = view.findViewById(R.id.editTextBCAFatherLastName);
        txtGenderLbl = view.findViewById(R.id.txtGenderLbl);
        radioGroupGender = view.findViewById(R.id.radioGroupGender);
        radioButtonMale = view.findViewById(R.id.radioButtonMale);
        radioButtonFemale = view.findViewById(R.id.radioButtonFemale);
        txtMaritalStatusLbl = view.findViewById(R.id.txtMaritalStatusLbl);
        radioGroupMaritalStatus = view.findViewById(R.id.radioGroupMaritalStatus);
        radioButtonMarried = view.findViewById(R.id.radioButtonMarried);
        radioButtonSingle = view.findViewById(R.id.radioButtonSingle);
        txtMotherNameLbl = view.findViewById(R.id.txtMotherNameLbl);
        editTextMotherName = view.findViewById(R.id.editTextMotherName);
        txtYearOfPassingLbl = view.findViewById(R.id.txtYearOfPassingLbl);
        textViewYearOfPassing = view.findViewById(R.id.textViewYearOfPassing);
        txtQualificationLbl = view.findViewById(R.id.txtQualificationLbl);
        spinnerQualification = view.findViewById(R.id.spinnerQualification);
        txtReligionLbl = view.findViewById(R.id.txtReligionLbl);
        spinnerReligion = view.findViewById(R.id.spinnerReligion);
        txtIIBFCertificationNumberLbl = view.findViewById(R.id.txtIIBFCertificationNumberLbl);
        editTextIIBFCertificationNumber = view.findViewById(R.id.editTextIIBFCertificationNumber);
        txtDateOfPassingLbl = view.findViewById(R.id.txtDateOfPassingLbl);
        textViewDateOfPassing = view.findViewById(R.id.textViewDateOfPassing);
        txtBCAbilityLbl = view.findViewById(R.id.txtBCAbilityLbl);
        spinnerBCAbility = view.findViewById(R.id.spinnerBCAbility);
        txtBCADOBLbl = view.findViewById(R.id.txtBCADOBLbl);
        textViewBCADOB = view.findViewById(R.id.textViewBCADOB);
        txtBCAContactAddressLbl = view.findViewById(R.id.txtBCAContactAddressLbl);
        editTextBCAContactAddress = view.findViewById(R.id.editTextBCAContactAddress);
        txtBCAMobileNumberLbl = view.findViewById(R.id.txtBCAMobileNumberLbl);
        editTextBCAMobileNo = view.findViewById(R.id.editTextBCAMobileNo);
        txtCategoryLbl = view.findViewById(R.id.txtCategoryLbl);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        txtStateLbl = view.findViewById(R.id.txtStateLbl);
        spinnerState = view.findViewById(R.id.spinnerState);
        txtDistrictLbl = view.findViewById(R.id.txtDistrictLbl);
        spinnerDistrict = view.findViewById(R.id.spinnerDistrict);
        txtTehsilLbl = view.findViewById(R.id.txtTehsilLbl);
        spinnerTehsil = view.findViewById(R.id.spinnerTehsil);
        txtVTCLbl = view.findViewById(R.id.txtVTCLbl);
        spinnerVTC = view.findViewById(R.id.spinnerVTC);
        txtLocalityLbl = view.findViewById(R.id.txtLocalityLbl);
        editTextLocality = view.findViewById(R.id.editTextLocality);
        txtPincodeLbl = view.findViewById(R.id.txtPincodeLbl);
        editTextPincode = view.findViewById(R.id.editTextPincode);
        layoutSpouseName = view.findViewById(R.id.layoutSpouseName);
        txtSpouseNameLbl = view.findViewById(R.id.txtSpouseNameLbl);
        spinnerSpouseSalutation = view.findViewById(R.id.spinnerSpouseSalutation);
        editTextSpouseFirstName = view.findViewById(R.id.editTextSpouseFirstName);
        editTextSpouseMiddleName = view.findViewById(R.id.editTextSpouseMiddleName);
        editTextSpouseLastName = view.findViewById(R.id.editTextSpouseLastName);
        layoutBCABasicInfo = view.findViewById(R.id.layoutBCABasicInfo);
        layoutSameAsFranchisee = view.findViewById(R.id.layoutSameAsFranchisee);
        checkboxSameAsFranchisee = view.findViewById(R.id.checkboxSameAsFranchisee);
        layoutBgMobileNo = view.findViewById(R.id.layoutBgMobileNo);
        layoutBgPincode = view.findViewById(R.id.layoutBgPincode);

        imgChoosePhoto.setOnClickListener(this);
        textViewDateOfPassing.setOnClickListener(this);
        textViewBCADOB.setOnClickListener(this);
        textViewYearOfPassing.setOnClickListener(this);


    }

    public void setCompulsoryMarkLabel() {
        TextView[] txtViewsForCompulsoryMark = {txtChoosePhotoLbl, txtBCANameLbl, txtBCAFatherNameLbl, txtGenderLbl, txtMaritalStatusLbl,
                txtMotherNameLbl, txtYearOfPassingLbl, txtQualificationLbl, txtReligionLbl, txtBCADOBLbl, txtBCAContactAddressLbl, txtBCAMobileNumberLbl,
                txtCategoryLbl, txtStateLbl, txtDistrictLbl, txtTehsilLbl, txtVTCLbl, txtPincodeLbl};
        GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);
    }

    public int IsBasicInformationValidated() {

        //STEP 1: BCA Passport Photo
        if (TextUtils.isEmpty(bcaBasicInformationDto.getProfilePicId()) || bcaBasicInformationDto.getProfilePicId().equalsIgnoreCase("0")) {
            if (TextUtils.isEmpty(bcaBasicInformationDto.getProfilePicBase64())) {
                showMessage("Please add BCA's Passport Size Photograph.");
                return 1;
            }
        }

        //STEP 2.1: BCA Salutation
        if (TextUtils.isEmpty(bcaBasicInformationDto.getBcaSalution())) {
            Toast.makeText(context, "Please select BCA Salutation.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerBCASalutation, "Please select BCA Salutation.", context);
            return 1;
        }

        //STEP 2.2: BCA First Name
        bcaBasicInformationDto.setBcaFirstName(editTextBCAFirstName.getText().toString().trim());
        if (TextUtils.isEmpty(bcaBasicInformationDto.getBcaFirstName())) {
            Toast.makeText(context, "Please enter BCA First Name.", Toast.LENGTH_LONG).show();
            editTextBCAFirstName.setError("Please enter BCA First Name.");
            return 1;
        }

        /*//STEP 2.3: BCA Middle Name
        bcaBasicInformationDto.setBcaMiddleName(editTextBCAMiddleName.getText().toString().trim());
        if (TextUtils.isEmpty(bcaBasicInformationDto.getBcaMiddleName())) {
            Toast.makeText(context, "Please enter BCA Middle Name.", Toast.LENGTH_LONG).show();
            editTextBCAMiddleName.setError("Please enter BCA Middle Name.");
            return 1;
        }*/

        //STEP 2.4: BCA Last Name
        bcaBasicInformationDto.setBcaLastName(editTextBCALastName.getText().toString().trim());
        if (TextUtils.isEmpty(bcaBasicInformationDto.getBcaLastName())) {
            Toast.makeText(context, "Please enter BCA Last Name.", Toast.LENGTH_LONG).show();
            editTextBCALastName.setError("Please enter BCA Last Name.");
            return 1;
        }

        //STEP 3.1: BCA Father's Salutation
        if (TextUtils.isEmpty(bcaBasicInformationDto.getBcaFatherNameSalution())) {
            Toast.makeText(context, "Please select BCA Father's Salutation.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerBCAFatherSalutation, "Please select BCA Father's Salutation.", context);
            return 1;
        }

        //STEP 3.2: BCA Father's First Name
        bcaBasicInformationDto.setBcaFatherFirstName(editTextBCAFatherFirstName.getText().toString().trim());
        if (TextUtils.isEmpty(bcaBasicInformationDto.getBcaFirstName())) {
            Toast.makeText(context, "Please enter BCA Father's First Name.", Toast.LENGTH_LONG).show();
            editTextBCAFatherFirstName.setError("Please enter BCA Father's First Name.");
            return 1;
        }

        /*//STEP 3.3: BCA Father Middle Name
        bcaBasicInformationDto.setBcaFatherMiddleName(editTextBCAFatherMiddleName.getText().toString().trim());
        if (TextUtils.isEmpty(bcaBasicInformationDto.getBcaFatherMiddleName())) {
            Toast.makeText(context, "Please enter BCA Father's Middle Name.", Toast.LENGTH_LONG).show();
            editTextBCAFatherMiddleName.setError("Please enter BCA Father's Middle Name.");
            return 1;
        }*/

        //STEP 3.4: BCA Father Last Name
        bcaBasicInformationDto.setBcaFatherLastName(editTextBCAFatherLastName.getText().toString().trim());
        if (TextUtils.isEmpty(bcaBasicInformationDto.getBcaFatherLastName())) {
            Toast.makeText(context, "Please enter BCA Father's Last Name.", Toast.LENGTH_LONG).show();
            editTextBCAFatherLastName.setError("Please enter BCA Father's Last Name.");
            return 1;
        }

        //STEP 4: Gender
        if (TextUtils.isEmpty(bcaBasicInformationDto.getGender())) {
            showMessage("Please select Gender.");
            return 1;
        }

        //STEP 5: Marital Status
        if (TextUtils.isEmpty(bcaBasicInformationDto.getMaritalStatus())) {
            showMessage("Please select Marital Status.");
            return 1;
        } else if (bcaBasicInformationDto.getMaritalStatus().equalsIgnoreCase("M")) {
            int status = validateSpouseDetails();
            if (status != 0)
                return 1;
        }

        //STEP 6: Mother Name
        bcaBasicInformationDto.setMotherName(editTextMotherName.getText().toString().trim());
        if (TextUtils.isEmpty(bcaBasicInformationDto.getMotherName())) {
            Toast.makeText(context, "Please enter Mother's Name.", Toast.LENGTH_LONG).show();
            editTextMotherName.setError("Please enter BCA Mother's Name.");
            return 1;
        }

        //STEP 7: Mother's Name
        bcaBasicInformationDto.setMotherName(editTextMotherName.getText().toString().trim());
        if (TextUtils.isEmpty(bcaBasicInformationDto.getMotherName())) {
            Toast.makeText(context, "Please enter Mother's Name.", Toast.LENGTH_LONG).show();
            editTextMotherName.setError("Please enter BCA Mother's Name.");
            return 1;
        }

        //STEP 8: Year Of Passing
        if (TextUtils.isEmpty(bcaBasicInformationDto.getYearOfPassing())) {
            Toast.makeText(context, "Please select Year Of Passing.", Toast.LENGTH_LONG).show();
            textViewYearOfPassing.setError("Please select Year Of Passing.");
            return 1;
        }

        //STEP 9: Qualification
        if (TextUtils.isEmpty(bcaBasicInformationDto.getQualification()) || bcaBasicInformationDto.getQualification().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Qualification.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerQualification, "Please select Qualification.", context);
            return 1;
        }

        //STEP 10: Religion
        if (TextUtils.isEmpty(bcaBasicInformationDto.getReligion()) || bcaBasicInformationDto.getReligion().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Religion.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerReligion, "Please select Religion.", context);
            return 1;
        }

        //STEP 11: BCA DOB
        if (TextUtils.isEmpty(bcaBasicInformationDto.getBcaDOB())) {
            Toast.makeText(context, "Please select BCA DOB.", Toast.LENGTH_LONG).show();
            textViewBCADOB.setError("Please select BCA DOB.");
            return 1;
        }

        //STEP 12: BCA Contact Address
        bcaBasicInformationDto.setBcaContactAddress(editTextBCAContactAddress.getText().toString().trim());
        if (TextUtils.isEmpty(bcaBasicInformationDto.getBcaContactAddress())) {
            Toast.makeText(context, "Please enter BCA Contact Address.", Toast.LENGTH_LONG).show();
            editTextBCAContactAddress.setError("Please enter BCA Contact Address.");
            return 1;
        }

        //STEP 13: BCA Mobile Number
        bcaBasicInformationDto.setBcaMobileNumber(editTextBCAMobileNo.getText().toString().trim());
        if (TextUtils.isEmpty(bcaBasicInformationDto.getBcaMobileNumber())) {
            Toast.makeText(context, "Please enter BCA Mobile Number.", Toast.LENGTH_LONG).show();
            editTextBCAMobileNo.setError("Please enter BCA Mobile Number.");
            return 1;
        }

        //STEP 14: Category
        if (TextUtils.isEmpty(bcaBasicInformationDto.getCategory()) || bcaBasicInformationDto.getCategory().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Category.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerCategory, "Please select Category.", context);
            return 1;
        }

        //STEP 15: State
        if (TextUtils.isEmpty(bcaBasicInformationDto.getState()) || bcaBasicInformationDto.getState().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select State.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerState, "Please select State.", context);
            return 1;
        }

        //STEP 16: District
        if (TextUtils.isEmpty(bcaBasicInformationDto.getDistrict()) || bcaBasicInformationDto.getDistrict().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select District.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerDistrict, "Please select District.", context);
            return 1;
        }

        //STEP 17: Block
        if (TextUtils.isEmpty(bcaBasicInformationDto.getBlock()) || bcaBasicInformationDto.getBlock().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Tehsil/Block.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerTehsil, "Please select Tehsil/Block.", context);
            return 1;
        }

        //STEP 18: VTC
        if (TextUtils.isEmpty(bcaBasicInformationDto.getVtc()) || bcaBasicInformationDto.getVtc().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select VTC.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerVTC, "Please select VTC.", context);
            return 1;
        }

        //STEP 19: Pin code
        bcaBasicInformationDto.setPincode(editTextPincode.getText().toString().trim());
        if (TextUtils.isEmpty(bcaBasicInformationDto.getPincode())) {
            Toast.makeText(context, "Please enter Pin code.", Toast.LENGTH_LONG).show();
            editTextPincode.setError("Please enter Pin code.");
            return 1;
        }

        return 0;
    }

    private int validateSpouseDetails() {

        //STEP 1: Spouse Salutation
        if (TextUtils.isEmpty(bcaBasicInformationDto.getBcaSpouseSalution())) {
            Toast.makeText(context, "Please select Spouse Salutation.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerSpouseSalutation, "Please select Spouse Salutation.", context);
            return 1;
        }

        //STEP 2: Spouse First Name
        bcaBasicInformationDto.setBcaSpouseFirstName(editTextSpouseFirstName.getText().toString().trim());
        if (TextUtils.isEmpty(bcaBasicInformationDto.getBcaSpouseFirstName())) {
            Toast.makeText(context, "Please enter Spouse First Name.", Toast.LENGTH_LONG).show();
            editTextSpouseFirstName.setError("Please enter Spouse First Name.");
            return 1;
        }

        //STEP 3: Spouse Middle Name
        bcaBasicInformationDto.setBcaSpouseMiddleName(editTextSpouseMiddleName.getText().toString().trim());
        /*if (TextUtils.isEmpty(franchiseeDetailsDto.getSpouseMiddleName())) {
            editTextSpouseMiddleName.setError("Please enter Spouse Middle Name.");
            return 1;
        }*/

        //STEP 4: Spouse Last Name
        bcaBasicInformationDto.setBcaSpouseLastName(editTextSpouseLastName.getText().toString().trim());
        if (TextUtils.isEmpty(bcaBasicInformationDto.getBcaSpouseLastName())) {
            Toast.makeText(context, "Please enter Spouse Last Name.", Toast.LENGTH_LONG).show();
            editTextSpouseLastName.setError("Please enter Spouse Last Name.");
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
                            bcaBasicInformationDto.setYearOfPassing(strYearOfPassingDate);

                            String DOB = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MM-yyyy", bcaBasicInformationDto.getYearOfPassing());
                            textViewYearOfPassing.setText(DOB);

                        } else if (selectedDateTimeId == R.id.textViewDateOfPassing) {
                            dateOfPassingDate = datetime;
                            strDateOfPassing = formateYMD;
                            bcaBasicInformationDto.setDateOfPassing(strDateOfPassing);

                            String DOB = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MM-yyyy", bcaBasicInformationDto.getDateOfPassing());
                            textViewDateOfPassing.setText(DOB);

                        } else if (selectedDateTimeId == R.id.textViewBCADOB) {
                            dOBDate = datetime;
                            strDOB = formateYMD;
                            bcaBasicInformationDto.setBcaDOB(strDOB);

                            String DateOfIncorporation = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MM-yyyy", bcaBasicInformationDto.getBcaDOB());
                            textViewBCADOB.setText(DateOfIncorporation);
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

        if (selectedDateTimeId == R.id.textViewYearOfPassing) {
            dateTimePickerDialog.setMinDate(c.getTimeInMillis());
            //dateTimePickerDialog.setMaxDate(now + after4days);
            dateTimePickerDialog.setMaxDate(curCal.getTimeInMillis());

        } else if (selectedDateTimeId == R.id.textViewDateOfPassing) {
            dateTimePickerDialog.setMinDate(c.getTimeInMillis());
            //dateTimePickerDialog.setMaxDate(now + after4days);
            dateTimePickerDialog.setMaxDate(curCal.getTimeInMillis());

        } else if (selectedDateTimeId == R.id.textViewBCADOB) {
            dateTimePickerDialog.setMinDate(c.getTimeInMillis());
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
        private String type;

        public GetAllSpinnerData(String stateId, String districtId, String blockId) {
            this.stateId = stateId;
            this.districtId = districtId;
            this.blockId = blockId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressSpinner(context);
        }

        @Override
        protected String doInBackground(String... strings) {

            type = strings[0];

            switch (type.toUpperCase()) {

                case "STATE":

                    //STEP 1: Salutation List
                    salutationList = bcaEntryDetailsRepo.getSalutationList();

                    //STEP 2: Qualification List
                    qualificationList = bcaEntryDetailsRepo.getQualificationList();

                    //STEP 3: Religion List
                    religionList = bcaEntryDetailsRepo.getReligionList();

                    //STEP 4: BC Ability List
                    bcAbilityList = bcaEntryDetailsRepo.getBCAbilityList();

                    //STEP 5: Category List
                    categoryList = bcaEntryDetailsRepo.getCategoryList();

                    //STEP 6: State List
                    stateList = bcaEntryDetailsRepo.getStateList();

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
                    setDistrictSpinnerAdapter(districtList, spinnerDistrict, bcaBasicInformationDto.getDistrict());
                    break;

                case "BLOCK":
                    setBlockSpinnerAdapter(blockList, spinnerTehsil, bcaBasicInformationDto.getBlock());
                    break;

                case "VTC":
                    setVTCSpinnerAdapter(vtcList, spinnerVTC, bcaBasicInformationDto.getVtc());
                    break;

                default:
                    break;
            }

        }
    }

    public BCABasicInformationDto getBCABasicInformationDto() {
        bcaBasicInformationDto.setBcaFirstName(editTextBCAFirstName.getText().toString());
        bcaBasicInformationDto.setBcaMiddleName(editTextBCAMiddleName.getText().toString());
        bcaBasicInformationDto.setBcaLastName(editTextBCALastName.getText().toString());
        bcaBasicInformationDto.setBcaFatherFirstName(editTextBCAFatherFirstName.getText().toString());
        bcaBasicInformationDto.setBcaFatherMiddleName(editTextBCAFatherMiddleName.getText().toString());
        bcaBasicInformationDto.setBcaFatherLastName(editTextBCAFatherLastName.getText().toString());
        bcaBasicInformationDto.setBcaSpouseFirstName(editTextSpouseFirstName.getText().toString());
        bcaBasicInformationDto.setBcaSpouseMiddleName(editTextSpouseMiddleName.getText().toString());
        bcaBasicInformationDto.setBcaSpouseLastName(editTextSpouseLastName.getText().toString());
        bcaBasicInformationDto.setMotherName(editTextMotherName.getText().toString());
        bcaBasicInformationDto.setiIBFCertificateNumber(editTextIIBFCertificationNumber.getText().toString());
        bcaBasicInformationDto.setBcaContactAddress(editTextBCAContactAddress.getText().toString());
        bcaBasicInformationDto.setBcaMobileNumber(editTextBCAMobileNo.getText().toString());
        bcaBasicInformationDto.setLocality(editTextLocality.getText().toString());
        bcaBasicInformationDto.setPincode(editTextPincode.getText().toString());

        String status = checkboxSameAsFranchisee.isChecked() ? "1" : "0";
        ((BCADetailEntryStartUpFragment) getParentFragment()).bcaEntryDetailsDto.setSameAsFranchisee(status);

        return bcaBasicInformationDto;
    }

    public void reloadData(String data, boolean IsEditable) {
        //Reload Data
        if (TextUtils.isEmpty(data))
            bcaBasicInformationDto = new BCABasicInformationDto();
        else {
            try {
                Gson gson = new GsonBuilder().create();
                bcaBasicInformationDto = gson.fromJson(data, BCABasicInformationDto.class);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.IsEditable = IsEditable;
        String isShown = ((BCADetailEntryStartUpFragment) getParentFragment()).bcaEntryDetailsDto.getIsSameAsFranchiseeVisible();
        boolean IsSameAsFranchiseeVisible = (!TextUtils.isEmpty(isShown) && isShown.equalsIgnoreCase("1")) ? true : false;

        getAllSpinnerData = new GetAllSpinnerData(null, null, null);
        getAllSpinnerData.execute("STATE");
    }

    private void bindSpinner() {

        spinner_focusablemode(spinnerQualification);
        spinner_focusablemode(spinnerReligion);
        spinner_focusablemode(spinnerBCAbility);
        spinner_focusablemode(spinnerCategory);

        spinner_focusablemode(spinnerState);
        spinner_focusablemode(spinnerDistrict);
        spinner_focusablemode(spinnerTehsil);
        spinner_focusablemode(spinnerVTC);

        //STEP 0: BCA is same as Franchisee
        checkboxSameAsFranchisee.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean IsChecked) {
                String status = IsChecked ? "1" : "0";
                ((BCADetailEntryStartUpFragment) getParentFragment()).updateIsFranchiseeDetail(status);

            }
        });
        String isSameFranchisee = ((BCADetailEntryStartUpFragment) getParentFragment()).bcaEntryDetailsDto.getSameAsFranchisee();
        if (!TextUtils.isEmpty(isSameFranchisee)) {
            int type = Integer.parseInt(isSameFranchisee);
            if (type == 1) {
                checkboxSameAsFranchisee.setChecked(true);
            } else {
                checkboxSameAsFranchisee.setChecked(false);
            }
        }

        //STEP 1: BCA's Passport Size Photograph
        if (!TextUtils.isEmpty(bcaBasicInformationDto.getProfilePicId())) {
            String gstUrl = Constants.DownloadImageUrl + bcaBasicInformationDto.getProfilePicId();
            Glide.with(context)
                    .load(gstUrl)
                    .apply(new RequestOptions()
                            .error(R.drawable.ic_camera_alt_black_72dp)
                            .placeholder(R.drawable.ic_camera_alt_black_72dp)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgChoosePhoto);
        }

        //STEP 2.1: BCA Salutation
        spinnerBCASalutation.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, salutationList));
        int sPos = bcaEntryDetailsRepo.getSelectedPos(salutationList, bcaBasicInformationDto.getBcaSalution());
        spinnerBCASalutation.setSelection(sPos);
        spinnerBCASalutation.setOnItemSelectedListener(this);

        //STEP 2.2: BCA First Name
        editTextBCAFirstName.setText(bcaBasicInformationDto.getBcaFirstName());

        //STEP 2.3: BCA Middle Name
        editTextBCAMiddleName.setText(bcaBasicInformationDto.getBcaMiddleName());

        //STEP 2.4: BCA Last Name
        editTextBCALastName.setText(bcaBasicInformationDto.getBcaLastName());

        //STEP 3.1: BCA Father Salutation
        spinnerBCAFatherSalutation.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, salutationList));
        int sFPos = bcaEntryDetailsRepo.getSelectedPos(salutationList, bcaBasicInformationDto.getBcaFatherNameSalution());
        spinnerBCAFatherSalutation.setSelection(sFPos);
        spinnerBCAFatherSalutation.setOnItemSelectedListener(this);

        //STEP 3.2: BCA Father First Name
        editTextBCAFatherFirstName.setText(bcaBasicInformationDto.getBcaFatherFirstName());

        //STEP 3.3: BCA Father Middle Name
        editTextBCAFatherMiddleName.setText(bcaBasicInformationDto.getBcaFatherMiddleName());

        //STEP 3.4: BCA Father Last Name
        editTextBCAFatherLastName.setText(bcaBasicInformationDto.getBcaFatherLastName());

        //STEP 4: Gender
        radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonMale) {
                    bcaBasicInformationDto.setGender("M");
                } else if (checkedId == R.id.radioButtonFemale) {
                    bcaBasicInformationDto.setGender("F");
                } else if (checkedId == R.id.radioButtonOthers) {
                    bcaBasicInformationDto.setGender("O");
                }
            }
        });

        if (!TextUtils.isEmpty(bcaBasicInformationDto.getGender())) {
            switch (bcaBasicInformationDto.getGender().toUpperCase()) {
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

        //STEP 5: Marital Status
        radioGroupMaritalStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonMarried) {
                    bcaBasicInformationDto.setMaritalStatus("M");
                    layoutSpouseName.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.radioButtonSingle) {
                    bcaBasicInformationDto.setMaritalStatus("U");
                    layoutSpouseName.setVisibility(View.GONE);
                }
            }
        });

        if (!TextUtils.isEmpty(bcaBasicInformationDto.getMaritalStatus())) {
            switch (bcaBasicInformationDto.getMaritalStatus().toUpperCase()) {
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

        //STEP 5.1: BCA Spouse Salutation
        spinnerSpouseSalutation.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, salutationList));
        int spousePos = bcaEntryDetailsRepo.getSelectedPos(salutationList, bcaBasicInformationDto.getBcaSpouseSalution());
        spinnerSpouseSalutation.setSelection(spousePos);
        spinnerSpouseSalutation.setOnItemSelectedListener(this);

        //STEP 5.2: Spouse First Name
        editTextSpouseFirstName.setText(bcaBasicInformationDto.getBcaSpouseFirstName());

        //STEP 5.3: Spouse Middle Name
        editTextSpouseMiddleName.setText(bcaBasicInformationDto.getBcaSpouseMiddleName());

        //STEP 5.4: Spouse Last Name
        editTextSpouseLastName.setText(bcaBasicInformationDto.getBcaSpouseLastName());

        //STEP 6: Mother's Name
        editTextMotherName.setText(bcaBasicInformationDto.getMotherName());

        //STEP 7: Year Of Passing
        if (!TextUtils.isEmpty(bcaBasicInformationDto.getYearOfPassing())) {
            textViewYearOfPassing.setText(bcaBasicInformationDto.getYearOfPassing());
        }

        //STEP 8: Qualification
        spinnerQualification.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, qualificationList));
        int quaPos = bcaEntryDetailsRepo.getSelectedPos(qualificationList, bcaBasicInformationDto.getQualification());
        spinnerQualification.setSelection(quaPos);
        spinnerQualification.setOnItemSelectedListener(this);

        //STEP 9: Religion
        spinnerReligion.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, religionList));
        int relPos = bcaEntryDetailsRepo.getSelectedPos(religionList, bcaBasicInformationDto.getReligion());
        spinnerReligion.setSelection(relPos);
        spinnerReligion.setOnItemSelectedListener(this);

        //STEP 10: IIBF Certification Number
        editTextIIBFCertificationNumber.setText(bcaBasicInformationDto.getiIBFCertificateNumber());

        //STEP 11: Date Of Passing
        if (!TextUtils.isEmpty(bcaBasicInformationDto.getDateOfPassing())) {
            String DOP = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", bcaBasicInformationDto.getDateOfPassing());
            textViewDateOfPassing.setText(DOP);
        }

        //STEP 12: BC Ability
        spinnerBCAbility.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, bcAbilityList));
        int bcPos = bcaEntryDetailsRepo.getSelectedPos(bcAbilityList, bcaBasicInformationDto.getBcAbility());
        spinnerBCAbility.setSelection(bcPos);
        spinnerBCAbility.setOnItemSelectedListener(this);

        //STEP 13: Date Of Birth
        if (!TextUtils.isEmpty(bcaBasicInformationDto.getBcaDOB())) {
            String DOB = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", bcaBasicInformationDto.getBcaDOB());
            textViewBCADOB.setText(DOB);
        }

        //STEP 14: BCA Contact Address
        editTextBCAContactAddress.setText(bcaBasicInformationDto.getBcaContactAddress());

        //STEP 15: BCA Mobile Number
        editTextBCAMobileNo.setText(bcaBasicInformationDto.getBcaMobileNumber());

        //STEP 16: Category
        spinnerCategory.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, categoryList));
        int catPos = bcaEntryDetailsRepo.getSelectedPos(categoryList, bcaBasicInformationDto.getCategory());
        spinnerCategory.setSelection(catPos);
        spinnerCategory.setOnItemSelectedListener(this);

        //STEP 17: State
        setStateSpinnerAdapter(stateList, spinnerState, bcaBasicInformationDto.getState());

        //STEP 18: Locality
        editTextLocality.setText(bcaBasicInformationDto.getLocality());

        //STEP 19: Pin Code
        editTextPincode.setText(bcaBasicInformationDto.getPincode());

        //Enable/disable views
        GUIUtils.setViewAndChildrenEnabled(layoutBCABasicInfo, IsEditable);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getAllSpinnerData != null && !getAllSpinnerData.isCancelled()) {
            getAllSpinnerData.cancel(true);
        }
    }

    private void spinner_focusablemode(CustomSearchableSpinner  spinner) {
        /*spinner.setFocusable(true);
        spinner.setFocusableInTouchMode(true);*/
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int Id = parent.getId();

        if (Id == R.id.spinnerBCASalutation) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto dto = (CustomFranchiseeApplicationSpinnerDto) spinnerBCASalutation.getItemAtPosition(position);
                bcaBasicInformationDto.setBcaSalution(dto.getId());
            }
        } else if (Id == R.id.spinnerBCAFatherSalutation) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto dto = (CustomFranchiseeApplicationSpinnerDto) spinnerBCAFatherSalutation.getItemAtPosition(position);
                bcaBasicInformationDto.setBcaFatherNameSalution(dto.getId());
            }
        } else if (Id == R.id.spinnerSpouseSalutation) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto dto = (CustomFranchiseeApplicationSpinnerDto) spinnerSpouseSalutation.getItemAtPosition(position);
                bcaBasicInformationDto.setBcaSpouseSalution(dto.getId());
            }
        } else if (Id == R.id.spinnerState) {
            if (position > 0) {
                spinnerState.setTitle("Select State");
                spinnerState.requestFocus();
                CustomFranchiseeApplicationSpinnerDto stateDto = (CustomFranchiseeApplicationSpinnerDto) spinnerState.getItemAtPosition(position);
                if (!stateDto.getId().equals("0")) {
                    bcaBasicInformationDto.setState(stateDto.getId());

                    //Get District
                    getAllSpinnerData = new GetAllSpinnerData(bcaBasicInformationDto.getState(), null, null);
                    getAllSpinnerData.execute("DISTRICT");
                }
            } else {
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

                //DISTRICT
                spinnerDistrict.setEnabled(IsEditable);
                setDistrictSpinnerAdapter(list1, spinnerDistrict, null);
                bcaBasicInformationDto.setDistrict(null);

                //BLOCK
                spinnerTehsil.setEnabled(IsEditable);
                setBlockSpinnerAdapter(list1, spinnerTehsil, null);
                bcaBasicInformationDto.setBlock(null);

                //VTC
                spinnerVTC.setEnabled(IsEditable);
                setVTCSpinnerAdapter(list1, spinnerVTC, null);
                bcaBasicInformationDto.setVtc(null);
            }
        } else if (Id == R.id.spinnerDistrict) {
            if (position > 0) {
                spinnerDistrict.setTitle("Select District");
                spinnerDistrict.requestFocus();
                CustomFranchiseeApplicationSpinnerDto disDto = (CustomFranchiseeApplicationSpinnerDto) spinnerDistrict.getItemAtPosition(position);
                if (!disDto.getId().equals("0")) {
                    bcaBasicInformationDto.setDistrict(disDto.getId());

                    //Get BLOCK
                    getAllSpinnerData = new GetAllSpinnerData(bcaBasicInformationDto.getState(), bcaBasicInformationDto.getDistrict(), null);
                    getAllSpinnerData.execute("BLOCK");

                }
            } else {
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

                //BLOCK
                spinnerTehsil.setEnabled(IsEditable);
                setBlockSpinnerAdapter(list1, spinnerTehsil, null);
                bcaBasicInformationDto.setBlock(null);

                //VTC
                spinnerVTC.setEnabled(IsEditable);
                setVTCSpinnerAdapter(list1, spinnerVTC, null);
                bcaBasicInformationDto.setVtc(null);
            }
        } else if (Id == R.id.spinnerTehsil) {
            if (position > 0) {
                spinnerTehsil.setTitle("Select Block");
                spinnerTehsil.requestFocus();
                CustomFranchiseeApplicationSpinnerDto blockDto = (CustomFranchiseeApplicationSpinnerDto) spinnerTehsil.getItemAtPosition(position);
                if (!blockDto.getId().equals("0")) {
                    bcaBasicInformationDto.setBlock(blockDto.getId());

                    //Get VTC
                    getAllSpinnerData = new GetAllSpinnerData(bcaBasicInformationDto.getState(), bcaBasicInformationDto.getDistrict(), bcaBasicInformationDto.getBlock());
                    getAllSpinnerData.execute("VTC");

                }
            } else {
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

                //VTC
                spinnerVTC.setEnabled(IsEditable);
                setVTCSpinnerAdapter(list1, spinnerVTC, null);
                bcaBasicInformationDto.setVtc(null);
            }
        } else if (Id == R.id.spinnerVTC) {
            spinnerVTC.setTitle("Select VTC");
            spinnerVTC.requestFocus();
            CustomFranchiseeApplicationSpinnerDto vtcDto = (CustomFranchiseeApplicationSpinnerDto) spinnerVTC.getItemAtPosition(position);
            bcaBasicInformationDto.setVtc(vtcDto.getId());

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //region Set State Adapter
    private void setStateSpinnerAdapter
    (List<CustomFranchiseeApplicationSpinnerDto> stateList, Spinner spinnerState, String
            selStateId) {

        spinnerState.setEnabled(IsEditable);
        spinnerState.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, stateList));
        int appsstatePos = bcaEntryDetailsRepo.getSelectedPos(stateList, selStateId);
        spinnerState.setSelection(appsstatePos);
        spinnerState.setOnItemSelectedListener(this);

    }
    //endregion

    //region Set District Adapter
    private void setDistrictSpinnerAdapter
    (List<CustomFranchiseeApplicationSpinnerDto> districtList, Spinner
            spinnerDistrict, String selDistrictId) {

        spinnerDistrict.setEnabled(IsEditable);
        spinnerDistrict.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, districtList));
        int appsstatePos = bcaEntryDetailsRepo.getSelectedPos(districtList, selDistrictId);
        spinnerDistrict.setSelection(appsstatePos);
        spinnerDistrict.setOnItemSelectedListener(this);

    }
    //endregion

    //region Set Block Adapter
    private void setBlockSpinnerAdapter
    (List<CustomFranchiseeApplicationSpinnerDto> blockList, Spinner spinner, String
            selBlockId) {

        spinner.setEnabled(IsEditable);
        spinner.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, blockList));
        int blockPos = bcaEntryDetailsRepo.getSelectedPos(blockList, selBlockId);
        spinner.setSelection(blockPos);
        spinner.setOnItemSelectedListener(this);

    }
    //endregion

    //region Set VTC Adapter
    private void setVTCSpinnerAdapter
    (List<CustomFranchiseeApplicationSpinnerDto> VTCList, Spinner spinnerVTC, String selVTCId) {

        spinnerVTC.setEnabled(IsEditable);
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
    public void onClick(View view) {
        int Id = view.getId();

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

            case BCA_PASSPORT_PHOTO:
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

                bcaBasicInformationDto.setProfilePicBase64(base64);
                bcaBasicInformationDto.setProfileExt(ext);
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
                //2019-04-16
                getDateConvert = year + "-" + monthConverted + "-" + dayOfMonth;
                System.out.println("get Date Convert " + getDateConvert);
                //textViewYearOfPassing.setText(monthString + " " + year);
                textViewYearOfPassing.setText(year);
            }
        });
        monthYearPickerDialog.show(getFragmentManager(), "MonthYearPickerDialog");
        monthYearPickerDialog.setMonthVisiblity(false);

    }

    public void updateBasicKYCDetails(boolean IsChecked, KYCDetailsDto kycDetailsDto) {

        if (IsChecked) {

            bcaBasicInformationDto.setProfilePicId(kycDetailsDto.getProfilePicId());
            bcaBasicInformationDto.setBcaSalution(kycDetailsDto.getBcaSalution());
            bcaBasicInformationDto.setBcaFirstName(kycDetailsDto.getBcaFirstName());
            bcaBasicInformationDto.setBcaMiddleName(kycDetailsDto.getBcaMiddleName());
            bcaBasicInformationDto.setBcaLastName(kycDetailsDto.getBcaLastName());
            bcaBasicInformationDto.setBcaFatherNameSalution(kycDetailsDto.getBcaFatherNameSalution());
            bcaBasicInformationDto.setBcaFatherFirstName(kycDetailsDto.getBcaFatherFirstName());
            bcaBasicInformationDto.setBcaFatherMiddleName(kycDetailsDto.getBcaFatherMiddleName());
            bcaBasicInformationDto.setBcaFatherLastName(kycDetailsDto.getBcaFatherLastName());
            bcaBasicInformationDto.setGender(kycDetailsDto.getGender());
            bcaBasicInformationDto.setMaritalStatus(kycDetailsDto.getMaritalStatus());
            bcaBasicInformationDto.setBcaDOB(kycDetailsDto.getBcaDOB());
            bcaBasicInformationDto.setBcaContactAddress(kycDetailsDto.getBcaContactAddress());
            bcaBasicInformationDto.setBcaMobileNumber(kycDetailsDto.getBcaMobileNumber());
            bcaBasicInformationDto.setState(kycDetailsDto.getState());
            bcaBasicInformationDto.setDistrict(kycDetailsDto.getDistrict());
            bcaBasicInformationDto.setBlock(kycDetailsDto.getBlock());
            bcaBasicInformationDto.setVtc(kycDetailsDto.getVtc());
            bcaBasicInformationDto.setPincode(kycDetailsDto.getPincode());

        } else {
            bcaBasicInformationDto.setProfilePicId(null);
            bcaBasicInformationDto.setProfilePicId(null);
            bcaBasicInformationDto.setBcaSalution(null);
            bcaBasicInformationDto.setBcaFirstName(null);
            bcaBasicInformationDto.setBcaMiddleName(null);
            bcaBasicInformationDto.setBcaLastName(null);
            bcaBasicInformationDto.setBcaFatherNameSalution(null);
            bcaBasicInformationDto.setBcaFatherFirstName(null);
            bcaBasicInformationDto.setBcaFatherMiddleName(null);
            bcaBasicInformationDto.setBcaFatherLastName(null);
            bcaBasicInformationDto.setGender(null);
            bcaBasicInformationDto.setMaritalStatus(null);
            bcaBasicInformationDto.setBcaDOB(null);
            bcaBasicInformationDto.setBcaContactAddress(null);
            bcaBasicInformationDto.setBcaMobileNumber(null);
            bcaBasicInformationDto.setState(null);
            bcaBasicInformationDto.setDistrict(null);
            bcaBasicInformationDto.setBlock(null);
            bcaBasicInformationDto.setVtc(null);
            bcaBasicInformationDto.setPincode(null);

        }

        //STEP 1: BCA's Passport Size Photograph
        if (!TextUtils.isEmpty(bcaBasicInformationDto.getProfilePicId())) {
            String gstUrl = Constants.DownloadImageUrl + bcaBasicInformationDto.getProfilePicId();
            Glide.with(context)
                    .load(gstUrl)
                    .apply(new RequestOptions()
                            .error(R.drawable.ic_camera_alt_black_72dp)
                            .placeholder(R.drawable.ic_camera_alt_black_72dp)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgChoosePhoto);
        }

        //STEP 2.1: BCA Salutation
        int sPos = bcaEntryDetailsRepo.getSelectedPos(salutationList, bcaBasicInformationDto.getBcaSalution());
        spinnerBCASalutation.setSelection(sPos);

        //STEP 2.2: BCA First Name
        editTextBCAFirstName.setText(bcaBasicInformationDto.getBcaFirstName());

        //STEP 2.3: BCA Middle Name
        editTextBCAMiddleName.setText(bcaBasicInformationDto.getBcaMiddleName());

        //STEP 2.4: BCA Last Name
        editTextBCALastName.setText(bcaBasicInformationDto.getBcaLastName());

        //STEP 3.1: BCA Father Salutation
        int sFPos = bcaEntryDetailsRepo.getSelectedPos(salutationList, bcaBasicInformationDto.getBcaFatherNameSalution());
        spinnerBCAFatherSalutation.setSelection(sFPos);

        //STEP 3.2: BCA Father First Name
        editTextBCAFatherFirstName.setText(bcaBasicInformationDto.getBcaFatherFirstName());

        //STEP 3.3: BCA Father Middle Name
        editTextBCAFatherMiddleName.setText(bcaBasicInformationDto.getBcaFatherMiddleName());

        //STEP 3.4: BCA Father Last Name
        editTextBCAFatherLastName.setText(bcaBasicInformationDto.getBcaFatherLastName());

        //STEP 4: Gender
        if (!TextUtils.isEmpty(bcaBasicInformationDto.getGender())) {
            switch (kycDetailsDto.getGender().toUpperCase()) {
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

        //STEP 5: Marital Status
        if (!TextUtils.isEmpty(bcaBasicInformationDto.getMaritalStatus())) {
            switch (kycDetailsDto.getMaritalStatus().toUpperCase()) {
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

        //STEP 6: Date Of Birth
        if (!TextUtils.isEmpty(bcaBasicInformationDto.getBcaDOB())) {
            String DOB = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", kycDetailsDto.getBcaDOB());
            textViewBCADOB.setText(DOB);
        }

        //STEP 7: BCA Contact Address
        editTextBCAContactAddress.setText(bcaBasicInformationDto.getBcaContactAddress());

        //STEP 8: BCA Mobile Number
        editTextBCAMobileNo.setText(bcaBasicInformationDto.getBcaMobileNumber());

        //STEP 9: State
        setStateSpinnerAdapter(stateList, spinnerState, bcaBasicInformationDto.getState());

        //STEP 10: District
        bcaBasicInformationDto.setDistrict(bcaBasicInformationDto.getDistrict());

        //STEP 11: Block
        bcaBasicInformationDto.setBlock(bcaBasicInformationDto.getBlock());

        //STEP 12: VTC
        bcaBasicInformationDto.setVtc(bcaBasicInformationDto.getVtc());

        //STEP 13: Pin Code
        editTextPincode.setText(bcaBasicInformationDto.getPincode());

        //Disable/Enable Views
        kycDisableEnableBasicView(IsChecked);


    }

    private void kycDisableEnableBasicView(boolean IsDisable) {

        Drawable drawable = IsDisable ? deprecateHandler.getDrawable(R.drawable.disable_edittext_bg) : deprecateHandler.getDrawable(R.drawable.edittext_bottom_bg);
        Drawable spinnerDrawable = IsDisable ? deprecateHandler.getDrawable(R.drawable.disable_gradient_white_spinner) : deprecateHandler.getDrawable(R.drawable.gradient_white_spinner);
        boolean IsEnabled = IsDisable ? false : true;

        spinnerBCASalutation.setBackground(spinnerDrawable);
        editTextBCAFirstName.setBackground(drawable);
        editTextBCAMiddleName.setBackground(drawable);
        editTextBCALastName.setBackground(drawable);
        spinnerBCAFatherSalutation.setBackground(spinnerDrawable);
        editTextBCAFatherFirstName.setBackground(drawable);
        editTextBCAFatherMiddleName.setBackground(drawable);
        editTextBCAFatherLastName.setBackground(drawable);
        textViewBCADOB.setBackground(drawable);
        editTextBCAContactAddress.setBackground(drawable);
        layoutBgMobileNo.setBackground(drawable);
        layoutBgPincode.setBackground(drawable);
        spinnerState.setBackground(spinnerDrawable);
        spinnerDistrict.setBackground(spinnerDrawable);
        spinnerTehsil.setBackground(spinnerDrawable);
        spinnerVTC.setBackground(spinnerDrawable);

        imgChoosePhoto.setEnabled(IsEnabled);
        spinnerBCASalutation.setEnabled(IsEnabled);
        editTextBCAFirstName.setEnabled(IsEnabled);
        editTextBCAMiddleName.setEnabled(IsEnabled);
        editTextBCALastName.setEnabled(IsEnabled);
        spinnerBCAFatherSalutation.setEnabled(IsEnabled);
        editTextBCAFatherFirstName.setEnabled(IsEnabled);
        editTextBCAFatherMiddleName.setEnabled(IsEnabled);
        editTextBCAFatherLastName.setEnabled(IsEnabled);
        radioGroupGender.setEnabled(IsEnabled);
        radioGroupMaritalStatus.setEnabled(IsEnabled);
        textViewBCADOB.setEnabled(IsEnabled);
        editTextBCAContactAddress.setEnabled(IsEnabled);
        editTextBCAMobileNo.setEnabled(IsEnabled);
        editTextPincode.setEnabled(IsEnabled);
        spinnerState.setEnabled(IsEnabled);
        spinnerDistrict.setEnabled(IsEnabled);
        spinnerTehsil.setEnabled(IsEnabled);
        spinnerVTC.setEnabled(IsEnabled);

    }
}
