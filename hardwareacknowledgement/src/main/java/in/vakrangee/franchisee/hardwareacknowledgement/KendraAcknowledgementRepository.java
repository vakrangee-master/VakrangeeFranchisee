package in.vakrangee.franchisee.hardwareacknowledgement;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import in.vakrangee.supercore.franchisee.nextgenfranchiseeapplication.CustomFranchiseeApplicationSpinnerDto;
import in.vakrangee.supercore.franchisee.service.OkHttpService;
import in.vakrangee.supercore.franchisee.utils.Constants;

public class KendraAcknowledgementRepository {

    private Context context;
    private OkHttpService okHttpService;
    private boolean IsCached = false;
    private static final String PLEASE_SELECT_CONST = "Please Select";

    public KendraAcknowledgementRepository(Context mContext) {
        this.context = mContext;
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

    //region Get Kendra All Equipment Acknowledgement Details List
    public String getGetKendraAllEquipmentAckData(String vkId) {
        String data = null;
        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_KENDRA_ALL_EQUIPMENT_ACK_DETAILS;
            url = url.replace("{VKID}", vkId);
            data = okHttpService.getDataFromService(IsCached, url);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    //endregion

    //region Get Existing Kendra Acknowledgement Detail
    public KendraAcknowledgementDto getExistingKendraAckDetail(String equipAckId) {
        KendraAcknowledgementDto acknowledgementDto = null;

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_KENDRA_ACK_DETAILS_BY_ID;
        url = url.replace("{equipAckId}", equipAckId);
        String data = okHttpService.getDataFromService(IsCached, url);
        try {
            if (TextUtils.isEmpty(data))
                return null;

            Gson gson = new GsonBuilder().create();
            List<KendraAcknowledgementDto> list = gson.fromJson(data, new TypeToken<ArrayList<KendraAcknowledgementDto>>() {
            }.getType());
            if (list.size() > 0)
                acknowledgementDto = list.get(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return acknowledgementDto;
    }
    //endregion

    //region Brand Name List
    public List<CustomFranchiseeApplicationSpinnerDto> getBrandNameList(String materialCode) {
        List<CustomFranchiseeApplicationSpinnerDto> brandNameList = new ArrayList<>();

        String url;
        if (TextUtils.isEmpty(materialCode)) {
            url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_BRAND_NAME;
        } else {
            url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_BRAND_NAME_WITH_MATERIAL_CODE;
            url = url.replace("{MATERIAL_CODE}", materialCode);
        }

        String data = okHttpService.getDataFromService(IsCached, url);

        try {
            JSONArray jsonArray = new JSONArray(data);
            Gson gson = new GsonBuilder().create();
            brandNameList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<CustomFranchiseeApplicationSpinnerDto>>() {
            }.getType());
            brandNameList.add(0, new CustomFranchiseeApplicationSpinnerDto("0", PLEASE_SELECT_CONST));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return brandNameList;
    }
    //endregion

    //region Check If Serial No already exists for the provided brand
    public String getSrNoExistsStatus(String brandId, String serialNo) {
        String data = null;
        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_SRNO_EXISTS_STATUS;
            url = url.replace("{BRAND_ID}", brandId);
            url = url.replace("{SERIAL_NO}", serialNo);

            data = okHttpService.getDataFromService(IsCached, url);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    //endregion

    //region Series List
    public List<CustomFranchiseeApplicationSpinnerDto> getSeriesList(String BrandId) {
        List<CustomFranchiseeApplicationSpinnerDto> seriesList = new ArrayList<>();
        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_SERIES;
            url = url.replace("{BRAND_ID}", BrandId);
            String data = okHttpService.getDataFromService(IsCached, url);
            JSONArray jsonArray = new JSONArray(data);
            Gson gson = new GsonBuilder().create();
            seriesList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<CustomFranchiseeApplicationSpinnerDto>>() {
            }.getType());
            seriesList.add(0, new CustomFranchiseeApplicationSpinnerDto("0", PLEASE_SELECT_CONST));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return seriesList;
    }
    //endregion

    //region Model List
    public List<CustomFranchiseeApplicationSpinnerDto> getModelList(String ProductId) {
        List<CustomFranchiseeApplicationSpinnerDto> modelList = new ArrayList<>();
        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_MODEL_PRODUCT;
            url = url.replace("{PRODUCT_ID}", ProductId);

            String data = okHttpService.getDataFromService(IsCached, url);
            JSONArray jsonArray = new JSONArray(data);

            Gson gson = new GsonBuilder().create();
            modelList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<CustomFranchiseeApplicationSpinnerDto>>() {
            }.getType());
            modelList.add(0, new CustomFranchiseeApplicationSpinnerDto("0", PLEASE_SELECT_CONST));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return modelList;
    }
    //endregion

    //region Goods Condition List
    public List<CustomFranchiseeApplicationSpinnerDto> getGoodsConditionList() {
        List<CustomFranchiseeApplicationSpinnerDto> goodsList = new ArrayList<>();
        String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_GOODS_SERVICE;
        String data = okHttpService.getDataFromService(IsCached, url);
        try {
            JSONArray jsonArray = new JSONArray(data);
            Gson gson = new GsonBuilder().create();
            goodsList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<CustomFranchiseeApplicationSpinnerDto>>() {
            }.getType());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return goodsList;
    }
    //endregion

    //region Post Kendra Acknowledgement detail
    public String saveKendraAckDetail(String userId, KendraAcknowledgementDto kendraAckDto) {

        String data = null;
        try {
            Gson gson = new Gson();
            String jsonData = gson.toJson(kendraAckDto, KendraAcknowledgementDto.class);

            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_SAVE_KENDRA_ACK_DETAILS;
            url = url.replace("{userId}", userId);

            data = okHttpService.postDataToService(url, jsonData);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
    //endregion

    //region Get Sample Equipment Packaging Image Base64
    public String getEquipmentPackingPreviewImage() {
        //direct url to image display
        String data = "";
        try {

            String url = Constants.URL_BASE_WS_FRANCHISEE_APP +
                    Constants.METHOD_NAME_GET_EQUIPMENT_PACKING_PREVIEW_IMAGE;
            data = url;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
    //endregion

}
