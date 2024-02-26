package in.vakrangee.core.impl;

import android.database.Cursor;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import in.vakrangee.core.ifc.GeoCordinatesIfc;
import in.vakrangee.core.model.GeoCordinates;
import in.vakrangee.core.utils.Connection;

/**
 * Created by nileshd on 6/21/2016.
 */
public class GeoCordinatesImpl  implements GeoCordinatesIfc {

    @Override
    public List<GeoCordinates> getServiceProvider(String lat, String Long) {
      String SQLite = "select Date_Time,Latitude,Longitude,Status  from LocationTrackingMaster";
       String sqlQuery = "select Date_Time,Latitude,Longitude,Status  from LocationTrackingMaster where Latitude=" + lat + " " + "and Longitude =" + Long + " order by Date_Time ";
        Cursor cursor = null;
        Log.d("", SQLite);

        List<GeoCordinates> serviceList = new LinkedList<GeoCordinates>();
        try {

            cursor = Connection.VKMSDatabase.rawQuery(SQLite, null);
            if (cursor != null && cursor.getCount() != 0) {
                while (cursor.moveToNext()) {

                    serviceList.add(new GeoCordinates(cursor.getString(1),cursor.getString(2)));
                }
            }
        } catch (Exception e) {
            return serviceList;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return serviceList;
    }


}
