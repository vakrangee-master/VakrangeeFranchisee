package in.vakrangee.franchisee.gwr.attendance;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.gwr.GWRRepository;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;

public class AttendanceDetailsAdapter extends RecyclerView.Adapter<AttendanceDetailsAdapter.MyViewHolder> {

    private static final String TAG = "AttendanceDetailsAdapter";
    public List<AttendanceDetailsDto> attendanceDetailsList;
    private Context context;
    private DeprecateHandler deprecateHandler;
    private int type;
    public static final int TYPE_CAMERA_MAN = 1;
    public static final int TYPE_WITNESS = 0;
    private IAttendancePicHandler iAttendancePicHandler;
    private GWRRepository gwrRepository;
    public static final int FROM_UPLOAD_STATEMENT = 1;
    public static final int FROM_ATTENDANCE = 0;
    private int from;
    private DateFormat dateTimeDisplayFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:SS");

    public AttendanceDetailsAdapter(Context context, int type, int from, List<AttendanceDetailsDto> attendanceDetailsList, IAttendancePicHandler iAttendancePicHandler) {
        this.context = context;
        this.attendanceDetailsList = attendanceDetailsList;
        deprecateHandler = new DeprecateHandler(context);
        this.type = type;
        this.from = from;
        this.iAttendancePicHandler = iAttendancePicHandler;
        gwrRepository = new GWRRepository(context);
    }

    public interface IAttendancePicHandler {
        public void cameraClick(int position, AttendanceDetailsDto attendanceDetailsDto);

        public void navigateMap(int position, AttendanceDetailsDto attendanceDetailsDto);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout layoutParent;
        public TextView txtName;
        public TextView txtOrganisation;
        public TextView txtOccupation;
        public TextView txtAddress;
        public ImageView imgProfilePic;
        public ImageView imgStatus;
        public LinearLayout layoutOccupation;
        public ImageView imgAttendancePhoto;
        public TextView txtCaptureOn;
        public LinearLayout layoutCaptureInfo;
        public TextView txtImpNote;
        public TextView txtAttendanceLbl;
        public LinearLayout layoutNavigateMap;

        public MyViewHolder(View view) {
            super(view);

            layoutParent = view.findViewById(R.id.layoutParent);
            imgStatus = view.findViewById(R.id.imgStatus);
            txtName = view.findViewById(R.id.txtName);
            txtOrganisation = view.findViewById(R.id.txtOrganisation);
            txtOccupation = view.findViewById(R.id.txtOccupation);
            txtAddress = view.findViewById(R.id.txtAddress);
            imgProfilePic = view.findViewById(R.id.imgProfilePic);
            layoutOccupation = view.findViewById(R.id.layoutOccupation);
            imgAttendancePhoto = view.findViewById(R.id.imgAttendancePhoto);
            layoutCaptureInfo = view.findViewById(R.id.layoutCaptureInfo);
            layoutNavigateMap = view.findViewById(R.id.layoutNavigateMap);
            txtCaptureOn = view.findViewById(R.id.txtCaptureOn);
            txtImpNote = view.findViewById(R.id.txtImpNote);
            txtAttendanceLbl = view.findViewById(R.id.txtAttendanceLbl);
        }
    }

