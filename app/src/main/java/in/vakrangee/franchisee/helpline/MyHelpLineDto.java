package in.vakrangee.franchisee.helpline;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MyHelpLineDto implements Serializable {

    @SerializedName("type")
    private String type;

    @SerializedName("designation")
    private String designation;

    @SerializedName("name")
    private String name;

    @SerializedName("emailId")
    private String emailId;

    @SerializedName("mobileNumber")
    private String mobileNumber;

    @SerializedName("image")
    private String image;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
