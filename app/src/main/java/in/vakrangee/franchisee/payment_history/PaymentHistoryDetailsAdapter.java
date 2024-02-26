package in.vakrangee.franchisee.payment_history;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.application.VakrangeeKendraApplication;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;

public class PaymentHistoryDetailsAdapter extends RecyclerView.Adapter<PaymentHistoryDetailsAdapter.MyViewHolder> {

    private static final String TAG = "PaymentHistoryDetailsAdapter";
    public List<PaymentHistoryDto> paymentHistoryDtos;
    private Context context;
    private PaymentHistoryDetailsAdapter.ifcItemClick ifcItemClick;
    private DeprecateHandler deprecateHandler;

    public interface ifcItemClick {
        public void clickPosition(int position, PaymentHistoryDto paymentHistoryDto);
    }

    public PaymentHistoryDetailsAdapter(Context context, List<PaymentHistoryDto> paymentHistoryDtos, PaymentHistoryDetailsAdapter.ifcItemClick ifcItemClick) {
        this.context = context;
        this.paymentHistoryDtos = paymentHistoryDtos;
        this.ifcItemClick = ifcItemClick;
        deprecateHandler = new DeprecateHandler(context);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textTotalPayment, textPaymentStep, textPaymentIcon, textPaymentAmountDate;
        private LinearLayout layoutMain;

        public MyViewHolder(View view) {
            super(view);
            textTotalPayment = view.findViewById(R.id.textTotalPayment);
            textPaymentStep = view.findViewById(R.id.textPaymentStep);
            textPaymentIcon = view.findViewById(R.id.textPaymentIcon);
            textPaymentAmountDate = view.findViewById(R.id.textPaymentAmountDate);
            layoutMain = view.findViewById(R.id.layoutMain);
        }
    }


    @Override
    public PaymentHistoryDetailsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_hisotry_details, parent, false);
        return new PaymentHistoryDetailsAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(PaymentHistoryDetailsAdapter.MyViewHolder holder, int position) {

        final PaymentHistoryDto detailsDto = paymentHistoryDtos.get(position);

        if (!TextUtils.isEmpty(detailsDto.getInstallmentFee())) {
            CommonUtils.setRupeeToSpeator(holder.textTotalPayment, detailsDto.getInstallmentFee());
        } else {
            holder.textTotalPayment.setText("0 ₹");
        }

        //holder.textTotalPayment.setText(TextUtils.isEmpty(detailsDto.getInstallmentFee()) ? "" : detailsDto.getInstallmentFee());
        holder.textPaymentStep.setText(TextUtils.isEmpty(detailsDto.getInstallmentStep()) ? "" : detailsDto.getInstallmentStep());
        holder.textPaymentAmountDate.setText(TextUtils.isEmpty(detailsDto.getInstalmentPaymentDate()) ? "" : detailsDto.getInstalmentPaymentDate());
        String installorNot = TextUtils.isEmpty(detailsDto.getInstallmentType()) ? "" : detailsDto.getInstallmentType();
        if (installorNot.equalsIgnoreCase("Y")) {
            holder.textPaymentIcon.setText(context.getResources().getString(R.string.fa_circle_check));
            holder.textPaymentIcon.setTextSize(22);
            holder.textPaymentIcon.setTextColor(deprecateHandler.getColor(R.color.md_green_500));
            holder.textPaymentIcon.setTypeface(VakrangeeKendraApplication.font_awesome, Typeface.BOLD);
        } else {
            holder.textPaymentIcon.setText(context.getResources().getString(R.string.fa_circle_cross));
            holder.textPaymentIcon.setTextSize(22);
            holder.textPaymentIcon.setTextColor(deprecateHandler.getColor(R.color.gray));
            holder.textPaymentIcon.setTypeface(VakrangeeKendraApplication.font_awesome, Typeface.BOLD);
        }

    }

    @Override
    public int getItemCount() {
        return paymentHistoryDtos.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setFontawesomeIcon(TextView textView, String icon) {
        textView.setText(icon);
        textView.setTextSize(20);
        textView.setTextColor(deprecateHandler.getColor(R.color.iGrey));
        textView.setTypeface(VakrangeeKendraApplication.font_awesome, Typeface.BOLD);

    }
}
