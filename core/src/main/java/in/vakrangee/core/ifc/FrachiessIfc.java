package in.vakrangee.core.ifc;

import java.util.HashMap;

/**
 * Created by Nileshd on 4/18/2016.
 */
public interface FrachiessIfc {

    /**
     * This method is used to call get current balance in your wallet.
     *
     * @param parameters
     * @param methodName
     * @param urlName
     * @param soapAction
     * @return
     */
    String getAvailableBalance(HashMap<String, String> parameters, String methodName, String urlName, String soapAction);

    /**
     * This method is used to call updateMPin .
     *
     * @param parameters
     * @param methodName
     * @param urlName
     * @param soapAction
     * @return
     */
    String updateMPin(HashMap<String, String> parameters, String methodName, String urlName, String soapAction);

    /**
     * This method is used to call get Account Statement for user .
     *
     * @param parameters
     * @param methodName
     * @param urlName
     * @param soapAction
     * @return
     */
    String getAccountStatement(HashMap<String, String> parameters, String methodName, String urlName, String soapAction);

    /**
     * This method is used to call Recharge Histroy Report for user .
     *
     * @param parameters
     * @param methodName
     * @param urlName
     * @param soapAction
     * @return
     */
    String getRechargeHistroyReport(HashMap<String, String> parameters, String methodName, String urlName, String soapAction);

    /**
     * This method is used to call get last 10 recharge  Account Statement  .
     *
     * @param parameters
     * @param methodName
     * @param urlName
     * @param soapAction
     * @return
     */

    String getAccountStatement10(HashMap<String, String> parameters, String methodName, String urlName, String soapAction);

    /**
     * This method is used to call get only Franchiess Assertion level user info display.
     *
     * @param parameters
     * @param methodName
     * @param urlName
     * @param soapAction
     * @return
     */
    String getMyHelpLine(HashMap<String, String> parameters, String methodName, String urlName, String soapAction);

    /**
     * This method is used to call Reset Mpin OR forget password.
     *
     * @param parameters
     * @param methodNameResetMpin
     * @param urlFrachisee
     * @param soapAction
     * @return
     */

    String resetMPin(HashMap<String, String> parameters, String methodNameResetMpin, String urlFrachisee, String soapAction);

    /**
     * This method is used to call  My transcation report.
     *
     * @param parameters
     * @param method_name_my_trancation
     * @param urlFrachisee
     * @param soapAction
     * @return
     */

    String getMyTransactions(HashMap<String, String> parameters, String method_name_my_trancation, String urlFrachisee, String soapAction);

    /**
     * This method is used to call curent Lat and Long  Location Log insert into server .
     *
     * @param parameters
     * @param method_name_my_trancation
     * @param urlFrachisee
     * @param soapAction
     * @return
     */

    String insertFFLocationLog(HashMap<String, String> parameters, String method_name_my_trancation, String urlFrachisee, String soapAction);

    /**
     * This method is used to call authenticate Franchisee On Upgrade your account
     * when user 2nd time register direct insert VKID and Mpin and Login.
     *
     * @param parameters
     * @param method_name_my_trancation
     * @param urlFrachisee
     * @param soapAction
     * @return
     */
    String authenticateFranchiseeOnUpgrade(HashMap<String, String> parameters, String method_name_my_trancation, String urlFrachisee, String soapAction);



}
