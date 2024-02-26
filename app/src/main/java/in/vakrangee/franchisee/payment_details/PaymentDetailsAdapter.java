package in.vakrangee.franchisee.payment_details;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.commongui.animation.AnimationHanndler;
import in.vakrangee.supercore.franchisee.commongui.imagegallery.RecyclerViewClickListener;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;

public class PaymentDetailsAdapter extends RecyclerView.Adapter<PaymentDetailsAdapter.MyViewHolder> {

    public List<PaymentDetailsDto> paymentDetailsList;
    private Context context;
    private RecyclerViewClickListener mListener;
    private DeprecateHandler deprecateHandler;

    public PaymentDetailsAdapter(Context context, List<PaymentDetailsDto> paymentDetailsList, RecyclerViewClickListener listener) {
        this.context = context;
        this.paymentDetailsList = paymentDetailsList;
        this.mListener = listener;
        deprecateHandler = new DeprecateHandler(context);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtDate, txtAmount;
        public LinearLayout layoutPrimaryDetails, layoutSecondaryDetails, layoutMoreInfo;
        public LinearLayout layoutParent;
        public TextView txtSDate, txtTransactionId, txtRemitterName, txtFromAccountNumber, txtIFSCCode, txtBankName, txtUTR, txtNarration, txtTransferMode, txtSAmount;
        private ImageView imgMoreIcon;

        public MyViewHolder(View view, RecyclerViewClickListener listener) {
            super(view);
            layoutParent = view.findViewById(R.id.layoutParent);

            layoutPrimaryDetails = view.findViewById(R.id.layoutPrimaryDetails);
            txtDate = view.findViewById(R.id.txtDate);
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
    public PaymentDetailsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_details, parent, false);
        return new PaymentDetailsAdapter.MyViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(PaymentDetailsAdapter.MyViewHolder holder, int position) {

        final PaymentDetailsDto detailsDto = paymentDetailsList.get(position);

        //Primary Details
        String date = TextUtils.isEmpty(detailsDto.getDateTime()) ? "-" : detailsDto.getDateTime();
        holder.txtDate.setText(date);

        String amt = TextUtils.isEmpty(detailsDto.getAmount()) ? "0" : detailsDto.getAmount();
        setCommaUsingPlaceValue(amt, holder.txtAmount);

        //Secondary Details
        holder.txtSDate.setText(date);

        String transId = TextUtils.isEmpty(detailsDto.getTransactionId()) ? "-" : detailsDto.getTransactionId();
        holder.txtTransactionId.setText(transId);

        String name = TextUtils.isEmpty(detailsDto.getRemitterName()) ? "-" : detailsDto.getRemitterName();
        holder.txtRemitterName.setText(name);

        String acNumber = TextUtils.isEmpty(detailsDto.getFromAccountNumber()) ? "-" : detailsDto.getFromAccountNumber();
        holder.txtFromAccountNumber.setText(acNumber);

        String ifsCode = TextUtils.isEmpty(detailsDto.getRemitterIFSC()) ? "-" : detailsDto.getRemitterIFSC();
        holder.txtIFSCCode.setText(ifsCode);

        String bankName = TextUtils.isEmpty(detailsDto.getFromBankName()) ? "-" : detailsDto.getFromBankName();
        holder.txtBankName.setText(bankName);

        String utr = TextUtils.isEmpty(detailsDto.getUTR()) ? "-" : detailsDto.getUTR();
        holder.txtUTR.setText(utr);

        String narration = TextUtils.isEmpty(detailsDto.getNarration()) ? "-" : detailsDto.getNarration();
        holder.txtNarration.setText(narration);

        String transferMode = TextUtils.isEmpty(detailsDto.getTransferMode()) ? "-" : detailsDto.getTransferMode();
        holder.txtTransferMode.setText(transferMode);

        setCommaUsingPlaceValue(amt, holder.txtSAmount);

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
                PaymentDetailsDto dto = (PaymentDetailsDto) v.getTag();

                if (dto.getIsMoreOpened() == 1) {
                    dto.setIsMoreOpened(0);
                } else {
                    dto.setIsMoreOpened(1);
                }
                notifyDataSetChanged();
            }
        });

        holder.txtDate.setTag(detailsDto);
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
        return paymentDetailsList.size();
    }


}
