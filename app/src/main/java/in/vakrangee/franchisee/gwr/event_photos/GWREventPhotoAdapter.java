package in.vakrangee.franchisee.gwr.event_photos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.application.VakrangeeKendraApplication;
import in.vakrangee.supercore.franchisee.model.GWREventPhotoDto;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.GUIUtils;

class GWREventPhotoAdapter extends RecyclerView.Adapter<GWREventPhotoAdapter.MyViewHolder> {
    private static final String TAG = "GWREventPhotoAdapter";
    private List<GWREventPhotoDto> eventPhotoDtos;
    private Context context;
    private View itemView;
    private GWREventPhotoAdapter.ISiteCheckHandler iSiteCheckHandler;
    private DeprecateHandler deprecateHandler;

    public GWREventPhotoAdapter(Context context, List<GWREventPhotoDto> gwrEventPhotoDtos, GWREventPhotoAdapter.ISiteCheckHandler iSiteCheckHandler) {
        this.context = context;
        this.eventPhotoDtos = gwrEventPhotoDtos;
        this.iSiteCheckHandler = iSiteCheckHandler;
        deprecateHandler = new DeprecateHandler(context);
    }

    public interface ISiteCheckHandler {
        public void cameraClick(int position, GWREventPhotoDto gwrEventPhotoDto);


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_photo_type_name, txtWarningIconEventDayPhoto;
        private ImageView image_event_photo;
        private LinearLayout layoutEDP;

        public MyViewHolder(View view) {
            super(view);
            txt_photo_type_name = view.findViewById(R.id.txt_photo_type_name);
            image_event_photo = view.findViewById(R.id.image_event_photo);
            txtWarningIconEventDayPhoto = view.findViewById(R.id.txtWarningIconEventDayPhoto);
            layoutEDP = view.findViewById(R.id.layoutEDP);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_eventphoto_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final GWREventPhotoDto gwrEventPhotoDto = eventPhotoDtos.get(position);

        holder.txt_photo_type_name.setText(gwrEventPhotoDto.getGuinnessEventPhotoTypeName());

        //Set Thumbnail
        if (!TextUtils.isEmpty(gwrEventPhotoDto.getEventPhoto())) {
            Bitmap imageBitmap = CommonUtils.StringToBitMap(gwrEventPhotoDto.getEventPhoto());
            holder.image_event_photo.setImageBitmap(imageBitmap);

        } else if (!TextUtils.isEmpty(gwrEventPhotoDto.getEventPhotoId())) {
            String imageUrl = Constants.DownloadImageUrl + gwrEventPhotoDto.getEventPhotoId();

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
        // get Event Day photo -status
        if (!TextUtils.isEmpty(gwrEventPhotoDto.getStatusEventDayPhoto())) {
            if (gwrEventPhotoDto.getStatusEventDayPhoto().equalsIgnoreCase("1")) {
                //disable views
                holder.txtWarningIconEventDayPhoto.setVisibility(View.VISIBLE);
                holder.txtWarningIconEventDayPhoto.setText(context.getString(R.string.fa_circle_check));
                holder.txtWarningIconEventDayPhoto.setTypeface(VakrangeeKendraApplication.font_awesome, Typeface.BOLD);
                holder.txtWarningIconEventDayPhoto.setTextColor(deprecateHandler.getColor(R.color.green));
                // GUIUtils.setViewAndChildrenEnabled(holder.layoutEDP, false);
            } else if (gwrEventPhotoDto.getStatusEventDayPhoto().equalsIgnoreCase("2")) {
                //send back for correction
                holder.txtWarningIconEventDayPhoto.setVisibility(View.VISIBLE);
                holder.txtWarningIconEventDayPhoto.setText(context.getString(R.string.fa_circle_cross));
                holder.txtWarningIconEventDayPhoto.setTypeface(VakrangeeKendraApplication.font_awesome, Typeface.BOLD);
                holder.txtWarningIconEventDayPhoto.setTextColor(deprecateHandler.getColor(R.color.ired));
                GUIUtils.setViewAndChildrenEnabled(holder.layoutEDP, true);
                if (!TextUtils.isEmpty(gwrEventPhotoDto.getStatusMsg())) {
                    holder.txtWarningIconEventDayPhoto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(context, "Send Back for Correction : " + gwrEventPhotoDto.getStatusMsg(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                //Enable views
                GUIUtils.setViewAndChildrenEnabled(holder.layoutEDP, true);
            }


            holder.image_event_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iSiteCheckHandler.cameraClick(position, gwrEventPhotoDto);
                }
            });

        }
    }

    private void displayPhotoEvenetImage(String eventPhotoId, ImageView imageview) {
        if (!TextUtils.isEmpty(eventPhotoId)) {
            String picUrl = Constants.DownloadImageUrl + eventPhotoId;
            Glide.with(context)
                    .load(picUrl)
                    .apply(new RequestOptions()
                            .error(R.drawable.ic_camera_alt_black_72dp)
                            .placeholder(R.drawable.ic_camera_alt_black_72dp)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))

                    .into(imageview);
        } else {
            Glide.with(context).asDrawable().load(R.drawable.ic_camera_alt_black_72dp).into(imageview);
        }
    }

    @Override
    public int getItemCount() {
        return eventPhotoDtos.size();
    }

}
