package in.vakrangee.franchisee.locationupdation;

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

public class KendraVerificationRepository {


    private Context context;
    private OkHttpService okHttpService;
    private boolean IsCached = false;

    public KendraVerificationRepository(Context context) {
        this.context = context;
        okHttpService = new OkHttpService(context);
    }

    //region Get Kendra location list
    public String getKendraLocationList(String vkid, String locationcode, String scope) {
        String response;
        String url;
        if (TextUtils.isEmpty(scope)) {
            url = Constants.URL_BASE_WS_VKMS_APP + Constants.METHOD_NAME_GET_KENDRA_LOCATION_DETAILS1;
        } else {
            url = Constants.URL_BASE_WS_VKMS_APP + Constants.METHOD_NAME_GET_KENDRA_LOCATION_DETAILS2;
            url = url.replace("{scope}", scope);
        }
        url = url.replace("{vkId}", vkid);
        url = url.replace("{locationId}", locationcode);
        response = okHttpService.postDataToService(url, " ");


        // data="OKAY|DL|[{\"name\":\"Ajmer\",\"id\":53},{\"name\":\"Bhartpur\",\"id\":54},{\"name\":\"Bikaner\",\"id\":55},{\"name\":\"Jaipur\",\"id\":56},{\"name\":\"Jodhpur\",\"id\":57},{\"name\":\"Kota\",\"id\":58},{\"name\":\"Udaipur\",\"id\":59}]";


        return response;
    }
    //endregion

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

