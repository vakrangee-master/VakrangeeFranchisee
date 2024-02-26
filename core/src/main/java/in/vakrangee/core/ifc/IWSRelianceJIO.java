package in.vakrangee.core.ifc;

import java.util.HashMap;

/**
 * Created by Nileshd on 4/18/2016.
 */
public interface IWSRelianceJIO {

    /**
     * Get Plans List of Reliance JIO Number
     * @param parameters
     * @param methodName
     * @param urlName
     * @param soapAction
     * @return Response from Service
     */
    public String getPlans(HashMap<String, String> parameters, String methodName, String urlName, String soapAction);

    /**
     * Recharge Reliance JIO Prepaid.
     * @param parameters
     * @param methodName
     * @param urlName
     * @param soapAction
     * @return Response from Service
     */
    public String rechargeBalance(HashMap<String, String> parameters, String methodName, String urlName, String soapAction);

}
