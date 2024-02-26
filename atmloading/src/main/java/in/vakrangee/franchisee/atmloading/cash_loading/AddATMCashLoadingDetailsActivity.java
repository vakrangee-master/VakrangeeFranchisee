package in.vakrangee.franchisee.atmloading.cash_loading;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.button.MaterialButton;
import com.nononsenseapps.filepicker.Utils;
import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;

import java.io.File;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import in.vakrangee.franchisee.atmloading.R;
import in.vakrangee.supercore.franchisee.baseutils.BaseAppCompatActivity;
import in.vakrangee.supercore.franchisee.baseutils.IHandleOkButton;
import in.vakrangee.supercore.franchisee.commongui.DateTimePickerDialog;
import in.vakrangee.supercore.franchisee.commongui.FileAttachmentDialog;
import in.vakrangee.supercore.franchisee.commongui.FileUtils;
import in.vakrangee.supercore.franchisee.commongui.FilteredFilePickerActivity;
import in.vakrangee.supercore.franchisee.commongui.animation.AnimationHanndler;
import in.vakrangee.supercore.franchisee.commongui.imagepreview.CustomImageZoomDialogDM;
import in.vakrangee.supercore.franchisee.gstdetails.GSTINDTO;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.GUIUtils;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.ImageZipper;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;

public class AddATMCashLoadingDetailsActivity extends BaseAppCompatActivity {

    private Toolbar toolbar;
    private Context context;
    private DeprecateHandler deprecateHandler;
    private PermissionHandler permissionHandler;
    private ATMCashLoadingDto atmCashLoadingDto;
    private TextView txtPhysicalCashbl, txtCashLoadedbl, txtCashRemovedbl, txtClosingbl, txtCashLoadingDateLbl;

    //Physical Cash
    private EditText edittextPhyscialC1, edittextPhyscialC2, edittextPhyscialC3, edittextPhyscialC4;
    private TextView textPhyscialC1, textPhyscialC2, textPhyscialC3, textPhyscialC4, textTotalNoteCountPhyscial, textTotalAmountCountPhyscial;

    //Cash Loaded
    private EditText edittextCashLoadedC1, edittextCashLoadedC2, edittextCashLoadedC3, edittextCashLoadedC4;
    private TextView textCashLoadedC1, textCashLoadedC2, textCashLoadedC3, textCashLoadedC4, textTotalNoteCountCashLoaded, textTotalAmountCountCashLoaded;

    //Cash Removed
    private EditText edittextCashRemovedC1, edittextCashRemovedC2, edittextCashRemovedC3, edittextCashRemovedC4;
    private TextView textCashRemovedC1, textCashRemovedC2, textCashRemovedC3, textCashRemovedC4, textTotalNoteCountCashRemoved, textTotalAmountCountCashRemoved;

    //Cash Closing
    private EditText edittextClosingC1, edittextClosingC2, edittextClosingC3, edittextClosingC4;
    private TextView textClosingC1, textClosingC2, textClosingC3, textClosingC4, textTotalNoteCountClosing, textTotalAmountCountClosing;

    private TextView txtATMCounterBeforeLbl, txtATMCounterAfterLbl, txtSwitchCounterBeforeLbl, txtSwitchCounterAfterLbl;
    private ImageView imgATMCounterBefore, imgATMCounterAfter, imgSwitchCounterBefore, imgSwitchCounterAfter;

    private EditText editTextRemarks;
    private MaterialButton btnCancel, btnSubmit;
    private TextView textViewCashLoadingDate;

    //Date
    private DateTimePickerDialog dateTimePickerDialog;
    private DateFormat dateFormatter2 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    private DateFormat dateFormatterYMD = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private Date loadingDate;
    private String strLoadingDate;

    private int selectedDateTimeId = 0;
    private AsyncSaveCashLoadingDetails asyncSaveCashLoadingDetails = null;

    private Uri picUri;                 //Picture URI
    private String SEL_FILE_TYPE = "images";
    private int FROM = -1;

