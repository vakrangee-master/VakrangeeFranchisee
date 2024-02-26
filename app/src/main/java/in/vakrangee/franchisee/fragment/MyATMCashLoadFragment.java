package in.vakrangee.franchisee.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import in.vakrangee.franchisee.R;

import static in.vakrangee.supercore.franchisee.utils.Constants.mgetData;

/**
 * Created by Nileshd on 6/7/2017.
 */
public class MyATMCashLoadFragment extends Fragment {


    View rootView;

    EditText edtpurge2000, edtpurge500, edtpurge100, edtpurge1004;
    TextView txt200, txt500, txt100, txt100new, totalamount, txttotalcasstte;
    Bundle bundle;
    ViewPager viewPager;
    Button btnSubmitAmount;
    int value2000, value500, value100, values100new;

    public MyATMCashLoadFragment(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_atm_cash_load, container, false);

        edtpurge2000 = (EditText) rootView.findViewById(R.id.edtpurge2000);
        edtpurge2000.setTypeface(Typeface.SANS_SERIF);

        edtpurge500 = (EditText) rootView.findViewById(R.id.edtpurge500);
        edtpurge500.setTypeface(Typeface.SANS_SERIF);

        edtpurge100 = (EditText) rootView.findViewById(R.id.edtpurge100);
        edtpurge100.setTypeface(Typeface.SANS_SERIF);

        edtpurge1004 = (EditText) rootView.findViewById(R.id.edtpurge1004);
        edtpurge1004.setTypeface(Typeface.SANS_SERIF);

        txt200 = (TextView) rootView.findViewById(R.id.txt200);
        txt500 = (TextView) rootView.findViewById(R.id.txt500);
        txt100 = (TextView) rootView.findViewById(R.id.txt100);
        txt100new = (TextView) rootView.findViewById(R.id.txt1004);
        txttotalcasstte = (TextView) rootView.findViewById(R.id.txttotalcasstte);
        totalamount = (TextView) rootView.findViewById(R.id.totalamount);
        btnSubmitAmount = (Button) rootView.findViewById(R.id.btnSubmitAmount);
        btnSubmitAmount.setVisibility(View.INVISIBLE);
        bundle = getArguments();

        bundle = getArguments();
        if (bundle != null) {
            try {


                value2000 = Integer.parseInt(getArguments().getString("edt200"));
                value500 = Integer.parseInt(getArguments().getString("edt500"));
                value100 = Integer.parseInt(getArguments().getString("edt100"));
                values100new = Integer.parseInt(getArguments().getString("edt100new"));
                //totalPurge.setText(value2000 + "\n" + value500 + " \n"+ value100);
                btnSubmitAmount.setVisibility(View.VISIBLE);
                edtpurge2000.setText(String.valueOf(value2000));
                edtpurge500.setText(String.valueOf(value500));
                edtpurge100.setText(String.valueOf(value100));
                edtpurge1004.setText(String.valueOf(values100new));

                int a = Integer.parseInt(String.valueOf(value2000 + value500 + value100 + values100new));
                txttotalcasstte.setText(String.valueOf(a));
                Double b = Double.parseDouble(String.valueOf(value2000 * 2000 + value500 * 500 + value100 * 100 + values100new * 100));
                totalamount.setText(String.valueOf(b));


            } catch (Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                e.getMessage();
            }
        }

        btnSubmitAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mgetData = new ATMgetData();
                Toast.makeText(getActivity(), "CA-A " + value2000 + "\n  CA-B" + value500 + "\n CA-C " + value100 + "\n CA-D " + values100new, Toast.LENGTH_SHORT).show();

                mgetData.setCb_l_1000(String.valueOf(value2000));
                mgetData.setCb_l_500(String.valueOf(value500));
                mgetData.setCb_l_100(String.valueOf(value100));
                mgetData.setCb_l_0(String.valueOf(values100new));

                //------------------------set Extra Parameters
                mgetData.setCb_c_100("0");
                mgetData.setCb_c_500("0");
                mgetData.setCb_c_100("0");
                mgetData.setCb_c_0("0");

                mgetData.setCb_o_1000("0");
                mgetData.setCb_o_500("0");
                mgetData.setCb_o_100("0");
                mgetData.setCb_o_0("0");

                mgetData.setCounter_b_1000("0");
                mgetData.setCounter_b_500("0");
                mgetData.setCounter_b_100("0");
                mgetData.setCounter_b_0("0");

                mgetData.setCounter_a_1000("0");
                mgetData.setCounter_a_500("0");
                mgetData.setCounter_a_100("0");
                mgetData.setCounter_a_0("0");

                mgetData.setSwitch_b_1000("0");
                mgetData.setSwitch_b_500("0");
                mgetData.setSwitch_b_100("0");
                mgetData.setSwitch_b_0("0");

