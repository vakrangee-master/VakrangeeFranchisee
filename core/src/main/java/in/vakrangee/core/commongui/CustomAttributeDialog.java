package in.vakrangee.core.commongui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import in.vakrangee.core.R;
import in.vakrangee.core.model.ElementAttributeDetailDto;
import in.vakrangee.core.utils.CommonUtils;

public class CustomAttributeDialog extends Dialog implements android.view.View.OnClickListener {

    private static final String TAG = "CustomAttributeDialog";
    private Context context;
    private RecyclerView recyclerViewElementAttributeLayout;
    private LinearLayout parentLayout;
    private Button btnClose;
    private String data;
    private ElementAttributeAdapter elementAttributeAdapter;
    private List<ElementAttributeDetailDto> attributeDetailList;
    private TextView txtTitle;
    private String title = null;

    public CustomAttributeDialog(@NonNull Context context, String data) {
        super(context);
        this.context = context;
        this.data = data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        final Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        setContentView(R.layout.dialog_element_attribute);

        //Widgets
        parentLayout = findViewById(R.id.parentLayout);
        CommonUtils.setDialogWidth(context, parentLayout);
        txtTitle = findViewById(R.id.txtTitle);
        btnClose = findViewById(R.id.btnClose);
        btnClose.setTypeface(font);
        btnClose.setText(new SpannableStringBuilder(new String(new char[]{0xf057}) + "  "));
        btnClose.setOnClickListener(this);

        //Set Adapter
        recyclerViewElementAttributeLayout = findViewById(R.id.recyclerViewElementDetailsLayout);
        btnClose.setOnClickListener(this);

        refresh(data);

    }

    public void refresh(String data) {
        this.data = data;

        //Prepare List
        Gson gson = new GsonBuilder().create();
        attributeDetailList = gson.fromJson(data, new TypeToken<ArrayList<ElementAttributeDetailDto>>() {
        }.getType());

        //Bind data
        elementAttributeAdapter = new ElementAttributeAdapter(context, title, attributeDetailList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerViewElementAttributeLayout.setLayoutManager(layoutManager);
        recyclerViewElementAttributeLayout.setItemAnimator(new DefaultItemAnimator());
        recyclerViewElementAttributeLayout.setAdapter(elementAttributeAdapter);
        recyclerViewElementAttributeLayout.setNestedScrollingEnabled(false);

    }

    // Allow Change Dialog Title Name
    public void setDialogTitle(String title) {

        if (!TextUtils.isEmpty(title)) {
            this.title = title;
            txtTitle.setText(title);
            elementAttributeAdapter.setDialogTitle(title);
        }
    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();

        if (Id == R.id.btnClose) {
            dismiss();
        }
    }
}
