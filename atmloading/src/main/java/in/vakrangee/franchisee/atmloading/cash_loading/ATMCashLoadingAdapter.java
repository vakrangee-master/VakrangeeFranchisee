package in.vakrangee.franchisee.atmloading.cash_loading;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import in.vakrangee.franchisee.atmloading.R;
import in.vakrangee.franchisee.atmloading.cash_sourcing.ATMCashSourcingDto;
import in.vakrangee.supercore.franchisee.commongui.animation.AnimationHanndler;
import in.vakrangee.supercore.franchisee.commongui.imagegallery.RecyclerViewClickListener;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;

public class ATMCashLoadingAdapter extends RecyclerView.Adapter<ATMCashLoadingAdapter.MyViewHolder> {

    public List<ATMCashLoadingDto> atmCashLoadingList;
    private Context context;
    private RecyclerViewClickListener mListener;
    private DeprecateHandler deprecateHandler;

    public ATMCashLoadingAdapter(Context context, List<ATMCashLoadingDto> atmCashLoadingList, RecyclerViewClickListener listener) {
        this.context = context;
        this.atmCashLoadingList = atmCashLoadingList;
        this.mListener = listener;
        deprecateHandler = new DeprecateHandler(context);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtSourcingId, txtSourceDate, txtLoadedAmount, txtAmount, txtPendingAmount;
        public TextView txtLoadingDate, txtSourcingDate, txtLoadingAmount;
        public LinearLayout layoutParent, layoutMoreInfo, layoutStatusBg;

        public MyViewHolder(View view, RecyclerViewClickListener listener) {
            super(view);
            layoutParent = view.findViewById(R.id.layoutParent);

            txtLoadingDate = view.findViewById(R.id.txtLoadingDate);
            txtSourcingDate = view.findViewById(R.id.txtSourcingDate);
            txtLoadingAmount = view.findViewById(R.id.txtLoadingAmount);

            layoutStatusBg = view.findViewById(R.id.layoutStatusBg);
            txtSourcingId = view.findViewById(R.id.txtSourcingId);
            txtSourceDate = view.findViewById(R.id.txtSourceDate);
            txtLoadedAmount = view.findViewById(R.id.txtLoadedAmount);
            txtPendingAmount = view.findViewById(R.id.txtPendingAmount);
            txtAmount = view.findViewById(R.id.txtAmount);
            layoutMoreInfo = view.findViewById(R.id.layoutMoreInfo);
            mListener = listener;
            layoutParent.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(view, getAdapterPosition());
        }
    }

    @Override
    public ATMCashLoadingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cash_loading, parent, false);
        return new ATMCashLoadingAdapter.MyViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(ATMCashLoadingAdapter.MyViewHolder holder, int position) {

        final ATMCashLoadingDto detailsDto = atmCashLoadingList.get(position);

        String sourcingId = TextUtils.isEmpty(detailsDto.getCashSourcingId()) ? "-" : detailsDto.getCashSourcingId();
        holder.txtSourcingId.setText(sourcingId);

        String date = TextUtils.isEmpty(detailsDto.getSourceDate()) ? "-" : detailsDto.getSourceDate();
        holder.txtSourceDate.setText(date);

        //String status = TextUtils.isEmpty(detailsDto.getStatus()) ? "-" : detailsDto.getStatus();
        String loadedAmt = TextUtils.isEmpty(detailsDto.getLoadedAmount()) ? "0" : detailsDto.getLoadedAmount();
        setCommaUsingPlaceValue(loadedAmt, holder.txtLoadedAmount);

        String pendingAmt = TextUtils.isEmpty(detailsDto.getPendingAmount()) ? "0" : detailsDto.getPendingAmount();
        setCommaUsingPlaceValue(pendingAmt, holder.txtPendingAmount);

        String amt = TextUtils.isEmpty(detailsDto.getAmount()) ? "0" : detailsDto.getAmount();
        setCommaUsingPlaceValue(amt, holder.txtAmount);

        //Set Work Status Color
        GradientDrawable bgWorkShape = (GradientDrawable) holder.layoutStatusBg.getBackground();
        String statusCode = TextUtils.isEmpty(detailsDto.getStatusCode()) ? "0" : detailsDto.getStatusCode();

        switch (statusCode.toLowerCase()) {

            case "1": //Loading Pending
                changeDrawable(bgWorkShape, deprecateHandler.getColor(R.color.orange_L1), 0, deprecateHandler.getColor(R.color.orange_L1));
                holder.txtLoadedAmount.setTextColor(deprecateHandler.getColor(R.color.white));
                break;

            case "2": //Cash Loaded
                changeDrawable(bgWorkShape, deprecateHandler.getColor(R.color.green), 0, deprecateHandler.getColor(R.color.green));
                holder.txtLoadedAmount.setTextColor(deprecateHandler.getColor(R.color.white));
                break;

            default:
                changeDrawable(bgWorkShape, deprecateHandler.getColor(R.color.gray), 0, deprecateHandler.getColor(R.color.gray));
                holder.txtLoadedAmount.setTextColor(deprecateHandler.getColor(R.color.headline_lbl));
                break;
        }

        holder.layoutMoreInfo.setTag(detailsDto);
        holder.layoutMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationHanndler.bubbleAnimation(context, v);
                ATMCashSourcingDto dto = (ATMCashSourcingDto) v.getTag();
            }
        });

        String sourcedDate = TextUtils.isEmpty(detailsDto.getSourceDate()) ? "-" : detailsDto.getSourceDate();
        holder.txtSourcingDate.setText(sourcedDate);

        String loadingDate = TextUtils.isEmpty(detailsDto.getLoadingDate()) ? "-" : detailsDto.getLoadingDate();
        holder.txtLoadingDate.setText(loadingDate);

        String loadingAmt = TextUtils.isEmpty(detailsDto.getLoadedAmount()) ? "0" : detailsDto.getLoadedAmount();
        setCommaUsingPlaceValue(loadingAmt, holder.txtLoadingAmount);

        holder.txtSourcingId.setTag(detailsDto);
    }

    @Override
    public int getItemCount() {
        return atmCashLoadingList.size();
    }

    private void setCommaUsingPlaceValue(String value, TextView textView) {

        if (value.equalsIgnoreCase("-"))
            textView.setText(value);
        else {
            BigDecimal bd = new BigDecimal(value);
            long lDurationMillis = bd.setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
            String moneyString1 = new DecimalFormat("##,##,###.##").format(lDurationMillis);
            textView.setText("₹ " + moneyString1);
        }
    }

    public void changeDrawable(GradientDrawable drawable, int solidColor, int strokeWidth, int strokeColor) {
        if (drawable != null) {

            drawable.setColor(solidColor);
            if (strokeWidth > 0)
                drawable.setStroke(strokeWidth, strokeColor);

        }
    }

    public void resetData() {
        atmCashLoadingList = new ArrayList<>();
        notifyDataSetChanged();
    }

}
