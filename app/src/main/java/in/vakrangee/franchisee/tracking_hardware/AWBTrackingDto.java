package in.vakrangee.franchisee.tracking_hardware;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AWBTrackingDto implements Serializable {

    @SerializedName("CN")
    private String cN;
    @SerializedName("CNDate")
    private String cNDate;
    @SerializedName("DeliveryDate")
    private String deliveryDate;
    @SerializedName("DeliveryPincode")
    private String deliveryPincode;
    @SerializedName("Destination")
    private String destination;
    @SerializedName("DocketTrackingStatus")
    private String docketTrackingStatus;
    @SerializedName("ExecutionMessage")
    private String executionMessage;
    @SerializedName("ExecutionStatus")
    private String executionStatus;
    @SerializedName("ExpectedDeliveryDate")
    private String expectedDeliveryDate;
    @SerializedName("InvoiceDetailList")
    private List<InvoiceDetailList> invoiceDetailList = null;
    @SerializedName("NoOfPackages")
    private String noOfPackages;
    @SerializedName("Origin")
    private String origin;
    @SerializedName("POD")
    private String pOD;
    @SerializedName("PickupPincode")
    private String pickupPincode;
    @SerializedName("Status")
    private String status;
    @SerializedName("StatusDate")
    private String statusDate;
    @SerializedName("StatusLocation")
    private String statusLocation;
    @SerializedName("StatusTime")
    private String statusTime;
    @SerializedName("TotalActualWight")
    private String totalActualWight;
    @SerializedName("TransportMode")
    private String transportMode;

    public String getCN() {
        return cN;
    }

    public void setCN(String cN) {
        this.cN = cN;
    }

    public String getCNDate() {
        return cNDate;
    }

    public void setCNDate(String cNDate) {
        this.cNDate = cNDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryPincode() {
        return deliveryPincode;
    }

    public void setDeliveryPincode(String deliveryPincode) {
        this.deliveryPincode = deliveryPincode;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDocketTrackingStatus() {
        return docketTrackingStatus;
    }

    public void setDocketTrackingStatus(String docketTrackingStatus) {
        this.docketTrackingStatus = docketTrackingStatus;
    }

    public String getExecutionMessage() {
        return executionMessage;
    }

    public void setExecutionMessage(String executionMessage) {
        this.executionMessage = executionMessage;
    }

    public String getExecutionStatus() {
        return executionStatus;
    }

    public void setExecutionStatus(String executionStatus) {
        this.executionStatus = executionStatus;
    }

    public String getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(String expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public List<InvoiceDetailList> getInvoiceDetailList() {
        return invoiceDetailList;
    }

    public void setInvoiceDetailList(List<InvoiceDetailList> invoiceDetailList) {
        this.invoiceDetailList = invoiceDetailList;
    }

    public String getNoOfPackages() {
        return noOfPackages;
    }

    public void setNoOfPackages(String noOfPackages) {
        this.noOfPackages = noOfPackages;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getPOD() {
        return pOD;
    }

    public void setPOD(String pOD) {
        this.pOD = pOD;
    }

    public String getPickupPincode() {
        return pickupPincode;
    }

    public void setPickupPincode(String pickupPincode) {
        this.pickupPincode = pickupPincode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(String statusDate) {
        this.statusDate = statusDate;
    }

    public String getStatusLocation() {
        return statusLocation;
    }

    public void setStatusLocation(String statusLocation) {
        this.statusLocation = statusLocation;
    }

    public String getStatusTime() {
        return statusTime;
    }

    public void setStatusTime(String statusTime) {
        this.statusTime = statusTime;
    }

    public String getTotalActualWight() {
        return totalActualWight;
    }

    public void setTotalActualWight(String totalActualWight) {
        this.totalActualWight = totalActualWight;
    }

    public String getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
    }

    public class InvoiceDetailList {

        @SerializedName("EWBNumber")
        private String eWBNumber;
        @SerializedName("InvoiceNo")
        private String invoiceNo;
        @SerializedName("InvoiceValues")
        private String invoiceValues;

        public String getEWBNumber() {
            return eWBNumber;
        }

        public void setEWBNumber(String eWBNumber) {
            this.eWBNumber = eWBNumber;
        }

        public String getInvoiceNo() {
            return invoiceNo;
        }

        public void setInvoiceNo(String invoiceNo) {
            this.invoiceNo = invoiceNo;
        }

        public String getInvoiceValues() {
            return invoiceValues;
        }

        public void setInvoiceValues(String invoiceValues) {
            this.invoiceValues = invoiceValues;
        }

    }

}
