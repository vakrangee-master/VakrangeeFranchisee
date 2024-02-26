package in.vakrangee.franchisee.workinprogress.wipchatview;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.shrikanthravi.chatview.data.Message;

import java.util.List;

import in.vakrangee.supercore.franchisee.nextgenfranchiseeapplication.CustomFranchiseeApplicationSpinnerDto;
import in.vakrangee.supercore.franchisee.service.OkHttpService;
import in.vakrangee.supercore.franchisee.utils.Constants;

public class WIPChatViewRepository {

    private Context context;
    private OkHttpService okHttpService;
    private boolean IsCached = false;

    public WIPChatViewRepository(Context context) {
        this.context = context;
        okHttpService = new OkHttpService(context);
    }

    //region Get Selected Pos
    public int getSelectedPos(List<CustomFranchiseeApplicationSpinnerDto> spinnerDtoList, String selectedValue) {

        if (TextUtils.isEmpty(selectedValue))
            return 0;

        for (int i = 0; i < spinnerDtoList.size(); i++) {
            if (selectedValue.equalsIgnoreCase(spinnerDtoList.get(i).getId()))
                return i;
        }
        return 0;
    }
    //endregion

    //region Get Category Details List
    public String getGetCategoryList(String VKIDOrEnquiryId) {
        String data = null;
        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_WIP_CHATVIEW_CATEGORY_DETAILS;
            url = url.replace("{VKIDOrEnquiryId}", VKIDOrEnquiryId);
            data = okHttpService.getDataFromService(true, url);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    //endregion

    //region Get Sub-Category Details List
    public String getGetSubCategoryList(String VKIDOrEnquiryId, String categoryId) {
        String data = null;
        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_WIP_CHATVIEW_SUB_CATEGORY_DETAILS;
            url = url.replace("{CATEGORYID}", categoryId);
            url = url.replace("{VKIDOrEnquiryId}", VKIDOrEnquiryId);
            data = okHttpService.getDataFromService(true, url);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    //endregion

    //region Get WIP Messages data
    public String getGetWIPChatMessagesData(String VKIDOrEnquiryId) {
        String data = null;
        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_WIP_CHATVIEW_CHAT_MESSAGES_DETAILS;
            url = url.replace("{VKIDOrEnquiryId}", VKIDOrEnquiryId);
            data = okHttpService.getDataFromService(IsCached, url);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    //endregion

    //region Post WIP Chat Message
    public String saveWIPChatMessage(Message message, String VKIDOrEnquiryId) {

        String data = null;
        try {
            Gson gson = new Gson();
            String jsonData = gson.toJson(message, Message.class);

            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_SAVE_WIP_CHATVIEW_CHAT_MESSAGE;
            url = url.replace("{VKIDOrEnquiryId}", VKIDOrEnquiryId);

            data = okHttpService.postDataToService(url, jsonData);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
    //endregion

    //region check WIP status
    public String getWIPStatus(String VKIDOrEnquiryId) {
        String data = null;
        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_CHECK_WIP_CHATVIEW_STATUS;
            url = url.replace("{VKIDOrEnquiryId}", VKIDOrEnquiryId);
            data = okHttpService.getDataFromService(IsCached, url);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    //endregion

    //region Get WIP Messages with Pagination data
    public String getGetWIPChatMessagesWithPaginationData(String VKIDOrEnquiryId, String pageNo) {
        String data = null;
        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_WIP_CHATVIEW_CHAT_MESSAGE_WITH_PAGINATION;
            url = url.replace("{VKIDOrEnquiryId}", VKIDOrEnquiryId);
            url = url.replace("{PageNo}", pageNo);

            data = okHttpService.getDataFromService(IsCached, url);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    //endregion

    //region Get WIP Filtered Messages
    public String getWIPFilterMessages(String VKIDOrEnquiryId, String pageNo, String elementId, String elementDetailId) {
        String data = null;
        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_WIP_CHATVIEW_FILTER_MESSAGE_WITH_PAGINATION;
            url = url.replace("{VKIDOrEnquiryId}", VKIDOrEnquiryId);
            url = url.replace("{PageNo}", pageNo);
            url = url.replace("{elementId}", elementId);
            url = url.replace("{elementDetailId}", elementDetailId);

            data = okHttpService.getDataFromService(IsCached, url);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    //endregion
}
