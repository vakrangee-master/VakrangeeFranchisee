package in.vakrangee.franchisee.sitelayout.fragment;

import static in.vakrangee.franchisee.sitelayout.fragment.NextGenKendraPhotoFragment.myvakranggekendraimageList;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
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

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import in.vakrangee.franchisee.sitelayout.NextGenPhotoViewPager;
import in.vakrangee.franchisee.sitelayout.NextGenViewPager;
import in.vakrangee.franchisee.sitelayout.OTPDialog;
import in.vakrangee.franchisee.sitelayout.R;
import in.vakrangee.franchisee.sitelayout.adapter.CustomRecyclerAdapter;
import in.vakrangee.franchisee.sitelayout.model.Bank;
import in.vakrangee.franchisee.sitelayout.model.District;
import in.vakrangee.franchisee.sitelayout.model.State;
import in.vakrangee.franchisee.sitelayout.model.Village;
import in.vakrangee.supercore.franchisee.commongui.DateTimePickerDialog;
import in.vakrangee.supercore.franchisee.commongui.PopupUtils;
import in.vakrangee.supercore.franchisee.commongui.imagegallery.ImageDto;
import in.vakrangee.supercore.franchisee.commongui.imagepreview.CustomImagePreviewDialog;
import in.vakrangee.supercore.franchisee.ifc.ServiceProviderIfc;
import in.vakrangee.supercore.franchisee.model.FranchiseeDetails;
import in.vakrangee.supercore.franchisee.model.FranchiseeRemarkDetails;
import in.vakrangee.supercore.franchisee.model.My_vakranggekendra_image;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.utils.GUIUtils;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;
import in.vakrangee.supercore.franchisee.utils.JSONUtils;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;
import in.vakrangee.supercore.franchisee.webservice.WebService;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class NextGenSiteVisitFragment extends Fragment {


    FranchiseeDetails franchiseeDetails;
    View view;

    private DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US); //new SimpleDateFormat("dd-MM-yyyy HH:mm a", Locale.US);
    private DateFormat dateFormatter2 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    private DateFormat dateFormatter3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

    ViewGroup root;
    ScrollView scrollViewNextGenSiteVisit;
    LinearLayout tvHeader, layoutPremiseLocatedAt, layoutAddressOfLocationVisited, layoutPremiseLevel, layoutPremiseShape, layoutClosestBankBranch, layoutClosestATM;
    TextView tooltipPremiseDetail, tooltipPremiseLocatedAt, tooltipAddressOfLocationVisited, tooltipPremiseLevel, tooltipPremiseShape, tooltipClosestBankBranch,
            tooltipClosestATM, tooltipInteriorWork, tooltipProvisionalSignBoard;

    public NextGenSiteVisitFragment() {
    }

    public NextGenSiteVisitFragment(FranchiseeDetails franchiseeDetails) {
        this.franchiseeDetails = franchiseeDetails;
    }

    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager linearLayoutManager;

    List<PremiseShape> premiseShapes;

    Spinner spinnerWidthFeet, spinnerWidthInch, spinnerDepthFeet, spinnerDepthInch, spinnerHeightFeet,
            spinnerHeightInch, spinnerFrontageFeet, spinnerFrontageInch, spinnerFloor, spinnerStructure,
            spinnerRoof, spinnerMainSignBoardFeet, spinnerMainSignBoardInch, spinnerEntranceFeet, spinnerEntranceInch;

    EditText AreaSqFeet, SiteVisitWardNo, SiteVisitPincode, Remarks;

    TextView wardNoTextView, SiteVisitAddress1, SiteVisitAddress2, SiteVisitLandmark;

    RadioGroup premiseLocated, PremiseLevel, interiorWorkStatus;

    RadioButton premiseLocatedAtHighStreet, premiseLocatedAtMall, roadLevel, belowRoad,
            FrontageObstructedYes, FrontageObstructedNo, FootpathYes, FootpathNo, BathroomYes,
            BathroomNo, PantryYes, PantryNo, PillersYes, PillersNo, WindowsYes, WindowsNo,
            BeamYes, BeamNo, AdjacentShopsYes, AdjacentShopsNo, MultipleEntriesYes, MultipleEntriesNo,
            InteriorWorkYTS, InteriorWorkInProgress, InteriorWorkCompleted, InteriorWorkApproved;

    public ArrayList<State> arrayListStates = new ArrayList<>();
    public ArrayList<District> arrayListDistrict = new ArrayList<>();
    private ArrayList<Village> arrayListVillage = new ArrayList<>();
    private ArrayList<Bank> arrayListBankBranch = new ArrayList<>();
    private ArrayList<Bank> arrayListATM = new ArrayList<>();

    private Spinner stateSpinner, districtSpinner, villageSpinner, closestATM1, closestATM2, closestATM3,
            closestBankBranch1, closestBankBranch2, closestBankBranch3;

    String selectedState, selectedDistrict, selectedVillage, selectNBCodeATM1;
    Button btn_submit_sitevisit;
    private static String TAG = NextGenSiteVisitFragment.class.toString();
    String diplayServerResopnse;
    TelephonyManager telephonyManager;
    ProgressDialog progress;
    private ImageView imgOtherSketch;

    private static final int CAMERA_PIC_REQUEST = 1;
    private CustomImagePreviewDialog customImagePreviewDialog;

    private AlphaAnimation btnClickAnim = new AlphaAnimation(1F, 0.6F);
    private PermissionHandler permissionHandler;
    private Uri picUri;
    private Bitmap bitmap = null;

    private boolean isAdhoc = false;

    private LinearLayout layoutInteriorWorkStatus;
    private TextView textViewStartDate;        // Start Date
    private TextView textViewCompletionDate;  // Completion Date
    private TextView textViewStartDateLabel;
    private TextView textViewCompletionDateLabel, textViewCompletionDateLbl;

    private Date startDate, completionDate;
    private String strStartDate, strCompletionDate;

    private TextView textViewWidthInfo, textViewDepthInfo, textViewHeightInfo, textViewSignboardInfo, textViewEntranceInfo;
    private LinearLayout layoutStartDate, layoutCompletionDate;
    private LinearLayout layoutVerified;
    private CheckBox checkBoxProfileVerify;
    private in.vakrangee.franchisee.sitelayout.OTPDialog OTPDialog;
    private ImageView imgSiteLayoutSketchInfo;
    private boolean IsToBeRemoved = false;
    private LinearLayout layoutCompleteAddress;
    private LinearLayout layoutMainSignBoardDetails;
    private TextView txtCarpetAreaLbl;
    private String mCurrentPhotoPath;

    //add provisional main signbaorad
    private Spinner spinnerProvisionalLengthFeet, spinnerProvisionalLengthInch, spinnerProvisionalWidthFeet, spinnerProvisionalWidthInch;
    private ArrayAdapter<Integer> WidthfeetArrayAdapter, LengthfeetArrayAdapter, inchArrayAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_vakrangee_kendra_premise_details, container, false);
        final Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fontawesome-webfont.ttf");
        try {

            // Get App MODE
            isAdhoc = Constants.ENABLE_ADHOC_MODE || Constants.ENABLE_FRANCHISEE_MODE;

            permissionHandler = new PermissionHandler(getActivity());

            // Scroll and Layout Components
            root = (ViewGroup) view.findViewById(R.id.root);
            scrollViewNextGenSiteVisit = view.findViewById(R.id.scrollViewNextGenSiteVisit);
            layoutCompleteAddress = view.findViewById(R.id.layoutCompleteAddress);
            layoutInteriorWorkStatus = view.findViewById(R.id.layoutInteriorWorkStatus);
            layoutMainSignBoardDetails = view.findViewById(R.id.layoutMainSignBoardDetails);
            txtCarpetAreaLbl = view.findViewById(R.id.txtCarpetAreaLbl);
            tvHeader = view.findViewById(R.id.tvHeader);
            layoutPremiseLocatedAt = view.findViewById(R.id.layoutPremiseLocatedAt);
            layoutAddressOfLocationVisited = view.findViewById(R.id.layoutAddressOfLocationVisited);
            layoutPremiseLevel = view.findViewById(R.id.layoutPremiseLevel);
            layoutPremiseShape = view.findViewById(R.id.layoutPremiseShape);
            layoutClosestBankBranch = view.findViewById(R.id.layoutClosestBankBranch);
            layoutClosestATM = view.findViewById(R.id.layoutClosestATM);
            imgOtherSketch = view.findViewById(R.id.imgOtherSketch);
            layoutVerified = view.findViewById(R.id.layoutVerified);
            checkBoxProfileVerify = view.findViewById(R.id.checkBoxProfileVerify);
            textViewCompletionDateLbl = view.findViewById(R.id.textViewCompletionDateLabel);
            imgSiteLayoutSketchInfo = view.findViewById(R.id.imgSiteLayoutSketchInfo);
            imgSiteLayoutSketchInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showImageDialog(R.drawable.site_layou_place_holder, "Site Layout Sketch Preview");
                }
            });

            // Tooltip
            tooltipPremiseDetail = (TextView) view.findViewById(R.id.tooltipPremiseDetail);
            tooltipPremiseLocatedAt = (TextView) view.findViewById(R.id.tooltipPremiseLocatedAt);
            tooltipAddressOfLocationVisited = (TextView) view.findViewById(R.id.tooltipAddressOfLocationVisited);
            tooltipPremiseLevel = view.findViewById(R.id.tooltipPremiseLevel);
            tooltipPremiseShape = (TextView) view.findViewById(R.id.tooltipPremiseShape);
            tooltipClosestBankBranch = (TextView) view.findViewById(R.id.tooltipClosestBankBranch);
            tooltipClosestATM = (TextView) view.findViewById(R.id.tooltipClosestATM);
            tooltipInteriorWork = (TextView) view.findViewById(R.id.tooltipInteriorWork);
            tooltipProvisionalSignBoard = (TextView) view.findViewById(R.id.tooltipProvisionalSignBoard);
            tooltipPremiseDetail.setTypeface(font);
            tooltipPremiseLocatedAt.setTypeface(font);
            tooltipAddressOfLocationVisited.setTypeface(font);
            tooltipPremiseLevel.setTypeface(font);
            tooltipPremiseShape.setTypeface(font);
            tooltipClosestBankBranch.setTypeface(font);
            tooltipClosestATM.setTypeface(font);
            tooltipInteriorWork.setTypeface(font);
            tooltipProvisionalSignBoard.setTypeface(font);
            tooltipPremiseDetail.setText(new SpannableStringBuilder(" " + new String(new char[]{0xf06a}) + " "));
            tooltipPremiseLocatedAt.setText(new SpannableStringBuilder(" " + new String(new char[]{0xf06a}) + " "));
            tooltipAddressOfLocationVisited.setText(new SpannableStringBuilder(" " + new String(new char[]{0xf06a}) + " "));
            tooltipPremiseLevel.setText(new SpannableStringBuilder(" " + new String(new char[]{0xf06a}) + " "));
            tooltipPremiseShape.setText(new SpannableStringBuilder(" " + new String(new char[]{0xf06a}) + " "));
            tooltipClosestBankBranch.setText(new SpannableStringBuilder(" " + new String(new char[]{0xf06a}) + " "));
            tooltipClosestATM.setText(new SpannableStringBuilder(" " + new String(new char[]{0xf06a}) + " "));
            tooltipInteriorWork.setText(new SpannableStringBuilder(" " + new String(new char[]{0xf06a}) + " "));
            tooltipProvisionalSignBoard.setText(new SpannableStringBuilder(" " + new String(new char[]{0xf06a}) + " "));

            spinnerProvisionalLengthFeet = view.findViewById(R.id.spinnerProvisionalLengthFeet);
            spinnerProvisionalLengthInch = view.findViewById(R.id.spinnerProvisionalLengthInch);
            spinnerProvisionalWidthFeet = view.findViewById(R.id.spinnerProvisionalWidthFeet);
            spinnerProvisionalWidthInch = view.findViewById(R.id.spinnerProvisionalWidthInch);

            //when focusable true , if item click in spinner
            spinner_focusablemode(spinnerProvisionalLengthFeet);
            spinner_focusablemode(spinnerProvisionalLengthInch);
            spinner_focusablemode(spinnerProvisionalWidthFeet);
            spinner_focusablemode(spinnerProvisionalWidthInch);

            //---------------------------------------------------------------------------------
            //Width Adapter
            List feetProvisional = new ArrayList<Integer>();
            feetProvisional.add("Please Select");
            for (int i = 1; i <= 3; i++) {
                if (i != 1) {
                    feetProvisional.add(Integer.toString(i));
                }
            }
            WidthfeetArrayAdapter = new ArrayAdapter<Integer>(getContext(), android.R.layout.simple_spinner_item, feetProvisional);
            WidthfeetArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            //Length Adapter
            List feet1feetProvisional = new ArrayList<Integer>();
            feet1feetProvisional.add("Please Select");
            for (int i = 1; i <= 50; i++) {
                feet1feetProvisional.add(Integer.toString(i));
            }
            LengthfeetArrayAdapter = new ArrayAdapter<Integer>(getContext(), android.R.layout.simple_spinner_item, feet1feetProvisional);
            LengthfeetArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            //setFeetAdapter(50, LengthfeetArrayAdapter);

            //Inch Adapter
            List inchProvisional = new ArrayList<Integer>();
            inchProvisional.add("Please Select");

            for (int i = 0; i < 12; i++) {
                inchProvisional.add(Integer.toString(i));
            }
            inchArrayAdapter = new ArrayAdapter<Integer>(getContext(), android.R.layout.simple_spinner_item, inchProvisional);
            inchArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            //inches
            String widthFeetAndInchesProvisional = inchesTofeetAndInches(franchiseeDetails.getProvisionalLengthMainSignboard() == null ? "0" : franchiseeDetails.getProvisionalLengthMainSignboard());
            final String[] splitWidthFeetAndInchesProvisional = widthFeetAndInchesProvisional.split("\\|");


            //length feet
            spinnerProvisionalLengthFeet.setAdapter(LengthfeetArrayAdapter);
            spinnerProvisionalLengthFeet.setSelection(getIndexText(spinnerProvisionalLengthFeet, splitWidthFeetAndInchesProvisional[0]));
            spinnerProvisionalLengthFeet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (spinnerProvisionalLengthFeet.getSelectedItem().toString().equals("Please Select") &&
                            spinnerProvisionalLengthInch.getSelectedItem().toString().equals("Please Select")) {
                        franchiseeDetails.setProvisionalLengthMainSignboard(null);
                    } else if (!spinnerProvisionalLengthFeet.getSelectedItem().toString().equals("Please Select") &&
                            spinnerProvisionalLengthInch.getSelectedItem().toString().equals("Please Select")) {
                        int a = feetAndInchesToInches(Integer.parseInt(spinnerProvisionalLengthFeet.getSelectedItem().toString()), 0);
                        franchiseeDetails.setProvisionalLengthMainSignboard(String.valueOf(a));
                    } else if (spinnerProvisionalLengthFeet.getSelectedItem().toString().equals("Please Select") &&
                            !spinnerProvisionalLengthInch.getSelectedItem().toString().equals("Please Select")) {
                        int a = feetAndInchesToInches(0, Integer.parseInt(spinnerProvisionalLengthInch.getSelectedItem().toString()));
                        franchiseeDetails.setProvisionalLengthMainSignboard(String.valueOf(a));
                    } else {
                        int a = feetAndInchesToInches(Integer.parseInt(spinnerProvisionalLengthFeet.getSelectedItem().toString()),
                                Integer.parseInt(spinnerProvisionalLengthInch.getSelectedItem().toString()));
                        franchiseeDetails.setProvisionalLengthMainSignboard(String.valueOf(a));
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            //length inch
            spinnerProvisionalLengthInch.setAdapter(inchArrayAdapter);
            spinnerProvisionalLengthInch.setSelection(getIndexText(spinnerProvisionalLengthInch, splitWidthFeetAndInchesProvisional[1]));
            spinnerProvisionalLengthInch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (spinnerProvisionalLengthFeet.getSelectedItem().toString().equals("Please Select") &&
                            spinnerProvisionalLengthInch.getSelectedItem().toString().equals("Please Select")) {
                        franchiseeDetails.setProvisionalLengthMainSignboard(null);
                    } else if (!spinnerProvisionalLengthFeet.getSelectedItem().toString().equals("Please Select") &&
                            spinnerProvisionalLengthInch.getSelectedItem().toString().equals("Please Select")) {
                        int a = feetAndInchesToInches(Integer.parseInt(spinnerProvisionalLengthFeet.getSelectedItem().toString()), 0);
                        franchiseeDetails.setProvisionalLengthMainSignboard(String.valueOf(a));
                    } else if (spinnerProvisionalLengthFeet.getSelectedItem().toString().equals("Please Select") &&
                            !spinnerProvisionalLengthInch.getSelectedItem().toString().equals("Please Select")) {
                        int a = feetAndInchesToInches(0, Integer.parseInt(spinnerProvisionalLengthInch.getSelectedItem().toString()));
                        franchiseeDetails.setProvisionalLengthMainSignboard(String.valueOf(a));
                    } else {
                        int a = feetAndInchesToInches(Integer.parseInt(spinnerProvisionalLengthFeet.getSelectedItem().toString()),
                                Integer.parseInt(spinnerProvisionalLengthInch.getSelectedItem().toString()));
                        franchiseeDetails.setProvisionalLengthMainSignboard(String.valueOf(a));
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            //width provisional
            String depthFeetAndInchesProvisional = inchesTofeetAndInches(franchiseeDetails.getProvisionalWidth() == null ? "0" : franchiseeDetails.getProvisionalWidth());
            String[] splitDepthFeetAndInchesP = depthFeetAndInchesProvisional.split("\\|");


            //Width Feet
            spinnerProvisionalWidthFeet.setAdapter(WidthfeetArrayAdapter);
            spinnerProvisionalWidthFeet.setSelection(getIndexText(spinnerProvisionalWidthFeet, splitDepthFeetAndInchesP[0]));
            spinnerProvisionalWidthFeet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (spinnerProvisionalWidthFeet.getSelectedItem().toString().equals("Please Select") &&
                            spinnerProvisionalWidthInch.getSelectedItem().toString().equals("Please Select")) {
                        franchiseeDetails.setProvisionalWidth(null);
                    } else if (!spinnerProvisionalWidthFeet.getSelectedItem().toString().equals("Please Select") &&
                            spinnerProvisionalWidthInch.getSelectedItem().toString().equals("Please Select")) {
                        int a = feetAndInchesToInches(Integer.parseInt(spinnerProvisionalWidthFeet.getSelectedItem().toString()), 0);
                        franchiseeDetails.setProvisionalWidth(String.valueOf(a));
                    } else if (spinnerProvisionalWidthFeet.getSelectedItem().toString().equals("Please Select") &&
                            !spinnerProvisionalWidthInch.getSelectedItem().toString().equals("Please Select")) {
                        int a = feetAndInchesToInches(0, Integer.parseInt(spinnerProvisionalWidthInch.getSelectedItem().toString()));
                        franchiseeDetails.setProvisionalWidth(String.valueOf(a));
                    } else {
                        int a = feetAndInchesToInches(Integer.parseInt(spinnerProvisionalWidthFeet.getSelectedItem().toString()),
                                Integer.parseInt(spinnerProvisionalWidthInch.getSelectedItem().toString()));
                        franchiseeDetails.setProvisionalWidth(String.valueOf(a));
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            spinnerProvisionalWidthInch.setAdapter(inchArrayAdapter);
            spinnerProvisionalWidthInch.setSelection(getIndexText(spinnerProvisionalWidthInch, splitDepthFeetAndInchesP[1]));
            spinnerProvisionalWidthInch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (spinnerProvisionalWidthFeet.getSelectedItem().toString().equals("Please Select") &&
                            spinnerProvisionalWidthInch.getSelectedItem().toString().equals("Please Select")) {
                        franchiseeDetails.setProvisionalWidth(null);
                    } else if (!spinnerProvisionalWidthFeet.getSelectedItem().toString().equals("Please Select") &&
                            spinnerProvisionalWidthInch.getSelectedItem().toString().equals("Please Select")) {
                        int a = feetAndInchesToInches(Integer.parseInt(spinnerProvisionalWidthFeet.getSelectedItem().toString()), 0);
                        franchiseeDetails.setProvisionalWidth(String.valueOf(a));
                    } else if (spinnerProvisionalWidthFeet.getSelectedItem().toString().equals("Please Select") &&
                            !spinnerProvisionalWidthInch.getSelectedItem().toString().equals("Please Select")) {
                        int a = feetAndInchesToInches(0, Integer.parseInt(spinnerProvisionalWidthInch.getSelectedItem().toString()));
                        franchiseeDetails.setProvisionalWidth(String.valueOf(a));
                    } else {
                        int a = feetAndInchesToInches(Integer.parseInt(spinnerProvisionalWidthFeet.getSelectedItem().toString()),
                                Integer.parseInt(spinnerProvisionalWidthInch.getSelectedItem().toString()));
                        franchiseeDetails.setProvisionalWidth(String.valueOf(a));
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            //----------------------------------------------------------------------

            //Other Sketch
            if (!TextUtils.isEmpty(franchiseeDetails.getSiteLayoutSketch())) {
                bitmap = CommonUtils.StringToBitMap(franchiseeDetails.getSiteLayoutSketch());
                if (bitmap != null)
                    imgOtherSketch.setImageBitmap(bitmap);
            }
            imgOtherSketch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtils.isEmpty(franchiseeDetails.getSiteLayoutSketch())) {
                        startCamera(view);

                    } else {
                        bitmap = CommonUtils.StringToBitMap(franchiseeDetails.getSiteLayoutSketch());
                        ImageDto imageDto = new ImageDto();
                        imageDto.setBitmap(bitmap);
                        imageDto.setName("Site Layout Sketch");
                        imageDto.setImageBase64(franchiseeDetails.getSiteLayoutSketch());
                        showImagePreviewDialog(imageDto);
                    }
                }
            });

            //Premise Detail
            btn_submit_sitevisit = (Button) view.findViewById(R.id.btn_submit_sitevisit);
            btn_submit_sitevisit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Validate Data Before Submission
                    updateProfileData();
                    ArrayList<String> imagename = new ArrayList<>();
                    for (My_vakranggekendra_image kendraPhoto : myvakranggekendraimageList) {
                        Log.d("datas images ::: ", kendraPhoto.getImgetype());
                        imagename.add(kendraPhoto.getImgetype());

                    }


                    if (!imagename.contains("Frontage Image (5ft Distance)")) {
                        Toast.makeText(getActivity(), "Please Add Frontage Image (5ft Distance) Image", Toast.LENGTH_SHORT).show();
                    } else if (!imagename.contains("Frontage Image (10ft Distance)")) {
                        Toast.makeText(getActivity(), "Please Add Frontage Image (10ft Distance) Image", Toast.LENGTH_SHORT).show();
                    } else if (!imagename.contains("Left Wall Image")) {
                        Toast.makeText(getActivity(), "Please Add Left Wall Image", Toast.LENGTH_SHORT).show();
                    } else if (!imagename.contains("Front Wall Image")) {
                        Toast.makeText(getActivity(), "Please Add Front Wall Image", Toast.LENGTH_SHORT).show();
                    } else if (!imagename.contains("Right Wall Image")) {
                        Toast.makeText(getActivity(), "Please Add Right Wall Image", Toast.LENGTH_SHORT).show();
                    } else if (!imagename.contains("Back Wall Image")) {
                        Toast.makeText(getActivity(), "Please Add Back Wall Image", Toast.LENGTH_SHORT).show();
                    } else if (!imagename.contains("Ceiling Image")) {
                        Toast.makeText(getActivity(), "Please Add Ceiling Image", Toast.LENGTH_SHORT).show();
                    } else if (!imagename.contains("Floor Image")) {
                        Toast.makeText(getActivity(), "Please Add Floor Image", Toast.LENGTH_SHORT).show();
                    } else if (!imagename.contains("Kendra Front outside view - from 10 ft distance")) {
                        Toast.makeText(getActivity(), "Please Add Kendra Front outside view - from 10 ft distance", Toast.LENGTH_SHORT).show();
                    } else if (!imagename.contains("Kendra Inside view")) {
                        Toast.makeText(getActivity(), "Please Add Kendra Inside view", Toast.LENGTH_SHORT).show();
                    } else if (!imagename.contains("Ribbon cutting ceremony")) {
                        Toast.makeText(getActivity(), "Please Add Ribbon cutting ceremony", Toast.LENGTH_SHORT).show();
                    } else if (!imagename.contains("Religious ceremony if any")) {
                        Toast.makeText(getActivity(), "Please Add Religious ceremony if any", Toast.LENGTH_SHORT).show();
                    } else if (!imagename.contains("Public Gathering")) {
                        Toast.makeText(getActivity(), "Please Add Public Gathering", Toast.LENGTH_SHORT).show();
                    } else {
                        if (OTPDialog != null && OTPDialog.isShowing()) {
                            return;
                        }

                        OTPDialog = new OTPDialog(getContext(), new OTPDialog.IIsOTPVerified() {
                            @Override
                            public void IsOTPVerified(boolean IsVerified) {
                                AsyncNextgenLocationUpdate asyncNextgenLocationUpdate = new AsyncNextgenLocationUpdate();
                                asyncNextgenLocationUpdate.execute();
                                OTPDialog.dismiss();
                            }
                        });
                        OTPDialog.setTitle("Please wait");
                        OTPDialog.setCancelable(true);
                        OTPDialog.show();
                    }
