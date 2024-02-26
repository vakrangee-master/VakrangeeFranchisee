package in.vakrangee.core.utils;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.core.app.NotificationManagerCompat;
import in.vakrangee.core.documentmanager.DocumentManagerConstants;
import in.vakrangee.core.franchiseelogin.FranchiseeLoginChecksDto;
import in.vakrangee.core.phasechecks.PhaseInfoDto;

public class CommonUtils {

    private static final String TAG = "CommonUtils";
    public static final int FILE_IMAGE_TYPE = 1;
    public static final int DIALOG_SCREEN_WIDTH = 80;  // Value for width of DIalog in percentage
    public static final int DIALOG_SCREEN_HEIGHT = 80;  // Value for height of DIalog in percentage
    public static final String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z]+\\.+[a-zA-Z]+";
    public static final String pancardpattern = "[a-z]{5}[0-9]{4}[a-z]{1}";
    public static final String pincodePattern = "^[1-9][0-9]{5}$";
    public static final String ifscCodePattern = "^[A-Za-z]{4}0[A-Z0-9a-z]{6}$";
    public static final String EMPIDPattern = "[eE]{1}\\d{5}";
    public static final String LandLinePattern = "[0]{1}\\d{10}";
    public static final String VKIDPattern = "[a-zA-Z]{2}\\d{7}";
    private static boolean edittext_listener = true;

    /**
     * Get File object which refer to external storage at VakrangeePhoto Folder.
     *
     * @param type
     * @return
     */
    public static File getOutputMediaFile(int type) {

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "VakrangeePhoto");
        mediaStorageDir.mkdirs();

        /**Create the storage directory if it does not exist*/
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        /**Create a media file name*/
        String timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
        File mediaFile;
        if (type == 1) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".png");
        } else {
            return null;
        }

        return mediaFile;
    }

    //region To check photo is in landscape

    /**
     * Check photo is capture into Landscape mode or not.
     *
     * @param picUri
     * @param imageView
     * @return
     * @throws IOException
     */
    public static boolean isLandscapePhoto(Uri picUri, ImageView imageView) throws IOException {
        ExifInterface exif = new ExifInterface(picUri.getPath());
        int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int rotationInDegrees = exifToDegrees(rotation);

        int width = imageView.getDrawable().getIntrinsicWidth();
        int height = imageView.getDrawable().getIntrinsicHeight();
        if ((rotation == 6 && rotationInDegrees == 90) || height > width) {
            return false;
        }

        return true;
    }

    /**
     * Save Lat & Long into Image.
     *
     * @param picUri
     * @param latitude
     * @param longitude
     * @return
     */
    public static boolean saveLatLongIntoImage(Uri picUri, String latitude, String longitude) {
        try {
            if (TextUtils.isEmpty(latitude) || TextUtils.isEmpty(longitude))
                return false;

            ExifInterface exif = new ExifInterface(picUri.getPath());
            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, latitude);
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, longitude);
            exif.saveAttributes();
            Log.e("LATITUDE: ", latitude);
            Log.e("LONGITUDE: ", longitude);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isLandscapePhoto(Uri picUri) throws IOException {
        ExifInterface exif = new ExifInterface(picUri.getPath());
        int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int rotationInDegrees = exifToDegrees(rotation);

        if ((rotation == 6 && rotationInDegrees == 90)) {
            return false;
        }

        return true;
    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }
    //endregion

    //region Bitmap Operation - Compression and Conversion
    public static byte[] compressBitmap(Bitmap bitmap) {
        return compressBitmap(bitmap, 50);
    }

    public static byte[] compressBitmap(Bitmap bitmap, int quality) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
        String a = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        return Base64.decode(a, Base64.DEFAULT);
    }

    public static Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);

            /*BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;*/

            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
    //endregion

    public static int getScreenWidth(Activity activity) {

        Display display = activity.getWindowManager().getDefaultDisplay();

        // display size in pixels
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        return width;
    }

    //region Get Screen Density
    public static int getScreenDensityWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        float density = context.getResources().getDisplayMetrics().density;
        int width = (int) (metrics.widthPixels / density);
        return width;
    }

    public static int getScreenDensityHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        float density = context.getResources().getDisplayMetrics().density;
        int height = (int) (metrics.heightPixels / density);
        return height;
    }
    //endregion

    public static boolean isJSONValid(String data) {
        try {
            new JSONObject(data);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(data);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    //Method to set width of dialog.
    public static void setDialog(Context context, LinearLayout linearLayout) {
        if (context instanceof Activity && linearLayout != null) {
            int screenWidth = (getActivityScreenWidth((Activity) context) * DIALOG_SCREEN_WIDTH) / 100;
            int screenHeight = (getActivityScreenHeight((Activity) context) * DIALOG_SCREEN_HEIGHT) / 100;
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(screenWidth, screenHeight);
            linearLayout.setLayoutParams(lp);
        }
    }

    //Method to set width of dialog.
    public static void setDialog(Context context, LinearLayout linearLayout, int DIALOG_SCREEN_HEIGHT) {
        if (context instanceof Activity && linearLayout != null) {
            int screenWidth = (getActivityScreenWidth((Activity) context) * DIALOG_SCREEN_WIDTH) / 100;
            int screenHeight = (getActivityScreenHeight((Activity) context) * DIALOG_SCREEN_HEIGHT) / 100;
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(screenWidth, screenHeight);
            linearLayout.setLayoutParams(lp);
        }
    }

    //Method to set width of dialog.
    public static void setDialogWidth(Context context, LinearLayout linearLayout) {
        if (context instanceof Activity && linearLayout != null) {
            int screenWidth = (getActivityScreenWidth((Activity) context) * DIALOG_SCREEN_WIDTH) / 100;
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(screenWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
            linearLayout.setLayoutParams(lp);
        }
    }

    //Method to get width of screen size.
    public static int getActivityScreenWidth(Activity activity) {

        Display display = activity.getWindowManager().getDefaultDisplay();

        // display size in pixels
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        return width;
    }

    public static int getActivityScreenHeight(Activity activity) {

        Display display = activity.getWindowManager().getDefaultDisplay();

        // display size in pixels
        Point size = new Point();
        display.getSize(size);
        int height = size.y;

        return height;
    }

    public static String getFormattedTime() {
        String formattedTime = null;

        try {
            String format = "HH:mm:ss";
            formattedTime = new SimpleDateFormat(format).format(new Date());

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getFormattedTime : Error: " + e.toString());
        }
        return formattedTime;
    }

    //Method to get Day from Selected Date
    public static String getFormattedDate(String fromFormat, String toFormat, String selectedDate) {
        String formatedDate = null;

        try {
            String formatedTime = getFormattedTime();
            if (!TextUtils.isEmpty(formatedTime))
                selectedDate = selectedDate + " " + formatedTime;

            Date date = new SimpleDateFormat(fromFormat).parse(selectedDate);
            formatedDate = new SimpleDateFormat(toFormat).format(date);
            Log.d(TAG, "Testing: Formated Date: " + formatedDate);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getFormattedDate : Error: " + e.toString());
        }
        return formatedDate;
    }

    //Method to format date
    public static String getFormattedGPSDateStamp(String fromFormat, String toFormat, String selectedDate) {
        String formatedDate = null;
        try {
            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);

            if (TextUtils.isEmpty(selectedDate))
                selectedDate = dateFormatter.format(new Date());

            Date date = new SimpleDateFormat(fromFormat).parse(selectedDate);
            formatedDate = new SimpleDateFormat(toFormat).format(date);
            Log.d(TAG, "Testing: getFormattedGPSDateStamp Date: " + formatedDate);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getFormattedGPSDateStamp : Error: " + e.toString());
        }
        return formatedDate;
    }

    /*

    public static void addShortcut(Context context) {

        SharedPreferences appSettings = context.getSharedPreferences("APP_NAME", MODE_PRIVATE);
        if (!appSettings.getBoolean("shortcut", false)) {

            Intent shortcutIntent = new Intent(context.getApplicationContext(), AdhocLoginActivity.class);
            shortcutIntent.setAction(Intent.ACTION_MAIN);

            Intent addIntent = new Intent();
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(R.string.app_name));
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(context.getApplicationContext(), R.mipmap.ic_launcher));

        */
/*addIntent.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
        context.getApplicationContext().sendBroadcast(addIntent);*//*


            addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            context.getApplicationContext().sendBroadcast(addIntent);

            SharedPreferences.Editor prefEditor = appSettings.edit();
            prefEditor.putBoolean("shortcut", true);
            prefEditor.commit();
        }
    }
*/

    public static String toTitleCase(String str) {

        if (str == null) {
            return null;
        }

        boolean space = true;
        StringBuilder builder = new StringBuilder(str);
        final int len = builder.length();

        for (int i = 0; i < len; ++i) {
            char c = builder.charAt(i);
            if (space) {
                if (!Character.isWhitespace(c)) {
                    // Convert to title case and switch out of whitespace mode.
                    builder.setCharAt(i, Character.toTitleCase(c));
                    space = false;
                }
            } else if (Character.isWhitespace(c)) {
                space = true;
            } else {
                builder.setCharAt(i, Character.toLowerCase(c));
            }
        }
        return builder.toString();
    }

    //region GSTIN Number Validation
    ///\d{2}[A-Z]{5}\d{4}[A-Z]{1}\d[Z]{1}[A-Z\d]{1}/i  - java script
    public static boolean GSTINisValid(String str) {
        boolean isValid = false;
        String expression = "[0-9]{2}[a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}[1-9A-Za-z]{1}[a-zA-Z]{1}[0-9a-zA-Z]{1}";
        CharSequence inputStr = str;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
    //endregion

    //region IFSC Code Validation
    //^[A-Za-z]{4}0[A-Z0-9a-z]{6}$ - java script
    public static boolean IsIFSCCodeValid(String str) {
        boolean isValid = false;
        String expression = ifscCodePattern;
        CharSequence inputStr = str;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
    //endregion

    //region Pan card Validation
    //^[A-Za-z]{4}0[A-Z0-9a-z]{6}$ -
    public static boolean PANCardValid(String str) {
        boolean isValid = false;
        String expression = "[a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}";
        CharSequence inputStr = str;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    //endregion
    //region Input Filter -"~#^|$%&*!'
    //private static final String blockCharacterSet = "~#^|$%&*!";
    public static void applyInputFilter(EditText editText, final String blockCharacterSet) {
        editText.setFilters(new InputFilter[]{new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                if (source != null && blockCharacterSet.contains(("" + source))) {
                    return "";
                }
                return null;
            }
        }});
    }
    //endregion

    //region Get Image Salt Data
    private static int MIN_IMAGE_LENGTH = 32;

    public static String getImageSalt(String imageData) {
        if (TextUtils.isEmpty(imageData))
            return null;

        int dataLength = imageData.length();
        if (dataLength > MIN_IMAGE_LENGTH) {
            return imageData.substring(dataLength - MIN_IMAGE_LENGTH); //imageData.substring(0,MIN_IMAGE_LENGTH - 1);
        } else {
            return imageData;
        }
    }
    //endregion

    //region TEXT - All textCapCharacters
    public static void AllCapCharCaptial(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable et) {
                String s = et.toString();
                if (!s.equals(s.toUpperCase())) {
                    s = s.toUpperCase();
                    editText.setText(s);
                    editText.setSelection(editText.getText().length());
                }
            }
        });
    } //endregion

    public static String convertBitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    public static String encodeFileToBase64Binary(File file) throws IOException {

        //File file = new File(fileName);
        byte[] bytes = loadFile(file);
        byte[] encoded = Base64.encode(bytes, Base64.DEFAULT);
        String encodedString = new String(encoded);

        return encodedString;
    }

    private static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }

    //region Get-Set Update LatLong Values[Status, Interval]
    //Method to set Status to start UpdateUserLatLong service into the preferences
    public static void setUpdateLatLngStatus(Context context, boolean status) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("IS_UPDATE_LAT_LONG_TO_START", status);
        editor.commit();
    }

    //Method to get Status to start UpdateUserLatLong service from Preference
    public static boolean IsUpdateLatLngToBeStarted(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("IS_UPDATE_LAT_LONG_TO_START", false);
    }

    //Method to set Interval to start UpdateUserLatLong service into the preferences
    public static void setUpdateLatLngInterval(Context context, int interval) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("UPDATE_LAT_LONG_INTERVAL", interval);
        editor.commit();
    }

    //Method to get Interval to start UpdateUserLatLong service from Preference
    public static int getUpdateLatLngInterval(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("UPDATE_LAT_LONG_INTERVAL", 0);
    }

    //endregion
    //private static final String blockCharacterSet = "~#^|$%&*!";
    public static void InputFiletrWithMaxLength(EditText editText, final String blockCharacterSet, int maxlength) {
        editText.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                if (charSequence != null && blockCharacterSet.contains(("" + charSequence))) {
                    return "";
                }
                return null;
            }
        }, new InputFilter.LengthFilter(maxlength) {
        }});

    }

    //region Valid Aadharcard number
    public static boolean validAadharNumber(String aadharNumber) {
        Pattern aadharPattern = Pattern.compile("\\d{12}");
        boolean isValidAadhar = aadharPattern.matcher(aadharNumber).matches();
        if (isValidAadhar) {
            isValidAadhar = VerhoeffAlgorithmAadharcardValidation.validateVerhoeff(aadharNumber);
        }
        return isValidAadhar;
    }
    //endregion

    //region Editext Listener
    public static void EditextListener(final EditText password, final EditText confirmpassword, final int length, final String message, final String controlname) {

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int len = password.getText().toString().trim().length();
                if (len <= 0)
                    return;

              /*  if (controlname.equals("Aadhaarcard")) {
                    if (!CommonUtils.validAadharNumber(String.valueOf(charSequence))) {
                        password.setTextColor(Color.parseColor("#FF0000"));
                        password.setError(message);
                    } else {
                        password.setTextColor(Color.parseColor("#468847"));
                        password.setError(null);
                    }
                } else if (controlname.equals("PANCard")) {
                    if (!CommonUtils.PANCardValid(String.valueOf(charSequence))) {
                        password.setTextColor(Color.parseColor("#FF0000"));
                        password.setError(message);
                    } else {
                        password.setTextColor(Color.parseColor("#468847"));
                        password.setError(null);
                    }
                } else if (controlname.equals("IFSCCode")) {
                    if (!CommonUtils.IsIFSCCodeValid(String.valueOf(charSequence))) {
                        password.setTextColor(Color.parseColor("#FF0000"));
                        password.setError(message);
                    } else {
                        password.setTextColor(Color.parseColor("#468847"));
                        password.setError(null);
                    }
                } else if (controlname.equals("AccountNumber")) {
                    if (i != length) {
                        password.setTextColor(Color.parseColor("#000000"));
                        password.setError(message);
                    } else {
                        password.setTextColor(Color.parseColor("#468847"));
                        password.setError(null);
                    }
                } else {
                    if (i != length) {
                        password.setTextColor(Color.parseColor("#000000"));
                        password.setError(message);
                    } else {
                        password.setTextColor(Color.parseColor("#468847"));
                        password.setError(null);
                    }
                }*/



               /* if (!CommonUtils.validAadharNumber(password.getText().toString())) {
                    password.setTextColor(Color.parseColor("#FF0000"));
                    password.setError(message);
                } else {
                    password.setTextColor(Color.parseColor("#468847"));
                    password.setError(null);
                }*/
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int len = password.getText().toString().trim().length();
                if (len <= 0)
                    return;

                if (password.getText().toString().contains(" ")) {
                    password.setText(password.getText().toString().replaceAll(" ", ""));
                    password.setSelection(password.getText().length());
                }
                if (controlname.equals("Aadhaarcard")) {
                    if (!CommonUtils.validAadharNumber(String.valueOf(editable))) {
                        edittext_listener = true;
                    } else {
                        edittext_listener = false;
                    }
                } else if (controlname.equals("PANCard")) {
                    if (!CommonUtils.PANCardValid(String.valueOf(editable.toString()))) {
                        edittext_listener = true;
                    } else {
                        edittext_listener = false;
                    }
                } else if (controlname.equals("IFSCCode")) {
                    String s = password.getText().toString().trim();
                    setWordsCaps(s, password);

                    if (!CommonUtils.IsIFSCCodeValid(String.valueOf(editable))) {
                        edittext_listener = true;
                    } else {
                        edittext_listener = false;
                    }
                } else if (controlname.equals("AccountNumber")) {
                    if (password.length() < 10) {
                        edittext_listener = true;
                    } else {
                        edittext_listener = false;
                    }
                } else if (controlname.equals("AddressProof")) {
                    if (password.length() < 1) {
                        edittext_listener = true;
                    } else {
                        edittext_listener = false;
                    }
                } else {
                    if (password.length() < length) {
                        edittext_listener = true;
                    } else {
                        edittext_listener = false;
                    }
                }
                //set color base on
                if (edittext_listener) {
                    password.setTextColor(Color.parseColor("#FF0000"));
                    password.setError(message);
                } else {
                    password.setTextColor(Color.parseColor("#468847"));
                    password.setError(null);

                    String strPass1 = password.getText().toString();
                    String strPass2 = confirmpassword.getText().toString();

                    if (password.length() < 1) {
                    } else if (!strPass1.equalsIgnoreCase(strPass2)) {
                        password.setError("Not Match");
                        password.setTextColor(Color.parseColor("#000000"));
                    } else {
                        confirmpassword.setTextColor(Color.parseColor("#468847"));
                        confirmpassword.setError(null);
                        password.setTextColor(Color.parseColor("#468847"));
                        password.setError(null);
                    }
                }
            }
        });

        //---------------------------confirmpassword-----------------
        confirmpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int len = confirmpassword.getText().toString().trim().length();
                if (len <= 0)
                    return;

                if (i != length) {
                    confirmpassword.setTextColor(Color.parseColor("#000000"));
                    confirmpassword.setError(message);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (controlname.equals("IFSCCode")) {
                    String s = confirmpassword.getText().toString().trim();
                    setWordsCaps(s, confirmpassword);
                }

                if (confirmpassword.getText().toString().contains(" ")) {
                    confirmpassword.setText(confirmpassword.getText().toString().replaceAll(" ", ""));
                    confirmpassword.setSelection(confirmpassword.getText().length());
                }

                String strPass1 = password.getText().toString();
                String strPass2 = confirmpassword.getText().toString();

                if (confirmpassword.length() < 1) {
                } else if (!strPass1.equalsIgnoreCase(strPass2)) {
                    confirmpassword.setError("Not Match");
                    confirmpassword.setTextColor(Color.parseColor("#000000"));
                } else {
                    confirmpassword.setTextColor(Color.parseColor("#468847"));
                    confirmpassword.setError(null);
                    password.setTextColor(Color.parseColor("#468847"));
                    password.setError(null);
                }
            }
        });
    }

