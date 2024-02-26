package in.vakrangee.franchisee.workinprogress.wipchatview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import in.vakrangee.franchisee.workinprogress.R;
import in.vakrangee.supercore.franchisee.nextgenfranchiseeapplication.CustomFranchiseeApplicationSpinnerDto;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;

public class WorkInProgressChatViewActivity extends AppCompatActivity {

    private static final String TAG = "WorkInProgressChatViewActivity";
    Toolbar toolbar;
    Connection connection;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alert;
    private Context context;
    private DeprecateHandler deprecateHandler;
    private WIPChatViewFragment chatViewFragment;
    private MenuItem filterItem;
    private WIPChatFilterDialog filterDialog;
    private CustomFranchiseeApplicationSpinnerDto selCategoryDto;
    private CustomFranchiseeApplicationSpinnerDto selSubCategoryDto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wip_chatview);

        this.context = this;
        connection = new Connection(getApplicationContext());
        deprecateHandler = new DeprecateHandler(context);

        toolbar = (Toolbar) findViewById(R.id.toolbarimage);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            String title = getString(R.string.nextgen_work_in_progress);
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + title + "</small>"));
        }

        chatViewFragment = (WIPChatViewFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentWIPChatView);
        chatViewFragment.reloadData();
    }

    public void showMessage(String msg) {
        if (TextUtils.isEmpty(msg))
            return;

        if (alert == null) {
            alertDialogBuilder = new AlertDialog.Builder(context);

            alertDialogBuilder
                    .setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            alert = null;
                        }
                    });
            alert = alertDialogBuilder.create();
            alert.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.wip_filter, menu);
        filterItem = menu.findItem(R.id.action_filter);

        setMenuItemColor(false);

        return super.onCreateOptionsMenu(menu);
    }

    private void setMenuItemColor(boolean IsFiltered) {
        Drawable icon = getResources().getDrawable(R.drawable.filter_wip_old);
        if (IsFiltered) {
            icon.setColorFilter(getResources().getColor(R.color.orange_L2), PorterDuff.Mode.SRC_IN);
        } else {
            icon.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        }
        filterItem.setIcon(icon);
    }

    @Override
    public void onBackPressed() {
        backPressed();
    }

    public void backPressed() {
        /*Intent intent = new Intent(this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();*/
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            backPressed();
        } else if (itemId == R.id.action_filter) {
            filterChatsDialog();
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void filterChatsDialog() {
        if (filterDialog != null && filterDialog.isShowing()) {
            filterDialog.refresh(selCategoryDto, selSubCategoryDto);
            return;
        }

        filterDialog = new WIPChatFilterDialog(context, selCategoryDto, selSubCategoryDto, new WIPChatFilterDialog.IChatClicks() {
            @Override
            public void onFilterClick(CustomFranchiseeApplicationSpinnerDto selCatDto, CustomFranchiseeApplicationSpinnerDto selSubCatDto) {
                selCategoryDto = selCatDto;
                selSubCategoryDto = selSubCatDto;
                setMenuItemColor(true);
                chatViewFragment.refreshData(selCatDto.getId(), selSubCatDto.getId(), true);
            }

            @Override
            public void onResetClick() {
                selCategoryDto = null;
                selSubCategoryDto = null;
                setMenuItemColor(false);
                chatViewFragment.refreshData(null, null, false);
            }
        });
        filterDialog.show();
        filterDialog.setCancelable(true);

        //Layout Params
        Window window = filterDialog.getWindow();
        window.getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        window.setGravity(Gravity.TOP | Gravity.RIGHT);

        WindowManager.LayoutParams wlp = window.getAttributes();
        //wlp.y = 10;
        window.setAttributes(wlp);

        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); // This flag is required to set otherwise the setDimAmount method will not show any effect
        window.setDimAmount(0); //0 for no dim to 1 for full dim

    }

}
