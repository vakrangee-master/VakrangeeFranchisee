package in.vakrangee.franchisee.activity;

import android.content.Context;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import in.vakrangee.franchisee.fragment.MyATMAmountFragment;
import in.vakrangee.franchisee.fragment.MyATMCashLoadFragment;
import in.vakrangee.franchisee.fragment.MyATMPhysicalCashFragment;
import in.vakrangee.franchisee.fragment.MyATMRoCashLoadingFragment;

/**
 * Created by Nileshd on 6/1/2017.
 */
public class ViewPagerAdapterATmSystem extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    Context context;
    ViewPager viewPager;
    TabLayout tabLayout;

    // public static ODSpinner odSpinner = new ODSpinner();
    public ViewPagerAdapterATmSystem(Context applicationContext, FragmentManager fm, int NumOfTabs, ViewPager viewPager, TabLayout tabLayout) {
        super(fm);
        this.context = applicationContext;
        this.mNumOfTabs = NumOfTabs;
        this.viewPager = viewPager;
        this.tabLayout=tabLayout;

    }




    @Override
    public Fragment getItem(int position) {


        switch (position) {
            case 0:

                MyATMRoCashLoadingFragment tab0 = new MyATMRoCashLoadingFragment(viewPager,tabLayout);

                return tab0;
            case 1:

                MyATMAmountFragment tab1 = new MyATMAmountFragment(viewPager);

                // MyATMFillDataFragment tab1 = new MyATMFillDataFragment();
                return tab1;
            case 2:

                MyATMPhysicalCashFragment tab2 = new MyATMPhysicalCashFragment(viewPager);

                return tab2;
            case 3:

                MyATMCashLoadFragment tab3 = new MyATMCashLoadFragment(viewPager);

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
