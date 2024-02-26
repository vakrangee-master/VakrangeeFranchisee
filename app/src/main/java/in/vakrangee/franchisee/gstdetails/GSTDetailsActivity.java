package in.vakrangee.franchisee.gstdetails;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nononsenseapps.filepicker.Utils;

import in.vakrangee.supercore.franchisee.utils.CustomSearchableSpinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.vakrangee.franchisee.BuildConfig;
import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.activity.DashboardActivity;
import in.vakrangee.franchisee.sitelayout.activity.MyVakrangeeKendraLocationDetailsNextGen;
import in.vakrangee.franchisee.sitelayout.asyntask.AsyncTaskDistricSpinner;
import in.vakrangee.franchisee.sitelayout.asyntask.AsyncTaskIsRural;
import in.vakrangee.franchisee.sitelayout.asyntask.AsyncTaskStateSpinner;
import in.vakrangee.franchisee.sitelayout.asyntask.AsyncTaskVillageSpinner;
import in.vakrangee.franchisee.sitelayout.model.District;
import in.vakrangee.franchisee.sitelayout.model.State;
import in.vakrangee.franchisee.sitelayout.model.Village;
import in.vakrangee.supercore.franchisee.commongui.DateTimePickerDialog;
import in.vakrangee.supercore.franchisee.commongui.FileAttachmentDialog;
import in.vakrangee.supercore.franchisee.commongui.FileUtils;
import in.vakrangee.supercore.franchisee.commongui.FilteredFilePickerActivity;
import in.vakrangee.supercore.franchisee.commongui.imagepreview.CustomImageZoomDialog;
import in.vakrangee.supercore.franchisee.gstdetails.GSTINDTO;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.GPSTracker;
import in.vakrangee.supercore.franchisee.utils.GUIUtils;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;

