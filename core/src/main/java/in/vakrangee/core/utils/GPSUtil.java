package in.vakrangee.core.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.util.List;
import java.util.Locale;

public class GPSUtil {
    private static StringBuilder sb = new StringBuilder(20);

    /**
     * returns ref for latitude which is S or N.
     *
     * @param latitude
     * @return S or N
     */
    public static String latitudeRef(double latitude) {
        return latitude < 0.0d ? "S" : "N";
    }

    /**
     * returns ref for latitude which is S or N.
     *
     * @param longitude
     * @return S or N
     */
    public static String longitudeRef(double longitude) {
        return longitude < 0.0d ? "W" : "E";
    }

    /**
     * convert latitude into DMS (degree minute second) format. For instance<br/>
     * -79.948862 becomes<br/>
     * 79/1,56/1,55903/1000<br/>
     * It works for latitude and longitude<br/>
     *
     * @param latitude could be longitude.
     * @return
     */
    synchronized public static final String convert(double latitude) {
        latitude = Math.abs(latitude);
        int degree = (int) latitude;
        latitude *= 60;
        latitude -= (degree * 60.0d);
        int minute = (int) latitude;
        latitude *= 60;
        latitude -= (minute * 60.0d);
        int second = (int) (latitude * 1000.0d);

        sb.setLength(0);
        sb.append(degree);
        sb.append("/1,");
        sb.append(minute);
        sb.append("/1,");
        sb.append(second);
        sb.append("/1000,");
        return sb.toString();
    }

    //region Decode Lat, Long to Get Address
    public static Address getAddress(Context context, double LATITUDE, double LONGITUDE) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                return  returnedAddress;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @SuppressLint("LongLogTag")
    public static String getCompleteAddressString(Context context, double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                strReturnedAddress.append(returnedAddress.getSubLocality()+", "+returnedAddress.getLocality()).append("\n");
                strReturnedAddress.append(returnedAddress.getAdminArea()+" "+returnedAddress.getPostalCode()+", "+returnedAddress.getCountryName()).append("\n");

               /* for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }*/
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction address", strReturnedAddress.toString());
            } else {
                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }

    //endregion
}