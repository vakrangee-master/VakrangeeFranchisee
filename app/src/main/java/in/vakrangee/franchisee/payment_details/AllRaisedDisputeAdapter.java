package in.vakrangee.franchisee.payment_details;

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
import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.commongui.animation.AnimationHanndler;
import in.vakrangee.supercore.franchisee.commongui.imagegallery.RecyclerViewClickListener;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;

public class AllRaisedDisputeAdapter extends RecyclerView.Adapter<AllRaisedDisputeAdapter.MyViewHolder> {

    public List<RaiseDisputeDto> raiseDisputeList;
    private Context context;
    private RecyclerViewClickListener mListener;
    private DeprecateHandler deprecateHandler;

    public AllRaisedDisputeAdapter(Context context, List<RaiseDisputeDto> raiseDisputeList, RecyclerViewClickListener listener) {
        this.context = context;
        this.raiseDisputeList = raiseDisputeList;
        this.mListener = listener;
        deprecateHandler = new DeprecateHandler(context);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtRaisedDate, txtStatus, txtPaymentDate, txtAmount;
        public LinearLayout layoutPrimaryDetails, layoutSecondaryDetails, layoutMoreInfo;
        public LinearLayout layoutParent;
        public TextView txtSDate, txtTransactionId, txtRemitterName, txtFromAccountNumber, txtIFSCCode, txtBankName, txtUTR, txtNarration, txtTransferMode, txtSAmount,
                txtRemarks, txtSRaisedDate, txtSStatus;
        private ImageView imgMoreIcon, imgProofImage;

        public MyViewHolder(View view, RecyclerViewClickListener listener) {
            super(view);
            layoutParent = view.findViewById(R.id.layoutParent);

            layoutPrimaryDetails = view.findViewById(R.id.layoutPrimaryDetails);
            txtRaisedDate = view.findViewById(R.id.txtRaisedDate);
            txtStatus = view.findViewById(R.id.txtStatus);
            txtPaymentDate = view.findViewById(R.id.txtPaymentDate);
            txtAmount = view.findViewById(R.id.txtAmount);

            layoutSecondaryDetails = view.findViewById(R.id.layoutSecondaryDetails);
            txtSDate = view.findViewById(R.id.txtSDate);
            txtTransactionId = view.findViewById(R.id.txtTransactionId);
            txtRemitterName = view.findViewById(R.id.txtRemitterName);
            txtFromAccountNumber = view.findViewById(R.id.txtFromAccountNumber);
            txtIFSCCode = view.findViewById(R.id.txtIFSCCode);
            txtBankName = view.findViewById(R.id.txtBankName);
            txtUTR = view.findViewById(R.id.txtUTR);
            txtNarration = view.findViewById(R.id.txtNarration);
            txtTransferMode = view.findViewById(R.id.txtTransferMode);
            txtSAmount = view.findViewById(R.id.txtSAmount);
            imgProofImage = view.findViewById(R.id.imgProofImage);
            txtRemarks = view.findViewById(R.id.txtRemarks);
            txtSRaisedDate = view.findViewById(R.id.txtSRaisedDate);
            txtSStatus = view.findViewById(R.id.txtSStatus);

            layoutMoreInfo = view.findViewById(R.id.layoutMoreInfo);
            imgMoreIcon = view.findViewById(R.id.imgMoreIcon);

            mListener = listener;
            layoutParent.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(view, getAdapterPosition());
        }
    }

