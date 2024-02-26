package in.vakrangee.franchisee.franchiseelogin;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.vakrangee.supercore.franchisee.application.VakrangeeKendraApplication;
import in.vakrangee.supercore.franchisee.service.OkHttpService;
import in.vakrangee.supercore.franchisee.utils.Constants;

public class FranchiseeAuthenticationRepository {

    private Context context;
    private OkHttpService okHttpService;
    private boolean IsCached = false;
    private VakrangeeKendraApplication vkApp;

    public FranchiseeAuthenticationRepository(Context context) {
        this.context = context;
        okHttpService = new OkHttpService(context);
        vkApp = (VakrangeeKendraApplication) context.getApplicationContext();
    }

    //region Validate User using Mobile No/Email Id
    public String validateUser(String mobEmailId) {
        String data = null;
        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_VALIDATE_MOB_EMAILID;
            url = url.replace("{MOB_EMAIL}", mobEmailId);

            data = okHttpService.getDataFromService(IsCached, url);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;

    }
    //endregion

    //region Update FCMID via EnquiryId
    public String updateFCMIdViaEnquiryId(String enquiryId, String fcmId) {
        String data = null;
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("enquiryId", enquiryId);
            jsonObject.put("fcmId", fcmId);
            String jsonData = jsonObject.toString();


            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_UPDATE_FCMID_VIA_ENQUIRY_ID;

            data = okHttpService.postDataToService(url, jsonData);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    //endregion

    //region Get All Franchisee Enquiry List
    public List<FranchiseeEnquiryDto> getGetAllFranchiseeEnquiryList(String mobEmailId) {
        List<FranchiseeEnquiryDto> enquiryList = new ArrayList<FranchiseeEnquiryDto>();
        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_ALL_FRANCHISEE_ENQUIRY_LIST;
            //String url = "https://vkms.vakrangee.in/WSFranchiseeApp/FLM/getAllFranchiseeEnquiryListByMobEmailID/9930300764";
            url = url.replace("{MOB_EMAIL}", mobEmailId);
            String data = okHttpService.getDataFromService(IsCached, url);

            Gson gson = new GsonBuilder().create();
            enquiryList = gson.fromJson(data, new TypeToken<ArrayList<FranchiseeEnquiryDto>>() {
            }.getType());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return enquiryList;
    }
    //endregion

    //region Validate User using Mobile No/Email Id
    public String postFormData(Map<String, String> formField) {
        String data = null;
        try {
            Map<String, String> mapData = new HashMap<String, String>();
            mapData.put("username", "vasundhara");
            mapData.put("pass", "password");

            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + "create";

            data = okHttpService.postDataInFormToService(url, formField);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    //endregion

    //region Get VKID by EnquiryId
    public String getVKIDByEnquiryId(String EnquiryId) {
        String data = null;
        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_VKID_BY_ENQUIRY_ID;
            url = url.replace("{ENQUIRY_ID}", EnquiryId);

            data = okHttpService.getDataFromService(IsCached, url);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    //endregion

    //region Check Version
    public String checkVersion() {
        String data = null;
        try {
            String version = vkApp.getAppVersion();
             String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_CHECK_FRANCHISEE_VERSION;
             url = url.replace("{PRODUCT_CODE}", Constants.PRODUCT_CODE);
            url = url.replace("{VERSION}", version);

            data = okHttpService.getDataFromService(IsCached, url);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    //endregion

}
