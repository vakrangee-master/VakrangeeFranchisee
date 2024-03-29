package in.vakrangee.core.support;

import com.google.gson.annotations.SerializedName;

public class IssueTypeDto {

    @SerializedName("tsdIssueType")
    private String description;

    @SerializedName("tsdIssueTypeId")
    private String Id;

    public IssueTypeDto(String Id, String description){
        this.Id = Id;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
