package in.vakrangee.core.utils;

import in.vakrangee.core.application.VakrangeeKendraApplication;
import in.vakrangee.core.model.ATMgetData;

/**
 * Created by Nileshd on 5/18/2016.
 */
public class Constants {

    public static String CONNECTION_TYPE;
    public static String PRODUCT_CODE;
    public static String HOST;
    public static boolean ENABLE_FRANCHISEE_LOGIN = true;
    public static boolean ENABLE_ADHOC_MODE = true;
    public static boolean ENABLE_FRANCHISEE_MODE = true;
    public static String APP_TYPE = "Android";
    public static String TYPE_VAKRANGEE_KENDRA = "1";
    public static String TYPE_VAKRANGEE_FRANCHISEE = "2";

    static {
        //CONNECTION_TYPE =  Helper.getConfigValue(MainActivity.getContext(), "CONNECTION_TYPE");
        CONNECTION_TYPE = Helper.getConfigValue(VakrangeeKendraApplication.getContext(), "CONNECTION_TYPE");
        PRODUCT_CODE = Helper.getConfigValue(VakrangeeKendraApplication.getContext(), "PRODUCT_CODE");
        if (CONNECTION_TYPE.equals("LIVE")) {
            HOST = "vkms";
        } else if (CONNECTION_TYPE.equals("UAT")) {
            HOST = "vkmssit";
        } else if (CONNECTION_TYPE.equals("DEV")) {
            HOST = "dev";
        }
        System.out.println("**************");
        System.out.println("CONNECTION_TYPE: " + CONNECTION_TYPE);
        System.out.println("PRODUCT_CODE: " + PRODUCT_CODE);
        System.out.println("**************");
    }

    public static String SOAP_ACTION = "";

    public static String TAG = "Response";
    public static String METHOD_NAME = "doCyberPlatRecharge";
    public static String NAMESPACE = "http://webservices.vkms.vakrangee.in/";

    public static String NAMESPACE_WS_ATTENDEANCE = "http://webservices.wsattendance.vkms.vakrangee.in/";
    public static String NAMESPACE_WS_ATM_RO_MODEL = "http://webservices.wsatm.vkms.vakrangee.in/";
    public static String NAMESPACE_WS_SUPPORT_TICKET = "http://webservices.wssupportticket.vkms.vakrangee.in/";
    public static String NAMESPACE_WS_RELIANCE_JIO = "http://webservices.wsreliancejio.vkms.vakrangee.in/";

    public static String URL_CYBER_PLAT = String.format("https://%s.vakrangee.in/WSCyberPlat/CyberPlat?WSDL", HOST);
    public static String URL_VKMS_ACCESS_CONTROL = String.format("https://%s.vakrangee.in/VKMSWebService/AccessControl?WSDL", HOST);
    public static String URL_FRACHISEE = String.format("https://%s.vakrangee.in/VKMSWebService/Franchisee?WSDL", HOST);
    public static String URL_MAHAVITRAN = String.format("https://%s.vakrangee.in/WSMahavitran/Mahavitran?WSDL", HOST);
    public static String URL_MY_VAKRANGEE_KENDRA = String.format("https://%s.vakrangee.in/WSMyVakrangeeKendra/MyVakrangeeKendra?WSDL", HOST);
    public static String URL_RELIANCE_JIO = String.format("https://%s.vakrangee.in/WSRelianceJio/RelianceJio?WSDL", HOST);
    public static String URL_SUPPORT_TICKET = String.format("https://%s.vakrangee.in/WSSupportTicket/SupportTicket?WSDL", HOST);

    // public static String URL_ADD_LEAVE_OD = String.format("https://%s.vakrangee.in/WSLeaveOD/LeaveOD?WSDL", HOST);
    public static String URL_ADD_LEAVE_OD = String.format("https://%s.vakrangee.in/WSAttendance/LeaveOD?WSDL", HOST);
    public static String URL_WS_ATTENDANCE = String.format("https://%s.vakrangee.in/WSAttendance/Attendance?WSDL", HOST);
    public static String URL_ATM_RO_MODEL = String.format("https://%s.vakrangee.in/WSATM/ATMROModel?WSDL", HOST);
    public static String DownloadPDFfileURL = String.format("https://%s.vakrangee.in/DownloadFranchiseeApplicationForm?image_id=", HOST);
    public static String DownloadImageUrl = String.format("https://%s.vakrangee.in/ImageDisplayDatabase?type=A&image_id=", HOST);

    public static String METHOD_NAME_REGISTER_FREANCHISEE = "registerFranchisee";
    public static String METHOD_NAME_VERFITY_OTP = "verifyOtp";
    public static String METHOD_NAME_RESEND_OTP = "resendOtp";
    public static String METHOD_NAME_CHECK_VERSION = "checkVersion";
    public static String METHOD_NAME_RESET_MPIN = "resetMPin";
    public static String METHOD_NAME_UPDATE_FCM_ID = "updateFCMId";

    public static String METHOD_NAME_GET_AVAILABLE_BALANCE = "getAvailableBalance";
    public static String METHOD_NAME_UPDATE_MPIN = "updateMPin";
    public static String METHOD_NAME_AUTHENTICATE_FRACHISEE = "authenticateFranchiseeWithToken";

