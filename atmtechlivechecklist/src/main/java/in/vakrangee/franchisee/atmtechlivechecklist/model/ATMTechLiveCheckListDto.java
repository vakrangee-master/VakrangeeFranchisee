package in.vakrangee.franchisee.atmtechlivechecklist.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ATMTechLiveCheckListDto implements Serializable {

    @SerializedName("no")
    private String no;

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("value")
    private String value;

    @SerializedName("ct")
    private String ct;

    @SerializedName("isMan")
    private String isMan;

    @SerializedName("isImg")
    private String isImg;

    @SerializedName("img")
    private String img;

    @SerializedName("proof_pic_base64")
    private String proofPicBase64;

    @SerializedName("proof_image_ext")
    private String proofExt;

    @SerializedName("minL")
    private String minL;

    @SerializedName("maxL")
    private String maxL;

    @SerializedName("regex")
    private String regex;

    @SerializedName("saveId")
    private String saveId;

    @SerializedName("options")
    public List<OptionsDto> optionsList;

    @SerializedName("answer_txt")
    private String answerTxt;

    @SerializedName("opt_selected_answers")
    public List<OptionsDto> optSelectedAnswersList;

    public transient HashMap<Integer, OptionsDto> selectedAnsList;

    public ATMTechLiveCheckListDto() {
        optionsList = new ArrayList<OptionsDto>();
        selectedAnsList = new HashMap<Integer, OptionsDto>();
        optSelectedAnswersList = new ArrayList<OptionsDto>();
    }

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public String getIsMan() {
        return isMan;
    }

    public void setIsMan(String isMan) {
        this.isMan = isMan;
    }

    public String getIsImg() {
        return isImg;
    }

    public void setIsImg(String isImg) {
        this.isImg = isImg;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getMinL() {
        return minL;
    }

    public void setMinL(String minL) {
        this.minL = minL;
    }

    public String getMaxL() {
        return maxL;
    }

    public void setMaxL(String maxL) {
        this.maxL = maxL;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getSaveId() {
        return saveId;
    }

    public void setSaveId(String saveId) {
        this.saveId = saveId;
    }

    public String getAnswerTxt() {
        return answerTxt;
    }

    public void setAnswerTxt(String answerTxt) {
        this.answerTxt = answerTxt;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getProofPicBase64() {
        return proofPicBase64;
    }

    public void setProofPicBase64(String proofPicBase64) {
        this.proofPicBase64 = proofPicBase64;
    }

    public String getProofExt() {
        return proofExt;
    }

    public void setProofExt(String proofExt) {
        this.proofExt = proofExt;
    }
}
