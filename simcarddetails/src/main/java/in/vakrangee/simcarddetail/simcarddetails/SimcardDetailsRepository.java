package in.vakrangee.simcarddetail.simcarddetails;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import in.vakrangee.supercore.franchisee.nextgenfranchiseeapplication.CustomFranchiseeApplicationSpinnerDto;
import in.vakrangee.supercore.franchisee.service.OkHttpService;
import in.vakrangee.supercore.franchisee.utils.Constants;

public class SimcardDetailsRepository {


    private Context context;
    private OkHttpService okHttpService;
    private boolean IsCached = false;

    public SimcardDetailsRepository(Context context) {
        this.context = context;
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

    //region simcard company list
    public List<CustomFranchiseeApplicationSpinnerDto> getSimcardComapanyName(String vkIdOrEnquiryId) {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();

        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_SIMCARD_COMPANY_LIST;
            url = url.replace("{vkIdOrEnquiryId}", vkIdOrEnquiryId);
            String data = okHttpService.getDataFromService(IsCached, url);

            // data = "[{\"Id\":1,\"Name\":\"Airtel\"}]";
            //JSONArray jsonArray = new JSONArray(data);
            Gson gson = new GsonBuilder().create();

            if (data.startsWith("OKAY|")) {
                data = data.replace("OKAY|", "");
                list = gson.fromJson(data, new TypeToken<ArrayList<CustomFranchiseeApplicationSpinnerDto>>() {
                }.getType());
                // list.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    //endregion

    //region Get all simcard details data
    public String getSimcardDetailsData(String vkIdOrEnquiryId, String ATMID) {

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_SIMCARD_DETAILS;
        url = url.replace("{vkIdOrEnquiryId}", vkIdOrEnquiryId);
        url = url.replace("{atmId}", ATMID);
        String data = okHttpService.getDataFromService(IsCached, url);
        //data = "OKAY|{\"IsEditable\":\"1\",\"service_provide\":\"1\",\"simcard_cover_photo\":\"\",\"simcard_cover_photo_ext\":\"jpg\",\"simcard_imsi_number\":\"858855895959595\",\"simcard_number\":\"Vvdddvvdvdvvdvddvggg\",\"simcard_photo_ext\":\"jpg\",\"simcard_qrcode\":\"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?> <PrintLetterBarcodeData simNo=\\\"8991921901932104448U\\\" imsiNo=\\\"404920193210444\\\"mobileNo=\\\"\\\"/>\",\"simcard_sim_photo\":\"\",\"simcard_cover_photo_id\":\"1\",\"simcard_photo_id\":\"1140\",\"simcard_qrcode_photo_id\":\"1\"}";
        return data;
    }
    //endregion


    //region Post simcard details save
    public String saveSimcardDetails(String vkIdOrEnquiryId, String jsonData) {

        String data = null;
        try {

            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_SAVE_SIMCARD_DETAIL;
            url = url.replace("{vkIdOrEnquiryId}", vkIdOrEnquiryId);

            data = okHttpService.postDataToService(url, jsonData);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    //endregion

    //region simcard company list
    public List<CustomFranchiseeApplicationSpinnerDto> getATMServiceProviderList(String vkIdOrEnquiryId) {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();

        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_ATM_LIST;
            url = url.replace("{vkIdOrEnquiryId}", vkIdOrEnquiryId);
            String data = okHttpService.getDataFromService(IsCached, url);

            // data = "[{\"Id\":1,\"Name\":\"Airtel\"}]";
            //JSONArray jsonArray = new JSONArray(data);
            Gson gson = new GsonBuilder().create();

            if (data.startsWith("OKAY|")) {
                data = data.replace("OKAY|", "");
                list = gson.fromJson(data, new TypeToken<ArrayList<CustomFranchiseeApplicationSpinnerDto>>() {
                }.getType());
                // list.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    //endregion


}
