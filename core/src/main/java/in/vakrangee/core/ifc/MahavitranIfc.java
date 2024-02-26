package in.vakrangee.core.ifc;

import java.util.HashMap;

/**
 * Created by Nileshd on 4/18/2016.
 */
public interface MahavitranIfc {

    /**
     *  This method is used to call get BillDetails information for  Mahavitran  .
     *
     * @param parameters
     * @param methodName
     * @param urlName
     * @param soapAction
     * @return
     */
    public String getBillDetails(HashMap<String, String> parameters, String methodName, String urlName, String soapAction);

    /**
     *
     *  This method is used to call pay BillMahavirtan for  Mahavitran.
     *
     * @param parameters
     * @param methodName
     * @param urlName
     * @param soapAction
     * @return
     */
    public String payBillMahavirtan(HashMap<String, String> parameters, String methodName, String urlName, String soapAction);

}
