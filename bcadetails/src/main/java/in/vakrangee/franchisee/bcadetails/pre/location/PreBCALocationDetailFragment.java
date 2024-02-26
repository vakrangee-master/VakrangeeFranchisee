package in.vakrangee.franchisee.bcadetails.pre.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import in.vakrangee.franchisee.bcadetails.BCAEntryDetailsRepository;
import in.vakrangee.franchisee.bcadetails.R;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.commongui.FileAttachmentDialog;
import in.vakrangee.supercore.franchisee.commongui.FileUtils;
import in.vakrangee.supercore.franchisee.commongui.FilteredFilePickerActivity;
import in.vakrangee.supercore.franchisee.nextgenfranchiseeapplication.CustomFranchiseeApplicationSpinnerDto;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.GUIUtils;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;
import in.vakrangee.supercore.franchisee.utils.ImageZipper;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;
import in.vakrangee.supercore.franchisee.utils.CustomSearchableSpinner;

public class PreBCALocationDetailFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private View view;
    private Context context;
    private DeprecateHandler deprecateHandler;
    private PermissionHandler permissionHandler;
    private PreBCALocationDetailsDto preBCALocationDetailsDto;
    private boolean IsEditable = false;
    private GetAllSpinnerData getAllSpinnerData = null;

    private BCAEntryDetailsRepository bcaEntryDetailsRepo;
    private List<CustomFranchiseeApplicationSpinnerDto> stateList, divisionList, districtList, blockList, vtcList, wardList;

    private TextView txtLocationTypeLbl;
    private RadioGroup radioGroupLocationType;
    private RadioButton radioButtonRural;
    private RadioButton radioButtonUrban;
    private RadioButton radioButtonSemiUrban;
    private RadioButton radioButtonMetro;
    private TextView txtBCAOutletAddressLbl;
    private EditText editTextBCAOutletAddress;
    private TextView txtStateLbl;
    private CustomSearchableSpinner spinnerState;
    private TextView txtDivisionLbl;
    private CustomSearchableSpinner spinnerDivision;
    private TextView txtDistrictLbl;
    private CustomSearchableSpinner spinnerDistrict;
    private TextView txtTehsilLbl;
    private CustomSearchableSpinner spinnerTehsil;
    private TextView txtVTCLbl;
    private CustomSearchableSpinner spinnerVTC;
    private TextView txtLocalityLbl;
    private EditText editTextLocality;
    private TextView txtPincodeLbl;
    private EditText editTextPincode;
    private LinearLayout layoutBCAOutletInfo;
    private TextView txtOutletAddressProofLbl;
    private ImageView imgOutletAddressProof;
    private FileAttachmentDialog fileAttachementDialog;
    private static final int CAMERA_PIC_REQUEST = 100;
    private static final int BROWSE_FOLDER_REQUEST = 101;
    private Uri picUri;                 //Picture URI
    private String SEL_FILE_TYPE;
    private int FROM = -1;
    private static final int OUTLET_ADDRESS_COPY = 4;

    public PreBCALocationDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag_pre_bca_location_details, container, false);

        bindViewId(view);

        //Initialize data
        this.context = getContext();
        deprecateHandler = new DeprecateHandler(context);
        permissionHandler = new PermissionHandler(getActivity());
        bcaEntryDetailsRepo = new BCAEntryDetailsRepository(context);

        setCompulsoryMarkLabel();


        return view;
    }

    private void bindViewId(View view) {
        txtLocationTypeLbl = view.findViewById(R.id.txtLocationTypeLbl);
        radioGroupLocationType = view.findViewById(R.id.radioGroupLocationType);
        radioButtonRural = view.findViewById(R.id.radioButtonRural);
        radioButtonUrban = view.findViewById(R.id.radioButtonUrban);
        radioButtonSemiUrban = view.findViewById(R.id.radioButtonSemiUrban);
        radioButtonMetro = view.findViewById(R.id.radioButtonMetro);
        txtBCAOutletAddressLbl = view.findViewById(R.id.txtBCAOutletAddressLbl);
        editTextBCAOutletAddress = view.findViewById(R.id.editTextBCAOutletAddress);
        txtStateLbl = view.findViewById(R.id.txtStateLbl);
        spinnerState = view.findViewById(R.id.spinnerState);
        txtDivisionLbl = view.findViewById(R.id.txtDivisionLbl);
        spinnerDivision = view.findViewById(R.id.spinnerDivision);
        txtDistrictLbl = view.findViewById(R.id.txtDistrictLbl);
        spinnerDistrict = view.findViewById(R.id.spinnerDistrict);
        txtTehsilLbl = view.findViewById(R.id.txtTehsilLbl);
        spinnerTehsil = view.findViewById(R.id.spinnerTehsil);
        txtVTCLbl = view.findViewById(R.id.txtVTCLbl);
        spinnerVTC = view.findViewById(R.id.spinnerVTC);
        txtLocalityLbl = view.findViewById(R.id.txtLocalityLbl);
        editTextLocality = view.findViewById(R.id.editTextLocality);
        txtPincodeLbl = view.findViewById(R.id.txtPincodeLbl);
        editTextPincode = view.findViewById(R.id.editTextPincode);
        layoutBCAOutletInfo = view.findViewById(R.id.layoutBCAOutletInfo);
        txtOutletAddressProofLbl = view.findViewById(R.id.txtOutletAddressProofLbl);
        imgOutletAddressProof = view.findViewById(R.id.imgOutletAddressProof);

        imgOutletAddressProof.setOnClickListener(this);

    }

    public void setCompulsoryMarkLabel() {
        TextView[] txtViewsForCompulsoryMark = {txtLocationTypeLbl, txtStateLbl, txtDivisionLbl, txtDistrictLbl, txtTehsilLbl,
                txtVTCLbl, txtBCAOutletAddressLbl, txtPincodeLbl, txtOutletAddressProofLbl};
        GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);
    }

    @Override
    public void onClick(View v) {
        int Id = v.getId();

        if (Id == R.id.imgOutletAddressProof) {
            FROM = OUTLET_ADDRESS_COPY;
            SEL_FILE_TYPE = " images";
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

        if (IsDrawable)
            Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(imgOutletAddressProof);
        else {
            try {
                bitmap = ImageUtils.rotateImageIfRequired(getActivity().getContentResolver(), bitmap, picUri);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Glide.with(context).asBitmap().load(bitmap).into(imgOutletAddressProof);
        }

        preBCALocationDetailsDto.setOutletAddProofBase64(base64);
        preBCALocationDetailsDto.setOutletAddProofExt(ext);
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

    public int IsLocationDetailsValidated() {

       /* //Location Type
        if (TextUtils.isEmpty(preBCALocationDetailsDto.getLocationType())) {
            showMessage("Please select Location Type.");
            return 1;
        }

        //State
        if (TextUtils.isEmpty(preBCALocationDetailsDto.getState()) || preBCALocationDetailsDto.getState().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select State.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerState, "Please select State.", context);
            return 1;
        }

        //Division
        if (TextUtils.isEmpty(preBCALocationDetailsDto.getDivision()) || preBCALocationDetailsDto.getDivision().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Division.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerState, "Please select Division.", context);
            return 1;
        }

        //District
        if (TextUtils.isEmpty(preBCALocationDetailsDto.getDistrict()) || preBCALocationDetailsDto.getDistrict().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select District.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerDistrict, "Please select District.", context);
            return 1;
        }

        //Block
        if (TextUtils.isEmpty(preBCALocationDetailsDto.getBlock()) || preBCALocationDetailsDto.getBlock().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Tehsil/Block.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerTehsil, "Please select Tehsil/Block.", context);
            return 1;
        }

        //VTC
        if (TextUtils.isEmpty(preBCALocationDetailsDto.getVtc()) || preBCALocationDetailsDto.getVtc().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select VTC.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerVTC, "Please select VTC.", context);
            return 1;
        }

         //Pin code
        preBCALocationDetailsDto.setPincode(editTextPincode.getText().toString().trim());
        if (TextUtils.isEmpty(preBCALocationDetailsDto.getPincode())) {
            Toast.makeText(context, "Please enter Pin code.", Toast.LENGTH_LONG).show();
            editTextPincode.setError("Please enter Pin code.");
            return 1;
        }

        //BCA Outlet Address
        preBCALocationDetailsDto.setBcaOutletAddress(editTextBCAOutletAddress.getText().toString().trim());
        if (TextUtils.isEmpty(preBCALocationDetailsDto.getBcaOutletAddress())) {
            Toast.makeText(context, "Please enter BCA Outlet Address.", Toast.LENGTH_LONG).show();
            editTextBCAOutletAddress.setError("Please enter BCA Outlet Address.");
            return 1;
        }

         //Outlet Address Copy
        if (TextUtils.isEmpty(preBCALocationDetailsDto.getOutletAddProofId()) || preBCALocationDetailsDto.getOutletAddProofId().equalsIgnoreCase("0")) {
            if (TextUtils.isEmpty(preBCALocationDetailsDto.getOutletAddProofBase64())) {
                showMessage("Please add Outlet Address Copy.");
                return 1;
            }
        }
        */

        return 0;
    }

    class GetAllSpinnerData extends AsyncTask<String, Void, String> {

        private String stateId;
        private String divisionId;
        private String districtId;
        private String blockId;
        private String vtcId;
        private String type;
        private String bankId;

        public GetAllSpinnerData(String stateId, String divisionId, String districtId, String blockId, String vtcId, String bankId) {
            this.stateId = stateId;
            this.divisionId = divisionId;
            this.districtId = districtId;
            this.blockId = blockId;
            this.vtcId = vtcId;
            this.bankId = bankId;
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

                case "STATE":
                    stateList = bcaEntryDetailsRepo.getStateList();
                    break;

                case "DIVISION_DISTRICT":
                    divisionList = bcaEntryDetailsRepo.getDivisionList(stateId);
                    districtList = bcaEntryDetailsRepo.getDistrictList(stateId);
                    break;

                case "DIVISION":
                    divisionList = bcaEntryDetailsRepo.getDivisionList(stateId);
                    break;

                case "DISTRICT":
                    districtList = bcaEntryDetailsRepo.getDistrictList(stateId);
                    break;

                case "BLOCK":
                    blockList = bcaEntryDetailsRepo.getBlockList(stateId, districtId);
                    break;

                case "VTC":
                    vtcList = bcaEntryDetailsRepo.getVTCList(stateId, districtId, blockId);
                    break;

                case "WARD":
                    wardList = bcaEntryDetailsRepo.getWardList(vtcId);
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
                    bindSpinner();
                    break;

                case "DIVISION_DISTRICT":
                    setDivisionSpinnerAdapter(divisionList, spinnerDivision, preBCALocationDetailsDto.getDivision());
                    setDistrictSpinnerAdapter(districtList, spinnerDistrict, preBCALocationDetailsDto.getDistrict());
                    break;

                case "DIVISION":
                    setDivisionSpinnerAdapter(divisionList, spinnerDivision, preBCALocationDetailsDto.getDivision());
                    break;

                case "DISTRICT":
                    setDistrictSpinnerAdapter(districtList, spinnerDistrict, preBCALocationDetailsDto.getDistrict());
                    break;

                case "BLOCK":
                    setBlockSpinnerAdapter(blockList, spinnerTehsil, preBCALocationDetailsDto.getBlock());
                    break;

                case "VTC":

                    setVTCSpinnerAdapter(vtcList, spinnerVTC, preBCALocationDetailsDto.getVtc());
                    break;

               /* case "WARD":
                    setWardSpinnerAdapter(wardList, spinnerWard, preBCALocationDetailsDto.getWard());
                    break;*/


                default:
                    break;
            }

            //Enable/disable views
            GUIUtils.setViewAndChildrenEnabled(layoutBCAOutletInfo, IsEditable);

        }
    }

    public PreBCALocationDetailsDto getPreBCALocationDetailsDto() {
        preBCALocationDetailsDto.setBcaOutletAddress(editTextBCAOutletAddress.getText().toString());
        preBCALocationDetailsDto.setLocality(editTextLocality.getText().toString());
        preBCALocationDetailsDto.setPincode(editTextPincode.getText().toString());
        return preBCALocationDetailsDto;
    }

    public void reloadData(String data, boolean IsEditable) {
        //Reload Data
        if (TextUtils.isEmpty(data))
            preBCALocationDetailsDto = new PreBCALocationDetailsDto();
        else {
            try {
                Gson gson = new GsonBuilder().create();
                preBCALocationDetailsDto = gson.fromJson(data, PreBCALocationDetailsDto.class);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.IsEditable = IsEditable;
        getAllSpinnerData = new GetAllSpinnerData(null, null, null, null, null, null);
        getAllSpinnerData.execute("STATE");
    }

    private void bindSpinner() {

        spinner_focusablemode(spinnerState);
        spinner_focusablemode(spinnerDivision);
        spinner_focusablemode(spinnerDistrict);
        spinner_focusablemode(spinnerTehsil);
        spinner_focusablemode(spinnerVTC);

        //Location Type
        radioGroupLocationType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonRural) {
                    preBCALocationDetailsDto.setLocationType("R");
                } else if (checkedId == R.id.radioButtonUrban) {
                    preBCALocationDetailsDto.setLocationType("U");
                } else if (checkedId == R.id.radioButtonSemiUrban) {
                    preBCALocationDetailsDto.setLocationType("S");
                } else if (checkedId == R.id.radioButtonMetro) {
                    preBCALocationDetailsDto.setLocationType("M");
                }
            }
        });

        if (!TextUtils.isEmpty(preBCALocationDetailsDto.getLocationType())) {
            switch (preBCALocationDetailsDto.getLocationType().toUpperCase()) {
                case "R":
                    radioGroupLocationType.check(R.id.radioButtonRural);
                    break;

                case "U":
                    radioGroupLocationType.check(R.id.radioButtonUrban);
                    break;

                case "S":
                    radioGroupLocationType.check(R.id.radioButtonSemiUrban);
                    break;

                case "M":
                    radioGroupLocationType.check(R.id.radioButtonMetro);
                    break;

                default:
                    break;
            }
        }

        //State
        setStateSpinnerAdapter(stateList, spinnerState, preBCALocationDetailsDto.getState());

        //Locality
        editTextLocality.setText(preBCALocationDetailsDto.getLocality());

        //Pin Code
        editTextPincode.setText(preBCALocationDetailsDto.getPincode());

        //BCA Outlet Address
        editTextBCAOutletAddress.setText(preBCALocationDetailsDto.getBcaOutletAddress());

        //Outlet Address Proof Copy
        if (!TextUtils.isEmpty(preBCALocationDetailsDto.getOutletAddProofId())) {
            String gstUrl = Constants.DownloadImageUrl + preBCALocationDetailsDto.getOutletAddProofId();
            Glide.with(context)
                    .load(gstUrl)
                    .apply(new RequestOptions()
                            .error(R.drawable.ic_camera_alt_black_72dp)
                            .placeholder(R.drawable.ic_camera_alt_black_72dp)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(imgOutletAddressProof);
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getAllSpinnerData != null && !getAllSpinnerData.isCancelled()) {
            getAllSpinnerData.cancel(true);
        }
    }

    private void spinner_focusablemode(CustomSearchableSpinner spinner) {
        spinner.setFocusable(true);
        spinner.setFocusableInTouchMode(true);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int Id = parent.getId();

        if (Id == R.id.spinnerState) {
            if (position > 0) {
                spinnerState.setTitle("Select State");
                spinnerState.requestFocus();
                CustomFranchiseeApplicationSpinnerDto stateDto = (CustomFranchiseeApplicationSpinnerDto) spinnerState.getItemAtPosition(position);
                if (!stateDto.getId().equals("0")) {
                    preBCALocationDetailsDto.setState(stateDto.getId());

                    //Get Division-District
                    getAllSpinnerData = new GetAllSpinnerData(preBCALocationDetailsDto.getState(), null, null, null, null, null);
                    getAllSpinnerData.execute("DIVISION_DISTRICT");
                }
            } else {
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

                //DIVISION
                spinnerDivision.setEnabled(true);
                setDivisionSpinnerAdapter(list1, spinnerDivision, null);
                preBCALocationDetailsDto.setDivision(null);

                //DISTRICT
                spinnerDistrict.setEnabled(true);
                setDistrictSpinnerAdapter(list1, spinnerDistrict, null);
                preBCALocationDetailsDto.setDistrict(null);

                //BLOCK
                spinnerTehsil.setEnabled(true);
                setBlockSpinnerAdapter(list1, spinnerTehsil, null);
                preBCALocationDetailsDto.setBlock(null);

                //VTC
                spinnerVTC.setEnabled(true);
                setVTCSpinnerAdapter(list1, spinnerVTC, null);
                preBCALocationDetailsDto.setVtc(null);

               /* //WARD
                spinnerWard.setEnabled(true);
                setWardSpinnerAdapter(list1, spinnerWard, null);
                bcaOutletInformationDto.setWard(null);*/
            }
        } else if (Id == R.id.spinnerDivision) {
            if (position > 0) {
                spinnerDivision.setTitle("Select Division");
                spinnerDivision.requestFocus();
                CustomFranchiseeApplicationSpinnerDto divDto = (CustomFranchiseeApplicationSpinnerDto) spinnerDivision.getItemAtPosition(position);
                if (!divDto.getId().equals("0")) {
                    preBCALocationDetailsDto.setDivision(divDto.getId());

                }
            } else {
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

                //BLOCK
                spinnerTehsil.setEnabled(true);
                setBlockSpinnerAdapter(list1, spinnerTehsil, null);
                preBCALocationDetailsDto.setBlock(null);

                //VTC
                spinnerVTC.setEnabled(true);
                setVTCSpinnerAdapter(list1, spinnerVTC, null);
                preBCALocationDetailsDto.setVtc(null);

              /*  //WARD
                spinnerWard.setEnabled(true);
                setWardSpinnerAdapter(list1, spinnerWard, null);
                bcaOutletInformationDto.setWard(null);*/
            }
        } else if (Id == R.id.spinnerDistrict) {
            if (position > 0) {
                spinnerDistrict.setTitle("Select District");
                spinnerDistrict.requestFocus();
                CustomFranchiseeApplicationSpinnerDto disDto = (CustomFranchiseeApplicationSpinnerDto) spinnerDistrict.getItemAtPosition(position);
                if (!disDto.getId().equals("0")) {
                    preBCALocationDetailsDto.setDistrict(disDto.getId());

                    //Get BLOCK
                    getAllSpinnerData = new GetAllSpinnerData(preBCALocationDetailsDto.getState(), preBCALocationDetailsDto.getDivision(), preBCALocationDetailsDto.getDistrict(), null, null, null);
                    getAllSpinnerData.execute("BLOCK");

                }
            } else {
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

                //BLOCK
                spinnerTehsil.setEnabled(true);
                setBlockSpinnerAdapter(list1, spinnerTehsil, null);
                preBCALocationDetailsDto.setBlock(null);

                //VTC
                spinnerVTC.setEnabled(true);
                setVTCSpinnerAdapter(list1, spinnerVTC, null);
                preBCALocationDetailsDto.setVtc(null);

              /*  //WARD
                spinnerWard.setEnabled(true);
                setWardSpinnerAdapter(list1, spinnerWard, null);
                bcaOutletInformationDto.setWard(null);*/
            }
        } else if (Id == R.id.spinnerTehsil) {
            if (position > 0) {
                spinnerTehsil.setTitle("Select Block");
                spinnerTehsil.requestFocus();
                CustomFranchiseeApplicationSpinnerDto blockDto = (CustomFranchiseeApplicationSpinnerDto) spinnerTehsil.getItemAtPosition(position);
                if (!blockDto.getId().equals("0")) {
                    preBCALocationDetailsDto.setBlock(blockDto.getId());

                    //Get VTC
                    getAllSpinnerData = new GetAllSpinnerData(preBCALocationDetailsDto.getState(), preBCALocationDetailsDto.getDivision(), preBCALocationDetailsDto.getDistrict(), preBCALocationDetailsDto.getBlock(), null, null);
                    getAllSpinnerData.execute("VTC");

                }
            } else {
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

                //VTC
                spinnerVTC.setEnabled(true);
                setVTCSpinnerAdapter(list1, spinnerVTC, null);
                preBCALocationDetailsDto.setVtc(null);

              /*  //WARD
                spinnerWard.setEnabled(true);
                setWardSpinnerAdapter(list1, spinnerWard, null);
                bcaOutletInformationDto.setWard(null);*/
            }
        } else if (Id == R.id.spinnerVTC) {
            if (position > 0) {

                spinnerVTC.setTitle("Select VTC");
                spinnerVTC.requestFocus();
                CustomFranchiseeApplicationSpinnerDto vtcDto = (CustomFranchiseeApplicationSpinnerDto) spinnerVTC.getItemAtPosition(position);
                if (!vtcDto.getId().equals("0")) {
                    preBCALocationDetailsDto.setVtc(vtcDto.getId());

                    //Get Ward
                    getAllSpinnerData = new GetAllSpinnerData(preBCALocationDetailsDto.getState(), preBCALocationDetailsDto.getDivision(), preBCALocationDetailsDto.getDistrict(), preBCALocationDetailsDto.getBlock(), preBCALocationDetailsDto.getVtc(), null);
                    getAllSpinnerData.execute("WARD");

                }
            } else {
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

               /* //WARD
                spinnerWard.setEnabled(true);
                setWardSpinnerAdapter(list1, spinnerWard, null);
                bcaOutletInformationDto.setWard(null);*/
            }
        } else if (Id == R.id.spinnerWard) {
            /*spinnerWard.setTitle("Select Ward");
            spinnerWard.requestFocus();
            CustomFranchiseeApplicationSpinnerDto wardDto = (CustomFranchiseeApplicationSpinnerDto) spinnerWard.getItemAtPosition(position);
            bcaOutletInformationDto.setWard(wardDto.getId());*/

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //region Set State Adapter
    private void setStateSpinnerAdapter
    (List<CustomFranchiseeApplicationSpinnerDto> stateList, Spinner spinnerState, String
            selStateId) {

        spinnerState.setEnabled(true);
        spinnerState.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, stateList));
        int appsstatePos = bcaEntryDetailsRepo.getSelectedPos(stateList, selStateId);
        spinnerState.setSelection(appsstatePos);
        spinnerState.setOnItemSelectedListener(this);

    }
    //endregion

    //region Set Division Adapter
    private void setDivisionSpinnerAdapter
    (List<CustomFranchiseeApplicationSpinnerDto> divisionList, Spinner spinnerDivision, String selDivisionId) {

        spinnerDivision.setEnabled(true);
        spinnerDivision.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, divisionList));
        int Pos = bcaEntryDetailsRepo.getSelectedPos(divisionList, selDivisionId);
        spinnerDivision.setSelection(Pos);
        spinnerDivision.setOnItemSelectedListener(this);

    }
    //endregion

    //region Set District Adapter
    private void setDistrictSpinnerAdapter
    (List<CustomFranchiseeApplicationSpinnerDto> districtList, Spinner
            spinnerDistrict, String selDistrictId) {

        spinnerDistrict.setEnabled(true);
        spinnerDistrict.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, districtList));
        int appsstatePos = bcaEntryDetailsRepo.getSelectedPos(districtList, selDistrictId);
        spinnerDistrict.setSelection(appsstatePos);
        spinnerDistrict.setOnItemSelectedListener(this);

    }
    //endregion

    //region Set Block Adapter
    private void setBlockSpinnerAdapter
    (List<CustomFranchiseeApplicationSpinnerDto> blockList, Spinner spinner, String
            selBlockId) {

        spinner.setEnabled(true);
        spinner.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, blockList));
        int blockPos = bcaEntryDetailsRepo.getSelectedPos(blockList, selBlockId);
        spinner.setSelection(blockPos);
        spinner.setOnItemSelectedListener(this);

    }
    //endregion

    //region Set VTC Adapter
    private void setVTCSpinnerAdapter(List<CustomFranchiseeApplicationSpinnerDto> VTCList, Spinner spinnerVTC, String selVTCId) {

        spinnerVTC.setEnabled(true);
        spinnerVTC.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, VTCList));
        int appsstatePos = bcaEntryDetailsRepo.getSelectedPos(VTCList, selVTCId);
        spinnerVTC.setSelection(appsstatePos);
        spinnerVTC.setOnItemSelectedListener(this);
    }
    //endregion

    //region Set Ward Adapter
    private void setWardSpinnerAdapter(List<CustomFranchiseeApplicationSpinnerDto> WardList, Spinner spinnerWard, String selWardId) {

        spinnerWard.setEnabled(true);
        spinnerWard.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, WardList));
        int Pos = bcaEntryDetailsRepo.getSelectedPos(WardList, selWardId);
        spinnerWard.setSelection(Pos);
        spinnerWard.setOnItemSelectedListener(this);
    }
    //endregion

    @Override
    public void onPause() {
        super.onPause();
        permissionHandler.dismissSnackbar();
    }

}
