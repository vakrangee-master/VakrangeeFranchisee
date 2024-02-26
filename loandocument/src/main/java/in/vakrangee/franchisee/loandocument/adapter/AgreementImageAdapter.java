package in.vakrangee.franchisee.loandocument.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.RecyclerView;
import in.vakrangee.franchisee.loandocument.R;
import in.vakrangee.franchisee.loandocument.drag_drop_utils.ItemTouchHelperAdapter;
import in.vakrangee.franchisee.loandocument.drag_drop_utils.ItemTouchHelperViewHolder;
import in.vakrangee.franchisee.loandocument.drag_drop_utils.OnStartDragListener;
import in.vakrangee.franchisee.loandocument.model.AgreementImageDto;
import in.vakrangee.supercore.franchisee.commongui.imagegallery.RecyclerViewClickLongClickListener;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;

public class AgreementImageAdapter extends RecyclerView.Adapter<AgreementImageAdapter.MyViewHolder> implements ItemTouchHelperAdapter {

    private static final String TAG = "AgreementImageAdapter";
    private List<AgreementImageDto> photoDtos;
    private Context context;
    private View itemView;
    private DeprecateHandler deprecateHandler;
    private OnStartDragListener mDragStartListener;
    private RecyclerViewClickLongClickListener mListener;
    private boolean IsLongClick = false;
    private IHandleChk iHandleChk;

    public AgreementImageAdapter(Context context, List<AgreementImageDto> photoDtos, OnStartDragListener dragListner, RecyclerViewClickLongClickListener listener, IHandleChk iHandleChk) {
        this.context = context;
        this.photoDtos = photoDtos;
        deprecateHandler = new DeprecateHandler(context);
        this.mDragStartListener = dragListner;
        this.mListener = listener;
        this.iHandleChk = iHandleChk;
    }

    public interface IHandleChk {

        public void onCheckEvent(int pos, boolean IsChecked);
    }

    @Override
    public void onItemDismiss(int position) {
        photoDtos.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < photoDtos.size() && toPosition < photoDtos.size()) {
            Collections.swap(photoDtos, fromPosition, toPosition);
            notifyDataSetChanged();
        }
        return true;
    }

    public void updateList(List<AgreementImageDto> list) {
        photoDtos = list;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder, View.OnClickListener, View.OnLongClickListener {

        private TextView txtAgreementName;
        private ImageView imgAgreement;
        public LinearLayout layoutParent, layoutEdit;
        public CheckBox chkBoxSelected;

        public MyViewHolder(View view) {
            super(view);
            txtAgreementName = view.findViewById(R.id.txtAgreementName);
            imgAgreement = view.findViewById(R.id.imgAgreement);
            layoutParent = view.findViewById(R.id.layoutParent);
            chkBoxSelected = view.findViewById(R.id.chkBoxSelected);
            layoutEdit = view.findViewById(R.id.layout_Edit);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onItemSelected() {

        }

        @Override
        public void onItemClear() {

        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onClick(v, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if (mListener != null) {
                mListener.onLongClick(view, getAdapterPosition());
            }
            return true;
        }
    }

    @Override
    public AgreementImageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_document_img_cell, parent, false);
        return new AgreementImageAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AgreementImageAdapter.MyViewHolder holder, final int position) {

        final AgreementImageDto agreementImageDto = photoDtos.get(position);
        int count = agreementImageDto.getName().length();

        String actualName = count == 1 ? "0" + agreementImageDto.getName() : agreementImageDto.getName();

        int len = String.valueOf(position + 1).length();
        String name = len == 1 ? "0" + String.valueOf(position + 1) : String.valueOf(position + 1);

        holder.txtAgreementName.setText(name);

        //Set Thumbnail
        if (!TextUtils.isEmpty(agreementImageDto.getImgBase64())) {
            Bitmap imageBitmap = CommonUtils.StringToBitMap(agreementImageDto.getImgBase64());
            holder.imgAgreement.setImageBitmap(imageBitmap);

        } else {
            Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.ic_camera_alt_black_72dp)).into(holder.imgAgreement);
        }

        //DragNDrop Touch Listener
        holder.layoutParent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });

        holder.chkBoxSelected.bringToFront();
        holder.chkBoxSelected.setTag(position);
        holder.chkBoxSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean IsChecked) {
                int pos = (int) compoundButton.getTag();
                if (IsChecked)
                    photoDtos.get(pos).setSelected(true);
                else
                    photoDtos.get(pos).setSelected(false);
                iHandleChk.onCheckEvent(pos, IsChecked);
            }
        });

        //Selected Checkbox
        // To avoid CheckedChange Listener
        if (agreementImageDto.isSelected()) {
            holder.chkBoxSelected.setChecked(true);
        } else {
            holder.chkBoxSelected.setChecked(false);
        }

        if (IsLongClick) {
            holder.chkBoxSelected.setVisibility(View.VISIBLE);
        } else {
            holder.chkBoxSelected.setVisibility(View.GONE);
        }

        //Edit
        holder.layoutEdit.setTag(position);
        holder.layoutEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                mListener.onClick(v, pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return photoDtos.size();
    }

    public void enableDisableChkBoxNotify(boolean IsChkVisible) {
        this.IsLongClick = IsChkVisible;
        notifyDataSetChanged();
    }
}
