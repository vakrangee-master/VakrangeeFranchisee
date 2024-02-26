package in.vakrangee.franchisee.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.utils.VerhoeffAlgorithmAadharcardValidation;

//

public class MyCustomerProflie extends AppCompatActivity {
    Toolbar toolbar;
    private static final int CAMERA_REQUEST = 1888;
    InternetConnection internetConnection;
    EditText edtJoingDate, edtFirstname, edtMiddleName, edtLastName, edtMobileNumber, edtAlertnetMobileNumber, edtEmailId, edtAlertnetMailId,
            edtAadharnumber, edtPanNumber, edtElectionIdNumber, edtDrivingLicence, edtAddress, edtPincode, edtBirthdayDate, edtMarraiageDate;
    Button btnSubmitCustomerProflie, btnClear, btnCancle;


    String sFirstNam, sMobileNumber, sJoiningDate, sMiddName, sLastName;

    ImageView btncaptureimage, imgProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fontawesome-webfont.ttf");

        setContentView(R.layout.activity_my_customer_proflie);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.customerprofile);

//            final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
//            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//            getSupportActionBar().setHomeAsUpIndicator(upArrow);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        internetConnection = new InternetConnection(MyCustomerProflie.this);

        btnSubmitCustomerProflie = (Button) findViewById(R.id.btnCustomerProflieSubmit);
        btnSubmitCustomerProflie.setTypeface(font);
        btnSubmitCustomerProflie.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " + getResources().getString(R.string.submit)));


        btnClear = (Button) findViewById(R.id.btnCustomerProflieClear);
        btnClear.setTypeface(font);
        btnClear.setText(new SpannableStringBuilder(new String(new char[]{0xf021}) + " " + getResources().getString(R.string.clear)));


        btnCancle = (Button) findViewById(R.id.btnCustomerProflieCancle);
        btnCancle.setTypeface(font);
        btnCancle.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + "  " + getResources().getString(R.string.cancel)));
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MyCustomerProflie.this, DashboardActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });
        edtJoingDate = (EditText) findViewById(R.id.edtJoinDate);
        edtFirstname = (EditText) findViewById(R.id.edtFirstName);
        edtMiddleName = (EditText) findViewById(R.id.edtSurnameName);
        edtLastName = (EditText) findViewById(R.id.edtLastName);
        edtMobileNumber = (EditText) findViewById(R.id.edtMobileNo);
        edtAlertnetMobileNumber = (EditText) findViewById(R.id.edtAlertnetMobileNo);
        edtEmailId = (EditText) findViewById(R.id.edtEmailId);
        edtAlertnetMailId = (EditText) findViewById(R.id.edtAlertnetEmailId);
        edtAadharnumber = (EditText) findViewById(R.id.edtAdharNumber);
        edtPanNumber = (EditText) findViewById(R.id.edtPanNumber);
        edtElectionIdNumber = (EditText) findViewById(R.id.edtElectionNumber);

        edtDrivingLicence = (EditText) findViewById(R.id.edtDrivingNumber);
        edtAddress = (EditText) findViewById(R.id.edtAddress);
        edtPincode = (EditText) findViewById(R.id.edtPinCode);
        edtBirthdayDate = (EditText) findViewById(R.id.edtDateofBirth);
        edtMarraiageDate = (EditText) findViewById(R.id.edtmarraigeAniversy);

        btncaptureimage = (ImageView) findViewById(R.id.btncaptureimage);
        imgProfileImage = (ImageView) findViewById(R.id.imgProfileImage);


        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearall();
            }
        });

        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat dfDate = new SimpleDateFormat(myFormat, Locale.US);

        Date nowDate = new Date(System.currentTimeMillis());


        sJoiningDate = dfDate.format(nowDate);
        edtJoingDate.setText(sJoiningDate);

        btncaptureimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();

            }
        });

        btnSubmitCustomerProflie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sFirstNam = edtFirstname.getText().toString();
                sMobileNumber = edtMobileNumber.getText().toString();
                sJoiningDate = edtJoingDate.getText().toString();
                sMiddName = edtMiddleName.getText().toString().trim();
                sLastName = edtLastName.getText().toString().trim();
                boolean valid = true;
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String pancardpattern = "[a-z]{5}[0-9]{4}[a-z]{1}";
                String pincodePattern = "^[1-9][0-9]{5}$";
                if (sFirstNam.length() < 4) {
                    edtFirstname.setError(getResources().getString(R.string.enter4CharacterName));
                    valid = false;
                }
                if (sMobileNumber.length() != 10) {
                    edtMobileNumber.setError(getResources().getString(R.string.enter10DigitesNumber));
                    valid = false;
                }
                if (internetConnection.isConnectingToInternet() == false) {

                    AlertDialogBoxInfo.alertDialogShow(MyCustomerProflie.this, getResources().getString(R.string.internetCheck));
                    valid = false;
                }

                if (edtMiddleName.getText().toString().trim().length() >= 1 && edtMiddleName.getText().toString().trim().length() < 4) {
                    edtMiddleName.setError(getString(R.string.enter4CharacterName));
                    valid = false;
                }

                if (edtLastName.getText().toString().trim().length() >= 1 && edtLastName.getText().toString().trim().length() < 4) {
                    edtLastName.setError(getString(R.string.enter4CharacterName));
                    valid = false;
                }


                if (edtAlertnetMobileNumber.getText().toString().trim().length() >= 1 && edtAlertnetMobileNumber.getText().toString().trim().length() < 10) {
                    edtAlertnetMobileNumber.setError(getString(R.string.enter10DigitesNumber));
                    valid = false;
                }

                if (edtEmailId.getText().toString().trim().length() >= 1 && !edtEmailId.getText().toString().trim().matches(emailPattern)) {
                    edtEmailId.setError(getResources().getString(R.string.invalidEmail));
                    // edtEmailId.setTextColor(Color.parseColor("#000000"));
                    valid = false;

                }
                if (edtAlertnetMailId.getText().toString().trim().length() >= 1 && !edtAlertnetMailId.getText().toString().trim().matches(emailPattern)) {
                    edtAlertnetMailId.setError(getResources().getString(R.string.invalidEmail));
                    // edtAlertnetMailId.setTextColor(Color.parseColor("#000000"));
                    valid = false;

                }
                if (edtAadharnumber.getText().toString().trim().length() >= 1 && !validAadharNumber(edtAadharnumber.getText().toString())) {
                    edtAadharnumber.setError(getResources().getString(R.string.aadharinvalid));
                    // edtAadharnumber.setTextColor(Color.parseColor("#000000"));
                    valid = false;

                }
                if (edtPanNumber.getText().toString().trim().length() >= 1 && !edtPanNumber.getText().toString().trim().matches(pancardpattern)) {
                    edtPanNumber.setError(getResources().getString(R.string.invalidPancard));
                    // edtAlertnetMailId.setTextColor(Color.parseColor("#000000"));
                    valid = false;

                }

                if (edtPincode.getText().toString().trim().length() >= 1 && !edtPincode.getText().toString().trim().matches(pincodePattern)) {
                    edtPincode.setError(getResources().getString(R.string.invalidPincode));
                    // edtAlertnetMailId.setTextColor(Color.parseColor("#000000"));
                    valid = false;

                }

                if (valid) {

                    // X509TrustManagerSSL aa=      new X509TrustManagerSSL();
//
                    Connection connection = new Connection(MyCustomerProflie.this);
                    // connection.openDatabase();
                    Toast.makeText(MyCustomerProflie.this, "Thanks", Toast.LENGTH_SHORT).show();

                }
            }
        });
        edtMobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i != 10) {
                    // Toast.makeText(getApplicationContext(), "Please Enter 10 digits", Toast.LENGTH_SHORT).show();
                    edtMobileNumber.setTextColor(Color.parseColor("#000000"));
                    edtMobileNumber.setError(getResources().getString(R.string.enter10DigitesNumber));

                }


            }

            @Override
            public void afterTextChanged(Editable editable) {


                if (edtMobileNumber.length() <= 9) {


                } else {
                    edtMobileNumber.setTextColor(Color.parseColor("#468847"));
                    edtMobileNumber.setError(null);

                }
                String tmp = editable.toString().trim();
                if (tmp.length() == 1 && tmp.equals("0"))
                    editable.clear();
            }
        });

        edtFirstname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (i < 4) {
                    // Toast.makeText(getApplicationContext(), "Please Enter 10 digits", Toast.LENGTH_SHORT).show();
                    edtFirstname.setTextColor(Color.parseColor("#000000"));
                    edtFirstname.setError(getResources().getString(R.string.enter4CharacterName));

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edtFirstname.length() <= 3) {


                } else {
                    edtFirstname.setTextColor(Color.parseColor("#468847"));
                    edtFirstname.setError(null);

                }
            }
        });

        edtJoingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        MyCustomerProflie.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mcurrentDate.set(Calendar.YEAR, year);
                        mcurrentDate.set(Calendar.MONTH, monthOfYear);
                        mcurrentDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        String myFormat = "dd-MM-yyyy";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        edtJoingDate.setText(sdf.format(mcurrentDate.getTime()));

                    }
                }, mYear, mMonth, mDay);


                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();

            }
        });
        edtBirthdayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        MyCustomerProflie.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mcurrentDate.set(Calendar.YEAR, year);
                        mcurrentDate.set(Calendar.MONTH, monthOfYear);
                        mcurrentDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        String myFormat = "dd-MM-yyyy";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        edtBirthdayDate.setText(sdf.format(mcurrentDate.getTime()));

                    }
                }, mYear, mMonth, mDay);


                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();

            }
        });
        edtMarraiageDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        MyCustomerProflie.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mcurrentDate.set(Calendar.YEAR, year);
                        mcurrentDate.set(Calendar.MONTH, monthOfYear);
                        mcurrentDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        String myFormat = "dd-MM-yyyy";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        edtMarraiageDate.setText(sdf.format(mcurrentDate.getTime()));

                    }
                }, mYear, mMonth, mDay);


                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();

            }
        });

        edtFirstname.setFilters(new InputFilter[]{
                new InputFilter() {
                    public CharSequence filter(CharSequence src, int start,
                                               int end, Spanned dst, int dstart, int dend) {
                        if (src.equals("")) { // for backspace
                            return src;
                        }
                        if (src.toString().matches("[a-zA-Z ]+")) {
                            return src;
                        }
                        return "";
                    }
                }
        });
        edtMiddleName.setFilters(new InputFilter[]{
                new InputFilter() {
                    public CharSequence filter(CharSequence src, int start,
                                               int end, Spanned dst, int dstart, int dend) {
                        if (src.equals("")) { // for backspace
                            return src;
                        }
                        if (src.toString().matches("[a-zA-Z ]+")) {
                            return src;
                        }
                        return "";
                    }
                }
        });

        edtLastName.setFilters(new InputFilter[]{
                new InputFilter() {
                    public CharSequence filter(CharSequence src, int start,
                                               int end, Spanned dst, int dstart, int dend) {
                        if (src.equals("")) { // for backspace
                            return src;
                        }
                        if (src.toString().matches("[a-zA-Z ]+")) {
                            return src;
                        }
                        return "";
                    }
                }
        });

    }

    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};


        AlertDialog.Builder builder = new AlertDialog.Builder(MyCustomerProflie.this);

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    //pic = f;

                    startActivityForResult(intent, 1);


                } else if (options[item].equals("Choose from Gallery")) {

                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(intent, 2);


                } else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }


    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {
                //h=0;
                File f = new File(Environment.getExternalStorageDirectory().toString());

                for (File temp : f.listFiles()) {

                    if (temp.getName().equals("temp.jpg")) {

                        f = temp;
                        File photo = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                        //pic = photo;
                        break;

                    }

                }

                try {

                    Bitmap bitmap;

                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();


                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),

                            bitmapOptions);


                    imgProfileImage.setImageBitmap(bitmap);


                    String path = android.os.Environment

                            .getExternalStorageDirectory()

                            + File.separator

                            + "Phoenix" + File.separator + "default";
                    //p = path;

                    f.delete();

                    OutputStream outFile = null;

                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");

                    try {

                        outFile = new FileOutputStream(file);

                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        //pic=file;
                        outFile.flush();

                        outFile.close();


                    } catch (FileNotFoundException e) {

                        e.printStackTrace();

                    } catch (IOException e) {

                        e.printStackTrace();

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                } catch (Exception e) {

                    e.printStackTrace();

                }

            } else if (requestCode == 2) {


                Uri selectedImage = data.getData();
                // h=1;
                //imgui = selectedImage;
                String[] filePath = {MediaStore.Images.Media.DATA};

                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);

                c.moveToFirst();

                int columnIndex = c.getColumnIndex(filePath[0]);

                String picturePath = c.getString(columnIndex);

                c.close();

                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));


                Log.w("path of image from gallery......******************.........", picturePath + "");


                imgProfileImage.setImageBitmap(thumbnail);

            }
        }
    }

    private boolean validAadharNumber(String aadharNumber) {
        Pattern aadharPattern = Pattern.compile("\\d{12}");
        boolean isValidAadhar = aadharPattern.matcher(aadharNumber).matches();
        if (isValidAadhar) {
            isValidAadhar = VerhoeffAlgorithmAadharcardValidation.validateVerhoeff(aadharNumber);
        }
        return isValidAadhar;
    }

    private void clearall() {

        edtJoingDate.setText("");
        edtFirstname.setText("");
        edtMiddleName.setText("");
        edtLastName.setText("");
        edtMobileNumber.setText("");
        edtAlertnetMobileNumber.setText("");
        edtEmailId.setText("");
        edtAlertnetMailId.setText("");
        edtAadharnumber.setText("");
        edtPanNumber.setText("");
        edtElectionIdNumber.setText("");
        edtDrivingLicence.setText("");
        edtPincode.setText("");
        edtBirthdayDate.setText("");
        edtMarraiageDate.setText("");
        edtAddress.setText("");

        edtFirstname.setError(null);
        edtMobileNumber.setError(null);


    }

    public void onBackPressed() {

        Intent intent = new Intent(MyCustomerProflie.this, MyVakrangeeKendra.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(MyCustomerProflie.this, MyVakrangeeKendra.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

                break;
        }
        return true;
    }

}
