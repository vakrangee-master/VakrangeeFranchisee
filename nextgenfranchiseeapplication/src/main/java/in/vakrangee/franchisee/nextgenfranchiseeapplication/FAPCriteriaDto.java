package in.vakrangee.franchisee.nextgenfranchiseeapplication;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FAPCriteriaDto implements Serializable {

    @SerializedName("criteria1")
    private String isAbleToDedecateFullTime;

    @SerializedName("criteria2")
    private String isAffiliatedToPoliticalParty;

    @SerializedName("criteria3")
    private String isFiledBankruptcyBefore;

    @SerializedName("criteria4")
    private String isLegalProceeding;

    @SerializedName("criteria5")
    private String isFIRFiled;

    @SerializedName("BackgroundVerificationFileId")
    private String BackgroundVerificationFileId;

    @SerializedName("BgVerificationFileExt")
    private String BgVerificationFileExt;

    @SerializedName("hearAboutUsId")
    private String aboutUsFrom;

    @SerializedName("BgVerificationConsentBase64")
    private String bgVerficationConsentBase64;

    private transient String bgVerficationConsentFileName;
    private transient String isKendraFranchiseeApplicationExit;

    public String getIsAbleToDedecateFullTime() {
        return isAbleToDedecateFullTime;
    }

    public void setIsAbleToDedecateFullTime(String isAbleToDedecateFullTime) {
        this.isAbleToDedecateFullTime = isAbleToDedecateFullTime;
    }

    public String getIsAffiliatedToPoliticalParty() {
        return isAffiliatedToPoliticalParty;
    }

    public void setIsAffiliatedToPoliticalParty(String isAffiliatedToPoliticalParty) {
        this.isAffiliatedToPoliticalParty = isAffiliatedToPoliticalParty;
    }

    public String getIsFiledBankruptcyBefore() {
        return isFiledBankruptcyBefore;
    }

    public void setIsFiledBankruptcyBefore(String isFiledBankruptcyBefore) {
        this.isFiledBankruptcyBefore = isFiledBankruptcyBefore;
    }

    public String getIsLegalProceeding() {
        return isLegalProceeding;
    }

    public void setIsLegalProceeding(String isLegalProceeding) {
        this.isLegalProceeding = isLegalProceeding;
    }

    public String getIsFIRFiled() {
        return isFIRFiled;
    }

    public void setIsFIRFiled(String isFIRFiled) {
        this.isFIRFiled = isFIRFiled;
    }

    public String getAboutUsFrom() {
        return aboutUsFrom;
    }

    public void setAboutUsFrom(String aboutUsFrom) {
        this.aboutUsFrom = aboutUsFrom;
    }

    public String getIsKendraFranchiseeApplicationExit() {
        return isKendraFranchiseeApplicationExit;
    }

    public void setIsKendraFranchiseeApplicationExit(String isKendraFranchiseeApplicationExit) {
        this.isKendraFranchiseeApplicationExit = isKendraFranchiseeApplicationExit;
    }

    public String getBgVerficationConsentBase64() {
        return bgVerficationConsentBase64;
    }

    public void setBgVerficationConsentBase64(String bgVerficationConsentBase64) {
        this.bgVerficationConsentBase64 = bgVerficationConsentBase64;
    }

    public String getBgVerficationConsentFileName() {
        return bgVerficationConsentFileName;
    }

    public void setBgVerficationConsentFileName(String bgVerficationConsentFileName) {
        this.bgVerficationConsentFileName = bgVerficationConsentFileName;
    }

    public String getBackgroundVerificationFileId() {
        return BackgroundVerificationFileId;
    }

    public void setBackgroundVerificationFileId(String backgroundVerificationFileId) {
        BackgroundVerificationFileId = backgroundVerificationFileId;
    }

    public String getBgVerificationFileExt() {
        return BgVerificationFileExt;
    }

    public void setBgVerificationFileExt(String bgVerificationFileExt) {
        BgVerificationFileExt = bgVerificationFileExt;
    }
}
