package in.vakrangee.core.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import androidx.core.content.ContextCompat;

/**
 * Created by Vasundhara on 6/18/2018.
 */

public class DeprecateHandler {

    private Context context;

    public DeprecateHandler(Context context) {
        this.context = context;
    }

    /**
     * Method to get color
     * @param id
     * @return
     */
    public int getColor(int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    /**
     * Method to get Drawable
     * @param id
     * @return
     */
    public Drawable getDrawable(int id) {
        final int sdk = Build.VERSION.SDK_INT;
        if (sdk >= 23) {
            return ContextCompat.getDrawable(context, id);
        } else {
            return context.getResources().getDrawable(id);
        }
    }

    /**
     * Method to set Drawable Background
     * @param drawable
     * @param view
     */
    public void setBackground(Drawable drawable,View view) {
        final int sdk = Build.VERSION.SDK_INT;
          if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(drawable);
        } else {
            view.setBackground(drawable);
        }
    }
}
