package in.vakrangee.franchisee.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;

/**
 * Created by Nileshd on 6/1/2017.
 */
public class MyATMFillDataFragment extends Fragment {

    TelephonyManager telephonyManager;
    String TAG = "Response";
    String diplayServerResopnse;
    InternetConnection internetConnection;
    Connection connection;

    Context context;


    EditText edtchquenumber, edtchquedate, edtchqueamount;
    Button btnrClear, btnrSubmit;
    String cnumber, cdate, camount;
    ImageView Imgcapture, ImgDisplay;
    private Uri picUri;
    final int CAMERA_CAPTURE = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fontawesome-webfont.ttf");

        View rootView = inflater.inflate(R.layout.fragment_atm_fill_data, container, false);

        try {
            telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            connection = new Connection(context);
            internetConnection = new InternetConnection(context);
            telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            setHasOptionsMenu(true);

            edtchquenumber = (EditText) rootView.findViewById(R.id.chquenumber);
            edtchquenumber.setTypeface(Typeface.SANS_SERIF);

            edtchquedate = (EditText) rootView.findViewById(R.id.chquedate);
            edtchquedate.setTypeface(Typeface.SANS_SERIF);


            edtchqueamount = (EditText) rootView.findViewById(R.id.chqueamount);
            edtchqueamount.setTypeface(Typeface.SANS_SERIF);

            btnrClear = (Button) rootView.findViewById(R.id.rCancel);
            btnrClear.setTypeface(font);
            btnrClear.setText(new SpannableStringBuilder(new String(new char[]{0xf021}) + " " + getResources().getString(R.string.clear)));

            btnrSubmit = (Button) rootView.findViewById(R.id.rSubmit);
            btnrSubmit.setTypeface(font);
            btnrSubmit.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " + getResources().getString(R.string.submit)));

            Imgcapture = (ImageView) rootView.findViewById(R.id.imageViewcapture);
            ImgDisplay = (ImageView) rootView.findViewById(R.id.imageVieawdisplay);

            edtchquedate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar mcurrentDate = Calendar.getInstance();
                    int mYear = mcurrentDate.get(Calendar.YEAR);
                    int mMonth = mcurrentDate.get(Calendar.MONTH);
                    int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            getActivity(), AlertDialog.BUTTON_POSITIVE, new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            mcurrentDate.set(Calendar.YEAR, year);
                            mcurrentDate.set(Calendar.MONTH, monthOfYear);
                            mcurrentDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                            String myFormat = "dd-MM-yyyy";
                            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                            edtchquedate.setText(sdf.format(mcurrentDate.getTime()));

                        }
                    }, mYear, mMonth, mDay);


                    //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                    datePickerDialog.show();
                }
            });

            Imgcapture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    File imagesFolder = new File(Environment.getExternalStorageDirectory(), "MyImages");
                    picUri = Uri.fromFile(imagesFolder); // create
                    i.putExtra(MediaStore.EXTRA_OUTPUT, picUri); // set the image file
                    startActivityForResult(i, CAMERA_CAPTURE);


                }
            });
            btnrClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    edtchquenumber.setText("");
                    edtchquedate.setText("");
                    edtchqueamount.setText("");

                    edtchquenumber.setError(null);
                    edtchquedate.setError(null);
                    edtchqueamount.setError(null);

                }
            });

            btnrSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cnumber = edtchquenumber.getText().toString();
                    cdate = edtchquedate.getText().toString();
                    camount = edtchqueamount.getText().toString().trim().toUpperCase();


                    boolean valid = true;
                    if (cnumber.length() < 2) {
                        edtchquenumber.setError("Enter Valid number");
                        valid = false;
                    }
                    if (cdate.length() < 4) {
                        edtchquedate.setError("Enter Date");
                        valid = false;
                    }
                    if (camount.length() < 2) {
                        edtchqueamount.setError("Enter minimum 2 digit Amount");
                        valid = false;
                    }

                    if (internetConnection.isConnectingToInternet() == false) {

                        AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.internetCheck));
                        valid = false;

                    }
                    if (telephonyManager.getSimState() == TelephonyManager.SIM_STATE_ABSENT) {
                        AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.insertSimcard));
                        valid = false;

                    }

                    if (valid) {

                        AlertDialogBoxInfo.alertDialogShow(getActivity(), "Success-full");


                    }

                }
            });


        } catch (Exception e) {
            e.getMessage();
        }
        return rootView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        try {

            if (requestCode == CAMERA_CAPTURE && resultCode == Activity.RESULT_OK) {
                Bitmap bitmapNew = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), picUri);


                // Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(picUri));
                ImgDisplay.setImageBitmap(bitmapNew);


            }


        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }

    }

}