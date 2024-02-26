package in.vakrangee.core.commongui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.vakrangee.core.R;

public class FileAttachmentDialog extends Dialog implements android.view.View.OnClickListener {

    private static final String TAG = "FileAttachmentDialog";
    private Context context;
    private ImageView imgCamera, imgFolder;
    private Button btnCancel;
    private LinearLayout layoutBrowse;
    private boolean IsBrowseHide = false;
    private TextView txtTitle;
    private String title;

    private IFileAttachmentClicks iFileAttachmentClicks;

    public interface IFileAttachmentClicks {
        void cameraClick();

        void folderClick();
    }

    public FileAttachmentDialog(@NonNull Context context, @NonNull IFileAttachmentClicks iFileAttachmentClicks) {
        super(context);
        this.context = context;
        this.iFileAttachmentClicks = iFileAttachmentClicks;
    }

    public FileAttachmentDialog(@NonNull Context context, boolean IsBrowseHide, @NonNull IFileAttachmentClicks iFileAttachmentClicks) {
        super(context);
        this.context = context;
        this.IsBrowseHide = IsBrowseHide;
        this.iFileAttachmentClicks = iFileAttachmentClicks;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        final Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        setContentView(R.layout.dialog_file_attachment);

        //Widgets
        txtTitle = findViewById(R.id.txtTitle);
        imgCamera = findViewById(R.id.imgCamera);
        btnCancel = findViewById(R.id.btnCancel);
        imgFolder = findViewById(R.id.imgFolder);
        layoutBrowse = findViewById(R.id.layoutBrowse);

        if (!TextUtils.isEmpty(title))
            txtTitle.setText(title);

        if (IsBrowseHide)
            layoutBrowse.setVisibility(View.GONE);
        else
            layoutBrowse.setVisibility(View.VISIBLE);

        // Font
        btnCancel.setTypeface(font);
        btnCancel.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + "  " + context.getResources().getString(R.string.cancel)));

        //Listeners
        imgFolder.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        imgCamera.setOnClickListener(this);

        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.70);
        getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();

        if (Id == R.id.imgFolder) {
            dismiss();
            iFileAttachmentClicks.folderClick();

        } else if (Id == R.id.btnCancel) {
            dismiss();

        } else if (Id == R.id.imgCamera) {
            dismiss();
            iFileAttachmentClicks.cameraClick();
        }
    }
}
