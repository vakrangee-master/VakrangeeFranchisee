package in.vakrangee.core.commongui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.vakrangee.core.R;
import in.vakrangee.core.utils.CommonUtils;

public class CustomCorrectionRemarksDialog extends Dialog implements android.view.View.OnClickListener {

    private static final String TAG = "CustomAttributeDialog";
    private Context context;
    private TextView txtCorrectionRemarks;
    private LinearLayout parentLayout;
    private Button btnClose;
    private String data;
    private TextView txtTitle;
    private String title = null;

    public CustomCorrectionRemarksDialog(@NonNull Context context, String data) {
        super(context);
        this.context = context;
        this.data = data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        final Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        setContentView(R.layout.dialog_correction_remarks);

        //Widgets
        parentLayout = findViewById(R.id.parentLayout);
        CommonUtils.setDialog(context, parentLayout,35);
        txtTitle = findViewById(R.id.txtTitle);
        btnClose = findViewById(R.id.btnClose);
        btnClose.setTypeface(font);
        btnClose.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + "  "));
        btnClose.setOnClickListener(this);

        txtCorrectionRemarks = findViewById(R.id.txtCorrectionRemarks);
        btnClose.setOnClickListener(this);

        refresh(data);

    }

    public void refresh(String data) {
        this.data = data;

        if (txtCorrectionRemarks != null)
            txtCorrectionRemarks.setText(data);

    }

    // Allow Change Dialog Title Name
    public void setDialogTitle(String title) {

        if (!TextUtils.isEmpty(title)) {
            this.title = title;
            txtTitle.setText(title);
        }
    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();

        if (Id == R.id.btnClose) {
            dismiss();
        }
    }
}
