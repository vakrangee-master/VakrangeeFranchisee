package in.vakrangee.franchisee.fragment;

public class DashboardMenuModel {
    public String dashboarddname;
    public String dashborddrawable;


    public DashboardMenuModel(String t, String d) {
        dashboarddname = t;
        dashborddrawable = d;

    }


    public String getDashboarddname() {
        return dashboarddname;
    }

    public void setDashboarddname(String dashboarddname) {
        this.dashboarddname = dashboarddname;
    }

    public String getDashborddrawable() {
        return dashborddrawable;
    }

    public void setDashborddrawable(String dashborddrawable) {
        this.dashborddrawable = dashborddrawable;
    }


}
