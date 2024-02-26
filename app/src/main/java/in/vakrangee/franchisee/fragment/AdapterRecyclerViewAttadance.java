package in.vakrangee.franchisee.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.model.Attedance;

/**
 * Created by Nileshd on 5/30/2017.
 */
public class AdapterRecyclerViewAttadance extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;

    List<Attedance> data = Collections.emptyList();


    // create constructor to innitilize context and data sent from MainActivity
    public AdapterRecyclerViewAttadance(Context context, List<Attedance> data) {
        this.context = context;

        this.data = data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_attendace_system, parent, false);

        MyHolder holder = new MyHolder(itemView);

        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder = (MyHolder) holder;
        final Attedance current = data.get(position);
        myHolder.textName.setText(current.name);
        myHolder.textVKID.setText(current.vkid);

        double ab = Double.parseDouble(current.near) * 1000;

        myHolder.textNear.setText(String.valueOf(ab) + "m");


        if (ab <= 5.0) {
            myHolder.textNear.setTextColor(ContextCompat.getColor(context, R.color.md_green_500));
        } else if (ab > 6.0 && ab <= 10.0) {
            myHolder.textNear.setTextColor(ContextCompat.getColor(context, R.color.md_yellow_500));
        } else {
            myHolder.textNear.setTextColor(ContextCompat.getColor(context, R.color.md_red_500));
        }

        // myHolder.textPrice.setText("Rs. " + current.price + "\\Kg");
        // myHolder.textPrice.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        myHolder.linearattedance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                //inflate layout from xml. you must create an xml layout file in res/layout first
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View layout = inflater.inflate(R.layout.popmvakrangeelocation, null);
                builder.setView(layout);
                Toast.makeText(context, current.vkid, Toast.LENGTH_SHORT).show();

                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "submit successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }
                });

                builder.setNegativeButton("Cancel  ", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();

            }
        });
        // load image into imageview using glide


    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {

        TextView textName;
        ImageView ivFish;
        TextView textNear;
        TextView textVKID;

        LinearLayout linearattedance;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            textName = (TextView) itemView.findViewById(R.id.textName);
            ivFish = (ImageView) itemView.findViewById(R.id.ivFish);
            textNear = (TextView) itemView.findViewById(R.id.textNear);

            textVKID = (TextView) itemView.findViewById(R.id.textVKID);
            linearattedance = (LinearLayout) itemView.findViewById(R.id.linearattedance);
        }

    }
}
