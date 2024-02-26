package in.vakrangee.franchisee.agreement_dispatch;

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

public class AgreementDispatchRepository {

    private static final String TAG = AgreementDispatchRepository.class.getSimpleName();
    private Context context;
    private OkHttpService okHttpService;
    private boolean IsCached = false;

    public AgreementDispatchRepository(Context context) {
        this.context = context;
        okHttpService = new OkHttpService(context);
    }


    //region get Selected Pos in spinner
    public int getSelectedPos(List<CustomFranchiseeApplicationSpinnerDto> spinnerDtoList, String selectedValue) {

        if (TextUtils.isEmpty(selectedValue))
            return 0;

        for (int i = 0; i < spinnerDtoList.size(); i++) {
            if (selectedValue.equalsIgnoreCase(spinnerDtoList.get(i).getId()))
                return i;
        }
        return 0;
    }
    //endregion


    //region Agreement Courier  Type List
    public List<CustomFranchiseeApplicationSpinnerDto> getAgreementTypeList() {
        List<CustomFranchiseeApplicationSpinnerDto> serviceList = new ArrayList<>();
        // String data = "[{\"Id\":1,\"Name\":\"Hand Delivery\"},{\"Id\":2,\"Name\":\"Courier\"}]";

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_DISPATCH_TYPE;
        // url = url.replace("{vkId}", vkid);
        //url = url.replace("{collateralId}", collateralType);
        String data = okHttpService.getDataFromService(true, url); // Cached for 2 hrs
        try {
            JSONArray jsonArray = new JSONArray(data);

            Gson gson = new GsonBuilder().create();
            serviceList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<CustomFranchiseeApplicationSpinnerDto>>() {
            }.getType());
            // serviceList.add(0, new CustomFranchiseeApplicationSpinnerDto("-1", "Please Select"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return serviceList;
    }

    //endregion


    //region save Agreement Dispatch  Details type
    public String saveAgreementDispatchData(String vkid, String type, String jsonData) {

        String data = null;
        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_SAVE_AGREEMENT_DISPATCH;
            url = url.replace("{VKIDOrEnquiryId}", vkid);
            url = url.replace("{type}", type);
            data = okHttpService.postDataToService(url, jsonData);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
    //endregion


    // region getAgreementDispatchDetails/{vkIdEnquiryId}
    public String getAgreementDispatchDetails(String VKIDOrEnquiryId) {
        String data = null;
        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_AGREMENT_DISPATCH_DETAILS;
            url = url.replace("{VKIDOrEnquiryId}", VKIDOrEnquiryId);
            data = okHttpService.getDataFromService(IsCached, url);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    //endregion


    // region getHardwareTrackingDetails {vkIdEnquiryId}
    public String getHardwareTrackingDetails(String VKIDOrEnquiryId, String trackingId) {
        String data = null;
        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_GET_HARDWARE_TRACKING_DETAILS;
            url = url.replace("{VKIDOrEnquiryId}", VKIDOrEnquiryId);
            url = url.replace("{TrackingId}", trackingId);
            data = okHttpService.getDataFromService(IsCached, url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    //endregion

}
