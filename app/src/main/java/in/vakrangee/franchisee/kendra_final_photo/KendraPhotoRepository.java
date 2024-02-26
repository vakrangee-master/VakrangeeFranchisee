package in.vakrangee.franchisee.kendra_final_photo;

import android.content.Context;

import in.vakrangee.supercore.franchisee.service.OkHttpService;
import in.vakrangee.supercore.franchisee.utils.Constants;

public class KendraPhotoRepository {

    private Context context;
    private OkHttpService okHttpService;
    private boolean isCached = false;

    public KendraPhotoRepository(Context mContext) {
        this.context = mContext;
        okHttpService = new OkHttpService(context);
    }

    //region Get Kendra Final Photo
    public String getKendraFinalPhoto(String vkId) {

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_KENDRA_FINAL_PHOTO;
        url = url.replace("{VKID}", vkId);
        String data = okHttpService.getDataFromService(isCached, url);

        return data;
    }
    //endregion

    //region Post Save Kendra final photo
    public String saveKendraFinalPhoto(String vkid, String jsonData) {

        String data = null;
        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_SAVE_KENDRA_FINAL_PHOTO;
            url = url.replace("{VKID}", vkid);

            data = okHttpService.postDataToService(url, jsonData);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
    //endregion

}
