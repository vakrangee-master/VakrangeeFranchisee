package in.vakrangee.core.ifc;

import java.util.List;

import in.vakrangee.core.model.GeoCordinates;

/**
 * Created by nileshd on 6/21/2016.
 */
public interface GeoCordinatesIfc {

    /**
     * This method is used to call curent Lat and Long  using Draw line in google map.
     * @param lat
     * @param Long
     * @return
     */
    public List<GeoCordinates> getServiceProvider(String lat, String Long);

}
