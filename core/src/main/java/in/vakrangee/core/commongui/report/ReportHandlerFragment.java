package in.vakrangee.core.commongui.report;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import in.vakrangee.core.R;
import in.vakrangee.core.utils.DeprecateHandler;

/**
 * Created by Vasundhara on 6/26/2018.
 */

public class ReportHandlerFragment extends Fragment {

    private static final String TAG = "ReportHandlerFragment";
    private DeprecateHandler deprecateHandler;
    private Context context;
    private LinearLayout layoutProgress;
    private HorizontalScrollView horizontalScrollView;
    private TableLayout tableLayoutPerformanceHeader, tableLayoutReportsData;
    private ScrollView scrollViewReport;
    private ReportRepository reportRepo;
    private String[] fixedColumns = {"SrNo", "Emp_Code", "Salutation", "Gender", "City", "State", "Emp Code", "Sr No"};
    private int[] maxColumnWidth;
    private int width;
    private TextView txtReportName, txtPleaseWait, txtreportTrendMsg;
    private TextView btnRetry;
    private ReportHandlerListener reportHandlerListener;
    private DateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private GetReport getReport;

    public ReportHandlerFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_report, container, false);

        //Initialize Data
        this.context = getContext();
        deprecateHandler = new DeprecateHandler(context);
        reportRepo = new ReportRepository(context);

        layoutProgress = (LinearLayout) rootView.findViewById(R.id.layoutProgress);
        txtPleaseWait = (TextView) rootView.findViewById(R.id.include_txtPleaseWait);
        btnRetry = (TextView) rootView.findViewById(R.id.include_btnRetry);
        txtReportName = (TextView) rootView.findViewById(R.id.txtReportName);
        horizontalScrollView = (HorizontalScrollView) rootView.findViewById(R.id.horizontalScrollView);
        tableLayoutPerformanceHeader = (TableLayout) rootView.findViewById(R.id.tableLayoutPerformanceHeader);
        scrollViewReport = (ScrollView) rootView.findViewById(R.id.scrollViewReport);
        tableLayoutReportsData = (TableLayout) rootView.findViewById(R.id.tableLayoutReportsData);
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
            horizontalScrollView.setVisibility(View.GONE);
            if (TextUtils.isEmpty(reportHeader))
                txtReportName.setVisibility(View.GONE);
            else
                txtReportName.setVisibility(View.VISIBLE);
        }
    }

    //region Refresh Report Handler Data
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
                //new GetReport(jsonKey, res).execute("");
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

    public void displayDynamicReport(ReportDataDto reportDataDto) {
        if (reportDataDto != null) {
            //logger.ERR(TAG, "Testing: PerformanceTrend: Start Time: " + datetimeFormat.format(new Date()));
            horizontalScrollView.setVisibility(View.VISIBLE);
            tableLayoutReportsData.removeAllViews();
            tableLayoutPerformanceHeader.removeAllViews();
            horizontalScrollView.getParent().requestDisallowInterceptTouchEvent(true);

            List<ReportHeader> reportHeaderList = reportDataDto.reportHeaderList;
            int headerCount = reportHeaderList.size();
            int temWidth = getMaxRowWidth(reportDataDto);
            int dataCount = reportDataDto.reportDataList.size();
            if (headerCount > 0 && dataCount > 0) {

                //Creating the Header
                String sortedColName = "";
                for (int i = 1; i <= headerCount; i++) {
                    String ColName = "";
                    for (ReportHeader headerDto : reportHeaderList) {
                        if (headerDto.getColumnOrder() == i) {
                            ColName = headerDto.getHeader();
                            break;
                        }
                    }

                    if (i == 1) {
                        sortedColName = ColName;
                    } else {
                        sortedColName += "," + ColName;
                    }
                }

                //Adding data to the table
                final List<Map<String, String>> dataList = reportDataDto.reportDataList;
                for (int j = 0; j < dataList.size(); j++) {
                    TableRow tableRow = new TableRow(context);
                    int alterCount = j % 2;
                    //Get the values of the data
                    Map<String, String> map = dataList.get(j);
                    String colData[] = sortedColName.split(",");
                    for (int i = 0; i < colData.length; i++) {
                        ReportHeader headerDto = reportDataDto.reportHeaderList.get(i);
                        String key = colData[i];
                        String colValue = "";
                        Set keys = map.keySet();
                        for (Iterator itr = keys.iterator(); itr.hasNext(); ) {
                            String mapkey = (String) itr.next();
                            if (key.equals(mapkey)) {
                                colValue = (String) map.get(key);
                                break;
                            }
                        }
                        TextView textView = new TextView(context);
                        textView.setTextColor(deprecateHandler.getColor(R.color.iGrey));
                        if (i == 0) {
                            textView.setTextColor(deprecateHandler.getColor(R.color.white));
                            textView.setBackgroundColor(deprecateHandler.getColor(R.color.colorPrimary));
                            textView.setGravity(Gravity.LEFT);
                        } else {
                            textView.setTextColor(deprecateHandler.getColor(R.color.iGrey));
                            textView.setGravity(Gravity.CENTER);
                            if (alterCount == 0) {
                                textView.setBackgroundColor(deprecateHandler.getColor(R.color.SIL));
                            } else {
                                textView.setBackgroundColor(deprecateHandler.getColor(R.color.colorGreyL2));
                            }
                        }
                        textView.setTextSize(16);

                        textView.setText("	" + colValue);
                        textView.setEllipsize(TextUtils.TruncateAt.END);
                        textView.setSingleLine();
                        textView.setTypeface(Typeface.SANS_SERIF);
                        textView.setPadding(5, 10, 5, 10);
                        textView.measure(0, 0);       //must call measure!
                        textView.getMeasuredHeight(); //get width
                        width = textView.getMeasuredWidth();
                        int fixedWidth = getFixedColumsWidth(key);
                        tableRow.setBackgroundResource(R.drawable.rowbackground1);
                        if (headerDto.isIsDefaultWidth()) {
                            tableRow.addView(textView, new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
                        } else {
                            if (fixedWidth > 0) {
                                tableRow.addView(textView, new TableRow.LayoutParams(fixedWidth, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
                            } else {
                                tableRow.addView(textView, new TableRow.LayoutParams(temWidth, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
                            }
                        }

                        //Set Column Width
                        String colWidth = (headerDto.getColWidth() == null) ? "" : headerDto.getColWidth();
                        if (colWidth.trim().length() == 0) {
                            colWidth = String.valueOf(width);
                        } else {
                            colWidth += "," + width;
                        }
                        headerDto.setColWidth(colWidth);
                        reportDataDto.reportHeaderList.set(i, headerDto);
                    }

                    // Add Listener to Table Row
                    tableRow.setTag(dataList.get(j));    // Set Tag to each Row Data
                    tableRow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (reportHandlerListener != null) {
                                reportHandlerListener.onRowClick(v.getTag());
                            }
                        }
                    });

                    tableLayoutReportsData.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                }
                tableLayoutReportsData.invalidate();

                //Creating the Header
                TableRow tableRowHeader = new TableRow(context);
                for (int i = 1; i <= headerCount; i++) {
                    boolean IsSetDefaultWidth = false;
                    String ColName = "";
                    for (ReportHeader headerDto : reportHeaderList) {
                        if (headerDto.getColumnOrder() == i) {
                            if (headerDto.getHeaderValue() == null) {
                                ColName = headerDto.getHeader();
                                IsSetDefaultWidth = headerDto.isIsDefaultWidth();
                                if (headerDto.getColWidth() != null) {
                                    maxColumnWidth = getIntConverted(headerDto.getColWidth().split(","));
                                }
                            } else {
                                ColName = headerDto.getHeaderValue();
                                IsSetDefaultWidth = headerDto.isIsDefaultWidth();
                                if (headerDto.getColWidth() != null) {
                                    maxColumnWidth = getIntConverted(headerDto.getColWidth().split(","));
                                }
                            }
                            break;
                        }
                    }

                    TextView textViewTitle = new TextView(context);
                    textViewTitle.setBackgroundColor(deprecateHandler.getColor(R.color.light_sky_blue));
                    textViewTitle.setGravity(Gravity.CENTER);
                    textViewTitle.setTextColor(Color.BLACK);
                    textViewTitle.setTypeface(Typeface.SANS_SERIF);
                    textViewTitle.setText(ColName);
                    textViewTitle.setTextSize(16);
                    textViewTitle.setEllipsize(TextUtils.TruncateAt.END);
                    textViewTitle.setHorizontallyScrolling(false);
                    textViewTitle.setSingleLine();
                    textViewTitle.setPadding(5, 10, 5, 10);
                    textViewTitle.measure(0, 0);       //must call measure!
                    textViewTitle.getMeasuredHeight();//get width
                    int fixedWidth = getFixedColumsWidth(ColName);

                    if (IsSetDefaultWidth) {
                        int maxWidth = getMaxValue(maxColumnWidth);
                        tableRowHeader.addView(textViewTitle, new TableRow.LayoutParams(maxWidth, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
                    } else {
                        if (fixedWidth > 0) {
                            tableRowHeader.addView(textViewTitle, new TableRow.LayoutParams(fixedWidth, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
                        } else {
                            tableRowHeader.addView(textViewTitle, new TableRow.LayoutParams(temWidth, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
                        }
                    }
                }

                //Adding the Header into the Table Layout
                tableLayoutPerformanceHeader.addView(tableRowHeader, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            } else {
                horizontalScrollView.setVisibility(View.INVISIBLE);
                //txtViewReportMessage.setVisibility(View.VISIBLE);
                //txtViewReportMessage.setText("No data available..");
                //showOkMessage("No data available..");
            }
        } else {
            horizontalScrollView.setVisibility(View.INVISIBLE);
        }
        //logger.ERR(TAG, "Testing: PerformanceTrend: End Time: " + datetimeFormat.format(new Date()));
    }

    //Class to get the Selected report data from server and bind to the view for displaying
    class GetReport extends AsyncTask<String, Integer, String> {

        private String reportName;
        private ReportDataDto reportDataDto;
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
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPreExecute();

            layoutProgress.setVisibility(View.GONE);
            if (reportDataDto == null) {
                txtreportTrendMsg.setVisibility(View.VISIBLE);

            } else {
                txtreportTrendMsg.setVisibility(View.GONE);
                displayDynamicReport(reportDataDto);
            }
        }
    }

    // getting the maximum value
    public static int getMaxValue(int[] array) {

        int maxValue = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > maxValue) {
                maxValue = array[i];
            }
        }
        return maxValue;
    }

    public int getFixedColumsWidth(String colValue) {
        int fixedWidth = 0;
        for (int i = 0; i < fixedColumns.length; i++) {
            if (colValue.equalsIgnoreCase(fixedColumns[i])) {

                fixedWidth = 60;
                break;
            }
        }
        return fixedWidth;
    }

    //Method to get Max from first row of data
    public int getMaxRowWidth(ReportDataDto reportDataDto) {
        int width = 0;

        if (reportDataDto != null) {

            if (reportDataDto.reportDataList != null && reportDataDto.reportDataList.size() > 0) {
                int count = reportDataDto.reportDataList.get(0).size() + reportDataDto.reportHeaderList.size();
                int row[] = new int[count];
                int i = 0;

                //Header width
                for (ReportHeader header : reportDataDto.reportHeaderList) {
                    String headervalue = header.getHeaderValue();

                    TextView textView = new TextView(context);

                    textView.setBackgroundResource(R.drawable.rowbackground1);
                    textView.setTextColor(Color.BLACK);
                    textView.setTextSize(16);
                    textView.setTypeface(null, Typeface.BOLD);
                    textView.setGravity(Gravity.CENTER);
                    textView.setPadding(10, 0, 10, 0);
                    textView.setText("	" + headervalue);
                    textView.measure(0, 0);         //must call measure!
                    textView.getMeasuredHeight();     //get width
                    width = textView.getMeasuredWidth();

                    row[i] = textView.getMeasuredWidth();
                    i++;
                }

                //Data width
                for (Map.Entry<String, String> entry : reportDataDto.reportDataList.get(0).entrySet()) {
                    String value = entry.getValue();
                    String key = entry.getKey();
                    boolean IsHeaderExist = reportRepo.IsHeaderExist(reportDataDto.reportHeaderList, key);
                    if (IsHeaderExist) {
                        TextView textView = new TextView(context);

                        textView.setBackgroundResource(R.drawable.rowbackground1);
                        textView.setTextColor(Color.BLACK);
                        textView.setTextSize(16);
                        textView.setTypeface(null, Typeface.BOLD);
                        textView.setGravity(Gravity.CENTER);
                        textView.setPadding(10, 0, 10, 0);
                        textView.setText("	" + value);
                        textView.measure(0, 0);         //must call measure!
                        textView.getMeasuredHeight();     //get width
                        width = textView.getMeasuredWidth();

                        row[i] = textView.getMeasuredWidth();
                        i++;
                    }
                }

                if (row.length > 0) {
                    width = getMaxValue(row);
                }
            }
        }
        return width;
    }

    public int[] getIntConverted(String[] items) {
        int[] results = new int[items.length];

        for (int i = 0; i < items.length; i++) {
            try {
                results[i] = Integer.parseInt(items[i]);
            } catch (NumberFormatException nfe) {
            }
        }
        return results;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (getReport != null && !getReport.isCancelled()) {
            getReport.cancel(true);
        }
    }
}
