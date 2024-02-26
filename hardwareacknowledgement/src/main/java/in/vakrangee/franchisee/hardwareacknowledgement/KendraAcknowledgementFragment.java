package in.vakrangee.franchisee.hardwareacknowledgement;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import java.io.Serializable;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import butterknife.ButterKnife;
import in.vakrangee.franchisee.nextgenfranchiseeapplication.CustomFranchiseeApplicationSpinnerAdapter;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.commongui.DateTimePickerDialog;
import in.vakrangee.supercore.franchisee.commongui.FileAttachmentDialog;
import in.vakrangee.supercore.franchisee.commongui.FileUtils;
import in.vakrangee.supercore.franchisee.commongui.FilteredFilePickerActivity;
import in.vakrangee.supercore.franchisee.commongui.imagegallery.ImageDto;
import in.vakrangee.supercore.franchisee.commongui.imagegallery.ImageGalleryFragmentWithGlide;
import in.vakrangee.supercore.franchisee.commongui.imagepreview.ImagePreview_zoom_Dialog;
import in.vakrangee.supercore.franchisee.ifc.IImagesHandler;
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
import in.vakrangee.supercore.franchisee.utils.Logger;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;

@SuppressLint("ValidFragment")
public class KendraAcknowledgementFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "KendraAcknowledgementFragment";

    private View view;
    private Context context;
    private PermissionHandler permissionHandler;
    private Logger logger;
    private DeprecateHandler deprecateHandler;
    private KendraAcknowledgementDto kendraAcknowledgementDto;

    private FileAttachmentDialog fileAttachementDialog;
    private static final int CAMERA_PIC_REQUEST = 100;
    private static final int BROWSE_FOLDER_REQUEST = 101;
    private Uri picUri;                 //Picture URI

    private DateTimePickerDialog dateTimePickerDialog;
    private DateFormat dateFormatter2 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    private static final String SERIES_NAME = "SERIES_NAME";
    private static final String MODEL_NAME = "MODEL_NAME";
    private static final String IMAGES = "images";
    private static final String YYYY_MM_DD_HH_MM_SS_CONST = "yyyy-MM-dd HH:mm:ss";
    private DateFormat dateTimeFormatter = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS_CONST, Locale.US);
    private Date receivedDate;
    private String strReceivedDate;
    private String SEL_FILE_TYPE;
    private int selectedDateTimeId = 0;


    //endregion

    private KendraAcknowledgementRepository kendraAckRepo;
    private List<CustomFranchiseeApplicationSpinnerDto> brandNameList;
    private List<CustomFranchiseeApplicationSpinnerDto> seriesList;
    private List<CustomFranchiseeApplicationSpinnerDto> modelList;
    private List<CustomFranchiseeApplicationSpinnerDto> goodsConditionList;

    private GetAllKendraAckSpinnerData getAllKendraAckSpinnerData = null;
    private GetAllAcknowledgementData getAllAcknowledgementData = null;
    private AsyncSaveKendraAckDetails asyncSaveKendraAckDetails = null;
    private String materialCode = null;
    private String statusCode = null;
    private boolean IsServiceCall = false;
    private String assetsTracking;
    private ImagePreview_zoom_Dialog imagePreview_zoom_dialog;

    private ImageGalleryFragmentWithGlide fragmentEquipImages;
    private List<ImageDto> imagesList = new ArrayList<ImageDto>();

    private TextView txtMaterialName;
    private ScrollView ackScrollview;
    private LinearLayout layoutBrandName;
    private TextView txtBrandNameLbl;
    private Spinner spinnerBrandName;
    private LinearLayout layoutSeries;
    private TextView txtSeriesLbl;
    private Spinner spinnerSeries;
    private LinearLayout layoutModel;
    private TextView txtModelLbl;
    private Spinner spinnerModel;
    private LinearLayout layoutSerialNo;
    private TextView txtSerialNoLbl;
    private EditText editTextSerialNo;
    private LinearLayout layoutGoodsCondition;
    private TextView txtGoodsConditionLbl;
    private Spinner spinnerGoodsCondition;
    private TextView txtGoodsConditionNote;
    private LinearLayout layoutReceivedDate;
    private TextView txtReceivedDateLbl;
    private TextView textViewReceivedDate;
    private LinearLayout layoutEquipmentImage;
    private TextView txtEquipmentImageLbl;
    private ImageView imgEquipmentImage;
    private TextView txtEquipmentImageName;
    private LinearLayout layoutUploadEquipmentPackagingImage;
    private TextView txtUploadEquipmentPackagingImageLbl;
    private TextView txtUploadEquipmentPackagingImageIcn;
    private ImageView imgUploadEquipmentPackagingImage;
    private TextView txtUploadEquipmentPackagingImageName;
    private LinearLayout layoutRemarks;
    private TextView txtRemarksLbl;
    private EditText editTextRemarks;
    private LinearLayout layoutFooter;
    private Button btnCancel;
    private Button btnSubmitAck;
    private ImageView imgBarcodeScan;

    public KendraAcknowledgementFragment() {
    }

    public KendraAcknowledgementFragment(KendraAcknowledgementDto kendraAcknowledgementDto) {
        this.kendraAcknowledgementDto = kendraAcknowledgementDto;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag_kendra_acknowledgement, container, false);

        bindViewId(view);
        //Initialize data
        this.context = getContext();
        logger = Logger.getInstance(context);
        deprecateHandler = new DeprecateHandler(context);
        permissionHandler = new PermissionHandler(getActivity());
        kendraAckRepo = new KendraAcknowledgementRepository(context);

        ButterKnife.bind(this, view);
        CommonUtils.InputFiletrWithMaxLength(editTextRemarks, "~#^|$%&*!", 100);

        fragmentEquipImages = (ImageGalleryFragmentWithGlide) getChildFragmentManager().findFragmentById(R.id.fragmentEquipmentImages);

        final Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        btnCancel.setTypeface(font);
        btnSubmitAck.setTypeface(font);

        // Set Font Text
        btnCancel.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + "  " + getResources().getString(R.string.cancel)));
        btnSubmitAck.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " + getResources().getString(R.string.submit)));

        //Set Compulsory mark
        TextView[] txtViewsForCompulsoryMark = {txtBrandNameLbl, txtSeriesLbl, txtModelLbl, txtGoodsConditionLbl, txtEquipmentImageLbl, //txtUploadEquipmentPackagingImageLbl,
                txtReceivedDateLbl, txtRemarksLbl};
        GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);

        //Listeners
        imgBarcodeScan.setOnClickListener(this);

        return view;
    }

    private void bindViewId(View view) {
        //region References
        txtMaterialName = view.findViewById(R.id.txtMaterialName);
        ackScrollview = view.findViewById(R.id.ackScrollview);
        layoutBrandName = view.findViewById(R.id.layoutBrandName);
        txtBrandNameLbl = view.findViewById(R.id.txtBrandNameLbl);
        spinnerBrandName = view.findViewById(R.id.spinnerBrandName);
        layoutSeries = view.findViewById(R.id.layoutSeries);
        txtSeriesLbl = view.findViewById(R.id.txtSeriesLbl);
        spinnerSeries = view.findViewById(R.id.spinnerSeries);
        layoutModel = view.findViewById(R.id.layoutModel);
        txtModelLbl = view.findViewById(R.id.txtModelLbl);
        spinnerModel = view.findViewById(R.id.spinnerModel);
        layoutSerialNo = view.findViewById(R.id.layoutSerialNo);
        txtSerialNoLbl = view.findViewById(R.id.txtSerialNoLbl);
        editTextSerialNo = view.findViewById(R.id.editTextSerialNo);
        layoutGoodsCondition = view.findViewById(R.id.layoutGoodsCondition);
        txtGoodsConditionLbl = view.findViewById(R.id.txtGoodsConditionLbl);
        spinnerGoodsCondition = view.findViewById(R.id.spinnerGoodsCondition);
        txtGoodsConditionNote = view.findViewById(R.id.txtGoodsConditionNote);
        layoutReceivedDate = view.findViewById(R.id.layoutReceivedDate);
        txtReceivedDateLbl = view.findViewById(R.id.txtReceivedDateLbl);
        textViewReceivedDate = view.findViewById(R.id.textViewReceivedDate);
        layoutEquipmentImage = view.findViewById(R.id.layoutEquipmentImage);
        txtEquipmentImageLbl = view.findViewById(R.id.txtEquipmentImageLbl);
        imgEquipmentImage = view.findViewById(R.id.imgEquipmentImage);
        txtEquipmentImageName = view.findViewById(R.id.txtEquipmentImageName);
        layoutUploadEquipmentPackagingImage = view.findViewById(R.id.layoutUploadEquipmentPackagingImage);
        txtUploadEquipmentPackagingImageLbl = view.findViewById(R.id.txtUploadEquipmentPackagingImageLbl);
        txtUploadEquipmentPackagingImageIcn = view.findViewById(R.id.txtUploadEquipmentPackagingImageIcn);
        imgUploadEquipmentPackagingImage = view.findViewById(R.id.imgUploadEquipmentPackagingImage);
        txtUploadEquipmentPackagingImageName = view.findViewById(R.id.txtUploadEquipmentPackagingImageName);
        layoutRemarks = view.findViewById(R.id.layoutRemarks);
        txtRemarksLbl = view.findViewById(R.id.txtRemarksLbl);
        editTextRemarks = view.findViewById(R.id.editTextRemarks);
        layoutFooter = view.findViewById(R.id.layoutFooter);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnSubmitAck = view.findViewById(R.id.btnSubmitAck);
        imgBarcodeScan = view.findViewById(R.id.imgBarcodeScan);
        //spineer click
        spinnerBrandName.setOnItemSelectedListener(this);
        spinnerSeries.setOnItemSelectedListener(this);
        spinnerModel.setOnItemSelectedListener(this);
        spinnerGoodsCondition.setOnItemSelectedListener(this);
        //button click
        imgEquipmentImage.setOnClickListener(this);
        imgUploadEquipmentPackagingImage.setOnClickListener(this);
        textViewReceivedDate.setOnClickListener(this);
        btnSubmitAck.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        txtUploadEquipmentPackagingImageLbl.setOnClickListener(this);
        txtUploadEquipmentPackagingImageIcn.setOnClickListener(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        permissionHandler.dismissSnackbar();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK) {

                Bitmap bitmapNew = ImageUtils.getBitmap(getActivity().getContentResolver(), picUri, "", "", "");
                bitmapNew = ImageUtils.getResizedBitmap(bitmapNew);
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
                        bitmapNew = ImageUtils.getResizedBitmap(bitmapNew);
                        base64Data = CommonUtils.convertBitmapToString(bitmapNew);
                    }

                    String fileName = URLDecoder.decode(file.getName(), "UTF-8");
                    setImageAndName(fileName, base64Data, uri);
                }
            }
            if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                StringTokenizer tokens1 = new StringTokenizer(result, "|");
                String key = tokens1.nextToken();
                if (key.equals("OKAY")) {
                    String response = tokens1.nextToken();
                    editTextSerialNo.setText(response);
                } else {
                    Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
                }
            }

            if (requestCode == 300 && resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                StringTokenizer tokens1 = new StringTokenizer(result, "|");
                String key = tokens1.nextToken();
                if (key.equals("OKAY")) {
                    String response = tokens1.nextToken();
                    editTextSerialNo.setText(response);
                } else {
                    Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setImageAndName(String fileName, String base64Data, Uri uri) {
        try {
            String ext = FileUtils.getFileExtension(context, uri);
            if (SEL_FILE_TYPE.equalsIgnoreCase(IMAGES)) {
                Bitmap imageBitMap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                Glide.with(context).asBitmap().load(imageBitMap).into(imgUploadEquipmentPackagingImage);

                txtUploadEquipmentPackagingImageName.setVisibility(View.GONE);
                txtUploadEquipmentPackagingImageName.setText(fileName);

                kendraAcknowledgementDto.setEquipmentPackagingBase64(base64Data);
                kendraAcknowledgementDto.setEquipmentPackagingExt(ext);
                kendraAcknowledgementDto.setEquipmentPackagingName(fileName);

            } else {
                if (ext.equalsIgnoreCase("pdf")) {
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgUploadEquipmentPackagingImage);
                } else {
                    Bitmap imageBitMap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    Glide.with(context).asBitmap().load(imageBitMap).into(imgUploadEquipmentPackagingImage);
                }

                txtUploadEquipmentPackagingImageName.setVisibility(View.VISIBLE);
                txtUploadEquipmentPackagingImageName.setText(fileName);

                kendraAcknowledgementDto.setEquipmentPackagingBase64(base64Data);
                kendraAcknowledgementDto.setEquipmentPackagingExt(ext);
                kendraAcknowledgementDto.setEquipmentPackagingName(fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showAttachmentDialog(final View view, final String fileType) {
        fileAttachementDialog = new FileAttachmentDialog(context, true, new FileAttachmentDialog.IFileAttachmentClicks() {
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
        fileAttachementDialog.setTitle("Capture Photo");
        fileAttachementDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getAllKendraAckSpinnerData != null && !getAllKendraAckSpinnerData.isCancelled()) {
            getAllKendraAckSpinnerData.cancel(true);
        }

        if (getAllAcknowledgementData != null && !getAllAcknowledgementData.isCancelled()) {
            getAllAcknowledgementData.cancel(true);
        }

        if (asyncSaveKendraAckDetails != null && !asyncSaveKendraAckDetails.isCancelled()) {
            asyncSaveKendraAckDetails.cancel(true);
        }

        //Date Time DIalog
        if (dateTimePickerDialog != null && dateTimePickerDialog.isShowing()) {
            dateTimePickerDialog.dismiss();
            dateTimePickerDialog = null;
        }

        //FileAttachement Dialog
        if (fileAttachementDialog != null && fileAttachementDialog.isShowing()) {
            fileAttachementDialog.dismiss();
            fileAttachementDialog = null;
        }
    }

    public void reloadData(String data, String statusCode, String materialCode, String materialName, boolean IsServiceCall, String euipAckId, String kendraEuipId, String standEquiId, String assetsTracking) {
        //Reload Data
        if (TextUtils.isEmpty(data))
            kendraAcknowledgementDto = new KendraAcknowledgementDto();
        else {
            try {
                Gson gson = new GsonBuilder().create();
                kendraAcknowledgementDto = gson.fromJson(data, KendraAcknowledgementDto.class);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        txtMaterialName.setText(materialName);
        this.IsServiceCall = IsServiceCall;
        this.statusCode = statusCode;
        this.materialCode = materialCode;
        this.assetsTracking = assetsTracking;
        kendraAcknowledgementDto.setStatusCode(statusCode);
        kendraAcknowledgementDto.setId(euipAckId);
        kendraAcknowledgementDto.setNextgen_vakrangee_kendra_equipments_id(kendraEuipId);
        kendraAcknowledgementDto.setNextgen_standard_equipment_id(standEquiId);

        getAllAcknowledgementData = new GetAllAcknowledgementData();
        getAllAcknowledgementData.execute("");
    }

    private void bindAllData() {

        if (kendraAcknowledgementDto == null)
            return;

        //STEP 1: Brand Name
        CustomFranchiseeApplicationSpinnerAdapter brandNameAdapter = new CustomFranchiseeApplicationSpinnerAdapter(context, brandNameList);
        spinnerBrandName.setAdapter(brandNameAdapter);
        int brandPos = kendraAckRepo.getSelectedPos(brandNameList, kendraAcknowledgementDto.getBrandName());
        spinnerBrandName.setSelection(brandPos);
        spinnerBrandName.setOnItemSelectedListener(this);

        //STEP 2: Goods Condition
        CustomFranchiseeApplicationSpinnerAdapter goodsConditionAdapter = new CustomFranchiseeApplicationSpinnerAdapter(context, goodsConditionList);
        spinnerGoodsCondition.setAdapter(goodsConditionAdapter);
        int goodsPos = kendraAckRepo.getSelectedPos(goodsConditionList, kendraAcknowledgementDto.getGoodsCondition());
        spinnerGoodsCondition.setSelection(goodsPos);
        spinnerGoodsCondition.setOnItemSelectedListener(this);

        //STEP 3: Serial No
        editTextSerialNo.setText(kendraAcknowledgementDto.getSerialNo());
        if (TextUtils.isEmpty(assetsTracking) || assetsTracking.equalsIgnoreCase("N")) {
            layoutSerialNo.setVisibility(View.GONE);
        } else {
            layoutSerialNo.setVisibility(View.VISIBLE);
        }

        //STEP 4: Received Date
        if (!TextUtils.isEmpty(kendraAcknowledgementDto.getReceivedDate())) {
            String rDate = CommonUtils.getFormattedDate(YYYY_MM_DD_HH_MM_SS_CONST, "dd-MM-yyyy hh:mm a", kendraAcknowledgementDto.getReceivedDate());
            rDate = TextUtils.isEmpty(rDate) ? null : rDate.replace("am", "AM").replace("pm", "PM");
            textViewReceivedDate.setText(rDate);
        }

        //STEP 5: Equipment Image Pic
       /* boolean IsEquipPDF = ((kendraAcknowledgementDto.getEquipmentPicExt() != null) && kendraAcknowledgementDto.getEquipmentPicExt().equalsIgnoreCase("pdf")) ? true : false;
        if (IsEquipPDF) {
            Glide.with(context).asDrawable().load(R.drawable.pdf).into(imgEquipmentImage);
        } else {
            if (!TextUtils.isEmpty(kendraAcknowledgementDto.getEquipmentPicFileId())) {
                String picUrl = Constants.DownloadImageUrl + kendraAcknowledgementDto.getEquipmentPicFileId();
                Glide.with(context)
                        .load(picUrl)
                        .apply(new RequestOptions()
                                .error(R.drawable.ic_camera_alt_black_72dp)
                                .placeholder(R.drawable.ic_camera_alt_black_72dp)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))

                        .into(imgEquipmentImage);
            }
        }*/

        reloadImageFragment();

        //STEP 6: Upload Equipment Packaging Label
        boolean IsUploadEquipPDF = ((kendraAcknowledgementDto.getEquipmentPackagingExt() != null) && kendraAcknowledgementDto.getEquipmentPackagingExt().equalsIgnoreCase("pdf")) ? true : false;
        if (IsUploadEquipPDF) {
            Glide.with(context).asDrawable().load(R.drawable.pdf).into(imgUploadEquipmentPackagingImage);
        } else {
            if (!TextUtils.isEmpty(kendraAcknowledgementDto.getEquipmentPackagingFileId())) {
                String picUrl = Constants.DownloadImageUrl + kendraAcknowledgementDto.getEquipmentPackagingFileId();
                Glide.with(context)
                        .load(picUrl)
                        .apply(new RequestOptions()
                                .error(R.drawable.ic_camera_alt_black_72dp)
                                .placeholder(R.drawable.ic_camera_alt_black_72dp)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))

                        .into(imgUploadEquipmentPackagingImage);
            }
        }

        //STEP 7: Remarks
        editTextRemarks.setText(kendraAcknowledgementDto.getRemarks());
    }

    public void disableEditingSerialNo() {
        if (IsServiceCall) {
            imgBarcodeScan.setEnabled(false);
            editTextSerialNo.setEnabled(false);
        } else {
            imgBarcodeScan.setEnabled(true);
            editTextSerialNo.setEnabled(true);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int Id = parent.getId();

        if (Id == R.id.spinnerBrandName) {
            if (position > 0) {
                spinnerBrandName.requestFocus();
                CustomFranchiseeApplicationSpinnerDto dto1 = (CustomFranchiseeApplicationSpinnerDto) spinnerBrandName.getItemAtPosition(position);
                if (!dto1.getId().equals("0")) {
                    kendraAcknowledgementDto.setBrandName(dto1.getId());
                    //Get Series
                    getAllKendraAckSpinnerData = new GetAllKendraAckSpinnerData(kendraAcknowledgementDto.getBrandName());
                    getAllKendraAckSpinnerData.execute(SERIES_NAME);

                }
            } else {
                spinnerSeries.setEnabled(false);
                spinnerModel.setEnabled(false);
                spinnerSeries.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, new ArrayList<CustomFranchiseeApplicationSpinnerDto>()));
                kendraAcknowledgementDto.setSeries(null);
                spinnerModel.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, new ArrayList<CustomFranchiseeApplicationSpinnerDto>()));
                kendraAcknowledgementDto.setModel(null);
            }

        } else if (Id == R.id.spinnerSeries) {
            if (position > 0) {
                spinnerSeries.requestFocus();
                CustomFranchiseeApplicationSpinnerDto dto2 = (CustomFranchiseeApplicationSpinnerDto) spinnerSeries.getItemAtPosition(position);
                if (!dto2.getId().equals("0")) {
                    kendraAcknowledgementDto.setSeries(dto2.getId());
                    //Get Model
                    getAllKendraAckSpinnerData = new GetAllKendraAckSpinnerData(kendraAcknowledgementDto.getSeries());
                    getAllKendraAckSpinnerData.execute(MODEL_NAME);

                }
            } else {
                spinnerModel.setEnabled(false);
                spinnerModel.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, new ArrayList<CustomFranchiseeApplicationSpinnerDto>()));
                kendraAcknowledgementDto.setSeries(null);
            }


        } else if (Id == R.id.spinnerModel) {
            spinnerModel.requestFocus();
            CustomFranchiseeApplicationSpinnerDto dto3 = (CustomFranchiseeApplicationSpinnerDto) spinnerModel.getItemAtPosition(position);
            kendraAcknowledgementDto.setModel(dto3.getId());

        } else if (Id == R.id.spinnerGoodsCondition) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto dto4 = (CustomFranchiseeApplicationSpinnerDto) spinnerGoodsCondition.getItemAtPosition(position);
                kendraAcknowledgementDto.setGoodsCondition(dto4.getId());
                // To show note in case of "Part Item Missing" selected. Date: 23-10-2018 | By: Dpk
                if (dto4.getName().equalsIgnoreCase("Part Item Missing")) {
                    txtGoodsConditionNote.setVisibility(View.VISIBLE);
                } else {
                    txtGoodsConditionNote.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @Override
    public void onClick(View view) {
        int Id = view.getId();

        if (Id == R.id.imgEquipmentImage) {
            SEL_FILE_TYPE = IMAGES;
            showAttachmentDialog(view, SEL_FILE_TYPE);

        } else if (Id == R.id.imgUploadEquipmentPackagingImage) {
            SEL_FILE_TYPE = IMAGES;
            showAttachmentDialog(view, SEL_FILE_TYPE);

        } else if (Id == R.id.textViewReceivedDate) {
            selectedDateTimeId = Id;
            showDateTimeDialogPicker();

        } else if (Id == R.id.imgBarcodeScan) {
            Intent i = new Intent(context, ZBarcodeReader.class);
            startActivityForResult(i, 300);

        } else if (Id == R.id.btnCancel) {
            ((NextGenVakrangeeKendraAcknowledgementActivity) getActivity()).backPressed();

        } else if (Id == R.id.btnSubmitAck) {
            saveKendraAckDetails();

        } else if (Id == R.id.txtUploadEquipmentPackagingImageLbl || Id == R.id.txtUploadEquipmentPackagingImageIcn) {
            displayEquipmentImage();
        }
    }

    //region display Equipment image show base on server image
    private void displayEquipmentImage() {
        String imgURL = kendraAckRepo.getEquipmentPackingPreviewImage();
        if (!TextUtils.isEmpty(imgURL)) {
            imagePreview_zoom_dialog = new ImagePreview_zoom_Dialog(context, imgURL, "Sample Equipment Package Image") {
            };
            imagePreview_zoom_dialog.show();
            imagePreview_zoom_dialog.setCancelable(false);
        } else {
            Toast.makeText(context, "No photo available to preview.", Toast.LENGTH_SHORT).show();
        }

    }
    //endregion

    private void showDateTimeDialogPicker() {

        Date defaultDate = null;
        if (selectedDateTimeId == R.id.textViewReceivedDate) {
            try {
                if (!TextUtils.isEmpty(kendraAcknowledgementDto.getReceivedDate()))
                    receivedDate = dateTimeFormatter.parse(kendraAcknowledgementDto.getReceivedDate());
            } catch (Exception e) {
                e.printStackTrace();
            }
            defaultDate = receivedDate;
        }

        // Show DateTime Picker Dialog.
        dateTimePickerDialog = new DateTimePickerDialog(getActivity(), false, defaultDate, new DateTimePickerDialog.IDateTimePicker() {
            @Override
            public void getDateTime(Date datetime, String defaultFormattedDateTime) {
                try {
                    String formatedDate = dateFormatter2.format(datetime);
                    String formateYMD = defaultFormattedDateTime; //dateFormatterYMD.format(datetime);
                    Toast.makeText(getActivity(), "Selected DateTime : " + formatedDate, Toast.LENGTH_LONG).show();

                    if (selectedDateTimeId != 0) {
                        TextView textViewDateTime = (TextView) view.findViewById(selectedDateTimeId);
                        textViewDateTime.setText(formateYMD);

                        if (selectedDateTimeId == R.id.textViewReceivedDate) {
                            receivedDate = datetime;
                            strReceivedDate = formateYMD;
                            kendraAcknowledgementDto.setReceivedDate(strReceivedDate);

                            String rDate = CommonUtils.getFormattedDate("yyyy-MM-dd HH:mm:ss", "dd-MM-yyyy hh:mm a", kendraAcknowledgementDto.getReceivedDate());
                            rDate = TextUtils.isEmpty(rDate) ? null : rDate.replace("am", "AM").replace("pm", "PM");
                            textViewReceivedDate.setText(rDate);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //Set Min and Max Date
        long now = new Date().getTime() - 1000;
        int after4days = (1000 * 60 * 60 * 24 * 4);         //no Of Days
        Calendar c = Calendar.getInstance();
        c.set(1920, 0, 1);//Year,Month -1,Day
        if (selectedDateTimeId == R.id.textViewReceivedDate) {
            dateTimePickerDialog.setMinDate(c.getTimeInMillis());
            dateTimePickerDialog.setMaxDate(now);

        }

        dateTimePickerDialog.setCancelable(false);
        dateTimePickerDialog.setActionButtonName("Save");
        dateTimePickerDialog.show();

    }

    class GetAllKendraAckSpinnerData extends AsyncTask<String, Void, String> {
        private String type;
        private String brandId_productId;

        public GetAllKendraAckSpinnerData(String brandId_productId) {
            this.brandId_productId = brandId_productId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressSpinner(context);
        }

        @Override
        protected String doInBackground(String... strings) {
            type = strings[0];
            switch (type.toUpperCase()) {

                case SERIES_NAME:
                    seriesList = kendraAckRepo.getSeriesList(brandId_productId);
                    break;
                case MODEL_NAME:
                    modelList = kendraAckRepo.getModelList(brandId_productId);
                    break;

                default:
                    break;
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissProgressSpinner();

            switch (type.toUpperCase()) {

                case SERIES_NAME:
                    setSpinnerAdapter(seriesList, spinnerSeries, kendraAcknowledgementDto.getSeries());
                    break;
                case MODEL_NAME:
                    setSpinnerAdapter(modelList, spinnerModel, kendraAcknowledgementDto.getModel());
                    break;

                default:
                    break;
            }
        }
    }

    class GetAllAcknowledgementData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressSpinner(context);
        }

        @Override
        protected String doInBackground(String... strings) {

            //existing Kendra Acknowledgement details, if status code is RECEIVED
            if (IsServiceCall) {
                kendraAcknowledgementDto = kendraAckRepo.getExistingKendraAckDetail(kendraAcknowledgementDto.getId());

                if (kendraAcknowledgementDto != null)
                    kendraAcknowledgementDto.setStatusCode(statusCode);
            }

            //STEP 1: Get all brands name
            brandNameList = kendraAckRepo.getBrandNameList(materialCode);

            //STEP 2: Get Goods Condition
            goodsConditionList = kendraAckRepo.getGoodsConditionList();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissProgressSpinner();
            bindAllData();
        }
    }

    //region Set Series OR Model Adapter
    private void setSpinnerAdapter(List<CustomFranchiseeApplicationSpinnerDto> seriesORModelList, Spinner spinner, String selId) {

        spinner.setEnabled(true);
        spinner.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, seriesORModelList));
        int Pos = kendraAckRepo.getSelectedPos(seriesORModelList, selId);
        spinner.setSelection(Pos);
        spinner.setOnItemSelectedListener(this);
    }
    //endregion

    public int IsKendraAckDetailsValidated() {

        //STEP 1:Brand Name
        if (TextUtils.isEmpty(kendraAcknowledgementDto.getBrandName())) {
            ackScrollview.scrollTo(0, 0);
            GUIUtils.setErrorToSpinner(spinnerBrandName, "Please select Brand Name", context);
            return 1;
        }

       /* //STEP 2: Series
        if (TextUtils.isEmpty(kendraAcknowledgementDto.getSeries())) {
            ackScrollview.scrollTo(0, 0);
            GUIUtils.setErrorToSpinner(spinnerSeries, "Please select Series", context);
            return 2;
        }

        //STEP 3: Model
        if (TextUtils.isEmpty(kendraAcknowledgementDto.getModel())) {
            ackScrollview.scrollTo(0, 0);
            GUIUtils.setErrorToSpinner(spinnerModel, "Please select Model", context);
            return 3;
        }*/

        //STEP 4: Goods Condition
        if (TextUtils.isEmpty(kendraAcknowledgementDto.getGoodsCondition())) {
            ackScrollview.scrollTo(0, 0);
            GUIUtils.setErrorToSpinner(spinnerGoodsCondition, "Please select Goods Condition", context);
            return 4;
        }

        //STEP 5: Received Date
        if (TextUtils.isEmpty(kendraAcknowledgementDto.getReceivedDate())) {
            textViewReceivedDate.setError("Please select Received Date.");
            return 5;
        }

        //STEP 6: Equipment Images
        if (imagesList.size() == 0) {
            showMessage("Please add Equipment Images.");
            return 6;
        }

        /*if (TextUtils.isEmpty(kendraAcknowledgementDto.getEquipmentPicFileId()) || kendraAcknowledgementDto.getEquipmentPicFileId().equalsIgnoreCase("0")) {
            if (TextUtils.isEmpty(kendraAcknowledgementDto.getEquipmentPicBase64())) {
                showMessage("Please choose Equipment Image.");
                return 6;
            }
        }*/

        /*//STEP 7: Upload Equipment Packaging Label
        if (TextUtils.isEmpty(kendraAcknowledgementDto.getEquipmentPackagingFileId()) || kendraAcknowledgementDto.getEquipmentPackagingFileId().equalsIgnoreCase("0")) {
            if (TextUtils.isEmpty(kendraAcknowledgementDto.getEquipmentPackagingBase64())) {
                showMessage("Please choose Equipment Packaging Label.");
                return 7;
            }
        }*/

        //STEP 8: Remarks
        if (TextUtils.isEmpty(editTextRemarks.getText().toString())) {
            editTextRemarks.setError("Please enter Remarks. ");
            return 8;
        }

        //STEP 8: Remarks Length should be greater than 3
        int remarksLen = editTextRemarks.getText().toString().length();
        if (remarksLen < 3) {
            editTextRemarks.setError("The Remarks must be minimum 3 characters long.");
            return 8;
        }
        return 0;
    }

    public void saveKendraAckDetails() {

        //Internet Connectivity check
        if (!InternetConnection.isNetworkAvailable(context)) {
            showMessage("No Internet Connection.");
            return;
        }

        //Validate Mandatory fields
        int status = IsKendraAckDetailsValidated();
        if (status != 0)
            return;

        //Post data
        kendraAcknowledgementDto.setSerialNo(editTextSerialNo.getText().toString());
        kendraAcknowledgementDto.setRemarks(editTextRemarks.getText().toString());

        asyncSaveKendraAckDetails = new AsyncSaveKendraAckDetails(context, kendraAcknowledgementDto, imagesList, new AsyncSaveKendraAckDetails.Callback() {
            @Override
            public void onResult(String result) {
                try {
                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                        return;
                    }

                    //Handle Response
                    if (result.startsWith("FAILED")) {
                        showMessage("Serial No. already exists.");

                    } else if (result.startsWith("OKAY")) {
                        showMessageWithFinish("Kendra Acknowledgement done successfully.");

                    } else {
                        StringTokenizer st1 = new StringTokenizer(result, "|");
                        st1.nextToken();
                        String sco1 = st1.nextToken();
                        if (!TextUtils.isEmpty(sco1)) {
                            showMessage(sco1);
                            return;
                        }

                        showMessage("Kendra Acknowledgement saving failed.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                }
            }
        });
        asyncSaveKendraAckDetails.execute("");
    }

    public void reloadImageFragment() {
        getAlreadyExistEquipmentImagesList();

        Bundle args = new Bundle();
        args.putSerializable(Constants.INTENT_KEY_IMAGE_GALLERY_LIST, (Serializable) imagesList);
        args.putString(Constants.INTENT_KEY_TYPE, Constants.RECYCLER_TYPE_HORIZONTAL);
        args.putString(Constants.INTENT_KEY_TITLE, "Equipment Images");
        args.putBoolean(Constants.INTENT_KEY_PORTRAIT_ALLOWED, true);
        args.putInt(Constants.INTENT_VALUE_MAX_IMAGES_COUNT, 3);
        fragmentEquipImages.refresh(args, new IImagesHandler() {
            @Override
            public void updateImagesList(List<ImageDto> imageDtoList) {
                imagesList = imageDtoList;
            }
        });
    }

    /**
     * Prepare already existing Equipment Images list
     *
     * @return
     */
    private List<ImageDto> getAlreadyExistEquipmentImagesList() {

        try {
            if (TextUtils.isEmpty(kendraAcknowledgementDto.getEquipmentPicFileId()))
                return imagesList;

            String[] imagesId = kendraAcknowledgementDto.getEquipmentPicFileId().split("\\|");
            for (int i = 0; i < imagesId.length; i++) {

                ImageDto imageDto = new ImageDto();

                imageDto.setNextgen_equip_images_id(imagesId[i]);
                imageDto.setName("Photo " + (i + 1));
                imagesList.add(imageDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.writeError(TAG, "getAlreadyExistImagesList: Error: " + e.toString());
        }
        return imagesList;
    }

}
