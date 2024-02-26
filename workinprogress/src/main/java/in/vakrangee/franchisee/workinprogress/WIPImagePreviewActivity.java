package in.vakrangee.franchisee.workinprogress;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import in.vakrangee.supercore.franchisee.commongui.carouselgallery.ImageCarouselGalleryFragment;
import in.vakrangee.supercore.franchisee.ifc.IRadioButtonClick;

public class WIPImagePreviewActivity extends AppCompatActivity {

    private static final String TAG = "WIPImagePreviewActivity";
    private WIPStatusDto wipStatusDto;
    private WIPCheckListDto wipCheckListDto;
    private Toolbar toolbar;
    private ImageCarouselGalleryFragment fragmentImageCarousel;
    private int maxCount = 10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wip_img_preview);

        //Widgets
        toolbar = (Toolbar) findViewById(R.id.toolbarReadiness);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        //Get Data from Intent
        wipStatusDto = (WIPStatusDto) getIntent().getSerializableExtra("WIPStatusDto");
        wipCheckListDto = (WIPCheckListDto) getIntent().getSerializableExtra("WIPCheckListDto");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            String title = getString(R.string.site_readiness_verification);
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + wipCheckListDto.getElementName() + "</small>"));
        }

        fragmentImageCarousel = (ImageCarouselGalleryFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentImagePreview);
        fragmentImageCarousel.refresh(wipStatusDto.imagesList, maxCount, wipStatusDto.getStatus(), new IRadioButtonClick() {
            @Override
            public void onRadioButtonSelected(String status) {
                wipStatusDto.setStatus(status);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

}
