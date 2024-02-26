package in.vakrangee.franchisee.atmloading.cash_sourcing;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import in.vakrangee.franchisee.atmloading.ATMLoadingRepository;
import in.vakrangee.franchisee.atmloading.R;
import in.vakrangee.supercore.franchisee.baseutils.BaseAppCompatActivity;
import in.vakrangee.supercore.franchisee.baseutils.IHandleOkButton;
import in.vakrangee.supercore.franchisee.commongui.CustomFranchiseeApplicationSpinnerAdapter;
import in.vakrangee.supercore.franchisee.commongui.DateTimePickerDialog;
import in.vakrangee.supercore.franchisee.commongui.FileAttachmentDialog;
import in.vakrangee.supercore.franchisee.commongui.FileUtils;
import in.vakrangee.supercore.franchisee.commongui.FilteredFilePickerActivity;
import in.vakrangee.supercore.franchisee.commongui.animation.AnimationHanndler;
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
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;

public class AddATMCashSourcingDetailsActivity extends BaseAppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Toolbar toolbar;
    private Context context;
    private TextView txtCashSourcingDateLbl, txtBankReferenceNumberLbl, txtDocumentProofLbl, txtDenominationbl, txtStatusLbl, txtATMIDLbl;
    private TextView textViewCashSourcingDate;
    private TextView textTotalNoteCountPurge;
    private TextView textTotalAmountCountPurge;
    private EditText edittextPurgeC1;
    private EditText edittextPurgeC2;
    private EditText edittextPurgeC3;
    private TextView textPurgeC1;
    private TextView textPurgeC2;
    private TextView textPurgeC3;
    private MaterialButton btnSubmit, btnCancel;
    private ImageView imgDocumentProof;
    private EditText editTextBankReferenceNumber;
    private Spinner spinnerStatus, spinnerATMID;
    //Date
    private DateTimePickerDialog dateTimePickerDialog;
    private DateFormat dateFormatter2 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    private DateFormat dateFormatterYMD = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private Date sourceDate;
    private String strSourceDate;

    private int selectedDateTimeId = 0;
    private DeprecateHandler deprecateHandler;
    private PermissionHandler permissionHandler;
    private FileAttachmentDialog fileAttachementDialog;
    private CustomImageZoomDialogDM customImagePreviewDialog = null;
    private boolean IsEditable = false;
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
    private ATMCashSourcingDto atmCashSourcingDto;
    private static final int PROOF_SCAN_COPY = 1;
    private AsyncSaveCashSourcingDetails asyncSaveCashSourcingDetails = null;
    private ATMLoadingRepository atmLoadingRepo;
    private List<CustomFranchiseeApplicationSpinnerDto> statusList, atmIDList;
    private GetAllSpinnerData getAllSpinnerData = null;
    private LinearLayout layoutFooter, layoutDetail1,layoutDetail2,layoutParent;
    private static final int MAX_TOTAL_AMOUNT = 1000000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_atm_cash_sourcing);

        context = this;
        permissionHandler = new PermissionHandler(this);
        deprecateHandler = new DeprecateHandler(context);
        atmLoadingRepo = new ATMLoadingRepository(context);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            toolbar.setTitleTextColor(Color.WHITE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + getResources().getString(R.string.add_atm_cash_sourcing) + "</small>"));

        }

        layoutParent = findViewById(R.id.layoutParent);
        layoutDetail1 = findViewById(R.id.layoutDetail1);
        layoutDetail2 = findViewById(R.id.layoutDetail2);
        layoutFooter = findViewById(R.id.layoutFooter);
        txtBankReferenceNumberLbl = findViewById(R.id.txtBankReferenceNumberLbl);
        txtCashSourcingDateLbl = findViewById(R.id.txtCashSourcingDateLbl);
        textViewCashSourcingDate = findViewById(R.id.textViewCashSourcingDate);
        txtDocumentProofLbl = findViewById(R.id.txtDocumentProofLbl);
        txtDenominationbl = findViewById(R.id.txtDenominationbl);
        btnCancel = findViewById(R.id.btnCancel);
        btnSubmit = findViewById(R.id.btnSubmit);
        imgDocumentProof = findViewById(R.id.imgDocumentProof);
        editTextBankReferenceNumber = findViewById(R.id.editTextBankReferenceNumber);
        txtStatusLbl = findViewById(R.id.txtStatusLbl);
        txtATMIDLbl = findViewById(R.id.txtATMIDLbl);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        spinnerATMID = findViewById(R.id.spinnerATMID);

        textTotalNoteCountPurge = findViewById(R.id.textTotalNoteCountPurge);
        textTotalAmountCountPurge = findViewById(R.id.textTotalAmountCountPurge);
        edittextPurgeC1 = findViewById(R.id.edittextPurgeC1);
        edittextPurgeC2 = findViewById(R.id.edittextPurgeC2);
        edittextPurgeC3 = findViewById(R.id.edittextPurgeC3);
        textPurgeC1 = findViewById(R.id.textPurgeC1);
        textPurgeC2 = findViewById(R.id.textPurgeC2);
        textPurgeC3 = findViewById(R.id.textPurgeC3);

        TextView[] txtViewsForCompulsoryMark = {txtCashSourcingDateLbl, txtBankReferenceNumberLbl, txtDocumentProofLbl, txtDenominationbl, txtStatusLbl, txtATMIDLbl};
        GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);

        editTextAmountEnter(edittextPurgeC1, textPurgeC1, Long.valueOf(2000));
        editTextAmountEnter(edittextPurgeC2, textPurgeC2, Long.valueOf(500));
        editTextAmountEnter(edittextPurgeC3, textPurgeC3, Long.valueOf(100));

        edittextPurgeC1.setFilters(new InputFilter[]{new CommonUtils.InputFilterMinMax("0", "2500")});
        edittextPurgeC2.setFilters(new InputFilter[]{new CommonUtils.InputFilterMinMax("0", "2500")});
        edittextPurgeC3.setFilters(new InputFilter[]{new CommonUtils.InputFilterMinMax("0", "5000")});

        textViewCashSourcingDate.setOnClickListener(this);
        imgDocumentProof.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        reloadData();

    }

    private void reloadData() {
        atmCashSourcingDto = (ATMCashSourcingDto) getIntent().getSerializableExtra("CASH_SOURCING_DETAILS");
        IsEditable = (!TextUtils.isEmpty(atmCashSourcingDto.getIsEditable()) && atmCashSourcingDto.getIsEditable().equalsIgnoreCase("1")) ? true : false;

        getAllSpinnerData = new GetAllSpinnerData();
        getAllSpinnerData.execute("");
    }

    private void bindData() {

        //ATM ID
        setSpinnerAdapter(atmIDList, spinnerATMID, atmCashSourcingDto.getAtmId());

        //Cash Withdrawal Date
        if (!TextUtils.isEmpty(atmCashSourcingDto.getSourceDate())) {
            String formattedDate = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd MMM yyyy", atmCashSourcingDto.getSourceDate());
            textViewCashSourcingDate.setText(formattedDate);
        }

        //Bank Reference Number
        editTextBankReferenceNumber.setText(atmCashSourcingDto.getBankReferenceNo());

        //Document Proof
        boolean IsPanPDF = (atmCashSourcingDto.getCashSourcingExt() != null && atmCashSourcingDto.getCashSourcingExt().equalsIgnoreCase("pdf")) ? true : false;
        if (IsPanPDF) {
            Glide.with(context)
                    .load(R.drawable.pdf)
                    .apply(new RequestOptions()
                            .error(R.drawable.pdf)
                            .placeholder(R.drawable.pdf)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgDocumentProof);

        } else {
            if (!TextUtils.isEmpty(atmCashSourcingDto.getCashSourcingScanCopyId())) {
                String gstUrl = Constants.DownloadImageUrl + atmCashSourcingDto.getCashSourcingScanCopyId();
                Glide.with(context)
                        .load(gstUrl)
                        .apply(new RequestOptions()
                                .error(R.drawable.ic_camera_alt_black_72dp)
                                .placeholder(R.drawable.ic_camera_alt_black_72dp)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))
                        .into(imgDocumentProof);
            }
        }

        //Status
        setSpinnerAdapter(statusList, spinnerStatus, atmCashSourcingDto.getStatusCode());

        //Denomination
        String d2000 = TextUtils.isEmpty(atmCashSourcingDto.getD2000NoteCount()) ? "0" : atmCashSourcingDto.getD2000NoteCount();
        String d500 = TextUtils.isEmpty(atmCashSourcingDto.getD500NoteCount()) ? "0" : atmCashSourcingDto.getD500NoteCount();
        String d100 = TextUtils.isEmpty(atmCashSourcingDto.getD100NoteCount()) ? "0" : atmCashSourcingDto.getD100NoteCount();
        edittextPurgeC1.setText(d2000);
        edittextPurgeC2.setText(d500);
        edittextPurgeC3.setText(d100);

        CommonUtils.animateTextView(0, Integer.parseInt(d2000), edittextPurgeC1);
        CommonUtils.animateTextView(0, Integer.parseInt(d500), edittextPurgeC2);
        CommonUtils.animateTextView(0, Integer.parseInt(d100), edittextPurgeC3);

        GUIUtils.setViewAndChildrenEnabled(layoutDetail1, IsEditable);
        GUIUtils.setViewAndChildrenEnabled(layoutDetail2, IsEditable);
        if (IsEditable) {
            layoutFooter.setVisibility(View.VISIBLE);
        } else {
            layoutFooter.setVisibility(View.GONE);
        }
    }

    private void editTextAmountEnter(final EditText enterNote, final TextView amount, final Long note) {
        enterNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                CommonUtils.totalPhyscialNoteCountPurge(textTotalNoteCountPurge, edittextPurgeC1, edittextPurgeC2, edittextPurgeC3,
                        textTotalAmountCountPurge);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    String e = enterNote.getText().toString();
                    Long cst = Long.valueOf(e);
                    cst = cst * note;

                    String moneyString = new DecimalFormat("##,##,##0").format(cst);
                    // amount.setText(moneyString + " ₹");
                    amount.setText(moneyString);
                } else {
                    //amount.setText("0 ₹");
                    amount.setText("0");
                }

               /* String tmp = editable.toString().trim();
                //if (tmp.length() == 1 && tmp.equals("0"))
                if (tmp.length() == 1)
                    editable.clear();*/
            }
        });
    }

    @Override
    public void onBackPressed() {
        backPressed();
    }

    private void setResult(boolean IsReload) {
        Intent intent = new Intent(context, ATMCashSourcingActivity.class);
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
    public void onClick(View v) {
        int Id = v.getId();
        if (Id == R.id.imgDocumentProof) {
            showImgOrPDF(v);

        } else if (Id == R.id.textViewCashSourcingDate) {
            selectedDateTimeId = v.getId();
            showDateTimeDialogPicker(v);

        } else if (Id == R.id.btnSubmit) {
            AnimationHanndler.bubbleAnimation(context, v);

            int status = IsATMSourcingDetailsValidated();
            if (status != 0)
                return;

            //Save
            promptSaveDialog();

        } else if (Id == R.id.btnCancel) {
            backPressed();
        }
    }

    private void promptSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to save Cash Withdrawal Details?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        saveCashSourcingDetails();
                        dialog.cancel();

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

    private void saveCashSourcingDetails() {
        asyncSaveCashSourcingDetails = new AsyncSaveCashSourcingDetails(context, atmCashSourcingDto, new AsyncSaveCashSourcingDetails.Callback() {
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
                        String msg = "ATM Cash Sourcing Details saved Successfully.";
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

        asyncSaveCashSourcingDetails.execute("");
    }

    public int IsATMSourcingDetailsValidated() {

        editTextBankReferenceNumber.setError(null);

        //ATM ID
        if (TextUtils.isEmpty(atmCashSourcingDto.getAtmId())) {
            Toast.makeText(context, "Please select ATM ID.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerATMID, "Please select ATM ID.", context);
            return 1;
        }

        //Cash Source Date
        if (TextUtils.isEmpty(atmCashSourcingDto.getSourceDate())) {
            Toast.makeText(context, "Please select Cash Source Date.", Toast.LENGTH_LONG).show();
            textViewCashSourcingDate.setError("Please select Cash Source Date.");
            return 1;
        }

        /*//Bank Reference Number
        atmCashSourcingDto.setBankReferenceNo(editTextBankReferenceNumber.getText().toString().trim());
        if (TextUtils.isEmpty(atmCashSourcingDto.getBankReferenceNo())) {
            Toast.makeText(context, "Please enter Bank Reference Number.", Toast.LENGTH_LONG).show();
            editTextBankReferenceNumber.setError("Please enter Bank Reference Number.");
            return 1;
        }*/

        //Scan Copy
        if (TextUtils.isEmpty(atmCashSourcingDto.getCashSourcingScanCopyId()) || atmCashSourcingDto.getCashSourcingScanCopyId().equalsIgnoreCase("0")) {
            if (TextUtils.isEmpty(atmCashSourcingDto.getCashSourcingBase64())) {
                showMessage("Please add Cash Source Document Proof.");
                return 1;
            }
        }

        //Denomination
        //2000
        atmCashSourcingDto.setD2000NoteCount(edittextPurgeC1.getText().toString().trim());
        if (TextUtils.isEmpty(atmCashSourcingDto.getD2000NoteCount())) {
            Toast.makeText(context, "Please enter note", Toast.LENGTH_LONG).show();
            edittextPurgeC1.setError("Please enter note.");
            return 5;
        }

        //500
        atmCashSourcingDto.setD500NoteCount(edittextPurgeC2.getText().toString().trim());
        if (TextUtils.isEmpty(atmCashSourcingDto.getD500NoteCount())) {
            Toast.makeText(context, "Please enter note", Toast.LENGTH_LONG).show();
            edittextPurgeC2.setError("Please enter note.");
            return 6;
        }

        //100
        atmCashSourcingDto.setD100NoteCount(edittextPurgeC3.getText().toString().trim());
        if (TextUtils.isEmpty(atmCashSourcingDto.getD100NoteCount())) {
            Toast.makeText(context, "Please enter note", Toast.LENGTH_LONG).show();
            edittextPurgeC3.setError("Please enter note.");
            return 7;
        }

        //Total Amount
        int number1 = Integer.parseInt(atmCashSourcingDto.getD2000NoteCount());
        int number2 = Integer.parseInt(atmCashSourcingDto.getD500NoteCount());
        int number3 = Integer.parseInt(atmCashSourcingDto.getD100NoteCount());

        int totalAmount = ((number1 * 2000 + number2 * 500 + number3 * 100));
        atmCashSourcingDto.setAmount("" + totalAmount);

        if (totalAmount <= 0) {
            showMessage("Total Amount should not be 0.");
            return 1;
        }

        if (totalAmount > MAX_TOTAL_AMOUNT) {
            String amt = getCommaUsingPlaceValue(String.valueOf(MAX_TOTAL_AMOUNT));
            showMessage("Total Amount should not exceed " + amt + ".");
            return 1;
        }

        //Status
        atmCashSourcingDto.setStatusCode("1");
        if (TextUtils.isEmpty(atmCashSourcingDto.getStatusCode()) || atmCashSourcingDto.getStatusCode().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Status.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerStatus, "Please select Status.", context);
            return 1;
        }

        return 0;
    }

    private void showDateTimeDialogPicker(final View view) {

        Date defaultDate = null;
        if (selectedDateTimeId == R.id.textViewCashSourcingDate) {
            defaultDate = sourceDate;
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

                        if (selectedDateTimeId == R.id.textViewCashSourcingDate) {
                            sourceDate = datetime;
                            strSourceDate = formateYMD;
                            atmCashSourcingDto.setSourceDate(strSourceDate);

                            String issuingDate = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", atmCashSourcingDto.getSourceDate());
                            textViewCashSourcingDate.setText(issuingDate);
                            textViewCashSourcingDate.setError(null);

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

        if (selectedDateTimeId == R.id.textViewCashSourcingDate) {

            Date minDate = CommonUtils.parseStringToDate(atmCashSourcingDto.getMinSourceDate());
            dateTimePickerDialog.setMinDate(minDate.getTime());
            //dateTimePickerDialog.setMinDate(minCal.getTimeInMillis());
            dateTimePickerDialog.setMaxDate(now);

        }

        dateTimePickerDialog.setCancelable(false);
        dateTimePickerDialog.setActionButtonName("Save");
        dateTimePickerDialog.show();

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
                    String base64Data = CommonUtils.encodeFileToBase64Binary(file);

                    atmCashSourcingDto.setCashSourcingBase64(base64Data);
                    atmCashSourcingDto.setCashSourcingExt(ext);
                    atmCashSourcingDto.setChangedPhoto(true);

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

            setScanCopyData(IsDrawable, imageBitMap, fileName, base64Data);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setScanCopyData(boolean IsDrawable, Bitmap bitmap, String fileName, String base64) {
        if (IsDrawable)
            Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgDocumentProof);
        else
            Glide.with(context).asBitmap().load(bitmap).into(imgDocumentProof);

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
                    FROM = PROOF_SCAN_COPY;

                    String ext = TextUtils.isEmpty(atmCashSourcingDto.getCashSourcingExt()) ? "jpg" : atmCashSourcingDto.getCashSourcingExt();

                    //FOR PDF
                    if (ext.equalsIgnoreCase("pdf")) {
                        SEL_FILE_TYPE = " images";
                        startScanCamera();
                        return;
                    }

                    //FOR Image
                    if ((TextUtils.isEmpty(atmCashSourcingDto.getCashSourcingScanCopyId()) || atmCashSourcingDto.getCashSourcingScanCopyId().equalsIgnoreCase("0")) && TextUtils.isEmpty(atmCashSourcingDto.getCashSourcingBase64())) {
                        SEL_FILE_TYPE = "images";
                        startScanCamera();

                    } else {
                        GSTINDTO previewDto = prepareDtoForPreview(FROM);
                        showImagePreviewDialog(previewDto);
                    }
                }
            }
        });

    }

    private void startScanCamera() {

        //If the app has not the permission then asking for the permission
        permissionHandler.requestMultiplePermission(imgDocumentProof, permissions, getString(R.string.needs_camera_storage_permission_msg), new IPermission() {
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

        atmCashSourcingDto.setChangedPhoto(imageDto.isChangedPhoto());
        atmCashSourcingDto.setCashSourcingScanCopyId(imageDto.getGstImageId());
        atmCashSourcingDto.setBitmap(imageDto.getBitmap());
        atmCashSourcingDto.setCashSourcingExt("jpg");
        atmCashSourcingDto.setCashSourcingBase64(imageDto.getGstImage());

    }

    private GSTINDTO prepareDtoForPreview(int FROM) {
        GSTINDTO imageDto = new GSTINDTO();

        imageDto.setChangedPhoto(atmCashSourcingDto.isChangedPhoto());
        imageDto.setGstImageId(atmCashSourcingDto.getCashSourcingScanCopyId());
        imageDto.setBitmap(atmCashSourcingDto.getBitmap());
        imageDto.setName("");
        imageDto.setGstImage(atmCashSourcingDto.getCashSourcingBase64());
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

                    //Refresh PAN Card Image
                    if (!TextUtils.isEmpty(atmCashSourcingDto.getCashSourcingBase64())) {
                        Bitmap bitmap = CommonUtils.StringToBitMap(atmCashSourcingDto.getCashSourcingBase64());
                        if (bitmap != null)
                            imgDocumentProof.setImageBitmap(bitmap);
                    }
                }
            });
            customImagePreviewDialog.show();
            customImagePreviewDialog.setCancelable(false);

            //Title
            String title = "Cash Withdrawal Document Proof Image";
            customImagePreviewDialog.setDialogTitle(title);

            //Change Photo Allowed
            customImagePreviewDialog.allowChangePhoto(IsEditable);
            customImagePreviewDialog.allowSaveOption(IsEditable);
            customImagePreviewDialog.allowRemarks(false);

        } else {
            Toast.makeText(context, "No photo available to preview.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showImgOrPDF(final View view) {

        boolean IsGSTImgPDF = (atmCashSourcingDto.getCashSourcingExt() != null && atmCashSourcingDto.getCashSourcingExt().equalsIgnoreCase("pdf")) ? true : false;

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int Id = parent.getId();

        if (Id == R.id.spinnerStatus) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto dto3 = (CustomFranchiseeApplicationSpinnerDto) spinnerStatus.getItemAtPosition(position);
                atmCashSourcingDto.setStatusCode(dto3.getId());
                atmCashSourcingDto.setStatus(dto3.getName());
            }

        } else if (Id == R.id.spinnerATMID) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto dto3 = (CustomFranchiseeApplicationSpinnerDto) spinnerATMID.getItemAtPosition(position);
                atmCashSourcingDto.setAtmId(dto3.getId());
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    class GetAllSpinnerData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressSpinner(context);
        }

        @Override
        protected String doInBackground(String... strings) {

            Connection connection = new Connection(context);
            String vkId = connection.getVkid();

            atmIDList = atmLoadingRepo.getAllATMList(vkId);
            statusList = atmLoadingRepo.getCashSourcingStatusList(vkId);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissProgressSpinner();

            bindData();

        }

    }

    private void setSpinnerAdapter(List<CustomFranchiseeApplicationSpinnerDto> list, Spinner spinner, String selectedId) {

        CustomFranchiseeApplicationSpinnerAdapter brandNameAdapter = new CustomFranchiseeApplicationSpinnerAdapter(context, list);
        spinner.setAdapter(brandNameAdapter);
        int pos = atmLoadingRepo.getSelectedPos(list, selectedId);
        spinner.setSelection(pos);
        spinner.setOnItemSelectedListener(this);

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

}
