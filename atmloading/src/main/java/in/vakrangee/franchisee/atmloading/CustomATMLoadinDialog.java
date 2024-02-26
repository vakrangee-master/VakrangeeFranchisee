package in.vakrangee.franchisee.atmloading;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.text.DecimalFormat;

import butterknife.ButterKnife;
import in.vakrangee.supercore.franchisee.model.ATMRoCashLoadingDetailsDto;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.GUIUtils;

public class CustomATMLoadinDialog extends Dialog implements android.view.View.OnClickListener {
    private Context context;
    private static final String TAG = "CustomATMLoadinDialog";
    private TextView txtTitle;
    private LinearLayout parentLayout;
    private Button btnOK, btnClose;
    private CustomATMLoadinDialog.IfcATMLoadingDialogClicks ifcATMLoadingDialogClicks;
    private Object object;
    private String type;
    private TextView textViewCassette1, textViewCassette2, textViewCassette3, textViewCassette4,
            textViewPurgeBinCassette1, textViewPurgeBinCassette2, textViewPurgeBinCassette3,
            textTotalNoteCountPhyscial, textTotalAmountCountPhyscial, textTotalNoteCountPurge, textTotalAmountCountPurge;

    private TextView textPhyscialC1, textPhyscialC2, textPhyscialC3, textPhyscialC4, textPurgeC1, textPurgeC2, textPurgeC3,
            textViewHeaderPhysicalCash, textviewPhysicalCashTotalAmount, textViewPhysicalDifferenceTitle, textviewFinalAmountC3;
    private LinearLayout layoutPurgeBin, layoutMain;
    private EditText edittextFinalAmount;

    public interface IfcATMLoadingDialogClicks {
        public void OkClick(Object object);
    }


