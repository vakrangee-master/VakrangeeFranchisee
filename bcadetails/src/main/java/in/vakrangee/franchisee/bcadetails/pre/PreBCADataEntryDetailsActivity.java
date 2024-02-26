package in.vakrangee.franchisee.bcadetails.pre;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import in.vakrangee.franchisee.bcadetails.R;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;

public class PreBCADataEntryDetailsActivity  extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "PreBCADataEntryDetailsActivity";
    private Context context;
    private Toolbar toolbar;
    private TextView btnNext, btnBack;
    private LinearLayout layoutFragment;
    private TextView txtPaging;
    private Typeface font;
    private Connection connection;
    private DeprecateHandler deprecateHandler;
    private ScrollView fapScrollview;
    private PreBCADataEntryDetailsFragment preBCADataEntryDetailsFragment;
    private PreBCADetailsDto preBCADetailsDto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_bca_details);

        //Widgets
        context = this;
        connection = new Connection(getApplicationContext());
        deprecateHandler = new DeprecateHandler(context);
        font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null) {
            //String title = getString(R.string.lic_proposal);
            String title = "BCA Detail";
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + title + "</small>"));
        }

        preBCADetailsDto = (PreBCADetailsDto) getIntent().getSerializableExtra("PRE_BCA_DETAILS");

        fapScrollview = findViewById(R.id.fapScrollview);
        btnBack = findViewById(R.id.btnBack);
        btnNext = findViewById(R.id.btnNext);
        txtPaging = findViewById(R.id.txtPaging);
        txtPaging.setText("1 of " + PreBCADataEntryDetailsFragment.MAX_COUNT);
        btnBack.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        btnNext.setTypeface(font);
        btnNext.setText(new SpannableStringBuilder(" " + " " + context.getResources().getString(R.string.next) + " " + " " + new String(new char[]{0xf054}) + " " + " "));

        btnBack.setTypeface(font);
        btnBack.setText(new SpannableStringBuilder(" " + " " + new String(new char[]{0xf053}) + " " + " " + context.getResources().getString(R.string.back) + " " + " "));

        preBCADataEntryDetailsFragment = (PreBCADataEntryDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentPreBCADetail);
        preBCADataEntryDetailsFragment.refresh(preBCADetailsDto);

        layoutFragment = findViewById(R.id.layoutFragment);

    }

    public void setPagingText(String txt) {
        txtPaging.setText(txt);
    }

    @Override
    public void onBackPressed() {
        backPressed();
    }

    public void backPressed() {
        finish();

    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();

        if (Id == R.id.btnBack) {
            preBCADataEntryDetailsFragment.showPrevious();

        } else if (Id == R.id.btnNext) {
            preBCADataEntryDetailsFragment.showNext();
        }
    }

    public TextView getBackButton() {
        return btnBack;
    }

    public TextView getNextButton() {
        return btnNext;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            backPressed();
        }
        return true;
    }
}
