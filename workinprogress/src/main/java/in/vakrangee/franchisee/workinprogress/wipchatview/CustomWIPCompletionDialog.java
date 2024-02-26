package in.vakrangee.franchisee.workinprogress.wipchatview;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import in.vakrangee.franchisee.workinprogress.R;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;

public class CustomWIPCompletionDialog extends Dialog implements android.view.View.OnClickListener {

    private static final String TAG = "CustomWIPCompletionDialog";
    private Context context;
    private LinearLayout parentLayout;
    private Button btnClose, btnSubmit;
    private TextView txtTitle;
    private String title = null;
    private String data;

    public CustomWIPCompletionDialog(@NonNull Context context, String data) {
        super(context);
        this.context = context;
        this.data = data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        final Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        setContentView(R.layout.dialog_wip_completion);

        //Widgets
        parentLayout = findViewById(R.id.parentLayout);
        CommonUtils.setDialogWidth(context, parentLayout);
        txtTitle = findViewById(R.id.txtTitle);
        btnClose = findViewById(R.id.btnClose);
        btnClose.setTypeface(font);
        btnClose.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + "  "));
        btnClose.setOnClickListener(this);

        //Save
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setTypeface(font);
        btnSubmit.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + "  " + context.getResources().getString(R.string.save)));

        //Listeners
        btnSubmit.setOnClickListener(this);

        //Set Adapter
        btnClose.setOnClickListener(this);

        //refresh(data);

    }

    public void refresh(String data) {
        this.data = data;


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
