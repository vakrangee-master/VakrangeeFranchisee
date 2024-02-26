package in.vakrangee.franchisee.documentmanager.address;

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
import java.util.ArrayList;
import java.util.List;

import in.vakrangee.franchisee.documentmanager.DocumentManagerActivity;
import in.vakrangee.franchisee.documentmanager.DocumentManagerRepository;
import in.vakrangee.franchisee.documentmanager.R;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.commongui.FileAttachmentDialog;
import in.vakrangee.supercore.franchisee.commongui.FileUtils;
import in.vakrangee.supercore.franchisee.commongui.FilteredFilePickerActivity;
import in.vakrangee.supercore.franchisee.commongui.imagepreview.CustomImageZoomDialogDM;
import in.vakrangee.supercore.franchisee.gstdetails.GSTINDTO;
import in.vakrangee.supercore.franchisee.nextgenfranchiseeapplication.CustomFranchiseeApplicationSpinnerDto;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
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

public class AddressDetailsFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "AddressDetailsFragment";

    private View view;
    private Context context;
    private DeprecateHandler deprecateHandler;
    private PermissionHandler permissionHandler;

    private AddressDetailsDto addressDetailsDto;

    // View Objects
    private LinearLayout layoutAddressDetail, layoutAddressBelowDetails;
    private EditText editTextAddressLine1, editTextAddressLine2, editTextLandmark, editTextPincode;
    private TextView txtAddressTypeLbl, txtAddressLine1Lbl, txtAddressLine2Lbl, txtLandmarkLbl, txtStateLbl, txtDistrictLbl, txtVTCLbl, txtPincodeLbl,
            txtAddressProofTypeLbl, txtAddressProofCopyLbl;
    public CustomSearchableSpinner spinnerAddressType, spinnerState, spinnerDistrict, spinnerVTC, spinnerAddressProofType;
    private ImageView imgAddressProof;

    //img
    private static final int CAMERA_PIC_REQUEST = 100;
    private static final int BROWSE_FOLDER_REQUEST = 101;
    private static final int SCANLIB_REQUEST_CODE = 99;
    private Uri picUri;                 //Picture URI
    private String SEL_FILE_TYPE;
    private int FROM = -1;
    private static final int ADDRESS_PROOF_IMAGE_PHOTO = 1;
    private FileAttachmentDialog fileAttachementDialog;
    private CustomImageZoomDialogDM customImagePreviewDialog;
    private List<CustomFranchiseeApplicationSpinnerDto> addressTypeList, stateList, districtList, vtcList, addressProofTypeList;
    private DocumentManagerRepository documentManagerRepo;
    private GetAllSpinnerData getAllSpinnerData = null;
    private boolean IsEditable = false;
    private boolean isPermanentAddress = false;
    private AsyncGetSpecificAddressDetails asyncGetSpecificAddressDetails = null;
    private LinearLayout layoutAddressProofDetail;

    public AddressDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_address_details, container, false);

        //Initialize data
        this.context = getContext();
        deprecateHandler = new DeprecateHandler(context);
        permissionHandler = new PermissionHandler(getActivity());
        documentManagerRepo = new DocumentManagerRepository(context);

        // Bind UI
        layoutAddressProofDetail = view.findViewById(R.id.layoutAddressProofDetail);
        layoutAddressBelowDetails = view.findViewById(R.id.layoutAddressBelowDetails);
        layoutAddressDetail = view.findViewById(R.id.layoutAddressDetail);
        editTextAddressLine1 = view.findViewById(R.id.editTextAddressLine1);
        editTextAddressLine2 = view.findViewById(R.id.editTextAddressLine2);
        editTextLandmark = view.findViewById(R.id.editTextLandmark);
        editTextPincode = view.findViewById(R.id.editTextPincode);
        txtAddressTypeLbl = view.findViewById(R.id.txtAddressTypeLbl);
        txtAddressLine1Lbl = view.findViewById(R.id.txtAddressLine1Lbl);
        txtAddressLine2Lbl = view.findViewById(R.id.txtAddressLine2Lbl);
        txtLandmarkLbl = view.findViewById(R.id.txtLandmarkLbl);
        txtStateLbl = view.findViewById(R.id.txtStateLbl);
        txtDistrictLbl = view.findViewById(R.id.txtDistrictLbl);
        txtVTCLbl = view.findViewById(R.id.txtVTCLbl);
        txtPincodeLbl = view.findViewById(R.id.txtPincodeLbl);
        txtAddressProofTypeLbl = view.findViewById(R.id.txtAddressProofTypeLbl);
        txtAddressProofCopyLbl = view.findViewById(R.id.txtAddressProofCopyLbl);
        spinnerAddressType = view.findViewById(R.id.spinnerAddressType);
        spinnerState = view.findViewById(R.id.spinnerState);
        spinnerDistrict = view.findViewById(R.id.spinnerDistrict);
        spinnerVTC = view.findViewById(R.id.spinnerVTC);
        spinnerAddressProofType = view.findViewById(R.id.spinnerAddressProofType);
        imgAddressProof = view.findViewById(R.id.imgAddressProof);

        imgAddressProof.setOnClickListener(this);

        //Pincode
        editTextPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int len = editTextPincode.getText().toString().trim().length();
                if (len <= 0)
                    return;

                if (i != 6) {
                    editTextPincode.setTextColor(Color.parseColor("#000000"));
                    editTextPincode.setError(getResources().getString(R.string.EnterPincode));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                if (editTextPincode.getText().toString().contains(" ")) {
                    editTextPincode.setText(editTextPincode.getText().toString().replaceAll(" ", ""));
                    editTextPincode.setSelection(editTextPincode.getText().length());
                }

                if (editTextPincode.length() >= 6) {
                    editTextPincode.setTextColor(Color.parseColor("#468847"));
                    editTextPincode.setError(null);

                }
            }
        });

        setCompulsoryMarkLabel();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        permissionHandler.dismissSnackbar();
    }

    public void refreshAddressType(String data) {
        try {

            layoutAddressBelowDetails.setVisibility(View.GONE);
            if (TextUtils.isEmpty(data)) {
                AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                return;
            }

            addressDetailsDto = new AddressDetailsDto();

            //Handle Response
            String jsonData = data.replace("OKAY|", "");
            if (TextUtils.isEmpty(jsonData)) {
                AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                return;
            }

            Gson gson = new GsonBuilder().create();
            addressTypeList = gson.fromJson(jsonData, new TypeToken<ArrayList<CustomFranchiseeApplicationSpinnerDto>>() {
            }.getType());
            addressTypeList.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

            //Address Type
            setSpinnerAdapter(addressTypeList, spinnerAddressType, addressDetailsDto.getAddressType());

        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
        }
    }

    //region realod data
    public void reloadSpecificAddress(String data, String addressType) {
        //Reload Data
        if (TextUtils.isEmpty(data))
            addressDetailsDto = new AddressDetailsDto();
        else {
            try {
                Gson gson = new GsonBuilder().create();
                List<AddressDetailsDto> applicationList = gson.fromJson(data, new TypeToken<ArrayList<AddressDetailsDto>>() {
                }.getType());
                if (applicationList.size() > 0) {
                    addressDetailsDto = applicationList.get(0);
                } else {
                    addressDetailsDto = new AddressDetailsDto();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        addressDetailsDto.setAddressType(addressType);
        getAllSpinnerData = new GetAllSpinnerData(null, null, null);
        getAllSpinnerData.execute("STATE");

    }
    //endregion

    //region Set Adapter
    private void setSpinnerAdapter(List<CustomFranchiseeApplicationSpinnerDto> spinnerList, Spinner spinner, String selId) {

        //spinnerState.setEnabled(true);
        spinner.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, spinnerList));
        int pos = documentManagerRepo.getSelectedPos(spinnerList, selId);
        spinner.setSelection(pos);
        spinner.setOnItemSelectedListener(this);

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
                    addressDetailsDto.setState(stateDto.getId());

                    //Get District
                    getAllSpinnerData = new GetAllSpinnerData(addressDetailsDto.getState(), null, null);
                    getAllSpinnerData.execute("DISTRICT");
                }
            } else {
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

                //STATE
                addressDetailsDto.setState(null);

                //DISTRICT
                spinnerDistrict.setEnabled(IsEditable);
                setSpinnerAdapter(list1, spinnerDistrict, null);
                addressDetailsDto.setDistrict(null);

                //VTC
                spinnerVTC.setEnabled(IsEditable);
                setSpinnerAdapter(list1, spinnerVTC, null);
                addressDetailsDto.setVtc(null);

            }
        } else if (Id == R.id.spinnerDistrict) {
            if (position > 0) {
                spinnerDistrict.setTitle("Select District");
                spinnerDistrict.requestFocus();
                CustomFranchiseeApplicationSpinnerDto disDto = (CustomFranchiseeApplicationSpinnerDto) spinnerDistrict.getItemAtPosition(position);
                if (!disDto.getId().equals("0")) {
                    addressDetailsDto.setDistrict(disDto.getId());

                    //VTC
                    getAllSpinnerData = new GetAllSpinnerData(addressDetailsDto.getState(), addressDetailsDto.getDistrict(), null);
                    getAllSpinnerData.execute("VTC");

                }
            } else {
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

                //VTC
                spinnerVTC.setEnabled(IsEditable);
                setSpinnerAdapter(list1, spinnerVTC, null);
                addressDetailsDto.setVtc(null);

            }
        } else if (Id == R.id.spinnerVTC) {
            spinnerVTC.setTitle("Select VTC");
            spinnerVTC.requestFocus();
            CustomFranchiseeApplicationSpinnerDto vtcDto = (CustomFranchiseeApplicationSpinnerDto) spinnerVTC.getItemAtPosition(position);
            addressDetailsDto.setVtc(vtcDto.getId());

        } else if (Id == R.id.spinnerAddressType) {
            spinnerAddressType.setTitle("Select Address Type");
            spinnerAddressType.requestFocus();
            CustomFranchiseeApplicationSpinnerDto addressTypeDto = (CustomFranchiseeApplicationSpinnerDto) spinnerAddressType.getItemAtPosition(position);
            layoutAddressBelowDetails.setVisibility(View.GONE);

            if (!addressTypeDto.getId().equals("0")) {
                addressDetailsDto.setAddressType(addressTypeDto.getId());

                //Get Async Task
                getSpecificAddressDetails(addressDetailsDto.getAddressType());
            }
        } else if (Id == R.id.spinnerAddressProofType) {
            spinnerAddressProofType.setTitle("Select Address Proof Type");
            spinnerAddressProofType.requestFocus();
            CustomFranchiseeApplicationSpinnerDto proofDto = (CustomFranchiseeApplicationSpinnerDto) spinnerAddressProofType.getItemAtPosition(position);
            addressDetailsDto.setAddressProofType(proofDto.getId());

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void getSpecificAddressDetails(String addressTypeId) {
        asyncGetSpecificAddressDetails = new AsyncGetSpecificAddressDetails(context, addressTypeId, new AsyncGetSpecificAddressDetails.Callback() {
            @Override
            public void onResult(String result) {
                processSpecificAddressDetails(result, addressTypeId);
            }
        });
        asyncGetSpecificAddressDetails.execute("");
    }

    //region process Specific Address Details
    public void processSpecificAddressDetails(String result, String addressType) {
        try {

            if (TextUtils.isEmpty(result)) {
                AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                return;
            }

            if (result.startsWith("ERROR")) {
                String msg = result.replace("ERROR|", "");
                msg = TextUtils.isEmpty(msg) ? "Something went wrong. Please try again later." : msg;
                AlertDialogBoxInfo.alertDialogShow(context, msg);
                return;
            }

            if (result.startsWith("OKAY")) {
                addressDetailsDto = new AddressDetailsDto();

                //Handle Response
                String jsonData = result.replace("OKAY|", "");
                if (TextUtils.isEmpty(jsonData)) {
                    AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                    return;
                }

                //bindData
                reloadSpecificAddress(jsonData, addressType);

            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
        }
    }

    //region bind data
    private void bindData() {

        /*//Address Type
        setSpinnerAdapter(addressTypeList, spinnerAddressType, addressDetailsDto.getAddressType());*/

        layoutAddressBelowDetails.setVisibility(View.VISIBLE);

        //Address Line 1
        editTextAddressLine1.setText(addressDetailsDto.getAddressLine1());

        //Address Line 2
        editTextAddressLine2.setText(addressDetailsDto.getAddressLine2());

        //LandMark
        editTextLandmark.setText(addressDetailsDto.getLandmark());

        //State
        setSpinnerAdapter(stateList, spinnerState, addressDetailsDto.getState());

        //Pincode
        editTextPincode.setText(addressDetailsDto.getPincode());

        //Address Proof Type
        setSpinnerAdapter(addressProofTypeList, spinnerAddressProofType, addressDetailsDto.getAddressProofType());

        //Address Proof  Scanned Copy
        boolean IsPDF = (addressDetailsDto.getAddressProofScanExt() != null && addressDetailsDto.getAddressProofScanExt().equalsIgnoreCase("pdf")) ? true : false;
        if (IsPDF) {
            Glide.with(context)
                    .load(R.drawable.pdf)
                    .override(200, 200)
                    .apply(new RequestOptions()
                            .error(R.drawable.pdf)
                            .placeholder(R.drawable.pdf)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgAddressProof);

        } else {
            if (!TextUtils.isEmpty(addressDetailsDto.getAddressProofScanId())) {
                String gstUrl = Constants.DownloadImageUrl + addressDetailsDto.getAddressProofScanId();
                Glide.with(context)
                        .load(gstUrl)
                        .apply(new RequestOptions()
                                .error(R.drawable.ic_camera_alt_black_72dp)
                                .placeholder(R.drawable.ic_camera_alt_black_72dp)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))
                        .into(imgAddressProof);
            }
        }

        //Enable/disable views
        IsEditable = (!TextUtils.isEmpty(addressDetailsDto.getIsEditable()) && addressDetailsDto.getIsEditable().equalsIgnoreCase("1")) ? true : false;
        GUIUtils.setViewAndChildrenEnabled(layoutAddressDetail, IsEditable);
        ((DocumentManagerActivity) getActivity()).IsFooterLayoutVisible(IsEditable);

        isPermanentAddress = (!TextUtils.isEmpty(addressDetailsDto.getIsPermanentAddress()) && addressDetailsDto.getIsPermanentAddress().equalsIgnoreCase("1")) ? true : false;
        if (isPermanentAddress) {
            layoutAddressProofDetail.setVisibility(View.GONE);
        } else {
            layoutAddressProofDetail.setVisibility(View.VISIBLE);
        }

    }
    //endregion

    //region compulsaroy red mark
    public void setCompulsoryMarkLabel() {
        TextView[] txtViewsForCompulsoryMark = {txtAddressTypeLbl, txtAddressLine1Lbl, txtAddressLine2Lbl, txtLandmarkLbl, txtStateLbl, txtDistrictLbl, txtVTCLbl, txtPincodeLbl,
                txtAddressProofTypeLbl, txtAddressProofCopyLbl};
        GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);
    }
    //endregion

    //region onclick image and datepicker
    @Override
    public void onClick(View v) {
        int Id = v.getId();
        if (Id == R.id.imgAddressProof) {
            showImgOrPDF(v);
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
                        Glide.with(context).asDrawable().load(context.getResources().getDrawable(R.drawable.pdf)).into(imgAddressProof);
                        addressDetailsDto.setAddressProofScanBase64(base64Data);
                        addressDetailsDto.setAddressProofScanExt(ext);
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

    public void showAttachmentDialog(final View view) {
        fileAttachementDialog = new FileAttachmentDialog(context, new FileAttachmentDialog.IFileAttachmentClicks() {
            @Override
            public void cameraClick() {
                //If the app has not the permission then asking for the permission
                permissionHandler.requestPermission(view, Manifest.permission.CAMERA, getString(R.string.needs_permission_camera_msg), new IPermission() {
                    @Override
                    public void IsPermissionGranted(boolean IsGranted) {
                        if (IsGranted) {
                            FROM = ADDRESS_PROOF_IMAGE_PHOTO;

                            String ext = TextUtils.isEmpty(addressDetailsDto.getAddressProofScanExt()) ? "jpg" : addressDetailsDto.getAddressProofScanExt();

                            //FOR PDF
                            if (ext.equalsIgnoreCase("pdf")) {
                                SEL_FILE_TYPE = " images";
                                startScanCamera();
                                return;
                            }

                            //FOR Image
                            if ((TextUtils.isEmpty(addressDetailsDto.getAddressProofScanId()) ||
                                    addressDetailsDto.getAddressProofScanId().equalsIgnoreCase("0"))
                                    && TextUtils.isEmpty(addressDetailsDto.getAddressProofScanBase64())) {
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

    public int IsAddressDetailsValidated() {

        //Address Type
        if (TextUtils.isEmpty(addressDetailsDto.getAddressType()) || addressDetailsDto.getAddressType().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Address Type.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerAddressType, "Please select Address Type.", context);
            return 1;
        }

        //Address Line 1
        addressDetailsDto.setAddressLine1(editTextAddressLine1.getText().toString().trim());
        if (TextUtils.isEmpty(addressDetailsDto.getAddressLine1())) {
            Toast.makeText(context, "Please enter Address Line 1.", Toast.LENGTH_LONG).show();
            editTextAddressLine1.setError("Please enter Address Line 1.");
            return 1;
        }

        if (addressDetailsDto.getAddressLine1().trim().length() < 3) {
            Toast.makeText(context, "Please enter minimum 3 characters in Address Line 1.", Toast.LENGTH_LONG).show();
            editTextAddressLine1.setError("Please enter minimum 3 characters in Address Line 1.");
            return 1;
        }

        //Address Line 2
        addressDetailsDto.setAddressLine2(editTextAddressLine2.getText().toString().trim());
        if (!TextUtils.isEmpty(addressDetailsDto.getAddressLine2())) {
            if (addressDetailsDto.getAddressLine2().trim().length() < 3) {
                Toast.makeText(context, "Please enter minimum 3 characters in Address Line 2.", Toast.LENGTH_LONG).show();
                editTextAddressLine2.setError("Please enter minimum 3 characters in Address Line 2.");
                return 1;
            }
        }

        //LandMark
        addressDetailsDto.setLandmark(editTextLandmark.getText().toString().trim());
        if (TextUtils.isEmpty(addressDetailsDto.getLandmark())) {
            Toast.makeText(context, "Please enter LandMark.", Toast.LENGTH_LONG).show();
            editTextLandmark.setError("Please enter LandMark.");
            return 1;
        }

        if (addressDetailsDto.getLandmark().trim().length() < 3) {
            Toast.makeText(context, "Please enter minimum 3 characters in LandMark.", Toast.LENGTH_LONG).show();
            editTextLandmark.setError("Please enter minimum 3 characters in LandMark.");
            return 1;
        }

        //State
        if (TextUtils.isEmpty(addressDetailsDto.getState()) || addressDetailsDto.getState().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select State.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerState, "Please select State.", context);
            return 1;
        }

        //District
        if (TextUtils.isEmpty(addressDetailsDto.getDistrict()) || addressDetailsDto.getDistrict().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select District.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerDistrict, "Please select District.", context);
            return 1;
        }

        //VTC
        if (TextUtils.isEmpty(addressDetailsDto.getVtc()) || addressDetailsDto.getVtc().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select VTC.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerVTC, "Please select VTC.", context);
            return 1;
        }

        //Pin Code
        addressDetailsDto.setPincode(editTextPincode.getText().toString().trim());
        if (TextUtils.isEmpty(addressDetailsDto.getPincode())) {
            Toast.makeText(context, "Please enter Pin Code.", Toast.LENGTH_LONG).show();
            editTextPincode.setError("Please enter Pin Code.");
            return 1;
        }

        if (!isPermanentAddress) {
            //Address Proof Type
            if (TextUtils.isEmpty(addressDetailsDto.getAddressProofType()) || addressDetailsDto.getAddressProofType().equalsIgnoreCase("0")) {
                Toast.makeText(context, "Please select Address Proof Type.", Toast.LENGTH_LONG).show();
                GUIUtils.setErrorToSpinner(spinnerAddressProofType, "Please select Address Proof Type.", context);
                return 1;
            }

            //Address Proof Type Scan Copy
            if (TextUtils.isEmpty(addressDetailsDto.getAddressProofScanId()) || addressDetailsDto.getAddressProofScanId().equalsIgnoreCase("0")) {
                if (TextUtils.isEmpty(addressDetailsDto.getAddressProofScanBase64())) {
                    showMessage("Please add Address Proof Scan Copy.");
                    return 1;
                }
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

    public AddressDetailsDto getAddressDetail() {

        if (addressDetailsDto == null)
            return null;

        addressDetailsDto.setAddressLine1(editTextAddressLine1.getText().toString());
        addressDetailsDto.setAddressLine2(editTextAddressLine2.getText().toString());
        addressDetailsDto.setLandmark(editTextLandmark.getText().toString());
        addressDetailsDto.setPincode(editTextPincode.getText().toString());

        return addressDetailsDto;
    }

    private void refreshAddressDetailsImg(GSTINDTO imageDto) {

        addressDetailsDto.setChangedPhoto(imageDto.isChangedPhoto());
        addressDetailsDto.setAddressProofScanId(imageDto.getGstImageId());
        addressDetailsDto.setBitmap(imageDto.getBitmap());
        addressDetailsDto.setAddressProofScanExt("jpg");
        addressDetailsDto.setAddressProofScanBase64(imageDto.getGstImage());

    }

    private GSTINDTO prepareDtoForPreview(int FROM) {
        GSTINDTO imageDto = new GSTINDTO();

        imageDto.setChangedPhoto(addressDetailsDto.isChangedPhoto());
        imageDto.setGstImageId(addressDetailsDto.getAddressProofScanId());
        imageDto.setBitmap(addressDetailsDto.getBitmap());
        imageDto.setName("");
        imageDto.setGstImage(addressDetailsDto.getAddressProofScanBase64());
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

                    refreshAddressDetailsImg(dto);

                    //Refresh Address Proof Image
                    if (!TextUtils.isEmpty(addressDetailsDto.getAddressProofScanBase64())) {
                        Bitmap bitmap = CommonUtils.StringToBitMap(addressDetailsDto.getAddressProofScanBase64());
                        if (bitmap != null)
                            imgAddressProof.setImageBitmap(bitmap);
                    }
                }
            });
            customImagePreviewDialog.show();
            customImagePreviewDialog.setCancelable(false);

            //Title
            String title = "Address Proof Image";
            customImagePreviewDialog.setDialogTitle(title);

            //Change Photo Allowed
            boolean IsEditable = (!TextUtils.isEmpty(addressDetailsDto.getIsEditable()) &&
                    addressDetailsDto.getIsEditable().equalsIgnoreCase("1")) ? true : false;
            customImagePreviewDialog.allowChangePhoto(IsEditable);
            customImagePreviewDialog.allowSaveOption(IsEditable);
            customImagePreviewDialog.allowRemarks(false);

        } else {
            Toast.makeText(context, "No photo available to preview.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showImgOrPDF(final View view) {

        boolean IsGSTImgPDF = (addressDetailsDto.getAddressProofScanExt() != null && addressDetailsDto.getAddressProofScanExt().equalsIgnoreCase("pdf")) ? true : false;
        boolean IsEditable = (!TextUtils.isEmpty(addressDetailsDto.getIsEditable()) && addressDetailsDto.getIsEditable().equalsIgnoreCase("1")) ? true : false;

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
                    addressProofTypeList = documentManagerRepo.getAddressProofTypeList(vkIdOrEnquiryId);
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
                    setSpinnerAdapter(districtList, spinnerDistrict, addressDetailsDto.getDistrict());
                    break;

                case "VTC":
                    setSpinnerAdapter(vtcList, spinnerVTC, addressDetailsDto.getVtc());
                    break;

                default:
                    break;
            }
        }
    }

}
