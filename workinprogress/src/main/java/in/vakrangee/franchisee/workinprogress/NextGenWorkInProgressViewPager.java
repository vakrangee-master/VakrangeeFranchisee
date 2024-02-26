package in.vakrangee.franchisee.workinprogress;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import in.vakrangee.supercore.franchisee.model.FranchiseeDetails;
import in.vakrangee.supercore.franchisee.utils.Logger;

public class NextGenWorkInProgressViewPager extends FragmentStatePagerAdapter {

    static final String TAG = "NextGenWorkInProgressViewPager";
    int mNumOfTabs;
    FranchiseeDetails franchiseeDetails;
    Context context;
    Logger logger;
    boolean IsEditable;

    public NextGenWorkInProgressViewPager(Context applicationContext, FragmentManager fm, int NumOfTabs, FranchiseeDetails franchiseeDetails, boolean IsEditable) {
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
                NextGenWorkInProgressInteriorFragment tab2 = new NextGenWorkInProgressInteriorFragment(franchiseeDetails, IsEditable);
                return tab2;
           /* case 0:
                NextGenProfileFragment tab1 = new NextGenProfileFragment(Constants.NEXT_GEN_WORK_IN_PROGRESS, franchiseeDetails, IsEditable);
                return tab1;
            case 1:
                NextGenWorkInProgressInteriorFragment tab2 =  new NextGenWorkInProgressInteriorFragment(franchiseeDetails, IsEditable);
                return tab2;
            case 2:
                NextGenWorkInProgressFragment tab3 = new NextGenWorkInProgressFragment(franchiseeDetails, IsEditable);
                return tab3;*/

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
