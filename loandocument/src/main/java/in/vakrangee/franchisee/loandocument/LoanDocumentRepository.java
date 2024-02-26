package in.vakrangee.franchisee.loandocument;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import in.vakrangee.franchisee.loandocument.model.AgreementImageDto;
import in.vakrangee.franchisee.loandocument.model.LoanDocumentDto;
import in.vakrangee.franchisee.loandocument.model.UploadAcknowledgementDto;
import in.vakrangee.franchisee.loandocument.model.UploadSanctionDto;
import in.vakrangee.supercore.franchisee.nextgenfranchiseeapplication.CustomFranchiseeApplicationSpinnerDto;
import in.vakrangee.supercore.franchisee.service.OkHttpService;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Constants;

public class LoanDocumentRepository {

    private Context context;
    private OkHttpService okHttpService;
    private boolean isCached = false;

    public LoanDocumentRepository(Context mContext) {
        this.context = mContext;
        okHttpService = new OkHttpService(context);
    }

    // region Get All Loan Doument Details
    public String getAllLoanDoumentListDetails(String enquiryId) {
        String data = null;
        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_ALL_LOAN_DOCUMENT_DETAILS;
            url = url.replace("{enquiryId}", enquiryId);

            data = okHttpService.getDataFromService(isCached, url);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    //endregion

    //region Post Loan Document Detail
    public String saveLoanDocumentDetail(String enquiryId, LoanDocumentDto loanDocumentDto) {

        String data = null;
        try {
            Gson gson = new Gson();
            String jsonData = gson.toJson(loanDocumentDto, LoanDocumentDto.class);

            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_SAVE_LOAN_DOCUMENT_DETAILS;
            url = url.replace("{enquiryId}", enquiryId);

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

    //region Branch Name
    public List<CustomFranchiseeApplicationSpinnerDto> getBranchNameList(String vkId) {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_UBI_LOAN_BRANCH_NAME_LIST;
        url = url.replace("{vkId}", vkId);
        String data = okHttpService.getDataFromService(false, url);

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

    //region Acknowledgement Type
    public List<CustomFranchiseeApplicationSpinnerDto> getAcknowledgementType(String vkId) {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_UBI_LOAN_ACKNOWLEDGEMENT_TYPE;
        url = url.replace("{vkId}", vkId);
        String data = okHttpService.getDataFromService(false, url);

        try {
            JSONArray jsonArray = new JSONArray(data);

            Gson gson = new GsonBuilder().create();
            list = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<CustomFranchiseeApplicationSpinnerDto>>() {
            }.getType());
            list.add(0, new CustomFranchiseeApplicationSpinnerDto("-1", "Please Select"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    //endregion

    // region Get UBI Loan Application Acknowledgement Details
    public String getUBILoanApplicationAckDetails(String vkId) {
        String data = null;
        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_UBI_LOAN_APPLICATION_ACK_DETAILS;
            url = url.replace("{vkId}", vkId);

            data = okHttpService.getDataFromService(isCached, url);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    //endregion

    //region Post UBI Loan Application Acknowledgement Details
    public String saveUBILoanApplicationAckDetail(String vkId, UploadAcknowledgementDto uploadAcknowledgementDto) {

        String data = null;
        try {
            Gson gson = new Gson();
            String jsonData = gson.toJson(uploadAcknowledgementDto, UploadAcknowledgementDto.class);

            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_SAVE_UBI_LOAN_APPLICATION_ACK_DETAILS;
            url = url.replace("{vkId}", vkId);

            data = okHttpService.postDataToService(url, jsonData);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
    //endregion

    // region Get UBI Loan Sanction Details
    public String getUBILoanSanctionDetails(String vkId) {
        String data = null;
        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_UBI_LOAN_SANCTION_DETAILS;
            url = url.replace("{vkId}", vkId);

            data = okHttpService.getDataFromService(isCached, url);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    //endregion

    //region Post UBI Loan Sanction Details
    public String saveUBILoanSanctionDetail(String vkId, UploadSanctionDto uploadSanctionDto) {

        String data = null;
        try {
            Gson gson = new Gson();
            String jsonData = gson.toJson(uploadSanctionDto, UploadSanctionDto.class);

            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_SAVE_UBI_LOAN_SANCTION_DETAILS;
            url = url.replace("{vkId}", vkId);

            data = okHttpService.postDataToService(url, jsonData);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
    //endregion
}
