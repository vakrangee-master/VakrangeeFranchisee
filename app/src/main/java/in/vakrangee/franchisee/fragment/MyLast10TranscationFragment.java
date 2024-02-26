package in.vakrangee.franchisee.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.adapter.AdapterRecyclerViewWalletStmt;
import in.vakrangee.supercore.franchisee.model.WalletData;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.webservice.WebService;

//

/**
 * Created by Nileshd on 6/17/2016.
 */
public class MyLast10TranscationFragment extends Fragment {
    View view;
    TelephonyManager telephonyManager;

    String TAG = "Response";
    String diplayServerResopnse;
    ProgressDialog progress;
    private RecyclerView recyclerView;
    private AdapterRecyclerViewWalletStmt mAdapter;
    private TextView emptyView;

    Connection connection;
    InternetConnection internetConnection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.last_ten_transcation, container, false);


        telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        emptyView = (TextView) view.findViewById(R.id.empty_view_lastten);

        internetConnection = new InternetConnection(getContext());
        if (internetConnection.isNetworkAvailable(getContext()) == false) {

            AlertDialogBoxInfo.alertDialogShow(getContext(), getResources().getString(R.string.internetCheck));

        } else {

            progress = new ProgressDialog(getActivity());
            progress.setTitle(R.string.accountStmt);
            progress.setMessage(getResources().getString(R.string.pleaseWait));
            progress.setCancelable(false);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();
            AsyncGetAccountStatement10 myRequest = new AsyncGetAccountStatement10();
            myRequest.execute();
        }


        return view;
    }


    private class AsyncGetAccountStatement10 extends AsyncTask<Void, Void, Void> {

        //this is the method to query

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {

                connection = new Connection(getActivity());
                String vkid = connection.getVkid();
                String tokenId = connection.getTokenId();

//

                String vkidd = EncryptionUtil.encryptString(vkid, getActivity());
                String TokenId = EncryptionUtil.encryptString(tokenId, getActivity());
                String deviceIdget = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
                String deviceid = EncryptionUtil.encryptString(deviceIdget, getActivity());

                String deviceIDAndroid = CommonUtils.getAndroidUniqueID(getActivity());
                String imei = EncryptionUtil.encryptString(deviceIDAndroid, getActivity());

                String simSerial = CommonUtils.getSimSerialNumber(getActivity());
                String simserialnumber = EncryptionUtil.encryptString(simSerial, getActivity());

                diplayServerResopnse = WebService.getAccountStatement10(vkidd, TokenId, imei, deviceid, simserialnumber);

                Log.d(TAG, "WebSer...ceResponse: " + diplayServerResopnse);

            } catch (Exception e) {
                e.printStackTrace();
                AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            progress.dismiss();
            try {

                String strJson = diplayServerResopnse;
                List<WalletData> data = new ArrayList<>();
                try {


                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonarray = new JSONArray(strJson);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);

                        WalletData constants = new WalletData();
                        constants.DATE = jsonobject.getString("myModelObject1");
                        constants.SERVICES = jsonobject.getString("myModelObject2");
                        constants.PARTICULAR = jsonobject.getString("myModelObject3");
                        constants.TRANACTION = jsonobject.getString("myModelObject4");
                        constants.BALANCECR = jsonobject.getString("myModelObject5");
                        constants.BALANCE = jsonobject.getString("myModelObject6");

                        String myModelObject7 = jsonobject.getString("myModelObject7");
                        String myModelObject8 = jsonobject.getString("myModelObject8");
                        String myModelObject9 = jsonobject.getString("myModelObject9");
                        String myModelObject10 = jsonobject.getString("myModelObject10");
                        data.add(constants);

                    }


                    recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view_last10tracation);
                    mAdapter = new AdapterRecyclerViewWalletStmt(getActivity(), data);
                    recyclerView.setAdapter(mAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    Log.e(" number of data", String.valueOf(data));
                    if (data.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);

                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        emptyView.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Error Json", e.getMessage());

                    Log.e(" AsyncGetAccountStatement10 background catch", e.getMessage());
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));

                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }

}
