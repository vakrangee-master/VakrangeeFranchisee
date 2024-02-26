package in.vakrangee.franchisee.gwr.gwrwitness_camera;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
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

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nononsenseapps.filepicker.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.vakrangee.franchisee.BuildConfig;
import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.gwr.AsyncGetGWR;
import in.vakrangee.franchisee.gwr.GWRRepository;
import in.vakrangee.franchisee.nextgenfranchiseeapplication.CustomFranchiseeApplicationSpinnerAdapter;
import in.vakrangee.supercore.franchisee.application.VakrangeeKendraApplication;
import in.vakrangee.supercore.franchisee.commongui.FileAttachmentDialog;
import in.vakrangee.supercore.franchisee.commongui.FileUtils;
import in.vakrangee.supercore.franchisee.commongui.FilteredFilePickerActivity;
import in.vakrangee.supercore.franchisee.nextgenfranchiseeapplication.CustomFranchiseeApplicationSpinnerDto;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.FranchiseeApplicationRepository;
import in.vakrangee.supercore.franchisee.utils.GUIUtils;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;
import in.vakrangee.supercore.franchisee.utils.ImageZipper;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;
import in.vakrangee.supercore.franchisee.utils.CustomSearchableSpinner;

@SuppressLint("ValidFragment")
public class GWRWitnessFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "GWRWitnessFragment";
    private View view;
    private Context context;
    private FranchiseeApplicationRepository fapRepo;
    private GWRRepository gwrRepo;
    private DeprecateHandler deprecateHandler;
    private PermissionHandler permissionHandler;

    //region References
    @BindView(R.id.txtWitnessHeader1)
    TextView txtWitnessHeader1;
    @BindView(R.id.txtWitnessHeader2)
    TextView txtWitnessHeader2;
    @BindView(R.id.layout1stWitness)
    LinearLayout layout1stWitness;
    @BindView(R.id.layout2ndWitness)
    LinearLayout layout2ndWitness;

    //---------------------1st witness refrence--------------------

    //applicant information W1
    @BindView(R.id.txtVKIDW1)
    TextView txtVKIDW1;
    @BindView(R.id.spinner1stWitnessSalutationW1)
    Spinner spinner1stWitnessSalutationW1;
    @BindView(R.id.editTextW1FirstName)
    EditText editTextW1FirstName;
    @BindView(R.id.editTextW1MiddleName)
    EditText editTextW1MiddleName;
    @BindView(R.id.editTextW1LastName)
    EditText editTextW1LastName;

    //region contact information W1
    @BindView(R.id.editTextEmailIDW1)
    EditText editTextEmailIDW1;
    @BindView(R.id.editTextMobileNoW1)
    EditText editTextMobileNoW1;
    @BindView(R.id.editTextAlternateMobileNoW1)
    EditText editTextAlternateMobileNoW1;
    @BindView(R.id.editTextLandlineNoW1)
    EditText editTextLandlineNoW1;

    // Occupation - Nationality W1
    @BindView(R.id.spinner_Occupation_W1)
    Spinner spinner_Occupation_W1;
    @BindView(R.id.spinnerNationality_W1)
    CustomSearchableSpinner spinnerNationality_W1;
    @BindView(R.id.editOrganizationNameW1)
    EditText editOrganizationNameW1;

    //Addresss information W1
    @BindView(R.id.editTextAddressLine1W1)
    EditText editTextAddressLine1W1;
    @BindView(R.id.editTextAddressLine2W1)
    EditText editTextAddressLine2W1;
    @BindView(R.id.editTextLandmarkW1)
    EditText editTextLandmarkW1;
    @BindView(R.id.spinnerState_W1)
    CustomSearchableSpinner spinnerState_W1;
    @BindView(R.id.spinnerDistrict_W1)
    CustomSearchableSpinner spinnerDistrict_W1;
    @BindView(R.id.spinnerVTC_W1)
    CustomSearchableSpinner spinnerVTC_W1;
    @BindView(R.id.editTextPincodeW1)
    EditText editTextPincodeW1;

    //upload image W1
    @BindView(R.id.imgBusinessCard_W1)
    ImageView imgBusinessCard_W1;
    @BindView(R.id.imgWitnnessStatementW1)
    ImageView imgWitnnessStatementW1;
    @BindView(R.id.imgPersonalPhoto_W1)
    ImageView imgPersonalPhoto_W1;

    //-----------------------Witness 2----------------

    //applicant information W2
    @BindView(R.id.txtVKIDW2)
    TextView txtVKIDW2;
    @BindView(R.id.spinner1stWitnessSalutationW2)
    Spinner spinner1stWitnessSalutationW2;
    @BindView(R.id.editTextW2FirstName)
    EditText editTextW2FirstName;
    @BindView(R.id.editTextW2MiddleName)
    EditText editTextW2MiddleName;
    @BindView(R.id.editTextW2LastName)
    EditText editTextW2LastName;


    //region contact information W1
    @BindView(R.id.editTextEmailIDW2)
    EditText editTextEmailIDW2;
    @BindView(R.id.editTextMobileNoW2)
    EditText editTextMobileNoW2;
    @BindView(R.id.editTextAlternateMobileNoW2)
    EditText editTextAlternateMobileNoW2;
    @BindView(R.id.editTextLandlineNoW2)
    EditText editTextLandlineNoW2;

    // Occupation - Nationality W1
    @BindView(R.id.editOrganizationNameW2)
    EditText editOrganizationNameW2;
    @BindView(R.id.spinner_Occupation_W2)
    Spinner spinner_Occupation_W2;
    @BindView(R.id.spinnerNationality_W2)
    CustomSearchableSpinner spinnerNationality_W2;


    //Addresss information W1
    @BindView(R.id.editTextAddressLine1W2)
    EditText editTextAddressLine1W2;
    @BindView(R.id.editTextAddressLine2W2)
    EditText editTextAddressLine2W2;
    @BindView(R.id.editTextLandmarkW2)
    EditText editTextLandmarkW2;
    @BindView(R.id.spinnerState_W2)
    CustomSearchableSpinner spinnerState_W2;
    @BindView(R.id.spinnerDistrict_W2)
    CustomSearchableSpinner spinnerDistrict_W2;
    @BindView(R.id.spinnerVTC_W2)
    CustomSearchableSpinner spinnerVTC_W2;
    @BindView(R.id.editTextPincodeW2)
    EditText editTextPincodeW2;

    //upload image W1
    @BindView(R.id.imgBusinessCard_W2)
    ImageView imgBusinessCard_W2;
    @BindView(R.id.imgWitnnessStatementW2)
    ImageView imgWitnnessStatementW2;
    @BindView(R.id.imgPersonalPhoto_W2)
    ImageView imgPersonalPhoto_W2;

    //status remark
    @BindView(R.id.txtWarningIconW1)
    TextView txtWarningIconW1;
    @BindView(R.id.txtWarningIconW2)
    TextView txtWarningIconW2;
    //---- Compulsory red mark 1 witness
    @BindView(R.id.txt1stRefrenceLblW1)
    TextView txt1stRefrenceLblW1;
    @BindView(R.id.txtEmailIdLblW1)
    TextView txtEmailIdLblW1;
    @BindView(R.id.txtMobileNoLblW1)
    TextView txtMobileNoLblW1;
    @BindView(R.id.txtAlternateMobileNoLblW1)
    TextView txtAlternateMobileNoLblW1;
    @BindView(R.id.txtLandlineNoLblW1)
    TextView txtLandlineNoLblW1;
    @BindView(R.id.organization_headerW1)
    TextView organization_headerW1;
    @BindView(R.id.txtCurrent_1stRefrence_OccupationLbl)
    TextView txtCurrent_1stRefrence_OccupationLbl;
    @BindView(R.id.txtNationalityLblW1)
    TextView txtNationalityLblW1;
    @BindView(R.id.txtAddressLine1LblW1)
    TextView txtAddressLine1LblW1;
    @BindView(R.id.txtLandmarkLblW1)
    TextView txtLandmarkLblW1;
    @BindView(R.id.txtStateLblW1)
    TextView txtStateLblW1;
    @BindView(R.id.txtDistrictLblW1)
    TextView txtDistrictLblW1;
    @BindView(R.id.txtVTCLblW1)
    TextView txtVTCLblW1;
    @BindView(R.id.txtPincodeLblW1)
    TextView txtPincodeLblW1;
    @BindView(R.id.txtBusinessCardUploadLblW1)
    TextView txtBusinessCardUploadLblW1;
    @BindView(R.id.txtWitnessStatementChooseFileW1)
    TextView txtWitnessStatementChooseFileW1;
    @BindView(R.id.txtPersonal1W1)
    TextView txtPersonal1W1;

    //---- Compulsory red mark 2nd witness
    @BindView(R.id.txt1stRefrenceLblW2)
    TextView txt1stRefrenceLblW2;
    @BindView(R.id.txtEmailIdLblW2)
    TextView txtEmailIdLblW2;
    @BindView(R.id.txtMobileNoLblW2)
    TextView txtMobileNoLblW2;
    @BindView(R.id.txtAlternateMobileNoLblW2)
    TextView txtAlternateMobileNoLblW2;
    @BindView(R.id.txtLandlineNoLblW2)
    TextView txtLandlineNoLblW2;
    @BindView(R.id.organization_headerW2)
    TextView organization_headerW2;
    @BindView(R.id.txtCurrent_1stRefrence_OccupationLblW2)
    TextView txtCurrent_1stRefrence_OccupationLblW2;
    @BindView(R.id.txtNationalityLblW2)
    TextView txtNationalityLblW2;
    @BindView(R.id.txtAddressLine1LblW2)
    TextView txtAddressLine1LblW2;
    @BindView(R.id.txtLandmarkLblW2)
    TextView txtLandmarkLblW2;
    @BindView(R.id.txtStateLblW2)
    TextView txtStateLblW2;
    @BindView(R.id.txtDistrictLblW2)
    TextView txtDistrictLblW2;
    @BindView(R.id.txtVTCLblW2)
    TextView txtVTCLblW2;
    @BindView(R.id.txtPincodeLblW2)
    TextView txtPincodeLblW2;
    @BindView(R.id.txtBusinnessCardUploadLblW2)
    TextView txtBusinnessCardUploadLblW2;
    @BindView(R.id.txtWitnessStatementChooseFileW2)
    TextView txtWitnessStatementChooseFileW2;
    @BindView(R.id.txtPersonal1W2)
    TextView txtPersonal1W2;
    @BindView(R.id.btnSubmitCamera)
    TextView btnSubmitCamera;
    @BindView(R.id.footer)
    LinearLayout layoutFooter;

    //layout - status
    @BindView(R.id.layoutStatusW1)
    LinearLayout layoutStatusW1;
    @BindView(R.id.layoutStatusW2)
    LinearLayout layoutStatusW2;
    //----- bind data -----------
    private GetAllReferencesSpinnerData getAllReferencesSpinnerData = null;
    private GetAllAddressDetailSpinnerData getAllAddressDetailSpinnerData = null;


    private List<CustomFranchiseeApplicationSpinnerDto> salutationList1st;
    private List<CustomFranchiseeApplicationSpinnerDto> currentOccupationList1st;
    private List<CustomFranchiseeApplicationSpinnerDto> nationalityList;
    private List<CustomFranchiseeApplicationSpinnerDto> stateList;
    private List<CustomFranchiseeApplicationSpinnerDto> districtList;
    private List<CustomFranchiseeApplicationSpinnerDto> VTCList;

    private int FROM = -1;
    private static final int BuinessCard_COPY_W1 = 1;
    private static final int Witness_Statement_W1 = 2;
    private static final int Personal_Image_W1 = 3;
    private static final int BuinessCard_COPY_W2 = 4;
    private static final int Witness_Statement_W2 = 5;
    private static final int Personal_Image_W2 = 6;
    private String SEL_FILE_TYPE;
    private FileAttachmentDialog fileAttachementDialog;
    private Uri picUri;                 //Picture URI
    private static final int CAMERA_PIC_REQUEST = 100;
    private static final int BROWSE_FOLDER_REQUEST = 101;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alert;

    private WitnessListDto witness1ListDto;
    private WitnessListDto witness2ListDto;
    private boolean IsEditable = false;
    private String vkid;
    private InternetConnection internetConnection;
    private AsyncGetGWR asyncGetGWR = null;
    private TextView txtNoDataMsg;
    private LinearLayout layoutProgress;
    private LinearLayout layoutWitness;
    private Connection connection;
    private AsyncSaveWitnessAndCameraInfo asyncSaveWitnessAndCameraInfo;
    private boolean IsAlreadyExecuted = false;


    public GWRWitnessFragment() {
    }

    public GWRWitnessFragment(boolean IsEditable) {
        this.IsEditable = IsEditable;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_gwr_witness, container, false);
        this.context = getContext();
        deprecateHandler = new DeprecateHandler(context);
        permissionHandler = new PermissionHandler(getActivity());
        fapRepo = new FranchiseeApplicationRepository(context);
        gwrRepo = new GWRRepository(context);
        internetConnection = new InternetConnection(context);
        ButterKnife.bind(this, view);
        connection = new Connection(context);

        layoutWitness = view.findViewById(R.id.layoutWitness);
        layoutProgress = (LinearLayout) view.findViewById(R.id.layoutProgress);
        txtNoDataMsg = view.findViewById(R.id.txtNoDataMsg);

        //set Compulsory mark  // remove - txtAlternateMobileNoLblW1 ,txtAlternateMobileNoLblW2
        TextView[] txtViewsForCompulsoryMark = {txt1stRefrenceLblW1, txtMobileNoLblW1,
                organization_headerW1, txtCurrent_1stRefrence_OccupationLbl, txtNationalityLblW1, txtAddressLine1LblW1,
                txtStateLblW1, txtDistrictLblW1, txtVTCLblW1, txtPincodeLblW1,
                txtLandmarkLblW1,

                txt1stRefrenceLblW2, txtMobileNoLblW2,
                organization_headerW2, txtCurrent_1stRefrence_OccupationLblW2, txtNationalityLblW2, txtAddressLine1LblW2,
                txtStateLblW2, txtDistrictLblW2, txtVTCLblW2, txtPincodeLblW2,
                txtLandmarkLblW2

        };
        GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);

        //Input filter
        //1st witness
        CommonUtils.InputFiletrWithMaxLength(editTextW1FirstName, "\"~#^|$%&*!'", 50);
        CommonUtils.InputFiletrWithMaxLength(editTextW1MiddleName, "\"~#^|$%&*!'", 50);
        CommonUtils.InputFiletrWithMaxLength(editTextW1LastName, "\"~#^|$%&*!'", 50);
        CommonUtils.InputFiletrWithMaxLength(editTextEmailIDW1, "\"~#^|$%&*!'", 50);
        CommonUtils.InputFiletrWithMaxLength(editOrganizationNameW1, "\"~#^|$%&*!'", 50);
        CommonUtils.InputFiletrWithMaxLength(editTextAddressLine1W1, "\"~#^|$%&*!'", 35);
        CommonUtils.InputFiletrWithMaxLength(editTextAddressLine2W1, "\"~#^|$%&*!'", 35);
        CommonUtils.InputFiletrWithMaxLength(editTextLandmarkW1, "\"~#^|$%&*!'", 35);
        //2nd witness
        CommonUtils.InputFiletrWithMaxLength(editTextW2FirstName, "\"~#^|$%&*!'", 50);
        CommonUtils.InputFiletrWithMaxLength(editTextW2MiddleName, "\"~#^|$%&*!'", 50);
        CommonUtils.InputFiletrWithMaxLength(editTextW2LastName, "\"~#^|$%&*!'", 50);
        CommonUtils.InputFiletrWithMaxLength(editTextEmailIDW2, "\"~#^|$%&*!'", 50);
        CommonUtils.InputFiletrWithMaxLength(editOrganizationNameW2, "\"~#^|$%&*!'", 50);
        CommonUtils.InputFiletrWithMaxLength(editTextAddressLine1W2, "\"~#^|$%&*!'", 35);
        CommonUtils.InputFiletrWithMaxLength(editTextAddressLine2W2, "\"~#^|$%&*!'", 35);
        CommonUtils.InputFiletrWithMaxLength(editTextLandmarkW2, "\"~#^|$%&*!'", 35);

        return view;
    }

    public void refresh(List<WitnessListDto> witnessListDtos) {

        if (witnessListDtos == null || witnessListDtos.size() == 0) {
            witness1ListDto = new WitnessListDto();
            witness2ListDto = new WitnessListDto();

        } else if (witnessListDtos.size() == 1) {
            witness1ListDto = witnessListDtos.get(0);
            witness2ListDto = new WitnessListDto();

        } else {
            witness1ListDto = witnessListDtos.get(0);
            witness2ListDto = witnessListDtos.get(1);
        }

        //bind
        getAllReferencesSpinnerData = new GetAllReferencesSpinnerData();
        getAllReferencesSpinnerData.execute("");

        getAllAddressDetailSpinnerData = new GetAllAddressDetailSpinnerData(null, null);
        getAllAddressDetailSpinnerData.execute("STATE");

        bindEditext();

    }

    private void bindEditext() {
        //vkid
        if (witness1ListDto.getVkid() != null) {
            txtVKIDW1.setText("VKID :  " + witness1ListDto.getVkid());
        }
        if (witness2ListDto.getVkid() != null) {
            txtVKIDW2.setText("VKID :  " + witness2ListDto.getVkid());
        }
        // first , middle ,last name  Witness 1 and 2
        editTextW1FirstName.setText(TextUtils.isEmpty(witness1ListDto.getWitnessFName()) ? null : witness1ListDto.getWitnessFName());
        editTextW1MiddleName.setText(TextUtils.isEmpty(witness1ListDto.getWitnessMName()) ? null : witness1ListDto.getWitnessMName());
        editTextW1LastName.setText(TextUtils.isEmpty(witness1ListDto.getWitnessLName()) ? null : witness1ListDto.getWitnessLName());

        editTextW2FirstName.setText(TextUtils.isEmpty(witness2ListDto.getWitnessFName()) ? null : witness2ListDto.getWitnessFName());
        editTextW2MiddleName.setText(TextUtils.isEmpty(witness2ListDto.getWitnessMName()) ? null : witness2ListDto.getWitnessMName());
        editTextW2LastName.setText(TextUtils.isEmpty(witness2ListDto.getWitnessLName()) ? null : witness2ListDto.getWitnessLName());

        //email id Witness 1 and 2
        editTextEmailIDW1.setText(TextUtils.isEmpty(witness1ListDto.getWitnessEmailId()) ? null : witness1ListDto.getWitnessEmailId());
        editTextEmailIDW2.setText(TextUtils.isEmpty(witness2ListDto.getWitnessEmailId()) ? null : witness2ListDto.getWitnessEmailId());

        if (witness1ListDto.getWitnessMobileNumber() != null) {
            editTextMobileNoW1.setText(witness1ListDto.getWitnessMobileNumber());
        }
        if (witness2ListDto.getWitnessMobileNumber() != null) {
            editTextMobileNoW2.setText(witness2ListDto.getWitnessMobileNumber());
        }
        if (witness1ListDto.getWitnessAltMobileNumber() != null) {
            editTextAlternateMobileNoW1.setText(witness1ListDto.getWitnessAltMobileNumber());
        }
        if (witness2ListDto.getWitnessAltMobileNumber() != null) {
            editTextAlternateMobileNoW2.setText(witness2ListDto.getWitnessAltMobileNumber());
        }
        if (witness1ListDto.getWitnessLandlineNumber() != null) {
            editTextLandlineNoW1.setText(witness1ListDto.getWitnessLandlineNumber());
        }
        if (witness2ListDto.getWitnessLandlineNumber() != null) {
            editTextLandlineNoW2.setText(witness2ListDto.getWitnessLandlineNumber());
        }
        //organzation name
        editOrganizationNameW1.setText(TextUtils.isEmpty(witness1ListDto.getWitnessOrganizationName()) ? null : witness1ListDto.getWitnessOrganizationName());
        editOrganizationNameW2.setText(TextUtils.isEmpty(witness2ListDto.getWitnessOrganizationName()) ? null : witness2ListDto.getWitnessOrganizationName());

        //Address line 1 and 2  and landmark
        editTextAddressLine1W1.setText(TextUtils.isEmpty(witness1ListDto.getAddressLine1()) ? null : witness1ListDto.getAddressLine1());
        editTextAddressLine2W1.setText(TextUtils.isEmpty(witness1ListDto.getAddressLine2()) ? null : witness1ListDto.getAddressLine2());

        editTextAddressLine1W2.setText(TextUtils.isEmpty(witness2ListDto.getAddressLine1()) ? null : witness2ListDto.getAddressLine1());
        editTextAddressLine2W2.setText(TextUtils.isEmpty(witness2ListDto.getAddressLine2()) ? null : witness2ListDto.getAddressLine2());

        editTextLandmarkW1.setText(TextUtils.isEmpty(witness1ListDto.getLandmark()) ? null : witness1ListDto.getLandmark());
        editTextLandmarkW2.setText(TextUtils.isEmpty(witness2ListDto.getLandmark()) ? null : witness2ListDto.getLandmark());

        editTextPincodeW1.setText(TextUtils.isEmpty(witness1ListDto.getPinCode()) ? null : witness1ListDto.getPinCode());
        editTextPincodeW2.setText(TextUtils.isEmpty(witness2ListDto.getPinCode()) ? null : witness2ListDto.getPinCode());

        // editTextMobileNoW1.setText(TextUtils.isEmpty(String.valueOf(witness1ListDto.getWitnessMobileNumber())) ? "" : String.valueOf(witness1ListDto.getWitnessMobileNumber()));
        //editTextMobileNoW2.setText(TextUtils.isEmpty(String.valueOf(witness2ListDto.getWitnessMobileNumber())) ? "" : String.valueOf(witness2ListDto.getWitnessMobileNumber()));
        //Alternate mobile  number
        // editTextAlternateMobileNoW1.setText(TextUtils.isEmpty(String.valueOf(witness1ListDto.getWitnessAltMobileNumber())) ? null : String.valueOf(witness1ListDto.getWitnessAltMobileNumber()));
        //editTextAlternateMobileNoW2.setText(TextUtils.isEmpty(String.valueOf(witness2ListDto.getWitnessAltMobileNumber())) ? null : String.valueOf(witness2ListDto.getWitnessAltMobileNumber()));
        //Landline number
        //editTextLandlineNoW1.setText(TextUtils.isEmpty(String.valueOf(witness1ListDto.getWitnessLandlineNumber())) ? null : String.valueOf(witness1ListDto.getWitnessLandlineNumber()));
        //editTextLandlineNoW2.setText(TextUtils.isEmpty(String.valueOf(witness2ListDto.getWitnessLandlineNumber())) ? null : String.valueOf(witness2ListDto.getWitnessLandlineNumber()));

        // Business card image - witness 1
        if (witness1ListDto.getWitnessBusinessCardImageId() != null && !witness1ListDto.getWitnessBusinessCardImageId().equalsIgnoreCase("0")) {
            photoDisplayWithExt(imgBusinessCard_W1, String.valueOf(witness1ListDto.getWitnessBusinessCardImageId()), witness1ListDto.getWitnessBusinessCardImageFileExt());
        }
        // statement image - witness 1
     /*   if (witness1ListDto.getWitnessStatementImageId() != null || witness1ListDto.getWitnessStatementImageId() != 0) {
            photoDisplayWithExt(imgWitnnessStatementW1, String.valueOf(witness1ListDto.getWitnessStatementImageId()), witness1ListDto.getWitnessStatementImageFileExt());

        }*/
        // witness pic - witness 1
        if (witness1ListDto.getWitnessPicId() != null && !witness1ListDto.getWitnessPicId().equals("0")) {
            photoDisplayWithExt(imgPersonalPhoto_W1, String.valueOf(witness1ListDto.getWitnessPicId()), witness1ListDto.getWitnessPicFileExt());
        }
        // business image - witness 2
        if (witness2ListDto.getWitnessBusinessCardImageId() != null && !witness2ListDto.getWitnessBusinessCardImageId().equalsIgnoreCase("0")) {
            photoDisplayWithExt(imgBusinessCard_W2, String.valueOf(witness2ListDto.getWitnessBusinessCardImageId()), witness2ListDto.getWitnessBusinessCardImageFileExt());

        }
        // statement image - witness 2
       /* if (witness2ListDto.getWitnessStatementImageId() != null || witness2ListDto.getWitnessStatementImageId() != 0) {
            photoDisplayWithExt(imgWitnnessStatementW2, String.valueOf(witness2ListDto.getWitnessStatementImageId()), witness2ListDto.getWitnessStatementImageFileExt());

        }*/
        // Witness pic  - witness 2
        if (witness2ListDto.getWitnessPicId() != null && !witness2ListDto.getWitnessPicId().equalsIgnoreCase("0")) {
            photoDisplayWithExt(imgPersonalPhoto_W2, String.valueOf(witness2ListDto.getWitnessPicId()), witness2ListDto.getWitnessPicFileExt());
        }

        // status -Witness 1
        if (!TextUtils.isEmpty(witness1ListDto.getStatusWitness())) {
            if (witness1ListDto.getStatusWitness().equalsIgnoreCase("1")) {
                //disable views
                txtWarningIconW1.setVisibility(View.VISIBLE);
                txtWarningIconW1.setText(context.getString(R.string.fa_circle_check));
                txtWarningIconW1.setTypeface(VakrangeeKendraApplication.font_awesome, Typeface.BOLD);
                txtWarningIconW1.setTextColor(deprecateHandler.getColor(R.color.green));
                GUIUtils.setViewAndChildrenEnabled(layout1stWitness, false);
            } else if (witness1ListDto.getStatusWitness().equalsIgnoreCase("2")) {
                //send back for correction
                txtWarningIconW1.setVisibility(View.VISIBLE);
                txtWarningIconW1.setText(context.getString(R.string.fa_circle_cross));
                txtWarningIconW1.setTypeface(VakrangeeKendraApplication.font_awesome, Typeface.BOLD);
                txtWarningIconW1.setTextColor(deprecateHandler.getColor(R.color.ired));
                GUIUtils.setViewAndChildrenEnabled(layout1stWitness, true);
                if (!TextUtils.isEmpty(witness1ListDto.getStatusMsg())) {
                    AlertDialogBoxInfo.alertDialogShow(context, "Send Back for Correction in Witness 1:  " + witness1ListDto.getStatusMsg());
                }
            } else {
                //Enable views
                GUIUtils.setViewAndChildrenEnabled(layout1stWitness, true);
            }
        }

        //status - witness 2
        if (!TextUtils.isEmpty(witness2ListDto.getStatusWitness())) {
            if (witness2ListDto.getStatusWitness().equalsIgnoreCase("1")) {
                //Enable/disable views
                txtWarningIconW2.setVisibility(View.VISIBLE);
                txtWarningIconW2.setText(context.getString(R.string.fa_circle_check));
                txtWarningIconW2.setTypeface(VakrangeeKendraApplication.font_awesome, Typeface.BOLD);
                txtWarningIconW2.setTextColor(deprecateHandler.getColor(R.color.green));
                GUIUtils.setViewAndChildrenEnabled(layout2ndWitness, false);
            } else if (witness2ListDto.getStatusWitness().equalsIgnoreCase("2")) {
                txtWarningIconW2.setVisibility(View.VISIBLE);
                txtWarningIconW2.setText(context.getString(R.string.fa_circle_cross));
                txtWarningIconW2.setTypeface(VakrangeeKendraApplication.font_awesome, Typeface.BOLD);
                txtWarningIconW2.setTextColor(deprecateHandler.getColor(R.color.ired));
                GUIUtils.setViewAndChildrenEnabled(layout2ndWitness, true);
                if (!TextUtils.isEmpty(witness2ListDto.getStatusMsg())) {
                    AlertDialogBoxInfo.alertDialogShow(context, "Send Back for Correction in Witness 2:  " + witness2ListDto.getStatusMsg());
                }
            } else {
                GUIUtils.setViewAndChildrenEnabled(layout2ndWitness, true);
            }
        }
        //witness 1 and witness 2 both - Approved button clickble disable
        if (!TextUtils.isEmpty(witness1ListDto.getStatusWitness()) && !TextUtils.isEmpty(witness2ListDto.getStatusWitness())) {
            if (witness1ListDto.getStatusWitness().equalsIgnoreCase("1") &&
                    witness2ListDto.getStatusWitness().equalsIgnoreCase("1")) {

                GUIUtils.setViewAndChildrenEnabled(layout1stWitness, false);
                GUIUtils.setViewAndChildrenEnabled(layout2ndWitness, false);
                GUIUtils.setViewAndChildrenEnabled(layoutFooter, false);
                layoutFooter.setVisibility(View.GONE);
            }
        }

    }

    private void photoDisplayWithExt(ImageView imageView, String imageid, String imageExt) {
        try {

            if (imageExt.equalsIgnoreCase("PDF")) {

                Glide.with(context)
                        .load(R.drawable.pdf)
                        .apply(new RequestOptions()
                                .error(R.drawable.pdf)
                                .placeholder(R.drawable.pdf)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))
                        .into(imageView);
            } else {
                String picUrl = Constants.DownloadImageUrl + imageid;
                Glide.with(context)
                        .load(picUrl)
                        .apply(new RequestOptions()
                                .error(R.drawable.ic_camera_alt_black_72dp)
                                .placeholder(R.drawable.ic_camera_alt_black_72dp)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))
                        .into(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        }
    }

    private void photoDisplay(ImageView imageView, String imageid) {

        String picUrl = Constants.DownloadImageUrl + imageid;
        Glide.with(context)
                .load(picUrl)
                .apply(new RequestOptions()
                        .error(R.drawable.ic_camera_alt_black_72dp)
                        .placeholder(R.drawable.ic_camera_alt_black_72dp)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true))

                .into(imageView);


    }

    @OnClick({R.id.txtWitnessHeader1, R.id.txtWitnessHeader2, R.id.imgBusinessCard_W1, R.id.imgWitnnessStatementW1,
            R.id.imgPersonalPhoto_W1, R.id.imgBusinessCard_W2, R.id.imgWitnnessStatementW2,
            R.id.imgPersonalPhoto_W2, R.id.btnSubmitCamera})
    @Override
    public void onClick(View view) {
        int Id = view.getId();
        if (Id == R.id.txtWitnessHeader1) {
            txtWitnessHeader1.setTextColor(deprecateHandler.getColor(R.color.gray));
            txtWitnessHeader1.setBackgroundColor(deprecateHandler.getColor(R.color.grey));

            txtWitnessHeader2.setTextColor(deprecateHandler.getColor(R.color.white));
            txtWitnessHeader2.setBackgroundColor(deprecateHandler.getColor(R.color.gray));

            txtWarningIconW1.setBackgroundColor(deprecateHandler.getColor(R.color.grey));
            txtWarningIconW2.setBackgroundColor(deprecateHandler.getColor(R.color.gray));

            layout2ndWitness.setVisibility(View.GONE);
            layout1stWitness.setVisibility(View.VISIBLE);

            layoutStatusW1.setBackgroundColor(deprecateHandler.getColor(R.color.gray));
            layoutStatusW2.setBackgroundColor(deprecateHandler.getColor(R.color.grey));

        } else if (Id == R.id.txtWitnessHeader2) {
            txtWitnessHeader2.setTextColor(deprecateHandler.getColor(R.color.gray));
            txtWitnessHeader2.setBackgroundColor(deprecateHandler.getColor(R.color.grey));

            txtWitnessHeader1.setTextColor(deprecateHandler.getColor(R.color.white));
            txtWitnessHeader1.setBackgroundColor(deprecateHandler.getColor(R.color.gray));

            txtWarningIconW1.setBackgroundColor(deprecateHandler.getColor(R.color.gray));
            txtWarningIconW2.setBackgroundColor(deprecateHandler.getColor(R.color.grey));

            layout2ndWitness.setVisibility(View.VISIBLE);
            layout1stWitness.setVisibility(View.GONE);

            layoutStatusW1.setBackgroundColor(deprecateHandler.getColor(R.color.grey));
            layoutStatusW2.setBackgroundColor(deprecateHandler.getColor(R.color.gray));

        } else if (Id == R.id.imgBusinessCard_W1) {
            FROM = BuinessCard_COPY_W1;
            SEL_FILE_TYPE = "images/pdf";
            showAttachmentDialog(view, SEL_FILE_TYPE);

        } else if (Id == R.id.imgWitnnessStatementW1) {
            FROM = Witness_Statement_W1;
            SEL_FILE_TYPE = "images/pdf";
            showAttachmentDialog(view, SEL_FILE_TYPE);

        } else if (Id == R.id.imgPersonalPhoto_W1) {
            FROM = Personal_Image_W1;
            SEL_FILE_TYPE = "images";
            showAttachmentDialog(view, SEL_FILE_TYPE);

        } else if (Id == R.id.imgBusinessCard_W2) {
            FROM = BuinessCard_COPY_W2;
            SEL_FILE_TYPE = "images/pdf";
            showAttachmentDialog(view, SEL_FILE_TYPE);

        } else if (Id == R.id.imgWitnnessStatementW2) {
            FROM = Witness_Statement_W2;
            SEL_FILE_TYPE = "images/pdf";
            showAttachmentDialog(view, SEL_FILE_TYPE);

        } else if (Id == R.id.imgPersonalPhoto_W2) {
            FROM = Personal_Image_W2;
            SEL_FILE_TYPE = "images";
            showAttachmentDialog(view, SEL_FILE_TYPE);

        } else if (Id == R.id.btnSubmitCamera) {

            int witnessValidation1 = IsWitnessValidated1();
            int witnessValidation2 = IsWitnessValidated2();

            if (witnessValidation1 == 0 && witnessValidation2 == 0) {
                String jsondataforWitness = modelToJsonConvert("all");
                saveWitnessAndCameraAsynTaskCall(jsondataforWitness);
            } else if (witnessValidation1 == 0 && witnessValidation2 != 0) {
                alertDialogWitnessConfirmation(getResources().getString(R.string.witness2), "witness2");
            } else if (witnessValidation2 == 0 && witnessValidation1 != 0) {
                alertDialogWitnessConfirmation(getResources().getString(R.string.witness1), "witness1");
            }
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
                            picUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
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
        if (Id == R.id.spinner1stWitnessSalutationW1) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinner1stWitnessSalutationW1.getItemAtPosition(position);
                witness1ListDto.setWitnessTitle(entityDto.getId());
            }
        } else if (Id == R.id.spinner_Occupation_W1) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinner_Occupation_W1.getItemAtPosition(position);
                witness1ListDto.setWitnessOccupation(entityDto.getId());
            }
        } else if (Id == R.id.spinnerNationality_W1) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerNationality_W1.getItemAtPosition(position);
                witness1ListDto.setWitnessNationality(entityDto.getId());
            }
        } else if (Id == R.id.spinner1stWitnessSalutationW2) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinner1stWitnessSalutationW2.getItemAtPosition(position);
                witness2ListDto.setWitnessTitle(entityDto.getId());
            }
        } else if (Id == R.id.spinner_Occupation_W2) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinner_Occupation_W2.getItemAtPosition(position);
                witness2ListDto.setWitnessOccupation(entityDto.getId());
            }
        } else if (Id == R.id.spinnerNationality_W2) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerNationality_W2.getItemAtPosition(position);
                witness2ListDto.setWitnessNationality(entityDto.getId());
            }
        } else if (Id == R.id.spinnerState_W1) {
            if (position > 0) {
                spinnerState_W1.setTitle("Select State");
                spinnerState_W1.requestFocus();
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerState_W1.getItemAtPosition(position);
                if (!entityDto.getId().equals("0")) {
                    //witness1ListDto.setState(Integer.valueOf(entityDto.getId()));
                    witness1ListDto.setState(entityDto.getId());
                    //Get District
                    getAllAddressDetailSpinnerData = new GetAllAddressDetailSpinnerData(witness1ListDto.getState(), null);
                    getAllAddressDetailSpinnerData.execute("COM_DISTRICT");

                }
            } else {
                witness1ListDto.setState(null);
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));
                spinnerDistrict_W1.setEnabled(true);
                setDistrictSpinnerAdapter(list1, spinnerDistrict_W1, null);
                witness1ListDto.setDistrict(null);
                spinnerVTC_W1.setEnabled(true);
                setDistrictSpinnerAdapter(list1, spinnerVTC_W1, null);
                witness1ListDto.setVtc(null);
            }

        } else if (Id == R.id.spinnerDistrict_W1) {
            if (position > 0) {
                spinnerDistrict_W1.setTitle("Select District");
                spinnerDistrict_W1.requestFocus();
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerDistrict_W1.getItemAtPosition(position);
                if (!entityDto.getId().equals("0")) {
                    witness1ListDto.setDistrict(entityDto.getId());

                    //Get VTC
                    getAllAddressDetailSpinnerData = new GetAllAddressDetailSpinnerData(witness1ListDto.getState(), witness1ListDto.getDistrict());
                    getAllAddressDetailSpinnerData.execute("COM_VTC");

                }
            } else {
                witness1ListDto.setDistrict(null);
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

                spinnerVTC_W1.setEnabled(true);
                setDistrictSpinnerAdapter(list1, spinnerVTC_W1, null);
                witness1ListDto.setVtc(null);

            }
        } else if (Id == R.id.spinnerVTC_W1) {
            spinnerVTC_W1.setTitle("Select District");
            spinnerVTC_W1.requestFocus();
            CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerVTC_W1.getItemAtPosition(position);
            witness1ListDto.setVtc(entityDto.getId());

        } else if (Id == R.id.spinnerState_W2) {
            if (position > 0) {
                spinnerState_W2.setTitle("Select State");
                spinnerState_W2.requestFocus();
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerState_W2.getItemAtPosition(position);
                if (!entityDto.getId().equals("0")) {
                    //witness2ListDto.setState(Integer.valueOf(entityDto.getId()));
                    witness2ListDto.setState(entityDto.getId());
                    //Get District
                    getAllAddressDetailSpinnerData = new GetAllAddressDetailSpinnerData(witness2ListDto.getState(), null);
                    getAllAddressDetailSpinnerData.execute("COM_DISTRICT_W2");

                }
            } else {
                witness2ListDto.setState(null);
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));
                spinnerDistrict_W2.setEnabled(true);
                setDistrictSpinnerAdapter(list1, spinnerDistrict_W2, null);
                witness2ListDto.setDistrict(null);
                spinnerVTC_W2.setEnabled(true);
                setDistrictSpinnerAdapter(list1, spinnerVTC_W2, null);
                witness2ListDto.setVtc(null);
            }

        } else if (Id == R.id.spinnerDistrict_W2) {
            if (position > 0) {
                spinnerDistrict_W2.setTitle("Select District");
                spinnerDistrict_W2.requestFocus();
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerDistrict_W2.getItemAtPosition(position);
                if (!entityDto.getId().equals("0")) {
                    witness2ListDto.setDistrict(entityDto.getId());

                    //Get VTC
                    getAllAddressDetailSpinnerData = new GetAllAddressDetailSpinnerData(witness2ListDto.getState(), witness2ListDto.getDistrict());
                    getAllAddressDetailSpinnerData.execute("COM_VTC_W2");

                }
            } else {
                witness2ListDto.setDistrict(null);
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

                spinnerVTC_W2.setEnabled(true);
                setDistrictSpinnerAdapter(list1, spinnerVTC_W2, null);
                witness2ListDto.setVtc(null);

            }
        } else if (Id == R.id.spinnerVTC_W2) {
            spinnerVTC_W2.setTitle("Select District");
            spinnerVTC_W2.requestFocus();
            CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerVTC_W2.getItemAtPosition(position);
            witness2ListDto.setVtc(entityDto.getId());

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //region IsFranchiseeReferencesValidated

    private void spinner_focusablemode(CustomSearchableSpinner spinner) {
        spinner.setFocusable(true);
        spinner.setFocusableInTouchMode(true);
    }

    class GetAllReferencesSpinnerData extends AsyncTask<String, Void, String> {
        // private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* progress = new ProgressDialog(context);
            progress.setTitle(R.string.pleaseWait);
            progress.setMessage(context.getResources().getString(R.string.pleaseWait));
            progress.setCancelable(false);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();*/
            //showProgressSpinner(context);
        }

        @Override
        protected String doInBackground(String... strings) {

            //STEP 1: 1st Applicant Salutation
            salutationList1st = fapRepo.getSalutationList();
            //STEP 2:1st Current Occupation
            currentOccupationList1st = gwrRepo.getOccupationsList();
            //STEP 3:
            nationalityList = fapRepo.getNationalityList();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //  progress.dismiss();
            //dismissProgressSpinner();
            bindSpinner();
        }
    }

    private void bindSpinner() {
        // spinner_focusablemode(spinnerNationality_Witness1);

        //STEP 1: 1st spinner 1st Witness Salutation W1
        CustomFranchiseeApplicationSpinnerAdapter salutationAdapterW1 = new CustomFranchiseeApplicationSpinnerAdapter(context, salutationList1st);
        spinner1stWitnessSalutationW1.setAdapter(salutationAdapterW1);
        int appSalutationPos = fapRepo.getSelectedPos(salutationList1st, String.valueOf(witness1ListDto.getWitnessTitle()));
        spinner1stWitnessSalutationW1.setSelection(appSalutationPos);
        spinner1stWitnessSalutationW1.setOnItemSelectedListener(this);

        //STEP 2:1st Current Occupation W1
        CustomFranchiseeApplicationSpinnerAdapter CurrentOccupationAdapterW1 = new CustomFranchiseeApplicationSpinnerAdapter(context, currentOccupationList1st);
        spinner_Occupation_W1.setAdapter(CurrentOccupationAdapterW1);
        int appCurrentOccupationPos = fapRepo.getSelectedPos(currentOccupationList1st, String.valueOf(witness1ListDto.getWitnessOccupation()));
        spinner_Occupation_W1.setSelection(appCurrentOccupationPos);
        spinner_Occupation_W1.setOnItemSelectedListener(this);


        //STEP 3: 1st Nationality W1
        spinnerNationality_W1.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, nationalityList));
        int nationalityPos = fapRepo.getSelectedPos(nationalityList, String.valueOf(witness1ListDto.getWitnessNationality()));
        spinnerNationality_W1.setSelection(nationalityPos);
        spinnerNationality_W1.setOnItemSelectedListener(this);

        //STEP 4: 1st spinner 1st Witness Salutation W2
        CustomFranchiseeApplicationSpinnerAdapter salutationAdapterW2 = new CustomFranchiseeApplicationSpinnerAdapter(context, salutationList1st);
        spinner1stWitnessSalutationW2.setAdapter(salutationAdapterW2);
        int appSalutation2Pos = fapRepo.getSelectedPos(salutationList1st, String.valueOf(witness2ListDto.getWitnessTitle()));
        spinner1stWitnessSalutationW2.setSelection(appSalutation2Pos);
        spinner1stWitnessSalutationW2.setOnItemSelectedListener(this);

        //STEP 5:1st Current Occupation W2
        CustomFranchiseeApplicationSpinnerAdapter CurrentOccupationAdapterW2 = new CustomFranchiseeApplicationSpinnerAdapter(context, currentOccupationList1st);
        spinner_Occupation_W2.setAdapter(CurrentOccupationAdapterW2);
        int appCurrentOccupation2Pos = fapRepo.getSelectedPos(currentOccupationList1st, String.valueOf(witness2ListDto.getWitnessOccupation()));
        spinner_Occupation_W2.setSelection(appCurrentOccupation2Pos);
        spinner_Occupation_W2.setOnItemSelectedListener(this);

        //STEP 6: 1st Nationality W2
        spinnerNationality_W2.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, nationalityList));
        int nationalityPosW2 = fapRepo.getSelectedPos(nationalityList, String.valueOf(witness2ListDto.getWitnessNationality()));
        spinnerNationality_W2.setSelection(nationalityPosW2);
        spinnerNationality_W2.setOnItemSelectedListener(this);

    }

    class GetAllAddressDetailSpinnerData extends AsyncTask<String, Void, String> {
        // private ProgressDialog progress;
        private String stateId;
        private String districtId;
        private String type;

        public GetAllAddressDetailSpinnerData(String stateId, String districtId) {
            this.stateId = stateId;
            this.districtId = districtId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* progress = new ProgressDialog(context);
            progress.setTitle(R.string.pleaseWait);
            progress.setMessage(context.getResources().getString(R.string.pleaseWait));
            progress.setCancelable(false);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();*/
        }

        @Override
        protected String doInBackground(String... strings) {
            type = strings[0];

            switch (type.toUpperCase()) {

                case "STATE":
                    //STEP 1 : State
                    stateList = fapRepo.getAllStateBylList();
                    break;
                case "COM_DISTRICT":
                case "COM_DISTRICT_W2":
                    //STEP 2: District List
                    districtList = fapRepo.getAllDistrictBylList(stateId);
                    break;
                case "COM_VTC":
                case "COM_VTC_W2":
                    //STEP 3: VTC List
                    VTCList = fapRepo.getAllVTCBylList(stateId, districtId);
                    break;
                default:
                    break;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //progress.dismiss();
            switch (type.toUpperCase()) {

                case "STATE":
                    BindSpineerforAddress();
                    break;

                case "COM_DISTRICT":
                    setDistrictSpinnerAdapter(districtList, spinnerDistrict_W1, String.valueOf(witness1ListDto.getDistrict()));
                    break;

                case "COM_VTC":
                    setVTCSpinnerAdapter(VTCList, spinnerVTC_W1, String.valueOf(witness1ListDto.getVtc()));
                    break;

                case "COM_DISTRICT_W2":
                    setDistrictSpinnerAdapter(districtList, spinnerDistrict_W2, String.valueOf(witness2ListDto.getDistrict()));
                    break;
                case "COM_VTC_W2":
                    setVTCSpinnerAdapter(VTCList, spinnerVTC_W2, String.valueOf(witness2ListDto.getVtc()));
                    break;
                default:
                    break;
            }

        }
    }

    private void BindSpineerforAddress() {

       /* spinner_focusablemode(spinnerState_W1);
        spinner_focusablemode(spinnerDistrict_W1);
        spinner_focusablemode(spinnerVTC_W1);

        spinner_focusablemode(spinnerState_W2);
        spinner_focusablemode(spinnerDistrict_W2);
        spinner_focusablemode(spinnerVTC_W2);

        spinner_focusablemode(spinnerNationality_W1);
        spinner_focusablemode(spinnerNationality_W2);*/


        spinnerState_W1.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, stateList));
        int appsstatePos = fapRepo.getSelectedPos(stateList, String.valueOf(witness1ListDto.getState()));
        spinnerState_W1.setSelection(appsstatePos);
        spinnerState_W1.setOnItemSelectedListener(this);

        spinnerState_W2.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, stateList));
        int appsstatePosW2 = fapRepo.getSelectedPos(stateList, String.valueOf(witness2ListDto.getState()));
        spinnerState_W2.setSelection(appsstatePosW2);
        spinnerState_W2.setOnItemSelectedListener(this);
    }

    //region Set District Adapter
    private void setDistrictSpinnerAdapter(List<CustomFranchiseeApplicationSpinnerDto> districtList, Spinner spinnerDistrict, String selDistrictId) {

        spinnerDistrict.setEnabled(true);
        spinnerDistrict.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, districtList));
        int appsstatePos = fapRepo.getSelectedPos(districtList, selDistrictId);
        spinnerDistrict.setSelection(appsstatePos);
        spinnerDistrict.setOnItemSelectedListener(this);

    }
    //endregion

    //region Set VTC Adapter
    private void setVTCSpinnerAdapter(List<CustomFranchiseeApplicationSpinnerDto> VTCList, Spinner spinnerVTC, String selVTCId) {

        spinnerVTC.setEnabled(true);
        spinnerVTC.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, VTCList));
        int appsstatePos = fapRepo.getSelectedPos(VTCList, selVTCId);
        spinnerVTC.setSelection(appsstatePos);
        spinnerVTC.setOnItemSelectedListener(this);
    }

    //endregion

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK) {

                Bitmap bitmapNew = ImageUtils.getBitmap(getActivity().getContentResolver(), picUri, "", "", "");
                bitmapNew = ImageUtils.rotateImageIfRequired(getActivity().getContentResolver(), bitmapNew, picUri);
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

    public void showMessage(String msg) {
        if (TextUtils.isEmpty(msg))
            return;

        if (alert == null) {
            alertDialogBuilder = new AlertDialog.Builder(context);

            alertDialogBuilder
                    .setMessage(Html.fromHtml(msg))
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            alert = null;
                        }
                    });
            alert = alertDialogBuilder.create();
            alert.show();
        }
    }

    private void setScanCopyData(boolean IsDrawable, Bitmap bitmap, String fileName, String base64, String ext) {

        switch (FROM) {
            case BuinessCard_COPY_W1:
                if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgBusinessCard_W1);
                else
                    Glide.with(context).asBitmap().load(bitmap).into(imgBusinessCard_W1);

                witness1ListDto.setWitnessBusinessCardImage(base64);
                witness1ListDto.setWitnessBusinessCardImageFileExt(ext);
                // franchiseeDetailsDto.setHighestQualiFileExt(ext);
                // franchiseeDetailsDto.setHighestQualificationName(fileName);
                break;

            case Witness_Statement_W1:
                if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgWitnnessStatementW1);
                else
                    Glide.with(context).asBitmap().load(bitmap).into(imgWitnnessStatementW1);

                witness1ListDto.setWitnessStatementImage(base64);
                witness1ListDto.setWitnessStatementImageFileExt(ext);

             /*   franchiseeDetailsDto.setHighestQualiUploadBase64(base64);
                franchiseeDetailsDto.setHighestQualiFileExt(ext);
                franchiseeDetailsDto.setHighestQualificationName(fileName);*/
                break;
            case Personal_Image_W1:
                if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgPersonalPhoto_W1);
                else
                    Glide.with(context).asBitmap().load(bitmap).into(imgPersonalPhoto_W1);

                witness1ListDto.setWitnessPic(base64);
                witness1ListDto.setWitnessPicFileExt(ext);
             /*   franchiseeDetailsDto.setHighestQualiUploadBase64(base64);
                franchiseeDetailsDto.setHighestQualiFileExt(ext);
                franchiseeDetailsDto.setHighestQualificationName(fileName);*/
                break;
            case BuinessCard_COPY_W2:
                if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgBusinessCard_W2);
                else
                    Glide.with(context).asBitmap().load(bitmap).into(imgBusinessCard_W2);


                witness2ListDto.setWitnessBusinessCardImage(base64);
                witness2ListDto.setWitnessBusinessCardImageFileExt(ext);
             /*   franchiseeDetailsDto.setHighestQualiUploadBase64(base64);
                franchiseeDetailsDto.setHighestQualiFileExt(ext);
                franchiseeDetailsDto.setHighestQualificationName(fileName);*/
                break;
            case Witness_Statement_W2:
                if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgWitnnessStatementW2);
                else
                    Glide.with(context).asBitmap().load(bitmap).into(imgWitnnessStatementW2);

                witness2ListDto.setWitnessStatementImage(base64);
                witness2ListDto.setWitnessStatementImageFileExt(ext);
             /*   franchiseeDetailsDto.setHighestQualiUploadBase64(base64);
                franchiseeDetailsDto.setHighestQualiFileExt(ext);
                franchiseeDetailsDto.setHighestQualificationName(fileName);*/
                break;
            case Personal_Image_W2:
                if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgPersonalPhoto_W2);
                else
                    Glide.with(context).asBitmap().load(bitmap).into(imgPersonalPhoto_W2);

                witness2ListDto.setWitnessPic(base64);
                witness2ListDto.setWitnessPicFileExt(ext);
             /*   franchiseeDetailsDto.setHighestQualiUploadBase64(base64);
                franchiseeDetailsDto.setHighestQualiFileExt(ext);
                franchiseeDetailsDto.setHighestQualificationName(fileName);*/
                break;
            default:
                break;

        }
    }

    //region isvalid for layout
    public int IsValid(String witness) {

        if (witness.equalsIgnoreCase("witness1")) {
            layout1stWitness.setVisibility(View.VISIBLE);
            layout2ndWitness.setVisibility(View.GONE);

            txtWitnessHeader1.setTextColor(deprecateHandler.getColor(R.color.gray));
            txtWitnessHeader1.setBackgroundColor(deprecateHandler.getColor(R.color.grey));
            txtWitnessHeader2.setTextColor(deprecateHandler.getColor(R.color.white));
            txtWitnessHeader2.setBackgroundColor(deprecateHandler.getColor(R.color.gray));

        } else {
            layout1stWitness.setVisibility(View.GONE);
            layout2ndWitness.setVisibility(View.VISIBLE);

            txtWitnessHeader2.setTextColor(deprecateHandler.getColor(R.color.gray));
            txtWitnessHeader2.setBackgroundColor(deprecateHandler.getColor(R.color.grey));
            txtWitnessHeader1.setTextColor(deprecateHandler.getColor(R.color.white));
            txtWitnessHeader1.setBackgroundColor(deprecateHandler.getColor(R.color.gray));
        }
      /*
        if (IsWitnessValidated1() == -1) {
            layout1stWitness.setVisibility(View.VISIBLE);
            layout2ndWitness.setVisibility(View.GONE);
            return -1;
        } else if (IsWitnessValidated2() == -2) {
            layout1stWitness.setVisibility(View.GONE);
            layout2ndWitness.setVisibility(View.VISIBLE);
            return -2;
        }*/
        return 0;
    }

    //region IsWitnessValidated 1
    public int IsWitnessValidated1() {
        //-----------**************Witness 1 validation --------------
        //1- spinner Name -MR-MRS W1
        if (witness1ListDto.getWitnessTitle() == null || witness1ListDto.getWitnessTitle().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select MR-MRS.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinner1stWitnessSalutationW1, "Please select MR-MRS.", context);
            return -1;
        }

        //1- first name W1
        witness1ListDto.setWitnessFName(editTextW1FirstName.getText().toString().trim());
        if (TextUtils.isEmpty(witness1ListDto.getWitnessFName())) {
            Toast.makeText(context, "Please enter First Name of Witness 1.", Toast.LENGTH_LONG).show();
            editTextW1FirstName.setError("Please enter First Name.");
            return -1;
        }
        // middle name W1
        witness1ListDto.setWitnessMName(editTextW1MiddleName.getText().toString().trim());
        //last name W1
        witness1ListDto.setWitnessLName(editTextW1LastName.getText().toString().trim());
        if (TextUtils.isEmpty(witness1ListDto.getWitnessLName())) {
            Toast.makeText(context, "Please enter last Name of Witness 1.", Toast.LENGTH_LONG).show();
            editTextW1LastName.setError("Please enter last Name.");
            return -1;
        }
        //email id
        witness1ListDto.setWitnessEmailId(editTextEmailIDW1.getText().toString().trim());
       /* if (TextUtils.isEmpty(witness1ListDto.getWitnessEmailId()) || !witness1ListDto.getWitnessEmailId().matches(CommonUtils.emailPattern)) {
            Toast.makeText(context, "Please enter Email Id of Witness 1.", Toast.LENGTH_LONG).show();
            editTextEmailIDW1.setError("Please enter Email Id.");
            return -1;
        }*/
        // mobile number W1
        witness1ListDto.setWitnessMobileNumber(editTextMobileNoW1.getText().toString().trim());
        if (TextUtils.isEmpty(witness1ListDto.getWitnessMobileNumber()) || editTextMobileNoW1.getText().toString().trim().length() != 10) {
            Toast.makeText(context, "Please enter Mobile Number of Witness 1.", Toast.LENGTH_LONG).show();
            editTextMobileNoW1.setError("Please enter Mobile Number.");
            return -1;
        }
        witness1ListDto.setWitnessAltMobileNumber(editTextAlternateMobileNoW1.getText().toString().trim());
        witness1ListDto.setWitnessLandlineNumber(editTextLandlineNoW1.getText().toString().trim());
        // OrganizationName
        witness1ListDto.setWitnessOrganizationName(editOrganizationNameW1.getText().toString().trim());
        if (TextUtils.isEmpty(witness1ListDto.getWitnessOrganizationName())) {
            Toast.makeText(context, "Please enter Organization Name of Witness 1.", Toast.LENGTH_LONG).show();
            editOrganizationNameW1.setError("Please enter Organization Name");
            return -1;
        }
        //1-current Occupdation W1
        if (witness1ListDto.getWitnessOccupation() == null || witness1ListDto.getWitnessOccupation().equalsIgnoreCase("-1")) {
            Toast.makeText(context, "Please select Current Occupation of Witness 1.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinner_Occupation_W1, "Please select Current Occupation.", context);
            return -1;
        }
        //STEP 16: Nationality W1
        if (witness1ListDto.getWitnessNationality() == null || witness1ListDto.getWitnessNationality().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Nationality of Witness 1.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerNationality_W1, "Please select Nationality.", context);
            return -1;
        }

        //1-Address Line 1
        witness1ListDto.setAddressLine1(editTextAddressLine1W1.getText().toString().trim());
        if (TextUtils.isEmpty(witness1ListDto.getAddressLine1())) {
            Toast.makeText(context, "Please enter Address line 1 of Witness 1.", Toast.LENGTH_LONG).show();
            editTextAddressLine1W1.setError("Please enter Address line 1.");
            return -1;
        }
        //Address Line 2
        witness1ListDto.setAddressLine2(editTextAddressLine2W1.getText().toString().trim());
        //landmardk
        witness1ListDto.setLandmark(editTextLandmarkW1.getText().toString().trim());
        if (TextUtils.isEmpty(witness1ListDto.getLandmark())) {
            Toast.makeText(context, "Please enter Landmark of Witness 1.", Toast.LENGTH_LONG).show();
            editTextLandmarkW1.setError("Please enter Landmark.");
            return -1;
        }

        //STEP 3: State W1
        if (witness1ListDto.getState() == null || witness1ListDto.getState().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select State in Witness 1.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerState_W1, "Please select State in Witness 1.", context);
            return -1;
        }
        //STEP 4: District W1
        if (witness1ListDto.getDistrict() == null || witness1ListDto.getDistrict().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select District in Witness 1.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerDistrict_W1, "Please select District in Witness 1.", context);
            return -1;
        }
        //STEP 5: Village/Town/City W1
        if (witness1ListDto.getVtc() == null || witness1ListDto.getVtc().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Village/Town/City in Witness 1.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerVTC_W1, "Please select Village/Town/City in Witness 1.", context);
            return -1;
        }
        //1- pincode
        witness1ListDto.setPinCode(editTextPincodeW1.getText().toString().trim());
        if (TextUtils.isEmpty(witness1ListDto.getPinCode()) || !editTextPincodeW1.getText().toString().trim().matches(CommonUtils.pincodePattern) || editTextPincodeW1.getText().toString().trim().length() != 6) {
            Toast.makeText(context, "Please enter PIN Code of Witness 1.", Toast.LENGTH_LONG).show();
            editTextPincodeW1.setError("Please enter PIN Code.");
            return -1;
        }

        //Choose Photo -Bussiness Card Witness 1
        /*if (witness1ListDto.getWitnessBusinessCardImageId() == null) {
            if (TextUtils.isEmpty(witness1ListDto.getWitnessBusinessCardImage())) {
                showMessage("Please add Business Card photo of Witness 1.");
                return -1;

            }
        }*/

        //Choose Photo - Statement Photo Witness 1
        /*if (witness1ListDto.getWitnessStatementImageId() == null) {
            if (TextUtils.isEmpty(witness1ListDto.getWitnessStatementImage())) {
                showMessage("Please add Statement photo of Witness 1.");
                return -1;

            }
        }*/
        //Choose Photo -Personal Photo
        /*if (witness1ListDto.getWitnessPicId() == null) {
            if (TextUtils.isEmpty(witness1ListDto.getWitnessPic())) {
                showMessage("Please add Personal photo of Witness 1.");
                return -1;

            }
        }*/
        return 0;
    }
    //endregion

    //region IsWitnessValidated 2
    public int IsWitnessValidated2() {

        //--------------*********Witness 2 validation************------

        if (witness2ListDto.getWitnessTitle() == null || witness2ListDto.getWitnessTitle().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select MR-MRS.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinner1stWitnessSalutationW2, "Please select MR-MRS.", context);
            return -2;
        }

        //1- first name Witness 2
        witness2ListDto.setWitnessFName(editTextW2FirstName.getText().toString().trim());
        if (TextUtils.isEmpty(witness2ListDto.getWitnessFName())) {
            Toast.makeText(context, "Please enter First Name of Witness 2.", Toast.LENGTH_LONG).show();
            editTextW2FirstName.setError("Please enter First Name.");
            return -2;
        }
        // middle name Witness 2
        witness2ListDto.setWitnessMName(editTextW2MiddleName.getText().toString().trim());
        //last name Witness 2
        witness2ListDto.setWitnessLName(editTextW2LastName.getText().toString().trim());
        if (TextUtils.isEmpty(witness2ListDto.getWitnessLName())) {
            Toast.makeText(context, "Please enter last Name of Witness 2.", Toast.LENGTH_LONG).show();
            editTextW2LastName.setError("Please enter last Name.");
            return -2;
        }
        //email id Witness 2
        witness2ListDto.setWitnessEmailId(editTextEmailIDW2.getText().toString().trim());
    /*    if (TextUtils.isEmpty(witness2ListDto.getWitnessEmailId()) || !editTextEmailIDW2.getText().toString().trim().matches(CommonUtils.emailPattern)) {
            Toast.makeText(context, "Please enter Email Id of Witness 2.", Toast.LENGTH_LONG).show();
            editTextEmailIDW2.setError("Please enter Email Id.");
            return -2;
        }*/
        // mobile number Witness 2
        witness2ListDto.setWitnessMobileNumber(editTextMobileNoW2.getText().toString().trim());
        if (TextUtils.isEmpty(witness2ListDto.getWitnessMobileNumber()) || editTextMobileNoW2.getText().toString().trim().length() != 10) {
            Toast.makeText(context, "Please enter Mobile Number of Witness 2.", Toast.LENGTH_LONG).show();
            editTextMobileNoW2.setError("Please enter Mobile Number.");
            return -2;
        }
        witness2ListDto.setWitnessAltMobileNumber(editTextAlternateMobileNoW2.getText().toString().trim());
        witness2ListDto.setWitnessLandlineNumber(editTextLandlineNoW2.getText().toString().trim());
        // OrganizationName Witness 2
        witness2ListDto.setWitnessOrganizationName(editOrganizationNameW2.getText().toString().trim());
        if (TextUtils.isEmpty(witness2ListDto.getWitnessOrganizationName())) {
            Toast.makeText(context, "Please enter Organization Name of Witness 2.", Toast.LENGTH_LONG).show();
            editOrganizationNameW2.setError("Please enter Organization Name");
            return -2;
        }
        // current Occupation Witness 2
        if (witness2ListDto.getWitnessOccupation() == null || witness2ListDto.getWitnessOccupation().equalsIgnoreCase("-1")) {
            Toast.makeText(context, "Please select Occupation of Witness 2.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinner_Occupation_W2, "Please select Occupation Witness 2.", context);
            return -2;
        }
        //Nationality Witness 2
        if (witness2ListDto.getWitnessNationality() == null || witness2ListDto.getWitnessNationality().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Nationality of Witness 2.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerNationality_W2, "Please select Nationality Witness 2.", context);
            return -2;
        }

        // Address Line Witness 2
        witness2ListDto.setAddressLine1(editTextAddressLine1W2.getText().toString().trim());
        if (TextUtils.isEmpty(witness2ListDto.getAddressLine1())) {
            Toast.makeText(context, "Please enter Address line 1 of Witness 2.", Toast.LENGTH_LONG).show();
            editTextAddressLine1W2.setError("Please enter Address line 1 of Witness 2.");
            return -2;
        }
        //Address Line Witness 2
        witness2ListDto.setAddressLine2(editTextAddressLine2W2.getText().toString().trim());
        //landmardk Witness 2
        witness2ListDto.setLandmark(editTextLandmarkW2.getText().toString().trim());
        if (TextUtils.isEmpty(witness2ListDto.getLandmark())) {
            Toast.makeText(context, "Please enter Landmark of Witness 2.", Toast.LENGTH_LONG).show();
            editTextLandmarkW2.setError("Please enter Landmark.");
            return -2;
        }

        //State Witness 2
        if (witness2ListDto.getState() == null || witness2ListDto.getState().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select State in Witness 2.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerState_W2, "Please select State in Witness 2.", context);
            return -2;
        }
        // District Witness 2
        if (witness2ListDto.getDistrict() == null || witness2ListDto.getDistrict().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select District in Witness 2.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerDistrict_W2, "Please select District in Witness 2.", context);
            return -2;
        }
        // Village/Town/City Witness 2
        if (witness2ListDto.getVtc() == null || witness2ListDto.getVtc().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Village/Town/City in Witness 2.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerVTC_W2, "Please select Village/Town/City in Witness 2.", context);
            return -2;
        }
        // pincode Witness 2
        witness2ListDto.setPinCode(editTextPincodeW2.getText().toString().trim());
        if (TextUtils.isEmpty(witness2ListDto.getPinCode()) || !editTextPincodeW2.getText().toString().trim().matches(CommonUtils.pincodePattern) || editTextPincodeW2.getText().toString().trim().length() != 6) {
            Toast.makeText(context, "Please enter PIN Code of Witness 2.", Toast.LENGTH_LONG).show();
            editTextPincodeW2.setError("Please enter PIN Code.");
            return -2;
        }


        //Choose Photo -Bussiness Card Witness 2
       /* if (witness2ListDto.getWitnessBusinessCardImageId() == null) {
            if (TextUtils.isEmpty(witness2ListDto.getWitnessBusinessCardImage())) {
                showMessage("Please add Business Card photo of Witness 2.");
                return -2;

            }
        }*/

        //Choose Photo - Statement Photo Witness 2
       /* if (witness2ListDto.getWitnessStatementImageId() == null) {
            if (TextUtils.isEmpty(witness2ListDto.getWitnessStatementImage())) {
                showMessage("Please add Statement photo of Witness 2.");
                return -2;

            }
        }*/


        //Choose Photo -Personal Photo W 2
      /*  if (witness2ListDto.getWitnessPicId() == null) {
            if (TextUtils.isEmpty(witness2ListDto.getWitnessPic())) {
                showMessage("Please add Personal photo of Witness 2.");
                return -2;

            }
        }*/

        return 0;
    }
    //endregion

    //region Object convert Json
    public String modelToJsonConvert(String dtoname) {
        String convertModeltoString = "";
        try {
            Gson gson = new Gson();
            String W1 = gson.toJson(witness1ListDto, WitnessListDto.class);
            String W2 = gson.toJson(witness2ListDto, WitnessListDto.class);

            JSONArray jsonArray = new JSONArray();

            if (dtoname.equalsIgnoreCase("witness2")) { //if witness 2 -send data for witness 1
                jsonArray.put(new JSONObject(W1));
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("witness_man_list", jsonArray);
                System.out.println("Witness JSON Array: " + jsonObject.toString());
                convertModeltoString = jsonObject.toString();
            } else if (dtoname.equalsIgnoreCase("witness1")) {
                jsonArray.put(new JSONObject(W2));
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("witness_man_list", jsonArray);
                System.out.println("Witness JSON Array: " + jsonObject.toString());
                convertModeltoString = jsonObject.toString();
            } else {
                jsonArray.put(new JSONObject(W1));
                jsonArray.put(new JSONObject(W2));
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("witness_man_list", jsonArray);
                System.out.println("Witness JSON Array: " + jsonObject.toString());
                convertModeltoString = jsonObject.toString();

            }

            /*String W1 = gson.toJson(witness1ListDto, WitnessListDto.class);
            String W2 = gson.toJson(witness2ListDto, WitnessListDto.class);

            JSONArray jsonArray = new JSONArray();
            jsonArray.put(new JSONObject(W1));
            jsonArray.put(new JSONObject(W2));

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("witness_man_list", jsonArray);
            System.out.println("Witness JSON Array: " + jsonObject.toString());
            convertModeltoString = jsonObject.toString();*/

        } catch (Exception e) {
            e.getMessage();
        }

        return convertModeltoString;
    }

    //endregion


    @Override
    public void onResume() {
        super.onResume();
        if (!IsAlreadyExecuted) {
            switchToGWRWitness("Witness");
            IsAlreadyExecuted = true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getAllReferencesSpinnerData != null && !getAllReferencesSpinnerData.isCancelled()) {
            getAllReferencesSpinnerData.cancel(true);
        }
        if (getAllAddressDetailSpinnerData != null && !getAllAddressDetailSpinnerData.isCancelled()) {
            getAllAddressDetailSpinnerData.cancel(true);
        }
        if (asyncGetGWR != null && !asyncGetGWR.isCancelled()) {
            asyncGetGWR.cancel(true);
        }
    }

    public void switchToGWRWitness(final String name) {
        layoutProgress.setVisibility(View.VISIBLE);
        txtNoDataMsg.setVisibility(View.GONE);
        layoutWitness.setVisibility(View.GONE);

        if (!internetConnection.isNetworkAvailable(context)) {
            AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.internetCheck));
        } else {

            Connection connection = new Connection(context);
            vkid = connection.getVkid();
            permissionHandler.requestPermission(imgBusinessCard_W1, Manifest.permission.READ_PHONE_STATE, getString(R.string.needs_permission_phone_state_msg), new IPermission() {
                @Override
                public void IsPermissionGranted(boolean IsGranted) {
                    if (IsGranted) {
                        if (!internetConnection.isNetworkAvailable(context)) {
                            AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.internetCheck));
                        } else {
                            asyncGetGWR = new AsyncGetGWR(context, vkid, new AsyncGetGWR.Callback() {
                                @Override
                                public void onResult(String result) {
                                    layoutProgress.setVisibility(View.GONE);
                                    processData(result);
                                }
                            });
                            asyncGetGWR.execute(name);

                        }
                    }
                }
            });

        }
    }

    private void processData(String result) {
        if (TextUtils.isEmpty(result)) {
            AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
            txtNoDataMsg.setVisibility(View.VISIBLE);
            layoutWitness.setVisibility(View.GONE);
            return;
        }

        // Handle Error Response From Server.
        if (result.startsWith("ERROR|")) {

            StringTokenizer tokens = new StringTokenizer(result, "|");
            tokens.nextToken();     // Jump to next Token
            String errMsg = tokens.nextToken();

            if (!TextUtils.isEmpty(errMsg)) {
                AlertDialogBoxInfo.alertDialogShow(context, errMsg);
                txtNoDataMsg.setVisibility(View.VISIBLE);
                layoutWitness.setVisibility(View.GONE);
            }
            return;
        }

        //Process response
        if (result.startsWith("OKAY|")) {
            StringTokenizer st1 = new StringTokenizer(result, "|");
            String key = st1.nextToken();
            String data = st1.nextToken();

            if (TextUtils.isEmpty(data)) {
                AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                txtNoDataMsg.setVisibility(View.VISIBLE);
                layoutWitness.setVisibility(View.GONE);
                return;
            }

            //Process Response
            txtNoDataMsg.setVisibility(View.GONE);
            layoutWitness.setVisibility(View.VISIBLE);
            refreshWitnessAndCamera(data);
        }
    }

    public void refreshWitnessAndCamera(String jsonData) {
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(jsonData);
            JSONArray camera_man_list = jsonObject.getJSONArray("camera_man_list");
            JSONArray witness_man_list = jsonObject.getJSONArray("witness_man_list");


            // Gson gson = new GsonBuilder().create();
            //gson.fromJson()


            //Witness List
            Gson gson = new GsonBuilder().create();
            List<WitnessListDto> witnessList = gson.fromJson(witness_man_list.toString(), new TypeToken<ArrayList<WitnessListDto>>() {
            }.getType());
            refresh(witnessList);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //region save witness and camera data to server
    private void saveWitnessAndCameraAsynTaskCall(String jsndat) {

        asyncSaveWitnessAndCameraInfo = new AsyncSaveWitnessAndCameraInfo(context, connection.getVkid(), jsndat, new AsyncSaveWitnessAndCameraInfo.Callback() {
            @Override
            public void onResult(String result) {
                try {

                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                        return;
                    }

                    if (result.startsWith("OKAY")) {
                        //Handle Response
                        System.out.println(result);
                        StringTokenizer st1 = new StringTokenizer(result, "|");
                        String key = st1.nextToken();
                        String franchiseeData = st1.nextToken();
                        try {
                            refreshWitnessAndCamera(franchiseeData);
                            AlertDialogBoxInfo.alertDialogShow(context, "Witness Information Submitted Successfully.");

                        } catch (Exception e) {
                            e.getMessage();
                        }
                    } else if (result.startsWith("ERROR")) {
                        AlertDialogBoxInfo.alertDialogShow(context, result + "Details saving failed.");
                    } else {
                        AlertDialogBoxInfo.alertDialogShow(context, "Details saving failed.");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                }

            }
        });
        asyncSaveWitnessAndCameraInfo.execute("");
    }
    //endregion

    //region confirmation witness dialog
    private void alertDialogWitnessConfirmation(String message, final String witness) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String jsondataforWitness = modelToJsonConvert(witness);
                        saveWitnessAndCameraAsynTaskCall(jsondataforWitness);
                        IsValid(witness);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    //endregion

}
