package in.vakrangee.franchisee.sitelayout.model;

public class Bank {


    private String nbinCode;
    private String bankName;

    public Bank(String nbinCode, String bankName) {
        this.nbinCode = nbinCode;
        this.bankName = bankName;
    }

    public String getNbinCode() {
        return nbinCode;
    }

    public void setNbinCode(String nbinCode) {
        this.nbinCode = nbinCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Override
    public String toString() {
        return  bankName;
    }
}
