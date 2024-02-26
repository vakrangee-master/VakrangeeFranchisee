package in.vakrangee.core.ifc;

import java.util.HashMap;

public interface IWSSupportTicketIfc {

    /**
     * Get Category list for Support Ticket
     * @param parameters
     * @param methodName
     * @param urlName
     * @param soapAction
     * @return
     */
    public String getCategory(HashMap<String, String> parameters, String methodName, String urlName, String soapAction);

    /**
     * Get Sub Category list for Support Ticket
     * @param parameters
     * @param methodName
     * @param urlName
     * @param soapAction
     * @return
     */
    public String getSubCategory(HashMap<String, String> parameters, String methodName, String urlName, String soapAction);

    /**
     * Get Issue Type list for Support Ticket
     * @param parameters
     * @param methodName
     * @param urlName
     * @param soapAction
     * @return
     */
    public String getIssueType(HashMap<String, String> parameters, String methodName, String urlName, String soapAction);
}