    public static String METHOD_NAME_LOGOUT_FRACHISEE = "logOutFranchisee";
    public static String METHOD_NAME_AUTHENTICATE_FRACHISEE_WITHOUT_TOKEN = "authenticateFranchisee";

    public static String METHOD_NAME_GET_BILL_DETAILS = "getBillDetails";
    public static String METHOD_NAME_PAYBILL = "payBill";
    public static String METHOD_NAME_ACCOUNT_STATEMENT = "getAccountStatement";
    public static String METHOD_NAME_RECHARGE_HISTROY = "getRechargeHistroyReport";
    public static String METHOD_NAME_ACCOUNT_STATEMENT_10 = "getAccountStatement10";
    public static String METHOD_NAME_MY_HELPLINE = "getMyHelpLine";
    public static String METHOD_NAME_My_TRANCATION = "getMyTransactions";
    public static String METHOD_NAME_INSERT_FF_LOCATION = "insertFFLocationLog";
    public static String METHOD_NAME_AUTHENTICATE_FRACHISEE_ON_UPGRADE = "authenticateFranchiseeOnUpgrade";
    public static String METHOD_NAME_MY_HELPLINE_1 = "getMyHelpLine1";
    public static String METHOD_NAME_MY_TRAINING_VIDEO = "getTrainingVideo";
    public static String METHOD_NAME_MY_VKARANGEE_KENDRA = "myVakrangeeKendra";
    public static String METHOD_NAME_MY_VKARANGEE_KENDRA_TIMING_RESPONSE = "myVakrangeeKendraTimings";
    public static String METHOD_NAME_MY_VKARANGEE_KENDRA_GEO_CORDINATES = "myVakrangeeKendraGeoCordinates";
    public static String METHOD_NAME_ADD_LEAVE_OD = "addLeaveOD";
    public static String METHOD_NAME_GET_MY_LEAVE_OD_LIST = "getMyLeaveODList";
    public static String METHOD_NAME_UPDATE_STATUS_LEAVE_OD = "updateStatusLeaveOD";
    public static String METHOD_NAME_MY_VKARANGEE_KENDRA_UPDATE_Schedule_DateTime = "nextgenScheduleDateTimeUpdate";

    public static String METHOD_NAME_MY_VAKRANGEE_LOCATION_DETAILS = "myVakrangeeKendraLocationDetails";

    public static String METHOD_NAME_MY_VKARANGEE_KENDRA_PARTICULAR = "myVakrangeeKendraParticular";
    public static String METHOD_NAME_MY_VKARANGEE_KENDRA_TIMING_RESPONSE_PARTICULAR = "myVakrangeeKendraTimingsParticular";
    public static String METHOD_NAME_MY_VKARANGEE_KENDRA_GEO_CORDINATES_PARTICULAR = "myVakrangeeKendraGeoCordinatesParticular";

    public static String METHOD_NAME_WS_ATTEDANCE = "getNearByVakrangeeKendra";

    public static String METHOD_GET_ATM_LIST = "getATMList";
    public static String METHOD_GET_ATM_RO_CASH_LOADING = "getAtmRoCashLoadingRecipts";
    public static String METHOD_GET_ADD_ATM_CASH_LOADING = "addAtmRoCashLoading";
    public static String METHOD_GET_DENOMINATION = "getDenomination";
    public static String METHOD_GET_RETAIL_OUTLETS_LIST = "getRetailOutletList";
    public static String METHOD_GET_CREATE_RO_ACK = "createROAcknowlegement";
    public static String METHOD_NAME_MY_VAKRANGEE_LOCATION_DETAILS_NextGen = "myVakrangeeKendraLocationDetailsNextgen";
    public static String METHOD_NAME_MY_VAKRANGEE_LOCATION_DETAILS_NextGen1 = "myVakrangeeKendraLocationDetailsNextgen1";

    public static String METHOD_NAME_MY_VAKRANGEE_FRANCHICESS_DETAILS = "myVakrangeeKendraFranchiseeDetailsNextgen";
    public static String METHOD_NAME_NEXTGEN_STATE = "nextgenState";
    public static String METHOD_NAME_NEXTGEN_DISTRICT = "nextgenDistrict";
    public static String METHOD_NAME_NEXTGEN_VILLAGE = "nextgenVillage";
    public static String METHOD_NAME_NEXTGEN_BANK = "nextgenBank";
    public static String METHOD_NAME_NEXTGEN_ATM = "nextgenATMBank";
    public static String METHOD_NAME_NEXTGEN_IS_RURAL = "nextgenIsRural";
    public static String METHOD_NAME_NEXTGEN_FRANCHISEE_DETAILS_UPDATE = "myVakrangeeKendraFranchiseeDetailsNextgenUpdate";

