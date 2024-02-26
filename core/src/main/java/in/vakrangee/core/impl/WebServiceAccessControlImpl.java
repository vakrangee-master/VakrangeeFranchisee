package in.vakrangee.core.impl;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.HashMap;

import in.vakrangee.core.ifc.WebServiceIfc;
import in.vakrangee.core.utils.Constants;

/**
 * Created by Nileshd on 6/8/2016.
 */
public class WebServiceAccessControlImpl implements WebServiceIfc {

    /**
     * This method is used to call registerFranchisee.
     *
     * @param parameters
     * @param methodName
     * @param urlName
     * @param soapAction
     * @return
     */
    @Override
    public String registerFranchisee(HashMap<String, String> parameters, String methodName, String urlName, String soapAction) {
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
            resTxt = "Service from VKMS unavailable. Please try again later.";

        }
        return resTxt;
    }

    /**
     * This method is used to call verifyOtp.
     *
     * @param parameters
     * @param methodName
     * @param urlName
     * @param soapAction
     * @return
     */
    @Override
    public String verifyOtp(HashMap<String, String> parameters, String methodName, String urlName, String soapAction) {
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
            resTxt = "Service from CyberPlat unavailable. Please try again later.";
        }
        return resTxt;
    }

    /**
     * This method is used to call authenticateFranchiseeWithToken.
     *
     * @param parameters
     * @param methodName
     * @param urlName
     * @param soapAction
     * @return
     */
    @Override
    public String authenticateFranchiseeWithToken(HashMap<String, String> parameters, String methodName, String urlName, String soapAction) {
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
            resTxt = "Service from CyberPlat unavailable. Please try again later.";
        }
        return resTxt;
    }

    /**
     * This method is used to call logOutFranchisee.
     *
     * @param parameters
     * @param methodName
     * @param urlName
     * @param soapAction
     * @return
     */
    @Override
    public String logOutFranchisee(HashMap<String, String> parameters, String methodName, String urlName, String soapAction) {
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
            resTxt = "Service from CyberPlat unavailable. Please try again later.";
        }
        return resTxt;
    }

    /**
     * This method is used to call authenticateFranchisee.
     *
     * @param parameters
     * @param methodName
     * @param urlName
     * @param soapAction
     * @return
     */
    @Override
    public String authenticateFranchisee(HashMap<String, String> parameters, String methodName, String urlName, String soapAction) {
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
            resTxt = "Service from CyberPlat unavailable. Please try again later.";
        }
        return resTxt;
    }

    @Override
    public String resendOtp(HashMap<String, String> parameters, String methodNameResendOtp, String urlVkmsAccessControl, String soapAction) {
        String resTxt = "";
        try {
            SoapObject request = new SoapObject(Constants.NAMESPACE, methodNameResendOtp);
            for (String key : parameters.keySet()) {
                request.addProperty(key, parameters.get(key));

            }
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(urlVkmsAccessControl);
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
            resTxt = "Service from CyberPlat unavailable. Please try again later.";
        }
        return resTxt;
    }

    @Override
    public String checkversion(HashMap<String, String> parameters, String methodNameResendOtp, String urlVkmsAccessControl, String soapAction) {
        String resTxt = "";
        try {
            SoapObject request = new SoapObject(Constants.NAMESPACE, methodNameResendOtp);
            for (String key : parameters.keySet()) {
                request.addProperty(key, parameters.get(key));

            }
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(urlVkmsAccessControl);
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
            Log.e("Error ", "Checkversion");
            resTxt = "Service from CyberPlat unavailable. Please try again later.";
        }
        return resTxt;
    }

    //region Update FCM Id
    public String updateFCMId(HashMap<String, String> parameters, String methodNameUpdateFCMId, String urlVkmsAccessControl, String soapAction) {
        return sendRequest("updateFCMId", parameters, methodNameUpdateFCMId, urlVkmsAccessControl, soapAction);
    }

    @Override
    public String authenticateUser(HashMap<String, String> parameters, String methodName, String urlName, String soapAction) {
        return sendRequest("authenticateUser", parameters, methodName, urlName, soapAction);
    }
    //endregion

    //region Common method to send request to Server
    private String sendRequest(String tag, HashMap<String, String> parameters, String methodName, String url, String soapAction) {
        String resTxt = "";
        try {
            SoapObject request = new SoapObject(Constants.NAMESPACE, methodName);
            for (String key : parameters.keySet()) {
                request.addProperty(key, parameters.get(key));
            }

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(url);
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
            resTxt = "Service from VKMS unavailable. Please try again later.";
        }
        return resTxt;
    }
    //endregion
}
