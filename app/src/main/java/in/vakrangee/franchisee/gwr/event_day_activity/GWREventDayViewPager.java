package in.vakrangee.franchisee.gwr.event_day_activity;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import in.vakrangee.franchisee.gwr.event_photos.GWREventPhotosFragment;
import in.vakrangee.franchisee.gwr.witness_statement.GWRUploadWitnessStatementFragment;
import in.vakrangee.supercore.franchisee.model.FranchiseeDetails;
import in.vakrangee.supercore.franchisee.utils.Logger;

public class GWREventDayViewPager extends FragmentStatePagerAdapter {

    static final String TAG = "GWREventDayViewPager";
    int mNumOfTabs;
    FranchiseeDetails franchiseeDetails;
    Context context;
    Logger logger;
    boolean IsEditable;

    public GWREventDayViewPager(Context applicationContext, FragmentManager fm, int NumOfTabs, boolean IsEditable) {
        super(fm);
        this.context = applicationContext;
        this.mNumOfTabs = NumOfTabs;
        this.IsEditable = IsEditable;
        logger = Logger.getInstance(context);

    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
           /* case 0:
                GWRFragment tab1 = new GWRFragment(false, "2");     //2: Event Day Activity
                return tab1;*/
            case 0:
                GWREventPhotosFragment tab2 = new GWREventPhotosFragment(false);
                return tab2;
            case 1:
                GWRUploadWitnessStatementFragment tab3 = new GWRUploadWitnessStatementFragment(false);
                return tab3;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
