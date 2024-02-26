package in.vakrangee.franchisee.delivery_address;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.activity.DashboardActivity;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;

public class DeliveryAddressActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private DeliveryAddressFragment deliveryAddressFragment;
    private AsyncGetDeliveryAddress asyncGetDeliveryAddress;
    //private CustomNavigationBar customNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_address);

        //Initialize data
        ButterKnife.bind(this);

        //setup toolbar
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            String title = getResources().getString(R.string.delivery_address);
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + title + "</small>"));
        }

        //customNavigationBar = new CustomNavigationBar(DeliveryAddressActivity.this, toolbar);

        deliveryAddressFragment = (DeliveryAddressFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentDeliveryAddress);

        asntaskDeliveryAddress();
    }


    private void asntaskDeliveryAddress() {
        //Connection connection = new Connection(DeliveryAddressActivity.this);
        //String tmpVkId = connection.getVkid();
        String enquiryId = CommonUtils.getEnquiryId(DeliveryAddressActivity.this);
        //String vkIdOrEnquiryId = TextUtils.isEmpty(tmpVkId) ? enquiryId : tmpVkId;

        asyncGetDeliveryAddress = new AsyncGetDeliveryAddress(DeliveryAddressActivity.this, enquiryId, "", new AsyncGetDeliveryAddress.Callback() {
            @Override
            public void onResult(String result) {
                try {

                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(DeliveryAddressActivity.this, getResources().getString(R.string.Warning));
                        return;
                    }
                    //Response
                    if (result.startsWith("ERROR|")) {
                        result = result.replace("ERROR|", "");
                        if (TextUtils.isEmpty(result)) {
                            AlertDialogBoxInfo.alertDialogShow(DeliveryAddressActivity.this, getResources().getString(R.string.Warning));
                        } else {
                            AlertDialogBoxInfo.alertDialogShow(DeliveryAddressActivity.this, result);
                        }
                    } else if (result.startsWith("OKAY|")) {
                        result = result.replace("OKAY|", "");
                        //result = "[{\"area\":\"C cchch\",\"pin_code\":\"797988\",\"address_line_1\":\"C GGC\",\"vtc_id\":\"301634\",\"is_flex_allowed\":\"Y\",\"is_editable\":\"Y\",\"address_line_2\":\"C GGC\",\"district_id\":\"319\",\"state_id\":\"18\",\"landmark\":\"Vhv\",\"provisional_length_main_signboard\":\"84\",\"provisional_width_main_signboard\":\"36\",\"locality\":\"h  hh\"}]";
                        //result = "[{\"is_editable\":\"Y\",\"is_flex_allowed\":\"Y\"}]";
                        deliveryAddressFragment.reload(result);

                    } else {
                        AlertDialogBoxInfo.alertDialogShow(DeliveryAddressActivity.this, getResources().getString(R.string.Warning));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(DeliveryAddressActivity.this, getResources().getString(R.string.Warning));
                }
            }
        });

        asyncGetDeliveryAddress.execute();
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