    //region Adhoc Methods
    public static String METHOD_NAME_MY_VAKRANGEE_FRANCHICESS_DETAILS1 = "myVakrangeeKendraFranchiseeDetailsNextgen1";
    public static String METHOD_NAME_MY_VAKRANGEE_FRANCHICESS_DETAILS2 = "myVakrangeeKendraFranchiseeDetailsNextgen2";
    public static String METHOD_NAME_NEXTGEN_STATE1 = "nextgenState1";
    public static String METHOD_NAME_NEXTGEN_DISTRICT1 = "nextgenDistrict1";
    public static String METHOD_NAME_NEXTGEN_VILLAGE1 = "nextgenVillage1";
    public static String METHOD_NAME_NEXTGEN_BANK1 = "nextgenBank1";
    public static String METHOD_NAME_NEXTGEN_ATM1 = "nextgenATMBank1";
    public static String METHOD_NAME_NEXTGEN_IS_RURAL1 = "nextgenIsRural1";
    public static String METHOD_NAME_NEXTGEN_FRANCHISEE_DETAILS_UPDATE1 = "myVakrangeeKendraFranchiseeDetailsNextgenUpdate1";
    public static String METHOD_NAME_NEXTGEN_FRANCHISEE_DETAILS_UPDATE2 = "myVakrangeeKendraFranchiseeDetailsNextgenUpdate2";
    public static String METHOD_NAME_SITE_VISIT_VERIFY_OTP = "getOTPSiteReview";
    public static String METHOD_NAME_SITE_VISIT_VERIFY_OTP_VIA_ENQUIRY_ID = "getOTPSiteReviewViaEnquiryId";
    public static String METHOD_NAME_AUTHENTICATE_USER = "authenticateUser";
    public static String METHOD_NAME_NEXTGEN_ADDRESS_PROOF_DETAILS = "getNextgenAddressProofDetail";
    //endregion

    public static String METHOD_NAME_RELIANCE_JIO_GET_PLANS = "getPlans";
    public static String METHOD_NAME_RELIANCE_JIO_RECHARGE = "rechargeBalance";

    public static String METHOD_NAME_SUPPORT_TICKET_GET_CATEGORY = "getCategoryDetail";
    public static String METHOD_NAME_SUPPORT_TICKET_GET_SUBCATEGORY = "getSubCategoryDetail";
    public static String METHOD_NAME_SUPPORT_TICKET_GET_ISSUE_TYPE = "getIssueTypeDetail";

    //region Work Commencement
    public static String METHOD_NAME_GET_NEXTGEN_WORK_COMMENCEMENT = "getNextGenSiteWorkCommencementDetail";
    public static String METHOD_NAME_UPDATE_NEXTGEN_WORK_COMMENCEMENT = "nextgenSiteWorkCommencementUpdate";

    public static String METHOD_NAME_GET_NEXTGEN_WORK_COMMENCEMENT1 = "getNextGenSiteWorkCommencementDetail1";
    public static String METHOD_NAME_UPDATE_NEXTGEN_WORK_COMMENCEMENT1 = "nextgenSiteWorkCommencementUpdate1";
    //endregion

    //region Work In Progress
    public static String METHOD_NAME_GET_NEXTGEN_ALL_WORK_IN_PROGRESS = "getAllNextGenSiteWorkInProgressDetail";
    public static String METHOD_NAME_GET_NEXTGEN_WORK_IN_PROGRESS = "getNextGenSiteWorkInProgressDetail";
    public static String METHOD_NAME_UPDATE_NEXTGEN_WORK_IN_PROGRESS = "nextgenSiteWorkInProgressUpdate";

    public static String METHOD_NAME_GET_NEXTGEN_ALL_WORK_IN_PROGRESS1 = "getAllNextGenSiteWorkInProgressDetail1";
    public static String METHOD_NAME_GET_NEXTGEN_WORK_IN_PROGRESS1 = "getNextGenSiteWorkInProgressDetail1";
    public static String METHOD_NAME_UPDATE_NEXTGEN_WORK_IN_PROGRESS1 = "nextgenSiteWorkInProgressUpdate1";
    //endregion

    //region Work Completion Intimation
    public static String METHOD_NAME_GET_NEXTGEN_WORK_COMPLETION1 = "getWorkCompletionIntimationDetail1";
    public static String METHOD_NAME_GET_WORK_COMPLETION_INTIMATION_DETAIL = "getWorkCompletionIntimationDetail";
    public static String METHOD_NAME_GET_WORK_COMPLETION_INTIMATION_CHECKLIST = "getWorkCompletionCheckList";
    //endregion

    //region Site Readiness and Verification
    public static String METHOD_NAME_SITE_READINEE_AND_VERIFICATION_DETAIL1 = "getSiteReadinessAndVerificationDetail1";
    public static String METHOD_NAME_SITE_READINEE_AND_VERIFICATION_DETAIL = "getSiteReadinessAndVerificationDetail";
    public static String METHOD_NAME_GET_READINEE_AND_VERIFICATION_CHECKLIST = "getSiteReadinessAndVerificationCheckList";
    public static String METHOD_NAME_NEXT_GEN_SITE_READINESS_VERIFICATION_UPDATE1 = "nextgenSiteReadinessAndVerificationUpdate1";
    public static String METHOD_NAME_NEXT_GEN_SITE_READINESS_GET_ELEMENT_ATTRIBUTE_DETAIL = "getElemenAttributetDetail";
    public static String METHOD_NAME_SITE_READINEE_VALIDATE_LAT_LONG_WITH_ADDRESS = "validateLatLongWithAddress";
    public static String METHOD_NAME_SITE_READINEE_UPDATE_USER_LAT_LONG = "updateUserLatLong";
    public static String SITE_READINESS_ATTRIBUTE_DESIGN_TYPE = "1";
    public static String SITE_READINESS_ATTRIBUTE_BRANDING_TYPE = "2";
    public static String SITE_READINESS_BRANDING_TYPE = "5";
    public static String SITE_READINESS_INTERIOR_TYPE = "4";
    public static String SITE_READINESS_ADDRESS_TYPE = "6";
    public static String SITE_READINESS_PROFILE_TYPE = "1";
    public static String METHOD_NAME_UPDATE_GST_DETAILS = "updateFranchiseeGSTDetail";
    public static String METHOD_NAME_GET_GST_DETAILS = "getFranchiseeGSTDetail";
    //endregion

