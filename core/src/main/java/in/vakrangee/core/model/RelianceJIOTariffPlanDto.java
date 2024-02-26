package in.vakrangee.core.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RelianceJIOTariffPlanDto implements Serializable{

    @SerializedName("reliance_jio_tariff_plan_id")
    private String reliance_jio_tariff_plan_id;
    @SerializedName("reliance_jio_tariff_plan_description")
    private String reliance_jio_tariff_plan_description;
    @SerializedName("reliance_jio_tariff_plan_name")
    private String reliance_jio_tariff_plan_name;
    @SerializedName("reliance_jio_tariff_plan_amount")
    private int reliance_jio_tariff_plan_amount;

    public RelianceJIOTariffPlanDto(String reliance_jio_tariff_plan_id, String reliance_jio_tariff_plan_description,
                                    String reliance_jio_tariff_plan_name, int reliance_jio_tariff_plan_amount) {

        this.reliance_jio_tariff_plan_id = reliance_jio_tariff_plan_id;
        this.reliance_jio_tariff_plan_description = reliance_jio_tariff_plan_description;
        this.reliance_jio_tariff_plan_name = reliance_jio_tariff_plan_name;
        this.reliance_jio_tariff_plan_amount = reliance_jio_tariff_plan_amount;
    }


    public String getReliance_jio_tariff_plan_id() {
        return reliance_jio_tariff_plan_id;
    }

    public void setReliance_jio_tariff_plan_id(String reliance_jio_tariff_plan_id) {
        this.reliance_jio_tariff_plan_id = reliance_jio_tariff_plan_id;
    }

    public String getReliance_jio_tariff_plan_description() {
        return reliance_jio_tariff_plan_description;
    }

    public void setReliance_jio_tariff_plan_description(String reliance_jio_tariff_plan_description) {
        this.reliance_jio_tariff_plan_description = reliance_jio_tariff_plan_description;
    }

    public String getReliance_jio_tariff_plan_name() {
        return reliance_jio_tariff_plan_name;
    }

    public void setReliance_jio_tariff_plan_name(String reliance_jio_tariff_plan_name) {
        this.reliance_jio_tariff_plan_name = reliance_jio_tariff_plan_name;
    }

    public int getReliance_jio_tariff_plan_amount() {
        return reliance_jio_tariff_plan_amount;
    }

    public void setReliance_jio_tariff_plan_amount(int reliance_jio_tariff_plan_amount) {
        this.reliance_jio_tariff_plan_amount = reliance_jio_tariff_plan_amount;
    }
}
