package in.vakrangee.franchisee.atmloading.cash_sourcing;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import in.vakrangee.franchisee.atmloading.R;
import in.vakrangee.supercore.franchisee.commongui.animation.AnimationHanndler;
import in.vakrangee.supercore.franchisee.commongui.imagegallery.RecyclerViewClickListener;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;

public class ATMCashSourcingAdapter extends RecyclerView.Adapter<ATMCashSourcingAdapter.MyViewHolder> {

    public List<ATMCashSourcingDto> atmCashSourcingList;
    private Context context;
    private RecyclerViewClickListener mListener;
    private DeprecateHandler deprecateHandler;

    public ATMCashSourcingAdapter(Context context, List<ATMCashSourcingDto> atmCashSourcingList, RecyclerViewClickListener listener) {
        this.context = context;
        this.atmCashSourcingList = atmCashSourcingList;
        this.mListener = listener;
        deprecateHandler = new DeprecateHandler(context);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtSourcingId, txtSourceDate, txtStatus, txtSourcedAmount, txtCashLoaded, txtBalanceCash;
        public LinearLayout layoutParent, layoutMoreInfo, layoutStatusBg;
        private ImageView imgDocumentProof;

        public MyViewHolder(View view, RecyclerViewClickListener listener) {
            super(view);
            layoutParent = view.findViewById(R.id.layoutParent);

            layoutStatusBg = view.findViewById(R.id.layoutStatusBg);
            txtSourcingId = view.findViewById(R.id.txtSourcingId);
            txtSourceDate = view.findViewById(R.id.txtSourceDate);
            txtStatus = view.findViewById(R.id.txtStatus);
            txtSourcedAmount = view.findViewById(R.id.txtSourcedAmount);
            txtCashLoaded = view.findViewById(R.id.txtCashLoaded);
            txtBalanceCash = view.findViewById(R.id.txtBalanceCash);
            imgDocumentProof = view.findViewById(R.id.imgDocumentProof);
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
    public ATMCashSourcingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cash_sourcing, parent, false);
        return new ATMCashSourcingAdapter.MyViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(ATMCashSourcingAdapter.MyViewHolder holder, int position) {

        final ATMCashSourcingDto detailsDto = atmCashSourcingList.get(position);

        String sourcingId = TextUtils.isEmpty(detailsDto.getCashSourcingId()) ? "-" : detailsDto.getCashSourcingId();
        holder.txtSourcingId.setText(sourcingId);

        if (TextUtils.isEmpty(detailsDto.getSourceDate())) {
            holder.txtSourceDate.setText("-");
        } else {
            String date = CommonUtils.getFormattedDate("yyyy-MM-dd", "dd MMM yyyy", detailsDto.getSourceDate());
            holder.txtSourceDate.setText(date);
        }

        String status = TextUtils.isEmpty(detailsDto.getStatus()) ? "-" : detailsDto.getStatus();
        holder.txtStatus.setText(status);

        //Sourced Amount
        String amt = TextUtils.isEmpty(detailsDto.getAmount()) ? "0" : detailsDto.getAmount();
        setCommaUsingPlaceValue(amt, holder.txtSourcedAmount);

        //Cash Loaded
        String cashLoadedAmt = TextUtils.isEmpty(detailsDto.getLoadedAmount()) ? "0" : detailsDto.getLoadedAmount();
        setCommaUsingPlaceValue(cashLoadedAmt, holder.txtCashLoaded);

        //Balance Amount
        String balAmt = TextUtils.isEmpty(detailsDto.getBalanceAmount()) ? "0" : detailsDto.getBalanceAmount();
        setCommaUsingPlaceValue(balAmt, holder.txtBalanceCash);

        //Set Work Status Color
        GradientDrawable bgWorkShape = (GradientDrawable) holder.layoutStatusBg.getBackground();

        switch (detailsDto.getStatusCode().toLowerCase()) {

            case "1": //Uploaded
                changeDrawable(bgWorkShape, deprecateHandler.getColor(R.color.green), 0, deprecateHandler.getColor(R.color.green));
                holder.txtStatus.setTextColor(deprecateHandler.getColor(R.color.white));
                break;

            case "0": //Cancelled
                changeDrawable(bgWorkShape, deprecateHandler.getColor(R.color.ired), 0, deprecateHandler.getColor(R.color.ired));
                holder.txtStatus.setTextColor(deprecateHandler.getColor(R.color.white));
                break;

            default:
                changeDrawable(bgWorkShape, deprecateHandler.getColor(R.color.gray), 0, deprecateHandler.getColor(R.color.gray));
                holder.txtStatus.setTextColor(deprecateHandler.getColor(R.color.headline_lbl));
                break;
        }

        boolean IsPDF = ((detailsDto.getCashSourcingExt() != null) && detailsDto.getCashSourcingExt().equalsIgnoreCase("pdf")) ? true : false;
        if (IsPDF) {
            holder.imgDocumentProof.setImageDrawable(deprecateHandler.getDrawable(R.drawable.pdf));
        } else if (!TextUtils.isEmpty(detailsDto.getCashSourcingScanCopyId())) {
            String gstUrl = Constants.DownloadImageUrl + detailsDto.getCashSourcingScanCopyId();
            Glide.with(context)
                    .load(gstUrl)
                    .apply(new RequestOptions()
                            .error(R.drawable.ic_camera_alt_black_72dp)
                            .placeholder(R.drawable.ic_camera_alt_black_72dp)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(holder.imgDocumentProof);
        }

        holder.layoutMoreInfo.setTag(detailsDto);
        holder.layoutMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationHanndler.bubbleAnimation(context, v);
                ATMCashSourcingDto dto = (ATMCashSourcingDto) v.getTag();
            }
        });

        holder.txtSourcingId.setTag(detailsDto);
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

    @Override
    public int getItemCount() {
        return atmCashSourcingList.size();
    }

    public void changeDrawable(GradientDrawable drawable, int solidColor, int strokeWidth, int strokeColor) {
        if (drawable != null) {

            drawable.setColor(solidColor);
            if (strokeWidth > 0)
                drawable.setStroke(strokeWidth, strokeColor);

        }
    }
}
