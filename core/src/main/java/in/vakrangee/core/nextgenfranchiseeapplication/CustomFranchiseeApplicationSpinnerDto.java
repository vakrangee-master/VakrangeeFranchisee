package in.vakrangee.core.nextgenfranchiseeapplication;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CustomFranchiseeApplicationSpinnerDto implements Serializable {

    @SerializedName(value = "id", alternate = "Id")
    private String Id;

    @SerializedName(value = "name", alternate = "Name")
    private String Name;

    public CustomFranchiseeApplicationSpinnerDto(String Id, String Name) {
        this.Id = Id;
        this.Name = Name;
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

    @Override
    public String toString() {
        return  Name;
    }
}
