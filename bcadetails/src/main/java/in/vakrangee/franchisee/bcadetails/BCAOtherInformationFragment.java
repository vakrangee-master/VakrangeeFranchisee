package in.vakrangee.franchisee.bcadetails;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import butterknife.ButterKnife;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.nextgenfranchiseeapplication.CustomFranchiseeApplicationSpinnerDto;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.GUIUtils;
import in.vakrangee.supercore.franchisee.utils.Logger;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;
import in.vakrangee.supercore.franchisee.utils.CustomSearchableSpinner;

public class BCAOtherInformationFragment extends BaseFragment implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "BCAOtherInformationFragment";

    private View view;
    private Context context;
    private PermissionHandler permissionHandler;
    private Logger logger;
    private DeprecateHandler deprecateHandler;

    private BCAOtherInformationDto bcaOtherInformationDto;
    private boolean IsEditable = false;
    private GetAllSpinnerData getAllSpinnerData = null;


    private BCAEntryDetailsRepository bcaEntryDetailsRepo;
    private List<CustomFranchiseeApplicationSpinnerDto> supervisorCodeList, deviceTypeList;

    private LinearLayout layoutOtherInformation;
    private TextView txtSupervisorCodeLbl;
    private CustomSearchableSpinner spinnerSupervisorCode;
    private TextView txtSupervisorNameLbl;
    private EditText editTextSupervisorName;
    private TextView txtSupervisorEmailIdLbl;
    private EditText editTextSupervisorEmailID;
    private TextView txtSupervisorMobileNoLbl;
    private EditText editTextSupervisorMobileNo;
    private TextView txtDeviceTypeLbl;
    private CustomSearchableSpinner spinnerDeviceType;
    private TextView txtDeviceSerialNoLbl;
    private EditText editTextDeviceSerialNo;

    public BCAOtherInformationFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag_bca_other_info, container, false);

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

        //Input filter
        CommonUtils.InputFiletrWithMaxLength(editTextSupervisorEmailID, "~#^|$%&*!", 50);

        //Email Id
        editTextSupervisorEmailID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int len = editTextSupervisorEmailID.getText().toString().trim().length();
                if (len <= 0)
                    return;

                boolean IsMatched = editTextSupervisorEmailID.getText().toString().trim().matches(CommonUtils.emailPattern);
                if (!IsMatched) {
                    editTextSupervisorEmailID.setTextColor(Color.parseColor("#000000"));
                    editTextSupervisorEmailID.setError(getResources().getString(R.string.InvalidEmail));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                if (editTextSupervisorEmailID.getText().toString().contains(" ")) {
                    editTextSupervisorEmailID.setText(editTextSupervisorEmailID.getText().toString().replaceAll(" ", ""));
                    editTextSupervisorEmailID.setSelection(editTextSupervisorEmailID.getText().length());
                }

                boolean IsMatched = editTextSupervisorEmailID.getText().toString().trim().matches(CommonUtils.emailPattern);
                if (IsMatched) {
                    editTextSupervisorEmailID.setTextColor(Color.parseColor("#468847"));
                    editTextSupervisorEmailID.setError(null);
                }
            }
        });

        //Mobile No
        editTextSupervisorMobileNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int len = editTextSupervisorMobileNo.getText().toString().trim().length();
                if (len <= 0)
                    return;

                if (i != 10) {
                    editTextSupervisorMobileNo.setTextColor(Color.parseColor("#000000"));
                    editTextSupervisorMobileNo.setError(getResources().getString(R.string.EnterMob));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                if (editTextSupervisorMobileNo.getText().toString().contains(" ")) {
                    editTextSupervisorMobileNo.setText(editTextSupervisorMobileNo.getText().toString().replaceAll(" ", ""));
                    editTextSupervisorMobileNo.setSelection(editTextSupervisorMobileNo.getText().length());
                }

                if (editTextSupervisorMobileNo.length() >= 10) {
                    editTextSupervisorMobileNo.setTextColor(Color.parseColor("#468847"));
                    editTextSupervisorMobileNo.setError(null);
                }
            }
        });


        return view;
    }

    private void bindViewId(View view) {
        layoutOtherInformation = view.findViewById(R.id.layoutOtherInformation);
        txtSupervisorCodeLbl = view.findViewById(R.id.txtSupervisorCodeLbl);
        spinnerSupervisorCode = view.findViewById(R.id.spinnerSupervisorCode);
        txtSupervisorNameLbl = view.findViewById(R.id.txtSupervisorNameLbl);
        editTextSupervisorName = view.findViewById(R.id.editTextSupervisorName);
        txtSupervisorEmailIdLbl = view.findViewById(R.id.txtSupervisorEmailIdLbl);
        editTextSupervisorEmailID = view.findViewById(R.id.editTextSupervisorEmailID);
        txtSupervisorMobileNoLbl = view.findViewById(R.id.txtSupervisorMobileNoLbl);
        editTextSupervisorMobileNo = view.findViewById(R.id.editTextSupervisorMobileNo);
        txtDeviceTypeLbl = view.findViewById(R.id.txtDeviceTypeLbl);
        spinnerDeviceType = view.findViewById(R.id.spinnerDeviceType);
        txtDeviceSerialNoLbl = view.findViewById(R.id.txtDeviceSerialNoLbl);
        editTextDeviceSerialNo = view.findViewById(R.id.editTextDeviceSerialNo);
    }

    public void setCompulsoryMarkLabel() {
        TextView[] txtViewsForCompulsoryMark = {txtSupervisorCodeLbl, txtSupervisorNameLbl, txtSupervisorMobileNoLbl, txtSupervisorEmailIdLbl, txtDeviceTypeLbl, txtDeviceSerialNoLbl};
        GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);
    }

    public int IsOtherInformationValidated() {

        //STEP 1: Supervisor Code
        if (TextUtils.isEmpty(bcaOtherInformationDto.getSupervisorCode())) {
            Toast.makeText(context, "Please select Supervisor Code.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerSupervisorCode, "Please select Supervisor Code.", context);
            return 1;
        }

        //STEP 2: Supervisor Name
        bcaOtherInformationDto.setSupervisorName(editTextSupervisorName.getText().toString().trim());
        if (TextUtils.isEmpty(bcaOtherInformationDto.getSupervisorName())) {
            Toast.makeText(context, "Please enter Supervisor Name.", Toast.LENGTH_LONG).show();
            editTextSupervisorName.setError("Please enter Supervisor Name.");
            return 1;
        }

        //STEP 3: Supervisor Email ID
        bcaOtherInformationDto.setSupervisorEmailID(editTextSupervisorEmailID.getText().toString().trim());
        if (TextUtils.isEmpty(bcaOtherInformationDto.getSupervisorEmailID()) && !editTextSupervisorEmailID.getText().toString().trim().matches(CommonUtils.emailPattern)) {
            Toast.makeText(context, "Please enter Supervisor Email ID.", Toast.LENGTH_LONG).show();
            editTextSupervisorEmailID.setError("Please enter Supervisor Email ID.");
            return 1;
        }

        //STEP 4: Supervisor Mobile No
        bcaOtherInformationDto.setSupervisorMobileNo(editTextSupervisorMobileNo.getText().toString().trim());
        if (TextUtils.isEmpty(bcaOtherInformationDto.getSupervisorMobileNo())) {
            Toast.makeText(context, "Please enter Supervisor Mobile No.", Toast.LENGTH_LONG).show();
            editTextSupervisorMobileNo.setError("Please enter Supervisor Mobile No.");
            return 1;
        }

        //STEP 5: Device Type
        if (TextUtils.isEmpty(bcaOtherInformationDto.getDeviceType())) {
            Toast.makeText(context, "Please select Device Type.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerDeviceType, "Please select Device Type.", context);
            return 1;
        }

        //STEP 6: Device Serial No
        bcaOtherInformationDto.setDeviceSerialNo(editTextDeviceSerialNo.getText().toString().trim());
        if (TextUtils.isEmpty(bcaOtherInformationDto.getDeviceSerialNo())) {
            Toast.makeText(context, "Please enter Device Serial No.", Toast.LENGTH_LONG).show();
            editTextDeviceSerialNo.setError("Please enter Device Serial No.");
            return 1;
        }

        return 0;
    }

    public BCAOtherInformationDto getBCAOtherInformationDto() {

        bcaOtherInformationDto.setSupervisorName(editTextSupervisorName.getText().toString());
        bcaOtherInformationDto.setSupervisorEmailID(editTextSupervisorEmailID.getText().toString());
        bcaOtherInformationDto.setSupervisorMobileNo(editTextSupervisorMobileNo.getText().toString());
        bcaOtherInformationDto.setDeviceSerialNo(editTextDeviceSerialNo.getText().toString());

        return bcaOtherInformationDto;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int Id = parent.getId();

        if (Id == R.id.spinnerSupervisorCode) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto dto = (CustomFranchiseeApplicationSpinnerDto) spinnerSupervisorCode.getItemAtPosition(position);
                bcaOtherInformationDto.setSupervisorCode(dto.getId());
            }
        } else if (Id == R.id.spinnerDeviceType) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto dto1 = (CustomFranchiseeApplicationSpinnerDto) spinnerDeviceType.getItemAtPosition(position);
                bcaOtherInformationDto.setDeviceType(dto1.getId());
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class GetAllSpinnerData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressSpinner(context);
        }

        @Override
        protected String doInBackground(String... strings) {

            //STEP 1: Supervisor Code
            supervisorCodeList = bcaEntryDetailsRepo.getSupervisorCodeList();

            //STEP 2: Device Type
            deviceTypeList = bcaEntryDetailsRepo.getDeviceTypeList();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissProgressSpinner();
            bindSpinner();
        }
    }

    public void reloadData(String data, boolean IsEditable) {
        //Reload Data
        if (TextUtils.isEmpty(data))
            bcaOtherInformationDto = new BCAOtherInformationDto();
        else {
            try {
                Gson gson = new GsonBuilder().create();
                bcaOtherInformationDto = gson.fromJson(data, BCAOtherInformationDto.class);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.IsEditable = IsEditable;
        getAllSpinnerData = new GetAllSpinnerData();
        getAllSpinnerData.execute("");
    }

    private void bindSpinner() {

        //STEP 1: Supervisor Code
        spinner_focusablemode(spinnerSupervisorCode);
        spinnerSupervisorCode.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, supervisorCodeList));
        int supCodePos = bcaEntryDetailsRepo.getSelectedPos(supervisorCodeList, bcaOtherInformationDto.getSupervisorCode());
        spinnerSupervisorCode.setSelection(supCodePos);
        spinnerSupervisorCode.setOnItemSelectedListener(this);

        //STEP 2: Supervisor Name
        editTextSupervisorName.setText(bcaOtherInformationDto.getSupervisorName());

        //STEP 3: Supervisor Email ID
        editTextSupervisorEmailID.setText(bcaOtherInformationDto.getSupervisorEmailID());

        //STEP 4: Supervisor Mobile No
        editTextSupervisorMobileNo.setText(bcaOtherInformationDto.getSupervisorMobileNo());

        //STEP 5: Device Type
        spinner_focusablemode(spinnerDeviceType);
        spinnerDeviceType.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, deviceTypeList));
        int deviceTypePos = bcaEntryDetailsRepo.getSelectedPos(deviceTypeList, bcaOtherInformationDto.getDeviceType());
        spinnerDeviceType.setSelection(deviceTypePos);
        spinnerDeviceType.setOnItemSelectedListener(this);

        //STEP 6: Device Serial No
        editTextDeviceSerialNo.setText(bcaOtherInformationDto.getDeviceSerialNo());

        //Enable/disable views
        GUIUtils.setViewAndChildrenEnabled(layoutOtherInformation, IsEditable);
    }

    @Override
    public void onPause() {
        super.onPause();
        permissionHandler.dismissSnackbar();
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
}
