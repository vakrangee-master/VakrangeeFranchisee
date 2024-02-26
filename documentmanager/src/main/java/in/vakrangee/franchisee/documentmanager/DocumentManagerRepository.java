package in.vakrangee.franchisee.documentmanager;

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

public class DocumentManagerRepository {

    private Context context;
    private OkHttpService okHttpService;
    private boolean IsCached = false;

    public DocumentManagerRepository(Context context) {
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

    //region Document Type List
    public List<CustomFranchiseeApplicationSpinnerDto> getDocumentTypeList(String vkIdOrEnquiryId) {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();

        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_DOCUMENT_TYPE_DETAILS;
            url = url.replace("{vkIdOrEnquiryId}", vkIdOrEnquiryId);
            String data = okHttpService.getDataFromService(IsCached, url);

            //data = "[{\"Id\":1,\"Name\":\"GSTIN Document\"},{\"Id\":2,\"Name\":\"PAN Card\"}]";
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

    //region Get Document Manager Details by Document Type
    public String getDocumentDetailsByType(String vkIdOrEnquiryId, String type) {
        //vkIdOrEnquiryId="UP5410036";
        String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_DOCUMENT_DETAILS_BY_TYPE;
        url = url.replace("{vkIdOrEnquiryId}", vkIdOrEnquiryId);
        url = url.replace("{type}", type);
        String data = okHttpService.getDataFromService(IsCached, url);

       /* if (type.equalsIgnoreCase("4")) {
            data = "OKAY|[{\"iibf_certificate_img_ext\":\".jpg\",\"bca_code\":\"40001857\",\"IsEditable\":1,\"iibf_certificate_scan_copy_id\":46866,\"membership_number\":\"ABCD12345\",\"certificate_number\":\"12345ZT\",\"date_of_examination\":\"1993-06-14\",\"iibf_certificate_scan_base64\":\"\"}]";
        }*/
        return data;
    }
    //endregion

    //region State list
    public List<CustomFranchiseeApplicationSpinnerDto> getStateList(String vkIdOrEnquiryId) {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_DOCUMENT_STATE;
            url = url.replace("{vkIdOrEnquiryId}", vkIdOrEnquiryId);

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

    //region District  list
    public List<CustomFranchiseeApplicationSpinnerDto> getDistrictList(String vkIdOrEnquiryId, String StateId) {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_DOCUMENT_DISTRICT;
            url = url.replace("{vkIdOrEnquiryId}", vkIdOrEnquiryId);
            url = url.replace("{stateId}", StateId);
            String data = okHttpService.getDataFromService(true, url);

            //String data = "[{\"Id\":1,\"Name\":\"Central\"},{\"Id\":2,\"Name\":\"East\"},{\"Id\":3,\"Name\":\"New Delhi\"}]";
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

    //region VTC list
    public List<CustomFranchiseeApplicationSpinnerDto> getVTCList(String vkIdOrEnquiryId, String stateId, String districtId) {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_DOCUMENT_VTC;
            url = url.replace("{vkIdOrEnquiryId}", vkIdOrEnquiryId);
            url = url.replace("{stateId}", stateId);
            url = url.replace("{districtId}", districtId);
            String data = okHttpService.getDataFromService(true, url);

            //String data = "[{\"Id\":1,\"Name\":\"Chilla Saroda Bangar (Ct)-Preet Vihar\"},{\"Id\":2,\"Name\":\"Dallo Pura (Ct)-Preet Vihar\"},{\"Id\":3,\"Name\":\"Kondli (Ct)-Preet Vihar\"}]";
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

    //region Post Document detail
    public String saveDocumentDetail(String vkIdOrEnquiryId, String type, String jsonData) {

        String data = null;
        try {

            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_SAVE_DOCUMENT_DETAIL;
            url = url.replace("{vkIdOrEnquiryId}", vkIdOrEnquiryId);
            url = url.replace("{type}", type);

            data = okHttpService.postDataToService(url, jsonData);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    //endregion

    //region Get Page No
    public int getPageNo(List<AgreementImageDto> agreementImagesList) {
        int pgNo = 1;

        int size = agreementImagesList.size();
        if (size > 0) {
            String name = agreementImagesList.get(size - 1).getName();
            if (!TextUtils.isEmpty(name)) {
                pgNo = Integer.parseInt(name) + 1;
            }
        }
        return pgNo;
    }
    //endregion

    //region Qualification List
    public List<CustomFranchiseeApplicationSpinnerDto> getQualificationList(String vkIdOrEnquiryId) {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();

        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_QUALIFICATION_DETAILS;
            url = url.replace("{vkIdOrEnquiryId}", vkIdOrEnquiryId);
            String data = okHttpService.getDataFromService(IsCached, url);

            //data = "[{\"Id\":1,\"Name\":\"GSTIN Document\"},{\"Id\":2,\"Name\":\"PAN Card\"}]";
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

    //region Address Type List
    public List<CustomFranchiseeApplicationSpinnerDto> getAddressTypeList(String vkIdOrEnquiryId) {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();

        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_ADDRESS_TYPE_DETAILS;
            url = url.replace("{vkIdOrEnquiryId}", vkIdOrEnquiryId);
            String data = okHttpService.getDataFromService(IsCached, url);

            //data = "[{\"Id\":1,\"Name\":\"GSTIN Document\"},{\"Id\":2,\"Name\":\"PAN Card\"}]";
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

    //region Address Proof Type List
    public List<CustomFranchiseeApplicationSpinnerDto> getAddressProofTypeList(String vkIdOrEnquiryId) {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<CustomFranchiseeApplicationSpinnerDto>();

        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_ADDRESS_PROOF_TYPE_DETAILS;
            url = url.replace("{vkIdOrEnquiryId}", vkIdOrEnquiryId);
            String data = okHttpService.getDataFromService(IsCached, url);

            //data = "[{\"Id\":1,\"Name\":\"GSTIN Document\"},{\"Id\":2,\"Name\":\"PAN Card\"}]";
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

    //region Get Specific Address Details By Address Type
    public String getSpecificAddressDetailsByType(String vkIdOrEnquiryId, String addressType) {
        String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_SPECIFIC_ADDRESS_DETAILS;
        url = url.replace("{vkIdOrEnquiryId}", vkIdOrEnquiryId);
        url = url.replace("{addressType}", addressType);
        String data = okHttpService.getDataFromService(IsCached, url);
        return data;
    }
    //endregion

}
