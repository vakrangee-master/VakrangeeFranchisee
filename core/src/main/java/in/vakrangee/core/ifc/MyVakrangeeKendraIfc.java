package in.vakrangee.core.ifc;

import java.util.HashMap;

/**
 * Created by nileshd on 12/15/2016.
 */

public interface MyVakrangeeKendraIfc {

    String MyVakrangeeKendraimpl(HashMap<String, String> parameters, String method_name_my_vakangee_kendra, String urlMyVakrangeeKendra, String soapAction);

    String myVakrangeeKendraTimingsResponse(HashMap<String, String> parameters, String methodNameAccountStatement10, String urlFrachisee, String soapAction);

    String myVakrangeeKendraLocationDetails(HashMap<String, String> parameters, String methodNameAccountStatement10, String urlFrachisee, String soapAction);

    String myVakrangeeKendraParticular(HashMap<String, String> parameters, String methodNameAccountStatement10, String urlFrachisee, String soapAction);

    String nextgenScheduleDateTimeUpdateResponse(HashMap<String, String> parameters, String methodNameScheduleAndCallUpdate, String url, String soapAction);

    //region NextGen Site Work Commencement
    String getNextGenSiteWorkCommencementDetail(HashMap<String, String> parameters, String methodName, String url, String soapAction);
    String nextgenSiteWorkCommencementUpdate(HashMap<String, String> parameters, String methodName, String url, String soapAction);
    //endregion

    //region Work In Progress
    String getAllNextGenSiteWorkInProgressDetail(HashMap<String, String> parameters, String methodName, String url, String soapAction);
    String getNextGenSiteWorkInProgressDetail(HashMap<String, String> parameters, String methodName, String url, String soapAction);
    String nextgenSiteWorkInProgressUpdate(HashMap<String, String> parameters, String methodName, String url, String soapAction);

    //endregion

    //region Work Completion Intimation
    String getNextGenSiteWorkCompletionDetail1(HashMap<String, String> parameters, String methodName, String url, String soapAction);
    String getWorkCompletionIntimationDetail(HashMap<String, String> parameters, String methodName, String url, String soapAction);
    String getWorkCompletionCheckList(HashMap<String, String> parameters, String methodName, String url, String soapAction);
    //endregion

    //region Site Readiness and Verification
    String getSiteReadinessAndVerificationDetail(HashMap<String, String> parameters, String methodName, String url, String soapAction);

    String getReadinessAndVerificationCheckList(HashMap<String, String> parameters, String methodName, String url, String soapAction);

    String nextgenSiteReadinessAndVerificationUpdate1(HashMap<String, String> parameters, String methodName, String url, String soapAction);

    String getElemenAttributetDetail(HashMap<String, String> parameters, String methodName, String url, String soapAction);

    String validateLatLongWithAddress(HashMap<String, String> parameters, String methodName, String url, String soapAction);

    String updateUserLatLong(HashMap<String, String> parameters, String methodName, String url, String soapAction);

    //endregion
}
