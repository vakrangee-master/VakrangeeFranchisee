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
import android.text.Html;
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

public class FAPAddressFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "FAPAddressFragment";
    private Context context;
    private DeprecateHandler deprecateHandler;
    private PermissionHandler permissionHandler;

    private FranchiseeApplicationRepository fapRepo;
    private FAPAddressDto fapAddressDto;
    private List<CustomFranchiseeApplicationSpinnerDto> stateList;
    private List<CustomFranchiseeApplicationSpinnerDto> districtList;
    private List<CustomFranchiseeApplicationSpinnerDto> VTCList;

    private GetAllAddressDetailSpinnerData getAllAddressDetailSpinnerData = null;
    private boolean IsEditable = false;


    private List<CustomFranchiseeApplicationSpinnerDto> AddressProofTypeList;

    private int FROM = -1;
    private static final int ADDRESS_PROOF_SCAN_COPY = 3;
    private String SEL_FILE_TYPE;
    private FileAttachmentDialog fileAttachementDialog;
    private static final int CAMERA_PIC_REQUEST = 100;
    private static final int BROWSE_FOLDER_REQUEST = 101;
    private Uri picUri;                 //Picture URI

    //region Address
    private TextView txtCommunicationAdressHeader;
    private TextView txtPermanentAdressHeader;
    private LinearLayout layoutCommunincationAddress;
    private LinearLayout layoutPermanentAdress;

    //Communication Address
    public EditText editTextAddressLine1;
    public EditText editTextAddressLine2;
    public EditText editTextLandmark;
    public EditText editTextPincode;

    //Permanent  Adress
    private CheckBox checkbox_communication_address;
    private EditText editTextPermanentAddressLine1;
    private EditText editTextPermanentAddressLine2;
    private EditText editTextPermanentLandmark;
    private EditText editTextPermanentPincode;
    //endregion

    //region Labels
    private TextView txtAddressLine1Lbl;
    private TextView txtLandmarkLbl;
    private TextView txtStateLbl;
    private TextView txtDistrictLbl;
    private TextView txtVTCLbl;
    private TextView txtPincodeLbl;
    private TextView txtPermanentAddressLine1Lbl;
    private TextView txtPermanentLandmarkLbl;
    private TextView txtPermanentStateLbl;
    private TextView txtPermanentDistrictLbl;
    private TextView txtPermanentVTCLbl;
    private TextView txtPermanentPincodeLbl;

    public CustomSearchableSpinner commspinnerState;
    public CustomSearchableSpinner commspinnerDistrict;
    public CustomSearchableSpinner commspinnerVTC;

    private CustomSearchableSpinner PerspinnerState;
    private CustomSearchableSpinner PerspinnerDistrict;
    private CustomSearchableSpinner PerspinnerVTC;

    //endregion
    private LinearLayout layoutAddressParent;
    private LinearLayout layoutPermanentSection;
    private LinearLayout layoutPermanentAddressLine1;


    private Spinner spinnerAddressProofType;
    private EditText editTextAddressProofNumber1;
    private EditText editTextAddressProofNumber2;
    private ImageView imgAddressProofScanCopy;
    private TextView txtAddressProofScanCopyName;
    private TextView txtAddressProofTypeLbl;
    private TextView txtAddressProofNumberLbl;
    private TextView txtAddressProofChooseFile;
    private AsyncGetStateDistrictDataUsingPincode asyncGetStateDistrictDataUsingPincode = null;
    private static final String PER_DISTRICT = "PER_DISTRICT";
    private static final String COM_DISTRICT = "COM_DISTRICT";
    private static final String COM_VTC = "COM_VTC";
    private static final String PER_VTC = "PER_VTC";
    private static final String COM_STATE = "COM_STATE";
    private static final String PER_STATE = "PER_STATE";
    private static final String SPECIAL_CHARS = "~#^|$%&*!";
    private static final String ENTER_ADDRESS_PROOF = "Please enter Address Proof Number.";
    private static final String PLEASE_SELECT = "Please Select";
    private static final String SELECT_DISTRICT = "Select District";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_fap_address, container, false);

        bindViewId(rootView);
        //Data
        this.context = getContext();
        deprecateHandler = new DeprecateHandler(context);
        permissionHandler = new PermissionHandler(getActivity());
        fapRepo = new FranchiseeApplicationRepository(context);
        ButterKnife.bind(this, rootView);

        //Input filter
        setEdiTextInputFilter();

        //Set Compulsory mark
        setMandatory();

        txtCommunicationAdressHeader.setText(Html.fromHtml(context.getResources().getString(R.string.lbl_current_address) + "<br/><small>(as per adress proof)</small>"));
        txtPermanentAdressHeader.setText(Html.fromHtml(context.getResources().getString(R.string.lbl_permanent_address) + "<br/>"));

        TextChangedListener_AddressProof_Number();

        //Communication Pincode
        editTextPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Do Nothing
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

        //Permanent Pincode
        setPermanentPinCodeListener();

        return rootView;
    }

    private void setPermanentPinCodeListener() {
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
                }
            }
        });

    }

    private void setEdiTextInputFilter() {
        CommonUtils.InputFiletrWithMaxLength(editTextAddressLine1, SPECIAL_CHARS, 35);
        CommonUtils.InputFiletrWithMaxLength(editTextAddressLine2, SPECIAL_CHARS, 35);
        CommonUtils.InputFiletrWithMaxLength(editTextLandmark, SPECIAL_CHARS, 35);
        CommonUtils.InputFiletrWithMaxLength(editTextPermanentAddressLine1, SPECIAL_CHARS, 35);
        CommonUtils.InputFiletrWithMaxLength(editTextPermanentAddressLine2, SPECIAL_CHARS, 35);
        CommonUtils.InputFiletrWithMaxLength(editTextPermanentLandmark, SPECIAL_CHARS, 35);
        CommonUtils.InputFiletrWithMaxLength(editTextAddressProofNumber1, SPECIAL_CHARS, 20);
        CommonUtils.InputFiletrWithMaxLength(editTextAddressProofNumber2, SPECIAL_CHARS, 20);
    }

    private void setMandatory() {
        TextView[] txtViewsForCompulsoryMark = {txtAddressLine1Lbl, txtLandmarkLbl, txtStateLbl, txtAddressProofTypeLbl, txtAddressProofNumberLbl, txtAddressProofChooseFile, txtDistrictLbl, txtVTCLbl, txtPincodeLbl,
                txtPermanentAddressLine1Lbl, txtPermanentLandmarkLbl, txtPermanentStateLbl, txtPermanentDistrictLbl,
                txtPermanentVTCLbl, txtPermanentPincodeLbl};
        GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);
    }

    private void bindViewId(View view) {

        //region Address
        txtCommunicationAdressHeader = view.findViewById(R.id.txtCommunicationAdressHeader);
        txtPermanentAdressHeader = view.findViewById(R.id.txtPermanentAdressHeader);
        layoutCommunincationAddress = view.findViewById(R.id.layoutCommunincationAddress);
        layoutPermanentAdress = view.findViewById(R.id.layoutPermanentAdress);

        //Communication Address
        editTextAddressLine1 = view.findViewById(R.id.editTextAddressLine1);
        editTextAddressLine2 = view.findViewById(R.id.editTextAddressLine2);
        editTextLandmark = view.findViewById(R.id.editTextLandmark);
        editTextPincode = view.findViewById(R.id.editTextPincode);

        //Permanent  Adress
        checkbox_communication_address = view.findViewById(R.id.checkbox_communication_address);
        editTextPermanentAddressLine1 = view.findViewById(R.id.editTextPermanentAddressLine1);
        editTextPermanentAddressLine2 = view.findViewById(R.id.editTextPermanentAddressLine2);
        editTextPermanentLandmark = view.findViewById(R.id.editTextPermanentLandmark);
        editTextPermanentPincode = view.findViewById(R.id.editTextPermanentPincode);
        //endregion

        //region Labels
        txtAddressLine1Lbl = view.findViewById(R.id.txtAddressLine1Lbl);
        txtLandmarkLbl = view.findViewById(R.id.txtLandmarkLbl);
        txtStateLbl = view.findViewById(R.id.txtStateLbl);
        txtDistrictLbl = view.findViewById(R.id.txtDistrictLbl);
        txtVTCLbl = view.findViewById(R.id.txtVTCLbl);
        txtPincodeLbl = view.findViewById(R.id.txtPincodeLbl);
        txtPermanentAddressLine1Lbl = view.findViewById(R.id.txtPermanentAddressLine1Lbl);
        txtPermanentLandmarkLbl = view.findViewById(R.id.txtPermanentLandmarkLbl);
        txtPermanentStateLbl = view.findViewById(R.id.txtPermanentStateLbl);
        txtPermanentDistrictLbl = view.findViewById(R.id.txtPermanentDistrictLbl);
        txtPermanentVTCLbl = view.findViewById(R.id.txtPermanentVTCLbl);
        txtPermanentPincodeLbl = view.findViewById(R.id.txtPermanentPincodeLbl);

        commspinnerState = view.findViewById(R.id.spinnerState);
        commspinnerDistrict = view.findViewById(R.id.spinnerDistrict);
        commspinnerVTC = view.findViewById(R.id.spinnerVTC);

        PerspinnerState = view.findViewById(R.id.spinnerPermanentState);
        PerspinnerDistrict = view.findViewById(R.id.spinnerPermanentDistrict);
        PerspinnerVTC = view.findViewById(R.id.spinnerPermanentVTC);


        //endregion

        layoutAddressParent = view.findViewById(R.id.layoutAddressParent);
        layoutPermanentSection = view.findViewById(R.id.layoutPermanentSection);
        layoutPermanentAddressLine1 = view.findViewById(R.id.layoutPermanentAddressLine1);


        spinnerAddressProofType = view.findViewById(R.id.spinnerAddressProofType);
        editTextAddressProofNumber1 = view.findViewById(R.id.editTextAddressProofNumber1);
        editTextAddressProofNumber2 = view.findViewById(R.id.editTextAddressProofNumber2);
        imgAddressProofScanCopy = view.findViewById(R.id.imgAddressProofScanCopy);
        txtAddressProofScanCopyName = view.findViewById(R.id.txtAddressProofScanCopyName);

        txtAddressProofTypeLbl = view.findViewById(R.id.txtAddressProofTypeLbl);
        txtAddressProofNumberLbl = view.findViewById(R.id.txtAddressProofNumberLbl);
        txtAddressProofChooseFile = view.findViewById(R.id.txtAddressProofChooseFile);

        txtCommunicationAdressHeader.setOnClickListener(this);
        txtPermanentAdressHeader.setOnClickListener(this);
        imgAddressProofScanCopy.setOnClickListener(this);

    }

    private void BindSpineer() {

        GUIUtils.setViewAndChildrenEnabled(layoutPermanentSection, true);

        commspinnerState.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, stateList));
        int appsstatePos = fapRepo.getSelectedPos(stateList, fapAddressDto.getComState());
        commspinnerState.setSelection(appsstatePos);
        commspinnerState.setOnItemSelectedListener(this);

        //STEP 2: Permanent state
        PerspinnerState.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, stateList));
        int appsperstatePos = fapRepo.getSelectedPos(stateList, fapAddressDto.getPerState());
        PerspinnerState.setSelection(appsperstatePos);
        PerspinnerState.setOnItemSelectedListener(this);

        //STEP 3: Address Line 1
        editTextAddressLine1.setText(fapAddressDto.getComAddressLine1());
        //STEP 3.1: Address Line 2
        editTextAddressLine2.setText(fapAddressDto.getComAdressLine2());
        //STEP 3.2: LandMark
        editTextLandmark.setText(fapAddressDto.getComLandmark());

        //STEP 4: Pin code
        editTextPincode.setText(fapAddressDto.getComPincode());

        //STEP 1-Address Proof Type
        CustomFranchiseeApplicationSpinnerAdapter vakrnageeKendramodelapdater = new CustomFranchiseeApplicationSpinnerAdapter(context, AddressProofTypeList);
        spinnerAddressProofType.setAdapter(vakrnageeKendramodelapdater);
        int appvakrangeeKendraModelPos = fapRepo.getSelectedPos(AddressProofTypeList, fapAddressDto.getAddressProofType());
        spinnerAddressProofType.setSelection(appvakrangeeKendraModelPos);
        spinnerAddressProofType.setOnItemSelectedListener(this);

        //STEP 11 - Address proof Number
        editTextAddressProofNumber1.setText(fapAddressDto.getAddressProofNumber1());
        //STEP 12 - re-enter Address proof Number 2
        editTextAddressProofNumber2.setText(fapAddressDto.getAddressProofNumber1());

        //STEP 16: Address Proof Image
        boolean IsAddressPDF = ((fapAddressDto.getAddressProofFileExt() != null) && fapAddressDto.getAddressProofFileExt().equalsIgnoreCase("pdf")) ? true : false;
        if (IsAddressPDF) {
            Glide.with(context).asDrawable().load(R.drawable.pdf).override(200, 200).into(imgAddressProofScanCopy);
        } else {
            if (!TextUtils.isEmpty(fapAddressDto.getAddressProofFileId())) {
                String addProofUrl = Constants.DownloadImageUrl + fapAddressDto.getAddressProofFileId();
                Glide.with(context)
                        .load(addProofUrl)
                        .apply(new RequestOptions()
                                .error(R.drawable.ic_camera_alt_black_72dp)
                                .placeholder(R.drawable.ic_camera_alt_black_72dp)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))
                        .into(imgAddressProofScanCopy);
            }
        }

        //STEP 5: Address Line 1
        editTextPermanentAddressLine1.setText(fapAddressDto.getPerAddressLine1());
        //STEP 5.1: Address Line 2
        editTextPermanentAddressLine2.setText(fapAddressDto.getPerAddressLine2());
        //STEP 5.2: LandMark
        editTextPermanentLandmark.setText(fapAddressDto.getPerLandmark());

        //STEP 6: Pin code
        editTextPermanentPincode.setText(fapAddressDto.getPerPincode());

        //STEP 7: Same Permanent Address
        checkbox_communication_address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean IsChecked) {
                String status = IsChecked ? "1" : "0";
                fapAddressDto.setIsSamePermanentAddress(status);
                if (status.equalsIgnoreCase("1")) {
                    setSameAddress();
                    GUIUtils.setViewAndChildrenEnabled(layoutPermanentSection, false);

                } else {
                    resetPermanentAddress();
                    GUIUtils.setViewAndChildrenEnabled(layoutPermanentSection, true);

                }
            }
        });

    }

    private void spinner_focusablemode() {
        //Do Nothing
    }

    @Override
    public void onPause() {
        super.onPause();
        permissionHandler.dismissSnackbar();
    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();
        if (Id == R.id.txtCommunicationAdressHeader) {
            txtCommunicationAdressHeader.setTextColor(deprecateHandler.getColor(R.color.gray));
            txtCommunicationAdressHeader.setBackgroundColor(deprecateHandler.getColor(R.color.grey));
            txtPermanentAdressHeader.setTextColor(deprecateHandler.getColor(R.color.white));
            txtPermanentAdressHeader.setBackgroundColor(deprecateHandler.getColor(R.color.gray));

            layoutPermanentAdress.setVisibility(View.GONE);
            layoutCommunincationAddress.setVisibility(View.VISIBLE);

        } else if (Id == R.id.txtPermanentAdressHeader) {
            txtPermanentAdressHeader.setTextColor(deprecateHandler.getColor(R.color.gray));
            txtPermanentAdressHeader.setBackgroundColor(deprecateHandler.getColor(R.color.grey));

            txtCommunicationAdressHeader.setTextColor(deprecateHandler.getColor(R.color.white));
            txtCommunicationAdressHeader.setBackgroundColor(deprecateHandler.getColor(R.color.gray));

            layoutPermanentAdress.setVisibility(View.VISIBLE);
            layoutCommunincationAddress.setVisibility(View.GONE);

        } else if (Id == R.id.imgAddressProofScanCopy) {
            FROM = ADDRESS_PROOF_SCAN_COPY;
            SEL_FILE_TYPE = "images/pdf";
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

            case ADDRESS_PROOF_SCAN_COPY:
                if (IsDrawable)
                    Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgAddressProofScanCopy);
                else
                    Glide.with(context).asBitmap().load(bitmap).into(imgAddressProofScanCopy);

                txtAddressProofScanCopyName.setVisibility(View.VISIBLE);
                txtAddressProofScanCopyName.setText(fileName);
                fapAddressDto.setAddressProofCopyImageBase64(base64);
                fapAddressDto.setAddressProofFileExt(ext);
                fapAddressDto.setAddressProofCopyFileName(fileName);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getAllAddressDetailSpinnerData != null && !getAllAddressDetailSpinnerData.isCancelled()) {
            getAllAddressDetailSpinnerData.cancel(true);
        }

        if (asyncGetStateDistrictDataUsingPincode != null && !asyncGetStateDistrictDataUsingPincode.isCancelled()) {
            asyncGetStateDistrictDataUsingPincode.cancel(true);
        }
    }

    //Validation
    public int IsFranchiseeAddressValidated() {

        int comStatus = validateCommunicationAddress();
        if (comStatus != 0) {
            return comStatus;
        }

        //STEP 10: Address Proof Type
        if (TextUtils.isEmpty(fapAddressDto.getAddressProofType()) || fapAddressDto.getAddressProofType().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Address Proof Type.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerAddressProofType, "Please select Address Proof Type.", context);
            return 1;
        }

        //STEP 11: Address Proof Number 1
        fapAddressDto.setAddressProofNumber1(editTextAddressProofNumber1.getText().toString().trim());
        if (TextUtils.isEmpty(fapAddressDto.getAddressProofNumber1())) {
            Toast.makeText(context, ENTER_ADDRESS_PROOF, Toast.LENGTH_LONG).show();
            editTextAddressProofNumber1.setError(ENTER_ADDRESS_PROOF);
            return 1;
        }

        //STEP 12: Address Proof Number 2
        if (TextUtils.isEmpty(fapAddressDto.getAddressProofNumber1())) {
            Toast.makeText(context, ENTER_ADDRESS_PROOF, Toast.LENGTH_LONG).show();
            editTextAddressProofNumber2.setError(ENTER_ADDRESS_PROOF);
            return 1;
        }

        //STEP 13: Address Proof Scan Copy
        if (TextUtils.isEmpty(fapAddressDto.getAddressProofFileId()) || fapAddressDto.getAddressProofFileId().equalsIgnoreCase("0")) {
            if (TextUtils.isEmpty(fapAddressDto.getAddressProofCopyImageBase64())) {
                showMessage("Please add Address Proof Scan copy.");
                return 1;
            }
        }

        int status = validatePermanentAddressData();
        if (status != 0) {
            return status;
        }
        return 0;
    }
    private int validateCommunicationAddress(){
        //STEP 1: Address Line 1
        fapAddressDto.setComAddressLine1(editTextAddressLine1.getText().toString().trim());
        if (TextUtils.isEmpty(fapAddressDto.getComAddressLine1())) {
            Toast.makeText(context, "Please enter Address Line 1 in Communication section.", Toast.LENGTH_LONG).show();
            editTextAddressLine1.setError("Please enter Address Line 1 in Communication section.");
            return 1;
        }

        //STEP 2: Landmark
        fapAddressDto.setComLandmark(editTextLandmark.getText().toString().trim());
        if (TextUtils.isEmpty(fapAddressDto.getComLandmark())) {
            Toast.makeText(context, "Please enter LandMark in Communication section.", Toast.LENGTH_LONG).show();
            editTextLandmark.setError("Please enter LandMark in Communication section.");
            return 2;
        }

        //STEP 3: State
        if (TextUtils.isEmpty(fapAddressDto.getComState()) || fapAddressDto.getComState().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select State in Communication section.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(commspinnerState, "Please select State in Communication section.", context);
            return 3;
        }

        //STEP 4: District
        if (TextUtils.isEmpty(fapAddressDto.getComDistrict()) || fapAddressDto.getComDistrict().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select District in Communication section.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(commspinnerState, "Please select District in Communication section.", context);
            return 4;
        }

        //STEP 5: Village/Town/City
        if (TextUtils.isEmpty(fapAddressDto.getComVTC()) || fapAddressDto.getComVTC().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Village/Town/City in Communication section.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(commspinnerVTC, "Please select Village/Town/City in Communication section.", context);
            return 5;
        }

        //STEP 6: PinCode
        fapAddressDto.setComPincode(editTextPincode.getText().toString().trim());
        if (TextUtils.isEmpty(fapAddressDto.getComPincode())) {
            Toast.makeText(context, "Please enter PinCode in Communication section.", Toast.LENGTH_LONG).show();
            editTextPincode.setError("Please enter PinCode in Communication section.");
            return 6;
        }

        return 0;

    }

    private int validatePermanentAddressData() { //STEP 7: Permanent Address Line 1
        fapAddressDto.setPerAddressLine1(editTextPermanentAddressLine1.getText().toString().trim());
        if (TextUtils.isEmpty(fapAddressDto.getPerAddressLine1())) {
            Toast.makeText(context, "Please enter Address Line 1 in Permanent section.", Toast.LENGTH_LONG).show();
            editTextPermanentAddressLine1.setError("Please enter Address Line 1 in Permanent section.");
            return 7;
        }

        //STEP 8: Permanent Landmark
        fapAddressDto.setPerLandmark(editTextPermanentLandmark.getText().toString().trim());
        if (TextUtils.isEmpty(fapAddressDto.getPerLandmark())) {
            Toast.makeText(context, "Please enter LandMark in Permanent section.", Toast.LENGTH_LONG).show();
            editTextPermanentLandmark.setError("Please enter LandMark in Permanent section.");
            return 8;
        }

        //STEP 9: Permanent State
        if (TextUtils.isEmpty(fapAddressDto.getPerState())) {
            Toast.makeText(context, "Please select State in Permanent section.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(PerspinnerState, "Please select State in Permanent section.", context);
            return 9;
        }

        //STEP 10: Permanent District
        if (TextUtils.isEmpty(fapAddressDto.getPerDistrict())) {
            Toast.makeText(context, "Please select District in Permanent section.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(PerspinnerDistrict, "Please select District in Permanent section.", context);
            return 10;
        }

        //STEP 11: Permanent Village/Town/City
        if (TextUtils.isEmpty(fapAddressDto.getPerVTC())) {
            Toast.makeText(context, "Please select Village/Town/City in Permanent section.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(PerspinnerVTC, "Please select Village/Town/City in Permanent section.", context);
            return 11;
        }

        //STEP 12: Permanent PinCode
        fapAddressDto.setPerPincode(editTextPermanentPincode.getText().toString().trim());
        if (TextUtils.isEmpty(fapAddressDto.getPerPincode())) {
            Toast.makeText(context, "Please enter PinCode in Permanent section.", Toast.LENGTH_LONG).show();
            editTextPermanentPincode.setError("Please enter PinCode in Permanent section.");
            return 12;
        }

        return 0;

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        int Id = adapterView.getId();
        if (Id == R.id.spinnerAddressProofType) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerAddressProofType.getItemAtPosition(position);
                fapAddressDto.setAddressProofType(entityDto.getId());
            }
        } else if (Id == R.id.spinnerState) {
            if (position > 0) {
                commspinnerState.setTitle("Select State");
                commspinnerState.requestFocus();
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) commspinnerState.getItemAtPosition(position);
                if (!entityDto.getId().equals("0")) {
                    fapAddressDto.setComState(entityDto.getId());

                    //Get District
                    fapAddressDto.setComPincode(editTextPincode.getText().toString());
                    getAllAddressDetailSpinnerData = new GetAllAddressDetailSpinnerData(fapAddressDto.getComPincode(), fapAddressDto.getComState(), null);
                    getAllAddressDetailSpinnerData.execute(COM_DISTRICT);

                }
            } else {
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", PLEASE_SELECT));

                commspinnerDistrict.setEnabled(true);
                setDistrictSpinnerAdapter(list1, commspinnerDistrict, null);
                fapAddressDto.setComDistrict(null);
                commspinnerVTC.setEnabled(true);
                setDistrictSpinnerAdapter(list1, commspinnerVTC, null);
                fapAddressDto.setComVTC(null);

            }
        } else if (Id == R.id.spinnerDistrict) {
            if (position > 0) {
                commspinnerDistrict.setTitle(SELECT_DISTRICT);
                commspinnerDistrict.requestFocus();
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) commspinnerDistrict.getItemAtPosition(position);
                if (!entityDto.getId().equals("0")) {
                    fapAddressDto.setComDistrict(entityDto.getId());

                    //Get VTC
                    fapAddressDto.setComPincode(editTextPincode.getText().toString());
                    getAllAddressDetailSpinnerData = new GetAllAddressDetailSpinnerData(fapAddressDto.getComPincode(), fapAddressDto.getComState(), fapAddressDto.getComDistrict());
                    getAllAddressDetailSpinnerData.execute(COM_VTC);

                }
            } else {
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", PLEASE_SELECT));

                commspinnerVTC.setEnabled(true);
                setDistrictSpinnerAdapter(list1, commspinnerVTC, null);
                fapAddressDto.setComVTC(null);
            }
        } else if (Id == R.id.spinnerVTC) {
            commspinnerVTC.setTitle("Select VTC");
            commspinnerVTC.requestFocus();
            CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) commspinnerVTC.getItemAtPosition(position);
            fapAddressDto.setComVTC(entityDto.getId());

        } else if (Id == R.id.spinnerPermanentState) {
            if (position > 0) {
                PerspinnerState.setTitle("Select State");
                PerspinnerState.requestFocus();
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) PerspinnerState.getItemAtPosition(position);
                if (!entityDto.getId().equals("0")) {
                    fapAddressDto.setPerState(entityDto.getId());

                    //Get District
                    fapAddressDto.setPerPincode(editTextPermanentPincode.getText().toString());
                    getAllAddressDetailSpinnerData = new GetAllAddressDetailSpinnerData(fapAddressDto.getPerPincode(), fapAddressDto.getPerState(), null);
                    getAllAddressDetailSpinnerData.execute(PER_DISTRICT);

                }
            } else {
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", PLEASE_SELECT));

                PerspinnerDistrict.setEnabled(true);
                setDistrictSpinnerAdapter(list1, PerspinnerDistrict, null);
                fapAddressDto.setPerDistrict(null);
                PerspinnerVTC.setEnabled(true);
                setDistrictSpinnerAdapter(list1, PerspinnerVTC, null);
                fapAddressDto.setPerVTC(null);

            }
        } else if (Id == R.id.spinnerPermanentDistrict) {
            if (position > 0) {
                PerspinnerDistrict.setTitle(SELECT_DISTRICT);
                PerspinnerDistrict.requestFocus();
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) PerspinnerDistrict.getItemAtPosition(position);
                if (!entityDto.getId().equals("0")) {
                    fapAddressDto.setPerDistrict(entityDto.getId());

                    //Get VTC
                    fapAddressDto.setPerPincode(editTextPermanentPincode.getText().toString());
                    getAllAddressDetailSpinnerData = new GetAllAddressDetailSpinnerData(fapAddressDto.getPerPincode(), fapAddressDto.getPerState(), fapAddressDto.getPerDistrict());
                    getAllAddressDetailSpinnerData.execute(PER_VTC);

                }
            } else {
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", PLEASE_SELECT));

                PerspinnerVTC.setEnabled(true);
                setDistrictSpinnerAdapter(list1, PerspinnerVTC, null);
                fapAddressDto.setPerVTC(null);

            }
        } else if (Id == R.id.spinnerPermanentVTC) {
            PerspinnerVTC.setTitle(SELECT_DISTRICT);
            PerspinnerVTC.requestFocus();
            CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) PerspinnerVTC.getItemAtPosition(position);
            fapAddressDto.setPerVTC(entityDto.getId());
        }

    }

    public void resetPermanentAddress() {
        editTextPermanentAddressLine1.setText("");
        editTextPermanentAddressLine2.setText("");
        editTextPermanentLandmark.setText("");
        editTextPermanentPincode.setText("");
        PerspinnerState.setSelection(0);
        PerspinnerDistrict.setSelection(0);
        PerspinnerVTC.setSelection(0);

    }

    public void setSameAddress() {

        try {
            editTextPermanentAddressLine1.setText(editTextAddressLine1.getText().toString().trim());
            editTextPermanentAddressLine2.setText(editTextAddressLine2.getText().toString().trim());
            editTextPermanentLandmark.setText(editTextLandmark.getText().toString().trim());
            editTextPermanentPincode.setText(editTextPincode.getText().toString().trim());

            //State
            int statePos = fapRepo.getSelectedPos(stateList, fapAddressDto.getComState());
            int stateSize = PerspinnerState.getAdapter().getCount();
            if (statePos >= stateSize)
                setStateSpinnerAdapter(stateList, PerspinnerState, fapAddressDto.getComState());
            else {
                PerspinnerState.setSelection(statePos);
                PerspinnerState.getOnItemSelectedListener().onItemSelected(PerspinnerState, PerspinnerState.getSelectedView(), statePos, PerspinnerState.getSelectedItemId());
            }

            //District
            int districtPos = fapRepo.getSelectedPos(districtList, fapAddressDto.getComDistrict());
            int districtSize = PerspinnerDistrict.getAdapter().getCount();
            if (districtPos >= districtSize)
                setDistrictSpinnerAdapter(districtList, PerspinnerDistrict, fapAddressDto.getComDistrict());
            else {
                PerspinnerDistrict.setSelection(districtPos);
                PerspinnerDistrict.getOnItemSelectedListener().onItemSelected(PerspinnerDistrict, PerspinnerDistrict.getSelectedView(), districtPos, PerspinnerDistrict.getSelectedItemId());
            }

            //VTC
            int VTCPos = fapRepo.getSelectedPos(VTCList, fapAddressDto.getComVTC());
            int VTCSize = PerspinnerVTC.getAdapter().getCount();
            if (VTCPos >= VTCSize)
                setVTCSpinnerAdapter(VTCList, PerspinnerVTC, fapAddressDto.getComVTC());
            else {
                PerspinnerVTC.setSelection(VTCPos);
                PerspinnerVTC.getOnItemSelectedListener().onItemSelected(PerspinnerVTC, PerspinnerVTC.getSelectedView(), VTCPos, PerspinnerVTC.getSelectedItemId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //region Set District Adapter
    private void setDistrictSpinnerAdapter(List<CustomFranchiseeApplicationSpinnerDto> districtList, Spinner spinnerDistrict, String selDistrictId) {

        spinnerDistrict.setEnabled(true);
        spinnerDistrict.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, districtList));
        int appsstatePos = fapRepo.getSelectedPos(districtList, selDistrictId);
        spinnerDistrict.setSelection(appsstatePos);
        spinnerDistrict.setOnItemSelectedListener(this);

    }
    //endregion

    //region Set State Adapter
    private void setStateSpinnerAdapter(List<CustomFranchiseeApplicationSpinnerDto> stateList, Spinner spinnerState, String selStateId) {

        spinnerState.setEnabled(true);
        spinnerState.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, stateList));
        int appsstatePos = fapRepo.getSelectedPos(stateList, selStateId);
        spinnerState.setSelection(appsstatePos);
        spinnerState.setOnItemSelectedListener(this);

    }
    //endregion

    //region Set VTC Adapter
    private void setVTCSpinnerAdapter(List<CustomFranchiseeApplicationSpinnerDto> VTCList, Spinner spinnerVTC, String selVTCId) {

        spinnerVTC.setEnabled(true);
        spinnerVTC.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, VTCList));
        int appsstatePos = fapRepo.getSelectedPos(VTCList, selVTCId);
        spinnerVTC.setSelection(appsstatePos);
        spinnerVTC.setOnItemSelectedListener(this);
    }
    //endregion

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //Do Nothing
    }

    public void reloadData(String data, boolean IsEditable) {

        //Reload Data
        if (TextUtils.isEmpty(data))
            fapAddressDto = new FAPAddressDto();
        else {
            try {
                Gson gson = new GsonBuilder().create();
                fapAddressDto = gson.fromJson(data, FAPAddressDto.class);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.IsEditable = IsEditable;
        getAllAddressDetailSpinnerData = new GetAllAddressDetailSpinnerData(null, null, null);
        getAllAddressDetailSpinnerData.execute("ALL");

    }

    class GetAllAddressDetailSpinnerData extends AsyncTask<String, Void, String> {

        private String pincode;
        private String stateId;
        private String districtId;
        private String type;

        public GetAllAddressDetailSpinnerData(String pincode, String stateId, String districtId) {
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

                case "ALL":

                    //STEP 1:Address Proof Type
                    AddressProofTypeList = fapRepo.getAddressProofTypeList();

                    stateList = fapRepo.getAllStateBylList();
                    break;

                case COM_STATE:
                case PER_STATE:
                    //STEP 2: State List
                    stateList = fapRepo.getAllStateBylListUsingPincode(pincode);
                    break;

                case COM_DISTRICT:
                case PER_DISTRICT:
                    //STEP 2: District List
                    districtList = fapRepo.getAllDistrictBylList(stateId);
                    break;

                case COM_VTC:
                case PER_VTC:
                    //STEP 3: VTC List
                    VTCList = fapRepo.getAllVTCBylList(stateId, districtId);
                    break;

                default:
                    break;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            bindData(type);

            //Enable/disable views
            GUIUtils.setViewAndChildrenEnabled(layoutAddressParent, IsEditable);

            //Added : 20th Nov 2018 -> Disable Permanent address detail in case of Same as Current Address.
            if (!TextUtils.isEmpty(fapAddressDto.getIsSamePermanentAddress())) {
                int type = Integer.parseInt(fapAddressDto.getIsSamePermanentAddress());
                if (type == 1) {
                    checkbox_communication_address.setChecked(true);
                    GUIUtils.setViewAndChildrenEnabled(layoutPermanentSection, false);

                } else {
                    checkbox_communication_address.setChecked(false);
                    if (IsEditable)
                        GUIUtils.setViewAndChildrenEnabled(layoutPermanentSection, true);

                }
            }
        }
    }

    private void bindData(String type) {

        switch (type.toUpperCase()) {

            case "ALL":
                BindSpineer();
                break;

            case COM_STATE:
                if (stateList.size() == 2) {
                    fapAddressDto.setComState(stateList.get(1).getId());
                }
                setDistrictSpinnerAdapter(stateList, commspinnerState, fapAddressDto.getComState());

                break;

            case PER_STATE:
                if (stateList.size() == 2) {
                    fapAddressDto.setPerState(stateList.get(1).getId());
                }
                setDistrictSpinnerAdapter(stateList, PerspinnerState, fapAddressDto.getPerState());

                break;

            case COM_DISTRICT:
                if (districtList.size() == 2) {
                    fapAddressDto.setComDistrict(districtList.get(1).getId());
                }
                setDistrictSpinnerAdapter(districtList, commspinnerDistrict, fapAddressDto.getComDistrict());

                break;

            case PER_DISTRICT:
                if (districtList.size() == 2) {
                    fapAddressDto.setPerDistrict(districtList.get(1).getId());
                }
                setDistrictSpinnerAdapter(districtList, PerspinnerDistrict, fapAddressDto.getPerDistrict());

                break;

            case COM_VTC:
                setVTCSpinnerAdapter(VTCList, commspinnerVTC, fapAddressDto.getComVTC());
                break;

            case PER_VTC:
                setVTCSpinnerAdapter(VTCList, PerspinnerVTC, fapAddressDto.getPerVTC());
                break;

            default:
                break;
        }

    }

    public FAPAddressDto getFapAddressDto() {

        fapAddressDto.setComAddressLine1(editTextAddressLine1.getText().toString());
        fapAddressDto.setComAdressLine2(editTextAddressLine2.getText().toString());
        fapAddressDto.setComLandmark(editTextLandmark.getText().toString());
        fapAddressDto.setComPincode(editTextPincode.getText().toString());
        fapAddressDto.setPerAddressLine1(editTextPermanentAddressLine1.getText().toString());
        fapAddressDto.setPerAddressLine2(editTextPermanentAddressLine2.getText().toString());
        fapAddressDto.setPerLandmark(editTextPermanentLandmark.getText().toString());
        fapAddressDto.setPerPincode(editTextPermanentPincode.getText().toString());
        fapAddressDto.setAddressProofNumber1(editTextAddressProofNumber1.getText().toString());

        return fapAddressDto;
    }

    //region TextChangedListener_AddressProof_Number
    private void TextChangedListener_AddressProof_Number() {
        CommonUtils.EditextListener(editTextAddressProofNumber1, editTextAddressProofNumber2, 1, "Please enter minimum 1 characters", "AddressProof");
    }
    //endregion

    //region Get State District Using PinCode
    private void performChangesPincode(String pincode, String type) {

        if (TextUtils.isEmpty(pincode))
            return;

        //Get State District Data Using Pin Code
        String pinCode = "";
        if (type.equalsIgnoreCase(COM_STATE)) {
            fapAddressDto.setComPincode(editTextPincode.getText().toString());
            pinCode = fapAddressDto.getComPincode();
        } else {
            fapAddressDto.setPerPincode(editTextPermanentPincode.getText().toString());
            pinCode = fapAddressDto.getPerPincode();
        }
        getAllAddressDetailSpinnerData = new GetAllAddressDetailSpinnerData(pinCode, null, null);
        getAllAddressDetailSpinnerData.execute(type);

    }
    //endregion

}