    //region GWR
    public static String URL_BASE_WS_GWR_HOST = "http://203.187.226.37";
    public static String URL_BASE_WS_GWR_APP;
    public static String URL_BASE_WS_GWR_SIGNAGE_APP;

    static {
        if (CONNECTION_TYPE.equals("LIVE")) {
            URL_BASE_WS_GWR_APP = String.format("%s/WSGWR/GWR/", URL_BASE_WS_GWR_HOST);
            URL_BASE_WS_GWR_SIGNAGE_APP = String.format("%s/WSGWR/Signage/", URL_BASE_WS_GWR_HOST);

        } else {
            URL_BASE_WS_GWR_APP = String.format("https://%s.vakrangee.in/WSGWR/GWR/", HOST);
            URL_BASE_WS_GWR_SIGNAGE_APP = String.format("https://%s.vakrangee.in/WSGWR/Signage/", HOST);
        }
    }

/*
    public static String URL_BASE_WS_GWR_APP_OLD = String.format("https://%s.vakrangee.in/WSGWR/GWR/", HOST);
    public static String URL_BASE_WS_GWR_SIGNAGE_APP_OLD = String.format("https://%s.vakrangee.in/WSGWR/Signage/", HOST);
    public static String URL_BASE_WS_GWR_APP = String.format("%s/WSGWR/GWR/", URL_BASE_WS_GWR_HOST);
    public static String URL_BASE_WS_GWR_SIGNAGE_APP = String.format("%s/WSGWR/Signage/", URL_BASE_WS_GWR_HOST);

*/

    public static String METHOD_NAME_GET_GWR_ACTIVITY_DETAILS = "getGWRActivityDetails/{VKID}/{type}";
    public static String METHOD_NAME_GET_WITNESS_AND_CAMERA_DETAILS = "getWitnessAndCameraManDetails/{VKID}";
    public static String METHOD_NAME_SAVE_WITNESS_AND_CAMERA = "saveWitnessAndCameraManDetails/{vkId}";
    public static String METHOD_NAME_SAVE_GWR_ACTIVITY_DETAILS = "saveGWRActivityDetails/{VKID}/{type}";
    public static String METHOD_NAME_GET_GWR_ATTENDANCE_DETAILS = "getWitnessAndCameraManAttendanceDetails/{VKID}";
    public static String METHOD_NAME_GET_OCCUPATION_LIST = "getWitnessOccupations";
    public static String METHOD_NAME_CHECK_GWR_DETAILS = "checkGuinnessWorldRecordDetails/{VKID}";
    public static String METHOD_NAME_SAVE_GWR_ATTENDANCE_DETAILS = "saveWitnessAndCameraManAttendance/{VKID}";
    public static String METHOD_NAME_UPDATE_GWR_DASHBOARD_DETAILS = "updateGuinnessWorldRecordDetails/{VKID}";
    public static String METHOD_NAME_SAVE_WITNESS_STATEMENT = "saveWitnessStatementDetails/{VKID}";
    public static String METHOD_NAME_GET_WITNESS_STATEMENT = "getWitnessStatementDetails/{VKID}";
    public static String METHOD_NAME_GET_SERVER_DATETIME = "getServerDateTime/{MAC_ADDRESS}";
    public static String METHOD_NAME_CHECK_GUINNESS_WORLD_RECORD_DETAILS = "checkGuinnessWorldRecordDetails/{MAC_ADDRESS}";
    public static String METHOD_NAME_GWR_EVENT_PHOTO_DETAILS = "getEventPhotoDetails/{VKID}";
    public static String METHOD_NAME_SAVE_EVENT_PHOTO_DETAILS = "saveEventPhotoDetails/{VKID}";
    public static String METHOD_NAME_SAVE_INAUGURATION_PHOTO = "saveInaugurationPhoto/{VKID}";
    public static String METHOD_NAME_SAVE_EVIDENCE_RAW_FILES = "saveEvidenceRawFile/{vkId}";

    //endregion

