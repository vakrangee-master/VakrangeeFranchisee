package in.vakrangee.franchisee.atmloading;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class ATMPageAdapter extends SmartFragmentStatePagerAdapter {

    private static int NUM_ITEMS = 2;

    public ATMPageAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return PhysicalLoadingFragment.newInstance();
            case 1:
                return CashLoadingFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "Physical Cash";
            case 1:
                return "Cash Loading";
            default:
                return null;
        }
    }

}
