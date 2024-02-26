package in.vakrangee.core.commongui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import in.vakrangee.core.R;
import in.vakrangee.core.commongui.imagepreview.CustomImagePreviewDialog;
import in.vakrangee.core.commongui.imagepreview.ImagePreview_zoom_Dialog;
import in.vakrangee.core.model.ElementAttributeDetailDto;
import in.vakrangee.core.utils.Constants;
import in.vakrangee.core.utils.DeprecateHandler;

public class ElementAttributeAdapter extends RecyclerView.Adapter<ElementAttributeAdapter.MyViewHolder> {

    private static final String TAG = "ElementAttributeAdapter";
    private List<ElementAttributeDetailDto> elementAttributeList;
    private DeprecateHandler deprecateHandler;
    private Context context;
    private View itemView;
    private CustomImagePreviewDialog customImagePreviewDialog;
    private String title;
    private ImagePreview_zoom_Dialog imagePreview_zoom_dialog;

    public ElementAttributeAdapter(Context context, String title, List<ElementAttributeDetailDto> elementAttributeList) {
        this.context = context;
        this.title = title;
        this.elementAttributeList = elementAttributeList;
        deprecateHandler = new DeprecateHandler(context);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtElementAttributeName, txtElementAttributeValue, txtElementAttributeUnit;
        public ImageView imgElementAttribute;
        private LinearLayout layoutAttributeImage;

        public MyViewHolder(View view) {
            super(view);
            txtElementAttributeName = view.findViewById(R.id.txtElementAttributeName);
            txtElementAttributeValue = view.findViewById(R.id.txtElementAttributeValue);
            txtElementAttributeUnit = view.findViewById(R.id.txtElementAttributeUnit);
            imgElementAttribute = view.findViewById(R.id.imgElementImage);
            layoutAttributeImage = view.findViewById(R.id.layoutAttributeImage);
        }
    }

    @Override
    public ElementAttributeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_element_attribute_cell, parent, false);
        return new ElementAttributeAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ElementAttributeAdapter.MyViewHolder holder, int position) {

        final ElementAttributeDetailDto attributeDetailDto = elementAttributeList.get(position);

        //Prepare Element name with Unit
        String name = "";

        if (TextUtils.isEmpty(attributeDetailDto.getAttributeUnit()))
            name = attributeDetailDto.getAttributeName();
        else
            name = attributeDetailDto.getAttributeName() + " (" + attributeDetailDto.getAttributeUnit() + ")";

        holder.txtElementAttributeName.setText(name);
        holder.txtElementAttributeValue.setText(attributeDetailDto.getAttributeVallue());
        holder.txtElementAttributeUnit.setText(attributeDetailDto.getAttributeUnit());

        //Image Attribute
        if (TextUtils.isEmpty(attributeDetailDto.getAttributeImageId()))
            holder.imgElementAttribute.setVisibility(View.GONE);
        else
            setImage(attributeDetailDto.getAttributeImageId(), holder.imgElementAttribute);

        holder.layoutAttributeImage.setTag(attributeDetailDto);
        holder.layoutAttributeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ElementAttributeDetailDto detailDto = (ElementAttributeDetailDto) view.getTag();
                showImagePreviewDialog(detailDto);
            }
        });

        holder.txtElementAttributeName.setTag(attributeDetailDto);

    }

    public void setImage(String imageId, final ImageView imageView) {
        imageView.setVisibility(View.VISIBLE);
        String imageUrl = Constants.DownloadImageUrl + imageId;

        Glide.with(context)
                .load(imageUrl)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        imageView.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return elementAttributeList.size();
    }

    // Allow Change Dialog Title Name
    public void setDialogTitle(String title) {

        if (!TextUtils.isEmpty(title)) {
            this.title = title;
        }
    }

    private void showImagePreviewDialog(Object object) {
        String imgId = ((ElementAttributeDetailDto) object).getAttributeImageId();
        if (imgId != null && imgId != "") {
            imagePreview_zoom_dialog = new ImagePreview_zoom_Dialog(context, imgId, title) {

            };
        } else {
            Toast.makeText(context, "No photo available to preview.", Toast.LENGTH_SHORT).show();
        }
        imagePreview_zoom_dialog.show();
        imagePreview_zoom_dialog.setCancelable(false);
    }
}
