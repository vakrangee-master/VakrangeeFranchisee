package in.vakrangee.franchisee.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.commongui.animation.AnimationHanndler;

class RecyclerViewDashboardMenuAdapter extends RecyclerView.Adapter<RecyclerViewDashboardMenuAdapter.ViewHolder> {
    private Context context;
    private ArrayList<DashboardMenuModel> dashboardMenuModels;
    private Typeface font;
    private OnItemClicked onClick;

    public RecyclerViewDashboardMenuAdapter(Context context, ArrayList arrayList, OnItemClicked onClick) {
        this.context = context;
        this.dashboardMenuModels = arrayList;
        font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        this.onClick = onClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_dashboard_menu, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle, txtMenuIcon;
        public LinearLayout layout_dashboard_menu;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.textView_dashboard_menu_title);
            txtMenuIcon = (TextView) itemView.findViewById(R.id.txtMenuIcon);
            layout_dashboard_menu = (LinearLayout) itemView.findViewById(R.id.layout_dashboard_menu);


        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final DashboardMenuModel item = dashboardMenuModels.get(position);
        holder.txtTitle.setText(item.getDashboarddname());
        holder.txtMenuIcon.setTypeface(font);

        holder.txtMenuIcon.setText(new String(Character.toChars(Integer.parseInt(
                item.getDashborddrawable(), 16))));

        holder.layout_dashboard_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationHanndler.bubbleAnimation(context, view);
                onClick.onItemClick(position);
            }
        });

    }


    @Override
    public int getItemCount() {
        return dashboardMenuModels.size();
    }

    //make interface like this
    public interface OnItemClicked {
        void onItemClick(int position);
    }
}
