package in.vakrangee.franchisee.support;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.commongui.FileAttachmentDto;

public class AttachmentListAdapter extends RecyclerView.Adapter<AttachmentListAdapter.MyViewHolder> {

    private static final String TAG = "AttachmentListAdapter";
    private List<FileAttachmentDto> fileAttachmentList;
    private Context context;
    private IAttachmentRemove iAttachmentRemove;

    public AttachmentListAdapter(Context context, List<FileAttachmentDto> fileAttachmentList, IAttachmentRemove iAttachmentRemove) {
        this.context = context;
        this.fileAttachmentList = fileAttachmentList;
        this.iAttachmentRemove = iAttachmentRemove;
    }

    public interface IAttachmentRemove {
        void removeAttachment(FileAttachmentDto fileAttachmentDto);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtFileName;
        public ImageView imgCancel;

        public MyViewHolder(View view) {
            super(view);
            txtFileName = view.findViewById(R.id.txtFileName);
            imgCancel = view.findViewById(R.id.imgCancel);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_attachment, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final FileAttachmentDto fileAttachmentDto = fileAttachmentList.get(position);
        holder.txtFileName.setText(fileAttachmentDto.getFileName());
        holder.txtFileName.setTag(fileAttachmentDto);
        holder.imgCancel.setTag(fileAttachmentDto);

        //Long Text
        holder.txtFileName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                FileAttachmentDto fileAttachmentDto1 = (FileAttachmentDto) view.getTag();
                Toast.makeText(context, fileAttachmentDto1.getFileName(), Toast.LENGTH_LONG).show();
                return false;
            }
        });

        holder.imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileAttachmentDto fileAttachmentDto1 = (FileAttachmentDto) view.getTag();
                iAttachmentRemove.removeAttachment(fileAttachmentDto1);

            }
        });
    }

    @Override
    public int getItemCount() {
        return fileAttachmentList.size();
    }
}
