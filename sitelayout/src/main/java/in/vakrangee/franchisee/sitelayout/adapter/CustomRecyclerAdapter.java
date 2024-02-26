package in.vakrangee.franchisee.sitelayout.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.vakrangee.franchisee.sitelayout.R;
import in.vakrangee.franchisee.sitelayout.fragment.NextGenSiteVisitFragment;
import in.vakrangee.supercore.franchisee.model.FranchiseeDetails;

public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder> {
    int selectedPosition = -1;
    private Context context;
    private List<NextGenSiteVisitFragment.PremiseShape> personUtils;
    FranchiseeDetails franchiseeDetails;
    boolean enabled = true;

    public CustomRecyclerAdapter(Context context, List<NextGenSiteVisitFragment.PremiseShape> personUtils, FranchiseeDetails franchiseeDetails) {
        this.context = context;
        this.personUtils = personUtils;
        this.franchiseeDetails = franchiseeDetails;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.premiseadapdterlayout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.itemView.setTag(personUtils.get(position));

        final NextGenSiteVisitFragment.PremiseShape pu = personUtils.get(position);

        holder.pName.setText(pu.getPersonName());

        holder.pJobProfile.setImageResource(pu.getJobProfile());


        if (selectedPosition == position) {
            holder.linearprimse.setBackgroundColor(context.getResources().getColor(R.color.gray));
        } else {
            if (franchiseeDetails.getPremiseShape() != null && !franchiseeDetails.getPremiseShape().isEmpty()) {

                int id = Integer.valueOf(franchiseeDetails.getPremiseShape()) - 1;
                if (id == position) {
                    holder.linearprimse.setBackgroundColor(context.getResources().getColor(R.color.gray));
                } else {
                    holder.linearprimse.setBackgroundColor(Color.parseColor("#ffffff"));
                }
            } else {
                holder.linearprimse.setBackgroundColor(Color.parseColor("#ffffff"));
            }
        }
        holder.linearprimse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Disable
                if (!enabled)
                    return;

                selectedPosition = position;
                franchiseeDetails.setPremiseShape(pu.getPersonName());
                notifyDataSetChanged();

            }
        });
    }


    @Override
    public int getItemCount() {
        return personUtils.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView pName;
        public ImageView pJobProfile;
        public LinearLayout linearprimse;

        public ViewHolder(View itemView) {
            super(itemView);

            pName = (TextView) itemView.findViewById(R.id.name);
            pJobProfile = (ImageView) itemView.findViewById(R.id.image);
            linearprimse = (LinearLayout) itemView.findViewById(R.id.linearprimse);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    NextGenSiteVisitFragment.PremiseShape cpu = (NextGenSiteVisitFragment.PremiseShape) view.getTag();

                    Toast.makeText(view.getContext(), cpu.getPersonName() + " is " + cpu.getJobProfile(), Toast.LENGTH_SHORT).show();

                }
            });

        }
    }

    public void setEnable(boolean enabled) {
        this.enabled = enabled;
    }

}