package in.vakrangee.core.ifc;

import java.util.HashMap;

/**
 * Created by Nileshd on 4/18/2016.
 */
public interface CyberPlatIfc {

    /**
     *  This method is used to call webservice.
     *
     * @param parameters
     * @param methodName
     * @param urlName
     * @param soapAction
     * @return
     */
    public String callWebService(HashMap<String, String> parameters, String methodName, String urlName, String soapAction);

}
