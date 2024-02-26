package in.vakrangee.franchisee.atmtechlivechecklist.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OptionsDto implements Serializable {

    @SerializedName("option_no")
    private String optionNo;

    @SerializedName(value = "id", alternate = "option_id")
    private String id;

    @SerializedName(value = "name", alternate = "option_name")
    private String name;

    @SerializedName("is_correct_option")
    private String isCorrectOption;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOptionNo() {
        return optionNo;
    }

    public void setOptionNo(String optionNo) {
        this.optionNo = optionNo;
    }

    public String getIsCorrectOption() {
        return isCorrectOption;
    }

    public void setIsCorrectOption(String isCorrectOption) {
        this.isCorrectOption = isCorrectOption;
    }

    @Override
    public String toString() {
        return  name;
    }
}
