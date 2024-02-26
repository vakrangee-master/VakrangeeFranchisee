package in.vakrangee.core.model;

/**
 * Created by nileshd on 6/21/2016.
 */
public class GeoCordinates {
    private String Latitude;
    private String Longitude;

    public GeoCordinates(String Latitude,String Longitude)
    {
        this.Latitude=Latitude;
        this.Longitude=Longitude;
    }
    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }
}
