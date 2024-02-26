package in.vakrangee.franchisee.documentmanager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import in.vakrangee.franchisee.documentmanager.address.AddressDetailsFragment;
import in.vakrangee.franchisee.documentmanager.qualification.QualificationDetailsFragment;
import in.vakrangee.supercore.franchisee.nextgenfranchiseeapplication.CustomFranchiseeApplicationSpinnerDto;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.utils.CustomSearchableSpinner;

public class DocumentManagerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private static final String TAG = "DocumentManagerActivity";

    private Toolbar toolbar;

    private CustomSearchableSpinner spinnerDocumentType;
    private LinearLayout layoutGSTDetail;
    private LinearLayout layoutPANDetail;
    private LinearLayout layoutAgreementUploadDetail;
    private LinearLayout layoutFooter;
    private LinearLayout layoutIIBFCertificationDetail;
    private LinearLayout layoutMSMECertificationDetail;
    private LinearLayout layoutShopEstCertificationDetail;
    private LinearLayout layoutQualificationDetail;
    private LinearLayout layoutAddressDetail;

    private Button btnClear;
    private Button btnCancel;
    private Button btnSubmit;
    private Typeface font;
    private Context context;
    private ProgressDialog progressDialog;
    private List<CustomFranchiseeApplicationSpinnerDto> documentTypeList;
    private DocumentManagerRepository documentManagerRepo;
    private GetDocumentTypeSpinnerData getDocumentTypeSpinnerData = null;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alert;

    //All Child Fragments
    GSTDetailsFragment gstDetailsFragment;
    PANDetailsFragment panDetailsFragment;
    AgreementUploadFragment agreementUploadFragment;
    IIBFCertificationFragment iibfCertificationFragment;
    MSMECertificateFragment msmeCertificateFragment;
    ShopEstablishmentCertificateFragment shopEstCertifiateFragment;
    QualificationDetailsFragment qualificationDetailsFragment;
    AddressDetailsFragment addressDetailsFragment;

    private static final String DOCUMENT_GST = "GSTIN Document";
    private static final String DOCUMENT_PAN = "PAN Card";
    private static final String DOCUMENT_AGREEMENT_UPLOAD = "Agreement Upload";
    private static final String IIBF_CERTIFICATION = "IIBF Certification Data Upload";
    private static final String DOCUMENT_MSME_UPLOAD = "MSME Certificate";
    private static final String DOCUMENT_SHOP_EST_CERTIFICATION = "Shop Establishment Certificate";
    private static final String DOCUMENT_QUALIFICATION = "Qualification Detail";
    private static final String DOCUMENT_ADDRESS_PROOF_DETAIL = "Address Proof Detail";
    public static final String TYPE_GST = "1";
    public static final String TYPE_PAN_CARD = "2";
    public static final String TYPE_AGREEMENT_UPLOAD = "3";
    public static final String TYPE_IIBF_CERFITIFCATION = "4";
    public static final String TYPE_MSME_CERITIFATE = "5";
    public static final String TYPE_SHOP_EST_CERFITIFCATION = "6";
    public static final String TYPE_QUALIFICATION= "7";
    public static final String TYPE_ADDRESS_PROOF= "8";
    private String SELECTED_TYPE = null;
    private AsyncGetSpecificDocumentMgrDetails asyncGetSpecificDocumentMgrDetails = null;
    private AsyncSaveDocumentDetails asyncSaveDocumentDetails = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_upload);

        //Initialize data
        context = this;
        documentManagerRepo = new DocumentManagerRepository(context);
        font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");

        //References
        spinnerDocumentType = findViewById(R.id.spinnerDocumentType);
        layoutGSTDetail = findViewById(R.id.layoutGSTDetail);
        layoutPANDetail = findViewById(R.id.layoutPANDetail);
        layoutAgreementUploadDetail = findViewById(R.id.layoutAgreementUploadDetail);
        layoutIIBFCertificationDetail = findViewById(R.id.layoutIIBFCertificationDetail);
        layoutMSMECertificationDetail = findViewById(R.id.layoutMSMECertificationDetail);
        layoutShopEstCertificationDetail = findViewById(R.id.layoutShopEstCertificationDetail);
        layoutQualificationDetail = findViewById(R.id.layoutQualificationDetail);
        layoutAddressDetail = findViewById(R.id.layoutAddressDetail);

        layoutFooter = findViewById(R.id.layoutFooter);
        btnClear = findViewById(R.id.btnClear);
        btnCancel = findViewById(R.id.btnCancel);
        btnSubmit = findViewById(R.id.btnSubmitDocuments);

        //Widgets
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null) {
            String title = "Document Manager";
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + title + "</small>"));
        }

        //All Child Fragments
        gstDetailsFragment = (GSTDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentGSTDetails);
        panDetailsFragment = (PANDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentPANDetails);
        agreementUploadFragment = (AgreementUploadFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentAgreementUploadDetails);
        iibfCertificationFragment = (IIBFCertificationFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentIIBFCertificationDetails);
        msmeCertificateFragment = (MSMECertificateFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentMSMECertificationDetails);
        shopEstCertifiateFragment = (ShopEstablishmentCertificateFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentShopEstCertificationDetails);
        qualificationDetailsFragment = (QualificationDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentQualificationDetails);
        addressDetailsFragment = (AddressDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentAddressDetails);

        btnSubmit.setTypeface(font);
        btnSubmit.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " + getResources().getString(R.string.submit)));
        btnSubmit.setOnClickListener(this);

        btnCancel.setTypeface(font);
        btnCancel.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " + getResources().getString(R.string.Cancel)));
        btnCancel.setOnClickListener(this);

        btnClear.setTypeface(font);
        btnClear.setText(new SpannableStringBuilder(new String(new char[]{0xf05d}) + " " + getResources().getString(R.string.clear)));
        btnClear.setOnClickListener(this);

        getDocumentTypeSpinnerData = new GetDocumentTypeSpinnerData();
        getDocumentTypeSpinnerData.execute("");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (getDocumentTypeSpinnerData != null && !getDocumentTypeSpinnerData.isCancelled()) {
            getDocumentTypeSpinnerData.cancel(true);
        }

        if (asyncGetSpecificDocumentMgrDetails != null && !asyncGetSpecificDocumentMgrDetails.isCancelled()) {
            asyncGetSpecificDocumentMgrDetails.cancel(true);
        }
    }

    @Override
    public void onBackPressed() {
        backPressed();
    }

    public void backPressed() {
      /*  Intent intent = new Intent(context, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);*/
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            backPressed();
        } else if (itemId == R.id.action_home_dashborad) {
            backPressed();
        }
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int Id = parent.getId();

        if (Id == R.id.spinnerDocumentType) {
            //spinnerDocumentType.isSpinnerDialogOpen = false;
            if (position >= 0) {
                CustomFranchiseeApplicationSpinnerDto dto = (CustomFranchiseeApplicationSpinnerDto) spinnerDocumentType.getItemAtPosition(position);
                String name = dto.getName();
                displaySpecificDocumentUploadSection(name);
               /* if (name.startsWith("Please"))
                    layoutFooter.setVisibility(View.GONE);
                else
                    layoutFooter.setVisibility(View.VISIBLE);*/

            }
        }
    }

    public void IsFooterLayoutVisible(boolean IsVisible) {
        if (IsVisible)
            layoutFooter.setVisibility(View.VISIBLE);
        else
            layoutFooter.setVisibility(View.GONE);
    }

    private void displaySpecificDocumentUploadSection(String name) {
        switch (name) {

            case DOCUMENT_GST:
                SELECTED_TYPE = TYPE_GST;
                layoutGSTDetail.setVisibility(View.VISIBLE);
                layoutPANDetail.setVisibility(View.GONE);
                layoutAgreementUploadDetail.setVisibility(View.GONE);
                layoutIIBFCertificationDetail.setVisibility(View.GONE);
                layoutMSMECertificationDetail.setVisibility(View.GONE);
                layoutShopEstCertificationDetail.setVisibility(View.GONE);
                getDocumentDetailsByType(TYPE_GST);

                break;

            case DOCUMENT_PAN:
                SELECTED_TYPE = TYPE_PAN_CARD;
                layoutGSTDetail.setVisibility(View.GONE);
                layoutPANDetail.setVisibility(View.VISIBLE);
                layoutAgreementUploadDetail.setVisibility(View.GONE);
                layoutIIBFCertificationDetail.setVisibility(View.GONE);
                layoutMSMECertificationDetail.setVisibility(View.GONE);
                layoutShopEstCertificationDetail.setVisibility(View.GONE);
                getDocumentDetailsByType(TYPE_PAN_CARD);
                break;

            case DOCUMENT_AGREEMENT_UPLOAD:
                SELECTED_TYPE = TYPE_AGREEMENT_UPLOAD;
                getDocumentDetailsByType(TYPE_AGREEMENT_UPLOAD);
                break;
            case IIBF_CERTIFICATION:
                SELECTED_TYPE = TYPE_IIBF_CERFITIFCATION;
                getDocumentDetailsByType(TYPE_IIBF_CERFITIFCATION);
                break;

            case DOCUMENT_MSME_UPLOAD:
                SELECTED_TYPE = TYPE_MSME_CERITIFATE;
                getDocumentDetailsByType(TYPE_MSME_CERITIFATE);
                break;

            case DOCUMENT_SHOP_EST_CERTIFICATION:
                SELECTED_TYPE = TYPE_SHOP_EST_CERFITIFCATION;
                getDocumentDetailsByType(TYPE_SHOP_EST_CERFITIFCATION);
                break;

            case DOCUMENT_QUALIFICATION:
                SELECTED_TYPE = TYPE_QUALIFICATION;
                getDocumentDetailsByType(TYPE_QUALIFICATION);
                break;

            case DOCUMENT_ADDRESS_PROOF_DETAIL:
                SELECTED_TYPE = TYPE_ADDRESS_PROOF;
                getDocumentDetailsByType(TYPE_ADDRESS_PROOF);
                break;

            default:
                SELECTED_TYPE = null;
                layoutGSTDetail.setVisibility(View.GONE);
                layoutPANDetail.setVisibility(View.GONE);
                layoutAgreementUploadDetail.setVisibility(View.GONE);
                layoutIIBFCertificationDetail.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // spinnerDocumentType.isSpinnerDialogOpen = false;
    }

    @Override
    public void onClick(View v) {
        int Id = v.getId();

        if (Id == R.id.btnSubmitDocuments) {
            saveDocumentData(SELECTED_TYPE);
        }
        if (Id == R.id.btnClear) {
        }
        if (Id == R.id.btnCancel) {
            /*Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);*/
            finish();
        }
    }

    public class GetDocumentTypeSpinnerData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            Connection connection = new Connection(context);
            String tmpVkId = connection.getVkid();
            String enquiryId = CommonUtils.getEnquiryId(context);
            String vkIdOrEnquiryId = TextUtils.isEmpty(tmpVkId) ? enquiryId : tmpVkId;

            //STEP 1: Documet Type List
            documentTypeList = documentManagerRepo.getDocumentTypeList(vkIdOrEnquiryId);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }

            //Bind Document Type Spinner
            spinnerDocumentType.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, documentTypeList));
            int pos = 0;
            spinnerDocumentType.setSelection(pos);
            spinnerDocumentType.setOnItemSelectedListener(DocumentManagerActivity.this);
        }
    }

    private void getDocumentDetailsByType(final String type) {

        asyncGetSpecificDocumentMgrDetails = new AsyncGetSpecificDocumentMgrDetails(context, new AsyncGetSpecificDocumentMgrDetails.Callback() {
            @Override
            public void onResult(String result) {
                try {
                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                        return;
                    }

                    if (result.startsWith("ERROR")) {
                        String msg = result.replace("ERROR|", "");
                        msg = TextUtils.isEmpty(msg) ? "Something went wrong. Please try again later." : msg;
                        AlertDialogBoxInfo.alertDialogShow(context, msg);
                        return;
                    }

                    if (result.startsWith("OKAY")) {
                        //Handle Response
                        String data = result.replace("OKAY|", "");
                        if (TextUtils.isEmpty(data))
                            AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                        else {
                            reloadFragmentAsPerType(type, data);
                        }
                    } else {
                        AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                }
            }
        });
        asyncGetSpecificDocumentMgrDetails.execute(type);
    }

    private void reloadFragmentAsPerType(String type, String data) {

        if (type.equalsIgnoreCase(TYPE_GST)) {

            layoutGSTDetail.setVisibility(View.VISIBLE);
            layoutPANDetail.setVisibility(View.GONE);
            layoutAgreementUploadDetail.setVisibility(View.GONE);
            layoutIIBFCertificationDetail.setVisibility(View.GONE);
            layoutMSMECertificationDetail.setVisibility(View.GONE);
            layoutShopEstCertificationDetail.setVisibility(View.GONE);
            layoutQualificationDetail.setVisibility(View.GONE);
            gstDetailsFragment.reload(data);

        } else if (type.equalsIgnoreCase(TYPE_PAN_CARD)) {

            layoutGSTDetail.setVisibility(View.GONE);
            layoutPANDetail.setVisibility(View.VISIBLE);
            layoutAgreementUploadDetail.setVisibility(View.GONE);
            layoutIIBFCertificationDetail.setVisibility(View.GONE);
            layoutMSMECertificationDetail.setVisibility(View.GONE);
            layoutShopEstCertificationDetail.setVisibility(View.GONE);
            layoutQualificationDetail.setVisibility(View.GONE);
            panDetailsFragment.reload(data);

        } else if (type.equalsIgnoreCase(TYPE_AGREEMENT_UPLOAD)) {

            layoutGSTDetail.setVisibility(View.GONE);
            layoutPANDetail.setVisibility(View.GONE);
            layoutAgreementUploadDetail.setVisibility(View.VISIBLE);
            layoutIIBFCertificationDetail.setVisibility(View.GONE);
            layoutMSMECertificationDetail.setVisibility(View.GONE);
            layoutShopEstCertificationDetail.setVisibility(View.GONE);
            layoutQualificationDetail.setVisibility(View.GONE);
            layoutAddressDetail.setVisibility(View.GONE);
            agreementUploadFragment.reload(data);
        } else if (type.equalsIgnoreCase(TYPE_IIBF_CERFITIFCATION)) {

            layoutGSTDetail.setVisibility(View.GONE);
            layoutPANDetail.setVisibility(View.GONE);
            layoutAgreementUploadDetail.setVisibility(View.GONE);
            layoutIIBFCertificationDetail.setVisibility(View.VISIBLE);
            layoutMSMECertificationDetail.setVisibility(View.GONE);
            layoutShopEstCertificationDetail.setVisibility(View.GONE);
            layoutQualificationDetail.setVisibility(View.GONE);
            layoutAddressDetail.setVisibility(View.GONE);
            iibfCertificationFragment.reload(data);
        } else if (type.equalsIgnoreCase(TYPE_MSME_CERITIFATE)) {

            layoutGSTDetail.setVisibility(View.GONE);
            layoutPANDetail.setVisibility(View.GONE);
            layoutAgreementUploadDetail.setVisibility(View.GONE);
            layoutIIBFCertificationDetail.setVisibility(View.GONE);
            layoutMSMECertificationDetail.setVisibility(View.VISIBLE);
            layoutShopEstCertificationDetail.setVisibility(View.GONE);
            layoutQualificationDetail.setVisibility(View.GONE);
            layoutAddressDetail.setVisibility(View.GONE);
            msmeCertificateFragment.reload(data);
        } else if (type.equalsIgnoreCase(TYPE_SHOP_EST_CERFITIFCATION)) {
            layoutGSTDetail.setVisibility(View.GONE);
            layoutPANDetail.setVisibility(View.GONE);
            layoutAgreementUploadDetail.setVisibility(View.GONE);
            layoutIIBFCertificationDetail.setVisibility(View.GONE);
            layoutMSMECertificationDetail.setVisibility(View.GONE);
            layoutShopEstCertificationDetail.setVisibility(View.VISIBLE);
            layoutQualificationDetail.setVisibility(View.GONE);
            layoutAddressDetail.setVisibility(View.GONE);
            shopEstCertifiateFragment.reload(data);
        } else if (type.equalsIgnoreCase(TYPE_QUALIFICATION)) {
            layoutGSTDetail.setVisibility(View.GONE);
            layoutPANDetail.setVisibility(View.GONE);
            layoutAgreementUploadDetail.setVisibility(View.GONE);
            layoutIIBFCertificationDetail.setVisibility(View.GONE);
            layoutMSMECertificationDetail.setVisibility(View.GONE);
            layoutShopEstCertificationDetail.setVisibility(View.GONE);
            layoutQualificationDetail.setVisibility(View.VISIBLE);
            layoutAddressDetail.setVisibility(View.GONE);
            qualificationDetailsFragment.reload(data);
        } else if (type.equalsIgnoreCase(TYPE_ADDRESS_PROOF)) {
            layoutGSTDetail.setVisibility(View.GONE);
            layoutPANDetail.setVisibility(View.GONE);
            layoutAgreementUploadDetail.setVisibility(View.GONE);
            layoutIIBFCertificationDetail.setVisibility(View.GONE);
            layoutMSMECertificationDetail.setVisibility(View.GONE);
            layoutShopEstCertificationDetail.setVisibility(View.GONE);
            layoutQualificationDetail.setVisibility(View.GONE);
            layoutAddressDetail.setVisibility(View.VISIBLE);
            addressDetailsFragment.refreshAddressType(data);
        }
    }

    private int validateAsPerType(String type) {

        if (type.equalsIgnoreCase(TYPE_GST)) {
            return gstDetailsFragment.IsGSTDetailsValidated();
        } else if (type.equalsIgnoreCase(TYPE_PAN_CARD)) {
            return panDetailsFragment.IsPANDetailsValidated();
        } else if (type.equalsIgnoreCase(TYPE_AGREEMENT_UPLOAD)) {
            return agreementUploadFragment.IsAgreementDetailsValidated();
        } else if (type.equalsIgnoreCase(TYPE_IIBF_CERFITIFCATION)) {
            return iibfCertificationFragment.IsIIBFValidation();
        } else if (type.equalsIgnoreCase(TYPE_MSME_CERITIFATE)) {
            return msmeCertificateFragment.IsMSMEDetailsValidated();
        } else if (type.equalsIgnoreCase(TYPE_SHOP_EST_CERFITIFCATION)) {
            return shopEstCertifiateFragment.IsShopEstDetailsValidated();
        } else if (type.equalsIgnoreCase(TYPE_QUALIFICATION)) {
            return qualificationDetailsFragment.IsQualificationDetailsValidated();
        } else if (type.equalsIgnoreCase(TYPE_ADDRESS_PROOF)) {
            return addressDetailsFragment.IsAddressDetailsValidated();
        }
        return 0;
    }

    private void saveDocumentData(final String type) {

        if (TextUtils.isEmpty(SELECTED_TYPE))
            return;

        //Validate
        int status = validateAsPerType(SELECTED_TYPE);
        if (status != 0)
            return;

        //Internet Connectivity check
        if (!InternetConnection.isNetworkAvailable(context)) {
            AlertDialogBoxInfo.alertDialogShow(context, "No Internet Connection.");
            return;
        }

        Object object = prepareObjUsingType(type);

        //base on type save data
        asyncSaveDocumentDetails = new AsyncSaveDocumentDetails(context, type, object, new AsyncSaveDocumentDetails.Callback() {
            @Override
            public void onResult(String result) {
                try {

                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                        return;
                    }

                    if (result.startsWith("ERROR")) {
                        String msg = result.replace("ERROR|", "");
                        msg = TextUtils.isEmpty(msg) ? "Something went wrong. Please try again later." : msg;
                        AlertDialogBoxInfo.alertDialogShow(context, msg);
                        return;
                    }

                    if (result.startsWith("OKAY")) {
                        //Handle Response
                        String data = result.replace("OKAY|", "");
                        if (TextUtils.isEmpty(data))
                            AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                        else {
                            String msg = "";
                            if (type.equalsIgnoreCase(TYPE_AGREEMENT_UPLOAD)) {
                                showMessage(getResources().getString(R.string.agreement_updated_successful), type, data);
                                return;
                            }

                            if (type.equalsIgnoreCase(TYPE_IIBF_CERFITIFCATION)) {
                                showMessage(getResources().getString(R.string.iibf_data_updated_successful), type, data);
                                return;
                            }

                            if (type.equalsIgnoreCase(TYPE_MSME_CERITIFATE)) {
                                showMessage(getResources().getString(R.string.msme_updated_successful), type, data);
                                return;
                            }

                            if (type.equalsIgnoreCase(TYPE_SHOP_EST_CERFITIFCATION)) {
                                showMessage(getResources().getString(R.string.shop_updated_successful), type, data);
                                return;
                            }

                            if (type.equalsIgnoreCase(TYPE_QUALIFICATION)) {
                                showMessage(getResources().getString(R.string.qualification_updated_successful), type, data);
                                return;
                            }

                            if (type.equalsIgnoreCase(TYPE_ADDRESS_PROOF)) {
                                showMessage(getResources().getString(R.string.address_updated_successful), type, data);
                                return;
                            }

                            msg = (type.equalsIgnoreCase(TYPE_GST)) ? "GSTIN " : "PAN Card ";
                            showMessage(msg + getResources().getString(R.string.docs_updated_successful), type, data);

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(DocumentManagerActivity.this, getResources().getString(R.string.Warning));
                }
            }
        });
        asyncSaveDocumentDetails.execute("");
    }

    public void showMessage(String msg, final String type, final String data) {
        if (TextUtils.isEmpty(msg))
            return;

        if (alert == null) {
            alertDialogBuilder = new AlertDialog.Builder(context);

            alertDialogBuilder
                    .setMessage(Html.fromHtml(msg))
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            alert = null;
                            reloadFragmentAsPerType(type, data);
                        }
                    });
            alert = alertDialogBuilder.create();
            alert.show();
        }
    }

    private Object prepareObjUsingType(String type) {
        Object object = null;

        if (type.equalsIgnoreCase(DocumentManagerActivity.TYPE_GST)) {
            object =  gstDetailsFragment.getGSTDetailsDto();

        } else if (type.equalsIgnoreCase(DocumentManagerActivity.TYPE_PAN_CARD)) {
            object =  panDetailsFragment.getPANCardDetailsDto();

        } else if (type.equalsIgnoreCase(DocumentManagerActivity.TYPE_AGREEMENT_UPLOAD)) {
            object = agreementUploadFragment.getAgreementDetailsDto();

        } else if (type.equalsIgnoreCase(DocumentManagerActivity.TYPE_IIBF_CERFITIFCATION)) {
           object = iibfCertificationFragment.getIIBFCertificationDto();

        } else if (type.equalsIgnoreCase(DocumentManagerActivity.TYPE_MSME_CERITIFATE)) {
            object = msmeCertificateFragment.getMSMECertificateDetail();

        } else if (type.equalsIgnoreCase(DocumentManagerActivity.TYPE_SHOP_EST_CERFITIFCATION)) {
            object = shopEstCertifiateFragment.getShopEstCertificateDetail();

        } else if (type.equalsIgnoreCase(DocumentManagerActivity.TYPE_QUALIFICATION)) {
            object = qualificationDetailsFragment.getQualificationDetail();

        } else if (type.equalsIgnoreCase(DocumentManagerActivity.TYPE_ADDRESS_PROOF)) {
            object = addressDetailsFragment.getAddressDetail();
        }
        return object;
    }

}

