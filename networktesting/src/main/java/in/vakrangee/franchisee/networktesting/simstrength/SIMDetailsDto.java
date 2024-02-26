package in.vakrangee.franchisee.networktesting.simstrength;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class SIMDetailsDto implements Serializable {

    @SerializedName("sim_slot_index")
    private int simSlotIndex;

    @SerializedName("subscriptionId")
    private int subscriptionId;

    @SerializedName("sim_slot")
    private String simSlotName;

    @SerializedName("signal_bar")
    private int signalBar;

    @SerializedName("line_number")
    private String lineNumber;

    @SerializedName("network")
    private String network;

    @SerializedName("mobile_network_state")
    private String mobNetworkState;

    @SerializedName("operator_info")
    private String operatorInfo;

    @SerializedName("service_state")
    private String serviceState;

    @SerializedName("signal_strength")
    private String signalStrength;

    @SerializedName("mobile_voice_network_type")
    private String mobVoiceNetworkType;

    @SerializedName("mobile_data_network_type")
    private String mobDataNetworkType;

    @SerializedName("roaming")
    private String roaming;

    @SerializedName("sim_signal_strength")
    private String simSignalStrength;

    private boolean IsExecutionStarted;

    public ArrayList<String> signalStrengthValue = new ArrayList<>();

    public boolean isExecutionStarted() {
        return IsExecutionStarted;
    }

    public void setExecutionStarted(boolean executionStarted) {
        IsExecutionStarted = executionStarted;
    }

    public ArrayList<String> getSignalStrengthValue() {
        return signalStrengthValue;
    }

    public void setSignalStrengthValue(ArrayList<String> signalStrengthValue) {
        this.signalStrengthValue = signalStrengthValue;
    }

    public int getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(int subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public int getSimSlotIndex() {
        return simSlotIndex;
    }

    public void setSimSlotIndex(int simSlotIndex) {
        this.simSlotIndex = simSlotIndex;
    }

    public int getSignalBar() {
        return signalBar;
    }

    public void setSignalBar(int signalBar) {
        this.signalBar = signalBar;
    }

    public String getSimSignalStrength() {
        return simSignalStrength;
    }

    public void setSimSignalStrength(String simSignalStrength) {
        this.simSignalStrength = simSignalStrength;
    }

    public String getSimSlotName() {
        return simSlotName;
    }

    public void setSimSlotName(String simSlotName) {
        this.simSlotName = simSlotName;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getMobNetworkState() {
        return mobNetworkState;
    }

    public void setMobNetworkState(String mobNetworkState) {
        this.mobNetworkState = mobNetworkState;
    }

    public String getOperatorInfo() {
        return operatorInfo;
    }

    public void setOperatorInfo(String operatorInfo) {
        this.operatorInfo = operatorInfo;
    }

    public String getServiceState() {
        return serviceState;
    }

    public void setServiceState(String serviceState) {
        this.serviceState = serviceState;
    }

    public String getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(String signalStrength) {
        this.signalStrength = signalStrength;
    }

    public String getMobVoiceNetworkType() {
        return mobVoiceNetworkType;
    }

    public void setMobVoiceNetworkType(String mobVoiceNetworkType) {
        this.mobVoiceNetworkType = mobVoiceNetworkType;
    }

    public String getMobDataNetworkType() {
        return mobDataNetworkType;
    }

    public void setMobDataNetworkType(String mobDataNetworkType) {
        this.mobDataNetworkType = mobDataNetworkType;
    }

    public String getRoaming() {
        return roaming;
    }

    public void setRoaming(String roaming) {
        this.roaming = roaming;
    }
}
