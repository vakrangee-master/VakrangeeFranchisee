package in.vakrangee.franchisee.sitelayout.model;

public class AddressProofDetailsDTO {


    private String kycId;
    private String kycDesc;
    private String kycName;


    public AddressProofDetailsDTO(String kycId, String kycDesc, String kycName) {
        this.kycId = kycId;
        this.kycDesc = kycDesc;
        this.kycName = kycName;
    }

    public String getKycId() {
        return kycId;
    }

    public void setKycId(String kycId) {
        this.kycId = kycId;
    }

    public String getKycDesc() {
        return kycDesc;
    }

    public void setKycDesc(String kycDesc) {
        this.kycDesc = kycDesc;
    }

    public String getKycName() {
        return kycName;
    }

    public void setKycName(String kycName) {
        this.kycName = kycName;
    }

    @Override
    public String toString() {
        return kycDesc;
    }

}
