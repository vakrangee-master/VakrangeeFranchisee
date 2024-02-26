package in.vakrangee.core.utils;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import in.vakrangee.core.model.NextGenFranchiseeApplicationFormDto;
import in.vakrangee.core.nextgenfranchiseeapplication.CustomFranchiseeApplicationSpinnerDto;
import in.vakrangee.core.service.OkHttpService;

public class FranchiseeApplicationRepository {

    private Context context;
    private OkHttpService okHttpService;
    private boolean IsCached = false;

    public FranchiseeApplicationRepository(Context context) {
        this.context = context;
        okHttpService = new OkHttpService(context);
    }

    //region Get Franchisee Application Details
    public String getFranchiseeApplicationDetails(String vkId) {

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_FRANCHISEE_APPLICATION_DETAILS;
        url = url.replace("{VKID}", vkId);
        String data = okHttpService.getDataFromService(IsCached, url);

        return data;
    }
    //endregion

    //region Get Franchisee Application Details by NextGenEnquiryId
    public String getFranchiseeApplicationDetailsByNextGenEnquiryId(String NextGenEnquiryId) {

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_FRANCHISEE_APPLICATION_DETAILS_BY_ENQUIRY_ID;
        url = url.replace("{NextGenEnquiryId}", NextGenEnquiryId);
        String data = okHttpService.getDataFromService(IsCached, url);

        return data;
    }
    //endregion

    //region Get IOCL RO NAME by IOCL RO CODE
    public String getIOCLRoName(String ioclCode) {

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_IOCL_RO_NAME;
        url = url.replace("{IOCLROCODE}", ioclCode);
        String data = okHttpService.getDataFromService(IsCached, url);

        return data;
    }
    //endregion

    //region Get Name Of Referral by EMPId
    public String getNameOfReferralByEmpId(String empId) {

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_NAME_OF_REFERRAL_BY_EMPID;
        url = url.replace("{EMPLOYEEID}", empId);
        String data = okHttpService.getDataFromService(IsCached, url);

        return data;
    }
    //endregion

    //region Get Name Of Referral by VKID
    public String getNameOfReferralByVKID(String vkId) {

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_NAME_OF_REFERRAL_BY_VKID;
        url = url.replace("{VKID}", vkId);
        String data = okHttpService.getDataFromService(IsCached, url);

        return data;
    }
    //endregion

    //region Entity Type List
    public List<CustomFranchiseeApplicationSpinnerDto> getEntityList() {
        List<CustomFranchiseeApplicationSpinnerDto> entityList = new ArrayList<>();

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_ENTITY_TYPE;
        String data = okHttpService.getDataFromService(true, url);
        //String data = "[{\"Id\":8,\"Name\":\"Co-Operative Society\"},{\"Id\":4,\"Name\":\"Company\"},{\"Id\":1,\"Name\":\"Individual\"},{\"Id\":10,\"Name\":\"Limited Liability Partnership Firm (LLP)\"},{\"Id\":5,\"Name\":\"NGO\"},{\"Id\":3,\"Name\":\"Partnership\"},{\"Id\":9,\"Name\":\"Proprietorship (HUF)\"},{\"Id\":2,\"Name\":\"Proprietorship (Individual)\"},{\"Id\":6,\"Name\":\"Self Help Group\"},{\"Id\":7,\"Name\":\"Trust\"}]";

        try {
            JSONArray jsonArray = new JSONArray(data);

            Gson gson = new GsonBuilder().create();
            entityList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<CustomFranchiseeApplicationSpinnerDto>>() {
            }.getType());
            entityList.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return entityList;
    }
    //endregion

