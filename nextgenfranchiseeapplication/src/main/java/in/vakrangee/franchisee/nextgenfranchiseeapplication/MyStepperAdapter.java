package in.vakrangee.franchisee.nextgenfranchiseeapplication;

import android.content.Context;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

public class MyStepperAdapter extends AbstractFragmentStepAdapter {

    private static final int TOTAL_COUNT = 8;

    public MyStepperAdapter(FragmentManager fm, Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return TOTAL_COUNT;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        //Override this method to set Step title for the Tabs, not necessary for other stepper types
        String title = getTitle(position);
        return new StepViewModel.Builder(context)
                .setTitle(title) //can be a CharSequence instead
                .create();
    }

    public String getTitle(int pos) {
        String title = "";
        switch (pos) {
            case 0:
                title = "Franchisee Details";
                break;

            case 1:
                title = "Address";
                break;

            case 2:
                title = "Contact Information";
                break;

            case 3:
                title = "General Information";
                break;

            case 4:
                title = "Bank Details";
                break;

            case 5:
                title = "Proposed Vakrangee Kendra Detail";
                break;

            case 6:
                title = "References";
                break;

            case 7:
                title = "Criteria";
                break;

            default:
                title = "Franchisee Details";
                break;
        }

        return title;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
