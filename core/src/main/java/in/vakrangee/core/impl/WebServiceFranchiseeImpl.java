package in.vakrangee.core.impl;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.HashMap;

import in.vakrangee.core.ifc.FrachiessIfc;
import in.vakrangee.core.utils.Constants;

/**
 * Created by Nileshd on 6/8/2016.
 */
public class WebServiceFranchiseeImpl implements FrachiessIfc {
    /**
     * This method is used to call webservice.
     *
     * @param parameters
     * @param methodName
     * @param urlName
     * @param soapAction
     * @return
     */
    SoapObject request;
    SoapSerializationEnvelope envelope;
    HttpTransportSE androidHttpTransport;
    SoapPrimitive response;

    @Override
    public String getAvailableBalance(HashMap<String, String> parameters, String methodName,
                                      String urlName, String soapAction) {
        String resTxt = "";
        try {
            request = new SoapObject(Constants.NAMESPACE, methodName);
            for (String key : parameters.keySet()) {
                request.addProperty(key, parameters.get(key));

            }
            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            androidHttpTransport = new HttpTransportSE(urlName);
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
            resTxt = "Service from CyberPlat unavailable. Please try again later.";
        }
        return resTxt;
    }

    /**
     * This method is used to call updateMPin .
     *
     * @param parameters
     * @param methodName
     * @param urlName
     * @param soapAction
     * @return
     */
    @Override
    public String updateMPin(HashMap<String, String> parameters, String methodName,
                             String urlName, String soapAction) {
        String resTxt = "";
        try {
            request = new SoapObject(Constants.NAMESPACE, methodName);
            for (String key : parameters.keySet()) {
                request.addProperty(key, parameters.get(key));

            }
            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            androidHttpTransport = new HttpTransportSE(urlName);
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
            resTxt = "Service from CyberPlat unavailable. Please try again later.";
        }
        return resTxt;
    }

    @Override
    public String getAccountStatement(HashMap<String, String> parameters, String methodNamePaybill,
                                      String url, String soapAction) {
        String resTxt = "";
        try {
            request = new SoapObject(Constants.NAMESPACE, methodNamePaybill);
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
            resTxt = "Service from CyberPlat unavailable. Please try again later.";
        }
        return resTxt;
    }

    @Override
    public String getRechargeHistroyReport(HashMap<String, String> parameters, String methodNamePaybill,
                                           String url, String soapAction) {
        String resTxt = "";
        try {
            request = new SoapObject(Constants.NAMESPACE, methodNamePaybill);
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
            resTxt = "Service from CyberPlat unavailable. Please try again later.";
        }
        return resTxt;
    }


    @Override
    public String getAccountStatement10(HashMap<String, String> parameters, String methodNamePaybill,
                                        String url, String soapAction) {
        String resTxt = "";
        try {
            request = new SoapObject(Constants.NAMESPACE, methodNamePaybill);
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
            resTxt = "Service from CyberPlat unavailable. Please try again later.";
        }
        return resTxt;
    }


    @Override
    public String getMyHelpLine(HashMap<String, String> parameters, String methodNamePaybill,
                                String url, String soapAction) {
        String resTxt = "";
        try {
            request = new SoapObject(Constants.NAMESPACE, methodNamePaybill);
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
            resTxt = "Service from CyberPlat unavailable. Please try again later.";
        }
        return resTxt;
    }

    @Override
    public String resetMPin(HashMap<String, String> parameters, String methodNamePaybill,
                            String url, String soapAction) {
        String resTxt = "";
        try {
            request = new SoapObject(Constants.NAMESPACE, methodNamePaybill);
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
            resTxt = "Service from CyberPlat unavailable. Please try again later.";
        }
        return resTxt;
    }


    @Override
    public String getMyTransactions(HashMap<String, String> parameters, String methodNamePaybill,
                                    String url, String soapAction) {
        String resTxt = "";
        try {
            request = new SoapObject(Constants.NAMESPACE, methodNamePaybill);
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
            resTxt = "Service from CyberPlat unavailable. Please try again later.";
        }
        return resTxt;
    }

    @Override
    public String insertFFLocationLog(HashMap<String, String> parameters, String methodNamePaybill,
                                      String url, String soapAction) {
        String resTxt = "";
        try {
            request = new SoapObject(Constants.NAMESPACE, methodNamePaybill);
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
            resTxt = "Service from CyberPlat unavailable. Please try again later.";
        }
        return resTxt;
    }


    @Override
    public String authenticateFranchiseeOnUpgrade(HashMap<String, String> parameters,
                                                  String authenticateFranchiseeOnUpgrade,
                                                  String url, String soapAction) {
        String resTxt = "";
        try {
            request = new SoapObject(Constants.NAMESPACE, authenticateFranchiseeOnUpgrade);
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
            resTxt = "Service from CyberPlat unavailable. Please try again later.";
        }
        return resTxt;
    }


}
