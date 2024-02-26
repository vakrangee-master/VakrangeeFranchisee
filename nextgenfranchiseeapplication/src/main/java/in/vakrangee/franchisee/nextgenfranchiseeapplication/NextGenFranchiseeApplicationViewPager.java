package in.vakrangee.franchisee.nextgenfranchiseeapplication;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import in.vakrangee.supercore.franchisee.model.FranchiseeDetails;
import in.vakrangee.supercore.franchisee.utils.Logger;

public class NextGenFranchiseeApplicationViewPager extends FragmentStatePagerAdapter {

    static final String TAG = "NextGenFranchiseeApplicationViewPager";
    int mNumOfTabs;
    FranchiseeDetails franchiseeDetails;
    Context context;
    Logger logger;
    boolean IsEditable;

    public NextGenFranchiseeApplicationViewPager(Context applicationContext, FragmentManager fm, int NumOfTabs, FranchiseeDetails franchiseeDetails, boolean IsEditable) {
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
                NextGenFranchiseeApplicationFormFragment formFragment = new NextGenFranchiseeApplicationFormFragment(franchiseeDetails);
                return formFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
