package biz.dealnote.powercodetestapp.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import biz.dealnote.powercodetestapp.R;

public class InputTextDialog {

    private Context context;
    private int inputType;
    private int titleRes;
    private String value;
    private boolean allowEmpty;
    private Callback callback;
    private Integer hint;

    private InputTextDialog() {
        // not instantiate class
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(titleRes);
        View view = View.inflate(context, R.layout.dialog_enter_text, null);

        final EditText input = view.findViewById(R.id.editText);
        input.setText(value);
        input.setSelection(input.getText().length());

        if (hint != null) {
            input.setHint(hint);
        }

        input.setInputType(inputType);
        builder.setView(view);
        builder.setPositiveButton(R.string.button_ok, null);
        builder.setNegativeButton(R.string.button_cancel, (dialog, which) -> dialog.cancel());

        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(dialog -> {
            Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);

            b.setOnClickListener(view1 -> {
                input.setError(null);
                String newValue = input.getText().toString().trim();

                if (TextUtils.isEmpty(newValue) && !allowEmpty) {
                    input.setError(context.getString(R.string.field_is_required));
                    input.requestFocus();
                } else {
                    hideSoftKeyboard(input);

                    if (callback != null) {
                        callback.onChanged(newValue);
                    }

                    alertDialog.dismiss();
                }
            });
        });

        alertDialog.show();

        input.post(() -> {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
        });
    }

    private static void hideSoftKeyboard(View focusedView) {
        InputMethodManager imm = (InputMethodManager) focusedView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }
    }

    public interface Callback {
        void onChanged(String newValue);
    }

    public static class Builder {

        private Context context;
        private int inputType;
        private int titleRes;
        private String value;
        private boolean allowEmpty;
        private Callback callback;
        private Integer hint;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setInputType(int inputType) {
            this.inputType = inputType;
            return this;
        }

        public Builder setTitleRes(int titleRes) {
            this.titleRes = titleRes;
            return this;
        }

        public Builder setValue(String value) {
            this.value = value;
            return this;
        }

        public Builder setAllowEmpty(boolean allowEmpty) {
            this.allowEmpty = allowEmpty;
            return this;
        }

        public Builder setCallback(Callback callback) {
            this.callback = callback;
            return this;
        }

        public InputTextDialog create() {
            InputTextDialog inputTextDialog = new InputTextDialog();
            inputTextDialog.context = context;
            inputTextDialog.inputType = inputType;
            inputTextDialog.titleRes = titleRes;
            inputTextDialog.value = value;
            inputTextDialog.allowEmpty = allowEmpty;
            inputTextDialog.callback = callback;
            inputTextDialog.hint = hint;
            return inputTextDialog;
        }

        public Builder setHint(Integer hint) {
            this.hint = hint;
            return this;
        }

        public void show() {
            create().show();
        }
    }
}