package in.vakrangee.core.commongui.status;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import in.vakrangee.core.R;
import in.vakrangee.core.commongui.imagegallery.RecyclerViewClickListener;
import in.vakrangee.core.utils.DeprecateHandler;

public class NameStatusAdapter extends RecyclerView.Adapter<NameStatusAdapter.MyViewHolder> {

    private static final String TAG = "NameStatusAdapter";
    public List<NameStatusDto> nameStatusList;
    private Context context;
    private RecyclerViewClickListener mListener;
    private DeprecateHandler deprecateHandler;

    public NameStatusAdapter(Context context, List<NameStatusDto> statusDtoList, RecyclerViewClickListener listener) {
        this.context = context;
        this.nameStatusList = statusDtoList;
        this.mListener = listener;
        deprecateHandler = new DeprecateHandler(context);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtStatus;
        public LinearLayout layoutStatus;
        public LinearLayout layoutParent;
        public LinearLayout layoutName;
        public TextView txtName;

        public MyViewHolder(View view, RecyclerViewClickListener listener) {
            super(view);
            layoutParent = view.findViewById(R.id.layoutParent);
            layoutStatus = view.findViewById(R.id.layoutStatus);
            txtStatus = view.findViewById(R.id.txtStatus);
            layoutName = view.findViewById(R.id.layoutName);
            txtName = view.findViewById(R.id.name);
            mListener = listener;
            layoutParent.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(view, getAdapterPosition());
        }
    }

    @Override
    public NameStatusAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_name_with_status, parent, false);
        return new NameStatusAdapter.MyViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(NameStatusAdapter.MyViewHolder holder, int position) {

        final NameStatusDto nameStatusDto = nameStatusList.get(position);

        holder.txtName.setText(nameStatusDto.getName());
        holder.txtStatus.setText(nameStatusDto.getStatus());

        //Set Work Status Color
        GradientDrawable bgWorkShape = (GradientDrawable) holder.txtStatus.getBackground();

        switch (nameStatusDto.getStatusCode().toLowerCase()) {

            case "70":
                changeDrawable(bgWorkShape, deprecateHandler.getColor(R.color.green), 0, deprecateHandler.getColor(R.color.green));
                holder.txtStatus.setTextColor(deprecateHandler.getColor(R.color.white));
                break;

            case "60":
                //case "already delivered":
                changeDrawable(bgWorkShape, deprecateHandler.getColor(R.color.orange), 0, deprecateHandler.getColor(R.color.orange));
                holder.txtStatus.setTextColor(deprecateHandler.getColor(R.color.white));
                break;

            default:
                changeDrawable(bgWorkShape, deprecateHandler.getColor(R.color.gray), 0, deprecateHandler.getColor(R.color.gray));
                holder.txtStatus.setTextColor(deprecateHandler.getColor(R.color.white));
                break;
        }
        holder.txtName.setTag(nameStatusDto);
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
        return nameStatusList.size();
    }

}
