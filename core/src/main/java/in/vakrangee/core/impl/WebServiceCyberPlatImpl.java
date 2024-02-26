package in.vakrangee.core.impl;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.HashMap;

import in.vakrangee.core.ifc.CyberPlatIfc;
import in.vakrangee.core.utils.Constants;

/**
 * Created by Nileshd on 6/8/2016.
 */
public class WebServiceCyberPlatImpl implements CyberPlatIfc {
    /**
     * This method is used to call WebServiceCyberPlatImpl.
     *
     * @param parameters
     * @param methodName
     * @param urlName
     * @param soapAction
     * @return
     */
    @Override
    public String callWebService(HashMap<String, String> parameters, String methodName, String urlName, String soapAction) {
        String resTxt = "";
        try {
            SoapObject request = new SoapObject(Constants.NAMESPACE, methodName);
            for (String key : parameters.keySet()) {
                request.addProperty(key, parameters.get(key));

            }
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(urlName);
            androidHttpTransport.call(soapAction, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            resTxt = response.toString();
            if (resTxt == null) {
                Log.d(Constants.TAG, "Response data");
            } else {
                Log.d(Constants.TAG, "Response data" + resTxt);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            Log.i("image load error", e.getMessage());

            resTxt ="Service from CyberPlat unavailable. Please try again later.";


        }
        return resTxt;
    }
}
