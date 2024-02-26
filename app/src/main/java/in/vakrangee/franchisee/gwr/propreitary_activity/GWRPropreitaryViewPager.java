package in.vakrangee.franchisee.gwr.propreitary_activity;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import in.vakrangee.franchisee.gwr.GWRFragment;
import in.vakrangee.franchisee.gwr.gwrwitness_camera.GWRCameraManFragment;
import in.vakrangee.franchisee.gwr.gwrwitness_camera.GWRWitnessFragment;
import in.vakrangee.supercore.franchisee.model.FranchiseeDetails;
import in.vakrangee.supercore.franchisee.utils.Logger;

public class GWRPropreitaryViewPager extends FragmentStatePagerAdapter {

    static final String TAG = "GWRPropreitaryViewPager";
    int mNumOfTabs;
    FranchiseeDetails franchiseeDetails;
    Context context;
    Logger logger;
    boolean IsEditable;

    public GWRPropreitaryViewPager(Context applicationContext, FragmentManager fm, int NumOfTabs, boolean IsEditable) {
        super(fm);
        this.context = applicationContext;
        this.mNumOfTabs = NumOfTabs;
        this.IsEditable = IsEditable;
        logger = Logger.getInstance(context);

    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                GWRFragment tab1 = new GWRFragment(false, "1"); // 1 - Prepartory Activity
                return tab1;
            case 1:
                GWRWitnessFragment tab2 = new GWRWitnessFragment(false);
                return tab2;
            case 2:
                GWRCameraManFragment tab3 = new GWRCameraManFragment(false);
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
