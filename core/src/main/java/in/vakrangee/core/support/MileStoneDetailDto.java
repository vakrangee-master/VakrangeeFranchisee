package in.vakrangee.core.support;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MileStoneDetailDto implements Serializable {

    @SerializedName("nextgen_tis_milestone_id")
    private String Id;

    @SerializedName("nextgen_tis_milestone")
    private String Name;

    public MileStoneDetailDto(String Id, String name){
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
