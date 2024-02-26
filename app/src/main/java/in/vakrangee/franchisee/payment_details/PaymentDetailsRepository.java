package in.vakrangee.franchisee.payment_details;

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

public class PaymentDetailsRepository {

    private Context context;
    private OkHttpService okHttpService;
    private boolean isCached = false;
    private static final String VKID_ENQUIRYID_CONST = "{vkIdOrEnquiryId}";

    public PaymentDetailsRepository(Context mContext) {
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

    //region Get Existing Raise Dispute Detail
    public RaiseDisputeDto getExistingRaiseDisputeDetail(String vkIdOrEnquiryId) {
        RaiseDisputeDto raiseDisputeDto = null;

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_RAISE_DISPUTE_DETAIL;
        url = url.replace(VKID_ENQUIRYID_CONST, vkIdOrEnquiryId);
        String data = okHttpService.getDataFromService(isCached, url);
        try {
            if (TextUtils.isEmpty(data))
                return null;

            Gson gson = new GsonBuilder().create();
            List<RaiseDisputeDto> list = gson.fromJson(data, new TypeToken<ArrayList<RaiseDisputeDto>>() {
            }.getType());
            if (list.size() > 0)
                raiseDisputeDto = list.get(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return raiseDisputeDto;
    }
    //endregion

    //region Bank Name List
    public List<CustomFranchiseeApplicationSpinnerDto> getBankNameList() {
        List<CustomFranchiseeApplicationSpinnerDto> entityList = new ArrayList<>();

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_PAYMENT_BANK_NAME;
        String data = okHttpService.getDataFromService(true, url);

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

    //region Transfer Mode List
    public List<CustomFranchiseeApplicationSpinnerDto> getTransferModeList() {
        List<CustomFranchiseeApplicationSpinnerDto> entityList = new ArrayList<>();

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_TRANSFER_MODE_LIST;
        String data = okHttpService.getDataFromService(true, url);

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

    // region Get Payment Details
    public String getPaymentDetails(String vkIdEnquiryId) {
        String data = null;
        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_PAYMENT_DETAIL;
            url = url.replace(VKID_ENQUIRYID_CONST, vkIdEnquiryId);

            data = okHttpService.getDataFromService(isCached, url);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    //endregion

    // region Get Raised Dispute Details
    public String getRaisedDisputeDetails(String vkIdEnquiryId) {
        String data = null;
        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_ALL_RAISED_DISPUTE_DETAIL;
            url = url.replace(VKID_ENQUIRYID_CONST, vkIdEnquiryId);

            data = okHttpService.getDataFromService(isCached, url);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    //endregion

    //region Post RaiseDispute Details
    public String saveRaiseDisputeDetail(String vkIdOrEnquiryId, RaiseDisputeDto raiseDisputeDto) {

        String data = null;
        try {
            Gson gson = new Gson();
            String jsonData = gson.toJson(raiseDisputeDto, RaiseDisputeDto.class);

            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_SAVE_RAISE_DISPUTE_DETAILS;
            url = url.replace(VKID_ENQUIRYID_CONST, vkIdOrEnquiryId);

            data = okHttpService.postDataToService(url, jsonData);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
    //endregion

}
