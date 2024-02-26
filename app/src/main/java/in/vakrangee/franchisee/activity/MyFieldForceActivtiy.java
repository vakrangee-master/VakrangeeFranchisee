package in.vakrangee.franchisee.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.StringTokenizer;

import javax.net.ssl.HttpsURLConnection;

import in.vakrangee.franchisee.R;

public class MyFieldForceActivtiy extends AppCompatActivity {
    private GoogleMap map;
    SupportMapFragment mapFragment;
    JSONArray Cargo = null;
    String vkid, vkname, status;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_field_force_activtiy);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);




        setUpMapIfNeeded();
        GetMarkers markers = new GetMarkers();

        markers.execute();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.MyFieldForce);

//            final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
//            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//            getSupportActionBar().setHomeAsUpIndicator(upArrow);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                View view = getLayoutInflater().inflate(R.layout.popfieldforce, null);
                TextView locationView = (TextView) view.findViewById(R.id.name);
                TextView addressView = (TextView) view.findViewById(R.id.address);
                TextView report_time = (TextView) view.findViewById(R.id.report_time);
                view.setBackgroundColor(Color.WHITE);


//                String formatLocator = "<b>VKID :</b> " + vkid;
//
//                StringTokenizer tokens = new StringTokenizer(vkname, "|");
//                String first = tokens.nextToken();
//                String second = tokens.nextToken();
//
//                String formatAddress = "<b>NAME:</b> " + first + " <br/> <b>Time:</b> " + second;

                //Likewise you can introduce some HTML formatting to the contentOpen
                locationView.setText(marker.getTitle());
                locationView.setTextColor(getResources().getColor(R.color.primary));

                addressView.setText(marker.getSnippet());
                addressView.setTextColor(getResources().getColor(R.color.primary));


                return view;

            }
        });
    }


    private void setUpMapIfNeeded() {
        if (map == null) {

//            map = mapFragment.getMap();
            if (map != null) {
                //setUpMap();
                try {
                    GetMarkers markers = new GetMarkers();

                    markers.execute();
                } catch (Exception e) {
                    // error, do something
                }
            }
        }
    }

    // Open previous opened link from history on webview when back button pressed

    @Override

    public void onBackPressed() {


        // Let the system handle the back button
        Intent intent = new Intent(MyFieldForceActivtiy.this, MyVakrangeeKendra.class);
        startActivity(intent);
        //super.onBackPressed();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent = new Intent(MyFieldForceActivtiy.this, MyVakrangeeKendra.class);
                startActivity(intent);

                // Toast.makeText(getApplicationContext(),"Back button clicked", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }


    class GetMarkers extends AsyncTask<Void, Void, JSONArray> {


        JSONArray jsonarray = null;

        @Override
        protected JSONArray doInBackground(Void... params) {

            try {
                URL url = new URL("https://vkms.vakrangee.in/GetGeoCoordinatesFFDashboard");
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);


                InputStream in = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                StringBuilder builder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                reader.close();
                String response = builder.toString();

                jsonarray = new JSONArray(response);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return jsonarray;
        }


        @Override
        protected void onPostExecute(JSONArray jsonarray) {

            try {

                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject obj = jsonarray.getJSONObject(i);

                    String vkid = obj.getString("vkId");
                    String vkname = obj.getString("vkName");
                    String status = obj.getString("vkId");

                    Double LAT = obj.getDouble("latitude");
                    Double LNG = obj.getDouble("longitude");
                    LatLng position = new LatLng(LAT, LNG);


                    if (i == 0) {
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(position).zoom(9).build();

                        map.animateCamera(CameraUpdateFactory
                                .newCameraPosition(cameraPosition));
                    }
                    String formatLocator = "<b>VKID :</b> " + vkid;

                    StringTokenizer tokens = new StringTokenizer(vkname, "|");
                    String first = tokens.nextToken();
                    String second = tokens.nextToken();

                    if (status.equals("green")) {
                        map.addMarker(new MarkerOptions().position(position).title("VKID: "+vkid).snippet("Name: "+first +"\n Time: "+second).icon(BitmapDescriptorFactory.fromResource(R.drawable.green)));
                    } else if (status.equals("dark_green")) {

                        map.addMarker(new MarkerOptions().position(position).title("VKID: "+vkid).snippet("Name: "+first +"\n Time: "+second).icon(BitmapDescriptorFactory.fromResource(R.drawable.dark_green)));

                    } else if (status.equals("orange")) {

                        map.addMarker(new MarkerOptions().position(position).title("VKID: "+vkid).snippet("Name: "+first +"\n Time: "+second).icon(BitmapDescriptorFactory.fromResource(R.drawable.orange)));

                    } else {

                        map.addMarker(new MarkerOptions().position(position).title("VKID: "+vkid).snippet("Name: "+first +"\n Time: "+second).icon(BitmapDescriptorFactory.
                                fromResource(R.drawable.red)));

                    }



                    // map.addMarker(new MarkerOptions().position(position).title(title).icon(BitmapDescriptorFactory.fromResource(R.drawable.add_marker)));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
