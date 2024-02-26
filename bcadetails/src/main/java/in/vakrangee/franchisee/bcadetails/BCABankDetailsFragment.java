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

public class BCABankDetailsFragment extends BaseFragment implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "BCABankDetailsFragment";

    private View view;
    private Context context;
    private PermissionHandler permissionHandler;
    private Logger logger;
    private DeprecateHandler deprecateHandler;
    private BCABankDetailsDto bcaBankDetailsDto;
    private boolean IsEditable = false;
    private GetAllSpinnerData getAllSpinnerData = null;
    private BCAEntryDetailsRepository bcaEntryDetailsRepo;
    private List<CustomFranchiseeApplicationSpinnerDto> bankNameList;
    private TextView txtBankNameLbl;
    private CustomSearchableSpinner spinnerBankName;
    private TextView txtAccountNumberLbl;
    private EditText editTextAccountNumber1;
    private EditText editTextAccountNumber2;
    private TextView txtBranchNameLbl;
    private EditText editTextBranchName;
    private TextView txtBCcustomerIDLbl;
    private EditText editTextBCcustomerID;
    private TextView txtIFSCCodeLbl;
    private EditText editTextIFSCCode1;
    private EditText editTextIFSCCode2;
    private TextView txtSOLIDLbl;
    private EditText editTextSOLID;
    private TextView txtSavingACNoLbl;
    private EditText editTextSavingACNo;
    private EditText editTextSavingACNo2;
    private TextView txtSSALbl;
    private EditText editTextSSA;
    private TextView txtCIFNumberLbl;
    private EditText editTextCIFNumber;
    private TextView txtCityCodeLbl;
    private EditText editTextCityCode;
    private TextView txtBankBranchAddressLbl;
    private EditText editTextBankBranchAddress;
    private TextView txtPhoneNumberLbl;
    private EditText editTextPhoneNumber;
    private LinearLayout layoutBankParent;
    private LinearLayout layoutBCcustomerID;
    private LinearLayout layoutSavingACNo;
    private LinearLayout layoutSavingAcName;
    private LinearLayout layoutSavingIFSCCode;
    private TextView txtSavingIFSCCodeLbl;
    private EditText editTextSavingAcName;
    private EditText editTextSavingIFSCCode1;
    private EditText editTextSavingIFSCCode2;

    public BCABankDetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag_bca_bank_detail, container, false);

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

        CommonUtils.InputFiletrWithMaxLength(editTextBranchName, "~#^|$%&*!", 50);
        CommonUtils.InputFiletrWithMaxLength(editTextIFSCCode1, "~#^|$%&*!", 11);
        CommonUtils.InputFiletrWithMaxLength(editTextIFSCCode2, "~#^|$%&*!", 11);

        TextChangedListener_IFSCCode();
        TextChangedListener_AccountNumber();

        return view;
    }

    private void bindViewId(View view) {
        txtBankNameLbl = view.findViewById(R.id.txtBankNameLbl);
        spinnerBankName = view.findViewById(R.id.spinnerBankName);
        txtAccountNumberLbl = view.findViewById(R.id.txtAccountNumberLbl);
        editTextAccountNumber1 = view.findViewById(R.id.editTextAccountNumber1);
        editTextAccountNumber2 = view.findViewById(R.id.editTextAccountNumber2);
        txtBranchNameLbl = view.findViewById(R.id.txtBranchNameLbl);
        editTextBranchName = view.findViewById(R.id.editTextBranchName);
        txtBCcustomerIDLbl = view.findViewById(R.id.txtBCcustomerIDLbl);
        editTextBCcustomerID = view.findViewById(R.id.editTextBCcustomerID);
        txtIFSCCodeLbl = view.findViewById(R.id.txtIFSCCodeLbl);
        editTextIFSCCode1 = view.findViewById(R.id.editTextIFSCCode1);
        editTextIFSCCode2 = view.findViewById(R.id.editTextIFSCCode2);
        txtSOLIDLbl = view.findViewById(R.id.txtSOLIDLbl);
        editTextSOLID = view.findViewById(R.id.editTextSOLID);
        txtSavingACNoLbl = view.findViewById(R.id.txtSavingACNoLbl);
        editTextSavingACNo = view.findViewById(R.id.editTextSavingACNo1);
        editTextSavingACNo2 = view.findViewById(R.id.editTextSavingACNo2);
        txtSSALbl = view.findViewById(R.id.txtSSALbl);
        editTextSSA = view.findViewById(R.id.editTextSSA);
        txtCIFNumberLbl = view.findViewById(R.id.txtCIFNumberLbl);
        editTextCIFNumber = view.findViewById(R.id.editTextCIFNumber);
        txtCityCodeLbl = view.findViewById(R.id.txtCityCodeLbl);
        editTextCityCode = view.findViewById(R.id.editTextCityCode);
        txtBankBranchAddressLbl = view.findViewById(R.id.txtBankBranchAddressLbl);
        editTextBankBranchAddress = view.findViewById(R.id.editTextBankBranchAddress);
        txtPhoneNumberLbl = view.findViewById(R.id.txtPhoneNumberLbl);
        editTextPhoneNumber = view.findViewById(R.id.editTextPhoneNumber);
        layoutBankParent = view.findViewById(R.id.layoutBankParent);
        layoutBCcustomerID = view.findViewById(R.id.layoutBCcustomerID);
        layoutSavingACNo = view.findViewById(R.id.layoutSavingACNo);
        layoutSavingAcName = view.findViewById(R.id.layoutSavingAcName);
        layoutSavingIFSCCode = view.findViewById(R.id.layoutSavingIFSCCode);
        txtSavingIFSCCodeLbl = view.findViewById(R.id.txtSavingIFSCCodeLbl);
        editTextSavingAcName = view.findViewById(R.id.editTextSavingAcName);
        editTextSavingIFSCCode1 = view.findViewById(R.id.editTextSavingIFSCCode1);
        editTextSavingIFSCCode2 = view.findViewById(R.id.editTextSavingIFSCCode2);
    }

    //region region TextChangedListener_IFSCCode
    private void TextChangedListener_IFSCCode() {
        CommonUtils.EditextListener(editTextIFSCCode1, editTextIFSCCode2, 11, "Please enter a valid IFSC Code.", "IFSCCode");
        CommonUtils.EditextListener(editTextIFSCCode1, editTextIFSCCode2, 11, "Please enter a valid IFSC Code.", "IFSCCode");

        //Saving A/c IFSC Code
        CommonUtils.EditextListener(editTextSavingIFSCCode1, editTextSavingIFSCCode2, 11, "Please enter a valid Saving A/c IFSC Code.", "IFSCCode");
        CommonUtils.EditextListener(editTextSavingIFSCCode1, editTextSavingIFSCCode2, 11, "Please enter a valid Saving A/c IFSC Code.", "IFSCCode");


    }
    //endregion

    //region TextChangedListener_AccountNumber
    private void TextChangedListener_AccountNumber() {
        CommonUtils.EditextListener(editTextAccountNumber1, editTextAccountNumber2, 10, "Account number must be 10 characters long", "AccountNumber");
        CommonUtils.EditextListener(editTextAccountNumber1, editTextAccountNumber2, 10, "Account number must be 10 characters long", "AccountNumber");

        //SAving A/c No
        CommonUtils.EditextListener(editTextSavingACNo, editTextSavingACNo2, 10, "Saving Account number must be 10 characters long", "AccountNumber");
        CommonUtils.EditextListener(editTextSavingACNo, editTextSavingACNo2, 10, "Saving Account number must be 10 characters long", "AccountNumber");

    }
    //endregion

    public void setCompulsoryMarkLabel() {
        TextView[] txtViewsForCompulsoryMark = {txtBankNameLbl, txtAccountNumberLbl, txtBranchNameLbl, txtBCcustomerIDLbl, txtIFSCCodeLbl,
                txtSOLIDLbl, txtSavingACNoLbl, txtCIFNumberLbl, txtSavingIFSCCodeLbl};
        GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);
    }

    public int IsBankInformationValidated() {

        //STEP 1: Bank Name
        if (TextUtils.isEmpty(bcaBankDetailsDto.getBankName())) {
            Toast.makeText(context, "Please select Bank Name.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerBankName, "Please select Bank Name.", context);
            return 1;
        } else {
            int status = bankSelectionValidation(bcaBankDetailsDto.getBankName());
            if (status != 0)
                return 1;
        }

        //STEP 2.1: Account Number
        bcaBankDetailsDto.setBankAccountNo(editTextAccountNumber1.getText().toString().trim());
        if (TextUtils.isEmpty(bcaBankDetailsDto.getBankAccountNo())) {
            Toast.makeText(context, "Please enter Account Number.", Toast.LENGTH_LONG).show();
            editTextAccountNumber1.setError("Please enter Account Number.");
            return 1;
        }

        //STEP 2.2: Confirm Account Number
        if (TextUtils.isEmpty(bcaBankDetailsDto.getBankAccountNo())) {
            Toast.makeText(context, "Please re-enter Account Number.", Toast.LENGTH_LONG).show();
            editTextAccountNumber2.setError("Please re-enter Account Number.");
            return 1;
        }

        //STEP 3: Branch Name
        bcaBankDetailsDto.setBranchName(editTextBranchName.getText().toString().trim());
        if (TextUtils.isEmpty(bcaBankDetailsDto.getBranchName())) {
            Toast.makeText(context, "Please enter Branch Name.", Toast.LENGTH_LONG).show();
            editTextBranchName.setError("Please enter Branch Name.");
            return 1;
        }

        //STEP 5.1: IFSC Code
        bcaBankDetailsDto.setIfscCode(editTextIFSCCode1.getText().toString().trim());
        if (TextUtils.isEmpty(bcaBankDetailsDto.getIfscCode())) {
            Toast.makeText(context, "Please enter IFSC Code.", Toast.LENGTH_LONG).show();
            editTextIFSCCode1.setError("Please enter IFSC Code.");
            return 1;
        }

        //STEP 5.2: Confirm IFSC Code
        if (TextUtils.isEmpty(bcaBankDetailsDto.getIfscCode())) {
            Toast.makeText(context, "Please re-enter IFSC Code.", Toast.LENGTH_LONG).show();
            editTextIFSCCode1.setError("Please re-enter IFSC Code.");
            return 1;
        }

        //STEP 6: SOL ID
        bcaBankDetailsDto.setSolId(editTextSOLID.getText().toString().trim());
        if (TextUtils.isEmpty(bcaBankDetailsDto.getSolId())) {
            Toast.makeText(context, "Please enter SOL ID.", Toast.LENGTH_LONG).show();
            editTextSOLID.setError("Please enter SOL ID.");
            return 1;
        } else if (bcaBankDetailsDto.getSolId().trim().length() < 5) {
            Toast.makeText(context, "Please enter minimum 5 digit SOL ID.", Toast.LENGTH_LONG).show();
            editTextSOLID.setError("Please enter minimum 5 digit SOL ID.");
            return 1;
        }

       /* //STEP 8: SSA
        bcaBankDetailsDto.setSsa(editTextSSA.getText().toString().trim());
        if (TextUtils.isEmpty(bcaBankDetailsDto.getSsa())) {
            Toast.makeText(context, "Please enter SSA.", Toast.LENGTH_LONG).show();
            editTextSSA.setError("Please enter SSA.");
            return 1;
        }*/

        //STEP 9: CIF Number
        bcaBankDetailsDto.setCifNumber(editTextCIFNumber.getText().toString().trim());
        if (TextUtils.isEmpty(bcaBankDetailsDto.getCifNumber())) {
            Toast.makeText(context, "Please enter CIF Number.", Toast.LENGTH_LONG).show();
            editTextCIFNumber.setError("Please enter CIF Number.");
            return 1;
        } else if (bcaBankDetailsDto.getCifNumber().trim().length() < 8) {
            Toast.makeText(context, "Please enter minimum 8 characters in CIF Number.", Toast.LENGTH_LONG).show();
            editTextCIFNumber.setError("Please enter minimum 8 characters in CIF Number.");
            return 1;
        }

       /* //STEP 10: City Code
        bcaBankDetailsDto.setCityCode(editTextCityCode.getText().toString().trim());
        if (TextUtils.isEmpty(bcaBankDetailsDto.getCityCode())) {
            Toast.makeText(context, "Please enter City Code.", Toast.LENGTH_LONG).show();
            editTextCityCode.setError("Please enter City Code.");
            return 1;
        }*/

        //STEP 11: Bank Branch Address
        bcaBankDetailsDto.setBankBranchAddress(editTextBankBranchAddress.getText().toString().trim());
        if (!TextUtils.isEmpty(bcaBankDetailsDto.getBankBranchAddress())) {
            if (bcaBankDetailsDto.getBankBranchAddress().trim().length() < 3) {
                Toast.makeText(context, "Please enter minimum 3 characters in Bank Branch Address.", Toast.LENGTH_LONG).show();
                editTextBankBranchAddress.setError("Please enter minimum 3 characters in Bank Branch Address.");
                return 1;
            }
        }

       /* //STEP 12: Phone Number
        bcaBankDetailsDto.setPhoneNumber(editTextPhoneNumber.getText().toString().trim());
        if (TextUtils.isEmpty(bcaBankDetailsDto.getPhoneNumber())) {
            Toast.makeText(context, "Please enter Phone Number.", Toast.LENGTH_LONG).show();
            editTextPhoneNumber.setError("Please enter Phone Number.");
            return 1;
        }*/

        return 0;
    }

    private int validateBCCBSCustomerId() {

        //STEP 4: BC CBS Customer Id
        bcaBankDetailsDto.setBcCBSCustomerId(editTextBCcustomerID.getText().toString().trim());
        if (TextUtils.isEmpty(bcaBankDetailsDto.getBcCBSCustomerId())) {
            Toast.makeText(context, "Please enter BC CBS Customer ID.", Toast.LENGTH_LONG).show();
            editTextBCcustomerID.setError("Please enter BC CBS Customer ID.");
            return 1;
        } else if (bcaBankDetailsDto.getBcCBSCustomerId().trim().length() < 9) {
            Toast.makeText(context, "Please enter minimum 9 digit BC CBS Customer Id.", Toast.LENGTH_LONG).show();
            editTextBCcustomerID.setError("Please enter minimum 9 digit BC CBS Customer Id.");
            return 1;
        }

        return 0;
    }

    private int validateSavingAcNo() {

        //STEP 7: Saving A/c No
        bcaBankDetailsDto.setSavingACNo(editTextSavingACNo.getText().toString().trim());
        if (TextUtils.isEmpty(bcaBankDetailsDto.getSavingACNo())) {
            Toast.makeText(context, "Please enter Saving A/c No.", Toast.LENGTH_LONG).show();
            editTextSavingACNo.setError("Please enter Saving A/c No.");
            return 1;
        }

        //Confirm Saving A/c No
        if (TextUtils.isEmpty(bcaBankDetailsDto.getSavingACNo())) {
            Toast.makeText(context, "Please re-enter Saving Account Number.", Toast.LENGTH_LONG).show();
            editTextSavingACNo2.setError("Please re-enter Saving Account Number.");
            return 1;
        }

        return 0;
    }

    private int validateSavingAcIFSCCode() {

        bcaBankDetailsDto.setSavingACIfscCode(editTextSavingIFSCCode1.getText().toString().trim());
        if (TextUtils.isEmpty(bcaBankDetailsDto.getSavingACIfscCode())) {
            Toast.makeText(context, "Please enter Saving A/c IFSC Code.", Toast.LENGTH_LONG).show();
            editTextSavingIFSCCode1.setError("Please enter Saving A/c IFSC Code.");
            return 1;
        }

        //STEP 5.2: Confirm IFSC Code
        if (TextUtils.isEmpty(bcaBankDetailsDto.getSavingACIfscCode())) {
            Toast.makeText(context, "Please re-enter Saving A/c IFSC Code.", Toast.LENGTH_LONG).show();
            editTextSavingIFSCCode2.setError("Please re-enter Saving A/c IFSC Code.");
            return 1;
        }

        return 0;
    }

    private int bankSelectionValidation(String bankName) {

        switch (bankName) {

            case BCAConstants.UNION_BANK_OF_INDIA:

                //BC CBS Customer Id
                int status = validateBCCBSCustomerId();
                if (status != 0)
                    return 1;

                //Saving A/c No
                int status1 = validateSavingAcNo();
                if (status1 != 0)
                    return 1;

                break;

            case BCAConstants.CENTRAL_BANK_OF_INDIA:
            case BCAConstants.STATE_BANK_OF_INDIA:
            case BCAConstants.BANK_OF_BARODA:
            case BCAConstants.BANK_OF_INDIA:

                //Saving A/c No
                int status2 = validateSavingAcNo();
                if (status2 != 0)
                    return 1;

                //Saving A/c IFSC Code
                int status3 = validateSavingAcIFSCCode();
                if (status3 != 0)
                    return 1;
                break;

            default:
                break;
        }
        return 0;
    }

    public BCABankDetailsDto getBCABankDetailsDto() {

        bcaBankDetailsDto.setBankAccountNo(editTextAccountNumber1.getText().toString());
        bcaBankDetailsDto.setBranchName(editTextBranchName.getText().toString());
        bcaBankDetailsDto.setBcCBSCustomerId(editTextBCcustomerID.getText().toString());
        bcaBankDetailsDto.setIfscCode(editTextIFSCCode1.getText().toString());
        bcaBankDetailsDto.setSolId(editTextSOLID.getText().toString());
        bcaBankDetailsDto.setSavingACNo(editTextSavingACNo.getText().toString());
        bcaBankDetailsDto.setSavingACHolderName(editTextSavingAcName.getText().toString());
        bcaBankDetailsDto.setSavingACIfscCode(editTextSavingIFSCCode1.getText().toString());
        bcaBankDetailsDto.setSsa(editTextSSA.getText().toString());
        bcaBankDetailsDto.setCifNumber(editTextCIFNumber.getText().toString());
        bcaBankDetailsDto.setCityCode(editTextCityCode.getText().toString());
        bcaBankDetailsDto.setBankBranchAddress(editTextBankBranchAddress.getText().toString());
        bcaBankDetailsDto.setPhoneNumber(editTextPhoneNumber.getText().toString());

        return bcaBankDetailsDto;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int Id = parent.getId();

        if (Id == R.id.spinnerBankName) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto dto = (CustomFranchiseeApplicationSpinnerDto) spinnerBankName.getItemAtPosition(position);
                bcaBankDetailsDto.setBankName(dto.getId());

                displayControlsOnBankChange(dto.getName());
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

            //STEP 1: Bank Name
            bankNameList = bcaEntryDetailsRepo.getBankNameList();

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
            bcaBankDetailsDto = new BCABankDetailsDto();
        else {
            try {
                Gson gson = new GsonBuilder().create();
                bcaBankDetailsDto = gson.fromJson(data, BCABankDetailsDto.class);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.IsEditable = IsEditable;
        getAllSpinnerData = new GetAllSpinnerData();
        getAllSpinnerData.execute("");
    }

    private void bindSpinner() {

        //STEP 1: Bank Name
        spinner_focusablemode(spinnerBankName);
        spinnerBankName.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, bankNameList));
        int bankNamePos = bcaEntryDetailsRepo.getSelectedPos(bankNameList, bcaBankDetailsDto.getBankName());
        spinnerBankName.setSelection(bankNamePos);
        spinnerBankName.setOnItemSelectedListener(this);

        //STEP 2.1: Bank Account Number
        editTextAccountNumber1.setText(bcaBankDetailsDto.getBankAccountNo());

        //STEP 2.2: Confirm Bank Account Number
        editTextAccountNumber2.setText(bcaBankDetailsDto.getBankAccountNo());

        //STEP 3: Branch Name
        editTextBranchName.setText(bcaBankDetailsDto.getBranchName());

        //STEP 4: BC CBS customer ID
        editTextBCcustomerID.setText(bcaBankDetailsDto.getBcCBSCustomerId());

        //STEP 5.1: IFSC Code
        editTextIFSCCode1.setText(bcaBankDetailsDto.getIfscCode());

        //STEP 5.2: Confirm IFSC Code
        editTextIFSCCode2.setText(bcaBankDetailsDto.getIfscCode());

        //STEP 6: SOL Id
        editTextSOLID.setText(bcaBankDetailsDto.getSolId());

        //STEP 7.1: Saving A/c No
        editTextSavingACNo.setText(bcaBankDetailsDto.getSavingACNo());

        //STEP 7.2: Saving A/c Name
        editTextSavingAcName.setText(bcaBankDetailsDto.getSavingACHolderName());

        //STEP 7.3: Saving A/c IFSC Code
        editTextSavingIFSCCode1.setText(bcaBankDetailsDto.getSavingACIfscCode());
        editTextSavingIFSCCode2.setText(bcaBankDetailsDto.getSavingACIfscCode());

        //STEP 8: SSA
        editTextSSA.setText(bcaBankDetailsDto.getSsa());

        //STEP 9: CIF number
        editTextCIFNumber.setText(bcaBankDetailsDto.getCifNumber());

        //STEP 10: City Code
        editTextCityCode.setText(bcaBankDetailsDto.getCityCode());

        //STEP 11: Bank Branch Address
        editTextBankBranchAddress.setText(bcaBankDetailsDto.getBankBranchAddress());

        //STEP 12: Phone Number
        editTextPhoneNumber.setText(bcaBankDetailsDto.getPhoneNumber());

        //Enable/disable views
        GUIUtils.setViewAndChildrenEnabled(layoutBankParent, IsEditable);

    }

    private void spinner_focusablemode(CustomSearchableSpinner spinner) {
        spinner.setFocusable(true);
        spinner.setFocusableInTouchMode(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getAllSpinnerData != null && !getAllSpinnerData.isCancelled()) {
            getAllSpinnerData.cancel(true);
        }
    }

    public void displayControlsOnBankChange(String bankName) {

        if (TextUtils.isEmpty(bankName))
            return;

        //CASES to hide/show controls
        switch (bankName) {

            case BCAConstants.UNION_BANK_OF_INDIA:
                layoutBCcustomerID.setVisibility(View.VISIBLE);
                layoutSavingACNo.setVisibility(View.VISIBLE);
                layoutSavingAcName.setVisibility(View.GONE);
                layoutSavingIFSCCode.setVisibility(View.GONE);
                break;

            case BCAConstants.CENTRAL_BANK_OF_INDIA:
            case BCAConstants.STATE_BANK_OF_INDIA:
            case BCAConstants.BANK_OF_BARODA:
            case BCAConstants.BANK_OF_INDIA:

                layoutBCcustomerID.setVisibility(View.GONE);
                layoutSavingACNo.setVisibility(View.VISIBLE);
                layoutSavingAcName.setVisibility(View.VISIBLE);
                layoutSavingIFSCCode.setVisibility(View.VISIBLE);
                break;

            default:
                layoutBCcustomerID.setVisibility(View.GONE);
                layoutSavingACNo.setVisibility(View.GONE);
                layoutSavingAcName.setVisibility(View.GONE);
                layoutSavingIFSCCode.setVisibility(View.GONE);
                break;
        }
    }
}
