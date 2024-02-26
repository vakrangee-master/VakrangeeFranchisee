package in.vakrangee.franchisee.sitelayout;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;

public class CustomPremiseShapeDialog extends Dialog implements android.view.View.OnClickListener {

    private static final String TAG = "CustomPremiseShapeDialog";
    private Context context;

    public CustomPremiseShapeDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        final Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        setContentView(R.layout.dialog_custom_premise);

        //Widgets


        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.70);
        getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();

        if (Id == R.id.btnCancel) {
            dismiss();

        }
    }
}