public class GSTDetailsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = "GSTDetailsActivity";
    private Toolbar toolbar;
    private Connection connection;
    private Uri picUri;
    private static final int CAMERA_PIC_REQUEST = 1;
    private CustomImageZoomDialog customImagePreviewDialog;
    private EditText editTextGSTName, editTextGSTNumber;
    private ImageView imgGSTCertificate;
    private Context context;
    private PermissionHandler permissionHandler;
    private MaterialButton btnClear, btnCancel, btnSubmit;
    private AsyncupdateFranchiseeGSTDetail asyncGSTDetails;
    private AsyncasyncgetFranchiseeGSTDetail asyncgetFranchiseeGSTDetail;
    private String latitude = "", longitude = "", currentTimestamp = "";
    private GPSTracker gpsTracker;
    private GSTINDTO gstindto;

    //Add new Funcationality
    private CustomSearchableSpinner stateSpinner, districtSpinner, villageSpinner;
    EditText SiteVisitPincode, SiteVisitAddress1, SiteVisitAddress2, SiteVisitLandmark, edtLocality, edtArea;
    private AsyncTaskStateSpinner asyncTaskStateSpinner;
    private AsyncTaskDistricSpinner asyncTaskDistricSpinner;
    private AsyncTaskVillageSpinner asyncTaskVillageSpinner;
    private AsyncTaskIsRural asyncTaskIsRural;
    public List<State> arrayListStates = new ArrayList<>();
    public List<District> arrayListDistrict = new ArrayList<>();
    private List<Village> arrayListVillage = new ArrayList<>();
    private String selectStateID, selectDistricID, selectVillageID;
    private String mCurrentPhotoPath;
    private static int FROM;
    private static final int TYPE_GST_CERTIFICATE = 1;
    private static final int TYPE_GST_TRN_RECEIPT = 2;

    @BindView(R.id.txtEntityNameLbl)
    TextView txtEntityName;

    @BindView(R.id.txtGstinNumberLbl)
    TextView txtGstinNumber;

    @BindView(R.id.txtAddressLine1Lbl)
    TextView txtAddressGstin;

    @BindView(R.id.txtLandmarkLbl)
    TextView txtLandMark;

    @BindView(R.id.txtAreaLbl)
    TextView txtArea;

    @BindView(R.id.txtLocalityLbl)
    TextView txtLocality;

    @BindView(R.id.txtStateLbl)
    TextView txtState;

    @BindView(R.id.txtDistrictLbl)
    TextView txtDistrict;

    @BindView(R.id.txtVTCLbl)
    TextView txtVTC;

    @BindView(R.id.txtPinCodeLbl)
    TextView txtPinCode;

    @BindView(R.id.txtGSTImageCertificateLbl)
    TextView txtImageCertificate;

    @BindView(R.id.gstInScrollView)
    ScrollView scrollViewgstInScrollView;

    @BindView(R.id.editTextConfirmGstNumber)
    EditText editTextConfirmGstNumber;
    private boolean edittext_listener = true;

    @BindView(R.id.layoutGSTDetailApplied)
    LinearLayout layoutGSTDetailApplied;

    @BindView(R.id.layoutGSTDetailExist)
    LinearLayout layoutGSTDetailExist;

    @BindView(R.id.layoutFooter)
    LinearLayout layoutFooter;

    private FileAttachmentDialog fileAttachementDialog = null;
    private static final int BROWSE_FOLDER_REQUEST = 101;

    //region Applied For GSTIN
    @BindView(R.id.checkboxAppliedForGSTIN)
    CheckBox checkboxAppliedForGSTIN;

    @BindView(R.id.layoutHavingGSTDetails)
    LinearLayout layoutHavingGSTDetails;

    @BindView(R.id.layoutAppliedGSTIN)
    LinearLayout layoutAppliedGSTIN;

    @BindView(R.id.txtTRNNumberLbl)
    TextView txtTRNNumberLbl;

    @BindView(R.id.editTextTRNNumber)
    EditText editTextTRNNumber;

    @BindView(R.id.txtGSTAckReceiptLbl)
    TextView txtGSTAckReceiptLbl;

    @BindView(R.id.imgGSTAckReceipt)
    ImageView imgGSTAckReceipt;

    @BindView(R.id.txtDateOfApplicationGSTLbl)
    TextView txtDateOfApplicationGSTLbl;

    @BindView(R.id.textViewDateOfApplicationGST)
    TextView textViewDateOfApplicationGST;

    @BindView(R.id.txtExpectedDateOfGSTLbl)
    TextView txtExpectedDateOfGSTLbl;

    @BindView(R.id.textViewExpectedDateOfGST)
    TextView textViewExpectedDateOfGST;

    private DateTimePickerDialog dateTimePickerDialog;
    private DateFormat dateFormatter2 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    private DateFormat dateFormatterYMD = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private Date gstApplicationDate;
    private String strGstApplicationDate;
    private Date expectedGstApplicationDate;
    private String strExpectedGstApplicationDate;
    private int selectedDateTimeId = 0;

    //endregion

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_gst_details);

        ButterKnife.bind(this);

        context = this;
        permissionHandler = new PermissionHandler(this);
        connection = new Connection(GSTDetailsActivity.this);
        final Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        gpsTracker = new GPSTracker(context);

        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbarReadiness);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + "Update GST Details" + "</small>"));

        }

        //Widgets
        btnSubmit = findViewById(R.id.btnSubmitGSTDetails);
        btnSubmit.setTypeface(font);
        btnSubmit.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " + getResources().getString(R.string.submit)));
        btnSubmit.setOnClickListener(this);

        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setTypeface(font);
        btnCancel.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " + getResources().getString(R.string.Cancel)));
        btnCancel.setOnClickListener(this);

        btnClear = findViewById(R.id.btnClear);
        btnClear.setTypeface(font);
        btnClear.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " + getResources().getString(R.string.clear)));
        btnClear.setOnClickListener(this);

        editTextGSTName = findViewById(R.id.editTextGstEntityName);
        editTextGSTNumber = findViewById(R.id.editTextGstNumber);
        imgGSTCertificate = findViewById(R.id.imgGSTImageDoc);
        stateSpinner = findViewById(R.id.spinnerState);
        districtSpinner = findViewById(R.id.spinnerDistrict);
        villageSpinner = findViewById(R.id.spinnerVTC);
        SiteVisitAddress1 = (EditText) findViewById(R.id.editTextAddressLine1);
        SiteVisitAddress2 = (EditText) findViewById(R.id.editTextAddressLine2);
        SiteVisitLandmark = (EditText) findViewById(R.id.editTextLandmark);
        edtArea = (EditText) findViewById(R.id.edtArea);
        edtLocality = (EditText) findViewById(R.id.edtLocality);
        SiteVisitPincode = (EditText) findViewById(R.id.ediTextGSTPincode);

        CommonUtils.AllCapCharCaptial(editTextGSTName);
        CommonUtils.AllCapCharCaptial(editTextTRNNumber);
        CommonUtils.InputFiletrWithMaxLength(SiteVisitAddress1, "~#^|$%&*!", 35);
        CommonUtils.InputFiletrWithMaxLength(SiteVisitAddress2, "~#^|$%&*!", 35);
        CommonUtils.InputFiletrWithMaxLength(SiteVisitLandmark, "~#^|$%&*!", 35);
        CommonUtils.InputFiletrWithMaxLength(editTextConfirmGstNumber, "~#^|$%&*!", 15);
        compulsoryMark();

        //GSTIN Image Certificate
        imgGSTCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImgOrPDF(view);
            }
        });

        //GSTIN Number
        TextChangedListenerGSTIN_Number();

        //Applied For GSTIN
        checkboxAppliedForGSTIN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                String status = isChecked ? "1" : "0";
                gstindto.setAppliedForGSTIN(status);

                if (isChecked) {
                    layoutAppliedGSTIN.setVisibility(View.VISIBLE);
                    layoutHavingGSTDetails.setVisibility(View.GONE);
                } else {
                    layoutAppliedGSTIN.setVisibility(View.GONE);
                    layoutHavingGSTDetails.setVisibility(View.VISIBLE);
                }

            }
        });

        //GST Acknowledgement Receipt
        imgGSTAckReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FROM = TYPE_GST_TRN_RECEIPT;
                if ((TextUtils.isEmpty(gstindto.getGstinAckReceiptImgId()) || gstindto.getGstinAckReceiptImgId().equalsIgnoreCase("0")) && TextUtils.isEmpty(gstindto.getGstinAckReceiptImgBase64())) {
                    startCamera(view);
                } else {
                    GSTINDTO previewDto = prepareDtoForPreview(FROM);
                    showImagePreviewDialog(previewDto);
                }
            }
        });

        //Date Of GST Application
        textViewDateOfApplicationGST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDateTimeId = view.getId();
                showDateTimeDialogPicker();
            }
        });

        //Expected Date Of GST Application
        textViewExpectedDateOfGST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDateTimeId = view.getId();
                showDateTimeDialogPicker();
            }
        });

        getFranchiseeGSTDetail();
        init_Spinner();
    }

    //region TextChangedListener GSTIN Number
    private void TextChangedListenerGSTIN_Number() {

        editTextGSTNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int len = editTextGSTNumber.getText().toString().trim().length();
                if (len <= 0)
                    return;
            }

            @Override
            public void afterTextChanged(Editable editable) {

                String s = editable.toString();

                int len = editTextGSTNumber.getText().toString().trim().length();
                if (len <= 0)
                    return;

                if (!s.equals(s.toUpperCase())) {
                    s = s.toUpperCase();
                    editTextGSTNumber.setText(s);
                    editTextGSTNumber.setSelection(editTextGSTNumber.getText().length());

                }

                if (editTextGSTNumber.getText().toString().contains(" ")) {
                    editTextGSTNumber.setText(editTextGSTNumber.getText().toString().replaceAll(" ", ""));
                    editTextGSTNumber.setSelection(editTextGSTNumber.getText().length());
                }


                if (!CommonUtils.GSTINisValid(s)) {
                    edittext_listener = true;
                } else {
                    edittext_listener = false;
                }

                //set color base on
                if (edittext_listener) {
                    editTextGSTNumber.setTextColor(Color.parseColor("#FF0000"));
                    editTextGSTNumber.setError(getResources().getString(R.string.hintGsTnumbernotValid));
                } else {
                    editTextGSTNumber.setTextColor(Color.parseColor("#468847"));
                    editTextGSTNumber.setError(null);

                    String strPass1 = editTextGSTNumber.getText().toString();
                    String strPass2 = editTextConfirmGstNumber.getText().toString();

                    if (editTextGSTNumber.length() < 1) {
                    } else if (!strPass1.equalsIgnoreCase(strPass2)) {
                        editTextGSTNumber.setError("Not Match");
                        editTextGSTNumber.setTextColor(Color.parseColor("#000000"));
                    } else {
                        editTextConfirmGstNumber.setTextColor(Color.parseColor("#468847"));
                        editTextConfirmGstNumber.setError(null);
                        editTextGSTNumber.setTextColor(Color.parseColor("#468847"));
                        editTextGSTNumber.setError(null);
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

                String strPass1 = editTextGSTNumber.getText().toString();
                String strPass2 = editTextConfirmGstNumber.getText().toString();

                if (editTextConfirmGstNumber.length() < 1) {
                } else if (!strPass1.equalsIgnoreCase(strPass2)) {
                    editTextConfirmGstNumber.setError("Not Match");
                    editTextConfirmGstNumber.setTextColor(Color.parseColor("#000000"));
                } else if (!CommonUtils.GSTINisValid(s1)) {

                    editTextConfirmGstNumber.setTextColor(Color.parseColor("#FF0000"));
                    editTextConfirmGstNumber.setError(getResources().getString(R.string.hintGsTnumbernotValid));
                    editTextGSTNumber.setTextColor(Color.parseColor("#FF0000"));
                    editTextGSTNumber.setError(getResources().getString(R.string.hintGsTnumbernotValid));
                } else {
                    editTextConfirmGstNumber.setTextColor(Color.parseColor("#468847"));
                    editTextConfirmGstNumber.setError(null);
                    editTextGSTNumber.setTextColor(Color.parseColor("#468847"));
                    editTextGSTNumber.setError(null);

                }
            }
        });

    }
    //endregion

    private void compulsoryMark() {
        GUIUtils.CompulsoryMark(txtEntityName, "Name of Entity on GSTIN Certificate ");
        GUIUtils.CompulsoryMark(txtGstinNumber, "GSTIN Number ");
        GUIUtils.CompulsoryMark(txtAddressGstin, "Address on GSTIN Certificate ");
        GUIUtils.CompulsoryMark(txtState, "State ");
        GUIUtils.CompulsoryMark(txtDistrict, "District ");
        GUIUtils.CompulsoryMark(txtVTC, "Village/Town/City ");
        GUIUtils.CompulsoryMark(txtPinCode, "PIN code ");
        GUIUtils.CompulsoryMark(txtImageCertificate, "GSTIN Certificate Image ");

        //Applied for GSTIN Details
        GUIUtils.CompulsoryMark(txtGSTAckReceiptLbl, "GSTIN Acknowledgement Receipt ");
        GUIUtils.CompulsoryMark(txtTRNNumberLbl, "Application Reference Number(ARN) ");
        GUIUtils.CompulsoryMark(txtDateOfApplicationGSTLbl, "Date Of GSTIN Application ");
        GUIUtils.CompulsoryMark(txtExpectedDateOfGSTLbl, "Expected Date Of GSTIN Application ");
    }

    //region init_Spinner
    private void init_Spinner() {
        spinner_focusablemode(stateSpinner);
        spinner_focusablemode(districtSpinner);
        spinner_focusablemode(villageSpinner);
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                stateSpinner.setTitle("Select State");
                stateSpinner.requestFocus();

                final State id = (State) stateSpinner.getItemAtPosition(i);
                selectStateID = id.getStateId();
                if (selectStateID == null || selectStateID.equalsIgnoreCase("") || selectStateID.equalsIgnoreCase("Please Select")) {

                } else {

                    CallDistricAsynTask();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //call to District Spinner
        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                districtSpinner.setTitle("Select District");
                districtSpinner.requestFocus();
                final District id = (District) districtSpinner.getItemAtPosition(i);
                selectDistricID = id.getDistrictId();
                if (selectDistricID == null || selectDistricID.equalsIgnoreCase("") || selectDistricID.equalsIgnoreCase("Please Select")) {

                } else {
                    // call to Village Asyntask  - when click Village list item
                    CallVillageAsynTask();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //call to village Spinner
        villageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                villageSpinner.setTitle("Select Village/Town/City");
                villageSpinner.setFocusable(true);
                final Village id = (Village) villageSpinner.getItemAtPosition(i);
                selectVillageID = id.getVillageId();
                if (selectVillageID == null || selectVillageID.equalsIgnoreCase("") || selectVillageID.equalsIgnoreCase("Please Select")) {

                } else {
                    // call to Rural asyntask - when click village list item
                    //  CallIsRuralAsyncTask();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        CallStateAsynTask();
    }

    private void spinner_focusablemode(CustomSearchableSpinner stateSpinner) {
        //true
        stateSpinner.setFocusable(false);
        stateSpinner.setFocusableInTouchMode(false);
    }
    //endregion

    //region AsynTask Call State
    private void CallStateAsynTask() {
        asyncTaskStateSpinner = new AsyncTaskStateSpinner(GSTDetailsActivity.this, false, new AsyncTaskStateSpinner.Callback() {
            @Override
            public void onResult(String diplayServerResopnse) {
                StringTokenizer tokens = new StringTokenizer(diplayServerResopnse, "|");
                String first = tokens.nextToken();
                String second = "{" + "\"state\":" + tokens.nextToken() + "}";

                try {
                    arrayListStates.removeAll(arrayListStates);
                    JSONObject jsnobject = new JSONObject(second);
                    JSONArray jsonArray = jsnobject.getJSONArray("state");
                    arrayListStates.add(new State("0", "Please Select"));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String a = obj.getString("lg_state_id");
                        String b = obj.getString("state_name");
                        arrayListStates.add(new State(a, b));
                    }

                } catch (Exception e) {
                    e.getMessage();
                }

                setStateSpinnerAdapter(arrayListStates, stateSpinner, gstindto.getGstState());
            }
        });
        asyncTaskStateSpinner.execute();

    }
    //endregion

    public int getStateSelectedPos(List<State> spinnerDtoList, String selectedValue) {

        if (TextUtils.isEmpty(selectedValue) || spinnerDtoList == null)
            return 0;

        for (int i = 0; i < spinnerDtoList.size(); i++) {
            if (selectedValue.equalsIgnoreCase(spinnerDtoList.get(i).getStateId()))
                return i;
        }
        return 0;
    }

    public int getDistrictSelectedPos(List<District> spinnerDtoList, String selectedValue) {

        if (TextUtils.isEmpty(selectedValue) || spinnerDtoList == null)
            return 0;

        for (int i = 0; i < spinnerDtoList.size(); i++) {
            if (selectedValue.equalsIgnoreCase(spinnerDtoList.get(i).getDistrictId()))
                return i;
        }
        return 0;
    }

    public int getVTCSelectedPos(List<Village> spinnerDtoList, String selectedValue) {

        if (TextUtils.isEmpty(selectedValue) || spinnerDtoList == null)
            return 0;

        for (int i = 0; i < spinnerDtoList.size(); i++) {
            if (selectedValue.equalsIgnoreCase(spinnerDtoList.get(i).getVillageId()))
                return i;
        }
        return 0;
    }

    private void setStateSpinnerAdapter(List<State> stateList, Spinner spinnerState, String selStateId) {

        //spinnerState.setEnabled(true);
        spinnerState.setAdapter(new ArrayAdapter<State>(context, android.R.layout.simple_spinner_dropdown_item, stateList));
        int appsstatePos = getStateSelectedPos(stateList, selStateId);
        spinnerState.setSelection(appsstatePos);
        spinnerState.setOnItemSelectedListener(this);

    }

    //region Set District Adapter
    private void setDistrictSpinnerAdapter(List<District> districtList, Spinner spinnerDistrict, String selDistrictId) {

        //spinnerDistrict.setEnabled(true);
        spinnerDistrict.setAdapter(new ArrayAdapter<District>(context, android.R.layout.simple_spinner_dropdown_item, districtList));
        int appsstatePos = getDistrictSelectedPos(districtList, selDistrictId);
        spinnerDistrict.setSelection(appsstatePos);
        spinnerDistrict.setOnItemSelectedListener(this);

    }
    //endregion

    //region Set VTC Adapter
    private void setVTCSpinnerAdapter(List<Village> VTCList, Spinner spinnerVTC, String selVTCId) {

        //spinnerVTC.setEnabled(true);
        spinnerVTC.setAdapter(new ArrayAdapter<Village>(context, android.R.layout.simple_spinner_dropdown_item, VTCList));
        int appsstatePos = getVTCSelectedPos(VTCList, selVTCId);
        spinnerVTC.setSelection(appsstatePos);
        spinnerVTC.setOnItemSelectedListener(this);
    }
    //endregion

    //region AsynTask Call Distrtic
    private void CallDistricAsynTask() {
        selectStateID = gstindto.getGstState();

        asyncTaskDistricSpinner = new AsyncTaskDistricSpinner(GSTDetailsActivity.this, selectStateID, false, new AsyncTaskDistricSpinner.Callback() {
            @Override
            public void onResult(String diplayServerResopnse) {
                System.out.println("asyncTaskDistricSpinner" + diplayServerResopnse);
                StringTokenizer tokens = new StringTokenizer(diplayServerResopnse, "|");
                String first = tokens.nextToken();
                String second = "{" + "\"District\":" + tokens.nextToken() + "}";
                try {
                    arrayListDistrict.removeAll(arrayListDistrict);
                    JSONObject jsnobject = new JSONObject(second);
                    JSONArray jsonArray = jsnobject.getJSONArray("District");
                    arrayListDistrict.add(new District("", "Please Select"));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject explrObject = jsonArray.getJSONObject(i);
                        String a = explrObject.getString("lg_district_id");
                        String b = explrObject.getString("district_name");
                        arrayListDistrict.add(new District(a, b));
                    }
                } catch (Exception e) {
                    e.getMessage();
                }

                setDistrictSpinnerAdapter(arrayListDistrict, districtSpinner, gstindto.getGstDistrict());

            }
        });
        asyncTaskDistricSpinner.execute();
    }
    //endregion

    //region AsynTask Call Village- VTC
    private void CallVillageAsynTask() {
        selectStateID = gstindto.getGstState();
        selectDistricID = gstindto.getGstDistrict();

        asyncTaskVillageSpinner = new AsyncTaskVillageSpinner(GSTDetailsActivity.this, selectStateID, selectDistricID, false, new AsyncTaskVillageSpinner.Callback() {
            @Override
            public void onResult(String diplayServerResopnse) {
                System.out.println("asyncTaskVillageSpinner" + diplayServerResopnse);
                StringTokenizer tokens = new StringTokenizer(diplayServerResopnse, "|");
                String first = tokens.nextToken();
                String second = "{" + "\"Village\":" + tokens.nextToken() + "}";
                try {

                    arrayListVillage.removeAll(arrayListVillage);
                    JSONObject jsnobject = new JSONObject(second);
                    JSONArray jsonArray = jsnobject.getJSONArray("Village");
                    arrayListVillage.add(new Village("", "Please Select"));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String a = obj.getString("lg_village_id");
                        String b = obj.getString("village_name");
                        arrayListVillage.add(new Village(a, b));
                    }
                } catch (Exception e) {
                    e.getMessage();
                }

                setVTCSpinnerAdapter(arrayListVillage, villageSpinner, gstindto.getGstVtc());

            }
        });
        asyncTaskVillageSpinner.execute();
    }
    //endregion

    //region getFranchiseeGSTDetail to server
    private void getFranchiseeGSTDetail() {
        asyncgetFranchiseeGSTDetail = new AsyncasyncgetFranchiseeGSTDetail(GSTDetailsActivity.this, new AsyncasyncgetFranchiseeGSTDetail.Callback() {
            @Override
            public void onResult(String result) {
                try {

                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(GSTDetailsActivity.this, getResources().getString(R.string.Warning));
                        return;
                    }

                    StringTokenizer tokens1 = new StringTokenizer(result, "|");
                    String key = tokens1.nextToken();
                    String response = tokens1.nextToken();

                    if (!key.equalsIgnoreCase(Constants.OKAY_RESPONSE)) {
                        gstindto = new GSTINDTO();
                        AlertDialogBoxInfo.showOkDialog(GSTDetailsActivity.this, response);
                        return;
                    }

                    //reload Data
                    reloadData(response);

                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(GSTDetailsActivity.this, getResources().getString(R.string.Warning));
                }
            }
        });
        asyncgetFranchiseeGSTDetail.execute();
    }
    //endregion

    private void reloadData(String response) {

        Gson gson = new GsonBuilder().create();
        ArrayList<GSTINDTO> gstList = gson.fromJson(response, new TypeToken<ArrayList<GSTINDTO>>() {
        }.getType());

        if (gstList == null || gstList.size() == 0)
            gstindto = new GSTINDTO();
        else
            gstindto = gstList.get(0);

        editTextGSTName.setText(gstindto.getGstEntityName());
        editTextGSTNumber.setText(gstindto.getGstNumber());
        editTextConfirmGstNumber.setText(gstindto.getGstNumber());
        if (!TextUtils.isEmpty(gstindto.getGstNumber()) && gstindto.getGstNumber().length() == 15)
            checkboxAppliedForGSTIN.setVisibility(View.GONE);
        else
            checkboxAppliedForGSTIN.setVisibility(View.VISIBLE);

        //Application Reference Number(ARN)
        editTextTRNNumber.setText(gstindto.getTrnNumber());
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

        //Applied For GSTIN
        String isAppliedForGST = gstindto.getAppliedForGSTIN();
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

        //Date of Application of GST
        if (!TextUtils.isEmpty(gstindto.getGstApplicationDate())) {
            String gstAppDate = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", gstindto.getGstApplicationDate());
            textViewDateOfApplicationGST.setText(gstAppDate);
        }

        //Expected Date of Application of GST
        if (!TextUtils.isEmpty(gstindto.getExpectedDateOfGSTApplication())) {
            String expectedDate = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", gstindto.getExpectedDateOfGSTApplication());
            textViewExpectedDateOfGST.setText(expectedDate);
        }

        //GSTIN Acknowledgement Receipt
        if (!TextUtils.isEmpty(gstindto.getGstinAckReceiptImgId())) {
            String gstUrl = Constants.DownloadImageUrl + gstindto.getGstinAckReceiptImgId();
            Glide.with(context)
                    .load(gstUrl)
                    .apply(new RequestOptions()
                            .error(R.drawable.ic_camera_alt_black_72dp)
                            .placeholder(R.drawable.ic_camera_alt_black_72dp)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgGSTAckReceipt);
        }

        SiteVisitAddress1.setText(gstindto.getGstAddressLine1());
        SiteVisitAddress2.setText(gstindto.getGstAddressLine2());
        SiteVisitLandmark.setText(gstindto.getGstLandmark());
        edtLocality.setText(gstindto.getGstLocality());
        edtArea.setText(gstindto.getGstArea());
        SiteVisitPincode.setText(gstindto.getGstPinCode());

        //GSTIN Image Certificate
        boolean IsGSTImgPDF = (gstindto.getGstImageExt() != null && gstindto.getGstImageExt().equalsIgnoreCase("pdf")) ? true : false;
        if (IsGSTImgPDF) {
            Glide.with(context)
                    .load(R.drawable.pdf)
                    .apply(new RequestOptions()
                            .error(R.drawable.pdf)
                            .placeholder(R.drawable.pdf)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgGSTCertificate);
            //   Glide.with(context).asDrawable().load(context.getResources().getDrawable(R.drawable.pdf)).into(imgPANScanCopy);

        } else {
            final String imageUrl = Constants.DownloadImageUrl + gstindto.getGstImageId();
            if (TextUtils.isEmpty(gstindto.getGstImageId()) || gstindto.getGstImageId().equalsIgnoreCase("0")) {
                Glide.with(context)
                        .applyDefaultRequestOptions(new RequestOptions()
                                .placeholder(R.drawable.ic_camera_alt_black_72dp)
                                .error(R.drawable.ic_camera_alt_black_72dp))
                        .load(imageUrl)
                        .into(imgGSTCertificate);
            } else {
                Bitmap bitmap = CommonUtils.StringToBitMap(gstindto.getGstImage());
                if (bitmap != null) {
                    imgGSTCertificate.setImageBitmap(bitmap);
                } else {
                    imgGSTCertificate.setImageDrawable(getResources().getDrawable(R.drawable.ic_camera_alt_black_72dp));
                }
            }
        }

        //Enable/disable views
        boolean IsEditable = (!TextUtils.isEmpty(gstindto.getIsEditable()) && gstindto.getIsEditable().equalsIgnoreCase("1")) ? true : false;
        checkboxAppliedForGSTIN.setEnabled(IsEditable);
        GUIUtils.setViewAndChildrenEnabled(layoutGSTDetailApplied, IsEditable);
        GUIUtils.setViewAndChildrenEnabled(layoutGSTDetailExist, IsEditable);
        GUIUtils.setViewAndChildrenEnabled(layoutFooter, IsEditable);

        if (IsEditable)
            layoutFooter.setVisibility(View.VISIBLE);
        else
            layoutFooter.setVisibility(View.GONE);

    }

    //region getIndexValue State-District-Village
    private int getIndexValueState(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (((State) spinner.getItemAtPosition(i)).getStateId().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }

    private int getIndexValueDistrict(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (((District) spinner.getItemAtPosition(i)).getDistrictId().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }

    private int getIndexValueVillage(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (((Village) spinner.getItemAtPosition(i)).getVillageId().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }
    //endregion

    //region showImagePreviewDialog
    private void showImagePreviewDialog(Object object) {

        if (customImagePreviewDialog != null && customImagePreviewDialog.isShowing()) {
            customImagePreviewDialog.refresh(object);
            return;
        }

        if (object != null) {
            customImagePreviewDialog = new CustomImageZoomDialog(this, object, new CustomImageZoomDialog.IImagePreviewDialogClicks() {
                @Override
                public void capturePhotoClick() {
                    startCamera(imgGSTCertificate);
                }

                @Override
                public void OkClick(Object object) {
                    GSTINDTO dto = ((GSTINDTO) object);

                    refreshGSTDetailsImg(dto);

                    //Refresh GST Image
                    if (FROM == TYPE_GST_CERTIFICATE) {
                        if (!TextUtils.isEmpty(gstindto.getGstImage())) {
                            Bitmap bitmap = CommonUtils.StringToBitMap(gstindto.getGstImage());
                            if (bitmap != null)
                                imgGSTCertificate.setImageBitmap(bitmap);
                        }
                    } else if (FROM == TYPE_GST_TRN_RECEIPT) {
                        //Refresh Receipt Image
                        if (!TextUtils.isEmpty(gstindto.getGstinAckReceiptImgBase64())) {
                            Bitmap bitmap = CommonUtils.StringToBitMap(gstindto.getGstinAckReceiptImgBase64());
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
            boolean IsEditable = (!TextUtils.isEmpty(gstindto.getIsEditable()) && gstindto.getIsEditable().equalsIgnoreCase("1")) ? true : false;
            customImagePreviewDialog.allowChangePhoto(IsEditable);
            customImagePreviewDialog.allowSaveOption(IsEditable);
            customImagePreviewDialog.allowRemarks(false);

        } else {
            Toast.makeText(this, "No photo available to preview.", Toast.LENGTH_SHORT).show();
        }
    }
    //endregion

    //region startCamera
    public void startCamera(View view) {
        //If the app has not the permission then asking for the permission
        permissionHandler.requestPermission(view, Manifest.permission.CAMERA, getString(R.string.needs_permission_camera_msg), new IPermission() {
            @Override
            public void IsPermissionGranted(boolean IsGranted) {
                if (IsGranted) {
                    try {
                        dispatchTakePictureIntent();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                   /* File file = CommonUtils.getOutputMediaFile(CommonUtils.FILE_IMAGE_TYPE);
                    Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    picUri = Uri.fromFile(file); // create
                    i.putExtra(MediaStore.EXTRA_OUTPUT, picUri); // set the image file
                    i.putExtra("ImageId", picUri); // set the image file
                    startActivityForResult(i, CAMERA_PIC_REQUEST);*/
                }
            }
        });
    }
    //endregion

    private void showDateTimeDialogPicker() {

        Date defaultDate = null;
        if (selectedDateTimeId == R.id.textViewDateOfApplicationGST) {
            defaultDate = gstApplicationDate;
        } else if (selectedDateTimeId == R.id.textViewExpectedDateOfGST) {
            defaultDate = expectedGstApplicationDate;
        }

        // Show DateTime Picker Dialog.
        dateTimePickerDialog = new DateTimePickerDialog(this, true, defaultDate, new DateTimePickerDialog.IDateTimePicker() {
            @Override
            public void getDateTime(Date datetime, String defaultFormattedDateTime) {
                try {
                    String formatedDate = dateFormatter2.format(datetime);
                    String formateYMD = dateFormatterYMD.format(datetime);
                    //Toast.makeText(GSTDetailsActivity.this, "Selected DateTime : " + formatedDate, Toast.LENGTH_LONG).show();

                    if (selectedDateTimeId != 0) {
                        TextView textViewDateTime = (TextView) findViewById(selectedDateTimeId);
                        textViewDateTime.setText(formateYMD);

                        if (selectedDateTimeId == R.id.textViewDateOfApplicationGST) {
                            gstApplicationDate = datetime;
                            strGstApplicationDate = formateYMD;
                            gstindto.setGstApplicationDate(strGstApplicationDate);

                            String DOB = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", gstindto.getGstApplicationDate());
                            textViewDateOfApplicationGST.setText(DOB);
                            textViewDateOfApplicationGST.setError(null);

                        } else if (selectedDateTimeId == R.id.textViewExpectedDateOfGST) {
                            expectedGstApplicationDate = datetime;
                            strExpectedGstApplicationDate = formateYMD;
                            gstindto.setExpectedDateOfGSTApplication(strExpectedGstApplicationDate);

                            String DOB = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", gstindto.getExpectedDateOfGSTApplication());
                            textViewExpectedDateOfGST.setText(DOB);
                            textViewExpectedDateOfGST.setError(null);

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

        }

        dateTimePickerDialog.setCancelable(false);
        dateTimePickerDialog.setActionButtonName("Save");
        dateTimePickerDialog.show();

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");

        storageDir.mkdirs();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile(); //CommonUtils.getOutputMediaFile(1);
            } catch (Exception ex) {
                ex.printStackTrace();
                // Error occurred while creating the File
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                //picUri = Uri.fromFile(createImageFile());
                picUri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
                startActivityForResult(takePictureIntent, CAMERA_PIC_REQUEST);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK) {
            try {
                Uri imageUri = Uri.parse(mCurrentPhotoPath);
                getLocationAndTimestamp();
                Bitmap bitmapNew = ImageUtils.getBitmap(getContentResolver(), imageUri, latitude, longitude, currentTimestamp);

                //BitMap with TimeStamp on it
                bitmapNew = ImageUtils.stampWithTimeInBitmap(bitmapNew);
                String imageBase64 = ImageUtils.updateExifData(imageUri, bitmapNew, latitude, longitude, currentTimestamp);

                ImageView imageView = new ImageView(context);
                imageView.setImageBitmap(bitmapNew);
                if (!CommonUtils.isLandscapePhoto(imageUri, imageView)) {
                    AlertDialogBoxInfo.alertDialogShow(context, getString(R.string.landscape_mode_allowed));
                    return;
                }

                setImageData(imageUri, imageBase64, bitmapNew);

            } catch (Exception e) {
                e.printStackTrace();
                AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
            }
        } else if (requestCode == BROWSE_FOLDER_REQUEST && resultCode == Activity.RESULT_OK) {
            try {
                // Use the provided utility method to parse the result
                List<Uri> files = Utils.getSelectedFilesFromResult(data);
                for (Uri uri : files) {
                    File file = Utils.getFileForUri(uri);

                    //Check File size
                    int fileSize = CommonUtils.getFileSizeInMB(file);
                    if (fileSize > 1) {
                        AlertDialogBoxInfo.alertDialogShow(GSTDetailsActivity.this, getString(R.string.file_size_msg));
                        return;
                    }

                    String ext = FileUtils.getFileExtension(context, uri);
                    String base64Data = CommonUtils.encodeFileToBase64Binary(file);

                    gstindto.setGstImage(base64Data);
                    gstindto.setGstImageExt(ext);
                    gstindto.setChangedPhoto(true);

                    Glide.with(context).asDrawable().load(context.getResources().getDrawable(R.drawable.pdf)).into(imgGSTCertificate);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setImageData(Uri imageUri, String imageBase64, Bitmap bitmapNew) {

        GSTINDTO imageDto = new GSTINDTO();
        imageDto.setUri(imageUri);
        imageDto.setBitmap(bitmapNew);
        imageDto.setGstImageExt("pdf");
        imageDto.setChangedPhoto(true);
        imageDto.setGstImage(imageBase64);

        showImagePreviewDialog((Object) imageDto);
    }

    private void refreshGSTDetailsImg(GSTINDTO imageDto) {

        if (FROM == TYPE_GST_CERTIFICATE) {

            gstindto.setChangedPhoto(imageDto.isChangedPhoto());
            gstindto.setGstImageId(imageDto.getGstImageId());
            gstindto.setBitmap(imageDto.getBitmap());
            gstindto.setGstImageExt("jpg");
            gstindto.setName("");
            gstindto.setGstImage(imageDto.getGstImage());

        } else if (FROM == TYPE_GST_TRN_RECEIPT) {

            gstindto.setReceiptImgChanged(imageDto.isChangedPhoto());
            gstindto.setGstinAckReceiptImgId(imageDto.getGstImageId());
            gstindto.setBitmapReceipt(imageDto.getBitmap());
            gstindto.setReceiptName("");
            gstindto.setGstinAckReceiptImgBase64(imageDto.getGstImage());

        }
    }

    private GSTINDTO prepareDtoForPreview(int FROM) {
        GSTINDTO imageDto = new GSTINDTO();

        if (FROM == TYPE_GST_CERTIFICATE) {

            imageDto.setChangedPhoto(gstindto.isChangedPhoto());
            imageDto.setGstImageId(gstindto.getGstImageId());
            imageDto.setBitmap(gstindto.getBitmap());
            imageDto.setName("");
            imageDto.setGstImage(gstindto.getGstImage());

        } else if (FROM == TYPE_GST_TRN_RECEIPT) {

            imageDto.setChangedPhoto(gstindto.isReceiptImgChanged());
            imageDto.setGstImageId(gstindto.getGstinAckReceiptImgId());
            imageDto.setBitmap(gstindto.getBitmapReceipt());
            imageDto.setName("");
            imageDto.setGstImage(gstindto.getGstinAckReceiptImgBase64());

        }
        return imageDto;
    }

    public void getLocationAndTimestamp() {
        //Get Current location and time stamp
        if (gpsTracker.canGetLocation()) {
            latitude = String.valueOf(gpsTracker.getLatitude());
            longitude = String.valueOf(gpsTracker.getLongitude());
            currentTimestamp = gpsTracker.getFormattedDateTime();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                if (connection.getVkid().toUpperCase().startsWith("VL") || connection.getVkid().toUpperCase().startsWith("VA")) {
                    super.onBackPressed();
                    Intent intent = new Intent(this, MyVakrangeeKendraLocationDetailsNextGen.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                } else {
                    // customNavigationBar.openDrawer();
                    Intent intent = new Intent(this, DashboardActivity.class);
                    startActivity(intent);
                }
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();
        if (Id == R.id.btnSubmitGSTDetails) {

            GSTINDTO gstindto = getGSTINDetailsDto();

            int status = ValidateGSTAsPerSelection();
            if (status == 0) {
                submitGSTDetails(gstindto);
            }
        } else if (Id == R.id.btnClear) {

            ClearFiledData(editTextGSTName);
            gstindto.setGstEntityName("");

            ClearFiledData(editTextGSTNumber);
            ClearFiledData(editTextConfirmGstNumber);
            gstindto.setGstNumber("");

            ClearFiledData(SiteVisitAddress1);
            gstindto.setGstAddressLine1("");

            ClearFiledData(SiteVisitAddress2);
            gstindto.setGstAddressLine2("");

            ClearFiledData(SiteVisitLandmark);
            gstindto.setGstLandmark("");

            ClearFiledData(edtArea);
            gstindto.setGstArea("");

            ClearFiledData(edtLocality);
            gstindto.setGstLocality("");

            imgGSTCertificate.setImageBitmap(null);
            imgGSTCertificate.setImageDrawable(getResources().getDrawable(R.drawable.ic_camera_alt_black_72dp));
            gstindto.setGstImage("");
            gstindto.setBitmap(null);

            ClearFiledData(editTextTRNNumber);
            gstindto.setTrnNumber("");

            textViewDateOfApplicationGST.setText("");
            gstindto.setGstApplicationDate("");

            textViewExpectedDateOfGST.setText("");
            gstindto.setExpectedDateOfGSTApplication("");

            imgGSTAckReceipt.setImageBitmap(null);
            imgGSTAckReceipt.setImageDrawable(getResources().getDrawable(R.drawable.ic_camera_alt_black_72dp));
            gstindto.setBitmapReceipt(null);
            gstindto.setGstinAckReceiptImgBase64("");

        } else if (Id == R.id.btnCancel) {
            Intent intent = new Intent(GSTDetailsActivity.this, DashboardActivity.class);
            startActivity(intent);
        }
    }

    private void ClearFiledData(EditText editText) {
        editText.setText("");
        editText.setError(null);
    }

    public GSTINDTO getGSTINDetailsDto() {

        if (gstindto == null)
            return null;

        gstindto.setGstEntityName(editTextGSTName.getText().toString());
        gstindto.setGstNumber(editTextGSTNumber.getText().toString());
        gstindto.setGstAddressLine1(SiteVisitAddress1.getText().toString());
        gstindto.setGstAddressLine2(SiteVisitAddress2.getText().toString());
        gstindto.setGstLandmark(SiteVisitLandmark.getText().toString());
        gstindto.setGstLocality(edtLocality.getText().toString());
        gstindto.setGstArea(edtArea.getText().toString());
        gstindto.setGstPinCode(SiteVisitPincode.getText().toString());
        gstindto.setTrnNumber(editTextTRNNumber.getText().toString());

        return gstindto;
    }

    //region updateFranchiseeGSTDetail to server
    public void submitGSTDetails(GSTINDTO gstindto) {

        asyncGSTDetails = new AsyncupdateFranchiseeGSTDetail(GSTDetailsActivity.this, gstindto, new AsyncupdateFranchiseeGSTDetail.Callback() {
            @Override
            public void onResult(String result) {
                try {
                    //Check if response if null or empty
                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                        return;
                    }

                    //Check response
                    if (result.startsWith("ERROR|")) {

                        StringTokenizer tokens = new StringTokenizer(result, "|");
                        tokens.nextToken();     // Jump to next Token
                        String errMsg = tokens.nextToken();

                        if (!TextUtils.isEmpty(errMsg)) {
                            AlertDialogBoxInfo.alertDialogShow(context, errMsg);
                        }
                        return;
                    }

                    //Process response
                    if (result.startsWith("OKAY|")) {

                        //Clear SetError of Date
                        textViewDateOfApplicationGST.setText("");
                        textViewDateOfApplicationGST.setError(null);
                        textViewExpectedDateOfGST.setText("");
                        textViewExpectedDateOfGST.setError(null);

                        AlertDialogBoxInfo.alertDialogShow(GSTDetailsActivity.this, getResources().getString(R.string.Gstin_Success));
                        getFranchiseeGSTDetail();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(GSTDetailsActivity.this, getResources().getString(R.string.Warning));
                }
            }
        });
        asyncGSTDetails.execute();
    }
    //endregion

    //region GSTIN data validation
    private int ValidateGSTAsPerSelection() {

        if (!TextUtils.isEmpty(gstindto.getAppliedForGSTIN()) && gstindto.getAppliedForGSTIN().equalsIgnoreCase("1")) {
            return IsAppliedForGSTDetailsValidated();
        }

        return IsHavingGSTDetailsValidated();
    }

    public int IsHavingGSTDetailsValidated() {

        //Step 1: Validate GST Name
        gstindto.setGstEntityName(editTextGSTName.getText().toString().trim());
        if (TextUtils.isEmpty(gstindto.getGstEntityName())) {
            Toast.makeText(context, "Please enter Name of Entity on GSTIN Certificate.", Toast.LENGTH_LONG).show();
            editTextGSTName.setError("Please enter Name of Entity on GSTIN Certificate.");
            return 1;
        }

        //Step 2: Validate GST Number
        gstindto.setGstNumber(editTextGSTNumber.getText().toString().trim());
        if (TextUtils.isEmpty(gstindto.getGstNumber()) || !CommonUtils.GSTINisValid(editTextGSTNumber.getText().toString())) {
            Toast.makeText(context, "Please enter valid GSTIN Number.", Toast.LENGTH_LONG).show();
            editTextGSTNumber.setError("Please enter valid GSTIN Number.");
            return 1;
        }

        //STEP 2.1: Confirm GSTIN Number
        if (TextUtils.isEmpty(gstindto.getGstNumber()) || !CommonUtils.GSTINisValid(editTextConfirmGstNumber.getText().toString())) {
            Toast.makeText(context, "Please Confirm GSTIN Number.", Toast.LENGTH_LONG).show();
            editTextConfirmGstNumber.setError("Please Confirm GSTIN Number.");
            return 1;
        }

        //Step 3.1: Address Line 1
        gstindto.setGstAddressLine1(SiteVisitAddress1.getText().toString().trim());
        if (TextUtils.isEmpty(gstindto.getGstAddressLine1())) {
            Toast.makeText(context, "Please enter Address Line 1.", Toast.LENGTH_LONG).show();
            SiteVisitAddress1.setError("Please enter Address Line 1.");
            return 1;
        }

        //Step 3.2: Address Line 2
        gstindto.setGstAddressLine2(SiteVisitAddress2.getText().toString().trim());
        if (TextUtils.isEmpty(gstindto.getGstAddressLine2())) {
            Toast.makeText(context, "Please enter Address Line 2.", Toast.LENGTH_LONG).show();
            SiteVisitAddress2.setError("Please enter Address Line 2.");
            return 1;
        }

        //STEP 4: State
        if (TextUtils.isEmpty(gstindto.getGstState()) || gstindto.getGstState().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select State.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(stateSpinner, "Please select State.", context);
            return 1;
        }

        //STEP 5: District
        if (TextUtils.isEmpty(gstindto.getGstDistrict()) || gstindto.getGstDistrict().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select District.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(districtSpinner, "Please select District.", context);
            return 1;
        }

        //STEP 6: VTC
        if (TextUtils.isEmpty(gstindto.getGstVtc()) || gstindto.getGstVtc().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select VTC.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(villageSpinner, "Please select VTC.", context);
            return 1;
        }

        //STEP 8: Pin code
        gstindto.setGstPinCode(SiteVisitPincode.getText().toString().trim());
        if (TextUtils.isEmpty(gstindto.getGstPinCode())) {
            Toast.makeText(context, "Please enter Pin code.", Toast.LENGTH_LONG).show();
            SiteVisitPincode.setError("Please enter Pin code.");
            return 1;
        }

        //STEP 9: GSTIN Certificate Image
        if (TextUtils.isEmpty(gstindto.getGstImageId()) || gstindto.getGstImageId().equalsIgnoreCase("0")) {
            if (TextUtils.isEmpty(gstindto.getGstImage())) {
                AlertDialogBoxInfo.alertDialogShow(GSTDetailsActivity.this, "Please add GSTIN Certificate Image.");
                return 1;
            }
        }
        return 0;
    }

    public int IsAppliedForGSTDetailsValidated() {

        //Step 1: Validate Application Reference Number(ARN)
        gstindto.setTrnNumber(editTextTRNNumber.getText().toString().trim());
        if (TextUtils.isEmpty(gstindto.getTrnNumber()) || !CommonUtils.isValidARN(editTextTRNNumber.getText().toString())) {
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
        if (TextUtils.isEmpty(gstindto.getGstApplicationDate())) {
            Toast.makeText(context, "Please select Date Of GSTIN Application.", Toast.LENGTH_LONG).show();
            textViewDateOfApplicationGST.setError("Please select Date Of GSTIN Application.");
            return 15;
        }

        //STEP 3: Expected Date Of GSTIN Application
        if (TextUtils.isEmpty(gstindto.getExpectedDateOfGSTApplication())) {
            Toast.makeText(context, "Please select Expected Date Of GSTIN Application.", Toast.LENGTH_LONG).show();
            textViewExpectedDateOfGST.setError("Please select Expected Date Of GSTIN Application.");
            return 15;
        }

        //STEP 4: GSTIN Acknowledgement Image
        if (TextUtils.isEmpty(gstindto.getGstinAckReceiptImgId()) || gstindto.getGstinAckReceiptImgId().equalsIgnoreCase("0")) {
            if (TextUtils.isEmpty(gstindto.getGstinAckReceiptImgBase64())) {
                AlertDialogBoxInfo.alertDialogShow(GSTDetailsActivity.this, "Please add GSTIN Acknowledgement Receipt Image.");
                return 1;
            }
        }
        return 0;
    }

    //endregion

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int Id = parent.getId();

        if (Id == R.id.spinnerState) {
            if (position > 0) {
                stateSpinner.setTitle("Select State");
                stateSpinner.requestFocus();
                State stateDto = (State) stateSpinner.getItemAtPosition(position);
                if (!stateDto.getStateId().equals("0")) {
                    gstindto.setGstState(stateDto.getStateId());

                    //Get Division-District
                    CallDistricAsynTask();
                }
            } else {
                List<District> list1 = new ArrayList<District>();
                list1.add(0, new District("0", "Please Select"));

                //DISTRICT
                //districtSpinner.setEnabled(true);
                setDistrictSpinnerAdapter(list1, districtSpinner, null);
                gstindto.setGstDistrict(null);

                //VTC
                List<Village> vtclist1 = new ArrayList<Village>();
                vtclist1.add(0, new Village("0", "Please Select"));
                //villageSpinner.setEnabled(true);
                setVTCSpinnerAdapter(vtclist1, villageSpinner, null);
                gstindto.setGstVtc(null);

            }
        } else if (Id == R.id.spinnerDistrict) {
            if (position > 0) {
                districtSpinner.setTitle("Select District");
                districtSpinner.requestFocus();
                District disDto = (District) districtSpinner.getItemAtPosition(position);
                if (!disDto.getDistrictId().equals("0")) {
                    gstindto.setGstDistrict(disDto.getDistrictId());

                    //Get BLOCK
                    CallVillageAsynTask();

                }
            } else {
                List<Village> list1 = new ArrayList<Village>();
                list1.add(0, new Village("0", "Please Select"));

                //VTC
                //villageSpinner.setEnabled(true);
                setVTCSpinnerAdapter(list1, villageSpinner, null);
                gstindto.setGstVtc(null);

            }
        } else if (Id == R.id.spinnerVTC) {
            if (position > 0) {
                villageSpinner.setTitle("Select VTC");
                villageSpinner.requestFocus();
                Village entityDto = (Village) villageSpinner.getItemAtPosition(position);
                gstindto.setGstVtc(entityDto.getVillageId());

            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void showImgOrPDF(View view) {
        FROM = TYPE_GST_CERTIFICATE;

        boolean IsGSTImgPDF = (gstindto.getGstImageExt() != null && gstindto.getGstImageExt().equalsIgnoreCase("pdf")) ? true : false;
        boolean IsEditable = (!TextUtils.isEmpty(gstindto.getIsEditable()) && gstindto.getIsEditable().equalsIgnoreCase("1")) ? true : false;

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
                            String ext = TextUtils.isEmpty(gstindto.getGstImageExt()) ? "jpg" : gstindto.getGstImageExt();

                            //FOR PDF
                            if (ext.equalsIgnoreCase("pdf")) {
                                startCamera(view);
                                return;
                            }

                            //FOR Image
                            if ((TextUtils.isEmpty(gstindto.getGstImageId()) || gstindto.getGstImageId().equalsIgnoreCase("0")) && TextUtils.isEmpty(gstindto.getGstImage())) {
                                startCamera(view);
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

}
