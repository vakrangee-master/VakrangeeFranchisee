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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import in.vakrangee.core.R;

public class DateTimePickerDialog extends Dialog implements android.view.View.OnClickListener {

    private final String TAG = "DateTimePickerDialog";

    private Context context;
    private TextView txtSetDate, txtSetTime;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button btnCancel, btnSchedule;
    private LinearLayout parentLayout;

    private Animation bounce;

    private static DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

    private int year, monthOfYear, dayOfMonth, hourOfDay, minute;
    private String selectedDate;

    private String strActionName;
    private long minDate = 0;
    private long maxDate = 0;

    private IDateTimePicker iDateTimePicker;

    private boolean isOnlyDate = false;
    private Date defaultDate;

    public interface IDateTimePicker {
        void getDateTime(Date datetime, String defaultFormattedDateTime);
    }

    public DateTimePickerDialog(@NonNull Context context, @NonNull IDateTimePicker iDateTimePicker) {
        super(context);
        this.context = context;
        this.iDateTimePicker = iDateTimePicker;
    }

    public DateTimePickerDialog(@NonNull Context context, boolean isOnlyDate, Date defaultDate, @NonNull IDateTimePicker iDateTimePicker) {
        super(context);
        this.context = context;
        this.iDateTimePicker = iDateTimePicker;
        this.isOnlyDate = isOnlyDate;
        this.defaultDate = defaultDate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        final Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        setContentView(R.layout.dialog_datetime_picker);

        bounce = AnimationUtils.loadAnimation(context, R.anim.bounce);

        //Initialize Views
        parentLayout = findViewById(R.id.parentLayout);
        txtSetDate = (TextView) findViewById(R.id.txtSetDate);
        txtSetTime = (TextView) findViewById(R.id.txtSetTime);

        datePicker = (DatePicker) findViewById(R.id.datePicker);
        timePicker = (TimePicker) findViewById(R.id.timePicker);

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnSchedule = (Button) findViewById(R.id.btnSchedule);

        btnCancel.setTypeface(font);
        btnCancel.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + " Cancel "));

        btnSchedule.setTypeface(font);
        if (TextUtils.isEmpty(strActionName)) {
            btnSchedule.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " Schedule "));
        } else {
            btnSchedule.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " + strActionName + " "));
        }

        txtSetDate.setOnClickListener(this);
        txtSetTime.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSchedule.setOnClickListener(this);

        //Set Current Date and Time to DatePicker and TimePicker
        Calendar now = Calendar.getInstance();
        if (defaultDate != null) {
            //now.setTime(defaultDate);
            now.set(Calendar.YEAR, defaultDate.getYear() + 1900);
            now.set(Calendar.MONTH, defaultDate.getMonth());
            now.set(Calendar.DATE, defaultDate.getDate());
            now.set(Calendar.HOUR_OF_DAY, 0);
            now.set(Calendar.MINUTE, 0);
            now.set(Calendar.SECOND, 0);
            now.set(Calendar.MILLISECOND, 0);
        }
        year = now.get(Calendar.YEAR);
        monthOfYear = now.get(Calendar.MONTH);
        dayOfMonth = now.get(Calendar.DAY_OF_MONTH);
        datePicker.init(year,
                monthOfYear,
                dayOfMonth, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int dYear, int dMonthOfYear, int dDayOfMonth) {
                        year = dYear;
                        monthOfYear = dMonthOfYear;
                        dayOfMonth = dDayOfMonth;
                    }
                });


        if (minDate == 0)
            datePicker.setMinDate(System.currentTimeMillis() - 1000);       // Disable Past Date
        else
            datePicker.setMinDate(minDate);                                // Disable Provided Date

        if (maxDate != 0)
            datePicker.setMaxDate(maxDate);

        hourOfDay = now.get(Calendar.HOUR_OF_DAY);
        minute = now.get(Calendar.MINUTE);
        timePicker.setCurrentHour(hourOfDay);
        timePicker.setCurrentMinute(minute);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int tHourOfDay, int tMinute) {
                hourOfDay = tHourOfDay;
                minute = tMinute;
            }
        });

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthOfYear);
        cal.set(Calendar.DATE, dayOfMonth);
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        selectedDate = dateFormatter.format(cal.getTime());

        //selectedDate = dateFormatter.format(now.getTime());

        // Hide TimePicker
        if (isOnlyDate)
            txtSetTime.setVisibility(View.GONE);
        else
            txtSetTime.setVisibility(View.VISIBLE);

       /* int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.70);
        getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);*/

    }

    /**
     * Set Minimum Date
     *
     * @param minDate
     */
    public void setMinDate(long minDate) {
        this.minDate = minDate;
    }

    /**
     * Set Maximum Date
     *
     * @param maxDate
     */
    public void setMaxDate(long maxDate) {
        this.maxDate = maxDate;

    }

    /**
     * Set Action Button Name
     *
     * @param actionButtonName
     */
    public void setActionButtonName(String actionButtonName) {
        this.strActionName = actionButtonName;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnSchedule) {//TODO: Get Date and Time from DatePicker & TimePicker

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, monthOfYear);
            cal.set(Calendar.DATE, dayOfMonth);
            if (isOnlyDate) {
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
            } else {
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                cal.set(Calendar.MINUTE, minute);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
            }

            selectedDate = dateFormatter.format(cal.getTime());

            if (iDateTimePicker != null) {
                iDateTimePicker.getDateTime(cal.getTime(), selectedDate);
            }

            dismiss();
        } else if (id == R.id.btnCancel) {
            dismiss();
        } else if (id == R.id.txtSetDate) {//txtSetDate.clearAnimation();
            //txtSetDate.startAnimation(bounce);
            datePicker.setVisibility(View.VISIBLE);
            timePicker.setVisibility(View.GONE);
        } else if (id == R.id.txtSetTime) {//txtSetTime.clearAnimation();
            //txtSetTime.startAnimation(bounce);
            datePicker.setVisibility(View.GONE);
            timePicker.setVisibility(View.VISIBLE);
        }
    }
}
