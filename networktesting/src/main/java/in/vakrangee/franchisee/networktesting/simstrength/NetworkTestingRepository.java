package in.vakrangee.franchisee.networktesting.simstrength;

import android.content.Context;

import in.vakrangee.supercore.franchisee.service.OkHttpService;
import in.vakrangee.supercore.franchisee.utils.Constants;

public class NetworkTestingRepository {

    private Context context;
    private OkHttpService okHttpService;
    private boolean IsCached = false;
    private static final String VKID_CONST = "{vkId}";

    public NetworkTestingRepository(Context mContext) {
        this.context = mContext;
        okHttpService = new OkHttpService(context);
    }

    // region Get SIM Network Testing Config Details
    public String getSIMNetworkTestingConfig(String VKIDOrEnquiryId) {
        String data = null;
        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_SIM_NETWORK_TESTING_CONFIG_DETAIL;
            url = url.replace(VKID_CONST, VKIDOrEnquiryId);
            data = okHttpService.getDataFromService(IsCached, url);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    //endregion

    //region Post Network Speed Testing Data
    public String saveNetworkSpeedTestingDetails(String vkId, String jsonData) {

        String data = null;
        try {

            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_SAVE_NETWORK_SPEED_TESTING_DETAIL;
            url = url.replace(VKID_CONST, vkId);

            data = okHttpService.postDataToService(url, jsonData);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    //endregion

    //region Post Network SIM Strength Data
    public String saveNetworkSIMStrengthDetails(String vkId, String jsonData) {

        String data = null;
        try {

            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_SAVE_NETWORK_SIM_STRENGTH_DETAIL;
            url = url.replace(VKID_CONST, vkId);

            data = okHttpService.postDataToService(url, jsonData);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    //endregion


}
