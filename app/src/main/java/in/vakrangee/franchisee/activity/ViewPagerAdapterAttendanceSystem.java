package in.vakrangee.franchisee.activity;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import in.vakrangee.franchisee.fragment.MyAttendanceFragment;
import in.vakrangee.franchisee.fragment.MyOutdoorDutyFragment;

/**
 * Created by Nileshd on 5/30/2017.
 */
public class ViewPagerAdapterAttendanceSystem extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    Context context;

    public ViewPagerAdapterAttendanceSystem(Context applicationContext, FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.context = applicationContext;
        this.mNumOfTabs = NumOfTabs;


    }


    @Override
    public Fragment getItem(int position) {


        switch (position) {
            case 0:
                MyAttendanceFragment tab1 = new MyAttendanceFragment(context);
                return tab1;
            case 1:
                MyOutdoorDutyFragment tab2 = new MyOutdoorDutyFragment(context);
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
