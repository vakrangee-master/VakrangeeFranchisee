package in.vakrangee.core.impl;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.HashMap;

import in.vakrangee.core.ifc.MyVakrangeeKendraIfc;
import in.vakrangee.core.utils.Constants;
import in.vakrangee.core.utils.WebServiceUtils;

/**
 * Created by nileshd on 12/15/2016.
 */

public class WebServiceMyVakrangeeKendraImpl implements MyVakrangeeKendraIfc {

    private static final String TAG = "WebServiceMyVakrangeeKendraImpl";

    SoapObject request;
    SoapSerializationEnvelope envelope;
    HttpTransportSE androidHttpTransport;
    SoapPrimitive response;

    @Override
    public String MyVakrangeeKendraimpl(HashMap<String, String> parameters, String myVakrangeeKendra, String url, String soapAction) {
        String resTxt = "";
        try {
            request = new SoapObject(Constants.NAMESPACE, myVakrangeeKendra);
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
    public String myVakrangeeKendraTimingsResponse(HashMap<String, String> parameters, String myVakrangeeKendra, String url, String soapAction) {
        String resTxt = "";
        try {
            request = new SoapObject(Constants.NAMESPACE, myVakrangeeKendra);
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
    public String myVakrangeeKendraLocationDetails(HashMap<String, String> parameters, String myVakrangeeKendra, String url, String soapAction) {
        String resTxt = "";
        try {
            request = new SoapObject(Constants.NAMESPACE, myVakrangeeKendra);
            for (String key : parameters.keySet()) {
                request.addProperty(key, parameters.get(key));

            }
            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            androidHttpTransport = new HttpTransportSE(url);
            androidHttpTransport.call(soapAction, envelope);
            envelope.dotNet = true;
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
    public String myVakrangeeKendraParticular(HashMap<String, String> parameters, String myVakrangeeKendra, String url, String soapAction) {
        String resTxt = "";
        try {
            request = new SoapObject(Constants.NAMESPACE, myVakrangeeKendra);
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
    public String nextgenScheduleDateTimeUpdateResponse(HashMap<String, String> parameters, String method, String url, String soapAction) {
        String resTxt = "";
        try {
            request = new SoapObject(Constants.NAMESPACE, method);
            for (String key : parameters.keySet()) {
                request.addProperty(key, parameters.get(key));
            }
            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            androidHttpTransport = new HttpTransportSE(url);
            androidHttpTransport.call(soapAction, envelope);
            //androidHttpTransport.debug = true;                   // For DEBUG Purpose
            response = (SoapPrimitive) envelope.getResponse();
            resTxt = response.toString();
            if (resTxt == null) {
                Log.d(Constants.TAG, "Response data");
            } else {
                Log.d(Constants.TAG, "Response data" + resTxt);
            }

            /*Log.e(TAG, "SOAP Request: ========================================================");
            Log.e(TAG, androidHttpTransport.requestDump);
            Log.e(TAG, "SOAP Response: =======================================================");
            Log.e(TAG, androidHttpTransport.responseDump);
            Log.e(TAG, "======================================================================");*/

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("updating schedule datetime/call error", e.getMessage());
            resTxt = "Service from Vakrangee Kendra unavailable. Please try again later.";
        }
        return resTxt;
    }

    //region NextGen Site Work Commencement
    @Override
    public String getNextGenSiteWorkCommencementDetail(HashMap<String, String> parameters, String methodName, String url, String soapAction) {
        return WebServiceUtils.getInstance().sendRequest(TAG, Constants.NAMESPACE, parameters, methodName, url, soapAction);
    }

    @Override
    public String nextgenSiteWorkCommencementUpdate(HashMap<String, String> parameters, String methodName, String url, String soapAction) {
        return WebServiceUtils.getInstance().sendRequest(TAG, Constants.NAMESPACE, parameters, methodName, url, soapAction);
    }
//endregion

    //region NextGen Site Work In Progress
    @Override
    public String getAllNextGenSiteWorkInProgressDetail(HashMap<String, String> parameters, String methodName, String url, String soapAction) {
        return WebServiceUtils.getInstance().sendRequest(TAG, Constants.NAMESPACE, parameters, methodName, url, soapAction);
    }

    @Override
    public String getNextGenSiteWorkInProgressDetail(HashMap<String, String> parameters, String methodName, String url, String soapAction) {
        return WebServiceUtils.getInstance().sendRequest(TAG, Constants.NAMESPACE, parameters, methodName, url, soapAction);
    }


    @Override
    public String nextgenSiteWorkInProgressUpdate(HashMap<String, String> parameters, String methodName, String url, String soapAction) {
        return WebServiceUtils.getInstance().sendRequest(TAG, Constants.NAMESPACE, parameters, methodName, url, soapAction);
    }
    //endregion

    //region NextGen Site Work Completion Intimation

    @Override
    public String getNextGenSiteWorkCompletionDetail1(HashMap<String, String> parameters, String methodName, String url, String soapAction) {
        return WebServiceUtils.getInstance().sendRequest(TAG, Constants.NAMESPACE, parameters, methodName, url, soapAction);
    }
    @Override
    public String getWorkCompletionIntimationDetail(HashMap<String, String> parameters, String methodName, String url, String soapAction) {
        return WebServiceUtils.getInstance().sendRequest(TAG+"- getWorkCompletionIntimationDetail: ", Constants.NAMESPACE, parameters, methodName, url, soapAction);
    }

    @Override
    public String getWorkCompletionCheckList(HashMap<String, String> parameters, String methodName, String url, String soapAction) {
        return WebServiceUtils.getInstance().sendRequest(TAG+": getWorkCompletionCheckList", Constants.NAMESPACE, parameters, methodName, url, soapAction);
    }

    //endregion

    //region NextGen Site Readiness and Verification
    @Override
    public String getSiteReadinessAndVerificationDetail(HashMap<String, String> parameters, String methodName, String url, String soapAction) {
        return WebServiceUtils.getInstance().sendRequest(TAG+"- getSiteReadinessAndVerificationDetail: ", Constants.NAMESPACE, parameters, methodName, url, soapAction);
    }

    @Override
    public String getReadinessAndVerificationCheckList(HashMap<String, String> parameters, String methodName, String url, String soapAction) {
        return WebServiceUtils.getInstance().sendRequest(TAG+": getReadinessAndVerificationCheckList", Constants.NAMESPACE, parameters, methodName, url, soapAction);
    }

    @Override
    public String nextgenSiteReadinessAndVerificationUpdate1(HashMap<String, String> parameters, String methodName, String url, String soapAction) {
        return WebServiceUtils.getInstance().sendRequest(TAG+": nextgenSiteReadinessAndVerificationUpdate1", Constants.NAMESPACE, parameters, methodName, url, soapAction);
    }

    @Override
    public String getElemenAttributetDetail(HashMap<String, String> parameters, String methodName, String url, String soapAction) {
        return WebServiceUtils.getInstance().sendRequest(TAG+": getElemenAttributetDetail", Constants.NAMESPACE, parameters, methodName, url, soapAction);
    }

    @Override
    public String validateLatLongWithAddress(HashMap<String, String> parameters, String methodName, String url, String soapAction) {
        return WebServiceUtils.getInstance().sendRequest(TAG+": validateLatLongWithAddress", Constants.NAMESPACE, parameters, methodName, url, soapAction);
    }

    @Override
    public String updateUserLatLong(HashMap<String, String> parameters, String methodName, String url, String soapAction) {
        return WebServiceUtils.getInstance().sendRequest(TAG+": updateUserLatLong", Constants.NAMESPACE, parameters, methodName, url, soapAction);
    }
    //endregion

}