    //region kendra final photo
    public static String METHOD_NAME_GET_KENDRA_FINAL_PHOTO = "getKendraFinalPhotoDetails/{VKID}";
    public static String METHOD_NAME_SAVE_KENDRA_FINAL_PHOTO = "saveKendraFinalPhotoDetails/{VKID}";
    //endregion
    //region NextGen Franchisee Application Form
    public static String URL_CONSENT_BASE_URL = String.format("https://%s.vakrangee.in/", HOST);
    public static String URL_BASE_WS_FRANCHISEE_APP = String.format("https://%s.vakrangee.in/WSFranchiseeApp/FLM/", HOST);
    public static String URL_BASE_WS_FRANCHISEE_APP_WITHOUT_FLM = String.format("https://%s.vakrangee.in/WSFranchiseeApp/", HOST);
    public static String METHOD_NAME_GET_FRANCHISEE_APPLICATION_DETAILS = "getFranchiseeApplicationDetail/{VKID}";
    public static String METHOD_NAME_GET_FRANCHISEE_APPLICATION_DETAILS_BY_ENQUIRY_ID = "getFranchiseeApplicationDetailByEnquiryId/{NextGenEnquiryId}";
    public static String METHOD_NAME_GET_IOCL_RO_NAME = "getIOCLRoName/{IOCLROCODE}";
    public static String METHOD_NAME_GET_NAME_OF_REFERRAL_BY_EMPID = "getNameOfReferralByEmpId/{EMPLOYEEID}";
    public static String METHOD_NAME_GET_NAME_OF_REFERRAL_BY_VKID = "getNameOfReferralByVKID/{VKID}";
    public static String METHOD_NAME_GET_ENTITY_TYPE = "getEntityType";
    public static String METHOD_NAME_GET_REFERRED_BY = "getReferredBy";
    public static String METHOD_NAME_GET_SALUTATION = "getTitleList";
    public static String METHOD_NAME_GET_NATIONALITY = "getNationalityList";
    public static String METHOD_NAME_GET_HIGHEST_QUALIFICATION = "getQualificationList";
    public static String METHOD_NAME_GET_PRIOR_EXPERIENCE = "getPriorExperienceList";
    public static String METHOD_NAME_GET_CURRENT_OCCUPATION = "getOccupationList";
    public static String METHOD_NAME_GET_ABOUT_US = "getAboutUsList";
    public static String METHOD_NAME_GET_ACCOUNT_TYPE = "getAccountTypeList";
    public static String METHOD_NAME_GET_MODEL = "getModelNameList";
    public static String METHOD_NAME_GET_SOURCE_OF_FUND = "getFundSourceList";
    public static String METHOD_NAME_GET_PREMISE_OWNERSHIP_TYPE = "getPremiseOwnerShipList";
    public static String METHOD_NAME_GET_OWNED_BY = "getOwnedByList";
    public static String METHOD_NAME_GET_RELATIONSHIP = "getRelationshipList";
    public static String METHOD_NAME_GET_STATE = "getStateList";
    public static String METHOD_NAME_GET_DISTRICT = "getDistrictList/{stateId}";
    public static String METHOD_NAME_GET_VTC = "getVTCList/{stateId}/{districtId}";
    public static String METHOD_NAME_GET_BANK_NAME = "getBankNameList";
    public static String METHOD_NAME_GET_ADDRESS_PROOF_TYPE = "getAddressProofType";
    public static String METHOD_NAME_GET_GSTIN_STATE = "getGSTINState";
    public static String METHOD_NAME_SAVE_FRANCHISEE_APPLICATION = "saveFranchiseeApplication/{userId}/{type}";
    public static String METHOD_NAME_GET_DISCLAIMER = "getDisclaimer";
    public static String METHOD_NAME_ISRURAL = "IsRural/{villageId}";
    public static String METHOD_NAME_GET_REQUEST_CALL_BACK_TYPE = "getRequestCallBackType";
    public static String METHOD_NAME_GET_ENTITY_PROOF_TYPE = "getEntityProofType/{entityId}";
    public static String METHOD_NAME_ISIOCL_EXIST = "IsIoclExist/{ioclCode}/{nextgenFranchiseeApplicationid}";
    public static String METHOD_NAME_VERIFY_PANCARD_DETAIL = "verifyPanCardDetail/{enquiryId}/{pan_card_no}/{pan_card_name}";
    public static String METHOD_GET_BCA_BANK_NAME_LIST = "getBCABankNameList";

    public static String METHOD_NAME_ISREQUIRE_NEW_OR_EXISTING_CHECK = "isRequireNewOrExistingCheck/{enquiryId}";
    public static String METHOD_NAME_AUTHENTICATE_EXISTING_FRANCHISEE = "authenticateExistingFranchisee";
    public static String FROM_ISREQUIRE_CHECK = "FROM_ISREQUIRE_CHECK";
    public static String FROM_AUTHENTICATE_EXISTING_FRANCHISEE = "FROM_AUTHENTICATE_EXISTING_FRANCHISEE";
    //endregion

    //region Vakrangee Kendra Acknowledgement Form
    public static String METHOD_NAME_GET_KENDRA_ACK_DETAILS_BY_ID = "getKendraAcknowledgementDetailsById/{equipAckId}";
    public static String METHOD_NAME_GET_KENDRA_ALL_EQUIPMENT_ACK_DETAILS = "getKendraAllEquipmentAckDetails/{VKID}";
    public static String METHOD_NAME_GET_BRAND_NAME_WITH_MATERIAL_CODE = "getBrandName/{MATERIAL_CODE}";
    public static String METHOD_NAME_GET_BRAND_NAME = "getBrandName";
    public static String METHOD_NAME_GET_SERIES = "getSeries/{BRAND_ID}";
    public static String METHOD_NAME_GET_MODEL_PRODUCT = "getModel/{PRODUCT_ID}";
    public static String METHOD_NAME_GET_SRNO_EXISTS_STATUS = "getSrNoExistsStatus/{BRAND_ID}/{SERIAL_NO}";
    public static String METHOD_NAME_GET_GOODS_SERVICE = "getGoodsCondition";
    public static String METHOD_NAME_SAVE_KENDRA_ACK_DETAILS = "saveKendraAckDetails/{userId}";
    public static String METHOD_NAME_GET_EQUIPMENT_PACKING_PREVIEW_IMAGE = "getEquipmentPackingPreviewImage";

