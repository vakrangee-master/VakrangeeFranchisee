package in.vakrangee.franchisee.networktesting.simstrength;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import in.vakrangee.franchisee.networktesting.R;
import in.vakrangee.supercore.franchisee.commongui.imagegallery.RecyclerViewClickListener;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;

@SuppressLint("NewApi")
public class SIMListAdapter extends RecyclerView.Adapter<SIMListAdapter.MyViewHolder> {

    public List<SIMDetailsDto> simDetailsList;
    private Context context;
    private RecyclerViewClickListener mListener;
    private DeprecateHandler deprecateHandler;

    public SIMListAdapter(Context mContext, List<SIMDetailsDto> simDetailsList, RecyclerViewClickListener listener) {
        this.context = mContext;
        this.simDetailsList = simDetailsList;
        this.mListener = listener;
        deprecateHandler = new DeprecateHandler(context);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtSIMActive;
        public TextView txtSIMActiveStatus;
        public LinearLayout layoutParent;
        public TextView txtSIMSlotLbl;
        public TextView txtSIMName;
        public SignalView signalViewBar;

        public MyViewHolder(View view, RecyclerViewClickListener listener) {
            super(view);
            layoutParent = view.findViewById(R.id.layoutParent);
            txtSIMSlotLbl = view.findViewById(R.id.txtSIMSlotLbl);
            txtSIMName = view.findViewById(R.id.txtSIMName);
            txtSIMActive = view.findViewById(R.id.txtSIMActive);
            txtSIMActiveStatus = view.findViewById(R.id.txtSIMActiveStatus);
            signalViewBar = view.findViewById(R.id.signalViewBar);
            mListener = listener;
            layoutParent.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(view, getAdapterPosition());
        }
    }

    @Override
    public SIMListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sim_list, parent, false);
        return new SIMListAdapter.MyViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(SIMListAdapter.MyViewHolder holder, int position) {

        final SIMDetailsDto simDetailsDto = simDetailsList.get(position);

        holder.txtSIMSlotLbl.setText(simDetailsDto.getSimSlotName());
        holder.txtSIMName.setText(simDetailsDto.getOperatorInfo()+" ("+simDetailsDto.getMobDataNetworkType()+")");
        String active = TextUtils.isEmpty(simDetailsDto.getOperatorInfo()) ? "Inactive or not present" : "Active";
        holder.txtSIMActive.setText(active);

        int bar = simDetailsDto.getSignalBar();

        if (TextUtils.isEmpty(simDetailsDto.getOperatorInfo()) && bar == NetworkTestingActivity.BAR_0_NO_SIGNAL) {
            holder.signalViewBar.setProgress(bar);
            holder.signalViewBar.setProgressColor(deprecateHandler.getColor(R.color.black_semi_transparent));
            holder.txtSIMName.setText("Unknown");
        } else {
            setSignalProgress(holder.signalViewBar, simDetailsDto.getSignalBar());
        }

        holder.txtSIMActiveStatus.setText(simDetailsDto.getSimSignalStrength());
        holder.txtSIMName.setTag(simDetailsDto);
    }

    @Override
    public int getItemCount() {
        return simDetailsList.size();
    }

    private void setSignalProgress(SignalView signalViewBar, int bar) {

        switch (bar) {

            case NetworkTestingActivity.BAR_0_NO_SIGNAL:
                signalViewBar.setProgress(bar);
                signalViewBar.setProgressColor(deprecateHandler.getColor(R.color.black_semi_transparent));
                break;

            case NetworkTestingActivity.BAR_1_POOR:
            case NetworkTestingActivity.BAR_2_FAIR:
                signalViewBar.setProgress(bar);
                signalViewBar.setProgressColor(deprecateHandler.getColor(R.color.orange));
                break;

            case NetworkTestingActivity.BAR_3_GOOD:
            case NetworkTestingActivity.BAR_4_VERY_GOOD:
            case NetworkTestingActivity.BAR_5_EXCELLENT:
                signalViewBar.setProgress(bar);
                signalViewBar.setProgressColor(deprecateHandler.getColor(R.color.green_l1));
                break;

            default:
                signalViewBar.setProgress(bar);
                signalViewBar.setProgressColor(deprecateHandler.getColor(R.color.black_semi_transparent));
                break;
        }
    }
}
