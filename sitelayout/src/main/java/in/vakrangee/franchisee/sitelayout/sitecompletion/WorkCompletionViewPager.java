package in.vakrangee.franchisee.sitelayout.sitecompletion;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

import in.vakrangee.supercore.franchisee.model.FranchiseeDetails;
import in.vakrangee.supercore.franchisee.model.FranchiseeTimeLineDetails;
import in.vakrangee.supercore.franchisee.utils.Logger;

public class WorkCompletionViewPager extends FragmentStatePagerAdapter {

    static final String TAG = WorkCompletionViewPager.class.getCanonicalName();
    int mNumOfTabs;
    FranchiseeDetails franchiseeDetails;
    Context context;
    Logger logger;

    List<FranchiseeTimeLineDetails> franchiseeTimeLineDetailsList;

    public static final int DATA_UPDATED = 0;
    public static final int SITE_SEND_BACK_FOR_CORRECTION = 1;
    public static final int SITE_RESUBMITTED = 2;
    public static final int SITE_VERIFIED = 3;


    public WorkCompletionViewPager(Context applicationContext, FragmentManager fm, int NumOfTabs, FranchiseeDetails franchiseeDetails) {
        super(fm);
        this.context = applicationContext;
        this.mNumOfTabs = NumOfTabs;
        this.franchiseeDetails = franchiseeDetails;
        logger = Logger.getInstance(context);

        // Set Allow to Edit
        franchiseeDetails.setAllowToEdit(true);
    }


    @Override
    public Fragment getItem(int position) {

        switch (position) {
            /*case 0:
                NextGenProfileFragment tab1 = new NextGenProfileFragment(Constants.NEXTGEN_WORK_COMPLETION_INTIMATION, franchiseeDetails);
                return tab1;*/
            case 0:
                WorkCompletionIntimationFragment tab2 = new WorkCompletionIntimationFragment(franchiseeDetails);
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}
