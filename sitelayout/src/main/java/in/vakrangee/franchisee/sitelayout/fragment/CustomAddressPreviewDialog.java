package in.vakrangee.franchisee.sitelayout.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.StringTokenizer;

import in.vakrangee.franchisee.sitelayout.R;
import in.vakrangee.franchisee.sitelayout.asyntask.AsyncTaskAddressProofDetail;
import in.vakrangee.franchisee.sitelayout.asyntask.AsyncTaskDistricSpinner;
import in.vakrangee.franchisee.sitelayout.asyntask.AsyncTaskIsRural;
import in.vakrangee.franchisee.sitelayout.asyntask.AsyncTaskVillageSpinner;
import in.vakrangee.franchisee.sitelayout.model.AddressProofDetailsDTO;
import in.vakrangee.franchisee.sitelayout.model.District;
import in.vakrangee.franchisee.sitelayout.model.State;
import in.vakrangee.franchisee.sitelayout.model.Village;
import in.vakrangee.supercore.franchisee.model.FranchiseeDetails;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.CustomSearchableSpinner;

public class CustomAddressPreviewDialog extends Dialog implements android.view.View.OnClickListener {
    private Context context;
    private AsyncTaskDistricSpinner asyncTaskDistricSpinner;
    private AsyncTaskVillageSpinner asyncTaskVillageSpinner;
    private AsyncTaskIsRural asyncTaskIsRural;
    private AsyncTaskAddressProofDetail asyncTaskAddressProofDetail;

    public ArrayList<District> arrayListDistrict = new ArrayList<>();
    private ArrayList<Village> arrayListVillage = new ArrayList<>();
    private ArrayList<AddressProofDetailsDTO> arrayListaddressProofDetails = new ArrayList<>();
    private CustomSearchableSpinner stateSpinner, districtSpinner, villageSpinner, AddressTypeSpinner;
    private String selectStateID, selectDistricID, selectVillageID;
    TextView wardNoTextView, addressName;
    EditText SiteVisitWardNo, SiteVisitPincode, SiteVisitAddress1, SiteVisitAddress2, SiteVisitLandmark, edtAddressTypeNumber;
    ScrollView scrollViewNextGenSiteVisit;
    LinearLayout layoutAddressOfLocationVisited, parentLayout, linAddressName;
    ImageView imgAddressProof;
    Button btnCancel;
    Button btnSubmitAddress;
    private Bitmap bitmap = null;
    private FranchiseeDetails franchiseeDetails;
    private AddressPreviewDialogClicks addressPreviewDialogClicks;
    private String AddressTypeSelectID;
    private static final String SPECIAL_CHARS = "~#^|$%&*!";
    private static final String PLEASE_SELECT = "Please Select";

    public CustomAddressPreviewDialog(Context context, FranchiseeDetails franchiseeDetails, AddressPreviewDialogClicks addressPreviewDialogClicks) {
        super(context);
        this.context = context;
        this.franchiseeDetails = franchiseeDetails;
        this.addressPreviewDialogClicks = addressPreviewDialogClicks;
    }

    public CustomAddressPreviewDialog(Context context, FranchiseeDetails franchiseeDetails) {
        super(context);
        this.context = context;
        this.franchiseeDetails = franchiseeDetails;
    }

    public interface AddressPreviewDialogClicks {
        public void capturePhotoClick();

        public void OkClick(FranchiseeDetails franchiseeDetails);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        final Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        setContentView(R.layout.dialog_address_preview);

        parentLayout = (LinearLayout) findViewById(R.id.parentLayout);
        CommonUtils.setDialogWidth(context, parentLayout);

        //init

        stateSpinner = findViewById(R.id.SpinnerCountryActivity_country_spinner);
        districtSpinner = findViewById(R.id.SpinnerCountryActivity_state_spinner);
        villageSpinner = findViewById(R.id.SpinnerCountryActivity_city_spinner);
        AddressTypeSpinner = findViewById(R.id.Spinner_Address_Type);
        wardNoTextView = (TextView) findViewById(R.id.wardNoTextView);
        SiteVisitAddress1 = (EditText) findViewById(R.id.SiteVisitAddress1);
        SiteVisitAddress2 = (EditText) findViewById(R.id.SiteVisitAddress2);
        SiteVisitLandmark = (EditText) findViewById(R.id.SiteVisitLandmark);
        imgAddressProof = (ImageView) findViewById(R.id.imgAddressProof);
        addressName = (TextView) findViewById(R.id.addressName);
        linAddressName = (LinearLayout) findViewById(R.id.linAddressName);
        edtAddressTypeNumber = (EditText) findViewById(R.id.edtAddressTypeNumber);

