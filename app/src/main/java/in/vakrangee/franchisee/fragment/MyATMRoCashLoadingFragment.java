package in.vakrangee.franchisee.fragment;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.adapter.AdapterLoadingPending;
import in.vakrangee.supercore.franchisee.model.ATMIDSpinner;
import in.vakrangee.supercore.franchisee.model.ATMgetData;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.webservice.WebService;

import static in.vakrangee.supercore.franchisee.utils.Constants.mgetData;

//

/**
 * Created by Nileshd on 6/5/2017.
 */
public class MyATMRoCashLoadingFragment extends Fragment {
    Button btnSubmitATM;
    String diplayServerResopnse, diplayServerResopnse_RO_CASH_LODING;

    ImageView btnGo;
    JSONObject jsonobject;
    JSONArray jsonarray;
    private RecyclerView recyclerView;
    private AdapterLoadingPending mAdapter;
    private AdapterCashLoading mAdapterCashLoad;
    Connection connection;
    InternetConnection internetConnection;
    ProgressDialog progress;
    String TAG = "Response";
    Spinner SpnGetATMList;
    View rootView;
    TextView emptyView;
    CheckBox chk_select_all;
    String SpnSelected, Status;
    double gpaDouble;
    String getATMID;
    private ArrayList<ATMIDSpinner> item_list = new ArrayList<>();

    private ViewPager viewPager;
    TabLayout tabLayout;

    public MyATMRoCashLoadingFragment(ViewPager viewPager, TabLayout tabLayout) {
        this.viewPager = viewPager;
        this.tabLayout = tabLayout;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_atm_ro_cash_loading, container, false);

        btnGo = (ImageView) rootView.findViewById(R.id.btnGo);
        btnSubmitATM = (Button) rootView.findViewById(R.id.btnSubmitATM);
        btnSubmitATM.setVisibility(View.INVISIBLE);
        connection = new Connection(getActivity());

        internetConnection = new InternetConnection(getContext());
        if (internetConnection.isNetworkAvailable(getContext()) == false) {

            AlertDialogBoxInfo.alertDialogShow(getContext(), getResources().getString(R.string.internetCheck));

        } else {

            progress = new ProgressDialog(getActivity());

            progress.setMessage(getResources().getString(R.string.pleaseWait));
            progress.setCancelable(false);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();
            AsyncGetATMList myRequest = new AsyncGetATMList();
            myRequest.execute();
        }

        emptyView = (TextView) rootView.findViewById(R.id.empty_view_atm_ro);
        SpnGetATMList = (Spinner) rootView.findViewById(R.id.spnATMID);
        // SpnGetATMList.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, atmNames));
        chk_select_all = (CheckBox) rootView.findViewById(R.id.chk_select_all);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        chk_select_all.setVisibility(View.INVISIBLE);

