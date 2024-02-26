package in.vakrangee.franchisee.fragment;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.model.ATMIDSpinner;
import in.vakrangee.supercore.franchisee.utils.Constants;

/**
 * Created by nileshd on 6/14/2017.
 */
public class AdapterCashLoading extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    public ArrayList<ATMIDSpinner> item_list;


    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;

    // create constructor to innitilize context and data sent from MainActivity

    public AdapterCashLoading(Context context, ArrayList<ATMIDSpinner> item_list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.item_list = item_list;

    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.container_atm_cash_loading, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder = (MyHolder) holder;
        final ATMIDSpinner current = item_list.get(position);


        myHolder.txtname.setText("Loading No:" + current.getMyModelObject1());
        myHolder.txtdate.setText("Loading Date" + current.getMyModelObject2());
        myHolder.txtackno.setText("Ack No:" + current.getMyModelObject4());
        myHolder.txtObj6.setText(current.getMyModelObject6());

        myHolder.txtstatus.setText(current.getMyModelObject3());
        myHolder.txtstatus.setTextColor(ContextCompat.getColor(context, R.color.md_green_500));
        myHolder.imgPDF.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                try {

                    String getID = current.getMyModelObject5();
                    String urllink = Constants.DownloadPDFfileURL + getID;

                    // final AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urllink));
                    context.startActivity(browserIntent);
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }
        });

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


        TextView txtdate;
        TextView txtname;

        TextView txtstatus, txtackno, txtObj6;
        ImageView imgPDF;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);

            txtdate = (TextView) itemView.findViewById(R.id.txtdate);
            txtname = (TextView) itemView.findViewById(R.id.txtname);

            txtstatus = (TextView) itemView.findViewById(R.id.txtstatus);
            txtackno = (TextView) itemView.findViewById(R.id.txtackno);
            txtObj6 = (TextView) itemView.findViewById(R.id.txtObj6);

            imgPDF = (ImageView) itemView.findViewById(R.id.imgPDF);

        }

    }


}

