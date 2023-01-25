package org.hwca.dhis2sms.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DynamincViews {
    private LinearLayout.LayoutParams textViewParams;
    private LinearLayout.LayoutParams editTextParams;

    public DynamincViews() {
        editTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textViewParams.setMargins(5, 5, 5, 0);
        editTextParams.setMargins(10, 0, 10, 10);
    }

    public TextView generateTextView(Context context, String text) {
        final TextView textView = new TextView(context);
        textView.setLayoutParams(textViewParams);
        textView.setTextSize(15);
        textView.setTextColor(Color.rgb(0, 0, 0));
        textView.setText(text);
        textView.setMaxEms(8);
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        textView.setPadding(15, 10, 15, 10);

        return textView;
    }

    public EditText generateEditText(final Context context, final int id, final String hint) {
        final EditText editText = new EditText(context);
        editText.setId(id);
        editText.setMinEms(2);
        editText.setTextSize(15);
        editText.setHintTextColor(Color.rgb(50, 50, 50));
        editText.setTextColor(Color.rgb(50, 50, 50));
        editText.setBackgroundColor(Color.rgb(255, 255, 255));
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setLayoutParams(editTextParams);
        editText.setHint(hint);
        editText.setTypeface(editText.getTypeface(), Typeface.ITALIC);
        editText.setText(null);

        return editText;
    }
}
