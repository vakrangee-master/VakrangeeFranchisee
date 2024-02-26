package in.vakrangee.franchisee.gwr;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.application.VakrangeeKendraApplication;
import in.vakrangee.supercore.franchisee.model.GWRCheckListDto;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;

public class GWRCategoryAdapter extends RecyclerView.Adapter<GWRCategoryAdapter.MyViewHolder> {

    private static final String TAG = "GWRCategoryAdapter";
    private List<GWRCategoryDto> categoryList;
    private Context context;
    private View itemView;
    private DeprecateHandler deprecateHandler;
    private GWRInteriorCellAdapter interiorChecklistAdapter;
    private GWRCategoryAdapter.ISiteCheckHandler iSiteCheckHandler;
    private String DOWN_ARROW, UP_ARROW;
    private RecyclerView previousRecyclerView;
    private TextView textViewPrevious;
    private int refreshedPosition = 0;
    private String ATTRIBUTE_TYPE;
    private boolean IsFranchisee = false;

    public interface ISiteCheckHandler {
        public void cameraClick(int parentPosition, int position, GWRCheckListDto siteReadinessCheckListDto);
    }

    public GWRCategoryAdapter(Context context, boolean IsFranchisee, String type, List<GWRCategoryDto> categoryList, GWRCategoryAdapter.ISiteCheckHandler iSiteCheckHandler) {
        this.context = context;
        this.IsFranchisee = IsFranchisee;
        this.ATTRIBUTE_TYPE = type;
        this.categoryList = categoryList;
        this.iSiteCheckHandler = iSiteCheckHandler;
        deprecateHandler = new DeprecateHandler(context);
        DOWN_ARROW = context.getResources().getString(R.string.fa_down_arrow);
        UP_ARROW = context.getResources().getString(R.string.fa_up_arrow);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtCategoryLayoutIcon;
        public TextView txtCategoryName;
        public TextView txtCategoryStatus;
        public TextView txtCategoryLayoutArrow;
        public RecyclerView recyclerViewCategoryLayout;
        public LinearLayout layoutCategoryClick;

        public MyViewHolder(View view) {
            super(view);
            txtCategoryLayoutIcon = view.findViewById(R.id.txtCategoryLayoutIcon);
            layoutCategoryClick = view.findViewById(R.id.layoutCategoryClick);
            txtCategoryName = view.findViewById(R.id.txtCategoryLayoutTitle);
            txtCategoryStatus = view.findViewById(R.id.txtCategoryLayoutStatus);
            txtCategoryLayoutArrow = view.findViewById(R.id.txtCategoryLayoutArrow);
            recyclerViewCategoryLayout = view.findViewById(R.id.recyclerViewCategoryLayout);
        }
    }

    @Override
    public GWRCategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_interior_category, parent, false);
        return new GWRCategoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final GWRCategoryAdapter.MyViewHolder holder, int position) {

        final GWRCategoryDto dto = categoryList.get(position);
        holder.txtCategoryName.setText(dto.getName());

        //Check list Recycler view
        interiorChecklistAdapter = new GWRInteriorCellAdapter(context, IsFranchisee, ATTRIBUTE_TYPE, position, dto.checkList, new GWRInteriorCellAdapter.ISiteCheckHandler() {
            @Override
            public void cameraClick(int parentPosition, int position, GWRCheckListDto siteReadinessCheckListDto) {
                iSiteCheckHandler.cameraClick(parentPosition, position, siteReadinessCheckListDto);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.recyclerViewCategoryLayout.setLayoutManager(layoutManager);
        holder.recyclerViewCategoryLayout.setItemAnimator(new DefaultItemAnimator());
        holder.recyclerViewCategoryLayout.setAdapter(interiorChecklistAdapter);
        holder.recyclerViewCategoryLayout.setNestedScrollingEnabled(false);
        holder.recyclerViewCategoryLayout.setTag(position);

        if (position == refreshedPosition) {
            holder.recyclerViewCategoryLayout.setVisibility(View.VISIBLE);
            setFontawesomeIcon(holder.txtCategoryLayoutArrow, UP_ARROW);

            textViewPrevious = holder.txtCategoryLayoutArrow;
            previousRecyclerView = holder.recyclerViewCategoryLayout;
        } else {
            holder.recyclerViewCategoryLayout.setVisibility(View.GONE);
            setFontawesomeIcon(holder.txtCategoryLayoutArrow, DOWN_ARROW);
        }

        //Change color of status
        if (!TextUtils.isEmpty(dto.getStatus())) {
            switch (dto.getStatus()) {
                case "0":
                    holder.txtCategoryStatus.setBackgroundColor(deprecateHandler.getColor(R.color.orange));
                    break;
                case "1":
                    holder.txtCategoryStatus.setBackgroundColor(deprecateHandler.getColor(R.color.ired));
                    break;
                case "2":
                    holder.txtCategoryStatus.setBackgroundColor(deprecateHandler.getColor(R.color.green));
                    break;

                default:
                    holder.txtCategoryStatus.setBackgroundColor(deprecateHandler.getColor(R.color.orange));
                    break;
            }
        } else {
            holder.txtCategoryStatus.setBackgroundColor(deprecateHandler.getColor(R.color.orange));
        }

        //TExtView Click
        holder.layoutCategoryClick.setTag(position);
        holder.layoutCategoryClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                ViewGroup topParent = (ViewGroup) view.getParent();
                RecyclerView recyclerView = topParent.findViewById(R.id.recyclerViewCategoryLayout);
                TextView txtArrow = view.findViewById(R.id.txtCategoryLayoutArrow);

                if (recyclerView.getVisibility() == View.VISIBLE) {
                    recyclerView.setVisibility(View.GONE);
                    setFontawesomeIcon(txtArrow, DOWN_ARROW);

                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    setFontawesomeIcon(txtArrow, UP_ARROW);
                    recyclerView.smoothScrollToPosition(pos);

                    collapseOtherViews(pos, holder.txtCategoryLayoutArrow, holder.recyclerViewCategoryLayout);
                }
            }
        });
        holder.txtCategoryName.setTag(position);
    }

    public void collapseOtherViews(int pos, TextView txtCategoryLayoutArrow, RecyclerView recyclerView) {
        if (previousRecyclerView != null && textViewPrevious != null) {
            int prevPos = (int) previousRecyclerView.getTag();
            if (prevPos != pos) {
                previousRecyclerView.setVisibility(View.GONE);
                setFontawesomeIcon(textViewPrevious, DOWN_ARROW);
                textViewPrevious = txtCategoryLayoutArrow;
                previousRecyclerView = recyclerView;
            }
        }
    }

    public void updateRefreshedPosition(int pos) {
        refreshedPosition = pos;
    }

    public void disableOtherViews(int selectedPos) {
        int size = getItemCount();
        for (int i = 0; i < size; i++) {
            getItemId(selectedPos);

        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void setFontawesomeIcon(TextView textView, String icon) {
        textView.setText(icon);
        textView.setTextSize(25);
        textView.setTextColor(deprecateHandler.getColor(R.color.white));
        textView.setTypeface(VakrangeeKendraApplication.font_awesome, Typeface.BOLD);

    }
}