    //img
    private static final int SCANLIB_REQUEST_CODE = 99;
    private static final int BROWSE_FOLDER_REQUEST = 101;
    private String[] permissions = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int ATM_BEFORE_SCAN_COPY = 1;
    private static final int ATM_AFTER_SCAN_COPY = 2;
    private static final int SWITCH_BEFORE_COPY = 3;
    private static final int SWITCH_AFTER_COPY = 4;
    private FileAttachmentDialog fileAttachementDialog = null;
    private CustomImageZoomDialogDM customImagePreviewDialog = null;
    private LinearLayout layoutLoading1, layoutClosing, layoutLoading2, layoutFooter;
    private String INITIAL_VALUE = "0";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_atm_cash_loading);

        context = this;
        permissionHandler = new PermissionHandler(this);
        deprecateHandler = new DeprecateHandler(context);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            toolbar.setTitleTextColor(Color.WHITE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + getResources().getString(R.string.add_atm_cash_loading) + "</small>"));

        }

        txtCashLoadingDateLbl = findViewById(R.id.txtCashLoadingDateLbl);
        textViewCashLoadingDate = findViewById(R.id.textViewCashLoadingDate);
        txtATMCounterBeforeLbl = findViewById(R.id.txtATMCounterBeforeLbl);
        imgATMCounterBefore = findViewById(R.id.imgATMCounterBefore);
        txtATMCounterAfterLbl = findViewById(R.id.txtATMCounterAfterLbl);
        imgATMCounterAfter = findViewById(R.id.imgATMCounterAfter);
        txtSwitchCounterBeforeLbl = findViewById(R.id.txtSwitchCounterBeforeLbl);
        imgSwitchCounterBefore = findViewById(R.id.imgSwitchCounterBefore);
        txtSwitchCounterAfterLbl = findViewById(R.id.txtSwitchCounterAfterLbl);
        imgSwitchCounterAfter = findViewById(R.id.imgSwitchCounterAfter);
        editTextRemarks = findViewById(R.id.editTextRemarks);
        layoutClosing = findViewById(R.id.layoutClosing);
        layoutLoading1 = findViewById(R.id.layoutLoading1);
        layoutLoading2 = findViewById(R.id.layoutLoading2);
        layoutFooter = findViewById(R.id.layoutFooter);

        //ATM Counter Before
        imgATMCounterBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FROM = ATM_BEFORE_SCAN_COPY;
                showImgOrPDF(v);
            }
        });

        //ATM Counter After
        imgATMCounterAfter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FROM = ATM_AFTER_SCAN_COPY;
                showImgOrPDF(v);
            }
        });

        //Switch Counter Before
        imgSwitchCounterBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FROM = SWITCH_BEFORE_COPY;
                showImgOrPDF(v);
            }
        });

        //Switch Counter After
        imgSwitchCounterAfter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FROM = SWITCH_AFTER_COPY;
                showImgOrPDF(v);
            }
        });

        //Cash Loading Date
        textViewCashLoadingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDateTimeId = v.getId();
                showDateTimeDialogPicker(v);
            }
        });

        //Cancel
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPressed();
            }
        });

        //Submit
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationHanndler.bubbleAnimation(context, v);

                int status = IsATMLoadingDetailsValidated();
                if (status != 0)
                    return;

                //Save
                saveCashLoadingDetails();
            }
        });

        //Physical
        txtPhysicalCashbl = findViewById(R.id.txtPhysicalCashbl);
        edittextPhyscialC1 = findViewById(R.id.edittextPhyscialC1);
        edittextPhyscialC2 = findViewById(R.id.edittextPhyscialC2);
        edittextPhyscialC3 = findViewById(R.id.edittextPhyscialC3);
        edittextPhyscialC4 = findViewById(R.id.edittextPhyscialC4);
        textPhyscialC1 = findViewById(R.id.textPhyscialC1);
        textPhyscialC2 = findViewById(R.id.textPhyscialC2);
        textPhyscialC3 = findViewById(R.id.textPhyscialC3);
        textPhyscialC4 = findViewById(R.id.textPhyscialC4);
        textTotalNoteCountPhyscial = findViewById(R.id.textTotalNoteCountPhyscial);
        textTotalAmountCountPhyscial = findViewById(R.id.textTotalAmountCountPhyscial);

        //Cash Loaded
        txtCashLoadedbl = findViewById(R.id.txtCashLoadedbl);
        edittextCashLoadedC1 = findViewById(R.id.edittextCashLoadedC1);
        textCashLoadedC1 = findViewById(R.id.textCashLoadedC1);
        edittextCashLoadedC2 = findViewById(R.id.edittextCashLoadedC2);
        textCashLoadedC2 = findViewById(R.id.textCashLoadedC2);
        edittextCashLoadedC3 = findViewById(R.id.edittextCashLoadedC3);
        textCashLoadedC3 = findViewById(R.id.textCashLoadedC3);
        edittextCashLoadedC4 = findViewById(R.id.edittextCashLoadedC4);
        textCashLoadedC4 = findViewById(R.id.textCashLoadedC4);
        textTotalNoteCountCashLoaded = findViewById(R.id.textTotalNoteCountCashLoaded);
        textTotalAmountCountCashLoaded = findViewById(R.id.textTotalAmountCountCashLoaded);

        //Cash Removed
        txtCashRemovedbl = findViewById(R.id.txtCashRemovedbl);
        edittextCashRemovedC1 = findViewById(R.id.edittextCashRemovedC1);
        textCashRemovedC1 = findViewById(R.id.textCashRemovedC1);
        edittextCashRemovedC2 = findViewById(R.id.edittextCashRemovedC2);
        textCashRemovedC2 = findViewById(R.id.textCashRemovedC2);
        edittextCashRemovedC3 = findViewById(R.id.edittextCashRemovedC3);
        textCashRemovedC3 = findViewById(R.id.textCashRemovedC3);
        edittextCashRemovedC4 = findViewById(R.id.edittextCashRemovedC4);
        textCashRemovedC4 = findViewById(R.id.textCashRemovedC4);
        textTotalNoteCountCashRemoved = findViewById(R.id.textTotalNoteCountCashRemoved);
        textTotalAmountCountCashRemoved = findViewById(R.id.textTotalAmountCountCashRemoved);

        //Cash Closing
        txtClosingbl = findViewById(R.id.txtClosingbl);
        edittextClosingC1 = findViewById(R.id.edittextClosingC1);
        textClosingC1 = findViewById(R.id.textClosingC1);
        edittextClosingC2 = findViewById(R.id.edittextClosingC2);
        textClosingC2 = findViewById(R.id.textClosingC2);
        edittextClosingC3 = findViewById(R.id.edittextClosingC3);
        textClosingC3 = findViewById(R.id.textClosingC3);
        edittextClosingC4 = findViewById(R.id.edittextClosingC4);
        textClosingC4 = findViewById(R.id.textClosingC4);
        textTotalNoteCountClosing = findViewById(R.id.textTotalNoteCountClosing);
        textTotalAmountCountClosing = findViewById(R.id.textTotalAmountCountClosing);

        TextView[] txtViewsForCompulsoryMark = {txtCashLoadingDateLbl, txtPhysicalCashbl, txtCashLoadedbl, txtCashRemovedbl, txtClosingbl, txtATMCounterBeforeLbl, txtATMCounterAfterLbl, txtSwitchCounterBeforeLbl, txtSwitchCounterAfterLbl};
        GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);

        //Physical Cash
        editTextAmountEnter(edittextPhyscialC1, textPhyscialC1, Long.valueOf(2000));
        editTextAmountEnter(edittextPhyscialC2, textPhyscialC2, Long.valueOf(500));
        editTextAmountEnter(edittextPhyscialC3, textPhyscialC3, Long.valueOf(100));
        editTextAmountEnter(edittextPhyscialC4, textPhyscialC4, Long.valueOf(100));

        edittextPhyscialC1.setFilters(new InputFilter[]{new CommonUtils.InputFilterMinMax(INITIAL_VALUE, "2500")});
        edittextPhyscialC2.setFilters(new InputFilter[]{new CommonUtils.InputFilterMinMax(INITIAL_VALUE, "2500")});
        edittextPhyscialC3.setFilters(new InputFilter[]{new CommonUtils.InputFilterMinMax(INITIAL_VALUE, "5000")});
        edittextPhyscialC4.setFilters(new InputFilter[]{new CommonUtils.InputFilterMinMax(INITIAL_VALUE, "2500")});

        //Cash Loaded
        editTextAmountEnter(edittextCashLoadedC1, textCashLoadedC1, Long.valueOf(2000));
        editTextAmountEnter(edittextCashLoadedC2, textCashLoadedC2, Long.valueOf(500));
        editTextAmountEnter(edittextCashLoadedC3, textCashLoadedC3, Long.valueOf(100));
        editTextAmountEnter(edittextCashLoadedC4, textCashLoadedC4, Long.valueOf(100));

        edittextCashLoadedC1.setFilters(new InputFilter[]{new CommonUtils.InputFilterMinMax(INITIAL_VALUE, "2500")});
        edittextCashLoadedC2.setFilters(new InputFilter[]{new CommonUtils.InputFilterMinMax(INITIAL_VALUE, "2500")});
        edittextCashLoadedC3.setFilters(new InputFilter[]{new CommonUtils.InputFilterMinMax(INITIAL_VALUE, "5000")});
        edittextCashLoadedC4.setFilters(new InputFilter[]{new CommonUtils.InputFilterMinMax(INITIAL_VALUE, "2500")});

        //Cash Removed
        editTextAmountEnter(edittextCashRemovedC1, textCashRemovedC1, Long.valueOf(2000));
        editTextAmountEnter(edittextCashRemovedC2, textCashRemovedC2, Long.valueOf(500));
        editTextAmountEnter(edittextCashRemovedC3, textCashRemovedC3, Long.valueOf(100));
        editTextAmountEnter(edittextCashRemovedC4, textCashRemovedC4, Long.valueOf(100));

        edittextCashRemovedC1.setFilters(new InputFilter[]{new CommonUtils.InputFilterMinMax(INITIAL_VALUE, "2500")});
        edittextCashRemovedC2.setFilters(new InputFilter[]{new CommonUtils.InputFilterMinMax(INITIAL_VALUE, "2500")});
        edittextCashRemovedC3.setFilters(new InputFilter[]{new CommonUtils.InputFilterMinMax(INITIAL_VALUE, "5000")});
        edittextCashRemovedC4.setFilters(new InputFilter[]{new CommonUtils.InputFilterMinMax(INITIAL_VALUE, "2500")});

        //Cash Closing
        editTextAmountEnterClosing(edittextClosingC1, textClosingC1, Long.valueOf(2000));
        editTextAmountEnterClosing(edittextClosingC2, textClosingC2, Long.valueOf(500));
        editTextAmountEnterClosing(edittextClosingC3, textClosingC3, Long.valueOf(100));
        editTextAmountEnterClosing(edittextClosingC4, textClosingC4, Long.valueOf(100));

        edittextClosingC1.setFilters(new InputFilter[]{new CommonUtils.InputFilterMinMax(INITIAL_VALUE, "2500")});
        edittextClosingC2.setFilters(new InputFilter[]{new CommonUtils.InputFilterMinMax(INITIAL_VALUE, "2500")});
        edittextClosingC3.setFilters(new InputFilter[]{new CommonUtils.InputFilterMinMax(INITIAL_VALUE, "5000")});
        edittextClosingC4.setFilters(new InputFilter[]{new CommonUtils.InputFilterMinMax(INITIAL_VALUE, "2500")});

        atmCashLoadingDto = (ATMCashLoadingDto) getIntent().getSerializableExtra("CASH_LOADING_DETAILS");
        bindData();

    }

    private void saveCashLoadingDetails() {
        asyncSaveCashLoadingDetails = new AsyncSaveCashLoadingDetails(context, atmCashLoadingDto, new AsyncSaveCashLoadingDetails.Callback() {
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
                        String msg = "ATM Cash Loading Details saved Successfully.";
                        showMessageWithFinish(msg, new IHandleOkButton() {
                            @Override
                            public void onOkClick() {
                                setResult(true);
                            }
                        });
                        return;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                }
            }
        });

        asyncSaveCashLoadingDetails.execute("");
    }

    public int IsATMLoadingDetailsValidated() {

        //Cash Loading Date
        if (TextUtils.isEmpty(atmCashLoadingDto.getLoadingDate())) {
            Toast.makeText(context, "Please select Cash Loading Date.", Toast.LENGTH_LONG).show();
            textViewCashLoadingDate.setError("Please select Cash Loading Date.");
            return 1;
        }

        //Physical Cash
        // Cassette 1
        atmCashLoadingDto.setPhysicalC1(edittextPhyscialC1.getText().toString().trim());
        if (TextUtils.isEmpty(atmCashLoadingDto.getPhysicalC1())) {
            Toast.makeText(context, "Please enter note", Toast.LENGTH_LONG).show();
            edittextPhyscialC1.setError("Please enter note.");
            return 5;
        }

        // Cassette 2
        atmCashLoadingDto.setPhysicalC2(edittextPhyscialC2.getText().toString().trim());
        if (TextUtils.isEmpty(atmCashLoadingDto.getPhysicalC2())) {
            Toast.makeText(context, "Please enter note", Toast.LENGTH_LONG).show();
            edittextPhyscialC2.setError("Please enter note.");
            return 6;
        }

        // Cassette 3
        atmCashLoadingDto.setPhysicalC3(edittextPhyscialC3.getText().toString().trim());
        if (TextUtils.isEmpty(atmCashLoadingDto.getPhysicalC3())) {
            Toast.makeText(context, "Please enter note", Toast.LENGTH_LONG).show();
            edittextPhyscialC3.setError("Please enter note.");
            return 7;
        }

        // Cassette 4
        atmCashLoadingDto.setPhysicalC4(edittextPhyscialC4.getText().toString().trim());
        if (TextUtils.isEmpty(atmCashLoadingDto.getPhysicalC4())) {
            Toast.makeText(context, "Please enter note", Toast.LENGTH_LONG).show();
            edittextPhyscialC4.setError("Please enter note.");
            return 7;
        }

        //Cash Loaded
        // Cassette 1
        atmCashLoadingDto.setLoadedC1(edittextCashLoadedC1.getText().toString().trim());
        if (TextUtils.isEmpty(atmCashLoadingDto.getLoadedC1())) {
            Toast.makeText(context, "Please enter note", Toast.LENGTH_LONG).show();
            edittextCashLoadedC1.setError("Please enter note.");
            return 5;
        }

        // Cassette 2
        atmCashLoadingDto.setLoadedC2(edittextCashLoadedC2.getText().toString().trim());
        if (TextUtils.isEmpty(atmCashLoadingDto.getLoadedC2())) {
            Toast.makeText(context, "Please enter note", Toast.LENGTH_LONG).show();
            edittextCashLoadedC2.setError("Please enter note.");
            return 6;
        }

        // Cassette 3
        atmCashLoadingDto.setLoadedC3(edittextCashLoadedC3.getText().toString().trim());
        if (TextUtils.isEmpty(atmCashLoadingDto.getLoadedC3())) {
            Toast.makeText(context, "Please enter note", Toast.LENGTH_LONG).show();
            edittextCashLoadedC3.setError("Please enter note.");
            return 7;
        }

        // Cassette 4
        atmCashLoadingDto.setLoadedC4(edittextCashLoadedC4.getText().toString().trim());
        if (TextUtils.isEmpty(atmCashLoadingDto.getLoadedC4())) {
            Toast.makeText(context, "Please enter note", Toast.LENGTH_LONG).show();
            edittextCashLoadedC4.setError("Please enter note.");
            return 7;
        }

      /*  //Cash Removed
        // Cassette 1
        atmCashLoadingDto.setRemovedC1(edittextCashRemovedC1.getText().toString().trim());
        if (TextUtils.isEmpty(atmCashLoadingDto.getRemovedC1())) {
            Toast.makeText(context, "Please enter note", Toast.LENGTH_LONG).show();
            edittextCashRemovedC1.setError("Please enter note.");
            return 5;
        }

        // Cassette 2
        atmCashLoadingDto.setRemovedC2(edittextCashRemovedC2.getText().toString().trim());
        if (TextUtils.isEmpty(atmCashLoadingDto.getRemovedC2())) {
            Toast.makeText(context, "Please enter note", Toast.LENGTH_LONG).show();
            edittextCashRemovedC2.setError("Please enter note.");
            return 6;
        }

        // Cassette 3
        atmCashLoadingDto.setRemovedC3(edittextCashRemovedC3.getText().toString().trim());
        if (TextUtils.isEmpty(atmCashLoadingDto.getRemovedC3())) {
            Toast.makeText(context, "Please enter note", Toast.LENGTH_LONG).show();
            edittextCashRemovedC3.setError("Please enter note.");
            return 7;
        }

        // Cassette 4
        atmCashLoadingDto.setRemovedC4(edittextCashRemovedC4.getText().toString().trim());
        if (TextUtils.isEmpty(atmCashLoadingDto.getRemovedC4())) {
            Toast.makeText(context, "Please enter note", Toast.LENGTH_LONG).show();
            edittextCashRemovedC4.setError("Please enter note.");
            return 7;
        }*/

        /*//Cash Closing
        // Cassette 1
        atmCashLoadingDto.setClosingC1(edittextClosingC1.getText().toString().trim());
        if (TextUtils.isEmpty(atmCashLoadingDto.getClosingC1())) {
            Toast.makeText(context, "Please enter note", Toast.LENGTH_LONG).show();
            edittextClosingC1.setError("Please enter note.");
            return 5;
        }

        // Cassette 2
        atmCashLoadingDto.setClosingC2(edittextClosingC2.getText().toString().trim());
        if (TextUtils.isEmpty(atmCashLoadingDto.getClosingC2())) {
            Toast.makeText(context, "Please enter note", Toast.LENGTH_LONG).show();
            edittextClosingC2.setError("Please enter note.");
            return 6;
        }

        // Cassette 3
        atmCashLoadingDto.setClosingC3(edittextClosingC3.getText().toString().trim());
        if (TextUtils.isEmpty(atmCashLoadingDto.getClosingC3())) {
            Toast.makeText(context, "Please enter note", Toast.LENGTH_LONG).show();
            edittextClosingC3.setError("Please enter note.");
            return 7;
        }

        // Cassette 4
        atmCashLoadingDto.setClosingC4(edittextClosingC4.getText().toString().trim());
        if (TextUtils.isEmpty(atmCashLoadingDto.getClosingC4())) {
            Toast.makeText(context, "Please enter note", Toast.LENGTH_LONG).show();
            edittextClosingC4.setError("Please enter note.");
            return 7;
        }*/

        //ATM Counter Before Scan Copy
        if (TextUtils.isEmpty(atmCashLoadingDto.getAtmCounterBeforeCopyId()) || atmCashLoadingDto.getAtmCounterBeforeCopyId().equalsIgnoreCase("0")) {
            if (TextUtils.isEmpty(atmCashLoadingDto.getAtmCounterBeforeBase64())) {
                showMessage("Please add ATM Counter Before Scan Copy.");
                return 1;
            }
        }

        //ATM Counter After Scan Copy
        if (TextUtils.isEmpty(atmCashLoadingDto.getAtmCounterAfterCopyId()) || atmCashLoadingDto.getAtmCounterAfterCopyId().equalsIgnoreCase("0")) {
            if (TextUtils.isEmpty(atmCashLoadingDto.getAtmCounterAfterBase64())) {
                showMessage("Please add ATM Counter After Scan Copy.");
                return 1;
            }
        }

        //Switch Counter Before Scan Copy
        if (TextUtils.isEmpty(atmCashLoadingDto.getSwitchCounterBeforeCopyId()) || atmCashLoadingDto.getSwitchCounterBeforeCopyId().equalsIgnoreCase("0")) {
            if (TextUtils.isEmpty(atmCashLoadingDto.getSwitchCounterBeforeBase64())) {
                showMessage("Please add Switch Counter Before Scan Copy.");
                return 1;
            }
        }

        //Switch Counter After Scan Copy
        if (TextUtils.isEmpty(atmCashLoadingDto.getSwitchCounterAfterCopyId()) || atmCashLoadingDto.getSwitchCounterAfterCopyId().equalsIgnoreCase("0")) {
            if (TextUtils.isEmpty(atmCashLoadingDto.getSwitchCounterAfterBase64())) {
                showMessage("Please add Switch Counter After Scan Copy.");
                return 1;
            }
        }

        //Cash Loaded Amount should not greater than Pending Cash Sourcing Amount
        //Total Cash Loaded Amount
        int number1 = Integer.parseInt(atmCashLoadingDto.getLoadedC1());
        int number2 = Integer.parseInt(atmCashLoadingDto.getLoadedC2());
        int number3 = Integer.parseInt(atmCashLoadingDto.getLoadedC3());
        int number4 = Integer.parseInt(atmCashLoadingDto.getLoadedC4());

        int totalLoadedAmount = ((number1 * 2000 + number2 * 500 + number3 * 100 + number4 * 100));
        int pendingSourcingAmount = TextUtils.isEmpty(atmCashLoadingDto.getPendingAmount()) ? 0 : Integer.parseInt(atmCashLoadingDto.getPendingAmount());

        if (totalLoadedAmount <= 0) {
            showMessage("Total Loaded Amount should not be 0.");
            return 1;
        }

        if (totalLoadedAmount > pendingSourcingAmount) {
            String amt = getCommaUsingPlaceValue(String.valueOf(pendingSourcingAmount));
            showMessage("Total Cash Loaded Amount should not exceed " + amt + ".");
            return 1;
        }

        //Check Remaining Note Count
        int pending2000NoteCount = TextUtils.isEmpty(atmCashLoadingDto.getRemaining_loading_note_count_c1()) ? 0 : Integer.parseInt(atmCashLoadingDto.getRemaining_loading_note_count_c1());
        if (number1 > pending2000NoteCount) {
            showMessage("2000 Note Count of Cash Loaded cannot exceed " + pending2000NoteCount);
            return 1;
        }

        int pending500NoteCount = TextUtils.isEmpty(atmCashLoadingDto.getRemaining_loading_note_count_c2()) ? 0 : Integer.parseInt(atmCashLoadingDto.getRemaining_loading_note_count_c2());
        if (number2 > pending500NoteCount) {
            showMessage("500 Note Count of Cash Loaded cannot exceed " + pending500NoteCount);
            return 1;
        }

        int pending100NoteCount = TextUtils.isEmpty(atmCashLoadingDto.getRemaining_loading_note_count_c3_c4()) ? 0 : Integer.parseInt(atmCashLoadingDto.getRemaining_loading_note_count_c3_c4());
        if ((number3 + number4) > pending100NoteCount) {
            showMessage("100 Note Count of Cash Loaded cannot exceed " + pending100NoteCount);
            return 1;
        }

        return 0;
    }

    private void showDateTimeDialogPicker(final View view) {

        Date defaultDate = null;
        if (selectedDateTimeId == R.id.textViewCashLoadingDate) {
            defaultDate = loadingDate;
        }
        // Show DateTime Picker Dialog.
        dateTimePickerDialog = new DateTimePickerDialog(context, true, defaultDate, new DateTimePickerDialog.IDateTimePicker() {
            @Override
            public void getDateTime(Date datetime, String defaultFormattedDateTime) {
                try {
                    String formatedDate = dateFormatter2.format(datetime);
                    String formateYMD = dateFormatterYMD.format(datetime);

                    if (selectedDateTimeId != 0) {
                        TextView textViewDateTime = (TextView) view.findViewById(selectedDateTimeId);
                        textViewDateTime.setText(formateYMD);

                        if (selectedDateTimeId == R.id.textViewCashLoadingDate) {
                            loadingDate = datetime;
                            strLoadingDate = formateYMD;
                            atmCashLoadingDto.setLoadingDate(strLoadingDate);

                            String issuingDate = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd MMM yyyy", atmCashLoadingDto.getLoadingDate());
                            textViewCashLoadingDate.setText(issuingDate);
                            textViewCashLoadingDate.setError(null);

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

        Calendar minCal = Calendar.getInstance();
        int day = minCal.get(Calendar.DATE);
        int month = minCal.get(Calendar.MONTH);
        int year = 1900;
        minCal.set(year, month, day);

        //Today's Date
        long now = new Date().getTime() - 1000;

        if (selectedDateTimeId == R.id.textViewCashLoadingDate) {
            Date minDate = CommonUtils.parseStringToDate(atmCashLoadingDto.getMinLoadingDate());
            dateTimePickerDialog.setMinDate(minDate.getTime());
            //dateTimePickerDialog.setMinDate(minCal.getTimeInMillis());
            dateTimePickerDialog.setMaxDate(now);

        }

        dateTimePickerDialog.setCancelable(false);
        dateTimePickerDialog.setActionButtonName("Save");
        dateTimePickerDialog.show();

    }

    private void editTextAmountEnter(final EditText enterNote, final TextView amount, final Long note) {
        enterNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                CommonUtils.totalPhyscialNoteCount(textTotalNoteCountPhyscial, edittextPhyscialC1, edittextPhyscialC2, edittextPhyscialC3,
                        edittextPhyscialC4, textTotalAmountCountPhyscial);

                CommonUtils.totalPhyscialNoteCount(textTotalNoteCountCashLoaded, edittextCashLoadedC1, edittextCashLoadedC2, edittextCashLoadedC3,
                        edittextCashLoadedC4, textTotalAmountCountCashLoaded);

                CommonUtils.totalPhyscialNoteCount(textTotalNoteCountCashRemoved, edittextCashRemovedC1, edittextCashRemovedC2, edittextCashRemovedC3,
                        edittextCashRemovedC4, textTotalAmountCountCashRemoved);

                calculateClosing();

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    String e = enterNote.getText().toString();
                    Long cst = Long.valueOf(e);
                    cst = cst * note;

                    String moneyString = new DecimalFormat("##,##,##0").format(cst);
                    amount.setText(moneyString);
                } else {
                    amount.setText("0");
                }

                String tmp = editable.toString().trim();
               /* if (tmp.length() == 1 && tmp.equals("0"))
                    editable.clear();*/
            }
        });
    }

    private void editTextAmountEnterClosing(final EditText enterNote, final TextView amount, final Long note) {
        enterNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                CommonUtils.totalClosingNoteCount(textTotalNoteCountClosing, edittextClosingC1, edittextClosingC2, edittextClosingC3,
                        edittextClosingC4, textTotalAmountCountClosing);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    String e = enterNote.getText().toString();
                    Long cst = Long.valueOf(e);
                    cst = cst * note;

                    String moneyString = new DecimalFormat("##,##,##0").format(cst);
                    amount.setText(moneyString);
                } else {
                    amount.setText("0");
                }

                String tmp = editable.toString().trim();
               /* if (tmp.length() == 1)
                    editable.clear();*/
            }
        });
    }

    private void calculateClosing() {

        //Closing C1
        int physicalC1 = TextUtils.isEmpty(edittextPhyscialC1.getText().toString().trim()) ? 0 : Integer.parseInt(edittextPhyscialC1.getText().toString().trim());
        int loadedC1 = TextUtils.isEmpty(edittextCashLoadedC1.getText().toString().trim()) ? 0 : Integer.parseInt(edittextCashLoadedC1.getText().toString().trim());
        int removedC1 = TextUtils.isEmpty(edittextCashRemovedC1.getText().toString().trim()) ? 0 : Integer.parseInt(edittextCashRemovedC1.getText().toString().trim());
        int closingC1 = (physicalC1 + loadedC1) - removedC1;
        edittextClosingC1.setText("" + closingC1);

        //Closing 2
        int physicalC2 = TextUtils.isEmpty(edittextPhyscialC2.getText().toString().trim()) ? 0 : Integer.parseInt(edittextPhyscialC2.getText().toString().trim());
        int loadedC2 = TextUtils.isEmpty(edittextCashLoadedC2.getText().toString().trim()) ? 0 : Integer.parseInt(edittextCashLoadedC2.getText().toString().trim());
        int removedC2 = TextUtils.isEmpty(edittextCashRemovedC2.getText().toString().trim()) ? 0 : Integer.parseInt(edittextCashRemovedC2.getText().toString().trim());
        int closingC2 = (physicalC2 + loadedC2) - removedC2;
        edittextClosingC2.setText("" + closingC2);

        //Closing 3
        int physicalC3 = TextUtils.isEmpty(edittextPhyscialC3.getText().toString().trim()) ? 0 : Integer.parseInt(edittextPhyscialC3.getText().toString().trim());
        int loadedC3 = TextUtils.isEmpty(edittextCashLoadedC3.getText().toString().trim()) ? 0 : Integer.parseInt(edittextCashLoadedC3.getText().toString().trim());
        int removedC3 = TextUtils.isEmpty(edittextCashRemovedC3.getText().toString().trim()) ? 0 : Integer.parseInt(edittextCashRemovedC3.getText().toString().trim());
        int closingC3 = (physicalC3 + loadedC3) - removedC3;
        edittextClosingC3.setText("" + closingC3);

        //Closing 4
        int physicalC4 = TextUtils.isEmpty(edittextPhyscialC4.getText().toString().trim()) ? 0 : Integer.parseInt(edittextPhyscialC4.getText().toString().trim());
        int loadedC4 = TextUtils.isEmpty(edittextCashLoadedC4.getText().toString().trim()) ? 0 : Integer.parseInt(edittextCashLoadedC4.getText().toString().trim());
        int removedC4 = TextUtils.isEmpty(edittextCashRemovedC4.getText().toString().trim()) ? 0 : Integer.parseInt(edittextCashRemovedC4.getText().toString().trim());
        int closingC4 = (physicalC4 + loadedC4) - removedC4;
        edittextClosingC4.setText("" + closingC4);

        /*CommonUtils.totalClosingNoteCount(textTotalNoteCountClosing, edittextClosingC1, edittextClosingC2, edittextClosingC3,
                edittextClosingC4, textTotalAmountCountClosing);*/

    }

    private void bindData() {

        //Cash Loading Date
        if (!TextUtils.isEmpty(atmCashLoadingDto.getLoadingDate())) {
            String formattedDate = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd MMM yyyy", atmCashLoadingDto.getLoadingDate());
            textViewCashLoadingDate.setText(formattedDate);
        }

        //Physical
        String physicalC1 = TextUtils.isEmpty(atmCashLoadingDto.getPhysicalC1()) ? "0" : atmCashLoadingDto.getPhysicalC1();
        String physicalC2 = TextUtils.isEmpty(atmCashLoadingDto.getPhysicalC2()) ? "0" : atmCashLoadingDto.getPhysicalC2();
        String physicalC3 = TextUtils.isEmpty(atmCashLoadingDto.getPhysicalC3()) ? "0" : atmCashLoadingDto.getPhysicalC3();
        String physicalC4 = TextUtils.isEmpty(atmCashLoadingDto.getPhysicalC4()) ? "0" : atmCashLoadingDto.getPhysicalC4();
        edittextPhyscialC1.setText(physicalC1);
        edittextPhyscialC2.setText(physicalC2);
        edittextPhyscialC3.setText(physicalC3);
        edittextPhyscialC4.setText(physicalC4);

        CommonUtils.animateTextView(0, Integer.parseInt(physicalC1), edittextPhyscialC1);
        CommonUtils.animateTextView(0, Integer.parseInt(physicalC2), edittextPhyscialC2);
        CommonUtils.animateTextView(0, Integer.parseInt(physicalC3), edittextPhyscialC3);
        CommonUtils.animateTextView(0, Integer.parseInt(physicalC4), edittextPhyscialC4);

        //Cash Loaded
        String loaded1 = TextUtils.isEmpty(atmCashLoadingDto.getLoadedC1()) ? "0" : atmCashLoadingDto.getLoadedC1();
        String loaded2 = TextUtils.isEmpty(atmCashLoadingDto.getLoadedC2()) ? "0" : atmCashLoadingDto.getLoadedC2();
        String loaded3 = TextUtils.isEmpty(atmCashLoadingDto.getLoadedC3()) ? "0" : atmCashLoadingDto.getLoadedC3();
        String loaded4 = TextUtils.isEmpty(atmCashLoadingDto.getLoadedC4()) ? "0" : atmCashLoadingDto.getLoadedC4();
        edittextCashLoadedC1.setText(loaded1);
        edittextCashLoadedC2.setText(loaded2);
        edittextCashLoadedC3.setText(loaded3);
        edittextCashLoadedC4.setText(loaded4);

        CommonUtils.animateTextView(0, Integer.parseInt(loaded1), edittextCashLoadedC1);
        CommonUtils.animateTextView(0, Integer.parseInt(loaded2), edittextCashLoadedC2);
        CommonUtils.animateTextView(0, Integer.parseInt(loaded3), edittextCashLoadedC3);
        CommonUtils.animateTextView(0, Integer.parseInt(loaded4), edittextCashLoadedC4);

        //Cash Removed
        String removed1 = TextUtils.isEmpty(atmCashLoadingDto.getRemovedC1()) ? "0" : atmCashLoadingDto.getRemovedC1();
        String removed2 = TextUtils.isEmpty(atmCashLoadingDto.getRemovedC2()) ? "0" : atmCashLoadingDto.getRemovedC2();
        String removed3 = TextUtils.isEmpty(atmCashLoadingDto.getRemovedC3()) ? "0" : atmCashLoadingDto.getRemovedC3();
        String removed4 = TextUtils.isEmpty(atmCashLoadingDto.getRemovedC4()) ? "0" : atmCashLoadingDto.getRemovedC4();
        edittextCashRemovedC1.setText(removed1);
        edittextCashRemovedC2.setText(removed2);
        edittextCashRemovedC3.setText(removed3);
        edittextCashRemovedC4.setText(removed4);

        CommonUtils.animateTextView(0, Integer.parseInt(removed1), edittextCashRemovedC1);
        CommonUtils.animateTextView(0, Integer.parseInt(removed2), edittextCashRemovedC2);
        CommonUtils.animateTextView(0, Integer.parseInt(removed3), edittextCashRemovedC3);
        CommonUtils.animateTextView(0, Integer.parseInt(removed4), edittextCashRemovedC4);

        //Cash Closing
        String closing1 = TextUtils.isEmpty(atmCashLoadingDto.getClosingC1()) ? "0" : atmCashLoadingDto.getClosingC1();
        String closing2 = TextUtils.isEmpty(atmCashLoadingDto.getClosingC1()) ? "0" : atmCashLoadingDto.getClosingC1();
        String closing3 = TextUtils.isEmpty(atmCashLoadingDto.getClosingC1()) ? "0" : atmCashLoadingDto.getClosingC1();
        String closing4 = TextUtils.isEmpty(atmCashLoadingDto.getClosingC1()) ? "0" : atmCashLoadingDto.getClosingC1();
        edittextClosingC1.setText(closing1);
        edittextClosingC2.setText(closing2);
        edittextClosingC3.setText(closing3);
        edittextClosingC4.setText(closing4);

        CommonUtils.animateTextView(0, Integer.parseInt(closing1), edittextClosingC1);
        CommonUtils.animateTextView(0, Integer.parseInt(closing2), edittextClosingC2);
        CommonUtils.animateTextView(0, Integer.parseInt(closing3), edittextClosingC3);
        CommonUtils.animateTextView(0, Integer.parseInt(closing4), edittextClosingC4);

        //ATM Counter After
        boolean IsATMAfterPDF = (atmCashLoadingDto.getAtmCounterAfterExt() != null && atmCashLoadingDto.getAtmCounterAfterExt().equalsIgnoreCase("pdf")) ? true : false;
        if (IsATMAfterPDF) {
            Glide.with(context)
                    .load(R.drawable.pdf)
                    .apply(new RequestOptions()
                            .error(R.drawable.pdf)
                            .placeholder(R.drawable.pdf)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgATMCounterAfter);

        } else {
            if (!TextUtils.isEmpty(atmCashLoadingDto.getAtmCounterAfterCopyId())) {
                String gstUrl = Constants.DownloadImageUrl + atmCashLoadingDto.getAtmCounterAfterCopyId();
                Glide.with(context)
                        .load(gstUrl)
                        .apply(new RequestOptions()
                                .error(R.drawable.ic_camera_alt_black_72dp)
                                .placeholder(R.drawable.ic_camera_alt_black_72dp)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))
                        .into(imgATMCounterAfter);
            }
        }

        //ATM Counter Before
        boolean IsATMBeforePDF = (atmCashLoadingDto.getAtmCounterBeforeExt() != null && atmCashLoadingDto.getAtmCounterBeforeExt().equalsIgnoreCase("pdf")) ? true : false;
        if (IsATMBeforePDF) {
            Glide.with(context)
                    .load(R.drawable.pdf)
                    .apply(new RequestOptions()
                            .error(R.drawable.pdf)
                            .placeholder(R.drawable.pdf)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgATMCounterBefore);

        } else {
            if (!TextUtils.isEmpty(atmCashLoadingDto.getAtmCounterBeforeCopyId())) {
                String gstUrl = Constants.DownloadImageUrl + atmCashLoadingDto.getAtmCounterBeforeCopyId();
                Glide.with(context)
                        .load(gstUrl)
                        .apply(new RequestOptions()
                                .error(R.drawable.ic_camera_alt_black_72dp)
                                .placeholder(R.drawable.ic_camera_alt_black_72dp)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))
                        .into(imgATMCounterBefore);
            }
        }

        //Switch Counter After
        boolean IsSwitchAfterPDF = (atmCashLoadingDto.getSwitchCounterAfterExt() != null && atmCashLoadingDto.getSwitchCounterAfterExt().equalsIgnoreCase("pdf")) ? true : false;
        if (IsSwitchAfterPDF) {
            Glide.with(context)
                    .load(R.drawable.pdf)
                    .apply(new RequestOptions()
                            .error(R.drawable.pdf)
                            .placeholder(R.drawable.pdf)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgSwitchCounterAfter);

        } else {
            if (!TextUtils.isEmpty(atmCashLoadingDto.getSwitchCounterAfterCopyId())) {
                String gstUrl = Constants.DownloadImageUrl + atmCashLoadingDto.getSwitchCounterAfterCopyId();
                Glide.with(context)
                        .load(gstUrl)
                        .apply(new RequestOptions()
                                .error(R.drawable.ic_camera_alt_black_72dp)
                                .placeholder(R.drawable.ic_camera_alt_black_72dp)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))
                        .into(imgSwitchCounterAfter);
            }
        }

        //Switch Counter Before
        boolean IsSwitchBeforePDF = (atmCashLoadingDto.getSwitchCounterBeforeExt() != null && atmCashLoadingDto.getSwitchCounterBeforeExt().equalsIgnoreCase("pdf")) ? true : false;
        if (IsSwitchBeforePDF) {
            Glide.with(context)
                    .load(R.drawable.pdf)
                    .apply(new RequestOptions()
                            .error(R.drawable.pdf)
                            .placeholder(R.drawable.pdf)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgSwitchCounterBefore);

        } else {
            if (!TextUtils.isEmpty(atmCashLoadingDto.getSwitchCounterBeforeCopyId())) {
                String gstUrl = Constants.DownloadImageUrl + atmCashLoadingDto.getSwitchCounterBeforeCopyId();
                Glide.with(context)
                        .load(gstUrl)
                        .apply(new RequestOptions()
                                .error(R.drawable.ic_camera_alt_black_72dp)
                                .placeholder(R.drawable.ic_camera_alt_black_72dp)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))
                        .into(imgSwitchCounterBefore);
            }
        }

        //Remarks
        editTextRemarks.setText(atmCashLoadingDto.getRemarks());

        //Enable/disable views
        boolean IsEditable = (!TextUtils.isEmpty(atmCashLoadingDto.getIsEditable()) && atmCashLoadingDto.getIsEditable().equalsIgnoreCase("1")) ? true : false;
        GUIUtils.setViewAndChildrenEnabled(layoutLoading2, IsEditable);
        GUIUtils.setViewAndChildrenEnabled(layoutLoading1, IsEditable);
        GUIUtils.setViewAndChildrenEnabled(layoutClosing, false);
        if (IsEditable) {
            layoutFooter.setVisibility(View.VISIBLE);
        } else {
            layoutFooter.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        backPressed();
    }

    private void setResult(boolean IsReload) {
        Intent intent = new Intent(context, ATMCashLoadingActivity.class);
        intent.putExtra("PERFORM_RELOAD", IsReload);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public void backPressed() {
        setResult(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_serach, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search_main);
        searchItem.setVisible(false);
        MenuItem home = menu.findItem(R.id.action_home_dashborad);
        home.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            backPressed();
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

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
                    picUri = uri;

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
            if (requestCode == SCANLIB_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
                Bitmap bitmap = null;

                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                picUri = uri;
                String base64Data = CommonUtils.convertBitmapToString(bitmap);
                setImageData(picUri, base64Data, bitmap);

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
                imageBitMap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);

            setScanCopyData(IsDrawable, ext, imageBitMap, fileName, base64Data);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setScanCopyData(boolean IsDrawable, String ext, Bitmap bitmap, String fileName, String base64) {

        GSTINDTO imageDto = new GSTINDTO();
        imageDto.setUri(picUri);
        imageDto.setBitmap(bitmap);
        imageDto.setChangedPhoto(true);
        imageDto.setGstImage(base64);
        imageDto.setGstImageExt(ext);

        switch (FROM) {

            case ATM_AFTER_SCAN_COPY:
                if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgATMCounterAfter);
                else {
                    Glide.with(context).asBitmap().load(bitmap).into(imgATMCounterAfter);
                }
                break;

            case ATM_BEFORE_SCAN_COPY:
                if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgATMCounterBefore);
                else {
                    Glide.with(context).asBitmap().load(bitmap).into(imgATMCounterBefore);
                }

                break;

            case SWITCH_AFTER_COPY:
                if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgSwitchCounterAfter);
                else {
                    Glide.with(context).asBitmap().load(bitmap).into(imgSwitchCounterAfter);
                }
                break;

            case SWITCH_BEFORE_COPY:
                if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgSwitchCounterBefore);
                else {
                    Glide.with(context).asBitmap().load(bitmap).into(imgSwitchCounterBefore);
                }
                break;

            default:
                break;

        }

        refreshGSTDetailsImg(imageDto);
    }

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
                performCameraClick(view);
            }

            @Override
            public void folderClick() {
                //If the app has not the permission then asking for the permission
                performFolderClick(view);
            }
        });
        fileAttachementDialog.setCancelable(false);
        fileAttachementDialog.show();
    }

    private void performFolderClick(View view) {
        permissionHandler.requestMultiplePermission(view, permissions, getString(R.string.needs_camera_storage_permission_msg), new IPermission() {
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

    private void performCameraClick(View view) {
        permissionHandler.requestMultiplePermission(view, permissions, getString(R.string.needs_camera_storage_permission_msg), new IPermission() {
            @Override
            public void IsPermissionGranted(boolean IsGranted) {
                if (IsGranted) {
                    showCamera();
                }
            }
        });

    }

    private void showCamera() {
        switch (FROM) {

            case ATM_AFTER_SCAN_COPY:

                String ext = TextUtils.isEmpty(atmCashLoadingDto.getAtmCounterAfterExt()) ? "jpg" : atmCashLoadingDto.getAtmCounterAfterExt();

                //FOR PDF
                if (ext.equalsIgnoreCase("pdf")) {
                    SEL_FILE_TYPE = " images";
                    startScanCamera();
                    return;
                }

                //FOR Image
                if ((TextUtils.isEmpty(atmCashLoadingDto.getAtmCounterAfterCopyId()) || atmCashLoadingDto.getAtmCounterAfterCopyId().equalsIgnoreCase("0")) && TextUtils.isEmpty(atmCashLoadingDto.getAtmCounterAfterBase64())) {
                    SEL_FILE_TYPE = "images";
                    startScanCamera();

                } else {
                    GSTINDTO previewDto = prepareDtoForPreview(FROM);
                    showImagePreviewDialog(previewDto);
                }

                break;
            case ATM_BEFORE_SCAN_COPY:

                String ext1 = TextUtils.isEmpty(atmCashLoadingDto.getAtmCounterBeforeExt()) ? "jpg" : atmCashLoadingDto.getAtmCounterBeforeExt();

                //FOR PDF
                if (ext1.equalsIgnoreCase("pdf")) {
                    SEL_FILE_TYPE = " images";
                    startScanCamera();
                    return;
                }

                //FOR Image
                if ((TextUtils.isEmpty(atmCashLoadingDto.getAtmCounterBeforeCopyId()) || atmCashLoadingDto.getAtmCounterBeforeCopyId().equalsIgnoreCase("0")) && TextUtils.isEmpty(atmCashLoadingDto.getAtmCounterBeforeBase64())) {
                    SEL_FILE_TYPE = "images";
                    startScanCamera();

                } else {
                    GSTINDTO previewDto = prepareDtoForPreview(FROM);
                    showImagePreviewDialog(previewDto);
                }

                break;

            case SWITCH_AFTER_COPY:

                String ext2 = TextUtils.isEmpty(atmCashLoadingDto.getSwitchCounterAfterExt()) ? "jpg" : atmCashLoadingDto.getSwitchCounterAfterExt();

                //FOR PDF
                if (ext2.equalsIgnoreCase("pdf")) {
                    SEL_FILE_TYPE = " images";
                    startScanCamera();
                    return;
                }

                //FOR Image
                if ((TextUtils.isEmpty(atmCashLoadingDto.getSwitchCounterAfterCopyId()) || atmCashLoadingDto.getSwitchCounterAfterCopyId().equalsIgnoreCase("0")) && TextUtils.isEmpty(atmCashLoadingDto.getSwitchCounterAfterBase64())) {
                    SEL_FILE_TYPE = "images";
                    startScanCamera();

                } else {
                    GSTINDTO previewDto = prepareDtoForPreview(FROM);
                    showImagePreviewDialog(previewDto);
                }

                break;
            case SWITCH_BEFORE_COPY:

                String ext3 = TextUtils.isEmpty(atmCashLoadingDto.getSwitchCounterBeforeExt()) ? "jpg" : atmCashLoadingDto.getSwitchCounterBeforeExt();

                //FOR PDF
                if (ext3.equalsIgnoreCase("pdf")) {
                    SEL_FILE_TYPE = " images";
                    startScanCamera();
                    return;
                }

                //FOR Image
                if ((TextUtils.isEmpty(atmCashLoadingDto.getSwitchCounterBeforeCopyId()) || atmCashLoadingDto.getSwitchCounterBeforeCopyId().equalsIgnoreCase("0")) && TextUtils.isEmpty(atmCashLoadingDto.getSwitchCounterBeforeBase64())) {
                    SEL_FILE_TYPE = "images";
                    startScanCamera();

                } else {
                    GSTINDTO previewDto = prepareDtoForPreview(FROM);
                    showImagePreviewDialog(previewDto);
                }

                break;

            default:
                break;
        }
    }

    private void startScanCamera() {

        //If the app has not the permission then asking for the permission
        permissionHandler.requestMultiplePermission(imgATMCounterAfter, permissions, getString(R.string.needs_camera_storage_permission_msg), new IPermission() {
            @Override
            public void IsPermissionGranted(boolean IsGranted) {
                if (IsGranted) {

                    SEL_FILE_TYPE = "images";
                    int preference = ScanConstants.OPEN_CAMERA;
                    Intent intent1 = new Intent(context, ScanActivity.class);
                    intent1.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
                    startActivityForResult(intent1, SCANLIB_REQUEST_CODE);

                }
            }
        });
    }

    private void refreshGSTDetailsImg(GSTINDTO imageDto) {
        if (FROM == ATM_AFTER_SCAN_COPY) {

            atmCashLoadingDto.setChangedPhotoAtmCounterAfter(imageDto.isChangedPhoto());
            atmCashLoadingDto.setAtmCounterAfterCopyId(imageDto.getGstImageId());
            atmCashLoadingDto.setAtmCounterAfterBitmap(imageDto.getBitmap());
            atmCashLoadingDto.setAtmCounterAfterExt(imageDto.getGstImageExt());
            atmCashLoadingDto.setAtmCounterAfterBase64(imageDto.getGstImage());

        } else if (FROM == ATM_BEFORE_SCAN_COPY) {

            atmCashLoadingDto.setChangedPhotoAtmCounterBefore(imageDto.isChangedPhoto());
            atmCashLoadingDto.setAtmCounterBeforeCopyId(imageDto.getGstImageId());
            atmCashLoadingDto.setAtmCounterBeforeBitmap(imageDto.getBitmap());
            atmCashLoadingDto.setAtmCounterBeforeExt(imageDto.getGstImageExt());
            atmCashLoadingDto.setAtmCounterBeforeBase64(imageDto.getGstImage());

        } else if (FROM == SWITCH_AFTER_COPY) {

            atmCashLoadingDto.setChangedPhotoSwitchCounterAfter(imageDto.isChangedPhoto());
            atmCashLoadingDto.setSwitchCounterAfterCopyId(imageDto.getGstImageId());
            atmCashLoadingDto.setSwitchCounterAfterBitmap(imageDto.getBitmap());
            atmCashLoadingDto.setSwitchCounterAfterExt(imageDto.getGstImageExt());
            atmCashLoadingDto.setSwitchCounterAfterBase64(imageDto.getGstImage());

        } else if (FROM == SWITCH_BEFORE_COPY) {

            atmCashLoadingDto.setChangedPhotoSwitchCounterBefore(imageDto.isChangedPhoto());
            atmCashLoadingDto.setSwitchCounterBeforeCopyId(imageDto.getGstImageId());
            atmCashLoadingDto.setSwitchCounterBeforeBitmap(imageDto.getBitmap());
            atmCashLoadingDto.setSwitchCounterBeforeExt(imageDto.getGstImageExt());
            atmCashLoadingDto.setSwitchCounterBeforeBase64(imageDto.getGstImage());

        }
    }

    private GSTINDTO prepareDtoForPreview(int FROM) {
        GSTINDTO imageDto = new GSTINDTO();

        if (FROM == ATM_AFTER_SCAN_COPY) {

            imageDto.setChangedPhoto(atmCashLoadingDto.isChangedPhotoAtmCounterAfter());
            imageDto.setGstImageId(atmCashLoadingDto.getAtmCounterAfterCopyId());
            imageDto.setBitmap(atmCashLoadingDto.getAtmCounterAfterBitmap());
            imageDto.setGstImageExt(atmCashLoadingDto.getAtmCounterAfterExt());
            imageDto.setName("");
            imageDto.setGstImage(atmCashLoadingDto.getAtmCounterAfterBase64());

        } else if (FROM == ATM_BEFORE_SCAN_COPY) {

            imageDto.setChangedPhoto(atmCashLoadingDto.isChangedPhotoAtmCounterBefore());
            imageDto.setGstImageId(atmCashLoadingDto.getAtmCounterBeforeCopyId());
            imageDto.setBitmap(atmCashLoadingDto.getAtmCounterBeforeBitmap());
            imageDto.setGstImageExt(atmCashLoadingDto.getAtmCounterBeforeExt());
            imageDto.setName("");
            imageDto.setGstImage(atmCashLoadingDto.getAtmCounterBeforeBase64());

        } else if (FROM == SWITCH_AFTER_COPY) {

            imageDto.setChangedPhoto(atmCashLoadingDto.isChangedPhotoSwitchCounterAfter());
            imageDto.setGstImageId(atmCashLoadingDto.getSwitchCounterAfterCopyId());
            imageDto.setBitmap(atmCashLoadingDto.getSwitchCounterAfterBitmap());
            imageDto.setGstImageExt(atmCashLoadingDto.getSwitchCounterAfterExt());
            imageDto.setName("");
            imageDto.setGstImage(atmCashLoadingDto.getSwitchCounterAfterBase64());

        } else if (FROM == SWITCH_BEFORE_COPY) {

            imageDto.setChangedPhoto(atmCashLoadingDto.isChangedPhotoSwitchCounterBefore());
            imageDto.setGstImageId(atmCashLoadingDto.getSwitchCounterBeforeCopyId());
            imageDto.setBitmap(atmCashLoadingDto.getSwitchCounterBeforeBitmap());
            imageDto.setGstImageExt(atmCashLoadingDto.getSwitchCounterBeforeExt());
            imageDto.setName("");
            imageDto.setGstImage(atmCashLoadingDto.getSwitchCounterBeforeBase64());

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

                    //Refresh Image
                    if (FROM == ATM_AFTER_SCAN_COPY) {
                        if (!TextUtils.isEmpty(atmCashLoadingDto.getAtmCounterAfterBase64())) {
                            Bitmap bitmap = CommonUtils.StringToBitMap(atmCashLoadingDto.getAtmCounterAfterBase64());
                            if (bitmap != null)
                                imgATMCounterAfter.setImageBitmap(bitmap);
                        }
                    } else if (FROM == ATM_BEFORE_SCAN_COPY) {
                        if (!TextUtils.isEmpty(atmCashLoadingDto.getAtmCounterBeforeBase64())) {
                            Bitmap bitmap = CommonUtils.StringToBitMap(atmCashLoadingDto.getAtmCounterBeforeBase64());
                            if (bitmap != null)
                                imgATMCounterBefore.setImageBitmap(bitmap);
                        }
                    } else if (FROM == SWITCH_AFTER_COPY) {
                        if (!TextUtils.isEmpty(atmCashLoadingDto.getSwitchCounterAfterBase64())) {
                            Bitmap bitmap = CommonUtils.StringToBitMap(atmCashLoadingDto.getSwitchCounterAfterBase64());
                            if (bitmap != null)
                                imgSwitchCounterAfter.setImageBitmap(bitmap);
                        }
                    } else if (FROM == SWITCH_BEFORE_COPY) {
                        if (!TextUtils.isEmpty(atmCashLoadingDto.getSwitchCounterBeforeBase64())) {
                            Bitmap bitmap = CommonUtils.StringToBitMap(atmCashLoadingDto.getSwitchCounterBeforeBase64());
                            if (bitmap != null)
                                imgSwitchCounterBefore.setImageBitmap(bitmap);
                        }
                    }
                }
            });
            customImagePreviewDialog.show();
            customImagePreviewDialog.setCancelable(false);

            //Title
            String title = getSpecificTitle();
            customImagePreviewDialog.setDialogTitle(title);

            //Change Photo Allowed
            boolean IsEditable = (!TextUtils.isEmpty(atmCashLoadingDto.getIsEditable()) && atmCashLoadingDto.getIsEditable().equalsIgnoreCase("1")) ? true : false;
            customImagePreviewDialog.allowChangePhoto(IsEditable);
            customImagePreviewDialog.allowSaveOption(IsEditable);
            customImagePreviewDialog.allowRemarks(false);

        } else {
            Toast.makeText(context, "No photo available to preview.", Toast.LENGTH_SHORT).show();
        }
    }

    private String getSpecificTitle() {
        String title = "Preview Image";

        switch (FROM) {

            case ATM_AFTER_SCAN_COPY:
                title = "ATM Counter Photo After Loading";
                break;

            case ATM_BEFORE_SCAN_COPY:
                title = "ATM Counter Photo Before Loading";
                break;

            case SWITCH_AFTER_COPY:
                title = "Switch Counter Photo After Loading";
                break;

            case SWITCH_BEFORE_COPY:
                title = "Switch Counter Photo Before Loading";
                break;

            default:
                break;

        }

        return title;
    }

    private void showImgOrPDF(final View view) {


        boolean isPdf = isPdf();
        boolean IsEditable = (!TextUtils.isEmpty(atmCashLoadingDto.getIsEditable()) && atmCashLoadingDto.getIsEditable().equalsIgnoreCase("1")) ? true : false;

        if (IsEditable) {
            showAttachmentDialog(view);

        } else {

            //PDF - No Preview for PDF

            //Image
            if (!isPdf) {
                GSTINDTO previewDto = prepareDtoForPreview(FROM);
                showImagePreviewDialog(previewDto);
                return;
            }
        }
    }

    private String getCommaUsingPlaceValue(String value) {

        if (value.equalsIgnoreCase("-"))
            return value;
        else {
            BigDecimal bd = new BigDecimal(value);
            long lDurationMillis = bd.setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
            String moneyString1 = new DecimalFormat("##,##,###.##").format(lDurationMillis);
            return ("₹ " + moneyString1);
        }
    }

    private boolean isPdf() {
        boolean IsPDF = false;

        if (FROM == ATM_AFTER_SCAN_COPY) {

            IsPDF = (atmCashLoadingDto.getAtmCounterAfterExt() != null && atmCashLoadingDto.getAtmCounterAfterExt().equalsIgnoreCase("pdf")) ? true : false;

        } else if (FROM == ATM_BEFORE_SCAN_COPY) {

            IsPDF = (atmCashLoadingDto.getAtmCounterBeforeExt() != null && atmCashLoadingDto.getAtmCounterBeforeExt().equalsIgnoreCase("pdf")) ? true : false;

        } else if (FROM == SWITCH_AFTER_COPY) {

            IsPDF = (atmCashLoadingDto.getSwitchCounterAfterExt() != null && atmCashLoadingDto.getSwitchCounterAfterExt().equalsIgnoreCase("pdf")) ? true : false;

        } else if (FROM == SWITCH_BEFORE_COPY) {

            IsPDF = (atmCashLoadingDto.getSwitchCounterBeforeExt() != null && atmCashLoadingDto.getSwitchCounterBeforeExt().equalsIgnoreCase("pdf")) ? true : false;

        }

        return IsPDF;
    }
}
