package in.vakrangee.core.impl;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.HashMap;

import in.vakrangee.core.ifc.WSAttendance;
import in.vakrangee.core.utils.Constants;

/**
 * Created by Nileshd on 6/2/2017.
 */

public class WebServiceWSAttendance1Impl implements WSAttendance {

    SoapObject request;
    SoapSerializationEnvelope envelope;
    HttpTransportSE androidHttpTransport;
    SoapPrimitive response;


    @Override
    public String getNearByVakrangeeKendra(HashMap<String, String> parameters, String getNearByVakrangeeKendra, String url, String soapAction,String NameSpace) {
        String resTxt = "";
        try {
            request = new SoapObject(NameSpace, getNearByVakrangeeKendra);
            for (String key : parameters.keySet()) {
                request.addProperty(key, parameters.get(key));

            }
            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            androidHttpTransport = new HttpTransportSE(url);
            androidHttpTransport.call(soapAction, envelope);
            response = (SoapPrimitive) envelope.getResponse();
            resTxt = response.toString();
            if (resTxt == null) {
                Log.d(Constants.TAG, "Response data");
            } else {
                Log.d(Constants.TAG, "Response data" + resTxt);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("image load error", e.getMessage());
            resTxt = "Service from Vakrangee Kendra unavailable. Please try again later.";
        }
        return resTxt;
    }

}