        final Spinner spnStatus = (Spinner) rootView.findViewById(R.id.spnStatus);
        List<String> Store = new ArrayList<String>();
        Store.add("Loading Pending");
        Store.add("Cash Loaded");
        spnStatus.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, Store));

        spnStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                String imc_met = spnStatus.getSelectedItem().toString();
                if (imc_met.equals("Loading Pending")) {
                    Status = "P";
                } else {
                    Status = "L";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncFetch().execute();

            }
        });

        btnSubmitATM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    List<ATMIDSpinner> stList = ((AdapterLoadingPending) mAdapter).getEmployeeList();
                    mgetData = new ATMgetData();
                    gpaDouble = 0;
                    getATMID = "";
                    for (int i = 0; i < stList.size(); i++) {
                        ATMIDSpinner employee = stList.get(i);
                        if (employee.isSelected() == true) {
                            // secemp = secemp + "\n" + employee.getMyModelObject5().toString();
                            gpaDouble = gpaDouble + Double.parseDouble(employee.getMyModelObject5());
                            getATMID = getATMID + employee.getMyModelObject1().toString();
                        }
                    }
                    double d = gpaDouble;
                    System.out.println(d);
                    Double b = new Double(gpaDouble);
                    int i = b.intValue();

                    String total2 = String.valueOf(i);
                    String str = total2 != null ? total2 : "0";

                    if (str.equals("0")) {
                        Toast.makeText(getActivity(), "please select atleast one", Toast.LENGTH_SHORT).show();
                    } else {


                        mgetData.setVkId(connection.getVkid());
                        mgetData.setAtmId(SpnSelected);
                        mgetData.setAtmRoCashReceiptId(getATMID);

                        // AsyncGetDenomination myRequest = new AsyncGetDenomination();
                        // myRequest.execute();

                        MyATMAmountFragment fragment = new MyATMAmountFragment(viewPager);
                        Bundle bundle = new Bundle();
                        bundle.putString("username", str);
                        fragment.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frgATMamount, fragment).commit();
                        viewPager.setCurrentItem(1);
                        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        return rootView;


    }


    private class AsyncGetATMList extends AsyncTask<Void, Void, Void> {

        //this is the method to query
        ArrayList<String> schoolNames = new ArrayList<String>();

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {


                String vkid = connection.getVkid();
//
                diplayServerResopnse = WebService.getATMList(vkid);

                Log.d("TAG", "WebSer...ceResponse: " + diplayServerResopnse);

            } catch (Exception e) {
                e.printStackTrace();
                AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            progress.dismiss();
            try {

                String strJson = diplayServerResopnse;
                StringTokenizer tokens = new StringTokenizer(strJson, "|");


                if (diplayServerResopnse.equals("OKAY|empty")) {

                    emptyView.setVisibility(View.VISIBLE);
                } else {

                    String first = tokens.nextToken();
                    String second = tokens.nextToken();

                    String jsonStr = second.toString();

                    JSONArray arr = new JSONArray(jsonStr);
                    String[] stringArray = null;
                    if (arr != null) {
                        int length = arr.length();
                        stringArray = new String[length];
                        for (int i = 0; i < length; i++) {
                            stringArray[i] = arr.optString(i);
                        }
                    }
                    String[] aa = stringArray;

                    SpnGetATMList.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, aa));


                    SpnGetATMList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1,
                                                   int arg2, long arg3) {
                            SpnSelected = SpnGetATMList.getSelectedItem().toString();


                            Toast.makeText(getActivity(), SpnSelected, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub

                        }
                    });
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }

    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(getActivity());


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String vkid = connection.getVkid();
            String atmId = SpnSelected;
            String stat = Status;

            Log.e(stat + "selected ", atmId);
            diplayServerResopnse_RO_CASH_LODING = WebService.getAtmRoCashLoadingRecipts(vkid, atmId, stat);
            Log.d("TAG", diplayServerResopnse_RO_CASH_LODING);

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                //this method will be running on UI thread

                pdLoading.dismiss();
                StringTokenizer tokens = new StringTokenizer(diplayServerResopnse_RO_CASH_LODING, "|");
                String first = tokens.nextToken();
                String second = tokens.nextToken();


                if (second.equals("empty")) {
                    chk_select_all.setVisibility(View.INVISIBLE);
                    emptyView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                    btnSubmitATM.setVisibility(View.INVISIBLE);


                } else {


                    item_list = new ArrayList<>();
                    chk_select_all.setVisibility(View.VISIBLE);
                    btnSubmitATM.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.INVISIBLE);

                    JSONArray jArray = new JSONArray(second);

                    if (Status.equals("P")) {


                        LinearLayout tabStrip = ((LinearLayout) tabLayout.getChildAt(0));
                        for (int i = 0; i < tabStrip.getChildCount(); i++) {
                            tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    return false;
                                }
                            });
                        }
                        viewPager.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                return false;
                            }
                        });

                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject json_data = jArray.getJSONObject(i);
                            ATMIDSpinner fishData = new ATMIDSpinner();
                            fishData.setMyModelObject1(json_data.getString("myModelObject1"));
                            fishData.setMyModelObject2(json_data.getString("myModelObject2"));
                            fishData.setMyModelObject3(json_data.getString("myModelObject3"));
                            fishData.setMyModelObject4(json_data.getString("myModelObject1"));
                            fishData.setMyModelObject5(json_data.getString("myModelObject4"));
                            fishData.setMyModelObject6(json_data.getString("myModelObject5"));


                            //  item_list.add(fishData);
                            ATMIDSpinner st = new ATMIDSpinner(fishData.getMyModelObject1(), fishData.getMyModelObject2(), fishData.getMyModelObject3(),
                                    fishData.getMyModelObject4(), fishData.getMyModelObject5(), fishData.getMyModelObject6(), false);

                            item_list.add(st);

                            mAdapter = new AdapterLoadingPending(getActivity(), item_list, chk_select_all);
                            recyclerView.setAdapter(mAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        }

                    } else {

                        chk_select_all.setVisibility(View.INVISIBLE);
                        btnSubmitATM.setVisibility(View.INVISIBLE);

                        LinearLayout tabStrip = ((LinearLayout) tabLayout.getChildAt(0));
                        for (int i = 0; i < tabStrip.getChildCount(); i++) {
                            tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    return true;
                                }
                            });
                        }
                        viewPager.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                return true;
                            }
                        });

                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject json_data = jArray.getJSONObject(i);
                            ATMIDSpinner fishData = new ATMIDSpinner();
                            fishData.setMyModelObject1(json_data.getString("myModelObject1"));
                            fishData.setMyModelObject2(json_data.getString("myModelObject2"));
                            fishData.setMyModelObject3(json_data.getString("myModelObject3"));
                            fishData.setMyModelObject4(json_data.getString("myModelObject4"));
                            fishData.setMyModelObject5(json_data.getString("myModelObject5"));
                            fishData.setMyModelObject6(json_data.getString("myModelObject6"));


                            //  item_list.add(fishData);
                            ATMIDSpinner st = new ATMIDSpinner(fishData.getMyModelObject1(), fishData.getMyModelObject2(), fishData.getMyModelObject3(),
                                    fishData.getMyModelObject4(), fishData.getMyModelObject5(), fishData.getMyModelObject6(), false);

                            item_list.add(st);

                            mAdapterCashLoad = new AdapterCashLoading(getActivity(), item_list);
                            recyclerView.setAdapter(mAdapterCashLoad);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        }

                    }
                }

            } catch (JSONException e) {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }


}
