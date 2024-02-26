package in.vakrangee.core.utils;

import android.text.Editable;
import android.text.TextWatcher;

public class EditTextWatcher implements TextWatcher {

    private IEditextData iEditextData;

    public interface IEditextData {
        public void getEditTextData(String data);
    }

    public EditTextWatcher(IEditextData iEditextData) {
        this.iEditextData = iEditextData;
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //do nothing
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //do nothing
    }

    public void afterTextChanged(Editable s) {
        String data = s.toString().trim();
        iEditextData.getEditTextData(data);

    }
}