    //region state list
    public List<CustomFranchiseeApplicationSpinnerDto> getAllStateBylList(String VKID) {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        try {
            String url = Constants.URL_BASE_WS_VKMS_APP + Constants.METHOD_NAME_GET_STATE_LIST;
            url = url.replace("{vkId}", VKID);
            String data = okHttpService.getDataFromService(IsCached, url);

            //data = "[{\"name\":\"Andaman And Nicobar Islands\",\"id\":\"35\"},{\"name\":\"Andhra Pradesh\",\"id\":\"28\"},{\"name\":\"Arunachal Pradesh\",\"id\":\"12\"},{\"name\":\"Assam\",\"id\":\"18\"},{\"name\":\"Bihar\",\"id\":\"10\"},{\"name\":\"Chandigarh\",\"id\":\"04\"},{\"name\":\"Chhattisgarh\",\"id\":\"22\"},{\"name\":\"Dadra And Nagar Haveli\",\"id\":\"26\"},{\"name\":\"Daman And Diu\",\"id\":\"25\"},{\"name\":\"Delhi\",\"id\":\"07\"},{\"name\":\"Goa\",\"id\":\"30\"},{\"name\":\"Gujarat\",\"id\":\"24\"},{\"name\":\"Haryana\",\"id\":\"06\"},{\"name\":\"Himachal Pradesh\",\"id\":\"02\"},{\"name\":\"Jammu And Kashmir\",\"id\":\"01\"},{\"name\":\"Jharkhand\",\"id\":\"20\"},{\"name\":\"Karnataka\",\"id\":\"29\"},{\"name\":\"Kerala\",\"id\":\"32\"},{\"name\":\"Lakshadweep\",\"id\":\"31\"},{\"name\":\"Madhya Pradesh\",\"id\":\"23\"},{\"name\":\"Maharashtra\",\"id\":\"27\"},{\"name\":\"Manipur\",\"id\":\"14\"},{\"name\":\"Meghalaya\",\"id\":\"17\"},{\"name\":\"Mizoram\",\"id\":\"15\"},{\"name\":\"Nagaland\",\"id\":\"13\"},{\"name\":\"Odisha\",\"id\":\"21\"},{\"name\":\"Puducherry\",\"id\":\"34\"},{\"name\":\"Punjab\",\"id\":\"03\"},{\"name\":\"Rajasthan\",\"id\":\"08\"},{\"name\":\"Sikkim\",\"id\":\"11\"},{\"name\":\"Tamil Nadu\",\"id\":\"33\"},{\"name\":\"Telangana\",\"id\":\"36\"},{\"name\":\"Tripura\",\"id\":\"16\"},{\"name\":\"Uttar Pradesh\",\"id\":\"09\"},{\"name\":\"Uttarakhand\",\"id\":\"05\"},{\"name\":\"West Bengal\",\"id\":\"19\"}]";
            //String data = "[{\"Id\":1,\"Name\":\"Chhattisgarh\"},{\"Id\":2,\"Name\":\"Delhi\"},{\"Id\":3,\"Name\":\"Himachal Pradesh\"}]";

            JSONArray jsonArray = new JSONArray(data);

            Gson gson = new GsonBuilder().create();
            list = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<CustomFranchiseeApplicationSpinnerDto>>() {
            }.getType());
            list.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    //endregion

    //region District  list
    public List<CustomFranchiseeApplicationSpinnerDto> getAllDistrictBylList(String VKID, String StateId) {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        try {
            String url = Constants.URL_BASE_WS_VKMS_APP + Constants.METHOD_NAME_GET_DISTRICT_LIST;
            url = url.replace("{vkId}", VKID);
            url = url.replace("{stateId}", StateId);
            String data = okHttpService.getDataFromService(IsCached, url);

            // data = "[{\"id\":\"410\",\"v_district_code\":\"23\",\"name\":\" Raipur\"},{\"id\":\"410\",\"v_district_code\":\"02\",\"name\":\"Baloda Bazar\"},{\"id\":\"401\",\"v_district_code\":\"03\",\"name\":\"Balrampur\"},{\"id\":\"904\",\"v_district_code\":\"05\",\"name\":\"Bametara\"},{\"id\":\"414\",\"v_district_code\":\"04\",\"name\":\"Bastar\"},{\"id\":\"417\",\"v_district_code\":\"06\",\"name\":\"Bijapur\"},{\"id\":\"406\",\"v_district_code\":\"07\",\"name\":\"Bilaspur\"},{\"id\":\"416\",\"v_district_code\":\"08\",\"name\":\"Dakshin Bastar Dantewada\"},{\"id\":\"412\",\"v_district_code\":\"09\",\"name\":\"Dhamtari\"},{\"id\":\"409\",\"v_district_code\":\"10\",\"name\":\"Durg\"},{\"id\":\"410\",\"v_district_code\":\"11\",\"name\":\"Gariyabad\"},{\"id\":\"405\",\"v_district_code\":\"12\",\"name\":\"Janjgir - Champa\"},{\"id\":\"402\",\"v_district_code\":\"13\",\"name\":\"Jashpur\"},{\"id\":\"407\",\"v_district_code\":\"15\",\"name\":\"Kabeerdham\"},{\"id\":\"414\",\"v_district_code\":\"16\",\"name\":\"Kondagaon\"},{\"id\":\"404\",\"v_district_code\":\"17\",\"name\":\"Korba\"},{\"id\":\"400\",\"v_district_code\":\"18\",\"name\":\"Koriya\"},{\"id\":\"411\",\"v_district_code\":\"19\",\"name\":\"Mahasamund\"},{\"id\":\"903\",\"v_district_code\":\"20\",\"name\":\"Mongeli\"},{\"id\":\"415\",\"v_district_code\":\"21\",\"name\":\"Narayanpur\"},{\"id\":\"403\",\"v_district_code\":\"22\",\"name\":\"Raigarh\"},{\"id\":\"408\",\"v_district_code\":\"24\",\"name\":\"Rajnandgaon\"},{\"id\":\"401\",\"v_district_code\":\"26\",\"name\":\"Saurajpur\"},{\"id\":\"416\",\"v_district_code\":\"25\",\"name\":\"Sukma\"},{\"id\":\"401\",\"v_district_code\":\"27\",\"name\":\"Surguja\"},{\"id\":\"413\",\"v_district_code\":\"14\",\"name\":\"Uttar Bastar Kanker\"}]";
            //String data = "[{\"Id\":1,\"Name\":\"Central\"},{\"Id\":2,\"Name\":\"East\"},{\"Id\":3,\"Name\":\"New Delhi\"}]";

            JSONArray jsonArray = new JSONArray(data);

            Gson gson = new GsonBuilder().create();
            list = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<CustomFranchiseeApplicationSpinnerDto>>() {
            }.getType());
            list.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    //endregion

    //region VTC  list
    public List<CustomFranchiseeApplicationSpinnerDto> getAllVTCBylList(String VKID, String stateId, String districtId) {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        try {
            String url = Constants.URL_BASE_WS_VKMS_APP + Constants.METHOD_NAME_GET_VTC_LIST;
            url = url.replace("{vkId}", VKID);
            url = url.replace("{stateId}", stateId);
            url = url.replace("{districtId}", districtId);
            String data = okHttpService.getDataFromService(IsCached, url);

            // String data = "[{\"id\":\"435068\",\"name\":\"Aadmuda-Pusour\"},{\"id\":\"434529\",\"name\":\"Aamanara-Udaipur Dharamjaigarh\"},{\"id\":\"434818\",\"name\":\"Aamgaon-Tamnar\"},{\"id\":\"435372\",\"name\":\"Achanak Pali Alias Churela-Sarangarh\"},{\"id\":\"435413\",\"name\":\"Achanak Pali-Sarangarh\"},{\"id\":\"435229\",\"name\":\"Adajhar-Kharsia\"},{\"id\":\"434949\",\"name\":\"Adbahal-Raigarh\"},{\"id\":\"435629\",\"name\":\"Adbhar-Baramkela\"},{\"id\":\"435180\",\"name\":\"Adpathra-Kharsia\"},{\"id\":\"434523\",\"name\":\"Adukala-Udaipur Dharamjaigarh\"},{\"id\":\"435170\",\"name\":\"Agasmar-Kharsia\"},{\"id\":\"434605\",\"name\":\"Ainkra-Lailunga\"},{\"id\":\"434725\",\"name\":\"Ajijgarh-Gharghoda\"},{\"id\":\"435769\",\"name\":\"Akabartola-Baramkela\"},{\"id\":\"434402\",\"name\":\"Alola-Udaipur Dharamjaigarh\"},{\"id\":\"435314\",\"name\":\"Ama Koni-Sarangarh\"},{\"id\":\"435189\",\"name\":\"Amadol-Kharsia\"},{\"id\":\"434801\",\"name\":\"Amaghat-Tamnar\"},{\"id\":\"435646\",\"name\":\"Amakoni Bade-Baramkela\"},{\"id\":\"435649\",\"name\":\"Amakoni Chhote-Baramkela\"},{\"id\":\"435457\",\"name\":\"Amakoni-Sarangarh\"},{\"id\":\"435378\",\"name\":\"Amal Diha-Sarangarh\"},{\"id\":\"435033\",\"name\":\"Amaldiha-Pusour\"},{\"id\":\"435106\",\"name\":\"Amalibhauna-Pusour\"},{\"id\":\"434836\",\"name\":\"Amalidhondha-Tamnar\"},{\"id\":\"435526\",\"name\":\"Amalidipa-Sarangarh\"},{\"id\":\"435678\",\"name\":\"Amalipali-Baramkela\"},{\"id\":\"435762\",\"name\":\"Amalipali-Baramkela\"},{\"id\":\"434998\",\"name\":\"Amalipali-Pusour\"},{\"id\":\"435107\",\"name\":\"Amalipali-Pusour\"},{\"id\":\"435442\",\"name\":\"Amalipali-Sarangarh\"},{\"id\":\"434358\",\"name\":\"Amalitikra-Udaipur Dharamjaigarh\"},{\"id\":\"434434\",\"name\":\"Amaljharia-Udaipur Dharamjaigarh\"},{\"id\":\"434929\",\"name\":\"Amapal-Raigarh\"},{\"id\":\"435781\",\"name\":\"Amapali-Baramkela\"},{\"id\":\"435261\",\"name\":\"Amapali-Kharsia\"},{\"id\":\"434625\",\"name\":\"Amapali-Lailunga\"},{\"id\":\"434999\",\"name\":\"Amapali-Pusour\"},{\"id\":\"434773\",\"name\":\"Amapali-Tamnar\"},{\"id\":\"434474\",\"name\":\"Amapali-Udaipur Dharamjaigarh\"},{\"id\":\"435663\",\"name\":\"Amarkot (Ameri)-Baramkela\"},{\"id\":\"435664\",\"name\":\"Amarkot (Bore)-Baramkela\"},{\"id\":\"435669\",\"name\":\"Ameri-Baramkela\"},{\"id\":\"435496\",\"name\":\"Amethi-Sarangarh\"},{\"id\":\"434475\",\"name\":\"Amgaon-Udaipur Dharamjaigarh\"},{\"id\":\"435510\",\"name\":\"Amjhar-Sarangarh\"},{\"id\":\"435374\",\"name\":\"Amli Pali-Sarangarh\"},{\"id\":\"434995\",\"name\":\"Amlibhauna (Og)-Raigarh\"},{\"id\":\"434698\",\"name\":\"Amlidih-Gharghoda\"},{\"id\":\"435628\",\"name\":\"Amlikot-Baramkela\"},{\"id\":\"435720\",\"name\":\"Amoda-Baramkela\"},{\"id\":\"435627\",\"name\":\"Amurra-Baramkela\"},{\"id\":\"435296\",\"name\":\"Andola-Sarangarh\"},{\"id\":\"434568\",\"name\":\"Angekela-Lailunga\"},{\"id\":\"435223\",\"name\":\"Anjoripali-Kharsia\"},{\"id\":\"434881\",\"name\":\"Arsipali-Raigarh\"},{\"id\":\"434901\",\"name\":\"Aunrabhantha-Raigarh\"},{\"id\":\"434841\",\"name\":\"Auorajor-Tamnar\"},{\"id\":\"434754\",\"name\":\"Auraimuda-Tamnar\"},{\"id\":\"434519\",\"name\":\"Auranara-Udaipur Dharamjaigarh\"},{\"id\":\"435206\",\"name\":\"Aurda-Kharsia\"},{\"id\":\"435050\",\"name\":\"Aurda-Pusour\"},{\"id\":\"434992\",\"name\":\"Bade Attarmuda (Og)-Raigarh\"},{\"id\":\"435112\",\"name\":\"Bade Bhandar-Pusour\"},{\"id\":\"435240\",\"name\":\"Bade Dumarpali-Kharsia\"},{\"id\":\"434684\",\"name\":\"Bade Gumda-Gharghoda\"}]";

            JSONArray jsonArray = new JSONArray(data);

            Gson gson = new GsonBuilder().create();
            list = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<CustomFranchiseeApplicationSpinnerDto>>() {
            }.getType());
            list.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    //endregion

    //region Get specific FR details
    public String getSpecificFranchiseeDetails(String VKID) {

        String data = null;
        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_GET_SPECIFIC_FRANCHISEE_DETAILS;
            url = url.replace("{vkId}", VKID);
            data = okHttpService.getDataFromService(true, url);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;

    }
    //endregion

    //region Post Kendra Location Updation Details
    public String saveKendraLocationUpdationDetail(String vkId, KendraSavePhotoDto kendraSavePhotoDto) {

        String data = null;
        try {
            Gson gson = new Gson();
            String jsonData = gson.toJson(kendraSavePhotoDto, KendraSavePhotoDto.class);

            String url = Constants.URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM + Constants.METHOD_NAME_SAVE_FRANCHISEE_KENDRA_LOCATION_DETAILS;
            url = url.replace("{vkId}", vkId);

            data = okHttpService.postDataToService(url, jsonData);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
    //endregion

}
