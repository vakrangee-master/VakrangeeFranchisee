package in.vakrangee.franchisee.sitelayout.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import in.vakrangee.franchisee.sitelayout.NextGenPhotoViewPager;
import in.vakrangee.franchisee.sitelayout.NextGenViewPager;
import in.vakrangee.franchisee.sitelayout.R;
import in.vakrangee.franchisee.sitelayout.StatusHistoryDialog;
import in.vakrangee.franchisee.sitelayout.Utils;
import in.vakrangee.franchisee.sitelayout.asyntask.AsyncSaveSiteReadinessVerification;
import in.vakrangee.franchisee.sitelayout.mendatorybranding.MandatoryBrandingActivity;
import in.vakrangee.franchisee.sitelayout.sitecommencement.NextGenSiteCommencementActivity;
import in.vakrangee.franchisee.sitelayout.sitereadiness.SiteReadinessActivity;
import in.vakrangee.franchisee.sitelayout.update_kendra_address.AsyncGetAddressDetails;
import in.vakrangee.franchisee.sitelayout.update_kendra_address.UpdateAddressDetailsDto;
import in.vakrangee.franchisee.sitelayout.update_kendra_address.UpdateKendraAddressDetailsActivity;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.commongui.DateTimePickerDialog;
import in.vakrangee.supercore.franchisee.commongui.PopupUtils;
import in.vakrangee.supercore.franchisee.commongui.animation.AnimationHanndler;
import in.vakrangee.supercore.franchisee.commongui.imagegallery.ImageDto;
import in.vakrangee.supercore.franchisee.commongui.imagepreview.CustomImagePreviewDialog;
import in.vakrangee.supercore.franchisee.model.FranchiseeDetails;
import in.vakrangee.supercore.franchisee.model.FranchiseeRemarkDetails;
import in.vakrangee.supercore.franchisee.model.FranchiseeTimeLineDetails;
import in.vakrangee.supercore.franchisee.support.CustomSpinnerAdapter;
import in.vakrangee.supercore.franchisee.support.WorkCommencementStatusDto;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.EditTextWatcher;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.utils.GUIUtils;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.utils.JSONUtils;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;
import in.vakrangee.supercore.franchisee.utils.SharedPrefUtils;
import in.vakrangee.supercore.franchisee.webservice.WebService;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class NextGenProfileFragment extends BaseFragment {

    private final static String TAG = "NextGenProfileFragment";

    private static final int PERMISSIONS_REQUEST_PHONE_CALL = 1;
    FranchiseeDetails franchiseeDetails;
    private Spinner mySpinner;
    View view;
    TextView NextGenFranchiseeApplicationNo, VKID, FranchiseeName, AddressLine1, VTC, Block, District, State, Pin, MobileNumber, ScheduleVisit, StatusHistory;
    ImageView profilepic;
    private InternetConnection internetConnection;
    private List<FranchiseeTimeLineDetails> franchiseeTimeLineDetailsList;

    LinearLayout layoutScheduleVisit, layoutStatus;

    private boolean isSiteCommencement = false;
    private boolean isSiteWorkInProgress = false;
    private boolean isSiteWorkCompletion = false;
    private boolean isSiteReadiness = false;
    private boolean isMandatoryBrandingCompleted = false;
    private boolean IsSiteVisit = false;
    private String modetype;
    private ImageView imgGSTImage;
    private PermissionHandler permissionHandler;
    private Uri picUri;
    private Bitmap bitmap = null;

    private static final int CAMERA_PIC_REQUEST = 1;
    private CustomImagePreviewDialog customImagePreviewDialog;

    private boolean isAdhoc = false;

    private AsyncNextgenProfileUpdate asyncNextgenLocationUpdate;
    private LinearLayout layoutLogisticsPaymentDate;

    //region Other Visit Details
    private LinearLayout layoutOtherVisitDetails, layoutGSTDetail, layoutGSTDetailData, layoutLogisticsPaymentMade;
    private RadioGroup radioGroupWelcomeMail, radioGroupCallReceived, radioGroupLogisticsMode, radioGroupConsent, radioGroupGstRegistered;
    private TextView txtConsentText, txtGSTNote;
    private EditText editTextGSTNumber, editTextGSTAddress;
    //endregion
    private RadioButton radioButtonLogisticsYes, radioButtonLogisticsNo;
    private TextView textViewStartDate, logisticpaymentdate;        // Start Date
    private int selectedDateTimeId = 0;
    private static final String YYYY_MM_DD_CONST = "yyyy-MM-dd";
    private static final String DD_MM_YYYY_CONST = "dd-MM-yyyy";
    private DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm a", Locale.US);
    private DateFormat dateFormatter1 = new SimpleDateFormat(YYYY_MM_DD_CONST, Locale.US);
    private DateFormat dateFormatter2 = new SimpleDateFormat(DD_MM_YYYY_CONST, Locale.US);

    private Date startDate;
    private String strStartDate;
    private Date commencementDate;
    private String strCommencementDate;

    private DateTimePickerDialog dateTimePickerDialog;
    private boolean IsEditable;

    private NextGenProfileFragment() {
    }

    public NextGenProfileFragment(FranchiseeDetails franchiseeDetails) {
        this.franchiseeDetails = franchiseeDetails;
        IsSiteVisit = true;
    }

    public NextGenProfileFragment(String modetype, FranchiseeDetails franchiseeDetails) {

        this.modetype = modetype;
        this.franchiseeDetails = franchiseeDetails;
        if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXTGEN_SITE_WORK_COMMENCEMENT)) {
            isSiteCommencement = true;
        } else if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXT_GEN_WORK_IN_PROGRESS)) {
            isSiteWorkInProgress = true;
        } else if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXTGEN_WORK_COMPLETION_INTIMATION)) {
            isSiteWorkCompletion = true;
        } else if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXTGEN_SITE_READINESS_AND_VERIFICATION)) {
            isSiteReadiness = true;
        } else if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXTGEN_SITE_MANDATORY_BRANDING_VERIFICATION)) {
            isMandatoryBrandingCompleted = true;
        } else {
            IsSiteVisit = true;
        }
    }

    public NextGenProfileFragment(String modetype, FranchiseeDetails franchiseeDetails, boolean Iseditable) {

        this.modetype = modetype;
        this.franchiseeDetails = franchiseeDetails;
        if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXTGEN_SITE_WORK_COMMENCEMENT)) {
            isSiteCommencement = true;
        } else if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXT_GEN_WORK_IN_PROGRESS)) {
            isSiteWorkInProgress = true;
            this.IsEditable = Iseditable;
        } else if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXTGEN_WORK_COMPLETION_INTIMATION)) {
            isSiteWorkCompletion = true;
        } else if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXTGEN_SITE_READINESS_AND_VERIFICATION)) {
            isSiteReadiness = true;
        }  else if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXTGEN_SITE_MANDATORY_BRANDING_VERIFICATION)) {
            isMandatoryBrandingCompleted = true;
        }
        else {
            IsSiteVisit = true;
        }
    }

    private AlphaAnimation btnClickAnim = new AlphaAnimation(1F, 0.6F);
    ScrollView scrollViewNextGenProfile;
    LinearLayout linearLayoutProfileContent;

    TextView tooltipLogisticsPayment, tooltipsGSTINDetails, tooltipCommunicationConfirmation, tooltipoConsentForVakrnagge;
    private String blockCharacterSet = "~#^|$%&*!";
    private LinearLayout layoutVerified;
    private CheckBox checkBoxProfileVerify;
    private LinearLayout layoutLocationNotIdentified;
    private TextView textViewExpectedIdentificationDate;
    private EditText editTextLocationNotIdentifiedRemarks;
    private LinearLayout layoutLocNotIdentifiedAndConsentNotAgreeSubmitButton;
    private Button btn_submit_loc_not_identified_and_consent_disagree;
    private Date expectedIdentificationDate;
    private String strExpectedIdentificationDate;
    private LinearLayout layoutConsentNotAgree;
    private CheckBox checkBox_consent_text1, checkBox_consent_text2, checkBox_consent_text3, checkBox_consent_text4;
    private EditText editTextConsentNotAgree;
    private static final String AESTRIC = "\\*";
    private LinearLayout layoutConsentNotAgreeRemarks, linearLocationStatus;
    private CustomAddressPreviewDialog customAddressPreviewDialog;
    private AsyncSaveSiteReadinessVerification asyncSaveSiteReadinessVerification = null;
    private Connection connection;
    private LinearLayout layoutReadinessVerification, layoutCommencementDate, layoutSubmitReadinessVerification;
    private TextView txtCommencementDateLbl, txtCommencementDate;
    private Spinner spinnerWorkCommencement;
    private CustomSpinnerAdapter customSpinnerAdapter;
    private Button btnReadinessVerification;
    private LinearLayout layoutRandomChanges, layoutDeliveryAddressEdit;
    private TextView txtConsentTitle, textDeliveryAddress;
    private MaterialButton btnUpdateAddress;
    private AsyncGetAddressDetails asyncGetAddressDetails = null;
    private LinearLayout layoutUpdateAddress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_vakrangee_kendra_profile_fragement, container, false);
        final Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fontawesome-webfont.ttf");

        // Get App MODE
        isAdhoc = Constants.ENABLE_ADHOC_MODE || Constants.ENABLE_FRANCHISEE_MODE;

        internetConnection = new InternetConnection(getActivity());
        permissionHandler = new PermissionHandler(getActivity());
        connection = new Connection(getContext());

        NextGenFranchiseeApplicationNo = (TextView) view.findViewById(R.id.frachicessno);
        VKID = (TextView) view.findViewById(R.id.vkid_name);
        FranchiseeName = (TextView) view.findViewById(R.id.frachicessname);
        AddressLine1 = (TextView) view.findViewById(R.id.AddressLine1);
        btnUpdateAddress = view.findViewById(R.id.btnUpdateAddress);
        layoutUpdateAddress = view.findViewById(R.id.layoutUpdateAddress);

        //Visibility of Address Edition
        setAddressVisibility();

        VTC = (TextView) view.findViewById(R.id.VTC);
        District = (TextView) view.findViewById(R.id.District);
        State = (TextView) view.findViewById(R.id.State);
        Pin = (TextView) view.findViewById(R.id.Pincode);
        MobileNumber = (TextView) view.findViewById(R.id.MobileNumber);
        profilepic = (ImageView) view.findViewById(R.id.profilepic);
        ScheduleVisit = (TextView) view.findViewById(R.id.ScheduleVisit);
        StatusHistory = (TextView) view.findViewById(R.id.StatusHistory);
        layoutRandomChanges = view.findViewById(R.id.layoutRandomChanges);
        txtConsentTitle = view.findViewById(R.id.txtConsentTitle);

        linearLocationStatus = (LinearLayout) view.findViewById(R.id.linearLocationStatus);
        layoutReadinessVerification = view.findViewById(R.id.layoutReadinessVerification);
        layoutCommencementDate = view.findViewById(R.id.layoutCommencementDate);
        txtCommencementDateLbl = view.findViewById(R.id.txtCommencementDateLbl);
        txtCommencementDate = view.findViewById(R.id.txtCommencementDate);
        spinnerWorkCommencement = view.findViewById(R.id.spinnerWorkCommencement);
        layoutSubmitReadinessVerification = view.findViewById(R.id.layoutSubmitReadinessVerification);
        btnReadinessVerification = view.findViewById(R.id.btnReadinessVerification);

        btnReadinessVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(franchiseeDetails.getReadinessWorkCommencementDate())) {
                    txtCommencementDate.setError("Please select Commencement date.");
                    return;
                }
                saveAddressAndProfileData(franchiseeDetails, Constants.SITE_READINESS_PROFILE_TYPE);
            }
        });

        //Location not identified
        layoutConsentNotAgreeRemarks = view.findViewById(R.id.layoutConsentNotAgreeRemarks);
        layoutLocNotIdentifiedAndConsentNotAgreeSubmitButton = view.findViewById(R.id.layoutLocNotIdentifiedAndConsentNotAgree);
        btn_submit_loc_not_identified_and_consent_disagree = view.findViewById(R.id.btn_submit_loc_not_identified_and_consent_disagree);
        layoutLocationNotIdentified = view.findViewById(R.id.layoutLocationNotIdentified);
        textViewExpectedIdentificationDate = view.findViewById(R.id.textViewExpectedIdentificationDate);
        editTextLocationNotIdentifiedRemarks = view.findViewById(R.id.editTextLocationNotIdentifiedRemarks);
        editTextLocationNotIdentifiedRemarks.setFilters(new InputFilter[]{filter});
        btn_submit_loc_not_identified_and_consent_disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updatePofileData();
            }
        });

        layoutScheduleVisit = (LinearLayout) view.findViewById(R.id.layoutScheduleVisit);
        layoutStatus = (LinearLayout) view.findViewById(R.id.layoutStatus);
        layoutVerified = view.findViewById(R.id.layoutVerified);
        checkBoxProfileVerify = view.findViewById(R.id.checkBoxProfileVerify);

        //Other Visit Details
        layoutOtherVisitDetails = view.findViewById(R.id.layoutOtherVisitDetails);
        layoutGSTDetail = view.findViewById(R.id.layoutGSTDetail);
        layoutGSTDetailData = view.findViewById(R.id.layoutGSTDetailData);
        layoutLogisticsPaymentMade = view.findViewById(R.id.layoutLogisticsPaymentMade);
        layoutLogisticsPaymentDate = view.findViewById(R.id.layoutLogisticsPaymentDate);


        radioGroupWelcomeMail = view.findViewById(R.id.radioGroupWelcomeMail);
        radioGroupCallReceived = view.findViewById(R.id.radioGroupCallReceived);
        radioGroupLogisticsMode = view.findViewById(R.id.radioGroupLogisticsMode);
        radioGroupConsent = view.findViewById(R.id.radioGroupConsent);
        txtConsentText = view.findViewById(R.id.txtConsentText);
        radioGroupGstRegistered = view.findViewById(R.id.radioGroupGstRegistered);
        editTextGSTNumber = view.findViewById(R.id.editTextGstNumber);
        editTextGSTAddress = view.findViewById(R.id.editTextGSTAddress);
        //GST Address Validaton  - No special Character allow
        editTextGSTAddress.setFilters(new InputFilter[]{filter});

        txtGSTNote = view.findViewById(R.id.txtGSTNote);
        imgGSTImage = view.findViewById(R.id.imgGSTImage);
        layoutLogisticsPaymentDate.setVisibility(View.GONE);

        radioButtonLogisticsYes = (RadioButton) view.findViewById(R.id.radioButtonLogisticsYes);
        radioButtonLogisticsNo = (RadioButton) view.findViewById(R.id.radioButtonLogisticsNo);
        textViewStartDate = view.findViewById(R.id.textViewStartDate);
        logisticpaymentdate = view.findViewById(R.id.logisticpaymentdate);

        scrollViewNextGenProfile = (ScrollView) view.findViewById(R.id.scrollViewNextGenProfile);
        linearLayoutProfileContent = (LinearLayout) view.findViewById(R.id.linearLayoutProfileContent);

        // Tooltip
        tooltipLogisticsPayment = (TextView) view.findViewById(R.id.tooltipLogisticspayment);
        tooltipsGSTINDetails = view.findViewById(R.id.tooltipGSTInDeatils);
        tooltipCommunicationConfirmation = view.findViewById(R.id.tooltipCommunicationConfirmation);
        tooltipoConsentForVakrnagge = view.findViewById(R.id.tooltipoConsentForVakrnagge);

        layoutConsentNotAgree = view.findViewById(R.id.layoutConsentNotAgree);
        checkBox_consent_text1 = view.findViewById(R.id.checkBox_consent_text1);
        checkBox_consent_text2 = view.findViewById(R.id.checkBox_consent_text2);
        checkBox_consent_text3 = view.findViewById(R.id.checkBox_consent_text3);
        checkBox_consent_text4 = view.findViewById(R.id.checkBox_consent_text4);
        editTextConsentNotAgree = view.findViewById(R.id.editTextConsentNotAgree);
        editTextConsentNotAgree.setFilters(new InputFilter[]{filter});
        tooltipLogisticsPayment.setTypeface(font);
        tooltipsGSTINDetails.setTypeface(font);
        tooltipCommunicationConfirmation.setTypeface(font);
        tooltipoConsentForVakrnagge.setTypeface(font);

        tooltipLogisticsPayment.setText(new SpannableStringBuilder(" " + new String(new char[]{0xf06a}) + " "));
        tooltipsGSTINDetails.setText(new SpannableStringBuilder(" " + new String(new char[]{0xf06a}) + " "));
        tooltipCommunicationConfirmation.setText(new SpannableStringBuilder(" " + new String(new char[]{0xf06a}) + " "));
        tooltipoConsentForVakrnagge.setText(new SpannableStringBuilder(" " + new String(new char[]{0xf06a}) + " "));
        //Other Sketch
            if (!TextUtils.isEmpty(franchiseeDetails.getGstImage())) {
                bitmap = CommonUtils.StringToBitMap(franchiseeDetails.getGstImage());
                if (bitmap != null)
                    imgGSTImage.setImageBitmap(bitmap);
            }

        imgGSTImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(franchiseeDetails.getGstImage())) {
                    startCamera(view);

                } else {
                    bitmap = CommonUtils.StringToBitMap(franchiseeDetails.getGstImage());
                    ImageDto imageDto = new ImageDto();
                    imageDto.setBitmap(bitmap);
                    imageDto.setName("GST Image");
                    imageDto.setImageBase64(franchiseeDetails.getGstImage());
                    showImagePreviewDialog(imageDto);
                }
            }
        });

            NextGenFranchiseeApplicationNo.setText(franchiseeDetails.getNextGenFranchiseeApplicationNo() == null ? "UNKNOWN" : franchiseeDetails.getNextGenFranchiseeApplicationNo());

