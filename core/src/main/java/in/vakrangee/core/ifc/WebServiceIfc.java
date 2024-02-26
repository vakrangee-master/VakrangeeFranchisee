package in.vakrangee.core.ifc;

import java.util.HashMap;

/**
 * Created by Nileshd on 4/18/2016.
 */
public interface WebServiceIfc {

    /**
     *  This method is used to call webservice.
     *
     * @param parameters
     * @param methodName
     * @param urlName
     * @param soapAction
     * @return
     */
    public String registerFranchisee(HashMap<String, String> parameters, String methodName, String urlName, String soapAction);

    public String verifyOtp(HashMap<String, String> parameters, String methodName, String urlName, String soapAction);


    public String authenticateFranchiseeWithToken(HashMap<String, String> parameters, String methodName, String urlName, String soapAction);

    public String logOutFranchisee(HashMap<String, String> parameters, String methodName, String urlName, String soapAction);

    public String authenticateFranchisee(HashMap<String, String> parameters, String methodName, String urlName, String soapAction);

    public  String resendOtp(HashMap<String, String> parameters, String methodNameResendOtp, String urlVkmsAccessControl, String soapAction);

    public  String checkversion(HashMap<String, String> parameters, String methodNameCheckVersion, String urlVkmsAccessControl, String soapAction);

    public String updateFCMId(HashMap<String, String> parameters, String methodNameUpdateFCMId, String urlVkmsAccessControl, String soapAction);

    public String authenticateUser(HashMap<String, String> parameters, String methodName, String urlName, String soapAction);
}