//                    if (NextGenKendraPhotoFragment.imgetype.equals("")) {
//                        Toast.makeText(getActivity(), "Please Select Image", Toast.LENGTH_SHORT).show();
//                    }
//                    if (franchiseeDetails.isNeedToBeReviewed()) {
//                        if (!isValidatedReviewedOption()) {
//                            return;
//                        }
//                    }

//                    if (!validateNextGenSiteVisit())
//                        return;

                    // Set Franchise Comment Entered into Site Visit Detail Tab.
//                    if (!TextUtils.isEmpty(Remarks.getText().toString().trim())) {
//                        franchiseeDetails.setFranchiseeComments(Remarks.getText().toString());
//                    }
//
//                    //Remarks Validated
//                    if (TextUtils.isEmpty(franchiseeDetails.getFranchiseeComments())) {
//                        Remarks.setError(getContext().getResources().getString(R.string.enterRemarks));
//                        return;
//                    }

                    if (!AreaSqFeet.getText().toString().equals("")) {
                        franchiseeDetails.setAreaSqFeet(AreaSqFeet.getText().toString());
                    }
                    if (!SiteVisitAddress1.getText().toString().equals("")) {
                        franchiseeDetails.setSiteVisitAddress1(SiteVisitAddress1.getText().toString());
                    }
                    if (!SiteVisitAddress2.getText().toString().equals("")) {
                        franchiseeDetails.setSiteVisitAddress2(SiteVisitAddress2.getText().toString());
                    }
                    if (!SiteVisitLandmark.getText().toString().equals("")) {
                        franchiseeDetails.setSiteVisitLandmark(SiteVisitLandmark.getText().toString());
                    }
                    if (!SiteVisitPincode.getText().toString().equals("")) {
                        franchiseeDetails.setSiteVisitPincode(SiteVisitPincode.getText().toString());
                    }
                    if (!SiteVisitWardNo.getText().toString().equals("")) {
                        franchiseeDetails.setSiteVisitWardNo(SiteVisitWardNo.getText().toString());
                    }

                    //franchiseeDetails.setFrontageInch(null);

                    // Set Status in case of "Site Send Back for Correction".
                    if (franchiseeDetails.getStatus() == NextGenViewPager.SITE_SEND_BACK_FOR_CORRECTION
                            || franchiseeDetails.getStatus() == NextGenViewPager.SITE_REESUBMITTED_FOR_VERIFICATION) {
                        franchiseeDetails.setResubmitStatus(1); // Only in case of "Site Send Back For Correction" 1 - Re-Submit

                    } else if (franchiseeDetails.isNeedToBeReviewed()) {
                        franchiseeDetails.setResubmitStatus(10);        //Reviewed and Confirmed

                    } else {
                        franchiseeDetails.setResubmitStatus(0); // 0 - Submit
                    }

                    //If REviewed
