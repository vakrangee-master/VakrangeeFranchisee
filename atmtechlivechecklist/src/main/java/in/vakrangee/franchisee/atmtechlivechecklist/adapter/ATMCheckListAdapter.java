package in.vakrangee.franchisee.atmtechlivechecklist.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.HashMap;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import in.vakrangee.franchisee.atmtechlivechecklist.ATMCheckListConstants;
import in.vakrangee.franchisee.atmtechlivechecklist.R;
import in.vakrangee.franchisee.atmtechlivechecklist.model.ATMTechLiveCheckListDto;
import in.vakrangee.franchisee.atmtechlivechecklist.model.OptionsDto;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.GUIUtils;

public class ATMCheckListAdapter extends RecyclerView.Adapter<ATMCheckListAdapter.MyViewHolder> {

    public List<ATMTechLiveCheckListDto> atmTechLiveCheckList;
    private Context context;
    private IclickListener mListener;
    private DeprecateHandler deprecateHandler;
    private ColorStateList colorStateList;
    private View itemView;

    public ATMCheckListAdapter(Context context, List<ATMTechLiveCheckListDto> atmTechLiveCheckList, IclickListener listener) {
        this.context = context;
        this.atmTechLiveCheckList = atmTechLiveCheckList;
        this.mListener = listener;
        deprecateHandler = new DeprecateHandler(context);

        colorStateList = new ColorStateList(new int[][]{
                new int[]{-android.R.attr.state_enabled}, //disabled
                new int[]{android.R.attr.state_enabled} //enabled
        },
                new int[]{
                        deprecateHandler.getColor(R.color.teal),
                        deprecateHandler.getColor(R.color.teal)
                        /* Color.RED //disabled
                         ,Color.BLUE //enabled*/

                }
        );
    }

    public interface IclickListener {

        public void nextClickListener(int position);

        public void cameraClick(int position, ATMTechLiveCheckListDto dto);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtQuestionNo, txtQuestionName;
        public LinearLayout layoutParent, layoutTextBoxAnswer, layoutCheckBoxes, layoutProofPhoto, layoutSpinnerOptions;
        private EditText editTextAnswer;
        private RadioGroup radioGroupOptions;
        private Spinner spinnerOptions;
        private ImageView imgProofImage;

        public MyViewHolder(View view) {
            super(view);
            layoutParent = view.findViewById(R.id.layoutParent);
            layoutTextBoxAnswer = view.findViewById(R.id.layoutTextBoxAnswer);

            layoutProofPhoto = view.findViewById(R.id.layoutProofPhoto);

            layoutSpinnerOptions = view.findViewById(R.id.layoutSpinnerOptions);
            spinnerOptions = view.findViewById(R.id.spinnerOptions);

            txtQuestionNo = view.findViewById(R.id.txtQuestionNo);
            txtQuestionName = view.findViewById(R.id.txtQuestionName);

            editTextAnswer = view.findViewById(R.id.editTextAnswer);
            radioGroupOptions = view.findViewById(R.id.radioGroupOptions);
            layoutCheckBoxes = view.findViewById(R.id.layoutCheckBoxes);
            imgProofImage = view.findViewById(R.id.imgProofImage);

        }
    }

