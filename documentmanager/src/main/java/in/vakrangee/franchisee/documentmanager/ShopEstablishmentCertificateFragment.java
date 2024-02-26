package in.vakrangee.franchisee.documentmanager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nononsenseapps.filepicker.Utils;
import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;

import java.io.File;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.commongui.DateTimePickerDialog;
import in.vakrangee.supercore.franchisee.commongui.FileAttachmentDialog;
import in.vakrangee.supercore.franchisee.commongui.FileUtils;
import in.vakrangee.supercore.franchisee.commongui.FilteredFilePickerActivity;
import in.vakrangee.supercore.franchisee.commongui.imagepreview.CustomImageZoomDialogDM;
import in.vakrangee.supercore.franchisee.gstdetails.GSTINDTO;
import in.vakrangee.supercore.franchisee.nextgenfranchiseeapplication.CustomFranchiseeApplicationSpinnerDto;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.GUIUtils;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;
import in.vakrangee.supercore.franchisee.utils.ImageZipper;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;
import in.vakrangee.supercore.franchisee.utils.CustomSearchableSpinner;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShopEstablishmentCertificateFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "ShopEstablishmentCertificateFragment";

    private View view;
    private Context context;
    private DeprecateHandler deprecateHandler;
    private PermissionHandler permissionHandler;

    private ShopEstablishmentCertificateDto shopEstCertificateDto;

    // View Objects
    private EditText editTextNameOnShopEstCertificate, editTextShopEstCertificateNumber, editTextShopEstIssuingAuthority;
    private TextView txtNameOnShopEstCertificateLbl, txtShopEstCertificateNumberLbl, txtShopEstIssuingAuthorityLbl, txtIssuingDateLbl, txtValidFromDateLbl, txtExpiringDateLbl, txtShopEstCopyLbl, txtStateLbl, txtDistrictLbl, txtVTCLbl;
    private TextView textViewIssuingDateOfShopEst;
    private TextView textViewValidFromDateOfShopEst;
    private TextView textViewExpiryDateOfShopEst;
    private ImageView imgShopEstCerificationImg;

    public CustomSearchableSpinner spinnerState;
    public CustomSearchableSpinner spinnerDistrict;
    public CustomSearchableSpinner spinnerVTC;

    private LinearLayout layoutShopEstCertificationDetail;
    //Date
    private DateTimePickerDialog dateTimePickerDialog;
    private DateFormat dateFormatter2 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    private DateFormat dateFormatterYMD = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private Date shopEstIssuingDate;
    private String strShopEstIssuingDate;
    private Date shopEstValidFromDate;
    private String strShopEstValidFromDate;
    private Date shopEstExpiryDate;
    private String strShopEstExpiryDate;
    private int selectedDateTimeId = 0;

    //img
    private static final int CAMERA_PIC_REQUEST = 100;
    private static final int BROWSE_FOLDER_REQUEST = 101;
    private static final int SCANLIB_REQUEST_CODE = 99;
    private Uri picUri;                 //Picture URI
    private String SEL_FILE_TYPE;
    private int FROM = -1;
    private static final int SHOP_EST_CERTIFICATION_IMAGE_PHOTO = 1;
    private FileAttachmentDialog fileAttachementDialog;
    private CustomImageZoomDialogDM customImagePreviewDialog;

    private List<CustomFranchiseeApplicationSpinnerDto> stateList, districtList, vtcList;
    private DocumentManagerRepository documentManagerRepo;
    private GetAllSpinnerData getAllSpinnerData = null;

    public ShopEstablishmentCertificateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_shop_establishment_certificate, container, false);

        //Initialize data
        this.context = getContext();
        deprecateHandler = new DeprecateHandler(context);
        permissionHandler = new PermissionHandler(getActivity());
        documentManagerRepo = new DocumentManagerRepository(context);

        // Bind UI
        editTextNameOnShopEstCertificate = view.findViewById(R.id.editTextNameOnShopEstCertificate);
        txtNameOnShopEstCertificateLbl = view.findViewById(R.id.txtNameOnShopEstCertificateLbl);
        txtIssuingDateLbl = view.findViewById(R.id.txtIssuingDateLbl);
        txtValidFromDateLbl = view.findViewById(R.id.txtValidFromDateLbl);
        txtExpiringDateLbl = view.findViewById(R.id.txtExpiringDateLbl);
        txtShopEstCopyLbl = view.findViewById(R.id.txtShopEstCopyLbl);
        textViewIssuingDateOfShopEst = view.findViewById(R.id.textViewIssuingDateOfShopEst);
        textViewValidFromDateOfShopEst = view.findViewById(R.id.textViewValidFromDateOfShopEst);
        textViewExpiryDateOfShopEst = view.findViewById(R.id.textViewExpiryDateOfShopEst);
        layoutShopEstCertificationDetail = view.findViewById(R.id.layoutShopEstCertificationDetail);
        imgShopEstCerificationImg = view.findViewById(R.id.imgShopEstCerificationImg);

        txtStateLbl = view.findViewById(R.id.txtStateLbl);
        txtDistrictLbl = view.findViewById(R.id.txtDistrictLbl);
        txtVTCLbl = view.findViewById(R.id.txtVTCLbl);
        spinnerState = view.findViewById(R.id.spinnerState);
        spinnerDistrict = view.findViewById(R.id.spinnerDistrict);
        spinnerVTC = view.findViewById(R.id.spinnerVTC);
        editTextShopEstCertificateNumber = view.findViewById(R.id.editTextShopEstCertificateNumber);
        editTextShopEstIssuingAuthority = view.findViewById(R.id.editTextShopEstIssuingAuthority);
        txtShopEstCertificateNumberLbl = view.findViewById(R.id.txtShopEstCertificateNumberLbl);
        txtShopEstIssuingAuthorityLbl = view.findViewById(R.id.txtShopEstIssuingAuthorityLbl);

        //
        imgShopEstCerificationImg.setOnClickListener(this);
        textViewIssuingDateOfShopEst.setOnClickListener(this);
        textViewValidFromDateOfShopEst.setOnClickListener(this);
        textViewExpiryDateOfShopEst.setOnClickListener(this);

        CommonUtils.InputFiletrWithMaxLength(editTextNameOnShopEstCertificate, "~#^|$%&*!", 50);
        CommonUtils.InputFiletrWithMaxLength(editTextShopEstIssuingAuthority, "~#^|$%&*!", 50);

        setCompulsoryMarkLabel();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        permissionHandler.dismissSnackbar();
    }

    //region realod data
    public void reload(String data) {
        //Reload Data
        if (TextUtils.isEmpty(data))
            shopEstCertificateDto = new ShopEstablishmentCertificateDto();
        else {
            try {
                Gson gson = new GsonBuilder().create();
                List<ShopEstablishmentCertificateDto> applicationList = gson.fromJson(data, new TypeToken<ArrayList<ShopEstablishmentCertificateDto>>() {
                }.getType());
                if (applicationList.size() > 0) {
                    shopEstCertificateDto = applicationList.get(0);
                } else {
                    shopEstCertificateDto = new ShopEstablishmentCertificateDto();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        getAllSpinnerData = new GetAllSpinnerData(null, null, null);
        getAllSpinnerData.execute("STATE");

        //Bind Data
        //bindData();
    }
    //endregion

    //region Set State Adapter
    private void setStateSpinnerAdapter(List<CustomFranchiseeApplicationSpinnerDto> stateList, Spinner spinnerState, String selStateId) {

        //spinnerState.setEnabled(true);
        spinnerState.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, stateList));
        int appsstatePos = documentManagerRepo.getSelectedPos(stateList, selStateId);
        spinnerState.setSelection(appsstatePos);
        spinnerState.setOnItemSelectedListener(this);

    }
    //endregion

    //region Set District Adapter
    private void setDistrictSpinnerAdapter(List<CustomFranchiseeApplicationSpinnerDto> districtList, Spinner spinnerDistrict, String selDistrictId) {

        //spinnerDistrict.setEnabled(true);
        spinnerDistrict.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, districtList));
        int appsstatePos = documentManagerRepo.getSelectedPos(districtList, selDistrictId);
        spinnerDistrict.setSelection(appsstatePos);
        spinnerDistrict.setOnItemSelectedListener(this);

    }
    //endregion

    //region Set VTC Adapter
    private void setVTCSpinnerAdapter(List<CustomFranchiseeApplicationSpinnerDto> VTCList, Spinner spinnerVTC, String selVTCId) {

        //spinnerVTC.setEnabled(true);
        spinnerVTC.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, VTCList));
        int appsstatePos = documentManagerRepo.getSelectedPos(VTCList, selVTCId);
        spinnerVTC.setSelection(appsstatePos);
        spinnerVTC.setOnItemSelectedListener(this);
    }
    //endregion

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int Id = parent.getId();


        if (Id == R.id.spinnerState) {
            if (position > 0) {
                spinnerState.setTitle("Select State");
                spinnerState.requestFocus();
                CustomFranchiseeApplicationSpinnerDto stateDto = (CustomFranchiseeApplicationSpinnerDto) spinnerState.getItemAtPosition(position);
                if (!stateDto.getId().equals("0")) {
                    shopEstCertificateDto.setState(stateDto.getId());

                    //Get Division-District
                    getAllSpinnerData = new GetAllSpinnerData(shopEstCertificateDto.getState(), null, null);
                    getAllSpinnerData.execute("DISTRICT");
                }
            } else {
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));
                shopEstCertificateDto.setState("0");

                //DISTRICT
                setDistrictSpinnerAdapter(list1, spinnerDistrict, null);
                shopEstCertificateDto.setDistrict(null);

                //VTC
                setVTCSpinnerAdapter(list1, spinnerVTC, null);
                shopEstCertificateDto.setVtc(null);

            }
        } else if (Id == R.id.spinnerDistrict) {
            if (position > 0) {
                spinnerDistrict.setTitle("Select District");
                spinnerDistrict.requestFocus();
                CustomFranchiseeApplicationSpinnerDto disDto = (CustomFranchiseeApplicationSpinnerDto) spinnerDistrict.getItemAtPosition(position);
                if (!disDto.getId().equals("0")) {
                    shopEstCertificateDto.setDistrict(disDto.getId());

                    //Get BLOCK
                    getAllSpinnerData = new GetAllSpinnerData(shopEstCertificateDto.getState(), shopEstCertificateDto.getDistrict(), null);
                    getAllSpinnerData.execute("VTC");

                }
            } else {
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));
                shopEstCertificateDto.setDistrict("0");

                //VTC
                setVTCSpinnerAdapter(list1, spinnerVTC, null);
                shopEstCertificateDto.setVtc(null);

            }
        } else if (Id == R.id.spinnerVTC) {
            if (position > 0) {
                spinnerVTC.setTitle("Select VTC");
                spinnerVTC.requestFocus();
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerVTC.getItemAtPosition(position);
                shopEstCertificateDto.setVtc(entityDto.getId());

            } else {
                shopEstCertificateDto.setVtc("0");
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    //region bind data
    private void bindData() {
        //STEP 1:Name on Certificate
        editTextNameOnShopEstCertificate.setText(shopEstCertificateDto.getNameOnShopEstCertificate());

        //Shop Est Certifcate Number
        editTextShopEstCertificateNumber.setText(shopEstCertificateDto.getShop_est_certificate_no());

        //Shop Est Certifcate Issuing Authority
        editTextShopEstIssuingAuthority.setText(shopEstCertificateDto.getShop_est_issuing_authority());

        //State
        setStateSpinnerAdapter(stateList, spinnerState, shopEstCertificateDto.getState());

        //STEP 2: Certificate Issuing Date
        if (!TextUtils.isEmpty(shopEstCertificateDto.getShopEstIssuingDate())) {
            String formattedDate = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", shopEstCertificateDto.getShopEstIssuingDate());
            textViewIssuingDateOfShopEst.setText(formattedDate);
        }

        //STEP : Certificate Valid From Date
        if (!TextUtils.isEmpty(shopEstCertificateDto.getShopEstValidFromDate())) {
            String formattedDate = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", shopEstCertificateDto.getShopEstValidFromDate());
            textViewValidFromDateOfShopEst.setText(formattedDate);
        }

        //STEP 3: Certificate Expiry Date
        if (!TextUtils.isEmpty(shopEstCertificateDto.getShopEstExpiryDate())) {
            String formattedDate = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", shopEstCertificateDto.getShopEstExpiryDate());
            textViewExpiryDateOfShopEst.setText(formattedDate);
        }

        //STEP 5: IIBF Certificate Scanned Copy
        boolean IsPanPDF = (shopEstCertificateDto.getShopEstCertificateScanExt() != null && shopEstCertificateDto.getShopEstCertificateScanExt().equalsIgnoreCase("pdf")) ? true : false;
        if (IsPanPDF) {
            Glide.with(context)
                    .load(R.drawable.pdf)
                    .apply(new RequestOptions()
                            .error(R.drawable.pdf)
                            .placeholder(R.drawable.pdf)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgShopEstCerificationImg);

        } else {
            if (!TextUtils.isEmpty(shopEstCertificateDto.getShopEstCertificateScanId())) {
                String gstUrl = Constants.DownloadImageUrl + shopEstCertificateDto.getShopEstCertificateScanId();
                Glide.with(context)
                        .load(gstUrl)
                        .apply(new RequestOptions()
                                .error(R.drawable.ic_camera_alt_black_72dp)
                                .placeholder(R.drawable.ic_camera_alt_black_72dp)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))
                        .into(imgShopEstCerificationImg);
            }
        }

        //Enable/disable views
        boolean IsEditable = (!TextUtils.isEmpty(shopEstCertificateDto.getIsEditable()) && shopEstCertificateDto.getIsEditable().equalsIgnoreCase("1")) ? true : false;
        GUIUtils.setViewAndChildrenEnabled(layoutShopEstCertificationDetail, IsEditable);
        ((DocumentManagerActivity) getActivity()).IsFooterLayoutVisible(IsEditable);

    }
    //endregion

    //region compulsaroy red mark
    public void setCompulsoryMarkLabel() {
        TextView[] txtViewsForCompulsoryMark = {txtNameOnShopEstCertificateLbl, txtShopEstCertificateNumberLbl, txtShopEstIssuingAuthorityLbl,
                txtStateLbl, txtDistrictLbl, txtVTCLbl, txtIssuingDateLbl,  txtShopEstCopyLbl}; //txtValidFromDateLbl, txtExpiringDateLbl,
        GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);
    }
    //endregion

    //region onclick image and datepicker
    @Override
    public void onClick(View v) {
        int Id = v.getId();
        if (Id == R.id.imgShopEstCerificationImg) {
            showImgOrPDF(v);
        } else if (Id == R.id.textViewIssuingDateOfShopEst) {
            selectedDateTimeId = v.getId();
            showDateTimeDialogPicker(v);
        } else if (Id == R.id.textViewValidFromDateOfShopEst) {
            if (TextUtils.isEmpty(shopEstCertificateDto.getShopEstIssuingDate())) {
                textViewIssuingDateOfShopEst.setError("Please select Certificate Issuing Date first.");
                showMessage("Please select Certificate Issuing Date first.");
                return;
            }
            selectedDateTimeId = v.getId();
            showDateTimeDialogPicker(v);
        } else if (Id == R.id.textViewExpiryDateOfShopEst) {

            if (TextUtils.isEmpty(shopEstCertificateDto.getShopEstValidFromDate())) {
                textViewValidFromDateOfShopEst.setError("Please select Certificate Valid From Date first.");
                showMessage("Please select Certificate Valid From Date first.");
                return;
            }

            selectedDateTimeId = v.getId();
            showDateTimeDialogPicker(v);
        }
    }
    //endregion

    //region On Activity Result

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
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
                        Glide.with(context).asDrawable().load(context.getResources().getDrawable(R.drawable.pdf)).into(imgShopEstCerificationImg);
                        shopEstCertificateDto.setShopEstCertificateScanBase64(base64Data);
                        shopEstCertificateDto.setShopEstCertificateScanExt(ext);
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
    //endregion

    private void setImageData(Uri imageUri, String imageBase64, Bitmap bitmapNew) {

        GSTINDTO imageDto = new GSTINDTO();
        imageDto.setUri(imageUri);
        imageDto.setBitmap(bitmapNew);
        imageDto.setChangedPhoto(true);
        imageDto.setGstImage(imageBase64);

        showImagePreviewDialog((Object) imageDto);
    }

    private void setScanCopyData(boolean IsDrawable, Bitmap bitmap, String base64, String ext) {

        switch (FROM) {

            case SHOP_EST_CERTIFICATION_IMAGE_PHOTO:
                if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgShopEstCerificationImg);
                else {
                    try {
                        bitmap = ImageUtils.rotateImageIfRequired(getActivity().getContentResolver(), bitmap, picUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Glide.with(context).asBitmap().load(bitmap).into(imgShopEstCerificationImg);
                }

                shopEstCertificateDto.setShopEstCertificateScanBase64(base64);
                shopEstCertificateDto.setShopEstCertificateScanExt(ext);
                break;

            default:
                break;

        }
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
                            FROM = SHOP_EST_CERTIFICATION_IMAGE_PHOTO;

                            String ext = TextUtils.isEmpty(shopEstCertificateDto.getShopEstCertificateScanExt()) ? "jpg" : shopEstCertificateDto.getShopEstCertificateScanExt();

                            //FOR PDF
                            if (ext.equalsIgnoreCase("pdf")) {
                                SEL_FILE_TYPE = " images";
                                startScanCamera();
                                return;
                            }

                            //FOR Image
                            if ((TextUtils.isEmpty(shopEstCertificateDto.getShopEstCertificateScanId()) ||
                                    shopEstCertificateDto.getShopEstCertificateScanId().equalsIgnoreCase("0"))
                                    && TextUtils.isEmpty(shopEstCertificateDto.getShopEstCertificateScanBase64())) {
                                SEL_FILE_TYPE = " images";
                                startScanCamera();

                            } else {
                                GSTINDTO previewDto = prepareDtoForPreview(FROM);
                                showImagePreviewDialog(previewDto);
                            }
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

    public int IsShopEstDetailsValidated() {

        //STEP 1.1: Name on Certificate
        shopEstCertificateDto.setNameOnShopEstCertificate(editTextNameOnShopEstCertificate.getText().toString().trim());
        if (TextUtils.isEmpty(shopEstCertificateDto.getNameOnShopEstCertificate())) {
            Toast.makeText(context, "Please enter Name as mentioned on Shop Establishment Certificate.", Toast.LENGTH_LONG).show();
            editTextNameOnShopEstCertificate.setError("Please enter Name as mentioned on Shop Establishment Certificate.");
            return 1;
        }

        //Certificate Number
        shopEstCertificateDto.setShop_est_certificate_no(editTextShopEstCertificateNumber.getText().toString().trim());
        if (TextUtils.isEmpty(shopEstCertificateDto.getShop_est_certificate_no())) {
            Toast.makeText(context, "Please enter ShopEst Certificate Number.", Toast.LENGTH_LONG).show();
            editTextShopEstCertificateNumber.setError("Please enter ShopEst Certificate Number.");
            return 1;
        }

        //ShopEst Issuing Authority
        shopEstCertificateDto.setShop_est_issuing_authority(editTextShopEstIssuingAuthority.getText().toString().trim());
        if (TextUtils.isEmpty(shopEstCertificateDto.getShop_est_issuing_authority())) {
            Toast.makeText(context, "Please enter ShopEst Issuing Authority.", Toast.LENGTH_LONG).show();
            editTextShopEstIssuingAuthority.setError("Please enter ShopEst Issuing Authority.");
            return 1;
        }

        //State
        if (TextUtils.isEmpty(shopEstCertificateDto.getState()) || shopEstCertificateDto.getState().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select State.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerState, "Please select State.", context);
            return 1;
        }

        // District
        if (TextUtils.isEmpty(shopEstCertificateDto.getDistrict()) || shopEstCertificateDto.getDistrict().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select District.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerDistrict, "Please select District.", context);
            return 1;
        }

        // VTC
        if (TextUtils.isEmpty(shopEstCertificateDto.getVtc()) || shopEstCertificateDto.getVtc().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Village/Town/City.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerDistrict, "Please select Village/Town/City.", context);
            return 1;
        }

        //STEP 2: Issuing Date
        if (TextUtils.isEmpty(shopEstCertificateDto.getShopEstIssuingDate())) {
            Toast.makeText(context, "Please select Shop Establishment Issuing Date.", Toast.LENGTH_LONG).show();
            textViewIssuingDateOfShopEst.setError("Please select Shop Establishment Issuing Date.");
            return 1;
        }
