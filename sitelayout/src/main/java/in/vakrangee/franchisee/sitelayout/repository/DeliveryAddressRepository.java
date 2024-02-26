package in.vakrangee.franchisee.sitelayout.repository;

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

public class DeliveryAddressRepository {

    private Context context;
    private static final String TAG = "DeliveryAddressRepository";
    private OkHttpService okHttpService;
    private boolean IsCached = false;


    public DeliveryAddressRepository(Context context) {
        this.context = context;
        okHttpService = new OkHttpService(context);
    }

    //region post data -Address
    public String saveDeliveryAddress(String vkid, String jsonData) {
        String data = null;
        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_POST_DELIVEY_DETAILS;
            url = url.replace("{vkIdOrEnquiryId}", vkid);
            data = okHttpService.postDataToService(url, jsonData);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
    //endregion

    //region get delivery data
    public String getDeliveryData(String vkId, String addressType) {

        String url;
        if (TextUtils.isEmpty(addressType)) {
            url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_DELIVEY_DETAILS;
        } else {
            url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_DELIVEY_DETAILS_1;
            url = url.replace("{addressType}", addressType);
        }
        url = url.replace("{vkIdOrEnquiryId}", vkId);
        String data = okHttpService.getDataFromService(IsCached, url);


      /*  String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_DELIVEY_DETAILS;
        url = url.replace("{vkIdOrEnquiryId}", vkId);
        String data = okHttpService.getDataFromService(IsCached, url);*/

        return data;
    }
    //endregion


    //region state list
    public List<CustomFranchiseeApplicationSpinnerDto> getAllStateBylList(String VKID) {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_STATE_LIST_DELIVERY;
            url = url.replace("{vkIdOrEnquiryId}", VKID);
            String data = okHttpService.getDataFromService(IsCached, url);
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
    public List<CustomFranchiseeApplicationSpinnerDto> getAllDistrictBylList(String VKID, String StateId) {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_DISTRICT_LIST_DELIVERY;
            url = url.replace("{vkIdOrEnquiryId}", VKID);
            url = url.replace("{stateId}", StateId);
            String data = okHttpService.getDataFromService(IsCached, url);

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
    public List<CustomFranchiseeApplicationSpinnerDto> getAllVTCBylList(String VKID, String stateId, String districtId) {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_VTC_LIST_DELIVERY;
            url = url.replace("{vkIdOrEnquiryId}", VKID);
            url = url.replace("{stateId}", stateId);
            url = url.replace("{districtId}", districtId);
            String data = okHttpService.getDataFromService(IsCached, url);

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

    //region in Drawer item allowed
    public String getFranchiseeAppMenuDetail(String vkIdOrEnquiryId) {
        String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_FRANCHISEE_APP_MENU_CONTROLLER;
        url = url.replace("{vkIdOrEnquiryId}", vkIdOrEnquiryId);
        String data = okHttpService.getDataFromService(IsCached, url);
        return data;
    }
    //endregion


    //region VTC  list
    public List<CustomFranchiseeApplicationSpinnerDto> getDeliveryTypeList(String VKID) {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_DELIVEY_ADDRESS_TYPE;
            url = url.replace("{vkIdOrEnquiryId}", VKID);
            String data = okHttpService.getDataFromService(IsCached, url);
            //data="[{\"name\":\"Propose Vakrangeee Kendra Address\",\"id\":\"1\"},{\"name\":\"Communication Address\",\"id\":\"2\"},{\"name\":\"Permanent Address\",\"id\":\"3\"},{\"name\":\"Other\",\"id\":\"4\"}]";

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

    //region get delivery data
    public String getHardwareDeliveryDetails(String enquiryId, String spinnerStatusType) {
        String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_DELIVEY_DETAILS;
        url = url.replace("{vkIdOrEnquiryId}", enquiryId);
        String data = okHttpService.getDataFromService(IsCached, url);

        //data = "OKAY|Vakrangee House , Plot -93 , Road number -123 , Andheri -East , MIDC , Mumbai -4000093";
        return data;
    }
    //endregion


    //region get payment hsitory details data
    public String getPaymentHistoryDetails(String enquiryId) {
        String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_PAY_HISTORY_DETAILS;
        url = url.replace("{vkIdOrEnquiryId}", enquiryId);
        String data = okHttpService.getDataFromService(IsCached, url);

        //data = "OKAY|Vakrangee House , Plot -93 , Road number -123 , Andheri -East , MIDC , Mumbai -4000093";
        return data;
    }
    //endregion

}