    //region Referred By List
    public List<CustomFranchiseeApplicationSpinnerDto> getReferredByList() {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_REFERRED_BY;
        String data = okHttpService.getDataFromService(true, url);
        //String data = "[{\"Id\":0,\"Name\":\"Not Applicable\"},{\"Id\":1,\"Name\":\"Employee\"},{\"Id\":2,\"Name\":\"Franchisee\"}]";

        try {
            JSONArray jsonArray = new JSONArray(data);

            Gson gson = new GsonBuilder().create();
            list = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<CustomFranchiseeApplicationSpinnerDto>>() {
            }.getType());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    //endregion

    //region Salutation List
    public List<CustomFranchiseeApplicationSpinnerDto> getSalutationList() {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_SALUTATION;
        String data = okHttpService.getDataFromService(true, url);
        //String data = "[{\"Id\":5,\"Name\":\"DR\"},{\"Id\":1,\"Name\":\"M/S\"},{\"Id\":7,\"Name\":\"Miss\"},{\"Id\":2,\"Name\":\"MR.\"},{\"Id\":4,\"Name\":\"MRS\"},{\"Id\":3,\"Name\":\"MS\"},{\"Id\":6,\"Name\":\"Other\"}]";

        try {
            JSONArray jsonArray = new JSONArray(data);

            Gson gson = new GsonBuilder().create();
            list = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<CustomFranchiseeApplicationSpinnerDto>>() {
            }.getType());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    //endregionS

    //region Nationality List
    public List<CustomFranchiseeApplicationSpinnerDto> getNationalityList() {
        List<CustomFranchiseeApplicationSpinnerDto> nationalityList = new ArrayList<>();

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_NATIONALITY;
        String data = okHttpService.getDataFromService(true, url);
        //String data = "[{\"Id\":1,\"Name\":\"Afghanistan\"},{\"Id\":9,\"Name\":\"Antarctica\"},{\"Id\":3,\"Name\":\"Brazil\"},{\"Id\":4,\"Name\":\"India\"}]";

        try {
            JSONArray jsonArray = new JSONArray(data);

            Gson gson = new GsonBuilder().create();
            nationalityList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<CustomFranchiseeApplicationSpinnerDto>>() {
            }.getType());
            nationalityList.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return nationalityList;
    }
    //endregion

    //region Highest Qualification List
    public List<CustomFranchiseeApplicationSpinnerDto> getHighestQualificationList() {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_HIGHEST_QUALIFICATION;
        String data = okHttpService.getDataFromService(true, url);
        //String data = "[{\"Id\":1,\"Name\":\"Secondary Certificate  (10th Pass)\"},{\"Id\":2,\"Name\":\"Higher Secondary certificate (12th Pass)\"},{\"Id\":3,\"Name\":\"Graduate/Diploma\"},{\"Id\":4,\"Name\":\"Post Graduate\"},{\"Id\":5,\"Name\":\"Professional Degree\"},{\"Id\":6,\"Name\":\"Doctorate\"},{\"Id\":7,\"Name\":\"Other\"}]";

        try {
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

    //region Prior Experience List
    public List<CustomFranchiseeApplicationSpinnerDto> getPriorExperienceList() {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_PRIOR_EXPERIENCE;
        String data = okHttpService.getDataFromService(true, url);
        //String data = "[{\"Id\":0,\"Name\":\"Not Applicable\"},{\"Id\":1,\"Name\":\"Insurance Agent\"},{\"Id\":2,\"Name\":\"Store Manager\"},{\"Id\":3,\"Name\":\"Bank Manager\"}]";

        try {
            JSONArray jsonArray = new JSONArray(data);

            Gson gson = new GsonBuilder().create();
            list = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<CustomFranchiseeApplicationSpinnerDto>>() {
            }.getType());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    //endregion

    //region Current Occupation List
    public List<CustomFranchiseeApplicationSpinnerDto> getCurrentOccupationList() {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_CURRENT_OCCUPATION;
        String data = okHttpService.getDataFromService(true, url);
        //String data = "[{\"Id\":2,\"Name\":\"House Wife\"},{\"Id\":3,\"Name\":\"Private Service\"},{\"Id\":4,\"Name\":\"Student\"},{\"Id\":6,\"Name\":\"Business/Profession\"},{\"Id\":5,\"Name\":\"Other\"}]";

        try {
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

    //region How do you hear about us List
    public List<CustomFranchiseeApplicationSpinnerDto> getHearAboutUsList() {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_ABOUT_US;
        String data = okHttpService.getDataFromService(true, url);
        //String data = "[{\"Id\":1,\"Name\":\"Event/Road Show\"},{\"Id\":2,\"Name\":\"Website\"},{\"Id\":3,\"Name\":\"Newspaper\"},{\"Id\":4,\"Name\":\"Employee\"},{\"Id\":5,\"Name\":\"Franchisee\"},{\"Id\":6,\"Name\":\"Social Media\"},{\"Id\":7,\"Name\":\"Hoardings\"},{\"Id\":8,\"Name\":\"Posters/Pamphlets\"},{\"Id\":9,\"Name\":\"Other\"}]";

        try {
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

    //region Account Type List
    public List<CustomFranchiseeApplicationSpinnerDto> getAccountTypeList() {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_ACCOUNT_TYPE;
        String data = okHttpService.getDataFromService(true, url);
        //String data = "[{\"Id\":1,\"Name\":\"Savings Account\"},{\"Id\":2,\"Name\":\"Current Account\"},{\"Id\":3,\"Name\":\"OD Account\"}]";

        try {
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

    //region Vakrangee Kendra Model List
    public List<CustomFranchiseeApplicationSpinnerDto> getVakrangeeKendraModelList() {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_MODEL;
        String data = okHttpService.getDataFromService(true, url);
        //String data = "[{\"Id\":8,\"Name\":\"Vakrangee Kendra Gold Model\"},{\"Id\":9,\"Name\":\"Vakrangee Kendra Silver Model\"}]";

        try {
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

    //region Source Of Funding List
    public List<CustomFranchiseeApplicationSpinnerDto> getSourceOfFundinglList() {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        try {

            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_SOURCE_OF_FUND;
            String data = okHttpService.getDataFromService(true, url);

            //String data = "[{\"Id\":1,\"Name\":\"Self Funding\"},{\"Id\":2,\"Name\":\"Loan\"}]";

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

    //region Premise Ownership Type List
    public List<CustomFranchiseeApplicationSpinnerDto> getPremiseOwnershipTypelList() {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_PREMISE_OWNERSHIP_TYPE;
            String data = okHttpService.getDataFromService(true, url);

            //String data = "[{\"Id\":1,\"Name\":\"Owned\"},{\"Id\":2,\"Name\":\"Rented\"}]";

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

    //region Owned By List
    public List<CustomFranchiseeApplicationSpinnerDto> getOwnedBylList() {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        try {

            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_OWNED_BY;
            String data = okHttpService.getDataFromService(true, url);

            //String data = "[{\"Id\":1,\"Name\":\"Self\"},{\"Id\":2,\"Name\":\"Family\"}]";

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

    //region Get Selected Pos
    public int getSelectedPos(List<CustomFranchiseeApplicationSpinnerDto> spinnerDtoList, String selectedValue) {

        if (TextUtils.isEmpty(selectedValue) || spinnerDtoList == null)
            return 0;

        for (int i = 0; i < spinnerDtoList.size(); i++) {
            if (selectedValue.equalsIgnoreCase(spinnerDtoList.get(i).getId()))
                return i;
        }
        return 0;
    }
    //endregion

    //region Relationship
    public List<CustomFranchiseeApplicationSpinnerDto> getRelationship() {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_RELATIONSHIP;
            String data = okHttpService.getDataFromService(true, url);

            //String data = "[{\"Id\":1,\"Name\":\"Friends\"},{\"Id\":2,\"Name\":\"Colleague\"},{\"Id\":3,\"Name\":\"Franchisee\"},{\"Id\":4,\"Name\":\"Vakrangee Employee\"},{\"Id\":5,\"Name\":\"Others\"}]";

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

    //region state list
    public List<CustomFranchiseeApplicationSpinnerDto> getAllStateBylList() {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_STATE;
            String data = okHttpService.getDataFromService(true, url);

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
    public List<CustomFranchiseeApplicationSpinnerDto> getAllDistrictBylList(String StateId) {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_DISTRICT;
            url = url.replace("{stateId}", StateId);
            String data = okHttpService.getDataFromService(true, url);

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
    public List<CustomFranchiseeApplicationSpinnerDto> getAllVTCBylList(String stateId, String districtId) {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_VTC;
            url = url.replace("{stateId}", stateId);
            url = url.replace("{districtId}", districtId);
            String data = okHttpService.getDataFromService(true, url);

            //String data = "[{\"Id\":1,\"Name\":\"Chilla Saroda Bangar (Ct)-Preet Vihar\"},{\"Id\":2,\"Name\":\"Dallo Pura (Ct)-Preet Vihar\"},{\"Id\":3,\"Name\":\"Kondli (Ct)-Preet Vihar\"}]";

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

    //region Bank Name List
    public List<CustomFranchiseeApplicationSpinnerDto> getBankNameList() {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        try {

            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_BANK_NAME;
            String data = okHttpService.getDataFromService(true, url);

            //String data = "[{\"Id\":1,\"Name\":\"Abhyudaya Co-Operative Bank\"},{\"Id\":2,\"Name\":\"Adarniya P.D. Patilsaheb Sahakari Bank Ltd.\"},{\"Id\":3,\"Name\":\"Allahabad Bank\"},{\"Id\":4,\"Name\":\"Bandhan Bank\"},{\"Id\":5,\"Name\":\"City Union Bank Limited\"}]";

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

    //region Address Proof Type List
    public List<CustomFranchiseeApplicationSpinnerDto> getAddressProofTypeList() {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_ADDRESS_PROOF_TYPE;
            String data = okHttpService.getDataFromService(true, url);

            //String data = "[{\"Id\":1,\"Name\":\"Aadhaar Card\"},{\"Id\":2,\"Name\":\"Bank Pass Book\"},{\"Id\":3,\"Name\":\"Digital Bonafide\"},{\"Id\":4,\"Name\":\"Domicile\"},{\"Id\":5,\"Name\":\"Driving License\"}]";

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

    //region GSTIN State List
    public List<CustomFranchiseeApplicationSpinnerDto> getGSTINStateList() {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_GSTIN_STATE;
            String data = okHttpService.getDataFromService(true, url);


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

    //region Post Franchisee Application detail
    public String saveFranchiseeDetail(String userId, String type, NextGenFranchiseeApplicationFormDto nextGenFranchiseeApplicationFormDto) {

        String data = null;
        try {
            Gson gson = new Gson();
            String jsonData = gson.toJson(nextGenFranchiseeApplicationFormDto, NextGenFranchiseeApplicationFormDto.class);

            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_SAVE_FRANCHISEE_APPLICATION;
            url = url.replace("{userId}", userId);
            url = url.replace("{type}", type);

            data = okHttpService.postDataToService(url, jsonData);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
    //endregion

    //region Get Disclaimer
    public String getDisclaimer() {

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_DISCLAIMER;
        String data = okHttpService.getDataFromService(true, url);

        return data;
    }
    //endregion

    //region Check IsRural
    public String IsRural(String villageId) {

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_ISRURAL;
        url = url.replace("{villageId}", villageId);

        String data = okHttpService.getDataFromService(IsCached, url);

        return data;
    }
    //endregion

    //region Request Call Back Type List
    public List<CustomFranchiseeApplicationSpinnerDto> getRequestCallBackTypeList() {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_REQUEST_CALL_BACK_TYPE;
        String data = okHttpService.getDataFromService(IsCached, url);
        //String data = "[{\"Id\":8,\"Name\":\"Co-Operative Society\"},{\"Id\":4,\"Name\":\"Company\"},{\"Id\":1,\"Name\":\"Individual\"},{\"Id\":10,\"Name\":\"Limited Liability Partnership Firm (LLP)\"},{\"Id\":5,\"Name\":\"NGO\"},{\"Id\":3,\"Name\":\"Partnership\"},{\"Id\":9,\"Name\":\"Proprietorship (HUF)\"},{\"Id\":2,\"Name\":\"Proprietorship (Individual)\"},{\"Id\":6,\"Name\":\"Self Help Group\"},{\"Id\":7,\"Name\":\"Trust\"}]";

        try {
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

    //region Entity Proof Type list
    public List<CustomFranchiseeApplicationSpinnerDto> getEntityProofTypelList(String entityTypeId) {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        try {
            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_GET_ENTITY_PROOF_TYPE;
            url = url.replace("{entityId}", entityTypeId);
            String data = okHttpService.getDataFromService(true, url);

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

    //region Check IOCL Already Exist
    public String IsIOCLAlreadyExist(String ioclCode, String applicationId) {

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_ISIOCL_EXIST;
        url = url.replace("{ioclCode}", ioclCode);
        url = url.replace("{nextgenFranchiseeApplicationid}", applicationId);

        String data = okHttpService.getDataFromService(IsCached, url);

        return data;
    }
    //endregion

    //region Check Require New or Existing Check
    public String IsRequireNewOrExistingCheck(String enquiryId) {

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_ISREQUIRE_NEW_OR_EXISTING_CHECK;
        url = url.replace("{enquiryId}", enquiryId);

        String data = okHttpService.getDataFromService(IsCached, url);

        return data;
    }
    //endregion

    //region Post Franchisee Application detail
    public String authenticateExistingFranchisee(String jsonData) {
        String data = null;
        try {

            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_AUTHENTICATE_EXISTING_FRANCHISEE;
            data = okHttpService.postDataToService(url, jsonData);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    //endregion

    //region get verify Pan Card Detail
    public String verifyPanCardDetail(String enquiryId, String pan_card_no, String pan_card_name) {

        String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_NAME_VERIFY_PANCARD_DETAIL;
        url = url.replace("{enquiryId}", enquiryId);
        url = url.replace("{pan_card_no}", pan_card_no);
        url = url.replace("{pan_card_name}", pan_card_name);

        String data = okHttpService.getDataFromService(IsCached, url);

        return data;
    }
    //endregion

    //region BCA Bank Name List
    public List<CustomFranchiseeApplicationSpinnerDto> getBCABankNameList() {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        try {

            String url = Constants.URL_BASE_WS_FRANCHISEE_APP + Constants.METHOD_GET_BCA_BANK_NAME_LIST;
            String data = okHttpService.getDataFromService(true, url);

            //JSONArray jsonArray = new JSONArray(data);

            Gson gson = new GsonBuilder().create();
            list = gson.fromJson(data, new TypeToken<ArrayList<CustomFranchiseeApplicationSpinnerDto>>() {
            }.getType());
            list.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    //endregion

}
