package in.vakrangee.franchisee.trainingvideo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import in.vakrangee.franchisee.R;

public class AdapterTrainingVideo extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    List<trainingvideopojo> data;

    // create constructor to innitilize context and data sent from MainActivity
    public AdapterTrainingVideo(Context context, List<trainingvideopojo> data) {
        this.context = context;
        this.data = data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerviewadpatertrainigvideo, null);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder = (MyHolder) holder;

        final trainingvideopojo constants = data.get(position);
        myHolder.dr.setText("Duration: " + constants.getDuration());
        myHolder.urltitle.setText(constants.getName());
        setImage(constants.getLink(), myHolder.image);

        Uri uri = Uri.parse(constants.getLink());
        final String getyoutubeID = uri.getQueryParameter("v");
        myHolder.play_actionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, VideoPlayActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("videolink", constants.getLink());
                intent.putExtra("youtube_id", getyoutubeID);
                context.startActivity(intent);
            }
        });
    }


    public void setImage(String imageId, final ImageView imageView) {
        Uri uri = Uri.parse(imageId);
        String getID = uri.getQueryParameter("v");  //will return "V-Maths-Addition "
        String URL = "https://img.youtube.com/vi/" + getID + "/mqdefault.jpg";
        final String imageUrl = URL;

        if (TextUtils.isEmpty(imageId) || imageId.equalsIgnoreCase("0")) {
            Glide.with(context)
                    .applyDefaultRequestOptions(new RequestOptions()
                            .placeholder(R.drawable.proflie)
                            .error(R.drawable.proflie))
                    .load(imageUrl)
                    .into(imageView);
            //Glide.with(context).asBitmap().load(bitmap).into(imageView);
        } else {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.proflie)
                    .error(R.drawable.proflie);
            Glide.with(context).load(imageUrl)
                    .apply(options)
                    .into(imageView);

        }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {
        TextView dr, urltitle;
        ImageView image;
        FloatingActionButton play_actionbutton;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            urltitle = (TextView) itemView.findViewById(R.id.urltitle);
            dr = (TextView) itemView.findViewById(R.id.text);
            image = (ImageView) itemView.findViewById(R.id.image);
            play_actionbutton = itemView.findViewById(R.id.play_actionbutton);
        }

    }
}
