package in.vakrangee.franchisee.franchiseelogin;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.commongui.imagegallery.RecyclerViewClickListener;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;

public class FranchiseeEnquiryDetailsAdapter extends RecyclerView.Adapter<FranchiseeEnquiryDetailsAdapter.MyViewHolder> {

    private static final String TAG = "FranchiseeEnquiryDetailsAdapter";
    public List<FranchiseeEnquiryDto> enquiryList;
    private Context context;
    private RecyclerViewClickListener mListener;
    private DeprecateHandler deprecateHandler;
    private RadioButton radioButton;
    private final String STATUS_PARTIALLY_FILLED = "Application Partially Filled";
    private final String STATUS_COMPLETED = "Application Completed";
    private final String STATUS_PENDING = "Application Pending";
    private Animation animationUp;
    private Animation animationDown;

    public FranchiseeEnquiryDetailsAdapter(Context context, List<FranchiseeEnquiryDto> enquiryList, RecyclerViewClickListener listener) {
        this.context = context;
        this.enquiryList = enquiryList;
        this.mListener = listener;
        deprecateHandler = new DeprecateHandler(context);
        animationUp = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        animationDown = AnimationUtils.loadAnimation(context, R.anim.slide_down);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtStatus;
        public LinearLayout layoutStatus;
        public TextView txtEnquiryDate;
        public LinearLayout layoutName;
        public TextView txtName;
        public RadioButton radioButtonSelection;
        public LinearLayout layoutParent;

        public LinearLayout linearEnquiryDetail;
        public TextView textViewEnqAppStatus;
        public MaterialButton btnEnquiryDetail;
        public LinearLayout linearEnquirySubDetail;
        public TextView textViewEnqEmail;
        public TextView textViewEnqPhone;
        public TextView textViewEnqAddress;
        public LinearLayout layoutApplicationStatus;

        public MyViewHolder(View view, RecyclerViewClickListener listener) {
            super(view);

            layoutParent = view.findViewById(R.id.layoutParent);
            radioButtonSelection = view.findViewById(R.id.radioButtonSelection);
            layoutStatus = view.findViewById(R.id.layoutStatus);
            txtStatus = view.findViewById(R.id.txtStatus);
            layoutName = view.findViewById(R.id.layoutName);
            txtName = view.findViewById(R.id.name);
            txtEnquiryDate = view.findViewById(R.id.txtEnquiryDate);
            layoutApplicationStatus = view.findViewById(R.id.layoutApplicationStatus);
            mListener = listener;
            layoutParent.setOnClickListener(this);
            //radioButtonSelection.setOnClickListener(this);

            // Enquiry and Application Detail Objects
            linearEnquiryDetail = view.findViewById(R.id.linearEnquiryDetail);
            textViewEnqAppStatus = view.findViewById(R.id.textViewEnqAppStatus);
            btnEnquiryDetail = view.findViewById(R.id.btnEnquiryDetail);
            linearEnquirySubDetail = view.findViewById(R.id.linearEnquirySubDetail);
            textViewEnqEmail = view.findViewById(R.id.textViewEnqEmail);
            textViewEnqPhone = view.findViewById(R.id.textViewEnqPhone);
            textViewEnqAddress = view.findViewById(R.id.textViewEnqAddress);

        }

        @Override
        public void onClick(View view) {
            radioButtonSelection.performClick();
        }
    }

    @Override
    public FranchiseeEnquiryDetailsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_franchisee_enquiry_details, parent, false);
        return new FranchiseeEnquiryDetailsAdapter.MyViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(final FranchiseeEnquiryDetailsAdapter.MyViewHolder holder, int position) {

        final FranchiseeEnquiryDto enquiryDto = enquiryList.get(position);

        holder.txtName.setText(enquiryDto.getName());
        holder.txtEnquiryDate.setText(enquiryDto.getEnquiryDate());

        boolean IsChecked = enquiryDto.isSelected();
        if (IsChecked)
            holder.radioButtonSelection.setChecked(true);
        else
            holder.radioButtonSelection.setChecked(false);

        holder.radioButtonSelection.setTag(enquiryDto);
        holder.radioButtonSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FranchiseeEnquiryDto dto = (FranchiseeEnquiryDto) view.getTag();
                dto.setSelected(true);

                RadioButton radioButtonS = view.findViewById(R.id.radioButtonSelection);
                if (radioButton != null && radioButtonS != radioButton) {
                    radioButton.setChecked(false);

                    FranchiseeEnquiryDto prevDto = (FranchiseeEnquiryDto) radioButton.getTag();
                    prevDto.setSelected(false);
                }

                radioButton = radioButtonS;
                mListener.onClick(view, holder.getAdapterPosition());
            }
        });

        //holder.txtStatus.setText(nameStatusDto.getStatus());

        // Set Enquiry and Application Detial
        String appStatus = TextUtils.isEmpty(enquiryDto.getStatus()) ? STATUS_PENDING : enquiryDto.getStatus();
        holder.textViewEnqAppStatus.setText(appStatus);
        holder.textViewEnqEmail.setText(enquiryDto.getEmailId());
        holder.textViewEnqPhone.setText(enquiryDto.getMobileNo());
        holder.textViewEnqAddress.setText(enquiryDto.getAddress());
        holder.btnEnquiryDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.linearEnquirySubDetail.getVisibility() == View.GONE) {
                    holder.linearEnquirySubDetail.setVisibility(View.VISIBLE);
                    //  holder.linearEnquirySubDetail.startAnimation(animationDown);
                    holder.btnEnquiryDetail.setText("less");
                    //holder.btnEnquiryDetail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_up_black_24dp, 0);

                } else {
                    holder.linearEnquirySubDetail.setVisibility(View.GONE);
                    //  holder.linearEnquirySubDetail.startAnimation(animationUp);
                    holder.btnEnquiryDetail.setText("more");
                    //holder.btnEnquiryDetail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_down_black_24dp, 0);
                }
            }
        });

        //Set Status
        setBackgroundAsPerStatus(appStatus.toLowerCase(), holder.textViewEnqAppStatus, holder.layoutApplicationStatus);

        holder.txtName.setTag(enquiryDto);
    }

    public void setBackgroundAsPerStatus(String status, TextView txtStatus, LinearLayout layoutStatusBg) {
        if (STATUS_COMPLETED.toLowerCase().equalsIgnoreCase(status)) {
            txtStatus.setTextColor(deprecateHandler.getColor(R.color.white));
            layoutStatusBg.setBackgroundColor(deprecateHandler.getColor(R.color.green));

        } else if (STATUS_PARTIALLY_FILLED.toLowerCase().equalsIgnoreCase(status)) {
            txtStatus.setTextColor(deprecateHandler.getColor(R.color.white));
            layoutStatusBg.setBackgroundColor(deprecateHandler.getColor(R.color.orange));

        } else if (STATUS_PENDING.toLowerCase().equalsIgnoreCase(status)) {
            txtStatus.setTextColor(deprecateHandler.getColor(R.color.black));
            layoutStatusBg.setBackgroundColor(deprecateHandler.getColor(R.color.grey));

        }
    }

    public void changeDrawable(GradientDrawable drawable, int solidColor, int strokeWidth, int strokeColor) {
        if (drawable != null) {

            drawable.setColor(solidColor);
            if (strokeWidth > 0)
                drawable.setStroke(strokeWidth, strokeColor);

        }
    }

    @Override
    public int getItemCount() {
        return enquiryList.size();
    }
}
