package in.vakrangee.franchisee.gwr.evidence;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EvidenceDto implements Serializable {

    @SerializedName("fileName")
    private String fileName;

    @SerializedName("file")
    private String file;

    @SerializedName("filePath")
    private String filePath;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
