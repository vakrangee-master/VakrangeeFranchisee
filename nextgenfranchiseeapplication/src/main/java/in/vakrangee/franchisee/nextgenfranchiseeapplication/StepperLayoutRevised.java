package in.vakrangee.franchisee.nextgenfranchiseeapplication;

import android.content.Context;
import android.util.AttributeSet;

import com.stepstone.stepper.StepperLayout;

public class StepperLayoutRevised extends StepperLayout {

    private Context mContext;

    public StepperLayoutRevised(Context context) {
        super(context);
        this.mContext = context;
    }

    public StepperLayoutRevised(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public StepperLayoutRevised(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    @Override
    public void onTabClicked(int position) {
        setCurrentStepPosition(position);
    }

}
