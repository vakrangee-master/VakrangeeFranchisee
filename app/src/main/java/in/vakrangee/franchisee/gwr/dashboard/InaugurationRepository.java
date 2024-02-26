package in.vakrangee.franchisee.gwr.dashboard;

import android.content.Context;

import in.vakrangee.supercore.franchisee.service.OkHttpService;
import in.vakrangee.supercore.franchisee.utils.Constants;

public class InaugurationRepository {

    private Context context;
    private OkHttpService okHttpService;
    private boolean IsCached = false;

    public InaugurationRepository(Context context) {
        this.context = context;
        okHttpService = new OkHttpService(context);
    }

    //region Get Server Date Time
    public String getServerDateTime(String macAddress) {

        String url = Constants.URL_BASE_WS_GWR_SIGNAGE_APP + Constants.METHOD_NAME_GET_SERVER_DATETIME;
        url = url.replace("{MAC_ADDRESS}", macAddress);
        String data = okHttpService.getDataFromService(IsCached, url);

        return data;
    }
    //endregion

    public String checkGuinnessWorldRecord(String macAddress) {
        String url = Constants.URL_BASE_WS_GWR_SIGNAGE_APP + Constants.METHOD_NAME_CHECK_GUINNESS_WORLD_RECORD_DETAILS;
        url = url.replace("{MAC_ADDRESS}", macAddress);
        String data = okHttpService.getDataFromService(IsCached, url);

        return data;
    }
}
