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
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import in.vakrangee.franchisee.sitelayout.NextGenPhotoViewPager;
import in.vakrangee.franchisee.sitelayout.NextGenViewPager;
import in.vakrangee.franchisee.sitelayout.R;
import in.vakrangee.franchisee.sitelayout.Utils;
import in.vakrangee.franchisee.sitelayout.adapter.MyVakrangeeKendraImageAdapter;
import in.vakrangee.franchisee.sitelayout.finalrmapproval.MainFinalRMApprovalActivity;
import in.vakrangee.franchisee.sitelayout.sitereadiness.SiteReadinessActivity;
import in.vakrangee.supercore.franchisee.model.FranchiseeDetails;
import in.vakrangee.supercore.franchisee.model.FranchiseeRemarkDetails;
import in.vakrangee.supercore.franchisee.model.My_vakranggekendra_image;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.utils.GPSTracker;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.utils.JSONUtils;
import in.vakrangee.supercore.franchisee.webservice.WebService;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class NextGenKendraPhotoFragment extends Fragment {


    String TAG = "Response";

    SimpleDateFormat sdf;
    String getlati;
    String currentDateandTime;

    String diplayServerResopnse;
    TelephonyManager telephonyManager;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 0;

    public MyVakrangeeKendraImageAdapter adapter;
    Connection connection;
    public static Spinner spinner;
    private ImageView imageViewa1;
    ImageView btnCapture;

    byte[] byteArray;

    Button btnAddphoto, btnSubmitPhoto;

    private RecyclerView recyclerView;
    private String mCurrentPhotoPath;
    FranchiseeDetails franchiseeDetails;

    ProgressDialog progress;
    String Frontage, Frontage10, LeftWall, FrontWall, RightWall, BackWall, Ceiling, Floor, Extra1, Extra2, Extra3, KendraFront, KendraInside, RibbonCutting,
            ReligiousCeremony, PublicGathering;
    final int CAMERA_CAPTURE = 1;

    int PIC_CROP = 3;
    private Uri picUri;
    InternetConnection internetConnection;
    public static String imgetype = "";
    public static List<My_vakranggekendra_image> myvakranggekendraimageList;

    ImageView captureImage;
    TextView txtImage;
    private GPSTracker gpsTracker;
    private String latitude = "", longitude = "", currentTimestamp = "";

    private boolean isAdhoc = false;
    private LinearLayout layoutVerified, layoutAddPhoto;
    private CheckBox checkBoxProfileVerify;
    boolean isSiteReadinessVerification = false;
    boolean IsSiteVisit = false;
    private String modetype;
    private Typeface font;
    private TextView textviewAddPhoto;

    public NextGenKendraPhotoFragment() {
    }

    public NextGenKendraPhotoFragment(FranchiseeDetails franchiseeDetails) {

        this.franchiseeDetails = franchiseeDetails;
    }

    public NextGenKendraPhotoFragment(FranchiseeDetails franchiseeDetails, String modeType) {
        this.franchiseeDetails = franchiseeDetails;
        this.modetype = modeType;
        if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXTGEN_SITE_READINESS_AND_VERIFICATION)) {
            isSiteReadinessVerification = true;
        } else {
            IsSiteVisit = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_vakrangee_kendra_next_gen_photo, container, false);

        try {
            font = Typeface.createFromAsset(getActivity().getAssets(), "fontawesome-webfont.ttf");

            // Get App MODE
            isAdhoc = Constants.ENABLE_ADHOC_MODE || Constants.ENABLE_FRANCHISEE_MODE;

            camerapermission();
            telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            connection = new Connection(getActivity());
            recyclerView = (RecyclerView) view.findViewById(R.id.recyle_view_search);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            layoutVerified = view.findViewById(R.id.layoutVerified);
            checkBoxProfileVerify = view.findViewById(R.id.checkBoxProfileVerify);
            textviewAddPhoto = view.findViewById(R.id.textviewAddPhoto);
            layoutAddPhoto = view.findViewById(R.id.layoutAddPhoto);

            myvakranggekendraimageList = new ArrayList<My_vakranggekendra_image>();
            if (franchiseeDetails != null && franchiseeDetails.getFrontageImageId() != null
                    && !franchiseeDetails.getFrontageImageId().isEmpty()
                    && !franchiseeDetails.getFrontageImageId().equals("null")) {
                My_vakranggekendra_image myvakranggekendraimage = new My_vakranggekendra_image();
                myvakranggekendraimage.setID(0);
                myvakranggekendraimage.setImgetype("Frontage Image (5ft Distance)");
                Bitmap img = StringToBitMap(franchiseeDetails.getFrontageImageId());
                myvakranggekendraimage.setImage(img);
                Log.d("f_image***", franchiseeDetails.getFrImageId());
                myvakranggekendraimage.setImageId(franchiseeDetails.getFrImageId());
                myvakranggekendraimage.setImageHash(CommonUtils.getImageSalt(franchiseeDetails.getFrontageImageId()));
                myvakranggekendraimageList.add(myvakranggekendraimage);
            }
            if (franchiseeDetails != null && franchiseeDetails.getFrontagePhoto10ftImageId() != null
                    && !franchiseeDetails.getFrontagePhoto10ftImageId().isEmpty()
                    && !franchiseeDetails.getFrontagePhoto10ftImageId().equals("null")) {
                My_vakranggekendra_image myvakranggekendraimage = new My_vakranggekendra_image();
                myvakranggekendraimage.setID(10);
                myvakranggekendraimage.setImgetype("Frontage Image (10ft Distance)");
                Bitmap img = StringToBitMap(franchiseeDetails.getFrontagePhoto10ftImageId());
                myvakranggekendraimage.setImage(img);
                myvakranggekendraimage.setImageId(franchiseeDetails.getFr10ImageId());
                myvakranggekendraimage.setImageHash(CommonUtils.getImageSalt(franchiseeDetails.getFrontagePhoto10ftImageId()));
                myvakranggekendraimageList.add(myvakranggekendraimage);
            }
            if (franchiseeDetails != null
                    && franchiseeDetails.getLeftWallImageId() != null
                    && !franchiseeDetails.getLeftWallImageId().isEmpty()
                    && !franchiseeDetails.getLeftWallImageId().equals("null")) {
                My_vakranggekendra_image myvakranggekendraimagea = new My_vakranggekendra_image();
                myvakranggekendraimagea.setID(1);
                myvakranggekendraimagea.setImgetype("Left Wall Image");
                Bitmap img = StringToBitMap(franchiseeDetails.getLeftWallImageId());
                myvakranggekendraimagea.setImage(img);
                myvakranggekendraimagea.setImageId(franchiseeDetails.getLwImageId());
                myvakranggekendraimagea.setImageHash(CommonUtils.getImageSalt(franchiseeDetails.getLeftWallImageId()));
                myvakranggekendraimageList.add(myvakranggekendraimagea);
            }
            if (franchiseeDetails != null
                    && franchiseeDetails.getFrontWallImageId() != null
                    && !franchiseeDetails.getFrontWallImageId().isEmpty()
                    && !franchiseeDetails.getFrontWallImageId().equals("null")) {
                My_vakranggekendra_image myvakranggekendraimagea = new My_vakranggekendra_image();
                myvakranggekendraimagea.setID(2);
                myvakranggekendraimagea.setImgetype("Front Wall Image");
                Bitmap img = StringToBitMap(franchiseeDetails.getFrontWallImageId());
                myvakranggekendraimagea.setImage(img);
                myvakranggekendraimagea.setImageId(franchiseeDetails.getFwImageId());
                myvakranggekendraimagea.setImageHash(CommonUtils.getImageSalt(franchiseeDetails.getFrontWallImageId()));
                myvakranggekendraimageList.add(myvakranggekendraimagea);
            }
            if (franchiseeDetails != null
                    && franchiseeDetails.getRightWallImageId() != null
                    && !franchiseeDetails.getRightWallImageId().isEmpty()
                    && !franchiseeDetails.getRightWallImageId().equals("null")) {
                My_vakranggekendra_image myvakranggekendraimagea = new My_vakranggekendra_image();
                myvakranggekendraimagea.setID(3);
                myvakranggekendraimagea.setImgetype("Right Wall Image");
                Bitmap img = StringToBitMap(franchiseeDetails.getRightWallImageId());
                myvakranggekendraimagea.setImage(img);
                myvakranggekendraimagea.setImageId(franchiseeDetails.getRwImageId());
                myvakranggekendraimagea.setImageHash(CommonUtils.getImageSalt(franchiseeDetails.getRightWallImageId()));
                myvakranggekendraimageList.add(myvakranggekendraimagea);
            }
            if (franchiseeDetails != null && franchiseeDetails.getBackWallImageId() != null
                    && !franchiseeDetails.getBackWallImageId().isEmpty()
                    && !franchiseeDetails.getBackWallImageId().equals("null")) {

                My_vakranggekendra_image myvakranggekendraimagea = new My_vakranggekendra_image();
                myvakranggekendraimagea.setID(4);
                myvakranggekendraimagea.setImgetype("Back Wall Image");
                Bitmap img = StringToBitMap(franchiseeDetails.getBackWallImageId());
                myvakranggekendraimagea.setImage(img);
                myvakranggekendraimagea.setImageId(franchiseeDetails.getBwImageId());
                myvakranggekendraimagea.setImageHash(CommonUtils.getImageSalt(franchiseeDetails.getBackWallImageId()));
                myvakranggekendraimageList.add(myvakranggekendraimagea);
            }
            if (franchiseeDetails != null
                    && franchiseeDetails.getCeilingImageId() != null
                    && !franchiseeDetails.getCeilingImageId().isEmpty()
                    && !franchiseeDetails.getCeilingImageId().equals("null")) {
                My_vakranggekendra_image myvakranggekendraimagea = new My_vakranggekendra_image();
                myvakranggekendraimagea.setID(5);
                myvakranggekendraimagea.setImgetype("Ceiling Image");
                Bitmap img = StringToBitMap(franchiseeDetails.getCeilingImageId());
                myvakranggekendraimagea.setImage(img);
                myvakranggekendraimagea.setImageId(franchiseeDetails.getCeImageId());
                myvakranggekendraimagea.setImageHash(CommonUtils.getImageSalt(franchiseeDetails.getCeilingImageId()));
                myvakranggekendraimageList.add(myvakranggekendraimagea);
            }

            if (franchiseeDetails != null && franchiseeDetails.getFloorImageId() != null
                    && !franchiseeDetails.getFloorImageId().isEmpty() && !franchiseeDetails.getFloorImageId().equals("null")) {
                My_vakranggekendra_image myvakranggekendraimagea = new My_vakranggekendra_image();
                myvakranggekendraimagea.setID(6);
                myvakranggekendraimagea.setImgetype("Floor Image");
                Bitmap img = StringToBitMap(franchiseeDetails.getFloorImageId());
                myvakranggekendraimagea.setImage(img);
                myvakranggekendraimagea.setImageId(franchiseeDetails.getFlImageId());
                myvakranggekendraimagea.setImageHash(CommonUtils.getImageSalt(franchiseeDetails.getFloorImageId()));
                myvakranggekendraimageList.add(myvakranggekendraimagea);
            }
            if (franchiseeDetails != null && franchiseeDetails.getFrontageOppositeImageId() != null
                    && !franchiseeDetails.getFrontageOppositeImageId().isEmpty() && !franchiseeDetails.getFrontageOppositeImageId().equals("null")) {
                My_vakranggekendra_image myvakranggekendraimagea = new My_vakranggekendra_image();
                myvakranggekendraimagea.setID(7);
                myvakranggekendraimagea.setImgetype("Frontage Opposite Side Road");
                Bitmap img = StringToBitMap(franchiseeDetails.getFrontageOppositeImageId());
                myvakranggekendraimagea.setImage(img);
                myvakranggekendraimagea.setImageId(franchiseeDetails.getFroImageId());
                myvakranggekendraimagea.setImageHash(CommonUtils.getImageSalt(franchiseeDetails.getFrontageOppositeImageId()));
                myvakranggekendraimageList.add(myvakranggekendraimagea);
            }
            if (franchiseeDetails != null && franchiseeDetails.getFrontageLeftImageId() != null
                    && !franchiseeDetails.getFrontageLeftImageId().isEmpty() && !franchiseeDetails.getFrontageLeftImageId().equals("null")) {
                My_vakranggekendra_image myvakranggekendraimagea = new My_vakranggekendra_image();
                myvakranggekendraimagea.setID(8);

                if (isSiteReadinessVerification) {
                    myvakranggekendraimagea.setImgetype("Selfie Indoor");

                } else {
                    myvakranggekendraimagea.setImgetype("Frontage Left Side Road");
                }

                Bitmap img = StringToBitMap(franchiseeDetails.getFrontageLeftImageId());
                myvakranggekendraimagea.setImage(img);
                myvakranggekendraimagea.setImageId(franchiseeDetails.getFrlImageId());
                myvakranggekendraimagea.setImageHash(CommonUtils.getImageSalt(franchiseeDetails.getFrontageLeftImageId()));
                myvakranggekendraimageList.add(myvakranggekendraimagea);
            }
            if (franchiseeDetails != null && franchiseeDetails.getFrontageRightImageId() != null
                    && !franchiseeDetails.getFrontageRightImageId().isEmpty() && !franchiseeDetails.getFrontageRightImageId().equals("null")) {
                My_vakranggekendra_image myvakranggekendraimagea = new My_vakranggekendra_image();
                myvakranggekendraimagea.setID(9);

                if (isSiteReadinessVerification) {
                    myvakranggekendraimagea.setImgetype("Selfie Outdoor");

                } else {
                    myvakranggekendraimagea.setImgetype("Frontage Right Side Road");
                }

                Bitmap img = StringToBitMap(franchiseeDetails.getFrontageRightImageId());
                myvakranggekendraimagea.setImage(img);
                myvakranggekendraimagea.setImageId(franchiseeDetails.getFrrImageId());
                myvakranggekendraimagea.setImageHash(CommonUtils.getImageSalt(franchiseeDetails.getFrontageRightImageId()));
                myvakranggekendraimageList.add(myvakranggekendraimagea);
            }
            if (franchiseeDetails != null && franchiseeDetails.getKendraFrontOutsideViewId() != null
                    && !franchiseeDetails.getKendraFrontOutsideViewId().isEmpty() && !franchiseeDetails.getKendraFrontOutsideViewId().equals("null")) {
                My_vakranggekendra_image myvakranggekendraimagea = new My_vakranggekendra_image();
                myvakranggekendraimagea.setID(11);
                myvakranggekendraimagea.setImgetype("Kendra Front outside view - from 10 ft distance");
                Bitmap img = StringToBitMap(franchiseeDetails.getKendraFrontOutsideViewId());
                myvakranggekendraimagea.setImage(img);
                myvakranggekendraimagea.setImageId(franchiseeDetails.getFr11ImageId());
                myvakranggekendraimagea.setImageHash(CommonUtils.getImageSalt(franchiseeDetails.getKendraFrontOutsideViewId()));
                myvakranggekendraimageList.add(myvakranggekendraimagea);
            }
            if (franchiseeDetails != null && franchiseeDetails.getKendraInsideViewId() != null
                    && !franchiseeDetails.getKendraInsideViewId().isEmpty() && !franchiseeDetails.getKendraInsideViewId().equals("null")) {
                My_vakranggekendra_image myvakranggekendraimagea = new My_vakranggekendra_image();
                myvakranggekendraimagea.setID(12);
                myvakranggekendraimagea.setImgetype("Kendra Inside view");
                Bitmap img = StringToBitMap(franchiseeDetails.getKendraInsideViewId());
                myvakranggekendraimagea.setImage(img);
                myvakranggekendraimagea.setImageId(franchiseeDetails.getKendraInsideViewId());
                myvakranggekendraimagea.setImageHash(CommonUtils.getImageSalt(franchiseeDetails.getKendraInsideViewId()));
                myvakranggekendraimageList.add(myvakranggekendraimagea);
            }
            if (franchiseeDetails != null && franchiseeDetails.getRibbonCuttingCeremonyId() != null
                    && !franchiseeDetails.getRibbonCuttingCeremonyId().isEmpty() && !franchiseeDetails.getRibbonCuttingCeremonyId().equals("null")) {
                My_vakranggekendra_image myvakranggekendraimagea = new My_vakranggekendra_image();
                myvakranggekendraimagea.setID(13);
                myvakranggekendraimagea.setImgetype("Ribbon cutting ceremony");
                Bitmap img = StringToBitMap(franchiseeDetails.getRibbonCuttingCeremonyId());
                myvakranggekendraimagea.setImage(img);
                myvakranggekendraimagea.setImageId(franchiseeDetails.getRibbonCuttingCeremonyId());
                myvakranggekendraimagea.setImageHash(CommonUtils.getImageSalt(franchiseeDetails.getRibbonCuttingCeremonyId()));
                myvakranggekendraimageList.add(myvakranggekendraimagea);
            }

            if (franchiseeDetails != null && franchiseeDetails.getReligiousCeremonyId() != null
                    && !franchiseeDetails.getReligiousCeremonyId().isEmpty() && !franchiseeDetails.getReligiousCeremonyId().equals("null")) {
                My_vakranggekendra_image myvakranggekendraimagea = new My_vakranggekendra_image();
                myvakranggekendraimagea.setID(14);
                myvakranggekendraimagea.setImgetype("Religious ceremony if any");
                Bitmap img = StringToBitMap(franchiseeDetails.getReligiousCeremonyId());
                myvakranggekendraimagea.setImage(img);
                myvakranggekendraimagea.setImageId(franchiseeDetails.getReligiousCeremonyId());
                myvakranggekendraimagea.setImageHash(CommonUtils.getImageSalt(franchiseeDetails.getReligiousCeremonyId()));
                myvakranggekendraimageList.add(myvakranggekendraimagea);
            }

            if (franchiseeDetails != null && franchiseeDetails.getPublicGatheringId() != null
                    && !franchiseeDetails.getPublicGatheringId().isEmpty() && !franchiseeDetails.getPublicGatheringId().equals("null")) {
                My_vakranggekendra_image myvakranggekendraimagea = new My_vakranggekendra_image();
                myvakranggekendraimagea.setID(15);
                myvakranggekendraimagea.setImgetype("public gathering");
                Bitmap img = StringToBitMap(franchiseeDetails.getPublicGatheringId());
                myvakranggekendraimagea.setImage(img);
                myvakranggekendraimagea.setImageId(franchiseeDetails.getPublicGatheringId());
                myvakranggekendraimagea.setImageHash(CommonUtils.getImageSalt(franchiseeDetails.getPublicGatheringId()));
                myvakranggekendraimageList.add(myvakranggekendraimagea);
            }

            // Check Status and Remarks
            checkStatusAndRemarks();

            internetConnection = new InternetConnection(getActivity());
            gpsTracker = new GPSTracker(getContext());

            adapter = new MyVakrangeeKendraImageAdapter(getContext(), myvakranggekendraimageList);
            recyclerView.setAdapter(adapter);

            btnSubmitPhoto = (Button) view.findViewById(R.id.btnSubmitPhoto);
            btnAddphoto = (Button) view.findViewById(R.id.btnAddphoto);
            btnAddphoto.setTypeface(font);
            btnAddphoto.setText(new SpannableStringBuilder(new String(new char[]{0xf055}) + " " + " " + getResources().getString(R.string.signIn)));

            btnAddphoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageCapturePopup();
                }
            });

            btnSubmitPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadtoserver();
                }
            });

            textviewAddPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageCapturePopup();
                }
            });
            return view;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
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
                franchiseeDetails.setPhotoReviewed(b);

                if (getActivity().getClass().getSimpleName().equalsIgnoreCase("NextGenPhotoViewPager")) {
                    ((NextGenPhotoViewPager) getActivity()).setFranchiseeDetails(franchiseeDetails);
                } else if (getActivity().getClass().getSimpleName().equalsIgnoreCase("MainFinalRMApprovalActivity")) {
                    ((MainFinalRMApprovalActivity) getActivity()).setFranchiseeDetails(franchiseeDetails);
                } else if (getActivity().getClass().getSimpleName().equalsIgnoreCase("SiteReadinessActivity")) {
                    ((SiteReadinessActivity) getActivity()).setFranchiseeDetails(franchiseeDetails);
                }

            }
        });
    }

    //region Check Status and Remarks and Perform Action
    public void checkStatusAndRemarks() {
        // Check Allow to Update Site Visit Data or not.
        if (!franchiseeDetails.isAllowToEdit()) {
            //TODO Disable Add Photo Action
            // Nilesh -15-11-2018 - layoutAddPhoto is visible when RM verified else visible
            setHasOptionsMenu(false);
            layoutAddPhoto.setVisibility(View.GONE);
        } else {
            layoutAddPhoto.setVisibility(View.VISIBLE);
        }

        // Check Status and Set Remarks
        if (franchiseeDetails.getStatus() == NextGenViewPager.SITE_SEND_BACK_FOR_CORRECTION) {
            FranchiseeRemarkDetails franchiseeRemarkDetails = franchiseeDetails.getFranchiseeRemarkDetails();
            if (franchiseeRemarkDetails != null) {

                // Set All Images Remarks
                for (My_vakranggekendra_image kendraPhoto : myvakranggekendraimageList) {
                    switch (kendraPhoto.getID()) {
                        case 0:     // FRONTAGE_IMAGE (5ft Distance)
                            kendraPhoto.setRemarks(franchiseeRemarkDetails.getFrontage_Photo_5ft());
                            break;
                        case 10:     // FRONTAGE_IMAGE (10ft Distance)
                            kendraPhoto.setRemarks(franchiseeRemarkDetails.getFrontage_Photo_10ft());
                            break;
                        case 1:     // LEFT_WALL
                            kendraPhoto.setRemarks(franchiseeRemarkDetails.getLEFT_WALL());
                            break;
                        case 2:     // FRONT_WALL
                            kendraPhoto.setRemarks(franchiseeRemarkDetails.getFRONT_WALL());
                            break;
                        case 3:     // RIGHT WALL
                            kendraPhoto.setRemarks(franchiseeRemarkDetails.getRIGHT_WALL());
                            break;
                        case 4:     // BACK WALL
                            kendraPhoto.setRemarks(franchiseeRemarkDetails.getBACK_WALL());
                            break;
                        case 5:     // CEILING
                            kendraPhoto.setRemarks(franchiseeRemarkDetails.getCEILING());
                            break;
                        case 6:     // FLOOR
                            kendraPhoto.setRemarks(franchiseeRemarkDetails.getFLOOR());
                            break;
                        case 7:     // FRONTAGE OPPOSITE SIDE ROAD
                            kendraPhoto.setRemarks(franchiseeRemarkDetails.getFRONTAGE_OPPOSITE_SIDE_ROAD());
                            break;
                        case 8:     // FRONTAGE LEFT SIDE ROAD
                            kendraPhoto.setRemarks(franchiseeRemarkDetails.getFRONTAGE_LEFT_SIDE_ROAD());
                            break;
                        case 9:     // FRONTAGE RIGHT SIDE ROAD
                            kendraPhoto.setRemarks(franchiseeRemarkDetails.getFRONTAGE_RIGHT_SIDE_ROAD());
                            break;
                        case 11:     // KENDRA_FRONT_OUTSIDE_VIEW
                            kendraPhoto.setRemarks(franchiseeRemarkDetails.getKENDRA_FRONT_OUTSIDE_VIEW());
                            break;
                        case 12:     // KENDRA_INSIDE_VIEW
                            kendraPhoto.setRemarks(franchiseeRemarkDetails.getKENDRA_INSIDE_VIEW());
                            break;
                        case 13:     // RIBBON_CUTTING_CEREMONY
                            kendraPhoto.setRemarks(franchiseeRemarkDetails.getRIBBON_CUTTING_CEREMONY());
                            break;
                        case 14:     // RELIGIOUS_CEREMONY_IF_ANY
                            kendraPhoto.setRemarks(franchiseeRemarkDetails.getRELIGIOUS_CEREMONY_IF_ANY());
                            break;
                        case 15:     // PUBLIC_GATHERING
                            kendraPhoto.setRemarks(franchiseeRemarkDetails.getPUBLIC_GATHERING());
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }
    //endregion

    public void getLocationAndTimestamp() {
        //Get Current location and time stamp
        if (gpsTracker.canGetLocation()) {
            latitude = String.valueOf(gpsTracker.getLatitude());
            longitude = String.valueOf(gpsTracker.getLongitude());
            currentTimestamp = gpsTracker.getFormattedDateTime() == null ? "" : gpsTracker.getFormattedDateTime();
        }
    }

    public void updateProfileData() {

        FranchiseeDetails tempFranchiseeDetails = null;
        if (getActivity().getClass().getSimpleName().equalsIgnoreCase("NextGenPhotoViewPager")) {
            tempFranchiseeDetails = ((NextGenPhotoViewPager) getActivity()).getFranchiseeDetails();


        } else if (getActivity().getClass().getSimpleName().equalsIgnoreCase("MainFinalRMApprovalActivity")) {
            tempFranchiseeDetails = ((MainFinalRMApprovalActivity) getActivity()).getFranchiseeDetails();

        } else if (getActivity().getClass().getSimpleName().equalsIgnoreCase("SiteReadinessActivity")) {
            tempFranchiseeDetails = ((SiteReadinessActivity) getActivity()).getFranchiseeDetails();
        }

        if (tempFranchiseeDetails == null)
            return;
        franchiseeDetails.setConsentStatus(tempFranchiseeDetails.getConsentStatus());
        franchiseeDetails.setWelcomeMailStatus(tempFranchiseeDetails.getWelcomeMailStatus());
        franchiseeDetails.setCallReceivedStatus(tempFranchiseeDetails.getCallReceivedStatus());
        franchiseeDetails.setLogisticsPaymentStatus(tempFranchiseeDetails.getLogisticsPaymentStatus());
        franchiseeDetails.setLogisticsPaymentDate(tempFranchiseeDetails.getLogisticsPaymentDate());
        franchiseeDetails.setGstRegisteredStatus(tempFranchiseeDetails.getGstRegisteredStatus());
        franchiseeDetails.setGstNumber(tempFranchiseeDetails.getGstNumber());
        franchiseeDetails.setGstAddress(tempFranchiseeDetails.getGstAddress());
//        String newImage = tempFranchiseeDetails.getGstImage().replace("*","");
//        String newImage1 = newImage.trim();
        franchiseeDetails.setGstImage(tempFranchiseeDetails.getGstImage());

        franchiseeDetails.setLocationStatus(tempFranchiseeDetails.getLocationStatus());

    }

    private void uploadtoserver() {
        try {

            if (internetConnection.isConnectingToInternet() == false) {
                AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.internetCheck));

            } else {
                Log.d("okay pressed***1", "yesssssssssssss");
                updateProfileData();
                AsyncMyVakrangeeKendra myRequest = new AsyncMyVakrangeeKendra();
                myRequest.execute();

                if (getActivity().getClass().getSimpleName().equalsIgnoreCase("NextGenPhotoViewPager")) {
                    ((NextGenPhotoViewPager) getActivity()).selectFragment(2);

                } else if (getActivity().getClass().getSimpleName().equalsIgnoreCase("MainFinalRMApprovalActivity")) {
                    ((MainFinalRMApprovalActivity) getActivity()).selectFragment(2);

                } else if (getActivity().getClass().getSimpleName().equalsIgnoreCase("SiteReadinessActivity")) {
                    ((SiteReadinessActivity) getActivity()).selectFragment(5);
                }

            }
        } catch (Exception e) {
            e.getMessage();
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

    private void camerapermission() {
        try {
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(getActivity()
                    ,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.CAMERA)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_LOCATION);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
        } catch (Exception e) {
            e.getMessage();

        }
    }

    private void ImageCapturePopup() {
        try {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View layout = inflater.inflate(R.layout.popupaddimage_premise, null);
            builder.setPositiveButton("ok", null);
            builder.setNegativeButton("cancel", null);
            builder.setView(layout);
            builder.setCancelable(false);
            btnCapture = (ImageView) layout.findViewById(R.id.btncapture);
            imageViewa1 = (ImageView) layout.findViewById(R.id.imageView1main);
            spinner = (Spinner) layout.findViewById(R.id.spinnerCategory);

            //Set Spinners
            if (isSiteReadinessVerification) {
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, getContext().getResources().getStringArray(R.array.PremisesPhotographsReadinessVerification));
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                spinner.setAdapter(spinnerArrayAdapter);

            } else {
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, getContext().getResources().getStringArray(R.array.PremisesPhotographs));
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
//                String[] array = getActivity().getResources().getStringArray(R.array.PremisesPhotographs);
//                Spanned[] spannedStrings = new Spanned[12];
//                for(int i=0; i<array.length; i++){
//                    spannedStrings[i] = Html.fromHtml(array[i]);
//                }
//                spinner.setAdapter(new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_dropdown_item,spannedStrings));
                spinner.setAdapter(spinnerArrayAdapter);
            }

            final AlertDialog mAlertDialog = builder.create();


            sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
            currentDateandTime = sdf.format(new Date());

            mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                @Override
                public void onShow(final DialogInterface dialog) {

                    Button b = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    b.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            Log.d("okay --- clicked", (String) spinner.getSelectedItem());
                            showOkAction(dialog);

                        }
                    });


                    Button cancle = mAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                    cancle.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();

                        }
                    });
                }
            });
            mAlertDialog.show();

            btnCapture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        dispatchTakePictureIntent();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void showOkAction(DialogInterface dialog) {
        try {
            getlati = franchiseeDetails.getLatitude();
            imgetype = spinner.getSelectedItem().toString().replace("*", "").trim();
            Log.d("i_type === ", imgetype);
            if (imageViewa1.getDrawable() == null) {
                Toast.makeText(getActivity(), "Please Capture Image", Toast.LENGTH_SHORT).show();
            } else if (spinner.getSelectedItemPosition() == 0) {
                Toast.makeText(getActivity(), "Please Select ", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Log.d(" [[[[[[ ", imgetype);

                if (imgetype.equals("Frontage Image (5ft Distance)")) {
                    Log.d("aafrontageImage [[[[[[ ", imgetype);
                    byte[] bytea = byteArray;
                    Frontage = bytea != null ? (EncryptionUtil.encodeBase64(bytea)) : "";
                    franchiseeDetails.setFrontageImageId(Frontage);
                    Log.d("frontageImage [[[[[[ ", Frontage);

                }
//                else {
//                    franchiseeDetails.setFrontageImageId(null);
//                    Log.d("frontageImage 1[[[[[[ ",Frontage);
//                }
                else if (imgetype.equals("Frontage Image (10ft Distance)")) {

                    byte[] bytea = byteArray;
                    Frontage10 = bytea != null ? (EncryptionUtil.encodeBase64(bytea)) : "";
                    franchiseeDetails.setFrontagePhoto10ftImageId(Frontage10);

                }
//               else {
//                    franchiseeDetails.setFrontagePhoto10ftImageId(null);
//                }
                else if (imgetype.equals("Left Wall Image")) {
                    byte[] bytea = byteArray;
                    byte[] encodeByte1 = bytea;
                    LeftWall = encodeByte1 != null ? (EncryptionUtil.encodeBase64(encodeByte1)) : "";
                    franchiseeDetails.setLeftWallImageId(LeftWall);

                }
//                else {
//                    franchiseeDetails.setLeftWallImageId(null);
//                    Log.e("Front Image ", "Null");
//                }
                else if (imgetype.equals("Front Wall Image")) {
                    byte[] bytea = byteArray;
                    byte[] encodeByte1 = bytea;
                    FrontWall = encodeByte1 != null ? (EncryptionUtil.encodeBase64(encodeByte1)) : "";
                    franchiseeDetails.setFrontWallImageId(FrontWall);

                }
//                else {
//                    franchiseeDetails.setFrontWallImageId(null);
//                }
                else if (imgetype.equals("Right Wall Image")) {
                    Log.d("rightwall -- ", "entry");
                    byte[] bytea = byteArray;
                    byte[] encodeByte1 = bytea;
                    RightWall = encodeByte1 != null ? (EncryptionUtil.encodeBase64(encodeByte1)) : "";
                    franchiseeDetails.setRightWallImageId(RightWall);
                    Log.d("rightwall -- ", franchiseeDetails.getRightWallImageId());

                }
//                else {
//                    franchiseeDetails.setRightWallImageId(null);
//                    Log.d("rightwall -- ","exit");
//                }
                else if (imgetype.equals("Back Wall Image")) {
                    byte[] bytea = byteArray;
                    byte[] encodeByte1 = bytea;
                    BackWall = encodeByte1 != null ? (EncryptionUtil.encodeBase64(encodeByte1)) : "";
                    franchiseeDetails.setBackWallImageId(BackWall);

                }
//                else {
//                    franchiseeDetails.setBackWallImageId(null);
//                }
                else if (imgetype.equals("Ceiling Image")) {
                    byte[] bytea = byteArray;
                    byte[] encodeByte1 = bytea;
                    Ceiling = encodeByte1 != null ? (EncryptionUtil.encodeBase64(encodeByte1)) : "";
                    franchiseeDetails.setCeilingImageId(Ceiling);

                }
//                else {
//                    franchiseeDetails.setCeilingImageId(null);
//                }
                else if (imgetype.equals("Floor Image")) {
                    byte[] bytea = byteArray;
                    byte[] encodeByte1 = bytea;
                    Floor = encodeByte1 != null ? (EncryptionUtil.encodeBase64(encodeByte1)) : "";
                    franchiseeDetails.setFloorImageId(Floor);

                }
//                else {
//                    franchiseeDetails.setFloorImageId(null);
//                }
                else if (imgetype.equals("Frontage Opposite Side Road")) {
                    byte[] bytea = byteArray;
                    byte[] encodeByte1 = bytea;
                    Extra1 = encodeByte1 != null ? (EncryptionUtil.encodeBase64(encodeByte1)) : "";
                    franchiseeDetails.setFrontageOppositeImageId(Extra1);

                }
//                else {
//                    franchiseeDetails.setFrontageOppositeImageId(null);
//
//                }
                else if (imgetype.equals("Frontage Road Left Side") || imgetype.equals("Selfie Indoor")) {
                    byte[] bytea = byteArray;
                    byte[] encodeByte1 = bytea;
                    Extra2 = encodeByte1 != null ? (EncryptionUtil.encodeBase64(encodeByte1)) : "";
                    franchiseeDetails.setFrontageLeftImageId(Extra2);

                }
//                else {
//                    franchiseeDetails.setFrontageLeftImageId(null);
//                }
                else if (imgetype.equals("Frontage Road Right Side") || imgetype.equals("Selfie Outdoor")) {
                    byte[] bytea = byteArray;
                    byte[] encodeByte1 = bytea;
                    Extra3 = encodeByte1 != null ? (EncryptionUtil.encodeBase64(encodeByte1)) : "";
                    franchiseeDetails.setFrontageRightImageId(Extra3);
                    //07-10-2022
                } else if (imgetype.equals("Kendra Front outside view - from 10 ft distance")) {
                    byte[] bytea = byteArray;
                    byte[] encodeByte1 = bytea;
                    KendraFront = encodeByte1 != null ? (EncryptionUtil.encodeBase64(encodeByte1)) : "";
                    franchiseeDetails.setKendraFrontOutsideViewId(KendraFront);

                } else if (imgetype.equals("Kendra Inside view")) {
                    byte[] bytea = byteArray;
                    byte[] encodeByte1 = bytea;
                    KendraInside = encodeByte1 != null ? (EncryptionUtil.encodeBase64(encodeByte1)) : "";
                    franchiseeDetails.setKendraInsideViewId(KendraInside);

                } else if (imgetype.equals("Ribbon cutting ceremony")) {
                    byte[] bytea = byteArray;
                    byte[] encodeByte1 = bytea;
                    RibbonCutting = encodeByte1 != null ? (EncryptionUtil.encodeBase64(encodeByte1)) : "";
                    franchiseeDetails.setRibbonCuttingCeremonyId(RibbonCutting);

                } else if (imgetype.equals("Religious ceremony if any")) {
                    byte[] bytea = byteArray;
                    byte[] encodeByte1 = bytea;
                    ReligiousCeremony = encodeByte1 != null ? (EncryptionUtil.encodeBase64(encodeByte1)) : "";
                    franchiseeDetails.setReligiousCeremonyId(ReligiousCeremony);

                } else if (imgetype.equals("Public Gathering")) {
                    byte[] bytea = byteArray;
                    byte[] encodeByte1 = bytea;
                    PublicGathering = encodeByte1 != null ? (EncryptionUtil.encodeBase64(encodeByte1)) : "";
                    franchiseeDetails.setPublicGatheringId(PublicGathering);

                } else {

                    franchiseeDetails.setFrontageImageId(null);
                    franchiseeDetails.setFrontagePhoto10ftImageId(null);
                    franchiseeDetails.setLeftWallImageId(null);
                    franchiseeDetails.setFrontWallImageId(null);
                    franchiseeDetails.setRightWallImageId(null);
                    franchiseeDetails.setBackWallImageId(null);
                    franchiseeDetails.setCeilingImageId(null);
                    franchiseeDetails.setFloorImageId(null);
                    franchiseeDetails.setFrontageOppositeImageId(null);
                    franchiseeDetails.setFrontageLeftImageId(null);
                    franchiseeDetails.setKendraFrontOutsideViewId(null);
                    franchiseeDetails.setKendraInsideViewId(null);
                    franchiseeDetails.setRibbonCuttingCeremonyId(null);
                    franchiseeDetails.setReligiousCeremonyId(null);
                    franchiseeDetails.setPublicGatheringId(null);
                }

                uploadtoserver();
            }
            Log.d("getList","---"+myvakranggekendraimageList);
            imageViewa1.setImageBitmap(null);
            spinner.setSelection(0);
            dialog.dismiss();
        } catch (Exception e) {
            e.getMessage();
        }
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
                photoFile = createImageFile();
            } catch (Exception ex) {
                ex.printStackTrace();
                // Error occurred while creating the File
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                picUri = FileProvider.getUriForFile(getContext(), getContext().getApplicationContext().getPackageName() + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
                startActivityForResult(takePictureIntent, CAMERA_CAPTURE);
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //Nilesh - 15-11-2018 - setHasOptionsMenu(true) - OptionsMenu add icon display
        setHasOptionsMenu(false);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        SubMenu subMenu1 = menu.addSubMenu("Add Photo");

        MenuItem subMenu1Item = subMenu1.getItem();
        subMenu1Item.setIcon(R.drawable.addod);
        subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        subMenu1Item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                ImageCapturePopup();
                return false;
            }
        });


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        try {

            if (requestCode == CAMERA_CAPTURE && resultCode == Activity.RESULT_OK) {
                getLocationAndTimestamp();

                Uri imageUri = Uri.parse(mCurrentPhotoPath);
                Bitmap bitmapNew = ImageUtils.getBitmap(getActivity().getContentResolver(), imageUri, latitude, longitude, currentTimestamp);
                bitmapNew = ImageUtils.getResizedBitmap(bitmapNew);
                imageViewa1.setImageBitmap(bitmapNew);
                File file = new File(imageUri.getPath());
                ExifInterface exif = new ExifInterface(file.getPath());


                int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                int rotationInDegrees = exifToDegrees(rotation);
                exif.getAttribute(ExifInterface.TAG_GPS_ALTITUDE);

                int width = imageViewa1.getDrawable().getIntrinsicWidth();
                int height = imageViewa1.getDrawable().getIntrinsicHeight();
                if (rotation == 6 && rotationInDegrees == 90) {
                    imageViewa1.setVisibility(View.GONE);
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), "Capture Image only landscape mode");
                    imageViewa1.setImageDrawable(null);

                } else if (height > width) {
                    imageViewa1.setVisibility(View.GONE);
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), "Capture Image only landscape mode");
                    imageViewa1.setImageDrawable(null);

                } else {
                    imageViewa1.setVisibility(View.VISIBLE);
                    String basse64Data = ImageUtils.updateExifData(imageUri, bitmapNew, latitude, longitude, currentTimestamp);
                    byte[] dataa = Base64.decode(basse64Data, Base64.DEFAULT);
                    byteArray = dataa;
                }
            }

        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }

    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private class AsyncMyVakrangeeKendra extends AsyncTask<Void, Void, Void> {

        String vkLatitude = EncryptionUtil.encryptString(franchiseeDetails.getLatitude(), getActivity());
        String vkLongitude = EncryptionUtil.encryptString(franchiseeDetails.getLongitude(), getActivity());


        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");


            progress = new ProgressDialog(getActivity());
            progress.setTitle(R.string.updateTiming);
            progress.setMessage(getResources().getString(R.string.pleaseWait));
            progress.setCancelable(false);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {
                if (isSiteReadinessVerification) {
                    franchiseeDetails.setDesignElements(null);
                    franchiseeDetails.setBranding_element_details(null);

                    String jsonData = JSONUtils.toString(franchiseeDetails);

                    Connection connection = new Connection(getContext());
                    String tmpVkId = connection.getVkid();
                    String data = jsonData;   // Work In Progress Data - JSON
                    String type = "3";
                    EncryptionUtil.encryptString(tmpVkId, getContext());

                    if (isAdhoc) {

                        if (!TextUtils.isEmpty(tmpVkId)) {
                            diplayServerResopnse = WebService.nextgenSiteReadinessAndVerificationUpdate1(tmpVkId, data, type);
                        } else {
                            String enquiryId = CommonUtils.getEnquiryId(getContext());
                            diplayServerResopnse = WebService.nextgenSiteReadinessAndVerificationUpdate1(enquiryId, data, type);
                        }
                    }
                } else {
                    Connection connection = new Connection(getActivity());
                    String vkid = connection.getVkid();
                    String tokenId = connection.getTokenId();

                    String deviceIdget = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
                    String deviceid = EncryptionUtil.encryptString(deviceIdget, getActivity());

                    String deviceIDAndroid = CommonUtils.getAndroidUniqueID(getActivity());
                    String imei = EncryptionUtil.encryptString(deviceIDAndroid, getActivity());

                    String simSerial = CommonUtils.getSimSerialNumber(getActivity());
                    String simserialnumber = EncryptionUtil.encryptString(simSerial, getActivity());

                    String vkidd = EncryptionUtil.encryptString(vkid, getActivity());
                    String TokenId = EncryptionUtil.encryptString(tokenId, getActivity());
                    String type = EncryptionUtil.encryptString("2", getActivity());
                    String jsonData = JSONUtils.toString(franchiseeDetails);
                    String data = jsonData;

                    if (Constants.ENABLE_FRANCHISEE_LOGIN) {
                        if (!TextUtils.isEmpty(vkid)) {
                            diplayServerResopnse = WebService.myVakrangeeKendraFranchiseeDetailsNextgenUpdate(vkid, "2", data);
                        } else {
                            String enquiryId = CommonUtils.getEnquiryId(getContext());
                            diplayServerResopnse = WebService.myVakrangeeKendraFranchiseeDetailsNextgenUpdate2(enquiryId, "2", data);
                        }
                    } else {
                        if (isAdhoc) {
                            diplayServerResopnse = WebService.myVakrangeeKendraFranchiseeDetailsNextgenUpdate(
                                    vkid, "2", data);
                        } else {
                            diplayServerResopnse = WebService.myVakrangeeKendraFranchiseeDetailsNextgenUpdate(
                                    vkidd, TokenId, imei, deviceid, simserialnumber, type, data);
                        }
                    }
                }
                Log.d(TAG, "WebSer...ceResponse: " + diplayServerResopnse);

            } catch (Exception e) {
                AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");

            progress.dismiss();
            try {

                if (diplayServerResopnse == null) {

                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                } else if (diplayServerResopnse.equals("OKAY|")) {
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.imageuploadsuccessful));

                } else {
                    Log.e(TAG + "Error in Server", diplayServerResopnse);
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));

                }


                final Connection connection = new Connection(getContext());
                final String getUserid = franchiseeDetails.getNextGenFranchiseeApplicationNo();
                String vkIdTemp = connection.getVkid();
                final String getVkid = EncryptionUtil.encryptString(vkIdTemp, getContext());
                final String getTokenId = EncryptionUtil.encryptString(connection.getTokenId(), getContext());

                String deviceIdget = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                String getdeviceid = EncryptionUtil.encryptString(deviceIdget, getContext());

                String deviceIDAndroid = CommonUtils.getAndroidUniqueID(getContext());
                String getimei = EncryptionUtil.encryptString(deviceIDAndroid, getContext());

                String simSerial = CommonUtils.getSimSerialNumber(getContext());
                String getsimserialnumber = EncryptionUtil.encryptString(simSerial, getContext());

                final String getid = EncryptionUtil.encryptString(getUserid, getContext());

                if (isAdhoc) {
                    if (isSiteReadinessVerification) {

                        if (!TextUtils.isEmpty(vkIdTemp)) {
                            if (getActivity().getClass().getSimpleName().equalsIgnoreCase("MainFinalRMApprovalActivity")) {
                                Utils.reloadFinalRMReadinessVerificationData(modetype, getContext(), vkIdTemp, franchiseeDetails.getNextGenFranchiseeApplicationId());
                            } else {
                                Utils.reloadReadinessVerificationData(modetype, getContext(), vkIdTemp, franchiseeDetails.getNextGenFranchiseeApplicationId());
                            }
                        } else {
                            String enquiryId = CommonUtils.getEnquiryId(getContext());
                            if (getActivity().getClass().getSimpleName().equalsIgnoreCase("MainFinalRMApprovalActivity")) {
                                Utils.reloadFinalRMReadinessVerificationData(modetype, getContext(), vkIdTemp, franchiseeDetails.getNextGenFranchiseeApplicationId());
                            } else {
                                Utils.reloadReadinessVerificationData(modetype, getContext(), enquiryId, franchiseeDetails.getNextGenFranchiseeApplicationId());
                            }
                        }
                    } else {
                        Utils.updateFranchicess(franchiseeDetails, getContext(), vkIdTemp, vkIdTemp);
                    }
                } else {
                    Utils.updateFranchicess(franchiseeDetails, getContext(), getVkid, getid, getTokenId, getimei, getdeviceid, getsimserialnumber);
                }

                if (getActivity().getClass().getSimpleName().equalsIgnoreCase("NextGenPhotoViewPager")) {
                    ((NextGenPhotoViewPager) getActivity()).viewPager.setCurrentItem(2, true);
                } else if (getActivity().getClass().getSimpleName().equalsIgnoreCase("MainFinalRMApprovalActivity")) {
                    ((MainFinalRMApprovalActivity) getActivity()).viewPager.setCurrentItem(2, true);
                } else {
                    ((SiteReadinessActivity) getActivity()).viewPager.setCurrentItem(5, true);
                }
            } catch (Exception e) {
                AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));

                e.printStackTrace();
            }

        }


    }

}