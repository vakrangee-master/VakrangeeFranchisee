package in.vakrangee.franchisee.sitelayout.finalrmapproval;

import android.content.Context;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import in.vakrangee.franchisee.sitelayout.fragment.NextGenKendraLocationFragment;
import in.vakrangee.franchisee.sitelayout.fragment.NextGenKendraPhotoFragment;
import in.vakrangee.franchisee.sitelayout.fragment.NextGenProfileFragment;
import in.vakrangee.franchisee.sitelayout.sitereadiness.SiteReadinessBrandingFragment;
import in.vakrangee.franchisee.sitelayout.sitereadiness.SiteReadinessInteriorFragment;
import in.vakrangee.supercore.franchisee.model.FranchiseeDetails;
import in.vakrangee.supercore.franchisee.model.FranchiseeTimeLineDetails;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.Logger;

public class FinalRMApprovalViewPager extends FragmentStatePagerAdapter {

    static final String TAG = "FinalRMApprovalViewPager";
    int mNumOfTabs;
    FranchiseeDetails franchiseeDetails;
    Context context;
    Logger logger;
    boolean IsEditable;

    public FinalRMApprovalViewPager(Context applicationContext, FragmentManager fm, int NumOfTabs,FranchiseeDetails franchiseeDetails, boolean IsEditable) {
        super(fm);
        this.context = applicationContext;
        this.mNumOfTabs = NumOfTabs;
        this.franchiseeDetails = franchiseeDetails;
        this.IsEditable = IsEditable;
        logger = Logger.getInstance(context);

    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                NextGenRMApprovalProfileFragment tab1 = new NextGenRMApprovalProfileFragment(Constants.NEXTGEN_SITE_READINESS_AND_VERIFICATION, franchiseeDetails);
                return tab1;
            case 1:
                NextGenFinalRMApprovalFragment tab2 = new NextGenFinalRMApprovalFragment(franchiseeDetails, IsEditable);
                return tab2;
            case 2:
                NextGenKendraPhotoFragment tab5 = new NextGenKendraPhotoFragment(franchiseeDetails, Constants.NEXTGEN_SITE_READINESS_AND_VERIFICATION);
                return tab5;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