    @Override
    public ATMCheckListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_atm_checklist, parent, false);
        return new ATMCheckListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ATMCheckListAdapter.MyViewHolder holder, int position) {

        final ATMTechLiveCheckListDto detailsDto = atmTechLiveCheckList.get(position);

        //Question No
        String quesNo = TextUtils.isEmpty(detailsDto.getNo()) ? "0" : detailsDto.getNo();
        holder.txtQuestionNo.setText(quesNo + ".");

        String ques = TextUtils.isEmpty(detailsDto.getName()) ? "-" : detailsDto.getName();
        holder.txtQuestionName.setText(ques);

        TextView[] txtViewsForCompulsoryMark = {holder.txtQuestionName};
        if (!TextUtils.isEmpty(detailsDto.getIsMan()) && detailsDto.getIsMan().equalsIgnoreCase("1")) {
            GUIUtils.CompulsoryMark(txtViewsForCompulsoryMark);
        } else {
            GUIUtils.removeCompulsoryMark(txtViewsForCompulsoryMark);
        }

        String questionType = TextUtils.isEmpty(detailsDto.getCt()) ? ATMCheckListConstants.TYPE_TEXTBOX_TEXT : detailsDto.getCt();
        switch (questionType) {

            case ATMCheckListConstants.TYPE_DROP_DOWN:

                holder.layoutSpinnerOptions.setVisibility(View.VISIBLE);
                holder.editTextAnswer.setVisibility(View.GONE);
                holder.radioGroupOptions.setVisibility(View.GONE);
                holder.layoutCheckBoxes.setVisibility(View.GONE);

                prepareSpinnerDetails(holder, detailsDto);
                break;


            case ATMCheckListConstants.TYPE_CHECKBOX:

                holder.layoutSpinnerOptions.setVisibility(View.GONE);
                holder.editTextAnswer.setVisibility(View.GONE);
                holder.radioGroupOptions.setVisibility(View.GONE);
                holder.layoutCheckBoxes.setVisibility(View.VISIBLE);

                prepareCheckBoxes(holder, detailsDto);
                break;

            case ATMCheckListConstants.TYPE_RADIO_BUTTON:

                holder.layoutSpinnerOptions.setVisibility(View.GONE);
                holder.editTextAnswer.setVisibility(View.GONE);
                holder.radioGroupOptions.setVisibility(View.VISIBLE);
                holder.layoutCheckBoxes.setVisibility(View.GONE);

                prepareRadioButtons(holder, detailsDto);
                break;

            case ATMCheckListConstants.TYPE_TEXTBOX_INT:

                holder.layoutSpinnerOptions.setVisibility(View.GONE);
                holder.editTextAnswer.setVisibility(View.VISIBLE);
                holder.editTextAnswer.setInputType(InputType.TYPE_CLASS_NUMBER);
                holder.editTextAnswer.setLines(2);
                holder.radioGroupOptions.setVisibility(View.GONE);
                holder.layoutCheckBoxes.setVisibility(View.GONE);

                break;

            default:
                holder.layoutSpinnerOptions.setVisibility(View.GONE);
                holder.editTextAnswer.setVisibility(View.VISIBLE);
                holder.editTextAnswer.setInputType(InputType.TYPE_CLASS_TEXT);
                holder.editTextAnswer.setLines(2);
                holder.radioGroupOptions.setVisibility(View.GONE);
                holder.layoutCheckBoxes.setVisibility(View.GONE);
                break;

        }

        //Add EditTextTextChangedListener
        holder.editTextAnswer.setTag(detailsDto);
        holder.editTextAnswer.addTextChangedListener(new MyTextWatcher(itemView));

        //Proof Photo
        if (!TextUtils.isEmpty(detailsDto.getIsImg()) && detailsDto.getIsImg().equalsIgnoreCase("1")) {
            holder.layoutProofPhoto.setVisibility(View.VISIBLE);
        } else {
            holder.layoutProofPhoto.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(detailsDto.getProofPicBase64())) {
            Bitmap bitmap = CommonUtils.StringToBitMap(detailsDto.getProofPicBase64());
            Glide.with(context).asBitmap().load(bitmap).into(holder.imgProofImage);

        } else if (!TextUtils.isEmpty(detailsDto.getImg())) {
            String picUrl = Constants.DownloadImageUrl + detailsDto.getImg();
            Glide.with(context)
                    .load(picUrl)
                    .apply(new RequestOptions()
                            .error(R.drawable.ic_camera_alt_black_72dp)
                            .placeholder(R.drawable.ic_camera_alt_black_72dp)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))

                    .into(holder.imgProofImage);
        }
        holder.imgProofImage.setTag(position);
        holder.imgProofImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                ATMTechLiveCheckListDto dto = atmTechLiveCheckList.get(pos);
                mListener.cameraClick(pos, dto);
            }
        });

        holder.txtQuestionName.setTag(position);
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //do nothing
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //do nothing
        }

        public void afterTextChanged(Editable s) {
            String data = s.toString().trim();
            EditText editText = (EditText) view.findViewById(R.id.editTextAnswer);
            ATMTechLiveCheckListDto obj = (ATMTechLiveCheckListDto) editText.getTag();

            if (data.length() >= 3) {
                editText.setError(null);
            }

            obj.setAnswerTxt(data);
        }
    }

    private void prepareRadioButtons(ATMCheckListAdapter.MyViewHolder holder, ATMTechLiveCheckListDto detailsDto) {
        holder.radioGroupOptions.removeAllViews();
        holder.radioGroupOptions.setOrientation(LinearLayout.VERTICAL);
        holder.radioGroupOptions.setTag(detailsDto);

        for (int i = 0; i < detailsDto.optionsList.size(); i++) {

            OptionsDto optionsDto = detailsDto.optionsList.get(i);

            RadioButton rbn = new RadioButton(context);
            rbn.setId(View.generateViewId());
            rbn.setText(optionsDto.getName());
            rbn.setButtonTintList(colorStateList);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT, 1f);
            rbn.setLayoutParams(params);
            rbn.setTag(optionsDto);
            holder.radioGroupOptions.addView(rbn);

            if (!TextUtils.isEmpty(optionsDto.getId()) && detailsDto.selectedAnsList.containsKey(Integer.parseInt(optionsDto.getId()))) {
                rbn.setChecked(true);
            } else {
                rbn.setChecked(false);
            }
        }

        holder.radioGroupOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                ATMTechLiveCheckListDto quesDto = (ATMTechLiveCheckListDto) group.getTag();
                OptionsDto dto = (OptionsDto) radioButton.getTag();

                if (dto != null) {
                    holder.txtQuestionName.setError(null);
                    setChkRadioData(ATMCheckListConstants.TYPE_RADIO_BUTTON, dto, quesDto, true);
                }
            }
        });
    }

    public int getSelectedPos(List<OptionsDto> spinnerDtoList, ATMTechLiveCheckListDto detailsDto) {

        if (detailsDto.selectedAnsList.size() == 0 || spinnerDtoList == null)
            return 0;

        if (detailsDto.selectedAnsList.entrySet().size() == 0) {
            return 0;
        }

        OptionsDto optionsDto = (OptionsDto) detailsDto.selectedAnsList.entrySet().iterator().next().getValue();
        for (int i = 0; i < spinnerDtoList.size(); i++) {
            if (optionsDto.getId().equalsIgnoreCase(spinnerDtoList.get(i).getId()))
                return i;
        }
        return 0;
    }

    private void prepareSpinnerDetails(ATMCheckListAdapter.MyViewHolder holder, ATMTechLiveCheckListDto detailsDto) {
        holder.spinnerOptions.setTag(detailsDto);
        holder.spinnerOptions.setAdapter(new ArrayAdapter<OptionsDto>(context, android.R.layout.simple_spinner_dropdown_item, detailsDto.optionsList));
        int pos = getSelectedPos(detailsDto.optionsList, detailsDto);
        holder.spinnerOptions.setSelection(pos);
        holder.spinnerOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int Id = parent.getId();
                if (Id == R.id.spinnerOptions) {
                    if (position >= 0) {
                        ATMTechLiveCheckListDto quesDto = (ATMTechLiveCheckListDto) holder.spinnerOptions.getTag();
                        OptionsDto dto = (OptionsDto) holder.spinnerOptions.getItemAtPosition(position);
                        setChkRadioData(ATMCheckListConstants.TYPE_RADIO_BUTTON, dto, quesDto, true);

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void prepareCheckBoxes(ATMCheckListAdapter.MyViewHolder holder, ATMTechLiveCheckListDto detailsDto) {

        holder.layoutCheckBoxes.removeAllViews();
        holder.layoutCheckBoxes.setOrientation(LinearLayout.VERTICAL);
        holder.layoutCheckBoxes.setTag(detailsDto);

        for (int i = 0; i < detailsDto.optionsList.size(); i++) {

            OptionsDto optionsDto = detailsDto.optionsList.get(i);

            CheckBox chk = new CheckBox(context);
            chk.setId(View.generateViewId());
            chk.setText(optionsDto.getName());
            chk.setButtonTintList(colorStateList);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            chk.setLayoutParams(params);
            chk.setTag(optionsDto);
            holder.layoutCheckBoxes.addView(chk);

            if (!TextUtils.isEmpty(optionsDto.getId()) && detailsDto.selectedAnsList.containsKey(Integer.parseInt(optionsDto.getId()))) {
                chk.setChecked(true);
            } else {
                chk.setChecked(false);
            }

            chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean IsChecked) {
                    ATMTechLiveCheckListDto quesDto = (ATMTechLiveCheckListDto) holder.layoutCheckBoxes.getTag();
                    OptionsDto dto = (OptionsDto) compoundButton.getTag();
                    holder.txtQuestionName.setError(null);
                    setChkRadioData(ATMCheckListConstants.TYPE_CHECKBOX, dto, quesDto, IsChecked);
                    if (IsChecked) {
                        // Toast.makeText(context, "Selected: Ques: " + quesDto.getQuestion() + " Ans:" + dto.getName(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return atmTechLiveCheckList.size();
    }

    private void setChkRadioData(String questionType, OptionsDto optionsDto, ATMTechLiveCheckListDto detailsDto, boolean IsTobeAdded) {

        if (TextUtils.isEmpty(optionsDto.getId())) {
            return;
        }

        int optionId = Integer.parseInt(optionsDto.getId());

        if (questionType.equalsIgnoreCase(ATMCheckListConstants.TYPE_RADIO_BUTTON) || questionType.equalsIgnoreCase(ATMCheckListConstants.TYPE_DROP_DOWN)) {

            detailsDto.selectedAnsList = new HashMap<>();
            detailsDto.selectedAnsList.put(optionId, optionsDto);

        } else {
            if (IsTobeAdded) {
                detailsDto.selectedAnsList.put(optionId, optionsDto);
            } else {
                detailsDto.selectedAnsList.remove(optionId);
            }
        }
    }

    public List<ATMTechLiveCheckListDto> getAtmTechLiveCheckList() {
        return atmTechLiveCheckList;
    }
}