        CommonUtils.applyInputFilter(SiteVisitAddress1, SPECIAL_CHARS);
        CommonUtils.applyInputFilter(SiteVisitAddress2, SPECIAL_CHARS);
        CommonUtils.applyInputFilter(SiteVisitLandmark, SPECIAL_CHARS);


        SiteVisitWardNo = (EditText) findViewById(R.id.SiteVisitWardNo);
        SiteVisitPincode = (EditText) findViewById(R.id.SiteVisitPincode);
        scrollViewNextGenSiteVisit = (ScrollView) findViewById(R.id.scrollViewNextGenSiteVisit);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnSubmitAddress = (Button) findViewById(R.id.btnSubmitAddress);


        //SiteVisite add text Change Listner
        SiteVisitWardNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not Required.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Do Nothing
            }

            @Override
            public void afterTextChanged(Editable s) {

                if ((SiteVisitWardNo.length() == 0)) {
                    SiteVisitWardNo.setTextColor(Color.parseColor("#468847"));
                    SiteVisitWardNo.setError(null);

                } else if (SiteVisitWardNo.length() <= 1) {
                    //Do Nothing
                } else {
                    SiteVisitWardNo.setTextColor(Color.parseColor("#468847"));
                    SiteVisitWardNo.setError(null);
                }
            }
        });
        layoutAddressOfLocationVisited = findViewById(R.id.layoutAddressOfLocationVisited);

        btnCancel.setTypeface(font);
        btnSubmitAddress.setTypeface(font);

        // Set Font Text
        btnCancel.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + "  " +
                getContext().getResources().getString(R.string.cancel)));
        btnSubmitAddress.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " +
                getContext().getResources().getString(R.string.update)));

        // Add Listener to Buttons
        btnCancel.setOnClickListener(this);
        btnSubmitAddress.setOnClickListener(this);

        //when First time call State Asyntask.

        //get Address type data
        CallAddressTypeAsynTask();

        //state Spinner On Click listner
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final State id = (State) stateSpinner.getItemAtPosition(i);
                selectStateID = id.getStateId();
                if (selectStateID == null || selectStateID.equalsIgnoreCase("") || selectStateID.equalsIgnoreCase(PLEASE_SELECT)) {
                    //Do Nothing
                } else {
                    franchiseeDetails.setSiteVisitState(selectStateID);
                    // call to District Asyntask- when click state list item
                    CallDistricAsynTask();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Do Nothing
            }
        });

        //call to District Spinner
        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final District id = (District) districtSpinner.getItemAtPosition(i);
                selectDistricID = id.getDistrictId();
                if (selectDistricID == null || selectDistricID.equalsIgnoreCase("") || selectDistricID.equalsIgnoreCase(PLEASE_SELECT)) {
                    //Do Nothing
                } else {
                    // call to Village Asyntask  - when click Village list item
                    CallVillageAsynTask();
                    franchiseeDetails.setSiteVisitDistrict(selectDistricID);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Do Nothing
            }
        });

        //call to village Spinner
        villageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final Village id = (Village) villageSpinner.getItemAtPosition(i);
                selectVillageID = id.getVillageId();
                if (selectVillageID == null || selectVillageID.equalsIgnoreCase("") || selectVillageID.equalsIgnoreCase(PLEASE_SELECT)) {
                    //Do Nothing
                } else {
                    // call to Rural asyntask - when click village list item
                    CallIsRuralAsyncTask();
                    franchiseeDetails.setSiteVisitVTC(selectVillageID);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Do Nothing
            }
        });

        franchiseeDetails.setAddressProofImage(null);
        if (!TextUtils.isEmpty(franchiseeDetails.getAddressProofImage())) {
            bitmap = CommonUtils.StringToBitMap(franchiseeDetails.getAddressProofImage());
            if (bitmap != null)
                imgAddressProof.setImageBitmap(bitmap);
        }
        imgAddressProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addressPreviewDialogClicks.capturePhotoClick();

            }
        });

        AddressTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final AddressProofDetailsDTO id = (AddressProofDetailsDTO) AddressTypeSpinner.getItemAtPosition(i);
                AddressTypeSelectID = id.getKycId();
                if (AddressTypeSelectID.equalsIgnoreCase("0")) {
                    linAddressName.setVisibility(View.GONE);
                } else {
                    linAddressName.setVisibility(View.VISIBLE);
                    addressName.setText(id.getKycName());
                    edtAddressTypeNumber.setHint(id.getKycName());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Do Nothing
            }
        });

    }

    //region image preview dialo refresh
    public void RefreshDialog() {
        if (!TextUtils.isEmpty(franchiseeDetails.getAddressProofImage())) {
            bitmap = CommonUtils.StringToBitMap(franchiseeDetails.getAddressProofImage());
            if (bitmap != null)
                imgAddressProof.setImageBitmap(bitmap);
        }
    }
    //endregion

    //region AsynTask CallAddressTypeAsynTask
    private void CallAddressTypeAsynTask() {
        asyncTaskAddressProofDetail = new AsyncTaskAddressProofDetail(getContext(), new AsyncTaskAddressProofDetail.Callback() {
            @Override
            public void onResult(String displayServerResopnse) {
                try {
                    StringTokenizer tokens = new StringTokenizer(displayServerResopnse, "|");
                    String okay = tokens.nextToken();
                    if (displayServerResopnse == null) {

                        String message = null;
                        Log.e("TAG", ((message == null) ? "string null" : message));

                    } else if (okay.equals("ERROR")) {
                        AlertDialogBoxInfo.alertDialogShow(getContext(), getContext().getResources().getString(R.string.Warning));

                    } else if (displayServerResopnse.equals("Invalid Request..")) {
                        AlertDialogBoxInfo.alertDialogShow(getContext(), getContext().getResources().getString(R.string.Warning));

                    } else if (okay.equals("Invalid request.")) {
                        AlertDialogBoxInfo.alertDialogShow(getContext(), getContext().getResources().getString(R.string.Warning));
                    } else if (okay.equals("OKAY")) {

                        String strJson = displayServerResopnse;
                        strJson = strJson.replace("OKAY|", "");
                        strJson = strJson.replace("\r\n", "");
                        JSONArray jArray = new JSONArray(strJson);

                        arrayListaddressProofDetails.add(new AddressProofDetailsDTO("0", PLEASE_SELECT, PLEASE_SELECT));
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject jsonObject = jArray.getJSONObject(i);
                            AddressProofDetailsDTO categoryModel = new AddressProofDetailsDTO(
                                    jsonObject.optString("kyc_id"),
                                    jsonObject.optString("kyc_desc"),
                                    jsonObject.optString("kyc_name"));

                            arrayListaddressProofDetails.add(categoryModel);

                        }
                        AddressTypeSpinner
                                .setAdapter(new ArrayAdapter<AddressProofDetailsDTO>(getContext(),
                                        android.R.layout.simple_spinner_dropdown_item,
                                        arrayListaddressProofDetails));


                    }
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });
        asyncTaskAddressProofDetail.execute();

    }
    //endregion

    //region AsynTask District
    private void CallDistricAsynTask() {
        asyncTaskDistricSpinner = new AsyncTaskDistricSpinner(getContext(), selectStateID, new AsyncTaskDistricSpinner.Callback() {
            @Override
            public void onResult(String diplayServerResopnse) {
                StringTokenizer tokens = new StringTokenizer(diplayServerResopnse, "|");
                tokens.nextToken();
                String second = "{" +
                        "\"District\":" + tokens.nextToken() + "}";
                try {
                    arrayListDistrict.removeAll(arrayListDistrict);
                    JSONObject jsnobject = new JSONObject(second);


                    JSONArray jsonArray = jsnobject.getJSONArray("District");
                    arrayListDistrict.add(new District("", PLEASE_SELECT));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject explrObject = jsonArray.getJSONObject(i);
                        String a = explrObject.getString("lg_district_id");
                        String b = explrObject.getString("district_name");
                        arrayListDistrict.add(new District(a, b));
                    }
                } catch (Exception e) {
                    e.getMessage();
                }

                districtSpinner
                        .setAdapter(new ArrayAdapter<District>(getContext(),
                                android.R.layout.simple_spinner_dropdown_item,
                                arrayListDistrict));


            }
        });
        asyncTaskDistricSpinner.execute();
    }
    //endregion

    //region AsynTask Village
    private void CallVillageAsynTask() {
        asyncTaskVillageSpinner = new AsyncTaskVillageSpinner(getContext(), selectStateID, selectDistricID, new AsyncTaskVillageSpinner.Callback() {
            @Override
            public void onResult(String diplayServerResopnse) {
                StringTokenizer tokens = new StringTokenizer(diplayServerResopnse, "|");
                tokens.nextToken();
                String second = "{" +
                        "\"Village\":" + tokens.nextToken() + "}";
                try {

                    arrayListVillage.removeAll(arrayListVillage);
                    JSONObject jsnobject = new JSONObject(second);


                    JSONArray jsonArray = jsnobject.getJSONArray("Village");
                    arrayListVillage.add(new Village("", PLEASE_SELECT));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String a = obj.getString("lg_village_id");
                        String b = obj.getString("village_name");
                        arrayListVillage.add(new Village(a, b));
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
                villageSpinner.setAdapter(new ArrayAdapter<Village>(getContext(), android.R.layout.simple_spinner_dropdown_item, arrayListVillage));
            }

        });
        asyncTaskVillageSpinner.execute();
    }
    //endregion

    //region AsynTask Rural
    private void CallIsRuralAsyncTask() {
        asyncTaskIsRural = new AsyncTaskIsRural(getContext(), selectVillageID, new AsyncTaskIsRural.Callback() {
            @Override
            public void onResult(String diplayServerResopnse) {
                try {
                    if (diplayServerResopnse.equals("Invalid Request.")) {
                        //Do Nothingd
                    }
                    StringTokenizer tokens = new StringTokenizer(diplayServerResopnse, "|");
                    tokens.nextToken();
                    String second = "{" +
                            "\"District\":" + tokens.nextToken() + "}";
                    String isRural = "0";
                    JSONObject jsonbject = new JSONObject(second);
                    JSONArray jsonArray = jsonbject.getJSONArray("District");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        isRural = obj.getString("isRural");
                    }

                    if (isRural.equals("1")) {
                        wardNoTextView.setVisibility(View.VISIBLE);
                        SiteVisitWardNo.setVisibility(View.VISIBLE);
                    } else {
                        wardNoTextView.setVisibility(View.VISIBLE);
                        SiteVisitWardNo.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        asyncTaskIsRural.execute();

    }
    //endregion

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnSubmitAddress) {
            if (isValidSiteVisit()) {
                setFranchiseeData();
                addressPreviewDialogClicks.OkClick(franchiseeDetails);
                dismiss();
            }
        } else if (id == R.id.btnCancel) {
            dismiss();
        }
    }

    //region setFranchiseeData
    public void setFranchiseeData() {
        if (!SiteVisitAddress1.getText().toString().equals("")) {
            franchiseeDetails.setSiteVisitAddress1(CommonUtils.toTitleCase(SiteVisitAddress1.getText().toString()));
        }
        if (!SiteVisitAddress2.getText().toString().equals("")) {
            franchiseeDetails.setSiteVisitAddress2(CommonUtils.toTitleCase(SiteVisitAddress2.getText().toString()));
        }
        if (!SiteVisitLandmark.getText().toString().equals("")) {
            franchiseeDetails.setSiteVisitLandmark(CommonUtils.toTitleCase(SiteVisitLandmark.getText().toString()));
        }
        if (!AddressTypeSelectID.equalsIgnoreCase("0")) {
            franchiseeDetails.setAddressProofType(AddressTypeSelectID);
        }
        if (!edtAddressTypeNumber.getText().toString().equals("")) {
            franchiseeDetails.setAddressProofNo(edtAddressTypeNumber.getText().toString().toUpperCase());
        }

        if (franchiseeDetails.getAddressProofImage() != null && !franchiseeDetails.getAddressProofImage().isEmpty()
                && !franchiseeDetails.getAddressProofImage().equals("null")) {
            franchiseeDetails.setAddressProofImage(franchiseeDetails.getAddressProofImage());
        }
    }
    //endregion

    //region Validate Address Of Location Visited
    private boolean isValidSiteVisit() {
        boolean isValid = true;
        int a = AddressTypeSpinner.getSelectedItemPosition();
        boolean hasDrawable = (imgAddressProof.getDrawable() == null);
        if (TextUtils.isEmpty(SiteVisitAddress1.getText().toString())) {
            SiteVisitAddress1.setError("Enter Address");
            isValid = false;
            SiteVisitAddress1.requestFocus();
        } else if (TextUtils.isEmpty(SiteVisitAddress2.getText().toString())) {
            SiteVisitAddress2.setError("Enter Address");
            isValid = false;
            SiteVisitAddress2.requestFocus();
        } else if (TextUtils.isEmpty(SiteVisitLandmark.getText().toString())) {
            SiteVisitLandmark.setError("Enter Landmark");
            isValid = false;
            SiteVisitLandmark.requestFocus();
        } else if (AddressTypeSpinner.getVisibility() == View.VISIBLE && a == 0) {
            ((TextView) AddressTypeSpinner.getSelectedView()).setError(PLEASE_SELECT);
            Toast.makeText(getContext(), "Please Select Address Proof Detail", Toast.LENGTH_SHORT).show();
            isValid = false;
        } else if (TextUtils.isEmpty(edtAddressTypeNumber.getText().toString())) {
            edtAddressTypeNumber.setError("Enter Number");
            isValid = false;
            edtAddressTypeNumber.requestFocus();
        } else if (!hasDrawable && bitmap == null) {
            Toast.makeText(getContext(), "Please Capture Address proof Image", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        return isValid;
    }

    //endregion
}
