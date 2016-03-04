package com.example.ibulatov.contactsprovider;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

public class EmailEditDialogFragment extends DialogFragment {

    public static final String LOG_TAG = "EmailEditDialogFragment";

    public static final String CARRIAGE_START_POS = "start";
    public static final String CARRIAGE_END_POS = "end";
    public static final String SAVED_TEXT = "text";

    public static final String EMAIL_ARG = "email";
    public static final String CONTACT_ID_ARG = "contactId";

    public static final String DEFAULT_EMAIL_VALUE = "";
    public static final String DEFAULT_CONTACT_ID_VALUE = "-1";

    private EditText mEmailEditText;

    public static DialogFragment newInstance(String contactId, String email) {
        DialogFragment fragment = new EmailEditDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EMAIL_ARG, email);
        bundle.putString(CONTACT_ID_ARG, contactId);
        fragment.setArguments(bundle);
        return fragment;
    }

    public String getEmailArg() {
        return getArg(EMAIL_ARG, DEFAULT_EMAIL_VALUE);
    }

    public String getContactIdArg() {
        return getArg(CONTACT_ID_ARG, DEFAULT_CONTACT_ID_VALUE);
    }

    private String getArg(String argument, String defaultValue) {
        Bundle bundle = getArguments();
        if(bundle != null) {
            return bundle.getString(argument);
        }
        return defaultValue;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_body, null);
        mEmailEditText = (EditText) view.findViewById(R.id.mail_editText);

        if(savedInstanceState != null) {

            String text = savedInstanceState.getString(SAVED_TEXT);
            int start = savedInstanceState.getInt(CARRIAGE_START_POS);
            int end = savedInstanceState.getInt(CARRIAGE_END_POS);

            mEmailEditText.setText(text);
            mEmailEditText.setSelection(start, end);

        } else {

            String email = getEmailArg();
            mEmailEditText.setText(email);
            mEmailEditText.setSelection(email.length());
        }

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.change_email_str)
                .setView(view)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            ContactsProviderUtils.updateContactsEmail(getActivity().getContentResolver(), getContactIdArg(), mEmailEditText.getText().toString());
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create();

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED);

        return dialog;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(SAVED_TEXT, mEmailEditText.getText().toString());
        outState.putInt(CARRIAGE_START_POS, mEmailEditText.getSelectionStart());
        outState.putInt(CARRIAGE_END_POS, mEmailEditText.getSelectionEnd());
    }


}
