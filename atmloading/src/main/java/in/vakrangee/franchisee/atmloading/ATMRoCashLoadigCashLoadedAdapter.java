package in.vakrangee.franchisee.atmloading;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;

public class ATMRoCashLoadigCashLoadedAdapter extends RecyclerView.Adapter<ATMRoCashLoadigCashLoadedAdapter.MyViewHolder> {

    private static final String TAG = "ATMRoCashLoadigCashLoadedAdapter";
    public List<ATMRoCashLoadedDto> atmRoCashLoadingDtos;
    private Context context;
    private DeprecateHandler deprecateHandler;
    private ATMRoCashLoadigCashLoadedAdapter.IfcATMRoCashPosition ifcATMRoCashPosition;


    public interface IfcATMRoCashPosition {
        public void itemPosition(int position, ATMRoCashLoadedDto atmRoCashLoadingDto);

        public void pdfViewClick(int position, ATMRoCashLoadedDto atmRoCashLoadedDto);
    }


    public ATMRoCashLoadigCashLoadedAdapter(Context context, List<ATMRoCashLoadedDto> atmRoCashLoadingDtos, ATMRoCashLoadigCashLoadedAdapter.IfcATMRoCashPosition ifcATMRoCashPosition) {
        this.context = context;
        this.atmRoCashLoadingDtos = atmRoCashLoadingDtos;
        deprecateHandler = new DeprecateHandler(context);
        this.ifcATMRoCashPosition = ifcATMRoCashPosition;
    }

    @NonNull
    @Override
    public ATMRoCashLoadigCashLoadedAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_atm_ro_cash_loaded, parent, false);
        return new ATMRoCashLoadigCashLoadedAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ATMRoCashLoadigCashLoadedAdapter.MyViewHolder holder, final int position) {
        final ATMRoCashLoadedDto cashLoadingDto = atmRoCashLoadingDtos.get(position);

        holder.textCashLoadingNumber.setText(cashLoadingDto.getAtmRoCashLoadingId());
        holder.textCashLoadingDate.setText(cashLoadingDto.getLodingDate());

        //if receipts id show PDF icon
        if (TextUtils.isEmpty(cashLoadingDto.getAtmRoCashReceiptImage())) {
            holder.layoutReceipts.setVisibility(View.GONE);
        } else {
            holder.layoutReceipts.setVisibility(View.VISIBLE);
        }

        //if cash modification allowed show edit icon -E -editable - D -view
        if (cashLoadingDto.getSt().equalsIgnoreCase("E")) {
            holder.layoutIsModificationAllowed.setVisibility(View.VISIBLE);
        } else {
            holder.layoutIsModificationAllowed.setVisibility(View.INVISIBLE);
        }

        //on item click to next activity
        holder.layoutParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ifcATMRoCashPosition.itemPosition(position, cashLoadingDto);
            }
        });

        //pdf view
        holder.layoutReceipts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ifcATMRoCashPosition.pdfViewClick(position, cashLoadingDto);
            }
        });

        //set status
        holder.textStatus.setText(cashLoadingDto.getStatus().toUpperCase());
        Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, R.drawable.round_shape_half_background);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, context.getResources().getColor(R.color.md_green_600));

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textCashLoadingNumber, textCashLoadingDate, textStatus;
        private ImageView imageEditAllowed;
        private LinearLayout layoutReceipts, layoutParent, layoutIsModificationAllowed;

        public MyViewHolder(View view) {
            super(view);

            layoutReceipts = view.findViewById(R.id.layoutReceipts);
            textCashLoadingNumber = view.findViewById(R.id.textCashLoadingNumber);
            textCashLoadingDate = view.findViewById(R.id.textCashLoadingDate);
            textStatus = view.findViewById(R.id.textStatus);
            imageEditAllowed = view.findViewById(R.id.imageEditAllowed);
            layoutParent = view.findViewById(R.id.layoutParent);
            layoutIsModificationAllowed = view.findViewById(R.id.layoutIsModificationAllowed);

        }
    }


    @Override
    public int getItemCount() {
        return atmRoCashLoadingDtos.size();
    }

}
