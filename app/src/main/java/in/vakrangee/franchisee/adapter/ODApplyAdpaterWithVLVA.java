package in.vakrangee.franchisee.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.places.Place;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.model.LeaveOD;
import in.vakrangee.supercore.franchisee.model.ODSpinner;
import in.vakrangee.supercore.franchisee.utils.GPSTracker;

/**
 * Created by Nileshd on 4/10/2017.
 */
public class ODApplyAdpaterWithVLVA extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    TelephonyManager telephonyManager;
    List<LeaveOD> data;
    private ArrayList<LeaveOD> arraylist;

    ProgressDialog progress;
    String Datef, Datet;

    String diplayServerResopnse;
    TextView edtODReasonpop;

    Long Spnitem;
    String leaveOdId;
    GPSTracker gps;
    private List<ODSpinner> ODSpinnerEntityList = new ArrayList<ODSpinner>();
    Place place;
    final int PLACE_PICKER_REQUEST = 1;
    String getRadioString;

    // create constructor to innitilize context and data sent from MainActivity
    public ODApplyAdpaterWithVLVA(Context context, List<LeaveOD> data) {
        this.context = context;
        this.data = data;
        this.arraylist = new ArrayList<LeaveOD>();
        this.arraylist.addAll(data);
    }


    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adpaterodapplywithvlva, parent, false);
        MyHolder holder = new MyHolder(view);
        gps = new GPSTracker(context);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


        // Get current position of item in recyclerview to bind data and assign values from list

        try {

            final MyHolder myHolder = (MyHolder) holder;
            final LeaveOD constants = data.get(position);

            myHolder.txtVAName.setText("Nilesh Dhola - VL0003167");
            myHolder.txtApplyDate.setText(constants.getTime());

            myHolder.txtVAStartDate.setText("start date: " + constants.getStartDate());
            myHolder.txtVAEndDate.setText("end date: " + constants.getEndDate());
            myHolder.txtVAnumberOfDay.setText("No of Days: 3");

            myHolder.txtVAReason.setText(constants.getReason());

            if (constants.getReason().equals("Pending")) {

                myHolder.ImgStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.approved));
            }


            myHolder.linerODClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context, "" + constants.getLeaveOdId(), Toast.LENGTH_SHORT).show();
                    final AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                    //inflate layout from xml. you must create an xml layout file in res/layout first
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


                    View layout = inflater.inflate(R.layout.popupodvavl, null);

                    TextView txtVANamepop = (TextView) layout.findViewById(R.id.txtVANamepop);
                    TextView txtApplyDatepop = (TextView) layout.findViewById(R.id.txtApplyDatepop);
                    TextView txtVAStartDatepop = (TextView) layout.findViewById(R.id.txtVAStartDatepop);
                    TextView txtVAEndDatepop = (TextView) layout.findViewById(R.id.txtVAEndDatepop);
                    TextView txtVAnumberOfDaypop = (TextView) layout.findViewById(R.id.txtVAnumberOfDaypop);
                    TextView txtVAReasonpop = (TextView) layout.findViewById(R.id.txtVAReasonpop);
                    TextView txtStatuspop = (TextView) layout.findViewById(R.id.txtStatuspop);
                    RadioGroup radioGroup = (RadioGroup) layout.findViewById(R.id.radioGroup);
                    RadioButton radioButtonApproved = (RadioButton) layout.findViewById(R.id.radioButtonApproved);
                    radioButtonApproved.setChecked(true);
                    getRadioString = "Approved";
                    Button btnCancel = (Button) layout.findViewById(R.id.btnpopcancel);
                    Button btnSubmit = (Button) layout.findViewById(R.id.btnpopsubmit);
                    builder.setView(layout);

                    txtVANamepop.setText("Name: Nilesh Dhola");
                    txtApplyDatepop.setText("Apply Date: " + constants.getTime());
                    txtVAStartDatepop.setText("Start Date: " + constants.getStartDate());
                    txtVAEndDatepop.setText("End Date: " + constants.getEndDate());
                    txtVAnumberOfDaypop.setText("No of days: 3 days");
                    txtVAReasonpop.setText("Reason: " + constants.getReason());
                    txtStatuspop.setText("Status: " + constants.getLeaveOdStatus());

                    final AlertDialog ad = builder.show();

                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            RadioButton rb = (RadioButton) group.findViewById(checkedId);

                            if (null != rb && checkedId > -1) {
                                //  Toast.makeText(context, rb.getText(), Toast.LENGTH_SHORT).show();
                                getRadioString = rb.getText().toString();
                            }

                        }
                    });

                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ad.dismiss();


                        }
                    });
                    btnSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(context, "Submit" + getRadioString, Toast.LENGTH_SHORT).show();
                        }
                    });

                    ad.show();
                }
            });
        } catch (Exception e) {
            e.getMessage();
        }


    }


    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }

    public Locale getFilter() {
        return null;
    }

    public void filter(String charText) {
        charText = charText;
        data.clear();
        if (charText.equals("ALL")) {
            data.addAll(arraylist);
        } else if (charText.length() == 0) {
            data.addAll(arraylist);
        } else {
            for (LeaveOD wp : arraylist) {
                if (wp.getLeaveOdStatus().contains(charText)) {
                    data.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }


    class MyHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView txtVAName;
        TextView txtApplyDate;
        TextView txtVAStartDate;
        TextView txtVAEndDate;
        TextView txtVAnumberOfDay;
        TextView txtVAReason;
        ImageView ImgStatus;
        LinearLayout linerODClick;


        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);

            cv = (CardView) itemView.findViewById(R.id.card_view);

            txtVAName = (TextView) itemView.findViewById(R.id.txtVAName);
            txtApplyDate = (TextView) itemView.findViewById(R.id.txtApplyDate);
            txtVAStartDate = (TextView) itemView.findViewById(R.id.txtVAStartDate);
            txtVAEndDate = (TextView) itemView.findViewById(R.id.txtVAEndDate);
            txtVAnumberOfDay = (TextView) itemView.findViewById(R.id.txtVAnumberOfDay);
            txtVAReason = (TextView) itemView.findViewById(R.id.txtVAReason);
            ImgStatus = (ImageView) itemView.findViewById(R.id.ImgStatus);
            linerODClick = (LinearLayout) itemView.findViewById(R.id.linerODClick);
        }

    }


}
