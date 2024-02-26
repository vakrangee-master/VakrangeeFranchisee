package in.vakrangee.franchisee.sitelayout.mendatorybranding;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.ButterKnife;
import in.vakrangee.franchisee.sitelayout.R;
import in.vakrangee.franchisee.sitelayout.Utils;
import in.vakrangee.franchisee.sitelayout.activity.MyVakrangeeKendraLocationDetailsNextGen;
import in.vakrangee.franchisee.sitelayout.mendatorybranding.model.MandatoryBrandingList;
import in.vakrangee.franchisee.sitelayout.mendatorybranding.model.MandatoryImageList;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.commongui.imagepreview.CustomImagePreviewDialog;
import in.vakrangee.supercore.franchisee.model.FranchiseeDetails;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.AppUtilsforLocationService;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.GPSTracker;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;
import in.vakrangee.supercore.franchisee.utils.Logger;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;
import in.vakrangee.supercore.franchisee.utils.SharedPrefUtils;

public class MandatoryBrandingInteriorFragment extends BaseFragment implements View.OnClickListener, MandatoryBrandingInteriorListAdapter.ISiteCheckHandler {

    private static final String TAG = "MandatoryBrandingInteriorFragment";
    boolean IsValidated;
    private Context context;
    private FranchiseeDetails franchiseeDetails;
    private boolean IsEditable;
    private View view;
    private Logger logger;
    private Uri picUri;
    private String brandingElements;

    private MandatoryBrandingList subCategoryList;
    private static final int CAMERA_PIC_REQUEST = 1;
    private CustomImagePreviewDialog customImagePreviewDialog;
    private GPSTracker gpsTracker;
    private String latitude = "", longitude = "", currentTimestamp = "";
    private PermissionHandler permissionHandler;
    private DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
    private Boolean IsFirstcall = false;
    //private String isFirstTime = "0";
    MaterialButton btnCancel;
    MaterialButton btnClear;
    MaterialButton btnSubmitInterior;
    RecyclerView recyclerViewCategoryList;
    private boolean IsFirstCall = false;
    private String subcategoryName, mImagePreview;
    private String imagebyte,imageBase64;

    private int selectedPos = -1;
    // private int selectedParentPos = -1;
    private MandatoryBrandingList siteReadinessCheckListDto;
    private String FROM = "";

    //Layouts

    private static final String APPROVED_BY_RM = "2";
    private static final String ON_HOLD_BY_RM = "6";
    private static final String SEND_BACK_FOR_CORRECTION_BY_RM = "3";
    private static final String REJECTED_BY_FIELD_TEAM = "7";

    //private List<SiteReadinessCategoryDto> categoryList;
    private List<MandatoryBrandingList> categoryList = new ArrayList<>();
    private MandatoryBrandingInteriorListAdapter categoryListAdapter;
    public List<MandatoryImageList> vkImageList = new ArrayList<>();

    //private AsyncSaveMandatoryBrandingVerification mAsyncSaveMandatoryBrandingVerification = null;

    private Connection connection;
    private boolean IsFranchisee = false;
    private String mCurrentPhotoPath;
    private String mode;
    private String Vkid;


    private MandatoryBrandingInteriorFragment() {
    }

    public MandatoryBrandingInteriorFragment(FranchiseeDetails franchiseeDetails, boolean IsEditable, String mode) {
        this.franchiseeDetails = franchiseeDetails;
        this.IsEditable = IsEditable;
        this.mode = mode;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_mandatory_branding_interior, container, false);
        final Typeface font = android.graphics.Typeface.createFromAsset(getActivity().getAssets(), "fontawesome-webfont.ttf");

        //Initialize data
        this.context = getContext();
        logger = Logger.getInstance(context);
        gpsTracker = new GPSTracker(context);
        connection = new Connection(context);
        permissionHandler = new PermissionHandler(getActivity());
        ButterKnife.bind(this, view);
/*
        Vkid = SharedPrefUtils.getInstance(context).getVKId();
*/

        btnCancel = view.findViewById(R.id.btnCancel);
        btnClear = view.findViewById(R.id.btnClear);
        btnSubmitInterior = view.findViewById(R.id.btnSubmitInterior);
        recyclerViewCategoryList = view.findViewById(R.id.recyclerViewCategoryList);

