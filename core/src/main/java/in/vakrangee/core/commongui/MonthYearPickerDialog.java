package in.vakrangee.core.commongui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

import in.vakrangee.core.R;

public class MonthYearPickerDialog extends DialogFragment {

    private static final String TAG = "MonthYearPickerDialog";
    private NumberPicker monthPicker;
    private NumberPicker yearPicker;
    private int current_date;
    private boolean IsMonthVisible = true;
    private DatePickerDialog.OnDateSetListener listener;
    final String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final Calendar cal = Calendar.getInstance();

        View dialog = inflater.inflate(R.layout.date_picker_dialog, null);
        monthPicker = (NumberPicker) dialog.findViewById(R.id.picker_month);
        yearPicker = (NumberPicker) dialog.findViewById(R.id.picker_year);
        current_date = cal.get(Calendar.DAY_OF_MONTH);

        //MIN MAX Month
        monthPicker.setMinValue(0);
        monthPicker.setMaxValue(11);
        monthPicker.setValue(cal.get(Calendar.MONTH));
        monthPicker.setDisplayedValues(monthNames);

        //MIN MAX Year
        int year = cal.get(Calendar.YEAR);
        yearPicker.setMinValue(1900);
        yearPicker.setMaxValue(year);
        yearPicker.setValue(year);

        if (IsMonthVisible)
            monthPicker.setVisibility(View.VISIBLE);
        else
            monthPicker.setVisibility(View.GONE);

        //Listener
        yearPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //  Toast.makeText(getContext(), "Value was: " +String.valueOf(oldVal) + " is now: " + String.valueOf(newVal), Toast.LENGTH_SHORT).show();
                int year = cal.get(Calendar.YEAR);
                if (year <= newVal) {
                    monthPicker.setMaxValue(cal.get(Calendar.MONTH));
                    monthPicker.setDisplayedValues(monthNames);

                } else {
                    monthPicker.setMaxValue(11);
                    monthPicker.setDisplayedValues(monthNames);
                }
            }
        });

        //View
        builder.setView(dialog)
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDateSet(null, yearPicker.getValue(), monthPicker.getValue(), current_date);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MonthYearPickerDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    public void setMonthVisiblity(boolean IsVisible) {
        this.IsMonthVisible = IsVisible;
    }
}
