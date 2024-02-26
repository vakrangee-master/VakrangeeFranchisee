package in.vakrangee.franchisee.sitelayout.finalrmapproval;

import android.content.Context;

import in.vakrangee.supercore.franchisee.service.OkHttpService;
import in.vakrangee.supercore.franchisee.utils.Constants;

public class FinalRMApprovalRepository {

    private Context context;
    private OkHttpService okHttpService;
    private boolean isCached = false;

    public FinalRMApprovalRepository(Context mContext) {
        this.context = mContext;
        okHttpService = new OkHttpService(context);
    }

    // region Get Final RM Approval Details
    public String getFinalRMApprovalDetails(String enquiryId) {
        String data = null;
        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_FINAL_RM_APPROVAL_DETAILS;
            url = url.replace("{enquiryId}", enquiryId);

            data = okHttpService.getDataFromService(isCached, url);
            data = "OKAY|{\"is_editable\":\"1\"}";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    //endregion
}
