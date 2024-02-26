package in.vakrangee.core.webservice;

import java.util.HashMap;

import in.vakrangee.core.impl.WSRelianceJIOImpl;
import in.vakrangee.core.impl.WSSupportTicketImpl;
import in.vakrangee.core.impl.WebServiceAccessControlImpl;
import in.vakrangee.core.impl.WebServiceAddLeaveODImpl;
import in.vakrangee.core.impl.WebServiceCyberPlatImpl;
import in.vakrangee.core.impl.WebServiceFranchiseeImpl;
import in.vakrangee.core.impl.WebServiceMahavitranImpl;
import in.vakrangee.core.impl.WebServiceMyVakrangeeKendraImpl;
import in.vakrangee.core.impl.WebServiceWSAttendance1Impl;
import in.vakrangee.core.utils.Constants;

import static in.vakrangee.core.utils.Constants.mgetData;

/**
 * Created by Nileshd on 4/25/2016.
 */
public class WebService {

    private static WebServiceFranchiseeImpl webServiceFranchiseeImpl = new WebServiceFranchiseeImpl();

    public static String registerFranchisee(String vkid, String name, String email, String mobilenumber, String imei,
                                            String deviceId,
                                            String opertorname, String password, String status, String osplateform,
                                            String Mversion) {

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkid);
        parameters.put("name", name);
        parameters.put("emailId", email);
        parameters.put("mobileNo", mobilenumber);
        parameters.put("IMEINo", imei);
        parameters.put("deviceId", deviceId);
        parameters.put("simNo", opertorname);
        parameters.put("password", password);
        parameters.put("status", status);
        parameters.put("osPlatform", osplateform);
        parameters.put("MVersion", Mversion);

