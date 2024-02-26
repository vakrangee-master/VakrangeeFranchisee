package in.vakrangee.franchisee.sitelayout.sitecommencement;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import org.json.JSONObject;

import java.util.List;

import in.vakrangee.supercore.franchisee.model.FranchiseeDetails;
import in.vakrangee.supercore.franchisee.model.FranchiseeRemarkDetails;
import in.vakrangee.supercore.franchisee.model.FranchiseeTimeLineDetails;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.JSONUtils;
import in.vakrangee.supercore.franchisee.utils.Logger;

public class NextGenSiteCommencementViewPager extends FragmentStatePagerAdapter {

    static final String TAG = NextGenSiteCommencementViewPager.class.getCanonicalName();
    int mNumOfTabs;
    FranchiseeDetails franchiseeDetails;
    Context context;
    Logger logger;

    List<FranchiseeTimeLineDetails> franchiseeTimeLineDetailsList;

    /*
    0=Work Commencement Pending
    1=Work Commencement Send Back For Correction By RM
    2=Work Commencement Resubmitted For Verification By FR
    3=Work Commencement On Hold By RM
    4=Work Commencement Verified By RM
     */
    public static final int DATA_UPDATED = 0;
    public static final int SITE_SEND_BACK_FOR_CORRECTION = 1;
    public static final int SITE_RESUBMITTED = 2;
    public static final int SITE_HOLD = 3;
    public static final int SITE_VERIFIED = 4;

    public NextGenSiteCommencementViewPager(Context applicationContext, FragmentManager fm, int NumOfTabs, FranchiseeDetails franchiseeDetails) {
        super(fm);
        this.context = applicationContext;
        this.mNumOfTabs = NumOfTabs;
        this.franchiseeDetails = franchiseeDetails;
        logger = Logger.getInstance(context);

        initStatusAndRemarks();
    }

    // Extract NextGen Site Visit Status and its Description
    private void initStatusAndRemarks() {
        try {
            franchiseeTimeLineDetailsList = franchiseeDetails.getTimeLineList();
            if (franchiseeTimeLineDetailsList != null &&
                    franchiseeTimeLineDetailsList.size() > 0) {

                int status = -1;
                FranchiseeTimeLineDetails franchiseeTimeLineDetails = franchiseeTimeLineDetailsList.get(franchiseeTimeLineDetailsList.size() - 1);
                status = Integer.parseInt(franchiseeTimeLineDetails.getNextgenSiteVisitStatus());
                String desc = franchiseeTimeLineDetails.getNextgenSiteVisitDescription();

                franchiseeDetails.setStatus(status);
                franchiseeDetails.setResubmitStatus(status);
                franchiseeDetails.setStatusDesc(desc);

                // Based on status allow to edit site detail
                if (status == DATA_UPDATED || status == SITE_RESUBMITTED || status == SITE_HOLD || status == SITE_VERIFIED) {
                    franchiseeDetails.setAllowToEdit(false);
                } else {
                    franchiseeDetails.setAllowToEdit(true);
                }

                // Extract Remarks Data Based On Status
                if (status == SITE_SEND_BACK_FOR_CORRECTION) {
                    JSONObject jsonObject = JSONUtils.convertStringToJSONObject(franchiseeTimeLineDetails.getNextgenSiteVisitRemarks(), "\\r?\\n", ":");
                    FranchiseeRemarkDetails franchiseeRemarkDetails = JSONUtils.toJson(FranchiseeRemarkDetails.class, jsonObject.toString());
                    franchiseeDetails.setFranchiseeRemarkDetails(franchiseeRemarkDetails);

                    AlertDialogBoxInfo.alertDialogShow(context, "There is an issue with Site Commencement Detail. Please re-submit the detail.");
                }

                return;
            } else {
                franchiseeDetails.setAllowToEdit(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.writeError(TAG, "Error in initializing status and remarks : " + e.toString());
        }
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            /*case 0:
                NextGenProfileFragment tab1 = new NextGenProfileFragment(Constants.NEXTGEN_SITE_WORK_COMMENCEMENT, franchiseeDetails);
                return tab1;
            case 1:
                NextGenKendraLocationFragment tab2 = new NextGenKendraLocationFragment(franchiseeDetails, true);
                return  tab2;*/
            case 0:
                NextGenSiteWorkCommencementFragment tab3 = new NextGenSiteWorkCommencementFragment(franchiseeDetails);
                return tab3;
            /*case 3:
                NextGenSiteReadinessDetailFragment tab4 = new NextGenSiteReadinessDetailFragment(franchiseeDetails);
                return tab4;*/
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}