    //endregion

    //region Franchisee Authentication
    public static String METHOD_NAME_CHECK_FRANCHISEE_VERSION = "checkVersion/{PRODUCT_CODE}/{VERSION}";
    public static String METHOD_NAME_VALIDATE_MOB_EMAILID = "validateUser/{MOB_EMAIL}";
    public static String METHOD_NAME_GET_ALL_FRANCHISEE_ENQUIRY_LIST = "getAllFranchiseeEnquiryListByMobEmailID/{MOB_EMAIL}";
    public static String METHOD_NAME_GET_VKID_BY_ENQUIRY_ID = "getVKIDByEnquiryId/{ENQUIRY_ID}";
    public static String METHOD_NAME_UPDATE_FCMID_VIA_ENQUIRY_ID = "updateFCMIdViaEnquiryId";
    //endregion

    //region Hide/Show some sections on the basis of flag
    //Hide Aadhaar section in NextGen Franchisee Application
    public static boolean IsAadhaarSectionToHidden = true;
    //Added to hide Communication, Logistics Payment Made and GST Details layout in Site Visit ---On 11th Oct, 2018
    public static boolean IsToBeRemoved = true;
    //endregion

    //region Support Ticket
    public static String SUPPORT_TICKET_ISSUE_TYPE = "IssueType";
    public static String SUPPORT_TICKET_CATEGORY = "Category";
    public static String SUPPORT_TICKET_SUB_CATEGORY = "SubCategory";
    public static String OKAY_RESPONSE = "OKAY";
    public static String ERROR_RESPONSE = "ERROR";
    //endregion

    public static final String DB_NAME = "config.db";
    public static final int DATABASE_VERSION = 1;
    public static String DB_PATH;

    public static String RECYCLER_TYPE_GRID = "GRID";
    public static String RECYCLER_TYPE_VERTICAL = "VERTICAL";
    public static String RECYCLER_TYPE_HORIZONTAL = "HORIZONTAL";
    public static String NEXT_GEN_LOCATION_DETAIL = "NEXT_GEN_LOCATION_DETAIL";
    public static String NEXTGEN_SITE_VISIT = "NEXTGEN_SITE_VISIT";
    public static String NEXTGEN_SITE_WORK_COMMENCEMENT = "NEXTGEN_SITE_WORK_COMMENCEMENT";
    public static String NEXT_GEN_WORK_IN_PROGRESS = "NEXT_GEN_WORK_IN_PROGRESS";
    public static String NEXTGEN_SITE_VISIT_ADHOC = "NEXTGEN_SITE_VISIT_ADHOC";
    public static String NEXTGEN_WORK_COMPLETION_INTIMATION = "NEXT_GEN_WORK_COMPLETION_INTIMATION";
    public static String NEXTGEN_SITE_READINESS_AND_VERIFICATION = "NEXT_GEN_SITE_READINESS_AND_VERIFICATION";
    public static String NEXTGEN_FRANCHISEE_APPLICATION = "NEXT_GEN_FRANCHISEE_APPLICATION";
    protected static final String TABLE_CONTACTS = "ImageMaster";

    //Intent Keys
    public static final String INTENT_KEY_TYPE = "TYPE";
    public static final String INTENT_KEY_TITLE = "TITLE";
    public static final String INTENT_KEY_PORTRAIT_ALLOWED = "PORTRAIT_ALLOWED";
    public static final String INTENT_KEY_IMAGE_GALLERY_LIST = "ImageGalleryList";
    public static final String INTENT_KEY_PHOTO_CAPTURE_LIST = "PhotoCaptureList";
    public static final String INTENT_KEY_FROM_ACTIVITY = "ACTIVITY";
    public static final String INTENT_VALUE_SUPPORT_TICKET = "SUPPORT_TICKET";
    public static final String INTENT_VALUE_MAX_IMAGES_COUNT = "MAX_IMAGES_COUNT";

    public static ATMgetData mgetData = new ATMgetData();

    // NEXTGEN SITE TYPE - To Fetch Kendra Location Details
    public static final int NEXTGEN_SITE_VISIT_TYPE = 1;
    public static final int NEXTGEN_SITE_COMMENCEMENT_TYPE = 2;
    public static final int NEXTGEN_SITE_WIP_TYPE = 3;
    public static final int NEXTGEN_WORK_COMPLETION_TYPE = 4;
    public static final int NEXTGEN_SITE_READINESS_TYPE = 5;

    //region NextGen Site Readiness and Verification Status
    public static final String SUBMITTED_BY_FRANCHISEE = "0";
    public static final String VERIFIED_BY_FIELD_TEAM = "1";
    public static final String APPROVED_BY_RM = "2";
    public static final String SEND_BACK_FOR_CORRECTION_BY_RM = "3";
    public static final String RESUBMITTED_BY_FRANCHISEE = "4";
    public static final String REVERIFIED_BY_FIELD_TEAM = "5";
    public static final String ON_HOLD_BY_RM = "6";
    public static final String REJECTED_BY_FIELD_TEAM = "7";
    //endregion

