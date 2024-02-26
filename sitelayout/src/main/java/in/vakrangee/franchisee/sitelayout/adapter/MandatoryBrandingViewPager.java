package in.vakrangee.franchisee.sitelayout.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

import in.vakrangee.franchisee.sitelayout.fragment.NextGenKendraLocationFragment;
import in.vakrangee.franchisee.sitelayout.fragment.NextGenProfileFragment;
import in.vakrangee.franchisee.sitelayout.mendatorybranding.MandatoryBrandingInteriorFragment;
import in.vakrangee.supercore.franchisee.model.FranchiseeDetails;
import in.vakrangee.supercore.franchisee.model.FranchiseeTimeLineDetails;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.Logger;

public class MandatoryBrandingViewPager extends FragmentStatePagerAdapter {

    static final String TAG = "MandatoryBrandingViewPager";
    int mNumOfTabs;
    FranchiseeDetails franchiseeDetails;
    Context context;
    Logger logger;
    boolean IsEditable;
    String mode;
    List<FranchiseeTimeLineDetails> franchiseeTimeLineDetailsList;

    public MandatoryBrandingViewPager(Context applicationContext, FragmentManager fm, int NumOfTabs, FranchiseeDetails franchiseeDetails, boolean IsEditable, String mode) {
        super(fm);
        this.context = applicationContext;
        this.mNumOfTabs = NumOfTabs;
        this.franchiseeDetails = franchiseeDetails;
        this.IsEditable = IsEditable;
        this.mode = mode;
        logger = Logger.getInstance(context);

        // initStatusAndRemarks();
    }

    // Extract NextGen Site Visit Status and its Description
    private void initStatusAndRemarks() {
        try {

            franchiseeDetails.setAllowToEdit(true);

        } catch (Exception e) {
            e.printStackTrace();
            logger.writeError(TAG, "Error in initializing status and remarks : " + e.toString());
        }
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                NextGenProfileFragment tab1 = new NextGenProfileFragment(Constants.NEXTGEN_SITE_MANDATORY_BRANDING_VERIFICATION, franchiseeDetails);
                return tab1;
            case 1:
                NextGenKendraLocationFragment tab2 = new NextGenKendraLocationFragment(Constants.NEXTGEN_SITE_MANDATORY_BRANDING_VERIFICATION, franchiseeDetails);
                return tab2;
            case 2:
                MandatoryBrandingInteriorFragment tab3 = new MandatoryBrandingInteriorFragment(franchiseeDetails, IsEditable,mode);
                return tab3;
            /*case 3:
                SiteReadinessBrandingFragment tab4 = new SiteReadinessBrandingFragment(franchiseeDetails, IsEditable);
                return tab4;
            case 4:
                NextGenKendraPhotoFragment tab5 = new NextGenKendraPhotoFragment(franchiseeDetails, Constants.NEXT_GEN_SITE_MANDATORY_BRANDING_COMPLETED);
                return tab5;*/

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