    @Override
    public AttendanceDetailsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_id_card, parent, false);
        return new AttendanceDetailsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AttendanceDetailsAdapter.MyViewHolder holder, int position) {

        final AttendanceDetailsDto attendanceDetailsDto = attendanceDetailsList.get(position);

        holder.txtName.setText(attendanceDetailsDto.getName());
        holder.txtOrganisation.setText(attendanceDetailsDto.getOrganization_name());
        holder.txtOccupation.setText(attendanceDetailsDto.getOccupation());
        holder.txtAddress.setText(attendanceDetailsDto.getAddress());

        //Status: [1 - Present, 0 - Absent]
        if (!TextUtils.isEmpty(attendanceDetailsDto.getStatus()) && attendanceDetailsDto.getStatus().equalsIgnoreCase("1")) {
            //Green
            holder.imgStatus.setImageDrawable(deprecateHandler.getDrawable(R.drawable.triangle));
        } else {
            holder.imgStatus.setImageDrawable(deprecateHandler.getDrawable(R.drawable.orange_triangle));
        }

        //Set Attendance Label
        if (from == FROM_UPLOAD_STATEMENT) {
            holder.imgAttendancePhoto.setImageDrawable(deprecateHandler.getDrawable(R.drawable.ic_add_a_photo_black_24dp));
            holder.txtAttendanceLbl.setText("Upload Witness Statement");
            holder.txtImpNote.setVisibility(View.GONE);
            setWitnessStatementImage(attendanceDetailsDto, holder.imgAttendancePhoto);
            holder.layoutCaptureInfo.setVisibility(View.GONE);

        } else {
            holder.imgAttendancePhoto.setImageDrawable(deprecateHandler.getDrawable(R.drawable.ic_camera_alt_black_72dp));
            holder.txtAttendanceLbl.setText("Attendance");
            holder.txtImpNote.setVisibility(View.VISIBLE);
            setAttendanceImage(attendanceDetailsDto, holder.imgAttendancePhoto);
            setCapturedInfo(position, holder.txtCaptureOn, holder.layoutNavigateMap, holder.layoutCaptureInfo);
        }

        //Profile Pic
        if (!TextUtils.isEmpty(attendanceDetailsDto.getPic_id())) {
            String picUrl = Constants.DownloadImageUrl + attendanceDetailsDto.getPic_id();
            Glide.with(context)
                    .load(picUrl)
                    .apply(new RequestOptions()
                            .error(R.drawable.proflie)
                            .placeholder(R.drawable.proflie)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))

                    .into(holder.imgProfilePic);
        }

        //Attendance Image
        holder.imgAttendancePhoto.setTag(position);

        //Set Click Listener to Attendance
        holder.imgAttendancePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                AttendanceDetailsDto dto = attendanceDetailsList.get(pos);
                iAttendancePicHandler.cameraClick(pos, dto);

            }
        });

        //Set Navigation of Map
        holder.layoutNavigateMap.setTag(position);
        holder.layoutNavigateMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                AttendanceDetailsDto dto = attendanceDetailsList.get(pos);
                iAttendancePicHandler.navigateMap(pos, dto);
            }
        });

        holder.txtName.setTag(attendanceDetailsDto);
    }

    private void setCapturedInfo(int pos, TextView txtCapturedOn, LinearLayout layoutNavigateMap, LinearLayout layoutCaptureInfo) {

        AttendanceDetailsDto attendanceDetailsDto = attendanceDetailsList.get(pos);
        if (attendanceDetailsDto == null)
            return;

        layoutCaptureInfo.setVisibility(View.VISIBLE);
        //Captured Info
        String lat = attendanceDetailsDto.getLatitude();
        String longi = attendanceDetailsDto.getLongitude();
        String capturedOn = attendanceDetailsDto.getCapturedDateTime();

        //STEP 1: Display Captured Date Time
        if (TextUtils.isEmpty(capturedOn)) {
            txtCapturedOn.setVisibility(View.GONE);
        } else {
            String formatedCapturedOn = CommonUtils.getFormattedDate("yyyy-MM-dd HH:mm:SS", "dd-MM-yyyy HH:mm:SS", capturedOn);
            if (TextUtils.isEmpty(formatedCapturedOn))
                txtCapturedOn.setVisibility(View.GONE);
            else {
                txtCapturedOn.setVisibility(View.VISIBLE);
                txtCapturedOn.setText(formatedCapturedOn);
            }
        }

        //STEP 2: Navigate Map
        if (TextUtils.isEmpty(lat) || TextUtils.isEmpty(longi)) {
            layoutNavigateMap.setVisibility(View.GONE);
        } else {
            layoutNavigateMap.setVisibility(View.VISIBLE);
        }
    }

    private void setWitnessStatementImage(AttendanceDetailsDto attendanceDetailsDto, ImageView imgAttendancePhoto) {
        boolean IsPDF = ((attendanceDetailsDto.getExtension() != null) && attendanceDetailsDto.getExtension().equalsIgnoreCase("pdf")) ? true : false;
        if (IsPDF) {
            imgAttendancePhoto.setImageDrawable(deprecateHandler.getDrawable(R.drawable.pdf));
        } else {

            String imageBase64 = attendanceDetailsDto.getWitnessStatementImage();
            if (imageBase64 != null) {
                Bitmap bitmap = CommonUtils.StringToBitMap(imageBase64);
                imgAttendancePhoto.setImageBitmap(bitmap);

            } else {
                imgAttendancePhoto.setImageDrawable(deprecateHandler.getDrawable(R.drawable.ic_add_a_photo_black_24dp));
            }
        }
    }

    private void setAttendanceImage(AttendanceDetailsDto attendanceDetailsDto, ImageView imgAttendancePhoto) {
        String imageBase64 = attendanceDetailsDto.getImgAttendBase64();
        if (imageBase64 != null) {
            Bitmap bitmap = CommonUtils.StringToBitMap(imageBase64);
            imgAttendancePhoto.setImageBitmap(bitmap);

        } else {
            imgAttendancePhoto.setImageDrawable(deprecateHandler.getDrawable(R.drawable.ic_camera_alt_black_72dp));
        }
    }

    @Override
    public int getItemCount() {
        return attendanceDetailsList.size();
    }
}
