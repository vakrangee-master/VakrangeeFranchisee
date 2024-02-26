package in.vakrangee.core.support;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WorkCommencementStatusDto implements Serializable {

    @SerializedName("Id")
    private String Id;

    @SerializedName("Status")
    private String Name;

    public WorkCommencementStatusDto(String Id, String name){
        this.Id = Id;
        this.Name = name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
