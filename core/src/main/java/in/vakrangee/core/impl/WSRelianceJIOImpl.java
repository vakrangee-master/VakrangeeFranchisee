package in.vakrangee.core.impl;

import java.util.HashMap;

import in.vakrangee.core.ifc.IWSRelianceJIO;
import in.vakrangee.core.utils.Constants;
import in.vakrangee.core.utils.WebServiceUtils;

/**
 * Created by Nileshd on 6/8/2016.
 */
public class WSRelianceJIOImpl implements IWSRelianceJIO {

    private final String TAG = WSRelianceJIOImpl.class.getCanonicalName();

    /**
     * This method is used to call WebServiceCyberPlatImpl.
     *
     * @param parameters
     * @param methodName
     * @param urlName
     * @param soapAction
     * @return
     */
    @Override
    public String getPlans(HashMap<String, String> parameters, String methodName, String urlName, String soapAction) {
        return WebServiceUtils.getInstance().sendRequest(TAG, Constants.NAMESPACE_WS_RELIANCE_JIO, parameters, methodName, urlName, soapAction);
    }

    @Override
    public String rechargeBalance(HashMap<String, String> parameters, String methodName, String urlName, String soapAction) {
        return WebServiceUtils.getInstance().sendRequest(TAG, Constants.NAMESPACE_WS_RELIANCE_JIO, parameters, methodName, urlName, soapAction);
    }
}
