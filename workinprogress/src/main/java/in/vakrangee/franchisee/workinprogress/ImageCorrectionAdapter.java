package in.vakrangee.franchisee.workinprogress;

import android.content.Context;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.vakrangee.supercore.franchisee.commongui.imagegallery.ImageDto;

public class ImageCorrectionAdapter extends RecyclerView.Adapter<ImageCorrectionAdapter.MyViewHolder> {

    private static final String TAG = "ImageCorrectionAdapter";
    private List<ImageDto> imageDtoList;
    private Context context;

    public ImageCorrectionAdapter(Context context, List<ImageDto> imageDtoList) {
        this.context = context;
        this.imageDtoList = imageDtoList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtFileName;
        public ImageView imgThumbnail;
        public TextView txtVlRemarks;

        public MyViewHolder(View view) {
            super(view);
            txtFileName = view.findViewById(R.id.txtFileName);
            imgThumbnail = view.findViewById(R.id.imgThumbnail);
            txtVlRemarks = view.findViewById(R.id.txtVLRemarks);
        }
    }

    @Override
    public ImageCorrectionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_correction_images, parent, false);
        return new ImageCorrectionAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ImageCorrectionAdapter.MyViewHolder holder, int position) {

        final ImageDto imageDto = imageDtoList.get(position);
        if (!TextUtils.isEmpty(imageDto.getBranding_element_name()))
            holder.txtFileName.setText(imageDto.getBranding_element_name());
        else
            holder.txtFileName.setText(imageDto.getName());
        holder.txtVlRemarks.setText(imageDto.getVl_remarks());
        holder.imgThumbnail.setImageBitmap(imageDto.getBitmap());

        //Remarks scroll
        holder.txtVlRemarks.setMovementMethod(new ScrollingMovementMethod());
        holder.txtFileName.setTag(imageDto);

    }

    @Override
    public int getItemCount() {
        return imageDtoList.size();
    }
}
