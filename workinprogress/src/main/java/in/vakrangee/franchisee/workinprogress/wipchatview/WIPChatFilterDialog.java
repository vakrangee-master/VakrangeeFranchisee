package in.vakrangee.franchisee.workinprogress.wipchatview;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import in.vakrangee.franchisee.workinprogress.R;
import in.vakrangee.supercore.franchisee.commongui.CustomFranchiseeApplicationSpinnerAdapter;
import in.vakrangee.supercore.franchisee.nextgenfranchiseeapplication.CustomFranchiseeApplicationSpinnerDto;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;

public class WIPChatFilterDialog extends Dialog implements android.view.View.OnClickListener {

    private static final String TAG = "WIPChatFilterDialog";
    private Context context;
    private LinearLayout parentLayout;
    private Button btnClose;
    private IChatClicks iChatClicks;
    private TextView btnReset, btnFilter;
    private AsyncGetWIPCategoryDetails asyncGetWIPCategoryDetails = null;
    private CustomFranchiseeApplicationSpinnerAdapter elementsAdapter;
    private Spinner spinnerCategory, spinnerSubCategory;
    private List<CustomFranchiseeApplicationSpinnerDto> elementsList;
    private List<CustomFranchiseeApplicationSpinnerDto> subElementsList;
    private CustomFranchiseeApplicationSpinnerDto selCategoryDto;
    private CustomFranchiseeApplicationSpinnerDto selSubCategoryDto;

    public interface IChatClicks {

        public void onFilterClick(CustomFranchiseeApplicationSpinnerDto selCatDto, CustomFranchiseeApplicationSpinnerDto selSubCatDto);

        public void onResetClick();
    }

    public WIPChatFilterDialog(@NonNull Context context, CustomFranchiseeApplicationSpinnerDto selCategoryDto, CustomFranchiseeApplicationSpinnerDto selSubCategoryDto, IChatClicks iChatClicks) {
        super(context);
        this.context = context;
        this.selCategoryDto = selCategoryDto;
        this.selSubCategoryDto = selSubCategoryDto;
        this.iChatClicks = iChatClicks;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        final Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        setContentView(R.layout.dialog_chat_filter);

        //Widgets
        parentLayout = findViewById(R.id.parentLayout);
        CommonUtils.setDialogWidth(context, parentLayout);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerSubCategory = findViewById(R.id.spinnerSubCategory);

        btnClose = findViewById(R.id.btnClose);
        btnClose.setTypeface(font);
        btnClose.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + "  "));
        btnClose.setOnClickListener(this);

        btnFilter = findViewById(R.id.btnFilter);
        btnReset = findViewById(R.id.btnReset);

        //Set Adapter
        btnClose.setOnClickListener(this);
        btnFilter.setOnClickListener(this);
        btnReset.setOnClickListener(this);

        refresh(selCategoryDto, selSubCategoryDto);

    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();

        if (Id == R.id.btnClose) {
            dismiss();

        } else if (Id == R.id.btnFilter) {

            if (selCategoryDto == null) {
                AlertDialogBoxInfo.alertDialogShow(context, "Please select Category to filter.");
                return;
            }

            if (selSubCategoryDto == null) {
                AlertDialogBoxInfo.alertDialogShow(context, "Please select Sub Category to filter.");
                return;
            }

            iChatClicks.onFilterClick(selCategoryDto, selSubCategoryDto);
            dismiss();

        } else if (Id == R.id.btnReset) {
            iChatClicks.onResetClick();
            dismiss();

        }
    }

    public void refresh(CustomFranchiseeApplicationSpinnerDto selCategoryDto, CustomFranchiseeApplicationSpinnerDto selSubCategoryDto) {
        this.selCategoryDto = selCategoryDto;
        this.selSubCategoryDto = selSubCategoryDto;

        //Get Category
        populateCategory();
    }

    /**
     * Populate Category
     */
    public void populateCategory() {

        asyncGetWIPCategoryDetails = new AsyncGetWIPCategoryDetails(context, Constants.WIP_CHATVIEW_CATEGORY, null, true, new AsyncGetWIPCategoryDetails.Callback() {
            @Override
            public void onResult(String result) {
                try {
                    //Check if response if null or empty
                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                        return;
                    }

                    //Process response
                    JSONArray jsonArray = new JSONArray(result);

                    Gson gson = new GsonBuilder().create();
                    elementsList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<CustomFranchiseeApplicationSpinnerDto>>() {
                    }.getType());
                    elementsList.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

                    elementsAdapter = new CustomFranchiseeApplicationSpinnerAdapter(context, elementsList);
                    spinnerCategory.setEnabled(true);
                    spinnerCategory.setAdapter(elementsAdapter);

                    int selPos = 0;
                    if (selCategoryDto != null) {
                        selPos = getSelectedPos(elementsList, selCategoryDto.getId());
                    }
                    spinnerCategory.setSelection(selPos);
                    spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position > 0) {
                                selCategoryDto = (CustomFranchiseeApplicationSpinnerDto) spinnerCategory.getItemAtPosition(position);
                                populateSubCategory(selCategoryDto.getId());
                            } else {
                                selCategoryDto = null;
                                selSubCategoryDto = null;
                                spinnerSubCategory.setAdapter(null);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                }
            }
        });
        asyncGetWIPCategoryDetails.execute("");
    }

    public int getSelectedPos(List<CustomFranchiseeApplicationSpinnerDto> spinnerDtoList, String selectedValue) {

        if (TextUtils.isEmpty(selectedValue))
            return 0;

        for (int i = 0; i < spinnerDtoList.size(); i++) {
            if (selectedValue.equalsIgnoreCase(spinnerDtoList.get(i).getId()))
                return i;
        }
        return 0;
    }

    public void populateSubCategory(String categoryId) {

        if (!InternetConnection.isNetworkAvailable(context)) {
            AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.internetCheck));
            return;
        }

        asyncGetWIPCategoryDetails = new AsyncGetWIPCategoryDetails(context, Constants.WIP_CHATVIEW_SUB_CATEGORY, categoryId, true, new AsyncGetWIPCategoryDetails.Callback() {
            @Override
            public void onResult(String result) {
                try {

                    //Check if response if null or empty
                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                        return;
                    }

                    //Process response
                    JSONArray jsonArray = new JSONArray(result);
                    Gson gson = new GsonBuilder().create();
                    subElementsList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<CustomFranchiseeApplicationSpinnerDto>>() {
                    }.getType());
                    subElementsList.add(0, new CustomFranchiseeApplicationSpinnerDto("0", "Please Select"));

                    elementsAdapter = new CustomFranchiseeApplicationSpinnerAdapter(context, subElementsList);
                    spinnerSubCategory.setEnabled(true);
                    spinnerSubCategory.setAdapter(elementsAdapter);
                    int selPos = 0;
                    if (selSubCategoryDto != null) {
                        selPos = getSelectedPos(subElementsList, selSubCategoryDto.getId());
                    }
                    spinnerSubCategory.setSelection(selPos);
                    spinnerSubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position > 0) {
                                selSubCategoryDto = (CustomFranchiseeApplicationSpinnerDto) spinnerSubCategory.getItemAtPosition(position);
                            } else {
                                selSubCategoryDto = null;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.Warning));
                }
            }
        });

        asyncGetWIPCategoryDetails.execute("");
    }

}
