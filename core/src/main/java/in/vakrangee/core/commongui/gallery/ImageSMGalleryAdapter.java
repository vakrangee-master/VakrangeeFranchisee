package in.vakrangee.core.commongui.gallery;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import in.vakrangee.core.R;
import in.vakrangee.core.commongui.imagegallery.ImageDto;
import in.vakrangee.core.commongui.imagegallery.RecyclerViewClickListener;
import in.vakrangee.core.utils.Constants;
import in.vakrangee.core.utils.DeprecateHandler;

public class ImageSMGalleryAdapter extends RecyclerView.Adapter<ImageSMGalleryAdapter.MyViewHolder> {

    private static final String TAG = "ImageThumbnailAdapter";
    public List<ImageDto> imageList;
    private Context context;
    private ImageSMGalleryAdapter.IImageRemove iImageRemove;
    private RecyclerViewClickListener mListener;
    public boolean isCancelEnabled = true;
    private DeprecateHandler deprecateHandler;
    private boolean IsAllowGlide = false;

    public ImageSMGalleryAdapter(Context context, List<ImageDto> imageList, RecyclerViewClickListener listener, ImageSMGalleryAdapter.IImageRemove iImageRemove) {
        this.context = context;
        this.imageList = imageList;
        this.iImageRemove = iImageRemove;
        this.mListener = listener;
        deprecateHandler = new DeprecateHandler(context);
    }

    public ImageSMGalleryAdapter(Context context, boolean IsAllowGlide, List<ImageDto> imageList, RecyclerViewClickListener listener, ImageSMGalleryAdapter.IImageRemove iImageRemove) {
        this.context = context;
        this.IsAllowGlide = IsAllowGlide;
        this.imageList = imageList;
        this.iImageRemove = iImageRemove;
        this.mListener = listener;
        deprecateHandler = new DeprecateHandler(context);
    }

    public interface IImageRemove {
        void removeImage(ImageDto imageDto);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtImageName;
        public ImageView imgThumbnail;
        public ImageView imgCancel;
        public LinearLayout layoutImages;

        public MyViewHolder(View view, RecyclerViewClickListener listener) {
            super(view);
            layoutImages = view.findViewById(R.id.layoutImages);
            txtImageName = view.findViewById(R.id.txtImageName);
            imgThumbnail = view.findViewById(R.id.imgThumbnail);
            imgCancel = view.findViewById(R.id.imgCancel);
            mListener = listener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(view, getAdapterPosition());
        }
    }

    @Override
    public ImageSMGalleryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_image_sm, parent, false);
        return new ImageSMGalleryAdapter.MyViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(ImageSMGalleryAdapter.MyViewHolder holder, int position) {

        final ImageDto imageDto = imageList.get(position);

        if (!TextUtils.isEmpty(imageDto.getBranding_element_name()))
            holder.txtImageName.setText(imageDto.getBranding_element_name());
        else
            holder.txtImageName.setText(imageDto.getName());

        if(IsAllowGlide) {
            if(imageDto.getBitmap() != null){
                holder.imgThumbnail.setImageBitmap(imageDto.getBitmap());
            } else {
                String imgId = imageDto.getNextgen_equip_images_id();
                if (!TextUtils.isEmpty(imgId)) {
                    String picUrl = Constants.DownloadImageUrl + imgId;
                    Glide.with(context)
                            .load(picUrl)
                            .apply(new RequestOptions()
                                    .error(R.drawable.ic_camera_alt_black_72dp)
                                    .placeholder(R.drawable.ic_camera_alt_black_72dp)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true))

                            .into(holder.imgThumbnail);
                }
            }
        } else{
            holder.imgThumbnail.setImageBitmap(imageDto.getBitmap());
            holder.imgThumbnail.setTag(imageDto);
        }

        holder.txtImageName.setTag(imageDto);
        holder.imgCancel.setEnabled(isCancelEnabled);
        holder.imgCancel.setTag(imageDto);

        if (!TextUtils.isEmpty(imageDto.getVl_remarks()))
            holder.layoutImages.setBackgroundDrawable(deprecateHandler.getDrawable(R.drawable.red_highlight));
        else
            holder.layoutImages.setBackgroundColor(deprecateHandler.getColor(R.color.grey));

        //Hide Cancel button in case of update
        if (TextUtils.isEmpty(imageDto.getNextgen_site_work_wip_images_id()))
            holder.imgCancel.setVisibility(View.VISIBLE);
        else
            holder.imgCancel.setVisibility(View.GONE);

        /*//Long Text
        holder.txtImageName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ImageDto imageDto1 = (ImageDto) view.getTag();
                Toast.makeText(context, imageDto1.getName(), Toast.LENGTH_LONG).show();
                return false;
            }
        });*/

        holder.imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageDto imageDto1 = (ImageDto) view.getTag();
                iImageRemove.removeImage(imageDto1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public int getItemPosition(Object object) {
        return RecyclerView.NO_POSITION;
    }
}
