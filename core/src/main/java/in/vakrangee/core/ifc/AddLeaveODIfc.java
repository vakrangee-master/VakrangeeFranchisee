package in.vakrangee.core.ifc;

import java.util.HashMap;

/**
 * Created by Nileshd on 1/6/2017.
 */

public interface AddLeaveODIfc {

    public String addLeaveOD(HashMap<String, String> parameters, String methodName, String urlName, String soapAction,String namespace);

    public String getMyLeaveODList(HashMap<String, String> parameters, String methodName, String urlName, String soapAction,String namespace);

    public String updateStatusLeaveOD(HashMap<String, String> parameters, String methodName, String urlName, String soapAction,String namespace);

}
