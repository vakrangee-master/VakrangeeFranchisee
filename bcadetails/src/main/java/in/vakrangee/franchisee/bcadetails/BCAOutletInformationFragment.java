package in.vakrangee.franchisee.bcadetails;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.nextgenfranchiseeapplication.CustomFranchiseeApplicationSpinnerDto;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.GUIUtils;
import in.vakrangee.supercore.franchisee.utils.Logger;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;
import in.vakrangee.supercore.franchisee.utils.CustomSearchableSpinner;

public class BCAOutletInformationFragment extends BaseFragment implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "BCAOutletInformationFragment";

    private View view;
    private Context context;
    private PermissionHandler permissionHandler;
    private Logger logger;
    private DeprecateHandler deprecateHandler;

    private BCAOutletInformationDto bcaOutletInformationDto;
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
    private TextView txtWardLbl;
    private CustomSearchableSpinner spinnerWard;
    private TextView txtLocalityLbl;
    private EditText editTextLocality;
    private TextView txtPincodeLbl;
    private EditText editTextPincode;
    private LinearLayout layoutBCAOutletInfo;

    public BCAOutletInformationFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag_bca_outlet_info, container, false);

        bindViewId(view);
        //Initialize data
        this.context = getContext();
        logger = Logger.getInstance(context);
        deprecateHandler = new DeprecateHandler(context);
        permissionHandler = new PermissionHandler(getActivity());
        bcaEntryDetailsRepo = new BCAEntryDetailsRepository(context);

        ButterKnife.bind(this, view);

        final Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");

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
        txtWardLbl = view.findViewById(R.id.txtWardLbl);
        spinnerWard = view.findViewById(R.id.spinnerWard);
        txtLocalityLbl = view.findViewById(R.id.txtLocalityLbl);
        editTextLocality = view.findViewById(R.id.editTextLocality);
        txtPincodeLbl = view.findViewById(R.id.txtPincodeLbl);
        editTextPincode = view.findViewById(R.id.editTextPincode);
        layoutBCAOutletInfo = view.findViewById(R.id.layoutBCAOutletInfo);
    }

    public void setCompulsoryMarkLabel() {
        TextView[] txtViewsForCompulsoryMark = {txtLocationTypeLbl, txtStateLbl, txtDivisionLbl, txtDistrictLbl, txtTehsilLbl, txtVTCLbl, txtBCAOutletAddressLbl, txtPincodeLbl};
        GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);
    }

    public int IsOutletInformationValidated() {

        //STEP 1: Location Type
        if (TextUtils.isEmpty(bcaOutletInformationDto.getLocationType())) {
            showMessage("Please select Location Type.");
            return 1;
        }

        //STEP 2: State
        if (TextUtils.isEmpty(bcaOutletInformationDto.getState()) || bcaOutletInformationDto.getState().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select State.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerState, "Please select State.", context);
            return 1;
        }

        //STEP 3: Division
        if (TextUtils.isEmpty(bcaOutletInformationDto.getDivision()) || bcaOutletInformationDto.getDivision().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Division.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerState, "Please select Division.", context);
            return 1;
        }

        //STEP 4: District
        if (TextUtils.isEmpty(bcaOutletInformationDto.getDistrict()) || bcaOutletInformationDto.getDistrict().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select District.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerDistrict, "Please select District.", context);
            return 1;
        }

        //STEP 5: Block
        if (TextUtils.isEmpty(bcaOutletInformationDto.getBlock()) || bcaOutletInformationDto.getBlock().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Tehsil/Block.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerTehsil, "Please select Tehsil/Block.", context);
            return 1;
        }

        //STEP : VTC
        if (TextUtils.isEmpty(bcaOutletInformationDto.getVtc()) || bcaOutletInformationDto.getVtc().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select VTC.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerVTC, "Please select VTC.", context);
            return 1;
        }

        //STEP 7: BCA Outlet Address
        bcaOutletInformationDto.setBcaOutletAddress(editTextBCAOutletAddress.getText().toString().trim());
        if (TextUtils.isEmpty(bcaOutletInformationDto.getBcaOutletAddress())) {
            Toast.makeText(context, "Please enter BCA Outlet Address.", Toast.LENGTH_LONG).show();
            editTextBCAOutletAddress.setError("Please enter BCA Outlet Address.");
            return 1;
        }

        //STEP 8: Pin code
        bcaOutletInformationDto.setPincode(editTextPincode.getText().toString().trim());
        if (TextUtils.isEmpty(bcaOutletInformationDto.getPincode())) {
            Toast.makeText(context, "Please enter Pin code.", Toast.LENGTH_LONG).show();
            editTextPincode.setError("Please enter Pin code.");
            return 1;
        }

        return 0;
    }

    class GetAllSpinnerData extends AsyncTask<String, Void, String> {

        private String stateId;
        private String divisionId;
        private String districtId;
        private String blockId;
        private String vtcId;
        private String type;

        public GetAllSpinnerData(String stateId, String divisionId, String districtId, String blockId, String vtcId) {
            this.stateId = stateId;
            this.divisionId = divisionId;
            this.districtId = districtId;
            this.blockId = blockId;
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
                    setDivisionSpinnerAdapter(divisionList, spinnerDivision, bcaOutletInformationDto.getDivision());
                    setDistrictSpinnerAdapter(districtList, spinnerDistrict, bcaOutletInformationDto.getDistrict());
                    break;

                case "DIVISION":
                    setDivisionSpinnerAdapter(divisionList, spinnerDivision, bcaOutletInformationDto.getDivision());
                    break;

                case "DISTRICT":
                    setDistrictSpinnerAdapter(districtList, spinnerDistrict, bcaOutletInformationDto.getDistrict());
                    break;

                case "BLOCK":
                    setBlockSpinnerAdapter(blockList, spinnerTehsil, bcaOutletInformationDto.getBlock());
                    break;

                case "VTC":
                    setVTCSpinnerAdapter(vtcList, spinnerVTC, bcaOutletInformationDto.getVtc());
                    break;

                case "WARD":
                    setWardSpinnerAdapter(wardList, spinnerWard, bcaOutletInformationDto.getWard());
                    break;

                default:
                    break;
            }

            //Enable/disable views
            GUIUtils.setViewAndChildrenEnabled(layoutBCAOutletInfo, IsEditable);

        }
    }

    public BCAOutletInformationDto getBCAOutletInformationDto() {
        bcaOutletInformationDto.setBcaOutletAddress(editTextBCAOutletAddress.getText().toString());
        bcaOutletInformationDto.setLocality(editTextLocality.getText().toString());
        bcaOutletInformationDto.setPincode(editTextPincode.getText().toString());
        return bcaOutletInformationDto;
    }

    public void reloadData(String data, boolean IsEditable) {
        //Reload Data
        if (TextUtils.isEmpty(data))
            bcaOutletInformationDto = new BCAOutletInformationDto();
        else {
            try {
                Gson gson = new GsonBuilder().create();
                bcaOutletInformationDto = gson.fromJson(data, BCAOutletInformationDto.class);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.IsEditable = IsEditable;
        getAllSpinnerData = new GetAllSpinnerData(null, null, null, null, null);
        getAllSpinnerData.execute("STATE");
    }

    private void bindSpinner() {

        spinner_focusablemode(spinnerState);
        spinner_focusablemode(spinnerDivision);
        spinner_focusablemode(spinnerDistrict);
        spinner_focusablemode(spinnerTehsil);
        spinner_focusablemode(spinnerVTC);
        spinner_focusablemode(spinnerWard);

        //STEP 1: Gender
        radioGroupLocationType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonRural) {
                    bcaOutletInformationDto.setLocationType("R");
                } else if (checkedId == R.id.radioButtonUrban) {
                    bcaOutletInformationDto.setLocationType("U");
                } else if (checkedId == R.id.radioButtonSemiUrban) {
                    bcaOutletInformationDto.setLocationType("S");
                } else if (checkedId == R.id.radioButtonMetro) {
                    bcaOutletInformationDto.setLocationType("M");
                }
            }
        });

        if (!TextUtils.isEmpty(bcaOutletInformationDto.getLocationType())) {
            switch (bcaOutletInformationDto.getLocationType().toUpperCase()) {
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

        //STEP 2: BCA Outlet Address
        editTextBCAOutletAddress.setText(bcaOutletInformationDto.getBcaOutletAddress());

        //STEP 3: State
        setStateSpinnerAdapter(stateList, spinnerState, bcaOutletInformationDto.getState());

        //STEP 4: Locality
        editTextLocality.setText(bcaOutletInformationDto.getLocality());

        //STEP 5: Pin Code
        editTextPincode.setText(bcaOutletInformationDto.getPincode());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getAllSpinnerData != null && !getAllSpinnerData.isCancelled()) {
            getAllSpinnerData.cancel(true);
        }
    }

    private void spinner_focusablemode(CustomSearchableSpinner  spinner) {
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
                    bcaOutletInformationDto.setState(stateDto.getId());

                    //Get Division-District
                    getAllSpinnerData = new GetAllSpinnerData(bcaOutletInformationDto.getState(), null, null, null, null);
                    getAllSpinnerData.execute("DIVISION_DISTRICT");
                }
            } else {
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

                //DIVISION
                spinnerDivision.setEnabled(true);
                setDivisionSpinnerAdapter(list1, spinnerDivision, null);
                bcaOutletInformationDto.setDivision(null);

                //DISTRICT
                spinnerDistrict.setEnabled(true);
                setDistrictSpinnerAdapter(list1, spinnerDistrict, null);
                bcaOutletInformationDto.setDistrict(null);

                //BLOCK
                spinnerTehsil.setEnabled(true);
                setBlockSpinnerAdapter(list1, spinnerTehsil, null);
                bcaOutletInformationDto.setBlock(null);

                //VTC
                spinnerVTC.setEnabled(true);
                setVTCSpinnerAdapter(list1, spinnerVTC, null);
                bcaOutletInformationDto.setVtc(null);

                //WARD
                spinnerWard.setEnabled(true);
                setWardSpinnerAdapter(list1, spinnerWard, null);
                bcaOutletInformationDto.setWard(null);
            }
        } else if (Id == R.id.spinnerDivision) {
            if (position > 0) {
                spinnerDivision.setTitle("Select Division");
                spinnerDivision.requestFocus();
                CustomFranchiseeApplicationSpinnerDto divDto = (CustomFranchiseeApplicationSpinnerDto) spinnerDivision.getItemAtPosition(position);
                if (!divDto.getId().equals("0")) {
                    bcaOutletInformationDto.setDivision(divDto.getId());

                }
            } else {
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

                //BLOCK
                spinnerTehsil.setEnabled(true);
                setBlockSpinnerAdapter(list1, spinnerTehsil, null);
                bcaOutletInformationDto.setBlock(null);

                //VTC
                spinnerVTC.setEnabled(true);
                setVTCSpinnerAdapter(list1, spinnerVTC, null);
                bcaOutletInformationDto.setVtc(null);

                //WARD
                spinnerWard.setEnabled(true);
                setWardSpinnerAdapter(list1, spinnerWard, null);
                bcaOutletInformationDto.setWard(null);
            }
        } else if (Id == R.id.spinnerDistrict) {
            if (position > 0) {
                spinnerDistrict.setTitle("Select District");
                spinnerDistrict.requestFocus();
                CustomFranchiseeApplicationSpinnerDto disDto = (CustomFranchiseeApplicationSpinnerDto) spinnerDistrict.getItemAtPosition(position);
                if (!disDto.getId().equals("0")) {
                    bcaOutletInformationDto.setDistrict(disDto.getId());

                    //Get BLOCK
                    getAllSpinnerData = new GetAllSpinnerData(bcaOutletInformationDto.getState(), bcaOutletInformationDto.getDivision(), bcaOutletInformationDto.getDistrict(), null, null);
                    getAllSpinnerData.execute("BLOCK");

                }
            } else {
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

                //BLOCK
                spinnerTehsil.setEnabled(true);
                setBlockSpinnerAdapter(list1, spinnerTehsil, null);
                bcaOutletInformationDto.setBlock(null);

                //VTC
                spinnerVTC.setEnabled(true);
                setVTCSpinnerAdapter(list1, spinnerVTC, null);
                bcaOutletInformationDto.setVtc(null);

                //WARD
                spinnerWard.setEnabled(true);
                setWardSpinnerAdapter(list1, spinnerWard, null);
                bcaOutletInformationDto.setWard(null);
            }
        } else if (Id == R.id.spinnerTehsil) {
            if (position > 0) {
                spinnerTehsil.setTitle("Select Block");
                spinnerTehsil.requestFocus();
                CustomFranchiseeApplicationSpinnerDto blockDto = (CustomFranchiseeApplicationSpinnerDto) spinnerTehsil.getItemAtPosition(position);
                if (!blockDto.getId().equals("0")) {
                    bcaOutletInformationDto.setBlock(blockDto.getId());

                    //Get VTC
                    getAllSpinnerData = new GetAllSpinnerData(bcaOutletInformationDto.getState(), bcaOutletInformationDto.getDivision(), bcaOutletInformationDto.getDistrict(), bcaOutletInformationDto.getBlock(), null);
                    getAllSpinnerData.execute("VTC");

                }
            } else {
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

                //VTC
                spinnerVTC.setEnabled(true);
                setVTCSpinnerAdapter(list1, spinnerVTC, null);
                bcaOutletInformationDto.setVtc(null);

                //WARD
                spinnerWard.setEnabled(true);
                setWardSpinnerAdapter(list1, spinnerWard, null);
                bcaOutletInformationDto.setWard(null);
            }
        } else if (Id == R.id.spinnerVTC) {
            if (position > 0) {

                spinnerVTC.setTitle("Select VTC");
                spinnerVTC.requestFocus();
                CustomFranchiseeApplicationSpinnerDto vtcDto = (CustomFranchiseeApplicationSpinnerDto) spinnerVTC.getItemAtPosition(position);
                if (!vtcDto.getId().equals("0")) {
                    bcaOutletInformationDto.setVtc(vtcDto.getId());

                    //Get Ward
                    getAllSpinnerData = new GetAllSpinnerData(bcaOutletInformationDto.getState(), bcaOutletInformationDto.getDivision(), bcaOutletInformationDto.getDistrict(), bcaOutletInformationDto.getBlock(), bcaOutletInformationDto.getVtc());
                    getAllSpinnerData.execute("WARD");

                }
            } else {
                List<CustomFranchiseeApplicationSpinnerDto> list1 = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();
                list1.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

                //WARD
                spinnerWard.setEnabled(true);
                setWardSpinnerAdapter(list1, spinnerWard, null);
                bcaOutletInformationDto.setWard(null);
            }
        } else if (Id == R.id.spinnerWard) {
            spinnerWard.setTitle("Select Ward");
            spinnerWard.requestFocus();
            CustomFranchiseeApplicationSpinnerDto wardDto = (CustomFranchiseeApplicationSpinnerDto) spinnerWard.getItemAtPosition(position);
            bcaOutletInformationDto.setWard(wardDto.getId());

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
    private void setVTCSpinnerAdapter
    (List<CustomFranchiseeApplicationSpinnerDto> VTCList, Spinner spinnerVTC, String selVTCId) {

        spinnerVTC.setEnabled(true);
        spinnerVTC.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, VTCList));
        int appsstatePos = bcaEntryDetailsRepo.getSelectedPos(VTCList, selVTCId);
        spinnerVTC.setSelection(appsstatePos);
        spinnerVTC.setOnItemSelectedListener(this);
    }
    //endregion

    //region Set Ward Adapter
    private void setWardSpinnerAdapter
    (List<CustomFranchiseeApplicationSpinnerDto> WardList, Spinner spinnerWard, String selWardId) {

        spinnerWard.setEnabled(true);
        spinnerWard.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, WardList));
        int Pos = bcaEntryDetailsRepo.getSelectedPos(wardList, selWardId);
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
