package in.vakrangee.franchisee.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import in.vakrangee.franchisee.fragment.MyLast10TranscationFragment;
import in.vakrangee.franchisee.fragment.MyWalltetStatementFragment;

/**
 * Created by Nileshd on 6/17/2016.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public ViewPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                MyLast10TranscationFragment tab1 = new MyLast10TranscationFragment();
                return tab1;
            case 1:
                MyWalltetStatementFragment tab2 = new MyWalltetStatementFragment();
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
