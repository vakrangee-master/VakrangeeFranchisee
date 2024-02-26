package in.vakrangee.franchisee.nextgenfranchiseeapplication;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import butterknife.ButterKnife;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.nextgenfranchiseeapplication.CustomFranchiseeApplicationSpinnerDto;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.FranchiseeApplicationRepository;
import in.vakrangee.supercore.franchisee.utils.GUIUtils;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;

public class FAPReferencesFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Context context;
    private DeprecateHandler deprecateHandler;
    private PermissionHandler permissionHandler;

    private FAPReferenceDto fapReferenceDto;
    private FranchiseeApplicationRepository fapRepo;
    private List<CustomFranchiseeApplicationSpinnerDto> salutationList1st;
    private List<CustomFranchiseeApplicationSpinnerDto> relationshipList1st;
    private List<CustomFranchiseeApplicationSpinnerDto> currentOccupationList1st;
    private GetAllReferencesSpinnerData getAllReferencesSpinnerData = null;
    private boolean IsEditable = false;

    private TextView txtRefrenceHeader1;
    private TextView txtRefrenceHeader2;
    private LinearLayout layout1stRefrenceAddress;
    private LinearLayout layout2ndRefrenceAddress;
    //1st refrence
    private Spinner spinner1stRefrenceSalutation;
    private EditText editText1stRefrenceFirstName;
    private EditText editText1stRefrenceMiddleName;
    private EditText editText1stRefrenceLastName;
    private EditText editText1stRefrenceMobile;
    private EditText editText1stRefrenceEmail;
    private Spinner spinner1stRefrenceRealtionship;
    private Spinner spinner_1stRefrence_Current_Occupation;
    private EditText editText1stRefrenceAddressLine1;
    private EditText editText1stRefrenceAddressLine2;
    private EditText editText1stRefrenceLandmark;
    private EditText editText1stRefrencePincode;

    //2dn refrences
    private Spinner spinner2ndRefrenceSalutation;
    private EditText editText2ndRefrenceFirstName;
    private EditText editText2ndRefrenceMiddleName;
    private EditText editText2ndefrenceLastName;
    private EditText editText2ndRefrenceMobile;
    private EditText editText2ndRefrenceEmail;
    private Spinner spinner2ndRefrenceRealtionship;
    private Spinner spinner_2ndRefrence_Current_Occupation;
    private EditText editText2ndRefrenceAddressLine1;
    private EditText editText2ndRefrenceAddressLine2;
    private EditText editText2ndRefrenceLandmark;
    private EditText editText2ndRefrencePincode;
    //Textview Bind for Compulsory mark for -1st Refrence

    private TextView txt1stRefrenceLbl;
    private TextView txt1stRefrenceMobileLbl;
    private TextView txt1stRefrenceEmailLbl;
    private TextView txt1stRefrenceRealtionshipLbl;
    private TextView txtCurrent_1stRefrence_OccupationLbl;
    private TextView txt1stRefrenceAddressLine1Lbl;
    private TextView txt1stRefrenceLandmarkLbl;
    private TextView txt1stRefrencePincodeLbl;
    //Textview Bind for Compulsory mark -for 2nd refrence
    private TextView txt2ndRefrenceLbl;
    private TextView txt2ndRefrenceMobileLbl;
    private TextView txt2ndRefrenceEmailLbl;
    private TextView txt2ndRefrenceRealtionshipLbl;
    private TextView txtCurrent_2ndRefrence_OccupationLbl;
    private TextView txt2ndRefrenceAddressLine1Lbl;
    private TextView txt2ndRefrenceLandmarkLbl;
    private TextView txt2ndRefrencePincodeLbl;
    private LinearLayout layoutReferencesParent;
    private static final String COLOR_468847 = "#468847";
    private static final String COLOR_000000 = "#000000";
    private static final String SPECIAL_CHARS = "\"~#^|$%&*!'";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_fap_references, container, false);

        bindViewId(rootView);

        //Data
        this.context = getContext();
        deprecateHandler = new DeprecateHandler(context);
        fapRepo = new FranchiseeApplicationRepository(context);
        permissionHandler = new PermissionHandler(getActivity());
        ButterKnife.bind(this, rootView);

        setEditTextInputFilter();

        TextView[] txtViewsForCompulsoryMark = {txt1stRefrenceLbl, txt1stRefrenceMobileLbl, txt1stRefrenceEmailLbl,
                txt1stRefrenceRealtionshipLbl, txtCurrent_1stRefrence_OccupationLbl, txt1stRefrenceAddressLine1Lbl,
                txt1stRefrenceLandmarkLbl, txt1stRefrencePincodeLbl, txt2ndRefrenceLbl, txt2ndRefrenceMobileLbl,
                txt2ndRefrenceEmailLbl, txt2ndRefrenceRealtionshipLbl, txtCurrent_2ndRefrence_OccupationLbl,
                txt2ndRefrenceAddressLine1Lbl, txt2ndRefrenceLandmarkLbl, txt2ndRefrencePincodeLbl
        };
        GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);

        //Reference 1 Email Id
        editText1stRefrenceEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Do Nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int len = editText1stRefrenceEmail.getText().toString().trim().length();
                if (len <= 0)
                    return;

                boolean IsMatched = editText1stRefrenceEmail.getText().toString().trim().matches(CommonUtils.emailPattern);
                if (!IsMatched) {
                    editText1stRefrenceEmail.setTextColor(Color.parseColor(COLOR_000000));
                    editText1stRefrenceEmail.setError(getResources().getString(R.string.InvalidEmail));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editText1stRefrenceEmail.getText().toString().contains(" ")) {
                    editText1stRefrenceEmail.setText(editText1stRefrenceEmail.getText().toString().replaceAll(" ", ""));
                    editText1stRefrenceEmail.setSelection(editText1stRefrenceEmail.getText().length());
                }

                boolean IsMatched = editText1stRefrenceEmail.getText().toString().trim().matches(CommonUtils.emailPattern);
                if (IsMatched) {
                    boolean IsBothEmailMatched = IsBothEmailExactMatch();
                    if (IsBothEmailMatched) {
                        editText1stRefrenceEmail.setTextColor(Color.parseColor(COLOR_000000));
                        editText1stRefrenceEmail.setError("Both References Email Id can not be same.");
                        editText1stRefrenceEmail.setText("");
                        fapReferenceDto.setRef1EmailId("");
                    } else {
                        editText1stRefrenceEmail.setTextColor(Color.parseColor(COLOR_468847));
                        editText1stRefrenceEmail.setError(null);
                    }
                }
            }
        });

        //Reference 1 Mobile No
        editText1stRefrenceMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Do Nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int len = editText1stRefrenceMobile.getText().toString().trim().length();
                if (len <= 0)
                    return;

                if (i != 10) {
                    editText1stRefrenceMobile.setTextColor(Color.parseColor(COLOR_000000));
                    editText1stRefrenceMobile.setError(getResources().getString(R.string.EnterMob));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editText1stRefrenceMobile.getText().toString().contains(" ")) {
                    editText1stRefrenceMobile.setText(editText1stRefrenceMobile.getText().toString().replaceAll(" ", ""));
                    editText1stRefrenceMobile.setSelection(editText1stRefrenceMobile.getText().length());
                }

                if (editText1stRefrenceMobile.length() >= 10) {
                    boolean IsBothMobMatched = IsBothMobileExactMatch();
                    if (IsBothMobMatched) {
                        editText1stRefrenceMobile.setTextColor(Color.parseColor(COLOR_000000));
                        editText1stRefrenceMobile.setError("Both References Mobile Number can not be same.");
                        editText1stRefrenceMobile.setText("");
                        fapReferenceDto.setRef1MobileNo("");
                    } else {
                        editText1stRefrenceMobile.setTextColor(Color.parseColor(COLOR_468847));
                        editText1stRefrenceMobile.setError(null);
                    }
                }
            }
        });

        //Reference 2 Email Id
        editText2ndRefrenceEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Do Nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int len = editText2ndRefrenceEmail.getText().toString().trim().length();
                if (len <= 0)
                    return;

                boolean IsMatched = editText2ndRefrenceEmail.getText().toString().trim().matches(CommonUtils.emailPattern);
                if (!IsMatched) {
                    editText2ndRefrenceEmail.setTextColor(Color.parseColor(COLOR_000000));
                    editText2ndRefrenceEmail.setError(getResources().getString(R.string.InvalidEmail));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editText2ndRefrenceEmail.getText().toString().contains(" ")) {
                    editText2ndRefrenceEmail.setText(editText2ndRefrenceEmail.getText().toString().replaceAll(" ", ""));
                    editText2ndRefrenceEmail.setSelection(editText2ndRefrenceEmail.getText().length());
                }

                boolean IsMatched = editText2ndRefrenceEmail.getText().toString().trim().matches(CommonUtils.emailPattern);
                if (IsMatched) {
                    boolean IsBothEmailMatched = IsBothEmailExactMatch();
                    if (IsBothEmailMatched) {
                        editText2ndRefrenceEmail.setTextColor(Color.parseColor(COLOR_000000));
                        editText2ndRefrenceEmail.setError("Both References Email Id can not be same.");
                        editText2ndRefrenceEmail.setText("");
                        fapReferenceDto.setRef2EmailId("");
                    } else {
                        editText2ndRefrenceEmail.setTextColor(Color.parseColor(COLOR_468847));
                        editText2ndRefrenceEmail.setError(null);
                    }
                }
            }
        });

        //Reference 2 Mobile No
        editText2ndRefrenceMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Do Nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int len = editText2ndRefrenceMobile.getText().toString().trim().length();
                if (len <= 0)
                    return;

                if (i != 10) {
                    editText2ndRefrenceMobile.setTextColor(Color.parseColor(COLOR_000000));
                    editText2ndRefrenceMobile.setError(getResources().getString(R.string.EnterMob));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editText2ndRefrenceMobile.getText().toString().contains(" ")) {
                    editText2ndRefrenceMobile.setText(editText2ndRefrenceMobile.getText().toString().replaceAll(" ", ""));
                    editText2ndRefrenceMobile.setSelection(editText2ndRefrenceMobile.getText().length());
                }

                if (editText2ndRefrenceMobile.length() >= 10) {
                    boolean IsBothMobMatched = IsBothMobileExactMatch();
                    if (IsBothMobMatched) {
                        editText2ndRefrenceMobile.setTextColor(Color.parseColor(COLOR_000000));
                        editText2ndRefrenceMobile.setError("Both References Mobile Number can not be same.");
                        editText2ndRefrenceMobile.setText("");
                        fapReferenceDto.setRef2MobileNo("");
                    } else {
                        editText2ndRefrenceMobile.setTextColor(Color.parseColor(COLOR_468847));
                        editText2ndRefrenceMobile.setError(null);
                    }
                }
            }
        });

        //Reference 1 Pincode
        editText1stRefrencePincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Do Nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int len = editText1stRefrencePincode.getText().toString().trim().length();
                if (len <= 0)
                    return;

                if (i != 6) {
                    editText1stRefrencePincode.setTextColor(Color.parseColor(COLOR_000000));
                    editText1stRefrencePincode.setError(getResources().getString(R.string.EnterPincode));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editText1stRefrencePincode.getText().toString().contains(" ")) {
                    editText1stRefrencePincode.setText(editText1stRefrencePincode.getText().toString().replaceAll(" ", ""));
                    editText1stRefrencePincode.setSelection(editText1stRefrencePincode.getText().length());
                }

                if (editText1stRefrencePincode.length() >= 6) {
                    editText1stRefrencePincode.setTextColor(Color.parseColor(COLOR_468847));
                    editText1stRefrencePincode.setError(null);
                }
            }
        });

        //Reference 2 Pincode
        editText2ndRefrencePincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Do Nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int len = editText2ndRefrencePincode.getText().toString().trim().length();
                if (len <= 0)
                    return;

                if (i != 6) {
                    editText2ndRefrencePincode.setTextColor(Color.parseColor(COLOR_000000));
                    editText2ndRefrencePincode.setError(getResources().getString(R.string.EnterPincode));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editText2ndRefrencePincode.getText().toString().contains(" ")) {
                    editText2ndRefrencePincode.setText(editText2ndRefrencePincode.getText().toString().replaceAll(" ", ""));
                    editText2ndRefrencePincode.setSelection(editText2ndRefrencePincode.getText().length());
                }

                if (editText2ndRefrencePincode.length() >= 6) {
                    editText2ndRefrencePincode.setTextColor(Color.parseColor(COLOR_468847));
                    editText2ndRefrencePincode.setError(null);
                }
            }
        });

        return rootView;
    }

    private void setEditTextInputFilter(){
        CommonUtils.InputFiletrWithMaxLength(editText1stRefrenceFirstName, SPECIAL_CHARS, 50);
        CommonUtils.InputFiletrWithMaxLength(editText1stRefrenceLastName, SPECIAL_CHARS, 50);
        CommonUtils.InputFiletrWithMaxLength(editText1stRefrenceMiddleName, SPECIAL_CHARS, 50);
        CommonUtils.InputFiletrWithMaxLength(editText1stRefrenceEmail, SPECIAL_CHARS, 50);
        CommonUtils.InputFiletrWithMaxLength(editText1stRefrenceAddressLine1, SPECIAL_CHARS, 35);
        CommonUtils.InputFiletrWithMaxLength(editText1stRefrenceAddressLine2, SPECIAL_CHARS, 35);
        CommonUtils.InputFiletrWithMaxLength(editText1stRefrenceLandmark, SPECIAL_CHARS, 35);
        CommonUtils.InputFiletrWithMaxLength(editText2ndRefrenceFirstName, SPECIAL_CHARS, 50);
        CommonUtils.InputFiletrWithMaxLength(editText2ndefrenceLastName, SPECIAL_CHARS, 50);
        CommonUtils.InputFiletrWithMaxLength(editText2ndRefrenceMiddleName, SPECIAL_CHARS, 50);
        CommonUtils.InputFiletrWithMaxLength(editText2ndRefrenceEmail, SPECIAL_CHARS, 50);
        CommonUtils.InputFiletrWithMaxLength(editText2ndRefrenceAddressLine1, SPECIAL_CHARS, 35);
        CommonUtils.InputFiletrWithMaxLength(editText2ndRefrenceAddressLine2, SPECIAL_CHARS, 35);
        CommonUtils.InputFiletrWithMaxLength(editText2ndRefrenceLandmark, SPECIAL_CHARS, 35);
           }

    private void bindViewId(View view) {
        //region References

        txtRefrenceHeader1 = view.findViewById(R.id.txtRefrenceHeader1);
        txtRefrenceHeader2 = view.findViewById(R.id.txtRefrenceHeader2);
        layout1stRefrenceAddress = view.findViewById(R.id.layout1stRefrenceAddress);
        layout2ndRefrenceAddress = view.findViewById(R.id.layout2ndRefrenceAddress);
        //1st refrence
        spinner1stRefrenceSalutation = view.findViewById(R.id.spinner1stRefrenceSalutation);
        editText1stRefrenceFirstName = view.findViewById(R.id.editText1stRefrenceFirstName);
        editText1stRefrenceMiddleName = view.findViewById(R.id.editText1stRefrenceMiddleName);
        editText1stRefrenceLastName = view.findViewById(R.id.editText1stRefrenceLastName);
        editText1stRefrenceMobile = view.findViewById(R.id.editText1stRefrenceMobile);
        editText1stRefrenceEmail = view.findViewById(R.id.editText1stRefrenceEmail);
        spinner1stRefrenceRealtionship = view.findViewById(R.id.spinner1stRefrenceRealtionship);
        spinner_1stRefrence_Current_Occupation = view.findViewById(R.id.spinner_1stRefrence_Current_Occupation);
        editText1stRefrenceAddressLine1 = view.findViewById(R.id.editText1stRefrenceAddressLine1);
        editText1stRefrenceAddressLine2 = view.findViewById(R.id.editText1stRefrenceAddressLine2);
        editText1stRefrenceLandmark = view.findViewById(R.id.editText1stRefrenceLandmark);
        editText1stRefrencePincode = view.findViewById(R.id.editText1stRefrencePincode);

        //2dn refrences
        spinner2ndRefrenceSalutation = view.findViewById(R.id.spinner2ndRefrenceSalutation);
        editText2ndRefrenceFirstName = view.findViewById(R.id.editText2ndRefrenceFirstName);
        editText2ndRefrenceMiddleName = view.findViewById(R.id.editText2ndRefrenceMiddleName);
        editText2ndefrenceLastName = view.findViewById(R.id.editText2ndefrenceLastName);
        editText2ndRefrenceMobile = view.findViewById(R.id.editText2ndRefrenceMobile);
        editText2ndRefrenceEmail = view.findViewById(R.id.editText2ndRefrenceEmail);
        spinner2ndRefrenceRealtionship = view.findViewById(R.id.spinner2ndRefrenceRealtionship);
        spinner_2ndRefrence_Current_Occupation = view.findViewById(R.id.spinner_2ndRefrence_Current_Occupation);
        editText2ndRefrenceAddressLine1 = view.findViewById(R.id.editText2ndRefrenceAddressLine1);
        editText2ndRefrenceAddressLine2 = view.findViewById(R.id.editText2ndRefrenceAddressLine2);
        editText2ndRefrenceLandmark = view.findViewById(R.id.editText2ndRefrenceLandmark);
        editText2ndRefrencePincode = view.findViewById(R.id.editText2ndRefrencePincode);
        //Textview Bind for Compulsory mark for -1st Refrence
        txt1stRefrenceLbl = view.findViewById(R.id.txt1stRefrenceLbl);
        txt1stRefrenceMobileLbl = view.findViewById(R.id.txt1stRefrenceMobileLbl);
        txt1stRefrenceEmailLbl = view.findViewById(R.id.txt1stRefrenceEmailLbl);
        txt1stRefrenceRealtionshipLbl = view.findViewById(R.id.txt1stRefrenceRealtionshipLbl);
        txtCurrent_1stRefrence_OccupationLbl = view.findViewById(R.id.txtCurrent_1stRefrence_OccupationLbl);
        txt1stRefrenceAddressLine1Lbl = view.findViewById(R.id.txt1stRefrenceAddressLine1Lbl);
        txt1stRefrenceLandmarkLbl = view.findViewById(R.id.txt1stRefrenceLandmarkLbl);
        txt1stRefrencePincodeLbl = view.findViewById(R.id.txt1stRefrencePincodeLbl);
        //Textview Bind for Compulsory mark -for 2nd refrence
        txt2ndRefrenceLbl = view.findViewById(R.id.txt2ndRefrenceLbl);
        txt2ndRefrenceMobileLbl = view.findViewById(R.id.txt2ndRefrenceMobileLbl);
        txt2ndRefrenceEmailLbl = view.findViewById(R.id.txt2ndRefrenceEmailLbl);
        txt2ndRefrenceRealtionshipLbl = view.findViewById(R.id.txt2ndRefrenceRealtionshipLbl);
        txtCurrent_2ndRefrence_OccupationLbl = view.findViewById(R.id.txtCurrent_2ndRefrence_OccupationLbl);
        txt2ndRefrenceAddressLine1Lbl = view.findViewById(R.id.txt2ndRefrenceAddressLine1Lbl);
        txt2ndRefrenceLandmarkLbl = view.findViewById(R.id.txt2ndRefrenceLandmarkLbl);
        txt2ndRefrencePincodeLbl = view.findViewById(R.id.txt2ndRefrencePincodeLbl);
        layoutReferencesParent = view.findViewById(R.id.layoutReferencesParent);

        txtRefrenceHeader1.setOnClickListener(this);
        txtRefrenceHeader2.setOnClickListener(this);
        //  @OnClick({R.id.txtRefrenceHeader1, R.id.txtRefrenceHeader2})

    }

    //region bind Spinner (set data in spinner view)
    private void bindSpinner() {

        //STEP 1: 1st Applicant Salutation
        CustomFranchiseeApplicationSpinnerAdapter salutationAdapter = new CustomFranchiseeApplicationSpinnerAdapter(context, salutationList1st);
        spinner1stRefrenceSalutation.setAdapter(salutationAdapter);
        int appSalutationPos = fapRepo.getSelectedPos(salutationList1st, fapReferenceDto.getRef1ApplicantSalutation());
        spinner1stRefrenceSalutation.setSelection(appSalutationPos);
        spinner1stRefrenceSalutation.setOnItemSelectedListener(this);

        //STEP 2: 1st Relationship
        CustomFranchiseeApplicationSpinnerAdapter realtionshipAdapter = new CustomFranchiseeApplicationSpinnerAdapter(context, relationshipList1st);
        spinner1stRefrenceRealtionship.setAdapter(realtionshipAdapter);
        int apprelationshipPos = fapRepo.getSelectedPos(relationshipList1st, fapReferenceDto.getRef1RelationShip());
        spinner1stRefrenceRealtionship.setSelection(apprelationshipPos);
        spinner1stRefrenceRealtionship.setOnItemSelectedListener(this);

        //STEP 3:1st Current Occupation
        CustomFranchiseeApplicationSpinnerAdapter CurrentOccupationAdapter = new CustomFranchiseeApplicationSpinnerAdapter(context, currentOccupationList1st);
        spinner_1stRefrence_Current_Occupation.setAdapter(CurrentOccupationAdapter);
        int appCurrentOccupationPos = fapRepo.getSelectedPos(currentOccupationList1st, fapReferenceDto.getRef1CurrentOccupation());
        spinner_1stRefrence_Current_Occupation.setSelection(appCurrentOccupationPos);
        spinner_1stRefrence_Current_Occupation.setOnItemSelectedListener(this);

        //STEP 4: 2nd Applicant Salutation
        CustomFranchiseeApplicationSpinnerAdapter salutationAdapter2nd = new CustomFranchiseeApplicationSpinnerAdapter(context, salutationList1st);
        spinner2ndRefrenceSalutation.setAdapter(salutationAdapter2nd);
        int appSalutation2ndPos = fapRepo.getSelectedPos(salutationList1st, fapReferenceDto.getRef2ApplicantSalutation());
        spinner2ndRefrenceSalutation.setSelection(appSalutation2ndPos);
        spinner2ndRefrenceSalutation.setOnItemSelectedListener(this);

        //STEP 5: 2nd Relationship
        CustomFranchiseeApplicationSpinnerAdapter realtionship2ndAdapter = new CustomFranchiseeApplicationSpinnerAdapter(context, relationshipList1st);
        spinner2ndRefrenceRealtionship.setAdapter(realtionship2ndAdapter);
        int apprelationship2ndPos = fapRepo.getSelectedPos(relationshipList1st, fapReferenceDto.getRef2RelationShip());
        spinner2ndRefrenceRealtionship.setSelection(apprelationship2ndPos);
        spinner2ndRefrenceRealtionship.setOnItemSelectedListener(this);

        //STEP 6:2nd Current Occupation
        CustomFranchiseeApplicationSpinnerAdapter CurrentOccupation2ndAdapter = new CustomFranchiseeApplicationSpinnerAdapter(context, currentOccupationList1st);
        spinner_2ndRefrence_Current_Occupation.setAdapter(CurrentOccupation2ndAdapter);
        int appCurrentOccupation2ndPos = fapRepo.getSelectedPos(currentOccupationList1st, fapReferenceDto.getRef2CurrentOccupation());
        spinner_2ndRefrence_Current_Occupation.setSelection(appCurrentOccupation2ndPos);
        spinner_2ndRefrence_Current_Occupation.setOnItemSelectedListener(this);

        //STEP 7 :1st Ref  First Name bind
        editText1stRefrenceFirstName.setText(fapReferenceDto.getRef1ApplicantFirstName());
        //STEP 8 :1st Ref  Middle Name bind
        editText1stRefrenceMiddleName.setText(fapReferenceDto.getRef1ApplicantMiddleName());
        //STEP 9 :1st Ref  last Name bind
        editText1stRefrenceLastName.setText(fapReferenceDto.getRef1ApplicantLastName());
        //STEP 10 :1st Ref Mobile Number bind
        editText1stRefrenceMobile.setText(fapReferenceDto.getRef1MobileNo());
        //STEP 11 :1st Ref Email Id bind
        editText1stRefrenceEmail.setText(fapReferenceDto.getRef1EmailId());
        //STEP 12 :1st Ref Address line1 bind
        editText1stRefrenceAddressLine1.setText(fapReferenceDto.getRef1AddressLine1());
        //STEP 13 :1st Ref Address line2 bind
        editText1stRefrenceAddressLine2.setText(fapReferenceDto.getRef1AddressLine2());
        //STEP 14 :1st Ref land Mark bind
        editText1stRefrenceLandmark.setText(fapReferenceDto.getRef1LandMark());
        //STEP 15 :1st Ref Pincode bind
        editText1stRefrencePincode.setText(fapReferenceDto.getRef1PinCode());

        //STEP 16 :2nd Ref  First Name bind
        editText2ndRefrenceFirstName.setText(fapReferenceDto.getRef2ApplicantFirstName());
        //STEP 17 :2nd Ref  Middel Name bind
        editText2ndRefrenceMiddleName.setText(fapReferenceDto.getRef2ApplicantMiddleName());
        //STEP 18 :2nd Ref  Last Name bind
        editText2ndefrenceLastName.setText(fapReferenceDto.getRef2ApplicantLastName());
        //STEP 19 :2nd Ref Mobile Number bind
        editText2ndRefrenceMobile.setText(fapReferenceDto.getRef2MobileNo());
        //STEP 20 :2nd Ref Email id bind
        editText2ndRefrenceEmail.setText(fapReferenceDto.getRef2EmailId());
        //STEP 21 :2nd Ref Address Line 1 bind
        editText2ndRefrenceAddressLine1.setText(fapReferenceDto.getRef2AddressLine1());
        //STEP 21 :2nd Ref Address Line 2 bind
        editText2ndRefrenceAddressLine2.setText(fapReferenceDto.getRef2AddressLine2());
        //STEP 21 :2nd Ref Landmark  bind
        editText2ndRefrenceLandmark.setText(fapReferenceDto.getRef2LandMark());
        //STEP 22 :2nd Ref Pincode  bind
        editText2ndRefrencePincode.setText(fapReferenceDto.getRef2PinCode());

        //Enable/disable views
        GUIUtils.setViewAndChildrenEnabled(layoutReferencesParent, IsEditable);

    }
    //endregion

    @Override
    public void onPause() {
        super.onPause();
        permissionHandler.dismissSnackbar();
    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();
        if (Id == R.id.txtRefrenceHeader1) {
            visibleReference1();

        } else if (Id == R.id.txtRefrenceHeader2) {
            visibleReference2();
        }
    }

    public void visibleReference1() {
        txtRefrenceHeader1.setTextColor(deprecateHandler.getColor(R.color.gray));
        txtRefrenceHeader1.setBackgroundColor(deprecateHandler.getColor(R.color.grey));
        txtRefrenceHeader2.setTextColor(deprecateHandler.getColor(R.color.white));
        txtRefrenceHeader2.setBackgroundColor(deprecateHandler.getColor(R.color.gray));

        layout2ndRefrenceAddress.setVisibility(View.GONE);
        layout1stRefrenceAddress.setVisibility(View.VISIBLE);
    }

    public void visibleReference2() {
        txtRefrenceHeader2.setTextColor(deprecateHandler.getColor(R.color.gray));
        txtRefrenceHeader2.setBackgroundColor(deprecateHandler.getColor(R.color.grey));
        txtRefrenceHeader1.setTextColor(deprecateHandler.getColor(R.color.white));
        txtRefrenceHeader1.setBackgroundColor(deprecateHandler.getColor(R.color.gray));

        layout2ndRefrenceAddress.setVisibility(View.VISIBLE);
        layout1stRefrenceAddress.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getAllReferencesSpinnerData != null && !getAllReferencesSpinnerData.isCancelled()) {
            getAllReferencesSpinnerData.cancel(true);
        }
    }

    //region IsFranchiseeReferencesValidated
    public int IsFranchiseeReferencesValidated() {
        // Refrenece Line 1
        //1- spinner Name -MR-MRS
        if (TextUtils.isEmpty(fapReferenceDto.getRef1ApplicantSalutation())) {
            Toast.makeText(context, "Please select MR-MRS.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinner1stRefrenceSalutation, "Please select MR-MRS.", context);
            visibleReference1();
            return 1;
        }
        //1- first name
        fapReferenceDto.setRef1ApplicantFirstName(editText1stRefrenceFirstName.getText().toString().trim());
        if (TextUtils.isEmpty(fapReferenceDto.getRef1ApplicantFirstName())) {
            Toast.makeText(context, "Please enter Applicant First Name of Reference 1.", Toast.LENGTH_LONG).show();
            editText1stRefrenceFirstName.setError("Please enter Applicant First Name.");
            visibleReference1();
            return 2;
        }
        //1- middle name
        fapReferenceDto.setRef1ApplicantMiddleName(editText1stRefrenceMiddleName.getText().toString().trim());

        //1- last name
        fapReferenceDto.setRef1ApplicantLastName(editText1stRefrenceLastName.getText().toString().trim());
        if (TextUtils.isEmpty(fapReferenceDto.getRef1ApplicantLastName())) {
            Toast.makeText(context, "Please enter Applicant last Name of Reference 1.", Toast.LENGTH_LONG).show();
            editText1stRefrenceLastName.setError("Please enter Applicant last Name.");
            visibleReference1();
            return 4;
        }
        //1 -mobile number
        fapReferenceDto.setRef1MobileNo(editText1stRefrenceMobile.getText().toString().trim());
        if (TextUtils.isEmpty(fapReferenceDto.getRef1MobileNo())) {
            Toast.makeText(context, "Please enter Mobile Number of Reference 1.", Toast.LENGTH_LONG).show();
            editText1stRefrenceMobile.setError("Please enter Mobile Number.");
            visibleReference1();
            return 5;
        }
        //1- EmailId
        fapReferenceDto.setRef1EmailId(editText1stRefrenceEmail.getText().toString().trim());
        if (TextUtils.isEmpty(fapReferenceDto.getRef1EmailId()) && !editText1stRefrenceEmail.getText().toString().trim().matches(CommonUtils.emailPattern)) {
            Toast.makeText(context, "Please enter Email Id of Reference 1.", Toast.LENGTH_LONG).show();
            editText1stRefrenceEmail.setError("Please enter Email Id.");
            visibleReference1();
            return 6;
        }
        //1-RelationShip
        if (TextUtils.isEmpty(fapReferenceDto.getRef1RelationShip()) || fapReferenceDto.getRef1RelationShip().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select RelationShip of Reference 1.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinner1stRefrenceRealtionship, "Please select RelationShip.", context);
            visibleReference1();
            return 7;
        }
        //1-current Occupdation
        if (TextUtils.isEmpty(fapReferenceDto.getRef1CurrentOccupation()) || fapReferenceDto.getRef1CurrentOccupation().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Current Occupation of Reference 1.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinner_1stRefrence_Current_Occupation, "Please select Current Occupation.", context);
            visibleReference1();
            return 8;
        }
        //1-Address Line 1
        fapReferenceDto.setRef1AddressLine1(editText1stRefrenceAddressLine1.getText().toString().trim());
        if (TextUtils.isEmpty(fapReferenceDto.getRef1AddressLine1())) {
            Toast.makeText(context, "Please enter Address line 1 of Reference 1.", Toast.LENGTH_LONG).show();
            editText1stRefrenceAddressLine1.setError("Please enter Address line 1.");
            visibleReference1();
            return 9;
        }
        //1-Address Line 2
        fapReferenceDto.setRef1LandMark(editText1stRefrenceLandmark.getText().toString().trim());
        if (TextUtils.isEmpty(fapReferenceDto.getRef1LandMark())) {
            Toast.makeText(context, "Please enter LandMark of Reference 1.", Toast.LENGTH_LONG).show();
            editText1stRefrenceLandmark.setError("Please enter LandMark.");
            visibleReference1();
            return 10;
        }
        //1- pincode
        fapReferenceDto.setRef1PinCode(editText1stRefrencePincode.getText().toString().trim());
        if (TextUtils.isEmpty(fapReferenceDto.getRef1PinCode()) && !editText1stRefrencePincode.getText().toString().trim().matches(CommonUtils.pincodePattern)) {
            Toast.makeText(context, "Please enter Pincode of Reference 1.", Toast.LENGTH_LONG).show();
            editText1stRefrencePincode.setError("Please enter Pincode.");
            visibleReference1();
            return 11;
        }


        // reference Line2
        //2- spinner Name -MR-MRS
        if (TextUtils.isEmpty(fapReferenceDto.getRef2ApplicantSalutation())) {
            Toast.makeText(context, "Please select MR-MRS of Reference 2.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinner2ndRefrenceSalutation, "Please select MR-MRS.", context);
            visibleReference2();
            return 12;
        }
        //2- first name
        fapReferenceDto.setRef2ApplicantFirstName(editText2ndRefrenceFirstName.getText().toString().trim());
        if (TextUtils.isEmpty(fapReferenceDto.getRef2ApplicantFirstName())) {
            Toast.makeText(context, "Please enter Applicant First Name of Reference 2.", Toast.LENGTH_LONG).show();
            editText2ndRefrenceFirstName.setError("Please enter Applicant First Name.");
            visibleReference2();
            return 13;
        }
        //2- middle name
        fapReferenceDto.setRef2ApplicantMiddleName(editText2ndRefrenceMiddleName.getText().toString().trim());

        //2- last name
        fapReferenceDto.setRef2ApplicantLastName(editText2ndefrenceLastName.getText().toString().trim());
        if (TextUtils.isEmpty(fapReferenceDto.getRef2ApplicantLastName())) {
            Toast.makeText(context, "Please enter Applicant last Name of Reference 2.", Toast.LENGTH_LONG).show();
            editText2ndefrenceLastName.setError("Please enter Applicant last Name.");
            visibleReference2();
            return 15;
        }
        //2 -mobile number
        fapReferenceDto.setRef2MobileNo(editText2ndRefrenceMobile.getText().toString().trim());
        if (TextUtils.isEmpty(fapReferenceDto.getRef2MobileNo())) {
            Toast.makeText(context, "Please enter Mobile Number of Reference 2.", Toast.LENGTH_LONG).show();
            editText2ndRefrenceMobile.setError("Please enter Mobile Number.");
            visibleReference2();
            return 16;
        }
        //2- EmailId
        fapReferenceDto.setRef2EmailId(editText2ndRefrenceEmail.getText().toString().trim());
        if (TextUtils.isEmpty(fapReferenceDto.getRef2EmailId()) && !editText2ndRefrenceEmail.getText().toString().trim().matches(CommonUtils.emailPattern)) {
            Toast.makeText(context, "Please enter Email Id of Reference 2.", Toast.LENGTH_LONG).show();
            editText2ndRefrenceEmail.setError("Please enter Email Id.");
            visibleReference2();
            return 17;
        }
        //2-RelationShip
        if (TextUtils.isEmpty(fapReferenceDto.getRef2RelationShip()) || fapReferenceDto.getRef2RelationShip().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Reference 2 RelationShip of Reference 2.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinner2ndRefrenceRealtionship, "Please select Reference 2 RelationShip.", context);
            visibleReference2();
            return 18;
        }
        //2-current Occupdation
        if (TextUtils.isEmpty(fapReferenceDto.getRef2CurrentOccupation()) || fapReferenceDto.getRef2CurrentOccupation().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Reference 2 Current Occupation of Reference 2.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinner_2ndRefrence_Current_Occupation, "Please select Reference 2 Current Occupation.", context);
            visibleReference2();
            return 19;
        }
        //2-Address Line 1
        fapReferenceDto.setRef2AddressLine1(editText2ndRefrenceAddressLine1.getText().toString().trim());
        if (TextUtils.isEmpty(fapReferenceDto.getRef2AddressLine1())) {
            Toast.makeText(context, "Please enter Reference 2 Address line 1 of Reference 2.", Toast.LENGTH_LONG).show();
            editText2ndRefrenceAddressLine1.setError("Please enter Reference 2 Address line 1.");
            visibleReference2();
            return 20;
        }
        //2-Address Line 2
        fapReferenceDto.setRef2LandMark(editText2ndRefrenceLandmark.getText().toString().trim());
        if (TextUtils.isEmpty(fapReferenceDto.getRef2LandMark())) {
            Toast.makeText(context, "Please enter Reference 2 LandMark of Reference 2.", Toast.LENGTH_LONG).show();
            editText2ndRefrenceLandmark.setError("Please enter Reference 2 LandMark.");
            visibleReference2();
            return 21;
        }
        //2- pincode
        fapReferenceDto.setRef2PinCode(editText2ndRefrencePincode.getText().toString().trim());
        if (TextUtils.isEmpty(fapReferenceDto.getRef2PinCode()) && !editText2ndRefrencePincode.getText().toString().trim().matches(CommonUtils.pincodePattern)) {
            Toast.makeText(context, "Please enter Reference 2 Pincode of Reference 2.", Toast.LENGTH_LONG).show();
            editText2ndRefrencePincode.setError("Please enter Reference 2 Pincode.");
            visibleReference2();
            return 22;
        }
        return 0;
    }

    //endregion

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        int Id = adapterView.getId();

        if (Id == R.id.spinner1stRefrenceSalutation) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinner1stRefrenceSalutation.getItemAtPosition(position);
                fapReferenceDto.setRef1ApplicantSalutation(entityDto.getId());
            }
        } else if (Id == R.id.spinner1stRefrenceRealtionship) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinner1stRefrenceRealtionship.getItemAtPosition(position);
                fapReferenceDto.setRef1RelationShip(entityDto.getId());
            }
        } else if (Id == R.id.spinner_1stRefrence_Current_Occupation) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinner_1stRefrence_Current_Occupation.getItemAtPosition(position);
                fapReferenceDto.setRef1CurrentOccupation(entityDto.getId());
            }
        } else if (Id == R.id.spinner2ndRefrenceSalutation) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinner2ndRefrenceSalutation.getItemAtPosition(position);
                fapReferenceDto.setRef2ApplicantSalutation(entityDto.getId());
            }
        } else if (Id == R.id.spinner2ndRefrenceRealtionship) {
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinner2ndRefrenceRealtionship.getItemAtPosition(position);
                fapReferenceDto.setRef2RelationShip(entityDto.getId());
            }
        } else if (Id == R.id.spinner_2ndRefrence_Current_Occupation && position >= 0) {
            CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinner_2ndRefrence_Current_Occupation.getItemAtPosition(position);
            fapReferenceDto.setRef2CurrentOccupation(entityDto.getId());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //Do Nothing
    }

    class GetAllReferencesSpinnerData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            //STEP 1: 1st Applicant Salutation
            salutationList1st = fapRepo.getSalutationList();

            //STEP 2: 1st Relationship
            relationshipList1st = fapRepo.getRelationship();

            //STEP 3:1st Current Occupation
            currentOccupationList1st = fapRepo.getCurrentOccupationList();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            bindSpinner();
        }
    }

    public void reloadData(String data, boolean IsEditable) {
        //Reload Data
        if (TextUtils.isEmpty(data))
            fapReferenceDto = new FAPReferenceDto();
        else {
            try {
                Gson gson = new GsonBuilder().create();
                fapReferenceDto = gson.fromJson(data, FAPReferenceDto.class);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.IsEditable = IsEditable;
        getAllReferencesSpinnerData = new GetAllReferencesSpinnerData();
        getAllReferencesSpinnerData.execute("");
    }

    public FAPReferenceDto getFapReferenceDto() {

        fapReferenceDto.setRef1ApplicantFirstName(editText1stRefrenceFirstName.getText().toString());
        fapReferenceDto.setRef1ApplicantMiddleName(editText1stRefrenceMiddleName.getText().toString());
        fapReferenceDto.setRef1ApplicantLastName(editText1stRefrenceLastName.getText().toString());
        fapReferenceDto.setRef1MobileNo(editText1stRefrenceMobile.getText().toString());
        fapReferenceDto.setRef1EmailId(editText1stRefrenceEmail.getText().toString());
        fapReferenceDto.setRef1AddressLine1(editText1stRefrenceAddressLine1.getText().toString());
        fapReferenceDto.setRef1AddressLine2(editText1stRefrenceAddressLine2.getText().toString());
        fapReferenceDto.setRef1LandMark(editText1stRefrenceLandmark.getText().toString());
        fapReferenceDto.setRef1PinCode(editText1stRefrencePincode.getText().toString());

        fapReferenceDto.setRef2ApplicantFirstName(editText2ndRefrenceFirstName.getText().toString());
        fapReferenceDto.setRef2ApplicantMiddleName(editText2ndRefrenceMiddleName.getText().toString());
        fapReferenceDto.setRef2ApplicantLastName(editText2ndefrenceLastName.getText().toString());
        fapReferenceDto.setRef2MobileNo(editText2ndRefrenceMobile.getText().toString());
        fapReferenceDto.setRef2EmailId(editText2ndRefrenceEmail.getText().toString());
        fapReferenceDto.setRef2AddressLine1(editText2ndRefrenceAddressLine1.getText().toString());
        fapReferenceDto.setRef2AddressLine2(editText2ndRefrenceAddressLine2.getText().toString());
        fapReferenceDto.setRef2LandMark(editText2ndRefrenceLandmark.getText().toString());
        fapReferenceDto.setRef2PinCode(editText2ndRefrencePincode.getText().toString());

        return fapReferenceDto;
    }

    private boolean IsBothEmailExactMatch() {

        String email1 = editText1stRefrenceEmail.getText().toString();
        String email2 = editText2ndRefrenceEmail.getText().toString();

        //Email
        if (email1.equalsIgnoreCase(email2))
            return true;

        return false;
    }

    private boolean IsBothMobileExactMatch() {

        String mob1 = editText1stRefrenceMobile.getText().toString();
        String mob2 = editText2ndRefrenceMobile.getText().toString();

        //Mobile No
        if (mob1.equalsIgnoreCase(mob2))
            return true;

        return false;
    }
}
