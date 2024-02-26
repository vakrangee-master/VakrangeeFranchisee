package in.vakrangee.core.commongui;

import android.content.Context;
import android.net.Uri;

import java.io.File;

public class FileUtils {

    public static String getFileExtension(Context context, Uri uri) {
        String type = null;

        try {
            File file = new File(uri.toString());
            type = file.getPath().substring(file.getPath().lastIndexOf(".") + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return type;
        /*ContentResolver cR = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String type = mime.getExtensionFromMimeType(cR.getType(uri));
        return type;*/
    }
}
