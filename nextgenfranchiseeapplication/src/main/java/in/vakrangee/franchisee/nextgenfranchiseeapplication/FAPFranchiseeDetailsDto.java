package in.vakrangee.franchisee.nextgenfranchiseeapplication;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FAPFranchiseeDetailsDto implements Serializable {

    @SerializedName("VakrangeeKendraType")
    private String vakrangeeKendraType;

    @SerializedName("EntityTypeId")
    private String entityType;

    @SerializedName("EntityName")
    private String entityName;

    @SerializedName("IoclRoCode")
    private String IoclRoCode;

    @SerializedName("IoclRoName")
    private String IoclRoName;

    @SerializedName("nayaraRoCode")
    private String nayaraRoCode;

    @SerializedName("nayaraRoName")
    private String nayaraRoName;

    @SerializedName("ReferredById")
    private String referredType;

    @SerializedName("NameOfReferralCode")
    private String empVKID;

    @SerializedName("EnquiryTitleId")
    private String enquiryTitle;

    @SerializedName("EnquiryFirstName")
    private String enquiryFirstName;

    @SerializedName("EnquiryMiddleName")
    private String enquiryMiddleName;

    @SerializedName("EnquiryLastName")
    private String enquiryLastName;

    @SerializedName("ApplicantPicFileId")
    private String ApplicantPicFileId;

    @SerializedName("ApplicantSalutationId")
    private String applicantSalutation;

    @SerializedName("ApplicantFirstName")
    private String applicantFirstName;

    @SerializedName("ApplicantMiddleName")
    private String applicantMiddleName;

    @SerializedName("ApplicantLastName")
    private String applicantLastName;

    @SerializedName("FatherSalutationId")
    private String fatherSalutation;

    @SerializedName("FatherFirstName")
    private String fatherFirstName;

    @SerializedName("FatherMiddleName")
    private String fatherMiddleName;

    @SerializedName("FatherLastName")
    private String fatherLastName;

    @SerializedName("SpouseSalutationId")
    private String spouseSalutation;

    @SerializedName("SpouseFirstName")
    private String spouseFirstName;

    @SerializedName("SpouseMiddleName")
    private String spouseMiddleName;

    @SerializedName("SpouseLastName")
    private String spouseLastName;

    @SerializedName("Gender")
    private String gender;

    @SerializedName("MaritalStatus")
    private String maritalStatus;

    @SerializedName("DateOfBirth")
    private String dateOfBirth;

    @SerializedName("year_of_passing")
    private String yearOfPassing;

    @SerializedName("NationalityId")
    private String nationality;

    @SerializedName("QualificationId")
    private String highestQualification;

    @SerializedName("QualificationUploadFileId")
    private String QualificationUploadFileId;

    @SerializedName("PriorExperienceId")
    private String priorExperience;

    @SerializedName("CurrentOccupationId")
    private String currentOccupation;

    @SerializedName("IsSpeciallyAbled")
    private String isSpeciallyAbled;

    @SerializedName("IsPriorMilitaryExperience")
    private String isPriorMilitaryExperience;

    @SerializedName("ApplicantPicFileBase64")
    private String profilePicBase64;

    @SerializedName("ApplicantPicFileExt")
    private String profilePicExt;

    @SerializedName("QualificationFileBase64")
    private String highestQualiUploadBase64;

    @SerializedName("QualiFileExt")
    private String highestQualiFileExt;

    @SerializedName("DateOfIncorporation")
    private String dateOfIncorporation;

    @SerializedName("EntityProofTypeId")
    private String entityProofType;

    @SerializedName("EntityProofFileId")
    private String entityProofFileId;

    @SerializedName("EntityProofFileBase64")
    private String entityProofFileBase64;

    @SerializedName("EntityProofFileExt")
    private String entityProofFileExt;

    private transient String profilePicName;
    private transient String highestQualificationName;
    private transient String entityProofName;
    private transient String nameOfReferral;

    @SerializedName("is_having_bca_code")
    private String isHavingBCACode;

    @SerializedName("bca_bank_name")
    private String bcaBankName;

    @SerializedName("bca_code")
    private String bcaCode;

    public String getYearOfPassing() {
        return yearOfPassing;
    }

    public void setYearOfPassing(String yearOfPassing) {
        this.yearOfPassing = yearOfPassing;
    }

    public String getIsHavingBCACode() {
        return isHavingBCACode;
    }

    public void setIsHavingBCACode(String isHavingBCACode) {
        this.isHavingBCACode = isHavingBCACode;
    }

    public String getBcaBankName() {
        return bcaBankName;
    }

    public void setBcaBankName(String bcaBankName) {
        this.bcaBankName = bcaBankName;
    }

    public String getBcaCode() {
        return bcaCode;
    }

    public void setBcaCode(String bcaCode) {
        this.bcaCode = bcaCode;
    }

    public String getVakrangeeKendraType() {
        return vakrangeeKendraType;
    }

    public void setVakrangeeKendraType(String vakrangeeKendraType) {
        this.vakrangeeKendraType = vakrangeeKendraType;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getReferredType() {
        return referredType;
    }

    public void setReferredType(String referredType) {
        this.referredType = referredType;
    }

    public String getEnquiryTitle() {
        return enquiryTitle;
    }

    public void setEnquiryTitle(String enquiryTitle) {
        this.enquiryTitle = enquiryTitle;
    }

    public String getEnquiryFirstName() {
        return enquiryFirstName;
    }

    public void setEnquiryFirstName(String enquiryFirstName) {
        this.enquiryFirstName = enquiryFirstName;
    }

    public String getEnquiryMiddleName() {
        return enquiryMiddleName;
    }

    public void setEnquiryMiddleName(String enquiryMiddleName) {
        this.enquiryMiddleName = enquiryMiddleName;
    }

    public String getEnquiryLastName() {
        return enquiryLastName;
    }

    public void setEnquiryLastName(String enquiryLastName) {
        this.enquiryLastName = enquiryLastName;
    }

    public String getApplicantSalutation() {
        return applicantSalutation;
    }

    public void setApplicantSalutation(String applicantSalutation) {
        this.applicantSalutation = applicantSalutation;
    }

    public String getApplicantFirstName() {
        return applicantFirstName;
    }

    public void setApplicantFirstName(String applicantFirstName) {
        this.applicantFirstName = applicantFirstName;
    }

    public String getApplicantMiddleName() {
        return applicantMiddleName;
    }

    public void setApplicantMiddleName(String applicantMiddleName) {
        this.applicantMiddleName = applicantMiddleName;
    }

    public String getApplicantLastName() {
        return applicantLastName;
    }

    public void setApplicantLastName(String applicantLastName) {
        this.applicantLastName = applicantLastName;
    }

    public String getFatherSalutation() {
        return fatherSalutation;
    }

    public void setFatherSalutation(String fatherSalutation) {
        this.fatherSalutation = fatherSalutation;
    }

    public String getFatherFirstName() {
        return fatherFirstName;
    }

    public void setFatherFirstName(String fatherFirstName) {
        this.fatherFirstName = fatherFirstName;
    }

    public String getFatherMiddleName() {
        return fatherMiddleName;
    }

    public void setFatherMiddleName(String fatherMiddleName) {
        this.fatherMiddleName = fatherMiddleName;
    }

    public String getFatherLastName() {
        return fatherLastName;
    }

    public void setFatherLastName(String fatherLastName) {
        this.fatherLastName = fatherLastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getHighestQualification() {
        return highestQualification;
    }

    public void setHighestQualification(String highestQualification) {
        this.highestQualification = highestQualification;
    }

    public String getHighestQualiUploadBase64() {
        return highestQualiUploadBase64;
    }

    public void setHighestQualiUploadBase64(String highestQualiUploadBase64) {
        this.highestQualiUploadBase64 = highestQualiUploadBase64;
    }

    public String getPriorExperience() {
        return priorExperience;
    }

    public void setPriorExperience(String priorExperience) {
        this.priorExperience = priorExperience;
    }

    public String getCurrentOccupation() {
        return currentOccupation;
    }

    public void setCurrentOccupation(String currentOccupation) {
        this.currentOccupation = currentOccupation;
    }

    public String getIsSpeciallyAbled() {
        return isSpeciallyAbled;
    }

    public void setIsSpeciallyAbled(String isSpeciallyAbled) {
        this.isSpeciallyAbled = isSpeciallyAbled;
    }

    public String getIsPriorMilitaryExperience() {
        return isPriorMilitaryExperience;
    }

    public void setIsPriorMilitaryExperience(String isPriorMilitaryExperience) {
        this.isPriorMilitaryExperience = isPriorMilitaryExperience;
    }

    public String getSpouseSalutation() {
        return spouseSalutation;
    }

    public void setSpouseSalutation(String spouseSalutation) {
        this.spouseSalutation = spouseSalutation;
    }

    public String getSpouseFirstName() {
        return spouseFirstName;
    }

    public void setSpouseFirstName(String spouseFirstName) {
        this.spouseFirstName = spouseFirstName;
    }

    public String getSpouseMiddleName() {
        return spouseMiddleName;
    }

    public void setSpouseMiddleName(String spouseMiddleName) {
        this.spouseMiddleName = spouseMiddleName;
    }

    public String getSpouseLastName() {
        return spouseLastName;
    }

    public void setSpouseLastName(String spouseLastName) {
        this.spouseLastName = spouseLastName;
    }

    public String getDateOfIncorporation() {
        return dateOfIncorporation;
    }

    public void setDateOfIncorporation(String dateOfIncorporation) {
        this.dateOfIncorporation = dateOfIncorporation;
    }

    public String getEmpVKID() {
        return empVKID;
    }

    public void setEmpVKID(String empVKID) {
        this.empVKID = empVKID;
    }

    public String getNameOfReferral() {
        return nameOfReferral;
    }

    public void setNameOfReferral(String nameOfReferral) {
        this.nameOfReferral = nameOfReferral;
    }

    public String getProfilePicBase64() {
        return profilePicBase64;
    }

    public void setProfilePicBase64(String profilePicBase64) {
        this.profilePicBase64 = profilePicBase64;
    }

    public String getProfilePicName() {
        return profilePicName;
    }

    public void setProfilePicName(String profilePicName) {
        this.profilePicName = profilePicName;
    }

    public String getHighestQualificationName() {
        return highestQualificationName;
    }

    public void setHighestQualificationName(String highestQualificationName) {
        this.highestQualificationName = highestQualificationName;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getIoclRoCode() {
        return IoclRoCode;
    }

    public void setIoclRoCode(String ioclRoCode) {
        IoclRoCode = ioclRoCode;
    }

    public String getIoclRoName() {
        return IoclRoName;
    }

    public void setIoclRoName(String ioclRoName) {
        IoclRoName = ioclRoName;
    }

    public String getApplicantPicFileId() {
        return ApplicantPicFileId;
    }

    public void setApplicantPicFileId(String applicantPicFileId) {
        ApplicantPicFileId = applicantPicFileId;
    }

    public String getQualificationUploadFileId() {
        return QualificationUploadFileId;
    }

    public void setQualificationUploadFileId(String qualificationUploadFileId) {
        QualificationUploadFileId = qualificationUploadFileId;
    }

    public String getProfilePicExt() {
        return profilePicExt;
    }

    public void setProfilePicExt(String profilePicExt) {
        this.profilePicExt = profilePicExt;
    }

    public String getHighestQualiFileExt() {
        return highestQualiFileExt;
    }

    public void setHighestQualiFileExt(String highestQualiFileExt) {
        this.highestQualiFileExt = highestQualiFileExt;
    }

    public String getEntityProofType() {
        return entityProofType;
    }

    public void setEntityProofType(String entityProofType) {
        this.entityProofType = entityProofType;
    }

    public String getEntityProofFileId() {
        return entityProofFileId;
    }

    public void setEntityProofFileId(String entityProofFileId) {
        this.entityProofFileId = entityProofFileId;
    }

    public String getEntityProofFileBase64() {
        return entityProofFileBase64;
    }

    public void setEntityProofFileBase64(String entityProofFileBase64) {
        this.entityProofFileBase64 = entityProofFileBase64;
    }

    public String getEntityProofFileExt() {
        return entityProofFileExt;
    }

    public void setEntityProofFileExt(String entityProofFileExt) {
        this.entityProofFileExt = entityProofFileExt;
    }

    public String getEntityProofName() {
        return entityProofName;
    }

    public void setEntityProofName(String entityProofName) {
        this.entityProofName = entityProofName;
    }

    public String getNayaraRoCode() {
        return nayaraRoCode;
    }

    public void setNayaraRoCode(String nayaraRoCode) {
        this.nayaraRoCode = nayaraRoCode;
    }

    public String getNayaraRoName() {
        return nayaraRoName;
    }

    public void setNayaraRoName(String nayaraRoName) {
        this.nayaraRoName = nayaraRoName;
    }
}