    @Override
    public AllRaisedDisputeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_raised_dispute, parent, false);
        return new AllRaisedDisputeAdapter.MyViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(AllRaisedDisputeAdapter.MyViewHolder holder, int position) {

        final RaiseDisputeDto detailsDto = raiseDisputeList.get(position);

        String raisedDate = TextUtils.isEmpty(detailsDto.getRaisedDate()) ? "-" : detailsDto.getRaisedDate();
        String statusDesc = TextUtils.isEmpty(detailsDto.getStatusDesc()) ? "-" : detailsDto.getStatusDesc();
        String paymtDate = TextUtils.isEmpty(detailsDto.getPaymentDate()) ? "-" : detailsDto.getPaymentDate();
        String amt = TextUtils.isEmpty(detailsDto.getAmount()) ? "0" : detailsDto.getAmount();

        //Primary Details
        bindPrimaryViews(detailsDto, holder, raisedDate, statusDesc, paymtDate);

        //Secondary Details
        holder.txtSDate.setText(paymtDate);
        holder.txtSStatus.setText(statusDesc);
        setStatusBg(holder.txtSStatus, detailsDto.getStatus());
        holder.txtSRaisedDate.setText(raisedDate);
        setCommaUsingPlaceValue(amt, holder.txtSAmount);
        bindSecondaryViews(detailsDto, holder);

        if (!TextUtils.isEmpty(detailsDto.getProofPicId())) {
            String gstUrl = Constants.DownloadImageUrl + detailsDto.getProofPicId();
            Glide.with(context)
                    .load(gstUrl)
                    .apply(new RequestOptions()
                            .error(R.drawable.ic_camera_alt_black_72dp)
                            .placeholder(R.drawable.ic_camera_alt_black_72dp)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(holder.imgProofImage);
        }

        if (detailsDto.getIsMoreOpened() == 1) {
            holder.layoutSecondaryDetails.setVisibility(View.VISIBLE);
            holder.layoutPrimaryDetails.setVisibility(View.GONE);
            holder.imgMoreIcon.setImageDrawable(deprecateHandler.getDrawable(R.drawable.up_arrow_24dp));
        } else {
            holder.layoutSecondaryDetails.setVisibility(View.GONE);
            holder.layoutPrimaryDetails.setVisibility(View.VISIBLE);
            holder.imgMoreIcon.setImageDrawable(deprecateHandler.getDrawable(R.drawable.down_arrow_16dp));
        }

        holder.layoutMoreInfo.setTag(detailsDto);
        holder.layoutMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationHanndler.bubbleAnimation(context, v);
                RaiseDisputeDto dto = (RaiseDisputeDto) v.getTag();

                if (dto.getIsMoreOpened() == 1) {
                    dto.setIsMoreOpened(0);
                } else {
                    dto.setIsMoreOpened(1);
                }
                notifyDataSetChanged();
            }
        });

        holder.txtRaisedDate.setTag(detailsDto);
    }

    private void bindSecondaryViews(RaiseDisputeDto detailsDto, AllRaisedDisputeAdapter.MyViewHolder holder) {

        String rrn = TextUtils.isEmpty(detailsDto.getRrn()) ? "-" : detailsDto.getRrn();
        holder.txtTransactionId.setText(rrn);

        String acName = TextUtils.isEmpty(detailsDto.getAccountHolderName()) ? "-" : detailsDto.getAccountHolderName();
        holder.txtRemitterName.setText(acName);

        String acNumber = TextUtils.isEmpty(detailsDto.getAccountNumber()) ? "-" : detailsDto.getAccountNumber();
        holder.txtFromAccountNumber.setText(acNumber);

        String ifscCode = TextUtils.isEmpty(detailsDto.getIfscCode()) ? "-" : detailsDto.getIfscCode();
        holder.txtIFSCCode.setText(ifscCode);

        String bnkName = TextUtils.isEmpty(detailsDto.getBankName()) ? "-" : detailsDto.getBankName();
        holder.txtBankName.setText(bnkName);

        String utr = TextUtils.isEmpty(detailsDto.getUtr()) ? "-" : detailsDto.getUtr();
        holder.txtUTR.setText(utr);

        String transferMode = TextUtils.isEmpty(detailsDto.getTransferMode()) ? "-" : detailsDto.getTransferMode();
        holder.txtTransferMode.setText(transferMode);

        String remarks = TextUtils.isEmpty(detailsDto.getRemarks()) ? "-" : detailsDto.getRemarks();
        holder.txtRemarks.setText(remarks);

    }

    private void bindPrimaryViews(RaiseDisputeDto detailsDto, AllRaisedDisputeAdapter.MyViewHolder holder, String raisedDate, String statusDesc, String paymtDate) {
        holder.txtRaisedDate.setText(raisedDate);

        holder.txtStatus.setText(statusDesc);
        setStatusBg(holder.txtStatus, detailsDto.getStatus());

        holder.txtPaymentDate.setText(paymtDate);

        String amt = TextUtils.isEmpty(detailsDto.getAmount()) ? "0" : detailsDto.getAmount();
        setCommaUsingPlaceValue(amt, holder.txtAmount);
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
        return raiseDisputeList.size();
    }

    public void changeDrawable(GradientDrawable drawable, int solidColor, int strokeWidth, int strokeColor) {
        if (drawable != null) {

            drawable.setColor(solidColor);
            if (strokeWidth > 0)
                drawable.setStroke(strokeWidth, strokeColor);

        }
    }

    private void setStatusBg(TextView textView, String status) {

        status = TextUtils.isEmpty(status) ? "-" : status;

        //Set Status Color
        GradientDrawable bgWorkShape = (GradientDrawable) textView.getBackground();

        switch (status) {
            case "0":
                changeDrawable(bgWorkShape, deprecateHandler.getColor(R.color.orange), 0, deprecateHandler.getColor(R.color.orange));
                textView.setTextColor(deprecateHandler.getColor(R.color.white));
                break;

            case "1":
                changeDrawable(bgWorkShape, deprecateHandler.getColor(R.color.green), 0, deprecateHandler.getColor(R.color.green));
                textView.setTextColor(deprecateHandler.getColor(R.color.white));
                break;

            case "2":
                changeDrawable(bgWorkShape, deprecateHandler.getColor(R.color.red), 0, deprecateHandler.getColor(R.color.red));
                textView.setTextColor(deprecateHandler.getColor(R.color.white));
                break;

            default:
                changeDrawable(bgWorkShape, deprecateHandler.getColor(R.color.grey_vl), 0, deprecateHandler.getColor(R.color.grey_vl));
                textView.setTextColor(deprecateHandler.getColor(R.color.headline_lbl));
                break;
        }

    }

}