/*
        // Valid From Date
        if (TextUtils.isEmpty(shopEstCertificateDto.getShopEstValidFromDate())) {
            Toast.makeText(context, "Please select Shop Establishment Valid From Date.", Toast.LENGTH_LONG).show();
            textViewIssuingDateOfShopEst.setError("Please select Shop Establishment Valid From Date.");
            return 1;
        }

        //STEP 3: Expiry Date
        if (TextUtils.isEmpty(shopEstCertificateDto.getShopEstExpiryDate())) {
            Toast.makeText(context, "Please select Shop Establishment Expiry Date.", Toast.LENGTH_LONG).show();
            textViewExpiryDateOfShopEst.setError("Please select Shop Establishment Expiry Date.");
            return 1;
        }*/

        //STEP 3: Shop Establishment Card Scan Copy
        if (TextUtils.isEmpty(shopEstCertificateDto.getShopEstCertificateScanId()) || shopEstCertificateDto.getShopEstCertificateScanId().equalsIgnoreCase("0")) {
            if (TextUtils.isEmpty(shopEstCertificateDto.getShopEstCertificateScanBase64())) {
                showMessage("Please add ShopEst Certificate Scan Copy.");
                return 1;
            }
        }

        return 0;
    }

    private void startScanCamera() {

        //If the app has not the permission then asking for the permission
        permissionHandler.requestPermission(view, Manifest.permission.CAMERA, getString(R.string.needs_permission_camera_msg), new IPermission() {
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

    public ShopEstablishmentCertificateDto getShopEstCertificateDetail() {

        if (shopEstCertificateDto == null)
            return null;

        shopEstCertificateDto.setNameOnShopEstCertificate(editTextNameOnShopEstCertificate.getText().toString());
        shopEstCertificateDto.setShop_est_issuing_authority(editTextShopEstIssuingAuthority.getText().toString());

        return shopEstCertificateDto;
    }

    private void refreshShopEstDetailsImg(GSTINDTO imageDto) {

        shopEstCertificateDto.setChangedPhoto(imageDto.isChangedPhoto());
        shopEstCertificateDto.setShopEstCertificateScanId(imageDto.getGstImageId());
        shopEstCertificateDto.setBitmap(imageDto.getBitmap());
        shopEstCertificateDto.setShopEstCertificateScanExt("jpg");
        shopEstCertificateDto.setShopEstCertificateScanBase64(imageDto.getGstImage());

    }

    private GSTINDTO prepareDtoForPreview(int FROM) {
        GSTINDTO imageDto = new GSTINDTO();

        imageDto.setChangedPhoto(shopEstCertificateDto.isChangedPhoto());
        imageDto.setGstImageId(shopEstCertificateDto.getShopEstCertificateScanId());
        imageDto.setBitmap(shopEstCertificateDto.getBitmap());
        imageDto.setName("");
        imageDto.setGstImage(shopEstCertificateDto.getShopEstCertificateScanBase64());
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

                    refreshShopEstDetailsImg(dto);

                    //Refresh ShopEst Certificate Image
                    if (!TextUtils.isEmpty(shopEstCertificateDto.getShopEstCertificateScanBase64())) {
                        Bitmap bitmap = CommonUtils.StringToBitMap(shopEstCertificateDto.getShopEstCertificateScanBase64());
                        if (bitmap != null)
                            imgShopEstCerificationImg.setImageBitmap(bitmap);
                    }
                }
            });
            customImagePreviewDialog.show();
            customImagePreviewDialog.setCancelable(false);

            //Title
            String title = "Shop Establishment Certificate Image";
            customImagePreviewDialog.setDialogTitle(title);

            //Change Photo Allowed
            boolean IsEditable = (!TextUtils.isEmpty(shopEstCertificateDto.getIsEditable()) &&
                    shopEstCertificateDto.getIsEditable().equalsIgnoreCase("1")) ? true : false;
            customImagePreviewDialog.allowChangePhoto(IsEditable);
            customImagePreviewDialog.allowSaveOption(IsEditable);
            customImagePreviewDialog.allowRemarks(false);

        } else {
            Toast.makeText(context, "No photo available to preview.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showImgOrPDF(final View view) {

        boolean IsGSTImgPDF = (shopEstCertificateDto.getShopEstCertificateScanExt() != null && shopEstCertificateDto.getShopEstCertificateScanExt().equalsIgnoreCase("pdf")) ? true : false;
        boolean IsEditable = (!TextUtils.isEmpty(shopEstCertificateDto.getIsEditable()) && shopEstCertificateDto.getIsEditable().equalsIgnoreCase("1")) ? true : false;

        if (IsEditable) {
            showAttachmentDialog(view);

        } else {

            //PDF - No Preview for PDF

            //Image
            if (!IsGSTImgPDF) {
                //GSTINDTO previewDto = prepareDtoForPreview(FROM);
                //showImagePreviewDialog(previewDto);
                return;
            }
        }
    }

    //region date picker dialog open
    private void showDateTimeDialogPicker(final View view) {

        Date defaultDate = null;
        if (selectedDateTimeId == R.id.textViewIssuingDateOfShopEst) {
            defaultDate = shopEstIssuingDate;
        } else if (selectedDateTimeId == R.id.textViewExpiryDateOfShopEst) {
            defaultDate = shopEstExpiryDate;
        }

        // Show DateTime Picker Dialog.
        dateTimePickerDialog = new DateTimePickerDialog(context, true, defaultDate, new DateTimePickerDialog.IDateTimePicker() {
            @Override
            public void getDateTime(Date datetime, String defaultFormattedDateTime) {
                try {
                    String formatedDate = dateFormatter2.format(datetime);
                    String formateYMD = dateFormatterYMD.format(datetime);
                    //Toast.makeText(context, "Selected DateTime : " + formatedDate, Toast.LENGTH_LONG).show();

                    if (selectedDateTimeId != 0) {
                        TextView textViewDateTime = (TextView) view.findViewById(selectedDateTimeId);
                        textViewDateTime.setText(formateYMD);

                        if (selectedDateTimeId == R.id.textViewIssuingDateOfShopEst) {
                            shopEstIssuingDate = datetime;
                            strShopEstIssuingDate = formateYMD;
                            shopEstCertificateDto.setShopEstIssuingDate(strShopEstIssuingDate);

                            String issuingDate = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", shopEstCertificateDto.getShopEstIssuingDate());
                            textViewIssuingDateOfShopEst.setText(issuingDate);
                            textViewIssuingDateOfShopEst.setError(null);

                        } else if (selectedDateTimeId == R.id.textViewExpiryDateOfShopEst) {
                            shopEstExpiryDate = datetime;
                            strShopEstExpiryDate = formateYMD;
                            shopEstCertificateDto.setShopEstExpiryDate(strShopEstExpiryDate);

                            String DOB = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", shopEstCertificateDto.getShopEstExpiryDate());
                            textViewExpiryDateOfShopEst.setText(DOB);
                            textViewExpiryDateOfShopEst.setError(null);

                        } else if (selectedDateTimeId == R.id.textViewValidFromDateOfShopEst) {
                            shopEstValidFromDate = datetime;
                            strShopEstValidFromDate = formateYMD;
                            shopEstCertificateDto.setShopEstValidFromDate(strShopEstValidFromDate);

                            String DOB = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd-MMM-yyyy", shopEstCertificateDto.getShopEstValidFromDate());
                            textViewValidFromDateOfShopEst.setText(DOB);
                            textViewValidFromDateOfShopEst.setError(null);

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

        //Today's Date
        long now = new Date().getTime() - 1000;

        if (selectedDateTimeId == R.id.textViewIssuingDateOfShopEst) {
            dateTimePickerDialog.setMinDate(end);
            dateTimePickerDialog.setMaxDate(now);

        } else if (selectedDateTimeId == R.id.textViewValidFromDateOfShopEst) {
            Date issueDate = CommonUtils.parseStringToDate(shopEstCertificateDto.getShopEstIssuingDate());
            dateTimePickerDialog.setMinDate(issueDate.getTime());
            dateTimePickerDialog.setMaxDate(now);

        } else if (selectedDateTimeId == R.id.textViewExpiryDateOfShopEst) {
            Date fromDate = CommonUtils.parseStringToDate(shopEstCertificateDto.getShopEstValidFromDate());
            dateTimePickerDialog.setMinDate(fromDate.getTime());

        }

        dateTimePickerDialog.setCancelable(false);
        dateTimePickerDialog.setActionButtonName("Save");
        dateTimePickerDialog.show();

    }
    //endregion

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    class GetAllSpinnerData extends AsyncTask<String, Void, String> {

        private String stateId;
        private String districtId;
        private String vtcId;
        private String type;

        public GetAllSpinnerData(String stateId, String districtId, String vtcId) {
            this.stateId = stateId;
            this.districtId = districtId;
            this.vtcId = vtcId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressSpinner(context);
        }

        @Override
        protected String doInBackground(String... strings) {

            type = strings[0];

            Connection connection = new Connection(context);
            String tmpVkId = connection.getVkid();
            String enquiryId = CommonUtils.getEnquiryId(context);
            String vkIdOrEnquiryId = TextUtils.isEmpty(tmpVkId) ? enquiryId : tmpVkId;

            switch (type.toUpperCase()) {

                case "STATE":
                    stateList = documentManagerRepo.getStateList(vkIdOrEnquiryId);
                    break;

                case "DISTRICT":
                    districtList = documentManagerRepo.getDistrictList(vkIdOrEnquiryId, stateId);
                    break;

                case "VTC":
                    vtcList = documentManagerRepo.getVTCList(vkIdOrEnquiryId, stateId, districtId);
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

                case "STATE":
                    //Bind Data
                    bindData();
                    break;

                case "DISTRICT":
                    setDistrictSpinnerAdapter(districtList, spinnerDistrict, shopEstCertificateDto.getDistrict());
                    break;

                case "VTC":
                    setVTCSpinnerAdapter(vtcList, spinnerVTC, shopEstCertificateDto.getVtc());
                    break;

                default:
                    break;
            }
        }
    }
}
