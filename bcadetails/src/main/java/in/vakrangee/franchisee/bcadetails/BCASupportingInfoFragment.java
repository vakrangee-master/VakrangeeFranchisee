package in.vakrangee.franchisee.bcadetails;

import android.Manifest;
import android.app.Activity;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.io.File;
import java.net.URLDecoder;
import java.util.List;

import butterknife.ButterKnife;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.commongui.FileAttachmentDialog;
import in.vakrangee.supercore.franchisee.commongui.FileUtils;
import in.vakrangee.supercore.franchisee.commongui.FilteredFilePickerActivity;
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

public class BCASupportingInfoFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "BCASupportingInfoFragment";

    private View view;
    private Context context;
    private PermissionHandler permissionHandler;
    private Logger logger;
    private DeprecateHandler deprecateHandler;

    private BCASupportingInformationDto bcaSupportingInformationDto;
    private boolean IsEditable = false;
    private GetAllSpinnerData getAllSpinnerData = null;

    private BCAEntryDetailsRepository bcaEntryDetailsRepo;
    private List<CustomFranchiseeApplicationSpinnerDto> supervisorCodeList, deviceTypeList;


    private boolean edittext_listener = true;
    private FileAttachmentDialog fileAttachementDialog;
    private static final int CAMERA_PIC_REQUEST = 100;
    private static final int BROWSE_FOLDER_REQUEST = 101;
    private Uri picUri;                 //Picture URI
    private String SEL_FILE_TYPE;
    private int FROM = -1;

    private static final int AADHAAR_CARD_SCAN_COPY = 1;
    private static final int PAN_CARD_SCAN_COPY = 2;
    private static final int SETTLEMENT_ACCOUNT_COPY = 3;
    private static final int OUTLET_ADDRESS_COPY = 4;
    private static final int IIBF_CERTIFICATION_COPY = 5;
    private static final int BCA_POLICE_VERIFICATION_COPY = 6;

    private LinearLayout layoutSupportingParent;
    private TextView txtEmailIdLbl;
    private EditText editTextEmailID;
    private TextView txtNameAsPerPANCardLbl;
    private EditText editTextNameAsPerPANCard;
    private TextView txtAadhaarNumberLbl;
    private EditText editTextAadhaarNumber1;
    private EditText editTextAadhaarNumber2;
    private TextView txtPANNumberLbl;
    private EditText editTextPANNumber1;
    private EditText editTextPANNumber2;
    private TextView txtAadharCardScanCopyLbl;
    private ImageView imgAadhaarScanCopy;
    private TextView txtPanCardScanCopyLbl;
    private ImageView imgPanScanCopy;
    private TextView txtSettlementAccountCopyLbl;
    private ImageView imgSettlementAccountCopy;
    private TextView txtOutletAddressProofLbl;
    private ImageView imgOutletAddressProof;
    private TextView txtIIBFCertificationCopyLbl;
    private ImageView imgIIBFCertificationCopy;
    private TextView txtBCAPoliceVerificationLbl;
    private ImageView imgBCAPoliceVerification;
    private LinearLayout layoutBgEmailId;

    public BCASupportingInfoFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag_bca_supporting_info, container, false);

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

        //Input filter
        CommonUtils.InputFiletrWithMaxLength(editTextEmailID, "~#^|$%&*!", 50);
        CommonUtils.InputFiletrWithMaxLength(editTextNameAsPerPANCard, "~#^|$%&*!", 50);
        CommonUtils.InputFiletrWithMaxLength(editTextPANNumber1, "~#^|$%&*!", 10);
        CommonUtils.InputFiletrWithMaxLength(editTextPANNumber2, "~#^|$%&*!", 10);

        //Email Id
        editTextEmailID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int len = editTextEmailID.getText().toString().trim().length();
                if (len <= 0)
                    return;

                boolean IsMatched = editTextEmailID.getText().toString().trim().matches(CommonUtils.emailPattern);
                if (!IsMatched) {
                    editTextEmailID.setTextColor(Color.parseColor("#000000"));
                    editTextEmailID.setError(getResources().getString(R.string.InvalidEmail));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                if (editTextEmailID.getText().toString().contains(" ")) {
                    editTextEmailID.setText(editTextEmailID.getText().toString().replaceAll(" ", ""));
                    editTextEmailID.setSelection(editTextEmailID.getText().length());
                }

                boolean IsMatched = editTextEmailID.getText().toString().trim().matches(CommonUtils.emailPattern);
                if (IsMatched) {
                    editTextEmailID.setTextColor(Color.parseColor("#468847"));
                    editTextEmailID.setError(null);
                }
            }
        });

        TextChangedListenerPANcard();
        TextChangedListener_AadhaarCard();

        return view;
    }

    private void bindViewId(View view) {
        layoutSupportingParent = view.findViewById(R.id.layoutSupportingParent);
        txtEmailIdLbl = view.findViewById(R.id.txtEmailIdLbl);
        editTextEmailID = view.findViewById(R.id.editTextEmailID);
        txtNameAsPerPANCardLbl = view.findViewById(R.id.txtNameAsPerPANCardLbl);
        editTextNameAsPerPANCard = view.findViewById(R.id.editTextNameAsPerPANCard);
        txtAadhaarNumberLbl = view.findViewById(R.id.txtAadhaarNumberLbl);
        editTextAadhaarNumber1 = view.findViewById(R.id.editTextAadhaarNumber1);
        editTextAadhaarNumber2 = view.findViewById(R.id.editTextAadhaarNumber2);
        txtPANNumberLbl = view.findViewById(R.id.txtPANNumberLbl);
        editTextPANNumber1 = view.findViewById(R.id.editTextPANNumber1);
        editTextPANNumber2 = view.findViewById(R.id.editTextPANNumber2);
        txtAadharCardScanCopyLbl = view.findViewById(R.id.txtAadharCardScanCopyLbl);
        imgAadhaarScanCopy = view.findViewById(R.id.imgAadhaarScanCopy);
        txtPanCardScanCopyLbl = view.findViewById(R.id.txtPanCardScanCopyLbl);
        imgPanScanCopy = view.findViewById(R.id.imgPanScanCopy);
        txtSettlementAccountCopyLbl = view.findViewById(R.id.txtSettlementAccountCopyLbl);
        imgSettlementAccountCopy = view.findViewById(R.id.imgSettlementAccountCopy);
        txtOutletAddressProofLbl = view.findViewById(R.id.txtOutletAddressProofLbl);
        imgOutletAddressProof = view.findViewById(R.id.imgOutletAddressProof);
        txtIIBFCertificationCopyLbl = view.findViewById(R.id.txtIIBFCertificationCopyLbl);
        imgIIBFCertificationCopy = view.findViewById(R.id.imgIIBFCertificationCopy);
        txtBCAPoliceVerificationLbl = view.findViewById(R.id.txtBCAPoliceVerificationLbl);
        imgBCAPoliceVerification = view.findViewById(R.id.imgBCAPoliceVerification);
        layoutBgEmailId = view.findViewById(R.id.layoutBgEmailId);

        imgAadhaarScanCopy.setOnClickListener(this);
        imgPanScanCopy.setOnClickListener(this);
        imgSettlementAccountCopy.setOnClickListener(this);
        imgOutletAddressProof.setOnClickListener(this);
        imgIIBFCertificationCopy.setOnClickListener(this);
        imgBCAPoliceVerification.setOnClickListener(this);


    }

    //region TextChangedListener_AadhaarCard
    private void TextChangedListener_AadhaarCard() {
        CommonUtils.EditextListener(editTextAadhaarNumber1, editTextAadhaarNumber2, 12, "Please enter a valid Aadhaar number.", "Aadhaarcard");
    }
    //endregion

    //region TextChangedListener_GSTIN and PANcard
    private void TextChangedListenerPANcard() {

        editTextPANNumber1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

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
                    } else if (!strPass1.equalsIgnoreCase(strPass2)) {
                        editTextPANNumber1.setError("Not Match");
                        editTextPANNumber1.setTextColor(Color.parseColor("#000000"));
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

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int len = editTextPANNumber2.getText().toString().trim().length();
                if (len <= 0)
                    return;

                if (start != 10) {
                    editTextPANNumber2.setTextColor(Color.parseColor("#000000"));
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
                } else if (!strPass1.equalsIgnoreCase(strPass2)) {
                    editTextPANNumber2.setError("Not Match");
                    editTextPANNumber2.setTextColor(Color.parseColor("#000000"));
                } else {
                    editTextPANNumber2.setTextColor(Color.parseColor("#468847"));
                    editTextPANNumber2.setError(null);
                    editTextPANNumber1.setTextColor(Color.parseColor("#468847"));
                    editTextPANNumber1.setError(null);

                }
            }
        });


    }
    //endregion

    public void setCompulsoryMarkLabel() {
        TextView[] txtViewsForCompulsoryMark = {txtEmailIdLbl, txtNameAsPerPANCardLbl, txtAadhaarNumberLbl, txtPANNumberLbl, txtAadharCardScanCopyLbl
                , txtPanCardScanCopyLbl, txtSettlementAccountCopyLbl, txtOutletAddressProofLbl};
        GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);
    }

    public int IsSupportingInformationValidated() {

        //STEP 1: Email ID
        bcaSupportingInformationDto.setEmailId(editTextEmailID.getText().toString().trim());
        if (TextUtils.isEmpty(bcaSupportingInformationDto.getEmailId()) && !editTextEmailID.getText().toString().trim().matches(CommonUtils.emailPattern)) {
            Toast.makeText(context, "Please enter Email ID.", Toast.LENGTH_LONG).show();
            editTextEmailID.setError("Please enter Email ID.");
            return 1;
        }

        //STEP 2: Name as per PAN Card
        bcaSupportingInformationDto.setNameAsPanCard(editTextNameAsPerPANCard.getText().toString().trim());
        if (TextUtils.isEmpty(bcaSupportingInformationDto.getNameAsPanCard())) {
            Toast.makeText(context, "Please enter Name as mentioned on PAN Card.", Toast.LENGTH_LONG).show();
            editTextNameAsPerPANCard.setError("Please enter Name as mentioned on PAN Card.");
            return 1;
        }

        //STEP 3.1: Aadhaar Number
        bcaSupportingInformationDto.setAadhaarNumber(editTextAadhaarNumber1.getText().toString().trim());
        if (TextUtils.isEmpty(bcaSupportingInformationDto.getAadhaarNumber()) && !CommonUtils.validAadharNumber(editTextAadhaarNumber1.getText().toString())) {
            Toast.makeText(context, "Please enter Aadhaar Number.", Toast.LENGTH_LONG).show();
            editTextAadhaarNumber1.setError("Please enter Aadhaar Number.");
            return 1;
        }

        //STEP 3.2: Confirm Aadhaar Number
        if (TextUtils.isEmpty(bcaSupportingInformationDto.getAadhaarNumber()) && !CommonUtils.validAadharNumber(editTextAadhaarNumber2.getText().toString())) {
            Toast.makeText(context, "Please enter Confirm Aadhaar Number.", Toast.LENGTH_LONG).show();
            editTextAadhaarNumber2.setError("Please enter Confirm Aadhaar Number.");
            return 1;
        }

        //STEP 4.1: PAN Number
        bcaSupportingInformationDto.setPanNumber(editTextPANNumber1.getText().toString().trim());
        if (TextUtils.isEmpty(bcaSupportingInformationDto.getPanNumber()) && !CommonUtils.validAadharNumber(editTextPANNumber1.getText().toString())) {
            Toast.makeText(context, "Please enter PAN Number.", Toast.LENGTH_LONG).show();
            editTextPANNumber1.setError("Please enter PAN Number.");
            return 1;
        }

        //STEP 4.2: Confirm PAN Number
        if (TextUtils.isEmpty(bcaSupportingInformationDto.getPanNumber()) && !CommonUtils.validAadharNumber(editTextPANNumber2.getText().toString())) {
            Toast.makeText(context, "Please enter Confirm PAN Number.", Toast.LENGTH_LONG).show();
            editTextPANNumber2.setError("Please enter Confirm PAN Number.");
            return 1;
        }

        //STEP 5: Aadhaar Card Scan Copy
        if (TextUtils.isEmpty(bcaSupportingInformationDto.getAadhaarCardScanId()) || bcaSupportingInformationDto.getAadhaarCardScanId().equalsIgnoreCase("0")) {
            if (TextUtils.isEmpty(bcaSupportingInformationDto.getAadhaarCardScanBase64())) {
                showMessage("Please add Aadhaar Card Scan Copy.");
                return 1;
            }
        }

        //STEP 6: PAN Card Scan Copy
        if (TextUtils.isEmpty(bcaSupportingInformationDto.getPanCardScanId()) || bcaSupportingInformationDto.getPanCardScanId().equalsIgnoreCase("0")) {
            if (TextUtils.isEmpty(bcaSupportingInformationDto.getPanCardScanBase64())) {
                showMessage("Please add PAN Card Scan Copy.");
                return 1;
            }
        }

        //STEP 7: Settlement Account Copy
        if (TextUtils.isEmpty(bcaSupportingInformationDto.getSettlemtAccountCopyId()) || bcaSupportingInformationDto.getSettlemtAccountCopyId().equalsIgnoreCase("0")) {
            if (TextUtils.isEmpty(bcaSupportingInformationDto.getSettlemtAccountCopyBase64())) {
                showMessage("Please add Settlement Account Copy.");
                return 1;
            }
        }

        //STEP 8: Outlet Address Copy
        if (TextUtils.isEmpty(bcaSupportingInformationDto.getOutletAddProofId()) || bcaSupportingInformationDto.getOutletAddProofId().equalsIgnoreCase("0")) {
            if (TextUtils.isEmpty(bcaSupportingInformationDto.getOutletAddProofBase64())) {
                showMessage("Please add Outlet Address Copy.");
                return 1;
            }
        }

        return 0;
    }

    public BCASupportingInformationDto getBCASupportingInformationDto() {

        bcaSupportingInformationDto.setEmailId(editTextEmailID.getText().toString());
        bcaSupportingInformationDto.setNameAsPanCard(editTextNameAsPerPANCard.getText().toString());
        bcaSupportingInformationDto.setAadhaarNumber(editTextAadhaarNumber1.getText().toString());
        bcaSupportingInformationDto.setPanNumber(editTextPANNumber1.getText().toString());

        return bcaSupportingInformationDto;
    }

    class GetAllSpinnerData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressSpinner(context);
        }

        @Override
        protected String doInBackground(String... strings) {

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissProgressSpinner();
            bindSpinner();
        }
    }

    public void reloadData(String data, boolean IsEditable) {
        //Reload Data
        if (TextUtils.isEmpty(data))
            bcaSupportingInformationDto = new BCASupportingInformationDto();
        else {
            try {
                Gson gson = new GsonBuilder().create();
                bcaSupportingInformationDto = gson.fromJson(data, BCASupportingInformationDto.class);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.IsEditable = IsEditable;
        getAllSpinnerData = new GetAllSpinnerData();
        getAllSpinnerData.execute("");
    }

    private void bindSpinner() {

        //STEP 1: Email ID
        editTextEmailID.setText(bcaSupportingInformationDto.getEmailId());

        //STEP 2: Name as per PAN Card
        editTextNameAsPerPANCard.setText(bcaSupportingInformationDto.getNameAsPanCard());

        //STEP 3: Aadhaar Number
        editTextAadhaarNumber1.setText(bcaSupportingInformationDto.getAadhaarNumber());
        editTextAadhaarNumber2.setText(bcaSupportingInformationDto.getAadhaarNumber());

        //STEP 4: PAN Card Number
        editTextPANNumber1.setText(bcaSupportingInformationDto.getPanNumber());
        editTextPANNumber2.setText(bcaSupportingInformationDto.getPanNumber());

        //STEP 5:  Aadhaar Card Scan Copy
        if (!TextUtils.isEmpty(bcaSupportingInformationDto.getAadhaarCardScanId())) {
            String gstUrl = Constants.DownloadImageUrl + bcaSupportingInformationDto.getAadhaarCardScanId();
            Glide.with(context)
                    .load(gstUrl)
                    .apply(new RequestOptions()
                            .error(R.drawable.ic_camera_alt_black_72dp)
                            .placeholder(R.drawable.ic_camera_alt_black_72dp)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgAadhaarScanCopy);
        }

        //STEP 6:  Pan Card Scan Copy
        if (!TextUtils.isEmpty(bcaSupportingInformationDto.getPanCardScanId())) {
            String gstUrl = Constants.DownloadImageUrl + bcaSupportingInformationDto.getPanCardScanId();
            Glide.with(context)
                    .load(gstUrl)
                    .apply(new RequestOptions()
                            .error(R.drawable.ic_camera_alt_black_72dp)
                            .placeholder(R.drawable.ic_camera_alt_black_72dp)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgPanScanCopy);
        }

        //STEP 7:  Settlement Account Copy
        if (!TextUtils.isEmpty(bcaSupportingInformationDto.getSettlemtAccountCopyId())) {
            String gstUrl = Constants.DownloadImageUrl + bcaSupportingInformationDto.getSettlemtAccountCopyId();
            Glide.with(context)
                    .load(gstUrl)
                    .apply(new RequestOptions()
                            .error(R.drawable.ic_camera_alt_black_72dp)
                            .placeholder(R.drawable.ic_camera_alt_black_72dp)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgSettlementAccountCopy);
        }

        //STEP 8:  Outlet Address Proof Copy
        if (!TextUtils.isEmpty(bcaSupportingInformationDto.getOutletAddProofId())) {
            String gstUrl = Constants.DownloadImageUrl + bcaSupportingInformationDto.getOutletAddProofId();
            Glide.with(context)
                    .load(gstUrl)
                    .apply(new RequestOptions()
                            .error(R.drawable.ic_camera_alt_black_72dp)
                            .placeholder(R.drawable.ic_camera_alt_black_72dp)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgOutletAddressProof);
        }

        //STEP 9:  IIBF Certification Copy
        if (!TextUtils.isEmpty(bcaSupportingInformationDto.getIibfCertificationCopyId())) {
            String gstUrl = Constants.DownloadImageUrl + bcaSupportingInformationDto.getIibfCertificationCopyId();
            Glide.with(context)
                    .load(gstUrl)
                    .apply(new RequestOptions()
                            .error(R.drawable.ic_camera_alt_black_72dp)
                            .placeholder(R.drawable.ic_camera_alt_black_72dp)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgIIBFCertificationCopy);
        }

        //STEP 10:  BCA Police Verification Copy
        if (!TextUtils.isEmpty(bcaSupportingInformationDto.getBcaPoliceVerificationId())) {
            String gstUrl = Constants.DownloadImageUrl + bcaSupportingInformationDto.getBcaPoliceVerificationId();
            Glide.with(context)
                    .load(gstUrl)
                    .apply(new RequestOptions()
                            .error(R.drawable.ic_camera_alt_black_72dp)
                            .placeholder(R.drawable.ic_camera_alt_black_72dp)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgBCAPoliceVerification);
        }

        //Enable/disable views
        GUIUtils.setViewAndChildrenEnabled(layoutSupportingParent, IsEditable);
    }

    @Override
    public void onPause() {
        super.onPause();
        permissionHandler.dismissSnackbar();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getAllSpinnerData != null && !getAllSpinnerData.isCancelled()) {
            getAllSpinnerData.cancel(true);
        }
    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();

        if (Id == R.id.imgAadhaarScanCopy) {
            FROM = AADHAAR_CARD_SCAN_COPY;
            SEL_FILE_TYPE = " images";
            showAttachmentDialog(view, SEL_FILE_TYPE);

        } else if (Id == R.id.imgPANScanCopy) {
            FROM = PAN_CARD_SCAN_COPY;
            SEL_FILE_TYPE = " images";
            showAttachmentDialog(view, SEL_FILE_TYPE);

        } else if (Id == R.id.imgSettlementAccountCopy) {
            FROM = SETTLEMENT_ACCOUNT_COPY;
            SEL_FILE_TYPE = " images";
            showAttachmentDialog(view, SEL_FILE_TYPE);

        } else if (Id == R.id.imgOutletAddressProof) {
            FROM = OUTLET_ADDRESS_COPY;
            SEL_FILE_TYPE = " images";
            showAttachmentDialog(view, SEL_FILE_TYPE);

        } else if (Id == R.id.imgIIBFCertificationCopy) {
            FROM = IIBF_CERTIFICATION_COPY;
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

            case AADHAAR_CARD_SCAN_COPY:
                if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgAadhaarScanCopy);
                else {
                    try {
                        bitmap = ImageUtils.rotateImageIfRequired(getActivity().getContentResolver(), bitmap, picUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Glide.with(context).asBitmap().load(bitmap).into(imgAadhaarScanCopy);
                }

                bcaSupportingInformationDto.setAadhaarCardScanBase64(base64);
                bcaSupportingInformationDto.setAadhaarCardScanExt(ext);
                break;

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

                bcaSupportingInformationDto.setPanCardScanBase64(base64);
                bcaSupportingInformationDto.setPanCardScanExt(ext);
                break;

            case SETTLEMENT_ACCOUNT_COPY:
                if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgSettlementAccountCopy);
                else {
                    try {
                        bitmap = ImageUtils.rotateImageIfRequired(getActivity().getContentResolver(), bitmap, picUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Glide.with(context).asBitmap().load(bitmap).into(imgSettlementAccountCopy);
                }

                bcaSupportingInformationDto.setSettlemtAccountCopyBase64(base64);
                bcaSupportingInformationDto.setSettlemtAccountCopyExt(ext);
                break;

            case OUTLET_ADDRESS_COPY:
                if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgOutletAddressProof);
                else {
                    try {
                        bitmap = ImageUtils.rotateImageIfRequired(getActivity().getContentResolver(), bitmap, picUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Glide.with(context).asBitmap().load(bitmap).into(imgOutletAddressProof);
                }

                bcaSupportingInformationDto.setOutletAddProofBase64(base64);
                bcaSupportingInformationDto.setOutletAddProofExt(ext);
                break;

            case IIBF_CERTIFICATION_COPY:
                if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgIIBFCertificationCopy);
                else {
                    try {
                        bitmap = ImageUtils.rotateImageIfRequired(getActivity().getContentResolver(), bitmap, picUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Glide.with(context).asBitmap().load(bitmap).into(imgIIBFCertificationCopy);
                }

                bcaSupportingInformationDto.setIibfCertificationCopyBase64(base64);
                bcaSupportingInformationDto.setIibfCertificationCopyExt(ext);
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

                bcaSupportingInformationDto.setBcaPoliceVerificationBase64(base64);
                bcaSupportingInformationDto.setBcaPoliceVerificationExt(ext);
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

    public void updateSupportingKYCDetails(boolean IsChecked, KYCDetailsDto kycDetailsDto) {

        if (IsChecked) {

            bcaSupportingInformationDto.setEmailId(kycDetailsDto.getEmailId());
            bcaSupportingInformationDto.setNameAsPanCard(kycDetailsDto.getNameAsPanCard());
            bcaSupportingInformationDto.setAadhaarNumber(kycDetailsDto.getAadhaarNumber());
            bcaSupportingInformationDto.setPanNumber(kycDetailsDto.getPanNumber());
            bcaSupportingInformationDto.setAadhaarCardScanId(kycDetailsDto.getAadhaarCardScanId());
            bcaSupportingInformationDto.setPanCardScanId(kycDetailsDto.getPanCardScanId());

        } else {
            bcaSupportingInformationDto.setEmailId(null);
            bcaSupportingInformationDto.setNameAsPanCard(null);
            bcaSupportingInformationDto.setAadhaarNumber(null);
            bcaSupportingInformationDto.setPanNumber(null);
            bcaSupportingInformationDto.setAadhaarCardScanId(null);
            bcaSupportingInformationDto.setPanCardScanId(null);

        }

        //STEP 1: Email ID
        editTextEmailID.setText(bcaSupportingInformationDto.getEmailId());

        //STEP 2: Name as per PAN Card
        editTextNameAsPerPANCard.setText(bcaSupportingInformationDto.getNameAsPanCard());

        //STEP 3: Aadhaar Number
        editTextAadhaarNumber1.setText(bcaSupportingInformationDto.getAadhaarNumber());
        editTextAadhaarNumber2.setText(bcaSupportingInformationDto.getAadhaarNumber());

        //STEP 4: PAN Card Number
        editTextPANNumber1.setText(bcaSupportingInformationDto.getPanNumber());
        editTextPANNumber2.setText(bcaSupportingInformationDto.getPanNumber());

        //STEP 5:  Aadhaar Card Scan Copy
        if (!TextUtils.isEmpty(bcaSupportingInformationDto.getAadhaarCardScanId())) {
            String gstUrl = Constants.DownloadImageUrl + bcaSupportingInformationDto.getAadhaarCardScanId();
            Glide.with(context)
                    .load(gstUrl)
                    .apply(new RequestOptions()
                            .error(R.drawable.ic_camera_alt_black_72dp)
                            .placeholder(R.drawable.ic_camera_alt_black_72dp)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgAadhaarScanCopy);
        }

        //STEP 6:  Pan Card Scan Copy
        if (!TextUtils.isEmpty(bcaSupportingInformationDto.getPanCardScanId())) {
            String gstUrl = Constants.DownloadImageUrl + bcaSupportingInformationDto.getPanCardScanId();
            Glide.with(context)
                    .load(gstUrl)
                    .apply(new RequestOptions()
                            .error(R.drawable.ic_camera_alt_black_72dp)
                            .placeholder(R.drawable.ic_camera_alt_black_72dp)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgPanScanCopy);
        }

        //Disable/Enable Views
        kycDisableEnableSupportingView(IsChecked);

    }

    private void kycDisableEnableSupportingView(boolean IsDisable) {

        Drawable drawable = IsDisable ? deprecateHandler.getDrawable(R.drawable.disable_edittext_bg) : deprecateHandler.getDrawable(R.drawable.edittext_bottom_bg);
        boolean IsEnabled = IsDisable ? false : true;

        layoutBgEmailId.setBackground(drawable);
        editTextNameAsPerPANCard.setBackground(drawable);
        editTextAadhaarNumber1.setBackground(drawable);
        editTextAadhaarNumber2.setBackground(drawable);
        editTextPANNumber1.setBackground(drawable);
        editTextPANNumber2.setBackground(drawable);

        editTextEmailID.setEnabled(IsEnabled);
        editTextNameAsPerPANCard.setEnabled(IsEnabled);
        editTextAadhaarNumber1.setEnabled(IsEnabled);
        editTextAadhaarNumber2.setEnabled(IsEnabled);
        editTextPANNumber1.setEnabled(IsEnabled);
        editTextPANNumber2.setEnabled(IsEnabled);
        imgAadhaarScanCopy.setEnabled(IsEnabled);
        imgPanScanCopy.setEnabled(IsEnabled);

    }

}
