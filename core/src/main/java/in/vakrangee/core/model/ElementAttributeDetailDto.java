package in.vakrangee.core.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ElementAttributeDetailDto implements Serializable {

    @SerializedName("attr_name")
    private String attributeName;

    @SerializedName("attr_value")
    private String attributeVallue;

    @SerializedName("attr_img_id")
    private String attributeImageId;

    @SerializedName("uom")
    private String attributeUnit;

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeVallue() {
        return attributeVallue;
    }

    public void setAttributeVallue(String attributeVallue) {
        this.attributeVallue = attributeVallue;
    }

    public String getAttributeImageId() {
        return attributeImageId;
    }

    public void setAttributeImageId(String attributeImageId) {
        this.attributeImageId = attributeImageId;
    }

    public String getAttributeUnit() {
        return attributeUnit;
    }

    public void setAttributeUnit(String attributeUnit) {
        this.attributeUnit = attributeUnit;
    }
}
