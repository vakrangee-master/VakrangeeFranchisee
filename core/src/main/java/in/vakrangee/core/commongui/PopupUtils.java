package in.vakrangee.core.commongui;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import androidx.annotation.NonNull;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import in.vakrangee.core.R;

public class PopupUtils {

    private static final String TAG = PopupUtils.class.getCanonicalName();
    private static Context context;
    private static String popupMessage;



    public static void show(@NonNull Context context, @NonNull View anchor, @NonNull String popupMessage) {

        context = context;
        popupMessage = popupMessage;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View popupContent = inflater.inflate(R.layout.popup_message, null);
        TextView textViewMessage = popupContent.findViewById(R.id.txt_popup_message);
        textViewMessage.setText(Html.fromHtml(popupMessage));
        textViewMessage.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        popupContent.bringToFront();

        int[] location = new int[2];
        anchor.getLocationOnScreen(location);
        //final View mView = getLayoutInflater().inflate(R.layout.xxxx, null, false);
        final PopupWindow popUp = new PopupWindow(popupContent, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, false);
        popUp.setBackgroundDrawable(new BitmapDrawable());
        popUp.setTouchable(true);
        popUp.setFocusable(true);
        popUp.setOutsideTouchable(true);
        popUp.showAtLocation(anchor, Gravity.NO_GRAVITY, location[0], location[1] ); //-  (LinearLayout.LayoutParams.WRAP_CONTENT / 2)
    }

}
