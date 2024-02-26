package in.vakrangee.franchisee.nextgenfranchiseeapplication.bank_statement;

import android.content.Context;
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
import in.vakrangee.franchisee.nextgenfranchiseeapplication.R;
import in.vakrangee.supercore.franchisee.commongui.imagegallery.RecyclerViewClickListener;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;

public class AllBankStatementDetailsAdapter extends RecyclerView.Adapter<AllBankStatementDetailsAdapter.MyViewHolder> {

    public List<BankStatementDto> bankStatementList;
    private Context context;
    private DeprecateHandler deprecateHandler;
    private RecyclerViewClickListener mListener;
    private static final String DATE_YY_MM_DD_CONSTANTS = "yyyy-MM-dd";

    public AllBankStatementDetailsAdapter(Context context, List<BankStatementDto> bankStatementList, RecyclerViewClickListener listener) {
        this.context = context;
        this.bankStatementList = bankStatementList;
        this.mListener = listener;
        deprecateHandler = new DeprecateHandler(context);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtBankStatementTitle, txtStartDate, txtEndDate, txtBankStatementPassword;
        public LinearLayout layoutParent;
        public ImageView imgBankStmtPDF;

        public MyViewHolder(View view, RecyclerViewClickListener listener) {
            super(view);
            layoutParent = view.findViewById(R.id.layoutParent);

            txtBankStatementTitle = view.findViewById(R.id.txtBankStatementTitle);
            txtStartDate = view.findViewById(R.id.txtStartDate);
            txtEndDate = view.findViewById(R.id.txtEndDate);
            txtBankStatementPassword = view.findViewById(R.id.txtBankStatementPassword);
            imgBankStmtPDF = view.findViewById(R.id.imgBankStmtPDF);


            mListener = listener;
            layoutParent.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            mListener.onClick(view, getAdapterPosition());
        }
    }

    @Override
    public AllBankStatementDetailsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bank_stmt_details, parent, false);
        return new AllBankStatementDetailsAdapter.MyViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(AllBankStatementDetailsAdapter.MyViewHolder holder, int position) {

        final BankStatementDto detailsDto = bankStatementList.get(position);

        String date = TextUtils.isEmpty(detailsDto.getBankStmtStartDate()) ? "-" : CommonUtils.getFormattedDate(DATE_YY_MM_DD_CONSTANTS, "dd MMM yyyy", detailsDto.getBankStmtStartDate());
        holder.txtStartDate.setText(date);

        String dateEnd = TextUtils.isEmpty(detailsDto.getBankStmtEndDate()) ? "-" : CommonUtils.getFormattedDate(DATE_YY_MM_DD_CONSTANTS, "dd MMM yyyy", detailsDto.getBankStmtEndDate());
        holder.txtEndDate.setText(dateEnd);

        String title = TextUtils.isEmpty(detailsDto.getTitle()) ? "-" : detailsDto.getTitle();
        holder.txtBankStatementTitle.setText(title);

        String pwd = TextUtils.isEmpty(detailsDto.getBankStmtPassword()) ? "-" : detailsDto.getBankStmtPassword();
        holder.txtBankStatementPassword.setText(pwd);

        boolean IsPDF = ((detailsDto.getBankStmtImageExt() != null) && detailsDto.getBankStmtImageExt().equalsIgnoreCase("pdf")) ? true : false;
        if (IsPDF) {
            Glide.with(context).asDrawable().load(R.drawable.pdf).into(holder.imgBankStmtPDF);
        } else {
            if (!TextUtils.isEmpty(detailsDto.getBankStmtImageId())) {
                String picUrl = Constants.DownloadImageUrl + detailsDto.getBankStmtImageId();
                Glide.with(context)
                        .load(picUrl)
                        .apply(new RequestOptions()
                                .error(R.drawable.ic_camera_alt_black_72dp)
                                .placeholder(R.drawable.ic_camera_alt_black_72dp)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))

                        .into(holder.imgBankStmtPDF);
            }
        }

        holder.txtBankStatementTitle.setTag(detailsDto);
    }

    @Override
    public int getItemCount() {
        return bankStatementList.size();
    }

}