    public CustomATMLoadinDialog(@NonNull Context context, Object object, String type, CustomATMLoadinDialog.IfcATMLoadingDialogClicks ifcATMLoadingDialogClicks) {
        super(context);
        this.context = context;
        this.object = object;
        this.type = type;
        this.ifcATMLoadingDialogClicks = ifcATMLoadingDialogClicks;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        ButterKnife.bind(this);
        final Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        setContentView(R.layout.dialog_atm_loading_preview);

        //Widgets
        txtTitle = findViewById(R.id.txtTitle);
        parentLayout = findViewById(R.id.parentLayout);
        //textview
        textViewCassette1 = findViewById(R.id.textViewCassette1);
        textViewCassette2 = findViewById(R.id.textViewCassette2);
        textViewCassette3 = findViewById(R.id.textViewCassette3);
        textViewCassette4 = findViewById(R.id.textViewCassette4);
        textTotalNoteCountPhyscial = findViewById(R.id.textTotalNoteCountPhyscial);
        textTotalAmountCountPhyscial = findViewById(R.id.textTotalAmountCountPhyscial);

        textPhyscialC1 = findViewById(R.id.textPhyscialC1);
        textPhyscialC2 = findViewById(R.id.textPhyscialC2);
        textPhyscialC3 = findViewById(R.id.textPhyscialC3);
        textPhyscialC4 = findViewById(R.id.textPhyscialC4);

        textViewPurgeBinCassette1 = findViewById(R.id.textViewPurgeBinCassette1);
        textViewPurgeBinCassette2 = findViewById(R.id.textViewPurgeBinCassette2);
        textViewPurgeBinCassette3 = findViewById(R.id.textViewPurgeBinCassette3);

        textPurgeC1 = findViewById(R.id.textPurgeC1);
        textPurgeC2 = findViewById(R.id.textPurgeC2);
        textPurgeC3 = findViewById(R.id.textPurgeC3);

        textTotalNoteCountPurge = findViewById(R.id.textTotalNoteCountPurge);
        textTotalAmountCountPurge = findViewById(R.id.textTotalAmountCountPurge);
        textviewPhysicalCashTotalAmount = findViewById(R.id.textviewPhysicalCashTotalAmount);
        textViewPhysicalDifferenceTitle = findViewById(R.id.textViewPhysicalDifferenceTitle);
        textviewFinalAmountC3 = findViewById(R.id.textviewFinalAmountC3);
        edittextFinalAmount = findViewById(R.id.edittextFinalAmount);
        layoutMain = findViewById(R.id.layoutMain);
        //layout
        layoutPurgeBin = findViewById(R.id.layoutPurgeBin);
        textViewHeaderPhysicalCash = findViewById(R.id.textViewHeaderPhysicalCash);

        btnOK = findViewById(R.id.btnOK);
        btnOK.setTypeface(font);
        btnOK.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + "  " + context.getResources().getString(R.string.submit)));

        GUIUtils.CompulsoryMark(textViewPhysicalDifferenceTitle, "Please Enter Amount ");

        //Listeners
        btnOK.setOnClickListener(this);

        btnClose = findViewById(R.id.btnClose);
        btnClose.setTypeface(font);
        btnClose.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + "  "));
        btnClose.setOnClickListener(this);

        CommonUtils.setDialogWidth(context, parentLayout);
        //  CommonUtils.setDialog(context, parentLayout);
        refresh(object, type);

    }

    public void refresh(Object object, String type) {
        try {
            this.object = object;
            if (object instanceof ATMRoCashLoadingDetailsDto) {
                ATMRoCashLoadingDetailsDto loadingDetailsDto = (ATMRoCashLoadingDetailsDto) object;
                if (type.equalsIgnoreCase("1")) {
                    //type 1 - physical and purge bin
                    layoutPurgeBin.setVisibility(View.VISIBLE);
                    textViewHeaderPhysicalCash.setText(" Physical Cash ");

                    textViewCassette1.setText(loadingDetailsDto.getOpeningCassette1());
                    textViewCassette2.setText(loadingDetailsDto.getOpeningCassette2());
                    textViewCassette3.setText(loadingDetailsDto.getOpeningCassette3());
                    textViewCassette4.setText(loadingDetailsDto.getOpeningCassette4());
                    textTotalNoteCountPhyscial.setText(loadingDetailsDto.getTotalPhysicalNote());
                    textTotalAmountCountPhyscial.setText(loadingDetailsDto.getTotalPhysicalAmount());

                    textViewPurgeBinCassette1.setText(loadingDetailsDto.getPurgeCassette1());
                    textViewPurgeBinCassette2.setText(loadingDetailsDto.getPurgeCassette2());
                    textViewPurgeBinCassette3.setText(loadingDetailsDto.getPurgeCassette3());
                    textTotalNoteCountPurge.setText(loadingDetailsDto.getTotalPurgeNote());
                    textTotalAmountCountPurge.setText(loadingDetailsDto.getTotalPurgeAmount());

                    editTextAmountEnter(loadingDetailsDto.getOpeningCassette1(), textPhyscialC1, Long.valueOf(2000));
                    editTextAmountEnter(loadingDetailsDto.getOpeningCassette2(), textPhyscialC2, Long.valueOf(500));
                    editTextAmountEnter(loadingDetailsDto.getOpeningCassette3(), textPhyscialC3, Long.valueOf(100));
                    editTextAmountEnter(loadingDetailsDto.getOpeningCassette4(), textPhyscialC4, Long.valueOf(100));

                    editTextAmountEnter(loadingDetailsDto.getPurgeCassette1(), textPurgeC1, Long.valueOf(2000));
                    editTextAmountEnter(loadingDetailsDto.getPurgeCassette2(), textPurgeC2, Long.valueOf(500));
                    editTextAmountEnter(loadingDetailsDto.getPurgeCassette3(), textPurgeC3, Long.valueOf(100));

                    String totalAmountPhysical = textTotalAmountCountPhyscial.getText().toString().equals("") ? "0" : textTotalAmountCountPhyscial.getText().toString();
                    totalAmountPhysical = totalAmountPhysical.replace(" ₹", "");
                    totalAmountPhysical = totalAmountPhysical.replace(",", "");

                    String totalAmountPurge = textTotalAmountCountPurge.getText().toString().equals("") ? "0" : textTotalAmountCountPurge.getText().toString();
                    totalAmountPurge = totalAmountPurge.replace(" ₹", "");
                    totalAmountPurge = totalAmountPurge.replace(",", "");

                    long total = Integer.parseInt(totalAmountPhysical) + Integer.parseInt(totalAmountPurge);

                    String totalNoteToFormate = new DecimalFormat("##,##,##0").format(total);
                    textviewFinalAmountC3.setText(totalNoteToFormate);

                    //textviewFinalAmountC3.setText(String.valueOf(total));
                } else if (type.equalsIgnoreCase("2")) {
                    //type 2 - cash loading
                    layoutPurgeBin.setVisibility(View.GONE);
                    textViewHeaderPhysicalCash.setText(" Cash Loading ");

                    textViewCassette1.setText(loadingDetailsDto.getLoadCassette1());
                    textViewCassette2.setText(loadingDetailsDto.getLoadCassette2());
                    textViewCassette3.setText(loadingDetailsDto.getLoadCassette3());
                    textViewCassette4.setText(loadingDetailsDto.getLoadCassette4());
                    textTotalNoteCountPhyscial.setText(loadingDetailsDto.getTotalCashNote());
                    textTotalAmountCountPhyscial.setText(loadingDetailsDto.getTotalCashAmount());

                    editTextAmountEnter(loadingDetailsDto.getLoadCassette1(), textPhyscialC1, Long.valueOf(2000));
                    editTextAmountEnter(loadingDetailsDto.getLoadCassette2(), textPhyscialC2, Long.valueOf(500));
                    editTextAmountEnter(loadingDetailsDto.getLoadCassette3(), textPhyscialC3, Long.valueOf(100));
                    editTextAmountEnter(loadingDetailsDto.getLoadCassette4(), textPhyscialC4, Long.valueOf(100));

                    textviewFinalAmountC3.setText(loadingDetailsDto.getTotalCashAmount());
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void editTextAmountEnter(String enterNote, TextView amount, Long note) {
        Long cst = Long.valueOf(enterNote);
        cst = cst * note;

        String moneyString = new DecimalFormat("##,##,##0").format(cst);
        amount.setText(moneyString);
    }


    // Allow Change Dialog Title Name
    public void setDialogTitle(String title) {

        if (!TextUtils.isEmpty(title)) {
            txtTitle.setText(title);
        }
    }


    @Override
    public void onClick(View view) {
        int Id = view.getId();
        if (Id == R.id.btnOK) {
            int amount = validationFinalAmount();
            if (amount != 0) {
                Toast.makeText(context, "ATM amount and enter amount not match", Toast.LENGTH_SHORT).show();
            } else {
                ifcATMLoadingDialogClicks.OkClick(object);
                dismiss();
            }
        } else if (Id == R.id.btnClose) {
            dismiss();
        }
    }

    private int validationFinalAmount() {
        int amount;
        String userEnterAmount = edittextFinalAmount.getText().toString();
        String finalAmountATM = textviewFinalAmountC3.getText().toString();
        String differenceAmount = CommonUtils.differenceVal(userEnterAmount, finalAmountATM);
        amount = Integer.parseInt(differenceAmount);
        return amount;
    }
}
