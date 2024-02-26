package in.vakrangee.franchisee.delivery_address;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import in.vakrangee.franchisee.sitelayout.repository.DeliveryAddressRepository;
import in.vakrangee.supercore.franchisee.utils.CustomSearchableSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.nextgenfranchiseeapplication.CustomFranchiseeApplicationSpinnerAdapter;
import in.vakrangee.supercore.franchisee.nextgenfranchiseeapplication.CustomFranchiseeApplicationSpinnerDto;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.GUIUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeliveryAddressFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private View view;
    private Context context;
    private Connection connection;

    //feet and inch
    @BindView(R.id.spinnerProvisionalLengthFeet)
    Spinner spinnerProvisionalLengthFeet;
    @BindView(R.id.spinnerProvisionalLengthInch)
    Spinner spinnerProvisionalLengthInch;

    @BindView(R.id.spinnerProvisionalWidthFeet)
    Spinner spinnerProvisionalWidthFeet;
    @BindView(R.id.spinnerProvisionalWidthInch)
    Spinner spinnerProvisionalWidthInch;

    //state-district-vtc
    @BindView(R.id.spinnerState)
    CustomSearchableSpinner spinnerState;
    @BindView(R.id.spinnerDistrict)
    CustomSearchableSpinner spinnerDistrict;
    @BindView(R.id.spinnerVTC)
    CustomSearchableSpinner spinnerVTC;

    @BindView(R.id.editTextAddressLine1)
    EditText editTextAddressLine1;
    @BindView(R.id.editTextAddressLine2)
    EditText editTextAddressLine2;
    @BindView(R.id.editTextLandMark)
    EditText editTextLandMark;
    @BindView(R.id.editTextPincode)
    EditText editTextPincode;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    // * mark
    @BindView(R.id.textviewAddress)
    TextView textviewAddress;
    @BindView(R.id.textviewLandmark)
    TextView textviewLandmark;
    @BindView(R.id.textviewState)
    TextView textviewState;
    @BindView(R.id.textviewDistrict)
    TextView textviewDistrict;
    @BindView(R.id.textviewVTC)
    TextView textviewVTC;
    @BindView(R.id.textviewPinCode)
    TextView textviewPinCode;
    @BindView(R.id.textViewLength)
    TextView textViewLength;
    @BindView(R.id.textViewWidth)
    TextView textViewWidth;
    @BindView(R.id.layoutMain)
    LinearLayout layoutMain;
    @BindView(R.id.cardviewFlex)
    CardView cardviewFlex;
    @BindView(R.id.spinnerDeliveryAddressType)
    Spinner spinnerDeliveryAddressType;
    @BindView(R.id.layoutMainAddressDetails)
    LinearLayout layoutMainAddressDetails;

    private DeliveryAddressRepository deliveryAddressRepository;
    //  private List<DeliveryAddressDto> deliveryAddressDto;
    private DeliveryAddressDto deliveryAddressDto;

    private ArrayAdapter<Integer> WidthfeetArrayAdapter, LengthfeetArrayAdapter, inchArrayAdapter;

    private List<CustomFranchiseeApplicationSpinnerDto> stateList;
    private List<CustomFranchiseeApplicationSpinnerDto> districtList;
    private List<CustomFranchiseeApplicationSpinnerDto> VTCList;
    private List<CustomFranchiseeApplicationSpinnerDto> deliveryAddressList;
    private AsynGetAllAddressSpinnerData asynGetAllAddressSpinnerData = null;
    private AsyncSaveDeliveryAddress asyncSaveDeliveryAddress;
    private AsyncGetDeliveryAddress asyncGetDeliveryAddress;

    public DeliveryAddressFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_delivery_address, container, false);
        this.context = getContext();
        connection = new Connection(context);
        ButterKnife.bind(this, view);

        //Input filter
        CommonUtils.InputFiletrWithMaxLength(editTextAddressLine1, "~#^|$%&*!", 35);
        CommonUtils.InputFiletrWithMaxLength(editTextAddressLine2, "~#^|$%&*!", 35);
        CommonUtils.InputFiletrWithMaxLength(editTextLandMark, "~#^|$%&*!", 35);

        GUIUtils.CompulsoryMark(textviewAddress, "Address ");
        GUIUtils.CompulsoryMark(textviewState, "State ");
        GUIUtils.CompulsoryMark(textviewDistrict, "District ");
        GUIUtils.CompulsoryMark(textviewVTC, "Village/Town/City ");
        GUIUtils.CompulsoryMark(textviewPinCode, "PIN Code ");
        GUIUtils.CompulsoryMark(textViewLength, "Length ");
        GUIUtils.CompulsoryMark(textViewWidth, "Width (Max. 3 feet) ");

        deliveryAddressRepository = new DeliveryAddressRepository(context);
        deliveryAddressDto = new DeliveryAddressDto();


        //get spinner data for delivery type
        asynGetAllAddressSpinnerData = new AsynGetAllAddressSpinnerData(null, null);
        asynGetAllAddressSpinnerData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "DELIVERY_ADDRESS");
        //load state data
        asynGetAllAddressSpinnerData = new AsynGetAllAddressSpinnerData(null, null);
        asynGetAllAddressSpinnerData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "STATE");
        //get address details
        asyntaskGetAddressDetails();


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validationData() == 0) {
                    //save data
                    alertDialogSaveAddressConfirmation(getResources().getString(R.string.delivery_address_submit));
                    //saveData();
                }
            }
        });
        return view;

    }

    private void alertDialogSaveAddressConfirmation(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        saveData();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private void saveData() {
        /*Gson gson = new Gson();
        String jsonData = gson.toJson(deliveryAddressDto, new TypeToken<ArrayList<DeliveryAddressDto>>() {
        }.getType());*/

        Gson gson = new Gson();
        String jsonData = gson.toJson(deliveryAddressDto, DeliveryAddressDto.class);

        /*Intent intent = new Intent();
        intent.putExtra("delivery_address_details", jsonData);
        getActivity().setResult(1111, intent);
        getActivity().finish();*/

        asyncSaveDeliveryAddress = new AsyncSaveDeliveryAddress(context, jsonData, new AsyncSaveDeliveryAddress.Callback() {
            @Override
            public void onResult(String result) {
                try {
                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                        return;
                    }
                    //Response
                    if (result.startsWith("ERROR|")) {
                        result = result.replace("ERROR|", "");
                        if (TextUtils.isEmpty(result)) {
                            AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                        } else {
                            AlertDialogBoxInfo.alertDialogShow(context, result);
                        }
                    } else if (result.startsWith("OKAY|")) {
                        //AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.delivery_address_save));
                        String getResult = result.replace("OKAY|", "");

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage(getResources().getString(R.string.delivery_address_save))
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        reload(getResult);
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();

                        // reload(result);

                    } else {
                        AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                }
            }
        });
        asyncSaveDeliveryAddress.execute();
    }

    public void reload(String result) {
        /*if (TextUtils.isEmpty(result)) {
            return;
        }*/
        if (!TextUtils.isEmpty(result)) {
            Gson gson = new GsonBuilder().create();
            List<DeliveryAddressDto> addressDtos = gson.fromJson(result, new TypeToken<ArrayList<DeliveryAddressDto>>() {
            }.getType());

            deliveryAddressDto = addressDtos.get(0);
        }

        bindData();

        loadFeetInchData();
    }


    //region button click validation data
    private int validationData() {

        //STEP 1-Address Line 1
        // deliveryAddressDto.get(0).setAddressLine1(editTextAddressLine1.getText().toString().trim());
        deliveryAddressDto.setAddressLine1(editTextAddressLine1.getText().toString());
        if (TextUtils.isEmpty(deliveryAddressDto.getAddressLine1())) {
            Toast.makeText(context, "Please enter Address line 1", Toast.LENGTH_LONG).show();
            editTextAddressLine1.setError("Please enter Address line 1.");
            return 1;
        }
        //STEP 2 -Address Line 2
        deliveryAddressDto.setAddressLine2(editTextAddressLine2.getText().toString());
        /*if (TextUtils.isEmpty(deliveryAddressDto.getAddressLine2())) {
            Toast.makeText(context, "Please enter Address line 2", Toast.LENGTH_LONG).show();
            editTextAddressLine2.setError("Please enter Address line 2.");
            return 2;
        }*/

        //STEP 3 -landmardk
        deliveryAddressDto.setLandmark(editTextLandMark.getText().toString());


        //STEP 6 - Pincode
        deliveryAddressDto.setPinCode(editTextPincode.getText().toString());
        if (TextUtils.isEmpty(deliveryAddressDto.getPinCode()) || deliveryAddressDto.getPinCode().length() != 6) {
            Toast.makeText(context, "Please enter Pincode.", Toast.LENGTH_LONG).show();
            editTextPincode.setError("Please enter Pincode.");
            return 6;
        }

        //STEP  7 -state
        if (TextUtils.isEmpty(deliveryAddressDto.getStateId()) || deliveryAddressDto.getStateId().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select State in section.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerState, "Please select State in section.", context);
            return 7;
        }

        // STEP 8 -District
        if (TextUtils.isEmpty(deliveryAddressDto.getDistrictId()) || deliveryAddressDto.getDistrictId().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select District in section.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerDistrict, "Please select District in section.", context);
            return 8;
        }

        //STEP 9: Village/Town/City
        if (TextUtils.isEmpty(deliveryAddressDto.getVtcId()) || deliveryAddressDto.getVtcId().equalsIgnoreCase("0")) {
            Toast.makeText(context, "Please select Village/Town/City in  section.", Toast.LENGTH_LONG).show();
            GUIUtils.setErrorToSpinner(spinnerVTC, "Please select Village/Town/City in section.", context);
            return 9;
        }

        //isFlex Y - then check validation(visible)  N- no validation (gone)
        String isFlex = TextUtils.isEmpty(deliveryAddressDto.getIsFlex()) ? "Y" : deliveryAddressDto.getIsFlex();

        if (isFlex.equalsIgnoreCase("Y")) {
            //STEP 10: Length
            String widthFeetAndInchesProvisional = CommonUtils.inchesTofeetAndInches(deliveryAddressDto.getProvisionalLengthMainSignboard() == null ? "0" : deliveryAddressDto.getProvisionalLengthMainSignboard());
            final String[] splitWidthFeetAndInchesProvisional = widthFeetAndInchesProvisional.split("\\|");
            if (TextUtils.isEmpty(splitWidthFeetAndInchesProvisional[0]) || splitWidthFeetAndInchesProvisional[0].equalsIgnoreCase("0")) {
                Toast.makeText(context, "Please select Length section.", Toast.LENGTH_LONG).show();
                GUIUtils.setErrorToSpinner(spinnerVTC, "Please select Length section.", context);
                return 10;
            }

            if (TextUtils.isEmpty(deliveryAddressDto.getProvisionalLengthMainSignboard()) || deliveryAddressDto.getProvisionalLengthMainSignboard().equalsIgnoreCase("0")) {
                Toast.makeText(context, "Please select Length section.", Toast.LENGTH_LONG).show();
                GUIUtils.setErrorToSpinner(spinnerVTC, "Please select Length section.", context);
                return 10;
            }

            //STEP 11: Width
            //width provisional
            String depthFeetAndInchesProvisional = CommonUtils.inchesTofeetAndInches(deliveryAddressDto.getProvisionalWidth() == null ? "0" : deliveryAddressDto.getProvisionalWidth());
            String[] splitDepthFeetAndInchesP = depthFeetAndInchesProvisional.split("\\|");
            if (TextUtils.isEmpty(splitDepthFeetAndInchesP[0]) || splitDepthFeetAndInchesP[0].equalsIgnoreCase("0")) {
                Toast.makeText(context, "Please select Width section.", Toast.LENGTH_LONG).show();
                GUIUtils.setErrorToSpinner(spinnerVTC, "Please select Width section.", context);
                return 10;
            }

            if (TextUtils.isEmpty(deliveryAddressDto.getProvisionalWidth()) || deliveryAddressDto.getProvisionalWidth().equalsIgnoreCase("0")) {
                Toast.makeText(context, "Please select Width section.", Toast.LENGTH_LONG).show();
                GUIUtils.setErrorToSpinner(spinnerVTC, "Please select Width section.", context);
                return 10;
            }
        }

        return 0;
    }
    //endregion

    //region spinner on  item select
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        int Id = adapterView.getId();
        if (Id == R.id.spinnerState) {
            if (position > 0) {
                spinnerState.setTitle("Select State");
                spinnerState.requestFocus();
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerState.getItemAtPosition(position);
                if (!entityDto.getId().equals("0")) {
                    deliveryAddressDto.setStateId(entityDto.getId());
                    //Get District
                    asynGetAllAddressSpinnerData = new AsynGetAllAddressSpinnerData(deliveryAddressDto.getStateId(), null);
                    asynGetAllAddressSpinnerData.execute("DISTRICT");
                }
            } else {
                spinnerDistrict.setEnabled(false);
                spinnerVTC.setEnabled(false);
                spinnerDistrict.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, new ArrayList<CustomFranchiseeApplicationSpinnerDto>()));
                deliveryAddressDto.setDistrictId(null);
                spinnerVTC.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, new ArrayList<CustomFranchiseeApplicationSpinnerDto>()));
                deliveryAddressDto.setVtcId(null);
            }
        } else if (Id == R.id.spinnerDistrict) {
            if (position > 0) {
                spinnerDistrict.setTitle("Select District");
                spinnerDistrict.requestFocus();
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerDistrict.getItemAtPosition(position);
                if (!entityDto.getId().equals("0")) {
                    deliveryAddressDto.setDistrictId(entityDto.getId());
                    //Get VTC
                    asynGetAllAddressSpinnerData = new AsynGetAllAddressSpinnerData(deliveryAddressDto.getStateId(), deliveryAddressDto.getDistrictId());
                    asynGetAllAddressSpinnerData.execute("VTC");

                }
            } else {
                spinnerVTC.setEnabled(false);
                spinnerVTC.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, new ArrayList<CustomFranchiseeApplicationSpinnerDto>()));
                deliveryAddressDto.setVtcId(null);
            }
        } else if (Id == R.id.spinnerVTC) {
            spinnerVTC.setTitle("Select District");
            spinnerVTC.requestFocus();
            CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerVTC.getItemAtPosition(position);
            deliveryAddressDto.setVtcId(entityDto.getId());
        } else if (Id == R.id.spinnerDeliveryAddressType) {
            CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerDeliveryAddressType.getItemAtPosition(position);
            if (!entityDto.getId().equals("0")) {
                deliveryAddressDto.setAddressDeliveryType(entityDto.getId());
                //click call address type asyntask
                asyntaskGetAddressDetails();
            } else {
                deliveryAddressDto.setAddressDeliveryType(null);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    //endregion

    //region get state- distrcit -vtc spinner data
    private class AsynGetAllAddressSpinnerData extends AsyncTask<String, Void, String> {

        String tmpVkId = connection.getVkid();
        String enquiryId = CommonUtils.getEnquiryId(context);
        String vkIdOrEnquiryId = TextUtils.isEmpty(tmpVkId) ? enquiryId : tmpVkId;

        private String stateId;
        private String districtId;
        private String type;

        public AsynGetAllAddressSpinnerData(String stateId, String districtId) {
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
                case "DELIVERY_ADDRESS":
                    deliveryAddressList = deliveryAddressRepository.getDeliveryTypeList(CommonUtils.getEnquiryId(context));
                    break;
                case "STATE":
                    //STEP 1: State List
                    stateList = deliveryAddressRepository.getAllStateBylList(vkIdOrEnquiryId);
                    break;
                case "DISTRICT":
                    districtList = deliveryAddressRepository.getAllDistrictBylList(vkIdOrEnquiryId, stateId);
                    break;
                case "VTC":
                    VTCList = deliveryAddressRepository.getAllVTCBylList(vkIdOrEnquiryId, stateId, districtId);
                    break;
                default:
                    break;

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            switch (type.toUpperCase()) {
                case "DELIVERY_ADDRESS":
                    setDeliveryAddressAdapter();
                    break;
                case "STATE":
                    setStateSpinnerAdapter();
                    break;
                case "DISTRICT":
                    setDistrictSpinnerAdapter(districtList, spinnerDistrict, deliveryAddressDto.getDistrictId());
                    break;
                case "VTC":
                    setVTCSpinnerAdapter(VTCList, spinnerVTC, deliveryAddressDto.getVtcId());
                    break;
                default:
                    break;
            }

        }
    }
    //endregion

    //region load feet and inch data
    private void loadFeetInchData() {
        //when focusable true , if item click in spinner
        CommonUtils.spinner_focusablemode(spinnerProvisionalLengthFeet);
        CommonUtils.spinner_focusablemode(spinnerProvisionalLengthInch);
        CommonUtils.spinner_focusablemode(spinnerProvisionalWidthFeet);
        CommonUtils.spinner_focusablemode(spinnerProvisionalWidthInch);

        //Width Adapter
        List feetProvisional = new ArrayList<Integer>();
        feetProvisional.add("Select");
        for (int i = 1; i <= 3; i++) {
            if (i != 1) {
                feetProvisional.add(Integer.toString(i));
            }
        }
        WidthfeetArrayAdapter = new ArrayAdapter<Integer>(getContext(), android.R.layout.simple_spinner_item, feetProvisional);
        WidthfeetArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Length Adapter
        List feet1feetProvisional = new ArrayList<Integer>();
        feet1feetProvisional.add("Select");
        for (int i = 1; i <= 50; i++) {
            feet1feetProvisional.add(Integer.toString(i));
        }
        LengthfeetArrayAdapter = new ArrayAdapter<Integer>(getContext(), android.R.layout.simple_spinner_item, feet1feetProvisional);
        LengthfeetArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //setFeetAdapter(50, LengthfeetArrayAdapter);

        //Inch Adapter
        List inchProvisional = new ArrayList<Integer>();
        inchProvisional.add("Select");

        for (int i = 0; i < 12; i++) {
            inchProvisional.add(Integer.toString(i));
        }
        inchArrayAdapter = new ArrayAdapter<Integer>(getContext(), android.R.layout.simple_spinner_item, inchProvisional);
        inchArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //inches
        String widthFeetAndInchesProvisional = CommonUtils.inchesTofeetAndInches(deliveryAddressDto.getProvisionalLengthMainSignboard() == null ? "0" : deliveryAddressDto.getProvisionalLengthMainSignboard());
        final String[] splitWidthFeetAndInchesProvisional = widthFeetAndInchesProvisional.split("\\|");

        //length feet
        spinnerProvisionalLengthFeet.setAdapter(LengthfeetArrayAdapter);
        spinnerProvisionalLengthFeet.setSelection(CommonUtils.getIndexText(spinnerProvisionalLengthFeet, splitWidthFeetAndInchesProvisional[0]));
        spinnerProvisionalLengthFeet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerProvisionalLengthFeet.getSelectedItem().toString().equals("Select") &&
                        spinnerProvisionalLengthInch.getSelectedItem().toString().equals("Select")) {
                    deliveryAddressDto.setProvisionalLengthMainSignboard(null);
                } else if (!spinnerProvisionalLengthFeet.getSelectedItem().toString().equals("Select") &&
                        spinnerProvisionalLengthInch.getSelectedItem().toString().equals("Select")) {
                    int a = CommonUtils.feetAndInchesToInches(Integer.parseInt(spinnerProvisionalLengthFeet.getSelectedItem().toString()), 0);
                    deliveryAddressDto.setProvisionalLengthMainSignboard(String.valueOf(a));
                } else if (spinnerProvisionalLengthFeet.getSelectedItem().toString().equals("Select") &&
                        !spinnerProvisionalLengthInch.getSelectedItem().toString().equals("Select")) {
                    int a = CommonUtils.feetAndInchesToInches(0, Integer.parseInt(spinnerProvisionalLengthInch.getSelectedItem().toString()));
                    deliveryAddressDto.setProvisionalLengthMainSignboard(String.valueOf(a));
                } else {
                    int a = CommonUtils.feetAndInchesToInches(Integer.parseInt(spinnerProvisionalLengthFeet.getSelectedItem().toString()),
                            Integer.parseInt(spinnerProvisionalLengthInch.getSelectedItem().toString()));
                    deliveryAddressDto.setProvisionalLengthMainSignboard(String.valueOf(a));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //length inch
        spinnerProvisionalLengthInch.setAdapter(inchArrayAdapter);
        spinnerProvisionalLengthInch.setSelection(CommonUtils.getIndexText(spinnerProvisionalLengthInch, splitWidthFeetAndInchesProvisional[1]));
        spinnerProvisionalLengthInch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerProvisionalLengthFeet.getSelectedItem().toString().equals("Select") &&
                        spinnerProvisionalLengthInch.getSelectedItem().toString().equals("Select")) {
                    deliveryAddressDto.setProvisionalLengthMainSignboard(null);
                } else if (!spinnerProvisionalLengthFeet.getSelectedItem().toString().equals("Select") &&
                        spinnerProvisionalLengthInch.getSelectedItem().toString().equals("Select")) {
                    int a = CommonUtils.feetAndInchesToInches(Integer.parseInt(spinnerProvisionalLengthFeet.getSelectedItem().toString()), 0);
                    deliveryAddressDto.setProvisionalLengthMainSignboard(String.valueOf(a));
                } else if (spinnerProvisionalLengthFeet.getSelectedItem().toString().equals("Select") &&
                        !spinnerProvisionalLengthInch.getSelectedItem().toString().equals("Select")) {
                    int a = CommonUtils.feetAndInchesToInches(0, Integer.parseInt(spinnerProvisionalLengthInch.getSelectedItem().toString()));
                    deliveryAddressDto.setProvisionalLengthMainSignboard(String.valueOf(a));
                } else {
                    int a = CommonUtils.feetAndInchesToInches(Integer.parseInt(spinnerProvisionalLengthFeet.getSelectedItem().toString()),
                            Integer.parseInt(spinnerProvisionalLengthInch.getSelectedItem().toString()));
                    deliveryAddressDto.setProvisionalLengthMainSignboard(String.valueOf(a));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //width provisional
        String depthFeetAndInchesProvisional = CommonUtils.inchesTofeetAndInches(deliveryAddressDto.getProvisionalWidth() == null ? "0" : deliveryAddressDto.getProvisionalWidth());
        String[] splitDepthFeetAndInchesP = depthFeetAndInchesProvisional.split("\\|");


        //Width Feet
        spinnerProvisionalWidthFeet.setAdapter(WidthfeetArrayAdapter);
        spinnerProvisionalWidthFeet.setSelection(CommonUtils.getIndexText(spinnerProvisionalWidthFeet, splitDepthFeetAndInchesP[0]));
        spinnerProvisionalWidthFeet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerProvisionalWidthFeet.getSelectedItem().toString().equals("Select") &&
                        spinnerProvisionalWidthInch.getSelectedItem().toString().equals("Select")) {
                    deliveryAddressDto.setProvisionalWidth(null);
                } else if (!spinnerProvisionalWidthFeet.getSelectedItem().toString().equals("Select") &&
                        spinnerProvisionalWidthInch.getSelectedItem().toString().equals("Select")) {
                    int a = CommonUtils.feetAndInchesToInches(Integer.parseInt(spinnerProvisionalWidthFeet.getSelectedItem().toString()), 0);
                    deliveryAddressDto.setProvisionalWidth(String.valueOf(a));
                } else if (spinnerProvisionalWidthFeet.getSelectedItem().toString().equals("Select") &&
                        !spinnerProvisionalWidthInch.getSelectedItem().toString().equals("Select")) {
                    int a = CommonUtils.feetAndInchesToInches(0, Integer.parseInt(spinnerProvisionalWidthInch.getSelectedItem().toString()));
                    deliveryAddressDto.setProvisionalWidth(String.valueOf(a));
                } else {
                    int a = CommonUtils.feetAndInchesToInches(Integer.parseInt(spinnerProvisionalWidthFeet.getSelectedItem().toString()),
                            Integer.parseInt(spinnerProvisionalWidthInch.getSelectedItem().toString()));
                    deliveryAddressDto.setProvisionalWidth(String.valueOf(a));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerProvisionalWidthInch.setAdapter(inchArrayAdapter);
        spinnerProvisionalWidthInch.setSelection(CommonUtils.getIndexText(spinnerProvisionalWidthInch, splitDepthFeetAndInchesP[1]));
        spinnerProvisionalWidthInch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerProvisionalWidthFeet.getSelectedItem().toString().equals("Select") &&
                        spinnerProvisionalWidthInch.getSelectedItem().toString().equals("Select")) {
                    deliveryAddressDto.setProvisionalWidth(null);
                } else if (!spinnerProvisionalWidthFeet.getSelectedItem().toString().equals("Select") &&
                        spinnerProvisionalWidthInch.getSelectedItem().toString().equals("Select")) {
                    int a = CommonUtils.feetAndInchesToInches(Integer.parseInt(spinnerProvisionalWidthFeet.getSelectedItem().toString()), 0);
                    deliveryAddressDto.setProvisionalWidth(String.valueOf(a));
                } else if (spinnerProvisionalWidthFeet.getSelectedItem().toString().equals("Select") &&
                        !spinnerProvisionalWidthInch.getSelectedItem().toString().equals("Select")) {
                    int a = CommonUtils.feetAndInchesToInches(0, Integer.parseInt(spinnerProvisionalWidthInch.getSelectedItem().toString()));
                    deliveryAddressDto.setProvisionalWidth(String.valueOf(a));
                } else {
                    int a = CommonUtils.feetAndInchesToInches(Integer.parseInt(spinnerProvisionalWidthFeet.getSelectedItem().toString()),
                            Integer.parseInt(spinnerProvisionalWidthInch.getSelectedItem().toString()));
                    deliveryAddressDto.setProvisionalWidth(String.valueOf(a));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    //endregion

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (asynGetAllAddressSpinnerData != null && !asynGetAllAddressSpinnerData.isCancelled()) {
            asynGetAllAddressSpinnerData.cancel(true);
        }
        if (asyncSaveDeliveryAddress != null && !asyncSaveDeliveryAddress.isCancelled()) {
            asyncSaveDeliveryAddress.cancel(true);
        }
    }

    //region Bind data
    private void bindData() {
        try {
            String isEditable = TextUtils.isEmpty(deliveryAddressDto.getIsEditable()) ? "Y" : deliveryAddressDto.getIsEditable();
            if (isEditable.equalsIgnoreCase("Y")) {
                GUIUtils.setViewAndChildrenEnabled(layoutMain, true);
                btnSubmit.setVisibility(View.VISIBLE);
            } else {
                GUIUtils.setViewAndChildrenEnabled(layoutMain, false);
                btnSubmit.setVisibility(View.GONE);
            }

            String isFlex = TextUtils.isEmpty(deliveryAddressDto.getIsFlex()) ? "Y" : deliveryAddressDto.getIsFlex();
            if (isFlex.equalsIgnoreCase("Y")) {
                cardviewFlex.setVisibility(View.VISIBLE);
            } else {
                cardviewFlex.setVisibility(View.GONE);
            }

            editTextAddressLine1.setText(TextUtils.isEmpty(deliveryAddressDto.getAddressLine1()) ? "" : deliveryAddressDto.getAddressLine1());
            editTextAddressLine2.setText(TextUtils.isEmpty(deliveryAddressDto.getAddressLine2()) ? "" : deliveryAddressDto.getAddressLine2());
            editTextLandMark.setText(TextUtils.isEmpty(deliveryAddressDto.getLandmark()) ? "" : deliveryAddressDto.getLandmark());
            editTextPincode.setText(TextUtils.isEmpty(deliveryAddressDto.getPinCode()) ? "" : deliveryAddressDto.getPinCode());

            //STEP 1 : Comm State
            CommonUtils.spinner_focusablemode(spinnerState);
            CommonUtils.spinner_focusablemode(spinnerDistrict);
            CommonUtils.spinner_focusablemode(spinnerVTC);

            setStateSpinnerAdapter();

            if (!TextUtils.isEmpty(deliveryAddressDto.getAddressDeliveryType())) {
                int spinnerPosition = deliveryAddressRepository.getSelectedPos(deliveryAddressList, String.valueOf(deliveryAddressDto.getAddressDeliveryType()));
                spinnerDeliveryAddressType.setSelection(spinnerPosition);
            }

        } catch (Exception e) {
            e.getMessage();
        }
    }
    //endregion

    //region Set address delivery Adapter
    private void setDeliveryAddressAdapter() {
        //delivery spinner data set
        CustomFranchiseeApplicationSpinnerAdapter deliveryAdapterW1 = new CustomFranchiseeApplicationSpinnerAdapter(context, deliveryAddressList);
        spinnerDeliveryAddressType.setAdapter(deliveryAdapterW1);
        int appSalutationPos = deliveryAddressRepository.getSelectedPos(deliveryAddressList, String.valueOf(deliveryAddressDto.getAddressDeliveryType()));
        spinnerDeliveryAddressType.setSelection(appSalutationPos);
        spinnerDeliveryAddressType.setOnItemSelectedListener(this);

    }
    //endregion

    //region Set District Adapter
    private void setStateSpinnerAdapter() {
        spinnerState.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, stateList));
        int appsstatePos = deliveryAddressRepository.getSelectedPos(stateList, deliveryAddressDto.getStateId());
        spinnerState.setSelection(appsstatePos);
        spinnerState.setOnItemSelectedListener(this);
    }

    //region Set District Adapter
    private void setDistrictSpinnerAdapter(List<CustomFranchiseeApplicationSpinnerDto> districtList, Spinner spinnerDistrict, String selDistrictId) {
        spinnerDistrict.setEnabled(true);
        spinnerDistrict.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, districtList));
        int appsDistrictPos = deliveryAddressRepository.getSelectedPos(districtList, selDistrictId);
        spinnerDistrict.setSelection(appsDistrictPos);
        spinnerDistrict.setOnItemSelectedListener(this);

    }
    //endregion

    //region Set VTC Adapter
    private void setVTCSpinnerAdapter(List<CustomFranchiseeApplicationSpinnerDto> VTCList, Spinner spinnerVTC, String selVTCId) {
        spinnerVTC.setEnabled(true);
        spinnerVTC.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, VTCList));
        int appsVTCPos = deliveryAddressRepository.getSelectedPos(VTCList, selVTCId);
        spinnerVTC.setSelection(appsVTCPos);
        spinnerVTC.setOnItemSelectedListener(this);
    }
    //endregion

    private void asyntaskGetAddressDetails() {
        String enquiryId = CommonUtils.getEnquiryId(context);
        String addressType;
        if (TextUtils.isEmpty(deliveryAddressDto.getAddressDeliveryType())) {
            addressType = "";
        } else {
            addressType = deliveryAddressDto.getAddressDeliveryType();
        }

        asyncGetDeliveryAddress = new AsyncGetDeliveryAddress(context, enquiryId, addressType, new AsyncGetDeliveryAddress.Callback() {
            @Override
            public void onResult(String result) {
                try {
                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                        return;
                    }
                    //Response
                    if (result.startsWith("ERROR|")) {
                        result = result.replace("ERROR|", "");
                        if (TextUtils.isEmpty(result)) {
                            AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                        } else {
                            AlertDialogBoxInfo.alertDialogShow(context, result);
                        }
                    } else if (result.startsWith("OKAY|")) {
                        result = result.replace("OKAY|", "");
                        reload(result);

                    } else {
                        AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                }
            }
        });

        asyncGetDeliveryAddress.execute();
    }

}
