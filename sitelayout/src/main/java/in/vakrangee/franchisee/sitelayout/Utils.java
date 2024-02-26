package in.vakrangee.franchisee.sitelayout;

import android.content.Context;

import in.vakrangee.franchisee.sitelayout.asyntask.AsyncNextGenFranchiseeDetails;
import in.vakrangee.franchisee.sitelayout.finalrmapproval.AsyncGetFinalRMApprovalDetails;
import in.vakrangee.supercore.franchisee.model.FranchiseeDetails;
import in.vakrangee.supercore.franchisee.utils.Constants;


public class Utils {

    public static FranchiseeDetails updateFranchiseeDetails(FranchiseeDetails franchiseeDetails, Context context,
                                                            String getid, String getVkid, String getTokenId, String getimei,
                                                            String getdeviceid, String getsimserialnumber) {
        new AsyncNextGenFranchiseeDetails(context, getid).execute(getid, getVkid, getTokenId, getimei, getdeviceid,
                getsimserialnumber);

        return franchiseeDetails;
    }

    public static void updateFranchicess(FranchiseeDetails franchiseeDetails, Context context,
                                         String getid, String getVkid, String getTokenId, String getimei,
                                         String getdeviceid, String getsimserialnumber) {
        new AsyncNextGenFranchiseeDetails(context, getid).execute(getid, getVkid, getTokenId, getimei, getdeviceid,
                getsimserialnumber);


    }

    public static void updateFranchicess(FranchiseeDetails franchiseeDetails, Context context,
                                         String getid, String getVkid) {
        new AsyncNextGenFranchiseeDetails(Constants.NEXTGEN_SITE_VISIT_ADHOC, context, getid).execute(getVkid);

    }

    public static void reloadWorkCommencentmentData(String modetype, Context context,
                                                    String vkId, String fAppNo, String tokenId, String imei,
                                                    String deviceid, String simno) {
        new AsyncNextGenFranchiseeDetails(modetype, context, vkId).execute(vkId, fAppNo, tokenId, imei, deviceid, simno);
    }

    public static void reloadReadinessVerificationData(String modetype, Context context, String vkId, String nextGenApplicationId) {
        new AsyncNextGenFranchiseeDetails(modetype, context, vkId).execute(vkId, nextGenApplicationId);
    }

    public static void reloadMandatoryBrandingVerificationData(String modetype, Context context, String vkId, String nextGenApplicationId) {
        new AsyncNextGenFranchiseeDetails(modetype, context, vkId).execute(vkId, nextGenApplicationId);
    }


    public static void reloadFinalRMReadinessVerificationData(String modetype, Context context, String vkId, String nextGenApplicationId) {
        new AsyncGetFinalRMApprovalDetails(modetype, context, vkId).execute(vkId, nextGenApplicationId);
    }

    public static void reloadWorkCommencentmentData(String modetype, Context context,
                                                    String vkId, String fAppNo) {
        new AsyncNextGenFranchiseeDetails(modetype, context, vkId).execute(vkId, fAppNo);
    }

}
