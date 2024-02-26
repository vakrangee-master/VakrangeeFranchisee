package in.vakrangee.franchisee.nextgenfranchiseeapplication.bank_statement;

import android.content.Context;

import com.google.gson.Gson;

import in.vakrangee.supercore.franchisee.service.OkHttpService;
import in.vakrangee.supercore.franchisee.utils.Constants;

public class BankStatementRepository {

    private Context context;
    private OkHttpService okHttpService;
    private boolean isCached = false;

    public BankStatementRepository(Context mContext) {
        this.context = mContext;
        okHttpService = new OkHttpService(context);
    }

    // region Get All Bank Statement Details
    public String getAllBankStatementDetails(String appId) {
        String data = null;
        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_ALL_BANK_STATEMENT_DETAILS;
            url = url.replace("{appId}", appId);

            data = okHttpService.getDataFromService(isCached, url);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    //endregion

    // region Get Specific Bank Statement Details
    public String getSpecificBankStatementDetails(String appId, String faBankkID) {
        String data = null;
        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_SPECIFIC_BANK_STATEMENT_DETAILS;
            url = url.replace("{appId}", appId);
            url = url.replace("{fa_bank_id}", faBankkID);

            data = okHttpService.getDataFromService(isCached, url);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    //endregion

    //region Post Specific Bank Statement Details
    public String saveSpecificBankStatementDetail(String appId, BankStatementDto bankStatementDto) {

        String data = null;
        try {
            Gson gson = new Gson();
            String jsonData = gson.toJson(bankStatementDto, BankStatementDto.class);

            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_SAVE_SPECIFIC_BANK_STATEMENT_DETAILS;
            url = url.replace("{appId}", appId);

            data = okHttpService.postDataToService(url, jsonData);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
    //endregion

}