        btnClear.setTypeface(font);
        btnCancel.setTypeface(font);
        btnSubmitInterior.setTypeface(font);
        // Set Font Text
        btnClear.setText(new SpannableStringBuilder(new String(new char[]{0xf021}) + " " + getResources().getString(R.string.clear)));
        btnCancel.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + "  " + getResources().getString(R.string.cancel)));
        btnSubmitInterior.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " + getResources().getString(R.string.submit)));

        // Add Listener to Buttons
        btnClear.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSubmitInterior.setOnClickListener(this);

        //Interior Category List
        //String data = "[{\"design_element_id\":1,\"design_element_name\":\"Flooring \",\"design_element_description\":\"Flooring \",\"sub_cat\":[{\"design_element_detail_id\":1,\"design_element_name\":\"Flooring \",\"design_element_description\":\"Flooring \",\"mandatory\":1}]},{\"design_element_id\":2,\"design_element_name\":\"Ceiling \",\"design_element_description\":\"Ceiling \",\"sub_cat\":[{\"design_element_detail_id\":2,\"design_element_name\":\"Ceiling with LED Lights \",\"design_element_description\":\"Ceiling with LED Lights \",\"mandatory\":1}]},{\"design_element_id\":3,\"design_element_name\":\"Electrical      \",\"design_element_description\":\"Electrical      \",\"sub_cat\":[{\"design_element_detail_id\":3,\"design_element_name\":\"Concealed Wiring \",\"design_element_description\":\"Concealed Wiring \",\"mandatory\":1},{\"design_element_detail_id\":4,\"design_element_name\":\"Point for Digital Signage \",\"design_element_description\":\"Point for Digital Signage \",\"mandatory\":1},{\"design_element_detail_id\":5,\"design_element_name\":\"UPS/ATM Points \",\"design_element_description\":\"UPS/ATM Points \",\"mandatory\":1},{\"design_element_detail_id\":6,\"design_element_name\":\"CCTV point \",\"design_element_description\":\"CCTV point \",\"mandatory\":1}]},{\"design_element_id\":4,\"design_element_name\":\"Painting      \",\"design_element_description\":\"Painting      \",\"sub_cat\":[{\"design_element_detail_id\":7,\"design_element_name\":\"Left Wall \",\"design_element_description\":\"Left Wall \",\"mandatory\":1},{\"design_element_detail_id\":8,\"design_element_name\":\"Left Wall\",\"design_element_description\":\"Left Wall\",\"mandatory\":1},{\"design_element_detail_id\":9,\"design_element_name\":\"Right Wall\",\"design_element_description\":\"Right Wall\",\"mandatory\":1},{\"design_element_detail_id\":10,\"design_element_name\":\"Back Wall\",\"design_element_description\":\"Back Wall\",\"mandatory\":1},{\"design_element_detail_id\":11,\"design_element_name\":\"Front Wall\",\"design_element_description\":\"Front Wall\",\"mandatory\":1},{\"design_element_detail_id\":12,\"design_element_name\":\"Outside Wall\",\"design_element_description\":\"Outside Wall (If applicable)\",\"mandatory\":1},{\"design_element_detail_id\":13,\"design_element_name\":\"Rolling Shutter\",\"design_element_description\":\"Rolling Shutter\",\"mandatory\":1}]},{\"design_element_id\":5,\"design_element_name\":\"Furniture\",\"design_element_description\":\"Furniture\",\"sub_cat\":[{\"design_element_detail_id\":14,\"design_element_name\":\"Bank Counter Table with Chairs\",\"design_element_description\":\"Bank Counter Table with Chairs - Front View\",\"mandatory\":1},{\"design_element_detail_id\":15,\"design_element_name\":\"Bank Counter Table with Chairs\",\"design_element_description\":\"Bank Counter Table with Chairs - Back View\",\"mandatory\":1},{\"design_element_detail_id\":22,\"design_element_name\":\"Franchisee/Owner desk\",\"design_element_description\":\"Franchisee/Owner desk - Front View\",\"mandatory\":1},{\"design_element_detail_id\":23,\"design_element_name\":\"Waiting Bench\",\"design_element_description\":\"Waiting Bench\",\"mandatory\":1},{\"design_element_detail_id\":24,\"design_element_name\":\"Storage Cabinet \",\"design_element_description\":\"Storage Cabinet \",\"mandatory\":1},{\"design_element_detail_id\":25,\"design_element_name\":\"Store Room/ Storage shelf on wall\",\"design_element_description\":\"Store Room/ Storage shelf on wall\",\"mandatory\":1},{\"design_element_detail_id\":26,\"design_element_name\":\"Main Entrance Glass Door & Fixed Glass\",\"design_element_description\":\"Main Entrance Glass Door & Fixed Glass\",\"mandatory\":1},{\"design_element_detail_id\":33,\"design_element_name\":\"E-Commerce & Logistics services counter table with chairs \",\"design_element_description\":\"E-Commerce & Logistics services counter table with chairs - Front View\",\"mandatory\":1},{\"design_element_detail_id\":34,\"design_element_name\":\"E-Commerce & Logistics services counter table with chairs \",\"design_element_description\":\"E-Commerce & Logistics services counter table with chairs - Back View\",\"mandatory\":1},{\"design_element_detail_id\":35,\"design_element_name\":\"Insurance & Financial services counter table with chairs \",\"design_element_description\":\"Insurance & Financial services counter table with chairs - Front View\",\"mandatory\":1},{\"design_element_detail_id\":36,\"design_element_name\":\"Insurance & Financial services counter table with chairs \",\"design_element_description\":\"Insurance & Financial services counter table with chairs - Back View\",\"mandatory\":1},{\"design_element_detail_id\":37,\"design_element_name\":\"E- Governance & Other services counter table with chairs \",\"design_element_description\":\"E- Governance & Other services counter table with chairs - Front View\",\"mandatory\":1},{\"design_element_detail_id\":38,\"design_element_name\":\"E- Governance & Other services counter table with chairs \",\"design_element_description\":\"E- Governance & Other services counter table with chairs - Back View\",\"mandatory\":1}]},{\"design_element_id\":6,\"design_element_name\":\"Fixtures\",\"design_element_description\":\"Fixtures\",\"sub_cat\":[{\"design_element_detail_id\":27,\"design_element_name\":\"AC\",\"design_element_description\":\"AC\",\"mandatory\":1},{\"design_element_detail_id\":28,\"design_element_name\":\"Water Cooler\",\"design_element_description\":\"Water Cooler\",\"mandatory\":1},{\"design_element_detail_id\":29,\"design_element_name\":\"Dust Bins - Qty 6\",\"design_element_description\":\"Dust Bins - Qty 6\",\"mandatory\":1},{\"design_element_detail_id\":30,\"design_element_name\":\"Inverter/Generator\",\"design_element_description\":\"Inverter/Generator (If applicable)\",\"mandatory\":1},{\"design_element_detail_id\":31,\"design_element_name\":\"Fire extinguisher\",\"design_element_description\":\"Fire extinguisher\",\"mandatory\":1},{\"design_element_detail_id\":32,\"design_element_name\":\"Softboard\",\"design_element_description\":\"Softboard\",\"mandatory\":1}]}]";

        /*String interiorJsonArray = franchiseeDetails.getDesignElements();
        Log.d("rrr", "interiorJsonArrayinteriorJsonArrayinteriorJsonArray------: " + interiorJsonArray);
        Gson gson = new GsonBuilder().create();
        categoryList = gson.fromJson(interiorJsonArray.toString(), new TypeToken<ArrayList<MandatoryBrandingList>>() {
        }.getType());*/

        if (connection.getVkid().startsWith("VL") || connection.getVkid().startsWith("VA")) {
            IsFranchisee = false;
        } else {
            IsFranchisee = true;
        }

        requestCategory();

        return view;
    }

    public void requestCategory() {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.GET, Constants.URL_MANDATORY_BRANDING,
                response -> {
                    Gson gson = new GsonBuilder().create();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        subCategoryList = new MandatoryBrandingList();
                        ArrayList<MandatoryBrandingList> list = gson.fromJson(jsonObject.getJSONObject("responseDTO")
                                .getJSONArray("List").toString(), new TypeToken<ArrayList<MandatoryBrandingList>>() {
                        }.getType());
                        Log.d("Response", "sendVolleyRequest listlistlist : " + list.size());
                        if (list.size() > 0) {

                            for (int i = 0; i < list.size(); i++) {
                                if (mode.equals(Constants.NEXTGEN_SITE_MANDATORY_BRANDING_VERIFICATION)) {
                                    if (Objects.equals(list.get(i).getKendra_category_name(), "Mandatory Branding")) {
                                        subCategoryList.setKendra_sub_category_mandatory(list.get(i).getKendra_sub_category_mandatory());
                                        categoryList.add(list.get(i));


                                    }

                                } else if (mode.equals(Constants.NEXTGEN_SITE_KENDRA_INTERIORS_COMPLETED)) {
                                    if (Objects.equals(list.get(i).getKendra_category_name(), "Interior Photos")) {
                                        subCategoryList.setKendra_sub_category_mandatory(list.get(i).getKendra_sub_category_mandatory());
                                        categoryList.add(list.get(i));
                                    }

                                } else if (mode.equals(Constants.NEXTGEN_SITE_INAUGURATION_RELUNCH_COMPLETED)) {
                                    if (Objects.equals(list.get(i).getKendra_category_name(), "Re-launch or Inauguration Photos")) {
                                        subCategoryList.setKendra_sub_category_mandatory(list.get(i).getKendra_sub_category_mandatory());
                                        categoryList.add(list.get(i));
                                    }

                                }
                            }
                        }

                        requestReturnImage();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.d("ERROR", "sendVolleyRequest error => " + error);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("api-key", "0bb6c340-85cb-42f5-8a22-192637df06c5");
                return params;
            }
        };
        queue.add(request);
    }

    public void saveImage() {
        showProgressSpinner(context);
        IsFirstCall = true;
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, Constants.URL_KDB_IMAGE_UP,
                response -> {
                    dismissProgressSpinner();
                    //Gson gson = new GsonBuilder().create();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Log.d("saveImage", "sendVolleyResponse jsonObject => " + jsonObject);
                        btnSubmitInterior.setVisibility(View.VISIBLE);
                        if (TextUtils.isEmpty(response)) {
                            AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                            return;
                        }

                        if (jsonObject.getJSONObject("responseDTO").getString("status").equals("00")) {
                            AlertDialogBoxInfo.alertDialogShow(getActivity(), jsonObject.getJSONObject("responseDTO").getString("toast"));
                            String vkIdTemp = connection.getVkid();
                            String lastSelectedMode = SharedPrefUtils.getStr("mode");
                            Utils.reloadMandatoryBrandingVerificationData(lastSelectedMode, getContext(), vkIdTemp, franchiseeDetails.getNextGenFranchiseeApplicationId());
                            ((MandatoryBrandingActivity) getActivity()).selectFragment(2);

                        } else {
                            AlertDialogBoxInfo.showOkDialog(getActivity(), getResources().getString(R.string.alert_msg_readiness_verification_fail));
                        }

                    } catch (JSONException e) {
                        Log.d("saveImage", "sendVolleyResponse JSONException => " + e.getMessage());
                        AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                        e.printStackTrace();
                    }
                },
                error -> {
                    dismissProgressSpinner();
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                    Log.d("saveImage", "sendVolleyResponse error => " + error);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("api-key", "0bb6c340-85cb-42f5-8a22-192637df06c5");
                params.put("Content-Type", "application/json");
                return params;
            }

            @Override
            public byte[] getBody() {
                HashMap<String, String> params = new HashMap<>();
                JSONArray images = new JSONArray();
                for (int i = 0; i < categoryList.size(); i++) {
                    if (categoryList.get(i).imageByte != null) {
                        JSONObject categoryValue = new JSONObject();
                        try {
                            categoryValue.put("imageByte", categoryList.get(i).imageByte);
                            categoryValue.put("subCategory", categoryList.get(i).nextgen_site_work_kendra_sub_category_id + "");
                            categoryValue.put("status", categoryList.get(i).getStatus());
                            images.put(categoryValue);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                //params.put("vkid", "MH1801167");
                params.put("vkid", Vkid);
                for (int i = 0; i < categoryList.size(); i++) {
                    params.put("category", categoryList.get(i).nextgen_site_work_kendra_category_id + "");
                }
                // params.put("appId", "FA887293232");
                params.put("appId", franchiseeDetails.getNextGenFranchiseeApplicationId());
                params.put("latitude", gpsTracker.getLatitude() + "");
                params.put("longitude", gpsTracker.getLongitude() + "");
                params.put("address", franchiseeDetails.getVAddress());
                //params.put("address", GPSUtil.getCompleteAddressString(context,gpsTracker.getLatitude(),gpsTracker.getLongitude()));
                params.put("images", String.valueOf(images));
                Log.d("saveImage", "sendVolleyRequest params => " + params);
                return new JSONObject(params).toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public void requestReturnImage() {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, Constants.URL_KDB_GET_IMAGE,
                response -> {
                    dismissProgressSpinner();
                    Gson gson = new GsonBuilder().create();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (!jsonObject.getJSONObject("responseDTO").getString("status").equals("05")) {
                            MandatoryImageList mandatoryImageLists = new MandatoryImageList();
                            if (jsonObject.getString("httpStatusCode").equalsIgnoreCase("200")) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject("responseDTO");
                                {
                                    ArrayList<MandatoryImageList> bucket = gson.fromJson(jsonObject.getJSONObject("responseDTO")
                                            .getJSONArray("imgByte").toString(), new TypeToken<ArrayList<MandatoryImageList>>() {
                                    }.getType());
                                    if (jsonObject1.has("imgByte")) {
                                        JSONArray jsonArray = jsonObject1.getJSONArray("imgByte");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                            mandatoryImageLists.setimgBytes(jsonObject2.getString("imgBytes"));
                                            mandatoryImageLists.setCategoryId(jsonObject2.getString("categoryId"));
                                            mandatoryImageLists.setCategoryName(jsonObject2.getString("categoryName"));
                                            mandatoryImageLists.setSubCategoryName(jsonObject2.getString("subCategoryName"));
                                            mandatoryImageLists.setStatus(jsonObject2.getString("status"));
                                            //mandatorylist.add(jsonArray.get(i).toString());
                                            vkImageList.add(bucket.get(i));
                                            Log.d("requestReturnImage", "mandatoryList => " + vkImageList);
                                        }
                                    }
                                }


                            }
                        }
                        categoryListAdapter = new MandatoryBrandingInteriorListAdapter(context, IsFranchisee, Constants.NEXTGEN_SITE_MANDATORY_BRANDING_VERIFICATION, categoryList, vkImageList, this);
                        recyclerViewCategoryList.setItemAnimator(new DefaultItemAnimator());
                        recyclerViewCategoryList.setAdapter(categoryListAdapter);

                    } catch (JSONException e) {
                        Log.d("saveImage", "sendVolleyResponse JSONException => " + e.getMessage());
                        AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                        e.printStackTrace();
                    }
                },
                error -> {
                    dismissProgressSpinner();
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));

                    Log.d("saveImage", "sendVolleyResponse error => " + error);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("api-key", "0bb6c340-85cb-42f5-8a22-192637df06c5");
                params.put("Content-Type", "application/json");
                return params;
            }

            @Override
            public byte[] getBody() {
                HashMap<String, String> params = new HashMap<>();
                for (int i = 0; i < categoryList.size(); i++) {
                    params.put("appId", franchiseeDetails.getNextGenFranchiseeApplicationId());
                    params.put("categoryId", categoryList.get(i).nextgen_site_work_kendra_category_id + "");
                    params.put("subCategoryId", "0");


                }
                return new JSONObject(params).toString().getBytes();
             /*   HashMap<String, String> params = new HashMap<>();
                params.put("appId", franchiseeDetails.getNextGenFranchiseeApplicationId());
                //params.put("appId", "288570");
                params.put("categoryId","0");
                params.put("subCategoryId","0");*/

                // return new JSONObject(params).toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        queue.add(request);


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
                }else{
                    Toast.makeText(context, getResources().getString(R.string.needs_permission_camera_msg), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
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
        //   if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
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
            picUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", photoFile);

            // picUri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
            startActivityForResult(takePictureIntent, CAMERA_PIC_REQUEST);
        }
       /* } else {
            Log.d("ImplicitIntents", "Can't handle this intent!");
        }*/
    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();

        if (Id == R.id.btnSubmitInterior) {

            //Validate Location in case of BE/DE and Franchisee
            //if (connection.getVkid().startsWith("VL") || connection.getVkid().startsWith("VA")) {

            // btnSubmitInterior.setClickable(false);
            btnSubmitInterior.setVisibility(View.VISIBLE);
            boolean IsLocationWithinRange = IsValidLocation();
            if (!IsLocationWithinRange) {
                //  btnSubmitInterior.setClickable(true);
                btnSubmitInterior.setVisibility(View.VISIBLE);
                AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.not_valid_location));
                return;
            } else {
                btnSubmitInterior.setVisibility(View.VISIBLE);
                // btnSubmitInterior.setClickable(false);
            }
            save();
            // saveImage();
            /*} else {
                save();
            }*/
        } else if (Id == R.id.btnCancel) {
            cancel();
        }
    }

    public boolean IsValidLocation() {
        if (gpsTracker.canGetLocation()) {
            if (TextUtils.isEmpty(franchiseeDetails.getLatitude()) || TextUtils.isEmpty(franchiseeDetails.getLongitude())
                    || franchiseeDetails.getWipLocationRange() == 0)
                return true;

            int distance = (int) gpsTracker.getDistance(Double.parseDouble(franchiseeDetails.getLatitude()),
                    Double.parseDouble(franchiseeDetails.getLongitude()));
            if (distance <= franchiseeDetails.getWipLocationRange())
                return true;
        }
        return false;
    }

    public void save() {
        //showProgressSpinner(context);
            IsValidated = IsInteriorElementsValidated();

        dismissProgressSpinner();

        if (IsValidated) {
            //added by -Niket
            btnSubmitInterior.setVisibility(View.VISIBLE);
            saveImage();
        } else {
            btnSubmitInterior.setEnabled(false);

        }

    }

    public void cancel() {
        if (connection.getVkid().toUpperCase().startsWith("VL") || connection.getVkid().toUpperCase().startsWith("VA")) {
            Intent intent = new Intent(context, MyVakrangeeKendraLocationDetailsNextGen.class);
            intent.putExtra("MODE", Constants.NEXTGEN_SITE_MANDATORY_BRANDING_VERIFICATION);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        } else {
           /* Intent intent = new Intent(context, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);*/
            getActivity().finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK) {
            try {
                Uri imageUri = Uri.parse(mCurrentPhotoPath);
                picUri = imageUri;

                getLocationAndTimestamp();
                Bitmap bitmapNew = ImageUtils.getBitmap(context.getContentResolver(), picUri, latitude, longitude, currentTimestamp);

                //BitMap with TimeStamp on it
                bitmapNew = ImageUtils.stampWithTimeInBitmap(bitmapNew);

                /*//START: TEsting
                String capturedAt = GPSUtil.getCompleteAddressString(context, Double.parseDouble(latitude), Double.parseDouble(longitude));
                bitmapNew = ImageUtils.stampWithTimeAndAddressInBitmap(bitmapNew, capturedAt);
                //END*/

                 imageBase64 = ImageUtils.updateExifData(picUri, bitmapNew, latitude, longitude, currentTimestamp);

                ImageView imageView = new ImageView(context);
                imageView.setImageBitmap(bitmapNew);

                if (!CommonUtils.isLandscapePhoto(picUri, imageView)) {
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getString(R.string.landscape_mode_allowed));
                } else {
                    MandatoryBrandingList checkListDto = savePhoto(bitmapNew, imageBase64);
                    //SiteReadinessCheckListDto checkListDto = savePhoto(bitmapNew, imageBase64);
                    //checkListDto.setChangedPhoto(true);
                    checkListDto.setChecked(true);
                    SharedPrefUtils.getInstance(context).setStr("clickImage", imageBase64);
                    showImagePreviewDialog((Object) checkListDto,imageBase64);
                }

            } catch (Exception e) {
                e.printStackTrace();
                AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
            }
        }
    }

    public void getLocationAndTimestamp() {
        //Get Current location and time stamp
        if (gpsTracker.canGetLocation()) {
            latitude = String.valueOf(gpsTracker.getLatitude());
            longitude = String.valueOf(gpsTracker.getLongitude());
            currentTimestamp = gpsTracker.getFormattedDateTime();
        }
    }

    private void showImagePreviewDialog(Object object,String imageBase64) {

        boolean IsRejected = false;
       /* if (!TextUtils.isEmpty(siteReadinessCheckListDto.getStatus()) && (siteReadinessCheckListDto.getStatus().equalsIgnoreCase(SEND_BACK_FOR_CORRECTION_BY_RM) || siteReadinessCheckListDto.getStatus().equalsIgnoreCase(REJECTED_BY_FIELD_TEAM)))
            IsRejected = true;*/

        if (customImagePreviewDialog != null && customImagePreviewDialog.isShowing()) {
            customImagePreviewDialog.refresh(object);
            //  customImagePreviewDialog.allowImageRejected(IsRejected && !siteReadinessCheckListDto.isChangedPhoto(), siteReadinessCheckListDto.getStatus());
            return;
        }

        if (object != null) {
            customImagePreviewDialog = new CustomImagePreviewDialog(context, object,imageBase64,brandingElements,new CustomImagePreviewDialog.IImagePreviewDialogClicks() {

                @Override
                public void capturePhotoClick() {
                    boolean IsLocationWithinRange = IsValidLocation();

                    //GPS OFF
                    if (!IsLocationEnabled()) {
                        displayAlertToEnableLocation();
                        return;
                    }

                    //Location Range
                    if (!IsLocationWithinRange) {
                        AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.not_valid_location));
                        return;
                    }
                    startCamera(view);
                }

                @Override
                public void OkClick(Object object) {
                    // List<SiteReadinessCheckListDto> siteCheckList = categoryList.get(selectedParentPos).checkList;
                    //  SiteReadinessCheckListDto siteReadinessCheckListDto = ((SiteReadinessCheckListDto) object);
                    // siteCheckList.set(selectedPos, siteReadinessCheckListDto);

                    //Parent Dto
                    MandatoryBrandingList categoryDto = categoryList.get(selectedPos);
                    // categoryDto.checkList = siteCheckList;
                    categoryList.set(selectedPos, categoryDto);
                    categoryListAdapter.updateRefreshedPosition(selectedPos);
                    categoryDto.setImageByte(imageBase64);
                    SharedPrefUtils.getInstance(context).setStr("clickImage", imageBase64);
                    categoryListAdapter.notifyDataSetChanged();

                }


            });
            customImagePreviewDialog.show();
            customImagePreviewDialog.setCancelable(false);

            boolean IsChangePhotoEnabled = true;
          /*  if (!TextUtils.isEmpty(siteReadinessCheckListDto.getStatus()) && (siteReadinessCheckListDto.getStatus().equalsIgnoreCase(APPROVED_BY_RM) || siteReadinessCheckListDto.getStatus().equalsIgnoreCase(ON_HOLD_BY_RM)))
                IsChangePhotoEnabled = false;*/

            customImagePreviewDialog.allowChangePhoto(IsChangePhotoEnabled);
            customImagePreviewDialog.allowSaveOption(IsChangePhotoEnabled);
            // customImagePreviewDialog.allowImageRejected(IsRejected && !siteReadinessCheckListDto.isChangedPhoto(), siteReadinessCheckListDto.getStatus());
            customImagePreviewDialog.allowRemarks(false);

        } else {
            Toast.makeText(getActivity(), "No photo available to preview.", Toast.LENGTH_SHORT).show();
        }
    }

    public MandatoryBrandingList savePhoto(Bitmap bitmap, String imageBase64) {
        categoryList.get(selectedPos).setChecked(true);
        // List<SiteReadinessCheckListDto> siteCheckList = categoryList.get(selectedParentPos).checkList;
        List<MandatoryBrandingList> siteCheckList = categoryList;

        MandatoryBrandingList imageDto = siteCheckList.get(selectedPos);
        // SiteReadinessCheckListDto imageDto = siteCheckList.get(selectedPos);
        imageDto.setImageByte(imageBase64);
        imageDto.setChecked(true);
       /* imageDto.setPhoto(imageBase64);
        imageDto.setElementUri(picUri);
        imageDto.setElementImgBitmap(bitmap);
        imageDto.setElementImageBase64(imageBase64);
        imageDto.setCapturedDateTime(dateTimeFormat.format(new Date()));*/
        return imageDto;

    }

    @Override
    public void cameraClick(int parentPosition, int position, MandatoryBrandingList checkListDto) {
        // FROM = checkListDto.getTYPE();
        selectedPos = position;

        siteReadinessCheckListDto = checkListDto;
        //  Vkid = SharedPrefUtils.getInstance(context).getVKId();

        subcategoryName = checkListDto.kendra_sub_category_name;
        imagebyte = checkListDto.imageByte;
        SharedPrefUtils.getInstance(context).setStr("SubCategoryName", subcategoryName);
        SharedPrefUtils.getInstance(context).setStr("imageByte", imagebyte);
        // if (TextUtils.isEmpty(checkListDto.getElementImageBase64())) {
        if (TextUtils.isEmpty(checkListDto.getImageByte())) {
            boolean IsLocationWithinRange = IsValidLocation();

            //GPS OFF
            if (!IsLocationEnabled()) {
                displayAlertToEnableLocation();
                return;
            }

            //Location Range
            if (!IsLocationWithinRange) {
                AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.not_valid_location));
                return;
            }

            startCamera(view);
        } else {
            showImagePreviewDialog(checkListDto,imageBase64);
        }
    }

    public boolean IsInteriorElementsValidated() {

        for (int i = 0; i < categoryList.size(); i++) {
            if (categoryList.get(i).getKendra_sub_category_mandatory().equals("1")) {
                MandatoryBrandingList categoryDto = categoryList.get(i);
                //List<SiteReadinessCheckListDto> checkList = categoryDto.checkList;

                //Validate Each Category
                if (imagebyte != null) {
                    int status = IsSubElementsValidated(categoryList);
                    if (status != -1) {
                        String msg = "Please capture missing photo of " + categoryList.get(status).getKendra_category_name();
                        // String msg = "Please capture missing photo of " + categoryList.get(status).getElementName() + " under Category: " + categoryDto.getName() + ", If it is completed.";
                        //  String msg = "Please capture missing photo of " + categoryList.get(status).getKendra_category_name() + " under Category: " + categoryList.get(status).getKendra_sub_category_name() + ", If it is completed.";
                        AlertDialogBoxInfo.alertDialogShow(context, msg);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public int IsSubElementsValidated(List<MandatoryBrandingList> siteCheckList) {

        for (int i = 0; i < siteCheckList.size(); i++) {
            MandatoryBrandingList checkListDto = siteCheckList.get(i);

            //Validate If Completed and Photo is taken
            if (!checkListDto.isChecked()) {
                if (TextUtils.isEmpty(checkListDto.getImageByte()))
                    return i;
            }
        }
        return -1;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       /* if (mAsyncSaveMandatoryBrandingVerification != null && !mAsyncSaveMandatoryBrandingVerification.isCancelled()) {
            mAsyncSaveMandatoryBrandingVerification.cancel(true);
        }*/
    }

    public boolean IsLocationEnabled() {
        if (AppUtilsforLocationService.checkPlayServices(getActivity())) {
            if (AppUtilsforLocationService.isLocationEnabled(context))
                return true;
        } else {
            Toast toast = Toast.makeText(context, "Location not supported in this device", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        return false;
    }

    public void displayAlertToEnableLocation() {
        // notify user
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage("Please enable GPS/Location.");
        dialog.setPositiveButton("Open location settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(myIntent);
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
            }
        });
        dialog.show();
    }
}