//                    if (franchiseeDetails.isNeedToBeReviewed()) {
//
//                        if (OTPDialog != null && OTPDialog.isShowing()) {
//                            return;
//                        }
//
//                        OTPDialog = new OTPDialog(getContext(), new OTPDialog.IIsOTPVerified() {
//                            @Override
//                            public void IsOTPVerified(boolean IsVerified) {
//                                AsyncNextgenLocationUpdate asyncNextgenLocationUpdate = new AsyncNextgenLocationUpdate();
//                                asyncNextgenLocationUpdate.execute();
//                                OTPDialog.dismiss();
//                            }
//                        });
//                        OTPDialog.setTitle("Please wait");
//                        OTPDialog.setCancelable(true);
//                        OTPDialog.show();
//
//                    } else {
//
//                        AsyncNextgenLocationUpdate asyncNextgenLocationUpdate = new AsyncNextgenLocationUpdate();
//                        asyncNextgenLocationUpdate.execute();
//                    }
                }
            });
            //Feet Adapter
            List feet = new ArrayList<Integer>();
            feet.add("Please Select");
            for (int i = 1; i <= 50; i++) {
                feet.add(Integer.toString(i));
            }
            ArrayAdapter<Integer> feetArrayAdapter = new ArrayAdapter<Integer>(
                    getContext(), android.R.layout.simple_spinner_item, feet);
            feetArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            //Inch Adapter
            List inch = new ArrayList<Integer>();
            inch.add("Please Select");

            for (int i = 0; i < 12; i++) {
                inch.add(Integer.toString(i));
            }
            ArrayAdapter<Integer> inchArrayAdapter = new ArrayAdapter<Integer>(
                    getContext(), android.R.layout.simple_spinner_item, inch);
            inchArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


            String widthFeetAndInches = inchesTofeetAndInches(franchiseeDetails.getWidthInch() == null ? "0" : franchiseeDetails.getWidthInch());
            final String[] splitWidthFeetAndInches = widthFeetAndInches.split("\\|");

            spinnerWidthFeet = (Spinner) view.findViewById(R.id.spinnerWidthFeet);

            spinnerWidthFeet.setAdapter(feetArrayAdapter);
            spinnerWidthFeet.setSelection(getIndexText(spinnerWidthFeet, splitWidthFeetAndInches[0]));
            spinnerWidthFeet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (spinnerWidthFeet.getSelectedItem().toString().equals("Please Select") &&
                            spinnerWidthInch.getSelectedItem().toString().equals("Please Select")) {
                        franchiseeDetails.setWidthInch(null);
                    } else if (!spinnerWidthFeet.getSelectedItem().toString().equals("Please Select") &&
                            spinnerWidthInch.getSelectedItem().toString().equals("Please Select")) {
                        int a = feetAndInchesToInches(Integer.parseInt(spinnerWidthFeet.getSelectedItem().toString()), 0);
                        franchiseeDetails.setWidthInch(String.valueOf(a));
                    } else if (spinnerWidthFeet.getSelectedItem().toString().equals("Please Select") &&
                            !spinnerWidthInch.getSelectedItem().toString().equals("Please Select")) {
                        int a = feetAndInchesToInches(0, Integer.parseInt(spinnerWidthInch.getSelectedItem().toString()));
                        franchiseeDetails.setWidthInch(String.valueOf(a));
                    } else {
                        int a = feetAndInchesToInches(Integer.parseInt(spinnerWidthFeet.getSelectedItem().toString()),
                                Integer.parseInt(spinnerWidthInch.getSelectedItem().toString()));
                        franchiseeDetails.setWidthInch(String.valueOf(a));
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            spinnerWidthInch = (Spinner) view.findViewById(R.id.spinnerWidthInch);

            spinnerWidthInch.setAdapter(inchArrayAdapter);
            spinnerWidthInch.setSelection(getIndexText(spinnerWidthInch, splitWidthFeetAndInches[1]));
            spinnerWidthInch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (spinnerWidthFeet.getSelectedItem().toString().equals("Please Select") &&
                            spinnerWidthInch.getSelectedItem().toString().equals("Please Select")) {
                        franchiseeDetails.setWidthInch(null);
                    } else if (!spinnerWidthFeet.getSelectedItem().toString().equals("Please Select") &&
                            spinnerWidthInch.getSelectedItem().toString().equals("Please Select")) {
                        int a = feetAndInchesToInches(Integer.parseInt(spinnerWidthFeet.getSelectedItem().toString()), 0);
                        franchiseeDetails.setWidthInch(String.valueOf(a));
                    } else if (spinnerWidthFeet.getSelectedItem().toString().equals("Please Select") &&
                            !spinnerWidthInch.getSelectedItem().toString().equals("Please Select")) {
                        int a = feetAndInchesToInches(0, Integer.parseInt(spinnerWidthInch.getSelectedItem().toString()));
                        franchiseeDetails.setWidthInch(String.valueOf(a));
                    } else {
                        int a = feetAndInchesToInches(Integer.parseInt(spinnerWidthFeet.getSelectedItem().toString()),
                                Integer.parseInt(spinnerWidthInch.getSelectedItem().toString()));
                        franchiseeDetails.setWidthInch(String.valueOf(a));
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            String depthFeetAndInches = inchesTofeetAndInches(franchiseeDetails.getDepthInch() == null ? "0" : franchiseeDetails.getDepthInch());
            String[] splitDepthFeetAndInches = depthFeetAndInches.split("\\|");

            spinnerDepthFeet = (Spinner) view.findViewById(R.id.spinnerDepthFeet);

            spinnerDepthFeet.setAdapter(feetArrayAdapter);
            spinnerDepthFeet.setSelection(getIndexText(spinnerDepthFeet, splitDepthFeetAndInches[0]));
            spinnerDepthFeet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (spinnerDepthFeet.getSelectedItem().toString().equals("Please Select") &&
                            spinnerDepthInch.getSelectedItem().toString().equals("Please Select")) {
                        franchiseeDetails.setDepthInch(null);
                    } else if (!spinnerDepthFeet.getSelectedItem().toString().equals("Please Select") &&
                            spinnerDepthInch.getSelectedItem().toString().equals("Please Select")) {
                        int a = feetAndInchesToInches(Integer.parseInt(spinnerDepthFeet.getSelectedItem().toString()), 0);
                        franchiseeDetails.setDepthInch(String.valueOf(a));
                    } else if (spinnerDepthFeet.getSelectedItem().toString().equals("Please Select") &&
                            !spinnerDepthInch.getSelectedItem().toString().equals("Please Select")) {
                        int a = feetAndInchesToInches(0, Integer.parseInt(spinnerDepthInch.getSelectedItem().toString()));
                        franchiseeDetails.setDepthInch(String.valueOf(a));
                    } else {
                        int a = feetAndInchesToInches(Integer.parseInt(spinnerDepthFeet.getSelectedItem().toString()),
                                Integer.parseInt(spinnerDepthInch.getSelectedItem().toString()));
                        franchiseeDetails.setDepthInch(String.valueOf(a));
                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            spinnerDepthInch = (Spinner) view.findViewById(R.id.spinnerDepthInch);
            spinnerDepthInch.setAdapter(inchArrayAdapter);
            spinnerDepthInch.setSelection(getIndexText(spinnerDepthInch, splitDepthFeetAndInches[1]));
            spinnerDepthInch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (spinnerDepthFeet.getSelectedItem().toString().equals("Please Select") &&
                            spinnerDepthInch.getSelectedItem().toString().equals("Please Select")) {
                        franchiseeDetails.setDepthInch(null);
                    } else if (!spinnerDepthFeet.getSelectedItem().toString().equals("Please Select") &&
                            spinnerDepthInch.getSelectedItem().toString().equals("Please Select")) {
                        int a = feetAndInchesToInches(Integer.parseInt(spinnerDepthFeet.getSelectedItem().toString()), 0);
                        franchiseeDetails.setDepthInch(String.valueOf(a));
                    } else if (spinnerDepthFeet.getSelectedItem().toString().equals("Please Select") &&
                            !spinnerDepthInch.getSelectedItem().toString().equals("Please Select")) {
                        int a = feetAndInchesToInches(0, Integer.parseInt(spinnerDepthInch.getSelectedItem().toString()));
                        franchiseeDetails.setDepthInch(String.valueOf(a));
                    } else {
                        int a = feetAndInchesToInches(Integer.parseInt(spinnerDepthFeet.getSelectedItem().toString()),
                                Integer.parseInt(spinnerDepthInch.getSelectedItem().toString()));
                        franchiseeDetails.setDepthInch(String.valueOf(a));
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            String heightFeetAndInches = inchesTofeetAndInches(franchiseeDetails.getCeilingHeightInch() == null ? "0" : franchiseeDetails.getCeilingHeightInch());
            String[] splitHeightFeetAndInches = heightFeetAndInches.split("\\|");

            spinnerHeightFeet = (Spinner) view.findViewById(R.id.spinnerHeightFeet);

            spinnerHeightFeet.setAdapter(feetArrayAdapter);
            spinnerHeightFeet.setSelection(getIndexText(spinnerHeightFeet, splitHeightFeetAndInches[0]));
            spinnerHeightFeet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (spinnerHeightFeet.getSelectedItem().toString().equals("Please Select") &&
                            spinnerHeightInch.getSelectedItem().toString().equals("Please Select")) {
                        franchiseeDetails.setCeilingHeightInch(null);
                    } else if (!spinnerHeightFeet.getSelectedItem().toString().equals("Please Select") &&
                            spinnerHeightInch.getSelectedItem().toString().equals("Please Select")) {
                        int a = feetAndInchesToInches(Integer.parseInt(spinnerHeightFeet.getSelectedItem().toString()), 0);
                        franchiseeDetails.setCeilingHeightInch(String.valueOf(a));
                    } else if (spinnerHeightFeet.getSelectedItem().toString().equals("Please Select") &&
                            !spinnerHeightInch.getSelectedItem().toString().equals("Please Select")) {
                        int a = feetAndInchesToInches(0, Integer.parseInt(spinnerHeightInch.getSelectedItem().toString()));
                        franchiseeDetails.setCeilingHeightInch(String.valueOf(a));
                    } else {
                        int a = feetAndInchesToInches(Integer.parseInt(spinnerHeightFeet.getSelectedItem().toString()),
                                Integer.parseInt(spinnerHeightInch.getSelectedItem().toString()));
                        franchiseeDetails.setCeilingHeightInch(String.valueOf(a));
                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            spinnerHeightInch = (Spinner) view.findViewById(R.id.spinnerHeightInch);
            spinnerHeightInch.setAdapter(inchArrayAdapter);
            spinnerHeightInch.setSelection(getIndexText(spinnerHeightInch, splitHeightFeetAndInches[1]));
            spinnerHeightInch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (spinnerHeightFeet.getSelectedItem().toString().equals("Please Select") &&
                            spinnerHeightInch.getSelectedItem().toString().equals("Please Select")) {
                        franchiseeDetails.setCeilingHeightInch(null);
                    } else if (!spinnerHeightFeet.getSelectedItem().toString().equals("Please Select") &&
                            spinnerHeightInch.getSelectedItem().toString().equals("Please Select")) {
                        int a = feetAndInchesToInches(Integer.parseInt(spinnerHeightFeet.getSelectedItem().toString()), 0);
                        franchiseeDetails.setCeilingHeightInch(String.valueOf(a));
                    } else if (spinnerHeightFeet.getSelectedItem().toString().equals("Please Select") &&
                            !spinnerHeightInch.getSelectedItem().toString().equals("Please Select")) {
                        int a = feetAndInchesToInches(0, Integer.parseInt(spinnerHeightInch.getSelectedItem().toString()));
                        franchiseeDetails.setCeilingHeightInch(String.valueOf(a));
                    } else {
                        int a = feetAndInchesToInches(Integer.parseInt(spinnerHeightFeet.getSelectedItem().toString()),
                                Integer.parseInt(spinnerHeightInch.getSelectedItem().toString()));
                        franchiseeDetails.setCeilingHeightInch(String.valueOf(a));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            String frontageFeetAndInches = inchesTofeetAndInches(franchiseeDetails.getFrontageInch() == null ? "0" : franchiseeDetails.getFrontageInch());
            String[] splitFrontageFeetAndInches = frontageFeetAndInches.split("\\|");

            spinnerFrontageFeet = (Spinner) view.findViewById(R.id.spinnerFrontageFeet);
            spinnerFrontageFeet.setAdapter(feetArrayAdapter);
            spinnerFrontageFeet.setSelection(getIndexText(spinnerFrontageFeet, splitFrontageFeetAndInches[0]));
            spinnerFrontageFeet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (spinnerFrontageFeet.getSelectedItem().toString().equals("Please Select") &&
                            spinnerFrontageInch.getSelectedItem().toString().equals("Please Select")) {
                        franchiseeDetails.setFrontageInch(null);
                    } else if (!spinnerFrontageFeet.getSelectedItem().toString().equals("Please Select") &&
                            spinnerFrontageInch.getSelectedItem().toString().equals("Please Select")) {
                        int a = feetAndInchesToInches(Integer.parseInt(spinnerFrontageFeet.getSelectedItem().toString()), 0);
                        franchiseeDetails.setFrontageInch(String.valueOf(a));
                    } else if (spinnerFrontageFeet.getSelectedItem().toString().equals("Please Select") &&
                            !spinnerFrontageInch.getSelectedItem().toString().equals("Please Select")) {
                        int a = feetAndInchesToInches(0, Integer.parseInt(spinnerFrontageInch.getSelectedItem().toString()));
                        franchiseeDetails.setFrontageInch(String.valueOf(a));
                    } else {
                        int a = feetAndInchesToInches(Integer.parseInt(spinnerFrontageFeet.getSelectedItem().toString()),
                                Integer.parseInt(spinnerFrontageInch.getSelectedItem().toString()));
                        franchiseeDetails.setFrontageInch(String.valueOf(a));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            spinnerFrontageInch = (Spinner) view.findViewById(R.id.spinnerFrontageInch);
            spinnerFrontageInch.setAdapter(inchArrayAdapter);
            spinnerFrontageInch.setSelection(getIndexText(spinnerFrontageInch, splitFrontageFeetAndInches[1]));
            spinnerFrontageInch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (spinnerFrontageFeet.getSelectedItem().toString().equals("Please Select") &&
                            spinnerFrontageInch.getSelectedItem().toString().equals("Please Select")) {
                        franchiseeDetails.setFrontageInch(null);
                    } else if (!spinnerFrontageFeet.getSelectedItem().toString().equals("Please Select") &&
                            spinnerFrontageInch.getSelectedItem().toString().equals("Please Select")) {
                        int a = feetAndInchesToInches(Integer.parseInt(spinnerFrontageFeet.getSelectedItem().toString()), 0);
                        franchiseeDetails.setFrontageInch(String.valueOf(a));
                    } else if (spinnerFrontageFeet.getSelectedItem().toString().equals("Please Select") &&
                            !spinnerFrontageInch.getSelectedItem().toString().equals("Please Select")) {
                        int a = feetAndInchesToInches(0, Integer.parseInt(spinnerFrontageInch.getSelectedItem().toString()));
                        franchiseeDetails.setFrontageInch(String.valueOf(a));
                    } else {
                        int a = feetAndInchesToInches(Integer.parseInt(spinnerFrontageFeet.getSelectedItem().toString()),
                                Integer.parseInt(spinnerFrontageInch.getSelectedItem().toString()));
                        franchiseeDetails.setFrontageInch(String.valueOf(a));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            //Main SignBoard
            String mainSignBoardFeetAndInches = inchesTofeetAndInches(franchiseeDetails.getMainSignboardInch() == null ? "0" : franchiseeDetails.getMainSignboardInch());
            String[] splitMainSignBoardFeetAndInches = mainSignBoardFeetAndInches.split("\\|");

            spinnerMainSignBoardFeet = (Spinner) view.findViewById(R.id.spinnerMainSignBoardFeet);
            spinnerMainSignBoardFeet.setAdapter(feetArrayAdapter);
            spinnerMainSignBoardFeet.setSelection(getIndexText(spinnerMainSignBoardFeet, splitMainSignBoardFeetAndInches[0]));
            spinnerMainSignBoardFeet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (spinnerMainSignBoardFeet.getSelectedItem().toString().equals("Please Select") &&
                            spinnerMainSignBoardInch.getSelectedItem().toString().equals("Please Select")) {
                        franchiseeDetails.setMainSignboardInch(null);
                    } else if (!spinnerMainSignBoardFeet.getSelectedItem().toString().equals("Please Select") &&
                            spinnerMainSignBoardInch.getSelectedItem().toString().equals("Please Select")) {
                        int a = feetAndInchesToInches(Integer.parseInt(spinnerMainSignBoardFeet.getSelectedItem().toString()), 0);
                        franchiseeDetails.setMainSignboardInch(String.valueOf(a));
                    } else if (spinnerMainSignBoardFeet.getSelectedItem().toString().equals("Please Select") &&
                            !spinnerMainSignBoardInch.getSelectedItem().toString().equals("Please Select")) {
                        int a = feetAndInchesToInches(0, Integer.parseInt(spinnerMainSignBoardInch.getSelectedItem().toString()));
                        franchiseeDetails.setMainSignboardInch(String.valueOf(a));
                    } else {
                        int a = feetAndInchesToInches(Integer.parseInt(spinnerMainSignBoardFeet.getSelectedItem().toString()), Integer.parseInt(spinnerMainSignBoardInch.getSelectedItem().toString()));
                        franchiseeDetails.setMainSignboardInch(String.valueOf(a));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            spinnerMainSignBoardInch = (Spinner) view.findViewById(R.id.spinnerMainSignBoardInch);
            spinnerMainSignBoardInch.setAdapter(inchArrayAdapter);
            spinnerMainSignBoardInch.setSelection(getIndexText(spinnerMainSignBoardInch, splitMainSignBoardFeetAndInches[1]));
            spinnerMainSignBoardInch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (spinnerMainSignBoardFeet.getSelectedItem().toString().equals("Please Select") &&
                            spinnerMainSignBoardInch.getSelectedItem().toString().equals("Please Select")) {
                        franchiseeDetails.setMainSignboardInch(null);
                    } else if (!spinnerMainSignBoardFeet.getSelectedItem().toString().equals("Please Select") &&
                            spinnerMainSignBoardInch.getSelectedItem().toString().equals("Please Select")) {
                        int a = feetAndInchesToInches(Integer.parseInt(spinnerMainSignBoardFeet.getSelectedItem().toString()), 0);
                        franchiseeDetails.setMainSignboardInch(String.valueOf(a));
                    } else if (spinnerMainSignBoardFeet.getSelectedItem().toString().equals("Please Select") &&
                            !spinnerMainSignBoardInch.getSelectedItem().toString().equals("Please Select")) {
                        int a = feetAndInchesToInches(0, Integer.parseInt(spinnerMainSignBoardInch.getSelectedItem().toString()));
                        franchiseeDetails.setMainSignboardInch(String.valueOf(a));
                    } else {
                        int a = feetAndInchesToInches(Integer.parseInt(spinnerMainSignBoardFeet.getSelectedItem().toString()),
                                Integer.parseInt(spinnerMainSignBoardInch.getSelectedItem().toString()));
                        franchiseeDetails.setMainSignboardInch(String.valueOf(a));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            //Entrance
            String entranceFeetAndInches = inchesTofeetAndInches(franchiseeDetails.getEntranceInch() == null ? "0" : franchiseeDetails.getEntranceInch());
            String[] splitentranceFeetAndInches = entranceFeetAndInches.split("\\|");

            spinnerEntranceFeet = (Spinner) view.findViewById(R.id.spinnerEntranceFeet);
            spinnerEntranceFeet.setAdapter(feetArrayAdapter);
            spinnerEntranceFeet.setSelection(getIndexText(spinnerEntranceFeet, splitentranceFeetAndInches[0]));
            spinnerEntranceFeet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (spinnerEntranceFeet.getSelectedItem().toString().equals("Please Select") &&
                            spinnerEntranceInch.getSelectedItem().toString().equals("Please Select")) {
                        franchiseeDetails.setEntranceInch(null);
                    } else if (!spinnerEntranceFeet.getSelectedItem().toString().equals("Please Select") &&
                            spinnerEntranceInch.getSelectedItem().toString().equals("Please Select")) {
                        int a = feetAndInchesToInches(Integer.parseInt(spinnerEntranceFeet.getSelectedItem().toString()), 0);
                        franchiseeDetails.setEntranceInch(String.valueOf(a));
                    } else if (spinnerEntranceFeet.getSelectedItem().toString().equals("Please Select") &&
                            !spinnerEntranceInch.getSelectedItem().toString().equals("Please Select")) {
                        int a = feetAndInchesToInches(0, Integer.parseInt(spinnerEntranceInch.getSelectedItem().toString()));
                        franchiseeDetails.setEntranceInch(String.valueOf(a));
                    } else {
                        int a = feetAndInchesToInches(Integer.parseInt(spinnerEntranceFeet.getSelectedItem().toString()), Integer.parseInt(spinnerEntranceInch.getSelectedItem().toString()));
                        franchiseeDetails.setEntranceInch(String.valueOf(a));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            spinnerEntranceInch = (Spinner) view.findViewById(R.id.spinnerEntranceInch);
            spinnerEntranceInch.setAdapter(inchArrayAdapter);
            spinnerEntranceInch.setSelection(getIndexText(spinnerEntranceInch, splitentranceFeetAndInches[1]));
            spinnerEntranceInch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (spinnerEntranceFeet.getSelectedItem().toString().equals("Please Select") &&
                            spinnerEntranceInch.getSelectedItem().toString().equals("Please Select")) {
                        franchiseeDetails.setEntranceInch(null);
                    } else if (!spinnerEntranceFeet.getSelectedItem().toString().equals("Please Select") &&
                            spinnerEntranceInch.getSelectedItem().toString().equals("Please Select")) {
                        int a = feetAndInchesToInches(Integer.parseInt(spinnerEntranceFeet.getSelectedItem().toString()), 0);
                        franchiseeDetails.setEntranceInch(String.valueOf(a));
                    } else if (spinnerEntranceFeet.getSelectedItem().toString().equals("Please Select") &&
                            !spinnerEntranceInch.getSelectedItem().toString().equals("Please Select")) {
                        int a = feetAndInchesToInches(0, Integer.parseInt(spinnerEntranceInch.getSelectedItem().toString()));
                        franchiseeDetails.setEntranceInch(String.valueOf(a));
                    } else {
                        int a = feetAndInchesToInches(Integer.parseInt(spinnerEntranceFeet.getSelectedItem().toString()),
                                Integer.parseInt(spinnerEntranceInch.getSelectedItem().toString()));
                        franchiseeDetails.setEntranceInch(String.valueOf(a));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            AreaSqFeet = (EditText) view.findViewById(R.id.AreaSqFeet);

            if (franchiseeDetails.getAreaSqFeet() != null) {
                AreaSqFeet.setText(franchiseeDetails.getAreaSqFeet(), TextView.BufferType.EDITABLE);
            }


            //Premise Located At
            premiseLocated = (RadioGroup) view.findViewById(R.id.PremiseLocated);
            premiseLocated.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.premiseLocatedAtHighStreet) {
                        franchiseeDetails.setPremiseLocated("0");
                    } else if (checkedId == R.id.premiseLocatedAtMall) {
                        franchiseeDetails.setPremiseLocated("1");
                    }
                }
            });

            premiseLocatedAtHighStreet = (RadioButton) view.findViewById(R.id.premiseLocatedAtHighStreet);
            premiseLocatedAtMall = (RadioButton) view.findViewById(R.id.premiseLocatedAtMall);

            System.out.println("PremiseLocated: " + franchiseeDetails.getPremiseLocated() == null ? "0" : franchiseeDetails.getPremiseLocated());
            if (franchiseeDetails.getPremiseLocated() != null) {
                if (franchiseeDetails.getPremiseLocated().equals("0")) {
                    premiseLocatedAtHighStreet.setChecked(true);
                    premiseLocatedAtMall.setChecked(false);
                } else if (franchiseeDetails.getPremiseLocated().equals("1")) {
                    premiseLocatedAtHighStreet.setChecked(false);
                    premiseLocatedAtMall.setChecked(true);
                }

            }

            spinnerFloor = (Spinner) view.findViewById(R.id.spinnerFloor);
            spinnerFloor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (spinnerFloor.getSelectedItem().toString()) {
                        case "Ground Floor":
                            franchiseeDetails.setPremiseFloor("0");
                            break;
                        case "1st Floor":
                            franchiseeDetails.setPremiseFloor("1");
                            break;
                        case "2nd Floor":
                            franchiseeDetails.setPremiseFloor("2");
                            break;
                        case "3rd Floor or above":
                            franchiseeDetails.setPremiseFloor("3");
                            break;

                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            spinnerFloor.setSelection(franchiseeDetails.getPremiseFloor() == null || franchiseeDetails.getPremiseFloor().isEmpty() ?
                    Integer.valueOf("0") : (Integer.valueOf(franchiseeDetails.getPremiseFloor()) + 1));


            // System.out.println("PremiseFloor: " + franchiseeDetails.getPremiseFloor() == null ? "0" : franchiseeDetails.getPremiseFloor() + 1);
            spinnerStructure = (Spinner) view.findViewById(R.id.spinnerStructure);
            spinnerStructure.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (spinnerStructure.getSelectedItem().toString()) {
                        case "Concrete/Stone/Bricks (Pakka)":
                            franchiseeDetails.setPremiseStructure("0");

                            break;
                        case "Mud (Kacha)":
                            franchiseeDetails.setPremiseStructure("1");


                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            // System.out.println("PremiseStructure: " + franchiseeDetails.getPremiseStructure() == null ? "0" : franchiseeDetails.getPremiseStructure() + 1);
            // spinnerStructure.setSelection(Integer.valueOf(franchiseeDetails.getPremiseStructure() == null ? "0" : franchiseeDetails.getPremiseStructure() + 1));
            spinnerStructure.setSelection(franchiseeDetails.getPremiseStructure() == null || franchiseeDetails.getPremiseStructure().isEmpty() ?
                    Integer.valueOf("0") : (Integer.valueOf(franchiseeDetails.getPremiseStructure()) + 1));

            spinnerRoof = (Spinner) view.findViewById(R.id.spinnerRoof);
            spinnerRoof.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (spinnerRoof.getSelectedItem().toString()) {
                        case "Concrete/Slab":

                            franchiseeDetails.setPremiseRoof("0");
                            break;
                        case "Tin Shed":
                            franchiseeDetails.setPremiseRoof("1");
                            break;
                        case "Roof Tile":
                            franchiseeDetails.setPremiseRoof("2");

                            break;
                        case "Kacha":
                            franchiseeDetails.setPremiseRoof("3");

                            break;
                        case "Others":
                            franchiseeDetails.setPremiseRoof("4");

                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            // spinnerRoof.setSelection(Integer.valueOf(franchiseeDetails.getPremiseRoof() == null ? "0" : franchiseeDetails.getPremiseRoof()));
            spinnerRoof.setSelection(franchiseeDetails.getPremiseRoof() == null || franchiseeDetails.getPremiseRoof().isEmpty() ?
                    Integer.valueOf("0") : (Integer.valueOf(franchiseeDetails.getPremiseRoof()) + 1));


            System.out.println("PremiseRoof: " + franchiseeDetails.getPremiseRoof() == null ? "0" : franchiseeDetails.getPremiseRoof());

            FrontageObstructedYes = (RadioButton) view.findViewById(R.id.FrontageObstructedYes);
            FrontageObstructedNo = (RadioButton) view.findViewById(R.id.FrontageObstructedNo);

            System.out.println("FrontageObstructed: " + franchiseeDetails.getFrontageObstructed() == null ? "0" : franchiseeDetails.getFrontageObstructed());

            if (franchiseeDetails.getFrontageObstructed() != null) {
                if (franchiseeDetails.getFrontageObstructed().equals("1")) {
                    FrontageObstructedYes.setChecked(true);
                    FrontageObstructedNo.setChecked(false);
                } else if (franchiseeDetails.getFrontageObstructed().equals("0")) {
                    FrontageObstructedYes.setChecked(false);
                    FrontageObstructedNo.setChecked(true);
                }

            }

            RadioGroup FrontageObstructed = (RadioGroup) view.findViewById(R.id.FrontageObstructed);
            FrontageObstructed.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.FrontageObstructedYes) {
                        franchiseeDetails.setFrontageObstructed("1");
                    } else if (checkedId == R.id.FrontageObstructedNo) {
                        franchiseeDetails.setFrontageObstructed("0");
                    }
                }
            });

            RadioGroup Foothpath = (RadioGroup) view.findViewById(R.id.Foothpath);
            Foothpath.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.FootpathYes) {
                        franchiseeDetails.setFootpath("1");
                    } else if (checkedId == R.id.FootpathNo) {
                        franchiseeDetails.setFootpath("0");
                    }
                }
            });


            FootpathYes = (RadioButton) view.findViewById(R.id.FootpathYes);
            FootpathNo = (RadioButton) view.findViewById(R.id.FootpathNo);

            System.out.println("Footpath: " + franchiseeDetails.getFootpath());
            if (franchiseeDetails.getFootpath() != null) {
                if (franchiseeDetails.getFootpath().equals("1")) {
                    FootpathYes.setChecked(true);
                    FootpathNo.setChecked(false);
                } else if (franchiseeDetails.getFootpath().equals("0")) {
                    FootpathYes.setChecked(false);
                    FootpathNo.setChecked(true);
                }

            }

            RadioGroup Bathroom = (RadioGroup) view.findViewById(R.id.Bathroom);
            Bathroom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.BathroomYes) {
                        franchiseeDetails.setBathroom("1");
                    } else if (checkedId == R.id.BathroomNo) {
                        franchiseeDetails.setBathroom("0");
                    }
                }
            });


            BathroomYes = (RadioButton) view.findViewById(R.id.BathroomYes);
            BathroomNo = (RadioButton) view.findViewById(R.id.BathroomNo);

            System.out.println("Bathroom: " + franchiseeDetails.getBathroom());
            if (franchiseeDetails.getBathroom() != null) {
                if (franchiseeDetails.getBathroom().equals("1")) {
                    BathroomYes.setChecked(true);
                    BathroomNo.setChecked(false);
                } else if (franchiseeDetails.getBathroom().equals("0")) {
                    BathroomYes.setChecked(false);
                    BathroomNo.setChecked(true);
                }

            }

            //Premise Level

            PremiseLevel = (RadioGroup) view.findViewById(R.id.PremiseLevel);


            RadioGroup Pantry = (RadioGroup) view.findViewById(R.id.Pantry);
            Pantry.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.PantryYes) {
                        franchiseeDetails.setPantry("1");
                    } else if (checkedId == R.id.PantryNo) {
                        franchiseeDetails.setPantry("0");
                    }
                }
            });

            PantryYes = (RadioButton) view.findViewById(R.id.PantryYes);
            PantryNo = (RadioButton) view.findViewById(R.id.PantryNo);

            System.out.println("Bathroom: " + franchiseeDetails.getPantry());
            if (franchiseeDetails.getPantry() != null) {
                if (franchiseeDetails.getPantry().equals("1")) {
                    PantryYes.setChecked(true);
                    PantryNo.setChecked(false);
                } else if (franchiseeDetails.getPantry().equals("0")) {
                    PantryYes.setChecked(false);
                    PantryNo.setChecked(true);
                }

            }

            // -- START Additional Property - As per new requirements
            // Pillers
            RadioGroup Pillers = (RadioGroup) view.findViewById(R.id.Pillers);
            Pillers.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.PillersYes) {
                        franchiseeDetails.setPillers("1");
                    } else if (checkedId == R.id.PillersNo) {
                        franchiseeDetails.setPillers("0");
                    }
                }
            });

            PillersYes = (RadioButton) view.findViewById(R.id.PillersYes);
            PillersNo = (RadioButton) view.findViewById(R.id.PillersNo);

            System.out.println("Pillers: " + franchiseeDetails.getPillers());
            if (franchiseeDetails.getPillers() != null) {
                if (franchiseeDetails.getPillers().equals("1")) {
                    PillersYes.setChecked(true);
                    PillersNo.setChecked(false);
                } else if (franchiseeDetails.getPillers().equals("0")) {
                    PillersYes.setChecked(false);
                    PillersNo.setChecked(true);
                }
            }

            // Windows
            RadioGroup Windows = (RadioGroup) view.findViewById(R.id.Windows);
            Windows.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.WindowsYes) {
                        franchiseeDetails.setWindows("1");
                    } else if (checkedId == R.id.WindowsNo) {
                        franchiseeDetails.setWindows("0");
                    }
                }
            });

            WindowsYes = (RadioButton) view.findViewById(R.id.WindowsYes);
            WindowsNo = (RadioButton) view.findViewById(R.id.WindowsNo);

            System.out.println("Windows: " + franchiseeDetails.getWindows());
            if (franchiseeDetails.getWindows() != null) {
                if (franchiseeDetails.getWindows().equals("1")) {
                    WindowsYes.setChecked(true);
                    WindowsNo.setChecked(false);
                } else if (franchiseeDetails.getWindows().equals("0")) {
                    WindowsYes.setChecked(false);
                    WindowsNo.setChecked(true);
                }
            }

            // Beam
            RadioGroup Beam = (RadioGroup) view.findViewById(R.id.Beam);
            Beam.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.BeamYes) {
                        franchiseeDetails.setBeam("1");
                    } else if (checkedId == R.id.BeamNo) {
                        franchiseeDetails.setBeam("0");
                    }
                }
            });

            BeamYes = (RadioButton) view.findViewById(R.id.BeamYes);
            BeamNo = (RadioButton) view.findViewById(R.id.BeamNo);

            System.out.println("Beam: " + franchiseeDetails.getBeam());
            if (franchiseeDetails.getBeam() != null) {
                if (franchiseeDetails.getBeam().equals("1")) {
                    BeamYes.setChecked(true);
                    BeamNo.setChecked(false);
                } else if (franchiseeDetails.getBeam().equals("0")) {
                    BeamYes.setChecked(false);
                    BeamNo.setChecked(true);
                }
            }

            // AdjacentShops
            RadioGroup AdjacentShops = (RadioGroup) view.findViewById(R.id.AdjacentShops);
            AdjacentShops.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.AdjacentShopsYes) {
                        franchiseeDetails.setAdjacentShops("1");
                    } else if (checkedId == R.id.AdjacentShopsNo) {
                        franchiseeDetails.setAdjacentShops("0");
                    }
                }
            });

            AdjacentShopsYes = (RadioButton) view.findViewById(R.id.AdjacentShopsYes);
            AdjacentShopsNo = (RadioButton) view.findViewById(R.id.AdjacentShopsNo);

            System.out.println("AdjacentShops: " + franchiseeDetails.getAdjacentShops());
            if (franchiseeDetails.getAdjacentShops() != null) {
                if (franchiseeDetails.getAdjacentShops().equals("1")) {
                    AdjacentShopsYes.setChecked(true);
                    AdjacentShopsNo.setChecked(false);
                } else if (franchiseeDetails.getAdjacentShops().equals("0")) {
                    AdjacentShopsYes.setChecked(false);
                    AdjacentShopsNo.setChecked(true);
                }
            }

            // MultipleEntries
            RadioGroup MultipleEntries = (RadioGroup) view.findViewById(R.id.MultipleEntries);
            MultipleEntries.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.MultipleEntriesYes) {
                        franchiseeDetails.setMultipleEntries("1");
                    } else if (checkedId == R.id.MultipleEntriesNo) {
                        franchiseeDetails.setMultipleEntries("0");
                    }
                }
            });

            MultipleEntriesYes = (RadioButton) view.findViewById(R.id.MultipleEntriesYes);
            MultipleEntriesNo = (RadioButton) view.findViewById(R.id.MultipleEntriesNo);

            System.out.println("MultipleEntries: " + franchiseeDetails.getMultipleEntries());
            if (franchiseeDetails.getMultipleEntries() != null) {
                if (franchiseeDetails.getMultipleEntries().equals("1")) {
                    MultipleEntriesYes.setChecked(true);
                    MultipleEntriesNo.setChecked(false);
                } else if (franchiseeDetails.getMultipleEntries().equals("0")) {
                    MultipleEntriesYes.setChecked(false);
                    MultipleEntriesNo.setChecked(true);
                }
            }


            InteriorWorkYTS = (RadioButton) view.findViewById(R.id.InteriorWorkYTS);
            InteriorWorkInProgress = (RadioButton) view.findViewById(R.id.InteriorWorkInProgress);
            InteriorWorkCompleted = (RadioButton) view.findViewById(R.id.InteriorWorkCompleted);
            InteriorWorkApproved = (RadioButton) view.findViewById(R.id.InteriorWorkApproved);

            layoutStartDate = view.findViewById(R.id.layoutStartDate);
            layoutCompletionDate = view.findViewById(R.id.layoutCompletionDate);

            textViewStartDate = view.findViewById(R.id.textViewStartDate);
            textViewCompletionDate = view.findViewById(R.id.textViewCompletionDate);
            textViewStartDateLabel = view.findViewById(R.id.textViewCompletionDate);
            textViewCompletionDateLabel = view.findViewById(R.id.textViewCompletionDate);

            /*textViewStartDate.setVisibility(View.GONE);
            textViewCompletionDate.setVisibility(View.GONE);
            textViewStartDateLabel.setVisibility(View.GONE);
            textViewCompletionDateLabel.setVisibility(View.GONE);*/
            layoutStartDate.setVisibility(View.GONE);
            layoutCompletionDate.setVisibility(View.GONE);

            interiorWorkStatus = (RadioGroup) view.findViewById(R.id.interiorWorkStatus);
            interiorWorkStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.InteriorWorkYTS) {
                        franchiseeDetails.setInteriorWorkStatus("0");
                        layoutStartDate.setVisibility(View.GONE);
                        layoutCompletionDate.setVisibility(View.GONE);
                    } else if (checkedId == R.id.InteriorWorkInProgress) {
                        franchiseeDetails.setInteriorWorkStatus("1");
                        layoutStartDate.setVisibility(View.VISIBLE);
                        layoutCompletionDate.setVisibility(View.VISIBLE);
                        textViewCompletionDateLbl.setText(getContext().getResources().getString(R.string.estimated_completion_date));
                    } else if (checkedId == R.id.InteriorWorkCompleted) {
                        franchiseeDetails.setInteriorWorkStatus("2");
                        layoutStartDate.setVisibility(View.VISIBLE);
                        layoutCompletionDate.setVisibility(View.VISIBLE);
                        textViewCompletionDateLbl.setText(getContext().getResources().getString(R.string.completion_date));
                    } else if (checkedId == R.id.InteriorWorkApproved) {
                        franchiseeDetails.setInteriorWorkStatus("3");
                        layoutStartDate.setVisibility(View.VISIBLE);
                        layoutCompletionDate.setVisibility(View.VISIBLE);
                        textViewCompletionDateLbl.setText(getContext().getResources().getString(R.string.completion_date));
                    }
                }
            });

            System.out.println("getInteriorWorkStatus: " + franchiseeDetails.getInteriorWorkStatus());
            franchiseeDetails.setInteriorWorkStatus((franchiseeDetails.getInteriorWorkStatus() == null ? "0" : franchiseeDetails.getInteriorWorkStatus()));
            if (franchiseeDetails.getInteriorWorkStatus() != null) {
                if (franchiseeDetails.getInteriorWorkStatus().equals("0")) {
                    InteriorWorkYTS.setChecked(true);
                    InteriorWorkInProgress.setChecked(false);
                    InteriorWorkCompleted.setChecked(false);
                    InteriorWorkApproved.setChecked(false);
                   /* textViewStartDate.setVisibility(View.GONE);
                    textViewCompletionDate.setVisibility(View.GONE);
                    textViewStartDateLabel.setVisibility(View.GONE);
                    textViewCompletionDateLabel.setVisibility(View.GONE);*/
                    layoutStartDate.setVisibility(View.GONE);
                    layoutCompletionDate.setVisibility(View.GONE);
                } else if (franchiseeDetails.getInteriorWorkStatus().equals("1")) {
                    InteriorWorkYTS.setChecked(false);
                    InteriorWorkInProgress.setChecked(true);
                    InteriorWorkCompleted.setChecked(false);
                    InteriorWorkApproved.setChecked(false);
                    /*textViewStartDate.setVisibility(View.GONE);
                    textViewCompletionDate.setVisibility(View.VISIBLE);
                    textViewStartDateLabel.setVisibility(View.GONE);
                    textViewCompletionDateLabel.setVisibility(View.VISIBLE);*/
                    layoutStartDate.setVisibility(View.VISIBLE);
                    layoutCompletionDate.setVisibility(View.VISIBLE);
                } else if (franchiseeDetails.getInteriorWorkStatus().equals("2")) {
                    InteriorWorkYTS.setChecked(false);
                    InteriorWorkInProgress.setChecked(false);
                    InteriorWorkCompleted.setChecked(true);
                    InteriorWorkApproved.setChecked(false);
                    /*textViewStartDate.setVisibility(View.VISIBLE);
                    textViewCompletionDate.setVisibility(View.GONE);
                    textViewStartDateLabel.setVisibility(View.VISIBLE);
                    textViewCompletionDateLabel.setVisibility(View.GONE);*/
                    layoutStartDate.setVisibility(View.VISIBLE);
                    layoutCompletionDate.setVisibility(View.VISIBLE);
                } else if (franchiseeDetails.getInteriorWorkStatus().equals("3")) {
                    InteriorWorkYTS.setChecked(false);
                    InteriorWorkInProgress.setChecked(false);
                    InteriorWorkCompleted.setChecked(false);
                    InteriorWorkApproved.setChecked(true);
                    /*textViewStartDate.setVisibility(View.VISIBLE);
                    textViewCompletionDate.setVisibility(View.GONE);
                    textViewStartDateLabel.setVisibility(View.VISIBLE);
                    textViewCompletionDateLabel.setVisibility(View.GONE);*/
                    layoutStartDate.setVisibility(View.VISIBLE);
                    layoutCompletionDate.setVisibility(View.VISIBLE);
                }
            }


            textViewStartDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedDateTimeId = v.getId();
                    showDateTimeDialogPicker();
                }
            });
            textViewCompletionDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedDateTimeId = v.getId();
                    showDateTimeDialogPicker();
                }
            });

            textViewWidthInfo = view.findViewById(R.id.textViewWidthInfo);
            textViewDepthInfo = view.findViewById(R.id.textViewDepthInfo);
            textViewHeightInfo = view.findViewById(R.id.textViewHeightInfo);
            textViewSignboardInfo = view.findViewById(R.id.textViewSignboardInfo);
            textViewEntranceInfo = view.findViewById(R.id.textViewEntranceInfo);

            textViewWidthInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showImageDialog(R.drawable.kendra_photo);
                }
            });
            textViewDepthInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showImageDialog(R.drawable.kendra_photo);
                }
            });
            textViewHeightInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showImageDialog(R.drawable.kendra_photo);
                }
            });
            textViewSignboardInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showImageDialog(R.drawable.kendra_photo_board);
                }
            });
            textViewEntranceInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showImageDialog(R.drawable.kendra_photo);
                }
            });

            try {

                // Set Start and Estimated End Date
                strStartDate = franchiseeDetails.getCompletionDate();
                if (!TextUtils.isEmpty(strStartDate)) {
                    startDate = dateFormatter.parse(strStartDate);
                    String tempStartDate = CommonUtils.getFormattedDate("yyyy-MM-dd HH:mm:ss", "dd-MM-yyyy", strStartDate);
                    textViewStartDate.setText(tempStartDate); //dateFormatter2.format(strStartDate);
                }

                strCompletionDate = franchiseeDetails.getExpectedCompletionDate();
                if (!TextUtils.isEmpty(strCompletionDate)) {
                    completionDate = dateFormatter.parse(strCompletionDate);
                    String tempCompletionDate = CommonUtils.getFormattedDate("yyyy-MM-dd HH:mm:ss", "dd-MM-yyyy", strCompletionDate);
                    textViewCompletionDate.setText(tempCompletionDate); //dateFormatter2.format(completionDate)
                }

            } catch (Exception pe) {
                pe.toString();
            }

            // -- END Additional Properties - As per new requirement 10th July 2018

            roadLevel = (RadioButton) view.findViewById(R.id.roadLevel);
            belowRoad = (RadioButton) view.findViewById(R.id.belowRoad);

            PremiseLevel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.roadLevel) {
                        franchiseeDetails.setPremiseLevel("0");
                    } else if (checkedId == R.id.belowRoad) {
                        franchiseeDetails.setPremiseLevel("1");
                    }

                }
            });
            System.out.println("PremiseLevel: " + franchiseeDetails.getPremiseLevel());
            if (franchiseeDetails.getPremiseLevel() != null) {
                if (franchiseeDetails.getPremiseLevel().equals("0")) {
                    roadLevel.setChecked(true);
                    belowRoad.setChecked(false);
                } else if (franchiseeDetails.getPremiseLevel().equals("1")) {
                    roadLevel.setChecked(false);
                    belowRoad.setChecked(true);
                }

            }

            //Address of Location Visited
            SiteVisitAddress1 = (EditText) view.findViewById(R.id.SiteVisitAddress1);
            if (franchiseeDetails.getSiteVisitAddress1() != null) {
                String address1 = CommonUtils.toTitleCase(franchiseeDetails.getSiteVisitAddress1());
                SiteVisitAddress1.setText(address1, TextView.BufferType.EDITABLE);
            }

            SiteVisitAddress2 = (EditText) view.findViewById(R.id.SiteVisitAddress2);
            if (franchiseeDetails.getSiteVisitAddress2() != null) {
                String address2 = CommonUtils.toTitleCase(franchiseeDetails.getSiteVisitAddress2());
                SiteVisitAddress2.setText(address2, TextView.BufferType.EDITABLE);
            }

            SiteVisitLandmark = (EditText) view.findViewById(R.id.SiteVisitLandmark);
            if (franchiseeDetails.getSiteVisitLandmark() != null) {
                String landMark = CommonUtils.toTitleCase(franchiseeDetails.getSiteVisitLandmark());
                SiteVisitLandmark.setText(landMark, TextView.BufferType.EDITABLE);
            }

            stateSpinner = (Spinner) view.findViewById(R.id.SpinnerCountryActivity_country_spinner);

            districtSpinner = (Spinner) view.findViewById(R.id.SpinnerCountryActivity_state_spinner);
            villageSpinner = (Spinner) view.findViewById(R.id.SpinnerCountryActivity_city_spinner);


            wardNoTextView = (TextView) view.findViewById(R.id.wardNoTextView);
            SiteVisitWardNo = (EditText) view.findViewById(R.id.SiteVisitWardNo);
            if (franchiseeDetails.getSiteVisitWardNo() != null) {
                SiteVisitWardNo.setText(franchiseeDetails.getSiteVisitWardNo(), TextView.BufferType.EDITABLE);
            }

            SiteVisitWardNo.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // Not Required.
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    /*if (start != 1) {
                        // Toast.makeText(getApplicationContext(), "Please Enter 10 digits", Toast.LENGTH_SHORT).show();
                        SiteVisitWardNo.setTextColor(Color.parseColor("#000000"));
                        SiteVisitWardNo.setError("Enter atleast 1 digit Ward No.");
                    }*/
                }

                @Override
                public void afterTextChanged(Editable s) {
                    System.out.println("SiteVisitWardNo.length(): " + SiteVisitWardNo.length());

                    if ((SiteVisitWardNo.length() == 0)) {
                        SiteVisitWardNo.setTextColor(Color.parseColor("#468847"));
                        SiteVisitWardNo.setError(null);

                    } else if (SiteVisitWardNo.length() <= 1) {

                    } else {
                        SiteVisitWardNo.setTextColor(Color.parseColor("#468847"));
                        SiteVisitWardNo.setError(null);
                    }
                }
            });

            SiteVisitPincode = (EditText) view.findViewById(R.id.SiteVisitPincode);
            if (franchiseeDetails.getSiteVisitPincode() != null) {
                SiteVisitPincode.setText(franchiseeDetails.getSiteVisitPincode(), TextView.BufferType.EDITABLE);
            }

            SiteVisitPincode.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (i != 6) {
                        // Toast.makeText(getApplicationContext(), "Please Enter 10 digits", Toast.LENGTH_SHORT).show();
                        SiteVisitPincode.setTextColor(Color.parseColor("#000000"));
                        SiteVisitPincode.setError("Enter 6 digits Pincode");

                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    {
                        System.out.println("SiteVisitPincode.length(): " + SiteVisitPincode.length());

                        if ((SiteVisitPincode.length() == 0)) {
                            SiteVisitPincode.setTextColor(Color.parseColor("#468847"));
                            SiteVisitPincode.setError(null);

                        } else if (SiteVisitPincode.length() <= 5) {


                        } else {
                            SiteVisitPincode.setTextColor(Color.parseColor("#468847"));
                            SiteVisitPincode.setError(null);

                        }
                    }
                }
            });


            //Closest Branch
            closestBankBranch1 = (Spinner) view.findViewById(R.id.closestBankBranch1);
            closestBankBranch2 = (Spinner) view.findViewById(R.id.closestBankBranch2);
            closestBankBranch3 = (Spinner) view.findViewById(R.id.closestBankBranch3);
            new ClosestBankBranchAsyncTask(getContext()).execute();


            closestBankBranch1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position > 0) {

                        final Bank bank = (Bank) closestBankBranch1.getItemAtPosition(position);
                        Log.d("SpinnerDistrict", "onItemSelected: district id: " + bank.getNbinCode());
                        franchiseeDetails.setClosestBankBranch1NbinCode(bank.getNbinCode());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            closestBankBranch2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position > 0) {

                        final Bank bank = (Bank) closestBankBranch2.getItemAtPosition(position);
                        Log.d("SpinnerDistrict", "onItemSelected: district id: " + bank.getNbinCode());
                        franchiseeDetails.setClosestBankBranch2NbinCode(bank.getNbinCode());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            closestBankBranch3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position > 0) {

                        final Bank bank = (Bank) closestBankBranch3.getItemAtPosition(position);
                        Log.d("SpinnerDistrict", "onItemSelected: district id: " + bank.getNbinCode());
                        franchiseeDetails.setClosestBankBranch3NbinCode(bank.getNbinCode());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            //Closest ATM
            closestATM1 = (Spinner) view.findViewById(R.id.closestATM1);
            closestATM2 = (Spinner) view.findViewById(R.id.closestATM2);
            closestATM3 = (Spinner) view.findViewById(R.id.closestATM3);
            new ClosestATMAsyncTask(getContext()).execute();


            closestATM1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position > 0) {

                        final Bank bank = (Bank) closestATM1.getItemAtPosition(position);
                        Log.d("SpinnerDistrict", "onItemSelected: district id: " + bank.getNbinCode());
                        franchiseeDetails.setClosestAtm1NbinCode(bank.getNbinCode());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            closestATM2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position > 0) {

                        final Bank bank = (Bank) closestATM2.getItemAtPosition(position);
                        Log.d("SpinnerDistrict", "onItemSelected: district id: " + bank.getNbinCode());
                        franchiseeDetails.setClosestAtm2NbinCode(bank.getNbinCode());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            closestATM3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position > 0) {
                        final Bank bank = (Bank) closestATM3.getItemAtPosition(position);
                        Log.d("SpinnerDistrict", "onItemSelected: district id: " + bank.getNbinCode());
                        franchiseeDetails.setClosestAtm3NbinCode(bank.getNbinCode());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            // Remarks
            Remarks = (EditText) view.findViewById(R.id.editTextRemarks);
            if (franchiseeDetails != null && TextUtils.isEmpty(franchiseeDetails.getFranchiseeComments())) {
                Remarks.setText(franchiseeDetails.getFranchiseeComments());
            }

            recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);


            premiseShapes = new ArrayList<>();

            //Adding Data into ArrayList
            premiseShapes.add(new PremiseShape("1", R.drawable.vk01));
            premiseShapes.add(new PremiseShape("2", R.drawable.horizontal_rectangle));
            premiseShapes.add(new PremiseShape("3", R.drawable.vertical_rectangle));
            premiseShapes.add(new PremiseShape("4", R.drawable.other));
            //premiseShapes.add(new PremiseShape("2", R.drawable.vk02));
            //premiseShapes.add(new PremiseShape("3", R.drawable.vk03));
           /* premiseShapes.add(new PremiseShape("4", R.drawable.vk04));
            premiseShapes.add(new PremiseShape("5", R.drawable.vk05));
            premiseShapes.add(new PremiseShape("6", R.drawable.vk06));
            premiseShapes.add(new PremiseShape("7", R.drawable.vk07));
            premiseShapes.add(new PremiseShape("8", R.drawable.vk08));
            premiseShapes.add(new PremiseShape("9", R.drawable.vk09));
            premiseShapes.add(new PremiseShape("10", R.drawable.vk10));*/

            mAdapter = new CustomRecyclerAdapter(getContext(), premiseShapes, franchiseeDetails);
            recyclerView.setAdapter(mAdapter);


