package in.vakrangee.franchisee.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import in.vakrangee.franchisee.fragment.MyVakrangeeKendraLocationFragment;
import in.vakrangee.franchisee.fragment.MyVakrangeeKendraPhotoFragment;
import in.vakrangee.franchisee.fragment.MyVakrangeeKendraTimingFragment;
import in.vakrangee.supercore.franchisee.model.MyVKMaster;

/**
 * Created by Nileshd on 12/30/2016.
 */
public class ViewPagerAdapterVakrangeePhoto extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    MyVKMaster myVKMaster;
    Context context;

    public ViewPagerAdapterVakrangeePhoto(Context applicationContext, FragmentManager fm, int NumOfTabs, MyVKMaster myVKMaster) {
        super(fm);
        this.context = applicationContext;
        this.mNumOfTabs = NumOfTabs;
        this.myVKMaster = myVKMaster;

    }


    @Override
    public Fragment getItem(int position) {


        switch (position) {
            case 0:
                MyVakrangeeKendraLocationFragment tab1 = new MyVakrangeeKendraLocationFragment(myVKMaster);
                return tab1;
            case 1:
                MyVakrangeeKendraPhotoFragment tab2 = new MyVakrangeeKendraPhotoFragment(myVKMaster);

                return tab2;
            case 2:
                MyVakrangeeKendraTimingFragment tab3 = new MyVakrangeeKendraTimingFragment(myVKMaster);

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
