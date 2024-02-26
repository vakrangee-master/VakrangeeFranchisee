package in.vakrangee.core.commongui;

import com.google.gson.annotations.SerializedName;

public class FileAttachmentDto {

    @SerializedName("fileName")
    private String fileName;

    @SerializedName("filePath")
    private String filePath;

    @SerializedName("fileExtension")
    private String fileExtension;

    @SerializedName("IsFile")
    private boolean IsFile;

    @SerializedName("IsDirectory")
    private boolean IsDirectory;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public boolean isFile() {
        return IsFile;
    }

    public void setFile(boolean file) {
        IsFile = file;
    }

    public boolean isDirectory() {
        return IsDirectory;
    }

    public void setDirectory(boolean directory) {
        IsDirectory = directory;
    }

}
