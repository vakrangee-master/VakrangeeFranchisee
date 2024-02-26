package in.vakrangee.franchisee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.model.WalletData;
import in.vakrangee.supercore.franchisee.utils.Constants;

/**
 * Created by Nileshd on 6/16/2016.
 */
public class AdapterRecyclerViewWalletStmt extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<WalletData> data = Collections.emptyList();
    Constants current;
    int currentPos = 0;

    // create constructor to innitilize context and data sent from MainActivity
    public AdapterRecyclerViewWalletStmt(Context context, List<WalletData> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerviewadpaterdata, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder = (MyHolder) holder;
        WalletData constants = data.get(position);
        myHolder.Service.setText(constants.SERVICES);
        myHolder.Date.setText("" + constants.DATE);
        myHolder.Particular.setText("" + constants.PARTICULAR);
        myHolder.Transaction.setText("Transaction Amount :" + constants.TRANACTION);

        if (constants.BALANCECR.equals("")) {
            myHolder.balance.setText("" + constants.BALANCE);
            myHolder.dr.setText("Dr");
            myHolder.dr.setTextColor(ContextCompat.getColor(context, R.color.md_light_green_900));


        } else {
            myHolder.balance.setText("" + constants.BALANCE);
            myHolder.dr.setText("Cr");
            myHolder.dr.setTextColor(ContextCompat.getColor(context, R.color.md_red_900));
        }

        // myHolder.balance.setText("" + constants.BALANCE);


        myHolder.Service.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));


    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView Service;
        TextView Date;
        TextView Particular;
        TextView Transaction;
        TextView balance;
        TextView dr;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);

            cv = (CardView) itemView.findViewById(R.id.cardView);
            Service = (TextView) itemView.findViewById(R.id.service);
            Date = (TextView) itemView.findViewById(R.id.date);
            Particular = (TextView) itemView.findViewById(R.id.particular);
            Transaction = (TextView) itemView.findViewById(R.id.transaction);
            balance = (TextView) itemView.findViewById(R.id.balance);
            dr = (TextView) itemView.findViewById(R.id.dr);


        }

    }


}
