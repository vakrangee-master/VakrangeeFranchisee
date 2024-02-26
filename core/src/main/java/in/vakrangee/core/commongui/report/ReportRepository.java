package in.vakrangee.core.commongui.report;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import in.vakrangee.core.commongui.status.NameStatusDto;


/**
 * Created by Vasundhara on 6/26/2018.
 */

public class ReportRepository {

    private Context context;
    private static final String TAG = "ReportRepository";
    private static final String JSON_KEY_MATERIAL_NAME = "material_name";
    private static final String JSON_KEY_STATUS_DESC = "status_desc";
    private static final String JSON_KEY_STATUS_CODE = "status";

    public ReportRepository(Context context) {
        this.context = context;

    }

    /**
     * {\"PerformanceTrend\": {\"Header\": [{\"ColumnOrder\": 1,\"Header\": \"PrefPara\",\"HeaderValue\": \"Pref Para\",\"IsDefaultWidth\": false},{\"ColumnOrder\": 2,\"Header\": \"Apr\",\"HeaderValue\": \"Apr\",\"IsDefaultWidth\": false},{\"ColumnOrder\": 3,\"Header\": \"May\",\"HeaderValue\": \"May\",\"IsDefaultWidth\": false},{\"ColumnOrder\": 4,\"Header\": \"Jun\",\"HeaderValue\": \"Jun\",\"IsDefaultWidth\": false}],\"Result\": [{\"PrefPara\": \"Dr Call Average\",\"Apr\": \"13\",\"May\": \"30\",\"Jun\": \"1.9\"},{\"PrefPara\": \"Dr Coverage\",\"Apr\": \"13\",\"May\": \"30\",\"Jun\": \"1.9\"},{\"PrefPara\": \"Chem Call Average\",\"Apr\": \"13\",\"May\": \"30\",\"Jun\": \"1.9\"},{\"PrefPara\": \"Chem Coverage\",\"Apr\": \"13\",\"May\": \"30\",\"Jun\": \"1.9\"},{\"PrefPara\": \"POB\",\"Apr\": \"13\",\"May\": \"30\",\"Jun\": \"1.9\"},{\"PrefPara\": \"Sync Frequency\",\"Apr\": \"13\",\"May\": \"30\",\"Jun\": \"1.9\"}]}}
     */
    //Method to get the key set of the provided json object
    public ReportDataDto getReportDataList(String reportKey, String reportResponse) {
        ReportDataDto dataDto = null;
        try {

            if (reportResponse != null && reportResponse.length() > 0) {

                //Get JSONObject as the response
                JSONObject jsonObject = new JSONObject(reportResponse);
                if (jsonObject != null && jsonObject.length() > 0) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject(reportKey);
                    if (jsonObject1 != null && jsonObject1.length() > 0) {

                        //Get the reports header list
                        JSONArray jsonArrayHeader = jsonObject1.getJSONArray("Header");
                        if (jsonArrayHeader != null) {
                            int len = jsonArrayHeader.length();
                            if (len > 0) {
                                dataDto = new ReportDataDto();
                            }
                            for (int i = 0; i < len; i++) {

                                JSONObject jObj = jsonArrayHeader.getJSONObject(i);
                                int colOrder = jObj.getInt("ColumnOrder");
                                if (colOrder > 0) {
                                    ReportHeader reportHeader = new ReportHeader();

                                    reportHeader.setHeaderValue(jObj.getString("HeaderValue"));
                                    reportHeader.setColumnOrder(jObj.getInt("ColumnOrder"));
                                    reportHeader.setHeader(jObj.getString("Header"));
                                    reportHeader.setIsDefaultWidth(jObj.getBoolean("IsDefaultWidth"));
                                    dataDto.reportHeaderList.add(reportHeader);
                                }
                            }
                        }

                        //Get the reports Data list
                        String result = jsonObject1.getString("Result");
                        JSONArray jsonArrayData = new JSONArray(result);
                        if (jsonArrayData != null) {
                            int len = jsonArrayData.length();
                            for (int i = 0; i < len; i++) {

                                Map<String, String> mapData = new HashMap<String, String>();
                                JSONObject jObj = jsonArrayData.getJSONObject(i);
                                Iterator itr = jObj.keys();
                                while (itr.hasNext()) {

                                    String currentDynamicKey = (String) itr.next();
                                    String currentKeyValue = jObj.getString(currentDynamicKey);
                                    mapData.put(currentDynamicKey, currentKeyValue);
                                }
                                dataDto.reportDataList.add(mapData);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getReportDataList : Error in getting all the report data list : " + e.toString());
        }
        return dataDto;
    }

    public boolean IsHeaderExist(List<ReportHeader> headerList, String key) {
        for (ReportHeader d : headerList) {
            if (d.getHeader() != null && d.getHeader().contains(key)) {
                return true;
            }
        }
        return false;
    }

    public List<NameStatusDto> getNameStatusList(ReportDataDto reportDataDto) {
        List<NameStatusDto> statusList = null;

        if (reportDataDto == null || reportDataDto.reportDataList.size() == 0)
            return statusList;

        //Prepare List
        statusList = new ArrayList<NameStatusDto>();
        for (int i = 0; i < reportDataDto.reportDataList.size(); i++) {
            Map<String, String> rowData = reportDataDto.reportDataList.get(i);
            String name = rowData.get(JSON_KEY_MATERIAL_NAME);
            String status = rowData.get(JSON_KEY_STATUS_DESC);
            String statusCode = rowData.get(JSON_KEY_STATUS_CODE);

            NameStatusDto dto = new NameStatusDto();
            dto.setName(name);
            dto.setStatus(status);
            dto.setStatusCode(statusCode);

            statusList.add(dto);
        }
        return statusList;
    }


}
