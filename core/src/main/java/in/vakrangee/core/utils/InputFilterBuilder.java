package in.vakrangee.core.utils;

import android.text.InputFilter;
import android.text.Spanned;

public class InputFilterBuilder {

    public static InputFilter getCharacterAndDigitFilter() {
        return new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.isLetterOrDigit(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };
    }

}