//endregion

    public static void setWordsCaps(String s, EditText editText) {

        if (!s.equals(s.toUpperCase())) {
            s = s.toUpperCase();
            editText.setText(s);
            editText.setSelection(editText.getText().length());
        }
    }

    public static int getFileSizeInMB(File file) {
        long fileSizeInBytes = file.length();
        //Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
        long fileSizeInKB = fileSizeInBytes / 1024;
        Log.d(TAG, "Testing: BROWSE_FOLDER_REQUEST Size KB: " + fileSizeInKB);
        //Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        long fileSizeInMB = fileSizeInKB / 1024;

        return (int) fileSizeInMB;
    }

    public static boolean isValidVKID(String VKID) {
        if (Pattern.matches(VKIDPattern, VKID))
            return true;

        return false;
    }

    public static boolean isValidEMPID(String EMPID) {
        if (Pattern.matches(EMPIDPattern, EMPID))
            return true;

        return false;
    }

    public static boolean isValidLandLine(String LandLineNo) {
        if (Pattern.matches(LandLinePattern, LandLineNo))
            return true;

        return false;
    }

    public static boolean isValidMobile(String phone) {
        boolean check = false;
        if (!Pattern.matches("[a-zA-Z.@]+", phone)) {
            //if (phone.length() < 6 || phone.length() > 13) {
            if (phone.length() != 10) {
                check = false;
            } else {
                check = true;
            }
        } else {
            check = false;
        }
        return check;
    }

    public static boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static FranchiseeLoginChecksDto getFranchiseeLoginDataFromPreferences(Context context) {
        FranchiseeLoginChecksDto loginChecksDto = null;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonData = preferences.getString("FRANCHISEE_LOGIN_DATA", null);
        if (!TextUtils.isEmpty(jsonData)) {
            Gson gson = new Gson();
            loginChecksDto = gson.fromJson(jsonData, FranchiseeLoginChecksDto.class);
        }
        return loginChecksDto;
    }

    public static void setFranchiseeLoginDataIntoPreferences(Context context, FranchiseeLoginChecksDto loginChecksDto) {

        if (loginChecksDto == null)
            return;

        Gson gson = new Gson();
        String data = gson.toJson(loginChecksDto, FranchiseeLoginChecksDto.class);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("FRANCHISEE_LOGIN_DATA", data);
        editor.commit();

    }

    public static void clearFranchiseeLoginDataFromPreferences(Context context) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("FRANCHISEE_LOGIN_DATA");
        editor.commit();

        //Clear Phase Info
        clearFranchiseePhaseInfoFromPreferences(context);

    }

    /**
     * Method to get Current Date
     *
     * @return
     */
    public static String getCurrentDate() {

        String currentDate = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();

            currentDate = dateFormat.format(cal.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentDate;
    }

    public static String getEnquiryId(Context context) {

        FranchiseeLoginChecksDto loginChecksDto = CommonUtils.getFranchiseeLoginDataFromPreferences(context);
        if (loginChecksDto == null)
            return null;

        //Get Enquiry Id
        String enquiryID = loginChecksDto.getNextGenEnquiryId();
        if (TextUtils.isEmpty(enquiryID) || enquiryID.equalsIgnoreCase("0"))
            return null;

        return enquiryID;
    }

    public static PhaseInfoDto getFranchiseePhaseInfoFromPreferences(Context context) {
        PhaseInfoDto phaseInfoDto = null;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonData = preferences.getString("FRANCHISEE_PHASE_INFO", null);
        if (!TextUtils.isEmpty(jsonData)) {
            Gson gson = new Gson();
            phaseInfoDto = gson.fromJson(jsonData, PhaseInfoDto.class);
        }
        return phaseInfoDto;
    }

    public static void setFranchiseePhaseInfoIntoPreferences(Context context, PhaseInfoDto phaseInfoDto) {

        if (phaseInfoDto == null)
            return;

        Gson gson = new Gson();
        String data = gson.toJson(phaseInfoDto, PhaseInfoDto.class);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("FRANCHISEE_PHASE_INFO", data);
        editor.commit();

    }

    public static void clearFranchiseePhaseInfoFromPreferences(Context context) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("FRANCHISEE_PHASE_INFO");
        editor.commit();

    }

    public static String getPhaseCode(Context context) {

        PhaseInfoDto phaseInfoDto = CommonUtils.getFranchiseePhaseInfoFromPreferences(context);
        if (phaseInfoDto == null)
            return null;

        //Get Phase Code
        String phaseCode = phaseInfoDto.getNextGenPhaseCode();
        if (TextUtils.isEmpty(phaseCode) || phaseCode.equalsIgnoreCase("0"))
            return null;

        return phaseCode;
    }

    public static void checkNotification(final Context context) {
        Log.e(TAG, "<<<<< Check Notification is Enable or Disable. >>>>>");
        boolean a = NotificationManagerCompat.from(context).areNotificationsEnabled();
        if (!a) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Enable Notification");
            builder.setMessage("Your Notification Setting is set to \"Off\". Please Enable Notification to use this app.");
            builder.setCancelable(false);
            // add a button
            builder.setPositiveButton("Notification Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                    intent.setData(uri);
                    context.startActivity(intent);
                   /* Intent myIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    context.startActivity(myIntent);*/
                    dialogInterface.cancel();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    checkNotification(context);
                }
            });
            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    //region set font style in textview
    public static void setTextStyleFont(TextView textView, Typeface fontname, String textname) {
        textView.setText(textname);
        textView.setTypeface(fontname, Typeface.ITALIC);
    }
    //endregion

    //region Is FA New or Existing Check Done
    public static String getFANewExistingCheckDoneStatusFromPreferences(Context context) {
        String status = null;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        status = preferences.getString("FA_NEW_OR_EXISTING_CHECK_DONE ", null);

        return status;
    }

    public static void setFANewExistingCheckDoneStatusIntoPreferences(Context context, String status) {

        if (status == null)
            return;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("FA_NEW_OR_EXISTING_CHECK_DONE", status);
        editor.commit();

    }

    public static void clearFANewExistingCheckDoneStatusFromPreferences(Context context) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("FA_NEW_OR_EXISTING_CHECK_DONE");
        editor.commit();

    }
    //endregion

    //region Is FA New or Existing Activity Done
    public static String getFANewExistingActivityDoneStatusFromPreferences(Context context) {
        String status = null;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        status = preferences.getString("FA_NEW_OR_EXISTING_ACTIVITY_DONE ", null);

        return status;
    }

    public static void setFANewExistingCheckctivityStatusIntoPreferences(Context context, String status) {

        if (status == null)
            return;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("FA_NEW_OR_EXISTING_ACTIVITY_DONE", status);
        editor.commit();

    }

    public static void clearFANewExistingCheckctivityStatusFromPreferences(Context context) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("FA_NEW_OR_EXISTING_ACTIVITY_DONE");
        editor.commit();

    }
    //endregion


    public static Bitmap decodeBitmapUri(Context ctx, Uri uri) throws FileNotFoundException {
        int targetW = 600;
        int targetH = 600;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(uri), null, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeStream(ctx.getContentResolver()
                .openInputStream(uri), null, bmOptions);
    }


    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();

            return bmRotated;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    //region cash loading count
    public static void totalPhyscialNoteCount(TextView textTotalNoteCount, EditText edittext1, EditText edittext2, EditText edittext3,
                                              EditText edittext4, TextView textTotalAmount) {
        Long number1, number2, number3, number4, totalNote, totalAmount;

        if (edittext1.getText().toString() != "" && edittext1.getText().length() > 0) {
            number1 = Long.parseLong(edittext1.getText().toString());
        } else {
            number1 = Long.valueOf(0);
        }
        if (edittext2.getText().toString() != "" && edittext2.getText().length() > 0) {
            number2 = Long.parseLong(edittext2.getText().toString());
        } else {
            number2 = Long.valueOf(0);
        }

        if (edittext3.getText().toString() != "" && edittext3.getText().length() > 0) {
            number3 = Long.parseLong(edittext3.getText().toString());
        } else {
            number3 = Long.valueOf(0);
        }

        if (edittext4.getText().toString() != "" && edittext4.getText().length() > 0) {
            number4 = Long.parseLong(edittext4.getText().toString());
        } else {
            number4 = Long.valueOf(0);
        }

        totalNote = (number1 + number2 + number3 + number4);

        String totalNoteToFormate = new DecimalFormat("##,##,##0").format(totalNote);
        textTotalNoteCount.setText(totalNoteToFormate);

        // textTotalNoteCount.setText(String.valueOf(totalNote));

        totalAmount = ((number1 * 2000 + number2 * 500 + number3 * 100 + number4 * 100));
        String totalAmountToFormate = new DecimalFormat("##,##,##0").format(totalAmount);
        textTotalAmount.setText(totalAmountToFormate);

        // textTotalAmount.setText(String.valueOf(totalAmount));
    }

    //region cash loading count
    public static void totalPhyscialNoteCountPurge(TextView textTotalNoteCount, EditText edittext1, EditText edittext2, EditText edittext3,
                                                   TextView textTotalAmount) {
        Long number1, number2, number3, totalNote, totalAmount;

        if (edittext1.getText().toString() != "" && edittext1.getText().length() > 0) {
            number1 = Long.parseLong(edittext1.getText().toString());
        } else {
            number1 = Long.valueOf(0);
        }
        if (edittext2.getText().toString() != "" && edittext2.getText().length() > 0) {
            number2 = Long.parseLong(edittext2.getText().toString());
        } else {
            number2 = Long.valueOf(0);
        }

        if (edittext3.getText().toString() != "" && edittext3.getText().length() > 0) {
            number3 = Long.parseLong(edittext3.getText().toString());
        } else {
            number3 = Long.valueOf(0);
        }


        totalNote = (number1 + number2 + number3);

        String totalNoteToFormate = new DecimalFormat("##,##,##0").format(totalNote);
        textTotalNoteCount.setText(totalNoteToFormate);

        //textTotalNoteCount.setText(String.valueOf(totalNote));

        totalAmount = ((number1 * 2000 + number2 * 500 + number3 * 100));
        String totalAmountToFormate = new DecimalFormat("##,##,##0").format(totalAmount);
        textTotalAmount.setText(totalAmountToFormate);

        //  textTotalAmount.setText(String.valueOf(totalAmount) + " ₹");
    }

    //region max and min value -- cash loading
    public static class InputFilterMinMax implements InputFilter {
        private int min, max;

        public InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public InputFilterMinMax(String min, String max) {
            this.min = Integer.parseInt(min);
            this.max = Integer.parseInt(max);
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                int input = Integer.parseInt(dest.toString() + source.toString());
                if (isInRange(min, max, input))

                    return null;
            } catch (NumberFormatException nfe) {
            }
            return "";
        }

        private boolean isInRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }

    public static void animateTextView(int initialValue, int finalValue, final TextView textview) {

        ValueAnimator valueAnimator = ValueAnimator.ofInt(initialValue, finalValue);
        valueAnimator.setDuration(1500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                textview.setText(valueAnimator.getAnimatedValue().toString());
            }
        });
        valueAnimator.start();
    }

    public static void spinner_focusablemode(Spinner spinner) {
        spinner.setFocusable(true);
        spinner.setFocusableInTouchMode(true);
    }

    public static String inchesTofeetAndInches(String inches) {
        int feet = Integer.valueOf(inches) / 12;
        int leftover = Integer.valueOf(inches) % 12;
        return feet + "|" + leftover;
    }

    public static int getIndexText(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }

        return 0;
    }

    public static int feetAndInchesToInches(int feet, int inch) {
        int inches = (feet * 12) + inch;
        return inches;
    }

    public static String differenceVal(String receiptAmount, String totalamount) {
        int number1;
        int number2;
        //comma remove for string
       /* String commaRemove = totalamount.getText().toString().replace(",", "");
        totalamount.setText(commaRemove);*/
        String commaRemove = totalamount.replace(",", "");
        totalamount = commaRemove;


        if (receiptAmount != "" && receiptAmount.length() > 0) {
            number1 = Integer.parseInt(receiptAmount);
        } else {
            number1 = Integer.valueOf(0);
        }

        /*if (totalamount.getText().toString() != "" && totalamount.getText().length() > 0) {
            number2 = Integer.parseInt(totalamount.getText().toString());
        } else {
            number2 = Integer.valueOf(0);
        }*/

        if (totalamount != "" && totalamount.length() > 0) {
            number2 = Integer.parseInt(totalamount);
        } else {
            number2 = Integer.valueOf(0);
        }

        return Integer.toString((int) (number1 - number2));
    }

    //region - tab layout set marign between 2 tab.
    public static void wrapTabIndicatorToTitle(TabLayout tabLayout, int externalMargin, int internalMargin) {
        View tabStrip = tabLayout.getChildAt(0);
        if (tabStrip instanceof ViewGroup) {
            ViewGroup tabStripGroup = (ViewGroup) tabStrip;
            int childCount = ((ViewGroup) tabStrip).getChildCount();
            for (int i = 0; i < childCount; i++) {
                View tabView = tabStripGroup.getChildAt(i);
                //set minimum width to 0 for instead for small texts, indicator is not wrapped as expected
                tabView.setMinimumWidth(0);
                // set padding to 0 for wrapping indicator as title
                tabView.setPadding(0, tabView.getPaddingTop(), 0, tabView.getPaddingBottom());
                // setting custom margin between tabs
                if (tabView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) tabView.getLayoutParams();
                    if (i == 0) {
                        // left
                        settingMargin(layoutParams, externalMargin, internalMargin);
                    } else if (i == childCount - 1) {
                        // right
                        settingMargin(layoutParams, internalMargin, externalMargin);
                    } else {
                        // internal
                        settingMargin(layoutParams, internalMargin, internalMargin);
                    }
                }
            }

            tabLayout.requestLayout();
        }
    }

    private static void settingMargin(ViewGroup.MarginLayoutParams layoutParams, int start, int end) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            layoutParams.setMarginStart(start);
            layoutParams.setMarginEnd(end);
            layoutParams.leftMargin = start;
            layoutParams.rightMargin = end;
        } else {
            layoutParams.leftMargin = start;
            layoutParams.rightMargin = end;
        }
    }

    //region cash loading count
    public static void totalPhyscialNoteCountUsingDialog(TextView textTotalNoteCount, TextView edittext1, TextView edittext2, TextView edittext3,
                                                         TextView edittext4, TextView textTotalAmount) {
        Long number1, number2, number3, number4, totalNote, totalAmount;

        if (edittext1.getText().toString() != "" && edittext1.getText().length() > 0) {
            number1 = Long.parseLong(edittext1.getText().toString());
        } else {
            number1 = Long.valueOf(0);
        }
        if (edittext2.getText().toString() != "" && edittext2.getText().length() > 0) {
            number2 = Long.parseLong(edittext2.getText().toString());
        } else {
            number2 = Long.valueOf(0);
        }

        if (edittext3.getText().toString() != "" && edittext3.getText().length() > 0) {
            number3 = Long.parseLong(edittext3.getText().toString());
        } else {
            number3 = Long.valueOf(0);
        }

        if (edittext4.getText().toString() != "" && edittext4.getText().length() > 0) {
            number4 = Long.parseLong(edittext4.getText().toString());
        } else {
            number4 = Long.valueOf(0);
        }

        totalNote = (number1 + number2 + number3 + number4);

        String totalNoteToFormate = new DecimalFormat("##,##,##0").format(totalNote);
        textTotalNoteCount.setText(totalNoteToFormate);

        // textTotalNoteCount.setText(String.valueOf(totalNote));

        totalAmount = ((number1 * 2000 + number2 * 500 + number3 * 100 + number4 * 100));
        String totalAmountToFormate = new DecimalFormat("##,##,##0").format(totalAmount);
        textTotalAmount.setText(totalAmountToFormate);

        // textTotalAmount.setText(String.valueOf(totalAmount));
    }

    //region cash loading count
    public static void totalPhyscialNoteCountPurgeUsingDialog(TextView textTotalNoteCount, TextView edittext1, TextView edittext2, TextView edittext3,
                                                              TextView textTotalAmount) {
        Long number1, number2, number3, totalNote, totalAmount;

        if (edittext1.getText().toString() != "" && edittext1.getText().length() > 0) {
            number1 = Long.parseLong(edittext1.getText().toString());
        } else {
            number1 = Long.valueOf(0);
        }
        if (edittext2.getText().toString() != "" && edittext2.getText().length() > 0) {
            number2 = Long.parseLong(edittext2.getText().toString());
        } else {
            number2 = Long.valueOf(0);
        }

        if (edittext3.getText().toString() != "" && edittext3.getText().length() > 0) {
            number3 = Long.parseLong(edittext3.getText().toString());
        } else {
            number3 = Long.valueOf(0);
        }


        totalNote = (number1 + number2 + number3);

        String totalNoteToFormate = new DecimalFormat("##,##,##0").format(totalNote);
        textTotalNoteCount.setText(totalNoteToFormate);

        //textTotalNoteCount.setText(String.valueOf(totalNote));

        totalAmount = ((number1 * 2000 + number2 * 500 + number3 * 100));
        String totalAmountToFormate = new DecimalFormat("##,##,##0").format(totalAmount);
        textTotalAmount.setText(totalAmountToFormate);

        //  textTotalAmount.setText(String.valueOf(totalAmount) + " ₹");
    }

    public static void setRupeeToSpeator(TextView textView, String amount) {
        Long totalAmount = Long.parseLong(amount);
        String totalAmountToFormate = new DecimalFormat("##,##,##0").format(totalAmount);
        textView.setText(totalAmountToFormate + " ₹");
    }

    public static File createFile(String enquiryId, String fileName) throws IOException {

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                DocumentManagerConstants.DIR_NAME_AGREEMENT_UPLOAD + File.separator + enquiryId);
        mediaStorageDir.mkdirs();

        /**Create the storage directory if it does not exist*/
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        /**Create a media file name*/
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + fileName + ".jpg");

        return mediaFile;
    }

    //Move File
    public static String moveFile(Uri sourceuri, String enquiryId, String fileName) {

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            String sourceFilename = sourceuri.getPath();
            File DestFile = createFile(enquiryId, fileName);
            String destinationFilename = DestFile.getPath();

            bis = new BufferedInputStream(new FileInputStream(sourceFilename));
            bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while (bis.read(buf) != -1);

            return destinationFilename;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //Delete Directory
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

    //region ARN Validation
    public static boolean isValidARN(String str) {
        boolean isValid = false;
        String expression = "[a-zA-Z]{2}[0-9]{13}";
        CharSequence inputStr = str;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    //endregion

}
