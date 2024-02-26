package in.vakrangee.franchisee.bcadetails;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import in.vakrangee.franchisee.bcadetails.pre.PreBCADetailsDto;
import in.vakrangee.supercore.franchisee.nextgenfranchiseeapplication.CustomFranchiseeApplicationSpinnerDto;
import in.vakrangee.supercore.franchisee.service.OkHttpService;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Constants;

public class BCAEntryDetailsRepository {

    private Context context;
    private OkHttpService okHttpService;
    private boolean IsCached = false;

    public BCAEntryDetailsRepository(Context context) {
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

    //region Get PRE BCA Entry Details by EnquiryId
    public String getPreBCAEntryDetailsByEnquiryId(String enquiryId) {

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_PRE_BCA_ENTRY_DETAILS;
        url = url.replace("{enquiryId}", enquiryId);
        String data = okHttpService.getDataFromService(IsCached, url);

        return data;
    }
    //endregion

    //region Get BCA Entry Details by VKID
    public String getBCAEntryDetailsByVKId(String VKID) {

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_BCA_ENTRY_DETAILS;
        url = url.replace("{VKID}", VKID);
        String data = okHttpService.getDataFromService(IsCached, url);

        return data;
    }
    //endregion

    //region Bank Name List
    public List<CustomFranchiseeApplicationSpinnerDto> getBankNameList() {
        List<CustomFranchiseeApplicationSpinnerDto> entityList = new ArrayList<>();

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_BCA_BANK_NAME;
        String data = okHttpService.getDataFromService(true, url);
        //String data = "[{\"Id\":1,\"Name\":\"Abhyudaya Co-Operative Bank\"},{\"Id\":2,\"Name\":\"Adarniya P.D. Patilsaheb Sahakari Bank Ltd.\"},{\"Id\":3,\"Name\":\"Allahabad Bank\"},{\"Id\":4,\"Name\":\"Bandhan Bank\"},{\"Id\":5,\"Name\":\"City Union Bank Limited\"}]";

        try {
            JSONArray jsonArray = new JSONArray(data);

            Gson gson = new GsonBuilder().create();
            entityList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<CustomFranchiseeApplicationSpinnerDto>>() {
            }.getType());
            entityList.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return entityList;
    }
    //endregion

    //region Salutation List
    public List<CustomFranchiseeApplicationSpinnerDto> getSalutationList() {
        List<CustomFranchiseeApplicationSpinnerDto> entityList = new ArrayList<>();

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_BCA_SALUTATION;
        String data = okHttpService.getDataFromService(true, url);

        try {
            JSONArray jsonArray = new JSONArray(data);

            Gson gson = new GsonBuilder().create();
            entityList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<CustomFranchiseeApplicationSpinnerDto>>() {
            }.getType());
            //entityList.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return entityList;
    }
    //endregion

    //region Qualification List
    public List<CustomFranchiseeApplicationSpinnerDto> getQualificationList() {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_BCA_QUALIFICATION;
        String data = okHttpService.getDataFromService(true, url);
        //String data = "[{\"Id\":1,\"Name\":\"Abhyudaya Co-Operative Bank\"},{\"Id\":2,\"Name\":\"Adarniya P.D. Patilsaheb Sahakari Bank Ltd.\"},{\"Id\":3,\"Name\":\"Allahabad Bank\"},{\"Id\":4,\"Name\":\"Bandhan Bank\"},{\"Id\":5,\"Name\":\"City Union Bank Limited\"}]";

        try {
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

    //region New Qualification List
    public List<CustomFranchiseeApplicationSpinnerDto> getNewQualificationList() {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_BCA_NEW_QUALIFICATION;
        String data = okHttpService.getDataFromService(true, url);

        try {
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

    //region Religion List
    public List<CustomFranchiseeApplicationSpinnerDto> getReligionList() {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_BCA_RELIGION;
        String data = okHttpService.getDataFromService(true, url);
        //String data = "[{\"Id\":1,\"Name\":\"Abhyudaya Co-Operative Bank\"},{\"Id\":2,\"Name\":\"Adarniya P.D. Patilsaheb Sahakari Bank Ltd.\"},{\"Id\":3,\"Name\":\"Allahabad Bank\"},{\"Id\":4,\"Name\":\"Bandhan Bank\"},{\"Id\":5,\"Name\":\"City Union Bank Limited\"}]";

        try {
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

    //region BC Ability List
    public List<CustomFranchiseeApplicationSpinnerDto> getBCAbilityList() {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_BCA_BC_ABILITY;
        String data = okHttpService.getDataFromService(true, url);
        //String data = "[{\"Id\":1,\"Name\":\"Abhyudaya Co-Operative Bank\"},{\"Id\":2,\"Name\":\"Adarniya P.D. Patilsaheb Sahakari Bank Ltd.\"},{\"Id\":3,\"Name\":\"Allahabad Bank\"},{\"Id\":4,\"Name\":\"Bandhan Bank\"},{\"Id\":5,\"Name\":\"City Union Bank Limited\"}]";

        try {
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

    //region Category List
    public List<CustomFranchiseeApplicationSpinnerDto> getCategoryList() {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_BCA_CATEGORY;
        String data = okHttpService.getDataFromService(true, url);
        //String data = "[{\"Id\":1,\"Name\":\"Abhyudaya Co-Operative Bank\"},{\"Id\":2,\"Name\":\"Adarniya P.D. Patilsaheb Sahakari Bank Ltd.\"},{\"Id\":3,\"Name\":\"Allahabad Bank\"},{\"Id\":4,\"Name\":\"Bandhan Bank\"},{\"Id\":5,\"Name\":\"City Union Bank Limited\"}]";

        try {
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

    //region State list
    public List<CustomFranchiseeApplicationSpinnerDto> getStateList() {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_BCA_STATE;
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

    //region Division  list
    public List<CustomFranchiseeApplicationSpinnerDto> getDivisionList(String StateId) {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_BCA_DIVISION;
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

    //region District  list
    public List<CustomFranchiseeApplicationSpinnerDto> getDistrictList(String StateId) {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_BCA_DISTRICT;
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

    //region Block list
    public List<CustomFranchiseeApplicationSpinnerDto> getBlockList(String StateId, String districtId) {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_BCA_BLOCK;
            url = url.replace("{stateId}", StateId);
            url = url.replace("{districtId}", districtId);
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

    //region VTC list
    public List<CustomFranchiseeApplicationSpinnerDto> getVTCList(String stateId, String districtId, String blockId) {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_BCA_VTC;
            url = url.replace("{stateId}", stateId);
            url = url.replace("{districtId}", districtId);
            url = url.replace("{blockId}", districtId);
            String data = okHttpService.getDataFromService(true, url);

            //JSONArray jsonArray = new JSONArray(data);

            Gson gson = new GsonBuilder().create();
            list = gson.fromJson(data, new TypeToken<ArrayList<CustomFranchiseeApplicationSpinnerDto>>() {
            }.getType());
            list.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    //endregion

    //region Ward list
    public List<CustomFranchiseeApplicationSpinnerDto> getWardList(String vtcId) {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_BCA_WARD;
            url = url.replace("{vtcId}", vtcId);
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

    //region Supervisor Code List
    public List<CustomFranchiseeApplicationSpinnerDto> getSupervisorCodeList() {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_BCA_SUPERVISOR_CODE;
        String data = okHttpService.getDataFromService(true, url);

        try {
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

    //region Device Type List
    public List<CustomFranchiseeApplicationSpinnerDto> getDeviceTypeList() {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_BCA_DEVICE_TYPE;
        String data = okHttpService.getDataFromService(true, url);

        try {
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

    //region Post BCA Entry detail
    public String saveBCAEntryDetail(String vkId, String type, BCAEntryDetailsDto bcaEntryDetailsDto) {

        String data = null;
        try {
            Gson gson = new Gson();
            String jsonData = gson.toJson(bcaEntryDetailsDto, BCAEntryDetailsDto.class);

            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_SAVE_BCA_ENTRY_DETAIL;
            url = url.replace("{vklId}", bcaEntryDetailsDto.getVklId());
            url = url.replace("{vkId}", vkId);
            url = url.replace("{type}", type);

            data = okHttpService.postDataToService(url, jsonData);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
    //endregion

    //region Bank Name List Using State and District
    public List<CustomFranchiseeApplicationSpinnerDto> getBankNameUsingStateDistrictList(String stateId, String districtId) {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();
        String enquiryId = CommonUtils.getEnquiryId(context);

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_BCA_BANK_NAME_USING_STATE_DISTRICT;
        url = url.replace("{enquiryId}", enquiryId);
        url = url.replace("{stateId}", stateId);
        url = url.replace("{districtId}", districtId);
        String data = okHttpService.getDataFromService(true, url);

        try {
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

    //region Branch Name List Using State and District
    public List<CustomFranchiseeApplicationSpinnerDto> getBranchNameUsingStateDistrictList(String bankId, String stateId, String districtId) {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();
        String enquiryId = CommonUtils.getEnquiryId(context);

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_BCA_BRANCH_NAME_USING_STATE_DISTRICT;
        url = url.replace("{enquiryId}", enquiryId);
        url = url.replace("{bankId}", bankId);
        url = url.replace("{stateId}", stateId);
        url = url.replace("{districtId}", districtId);
        String data = okHttpService.getDataFromService(true, url);

        try {
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

    //region Approx Bank Distance List
    public List<CustomFranchiseeApplicationSpinnerDto> getApproxBankDistanceList() {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        list.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));
        for (int i = 1; i <= 99; i++) {
            list.add(i, new CustomFranchiseeApplicationSpinnerDto(String.valueOf(i), String.valueOf(i)));
        }
        return list;
    }
    //endregion

    //region Post BCA detail
    public String saveBCADetail(String vkId, String enquiryId, PreBCADetailsDto preBCADetailsDto) {

        String data = null;
        try {
            Gson gson = new Gson();
            String jsonData = gson.toJson(preBCADetailsDto, PreBCADetailsDto.class);

            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_SAVE_BCA_DETAIL;
            //url = url.replace("{vkId}", vkId);
            url = url.replace("{enquiryId}", enquiryId);

            data = okHttpService.postDataToService(url, jsonData);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
    //endregion

    //region Validate Mobile No by EnquiryId
    public String validateMobileNo(String enquiryId, String mobileNo) {

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_BCA_VALIDATE_MOB_NO_USING_OTP;
        url = url.replace("{enquiryId}", enquiryId);
        url = url.replace("{mobile_number}", mobileNo);
        String data = okHttpService.getDataFromService(IsCached, url);

        return data;
    }
    //endregion

    //region get verify Pan Card for BCA Detail
    public String verifyPanCardForBCADetail(String enquiryId, String pan_card_no, String pan_card_name) {

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_VERIFY_PANCARD_FOR_BCA_DETAIL;
        url = url.replace("{enquiryId}", enquiryId);
        url = url.replace("{pan_card_no}", pan_card_no);
        url = url.replace("{pan_card_name}", pan_card_name);

        String data = okHttpService.getDataFromService(IsCached, url);

        return data;
    }
    //endregion

}
