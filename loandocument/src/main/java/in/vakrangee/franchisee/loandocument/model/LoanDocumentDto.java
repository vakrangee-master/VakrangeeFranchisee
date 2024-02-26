package in.vakrangee.franchisee.loandocument.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LoanDocumentDto implements Serializable {

    @SerializedName("vkId")
    private String vkId;

    @SerializedName("agreement_images")
    public List<AgreementImageDto> agreementImagesList;

    public LoanDocumentDto() {
        agreementImagesList = new ArrayList<AgreementImageDto>();
    }

    @SerializedName("franchisee_loan_request_id")
    private String franchiseeLoanRequestId;

    @SerializedName("loan_document_id")
    private String loanDocumentId;

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("is_mandatory")
    private String IsMandatory;

    @SerializedName("is_browse")
    private String IsBrowse;

    @SerializedName("browse_file_type")
    private String browseFileType;

    @SerializedName("file_size")
    private String fileSize;

    @SerializedName("loan_document_img_id")
    private String loanDocumentImgId;

    @SerializedName("loan_document_img_base64")
    private String loanDocumentImgBase64;

    @SerializedName("loan_document_img_ext")
    private String loanDocumentImgExt;

    @SerializedName("agreement_file_path")
    private String agreementFilePath;

    @SerializedName("is_editable")
    private String IsEditable;

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

    public String getLoanDocumentImgId() {
        return loanDocumentImgId;
    }

    public void setLoanDocumentImgId(String loanDocumentImgId) {
        this.loanDocumentImgId = loanDocumentImgId;
    }

    public String getLoanDocumentImgBase64() {
        return loanDocumentImgBase64;
    }

    public void setLoanDocumentImgBase64(String loanDocumentImgBase64) {
        this.loanDocumentImgBase64 = loanDocumentImgBase64;
    }

    public String getLoanDocumentImgExt() {
        return loanDocumentImgExt;
    }

    public void setLoanDocumentImgExt(String loanDocumentImgExt) {
        this.loanDocumentImgExt = loanDocumentImgExt;
    }

    public String getLoanDocumentId() {
        return loanDocumentId;
    }

    public void setLoanDocumentId(String loanDocumentId) {
        this.loanDocumentId = loanDocumentId;
    }

    public String getIsMandatory() {
        return IsMandatory;
    }

    public void setIsMandatory(String isMandatory) {
        IsMandatory = isMandatory;
    }

    public String getFranchiseeLoanRequestId() {
        return franchiseeLoanRequestId;
    }

    public void setFranchiseeLoanRequestId(String franchiseeLoanRequestId) {
        this.franchiseeLoanRequestId = franchiseeLoanRequestId;
    }

    public String getVkId() {
        return vkId;
    }

    public void setVkId(String vkId) {
        this.vkId = vkId;
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

    public String getIsBrowse() {
        return IsBrowse;
    }

    public void setIsBrowse(String isBrowse) {
        IsBrowse = isBrowse;
    }

    public String getBrowseFileType() {
        return browseFileType;
    }

    public void setBrowseFileType(String browseFileType) {
        this.browseFileType = browseFileType;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }
}
