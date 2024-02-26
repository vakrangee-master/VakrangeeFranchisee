package in.vakrangee.franchisee.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import in.vakrangee.franchisee.R;

import static in.vakrangee.supercore.franchisee.utils.Constants.mgetData;

/**
 * Created by Nileshd on 6/7/2017.
 */
public class MyATMPhysicalCashFragment extends Fragment {

    View rootView;
    EditText edt200, edt500, edt100, edt100new;
    EditText edtpurge2000, edtpurge500, edtpurge100, edtpurge1004;
    TextView txt200, txt500, txt100, txt100new, totalamount, totalPurge, totalcasstte;

    Bundle bundle;
    Button btnSubmitAmount;
    ViewPager viewPager;
    int value2000, value500, value100;

    public MyATMPhysicalCashFragment(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_atm_physical_cash_loading, container, false);
        totalamount = (TextView) rootView.findViewById(R.id.totalamount);
        totalPurge = (TextView) rootView.findViewById(R.id.totalPurge);
        totalcasstte = (TextView) rootView.findViewById(R.id.totalcasstte);
        btnSubmitAmount = (Button) rootView.findViewById(R.id.btnSubmitAmount);
        btnSubmitAmount.setVisibility(View.INVISIBLE);

        bundle = getArguments();
        if (bundle != null) {

            value2000 = Integer.parseInt(getArguments().getString("edt200"));
            value500 = Integer.parseInt(getArguments().getString("edt500"));
            value100 = Integer.parseInt(getArguments().getString("edt100"));
            //totalPurge.setText(value2000 + "\n" + value500 + " \n"+ value100);
            btnSubmitAmount.setVisibility(View.VISIBLE);
        }

