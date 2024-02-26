package in.vakrangee.core.commongui.status;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import in.vakrangee.core.R;
import in.vakrangee.core.commongui.imagegallery.RecyclerViewClickListener;
import in.vakrangee.core.commongui.report.ReportDataDto;
import in.vakrangee.core.commongui.report.ReportHandlerListener;
import in.vakrangee.core.commongui.report.ReportRepository;
import in.vakrangee.core.utils.DeprecateHandler;

public class NameStatusHandlerFragment extends Fragment {

    private static final String TAG = "NameStatusHandlerFragment";
    private DeprecateHandler deprecateHandler;
    private Context context;
    private LinearLayout layoutProgress;
    private ScrollView scrollViewReport;
    private TextView txtReportName, txtPleaseWait, txtreportTrendMsg;
    private TextView btnRetry;
    private RecyclerView recyclerViewNameStatus;
    private ReportHandlerListener reportHandlerListener;
    private ReportRepository reportRepo;
    private GetReport getReport;
    private NameStatusAdapter nameStatusAdapter;
    private ReportDataDto dataDto;

    public NameStatusHandlerFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_name_status, container, false);

        //Initialize Data
        this.context = getContext();
        deprecateHandler = new DeprecateHandler(context);
        reportRepo = new ReportRepository(context);

        layoutProgress = (LinearLayout) rootView.findViewById(R.id.layoutProgress);
        txtPleaseWait = (TextView) rootView.findViewById(R.id.include_txtPleaseWait);
        btnRetry = (TextView) rootView.findViewById(R.id.include_btnRetry);
        txtReportName = (TextView) rootView.findViewById(R.id.txtReportName);
        scrollViewReport = (ScrollView) rootView.findViewById(R.id.scrollViewReport);
        recyclerViewNameStatus = rootView.findViewById(R.id.recyclerViewNameStatus);
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

    public void setNameStatusAdapter(final List<NameStatusDto> nameStatusList) {
        nameStatusAdapter = new NameStatusAdapter(context, nameStatusList, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (reportHandlerListener != null) {
                    if (dataDto != null) {
                        reportHandlerListener.onRowClick(dataDto.reportDataList.get(position));
                    }
                }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerViewNameStatus.setLayoutManager(layoutManager);
        recyclerViewNameStatus.setItemAnimator(new DefaultItemAnimator());
        recyclerViewNameStatus.setAdapter(nameStatusAdapter);

    }

    //Class to get the Selected report data from server and bind to the view for displaying
    class GetReport extends AsyncTask<String, Integer, String> {

        private String reportName;
        private ReportDataDto reportDataDto;
        private List<NameStatusDto> nameStatusList;
        private String res;

        public GetReport(String reportName, String response) {
            this.reportName = reportName;
            this.res = response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            layoutProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            reportDataDto = reportRepo.getReportDataList(reportName, res);
            nameStatusList = reportRepo.getNameStatusList(reportDataDto);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPreExecute();

            layoutProgress.setVisibility(View.GONE);
            if (nameStatusList == null) {
                txtreportTrendMsg.setVisibility(View.VISIBLE);

            } else {
                dataDto = reportDataDto;
                txtreportTrendMsg.setVisibility(View.GONE);
                setNameStatusAdapter(nameStatusList);
            }
        }
    }

    //region Refresh Name Status Handler Data
    public void refresh(Bundle args) {
        if (args != null) {
            //Get Data from Bundle
            String res = "", jsonKey = "", reportHeader = "";

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

            res = args.getString("Response", "");
            jsonKey = args.getString("ReportJSONKey");
            reportHeader = args.getString("ReportHeader");

            txtReportName.setText(reportHeader);
            if (TextUtils.isEmpty(reportHeader))
                txtReportName.setVisibility(View.GONE);
            else
                txtReportName.setVisibility(View.VISIBLE);

            if (res.length() > 0) {
                txtreportTrendMsg.setVisibility(View.GONE);

                getReport = new GetReport(jsonKey, res);
                getReport.execute("");

            } else {
                layoutProgress.setVisibility(View.GONE);
                txtreportTrendMsg.setVisibility(View.VISIBLE);
            }
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

        if (getReport != null && !getReport.isCancelled()) {
            getReport.cancel(true);
        }
    }
}
