package in.vakrangee.franchisee.atmloading;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import in.vakrangee.franchisee.atmloading.cash_loading.ATMCashLoadingDto;
import in.vakrangee.franchisee.atmloading.cash_sourcing.ATMCashSourcingDto;
import in.vakrangee.supercore.franchisee.nextgenfranchiseeapplication.CustomFranchiseeApplicationSpinnerDto;
import in.vakrangee.supercore.franchisee.service.OkHttpService;
import in.vakrangee.supercore.franchisee.utils.Constants;

public class ATMLoadingRepository {

    private Context context;
    private static final String TAG = "ATMLoadingRepository";
    private OkHttpService okHttpService;
    private boolean IsCached = false;


    public ATMLoadingRepository(Context context) {
        this.context = context;
        okHttpService = new OkHttpService(context);
    }

    /**
     * Get Default Choose Data from Selected List.
     *
     * @param chooserList
     * @return
     */
    public int getDefaultOrSelectedChooserData(List<CustomFranchiseeApplicationSpinnerDto> chooserList, String selectedId) {

        if (chooserList != null) {
            for (int i = 0; i < chooserList.size(); i++) {

                CustomFranchiseeApplicationSpinnerDto spinnerDto = chooserList.get(i);
                String isDefault = spinnerDto.getIsDefault();
                String id = spinnerDto.getId();

                if (TextUtils.isEmpty(selectedId)) {
                    if (!TextUtils.isEmpty(isDefault) && isDefault.equalsIgnoreCase("1")) {
                        return i;
                    }
                } else {
                    if (id.equalsIgnoreCase(selectedId))
                        return i;
                }
            }
        }
        return 0;
    }