        WebServiceAccessControlImpl webService = new WebServiceAccessControlImpl();
        return webService.registerFranchisee(parameters, Constants.METHOD_NAME_REGISTER_FREANCHISEE,
                Constants.URL_VKMS_ACCESS_CONTROL,
                Constants.SOAP_ACTION);

    }


    public static String otpVerfiy(String vkid, String otp, String imei, String deviceId, String opertorname) {


        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkid);
        parameters.put("otp", otp);
        parameters.put("IMEINo", imei);
        parameters.put("deviceId", deviceId);
        parameters.put("simNo", opertorname);


        WebServiceAccessControlImpl webService = new WebServiceAccessControlImpl();
        return webService.verifyOtp(parameters, Constants.METHOD_NAME_VERFITY_OTP, Constants.URL_VKMS_ACCESS_CONTROL,
                Constants.SOAP_ACTION);
    }

    public static String getAvailableBalance(String vkid, String tokenId, String IMEINo, String deviceId, String simNo) {

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkid);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", IMEINo);
        parameters.put("deviceId", deviceId);
        parameters.put("simNo", simNo);

        WebServiceFranchiseeImpl webServiceFrachiessIfc = new WebServiceFranchiseeImpl();
        return webServiceFrachiessIfc.getAvailableBalance(parameters, Constants.METHOD_NAME_GET_AVAILABLE_BALANCE,
                Constants.URL_FRACHISEE,
                Constants.SOAP_ACTION);


    }


    public static String updateMPin(String vkid, String mpin, String imei, String deviceId, String opertorname) {

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkid);
        parameters.put("MPin", mpin);
        parameters.put("IMEINo", imei);
        parameters.put("deviceId", deviceId);
        parameters.put("simNo", opertorname);
        WebServiceFranchiseeImpl webServiceFrachiessIfc = new WebServiceFranchiseeImpl();
        return webServiceFrachiessIfc.updateMPin(parameters, Constants.METHOD_NAME_UPDATE_MPIN, Constants.URL_FRACHISEE,
                Constants.SOAP_ACTION);

    }


    public static String authenticateFranchisee(String vkid, String mpin, String tokenid, String imei, String deviceId,
                                                String opertorname) {

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkid);
        parameters.put("MPin", mpin);
        parameters.put("tokenId", tokenid);
        parameters.put("IMEINo", imei);
        parameters.put("deviceId", deviceId);
        parameters.put("simNo", opertorname);

        WebServiceAccessControlImpl webService = new WebServiceAccessControlImpl();
        return webService.authenticateFranchiseeWithToken(parameters, Constants.METHOD_NAME_AUTHENTICATE_FRACHISEE,
                Constants.URL_VKMS_ACCESS_CONTROL,
                Constants.SOAP_ACTION);

    }


    public static String logOutFranchisee(String vkid, String imei, String deviceid, String simopertaor) {


        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkid);
        parameters.put("IMEINo", imei);
        parameters.put("deviceId", deviceid);
        parameters.put("simNo", simopertaor);


        WebServiceAccessControlImpl webService = new WebServiceAccessControlImpl();
        return webService.logOutFranchisee(parameters, Constants.METHOD_NAME_LOGOUT_FRACHISEE,
                Constants.URL_VKMS_ACCESS_CONTROL,
                Constants.SOAP_ACTION);

    }

    public static String authenticateFranchiseewithoutTid(String vkId, String mPin, String imei, String deviceid,
                                                          String simserialnumber) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkId);
        parameters.put("MPin", mPin);
        parameters.put("IMEINo", imei);
        parameters.put("deviceId", deviceid);
        parameters.put("simNo", simserialnumber);


        WebServiceAccessControlImpl webService = new WebServiceAccessControlImpl();
        return webService.authenticateFranchisee(parameters, Constants.METHOD_NAME_AUTHENTICATE_FRACHISEE_WITHOUT_TOKEN,
                Constants.URL_VKMS_ACCESS_CONTROL,
                Constants.SOAP_ACTION);

    }

    public static String MobileRecharge(String vkidd, String tokenId, String imei, String deviceid, String simserialnumber,
                                        String serviceProvider, String rechargeType, String mobileNumber, String amount) {


        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkidd);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", imei);
        parameters.put("deviceId", deviceid);
        parameters.put("simNo", simserialnumber);
        parameters.put("serviceProvider", serviceProvider);
        parameters.put("rechargeType", rechargeType);
        parameters.put("mobileNumber", mobileNumber);
        parameters.put("amount", amount);


        WebServiceCyberPlatImpl webServiceCyberPlat = new WebServiceCyberPlatImpl();
        return webServiceCyberPlat.callWebService(parameters, Constants.METHOD_NAME, Constants.URL_CYBER_PLAT,
                Constants.SOAP_ACTION);

    }

    public static String getBillDetails(String vkidreg, String tokenid, String imei, String deviceid, String simserialnumber,
                                        String billUnitNo, String consumerNumber) {

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkidreg);
        parameters.put("tokenId", tokenid);
        parameters.put("IMEINo", imei);
        parameters.put("deviceId", deviceid);
        parameters.put("simNo", simserialnumber);
        parameters.put("billingUnit", billUnitNo);
        parameters.put("consumerNumber", consumerNumber);

        WebServiceMahavitranImpl webServiceMahavitran = new WebServiceMahavitranImpl();
        return webServiceMahavitran.getBillDetails(parameters, Constants.METHOD_NAME_GET_BILL_DETAILS,
                Constants.URL_MAHAVITRAN,
                Constants.SOAP_ACTION);

    }

    public static String payBillMahavirtan(String vkidd, String tokenId, String imei, String deviceid, String simserialnumber,
                                           String billingUnit, String consumerNumber, String consumerMobileNumber,
                                           String consumerEmailId) {

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkidd);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", imei);
        parameters.put("deviceId", deviceid);
        parameters.put("simNo", simserialnumber);
        parameters.put("billingUnit", billingUnit);
        parameters.put("consumerNumber", consumerNumber);
        parameters.put("consumerMobileNumber", consumerMobileNumber);
        parameters.put("consumerEmailId", consumerEmailId);

        WebServiceMahavitranImpl webServiceMahavitran = new WebServiceMahavitranImpl();
        return webServiceMahavitran.payBillMahavirtan(parameters, Constants.METHOD_NAME_PAYBILL, Constants.URL_MAHAVITRAN,
                Constants.SOAP_ACTION);


    }

    public static String getAccountStatement(String vkidd, String tokenId, String imei, String deviceid, String simserialnumber,
                                             String fromDate, String toDate, String serviceProvider) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkidd);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", imei);
        parameters.put("deviceId", deviceid);
        parameters.put("simNo", simserialnumber);
        parameters.put("fromDate", fromDate);
        parameters.put("toDate", toDate);
        parameters.put("subServiceId", serviceProvider);


        WebServiceFranchiseeImpl webAccountStmt = new WebServiceFranchiseeImpl();
        return webAccountStmt.getAccountStatement(parameters, Constants.METHOD_NAME_ACCOUNT_STATEMENT, Constants.URL_FRACHISEE,
                Constants.SOAP_ACTION);
    }

    public static String getRechargeHistroyReport(String vkidd, String tokenId, String imei, String deviceid,
                                                  String simserialnumber,
                                                  String fromDate, String toDate) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkidd);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", imei);
        parameters.put("deviceId", deviceid);
        parameters.put("simNo", simserialnumber);
        parameters.put("fromDate", fromDate);
        parameters.put("toDate", toDate);


        WebServiceFranchiseeImpl webAccountStmt = new WebServiceFranchiseeImpl();
        return webAccountStmt.getRechargeHistroyReport(parameters, Constants.METHOD_NAME_RECHARGE_HISTROY, Constants.URL_FRACHISEE,
                Constants.SOAP_ACTION);
    }

    public static String getAccountStatement10(String vkidd, String tokenId, String imei, String deviceid, String simserialnumber) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkidd);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", imei);
        parameters.put("deviceId", deviceid);
        parameters.put("simNo", simserialnumber);


        WebServiceFranchiseeImpl webAccountStmt = new WebServiceFranchiseeImpl();
        return webAccountStmt.getAccountStatement10(parameters, Constants.METHOD_NAME_ACCOUNT_STATEMENT_10, Constants.URL_FRACHISEE,
                Constants.SOAP_ACTION);
    }

    public static String resendOtp(String vkidreg, String imei, String deviceid, String simserialnumber) {

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkidreg);
        parameters.put("IMEINo", imei);
        parameters.put("deviceId", deviceid);
        parameters.put("simNo", simserialnumber);


        WebServiceAccessControlImpl webService = new WebServiceAccessControlImpl();
        return webService.resendOtp(parameters, Constants.METHOD_NAME_RESEND_OTP, Constants.URL_VKMS_ACCESS_CONTROL,
                Constants.SOAP_ACTION);
    }

    public static String checkVersion(String versioncode) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("version", versioncode);


        WebServiceAccessControlImpl webService = new WebServiceAccessControlImpl();
        return webService.checkversion(parameters, Constants.METHOD_NAME_CHECK_VERSION, Constants.URL_VKMS_ACCESS_CONTROL,
                Constants.SOAP_ACTION);
    }

    public static String getMyHelpLine(String vkidd, String tokenId, String imei, String deviceid, String simserialnumber) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkidd);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", imei);
        parameters.put("deviceId", deviceid);
        parameters.put("simNo", simserialnumber);


        WebServiceFranchiseeImpl webAccountStmt = new WebServiceFranchiseeImpl();
        return webAccountStmt.getMyHelpLine(parameters, Constants.METHOD_NAME_MY_HELPLINE, Constants.URL_FRACHISEE,
                Constants.SOAP_ACTION);
    }

    public static String resetMPin(String vkidd, String imei, String deviceid, String simserialnumber) {


        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkidd);
        parameters.put("IMEINo", imei);
        parameters.put("deviceId", deviceid);
        parameters.put("simNo", simserialnumber);


        WebServiceFranchiseeImpl webAccountStmt = new WebServiceFranchiseeImpl();
        return webAccountStmt.resetMPin(parameters, Constants.METHOD_NAME_RESET_MPIN, Constants.URL_FRACHISEE,
                Constants.SOAP_ACTION);
    }

    public static String getMyTransactions(String vkidd, String tokenId, String imei, String deviceid, String simserialnumber, String fromDate, String toDate, String serviceProvider) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkidd);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", imei);
        parameters.put("deviceId", deviceid);
        parameters.put("simNo", simserialnumber);
        parameters.put("fromDate", fromDate);
        parameters.put("toDate", toDate);
        parameters.put("subServiceId", serviceProvider);


        WebServiceFranchiseeImpl webAccountStmt = new WebServiceFranchiseeImpl();
        return webAccountStmt.getMyTransactions(parameters, Constants.METHOD_NAME_My_TRANCATION, Constants.URL_FRACHISEE,
                Constants.SOAP_ACTION);
    }

    public static String insertFFLocationLog(String vkdi, String imei, String deviceid, String simserialnumber,
                                             String date, String latitu, String longit, String accurcy, String getAltitude,
                                             String batteryStatus, String networkStrengthStatus) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkdi);

        parameters.put("IMEINo", imei);
        parameters.put("deviceId", deviceid);
        parameters.put("simNo", simserialnumber);
        parameters.put("deviceDateTime", date);
        parameters.put("latitude", latitu);
        parameters.put("longitude", longit);
        parameters.put("accuracyLevel", accurcy);
        parameters.put("altitude", getAltitude);
        parameters.put("batteryStatus", batteryStatus);
        parameters.put("networkStrengthStatus", networkStrengthStatus);


        WebServiceFranchiseeImpl webAccountStmt = new WebServiceFranchiseeImpl();
        return webAccountStmt.insertFFLocationLog(parameters, Constants.METHOD_NAME_INSERT_FF_LOCATION, Constants.URL_FRACHISEE,
                Constants.SOAP_ACTION);
    }

    public static String authenticateFranchiseeOnUpgrade(String vkid, String mPin, String imei, String deviceid, String simserialnumber) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkid);
        parameters.put("IMEINo", imei);
        parameters.put("MPin", mPin);
        parameters.put("deviceId", deviceid);
        parameters.put("simNo", simserialnumber);


        WebServiceFranchiseeImpl webAccountStmt = new WebServiceFranchiseeImpl();
        return webAccountStmt.authenticateFranchiseeOnUpgrade(parameters, Constants.METHOD_NAME_AUTHENTICATE_FRACHISEE_ON_UPGRADE, Constants.URL_VKMS_ACCESS_CONTROL,
                Constants.SOAP_ACTION);
    }

    public static String myVakrangeeKendra(String vkid, String Tokenid, String imei, String deviceid, String simserialnumber,
                                           String isMondayClosed, String isTuesdayClosed, String isWednesdayClosed,
                                           String isThursdayClosed, String isFridayClosed, String isSaturdayClosed,
                                           String isSundayClosed, String mondayO, String mondayC, String tuesdayO,
                                           String tuesdayC, String wednesdayO, String wednesdayC, String thursdayO,
                                           String thursdayC, String fridayO, String fridayC, String saturdayO,
                                           String saturdayC, String sundayO, String sundayC, String vkLatitude,
                                           String vkLongitude, String frontage, String leftWall, String frontWall,
                                           String rightWall, String backWall, String ceiling, String floor, String extra1,
                                           String extra2, String extra3) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkid);
        parameters.put("tokenId", Tokenid);
        parameters.put("IMEINo", imei);
        parameters.put("deviceId", deviceid);
        parameters.put("simNo", simserialnumber);

        parameters.put("isMondayClosed", isMondayClosed);
        parameters.put("isTuesdayClosed", isTuesdayClosed);
        parameters.put("isWednesdayClosed", isWednesdayClosed);
        parameters.put("isThursdayClosed", isThursdayClosed);
        parameters.put("isFridayClosed", isFridayClosed);
        parameters.put("isSaturdayClosed", isSaturdayClosed);
        parameters.put("isSundayClosed", isSundayClosed);
        parameters.put("mondayO", mondayO);
        parameters.put("mondayC", mondayC);
        parameters.put("tuesdayO", tuesdayO);
        parameters.put("tuesdayC", tuesdayC);
        parameters.put("wednesdayO", wednesdayO);
        parameters.put("wednesdayC", wednesdayC);
        parameters.put("thursdayO", thursdayO);
        parameters.put("thursdayC", thursdayC);
        parameters.put("fridayO", fridayO);
        parameters.put("fridayC", fridayC);
        parameters.put("saturdayO", saturdayO);
        parameters.put("saturdayC", saturdayC);
        parameters.put("sundayO", sundayO);
        parameters.put("sundayC", sundayC);
        parameters.put("vkLatitude", vkLatitude);
        parameters.put("vkLongitude", vkLongitude);
        parameters.put("frontage", frontage);
        parameters.put("leftWall", leftWall);
        parameters.put("frontWall", frontWall);
        parameters.put("rightWall", rightWall);
        parameters.put("backWall", backWall);
        parameters.put("ceiling", ceiling);
        parameters.put("floor", floor);
        parameters.put("extra1", extra1);
        parameters.put("extra2", extra2);
        parameters.put("extra3", extra3);

        WebServiceMyVakrangeeKendraImpl webAccountStmt = new WebServiceMyVakrangeeKendraImpl();
        return webAccountStmt.MyVakrangeeKendraimpl(parameters, Constants.METHOD_NAME_MY_VKARANGEE_KENDRA, Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);
    }

    public static String myVakrangeeKendraTimingsResponse(String vkidd, String tokenId, String imei, String deviceid, String simserialnumber) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkidd);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", imei);
        parameters.put("deviceId", deviceid);
        parameters.put("simNo", simserialnumber);


        WebServiceMyVakrangeeKendraImpl webAccountStmt = new WebServiceMyVakrangeeKendraImpl();
        return webAccountStmt.myVakrangeeKendraTimingsResponse(parameters, Constants.METHOD_NAME_MY_VKARANGEE_KENDRA_TIMING_RESPONSE, Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);
    }

    public static String myVakrangeeKendraGeoCordinates(String vkidd, String tokenId, String imei, String deviceid, String simserialnumber) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkidd);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", imei);
        parameters.put("deviceId", deviceid);
        parameters.put("simNo", simserialnumber);


        WebServiceMyVakrangeeKendraImpl webAccountStmt = new WebServiceMyVakrangeeKendraImpl();
        return webAccountStmt.myVakrangeeKendraTimingsResponse(parameters, Constants.METHOD_NAME_MY_VKARANGEE_KENDRA_GEO_CORDINATES, Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);
    }

    public static String addLeaveOD(String vkidd, String tokenId, String imei, String deviceid, String simserialnumber, String leaveTypeId,
                                    String fromDate, String toDate, String reason) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkidd);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", imei);
        parameters.put("deviceId", deviceid);
        parameters.put("simNo", simserialnumber);
        parameters.put("leaveTypeId", leaveTypeId);
        parameters.put("startDate", fromDate);
        parameters.put("endDate", toDate);
        parameters.put("reason", reason);


        WebServiceAddLeaveODImpl webServiceAddLeaveOD = new WebServiceAddLeaveODImpl();
        return webServiceAddLeaveOD.addLeaveOD(parameters, Constants.METHOD_NAME_ADD_LEAVE_OD, Constants.URL_ADD_LEAVE_OD,
                Constants.SOAP_ACTION, Constants.NAMESPACE_WS_ATTENDEANCE);
    }


    public static String getMyLeaveODList(String vkidd, String tokenId, String imei, String deviceid, String simserialnumber, String leaveOdId) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkidd);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", imei);
        parameters.put("deviceId", deviceid);
        parameters.put("simNo", simserialnumber);
        parameters.put("leaveOdId", leaveOdId);


        WebServiceAddLeaveODImpl webServiceAddLeaveOD = new WebServiceAddLeaveODImpl();
        return webServiceAddLeaveOD.getMyLeaveODList(parameters, Constants.METHOD_NAME_GET_MY_LEAVE_OD_LIST, Constants.URL_ADD_LEAVE_OD,
                Constants.SOAP_ACTION, Constants.NAMESPACE_WS_ATTENDEANCE);
    }

    public static String updateStatusLeaveOD(String vkidd, String tokenId, String imei, String deviceid, String simserialnumber, String leaveTypeId, String leaveOdStatus, String leaveOdReason) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkidd);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", imei);
        parameters.put("deviceId", deviceid);
        parameters.put("simNo", simserialnumber);
        parameters.put("leaveOdId", leaveTypeId);
        parameters.put("leaveOdStatus", leaveOdStatus);
        parameters.put("leaveOdReason", leaveOdReason);

        WebServiceAddLeaveODImpl webServiceAddLeaveOD = new WebServiceAddLeaveODImpl();
        return webServiceAddLeaveOD.getMyLeaveODList(parameters, Constants.METHOD_NAME_UPDATE_STATUS_LEAVE_OD, Constants.URL_ADD_LEAVE_OD,
                Constants.SOAP_ACTION, Constants.NAMESPACE_WS_ATTENDEANCE);
    }

    public static String myVakrangeeKendraLocationDetails(String vkidd, String tokenId, String imei, String deviceid, String simserialnumber, String scope, String locationCode) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkidd);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", imei);
        parameters.put("deviceId", deviceid);
        parameters.put("simNo", simserialnumber);
        parameters.put("scope", scope);
        parameters.put("locationCode", locationCode);


        WebServiceMyVakrangeeKendraImpl webServiceAddLeaveOD = new WebServiceMyVakrangeeKendraImpl();
        return webServiceAddLeaveOD.myVakrangeeKendraLocationDetails(parameters, Constants.METHOD_NAME_MY_VAKRANGEE_LOCATION_DETAILS, Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);
    }


    public static String myVakrangeeKendraTimingsParticular(String userId, String vkidd, String tokenId, String imei, String deviceid, String simserialnumber) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("userId", userId);
        parameters.put("vkId", vkidd);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", imei);
        parameters.put("deviceId", deviceid);
        parameters.put("simNo", simserialnumber);


        WebServiceMyVakrangeeKendraImpl webAccountStmt = new WebServiceMyVakrangeeKendraImpl();
        return webAccountStmt.myVakrangeeKendraTimingsResponse(parameters, Constants.METHOD_NAME_MY_VKARANGEE_KENDRA_TIMING_RESPONSE_PARTICULAR, Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);
    }

    public static String myVakrangeeKendraGeoCordinatesParticular(String userId, String vkidd, String tokenId, String imei, String deviceid, String simserialnumber) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("userId", userId);
        parameters.put("vkId", vkidd);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", imei);
        parameters.put("deviceId", deviceid);
        parameters.put("simNo", simserialnumber);


        WebServiceMyVakrangeeKendraImpl webAccountStmt = new WebServiceMyVakrangeeKendraImpl();
        return webAccountStmt.myVakrangeeKendraTimingsResponse(parameters, Constants.METHOD_NAME_MY_VKARANGEE_KENDRA_GEO_CORDINATES_PARTICULAR, Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);
    }

    public static String myVakrangeeKendraParticular(String userid, String vkid, String Tokenid, String imei, String deviceid, String simserialnumber,
                                                     String isMondayClosed, String isTuesdayClosed, String isWednesdayClosed,
                                                     String isThursdayClosed, String isFridayClosed, String isSaturdayClosed,
                                                     String isSundayClosed, String mondayO, String mondayC, String tuesdayO,
                                                     String tuesdayC, String wednesdayO, String wednesdayC, String thursdayO,
                                                     String thursdayC, String fridayO, String fridayC, String saturdayO,
                                                     String saturdayC, String sundayO, String sundayC, String vkLatitude,
                                                     String vkLongitude, String frontage, String leftWall, String frontWall,
                                                     String rightWall, String backWall, String ceiling, String floor, String extra1,
                                                     String extra2, String extra3) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("userId", userid);
        parameters.put("vkId", vkid);
        parameters.put("tokenId", Tokenid);
        parameters.put("IMEINo", imei);
        parameters.put("deviceId", deviceid);
        parameters.put("simNo", simserialnumber);
        parameters.put("isMondayClosed", isMondayClosed);
        parameters.put("isTuesdayClosed", isTuesdayClosed);
        parameters.put("isWednesdayClosed", isWednesdayClosed);
        parameters.put("isThursdayClosed", isThursdayClosed);
        parameters.put("isFridayClosed", isFridayClosed);
        parameters.put("isSaturdayClosed", isSaturdayClosed);
        parameters.put("isSundayClosed", isSundayClosed);
        parameters.put("mondayO", mondayO);
        parameters.put("mondayC", mondayC);
        parameters.put("tuesdayO", tuesdayO);
        parameters.put("tuesdayC", tuesdayC);
        parameters.put("wednesdayO", wednesdayO);
        parameters.put("wednesdayC", wednesdayC);
        parameters.put("thursdayO", thursdayO);
        parameters.put("thursdayC", thursdayC);
        parameters.put("fridayO", fridayO);
        parameters.put("fridayC", fridayC);
        parameters.put("saturdayO", saturdayO);
        parameters.put("saturdayC", saturdayC);
        parameters.put("sundayO", sundayO);
        parameters.put("sundayC", sundayC);
        parameters.put("vkLatitude", vkLatitude);
        parameters.put("vkLongitude", vkLongitude);
        parameters.put("frontage", frontage);
        parameters.put("leftWall", leftWall);
        parameters.put("frontWall", frontWall);
        parameters.put("rightWall", rightWall);
        parameters.put("backWall", backWall);
        parameters.put("ceiling", ceiling);
        parameters.put("floor", floor);
        parameters.put("extra1", extra1);
        parameters.put("extra2", extra2);
        parameters.put("extra3", extra3);

        WebServiceMyVakrangeeKendraImpl webAccountStmt = new WebServiceMyVakrangeeKendraImpl();
        return webAccountStmt.myVakrangeeKendraParticular(parameters, Constants.METHOD_NAME_MY_VKARANGEE_KENDRA_PARTICULAR, Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);
    }

    public static String getNearByVakrangeeKendra(String vkidd, String tokenId, String imei, String deviceid, String simserialnumber, String latitude, String longitude) {
        HashMap<String, String> parameters = new HashMap<>();

        parameters.put("vkId", vkidd);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", imei);
        parameters.put("deviceId", deviceid);
        parameters.put("simNo", simserialnumber);
        parameters.put("latitude", latitude);
        parameters.put("longitude", longitude);

        WebServiceWSAttendance1Impl webAccountStmt = new WebServiceWSAttendance1Impl();
        return webAccountStmt.getNearByVakrangeeKendra(parameters, Constants.METHOD_NAME_WS_ATTEDANCE, Constants.URL_WS_ATTENDANCE,
                Constants.SOAP_ACTION, Constants.NAMESPACE_WS_ATTENDEANCE);
    }

    public static String getATMList(String vkidd) {
        HashMap<String, String> parameters = new HashMap<>();

        parameters.put("vkId", vkidd);


        WebServiceWSATMROMODEL webAccountStmt = new WebServiceWSATMROMODEL();
        return webAccountStmt.getATMList(parameters, Constants.METHOD_GET_ATM_LIST, Constants.URL_ATM_RO_MODEL,
                Constants.SOAP_ACTION, Constants.NAMESPACE_WS_ATM_RO_MODEL);
    }

    public static String getAtmRoCashLoadingRecipts(String vkid, String atmId, String stat) {
        HashMap<String, String> parameters = new HashMap<>();

        parameters.put("vkId", vkid);
        parameters.put("atmId", atmId);
        parameters.put("status", stat);


        WebServiceWSATMROMODEL webAccountStmt = new WebServiceWSATMROMODEL();
        return webAccountStmt.getAtmRoCashLoadingRecipts(parameters, Constants.METHOD_GET_ATM_RO_CASH_LOADING, Constants.URL_ATM_RO_MODEL,
                Constants.SOAP_ACTION, Constants.NAMESPACE_WS_ATM_RO_MODEL);
    }

    public static String addAtmRoCashLoading() {
        // mgetData = new ATMgetData();
        HashMap<String, String> parameters = new HashMap<>();

        parameters.put("vkId", mgetData.getVkId());
        parameters.put("atmId", mgetData.getAtmId());
        parameters.put("atmRoCashReceiptId", mgetData.getAtmRoCashReceiptId());
        parameters.put("typeA", mgetData.getTypeA());

        parameters.put("typeB", mgetData.getTypeB());
        parameters.put("typeC", mgetData.getTypeC());
        parameters.put("atmRoCashLoadingId", mgetData.getAtmRoCashLoadingId());
        parameters.put("balance", mgetData.getBalance());

        parameters.put("differenceRemarks", mgetData.getDifferenceRemarks());
        parameters.put("remarks", mgetData.getRemarks());

        parameters.put("cb_o_1000", mgetData.getCb_o_1000());
        parameters.put("cb_o_500", mgetData.getCb_o_500());
        parameters.put("cb_o_100", mgetData.getCb_o_100());
        parameters.put("cb_o_0", mgetData.getCb_o_0());

        parameters.put("cb_p_1000", mgetData.getCb_p_1000());
        parameters.put("cb_p_500", mgetData.getCb_p_500());
        parameters.put("cb_p_100", mgetData.getCb_p_100());
        parameters.put("cb_p_0", mgetData.getCb_p_0());

        parameters.put("dis_1000", mgetData.getDis_1000());
        parameters.put("dis_500", mgetData.getDis_500());
        parameters.put("dis_100", mgetData.getDis_100());
        parameters.put("dis_0", mgetData.getDis_0());

        parameters.put("cb_l_1000", mgetData.getCb_l_1000());
        parameters.put("cb_l_500", mgetData.getCb_l_500());
        parameters.put("cb_l_100", mgetData.getCb_l_100());
        parameters.put("cb_l_0", mgetData.getCb_l_0());

        parameters.put("cb_c_1000", mgetData.getCb_c_1000());
        parameters.put("cb_c_500", mgetData.getCb_c_500());
        parameters.put("cb_c_100", mgetData.getCb_c_100());
        parameters.put("cb_c_0", mgetData.getCb_c_0());

        parameters.put("counter_b_1000", mgetData.getCounter_b_1000());
        parameters.put("counter_b_500", mgetData.getCounter_b_500());
        parameters.put("counter_b_100", mgetData.getCounter_b_100());
        parameters.put("counter_b_0", mgetData.getCounter_b_0());

        parameters.put("counter_a_1000", mgetData.getCounter_a_1000());
        parameters.put("counter_a_500", mgetData.getCounter_a_500());
        parameters.put("counter_a_100", mgetData.getCounter_a_100());
        parameters.put("counter_a_0", mgetData.getCounter_a_0());

        parameters.put("switch_b_1000", mgetData.getSwitch_b_1000());
        parameters.put("switch_b_500", mgetData.getSwitch_b_500());
        parameters.put("switch_b_100", mgetData.getSwitch_b_100());
        parameters.put("switch_b_0", mgetData.getSwitch_b_0());

        parameters.put("switch_a_1000", mgetData.getSwitch_a_1000());
        parameters.put("switch_a_500", mgetData.getSwitch_a_500());
        parameters.put("switch_a_100", mgetData.getSwitch_a_100());
        parameters.put("switch_a_0", mgetData.getSwitch_a_0());

        parameters.put("atmBeforeLoading", mgetData.getAtmBeforeLoading());
        parameters.put("atmAfterLoading", mgetData.getAtmAfterLoading());
        parameters.put("swtichBeforeLoading", mgetData.getSwtichBeforeLoading());
        parameters.put("swtichAfterLoading", mgetData.getSwtichAfterLoading());
        parameters.put("loadingDate", mgetData.getLoadingDate());


        WebServiceWSATMROMODEL webAccountStmt = new WebServiceWSATMROMODEL();
        return webAccountStmt.getAddAtmRoCashLoading(parameters, Constants.METHOD_GET_ADD_ATM_CASH_LOADING, Constants.URL_ATM_RO_MODEL,
                Constants.SOAP_ACTION, Constants.NAMESPACE_WS_ATM_RO_MODEL);
    }

    public static String getDenomination(String date) {
        HashMap<String, String> parameters = new HashMap<>();

        parameters.put("date", date);

        WebServiceWSATMROMODEL webAccountStmt = new WebServiceWSATMROMODEL();
        return webAccountStmt.getDenomination(parameters, Constants.METHOD_GET_DENOMINATION, Constants.URL_ATM_RO_MODEL,
                Constants.SOAP_ACTION, Constants.NAMESPACE_WS_ATM_RO_MODEL);
    }

    public static String getRetailOutletList(String vkidd) {
        HashMap<String, String> parameters = new HashMap<>();

        parameters.put("vkId", vkidd);


        WebServiceWSATMROMODEL webAccountStmt = new WebServiceWSATMROMODEL();
        return webAccountStmt.getRetailOutletList(parameters, Constants.METHOD_GET_RETAIL_OUTLETS_LIST, Constants.URL_ATM_RO_MODEL,
                Constants.SOAP_ACTION, Constants.NAMESPACE_WS_ATM_RO_MODEL);
    }

    public static String createROAcknowlegementa(String vkid, String atmCashRoId, String typeA, String typeB, String typeC, String status, String parsedDate) {
        HashMap<String, String> parameters = new HashMap<>();

        parameters.put("vkId", vkid);
        parameters.put("atmCashRoId", atmCashRoId);
        parameters.put("date", typeA);
        parameters.put("typeA", typeB);
        parameters.put("typeB", typeC);
        parameters.put("typeC", status);
        parameters.put("status", parsedDate);

        WebServiceWSATMROMODEL webAccountStmt = new WebServiceWSATMROMODEL();
        return webAccountStmt.createROAcknowlegement(parameters, Constants.METHOD_GET_CREATE_RO_ACK, Constants.URL_ATM_RO_MODEL,
                Constants.SOAP_ACTION, Constants.NAMESPACE_WS_ATM_RO_MODEL);

    }

    public static String createROAcknowlegement(String vkid, String atmCashRoId, String parsedDate, String typeA, String typeB, String typeC, String status) {
        HashMap<String, String> parameters = new HashMap<>();

        parameters.put("vkId", vkid);
        parameters.put("atmCashRoId", atmCashRoId);
        parameters.put("date", parsedDate);
        parameters.put("typeA", typeB);
        parameters.put("typeB", typeB);
        parameters.put("typeC", typeC);
        parameters.put("status", status);


        WebServiceWSATMROMODEL webAccountStmt = new WebServiceWSATMROMODEL();
        return webAccountStmt.createROAcknowlegement(parameters, Constants.METHOD_GET_CREATE_RO_ACK, Constants.URL_ATM_RO_MODEL,
                Constants.SOAP_ACTION, Constants.NAMESPACE_WS_ATM_RO_MODEL);
    }

    public static String myVakrangeeKendraLocationDetailsNextGen1(String vkidd, String scope, String locationCode, String site_type) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkidd);
        parameters.put("scope", scope);
        parameters.put("locationCode", locationCode);
        parameters.put("nextgenSiteType", site_type);   // Visit, Commencement or WIP


        WebServiceMyVakrangeeKendraImpl webServiceAddLeaveOD = new WebServiceMyVakrangeeKendraImpl();
        return webServiceAddLeaveOD.myVakrangeeKendraLocationDetails(parameters, Constants.METHOD_NAME_MY_VAKRANGEE_LOCATION_DETAILS_NextGen1, Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);
    }

    public static String myVakrangeeKendraLocationDetailsNextGen(String vkidd, String tokenId, String imei, String deviceid, String simserialnumber, String scope, String locationCode, String site_type) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkidd);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", imei);
        parameters.put("deviceId", deviceid);
        parameters.put("simNo", simserialnumber);
        parameters.put("scope", scope);
        parameters.put("locationCode", locationCode);
        parameters.put("nextgenSiteType", site_type);   // Visit, Commencement or WIP


        WebServiceMyVakrangeeKendraImpl webServiceAddLeaveOD = new WebServiceMyVakrangeeKendraImpl();
        return webServiceAddLeaveOD.myVakrangeeKendraLocationDetails(parameters, Constants.METHOD_NAME_MY_VAKRANGEE_LOCATION_DETAILS_NextGen, Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);
    }


    public static String myVakrangeeKendraFranchiseeDetailsNextgen(String vkId, String nextgenFranchiseeApplicationNo, String tokenId, String imei, String deviceid, String simserialnumber) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkId);
        parameters.put("nextgenFranchiseeApplicationNo", nextgenFranchiseeApplicationNo);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", imei);
        parameters.put("deviceId", deviceid);
        parameters.put("simNo", simserialnumber);


        WebServiceMyVakrangeeKendraImpl webAccountStmt = new WebServiceMyVakrangeeKendraImpl();
        return webAccountStmt.myVakrangeeKendraTimingsResponse(parameters, Constants.METHOD_NAME_MY_VAKRANGEE_FRANCHICESS_DETAILS, Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);
    }

    public static String myVakrangeeKendraFranchiseeDetailsNextgen(String vkId) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkId);

        WebServiceMyVakrangeeKendraImpl webAccountStmt = new WebServiceMyVakrangeeKendraImpl();
        return webAccountStmt.myVakrangeeKendraTimingsResponse(parameters, Constants.METHOD_NAME_MY_VAKRANGEE_FRANCHICESS_DETAILS1, Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);
    }

    public static String myVakrangeeKendraFranchiseeDetailsNextgen2(String enquiryId) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("enquiryId", enquiryId);

        WebServiceMyVakrangeeKendraImpl webAccountStmt = new WebServiceMyVakrangeeKendraImpl();
        return webAccountStmt.myVakrangeeKendraTimingsResponse(parameters, Constants.METHOD_NAME_MY_VAKRANGEE_FRANCHICESS_DETAILS2, Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);
    }

    public static String getState(String vkid, String tokenId, String IMEINo,
                                  String deviceId, String simNo) {

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkid);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", IMEINo);
        parameters.put("deviceId", deviceId);
        parameters.put("simNo", simNo);

        return webServiceFranchiseeImpl.getAvailableBalance(parameters, Constants.METHOD_NAME_NEXTGEN_STATE,
                Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);


    }

    public static String getState(String vkid) {

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkid);

        return webServiceFranchiseeImpl.getAvailableBalance(parameters, Constants.METHOD_NAME_NEXTGEN_STATE1,
                Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);

    }


    public static String getDistrict(String vkid, String tokenId, String IMEINo, String deviceId,
                                     String simNo, String stateId) {

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkid);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", IMEINo);
        parameters.put("deviceId", deviceId);
        parameters.put("simNo", simNo);
        parameters.put("state", stateId);

        return webServiceFranchiseeImpl.getAvailableBalance(parameters, Constants.METHOD_NAME_NEXTGEN_DISTRICT,
                Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);


    }

    public static String getDistrict(String vkid, String stateId) {

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkid);
        parameters.put("state", stateId);

        return webServiceFranchiseeImpl.getAvailableBalance(parameters, Constants.METHOD_NAME_NEXTGEN_DISTRICT1,
                Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);

    }

    public static String getVillage(String vkid, String tokenId, String IMEINo, String deviceId,
                                    String simNo, String stateId, String districtId) {

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkid);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", IMEINo);
        parameters.put("deviceId", deviceId);
        parameters.put("simNo", simNo);
        parameters.put("state", stateId);
        parameters.put("district", districtId);

        return webServiceFranchiseeImpl.getAvailableBalance(parameters,
                Constants.METHOD_NAME_NEXTGEN_VILLAGE,
                Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);


    }

    public static String getVillage(String vkid, String stateId, String districtId) {

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkid);
        parameters.put("state", stateId);
        parameters.put("district", districtId);

        return webServiceFranchiseeImpl.getAvailableBalance(parameters,
                Constants.METHOD_NAME_NEXTGEN_VILLAGE1,
                Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);

    }

    public static String getBank(String vkid, String tokenId, String IMEINo,
                                 String deviceId, String simNo) {

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkid);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", IMEINo);
        parameters.put("deviceId", deviceId);
        parameters.put("simNo", simNo);

        return webServiceFranchiseeImpl.getAvailableBalance(parameters, Constants.METHOD_NAME_NEXTGEN_BANK,
                Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);


    }

    public static String getBank(String vkid) {

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkid);

        return webServiceFranchiseeImpl.getAvailableBalance(parameters, Constants.METHOD_NAME_NEXTGEN_BANK1,
                Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);
    }

    public static String getATM(String vkid, String tokenId, String IMEINo,
                                String deviceId, String simNo) {

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkid);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", IMEINo);
        parameters.put("deviceId", deviceId);
        parameters.put("simNo", simNo);

        return webServiceFranchiseeImpl.getAvailableBalance(parameters, Constants.METHOD_NAME_NEXTGEN_ATM,
                Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);


    }

    public static String getATM(String vkid) {

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkid);

        return webServiceFranchiseeImpl.getAvailableBalance(parameters, Constants.METHOD_NAME_NEXTGEN_ATM1,
                Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);

    }

    public static String isRural(String vkid, String tokenId, String IMEINo, String deviceId,
                                 String simNo, String villageId) {

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkid);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", IMEINo);
        parameters.put("deviceId", deviceId);
        parameters.put("simNo", simNo);
        parameters.put("village", villageId);

        return webServiceFranchiseeImpl.getAvailableBalance(parameters,
                Constants.METHOD_NAME_NEXTGEN_IS_RURAL,
                Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);


    }


    public static String isRural(String vkid, String villageId) {

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkid);
        parameters.put("village", villageId);

        return webServiceFranchiseeImpl.getAvailableBalance(parameters,
                Constants.METHOD_NAME_NEXTGEN_IS_RURAL1,
                Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);


    }

    public static String myVakrangeeKendraFranchiseeDetailsNextgenUpdate(String vkid, String tokenId,
                                                                         String IMEINo, String deviceId,
                                                                         String simNo, String type, String data) {

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkid);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", IMEINo);
        parameters.put("deviceId", deviceId);
        parameters.put("simNo", simNo);
        parameters.put("type", type);
        parameters.put("data", data);


        return webServiceFranchiseeImpl.getAvailableBalance(parameters,
                Constants.METHOD_NAME_NEXTGEN_FRANCHISEE_DETAILS_UPDATE,
                Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);


    }

    public static String myVakrangeeKendraFranchiseeDetailsNextgenUpdate(String vkid, String type, String data) {

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkid);
        parameters.put("type", type);
        parameters.put("data", data);


        return webServiceFranchiseeImpl.getAvailableBalance(parameters,
                Constants.METHOD_NAME_NEXTGEN_FRANCHISEE_DETAILS_UPDATE1,
                Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);

    }

    public static String myVakrangeeKendraFranchiseeDetailsNextgenUpdate2(String enquiryId, String type, String data) {

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("enquiryId", enquiryId);
        parameters.put("type", type);
        parameters.put("data", data);


        return webServiceFranchiseeImpl.getAvailableBalance(parameters,
                Constants.METHOD_NAME_NEXTGEN_FRANCHISEE_DETAILS_UPDATE2,
                Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);

    }

    //region WebServiceMyVakrangeeKendra Implementation
    public static String nextgenScheduleDateTimeUpdateResponse(String vkId, String tokenId, String IMEINo, String deviceId, String simNo, String nextgenFranchiseeApplicationId, String activity, String remarks, String activityDateTime) {
        HashMap<String, String> parameters = new HashMap<>();

        parameters.put("vkId", vkId);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", IMEINo);
        parameters.put("deviceId", deviceId);
        parameters.put("simNo", simNo);
        parameters.put("nextgenFranchiseeApplicationId", nextgenFranchiseeApplicationId);
        parameters.put("activity", activity);
        parameters.put("remarks", remarks);
        parameters.put("activityDateTime", activityDateTime);

        WebServiceMyVakrangeeKendraImpl webServiceMyVakrangeeKendra = new WebServiceMyVakrangeeKendraImpl();
        return webServiceMyVakrangeeKendra.nextgenScheduleDateTimeUpdateResponse(parameters, Constants.METHOD_NAME_MY_VKARANGEE_KENDRA_UPDATE_Schedule_DateTime, Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);
    }
    //endregion

    //region Update FCM Id
    public static String updateFCMId(String vkId, String tokenId, String IMEINo, String deviceId, String simNo, String fcmId) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkId);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", IMEINo);
        parameters.put("deviceId", deviceId);
        parameters.put("simNo", simNo);
        parameters.put("fcmId", fcmId);

        WebServiceAccessControlImpl webService = new WebServiceAccessControlImpl();
        return webService.updateFCMId(parameters, Constants.METHOD_NAME_UPDATE_FCM_ID, Constants.URL_VKMS_ACCESS_CONTROL,
                Constants.SOAP_ACTION);
    }
    //endregion

    //region Get Reliance JIO Plan & Recharge Reliance JIO Prepaid
    public static String getRelianceJIOPlans(String vkId, String tokenId, String IMEINo, String deviceId, String simNo, String mobileNumber) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkId);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", IMEINo);
        parameters.put("deviceId", deviceId);
        parameters.put("simNo", simNo);
        parameters.put("mobileNumber", mobileNumber);

        WSRelianceJIOImpl webService = new WSRelianceJIOImpl();
        return webService.getPlans(parameters, Constants.METHOD_NAME_RELIANCE_JIO_GET_PLANS, Constants.URL_RELIANCE_JIO,
                Constants.SOAP_ACTION);
    }

    /**
     * @param vkId
     * @param tokenId
     * @param IMEINo
     * @param deviceId
     * @param simNo
     * @param mobileNumber
     * @param rechargeAmount
     * @param vr             : ValidateResponse By Reliance JIO
     * @param planId
     * @return
     */
    public static String rechargeRelianceJIO(String vkId, String tokenId, String IMEINo, String deviceId, String simNo, String mobileNumber, String rechargeAmount, String vr, String planId) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkId);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", IMEINo);
        parameters.put("deviceId", deviceId);
        parameters.put("simNo", simNo);
        parameters.put("mobileNumber", mobileNumber);
        parameters.put("rechargeAmount", rechargeAmount);
        parameters.put("vr", vr);
        parameters.put("planId", planId);

        WSRelianceJIOImpl webService = new WSRelianceJIOImpl();
        return webService.getPlans(parameters, Constants.METHOD_NAME_RELIANCE_JIO_RECHARGE, Constants.URL_RELIANCE_JIO,
                Constants.SOAP_ACTION);
    }
    //endregion

    //region Support Ticket
    public static String getSupportTicketCategories(String vkId, String tokenId, String IMEINo, String deviceId, String simNo) {
        HashMap<String, String> parameters = new HashMap<>();

        parameters.put("vkId", vkId);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", IMEINo);
        parameters.put("deviceId", deviceId);
        parameters.put("simNo", simNo);

        WSSupportTicketImpl webService = new WSSupportTicketImpl();
        return webService.getCategory(parameters, Constants.METHOD_NAME_SUPPORT_TICKET_GET_CATEGORY, Constants.URL_SUPPORT_TICKET,
                Constants.SOAP_ACTION);
    }

    public static String getSupportTicketSubCategories(String vkId, String tokenId, String IMEINo, String deviceId, String simNo, String categoryId) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkId);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", IMEINo);
        parameters.put("deviceId", deviceId);
        parameters.put("simNo", simNo);
        parameters.put("categoryId", categoryId);

        WSSupportTicketImpl webService = new WSSupportTicketImpl();
        return webService.getSubCategory(parameters, Constants.METHOD_NAME_SUPPORT_TICKET_GET_SUBCATEGORY, Constants.URL_SUPPORT_TICKET,
                Constants.SOAP_ACTION);
    }

    public static String getSupportTicketIssueType(String vkId, String tokenId, String IMEINo, String deviceId, String simNo, String subCategoryId) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkId);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", IMEINo);
        parameters.put("deviceId", deviceId);
        parameters.put("simNo", simNo);
        parameters.put("subCategoryId", subCategoryId);

        WSSupportTicketImpl webService = new WSSupportTicketImpl();
        return webService.getIssueType(parameters, Constants.METHOD_NAME_SUPPORT_TICKET_GET_ISSUE_TYPE, Constants.URL_SUPPORT_TICKET,
                Constants.SOAP_ACTION);
    }
    //endregion

    //region NextGen Site Work Commencement
    public static String getNextGenSiteWorkCommencementDetail(String vkId, String tokenId, String IMEINo, String deviceId, String simNo, String nextgenFranchiseeApplicationNo) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkId);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", IMEINo);
        parameters.put("deviceId", deviceId);
        parameters.put("simNo", simNo);
        parameters.put("nextgenFranchiseeApplicationNo", nextgenFranchiseeApplicationNo);

        WebServiceMyVakrangeeKendraImpl webService = new WebServiceMyVakrangeeKendraImpl();
        return webService.getNextGenSiteWorkCommencementDetail(parameters, Constants.METHOD_NAME_GET_NEXTGEN_WORK_COMMENCEMENT, Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);
    }

    public static String getNextGenSiteWorkCommencementDetail(String vkId, String nextgenFranchiseeApplicationNo) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkId);
        parameters.put("nextgenFranchiseeApplicationNo", nextgenFranchiseeApplicationNo);

        WebServiceMyVakrangeeKendraImpl webService = new WebServiceMyVakrangeeKendraImpl();
        return webService.getNextGenSiteWorkCommencementDetail(parameters, Constants.METHOD_NAME_GET_NEXTGEN_WORK_COMMENCEMENT1, Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);
    }

    public static String nextgenSiteWorkCommencementUpdate(String vkId, String tokenId, String IMEINo, String deviceId, String simNo, String data) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkId);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", IMEINo);
        parameters.put("deviceId", deviceId);
        parameters.put("simNo", simNo);
        parameters.put("data", data);

        WebServiceMyVakrangeeKendraImpl webService = new WebServiceMyVakrangeeKendraImpl();
        return webService.nextgenSiteWorkCommencementUpdate(parameters, Constants.METHOD_NAME_UPDATE_NEXTGEN_WORK_COMMENCEMENT, Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);
    }

    public static String nextgenSiteWorkCommencementUpdate(String vkId, String data) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkId);
        parameters.put("data", data);

        WebServiceMyVakrangeeKendraImpl webService = new WebServiceMyVakrangeeKendraImpl();
        return webService.nextgenSiteWorkCommencementUpdate(parameters, Constants.METHOD_NAME_UPDATE_NEXTGEN_WORK_COMMENCEMENT1, Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);
    }

    //endregion

    //region NextGen Site Work In Progress

    public static String getAllNextGenSiteWorkInProgressDetail(String vkId, String tokenId, String IMEINo, String deviceId, String simNo, String nextgenFranchiseeApplicationNo) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkId);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", IMEINo);
        parameters.put("deviceId", deviceId);
        parameters.put("simNo", simNo);
        parameters.put("nextgenFranchiseeApplicationNo", nextgenFranchiseeApplicationNo);

        WebServiceMyVakrangeeKendraImpl webService = new WebServiceMyVakrangeeKendraImpl();
        return webService.getAllNextGenSiteWorkInProgressDetail(parameters, Constants.METHOD_NAME_GET_NEXTGEN_ALL_WORK_IN_PROGRESS, Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);
    }

    public static String getAllNextGenSiteWorkInProgressDetail(String vkId, String nextgenFranchiseeApplicationNo) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkId);
        parameters.put("nextgenFranchiseeApplicationNo", nextgenFranchiseeApplicationNo);

        WebServiceMyVakrangeeKendraImpl webService = new WebServiceMyVakrangeeKendraImpl();
        return webService.getAllNextGenSiteWorkInProgressDetail(parameters, Constants.METHOD_NAME_GET_NEXTGEN_ALL_WORK_IN_PROGRESS1, Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);
    }

    public static String getNextGenSiteWorkInProgressDetail(String vkId, String tokenId, String IMEINo, String deviceId, String simNo, String nextgenFranchiseeApplicationNo, String nextgenSiteWorkInProgressId) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkId);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", IMEINo);
        parameters.put("deviceId", deviceId);
        parameters.put("simNo", simNo);
        parameters.put("nextgenFranchiseeApplicationNo", nextgenFranchiseeApplicationNo);
        parameters.put("nextgenSiteWorkInProgressId", nextgenSiteWorkInProgressId);

        WebServiceMyVakrangeeKendraImpl webService = new WebServiceMyVakrangeeKendraImpl();
        return webService.getNextGenSiteWorkInProgressDetail(parameters, Constants.METHOD_NAME_GET_NEXTGEN_WORK_IN_PROGRESS, Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);
    }

    public static String getNextGenSiteWorkInProgressDetail(String vkId, String nextgenFranchiseeApplicationNo, String nextgenSiteWorkInProgressId) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkId);
        parameters.put("nextgenFranchiseeApplicationNo", nextgenFranchiseeApplicationNo);
        parameters.put("nextgenSiteWorkInProgressId", nextgenSiteWorkInProgressId);

        WebServiceMyVakrangeeKendraImpl webService = new WebServiceMyVakrangeeKendraImpl();
        return webService.getNextGenSiteWorkInProgressDetail(parameters, Constants.METHOD_NAME_GET_NEXTGEN_WORK_IN_PROGRESS1, Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);
    }

    public static String nextgenSiteWorkInProgressUpdate(String vkId, String tokenId, String IMEINo, String deviceId, String simNo, String data) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkId);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", IMEINo);
        parameters.put("deviceId", deviceId);
        parameters.put("simNo", simNo);
        parameters.put("data", data);

        WebServiceMyVakrangeeKendraImpl webService = new WebServiceMyVakrangeeKendraImpl();
        return webService.nextgenSiteWorkInProgressUpdate(parameters, Constants.METHOD_NAME_UPDATE_NEXTGEN_WORK_IN_PROGRESS, Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);
    }

    public static String nextgenSiteWorkInProgressUpdate(String vkId, String data) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkId);
        parameters.put("data", data);

        WebServiceMyVakrangeeKendraImpl webService = new WebServiceMyVakrangeeKendraImpl();
        return webService.nextgenSiteWorkInProgressUpdate(parameters, Constants.METHOD_NAME_UPDATE_NEXTGEN_WORK_IN_PROGRESS1, Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);
    }
    //endregion

    //region NextGen Site Work Completion Intimation
    public static String getNextGenSiteWorkCompletionIntimationDetail(String vkId, String nextgenFranchiseeApplicationNo) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkId);
        parameters.put("nextgenFranchiseeApplicationNo", nextgenFranchiseeApplicationNo);

        WebServiceMyVakrangeeKendraImpl webService = new WebServiceMyVakrangeeKendraImpl();
        return webService.getNextGenSiteWorkCompletionDetail1(parameters, Constants.METHOD_NAME_GET_NEXTGEN_WORK_COMPLETION1, Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);
    }

    public static String getWorkCompletionIntimationDetail(String vkId, String tokenId, String IMEINo, String deviceId, String simNo, String nextgenFranchiseeApplicationNo) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkId);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", IMEINo);
        parameters.put("deviceId", deviceId);
        parameters.put("simNo", simNo);
        parameters.put("nextgenFranchiseeApplicationNo", nextgenFranchiseeApplicationNo);

        WebServiceMyVakrangeeKendraImpl webService = new WebServiceMyVakrangeeKendraImpl();
        /*return webService.getWorkCompletionIntimationDetail(parameters, Constants.METHOD_NAME_GET_WORK_COMPLETION_INTIMATION_DETAIL, Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);*/
        return webService.getAllNextGenSiteWorkInProgressDetail(parameters, Constants.METHOD_NAME_GET_NEXTGEN_ALL_WORK_IN_PROGRESS, Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);
    }

    public static String getWorkCompletionCheckList(String vkId, String tokenId, String IMEINo, String deviceId, String simNo) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkId);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", IMEINo);
        parameters.put("deviceId", deviceId);
        parameters.put("simNo", simNo);

        WebServiceMyVakrangeeKendraImpl webService = new WebServiceMyVakrangeeKendraImpl();
        return webService.getWorkCompletionCheckList(parameters, Constants.METHOD_NAME_GET_WORK_COMPLETION_INTIMATION_CHECKLIST, Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);
    }

    //endregion

    //region NextGen Site Readiness and Verification
    public static String getSiteReadinessAndVerificationDetail(String vkId, String tokenId, String IMEINo, String deviceId, String simNo, String nextgenFranchiseeApplicationNo) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkId);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", IMEINo);
        parameters.put("deviceId", deviceId);
        parameters.put("simNo", simNo);
        parameters.put("nextgenFranchiseeApplicationNo", nextgenFranchiseeApplicationNo);

        WebServiceMyVakrangeeKendraImpl webService = new WebServiceMyVakrangeeKendraImpl();
        return webService.getSiteReadinessAndVerificationDetail(parameters, Constants.METHOD_NAME_SITE_READINEE_AND_VERIFICATION_DETAIL, Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);

    }

    public static String getSiteReadinessAndVerificationDetail(String vkId, String nextgenFranchiseeApplicationNo) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkId);
        parameters.put("nextgenFranchiseeApplicationNo", nextgenFranchiseeApplicationNo);

        WebServiceMyVakrangeeKendraImpl webService = new WebServiceMyVakrangeeKendraImpl();
        return webService.getSiteReadinessAndVerificationDetail(parameters, Constants.METHOD_NAME_SITE_READINEE_AND_VERIFICATION_DETAIL1, Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);
    }

    public static String getReadinessAndVerificationCheckList(String vkId, String tokenId, String IMEINo, String deviceId, String simNo, String nextgenFranchiseeApplicationNo) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkId);
        parameters.put("tokenId", tokenId);
        parameters.put("IMEINo", IMEINo);
        parameters.put("deviceId", deviceId);
        parameters.put("simNo", simNo);
        parameters.put("nextgenFranchiseeApplicationNo", nextgenFranchiseeApplicationNo);

        WebServiceMyVakrangeeKendraImpl webService = new WebServiceMyVakrangeeKendraImpl();
        return webService.getReadinessAndVerificationCheckList(parameters, Constants.METHOD_NAME_GET_READINEE_AND_VERIFICATION_CHECKLIST, Constants.URL_MY_VAKRANGEE_KENDRA, Constants.SOAP_ACTION);
    }

    public static String nextgenSiteReadinessAndVerificationUpdate1(String vkId, String data, String type) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkId);
        parameters.put("data", data);
        parameters.put("type", type);

        WebServiceMyVakrangeeKendraImpl webService = new WebServiceMyVakrangeeKendraImpl();
        return webService.nextgenSiteReadinessAndVerificationUpdate1(parameters, Constants.METHOD_NAME_NEXT_GEN_SITE_READINESS_VERIFICATION_UPDATE1, Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);
    }

    public static String getElemenAttributetDetail(String vkId, String elementType, String elementDetailId) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkId);
        parameters.put("elementType ", elementType);
        parameters.put("elementDetailId ", elementDetailId);

        WebServiceMyVakrangeeKendraImpl webService = new WebServiceMyVakrangeeKendraImpl();
        return webService.getElemenAttributetDetail(parameters, Constants.METHOD_NAME_NEXT_GEN_SITE_READINESS_GET_ELEMENT_ATTRIBUTE_DETAIL, Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);
    }

    public static String validateLatLongWithAddress(String vkId, String nextgenFranchiseeApplicationNo, String latitude, String longitude) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkId);
        parameters.put("nextgenFranchiseeApplicationNo", nextgenFranchiseeApplicationNo);
        parameters.put("latitude", latitude);
        parameters.put("longitude", longitude);

        WebServiceMyVakrangeeKendraImpl webService = new WebServiceMyVakrangeeKendraImpl();
        return webService.validateLatLongWithAddress(parameters, Constants.METHOD_NAME_SITE_READINEE_VALIDATE_LAT_LONG_WITH_ADDRESS, Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);
    }

    //endregion

    public static String checkVersionWithType(String versioncode) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("version", versioncode);
        parameters.put("platform_type", Constants.TYPE_VAKRANGEE_FRANCHISEE);
        parameters.put("app_type", Constants.APP_TYPE);

        WebServiceAccessControlImpl webService = new WebServiceAccessControlImpl();
        return webService.checkversion(parameters, Constants.METHOD_NAME_CHECK_VERSION, Constants.URL_VKMS_ACCESS_CONTROL,
                Constants.SOAP_ACTION);
    }

    //region Verify Site Review OTP
    public static String SiteotpVerfiy(String vkid) {

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkid);

        WebServiceAccessControlImpl webService = new WebServiceAccessControlImpl();
        return webService.verifyOtp(parameters, Constants.METHOD_NAME_SITE_VISIT_VERIFY_OTP, Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);
    }

    //endregion

    //region Verify Site Review OTP via EnquiryId
    public static String getOTPSiteReviewViaEnquiryId(String enquiryId) {

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("enquiryId", enquiryId);

        WebServiceAccessControlImpl webService = new WebServiceAccessControlImpl();
        return webService.verifyOtp(parameters, Constants.METHOD_NAME_SITE_VISIT_VERIFY_OTP_VIA_ENQUIRY_ID, Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);
    }

    //endregion

    //region Authenticate User
    public static String authenticateUser(String vkId, String mPin, String IMEINo, String deviceId, String simNo) {
        HashMap<String, String> parameters = new HashMap<>();

        parameters.put("vkId", vkId);
        parameters.put("mPin", mPin);
        parameters.put("IMEINo", IMEINo);
        parameters.put("deviceId", deviceId);
        parameters.put("simNo", simNo);

        WebServiceAccessControlImpl webServiceAccessControl = new WebServiceAccessControlImpl();
        return webServiceAccessControl.authenticateUser(parameters, Constants.METHOD_NAME_AUTHENTICATE_USER, Constants.URL_VKMS_ACCESS_CONTROL,
                Constants.SOAP_ACTION);
    }

    //endregion

    //region getNextgenAddressProofDetail
    public static String getNextgenAddressProofDetail(String vkid) {

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkid);

        return webServiceFranchiseeImpl.getAvailableBalance(parameters, Constants.METHOD_NAME_NEXTGEN_ADDRESS_PROOF_DETAILS,
                Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);

    }
    //endregion

    //region MyhelpLine  -VL- VA
    public static String getMyHelpLine1(String vkidd) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkidd);
        WebServiceFranchiseeImpl webAccountStmt = new WebServiceFranchiseeImpl();
        return webAccountStmt.getMyHelpLine(parameters, Constants.METHOD_NAME_MY_HELPLINE_1, Constants.URL_FRACHISEE,
                Constants.SOAP_ACTION);
    }

    //region updateFranchiseeGSTDetail
    public static String updateFranchiseeGSTDetail(HashMap<String, String> parameters) {

        WebServiceMyVakrangeeKendraImpl webService = new WebServiceMyVakrangeeKendraImpl();
        return webService.getSiteReadinessAndVerificationDetail(parameters, Constants.METHOD_NAME_UPDATE_GST_DETAILS, Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);

    }
    //endregion

    //region getFranchiseeGSTDetail
    public static String getFranchiseeGSTDetail(String vkId) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkId);
        WebServiceMyVakrangeeKendraImpl webService = new WebServiceMyVakrangeeKendraImpl();
        return webService.getSiteReadinessAndVerificationDetail(parameters, Constants.METHOD_NAME_GET_GST_DETAILS, Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);

    }
    //endregion

    //region Update User LatLng
    public static String updateUserLatLong(String vkId, String latitude, String longitude) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkId);
        parameters.put("latitude", latitude);
        parameters.put("longitude", longitude);

        WebServiceMyVakrangeeKendraImpl webService = new WebServiceMyVakrangeeKendraImpl();
        return webService.updateUserLatLong(parameters, Constants.METHOD_NAME_SITE_READINEE_UPDATE_USER_LAT_LONG, Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);
    }
    //endregion

    //region MyhelpLine  -VL- VA
    public static String getTrainingVideo(String vkidd) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("vkId", vkidd);
        WebServiceFranchiseeImpl webAccountStmt = new WebServiceFranchiseeImpl();
        return webAccountStmt.getMyHelpLine(parameters, Constants.METHOD_NAME_MY_TRAINING_VIDEO, Constants.URL_MY_VAKRANGEE_KENDRA,
                Constants.SOAP_ACTION);
    }
    //endregion
}
