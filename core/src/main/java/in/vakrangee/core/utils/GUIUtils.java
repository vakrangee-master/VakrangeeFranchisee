package in.vakrangee.core.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class GUIUtils {

    public static void setViewAndChildrenEnabled(View view, final boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                setViewAndChildrenEnabled(child, enabled);
            }
        }
    }

    //region * mark for Compulsory Edit_text
    public static void CompulsoryMark(TextView textView, String Value) {
        String simple = Value;
        String colored = "*";
        SpannableStringBuilder builder = new SpannableStringBuilder(simple + colored);

        builder.setSpan(new ForegroundColorSpan(Color.RED), simple.length(), builder.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(builder);
    }

    public static void CompulsoryMark(TextView[] allTextViews) {

        for (int i = 0; i < allTextViews.length; i++) {
            TextView textView = allTextViews[i];
            String Value = textView.getText().toString() + " ";
            Value = Value.replace("*", "");
            CompulsoryMark(textView, Value);
        }
    }
    //endregion

    public static void setErrorToSpinner(Spinner spinner, String msg, Context context) {
        Object layout = spinner.getSelectedView();
        if (layout == null || !(layout instanceof LinearLayout)) {
            AlertDialogBoxInfo.alertDialogShow(context, msg);
            return;
        }

        LinearLayout linearLayout = (LinearLayout) layout;
        TextView errorText = (TextView) linearLayout.getChildAt(0);
        if (errorText == null) {
            AlertDialogBoxInfo.alertDialogShow(context, msg);
            return;
        }

        errorText.setError("");
        errorText.setTextColor(Color.RED);//just to highlight that this is an error
        errorText.setText(msg);
    }
}
