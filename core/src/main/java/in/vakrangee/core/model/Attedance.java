package in.vakrangee.core.model;

/**
 * Created by Nileshd on 5/30/2017.
 */
public class Attedance {
    public String vkid;
    public String name;
    public String lati;
    public String longi;
    public String near;


    public String atmCashRoId;

    public String getAtmCashRoId() {
        return atmCashRoId;
    }

    public void setAtmCashRoId(String atmCashRoId) {
        this.atmCashRoId = atmCashRoId;
    }

    public String getAtmCashRoName() {
        return atmCashRoName;
    }

    public void setAtmCashRoName(String atmCashRoName) {
        this.atmCashRoName = atmCashRoName;
    }

    public String atmCashRoName;
}