/*
            VKID.setText(SharedPrefUtils.getInstance(getActivity()).getVKId());
*/
            FranchiseeName.setText(franchiseeDetails.getFranchiseeName());
            AddressLine1.setText(franchiseeDetails.getVAddress());
            VTC.setText(franchiseeDetails.getVTC());
            District.setText(franchiseeDetails.getDistrict());
            State.setText(franchiseeDetails.getState());
            Pin.setText(franchiseeDetails.getPincode());
            MobileNumber.setText(franchiseeDetails.getMobileNumber());

            if (franchiseeDetails.getFranchiseePicFile() != null) {
                Bitmap img = StringToBitMap(franchiseeDetails.getFranchiseePicFile());
                profilepic.setImageBitmap(img);
            }
        

        User[] users = new User[2];
        users[0] = new User();
        users[0].setId(1);
        users[0].setName("Identified");
        users[1] = new User();
        users[1].setId(2);
        users[1].setName("Not Identified");

        AddressLine1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAdressPreviewDialog();

            }
        });
        MobileNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (franchiseeDetails == null || TextUtils.isEmpty(franchiseeDetails.getMobileNumber()))
                    return;

                //Open call function
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + franchiseeDetails.getMobileNumber()));
                startActivity(intent);
            }
        });

        ScheduleVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showScheduleVisitDateTimeDialogPicker();
            }
        });

        try {
            franchiseeTimeLineDetailsList = franchiseeDetails.getTimeLineList();
            String desc = franchiseeDetails.getStatusDesc();
            if (!TextUtils.isEmpty(desc))
                StatusHistory.setText(desc);
            else {
                //TODO: Need to set Status Based on Type - Site Visit, Commencement or WIP.
                if (isSiteCommencement) {
                    StatusHistory.setText(R.string.nextgen_commencement_default_current_status);
                } else if (isSiteWorkInProgress) {
                    StatusHistory.setText(R.string.nextgen_site_no_status_available);
                } else if (isSiteWorkCompletion) {
                    StatusHistory.setText(R.string.nextgen_site_no_status_available);
                } else if (isSiteReadiness) {
                    StatusHistory.setText(R.string.nextgen_site_no_status_available);
                } else {
                    StatusHistory.setText(R.string.nextgen_visit_default_current_status);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        StatusHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStatusHistoryDialog();
            }
        });


        final ArrayAdapter userAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, users);

        mySpinner = (Spinner) view.findViewById(R.id.location_status);
        mySpinner.setAdapter(userAdapter);
        if (franchiseeDetails.getLocationStatus() != null) {
            if (franchiseeDetails.getLocationStatus().equals("0")) {
                mySpinner.setSelection(1, false);
                layoutOtherVisitDetails.setVisibility(View.GONE);
                layoutLocationNotIdentified.setVisibility(View.VISIBLE);
                handleUpdateAdressOnLocationIdentied(false);
            } else {
                mySpinner.setSelection(0, false);
                layoutOtherVisitDetails.setVisibility(View.VISIBLE);
                layoutLocationNotIdentified.setVisibility(View.GONE);
                handleUpdateAdressOnLocationIdentied(true);
            }
        }
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equalsIgnoreCase("Not Identified")) {
                    franchiseeDetails.setLocationStatus("0");
                    handleUpdateAdressOnLocationIdentied(false);

                    if (getActivity().getClass().getSimpleName().equalsIgnoreCase("NextGenPhotoViewPager")) {
                        ((NextGenPhotoViewPager) getActivity()).setFranchiseeDetails(franchiseeDetails);
                        layoutLocationNotIdentified.setVisibility(View.VISIBLE);
                        layoutOtherVisitDetails.setVisibility(View.GONE);
                        setLocationNotIdentifiedAndConsentNotAgreeSubmitVisibility();

                    } else if (getActivity().getClass().getSimpleName().equalsIgnoreCase("SiteReadinessActivity")) {
                        ((SiteReadinessActivity) getActivity()).setFranchiseeDetails(franchiseeDetails);

                    }else if (getActivity().getClass().getSimpleName().equalsIgnoreCase("MandatoryBrandingActivity")) {
                        ((MandatoryBrandingActivity) getActivity()).setFranchiseeDetails(franchiseeDetails);

                    } else {
                        ((NextGenSiteCommencementActivity) getActivity()).setFranchiseeDetails(franchiseeDetails);
                    }

                    Log.d(TAG, "From Spinner: ");
                } else {
                    franchiseeDetails.setLocationStatus("1");
                    handleUpdateAdressOnLocationIdentied(true);

                    if (getActivity().getClass().getSimpleName().equalsIgnoreCase("NextGenPhotoViewPager")) {
                        ((NextGenPhotoViewPager) getActivity()).setFranchiseeDetails(franchiseeDetails);
                        layoutOtherVisitDetails.setVisibility(View.VISIBLE);
                        layoutLocationNotIdentified.setVisibility(View.GONE);
                        setLocationNotIdentifiedAndConsentNotAgreeSubmitVisibility();

                    } else if (getActivity().getClass().getSimpleName().equalsIgnoreCase("SiteReadinessActivity")) {
                        ((SiteReadinessActivity) getActivity()).setFranchiseeDetails(franchiseeDetails);

                    } else if (getActivity().getClass().getSimpleName().equalsIgnoreCase("MandatoryBrandingActivity")) {
                        ((MandatoryBrandingActivity) getActivity()).setFranchiseeDetails(franchiseeDetails);

                    }else {
                        ((NextGenSiteCommencementActivity) getActivity()).setFranchiseeDetails(franchiseeDetails);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do Nothing
            }
        });

        //Commencement
        if (getActivity().getClass().getSimpleName().equalsIgnoreCase("NextGenSiteCommencementActivity")) {
            mySpinner.setEnabled(false);
        }

        // Hide Controls for NextGen Site Readiness and NextGen Work In Progress
        hideControls();

        if (IsSiteVisit) {
            reloadOtherDetailsSepcificToVisit();
        } else {
            layoutOtherVisitDetails.setVisibility(View.GONE);
        }

        //Check status and remark
        checkStatusandRemarks();

        //Reload Location Not Identification
        reloadLocationNotIdentifiedAndConsentNotAgreeData();

        //Reload Readiness/Verfication data
        reloadReadinessVerification();

        //Dismiss Spinner
        dismissProgressSpinner();

        //Hide Communication, Logistic Payment and GST Details layout       ---Added on 11th Oct, 2018
        boolean IsToBeRemoved = false;
        if (getActivity().getClass().getSimpleName().equalsIgnoreCase("NextGenPhotoViewPager")) {
            IsToBeRemoved = Constants.IsToBeRemoved;
        }

        if (IsSiteVisit && IsToBeRemoved) {
            checkBox_consent_text4.setVisibility(View.GONE);
            layoutRandomChanges.setVisibility(View.GONE);
        } else {
            checkBox_consent_text4.setVisibility(View.VISIBLE);
            layoutRandomChanges.setVisibility(View.VISIBLE);
        }

        //hardware delivery address
        textDeliveryAddress = view.findViewById(R.id.textDeliveryAddress);
        layoutDeliveryAddressEdit = view.findViewById(R.id.layoutDeliveryAddressEdit);

        if (TextUtils.isEmpty(franchiseeDetails.getHardwareDeliveryAddress())) {
            textDeliveryAddress.setText("No address details found.");
        } else {
            textDeliveryAddress.setText(franchiseeDetails.getHardwareDeliveryAddress());
        }

        layoutDeliveryAddressEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Do Nothing
            }
        });

        //Update Kendra Address Details
        updateKendraAddressDetails();
        return view;
    }

    //region Custom Address Preview Dialog
    private void showAdressPreviewDialog() {
        if (customAddressPreviewDialog != null && customAddressPreviewDialog.isShowing()) {
            return;
        }

        customAddressPreviewDialog = new CustomAddressPreviewDialog(getContext(), franchiseeDetails, new CustomAddressPreviewDialog.AddressPreviewDialogClicks() {
            @Override
            public void capturePhotoClick() {
                if (TextUtils.isEmpty(franchiseeDetails.getAddressProofImage())) {
                    startCamera(view);

                } else {
                    bitmap = CommonUtils.StringToBitMap(franchiseeDetails.getAddressProofImage());
                    ImageDto imageDto = new ImageDto();
                    imageDto.setBitmap(bitmap);
                    imageDto.setName("Address Proof");
                    imageDto.setImageBase64(franchiseeDetails.getAddressProofImage());
                    showImagePreviewDialog((Object) imageDto);
                }

            }

            @Override
            public void OkClick(FranchiseeDetails franchiseeDetails) {
                saveAddressAndProfileData(franchiseeDetails, Constants.SITE_READINESS_ADDRESS_TYPE);

            }
        });
        customAddressPreviewDialog.show();
        customAddressPreviewDialog.setCancelable(false);


    }
    //endregion

    public void saveAddressAndProfileData(final FranchiseeDetails franchiseeDetails, final String type) {

        asyncSaveSiteReadinessVerification = new AsyncSaveSiteReadinessVerification(getContext(),
                null, franchiseeDetails, type, new AsyncSaveSiteReadinessVerification.Callback() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResult(String result) {
                try {
                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(getContext(), getContext().getResources().getString(R.string.Warning));
                        return;
                    }

                    Log.e(TAG, "Result : " + result);
                    if (result.startsWith("OKAY")) {
                        if (type.equalsIgnoreCase(Constants.SITE_READINESS_ADDRESS_TYPE))
                            AlertDialogBoxInfo.alertDialogShow(getContext(), getContext().getResources().getString(R.string.address_update));
                        else
                            AlertDialogBoxInfo.alertDialogShow(getContext(), getContext().getResources().getString(R.string.profile_update));
                        String vkIdTemp = connection.getVkid();
                        Utils.reloadReadinessVerificationData(Constants.NEXTGEN_SITE_READINESS_AND_VERIFICATION, getContext(), vkIdTemp, franchiseeDetails.getNextGenFranchiseeApplicationId());
                        ((SiteReadinessActivity) getActivity()).selectFragment(0);
                    } else {
                        AlertDialogBoxInfo.showOkDialog(getContext(), getContext().getResources().getString(R.string.alert_msg_readiness_verification_fail));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(getContext(), getContext().getResources().getString(R.string.Warning));
                }
            }
        });
        asyncSaveSiteReadinessVerification.execute();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        showProgressSpinner(getContext());
    }

    public void IsReviewed(boolean IsReviewed) {
        franchiseeDetails.setNeedToBeReviewed(IsReviewed);
        if (franchiseeDetails.isNeedToBeReviewed())
            layoutVerified.setVisibility(View.VISIBLE);
        else
            layoutVerified.setVisibility(View.GONE);

        checkBoxProfileVerify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                franchiseeDetails.setProfileReviewed(b);
                ((NextGenPhotoViewPager) getActivity()).setFranchiseeDetails(franchiseeDetails);

            }
        });
    }

    //region Check Status and Remarks and Perform Action
    private void checkStatusandRemarks() {
        //check allow to update profile or not
        if (!franchiseeDetails.isAllowToEdit()) {
            GUIUtils.setViewAndChildrenEnabled(linearLayoutProfileContent, false);
        }
        if (franchiseeDetails.getStatus() == NextGenViewPager.SITE_SEND_BACK_FOR_CORRECTION) {
            FranchiseeRemarkDetails franchiseeRemarkDetails = franchiseeDetails.getFranchiseeRemarkDetails();
            if (franchiseeRemarkDetails != null) {
                //Logistic payment
                final String logisticPaymentDate = franchiseeRemarkDetails.getLogisticsPaymentStatus();
                if (!TextUtils.isEmpty(logisticPaymentDate)) {
                    tooltipLogisticsPayment.setVisibility(View.VISIBLE);
                    tooltipLogisticsPayment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tooltipLogisticsPayment.setAnimation(btnClickAnim);
                            showToolTip(v, logisticPaymentDate);
                        }
                    });
                }

                //GTSIN Deatils
                final String GSTINDeatails = franchiseeRemarkDetails.getGSTINDetail();
                if (!TextUtils.isEmpty(GSTINDeatails)) {
                    tooltipsGSTINDetails.setVisibility(View.VISIBLE);
                    tooltipsGSTINDetails.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tooltipsGSTINDetails.setAnimation(btnClickAnim);
                            showToolTip(v, GSTINDeatails);
                        }
                    });
                }

                //Communication Confirmation Details
                final String Communication_Confirmation = franchiseeRemarkDetails.getCommunicationStatus();
                if (!TextUtils.isEmpty(Communication_Confirmation)) {
                    tooltipCommunicationConfirmation.setVisibility(View.VISIBLE);
                    tooltipCommunicationConfirmation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            tooltipCommunicationConfirmation.setAnimation(btnClickAnim);
                            showToolTip(view, Communication_Confirmation);
                        }
                    });
                }

                final String ConsentForVakrnagee = franchiseeRemarkDetails.getConsentStatus();
                if (!TextUtils.isEmpty(ConsentForVakrnagee)) {
                    tooltipoConsentForVakrnagge.setVisibility(View.VISIBLE);
                    tooltipoConsentForVakrnagge.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            tooltipoConsentForVakrnagge.setAnimation(btnClickAnim);
                            showToolTip(view, ConsentForVakrnagee);
                        }
                    });
                }
            }
        }
    }
    //endregion

    private void showToolTip(View v, String logisticPaymentDate) {
        PopupUtils.show(getActivity(), v, logisticPaymentDate);
    }

    private void showImagePreviewDialog(Object object) {

        if (customImagePreviewDialog != null && customImagePreviewDialog.isShowing()) {
            customImagePreviewDialog.refresh(object);
            return;
        }

        if (object != null) {
            customImagePreviewDialog = new CustomImagePreviewDialog(getContext(), object, new CustomImagePreviewDialog.IImagePreviewDialogClicks() {
                @Override
                public void capturePhotoClick() {
                    startCamera(view);
                }

                @Override
                public void OkClick(Object object) {
                    ImageDto imageDto = ((ImageDto) object);
                    if (getActivity().getClass().getSimpleName().equalsIgnoreCase("NextGenPhotoViewPager")) {
                        franchiseeDetails.setGstImage(imageDto.getImageBase64());
                        ((NextGenPhotoViewPager) getActivity()).setFranchiseeDetails(franchiseeDetails);
                        imgGSTImage.setImageBitmap(imageDto.getBitmap());
                    } else if (getActivity().getClass().getSimpleName().equalsIgnoreCase("SiteReadinessActivity")) {
                        franchiseeDetails.setAddressProofImage(imageDto.getImageBase64());
                        ((SiteReadinessActivity) getActivity()).setFranchiseeDetails(franchiseeDetails);
                        customAddressPreviewDialog.RefreshDialog();

                    } else {
                        ((NextGenSiteCommencementActivity) getActivity()).setFranchiseeDetails(franchiseeDetails);
                    }

                }
            });
            customImagePreviewDialog.show();
            customImagePreviewDialog.setCancelable(false);
            customImagePreviewDialog.allowChangePhoto(true);
            customImagePreviewDialog.allowRemarks(false);

        } else {
            Toast.makeText(getActivity(), "No photo available to preview.", Toast.LENGTH_SHORT).show();
        }
    }

    public void startCamera(View view) {
        //If the app has not the permission then asking for the permission
        permissionHandler.requestPermission(view, Manifest.permission.CAMERA, getString(R.string.needs_permission_camera_msg), new IPermission() {
            @Override
            public void IsPermissionGranted(boolean IsGranted) {
                if (IsGranted) {
                    File file = CommonUtils.getOutputMediaFile(CommonUtils.FILE_IMAGE_TYPE);
                    Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    picUri = Uri.fromFile(file); // create
                    i.putExtra(MediaStore.EXTRA_OUTPUT, picUri); // set the image file
                    i.putExtra("ImageId", picUri); // set the image file
                    startActivityForResult(i, CAMERA_PIC_REQUEST);
                }
            }
        });
    }

    private void showDateTimeDialogPicker() {

        Date defaultDate = null;
        if (selectedDateTimeId == R.id.textViewStartDate) {
            defaultDate = startDate;
        } else if (selectedDateTimeId == R.id.textViewExpectedIdentificationDate) {
            defaultDate = expectedIdentificationDate;
        } else if (selectedDateTimeId == R.id.txtCommencementDate) {
            defaultDate = commencementDate;
        }

        // Show DateTime Picker Dialog.
        dateTimePickerDialog = new DateTimePickerDialog(getActivity(), true, defaultDate, new DateTimePickerDialog.IDateTimePicker() {
            @Override
            public void getDateTime(Date datetime, String defaultFormattedDateTime) {
                try {
                    String formatedDate = dateFormatter2.format(datetime);
                    Toast.makeText(getActivity(), "Selected DateTime : " + formatedDate, Toast.LENGTH_LONG).show();

                    if (selectedDateTimeId != 0) {
                        TextView textViewDateTime = (TextView) view.findViewById(selectedDateTimeId);
                        textViewDateTime.setText(formatedDate);

                        if (selectedDateTimeId == R.id.textViewStartDate) {
                            startDate = datetime;
                            strStartDate = formatedDate;
                            franchiseeDetails.setLogisticsPaymentDate(defaultFormattedDateTime);

                        } else if (selectedDateTimeId == R.id.textViewExpectedIdentificationDate) {
                            expectedIdentificationDate = datetime;
                            strExpectedIdentificationDate = formatedDate;
                            franchiseeDetails.setExpectedDateOfSiteIdentification(defaultFormattedDateTime);

                        } else if (selectedDateTimeId == R.id.txtCommencementDate) {
                            commencementDate = datetime;
                            strCommencementDate = formatedDate;
                            franchiseeDetails.setReadinessWorkCommencementDate(defaultFormattedDateTime);

                            ((SiteReadinessActivity) getActivity()).setFranchiseeDetails(franchiseeDetails);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        dateTimePickerDialog.setCancelable(false);
        dateTimePickerDialog.setActionButtonName("Save");

        // Feb 2018  Days Back To allow.
        if (selectedDateTimeId == R.id.textViewStartDate) {

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, 2018);
            cal.set(Calendar.MONTH, 1);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            dateTimePickerDialog.setMinDate(cal.getTimeInMillis());

        } else if (selectedDateTimeId == R.id.textViewCompletionDate && !TextUtils.isEmpty(strStartDate)) {
            dateTimePickerDialog.setMinDate(startDate.getTime());

        } else if (selectedDateTimeId == R.id.textViewExpectedIdentificationDate) {
            dateTimePickerDialog.setMinDate(new Date().getTime());
        } else if (selectedDateTimeId == R.id.txtCommencementDate) {
            try {
                if (franchiseeDetails.getReadinessWorkCommencementStatus().equalsIgnoreCase("1")) {
                    Date minDate = dateFormatter1.parse(franchiseeDetails.getReadinessWorkCommencementMinDate());
                    dateTimePickerDialog.setMinDate(minDate.getTime());

                } else {
                    dateTimePickerDialog.setMinDate(new Date().getTime());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        dateTimePickerDialog.show();

    }

    public class User {
        @Override
        public String toString() {
            return getName();
        }

        private int _id;
        private String _name;

        public User() {
            this._id = 0;
            this._name = "";
        }

        public void setId(int id) {
            this._id = id;
        }

        public int getId() {
            return this._id;
        }

        public void setName(String name) {
            this._name = name;
        }

        public String getName() {
            return this._name;
        }
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public void reloadOtherDetailsSepcificToVisit() {

        if (franchiseeDetails.getLocationStatus() != null) {
            if (franchiseeDetails.getLocationStatus().equals("0")) {
                mySpinner.setSelection(1, false);
                layoutOtherVisitDetails.setVisibility(View.GONE);
                layoutLocationNotIdentified.setVisibility(View.VISIBLE);
            } else {
                mySpinner.setSelection(0, false);
                layoutOtherVisitDetails.setVisibility(View.VISIBLE);
                layoutLocationNotIdentified.setVisibility(View.GONE);
            }
        }

        //Consent Text
        String str = "";
        str += ("\t  \u2022 \t" + getContext().getResources().getString(R.string.consent_text1) + "\n");
        str += ("\t  \u2022 \t" + getContext().getResources().getString(R.string.consent_text2) + "\n");
        str += ("\t  \u2022 \t" + getContext().getResources().getString(R.string.consent_text3) + "\n");
        txtConsentText.setText(str);

        //GST Note
        txtGSTNote.setText(Html.fromHtml("<b>Note :-</b> &nbsp; " + getContext().getResources().getString(R.string.gst_note)));

        //Welcome Mail
        radioGroupWelcomeMail.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonWelcomeYes) {
                    franchiseeDetails.setWelcomeMailStatus("1");
                    ((NextGenPhotoViewPager) getActivity()).setFranchiseeDetails(franchiseeDetails);
                } else if (checkedId == R.id.radioButtonWelcomeNo) {
                    franchiseeDetails.setWelcomeMailStatus("0");
                    ((NextGenPhotoViewPager) getActivity()).setFranchiseeDetails(franchiseeDetails);
                }
            }
        });
        if (franchiseeDetails.getWelcomeMailStatus() != null) {
            if (franchiseeDetails.getWelcomeMailStatus().equals("1")) {
                radioGroupWelcomeMail.check(R.id.radioButtonWelcomeYes);

            } else if (franchiseeDetails.getWelcomeMailStatus().equals("0")) {
                radioGroupWelcomeMail.check(R.id.radioButtonWelcomeNo);
            }
        }

        //Call Received
        radioGroupCallReceived.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonCallYes) {
                    franchiseeDetails.setCallReceivedStatus("1");
                    ((NextGenPhotoViewPager) getActivity()).setFranchiseeDetails(franchiseeDetails);
                } else if (checkedId == R.id.radioButtonCallNo) {
                    franchiseeDetails.setCallReceivedStatus("0");
                    ((NextGenPhotoViewPager) getActivity()).setFranchiseeDetails(franchiseeDetails);
                }
            }
        });
        if (franchiseeDetails.getCallReceivedStatus() != null) {
            if (franchiseeDetails.getCallReceivedStatus().equals("1")) {
                radioGroupCallReceived.check(R.id.radioButtonCallYes);

            } else if (franchiseeDetails.getCallReceivedStatus().equals("0")) {
                radioGroupCallReceived.check(R.id.radioButtonCallNo);
            }
        }

        //Logistics Mode
        radioGroupLogisticsMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonLogisticsYes) {
                    franchiseeDetails.setLogisticsPaymentStatus("1");
                    startDate = null;
                    strStartDate = null;
                    logisticpaymentdate.setText("Payment Made On");
                    layoutLogisticsPaymentDate.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.radioButtonLogisticsNo) {
                    startDate = null;
                    strStartDate = null;
                    franchiseeDetails.setLogisticsPaymentStatus("0");
                    logisticpaymentdate.setText("Expected Payment Date");
                    layoutLogisticsPaymentDate.setVisibility(View.VISIBLE);
                }
            }
        });

        if (franchiseeDetails.getLogisticsPaymentStatus() != null) {
            if (franchiseeDetails.getLogisticsPaymentStatus().equals("1")) {
                RadioButton radioButton = radioGroupLogisticsMode.findViewById(R.id.radioButtonLogisticsYes);
                radioButton.setChecked(true);
                layoutLogisticsPaymentDate.setVisibility(View.VISIBLE);
                logisticpaymentdate.setText("Payment Made On");
            } else if (franchiseeDetails.getLogisticsPaymentStatus().equals("0")) {
                RadioButton radioButton = radioGroupLogisticsMode.findViewById(R.id.radioButtonLogisticsNo);
                radioButton.setChecked(true);
                layoutLogisticsPaymentDate.setVisibility(View.VISIBLE);
                logisticpaymentdate.setText("Expected Payment Date");

            }
        }

        textViewStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDateTimeId = v.getId();
                showDateTimeDialogPicker();
            }
        });

        //Consent
        radioGroupConsent.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonConsentYes) {
                    txtConsentTitle.setText(getContext().getResources().getString(R.string.consent_header));
                    franchiseeDetails.setConsentStatus("1");
                    ((NextGenPhotoViewPager) getActivity()).setFranchiseeDetails(franchiseeDetails);
                    layoutLogisticsPaymentMade.setVisibility(View.VISIBLE);
                    layoutGSTDetail.setVisibility(View.VISIBLE);
                    layoutConsentNotAgree.setVisibility(View.GONE);
                    txtConsentText.setVisibility(View.VISIBLE);
                    setLocationNotIdentifiedAndConsentNotAgreeSubmitVisibility();

                    //when user Yes COnsent select - Nilesh
                    if (franchiseeDetails.getLogisticsPaymentStatus() != null) {
                        if (franchiseeDetails.getLogisticsPaymentStatus().equals("1")) {
                            radioGroupLogisticsMode.check(R.id.radioButtonLogisticsYes);
                            layoutLogisticsPaymentDate.setVisibility(View.VISIBLE);
                        } else if (franchiseeDetails.getLogisticsPaymentStatus().equals("0")) {
                            radioGroupLogisticsMode.check(R.id.radioButtonLogisticsNo);
                            layoutLogisticsPaymentDate.setVisibility(View.VISIBLE);

                        }
                    }
                } else if (checkedId == R.id.radioButtonConsentNo) {
                    txtConsentTitle.setText("Reason to Exit Vakrangee Business");
                    franchiseeDetails.setConsentStatus("0");
                    ((NextGenPhotoViewPager) getActivity()).setFranchiseeDetails(franchiseeDetails);
                    layoutLogisticsPaymentMade.setVisibility(View.GONE);
                    layoutLogisticsPaymentDate.setVisibility(View.GONE);
                    layoutGSTDetail.setVisibility(View.GONE);
                    layoutConsentNotAgree.setVisibility(View.VISIBLE);
                    txtConsentText.setVisibility(View.GONE);
                    setLocationNotIdentifiedAndConsentNotAgreeSubmitVisibility();
                    Log.d(TAG, "From radioGroupConsent: ");
                }
            }
        });
        if (franchiseeDetails.getConsentStatus() != null) {
            if (franchiseeDetails.getConsentStatus().equals("1")) {
                //radioGroupConsent.check(R.id.radioButtonConsentYes);
                RadioButton radioButton = radioGroupConsent.findViewById(R.id.radioButtonConsentYes);
                radioButton.setChecked(true);
            } else if (franchiseeDetails.getConsentStatus().equals("0")) {
                RadioButton radioButton = radioGroupConsent.findViewById(R.id.radioButtonConsentNo);
                radioButton.setChecked(true);
                //radioGroupConsent.check(R.id.radioButtonConsentNo);
            }
        } else {
            layoutConsentNotAgree.setVisibility(View.GONE);
            txtConsentText.setVisibility(View.VISIBLE);
        }

        try {

            // Set Start and Estimated End Date
            strStartDate = franchiseeDetails.getLogisticsPaymentDate();
            if (!TextUtils.isEmpty(strStartDate)) {
                startDate = dateFormatter1.parse(strStartDate);
                String tempStartDate = CommonUtils.getFormattedDate(YYYY_MM_DD_CONST, DD_MM_YYYY_CONST, strStartDate);
                textViewStartDate.setText(tempStartDate);
            }
        } catch (
                Exception pe) {
            pe.getMessage();
        }

        //GST Details
        radioGroupGstRegistered.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonGstYes) {
                    franchiseeDetails.setGstRegisteredStatus("1");
                    ((NextGenPhotoViewPager) getActivity()).setFranchiseeDetails(franchiseeDetails);
                    layoutGSTDetailData.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.radioButtonGstNo) {
                    franchiseeDetails.setGstRegisteredStatus("0");
                    ((NextGenPhotoViewPager) getActivity()).setFranchiseeDetails(franchiseeDetails);
                    layoutGSTDetailData.setVisibility(View.GONE);
                }
            }
        });
        if (franchiseeDetails.getGstRegisteredStatus() != null) {
            if (franchiseeDetails.getGstRegisteredStatus().equals("1")) {
                radioGroupGstRegistered.check(R.id.radioButtonGstYes);

            } else if (franchiseeDetails.getGstRegisteredStatus().equals("0")) {
                radioGroupGstRegistered.check(R.id.radioButtonGstNo);
            }
        }

        if (!TextUtils.isEmpty(franchiseeDetails.getGstNumber()) || !TextUtils.isEmpty(franchiseeDetails.getGstAddress())) {
            radioGroupGstRegistered.check(R.id.radioButtonGstYes);
        }

        //GST Number
        editTextGSTNumber.setText(franchiseeDetails.getGstNumber());
       /* editTextGSTNumber.addTextChangedListener(new EditTextWatcher(new EditTextWatcher.IEditextData() {
            @Override
            public void getEditTextData(String data) {
                franchiseeDetails.setGstNumber(data);
                ((NextGenPhotoViewPager) getActivity()).setFranchiseeDetails(franchiseeDetails);
            }
        }));*/

        editTextGSTNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //DO Nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!CommonUtils.GSTINisValid(String.valueOf(s))) {
                    editTextGSTNumber.setTextColor(Color.parseColor("#FF0000"));
                    editTextGSTNumber.setError(getResources().getString(R.string.hintGsTnumbernotValid));
                } else {

                    editTextGSTNumber.setTextColor(Color.parseColor("#468847"));
                    editTextGSTNumber.setError(null);
                }
                franchiseeDetails.setGstNumber(editTextGSTNumber.getText().toString());
                ((NextGenPhotoViewPager) getActivity()).setFranchiseeDetails(franchiseeDetails);

            }

            @Override
            public void afterTextChanged(Editable s) {
                //Do Nothing
            }
        });

        //GST Address
        editTextGSTAddress.setText(franchiseeDetails.getGstAddress());
        editTextGSTAddress.addTextChangedListener(new

                EditTextWatcher(new EditTextWatcher.IEditextData() {
            @Override
            public void getEditTextData(String data) {
                franchiseeDetails.setGstAddress(data);
                ((NextGenPhotoViewPager) getActivity()).setFranchiseeDetails(franchiseeDetails);
            }
        }));
    }

    //region Show Schedule Visit DateTime Picker Dialog

    private void showScheduleVisitDateTimeDialogPicker() {

        // Check Internet Connectivity
        if (internetConnection.isConnectingToInternet() == false) {
            AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.internetCheck));
            return;
        }

        // Show DateTime Picker Dialog.
        dateTimePickerDialog = new DateTimePickerDialog(getActivity(), new DateTimePickerDialog.IDateTimePicker() {
            @Override
            public void getDateTime(Date datetime, String defaultFormattedDateTime) {
                try {
                    String formatedDate = dateFormatter.format(datetime);
                    Toast.makeText(getActivity(), "Selected DateTime : " + formatedDate, Toast.LENGTH_LONG).show();

                    AsyncNextgenScheduleAndCallUpdate asyncNextgenScheduleAndCallUpdate = new AsyncNextgenScheduleAndCallUpdate(getActivity());
                    asyncNextgenScheduleAndCallUpdate.execute(franchiseeDetails.getNextGenFranchiseeApplicationId(), "1", "Schedule Visit", defaultFormattedDateTime);
                    //Format DateTime and Set to ScheduleVisit
                    ScheduleVisit.setText(formatedDate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        dateTimePickerDialog.setCancelable(false);
        dateTimePickerDialog.show();
    }
    //endregion

    //region Show Status/Remarks History
    private StatusHistoryDialog statusHistoryDialog;

    private void showStatusHistoryDialog() {
        if (franchiseeTimeLineDetailsList != null && franchiseeTimeLineDetailsList.size() > 0) {
            statusHistoryDialog = new StatusHistoryDialog(getActivity(), franchiseeTimeLineDetailsList);
            statusHistoryDialog.setCancelable(false);
            statusHistoryDialog.show();
        }
    }
//endregion

    //region update Schedule DateTime
    private class AsyncNextgenScheduleAndCallUpdate extends AsyncTask<String, Void, Void> {

        private String displayServerResopnse;
        private Context mContext;
        private ProgressDialog progress;

        private AsyncNextgenScheduleAndCallUpdate() {
        }

        public AsyncNextgenScheduleAndCallUpdate(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
            progress = new ProgressDialog(mContext);
            progress.setTitle(R.string.loading_msg_schedule);
            progress.setMessage(mContext.getResources().getString(R.string.pleaseWait));
            progress.setCancelable(false);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");
            try {
                //Get Parameters Data
                String tempNextgenFranchiseeApplicationId = params[0];
                String tempActivity = params[1];                            // activity 0 - call , 1 - schedule
                String tempRemarks = params[2];
                String tempActivityDateTime = params[3];

                // Preparing Data
                Connection connection = new Connection(getActivity());
                String vkId = EncryptionUtil.encryptString(connection.getVkid(), mContext);
                String tokenId = EncryptionUtil.encryptString(connection.getTokenId(), mContext);

                String deviceIdget = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                String deviceId = EncryptionUtil.encryptString(deviceIdget, getContext());

                String deviceIDAndroid = CommonUtils.getAndroidUniqueID(getContext());
                String IMEINo = EncryptionUtil.encryptString(deviceIDAndroid, getContext());

                String simSerial = CommonUtils.getSimSerialNumber(getContext());
                String simNo = EncryptionUtil.encryptString(simSerial, getContext());

                String nextgenFranchiseeApplicationId = EncryptionUtil.encryptString(tempNextgenFranchiseeApplicationId, mContext);
                String activity = EncryptionUtil.encryptString(tempActivity, mContext);
                String remarks = EncryptionUtil.encryptString(tempRemarks, mContext);
                String activityDateTime = EncryptionUtil.encryptString(tempActivityDateTime, mContext);

                // Sending Request
                displayServerResopnse = WebService.nextgenScheduleDateTimeUpdateResponse(vkId, tokenId, IMEINo, deviceId,
                        simNo, nextgenFranchiseeApplicationId, activity, remarks, activityDateTime);

                Log.d(TAG, "WebSer...ceResponse: " + displayServerResopnse);

            } catch (Exception e) {
                AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");

            progress.dismiss();
            try {
                if (displayServerResopnse.startsWith("OKAY")) {
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.imageuploadsuccessful));
                } else {
                    Log.e("Error in Server", displayServerResopnse);
                    Toast.makeText(getActivity(), displayServerResopnse, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                e.printStackTrace();
            }
        }
    }
//endregion

    //region Hide Controls for NextGen Site Readiness and NextGen Work In Progress
    public void hideControls() {
        if (isSiteCommencement || isSiteWorkInProgress) {
            layoutScheduleVisit.setVisibility(View.GONE);
        }

        if (isAdhoc) {
            layoutScheduleVisit.setVisibility(View.GONE);
            layoutStatus.setVisibility(View.GONE);
        }

        if (isSiteWorkCompletion || isSiteReadiness) {
            layoutScheduleVisit.setVisibility(View.GONE);
            layoutStatus.setVisibility(View.GONE);
        }

        if (isSiteCommencement || isSiteReadiness) {
            linearLocationStatus.setVisibility(View.GONE);
        }


    }
    //endregion

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK) {
            try {
                Bitmap bitmapNew = ImageUtils.getBitmap(getActivity().getContentResolver(), picUri, "", "", "");
                //BitMap with TimeStamp on it
                bitmapNew = ImageUtils.stampWithTimeInBitmap(bitmapNew);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmapNew.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                String imageBase64 = EncryptionUtil.encodeBase64(byteArrayOutputStream.toByteArray());

                ImageView imageView = new ImageView(getContext());
                imageView.setImageBitmap(bitmapNew);

                if (!CommonUtils.isLandscapePhoto(picUri, imageView)) {
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getString(R.string.landscape_mode_allowed));
                } else {
                    ImageDto imageDto = new ImageDto();
                    imageDto.setUri(picUri);
                    imageDto.setBitmap(bitmapNew);
                    imageDto.setImageBase64(imageBase64);
                    showImagePreviewDialog((Object) imageDto);
                }

            } catch (Exception e) {
                e.printStackTrace();
                AlertDialogBoxInfo.alertDialogShow(getContext(), getResources().getString(R.string.Warning));
            }
        }
        if (requestCode == 1111 && resultCode == Activity.RESULT_OK) {
            String addressDetails = data.getStringExtra("delivery_address_details");
            textDeliveryAddress.setText(TextUtils.isEmpty(addressDetails) ? "No Address Details Found" : addressDetails);
        }
    }

    //region Submit Profile Data
    private void updatePofileData() {
        updateConsentExitReason();
        if (validateConsentNotAgreeAndLocationNotIdentifiedData()) {
            asyncNextgenLocationUpdate = new AsyncNextgenProfileUpdate();
            asyncNextgenLocationUpdate.execute();
        }
    }

    private ProgressDialog progress;
    private String diplayServerResopnse;

    private class AsyncNextgenProfileUpdate extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
            progress = new ProgressDialog(getActivity());
            progress.setTitle(R.string.updateProfile);
            progress.setMessage(getResources().getString(R.string.pleaseWait));
            progress.setCancelable(false);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {
                Connection connection = new Connection(getActivity());
                String vkid = connection.getVkid();
                String tokenId = connection.getTokenId();

                String deviceIdget = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                String deviceid = EncryptionUtil.encryptString(deviceIdget, getContext());

                String deviceIDAndroid = CommonUtils.getAndroidUniqueID(getContext());
                String imei = EncryptionUtil.encryptString(deviceIDAndroid, getContext());

                String simSerial = CommonUtils.getSimSerialNumber(getContext());
                String simserialnumber = EncryptionUtil.encryptString(simSerial, getContext());

                String vkidd = EncryptionUtil.encryptString(vkid, getActivity());
                String TokenId = EncryptionUtil.encryptString(tokenId, getActivity());
                String type = EncryptionUtil.encryptString("3", getActivity());
                String jsonData = JSONUtils.toString(franchiseeDetails);
                String data = jsonData;

                if (Constants.ENABLE_FRANCHISEE_LOGIN) {
                    if (!TextUtils.isEmpty(vkid)) {
                        diplayServerResopnse = WebService.myVakrangeeKendraFranchiseeDetailsNextgenUpdate(vkid, "4", data);
                    } else {
                        String enquiryId = CommonUtils.getEnquiryId(getContext());
                        diplayServerResopnse = WebService.myVakrangeeKendraFranchiseeDetailsNextgenUpdate2(enquiryId, "4", data);
                    }
                } else {
                    if (isAdhoc) {
                        diplayServerResopnse = WebService.myVakrangeeKendraFranchiseeDetailsNextgenUpdate(vkid, "4", data);
                    } else {
                        diplayServerResopnse = WebService.myVakrangeeKendraFranchiseeDetailsNextgenUpdate(
                                vkidd, TokenId, imei, deviceid, simserialnumber, type, data);
                    }
                }
                Log.d(TAG, "WebSer...ceResponse: " + diplayServerResopnse);

            } catch (Exception e) {
                AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");

            progress.dismiss();
            try {
                if (diplayServerResopnse.startsWith("OKAY|")) {
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.imageuploadsuccessful));

                } else {
                    Log.e("Error in Server", diplayServerResopnse);
                    Toast.makeText(getActivity(), diplayServerResopnse, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                e.printStackTrace();
            }
        }

    }
    //endregion

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (asyncNextgenLocationUpdate != null && !asyncNextgenLocationUpdate.isCancelled()) {
            asyncNextgenLocationUpdate.cancel(true);
        }
        //Image Preview DIalog
        if (customAddressPreviewDialog != null) {
            customAddressPreviewDialog.dismiss();
            customAddressPreviewDialog = null;

        }
        if (asyncSaveSiteReadinessVerification != null) {
            asyncSaveSiteReadinessVerification.cancel(true);
        }

        if (asyncGetAddressDetails != null && !asyncGetAddressDetails.isCancelled()) {
            asyncGetAddressDetails.cancel(true);
        }

    }

    //region GSTIN Address Validation - No Special char allowed
    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };
    //endregion

    public void reloadLocationNotIdentifiedAndConsentNotAgreeData() {

        try {

            //region Location Not Identified

            //Remarks
            editTextLocationNotIdentifiedRemarks.setText(franchiseeDetails.getSiteNotIdentifiedRemarks());
            editTextLocationNotIdentifiedRemarks.addTextChangedListener(new EditTextWatcher(new EditTextWatcher.IEditextData() {
                @Override
                public void getEditTextData(String data) {
                    franchiseeDetails.setSiteNotIdentifiedRemarks(data);
                }
            }));

            //Expected Identification Date
            strExpectedIdentificationDate = franchiseeDetails.getExpectedDateOfSiteIdentification();
            if (!TextUtils.isEmpty(strExpectedIdentificationDate)) {
                expectedIdentificationDate = dateFormatter1.parse(strExpectedIdentificationDate);
                String tempCompletionDate = CommonUtils.getFormattedDate(YYYY_MM_DD_CONST, DD_MM_YYYY_CONST, strExpectedIdentificationDate);
                textViewExpectedIdentificationDate.setText(tempCompletionDate);
            }

            textViewExpectedIdentificationDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedDateTimeId = v.getId();
                    showDateTimeDialogPicker();
                }
            });

            //endregion

            //region Consent Not Agree

            //Remarks
            editTextConsentNotAgree.setText(franchiseeDetails.getConsentExitRemarks());
            editTextConsentNotAgree.addTextChangedListener(new EditTextWatcher(new EditTextWatcher.IEditextData() {
                @Override
                public void getEditTextData(String data) {
                    franchiseeDetails.setConsentExitRemarks(data);
                }
            }));

            //Set Reason Check box
            if (!TextUtils.isEmpty(franchiseeDetails.getConsentExitReason())) {
                String[] chkReason = franchiseeDetails.getConsentExitReason().split(AESTRIC);

                checkBox_consent_text1.setChecked(chkReason[0].equalsIgnoreCase("1") ? true : false);
                checkBox_consent_text2.setChecked(chkReason[1].equalsIgnoreCase("1") ? true : false);
                checkBox_consent_text3.setChecked(chkReason[2].equalsIgnoreCase("1") ? true : false);
                checkBox_consent_text4.setChecked(chkReason[3].equalsIgnoreCase("1") ? true : false);
                if (checkBox_consent_text4.isChecked()) {
                    layoutConsentNotAgreeRemarks.setVisibility(View.VISIBLE);
                } else {
                    layoutConsentNotAgreeRemarks.setVisibility(View.GONE);
                }
            }

            //Visible Remarks if Other is selected
            checkBox_consent_text4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked) {
                        layoutConsentNotAgreeRemarks.setVisibility(View.VISIBLE);
                    } else {
                        layoutConsentNotAgreeRemarks.setVisibility(View.GONE);
                    }
                }
            });


            //endregion


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean validateConsentNotAgreeAndLocationNotIdentifiedData() {
        if (franchiseeDetails.isNeedToBeReviewed()) {
            if (!franchiseeDetails.isProfileReviewed()) {
                showAlertOkMessage(getContext().getString(R.string.alert_msg_i_agree));
                return false;
            }
        }

        //Consent and Location not identified
        if (!TextUtils.isEmpty(franchiseeDetails.getLocationStatus()) && (!TextUtils.isEmpty(franchiseeDetails.getConsentStatus()) && franchiseeDetails.getConsentStatus().equalsIgnoreCase("0"))) {

            if (!IsConsentNotAgree())
                return false;

        } else {
            if (!IsLocationNotIdentifiedValidated())
                return false;
        }
        return true;
    }

    public boolean IsLocationNotIdentifiedValidated() {
        boolean isValid = true;
        String msg = "";

        if (franchiseeDetails.getLocationStatus() != null && franchiseeDetails.getLocationStatus().equalsIgnoreCase("0")) {

            //Validate Identification Date
            if (TextUtils.isEmpty(franchiseeDetails.getExpectedDateOfSiteIdentification())) {
                isValid = false;
                msg = "Please enter Expected Identification Date.";

            } else if (TextUtils.isEmpty(franchiseeDetails.getSiteNotIdentifiedRemarks())) {         //Remarks
                isValid = false;
                msg = "Please enter Remarks for location not identified";
            }
        }

        if (!isValid) {
            showAlertOkMessage(msg);
        }

        return isValid;
    }

    public boolean IsConsentNotAgree() {
        boolean isValid = true;
        String msg = "";
        boolean IsToBeRemoved = false;
        if (getActivity().getClass().getSimpleName().equalsIgnoreCase("NextGenPhotoViewPager")) {
            IsToBeRemoved = Constants.IsToBeRemoved;
        }

        if (franchiseeDetails.getConsentStatus() != null && franchiseeDetails.getConsentStatus().equalsIgnoreCase("0")) {

            //Validate Other
            if (!IsAnyCheckBoxSelected()) {
                isValid = false;
                msg = "Please select any reason.";

            }

            if (IsSiteVisit && IsToBeRemoved) {
                isValid = true;
            } else {
                if (checkBox_consent_text4.isChecked()) {
                    if (TextUtils.isEmpty(franchiseeDetails.getConsentExitRemarks())) {
                        isValid = false;
                        msg = "Please enter remarks for other reason.";
                    }
                }
            }
        }

        if (!isValid) {
            showAlertOkMessage(msg);
        }
        return isValid;
    }

    public boolean IsAnyCheckBoxSelected() {
        int count = 0;

        if (checkBox_consent_text1.isChecked())
            count++;

        if (checkBox_consent_text2.isChecked())
            count++;

        if (checkBox_consent_text3.isChecked())
            count++;

        if (checkBox_consent_text4.isChecked())
            count++;

        if (count > 0)
            return true;

        return false;
    }

    private void updateConsentExitReason() {

        String consent1 = checkBox_consent_text1.isChecked() ? "1" : "0";
        String consent2 = checkBox_consent_text2.isChecked() ? "1" : "0";
        String consent3 = checkBox_consent_text3.isChecked() ? "1" : "0";
        String consent4 = checkBox_consent_text4.isChecked() ? "1" : "0";

        String status = consent1 + "*" + consent2 + "*" + consent3 + "*" + consent4;
        franchiseeDetails.setConsentExitReason(status);

    }

    public void setLocationNotIdentifiedAndConsentNotAgreeSubmitVisibility() {

        RadioButton radioButtonConsentNo = radioGroupConsent.findViewById(R.id.radioButtonConsentNo);
        if (radioButtonConsentNo.isChecked() || franchiseeDetails.getLocationStatus().equalsIgnoreCase("0")) {
            layoutLocNotIdentifiedAndConsentNotAgreeSubmitButton.setVisibility(View.VISIBLE);

        } else {
            layoutLocNotIdentifiedAndConsentNotAgreeSubmitButton.setVisibility(View.GONE);
        }
    }

    //region Show Alert Message
    private void showAlertOkMessage(String msg) {

        if (TextUtils.isEmpty(msg))
            return;

        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        alertDialogBuilder.show();
    }
    //endregion

    public void directCall(String mobileNo) {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_PHONE_CALL);
        } else {
            //Open call function
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + mobileNo));
            startActivity(intent);
        }
    }

    public void reloadReadinessVerification() {
        if (!isSiteReadiness) {
            layoutReadinessVerification.setVisibility(View.GONE);
            return;
        }

        if ((connection.getVkid().startsWith("VL") || connection.getVkid().startsWith("VA"))) {
            layoutReadinessVerification.setVisibility(View.GONE);
            return;
        }

        //region Work Completion Intimation: Hide for Completion Intimation for Phase 1 ---Vasundhara 19th Feb, 2019
        String phaseCode = TextUtils.isEmpty(CommonUtils.getPhaseCode(getContext())) ? Constants.PHASE_0 : CommonUtils.getPhaseCode(getContext());
        if (phaseCode.equalsIgnoreCase(Constants.PHASE_1)) {
            layoutReadinessVerification.setVisibility(View.GONE);
        } else {
            layoutReadinessVerification.setVisibility(View.VISIBLE);
        }
        //endregion

        //Work Comencement Spinner
        List<WorkCommencementStatusDto> statusList = prepareCommencementData();
        ArrayList<Object> list1 = new ArrayList<Object>(statusList);
        customSpinnerAdapter = new CustomSpinnerAdapter(getContext(), list1);
        spinnerWorkCommencement.setAdapter(customSpinnerAdapter);
        int selPos = getSelWorkCommencementPos(statusList);
        spinnerWorkCommencement.setSelection(selPos);
        spinnerWorkCommencement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0) {
                    WorkCommencementStatusDto selDto = (WorkCommencementStatusDto) spinnerWorkCommencement.getItemAtPosition(position);
                    franchiseeDetails.setReadinessWorkCommencementStatus(selDto.getId());
                    ((SiteReadinessActivity) getActivity()).setFranchiseeDetails(franchiseeDetails);

                    if (selDto.getId().equalsIgnoreCase("1")) {
                        txtCommencementDateLbl.setText("Work Started On");
                    } else {
                        txtCommencementDateLbl.setText("Expected Date of Work Start");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Do Nothing
            }
        });


        //Commencement Date Label
        int commencementStatus = TextUtils.isEmpty(franchiseeDetails.getReadinessWorkCommencementStatus()) ? 0 : Integer.parseInt(franchiseeDetails.getReadinessWorkCommencementStatus());
        if (commencementStatus == 1)
            txtCommencementDateLbl.setText("Work Started On");
        else
            txtCommencementDateLbl.setText("Expected Date of Work Start");

        //Set Date
        try {
            strCommencementDate = franchiseeDetails.getReadinessWorkCommencementDate();
            if (!TextUtils.isEmpty(strCommencementDate)) {
                commencementDate = dateFormatter1.parse(strCommencementDate);
                txtCommencementDate.setText(dateFormatter2.format(commencementDate));
            }
        } catch (Exception pe) {
            pe.getMessage();
        }

        txtCommencementDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDateTimeId = v.getId();
                showDateTimeDialogPicker();
            }
        });


        //Disable layout
        int status = TextUtils.isEmpty(franchiseeDetails.getReadinessWorkCommencementStatus()) ? 0 : Integer.parseInt(franchiseeDetails.getReadinessWorkCommencementStatus());
        String date = franchiseeDetails.getReadinessWorkCommencementDate();

        // Status - Start and Date available
        if (TextUtils.isEmpty(franchiseeDetails.getReadinessWorkCount()) || franchiseeDetails.getReadinessWorkCount().equals("0")) {
            GUIUtils.setViewAndChildrenEnabled(layoutReadinessVerification, true);
            layoutSubmitReadinessVerification.setVisibility(View.VISIBLE);
        } else if (status == 1 && !TextUtils.isEmpty(date)) {
            GUIUtils.setViewAndChildrenEnabled(layoutReadinessVerification, false);
            layoutSubmitReadinessVerification.setVisibility(View.GONE);
        } else if (status == 1) {
            spinnerWorkCommencement.setEnabled(false);
            layoutSubmitReadinessVerification.setVisibility(View.VISIBLE);
        } else {
            GUIUtils.setViewAndChildrenEnabled(layoutReadinessVerification, true);
            layoutSubmitReadinessVerification.setVisibility(View.VISIBLE);
        }
    }

    public int getSelWorkCommencementPos(List<WorkCommencementStatusDto> commencementList) {

        if (TextUtils.isEmpty(franchiseeDetails.getReadinessWorkCommencementStatus()))
            return 0;

        for (int i = 0; i < commencementList.size(); i++) {
            if (franchiseeDetails.getReadinessWorkCommencementStatus().equalsIgnoreCase(commencementList.get(i).getId()))
                return i;
        }
        return 0;
    }

    private List<WorkCommencementStatusDto> prepareCommencementData() {
        List<WorkCommencementStatusDto> commencementStatusList = new ArrayList<WorkCommencementStatusDto>();

        WorkCommencementStatusDto statusDto = new WorkCommencementStatusDto("0", "Work Not Started");
        commencementStatusList.add(statusDto);
        WorkCommencementStatusDto statusDto1 = new WorkCommencementStatusDto("1", "Work Started");
        commencementStatusList.add(statusDto1);

        return commencementStatusList;
    }

    public void setAddressVisibility() {

        if (!isSiteReadiness) {
            AddressLine1.setEnabled(false);
            AddressLine1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            return;
        }

        //Site Readiness
        if (connection.getVkid().startsWith("VL") || connection.getVkid().startsWith("VA")) {
            AddressLine1.setEnabled(false);
            AddressLine1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else {
          /*  AddressLine1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_edit_black_24dp, 0);
            AddressLine1.setEnabled(true);*/

            AddressLine1.setEnabled(false);
            AddressLine1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
    }

    private void updateKendraAddressDetails() {

        boolean IsUpdateAddressShown = (!TextUtils.isEmpty(franchiseeDetails.getIsUpdateAddressShown()) && franchiseeDetails.getIsUpdateAddressShown().equalsIgnoreCase("1")) ? true : false;

        if (IsUpdateAddressShown)
            layoutUpdateAddress.setVisibility(View.VISIBLE);
        else
            layoutUpdateAddress.setVisibility(View.GONE);

        //Update Address
        btnUpdateAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationHanndler.bubbleAnimation(getContext(), v);
                asyncGetAddressDetails = new AsyncGetAddressDetails(getContext(), new AsyncGetAddressDetails.Callback() {
                    @Override
                    public void onResult(String result) {
                        processAddressResult(result);
                    }
                });
                asyncGetAddressDetails.execute("");
            }
        });
    }

    private void processAddressResult(String result) {
        try {
            if (TextUtils.isEmpty(result)) {
                AlertDialogBoxInfo.alertDialogShow(getContext(), getContext().getResources().getString(R.string.Warning));
                return;
            }

            if (result.startsWith("ERROR")) {
                String msg = result.replace("ERROR|", "");
                msg = TextUtils.isEmpty(msg) ? "Something went wrong. Please try again later." : msg;
                AlertDialogBoxInfo.alertDialogShow(getContext(), msg);
                return;
            }

            if (result.startsWith("OKAY")) {
                //Handle Response
                String data = result.replace("OKAY|", "");
                if (TextUtils.isEmpty(data))
                    AlertDialogBoxInfo.alertDialogShow(getContext(), getContext().getResources().getString(R.string.Warning));
                else {

                    Gson gson = new Gson();
                    UpdateAddressDetailsDto addressDetailsDto = gson.fromJson(data, UpdateAddressDetailsDto.class);

                    Intent intent = new Intent(getContext(), UpdateKendraAddressDetailsActivity.class);
                    intent.putExtra("ADDRESS_DETAILS", addressDetailsDto);
                    startActivity(intent);

                }
            } else {
                AlertDialogBoxInfo.alertDialogShow(getContext(), getContext().getResources().getString(R.string.Warning));
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogBoxInfo.alertDialogShow(getContext(), getContext().getResources().getString(R.string.Warning));
        }
    }

    private void handleUpdateAdressOnLocationIdentied(boolean IsLocationIdentified){

        boolean IsUpdateAddressShown = (!TextUtils.isEmpty(franchiseeDetails.getIsUpdateAddressShown()) && franchiseeDetails.getIsUpdateAddressShown().equalsIgnoreCase("1")) ? true : false;
        if (!IsUpdateAddressShown)
            return;

        if(IsLocationIdentified){
            layoutUpdateAddress.setVisibility(View.VISIBLE);
        } else {
            layoutUpdateAddress.setVisibility(View.GONE);
        }
    }
}