    //region GWR Activity Status
    public static final String GWR_ACTIVITY_SUBMITTED = "0";
    public static final String GWR_ACTIVITY_APPROVED = "1";
    public static final String GWR_ACTIVITY_RESUBMITTED = "3";
    public static final String GWR_ACTIVITY_SEND_BACK_FOR_CORRECTION = "2";
    //endregion

    //region WIP ChatView
    public static String WIP_CHATVIEW_CATEGORY = "Category";
    public static String WIP_CHATVIEW_SUB_CATEGORY = "SubCategory";
    public static String WIP_CHATVIEW_GET_MESSAGES = "WIPMessages";
    public static String WIP_CHATVIEW_GET_MESSAGES_WITH_PAGINATION = "WIPMessagesWithPagination";
    public static String METHOD_NAME_GET_WIP_CHATVIEW_CATEGORY_DETAILS = "getWIPCategoryDetails/{VKIDOrEnquiryId}";
    public static String METHOD_NAME_GET_WIP_CHATVIEW_SUB_CATEGORY_DETAILS = "getWIPSubCategoryDetails/{CATEGORYID}/{VKIDOrEnquiryId}";
    public static String METHOD_NAME_GET_WIP_CHATVIEW_CHAT_MESSAGES_DETAILS = "getWIPMessages/{VKIDOrEnquiryId}";
    public static String METHOD_NAME_SAVE_WIP_CHATVIEW_CHAT_MESSAGE = "saveWIPMessage/{VKIDOrEnquiryId}";
    public static String METHOD_NAME_CHECK_WIP_CHATVIEW_STATUS = "checkWorkInProgress/{VKIDOrEnquiryId}";
    public static String METHOD_NAME_GET_WIP_CHATVIEW_CHAT_MESSAGE_WITH_PAGINATION = "getWIPMessagesWithPagination/{VKIDOrEnquiryId}/{PageNo}";
    public static String METHOD_NAME_GET_WIP_CHATVIEW_FILTER_MESSAGE_WITH_PAGINATION = "getWIPFilterMessagesWithPagination/{VKIDOrEnquiryId}/{PageNo}/{elementId}/{elementDetailId}";
    //endregion

    //region Phase Info
    public static String METHOD_NAME_GET_PHASE_DETAIL = "getPhaseDetail/{enquiryId}";
    public static String PHASE_1 = "PHASE-1";
    public static String PHASE_0 = "PHASE-0";
    //endregion

    //region Agreement type
    public static String METHOD_NAME_GET_DISPATCH_TYPE = "getDispatchType";
    public static String METHOD_NAME_GET_AGREMENT_DISPATCH_DETAILS = "getAgreementDispatchDetails/{VKIDOrEnquiryId}";
    public static String METHOD_SAVE_AGREEMENT_DISPATCH = "saveAgreementDispatch/{VKIDOrEnquiryId}/{type}";
    //endregion

    //region Hardware Tracking
    public static String METHOD_GET_HARDWARE_TRACKING_DETAILS = "getHardwareTrackingDetails/{VKIDOrEnquiryId}/{TrackingId}";
    //endregion

    //region BCA Entry Details
    public static String METHOD_NAME_GET_BCA_ENTRY_DETAILS = "BCA/getCompleteBCADetail/{VKID}";
    public static String METHOD_NAME_GET_BCA_SALUTATION = "BCA/getTitleList";
    public static String METHOD_NAME_GET_BCA_BANK_NAME = "BCA/getBankNameList";
    public static String METHOD_NAME_GET_BCA_QUALIFICATION = "BCA/getQualificationList";
    public static String METHOD_NAME_GET_BCA_RELIGION = "BCA/getReligionList";
    public static String METHOD_NAME_GET_BCA_BC_ABILITY = "BCA/getBCAbilityList";
    public static String METHOD_NAME_GET_BCA_CATEGORY = "BCA/getCategoryList";
    public static String METHOD_NAME_GET_BCA_STATE = "BCA/getStateList";
    public static String METHOD_NAME_GET_BCA_DIVISION = "BCA/getDivisionList/{stateId}";
    public static String METHOD_NAME_GET_BCA_DISTRICT = "BCA/getDistrictList/{stateId}";
    public static String METHOD_NAME_GET_BCA_BLOCK = "BCA/getBlockList/{districtId}";
    public static String METHOD_NAME_GET_BCA_VTC = "BCA/getVTCList/{blockId}";
    public static String METHOD_NAME_GET_BCA_WARD = "BCA/getWardList/{vtcId}";
    public static String METHOD_NAME_GET_BCA_SUPERVISOR_CODE = "BCA/getSupervisorCodeList";
    public static String METHOD_NAME_GET_BCA_DEVICE_TYPE = "BCA/getDeviceTypeList";
    public static String METHOD_NAME_SAVE_BCA_ENTRY_DETAIL = "BCA/saveBCAEntryDetail/{vklId}/{vkId}/{type}";
    //endregion

