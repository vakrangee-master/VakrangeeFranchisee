package in.vakrangee.franchisee.payment_details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.commongui.animation.AnimationHanndler;
import in.vakrangee.supercore.franchisee.commongui.imagegallery.RecyclerViewClickListener;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;

public class PaymentDetailsFragment extends BaseFragment {

    private View view;
    private Context context;
    private RecyclerView recyclerViewPaymentDetails;
    private TextView txtNoDataMsg;
    private AsyncGetPaymentDetails asyncGetPaymentDetails = null;
    private List<PaymentDetailsDto> paymentDetailsList;
    private PaymentDetailsAdapter paymentDetailsAdapter;
    private LinearLayout layoutRaiseDispute;

    public PaymentDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag_payment_details, container, false);

        this.context = getContext();
        recyclerViewPaymentDetails = view.findViewById(R.id.recyclerViewPaymentDetails);
        layoutRaiseDispute = view.findViewById(R.id.layoutRaiseDispute);
        txtNoDataMsg = view.findViewById(R.id.txtNoDataMsg);

        layoutRaiseDispute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationHanndler.bubbleAnimation(context, v);

                Intent intent = new Intent(context, AllRaisedDisputeDetailsActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    public void reloadData() {
        asyncGetPaymentDetails = new AsyncGetPaymentDetails(context, new AsyncGetPaymentDetails.Callback() {
            @Override
            public void onResult(String result) {
                processResult(result);
            }
        });
        asyncGetPaymentDetails.execute("");
    }

    private void processResult(String result) {
        try {
            if (TextUtils.isEmpty(result)) {
                AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                return;
            }

            if (result.startsWith("ERROR")) {
                String msg = result.replace("ERROR|", "");
                msg = TextUtils.isEmpty(msg) ? "Something went wrong. Please try again later." : msg;
                AlertDialogBoxInfo.alertDialogShow(context, msg);
                return;
            }

            if (result.startsWith("OKAY")) {
                //Handle Response
                String data = result.replace("OKAY|", "");
                if (TextUtils.isEmpty(data))
                    AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                else {
                    refreshDetails(data);
                }
            } else {
                AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
        }
    }

    private void refreshDetails(String data) {
        //Reload Data
        if (TextUtils.isEmpty(data)) {
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray paymentDetailsJSON = jsonObject.getJSONArray("payment_details");

            if (paymentDetailsJSON == null || paymentDetailsJSON.length() == 0) {
                return;
            }

            Gson gson = new GsonBuilder().create();
            paymentDetailsList = gson.fromJson(paymentDetailsJSON.toString(), new TypeToken<ArrayList<PaymentDetailsDto>>() {
            }.getType());

            if (paymentDetailsList != null && paymentDetailsList.size() > 0) {
                txtNoDataMsg.setVisibility(View.GONE);
                recyclerViewPaymentDetails.setVisibility(View.VISIBLE);
                paymentDetailsAdapter = new PaymentDetailsAdapter(context, paymentDetailsList, new RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        //Do nothing
                    }
                });
                LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                recyclerViewPaymentDetails.setLayoutManager(layoutManager);
                recyclerViewPaymentDetails.setItemAnimator(new DefaultItemAnimator());
                recyclerViewPaymentDetails.setAdapter(paymentDetailsAdapter);

            } else {
                txtNoDataMsg.setVisibility(View.VISIBLE);
                recyclerViewPaymentDetails.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (asyncGetPaymentDetails != null && !asyncGetPaymentDetails.isCancelled()) {
            asyncGetPaymentDetails.cancel(true);
        }

    }
}
