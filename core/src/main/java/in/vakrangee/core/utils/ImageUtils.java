package in.vakrangee.core.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ImageUtils {

    private static final String TAG = "ImageUtils";
    private static final int IMAGE_MAX_SIZE = 500;

    public static ByteArrayOutputStream stampWithTimeAndName(Bitmap src, String name) {

        //Bitmap src = BitmapFactory.decodeResource(getResources(), R.drawable.cuty); // the original file is cuty.jpg i added in resources
        int w = src.getWidth();
        int h = src.getHeight();
        Log.e("ImageUtils", "Width : " + w + " | Height : " + h);
        Bitmap dest = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String dateTime = sdf.format(Calendar.getInstance().getTime()); // reading local time in the system

        Canvas cs = new Canvas(dest);
        Paint tPaint = new Paint();
        tPaint.setTextSize(60);
        tPaint.setColor(Color.WHITE);
        tPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        tPaint.setStyle(Paint.Style.FILL);
        cs.drawBitmap(src, 0f, 0f, null);

        float width = tPaint.measureText(dateTime);//bounds.width();
        float height = tPaint.measureText("yY");

        // Draw Name On Image - Bottom Left Side
        //Log.e("ImageUtils", "Paint Width : "+width+" | Height : "+height);
        if (!TextUtils.isEmpty(name)) {
            //cs.drawText(name, 20f, (h - (height+15f)), tPaint);   // Bottom Left
            cs.drawText(name, 20f, height + 15f, tPaint);       // Top Left
        }

        // Draw DateTime On Image - Bottom Right Side
        //Log.e("ImageUtils", "Cal Width : "+(w - (width + 20f))+" | Cal Height : "+(h - (height+15f)));
        cs.drawText(dateTime, (w - (width + 20f)), (h - (height + 15f)), tPaint);

        ByteArrayOutputStream byteArrayOutputStream = null;

        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            //dest.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(new File("/sdcard/timeStampedImage.jpg")));
            dest.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);    // 100% - quality
        } catch (Exception e) {
            e.printStackTrace();
        }

        return byteArrayOutputStream;
    }

    public static Bitmap stampWithTimeAndNameInBitmap(Bitmap src, String name) {

        //Bitmap src = BitmapFactory.decodeResource(getResources(), R.drawable.cuty); // the original file is cuty.jpg i added in resources
        int w = src.getWidth();
        int h = src.getHeight();
        Log.e("ImageUtils", "Width : " + w + " | Height : " + h);
        Bitmap dest = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        //SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        String dateTime = sdf.format(Calendar.getInstance().getTime()); // reading local time in the system

        Canvas cs = new Canvas(dest);
        Paint tPaint = new Paint();
        tPaint.setTextSize(40);
        tPaint.setColor(Color.WHITE);
        tPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        tPaint.setStyle(Paint.Style.FILL);
        cs.drawBitmap(src, 0f, 0f, null);

        float width = tPaint.measureText(dateTime);//bounds.width();
        float height = tPaint.measureText("yY");

        // Draw Name On Image - Bottom Left Side
        //Log.e("ImageUtils", "Paint Width : "+width+" | Height : "+height);
        if (!TextUtils.isEmpty(name)) {
            //cs.drawText(name, 20f, (h - (height+15f)), tPaint);   // Bottom Left
            cs.drawText(name, 20f, height + 15f, tPaint);       // Top Left
        }

        // Draw DateTime On Image - Bottom Right Side
        //Log.e("ImageUtils", "Cal Width : "+(w - (width + 20f))+" | Cal Height : "+(h - (height+15f)));
        cs.drawText(dateTime, (w - (width + 20f)), (h - (height + 15f)), tPaint);

        ByteArrayOutputStream byteArrayOutputStream = null;

        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            //dest.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(new File("/sdcard/timeStampedImage.jpg")));
            dest.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);    // 100% - quality
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dest;
    }

    public static Bitmap stampWithTimeInBitmap(Bitmap src) {

        int w = src.getWidth();
        int h = src.getHeight();
        Log.e("ImageUtils", "Width : " + w + " | Height : " + h);
        Bitmap dest = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        String dateTime = sdf.format(Calendar.getInstance().getTime()); // reading local time in the system

        Canvas cs = new Canvas(dest);
        Paint tPaint = new Paint();
        tPaint.setTextSize(40);
        tPaint.setColor(Color.WHITE);
        //tPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        tPaint.setStyle(Paint.Style.FILL);
        cs.drawBitmap(src, 0f, 0f, null);

        float width = tPaint.measureText(dateTime);//bounds.width();
        float height = tPaint.measureText("yY");

        cs.drawText(dateTime, (w - (width + 20f)), (h - (height + 15f)), tPaint);

        ByteArrayOutputStream byteArrayOutputStream = null;

        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            //dest.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(new File("/sdcard/timeStampedImage.jpg")));
            dest.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);    // 100% - quality
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dest;
    }

    public static Bitmap stampWithTimeAndAddressInBitmap(Bitmap src, String capturedAt) {

        int w = src.getWidth();
        int h = src.getHeight();
        Log.e("ImageUtils", "Width : " + w + " | Height : " + h);
        Bitmap dest = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        String dateTime = sdf.format(Calendar.getInstance().getTime()); // reading local time in the system

        Canvas cs = new Canvas(dest);
        Paint tPaint = new Paint();
        tPaint.setTextSize(30);
        tPaint.setColor(Color.WHITE);
        //tPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        tPaint.setStyle(Paint.Style.FILL);
        cs.drawBitmap(src, 0f, 0f, null);

        float width = tPaint.measureText(dateTime);//bounds.width();
        float height = tPaint.measureText("yY");

        TextPaint tp = new TextPaint();
        tp.setColor(Color.WHITE);
        tp.setTextSize(30);
        tp.setAntiAlias(true);
        StaticLayout sl = new StaticLayout(capturedAt, tp, src.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        sl.draw(cs);

       /* for (String line : capturedAt.split(",")) {
            cs.drawText(line, 20f, height + 15f, tPaint);
            y += tPaint.descent() - tPaint.ascent();
        }*/
        //cs.drawText(capturedAt, 20f, height + 15f, tPaint);

        cs.drawText(dateTime, (w - (width + 20f)), (h - (height + 15f)), tPaint);

        ByteArrayOutputStream byteArrayOutputStream = null;

        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            //dest.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(new File("/sdcard/timeStampedImage.jpg")));
            dest.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);    // 100% - quality
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dest;
    }

    public static Bitmap getBitmap(ContentResolver mContentResolver, Uri uri, String latitude, String longitude, String currentTimestamp) {

        InputStream in = null;
        try {

            //Set Current Timestamp and Location ino image
            setLocationAndTimeStamp(latitude, longitude, currentTimestamp, uri);

            final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
            in = mContentResolver.openInputStream(uri);

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();

            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) > IMAGE_MAX_SIZE) {
                scale++;
            }
            Log.d(TAG, "scale = " + scale + ", orig-width: " + o.outWidth + ", orig-height: " + o.outHeight);

            Bitmap bitmap = null;
            in = mContentResolver.openInputStream(uri);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                bitmap = BitmapFactory.decodeStream(in, null, o);

                // resize to desired dimensions
                int height = bitmap.getHeight();
                int width = bitmap.getWidth();
                Log.d(TAG, "1th scale operation dimenions - width: " + width + ", height: " + height);

                double y = Math.sqrt(IMAGE_MAX_SIZE / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) x, (int) y, true);
                bitmap.recycle();
                bitmap = scaledBitmap;

                System.gc();
            } else {
                bitmap = BitmapFactory.decodeStream(in);
            }
            in.close();

            Log.d(TAG, "bitmap size - width: " + bitmap.getWidth() + ", height: " +
                    bitmap.getHeight());
            return bitmap;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }

    public static Bitmap getPortraitBitmap(ContentResolver mContentResolver, Uri uri, String latitude, String longitude, String currentTimestamp) {
        Bitmap bitmap = null;
        try {
            //Set Current Timestamp and Location ino image
            bitmap = getBitmap(mContentResolver, uri, latitude, longitude, currentTimestamp);
            bitmap = rotateImageIfRequired(mContentResolver, bitmap, uri);
        } catch (Exception e) {
            Log.e(TAG, "getPortraitBitmap: " + e.getMessage(), e);
            return null;
        }
        return bitmap;
    }

    public static Bitmap rotateImageIfRequired(ContentResolver mContentResolver, Bitmap img, Uri selectedImage) throws IOException {

        InputStream input = mContentResolver.openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else
            ei = new ExifInterface(selectedImage.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    public static void setLocationAndTimeStamp(String latitude, String longitude, String currentTimestamp, Uri picUri) {
        try {

            if (TextUtils.isEmpty(latitude) || TextUtils.isEmpty(longitude) || TextUtils.isEmpty(currentTimestamp))
                return;

            ExifInterface exif = new ExifInterface(picUri.getPath());

            double lat = Double.parseDouble(latitude);
            double lng = Double.parseDouble(longitude);

            //GPS TimeStamp
            String exifGPSTimestamp = CommonUtils.getFormattedGPSDateStamp("yyyy-MM-dd HH:mm:ss.SSS", "HH:mm:ss", currentTimestamp);

            //Get GPS DATESTAMP
            String gpsDateStamp = CommonUtils.getFormattedGPSDateStamp("yyyy-MM-dd HH:mm:ss.SSS", "yyyy:MM:dd HH:mm:ss", currentTimestamp);
            exif.setAttribute(ExifInterface.TAG_GPS_DATESTAMP, gpsDateStamp);
            exif.setAttribute(ExifInterface.TAG_GPS_TIMESTAMP, exifGPSTimestamp);
            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, GPSUtil.convert(lat));
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, GPSUtil.convert(lng));
            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, GPSUtil.latitudeRef(lat));
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, GPSUtil.longitudeRef(lng));
            exif.saveAttributes();
            Log.e("LATITUDE: ", latitude);
            Log.e("LONGITUDE: ", longitude);
            Log.e("TIMESTAMP: ", exifGPSTimestamp);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getResizedBitmap(Bitmap image) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = IMAGE_MAX_SIZE;
            height = (int) (width / bitmapRatio);
        } else {
            height = IMAGE_MAX_SIZE;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static String updateExifData(Uri uriPath, Bitmap compressedBitmap, String latitude, String longitude, String currentTimestamp) {
        try {
            // Save new image
            String path = Environment.getExternalStorageDirectory().toString();
            File file = new File(path, "temp.jpeg");
            if (file.exists())
                file.delete();

            FileOutputStream fos = new FileOutputStream(file);
            compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);

            // copy paste exif information from original file to new
            // file
            ExifInterface oldexif = new ExifInterface(uriPath.getPath());
            ExifInterface newexif = new ExifInterface(file.getAbsolutePath());

            int build = Build.VERSION.SDK_INT;


            // From API 11
            if (build >= 11) {
                if (oldexif.getAttribute("FNumber") != null) {
                    newexif.setAttribute("FNumber",
                            oldexif.getAttribute("FNumber"));
                }
                if (oldexif.getAttribute("ExposureTime") != null) {
                    newexif.setAttribute("ExposureTime",
                            oldexif.getAttribute("ExposureTime"));
                }
                if (oldexif.getAttribute("ISOSpeedRatings") != null) {
                    newexif.setAttribute("ISOSpeedRatings",
                            oldexif.getAttribute("ISOSpeedRatings"));
                }
            }
            // From API 9
            if (build >= 9) {
                if (oldexif.getAttribute("GPSAltitude") != null) {
                    newexif.setAttribute("GPSAltitude",
                            oldexif.getAttribute("GPSAltitude"));
                }
                if (oldexif.getAttribute("GPSAltitudeRef") != null) {
                    newexif.setAttribute("GPSAltitudeRef",
                            oldexif.getAttribute("GPSAltitudeRef"));
                }
            }
            // From API 8
            if (build >= 8) {
                if (oldexif.getAttribute("FocalLength") != null) {
                    newexif.setAttribute("FocalLength",
                            oldexif.getAttribute("FocalLength"));
                }
                if (oldexif.getAttribute("GPSDateStamp") != null) {
                    newexif.setAttribute("GPSDateStamp",
                            oldexif.getAttribute("GPSDateStamp"));
                }
                if (oldexif.getAttribute("GPSProcessingMethod") != null) {
                    newexif.setAttribute(
                            "GPSProcessingMethod",
                            oldexif.getAttribute("GPSProcessingMethod"));
                }
                if (oldexif.getAttribute("GPSTimeStamp") != null) {
                    newexif.setAttribute("GPSTimeStamp", ""
                            + oldexif.getAttribute("GPSTimeStamp"));
                }
            }
            if (oldexif.getAttribute("DateTime") != null) {
                newexif.setAttribute("DateTime",
                        oldexif.getAttribute("DateTime"));
            }
            if (oldexif.getAttribute("Flash") != null) {
                newexif.setAttribute("Flash",
                        oldexif.getAttribute("Flash"));
            }
            if (oldexif.getAttribute("GPSLatitude") != null) {
                newexif.setAttribute("GPSLatitude", oldexif.getAttribute("GPSLatitude"));
            }
            if (oldexif.getAttribute("GPSLatitudeRef") != null) {
                newexif.setAttribute("GPSLatitudeRef",
                        oldexif.getAttribute("GPSLatitudeRef"));
            }
            if (oldexif.getAttribute("GPSLongitude") != null) {
                newexif.setAttribute("GPSLongitude",
                        oldexif.getAttribute("GPSLongitude"));
            }
            if (oldexif.getAttribute("GPSLatitudeRef") != null) {
                newexif.setAttribute("GPSLongitudeRef",
                        oldexif.getAttribute("GPSLongitudeRef"));
            }

            if (oldexif.getAttribute("Make") != null) {
                newexif.setAttribute("Make",
                        oldexif.getAttribute("Make"));
            }
            if (oldexif.getAttribute("Model") != null) {
                newexif.setAttribute("Model",
                        oldexif.getAttribute("Model"));
            }
            if (oldexif.getAttribute("Orientation") != null) {
                newexif.setAttribute("Orientation",
                        oldexif.getAttribute("Orientation"));
            }
            if (oldexif.getAttribute("WhiteBalance") != null) {
                newexif.setAttribute("WhiteBalance",
                        oldexif.getAttribute("WhiteBalance"));
            }

            double lat = Double.parseDouble(latitude);
            double lng = Double.parseDouble(longitude);

            //GPS TimeStamp
            String exifGPSTimestamp = CommonUtils.getFormattedGPSDateStamp("yyyy-MM-dd HH:mm:ss.SSS", "HH:mm:ss", currentTimestamp);

            //Get GPS DATESTAMP
            String gpsDateStamp = CommonUtils.getFormattedGPSDateStamp("yyyy-MM-dd HH:mm:ss.SSS", "yyyy:MM:dd HH:mm:ss", currentTimestamp);
            newexif.setAttribute(ExifInterface.TAG_GPS_DATESTAMP, gpsDateStamp);
            newexif.setAttribute(ExifInterface.TAG_GPS_TIMESTAMP, exifGPSTimestamp);
            newexif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, GPSUtil.convert(lat));
            newexif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, GPSUtil.convert(lng));
            newexif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, GPSUtil.latitudeRef(lat));
            newexif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, GPSUtil.longitudeRef(lng));

            newexif.saveAttributes();

            // Read Final Bitmap with ExifData
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap finalBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

            return getStringFile(file.getAbsoluteFile());

            /*ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);*/

        } catch (Exception e) {
            Log.e(e.getClass().getName(), e.getMessage(), e);
        }
        return getBase64FromBitmap(compressedBitmap);
    }

    private static String getBase64FromBitmap(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

    }

    public static String convertLatLngToDMS(double dec) {
        String decString = String.valueOf(Math.abs(dec));
        String degreeString = decString.substring(0, decString.lastIndexOf("."));
        String remainderString = "0." + decString.substring((decString.lastIndexOf(".") + 1), decString.length());
        double minutes = Double.parseDouble(remainderString) * 60;
        String minutesString = String.valueOf(minutes);
        String minutesFraction = minutesString.substring(0, minutesString.lastIndexOf(".")) + "/1,";
        String secondsRemainderString = "0." + minutesString.substring((minutesString.lastIndexOf(".") + 1), minutesString.length());
        double seconds = Double.parseDouble(secondsRemainderString) * 60;
        String secondsFraction = String.valueOf(seconds).replace(".", "");
        String multiple = "1";
        for (int i = 0; i < secondsFraction.length() - 2; i++) {
            multiple = multiple + "0";
        }

        String result = degreeString + "/1," + minutesFraction + secondsFraction + "/" + multiple;

        Log.d(TAG, result);

        return result;
    }

    //region Converting File to Base64.encode String type using Method
    public static String getStringFile(File f) {
        InputStream inputStream = null;
        String encodedFile = "", lastVal;
        try {
            inputStream = new FileInputStream(f.getAbsolutePath());

            byte[] buffer = new byte[10240];//specify the size to allow
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output64.write(buffer, 0, bytesRead);
            }
            output64.close();
            encodedFile = output.toString();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastVal = encodedFile;
        return lastVal;
    }
    //endregion

    public static String getImageToBitmap(ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, ba);
        byte[] by = ba.toByteArray();
        String encod = Base64.encodeToString(by, Base64.DEFAULT);
        return encod;
    }


    public static String getDateTimeFromExif(Uri uriPath, Context context) {
        String dateTime = null;
        InputStream in = null;
        try {
            in = context.getContentResolver().openInputStream(uriPath);
            androidx.exifinterface.media.ExifInterface exifInterface = new androidx.exifinterface.media.ExifInterface(in);
            dateTime = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
            System.out.println(dateTime);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignored) {
                }
            }
        }
        return dateTime;
    }


}
