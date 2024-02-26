package in.vakrangee.franchisee.helpline;

import android.content.Context;

import in.vakrangee.supercore.connect.Constants;
import in.vakrangee.supercore.franchisee.service.OkHttpService;

public class ServiceListRepository {

    private Context context;
    private OkHttpService okHttpService;
    private boolean IsCached = false;
    public static final String ImageURL = "https://vkmssit.vakrangee.in/WSVKApp/media/img/mobile";

    public ServiceListRepository(Context context) {
        this.context = context;
        okHttpService = new OkHttpService(context);
    }

    //region get myhelp line data
    public String getMyHelplineList(String enquiryId) {
        String url = Constants.URL_BASE_WS_ATM_DETAIL_URL + Constants.GET_MY_HELP_LINE_LIST;
        url = url.replace("{vkid}", enquiryId);
        String data = okHttpService.getDataFromService(IsCached, url);

        return data;
    }

    //endregion

}
