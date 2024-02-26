package in.vakrangee.franchisee.documentmanager;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AgreementUploadDetailsDto implements Serializable {

    @SerializedName("agreement_images")
    public List<AgreementImageDto> agreementImagesList;

    public AgreementUploadDetailsDto(){
        agreementImagesList = new ArrayList<AgreementImageDto>();
    }

    @SerializedName("stamp_franking_date")
    private String stampFrankingDate;

    @SerializedName("model_wise_page_count")
    private String modelWisePageCount;

    @SerializedName("page_count")
    private String noOfPages;

    @SerializedName("agreement_image_id")
    private String agreementImageId;

    @SerializedName("agreement_image_base64")
    private String agreementImageBase64;

    @SerializedName("agreement_image_ext")
    private String agreementImageExt;

    @SerializedName("agreement_file_path")
    private String agreementFilePath;

    @SerializedName("IsEditable")
    private String IsEditable;

    public List<AgreementImageDto> getAgreementImagesList() {
        return agreementImagesList;
    }

    public void setAgreementImagesList(List<AgreementImageDto> agreementImagesList) {
        this.agreementImagesList = agreementImagesList;
    }

    public String getStampFrankingDate() {
        return stampFrankingDate;
    }

    public void setStampFrankingDate(String stampFrankingDate) {
        this.stampFrankingDate = stampFrankingDate;
    }

    public String getNoOfPages() {
        return noOfPages;
    }

    public void setNoOfPages(String noOfPages) {
        this.noOfPages = noOfPages;
    }

    public String getAgreementImageId() {
        return agreementImageId;
    }

    public void setAgreementImageId(String agreementImageId) {
        this.agreementImageId = agreementImageId;
    }

    public String getAgreementImageBase64() {
        return agreementImageBase64;
    }

    public void setAgreementImageBase64(String agreementImageBase64) {
        this.agreementImageBase64 = agreementImageBase64;
    }

    public String getAgreementImageExt() {
        return agreementImageExt;
    }

    public void setAgreementImageExt(String agreementImageExt) {
        this.agreementImageExt = agreementImageExt;
    }

    public String getAgreementFilePath() {
        return agreementFilePath;
    }

    public void setAgreementFilePath(String agreementFilePath) {
        this.agreementFilePath = agreementFilePath;
    }

    public String getIsEditable() {
        return IsEditable;
    }

    public void setIsEditable(String isEditable) {
        IsEditable = isEditable;
    }

    public String getModelWisePageCount() {
        return modelWisePageCount;
    }

    public void setModelWisePageCount(String modelWisePageCount) {
        this.modelWisePageCount = modelWisePageCount;
    }
}