        edt200 = (EditText) rootView.findViewById(R.id.TotalAmt2000);
        edt200.setTypeface(Typeface.SANS_SERIF);
        edt200.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    edt200.setHint("");
                else
                    edt200.setHint("0");
            }
        });
        edt500 = (EditText) rootView.findViewById(R.id.TotalAmt500);
        edt500.setTypeface(Typeface.SANS_SERIF);
        edt500.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    edt500.setHint("");
                else
                    edt500.setHint("0");
            }
        });
        edt100 = (EditText) rootView.findViewById(R.id.TotalAmt100);
        edt100.setTypeface(Typeface.SANS_SERIF);
        edt100.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    edt100.setHint("");
                else
                    edt100.setHint("0");
            }
        });
        edt100new = (EditText) rootView.findViewById(R.id.TotalAmt1004);
        edt100new.setTypeface(Typeface.SANS_SERIF);
        edt100new.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    edt100new.setHint("");
                else
                    edt100new.setHint("0");
            }
        });
        edtpurge2000 = (EditText) rootView.findViewById(R.id.edtpurge2000);
        edtpurge2000.setTypeface(Typeface.SANS_SERIF);
        edtpurge2000.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    edtpurge2000.setHint("");
                else
                    edtpurge2000.setHint("0");
            }
        });
        edtpurge500 = (EditText) rootView.findViewById(R.id.edtpurge500);
        edtpurge500.setTypeface(Typeface.SANS_SERIF);
        edtpurge500.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    edtpurge500.setHint("");
                else
                    edtpurge500.setHint("0");
            }
        });
        edtpurge100 = (EditText) rootView.findViewById(R.id.edtpurge100);
        edtpurge100.setTypeface(Typeface.SANS_SERIF);
        edtpurge100.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    edtpurge100.setHint("");
                else
                    edtpurge100.setHint("0");
            }
        });
        edtpurge1004 = (EditText) rootView.findViewById(R.id.edtpurge1004);
        edtpurge1004.setTypeface(Typeface.SANS_SERIF);
        edtpurge1004.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    edtpurge1004.setHint("");
                else
                    edtpurge1004.setHint("0");
            }
        });
        txt200 = (TextView) rootView.findViewById(R.id.txt200);
        txt500 = (TextView) rootView.findViewById(R.id.txt500);
        txt100 = (TextView) rootView.findViewById(R.id.txt100);
        txt100new = (TextView) rootView.findViewById(R.id.txt1004);

        TextwatcherFunction();

        btnSubmitAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                   // mgetData = new ATMgetData();
                    int a2 = edt200.getText().toString().equals("") ? 0 : Integer.parseInt(edt200.getText().toString());
                    int a5 = edt500.getText().toString().equals("") ? 0 : Integer.parseInt(edt500.getText().toString());
                    int a1 = edt100.getText().toString().equals("") ? 0 : Integer.parseInt(edt100.getText().toString());
                    int a1n = edt100new.getText().toString().equals("") ? 0 : Integer.parseInt(edt100new.getText().toString());
                    int P2 = edtpurge2000.getText().toString().equals("") ? 0 : Integer.parseInt(edtpurge2000.getText().toString());
                    int P5 = edtpurge500.getText().toString().equals("") ? 0 : Integer.parseInt(edtpurge500.getText().toString());
                    int P1 = edtpurge100.getText().toString().equals("") ? 0 : Integer.parseInt(edtpurge100.getText().toString());
                    int P1n = edtpurge1004.getText().toString().equals("") ? 0 : Integer.parseInt(edtpurge1004.getText().toString());

                    int foo2000 = value2000 + a2 + P2;
                    int f00500 = value500 + a5 + P5;
                    int foo100 = value100 + a1 + P1;
                    int foo100new = a1n + P1n;
                    Log.e("Total get count ", " : 2000 :  " + String.valueOf(foo2000) + ": 500 : " +
                            String.valueOf(f00500) + " : 100 : " + String.valueOf(foo100));

                    MyATMCashLoadFragment fragment = new MyATMCashLoadFragment(viewPager);
                    Bundle bundle = new Bundle();
                    bundle.putString("edt200", Integer.toString(foo2000));
                    bundle.putString("edt500", Integer.toString(f00500));
                    bundle.putString("edt100", Integer.toString(foo100));
                    bundle.putString("edt100new", Integer.toString(foo100new));
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frgATMCashaa, fragment).commit();

                    mgetData.setCb_c_1000(String.valueOf(a2));
                    mgetData.setCb_c_500(String.valueOf(a5));
                    mgetData.setCb_c_100(String.valueOf(a1));
                    mgetData.setCb_c_0(String.valueOf(a1n));

                    mgetData.setCb_p_1000(String.valueOf(P2));
                    mgetData.setCb_p_500(String.valueOf(P5));
                    mgetData.setCb_p_100(String.valueOf(P1));
                    mgetData.setCb_p_0(String.valueOf(P1n));

                    mgetData.setDis_1000("0");
                    mgetData.setDis_500("0");
                    mgetData.setDis_100("0");
                    mgetData.setDis_0("0");

                    viewPager.setCurrentItem(3);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        return rootView;
    }

    private void TextwatcherFunction() {
        final ArrayList<EditText> editTexts = new ArrayList<>(); // Container list
        editTexts.add(edtpurge2000); // editTexts[0]
        editTexts.add(edtpurge500); // editTexts[1]
        editTexts.add(edtpurge100); // editTexts[2]
        editTexts.add(edtpurge1004);// editTexts[4]

        editTexts.add(edt200);
        editTexts.add(edt500);
        editTexts.add(edt100);
        editTexts.add(edt100new);

        for (final EditText editText : editTexts) { //need to be final for custom behaviors
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    totalamount.setText(addNumbers());
                    txt200.setText(getTotal2000());
                    txt500.setText(getTotal500());
                    txt100.setText(getTotal100());
                    txt100new.setText(getTotal100new());
                    totalcasstte.setText(getTotalCasste());
                    totalPurge.setText(getTotalPurge());
                }

                @Override
                public void afterTextChanged(Editable s) {
                    //Apply general behavior for all editTexts

                }
            });


        }

    }

    private String getTotalPurge() {

        Long number5;
        Long number6;
        Long number7;
        Long number8;

        if (edtpurge2000.getText().toString() != "" && edtpurge2000.getText().length() > 0) {
            number5 = Long.parseLong(edtpurge2000.getText().toString());
        } else {
            number5 = Long.valueOf(0);
        }

        if (edtpurge500.getText().toString() != "" && edtpurge500.getText().length() > 0) {
            number6 = Long.parseLong(edtpurge500.getText().toString());
        } else {
            number6 = Long.valueOf(0);
        }
        if (edtpurge100.getText().toString() != "" && edtpurge100.getText().length() > 0) {
            number7 = Long.parseLong(edtpurge100.getText().toString());
        } else {
            number7 = Long.valueOf(0);
        }

        if (edtpurge1004.getText().toString() != "" && edtpurge1004.getText().length() > 0) {
            number8 = Long.parseLong(edtpurge1004.getText().toString());
        } else {
            number8 = Long.valueOf(0);
        }
        return Long.toString((int) (number5 + number6 + number7 + number8));
    }

    private String getTotalCasste() {
        Long number1;
        Long number2;
        Long number3;
        Long number4;
        if (edt200.getText().toString() != "" && edt200.getText().length() > 0) {
            number1 = Long.parseLong(edt200.getText().toString());
        } else {
            number1 = Long.valueOf(0);
        }
        if (edt500.getText().toString() != "" && edt500.getText().length() > 0) {
            number2 = Long.parseLong(edt500.getText().toString());
        } else {
            number2 = Long.valueOf(0);
        }

        if (edt100.getText().toString() != "" && edt100.getText().length() > 0) {
            number3 = Long.parseLong(edt100.getText().toString());
        } else {
            number3 = Long.valueOf(0);
        }
        if (edt100new.getText().toString() != "" && edt100new.getText().length() > 0) {
            number4 = Long.parseLong(edt100new.getText().toString());
        } else {
            number4 = Long.valueOf(0);
        }

        return Long.toString((int) (number1 + number2 + number3 + number4));
    }

    private String getTotal100new() {
        Long number1;
        Long number2;
        if (edt100new.getText().toString() != "" && edt100new.getText().length() > 0) {
            number1 = Long.parseLong(edt100new.getText().toString());
        } else {
            number1 = Long.valueOf(0);
        }
        if (edtpurge1004.getText().toString() != "" && edtpurge1004.getText().length() > 0) {
            number2 = Long.parseLong(edtpurge1004.getText().toString());
        } else {
            number2 = Long.valueOf(0);
        }

        return Long.toString((int) (number1 * 100 + number2 * 100));
    }

    private String getTotal500() {
        Long number1;
        Long number2;
        if (edt500.getText().toString() != "" && edt500.getText().length() > 0) {
            number1 = Long.parseLong(edt500.getText().toString());
        } else {
            number1 = Long.valueOf(0);
        }
        if (edtpurge500.getText().toString() != "" && edtpurge500.getText().length() > 0) {
            number2 = Long.parseLong(edtpurge500.getText().toString());
        } else {
            number2 = Long.valueOf(0);
        }

        return Long.toString((int) (number1 * 500 + number2 * 500));
    }

    private String getTotal100() {
        Long number1;
        Long number2;
        if (edt100.getText().toString() != "" && edt100.getText().length() > 0) {
            number1 = Long.parseLong(edt100.getText().toString());
        } else {
            number1 = Long.valueOf(0);
        }
        if (edtpurge100.getText().toString() != "" && edtpurge100.getText().length() > 0) {
            number2 = Long.parseLong(edtpurge100.getText().toString());
        } else {
            number2 = Long.valueOf(0);
        }

        return Long.toString((int) (number1 * 100 + number2 * 100));
    }

    private String getTotal2000() {
        Long number1;
        Long number2;
        if (edt200.getText().toString() != "" && edt200.getText().length() > 0) {
            number1 = Long.parseLong(edt200.getText().toString());
        } else {
            number1 = Long.valueOf(0);
        }
        if (edtpurge2000.getText().toString() != "" && edtpurge2000.getText().length() > 0) {
            number2 = Long.parseLong(edtpurge2000.getText().toString());
        } else {
            number2 = Long.valueOf(0);
        }

        return Long.toString((int) (number1 * 2000 + number2 * 2000));
    }

    private String addNumbers() {
        Long number1;
        Long number2;
        Long number3;
        Long number4;

        Long number5;
        Long number6;
        Long number7;
        Long number8;

        if (edt200.getText().toString() != "" && edt200.getText().length() > 0) {
            number1 = Long.parseLong(edt200.getText().toString());
        } else {
            number1 = Long.valueOf(0);
        }
        if (edt500.getText().toString() != "" && edt500.getText().length() > 0) {
            number2 = Long.parseLong(edt500.getText().toString());
        } else {
            number2 = Long.valueOf(0);
        }

        if (edt100.getText().toString() != "" && edt100.getText().length() > 0) {
            number3 = Long.parseLong(edt100.getText().toString());
        } else {
            number3 = Long.valueOf(0);
        }
        if (edt100new.getText().toString() != "" && edt100new.getText().length() > 0) {
            number4 = Long.parseLong(edt100new.getText().toString());
        } else {
            number4 = Long.valueOf(0);
        }

        if (edtpurge2000.getText().toString() != "" && edtpurge2000.getText().length() > 0) {
            number5 = Long.parseLong(edtpurge2000.getText().toString());
        } else {
            number5 = Long.valueOf(0);
        }

        if (edtpurge500.getText().toString() != "" && edtpurge500.getText().length() > 0) {
            number6 = Long.parseLong(edtpurge500.getText().toString());
        } else {
            number6 = Long.valueOf(0);
        }
        if (edtpurge100.getText().toString() != "" && edtpurge100.getText().length() > 0) {
            number7 = Long.parseLong(edtpurge100.getText().toString());
        } else {
            number7 = Long.valueOf(0);
        }

        if (edtpurge1004.getText().toString() != "" && edtpurge1004.getText().length() > 0) {
            number8 = Long.parseLong(edtpurge1004.getText().toString());
        } else {
            number8 = Long.valueOf(0);
        }

        return Long.toString((int) (number1 * 2000 + number2 * 500 + number3 * 100 + number4 * 100 + number5 * 2000 +
                number6 * 500 + number7 * 100 + number8 * 100));
    }


}