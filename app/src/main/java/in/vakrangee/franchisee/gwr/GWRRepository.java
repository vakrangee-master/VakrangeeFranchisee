package in.vakrangee.franchisee.gwr;

import android.content.Context;
import android.location.Geocoder;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import in.vakrangee.supercore.franchisee.nextgenfranchiseeapplication.CustomFranchiseeApplicationSpinnerDto;
import in.vakrangee.supercore.franchisee.service.OkHttpService;
import in.vakrangee.supercore.franchisee.utils.Constants;

public class GWRRepository {

    private Context context;
    private OkHttpService okHttpService;
    private boolean IsCached = false;

    public GWRRepository(Context context) {
        this.context = context;
        okHttpService = new OkHttpService(context);
    }

    //region Get GWR Activity Details
    public String getGWRActivityDetails(String vkId) {

        String url = Constants.URL_BASE_WS_GWR_APP + Constants.METHOD_NAME_GET_GWR_ACTIVITY_DETAILS;
        url = url.replace("{VKID}", vkId);
        String data = okHttpService.getDataFromService(IsCached, url);

        return data;
    }

    public String getGWRActivityDetails(String vkId, String type) {

        String url = Constants.URL_BASE_WS_GWR_APP + Constants.METHOD_NAME_GET_GWR_ACTIVITY_DETAILS;
        url = url.replace("{VKID}", vkId);
        url = url.replace("{type}", type);
        String data = okHttpService.getDataFromService(IsCached, url);

        return data;
    }
    //endregion

    //region Get GWR Witness and Camera Details
    public String getGWRWitnessAndCameraDetails(String vkId) {

        String url = Constants.URL_BASE_WS_GWR_APP + Constants.METHOD_NAME_GET_WITNESS_AND_CAMERA_DETAILS;
        url = url.replace("{VKID}", vkId);
        String data = okHttpService.getDataFromService(IsCached, url);

        return data;
    }
    //endregion

    //region Post GWR Activity Details
    public String saveGWRActivityDetail(String jsonData, String vkId, String type) {

        String data = null;
        try {

            String url = Constants.URL_BASE_WS_GWR_APP + Constants.METHOD_NAME_SAVE_GWR_ACTIVITY_DETAILS;
            url = url.replace("{VKID}", vkId);
            url = url.replace("{type}", type);

            data = okHttpService.postDataToService(url, jsonData);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
    //endregion

    //region Get GWR Attendance Details
    public String getGWRAttendanceDetails(String vkId) {

        String url = Constants.URL_BASE_WS_GWR_APP + Constants.METHOD_NAME_GET_GWR_ATTENDANCE_DETAILS;
        url = url.replace("{VKID}", vkId);
        String data = okHttpService.getDataFromService(IsCached, url);

        return data;
    }
    //endregion

    //region Post GWR Attendance Details
    public String saveGWRAttendanceDetail(String vkId, String jsonData) {

        String data = null;
        try {

            String url = Constants.URL_BASE_WS_GWR_APP + Constants.METHOD_NAME_SAVE_GWR_ATTENDANCE_DETAILS;
            url = url.replace("{VKID}", vkId);

            data = okHttpService.postDataToService(url, jsonData);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
    //endregion

    //region Post Save Witness And Camera
    public String saveWitnessAndCamera(String vkid, String jsonData) {

        String data = null;
        try {
            String url = Constants.URL_BASE_WS_GWR_APP + Constants.METHOD_NAME_SAVE_WITNESS_AND_CAMERA;
            url = url.replace("{vkId}", vkid);

            data = okHttpService.postDataToService(url, jsonData);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
    //endregion

    //region Occupations List
    public List<CustomFranchiseeApplicationSpinnerDto> getOccupationsList() {
        List<CustomFranchiseeApplicationSpinnerDto> list = new ArrayList<>();

        String url = Constants.URL_BASE_WS_GWR_APP + Constants.METHOD_NAME_GET_OCCUPATION_LIST;
        String data = okHttpService.getDataFromService(IsCached, url);

        try {
            JSONArray jsonArray = new JSONArray(data);

            Gson gson = new GsonBuilder().create();
            list = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<CustomFranchiseeApplicationSpinnerDto>>() {
            }.getType());

            list.add(0, new CustomFranchiseeApplicationSpinnerDto("-1", "Please Select"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    //endregion

    //region Get Complete Address Using Lat and Long
    public String getCompleteAdressString(Context context, double LATITUDE, double LONGITUDE) {

        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<android.location.Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);

            if (addresses != null) {

                android.location.Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(",");
                }

                strAdd = strReturnedAddress.toString();

                Log.e("My Current loction address", "" + strReturnedAddress.toString());
            } else {
                Log.e("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }
    //endregion

    //region Get GWR Dashboard Details
    public String getGWRDashboardDetails(String vkId) {

        String url = Constants.URL_BASE_WS_GWR_APP + Constants.METHOD_NAME_CHECK_GWR_DETAILS;
        url = url.replace("{VKID}", vkId);
        String data = okHttpService.getDataFromService(IsCached, url);

        return data;
    }
    //endregion

    //region Post Save GWR InAuguration
    public String saveGWRInAuguration(String vkid, String jsonData) {

        String data = null;
        try {
            String url = Constants.URL_BASE_WS_GWR_APP + Constants.METHOD_NAME_UPDATE_GWR_DASHBOARD_DETAILS;
            url = url.replace("{VKID}", vkid);

            data = okHttpService.postDataToService(url, jsonData);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
    //endregion

    //region Get Upload Witness Statement
    public String getUploadWitnesStatement(String vkId) {

        String url = Constants.URL_BASE_WS_GWR_APP + Constants.METHOD_NAME_GET_WITNESS_STATEMENT;
        url = url.replace("{VKID}", vkId);
        String data = okHttpService.getDataFromService(IsCached, url);

        return data;
    }
    //endregion

    //region Post Witness Statement
    public String saveWitnessStatement(String vkId, String jsonData) {

        String data = null;
        try {

            String url = Constants.URL_BASE_WS_GWR_APP + Constants.METHOD_NAME_SAVE_WITNESS_STATEMENT;
            url = url.replace("{VKID}", vkId);

            data = okHttpService.postDataToService(url, jsonData);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
    //endregion

    //region Get Guinness Event Photo Details
    public String getGWREventPhotoDetails(String vkId) {

        String url = Constants.URL_BASE_WS_GWR_APP + Constants.METHOD_NAME_GWR_EVENT_PHOTO_DETAILS;
        url = url.replace("{VKID}", vkId);
        String data = okHttpService.getDataFromService(IsCached, url);

        return data;
    }
    //endregion

    //region Post Save Event Photo
    public String saveEventPhoto(String vkid, String jsonData) {

        String data = null;
        try {
            String url = Constants.URL_BASE_WS_GWR_APP + Constants.METHOD_NAME_SAVE_EVENT_PHOTO_DETAILS;
            url = url.replace("{VKID}", vkid);

            data = okHttpService.postDataToService(url, jsonData);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
    //endregion

    //region Post Save InAuguration Image
    public String saveInAugurationImage(String vkid, String jsonData) {

        String data = null;
        try {
            String url = Constants.URL_BASE_WS_GWR_APP + Constants.METHOD_NAME_SAVE_INAUGURATION_PHOTO;
            url = url.replace("{VKID}", vkid);

            data = okHttpService.postDataToService(url, jsonData);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
    //endregion
}
