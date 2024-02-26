package in.vakrangee.core.commongui;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;

import com.nononsenseapps.filepicker.FilePickerFragment;

import java.io.File;

@SuppressLint("ValidFragment")
public class FilteredFilePickerFragment extends FilePickerFragment {

    private String fileType;
    private static final String FILE_TYPE_IMAGES = "images";
    private static final String FILE_TYPE_PDF = "pdf";
    private static final String FILE_TYPE_IMAGES_PDF = "images/pdf";

    public FilteredFilePickerFragment(String fileType){
        this.fileType  = fileType;
    }

    /**
     * @param file
     * @return The file extension. If file has no extension, it returns null.
     */
    private String getExtension(@NonNull File file) {
        String path = file.getPath();
        int i = path.lastIndexOf(".");
        if (i < 0) {
            return null;
        } else {
            return path.substring(i);
        }
    }

    @Override
    protected boolean isItemVisible(final File file) {
        boolean ret = super.isItemVisible(file);
        if (ret && !isDir(file) && (mode == MODE_FILE || mode == MODE_FILE_AND_DIR)) {
            String ext = getExtension(file);

            //Check File Type
            if(fileType.equalsIgnoreCase(FILE_TYPE_IMAGES)){
                return ext != null && (".jpg".equalsIgnoreCase(ext) || ".jpeg".equalsIgnoreCase(ext) || ".png".equalsIgnoreCase(ext));
            } else if(fileType.equalsIgnoreCase(FILE_TYPE_PDF)){
                return ext != null && (".pdf".equalsIgnoreCase(ext));
            } else if(fileType.equalsIgnoreCase(FILE_TYPE_IMAGES_PDF)){
                return ext != null && (".pdf".equalsIgnoreCase(ext) ||".jpg".equalsIgnoreCase(ext) || ".jpeg".equalsIgnoreCase(ext) || ".png".equalsIgnoreCase(ext));
            }
        }
        return ret;
    }
}