                mgetData.setSwitch_a_1000("0");
                mgetData.setSwitch_a_500("0");
                mgetData.setSwitch_a_100("0");
                mgetData.setSwitch_a_0("0");

                mgetData.setAtmBeforeLoading("0");
                mgetData.setAtmAfterLoading("0");

                mgetData.setSwtichBeforeLoading("0");
                mgetData.setSwtichAfterLoading("0");
                new AsyncAddAtmRoCashLoading(getActivity()).execute();

            }
        });
        TextwatcherFunction();

        return rootView;
    }


    private void TextwatcherFunction() {
        final ArrayList<EditText> editTexts = new ArrayList<>(); // Container list
        editTexts.add(edtpurge2000); // editTexts[0]
        editTexts.add(edtpurge500); // editTexts[1]
        editTexts.add(edtpurge100); // editTexts[2]
        editTexts.add(edtpurge1004);// editTexts[4]


        for (final EditText editText : editTexts) { //need to be final for custom behaviors
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    totalamount.setText(addNumbers());
                    txttotalcasstte.setText(totalCastte());
                }

                @Override
                public void afterTextChanged(Editable s) {
                    //Apply general behavior for all editTexts
                    if (editText == editTexts.get(0)) {
                        if (s.length() > 0) {
                            String e = edtpurge2000.getText().toString();
                            Long cst = Long.valueOf(e);
                            cst = cst * 2000;
                            txt200.setText(cst.toString());
                        } else {
                            txt200.setText("");
                        }
                    }
                    if (editText == editTexts.get(1)) {
                        if (s.length() > 0) {
                            String e = edtpurge500.getText().toString();
                            Long cst = Long.valueOf(e);
                            cst = cst * 500;
                            txt500.setText(cst.toString());
                        } else {
                            txt500.setText("");
                        }
                    }
                    if (editText == editTexts.get(2)) {
                        if (s.length() > 0) {
                            String e = edtpurge100.getText().toString();
                            Long cst = Long.valueOf(e);
                            cst = cst * 100;
                            txt100.setText(cst.toString());
                        } else {
                            txt100.setText("");
                        }

                    }
                    if (editText == editTexts.get(3)) {
                        if (s.length() > 0) {
                            String e = edtpurge1004.getText().toString();
                            Long cst = Long.valueOf(e);
                            cst = cst * 100;
                            txt100new.setText(cst.toString());
                        } else {
                            txt100new.setText("");
                        }


                    }

                }
            });


        }

    }

    private String totalCastte() {
        Long number1;
        Long number2;
        Long number3;
        Long number4;
        if (edtpurge2000.getText().toString() != "" && edtpurge2000.getText().length() > 0) {
            number1 = Long.parseLong(edtpurge2000.getText().toString());
        } else {
            number1 = Long.valueOf(0);
        }
        if (edtpurge500.getText().toString() != "" && edtpurge500.getText().length() > 0) {
            number2 = Long.parseLong(edtpurge500.getText().toString());
        } else {
            number2 = Long.valueOf(0);
        }

        if (edtpurge100.getText().toString() != "" && edtpurge100.getText().length() > 0) {
            number3 = Long.parseLong(edtpurge100.getText().toString());
        } else {
            number3 = Long.valueOf(0);
        }

        if (edtpurge1004.getText().toString() != "" && edtpurge1004.getText().length() > 0) {
            number4 = Long.parseLong(edtpurge1004.getText().toString());
        } else {
            number4 = Long.valueOf(0);
        }

        return Long.toString((int) (number1 + number2 + number3 + number4));
    }

    private String addNumbers() {
        Long number1;
        Long number2;
        Long number3;
        Long number4;
        if (edtpurge2000.getText().toString() != "" && edtpurge2000.getText().length() > 0) {
            number1 = Long.parseLong(edtpurge2000.getText().toString());
        } else {
            number1 = Long.valueOf(0);
        }
        if (edtpurge500.getText().toString() != "" && edtpurge500.getText().length() > 0) {
            number2 = Long.parseLong(edtpurge500.getText().toString());
        } else {
            number2 = Long.valueOf(0);
        }

        if (edtpurge100.getText().toString() != "" && edtpurge100.getText().length() > 0) {
            number3 = Long.parseLong(edtpurge100.getText().toString());
        } else {
            number3 = Long.valueOf(0);
        }

        if (edtpurge1004.getText().toString() != "" && edtpurge1004.getText().length() > 0) {
            number4 = Long.parseLong(edtpurge1004.getText().toString());
        } else {
            number4 = Long.valueOf(0);
        }

        return Long.toString((int) (number1 * 2000 + number2 * 500 + number3 * 100 + number4 * 100));
    }


}
