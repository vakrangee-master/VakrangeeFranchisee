package in.vakrangee.franchisee.commongui.photocapture;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.commongui.imagegallery.RecyclerViewClickListener;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.MyViewHolder> {

    private static final String TAG = PhotoAdapter.class.getCanonicalName();

    private Context context;
    private List<PhotoDto> photoList;
    private DeprecateHandler deprecateHandler;
    private RecyclerViewClickListener mListener;

    public PhotoAdapter(Context context, List<PhotoDto> photoList, RecyclerViewClickListener listener) {
        this.context = context;
        this.photoList = photoList;
        this.mListener = listener;
        deprecateHandler = new DeprecateHandler(context);
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
            imgCancel.setVisibility(View.GONE);
            mListener = listener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(view, getAdapterPosition());
        }
    }

    @Override
    public PhotoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_thumbnail, parent, false);
        return new PhotoAdapter.MyViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(PhotoAdapter.MyViewHolder holder, int position) {

        final PhotoDto photoDto = photoList.get(position);

        holder.txtImageName.setText(photoDto.getName());

        if (photoDto.getBitmap() != null)
            holder.imgThumbnail.setImageBitmap(photoDto.getBitmap());
        else
            holder.imgThumbnail.setImageDrawable(deprecateHandler.getDrawable(R.drawable.ic_camera_alt_black_72dp));

        holder.txtImageName.setTag(photoDto);
        holder.imgThumbnail.setTag(photoDto);

    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public int getItemPosition(Object object) {
        return RecyclerView.NO_POSITION;
    }
}
