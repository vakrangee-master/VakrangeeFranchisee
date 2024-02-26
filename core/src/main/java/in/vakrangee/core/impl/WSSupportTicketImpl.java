package in.vakrangee.core.impl;

import java.util.HashMap;

import in.vakrangee.core.ifc.IWSSupportTicketIfc;
import in.vakrangee.core.utils.Constants;
import in.vakrangee.core.utils.WebServiceUtils;

public class WSSupportTicketImpl implements IWSSupportTicketIfc {

    private static final String TAG = "WSSupportTicketImpl";

    @Override
    public String getCategory(HashMap<String, String> parameters, String methodName, String urlName, String soapAction) {
        //return WebServiceUtils.getInstance().sendRequest(TAG, parameters, methodName, urlName, soapAction);
        return WebServiceUtils.getInstance().sendRequest(TAG, Constants.NAMESPACE_WS_SUPPORT_TICKET, parameters, methodName, urlName, soapAction);
    }

    @Override
    public String getSubCategory(HashMap<String, String> parameters, String methodName, String urlName, String soapAction) {
        //return WebServiceUtils.getInstance().sendRequest(TAG, parameters, methodName, urlName, soapAction);
        return WebServiceUtils.getInstance().sendRequest(TAG, Constants.NAMESPACE_WS_SUPPORT_TICKET, parameters, methodName, urlName, soapAction);
    }

    @Override
    public String getIssueType(HashMap<String, String> parameters, String methodName, String urlName, String soapAction) {
        //return WebServiceUtils.getInstance().sendRequest(TAG, parameters, methodName, urlName, soapAction);
        return WebServiceUtils.getInstance().sendRequest(TAG, Constants.NAMESPACE_WS_SUPPORT_TICKET, parameters, methodName, urlName, soapAction);
    }
}
