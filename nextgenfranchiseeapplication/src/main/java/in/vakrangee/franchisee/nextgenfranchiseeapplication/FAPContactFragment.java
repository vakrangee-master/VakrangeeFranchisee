package in.vakrangee.franchisee.nextgenfranchiseeapplication;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import butterknife.ButterKnife;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.GUIUtils;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;

public class FAPContactFragment extends BaseFragment implements View.OnClickListener {

    private Context context;
    private PermissionHandler permissionHandler;
    private FAPContactInfoDto fapContactInfoDto;
    private boolean IsEditable = false;

    private EditText editTextEmailID;
    private EditText editTextAlternateEmailID;
    private EditText editTextMobileNo;
    private EditText editTextAlternateMobileNo;
    private EditText editTextLandlineNo;
    private TextView txtEmailIdLbl;
    private TextView txtMobileNoLbl;
    private LinearLayout layoutContactParent;
    private static final String COLOR_468847 = "#468847";
    private static final String COLOR_000000 = "#000000";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_fap_contact, container, false);

        bindViewId(rootView);
        //Data
        this.context = getContext();
        permissionHandler = new PermissionHandler(getActivity());
        ButterKnife.bind(this, rootView);

        //Input filter
        CommonUtils.InputFiletrWithMaxLength(editTextEmailID, "~#^|$%&*!", 50);
        CommonUtils.InputFiletrWithMaxLength(editTextAlternateEmailID, "~#^|$%&*!", 50);

        //Reload
        fapContactInfoDto = new FAPContactInfoDto();

        //Set Compulsory mark
        TextView[] txtViewsForCompulsoryMark = {txtEmailIdLbl, txtMobileNoLbl};
        GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);

        //Email Id
        setEmailIdListener();

        //Alternate Email Id
        setAlternateEmailIdListener();

        //Mobile No
        setMobileNoListener();

        //Alternate Mobile No
        setAltMobileNoListener();

        //LandLine No
        editTextLandlineNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Do Nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Do Nothing
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String data = editable.toString();

                int length = data.length();
                if (length == 0)
                    return;

                editTextLandlineNo.setTextColor(Color.parseColor(COLOR_000000));

                if (length != 11) {
                    editTextLandlineNo.setError("Please Enter 11 digit Landline Number.");
                    return;
                }

                if (!CommonUtils.isValidLandLine(data)) {
                    editTextLandlineNo.setError("Invalid LandLine Number.");
                    return;
                }

                editTextLandlineNo.setTextColor(Color.parseColor(COLOR_468847));
                editTextLandlineNo.setError(null);
            }
        });

        return rootView;
    }

    private void setEmailIdListener() {
        editTextEmailID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Do Nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int len = editTextEmailID.getText().toString().trim().length();
                if (len <= 0)
                    return;

                boolean IsMatched = editTextEmailID.getText().toString().trim().matches(CommonUtils.emailPattern);
                if (!IsMatched) {
                    editTextEmailID.setTextColor(Color.parseColor(COLOR_000000));
                    editTextEmailID.setError(getResources().getString(R.string.InvalidEmail));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editTextEmailID.getText().toString().contains(" ")) {
                    editTextEmailID.setText(editTextEmailID.getText().toString().replaceAll(" ", ""));
                    editTextEmailID.setSelection(editTextEmailID.getText().length());
                }

                boolean IsMatched = editTextEmailID.getText().toString().trim().matches(CommonUtils.emailPattern);
                if (IsMatched) {
                    editTextEmailID.setTextColor(Color.parseColor(COLOR_468847));
                    editTextEmailID.setError(null);
                }
            }
        });
    }

    private void setAlternateEmailIdListener() {
        editTextAlternateEmailID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Do Nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int len = editTextAlternateEmailID.getText().toString().trim().length();
                if (len <= 0)
                    return;

                boolean IsMatched = editTextAlternateEmailID.getText().toString().trim().matches(CommonUtils.emailPattern);
                if (!IsMatched) {
                    editTextAlternateEmailID.setTextColor(Color.parseColor(COLOR_000000));
                    editTextAlternateEmailID.setError(getResources().getString(R.string.InvalidEmail));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editTextAlternateEmailID.getText().toString().contains(" ")) {
                    editTextAlternateEmailID.setText(editTextAlternateEmailID.getText().toString().replaceAll(" ", ""));
                    editTextAlternateEmailID.setSelection(editTextAlternateEmailID.getText().length());
                }

                boolean IsMatched = editTextAlternateEmailID.getText().toString().trim().matches(CommonUtils.emailPattern);
                if (IsMatched) {
                    editTextAlternateEmailID.setTextColor(Color.parseColor(COLOR_468847));
                    editTextAlternateEmailID.setError(null);
                }
            }
        });
    }

    private void setMobileNoListener() {
        editTextMobileNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Do Nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int len = editTextMobileNo.getText().toString().trim().length();
                if (len <= 0)
                    return;

                if (i != 10) {
                    editTextMobileNo.setTextColor(Color.parseColor(COLOR_000000));
                    editTextMobileNo.setError(getResources().getString(R.string.EnterMob));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editTextMobileNo.getText().toString().contains(" ")) {
                    editTextMobileNo.setText(editTextMobileNo.getText().toString().replaceAll(" ", ""));
                    editTextMobileNo.setSelection(editTextMobileNo.getText().length());
                }

                if (editTextMobileNo.length() >= 10) {
                    editTextMobileNo.setTextColor(Color.parseColor(COLOR_468847));
                    editTextMobileNo.setError(null);
                }
            }
        });
    }

    private void setAltMobileNoListener() {
        editTextAlternateMobileNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Do Nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int len = editTextAlternateMobileNo.getText().toString().trim().length();
                if (len <= 0)
                    return;

                if (i != 10) {
                    editTextAlternateMobileNo.setTextColor(Color.parseColor(COLOR_000000));
                    editTextAlternateMobileNo.setError(getResources().getString(R.string.EnterMob));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editTextAlternateMobileNo.getText().toString().contains(" ")) {
                    editTextAlternateMobileNo.setText(editTextAlternateMobileNo.getText().toString().replaceAll(" ", ""));
                    editTextAlternateMobileNo.setSelection(editTextAlternateMobileNo.getText().length());
                }

                if (editTextAlternateMobileNo.length() >= 10) {
                    editTextAlternateMobileNo.setTextColor(Color.parseColor(COLOR_468847));
                    editTextAlternateMobileNo.setError(null);
                }
            }
        });
    }

    private void bindViewId(View view) {
        //region References
        editTextEmailID = view.findViewById(R.id.editTextEmailID);
        editTextAlternateEmailID = view.findViewById(R.id.editTextAlternateEmailID);
        editTextMobileNo = view.findViewById(R.id.editTextMobileNo);
        editTextAlternateMobileNo = view.findViewById(R.id.editTextAlternateMobileNo);
        editTextLandlineNo = view.findViewById(R.id.editTextLandlineNo);
        txtEmailIdLbl = view.findViewById(R.id.txtEmailIdLbl);
        txtMobileNoLbl = view.findViewById(R.id.txtMobileNoLbl);
        layoutContactParent = view.findViewById(R.id.layoutContactParent);

    }

    @Override
    public void onPause() {
        super.onPause();
        permissionHandler.dismissSnackbar();
    }

    @Override
    public void onClick(View view) {
        //Do Nothing
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public int IsFranchiseeContactValidated() {

        //STEP 1: Email ID
        fapContactInfoDto.setEmailId(editTextEmailID.getText().toString().trim());
        if (TextUtils.isEmpty(fapContactInfoDto.getEmailId()) && !editTextEmailID.getText().toString().trim().matches(CommonUtils.emailPattern)) {
            Toast.makeText(context, "Please enter Email ID.", Toast.LENGTH_LONG).show();
            editTextEmailID.setError("Please enter Email ID.");
            return 1;
        }

        //Alternate Email ID
        fapContactInfoDto.setAlternateEmailId(editTextAlternateEmailID.getText().toString().trim());
        if (!TextUtils.isEmpty(fapContactInfoDto.getAlternateEmailId())) {

            //Check Pattern
            if (!editTextAlternateEmailID.getText().toString().trim().matches(CommonUtils.emailPattern)) {
                Toast.makeText(context, "Please enter Proper Alternate Email ID.", Toast.LENGTH_LONG).show();
                editTextAlternateEmailID.setError("Please enter Proper Alternate Email ID.");
                return 1;
            }

            //Both Same
            if (fapContactInfoDto.getEmailId().equalsIgnoreCase(fapContactInfoDto.getAlternateEmailId())) {
                Toast.makeText(context, "Both Email ID and Alternate Email ID should not be same.", Toast.LENGTH_LONG).show();
                editTextAlternateEmailID.setError("Both Email ID and Alternate Email ID should not be same.");
                return 1;
            }
        }

        //STEP 2: Mobile No
        fapContactInfoDto.setMobileNumber(editTextMobileNo.getText().toString().trim());
        if (TextUtils.isEmpty(fapContactInfoDto.getMobileNumber())) {
            Toast.makeText(context, "Please enter Mobile number.", Toast.LENGTH_LONG).show();
            editTextMobileNo.setError("Please enter Mobile number.");
            return 2;
        }

        //Alternate Mobile No
        fapContactInfoDto.setAlternateMobileNumber(editTextAlternateMobileNo.getText().toString().trim());
        if (!TextUtils.isEmpty(fapContactInfoDto.getAlternateMobileNumber())) {

            //Both Same
            if (fapContactInfoDto.getMobileNumber().equalsIgnoreCase(fapContactInfoDto.getAlternateMobileNumber())) {
                Toast.makeText(context, "Both Mobile No and Alternate Mobile No should not be same.", Toast.LENGTH_LONG).show();
                editTextAlternateEmailID.setError("Both Mobile No and Alternate Mobile No should not be same.");
                return 1;
            }
        }
        return 0;
    }

    public void reloadData(String data, boolean isEdit) {

        this.IsEditable = isEdit;

        //Reload Data
        if (TextUtils.isEmpty(data))
            fapContactInfoDto = new FAPContactInfoDto();
        else {
            try {
                Gson gson = new GsonBuilder().create();
                fapContactInfoDto = gson.fromJson(data, FAPContactInfoDto.class);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //STEP 1: EmailId
        editTextEmailID.setText(fapContactInfoDto.getEmailId());

        //STEP 2: Alt Email Id
        editTextAlternateEmailID.setText(fapContactInfoDto.getAlternateEmailId());

        //STEP 3: Mobile Number
        editTextMobileNo.setText(fapContactInfoDto.getMobileNumber());

        //STEP 4: Alt Mobile Number
        editTextAlternateMobileNo.setText(fapContactInfoDto.getAlternateMobileNumber());

        //STEP 5: Landline Number
        editTextLandlineNo.setText(fapContactInfoDto.getLandlineNumber());

        //Enable/disable views
        GUIUtils.setViewAndChildrenEnabled(layoutContactParent, IsEditable);
    }

    public FAPContactInfoDto getFapContactInfoDto() {

        fapContactInfoDto.setEmailId(editTextEmailID.getText().toString());
        fapContactInfoDto.setAlternateEmailId(editTextAlternateEmailID.getText().toString());
        fapContactInfoDto.setMobileNumber(editTextMobileNo.getText().toString());
        fapContactInfoDto.setAlternateMobileNumber(editTextAlternateMobileNo.getText().toString());
        fapContactInfoDto.setLandlineNumber(editTextLandlineNo.getText().toString());

        return fapContactInfoDto;
    }
}
