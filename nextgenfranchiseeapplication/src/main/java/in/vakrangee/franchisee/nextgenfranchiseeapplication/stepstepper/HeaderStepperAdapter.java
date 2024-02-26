package in.vakrangee.franchisee.nextgenfranchiseeapplication.stepstepper;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.vakrangee.franchisee.nextgenfranchiseeapplication.R;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;

public class HeaderStepperAdapter extends RecyclerView.Adapter<HeaderStepperAdapter.MyViewHolder> {

    private ArrayList<HeaderStepperDTO> headerStepperDTOS;
    private Context context;
    private int row_index;
    private ifcStepperItemClick stepperItemClick;
    private DeprecateHandler deprecateHandler;

    public HeaderStepperAdapter(Context context, ArrayList<HeaderStepperDTO> headerStepperDTOS,ifcStepperItemClick ifcStepperItemClicka) {
        this.context = context;
        this.headerStepperDTOS = headerStepperDTOS;
        this.stepperItemClick = ifcStepperItemClicka;
        deprecateHandler = new DeprecateHandler(context);
    }

    //View holder - find Id
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView StepperName, StepperID;
        private View view_line;
        private LinearLayout lin_layout_adapter;
        private ImageView imgDone;

        public MyViewHolder(View view) {
            super(view);
            StepperName = (TextView) view.findViewById(R.id.tvHeaderName);
            StepperID = (TextView) view.findViewById(R.id.tvHeaderId);
            view_line = (View) view.findViewById(R.id.view_line);
            lin_layout_adapter = (LinearLayout) view.findViewById(R.id.layout_adapter);
            imgDone = view.findViewById(R.id.imgDone);

        }

        @Override
        public void onClick(View view) {
            stepperItemClick.onItemClick(getAdapterPosition(), view, headerStepperDTOS);
        }
    }

    //Set view layout
    @Override
    public HeaderStepperAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_steeper_adapter_layout, parent, false);
        return new HeaderStepperAdapter.MyViewHolder(itemView);
    }

    //Bind data
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final HeaderStepperDTO stepperDTO = headerStepperDTOS.get(position);
        holder.StepperName.setText(stepperDTO.getStepperName());
        holder.StepperID.setText(stepperDTO.getStpeerId());

        holder.StepperName.setTypeface(holder.StepperName.getTypeface(), Typeface.NORMAL);
        holder.StepperName.setTextColor(context.getResources().getColor(R.color.grey));

        //Line draw -when last position item line Gone
        if (position == (getItemCount() - 1)) {
            holder.view_line.setVisibility(View.GONE);
        } else {
            holder.view_line.setVisibility(View.VISIBLE);
        }

        //when linear layout item click - notify data changed
        holder.lin_layout_adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = position;
                stepperItemClick.onItemClick(row_index, view, headerStepperDTOS);
                notifyDataSetChanged();
            }
        });

        if (row_index == position) {
            holder.StepperName.setTypeface(holder.StepperName.getTypeface(), Typeface.BOLD);
            holder.StepperName.setTextColor(context.getResources().getColor(R.color.black));
        } else {
            holder.StepperName.setTypeface(holder.StepperName.getTypeface(), Typeface.NORMAL);
            holder.StepperName.setTextColor(context.getResources().getColor(R.color.gray));
        }

        //Set Indicator
        GradientDrawable bgShape = (GradientDrawable) holder.StepperID.getBackground();
        setCircleIndicator(stepperDTO, bgShape, holder.imgDone);
    }

    public void notify(int pos) {
        row_index = pos;
        notifyDataSetChanged();
    }

    public void changeDrawable(GradientDrawable drawable, int solidColor) {
        if (drawable != null) {

            drawable.setColor(solidColor);
        }
    }

    @Override
    public int getItemCount() {
        return headerStepperDTOS.size();
    }

    public void setCircleIndicator(HeaderStepperDTO stepperDTO, GradientDrawable bgShape, ImageView imageView) {

        //STEP 1: Completed
        if (stepperDTO.isAllEnterValidated()) {
            changeDrawable(bgShape, deprecateHandler.getColor(R.color.green));
            return;
        }

        //STEP 2: Partial
        if (stepperDTO.isPartiallyfilled()) {
            changeDrawable(bgShape, deprecateHandler.getColor(R.color.orange));
            return;
        }

        imageView.setVisibility(View.GONE);
        changeDrawable(bgShape, deprecateHandler.getColor(R.color.gray));

    }

}
