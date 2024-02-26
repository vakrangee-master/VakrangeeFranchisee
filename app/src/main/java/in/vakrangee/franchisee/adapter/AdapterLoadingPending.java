package in.vakrangee.franchisee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.model.ATMIDSpinner;

/**
 * Created by Nileshd on 6/6/2017.
 */
public class AdapterLoadingPending extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    public ArrayList<ATMIDSpinner> item_list;
    private int lastCheckedPosition = -1;
    CheckBox check_all;

    // create constructor to innitilize context and data sent from MainActivity

    public AdapterLoadingPending(Context context, ArrayList<ATMIDSpinner> item_list, CheckBox chk_select_all) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.item_list = item_list;
        this.check_all = chk_select_all;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.container_atm_pading_loading, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder = (MyHolder) holder;
        final ATMIDSpinner current = item_list.get(position);
        myHolder.txtackno.setText(current.getMyModelObject2());
        myHolder.txtdate.setText(current.getMyModelObject3());
        myHolder.txtname.setText(current.getMyModelObject4());
        myHolder.txtamount.setText("Rs." + current.getMyModelObject5());
        myHolder.txtamount.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));

        String s = current.getMyModelObject6();
        if (s.equals("Loading Pending")) {
            myHolder.txtstatus.setText("Pending");
        } else {
            myHolder.txtstatus.setText(current.getMyModelObject6());
        }
        // myHolder.txtstatus.setText(current.getMyModelObject6());
        myHolder.txtstatus.setTextColor(ContextCompat.getColor(context, R.color.md_red_500));

        check_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView == check_all) {
                    check_all.setChecked(isChecked);
                    for (int i = 0; i < item_list.size(); i++) {
                        item_list.get(i).setSelected(isChecked);
                    }
                    notifyDataSetChanged();
                } else {
                    int position = (Integer) buttonView.getTag();
                    if (isChecked) {
                        item_list.get(position).setSelected(true);
                    } else {
                        item_list.get(position).setSelected(false);
                        if (check_all.isChecked()) {
                            check_all.setChecked(false);
                            for (int i = 0; i < item_list.size(); i++) {
                                item_list.get(i).setSelected(true);
                                item_list.get(position).setSelected(false);
                            }
                        }
                    }
                    notifyDataSetChanged();
                }
            }
        });

//        myHolder.chkSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                //set your object's last status
//                current.setSelected(isChecked);
//                Toast.makeText(context, current.getMyModelObject5(), Toast.LENGTH_SHORT).show();
//            }
//        });


//        myHolder.chkSelected.setOnCheckedChangeListener(null);
//
//        //if true, your checkbox will be selected, else unselected
//        myHolder.chkSelected.setChecked(current.isSelected());
//
//        myHolder.chkSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                //set your object's last status
//
//
//                current.setSelected(isChecked);
//                Toast.makeText(context, current.getMyModelObject5(), Toast.LENGTH_SHORT).show();
//            }
//        });

        myHolder.chkSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    current.setSelected(true);
                } else {
                    current.setSelected(false);
                }
            }
        });
        myHolder.chkSelected.setChecked(current.isSelected());
    }

    // return total item from List
    @Override
    public int getItemCount() {
        return item_list.size();
    }

    public List<ATMIDSpinner> getEmployeeList() {
        return item_list;
    }


    class MyHolder extends RecyclerView.ViewHolder {

        TextView txtackno;
        TextView txtdate;
        TextView txtname;
        TextView txtamount;
        TextView txtstatus;
        CheckBox chkSelected;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            txtackno = (TextView) itemView.findViewById(R.id.txtackno);
            txtdate = (TextView) itemView.findViewById(R.id.txtdate);
            txtname = (TextView) itemView.findViewById(R.id.txtname);
            txtamount = (TextView) itemView.findViewById(R.id.txtamount);
            txtstatus = (TextView) itemView.findViewById(R.id.txtstatus);
            chkSelected = (CheckBox) itemView.findViewById(R.id.chk_selected);

        }

    }

}
