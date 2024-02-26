package in.vakrangee.franchisee.sitelayout.update_kendra_address;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import in.vakrangee.supercore.franchisee.nextgenfranchiseeapplication.CustomFranchiseeApplicationSpinnerDto;
import in.vakrangee.supercore.franchisee.service.OkHttpService;
import in.vakrangee.supercore.franchisee.utils.Constants;

public class UpdateAddressRepository {

    private Context context;
    private OkHttpService okHttpService;
    private boolean isCached = false;

    public UpdateAddressRepository(Context mContext) {
        this.context = mContext;
        okHttpService = new OkHttpService(context);
    }

    //region Get Selected Pos
    public int getSelectedPos(List<CustomFranchiseeApplicationSpinnerDto> spinnerDtoList, String selectedValue) {

        if (TextUtils.isEmpty(selectedValue) || spinnerDtoList == null)
            return 0;

        for (int i = 0; i < spinnerDtoList.size(); i++) {
            if (selectedValue.equalsIgnoreCase(spinnerDtoList.get(i).getId()))
                return i;
        }
        return 0;
    }
    //endregion

    //region state list
    public List<CustomFranchiseeApplicationSpinnerDto> getAllStateBylList() {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_STATE;
            String data = okHttpService.getDataFromService(true, url);
            JSONArray jsonArray = new JSONArray(data);

            Gson gson = new GsonBuilder().create();
            list = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<CustomFranchiseeApplicationSpinnerDto>>() {
            }.getType());
            list.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    //endregion

    //region State list Using Pincode
    public List<CustomFranchiseeApplicationSpinnerDto> getAllStateBylListUsingPincode (String pinCode) {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_STATE_USING_PINCODE;
            url = url.replace("{pincode}", pinCode);
            String data = okHttpService.getDataFromService(true, url);

            JSONArray jsonArray = new JSONArray(data);

            Gson gson = new GsonBuilder().create();
            list = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<CustomFranchiseeApplicationSpinnerDto>>() {
            }.getType());
            list.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    //endregion

    //region District  list
    public List<CustomFranchiseeApplicationSpinnerDto> getAllDistrictBylList(String StateId) {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_DISTRICT;
            url = url.replace("{stateId}", StateId);
            String data = okHttpService.getDataFromService(true, url);

            //String data = "[{\"Id\":1,\"Name\":\"Central\"},{\"Id\":2,\"Name\":\"East\"},{\"Id\":3,\"Name\":\"New Delhi\"}]";

            JSONArray jsonArray = new JSONArray(data);

            Gson gson = new GsonBuilder().create();
            list = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<CustomFranchiseeApplicationSpinnerDto>>() {
            }.getType());
            list.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    //endregion

    //region VTC  list
    public List<CustomFranchiseeApplicationSpinnerDto> getAllVTCBylList(String stateId, String districtId) {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_VTC;
            url = url.replace("{stateId}", stateId);
            url = url.replace("{districtId}", districtId);
            String data = okHttpService.getDataFromService(true, url);

            //String data = "[{\"Id\":1,\"Name\":\"Chilla Saroda Bangar (Ct)-Preet Vihar\"},{\"Id\":2,\"Name\":\"Dallo Pura (Ct)-Preet Vihar\"},{\"Id\":3,\"Name\":\"Kondli (Ct)-Preet Vihar\"}]";

            JSONArray jsonArray = new JSONArray(data);

            Gson gson = new GsonBuilder().create();
            list = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<CustomFranchiseeApplicationSpinnerDto>>() {
            }.getType());
            list.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    //endregion

    //region Municipal Corporation List
    public List<CustomFranchiseeApplicationSpinnerDto> getMunicipalCorporationList(String vtcId) {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_MUNICIPAL_CORPORATION_USING_VTC_ID;
            url = url.replace("{villageId}", vtcId);
            String data = okHttpService.getDataFromService(true, url);

            JSONArray jsonArray = new JSONArray(data);

            Gson gson = new GsonBuilder().create();
            list = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<CustomFranchiseeApplicationSpinnerDto>>() {
            }.getType());
            list.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    //endregion

    //region Ward No List
    public List<CustomFranchiseeApplicationSpinnerDto> getWardNoList(String municipalCorporationId) {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_WARD_NO_LIST_USING_MUNICIPAL_CORPORATIONID;
            url = url.replace("{municipalCorpId}", municipalCorporationId);
            String data = okHttpService.getDataFromService(true, url);

            JSONArray jsonArray = new JSONArray(data);

            Gson gson = new GsonBuilder().create();
            list = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<CustomFranchiseeApplicationSpinnerDto>>() {
            }.getType());
            list.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    //endregion

    //region Check IsRural
    public String IsRural(String villageId) {

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_ISRURAL;
        url = url.replace("{villageId}", villageId);

        String data = okHttpService.getDataFromService(isCached, url);

        return data;
    }
    //endregion

    // region Get Address Details
    public String getUpdateAddressDetails(String enquiryId) {
        String data = null;
        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_UPDATE_ADDRESS_DETAILS;
            url = url.replace("{enquiryId}", enquiryId);

            data = okHttpService.getDataFromService(isCached, url);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    //endregion

    //region Post Address Details
    public String saveAddressDetail(String enquiryId, UpdateAddressDetailsDto updateAddressDetailsDto) {

        String data = null;
        try {
            Gson gson = new Gson();
            String jsonData = gson.toJson(updateAddressDetailsDto, UpdateAddressDetailsDto.class);

            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_SAVE_UPDATE_ADDRESS_DETAILS;
            url = url.replace("{enquiryId}", enquiryId);

            data = okHttpService.postDataToService(url, jsonData);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
    //endregion

    //region Premise Ownership Type List
    public List<CustomFranchiseeApplicationSpinnerDto> getPremiseOwnershipTypelList() {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_PREMISE_OWNERSHIP_TYPE;
            String data = okHttpService.getDataFromService(true, url);

            //String data = "[{\"Id\":1,\"Name\":\"Owned\"},{\"Id\":2,\"Name\":\"Rented\"}]";

            JSONArray jsonArray = new JSONArray(data);

            Gson gson = new GsonBuilder().create();
            list = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<CustomFranchiseeApplicationSpinnerDto>>() {
            }.getType());
            list.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    //endregion

    //region Owned By List
    public List<CustomFranchiseeApplicationSpinnerDto> getOwnedBylList() {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        try {

            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_OWNED_BY;
            String data = okHttpService.getDataFromService(true, url);

            //String data = "[{\"Id\":1,\"Name\":\"Self\"},{\"Id\":2,\"Name\":\"Family\"}]";

            JSONArray jsonArray = new JSONArray(data);

            Gson gson = new GsonBuilder().create();
            list = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<CustomFranchiseeApplicationSpinnerDto>>() {
            }.getType());
            list.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    //endregion

}


