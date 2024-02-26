package in.vakrangee.core.impl;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.HashMap;

import in.vakrangee.core.ifc.AddLeaveODIfc;
import in.vakrangee.core.utils.Constants;

/**
 * Created by Nileshd on 1/6/2017.
 */

public class WebServiceAddLeaveODImpl implements AddLeaveODIfc {

    SoapObject request;
    SoapSerializationEnvelope envelope;
    HttpTransportSE androidHttpTransport;
    SoapPrimitive response;

    @Override
    public String addLeaveOD(HashMap<String, String> parameters, String myVakrangeeKendra, String url, String soapAction,String namespace) {
        String resTxt = "";
        try {
            request = new SoapObject(namespace, myVakrangeeKendra);
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

    @Override
    public String getMyLeaveODList(HashMap<String, String> parameters, String myVakrangeeKendra, String url, String soapAction,String namespace) {
        String resTxt = "";
        try {
            request = new SoapObject(namespace, myVakrangeeKendra);
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

    @Override
    public String updateStatusLeaveOD(HashMap<String, String> parameters, String myVakrangeeKendra, String url, String soapAction,String namespace) {
        String resTxt = "";
        try {
            request = new SoapObject(namespace, myVakrangeeKendra);
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
