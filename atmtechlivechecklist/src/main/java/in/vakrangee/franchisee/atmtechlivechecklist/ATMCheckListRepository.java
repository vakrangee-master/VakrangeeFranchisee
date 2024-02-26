package in.vakrangee.franchisee.atmtechlivechecklist;

import android.content.Context;

import in.vakrangee.supercore.franchisee.service.OkHttpService;
import in.vakrangee.supercore.franchisee.utils.Constants;

public class ATMCheckListRepository {

    private Context context;
    private OkHttpService okHttpService;
    private boolean isCached = false;

    public ATMCheckListRepository(Context mContext) {
        this.context = mContext;
        okHttpService = new OkHttpService(context);
    }

    // region Get All ATM TechLive CheckList Details
    public String getAllATMTechLiveCheckListDetails(String enquiryId) {
        String data = null;
        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_ATM_TECHLIVE_CHECKLIST_DETAILS;
            url = url.replace("{enquiryId}", enquiryId);

            data = okHttpService.getDataFromService(isCached, url);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    //endregion

}
