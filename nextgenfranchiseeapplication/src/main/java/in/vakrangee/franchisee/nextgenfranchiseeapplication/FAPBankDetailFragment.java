package in.vakrangee.franchisee.nextgenfranchiseeapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nononsenseapps.filepicker.Utils;

import java.io.File;
import java.net.URLDecoder;
import java.util.List;

import androidx.core.content.FileProvider;
import butterknife.ButterKnife;
import in.vakrangee.franchisee.nextgenfranchiseeapplication.bank_statement.AllBankStatementActivity;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
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

public class FAPBankDetailFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Context context;
    private DeprecateHandler deprecateHandler;
    private PermissionHandler permissionHandler;

    private int FROM = -1;
    private static final int PASS_CHEQUE_BOOK_COPY = 1;
    private FAPBankDetailsDto fapBankDetailsDto;
    private String SEL_FILE_TYPE;
    private FileAttachmentDialog fileAttachementDialog;
    private static final int CAMERA_PIC_REQUEST = 100;
    private static final int BROWSE_FOLDER_REQUEST = 101;
    private Uri picUri;
    private List<CustomFranchiseeApplicationSpinnerDto> accountTypeList;
    private List<CustomFranchiseeApplicationSpinnerDto> BankNameTypeList;
    private FranchiseeApplicationRepository fapRepo;
    private GetAllBankDetailSpinnerData getAllBankDetailSpinnerData = null;
    private boolean IsEditable = false;

    //region refrence
    private CustomSearchableSpinner spinnerBankName;
    private EditText editTextAccountHolderName;
    private EditText editTextBranchName;
    private EditText editTextAccountNumber1;
    private EditText editTextAccountNumber2;
    private Spinner spinnerAccountType;
    private EditText editTextIFSCCode1;
    private EditText editTextIFSCCode2;

    //endregion
    //region Labels
    private TextView txtBankNameLbl;
    private TextView txtAccountHolderNameLbl;
    private TextView txtBranchNameLbl;
    private TextView txtAccountNumberLbl;
    private TextView txtAccountTypeLbl;
    private TextView txtIFSCCodeLbl;
    private TextView txtPassbookChequeBookChooseFile;
    //endregion
    private LinearLayout layoutBankParent,layoutPassBookChequeBook;
    private TextView txtNotes;
    private static final String SPECIAL_CHARS = "~#^|$%&*!";
    private MaterialButton btnAddBankStatement;
    private static final int BANK_STATEMENT_REQUEST = 102;

    private LinearLayout layoutBankOldWay,layoutAddBankStatement;
    private ImageView imgPassBookChequeBook;
    private TextView txtPassbookChequeBookCopyName;
    private boolean isBankStmtNewWay = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_fap_bank, container, false);

        bindViewID(rootView);
        //Data
        this.context = getContext();
        deprecateHandler = new DeprecateHandler(context);
        permissionHandler = new PermissionHandler(getActivity());
        fapRepo = new FranchiseeApplicationRepository(context);
        ButterKnife.bind(this, rootView);

        //Input filter
        CommonUtils.InputFiletrWithMaxLength(editTextAccountHolderName, SPECIAL_CHARS, 50);
        CommonUtils.InputFiletrWithMaxLength(editTextBranchName, SPECIAL_CHARS, 50);
        CommonUtils.InputFiletrWithMaxLength(editTextIFSCCode1, SPECIAL_CHARS, 11);
        CommonUtils.InputFiletrWithMaxLength(editTextIFSCCode2, SPECIAL_CHARS, 11);

        //Set Compulsory mark
        TextView[] txtViewsForCompulsoryMark = {txtBankNameLbl, txtAccountHolderNameLbl, txtBranchNameLbl, txtAccountNumberLbl, txtAccountTypeLbl, txtIFSCCodeLbl,
                txtPassbookChequeBookChooseFile};
        GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);
        TextChangedListener_IFSCCode();
        TextChangedListener_AccountNumber();

        //Consent Text
        String str = "";
        str += ("\u2022 \t" + getContext().getResources().getString(R.string.bank_note1) + "\n");
        str += ("\u2022 \t" + getContext().getResources().getString(R.string.bank_note2) + "\n");
        str += ("\u2022 \t" + getContext().getResources().getString(R.string.bank_note3));
        txtNotes.setText(str);

        return rootView;
    }

    private void bindViewID(View view) {
        //region refrence
        spinnerBankName = view.findViewById(R.id.spinnerBankName);
        editTextAccountHolderName = view.findViewById(R.id.editTextAccountHolderName);
        editTextBranchName = view.findViewById(R.id.editTextBranchName);
        editTextAccountNumber1 = view.findViewById(R.id.editTextAccountNumber1);
        editTextAccountNumber2 = view.findViewById(R.id.editTextAccountNumber2);
        spinnerAccountType = view.findViewById(R.id.spinnerAccountType);
        editTextIFSCCode1 = view.findViewById(R.id.editTextIFSCCode1);
        editTextIFSCCode2 = view.findViewById(R.id.editTextIFSCCode2);

        //endregion
        //region Labels
        txtBankNameLbl = view.findViewById(R.id.txtBankNameLbl);
        txtAccountHolderNameLbl = view.findViewById(R.id.txtAccountHolderNameLbl);
        txtBranchNameLbl = view.findViewById(R.id.txtBranchNameLbl);
        txtAccountNumberLbl = view.findViewById(R.id.txtAccountNumberLbl);
        txtAccountTypeLbl = view.findViewById(R.id.txtAccountTypeLbl);
        txtIFSCCodeLbl = view.findViewById(R.id.txtIFSCCodeLbl);
        txtPassbookChequeBookChooseFile = view.findViewById(R.id.txtPassbookChequeBookChooseFile);
        //endregion

        layoutPassBookChequeBook = view.findViewById(R.id.layoutPassBookChequeBook);
        layoutBankOldWay = view.findViewById(R.id.layoutBankOldWay);
        imgPassBookChequeBook = view.findViewById(R.id.imgPassBookChequeBook);
        txtPassbookChequeBookCopyName = view.findViewById(R.id.txtPassbookChequeBookCopyName);
        layoutAddBankStatement = view.findViewById(R.id.layoutAddBankStatement);

        btnAddBankStatement = view.findViewById(R.id.btnAddBankStatement);
        layoutBankParent = view.findViewById(R.id.layoutBankParent);
        txtNotes = view.findViewById(R.id.txtNotes);

        imgPassBookChequeBook.setOnClickListener(this);
        spinnerAccountType.setOnItemSelectedListener(this);
        btnAddBankStatement.setOnClickListener(this);
    }

    //region region TextChangedListener_IFSCCode
    private void TextChangedListener_IFSCCode() {
        CommonUtils.EditextListener(editTextIFSCCode1, editTextIFSCCode2, 11, "Please enter a valid IFSC Code.", "IFSCCode");

        CommonUtils.EditextListener(editTextIFSCCode1, editTextIFSCCode2, 11, "Please enter a valid IFSC Code.", "IFSCCode");

    }
    //endregion

    //region TextChangedListener_AccountNumber
    private void TextChangedListener_AccountNumber() {
        CommonUtils.EditextListener(editTextAccountNumber1, editTextAccountNumber2, 10, "Account number must be 10 characters long", "AccountNumber");

        CommonUtils.EditextListener(editTextAccountNumber1, editTextAccountNumber2, 10, "Account number must be 10 characters long", "AccountNumber");

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

      /*  if (Id == R.id.imgPassBookChequeBook) {
            FROM = PASS_CHEQUE_BOOK_COPY;
            SEL_FILE_TYPE = "images/pdf";
            showAttachmentDialog(view, SEL_FILE_TYPE);

        } else */
        if (Id == R.id.btnAddBankStatement) {
            String aId = ((NextGenFranchiseeApplicationFormFragment) getParentFragment()).applicationFormDto.getNextgenApplicationId();
            String appId = TextUtils.isEmpty(aId) ? "0" : aId;

            Intent intent = new Intent(context, AllBankStatementActivity.class);
            intent.putExtra("APP_ID", appId);
            startActivityForResult(intent, BANK_STATEMENT_REQUEST);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getAllBankDetailSpinnerData != null && !getAllBankDetailSpinnerData.isCancelled()) {
            getAllBankDetailSpinnerData.cancel(true);
        }
    }

    public int IsFranchiseeBankDetailsValidated() {

        //1- Bank Name
        if (TextUtils.isEmpty(fapBankDetailsDto.getBankName())) {
            Toast.makeText(context, "Please select Bank Name.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerBankName, "Please select Bank Name.", context);
            return 1;
        }
        //2-Account Holder's Name
        fapBankDetailsDto.setAccountHolderName(editTextAccountHolderName.getText().toString().trim());
        if (TextUtils.isEmpty(fapBankDetailsDto.getAccountHolderName())) {
            Toast.makeText(context, "Please enter Account Holder's Name.", Toast.LENGTH_LONG).show();
            editTextAccountHolderName.setError("Please enter Account Holder's Name.");
            return 2;
        }
        //3-Branch Name
        fapBankDetailsDto.setBranchName(editTextBranchName.getText().toString().trim());
        if (TextUtils.isEmpty(fapBankDetailsDto.getBranchName())) {
            Toast.makeText(context, "Please enter Branch Name.", Toast.LENGTH_LONG).show();
            editTextBranchName.setError("Please enter Branch Name.");
            return 3;
        }
        //4-Account Number
        fapBankDetailsDto.setAccountNumber(editTextAccountNumber1.getText().toString().trim());
        if (TextUtils.isEmpty(fapBankDetailsDto.getAccountNumber())) {
            Toast.makeText(context, "Please enter Account Number.", Toast.LENGTH_LONG).show();
            editTextAccountNumber1.setError("Please enter Account Number.");
            return 4;
        }
        //5-Account Number
        if (TextUtils.isEmpty(fapBankDetailsDto.getAccountNumber())) {
            Toast.makeText(context, "Please re-enter Account Number.", Toast.LENGTH_LONG).show();
            editTextAccountNumber2.setError("Please re-enter Account Number.");
            return 5;
        }
        //6 -Account Type
        if (TextUtils.isEmpty(fapBankDetailsDto.getAccountType()) || fapBankDetailsDto.getAccountType().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Account Type.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerAccountType, "Please select Account Type.", context);
            return 6;
        }
        //7-IFSC Code
        fapBankDetailsDto.setiFSCCode(editTextIFSCCode1.getText().toString().trim());
        if (TextUtils.isEmpty(fapBankDetailsDto.getiFSCCode()) && !CommonUtils.IsIFSCCodeValid(editTextIFSCCode1.getText().toString().trim())) {
            Toast.makeText(context, "Please enter proper IFSC Code Number.", Toast.LENGTH_LONG).show();
            editTextIFSCCode1.setError("Please enter proper IFSC Code Number.");
            return 7;
        }
        //8-IFSC Code
        if (TextUtils.isEmpty(fapBankDetailsDto.getiFSCCode())) {
            Toast.makeText(context, "Please re-enter IFSC Code Number.", Toast.LENGTH_LONG).show();
            editTextIFSCCode2.setError("Please re-enter IFSC Code Number.");
            return 8;
        }

        //Bank Statement
        if (isBankStmtNewWay) {

            //Last 6 Months Bank Statement
            if (TextUtils.isEmpty(fapBankDetailsDto.getIsBankStmtAvailable()) || fapBankDetailsDto.getIsBankStmtAvailable().equalsIgnoreCase("0")) {
                Toast.makeText(context, "Please add Last 6 Months Bank Statement.", Toast.LENGTH_LONG).show();
                showMessage("Please add Last 6 Months Bank Statement.");
                return 6;
            }

            //Check Last 6 Months Bank Statement is Valid
            if (TextUtils.isEmpty(fapBankDetailsDto.getIsBankStmtValid()) || fapBankDetailsDto.getIsBankStmtValid().equalsIgnoreCase("0")) {
                Toast.makeText(context, fapBankDetailsDto.getIsBankStmtValidMsg(), Toast.LENGTH_LONG).show();
                showMessage(fapBankDetailsDto.getIsBankStmtValidMsg());
                return 6;
            }

        } else {

            //Pass Book/Bank Statement/ Cancelled Cheque
            if (TextUtils.isEmpty(fapBankDetailsDto.getChequeFileId()) || fapBankDetailsDto.getChequeFileId().equalsIgnoreCase("0")) {
                if (TextUtils.isEmpty(fapBankDetailsDto.getPassBookChequeBase64())) {
                    showMessage("Please choose Pass Book/Bank Statement/ Cancelled Cheque photo.");
                    return 9;
                }
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
            if (requestCode == BANK_STATEMENT_REQUEST && resultCode == Activity.RESULT_OK) {
                try {

                    if (data != null) {
                        String isBankAvail = data.getStringExtra("IS_BANK_AVAILABLE");
                        String bankStmtValid = data.getStringExtra("IS_BANK_STM_VALID");
                        String bankStmtValidMsg = data.getStringExtra("IS_BANK_STM_VALID_MSG");

                        fapBankDetailsDto.setIsBankStmtAvailable(isBankAvail);
                        fapBankDetailsDto.setIsBankStmtValid(bankStmtValid);
                        fapBankDetailsDto.setIsBankStmtValidMsg(bankStmtValidMsg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
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
/*
        switch (FROM) {

            case PASS_CHEQUE_BOOK_COPY:
                if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgPassBookChequeBook);
                else
                    Glide.with(context).asBitmap().load(bitmap).into(imgPassBookChequeBook);

                txtPassbookChequeBookCopyName.setVisibility(View.VISIBLE);
                txtPassbookChequeBookCopyName.setText(fileName);
                fapBankDetailsDto.setPassBookChequeBase64(base64);
                fapBankDetailsDto.setChequeFileExt(ext);
                fapBankDetailsDto.setPassBookChequeFileName(fileName);
                break;

            default:
                break;

        }*/
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
                            picUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName()
                                    + ".provider", file);
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

    public void reloadData(String data, boolean IsEditable) {
        //Reload Data
        if (TextUtils.isEmpty(data))
            fapBankDetailsDto = new FAPBankDetailsDto();
        else {
            try {
                Gson gson = new GsonBuilder().create();
                fapBankDetailsDto = gson.fromJson(data, FAPBankDetailsDto.class);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.IsEditable = IsEditable;
        isBankStmtNewWay = (!TextUtils.isEmpty(fapBankDetailsDto.getIsBankStmtNewWay()) && fapBankDetailsDto.getIsBankStmtNewWay().equalsIgnoreCase("1")) ? true : false;
        getAllBankDetailSpinnerData = new GetAllBankDetailSpinnerData();
        getAllBankDetailSpinnerData.execute("");
    }

    private void spinner_focusablemode(CustomSearchableSpinner spinner) {
        spinner.setFocusable(true);
        spinner.setFocusableInTouchMode(true);
    }

    private void bindSpinner() {

        spinner_focusablemode(spinnerBankName);

        //STEP 1: Bank Name
        spinnerBankName.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, BankNameTypeList));
        int banknameTypePos = fapRepo.getSelectedPos(BankNameTypeList, fapBankDetailsDto.getBankName());
        spinnerBankName.setSelection(banknameTypePos);
        spinnerBankName.setOnItemSelectedListener(this);

        //STEP 2: Account Type
        CustomFranchiseeApplicationSpinnerAdapter accountTypeAdapter = new CustomFranchiseeApplicationSpinnerAdapter(context, accountTypeList);
        spinnerAccountType.setAdapter(accountTypeAdapter);
        int acTypePos = fapRepo.getSelectedPos(accountTypeList, fapBankDetailsDto.getAccountType());
        spinnerAccountType.setSelection(acTypePos);
        spinnerAccountType.setOnItemSelectedListener(this);

        //STEP 3 :Account Holder Name
        editTextAccountHolderName.setText(fapBankDetailsDto.getAccountHolderName());
        //STEP 4 :Bank Name
        editTextBranchName.setText(fapBankDetailsDto.getBranchName());
        //STEP 5 :Account Number Name
        editTextAccountNumber1.setText(fapBankDetailsDto.getAccountNumber());
        //STEP 6 :re-enter Account Number  Name
        editTextAccountNumber2.setText(fapBankDetailsDto.getAccountNumber());
        //STEP 7 :IFSC code  Name
        editTextIFSCCode1.setText(fapBankDetailsDto.getiFSCCode());
        //STEP 8 : re-enter IFSC code  Name
        editTextIFSCCode2.setText(fapBankDetailsDto.getiFSCCode());

        //STEP 9: Cheque book Image
        boolean IsPDF = ((fapBankDetailsDto.getChequeFileExt() != null) && fapBankDetailsDto.getChequeFileExt().equalsIgnoreCase("pdf")) ? true : false;

        if (isBankStmtNewWay) {
            txtPassbookChequeBookChooseFile.setText(""+context.getString(R.string.last_6_months_bank_statement));
            layoutBankOldWay.setVisibility(View.GONE);
            layoutAddBankStatement.setVisibility(View.VISIBLE);
        } else {
            txtPassbookChequeBookChooseFile.setText(""+context.getString(R.string.pass_book_cheque));
            layoutBankOldWay.setVisibility(View.VISIBLE);
            layoutAddBankStatement.setVisibility(View.GONE);

            if (IsPDF) {
                Glide.with(context).asDrawable().load(R.drawable.pdf).into(imgPassBookChequeBook);
            } else {
                if (!TextUtils.isEmpty(fapBankDetailsDto.getChequeFileId())) {
                    String chequeUrl = Constants.DownloadImageUrl + fapBankDetailsDto.getChequeFileId();
                    Glide.with(context)
                            .load(chequeUrl)
                            .apply(new RequestOptions()
                                    .error(R.drawable.ic_camera_alt_black_72dp)
                                    .placeholder(R.drawable.ic_camera_alt_black_72dp)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true))
                            .into(imgPassBookChequeBook);
                }
            }
        }

        //Enable/disable views
        GUIUtils.setViewAndChildrenEnabled(layoutBankParent, IsEditable);
        GUIUtils.setViewAndChildrenEnabled(layoutPassBookChequeBook, ((NextGenFranchiseeApplicationFormFragment) getParentFragment()).isBankStmtEditable);
    }

    class GetAllBankDetailSpinnerData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            //STEP 1: Bank Name
            BankNameTypeList = fapRepo.getBankNameList();

            //STEP 2: Account Type
            accountTypeList = fapRepo.getAccountTypeList();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            bindSpinner();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long ID) {
        int id = parent.getId();

        if (id == R.id.spinnerAccountType) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto accTypeDto = (CustomFranchiseeApplicationSpinnerDto) spinnerAccountType.getItemAtPosition(position);
                fapBankDetailsDto.setAccountType(accTypeDto.getId());

            }
        } else if (id == R.id.spinnerBankName) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto banknameDto = (CustomFranchiseeApplicationSpinnerDto) spinnerBankName.getItemAtPosition(position);
                fapBankDetailsDto.setBankName(banknameDto.getId());
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //Do Nothing
    }

    public FAPBankDetailsDto getFapBankDetailsDto() {
        fapBankDetailsDto.setAccountHolderName(editTextAccountHolderName.getText().toString());
        fapBankDetailsDto.setBranchName(editTextBranchName.getText().toString());
        fapBankDetailsDto.setAccountNumber(editTextAccountNumber1.getText().toString());
        fapBankDetailsDto.setiFSCCode(editTextIFSCCode1.getText().toString());

        return fapBankDetailsDto;
    }
}
