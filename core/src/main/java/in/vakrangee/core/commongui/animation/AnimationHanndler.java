package in.vakrangee.core.commongui.animation;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import in.vakrangee.core.R;


/**
 * Created by nilesh.y on 1/13/2017.
 */

public class AnimationHanndler {

    /**
     *
     * @param context
     * @param view: Clicked View which needs to be animated
     */

    public static void bubbleAnimation(Context context, View view){

        final Animation buttonAnimation = AnimationUtils.loadAnimation(context, R.anim.button_bounce);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        BounceEffectInterpolator interpolator = new BounceEffectInterpolator(0.2, 20);
        buttonAnimation.setInterpolator(interpolator);
        view.startAnimation(buttonAnimation);

    }

    public static void setActionBarColor(AppCompatActivity appCompatActivity, int colorId){
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(getColor(appCompatActivity, colorId));
        actionBar.setBackgroundDrawable(colorDrawable);
    }

    public static final int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }
}
