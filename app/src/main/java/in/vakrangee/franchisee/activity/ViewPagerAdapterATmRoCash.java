package in.vakrangee.franchisee.activity;

import android.content.Context;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import in.vakrangee.franchisee.fragment.RetailOutletCashAcktFragment;

/**
 * Created by nileshd on 6/16/2017.
 */
public class ViewPagerAdapterATmRoCash extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    Context context;
    ViewPager viewPager;
    TabLayout tabLayout;

    // public static ODSpinner odSpinner = new ODSpinner();
    public ViewPagerAdapterATmRoCash(Context applicationContext, FragmentManager fm, int NumOfTabs, ViewPager viewPager, TabLayout tabLayout) {
        super(fm);
        this.context = applicationContext;
        this.mNumOfTabs = NumOfTabs;
        this.viewPager = viewPager;
        this.tabLayout = tabLayout;

    }


    @Override
    public Fragment getItem(int position) {


        switch (position) {
            case 0:

                RetailOutletCashAcktFragment tab0 = new RetailOutletCashAcktFragment(viewPager);
                return tab0;


            default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
