package in.vakrangee.franchisee.sitelayout.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import in.vakrangee.supercore.franchisee.model.SiteReadinessCheckListDto;

public class MandatoryCategoryDto implements Serializable {

    @SerializedName("element_id")
    private String Id;

    @SerializedName("element_name")
    private String name;

    @SerializedName("element_description")
    private String desc;


}
