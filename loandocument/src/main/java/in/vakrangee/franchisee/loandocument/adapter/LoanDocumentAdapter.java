package in.vakrangee.franchisee.loandocument.adapter;

import android.content.Context;
import android.graphics.Bitmap;
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

import androidx.recyclerview.widget.RecyclerView;
import in.vakrangee.franchisee.loandocument.R;
import in.vakrangee.franchisee.loandocument.model.LoanDocumentDto;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.GUIUtils;

public class LoanDocumentAdapter extends RecyclerView.Adapter<LoanDocumentAdapter.MyViewHolder> {

    private static final String TAG = "LoanDocumentAdapter";
    private List<LoanDocumentDto> loanDocumentList;
    private Context context;
    private View itemView;
    private IClickHandler iSiteCheckHandler;
    private DeprecateHandler deprecateHandler;

    public LoanDocumentAdapter(Context context, List<LoanDocumentDto> loanDocumentList, IClickHandler iSiteCheckHandler) {
        this.context = context;
        this.loanDocumentList = loanDocumentList;
        this.iSiteCheckHandler = iSiteCheckHandler;
        deprecateHandler = new DeprecateHandler(context);
    }

    public interface IClickHandler {
        public void cameraClick(int position, LoanDocumentDto documentDto);

        public void onUploadClick(View v, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtPhotoName;
        private ImageView imgPhoto, txtChecked;
        private LinearLayout layoutParent, btnUploadAgreement, layoutChecked;

        public MyViewHolder(View view) {
            super(view);
            txtPhotoName = view.findViewById(R.id.txtPhotoName);
            imgPhoto = view.findViewById(R.id.imgPhoto);
            layoutParent = view.findViewById(R.id.layoutParent);
            btnUploadAgreement = view.findViewById(R.id.btnUploadAgreement);
            layoutChecked = view.findViewById(R.id.layoutChecked);
            txtChecked = view.findViewById(R.id.txtChecked);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loan_document, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final LoanDocumentDto photoDto = loanDocumentList.get(position);

        //Checked
        setCircleChecked(holder, photoDto);

        //Image
        setImage(holder, photoDto);

        //Photo Name
        if (!TextUtils.isEmpty(photoDto.getIsMandatory())) {
            if (photoDto.getIsMandatory().equalsIgnoreCase("1")) {
                GUIUtils.CompulsoryMark(holder.txtPhotoName, photoDto.getName());
            } else {
                holder.txtPhotoName.setText(photoDto.getName());
            }
        } else {
            holder.txtPhotoName.setText(photoDto.getName());
        }


        holder.imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iSiteCheckHandler.cameraClick(position, photoDto);
            }
        });

        holder.btnUploadAgreement.setTag(position);
        holder.btnUploadAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                iSiteCheckHandler.onUploadClick(v, pos);
            }
        });

        //Editable
        if (!TextUtils.isEmpty(photoDto.getIsEditable()) && photoDto.getIsEditable().equalsIgnoreCase("1")) {
            holder.btnUploadAgreement.setVisibility(View.VISIBLE);
        } else {
            holder.btnUploadAgreement.setVisibility(View.GONE);
        }

    }

    private void setImage(MyViewHolder holder, LoanDocumentDto photoDto) {

        if (TextUtils.isEmpty(photoDto.getLoanDocumentImgExt())) {
            Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.ic_camera_alt_black_72dp)).into(holder.imgPhoto);

        } else if (photoDto.getLoanDocumentImgExt().contains("pdf")) {
            Glide.with(context).asDrawable().load(deprecateHandler.getDrawable(R.drawable.pdf)).into(holder.imgPhoto);

        } else if (!TextUtils.isEmpty(photoDto.getLoanDocumentImgBase64())) {
            Bitmap imageBitmap = CommonUtils.StringToBitMap(photoDto.getLoanDocumentImgBase64());
            holder.imgPhoto.setImageBitmap(imageBitmap);

        } else if (!TextUtils.isEmpty(photoDto.getLoanDocumentImgId())) {
            String imageUrl = Constants.DownloadImageUrl + photoDto.getLoanDocumentImgId();

            Glide.with(context)
                    .load(imageUrl)
                    .apply(new RequestOptions()
                            .error(R.drawable.ic_camera_alt_black_72dp)
                            .placeholder(R.drawable.ic_camera_alt_black_72dp)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(holder.imgPhoto);
        }
    }

    @Override
    public int getItemCount() {
        return loanDocumentList.size();
    }

    private void setCircleChecked(MyViewHolder holder, LoanDocumentDto photoDto) {

        if (TextUtils.isEmpty(photoDto.getLoanDocumentId())) {
            holder.txtChecked.setImageDrawable(deprecateHandler.getDrawable(R.drawable.ic_check_grey_24dp));
        } else {
            holder.txtChecked.setImageDrawable(deprecateHandler.getDrawable(R.drawable.ic_check_circle_green_24dp));
        }

       /* holder.txtChecked.setText(context.getString(R.string.fa_circle_check));
        holder.txtChecked.setTypeface(VakrangeeKendraApplication.font_awesome, Typeface.BOLD);
        holder.txtChecked.setTextSize(22);

        if (TextUtils.isEmpty(photoDto.getLoanDocumentId())) {
            holder.txtChecked.setTextColor(deprecateHandler.getColor(R.color.gray));
        } else {
            holder.txtChecked.setTextColor(deprecateHandler.getColor(R.color.green));
        }*/
    }
}