//            String stateSpineer =franchiseeDetails.getState() == null ? "0" : franchiseeDetails.getState();
//
//            stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    if (stateSpinner.getSelectedItem().toString().equals("Please Select")) {
//                        franchiseeDetails.setState(null);
//                    } else {
//                        String a = stateSpinner.getSelectedItem().toString();
//                        franchiseeDetails.setState(String.valueOf(a));
//                    }
//
//
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });


            stateSpinner.setOnItemSelectedListener(stateListener);
            districtSpinner.setOnItemSelectedListener(districtListener);
            villageSpinner.setOnItemSelectedListener(villageListener);

            new StateAsyncTask(getContext()).execute();

            // Check Status and Remarks
            checkStatusAndRemarks();

            // Interior Work Status
            view.findViewById(R.id.layoutInteriorWorkStatus);

            //Hide Main Sign board, Interior Work Status layout       ---Added on 11th Oct, 2018
            if (getActivity().getClass().getSimpleName().equalsIgnoreCase("NextGenPhotoViewPager")) {
                IsToBeRemoved = Constants.IsToBeRemoved;
            }

            if (IsToBeRemoved) {
                layoutInteriorWorkStatus.setVisibility(View.GONE);
                layoutCompleteAddress.setVisibility(View.GONE);
                layoutMainSignBoardDetails.setVisibility(View.GONE);
                //txtCarpetAreaLbl.setText("5) Carpet Area (W X D)");
            } else {
                layoutInteriorWorkStatus.setVisibility(View.VISIBLE);
                layoutCompleteAddress.setVisibility(View.VISIBLE);
                layoutMainSignBoardDetails.setVisibility(View.VISIBLE);
                //txtCarpetAreaLbl.setText("6) Carpet Area (W X D)");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return view;
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
                franchiseeDetails.setSiteVisitReviewed(b);
                ((NextGenPhotoViewPager) getActivity()).setFranchiseeDetails(franchiseeDetails);

            }
        });
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
                    franchiseeDetails.setSiteLayoutSketch(imageDto.getImageBase64());
                    imgOtherSketch.setImageBitmap(imageDto.getBitmap());

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
                    try {
                        dispatchTakePictureIntent();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

/*
                    File file = CommonUtils.getOutputMediaFile(CommonUtils.FILE_IMAGE_TYPE);
                    Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    picUri = Uri.fromFile(file); // create
                    i.putExtra(MediaStore.EXTRA_OUTPUT, picUri); // set the image file
                    i.putExtra("ImageId", picUri); // set the image file
                    startActivityForResult(i, CAMERA_PIC_REQUEST);*/
                }
            }
        });
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
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
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
                picUri = FileProvider.getUriForFile(getContext(), getContext().getApplicationContext().getPackageName() + ".provider", photoFile);
                // picUri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
                startActivityForResult(takePictureIntent, CAMERA_PIC_REQUEST);
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!NextGenViewPager.needToShowAlert)
            return;

        if (isResumed() && isVisibleToUser) { // fragment have created
            if (franchiseeDetails != null && franchiseeDetails.getStatus() == NextGenViewPager.SITE_SEND_BACK_FOR_CORRECTION) {
                FranchiseeRemarkDetails franchiseeRemarkDetails = franchiseeDetails.getFranchiseeRemarkDetails();
                if (franchiseeRemarkDetails != null && NextGenViewPager.needToShowAlert) {
                    showAlertOkMessage("For more information, click on icon next to each label.");
                    NextGenViewPager.needToShowAlert = false;
                }
            }
        }
    }

    //region Check Status and Remarks and Perform Action
    public void checkStatusAndRemarks() {
        // Check Allow to Update Site Visit Data or not.
        if (!franchiseeDetails.isAllowToEdit()) {
            GUIUtils.setViewAndChildrenEnabled(scrollViewNextGenSiteVisit, false);
            ((CustomRecyclerAdapter) mAdapter).setEnable(false);
        }

        // Check Status and Set Remarks
        if (franchiseeDetails.getStatus() == NextGenViewPager.SITE_SEND_BACK_FOR_CORRECTION) {
            FranchiseeRemarkDetails franchiseeRemarkDetails = franchiseeDetails.getFranchiseeRemarkDetails();
            if (franchiseeRemarkDetails != null) {

                // Premise Detail
                final String premiseDetailRemarks = franchiseeRemarkDetails.getPremiseDetailRemarks();
                if (!TextUtils.isEmpty(premiseDetailRemarks)) {
                    tooltipPremiseDetail.setVisibility(View.VISIBLE);
                    tooltipPremiseDetail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tooltipPremiseDetail.setAnimation(btnClickAnim);
                            showTooltip(v, premiseDetailRemarks);
                        }
                    });
                }

                // Premise Located At
                final String premiseLocatedAtRemarks = franchiseeRemarkDetails.getPremiseLocatedAtRemarks();
                if (!TextUtils.isEmpty(premiseLocatedAtRemarks)) {
                    tooltipPremiseLocatedAt.setVisibility(View.VISIBLE);
                    tooltipPremiseLocatedAt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tooltipPremiseLocatedAt.setAnimation(btnClickAnim);
                            showTooltip(v, premiseLocatedAtRemarks);
                        }
                    });
                }

                // Address Of Location Visited
                final String addressOfLocationVisitedRemarks = franchiseeRemarkDetails.getAddressOfLocationVisitedRemarks();
                if (!TextUtils.isEmpty(addressOfLocationVisitedRemarks)) {
                    tooltipAddressOfLocationVisited.setVisibility(View.VISIBLE);
                    tooltipAddressOfLocationVisited.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tooltipAddressOfLocationVisited.setAnimation(btnClickAnim);
                            showTooltip(v, addressOfLocationVisitedRemarks);
                        }
                    });
                }

                // Premise Level
                final String premiseLevelRemarks = franchiseeRemarkDetails.getPremiseLevelRemarks();
                if (!TextUtils.isEmpty(premiseLevelRemarks)) {
                    tooltipPremiseLevel.setVisibility(View.VISIBLE);
                    tooltipPremiseLevel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tooltipPremiseLevel.setAnimation(btnClickAnim);
                            showTooltip(v, premiseLevelRemarks);
                        }
                    });
                }

                // Premise Shape
                final String premiseShapeRemarks = franchiseeRemarkDetails.getPremiseShapeRemarks();
                if (!TextUtils.isEmpty(premiseShapeRemarks)) {
                    tooltipPremiseShape.setVisibility(View.VISIBLE);
                    tooltipPremiseShape.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tooltipPremiseShape.setAnimation(btnClickAnim);
                            showTooltip(v, premiseShapeRemarks);
                        }
                    });
                }

                // Closest Bank Branch
                final String premiseClosestBankBranchRemarks = franchiseeRemarkDetails.getClosestBankBranchRemarks();
                if (!TextUtils.isEmpty(premiseClosestBankBranchRemarks)) {
                    tooltipClosestBankBranch.setVisibility(View.VISIBLE);
                    tooltipClosestBankBranch.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tooltipClosestBankBranch.setAnimation(btnClickAnim);
                            showTooltip(v, premiseClosestBankBranchRemarks);
                        }
                    });
                }

                // Closest Bank ATM
                final String premiseClosestBankATMRemarks = franchiseeRemarkDetails.getClosestBankATMRemarks();
                if (!TextUtils.isEmpty(premiseClosestBankATMRemarks)) {
                    tooltipClosestATM.setVisibility(View.VISIBLE);
                    tooltipClosestATM.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tooltipClosestATM.setAnimation(btnClickAnim);
                            showTooltip(v, premiseClosestBankATMRemarks);
                        }
                    });
                }

                //Interior work status
                final String interiorWorkStatus = franchiseeRemarkDetails.getInteriorWorkStatus();
                if (!TextUtils.isEmpty(interiorWorkStatus)) {
                    tooltipInteriorWork.setVisibility(View.VISIBLE);
                    tooltipInteriorWork.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tooltipInteriorWork.setAnimation(btnClickAnim);
                            showTooltip(v, interiorWorkStatus);
                        }
                    });
                }

                //provisional main signbaord
                final String provisionalMainSignborad = franchiseeRemarkDetails.getProvisionalMainSignborad();
                if (!TextUtils.isEmpty(provisionalMainSignborad)) {
                    tooltipProvisionalSignBoard.setVisibility(View.VISIBLE);
                    tooltipProvisionalSignBoard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tooltipProvisionalSignBoard.setAnimation(btnClickAnim);
                            showTooltip(v, provisionalMainSignborad);
                        }
                    });
                }


            }
        }
    }
    //endregion

    public void showTooltip(View anchor, String tooltipText) {
        // Toast.makeText(getActivity(), Html.fromHtml(tooltipText), Toast.LENGTH_SHORT).show();
        PopupUtils.show(getActivity(), anchor, tooltipText);
    }

    public class PremiseShape {

        private String personName;
        private int jobProfile;

        public PremiseShape(String personName, int jobProfile) {
            this.personName = personName;
            this.jobProfile = jobProfile;
        }

        public String getPersonName() {
            return personName;
        }

        public void setPersonName(String personName) {
            this.personName = personName;
        }

        public int getJobProfile() {
            return jobProfile;
        }

        public void setJobProfile(int jobProfile) {
            this.jobProfile = jobProfile;
        }
    }

    private AdapterView.OnItemSelectedListener stateListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position > 0) {

                final State state = (State) stateSpinner.getItemAtPosition(position);
                Log.d("SpinnerState", "state id: " + state.getStateId());
                selectedState = state.getStateId();
                franchiseeDetails.setSiteVisitState(selectedState);
                new DistrictAsyncTask(getContext()).execute();


            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemSelectedListener districtListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position > 0) {

                final District district = (District) districtSpinner.getItemAtPosition(position);
                Log.d("SpinnerDistrict", "onItemSelected: district id: " + district.getDistrictId());
                selectedDistrict = district.getDistrictId();
                franchiseeDetails.setSiteVisitDistrict(selectedDistrict);
                new VillageAsyncTask(getContext()).execute();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemSelectedListener villageListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position > 0) {

                final Village village = (Village) villageSpinner.getItemAtPosition(position);
                Log.d("SpinnerVillage", "onItemSelected: village id: " + village.getVillageId());
                selectedVillage = village.getVillageId();
                franchiseeDetails.setSiteVisitVTC(selectedVillage);
                new IsRuralAsyncTask(getContext()).execute();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    public class StateAsyncTask extends AsyncTask<Void, Void, Void> {
        String TAG = "Response";
        String diplayServerResopnse;
        private Context context;
        private TelephonyManager telephonyManager;
        public ServiceProviderIfc delegate = null;

        public StateAsyncTask(Context context) {

            // this.soapPrimitive = context1;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }


        @Override
        protected Void doInBackground(Void... voids) {
            Log.i(TAG, "doInBackground");
            getAvailableBalance();
            Log.i("", "Some not null string");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");

            try {
                if (diplayServerResopnse.equals("Invalid Request.")) {
                    //getBalance.setText("Invalid Request.");
                    Log.e(TAG + "Invalid Request.", diplayServerResopnse);
                    delegate.getAvailableBalance(diplayServerResopnse);
                }

                StringTokenizer tokens = new StringTokenizer(diplayServerResopnse, "|");
                String first = tokens.nextToken();
                String second = "{" +
                        "\"state\":" + tokens.nextToken() + "}";
                try {
                    arrayListStates.removeAll(arrayListStates);
                    JSONObject jsnobject = new JSONObject(second);


                    JSONArray jsonArray = jsnobject.getJSONArray("state");
                    arrayListStates.add(new State("", "Please Select"));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String a = obj.getString("lg_state_id");
                        String b = obj.getString("state_name");
//                        System.out.println("State: " + a + " " + b);
                        arrayListStates.add(new State(a, b));
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
                ArrayAdapter<State> countryArrayAdapter = new ArrayAdapter<State>(getActivity(), android.R.layout.simple_spinner_dropdown_item, arrayListStates);
                countryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                stateSpinner.setAdapter(countryArrayAdapter);
                if (franchiseeDetails.getSiteVisitState() != null) {
                    stateSpinner.setSelection(getIndexValueState(stateSpinner,
                            franchiseeDetails.getSiteVisitState()));
                }


            } catch (Exception e) {
                Log.d(TAG, "Error" + e);
                // AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));

            }
        }

        private void getAvailableBalance() {
            telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Connection connection = new Connection(context);
            String getVkid = connection.getVkid();
            String getTokenId = connection.getTokenId();

            String vkid = EncryptionUtil.encryptString(getVkid, context);
            String token = EncryptionUtil.encryptString(getTokenId, context);

            final String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            String deviceid = EncryptionUtil.encryptString(deviceId, context);
            String deviceIDAndroid = CommonUtils.getAndroidUniqueID(context);
            String imei = EncryptionUtil.encryptString(deviceIDAndroid, context);
            String simSerial = CommonUtils.getSimSerialNumber(context);
            String simopertaor = EncryptionUtil.encryptString(simSerial, context);
            try {
                if (isAdhoc) {
                    diplayServerResopnse = WebService.getState(getVkid);
                } else {
                    diplayServerResopnse = WebService.getState(vkid, token, imei, deviceid, simopertaor);

                }
                // Log.d(TAG, "WebSer...ceResponse: " + diplayServerResopnse);
            } catch (Exception e) {
                // AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                e.printStackTrace();

            }

        }
    }

    public class DistrictAsyncTask extends AsyncTask<Void, Void, Void> {
        String TAG = "Response";
        String diplayServerResopnse;
        private Context context;
        private TelephonyManager telephonyManager;
        public ServiceProviderIfc delegate = null;

        public DistrictAsyncTask(Context context) {

            // this.soapPrimitive = context1;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }


        @Override
        protected Void doInBackground(Void... voids) {
            Log.i(TAG, "doInBackground");
            telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Connection connection = new Connection(context);
            String getVkid = connection.getVkid();
            String getTokenId = connection.getTokenId();

            String vkid = EncryptionUtil.encryptString(getVkid, context);
            String token = EncryptionUtil.encryptString(getTokenId, context);
            final String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            String deviceid = EncryptionUtil.encryptString(deviceId, context);
            String deviceIDAndroid = CommonUtils.getAndroidUniqueID(context);
            String imei = EncryptionUtil.encryptString(deviceIDAndroid, context);
            String simSerial = CommonUtils.getSimSerialNumber(context);
            String simopertaor = EncryptionUtil.encryptString(simSerial, context);
            String stateid = selectedState;

            try {
                if (isAdhoc) {
                    diplayServerResopnse = WebService.getDistrict(getVkid, stateid);
                } else {
                    diplayServerResopnse = WebService.getDistrict(vkid, token, imei, deviceid, simopertaor, EncryptionUtil.encryptString(stateid, context));
                }
                //Log.d(TAG, "WebSer...ceResponse: " + diplayServerResopnse);
            } catch (Exception e) {
                // AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                e.printStackTrace();

            }

            Log.i("", "Some not null string");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");

            try {
                if (diplayServerResopnse.equals("Invalid Request.")) {
                    //getBalance.setText("Invalid Request.");
                    Log.e(TAG + "Invalid Request.", diplayServerResopnse);
                    delegate.getAvailableBalance(diplayServerResopnse);
                }


                StringTokenizer tokens = new StringTokenizer(diplayServerResopnse, "|");
                String first = tokens.nextToken();
                String second = "{" +
                        "\"District\":" + tokens.nextToken() + "}";
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


                ArrayAdapter<District> countryArrayAdapter = new ArrayAdapter<District>(getActivity(), android.R.layout.simple_spinner_dropdown_item, arrayListDistrict);
                countryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                districtSpinner.setAdapter(countryArrayAdapter);

                if (franchiseeDetails.getSiteVisitState() != null &&
                        franchiseeDetails.getSiteVisitDistrict() != null) {
                    districtSpinner.setSelection(getIndexValueDistrict(districtSpinner,
                            franchiseeDetails.getSiteVisitDistrict()));
                }

            } catch (Exception e) {
                Log.d(TAG, "Error" + e);
                // AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));

            }
        }

    }

    public class VillageAsyncTask extends AsyncTask<Void, Void, Void> {
        String TAG = "Response";
        String displayServerResopnse;
        private Context context;
        private TelephonyManager telephonyManager;
        public ServiceProviderIfc delegate = null;

        public VillageAsyncTask(Context context) {

            // this.soapPrimitive = context1;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }


        @Override
        protected Void doInBackground(Void... voids) {
            Log.i(TAG, "doInBackground");
            //  arrayListDistrict.clear();
            telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Connection connection = new Connection(context);
            String getVkid = connection.getVkid();
            String getTokenId = connection.getTokenId();

            String vkid = EncryptionUtil.encryptString(getVkid, context);
            String token = EncryptionUtil.encryptString(getTokenId, context);
            final String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            String deviceid = EncryptionUtil.encryptString(deviceId, context);
            String deviceIDAndroid = CommonUtils.getAndroidUniqueID(context);
            String imei = EncryptionUtil.encryptString(deviceIDAndroid, context);
            String simSerial = CommonUtils.getSimSerialNumber(context);
            String simopertaor = EncryptionUtil.encryptString(simSerial, context);
//            String stateId = selectedState;
//            String districtId = selectedDistrict;

            try {
                if (isAdhoc) {
                    displayServerResopnse = WebService.getVillage(vkid, selectedState, selectedDistrict);
                } else {
                    displayServerResopnse = WebService.getVillage(vkid, token, imei, deviceid, simopertaor,
                            EncryptionUtil.encryptString(selectedState, context),
                            EncryptionUtil.encryptString(selectedDistrict, context)
                    );
                }

                //Log.d(TAG, "WebSer...ceResponse: " + displayServerResopnse);
            } catch (Exception e) {
                // AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                e.printStackTrace();

            }

            Log.i("", "Some not null string");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");

            try {
                if (displayServerResopnse.equals("Invalid Request.")) {
                    //getBalance.setText("Invalid Request.");
                    Log.e(TAG + "Invalid Request.", displayServerResopnse);
                    delegate.getAvailableBalance(displayServerResopnse);
                }

                StringTokenizer tokens = new StringTokenizer(displayServerResopnse, "|");
                String first = tokens.nextToken();
                String second = "{" +
                        "\"Village\":" + tokens.nextToken() + "}";
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
//                System.out.println("arrayListVillage count: " + arrayListVillage.size());

                ArrayAdapter<Village> villageArrayAdapter = new ArrayAdapter<Village>(getActivity(),
                        android.R.layout.simple_spinner_dropdown_item, arrayListVillage);

                villageArrayAdapter.setDropDownViewResource(
                        android.R.layout.simple_spinner_dropdown_item);
                villageSpinner.setAdapter(villageArrayAdapter);
                if (franchiseeDetails.getSiteVisitState() != null &&
                        franchiseeDetails.getSiteVisitDistrict() != null
                        && franchiseeDetails.getSiteVisitVTC() != null) {
                    villageSpinner.setSelection(getIndexValueVillage(villageSpinner,
                            franchiseeDetails.getSiteVisitVTC()));
                }

            } catch (Exception e) {
                Log.d(TAG, "Error" + e);
                // AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));

            }
        }

    }

    public class ClosestATMAsyncTask extends AsyncTask<Void, Void, Void> {
        String TAG = "Response";
        String diplayServerResopnse;
        private Context context;
        private TelephonyManager telephonyManager;
        public ServiceProviderIfc delegate = null;

        public ClosestATMAsyncTask(Context context) {

            // this.soapPrimitive = context1;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }


        @Override
        protected Void doInBackground(Void... voids) {
            Log.i(TAG, "doInBackground");
            getAvailableBalance();
            Log.i("", "Some not null string");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");

            try {
                if (diplayServerResopnse.equals("Invalid Request.")) {
                    //getBalance.setText("Invalid Request.");
                    Log.e(TAG + "Invalid Request.", diplayServerResopnse);
                    delegate.getAvailableBalance(diplayServerResopnse);
                }

                StringTokenizer tokens = new StringTokenizer(diplayServerResopnse, "|");
                String first = tokens.nextToken();
                String second = "{" +
                        "\"bank\":" + tokens.nextToken() + "}";
                try {
                    JSONObject jsnobject = new JSONObject(second);


                    JSONArray jsonArray = jsnobject.getJSONArray("bank");
                    arrayListATM.add(new Bank("", "Please Select"));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String a = obj.getString("nbin_code");
                        String b = obj.getString("bank_name");
//                        System.out.println("State: " + a + " " + b);
                        arrayListATM.add(new Bank(a, b));
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
                ArrayAdapter<Bank> atmArrayAdapter = new ArrayAdapter<Bank>(getActivity(),
                        android.R.layout.simple_spinner_dropdown_item, arrayListATM);
                atmArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                closestATM1.setAdapter(atmArrayAdapter);
                closestATM2.setAdapter(atmArrayAdapter);
                closestATM3.setAdapter(atmArrayAdapter);
                if (franchiseeDetails.getClosestAtm1NbinCode() != null) {
                    closestATM1.setSelection(getIndexValueBank(closestATM1,
                            franchiseeDetails.getClosestAtm1NbinCode()));
                }

                if (franchiseeDetails.getClosestAtm2NbinCode() != null) {
                    closestATM2.setSelection(getIndexValueBank(closestATM2,
                            franchiseeDetails.getClosestAtm2NbinCode()));
                }

                if (franchiseeDetails.getClosestAtm3NbinCode() != null) {
                    closestATM3.setSelection(getIndexValueBank(closestATM3,
                            franchiseeDetails.getClosestAtm3NbinCode()));
                }


            } catch (Exception e) {
                Log.d(TAG, "Error" + e);
                // AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));

            }
        }

        private void getAvailableBalance() {
            telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Connection connection = new Connection(context);
            String getVkid = connection.getVkid();
            String getTokenId = connection.getTokenId();

            String vkid = EncryptionUtil.encryptString(getVkid, context);
            String token = EncryptionUtil.encryptString(getTokenId, context);
            final String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            String deviceid = EncryptionUtil.encryptString(deviceId, context);
            String deviceIDAndroid = CommonUtils.getAndroidUniqueID(context);
            String imei = EncryptionUtil.encryptString(deviceIDAndroid, context);
            String simSerial = CommonUtils.getSimSerialNumber(context);
            String simopertaor = EncryptionUtil.encryptString(simSerial, context);
            try {
                if (isAdhoc) {
                    diplayServerResopnse = WebService.getATM(getVkid);
                } else {
                    diplayServerResopnse = WebService.getATM(vkid, token, imei, deviceid, simopertaor);
                }
                // Log.d(TAG, "WebSer...ceResponse: " + diplayServerResopnse);
            } catch (Exception e) {
                // AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                e.printStackTrace();

            }

        }
    }

    public class ClosestBankBranchAsyncTask extends AsyncTask<Void, Void, Void> {
        String TAG = "Response";
        String diplayServerResopnse;
        private Context context;
        private TelephonyManager telephonyManager;
        public ServiceProviderIfc delegate = null;

        public ClosestBankBranchAsyncTask(Context context) {

            // this.soapPrimitive = context1;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }


        @Override
        protected Void doInBackground(Void... voids) {
            Log.i(TAG, "doInBackground");
            getAvailableBalance();
            Log.i("", "Some not null string");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");

            try {
                if (diplayServerResopnse.equals("Invalid Request.")) {
                    //getBalance.setText("Invalid Request.");
                    Log.e(TAG + "Invalid Request.", diplayServerResopnse);
                    delegate.getAvailableBalance(diplayServerResopnse);
                }

                StringTokenizer tokens = new StringTokenizer(diplayServerResopnse, "|");
                String first = tokens.nextToken();
                String second = "{" +
                        "\"bank\":" + tokens.nextToken() + "}";
                try {
                    JSONObject jsnobject = new JSONObject(second);


                    JSONArray jsonArray = jsnobject.getJSONArray("bank");
                    arrayListBankBranch.add(new Bank("", "Please Select"));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String a = obj.getString("nbin_code");
                        String b = obj.getString("bank_name");
//                        System.out.println("State: " + a + " " + b);
                        arrayListBankBranch.add(new Bank(a, b));
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
                ArrayAdapter<Bank> atmArrayAdapter = new ArrayAdapter<Bank>(getActivity(),
                        android.R.layout.simple_spinner_dropdown_item, arrayListBankBranch);
                atmArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                closestBankBranch1.setAdapter(atmArrayAdapter);
                closestBankBranch2.setAdapter(atmArrayAdapter);
                closestBankBranch3.setAdapter(atmArrayAdapter);


                if (franchiseeDetails.getClosestBankBranch1NbinCode() != null) {
                    closestBankBranch1.setSelection(getIndexValueBank(closestBankBranch1,
                            franchiseeDetails.getClosestBankBranch1NbinCode()));
                }

                if (franchiseeDetails.getClosestBankBranch2NbinCode() != null) {
                    closestBankBranch2.setSelection(getIndexValueBank(closestBankBranch2,
                            franchiseeDetails.getClosestBankBranch2NbinCode()));
                }

                if (franchiseeDetails.getClosestBankBranch3NbinCode() != null) {
                    closestBankBranch3.setSelection(getIndexValueBank(closestBankBranch3,
                            franchiseeDetails.getClosestBankBranch3NbinCode()));
                }

            } catch (Exception e) {
                Log.d(TAG, "Error" + e);
                // AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));

            }
        }

        private void getAvailableBalance() {
            telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Connection connection = new Connection(context);
            String getVkid = connection.getVkid();
            String getTokenId = connection.getTokenId();

            String vkid = EncryptionUtil.encryptString(getVkid, context);
            String token = EncryptionUtil.encryptString(getTokenId, context);

            final String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            String deviceid = EncryptionUtil.encryptString(deviceId, context);
            String deviceIDAndroid = CommonUtils.getAndroidUniqueID(context);
            String imei = EncryptionUtil.encryptString(deviceIDAndroid, context);
            String simSerial = CommonUtils.getSimSerialNumber(context);
            String simopertaor = EncryptionUtil.encryptString(simSerial, context);
            try {
                if (isAdhoc) {
                    diplayServerResopnse = WebService.getBank(getVkid);
                } else {
                    diplayServerResopnse = WebService.getBank(vkid, token, imei, deviceid, simopertaor);
                }
                //Log.d(TAG, "WebSer...ceResponse: " + diplayServerResopnse);
            } catch (Exception e) {
                // AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                e.printStackTrace();

            }

        }
    }

    public class IsRuralAsyncTask extends AsyncTask<Void, Void, Void> {
        String TAG = "Response";
        String diplayServerResopnse;
        private Context context;
        private TelephonyManager telephonyManager;
        public ServiceProviderIfc delegate = null;

        public IsRuralAsyncTask(Context context) {

            // this.soapPrimitive = context1;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }


        @Override
        protected Void doInBackground(Void... voids) {
            Log.i(TAG, "doInBackground");
            // arrayListVillage.clear();
            telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Connection connection = new Connection(context);
            String getVkid = connection.getVkid();
            String getTokenId = connection.getTokenId();

            String vkid = EncryptionUtil.encryptString(getVkid, context);
            String token = EncryptionUtil.encryptString(getTokenId, context);
            final String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            String deviceid = EncryptionUtil.encryptString(deviceId, context);
            String deviceIDAndroid = CommonUtils.getAndroidUniqueID(context);
            String imei = EncryptionUtil.encryptString(deviceIDAndroid, context);
            String simSerial = CommonUtils.getSimSerialNumber(context);
            String simopertaor = EncryptionUtil.encryptString(simSerial, context);


            try {
                if (isAdhoc) {
                    diplayServerResopnse = WebService.isRural(getVkid, selectedVillage);
                } else {
                    diplayServerResopnse = WebService.isRural(vkid, token, imei, deviceid, simopertaor,
                            EncryptionUtil.encryptString(selectedVillage, context));
                }
                // Log.d(TAG, "WebSer...ceResponse: " + diplayServerResopnse);
            } catch (Exception e) {
                // AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                e.printStackTrace();

            }

            Log.i("", "Some not null string");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");

            try {
                if (diplayServerResopnse.equals("Invalid Request.")) {
                    //getBalance.setText("Invalid Request.");
                    Log.e(TAG + "Invalid Request.", diplayServerResopnse);
                    delegate.getAvailableBalance(diplayServerResopnse);
                }

                StringTokenizer tokens = new StringTokenizer(diplayServerResopnse, "|");
                String first = tokens.nextToken();
                String second = "{" +
                        "\"District\":" + tokens.nextToken() + "}";
                String isRural = "0";
                try {
                    JSONObject jsonbject = new JSONObject(second);
                    JSONArray jsonArray = jsonbject.getJSONArray("District");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        isRural = obj.getString("isRural");
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
                if (isRural.equals("1")) {
                    wardNoTextView.setVisibility(View.VISIBLE);
                    SiteVisitWardNo.setVisibility(View.VISIBLE);
                } else {
                    wardNoTextView.setVisibility(View.VISIBLE);
                    SiteVisitWardNo.setVisibility(View.VISIBLE);
                }

            } catch (Exception e) {
                Log.d(TAG, "Error" + e);
                // AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));

            }
        }

    }

    private int getIndexText(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }

        return 0;
    }

    private int getIndexValueBank(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (((Bank) spinner.getItemAtPosition(i)).getNbinCode().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }

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

    private String inchesTofeetAndInches(String inches) {
        int feet = Integer.valueOf(inches) / 12;
        int leftover = Integer.valueOf(inches) % 12;
        return feet + "|" + leftover;
    }

    private int feetAndInchesToInches(int feet, int inch) {
        int inches = (feet * 12) + inch;
        return inches;
    }

    private class AsyncNextgenLocationUpdate extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
            progress = new ProgressDialog(getActivity());
            progress.setTitle(R.string.updateLocation);
            progress.setMessage(getResources().getString(R.string.pleaseWait));
            progress.setCancelable(false);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {
                telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
                Connection connection = new Connection(getActivity());
                String vkid = connection.getVkid();
                String tokenId = connection.getTokenId();
//

                final String deviceId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
                String deviceid = EncryptionUtil.encryptString(deviceId, getActivity());

                String deviceIDAndroid = CommonUtils.getAndroidUniqueID(getActivity());
                String imei = EncryptionUtil.encryptString(deviceIDAndroid, getActivity());

                String simSerial = CommonUtils.getSimSerialNumber(getActivity());
                String simserialnumber = EncryptionUtil.encryptString(simSerial, getActivity());


                String vkidd = EncryptionUtil.encryptString(vkid, getActivity());
                String TokenId = EncryptionUtil.encryptString(tokenId, getActivity());
                String type = EncryptionUtil.encryptString("3", getActivity());
                String jsonData = JSONUtils.toString(franchiseeDetails);
                System.out.println("jsonData: " + jsonData);
//                String data = EncryptionUtil.encryptString(jsonData, getActivity());
                String data = jsonData;
                System.out.println("jsonData: " + data);

                if (Constants.ENABLE_FRANCHISEE_LOGIN) {
                    if (!TextUtils.isEmpty(vkid)) {
                        diplayServerResopnse = WebService.myVakrangeeKendraFranchiseeDetailsNextgenUpdate(vkid, "3", data);
                    } else {
                        String enquiryId = CommonUtils.getEnquiryId(getContext());
                        diplayServerResopnse = WebService.myVakrangeeKendraFranchiseeDetailsNextgenUpdate2(enquiryId, "3", data);
                    }
                } else {

                    if (isAdhoc) {
                        diplayServerResopnse = WebService.myVakrangeeKendraFranchiseeDetailsNextgenUpdate(
                                vkid, "3", data);
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
            // progress.dismiss();
            System.out.println("diplayServerResopnse: " + diplayServerResopnse);
            try {

                if (TextUtils.isEmpty(diplayServerResopnse)) {
                    AlertDialogBoxInfo.alertDialogShow(getContext(), diplayServerResopnse);
                    return;
                }
                if (diplayServerResopnse.startsWith("ERROR")) {
                    diplayServerResopnse = diplayServerResopnse.replace("ERROR|", "");
                    if (TextUtils.isEmpty(diplayServerResopnse)) {
                        AlertDialogBoxInfo.alertDialogShow(getContext(), getResources().getString(R.string.Warning));
                    } else {
                        AlertDialogBoxInfo.alertDialogShow(getContext(), diplayServerResopnse);
                    }
                } else if (diplayServerResopnse.startsWith("OKAY")) {
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.imageuploadsuccessful));

                } else {
                    AlertDialogBoxInfo.alertDialogShow(getContext(), getResources().getString(R.string.Warning));

                }


/*                if (diplayServerResopnse.startsWith("OKAY|")) {
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.imageuploadsuccessful));

                    //TODO: Need to refactor below process
                    *//*try {

                        final Connection connection = new Connection(getContext());
                        final String getUserid = franchiseeDetails.getNextGenFranchiseeApplicationNo();
                        String vkIdTemp = connection.getVkid();
                        final String getVkid = EncryptionUtil.encryptString(vkIdTemp, getContext());
                        final String getTokenId = EncryptionUtil.encryptString(connection.getTokenId(), getContext());
                        final String getimei = EncryptionUtil.encryptString(telephonyManager.getDeviceId(), getContext());
                        String deviceIdget = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                        final String getdeviceid = EncryptionUtil.encryptString(deviceIdget, getContext());
                        final String getsimserialnumber = EncryptionUtil.encryptString(telephonyManager.getSimSerialNumber(), getContext());
                        final String getid = EncryptionUtil.encryptString(getUserid, getContext());

                        if (isAdhoc) {
                            Utils.updateFranchicess(franchiseeDetails, getContext(), vkIdTemp, vkIdTemp);

                        } else {
                            Utils.updateFranchicess(franchiseeDetails, getContext(), getVkid, getid, getTokenId, getimei, getdeviceid, getsimserialnumber);
                        }
                        ((NextGenPhotoViewPager) getActivity()).viewPager.setCurrentItem(3, true);

                    } catch (Exception e) {
                        AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                        e.printStackTrace();
                    }*//*

                } else {
                    Log.e("Error in Server", diplayServerResopnse);
                    Toast.makeText(getActivity(), diplayServerResopnse, Toast.LENGTH_SHORT).show();
                }*/
            } catch (Exception e) {
                AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                e.printStackTrace();
            }
        }
    }

    public boolean validateNextGenSiteVisit() {

        return (isValidateProfileData() && isValidKendraLocation() && isValidKendraPhoto() && isValidPremiseDetail() && isValidPremiseLocatedAt() &&
                isValidSiteVisit() && isValidPremiseLevel() &&
                isValidPremiseShape() && isValidClosestBankBranch() &&
                isValidClosestATM() && isValidInteriorWorkStatus() && isValidateProvisionalSignBoard());
    }

    //region Profile Data
    public boolean isValidateProfileData() {
        boolean isValid = true;
        String msg = "";

        if (!IsToBeRemoved) {
            if (TextUtils.isEmpty(franchiseeDetails.getWelcomeMailStatus())) {
                isValid = false;
                msg = getString(R.string.alert_msg_welcome_status_unavailable);

            } else if (TextUtils.isEmpty(franchiseeDetails.getCallReceivedStatus())) {
                isValid = false;
                msg = getString(R.string.alert_msg_call_received_unavailable);

            } else if (TextUtils.isEmpty(franchiseeDetails.getLogisticsPaymentStatus())) {
                isValid = false;
                msg = getString(R.string.alert_msg_logistics_unavailable);

            } else if (!TextUtils.isEmpty(franchiseeDetails.getLogisticsPaymentStatus()) &&
                    TextUtils.isEmpty(franchiseeDetails.getLogisticsPaymentDate())) {

                isValid = false;
                msg = getString(R.string.alert_msg_logistics_payment_date_unavailable);

            } else if (TextUtils.isEmpty(franchiseeDetails.getConsentStatus())) {
                isValid = false;
                msg = getString(R.string.alert_msg_consent_unavailable);

            } else {
                if (TextUtils.isEmpty(franchiseeDetails.getGstRegisteredStatus())) {
                    isValid = false;
                    msg = getString(R.string.alert_msg_gst_unavailable);

                } else {
                    if (franchiseeDetails.getGstRegisteredStatus().equalsIgnoreCase("1")) {
                        if (TextUtils.isEmpty(franchiseeDetails.getGstNumber()) || TextUtils.isEmpty(franchiseeDetails.getGstAddress())) {
                            isValid = false;
                            msg = getString(R.string.alert_msg_gst_details_unavailable);
                        } else if (!TextUtils.isEmpty(franchiseeDetails.getGstNumber()) && !CommonUtils.GSTINisValid(franchiseeDetails.getGstNumber())) {
                            isValid = false;
                            msg = getString(R.string.alert_msg_gst_number_invalid);
                        }
                    }
                }
            }
        } else {
            if (TextUtils.isEmpty(franchiseeDetails.getConsentStatus())) {
                isValid = false;
                msg = getString(R.string.alert_msg_consent_unavailable);

            }
        }

        // If Invalid
        if (!isValid) {
            showAlertOkMessage(msg);
            ((NextGenPhotoViewPager) getActivity()).selectFragment(0);
        }

        return isValid;
    }
    //endregion

    public void updateProfileData() {
        FranchiseeDetails tempFranchiseeDetails = ((NextGenPhotoViewPager) getActivity()).getFranchiseeDetails();
        franchiseeDetails.setConsentStatus(tempFranchiseeDetails.getConsentStatus());
        franchiseeDetails.setWelcomeMailStatus(tempFranchiseeDetails.getWelcomeMailStatus());
        franchiseeDetails.setCallReceivedStatus(tempFranchiseeDetails.getCallReceivedStatus());
        franchiseeDetails.setLogisticsPaymentStatus(tempFranchiseeDetails.getLogisticsPaymentStatus());
        franchiseeDetails.setLogisticsPaymentDate(tempFranchiseeDetails.getLogisticsPaymentDate());
        franchiseeDetails.setGstRegisteredStatus(tempFranchiseeDetails.getGstRegisteredStatus());
        franchiseeDetails.setGstNumber(tempFranchiseeDetails.getGstNumber());
        franchiseeDetails.setGstAddress(tempFranchiseeDetails.getGstAddress());
        franchiseeDetails.setGstImage(tempFranchiseeDetails.getGstImage());
        franchiseeDetails.setLocationStatus(tempFranchiseeDetails.getLocationStatus());


    }

    //region validate provisional sign board spinner  data
    private boolean isValidateProvisionalSignBoard() {

        boolean IsOtheSpinnerDataValidationFailed = spinnerProvisionalLengthFeet.getSelectedItem() == null || spinnerProvisionalLengthFeet.getSelectedItem().toString().equals("Please Select") ||
                spinnerProvisionalLengthInch.getSelectedItem() == null || spinnerProvisionalLengthInch.getSelectedItem().toString().equals("Please Select") ||
                spinnerProvisionalWidthFeet.getSelectedItem() == null || spinnerProvisionalWidthFeet.getSelectedItem().toString().equals("Please Select") ||
                spinnerProvisionalWidthInch.getSelectedItem() == null || spinnerProvisionalWidthInch.getSelectedItem().toString().equals("Please Select");

        //Validation
        if (IsOtheSpinnerDataValidationFailed) {
            scrollViewNextGenSiteVisit.smoothScrollTo(0, tvHeader.getTop());
            showAlertOkMessage(getString(R.string.alert_msg_incomplete_provisional_main_signborad));
            return false;
        }

        return true;

    }

    //Validate Premise Detail
    private boolean isValidPremiseDetail() {

        boolean IsMainSignValidationFailed = spinnerMainSignBoardFeet.getSelectedItem() == null || spinnerMainSignBoardFeet.getSelectedItem().toString().equals("Please Select") ||
                spinnerMainSignBoardInch.getSelectedItem() == null || spinnerMainSignBoardInch.getSelectedItem().toString().equals("Please Select");

        boolean IsOtheSpinnerDataValidationFailed = spinnerWidthFeet.getSelectedItem() == null || spinnerWidthFeet.getSelectedItem().toString().equals("Please Select") ||
                spinnerWidthInch.getSelectedItem() == null || spinnerWidthInch.getSelectedItem().toString().equals("Please Select") ||
                spinnerDepthFeet.getSelectedItem() == null || spinnerDepthFeet.getSelectedItem().toString().equals("Please Select") ||
                spinnerDepthInch.getSelectedItem() == null || spinnerDepthInch.getSelectedItem().toString().equals("Please Select") ||
                spinnerHeightFeet.getSelectedItem() == null || spinnerHeightFeet.getSelectedItem().toString().equals("Please Select") ||
                spinnerHeightInch.getSelectedItem() == null || spinnerHeightInch.getSelectedItem().toString().equals("Please Select") ||
                spinnerEntranceFeet.getSelectedItem() == null || spinnerEntranceFeet.getSelectedItem().toString().equals("Please Select") ||
                spinnerEntranceInch.getSelectedItem() == null || spinnerEntranceInch.getSelectedItem().toString().equals("Please Select");

        boolean IsValidationFailed;
        if (IsToBeRemoved)
            IsValidationFailed = IsOtheSpinnerDataValidationFailed;
        else
            IsValidationFailed = IsOtheSpinnerDataValidationFailed || IsMainSignValidationFailed;

        //Validation
        if (IsValidationFailed) {

            scrollViewNextGenSiteVisit.smoothScrollTo(0, tvHeader.getTop());
            showAlertOkMessage(getString(R.string.alert_msg_incomplete_premise_detail));

            return false;
        } else if (TextUtils.isEmpty(AreaSqFeet.getText().toString())) {
            scrollViewNextGenSiteVisit.smoothScrollTo(0, tvHeader.getTop());
            AreaSqFeet.requestFocus();
            showAlertOkMessage(getString(R.string.alert_msg_incomplete_premise_detail));

            return false;
        } else if (Float.parseFloat(AreaSqFeet.getText().toString()) == 0) {
            scrollViewNextGenSiteVisit.smoothScrollTo(0, tvHeader.getTop());
            AreaSqFeet.requestFocus();
            showAlertOkMessage(getString(R.string.alert_msg_incomplete_premise_area_detail));

            return false;
        }

        return true;
    }

    //Validate PremiseLocatedAt
    private boolean isValidPremiseLocatedAt() {

        /*if (!(premiseLocatedAtHighStreet.isChecked() || premiseLocatedAtMall.isChecked()) ||
                spinnerFloor.getSelectedItem() == null || spinnerFloor.getSelectedItem().toString().equals("Please Select") ||
                spinnerStructure.getSelectedItem() == null || spinnerStructure.getSelectedItem().toString().equals("Please Select") ||
                spinnerRoof.getSelectedItem() == null || spinnerRoof.getSelectedItem().toString().equals("Please Select") ||
                !(FrontageObstructedYes.isChecked() || FrontageObstructedNo.isChecked()) ||
                !(FootpathYes.isChecked() || FootpathNo.isChecked()) ||
                !(BathroomYes.isChecked() || BathroomNo.isChecked()) ||
                !(PantryYes.isChecked() || PantryNo.isChecked()) ||
                !(PillersYes.isChecked() || PillersNo.isChecked()) ||
                !(WindowsYes.isChecked() || WindowsNo.isChecked()) ||
                !(BeamYes.isChecked() || BeamNo.isChecked()) ||
                !(AdjacentShopsYes.isChecked() || AdjacentShopsNo.isChecked()) ||
                !(MultipleEntriesYes.isChecked() || MultipleEntriesNo.isChecked())) {*/

        if (spinnerFloor.getSelectedItem() == null || spinnerFloor.getSelectedItem().toString().equals("Please Select") ||
                spinnerStructure.getSelectedItem() == null || spinnerStructure.getSelectedItem().toString().equals("Please Select") ||
                spinnerRoof.getSelectedItem() == null || spinnerRoof.getSelectedItem().toString().equals("Please Select") ||
                !(PillersYes.isChecked() || PillersNo.isChecked()) ||
                !(WindowsYes.isChecked() || WindowsNo.isChecked()) ||
                !(BeamYes.isChecked() || BeamNo.isChecked()) ||
                !(AdjacentShopsYes.isChecked() || AdjacentShopsNo.isChecked()) ||
                !(MultipleEntriesYes.isChecked() || MultipleEntriesNo.isChecked())) {

            scrollViewNextGenSiteVisit.smoothScrollTo(0, layoutPremiseLocatedAt.getTop());
            showAlertOkMessage(getString(R.string.alert_msg_incomplete_premise_located_at_detail));

            return false;
        }

        return true;
    }

    //Validate Address Of Location Visited
    private boolean isValidSiteVisit() {
        boolean isValid = true;

        if (IsToBeRemoved)
            return true;

        if (TextUtils.isEmpty(SiteVisitAddress1.getText().toString())) {
            SiteVisitAddress1.setError("Enter Address");
            isValid = false;
            SiteVisitAddress1.requestFocus();
        } else if (TextUtils.isEmpty(SiteVisitAddress2.getText().toString())) {
            SiteVisitAddress2.setError("Enter Address");
            isValid = false;
            SiteVisitAddress2.requestFocus();

        } else if (TextUtils.isEmpty(SiteVisitLandmark.getText().toString())) {
            SiteVisitLandmark.setError("Enter Landmark");
            isValid = false;
            SiteVisitLandmark.requestFocus();
        } else if (stateSpinner.getSelectedItem() == null || stateSpinner.getSelectedItem().toString().equals("Please Select") ||
                districtSpinner.getSelectedItem() == null || districtSpinner.getSelectedItem().toString().equals("Please Select") ||
                villageSpinner.getSelectedItem() == null || villageSpinner.getSelectedItem().toString().equals("Please Select")) {
            isValid = false;
        } else if (TextUtils.isEmpty(SiteVisitWardNo.getText().toString()) || SiteVisitWardNo.getText().toString().length() == 0) {
            SiteVisitWardNo.setTextColor(Color.parseColor("#000000"));
            SiteVisitWardNo.setError("Enter atleast 1 digit Ward No.");
            isValid = false;
            SiteVisitWardNo.requestFocus();
        } else if (Integer.parseInt(SiteVisitWardNo.getText().toString()) == 0) {
            SiteVisitWardNo.setTextColor(Color.parseColor("#000000"));
            SiteVisitWardNo.setError("Ward No. can not be zero.");
            isValid = false;
            SiteVisitWardNo.requestFocus();
        } else if (TextUtils.isEmpty(SiteVisitPincode.getText().toString()) || SiteVisitPincode.getText().toString().length() != 6) {
            SiteVisitPincode.setTextColor(Color.parseColor("#000000"));
            SiteVisitPincode.setError("Enter 6 digits Pincode");
            isValid = false;
            SiteVisitPincode.requestFocus();
        } else if (Integer.parseInt(SiteVisitPincode.getText().toString()) == 0) {
            SiteVisitPincode.setTextColor(Color.parseColor("#000000"));
            SiteVisitPincode.setError("PIN can not be zero.");
            isValid = false;
            SiteVisitPincode.requestFocus();
        }

        if (!isValid) {
            scrollViewNextGenSiteVisit.smoothScrollTo(0, layoutAddressOfLocationVisited.getTop());
            showAlertOkMessage(getString(R.string.alert_msg_incomplete_address_of_location_visited_detail));
        }

        return isValid;
    }


    public boolean isValidatedReviewedOption() {

        int tab = 0;
        boolean isValid = true;

        if (!franchiseeDetails.isProfileReviewed()) {
            isValid = false;
            tab = 0;

        } else if (!franchiseeDetails.isLocationReviewed()) {
            isValid = false;
            tab = 1;

        } else if (!franchiseeDetails.isPhotoReviewed()) {
            isValid = false;
            tab = 2;

        } else if (!franchiseeDetails.isSiteVisitReviewed()) {
            isValid = false;
            tab = 3;

        }

        // If Invalid
        if (!isValid) {
            showAlertOkMessage(getContext().getString(R.string.alert_msg_i_agree));
            ((NextGenPhotoViewPager) getActivity()).selectFragment(tab);
        }
        return isValid;
    }

    //Validate Premise Level
    private boolean isValidPremiseLevel() {
        /*if (!(roadLevel.isChecked() || belowRoad.isChecked())) {
            scrollViewNextGenSiteVisit.smoothScrollTo(0, layoutPremiseLevel.getTop());
            showAlertOkMessage(getString(R.string.alert_msg_incomplete_premise_level_detail));
            return false;
        }*/
        return true;
    }

    //Validate Premise Shape
    private boolean isValidPremiseShape() {
        /*if (TextUtils.isEmpty(franchiseeDetails.getPremiseShape())) {
            scrollViewNextGenSiteVisit.smoothScrollTo(0, layoutPremiseShape.getTop());
            showAlertOkMessage(getString(R.string.alert_msg_incomplete_premise_shape_detail));
            return false;
        }

        //Validattion For Site Layout Sketch
        if (TextUtils.isEmpty(franchiseeDetails.getSiteLayoutSketch()) && franchiseeDetails.getPremiseShape() != null
                && franchiseeDetails.getPremiseShape().equals("4")) {
            scrollViewNextGenSiteVisit.smoothScrollTo(0, layoutPremiseShape.getTop());
            showAlertOkMessage(getString(R.string.alert_msg_layout_sketch));
            return false;
        }*/
        return true;
    }

    //Validate Closet Bank Branch
    private boolean isValidClosestBankBranch() {

        if (closestBankBranch1.getSelectedItem().toString().equals("Please Select") ||
                closestBankBranch2.getSelectedItem().toString().equals("Please Select") ||
                closestBankBranch3.getSelectedItem().toString().equals("Please Select")) {

            scrollViewNextGenSiteVisit.smoothScrollTo(0, layoutClosestBankBranch.getTop());
            showAlertOkMessage(getString(R.string.alert_msg_incomplete_closest_bank_branch_detail));

            return false;
        }

        return true;
    }

    //Validate Closest
    private boolean isValidClosestATM() {

        if (closestATM1.getSelectedItem().toString().equals("Please Select") ||
                closestATM2.getSelectedItem().toString().equals("Please Select") ||
                closestATM3.getSelectedItem().toString().equals("Please Select")) {

            scrollViewNextGenSiteVisit.smoothScrollTo(0, layoutClosestATM.getTop());
            showAlertOkMessage(getString(R.string.alert_msg_incomplete_closest_atm_detail));
            return false;
        }

        return true;
    }

    //Validate Interior Work Status
    private boolean isValidInteriorWorkStatus() {

        if (IsToBeRemoved)
            return true;

        boolean isValid = true;
        String msg = "";

        if (!franchiseeDetails.getInteriorWorkStatus().equals("0")) {
            if (TextUtils.isEmpty(franchiseeDetails.getCompletionDate()) || startDate == null) {
                isValid = false;
                msg = getString(R.string.alert_msg_work_start_date_unavailable1);
            } else if (TextUtils.isEmpty(franchiseeDetails.getExpectedCompletionDate()) || completionDate == null) {
                isValid = false;
                msg = getString(R.string.alert_msg_work_end_date_unavailable1);
            } else if (completionDate.before(startDate)) {
                isValid = false;
                msg = getString(R.string.alert_msg_work_date_validation);
            }

            // If Invalid
            if (!isValid) {
                AlertDialogBoxInfo.alertDialogShow(getActivity(), msg);
            }
        }

        return isValid;
    }

    //endregion

    //region Validate VKendra Location
    private boolean isValidKendraLocation() {
        if (TextUtils.isEmpty(franchiseeDetails.getLatitude()) || TextUtils.isEmpty(franchiseeDetails.getLongitude())) {
            //TODO: Need to show an alert and jump to Kendra Location Tab
            showAlertOkMessage(getString(R.string.alert_msg_location_unavailable));
            ((NextGenPhotoViewPager) getActivity()).selectFragment(1);
            return false;
        } else if (franchiseeDetails.getLatitude().startsWith("0") || franchiseeDetails.getLongitude().startsWith("0")) {
            showAlertOkMessage(getString(R.string.alert_msg_location_unavailable2));
            ((NextGenPhotoViewPager) getActivity()).selectFragment(1);
            return false;
        }
        return true;
    }
    //endregion

    //region Validate VKendra Photo
    private boolean isValidKendraPhoto() {

        boolean isValid = true;
        String msg = "";

        if (TextUtils.isEmpty(franchiseeDetails.getFrontageImageId())) {
            isValid = false;
            msg = getString(R.string.alert_msg_frontageimage_unavailable);
        } else if (TextUtils.isEmpty(franchiseeDetails.getFrontagePhoto10ftImageId())) {
            isValid = false;
            msg = getString(R.string.alert_msg_frontageimage10_unavailable);
        } else if (TextUtils.isEmpty(franchiseeDetails.getLeftWallImageId())) {
            isValid = false;
            msg = getString(R.string.alert_msg_leftwallimage_unavailable);
        } else if (TextUtils.isEmpty(franchiseeDetails.getFrontWallImageId())) {
            isValid = false;
            msg = getString(R.string.alert_msg_frontwallimage_unavailable);
        } else if (TextUtils.isEmpty(franchiseeDetails.getRightWallImageId())) {
            isValid = false;
            msg = getString(R.string.alert_msg_rightwallimage_unavailable);
        } else if (TextUtils.isEmpty(franchiseeDetails.getBackWallImageId())) {
            isValid = false;
            msg = getString(R.string.alert_msg_backwallimage_unavailable);
        } else if (TextUtils.isEmpty(franchiseeDetails.getCeilingImageId())) {
            isValid = false;
            msg = getString(R.string.alert_msg_ceilingimage_unavailable);
        } else if (TextUtils.isEmpty(franchiseeDetails.getFloorImageId())) {
            isValid = false;
            msg = getString(R.string.alert_msg_floorimage_unavailable);
        } else if (TextUtils.isEmpty(franchiseeDetails.getFrontageOppositeImageId())) {
            isValid = false;
            msg = getString(R.string.alert_msg_frontageoppositesideroad_unavailable);
        } else if (TextUtils.isEmpty(franchiseeDetails.getFrontageLeftImageId())) {
            isValid = false;
            msg = getString(R.string.alert_msg_frontageroadleftside_unavailable);
        } else if (TextUtils.isEmpty(franchiseeDetails.getFrontageRightImageId())) {
            isValid = false;
            msg = getString(R.string.alert_msg_frontageroadrightside_unavailable);
        }

        // If Invalid
        if (!isValid) {
            showAlertOkMessage(msg);
            ((NextGenPhotoViewPager) getActivity()).selectFragment(2);
        }

        return isValid;
    }
    //endregion

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK) {
            try {
                Uri imageUri = Uri.parse(mCurrentPhotoPath);

                Bitmap bitmapNew = ImageUtils.getBitmap(getActivity().getContentResolver(), imageUri, "", "", "");
                //BitMap with TimeStamp on it
                bitmapNew = ImageUtils.stampWithTimeInBitmap(bitmapNew);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmapNew.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                String imageBase64 = EncryptionUtil.encodeBase64(byteArrayOutputStream.toByteArray());

                ImageView imageView = new ImageView(getContext());
                imageView.setImageBitmap(bitmapNew);

                if (!CommonUtils.isLandscapePhoto(imageUri, imageView)) {
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getString(R.string.landscape_mode_allowed));
                } else {
                    ImageDto imageDto = new ImageDto();
                    imageDto.setUri(imageUri);
                    imageDto.setBitmap(bitmapNew);
                    imageDto.setImageBase64(imageBase64);
                    showImagePreviewDialog((Object) imageDto);
                }

            } catch (Exception e) {
                e.printStackTrace();
                AlertDialogBoxInfo.alertDialogShow(getContext(), getResources().getString(R.string.Warning));
            }
        }
    }

    public void showImageDialog(int resourceId) {
        showImageDialog(resourceId, null);
    }

    public void showImageDialog(int resourceId, String dialogTitle) {

        Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(), resourceId);

        ImageDto imageDto = new ImageDto();
        imageDto.setBitmap(icon);
        imageDto.setName("");
        imageDto.setImageBase64("");

        /*if (customImagePreviewDialog != null && customImagePreviewDialog.isShowing()) {
            customImagePreviewDialog.refresh(imageDto);
            return;
        }*/

        if (imageDto != null) {
            customImagePreviewDialog = new CustomImagePreviewDialog(getContext(), imageDto, new CustomImagePreviewDialog.IImagePreviewDialogClicks() {
                @Override
                public void capturePhotoClick() {
                    startCamera(view);
                }

                @Override
                public void OkClick(Object object) {
                    ImageDto imageDto = ((ImageDto) object);
                    franchiseeDetails.setSiteLayoutSketch(imageDto.getImageBase64());
                    imgOtherSketch.setImageBitmap(imageDto.getBitmap());

                }
            });
            customImagePreviewDialog.show();
            customImagePreviewDialog.setCancelable(false);
            customImagePreviewDialog.allowChangePhoto(false);
            customImagePreviewDialog.allowRemarks(false);
            customImagePreviewDialog.setDialogTitle(dialogTitle);
            customImagePreviewDialog.allowSaveOption(false);

        } else {
            Toast.makeText(getActivity(), "No photo available", Toast.LENGTH_SHORT).show();
        }
    }

    //region Show Schedule Visit DateTime Picker Dialog
    private DateTimePickerDialog dateTimePickerDialog;
    private int selectedDateTimeId = 0;

    private void showDateTimeDialogPicker() {

        Date defaultDate = null;
        if (selectedDateTimeId == R.id.textViewStartDate) {
            defaultDate = startDate;
        } else if (selectedDateTimeId == R.id.textViewCompletionDate) {
            defaultDate = completionDate;
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
                            franchiseeDetails.setCompletionDate(defaultFormattedDateTime);
                        } else if (selectedDateTimeId == R.id.textViewCompletionDate) {
                            completionDate = datetime;
                            strCompletionDate = formatedDate;
                            franchiseeDetails.setExpectedCompletionDate(defaultFormattedDateTime);
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
            //cal.setTime(new Date());
            //cal.add(Calendar.MONTH, -5); // current date to past 5 month date display
            cal.set(Calendar.YEAR, 2018);
            cal.set(Calendar.MONTH, 1);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            dateTimePickerDialog.setMinDate(cal.getTimeInMillis());

           /* if (TextUtils.isEmpty(strStartDate)) {
                dateTimePickerDialog.setMinDate(new Date().getTime() - 604800000L); // 7 * 24 * 60 * 60 * 1000
            } else {
                Date newDate = new Date(startDate.getTime() - 604800000L); // 7 * 24 * 60 * 60 * 1000
                dateTimePickerDialog.setMinDate(newDate.getTime());
            }*/

        } else if (selectedDateTimeId == R.id.textViewCompletionDate && !TextUtils.isEmpty(strStartDate)) {
            dateTimePickerDialog.setMinDate(startDate.getTime());
        }
        dateTimePickerDialog.show();

    }

    //endregion

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (OTPDialog != null && OTPDialog.isShowing())
            OTPDialog.dismiss();
    }

    private void spinner_focusablemode(Spinner spinner) {
        spinner.setFocusable(true);
        spinner.setFocusableInTouchMode(true);
    }

}

