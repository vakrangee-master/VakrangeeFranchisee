package in.vakrangee.franchisee.kendra_final_photo;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.model.KendraFinalPhotoDto;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;

public class KendraFinalPhotoAdapter extends RecyclerView.Adapter<KendraFinalPhotoAdapter.MyViewHolder> {

    private List<KendraFinalPhotoDto> photoDtos;
    private Context context;
    private View itemView;
    private KendraFinalPhotoAdapter.ISiteCheckHandler iSiteCheckHandler;
    private DeprecateHandler deprecateHandler;

    public KendraFinalPhotoAdapter(Context context, List<KendraFinalPhotoDto> photoDtos, KendraFinalPhotoAdapter.ISiteCheckHandler iSiteCheckHandler) {
        this.context = context;
        this.photoDtos = photoDtos;
        this.iSiteCheckHandler = iSiteCheckHandler;
        deprecateHandler = new DeprecateHandler(context);
    }

    public interface ISiteCheckHandler {
        public void cameraClick(int position, KendraFinalPhotoDto photoDtos);


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_photo_type_name;
        private ImageView image_event_photo;

        public MyViewHolder(View view) {
            super(view);
            txt_photo_type_name = view.findViewById(R.id.txt_photo_type_name);
            image_event_photo = view.findViewById(R.id.image_event_photo);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kendra_photo_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final KendraFinalPhotoDto kendraFinalPhotoDto = photoDtos.get(position);

        holder.txt_photo_type_name.setText(kendraFinalPhotoDto.getNextgen_photo_type_name());

        //Set Thumbnail
        if (!TextUtils.isEmpty(kendraFinalPhotoDto.getPhoto())) {
            Bitmap imageBitmap = CommonUtils.StringToBitMap(kendraFinalPhotoDto.getPhoto());
            holder.image_event_photo.setImageBitmap(imageBitmap);

        } else if (!TextUtils.isEmpty(kendraFinalPhotoDto.getPhoto_id())) {
            String imageUrl = Constants.DownloadImageUrl + kendraFinalPhotoDto.getPhoto_id();
            Glide.with(context)
                    .load(imageUrl)
                    .apply(new RequestOptions()
                            .error(R.drawable.ic_camera_alt_black_72dp)
                            .placeholder(R.drawable.ic_camera_alt_black_72dp)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(holder.image_event_photo);

        } else {
            Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.ic_camera_alt_black_72dp)).into(holder.image_event_photo);
        }

        holder.image_event_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iSiteCheckHandler.cameraClick(position, kendraFinalPhotoDto);
            }
        });

    }

    @Override
    public int getItemCount() {
        return photoDtos.size();
    }

}
