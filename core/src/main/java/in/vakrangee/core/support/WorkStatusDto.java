package in.vakrangee.core.support;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WorkStatusDto implements Serializable {

    @SerializedName("work_status_id")
    private String Id;

    @SerializedName("work_status_name")
    private String Name;

    public WorkStatusDto(String Id, String name){
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
