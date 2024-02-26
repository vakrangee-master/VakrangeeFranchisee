package in.vakrangee.franchisee.sitelayout.model;

public class Village {

    private String villageId;
    private String villageName;

    public Village(String villageId, String villageName) {
        this.villageId = villageId;
        this.villageName = villageName;
    }

    public String getVillageId() {
        return villageId;
    }

    public void setVillageId(String villageId) {
        this.villageId = villageId;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    @Override
    public String toString() {
        return  villageName;
    }
}
