package in.vakrangee.franchisee.fragment;

import android.content.Context;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import in.vakrangee.franchisee.R;

/**
 * Created by Nileshd on 11/12/2016.
 */
public class ExpandableListDataSource {
    /**
     * Returns fake data of films
     *
     * @param context
     * @return
     */
    public static Map<String, List<String>> getData(Context context) {
        Map<String, List<String>> expandableListData = new TreeMap<>();

        List<String> atm_list = Arrays.asList(context.getResources().getStringArray(R.array.all_atm_list));

        List<String> bihar = Arrays.asList(context.getResources().getStringArray(R.array.bihar));
        List<String> Chandigarh = Arrays.asList(context.getResources().getStringArray(R.array.Chandigarh));
        List<String> Delhi = Arrays.asList(context.getResources().getStringArray(R.array.Delhi));
        List<String> GCR = Arrays.asList(context.getResources().getStringArray(R.array.GCR));
        List<String> Gujrat = Arrays.asList(context.getResources().getStringArray(R.array.Gujrat));
        List<String> Haryana = Arrays.asList(context.getResources().getStringArray(R.array.haryana));
        List<String> HimachalPradesh = Arrays.asList(context.getResources().getStringArray(R.array.HimachalPradesh));
        List<String> Jharkhand = Arrays.asList(context.getResources().getStringArray(R.array.Jharkhand));


        expandableListData.put(atm_list.get(0), bihar);
        expandableListData.put(atm_list.get(1), Chandigarh);
        expandableListData.put(atm_list.get(2), Delhi);
        expandableListData.put(atm_list.get(3), GCR);
        expandableListData.put(atm_list.get(4), Gujrat);
        expandableListData.put(atm_list.get(5), Haryana);
        expandableListData.put(atm_list.get(6), HimachalPradesh);
        expandableListData.put(atm_list.get(7), Jharkhand);

        return expandableListData;
    }

}
