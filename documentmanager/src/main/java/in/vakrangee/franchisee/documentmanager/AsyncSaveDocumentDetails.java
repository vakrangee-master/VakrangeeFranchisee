package in.vakrangee.franchisee.documentmanager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import in.vakrangee.franchisee.documentmanager.address.AddressDetailsDto;
import in.vakrangee.franchisee.documentmanager.qualification.QualificationDetailsDto;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Logger;

@SuppressLint("LongLogTag")
public class AsyncSaveDocumentDetails extends AsyncTask<String, Void, String> {

    private final static String TAG = "AsyncSaveDocumentDetails";
    private Context mContext;
    private Logger logger;
    private ProgressDialog progress;
    private DocumentManagerRepository documentManagerRepo;
    private AsyncSaveDocumentDetails.Callback callback;
    private String type;
    private Object object;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncSaveDocumentDetails(Context context, String type,Object obj,AsyncSaveDocumentDetails.Callback icallback) {
        super();
        this.mContext = context;
        logger = Logger.getInstance(context);
        documentManagerRepo = new DocumentManagerRepository(context);
        this.type = type;
        this.object = obj;
        this.callback = icallback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.e("TAG", "onPreExecute");
        progress = new ProgressDialog(mContext);
        //progress.setTitle(R.string.pleaseWait);
        progress.setMessage(mContext.getResources().getString(R.string.pleaseWait));
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            Connection connection = new Connection(mContext);
            String tmpVkId = connection.getVkid();
            String enquiryId = CommonUtils.getEnquiryId(mContext);
            String vkIdOrEnquiryId = enquiryId;
            if (TextUtils.isEmpty(vkIdOrEnquiryId)) {
                vkIdOrEnquiryId = tmpVkId;
            }

            //JSON Data
            String jsonData = null;
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();

            if (type.equalsIgnoreCase(DocumentManagerActivity.TYPE_GST)) {
                GSTDetailsDto gstDetailsDto = ((GSTDetailsDto) object);
                jsonData = gson.toJson(gstDetailsDto, GSTDetailsDto.class);

            } else if (type.equalsIgnoreCase(DocumentManagerActivity.TYPE_PAN_CARD)) {
                PANCardDetailsDto panCardDetailsDto = ((PANCardDetailsDto) object);
                jsonData = gson.toJson(panCardDetailsDto, PANCardDetailsDto.class);

            } else if (type.equalsIgnoreCase(DocumentManagerActivity.TYPE_AGREEMENT_UPLOAD)) {
                AgreementUploadDetailsDto agreementUploadDetailsDto = ((AgreementUploadDetailsDto) object);
                jsonData = gson.toJson(agreementUploadDetailsDto, AgreementUploadDetailsDto.class);

            } else if (type.equalsIgnoreCase(DocumentManagerActivity.TYPE_IIBF_CERFITIFCATION)) {
                IIBFCertificationDto iibfCertificationDto = ((IIBFCertificationDto) object);
                jsonData = gson.toJson(iibfCertificationDto, IIBFCertificationDto.class);

            } else if (type.equalsIgnoreCase(DocumentManagerActivity.TYPE_MSME_CERITIFATE)) {
                MSMECertificateDto msmeCertificateDto = ((MSMECertificateDto) object);
                jsonData = gson.toJson(msmeCertificateDto, MSMECertificateDto.class);

            } else if (type.equalsIgnoreCase(DocumentManagerActivity.TYPE_SHOP_EST_CERFITIFCATION)) {
                ShopEstablishmentCertificateDto shopEstablishmentCertificateDto = ((ShopEstablishmentCertificateDto) object);
                jsonData = gson.toJson(shopEstablishmentCertificateDto, ShopEstablishmentCertificateDto.class);

            } else if (type.equalsIgnoreCase(DocumentManagerActivity.TYPE_QUALIFICATION)) {
                QualificationDetailsDto qualificationDetailsDto = ((QualificationDetailsDto) object);
                jsonData = gson.toJson(qualificationDetailsDto, QualificationDetailsDto.class);

            }  else if (type.equalsIgnoreCase(DocumentManagerActivity.TYPE_ADDRESS_PROOF)) {
                AddressDetailsDto addressDetailsDto = ((AddressDetailsDto) object);
                jsonData = gson.toJson(addressDetailsDto, AddressDetailsDto.class);
            }

            String response = documentManagerRepo.saveDocumentDetail(vkIdOrEnquiryId, type, jsonData);

            return response;

        } catch (Exception e) {
            Log.e("TAG", "Exception: " + e.getMessage());
            logger.writeError(TAG, "Exception: " + e.toString());
        }

        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        progress.dismiss();     // Hide Progress Bar

        if (!TextUtils.isEmpty(response)) {
            if (response.startsWith("OKAY"))
                Log.d(TAG, "Document Details Updated successfully. " + response);
            else
                Log.e(TAG, "Failed to Update Document Details . " + response);

            if (callback != null)
                callback.onResult(response);  // Send Response Back To Caller.
        } else {
            if (callback != null)
                callback.onResult(null);    // Exception Occurred or Some Issue Occurred.
        }
    }
}
