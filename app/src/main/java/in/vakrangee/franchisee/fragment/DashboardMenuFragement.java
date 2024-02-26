package in.vakrangee.franchisee.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.activity.DashboardActivity;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class DashboardMenuFragement extends Fragment {


    private View view;
    private ArrayList<DashboardMenuModel> arrayList;
    private Context context;
    private RecyclerView recyclerView_menu_fragment;
    private RecyclerViewDashboardMenuAdapter recyclerViewDashboardMenuAdapter;

    public DashboardMenuFragement() {
    }

    public DashboardMenuFragement(Context context) {
        // Required empty public constructor
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_dashboard_menu_fragement, container, false);

        if (context == null)
            context = getContext();
        init(view);
        return view;
    }

    private void init(View view) {
        recyclerView_menu_fragment = view.findViewById(R.id.recyclerView_menu_fragment);

        arrayList = new ArrayList<DashboardMenuModel>();
        arrayList.add(new DashboardMenuModel("Application Form", "f298"));
        arrayList.add(new DashboardMenuModel("NextGen Site Details", "f015"));
       // arrayList.add(new DashboardMenuModel("Vakrangee Kendra Launching and Inauguration", "f015"));


        recyclerViewDashboardMenuAdapter = new RecyclerViewDashboardMenuAdapter(context, arrayList, new RecyclerViewDashboardMenuAdapter.OnItemClicked() {
            @Override
            public void onItemClick(int position) {
                //Toast.makeText(context, "Item" + position, Toast.LENGTH_SHORT).show();
                methodFranhiseeApplication(position);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView_menu_fragment.setLayoutManager(layoutManager);
        recyclerView_menu_fragment.setItemAnimator(new DefaultItemAnimator());
        recyclerView_menu_fragment.setAdapter(recyclerViewDashboardMenuAdapter);
        GridLayoutManager manager = new GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false);
        recyclerView_menu_fragment.setLayoutManager(manager);
    }

    private void methodFranhiseeApplication(int position) {
        DashboardActivity dashboardActivity = new DashboardActivity();
        if (position == 0) {
            dashboardActivity.getNextGenFranhiseeApp(context);
        } else
        if (position == 1) {
            dashboardActivity.getNextGenSiteDeatils(context);
        } /*else if(position == 1) {

        }
*/
    }

    private void loadFragment(Fragment fragment) {
        // create a FragmentManager
        FragmentManager fm = getFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit(); // save the changes
    }

}
