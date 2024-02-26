package in.vakrangee.franchisee.nextgenfranchiseeapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nononsenseapps.filepicker.Utils;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import androidx.core.content.FileProvider;
import butterknife.ButterKnife;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.commongui.FileAttachmentDialog;
import in.vakrangee.supercore.franchisee.commongui.FileUtils;
import in.vakrangee.supercore.franchisee.commongui.FilteredFilePickerActivity;
import in.vakrangee.supercore.franchisee.nextgenfranchiseeapplication.CustomFranchiseeApplicationSpinnerDto;
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

public class FAPProposedKendraDetailFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Context context;
    private DeprecateHandler deprecateHandler;
    private PermissionHandler permissionHandler;

    private int FROM = -1;
    private static final int OWNERSHIP_PROOF_COPY = 1;
    private static final int LANDLORD_NOC_COPY = 2;
    private static final int LOCATION_ADDRESS_PROOF_SCAN_COPY = 3;
    private FAPProposedKendraDetailDto fapProposedKendraDetailDto;
    private String SEL_FILE_TYPE;
    private FileAttachmentDialog fileAttachementDialog;
    private static final int CAMERA_PIC_REQUEST = 100;
    private static final int BROWSE_FOLDER_REQUEST = 101;
    private Uri picUri;                 //Picture URI
    private List<CustomFranchiseeApplicationSpinnerDto> vkendraModelList, allKendraModelList;
    private List<CustomFranchiseeApplicationSpinnerDto> sourceOfFundList;
    private List<CustomFranchiseeApplicationSpinnerDto> premiseOwnershipTypelList;
    private List<CustomFranchiseeApplicationSpinnerDto> ownedByList;
    private List<CustomFranchiseeApplicationSpinnerDto> stateByList;
    private List<CustomFranchiseeApplicationSpinnerDto> districtByList;
    private List<CustomFranchiseeApplicationSpinnerDto> VTCByList;
    private List<CustomFranchiseeApplicationSpinnerDto> municipalCorporationList;
    private List<CustomFranchiseeApplicationSpinnerDto> wardNoList;
    private FranchiseeApplicationRepository fapRepo;
    private CustomSearchableSpinner spinnerState, spinnerPermanentDistrict, spinnerPermanentVTC;
    private GetAllProposedKendraSpinnerData getAllProposedKendraSpinnerData = null;
    private GetDistrictVTCSpinnerData getDistrictVTCSpinnerData = null;
    private GetMunicipalCorporationWardNoSpinnerData getMunicipalCorporationWardNoSpinnerData = null;
    private boolean IsEditable = false;
    private CheckIsRuralForWardNo checkIsRuralForWardNo = null;

    private boolean IsHideControlsAsLocationNeedsToBeIdentified = false;

    //Added this after new Model addition [Bronze Model]
    private boolean IsInvestAreaConsentRemoved = true;
    private CustomFranchiseeApplicationSpinnerAdapter vakrnageeKendramodelapdater;

    private Spinner spinnerVakrangeeKendraModel;
    private CheckBox checkboxInvest7Lakhs;
    private CheckBox checkboxArea200Sqft;
    private Spinner spinnerSourceOfFund;
    private Spinner spinnerPremiseOwnerShipType;
    private ImageView imgRentAgreement;
    private ImageView imgLandlordSocietyNOC;
    private ImageView imgKendraLocAddressProof;
    private EditText editTextPermanentAddressLine1;
    private EditText editTextPermanentAddressLine2;
    private EditText editTextPermanentLandmark;
    private EditText editTextPermanentPincode;
    private EditText editTextRemarks;
    private TextView txtRentAgreementName;
    private TextView txtLandlordSocietyNOCName;
    private TextView txtKendraLocAddressProofName;
    private TextView txtVakrangeeKendraModelLbl;
    private TextView txtSourceOfFundLbl;
    private TextView txtPremiseOwnerShipTypeLbl;
    private TextView txtOwnedByLbl;
    private TextView txtPermanentAddressLine1Lbl, txtKendraLocAddressProofChooseFileLbl;
    private TextView txtPermanentLandmarkLbl;
    private TextView txtStateLbl;
    private TextView txtPermanentDistrictLbl;
    private TextView txtPermanentVTCLbl;
    private TextView txtWardNoLbl;
    private TextView txtPermanentPincodeLbl;
    private LinearLayout linvkmodel;
    private LinearLayout layoutOwnedBy;
    private TextView txtRentAgreementChooseFile;
    private TextView txtLandlordSocietyNOCChooseFile;
    private Spinner spinnerOwnedBy;
    private LinearLayout layoutProposedKendraParent;
    private LinearLayout layoutProposedKendraParent1;
    private LinearLayout layoutProposedKendraParent2;
    private LinearLayout layoutProposedKendraParent3;
    private LinearLayout layoutWardNo;
    private LinearLayout layoutRentAgreement;
    private LinearLayout layoutLandlordSocietyNOC;
    private LinearLayout layoutKendraLocAddressProof;
    private LinearLayout layoutLocationIdentified;
    private CheckBox checkbox_location_to_identified;
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
    private static final String RENT_AGREEMENT_LBL = "5 Year Registered Rent Agreement";
    private static final String OWNED_AGREEMENT_LBL = "5 Year Registered Rent Agreement /Registered Power Of Attorney";
    private static final String OWNERSHIP_PROOF_LBL = "Ownership Proof";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_fap_proposed_kendra_detail, container, false);

        bindViewId(rootView);
        //Data
        this.context = getContext();
        deprecateHandler = new DeprecateHandler(context);
        fapRepo = new FranchiseeApplicationRepository(context);
        permissionHandler = new PermissionHandler(getActivity());
        spinnerState = rootView.findViewById(R.id.spinnerState);
        spinnerPermanentDistrict = rootView.findViewById(R.id.spinnerPermanentDistrict);
        spinnerPermanentVTC = rootView.findViewById(R.id.spinnerPermanentVTC);

        ButterKnife.bind(this, rootView);

        //Input filter
        CommonUtils.InputFiletrWithMaxLength(editTextPermanentAddressLine1, SPECIAL_CHARS, 35);
        CommonUtils.InputFiletrWithMaxLength(editTextPermanentAddressLine2, SPECIAL_CHARS, 35);
        CommonUtils.InputFiletrWithMaxLength(editTextPermanentLandmark, SPECIAL_CHARS, 35);
        CommonUtils.InputFiletrWithMaxLength(editTextRemarks, SPECIAL_CHARS, 100);

        //Set Compulsory mark
        TextView[] txtViewsForCompulsoryMark = {txtVakrangeeKendraModelLbl, txtSourceOfFundLbl, txtPremiseOwnerShipTypeLbl, txtOwnedByLbl,
                txtPermanentAddressLine1Lbl, txtPermanentLandmarkLbl, txtStateLbl, txtPermanentDistrictLbl,
                txtPermanentVTCLbl, txtPermanentPincodeLbl, txtMunicipalCorporationLbl, txtWardNoLbl, txtKendraLocAddressProofChooseFileLbl};
        GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);

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
        //region Reference for kendra details
        txtMunicipalCorporationLbl = view.findViewById(R.id.txtMunicipalCorporationLbl);
        layoutMunicipalCorporation = view.findViewById(R.id.layoutMunicipalCorporation);
        spinnerMunicipalCorporation = view.findViewById(R.id.spinnerMunicipalCorporation);
        spinnerWardNo = view.findViewById(R.id.spinnerWardNo);
        spinnerVakrangeeKendraModel = view.findViewById(R.id.spinnerVakrangeeKendraModel);
        checkboxInvest7Lakhs = view.findViewById(R.id.checkboxInvest7Lakhs);
        checkboxArea200Sqft = view.findViewById(R.id.checkboxArea200Sqft);
        spinnerSourceOfFund = view.findViewById(R.id.spinnerSourceOfFund);
        spinnerPremiseOwnerShipType = view.findViewById(R.id.spinnerPremiseOwnerShipType);
        imgRentAgreement = view.findViewById(R.id.imgRentAgreement);
        imgLandlordSocietyNOC = view.findViewById(R.id.imgLandlordSocietyNOC);
        imgKendraLocAddressProof = view.findViewById(R.id.imgKendraLocAddressProof);
        editTextPermanentAddressLine1 = view.findViewById(R.id.editTextPermanentAddressLine1);
        editTextPermanentAddressLine2 = view.findViewById(R.id.editTextPermanentAddressLine2);
        editTextPermanentLandmark = view.findViewById(R.id.editTextPermanentLandmark);
        editTextPermanentPincode = view.findViewById(R.id.editTextPermanentPincode);
        editTextRemarks = view.findViewById(R.id.editTextRemarks);
        txtRentAgreementName = view.findViewById(R.id.txtRentAgreementName);
        txtLandlordSocietyNOCName = view.findViewById(R.id.txtLandlordSocietyNOCName);
        txtKendraLocAddressProofName = view.findViewById(R.id.txtKendraLocAddressProofName);
        txtVakrangeeKendraModelLbl = view.findViewById(R.id.txtVakrangeeKendraModelLbl);
        txtSourceOfFundLbl = view.findViewById(R.id.txtSourceOfFundLbl);
        txtPremiseOwnerShipTypeLbl = view.findViewById(R.id.txtPremiseOwnerShipTypeLbl);
        txtOwnedByLbl = view.findViewById(R.id.txtOwnedByLbl);
        txtPermanentAddressLine1Lbl = view.findViewById(R.id.txtPermanentAddressLine1Lbl);
        txtPermanentLandmarkLbl = view.findViewById(R.id.txtPermanentLandmarkLbl);
        txtStateLbl = view.findViewById(R.id.txtStateLbl);
        txtPermanentDistrictLbl = view.findViewById(R.id.txtPermanentDistrictLbl);
        txtPermanentVTCLbl = view.findViewById(R.id.txtPermanentVTCLbl);
        txtWardNoLbl = view.findViewById(R.id.txtWardNoLbl);
        txtPermanentPincodeLbl = view.findViewById(R.id.txtPermanentPincodeLbl);
        linvkmodel = view.findViewById(R.id.linvkmodel);
        layoutOwnedBy = view.findViewById(R.id.layoutOwnedBy);
        txtRentAgreementChooseFile = view.findViewById(R.id.txtRentAgreementChooseFile);
        txtLandlordSocietyNOCChooseFile = view.findViewById(R.id.txtLandlordSocietyNOCChooseFile);
        spinnerOwnedBy = view.findViewById(R.id.spinnerOwnedBy);
        layoutProposedKendraParent = view.findViewById(R.id.layoutProposedKendraParent);
        layoutWardNo = view.findViewById(R.id.layoutWardNo);
        layoutRentAgreement = view.findViewById(R.id.layoutRentAgreement);
        layoutLandlordSocietyNOC = view.findViewById(R.id.layoutLandlordSocietyNOC);
        layoutKendraLocAddressProof = view.findViewById(R.id.layoutKendraLocAddressProof);
        layoutLocationIdentified = view.findViewById(R.id.layoutLocationIdentified);
        checkbox_location_to_identified = view.findViewById(R.id.checkbox_location_to_identified);
        txtKendraLocAddressProofChooseFileLbl = view.findViewById(R.id.txtKendraLocAddressProofChooseFile);

        layoutProposedKendraParent1 = view.findViewById(R.id.layoutProposedKendraParent1);
        layoutProposedKendraParent2 = view.findViewById(R.id.layoutProposedKendraParent2);
        layoutProposedKendraParent3 = view.findViewById(R.id.layoutProposedKendraParent3);

        imgRentAgreement.setOnClickListener(this);
        imgLandlordSocietyNOC.setOnClickListener(this);
        imgKendraLocAddressProof.setOnClickListener(this);

    }

    public void setCompulsoryMarkToRentLabel() {
        /*TextView[] txtViewsForCompulsoryMark = {txtRentAgreementChooseFile};
        GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);*/
    }

    private void bindSpinner() {

        //STEP 1:Vakrangee Kendra Model
        vakrnageeKendramodelapdater = new CustomFranchiseeApplicationSpinnerAdapter(context, vkendraModelList);
        spinnerVakrangeeKendraModel.setAdapter(vakrnageeKendramodelapdater);
        int appvakrangeeKendraModelPos = fapRepo.getSelectedPos(vkendraModelList, fapProposedKendraDetailDto.getVakrangeeKendraModel());
        spinnerVakrangeeKendraModel.setSelection(appvakrangeeKendraModelPos);
        spinnerVakrangeeKendraModel.setOnItemSelectedListener(this);
        linvkmodel.setVisibility(View.GONE);

        //STEP 2 : Source of fund
        CustomFranchiseeApplicationSpinnerAdapter sourceoffundapdater = new CustomFranchiseeApplicationSpinnerAdapter(context, sourceOfFundList);
        spinnerSourceOfFund.setAdapter(sourceoffundapdater);
        int appsourceoffundPos = fapRepo.getSelectedPos(sourceOfFundList, fapProposedKendraDetailDto.getSourceOfFund());
        spinnerSourceOfFund.setSelection(appsourceoffundPos);
        spinnerSourceOfFund.setOnItemSelectedListener(this);

        //STEP 3 : premise Ownership Type
        CustomFranchiseeApplicationSpinnerAdapter spremiseOwnershipapdater = new CustomFranchiseeApplicationSpinnerAdapter(context, premiseOwnershipTypelList);
        spinnerPremiseOwnerShipType.setAdapter(spremiseOwnershipapdater);
        int appspremiseOwnershipPos = fapRepo.getSelectedPos(premiseOwnershipTypelList, fapProposedKendraDetailDto.getPremiseOwnerShipType());
        spinnerPremiseOwnerShipType.setSelection(appspremiseOwnershipPos);
        spinnerPremiseOwnerShipType.setOnItemSelectedListener(this);

        //STEP 4 : Owned By
        CustomFranchiseeApplicationSpinnerAdapter spremiseOwnedByapdater = new CustomFranchiseeApplicationSpinnerAdapter(context, ownedByList);
        spinnerOwnedBy.setAdapter(spremiseOwnedByapdater);
        int appsOwnedByPos = fapRepo.getSelectedPos(ownedByList, fapProposedKendraDetailDto.getOwnedBy());
        spinnerOwnedBy.setSelection(appsOwnedByPos);
        spinnerOwnedBy.setOnItemSelectedListener(this);

        spinner_focusablemode(spinnerState);
        spinner_focusablemode(spinnerPermanentDistrict);
        spinner_focusablemode(spinnerPermanentVTC);
        setDistrictSpinnerAdapter(stateByList, spinnerState, fapProposedKendraDetailDto.getState());

        //STEP 6 :Address Line 1
        editTextPermanentAddressLine1.setText(fapProposedKendraDetailDto.getAddressLine1());
        //STEP 7 :Address Line 2
        editTextPermanentAddressLine2.setText(fapProposedKendraDetailDto.getAddressLine2());
        //STEP 7 :LandMark
        editTextPermanentLandmark.setText(fapProposedKendraDetailDto.getLandMark());
        //STEP 8 :Pincode
        editTextPermanentPincode.setText(fapProposedKendraDetailDto.getPinCode());
        //STEP 9 :Pincode
        editTextRemarks.setText(fapProposedKendraDetailDto.getRemarks());

        //STEP 10: Investment consent
        checkboxInvest7Lakhs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean IsChecked) {
                String status = IsChecked ? "1" : "0";
                fapProposedKendraDetailDto.setIsInvestmentConsent(status);
            }
        });

        if (!TextUtils.isEmpty(fapProposedKendraDetailDto.getIsInvestmentConsent())) {
            int type = Integer.parseInt(fapProposedKendraDetailDto.getIsInvestmentConsent());
            if (type == 1)
                checkboxInvest7Lakhs.setChecked(true);
            else
                checkboxInvest7Lakhs.setChecked(false);
        }

        //STEP 11: Area consent
        checkboxArea200Sqft.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean IsChecked) {
                String status = IsChecked ? "1" : "0";
                fapProposedKendraDetailDto.setIsAreaConsent(status);
            }
        });

        if (!TextUtils.isEmpty(fapProposedKendraDetailDto.getIsAreaConsent())) {
            int type = Integer.parseInt(fapProposedKendraDetailDto.getIsAreaConsent());
            if (type == 1)
                checkboxArea200Sqft.setChecked(true);
            else
                checkboxArea200Sqft.setChecked(false);
        }

        //STEP 12: Ownership Proof Image
        boolean IsOwnerPDF = ((fapProposedKendraDetailDto.getOwnershipProofFileExt() != null) && fapProposedKendraDetailDto.getOwnershipProofFileExt().equalsIgnoreCase("pdf")) ? true : false;
        if (IsOwnerPDF) {
            Glide.with(context).asDrawable().load(R.drawable.pdf).
                    apply(new RequestOptions()
                            .error(R.drawable.ic_camera_alt_black_72dp)
                            .placeholder(R.drawable.ic_camera_alt_black_72dp)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)).into(imgRentAgreement);
        } else {
            if (!TextUtils.isEmpty(fapProposedKendraDetailDto.getOwnerProofFileId())) {
                String ownershipUrl = Constants.DownloadImageUrl + fapProposedKendraDetailDto.getOwnerProofFileId();
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

        //STEP 13: NOC Image
        boolean IsNOCPDF = ((fapProposedKendraDetailDto.getNOCProofFileExt() != null) && fapProposedKendraDetailDto.getNOCProofFileExt().equalsIgnoreCase("pdf")) ? true : false;
        if (IsNOCPDF) {
            Glide.with(context).asDrawable().load(R.drawable.pdf).apply(new RequestOptions()
                    .error(R.drawable.ic_camera_alt_black_72dp)
                    .placeholder(R.drawable.ic_camera_alt_black_72dp)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)).into(imgLandlordSocietyNOC);
        } else {
            if (!TextUtils.isEmpty(fapProposedKendraDetailDto.getNOCProofFileId())) {
                String nocUrl = Constants.DownloadImageUrl + fapProposedKendraDetailDto.getNOCProofFileId();
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

        //STEP 14: Kendra Location Image
        boolean IsLOCPDF = ((fapProposedKendraDetailDto.getOutletLocationFileExt() != null) && fapProposedKendraDetailDto.getOutletLocationFileExt().equalsIgnoreCase("pdf")) ? true : false;
        if (IsLOCPDF) {
            Glide.with(context).asDrawable().load(R.drawable.pdf).apply(new RequestOptions()
                    .error(R.drawable.ic_camera_alt_black_72dp)
                    .placeholder(R.drawable.ic_camera_alt_black_72dp)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)).into(imgKendraLocAddressProof);
        } else {
            if (!TextUtils.isEmpty(fapProposedKendraDetailDto.getOutLetLocationFileId())) {
                String locUrl = Constants.DownloadImageUrl + fapProposedKendraDetailDto.getOutLetLocationFileId();
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

        //STEP 15: Location needs to be Identified
        checkbox_location_to_identified.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean IsChecked) {
                String status = IsChecked ? "0" : "1";
                fapProposedKendraDetailDto.setIsLocationNeedsToBeIdentified(status);
                IsHideControlsAsLocationNeedsToBeIdentified = status.equalsIgnoreCase("0") ? true : false;
                hideConstrolsAsLocationCheckbox();
            }
        });

        if (!TextUtils.isEmpty(fapProposedKendraDetailDto.getIsLocationNeedsToBeIdentified())) {
            int type = Integer.parseInt(fapProposedKendraDetailDto.getIsLocationNeedsToBeIdentified());
            if (type == 0) {
                checkbox_location_to_identified.setChecked(true);
                IsHideControlsAsLocationNeedsToBeIdentified = true;
                hideConstrolsAsLocationCheckbox();
            } else {
                checkbox_location_to_identified.setChecked(false);
                IsHideControlsAsLocationNeedsToBeIdentified = false;
                hideConstrolsAsLocationCheckbox();
            }
        }

        //Hide Location Needs to be Identified Checkbox
        boolean isChkShown = ((fapProposedKendraDetailDto.getIsLocationNeedsToBeIdentifiedShown() != null) && fapProposedKendraDetailDto.getIsLocationNeedsToBeIdentifiedShown().equalsIgnoreCase("1")) ? true : false;
        if (isChkShown)
            checkbox_location_to_identified.setVisibility(View.VISIBLE);
        else
            checkbox_location_to_identified.setVisibility(View.GONE);

        //Enable/disable views
        GUIUtils.setViewAndChildrenEnabled(layoutRentAgreement, ((NextGenFranchiseeApplicationFormFragment) getParentFragment()).isOwnershipProofEditable);
        GUIUtils.setViewAndChildrenEnabled(layoutKendraLocAddressProof, ((NextGenFranchiseeApplicationFormFragment) getParentFragment()).isKendraAddressEditable);

        //Enable/disable views
        GUIUtils.setViewAndChildrenEnabled(layoutProposedKendraParent, IsEditable);
        GUIUtils.setViewAndChildrenEnabled(layoutProposedKendraParent1, IsEditable);
        GUIUtils.setViewAndChildrenEnabled(layoutProposedKendraParent2, IsEditable);
        GUIUtils.setViewAndChildrenEnabled(layoutProposedKendraParent3, IsEditable);
        GUIUtils.setViewAndChildrenEnabled(layoutLandlordSocietyNOC, IsEditable);
    }

    private void hideConstrolsAsLocationCheckbox() {

        if (!IsHideControlsAsLocationNeedsToBeIdentified) {
            layoutLocationIdentified.setVisibility(View.VISIBLE);

        } else {
            layoutLocationIdentified.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        permissionHandler.dismissSnackbar();
    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();

        if (Id == R.id.imgRentAgreement) {
            FROM = OWNERSHIP_PROOF_COPY;
            SEL_FILE_TYPE = IMAGES_PDF;
            showAttachmentDialog(view, SEL_FILE_TYPE);

        } else if (Id == R.id.imgLandlordSocietyNOC) {
            FROM = LANDLORD_NOC_COPY;
            SEL_FILE_TYPE = IMAGES_PDF;
            showAttachmentDialog(view, SEL_FILE_TYPE);

        } else if (Id == R.id.imgKendraLocAddressProof) {
            FROM = LOCATION_ADDRESS_PROOF_SCAN_COPY;
            SEL_FILE_TYPE = IMAGES_PDF;
            showAttachmentDialog(view, SEL_FILE_TYPE);
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

    public int IsFranchiseeProposedValidated() {

        //1 - vakrnagee Kendra model
        if (TextUtils.isEmpty(fapProposedKendraDetailDto.getVakrangeeKendraModel())) {
            Toast.makeText(context, "Please select Vakrangee Kendra Model.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerVakrangeeKendraModel, "Please select Vakrangee Kendra Model.", context);
            return 1;
        }
        if (!IsInvestAreaConsentRemoved) {
            // Investment Consent
            if (TextUtils.isEmpty(fapProposedKendraDetailDto.getIsInvestmentConsent()) || fapProposedKendraDetailDto.getIsInvestmentConsent().equalsIgnoreCase("0")) {
                String msg = "Please select Vakrange Kendra Model, <br/><b>" + checkboxInvest7Lakhs.getText().toString() + " </b>";
                showMessage(msg);
                return 1;
            }

            // Area Consent
            if (TextUtils.isEmpty(fapProposedKendraDetailDto.getIsAreaConsent()) || fapProposedKendraDetailDto.getIsAreaConsent().equalsIgnoreCase("0")) {
                String msg = "Please select Vakrange Kendra Model, <br/><b>" + checkboxArea200Sqft.getText().toString() + " </b>";
                showMessage(msg);
                return 1;
            }
        }

        //2- Source of Fund
        if (TextUtils.isEmpty(fapProposedKendraDetailDto.getSourceOfFund()) || fapProposedKendraDetailDto.getSourceOfFund().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Source of Fund.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerSourceOfFund, "Please select Source of Fund.", context);
            return 2;
        }
        if (!IsHideControlsAsLocationNeedsToBeIdentified) {

            //3- Premise Ownership Type
            if (TextUtils.isEmpty(fapProposedKendraDetailDto.getPremiseOwnerShipType()) || fapProposedKendraDetailDto.getPremiseOwnerShipType().equalsIgnoreCase("0")) {
                Toast.makeText(context, "Please select Premise Ownership Type.", Toast.LENGTH_LONG).show();
                GUIUtils.setErrorToSpinner(spinnerPremiseOwnerShipType, "Please select Premise Ownership Type.", context);
                return 3;
            }

            //Owned By
            String ownerShipType = spinnerPremiseOwnerShipType.getSelectedItem().toString();
            if (!TextUtils.isEmpty(ownerShipType) && ownerShipType.equalsIgnoreCase("Owned")) {
                if (TextUtils.isEmpty(fapProposedKendraDetailDto.getOwnedBy()) || fapProposedKendraDetailDto.getOwnedBy().equalsIgnoreCase("0")) {
                    Toast.makeText(context, "Please select Owned By.", Toast.LENGTH_LONG).show();
                    GUIUtils.setErrorToSpinner(spinnerOwnedBy, "Please select Owned By.", context);
                    return 3;
                }
            }

            if (fapProposedKendraDetailDto.getPremiseOwnerShipType().equals("1")) {
                if (!TextUtils.isEmpty(fapProposedKendraDetailDto.getOwnedBy()) && fapProposedKendraDetailDto.getOwnedBy().equalsIgnoreCase("2")) {
                    if (TextUtils.isEmpty(fapProposedKendraDetailDto.getOwnerProofFileId()) || fapProposedKendraDetailDto.getOwnerProofFileId().equalsIgnoreCase("0")) {
                        if (TextUtils.isEmpty(fapProposedKendraDetailDto.getRentAgreementBase64())) {
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

            //Rent Agreement or Ownership Proof
            /*if (TextUtils.isEmpty(fapProposedKendraDetailDto.getOwnerProofFileId()) || fapProposedKendraDetailDto.getOwnerProofFileId().equalsIgnoreCase("0")) {
                if (TextUtils.isEmpty(fapProposedKendraDetailDto.getRentAgreementBase64())) {
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
            }*/

            //Kendra Location Address
            if (TextUtils.isEmpty(fapProposedKendraDetailDto.getOutLetLocationFileId()) || fapProposedKendraDetailDto.getOutLetLocationFileId().equalsIgnoreCase("0")) {
                if (TextUtils.isEmpty(fapProposedKendraDetailDto.getKendraLocAddressProofBase64())) {
                    String msg = "Please add Kendra Location Address Proof";
                    showMessage(msg);
                    return 9;
                }
            }

            //4- Address line 1
            fapProposedKendraDetailDto.setAddressLine1(editTextPermanentAddressLine1.getText().toString().trim());
            if (TextUtils.isEmpty(fapProposedKendraDetailDto.getAddressLine1())) {
                Toast.makeText(context, "Please enter Address Line1.", Toast.LENGTH_LONG).show();
                editTextPermanentAddressLine1.setError("Please enter Address Line1.");
                return 4;
            }
            //5-Landmark
            fapProposedKendraDetailDto.setLandMark(editTextPermanentLandmark.getText().toString().trim());
            if (TextUtils.isEmpty(fapProposedKendraDetailDto.getLandMark())) {
                Toast.makeText(context, "Please enter Landmark.", Toast.LENGTH_LONG).show();
                editTextPermanentLandmark.setError("Please enter Landmark.");
                return 5;
            }
        }

        //10- Pincode
        fapProposedKendraDetailDto.setPinCode(editTextPermanentPincode.getText().toString().trim());
        if (TextUtils.isEmpty(fapProposedKendraDetailDto.getPinCode())) {
            Toast.makeText(context, "Please enter PINCode.", Toast.LENGTH_LONG).show();
            editTextPermanentPincode.setError("Please enter PINCode.");
            return 10;
        }

        //6-state
        if (TextUtils.isEmpty(fapProposedKendraDetailDto.getState()) || fapProposedKendraDetailDto.getState().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select State.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerState, "Please select State.", context);
            return 6;
        }

        //7 -District
        if (TextUtils.isEmpty(fapProposedKendraDetailDto.getDistrict()) || fapProposedKendraDetailDto.getDistrict().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select District.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerPermanentDistrict, "Please select District.", context);
            return 7;
        }

        //8- Village/Town/City
        if (TextUtils.isEmpty(fapProposedKendraDetailDto.getVTC()) || fapProposedKendraDetailDto.getVTC().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Village/Town/City.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerPermanentVTC, "Please select Village/Town/City.", context);
            return 8;
        }

        //Validate Municipal Corporation and Ward No
        if (!TextUtils.isEmpty(IsRural) && IsRural.equalsIgnoreCase("0")) {

            //Municipal Corporation
            if (TextUtils.isEmpty(fapProposedKendraDetailDto.getMunicipalCorporationId()) || fapProposedKendraDetailDto.getMunicipalCorporationId().equalsIgnoreCase("0")) {
                Toast.makeText(context, "Please select Municipal Corporation.", Toast.LENGTH_LONG).show();
                GUIUtils.setErrorToSpinner(spinnerMunicipalCorporation, "Please select Municipal Corporation.", context);
                return 7;
            }

            //Ward No
            if (TextUtils.isEmpty(fapProposedKendraDetailDto.getWardNo()) || fapProposedKendraDetailDto.getWardNo().equalsIgnoreCase("0")) {
                Toast.makeText(context, "Please select Ward No.", Toast.LENGTH_LONG).show();
                GUIUtils.setErrorToSpinner(spinnerWardNo, "Please select Ward No.", context);
                return 7;
            }
        }

        //STEP 11: Remarks Length should be greater than 3 if any thing entered
        fapProposedKendraDetailDto.setRemarks(editTextRemarks.getText().toString().trim());
        if (!TextUtils.isEmpty(fapProposedKendraDetailDto.getRemarks())) {
            int remarksLen = editTextRemarks.getText().toString().length();
            if (remarksLen < 3) {
                Toast.makeText(context, "The Remarks must be minimum 3 characters long.", Toast.LENGTH_LONG).show();
                editTextRemarks.setError("The Remarks must be minimum 3 characters long.");
                return 8;
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

            case OWNERSHIP_PROOF_COPY:
                if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgRentAgreement);
                else
                    Glide.with(context).asBitmap().load(bitmap).into(imgRentAgreement);

                txtRentAgreementName.setVisibility(View.VISIBLE);
                txtRentAgreementName.setText(fileName);
                fapProposedKendraDetailDto.setRentAgreementBase64(base64);
                fapProposedKendraDetailDto.setOwnershipProofFileExt(ext);
                fapProposedKendraDetailDto.setRentAgreementFileName(fileName);
                break;

            case LANDLORD_NOC_COPY:
                if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgLandlordSocietyNOC);
                else
                    Glide.with(context).asBitmap().load(bitmap).into(imgLandlordSocietyNOC);

                txtLandlordSocietyNOCName.setVisibility(View.VISIBLE);
                txtLandlordSocietyNOCName.setText(fileName);
                fapProposedKendraDetailDto.setLandLordSocietyNOCBase64(base64);
                fapProposedKendraDetailDto.setNOCProofFileExt(ext);
                fapProposedKendraDetailDto.setLandLordSocietyNOCFileName(fileName);
                break;

            case LOCATION_ADDRESS_PROOF_SCAN_COPY:
                if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgKendraLocAddressProof);
                else
                    Glide.with(context).asBitmap().load(bitmap).into(imgKendraLocAddressProof);

                txtKendraLocAddressProofName.setVisibility(View.VISIBLE);
                txtKendraLocAddressProofName.setText(fileName);
                fapProposedKendraDetailDto.setKendraLocAddressProofBase64(base64);
                fapProposedKendraDetailDto.setOutletLocationFileExt(ext);
                fapProposedKendraDetailDto.setKendraLocAddressProofFileName(fileName);
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

    private void spinner_focusablemode(CustomSearchableSpinner stateSpinner) {
        //Do Nothing
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        int Id = adapterView.getId();
        if (Id == R.id.spinnerVakrangeeKendraModel) {
            if (position >= 0) {
                if (position == 0) {
                    linvkmodel.setVisibility(View.GONE);
                } else {
                    linvkmodel.setVisibility(View.VISIBLE);
                    entityChangeBasedonCheckBox(position);
                }
            }
        } else if (Id == R.id.spinnerSourceOfFund) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerSourceOfFund.getItemAtPosition(position);
                fapProposedKendraDetailDto.setSourceOfFund(entityDto.getId());
            }
        } else if (Id == R.id.spinnerPremiseOwnerShipType) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerPremiseOwnerShipType.getItemAtPosition(position);
                fapProposedKendraDetailDto.setPremiseOwnerShipType(entityDto.getId());
                entityChangeBasedOnOwnerShipType(fapProposedKendraDetailDto.getPremiseOwnerShipType(), fapProposedKendraDetailDto.getOwnedBy());
            }
        } else if (Id == R.id.spinnerOwnedBy) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerOwnedBy.getItemAtPosition(position);
                fapProposedKendraDetailDto.setOwnedBy(entityDto.getId());
                entityChangeBasedOnOwnerShipType(fapProposedKendraDetailDto.getPremiseOwnerShipType(), fapProposedKendraDetailDto.getOwnedBy());
            }
        } else if (Id == R.id.spinnerState) {
            if (position > 0) {
                spinnerState.setTitle("Select State");
                spinnerState.requestFocus();
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerState.getItemAtPosition(position);
                if (!entityDto.getId().equals("0")) {
                    fapProposedKendraDetailDto.setState(entityDto.getId());

                    //Get District
                    fapProposedKendraDetailDto.setPinCode(editTextPermanentPincode.getText().toString());
                    getDistrictVTCSpinnerData = new GetDistrictVTCSpinnerData(fapProposedKendraDetailDto.getPinCode(), fapProposedKendraDetailDto.getState(), null);
                    getDistrictVTCSpinnerData.execute(DISTRICT);

                }
            } else {
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", PLEASE_SELECT));

                setDistrictSpinnerAdapter(list1, spinnerPermanentDistrict, null);
                fapProposedKendraDetailDto.setDistrict(null);
                setDistrictSpinnerAdapter(list1, spinnerPermanentVTC, null);
                fapProposedKendraDetailDto.setVTC(null);

            }
        } else if (Id == R.id.spinnerPermanentDistrict) {
            if (position > 0) {
                spinnerPermanentDistrict.setTitle("Select District");
                spinnerPermanentDistrict.requestFocus();
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerPermanentDistrict.getItemAtPosition(position);
                if (!entityDto.getId().equals("0")) {
                    fapProposedKendraDetailDto.setDistrict(entityDto.getId());

                    //Get VTC
                    fapProposedKendraDetailDto.setPinCode(editTextPermanentPincode.getText().toString());
                    getDistrictVTCSpinnerData = new GetDistrictVTCSpinnerData(fapProposedKendraDetailDto.getPinCode(), fapProposedKendraDetailDto.getState(), fapProposedKendraDetailDto.getDistrict());
                    getDistrictVTCSpinnerData.execute("VTC");

                }
            } else {
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", PLEASE_SELECT));

                setDistrictSpinnerAdapter(list1, spinnerPermanentVTC, null);
                fapProposedKendraDetailDto.setVTC(null);

            }
        } else if (Id == R.id.spinnerPermanentVTC) {
            spinnerPermanentVTC.setTitle("Select VTC");
            spinnerPermanentVTC.requestFocus();
            CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerPermanentVTC.getItemAtPosition(position);
            fapProposedKendraDetailDto.setVTC(entityDto.getId());
            checkIsRuralForWardNo = new CheckIsRuralForWardNo();
            checkIsRuralForWardNo.execute(entityDto.getId());

        } else if (Id == R.id.spinnerMunicipalCorporation) {
            if (position > 0) {
                spinnerMunicipalCorporation.setTitle("Select Municipal Corporation");
                spinnerMunicipalCorporation.requestFocus();
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerMunicipalCorporation.getItemAtPosition(position);
                if (!entityDto.getId().equals("0")) {
                    fapProposedKendraDetailDto.setMunicipalCorporationId(entityDto.getId());
                    fapProposedKendraDetailDto.setPinCode(editTextPermanentPincode.getText().toString());
                    getMunicipalCorporationWardNoSpinnerData = new GetMunicipalCorporationWardNoSpinnerData(fapProposedKendraDetailDto.getVTC(), fapProposedKendraDetailDto.getMunicipalCorporationId());
                    getMunicipalCorporationWardNoSpinnerData.execute(WARD_NO);

                }
            } else {
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", PLEASE_SELECT));

                setWardNoSpinnerAdapter(list1, spinnerWardNo, null);
                fapProposedKendraDetailDto.setWardNo(null);

            }
        } else if (Id == R.id.spinnerWardNo) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerWardNo.getItemAtPosition(position);
                fapProposedKendraDetailDto.setWardNo(entityDto.getId());
                fapProposedKendraDetailDto.setWardDetailId(entityDto.getAdditionalDetailId());
            }
        }
    }

    private void entityChangeBasedOnOwnerShipType(String premiseOwnerShipType, String ownedBy) {
        if (premiseOwnerShipType.equals("1")) {
            layoutOwnedBy.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(ownedBy) && ownedBy.equalsIgnoreCase("2")) {
                txtRentAgreementChooseFile.setText(OWNED_AGREEMENT_LBL);
            } else {
                txtRentAgreementChooseFile.setText(OWNERSHIP_PROOF_LBL);
                TextView[] txtViewsForCompulsoryMark = {txtRentAgreementChooseFile};
                GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);
            }
            setCompulsoryMarkToRentLabel();
            txtLandlordSocietyNOCChooseFile.setText("Society NOC");

        } else {
            layoutOwnedBy.setVisibility(View.GONE);
            txtRentAgreementChooseFile.setText(RENT_AGREEMENT_LBL);
            setCompulsoryMarkToRentLabel();
            txtLandlordSocietyNOCChooseFile.setText("Landlord/Society NOC");

        }
    }

    //region base on Spinner Check Box item change
    private void entityChangeBasedonCheckBox(int position) {
        CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerVakrangeeKendraModel.getItemAtPosition(position);
        fapProposedKendraDetailDto.setVakrangeeKendraModel(entityDto.getId());
        if (entityDto.getName().trim().equalsIgnoreCase("Vakrangee Kendra Gold Model")) {
            checkboxInvest7Lakhs.setText("I am ready to invest Rs.10 Lakhs plus working capital as mentioned in Franchisee presentation.");
            checkboxArea200Sqft.setText("Area of 300 sq.ft is available.");
        } else {
            checkboxInvest7Lakhs.setText("I am ready to invest Rs.7.5 Lakhs plus working capital as mentioned in Franchisee presentation.");
            checkboxArea200Sqft.setText("Area of 150 sq.ft is available.");
        }

        //Removed after adding Bronze Model
        if (IsInvestAreaConsentRemoved)
            linvkmodel.setVisibility(View.GONE);
        else
            linvkmodel.setVisibility(View.VISIBLE);
    }//endregion

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

            //STEP 1:Vakrangee Kendra Model
            vkendraModelList = fapRepo.getVakrangeeKendraModelList();
            allKendraModelList = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
            allKendraModelList.addAll(vkendraModelList);

            //STEP 2 : Source of fund
            sourceOfFundList = fapRepo.getSourceOfFundinglList();

            //STEP 3 : premise Ownership Type
            premiseOwnershipTypelList = fapRepo.getPremiseOwnershipTypelList();

            //STEP 4 : Owned By
            ownedByList = fapRepo.getOwnedBylList();

            //STEP 5: State
            stateByList = fapRepo.getAllStateBylList();

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
                    stateByList = fapRepo.getAllStateBylListUsingPincode(pincode);
                    if (stateByList.size() == 2) {
                        fapProposedKendraDetailDto.setState(stateByList.get(1).getId());
                    }
                    break;

                case DISTRICT:
                    //STEP 2: District List
                    districtByList = fapRepo.getAllDistrictBylList(stateId);
                    if (districtByList.size() == 2) {
                        fapProposedKendraDetailDto.setDistrict(districtByList.get(1).getId());
                    }
                    break;

                case "VTC":
                    //STEP 3: VTC List
                    VTCByList = fapRepo.getAllVTCBylList(stateId, districtId);
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
                    int appsstatePos = fapRepo.getSelectedPos(stateByList, fapProposedKendraDetailDto.getState());
                    spinnerState.setSelection(appsstatePos);
                    spinnerState.setOnItemSelectedListener(FAPProposedKendraDetailFragment.this);
                    break;

                case DISTRICT:
                    setDistrictSpinnerAdapter(districtByList, spinnerPermanentDistrict, fapProposedKendraDetailDto.getDistrict());
                    break;

                case "VTC":
                    setVTCSpinnerAdapter(VTCByList, spinnerPermanentVTC, fapProposedKendraDetailDto.getVTC());
                    break;

                default:
                    break;
            }

            //Enable/disable views
            GUIUtils.setViewAndChildrenEnabled(layoutProposedKendraParent, IsEditable);
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
                    municipalCorporationList = fapRepo.getMunicipalCorporationList(vtcId);
                    break;

                case WARD_NO:
                    wardNoList = fapRepo.getWardNoList(municipalCorporationId);
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
                    setMunicipalCorporationSpinnerAdapter(municipalCorporationList, spinnerMunicipalCorporation, fapProposedKendraDetailDto.getMunicipalCorporationId());
                    break;

                case WARD_NO:
                    setWardNoSpinnerAdapter(wardNoList, spinnerWardNo, fapProposedKendraDetailDto.getWardNo());
                    break;

                default:
                    break;
            }

            //Enable/disable views
            GUIUtils.setViewAndChildrenEnabled(layoutProposedKendraParent, IsEditable);
        }
    }

    //region Set District Adapter
    private void setDistrictSpinnerAdapter(List<CustomFranchiseeApplicationSpinnerDto> districtList, Spinner spinnerDistrict, String selDistrictId) {

        spinnerDistrict.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, districtList));
        int appsstatePos = fapRepo.getSelectedPos(districtList, selDistrictId);
        spinnerDistrict.setSelection(appsstatePos);
        spinnerDistrict.setOnItemSelectedListener(this);
    }
    //endregion

    //region Set VTC Adapter
    private void setVTCSpinnerAdapter(List<CustomFranchiseeApplicationSpinnerDto> VTCList, Spinner spinnerVTC, String selVTCId) {

        spinnerVTC.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, VTCList));
        int appsstatePos = fapRepo.getSelectedPos(VTCList, selVTCId);
        spinnerVTC.setSelection(appsstatePos);
        spinnerVTC.setOnItemSelectedListener(this);
    }
    //endregion

    //region Set Municipal Corporation Adapter
    private void setMunicipalCorporationSpinnerAdapter(List<CustomFranchiseeApplicationSpinnerDto> municipalCorporationList, Spinner spinnerMunicipalCorporation, String selMunicipalCorporationId) {

        spinnerMunicipalCorporation.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, municipalCorporationList));
        int pos = fapRepo.getSelectedPos(municipalCorporationList, selMunicipalCorporationId);
        spinnerMunicipalCorporation.setSelection(pos);
        spinnerMunicipalCorporation.setOnItemSelectedListener(this);
    }
    //endregion

    //region Set Ward No Adapter
    private void setWardNoSpinnerAdapter(List<CustomFranchiseeApplicationSpinnerDto> wardNoList, Spinner spinnerWardNo, String selWardNoId) {

        spinnerWardNo.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, wardNoList));
        int pos = fapRepo.getSelectedPos(wardNoList, selWardNoId);
        spinnerWardNo.setSelection(pos);
        spinnerWardNo.setOnItemSelectedListener(this);
    }
    //endregion

    public void reloadData(String data, boolean IsEditable) {
        //Reload Data
        if (TextUtils.isEmpty(data))
            fapProposedKendraDetailDto = new FAPProposedKendraDetailDto();
        else {
            try {
                Gson gson = new GsonBuilder().create();
                fapProposedKendraDetailDto = gson.fromJson(data, FAPProposedKendraDetailDto.class);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.IsEditable = IsEditable;
        getAllProposedKendraSpinnerData = new GetAllProposedKendraSpinnerData();
        getAllProposedKendraSpinnerData.execute("");

    }

    public FAPProposedKendraDetailDto getFapProposedKendraDetailDto() {

        fapProposedKendraDetailDto.setAddressLine1(editTextPermanentAddressLine1.getText().toString());
        fapProposedKendraDetailDto.setAddressLine2(editTextPermanentAddressLine2.getText().toString());
        fapProposedKendraDetailDto.setLandMark(editTextPermanentLandmark.getText().toString());
        fapProposedKendraDetailDto.setPinCode(editTextPermanentPincode.getText().toString());
        fapProposedKendraDetailDto.setRemarks(editTextRemarks.getText().toString());

        return fapProposedKendraDetailDto;
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
            IsRural = fapRepo.IsRural(villageId);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (!TextUtils.isEmpty(IsRural) && IsRural.equalsIgnoreCase("0")) {
                layoutWardNo.setVisibility(View.VISIBLE);
                layoutMunicipalCorporation.setVisibility(View.VISIBLE);

                //Get Municipal Corporation Data
                getMunicipalCorporationWardNoSpinnerData = new GetMunicipalCorporationWardNoSpinnerData(fapProposedKendraDetailDto.getVTC(), fapProposedKendraDetailDto.getMunicipalCorporationId());
                getMunicipalCorporationWardNoSpinnerData.execute(MUNICIPAL_CORPORATION);

            } else {
                layoutWardNo.setVisibility(View.GONE);
                layoutMunicipalCorporation.setVisibility(View.GONE);
            }
        }
    }

    class GetVakrangeeKendraTypeData extends AsyncTask<String, Void, String> {

        private String vakrangeKendraType;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        public GetVakrangeeKendraTypeData(String vakrangeKendraType) {
            this.vakrangeKendraType = vakrangeKendraType;

        }

        @Override
        protected String doInBackground(String... strings) {

            //STEP 1:Vakrangee Kendra Model
            vkendraModelList = fapRepo.getVakrangeeKendraModelList();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            validateSpinner(vakrangeKendraType);

        }
    }

    private void bindVakrangeeKendraTypeSpinner() {
        if (vakrnageeKendramodelapdater != null)
            vakrnageeKendramodelapdater.notify(vkendraModelList);
    }

    public void updateVakrangeeKendraModel(String vakrangeKendraType) {
        if (vkendraModelList == null || vkendraModelList.size() == 0 || TextUtils.isEmpty(vakrangeKendraType) || TextUtils.isEmpty(fapProposedKendraDetailDto.getNayaraModel()))
            return;

        //Nayara
        CustomFranchiseeApplicationSpinnerDto nayaraDto = getNayaraDto(fapProposedKendraDetailDto.getNayaraModel());
        if (nayaraDto == null)
            return;

        vkendraModelList = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
        vkendraModelList.addAll(allKendraModelList);
        if (!vakrangeKendraType.equalsIgnoreCase("3")) {            //Nayara
            vkendraModelList.remove(nayaraDto);
        }

        bindVakrangeeKendraTypeSpinner();
    }

    private CustomFranchiseeApplicationSpinnerDto getNayaraDto(String value) {
        for (CustomFranchiseeApplicationSpinnerDto dto : allKendraModelList) {
            if (dto.getId().contains(value)) {
                return dto;
            }
        }
        return null;
    }

    private void validateSpinner(String vakrangeKendraType) {
        if (vkendraModelList.size() == 0 || TextUtils.isEmpty(vakrangeKendraType))
            return;

        String IsBCAStatus = ((NextGenFranchiseeApplicationFormFragment) getParentFragment()).applicationFormDto.getIsBCA();
        String IsBCA = TextUtils.isEmpty(IsBCAStatus) ? "0" : IsBCAStatus;
        CustomFranchiseeApplicationSpinnerDto bcaDto = spinnerKeyDto("BCA");
        CustomFranchiseeApplicationSpinnerDto ioclDto = spinnerKeyDto("IOCL");

        try {
            if (TextUtils.isEmpty(vakrangeKendraType)) {
                if (bcaDto != null)
                    vkendraModelList.remove(bcaDto);
                if (ioclDto != null)
                    vkendraModelList.remove(ioclDto);

                bindVakrangeeKendraTypeSpinner();
                return;
            }

            //IOCL
            if (vakrangeKendraType.equalsIgnoreCase("2")) {
                if (bcaDto != null) {
                    vkendraModelList.remove(bcaDto);
                    bindVakrangeeKendraTypeSpinner();
                    return;
                }
            }

            //NON-IOCL + BCA
            if (vakrangeKendraType.equalsIgnoreCase("1") && IsBCA.equalsIgnoreCase("1")) {
                if (ioclDto != null)
                    vkendraModelList.remove(ioclDto);

            } else {         // NON-IOCL
                if (bcaDto != null)
                    vkendraModelList.remove(bcaDto);
                if (ioclDto != null)
                    vkendraModelList.remove(ioclDto);

            }

            bindVakrangeeKendraTypeSpinner();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private CustomFranchiseeApplicationSpinnerDto spinnerKeyDto(String key) {
        for (CustomFranchiseeApplicationSpinnerDto dto : vkendraModelList) {
            if (dto.getName().contains(key)) {
                return dto;
            }
        }
        return null;
    }

    //region Get State District Using PinCode
    private void performChangesPincode(String pincode) {

        if (TextUtils.isEmpty(pincode))
            return;

        //Get State Using PinCode
        fapProposedKendraDetailDto.setPinCode(editTextPermanentPincode.getText().toString());
        getDistrictVTCSpinnerData = new GetDistrictVTCSpinnerData(fapProposedKendraDetailDto.getPinCode(), null, null);
        getDistrictVTCSpinnerData.execute(STATE);

    }
    //endregion

}
