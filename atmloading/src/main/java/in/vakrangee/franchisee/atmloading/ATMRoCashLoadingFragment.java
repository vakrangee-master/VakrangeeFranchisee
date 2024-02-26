package in.vakrangee.franchisee.atmloading;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import in.vakrangee.supercore.franchisee.tracking_hardware.CustomTrackingPDFdialog;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class ATMRoCashLoadingFragment extends Fragment {
    private View view;
    private Context context;
    private List<ATMRoLoadingPendingDto> loadingPendingDtoList;
    private List<ATMRoCashLoadedDto> cashLoadedDtoList;

    private ATMRoCashLoadigPendingAdapter atmRoCashLoadigPendingAdapter;
    private ATMRoCashLoadigCashLoadedAdapter roCashLoadigCashLoadedAdapter;
    private CustomTrackingPDFdialog customTrackingPDFdialog;
    private TextView empty_view;
    private RecyclerView recycler_view_ro_details;
    private Button btnSelectItem;

    public ATMRoCashLoadingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_atmro_cash_loading, container, false);
        this.context = getContext();
        bindViewId(view);
        ButterKnife.bind(this, view);

        btnSelectItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check box item selection --  loading pending item
                listOfItemSelected();
            }
        });
        return view;
    }

    private void bindViewId(View view) {
        empty_view = view.findViewById(R.id.empty_view);
        recycler_view_ro_details = view.findViewById(R.id.recycler_view_ro_details);
        btnSelectItem = view.findViewById(R.id.btnSelectItem);
    }

    private void listOfItemSelected() {
        int totalamount = 0;
        String ackNo = null;
        StringBuilder sb = new StringBuilder();

        List<ATMRoLoadingPendingDto> atmRoList = atmRoCashLoadigPendingAdapter.getATMRoList();
        for (ATMRoLoadingPendingDto cashLoadingDto : atmRoList) {
            if (cashLoadingDto.getSelected()) {
                totalamount += Integer.parseInt(cashLoadingDto.getAmount());
                sb.append(cashLoadingDto.getAtmRoCashReceiptNo()).append("|");
            }
        }

        ackNo = sb.toString();
        ackNo = ackNo.substring(0, ackNo.length() - 1);
                /*String[] parts = itemList.split("\\|");
                System.out.println(parts);
                System.out.println("ACK No" + itemList);
                result.append("\n" + totalamount);*/


        if (totalamount == 0) {
            Toast.makeText(context, "Please select atleast one item", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(context, ATMActivity.class);
            intent.putExtra("ackNo", ackNo);
            intent.putExtra("totalAmount", totalamount);
            intent.putExtra("statusType", "P");
            startActivity(intent);
        }
    }

    //spinner id -L cash loading P- loading pending
    public void reloadData(String result, String spinnerSelectedId) {
        try {
            if (TextUtils.isEmpty(result)) {
                AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                return;
            }
            //Response
            if (result.startsWith("ERROR|")) {
                result = result.replace("ERROR|", "");
                if (TextUtils.isEmpty(result)) {
                    AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                } else {
                    AlertDialogBoxInfo.alertDialogShow(context, result);
                }
            } else if (result.startsWith("OKAY|")) {
                result = result.replace("OKAY|", "");
                if (spinnerSelectedId.equalsIgnoreCase("L")) {
                    dataSetCashLoading(result);
                } else {
                    dataSet(result);
                }

            } else {
                AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
        }
    }

    //set cash loaded
    private void dataSetCashLoading(String result) {
        Gson gson = new GsonBuilder().create();
        cashLoadedDtoList = gson.fromJson(result, new TypeToken<ArrayList<ATMRoCashLoadedDto>>() {
        }.getType());

        if (cashLoadedDtoList == null && cashLoadedDtoList.size() == 0) {
            empty_view.setVisibility(View.VISIBLE);
            recycler_view_ro_details.setVisibility(View.GONE);
            btnSelectItem.setVisibility(View.GONE);
        } else {
            empty_view.setVisibility(View.GONE);
            recycler_view_ro_details.setVisibility(View.VISIBLE);
            btnSelectItem.setVisibility(View.GONE);
            roCashLoadigCashLoadedAdapter = new ATMRoCashLoadigCashLoadedAdapter(context, cashLoadedDtoList, new ATMRoCashLoadigCashLoadedAdapter.IfcATMRoCashPosition() {
                @Override
                public void itemPosition(int position, ATMRoCashLoadedDto atmRoCashLoadingDto) {
                    Intent intent = new Intent(context, ATMActivity.class);
                    intent.putExtra("statusType", "L");
                    intent.putExtra("cashLoadingDate", atmRoCashLoadingDto.getLodingDate());
                    intent.putExtra("cashLoadingId", atmRoCashLoadingDto.getAtmRoCashLoadingId());
                    startActivity(intent);
                }

                @Override
                public void pdfViewClick(int position, ATMRoCashLoadedDto atmRoCashLoadedDto) {
                    String PDFLinkURL = Constants.DownloadPDFfileURL + atmRoCashLoadedDto.getAtmRoCashReceiptImage();
                    customTrackingPDFdialog = new CustomTrackingPDFdialog(context, PDFLinkURL);
                    customTrackingPDFdialog.show();
                    customTrackingPDFdialog.setDialogTitle("ATM RO Cash Loading");
                    customTrackingPDFdialog.setCancelable(false);

                }

            });

            LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
            recycler_view_ro_details.setLayoutManager(layoutManager);
            recycler_view_ro_details.setItemAnimator(new DefaultItemAnimator());
            recycler_view_ro_details.setAdapter(roCashLoadigCashLoadedAdapter);
        }

    }

    //set loading pending
    private void dataSet(String result) {
        Gson gson = new GsonBuilder().create();
        loadingPendingDtoList = gson.fromJson(result, new TypeToken<ArrayList<ATMRoLoadingPendingDto>>() {
        }.getType());

        if (loadingPendingDtoList == null && loadingPendingDtoList.size() == 0) {
            empty_view.setVisibility(View.VISIBLE);
            recycler_view_ro_details.setVisibility(View.GONE);
            btnSelectItem.setVisibility(View.GONE);
        } else {
            btnSelectItem.setVisibility(View.VISIBLE);
            empty_view.setVisibility(View.GONE);
            recycler_view_ro_details.setVisibility(View.VISIBLE);
            atmRoCashLoadigPendingAdapter = new ATMRoCashLoadigPendingAdapter(context, loadingPendingDtoList, new ATMRoCashLoadigPendingAdapter.IfcATMRoCashPosition() {
                @Override
                public void itemPosition(int position, ATMRoLoadingPendingDto atmRoLoadingPendingDto) {

                }

            });

            LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
            recycler_view_ro_details.setLayoutManager(layoutManager);
            recycler_view_ro_details.setItemAnimator(new DefaultItemAnimator());
            recycler_view_ro_details.setAdapter(atmRoCashLoadigPendingAdapter);

        }

    }


}
