package in.vakrangee.franchisee.payment_history;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.activity.DashboardActivity;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;

public class PaymentHistoryActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private PaymentHistoryFragment paymentHistoryFragment;
    private AsynctaskGetPaymentHistory asynctaskGetPaymentHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            String title = getResources().getString(R.string.payment_history);
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + title + "</small>"));
        }

        //customNavigationBar = new CustomNavigationBar(DeliveryAddressActivity.this, toolbar);

        paymentHistoryFragment = (PaymentHistoryFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentPaymentHistory);

        asynctaskPaymentHistoryDetails();
    }

    private void asynctaskPaymentHistoryDetails() {
        //Connection connection = new Connection(DeliveryAddressActivity.this);
        //String tmpVkId = connection.getVkid();
        String enquiryId = CommonUtils.getEnquiryId(PaymentHistoryActivity.this);
        //String vkIdOrEnquiryId = TextUtils.isEmpty(tmpVkId) ? enquiryId : tmpVkId;

        asynctaskGetPaymentHistory = new AsynctaskGetPaymentHistory(PaymentHistoryActivity.this, enquiryId, new AsynctaskGetPaymentHistory.Callback() {
            @Override
            public void onResult(String result) {
                try {
                    //result="OKAY|{\"frachisee_name\":\"Nilesh Dhola\",\"frachisee_application_no\":\"FA20190827237072\",\"model_type\":\"broze_model_type_c\",\"mobile_no\":\"9167030868\",\"email_id\":\"sandeeps@vakrangee.in\",\"alter_email_id\":\"nilesh@vakrangee.in\",\"total_fee\":\"371700\",\"address\":\"vakrangee House plot 93,MIDC\",\"payment_history_details\":[{\"instalment_type\":\"Y\",\"instalment_step\":\"1st Instalment\",\"instalment_fee\":\"9999\",\"instalment_payment_date\":\"14-09-2019\"},{\"instalment_type\":\"Y\",\"instalment_step\":\"2nd Instalment\",\"instalment_fee\":\"72600\",\"instalment_payment_date\":\"14-09-2019\"},{\"instalment_type\":\"Y\",\"instalment_step\":\"3rd Instalment\",\"instalment_fee\":\"259600\",\"instalment_date\":\"14-09-2019\"}]}";
                    paymentHistoryFragment.reload(result);
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(PaymentHistoryActivity.this, getResources().getString(R.string.Warning));
                }
            }
        });
        asynctaskGetPaymentHistory.execute();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressed();
    }

    public void backPressed() {

        Intent intent = new Intent(this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //customNavigationBar.openDrawer();
                onBackPressed();
                break;
        }
        return true;
    }
}
