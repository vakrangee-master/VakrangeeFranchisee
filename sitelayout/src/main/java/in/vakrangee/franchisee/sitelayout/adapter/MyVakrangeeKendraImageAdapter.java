package in.vakrangee.franchisee.sitelayout.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.vakrangee.franchisee.sitelayout.R;
import in.vakrangee.supercore.franchisee.commongui.ImageSliderDialog;
import in.vakrangee.supercore.franchisee.model.My_vakranggekendra_image;
import in.vakrangee.supercore.franchisee.utils.Connection;

/**
 * Created by Nileshd on 12/28/2016.
 */
public class MyVakrangeeKendraImageAdapter extends RecyclerView.Adapter<MyVakrangeeKendraImageAdapter.ViewHolder> {

    private Context context;
    private List<My_vakranggekendra_image> contactList;
    Animation marquee;

    public MyVakrangeeKendraImageAdapter(Context activity, List<My_vakranggekendra_image> contacts) {

        this.context = activity;
        this.contactList = contacts;
        marquee = AnimationUtils.loadAnimation(context, R.anim.marquee);
    }

    @Override
    public int getItemCount() {
        return (null != contactList ? contactList.size() : 0);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final My_vakranggekendra_image list = contactList.get(position);
        //setting data to view holder elements


        viewHolder.name.setText(list.getImgetype());

        viewHolder.photoView.setImageBitmap(list.getImage());
        String remarks = list.getRemarks();
        if (!TextUtils.isEmpty(remarks)) {
            viewHolder.txtRemarks.setText(remarks);
        }

        viewHolder.photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO: Below code is commmented because its showing only selected image

                //Image Slider is used to show Sliding Images with selected position in list. - [Date: 21-05-2018 By: Dpk]
                ImageSliderDialog imageSliderDialog = new ImageSliderDialog(context, new ArrayList<Object>(contactList), position, new ImageSliderDialog.ISliderClickHandler() {
                    @Override
                    public void captureClick(int position) {
                        //Do Nothing
                    }

                    @Override
                    public void saveClick(List<Object> objectList) {
                        //Do Nothing
                    }
                });
                imageSliderDialog.allowRemarks(false);
                imageSliderDialog.allowChangePhoto(false);
                imageSliderDialog.show();
                imageSliderDialog.setCancelable(false);

            }
        });

        viewHolder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Image Slider is used to show Sliding Images with selected position in list. - [Date: 21-05-2018 By: Dpk]
                ImageSliderDialog imageSliderDialog = new ImageSliderDialog(context, new ArrayList<Object>(contactList), position, new ImageSliderDialog.ISliderClickHandler() {
                    @Override
                    public void captureClick(int position) {
                        //Do Nothing
                    }

                    @Override
                    public void saveClick(List<Object> objectList) {
                        //Do Nothing
                    }
                });
                imageSliderDialog.show();
                imageSliderDialog.setCancelable(false);
            }
        });

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.add_image_adapter, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
        //inflate your layout and pass it to view holder

    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        ImageView photoView;
        TextView txtRemarks;

        public ViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.contactName);
            photoView = (ImageView) view.findViewById(R.id.imageadpter);
            txtRemarks = (TextView) view.findViewById(R.id.txtRemarks);

        }
    }


    private class ListItemClickListener implements View.OnClickListener {

        int position;
        My_vakranggekendra_image list;

        public ListItemClickListener(int position, My_vakranggekendra_image list) {
            this.position = position;
            this.list = list;
        }

        @Override
        public void onClick(View v) {

            Connection db = new Connection(context);
            db.deleteimage(list);
            contactList.remove(position);
            notifyDataSetChanged();
        }
    }

}
