package in.vakrangee.franchisee.sitelayout.update_kendra_address;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.nononsenseapps.filepicker.Utils;
import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import in.vakrangee.franchisee.sitelayout.R;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.commongui.CustomFranchiseeApplicationSpinnerAdapter;
import in.vakrangee.supercore.franchisee.commongui.FileAttachmentDialog;
import in.vakrangee.supercore.franchisee.commongui.FileUtils;
import in.vakrangee.supercore.franchisee.commongui.FilteredFilePickerActivity;
import in.vakrangee.supercore.franchisee.commongui.animation.AnimationHanndler;
import in.vakrangee.supercore.franchisee.commongui.imagepreview.CustomImageZoomDialogDM;
import in.vakrangee.supercore.franchisee.gstdetails.GSTINDTO;
import in.vakrangee.supercore.franchisee.nextgenfranchiseeapplication.CustomFranchiseeApplicationSpinnerDto;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.GUIUtils;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;
import in.vakrangee.supercore.franchisee.utils.ImageZipper;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;
import in.vakrangee.supercore.franchisee.utils.CustomSearchableSpinner;

@SuppressLint("ValidFragment")
public class UpdateKendraAddressFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private PermissionHandler permissionHandler;
    private DeprecateHandler deprecateHandler;

    private static final int CAMERA_PIC_REQUEST = 100;
    private static final int BROWSE_FOLDER_REQUEST = 101;
    private static final int SCANLIB_REQUEST_CODE = 99;
    private Uri picUri;                 //Picture URI
    private String SEL_FILE_TYPE;
    private int FROM = -1;
    private static final int ADDRESS_PROOF_IMAGE_PHOTO = 1;
    private static final int OWNERSHIP_PROOF_IMAGE_PHOTO = 2;
    private static final int NOC_PROOF_IMAGE_PHOTO = 3;
    private FileAttachmentDialog fileAttachementDialog;
    private CustomImageZoomDialogDM customImagePreviewDialog;

    private UpdateAddressDetailsDto updateAddressDetailsDto;

    private List<CustomFranchiseeApplicationSpinnerDto> stateByList;
    private List<CustomFranchiseeApplicationSpinnerDto> districtByList;
    private List<CustomFranchiseeApplicationSpinnerDto> VTCByList;
    private List<CustomFranchiseeApplicationSpinnerDto> municipalCorporationList;
    private List<CustomFranchiseeApplicationSpinnerDto> wardNoList;
    private CustomSearchableSpinner spinnerState, spinnerPermanentDistrict, spinnerPermanentVTC;
    private GetAllProposedKendraSpinnerData getAllProposedKendraSpinnerData = null;
    private GetDistrictVTCSpinnerData getDistrictVTCSpinnerData = null;
    private GetMunicipalCorporationWardNoSpinnerData getMunicipalCorporationWardNoSpinnerData = null;
    private boolean IsEditable = false;
    private CheckIsRuralForWardNo checkIsRuralForWardNo = null;

    private ImageView imgKendraLocAddressProof;
    private EditText editTextPermanentAddressLine1;
    private EditText editTextPermanentAddressLine2;
    private EditText editTextPermanentLandmark;
    private EditText editTextPermanentPincode;
    private TextView txtKendraLocAddressProofName;

    private TextView txtKendraLocAddressProofChooseFile, txtPermanentAddressLine1Lbl;
    private TextView txtPermanentLandmarkLbl;
    private TextView txtStateLbl;
    private TextView txtPermanentDistrictLbl;
    private TextView txtPermanentVTCLbl;
    private TextView txtWardNoLbl;
    private TextView txtPermanentPincodeLbl;
    private LinearLayout layoutWardNo;
    private TextView txtMunicipalCorporationLbl;
    private LinearLayout layoutMunicipalCorporation;
    private CustomSearchableSpinner spinnerMunicipalCorporation, spinnerWardNo;
    private String IsRural;
    private static final String VTC = "VTC";
    private static final String DISTRICT = "DISTRICT";
    private static final String PLEASE_SELECT = "Please Select";
    private static final String WARD_NO = "WARD_NO";
    private static final String STATE = "STATE";
    private static final String SPECIAL_CHARS = "~#^|$%&*!";
    private static final String IMAGES_PDF = "images/pdf";
    private static final String MUNICIPAL_CORPORATION = "MUNICIPAL_CORPORATION";
    private Context context;
    private LinearLayout layoutAddressDetail;
    private UpdateAddressRepository updateAddressRepo;
    private MaterialButton btnCancel, btnSubmit;
    private AsyncSaveAddressDetails asyncSaveAddressDetails = null;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alert;
    private View rootView;
    private LinearLayout layoutFooter;

    private TextView txtPremiseOwnerShipTypeLbl;
    private Spinner spinnerPremiseOwnerShipType;
    private LinearLayout layoutOwnedBy;
    private TextView txtOwnedByLbl;
    private Spinner spinnerOwnedBy;
    private TextView txtRentAgreementChooseFile;
    private TextView txtLandlordSocietyNOCChooseFile;
    private ImageView imgRentAgreement;
    private ImageView imgLandlordSocietyNOC;
    private TextView txtRentAgreementName;
    private TextView txtLandlordSocietyNOCName;
    private static final String RENT_AGREEMENT_LBL = "5 Year Registered Rent Agreement";
    private static final String OWNED_AGREEMENT_LBL = "5 Year Registered Rent Agreement /Registered Power Of Attorney";
    private static final String OWNERSHIP_PROOF_LBL = "Ownership Proof";

    private List<CustomFranchiseeApplicationSpinnerDto> premiseOwnershipTypelList;
    private List<CustomFranchiseeApplicationSpinnerDto> ownedByList;
    private LinearLayout layoutAddressStateDetail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_update_address_details, container, false);

        bindViewId(rootView);

        //Data
        this.context = getContext();
        deprecateHandler = new DeprecateHandler(context);
        updateAddressRepo = new UpdateAddressRepository(context);
        permissionHandler = new PermissionHandler(getActivity());

        //Input filter
        CommonUtils.InputFiletrWithMaxLength(editTextPermanentAddressLine1, SPECIAL_CHARS, 35);
        CommonUtils.InputFiletrWithMaxLength(editTextPermanentAddressLine2, SPECIAL_CHARS, 35);
        CommonUtils.InputFiletrWithMaxLength(editTextPermanentLandmark, SPECIAL_CHARS, 35);

        //Set Compulsory mark
        TextView[] txtViewsForCompulsoryMark = {txtPremiseOwnerShipTypeLbl, txtOwnedByLbl,
                txtPermanentAddressLine1Lbl, txtPermanentLandmarkLbl, txtStateLbl, txtPermanentDistrictLbl,
                txtPermanentVTCLbl, txtPermanentPincodeLbl, txtMunicipalCorporationLbl, txtWardNoLbl, txtKendraLocAddressProofChooseFile};
        GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);

        SEL_FILE_TYPE = "images/pdf";

        //Pincode
        editTextPermanentPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Do Nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int len = editTextPermanentPincode.getText().toString().trim().length();
                if (len <= 0)
                    return;

                if (i != 6) {
                    editTextPermanentPincode.setTextColor(Color.parseColor("#000000"));
                    editTextPermanentPincode.setError(getResources().getString(R.string.EnterPincode));

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editTextPermanentPincode.getText().toString().contains(" ")) {
                    editTextPermanentPincode.setText(editTextPermanentPincode.getText().toString().replaceAll(" ", ""));
                    editTextPermanentPincode.setSelection(editTextPermanentPincode.getText().length());

                }

                if (editTextPermanentPincode.length() >= 6) {
                    editTextPermanentPincode.setTextColor(Color.parseColor("#468847"));
                    editTextPermanentPincode.setError(null);

                    //Get State
                    //performChangesPincode(editTextPermanentPincode.getText().toString());
                }
            }
        });

        return rootView;
    }

    private void bindViewId(View view) {

        layoutAddressStateDetail = view.findViewById(R.id.layoutAddressStateDetail);
        layoutFooter = view.findViewById(R.id.layoutFooter);
        txtKendraLocAddressProofChooseFile = view.findViewById(R.id.txtKendraLocAddressProofChooseFile);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        layoutAddressDetail = view.findViewById(R.id.layoutAddressDetail);
        editTextPermanentAddressLine1 = view.findViewById(R.id.editTextPermanentAddressLine1);
        editTextPermanentAddressLine2 = view.findViewById(R.id.editTextPermanentAddressLine2);
        editTextPermanentLandmark = view.findViewById(R.id.editTextPermanentLandmark);
        editTextPermanentPincode = view.findViewById(R.id.editTextPermanentPincode);
        spinnerState = view.findViewById(R.id.spinnerState);
        spinnerPermanentDistrict = view.findViewById(R.id.spinnerPermanentDistrict);
        spinnerPermanentVTC = view.findViewById(R.id.spinnerPermanentVTC);
        spinnerMunicipalCorporation = view.findViewById(R.id.spinnerMunicipalCorporation);
        layoutMunicipalCorporation = view.findViewById(R.id.layoutMunicipalCorporation);
        spinnerWardNo = view.findViewById(R.id.spinnerWardNo);
        layoutWardNo = view.findViewById(R.id.layoutWardNo);
        imgKendraLocAddressProof = view.findViewById(R.id.imgKendraLocAddressProof);
        txtKendraLocAddressProofName = view.findViewById(R.id.txtKendraLocAddressProofName);

        txtPermanentAddressLine1Lbl = view.findViewById(R.id.txtPermanentAddressLine1Lbl);
        txtPermanentLandmarkLbl = view.findViewById(R.id.txtPermanentLandmarkLbl);
        txtStateLbl = view.findViewById(R.id.txtStateLbl);
        txtPermanentDistrictLbl = view.findViewById(R.id.txtPermanentDistrictLbl);
        txtPermanentVTCLbl = view.findViewById(R.id.txtPermanentVTCLbl);
        txtMunicipalCorporationLbl = view.findViewById(R.id.txtMunicipalCorporationLbl);
        txtWardNoLbl = view.findViewById(R.id.txtWardNoLbl);
        txtPermanentPincodeLbl = view.findViewById(R.id.txtPermanentPincodeLbl);

        imgKendraLocAddressProof.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        txtPremiseOwnerShipTypeLbl = view.findViewById(R.id.txtPremiseOwnerShipTypeLbl);
        spinnerPremiseOwnerShipType = view.findViewById(R.id.spinnerPremiseOwnerShipType);
        layoutOwnedBy = view.findViewById(R.id.layoutOwnedBy);
        txtOwnedByLbl = view.findViewById(R.id.txtOwnedByLbl);
        spinnerOwnedBy = view.findViewById(R.id.spinnerOwnedBy);
        txtRentAgreementChooseFile = view.findViewById(R.id.txtRentAgreementChooseFile);
        txtLandlordSocietyNOCChooseFile = view.findViewById(R.id.txtLandlordSocietyNOCChooseFile);
        imgRentAgreement = view.findViewById(R.id.imgRentAgreement);
        imgLandlordSocietyNOC = view.findViewById(R.id.imgLandlordSocietyNOC);
        txtRentAgreementName = view.findViewById(R.id.txtRentAgreementName);
        txtLandlordSocietyNOCName = view.findViewById(R.id.txtLandlordSocietyNOCName);

        imgRentAgreement.setOnClickListener(this);
        imgLandlordSocietyNOC.setOnClickListener(this);

    }

    private void bindSpinner() {

        spinner_focusablemode(spinnerState);
        spinner_focusablemode(spinnerPermanentDistrict);
        spinner_focusablemode(spinnerPermanentVTC);
        setDistrictSpinnerAdapter(stateByList, spinnerState, updateAddressDetailsDto.getState());

        //premise Ownership Type
        CustomFranchiseeApplicationSpinnerAdapter spremiseOwnershipapdater = new CustomFranchiseeApplicationSpinnerAdapter(context, premiseOwnershipTypelList);
        spinnerPremiseOwnerShipType.setAdapter(spremiseOwnershipapdater);
        int appspremiseOwnershipPos = updateAddressRepo.getSelectedPos(premiseOwnershipTypelList, updateAddressDetailsDto.getPremiseOwnerShipType());
        spinnerPremiseOwnerShipType.setSelection(appspremiseOwnershipPos);
        spinnerPremiseOwnerShipType.setOnItemSelectedListener(this);

        //Owned By
        CustomFranchiseeApplicationSpinnerAdapter spremiseOwnedByapdater = new CustomFranchiseeApplicationSpinnerAdapter(context, ownedByList);
        spinnerOwnedBy.setAdapter(spremiseOwnedByapdater);
        int appsOwnedByPos = updateAddressRepo.getSelectedPos(ownedByList, updateAddressDetailsDto.getOwnedBy());
        spinnerOwnedBy.setSelection(appsOwnedByPos);
        spinnerOwnedBy.setOnItemSelectedListener(this);

        //Ownership Proof Image
        boolean IsOwnerPDF = ((updateAddressDetailsDto.getOwnershipProofFileExt() != null) && updateAddressDetailsDto.getOwnershipProofFileExt().equalsIgnoreCase("pdf")) ? true : false;
        if (IsOwnerPDF) {
            Glide.with(context).asDrawable().load(R.drawable.pdf).
                    apply(new RequestOptions()
                            .error(R.drawable.ic_camera_alt_black_72dp)
                            .placeholder(R.drawable.ic_camera_alt_black_72dp)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)).into(imgRentAgreement);
        } else {
            if (!TextUtils.isEmpty(updateAddressDetailsDto.getOwnerProofFileId())) {
                String ownershipUrl = Constants.DownloadImageUrl + updateAddressDetailsDto.getOwnerProofFileId();
                Glide.with(context)
                        .load(ownershipUrl)
                        .apply(new RequestOptions()
                                .error(R.drawable.ic_camera_alt_black_72dp)
                                .placeholder(R.drawable.ic_camera_alt_black_72dp)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))
                        .into(imgRentAgreement);
            }
        }

        //NOC Image
        boolean IsNOCPDF = ((updateAddressDetailsDto.getNOCProofFileExt() != null) && updateAddressDetailsDto.getNOCProofFileExt().equalsIgnoreCase("pdf")) ? true : false;
        if (IsNOCPDF) {
            Glide.with(context).asDrawable().load(R.drawable.pdf).apply(new RequestOptions()
                    .error(R.drawable.ic_camera_alt_black_72dp)
                    .placeholder(R.drawable.ic_camera_alt_black_72dp)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)).into(imgLandlordSocietyNOC);
        } else {
            if (!TextUtils.isEmpty(updateAddressDetailsDto.getNOCProofFileId())) {
                String nocUrl = Constants.DownloadImageUrl + updateAddressDetailsDto.getNOCProofFileId();
                Glide.with(context)
                        .load(nocUrl)
                        .apply(new RequestOptions()
                                .error(R.drawable.ic_camera_alt_black_72dp)
                                .placeholder(R.drawable.ic_camera_alt_black_72dp)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))
                        .into(imgLandlordSocietyNOC);
            }
        }

        //Address Line 1
        editTextPermanentAddressLine1.setText(updateAddressDetailsDto.getAddressLine1());

        //Address Line 2
        editTextPermanentAddressLine2.setText(updateAddressDetailsDto.getAddressLine2());

        //LandMark
        editTextPermanentLandmark.setText(updateAddressDetailsDto.getLandMark());

        //Pincode
        editTextPermanentPincode.setText(updateAddressDetailsDto.getPinCode());

        //Kendra Location Image
        boolean IsLOCPDF = ((updateAddressDetailsDto.getOutletLocationFileExt() != null) && updateAddressDetailsDto.getOutletLocationFileExt().equalsIgnoreCase("pdf")) ? true : false;
        if (IsLOCPDF) {
            Glide.with(context).asDrawable().load(R.drawable.pdf).into(imgKendraLocAddressProof);
        } else {
            if (!TextUtils.isEmpty(updateAddressDetailsDto.getOutLetLocationFileId())) {
                String locUrl = Constants.DownloadImageUrl + updateAddressDetailsDto.getOutLetLocationFileId();
                Glide.with(context)
                        .load(locUrl)
                        .apply(new RequestOptions()
                                .error(R.drawable.ic_camera_alt_black_72dp)
                                .placeholder(R.drawable.ic_camera_alt_black_72dp)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))
                        .into(imgKendraLocAddressProof);
            }
        }

        //Is State Editable
        boolean isAddressEditable = (!TextUtils.isEmpty(updateAddressDetailsDto.getIs_address_edit_allow()) && updateAddressDetailsDto.getIs_address_edit_allow().equalsIgnoreCase("1")) ? true : false;
        if(!isAddressEditable){
            if(!TextUtils.isEmpty(updateAddressDetailsDto.getAddress_edit_allow_msg())) {
                showMessage(updateAddressDetailsDto.getAddress_edit_allow_msg());
            }
        }

        //Enable/disable views
        GUIUtils.setViewAndChildrenEnabled(layoutAddressDetail, IsEditable);
        if (IsEditable) {
            layoutFooter.setVisibility(View.VISIBLE);
        } else {
            layoutFooter.setVisibility(View.GONE);
        }

        GUIUtils.setViewAndChildrenEnabled(layoutAddressStateDetail, isAddressEditable);

    }

    @Override
    public void onPause() {
        super.onPause();
        permissionHandler.dismissSnackbar();
    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();

        if (Id == R.id.imgKendraLocAddressProof) {
            FROM = ADDRESS_PROOF_IMAGE_PHOTO;
            showImgOrPDF(view);

        } else if (Id == R.id.imgRentAgreement) {
            FROM = OWNERSHIP_PROOF_IMAGE_PHOTO;
            showImgOrPDF(view);

        } else if (Id == R.id.imgLandlordSocietyNOC) {
            FROM = NOC_PROOF_IMAGE_PHOTO;
            showImgOrPDF(view);

        } else if (Id == R.id.btnCancel) {
            AnimationHanndler.bubbleAnimation(context, view);

            getActivity().finish();

        } else if (Id == R.id.btnSubmit) {
            AnimationHanndler.bubbleAnimation(context, view);

            saveAddressDetails();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getAllProposedKendraSpinnerData != null && !getAllProposedKendraSpinnerData.isCancelled()) {
            getAllProposedKendraSpinnerData.cancel(true);
        }

        if (getDistrictVTCSpinnerData != null && !getDistrictVTCSpinnerData.isCancelled()) {
            getDistrictVTCSpinnerData.cancel(true);
        }

        if (checkIsRuralForWardNo != null && !checkIsRuralForWardNo.isCancelled()) {
            checkIsRuralForWardNo.cancel(true);
        }
    }

    public int IsAddressDetailsValidated() {

        //Premise Ownership Type
        if (TextUtils.isEmpty(updateAddressDetailsDto.getPremiseOwnerShipType()) || updateAddressDetailsDto.getPremiseOwnerShipType().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Premise Ownership Type.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerPremiseOwnerShipType, "Please select Premise Ownership Type.", context);
            return 3;
        }

        //Owned By
        String ownerShipType = spinnerPremiseOwnerShipType.getSelectedItem().toString();
        if (!TextUtils.isEmpty(ownerShipType) && ownerShipType.equalsIgnoreCase("Owned")) {
            if (TextUtils.isEmpty(updateAddressDetailsDto.getOwnedBy()) || updateAddressDetailsDto.getOwnedBy().equalsIgnoreCase("0")) {
                Toast.makeText(context, "Please select Owned By.", Toast.LENGTH_LONG).show();
                GUIUtils.setErrorToSpinner(spinnerOwnedBy, "Please select Owned By.", context);
                return 3;
            }
        }

        //Ownership Proof
        if (updateAddressDetailsDto.getPremiseOwnerShipType().equals("1")) {
            if (!TextUtils.isEmpty(updateAddressDetailsDto.getOwnedBy()) && updateAddressDetailsDto.getOwnedBy().equalsIgnoreCase("2")) {
                if (TextUtils.isEmpty(updateAddressDetailsDto.getOwnerProofFileId()) || updateAddressDetailsDto.getOwnerProofFileId().equalsIgnoreCase("0")) {
                    if (TextUtils.isEmpty(updateAddressDetailsDto.getRentAgreementBase64())) {
                        String type = "";
                        if (!TextUtils.isEmpty(ownerShipType) && ownerShipType.equalsIgnoreCase("Owned")) {
                            type = "Ownership Proof";
                        } else {
                            type = RENT_AGREEMENT_LBL;
                        }

                        String msg = "Please add " + type + " photo.";
                        showMessage(msg);
                        return 9;
                    }
                }
            }
        }

        //Address Line 1
        updateAddressDetailsDto.setAddressLine1(editTextPermanentAddressLine1.getText().toString().trim());
        if (TextUtils.isEmpty(updateAddressDetailsDto.getAddressLine1())) {
            Toast.makeText(context, "Please enter Address Line 1.", Toast.LENGTH_LONG).show();
            editTextPermanentAddressLine1.setError("Please enter Address Line 1.");
            return 1;
        }

        if (updateAddressDetailsDto.getAddressLine1().trim().length() < 3) {
            Toast.makeText(context, "Please enter minimum 3 characters in Address Line 1.", Toast.LENGTH_LONG).show();
            editTextPermanentAddressLine1.setError("Please enter minimum 3 characters in Address Line 1.");
            return 1;
        }

        //Address Line 2
        updateAddressDetailsDto.setAddressLine2(editTextPermanentAddressLine2.getText().toString().trim());
        if (!TextUtils.isEmpty(updateAddressDetailsDto.getAddressLine2())) {
            if (updateAddressDetailsDto.getAddressLine2().trim().length() < 3) {
                Toast.makeText(context, "Please enter minimum 3 characters in Address Line 2.", Toast.LENGTH_LONG).show();
                editTextPermanentAddressLine2.setError("Please enter minimum 3 characters in Address Line 2.");
                return 1;
            }
        }

        //LandMark
        updateAddressDetailsDto.setLandMark(editTextPermanentLandmark.getText().toString().trim());
        if (TextUtils.isEmpty(updateAddressDetailsDto.getLandMark())) {
            Toast.makeText(context, "Please enter LandMark.", Toast.LENGTH_LONG).show();
            editTextPermanentLandmark.setError("Please enter LandMark.");
            return 1;
        }

        if (updateAddressDetailsDto.getLandMark().trim().length() < 3) {
            Toast.makeText(context, "Please enter minimum 3 characters in LandMark.", Toast.LENGTH_LONG).show();
            editTextPermanentLandmark.setError("Please enter minimum 3 characters in LandMark.");
            return 1;
        }

        //Pin Code
        updateAddressDetailsDto.setPinCode(editTextPermanentPincode.getText().toString().trim());
        if (TextUtils.isEmpty(updateAddressDetailsDto.getPinCode())) {
            Toast.makeText(context, "Please enter Pin Code.", Toast.LENGTH_LONG).show();
            editTextPermanentPincode.setError("Please enter Pin Code.");
            return 1;
        }

        //State
        if (TextUtils.isEmpty(updateAddressDetailsDto.getState()) || updateAddressDetailsDto.getState().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select State.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerState, "Please select State.", context);
            return 1;
        }

        //District
        if (TextUtils.isEmpty(updateAddressDetailsDto.getDistrict()) || updateAddressDetailsDto.getDistrict().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select District.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerPermanentDistrict, "Please select District.", context);
            return 1;
        }

        //VTC
        if (TextUtils.isEmpty(updateAddressDetailsDto.getVTC()) || updateAddressDetailsDto.getVTC().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select VTC.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerPermanentVTC, "Please select VTC.", context);
            return 1;
        }

        //Municipal Corporation / Ward No
        if (!TextUtils.isEmpty(IsRural) && IsRural.equalsIgnoreCase("0")) {

            //Municipal Corporation
            if (TextUtils.isEmpty(updateAddressDetailsDto.getMunicipalCorporationId()) || updateAddressDetailsDto.getMunicipalCorporationId().equalsIgnoreCase("0")) {
                Toast.makeText(context, "Please select Municipal Corporation.", Toast.LENGTH_LONG).show();
                GUIUtils.setErrorToSpinner(spinnerMunicipalCorporation, "Please select Municipal Corporation.", context);
                return 1;
            }

            //Ward No
            if (TextUtils.isEmpty(updateAddressDetailsDto.getWardNo()) || updateAddressDetailsDto.getWardNo().equalsIgnoreCase("0")) {
                Toast.makeText(context, "Please select Ward No.", Toast.LENGTH_LONG).show();
                GUIUtils.setErrorToSpinner(spinnerWardNo, "Please select Ward No.", context);
                return 1;
            }
        }

        //Address Proof Type Scan Copy
        if (TextUtils.isEmpty(updateAddressDetailsDto.getOutLetLocationFileId()) || updateAddressDetailsDto.getOutLetLocationFileId().equalsIgnoreCase("0")) {
            if (TextUtils.isEmpty(updateAddressDetailsDto.getKendraLocAddressProofBase64())) {
                showMessage("Please add Kendra Location Address Proof Scan Copy.");
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
                setImageData(picUri, base64Data, bitmapNew);

            }
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

                    if (ext.equalsIgnoreCase("pdf")) {
                        base64Data = CommonUtils.encodeFileToBase64Binary(file);
                        loadImage(base64Data, ext);
                    } else {
                        file = new ImageZipper(context).setQuality(50).compressToFile(file);
                        Bitmap bitmapNew = new ImageZipper(context).compressToBitmap(file);
                        bitmapNew = ImageUtils.getResizedBitmap(bitmapNew);
                        base64Data = CommonUtils.convertBitmapToString(bitmapNew);
                        setImageData(uri, base64Data, bitmapNew);
                    }
                }
            }
            if (requestCode == SCANLIB_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
                Bitmap bitmap = null;

                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                picUri = uri;

                File file = new File(uri.getPath());

                //getActivity().getContentResolver().delete(uri, null, null);
                String base64Data = CommonUtils.convertBitmapToString(bitmap);
                String fileName = URLDecoder.decode(file.getName(), "UTF-8");

                setImageData(picUri, base64Data, bitmap);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadImage(String base64Data, String ext) {

        switch (FROM) {
            case ADDRESS_PROOF_IMAGE_PHOTO:
                Glide.with(context).asDrawable().load(context.getResources().getDrawable(R.drawable.pdf)).into(imgKendraLocAddressProof);
                updateAddressDetailsDto.setKendraLocAddressProofBase64(base64Data);
                updateAddressDetailsDto.setOutletLocationFileExt(ext);
                break;

            case OWNERSHIP_PROOF_IMAGE_PHOTO:
                Glide.with(context).asDrawable().load(context.getResources().getDrawable(R.drawable.pdf)).into(imgRentAgreement);
                updateAddressDetailsDto.setRentAgreementBase64(base64Data);
                updateAddressDetailsDto.setOwnershipProofFileExt(ext);

                break;
            case NOC_PROOF_IMAGE_PHOTO:
                Glide.with(context).asDrawable().load(context.getResources().getDrawable(R.drawable.pdf)).into(imgLandlordSocietyNOC);
                updateAddressDetailsDto.setLandLordSocietyNOCBase64(base64Data);
                updateAddressDetailsDto.setNOCProofFileExt(ext);
                break;
            default:
                break;

        }
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
                permissionHandler.requestPermission(view, Manifest.permission.CAMERA, getString(R.string.needs_permission_camera_msg), new IPermission() {
                    @Override
                    public void IsPermissionGranted(boolean IsGranted) {
                        if (IsGranted) {
                            handleCameraClick();
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
        });
        fileAttachementDialog.setCancelable(false);
        fileAttachementDialog.show();
    }

    private void startScanCamera() {

        //If the app has not the permission then asking for the permission
        permissionHandler.requestPermission(rootView, Manifest.permission.CAMERA, getString(R.string.needs_permission_camera_msg), new IPermission() {
            @Override
            public void IsPermissionGranted(boolean IsGranted) {
                if (IsGranted) {
                    int preference = ScanConstants.OPEN_CAMERA;
                    Intent intent1 = new Intent(getActivity(), ScanActivity.class);
                    intent1.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
                    startActivityForResult(intent1, SCANLIB_REQUEST_CODE);
                }
            }
        });
    }

    private void refreshAddressDetailsImg(GSTINDTO imageDto) {

        switch (FROM) {
            case ADDRESS_PROOF_IMAGE_PHOTO:
                updateAddressDetailsDto.setChangedPhoto_LOC(imageDto.isChangedPhoto());
                updateAddressDetailsDto.setOutLetLocationFileId(imageDto.getGstImageId());
                updateAddressDetailsDto.setBitmap_LOC(imageDto.getBitmap());
                updateAddressDetailsDto.setOutletLocationFileExt("jpg");
                updateAddressDetailsDto.setKendraLocAddressProofBase64(imageDto.getGstImage());
                break;

            case OWNERSHIP_PROOF_IMAGE_PHOTO:
                updateAddressDetailsDto.setChangedPhoto_RENT(imageDto.isChangedPhoto());
                updateAddressDetailsDto.setOwnerProofFileId(imageDto.getGstImageId());
                updateAddressDetailsDto.setBitmap_RENT(imageDto.getBitmap());
                updateAddressDetailsDto.setOwnershipProofFileExt("jpg");
                updateAddressDetailsDto.setRentAgreementBase64(imageDto.getGstImage());

                break;
            case NOC_PROOF_IMAGE_PHOTO:
                updateAddressDetailsDto.setChangedPhoto_NOC(imageDto.isChangedPhoto());
                updateAddressDetailsDto.setNOCProofFileId(imageDto.getGstImageId());
                updateAddressDetailsDto.setBitmap_NOC(imageDto.getBitmap());
                updateAddressDetailsDto.setNOCProofFileExt("jpg");
                updateAddressDetailsDto.setLandLordSocietyNOCBase64(imageDto.getGstImage());
                break;
            default:
                break;

        }
    }

    private GSTINDTO prepareDtoForPreview(int FROM) {
        GSTINDTO imageDto = new GSTINDTO();
        switch (FROM) {

            case ADDRESS_PROOF_IMAGE_PHOTO:
                imageDto.setChangedPhoto(updateAddressDetailsDto.isChangedPhoto_LOC());
                imageDto.setGstImageId(updateAddressDetailsDto.getOutLetLocationFileId());
                imageDto.setBitmap(updateAddressDetailsDto.getBitmap_LOC());
                imageDto.setName("");
                imageDto.setGstImage(updateAddressDetailsDto.getKendraLocAddressProofBase64());
                break;

            case OWNERSHIP_PROOF_IMAGE_PHOTO:
                imageDto.setChangedPhoto(updateAddressDetailsDto.isChangedPhoto_RENT());
                imageDto.setGstImageId(updateAddressDetailsDto.getOwnerProofFileId());
                imageDto.setBitmap(updateAddressDetailsDto.getBitmap_RENT());
                imageDto.setName("");
                imageDto.setGstImage(updateAddressDetailsDto.getRentAgreementBase64());
                break;

            case NOC_PROOF_IMAGE_PHOTO:
                imageDto.setChangedPhoto(updateAddressDetailsDto.isChangedPhoto_NOC());
                imageDto.setGstImageId(updateAddressDetailsDto.getNOCProofFileId());
                imageDto.setBitmap(updateAddressDetailsDto.getBitmap_NOC());
                imageDto.setName("");
                imageDto.setGstImage(updateAddressDetailsDto.getLandLordSocietyNOCBase64());
                break;

            default:
                break;
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
                    refreshProofImage(dto);
                }
            });
            customImagePreviewDialog.show();
            customImagePreviewDialog.setCancelable(false);

            //Title
            String title = "Proof Image Preview";
            customImagePreviewDialog.setDialogTitle(title);

            //Change Photo Allowed
            boolean IsEditable = (!TextUtils.isEmpty(updateAddressDetailsDto.getIsEditable()) &&
                    updateAddressDetailsDto.getIsEditable().equalsIgnoreCase("1")) ? true : false;
            customImagePreviewDialog.allowChangePhoto(IsEditable);
            customImagePreviewDialog.allowSaveOption(IsEditable);
            customImagePreviewDialog.allowRemarks(false);

        } else {
            Toast.makeText(context, "No photo available to preview.", Toast.LENGTH_SHORT).show();
        }
    }

    private void refreshProofImage(GSTINDTO dto) {
        switch (FROM) {
            case ADDRESS_PROOF_IMAGE_PHOTO:

                refreshAddressDetailsImg(dto);

                //Refresh Address Proof Image
                if (!TextUtils.isEmpty(updateAddressDetailsDto.getKendraLocAddressProofBase64())) {
                    Bitmap bitmap = CommonUtils.StringToBitMap(updateAddressDetailsDto.getKendraLocAddressProofBase64());
                    if (bitmap != null)
                        imgKendraLocAddressProof.setImageBitmap(bitmap);
                }
                break;

            case OWNERSHIP_PROOF_IMAGE_PHOTO:
                refreshAddressDetailsImg(dto);

                //Refresh Proof Image
                if (!TextUtils.isEmpty(updateAddressDetailsDto.getRentAgreementBase64())) {
                    Bitmap bitmap = CommonUtils.StringToBitMap(updateAddressDetailsDto.getRentAgreementBase64());
                    if (bitmap != null)
                        imgRentAgreement.setImageBitmap(bitmap);
                }

                break;
            case NOC_PROOF_IMAGE_PHOTO:

                refreshAddressDetailsImg(dto);

                //Refresh Proof Image
                if (!TextUtils.isEmpty(updateAddressDetailsDto.getLandLordSocietyNOCBase64())) {
                    Bitmap bitmap = CommonUtils.StringToBitMap(updateAddressDetailsDto.getLandLordSocietyNOCBase64());
                    if (bitmap != null)
                        imgLandlordSocietyNOC.setImageBitmap(bitmap);
                }
                break;
            default:
                break;

        }
    }

    private String getDialogTitle() {
        String title = "";

        switch (FROM) {
            case ADDRESS_PROOF_IMAGE_PHOTO:
                title = "Proof Image Preview";
                break;

            case OWNERSHIP_PROOF_IMAGE_PHOTO:
                title = "Address Proof Image";

                break;
            case NOC_PROOF_IMAGE_PHOTO:
                break;
            default:
                break;

        }
        return title;
    }

    private void showImgOrPDF(final View view) {

        boolean IsEditable = (!TextUtils.isEmpty(updateAddressDetailsDto.getIsEditable()) && updateAddressDetailsDto.getIsEditable().equalsIgnoreCase("1")) ? true : false;

        if (IsEditable) {
            showAttachmentDialog(view);

        }
    }

    private void handleCameraClick() {

        switch (FROM) {

            case ADDRESS_PROOF_IMAGE_PHOTO:

                String ext = TextUtils.isEmpty(updateAddressDetailsDto.getOutletLocationFileExt()) ? "jpg" : updateAddressDetailsDto.getOutletLocationFileExt();

                //FOR PDF
                if (ext.equalsIgnoreCase("pdf")) {
                    startScanCamera();
                    return;
                }

                //FOR Image
                if ((TextUtils.isEmpty(updateAddressDetailsDto.getOutLetLocationFileId()) || updateAddressDetailsDto.getOutLetLocationFileId().equalsIgnoreCase("0")) && TextUtils.isEmpty(updateAddressDetailsDto.getKendraLocAddressProofBase64())) {
                    startScanCamera();

                } else {
                    GSTINDTO previewDto = prepareDtoForPreview(FROM);
                    showImagePreviewDialog(previewDto);
                }
                break;

            case OWNERSHIP_PROOF_IMAGE_PHOTO:
                String o_ext = TextUtils.isEmpty(updateAddressDetailsDto.getOwnershipProofFileExt()) ? "jpg" : updateAddressDetailsDto.getOwnershipProofFileExt();

                //FOR PDF
                if (o_ext.equalsIgnoreCase("pdf")) {
                    startScanCamera();
                    return;
                }

                //FOR Image
                if ((TextUtils.isEmpty(updateAddressDetailsDto.getOwnerProofFileId()) || updateAddressDetailsDto.getOwnerProofFileId().equalsIgnoreCase("0")) && TextUtils.isEmpty(updateAddressDetailsDto.getRentAgreementBase64())) {
                    startScanCamera();

                } else {
                    GSTINDTO previewDto = prepareDtoForPreview(FROM);
                    showImagePreviewDialog(previewDto);
                }
                break;

            case NOC_PROOF_IMAGE_PHOTO:

                String n_ext = TextUtils.isEmpty(updateAddressDetailsDto.getNOCProofFileExt()) ? "jpg" : updateAddressDetailsDto.getNOCProofFileExt();

                //FOR PDF
                if (n_ext.equalsIgnoreCase("pdf")) {
                    startScanCamera();
                    return;
                }

                //FOR Image
                if ((TextUtils.isEmpty(updateAddressDetailsDto.getNOCProofFileId()) || updateAddressDetailsDto.getNOCProofFileId().equalsIgnoreCase("0")) && TextUtils.isEmpty(updateAddressDetailsDto.getLandLordSocietyNOCBase64())) {
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

    private void spinner_focusablemode(CustomSearchableSpinner stateSpinner) {
        //Do Nothing
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        int Id = adapterView.getId();

        if (Id == R.id.spinnerPremiseOwnerShipType) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerPremiseOwnerShipType.getItemAtPosition(position);
                updateAddressDetailsDto.setPremiseOwnerShipType(entityDto.getId());
                entityChangeBasedOnOwnerShipType(updateAddressDetailsDto.getPremiseOwnerShipType(), updateAddressDetailsDto.getOwnedBy());
            }
        } else if (Id == R.id.spinnerOwnedBy) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerOwnedBy.getItemAtPosition(position);
                updateAddressDetailsDto.setOwnedBy(entityDto.getId());
                entityChangeBasedOnOwnerShipType(updateAddressDetailsDto.getPremiseOwnerShipType(), updateAddressDetailsDto.getOwnedBy());
            }
        } else if (Id == R.id.spinnerState) {
            if (position > 0) {
                spinnerState.setTitle("Select State");
                spinnerState.requestFocus();
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerState.getItemAtPosition(position);
                if (!entityDto.getId().equals("0")) {
                    updateAddressDetailsDto.setState(entityDto.getId());

                    //Get District
                    updateAddressDetailsDto.setPinCode(editTextPermanentPincode.getText().toString());
                    getDistrictVTCSpinnerData = new GetDistrictVTCSpinnerData(updateAddressDetailsDto.getPinCode(), updateAddressDetailsDto.getState(), null);
                    getDistrictVTCSpinnerData.execute(DISTRICT);

                }
            } else {
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", PLEASE_SELECT));

                setDistrictSpinnerAdapter(list1, spinnerPermanentDistrict, null);
                updateAddressDetailsDto.setDistrict(null);
                setDistrictSpinnerAdapter(list1, spinnerPermanentVTC, null);
                updateAddressDetailsDto.setVTC(null);

            }
        } else if (Id == R.id.spinnerPermanentDistrict) {
            if (position > 0) {
                spinnerPermanentDistrict.setTitle("Select District");
                spinnerPermanentDistrict.requestFocus();
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerPermanentDistrict.getItemAtPosition(position);
                if (!entityDto.getId().equals("0")) {
                    updateAddressDetailsDto.setDistrict(entityDto.getId());

                    //Get VTC
                    updateAddressDetailsDto.setPinCode(editTextPermanentPincode.getText().toString());
                    getDistrictVTCSpinnerData = new GetDistrictVTCSpinnerData(updateAddressDetailsDto.getPinCode(), updateAddressDetailsDto.getState(), updateAddressDetailsDto.getDistrict());
                    getDistrictVTCSpinnerData.execute("VTC");

                }
            } else {
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", PLEASE_SELECT));

                setDistrictSpinnerAdapter(list1, spinnerPermanentVTC, null);
                updateAddressDetailsDto.setVTC(null);

            }
        } else if (Id == R.id.spinnerPermanentVTC) {
            spinnerPermanentVTC.setTitle("Select VTC");
            spinnerPermanentVTC.requestFocus();
            CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerPermanentVTC.getItemAtPosition(position);
            updateAddressDetailsDto.setVTC(entityDto.getId());
            checkIsRuralForWardNo = new CheckIsRuralForWardNo();
            checkIsRuralForWardNo.execute(entityDto.getId());

        } else if (Id == R.id.spinnerMunicipalCorporation) {
            if (position > 0) {
                spinnerMunicipalCorporation.setTitle("Select Municipal Corporation");
                spinnerMunicipalCorporation.requestFocus();
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerMunicipalCorporation.getItemAtPosition(position);
                if (!entityDto.getId().equals("0")) {
                    updateAddressDetailsDto.setMunicipalCorporationId(entityDto.getId());
                    updateAddressDetailsDto.setPinCode(editTextPermanentPincode.getText().toString());
                    getMunicipalCorporationWardNoSpinnerData = new GetMunicipalCorporationWardNoSpinnerData(updateAddressDetailsDto.getVTC(), updateAddressDetailsDto.getMunicipalCorporationId());
                    getMunicipalCorporationWardNoSpinnerData.execute(WARD_NO);

                }
            } else {
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", PLEASE_SELECT));

                setWardNoSpinnerAdapter(list1, spinnerWardNo, null);
                updateAddressDetailsDto.setWardNo(null);

            }
        } else if (Id == R.id.spinnerWardNo) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerWardNo.getItemAtPosition(position);
                updateAddressDetailsDto.setWardNo(entityDto.getId());
                updateAddressDetailsDto.setWardDetailId(entityDto.getAdditionalDetailId());
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //Do Nothing
    }

    class GetAllProposedKendraSpinnerData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            //Premise Ownership Type
            premiseOwnershipTypelList = updateAddressRepo.getPremiseOwnershipTypelList();

            //Owned By
            ownedByList = updateAddressRepo.getOwnedBylList();

            //State
            stateByList = updateAddressRepo.getAllStateBylList();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            bindSpinner();
        }
    }

    class GetDistrictVTCSpinnerData extends AsyncTask<String, Void, String> {

        private String pincode;
        private String stateId;
        private String districtId;
        private String type;

        public GetDistrictVTCSpinnerData(String pincode, String stateId, String districtId) {
            this.pincode = pincode;
            this.stateId = stateId;
            this.districtId = districtId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            type = strings[0];

            switch (type.toUpperCase()) {

                case STATE:
                    //STEP 1: State List
                    stateByList = updateAddressRepo.getAllStateBylListUsingPincode(pincode);
                    if (stateByList.size() == 2) {
                        updateAddressDetailsDto.setState(stateByList.get(1).getId());
                    }
                    break;

                case DISTRICT:
                    //STEP 2: District List
                    districtByList = updateAddressRepo.getAllDistrictBylList(stateId);
                    if (districtByList.size() == 2) {
                        updateAddressDetailsDto.setDistrict(districtByList.get(1).getId());
                    }
                    break;

                case "VTC":
                    //STEP 3: VTC List
                    VTCByList = updateAddressRepo.getAllVTCBylList(stateId, districtId);
                    break;

                default:
                    break;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            switch (type.toUpperCase()) {

                case STATE:
                    spinnerState.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, stateByList));
                    int appsstatePos = updateAddressRepo.getSelectedPos(stateByList, updateAddressDetailsDto.getState());
                    spinnerState.setSelection(appsstatePos);
                    spinnerState.setOnItemSelectedListener(UpdateKendraAddressFragment.this);
                    break;

                case DISTRICT:
                    setDistrictSpinnerAdapter(districtByList, spinnerPermanentDistrict, updateAddressDetailsDto.getDistrict());
                    break;

                case "VTC":
                    setVTCSpinnerAdapter(VTCByList, spinnerPermanentVTC, updateAddressDetailsDto.getVTC());
                    break;

                default:
                    break;
            }

            //Enable/disable views
            GUIUtils.setViewAndChildrenEnabled(layoutAddressDetail, IsEditable);
        }
    }

    class GetMunicipalCorporationWardNoSpinnerData extends AsyncTask<String, Void, String> {

        private String vtcId;
        private String municipalCorporationId;
        private String type;

        public GetMunicipalCorporationWardNoSpinnerData(String vtcId, String municipalCorporationId) {
            this.vtcId = vtcId;
            this.municipalCorporationId = municipalCorporationId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            type = strings[0];

            switch (type.toUpperCase()) {

                case MUNICIPAL_CORPORATION:
                    municipalCorporationList = updateAddressRepo.getMunicipalCorporationList(vtcId);
                    break;

                case WARD_NO:
                    wardNoList = updateAddressRepo.getWardNoList(municipalCorporationId);
                    break;

                default:
                    break;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            switch (type.toUpperCase()) {

                case MUNICIPAL_CORPORATION:
                    setMunicipalCorporationSpinnerAdapter(municipalCorporationList, spinnerMunicipalCorporation, updateAddressDetailsDto.getMunicipalCorporationId());
                    break;

                case WARD_NO:
                    setWardNoSpinnerAdapter(wardNoList, spinnerWardNo, updateAddressDetailsDto.getWardNo());
                    break;

                default:
                    break;
            }

            //Enable/disable views
            GUIUtils.setViewAndChildrenEnabled(layoutAddressDetail, IsEditable);
        }
    }

    //region Set District Adapter
    private void setDistrictSpinnerAdapter
    (List<CustomFranchiseeApplicationSpinnerDto> districtList, Spinner
            spinnerDistrict, String selDistrictId) {

        spinnerDistrict.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, districtList));
        int appsstatePos = updateAddressRepo.getSelectedPos(districtList, selDistrictId);
        spinnerDistrict.setSelection(appsstatePos);
        spinnerDistrict.setOnItemSelectedListener(this);
    }
    //endregion

    //region Set VTC Adapter
    private void setVTCSpinnerAdapter(List<CustomFranchiseeApplicationSpinnerDto> VTCList, Spinner spinnerVTC, String selVTCId) {

        spinnerVTC.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, VTCList));
        int appsstatePos = updateAddressRepo.getSelectedPos(VTCList, selVTCId);
        spinnerVTC.setSelection(appsstatePos);
        spinnerVTC.setOnItemSelectedListener(this);
    }
    //endregion

    //region Set Municipal Corporation Adapter
    private void setMunicipalCorporationSpinnerAdapter(List<CustomFranchiseeApplicationSpinnerDto> municipalCorporationList, Spinner
            spinnerMunicipalCorporation, String selMunicipalCorporationId) {

        spinnerMunicipalCorporation.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, municipalCorporationList));
        int pos = updateAddressRepo.getSelectedPos(municipalCorporationList, selMunicipalCorporationId);
        spinnerMunicipalCorporation.setSelection(pos);
        spinnerMunicipalCorporation.setOnItemSelectedListener(this);
    }
    //endregion

    //region Set Ward No Adapter
    private void setWardNoSpinnerAdapter(List<CustomFranchiseeApplicationSpinnerDto> wardNoList, Spinner spinnerWardNo, String
            selWardNoId) {

        spinnerWardNo.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, wardNoList));
        int pos = updateAddressRepo.getSelectedPos(wardNoList, selWardNoId);
        spinnerWardNo.setSelection(pos);
        spinnerWardNo.setOnItemSelectedListener(this);
    }
    //endregion

    public void reloadData(UpdateAddressDetailsDto addressDetailsDto) {
        this.updateAddressDetailsDto = addressDetailsDto;

        //Reload Data
        if (updateAddressDetailsDto == null)
            updateAddressDetailsDto = new UpdateAddressDetailsDto();

        boolean IsEdit = (!TextUtils.isEmpty(updateAddressDetailsDto.getIsEditable()) && updateAddressDetailsDto.getIsEditable().equalsIgnoreCase("1")) ? true : false;

        this.IsEditable = IsEdit;
        getAllProposedKendraSpinnerData = new GetAllProposedKendraSpinnerData();
        getAllProposedKendraSpinnerData.execute("");

    }

    public UpdateAddressDetailsDto getUpdateAddressDetailsDto() {

        updateAddressDetailsDto.setAddressLine1(editTextPermanentAddressLine1.getText().toString());
        updateAddressDetailsDto.setAddressLine2(editTextPermanentAddressLine2.getText().toString());
        updateAddressDetailsDto.setLandMark(editTextPermanentLandmark.getText().toString());
        updateAddressDetailsDto.setPinCode(editTextPermanentPincode.getText().toString());

        return updateAddressDetailsDto;
    }

    class CheckIsRuralForWardNo extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String villageId = strings[0];

            if (TextUtils.isEmpty(villageId) || villageId.equalsIgnoreCase("0")) {
                IsRural = null;
                return IsRural;
            }

            //Check Selected villageId IsRural or not
            IsRural = updateAddressRepo.IsRural(villageId);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (!TextUtils.isEmpty(IsRural) && IsRural.equalsIgnoreCase("0")) {
                layoutWardNo.setVisibility(View.VISIBLE);
                layoutMunicipalCorporation.setVisibility(View.VISIBLE);

                //Get Municipal Corporation Data
                getMunicipalCorporationWardNoSpinnerData = new GetMunicipalCorporationWardNoSpinnerData(updateAddressDetailsDto.getVTC(), updateAddressDetailsDto.getMunicipalCorporationId());
                getMunicipalCorporationWardNoSpinnerData.execute(MUNICIPAL_CORPORATION);

            } else {
                layoutWardNo.setVisibility(View.GONE);
                layoutMunicipalCorporation.setVisibility(View.GONE);
            }
        }

    }

    //region Get State District Using PinCode
    private void performChangesPincode(String pincode) {

        if (TextUtils.isEmpty(pincode))
            return;

        //Get State Using PinCode
        updateAddressDetailsDto.setPinCode(editTextPermanentPincode.getText().toString());
        getDistrictVTCSpinnerData = new GetDistrictVTCSpinnerData(updateAddressDetailsDto.getPinCode(), null, null);
        getDistrictVTCSpinnerData.execute(STATE);

    }
    //endregion

    public void saveAddressDetails() {

        //Get Data
        updateAddressDetailsDto = getUpdateAddressDetailsDto();

        //Internet Connectivity check
        if (!InternetConnection.isNetworkAvailable(context)) {
            showMessage("No Internet Connection.");
            return;
        }

        //Validate Mandatory fields
        int status = IsAddressDetailsValidated();
        if (status != 0)
            return;

        //Post data
        asyncSaveAddressDetails = new AsyncSaveAddressDetails(context, updateAddressDetailsDto, new AsyncSaveAddressDetails.Callback() {
            @Override
            public void onResult(String result) {
                processResult(result);
            }
        });
        asyncSaveAddressDetails.execute("");
    }

    private void processResult(String result) {
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
                result = result.replace("OKAY|", "");
                String msg = TextUtils.isEmpty(result) ? "Kendra Address updated Successfully." : result;
                showMessageWithFinish(msg);
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
        }
    }

    public void showMessageWithFinish(String msg) {
        if (TextUtils.isEmpty(msg))
            return;

        if (alert == null) {
            alertDialogBuilder = new AlertDialog.Builder(context);

            alertDialogBuilder
                    .setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            alert = null;
                            getActivity().finish();
                            // setResult(true);
                        }
                    });
            alert = alertDialogBuilder.create();
            alert.show();
        }
    }

    private void entityChangeBasedOnOwnerShipType(String premiseOwnerShipType, String ownedBy) {
        if (premiseOwnerShipType.equals("1")) {
            layoutOwnedBy.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(ownedBy) && ownedBy.equalsIgnoreCase("2")) {
                txtRentAgreementChooseFile.setText(OWNED_AGREEMENT_LBL);
            } else {
                txtRentAgreementChooseFile.setText(OWNERSHIP_PROOF_LBL);
                setCompulsoryMarkToRentLabel();

            }
            txtLandlordSocietyNOCChooseFile.setText("Society NOC");

        } else {
            layoutOwnedBy.setVisibility(View.GONE);
            txtRentAgreementChooseFile.setText(RENT_AGREEMENT_LBL);
            txtLandlordSocietyNOCChooseFile.setText("Landlord/Society NOC");

        }
    }

    public void setCompulsoryMarkToRentLabel() {
        TextView[] txtViewsForCompulsoryMark = {txtRentAgreementChooseFile};
        GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);
    }

}
