package in.vakrangee.core.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BrandingElementDto implements Serializable {

    @SerializedName("design_element_id")
    private String Id;

    @SerializedName("design_element_name")
    private String Name;

    public BrandingElementDto(String Id, String name){
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
