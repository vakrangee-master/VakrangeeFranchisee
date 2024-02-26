package in.vakrangee.core.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.File;
import java.io.IOException;

public class ImageZipper {

    private int maxWidth = 612;
    private int maxHeight = 816;
    private int quality = 80;
    private int orientation = 0;
    private Bitmap.CompressFormat compressFormat;
    String destinationDirectory;

    public ImageZipper(Context context) {
        this.compressFormat = Bitmap.CompressFormat.JPEG;
        this.destinationDirectory = context.getCacheDir().getPath() + File.separator + "images";
    }

    public ImageZipper setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        return this;
    }

    public ImageZipper setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        return this;
    }

    public ImageZipper setQuality(int quality) {
        this.quality = quality;
        return this;
    }

    public ImageZipper setOrientation(int orientation) {
        this.orientation = orientation;
        return this;
    }

    public ImageZipper setCompressFormat(Bitmap.CompressFormat compressFormat) {
        this.compressFormat = compressFormat;
        return this;
    }

    public Bitmap compressToBitmap(File imageFile) throws IOException {
        return Compressor.decodeBitmapAndCompress(imageFile, this.maxHeight, this.maxWidth, this.orientation);
    }

    public File compressToFile(File imageFile) throws IOException {
        return this.compressToFile(imageFile, imageFile.getName(), this.orientation);
    }

    public File compressToFile(File imageFile, String fileName, int orientation) throws IOException {
        return Compressor.compressImageFile(imageFile, this.maxHeight, this.maxWidth, this.destinationDirectory + File.separator + fileName, this.quality, this.compressFormat, orientation);
    }

    public static String getBase64forImage(File compressFile) {
        return Compressor.getBase64forCompressedImage(compressFile);
    }

    public static Bitmap decodeBase64(String base64) {
        byte[] decodedBytes = Base64.decode(base64, 0);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        return decodedImage;
    }
}
