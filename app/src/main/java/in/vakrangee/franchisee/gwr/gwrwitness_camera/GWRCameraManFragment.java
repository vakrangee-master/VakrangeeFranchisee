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
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

import in.vakrangee.supercore.franchisee.utils.CustomSearchableSpinner;

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

@SuppressLint("ValidFragment")
public class GWRCameraManFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "GWRCameraManFragment";
    private View view;
    private Context context;
    private FranchiseeApplicationRepository fapRepo;
    private DeprecateHandler deprecateHandler;
    private PermissionHandler permissionHandler;

    //region References
    @BindView(R.id.txtCameraManHeader1)
    TextView txtCameraManHeader1;
    @BindView(R.id.txtCameraManHeader2)
    TextView txtCameraManHeader2;
    @BindView(R.id.layout1stCamera)
    LinearLayout layout1stCamera;
    @BindView(R.id.layout2ndCamera)
    LinearLayout layout2ndCamera;
    @BindView(R.id.footer)
    LinearLayout layoutFooter;
    //---------------------1st camera refrence--------------------

    //applicant information C1
    @BindView(R.id.txtVKIDC1)
    TextView txtVKIDC1;
    @BindView(R.id.spinner1stCameraSalutationC1)
    Spinner spinner1stCameraSalutationC1;
    @BindView(R.id.editTextC1FirstName)
    EditText editTextC1FirstName;
    @BindView(R.id.editTextC1MiddleName)
    EditText editTextC1MiddleName;
    @BindView(R.id.editTextC1LastName)
    EditText editTextC1LastName;

    //region contact information C1
    @BindView(R.id.editTextEmailIDC1)
    EditText editTextEmailIDC1;
    @BindView(R.id.editTextMobileNoC1)
    EditText editTextMobileNoC1;
    @BindView(R.id.editTextAlternateMobileNoC1)
    EditText editTextAlternateMobileNoC1;
    @BindView(R.id.editTextLandlineNoC1)
    EditText editTextLandlineNoC1;

    // Occupation - Nationality C1
    @BindView(R.id.spinnerNationality_C1)
    CustomSearchableSpinner spinnerNationality_C1;
    @BindView(R.id.editOrganizationNameC1)
    EditText editOrganizationNameC1;

    //Addresss information C1
    @BindView(R.id.editTextAddressLine1C1)
    EditText editTextAddressLine1C1;
    @BindView(R.id.editTextAddressLine2C1)
    EditText editTextAddressLine2C1;
    @BindView(R.id.editTextLandmarkC1)
    EditText editTextLandmarkC1;
    @BindView(R.id.spinnerState_C1)
    CustomSearchableSpinner spinnerState_C1;
    @BindView(R.id.spinnerDistrict_C1)
    CustomSearchableSpinner spinnerDistrict_C1;
    @BindView(R.id.spinnerVTC_C1)
    CustomSearchableSpinner spinnerVTC_C1;
    @BindView(R.id.editTextPincodeC1)
    EditText editTextPincodeC1;

    //upload image C1
    @BindView(R.id.imgBusinessCard_C1)
    ImageView imgBusinessCard_C1;
    @BindView(R.id.imgPersonalPhoto_C1)
    ImageView imgPersonalPhoto_C1;

    //---------------------2nd camera refrence--------------------

    //applicant information C1
    @BindView(R.id.txtVKIDC2)
    TextView txtVKIDC2;
    @BindView(R.id.spinner1stCameraSalutationC2)
    Spinner spinner1stCameraSalutationC2;
    @BindView(R.id.editTextC2FirstName)
    EditText editTextC2FirstName;
    @BindView(R.id.editTextC2MiddleName)
    EditText editTextC2MiddleName;
    @BindView(R.id.editTextC2LastName)
    EditText editTextC2LastName;

    //region contact information C1
    @BindView(R.id.editTextEmailIDC2)
    EditText editTextEmailIDC2;
    @BindView(R.id.editTextMobileNoC2)
    EditText editTextMobileNoC2;
    @BindView(R.id.editTextAlternateMobileNoC2)
    EditText editTextAlternateMobileNoC2;
    @BindView(R.id.editTextLandlineNoC2)
    EditText editTextLandlineNoC2;

    // Occupation - Nationality C1
    @BindView(R.id.spinnerNationality_C2)
    CustomSearchableSpinner spinnerNationality_C2;
    @BindView(R.id.editOrganizationNameC2)
    EditText editOrganizationNameC2;

    //Addresss information C1
    @BindView(R.id.editTextAddressLine1C2)
    EditText editTextAddressLine1C2;
    @BindView(R.id.editTextAddressLine2C2)
    EditText editTextAddressLine2C2;
    @BindView(R.id.editTextLandmarkC2)
    EditText editTextLandmarkC2;
    @BindView(R.id.spinnerState_C2)
    CustomSearchableSpinner spinnerState_C2;
    @BindView(R.id.spinnerDistrict_C2)
    CustomSearchableSpinner spinnerDistrict_C2;
    @BindView(R.id.spinnerVTC_C2)
    CustomSearchableSpinner spinnerVTC_C2;
    @BindView(R.id.editTextPincodeC2)
    EditText editTextPincodeC2;

    //Camera make c1 and c2
    @BindView(R.id.editTextCameraMakeC1)
    EditText editTextCameraMakeC1;
    @BindView(R.id.editTextCameraMakeC2)
    EditText editTextCameraMakeC2;

    //camera model c1 and c2
    @BindView(R.id.editTextCameraModelC1)
    EditText editTextCameraModelC1;
    @BindView(R.id.editTextCameraModelC2)
    EditText editTextCameraModelC2;

    //ISDate and time stamping
    @BindView(R.id.checkBoxDateTimeStampingC1)
    CheckBox checkBoxDateTimeStampingC1;
    @BindView(R.id.checkBoxDateTimeStampingC2)
    CheckBox checkBoxDateTimeStampingC2;
    //upload image C1
    @BindView(R.id.imgBusinessCard_C2)
    ImageView imgBusinessCard_C2;
    @BindView(R.id.imgPersonalPhoto_C2)
    ImageView imgPersonalPhoto_C2;

    //---- Compulsory red mark camera_man 1
    @BindView(R.id.txt1stRefrenceLblC1)
    TextView txt1stRefrenceLblC1;
    @BindView(R.id.txtEmailIdLblC1)
    TextView txtEmailIdLblC1;
    @BindView(R.id.txtMobileNoLblC1)
    TextView txtMobileNoLblC1;
    @BindView(R.id.txtAlternateMobileNoLblC1)
    TextView txtAlternateMobileNoLblC1;
    @BindView(R.id.txtLandlineNoLblC1)
    TextView txtLandlineNoLblC1;
    @BindView(R.id.txtOragnizationNameC1)
    TextView txtOragnizationNameC1;
    @BindView(R.id.txtNationalityLblC1)
    TextView txtNationalityLblC1;
    @BindView(R.id.txtAddressLine1LblC1)
    TextView txtAddressLine1LblC1;
    @BindView(R.id.txtLandmarkLblC1)
    TextView txtLandmarkLblC1;
    @BindView(R.id.txtStateLblC1)
    TextView txtStateLblC1;
    @BindView(R.id.txtDistrictLblC1)
    TextView txtDistrictLblC1;
    @BindView(R.id.txtVTCLblC1)
    TextView txtVTCLblC1;
    @BindView(R.id.txtPincodeLblC1)
    TextView txtPincodeLblC1;
    @BindView(R.id.txtBusinessCardUploadLblC1)
    TextView txtBusinessCardUploadLblC1;
    @BindView(R.id.txtPersonal1C1)
    TextView txtPersonal1C1;

    @BindView(R.id.txtCameraMakeC1)
    TextView txtCameraMakeC1;
    @BindView(R.id.txtCameraModelC1)
    TextView txtCameraModelC1;
    @BindView(R.id.txtIsDateTimeStampingC1)
    TextView txtIsDateTimeStampingC1;

    //----------- Compulsory red mark camera_man 2

    @BindView(R.id.txt1stRefrenceLblC2)
    TextView txt1stRefrenceLblC2;
    @BindView(R.id.txtEmailIdLblC2)
    TextView txtEmailIdLblC2;
    @BindView(R.id.txtMobileNoLblC2)
    TextView txtMobileNoLblC2;
    @BindView(R.id.txtAlternateMobileNoLblC2)
    TextView txtAlternateMobileNoLblC2;
    @BindView(R.id.txtLandlineNoLblC2)
    TextView txtLandlineNoLblC2;
    @BindView(R.id.txtOragnizationNameC2)
    TextView txtOragnizationNameC2;
    @BindView(R.id.txtNationalityLblC2)
    TextView txtNationalityLblC2;
    @BindView(R.id.txtAddressLine1LblC2)
    TextView txtAddressLine1LblC2;
    @BindView(R.id.txtLandmarkLblC2)
    TextView txtLandmarkLblC2;
    @BindView(R.id.txtStateLblC2)
    TextView txtStateLblC2;
    @BindView(R.id.txtDistrictLblC2)
    TextView txtDistrictLblC2;
    @BindView(R.id.txtVTCLblC2)
    TextView txtVTCLblC2;
    @BindView(R.id.txtPincodeLblC2)
    TextView txtPincodeLblC2;
    @BindView(R.id.txtBusinessCardUploadLblC2)
    TextView txtBusinessCardUploadLblC2;
    @BindView(R.id.txtPersonal1C2)
    TextView txtPersonal1C2;


    @BindView(R.id.txtCameraMakeC2)
    TextView txtCameraMakeC2;
    @BindView(R.id.txtCameraModelC2)
    TextView txtCameraModelC2;
    @BindView(R.id.txtIsDateTimeStampingC2)
    TextView txtIsDateTimeStampingC2;
    @BindView(R.id.btnSubmitCamera)
    TextView btnSubmitCamera;
    @BindView(R.id.txtWarningIconC1)
    TextView txtWarningIconC1;
    private GetAllReferencesSpinnerData getAllReferencesSpinnerData = null;
    private GetAllAddressDetailSpinnerData getAllAddressDetailSpinnerData = null;

    private List<CustomFranchiseeApplicationSpinnerDto> salutationList1st;
    private List<CustomFranchiseeApplicationSpinnerDto> currentOccupationList1st;
    private List<CustomFranchiseeApplicationSpinnerDto> nationalityList;
    private List<CustomFranchiseeApplicationSpinnerDto> stateList;
    private List<CustomFranchiseeApplicationSpinnerDto> districtList;
    private List<CustomFranchiseeApplicationSpinnerDto> VTCList;
    private int FROM = -1;
    private static final int BuinessCard_COPY_C1 = 1;
    private static final int Personal_Image_C1 = 2;
    private static final int BuinessCard_COPY_C2 = 3;
    private static final int Personal_Image_C2 = 4;
    private String SEL_FILE_TYPE;
    private FileAttachmentDialog fileAttachementDialog;
    private Uri picUri;                 //Picture URI
    private static final int CAMERA_PIC_REQUEST = 100;
    private static final int BROWSE_FOLDER_REQUEST = 101;
    private CameraListDto camera1ListDto;
    private CameraListDto camera2ListDto;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alert;
    private boolean IsEditable = false;
    private String vkid;
    private InternetConnection internetConnection;
    private AsyncGetGWR asyncGetGWR = null;
    private TextView txtNoDataMsg;
    private LinearLayout layoutProgress;
    private LinearLayout layoutCamera;
    private AsyncSaveWitnessAndCameraInfo asyncSaveWitnessAndCameraInfo;
    private Connection connection;
    private boolean IsAlreadyExecuted = false;


    public GWRCameraManFragment() {
    }

    public GWRCameraManFragment(boolean IsEditable) {
        this.IsEditable = IsEditable;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment fragment_gwrcamera_man
        view = inflater.inflate(R.layout.fragment_gwr_camera, container, false);
        this.context = getContext();
        deprecateHandler = new DeprecateHandler(context);
        permissionHandler = new PermissionHandler(getActivity());
        fapRepo = new FranchiseeApplicationRepository(context);
        internetConnection = new InternetConnection(context);
        ButterKnife.bind(this, view);
        connection = new Connection(context);

        layoutCamera = view.findViewById(R.id.layoutCamera);
        layoutProgress = (LinearLayout) view.findViewById(R.id.layoutProgress);
        txtNoDataMsg = view.findViewById(R.id.txtNoDataMsg);

        //set Compulsory mark
        TextView[] txtViewsForCompulsoryMark = {txt1stRefrenceLblC1, txtMobileNoLblC1, txtOragnizationNameC1,
                txtNationalityLblC1, txtAddressLine1LblC1, txtLandmarkLblC1, txtStateLblC1, txtDistrictLblC1, txtVTCLblC1, txtPincodeLblC1,
                txtCameraMakeC1, txtCameraModelC1,

                txt1stRefrenceLblC2, txtMobileNoLblC2, txtOragnizationNameC2,
                txtNationalityLblC2, txtAddressLine1LblC2, txtLandmarkLblC2, txtStateLblC2, txtDistrictLblC2, txtVTCLblC2, txtPincodeLblC2,
                txtCameraMakeC2, txtCameraModelC2,
        };
        GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);

        //Input filter
        //1st witness
        CommonUtils.InputFiletrWithMaxLength(editTextC1FirstName, "\"~#^|$%&*!'", 50);
        CommonUtils.InputFiletrWithMaxLength(editTextC1MiddleName, "\"~#^|$%&*!'", 50);
        CommonUtils.InputFiletrWithMaxLength(editTextC1LastName, "\"~#^|$%&*!'", 50);
        CommonUtils.InputFiletrWithMaxLength(editTextEmailIDC1, "\"~#^|$%&*!'", 50);
        CommonUtils.InputFiletrWithMaxLength(editOrganizationNameC1, "\"~#^|$%&*!'", 50);
        CommonUtils.InputFiletrWithMaxLength(editTextAddressLine1C1, "\"~#^|$%&*!'", 35);
        CommonUtils.InputFiletrWithMaxLength(editTextAddressLine2C1, "\"~#^|$%&*!'", 35);
        CommonUtils.InputFiletrWithMaxLength(editTextLandmarkC1, "\"~#^|$%&*!'", 35);
        CommonUtils.InputFiletrWithMaxLength(editTextCameraMakeC1, "\"~#^|$%&*!'", 50);
        CommonUtils.InputFiletrWithMaxLength(editTextCameraModelC1, "\"~#^|$%&*!'", 50);

        //2nd witness
        CommonUtils.InputFiletrWithMaxLength(editTextC2FirstName, "\"~#^|$%&*!'", 50);
        CommonUtils.InputFiletrWithMaxLength(editTextC2MiddleName, "\"~#^|$%&*!'", 50);
        CommonUtils.InputFiletrWithMaxLength(editTextC2LastName, "\"~#^|$%&*!'", 50);
        CommonUtils.InputFiletrWithMaxLength(editTextEmailIDC2, "\"~#^|$%&*!'", 50);
        CommonUtils.InputFiletrWithMaxLength(editOrganizationNameC2, "\"~#^|$%&*!'", 50);
        CommonUtils.InputFiletrWithMaxLength(editTextAddressLine1C2, "\"~#^|$%&*!'", 35);
        CommonUtils.InputFiletrWithMaxLength(editTextAddressLine2C2, "\"~#^|$%&*!'", 35);
        CommonUtils.InputFiletrWithMaxLength(editTextLandmarkC2, "\"~#^|$%&*!'", 35);
        CommonUtils.InputFiletrWithMaxLength(editTextCameraMakeC2, "\"~#^|$%&*!'", 50);
        CommonUtils.InputFiletrWithMaxLength(editTextCameraModelC2, "\"~#^|$%&*!'", 50);

        checkBoxDateTimeStampingC1.setOnCheckedChangeListener(this);
        checkBoxDateTimeStampingC2.setOnCheckedChangeListener(this);
        return view;
    }

    @OnClick({R.id.txtCameraManHeader1, R.id.txtCameraManHeader2, R.id.imgBusinessCard_C1, R.id.imgPersonalPhoto_C1,
            R.id.imgBusinessCard_C2, R.id.imgPersonalPhoto_C2, R.id.btnSubmitCamera})
    @Override
    public void onClick(View view) {
        int Id = view.getId();
        if (Id == R.id.txtCameraManHeader1) {
            txtCameraManHeader1.setTextColor(deprecateHandler.getColor(R.color.gray));
            txtCameraManHeader1.setBackgroundColor(deprecateHandler.getColor(R.color.grey));
            txtCameraManHeader2.setTextColor(deprecateHandler.getColor(R.color.white));
            txtCameraManHeader2.setBackgroundColor(deprecateHandler.getColor(R.color.gray));

            layout2ndCamera.setVisibility(View.GONE);
            layout1stCamera.setVisibility(View.VISIBLE);

        } else if (Id == R.id.txtCameraManHeader2) {
            txtCameraManHeader2.setTextColor(deprecateHandler.getColor(R.color.gray));
            txtCameraManHeader2.setBackgroundColor(deprecateHandler.getColor(R.color.grey));

            txtCameraManHeader1.setTextColor(deprecateHandler.getColor(R.color.white));
            txtCameraManHeader1.setBackgroundColor(deprecateHandler.getColor(R.color.gray));

            layout2ndCamera.setVisibility(View.GONE); //VISIBLE
            layout1stCamera.setVisibility(View.GONE);

        } else if (Id == R.id.imgBusinessCard_C1) {
            FROM = BuinessCard_COPY_C1;
            SEL_FILE_TYPE = "images/pdf";
            showAttachmentDialog(view, SEL_FILE_TYPE);

        } else if (Id == R.id.imgPersonalPhoto_C1) {
            FROM = Personal_Image_C1;
            SEL_FILE_TYPE = "images";
            showAttachmentDialog(view, SEL_FILE_TYPE);

        } else if (Id == R.id.imgBusinessCard_C2) {
            FROM = BuinessCard_COPY_C2;
            SEL_FILE_TYPE = "images/pdf";
            showAttachmentDialog(view, SEL_FILE_TYPE);

        } else if (Id == R.id.imgPersonalPhoto_C2) {
            FROM = Personal_Image_C2;
            SEL_FILE_TYPE = "images";
            showAttachmentDialog(view, SEL_FILE_TYPE);

        } else if (Id == R.id.btnSubmitCamera) {

            int cameramanValidation1 = IsCameraManValidated1();
            // int cameramanValidation2 = gwrCameraManFragment.IsCameraManValidated2();

            if (cameramanValidation1 == 0) {
                String jsondataforWitness = modelToJsonConvert("cameraman2");
                saveWitnessAndCameraAsynTaskCall(jsondataforWitness);
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
            case BuinessCard_COPY_C1:
                if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgBusinessCard_C1);
                else
                    Glide.with(context).asBitmap().load(bitmap).into(imgBusinessCard_C1);

                camera1ListDto.setCameramanBusinessCardImage(base64);
                camera1ListDto.setCameramanBussinessCardImageFileExt(ext);
                break;

            case Personal_Image_C1:
                if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgPersonalPhoto_C1);
                else
                    Glide.with(context).asBitmap().load(bitmap).into(imgPersonalPhoto_C1);


                camera1ListDto.setCameramanPic(base64);
                camera1ListDto.setCameramanPicFileExt(ext);
                break;

            case BuinessCard_COPY_C2:
                if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgBusinessCard_C2);
                else
                    Glide.with(context).asBitmap().load(bitmap).into(imgBusinessCard_C2);


                camera2ListDto.setCameramanBusinessCardImage(base64);
                camera2ListDto.setCameramanBussinessCardImageFileExt(ext);
                break;

            case Personal_Image_C2:
                if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgPersonalPhoto_C2);
                else
                    Glide.with(context).asBitmap().load(bitmap).into(imgPersonalPhoto_C2);

                camera2ListDto.setCameramanPic(base64);
                camera2ListDto.setCameramanPicFileExt(ext);
                break;
            default:
                break;

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        int Id = adapterView.getId();
        if (Id == R.id.spinner1stCameraSalutationC1) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinner1stCameraSalutationC1.getItemAtPosition(position);
                camera1ListDto.setCameramanTitle(entityDto.getId());
            }
        } else if (Id == R.id.spinner1stCameraSalutationC2) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinner1stCameraSalutationC2.getItemAtPosition(position);
                camera2ListDto.setCameramanTitle(entityDto.getId());
            }
        }
       /* else if (Id == R.id.spinnerNationality_C1) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerNationality_C1.getItemAtPosition(position);
                camera1ListDto.setCameramanNationality(Integer.valueOf(entityDto.getId()));
            }
        }
        else if (Id == R.id.spinnerNationality_C2) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerNationality_C2.getItemAtPosition(position);
                camera2ListDto.setCameramanNationality(Integer.valueOf(entityDto.getId()));
            }
        }*/

        //-----C1

        else if (Id == R.id.spinnerState_C1) {
            if (position > 0) {
                spinnerState_C1.setTitle("Select State");
                spinnerState_C1.requestFocus();
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerState_C1.getItemAtPosition(position);
                if (!entityDto.getId().equals("0")) {
                    camera1ListDto.setState(entityDto.getId());

                    //Get District
                    getAllAddressDetailSpinnerData = new GetAllAddressDetailSpinnerData(camera1ListDto.getState(), null);
                    getAllAddressDetailSpinnerData.execute("COM_DISTRICT");

                }
            } else {
                camera1ListDto.setState(null);
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));
                spinnerDistrict_C1.setEnabled(true);
                setDistrictSpinnerAdapter(list1, spinnerDistrict_C1, null);
                camera1ListDto.setDistrict(null);
                spinnerVTC_C1.setEnabled(true);
                setDistrictSpinnerAdapter(list1, spinnerVTC_C1, null);
                camera1ListDto.setVtc(null);
            }

        } else if (Id == R.id.spinnerDistrict_C1) {
            if (position > 0) {
                spinnerDistrict_C1.setTitle("Select District");
                spinnerDistrict_C1.requestFocus();
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerDistrict_C1.getItemAtPosition(position);
                if (!entityDto.getId().equals("0")) {
                    camera1ListDto.setDistrict(entityDto.getId());

                    //Get VTC
                    getAllAddressDetailSpinnerData = new GetAllAddressDetailSpinnerData(camera1ListDto.getState(), camera1ListDto.getDistrict());
                    getAllAddressDetailSpinnerData.execute("COM_VTC");

                }
            } else {
                camera1ListDto.setDistrict(null);
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

                spinnerVTC_C1.setEnabled(true);
                setDistrictSpinnerAdapter(list1, spinnerVTC_C1, null);
                camera1ListDto.setVtc(null);

            }
        } else if (Id == R.id.spinnerVTC_C1) {
            spinnerVTC_C1.setTitle("Select District");
            spinnerVTC_C1.requestFocus();
            CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerVTC_C1.getItemAtPosition(position);
            camera1ListDto.setVtc(entityDto.getId());

        } else if (Id == R.id.spinnerState_C2) {
            if (position > 0) {
                spinnerState_C2.setTitle("Select State");
                spinnerState_C2.requestFocus();
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerState_C2.getItemAtPosition(position);
                if (!entityDto.getId().equals("0")) {
                    camera2ListDto.setState(entityDto.getId());

                    //Get District
                    getAllAddressDetailSpinnerData = new GetAllAddressDetailSpinnerData(camera2ListDto.getState(), null);
                    getAllAddressDetailSpinnerData.execute("COM_DISTRICT_C2");

                }
            } else {
                camera2ListDto.setState(null);
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));
                spinnerDistrict_C2.setEnabled(true);
                setDistrictSpinnerAdapter(list1, spinnerDistrict_C2, null);
                camera2ListDto.setDistrict(null);
                spinnerVTC_C2.setEnabled(true);
                setDistrictSpinnerAdapter(list1, spinnerVTC_C2, null);
                camera2ListDto.setVtc(null);
            }

        } else if (Id == R.id.spinnerDistrict_C2) {
            if (position > 0) {
                spinnerDistrict_C2.setTitle("Select District");
                spinnerDistrict_C2.requestFocus();
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerDistrict_C2.getItemAtPosition(position);
                if (!entityDto.getId().equals("0")) {
                    camera2ListDto.setDistrict(entityDto.getId());

                    //Get VTC
                    getAllAddressDetailSpinnerData = new GetAllAddressDetailSpinnerData(camera2ListDto.getState(), camera2ListDto.getDistrict());
                    getAllAddressDetailSpinnerData.execute("COM_VTC_C2");
                }
            } else {
                camera2ListDto.setDistrict(null);
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));
                spinnerVTC_C2.setEnabled(true);
                setDistrictSpinnerAdapter(list1, spinnerVTC_C2, null);
                camera2ListDto.setVtc(null);
            }
        } else if (Id == R.id.spinnerVTC_C2) {
            spinnerVTC_C2.setTitle("Select District");
            spinnerVTC_C2.requestFocus();
            CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerVTC_C2.getItemAtPosition(position);
            camera2ListDto.setVtc(entityDto.getId());

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public void refresh(List<CameraListDto> cameraList) {
        if (cameraList == null || cameraList.size() == 0) {
            camera1ListDto = new CameraListDto();
            camera2ListDto = new CameraListDto();

        } else if (cameraList.size() == 1) {
            camera1ListDto = cameraList.get(0);
            camera2ListDto = new CameraListDto();

        } else {
            camera1ListDto = cameraList.get(0);
            camera2ListDto = cameraList.get(1);
        }

        //bind spinner

        getAllReferencesSpinnerData = new GetAllReferencesSpinnerData();
        getAllReferencesSpinnerData.execute("");

        getAllAddressDetailSpinnerData = new GetAllAddressDetailSpinnerData(null, null);
        getAllAddressDetailSpinnerData.execute("STATE");

        bindEditext();
    }

    private void bindEditext() {
        //vkid
        if (camera1ListDto.getVkid() != null) {
            txtVKIDC1.setText("VKID :  " + camera1ListDto.getVkid());
        }
        if (camera2ListDto.getVkid() != null) {
            txtVKIDC2.setText("VKID :  " + camera2ListDto.getVkid());
        }
        // first , middle ,last name  camera 1 and 2
        editTextC1FirstName.setText(TextUtils.isEmpty(camera1ListDto.getCameramanFName()) ? null : camera1ListDto.getCameramanFName());
        editTextC1MiddleName.setText(TextUtils.isEmpty(camera1ListDto.getCameramanMName()) ? null : camera1ListDto.getCameramanMName());
        editTextC1LastName.setText(TextUtils.isEmpty(camera1ListDto.getCameramanLName()) ? null : camera1ListDto.getCameramanLName());

        editTextC2FirstName.setText(TextUtils.isEmpty(camera2ListDto.getCameramanFName()) ? null : camera2ListDto.getCameramanFName());
        editTextC2MiddleName.setText(TextUtils.isEmpty(camera2ListDto.getCameramanMName()) ? null : camera2ListDto.getCameramanMName());
        editTextC2LastName.setText(TextUtils.isEmpty(camera2ListDto.getCameramanLName()) ? null : camera2ListDto.getCameramanLName());

        //email id camera 1 and 2
        editTextEmailIDC1.setText(TextUtils.isEmpty(camera1ListDto.getCameramanEmailId()) ? null : camera1ListDto.getCameramanEmailId());
        editTextEmailIDC2.setText(TextUtils.isEmpty(camera2ListDto.getCameramanEmailId()) ? null : camera2ListDto.getCameramanEmailId());

        if (camera1ListDto.getCameramanMobileNumber() != null) {
            editTextMobileNoC1.setText(camera1ListDto.getCameramanMobileNumber());
        }
        if (camera2ListDto.getCameramanMobileNumber() != null) {
            editTextMobileNoC2.setText(camera2ListDto.getCameramanMobileNumber());
        }
        if (camera1ListDto.getCameramanAltMobileNumber() != null) {
            editTextAlternateMobileNoC1.setText(camera1ListDto.getCameramanAltMobileNumber());
        }
        if (camera2ListDto.getCameramanAltMobileNumber() != null) {
            editTextAlternateMobileNoC2.setText(camera2ListDto.getCameramanAltMobileNumber());
        }
        if (camera1ListDto.getCameramanLandlineNumber() != null) {
            editTextLandlineNoC1.setText(camera1ListDto.getCameramanLandlineNumber());
        }
        if (camera2ListDto.getCameramanLandlineNumber() != null) {
            editTextLandlineNoC2.setText(camera2ListDto.getCameramanLandlineNumber());
        }
        //organzation name
        editOrganizationNameC1.setText(TextUtils.isEmpty(camera1ListDto.getCameramanOrganizationName()) ? null : camera1ListDto.getCameramanOrganizationName());
        editOrganizationNameC2.setText(TextUtils.isEmpty(camera2ListDto.getCameramanOrganizationName()) ? null : camera2ListDto.getCameramanOrganizationName());

        //Address line 1 and 2  and landmark
        editTextAddressLine1C1.setText(TextUtils.isEmpty(camera1ListDto.getAddressLine1()) ? null : camera1ListDto.getAddressLine1());
        editTextAddressLine2C1.setText(TextUtils.isEmpty(camera1ListDto.getAddressLine2()) ? null : camera1ListDto.getAddressLine2());

        editTextAddressLine1C2.setText(TextUtils.isEmpty(camera2ListDto.getAddressLine1()) ? null : camera2ListDto.getAddressLine1());
        editTextAddressLine2C2.setText(TextUtils.isEmpty(camera2ListDto.getAddressLine2()) ? null : camera2ListDto.getAddressLine2());

        editTextLandmarkC1.setText(TextUtils.isEmpty(camera1ListDto.getLandmark()) ? null : camera1ListDto.getLandmark());
        editTextLandmarkC2.setText(TextUtils.isEmpty(camera2ListDto.getLandmark()) ? null : camera2ListDto.getLandmark());

        editTextPincodeC1.setText(TextUtils.isEmpty(camera1ListDto.getPinCode()) ? null : camera1ListDto.getPinCode());
        editTextPincodeC2.setText(TextUtils.isEmpty(camera2ListDto.getPinCode()) ? null : camera2ListDto.getPinCode());

        // camera make model
        editTextCameraMakeC1.setText(TextUtils.isEmpty(camera1ListDto.getMake()) ? null : camera1ListDto.getMake());
        editTextCameraMakeC2.setText(TextUtils.isEmpty(camera2ListDto.getMake()) ? null : camera2ListDto.getMake());

        //camera model
        editTextCameraModelC1.setText(TextUtils.isEmpty(camera1ListDto.getModel()) ? null : camera1ListDto.getModel());
        editTextCameraModelC2.setText(TextUtils.isEmpty(camera2ListDto.getModel()) ? null : camera2ListDto.getModel());

        // checkbox date and time stamping c1 adn c2
        // checkbox date and time stamping c1 adn c2
        if (camera1ListDto.isIs_datetime_stamping() != null) {
            if (camera1ListDto.isIs_datetime_stamping() == 1)
                checkBoxDateTimeStampingC1.setChecked(true);
            else
                checkBoxDateTimeStampingC1.setChecked(false);
        } else {
            checkBoxDateTimeStampingC1.setChecked(false);
        }
        if (camera2ListDto.isIs_datetime_stamping() != null) {
            if (camera2ListDto.isIs_datetime_stamping() == 1)
                checkBoxDateTimeStampingC2.setChecked(true);
            else
                checkBoxDateTimeStampingC2.setChecked(false);
        } else {
            checkBoxDateTimeStampingC2.setChecked(false);
        }

        // bussiness card  C1
        if (camera1ListDto.getCameramanBusinessCardImageId() != null && !camera1ListDto.getCameramanBusinessCardImageId().equalsIgnoreCase("0")) {
            photoDisplay(imgBusinessCard_C1, String.valueOf(camera1ListDto.getCameramanBusinessCardImageId()), camera1ListDto.getCameramanBussinessCardImageFileExt());

        }
        // Personal image  c1
        if (camera1ListDto.getCameramanPicId() != null && !camera1ListDto.getCameramanPicId().equalsIgnoreCase("0")) {
            photoDisplay(imgPersonalPhoto_C1, String.valueOf(camera1ListDto.getCameramanPicId()), camera1ListDto.getCameramanPicFileExt());
        }
        // bussiness card  C2
        if (camera2ListDto.getCameramanBusinessCardImageId() != null && !camera2ListDto.getCameramanBusinessCardImageId().equalsIgnoreCase("0")) {
            photoDisplay(imgBusinessCard_C2, String.valueOf(camera2ListDto.getCameramanBusinessCardImageId()), camera2ListDto.getCameramanBussinessCardImageFileExt());
        }
        // Personal image  c2
        if (camera2ListDto.getCameramanPicId() != null && !camera2ListDto.getCameramanPicId().equalsIgnoreCase("0")) {
            photoDisplay(imgPersonalPhoto_C2, String.valueOf(camera2ListDto.getCameramanPicId()), camera2ListDto.getCameramanPicFileExt());
        }

        //status -cameraman 1
        if (!TextUtils.isEmpty(camera1ListDto.getStatusCameraman())) {
            if (camera1ListDto.getStatusCameraman().equalsIgnoreCase("1")) {
                //disable views
                GUIUtils.setViewAndChildrenEnabled(layout1stCamera, false);
                GUIUtils.setViewAndChildrenEnabled(layoutFooter, false);
                layoutFooter.setVisibility(View.GONE);

                txtWarningIconC1.setVisibility(View.VISIBLE);
                txtWarningIconC1.setText(context.getString(R.string.fa_circle_check));
                txtWarningIconC1.setTypeface(VakrangeeKendraApplication.font_awesome, Typeface.BOLD);
                txtWarningIconC1.setTextColor(deprecateHandler.getColor(R.color.green));

            } else if (camera1ListDto.getStatusCameraman().equalsIgnoreCase("2")) {
                //send back for correction
                txtWarningIconC1.setVisibility(View.VISIBLE);
                txtWarningIconC1.setText(context.getString(R.string.fa_circle_cross));
                txtWarningIconC1.setTypeface(VakrangeeKendraApplication.font_awesome, Typeface.BOLD);
                txtWarningIconC1.setTextColor(deprecateHandler.getColor(R.color.ired));

                GUIUtils.setViewAndChildrenEnabled(layout1stCamera, true);
                if (!TextUtils.isEmpty(camera1ListDto.getStatusMsg())) {
                    AlertDialogBoxInfo.alertDialogShow(context, "Send Back for Correction in Cameraman:  " + camera1ListDto.getStatusMsg());
                }
            } else {
                //enable view
                GUIUtils.setViewAndChildrenEnabled(layout1stCamera, true);
            }
        }

    }

    //region image display as id and .ext
    private void photoDisplay(ImageView imageView, String imageid, String imageExt) {
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

    }
    //endregion

    //region isvalid for layout
    public int IsValid(String cameraman) {
        if (cameraman.equalsIgnoreCase("cameraman1")) {
            layout1stCamera.setVisibility(View.VISIBLE);
            layout2ndCamera.setVisibility(View.GONE);

            txtCameraManHeader1.setTextColor(deprecateHandler.getColor(R.color.gray));
            txtCameraManHeader1.setBackgroundColor(deprecateHandler.getColor(R.color.grey));
            txtCameraManHeader2.setTextColor(deprecateHandler.getColor(R.color.white));
            txtCameraManHeader2.setBackgroundColor(deprecateHandler.getColor(R.color.gray));

        } else {
            layout1stCamera.setVisibility(View.GONE);
            layout2ndCamera.setVisibility(View.GONE); //VISIBLE
            txtCameraManHeader2.setTextColor(deprecateHandler.getColor(R.color.gray));
            txtCameraManHeader2.setBackgroundColor(deprecateHandler.getColor(R.color.grey));

            txtCameraManHeader1.setTextColor(deprecateHandler.getColor(R.color.white));
            txtCameraManHeader1.setBackgroundColor(deprecateHandler.getColor(R.color.gray));
        }
       /* if (IsCameraManValidated() == -1) {
            layout1stCamera.setVisibility(View.VISIBLE);
            layout2ndCamera.setVisibility(View.GONE);
            return -1;
        } else if (IsCameraManValidated() == -2) {
            layout1stCamera.setVisibility(View.GONE);
            layout2ndCamera.setVisibility(View.VISIBLE);
            return -2;
        }*/
        return 0;
    }
    //endregion

    //region CameraMan validate 1
    public int IsCameraManValidated1() {
        //-----------**************Wintess 1 validation --------------
        //1- spinner Name -MR-MRS C1
        if (camera1ListDto.getCameramanTitle() == null || camera1ListDto.getCameramanTitle().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select MR-MRS.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinner1stCameraSalutationC1, "Please select MR-MRS.", context);
            return -1;
        }

        //1- first name C1
        camera1ListDto.setCameramanFName(editTextC1FirstName.getText().toString().trim());
        if (TextUtils.isEmpty(camera1ListDto.getCameramanFName())) {
            Toast.makeText(context, "Please enter First Name of Cameraman.", Toast.LENGTH_LONG).show();
            editTextC1FirstName.setError("Please enter First Name.");
            return -1;
        }
        // middle name C1
        camera1ListDto.setCameramanMName(editTextC1MiddleName.getText().toString().trim());
        //last name C1
        camera1ListDto.setCameramanLName(editTextC1LastName.getText().toString().trim());
        if (TextUtils.isEmpty(camera1ListDto.getCameramanLName())) {
            Toast.makeText(context, "Please enter last Name of Cameraman.", Toast.LENGTH_LONG).show();
            editTextC1LastName.setError("Please enter last Name.");
            return -1;
        }
        //email id
        camera1ListDto.setCameramanEmailId(editTextEmailIDC1.getText().toString().trim());

       /*  if (TextUtils.isEmpty(camera1ListDto.getCameramanEmailId()) || !editTextEmailIDC1.getText().toString().trim().matches(CommonUtils.emailPattern)) {
            Toast.makeText(context, "Please enter Email Id of CameraMan 1.", Toast.LENGTH_LONG).show();
            editTextEmailIDC1.setError("Please enter Email Id.");
            return -1;
        }*/
        // mobile number C1
        camera1ListDto.setCameramanMobileNumber(editTextMobileNoC1.getText().toString().trim());
        if (TextUtils.isEmpty(camera1ListDto.getCameramanMobileNumber()) || editTextMobileNoC1.getText().toString().trim().length() != 10) {
            Toast.makeText(context, "Please enter Mobile Number of Cameraman.", Toast.LENGTH_LONG).show();
            editTextMobileNoC1.setError("Please enter Mobile Number.");
            return -1;
        }
        camera1ListDto.setCameramanAltMobileNumber(editTextAlternateMobileNoC1.getText().toString().trim());
        camera1ListDto.setCameramanLandlineNumber(editTextLandlineNoC1.getText().toString().trim());
        // OrganizationName
        camera1ListDto.setCameramanOrganizationName(editOrganizationNameC1.getText().toString().trim());
        if (TextUtils.isEmpty(camera1ListDto.getCameramanOrganizationName())) {
            Toast.makeText(context, "Please enter Studio Name of Cameraman.", Toast.LENGTH_LONG).show();
            editOrganizationNameC1.setError("Please enter Studio Name");
            return -1;
        }
        //STEP 16: Nationality C1
       /* if (camera1ListDto.getCameramanNationality() == null || camera1ListDto.getCameramanNationality() == 0) {
            Toast.makeText(context, "Please select Nationality of CameraMan 1 .", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerNationality_C1, "Please select Nationality CameraMan 1.", context);
            return -1;
        }*/

        //1-Address Line 1
        camera1ListDto.setAddressLine1(editTextAddressLine1C1.getText().toString().trim());
        if (TextUtils.isEmpty(camera1ListDto.getAddressLine1())) {
            Toast.makeText(context, "Please enter Address line 1 of Cameraman.", Toast.LENGTH_LONG).show();
            editTextAddressLine1C1.setError("Please enter Address line 1 of Cameraman.");
            return -1;
        }
        //Address Line 2
        camera1ListDto.setAddressLine2(editTextAddressLine2C1.getText().toString().trim());
        //landmardk
        camera1ListDto.setLandmark(editTextLandmarkC1.getText().toString().trim());
        if (TextUtils.isEmpty(camera1ListDto.getLandmark())) {
            Toast.makeText(context, "Please enter Landmark of Cameraman.", Toast.LENGTH_LONG).show();
            editTextLandmarkC1.setError("Please enter Landmark Cameraman.");
            return -1;
        }

        //STEP 3: State C1
        if (camera1ListDto.getState() == null || camera1ListDto.getState().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select State in CameraMan.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerState_C1, "Please select State in Cameraman.", context);
            return -1;
        }
        //STEP 4: District C1
        if (camera1ListDto.getDistrict() == null || camera1ListDto.getDistrict().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select District in CameraMan.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerDistrict_C1, "Please select District in Cameraman.", context);
            return -1;
        }
        //STEP 5: Village/Town/City C1
        if (camera1ListDto.getVtc() == null || camera1ListDto.getVtc().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Village/Town/City in CameraMan.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerVTC_C1, "Please select Village/Town/City in Cameraman 1.", context);
            return -1;
        }
        //1- pincode
        camera1ListDto.setPinCode(editTextPincodeC1.getText().toString().trim());
        if (TextUtils.isEmpty(camera1ListDto.getPinCode()) || !editTextPincodeC1.getText().toString().trim().matches(CommonUtils.pincodePattern) || editTextPincodeC1.getText().toString().trim().length() != 6) {
            Toast.makeText(context, "Please enter PIN Code of Cameraman.", Toast.LENGTH_LONG).show();
            editTextPincodeC1.setError("Please enter PIN Code.");
            return -1;
        }

        // camera make c1
        camera1ListDto.setMake(editTextCameraMakeC1.getText().toString().trim());
        if (camera1ListDto.getMake() == null || TextUtils.isEmpty(camera1ListDto.getMake())) {
            Toast.makeText(context, "Please enter Camera Make of Cameraman. e.g:Sony,Nikon", Toast.LENGTH_LONG).show();
            editTextCameraMakeC1.setError("e.g: Sony,Nikon");
            return -1;
        }
        //camera model c1
        camera1ListDto.setModel(editTextCameraModelC1.getText().toString().trim());
        if (camera1ListDto.getModel() == null || TextUtils.isEmpty(camera1ListDto.getModel())) {
            Toast.makeText(context, "Please enter Camera Model name of Cameraman. e.g:Nikon d5300", Toast.LENGTH_LONG).show();
            editTextCameraModelC1.setError("e.g:Nikon d5300");
            return -1;
        }
        // is date and time stamping c1
        /*if (camera1ListDto.isIs_datetime_stamping() == null || camera1ListDto.isIs_datetime_stamping() == 0) {
            Toast.makeText(context, "Please select Date and Time Stamping on CameraMan 1.", Toast.LENGTH_LONG).show();
            checkBoxDateTimeStampingC1.setError("Please select Date and Time Stamping.");
            return -1;
        }*/

        //Choose Photo - Business card C1

        /*if (camera1ListDto.getCameramanBusinessCardImageId() == null) {
            if (TextUtils.isEmpty(camera1ListDto.getCameramanBusinessCardImage())) {
                showMessage("Please add Business Card photo of CameraMan 1.");
                return -1;

            }
        }*/
        //Choose Photo - Personal photo C1
        /*if (camera1ListDto.getCameramanPicId() == null) {
            if (TextUtils.isEmpty(camera1ListDto.getCameramanPic())) {
                showMessage("Please add Personal photo of CameraMan 1.");
                return -1;

            }
        }*/
        return 0;
    }

    //endregion

    //region cameraman validate 2
    public int IsCameraManValidated2() {

        //--------------*********camera 2 validation************------

        if (camera2ListDto.getCameramanTitle() == null || camera2ListDto.getCameramanTitle().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select MR-MRS.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinner1stCameraSalutationC2, "Please select MR-MRS.", context);
            return -2;
        }

        //1- first name camera 2
        camera2ListDto.setCameramanFName(editTextC2FirstName.getText().toString().trim());
        if (TextUtils.isEmpty(camera2ListDto.getCameramanFName())) {
            Toast.makeText(context, "Please enter First Name of Cameraman 2.", Toast.LENGTH_LONG).show();
            editTextC2FirstName.setError("Please enter First Name Cameraman 2.");
            return -2;
        }
        // middle name camera 2
        camera2ListDto.setCameramanMName(editTextC2MiddleName.getText().toString().trim());
        //last name camera 2
        camera2ListDto.setCameramanLName(editTextC2LastName.getText().toString().trim());
        if (TextUtils.isEmpty(camera2ListDto.getCameramanLName())) {
            Toast.makeText(context, "Please enter last Name of Cameraman 2.", Toast.LENGTH_LONG).show();
            editTextC2LastName.setError("Please enter last Name.");
            return -2;
        }
        //email id camera 2
        camera2ListDto.setCameramanEmailId(editTextEmailIDC2.getText().toString().trim());
       /* if (TextUtils.isEmpty(camera2ListDto.getCameramanEmailId()) || !editTextEmailIDC2.getText().toString().trim().matches(CommonUtils.emailPattern)) {
            Toast.makeText(context, "Please enter Email Id of CameraMan 2.", Toast.LENGTH_LONG).show();
            editTextEmailIDC2.setError("Please enter Email Id.");
            return -2;
        }*/
        // mobile number camera 2
        camera2ListDto.setCameramanMobileNumber(editTextMobileNoC2.getText().toString().trim());
        if (TextUtils.isEmpty(camera2ListDto.getCameramanMobileNumber()) || editTextMobileNoC2.getText().toString().trim().length() != 10) {
            Toast.makeText(context, "Please enter Mobile Number of Cameraman 2.", Toast.LENGTH_LONG).show();
            editTextMobileNoC2.setError("Please enter Mobile Number.");
            return -2;
        }
        camera2ListDto.setCameramanAltMobileNumber(editTextAlternateMobileNoC2.getText().toString().trim());
        camera2ListDto.setCameramanLandlineNumber(editTextLandlineNoC2.getText().toString().trim());
        // OrganizationName camera 2
        camera2ListDto.setCameramanOrganizationName(editOrganizationNameC2.getText().toString().trim());
        if (TextUtils.isEmpty(camera2ListDto.getCameramanOrganizationName())) {
            Toast.makeText(context, "Please enter Studio Name of Cameraman 2.", Toast.LENGTH_LONG).show();
            editOrganizationNameC2.setError("Please enter Studio Name");
            return -2;
        }

        //Nationality camera 2
       /* if (camera2ListDto.getCameramanNationality() == null || camera2ListDto.getCameramanNationality() == 0) {
            Toast.makeText(context, "Please select Nationality of CameraMan 2.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerNationality_C2, "Please select Nationality.", context);
            return -2;
        }*/

        // Address Line camera 2
        camera2ListDto.setAddressLine1(editTextAddressLine1C2.getText().toString().trim());
        if (TextUtils.isEmpty(camera2ListDto.getAddressLine1())) {
            Toast.makeText(context, "Please enter Address line 1 of Cameraman 2.", Toast.LENGTH_LONG).show();
            editTextAddressLine1C2.setError("Please enter Address line 1.");
            return -2;
        }
        //Address Line camera 2
        camera2ListDto.setAddressLine2(editTextAddressLine2C2.getText().toString().trim());
        //landmardk camera 2
        camera2ListDto.setLandmark(editTextLandmarkC2.getText().toString().trim());
        if (TextUtils.isEmpty(camera2ListDto.getLandmark())) {
            Toast.makeText(context, "Please enter Landmark of Cameraman 2.", Toast.LENGTH_LONG).show();
            editTextLandmarkC2.setError("Please enter Landmark.");
            return -2;
        }

        //State camera 2
        if (camera2ListDto.getState() == null || camera2ListDto.getState().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select State in Cameraman 2.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerState_C2, "Please select State in Cameraman 2.", context);
            return -2;
        }
        // District camera 2
        if (camera2ListDto.getDistrict() == null || camera2ListDto.getDistrict().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select District in Cameraman 2.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerDistrict_C2, "Please select District in Cameraman 2.", context);
            return -2;
        }
        // Village/Town/City camera 2
        if (camera2ListDto.getVtc() == null || camera2ListDto.getVtc().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Village/Town/City in Cameraman 2.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerVTC_C2, "Please select Village/Town/City in Cameraman 2.", context);
            return -2;
        }
        // pincode Witness 2
        camera2ListDto.setPinCode(editTextPincodeC2.getText().toString().trim());
        if (TextUtils.isEmpty(camera2ListDto.getPinCode()) || !editTextPincodeC2.getText().toString().trim().matches(CommonUtils.pincodePattern) || editTextPincodeC2.getText().toString().trim().length() != 6) {
            Toast.makeText(context, "Please enter PIN Code of Cameraman 2.", Toast.LENGTH_LONG).show();
            editTextPincodeC2.setError("Please enter PIN Code.");
            return -2;
        }

        // camera make c2
        camera2ListDto.setMake(editTextCameraMakeC2.getText().toString().trim());
        if (camera2ListDto.getMake() == null || TextUtils.isEmpty(camera2ListDto.getMake())) {
            Toast.makeText(context, "Please enter Camera Make of Cameraman 2. e.g:Sony,Nikon", Toast.LENGTH_LONG).show();
            editTextCameraMakeC2.setError("e.g: Sony,Nikon");
            return -2;
        }
        //camera model c2
        camera2ListDto.setModel(editTextCameraModelC2.getText().toString().trim());
        if (camera2ListDto.getModel() == null || TextUtils.isEmpty(camera2ListDto.getModel())) {
            Toast.makeText(context, "Please enter Camera Model name of Cameraman 2. e.g:Nikon d5300", Toast.LENGTH_LONG).show();
            editTextCameraModelC2.setError("e.g:Nikon d5300");
            return -2;
        }
        // is date and time stamping c2
       /* if (camera2ListDto.isIs_datetime_stamping() == null || camera2ListDto.isIs_datetime_stamping() == 0) {
            Toast.makeText(context, "Please select Date and Time Stamping  of CameraMan 2.", Toast.LENGTH_LONG).show();
            checkBoxDateTimeStampingC2.setError("Please select Date and Time Stamping.");
            return -2;
        }*/

        //Choose Photo -Bussiness card C2
       /* if (camera2ListDto.getCameramanBusinessCardImageId() == null) {
            if (TextUtils.isEmpty(camera2ListDto.getCameramanBusinessCardImage())) {
                showMessage("Please add Business Card photo of CameraMan 2.");
                return -2;

            }
        }*/
        //Choose Photo - Personal photo C2
       /* if (camera2ListDto.getCameramanPicId() == null) {
            if (TextUtils.isEmpty(camera2ListDto.getCameramanPic())) {
                showMessage("Please add Personal photo of CameraMan 2.");
                return -2;

            }
        }*/
        return 0;
    }
    //endregion

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()) {
            case R.id.checkBoxDateTimeStampingC1:
                if (isChecked) {
                    camera1ListDto.setIs_datetime_stamping(1);
                    checkBoxDateTimeStampingC1.setError(null);
                } else {
                    camera1ListDto.setIs_datetime_stamping(0);
                }
                break;
            case R.id.checkBoxDateTimeStampingC2:
                if (isChecked) {
                    camera2ListDto.setIs_datetime_stamping(1);
                    checkBoxDateTimeStampingC2.setError(null);
                } else {
                    camera2ListDto.setIs_datetime_stamping(0);
                }
                break;
        }
    }


    class GetAllReferencesSpinnerData extends AsyncTask<String, Void, String> {
        //private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          /*  progress = new ProgressDialog(context);
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

            //STEP 2: Nationality list
            nationalityList = fapRepo.getNationalityList();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // progress.dismiss();
            //dismissProgressSpinner();
            bindSpinner();
        }
    }

    private void bindSpinner() {
        // spinner_focusablemode(spinnerNationality_Witness1);

        //STEP 1: 1st spinner 1st cameraMan Salutation C1
        CustomFranchiseeApplicationSpinnerAdapter salutationAdapterC1 = new CustomFranchiseeApplicationSpinnerAdapter(context, salutationList1st);
        spinner1stCameraSalutationC1.setAdapter(salutationAdapterC1);
        int appSalutationPos = fapRepo.getSelectedPos(salutationList1st, String.valueOf(camera1ListDto.getCameramanTitle()));
        spinner1stCameraSalutationC1.setSelection(appSalutationPos);
        spinner1stCameraSalutationC1.setOnItemSelectedListener(this);

        //STEP 2: 1st Nationality C1
        spinnerNationality_C1.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, nationalityList));
        int nationalityPos = fapRepo.getSelectedPos(nationalityList, String.valueOf(camera1ListDto.getCameramanNationality()));
        spinnerNationality_C1.setSelection(nationalityPos);
        spinnerNationality_C1.setOnItemSelectedListener(this);

        //STEP 3: 1st spinner 1st cameraMan Salutation C2
        CustomFranchiseeApplicationSpinnerAdapter salutationAdapterW2 = new CustomFranchiseeApplicationSpinnerAdapter(context, salutationList1st);
        spinner1stCameraSalutationC2.setAdapter(salutationAdapterW2);
        int appSalutationPosW2 = fapRepo.getSelectedPos(salutationList1st, String.valueOf(camera2ListDto.getCameramanTitle()));
        spinner1stCameraSalutationC2.setSelection(appSalutationPosW2);
        spinner1stCameraSalutationC2.setOnItemSelectedListener(this);

        //STEP 4: 1st Nationality C2
        spinnerNationality_C2.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, nationalityList));
        int nationalityPosC2 = fapRepo.getSelectedPos(nationalityList, String.valueOf(camera2ListDto.getCameramanNationality()));
        spinnerNationality_C2.setSelection(nationalityPosC2);
        spinnerNationality_C2.setOnItemSelectedListener(this);

    }

    class GetAllAddressDetailSpinnerData extends AsyncTask<String, Void, String> {
        //private ProgressDialog progress;
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
                case "COM_DISTRICT_C2":
                    //STEP 2: District List
                    districtList = fapRepo.getAllDistrictBylList(stateId);
                    break;
                case "COM_VTC":
                case "COM_VTC_C2":
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
                    setDistrictSpinnerAdapter(districtList, spinnerDistrict_C1, String.valueOf(camera1ListDto.getDistrict()));
                    break;

                case "COM_VTC":
                    setVTCSpinnerAdapter(VTCList, spinnerVTC_C1, String.valueOf(camera1ListDto.getVtc()));
                    break;

                case "COM_DISTRICT_C2":
                    setDistrictSpinnerAdapter(districtList, spinnerDistrict_C2, String.valueOf(camera2ListDto.getDistrict()));
                    break;
                case "COM_VTC_C2":
                    setVTCSpinnerAdapter(VTCList, spinnerVTC_C2, String.valueOf(camera2ListDto.getVtc()));
                    break;

                default:
                    break;
            }

        }
    }

    private void BindSpineerforAddress() {

        /*spinner_focusablemode(spinnerState_C1);
        spinner_focusablemode(spinnerDistrict_C1);
        spinner_focusablemode(spinnerVTC_C1);

        spinner_focusablemode(spinnerState_C2);
        spinner_focusablemode(spinnerDistrict_C2);
        spinner_focusablemode(spinnerVTC_C2);*/

        spinnerState_C1.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, stateList));
        int appsstatePos = fapRepo.getSelectedPos(stateList, String.valueOf(camera1ListDto.getState()));
        spinnerState_C1.setSelection(appsstatePos);
        spinnerState_C1.setOnItemSelectedListener(this);

        spinnerState_C2.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, stateList));
        int appsstatePosC2 = fapRepo.getSelectedPos(stateList, String.valueOf(camera2ListDto.getState()));
        spinnerState_C2.setSelection(appsstatePosC2);
        spinnerState_C2.setOnItemSelectedListener(this);

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

    //region Object convert Json send to server
    public String modelToJsonConvert(String dtoname) {
        String convertModeltoString = "";
        try {
            Gson gson = new Gson();
            String C1 = gson.toJson(camera1ListDto, CameraListDto.class);
            String C2 = gson.toJson(camera2ListDto, CameraListDto.class);

            JSONArray jsonArray = new JSONArray();

            if (dtoname.equalsIgnoreCase("cameraman2")) { //if cameraman 2 -send data for cameraman 1
                jsonArray.put(new JSONObject(C1));
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("camera_man_list", jsonArray);
                System.out.println("Camera JSON Array: " + jsonObject.toString());
                convertModeltoString = jsonObject.toString();
            } else if (dtoname.equalsIgnoreCase("cameraman1")) {
                jsonArray.put(new JSONObject(C2));
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("camera_man_list", jsonArray);
                System.out.println("Camera JSON Array: " + jsonObject.toString());
                convertModeltoString = jsonObject.toString();
            } else {
                jsonArray.put(new JSONObject(C1));
                jsonArray.put(new JSONObject(C2));
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("camera_man_list", jsonArray);
                System.out.println("Camera JSON Array: " + jsonObject.toString());
                convertModeltoString = jsonObject.toString();

            }

        } catch (Exception e) {
            e.getMessage();
        }

        return convertModeltoString;
    }
    //endregion

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

    @Override
    public void onResume() {
        super.onResume();
        if (!IsAlreadyExecuted) {
            switchToGWRCamera("Camera");
            IsAlreadyExecuted = true;
        }
    }

    public void switchToGWRCamera(final String name) {
        layoutProgress.setVisibility(View.VISIBLE);
        txtNoDataMsg.setVisibility(View.GONE);
        layoutCamera.setVisibility(View.GONE);

        if (!internetConnection.isNetworkAvailable(context)) {
            AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.internetCheck));
        } else {

            Connection connection = new Connection(context);
            vkid = connection.getVkid();
            permissionHandler.requestPermission(imgBusinessCard_C1, Manifest.permission.READ_PHONE_STATE, getString(R.string.needs_permission_phone_state_msg), new IPermission() {
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
            layoutCamera.setVisibility(View.GONE);
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
                layoutCamera.setVisibility(View.GONE);
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
                layoutCamera.setVisibility(View.GONE);
                return;
            }

            //Process Response
            txtNoDataMsg.setVisibility(View.GONE);
            layoutCamera.setVisibility(View.VISIBLE);
            refreshWitnessAndCamera(data);
        }
    }

    public void refreshWitnessAndCamera(String jsonData) {
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(jsonData);
            JSONArray camera_man_list = jsonObject.getJSONArray("camera_man_list");

            Gson gson = new GsonBuilder().create();
            List<CameraListDto> CameraList = gson.fromJson(camera_man_list.toString(), new TypeToken<ArrayList<CameraListDto>>() {
            }.getType());
            refresh(CameraList);


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
                            AlertDialogBoxInfo.alertDialogShow(context, "Cameraman Information Submitted Successfully.");

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

}