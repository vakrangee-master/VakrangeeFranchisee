package in.vakrangee.franchisee.phasechecks;

import android.content.Context;

import in.vakrangee.supercore.franchisee.service.OkHttpService;
import in.vakrangee.supercore.franchisee.utils.Constants;

public class FranchiseePhaseInfoRepository {

    private Context context;
    private OkHttpService okHttpService;
    private boolean IsCached = false;

    public FranchiseePhaseInfoRepository(Context context) {
        this.context = context;
        okHttpService = new OkHttpService(context);
    }

    //region Get Franchisee Phase Details
    public String getFranchiseePhaseInfo(String enquiryId) {

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_PHASE_DETAIL;
        url = url.replace("{enquiryId}", enquiryId);
        String data = okHttpService.getDataFromService(IsCached, url);

        return data;
    }

    //endregion
}
