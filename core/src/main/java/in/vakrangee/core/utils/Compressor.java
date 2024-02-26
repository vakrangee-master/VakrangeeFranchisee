package in.vakrangee.core.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Compressor {

    private static int height;
    private static int width;
    private static int inSampleSize;
    private static String encodedfile;

    public Compressor() {
    }

    public static File compressImageFile(File imageFile, int reqHeight, int reqWidth, String filePath, int quality, Bitmap.CompressFormat compressFormat, int orientation) throws IOException {
        FileOutputStream fileOutputStream = null;
        File file = (new File(filePath)).getParentFile();
        if (!file.exists()) {
            file.mkdirs();
        }

        try {
            fileOutputStream = new FileOutputStream(filePath);
            decodeBitmapAndCompress(imageFile, reqHeight, reqWidth, orientation).compress(compressFormat, quality, fileOutputStream);
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }

        }

        return new File(filePath);
    }

    public static Bitmap decodeBitmapAndCompress(File imageFile, int reqHeight, int reqWidth, int reqOrientation) throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
        options.inSampleSize = calculateSampleSize(options, reqHeight, reqWidth);
        options.inJustDecodeBounds = false;
        Bitmap scaledBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
        ExifInterface exifInterface = new ExifInterface(imageFile.getAbsolutePath());
        int orientation = exifInterface.getAttributeInt("Orientation", 0);
        Matrix matrix = new Matrix();
        if (orientation == 6) {
            matrix.postRotate(90.0F);
        } else if (orientation == 3) {
            matrix.postRotate(180.0F);
        } else if (orientation == 8) {
            matrix.postRotate(270.0F);
        }

        if (reqOrientation > 0) {
            matrix.postRotate((float) reqOrientation);
        }

        scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        return scaledBitmap;
    }

    private static int calculateSampleSize(BitmapFactory.Options options, int reqHeight, int reqWidth) {
        height = options.outHeight;
        width = options.outWidth;
        inSampleSize = 1;
        int halfHeight = height / 2;
        int halfWidth = width / 2;
        if (height > reqHeight || width > reqWidth) {
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static String getBase64forCompressedImage(File compressFile) {
        FileInputStream fileInputStreamReader = null;
        byte[] bytes = new byte[(int) compressFile.length()];

        try {
            fileInputStreamReader = new FileInputStream(compressFile);
            fileInputStreamReader.read(bytes);
            encodedfile = Base64.encodeToString(bytes, 0);
        } catch (IOException var4) {
            var4.printStackTrace();
        }

        return encodedfile;
    }
}