    //region Get ATM cash physical or cash loading details  - physical TYPE 1  / ATM Cashloading TYPE -2
    public String getATMPhysicalORCashLoadingDetails(String VKIDOrEnquiryId, String cashLodingId) {
        String data = null;
        try {
            VKIDOrEnquiryId = "MH1801014";
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_GET_ATM_PHYSICAL_CASH_LOADING;
            url = url.replace("{vkIdOrEnquiryId}", VKIDOrEnquiryId);
            url = url.replace("{cashLodingId}", cashLodingId);
            data = okHttpService.getDataFromService(true, url);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    //endregion


    //region save ATM physical loading - TYPE 1  / ATM Cashloading TYPE -2
    public String saveATMLodingData(String vkid, String type, String jsonData) {
        String data = null;
        try {
            /*if (TextUtils.isEmpty(jsonData)) {
                jsonData = "{}";
            }*/
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_SAVE_ATM_PHYSICAL_CASH_LOADING;
            url = url.replace("{VKIDOrEnquiryId}", vkid);
            url = url.replace("{type}", type);
            data = okHttpService.postDataToService(url, jsonData);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }


    //endregion

    //region ATM ID  list
    public List<CustomFranchiseeApplicationSpinnerDto> getATMIDList(String VKID) {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        try {
            VKID = "MH1801014";
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_GET_ATM_ID_DETAILS_LIST;
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

    //region Status ID  list
    public List<CustomFranchiseeApplicationSpinnerDto> getStatusList(String VKID) {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_GET_STATUS_ID_DETAILS_LIST;
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

    //region Get ATM Ro cash details
    public String getATMROCashDetails(String VKIDOrEnquiryId, String atmId, String statusId) {
        String data = null;
        try {
            VKIDOrEnquiryId = "MH1801014";
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_GET_ATM_RO_CASH_LOADING_DATA;
            url = url.replace("{vkIdOrEnquiryId}", VKIDOrEnquiryId);
            url = url.replace("{atmId}", atmId);
            url = url.replace("{statusId}", statusId);
            data = okHttpService.getDataFromService(true, url);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    //endregion

    //region All ATM list
    public List<CustomFranchiseeApplicationSpinnerDto> getAllATMList(String VKID) {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_GET_ALL_ATM_LIST;
            url = url.replace("{VKID}", VKID);
            String data = okHttpService.getDataFromService(IsCached, url);
            JSONArray jsonArray = new JSONArray(data);

            Gson gson = new GsonBuilder().create();
            list = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<CustomFranchiseeApplicationSpinnerDto>>() {
            }.getType());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    //endregion

    //region Cash Sourcing Status list
    public List<CustomFranchiseeApplicationSpinnerDto> getCashSourcingStatusList(String VKID) {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_GET_ATM_CASH_SOURCING_STATUS_DETAILS;
            url = url.replace("{VKID}", VKID);
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

    //region Get All ATM Cash Sourcing List
    public String getAllATMCashSourcingList(String VKID) {
        String data = null;
        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_GET_ALL_ATM_CASH_SOURCING_DETAILS;
            url = url.replace("{VKID}", VKID);
            data = okHttpService.getDataFromService(IsCached, url);
            //data = "OKAY|{\"cash_sourcing_details\":[{\"source_date\":\"11 Feb 2021\",\"status\":\"Uploaded\",\"status_code\":\"1\",\"cash_sourcing_id\":\"20181122210392\",\"cash_sourcing_scan_copy_id\":\"1\",\"cash_sourcing_ext\":\"pdf\",\"IsEditable\":\"1\"},{\"source_date\":\"12 Feb 2021\",\"status\":\"Cancelled\",\"status_code\":\"0\",\"cash_sourcing_id\":\"2021112221042\",\"cash_sourcing_scan_copy_id\":\"2\",\"cash_sourcing_ext\":\"pdf\",\"IsEditable\":\"1\"},{\"source_date\":\"10 Feb 2021\",\"status\":\"Uploaded\",\"status_code\":\"1\",\"cash_sourcing_id\":\"2019112221076\",\"cash_sourcing_scan_copy_id\":\"3\",\"cash_sourcing_ext\":\"pdf\",\"IsEditable\":\"1\"}]}";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    //endregion

    //region Post ATM Cash Sourcing Details
    public String saveATMCashSourcingDetail(String vkId, ATMCashSourcingDto atmCashSourcingDto) {

        String data = null;
        try {
            Gson gson = new Gson();
            String jsonData = gson.toJson(atmCashSourcingDto, ATMCashSourcingDto.class);

            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_SAVE_ATM_CASH_SOURCING_DETAILS;
            url = url.replace("{VKID}", vkId);

            data = okHttpService.postDataToService(url, jsonData);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
    //endregion

    //region Get All ATM Cash Loading List
    public String getAllATMCashLoadingList(String VKID, String atmID, String status) {
        String data = null;
        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_GET_ALL_ATM_CASH_LOADING_DETAILS;
            url = url.replace("{VKID}", VKID);
            url = url.replace("{atmID}", atmID);
            url = url.replace("{status}", status);

            data = okHttpService.getDataFromService(IsCached, url);
            //data = "OKAY|{\"cash_loading_details\":[{\"source_date\":\"11 Feb 2021\",\"status\":\"Uploaded\",\"status_code\":\"1\",\"cash_sourcing_id\":\"20181122210392\",\"cash_sourcing_scan_copy_id\":\"1\",\"cash_sourcing_ext\":\"pdf\",\"IsEditable\":\"1\"},{\"source_date\":\"12 Feb 2021\",\"status\":\"Cancelled\",\"status_code\":\"0\",\"cash_sourcing_id\":\"2021112221042\",\"cash_sourcing_scan_copy_id\":\"2\",\"cash_sourcing_ext\":\"pdf\",\"IsEditable\":\"1\"},{\"source_date\":\"10 Feb 2021\",\"status\":\"Uploaded\",\"status_code\":\"1\",\"cash_sourcing_id\":\"2019112221076\",\"cash_sourcing_scan_copy_id\":\"3\",\"cash_sourcing_ext\":\"pdf\",\"IsEditable\":\"1\"}]}";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    //endregion

    //region Cash Loading Status list
    public List<CustomFranchiseeApplicationSpinnerDto> getCashLoadingStatusList(String VKID) {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_GET_ATM_CASH_LOADING_STATUS_DETAILS;
            url = url.replace("{VKID}", VKID);
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

    //region Get Specific ATM Cash Loading Details
    public String getSpecificATMCashLoadingDetails(String VKID, String atmCashLoadingID) {
        String data = null;
        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_GET_SPECIFIC_ATM_CASH_LOADING_DETAILS;
            url = url.replace("{VKID}", VKID);
            url = url.replace("{atmCashLoadingID}", atmCashLoadingID);

            data = okHttpService.getDataFromService(IsCached, url);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    //endregion

    //region Post ATM Cash Loading Details
    public String saveATMCashLoadingDetail(String vkId, ATMCashLoadingDto atmCashLoadingDto) {

        String data = null;
        try {
            Gson gson = new Gson();
            String jsonData = gson.toJson(atmCashLoadingDto, ATMCashLoadingDto.class);

            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_SAVE_ATM_CASH_LOADING_DETAILS;
            url = url.replace("{VKID}", vkId);

            data = okHttpService.postDataToService(url, jsonData);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
    //endregion

}