    //kendra location updation
    public static String URL_BASE_WS_VKMS_APP = String.format("https://%s.vakrangee.in/WSVKMSApp/", HOST);
    public static String METHOD_NAME_GET_KENDRA_LOCATION_DETAILS1 = "kendralocation/getKendraLocationDetails/{vkId}/{locationId}";
    public static String METHOD_NAME_GET_KENDRA_LOCATION_DETAILS2 = "kendralocation/getKendraLocationDetails/{vkId}/{locationId}/{scope} ";
    public static String METHOD_NAME_GET_STATE_LIST = "kendralocation/getStateList/{vkId}";
    public static String METHOD_NAME_GET_DISTRICT_LIST = "kendralocation/getDistrictList/{vkId}/{stateId}";
    public static String METHOD_NAME_GET_VTC_LIST = "kendralocation/getVTCList/{vkId}/{stateId}/{districtId}";
    public static String METHOD_NAME_GET_SPECIFIC_FRANCHISEE_DETAILS = "kendralocationupdation/getFranchiseeKendraLocationDetails/{vkId}";

    //atm loading
    public static String METHOD_SAVE_ATM_PHYSICAL_CASH_LOADING = "ATMLoading/saveATMPhysicalORCashLoadingDetails/{VKIDOrEnquiryId}/{type}";
    public static String METHOD_GET_ATM_PHYSICAL_CASH_LOADING = "ATMLoading/getATMPhysicalORCashLoadingDetails/{vkIdOrEnquiryId}/{cashLodingId}";
    public static final String PHYSICAL_LOADING_TYPE = "1";
    public static final String CASH_LOADING_TYPE = "2";
    //added
    public static String METHOD_GET_ATM_ID_DETAILS_LIST = "ATMLoading/getATMIDList/{vkIdOrEnquiryId}";
    public static String METHOD_GET_STATUS_ID_DETAILS_LIST = "ATMLoading/getStatusList/{vkIdOrEnquiryId}";
    public static String METHOD_GET_ATM_RO_CASH_LOADING_DATA = "ATMLoading/getATMROCashLoadingList/{vkIdOrEnquiryId}/{atmId}/{statusId}";

    //delivery address
    public static String METHOD_NAME_GET_DELIVEY_DETAILS = "DeliveryDetail/getDeliveryDetails/{vkIdOrEnquiryId}";
    public static String METHOD_NAME_GET_DELIVEY_DETAILS_1 = "DeliveryDetail/getDeliveryDetails/{vkIdOrEnquiryId}/{addressType}";
    public static String METHOD_NAME_GET_STATE_LIST_DELIVERY = "DeliveryDetail/getStateList/{vkIdOrEnquiryId}";
    public static String METHOD_NAME_GET_DISTRICT_LIST_DELIVERY = "DeliveryDetail/getDistrictList/{vkIdOrEnquiryId}/{stateId}";
    public static String METHOD_NAME_GET_VTC_LIST_DELIVERY = "DeliveryDetail/getVTCList/{vkIdOrEnquiryId}/{stateId}/{districtId}\n";
    public static String METHOD_NAME_GET_DELIVEY_ADDRESS_TYPE = "DeliveryDetail/getDeliveryAddressType/{vkIdOrEnquiryId}";


    public static String METHOD_NAME_POST_DELIVEY_DETAILS = "DeliveryDetail/saveDeliveryDetails/{vkIdOrEnquiryId}";
    public static String METHOD_NAME_GET_FRANCHISEE_APP_MENU_CONTROLLER = "FranchiseeAppMenuController/getFranchiseeAppMenuDetail/{vkIdOrEnquiryId}";

    public static String METHOD_NAME_GET_PAY_HISTORY_DETAILS = "PaymentHistory/getPaymentHistoryDetails/{vkIdOrEnquiryId}";

    //region Document Manager
    public static String METHOD_NAME_GET_DOCUMENT_DETAILS_BY_TYPE = "DocumentManager/getDocumentDetailsByType/{vkIdOrEnquiryId}/{type}";
    public static String METHOD_NAME_GET_DOCUMENT_TYPE_DETAILS = "DocumentManager/getDocumentTypeList/{vkIdOrEnquiryId}";
    public static String METHOD_NAME_GET_DOCUMENT_STATE = "DocumentManager/getStateList/{vkIdOrEnquiryId}";
    public static String METHOD_NAME_GET_DOCUMENT_DISTRICT = "DocumentManager/getDistrictList/{vkIdOrEnquiryId}/{stateId}";
    public static String METHOD_NAME_GET_DOCUMENT_VTC = "DocumentManager/getVTCList/{vkIdOrEnquiryId}/{stateId}/{districtId}";
    public static String METHOD_NAME_SAVE_DOCUMENT_DETAIL = "DocumentManager/saveDocumentDetails/{vkIdOrEnquiryId}/{type}";

    //endregion

    //region Simcard details
    public static String METHOD_NAME_GET_SIMCARD_COMPANY_LIST = "ATMSIMDetails/getServiceProviderList/{vkIdOrEnquiryId}";
    public static String METHOD_NAME_GET_SIMCARD_DETAILS = "ATMSIMDetails/getAllATMSIMDetails/{vkIdOrEnquiryId}/{atmId}";
    public static String METHOD_NAME_SAVE_SIMCARD_DETAIL = "ATMSIMDetails/saveATMSIMDetails/{vkIdOrEnquiryId}";
    public static String METHOD_NAME_GET_ATM_LIST = "ATMSIMDetails/getATMIDList/{vkIdOrEnquiryId}";

}