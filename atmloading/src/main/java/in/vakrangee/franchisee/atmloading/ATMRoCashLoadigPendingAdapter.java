package in.vakrangee.franchisee.atmloading;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;

public class ATMRoCashLoadigPendingAdapter extends RecyclerView.Adapter<ATMRoCashLoadigPendingAdapter.MyViewHolder> {

    private static final String TAG = "ATMRoCashLoadigPendingAdapter";
    public List<ATMRoLoadingPendingDto> atmRoLoadingPendingDtos;
    private Context context;
    private DeprecateHandler deprecateHandler;
    private IfcATMRoCashPosition ifcATMRoCashPosition;


    public interface IfcATMRoCashPosition {
        public void itemPosition(int position, ATMRoLoadingPendingDto atmRoLoadingPendingDto);
    }


    public ATMRoCashLoadigPendingAdapter(Context context, List<ATMRoLoadingPendingDto> atmRoLoadingPendingDtos, IfcATMRoCashPosition ifcATMRoCashPosition) {
        this.context = context;
        this.atmRoLoadingPendingDtos = atmRoLoadingPendingDtos;
        deprecateHandler = new DeprecateHandler(context);
        this.ifcATMRoCashPosition = ifcATMRoCashPosition;
    }

    @NonNull
    @Override
    public ATMRoCashLoadigPendingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_atm_ro_cash_loading, parent, false);
        return new ATMRoCashLoadigPendingAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ATMRoCashLoadigPendingAdapter.MyViewHolder holder, int position) {
        final ATMRoLoadingPendingDto cashLoadingDto = atmRoLoadingPendingDtos.get(position);

        holder.textAckNo.setText(cashLoadingDto.getAtmRoCashReceiptNo());
        holder.textAckDate.setText(cashLoadingDto.getAtmRoCashReceiptDate());
        holder.textName.setText(cashLoadingDto.getAtmCashRoName());
        holder.textStatus.setText(cashLoadingDto.getStatus());
        holder.textAmount.setText(cashLoadingDto.getAmount());

        //check box
        holder.checkboxROCash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    cashLoadingDto.setSelected(true);
                } else {
                    cashLoadingDto.setSelected(false);
                }
            }
        });
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout layoutParent;
        private TextView textAckNo, textAckDate, textName, textStatus, textAmount;
        private CheckBox checkboxROCash;

        public MyViewHolder(View view) {
            super(view);

            layoutParent = view.findViewById(R.id.layoutParent);
            textAckNo = view.findViewById(R.id.textAckNo);
            textAckDate = view.findViewById(R.id.textAckDate);
            textName = view.findViewById(R.id.textName);
            textStatus = view.findViewById(R.id.textStatus);
            textAmount = view.findViewById(R.id.textAmount);
            checkboxROCash = view.findViewById(R.id.checkboxROCash);
        }
    }

    public List<ATMRoLoadingPendingDto> getATMRoList() {
        return atmRoLoadingPendingDtos;
    }

    @Override
    public int getItemCount() {
        return atmRoLoadingPendingDtos.size();
    }
}
