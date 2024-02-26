package in.vakrangee.core.commongui.carouselgallery;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import in.vakrangee.core.R;
import in.vakrangee.core.commongui.imagegallery.ImageDto;
import in.vakrangee.core.commongui.imagegallery.RecyclerViewClickListener;
import in.vakrangee.core.utils.Constants;
import in.vakrangee.core.utils.DeprecateHandler;

public class ImageCarouselAdapter extends RecyclerView.Adapter<ImageCarouselAdapter.MyViewHolder> {

    private List<ImageDto> mData = new ArrayList<>();
    private Context mContext;
    private boolean IsAllowGlide = false;
    private ImageCarouselAdapter.IImageRemove iImageRemove;
    private RecyclerViewClickListener mListener;
    private DeprecateHandler deprecateHandler;
    public boolean isCancelEnabled = true;

    public ImageCarouselAdapter(Context context, List<ImageDto> imageList, RecyclerViewClickListener listener, ImageCarouselAdapter.IImageRemove iImageRemove) {
        this.mContext = context;
        this.mData = imageList;
        this.iImageRemove = iImageRemove;
        this.mListener = listener;
        deprecateHandler = new DeprecateHandler(context);
    }

    public void setData(List<ImageDto> data) {
        mData = data;
    }

    public interface IImageRemove {
        void removeImage(ImageDto imageDto);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView label;
        public ImageView image;
        public ImageView imgCancel;

        public MyViewHolder(View view, RecyclerViewClickListener listener) {
            super(view);
            label = view.findViewById(R.id.label);
            image = view.findViewById(R.id.image);
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
    public ImageCarouselAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coverflow, parent, false);
        return new ImageCarouselAdapter.MyViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(ImageCarouselAdapter.MyViewHolder holder, int position) {

        final ImageDto imageDto = mData.get(position);

        if(IsAllowGlide) {
            if(imageDto.getBitmap() != null){
                holder.image.setImageBitmap(imageDto.getBitmap());
            } else {
                String imgId = imageDto.getNextgen_equip_images_id();
                if (!TextUtils.isEmpty(imgId)) {
                    String picUrl = Constants.DownloadImageUrl + imgId;
                    Glide.with(mContext)
                            .load(picUrl)
                            .apply(new RequestOptions()
                                    .error(R.drawable.ic_camera_alt_black_72dp)
                                    .placeholder(R.drawable.ic_camera_alt_black_72dp)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true))

                            .into(holder.image);
                }
            }
        } else{
            holder.image.setImageBitmap(imageDto.getBitmap());
            holder.image.setTag(imageDto);
        }

        holder.label.setText(imageDto.getName());

        holder.label.setTag(imageDto);
        holder.imgCancel.setEnabled(isCancelEnabled);
        holder.imgCancel.setTag(imageDto);

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
        return mData.size();
    }

    public int getItemPosition(Object object) {
        return RecyclerView.NO_POSITION;
    }
    @Override
    public long getItemId(int pos) {
        return pos;
    }

}
