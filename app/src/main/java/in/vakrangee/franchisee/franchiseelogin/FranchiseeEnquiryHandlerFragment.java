package in.vakrangee.franchisee.franchiseelogin;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.commongui.imagegallery.RecyclerViewClickListener;
import in.vakrangee.supercore.franchisee.commongui.report.ReportHandlerListener;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;

public class FranchiseeEnquiryHandlerFragment extends Fragment {

    private static final String TAG = "FranchiseeEnquiryHandlerFragment";
    private DeprecateHandler deprecateHandler;
    private Context context;
    private LinearLayout layoutProgress;
    private ScrollView scrollViewReport;
    private TextView txtReportName, txtPleaseWait, txtreportTrendMsg;
    private TextView btnRetry;
    private RecyclerView recyclerViewEnquiryDetails;
    private ReportHandlerListener reportHandlerListener;
    private FranchiseeAuthenticationRepository franchiseeAuthRepo;
    private GetFranchiseeEnquiryDetails getFranchiseeEnquiryDetails;
    private FranchiseeEnquiryDetailsAdapter franchiseeEnquiryDetailsAdapter;

    public FranchiseeEnquiryHandlerFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_enquiry_details, container, false);

        //Initialize Data
        this.context = getContext();
        deprecateHandler = new DeprecateHandler(context);
        franchiseeAuthRepo = new FranchiseeAuthenticationRepository(context);

        layoutProgress = (LinearLayout) rootView.findViewById(R.id.layoutProgress);
        txtPleaseWait = (TextView) rootView.findViewById(R.id.include_txtPleaseWait);
        btnRetry = (TextView) rootView.findViewById(R.id.include_btnRetry);
        txtReportName = (TextView) rootView.findViewById(R.id.txtReportName);
        scrollViewReport = (ScrollView) rootView.findViewById(R.id.scrollViewReport);
        recyclerViewEnquiryDetails = rootView.findViewById(R.id.recyclerViewEnquiryDetails);
        txtreportTrendMsg = (TextView) rootView.findViewById(R.id.txtreportTrendMsg);

        return rootView;
    }

    /**
     * Visible Please wait and hide
     */
    public void init(Bundle args) {
        if (args != null) {

            //Get Report Header from Bundle
            String reportHeader = args.getString("ReportHeader");
            txtReportName.setText(reportHeader);
            layoutProgress.setVisibility(View.VISIBLE);
            txtreportTrendMsg.setVisibility(View.GONE);
            if (TextUtils.isEmpty(reportHeader))
                txtReportName.setVisibility(View.GONE);
            else
                txtReportName.setVisibility(View.VISIBLE);
        }
    }

    public void setFranchiseeAdapter(final List<FranchiseeEnquiryDto> enquiryList) {
        franchiseeEnquiryDetailsAdapter = new FranchiseeEnquiryDetailsAdapter(context, enquiryList, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (reportHandlerListener != null) {
                    reportHandlerListener.onRowClick(enquiryList.get(position));
                }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerViewEnquiryDetails.setLayoutManager(layoutManager);
        recyclerViewEnquiryDetails.setItemAnimator(new DefaultItemAnimator());
        recyclerViewEnquiryDetails.setAdapter(franchiseeEnquiryDetailsAdapter);

    }

    class GetFranchiseeEnquiryDetails extends AsyncTask<String, Integer, String> {

        private String mobEmailId;
        private List<FranchiseeEnquiryDto> enquiryList;

        public GetFranchiseeEnquiryDetails(String mobEmailId) {
            this.mobEmailId = mobEmailId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            layoutProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            enquiryList = franchiseeAuthRepo.getGetAllFranchiseeEnquiryList(mobEmailId);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPreExecute();

            layoutProgress.setVisibility(View.GONE);
            if (enquiryList.size() == 0) {
                txtreportTrendMsg.setVisibility(View.VISIBLE);

            } else {
                txtreportTrendMsg.setVisibility(View.GONE);
                setFranchiseeAdapter(enquiryList);
            }
        }
    }

    //region Refresh Name Status Handler Data
    public void refresh(Bundle args) {
        if (args != null) {
            //Get Data from Bundle
            String mobEmailId = "", reportHeader = "";

            // Check Error Response - Added By : Dpk - 01st March 2018
            boolean isError = args.getBoolean("IsError", false);
            if (isError) {
                String errMsg = args.getString("ErrMsg", "UNKNOWN");
                layoutProgress.setVisibility(View.GONE);
                txtreportTrendMsg.setVisibility(View.VISIBLE);
                txtreportTrendMsg.setText(errMsg);

                return;
            }
            // -- END

            mobEmailId = args.getString("MOB_EMAIL");
            reportHeader = args.getString("ReportHeader");

            txtReportName.setText(reportHeader);
            if (TextUtils.isEmpty(reportHeader))
                txtReportName.setVisibility(View.GONE);
            else
                txtReportName.setVisibility(View.VISIBLE);

            getFranchiseeEnquiryDetails = new GetFranchiseeEnquiryDetails(mobEmailId);
            getFranchiseeEnquiryDetails.execute("");

        }
    }
    //endregion

    //region setReportHandlerListener
    public void setOnRowClickListener(ReportHandlerListener listener) {
        if (listener != null) {
            reportHandlerListener = listener;
        }
    }
    //endregion

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (getFranchiseeEnquiryDetails != null && !getFranchiseeEnquiryDetails.isCancelled()) {
            getFranchiseeEnquiryDetails.cancel(true);
        }
    }
}
