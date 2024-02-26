package in.vakrangee.core.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import in.vakrangee.core.application.VakrangeeKendraApplication;

/**
 * Common WebService Utils to send request to server and get response.
 */
public class WebServiceUtils {

    private static final String TAG = WebServiceUtils.class.getCanonicalName();
    private static WebServiceUtils webServiceUtils;
    private static Context mContext;
    private static Logger logger;
    private String SEPERATOR = "=====================================================================";

    private WebServiceUtils() {
    }

    public static WebServiceUtils getInstance() {

        if (webServiceUtils == null) {
            webServiceUtils = new WebServiceUtils();
        }

        mContext = VakrangeeKendraApplication.getContext();
        logger = Logger.getInstance(mContext);

        return webServiceUtils;
    }

    //region Common method to send request to Server
    public String sendRequest(String tag, HashMap<String, String> parameters, String methodName, String url, String soapAction) {
        return sendRequest(tag, null, parameters, methodName, url, soapAction);
    }

    public String sendRequest(String tag, String namespace, HashMap<String, String> parameters, String methodName, String url, String soapAction) {
        //logger.writeDebug(TAG, SEPERATOR);
        logger.writeDebug(TAG, "Sending Request URL: " + url);
        String resTxt = "";
        try {
            SoapObject request;
            if (TextUtils.isEmpty(namespace))
                request = new SoapObject(Constants.NAMESPACE, methodName);
            else
                request = new SoapObject(namespace, methodName);

            for (String key : parameters.keySet()) {
                request.addProperty(key, parameters.get(key));
            }
            //logger.writeDebug(TAG, "Request Data: " + parameters.toString());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(url);
            androidHttpTransport.call(soapAction, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            resTxt = response.toString();

            //logger.writeDebug(TAG, "Response Data: " + resTxt);

            if (resTxt == null) {
                Log.d(Constants.TAG, "Response data");
            } else {
                Log.d(Constants.TAG, "Response data" + resTxt);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //resTxt = "Service from VKMS unavailable. Please try again later.";
        }
        //logger.writeDebug(TAG, SEPERATOR);
        return resTxt;
    }

    //endregion

    public static String ReadFromfile(String fileName, Context context) {
        StringBuilder returnString = new StringBuilder();
        InputStream fIn = null;
        InputStreamReader isr = null;
        BufferedReader input = null;
        try {
            fIn = context.getResources().getAssets().open(fileName, Context.MODE_WORLD_READABLE);
            isr = new InputStreamReader(fIn);
            input = new BufferedReader(isr);
            String line = "";
            while ((line = input.readLine()) != null) {
                returnString.append(line);
            }
        } catch (Exception e) {
            e.getMessage();
        } finally {
            try {
                if (isr != null)
                    isr.close();
                if (fIn != null)
                    fIn.close();
                if (input != null)
                    input.close();
            } catch (Exception e2) {
                e2.getMessage();
            }
        }
        return returnString.toString();
    }
}
