package in.vakrangee.franchisee.sitelayout;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.vipulasri.timelineview.TimelineView;

import java.util.List;

import butterknife.ButterKnife;
import in.vakrangee.supercore.franchisee.model.FranchiseeTimeLineDetails;

public class StatusHistoryDialog extends Dialog implements View.OnClickListener {

    final String TAG = StatusHistoryDialog.class.getCanonicalName();

    private Button btnClose;
    private Context mContext;
    private List<FranchiseeTimeLineDetails> franchiseeTimeLineDetailsList;
    private RecyclerView mRecyclerView;
    private TimeLineAdapter mTimeLineAdapter;
    final Typeface font;

    public StatusHistoryDialog(@NonNull Context context, @NonNull List<FranchiseeTimeLineDetails> franchiseeTimeLineDetailsList) {
        super(context);
        this.mContext = context;
        this.franchiseeTimeLineDetailsList = franchiseeTimeLineDetailsList;
        font = Typeface.createFromAsset(mContext.getAssets(), "fontawesome-webfont.ttf");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        final Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fontawesome-webfont.ttf");
        setContentView(R.layout.dialog_status_history);

        btnClose = (Button) findViewById(R.id.btnClose);

        btnClose.setTypeface(font);
        btnClose.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + " Close "));
        btnClose.setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);

        mTimeLineAdapter = new TimeLineAdapter(franchiseeTimeLineDetailsList);
        mRecyclerView.setAdapter(mTimeLineAdapter);
        mRecyclerView.setScrollbarFadingEnabled(true);

        int width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (mContext.getResources().getDisplayMetrics().heightPixels * 0.90);
        getWindow().setLayout(width, height);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnClose) {
            dismiss();
        }
    }

    //region TimeLine ViewHolder and Adapter
    public class TimeLineViewHolder extends RecyclerView.ViewHolder {


        TextView mUserName;
        TextView mStatusDesc;
        TextView mDate;
        TextView mRemarks;
        TimelineView mTimelineView;
        TextView txtStatus;

        public TimeLineViewHolder(View itemView, int viewType) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mTimelineView.initLine(viewType);

            mUserName = itemView.findViewById(R.id.txt_fas_username);
            mStatusDesc = itemView.findViewById(R.id.txt_fas_status);
            mDate = itemView.findViewById(R.id.txt_fas_date);
            mRemarks = itemView.findViewById(R.id.txt_fas_remarks);
            mTimelineView = itemView.findViewById(R.id.time_marker);
            txtStatus = itemView.findViewById(R.id.txtStatus);


            mUserName.setTypeface(font);
            mStatusDesc.setTypeface(font);
            mDate.setTypeface(font);
            mRemarks.setTypeface(font);
            txtStatus.setTypeface(font);
        }
    }

    public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineViewHolder> {

        private List<FranchiseeTimeLineDetails> faStatusHistory;
        private Context mContext;
        private LayoutInflater mLayoutInflater;

        public TimeLineAdapter(List<FranchiseeTimeLineDetails> faStatusHistory) {
            this.faStatusHistory = faStatusHistory;
        }

        @Override
        public int getItemViewType(int position) {
            return TimelineView.getTimeLineViewType(position, getItemCount());
        }

        @Override
        public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            mContext = parent.getContext();
            mLayoutInflater = LayoutInflater.from(mContext);
            View view;

            view = mLayoutInflater.inflate(R.layout.item_sitevisit_status_timeline, parent, false);

            return new TimeLineViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(TimeLineViewHolder holder, int position) {

            FranchiseeTimeLineDetails timeLineModel = faStatusHistory.get(position);
            //holder.mTimelineView.setMarkerColor();
            if (timeLineModel.getColor() == 1) {     // Thumbs Up with Green Color
                //holder.mTimelineView.setMarkerColor(mContext.getResources().getColor(R.color.md_green_A700));
                holder.mTimelineView.setMarker(mContext.getResources().getDrawable(R.drawable.circle_success));
                holder.txtStatus.setBackgroundResource(R.drawable.circle_success);
                holder.txtStatus.setText(new SpannableStringBuilder(" " + new String(new char[]{0xf164}) + " "));
            } else if (timeLineModel.getColor() == 2) { // Thumbs Down with Orange Color
                //holder.mTimelineView.setMarkerColor(mContext.getResources().getColor(R.color.md_orange_A700));
                holder.mTimelineView.setMarker(mContext.getResources().getDrawable(R.drawable.circle_clear));
                holder.txtStatus.setBackgroundResource(R.drawable.circle_clear);
                holder.txtStatus.setText(new SpannableStringBuilder(" " + new String(new char[]{0xf165}) + " "));
            } else if (timeLineModel.getColor() == 3) {  // Reject (Cancel Icon)
                //holder.mTimelineView.setMarkerColor(mContext.getResources().getColor(R.color.md_red_A700));
                holder.mTimelineView.setMarker(mContext.getResources().getDrawable(R.drawable.circle_cancel));
                holder.txtStatus.setBackgroundResource(R.drawable.circle_cancel);
                holder.txtStatus.setText(new SpannableStringBuilder(" " + new String(new char[]{0xf00d}) + " "));

            } else {
                holder.mTimelineView.setMarkerColor(mContext.getResources().getColor(R.color.md_grey_600));
            }

            holder.mTimelineView.setMarkerSize(20);
            holder.mUserName.setText(timeLineModel.getUserName());
            holder.mStatusDesc.setText(new SpannableStringBuilder(new String(new char[]{0xf02c}) + "  " + timeLineModel.getNextgenSiteVisitDescription())); //\e041
            holder.mDate.setText(new SpannableStringBuilder(new String(new char[]{0xf017}) + "  " + timeLineModel.getDateTime()));      //\e023

            //TODO: Need to Format Remarks in case status is "Site Send Back For Correction"
            if (Integer.parseInt(timeLineModel.getNextgenSiteVisitStatus()) == NextGenViewPager.SITE_SEND_BACK_FOR_CORRECTION) {
                holder.mRemarks.setText(Html.fromHtml(formatRemarks(timeLineModel.getNextgenSiteVisitRemarks())));
            } else {
                holder.mRemarks.setText(Html.fromHtml(timeLineModel.getNextgenSiteVisitRemarks()));
            }

            holder.mRemarks.setMovementMethod(new ScrollingMovementMethod());
        }

        @Override
        public int getItemCount() {
            return (faStatusHistory != null ? faStatusHistory.size() : 0);
        }

    }
    //endregion

    /**
     * Format Comments
     *
     * @param remarks
     * @return
     */
    public String formatRemarks(@NonNull String remarks) {
        StringBuilder stringBuilder = new StringBuilder();

        // Format data
        try {
            String[] lines = remarks.split("\\r?\\n");  // "\\r?\\n"
            for (String line : lines) {
                String[] data = line.split(":");  // ":"      // Split Key and Value from line
                stringBuilder.append("<b>" + data[0].trim() + ":</b> &nbsp;&nbsp;" + data[1].trim() + "<br>");
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return remarks;
    }
}
